package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
public abstract class IContextHubWrapper {
    private static final java.lang.String TAG = "IContextHubWrapper";

    public interface ICallback {
        void handleContextHubEvent(int i);

        void handleMessageDeliveryStatus(android.hardware.contexthub.MessageDeliveryStatus messageDeliveryStatus);

        void handleNanoappAbort(long j, int i);

        void handleNanoappInfo(java.util.List<android.hardware.location.NanoAppState> list);

        void handleNanoappMessage(short s, android.hardware.location.NanoAppMessage nanoAppMessage, java.util.List<java.lang.String> list, java.util.List<java.lang.String> list2);

        void handleServiceRestart();

        void handleTransactionResult(int i, boolean z);
    }

    public abstract int disableNanoapp(int i, long j, int i2) throws android.os.RemoteException;

    public abstract int enableNanoapp(int i, long j, int i2) throws android.os.RemoteException;

    public abstract android.util.Pair<java.util.List<android.hardware.location.ContextHubInfo>, java.util.List<java.lang.String>> getHubs() throws android.os.RemoteException;

    public abstract long[] getPreloadedNanoappIds(int i);

    public abstract int loadNanoapp(int i, android.hardware.location.NanoAppBinary nanoAppBinary, int i2) throws android.os.RemoteException;

    public abstract void onAirplaneModeSettingChanged(boolean z);

    public abstract void onBtMainSettingChanged(boolean z);

    public abstract void onBtScanningSettingChanged(boolean z);

    public abstract void onLocationSettingChanged(boolean z);

    public abstract void onMicrophoneSettingChanged(boolean z);

    public abstract void onWifiMainSettingChanged(boolean z);

    public abstract void onWifiScanningSettingChanged(boolean z);

    public abstract void onWifiSettingChanged(boolean z);

    public abstract int queryNanoapps(int i) throws android.os.RemoteException;

    public abstract void registerCallback(int i, com.android.server.location.contexthub.IContextHubWrapper.ICallback iCallback) throws android.os.RemoteException;

    public abstract void registerExistingCallback(int i) throws android.os.RemoteException;

    public abstract int sendMessageDeliveryStatusToContextHub(int i, android.hardware.contexthub.MessageDeliveryStatus messageDeliveryStatus);

    public abstract int sendMessageToContextHub(short s, int i, android.hardware.location.NanoAppMessage nanoAppMessage) throws android.os.RemoteException;

    public abstract boolean setTestMode(boolean z);

    public abstract boolean supportsAirplaneModeSettingNotifications();

    public abstract boolean supportsBtSettingNotifications();

    public abstract boolean supportsLocationSettingNotifications();

    public abstract boolean supportsMicrophoneSettingNotifications();

    public abstract boolean supportsWifiSettingNotifications();

    public abstract int unloadNanoapp(int i, long j, int i2) throws android.os.RemoteException;

    public static com.android.server.location.contexthub.IContextHubWrapper getContextHubWrapper() {
        com.android.server.location.contexthub.IContextHubWrapper wrapper = maybeConnectToAidl();
        if (wrapper == null) {
            wrapper = maybeConnectTo1_2();
        }
        if (wrapper == null) {
            wrapper = maybeConnectTo1_1();
        }
        if (wrapper == null) {
            return maybeConnectTo1_0();
        }
        return wrapper;
    }

