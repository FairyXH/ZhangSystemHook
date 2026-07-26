package com.android.server.smartspace;

/* JADX INFO: loaded from: classes3.dex */
public class SmartspaceManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.smartspace.SmartspaceManagerService, com.android.server.smartspace.SmartspacePerUserService> {
    private static final boolean DEBUG = false;
    private static final int MAX_TEMP_SERVICE_DURATION_MS = 120000;
    private static final java.lang.String TAG = com.android.server.smartspace.SmartspaceManagerService.class.getSimpleName();
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;

    public SmartspaceManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_devicePolicyManagementUpdater), null, 17);
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.smartspace.SmartspacePerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.smartspace.SmartspacePerUserService(this, this.mLock, resolvedUserId);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("smartspace", new com.android.server.smartspace.SmartspaceManagerService.SmartspaceManagerStub());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_SMARTSPACE", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatedLocked(int userId) {
        com.android.server.smartspace.SmartspacePerUserService service = peekServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageUpdatedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageRestartedLocked(int userId) {
        com.android.server.smartspace.SmartspacePerUserService service = peekServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageRestartedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 120000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SmartspaceManagerStub extends android.app.smartspace.ISmartspaceManager.Stub {
        private SmartspaceManagerStub() {
        }

        public void createSmartspaceSession(final android.app.smartspace.SmartspaceConfig smartspaceConfig, final android.app.smartspace.SmartspaceSessionId sessionId, final android.os.IBinder token) {
            runForUserLocked("createSmartspaceSession", sessionId, new java.util.function.Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.smartspace.SmartspacePerUserService) obj).onCreateSmartspaceSessionLocked(smartspaceConfig, sessionId, token);
                }
            });
        }

        public void notifySmartspaceEvent(final android.app.smartspace.SmartspaceSessionId sessionId, final android.app.smartspace.SmartspaceTargetEvent event) {
            runForUserLocked("notifySmartspaceEvent", sessionId, new java.util.function.Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.smartspace.SmartspacePerUserService) obj).notifySmartspaceEventLocked(sessionId, event);
                }
            });
        }

        public void requestSmartspaceUpdate(final android.app.smartspace.SmartspaceSessionId sessionId) {
            runForUserLocked("requestSmartspaceUpdate", sessionId, new java.util.function.Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.smartspace.SmartspacePerUserService) obj).requestSmartspaceUpdateLocked(sessionId);
                }
            });
        }

        public void registerSmartspaceUpdates(final android.app.smartspace.SmartspaceSessionId sessionId, final android.app.smartspace.ISmartspaceCallback callback) {
            runForUserLocked("registerSmartspaceUpdates", sessionId, new java.util.function.Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.smartspace.SmartspacePerUserService) obj).registerSmartspaceUpdatesLocked(sessionId, callback);
                }
            });
        }

        public void unregisterSmartspaceUpdates(final android.app.smartspace.SmartspaceSessionId sessionId, final android.app.smartspace.ISmartspaceCallback callback) {
            runForUserLocked("unregisterSmartspaceUpdates", sessionId, new java.util.function.Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.smartspace.SmartspacePerUserService) obj).unregisterSmartspaceUpdatesLocked(sessionId, callback);
                }
            });
        }

        public void destroySmartspaceSession(final android.app.smartspace.SmartspaceSessionId sessionId) {
            runForUserLocked("destroySmartspaceSession", sessionId, new java.util.function.Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.smartspace.SmartspacePerUserService) obj).onDestroyLocked(sessionId);
                }
            });
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.smartspace.SmartspaceManagerServiceShellCommand(com.android.server.smartspace.SmartspaceManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        private void runForUserLocked(java.lang.String func, android.app.smartspace.SmartspaceSessionId sessionId, java.util.function.Consumer<com.android.server.smartspace.SmartspacePerUserService> c) {
            android.app.ActivityManagerInternal am = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            int userId = am.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), sessionId.getUserHandle().getIdentifier(), false, 0, (java.lang.String) null, (java.lang.String) null);
            android.content.Context ctx = com.android.server.smartspace.SmartspaceManagerService.this.getContext();
            if (ctx.checkCallingPermission("android.permission.MANAGE_SMARTSPACE") != 0 && ((!com.android.internal.hidden_from_bootclasspath.android.app.smartspace.flags.Flags.accessSmartspace() || ctx.checkCallingPermission("android.permission.ACCESS_SMARTSPACE") != 0) && !com.android.server.smartspace.SmartspaceManagerService.this.mServiceNameResolver.isTemporary(userId) && !com.android.server.smartspace.SmartspaceManagerService.this.mActivityTaskManagerInternal.isCallerRecents(android.os.Binder.getCallingUid()))) {
                java.lang.String msg = "Permission Denial: Cannot call " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid();
                android.util.Slog.w(com.android.server.smartspace.SmartspaceManagerService.TAG, msg);
                throw new java.lang.SecurityException(msg);
            }
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.smartspace.SmartspaceManagerService.this.mLock) {
                    com.android.server.smartspace.SmartspacePerUserService service = (com.android.server.smartspace.SmartspacePerUserService) com.android.server.smartspace.SmartspaceManagerService.this.getServiceForUserLocked(userId);
                    c.accept(service);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }
    }
}
