package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public final class UserTokenWatcher {
    private final com.android.server.utils.UserTokenWatcher.Callback mCallback;
    private final android.os.Handler mHandler;
    private final java.lang.String mTag;
    private final android.util.SparseArray<android.os.TokenWatcher> mWatchers = new android.util.SparseArray<>(1);

    public interface Callback {
        void acquired(int i);

        void released(int i);
    }

    public UserTokenWatcher(com.android.server.utils.UserTokenWatcher.Callback callback, android.os.Handler handler, java.lang.String tag) {
        this.mCallback = callback;
        this.mHandler = handler;
        this.mTag = tag;
    }

    public void acquire(android.os.IBinder token, java.lang.String tag, int userId) {
        synchronized (this.mWatchers) {
            android.os.TokenWatcher watcher = this.mWatchers.get(userId);
            if (watcher == null) {
                watcher = new com.android.server.utils.UserTokenWatcher.InnerTokenWatcher(userId, this.mHandler, this.mTag);
                this.mWatchers.put(userId, watcher);
            }
            watcher.acquire(token, tag);
        }
    }

    public void release(android.os.IBinder token, int userId) {
        synchronized (this.mWatchers) {
            android.os.TokenWatcher watcher = this.mWatchers.get(userId);
            if (watcher != null) {
                watcher.release(token);
            }
        }
    }

    public boolean isAcquired(int userId) {
        boolean z;
        synchronized (this.mWatchers) {
            android.os.TokenWatcher watcher = this.mWatchers.get(userId);
            z = watcher != null && watcher.isAcquired();
        }
        return z;
    }

    public void dump(java.io.PrintWriter pw) {
        synchronized (this.mWatchers) {
            for (int i = 0; i < this.mWatchers.size(); i++) {
                int userId = this.mWatchers.keyAt(i);
                android.os.TokenWatcher watcher = this.mWatchers.valueAt(i);
                if (watcher.isAcquired()) {
                    pw.print("User ");
                    pw.print(userId);
                    pw.println(":");
                    watcher.dump(new com.android.internal.util.IndentingPrintWriter(pw, " "));
                }
            }
        }
    }

    private final class InnerTokenWatcher extends android.os.TokenWatcher {
        private final int mUserId;

        private InnerTokenWatcher(int userId, android.os.Handler handler, java.lang.String tag) {
            super(handler, tag);
            this.mUserId = userId;
        }

        @Override // android.os.TokenWatcher
        public void acquired() {
            com.android.server.utils.UserTokenWatcher.this.mCallback.acquired(this.mUserId);
        }

        @Override // android.os.TokenWatcher
        public void released() {
            com.android.server.utils.UserTokenWatcher.this.mCallback.released(this.mUserId);
            synchronized (com.android.server.utils.UserTokenWatcher.this.mWatchers) {
                android.os.TokenWatcher watcher = (android.os.TokenWatcher) com.android.server.utils.UserTokenWatcher.this.mWatchers.get(this.mUserId);
                if (watcher != null && !watcher.isAcquired()) {
                    com.android.server.utils.UserTokenWatcher.this.mWatchers.remove(this.mUserId);
                }
            }
        }
    }
}
