package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class ContentObserverController extends com.android.server.job.controllers.StateController {
    private static final boolean DEBUG;
    private static final int MAX_URIS_REPORTED = 50;
    private static final java.lang.String TAG = "JobScheduler.ContentObserver";
    private static final int URIS_URGENT_THRESHOLD = 40;
    final android.os.Handler mHandler;
    final android.util.SparseArray<android.util.ArrayMap<android.app.job.JobInfo.TriggerContentUri, com.android.server.job.controllers.ContentObserverController.ObserverInstance>> mObservers;
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mTrackedTasks;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    public ContentObserverController(com.android.server.job.JobSchedulerService service) {
        super(service);
        this.mTrackedTasks = new android.util.ArraySet<>();
        this.mObservers = new android.util.SparseArray<>();
        this.mHandler = new android.os.Handler(com.android.server.AppSchedulingModuleThread.get().getLooper());
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus taskStatus, com.android.server.job.controllers.JobStatus lastJob) {
        if (taskStatus.hasContentTriggerConstraint()) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (taskStatus.contentObserverJobInstance == null) {
                taskStatus.contentObserverJobInstance = new com.android.server.job.controllers.ContentObserverController.JobInstance(taskStatus);
            }
            if (DEBUG) {
                android.util.Slog.i(TAG, "Tracking content-trigger job " + taskStatus);
            }
            this.mTrackedTasks.add(taskStatus);
            taskStatus.setTrackingController(4);
            boolean havePendingUris = false;
            if (taskStatus.contentObserverJobInstance.mChangedAuthorities != null) {
                havePendingUris = true;
            }
            if (taskStatus.changedAuthorities != null) {
                havePendingUris = true;
                if (taskStatus.contentObserverJobInstance.mChangedAuthorities == null) {
                    taskStatus.contentObserverJobInstance.mChangedAuthorities = new android.util.ArraySet<>();
                }
                for (java.lang.String auth : taskStatus.changedAuthorities) {
                    taskStatus.contentObserverJobInstance.mChangedAuthorities.add(auth);
                }
                if (taskStatus.changedUris != null) {
                    if (taskStatus.contentObserverJobInstance.mChangedUris == null) {
                        taskStatus.contentObserverJobInstance.mChangedUris = new android.util.ArraySet<>();
                    }
                    for (android.net.Uri uri : taskStatus.changedUris) {
                        taskStatus.contentObserverJobInstance.mChangedUris.add(uri);
                    }
                }
            }
            taskStatus.changedAuthorities = null;
            taskStatus.changedUris = null;
            taskStatus.setContentTriggerConstraintSatisfied(nowElapsed, havePendingUris);
        }
        if (lastJob != null && lastJob.contentObserverJobInstance != null) {
            lastJob.contentObserverJobInstance.detachLocked();
            lastJob.contentObserverJobInstance = null;
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForExecutionLocked(com.android.server.job.controllers.JobStatus taskStatus) {
        if (taskStatus.hasContentTriggerConstraint() && taskStatus.contentObserverJobInstance != null) {
            taskStatus.changedUris = taskStatus.contentObserverJobInstance.mChangedUris;
            taskStatus.changedAuthorities = taskStatus.contentObserverJobInstance.mChangedAuthorities;
            taskStatus.contentObserverJobInstance.mChangedUris = null;
            taskStatus.contentObserverJobInstance.mChangedAuthorities = null;
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void unprepareFromExecutionLocked(com.android.server.job.controllers.JobStatus taskStatus) {
        if (taskStatus.hasContentTriggerConstraint() && taskStatus.contentObserverJobInstance != null) {
            if (taskStatus.contentObserverJobInstance.mChangedUris == null) {
                taskStatus.contentObserverJobInstance.mChangedUris = taskStatus.changedUris;
            } else {
                taskStatus.contentObserverJobInstance.mChangedUris.addAll((android.util.ArraySet<? extends android.net.Uri>) taskStatus.changedUris);
            }
            if (taskStatus.contentObserverJobInstance.mChangedAuthorities == null) {
                taskStatus.contentObserverJobInstance.mChangedAuthorities = taskStatus.changedAuthorities;
            } else {
                taskStatus.contentObserverJobInstance.mChangedAuthorities.addAll((android.util.ArraySet<? extends java.lang.String>) taskStatus.changedAuthorities);
            }
            taskStatus.changedUris = null;
            taskStatus.changedAuthorities = null;
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus taskStatus, com.android.server.job.controllers.JobStatus incomingJob) {
        if (taskStatus.clearTrackingController(4)) {
            this.mTrackedTasks.remove(taskStatus);
            if (taskStatus.contentObserverJobInstance != null) {
                taskStatus.contentObserverJobInstance.unscheduleLocked();
                if (incomingJob != null) {
                    if (taskStatus.contentObserverJobInstance != null && taskStatus.contentObserverJobInstance.mChangedAuthorities != null) {
                        if (incomingJob.contentObserverJobInstance == null) {
                            incomingJob.contentObserverJobInstance = new com.android.server.job.controllers.ContentObserverController.JobInstance(incomingJob);
                        }
                        incomingJob.contentObserverJobInstance.mChangedAuthorities = taskStatus.contentObserverJobInstance.mChangedAuthorities;
                        incomingJob.contentObserverJobInstance.mChangedUris = taskStatus.contentObserverJobInstance.mChangedUris;
                        taskStatus.contentObserverJobInstance.mChangedAuthorities = null;
                        taskStatus.contentObserverJobInstance.mChangedUris = null;
                    }
                } else {
                    taskStatus.contentObserverJobInstance.detachLocked();
                    taskStatus.contentObserverJobInstance = null;
                }
            }
            if (DEBUG) {
                android.util.Slog.i(TAG, "No longer tracking job " + taskStatus);
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void rescheduleForFailureLocked(com.android.server.job.controllers.JobStatus newJob, com.android.server.job.controllers.JobStatus failureToReschedule) {
        if (failureToReschedule.hasContentTriggerConstraint() && newJob.hasContentTriggerConstraint()) {
            newJob.changedAuthorities = failureToReschedule.changedAuthorities;
            newJob.changedUris = failureToReschedule.changedUris;
        }
    }

    final class ObserverInstance extends android.database.ContentObserver {
        final android.util.ArraySet<com.android.server.job.controllers.ContentObserverController.JobInstance> mJobs;
        final android.app.job.JobInfo.TriggerContentUri mUri;
        final int mUserId;

        public ObserverInstance(android.os.Handler handler, android.app.job.JobInfo.TriggerContentUri uri, int userId) {
            super(handler);
            this.mJobs = new android.util.ArraySet<>();
            this.mUri = uri;
            this.mUserId = userId;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (com.android.server.job.controllers.ContentObserverController.DEBUG) {
                android.util.Slog.i(com.android.server.job.controllers.ContentObserverController.TAG, "onChange(self=" + selfChange + ") for " + uri + " when mUri=" + this.mUri + " mUserId=" + this.mUserId);
            }
            synchronized (com.android.server.job.controllers.ContentObserverController.this.mLock) {
                int N = this.mJobs.size();
                for (int i = 0; i < N; i++) {
                    com.android.server.job.controllers.ContentObserverController.JobInstance inst = this.mJobs.valueAt(i);
                    if (inst.mChangedUris == null) {
                        inst.mChangedUris = new android.util.ArraySet<>();
                    }
                    if (inst.mChangedUris.size() < 50) {
                        inst.mChangedUris.add(uri);
                    }
                    if (inst.mChangedAuthorities == null) {
                        inst.mChangedAuthorities = new android.util.ArraySet<>();
                    }
                    inst.mChangedAuthorities.add(uri.getAuthority());
                    inst.scheduleLocked();
                }
            }
        }
    }

    static final class TriggerRunnable implements java.lang.Runnable {
        final com.android.server.job.controllers.ContentObserverController.JobInstance mInstance;

        TriggerRunnable(com.android.server.job.controllers.ContentObserverController.JobInstance instance) {
            this.mInstance = instance;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mInstance.trigger();
        }
    }

    final class JobInstance {
        android.util.ArraySet<java.lang.String> mChangedAuthorities;
        android.util.ArraySet<android.net.Uri> mChangedUris;
        final com.android.server.job.controllers.JobStatus mJobStatus;
        boolean mTriggerPending;
        final java.util.ArrayList<com.android.server.job.controllers.ContentObserverController.ObserverInstance> mMyObservers = new java.util.ArrayList<>();
        final java.lang.Runnable mExecuteRunner = new com.android.server.job.controllers.ContentObserverController.TriggerRunnable(this);
        final java.lang.Runnable mTimeoutRunner = new com.android.server.job.controllers.ContentObserverController.TriggerRunnable(this);

        JobInstance(com.android.server.job.controllers.JobStatus jobStatus) {
            this.mJobStatus = jobStatus;
            android.app.job.JobInfo.TriggerContentUri[] uris = jobStatus.getJob().getTriggerContentUris();
            int sourceUserId = jobStatus.getSourceUserId();
            android.util.ArrayMap<android.app.job.JobInfo.TriggerContentUri, com.android.server.job.controllers.ContentObserverController.ObserverInstance> observersOfUser = com.android.server.job.controllers.ContentObserverController.this.mObservers.get(sourceUserId);
            if (observersOfUser == null) {
                observersOfUser = new android.util.ArrayMap<>();
                com.android.server.job.controllers.ContentObserverController.this.mObservers.put(sourceUserId, observersOfUser);
            }
            if (uris != null) {
                for (android.app.job.JobInfo.TriggerContentUri uri : uris) {
                    com.android.server.job.controllers.ContentObserverController.ObserverInstance obs = observersOfUser.get(uri);
                    if (obs == null) {
                        obs = com.android.server.job.controllers.ContentObserverController.this.new ObserverInstance(com.android.server.job.controllers.ContentObserverController.this.mHandler, uri, jobStatus.getSourceUserId());
                        observersOfUser.put(uri, obs);
                        boolean andDescendants = (uri.getFlags() & 1) != 0;
                        if (com.android.server.job.controllers.ContentObserverController.DEBUG) {
                            android.util.Slog.v(com.android.server.job.controllers.ContentObserverController.TAG, "New observer " + obs + " for " + uri.getUri() + " andDescendants=" + andDescendants + " sourceUserId=" + sourceUserId);
                        }
                        com.android.server.job.controllers.ContentObserverController.this.mContext.getContentResolver().registerContentObserver(uri.getUri(), andDescendants, obs, sourceUserId);
                    } else if (com.android.server.job.controllers.ContentObserverController.DEBUG) {
                        android.util.Slog.v(com.android.server.job.controllers.ContentObserverController.TAG, "Reusing existing observer " + obs + " for " + uri.getUri() + " andDescendants=" + ((uri.getFlags() & 1) != 0));
                    }
                    obs.mJobs.add(this);
                    this.mMyObservers.add(obs);
                }
            }
        }

        void trigger() {
            boolean reportChange = false;
            synchronized (com.android.server.job.controllers.ContentObserverController.this.mLock) {
                if (this.mTriggerPending) {
                    long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                    if (this.mJobStatus.setContentTriggerConstraintSatisfied(nowElapsed, true)) {
                        reportChange = true;
                    }
                    unscheduleLocked();
                }
            }
            if (reportChange) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJob = new android.util.ArraySet<>();
                changedJob.add(this.mJobStatus);
                com.android.server.job.controllers.ContentObserverController.this.mStateChangedListener.onControllerStateChanged(changedJob);
            }
        }

        void scheduleLocked() {
            if (!this.mTriggerPending) {
                this.mTriggerPending = true;
                com.android.server.job.controllers.ContentObserverController.this.mHandler.postDelayed(this.mTimeoutRunner, this.mJobStatus.getTriggerContentMaxDelay());
            }
            com.android.server.job.controllers.ContentObserverController.this.mHandler.removeCallbacks(this.mExecuteRunner);
            if (this.mChangedUris.size() >= 40) {
                com.android.server.job.controllers.ContentObserverController.this.mHandler.post(this.mExecuteRunner);
            } else {
                com.android.server.job.controllers.ContentObserverController.this.mHandler.postDelayed(this.mExecuteRunner, this.mJobStatus.getTriggerContentUpdateDelay());
            }
        }

        void unscheduleLocked() {
            if (this.mTriggerPending) {
                com.android.server.job.controllers.ContentObserverController.this.mHandler.removeCallbacks(this.mExecuteRunner);
                com.android.server.job.controllers.ContentObserverController.this.mHandler.removeCallbacks(this.mTimeoutRunner);
                this.mTriggerPending = false;
            }
        }

        void detachLocked() {
            int N = this.mMyObservers.size();
            for (int i = 0; i < N; i++) {
                com.android.server.job.controllers.ContentObserverController.ObserverInstance obs = this.mMyObservers.get(i);
                obs.mJobs.remove(this);
                if (obs.mJobs.size() == 0) {
                    if (com.android.server.job.controllers.ContentObserverController.DEBUG) {
                        android.util.Slog.i(com.android.server.job.controllers.ContentObserverController.TAG, "Unregistering observer " + obs + " for " + obs.mUri.getUri());
                    }
                    com.android.server.job.controllers.ContentObserverController.this.mContext.getContentResolver().unregisterContentObserver(obs);
                    android.util.ArrayMap<android.app.job.JobInfo.TriggerContentUri, com.android.server.job.controllers.ContentObserverController.ObserverInstance> observerOfUser = com.android.server.job.controllers.ContentObserverController.this.mObservers.get(obs.mUserId);
                    if (observerOfUser != null) {
                        observerOfUser.remove(obs.mUri);
                    }
                }
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        int N;
        int N2;
        com.android.server.job.controllers.ContentObserverController contentObserverController = this;
        java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate2 = predicate;
        for (int i = 0; i < contentObserverController.mTrackedTasks.size(); i++) {
            com.android.server.job.controllers.JobStatus js = contentObserverController.mTrackedTasks.valueAt(i);
            if (predicate2.test(js)) {
                pw.print("#");
                js.printUniqueId(pw);
                pw.print(" from ");
                android.os.UserHandle.formatUid(pw, js.getSourceUid());
                pw.println();
            }
        }
        pw.println();
        int N3 = contentObserverController.mObservers.size();
        if (N3 > 0) {
            pw.println("Observers:");
            pw.increaseIndent();
            int userIdx = 0;
            while (userIdx < N3) {
                int userId = contentObserverController.mObservers.keyAt(userIdx);
                android.util.ArrayMap<android.app.job.JobInfo.TriggerContentUri, com.android.server.job.controllers.ContentObserverController.ObserverInstance> observersOfUser = contentObserverController.mObservers.get(userId);
                int numbOfObserversPerUser = observersOfUser.size();
                int observerIdx = 0;
                while (observerIdx < numbOfObserversPerUser) {
                    com.android.server.job.controllers.ContentObserverController.ObserverInstance obs = observersOfUser.valueAt(observerIdx);
                    int M = obs.mJobs.size();
                    boolean shouldDump = false;
                    int j = 0;
                    while (true) {
                        if (j >= M) {
                            break;
                        }
                        if (!predicate2.test(obs.mJobs.valueAt(j).mJobStatus)) {
                            j++;
                        } else {
                            shouldDump = true;
                            break;
                        }
                    }
                    if (!shouldDump) {
                        N = N3;
                    } else {
                        android.app.job.JobInfo.TriggerContentUri trigger = observersOfUser.keyAt(observerIdx);
                        pw.print(trigger.getUri());
                        pw.print(" 0x");
                        pw.print(java.lang.Integer.toHexString(trigger.getFlags()));
                        pw.print(" (");
                        pw.print(java.lang.System.identityHashCode(obs));
                        pw.println("):");
                        pw.increaseIndent();
                        pw.println("Jobs:");
                        pw.increaseIndent();
                        int j2 = 0;
                        while (j2 < M) {
                            com.android.server.job.controllers.ContentObserverController.JobInstance inst = obs.mJobs.valueAt(j2);
                            pw.print("#");
                            android.app.job.JobInfo.TriggerContentUri trigger2 = trigger;
                            inst.mJobStatus.printUniqueId(pw);
                            pw.print(" from ");
                            android.os.UserHandle.formatUid(pw, inst.mJobStatus.getSourceUid());
                            if (inst.mChangedAuthorities != null) {
                                pw.println(":");
                                pw.increaseIndent();
                                if (!inst.mTriggerPending) {
                                    N2 = N3;
                                } else {
                                    pw.print("Trigger pending: update=");
                                    N2 = N3;
                                    android.util.TimeUtils.formatDuration(inst.mJobStatus.getTriggerContentUpdateDelay(), pw);
                                    pw.print(", max=");
                                    android.util.TimeUtils.formatDuration(inst.mJobStatus.getTriggerContentMaxDelay(), pw);
                                    pw.println();
                                }
                                pw.println("Changed Authorities:");
                                for (int k = 0; k < inst.mChangedAuthorities.size(); k++) {
                                    pw.println(inst.mChangedAuthorities.valueAt(k));
                                }
                                if (inst.mChangedUris != null) {
                                    pw.println("          Changed URIs:");
                                    for (int k2 = 0; k2 < inst.mChangedUris.size(); k2++) {
                                        pw.println(inst.mChangedUris.valueAt(k2));
                                    }
                                }
                                pw.decreaseIndent();
                            } else {
                                N2 = N3;
                                pw.println();
                            }
                            j2++;
                            trigger = trigger2;
                            N3 = N2;
                        }
                        N = N3;
                        pw.decreaseIndent();
                        pw.decreaseIndent();
                    }
                    observerIdx++;
                    predicate2 = predicate;
                    N3 = N;
                }
                userIdx++;
                contentObserverController = this;
                predicate2 = predicate;
            }
            pw.decreaseIndent();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        int numbOfObserversPerUser;
        int userId;
        long token;
        long mToken;
        int m;
        long oToken;
        android.util.ArrayMap<android.app.job.JobInfo.TriggerContentUri, com.android.server.job.controllers.ContentObserverController.ObserverInstance> observersOfUser;
        int userIdx;
        long oToken2;
        com.android.server.job.controllers.ContentObserverController contentObserverController = this;
        java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate2 = predicate;
        long token2 = proto.start(fieldId);
        long mToken2 = proto.start(1146756268036L);
        for (int i = 0; i < contentObserverController.mTrackedTasks.size(); i++) {
            com.android.server.job.controllers.JobStatus js = contentObserverController.mTrackedTasks.valueAt(i);
            if (predicate2.test(js)) {
                long jsToken = proto.start(2246267895809L);
                js.writeToShortProto(proto, 1146756268033L);
                proto.write(1120986464258L, js.getSourceUid());
                proto.end(jsToken);
            }
        }
        int n = contentObserverController.mObservers.size();
        int userIdx2 = 0;
        while (userIdx2 < n) {
            long oToken3 = proto.start(2246267895810L);
            int userId2 = contentObserverController.mObservers.keyAt(userIdx2);
            proto.write(1120986464257L, userId2);
            android.util.ArrayMap<android.app.job.JobInfo.TriggerContentUri, com.android.server.job.controllers.ContentObserverController.ObserverInstance> observersOfUser2 = contentObserverController.mObservers.get(userId2);
            int numbOfObserversPerUser2 = observersOfUser2.size();
            int observerIdx = 0;
            while (observerIdx < numbOfObserversPerUser2) {
                com.android.server.job.controllers.ContentObserverController.ObserverInstance obs = observersOfUser2.valueAt(observerIdx);
                int userIdx3 = obs.mJobs.size();
                boolean shouldDump = false;
                int n2 = n;
                int n3 = 0;
                while (true) {
                    if (n3 >= userIdx3) {
                        numbOfObserversPerUser = numbOfObserversPerUser2;
                        userId = userId2;
                        break;
                    }
                    numbOfObserversPerUser = numbOfObserversPerUser2;
                    userId = userId2;
                    if (!predicate2.test(obs.mJobs.valueAt(n3).mJobStatus)) {
                        n3++;
                        numbOfObserversPerUser2 = numbOfObserversPerUser;
                        userId2 = userId;
                    } else {
                        shouldDump = true;
                        break;
                    }
                }
                if (!shouldDump) {
                    token = token2;
                    mToken = mToken2;
                    m = userIdx2;
                    oToken = oToken3;
                    observersOfUser = observersOfUser2;
                } else {
                    token = token2;
                    mToken = mToken2;
                    long tToken = proto.start(2246267895810L);
                    android.app.job.JobInfo.TriggerContentUri trigger = observersOfUser2.keyAt(observerIdx);
                    android.net.Uri u = trigger.getUri();
                    if (u != null) {
                        proto.write(1138166333441L, u.toString());
                    }
                    proto.write(1120986464258L, trigger.getFlags());
                    int j = 0;
                    android.net.Uri u2 = u;
                    while (j < userIdx3) {
                        long jToken = proto.start(2246267895811L);
                        com.android.server.job.controllers.ContentObserverController.JobInstance inst = obs.mJobs.valueAt(j);
                        int m2 = userIdx3;
                        android.util.ArrayMap<android.app.job.JobInfo.TriggerContentUri, com.android.server.job.controllers.ContentObserverController.ObserverInstance> observersOfUser3 = observersOfUser2;
                        android.net.Uri u3 = u2;
                        inst.mJobStatus.writeToShortProto(proto, 1146756268033L);
                        proto.write(1120986464258L, inst.mJobStatus.getSourceUid());
                        if (inst.mChangedAuthorities == null) {
                            proto.end(jToken);
                            userIdx = userIdx2;
                            oToken2 = oToken3;
                        } else {
                            if (!inst.mTriggerPending) {
                                userIdx = userIdx2;
                                oToken2 = oToken3;
                            } else {
                                userIdx = userIdx2;
                                oToken2 = oToken3;
                                proto.write(1112396529667L, inst.mJobStatus.getTriggerContentUpdateDelay());
                                proto.write(1112396529668L, inst.mJobStatus.getTriggerContentMaxDelay());
                            }
                            for (int k = 0; k < inst.mChangedAuthorities.size(); k++) {
                                proto.write(2237677961221L, inst.mChangedAuthorities.valueAt(k));
                            }
                            if (inst.mChangedUris != null) {
                                for (int k2 = 0; k2 < inst.mChangedUris.size(); k2++) {
                                    u3 = inst.mChangedUris.valueAt(k2);
                                    if (u3 != null) {
                                        proto.write(2237677961222L, u3.toString());
                                    }
                                }
                            }
                            proto.end(jToken);
                        }
                        u2 = u3;
                        j++;
                        userIdx2 = userIdx;
                        userIdx3 = m2;
                        observersOfUser2 = observersOfUser3;
                        oToken3 = oToken2;
                    }
                    m = userIdx2;
                    oToken = oToken3;
                    observersOfUser = observersOfUser2;
                    proto.end(tToken);
                }
                observerIdx++;
                predicate2 = predicate;
                userIdx2 = m;
                mToken2 = mToken;
                numbOfObserversPerUser2 = numbOfObserversPerUser;
                n = n2;
                userId2 = userId;
                token2 = token;
                observersOfUser2 = observersOfUser;
                oToken3 = oToken;
            }
            proto.end(oToken3);
            userIdx2++;
            contentObserverController = this;
            predicate2 = predicate;
            token2 = token2;
        }
        proto.end(mToken2);
        proto.end(token2);
    }
}
