package com.android.server.texttospeech;

/* JADX INFO: loaded from: classes3.dex */
public final class TextToSpeechManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.texttospeech.TextToSpeechManagerService, com.android.server.texttospeech.TextToSpeechManagerPerUserService> {
    private static final java.lang.String TAG = com.android.server.texttospeech.TextToSpeechManagerService.class.getSimpleName();

    public TextToSpeechManagerService(android.content.Context context) {
        super(context, null, null);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("texttospeech", new com.android.server.texttospeech.TextToSpeechManagerService.TextToSpeechManagerServiceStub());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.texttospeech.TextToSpeechManagerPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.texttospeech.TextToSpeechManagerPerUserService(this, this.mLock, resolvedUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class TextToSpeechManagerServiceStub extends android.speech.tts.ITextToSpeechManager.Stub {
        private TextToSpeechManagerServiceStub() {
        }

        public void createSession(java.lang.String engine, final android.speech.tts.ITextToSpeechSessionCallback sessionCallback) {
            synchronized (com.android.server.texttospeech.TextToSpeechManagerService.this.mLock) {
                if (engine != null) {
                    com.android.server.texttospeech.TextToSpeechManagerPerUserService perUserService = (com.android.server.texttospeech.TextToSpeechManagerPerUserService) com.android.server.texttospeech.TextToSpeechManagerService.this.getServiceForUserLocked(android.os.UserHandle.getCallingUserId());
                    if (perUserService != null) {
                        perUserService.createSessionLocked(engine, sessionCallback);
                    } else {
                        com.android.server.texttospeech.TextToSpeechManagerPerUserService.runSessionCallbackMethod(new com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable() { // from class: com.android.server.texttospeech.TextToSpeechManagerService$TextToSpeechManagerServiceStub$$ExternalSyntheticLambda1
                            @Override // com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable
                            public final void runOrThrow() {
                                sessionCallback.onError("Service is not available for user");
                            }
                        });
                    }
                    return;
                }
                com.android.server.texttospeech.TextToSpeechManagerPerUserService.runSessionCallbackMethod(new com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable() { // from class: com.android.server.texttospeech.TextToSpeechManagerService$TextToSpeechManagerServiceStub$$ExternalSyntheticLambda0
                    @Override // com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable
                    public final void runOrThrow() {
                        sessionCallback.onError("Engine cannot be null");
                    }
                });
            }
        }
    }
}
