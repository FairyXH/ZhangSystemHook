package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
class PendingJobQueue {
    private final android.util.Pools.Pool<com.android.server.job.PendingJobQueue.AppJobQueue> mAppJobQueuePool = new android.util.Pools.SimplePool(8);
    private final android.util.SparseArray<com.android.server.job.PendingJobQueue.AppJobQueue> mCurrentQueues = new android.util.SparseArray<>();
    private final java.util.PriorityQueue<com.android.server.job.PendingJobQueue.AppJobQueue> mOrderedQueues = new java.util.PriorityQueue<>(new java.util.Comparator() { // from class: com.android.server.job.PendingJobQueue$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
            return com.android.server.job.PendingJobQueue.lambda$new$0((com.android.server.job.PendingJobQueue.AppJobQueue) obj, (com.android.server.job.PendingJobQueue.AppJobQueue) obj2);
        }
    });
    private int mSize = 0;
    private boolean mOptimizeIteration = true;
    private int mPullCount = 0;
    private boolean mNeedToResetIterators = false;

    PendingJobQueue() {
    }

    static /* synthetic */ int lambda$new$0(com.android.server.job.PendingJobQueue.AppJobQueue ajq1, com.android.server.job.PendingJobQueue.AppJobQueue ajq2) {
        long t1 = ajq1.peekNextTimestamp();
        long t2 = ajq2.peekNextTimestamp();
        if (t1 == -1) {
            if (t2 == -1) {
                return 0;
            }
            return 1;
        }
        if (t2 == -1) {
            return -1;
        }
        int o1 = ajq1.peekNextOverrideState();
        int o2 = ajq2.peekNextOverrideState();
        if (o1 != o2) {
            return java.lang.Integer.compare(o2, o1);
        }
        return java.lang.Long.compare(t1, t2);
    }

    void add(com.android.server.job.controllers.JobStatus job) {
        com.android.server.job.PendingJobQueue.AppJobQueue ajq = getAppJobQueue(job.getSourceUid(), true);
        long prevTimestamp = ajq.peekNextTimestamp();
        ajq.add(job);
        this.mSize++;
        if (prevTimestamp != ajq.peekNextTimestamp()) {
            this.mOrderedQueues.remove(ajq);
            this.mOrderedQueues.offer(ajq);
        }
    }

    void addAll(android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs) {
        android.util.SparseArray<java.util.List<com.android.server.job.controllers.JobStatus>> jobsByUid = new android.util.SparseArray<>();
        for (int i = jobs.size() - 1; i >= 0; i--) {
            com.android.server.job.controllers.JobStatus job = jobs.valueAt(i);
            java.util.List<com.android.server.job.controllers.JobStatus> appJobs = jobsByUid.get(job.getSourceUid());
            if (appJobs == null) {
                appJobs = new java.util.ArrayList<>();
                jobsByUid.put(job.getSourceUid(), appJobs);
            }
            appJobs.add(job);
        }
        int i2 = jobsByUid.size();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            com.android.server.job.PendingJobQueue.AppJobQueue ajq = getAppJobQueue(jobsByUid.keyAt(i3), true);
            ajq.addAll(jobsByUid.valueAt(i3));
        }
        int i4 = this.mSize;
        this.mSize = i4 + jobs.size();
        this.mOrderedQueues.clear();
    }

    void clear() {
        this.mSize = 0;
        for (int i = this.mCurrentQueues.size() - 1; i >= 0; i--) {
            com.android.server.job.PendingJobQueue.AppJobQueue ajq = this.mCurrentQueues.valueAt(i);
            ajq.clear();
            this.mAppJobQueuePool.release(ajq);
        }
        this.mCurrentQueues.clear();
        this.mOrderedQueues.clear();
    }

    boolean contains(com.android.server.job.controllers.JobStatus job) {
        com.android.server.job.PendingJobQueue.AppJobQueue ajq = this.mCurrentQueues.get(job.getSourceUid());
        if (ajq == null) {
            return false;
        }
        return ajq.contains(job);
    }

    private com.android.server.job.PendingJobQueue.AppJobQueue getAppJobQueue(int uid, boolean create) {
        com.android.server.job.PendingJobQueue.AppJobQueue ajq = this.mCurrentQueues.get(uid);
        if (ajq == null && create) {
            ajq = (com.android.server.job.PendingJobQueue.AppJobQueue) this.mAppJobQueuePool.acquire();
            if (ajq == null) {
                ajq = new com.android.server.job.PendingJobQueue.AppJobQueue();
            }
            this.mCurrentQueues.put(uid, ajq);
        }
        return ajq;
    }

    com.android.server.job.controllers.JobStatus next() {
        if (this.mNeedToResetIterators) {
            this.mOrderedQueues.clear();
            for (int i = this.mCurrentQueues.size() - 1; i >= 0; i--) {
                com.android.server.job.PendingJobQueue.AppJobQueue ajq = this.mCurrentQueues.valueAt(i);
                ajq.resetIterator(0L);
                this.mOrderedQueues.offer(ajq);
            }
            this.mNeedToResetIterators = false;
            this.mPullCount = 0;
        } else if (this.mOrderedQueues.size() == 0) {
            for (int i2 = this.mCurrentQueues.size() - 1; i2 >= 0; i2--) {
                this.mOrderedQueues.offer(this.mCurrentQueues.valueAt(i2));
            }
            this.mPullCount = 0;
        }
        int numQueues = this.mOrderedQueues.size();
        if (numQueues == 0) {
            return null;
        }
        int pullLimit = this.mOptimizeIteration ? java.lang.Math.min(3, ((numQueues - 1) >>> 2) + 1) : 1;
        com.android.server.job.PendingJobQueue.AppJobQueue earliestQueue = this.mOrderedQueues.peek();
        if (earliestQueue == null) {
            return null;
        }
        com.android.server.job.controllers.JobStatus job = earliestQueue.next();
        int i3 = this.mPullCount + 1;
        this.mPullCount = i3;
        if (i3 >= pullLimit || ((job != null && earliestQueue.peekNextOverrideState() != job.overrideState) || earliestQueue.peekNextTimestamp() == -1)) {
            this.mOrderedQueues.poll();
            if (earliestQueue.peekNextTimestamp() != -1) {
                this.mOrderedQueues.offer(earliestQueue);
            }
            this.mPullCount = 0;
        }
        return job;
    }

    boolean remove(com.android.server.job.controllers.JobStatus job) {
        com.android.server.job.PendingJobQueue.AppJobQueue ajq = getAppJobQueue(job.getSourceUid(), false);
        if (ajq == null) {
            return false;
        }
        long prevTimestamp = ajq.peekNextTimestamp();
        if (!ajq.remove(job)) {
            return false;
        }
        this.mSize--;
        if (ajq.size() == 0) {
            this.mCurrentQueues.remove(job.getSourceUid());
            this.mOrderedQueues.remove(ajq);
            ajq.clear();
            this.mAppJobQueuePool.release(ajq);
        } else if (prevTimestamp != ajq.peekNextTimestamp()) {
            this.mOrderedQueues.remove(ajq);
            this.mOrderedQueues.offer(ajq);
        }
        return true;
    }

    void resetIterator() {
        this.mNeedToResetIterators = true;
    }

    void setOptimizeIteration(boolean optimize) {
        this.mOptimizeIteration = optimize;
    }

    int size() {
        return this.mSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class AppJobQueue {
        static final int NO_NEXT_OVERRIDE_STATE = -1;
        static final long NO_NEXT_TIMESTAMP = -1;
        private int mCurIndex;
        private final java.util.List<com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus> mJobs;
        private static final java.util.Comparator<com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus> sJobComparator = new java.util.Comparator() { // from class: com.android.server.job.PendingJobQueue$AppJobQueue$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.job.PendingJobQueue.AppJobQueue.lambda$static$0((com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus) obj, (com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus) obj2);
            }
        };
        private static final android.util.Pools.Pool<com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus> mAdjustedJobStatusPool = new android.util.Pools.SimplePool(16);

        private AppJobQueue() {
            this.mJobs = new java.util.ArrayList();
            this.mCurIndex = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        static class AdjustedJobStatus {
            public long adjustedEnqueueTime;
            public com.android.server.job.controllers.JobStatus job;

            private AdjustedJobStatus() {
            }

            void clear() {
                this.adjustedEnqueueTime = 0L;
                this.job = null;
            }
        }

        static /* synthetic */ int lambda$static$0(com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus aj1, com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus aj2) {
            com.android.server.job.controllers.JobStatus job1;
            com.android.server.job.controllers.JobStatus job2;
            int job1Priority;
            int job2Priority;
            if (aj1 == aj2 || (job1 = aj1.job) == (job2 = aj2.job)) {
                return 0;
            }
            if (job1.overrideState != job2.overrideState) {
                return java.lang.Integer.compare(job2.overrideState, job1.overrideState);
            }
            boolean job1UI = job1.getJob().isUserInitiated();
            boolean job2UI = job2.getJob().isUserInitiated();
            if (job1UI != job2UI) {
                return job1UI ? -1 : 1;
            }
            boolean job1EJ = job1.isRequestedExpeditedJob();
            boolean job2EJ = job2.isRequestedExpeditedJob();
            if (job1EJ != job2EJ) {
                return job1EJ ? -1 : 1;
            }
            if (java.util.Objects.equals(job1.getNamespace(), job2.getNamespace()) && (job1Priority = job1.getEffectivePriority()) != (job2Priority = job2.getEffectivePriority())) {
                return java.lang.Integer.compare(job2Priority, job1Priority);
            }
            if (job1.lastEvaluatedBias != job2.lastEvaluatedBias) {
                return java.lang.Integer.compare(job2.lastEvaluatedBias, job1.lastEvaluatedBias);
            }
            return java.lang.Long.compare(job1.enqueueTime, job2.enqueueTime);
        }

        void add(com.android.server.job.controllers.JobStatus jobStatus) {
            com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus adjustedJobStatus = (com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus) mAdjustedJobStatusPool.acquire();
            if (adjustedJobStatus == null) {
                adjustedJobStatus = new com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus();
            }
            adjustedJobStatus.adjustedEnqueueTime = jobStatus.enqueueTime;
            adjustedJobStatus.job = jobStatus;
            int where = java.util.Collections.binarySearch(this.mJobs, adjustedJobStatus, sJobComparator);
            if (where < 0) {
                where = ~where;
            }
            this.mJobs.add(where, adjustedJobStatus);
            if (where < this.mCurIndex) {
                this.mCurIndex = where;
            }
            if (where > 0) {
                long prevTimestamp = this.mJobs.get(where - 1).adjustedEnqueueTime;
                adjustedJobStatus.adjustedEnqueueTime = java.lang.Math.max(prevTimestamp, adjustedJobStatus.adjustedEnqueueTime);
            }
            int numJobs = this.mJobs.size();
            if (where < numJobs - 1) {
                for (int i = where; i < numJobs; i++) {
                    com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus ajs = this.mJobs.get(i);
                    if (adjustedJobStatus.adjustedEnqueueTime >= ajs.adjustedEnqueueTime) {
                        ajs.adjustedEnqueueTime = adjustedJobStatus.adjustedEnqueueTime;
                    } else {
                        return;
                    }
                }
            }
        }

        void addAll(java.util.List<com.android.server.job.controllers.JobStatus> jobs) {
            int earliestIndex = Integer.MAX_VALUE;
            for (int i = jobs.size() - 1; i >= 0; i--) {
                com.android.server.job.controllers.JobStatus job = jobs.get(i);
                com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus adjustedJobStatus = (com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus) mAdjustedJobStatusPool.acquire();
                if (adjustedJobStatus == null) {
                    adjustedJobStatus = new com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus();
                }
                adjustedJobStatus.adjustedEnqueueTime = job.enqueueTime;
                adjustedJobStatus.job = job;
                int where = java.util.Collections.binarySearch(this.mJobs, adjustedJobStatus, sJobComparator);
                if (where < 0) {
                    where = ~where;
                }
                this.mJobs.add(where, adjustedJobStatus);
                if (where < this.mCurIndex) {
                    this.mCurIndex = where;
                }
                earliestIndex = java.lang.Math.min(earliestIndex, where);
            }
            int numJobs = this.mJobs.size();
            for (int i2 = java.lang.Math.max(earliestIndex, 1); i2 < numJobs; i2++) {
                com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus ajs = this.mJobs.get(i2);
                com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus prev = this.mJobs.get(i2 - 1);
                ajs.adjustedEnqueueTime = java.lang.Math.max(ajs.adjustedEnqueueTime, prev.adjustedEnqueueTime);
            }
        }

        void clear() {
            this.mJobs.clear();
            this.mCurIndex = 0;
        }

        boolean contains(com.android.server.job.controllers.JobStatus job) {
            return indexOf(job) >= 0;
        }

        private int indexOf(com.android.server.job.controllers.JobStatus jobStatus) {
            int size = this.mJobs.size();
            for (int i = 0; i < size; i++) {
                com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus adjustedJobStatus = this.mJobs.get(i);
                if (adjustedJobStatus.job == jobStatus) {
                    return i;
                }
            }
            return -1;
        }

        com.android.server.job.controllers.JobStatus next() {
            if (this.mCurIndex >= this.mJobs.size()) {
                return null;
            }
            java.util.List<com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus> list = this.mJobs;
            int i = this.mCurIndex;
            this.mCurIndex = i + 1;
            return list.get(i).job;
        }

        int peekNextOverrideState() {
            if (this.mCurIndex >= this.mJobs.size()) {
                return -1;
            }
            return this.mJobs.get(this.mCurIndex).job.overrideState;
        }

        long peekNextTimestamp() {
            if (this.mCurIndex >= this.mJobs.size()) {
                return -1L;
            }
            return this.mJobs.get(this.mCurIndex).adjustedEnqueueTime;
        }

        boolean remove(com.android.server.job.controllers.JobStatus jobStatus) {
            int idx = indexOf(jobStatus);
            if (idx < 0) {
                return false;
            }
            com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus adjustedJobStatus = this.mJobs.remove(idx);
            adjustedJobStatus.clear();
            mAdjustedJobStatusPool.release(adjustedJobStatus);
            if (idx < this.mCurIndex) {
                this.mCurIndex--;
            }
            return true;
        }

        void resetIterator(long earliestEnqueueTime) {
            if (earliestEnqueueTime == 0 || this.mJobs.size() == 0) {
                this.mCurIndex = 0;
                return;
            }
            int low = 0;
            int high = this.mJobs.size() - 1;
            while (low < high) {
                int mid = (low + high) >>> 1;
                com.android.server.job.PendingJobQueue.AppJobQueue.AdjustedJobStatus midVal = this.mJobs.get(mid);
                if (midVal.adjustedEnqueueTime < earliestEnqueueTime) {
                    low = mid + 1;
                } else if (midVal.adjustedEnqueueTime > earliestEnqueueTime) {
                    high = mid - 1;
                } else {
                    high = mid;
                }
            }
            this.mCurIndex = high;
        }

        int size() {
            return this.mJobs.size();
        }
    }
}
