package com.android.server.companion.datatransfer.contextsync;

/* JADX INFO: loaded from: classes.dex */
public class CrossDeviceSyncController {
    private static final int CURRENT_VERSION = 1;
    static final java.lang.String EXTRA_ASSOCIATION_ID = "com.android.server.companion.datatransfer.contextsync.extra.ASSOCIATION_ID";
    static final java.lang.String EXTRA_CALL = "com.android.server.companion.datatransfer.contextsync.extra.CALL";
    static final java.lang.String EXTRA_CALL_FACILITATOR_ID = "com.android.server.companion.datatransfer.contextsync.extra.CALL_FACILITATOR_ID";
    public static final java.lang.String EXTRA_CALL_ID = "com.android.companion.datatransfer.contextsync.extra.CALL_ID";
    static final java.lang.String EXTRA_FACILITATOR_ICON = "com.android.companion.datatransfer.contextsync.extra.FACILITATOR_ICON";
    static final java.lang.String EXTRA_IS_REMOTE_ORIGIN = "com.android.companion.datatransfer.contextsync.extra.IS_REMOTE_ORIGIN";
    public static final java.lang.String FACILITATOR_ID_SYSTEM = "system";
    private static final java.lang.String TAG = "CrossDeviceSyncController";
    private static final int VERSION_1 = 1;
    private final com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.CallManager mCallManager;
    private final com.android.server.companion.transport.CompanionTransportManager mCompanionTransportManager;
    private java.lang.ref.WeakReference<com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback> mConnectionServiceCallbackRef;
    private final android.content.Context mContext;
    private java.lang.ref.WeakReference<com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback> mInCallServiceCallbackRef;
    private final com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountManager mPhoneAccountManager;
    private final java.util.List<android.companion.AssociationInfo> mConnectedAssociations = new java.util.ArrayList();
    private final java.util.Set<java.lang.Integer> mBlocklist = new java.util.HashSet();
    private final java.util.List<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator> mCallFacilitators = new java.util.ArrayList();

