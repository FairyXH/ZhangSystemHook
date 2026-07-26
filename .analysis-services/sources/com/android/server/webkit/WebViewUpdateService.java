package com.android.server.webkit;

/* JADX INFO: loaded from: classes3.dex */
public class WebViewUpdateService extends com.android.server.SystemService {
    static final int PACKAGE_ADDED = 1;
    static final int PACKAGE_ADDED_REPLACED = 2;
    static final int PACKAGE_CHANGED = 0;
    static final int PACKAGE_REMOVED = 3;
    private static final java.lang.String TAG = "WebViewUpdateService";
    private com.android.server.webkit.WebViewUpdateServiceInterface mImpl;
    private android.content.BroadcastReceiver mWebViewUpdatedReceiver;
    private static final com.android.modules.expresslog.Histogram sPrepareWebViewInSystemServerLatency = new com.android.modules.expresslog.Histogram("webview.value_prepare_webview_in_system_server_latency", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(20, 0, 1.0f, 1.5f));
    private static final com.android.modules.expresslog.Histogram sAppWaitingForRelroCompletionDelay = new com.android.modules.expresslog.Histogram("webview.value_app_waiting_for_relro_completion_delay", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(20, 0, 1.0f, 1.4f));

    public WebViewUpdateService(android.content.Context context) {
        super(context);
        if (android.webkit.Flags.updateServiceV2()) {
            this.mImpl = new com.android.server.webkit.WebViewUpdateServiceImpl2(context, com.android.server.webkit.SystemImpl.getInstance());
        } else {
            this.mImpl = new com.android.server.webkit.WebViewUpdateServiceImpl(context, com.android.server.webkit.SystemImpl.getInstance());
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mWebViewUpdatedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.webkit.WebViewUpdateService.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:20:0x004a  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r8, android.content.Intent r9) {
                /*
                    Method dump skipped, instruction units count: 210
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.webkit.WebViewUpdateService.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_CHANGED");
        filter.addDataScheme("package");
        filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=ENTIRE_PKG_CHANGED");
        for (android.webkit.WebViewProviderInfo provider : this.mImpl.getWebViewPackages()) {
            filter.addDataSchemeSpecificPart(provider.packageName, 0);
        }
        getContext().registerReceiverAsUser(this.mWebViewUpdatedReceiver, android.os.UserHandle.ALL, filter, null, null);
        android.content.IntentFilter userAddedFilter = new android.content.IntentFilter();
        userAddedFilter.addAction("android.intent.action.USER_STARTED");
        userAddedFilter.addAction("android.intent.action.USER_REMOVED");
        getContext().registerReceiverAsUser(this.mWebViewUpdatedReceiver, android.os.UserHandle.ALL, userAddedFilter, null, null);
        publishBinderService("webviewupdate", new com.android.server.webkit.WebViewUpdateService.BinderService(), true);
    }

    public void prepareWebViewInSystemServer() {
        long currentTimeMs = android.os.SystemClock.uptimeMillis();
        this.mImpl.prepareWebViewInSystemServer();
        sPrepareWebViewInSystemServerLatency.logSample(android.os.SystemClock.uptimeMillis() - currentTimeMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String packageNameFromIntent(android.content.Intent intent) {
        return intent.getDataString().substring("package:".length());
    }

    public static boolean entirePackageChanged(android.content.Intent intent) {
        java.lang.String[] componentList = intent.getStringArrayExtra("android.intent.extra.changed_component_name_list");
        return java.util.Arrays.asList(componentList).contains(intent.getDataString().substring("package:".length()));
    }

    private class BinderService extends android.webkit.IWebViewUpdateService.Stub {
        private BinderService() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            if (android.webkit.Flags.updateServiceV2()) {
                new com.android.server.webkit.WebViewUpdateServiceShellCommand2(this).exec(this, in, out, err, args, callback, resultReceiver);
            } else {
                new com.android.server.webkit.WebViewUpdateServiceShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
            }
        }

        public void notifyRelroCreationCompleted() {
            if (android.os.Binder.getCallingUid() != 1037 && android.os.Binder.getCallingUid() != 1000) {
                return;
            }
            long callingId = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.webkit.WebViewUpdateService.this.mImpl.notifyRelroCreationCompleted();
            } finally {
                android.os.Binder.restoreCallingIdentity(callingId);
            }
        }

        public android.webkit.WebViewProviderResponse waitForAndGetProvider() {
            if (android.os.Binder.getCallingPid() == android.os.Process.myPid()) {
                throw new java.lang.IllegalStateException("Cannot create a WebView from the SystemServer");
            }
            long startTimeMs = android.os.SystemClock.uptimeMillis();
            android.webkit.WebViewProviderResponse webViewProviderResponse = com.android.server.webkit.WebViewUpdateService.this.mImpl.waitForAndGetProvider();
            long endTimeMs = android.os.SystemClock.uptimeMillis();
            com.android.server.webkit.WebViewUpdateService.sAppWaitingForRelroCompletionDelay.logSample(endTimeMs - startTimeMs);
            if (webViewProviderResponse.packageInfo != null) {
                grantVisibilityToCaller(webViewProviderResponse.packageInfo.packageName, android.os.Binder.getCallingUid());
            }
            return webViewProviderResponse;
        }

        private void grantVisibilityToCaller(java.lang.String webViewPackageName, int callingUid) {
            android.content.pm.PackageManagerInternal pmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            int webviewUid = pmInternal.getPackageUid(webViewPackageName, 0L, android.os.UserHandle.getUserId(callingUid));
            pmInternal.grantImplicitAccess(android.os.UserHandle.getUserId(callingUid), null, android.os.UserHandle.getAppId(callingUid), webviewUid, true);
        }

        public java.lang.String changeProviderAndSetting(java.lang.String newProvider) {
            if (com.android.server.webkit.WebViewUpdateService.this.getContext().checkCallingPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
                java.lang.String msg = "Permission Denial: changeProviderAndSetting() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.WRITE_SECURE_SETTINGS";
                android.util.Slog.w(com.android.server.webkit.WebViewUpdateService.TAG, msg);
                throw new java.lang.SecurityException(msg);
            }
            long callingId = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.webkit.WebViewUpdateService.this.mImpl.changeProviderAndSetting(newProvider);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingId);
            }
        }

        public android.webkit.WebViewProviderInfo[] getValidWebViewPackages() {
            return com.android.server.webkit.WebViewUpdateService.this.mImpl.getValidWebViewPackages();
        }

        public android.webkit.WebViewProviderInfo getDefaultWebViewPackage() {
            return com.android.server.webkit.WebViewUpdateService.this.mImpl.getDefaultWebViewPackage();
        }

        public android.webkit.WebViewProviderInfo[] getAllWebViewPackages() {
            return com.android.server.webkit.WebViewUpdateService.this.mImpl.getWebViewPackages();
        }

        public java.lang.String getCurrentWebViewPackageName() {
            android.content.pm.PackageInfo pi = getCurrentWebViewPackage();
            if (pi == null) {
                return null;
            }
            return pi.packageName;
        }

        public android.content.pm.PackageInfo getCurrentWebViewPackage() {
            android.content.pm.PackageInfo currentWebViewPackage = com.android.server.webkit.WebViewUpdateService.this.mImpl.getCurrentWebViewPackage();
            if (currentWebViewPackage != null) {
                grantVisibilityToCaller(currentWebViewPackage.packageName, android.os.Binder.getCallingUid());
            }
            return currentWebViewPackage;
        }

        public boolean isMultiProcessEnabled() {
            if (android.webkit.Flags.updateServiceV2()) {
                throw new java.lang.IllegalStateException("isMultiProcessEnabled shouldn't be called if update_service_v2 flag is set.");
            }
            return com.android.server.webkit.WebViewUpdateService.this.mImpl.isMultiProcessEnabled();
        }

        public void enableMultiProcess(boolean enable) {
            if (android.webkit.Flags.updateServiceV2()) {
                throw new java.lang.IllegalStateException("enableMultiProcess shouldn't be called if update_service_v2 flag is set.");
            }
            if (com.android.server.webkit.WebViewUpdateService.this.getContext().checkCallingPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
                java.lang.String msg = "Permission Denial: enableMultiProcess() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.WRITE_SECURE_SETTINGS";
                android.util.Slog.w(com.android.server.webkit.WebViewUpdateService.TAG, msg);
                throw new java.lang.SecurityException(msg);
            }
            long callingId = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.webkit.WebViewUpdateService.this.mImpl.enableMultiProcess(enable);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingId);
            }
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.webkit.WebViewUpdateService.this.getContext(), com.android.server.webkit.WebViewUpdateService.TAG, pw)) {
                com.android.server.webkit.WebViewUpdateService.this.mImpl.dumpState(pw);
            }
        }
    }
}
