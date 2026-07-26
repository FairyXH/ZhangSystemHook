package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class RemoteFieldClassificationService extends com.android.internal.infra.ServiceConnector.Impl<android.service.assist.classification.IFieldClassificationService> {
    private static final java.lang.String TAG = "Autofill" + com.android.server.autofill.RemoteFieldClassificationService.class.getSimpleName();
    private static final long TIMEOUT_IDLE_UNBIND_MS = 0;
    private final android.content.ComponentName mComponentName;

    public interface FieldClassificationServiceCallbacks {
        void logFieldClassificationEvent(long j, android.service.assist.classification.FieldClassificationResponse fieldClassificationResponse, int i);

        void onClassificationRequestFailure(int i, java.lang.CharSequence charSequence);

        void onClassificationRequestSuccess(android.service.assist.classification.FieldClassificationResponse fieldClassificationResponse);

        void onClassificationRequestTimeout(int i);

        void onServiceDied(com.android.server.autofill.RemoteFieldClassificationService remoteFieldClassificationService);
    }

    RemoteFieldClassificationService(android.content.Context context, android.content.ComponentName serviceName, int serviceUid, int userId) {
        super(context, new android.content.Intent("android.service.assist.classification.FieldClassificationService").setComponent(serviceName), 0, userId, new java.util.function.Function() { // from class: com.android.server.autofill.RemoteFieldClassificationService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.service.assist.classification.IFieldClassificationService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        this.mComponentName = serviceName;
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "About to connect to serviceName: " + serviceName);
        }
        connect();
    }

    static android.util.Pair<android.content.pm.ServiceInfo, android.content.ComponentName> getComponentName(java.lang.String serviceName, int userId, boolean isTemporary) {
        int flags = isTemporary ? 128 : 128 | 1048576;
        try {
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            android.content.pm.ServiceInfo serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, flags, userId);
            if (serviceInfo == null) {
                android.util.Slog.e(TAG, "Bad service name for flags " + flags + ": " + serviceName);
                return null;
            }
            return new android.util.Pair<>(serviceInfo, serviceComponent);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Error getting service info for '" + serviceName + "': " + e);
            return null;
        }
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(android.service.assist.classification.IFieldClassificationService service, boolean connected) {
        try {
            if (connected) {
                service.onConnected(false, false);
            } else {
                service.onDisconnected();
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Exception calling onServiceConnectionStatusChanged(" + connected + "): ", e);
        }
    }

    protected long getAutoDisconnectTimeoutMs() {
        return 0L;
    }

    public void onFieldClassificationRequest(final android.service.assist.classification.FieldClassificationRequest request, final java.lang.ref.WeakReference<com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks> fieldClassificationServiceCallbacksWeakRef) {
        final long startTime = android.os.SystemClock.elapsedRealtime();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "onFieldClassificationRequest request:" + request);
        }
        run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.autofill.RemoteFieldClassificationService$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                this.f$0.lambda$onFieldClassificationRequest$0(request, fieldClassificationServiceCallbacksWeakRef, startTime, (android.service.assist.classification.IFieldClassificationService) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFieldClassificationRequest$0(android.service.assist.classification.FieldClassificationRequest request, final java.lang.ref.WeakReference fieldClassificationServiceCallbacksWeakRef, final long startTime, android.service.assist.classification.IFieldClassificationService s) throws java.lang.Exception {
        s.onFieldClassificationRequest(request, new android.service.assist.classification.IFieldClassificationCallback.Stub() { // from class: com.android.server.autofill.RemoteFieldClassificationService.1
            public void onCancellable(android.os.ICancellationSignal cancellation) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Log.d(com.android.server.autofill.RemoteFieldClassificationService.TAG, "onCancellable");
                }
                com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks fieldClassificationServiceCallbacks = (com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks) com.android.server.autofill.Helper.weakDeref(fieldClassificationServiceCallbacksWeakRef, com.android.server.autofill.RemoteFieldClassificationService.TAG, "onCancellable ");
                com.android.server.autofill.RemoteFieldClassificationService.this.logFieldClassificationEvent(startTime, fieldClassificationServiceCallbacks, 3, null);
            }

            public void onSuccess(android.service.assist.classification.FieldClassificationResponse response) {
                java.lang.String msg;
                if (com.android.server.autofill.Helper.sDebug) {
                    if (android.os.Build.IS_DEBUGGABLE) {
                        android.util.Slog.d(com.android.server.autofill.RemoteFieldClassificationService.TAG, "onSuccess Response: " + response);
                    } else {
                        if (response == null || response.getClassifications() == null) {
                            msg = "null response";
                        } else {
                            msg = "size: " + response.getClassifications().size();
                        }
                        android.util.Slog.d(com.android.server.autofill.RemoteFieldClassificationService.TAG, "onSuccess " + msg);
                    }
                }
                com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks fieldClassificationServiceCallbacks = (com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks) com.android.server.autofill.Helper.weakDeref(fieldClassificationServiceCallbacksWeakRef, com.android.server.autofill.RemoteFieldClassificationService.TAG, "onSuccess ");
                com.android.server.autofill.RemoteFieldClassificationService.this.logFieldClassificationEvent(startTime, fieldClassificationServiceCallbacks, 1, response);
                if (fieldClassificationServiceCallbacks == null) {
                    return;
                }
                fieldClassificationServiceCallbacks.onClassificationRequestSuccess(response);
            }

            public void onFailure() {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(com.android.server.autofill.RemoteFieldClassificationService.TAG, "onFailure");
                }
                com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks fieldClassificationServiceCallbacks = (com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks) com.android.server.autofill.Helper.weakDeref(fieldClassificationServiceCallbacksWeakRef, com.android.server.autofill.RemoteFieldClassificationService.TAG, "onFailure ");
                com.android.server.autofill.RemoteFieldClassificationService.this.logFieldClassificationEvent(startTime, fieldClassificationServiceCallbacks, 2, null);
                if (fieldClassificationServiceCallbacks == null) {
                    return;
                }
                fieldClassificationServiceCallbacks.onClassificationRequestFailure(0, null);
            }

            public boolean isCompleted() throws android.os.RemoteException {
                return false;
            }

            public void cancel() throws android.os.RemoteException {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logFieldClassificationEvent(long startTime, com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks fieldClassificationServiceCallbacks, int status, android.service.assist.classification.FieldClassificationResponse response) {
        if (fieldClassificationServiceCallbacks == null) {
            com.android.server.autofill.FieldClassificationEventLogger logger = com.android.server.autofill.FieldClassificationEventLogger.createLogger();
            logger.startNewLogForRequest();
            logger.maybeSetLatencyMillis(android.os.SystemClock.elapsedRealtime() - startTime);
            logger.maybeSetSessionGc(true);
            logger.maybeSetRequestStatus(status);
            logger.logAndEndEvent();
            return;
        }
        fieldClassificationServiceCallbacks.logFieldClassificationEvent(startTime, response, status);
    }
}
