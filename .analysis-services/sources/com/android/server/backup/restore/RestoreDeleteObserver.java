package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public class RestoreDeleteObserver extends android.content.pm.IPackageDeleteObserver.Stub {
    private final java.util.concurrent.atomic.AtomicBoolean mDone = new java.util.concurrent.atomic.AtomicBoolean();

    public void reset() {
        synchronized (this.mDone) {
            this.mDone.set(false);
        }
    }

    public void waitForCompletion() {
        synchronized (this.mDone) {
            while (!this.mDone.get()) {
                try {
                    this.mDone.wait();
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
    }

    public void packageDeleted(java.lang.String packageName, int returnCode) throws android.os.RemoteException {
        synchronized (this.mDone) {
            this.mDone.set(true);
            this.mDone.notifyAll();
        }
    }
}
