package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class SensitiveContentPackages {
    private final android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> mProtectedPackages = new android.util.ArraySet<>();

    public boolean shouldBlockScreenCaptureForApp(java.lang.String pkg, int uid, android.os.IBinder windowToken) {
        if (!android.view.flags.Flags.sensitiveContentAppProtection() && !com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveNotificationAppProtection()) {
            return false;
        }
        for (int i = 0; i < this.mProtectedPackages.size(); i++) {
            com.android.server.wm.SensitiveContentPackages.PackageInfo info = this.mProtectedPackages.valueAt(i);
            if (info != null && info.mPkg.equals(pkg) && info.mUid == uid) {
                if (!android.view.flags.Flags.sensitiveContentAppProtection() || windowToken != info.getWindowToken()) {
                    if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveNotificationAppProtection() && info.getWindowToken() == null) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addBlockScreenCaptureForApps(android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> packageInfos) {
        if (this.mProtectedPackages.equals(packageInfos)) {
            return false;
        }
        this.mProtectedPackages.addAll((android.util.ArraySet<? extends com.android.server.wm.SensitiveContentPackages.PackageInfo>) packageInfos);
        return true;
    }

    public boolean removeBlockScreenCaptureForApps(android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> packageInfos) {
        return this.mProtectedPackages.removeAll((android.util.ArraySet<? extends com.android.server.wm.SensitiveContentPackages.PackageInfo>) packageInfos);
    }

    public boolean clearBlockedApps() {
        if (this.mProtectedPackages.isEmpty()) {
            return false;
        }
        this.mProtectedPackages.clear();
        return true;
    }

    public int size() {
        return this.mProtectedPackages.size();
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("SensitiveContentPackages:");
        pw.println("  Packages that should block screen capture (" + this.mProtectedPackages.size() + "):");
        for (com.android.server.wm.SensitiveContentPackages.PackageInfo info : this.mProtectedPackages) {
            pw.println("    package=" + info.mPkg + "  uid=" + info.mUid + " windowToken=" + info.mWindowToken);
        }
    }

    public static class PackageInfo {
        private final java.lang.String mPkg;
        private final int mUid;
        private final android.os.IBinder mWindowToken;

        public PackageInfo(java.lang.String pkg, int uid) {
            this(pkg, uid, null);
        }

        public PackageInfo(java.lang.String pkg, int uid, android.os.IBinder windowToken) {
            this.mPkg = pkg;
            this.mUid = uid;
            this.mWindowToken = windowToken;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.wm.SensitiveContentPackages.PackageInfo)) {
                return false;
            }
            com.android.server.wm.SensitiveContentPackages.PackageInfo that = (com.android.server.wm.SensitiveContentPackages.PackageInfo) o;
            return this.mUid == that.mUid && java.util.Objects.equals(this.mPkg, that.mPkg) && java.util.Objects.equals(this.mWindowToken, that.mWindowToken);
        }

        public int hashCode() {
            return java.util.Objects.hash(this.mPkg, java.lang.Integer.valueOf(this.mUid), this.mWindowToken);
        }

        public android.os.IBinder getWindowToken() {
            return this.mWindowToken;
        }

        public int getUid() {
            return this.mUid;
        }

        public java.lang.String getPkg() {
            return this.mPkg;
        }

        public java.lang.String toString() {
            return "package=" + this.mPkg + "  uid=" + this.mUid + " windowToken=" + this.mWindowToken;
        }
    }
}
