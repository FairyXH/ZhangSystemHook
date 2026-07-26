package com.android.server.texttospeech;

/* JADX INFO: loaded from: classes3.dex */
final class TextToSpeechManagerPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.texttospeech.TextToSpeechManagerPerUserService, com.android.server.texttospeech.TextToSpeechManagerService> {
    private static final java.lang.String TAG = com.android.server.texttospeech.TextToSpeechManagerPerUserService.class.getSimpleName();

    interface ThrowingRunnable {
        void runOrThrow() throws android.os.RemoteException;
    }

    TextToSpeechManagerPerUserService(com.android.server.texttospeech.TextToSpeechManagerService master, java.lang.Object lock, int userId) {
        super(master, lock, userId);
    }

    void createSessionLocked(java.lang.String engine, android.speech.tts.ITextToSpeechSessionCallback sessionCallback) {
        com.android.server.texttospeech.TextToSpeechManagerPerUserService.TextToSpeechSessionConnection.start(getContext(), this.mUserId, engine, sessionCallback);
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            return android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 128L, this.mUserId);
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class TextToSpeechSessionConnection extends com.android.internal.infra.ServiceConnector.Impl<android.speech.tts.ITextToSpeechService> {
        private android.speech.tts.ITextToSpeechSessionCallback mCallback;
        private final java.lang.String mEngine;
        private final android.os.IBinder.DeathRecipient mUnbindOnDeathHandler;

        static void start(android.content.Context context, int userId, java.lang.String engine, android.speech.tts.ITextToSpeechSessionCallback callback) {
            new com.android.server.texttospeech.TextToSpeechManagerPerUserService.TextToSpeechSessionConnection(context, userId, engine, callback).start();
        }

        private TextToSpeechSessionConnection(android.content.Context context, int userId, java.lang.String engine, android.speech.tts.ITextToSpeechSessionCallback callback) {
            super(context, new android.content.Intent("android.intent.action.TTS_SERVICE").setPackage(engine), 524289, userId, new java.util.function.Function() { // from class: com.android.server.texttospeech.TextToSpeechManagerPerUserService$TextToSpeechSessionConnection$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.speech.tts.ITextToSpeechService.Stub.asInterface((android.os.IBinder) obj);
                }
            });
            this.mEngine = engine;
            this.mCallback = callback;
            this.mUnbindOnDeathHandler = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.texttospeech.TextToSpeechManagerPerUserService$TextToSpeechSessionConnection$$ExternalSyntheticLambda3
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$new$0();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            unbindEngine("client process death is reported");
        }

