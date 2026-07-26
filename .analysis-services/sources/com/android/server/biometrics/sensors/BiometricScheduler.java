package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class BiometricScheduler<T, U> {
    protected static final int LOG_NUM_RECENT_OPERATIONS = 50;
    public static final int SENSOR_TYPE_FACE = 1;
    public static final int SENSOR_TYPE_FP_OTHER = 3;
    public static final int SENSOR_TYPE_UDFPS = 2;
    public static final int SENSOR_TYPE_UNKNOWN = 0;
    private static final java.lang.String TAG = "BiometricScheduler";
    private com.android.server.biometrics.sensors.IBiometricSchedulerExt mBiometricSchedulerExt;
    private final android.hardware.biometrics.IBiometricService mBiometricService;
    private final java.util.ArrayDeque<com.android.server.biometrics.sensors.BiometricScheduler.CrashState> mCrashStates;
    com.android.server.biometrics.sensors.BiometricSchedulerOperation mCurrentOperation;
    private java.util.function.Supplier<java.lang.Integer> mCurrentUserRetriever;
    private final com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher mGestureAvailabilityDispatcher;
    protected final android.os.Handler mHandler;
    private final com.android.server.biometrics.sensors.ClientMonitorCallback mInternalCallback;
    private com.android.server.biometrics.sensors.IBiometricSchedulerWrapper mOplusBiometricSchedulerWrapper;
    final java.util.Deque<com.android.server.biometrics.sensors.BiometricSchedulerOperation> mPendingOperations;
    private final java.util.List<java.lang.Integer> mRecentOperations;
    private final int mRecentOperationsLimit;
    private final int mSensorType;
    private com.android.server.biometrics.sensors.StopUserClient<U> mStopUserClient;
    private int mTotalOperationsHandled;
    private com.android.server.biometrics.sensors.UserSwitchProvider<T, U> mUserSwitchProvider;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SensorType {
    }

    private static final class CrashState {
        static final int NUM_ENTRIES = 10;
        final java.lang.String currentOperation;
        final java.util.List<java.lang.String> pendingOperations;
        final java.lang.String timestamp;

        CrashState(java.lang.String timestamp, java.lang.String currentOperation, java.util.List<java.lang.String> pendingOperations) {
            this.timestamp = timestamp;
            this.currentOperation = currentOperation;
            this.pendingOperations = pendingOperations;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(this.timestamp).append(": ");
            sb.append("Current Operation: {").append(this.currentOperation).append("}");
            sb.append(", Pending Operations(").append(this.pendingOperations.size()).append(")");
            if (!this.pendingOperations.isEmpty()) {
                sb.append(": ");
            }
            for (int i = 0; i < this.pendingOperations.size(); i++) {
                sb.append(this.pendingOperations.get(i));
                if (i < this.pendingOperations.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class UserSwitchClientCallback implements com.android.server.biometrics.sensors.ClientMonitorCallback {
        private final com.android.server.biometrics.sensors.BaseClientMonitor mOwner;

        UserSwitchClientCallback(com.android.server.biometrics.sensors.BaseClientMonitor owner) {
            this.mOwner = owner;
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientFinished(final com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, final boolean success) {
            com.android.server.biometrics.sensors.BiometricScheduler.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.BiometricScheduler$UserSwitchClientCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onClientFinished$0(clientMonitor, success);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClientFinished$0(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
            android.util.Slog.d(com.android.server.biometrics.sensors.BiometricScheduler.TAG, "[Client finished] " + clientMonitor + ", success: " + success);
            if (clientMonitor instanceof com.android.server.biometrics.sensors.StopUserClient) {
                if (!success) {
                    android.util.Slog.w(com.android.server.biometrics.sensors.BiometricScheduler.TAG, "StopUserClient failed(), is the HAL stuck? Clearing mStopUserClient");
                }
                com.android.server.biometrics.sensors.BiometricScheduler.this.mStopUserClient = null;
            }
            if (com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation != null && com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation.isFor(this.mOwner)) {
                com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation = null;
            } else {
                android.util.Slog.w(com.android.server.biometrics.sensors.BiometricScheduler.TAG, "operation is already null or different (reset?): " + com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation);
            }
            com.android.server.biometrics.sensors.BiometricScheduler.this.checkCurrentUserAndStartNextOperation();
        }
    }

    /* JADX INFO: renamed from: com.android.server.biometrics.sensors.BiometricScheduler$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.biometrics.sensors.ClientMonitorCallback {
        AnonymousClass1() {
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
            android.util.Slog.d(com.android.server.biometrics.sensors.BiometricScheduler.TAG, "[Started] " + clientMonitor);
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientFinished(final com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, final boolean success) {
            com.android.server.biometrics.sensors.BiometricScheduler.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.BiometricScheduler$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onClientFinished$0(clientMonitor, success);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClientFinished$0(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
            if (com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation == null) {
                android.util.Slog.e(com.android.server.biometrics.sensors.BiometricScheduler.TAG, "[Finishing] " + clientMonitor + " but current operation is null, success: " + success + ", possible lifecycle bug in clientMonitor implementation?");
                return;
            }
            if (!com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation.isFor(clientMonitor)) {
                android.util.Slog.e(com.android.server.biometrics.sensors.BiometricScheduler.TAG, "[Ignoring Finish] " + clientMonitor + " does not match current: " + com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation);
                return;
            }
            android.util.Slog.d(com.android.server.biometrics.sensors.BiometricScheduler.TAG, "[Finishing] " + clientMonitor + ", success: " + success);
            if (com.android.server.biometrics.sensors.BiometricScheduler.this.mGestureAvailabilityDispatcher != null) {
                com.android.server.biometrics.sensors.BiometricScheduler.this.mGestureAvailabilityDispatcher.markSensorActive(com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation.getSensorId(), false);
            }
            if (com.android.server.biometrics.sensors.BiometricScheduler.this.mRecentOperations.size() >= com.android.server.biometrics.sensors.BiometricScheduler.this.mRecentOperationsLimit) {
                com.android.server.biometrics.sensors.BiometricScheduler.this.mRecentOperations.remove(0);
            }
            com.android.server.biometrics.sensors.BiometricScheduler.this.mRecentOperations.add(java.lang.Integer.valueOf(com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation.getProtoEnum()));
            com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation = null;
            com.android.server.biometrics.sensors.BiometricScheduler.this.mTotalOperationsHandled++;
            com.android.server.biometrics.sensors.BiometricScheduler.this.checkCurrentUserAndStartNextOperation();
        }
    }

    public BiometricScheduler(android.os.Handler handler, int sensorType, com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, android.hardware.biometrics.IBiometricService biometricService, int recentOperationsLimit) {
        this.mOplusBiometricSchedulerWrapper = new com.android.server.biometrics.sensors.BiometricScheduler.OplusBiometricSchedulerWrapper();
        this.mBiometricSchedulerExt = (com.android.server.biometrics.sensors.IBiometricSchedulerExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.IBiometricSchedulerExt.class).base(this).create();
        this.mInternalCallback = new com.android.server.biometrics.sensors.BiometricScheduler.AnonymousClass1();
        this.mHandler = handler;
        this.mSensorType = sensorType;
        this.mGestureAvailabilityDispatcher = gestureAvailabilityDispatcher;
        this.mPendingOperations = new java.util.ArrayDeque();
        this.mBiometricService = biometricService;
        this.mCrashStates = new java.util.ArrayDeque<>();
        this.mRecentOperationsLimit = recentOperationsLimit;
        this.mRecentOperations = new java.util.ArrayList();
    }

    public BiometricScheduler(android.os.Handler handler, int sensorType, com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, android.hardware.biometrics.IBiometricService biometricService, int recentOperationsLimit, java.util.function.Supplier<java.lang.Integer> currentUserRetriever, com.android.server.biometrics.sensors.UserSwitchProvider<T, U> userSwitchProvider) {
        this.mOplusBiometricSchedulerWrapper = new com.android.server.biometrics.sensors.BiometricScheduler.OplusBiometricSchedulerWrapper();
        this.mBiometricSchedulerExt = (com.android.server.biometrics.sensors.IBiometricSchedulerExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.IBiometricSchedulerExt.class).base(this).create();
        this.mInternalCallback = new com.android.server.biometrics.sensors.BiometricScheduler.AnonymousClass1();
        this.mHandler = handler;
        this.mSensorType = sensorType;
        this.mGestureAvailabilityDispatcher = gestureAvailabilityDispatcher;
        this.mPendingOperations = new java.util.ArrayDeque();
        this.mBiometricService = biometricService;
        this.mCrashStates = new java.util.ArrayDeque<>();
        this.mRecentOperationsLimit = recentOperationsLimit;
        this.mRecentOperations = new java.util.ArrayList();
        this.mCurrentUserRetriever = currentUserRetriever;
        this.mUserSwitchProvider = userSwitchProvider;
    }

    public BiometricScheduler(android.os.Handler handler, int sensorType, com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, java.util.function.Supplier<java.lang.Integer> currentUserRetriever, com.android.server.biometrics.sensors.UserSwitchProvider<T, U> userSwitchProvider) {
        this(handler, sensorType, gestureAvailabilityDispatcher, android.hardware.biometrics.IBiometricService.Stub.asInterface(android.os.ServiceManager.getService("biometric")), 50, currentUserRetriever, userSwitchProvider);
    }

    public BiometricScheduler(int sensorType, com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher) {
        this(new android.os.Handler(android.os.Looper.getMainLooper()), sensorType, gestureAvailabilityDispatcher, android.hardware.biometrics.IBiometricService.Stub.asInterface(android.os.ServiceManager.getService("biometric")), 50);
    }

    public static int sensorTypeFromFingerprintProperties(android.hardware.fingerprint.FingerprintSensorPropertiesInternal props) {
        if (props.isAnyUdfpsType()) {
            return 2;
        }
        return 3;
    }

    public com.android.server.biometrics.sensors.ClientMonitorCallback getInternalCallback() {
        return this.mInternalCallback;
    }

    protected void checkCurrentUserAndStartNextOperation() {
        if (this.mCurrentOperation != null) {
            android.util.Slog.v(TAG, "Not idle, current operation: " + this.mCurrentOperation);
            return;
        }
        if (this.mPendingOperations.isEmpty()) {
            android.util.Slog.d(TAG, "No operations, returning to idle");
            return;
        }
        int currentUserId = this.mCurrentUserRetriever.get().intValue();
        int nextUserId = this.mPendingOperations.getFirst().getTargetUserId();
        if (nextUserId == currentUserId || this.mPendingOperations.getFirst().isStartUserOperation()) {
            startNextOperationIfIdle();
            return;
        }
        if (currentUserId == -10000 && this.mUserSwitchProvider != null) {
            com.android.server.biometrics.sensors.BaseClientMonitor startClient = this.mUserSwitchProvider.getStartUserClient(nextUserId);
            com.android.server.biometrics.sensors.BiometricScheduler<T, U>.UserSwitchClientCallback finishedCallback = new com.android.server.biometrics.sensors.BiometricScheduler.UserSwitchClientCallback(startClient);
            android.util.Slog.d(TAG, "[Starting User] " + startClient);
            this.mCurrentOperation = new com.android.server.biometrics.sensors.BiometricSchedulerOperation(startClient, finishedCallback, 2);
            startClient.start(finishedCallback);
            return;
        }
        if (this.mUserSwitchProvider != null) {
            if (this.mStopUserClient != null) {
                android.util.Slog.d(TAG, "[Waiting for StopUser] " + this.mStopUserClient);
                return;
            }
            this.mStopUserClient = this.mUserSwitchProvider.getStopUserClient(currentUserId);
            com.android.server.biometrics.sensors.BiometricScheduler<T, U>.UserSwitchClientCallback finishedCallback2 = new com.android.server.biometrics.sensors.BiometricScheduler.UserSwitchClientCallback(this.mStopUserClient);
            android.util.Slog.d(TAG, "[Stopping User] current: " + currentUserId + ", next: " + nextUserId + ". " + this.mStopUserClient);
            this.mCurrentOperation = new com.android.server.biometrics.sensors.BiometricSchedulerOperation(this.mStopUserClient, finishedCallback2, 2);
            this.mStopUserClient.start(finishedCallback2);
            return;
        }
        android.util.Slog.e(TAG, "Cannot start next operation.");
    }

    protected void startNextOperationIfIdle() {
        if (this.mCurrentOperation != null) {
            android.util.Slog.v(TAG, "Not idle, current operation: " + this.mCurrentOperation);
            return;
        }
        if (this.mPendingOperations.isEmpty()) {
            android.util.Slog.d(TAG, "No operations, returning to idle");
            return;
        }
        this.mCurrentOperation = this.mPendingOperations.poll();
        android.util.Slog.d(TAG, "[Polled] " + this.mCurrentOperation);
        if (this.mCurrentOperation.isMarkedCanceling()) {
            android.util.Slog.d(TAG, "[Now Cancelling] " + this.mCurrentOperation);
            this.mCurrentOperation.cancel(this.mHandler, this.mInternalCallback);
            return;
        }
        if (this.mCurrentOperation.isAcquisitionOperation()) {
            com.android.server.biometrics.sensors.AcquisitionClient client = (com.android.server.biometrics.sensors.AcquisitionClient) this.mCurrentOperation.getClientMonitor();
            if (client.isAlreadyCancelled()) {
                this.mCurrentOperation.cancel(this.mHandler, this.mInternalCallback);
                return;
            }
        }
        if (this.mGestureAvailabilityDispatcher != null && this.mCurrentOperation.isAcquisitionOperation()) {
            this.mGestureAvailabilityDispatcher.markSensorActive(this.mCurrentOperation.getSensorId(), true);
        }
        int cookie = this.mCurrentOperation.isReadyToStart(this.mInternalCallback);
        if (cookie == 0) {
            if (!this.mCurrentOperation.start(this.mInternalCallback)) {
                int pendingOperationsLength = this.mPendingOperations.size();
                com.android.server.biometrics.sensors.BiometricSchedulerOperation lastOperation = this.mPendingOperations.peekLast();
                android.util.Slog.e(TAG, "[Unable To Start] " + this.mCurrentOperation + ". Last pending operation: " + lastOperation);
                for (int i = 0; i < pendingOperationsLength; i++) {
                    com.android.server.biometrics.sensors.BiometricSchedulerOperation operation = this.mPendingOperations.pollFirst();
                    if (operation != null) {
                        android.util.Slog.w(TAG, "[Aborting Operation] " + operation);
                        operation.abort();
                    } else {
                        android.util.Slog.e(TAG, "Null operation, index: " + i + ", expected length: " + pendingOperationsLength);
                    }
                }
                this.mCurrentOperation = null;
                checkCurrentUserAndStartNextOperation();
                return;
            }
            return;
        }
        try {
            this.mBiometricService.onReadyForAuthentication(this.mCurrentOperation.getClientMonitor().getRequestId(), cookie);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when contacting BiometricService", e);
        }
        android.util.Slog.d(TAG, "Waiting for cookie before starting: " + this.mCurrentOperation);
    }

    public void startPreparedClient(int cookie) {
        if (this.mCurrentOperation == null) {
            android.util.Slog.e(TAG, "Current operation is null");
        } else {
            if (this.mCurrentOperation.startWithCookie(this.mInternalCallback, cookie)) {
                android.util.Slog.d(TAG, "[Started] Prepared client: " + this.mCurrentOperation);
                return;
            }
            android.util.Slog.e(TAG, "[Unable To Start] Prepared client: " + this.mCurrentOperation);
            this.mCurrentOperation = null;
            checkCurrentUserAndStartNextOperation();
        }
    }

    public void scheduleClientMonitor(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
        scheduleClientMonitor(clientMonitor, null);
    }

    public void scheduleClientMonitor(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, com.android.server.biometrics.sensors.ClientMonitorCallback clientCallback) {
        if (clientMonitor.interruptsPrecedingClients()) {
            for (com.android.server.biometrics.sensors.BiometricSchedulerOperation operation : this.mPendingOperations) {
                if (operation.markCanceling()) {
                    android.util.Slog.d(TAG, "New client, marking pending op as canceling: " + operation);
                }
            }
        }
        this.mPendingOperations.add(new com.android.server.biometrics.sensors.BiometricSchedulerOperation(clientMonitor, clientCallback));
        android.util.Slog.d(TAG, "[Added] " + clientMonitor + ", new queue size: " + this.mPendingOperations.size() + ", interruptable: " + clientMonitor.interruptsPrecedingClients());
        if (clientMonitor.interruptsPrecedingClients() && this.mCurrentOperation != null && this.mCurrentOperation.isInterruptable() && (this.mCurrentOperation.isStarted() || this.mCurrentOperation.isWaitingForCookie())) {
            if (this.mCurrentOperation.getClientMonitor().getClass().getName().contains("FingerprintAuthenticationClient") && clientMonitor.getClass().getName().contains("FingerprintResetLockoutClient") && "com.android.settings".equals(this.mCurrentOperation.getClientMonitor().getOwnerString()) && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(clientMonitor.getOwnerString())) {
                int userId = this.mCurrentOperation.getClientMonitor().getTargetUserId();
                android.content.Context mContext = this.mCurrentOperation.getClientMonitor().getContext();
                int mSensorId = this.mCurrentOperation.getClientMonitor().getSensorId();
                java.util.List<android.hardware.fingerprint.Fingerprint> mFingers = com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getLegacyInstance(mSensorId).getBiometricsForUser(mContext, userId);
                if (!mFingers.isEmpty() && mFingers.size() == 5) {
                    android.util.Slog.d(TAG, "[startNextOperationIfIdle] mCurrentOperation: " + this.mCurrentOperation);
                    startNextOperationIfIdle();
                    return;
                }
            }
            if (this.mCurrentOperation.getClientMonitor().getClass().getName().contains(".FingerprintEnrollClient") && clientMonitor.getClass().getName().contains(".FingerprintResetLockoutClient") && "com.android.settings".equals(this.mCurrentOperation.getClientMonitor().getOwnerString()) && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(clientMonitor.getOwnerString())) {
                android.util.Slog.d(TAG, "[startNextOperationIfIdle] mCurrentOperation: " + this.mCurrentOperation);
                startNextOperationIfIdle();
                return;
            } else {
                android.util.Slog.d(TAG, "[Cancelling Interruptable]: " + this.mCurrentOperation);
                this.mCurrentOperation.cancel(this.mHandler, this.mInternalCallback);
                return;
            }
        }
        checkCurrentUserAndStartNextOperation();
    }

    public void cancelEnrollment(android.os.IBinder token, long requestId) {
        android.util.Slog.d(TAG, "cancelEnrollment, requestId: " + requestId);
        if (this.mCurrentOperation != null && canCancelEnrollOperation(this.mCurrentOperation, token, requestId)) {
            android.util.Slog.d(TAG, "Cancelling enrollment op: " + this.mCurrentOperation);
            this.mCurrentOperation.cancel(this.mHandler, this.mInternalCallback);
            return;
        }
        for (com.android.server.biometrics.sensors.BiometricSchedulerOperation operation : this.mPendingOperations) {
            if (canCancelEnrollOperation(operation, token, requestId)) {
                android.util.Slog.d(TAG, "Cancelling pending enrollment op: " + operation);
                operation.markCanceling();
            }
        }
    }

    public void cancelAuthenticationOrDetection(android.os.IBinder token, long requestId) {
        android.util.Slog.d(TAG, "cancelAuthenticationOrDetection, requestId: " + requestId);
        if (this.mCurrentOperation != null && canCancelAuthOperation(this.mCurrentOperation, token, requestId)) {
            android.util.Slog.d(TAG, "Cancelling auth/detect op: " + this.mCurrentOperation);
            this.mCurrentOperation.cancel(this.mHandler, this.mInternalCallback);
            return;
        }
        for (com.android.server.biometrics.sensors.BiometricSchedulerOperation operation : this.mPendingOperations) {
            if (canCancelAuthOperation(operation, token, requestId)) {
                android.util.Slog.d(TAG, "Cancelling pending auth/detect op: " + operation);
                operation.markCanceling();
            }
        }
    }

    private static boolean canCancelEnrollOperation(com.android.server.biometrics.sensors.BiometricSchedulerOperation operation, android.os.IBinder token, long requestId) {
        return operation.isEnrollOperation() && operation.isMatchingToken(token) && operation.isMatchingRequestId(requestId);
    }

    private static boolean canCancelAuthOperation(com.android.server.biometrics.sensors.BiometricSchedulerOperation operation, android.os.IBinder token, long requestId) {
        return operation.isAuthenticationOrDetectionOperation() && operation.isMatchingToken(token) && operation.isMatchingRequestId(requestId);
    }

    @java.lang.Deprecated
    public com.android.server.biometrics.sensors.BaseClientMonitor getCurrentClient() {
        if (this.mCurrentOperation != null) {
            return this.mCurrentOperation.getClientMonitor();
        }
        return null;
    }

    @java.lang.Deprecated
    public void getCurrentClientIfMatches(final long requestId, final java.util.function.Consumer<com.android.server.biometrics.sensors.BaseClientMonitor> clientMonitorConsumer) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.BiometricScheduler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getCurrentClientIfMatches$0(requestId, clientMonitorConsumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCurrentClientIfMatches$0(long requestId, java.util.function.Consumer clientMonitorConsumer) {
        if (this.mCurrentOperation != null && this.mCurrentOperation.isMatchingRequestId(requestId)) {
            clientMonitorConsumer.accept(this.mCurrentOperation.getClientMonitor());
        } else {
            clientMonitorConsumer.accept(null);
        }
    }

    public int getCurrentPendingCount() {
        return this.mPendingOperations.size();
    }

    public void recordCrashState() {
        if (this.mCrashStates.size() >= 10) {
            this.mCrashStates.removeFirst();
        }
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.US);
        java.lang.String timestamp = dateFormat.format(new java.util.Date(java.lang.System.currentTimeMillis()));
        java.util.List<java.lang.String> pendingOperations = new java.util.ArrayList<>();
        for (com.android.server.biometrics.sensors.BiometricSchedulerOperation operation : this.mPendingOperations) {
            pendingOperations.add(operation.toString());
        }
        com.android.server.biometrics.sensors.BiometricScheduler.CrashState crashState = new com.android.server.biometrics.sensors.BiometricScheduler.CrashState(timestamp, this.mCurrentOperation != null ? this.mCurrentOperation.toString() : null, pendingOperations);
        this.mCrashStates.add(crashState);
        android.util.Slog.e(TAG, "Recorded crash state: " + crashState.toString());
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("Dump of BiometricScheduler BiometricScheduler");
        pw.println("Type: " + this.mSensorType);
        pw.println("Current operation: " + this.mCurrentOperation);
        pw.println("Pending operations: " + this.mPendingOperations.size());
        for (com.android.server.biometrics.sensors.BiometricSchedulerOperation operation : this.mPendingOperations) {
            pw.println("Pending operation: " + operation);
        }
        for (com.android.server.biometrics.sensors.BiometricScheduler.CrashState crashState : this.mCrashStates) {
            pw.println("Crash State " + crashState);
        }
    }

    public byte[] dumpProtoState(boolean clearSchedulerBuffer) {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
        proto.write(1159641169921L, this.mCurrentOperation != null ? this.mCurrentOperation.getProtoEnum() : 0);
        proto.write(1120986464258L, this.mTotalOperationsHandled);
        if (!this.mRecentOperations.isEmpty()) {
            for (int i = 0; i < this.mRecentOperations.size(); i++) {
                proto.write(2259152797699L, this.mRecentOperations.get(i).intValue());
            }
        } else {
            proto.write(2259152797699L, 0);
        }
        proto.flush();
        if (clearSchedulerBuffer) {
            this.mRecentOperations.clear();
        }
        return proto.getBytes();
    }

    public void reset() {
        android.util.Slog.d(TAG, "Resetting scheduler");
        this.mPendingOperations.clear();
        this.mCurrentOperation = null;
    }

    private void clearScheduler() {
        if (this.mCurrentOperation == null) {
            return;
        }
        for (com.android.server.biometrics.sensors.BiometricSchedulerOperation pendingOperation : this.mPendingOperations) {
            android.util.Slog.d(TAG, "[Watchdog cancelling pending] " + pendingOperation.getClientMonitor());
            pendingOperation.markCancelingForWatchdog();
        }
        android.util.Slog.d(TAG, "[Watchdog cancelling current] " + this.mCurrentOperation.getClientMonitor());
        this.mCurrentOperation.cancel(this.mHandler, getInternalCallback());
    }

    public void startWatchdog() {
        final com.android.server.biometrics.sensors.BiometricSchedulerOperation operation = this.mCurrentOperation;
        if (operation == null) {
            android.util.Slog.e(TAG, "Current operation is null,no need to start watchdog");
        } else {
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.BiometricScheduler$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startWatchdog$1(operation);
                }
            }, 10000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startWatchdog$1(com.android.server.biometrics.sensors.BiometricSchedulerOperation operation) {
        if (operation == this.mCurrentOperation && !operation.isFinished()) {
            com.android.modules.expresslog.Counter.logIncrement("biometric.value_scheduler_watchdog_triggered_count");
            clearScheduler();
        }
    }

    public void onUserStopped() {
        if (this.mStopUserClient == null) {
            android.util.Slog.e(TAG, "Unexpected onUserStopped");
            return;
        }
        android.util.Slog.d(TAG, "[OnUserStopped]: " + this.mStopUserClient);
        this.mStopUserClient.onUserStopped();
        this.mStopUserClient = null;
    }

    public android.os.Handler getHandler() {
        return this.mHandler;
    }

    public com.android.server.biometrics.sensors.StopUserClient<?> getStopUserClient() {
        return this.mStopUserClient;
    }

    public com.android.server.biometrics.sensors.IBiometricSchedulerWrapper getWrapper() {
        return this.mOplusBiometricSchedulerWrapper;
    }

    private class OplusBiometricSchedulerWrapper implements com.android.server.biometrics.sensors.IBiometricSchedulerWrapper {
        private OplusBiometricSchedulerWrapper() {
        }

        @Override // com.android.server.biometrics.sensors.IBiometricSchedulerWrapper
        public com.android.server.biometrics.sensors.IBiometricSchedulerExt getExtImpl() {
            return com.android.server.biometrics.sensors.BiometricScheduler.this.mBiometricSchedulerExt;
        }

        @Override // com.android.server.biometrics.sensors.IBiometricSchedulerWrapper
        public com.android.server.biometrics.sensors.BiometricSchedulerOperation getCurrentOperationWrapper() {
            return com.android.server.biometrics.sensors.BiometricScheduler.this.mCurrentOperation;
        }

        @Override // com.android.server.biometrics.sensors.IBiometricSchedulerWrapper
        public java.util.Deque<com.android.server.biometrics.sensors.BiometricSchedulerOperation> getPendingOperationWrapper() {
            return com.android.server.biometrics.sensors.BiometricScheduler.this.mPendingOperations;
        }
    }
}
