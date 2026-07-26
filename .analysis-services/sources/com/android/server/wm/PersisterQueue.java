package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class PersisterQueue {
    private static final boolean DEBUG = false;
    static final com.android.server.wm.PersisterQueue.WriteQueueItem EMPTY_ITEM = new com.android.server.wm.PersisterQueue.WriteQueueItem() { // from class: com.android.server.wm.PersisterQueue$$ExternalSyntheticLambda0
        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public final void process() {
            com.android.server.wm.PersisterQueue.lambda$static$0();
        }
    };
    private static final long FLUSH_QUEUE = -1;
    private static final long INTER_WRITE_DELAY_MS = 500;
    private static final int MAX_WRITE_QUEUE_LENGTH = 6;
    private static final long PRE_TASK_DELAY_MS = 3000;
    private static final java.lang.String TAG = "PersisterQueue";
    private final long mInterWriteDelayMs;
    private final com.android.server.wm.PersisterQueue.LazyTaskWriterThread mLazyTaskWriterThread;
    private final java.util.ArrayList<com.android.server.wm.PersisterQueue.Listener> mListeners;
    private long mNextWriteTime;
    private final long mPreTaskDelayMs;
    private final java.util.ArrayList<com.android.server.wm.PersisterQueue.WriteQueueItem> mWriteQueue;

    interface Listener {
        void onPreProcessItem(boolean z);
    }

    static /* synthetic */ void lambda$static$0() {
    }

    PersisterQueue() {
        this(500L, 3000L);
    }

    PersisterQueue(long interWriteDelayMs, long preTaskDelayMs) {
        this.mWriteQueue = new java.util.ArrayList<>();
        this.mListeners = new java.util.ArrayList<>();
        this.mNextWriteTime = 0L;
        if (interWriteDelayMs < 0 || preTaskDelayMs < 0) {
            throw new java.lang.IllegalArgumentException("Both inter-write delay and pre-task delay need tobe non-negative. inter-write delay: " + interWriteDelayMs + "ms pre-task delay: " + preTaskDelayMs);
        }
        this.mInterWriteDelayMs = interWriteDelayMs;
        this.mPreTaskDelayMs = preTaskDelayMs;
        this.mLazyTaskWriterThread = new com.android.server.wm.PersisterQueue.LazyTaskWriterThread("LazyTaskWriterThread");
    }

    synchronized void startPersisting() {
        if (!this.mLazyTaskWriterThread.isAlive()) {
            this.mLazyTaskWriterThread.start();
        }
    }

    void stopPersisting() throws java.lang.InterruptedException {
        if (!this.mLazyTaskWriterThread.isAlive()) {
            return;
        }
        synchronized (this) {
            this.mLazyTaskWriterThread.interrupt();
        }
        this.mLazyTaskWriterThread.join();
    }

    synchronized void addItem(com.android.server.wm.PersisterQueue.WriteQueueItem item, boolean flush) {
        this.mWriteQueue.add(item);
        if (flush || this.mWriteQueue.size() > 6) {
            this.mNextWriteTime = -1L;
        } else if (this.mNextWriteTime == 0) {
            this.mNextWriteTime = android.os.SystemClock.uptimeMillis() + this.mPreTaskDelayMs;
        }
        notify();
    }

    synchronized <T extends com.android.server.wm.PersisterQueue.WriteQueueItem> T findLastItem(java.util.function.Predicate<T> predicate, java.lang.Class<T> clazz) {
        for (int i = this.mWriteQueue.size() - 1; i >= 0; i--) {
            com.android.server.wm.PersisterQueue.WriteQueueItem writeQueueItem = this.mWriteQueue.get(i);
            if (clazz.isInstance(writeQueueItem)) {
                T item = clazz.cast(writeQueueItem);
                if (predicate.test(item)) {
                    return item;
                }
            }
        }
        return null;
    }

    synchronized <T extends com.android.server.wm.PersisterQueue.WriteQueueItem> void updateLastOrAddItem(final T item, boolean flush) {
        java.util.Objects.requireNonNull(item);
        com.android.server.wm.PersisterQueue.WriteQueueItem writeQueueItemFindLastItem = findLastItem(new java.util.function.Predicate() { // from class: com.android.server.wm.PersisterQueue$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return item.matches((com.android.server.wm.PersisterQueue.WriteQueueItem) obj);
            }
        }, item.getClass());
        if (writeQueueItemFindLastItem == null) {
            addItem(item, flush);
        } else {
            writeQueueItemFindLastItem.updateFrom(item);
        }
        yieldIfQueueTooDeep();
    }

    synchronized <T extends com.android.server.wm.PersisterQueue.WriteQueueItem> void removeItems(java.util.function.Predicate<T> predicate, java.lang.Class<T> clazz) {
        for (int i = this.mWriteQueue.size() - 1; i >= 0; i--) {
            com.android.server.wm.PersisterQueue.WriteQueueItem writeQueueItem = this.mWriteQueue.get(i);
            if (clazz.isInstance(writeQueueItem)) {
                T item = clazz.cast(writeQueueItem);
                if (predicate.test(item)) {
                    this.mWriteQueue.remove(i);
                }
            }
        }
    }

    synchronized void flush() {
        this.mNextWriteTime = -1L;
        notifyAll();
        do {
            try {
                wait();
            } catch (java.lang.InterruptedException e) {
            }
        } while (this.mNextWriteTime == -1);
    }

    void yieldIfQueueTooDeep() {
        boolean stall = false;
        synchronized (this) {
            if (this.mNextWriteTime == -1) {
                stall = true;
            }
        }
        if (stall) {
            java.lang.Thread.yield();
        }
    }

    void addListener(com.android.server.wm.PersisterQueue.Listener listener) {
        this.mListeners.add(listener);
    }

    boolean removeListener(com.android.server.wm.PersisterQueue.Listener listener) {
        return this.mListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNextItem() throws java.lang.InterruptedException {
        com.android.server.wm.PersisterQueue.WriteQueueItem item;
        synchronized (this) {
            if (this.mNextWriteTime != -1) {
                this.mNextWriteTime = android.os.SystemClock.uptimeMillis() + this.mInterWriteDelayMs;
            }
            while (this.mWriteQueue.isEmpty()) {
                if (this.mNextWriteTime != 0) {
                    this.mNextWriteTime = 0L;
                    notify();
                }
                if (java.lang.Thread.currentThread().isInterrupted()) {
                    throw new java.lang.InterruptedException();
                }
                wait();
            }
            item = this.mWriteQueue.remove(0);
            for (long now = android.os.SystemClock.uptimeMillis(); now < this.mNextWriteTime; now = android.os.SystemClock.uptimeMillis()) {
                wait(this.mNextWriteTime - now);
            }
        }
        item.process();
    }

    interface WriteQueueItem<T extends com.android.server.wm.PersisterQueue.WriteQueueItem<T>> {
        void process();

        default void updateFrom(T item) {
        }

        default boolean matches(T item) {
            return false;
        }
    }

    private class LazyTaskWriterThread extends java.lang.Thread {
        private LazyTaskWriterThread(java.lang.String name) {
            super(name);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            boolean probablyDone;
            android.os.Process.setThreadPriority(10);
            while (true) {
                try {
                    synchronized (com.android.server.wm.PersisterQueue.this) {
                        probablyDone = com.android.server.wm.PersisterQueue.this.mWriteQueue.isEmpty();
                    }
                    for (int i = com.android.server.wm.PersisterQueue.this.mListeners.size() - 1; i >= 0; i--) {
                        ((com.android.server.wm.PersisterQueue.Listener) com.android.server.wm.PersisterQueue.this.mListeners.get(i)).onPreProcessItem(probablyDone);
                    }
                    com.android.server.wm.PersisterQueue.this.processNextItem();
                } catch (java.lang.InterruptedException e) {
                    android.util.Slog.e(com.android.server.wm.PersisterQueue.TAG, "Persister thread is exiting. Should never happen in prod, butit's OK in tests.");
                    return;
                }
            }
        }
    }
}
