package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayHashController {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "WindowManager";
    private final android.content.Context mContext;
    private java.util.Map<java.lang.String, android.service.displayhash.DisplayHashParams> mDisplayHashAlgorithms;
    private long mLastRequestTimeMs;
    private int mLastRequestUid;
    private com.android.server.wm.DisplayHashController.DisplayHashingServiceConnection mServiceConnection;
    private final java.lang.Object mServiceConnectionLock = new java.lang.Object();
    private final java.lang.Object mDisplayHashAlgorithmsLock = new java.lang.Object();
    private final float[] mTmpFloat9 = new float[9];
    private final android.graphics.Matrix mTmpMatrix = new android.graphics.Matrix();
    private final android.graphics.RectF mTmpRectF = new android.graphics.RectF();
    private final java.lang.Object mIntervalBetweenRequestsLock = new java.lang.Object();
    private int mIntervalBetweenRequestMillis = -1;
    private boolean mDisplayHashThrottlingEnabled = true;
    private final com.android.server.wm.DisplayHashController.Handler mHandler = new com.android.server.wm.DisplayHashController.Handler(android.os.Looper.getMainLooper());
    private final byte[] mSalt = java.util.UUID.randomUUID().toString().getBytes();

    /* JADX INFO: Access modifiers changed from: private */
    interface Command {
        void run(android.service.displayhash.IDisplayHashingService iDisplayHashingService) throws android.os.RemoteException;
    }

    DisplayHashController(android.content.Context context) {
        this.mContext = context;
    }

    java.lang.String[] getSupportedHashAlgorithms() {
        java.util.Map<java.lang.String, android.service.displayhash.DisplayHashParams> displayHashAlgorithms = getDisplayHashAlgorithms();
        return (java.lang.String[]) displayHashAlgorithms.keySet().toArray(new java.lang.String[0]);
    }

    android.view.displayhash.VerifiedDisplayHash verifyDisplayHash(final android.view.displayhash.DisplayHash displayHash) {
        com.android.server.wm.DisplayHashController.SyncCommand syncCommand = new com.android.server.wm.DisplayHashController.SyncCommand();
        android.os.Bundle results = syncCommand.run(new java.util.function.BiConsumer() { // from class: com.android.server.wm.DisplayHashController$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$verifyDisplayHash$0(displayHash, (android.service.displayhash.IDisplayHashingService) obj, (android.os.RemoteCallback) obj2);
            }
        });
        return (android.view.displayhash.VerifiedDisplayHash) results.getParcelable("android.service.displayhash.extra.VERIFIED_DISPLAY_HASH");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyDisplayHash$0(android.view.displayhash.DisplayHash displayHash, android.service.displayhash.IDisplayHashingService service, android.os.RemoteCallback remoteCallback) {
        try {
            service.verifyDisplayHash(this.mSalt, displayHash, remoteCallback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to invoke verifyDisplayHash command");
        }
    }

    void setDisplayHashThrottlingEnabled(boolean enable) {
        this.mDisplayHashThrottlingEnabled = enable;
    }

    private void generateDisplayHash(final android.hardware.HardwareBuffer buffer, final android.graphics.Rect bounds, final java.lang.String hashAlgorithm, final android.os.RemoteCallback callback) {
        connectAndRun(new com.android.server.wm.DisplayHashController.Command() { // from class: com.android.server.wm.DisplayHashController$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.DisplayHashController.Command
            public final void run(android.service.displayhash.IDisplayHashingService iDisplayHashingService) throws android.os.RemoteException {
                this.f$0.lambda$generateDisplayHash$1(buffer, bounds, hashAlgorithm, callback, iDisplayHashingService);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateDisplayHash$1(android.hardware.HardwareBuffer buffer, android.graphics.Rect bounds, java.lang.String hashAlgorithm, android.os.RemoteCallback callback, android.service.displayhash.IDisplayHashingService service) throws android.os.RemoteException {
        service.generateDisplayHash(this.mSalt, buffer, bounds, hashAlgorithm, callback);
    }

    private boolean allowedToGenerateHash(int uid) {
        if (!this.mDisplayHashThrottlingEnabled) {
            return true;
        }
        long currentTime = java.lang.System.currentTimeMillis();
        if (this.mLastRequestUid != uid) {
            this.mLastRequestUid = uid;
            this.mLastRequestTimeMs = currentTime;
            return true;
        }
        int mIntervalBetweenRequestsMs = getIntervalBetweenRequestMillis();
        if (currentTime - this.mLastRequestTimeMs < mIntervalBetweenRequestsMs) {
            return false;
        }
        this.mLastRequestTimeMs = currentTime;
        return true;
    }

    void generateDisplayHash(android.window.ScreenCapture.LayerCaptureArgs.Builder args, android.graphics.Rect boundsInWindow, java.lang.String hashAlgorithm, int uid, android.os.RemoteCallback callback) {
        if (!allowedToGenerateHash(uid)) {
            sendDisplayHashError(callback, -6);
            return;
        }
        java.util.Map<java.lang.String, android.service.displayhash.DisplayHashParams> displayHashAlgorithmsMap = getDisplayHashAlgorithms();
        android.service.displayhash.DisplayHashParams displayHashParams = displayHashAlgorithmsMap.get(hashAlgorithm);
        if (displayHashParams == null) {
            android.util.Slog.w(TAG, "Failed to generateDisplayHash. Invalid hashAlgorithm");
            sendDisplayHashError(callback, -5);
            return;
        }
        android.util.Size size = displayHashParams.getBufferSize();
        if (size != null && (size.getWidth() > 0 || size.getHeight() > 0)) {
            args.setFrameScale(size.getWidth() / boundsInWindow.width(), size.getHeight() / boundsInWindow.height());
        }
        args.setGrayscale(displayHashParams.isGrayscaleBuffer());
        android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotHardwareBuffer = android.window.ScreenCapture.captureLayers(args.build());
        if (screenshotHardwareBuffer == null || screenshotHardwareBuffer.getHardwareBuffer() == null) {
            android.util.Slog.w(TAG, "Failed to generate DisplayHash. Couldn't capture content");
            sendDisplayHashError(callback, -1);
        } else {
            generateDisplayHash(screenshotHardwareBuffer.getHardwareBuffer(), boundsInWindow, hashAlgorithm, callback);
        }
    }

    private java.util.Map<java.lang.String, android.service.displayhash.DisplayHashParams> getDisplayHashAlgorithms() {
        synchronized (this.mDisplayHashAlgorithmsLock) {
            if (this.mDisplayHashAlgorithms != null) {
                return this.mDisplayHashAlgorithms;
            }
            com.android.server.wm.DisplayHashController.SyncCommand syncCommand = new com.android.server.wm.DisplayHashController.SyncCommand();
            android.os.Bundle results = syncCommand.run(new java.util.function.BiConsumer() { // from class: com.android.server.wm.DisplayHashController$$ExternalSyntheticLambda3
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.wm.DisplayHashController.lambda$getDisplayHashAlgorithms$2((android.service.displayhash.IDisplayHashingService) obj, (android.os.RemoteCallback) obj2);
                }
            });
            this.mDisplayHashAlgorithms = new java.util.HashMap(results.size());
            for (java.lang.String key : results.keySet()) {
                this.mDisplayHashAlgorithms.put(key, (android.service.displayhash.DisplayHashParams) results.getParcelable(key));
            }
            return this.mDisplayHashAlgorithms;
        }
    }

    static /* synthetic */ void lambda$getDisplayHashAlgorithms$2(android.service.displayhash.IDisplayHashingService service, android.os.RemoteCallback remoteCallback) {
        try {
            service.getDisplayHashAlgorithms(remoteCallback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to invoke getDisplayHashAlgorithms command", e);
        }
    }

    void sendDisplayHashError(android.os.RemoteCallback callback, int errorCode) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putInt("DISPLAY_HASH_ERROR_CODE", errorCode);
        callback.sendResult(bundle);
    }

    void calculateDisplayHashBoundsLocked(com.android.server.wm.WindowState win, android.graphics.Rect boundsInWindow, android.graphics.Rect outBounds) {
        outBounds.set(boundsInWindow);
        com.android.server.wm.DisplayContent displayContent = win.getDisplayContent();
        if (displayContent == null) {
            return;
        }
        android.graphics.Rect windowBounds = new android.graphics.Rect();
        win.getBounds(windowBounds);
        windowBounds.offsetTo(0, 0);
        outBounds.intersectUnchecked(windowBounds);
        if (outBounds.isEmpty()) {
            return;
        }
        win.getTransformationMatrix(this.mTmpFloat9, this.mTmpMatrix);
        this.mTmpRectF.set(outBounds);
        this.mTmpMatrix.mapRect(this.mTmpRectF, this.mTmpRectF);
        outBounds.set((int) this.mTmpRectF.left, (int) this.mTmpRectF.top, (int) this.mTmpRectF.right, (int) this.mTmpRectF.bottom);
        android.view.MagnificationSpec magSpec = displayContent.getMagnificationSpec();
        if (magSpec != null) {
            outBounds.scale(magSpec.scale);
            outBounds.offset((int) magSpec.offsetX, (int) magSpec.offsetY);
        }
        if (outBounds.isEmpty()) {
            return;
        }
        android.graphics.Rect displayBounds = displayContent.getBounds();
        outBounds.intersectUnchecked(displayBounds);
    }

    private int getIntervalBetweenRequestMillis() {
        synchronized (this.mIntervalBetweenRequestsLock) {
            if (this.mIntervalBetweenRequestMillis != -1) {
                return this.mIntervalBetweenRequestMillis;
            }
            com.android.server.wm.DisplayHashController.SyncCommand syncCommand = new com.android.server.wm.DisplayHashController.SyncCommand();
            android.os.Bundle results = syncCommand.run(new java.util.function.BiConsumer() { // from class: com.android.server.wm.DisplayHashController$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.wm.DisplayHashController.lambda$getIntervalBetweenRequestMillis$3((android.service.displayhash.IDisplayHashingService) obj, (android.os.RemoteCallback) obj2);
                }
            });
            this.mIntervalBetweenRequestMillis = results.getInt("android.service.displayhash.extra.INTERVAL_BETWEEN_REQUESTS", 0);
            return this.mIntervalBetweenRequestMillis;
        }
    }

    static /* synthetic */ void lambda$getIntervalBetweenRequestMillis$3(android.service.displayhash.IDisplayHashingService service, android.os.RemoteCallback remoteCallback) {
        try {
            service.getIntervalBetweenRequestsMillis(remoteCallback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to invoke getDisplayHashAlgorithms command", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectAndRun(com.android.server.wm.DisplayHashController.Command command) {
        android.content.ComponentName component;
        synchronized (this.mServiceConnectionLock) {
            this.mHandler.resetTimeoutMessage();
            if (this.mServiceConnection == null && (component = getServiceComponentName()) != null) {
                android.content.Intent intent = new android.content.Intent();
                intent.setComponent(component);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    this.mServiceConnection = new com.android.server.wm.DisplayHashController.DisplayHashingServiceConnection();
                    this.mContext.bindService(intent, this.mServiceConnection, 1);
                    android.os.Binder.restoreCallingIdentity(token);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            }
            if (this.mServiceConnection != null) {
                this.mServiceConnection.runCommandLocked(command);
            }
        }
    }

    private android.content.pm.ServiceInfo getServiceInfo() {
        java.lang.String packageName = this.mContext.getPackageManager().getServicesSystemSharedLibraryPackageName();
        if (packageName == null) {
            android.util.Slog.w(TAG, "no external services package!");
            return null;
        }
        android.content.Intent intent = new android.content.Intent("android.service.displayhash.DisplayHashingService");
        intent.setPackage(packageName);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.ResolveInfo resolveInfo = this.mContext.getPackageManager().resolveService(intent, 132);
            if (resolveInfo == null || resolveInfo.serviceInfo == null) {
                android.util.Slog.w(TAG, "No valid components found.");
                return null;
            }
            return resolveInfo.serviceInfo;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private android.content.ComponentName getServiceComponentName() {
        android.content.pm.ServiceInfo serviceInfo = getServiceInfo();
        if (serviceInfo == null) {
            return null;
        }
        android.content.ComponentName name = new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
        if (!"android.permission.BIND_DISPLAY_HASHING_SERVICE".equals(serviceInfo.permission)) {
            android.util.Slog.w(TAG, name.flattenToShortString() + " requires permission android.permission.BIND_DISPLAY_HASHING_SERVICE");
            return null;
        }
        return name;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SyncCommand {
        private static final int WAIT_TIME_S = 5;
        private final java.util.concurrent.CountDownLatch mCountDownLatch;
        private android.os.Bundle mResult;

        private SyncCommand() {
            this.mCountDownLatch = new java.util.concurrent.CountDownLatch(1);
        }

        public android.os.Bundle run(final java.util.function.BiConsumer<android.service.displayhash.IDisplayHashingService, android.os.RemoteCallback> func) {
            com.android.server.wm.DisplayHashController.this.connectAndRun(new com.android.server.wm.DisplayHashController.Command() { // from class: com.android.server.wm.DisplayHashController$SyncCommand$$ExternalSyntheticLambda1
                @Override // com.android.server.wm.DisplayHashController.Command
                public final void run(android.service.displayhash.IDisplayHashingService iDisplayHashingService) throws android.os.RemoteException {
                    this.f$0.lambda$run$1(func, iDisplayHashingService);
                }
            });
            try {
                this.mCountDownLatch.await(5L, java.util.concurrent.TimeUnit.SECONDS);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.wm.DisplayHashController.TAG, "Failed to wait for command", e);
            }
            return this.mResult;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(java.util.function.BiConsumer func, android.service.displayhash.IDisplayHashingService service) throws android.os.RemoteException {
            android.os.RemoteCallback callback = new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.wm.DisplayHashController$SyncCommand$$ExternalSyntheticLambda0
                public final void onResult(android.os.Bundle bundle) {
                    this.f$0.lambda$run$0(bundle);
                }
            });
            func.accept(service, callback);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(android.os.Bundle result) {
            this.mResult = result;
            this.mCountDownLatch.countDown();
        }
    }

    private class DisplayHashingServiceConnection implements android.content.ServiceConnection {
        private java.util.ArrayList<com.android.server.wm.DisplayHashController.Command> mQueuedCommands;
        private android.service.displayhash.IDisplayHashingService mRemoteService;

        private DisplayHashingServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized (com.android.server.wm.DisplayHashController.this.mServiceConnectionLock) {
                this.mRemoteService = android.service.displayhash.IDisplayHashingService.Stub.asInterface(service);
                if (this.mQueuedCommands != null) {
                    int size = this.mQueuedCommands.size();
                    for (int i = 0; i < size; i++) {
                        com.android.server.wm.DisplayHashController.Command queuedCommand = this.mQueuedCommands.get(i);
                        try {
                            queuedCommand.run(this.mRemoteService);
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.w(com.android.server.wm.DisplayHashController.TAG, "exception calling " + name + ": " + e);
                        }
                    }
                    this.mQueuedCommands = null;
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            synchronized (com.android.server.wm.DisplayHashController.this.mServiceConnectionLock) {
                this.mRemoteService = null;
            }
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(android.content.ComponentName name) {
            synchronized (com.android.server.wm.DisplayHashController.this.mServiceConnectionLock) {
                this.mRemoteService = null;
            }
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(android.content.ComponentName name) {
            synchronized (com.android.server.wm.DisplayHashController.this.mServiceConnectionLock) {
                this.mRemoteService = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void runCommandLocked(com.android.server.wm.DisplayHashController.Command command) {
            if (this.mRemoteService == null) {
                if (this.mQueuedCommands == null) {
                    this.mQueuedCommands = new java.util.ArrayList<>(1);
                }
                this.mQueuedCommands.add(command);
            } else {
                try {
                    command.run(this.mRemoteService);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.wm.DisplayHashController.TAG, "exception calling service: " + e);
                }
            }
        }
    }

    private class Handler extends android.os.Handler {
        static final int MSG_SERVICE_SHUTDOWN_TIMEOUT = 1;
        static final long SERVICE_SHUTDOWN_TIMEOUT_MILLIS = 10000;

        Handler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                synchronized (com.android.server.wm.DisplayHashController.this.mServiceConnectionLock) {
                    if (com.android.server.wm.DisplayHashController.this.mServiceConnection != null) {
                        com.android.server.wm.DisplayHashController.this.mContext.unbindService(com.android.server.wm.DisplayHashController.this.mServiceConnection);
                        com.android.server.wm.DisplayHashController.this.mServiceConnection = null;
                    }
                }
            }
        }

        void resetTimeoutMessage() {
            removeMessages(1);
            sendEmptyMessageDelayed(1, 10000L);
        }
    }
}
