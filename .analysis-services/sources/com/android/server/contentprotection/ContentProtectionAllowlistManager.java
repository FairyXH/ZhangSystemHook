package com.android.server.contentprotection;

/* JADX INFO: loaded from: classes.dex */
public class ContentProtectionAllowlistManager {
    private static final java.lang.String TAG = "ContentProtectionAllowlistManager";
    private final com.android.server.contentcapture.ContentCaptureManagerService mContentCaptureManagerService;
    private final android.os.Handler mHandler;
    private boolean mStarted;
    private final long mTimeoutMs;
    private java.time.Instant mUpdatePendingUntil;
    private final java.lang.Object mHandlerToken = new java.lang.Object();
    private final java.lang.Object mLock = new java.lang.Object();
    private java.util.Set<java.lang.String> mAllowedPackages = java.util.Set.of();
    final com.android.internal.content.PackageMonitor mPackageMonitor = createPackageMonitor();
    final android.service.contentcapture.IContentProtectionAllowlistCallback mAllowlistCallback = createAllowlistCallback();

    public ContentProtectionAllowlistManager(com.android.server.contentcapture.ContentCaptureManagerService contentCaptureManagerService, android.os.Handler handler, long timeoutMs) {
        this.mContentCaptureManagerService = contentCaptureManagerService;
        this.mHandler = handler;
        this.mTimeoutMs = timeoutMs;
    }

    public void start(long delayMs) {
        if (this.mStarted) {
            return;
        }
        this.mStarted = true;
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.contentprotection.ContentProtectionAllowlistManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.handleInitialUpdate();
            }
        }, this.mHandlerToken, delayMs);
    }

    public void stop() {
        try {
            this.mPackageMonitor.unregister();
        } catch (java.lang.IllegalStateException e) {
        }
        this.mHandler.removeCallbacksAndMessages(this.mHandlerToken);
        this.mUpdatePendingUntil = null;
        this.mStarted = false;
    }

    public boolean isAllowed(java.lang.String packageName) {
        java.util.Set<java.lang.String> allowedPackages;
        synchronized (this.mLock) {
            allowedPackages = this.mAllowedPackages;
        }
        return allowedPackages.contains(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateAllowlistResponse(java.util.List<java.lang.String> packages) {
        synchronized (this.mLock) {
            this.mAllowedPackages = (java.util.Set) packages.stream().collect(java.util.stream.Collectors.toUnmodifiableSet());
        }
        this.mUpdatePendingUntil = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitialUpdate() {
        handlePackagesChanged();
        this.mPackageMonitor.register(this.mContentCaptureManagerService.getContext(), android.os.UserHandle.ALL, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackagesChanged() {
        com.android.server.contentprotection.RemoteContentProtectionService remoteContentProtectionService;
        if ((this.mUpdatePendingUntil != null && java.time.Instant.now().isBefore(this.mUpdatePendingUntil)) || (remoteContentProtectionService = this.mContentCaptureManagerService.createRemoteContentProtectionService()) == null) {
            return;
        }
        this.mHandler.removeCallbacksAndMessages(this.mHandlerToken);
        this.mUpdatePendingUntil = java.time.Instant.now().plusMillis(this.mTimeoutMs);
        try {
            remoteContentProtectionService.onUpdateAllowlistRequest(this.mAllowlistCallback);
        } catch (java.lang.Exception ex) {
            android.util.Slog.e(TAG, "Failed to call remote service", ex);
        }
    }

    protected com.android.internal.content.PackageMonitor createPackageMonitor() {
        return new com.android.server.contentprotection.ContentProtectionAllowlistManager.ContentProtectionPackageMonitor();
    }

    protected android.service.contentcapture.IContentProtectionAllowlistCallback createAllowlistCallback() {
        return new com.android.server.contentprotection.ContentProtectionAllowlistManager.ContentProtectionAllowlistCallback();
    }

    private final class ContentProtectionPackageMonitor extends com.android.internal.content.PackageMonitor {
        private ContentProtectionPackageMonitor() {
        }

        public void onSomePackagesChanged() {
            com.android.server.contentprotection.ContentProtectionAllowlistManager.this.handlePackagesChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class ContentProtectionAllowlistCallback extends android.service.contentcapture.IContentProtectionAllowlistCallback.Stub {
        private ContentProtectionAllowlistCallback() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setAllowlist$0(java.util.List packages) {
            com.android.server.contentprotection.ContentProtectionAllowlistManager.this.handleUpdateAllowlistResponse(packages);
        }

        public void setAllowlist(final java.util.List<java.lang.String> packages) {
            com.android.server.contentprotection.ContentProtectionAllowlistManager.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.contentprotection.ContentProtectionAllowlistManager$ContentProtectionAllowlistCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setAllowlist$0(packages);
                }
            });
        }
    }
}
