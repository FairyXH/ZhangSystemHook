package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public class ResumeOnRebootServiceProvider {
    static final java.lang.String PROP_ROR_PROVIDER_PACKAGE = "persist.sys.resume_on_reboot_provider_package";
    private static final java.lang.String PROVIDER_PACKAGE = android.provider.DeviceConfig.getString("ota", "resume_on_reboot_service_package", "");
    private static final java.lang.String PROVIDER_REQUIRED_PERMISSION = "android.permission.BIND_RESUME_ON_REBOOT_SERVICE";
    private static final java.lang.String TAG = "ResumeOnRebootServiceProvider";
    private final android.content.Context mContext;
    private final android.content.pm.PackageManager mPackageManager;

    public ResumeOnRebootServiceProvider(android.content.Context context) {
        this(context, context.getPackageManager());
    }

    public ResumeOnRebootServiceProvider(android.content.Context context, android.content.pm.PackageManager packageManager) {
        this.mContext = context;
        this.mPackageManager = packageManager;
    }

    private android.content.pm.ServiceInfo resolveService() {
        android.content.Intent intent = new android.content.Intent();
        intent.setAction("android.service.resumeonreboot.ResumeOnRebootService");
        int queryFlag = 4;
        java.lang.String testAppName = android.os.SystemProperties.get(PROP_ROR_PROVIDER_PACKAGE, "");
        if (testAppName.isEmpty()) {
            queryFlag = 4 | 1048576;
            if (PROVIDER_PACKAGE != null && !PROVIDER_PACKAGE.equals("")) {
                intent.setPackage(PROVIDER_PACKAGE);
            }
        } else {
            android.util.Slog.i(TAG, "Using test app: " + testAppName);
            intent.setPackage(testAppName);
        }
        java.util.List<android.content.pm.ResolveInfo> resolvedIntents = this.mPackageManager.queryIntentServices(intent, queryFlag);
        for (android.content.pm.ResolveInfo resolvedInfo : resolvedIntents) {
            if (resolvedInfo.serviceInfo != null && PROVIDER_REQUIRED_PERMISSION.equals(resolvedInfo.serviceInfo.permission)) {
                return resolvedInfo.serviceInfo;
            }
        }
        return null;
    }

    public com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection getServiceConnection() {
        android.content.pm.ServiceInfo serviceInfo = resolveService();
        if (serviceInfo == null) {
            return null;
        }
        return new com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection(this.mContext, serviceInfo.getComponentName());
    }

    public static class ResumeOnRebootServiceConnection {
        private static final java.lang.String TAG = "ResumeOnRebootServiceConnection";
        private android.service.resumeonreboot.IResumeOnRebootService mBinder;
        private final android.content.ComponentName mComponentName;
        private final android.content.Context mContext;
        android.content.ServiceConnection mServiceConnection;

        private ResumeOnRebootServiceConnection(android.content.Context context, android.content.ComponentName componentName) {
            this.mContext = context;
            this.mComponentName = componentName;
        }

        public void unbindService() {
            if (this.mServiceConnection != null) {
                this.mContext.unbindService(this.mServiceConnection);
            }
            this.mBinder = null;
        }

        public void bindToService(long timeOut) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
            if (this.mBinder == null || !this.mBinder.asBinder().isBinderAlive()) {
                final java.util.concurrent.CountDownLatch connectionLatch = new java.util.concurrent.CountDownLatch(1);
                android.content.Intent intent = new android.content.Intent();
                intent.setComponent(this.mComponentName);
                this.mServiceConnection = new android.content.ServiceConnection() { // from class: com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection.1
                    @Override // android.content.ServiceConnection
                    public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                        com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection.this.mBinder = android.service.resumeonreboot.IResumeOnRebootService.Stub.asInterface(service);
                        connectionLatch.countDown();
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceDisconnected(android.content.ComponentName name) {
                        com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection.this.mBinder = null;
                    }
                };
                boolean success = this.mContext.bindServiceAsUser(intent, this.mServiceConnection, android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN, com.android.internal.os.BackgroundThread.getHandler(), android.os.UserHandle.SYSTEM);
                if (!success) {
                    android.util.Slog.e(TAG, "Binding: " + this.mComponentName + " u" + android.os.UserHandle.SYSTEM + " failed.");
                } else {
                    waitForLatch(connectionLatch, "serviceConnection", timeOut);
                }
            }
        }

        public byte[] wrapBlob(byte[] unwrappedBlob, long lifeTimeInMillis, long timeOutInMillis) throws java.util.concurrent.TimeoutException, android.os.RemoteException, java.io.IOException {
            if (this.mBinder == null || !this.mBinder.asBinder().isBinderAlive()) {
                throw new android.os.RemoteException("Service not bound");
            }
            java.util.concurrent.CountDownLatch binderLatch = new java.util.concurrent.CountDownLatch(1);
            com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceCallback resultCallback = new com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceCallback(binderLatch);
            this.mBinder.wrapSecret(unwrappedBlob, lifeTimeInMillis, new android.os.RemoteCallback(resultCallback));
            waitForLatch(binderLatch, "wrapSecret", timeOutInMillis);
            if (resultCallback.getResult().containsKey("exception_key")) {
                throwTypedException((android.os.ParcelableException) resultCallback.getResult().getParcelable("exception_key", android.os.ParcelableException.class));
            }
            return resultCallback.mResult.getByteArray("wrapped_blob_key");
        }

        public byte[] unwrap(byte[] wrappedBlob, long timeOut) throws java.util.concurrent.TimeoutException, android.os.RemoteException, java.io.IOException {
            if (this.mBinder == null || !this.mBinder.asBinder().isBinderAlive()) {
                throw new android.os.RemoteException("Service not bound");
            }
            java.util.concurrent.CountDownLatch binderLatch = new java.util.concurrent.CountDownLatch(1);
            com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceCallback resultCallback = new com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceCallback(binderLatch);
            this.mBinder.unwrap(wrappedBlob, new android.os.RemoteCallback(resultCallback));
            waitForLatch(binderLatch, "unWrapSecret", timeOut);
            if (resultCallback.getResult().containsKey("exception_key")) {
                throwTypedException((android.os.ParcelableException) resultCallback.getResult().getParcelable("exception_key", android.os.ParcelableException.class));
            }
            return resultCallback.getResult().getByteArray("unrwapped_blob_key");
        }

        private void throwTypedException(android.os.ParcelableException exception) throws android.os.RemoteException, java.io.IOException {
            if (exception != null && (exception.getCause() instanceof java.io.IOException)) {
                exception.maybeRethrow(java.io.IOException.class);
                return;
            }
            throw new android.os.RemoteException("ResumeOnRebootServiceConnection wrap/unwrap failed", exception, true, true);
        }

        private void waitForLatch(java.util.concurrent.CountDownLatch latch, java.lang.String reason, long timeOut) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
            try {
                if (!latch.await(timeOut, java.util.concurrent.TimeUnit.SECONDS)) {
                    throw new java.util.concurrent.TimeoutException("Latch wait for " + reason + " elapsed");
                }
            } catch (java.lang.InterruptedException e) {
                java.lang.Thread.currentThread().interrupt();
                throw new android.os.RemoteException("Latch wait for " + reason + " interrupted");
            }
        }
    }

    private static class ResumeOnRebootServiceCallback implements android.os.RemoteCallback.OnResultListener {
        private android.os.Bundle mResult;
        private final java.util.concurrent.CountDownLatch mResultLatch;

        private ResumeOnRebootServiceCallback(java.util.concurrent.CountDownLatch resultLatch) {
            this.mResultLatch = resultLatch;
        }

        public void onResult(android.os.Bundle result) {
            this.mResult = result;
            this.mResultLatch.countDown();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.os.Bundle getResult() {
            return this.mResult;
        }
    }
}
