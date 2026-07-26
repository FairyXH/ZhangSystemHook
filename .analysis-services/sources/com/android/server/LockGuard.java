package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class LockGuard {
    public static final int INDEX_ACTIVITY = 7;
    public static final int INDEX_APP_OPS = 0;
    public static final int INDEX_DPMS = 8;
    public static final int INDEX_PACKAGES = 3;
    public static final int INDEX_POWER = 1;
    public static final int INDEX_PROC = 6;
    public static final int INDEX_STORAGE = 4;
    public static final int INDEX_USER = 2;
    public static final int INDEX_WINDOW = 5;
    private static final java.lang.String TAG = "LockGuard";
    private static java.lang.Object[] sKnownFixed = new java.lang.Object[9];
    private static android.util.ArrayMap<java.lang.Object, com.android.server.LockGuard.LockInfo> sKnown = new android.util.ArrayMap<>(0, true);

    private static class LockInfo {
        public android.util.ArraySet<java.lang.Object> children;
        public boolean doWtf;
        public java.lang.String label;

        private LockInfo() {
            this.children = new android.util.ArraySet<>(0, true);
        }
    }

    private static com.android.server.LockGuard.LockInfo findOrCreateLockInfo(java.lang.Object lock) {
        com.android.server.LockGuard.LockInfo info = sKnown.get(lock);
        if (info == null) {
            com.android.server.LockGuard.LockInfo info2 = new com.android.server.LockGuard.LockInfo();
            info2.label = "0x" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(lock)) + " [" + new java.lang.Throwable().getStackTrace()[2].toString() + "]";
            sKnown.put(lock, info2);
            return info2;
        }
        return info;
    }

    public static java.lang.Object guard(java.lang.Object lock) {
        if (lock == null || java.lang.Thread.holdsLock(lock)) {
            return lock;
        }
        boolean triggered = false;
        com.android.server.LockGuard.LockInfo info = findOrCreateLockInfo(lock);
        for (int i = 0; i < info.children.size(); i++) {
            java.lang.Object child = info.children.valueAt(i);
            if (child != null && java.lang.Thread.holdsLock(child)) {
                doLog(lock, "Calling thread " + java.lang.Thread.currentThread().getName() + " is holding " + lockToString(child) + " while trying to acquire " + lockToString(lock));
                triggered = true;
            }
        }
        if (!triggered) {
            for (int i2 = 0; i2 < sKnown.size(); i2++) {
                java.lang.Object test = sKnown.keyAt(i2);
                if (test != null && test != lock && java.lang.Thread.holdsLock(test)) {
                    sKnown.valueAt(i2).children.add(lock);
                }
            }
        }
        return lock;
    }

    public static void guard(int index) {
        for (int i = 0; i < index; i++) {
            java.lang.Object lock = sKnownFixed[i];
            if (lock != null && java.lang.Thread.holdsLock(lock)) {
                java.lang.Object targetMayBeNull = sKnownFixed[index];
                doLog(targetMayBeNull, "Calling thread " + java.lang.Thread.currentThread().getName() + " is holding " + lockToString(i) + " while trying to acquire " + lockToString(index));
            }
        }
    }

    private static void doLog(java.lang.Object lock, java.lang.String message) {
        if (lock != null && findOrCreateLockInfo(lock).doWtf) {
            final java.lang.Throwable stackTrace = new java.lang.RuntimeException(message);
            new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.LockGuard$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    android.util.Slog.wtf(com.android.server.LockGuard.TAG, stackTrace);
                }
            }).start();
        } else {
            android.util.Slog.w(TAG, message, new java.lang.Throwable());
        }
    }

    public static java.lang.Object installLock(java.lang.Object lock, java.lang.String label) {
        com.android.server.LockGuard.LockInfo info = findOrCreateLockInfo(lock);
        info.label = label;
        return lock;
    }

    public static java.lang.Object installLock(java.lang.Object lock, int index) {
        return installLock(lock, index, false);
    }

    public static java.lang.Object installLock(java.lang.Object lock, int index, boolean doWtf) {
        sKnownFixed[index] = lock;
        com.android.server.LockGuard.LockInfo info = findOrCreateLockInfo(lock);
        info.doWtf = doWtf;
        info.label = "Lock-" + lockToString(index);
        return lock;
    }

    public static java.lang.Object installNewLock(int index) {
        return installNewLock(index, false);
    }

    public static java.lang.Object installNewLock(int index, boolean doWtf) {
        java.lang.Object lock = new java.lang.Object();
        installLock(lock, index, doWtf);
        return lock;
    }

    private static java.lang.String lockToString(java.lang.Object lock) {
        com.android.server.LockGuard.LockInfo info = sKnown.get(lock);
        if (info != null && !android.text.TextUtils.isEmpty(info.label)) {
            return info.label;
        }
        return "0x" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(lock));
    }

    private static java.lang.String lockToString(int index) {
        switch (index) {
            case 0:
                return "APP_OPS";
            case 1:
                return "POWER";
            case 2:
                return "USER";
            case 3:
                return "PACKAGES";
            case 4:
                return "STORAGE";
            case 5:
                return "WINDOW";
            case 6:
                return "PROCESS";
            case 7:
                return "ACTIVITY";
            case 8:
                return "DPMS";
            default:
                return java.lang.Integer.toString(index);
        }
    }

    public static void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        for (int i = 0; i < sKnown.size(); i++) {
            java.lang.Object lock = sKnown.keyAt(i);
            com.android.server.LockGuard.LockInfo info = sKnown.valueAt(i);
            pw.println("Lock " + lockToString(lock) + ":");
            for (int j = 0; j < info.children.size(); j++) {
                pw.println("  Child " + lockToString(info.children.valueAt(j)));
            }
            pw.println();
        }
    }
}