        private void start() {
            android.util.Slog.d(com.android.server.texttospeech.TextToSpeechManagerPerUserService.TAG, "Trying to start connection to TTS engine: " + this.mEngine);
            connect().thenAccept(new java.util.function.Consumer() { // from class: com.android.server.texttospeech.TextToSpeechManagerPerUserService$TextToSpeechSessionConnection$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$start$2((android.speech.tts.ITextToSpeechService) obj);
                }
            }).exceptionally(new java.util.function.Function() { // from class: com.android.server.texttospeech.TextToSpeechManagerPerUserService$TextToSpeechSessionConnection$$ExternalSyntheticLambda5
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$start$4((java.lang.Throwable) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$2(android.speech.tts.ITextToSpeechService serviceBinder) {
            if (serviceBinder != null) {
                android.util.Slog.d(com.android.server.texttospeech.TextToSpeechManagerPerUserService.TAG, "Connected successfully to TTS engine: " + this.mEngine);
                try {
                    this.mCallback.onConnected(new android.speech.tts.ITextToSpeechSession.Stub() { // from class: com.android.server.texttospeech.TextToSpeechManagerPerUserService.TextToSpeechSessionConnection.1
                        public void disconnect() {
                            com.android.server.texttospeech.TextToSpeechManagerPerUserService.TextToSpeechSessionConnection.this.unbindEngine("client disconnection request");
                        }
                    }, serviceBinder.asBinder());
                    this.mCallback.asBinder().linkToDeath(this.mUnbindOnDeathHandler, 0);
                    return;
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(com.android.server.texttospeech.TextToSpeechManagerPerUserService.TAG, "Error notifying the client on connection", ex);
                    unbindEngine("failed communicating with the client - process is dead");
                    return;
                }
            }
            android.util.Slog.w(com.android.server.texttospeech.TextToSpeechManagerPerUserService.TAG, "Failed to obtain TTS engine binder");
            com.android.server.texttospeech.TextToSpeechManagerPerUserService.runSessionCallbackMethod(new com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable() { // from class: com.android.server.texttospeech.TextToSpeechManagerPerUserService$TextToSpeechSessionConnection$$ExternalSyntheticLambda0
                @Override // com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable
                public final void runOrThrow() throws android.os.RemoteException {
                    this.f$0.lambda$start$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$1() throws android.os.RemoteException {
            this.mCallback.onError("Failed creating TTS session");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.lang.Void lambda$start$4(final java.lang.Throwable ex) {
            android.util.Slog.w(com.android.server.texttospeech.TextToSpeechManagerPerUserService.TAG, "TTS engine binding error", ex);
            com.android.server.texttospeech.TextToSpeechManagerPerUserService.runSessionCallbackMethod(new com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable() { // from class: com.android.server.texttospeech.TextToSpeechManagerPerUserService$TextToSpeechSessionConnection$$ExternalSyntheticLambda6
                @Override // com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable
                public final void runOrThrow() throws android.os.RemoteException {
                    this.f$0.lambda$start$3(ex);
                }
            });
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$3(java.lang.Throwable ex) throws android.os.RemoteException {
            this.mCallback.onError("Failed creating TTS session: " + ex.getCause());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onServiceConnectionStatusChanged(android.speech.tts.ITextToSpeechService service, boolean connected) {
            if (!connected) {
                android.util.Slog.w(com.android.server.texttospeech.TextToSpeechManagerPerUserService.TAG, "Disconnected from TTS engine");
                try {
                    final android.speech.tts.ITextToSpeechSessionCallback iTextToSpeechSessionCallback = this.mCallback;
                    java.util.Objects.requireNonNull(iTextToSpeechSessionCallback);
                    com.android.server.texttospeech.TextToSpeechManagerPerUserService.runSessionCallbackMethod(new com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable() { // from class: com.android.server.texttospeech.TextToSpeechManagerPerUserService$TextToSpeechSessionConnection$$ExternalSyntheticLambda1
                        @Override // com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable
                        public final void runOrThrow() {
                            iTextToSpeechSessionCallback.onDisconnected();
                        }
                    });
                    this.mCallback.asBinder().unlinkToDeath(this.mUnbindOnDeathHandler, 0);
                } catch (java.util.NoSuchElementException e) {
                    android.util.Slog.d(com.android.server.texttospeech.TextToSpeechManagerPerUserService.TAG, "The death recipient was not linked.");
                } catch (java.lang.Exception e2) {
                    android.util.Slog.d(com.android.server.texttospeech.TextToSpeechManagerPerUserService.TAG, "exception while running callback.");
                }
                this.mCallback = null;
            }
        }

        protected long getAutoDisconnectTimeoutMs() {
            return 0L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unbindEngine(java.lang.String reason) {
            android.util.Slog.d(com.android.server.texttospeech.TextToSpeechManagerPerUserService.TAG, "Unbinding TTS engine: " + this.mEngine + ". Reason: " + reason);
            unbind();
        }
    }

    static void runSessionCallbackMethod(com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable callbackRunnable) {
        try {
            callbackRunnable.runOrThrow();
        } catch (android.os.RemoteException ex) {
            android.util.Slog.i(TAG, "Failed running callback method: " + ex);
        }
    }
}
