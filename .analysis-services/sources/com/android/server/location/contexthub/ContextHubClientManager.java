package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
class ContextHubClientManager {
    public static final int ACTION_CANCELLED = 2;
    public static final int ACTION_REGISTERED = 0;
    public static final int ACTION_UNREGISTERED = 1;
    private static final boolean DEBUG_LOG_ENABLED = false;
    private static final int MAX_CLIENT_ID = 32767;
    private static final int NUM_CLIENT_RECORDS = 20;
    private static final java.lang.String TAG = "ContextHubClientManager";
    private final android.content.Context mContext;
    private final com.android.server.location.contexthub.IContextHubWrapper mContextHubProxy;
    private final java.util.concurrent.ConcurrentHashMap<java.lang.Short, com.android.server.location.contexthub.ContextHubClientBroker> mHostEndPointIdToClientMap = new java.util.concurrent.ConcurrentHashMap<>();
    private int mNextHostEndPointId = 0;
    private final com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<com.android.server.location.contexthub.ContextHubClientManager.RegistrationRecord> mRegistrationRecordDeque = new com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<>(20);

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Action {
    }

    private class RegistrationRecord {
        private final int mAction;
        private final java.lang.String mBroker;
        private final long mTimestamp = java.lang.System.currentTimeMillis();

        RegistrationRecord(java.lang.String broker, int action) {
            this.mBroker = broker;
            this.mAction = action;
        }

        void dump(android.util.proto.ProtoOutputStream proto) {
            proto.write(1112396529665L, this.mTimestamp);
            proto.write(1120986464258L, this.mAction);
            proto.write(1138166333443L, this.mBroker);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(com.android.server.location.contexthub.ContextHubServiceUtil.formatDateFromTimestamp(this.mTimestamp));
            sb.append(" ");
            sb.append(this.mAction == 0 ? "+ " : "- ");
            sb.append(this.mBroker);
            if (this.mAction == 2) {
                sb.append(" (cancelled)");
            }
            return sb.toString();
        }
    }

