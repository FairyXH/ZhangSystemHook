package com.android.server.translation;

/* JADX INFO: loaded from: classes3.dex */
public final class TranslationManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.translation.TranslationManagerService, com.android.server.translation.TranslationManagerServiceImpl> {
    private static final int MAX_TEMP_SERVICE_SUBSTITUTION_DURATION_MS = 120000;
    private static final java.lang.String TAG = "TranslationManagerService";

    public TranslationManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_deviceSpecificDeviceStatePolicyProvider), null, 4);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.translation.TranslationManagerServiceImpl newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.translation.TranslationManagerServiceImpl(this, this.mLock, resolvedUserId, disabled);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_UI_TRANSLATION", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 120000;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        super.dumpLocked(prefix, pw);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCallerHasPermission(java.lang.String permission) {
        java.lang.String msg = "Permission Denial from pid =" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " doesn't hold " + permission;
        getContext().enforceCallingPermission(permission, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDefaultServiceLocked(int userId) {
        java.lang.String defaultServiceName = this.mServiceNameResolver.getDefaultServiceName(userId);
        if (defaultServiceName == null) {
            return false;
        }
        java.lang.String currentServiceName = this.mServiceNameResolver.getServiceName(userId);
        return defaultServiceName.equals(currentServiceName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCalledByServiceAppLocked(int userId, java.lang.String methodName) {
        int callingUid = android.os.Binder.getCallingUid();
        java.lang.String serviceName = this.mServiceNameResolver.getServiceName(userId);
        if (serviceName == null) {
            android.util.Slog.e(TAG, methodName + ": called by UID " + callingUid + ", but there's no service set for user " + userId);
            return false;
        }
        android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
        if (serviceComponent == null) {
            android.util.Slog.w(TAG, methodName + ": invalid service name: " + serviceName);
            return false;
        }
        java.lang.String servicePackageName = serviceComponent.getPackageName();
        android.content.pm.PackageManager pm = getContext().getPackageManager();
        try {
            int serviceUid = pm.getPackageUidAsUser(servicePackageName, userId);
            if (callingUid != serviceUid) {
                android.util.Slog.e(TAG, methodName + ": called by UID " + callingUid + ", but service UID is " + serviceUid);
                return false;
            }
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, methodName + ": could not verify UID for " + serviceName);
            return false;
        }
    }

    final class TranslationManagerServiceStub extends android.view.translation.ITranslationManager.Stub {
        TranslationManagerServiceStub() {
        }

        public void onTranslationCapabilitiesRequest(int sourceFormat, int targetFormat, android.os.ResultReceiver receiver, int userId) throws android.os.RemoteException {
            synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                com.android.server.translation.TranslationManagerServiceImpl service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
                if (service != null && (com.android.server.translation.TranslationManagerService.this.isDefaultServiceLocked(userId) || com.android.server.translation.TranslationManagerService.this.isCalledByServiceAppLocked(userId, "getTranslationCapabilities"))) {
                    service.onTranslationCapabilitiesRequestLocked(sourceFormat, targetFormat, receiver);
                } else {
                    android.util.Slog.v(com.android.server.translation.TranslationManagerService.TAG, "onGetTranslationCapabilitiesLocked(): no service for " + userId);
                    receiver.send(2, null);
                }
            }
        }

        public void registerTranslationCapabilityCallback(android.os.IRemoteCallback callback, int userId) {
            com.android.server.translation.TranslationManagerServiceImpl service;
            synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
            }
            if (service != null) {
                service.registerTranslationCapabilityCallback(callback, android.os.Binder.getCallingUid());
            }
        }

        public void unregisterTranslationCapabilityCallback(android.os.IRemoteCallback callback, int userId) {
            com.android.server.translation.TranslationManagerServiceImpl service;
            synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
            }
            if (service != null) {
                service.unregisterTranslationCapabilityCallback(callback);
            }
        }

        public void onSessionCreated(android.view.translation.TranslationContext translationContext, int sessionId, com.android.internal.os.IResultReceiver receiver, int userId) throws android.os.RemoteException {
            synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                com.android.server.translation.TranslationManagerServiceImpl service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
                if (service != null && (com.android.server.translation.TranslationManagerService.this.isDefaultServiceLocked(userId) || com.android.server.translation.TranslationManagerService.this.isCalledByServiceAppLocked(userId, "onSessionCreated"))) {
                    service.onSessionCreatedLocked(translationContext, sessionId, receiver);
                } else {
                    android.util.Slog.v(com.android.server.translation.TranslationManagerService.TAG, "onSessionCreated(): no service for " + userId);
                    receiver.send(2, (android.os.Bundle) null);
                }
            }
        }

        public void updateUiTranslationState(int state, android.view.translation.TranslationSpec sourceSpec, android.view.translation.TranslationSpec targetSpec, java.util.List<android.view.autofill.AutofillId> viewIds, android.os.IBinder token, int taskId, android.view.translation.UiTranslationSpec uiTranslationSpec, int userId) {
            com.android.server.translation.TranslationManagerService.this.enforceCallerHasPermission("android.permission.MANAGE_UI_TRANSLATION");
            synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                com.android.server.translation.TranslationManagerServiceImpl service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
                if (service != null && (com.android.server.translation.TranslationManagerService.this.isDefaultServiceLocked(userId) || com.android.server.translation.TranslationManagerService.this.isCalledByServiceAppLocked(userId, "updateUiTranslationState"))) {
                    service.updateUiTranslationStateLocked(state, sourceSpec, targetSpec, viewIds, token, taskId, uiTranslationSpec);
                }
            }
        }

        public void registerUiTranslationStateCallback(android.os.IRemoteCallback callback, int userId) {
            synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                com.android.server.translation.TranslationManagerServiceImpl service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
                if (service != null) {
                    service.registerUiTranslationStateCallbackLocked(callback, android.os.Binder.getCallingUid());
                }
            }
        }

        public void unregisterUiTranslationStateCallback(android.os.IRemoteCallback callback, int userId) {
            com.android.server.translation.TranslationManagerServiceImpl service;
            synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
            }
            if (service != null) {
                service.unregisterUiTranslationStateCallback(callback);
            }
        }

        public void onTranslationFinished(boolean activityDestroyed, android.os.IBinder token, android.content.ComponentName componentName, int userId) {
            synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                com.android.server.translation.TranslationManagerServiceImpl service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
                service.onTranslationFinishedLocked(activityDestroyed, token, componentName);
            }
        }

        public void getServiceSettingsActivity(com.android.internal.os.IResultReceiver result, int userId) {
            com.android.server.translation.TranslationManagerServiceImpl service;
            synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
            }
            if (service == null) {
                try {
                    result.send(2, (android.os.Bundle) null);
                    return;
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.translation.TranslationManagerService.TAG, "Unable to send getServiceSettingsActivity(): " + e);
                    return;
                }
            }
            android.content.ComponentName componentName = service.getServiceSettingsActivityLocked();
            if (componentName == null) {
                try {
                    result.send(1, (android.os.Bundle) null);
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.w(com.android.server.translation.TranslationManagerService.TAG, "Unable to send getServiceSettingsActivity(): " + e2);
                }
            }
            android.content.Intent intent = new android.content.Intent();
            intent.setComponent(componentName);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivityAsUser(com.android.server.translation.TranslationManagerService.this.getContext(), 0, intent, 67108864, null, new android.os.UserHandle(userId));
                try {
                    result.send(1, com.android.internal.util.SyncResultReceiver.bundleFor(pendingIntent));
                } catch (android.os.RemoteException e3) {
                    android.util.Slog.w(com.android.server.translation.TranslationManagerService.TAG, "Unable to send getServiceSettingsActivity(): " + e3);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.translation.TranslationManagerService.this.getContext(), com.android.server.translation.TranslationManagerService.TAG, pw)) {
                synchronized (com.android.server.translation.TranslationManagerService.this.mLock) {
                    com.android.server.translation.TranslationManagerService.this.dumpLocked("", pw);
                    int userId = android.os.UserHandle.getCallingUserId();
                    com.android.server.translation.TranslationManagerServiceImpl service = (com.android.server.translation.TranslationManagerServiceImpl) com.android.server.translation.TranslationManagerService.this.getServiceForUserLocked(userId);
                    if (service != null) {
                        service.dumpLocked("  ", fd, pw);
                    }
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            new com.android.server.translation.TranslationManagerServiceShellCommand(com.android.server.translation.TranslationManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("translation", new com.android.server.translation.TranslationManagerService.TranslationManagerServiceStub());
    }
}
