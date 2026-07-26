package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class KeyguardDisableHandler {
    private static final java.lang.String TAG = "WindowManager";
    private final com.android.server.utils.UserTokenWatcher mAppTokenWatcher;
    private com.android.server.wm.KeyguardDisableHandler.Injector mInjector;
    private final com.android.server.utils.UserTokenWatcher mSystemTokenWatcher;
    private int mCurrentUser = 0;
    private final com.android.server.utils.UserTokenWatcher.Callback mCallback = new com.android.server.utils.UserTokenWatcher.Callback() { // from class: com.android.server.wm.KeyguardDisableHandler.1
        @Override // com.android.server.utils.UserTokenWatcher.Callback
        public void acquired(int userId) {
            com.android.server.wm.KeyguardDisableHandler.this.updateKeyguardEnabled(userId);
        }

        @Override // com.android.server.utils.UserTokenWatcher.Callback
        public void released(int userId) {
            com.android.server.wm.KeyguardDisableHandler.this.updateKeyguardEnabled(userId);
        }
    };

    interface Injector {
        boolean dpmRequiresPassword(int i);

        void enableKeyguard(boolean z);

        int getProfileParentId(int i);

        boolean isKeyguardSecure(int i);
    }

    KeyguardDisableHandler(com.android.server.wm.KeyguardDisableHandler.Injector injector, android.os.Handler handler) {
        this.mInjector = injector;
        this.mAppTokenWatcher = new com.android.server.utils.UserTokenWatcher(this.mCallback, handler, TAG);
        this.mSystemTokenWatcher = new com.android.server.utils.UserTokenWatcher(this.mCallback, handler, TAG);
    }

    public void setCurrentUser(int user) {
        synchronized (this) {
            this.mCurrentUser = user;
            updateKeyguardEnabledLocked(-1);
        }
    }

    void updateKeyguardEnabled(int userId) {
        synchronized (this) {
            updateKeyguardEnabledLocked(userId);
        }
    }

    private void updateKeyguardEnabledLocked(int userId) {
        if (this.mCurrentUser == userId || userId == -1) {
            this.mInjector.enableKeyguard(shouldKeyguardBeEnabled(this.mCurrentUser));
        }
    }

    void disableKeyguard(android.os.IBinder token, java.lang.String tag, int callingUid, int userId) {
        com.android.server.utils.UserTokenWatcher watcherForCaller = watcherForCallingUid(token, callingUid);
        watcherForCaller.acquire(token, tag, this.mInjector.getProfileParentId(userId));
    }

    void reenableKeyguard(android.os.IBinder token, int callingUid, int userId) {
        com.android.server.utils.UserTokenWatcher watcherForCaller = watcherForCallingUid(token, callingUid);
        watcherForCaller.release(token, this.mInjector.getProfileParentId(userId));
    }

    private com.android.server.utils.UserTokenWatcher watcherForCallingUid(android.os.IBinder token, int callingUid) {
        if (android.os.Process.isApplicationUid(callingUid)) {
            return this.mAppTokenWatcher;
        }
        if (callingUid == 1000 && (token instanceof com.android.server.wm.LockTaskController.LockTaskToken)) {
            return this.mSystemTokenWatcher;
        }
        throw new java.lang.UnsupportedOperationException("Only apps can use the KeyguardLock API");
    }

    private boolean shouldKeyguardBeEnabled(int userId) {
        boolean dpmRequiresPassword = this.mInjector.dpmRequiresPassword(this.mCurrentUser);
        boolean keyguardSecure = this.mInjector.isKeyguardSecure(this.mCurrentUser);
        boolean allowedFromApps = (dpmRequiresPassword || keyguardSecure) ? false : true;
        boolean allowedFromSystem = !dpmRequiresPassword;
        boolean shouldBeDisabled = (allowedFromApps && this.mAppTokenWatcher.isAcquired(userId)) || (allowedFromSystem && this.mSystemTokenWatcher.isAcquired(userId));
        return !shouldBeDisabled;
    }

    static com.android.server.wm.KeyguardDisableHandler create(android.content.Context context, final com.android.server.policy.WindowManagerPolicy policy, android.os.Handler handler) {
        final com.android.server.pm.UserManagerInternal userManager = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        return new com.android.server.wm.KeyguardDisableHandler(new com.android.server.wm.KeyguardDisableHandler.Injector() { // from class: com.android.server.wm.KeyguardDisableHandler.2
            @Override // com.android.server.wm.KeyguardDisableHandler.Injector
            public boolean dpmRequiresPassword(int userId) {
                return android.app.admin.DevicePolicyCache.getInstance().getPasswordQuality(userId) != 0;
            }

            @Override // com.android.server.wm.KeyguardDisableHandler.Injector
            public boolean isKeyguardSecure(int userId) {
                return policy.isKeyguardSecure(userId);
            }

            @Override // com.android.server.wm.KeyguardDisableHandler.Injector
            public int getProfileParentId(int userId) {
                return userManager.getProfileParentId(userId);
            }

            @Override // com.android.server.wm.KeyguardDisableHandler.Injector
            public void enableKeyguard(boolean enabled) {
                policy.enableKeyguard(enabled);
            }
        }, handler);
    }
}
