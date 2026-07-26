package com.android.server.location.eventlog;

/* JADX INFO: loaded from: classes2.dex */
public class LocalEventLog<T> {
    private static final int IS_FILLER_MASK = Integer.MIN_VALUE;
    private static final int TIME_DELTA_MASK = Integer.MAX_VALUE;
    int[] mEntries;
    long mLastLogTime;
    int mLogEndIndex;
    T[] mLogEvents;
    int mLogSize;
    long mModificationCount;
    long mStartTime;
    private final com.android.server.location.eventlog.LocalEventLog<T>.LocalEventLogWrapper mWrapper = new com.android.server.location.eventlog.LocalEventLog.LocalEventLogWrapper();
    private static final int IS_FILLER_OFFSET = countTrailingZeros(Integer.MIN_VALUE);
    private static final int TIME_DELTA_OFFSET = countTrailingZeros(Integer.MAX_VALUE);
    static final int MAX_TIME_DELTA = (1 << java.lang.Integer.bitCount(Integer.MAX_VALUE)) - 1;

    public interface LogConsumer<T> {
        void acceptLog(long j, T t);
    }

    private static int countTrailingZeros(int i) {
        int c = 0;
        while (i != 0 && (i & 1) == 0) {
            c++;
            i >>>= 1;
        }
        return c;
    }

    private static int createEntry(boolean z, int i) {
        com.android.internal.util.Preconditions.checkArgument(i >= 0 && i <= MAX_TIME_DELTA);
        return (((z ? 1 : 0) << IS_FILLER_OFFSET) & Integer.MIN_VALUE) | ((i << TIME_DELTA_OFFSET) & Integer.MAX_VALUE);
    }

    static int getTimeDelta(int entry) {
        return (Integer.MAX_VALUE & entry) >>> TIME_DELTA_OFFSET;
    }

    static boolean isFiller(int entry) {
        return (Integer.MIN_VALUE & entry) != 0;
    }

