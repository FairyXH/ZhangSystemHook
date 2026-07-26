package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerMiddlewarePermission implements com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal, com.android.server.soundtrigger_middleware.Dumpable {
    private static final java.lang.String TAG = "SoundTriggerMiddlewarePermission";
    private final android.content.Context mContext;
    private final com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal mDelegate;

    public SoundTriggerMiddlewarePermission(com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal delegate, android.content.Context context) {
        this.mDelegate = delegate;
        this.mContext = context;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] listModules() {
        android.media.permission.Identity identity = getIdentity();
        enforcePermissionForPreflight(this.mContext, identity, "android.permission.CAPTURE_AUDIO_HOTWORD", true);
        return this.mDelegate.listModules();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public android.media.soundtrigger_middleware.ISoundTriggerModule attach(int handle, android.media.soundtrigger_middleware.ISoundTriggerCallback callback, boolean isTrusted) {
        android.media.permission.Identity identity = getIdentity();
        enforcePermissionsForPreflight(identity);
        com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.ModuleWrapper wrapper = new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.ModuleWrapper(identity, callback, isTrusted);
        return wrapper.attach(this.mDelegate.attach(handle, wrapper.getCallbackWrapper(), isTrusted));
    }

    public java.lang.String toString() {
        return java.util.Objects.toString(this.mDelegate);
    }

    private static android.media.permission.Identity getIdentity() {
        return android.media.permission.IdentityContext.getNonNull();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforcePermissionsForPreflight(android.media.permission.Identity identity) {
        enforcePermissionForPreflight(this.mContext, identity, "android.permission.RECORD_AUDIO", true);
        enforcePermissionForPreflight(this.mContext, identity, "android.permission.CAPTURE_AUDIO_HOTWORD", true);
    }

    void enforcePermissionsForDataDelivery(android.media.permission.Identity identity, java.lang.String reason) {
        enforceSoundTriggerRecordAudioPermissionForDataDelivery(identity, reason);
        enforcePermissionForDataDelivery(this.mContext, identity, "android.permission.CAPTURE_AUDIO_HOTWORD", reason);
    }

    private static void enforcePermissionForDataDelivery(android.content.Context context, android.media.permission.Identity identity, java.lang.String permission, java.lang.String reason) {
        int status = android.media.permission.PermissionUtil.checkPermissionForDataDelivery(context, identity, permission, reason);
        if (status != 0) {
            throw new java.lang.SecurityException(java.lang.String.format("Failed to obtain permission %s for identity %s", permission, com.android.server.soundtrigger_middleware.ObjectPrinter.print(identity, 16)));
        }
    }

    private static void enforceSoundTriggerRecordAudioPermissionForDataDelivery(android.media.permission.Identity identity, java.lang.String reason) {
        com.android.server.pm.permission.LegacyPermissionManagerInternal lpmi = (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);
        int status = lpmi.checkSoundTriggerRecordAudioPermissionForDataDelivery(identity.uid, identity.packageName, identity.attributionTag, reason);
        if (status != 0) {
            throw new java.lang.SecurityException(java.lang.String.format("Failed to obtain permission RECORD_AUDIO for identity %s", com.android.server.soundtrigger_middleware.ObjectPrinter.print(identity, 16)));
        }
    }

    private static void enforcePermissionForPreflight(android.content.Context context, android.media.permission.Identity identity, java.lang.String permission, boolean allowSoftDenial) {
        int status = android.media.permission.PermissionUtil.checkPermissionForPreflight(context, identity, permission);
        switch (status) {
            case 0:
                return;
            case 1:
                if (allowSoftDenial) {
                    return;
                }
                break;
            case 2:
                break;
            default:
                throw new java.lang.RuntimeException("Unexpected perimission check result.");
        }
        throw new java.lang.SecurityException(java.lang.String.format("Failed to obtain permission %s for identity %s", permission, com.android.server.soundtrigger_middleware.ObjectPrinter.print(identity, 16)));
    }

    @Override // com.android.server.soundtrigger_middleware.Dumpable
    public void dump(java.io.PrintWriter pw) {
        if (this.mDelegate instanceof com.android.server.soundtrigger_middleware.Dumpable) {
            ((com.android.server.soundtrigger_middleware.Dumpable) this.mDelegate).dump(pw);
        }
    }

    private class ModuleWrapper extends android.media.soundtrigger_middleware.ISoundTriggerModule.Stub {
        private final com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.ModuleWrapper.CallbackWrapper mCallbackWrapper;
        private android.media.soundtrigger_middleware.ISoundTriggerModule mDelegate;
        private final boolean mIsTrusted;
        private final android.media.permission.Identity mOriginatorIdentity;

        ModuleWrapper(android.media.permission.Identity originatorIdentity, android.media.soundtrigger_middleware.ISoundTriggerCallback callback, boolean isTrusted) {
            this.mOriginatorIdentity = originatorIdentity;
            this.mCallbackWrapper = new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.ModuleWrapper.CallbackWrapper(callback);
            this.mIsTrusted = isTrusted;
        }

        com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.ModuleWrapper attach(android.media.soundtrigger_middleware.ISoundTriggerModule delegate) {
            this.mDelegate = delegate;
            return this;
        }

        android.media.soundtrigger_middleware.ISoundTriggerCallback getCallbackWrapper() {
            return this.mCallbackWrapper;
        }

        public int loadModel(android.media.soundtrigger.SoundModel model) throws android.os.RemoteException {
            enforcePermissions();
            return this.mDelegate.loadModel(model);
        }

        public int loadPhraseModel(android.media.soundtrigger.PhraseSoundModel model) throws android.os.RemoteException {
            enforcePermissions();
            return this.mDelegate.loadPhraseModel(model);
        }

        public void unloadModel(int modelHandle) throws android.os.RemoteException {
            this.mDelegate.unloadModel(modelHandle);
        }

        public android.os.IBinder startRecognition(int modelHandle, android.media.soundtrigger.RecognitionConfig config) throws android.os.RemoteException {
            enforcePermissions();
            return this.mDelegate.startRecognition(modelHandle, config);
        }

        public void stopRecognition(int modelHandle) throws android.os.RemoteException {
            this.mDelegate.stopRecognition(modelHandle);
        }

        public void forceRecognitionEvent(int modelHandle) throws android.os.RemoteException {
            enforcePermissions();
            this.mDelegate.forceRecognitionEvent(modelHandle);
        }

        public void setModelParameter(int modelHandle, int modelParam, int value) throws android.os.RemoteException {
            enforcePermissions();
            this.mDelegate.setModelParameter(modelHandle, modelParam, value);
        }

        public int getModelParameter(int modelHandle, int modelParam) throws android.os.RemoteException {
            enforcePermissions();
            return this.mDelegate.getModelParameter(modelHandle, modelParam);
        }

        public android.media.soundtrigger.ModelParameterRange queryModelParameterSupport(int modelHandle, int modelParam) throws android.os.RemoteException {
            enforcePermissions();
            return this.mDelegate.queryModelParameterSupport(modelHandle, modelParam);
        }

        public void detach() throws android.os.RemoteException {
            this.mDelegate.detach();
        }

        public java.lang.String toString() {
            return java.util.Objects.toString(this.mDelegate);
        }

        private void enforcePermissions() {
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.this.enforcePermissionsForPreflight(this.mOriginatorIdentity);
        }

        private class CallbackWrapper implements android.media.soundtrigger_middleware.ISoundTriggerCallback {
            private final android.media.soundtrigger_middleware.ISoundTriggerCallback mDelegate;

            private CallbackWrapper(android.media.soundtrigger_middleware.ISoundTriggerCallback delegate) {
                this.mDelegate = delegate;
            }

            public void onRecognition(int modelHandle, android.media.soundtrigger_middleware.RecognitionEventSys event, int captureSession) throws android.os.RemoteException {
                enforcePermissions("Sound trigger recognition.");
                this.mDelegate.onRecognition(modelHandle, event, captureSession);
            }

            public void onPhraseRecognition(int modelHandle, android.media.soundtrigger_middleware.PhraseRecognitionEventSys event, int captureSession) throws android.os.RemoteException {
                enforcePermissions("Sound trigger phrase recognition.");
                this.mDelegate.onPhraseRecognition(modelHandle, event, captureSession);
            }

            public void onResourcesAvailable() throws android.os.RemoteException {
                this.mDelegate.onResourcesAvailable();
            }

            public void onModelUnloaded(int modelHandle) throws android.os.RemoteException {
                this.mDelegate.onModelUnloaded(modelHandle);
            }

            public void onModuleDied() throws android.os.RemoteException {
                this.mDelegate.onModuleDied();
            }

            public android.os.IBinder asBinder() {
                return this.mDelegate.asBinder();
            }

            public java.lang.String toString() {
                return this.mDelegate.toString();
            }

            private void enforcePermissions(java.lang.String reason) {
                if (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.ModuleWrapper.this.mIsTrusted) {
                    com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.this.enforcePermissionsForPreflight(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.ModuleWrapper.this.mOriginatorIdentity);
                } else {
                    com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.this.enforcePermissionsForDataDelivery(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission.ModuleWrapper.this.mOriginatorIdentity, reason);
                }
            }
        }
    }
}
