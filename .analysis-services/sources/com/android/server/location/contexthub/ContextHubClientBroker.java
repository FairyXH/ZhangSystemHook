package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
public class ContextHubClientBroker extends android.hardware.location.IContextHubClient.Stub implements android.os.IBinder.DeathRecipient, android.app.AppOpsManager.OnOpChangedListener, android.app.PendingIntent.OnFinished {
    private static final int AUTHORIZATION_UNKNOWN = -1;
    private static final long CHANGE_ID_AUTH_STATE_DENIED = 181350407;
    private static final java.lang.String RECEIVE_MSG_NOTE = "NanoappMessageDelivery ";
    private static final java.lang.String TAG = "ContextHubClientBroker";
    private static final long WAKELOCK_TIMEOUT_MILLIS = 5000;
    private final android.app.AppOpsManager mAppOpsManager;
    private final android.hardware.location.ContextHubInfo mAttachedContextHubInfo;
    private java.lang.String mAttributionTag;
    private final com.android.server.location.contexthub.ContextHubClientManager mClientManager;
    private final android.content.Context mContext;
    private android.hardware.location.IContextHubClientCallback mContextHubClientCallback;
    private final com.android.server.location.contexthub.IContextHubWrapper mContextHubProxy;
    private final java.util.Set<java.lang.Long> mForceDeniedNapps;
    private final short mHostEndPointId;
    private final java.util.concurrent.atomic.AtomicBoolean mIsPendingIntentCancelled;
    private final java.util.concurrent.atomic.AtomicBoolean mIsPermQueryIssued;
    private java.util.concurrent.atomic.AtomicBoolean mIsWakelockUsable;
    private final java.util.Map<java.lang.Long, java.lang.Integer> mMessageChannelNanoappIdMap;
    private final java.util.Map<java.lang.Long, com.android.server.location.contexthub.AuthStateDenialTimer> mNappToAuthTimerMap;
    private final java.lang.String mPackage;
    private final com.android.server.location.contexthub.ContextHubClientBroker.PendingIntentRequest mPendingIntentRequest;
    private final int mPid;
    private final android.hardware.location.IContextHubTransactionCallback mQueryPermsCallback;
    private boolean mRegistered;
    private final com.android.server.location.contexthub.ContextHubTransactionManager mTransactionManager;
    private final int mUid;
    private final android.os.PowerManager.WakeLock mWakeLock;

    interface CallbackConsumer {
        void accept(android.hardware.location.IContextHubClientCallback iContextHubClientCallback) throws android.os.RemoteException;
    }

    private static class PendingIntentRequest {
        private long mNanoAppId;
        private android.app.PendingIntent mPendingIntent;
        private boolean mValid;

        PendingIntentRequest() {
            this.mValid = false;
        }

        PendingIntentRequest(android.app.PendingIntent pendingIntent, long nanoAppId) {
            this.mValid = false;
            this.mPendingIntent = pendingIntent;
            this.mNanoAppId = nanoAppId;
            this.mValid = true;
        }

        public long getNanoAppId() {
            return this.mNanoAppId;
        }

        public android.app.PendingIntent getPendingIntent() {
            return this.mPendingIntent;
        }

        public boolean hasPendingIntent() {
            return this.mPendingIntent != null;
        }

        public void clear() {
            this.mPendingIntent = null;
        }

        public boolean isValid() {
            return this.mValid;
        }
    }