    public static com.android.server.location.contexthub.IContextHubWrapper maybeConnectTo1_0() {
        android.hardware.contexthub.V1_0.IContexthub proxy = null;
        try {
            proxy = android.hardware.contexthub.V1_0.IContexthub.getService(true);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "RemoteException while attaching to Context Hub HAL proxy", e);
        } catch (java.util.NoSuchElementException e2) {
            android.util.Log.i(TAG, "Context Hub HAL service not found");
        }
        if (proxy == null) {
            return null;
        }
        return new com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperV1_0(proxy);
    }

    public static com.android.server.location.contexthub.IContextHubWrapper maybeConnectTo1_1() {
        android.hardware.contexthub.V1_1.IContexthub proxy = null;
        try {
            proxy = android.hardware.contexthub.V1_1.IContexthub.getService(true);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "RemoteException while attaching to Context Hub HAL proxy", e);
        } catch (java.util.NoSuchElementException e2) {
            android.util.Log.i(TAG, "Context Hub HAL service not found");
        }
        if (proxy == null) {
            return null;
        }
        return new com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperV1_1(proxy);
    }

    public static com.android.server.location.contexthub.IContextHubWrapper maybeConnectTo1_2() {
        android.hardware.contexthub.V1_2.IContexthub proxy = null;
        try {
            proxy = android.hardware.contexthub.V1_2.IContexthub.getService(true);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "RemoteException while attaching to Context Hub HAL proxy", e);
        } catch (java.util.NoSuchElementException e2) {
            android.util.Log.i(TAG, "Context Hub HAL service not found");
        }
        if (proxy == null) {
            return null;
        }
        return new com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperV1_2(proxy);
    }

    public static android.hardware.contexthub.IContextHub maybeConnectToAidlGetProxy() {
        android.hardware.contexthub.IContextHub proxy = null;
        java.lang.String aidlServiceName = android.hardware.contexthub.IContextHub.class.getCanonicalName() + "/default";
        if (android.os.ServiceManager.isDeclared(aidlServiceName)) {
            proxy = android.hardware.contexthub.IContextHub.Stub.asInterface(android.os.ServiceManager.waitForService(aidlServiceName));
            if (proxy == null) {
                android.util.Log.e(TAG, "Context Hub AIDL service was declared but was not found");
            }
        } else {
            android.util.Log.d(TAG, "Context Hub AIDL service is not declared");
        }
        return proxy;
    }

    public static com.android.server.location.contexthub.IContextHubWrapper maybeConnectToAidl() {
        android.hardware.contexthub.IContextHub proxy = maybeConnectToAidlGetProxy();
        if (proxy == null) {
            return null;
        }
        return new com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperAidl(proxy);
    }

    public void onHostEndpointConnected(android.hardware.contexthub.HostEndpointInfo info) {
    }

    public void onHostEndpointDisconnected(short hostEndpointId) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ContextHubWrapperAidl extends com.android.server.location.contexthub.IContextHubWrapper implements android.os.IBinder.DeathRecipient {
        private android.os.Handler mHandler;
        private android.hardware.contexthub.IContextHub mHub;
        private final java.util.Map<java.lang.Integer, com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperAidl.ContextHubAidlCallback> mAidlCallbackMap = new java.util.HashMap();
        private java.lang.Runnable mHandleServiceRestartCallback = null;
        private android.os.HandlerThread mHandlerThread = new android.os.HandlerThread("Context Hub AIDL callback", 10);

        /* JADX INFO: Access modifiers changed from: private */
        class ContextHubAidlCallback extends android.hardware.contexthub.IContextHubCallback.Stub {
            private static final java.lang.String NAME = "ContextHubService";
            private static final byte[] UUID = {-102, 23, 0, -115, 107, -15, 68, 90, -112, 17, 109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_HID, -67, -104, 91, 108};
            private final com.android.server.location.contexthub.IContextHubWrapper.ICallback mCallback;
            private final int mContextHubId;

            ContextHubAidlCallback(int contextHubId, com.android.server.location.contexthub.IContextHubWrapper.ICallback callback) {
                this.mContextHubId = contextHubId;
                this.mCallback = callback;
            }

            public void handleNanoappInfo(android.hardware.contexthub.NanoappInfo[] appInfo) {
                final java.util.List<android.hardware.location.NanoAppState> nanoAppStateList = com.android.server.location.contexthub.ContextHubServiceUtil.createNanoAppStateList(appInfo);
                com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperAidl.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.contexthub.IContextHubWrapper$ContextHubWrapperAidl$ContextHubAidlCallback$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleNanoappInfo$0(nanoAppStateList);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$handleNanoappInfo$0(java.util.List nanoAppStateList) {
                this.mCallback.handleNanoappInfo(nanoAppStateList);
            }

            public void handleContextHubMessage(final android.hardware.contexthub.ContextHubMessage msg, final java.lang.String[] msgContentPerms) {
                com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperAidl.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.contexthub.IContextHubWrapper$ContextHubWrapperAidl$ContextHubAidlCallback$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleContextHubMessage$1(msg, msgContentPerms);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$handleContextHubMessage$1(android.hardware.contexthub.ContextHubMessage msg, java.lang.String[] msgContentPerms) {
                this.mCallback.handleNanoappMessage((short) msg.hostEndPoint, com.android.server.location.contexthub.ContextHubServiceUtil.createNanoAppMessage(msg), new java.util.ArrayList(java.util.Arrays.asList(msg.permissions)), new java.util.ArrayList(java.util.Arrays.asList(msgContentPerms)));
            }

            public void handleContextHubAsyncEvent(final int evt) {
                com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperAidl.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.contexthub.IContextHubWrapper$ContextHubWrapperAidl$ContextHubAidlCallback$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleContextHubAsyncEvent$2(evt);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$handleContextHubAsyncEvent$2(int evt) {
                this.mCallback.handleContextHubEvent(com.android.server.location.contexthub.ContextHubServiceUtil.toContextHubEventFromAidl(evt));
            }

            public void handleTransactionResult(final int transactionId, final boolean success) {
                com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperAidl.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.contexthub.IContextHubWrapper$ContextHubWrapperAidl$ContextHubAidlCallback$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleTransactionResult$3(transactionId, success);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$handleTransactionResult$3(int transactionId, boolean success) {
                this.mCallback.handleTransactionResult(transactionId, success);
            }

            public void handleNanSessionRequest(android.hardware.contexthub.NanSessionRequest request) {
            }

            public void handleMessageDeliveryStatus(char hostEndpointId, final android.hardware.contexthub.MessageDeliveryStatus messageDeliveryStatus) {
                if (android.chre.flags.Flags.reliableMessageImplementation()) {
                    com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperAidl.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.contexthub.IContextHubWrapper$ContextHubWrapperAidl$ContextHubAidlCallback$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$handleMessageDeliveryStatus$4(messageDeliveryStatus);
                        }
                    });
                } else {
                    android.util.Log.w(com.android.server.location.contexthub.IContextHubWrapper.TAG, "handleMessageDeliveryStatus called when the reliableMessageImplementation flag is disabled");
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$handleMessageDeliveryStatus$4(android.hardware.contexthub.MessageDeliveryStatus messageDeliveryStatus) {
                this.mCallback.handleMessageDeliveryStatus(messageDeliveryStatus);
            }

            public byte[] getUuid() {
                return UUID;
            }

            public java.lang.String getName() {
                return NAME;
            }

            public java.lang.String getInterfaceHash() {
                return "03f1982c8e20e58494a4ff8c9736b1c257dfeb6c";
            }

            public int getInterfaceVersion() {
                return 3;
            }
        }

        ContextHubWrapperAidl(android.hardware.contexthub.IContextHub hub) {
            setHub(hub);
            this.mHandlerThread.start();
            this.mHandler = new android.os.Handler(this.mHandlerThread.getLooper());
            linkWrapperToHubDeath();
        }

        private synchronized android.hardware.contexthub.IContextHub getHub() {
            return this.mHub;
        }

        private synchronized void setHub(android.hardware.contexthub.IContextHub hub) {
            this.mHub = hub;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Log.i(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Context Hub AIDL HAL died");
            setHub(maybeConnectToAidlGetProxy());
            if (getHub() == null) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Could not reconnect to Context Hub AIDL HAL");
                return;
            }
            linkWrapperToHubDeath();
            if (this.mHandleServiceRestartCallback != null) {
                this.mHandleServiceRestartCallback.run();
            } else {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "mHandleServiceRestartCallback is not set");
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public android.util.Pair<java.util.List<android.hardware.location.ContextHubInfo>, java.util.List<java.lang.String>> getHubs() throws android.os.RemoteException {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return new android.util.Pair<>(new java.util.ArrayList(), new java.util.ArrayList());
            }
            java.util.Set<java.lang.String> supportedPermissions = new java.util.HashSet<>();
            java.util.ArrayList<android.hardware.location.ContextHubInfo> hubInfoList = new java.util.ArrayList<>();
            for (android.hardware.contexthub.ContextHubInfo hubInfo : hub.getContextHubs()) {
                hubInfoList.add(new android.hardware.location.ContextHubInfo(hubInfo));
                for (java.lang.String permission : hubInfo.supportedPermissions) {
                    supportedPermissions.add(permission);
                }
            }
            return new android.util.Pair<>(hubInfoList, new java.util.ArrayList(supportedPermissions));
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsLocationSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsWifiSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsAirplaneModeSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsMicrophoneSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsBtSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onLocationSettingChanged(boolean enabled) {
            onSettingChanged((byte) 1, enabled);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onWifiSettingChanged(boolean enabled) {
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onAirplaneModeSettingChanged(boolean enabled) {
            onSettingChanged((byte) 4, enabled);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onMicrophoneSettingChanged(boolean enabled) {
            onSettingChanged((byte) 5, enabled);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onWifiMainSettingChanged(boolean enabled) {
            onSettingChanged((byte) 2, enabled);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onWifiScanningSettingChanged(boolean enabled) {
            onSettingChanged((byte) 3, enabled);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onBtMainSettingChanged(boolean enabled) {
            onSettingChanged((byte) 6, enabled);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onBtScanningSettingChanged(boolean enabled) {
            onSettingChanged((byte) 7, enabled);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onHostEndpointConnected(android.hardware.contexthub.HostEndpointInfo info) {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return;
            }
            try {
                hub.onHostEndpointConnected(info);
            } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Exception in onHostEndpointConnected" + e.getMessage());
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onHostEndpointDisconnected(short hostEndpointId) {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return;
            }
            try {
                hub.onHostEndpointDisconnected((char) hostEndpointId);
            } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Exception in onHostEndpointDisconnected" + e.getMessage());
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int sendMessageToContextHub(short hostEndpointId, int contextHubId, android.hardware.location.NanoAppMessage message) throws android.os.RemoteException {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return 2;
            }
            try {
                android.hardware.contexthub.ContextHubMessage msg = com.android.server.location.contexthub.ContextHubServiceUtil.createAidlContextHubMessage(hostEndpointId, message);
                hub.sendMessageToHub(contextHubId, msg);
                return 0;
            } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                return 1;
            } catch (java.lang.IllegalArgumentException e2) {
                return 2;
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int sendMessageDeliveryStatusToContextHub(int contextHubId, android.hardware.contexthub.MessageDeliveryStatus status) {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return 2;
            }
            try {
                hub.sendMessageDeliveryStatusToHub(contextHubId, status);
                return 0;
            } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                return 1;
            } catch (java.lang.IllegalArgumentException e2) {
                return 2;
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int loadNanoapp(int contextHubId, android.hardware.location.NanoAppBinary binary, int transactionId) throws android.os.RemoteException {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return 2;
            }
            android.hardware.contexthub.NanoappBinary aidlNanoAppBinary = com.android.server.location.contexthub.ContextHubServiceUtil.createAidlNanoAppBinary(binary);
            try {
                hub.loadNanoapp(contextHubId, aidlNanoAppBinary, transactionId);
                return 0;
            } catch (android.os.RemoteException | android.os.ServiceSpecificException | java.lang.UnsupportedOperationException e) {
                return 1;
            } catch (java.lang.IllegalArgumentException e2) {
                return 2;
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int unloadNanoapp(int contextHubId, long nanoappId, int transactionId) throws android.os.RemoteException {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return 2;
            }
            try {
                hub.unloadNanoapp(contextHubId, nanoappId, transactionId);
                return 0;
            } catch (android.os.RemoteException | android.os.ServiceSpecificException | java.lang.UnsupportedOperationException e) {
                return 1;
            } catch (java.lang.IllegalArgumentException e2) {
                return 2;
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int enableNanoapp(int contextHubId, long nanoappId, int transactionId) throws android.os.RemoteException {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return 2;
            }
            try {
                hub.enableNanoapp(contextHubId, nanoappId, transactionId);
                return 0;
            } catch (android.os.RemoteException | android.os.ServiceSpecificException | java.lang.UnsupportedOperationException e) {
                return 1;
            } catch (java.lang.IllegalArgumentException e2) {
                return 2;
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int disableNanoapp(int contextHubId, long nanoappId, int transactionId) throws android.os.RemoteException {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return 2;
            }
            try {
                hub.disableNanoapp(contextHubId, nanoappId, transactionId);
                return 0;
            } catch (android.os.RemoteException | android.os.ServiceSpecificException | java.lang.UnsupportedOperationException e) {
                return 1;
            } catch (java.lang.IllegalArgumentException e2) {
                return 2;
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int queryNanoapps(int contextHubId) throws android.os.RemoteException {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return 2;
            }
            try {
                hub.queryNanoapps(contextHubId);
                return 0;
            } catch (android.os.RemoteException | android.os.ServiceSpecificException | java.lang.UnsupportedOperationException e) {
                return 1;
            } catch (java.lang.IllegalArgumentException e2) {
                return 2;
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public long[] getPreloadedNanoappIds(int contextHubId) {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return null;
            }
            try {
                return hub.getPreloadedNanoappIds(contextHubId);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Exception while getting preloaded nanoapp IDs: " + e.getMessage());
                return null;
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void registerExistingCallback(int contextHubId) {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return;
            }
            com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperAidl.ContextHubAidlCallback callback = this.mAidlCallbackMap.get(java.lang.Integer.valueOf(contextHubId));
            if (callback == null) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Could not find existing callback to register for context hub ID = " + contextHubId);
                return;
            }
            try {
                hub.registerCallback(contextHubId, callback);
            } catch (android.os.RemoteException | android.os.ServiceSpecificException | java.lang.IllegalArgumentException e) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Exception while registering callback: " + e.getMessage());
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void registerCallback(int contextHubId, final com.android.server.location.contexthub.IContextHubWrapper.ICallback callback) {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return;
            }
            java.util.Objects.requireNonNull(callback);
            this.mHandleServiceRestartCallback = new java.lang.Runnable() { // from class: com.android.server.location.contexthub.IContextHubWrapper$ContextHubWrapperAidl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    callback.handleServiceRestart();
                }
            };
            this.mAidlCallbackMap.put(java.lang.Integer.valueOf(contextHubId), new com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperAidl.ContextHubAidlCallback(contextHubId, callback));
            registerExistingCallback(contextHubId);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean setTestMode(boolean enable) {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return false;
            }
            try {
                hub.setTestMode(enable);
                return true;
            } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Exception while setting test mode (enable: " + (enable ? "true" : "false") + "): " + e.getMessage());
                return false;
            }
        }

        private void onSettingChanged(byte setting, boolean enabled) {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return;
            }
            try {
                hub.onSettingChanged(setting, enabled);
            } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Exception while sending setting update: " + e.getMessage());
            }
        }

        private void linkWrapperToHubDeath() {
            android.hardware.contexthub.IContextHub hub = getHub();
            if (hub == null) {
                return;
            }
            try {
                hub.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Context Hub AIDL service death receipt could not be linked");
            }
        }
    }

    private static abstract class ContextHubWrapperHidl extends com.android.server.location.contexthub.IContextHubWrapper {
        protected com.android.server.location.contexthub.IContextHubWrapper.ICallback mCallback = null;
        protected final java.util.Map<java.lang.Integer, com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperHidl.ContextHubWrapperHidlCallback> mHidlCallbackMap = new java.util.HashMap();
        private android.hardware.contexthub.V1_0.IContexthub mHub;

        protected class ContextHubWrapperHidlCallback extends android.hardware.contexthub.V1_2.IContexthubCallback.Stub {
            private final com.android.server.location.contexthub.IContextHubWrapper.ICallback mCallback;
            private final int mContextHubId;

            ContextHubWrapperHidlCallback(int contextHubId, com.android.server.location.contexthub.IContextHubWrapper.ICallback callback) {
                this.mContextHubId = contextHubId;
                this.mCallback = callback;
            }

            public void handleClientMsg(android.hardware.contexthub.V1_0.ContextHubMsg message) {
                this.mCallback.handleNanoappMessage(message.hostEndPoint, com.android.server.location.contexthub.ContextHubServiceUtil.createNanoAppMessage(message), java.util.Collections.emptyList(), java.util.Collections.emptyList());
            }

            public void handleTxnResult(int transactionId, int result) {
                this.mCallback.handleTransactionResult(transactionId, result == 0);
            }

            public void handleHubEvent(int eventType) {
                this.mCallback.handleContextHubEvent(com.android.server.location.contexthub.ContextHubServiceUtil.toContextHubEvent(eventType));
            }

            public void handleAppAbort(long nanoAppId, int abortCode) {
                this.mCallback.handleNanoappAbort(nanoAppId, abortCode);
            }

            public void handleAppsInfo(java.util.ArrayList<android.hardware.contexthub.V1_0.HubAppInfo> nanoAppInfoList) {
                handleAppsInfo_1_2(com.android.server.location.contexthub.ContextHubServiceUtil.toHubAppInfo_1_2(nanoAppInfoList));
            }

            public void handleClientMsg_1_2(android.hardware.contexthub.V1_2.ContextHubMsg message, java.util.ArrayList<java.lang.String> messagePermissions) {
                this.mCallback.handleNanoappMessage(message.msg_1_0.hostEndPoint, com.android.server.location.contexthub.ContextHubServiceUtil.createNanoAppMessage(message.msg_1_0), message.permissions, messagePermissions);
            }

            public void handleAppsInfo_1_2(java.util.ArrayList<android.hardware.contexthub.V1_2.HubAppInfo> nanoAppInfoList) {
                java.util.List<android.hardware.location.NanoAppState> nanoAppStateList = com.android.server.location.contexthub.ContextHubServiceUtil.createNanoAppStateList(nanoAppInfoList);
                this.mCallback.handleNanoappInfo(nanoAppStateList);
            }
        }

        ContextHubWrapperHidl(android.hardware.contexthub.V1_0.IContexthub hub) {
            this.mHub = hub;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int sendMessageToContextHub(short hostEndpointId, int contextHubId, android.hardware.location.NanoAppMessage message) throws android.os.RemoteException {
            if (message.isReliable()) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Reliable messages are only supported with the AIDL HAL");
                return 2;
            }
            android.hardware.contexthub.V1_0.ContextHubMsg messageToNanoApp = com.android.server.location.contexthub.ContextHubServiceUtil.createHidlContextHubMessage(hostEndpointId, message);
            return com.android.server.location.contexthub.ContextHubServiceUtil.toTransactionResult(this.mHub.sendMessageToHub(contextHubId, messageToNanoApp));
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int sendMessageDeliveryStatusToContextHub(int contextHubId, android.hardware.contexthub.MessageDeliveryStatus status) {
            return 9;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int loadNanoapp(int contextHubId, android.hardware.location.NanoAppBinary binary, int transactionId) throws android.os.RemoteException {
            android.hardware.contexthub.V1_0.NanoAppBinary hidlNanoAppBinary = com.android.server.location.contexthub.ContextHubServiceUtil.createHidlNanoAppBinary(binary);
            return com.android.server.location.contexthub.ContextHubServiceUtil.toTransactionResult(this.mHub.loadNanoApp(contextHubId, hidlNanoAppBinary, transactionId));
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int unloadNanoapp(int contextHubId, long nanoappId, int transactionId) throws android.os.RemoteException {
            return com.android.server.location.contexthub.ContextHubServiceUtil.toTransactionResult(this.mHub.unloadNanoApp(contextHubId, nanoappId, transactionId));
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int enableNanoapp(int contextHubId, long nanoappId, int transactionId) throws android.os.RemoteException {
            return com.android.server.location.contexthub.ContextHubServiceUtil.toTransactionResult(this.mHub.enableNanoApp(contextHubId, nanoappId, transactionId));
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int disableNanoapp(int contextHubId, long nanoappId, int transactionId) throws android.os.RemoteException {
            return com.android.server.location.contexthub.ContextHubServiceUtil.toTransactionResult(this.mHub.disableNanoApp(contextHubId, nanoappId, transactionId));
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public int queryNanoapps(int contextHubId) throws android.os.RemoteException {
            return com.android.server.location.contexthub.ContextHubServiceUtil.toTransactionResult(this.mHub.queryApps(contextHubId));
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public long[] getPreloadedNanoappIds(int contextHubId) {
            return new long[0];
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void registerCallback(int contextHubId, com.android.server.location.contexthub.IContextHubWrapper.ICallback callback) throws android.os.RemoteException {
            this.mHidlCallbackMap.put(java.lang.Integer.valueOf(contextHubId), new com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperHidl.ContextHubWrapperHidlCallback(contextHubId, callback));
            this.mHub.registerCallback(contextHubId, this.mHidlCallbackMap.get(java.lang.Integer.valueOf(contextHubId)));
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void registerExistingCallback(int contextHubId) throws android.os.RemoteException {
            com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperHidl.ContextHubWrapperHidlCallback callback = this.mHidlCallbackMap.get(java.lang.Integer.valueOf(contextHubId));
            if (callback == null) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Could not find existing callback for context hub with ID = " + contextHubId);
            } else {
                this.mHub.registerCallback(contextHubId, callback);
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean setTestMode(boolean enable) {
            return false;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsBtSettingNotifications() {
            return false;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onWifiMainSettingChanged(boolean enabled) {
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onWifiScanningSettingChanged(boolean enabled) {
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onBtMainSettingChanged(boolean enabled) {
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onBtScanningSettingChanged(boolean enabled) {
        }
    }

    private static class ContextHubWrapperV1_0 extends com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperHidl {
        private android.hardware.contexthub.V1_0.IContexthub mHub;

        ContextHubWrapperV1_0(android.hardware.contexthub.V1_0.IContexthub hub) {
            super(hub);
            this.mHub = hub;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public android.util.Pair<java.util.List<android.hardware.location.ContextHubInfo>, java.util.List<java.lang.String>> getHubs() throws android.os.RemoteException {
            java.util.ArrayList<android.hardware.location.ContextHubInfo> hubInfoList = new java.util.ArrayList<>();
            for (android.hardware.contexthub.V1_0.ContextHub hub : this.mHub.getHubs()) {
                hubInfoList.add(new android.hardware.location.ContextHubInfo(hub));
            }
            return new android.util.Pair<>(hubInfoList, new java.util.ArrayList());
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsLocationSettingNotifications() {
            return false;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsWifiSettingNotifications() {
            return false;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsAirplaneModeSettingNotifications() {
            return false;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsMicrophoneSettingNotifications() {
            return false;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onLocationSettingChanged(boolean enabled) {
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onWifiSettingChanged(boolean enabled) {
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onAirplaneModeSettingChanged(boolean enabled) {
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onMicrophoneSettingChanged(boolean enabled) {
        }
    }

    private static class ContextHubWrapperV1_1 extends com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperHidl {
        private android.hardware.contexthub.V1_1.IContexthub mHub;

        ContextHubWrapperV1_1(android.hardware.contexthub.V1_1.IContexthub hub) {
            super(hub);
            this.mHub = hub;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public android.util.Pair<java.util.List<android.hardware.location.ContextHubInfo>, java.util.List<java.lang.String>> getHubs() throws android.os.RemoteException {
            java.util.ArrayList<android.hardware.location.ContextHubInfo> hubInfoList = new java.util.ArrayList<>();
            for (android.hardware.contexthub.V1_0.ContextHub hub : this.mHub.getHubs()) {
                hubInfoList.add(new android.hardware.location.ContextHubInfo(hub));
            }
            return new android.util.Pair<>(hubInfoList, new java.util.ArrayList());
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsLocationSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsWifiSettingNotifications() {
            return false;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsAirplaneModeSettingNotifications() {
            return false;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsMicrophoneSettingNotifications() {
            return false;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onLocationSettingChanged(boolean enabled) {
            try {
                this.mHub.onSettingChanged((byte) 0, enabled ? (byte) 1 : (byte) 0);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Failed to send setting change to Contexthub", e);
            }
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onWifiSettingChanged(boolean enabled) {
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onAirplaneModeSettingChanged(boolean enabled) {
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onMicrophoneSettingChanged(boolean enabled) {
        }
    }

    private static class ContextHubWrapperV1_2 extends com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperHidl implements android.hardware.contexthub.V1_2.IContexthub.getHubs_1_2Callback {
        private final android.hardware.contexthub.V1_2.IContexthub mHub;
        private android.util.Pair<java.util.List<android.hardware.location.ContextHubInfo>, java.util.List<java.lang.String>> mHubInfo;

        ContextHubWrapperV1_2(android.hardware.contexthub.V1_2.IContexthub hub) {
            super(hub);
            this.mHubInfo = new android.util.Pair<>(java.util.Collections.emptyList(), java.util.Collections.emptyList());
            this.mHub = hub;
        }

        public void onValues(java.util.ArrayList<android.hardware.contexthub.V1_0.ContextHub> hubs, java.util.ArrayList<java.lang.String> supportedPermissions) {
            java.util.ArrayList<android.hardware.location.ContextHubInfo> hubInfoList = new java.util.ArrayList<>();
            for (android.hardware.contexthub.V1_0.ContextHub hub : hubs) {
                hubInfoList.add(new android.hardware.location.ContextHubInfo(hub));
            }
            this.mHubInfo = new android.util.Pair<>(hubInfoList, supportedPermissions);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public android.util.Pair<java.util.List<android.hardware.location.ContextHubInfo>, java.util.List<java.lang.String>> getHubs() throws android.os.RemoteException {
            this.mHub.getHubs_1_2(this);
            return this.mHubInfo;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsLocationSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsWifiSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsAirplaneModeSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public boolean supportsMicrophoneSettingNotifications() {
            return true;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onLocationSettingChanged(boolean z) {
            sendSettingChanged((byte) 0, z ? (byte) 1 : (byte) 0);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onWifiSettingChanged(boolean z) {
            sendSettingChanged((byte) 1, z ? (byte) 1 : (byte) 0);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onAirplaneModeSettingChanged(boolean z) {
            sendSettingChanged((byte) 2, z ? (byte) 1 : (byte) 0);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper
        public void onMicrophoneSettingChanged(boolean z) {
            sendSettingChanged((byte) 3, z ? (byte) 1 : (byte) 0);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperHidl, com.android.server.location.contexthub.IContextHubWrapper
        public void registerCallback(int contextHubId, com.android.server.location.contexthub.IContextHubWrapper.ICallback callback) throws android.os.RemoteException {
            this.mHidlCallbackMap.put(java.lang.Integer.valueOf(contextHubId), new com.android.server.location.contexthub.IContextHubWrapper.ContextHubWrapperHidl.ContextHubWrapperHidlCallback(contextHubId, callback));
            this.mHub.registerCallback_1_2(contextHubId, this.mHidlCallbackMap.get(java.lang.Integer.valueOf(contextHubId)));
        }

        private void sendSettingChanged(byte setting, byte newValue) {
            try {
                this.mHub.onSettingChanged_1_2(setting, newValue);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.location.contexthub.IContextHubWrapper.TAG, "Failed to send setting change to Contexthub", e);
            }
        }
    }
}
