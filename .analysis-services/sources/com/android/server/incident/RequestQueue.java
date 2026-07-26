package com.android.server.incident;

/* JADX INFO: loaded from: classes2.dex */
class RequestQueue {
    private final android.os.Handler mHandler;
    private boolean mStarted;
    private java.util.ArrayList<com.android.server.incident.RequestQueue.Rec> mPending = new java.util.ArrayList<>();
    private final java.lang.Runnable mWorker = new java.lang.Runnable() { // from class: com.android.server.incident.RequestQueue.1
        @Override // java.lang.Runnable
        public void run() {
            java.util.ArrayList<com.android.server.incident.RequestQueue.Rec> copy = null;
            synchronized (com.android.server.incident.RequestQueue.this.mPending) {
                if (com.android.server.incident.RequestQueue.this.mPending.size() > 0) {
                    copy = new java.util.ArrayList<>(com.android.server.incident.RequestQueue.this.mPending);
                    com.android.server.incident.RequestQueue.this.mPending.clear();
                }
            }
            if (copy != null) {
                int size = copy.size();
                for (int i = 0; i < size; i++) {
                    copy.get(i).runnable.run();
                }
            }
        }
    };

    private class Rec {
        public final android.os.IBinder key;
        public final java.lang.Runnable runnable;
        public final boolean value;

        Rec(android.os.IBinder key, boolean value, java.lang.Runnable runnable) {
            this.key = key;
            this.value = value;
            this.runnable = runnable;
        }
    }

    RequestQueue(android.os.Handler handler) {
        this.mHandler = handler;
    }

    public void start() {
        synchronized (this.mPending) {
            if (!this.mStarted) {
                if (this.mPending.size() > 0) {
                    this.mHandler.post(this.mWorker);
                }
                this.mStarted = true;
            }
        }
    }

    public void enqueue(android.os.IBinder key, boolean value, java.lang.Runnable runnable) {
        synchronized (this.mPending) {
            boolean skip = false;
            if (!value) {
                try {
                    int i = this.mPending.size() - 1;
                    while (true) {
                        if (i < 0) {
                            break;
                        }
                        com.android.server.incident.RequestQueue.Rec r = this.mPending.get(i);
                        if (r.key != key || !r.value) {
                            i--;
                        } else {
                            skip = true;
                            this.mPending.remove(i);
                            break;
                        }
                    }
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
            if (!skip) {
                this.mPending.add(new com.android.server.incident.RequestQueue.Rec(key, value, runnable));
            }
            if (this.mStarted) {
                this.mHandler.post(this.mWorker);
            }
        }
    }
}
