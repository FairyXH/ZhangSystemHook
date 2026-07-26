package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class KillAppBlocker {
    private static final int MAX_WAIT_TIMEOUT_MS = 1000;
    private java.util.concurrent.CountDownLatch mUidsGoneCountDownLatch = new java.util.concurrent.CountDownLatch(1);
    private java.util.List mActiveUids = new java.util.ArrayList();
    private boolean mRegistered = false;
    private final android.app.IUidObserver mUidObserver = new android.app.UidObserver() { // from class: com.android.server.pm.KillAppBlocker.1
        public void onUidGone(int uid, boolean disabled) {
            synchronized (this) {
                com.android.server.pm.KillAppBlocker.this.mActiveUids.remove(java.lang.Integer.valueOf(uid));
                if (com.android.server.pm.KillAppBlocker.this.mActiveUids.size() == 0) {
                    com.android.server.pm.KillAppBlocker.this.mUidsGoneCountDownLatch.countDown();
                }
            }
        }
    };

    KillAppBlocker() {
    }

    void register() {
        android.app.IActivityManager am;
        if (!this.mRegistered && (am = android.app.ActivityManager.getService()) != null) {
            try {
                am.registerUidObserver(this.mUidObserver, 2, -1, "pm");
                this.mRegistered = true;
            } catch (android.os.RemoteException e) {
            }
        }
    }

    void unregister() {
        android.app.IActivityManager am;
        if (this.mRegistered && (am = android.app.ActivityManager.getService()) != null) {
            try {
                this.mRegistered = false;
                am.unregisterUidObserver(this.mUidObserver);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    void waitAppProcessGone(android.app.ActivityManagerInternal ami, com.android.server.pm.Computer snapshot, com.android.server.pm.UserManagerService userManager, java.lang.String packageName) {
        if (!this.mRegistered) {
            return;
        }
        synchronized (this) {
            if (ami != null) {
                int[] users = userManager.getUserIds();
                for (int userId : users) {
                    int uid = snapshot.getPackageUidInternal(packageName, 131072L, userId, 1000);
                    if (uid != -1 && ami.getUidProcessState(uid) != 20) {
                        this.mActiveUids.add(java.lang.Integer.valueOf(uid));
                    }
                }
            }
            if (this.mActiveUids.size() == 0) {
                return;
            }
            try {
                this.mUidsGoneCountDownLatch.await(1000L, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.lang.InterruptedException e) {
            }
        }
    }
}