    ContextHubClientManager(android.content.Context context, com.android.server.location.contexthub.IContextHubWrapper contextHubProxy) {
        this.mContext = context;
        this.mContextHubProxy = contextHubProxy;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [android.os.IBinder, com.android.server.location.contexthub.ContextHubClientBroker, java.lang.Object] */
    android.hardware.location.IContextHubClient registerClient(android.hardware.location.ContextHubInfo contextHubInfo, android.hardware.location.IContextHubClientCallback iContextHubClientCallback, java.lang.String str, com.android.server.location.contexthub.ContextHubTransactionManager contextHubTransactionManager, java.lang.String str2) {
        ?? contextHubClientBroker;
        synchronized (this) {
            short hostEndPointId = getHostEndPointId();
            contextHubClientBroker = new com.android.server.location.contexthub.ContextHubClientBroker(this.mContext, this.mContextHubProxy, this, contextHubInfo, hostEndPointId, iContextHubClientCallback, str, contextHubTransactionManager, str2);
            this.mHostEndPointIdToClientMap.put(java.lang.Short.valueOf(hostEndPointId), (com.android.server.location.contexthub.ContextHubClientBroker) contextHubClientBroker);
            this.mRegistrationRecordDeque.add(new com.android.server.location.contexthub.ContextHubClientManager.RegistrationRecord(contextHubClientBroker.toString(), 0));
        }
        try {
            contextHubClientBroker.attachDeathRecipient();
            android.util.Log.d(TAG, "Registered client with host endpoint ID " + ((int) contextHubClientBroker.getHostEndPointId()));
            return android.hardware.location.IContextHubClient.Stub.asInterface((android.os.IBinder) contextHubClientBroker);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Failed to attach death recipient to client");
            contextHubClientBroker.close();
            return null;
        }
    }

    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v7, types: [android.os.IBinder, com.android.server.location.contexthub.ContextHubClientBroker] */
    android.hardware.location.IContextHubClient registerClient(android.hardware.location.ContextHubInfo contextHubInfo, android.app.PendingIntent pendingIntent, long j, java.lang.String str, com.android.server.location.contexthub.ContextHubTransactionManager contextHubTransactionManager) throws java.lang.Throwable {
        ?? r0;
        java.lang.String str2 = "Regenerated";
        synchronized (this) {
            try {
                try {
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
                try {
                    com.android.server.location.contexthub.ContextHubClientBroker clientBroker = getClientBroker(contextHubInfo.getId(), pendingIntent, j);
                    if (clientBroker == null) {
                        short hostEndPointId = getHostEndPointId();
                        com.android.server.location.contexthub.ContextHubClientBroker contextHubClientBroker = new com.android.server.location.contexthub.ContextHubClientBroker(this.mContext, this.mContextHubProxy, this, contextHubInfo, hostEndPointId, pendingIntent, j, str, contextHubTransactionManager);
                        this.mHostEndPointIdToClientMap.put(java.lang.Short.valueOf(hostEndPointId), contextHubClientBroker);
                        str2 = "Registered";
                        this.mRegistrationRecordDeque.add(new com.android.server.location.contexthub.ContextHubClientManager.RegistrationRecord(contextHubClientBroker.toString(), 0));
                        r0 = contextHubClientBroker;
                    } else {
                        clientBroker.setAttributionTag(str);
                        r0 = clientBroker;
                    }
                    android.util.Log.d(TAG, str2 + " client with host endpoint ID " + ((int) r0.getHostEndPointId()));
                    return android.hardware.location.IContextHubClient.Stub.asInterface((android.os.IBinder) r0);
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    byte onMessageFromNanoApp(int contextHubId, short hostEndpointId, android.hardware.location.NanoAppMessage message, java.util.List<java.lang.String> nanoappPermissions, java.util.List<java.lang.String> messagePermissions) {
        if (message.isBroadcastMessage()) {
            if (android.chre.flags.Flags.reliableMessageImplementation() && message.isReliable()) {
                android.util.Log.e(TAG, "Received reliable broadcast message from " + message.getNanoAppId());
                return (byte) 2;
            }
            if (!messagePermissions.isEmpty()) {
                android.util.Log.e(TAG, "Received broadcast message with permissions from " + message.getNanoAppId());
                return (byte) 2;
            }
            com.android.server.location.contexthub.ContextHubEventLogger.getInstance().logMessageFromNanoapp(contextHubId, message, true);
            broadcastMessage(contextHubId, message, nanoappPermissions, messagePermissions);
            return (byte) 0;
        }
        com.android.server.location.contexthub.ContextHubClientBroker proxy = this.mHostEndPointIdToClientMap.get(java.lang.Short.valueOf(hostEndpointId));
        if (proxy == null) {
            com.android.server.location.contexthub.ContextHubEventLogger.getInstance().logMessageFromNanoapp(contextHubId, message, false);
            android.util.Log.e(TAG, "Cannot send message to unregistered client (host endpoint ID = " + ((int) hostEndpointId) + ")");
            return (byte) 4;
        }
        com.android.server.location.contexthub.ContextHubEventLogger.getInstance().logMessageFromNanoapp(contextHubId, message, true);
        return proxy.sendMessageToClient(message, nanoappPermissions, messagePermissions);
    }

    void unregisterClient(short hostEndPointId) {
        com.android.server.location.contexthub.ContextHubClientBroker broker = this.mHostEndPointIdToClientMap.get(java.lang.Short.valueOf(hostEndPointId));
        if (broker != null) {
            int action = broker.isPendingIntentCancelled() ? 2 : 1;
            this.mRegistrationRecordDeque.add(new com.android.server.location.contexthub.ContextHubClientManager.RegistrationRecord(broker.toString(), action));
        }
        if (this.mHostEndPointIdToClientMap.remove(java.lang.Short.valueOf(hostEndPointId)) != null) {
            android.util.Log.d(TAG, "Unregistered client with host endpoint ID " + ((int) hostEndPointId));
        } else {
            android.util.Log.e(TAG, "Cannot unregister non-existing client with host endpoint ID " + ((int) hostEndPointId));
        }
    }

    void onNanoAppLoaded(int contextHubId, final long nanoAppId) {
        forEachClientOfHub(contextHubId, new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.location.contexthub.ContextHubClientBroker) obj).onNanoAppLoaded(nanoAppId);
            }
        });
    }

    void onNanoAppUnloaded(int contextHubId, final long nanoAppId) {
        forEachClientOfHub(contextHubId, new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.location.contexthub.ContextHubClientBroker) obj).onNanoAppUnloaded(nanoAppId);
            }
        });
    }

    void onHubReset(int contextHubId) {
        forEachClientOfHub(contextHubId, new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.location.contexthub.ContextHubClientBroker) obj).onHubReset();
            }
        });
    }

    void onNanoAppAborted(int contextHubId, final long nanoAppId, final int abortCode) {
        forEachClientOfHub(contextHubId, new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.location.contexthub.ContextHubClientBroker) obj).onNanoAppAborted(nanoAppId, abortCode);
            }
        });
    }

    void forEachClientOfHub(int contextHubId, java.util.function.Consumer<com.android.server.location.contexthub.ContextHubClientBroker> callback) {
        for (com.android.server.location.contexthub.ContextHubClientBroker broker : this.mHostEndPointIdToClientMap.values()) {
            if (broker.getAttachedContextHubId() == contextHubId) {
                callback.accept(broker);
            }
        }
    }

    private short getHostEndPointId() {
        if (this.mHostEndPointIdToClientMap.size() == 32768) {
            throw new java.lang.IllegalStateException("Could not register client - max limit exceeded");
        }
        int id = this.mNextHostEndPointId;
        int i = 0;
        while (true) {
            if (i > MAX_CLIENT_ID) {
                break;
            }
            if (!this.mHostEndPointIdToClientMap.containsKey(java.lang.Short.valueOf((short) id))) {
                this.mNextHostEndPointId = id != MAX_CLIENT_ID ? id + 1 : 0;
            } else {
                if (id != MAX_CLIENT_ID) {
                    i = id + 1;
                }
                id = i;
                i++;
            }
        }
        return (short) id;
    }

    private void broadcastMessage(int contextHubId, final android.hardware.location.NanoAppMessage message, final java.util.List<java.lang.String> nanoappPermissions, final java.util.List<java.lang.String> messagePermissions) {
        forEachClientOfHub(contextHubId, new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.location.contexthub.ContextHubClientBroker) obj).sendMessageToClient(message, nanoappPermissions, messagePermissions);
            }
        });
    }

    private com.android.server.location.contexthub.ContextHubClientBroker getClientBroker(int contextHubId, android.app.PendingIntent pendingIntent, long nanoAppId) {
        for (com.android.server.location.contexthub.ContextHubClientBroker broker : this.mHostEndPointIdToClientMap.values()) {
            if (broker.hasPendingIntent(pendingIntent, nanoAppId) && broker.getAttachedContextHubId() == contextHubId) {
                return broker;
            }
        }
        return null;
    }

    void dump(android.util.proto.ProtoOutputStream proto) {
        for (com.android.server.location.contexthub.ContextHubClientBroker broker : this.mHostEndPointIdToClientMap.values()) {
            long token = proto.start(2246267895809L);
            broker.dump(proto);
            proto.end(token);
        }
        java.util.Iterator<com.android.server.location.contexthub.ContextHubClientManager.RegistrationRecord> it = this.mRegistrationRecordDeque.descendingIterator();
        while (it.hasNext()) {
            long token2 = proto.start(2246267895810L);
            it.next().dump(proto);
            proto.end(token2);
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (com.android.server.location.contexthub.ContextHubClientBroker broker : this.mHostEndPointIdToClientMap.values()) {
            sb.append(broker);
            sb.append(java.lang.System.lineSeparator());
        }
        sb.append(java.lang.System.lineSeparator());
        sb.append("Registration History:");
        sb.append(java.lang.System.lineSeparator());
        java.util.Iterator<com.android.server.location.contexthub.ContextHubClientManager.RegistrationRecord> it = this.mRegistrationRecordDeque.descendingIterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append(java.lang.System.lineSeparator());
        }
        return sb.toString();
    }
}
