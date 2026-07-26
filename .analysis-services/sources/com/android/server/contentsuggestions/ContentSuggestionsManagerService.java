package com.android.server.contentsuggestions;

/* JADX INFO: loaded from: classes.dex */
public class ContentSuggestionsManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.contentsuggestions.ContentSuggestionsManagerService, com.android.server.contentsuggestions.ContentSuggestionsPerUserService> {
    private static final java.lang.String EXTRA_BITMAP = "android.contentsuggestions.extra.BITMAP";
    private static final int MAX_TEMP_SERVICE_DURATION_MS = 120000;
    private static final java.lang.String TAG = com.android.server.contentsuggestions.ContentSuggestionsManagerService.class.getSimpleName();
    private static final boolean VERBOSE = false;
    private com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
    public com.android.server.contentsuggestions.IContentSuggestionsManagerServiceExt mContentSuggestionsManagerServiceExt;

    public ContentSuggestionsManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_defaultDockManagerPackageName), "no_content_suggestions");
        this.mContentSuggestionsManagerServiceExt = (com.android.server.contentsuggestions.IContentSuggestionsManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.contentsuggestions.IContentSuggestionsManagerServiceExt.class).create();
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mContentSuggestionsManagerServiceExt.initContentSuggestionsExAndInner(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.contentsuggestions.ContentSuggestionsPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.contentsuggestions.ContentSuggestionsPerUserService(this, this.mLock, resolvedUserId);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("content_suggestions", new com.android.server.contentsuggestions.ContentSuggestionsManagerService.ContentSuggestionsManagerStub());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_CONTENT_SUGGESTIONS", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 120000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCaller(int userId, java.lang.String func) {
        android.content.Context ctx = getContext();
        if (ctx.checkCallingPermission("android.permission.MANAGE_CONTENT_SUGGESTIONS") == 0 || this.mServiceNameResolver.isTemporary(userId) || this.mActivityTaskManagerInternal.isCallerRecents(android.os.Binder.getCallingUid()) || this.mContentSuggestionsManagerServiceExt.enforceCallerExt(userId, func)) {
            return;
        }
        java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " expected caller is recents";
        android.util.Slog.w(TAG, msg);
        throw new java.lang.SecurityException(msg);
    }

    private class ContentSuggestionsManagerStub extends android.app.contentsuggestions.IContentSuggestionsManager.Stub {
        private ContentSuggestionsManagerStub() {
        }

        public void provideContextBitmap(int userId, android.graphics.Bitmap bitmap, android.os.Bundle imageContextRequestExtras) {
            if (bitmap == null) {
                throw new java.lang.IllegalArgumentException("Expected non-null bitmap");
            }
            if (imageContextRequestExtras == null) {
                throw new java.lang.IllegalArgumentException("Expected non-null imageContextRequestExtras");
            }
            com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.enforceCaller(android.os.UserHandle.getCallingUserId(), "provideContextBitmap");
            synchronized (com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.mLock) {
                com.android.server.contentsuggestions.ContentSuggestionsPerUserService service = (com.android.server.contentsuggestions.ContentSuggestionsPerUserService) com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.getServiceForUserLocked(userId);
                if (service != null) {
                    imageContextRequestExtras.putParcelable(com.android.server.contentsuggestions.ContentSuggestionsManagerService.EXTRA_BITMAP, bitmap);
                    service.provideContextImageFromBitmapLocked(imageContextRequestExtras);
                }
            }
        }

        public void provideContextImage(int userId, int taskId, android.os.Bundle imageContextRequestExtras) {
            android.window.TaskSnapshot snapshot;
            if (imageContextRequestExtras == null) {
                throw new java.lang.IllegalArgumentException("Expected non-null imageContextRequestExtras");
            }
            com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.enforceCaller(android.os.UserHandle.getCallingUserId(), "provideContextImage");
            android.hardware.HardwareBuffer snapshotBuffer = null;
            int colorSpaceId = 0;
            if (!imageContextRequestExtras.containsKey(com.android.server.contentsuggestions.ContentSuggestionsManagerService.EXTRA_BITMAP) && (snapshot = com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.mActivityTaskManagerInternal.getTaskSnapshotBlocking(taskId, false)) != null) {
                snapshotBuffer = snapshot.getHardwareBuffer();
                android.graphics.ColorSpace colorSpace = snapshot.getColorSpace();
                if (colorSpace != null) {
                    colorSpaceId = colorSpace.getId();
                }
            }
            synchronized (com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.mLock) {
                com.android.server.contentsuggestions.ContentSuggestionsPerUserService service = (com.android.server.contentsuggestions.ContentSuggestionsPerUserService) com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.getServiceForUserLocked(userId);
                if (service != null) {
                    service.provideContextImageLocked(taskId, snapshotBuffer, colorSpaceId, imageContextRequestExtras);
                }
            }
        }

        public void suggestContentSelections(int userId, android.app.contentsuggestions.SelectionsRequest selectionsRequest, android.app.contentsuggestions.ISelectionsCallback selectionsCallback) {
            com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.enforceCaller(android.os.UserHandle.getCallingUserId(), "suggestContentSelections");
            synchronized (com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.mLock) {
                com.android.server.contentsuggestions.ContentSuggestionsPerUserService service = (com.android.server.contentsuggestions.ContentSuggestionsPerUserService) com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.getServiceForUserLocked(userId);
                if (service != null) {
                    service.suggestContentSelectionsLocked(selectionsRequest, selectionsCallback);
                }
            }
        }

        public void classifyContentSelections(int userId, android.app.contentsuggestions.ClassificationsRequest classificationsRequest, android.app.contentsuggestions.IClassificationsCallback callback) {
            com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.enforceCaller(android.os.UserHandle.getCallingUserId(), "classifyContentSelections");
            synchronized (com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.mLock) {
                com.android.server.contentsuggestions.ContentSuggestionsPerUserService service = (com.android.server.contentsuggestions.ContentSuggestionsPerUserService) com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.getServiceForUserLocked(userId);
                if (service != null) {
                    service.classifyContentSelectionsLocked(classificationsRequest, callback);
                }
            }
        }

        public void notifyInteraction(int userId, java.lang.String requestId, android.os.Bundle bundle) {
            com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.enforceCaller(android.os.UserHandle.getCallingUserId(), "notifyInteraction");
            synchronized (com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.mLock) {
                com.android.server.contentsuggestions.ContentSuggestionsPerUserService service = (com.android.server.contentsuggestions.ContentSuggestionsPerUserService) com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.getServiceForUserLocked(userId);
                if (service != null) {
                    service.notifyInteractionLocked(requestId, bundle);
                }
            }
        }

        public void isEnabled(int userId, com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            boolean isDisabled;
            com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.enforceCaller(android.os.UserHandle.getCallingUserId(), "isEnabled");
            synchronized (com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.mLock) {
                isDisabled = com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.isDisabledLocked(userId);
            }
            receiver.send(isDisabled ? 0 : 1, (android.os.Bundle) null);
        }

        public void resetTemporaryService(int userId) {
            com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.resetTemporaryService(userId);
        }

        public void setTemporaryService(int userId, java.lang.String serviceName, int duration) {
            com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.setTemporaryService(userId, serviceName, duration);
        }

        public void setDefaultServiceEnabled(int userId, boolean enabled) {
            com.android.server.contentsuggestions.ContentSuggestionsManagerService.this.setDefaultServiceEnabled(userId, enabled);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            int callingUid = android.os.Binder.getCallingUid();
            if (callingUid != 2000 && callingUid != 0) {
                android.util.Slog.e(com.android.server.contentsuggestions.ContentSuggestionsManagerService.TAG, "Expected shell caller");
            } else {
                new com.android.server.contentsuggestions.ContentSuggestionsManagerServiceShellCommand(com.android.server.contentsuggestions.ContentSuggestionsManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
            }
        }
    }
}