    private ContextHubClientBroker(android.content.Context context, com.android.server.location.contexthub.IContextHubWrapper contextHubProxy, com.android.server.location.contexthub.ContextHubClientManager clientManager, android.hardware.location.ContextHubInfo contextHubInfo, short hostEndPointId, android.hardware.location.IContextHubClientCallback callback, java.lang.String attributionTag, com.android.server.location.contexthub.ContextHubTransactionManager transactionManager, android.app.PendingIntent pendingIntent, long nanoAppId, java.lang.String packageName) {
        java.lang.String packageName2;
        this.mRegistered = true;
        this.mIsWakelockUsable = new java.util.concurrent.atomic.AtomicBoolean(true);
        this.mIsPendingIntentCancelled = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mIsPermQueryIssued = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mMessageChannelNanoappIdMap = new java.util.concurrent.ConcurrentHashMap();
        this.mForceDeniedNapps = new java.util.HashSet();
        this.mNappToAuthTimerMap = new java.util.concurrent.ConcurrentHashMap();
        this.mQueryPermsCallback = new android.hardware.location.IContextHubTransactionCallback.Stub() { // from class: com.android.server.location.contexthub.ContextHubClientBroker.1
            public void onTransactionComplete(int result) {
            }

            public void onQueryResponse(int result, java.util.List<android.hardware.location.NanoAppState> nanoAppStateList) {
                com.android.server.location.contexthub.ContextHubClientBroker.this.mIsPermQueryIssued.set(false);
                if (result != 0 && nanoAppStateList != null) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubClientBroker.TAG, "Permissions query failed, but still received nanoapp state");
                    return;
                }
                if (nanoAppStateList != null) {
                    for (android.hardware.location.NanoAppState state : nanoAppStateList) {
                        if (com.android.server.location.contexthub.ContextHubClientBroker.this.mMessageChannelNanoappIdMap.containsKey(java.lang.Long.valueOf(state.getNanoAppId()))) {
                            java.util.List<java.lang.String> permissions = state.getNanoAppPermissions();
                            com.android.server.location.contexthub.ContextHubClientBroker.this.updateNanoAppAuthState(state.getNanoAppId(), permissions, false);
                        }
                    }
                }
            }
        };
        this.mContext = context;
        this.mContextHubProxy = contextHubProxy;
        this.mClientManager = clientManager;
        this.mAttachedContextHubInfo = contextHubInfo;
        this.mHostEndPointId = hostEndPointId;
        this.mContextHubClientCallback = callback;
        if (pendingIntent == null) {
            this.mPendingIntentRequest = new com.android.server.location.contexthub.ContextHubClientBroker.PendingIntentRequest();
        } else {
            this.mPendingIntentRequest = new com.android.server.location.contexthub.ContextHubClientBroker.PendingIntentRequest(pendingIntent, nanoAppId);
        }
        if (packageName != null) {
            packageName2 = packageName;
        } else {
            java.lang.String[] packages = this.mContext.getPackageManager().getPackagesForUid(android.os.Binder.getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName2 = packages[0];
            } else {
                packageName2 = packageName;
            }
            android.util.Log.e(TAG, "createClient: Provided package name null. Using first package name " + packageName2);
        }
        this.mPackage = packageName2;
        this.mAttributionTag = attributionTag;
        this.mTransactionManager = transactionManager;
        this.mPid = android.os.Binder.getCallingPid();
        this.mUid = android.os.Binder.getCallingUid();
        this.mAppOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        android.os.PowerManager powerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        this.mWakeLock = powerManager.newWakeLock(1, TAG);
        this.mWakeLock.setWorkSource(new android.os.WorkSource(this.mUid, this.mPackage));
        this.mWakeLock.setReferenceCounted(true);
        startMonitoringOpChanges();
        sendHostEndpointConnectedEvent();
    }

    ContextHubClientBroker(android.content.Context context, com.android.server.location.contexthub.IContextHubWrapper contextHubProxy, com.android.server.location.contexthub.ContextHubClientManager clientManager, android.hardware.location.ContextHubInfo contextHubInfo, short hostEndPointId, android.hardware.location.IContextHubClientCallback callback, java.lang.String attributionTag, com.android.server.location.contexthub.ContextHubTransactionManager transactionManager, java.lang.String packageName) {
        this(context, contextHubProxy, clientManager, contextHubInfo, hostEndPointId, callback, attributionTag, transactionManager, null, 0L, packageName);
    }

    ContextHubClientBroker(android.content.Context context, com.android.server.location.contexthub.IContextHubWrapper contextHubProxy, com.android.server.location.contexthub.ContextHubClientManager clientManager, android.hardware.location.ContextHubInfo contextHubInfo, short hostEndPointId, android.app.PendingIntent pendingIntent, long nanoAppId, java.lang.String attributionTag, com.android.server.location.contexthub.ContextHubTransactionManager transactionManager) {
        this(context, contextHubProxy, clientManager, contextHubInfo, hostEndPointId, null, attributionTag, transactionManager, pendingIntent, nanoAppId, pendingIntent.getCreatorPackage());
    }

    private void startMonitoringOpChanges() {
        this.mAppOpsManager.startWatchingMode(-1, this.mPackage, this);
    }

    public int sendMessageToNanoApp(android.hardware.location.NanoAppMessage message) {
        return doSendMessageToNanoApp(message, null);
    }

    public int sendReliableMessageToNanoApp(android.hardware.location.NanoAppMessage message, android.hardware.location.IContextHubTransactionCallback transactionCallback) {
        return doSendMessageToNanoApp(message, transactionCallback);
    }

    private int doSendMessageToNanoApp(android.hardware.location.NanoAppMessage message, android.hardware.location.IContextHubTransactionCallback transactionCallback) throws android.os.RemoteException {
        int result;
        com.android.server.location.contexthub.ContextHubServiceUtil.checkPermissions(this.mContext);
        boolean z = false;
        message.setIsReliable(false);
        message.setMessageSequenceNumber(0);
        if (isRegistered()) {
            int authState = this.mMessageChannelNanoappIdMap.getOrDefault(java.lang.Long.valueOf(message.getNanoAppId()), -1).intValue();
            if (authState == 0) {
                if (!android.compat.Compatibility.isChangeEnabled(CHANGE_ID_AUTH_STATE_DENIED)) {
                    return 1;
                }
                throw new java.lang.SecurityException("Client doesn't have valid permissions to send message to " + message.getNanoAppId());
            }
            if (authState == -1) {
                checkNanoappPermsAsync();
            }
            if (!android.chre.flags.Flags.reliableMessageImplementation() || transactionCallback == null) {
                try {
                    result = this.mContextHubProxy.sendMessageToContextHub(this.mHostEndPointId, this.mAttachedContextHubInfo.getId(), message);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(TAG, "RemoteException in sendMessageToNanoApp (target hub ID = " + this.mAttachedContextHubInfo.getId() + ")", e);
                    result = 1;
                }
            } else {
                result = 0;
                com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createMessageTransaction(this.mHostEndPointId, this.mAttachedContextHubInfo.getId(), message, transactionCallback, getPackageName());
                try {
                    this.mTransactionManager.addTransaction(transaction);
                } catch (java.lang.IllegalStateException e2) {
                    android.util.Log.e(TAG, "Unable to add a transaction in sendMessageToNanoApp (target hub ID = " + this.mAttachedContextHubInfo.getId() + ")", e2);
                    result = 7;
                }
            }
            com.android.server.location.contexthub.ContextHubEventLogger contextHubEventLogger = com.android.server.location.contexthub.ContextHubEventLogger.getInstance();
            int id = this.mAttachedContextHubInfo.getId();
            if (result == 0) {
                z = true;
            }
            contextHubEventLogger.logMessageToNanoapp(id, message, z);
            return result;
        }
        java.lang.String messageString = java.util.Base64.getEncoder().encodeToString(message.getMessageBody());
        android.util.Log.e(TAG, java.lang.String.format("Failed to send message (connection closed): hostEndpointId= %1$d payload %2$s", java.lang.Short.valueOf(this.mHostEndPointId), messageString));
        return 1;
    }

    public void close() {
        synchronized (this) {
            this.mPendingIntentRequest.clear();
        }
        onClientExit();
    }

    public int getId() {
        return this.mHostEndPointId;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        onClientExit();
    }

    @Override // android.app.AppOpsManager.OnOpChangedListener
    public void onOpChanged(java.lang.String op, java.lang.String packageName) {
        if (packageName.equals(this.mPackage) && !this.mMessageChannelNanoappIdMap.isEmpty()) {
            checkNanoappPermsAsync();
        }
    }

    java.lang.String getPackageName() {
        return this.mPackage;
    }

    boolean isWakelockUsable() {
        boolean z;
        synchronized (this.mWakeLock) {
            z = this.mIsWakelockUsable.get();
        }
        return z;
    }

    android.os.PowerManager.WakeLock getWakeLock() {
        android.os.PowerManager.WakeLock wakeLock;
        synchronized (this.mWakeLock) {
            wakeLock = this.mWakeLock;
        }
        return wakeLock;
    }

    void setAttributionTag(java.lang.String attributionTag) {
        this.mAttributionTag = attributionTag;
    }

    java.lang.String getAttributionTag() {
        return this.mAttributionTag;
    }

    int getAttachedContextHubId() {
        return this.mAttachedContextHubInfo.getId();
    }

    short getHostEndPointId() {
        return this.mHostEndPointId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte sendMessageToClient(final android.hardware.location.NanoAppMessage message, java.util.List<java.lang.String> nanoappPermissions, java.util.List<java.lang.String> messagePermissions) {
        java.util.function.Consumer<java.lang.Byte> consumer;
        final long nanoAppId = message.getNanoAppId();
        int authState = updateNanoAppAuthState(nanoAppId, nanoappPermissions, false);
        if (authState == 1 && !messagePermissions.isEmpty()) {
            android.util.Log.e(TAG, "Dropping message from " + java.lang.Long.toHexString(nanoAppId) + ". " + this.mPackage + " in grace period and napp msg has permissions");
            return (byte) 3;
        }
        if (authState == 0 || !notePermissions(messagePermissions, RECEIVE_MSG_NOTE + nanoAppId)) {
            android.util.Log.e(TAG, "Dropping message from " + java.lang.Long.toHexString(nanoAppId) + ". " + this.mPackage + " doesn't have permission");
            return (byte) 3;
        }
        byte errorCode = invokeCallback(new com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda1
            @Override // com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer
            public final void accept(android.hardware.location.IContextHubClientCallback iContextHubClientCallback) {
                iContextHubClientCallback.onMessageFromNanoApp(message);
            }
        });
        if (errorCode != 0) {
            return errorCode;
        }
        java.util.function.Supplier<android.content.Intent> supplier = new java.util.function.Supplier() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$sendMessageToClient$1(nanoAppId, message);
            }
        };
        java.util.function.Consumer<java.lang.Byte> onFinishedCallback = new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$sendMessageToClient$2(message, (java.lang.Byte) obj);
            }
        };
        if (android.chre.flags.Flags.reliableMessageImplementation() && message.isReliable()) {
            consumer = onFinishedCallback;
        } else {
            consumer = null;
        }
        return sendPendingIntent(supplier, nanoAppId, consumer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.Intent lambda$sendMessageToClient$1(long nanoAppId, android.hardware.location.NanoAppMessage message) {
        return createIntent(5, nanoAppId).putExtra("android.hardware.location.extra.MESSAGE", (android.os.Parcelable) message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessageToClient$2(android.hardware.location.NanoAppMessage message, java.lang.Byte error) {
        sendMessageDeliveryStatusToContextHub(message.getMessageSequenceNumber(), error.byteValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onNanoAppLoaded(final long nanoAppId) {
        checkNanoappPermsAsync();
        invokeCallback(new com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda4
            @Override // com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer
            public final void accept(android.hardware.location.IContextHubClientCallback iContextHubClientCallback) {
                iContextHubClientCallback.onNanoAppLoaded(nanoAppId);
            }
        });
        sendPendingIntent(new java.util.function.Supplier() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$onNanoAppLoaded$4(nanoAppId);
            }
        }, nanoAppId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.Intent lambda$onNanoAppLoaded$4(long nanoAppId) {
        return createIntent(0, nanoAppId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onNanoAppUnloaded(final long nanoAppId) {
        invokeCallback(new com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda14
            @Override // com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer
            public final void accept(android.hardware.location.IContextHubClientCallback iContextHubClientCallback) {
                iContextHubClientCallback.onNanoAppUnloaded(nanoAppId);
            }
        });
        sendPendingIntent(new java.util.function.Supplier() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda15
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$onNanoAppUnloaded$6(nanoAppId);
            }
        }, nanoAppId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.Intent lambda$onNanoAppUnloaded$6(long nanoAppId) {
        return createIntent(1, nanoAppId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onHubReset() {
        invokeCallback(new com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda7
            @Override // com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer
            public final void accept(android.hardware.location.IContextHubClientCallback iContextHubClientCallback) {
                iContextHubClientCallback.onHubReset();
            }
        });
        sendPendingIntent(new java.util.function.Supplier() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda8
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$onHubReset$7();
            }
        });
        sendHostEndpointConnectedEvent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.Intent lambda$onHubReset$7() {
        return createIntent(6);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onNanoAppAborted(final long nanoAppId, final int abortCode) {
        invokeCallback(new com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda11
            @Override // com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer
            public final void accept(android.hardware.location.IContextHubClientCallback iContextHubClientCallback) {
                iContextHubClientCallback.onNanoAppAborted(nanoAppId, abortCode);
            }
        });
        java.util.function.Supplier<android.content.Intent> supplier = new java.util.function.Supplier() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda12
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$onNanoAppAborted$9(nanoAppId, abortCode);
            }
        };
        sendPendingIntent(supplier, nanoAppId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.Intent lambda$onNanoAppAborted$9(long nanoAppId, int abortCode) {
        return createIntent(4, nanoAppId).putExtra("android.hardware.location.extra.NANOAPP_ABORT_CODE", abortCode);
    }

    boolean hasPendingIntent(android.app.PendingIntent intent, long nanoAppId) {
        android.app.PendingIntent pendingIntent;
        long intentNanoAppId;
        synchronized (this) {
            pendingIntent = this.mPendingIntentRequest.getPendingIntent();
            intentNanoAppId = this.mPendingIntentRequest.getNanoAppId();
        }
        return pendingIntent != null && pendingIntent.equals(intent) && intentNanoAppId == nanoAppId;
    }

    void attachDeathRecipient() throws android.os.RemoteException {
        if (this.mContextHubClientCallback != null) {
            this.mContextHubClientCallback.asBinder().linkToDeath(this, 0);
        }
    }

    boolean hasPermissions(java.util.List<java.lang.String> permissions) {
        for (java.lang.String permission : permissions) {
            if (this.mContext.checkPermission(permission, this.mPid, this.mUid) != 0) {
                return false;
            }
        }
        return true;
    }

    boolean notePermissions(java.util.List<java.lang.String> permissions, java.lang.String noteMessage) {
        for (java.lang.String permission : permissions) {
            int opCode = android.app.AppOpsManager.permissionToOpCode(permission);
            if (opCode != -1) {
                try {
                    if (this.mAppOpsManager.noteOp(opCode, this.mUid, this.mPackage, this.mAttributionTag, noteMessage) != 0) {
                        return false;
                    }
                } catch (java.lang.SecurityException e) {
                    android.util.Log.e(TAG, "SecurityException: noteOp for pkg " + this.mPackage + " opcode " + opCode + ": " + e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    boolean isPendingIntentCancelled() {
        return this.mIsPendingIntentCancelled.get();
    }

    void handleAuthStateTimerExpiry(long nanoAppId) {
        com.android.server.location.contexthub.AuthStateDenialTimer timer;
        synchronized (this.mMessageChannelNanoappIdMap) {
            timer = this.mNappToAuthTimerMap.remove(java.lang.Long.valueOf(nanoAppId));
        }
        if (timer != null) {
            updateNanoAppAuthState(nanoAppId, java.util.Collections.emptyList(), true);
        }
    }

    private void checkNanoappPermsAsync() {
        if (!this.mIsPermQueryIssued.getAndSet(true)) {
            com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createQueryTransaction(this.mAttachedContextHubInfo.getId(), this.mQueryPermsCallback, this.mPackage);
            this.mTransactionManager.addTransaction(transaction);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int updateNanoAppAuthState(long nanoAppId, java.util.List<java.lang.String> nanoappPermissions, boolean gracePeriodExpired) {
        return updateNanoAppAuthState(nanoAppId, nanoappPermissions, gracePeriodExpired, false);
    }

    int updateNanoAppAuthState(long nanoAppId, java.util.List<java.lang.String> nanoappPermissions, boolean gracePeriodExpired, boolean forceDenied) {
        int curAuthState;
        int newAuthState;
        synchronized (this.mMessageChannelNanoappIdMap) {
            boolean hasPermissions = hasPermissions(nanoappPermissions);
            curAuthState = this.mMessageChannelNanoappIdMap.getOrDefault(java.lang.Long.valueOf(nanoAppId), -1).intValue();
            if (curAuthState == -1) {
                curAuthState = 2;
                this.mMessageChannelNanoappIdMap.put(java.lang.Long.valueOf(nanoAppId), 2);
            }
            newAuthState = curAuthState;
            if (forceDenied || this.mForceDeniedNapps.contains(java.lang.Long.valueOf(nanoAppId))) {
                newAuthState = 0;
                this.mForceDeniedNapps.add(java.lang.Long.valueOf(nanoAppId));
            } else if (gracePeriodExpired) {
                if (curAuthState == 1) {
                    newAuthState = 0;
                }
            } else if (curAuthState == 2 && !hasPermissions) {
                newAuthState = 1;
            } else if (curAuthState != 2 && hasPermissions) {
                newAuthState = 2;
            }
            if (newAuthState != 1) {
                com.android.server.location.contexthub.AuthStateDenialTimer timer = this.mNappToAuthTimerMap.remove(java.lang.Long.valueOf(nanoAppId));
                if (timer != null) {
                    timer.cancel();
                }
            } else if (curAuthState == 2) {
                com.android.server.location.contexthub.AuthStateDenialTimer timer2 = new com.android.server.location.contexthub.AuthStateDenialTimer(this, nanoAppId, android.os.Looper.getMainLooper());
                this.mNappToAuthTimerMap.put(java.lang.Long.valueOf(nanoAppId), timer2);
                timer2.start();
            }
            if (curAuthState != newAuthState) {
                this.mMessageChannelNanoappIdMap.put(java.lang.Long.valueOf(nanoAppId), java.lang.Integer.valueOf(newAuthState));
            }
        }
        if (curAuthState != newAuthState) {
            sendAuthStateCallback(nanoAppId, newAuthState);
        }
        return newAuthState;
    }

    private void sendAuthStateCallback(final long nanoAppId, final int authState) {
        invokeCallback(new com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda9
            @Override // com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer
            public final void accept(android.hardware.location.IContextHubClientCallback iContextHubClientCallback) {
                iContextHubClientCallback.onClientAuthorizationChanged(nanoAppId, authState);
            }
        });
        java.util.function.Supplier<android.content.Intent> supplier = new java.util.function.Supplier() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda10
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$sendAuthStateCallback$11(nanoAppId, authState);
            }
        };
        sendPendingIntent(supplier, nanoAppId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.Intent lambda$sendAuthStateCallback$11(long nanoAppId, int authState) {
        return createIntent(7, nanoAppId).putExtra("android.hardware.location.extra.CLIENT_AUTHORIZATION_STATE", authState);
    }

    private synchronized byte invokeCallback(com.android.server.location.contexthub.ContextHubClientBroker.CallbackConsumer consumer) {
        if (this.mContextHubClientCallback != null) {
            try {
                acquireWakeLock();
                consumer.accept(this.mContextHubClientCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "RemoteException while invoking client callback (host endpoint ID = " + ((int) this.mHostEndPointId) + ")", e);
                return (byte) 2;
            }
        }
        return (byte) 0;
    }

    private android.content.Intent createIntent(int eventType) {
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.hardware.location.extra.EVENT_TYPE", eventType);
        intent.putExtra("android.hardware.location.extra.CONTEXT_HUB_INFO", (android.os.Parcelable) this.mAttachedContextHubInfo);
        return intent;
    }

    private android.content.Intent createIntent(int eventType, long nanoAppId) {
        android.content.Intent intent = createIntent(eventType);
        intent.putExtra("android.hardware.location.extra.NANOAPP_ID", nanoAppId);
        return intent;
    }

    private synchronized byte sendPendingIntent(java.util.function.Supplier<android.content.Intent> supplier) {
        if (!this.mPendingIntentRequest.hasPendingIntent()) {
            return (byte) 0;
        }
        return doSendPendingIntent(this.mPendingIntentRequest.getPendingIntent(), supplier.get(), this);
    }

    private synchronized byte sendPendingIntent(java.util.function.Supplier<android.content.Intent> supplier, long nanoAppId) {
        return sendPendingIntent(supplier, nanoAppId, null);
    }

    private synchronized byte sendPendingIntent(java.util.function.Supplier<android.content.Intent> supplier, long nanoAppId, final java.util.function.Consumer<java.lang.Byte> onFinishedCallback) {
        if (!this.mPendingIntentRequest.hasPendingIntent() || this.mPendingIntentRequest.getNanoAppId() != nanoAppId) {
            return (byte) 0;
        }
        android.app.PendingIntent.OnFinished onFinished = new android.app.PendingIntent.OnFinished() { // from class: com.android.server.location.contexthub.ContextHubClientBroker.2
            @Override // android.app.PendingIntent.OnFinished
            public void onSendFinished(android.app.PendingIntent pendingIntent, android.content.Intent intent, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras) {
                byte b;
                if (onFinishedCallback != null) {
                    java.util.function.Consumer consumer = onFinishedCallback;
                    if (resultCode == 0) {
                        b = 0;
                    } else {
                        b = 1;
                    }
                    consumer.accept(java.lang.Byte.valueOf(b));
                }
                this.onSendFinished(pendingIntent, intent, resultCode, resultData, resultExtras);
            }
        };
        return doSendPendingIntent(this.mPendingIntentRequest.getPendingIntent(), supplier.get(), onFinished);
    }

    byte doSendPendingIntent(android.app.PendingIntent pendingIntent, android.content.Intent intent, android.app.PendingIntent.OnFinished onFinishedCallback) {
        try {
            acquireWakeLock();
            pendingIntent.send(this.mContext, 0, intent, onFinishedCallback, null, "android.permission.ACCESS_CONTEXT_HUB", null);
            return (byte) 0;
        } catch (android.app.PendingIntent.CanceledException e) {
            this.mIsPendingIntentCancelled.set(true);
            android.util.Log.w(TAG, "PendingIntent has been canceled, unregistering from client (host endpoint ID " + ((int) this.mHostEndPointId) + ")");
            close();
            return (byte) 2;
        }
    }

    private synchronized boolean isRegistered() {
        return this.mRegistered;
    }

    private synchronized void onClientExit() {
        if (this.mContextHubClientCallback != null) {
            this.mContextHubClientCallback.asBinder().unlinkToDeath(this, 0);
            this.mContextHubClientCallback = null;
        }
        if (!this.mPendingIntentRequest.hasPendingIntent() && this.mRegistered) {
            this.mClientManager.unregisterClient(this.mHostEndPointId);
            this.mRegistered = false;
            this.mAppOpsManager.stopWatchingMode(this);
            this.mContextHubProxy.onHostEndpointDisconnected(this.mHostEndPointId);
            releaseWakeLockOnExit();
        }
    }

    private java.lang.String authStateToString(int state) {
        switch (state) {
            case 0:
                return "DENIED";
            case 1:
                return "DENIED_GRACE_PERIOD";
            case 2:
                return "GRANTED";
            default:
                return "UNKNOWN";
        }
    }

    void sendHostEndpointConnectedEvent() {
        int i;
        android.hardware.contexthub.HostEndpointInfo info = new android.hardware.contexthub.HostEndpointInfo();
        info.hostEndpointId = (char) this.mHostEndPointId;
        info.packageName = this.mPackage;
        info.attributionTag = this.mAttributionTag;
        if (this.mUid == 1000) {
            i = 1;
        } else {
            i = 2;
        }
        info.type = i;
        this.mContextHubProxy.onHostEndpointConnected(info);
    }

    void dump(android.util.proto.ProtoOutputStream proto) {
        proto.write(1120986464257L, (int) getHostEndPointId());
        proto.write(1120986464258L, getAttachedContextHubId());
        proto.write(1138166333443L, this.mPackage);
        if (this.mPendingIntentRequest.isValid()) {
            proto.write(1133871366149L, true);
            proto.write(1112396529668L, this.mPendingIntentRequest.getNanoAppId());
        }
        proto.write(1133871366150L, this.mPendingIntentRequest.hasPendingIntent());
        proto.write(1133871366151L, isPendingIntentCancelled());
        proto.write(1133871366152L, this.mRegistered);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder out = new java.lang.StringBuilder();
        out.append("endpointID: ").append((int) getHostEndPointId()).append(", ");
        out.append("contextHub: ").append(getAttachedContextHubId()).append(", ");
        if (this.mAttributionTag != null) {
            out.append("attributionTag: ").append(getAttributionTag()).append(", ");
        }
        if (this.mPendingIntentRequest.isValid()) {
            out.append("intentCreatorPackage: ").append(this.mPackage).append(", ");
            out.append("nanoAppId: 0x").append(java.lang.Long.toHexString(this.mPendingIntentRequest.getNanoAppId())).append(", ");
        } else {
            out.append("package: ").append(this.mPackage).append(", ");
        }
        if (this.mMessageChannelNanoappIdMap.size() > 0) {
            out.append("messageChannelNanoappSet: (");
            java.util.Iterator<java.util.Map.Entry<java.lang.Long, java.lang.Integer>> it = this.mMessageChannelNanoappIdMap.entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<java.lang.Long, java.lang.Integer> entry = it.next();
                out.append("Nanoapp 0x").append(java.lang.Long.toHexString(entry.getKey().longValue())).append(": Auth state: ").append(authStateToString(entry.getValue().intValue()));
                if (it.hasNext()) {
                    out.append(", ");
                }
            }
            out.append(")").append(", ");
        }
        synchronized (this.mWakeLock) {
            out.append("wakelock: ").append(this.mWakeLock);
        }
        out.append("]");
        return out.toString();
    }

    public void callbackFinished() {
        releaseWakeLock();
    }

    public void reliableMessageCallbackFinished(int messageSequenceNumber, byte errorCode) {
        sendMessageDeliveryStatusToContextHub(messageSequenceNumber, errorCode);
        callbackFinished();
    }

    @Override // android.app.PendingIntent.OnFinished
    public void onSendFinished(android.app.PendingIntent pendingIntent, android.content.Intent intent, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras) {
        releaseWakeLock();
    }

    private void acquireWakeLock() {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda13
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$acquireWakeLock$12();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acquireWakeLock$12() throws java.lang.Exception {
        if (this.mIsWakelockUsable.get()) {
            this.mWakeLock.acquire(WAKELOCK_TIMEOUT_MILLIS);
        }
    }

    private void releaseWakeLock() {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda6
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$releaseWakeLock$13();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$releaseWakeLock$13() throws java.lang.Exception {
        if (this.mWakeLock.isHeld()) {
            try {
                this.mWakeLock.release();
            } catch (java.lang.RuntimeException e) {
                android.util.Log.e(TAG, "Releasing the wakelock fails - ", e);
            }
        }
    }

    private void releaseWakeLockOnExit() {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.contexthub.ContextHubClientBroker$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$releaseWakeLockOnExit$14();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$releaseWakeLockOnExit$14() throws java.lang.Exception {
        this.mIsWakelockUsable.set(false);
        while (this.mWakeLock.isHeld()) {
            try {
                this.mWakeLock.release();
            } catch (java.lang.RuntimeException e) {
                android.util.Log.e(TAG, "Releasing the wakelock for all acquisitions fails - ", e);
                return;
            }
        }
    }

    private void sendMessageDeliveryStatusToContextHub(int messageSequenceNumber, byte errorCode) {
        if (!android.chre.flags.Flags.reliableMessageImplementation()) {
            return;
        }
        android.hardware.contexthub.MessageDeliveryStatus status = new android.hardware.contexthub.MessageDeliveryStatus();
        status.messageSequenceNumber = messageSequenceNumber;
        status.errorCode = errorCode;
        if (this.mContextHubProxy.sendMessageDeliveryStatusToContextHub(this.mAttachedContextHubInfo.getId(), status) != 0) {
            android.util.Log.e(TAG, "Failed to send the reliable message status");
        }
    }
}
