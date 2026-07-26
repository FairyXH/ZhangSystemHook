package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemUserInfoHelper extends com.android.server.location.injector.UserInfoHelper {
    private android.app.IActivityManager mActivityManager;
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private final android.content.Context mContext;
    private android.os.UserManager mUserManager;
    private com.android.server.pm.UserManagerInternal mUserManagerInternal;

    public SystemUserInfoHelper(android.content.Context context) {
        this.mContext = context;
    }

    public synchronized void onSystemReady() {
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) java.util.Objects.requireNonNull((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class));
        this.mUserManagerInternal.addUserVisibilityListener(new com.android.server.pm.UserManagerInternal.UserVisibilityListener() { // from class: com.android.server.location.injector.SystemUserInfoHelper$$ExternalSyntheticLambda0
            @Override // com.android.server.pm.UserManagerInternal.UserVisibilityListener
            public final void onUserVisibilityChanged(int i, boolean z) {
                this.f$0.lambda$onSystemReady$0(i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0(int userId, boolean visible) {
        dispatchOnVisibleUserChanged(userId, visible);
    }

    protected final android.app.ActivityManagerInternal getActivityManagerInternal() {
        synchronized (this) {
            if (this.mActivityManagerInternal == null) {
                this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            }
        }
        return this.mActivityManagerInternal;
    }

    protected final android.app.IActivityManager getActivityManager() {
        synchronized (this) {
            if (this.mActivityManager == null) {
                this.mActivityManager = android.app.ActivityManager.getService();
            }
        }
        return this.mActivityManager;
    }

    protected final android.os.UserManager getUserManager() {
        synchronized (this) {
            if (this.mUserManager == null) {
                this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
            }
        }
        return this.mUserManager;
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public int[] getRunningUserIds() {
        android.app.IActivityManager activityManager = getActivityManager();
        if (activityManager != null) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    return activityManager.getRunningUserIds();
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        return new int[0];
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public boolean isCurrentUserId(int userId) {
        android.app.ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal != null) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return activityManagerInternal.isCurrentProfile(userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        return false;
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public int getCurrentUserId() {
        android.app.ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal != null) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return activityManagerInternal.getCurrentUserId();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        return -10000;
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public boolean isVisibleUserId(int userId) {
        boolean zIsUserVisible;
        synchronized (this) {
            com.android.internal.util.Preconditions.checkState(this.mUserManagerInternal != null);
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this) {
                zIsUserVisible = this.mUserManagerInternal.isUserVisible(userId);
            }
            return zIsUserVisible;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    protected int[] getProfileIds(int userId) {
        android.os.UserManager userManager = getUserManager();
        com.android.internal.util.Preconditions.checkState(userManager != null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return userManager.getEnabledProfileIds(userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public void dump(java.io.FileDescriptor fd, android.util.IndentingPrintWriter pw, java.lang.String[] args) {
        int[] runningUserIds = getRunningUserIds();
        if (runningUserIds.length > 1) {
            pw.println("running users: u" + java.util.Arrays.toString(runningUserIds));
        }
        android.app.ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal == null) {
            return;
        }
        int[] currentProfileIds = activityManagerInternal.getCurrentProfileIds();
        pw.println("current users: u" + java.util.Arrays.toString(currentProfileIds));
        android.os.UserManager userManager = getUserManager();
        if (userManager != null) {
            for (int userId : currentProfileIds) {
                if (userManager.hasUserRestrictionForUser("no_share_location", android.os.UserHandle.of(userId))) {
                    pw.increaseIndent();
                    pw.println("u" + userId + " restricted");
                    pw.decreaseIndent();
                }
            }
        }
    }
}
