package com.android.server.searchui;

/* JADX INFO: loaded from: classes3.dex */
public class SearchUiManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.searchui.SearchUiManagerService, com.android.server.searchui.SearchUiPerUserService> {
    private static final boolean DEBUG = false;
    private static final int MAX_TEMP_SERVICE_DURATION_MS = 120000;
    private static final java.lang.String TAG = com.android.server.searchui.SearchUiManagerService.class.getSimpleName();
    private com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;

    public SearchUiManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_default_dns_server), null, 17);
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.searchui.SearchUiPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.searchui.SearchUiPerUserService(this, this.mLock, resolvedUserId);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("search_ui", new com.android.server.searchui.SearchUiManagerService.SearchUiManagerStub());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_SEARCH_UI", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatedLocked(int userId) {
        com.android.server.searchui.SearchUiPerUserService service = peekServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageUpdatedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageRestartedLocked(int userId) {
        com.android.server.searchui.SearchUiPerUserService service = peekServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageRestartedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 120000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SearchUiManagerStub extends android.app.search.ISearchUiManager.Stub {
        private SearchUiManagerStub() {
        }

        public void createSearchSession(final android.app.search.SearchContext context, final android.app.search.SearchSessionId sessionId, final android.os.IBinder token) {
            runForUserLocked("createSearchSession", sessionId, new java.util.function.Consumer() { // from class: com.android.server.searchui.SearchUiManagerService$SearchUiManagerStub$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.searchui.SearchUiPerUserService) obj).onCreateSearchSessionLocked(context, sessionId, token);
                }
            });
        }

        public void notifyEvent(final android.app.search.SearchSessionId sessionId, final android.app.search.Query query, final android.app.search.SearchTargetEvent event) {
            runForUserLocked("notifyEvent", sessionId, new java.util.function.Consumer() { // from class: com.android.server.searchui.SearchUiManagerService$SearchUiManagerStub$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.searchui.SearchUiPerUserService) obj).notifyLocked(sessionId, query, event);
                }
            });
        }

        public void query(final android.app.search.SearchSessionId sessionId, final android.app.search.Query query, final android.app.search.ISearchCallback callback) {
            runForUserLocked("query", sessionId, new java.util.function.Consumer() { // from class: com.android.server.searchui.SearchUiManagerService$SearchUiManagerStub$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.searchui.SearchUiPerUserService) obj).queryLocked(sessionId, query, callback);
                }
            });
        }

        public void registerEmptyQueryResultUpdateCallback(final android.app.search.SearchSessionId sessionId, final android.app.search.ISearchCallback callback) {
            runForUserLocked("registerEmptyQueryResultUpdateCallback", sessionId, new java.util.function.Consumer() { // from class: com.android.server.searchui.SearchUiManagerService$SearchUiManagerStub$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.searchui.SearchUiPerUserService) obj).registerEmptyQueryResultUpdateCallbackLocked(sessionId, callback);
                }
            });
        }

        public void unregisterEmptyQueryResultUpdateCallback(final android.app.search.SearchSessionId sessionId, final android.app.search.ISearchCallback callback) {
            runForUserLocked("unregisterEmptyQueryResultUpdateCallback", sessionId, new java.util.function.Consumer() { // from class: com.android.server.searchui.SearchUiManagerService$SearchUiManagerStub$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.searchui.SearchUiPerUserService) obj).unregisterEmptyQueryResultUpdateCallbackLocked(sessionId, callback);
                }
            });
        }

        public void destroySearchSession(final android.app.search.SearchSessionId sessionId) {
            runForUserLocked("destroySearchSession", sessionId, new java.util.function.Consumer() { // from class: com.android.server.searchui.SearchUiManagerService$SearchUiManagerStub$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.searchui.SearchUiPerUserService) obj).onDestroyLocked(sessionId);
                }
            });
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.searchui.SearchUiManagerServiceShellCommand(com.android.server.searchui.SearchUiManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        private void runForUserLocked(java.lang.String func, android.app.search.SearchSessionId sessionId, java.util.function.Consumer<com.android.server.searchui.SearchUiPerUserService> c) {
            android.app.ActivityManagerInternal am = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            int userId = am.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), sessionId.getUserId(), false, 0, (java.lang.String) null, (java.lang.String) null);
            if (!com.android.server.searchui.SearchUiManagerService.this.mServiceNameResolver.isTemporary(userId) && !com.android.server.searchui.SearchUiManagerService.this.mActivityTaskManagerInternal.isCallerRecents(android.os.Binder.getCallingUid())) {
                java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid();
                android.util.Slog.w(com.android.server.searchui.SearchUiManagerService.TAG, msg);
                throw new java.lang.SecurityException(msg);
            }
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.searchui.SearchUiManagerService.this.mLock) {
                    com.android.server.searchui.SearchUiPerUserService service = (com.android.server.searchui.SearchUiPerUserService) com.android.server.searchui.SearchUiManagerService.this.getServiceForUserLocked(userId);
                    c.accept(service);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }
    }
}
