package com.android.server.contextualsearch;

/* JADX INFO: loaded from: classes.dex */
public class ContextualSearchManagerService extends com.android.server.SystemService {
    private static final int MAX_TEMP_PACKAGE_DURATION_MS = 120000;
    private static final int MAX_TOKEN_VALID_DURATION_MS = 600000;
    private static final int MSG_INVALIDATE_TOKEN = 1;
    private static final int MSG_RESET_TEMPORARY_PACKAGE = 0;
    private static final java.lang.String TAG = com.android.server.contextualsearch.ContextualSearchManagerService.class.getSimpleName();
    private final com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks mAssistDataCallbacks;
    private final com.android.server.am.AssistDataRequester mAssistDataRequester;
    private final com.android.server.wm.ActivityTaskManagerInternal mAtmInternal;
    private final android.content.Context mContext;
    public com.android.server.contextualsearch.IContextualManagerServiceExt mContextualManagerServiceExt;
    private final android.app.admin.DevicePolicyManagerInternal mDpmInternal;
    private final java.lang.Object mLock;
    private final android.content.pm.PackageManagerInternal mPackageManager;
    private android.app.contextualsearch.IContextualSearchCallback mStateCallback;
    private android.os.Handler mTemporaryHandler;
    private java.lang.String mTemporaryPackage;
    private long mTokenValidDurationMs;
    private final com.android.server.wm.WindowManagerInternal mWmInternal;