    public LocalEventLog(int i, java.lang.Class<T> cls) {
        com.android.internal.util.Preconditions.checkArgument(i > 0);
        this.mEntries = new int[i];
        this.mLogEvents = (T[]) ((java.lang.Object[]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) cls, i));
        this.mLogSize = 0;
        this.mLogEndIndex = 0;
        this.mStartTime = -1L;
        this.mLastLogTime = -1L;
    }

    protected synchronized void addLog(long time, T logEvent) {
        com.android.internal.util.Preconditions.checkArgument(logEvent != null);
        long delta = 0;
        if (!isEmpty()) {
            delta = time - this.mLastLogTime;
            if (delta < 0 || delta / ((long) MAX_TIME_DELTA) >= this.mEntries.length - 1) {
                clear();
                delta = 0;
            } else {
                while (delta >= MAX_TIME_DELTA) {
                    addLogEventInternal(true, MAX_TIME_DELTA, null);
                    delta -= (long) MAX_TIME_DELTA;
                }
            }
        }
        if (isEmpty()) {
            this.mStartTime = time;
            this.mLastLogTime = this.mStartTime;
            this.mModificationCount++;
        }
        addLogEventInternal(false, (int) delta, logEvent);
    }

    private void addLogEventInternal(boolean isFiller, int timeDelta, T logEvent) {
        boolean z = false;
        com.android.internal.util.Preconditions.checkArgument(isFiller || logEvent != null);
        if (this.mStartTime != -1 && this.mLastLogTime != -1) {
            z = true;
        }
        com.android.internal.util.Preconditions.checkState(z);
        if (this.mLogSize == this.mEntries.length) {
            this.mStartTime += (long) getTimeDelta(this.mEntries[startIndex()]);
            this.mModificationCount++;
        } else {
            this.mLogSize++;
        }
        this.mEntries[this.mLogEndIndex] = createEntry(isFiller, timeDelta);
        this.mLogEvents[this.mLogEndIndex] = logEvent;
        this.mLogEndIndex = incrementIndex(this.mLogEndIndex);
        this.mLastLogTime += (long) timeDelta;
    }

    public synchronized void clear() {
        java.util.Arrays.fill(this.mLogEvents, (java.lang.Object) null);
        this.mLogEndIndex = 0;
        this.mLogSize = 0;
        this.mModificationCount++;
        this.mStartTime = -1L;
        this.mLastLogTime = -1L;
    }

    private boolean isEmpty() {
        return this.mLogSize == 0;
    }

    public synchronized void iterate(com.android.server.location.eventlog.LocalEventLog.LogConsumer<? super T> logConsumer) {
        com.android.server.location.eventlog.LocalEventLog.LogIterator logIterator = new com.android.server.location.eventlog.LocalEventLog.LogIterator();
        while (logIterator.hasNext()) {
            logIterator.next();
            logConsumer.acceptLog(logIterator.getTime(), (T) logIterator.getLog());
        }
    }

    @java.lang.SafeVarargs
    public static <T> void iterate(com.android.server.location.eventlog.LocalEventLog.LogConsumer<? super T> logConsumer, com.android.server.location.eventlog.LocalEventLog<T>... localEventLogArr) {
        java.util.ArrayList<com.android.server.location.eventlog.LocalEventLog<T>.LogIterator> arrayList = new java.util.ArrayList(localEventLogArr.length);
        for (com.android.server.location.eventlog.LocalEventLog<T> localEventLog : localEventLogArr) {
            java.util.Objects.requireNonNull(localEventLog);
            com.android.server.location.eventlog.LocalEventLog.LogIterator logIterator = localEventLog.new LogIterator();
            if (logIterator.hasNext()) {
                arrayList.add(logIterator);
                logIterator.next();
            }
        }
        while (true) {
            com.android.server.location.eventlog.LocalEventLog<T>.LogIterator logIterator2 = null;
            for (com.android.server.location.eventlog.LocalEventLog<T>.LogIterator logIterator3 : arrayList) {
                if (logIterator3 != null && (logIterator2 == null || logIterator3.getTime() < logIterator2.getTime())) {
                    logIterator2 = logIterator3;
                }
            }
            if (logIterator2 == null) {
                return;
            }
            logConsumer.acceptLog(logIterator2.getTime(), logIterator2.getLog());
            if (logIterator2.hasNext()) {
                logIterator2.next();
            } else {
                arrayList.remove(logIterator2);
            }
        }
    }

    int startIndex() {
        return wrapIndex(this.mLogEndIndex - this.mLogSize);
    }

    int incrementIndex(int index) {
        if (index == -1) {
            return startIndex();
        }
        if (index >= 0) {
            return wrapIndex(index + 1);
        }
        throw new java.lang.IllegalArgumentException();
    }

    int wrapIndex(int index) {
        return ((index % this.mEntries.length) + this.mEntries.length) % this.mEntries.length;
    }

    protected final class LogIterator {
        private int mCount;
        private T mCurrentLogEvent;
        private long mCurrentTime;
        private int mIndex;
        private long mLogTime;
        private final long mModificationCount;

        public LogIterator() {
            synchronized (com.android.server.location.eventlog.LocalEventLog.this) {
                this.mModificationCount = com.android.server.location.eventlog.LocalEventLog.this.mModificationCount;
                this.mLogTime = com.android.server.location.eventlog.LocalEventLog.this.mStartTime;
                this.mIndex = -1;
                this.mCount = -1;
                increment();
            }
        }

        public boolean hasNext() {
            boolean z;
            synchronized (com.android.server.location.eventlog.LocalEventLog.this) {
                checkModifications();
                z = this.mCount < com.android.server.location.eventlog.LocalEventLog.this.mLogSize;
            }
            return z;
        }

        public void next() {
            synchronized (com.android.server.location.eventlog.LocalEventLog.this) {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                this.mCurrentTime = this.mLogTime + ((long) com.android.server.location.eventlog.LocalEventLog.getTimeDelta(com.android.server.location.eventlog.LocalEventLog.this.mEntries[this.mIndex]));
                this.mCurrentLogEvent = (T) java.util.Objects.requireNonNull(com.android.server.location.eventlog.LocalEventLog.this.mLogEvents[this.mIndex]);
                increment();
            }
        }

        public long getTime() {
            return this.mCurrentTime;
        }

        public T getLog() {
            return this.mCurrentLogEvent;
        }

        private void increment() {
            long nextDeltaMs = this.mIndex == -1 ? 0L : com.android.server.location.eventlog.LocalEventLog.getTimeDelta(com.android.server.location.eventlog.LocalEventLog.this.mEntries[this.mIndex]);
            do {
                this.mLogTime += nextDeltaMs;
                this.mIndex = com.android.server.location.eventlog.LocalEventLog.this.incrementIndex(this.mIndex);
                int i = this.mCount + 1;
                this.mCount = i;
                if (i < com.android.server.location.eventlog.LocalEventLog.this.mLogSize) {
                    nextDeltaMs = com.android.server.location.eventlog.LocalEventLog.getTimeDelta(com.android.server.location.eventlog.LocalEventLog.this.mEntries[this.mIndex]);
                }
                if (this.mCount >= com.android.server.location.eventlog.LocalEventLog.this.mLogSize) {
                    return;
                }
            } while (com.android.server.location.eventlog.LocalEventLog.isFiller(com.android.server.location.eventlog.LocalEventLog.this.mEntries[this.mIndex]));
        }

        private void checkModifications() {
            if (this.mModificationCount != com.android.server.location.eventlog.LocalEventLog.this.mModificationCount) {
                throw new java.util.ConcurrentModificationException();
            }
        }
    }

    public com.android.server.location.eventlog.ILocalEventLogWrapper getLocalWrapper() {
        return this.mWrapper;
    }

    public final class LocalEventLogWrapper implements com.android.server.location.eventlog.ILocalEventLogWrapper {
        public LocalEventLogWrapper() {
        }

        @Override // com.android.server.location.eventlog.ILocalEventLogWrapper
        public void updateEventsLogSize(int i) {
            int iMax = java.lang.Math.max(com.android.server.location.eventlog.LocalEventLog.this.mEntries.length, i);
            com.android.server.location.eventlog.LocalEventLog.this.mEntries = java.util.Arrays.copyOf(com.android.server.location.eventlog.LocalEventLog.this.mEntries, iMax);
            com.android.server.location.eventlog.LocalEventLog.this.mLogEvents = (T[]) java.util.Arrays.copyOf(com.android.server.location.eventlog.LocalEventLog.this.mLogEvents, iMax);
        }
    }
}
