package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class RemoteFillService extends com.android.internal.infra.ServiceConnector.Impl<android.service.autofill.IAutoFillService> {
    private static final java.lang.String TAG = "RemoteFillService";
    private static final long TIMEOUT_IDLE_BIND_MILLIS = 5000;
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 5000;
    private final com.android.server.autofill.RemoteFillService.FillServiceCallbacks mCallbacks;
    private final android.content.ComponentName mComponentName;
    private java.util.concurrent.atomic.AtomicReference<android.service.autofill.IConvertCredentialCallback> mConvertCredentialCallback;
    private java.util.concurrent.atomic.AtomicReference<android.service.autofill.IFillCallback> mFillCallback;
    private final boolean mIsCredentialAutofillService;
    private final java.lang.Object mLock;
    private java.util.concurrent.CompletableFuture<android.service.autofill.FillResponse> mPendingFillRequest;
    private int mPendingFillRequestId;
    private com.android.server.autofill.IRemoteFillServiceExt mRemoteFillServiceExt;
    private java.util.concurrent.atomic.AtomicReference<android.service.autofill.ISaveCallback> mSaveCallback;
    private com.android.server.autofill.IRemoteFillServiceWrapper mWrapper;

    public interface FillServiceCallbacks extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.autofill.RemoteFillService> {
        void onConvertCredentialRequestSuccess(android.service.autofill.ConvertCredentialResponse convertCredentialResponse);

        void onFillRequestFailure(int i, java.lang.Throwable th);

        void onFillRequestSuccess(int i, android.service.autofill.FillResponse fillResponse, java.lang.String str, int i2);

        void onSaveRequestFailure(java.lang.CharSequence charSequence, java.lang.String str);

        void onSaveRequestSuccess(java.lang.String str, android.content.IntentSender intentSender);
    }

    public boolean isCredentialAutofillService() {
        return this.mIsCredentialAutofillService;
    }

    RemoteFillService(android.content.Context context, android.content.ComponentName componentName, int userId, com.android.server.autofill.RemoteFillService.FillServiceCallbacks callbacks, boolean bindInstantServiceAllowed, android.content.ComponentName credentialAutofillService) {
        super(context, new android.content.Intent("android.service.autofill.AutofillService").setComponent(componentName), (bindInstantServiceAllowed ? 4194304 : 0) | 1048576, userId, new java.util.function.Function() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.service.autofill.IAutoFillService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        this.mRemoteFillServiceExt = (com.android.server.autofill.IRemoteFillServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.autofill.IRemoteFillServiceExt.class).create();
        this.mLock = new java.lang.Object();
        this.mPendingFillRequestId = Integer.MIN_VALUE;
        this.mWrapper = new com.android.server.autofill.RemoteFillService.RemoteFillServiceWrapper();
        this.mCallbacks = callbacks;
        this.mComponentName = componentName;
        this.mIsCredentialAutofillService = this.mComponentName.equals(credentialAutofillService);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(android.service.autofill.IAutoFillService service, boolean connected) {
        try {
            service.onConnectedStateChanged(connected);
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Exception calling onConnectedStateChanged(" + connected + "): " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchCancellationSignal(android.os.ICancellationSignal signal) {
        if (signal == null) {
            return;
        }
        try {
            signal.cancel();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error requesting a cancellation", e);
        }
    }

    protected long getAutoDisconnectTimeoutMs() {
        return 5000L;
    }

    public void addLast(com.android.internal.infra.ServiceConnector.Job<android.service.autofill.IAutoFillService, ?> iAutoFillServiceJob) {
        cancelPendingJobs();
        super.addLast(iAutoFillServiceJob);
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    public int cancelCurrentRequest() {
        int i;
        synchronized (this.mLock) {
            if (this.mPendingFillRequest != null && this.mPendingFillRequest.cancel(false)) {
                i = this.mPendingFillRequestId;
            } else {
                i = Integer.MIN_VALUE;
            }
        }
        return i;
    }

    static class IFillCallbackDelegate extends android.service.autofill.IFillCallback.Stub {
        private java.lang.ref.WeakReference<android.service.autofill.IFillCallback> mCallbackWeakRef;

        IFillCallbackDelegate(android.service.autofill.IFillCallback callback) {
            this.mCallbackWeakRef = new java.lang.ref.WeakReference<>(callback);
        }

        public void onCancellable(android.os.ICancellationSignal cancellation) throws android.os.RemoteException {
            android.service.autofill.IFillCallback callback = this.mCallbackWeakRef.get();
            if (callback != null) {
                callback.onCancellable(cancellation);
            }
        }

        public void onSuccess(android.service.autofill.FillResponse response) throws android.os.RemoteException {
            android.service.autofill.IFillCallback callback = this.mCallbackWeakRef.get();
            if (callback != null) {
                callback.onSuccess(response);
            }
        }

        public void onFailure(int requestId, java.lang.CharSequence message) throws android.os.RemoteException {
            android.service.autofill.IFillCallback callback = this.mCallbackWeakRef.get();
            if (callback != null) {
                callback.onFailure(requestId, message);
            }
        }
    }

    static class ISaveCallbackDelegate extends android.service.autofill.ISaveCallback.Stub {
        private java.lang.ref.WeakReference<android.service.autofill.ISaveCallback> mCallbackWeakRef;

        ISaveCallbackDelegate(android.service.autofill.ISaveCallback callback) {
            this.mCallbackWeakRef = new java.lang.ref.WeakReference<>(callback);
        }

        public void onSuccess(android.content.IntentSender intentSender) throws android.os.RemoteException {
            android.service.autofill.ISaveCallback callback = this.mCallbackWeakRef.get();
            if (callback != null) {
                callback.onSuccess(intentSender);
            }
        }

        public void onFailure(java.lang.CharSequence message) throws android.os.RemoteException {
            android.service.autofill.ISaveCallback callback = this.mCallbackWeakRef.get();
            if (callback != null) {
                callback.onFailure(message);
            }
        }
    }

    static class IConvertCredentialCallbackDelegate extends android.service.autofill.IConvertCredentialCallback.Stub {
        private java.lang.ref.WeakReference<android.service.autofill.IConvertCredentialCallback> mCallbackWeakRef;

        IConvertCredentialCallbackDelegate(android.service.autofill.IConvertCredentialCallback callback) {
            this.mCallbackWeakRef = new java.lang.ref.WeakReference<>(callback);
        }

        public void onSuccess(android.service.autofill.ConvertCredentialResponse convertCredentialResponse) throws android.os.RemoteException {
            android.service.autofill.IConvertCredentialCallback callback = this.mCallbackWeakRef.get();
            if (callback != null) {
                callback.onSuccess(convertCredentialResponse);
            }
        }

        public void onFailure(java.lang.CharSequence message) throws android.os.RemoteException {
            android.service.autofill.IConvertCredentialCallback callback = this.mCallbackWeakRef.get();
            if (callback != null) {
                callback.onFailure(message);
            }
        }
    }

    private android.service.autofill.IFillCallback maybeWrapWithWeakReference(android.service.autofill.IFillCallback callback) {
        if (android.service.autofill.Flags.remoteFillServiceUseWeakReference()) {
            this.mFillCallback = new java.util.concurrent.atomic.AtomicReference<>(callback);
            return new com.android.server.autofill.RemoteFillService.IFillCallbackDelegate(callback);
        }
        return callback;
    }

    private android.service.autofill.ISaveCallback maybeWrapWithWeakReference(android.service.autofill.ISaveCallback callback) {
        if (android.service.autofill.Flags.remoteFillServiceUseWeakReference()) {
            this.mSaveCallback = new java.util.concurrent.atomic.AtomicReference<>(callback);
            return new com.android.server.autofill.RemoteFillService.ISaveCallbackDelegate(callback);
        }
        return callback;
    }

    private android.service.autofill.IConvertCredentialCallback maybeWrapWithWeakReference(android.service.autofill.IConvertCredentialCallback callback) {
        if (android.service.autofill.Flags.remoteFillServiceUseWeakReference()) {
            this.mConvertCredentialCallback = new java.util.concurrent.atomic.AtomicReference<>(callback);
            return new com.android.server.autofill.RemoteFillService.IConvertCredentialCallbackDelegate(callback);
        }
        return callback;
    }

    public void onFillCredentialRequest(final android.service.autofill.FillRequest request, final android.os.IBinder autofillCallback) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "onFillRequest:" + request);
        }
        final java.util.concurrent.atomic.AtomicReference<android.os.ICancellationSignal> cancellationSink = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<java.util.concurrent.CompletableFuture<android.service.autofill.FillResponse>> futureRef = new java.util.concurrent.atomic.AtomicReference<>();
        com.android.internal.infra.AndroidFuture androidFutureOrTimeout = postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda10
            public final java.lang.Object run(java.lang.Object obj) {
                return this.f$0.lambda$onFillCredentialRequest$0(request, futureRef, cancellationSink, autofillCallback, (android.service.autofill.IAutoFillService) obj);
            }
        }).orTimeout(5000L, java.util.concurrent.TimeUnit.MILLISECONDS);
        futureRef.set(androidFutureOrTimeout);
        synchronized (this.mLock) {
            this.mPendingFillRequest = androidFutureOrTimeout;
            this.mPendingFillRequestId = request.getId();
        }
        androidFutureOrTimeout.whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda11
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$onFillCredentialRequest$2(request, cancellationSink, (android.service.autofill.FillResponse) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.concurrent.CompletableFuture lambda$onFillCredentialRequest$0(android.service.autofill.FillRequest request, final java.util.concurrent.atomic.AtomicReference futureRef, final java.util.concurrent.atomic.AtomicReference cancellationSink, android.os.IBinder autofillCallback, android.service.autofill.IAutoFillService remoteService) throws java.lang.Exception {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "calling onFillRequest() for id=" + request.getId());
        }
        final java.util.concurrent.CompletableFuture<android.service.autofill.FillResponse> fillRequest = new java.util.concurrent.CompletableFuture<>();
        remoteService.onFillCredentialRequest(request, maybeWrapWithWeakReference((android.service.autofill.IFillCallback) new android.service.autofill.IFillCallback.Stub() { // from class: com.android.server.autofill.RemoteFillService.1
            public void onCancellable(android.os.ICancellationSignal cancellation) {
                java.util.concurrent.CompletableFuture<android.service.autofill.FillResponse> future = (java.util.concurrent.CompletableFuture) futureRef.get();
                if (future != null && future.isCancelled()) {
                    com.android.server.autofill.RemoteFillService.this.dispatchCancellationSignal(cancellation);
                } else {
                    cancellationSink.set(cancellation);
                }
            }

            public void onSuccess(android.service.autofill.FillResponse response) {
                fillRequest.complete(response);
            }

            public void onFailure(int requestId, java.lang.CharSequence message) {
                java.lang.String errorMessage = message == null ? "" : java.lang.String.valueOf(message);
                fillRequest.completeExceptionally(new java.lang.RuntimeException(errorMessage));
            }
        }), autofillCallback);
        return fillRequest;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFillCredentialRequest$2(final android.service.autofill.FillRequest request, final java.util.concurrent.atomic.AtomicReference cancellationSink, final android.service.autofill.FillResponse res, final java.lang.Throwable err) {
        android.os.Handler.getMain().post(new java.lang.Runnable() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onFillCredentialRequest$1(err, request, res, cancellationSink);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFillCredentialRequest$1(java.lang.Throwable err, android.service.autofill.FillRequest request, android.service.autofill.FillResponse res, java.util.concurrent.atomic.AtomicReference cancellationSink) {
        synchronized (this.mLock) {
            this.mPendingFillRequest = null;
            this.mPendingFillRequestId = Integer.MIN_VALUE;
        }
        if (this.mCallbacks == null) {
            android.util.Slog.w(TAG, "Error calling RemoteFillService - service already unbound");
            return;
        }
        if (err == null) {
            this.mCallbacks.onFillRequestSuccess(request.getId(), res, this.mComponentName.getPackageName(), request.getFlags());
            return;
        }
        android.util.Slog.e(TAG, "Error calling on fill request", err);
        if (err instanceof java.util.concurrent.TimeoutException) {
            dispatchCancellationSignal((android.os.ICancellationSignal) cancellationSink.get());
            this.mCallbacks.onFillRequestFailure(request.getId(), err);
        } else if (err instanceof java.util.concurrent.CancellationException) {
            dispatchCancellationSignal((android.os.ICancellationSignal) cancellationSink.get());
        } else {
            this.mCallbacks.onFillRequestFailure(request.getId(), err);
        }
    }

    public void onFillRequest(final android.service.autofill.FillRequest request) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "onFillRequest:" + request);
        }
        final java.util.concurrent.atomic.AtomicReference<android.os.ICancellationSignal> cancellationSink = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<java.util.concurrent.CompletableFuture<android.service.autofill.FillResponse>> futureRef = new java.util.concurrent.atomic.AtomicReference<>();
        com.android.internal.infra.AndroidFuture androidFutureOrTimeout = postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda0
            public final java.lang.Object run(java.lang.Object obj) {
                return this.f$0.lambda$onFillRequest$3(request, futureRef, cancellationSink, (android.service.autofill.IAutoFillService) obj);
            }
        }).orTimeout(this.mRemoteFillServiceExt.getOplusTimeoutMillis(this.mComponentName, 5000L), java.util.concurrent.TimeUnit.MILLISECONDS);
        futureRef.set(androidFutureOrTimeout);
        synchronized (this.mLock) {
            this.mPendingFillRequest = androidFutureOrTimeout;
            this.mPendingFillRequestId = request.getId();
        }
        androidFutureOrTimeout.whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$onFillRequest$5(request, cancellationSink, (android.service.autofill.FillResponse) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.concurrent.CompletableFuture lambda$onFillRequest$3(android.service.autofill.FillRequest request, java.util.concurrent.atomic.AtomicReference futureRef, java.util.concurrent.atomic.AtomicReference cancellationSink, android.service.autofill.IAutoFillService remoteService) throws java.lang.Exception {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "calling onFillRequest() for id=" + request.getId());
        }
        java.util.concurrent.CompletableFuture<android.service.autofill.FillResponse> fillRequest = new java.util.concurrent.CompletableFuture<>();
        remoteService.onFillRequest(request, maybeWrapWithWeakReference((android.service.autofill.IFillCallback) new com.android.server.autofill.RemoteFillService.AnonymousClass2(futureRef, cancellationSink, request, fillRequest)));
        return fillRequest;
    }

    /* JADX INFO: renamed from: com.android.server.autofill.RemoteFillService$2, reason: invalid class name */
    class AnonymousClass2 extends android.service.autofill.IFillCallback.Stub {
        final /* synthetic */ java.util.concurrent.atomic.AtomicReference val$cancellationSink;
        final /* synthetic */ java.util.concurrent.CompletableFuture val$fillRequest;
        final /* synthetic */ java.util.concurrent.atomic.AtomicReference val$futureRef;
        final /* synthetic */ android.service.autofill.FillRequest val$request;

        AnonymousClass2(java.util.concurrent.atomic.AtomicReference atomicReference, java.util.concurrent.atomic.AtomicReference atomicReference2, android.service.autofill.FillRequest fillRequest, java.util.concurrent.CompletableFuture completableFuture) {
            this.val$futureRef = atomicReference;
            this.val$cancellationSink = atomicReference2;
            this.val$request = fillRequest;
            this.val$fillRequest = completableFuture;
        }

        public void onCancellable(android.os.ICancellationSignal cancellation) {
            java.util.concurrent.CompletableFuture<android.service.autofill.FillResponse> future = (java.util.concurrent.CompletableFuture) this.val$futureRef.get();
            if (future != null && future.isCancelled()) {
                com.android.server.autofill.RemoteFillService.this.dispatchCancellationSignal(cancellation);
            } else {
                this.val$cancellationSink.set(cancellation);
            }
        }

        public void onSuccess(final android.service.autofill.FillResponse response) {
            boolean requestIsNull;
            synchronized (com.android.server.autofill.RemoteFillService.this.mLock) {
                requestIsNull = com.android.server.autofill.RemoteFillService.this.mPendingFillRequest == null;
            }
            if (com.android.server.autofill.RemoteFillService.this.mRemoteFillServiceExt.hookIfNeedForceCallOnFillRequestSuccess(requestIsNull, com.android.server.autofill.RemoteFillService.this.mComponentName.getPackageName(), this.val$request, response)) {
                android.os.Handler main = android.os.Handler.getMain();
                final android.service.autofill.FillRequest fillRequest = this.val$request;
                main.post(new java.lang.Runnable() { // from class: com.android.server.autofill.RemoteFillService$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onSuccess$0(fillRequest, response);
                    }
                });
            }
            this.val$fillRequest.complete(response);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSuccess$0(android.service.autofill.FillRequest request, android.service.autofill.FillResponse response) {
            com.android.server.autofill.RemoteFillService.this.mCallbacks.onFillRequestSuccess(request.getId(), response, com.android.server.autofill.RemoteFillService.this.mComponentName.getPackageName(), request.getFlags());
        }

        public void onFailure(int requestId, java.lang.CharSequence message) {
            java.lang.String errorMessage = message == null ? "" : java.lang.String.valueOf(message);
            this.val$fillRequest.completeExceptionally(new java.lang.RuntimeException(errorMessage));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFillRequest$5(final android.service.autofill.FillRequest request, final java.util.concurrent.atomic.AtomicReference cancellationSink, final android.service.autofill.FillResponse res, final java.lang.Throwable err) {
        android.os.Handler.getMain().post(new java.lang.Runnable() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onFillRequest$4(err, request, res, cancellationSink);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFillRequest$4(java.lang.Throwable err, android.service.autofill.FillRequest request, android.service.autofill.FillResponse res, java.util.concurrent.atomic.AtomicReference cancellationSink) {
        synchronized (this.mLock) {
            this.mPendingFillRequest = null;
            this.mPendingFillRequestId = Integer.MIN_VALUE;
        }
        if (err == null) {
            this.mCallbacks.onFillRequestSuccess(request.getId(), res, this.mComponentName.getPackageName(), request.getFlags());
            return;
        }
        android.util.Slog.e(TAG, "Error calling on fill request", err);
        if (err instanceof java.util.concurrent.TimeoutException) {
            dispatchCancellationSignal((android.os.ICancellationSignal) cancellationSink.get());
            this.mCallbacks.onFillRequestFailure(request.getId(), err);
        } else if (err instanceof java.util.concurrent.CancellationException) {
            dispatchCancellationSignal((android.os.ICancellationSignal) cancellationSink.get());
        } else {
            this.mCallbacks.onFillRequestFailure(request.getId(), err);
        }
    }

    public void onConvertCredentialRequest(final android.service.autofill.ConvertCredentialRequest convertCredentialRequest) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "calling onConvertCredentialRequest()");
        }
        postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda12
            public final java.lang.Object run(java.lang.Object obj) {
                return this.f$0.lambda$onConvertCredentialRequest$6(convertCredentialRequest, (android.service.autofill.IAutoFillService) obj);
            }
        }).orTimeout(5000L, java.util.concurrent.TimeUnit.MILLISECONDS).whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda13
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$onConvertCredentialRequest$8((android.service.autofill.ConvertCredentialResponse) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.concurrent.CompletableFuture lambda$onConvertCredentialRequest$6(android.service.autofill.ConvertCredentialRequest convertCredentialRequest, android.service.autofill.IAutoFillService remoteService) throws java.lang.Exception {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "calling onConvertCredentialRequest()");
        }
        final java.util.concurrent.CompletableFuture<android.service.autofill.ConvertCredentialResponse> convertCredentialCompletableFuture = new java.util.concurrent.CompletableFuture<>();
        remoteService.onConvertCredentialRequest(convertCredentialRequest, maybeWrapWithWeakReference((android.service.autofill.IConvertCredentialCallback) new android.service.autofill.IConvertCredentialCallback.Stub() { // from class: com.android.server.autofill.RemoteFillService.3
            public void onSuccess(android.service.autofill.ConvertCredentialResponse convertCredentialResponse) {
                convertCredentialCompletableFuture.complete(convertCredentialResponse);
            }

            public void onFailure(java.lang.CharSequence message) {
                java.lang.String errorMessage = message == null ? "" : java.lang.String.valueOf(message);
                convertCredentialCompletableFuture.completeExceptionally(new java.lang.RuntimeException(errorMessage));
            }
        }));
        return convertCredentialCompletableFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConvertCredentialRequest$8(final android.service.autofill.ConvertCredentialResponse res, final java.lang.Throwable err) {
        android.os.Handler.getMain().post(new java.lang.Runnable() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onConvertCredentialRequest$7(err, res);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConvertCredentialRequest$7(java.lang.Throwable err, android.service.autofill.ConvertCredentialResponse res) {
        if (err == null) {
            this.mCallbacks.onConvertCredentialRequestSuccess(res);
        } else {
            android.util.Slog.e(TAG, "Error calling on convert credential request", err);
        }
    }

    public void onSaveRequest(final android.service.autofill.SaveRequest request) {
        postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda3
            public final java.lang.Object run(java.lang.Object obj) {
                return this.f$0.lambda$onSaveRequest$9(request, (android.service.autofill.IAutoFillService) obj);
            }
        }).orTimeout(this.mRemoteFillServiceExt.getOplusTimeoutMillis(this.mComponentName, 5000L), java.util.concurrent.TimeUnit.MILLISECONDS).whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$onSaveRequest$11((android.content.IntentSender) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.concurrent.CompletableFuture lambda$onSaveRequest$9(android.service.autofill.SaveRequest request, android.service.autofill.IAutoFillService service) throws java.lang.Exception {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "calling onSaveRequest()");
        }
        final java.util.concurrent.CompletableFuture<android.content.IntentSender> save = new java.util.concurrent.CompletableFuture<>();
        service.onSaveRequest(request, maybeWrapWithWeakReference((android.service.autofill.ISaveCallback) new android.service.autofill.ISaveCallback.Stub() { // from class: com.android.server.autofill.RemoteFillService.4
            public void onSuccess(android.content.IntentSender intentSender) {
                save.complete(intentSender);
            }

            public void onFailure(java.lang.CharSequence message) {
                save.completeExceptionally(new java.lang.RuntimeException(java.lang.String.valueOf(message)));
            }
        }));
        return save;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSaveRequest$11(final android.content.IntentSender res, final java.lang.Throwable err) {
        android.os.Handler.getMain().post(new java.lang.Runnable() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSaveRequest$10(err, res);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSaveRequest$10(java.lang.Throwable err, android.content.IntentSender res) {
        if (err == null) {
            this.mCallbacks.onSaveRequestSuccess(this.mComponentName.getPackageName(), res);
        } else {
            this.mCallbacks.onSaveRequestFailure(this.mComponentName.getPackageName(), err.getMessage());
        }
    }

    void onSavedPasswordCountRequest(final com.android.internal.os.IResultReceiver receiver) {
        run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda6
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.autofill.IAutoFillService) obj).onSavedPasswordCountRequest(receiver);
            }
        });
    }

    protected long getRequestTimeoutMs() {
        return this.mRemoteFillServiceExt.getOplusRequestTimeoutMillis(this.mComponentName, super.getRequestTimeoutMs());
    }

    public void destroy() {
        unbind();
    }

    protected boolean bindService(android.content.ServiceConnection serviceConnection) {
        if (!this.mRemoteFillServiceExt.getSessionDestroyed()) {
            return super.bindService(serviceConnection);
        }
        android.util.Slog.e(TAG, "Session already destroyed! Skip bind service!");
        return false;
    }

    public com.android.server.autofill.IRemoteFillServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    private class RemoteFillServiceWrapper implements com.android.server.autofill.IRemoteFillServiceWrapper {
        private RemoteFillServiceWrapper() {
        }

        @Override // com.android.server.autofill.IRemoteFillServiceWrapper
        public com.android.server.autofill.IRemoteFillServiceExt getRemoteFillServiceExt() {
            return com.android.server.autofill.RemoteFillService.this.mRemoteFillServiceExt;
        }

        @Override // com.android.server.autofill.IRemoteFillServiceWrapper
        public void delayCancelRequest(java.util.List<android.service.autofill.FillContext> fillContexts) {
            synchronized (com.android.server.autofill.RemoteFillService.this.mLock) {
                com.android.server.autofill.RemoteFillService.this.mRemoteFillServiceExt.delayCancelRequest(fillContexts, com.android.server.autofill.RemoteFillService.this.mPendingFillRequestId, com.android.server.autofill.RemoteFillService.this.mPendingFillRequest);
            }
        }
    }
}