    public ContextualSearchManagerService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mContextualManagerServiceExt = (com.android.server.contextualsearch.IContextualManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.contextualsearch.IContextualManagerServiceExt.class).create();
        this.mAssistDataCallbacks = new com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks() { // from class: com.android.server.contextualsearch.ContextualSearchManagerService.1
            @Override // com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks
            public boolean canHandleReceivedAssistDataLocked() {
                boolean z;
                synchronized (com.android.server.contextualsearch.ContextualSearchManagerService.this.mLock) {
                    z = com.android.server.contextualsearch.ContextualSearchManagerService.this.mStateCallback != null;
                }
                return z;
            }

            @Override // com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks
            public void onAssistDataReceivedLocked(android.os.Bundle data, int activityIndex, int activityCount) {
                android.app.contextualsearch.IContextualSearchCallback callback;
                synchronized (com.android.server.contextualsearch.ContextualSearchManagerService.this.mLock) {
                    callback = com.android.server.contextualsearch.ContextualSearchManagerService.this.mStateCallback;
                }
                if (callback != null) {
                    try {
                        callback.onResult(new android.app.contextualsearch.ContextualSearchState((android.app.assist.AssistStructure) data.getParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_STRUCTURE, android.app.assist.AssistStructure.class), (android.app.assist.AssistContent) data.getParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT, android.app.assist.AssistContent.class), data));
                        return;
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(com.android.server.contextualsearch.ContextualSearchManagerService.TAG, "Error invoking ContextualSearchCallback", e);
                        return;
                    }
                }
                android.util.Log.w(com.android.server.contextualsearch.ContextualSearchManagerService.TAG, "Callback went away!");
            }

            @Override // com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks
            public void onAssistRequestCompleted() {
                synchronized (com.android.server.contextualsearch.ContextualSearchManagerService.this.mLock) {
                    com.android.server.contextualsearch.ContextualSearchManagerService.this.mStateCallback = null;
                }
            }
        };
        this.mTemporaryPackage = null;
        this.mTokenValidDurationMs = 600000L;
        this.mContextualManagerServiceExt.initContextualExAndInner(context);
        this.mContext = context;
        this.mAtmInternal = (com.android.server.wm.ActivityTaskManagerInternal) java.util.Objects.requireNonNull((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class));
        this.mPackageManager = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mWmInternal = (com.android.server.wm.WindowManagerInternal) java.util.Objects.requireNonNull((com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class));
        this.mDpmInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        this.mAssistDataRequester = new com.android.server.am.AssistDataRequester(this.mContext, android.view.IWindowManager.Stub.asInterface(android.os.ServiceManager.getService("window")), (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class), this.mAssistDataCallbacks, this.mLock, 49, 50);
        updateSecureSetting();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("contextual_search", new com.android.server.contextualsearch.ContextualSearchManagerService.ContextualSearchManagerStub());
    }

    private void updateSecureSetting() {
        android.provider.Settings.Secure.putString(this.mContext.getContentResolver(), "contextual_search_package", getContextualSearchPackageName());
    }

    private java.lang.String getContextualSearchPackageName() {
        java.lang.String string;
        synchronized (this) {
            string = this.mTemporaryPackage != null ? this.mTemporaryPackage : this.mContext.getResources().getString(android.R.string.config_defaultListenerAccessPackages);
        }
        return string;
    }

    void resetTemporaryPackage() {
        synchronized (this) {
            enforceOverridingPermission("resetTemporaryPackage");
            if (this.mTemporaryHandler != null) {
                this.mTemporaryHandler.removeMessages(0);
                this.mTemporaryHandler = null;
            }
            this.mTemporaryPackage = null;
            updateSecureSetting();
        }
    }

    void setTemporaryPackage(java.lang.String temporaryPackage, int durationMs) {
        synchronized (this) {
            enforceOverridingPermission("setTemporaryPackage");
            if (durationMs > 120000) {
                throw new java.lang.IllegalArgumentException("Max duration is 120000 (called with " + durationMs + ")");
            }
            if (this.mTemporaryHandler == null) {
                this.mTemporaryHandler = new android.os.Handler(android.os.Looper.getMainLooper(), null, true) { // from class: com.android.server.contextualsearch.ContextualSearchManagerService.2
                    @Override // android.os.Handler
                    public void handleMessage(android.os.Message msg) {
                        if (msg.what == 0) {
                            synchronized (this) {
                                com.android.server.contextualsearch.ContextualSearchManagerService.this.resetTemporaryPackage();
                            }
                        } else {
                            android.util.Slog.wtf(com.android.server.contextualsearch.ContextualSearchManagerService.TAG, "invalid handler msg: " + msg);
                        }
                    }
                };
            } else {
                this.mTemporaryHandler.removeMessages(0);
            }
            this.mTemporaryPackage = temporaryPackage;
            updateSecureSetting();
            this.mTemporaryHandler.sendEmptyMessageDelayed(0, durationMs);
        }
    }

    void resetTokenValidDurationMs() {
        setTokenValidDurationMs(MAX_TOKEN_VALID_DURATION_MS);
    }

    void setTokenValidDurationMs(int durationMs) {
        synchronized (this) {
            enforceOverridingPermission("setTokenValidDurationMs");
            if (durationMs > MAX_TOKEN_VALID_DURATION_MS) {
                throw new java.lang.IllegalArgumentException("Token max duration is 600000 (called with " + durationMs + ")");
            }
            this.mTokenValidDurationMs = durationMs;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getTokenValidDurationMs() {
        long j;
        synchronized (this) {
            j = this.mTokenValidDurationMs;
        }
        return j;
    }

    private android.content.Intent getResolvedLaunchIntent() {
        synchronized (this) {
            java.lang.String csPkgName = getContextualSearchPackageName();
            if (csPkgName.isEmpty()) {
                return null;
            }
            android.content.Intent launchIntent = new android.content.Intent("android.app.contextualsearch.action.LAUNCH_CONTEXTUAL_SEARCH");
            launchIntent.setPackage(csPkgName);
            android.content.pm.ResolveInfo resolveInfo = this.mContext.getPackageManager().resolveActivity(launchIntent, 2097152);
            if (resolveInfo == null) {
                return null;
            }
            android.content.ComponentName componentName = resolveInfo.getComponentInfo().getComponentName();
            if (componentName == null) {
                return null;
            }
            launchIntent.setComponent(componentName);
            return launchIntent;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Incorrect condition in loop: B:7:0x004e */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.content.Intent getContextualSearchIntent(int r22, android.app.contextualsearch.CallbackToken r23) {
        /*
            Method dump skipped, instruction units count: 281
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.contextualsearch.ContextualSearchManagerService.getContextualSearchIntent(int, android.app.contextualsearch.CallbackToken):android.content.Intent");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int invokeContextualSearchIntent(android.content.Intent launchIntent, int userId) {
        android.app.ActivityOptions opts = android.app.ActivityOptions.makeCustomTaskAnimation(this.mContext, 0, 0, null, null, null);
        opts.setDisableStartingWindow(true);
        return this.mAtmInternal.startActivityWithScreenshot(launchIntent, this.mContext.getPackageName(), android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), null, opts.toBundle(), userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforcePermission(java.lang.String func) {
        android.content.Context ctx = getContext();
        if (ctx.checkCallingPermission("android.permission.ACCESS_CONTEXTUAL_SEARCH") != 0 && !isCallerTemporary()) {
            java.lang.String msg = "Permission Denial: Cannot call " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid();
            throw new java.lang.SecurityException(msg);
        }
    }

    private void enforceOverridingPermission(java.lang.String func) {
        if (android.os.Binder.getCallingUid() != 2000 && android.os.Binder.getCallingUid() != 0 && android.os.Binder.getCallingUid() != 1000) {
            java.lang.String msg = "Permission Denial: Cannot override Contextual Search. Called " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid();
            throw new java.lang.SecurityException(msg);
        }
    }

    private boolean isCallerTemporary() {
        boolean z;
        synchronized (this) {
            z = this.mTemporaryPackage != null && this.mTemporaryPackage.equals(getContext().getPackageManager().getNameForUid(android.os.Binder.getCallingUid()));
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ContextualSearchManagerStub extends android.app.contextualsearch.IContextualSearchManager.Stub {
        private android.app.contextualsearch.CallbackToken mToken;
        private android.os.Handler mTokenHandler;

        private ContextualSearchManagerStub() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void invalidateToken() {
            synchronized (this) {
                if (this.mTokenHandler != null) {
                    this.mTokenHandler.removeMessages(1);
                    this.mTokenHandler = null;
                }
                this.mToken = null;
            }
        }

        private void issueToken() {
            synchronized (this) {
                this.mToken = new android.app.contextualsearch.CallbackToken();
                boolean z = true;
                if (this.mTokenHandler == null) {
                    this.mTokenHandler = new android.os.Handler(android.os.Looper.getMainLooper(), null, z) { // from class: com.android.server.contextualsearch.ContextualSearchManagerService.ContextualSearchManagerStub.1
                        @Override // android.os.Handler
                        public void handleMessage(android.os.Message msg) {
                            if (msg.what == 1) {
                                com.android.server.contextualsearch.ContextualSearchManagerService.ContextualSearchManagerStub.this.invalidateToken();
                            } else {
                                android.util.Slog.wtf(com.android.server.contextualsearch.ContextualSearchManagerService.TAG, "invalid token handler msg: " + msg);
                            }
                        }
                    };
                } else {
                    this.mTokenHandler.removeMessages(1);
                }
                this.mTokenHandler.sendEmptyMessageDelayed(1, com.android.server.contextualsearch.ContextualSearchManagerService.this.getTokenValidDurationMs());
            }
        }

        public void startContextualSearch(final int entrypoint) {
            synchronized (this) {
                com.android.server.contextualsearch.ContextualSearchManagerService.this.enforcePermission("startContextualSearch");
                final int callingUserId = com.android.server.contextualsearch.ContextualSearchManagerService.this.mContextualManagerServiceExt.beforeStartContextualSearchGetUserId(entrypoint);
                com.android.server.contextualsearch.ContextualSearchManagerService.this.mAssistDataRequester.cancel();
                issueToken();
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.contextualsearch.ContextualSearchManagerService$ContextualSearchManagerStub$$ExternalSyntheticLambda1
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$startContextualSearch$0(entrypoint, callingUserId);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$startContextualSearch$0(int entrypoint, int callingUserId) throws java.lang.Exception {
            android.content.Intent launchIntent = com.android.server.contextualsearch.ContextualSearchManagerService.this.getContextualSearchIntent(entrypoint, this.mToken);
            if (launchIntent != null) {
                com.android.server.contextualsearch.ContextualSearchManagerService.this.invokeContextualSearchIntent(launchIntent, callingUserId);
            }
        }

        public void getContextualSearchState(android.os.IBinder token, final android.app.contextualsearch.IContextualSearchCallback callback) {
            if (this.mToken == null || !this.mToken.getToken().equals(token)) {
                try {
                    callback.onError(new android.os.ParcelableException(new java.lang.IllegalArgumentException("Invalid token")));
                    return;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.contextualsearch.ContextualSearchManagerService.TAG, "Could not invoke onError callback", e);
                    return;
                }
            }
            invalidateToken();
            if (android.app.contextualsearch.flags.Flags.enableTokenRefresh()) {
                issueToken();
                final android.os.Bundle bundle = new android.os.Bundle();
                bundle.putParcelable("android.app.contextualsearch.extra.TOKEN", this.mToken);
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.contextualsearch.ContextualSearchManagerService$ContextualSearchManagerStub$$ExternalSyntheticLambda0
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$getContextualSearchState$1(bundle, callback);
                    }
                });
            }
            synchronized (com.android.server.contextualsearch.ContextualSearchManagerService.this.mLock) {
                com.android.server.contextualsearch.ContextualSearchManagerService.this.mStateCallback = callback;
            }
            com.android.server.contextualsearch.ContextualSearchManagerService.this.mAssistDataRequester.processPendingAssistData();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getContextualSearchState$1(android.os.Bundle bundle, android.app.contextualsearch.IContextualSearchCallback callback) throws java.lang.Exception {
            if (com.android.server.contextualsearch.ContextualSearchManagerService.this.mWmInternal != null) {
                bundle.putParcelable("android.app.contextualsearch.extra.SCREENSHOT", com.android.server.contextualsearch.ContextualSearchManagerService.this.mWmInternal.takeAssistScreenshot(java.util.Set.of(2000, 2019, 2024, 2018)).asBitmap().asShared());
            }
            try {
                callback.onResult(new android.app.contextualsearch.ContextualSearchState((android.app.assist.AssistStructure) null, (android.app.assist.AssistContent) null, bundle));
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.contextualsearch.ContextualSearchManagerService.TAG, "Error invoking ContextualSearchCallback", e);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.contextualsearch.ContextualSearchManagerShellCommand(com.android.server.contextualsearch.ContextualSearchManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }
    }
}
