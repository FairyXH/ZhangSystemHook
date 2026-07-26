package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class RemoteCredentialService extends com.android.internal.infra.ServiceConnector.Impl<android.service.credentials.ICredentialProviderService> {
    private static final java.lang.String TAG = "CredentialManager";
    private static final long TIMEOUT_IDLE_SERVICE_CONNECTION_MILLIS = 5000;
    private static final long TIMEOUT_REQUEST_MILLIS = 3000;
    private com.android.server.credentials.RemoteCredentialService.ProviderCallbacks mCallback;
    private final android.content.ComponentName mComponentName;
    private java.util.concurrent.atomic.AtomicBoolean mOngoingRequest;

    public interface ProviderCallbacks<T> {
        void onProviderCancellable(android.os.ICancellationSignal iCancellationSignal);

        void onProviderResponseFailure(int i, java.lang.Exception exc);

        void onProviderResponseSuccess(T t);

        void onProviderServiceDied(com.android.server.credentials.RemoteCredentialService remoteCredentialService);
    }

    public RemoteCredentialService(android.content.Context context, android.content.ComponentName componentName, int userId) {
        super(context, new android.content.Intent("android.service.credentials.CredentialProviderService").setComponent(componentName), 0, userId, new java.util.function.Function() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.service.credentials.ICredentialProviderService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        this.mOngoingRequest = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mComponentName = componentName;
    }

    public void setCallback(com.android.server.credentials.RemoteCredentialService.ProviderCallbacks callback) {
        this.mCallback = callback;
    }

    protected long getAutoDisconnectTimeoutMs() {
        return TIMEOUT_IDLE_SERVICE_CONNECTION_MILLIS;
    }

    public void onBindingDied(android.content.ComponentName name) {
        super.onBindingDied(name);
        android.util.Slog.w(TAG, "binding died for: " + name);
    }

    public void binderDied() {
        super.binderDied();
        android.util.Slog.w(TAG, "binderDied");
        if (this.mCallback != null) {
            this.mOngoingRequest.set(false);
            this.mCallback.onProviderServiceDied(this);
        }
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    public void destroy() {
        unbind();
    }

    public void onBeginGetCredential(final android.service.credentials.BeginGetCredentialRequest request) {
        if (this.mCallback == null) {
            android.util.Slog.w(TAG, "Callback is not set");
            return;
        }
        this.mOngoingRequest.set(true);
        final java.util.concurrent.atomic.AtomicReference<android.os.ICancellationSignal> cancellationSink = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<java.util.concurrent.CompletableFuture<android.service.credentials.BeginGetCredentialResponse>> futureRef = new java.util.concurrent.atomic.AtomicReference<>();
        com.android.internal.infra.AndroidFuture androidFutureOrTimeout = postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda3
            public final java.lang.Object run(java.lang.Object obj) {
                return this.f$0.lambda$onBeginGetCredential$0(request, futureRef, cancellationSink, (android.service.credentials.ICredentialProviderService) obj);
            }
        }).orTimeout(3000L, java.util.concurrent.TimeUnit.MILLISECONDS);
        futureRef.set(androidFutureOrTimeout);
        androidFutureOrTimeout.whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$onBeginGetCredential$2(cancellationSink, (android.service.credentials.BeginGetCredentialResponse) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.concurrent.CompletableFuture lambda$onBeginGetCredential$0(android.service.credentials.BeginGetCredentialRequest request, final java.util.concurrent.atomic.AtomicReference futureRef, final java.util.concurrent.atomic.AtomicReference cancellationSink, android.service.credentials.ICredentialProviderService service) throws java.lang.Exception {
        final java.util.concurrent.CompletableFuture<android.service.credentials.BeginGetCredentialResponse> getCredentials = new java.util.concurrent.CompletableFuture<>();
        long originalCallingUidToken = android.os.Binder.clearCallingIdentity();
        try {
            service.onBeginGetCredential(request, new android.service.credentials.IBeginGetCredentialCallback.Stub() { // from class: com.android.server.credentials.RemoteCredentialService.1
                public void onSuccess(android.service.credentials.BeginGetCredentialResponse response) {
                    getCredentials.complete(response);
                }

                public void onFailure(java.lang.String errorType, java.lang.CharSequence message) {
                    java.lang.String errorMsg = message == null ? "" : java.lang.String.valueOf(message);
                    getCredentials.completeExceptionally(new android.credentials.GetCredentialException(errorType, errorMsg));
                }

                public void onCancellable(android.os.ICancellationSignal cancellation) {
                    java.util.concurrent.CompletableFuture<android.service.credentials.BeginGetCredentialResponse> future = (java.util.concurrent.CompletableFuture) futureRef.get();
                    if (future != null && future.isCancelled()) {
                        com.android.server.credentials.RemoteCredentialService.this.dispatchCancellationSignal(cancellation);
                        return;
                    }
                    cancellationSink.set(cancellation);
                    if (com.android.server.credentials.RemoteCredentialService.this.mCallback != null) {
                        com.android.server.credentials.RemoteCredentialService.this.mCallback.onProviderCancellable(cancellation);
                    }
                }
            });
            return getCredentials;
        } finally {
            android.os.Binder.restoreCallingIdentity(originalCallingUidToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBeginGetCredential$2(final java.util.concurrent.atomic.AtomicReference cancellationSink, final android.service.credentials.BeginGetCredentialResponse result, final java.lang.Throwable error) {
        android.os.Handler.getMain().post(new java.lang.Runnable() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBeginGetCredential$1(result, error, cancellationSink);
            }
        });
    }

    public void onBeginCreateCredential(final android.service.credentials.BeginCreateCredentialRequest request) {
        if (this.mCallback == null) {
            android.util.Slog.w(TAG, "Callback is not set");
            return;
        }
        this.mOngoingRequest.set(true);
        final java.util.concurrent.atomic.AtomicReference<android.os.ICancellationSignal> cancellationSink = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<java.util.concurrent.CompletableFuture<android.service.credentials.BeginCreateCredentialResponse>> futureRef = new java.util.concurrent.atomic.AtomicReference<>();
        com.android.internal.infra.AndroidFuture androidFutureOrTimeout = postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda0
            public final java.lang.Object run(java.lang.Object obj) {
                return this.f$0.lambda$onBeginCreateCredential$3(request, futureRef, cancellationSink, (android.service.credentials.ICredentialProviderService) obj);
            }
        }).orTimeout(3000L, java.util.concurrent.TimeUnit.MILLISECONDS);
        futureRef.set(androidFutureOrTimeout);
        androidFutureOrTimeout.whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$onBeginCreateCredential$5(cancellationSink, (android.service.credentials.BeginCreateCredentialResponse) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.concurrent.CompletableFuture lambda$onBeginCreateCredential$3(android.service.credentials.BeginCreateCredentialRequest request, final java.util.concurrent.atomic.AtomicReference futureRef, final java.util.concurrent.atomic.AtomicReference cancellationSink, android.service.credentials.ICredentialProviderService service) throws java.lang.Exception {
        final java.util.concurrent.CompletableFuture<android.service.credentials.BeginCreateCredentialResponse> createCredentialFuture = new java.util.concurrent.CompletableFuture<>();
        long originalCallingUidToken = android.os.Binder.clearCallingIdentity();
        try {
            service.onBeginCreateCredential(request, new android.service.credentials.IBeginCreateCredentialCallback.Stub() { // from class: com.android.server.credentials.RemoteCredentialService.2
                public void onSuccess(android.service.credentials.BeginCreateCredentialResponse response) {
                    createCredentialFuture.complete(response);
                }

                public void onFailure(java.lang.String errorType, java.lang.CharSequence message) {
                    java.lang.String errorMsg = message == null ? "" : java.lang.String.valueOf(message);
                    createCredentialFuture.completeExceptionally(new android.credentials.CreateCredentialException(errorType, errorMsg));
                }

                public void onCancellable(android.os.ICancellationSignal cancellation) {
                    java.util.concurrent.CompletableFuture<android.service.credentials.BeginCreateCredentialResponse> future = (java.util.concurrent.CompletableFuture) futureRef.get();
                    if (future != null && future.isCancelled()) {
                        com.android.server.credentials.RemoteCredentialService.this.dispatchCancellationSignal(cancellation);
                        return;
                    }
                    cancellationSink.set(cancellation);
                    if (com.android.server.credentials.RemoteCredentialService.this.mCallback != null) {
                        com.android.server.credentials.RemoteCredentialService.this.mCallback.onProviderCancellable(cancellation);
                    }
                }
            });
            return createCredentialFuture;
        } finally {
            android.os.Binder.restoreCallingIdentity(originalCallingUidToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBeginCreateCredential$5(final java.util.concurrent.atomic.AtomicReference cancellationSink, final android.service.credentials.BeginCreateCredentialResponse result, final java.lang.Throwable error) {
        android.os.Handler.getMain().post(new java.lang.Runnable() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBeginCreateCredential$4(result, error, cancellationSink);
            }
        });
    }

    public void onClearCredentialState(final android.service.credentials.ClearCredentialStateRequest request) {
        if (this.mCallback == null) {
            android.util.Slog.w(TAG, "Callback is not set");
            return;
        }
        this.mOngoingRequest.set(true);
        final java.util.concurrent.atomic.AtomicReference<android.os.ICancellationSignal> cancellationSink = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<java.util.concurrent.CompletableFuture<java.lang.Void>> futureRef = new java.util.concurrent.atomic.AtomicReference<>();
        com.android.internal.infra.AndroidFuture androidFutureOrTimeout = postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda6
            public final java.lang.Object run(java.lang.Object obj) {
                return this.f$0.lambda$onClearCredentialState$6(request, futureRef, cancellationSink, (android.service.credentials.ICredentialProviderService) obj);
            }
        }).orTimeout(3000L, java.util.concurrent.TimeUnit.MILLISECONDS);
        futureRef.set(androidFutureOrTimeout);
        androidFutureOrTimeout.whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda7
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$onClearCredentialState$8(cancellationSink, (java.lang.Void) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.concurrent.CompletableFuture lambda$onClearCredentialState$6(android.service.credentials.ClearCredentialStateRequest request, final java.util.concurrent.atomic.AtomicReference futureRef, final java.util.concurrent.atomic.AtomicReference cancellationSink, android.service.credentials.ICredentialProviderService service) throws java.lang.Exception {
        final java.util.concurrent.CompletableFuture<java.lang.Void> clearCredentialFuture = new java.util.concurrent.CompletableFuture<>();
        long originalCallingUidToken = android.os.Binder.clearCallingIdentity();
        try {
            service.onClearCredentialState(request, new android.service.credentials.IClearCredentialStateCallback.Stub() { // from class: com.android.server.credentials.RemoteCredentialService.3
                public void onSuccess() {
                    clearCredentialFuture.complete(null);
                }

                public void onFailure(java.lang.String errorType, java.lang.CharSequence message) {
                    java.lang.String errorMsg = message == null ? "" : java.lang.String.valueOf(message);
                    clearCredentialFuture.completeExceptionally(new android.credentials.ClearCredentialStateException(errorType, errorMsg));
                }

                public void onCancellable(android.os.ICancellationSignal cancellation) {
                    java.util.concurrent.CompletableFuture<java.lang.Void> future = (java.util.concurrent.CompletableFuture) futureRef.get();
                    if (future != null && future.isCancelled()) {
                        com.android.server.credentials.RemoteCredentialService.this.dispatchCancellationSignal(cancellation);
                        return;
                    }
                    cancellationSink.set(cancellation);
                    if (com.android.server.credentials.RemoteCredentialService.this.mCallback != null) {
                        com.android.server.credentials.RemoteCredentialService.this.mCallback.onProviderCancellable(cancellation);
                    }
                }
            });
            return clearCredentialFuture;
        } finally {
            android.os.Binder.restoreCallingIdentity(originalCallingUidToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClearCredentialState$8(final java.util.concurrent.atomic.AtomicReference cancellationSink, final java.lang.Void result, final java.lang.Throwable error) {
        android.os.Handler.getMain().post(new java.lang.Runnable() { // from class: com.android.server.credentials.RemoteCredentialService$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onClearCredentialState$7(result, error, cancellationSink);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleExecutionResponse, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public <T> void lambda$onClearCredentialState$7(T result, java.lang.Throwable error, java.util.concurrent.atomic.AtomicReference<android.os.ICancellationSignal> cancellationSink) {
        if (error == null) {
            if (this.mCallback != null) {
                this.mCallback.onProviderResponseSuccess(result);
                return;
            }
            return;
        }
        if (error instanceof java.util.concurrent.TimeoutException) {
            android.util.Slog.i(TAG, "Remote provider response timed tuo for: " + this.mComponentName);
            if (!this.mOngoingRequest.get()) {
                return;
            }
            dispatchCancellationSignal(cancellationSink.get());
            if (this.mCallback != null) {
                this.mOngoingRequest.set(false);
                this.mCallback.onProviderResponseFailure(1, null);
                return;
            }
            return;
        }
        if (error instanceof java.util.concurrent.CancellationException) {
            android.util.Slog.i(TAG, "Cancellation exception for remote provider: " + this.mComponentName);
            if (!this.mOngoingRequest.get()) {
                return;
            }
            dispatchCancellationSignal(cancellationSink.get());
            if (this.mCallback != null) {
                this.mOngoingRequest.set(false);
                this.mCallback.onProviderResponseFailure(2, null);
                return;
            }
            return;
        }
        if (error instanceof android.credentials.GetCredentialException) {
            if (this.mCallback != null) {
                this.mCallback.onProviderResponseFailure(3, (android.credentials.GetCredentialException) error);
            }
        } else if (error instanceof android.credentials.CreateCredentialException) {
            if (this.mCallback != null) {
                this.mCallback.onProviderResponseFailure(3, (android.credentials.CreateCredentialException) error);
            }
        } else if (this.mCallback != null) {
            this.mCallback.onProviderResponseFailure(0, (java.lang.Exception) error);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchCancellationSignal(android.os.ICancellationSignal signal) {
        if (signal == null) {
            android.util.Slog.e(TAG, "Error dispatching a cancellation - Signal is null");
            return;
        }
        try {
            signal.cancel();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error dispatching a cancellation", e);
        }
    }
}
