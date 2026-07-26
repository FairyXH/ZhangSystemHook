package com.android.server.appprediction;

/* JADX INFO: loaded from: classes.dex */
public class AppPredictionManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.appprediction.AppPredictionManagerService, com.android.server.appprediction.AppPredictionPerUserService> {
    private static final int MAX_TEMP_SERVICE_DURATION_MS = 120000;
    private static final java.lang.String TAG = com.android.server.appprediction.AppPredictionManagerService.class.getSimpleName();
    private com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;

    public AppPredictionManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_defaultContextualSearchEnabled), null, 17);
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.appprediction.AppPredictionPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.appprediction.AppPredictionPerUserService(this, this.mLock, resolvedUserId);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("app_prediction", new com.android.server.appprediction.AppPredictionManagerService.PredictionManagerServiceStub());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_APP_PREDICTIONS", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatedLocked(int userId) {
        com.android.server.appprediction.AppPredictionPerUserService service = peekServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageUpdatedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageRestartedLocked(int userId) {
        com.android.server.appprediction.AppPredictionPerUserService service = peekServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageRestartedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 120000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class PredictionManagerServiceStub extends android.app.prediction.IPredictionManager.Stub {
        private PredictionManagerServiceStub() {
        }

        public void createPredictionSession(final android.app.prediction.AppPredictionContext context, final android.app.prediction.AppPredictionSessionId sessionId, final android.os.IBinder token) {
            runForUserLocked("createPredictionSession", sessionId, new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionManagerService$PredictionManagerServiceStub$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appprediction.AppPredictionPerUserService) obj).onCreatePredictionSessionLocked(context, sessionId, token);
                }
            });
        }

        public void notifyAppTargetEvent(final android.app.prediction.AppPredictionSessionId sessionId, final android.app.prediction.AppTargetEvent event) {
            runForUserLocked("notifyAppTargetEvent", sessionId, new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionManagerService$PredictionManagerServiceStub$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appprediction.AppPredictionPerUserService) obj).notifyAppTargetEventLocked(sessionId, event);
                }
            });
        }

        public void notifyLaunchLocationShown(final android.app.prediction.AppPredictionSessionId sessionId, final java.lang.String launchLocation, final android.content.pm.ParceledListSlice targetIds) {
            runForUserLocked("notifyLaunchLocationShown", sessionId, new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionManagerService$PredictionManagerServiceStub$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appprediction.AppPredictionPerUserService) obj).notifyLaunchLocationShownLocked(sessionId, launchLocation, targetIds);
                }
            });
        }

        public void sortAppTargets(final android.app.prediction.AppPredictionSessionId sessionId, final android.content.pm.ParceledListSlice targets, final android.app.prediction.IPredictionCallback callback) {
            runForUserLocked("sortAppTargets", sessionId, new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionManagerService$PredictionManagerServiceStub$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appprediction.AppPredictionPerUserService) obj).sortAppTargetsLocked(sessionId, targets, callback);
                }
            });
        }

        public void registerPredictionUpdates(final android.app.prediction.AppPredictionSessionId sessionId, final android.app.prediction.IPredictionCallback callback) {
            runForUserLocked("registerPredictionUpdates", sessionId, new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionManagerService$PredictionManagerServiceStub$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appprediction.AppPredictionPerUserService) obj).registerPredictionUpdatesLocked(sessionId, callback);
                }
            });
        }

        public void unregisterPredictionUpdates(final android.app.prediction.AppPredictionSessionId sessionId, final android.app.prediction.IPredictionCallback callback) {
            runForUserLocked("unregisterPredictionUpdates", sessionId, new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionManagerService$PredictionManagerServiceStub$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appprediction.AppPredictionPerUserService) obj).unregisterPredictionUpdatesLocked(sessionId, callback);
                }
            });
        }

        public void requestPredictionUpdate(final android.app.prediction.AppPredictionSessionId sessionId) {
            runForUserLocked("requestPredictionUpdate", sessionId, new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionManagerService$PredictionManagerServiceStub$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appprediction.AppPredictionPerUserService) obj).requestPredictionUpdateLocked(sessionId);
                }
            });
        }

        public void onDestroyPredictionSession(final android.app.prediction.AppPredictionSessionId sessionId) {
            runForUserLocked("onDestroyPredictionSession", sessionId, new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionManagerService$PredictionManagerServiceStub$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appprediction.AppPredictionPerUserService) obj).onDestroyPredictionSessionLocked(sessionId);
                }
            });
        }

        public void requestServiceFeatures(final android.app.prediction.AppPredictionSessionId sessionId, final android.os.IRemoteCallback callback) {
            runForUserLocked("requestServiceFeatures", sessionId, new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionManagerService$PredictionManagerServiceStub$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appprediction.AppPredictionPerUserService) obj).requestServiceFeaturesLocked(sessionId, callback);
                }
            });
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.appprediction.AppPredictionManagerServiceShellCommand(com.android.server.appprediction.AppPredictionManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        private void runForUserLocked(java.lang.String func, android.app.prediction.AppPredictionSessionId sessionId, java.util.function.Consumer<com.android.server.appprediction.AppPredictionPerUserService> c) {
            android.app.ActivityManagerInternal am = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            int userId = am.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), sessionId.getUserId(), false, 0, (java.lang.String) null, (java.lang.String) null);
            android.content.Context ctx = com.android.server.appprediction.AppPredictionManagerService.this.getContext();
            if (ctx.checkCallingPermission("android.permission.PACKAGE_USAGE_STATS") != 0 && !com.android.server.appprediction.AppPredictionManagerService.this.mServiceNameResolver.isTemporary(userId) && !com.android.server.appprediction.AppPredictionManagerService.this.mActivityTaskManagerInternal.isCallerRecents(android.os.Binder.getCallingUid()) && android.os.Binder.getCallingUid() != 1000) {
                java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " expected caller to hold PACKAGE_USAGE_STATS permission";
                android.util.Slog.w(com.android.server.appprediction.AppPredictionManagerService.TAG, msg);
                throw new java.lang.SecurityException(msg);
            }
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.appprediction.AppPredictionManagerService.this.mLock) {
                    com.android.server.appprediction.AppPredictionPerUserService service = (com.android.server.appprediction.AppPredictionPerUserService) com.android.server.appprediction.AppPredictionManagerService.this.getServiceForUserLocked(userId);
                    c.accept(service);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }
    }
}
