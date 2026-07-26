package com.android.server.speech;

/* JADX INFO: loaded from: classes3.dex */
public final class SpeechRecognitionManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.speech.SpeechRecognitionManagerService, com.android.server.speech.SpeechRecognitionManagerServiceImpl> {
    private static final int MAX_TEMP_SERVICE_SUBSTITUTION_DURATION_MS = 60000;
    private static final java.lang.String TAG = com.android.server.speech.SpeechRecognitionManagerService.class.getSimpleName();

    public SpeechRecognitionManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_defaultTranslationService), null);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("speech_recognition", new com.android.server.speech.SpeechRecognitionManagerService.SpeechRecognitionManagerServiceStub());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_SPEECH_RECOGNITION", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 60000;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.speech.SpeechRecognitionManagerServiceImpl newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.speech.SpeechRecognitionManagerServiceImpl(this, this.mLock, resolvedUserId);
    }

    final class SpeechRecognitionManagerServiceStub extends android.speech.IRecognitionServiceManager.Stub {
        SpeechRecognitionManagerServiceStub() {
        }

        public void createSession(android.content.ComponentName componentName, android.os.IBinder clientToken, boolean onDevice, android.speech.IRecognitionServiceManagerCallback callback) {
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.speech.SpeechRecognitionManagerService.this.mLock) {
                com.android.server.speech.SpeechRecognitionManagerServiceImpl service = (com.android.server.speech.SpeechRecognitionManagerServiceImpl) com.android.server.speech.SpeechRecognitionManagerService.this.getServiceForUserLocked(userId);
                service.createSessionLocked(componentName, clientToken, onDevice, callback);
            }
        }

        public void setTemporaryComponent(android.content.ComponentName componentName) {
            int userId = android.os.UserHandle.getCallingUserId();
            if (componentName == null) {
                com.android.server.speech.SpeechRecognitionManagerService.this.resetTemporaryService(userId);
                android.util.Slog.i(com.android.server.speech.SpeechRecognitionManagerService.TAG, "Reset temporary service for user " + userId);
            } else {
                com.android.server.speech.SpeechRecognitionManagerService.this.setTemporaryService(userId, componentName.flattenToString(), 60000);
                android.util.Slog.i(com.android.server.speech.SpeechRecognitionManagerService.TAG, "SpeechRecognition temporarily set to " + componentName + " for 60000ms");
            }
        }
    }
}
