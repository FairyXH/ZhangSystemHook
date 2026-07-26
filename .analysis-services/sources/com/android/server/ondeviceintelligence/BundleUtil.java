package com.android.server.ondeviceintelligence;

/* JADX INFO: loaded from: classes2.dex */
public class BundleUtil {
    private static final java.lang.String TAG = "BundleUtil";

    public static void sanitizeInferenceParams(android.os.Bundle bundle) {
        ensureValidBundle(bundle);
        if (!bundle.hasFileDescriptors()) {
            return;
        }
        for (java.lang.String key : bundle.keySet()) {
            java.lang.Object obj = bundle.get(key);
            if (obj == null) {
                bundle.putObject(key, null);
            } else if (!canMarshall(obj) && !(obj instanceof android.database.CursorWindow)) {
                if (obj instanceof android.os.Bundle) {
                    sanitizeInferenceParams((android.os.Bundle) obj);
                } else if (obj instanceof android.os.ParcelFileDescriptor) {
                    validatePfdReadOnly((android.os.ParcelFileDescriptor) obj);
                } else if (obj instanceof android.os.SharedMemory) {
                    ((android.os.SharedMemory) obj).setProtect(android.system.OsConstants.PROT_READ);
                } else if (obj instanceof android.graphics.Bitmap) {
                    validateBitmap((android.graphics.Bitmap) obj);
                } else if (obj instanceof android.os.Parcelable[]) {
                    validateParcelableArray((android.os.Parcelable[]) obj);
                } else {
                    throw new android.os.BadParcelableException("Unsupported Parcelable type encountered in the Bundle: " + obj.getClass().getSimpleName());
                }
            }
        }
    }

    public static void sanitizeResponseParams(android.os.Bundle bundle) {
        ensureValidBundle(bundle);
        if (!bundle.hasFileDescriptors()) {
            return;
        }
        for (java.lang.String key : bundle.keySet()) {
            java.lang.Object obj = bundle.get(key);
            if (obj == null) {
                bundle.putObject(key, null);
            } else if (canMarshall(obj)) {
                continue;
            } else if (obj instanceof android.os.Bundle) {
                sanitizeResponseParams((android.os.Bundle) obj);
            } else if (obj instanceof android.os.ParcelFileDescriptor) {
                validatePfdReadOnly((android.os.ParcelFileDescriptor) obj);
            } else if (obj instanceof android.graphics.Bitmap) {
                validateBitmap((android.graphics.Bitmap) obj);
            } else if (obj instanceof android.os.Parcelable[]) {
                validateParcelableArray((android.os.Parcelable[]) obj);
            } else {
                throw new android.os.BadParcelableException("Unsupported Parcelable type encountered in the Bundle: " + obj.getClass().getSimpleName());
            }
        }
    }

