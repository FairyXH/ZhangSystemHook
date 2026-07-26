package com.android.server.contentsuggestions;

/* JADX INFO: loaded from: classes.dex */
public final class ContentSuggestionsPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.contentsuggestions.ContentSuggestionsPerUserService, com.android.server.contentsuggestions.ContentSuggestionsManagerService> {
    private static final java.lang.String TAG = com.android.server.contentsuggestions.ContentSuggestionsPerUserService.class.getSimpleName();
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
    private com.android.server.contentsuggestions.RemoteContentSuggestionsService mRemoteService;

    ContentSuggestionsPerUserService(com.android.server.contentsuggestions.ContentSuggestionsManagerService master, java.lang.Object lock, int userId) {
        super(master, lock, userId);
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ServiceInfo si = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 128L, this.mUserId);
            if (!"android.permission.BIND_CONTENT_SUGGESTIONS_SERVICE".equals(si.permission)) {
                android.util.Slog.w(TAG, "ContentSuggestionsService from '" + si.packageName + "' does not require permission android.permission.BIND_CONTENT_SUGGESTIONS_SERVICE");
                throw new java.lang.SecurityException("Service does not require permission android.permission.BIND_CONTENT_SUGGESTIONS_SERVICE");
            }
            return si;
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected boolean updateLocked(boolean disabled) {
        boolean enabledChanged = super.updateLocked(disabled);
        updateRemoteServiceLocked();
        return enabledChanged;
    }

    void provideContextImageFromBitmapLocked(android.os.Bundle bitmapContainingExtras) {
        provideContextImageLocked(-1, null, 0, bitmapContainingExtras);
    }

    void provideContextImageLocked(int taskId, android.hardware.HardwareBuffer snapshot, int colorSpaceIdForSnapshot, android.os.Bundle imageContextRequestExtras) {
        com.android.server.contentsuggestions.RemoteContentSuggestionsService service = ensureRemoteServiceLocked();
        if (service != null) {
            service.provideContextImage(taskId, snapshot, colorSpaceIdForSnapshot, imageContextRequestExtras);
        }
    }

    void suggestContentSelectionsLocked(android.app.contentsuggestions.SelectionsRequest selectionsRequest, android.app.contentsuggestions.ISelectionsCallback selectionsCallback) {
        com.android.server.contentsuggestions.RemoteContentSuggestionsService service = ensureRemoteServiceLocked();
        if (service != null) {
            service.suggestContentSelections(selectionsRequest, selectionsCallback);
        }
    }

    void classifyContentSelectionsLocked(android.app.contentsuggestions.ClassificationsRequest classificationsRequest, android.app.contentsuggestions.IClassificationsCallback callback) {
        com.android.server.contentsuggestions.RemoteContentSuggestionsService service = ensureRemoteServiceLocked();
        if (service != null) {
            service.classifyContentSelections(classificationsRequest, callback);
        }
    }

    void notifyInteractionLocked(java.lang.String requestId, android.os.Bundle bundle) {
        com.android.server.contentsuggestions.RemoteContentSuggestionsService service = ensureRemoteServiceLocked();
        if (service != null) {
            service.notifyInteraction(requestId, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRemoteServiceLocked() {
        if (this.mRemoteService != null) {
            this.mRemoteService.destroy();
            this.mRemoteService = null;
        }
    }

    private com.android.server.contentsuggestions.RemoteContentSuggestionsService ensureRemoteServiceLocked() {
        if (this.mRemoteService == null) {
            java.lang.String serviceName = getComponentNameLocked();
            if (serviceName == null) {
                if (((com.android.server.contentsuggestions.ContentSuggestionsManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "ensureRemoteServiceLocked(): not set");
                    return null;
                }
                return null;
            }
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            this.mRemoteService = new com.android.server.contentsuggestions.RemoteContentSuggestionsService(getContext(), serviceComponent, this.mUserId, new com.android.server.contentsuggestions.RemoteContentSuggestionsService.Callbacks() { // from class: com.android.server.contentsuggestions.ContentSuggestionsPerUserService.1
                public void onServiceDied(com.android.server.contentsuggestions.RemoteContentSuggestionsService service) {
                    android.util.Slog.w(com.android.server.contentsuggestions.ContentSuggestionsPerUserService.TAG, "remote content suggestions service died");
                    com.android.server.contentsuggestions.ContentSuggestionsPerUserService.this.updateRemoteServiceLocked();
                }
            }, ((com.android.server.contentsuggestions.ContentSuggestionsManagerService) this.mMaster).isBindInstantServiceAllowed(), ((com.android.server.contentsuggestions.ContentSuggestionsManagerService) this.mMaster).verbose);
        }
        return this.mRemoteService;
    }
}
