package com.android.server.companion.datatransfer.contextsync;

/* JADX INFO: loaded from: classes.dex */
public class CallMetadataSyncConnectionService extends android.telecom.ConnectionService {
    private static final java.lang.String TAG = "CallMetadataSyncConnectionService";
    android.media.AudioManager mAudioManager;
    private com.android.server.companion.CompanionDeviceManagerServiceInternal mCdmsi;
    android.telecom.TelecomManager mTelecomManager;
    final java.util.Map<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection> mActiveConnections = new java.util.HashMap();
    final com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback mCrossDeviceSyncControllerCallback = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.AnonymousClass1();

    /* JADX INFO: renamed from: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService$1, reason: invalid class name */
    class AnonymousClass1 extends com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback {
        AnonymousClass1() {
        }

        @Override // com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback
        void processContextSyncMessage(final int associationId, final com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData callMetadataSyncData) {
            for (com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call call : callMetadataSyncData.getCalls()) {
                com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection existingConnection = com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.this.mActiveConnections.get(new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier(associationId, call.getId()));
                if (existingConnection != null) {
                    existingConnection.update(call);
                } else {
                    com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier key = null;
                    java.util.Iterator<java.util.Map.Entry<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection>> it = com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.this.mActiveConnections.entrySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        java.util.Map.Entry<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection> e = it.next();
                        if (e.getValue().getAssociationId() == associationId && !e.getValue().isIdFinalized() && call.getId().endsWith(e.getValue().getCallId())) {
                            com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier key2 = e.getKey();
                            key = key2;
                            break;
                        }
                    }
                    if (key != null) {
                        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection connection = com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.this.mActiveConnections.remove(key);
                        connection.update(call);
                        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.this.mActiveConnections.put(new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier(associationId, call.getId()), connection);
                    }
                }
            }
            com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.this.mActiveConnections.values().removeIf(new java.util.function.Predicate() { // from class: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService$1$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.AnonymousClass1.lambda$processContextSyncMessage$0(associationId, callMetadataSyncData, (com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection) obj);
                }
            });
        }

        static /* synthetic */ boolean lambda$processContextSyncMessage$0(int associationId, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData callMetadataSyncData, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection connection) {
            if (connection.isIdFinalized() && associationId == connection.getAssociationId() && !callMetadataSyncData.hasCall(connection.getCallId())) {
                connection.setDisconnected(new android.telecom.DisconnectCause(3));
                return true;
            }
            return false;
        }

        @Override // com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback
        void cleanUpCallIds(final java.util.Set<java.lang.String> callIds) {
            com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.this.mActiveConnections.values().removeIf(new java.util.function.Predicate() { // from class: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.AnonymousClass1.lambda$cleanUpCallIds$1(callIds, (com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection) obj);
                }
            });
        }

        static /* synthetic */ boolean lambda$cleanUpCallIds$1(java.util.Set callIds, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection connection) {
            if (callIds.contains(connection.getCallId())) {
                connection.setDisconnected(new android.telecom.DisconnectCause(3));
                return true;
            }
            return false;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mAudioManager = (android.media.AudioManager) getSystemService(android.media.AudioManager.class);
        this.mTelecomManager = (android.telecom.TelecomManager) getSystemService(android.telecom.TelecomManager.class);
        this.mCdmsi = (com.android.server.companion.CompanionDeviceManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.companion.CompanionDeviceManagerServiceInternal.class);
        this.mCdmsi.registerCallMetadataSyncCallback(this.mCrossDeviceSyncControllerCallback, 1);
    }

    @Override // android.telecom.ConnectionService
    public android.telecom.Connection onCreateIncomingConnection(android.telecom.PhoneAccountHandle phoneAccountHandle, android.telecom.ConnectionRequest connectionRequest) {
        int associationId = connectionRequest.getExtras().getInt("com.android.server.companion.datatransfer.contextsync.extra.ASSOCIATION_ID");
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call call = com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call.fromBundle(connectionRequest.getExtras().getBundle("com.android.server.companion.datatransfer.contextsync.extra.CALL"));
        call.setDirection(1);
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.CALL");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.CALL_FACILITATOR_ID");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.ASSOCIATION_ID");
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection connection = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection(this.mTelecomManager, this.mAudioManager, associationId, call, new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionCallback() { // from class: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.2
            @Override // com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionCallback
            void sendCallAction(int associationId2, java.lang.String callId, int action) {
                com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.this.mCdmsi.sendCrossDeviceSyncMessage(associationId2, com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.createCallControlMessage(callId, action));
            }
        });
        connection.setConnectionProperties(16);
        connection.setInitializing();
        return connection;
    }

    @Override // android.telecom.ConnectionService
    public void onCreateIncomingConnectionFailed(android.telecom.PhoneAccountHandle phoneAccountHandle, android.telecom.ConnectionRequest connectionRequest) {
        java.lang.String id = phoneAccountHandle != null ? phoneAccountHandle.getId() : "unknown PhoneAccount";
        android.util.Slog.e(TAG, "onCreateOutgoingConnectionFailed for: " + id);
    }

    @Override // android.telecom.ConnectionService
    public android.telecom.Connection onCreateOutgoingConnection(android.telecom.PhoneAccountHandle phoneAccountHandle, android.telecom.ConnectionRequest connectionRequest) {
        java.lang.String shortClassName;
        java.lang.String packageName;
        android.telecom.PhoneAccountHandle handle = phoneAccountHandle != null ? phoneAccountHandle : connectionRequest.getAccountHandle();
        android.telecom.PhoneAccount phoneAccount = this.mTelecomManager.getPhoneAccount(handle);
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call call = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call();
        call.setId(connectionRequest.getExtras().getString(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_CALL_ID));
        call.setStatus(0);
        if (phoneAccount != null) {
            shortClassName = phoneAccount.getLabel().toString();
        } else {
            shortClassName = handle.getComponentName().getShortClassName();
        }
        if (phoneAccount != null) {
            packageName = phoneAccount.getExtras().getString("com.android.server.companion.datatransfer.contextsync.extra.CALL_FACILITATOR_ID");
        } else {
            packageName = handle.getComponentName().getPackageName();
        }
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator callFacilitator = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator(shortClassName, packageName, handle.getComponentName().flattenToString());
        call.setFacilitator(callFacilitator);
        call.setDirection(2);
        call.setCallerId(connectionRequest.getAddress().getSchemeSpecificPart());
        int associationId = phoneAccount.getExtras().getInt("com.android.server.companion.datatransfer.contextsync.extra.ASSOCIATION_ID");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.CALL");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.CALL_FACILITATOR_ID");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.ASSOCIATION_ID");
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection connection = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection(this.mTelecomManager, this.mAudioManager, associationId, call, new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionCallback() { // from class: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.3
            @Override // com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionCallback
            void sendCallAction(int associationId2, java.lang.String callId, int action) {
                com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.this.mCdmsi.sendCrossDeviceSyncMessage(associationId2, com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.createCallControlMessage(callId, action));
            }
        });
        connection.setCallerDisplayName(call.getCallerId(), 1);
        this.mCdmsi.addSelfOwnedCallId(call.getId());
        this.mCdmsi.sendCrossDeviceSyncMessage(associationId, com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.createCallCreateMessage(call.getId(), connectionRequest.getAddress().toString(), call.getFacilitator().getIdentifier()));
        connection.setInitializing();
        return connection;
    }

    @Override // android.telecom.ConnectionService
    public void onCreateOutgoingConnectionFailed(android.telecom.PhoneAccountHandle phoneAccountHandle, android.telecom.ConnectionRequest connectionRequest) {
        java.lang.String id = phoneAccountHandle != null ? phoneAccountHandle.getId() : "unknown PhoneAccount";
        android.util.Slog.e(TAG, "onCreateOutgoingConnectionFailed for: " + id);
    }

    public void onCreateConnectionComplete(android.telecom.Connection connection) {
        if (connection instanceof com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection) {
            com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection callMetadataSyncConnection = (com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnection) connection;
            callMetadataSyncConnection.initialize();
            this.mActiveConnections.put(new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier(callMetadataSyncConnection.getAssociationId(), callMetadataSyncConnection.getCallId()), callMetadataSyncConnection);
        }
    }

    static final class CallMetadataSyncConnectionIdentifier {
        private final int mAssociationId;
        private final java.lang.String mCallId;

        CallMetadataSyncConnectionIdentifier(int associationId, java.lang.String callId) {
            this.mAssociationId = associationId;
            this.mCallId = callId;
        }

        public int getAssociationId() {
            return this.mAssociationId;
        }

        public java.lang.String getCallId() {
            return this.mCallId;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mAssociationId), this.mCallId);
        }

        public boolean equals(java.lang.Object other) {
            return (other instanceof com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier) && ((com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier) other).getAssociationId() == this.mAssociationId && this.mCallId != null && this.mCallId.equals(((com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionIdentifier) other).getCallId());
        }
    }

    static abstract class CallMetadataSyncConnectionCallback {
        abstract void sendCallAction(int i, java.lang.String str, int i2);

        CallMetadataSyncConnectionCallback() {
        }
    }

    static class CallMetadataSyncConnection extends android.telecom.Connection {
        private final int mAssociationId;
        private final android.media.AudioManager mAudioManager;
        private final com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call mCall;
        private final com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionCallback mCallback;
        private boolean mIsIdFinalized;
        private final android.telecom.TelecomManager mTelecomManager;

        CallMetadataSyncConnection(android.telecom.TelecomManager telecomManager, android.media.AudioManager audioManager, int associationId, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call call, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionCallback callback) {
            this.mTelecomManager = telecomManager;
            this.mAudioManager = audioManager;
            this.mAssociationId = associationId;
            this.mCall = call;
            this.mCallback = callback;
        }

        public java.lang.String getCallId() {
            return this.mCall.getId();
        }

        public int getAssociationId() {
            return this.mAssociationId;
        }

        public boolean isIdFinalized() {
            return this.mIsIdFinalized;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initialize() {
            int capabilities;
            int capabilities2;
            int status = this.mCall.getStatus();
            if (status == 4) {
                this.mTelecomManager.silenceRinger();
            }
            int state = com.android.server.companion.datatransfer.contextsync.CrossDeviceCall.convertStatusToState(status);
            if (state == 2) {
                setRinging();
            } else if (state == 4) {
                setActive();
            } else if (state == 3) {
                setOnHold();
            } else if (state == 7) {
                setDisconnected(new android.telecom.DisconnectCause(3));
            } else if (state == 1) {
                setDialing();
            } else {
                setInitialized();
            }
            java.lang.String callerId = this.mCall.getCallerId();
            if (callerId != null) {
                setCallerDisplayName(callerId, 1);
                setAddress(android.net.Uri.fromParts("custom", this.mCall.getCallerId(), null), 1);
            }
            android.os.Bundle extras = new android.os.Bundle();
            extras.putString(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_CALL_ID, this.mCall.getId());
            putExtras(extras);
            int capabilities3 = getConnectionCapabilities();
            if (this.mCall.hasControl(7)) {
                capabilities = capabilities3 | 1;
            } else {
                capabilities = capabilities3 & (-2);
            }
            if (this.mCall.hasControl(4)) {
                capabilities2 = capabilities | 64;
            } else {
                capabilities2 = capabilities & (-65);
            }
            this.mAudioManager.setMicrophoneMute(this.mCall.hasControl(5));
            if (capabilities2 != getConnectionCapabilities()) {
                setConnectionCapabilities(capabilities2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void update(com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call call) {
            int capabilities;
            int capabilities2;
            boolean hasMuteControl = true;
            if (!this.mIsIdFinalized) {
                this.mCall.setId(call.getId());
                this.mIsIdFinalized = true;
            }
            int status = call.getStatus();
            if (status == 4 && this.mCall.getStatus() != 4) {
                this.mTelecomManager.silenceRinger();
            }
            this.mCall.setStatus(status);
            int state = com.android.server.companion.datatransfer.contextsync.CrossDeviceCall.convertStatusToState(status);
            if (state != getState()) {
                if (state == 2) {
                    setRinging();
                } else if (state == 4) {
                    setActive();
                } else if (state == 3) {
                    setOnHold();
                } else if (state == 7) {
                    setDisconnected(new android.telecom.DisconnectCause(3));
                } else if (state == 1) {
                    setDialing();
                } else {
                    android.util.Slog.e(com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.TAG, "Could not update call to unknown state");
                }
            }
            int capabilities3 = getConnectionCapabilities();
            this.mCall.setControls(call.getControls());
            boolean hasHoldControl = this.mCall.hasControl(7) || this.mCall.hasControl(8);
            if (hasHoldControl) {
                capabilities = capabilities3 | 1;
            } else {
                capabilities = capabilities3 & (-2);
            }
            if (!this.mCall.hasControl(4) && !this.mCall.hasControl(5)) {
                hasMuteControl = false;
            }
            if (hasMuteControl) {
                capabilities2 = capabilities | 64;
            } else {
                capabilities2 = capabilities & (-65);
            }
            this.mAudioManager.setMicrophoneMute(this.mCall.hasControl(5));
            if (capabilities2 != getConnectionCapabilities()) {
                setConnectionCapabilities(capabilities2);
            }
        }

        @Override // android.telecom.Connection
        public void onAnswer(int videoState) {
            sendCallAction(1);
        }

        @Override // android.telecom.Connection
        public void onReject() {
            sendCallAction(2);
        }

        @Override // android.telecom.Connection
        public void onReject(int rejectReason) {
            onReject();
        }

        @Override // android.telecom.Connection
        public void onReject(java.lang.String replyMessage) {
            onReject();
        }

        @Override // android.telecom.Connection
        public void onSilence() {
            sendCallAction(3);
        }

        @Override // android.telecom.Connection
        public void onHold() {
            sendCallAction(7);
        }

        @Override // android.telecom.Connection
        public void onUnhold() {
            sendCallAction(8);
        }

        @Override // android.telecom.Connection
        public void onMuteStateChanged(boolean isMuted) {
            sendCallAction(isMuted ? 4 : 5);
        }

        @Override // android.telecom.Connection
        public void onDisconnect() {
            sendCallAction(6);
        }

        private void sendCallAction(int action) {
            this.mCallback.sendCallAction(this.mAssociationId, this.mCall.getId(), action);
        }
    }
}
