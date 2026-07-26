package com.android.server.biometrics.log;

/* JADX INFO: loaded from: classes.dex */
public final class BiometricContextProvider implements com.android.server.biometrics.log.BiometricContext {
    private static final int SESSION_TYPES = 3;
    private static final java.lang.String TAG = "BiometricContextProvider";
    private static com.android.server.biometrics.log.BiometricContextProvider sInstance;
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator mAuthSessionCoordinator;
    private final android.os.Handler mHandler;
    private final android.view.WindowManager mWindowManager;
    private final java.util.Map<com.android.server.biometrics.log.OperationContextExt, java.util.function.Consumer<android.hardware.biometrics.common.OperationContext>> mSubscribers = new java.util.concurrent.ConcurrentHashMap();
    private final java.util.Map<java.lang.Integer, com.android.server.biometrics.log.BiometricContextSessionInfo> mSession = new java.util.concurrent.ConcurrentHashMap();
    private int mDockState = 0;
    private int mFoldState = 0;
    private int mDisplayState = 0;
    private boolean mIsHardwareIgnoringTouches = false;
    final android.content.BroadcastReceiver mDockStateReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.biometrics.log.BiometricContextProvider.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.biometrics.log.BiometricContextProvider.this.mDockState = intent.getIntExtra("android.intent.extra.DOCK_STATE", 0);
        }
    };

    static com.android.server.biometrics.log.BiometricContextProvider defaultProvider(android.content.Context context) {
        synchronized (com.android.server.biometrics.log.BiometricContextProvider.class) {
            if (sInstance == null) {
                try {
                    sInstance = new com.android.server.biometrics.log.BiometricContextProvider(context, (android.view.WindowManager) context.getSystemService("window"), com.android.internal.statusbar.IStatusBarService.Stub.asInterface(android.os.ServiceManager.getServiceOrThrow("statusbar")), null, new com.android.server.biometrics.sensors.AuthSessionCoordinator());
                } catch (android.os.ServiceManager.ServiceNotFoundException e) {
                    throw new java.lang.IllegalStateException("Failed to find required service", e);
                }
            }
        }
        return sInstance;
    }

    public BiometricContextProvider(android.content.Context context, android.view.WindowManager windowManager, com.android.internal.statusbar.IStatusBarService service, android.os.Handler handler, com.android.server.biometrics.sensors.AuthSessionCoordinator authSessionCoordinator) {
        this.mWindowManager = windowManager;
        this.mAuthSessionCoordinator = authSessionCoordinator;
        this.mHandler = handler;
        subscribeBiometricContextListener(service);
        subscribeDockState(context);
    }

    private void subscribeBiometricContextListener(com.android.internal.statusbar.IStatusBarService service) {
        try {
            service.setBiometicContextListener(new android.hardware.biometrics.IBiometricContextListener.Stub() { // from class: com.android.server.biometrics.log.BiometricContextProvider.2
                public void onFoldChanged(int foldState) {
                    if (com.android.server.biometrics.log.BiometricContextProvider.this.mFoldState != foldState) {
                        com.android.server.biometrics.log.BiometricContextProvider.this.mFoldState = foldState;
                        com.android.server.biometrics.log.BiometricContextProvider.this.notifyChanged();
                    }
                }

                public void onDisplayStateChanged(int displayState) {
                    if (displayState != com.android.server.biometrics.log.BiometricContextProvider.this.mDisplayState) {
                        com.android.server.biometrics.log.BiometricContextProvider.this.mDisplayState = displayState;
                        com.android.server.biometrics.log.BiometricContextProvider.this.notifyChanged();
                    }
                }

                public void onHardwareIgnoreTouchesChanged(boolean shouldIgnore) {
                    if (com.android.server.biometrics.log.BiometricContextProvider.this.mIsHardwareIgnoringTouches != shouldIgnore) {
                        com.android.server.biometrics.log.BiometricContextProvider.this.mIsHardwareIgnoringTouches = shouldIgnore;
                        com.android.server.biometrics.log.BiometricContextProvider.this.notifyChanged();
                    }
                }
            });
            service.registerSessionListener(3, new com.android.internal.statusbar.ISessionListener.Stub() { // from class: com.android.server.biometrics.log.BiometricContextProvider.3
                public void onSessionStarted(int sessionType, com.android.internal.logging.InstanceId instance) {
                    com.android.server.biometrics.log.BiometricContextProvider.this.mSession.put(java.lang.Integer.valueOf(sessionType), new com.android.server.biometrics.log.BiometricContextSessionInfo(instance));
                }

                public void onSessionEnded(int sessionType, com.android.internal.logging.InstanceId instance) {
                    com.android.server.biometrics.log.BiometricContextSessionInfo info = (com.android.server.biometrics.log.BiometricContextSessionInfo) com.android.server.biometrics.log.BiometricContextProvider.this.mSession.remove(java.lang.Integer.valueOf(sessionType));
                    if (info != null && instance != null && info.getId() != instance.getId()) {
                        android.util.Slog.w(com.android.server.biometrics.log.BiometricContextProvider.TAG, "session id mismatch");
                    }
                }
            });
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to register biometric context listener", e);
        }
    }

    private void subscribeDockState(android.content.Context context) {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.DOCK_EVENT");
        context.registerReceiver(this.mDockStateReceiver, filter);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public com.android.server.biometrics.log.OperationContextExt updateContext(com.android.server.biometrics.log.OperationContextExt operationContext, boolean isCryptoOperation) {
        return operationContext.update(this, isCryptoOperation);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public com.android.server.biometrics.log.BiometricContextSessionInfo getKeyguardEntrySessionInfo() {
        return this.mSession.get(1);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public com.android.server.biometrics.log.BiometricContextSessionInfo getBiometricPromptSessionInfo() {
        return this.mSession.get(2);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public boolean isAod() {
        return this.mDisplayState == 4;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public boolean isAwake() {
        switch (this.mDisplayState) {
            case 0:
            case 1:
            case 3:
                return true;
            case 2:
            default:
                return false;
        }
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public boolean isDisplayOn() {
        return this.mWindowManager.getDefaultDisplay().getState() == 2;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public int getDockedState() {
        return this.mDockState;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public int getFoldState() {
        return this.mFoldState;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public int getCurrentRotation() {
        return this.mWindowManager.getDefaultDisplay().getRotation();
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public int getDisplayState() {
        return this.mDisplayState;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public boolean isHardwareIgnoringTouches() {
        return this.mIsHardwareIgnoringTouches;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public void subscribe(com.android.server.biometrics.log.OperationContextExt context, java.util.function.Consumer<android.hardware.biometrics.common.OperationContext> startHalConsumer, java.util.function.Consumer<android.hardware.biometrics.common.OperationContext> updateContextConsumer, android.hardware.biometrics.AuthenticateOptions options) {
        this.mSubscribers.put(updateContext(context, context.isCrypto()), updateContextConsumer);
        if (options != null) {
            startHalConsumer.accept(context.toAidlContext(options));
        } else {
            startHalConsumer.accept(context.toAidlContext());
        }
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public void unsubscribe(com.android.server.biometrics.log.OperationContextExt context) {
        this.mSubscribers.remove(context);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public com.android.server.biometrics.sensors.AuthSessionCoordinator getAuthSessionCoordinator() {
        return this.mAuthSessionCoordinator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyChanged() {
        if (this.mHandler != null) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.log.BiometricContextProvider$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.notifySubscribers();
                }
            });
        } else {
            notifySubscribers();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySubscribers() {
        this.mSubscribers.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.biometrics.log.BiometricContextProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$notifySubscribers$0((com.android.server.biometrics.log.OperationContextExt) obj, (java.util.function.Consumer) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySubscribers$0(com.android.server.biometrics.log.OperationContextExt context, java.util.function.Consumer consumer) {
        consumer.accept(context.update(this, context.isCrypto()).toAidlContext());
    }

    public java.lang.String toString() {
        return "[keyguard session: " + getKeyguardEntrySessionInfo() + ", bp session: " + getBiometricPromptSessionInfo() + ", displayState: " + getDisplayState() + ", isAwake: " + isAwake() + ", isHardwareIgnoring: " + isHardwareIgnoringTouches() + ", isDisplayOn: " + isDisplayOn() + ", dock: " + getDockedState() + ", rotation: " + getCurrentRotation() + ", foldState: " + this.mFoldState + "]";
    }
}