    public static void sanitizeStateParams(android.os.Bundle bundle) {
        ensureValidBundle(bundle);
        if (!bundle.hasFileDescriptors()) {
            return;
        }
        for (java.lang.String key : bundle.keySet()) {
            java.lang.Object obj = bundle.get(key);
            if (obj == null) {
                bundle.putObject(key, null);
            } else if (canMarshall(obj)) {
                continue;
            } else if (obj instanceof android.os.ParcelFileDescriptor) {
                validatePfdReadOnly((android.os.ParcelFileDescriptor) obj);
            } else {
                throw new android.os.BadParcelableException("Unsupported Parcelable type encountered in the Bundle: " + obj.getClass().getSimpleName());
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.ondeviceintelligence.BundleUtil$1, reason: invalid class name */
    class AnonymousClass1 extends android.app.ondeviceintelligence.IStreamingResponseCallback.Stub {
        final /* synthetic */ com.android.internal.infra.AndroidFuture val$future;
        final /* synthetic */ com.android.server.ondeviceintelligence.InferenceInfoStore val$inferenceInfoStore;
        final /* synthetic */ java.util.concurrent.Executor val$resourceClosingExecutor;
        final /* synthetic */ android.app.ondeviceintelligence.IStreamingResponseCallback val$streamingResponseCallback;

        AnonymousClass1(android.app.ondeviceintelligence.IStreamingResponseCallback iStreamingResponseCallback, java.util.concurrent.Executor executor, com.android.server.ondeviceintelligence.InferenceInfoStore inferenceInfoStore, com.android.internal.infra.AndroidFuture androidFuture) {
            this.val$streamingResponseCallback = iStreamingResponseCallback;
            this.val$resourceClosingExecutor = executor;
            this.val$inferenceInfoStore = inferenceInfoStore;
            this.val$future = androidFuture;
        }

        public void onNewContent(final android.os.Bundle processedResult) throws android.os.RemoteException {
            try {
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeResponseParams(processedResult);
                this.val$streamingResponseCallback.onNewContent(processedResult);
            } finally {
                this.val$resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.BundleUtil$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(processedResult);
                    }
                });
            }
        }

        public void onSuccess(final android.os.Bundle resultBundle) throws android.os.RemoteException {
            try {
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeResponseParams(resultBundle);
                this.val$streamingResponseCallback.onSuccess(resultBundle);
            } finally {
                this.val$inferenceInfoStore.addInferenceInfoFromBundle(resultBundle);
                this.val$resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.BundleUtil$1$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(resultBundle);
                    }
                });
                this.val$future.complete((java.lang.Object) null);
            }
        }

        public void onFailure(int errorCode, java.lang.String errorMessage, android.os.PersistableBundle errorParams) throws android.os.RemoteException {
            this.val$streamingResponseCallback.onFailure(errorCode, errorMessage, errorParams);
            this.val$inferenceInfoStore.addInferenceInfoFromBundle(errorParams);
            this.val$future.completeExceptionally(new java.util.concurrent.TimeoutException());
        }

        public void onDataAugmentRequest(final android.os.Bundle processedContent, final android.os.RemoteCallback remoteCallback) throws android.os.RemoteException {
            try {
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeResponseParams(processedContent);
                android.app.ondeviceintelligence.IStreamingResponseCallback iStreamingResponseCallback = this.val$streamingResponseCallback;
                final java.util.concurrent.Executor executor = this.val$resourceClosingExecutor;
                iStreamingResponseCallback.onDataAugmentRequest(processedContent, new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ondeviceintelligence.BundleUtil$1$$ExternalSyntheticLambda3
                    public final void onResult(android.os.Bundle bundle) {
                        com.android.server.ondeviceintelligence.BundleUtil.AnonymousClass1.lambda$onDataAugmentRequest$3(remoteCallback, executor, bundle);
                    }
                }));
            } finally {
                this.val$resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.BundleUtil$1$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(processedContent);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$onDataAugmentRequest$3(android.os.RemoteCallback remoteCallback, java.util.concurrent.Executor resourceClosingExecutor, final android.os.Bundle augmentedData) {
            try {
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeInferenceParams(augmentedData);
                remoteCallback.sendResult(augmentedData);
            } finally {
                resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.BundleUtil$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(augmentedData);
                    }
                });
            }
        }
    }

    public static android.app.ondeviceintelligence.IStreamingResponseCallback wrapWithValidation(android.app.ondeviceintelligence.IStreamingResponseCallback streamingResponseCallback, java.util.concurrent.Executor resourceClosingExecutor, com.android.internal.infra.AndroidFuture future, com.android.server.ondeviceintelligence.InferenceInfoStore inferenceInfoStore) {
        return new com.android.server.ondeviceintelligence.BundleUtil.AnonymousClass1(streamingResponseCallback, resourceClosingExecutor, inferenceInfoStore, future);
    }

    /* JADX INFO: renamed from: com.android.server.ondeviceintelligence.BundleUtil$2, reason: invalid class name */
    class AnonymousClass2 extends android.app.ondeviceintelligence.IResponseCallback.Stub {
        final /* synthetic */ com.android.internal.infra.AndroidFuture val$future;
        final /* synthetic */ com.android.server.ondeviceintelligence.InferenceInfoStore val$inferenceInfoStore;
        final /* synthetic */ java.util.concurrent.Executor val$resourceClosingExecutor;
        final /* synthetic */ android.app.ondeviceintelligence.IResponseCallback val$responseCallback;

        AnonymousClass2(android.app.ondeviceintelligence.IResponseCallback iResponseCallback, com.android.server.ondeviceintelligence.InferenceInfoStore inferenceInfoStore, java.util.concurrent.Executor executor, com.android.internal.infra.AndroidFuture androidFuture) {
            this.val$responseCallback = iResponseCallback;
            this.val$inferenceInfoStore = inferenceInfoStore;
            this.val$resourceClosingExecutor = executor;
            this.val$future = androidFuture;
        }

        public void onSuccess(final android.os.Bundle resultBundle) throws android.os.RemoteException {
            try {
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeResponseParams(resultBundle);
                this.val$responseCallback.onSuccess(resultBundle);
            } finally {
                this.val$inferenceInfoStore.addInferenceInfoFromBundle(resultBundle);
                this.val$resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.BundleUtil$2$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(resultBundle);
                    }
                });
                this.val$future.complete((java.lang.Object) null);
            }
        }

        public void onFailure(int errorCode, java.lang.String errorMessage, android.os.PersistableBundle errorParams) throws android.os.RemoteException {
            this.val$responseCallback.onFailure(errorCode, errorMessage, errorParams);
            this.val$inferenceInfoStore.addInferenceInfoFromBundle(errorParams);
            this.val$future.completeExceptionally(new java.util.concurrent.TimeoutException());
        }

        public void onDataAugmentRequest(final android.os.Bundle processedContent, final android.os.RemoteCallback remoteCallback) throws android.os.RemoteException {
            try {
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeResponseParams(processedContent);
                android.app.ondeviceintelligence.IResponseCallback iResponseCallback = this.val$responseCallback;
                final java.util.concurrent.Executor executor = this.val$resourceClosingExecutor;
                iResponseCallback.onDataAugmentRequest(processedContent, new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ondeviceintelligence.BundleUtil$2$$ExternalSyntheticLambda1
                    public final void onResult(android.os.Bundle bundle) {
                        com.android.server.ondeviceintelligence.BundleUtil.AnonymousClass2.lambda$onDataAugmentRequest$2(remoteCallback, executor, bundle);
                    }
                }));
            } finally {
                this.val$resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.BundleUtil$2$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(processedContent);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$onDataAugmentRequest$2(android.os.RemoteCallback remoteCallback, java.util.concurrent.Executor resourceClosingExecutor, final android.os.Bundle augmentedData) {
            try {
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeInferenceParams(augmentedData);
                remoteCallback.sendResult(augmentedData);
            } finally {
                resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.BundleUtil$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(augmentedData);
                    }
                });
            }
        }
    }

    public static android.app.ondeviceintelligence.IResponseCallback wrapWithValidation(android.app.ondeviceintelligence.IResponseCallback responseCallback, java.util.concurrent.Executor resourceClosingExecutor, com.android.internal.infra.AndroidFuture future, com.android.server.ondeviceintelligence.InferenceInfoStore inferenceInfoStore) {
        return new com.android.server.ondeviceintelligence.BundleUtil.AnonymousClass2(responseCallback, inferenceInfoStore, resourceClosingExecutor, future);
    }

    public static android.app.ondeviceintelligence.ITokenInfoCallback wrapWithValidation(final android.app.ondeviceintelligence.ITokenInfoCallback responseCallback, final com.android.internal.infra.AndroidFuture future, final com.android.server.ondeviceintelligence.InferenceInfoStore inferenceInfoStore) {
        return new android.app.ondeviceintelligence.ITokenInfoCallback.Stub() { // from class: com.android.server.ondeviceintelligence.BundleUtil.3
            public void onSuccess(android.app.ondeviceintelligence.TokenInfo tokenInfo) throws android.os.RemoteException {
                responseCallback.onSuccess(tokenInfo);
                inferenceInfoStore.addInferenceInfoFromBundle(tokenInfo.getInfoParams());
                future.complete((java.lang.Object) null);
            }

            public void onFailure(int errorCode, java.lang.String errorMessage, android.os.PersistableBundle errorParams) throws android.os.RemoteException {
                responseCallback.onFailure(errorCode, errorMessage, errorParams);
                inferenceInfoStore.addInferenceInfoFromBundle(errorParams);
                future.completeExceptionally(new java.util.concurrent.TimeoutException());
            }
        };
    }

    private static boolean canMarshall(java.lang.Object obj) {
        return (obj instanceof byte[]) || (obj instanceof android.os.PersistableBundle) || android.os.PersistableBundle.isValidType(obj);
    }

    private static void ensureValidBundle(android.os.Bundle bundle) {
        if (bundle == null) {
            throw new java.lang.IllegalArgumentException("Request passed is expected to be non-null");
        }
        if (bundle.hasBinders() != 0) {
            throw new android.os.BadParcelableException("Bundle should not contain IBinder objects.");
        }
    }

    private static void validateParcelableArray(android.os.Parcelable[] parcelables) {
        if (parcelables.length > 0 && (parcelables[0] instanceof android.os.ParcelFileDescriptor)) {
            validatePfdsReadOnly(parcelables);
        } else {
            if (parcelables.length > 0 && (parcelables[0] instanceof android.graphics.Bitmap)) {
                validateBitmapsImmutable(parcelables);
                return;
            }
            throw new android.os.BadParcelableException("Could not cast to any known parcelable array");
        }
    }

    public static void validatePfdsReadOnly(android.os.Parcelable[] pfds) {
        for (android.os.Parcelable pfd : pfds) {
            validatePfdReadOnly((android.os.ParcelFileDescriptor) pfd);
        }
    }

    public static void validatePfdReadOnly(android.os.ParcelFileDescriptor pfd) {
        if (pfd == null) {
            return;
        }
        try {
            int readMode = android.system.Os.fcntlInt(pfd.getFileDescriptor(), android.system.OsConstants.F_GETFL, 0) & android.system.OsConstants.O_ACCMODE;
            if (readMode != android.system.OsConstants.O_RDONLY) {
                throw new android.os.BadParcelableException("Bundle contains a parcel file descriptor which is not read-only.");
            }
        } catch (android.system.ErrnoException e) {
            throw new android.os.BadParcelableException("Invalid File descriptor passed in the Bundle.", e);
        }
    }

    private static void validateBitmap(android.graphics.Bitmap obj) {
        if (obj.isMutable()) {
            throw new android.os.BadParcelableException("Encountered a mutable Bitmap in the Bundle at key : " + obj);
        }
    }

    private static void validateBitmapsImmutable(android.os.Parcelable[] bitmaps) {
        for (android.os.Parcelable bitmap : bitmaps) {
            validateBitmap((android.graphics.Bitmap) bitmap);
        }
    }

    public static void tryCloseResource(android.os.Bundle bundle) {
        if (bundle == null || bundle.isEmpty() || !bundle.hasFileDescriptors()) {
            return;
        }
        for (java.lang.String key : bundle.keySet()) {
            java.lang.Object obj = bundle.get(key);
            try {
                if (obj instanceof android.os.ParcelFileDescriptor) {
                    ((android.os.ParcelFileDescriptor) obj).close();
                } else if (obj instanceof android.database.CursorWindow) {
                    ((android.database.CursorWindow) obj).close();
                } else if (obj instanceof android.os.SharedMemory) {
                    ((android.os.SharedMemory) obj).close();
                }
            } catch (java.lang.Exception e) {
                android.util.Log.e(TAG, "Error closing resource with key: " + key, e);
            }
        }
    }
}
