package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppErrorResult {
    boolean mHasResult = false;
    int mResult;

    AppErrorResult() {
    }

    public void set(int res) {
        synchronized (this) {
            this.mHasResult = true;
            this.mResult = res;
            notifyAll();
        }
    }

    public int get() {
        synchronized (this) {
            while (!this.mHasResult) {
                try {
                    wait();
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
        return this.mResult;
    }
}