    public CrossDeviceSyncController(android.content.Context context, com.android.server.companion.transport.CompanionTransportManager companionTransportManager) {
        this.mContext = context;
        this.mCompanionTransportManager = companionTransportManager;
        this.mCompanionTransportManager.addListener(new android.companion.IOnTransportsChangedListener.Stub() { // from class: com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.1
            public void onTransportsChanged(java.util.List<android.companion.AssociationInfo> newAssociations) {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    if (!com.android.server.companion.CompanionDeviceConfig.isEnabled(com.android.server.companion.CompanionDeviceConfig.ENABLE_CONTEXT_SYNC_TELECOM)) {
                        return;
                    }
                    android.os.Binder.restoreCallingIdentity(token);
                    java.util.List<android.companion.AssociationInfo> existingAssociations = new java.util.ArrayList<>(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mConnectedAssociations);
                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mConnectedAssociations.clear();
                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mConnectedAssociations.addAll(newAssociations);
                    java.util.Iterator<android.companion.AssociationInfo> it = newAssociations.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        android.companion.AssociationInfo associationInfo = it.next();
                        if (!existingAssociations.contains(associationInfo)) {
                            if (!com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.isAssociationBlocked(associationInfo)) {
                                com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback callback = com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef != null ? (com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback) com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef.get() : null;
                                if (callback != null) {
                                    callback.updateNumberOfActiveSyncAssociations(associationInfo.getUserId(), true);
                                    callback.requestCrossDeviceSync(associationInfo);
                                } else {
                                    android.util.Slog.w(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.TAG, "No callback to report new transport");
                                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.syncMessageToDevice(associationInfo.getId(), com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.createFacilitatorMessage());
                                }
                            } else {
                                com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mBlocklist.add(java.lang.Integer.valueOf(associationInfo.getId()));
                                android.util.Slog.i(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.TAG, "New association was blocked from context syncing");
                            }
                        }
                    }
                    for (android.companion.AssociationInfo associationInfo2 : existingAssociations) {
                        if (!newAssociations.contains(associationInfo2)) {
                            com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mBlocklist.remove(java.lang.Integer.valueOf(associationInfo2.getId()));
                            if (!com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.isAssociationBlockedLocal(associationInfo2.getId())) {
                                com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback callback2 = com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef != null ? (com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback) com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef.get() : null;
                                if (callback2 != null) {
                                    callback2.updateNumberOfActiveSyncAssociations(associationInfo2.getUserId(), false);
                                } else {
                                    android.util.Slog.w(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.TAG, "No callback to report removed transport");
                                }
                            }
                            com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.clearInProgressCalls(associationInfo2.getId());
                        } else {
                            boolean systemBlocked = com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.isAssociationBlocked(associationInfo2);
                            if (com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.isAssociationBlockedLocal(associationInfo2.getId()) != systemBlocked) {
                                com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback callback3 = com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef != null ? (com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback) com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef.get() : null;
                                if (!systemBlocked) {
                                    android.util.Slog.i(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.TAG, "Unblocking existing association for context sync");
                                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mBlocklist.remove(java.lang.Integer.valueOf(associationInfo2.getId()));
                                    if (callback3 != null) {
                                        callback3.updateNumberOfActiveSyncAssociations(associationInfo2.getUserId(), true);
                                        callback3.requestCrossDeviceSync(associationInfo2);
                                    } else {
                                        android.util.Slog.w(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.TAG, "No callback to report changed transport");
                                        com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.syncMessageToDevice(associationInfo2.getId(), com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.createFacilitatorMessage());
                                    }
                                } else {
                                    android.util.Slog.i(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.TAG, "Blocking existing association for context sync");
                                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mBlocklist.add(java.lang.Integer.valueOf(associationInfo2.getId()));
                                    if (callback3 != null) {
                                        callback3.updateNumberOfActiveSyncAssociations(associationInfo2.getUserId(), false);
                                    } else {
                                        android.util.Slog.w(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.TAG, "No callback to report changed transport");
                                    }
                                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.syncMessageToDevice(associationInfo2.getId(), com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.createEmptyMessage());
                                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.clearInProgressCalls(associationInfo2.getId());
                                }
                            }
                        }
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        });
        this.mCompanionTransportManager.addListener(1667729539, new android.companion.IOnMessageReceivedListener.Stub() { // from class: com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.2
            public void onMessageReceived(int associationId, byte[] data) {
                if (com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.isAssociationBlockedLocal(associationId)) {
                    return;
                }
                com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData processedData = com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.processTelecomDataFromSync(data);
                boolean isRequest = (processedData.getCallControlRequests().size() == 0 && processedData.getCallCreateRequests().size() == 0) ? false : true;
                if (!isRequest) {
                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mPhoneAccountManager.updateFacilitators(associationId, processedData);
                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mCallManager.updateCalls(associationId, processedData);
                } else {
                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.processCallCreateRequests(processedData);
                }
                if (com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef == null && com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mConnectionServiceCallbackRef == null) {
                    android.util.Slog.w(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.TAG, "No callback to process context sync message");
                    return;
                }
                com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback inCallServiceCallback = com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef != null ? (com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback) com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef.get() : null;
                if (inCallServiceCallback != null) {
                    if (isRequest) {
                        inCallServiceCallback.processContextSyncMessage(associationId, processedData);
                    }
                } else {
                    com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mInCallServiceCallbackRef = null;
                }
                com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback connectionServiceCallback = com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mConnectionServiceCallbackRef != null ? (com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback) com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mConnectionServiceCallbackRef.get() : null;
                if (connectionServiceCallback != null) {
                    if (!isRequest) {
                        connectionServiceCallback.processContextSyncMessage(associationId, processedData);
                        return;
                    }
                    return;
                }
                com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.this.mConnectionServiceCallbackRef = null;
            }
        });
        this.mPhoneAccountManager = new com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountManager(this.mContext);
        this.mCallManager = new com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.CallManager(this.mContext, this.mPhoneAccountManager);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearInProgressCalls(int associationId) {
        java.util.Set<java.lang.String> removedIds = this.mCallManager.clearCallIdsForAssociationId(associationId);
        com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback connectionServiceCallback = this.mConnectionServiceCallbackRef != null ? this.mConnectionServiceCallbackRef.get() : null;
        if (connectionServiceCallback != null) {
            connectionServiceCallback.cleanUpCallIds(removedIds);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAssociationBlocked(android.companion.AssociationInfo info) {
        return (info.getSystemDataSyncFlags() & 1) != 1;
    }

    public void onBootCompleted() {
        android.telecom.PhoneAccountHandle defaultOutgoingTelAccountHandle;
        android.telecom.PhoneAccount defaultOutgoingTelAccount;
        if (!com.android.server.companion.CompanionDeviceConfig.isEnabled(com.android.server.companion.CompanionDeviceConfig.ENABLE_CONTEXT_SYNC_TELECOM)) {
            return;
        }
        this.mPhoneAccountManager.onBootCompleted();
        android.telecom.TelecomManager telecomManager = (android.telecom.TelecomManager) this.mContext.getSystemService(android.telecom.TelecomManager.class);
        if (telecomManager != null && telecomManager.getCallCapablePhoneAccounts().size() != 0 && (defaultOutgoingTelAccountHandle = telecomManager.getDefaultOutgoingPhoneAccount("tel")) != null && (defaultOutgoingTelAccount = telecomManager.getPhoneAccount(defaultOutgoingTelAccountHandle)) != null) {
            this.mCallFacilitators.add(new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator(defaultOutgoingTelAccount.getLabel().toString(), "system", "system"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCallCreateRequests(com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData callMetadataSyncData) {
        java.util.Iterator<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallCreateRequest> iterator = callMetadataSyncData.getCallCreateRequests().iterator();
        while (iterator.hasNext()) {
            com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallCreateRequest request = iterator.next();
            if ("system".equals(request.getFacilitator().getIdentifier())) {
                if (request.getAddress() != null && request.getAddress().startsWith("tel")) {
                    this.mCallManager.addSelfOwnedCallId(request.getId());
                    android.net.Uri uri = android.net.Uri.fromParts("tel", request.getAddress().replaceAll("\\D+", ""), null);
                    android.os.Bundle extras = new android.os.Bundle();
                    extras.putString(EXTRA_CALL_ID, request.getId());
                    android.os.Bundle outerExtras = new android.os.Bundle();
                    outerExtras.putParcelable("android.telecom.extra.OUTGOING_CALL_EXTRAS", extras);
                    ((android.telecom.TelecomManager) this.mContext.getSystemService(android.telecom.TelecomManager.class)).placeCall(uri, outerExtras);
                }
            } else {
                android.util.Slog.e(TAG, "Non-system facilitated calls are not supported yet");
            }
            iterator.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAssociationBlockedLocal(int associationId) {
        return this.mBlocklist.contains(java.lang.Integer.valueOf(associationId));
    }

    public void registerCallMetadataSyncCallback(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback callback, int type) {
        if (type == 2) {
            this.mInCallServiceCallbackRef = new java.lang.ref.WeakReference<>(callback);
            for (android.companion.AssociationInfo associationInfo : this.mConnectedAssociations) {
                if (!isAssociationBlocked(associationInfo)) {
                    this.mBlocklist.remove(java.lang.Integer.valueOf(associationInfo.getId()));
                    callback.updateNumberOfActiveSyncAssociations(associationInfo.getUserId(), true);
                    callback.requestCrossDeviceSync(associationInfo);
                } else {
                    this.mBlocklist.add(java.lang.Integer.valueOf(associationInfo.getId()));
                }
            }
            return;
        }
        if (type == 1) {
            this.mConnectionServiceCallbackRef = new java.lang.ref.WeakReference<>(callback);
        } else {
            android.util.Slog.e(TAG, "Cannot register callback of unknown type: " + type);
        }
    }

    private boolean isAdminBlocked(int userId) {
        return ((android.app.admin.DevicePolicyManager) this.mContext.getSystemService(android.app.admin.DevicePolicyManager.class)).getBluetoothContactSharingDisabled(android.os.UserHandle.of(userId));
    }

    public void syncToAllDevicesForUserId(int userId, java.util.Collection<com.android.server.companion.datatransfer.contextsync.CrossDeviceCall> calls) {
        java.util.Set<java.lang.Integer> associationIds = new java.util.HashSet<>();
        for (android.companion.AssociationInfo associationInfo : this.mConnectedAssociations) {
            if (associationInfo.getUserId() == userId && !isAssociationBlocked(associationInfo)) {
                associationIds.add(java.lang.Integer.valueOf(associationInfo.getId()));
            }
        }
        if (associationIds.isEmpty()) {
            android.util.Slog.w(TAG, "No eligible devices to sync to");
        } else {
            this.mCompanionTransportManager.sendMessage(1667729539, createCallUpdateMessage(calls, userId), associationIds.stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray());
        }
    }

    public void syncToSingleDevice(android.companion.AssociationInfo associationInfo, java.util.Collection<com.android.server.companion.datatransfer.contextsync.CrossDeviceCall> calls) {
        if (isAssociationBlocked(associationInfo)) {
            android.util.Slog.e(TAG, "Cannot sync to requested device; connection is blocked");
        } else {
            this.mCompanionTransportManager.sendMessage(1667729539, createCallUpdateMessage(calls, associationInfo.getUserId()), new int[]{associationInfo.getId()});
        }
    }

    public void syncMessageToDevice(int associationId, byte[] message) {
        if (isAssociationBlockedLocal(associationId)) {
            android.util.Slog.e(TAG, "Cannot sync to requested device; connection is blocked");
        } else {
            this.mCompanionTransportManager.sendMessage(1667729539, message, new int[]{associationId});
        }
    }

    public void syncMessageToAllDevicesForUserId(int userId, byte[] message) {
        java.util.Set<java.lang.Integer> associationIds = new java.util.HashSet<>();
        for (android.companion.AssociationInfo associationInfo : this.mConnectedAssociations) {
            if (associationInfo.getUserId() == userId && !isAssociationBlocked(associationInfo)) {
                associationIds.add(java.lang.Integer.valueOf(associationInfo.getId()));
            }
        }
        if (associationIds.isEmpty()) {
            android.util.Slog.w(TAG, "No eligible devices to sync to");
        } else {
            this.mCompanionTransportManager.sendMessage(1667729539, message, associationIds.stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray());
        }
    }

    public void addSelfOwnedCallId(java.lang.String callId) {
        this.mCallManager.addSelfOwnedCallId(callId);
    }

    public void removeSelfOwnedCallId(java.lang.String callId) {
        if (callId != null) {
            this.mCallManager.removeSelfOwnedCallId(callId);
        }
    }

    com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData processTelecomDataFromSync(byte[] data) {
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData callMetadataSyncData = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData();
        android.util.proto.ProtoInputStream pis = new android.util.proto.ProtoInputStream(data);
        int version = -1;
        while (pis.nextField() != -1) {
            try {
                switch (pis.getFieldNumber()) {
                    case 1:
                        version = pis.readInt(1120986464257L);
                        android.util.Slog.e(TAG, "Processing context sync message version " + version);
                        break;
                    case 4:
                        if (version == 1) {
                            long telecomToken = pis.start(1146756268036L);
                            while (pis.nextField() != -1) {
                                if (pis.getFieldNumber() == 1) {
                                    long callsToken = pis.start(2246267895809L);
                                    callMetadataSyncData.addCall(processCallDataFromSync(pis));
                                    pis.end(callsToken);
                                } else if (pis.getFieldNumber() == 2) {
                                    long requestsToken = pis.start(2246267895810L);
                                    while (pis.nextField() != -1) {
                                        switch (pis.getFieldNumber()) {
                                            case 1:
                                                long createActionToken = pis.start(1146756268033L);
                                                callMetadataSyncData.addCallCreateRequest(processCallCreateRequestDataFromSync(pis));
                                                pis.end(createActionToken);
                                                break;
                                            case 2:
                                                long controlActionToken = pis.start(1146756268034L);
                                                callMetadataSyncData.addCallControlRequest(processCallControlRequestDataFromSync(pis));
                                                pis.end(controlActionToken);
                                                break;
                                            default:
                                                android.util.Slog.e(TAG, "Unhandled field in Request:" + android.util.proto.ProtoUtils.currentFieldToString(pis));
                                                break;
                                        }
                                    }
                                    pis.end(requestsToken);
                                } else if (pis.getFieldNumber() == 3) {
                                    long facilitatorsToken = pis.start(2246267895811L);
                                    com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator facilitator = processFacilitatorDataFromSync(pis);
                                    facilitator.setIsTel(true);
                                    callMetadataSyncData.addFacilitator(facilitator);
                                    pis.end(facilitatorsToken);
                                } else {
                                    android.util.Slog.e(TAG, "Unhandled field in Telecom:" + android.util.proto.ProtoUtils.currentFieldToString(pis));
                                }
                            }
                            pis.end(telecomToken);
                        } else {
                            android.util.Slog.e(TAG, "Cannot process unsupported version " + version);
                        }
                        break;
                    default:
                        android.util.Slog.e(TAG, "Unhandled field in ContextSyncMessage:" + android.util.proto.ProtoUtils.currentFieldToString(pis));
                        break;
                }
            } catch (java.io.IOException | android.util.proto.ProtoParseException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
        return callMetadataSyncData;
    }

    public static com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallCreateRequest processCallCreateRequestDataFromSync(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallCreateRequest callCreateRequest = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallCreateRequest();
        while (pis.nextField() != -1) {
            switch (pis.getFieldNumber()) {
                case 1:
                    callCreateRequest.setId(pis.readString(1138166333441L));
                    break;
                case 2:
                    callCreateRequest.setAddress(pis.readString(1138166333442L));
                    break;
                case 3:
                    long facilitatorToken = pis.start(1146756268035L);
                    callCreateRequest.setFacilitator(processFacilitatorDataFromSync(pis));
                    pis.end(facilitatorToken);
                    break;
                default:
                    android.util.Slog.e(TAG, "Unhandled field in CreateAction:" + android.util.proto.ProtoUtils.currentFieldToString(pis));
                    break;
            }
        }
        return callCreateRequest;
    }

    public static com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallControlRequest processCallControlRequestDataFromSync(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallControlRequest callControlRequest = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallControlRequest();
        while (pis.nextField() != -1) {
            switch (pis.getFieldNumber()) {
                case 1:
                    callControlRequest.setId(pis.readString(1138166333441L));
                    break;
                case 2:
                    callControlRequest.setControl(pis.readInt(1159641169922L));
                    break;
                default:
                    android.util.Slog.e(TAG, "Unhandled field in ControlAction:" + android.util.proto.ProtoUtils.currentFieldToString(pis));
                    break;
            }
        }
        return callControlRequest;
    }

    public static com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator processFacilitatorDataFromSync(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator facilitator = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator();
        while (pis.nextField() != -1) {
            switch (pis.getFieldNumber()) {
                case 1:
                    facilitator.setName(pis.readString(1138166333441L));
                    break;
                case 2:
                    facilitator.setIdentifier(pis.readString(1138166333442L));
                    break;
                case 3:
                    facilitator.setExtendedIdentifier(pis.readString(1138166333443L));
                    break;
                default:
                    android.util.Slog.e(TAG, "Unhandled field in Facilitator:" + android.util.proto.ProtoUtils.currentFieldToString(pis));
                    break;
            }
        }
        return facilitator;
    }

    com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call processCallDataFromSync(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call call = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call();
        while (pis.nextField() != -1) {
            switch (pis.getFieldNumber()) {
                case 1:
                    call.setId(pis.readString(1138166333441L));
                    break;
                case 2:
                    long originToken = pis.start(1146756268034L);
                    while (pis.nextField() != -1) {
                        switch (pis.getFieldNumber()) {
                            case 1:
                                call.setCallerId(pis.readString(1138166333441L));
                                break;
                            case 2:
                                call.setAppIcon(pis.readBytes(1151051235330L));
                                break;
                            case 3:
                                long facilitatorToken = pis.start(1146756268035L);
                                call.setFacilitator(processFacilitatorDataFromSync(pis));
                                pis.end(facilitatorToken);
                                break;
                            default:
                                android.util.Slog.e(TAG, "Unhandled field in Origin:" + android.util.proto.ProtoUtils.currentFieldToString(pis));
                                break;
                        }
                    }
                    pis.end(originToken);
                    break;
                case 3:
                    call.setStatus(pis.readInt(1159641169923L));
                    break;
                case 4:
                    call.addControl(pis.readInt(2259152797700L));
                    break;
                case 5:
                    call.setDirection(pis.readInt(1159641169925L));
                    break;
                default:
                    android.util.Slog.e(TAG, "Unhandled field in Telecom:" + android.util.proto.ProtoUtils.currentFieldToString(pis));
                    break;
            }
        }
        return call;
    }

    byte[] createCallUpdateMessage(java.util.Collection<com.android.server.companion.datatransfer.contextsync.CrossDeviceCall> calls, int userId) {
        android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
        pos.write(1120986464257L, 1);
        long telecomToken = pos.start(1146756268036L);
        for (com.android.server.companion.datatransfer.contextsync.CrossDeviceCall call : calls) {
            if (!call.isCallPlacedByContextSync() && !this.mCallManager.isExternallyOwned(call.getId())) {
                long callsToken = pos.start(2246267895809L);
                pos.write(1138166333441L, call.getId());
                long originToken = pos.start(1146756268034L);
                pos.write(1138166333441L, call.getReadableCallerId(isAdminBlocked(call.getUserId())));
                pos.write(1151051235330L, call.getCallingAppIcon());
                long facilitatorToken = pos.start(1146756268035L);
                pos.write(1138166333441L, call.getCallingAppName());
                pos.write(1138166333442L, call.getCallingAppPackageName());
                pos.write(1138166333443L, call.getSerializedPhoneAccountHandle());
                pos.end(facilitatorToken);
                pos.end(originToken);
                pos.write(1159641169923L, call.getStatus());
                pos.write(1159641169925L, call.getDirection());
                java.util.Iterator<java.lang.Integer> it = call.getControls().iterator();
                while (it.hasNext()) {
                    int control = it.next().intValue();
                    pos.write(2259152797700L, control);
                }
                pos.end(callsToken);
            }
        }
        for (com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator facilitator : this.mCallFacilitators) {
            long facilitatorsToken = pos.start(2246267895811L);
            pos.write(1138166333441L, facilitator.getName());
            pos.write(1138166333442L, facilitator.getIdentifier());
            pos.write(1138166333443L, facilitator.getExtendedIdentifier());
            pos.end(facilitatorsToken);
        }
        pos.end(telecomToken);
        return pos.getBytes();
    }

    public static byte[] createCallControlMessage(java.lang.String callId, int control) {
        android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
        pos.write(1120986464257L, 1);
        long telecomToken = pos.start(1146756268036L);
        long requestsToken = pos.start(2246267895810L);
        long actionToken = pos.start(1146756268034L);
        pos.write(1138166333441L, callId);
        pos.write(1159641169922L, control);
        pos.end(actionToken);
        pos.end(requestsToken);
        pos.end(telecomToken);
        return pos.getBytes();
    }

    public static byte[] createCallCreateMessage(java.lang.String id, java.lang.String callAddress, java.lang.String facilitatorIdentifier) {
        android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
        pos.write(1120986464257L, 1);
        long telecomToken = pos.start(1146756268036L);
        long requestsToken = pos.start(2246267895810L);
        long actionToken = pos.start(1146756268033L);
        pos.write(1138166333441L, id);
        pos.write(1138166333442L, callAddress);
        long facilitatorToken = pos.start(1146756268035L);
        pos.write(1138166333442L, facilitatorIdentifier);
        pos.end(facilitatorToken);
        pos.end(actionToken);
        pos.end(requestsToken);
        pos.end(telecomToken);
        return pos.getBytes();
    }

    public static byte[] createEmptyMessage() {
        android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
        pos.write(1120986464257L, 1);
        return pos.getBytes();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] createFacilitatorMessage() {
        return createCallUpdateMessage(java.util.Collections.emptyList(), -1);
    }

    static class CallManager {
        private final com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountManager mPhoneAccountManager;
        private final android.telecom.TelecomManager mTelecomManager;
        final java.util.Set<java.lang.String> mSelfOwnedCalls = new java.util.HashSet();
        final java.util.Set<java.lang.String> mExternallyOwnedCalls = new java.util.HashSet();
        final java.util.Map<java.lang.Integer, java.util.Set<java.lang.String>> mCallIds = new java.util.HashMap();

        CallManager(android.content.Context context, com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountManager phoneAccountManager) {
            this.mTelecomManager = (android.telecom.TelecomManager) context.getSystemService(android.telecom.TelecomManager.class);
            this.mPhoneAccountManager = phoneAccountManager;
        }

        void updateCalls(int associationId, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData data) {
            java.util.Set<java.lang.String> oldCallIds = this.mCallIds.getOrDefault(java.lang.Integer.valueOf(associationId), new java.util.HashSet());
            java.util.Set<java.lang.String> newCallIds = (java.util.Set) data.getCalls().stream().map(new java.util.function.Function() { // from class: com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController$CallManager$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call) obj).getId();
                }
            }).collect(java.util.stream.Collectors.toSet());
            if (oldCallIds.equals(newCallIds)) {
                return;
            }
            for (com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call currentCall : data.getCalls()) {
                if (!oldCallIds.contains(currentCall.getId()) && currentCall.getFacilitator() != null && !isSelfOwned(currentCall.getId())) {
                    this.mExternallyOwnedCalls.add(currentCall.getId());
                    android.os.Bundle extras = new android.os.Bundle();
                    extras.putInt(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_ASSOCIATION_ID, associationId);
                    extras.putBoolean(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_IS_REMOTE_ORIGIN, true);
                    extras.putBundle(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_CALL, currentCall.writeToBundle());
                    extras.putString(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_CALL_ID, currentCall.getId());
                    extras.putByteArray(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_FACILITATOR_ICON, currentCall.getAppIcon());
                    android.telecom.PhoneAccountHandle handle = this.mPhoneAccountManager.getPhoneAccountHandle(associationId, currentCall.getFacilitator().getIdentifier());
                    if (currentCall.getDirection() == 1) {
                        this.mTelecomManager.addNewIncomingCall(handle, extras);
                    } else if (currentCall.getDirection() == 2) {
                        android.os.Bundle wrappedExtras = new android.os.Bundle();
                        wrappedExtras.putParcelable("android.telecom.extra.OUTGOING_CALL_EXTRAS", extras);
                        wrappedExtras.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", handle);
                        java.lang.String address = currentCall.getCallerId();
                        if (address != null) {
                            this.mTelecomManager.placeCall(android.net.Uri.fromParts("sip", address, null), wrappedExtras);
                        }
                    }
                }
            }
            this.mCallIds.put(java.lang.Integer.valueOf(associationId), newCallIds);
        }

        java.util.Set<java.lang.String> clearCallIdsForAssociationId(int associationId) {
            return this.mCallIds.remove(java.lang.Integer.valueOf(associationId));
        }

        void addSelfOwnedCallId(java.lang.String callId) {
            this.mSelfOwnedCalls.add(callId);
        }

        void removeSelfOwnedCallId(java.lang.String callId) {
            this.mSelfOwnedCalls.remove(callId);
        }

        boolean isExternallyOwned(java.lang.String callId) {
            return this.mExternallyOwnedCalls.contains(callId);
        }

        private boolean isSelfOwned(java.lang.String currentCallId) {
            for (java.lang.String selfOwnedCallId : this.mSelfOwnedCalls) {
                if (currentCallId.endsWith(selfOwnedCallId)) {
                    return true;
                }
            }
            return false;
        }
    }

    static class PhoneAccountManager {
        private final android.content.ComponentName mConnectionServiceComponentName;
        private final java.util.Map<com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier, android.telecom.PhoneAccountHandle> mPhoneAccountHandles = new java.util.HashMap();
        private final android.telecom.TelecomManager mTelecomManager;

        PhoneAccountManager(android.content.Context context) {
            this.mTelecomManager = (android.telecom.TelecomManager) context.getSystemService(android.telecom.TelecomManager.class);
            this.mConnectionServiceComponentName = new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.class);
        }

        void onBootCompleted() {
            this.mTelecomManager.clearPhoneAccounts();
        }

        android.telecom.PhoneAccountHandle getPhoneAccountHandle(int associationId, java.lang.String appIdentifier) {
            return this.mPhoneAccountHandles.get(new com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier(associationId, appIdentifier));
        }

        void updateFacilitators(int associationId, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData data) {
            java.util.ArrayList<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator> facilitators = new java.util.ArrayList<>();
            for (com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call call : data.getCalls()) {
                facilitators.add(call.getFacilitator());
            }
            facilitators.addAll(data.getFacilitators());
            updateFacilitators(associationId, facilitators);
        }

        private void updateFacilitators(int associationId, java.util.List<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator> facilitators) {
            java.util.Iterator<com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier> iterator = this.mPhoneAccountHandles.keySet().iterator();
            while (iterator.hasNext()) {
                com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier handleIdentifier = iterator.next();
                final java.lang.String handleAppIdentifier = handleIdentifier.getAppIdentifier();
                int handleAssociationId = handleIdentifier.getAssociationId();
                if (associationId == handleAssociationId && facilitators.stream().noneMatch(new java.util.function.Predicate() { // from class: com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController$PhoneAccountManager$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountManager.lambda$updateFacilitators$0(handleAppIdentifier, (com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator) obj);
                    }
                })) {
                    unregisterPhoneAccount(this.mPhoneAccountHandles.get(handleIdentifier));
                    iterator.remove();
                }
            }
            for (com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator facilitator : facilitators) {
                com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier phoneAccountHandleIdentifier = new com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier(associationId, facilitator.getIdentifier());
                if (!this.mPhoneAccountHandles.containsKey(phoneAccountHandleIdentifier)) {
                    registerPhoneAccount(phoneAccountHandleIdentifier, facilitator.getName(), facilitator.isTel());
                }
            }
        }

        static /* synthetic */ boolean lambda$updateFacilitators$0(java.lang.String handleAppIdentifier, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator facilitator) {
            return handleAppIdentifier != null && handleAppIdentifier.equals(facilitator.getIdentifier());
        }

        private void registerPhoneAccount(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier handleIdentifier, java.lang.String humanReadableAppName, boolean isTel) {
            if (this.mPhoneAccountHandles.containsKey(handleIdentifier)) {
                return;
            }
            android.telecom.PhoneAccountHandle handle = new android.telecom.PhoneAccountHandle(this.mConnectionServiceComponentName, java.util.UUID.randomUUID().toString());
            this.mPhoneAccountHandles.put(handleIdentifier, handle);
            android.telecom.PhoneAccount phoneAccount = createPhoneAccount(handle, humanReadableAppName, handleIdentifier.getAppIdentifier(), handleIdentifier.getAssociationId(), isTel);
            this.mTelecomManager.registerPhoneAccount(phoneAccount);
            this.mTelecomManager.enablePhoneAccount(this.mPhoneAccountHandles.get(handleIdentifier), true);
        }

        private void unregisterPhoneAccount(android.telecom.PhoneAccountHandle phoneAccountHandle) {
            this.mTelecomManager.unregisterPhoneAccount(phoneAccountHandle);
        }

        static android.telecom.PhoneAccount createPhoneAccount(android.telecom.PhoneAccountHandle handle, java.lang.String humanReadableAppName, java.lang.String appIdentifier, int associationId, boolean isTel) {
            android.os.Bundle extras = new android.os.Bundle();
            extras.putString(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_CALL_FACILITATOR_ID, appIdentifier);
            extras.putInt(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_ASSOCIATION_ID, associationId);
            return new android.telecom.PhoneAccount.Builder(handle, humanReadableAppName).setExtras(extras).setSupportedUriSchemes(java.util.List.of(isTel ? "tel" : "sip")).setCapabilities(3).build();
        }
    }

    static final class PhoneAccountHandleIdentifier {
        private final java.lang.String mAppIdentifier;
        private final int mAssociationId;

        PhoneAccountHandleIdentifier(int associationId, java.lang.String appIdentifier) {
            this.mAssociationId = associationId;
            this.mAppIdentifier = appIdentifier;
        }

        public int getAssociationId() {
            return this.mAssociationId;
        }

        public java.lang.String getAppIdentifier() {
            return this.mAppIdentifier;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mAssociationId), this.mAppIdentifier);
        }

        public boolean equals(java.lang.Object other) {
            return (other instanceof com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier) && ((com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier) other).getAssociationId() == this.mAssociationId && this.mAppIdentifier != null && this.mAppIdentifier.equals(((com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.PhoneAccountHandleIdentifier) other).getAppIdentifier());
        }
    }
}
