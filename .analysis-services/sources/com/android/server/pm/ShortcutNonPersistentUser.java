package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ShortcutNonPersistentUser {
    private final int mUserId;
    private final android.util.ArrayMap<java.lang.String, java.lang.String> mHostPackages = new android.util.ArrayMap<>();
    private final android.util.ArraySet<java.lang.String> mHostPackageSet = new android.util.ArraySet<>();

    public ShortcutNonPersistentUser(int userId) {
        this.mUserId = userId;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public void setShortcutHostPackage(java.lang.String type, java.lang.String packageName) {
        if (packageName != null) {
            this.mHostPackages.put(type, packageName);
        } else {
            this.mHostPackages.remove(type);
        }
        this.mHostPackageSet.clear();
        for (int i = 0; i < this.mHostPackages.size(); i++) {
            this.mHostPackageSet.add(this.mHostPackages.valueAt(i));
        }
    }

    public boolean hasHostPackage(java.lang.String packageName) {
        return this.mHostPackageSet.contains(packageName);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.pm.ShortcutService.DumpFilter filter) {
        if (filter.shouldDumpDetails() && this.mHostPackages.size() > 0) {
            pw.print(prefix);
            pw.print("Non-persistent: user ID:");
            pw.println(this.mUserId);
            pw.print(prefix);
            pw.println("  Host packages:");
            for (int i = 0; i < this.mHostPackages.size(); i++) {
                pw.print(prefix);
                pw.print("    ");
                pw.print(this.mHostPackages.keyAt(i));
                pw.print(": ");
                pw.println(this.mHostPackages.valueAt(i));
            }
            pw.println();
        }
    }
}
