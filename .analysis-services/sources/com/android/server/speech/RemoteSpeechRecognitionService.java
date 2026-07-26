package com.android.server.speech;

/* JADX INFO: loaded from: classes3.dex */
final class RemoteSpeechRecognitionService extends com.android.internal.infra.ServiceConnector.Impl<android.speech.IRecognitionService> {
    private static final boolean DEBUG = false;
    private static final int MAX_CONCURRENT_CLIENTS = 100;
    private static final java.lang.String TAG = com.android.server.speech.RemoteSpeechRecognitionService.class.getSimpleName();
    private final int mCallingUid;
    private final java.util.List<android.util.Pair<android.os.IBinder, android.speech.IRecognitionListener>> mClientListeners;
    private final java.util.Map<android.os.IBinder, com.android.server.speech.RemoteSpeechRecognitionService.ClientState> mClients;
    private final android.content.ComponentName mComponentName;
    private boolean mConnected;
    private final java.lang.Object mLock;

    RemoteSpeechRecognitionService(android.content.Context context, android.content.ComponentName serviceName, int userId, int callingUid, boolean isPrivileged) {
        super(context, new android.content.Intent("android.speech.RecognitionService").setComponent(serviceName), getBindingFlags(isPrivileged), userId, new java.util.function.Function() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.speech.IRecognitionService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        this.mLock = new java.lang.Object();
        this.mConnected = false;
        this.mClients = new java.util.HashMap();
        this.mClientListeners = new java.util.ArrayList();
        this.mCallingUid = callingUid;
        this.mComponentName = serviceName;
    }

    private static int getBindingFlags(boolean isPrivileged) {
        if (isPrivileged) {
            int bindingFlags = 1 | 67112960;
            return bindingFlags;
        }
        return 1;
    }

    android.content.ComponentName getServiceComponentName() {
        return this.mComponentName;
    }

    void startListening(final android.content.Intent recognizerIntent, final android.speech.IRecognitionListener listener, final android.content.AttributionSource attributionSource) {
        if (listener == null) {
            android.util.Slog.w(TAG, "#startListening called with no preceding #setListening - ignoring.");
            return;
        }
        if (!this.mConnected) {
            tryRespondWithError(listener, 11);
            return;
        }
        synchronized (this.mLock) {
            com.android.server.speech.RemoteSpeechRecognitionService.ClientState clientState = this.mClients.get(listener.asBinder());
            if (clientState == null) {
                if (this.mClients.size() >= 100) {
                    tryRespondWithError(listener, 8);
                    android.util.Log.i(TAG, "#startListening received when the recognizer's capacity is full - ignoring this call.");
                    return;
                } else {
                    final com.android.server.speech.RemoteSpeechRecognitionService.ClientState newClientState = new com.android.server.speech.RemoteSpeechRecognitionService.ClientState();
                    newClientState.mDelegatingListener = new com.android.server.speech.RemoteSpeechRecognitionService.DelegatingListener(listener, new java.lang.Runnable() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$startListening$0(newClientState);
                        }
                    }, new java.lang.Runnable() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$startListening$1(listener);
                        }
                    });
                    this.mClients.put(listener.asBinder(), newClientState);
                    clientState = newClientState;
                }
            } else {
                if (clientState.mRecordingInProgress) {
                    android.util.Slog.i(TAG, "#startListening called while listening is in progress for this caller.");
                    tryRespondWithError(listener, 5);
                    return;
                }
                clientState.mRecordingInProgress = true;
            }
            final com.android.server.speech.RemoteSpeechRecognitionService.DelegatingListener listenerToStart = clientState.mDelegatingListener;
            run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda5
                public final void runNoResult(java.lang.Object obj) {
                    ((android.speech.IRecognitionService) obj).startListening(recognizerIntent, listenerToStart, attributionSource);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startListening$0(com.android.server.speech.RemoteSpeechRecognitionService.ClientState newClientState) {
        synchronized (this.mLock) {
            newClientState.mRecordingInProgress = false;
        }
    }

    void stopListening(android.speech.IRecognitionListener listener) {
        if (!this.mConnected) {
            tryRespondWithError(listener, 11);
            return;
        }
        synchronized (this.mLock) {
            com.android.server.speech.RemoteSpeechRecognitionService.ClientState clientState = this.mClients.get(listener.asBinder());
            if (clientState == null) {
                android.util.Slog.w(TAG, "#stopListening called with no preceding #startListening - ignoring.");
                tryRespondWithError(listener, 5);
            } else if (!clientState.mRecordingInProgress) {
                tryRespondWithError(listener, 5);
                android.util.Slog.i(TAG, "#stopListening called while listening isn't in progress - ignoring.");
            } else {
                clientState.mRecordingInProgress = false;
                final com.android.server.speech.RemoteSpeechRecognitionService.DelegatingListener listenerToStop = clientState.mDelegatingListener;
                run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda7
                    public final void runNoResult(java.lang.Object obj) {
                        ((android.speech.IRecognitionService) obj).stopListening(listenerToStop);
                    }
                });
            }
        }
    }

    void cancel(android.speech.IRecognitionListener listener, final boolean isShutdown) {
        if (!this.mConnected) {
            tryRespondWithError(listener, 11);
        }
        synchronized (this.mLock) {
            com.android.server.speech.RemoteSpeechRecognitionService.ClientState clientState = this.mClients.get(listener.asBinder());
            if (clientState != null) {
                clientState.mRecordingInProgress = false;
                final com.android.server.speech.RemoteSpeechRecognitionService.DelegatingListener delegatingListener = clientState.mDelegatingListener;
                run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda1
                    public final void runNoResult(java.lang.Object obj) {
                        ((android.speech.IRecognitionService) obj).cancel(delegatingListener, isShutdown);
                    }
                });
            }
            if (isShutdown) {
                lambda$startListening$1(listener);
                if (this.mClients.isEmpty()) {
                    run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda2
                        public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                            this.f$0.lambda$cancel$5((android.speech.IRecognitionService) obj);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancel$5(android.speech.IRecognitionService service) throws java.lang.Exception {
        unbind();
    }

    void checkRecognitionSupport(final android.content.Intent recognizerIntent, final android.content.AttributionSource attributionSource, final android.speech.IRecognitionSupportCallback callback) {
        if (!this.mConnected) {
            try {
                callback.onError(11);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to report the connection broke to the caller.", e);
                e.printStackTrace();
                return;
            }
        }
        run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda0
            public final void runNoResult(java.lang.Object obj) {
                ((android.speech.IRecognitionService) obj).checkRecognitionSupport(recognizerIntent, attributionSource, callback);
            }
        });
    }

    void triggerModelDownload(final android.content.Intent recognizerIntent, final android.content.AttributionSource attributionSource, final android.speech.IModelDownloadListener listener) {
        if (!this.mConnected) {
            try {
                listener.onError(11);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "#downloadModel failed due to connection.", e);
                e.printStackTrace();
                return;
            }
        }
        run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda6
            public final void runNoResult(java.lang.Object obj) {
                ((android.speech.IRecognitionService) obj).triggerModelDownload(recognizerIntent, attributionSource, listener);
            }
        });
    }

    void shutdown(android.os.IBinder clientToken) {
        synchronized (this.mLock) {
            for (android.util.Pair<android.os.IBinder, android.speech.IRecognitionListener> clientListener : this.mClientListeners) {
                if (clientListener.first == clientToken) {
                    cancel((android.speech.IRecognitionListener) clientListener.second, true);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(android.speech.IRecognitionService service, boolean connected) {
        this.mConnected = connected;
        synchronized (this.mLock) {
            if (!connected) {
                if (this.mClients.isEmpty()) {
                    android.util.Slog.i(TAG, "Connection to speech recognition service lost, but no #startListening has been invoked yet.");
                    return;
                }
                for (com.android.server.speech.RemoteSpeechRecognitionService.ClientState clientState : (com.android.server.speech.RemoteSpeechRecognitionService.ClientState[]) this.mClients.values().toArray(new com.android.server.speech.RemoteSpeechRecognitionService.ClientState[0])) {
                    tryRespondWithError(clientState.mDelegatingListener.mRemoteListener, 11);
                    lambda$startListening$1(clientState.mDelegatingListener.mRemoteListener);
                }
            }
        }
    }

    protected long getAutoDisconnectTimeoutMs() {
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: removeClient, reason: merged with bridge method [inline-methods] */
    public void lambda$startListening$1(final android.speech.IRecognitionListener listener) {
        synchronized (this.mLock) {
            com.android.server.speech.RemoteSpeechRecognitionService.ClientState clientState = this.mClients.remove(listener.asBinder());
            if (clientState != null) {
                clientState.reset();
            }
            this.mClientListeners.removeIf(new java.util.function.Predicate() { // from class: com.android.server.speech.RemoteSpeechRecognitionService$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.speech.RemoteSpeechRecognitionService.lambda$removeClient$8(listener, (android.util.Pair) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$removeClient$8(android.speech.IRecognitionListener listener, android.util.Pair clientListener) {
        return clientListener.second == listener;
    }

    private static void tryRespondWithError(android.speech.IRecognitionListener listener, int errorCode) {
        if (listener != null) {
            try {
                listener.onError(errorCode);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Failed to respond with an error %d to the client", new java.lang.Object[]{java.lang.Integer.valueOf(errorCode)}), e);
            }
        }
    }

    boolean hasActiveSessions() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mClients.isEmpty();
        }
        return z;
    }

    void associateClientWithActiveListener(android.os.IBinder clientToken, android.speech.IRecognitionListener listener) {
        synchronized (this.mLock) {
            if (this.mClients.containsKey(listener.asBinder())) {
                this.mClientListeners.add(new android.util.Pair<>(clientToken, listener));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class DelegatingListener extends android.speech.IRecognitionListener.Stub {
        private final java.lang.Runnable mOnSessionFailure;
        private final java.lang.Runnable mOnSessionSuccess;
        private final android.speech.IRecognitionListener mRemoteListener;

        DelegatingListener(android.speech.IRecognitionListener listener, java.lang.Runnable onSessionSuccess, java.lang.Runnable onSessionFailure) {
            this.mRemoteListener = listener;
            this.mOnSessionSuccess = onSessionSuccess;
            this.mOnSessionFailure = onSessionFailure;
        }

        public void onReadyForSpeech(android.os.Bundle params) throws android.os.RemoteException {
            this.mRemoteListener.onReadyForSpeech(params);
        }

        public void onBeginningOfSpeech() throws android.os.RemoteException {
            this.mRemoteListener.onBeginningOfSpeech();
        }

        public void onRmsChanged(float rmsdB) throws android.os.RemoteException {
            this.mRemoteListener.onRmsChanged(rmsdB);
        }

        public void onBufferReceived(byte[] buffer) throws android.os.RemoteException {
            this.mRemoteListener.onBufferReceived(buffer);
        }

        public void onEndOfSpeech() throws android.os.RemoteException {
            this.mRemoteListener.onEndOfSpeech();
        }

        public void onError(int error) throws android.os.RemoteException {
            this.mOnSessionFailure.run();
            this.mRemoteListener.onError(error);
        }

        public void onResults(android.os.Bundle results) throws android.os.RemoteException {
            this.mOnSessionSuccess.run();
            this.mRemoteListener.onResults(results);
        }

        public void onPartialResults(android.os.Bundle results) throws android.os.RemoteException {
            this.mRemoteListener.onPartialResults(results);
        }

        public void onSegmentResults(android.os.Bundle results) throws android.os.RemoteException {
            this.mRemoteListener.onSegmentResults(results);
        }

        public void onEndOfSegmentedSession() throws android.os.RemoteException {
            this.mOnSessionSuccess.run();
            this.mRemoteListener.onEndOfSegmentedSession();
        }

        public void onLanguageDetection(android.os.Bundle results) throws android.os.RemoteException {
            this.mRemoteListener.onLanguageDetection(results);
        }

        public void onEvent(int eventType, android.os.Bundle params) throws android.os.RemoteException {
            this.mRemoteListener.onEvent(eventType, params);
        }
    }

    static class ClientState {
        com.android.server.speech.RemoteSpeechRecognitionService.DelegatingListener mDelegatingListener;
        boolean mRecordingInProgress;

        ClientState(com.android.server.speech.RemoteSpeechRecognitionService.DelegatingListener delegatingListener, boolean recordingInProgress) {
            this.mDelegatingListener = delegatingListener;
            this.mRecordingInProgress = recordingInProgress;
        }

        ClientState(com.android.server.speech.RemoteSpeechRecognitionService.DelegatingListener delegatingListener) {
            this(delegatingListener, true);
        }

        ClientState() {
            this(null, true);
        }

        void reset() {
            this.mDelegatingListener = null;
            this.mRecordingInProgress = false;
        }
    }
}
