package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class DevicePolicyCacheImpl extends android.app.admin.DevicePolicyCache {
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.Set<java.lang.Integer> mScreenCaptureDisallowedUsers = new java.util.HashSet();
    private final android.util.SparseIntArray mPasswordQuality = new android.util.SparseIntArray();
    private final android.util.SparseIntArray mPermissionPolicy = new android.util.SparseIntArray();
    private android.util.ArrayMap<java.lang.String, java.lang.String> mLauncherShortcutOverrides = new android.util.ArrayMap<>();
    private volatile boolean mCanGrantSensorsPermissions = false;
    private final android.util.SparseIntArray mContentProtectionPolicy = new android.util.SparseIntArray();

    public void onUserRemoved(int userHandle) {
        synchronized (this.mLock) {
            this.mPasswordQuality.delete(userHandle);
            this.mPermissionPolicy.delete(userHandle);
            this.mContentProtectionPolicy.delete(userHandle);
        }
    }

    public boolean isScreenCaptureAllowed(int userHandle) {
        boolean z;
        synchronized (this.mLock) {
            z = (this.mScreenCaptureDisallowedUsers.contains(java.lang.Integer.valueOf(userHandle)) || this.mScreenCaptureDisallowedUsers.contains(-1)) ? false : true;
        }
        return z;
    }

    public void setScreenCaptureDisallowedUser(int userHandle, boolean disallowed) {
        synchronized (this.mLock) {
            if (disallowed) {
                this.mScreenCaptureDisallowedUsers.add(java.lang.Integer.valueOf(userHandle));
            } else {
                this.mScreenCaptureDisallowedUsers.remove(java.lang.Integer.valueOf(userHandle));
            }
        }
    }

    public int getPasswordQuality(int userHandle) {
        int i;
        synchronized (this.mLock) {
            i = this.mPasswordQuality.get(userHandle, 0);
        }
        return i;
    }

    public void setPasswordQuality(int userHandle, int quality) {
        synchronized (this.mLock) {
            this.mPasswordQuality.put(userHandle, quality);
        }
    }

    public int getPermissionPolicy(int userHandle) {
        int i;
        synchronized (this.mLock) {
            i = this.mPermissionPolicy.get(userHandle, 0);
        }
        return i;
    }

    public void setPermissionPolicy(int userHandle, int policy) {
        synchronized (this.mLock) {
            this.mPermissionPolicy.put(userHandle, policy);
        }
    }

    public int getContentProtectionPolicy(int userId) {
        int i;
        synchronized (this.mLock) {
            i = this.mContentProtectionPolicy.get(userId, 1);
        }
        return i;
    }

    public void setContentProtectionPolicy(int userId, java.lang.Integer value) {
        synchronized (this.mLock) {
            if (value == null) {
                this.mContentProtectionPolicy.delete(userId);
            } else {
                this.mContentProtectionPolicy.put(userId, value.intValue());
            }
        }
    }

    public boolean canAdminGrantSensorsPermissions() {
        return this.mCanGrantSensorsPermissions;
    }

    public void setAdminCanGrantSensorsPermissions(boolean canGrant) {
        this.mCanGrantSensorsPermissions = canGrant;
    }

    public java.util.Map<java.lang.String, java.lang.String> getLauncherShortcutOverrides() {
        android.util.ArrayMap arrayMap;
        synchronized (this.mLock) {
            arrayMap = new android.util.ArrayMap(this.mLauncherShortcutOverrides);
        }
        return arrayMap;
    }

    public void setLauncherShortcutOverrides(android.util.ArrayMap<java.lang.String, java.lang.String> launcherShortcutOverrides) {
        synchronized (this.mLock) {
            this.mLauncherShortcutOverrides = new android.util.ArrayMap<>(launcherShortcutOverrides);
        }
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("Device policy cache:");
            pw.increaseIndent();
            pw.println("Screen capture disallowed users: " + this.mScreenCaptureDisallowedUsers);
            pw.println("Password quality: " + this.mPasswordQuality);
            pw.println("Permission policy: " + this.mPermissionPolicy);
            pw.println("Content protection policy: " + this.mContentProtectionPolicy);
            pw.println("Admin can grant sensors permission: " + this.mCanGrantSensorsPermissions);
            pw.print("Shortcuts overrides: ");
            pw.println(this.mLauncherShortcutOverrides);
            pw.decreaseIndent();
        }
    }
}
