package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class ConversationStore {
    private static final java.lang.String CONVERSATIONS_FILE_NAME = "conversations";
    private static final int CONVERSATION_INFOS_END_TOKEN = -1;
    private static final java.lang.String TAG = com.android.server.people.data.ConversationStore.class.getSimpleName();
    private com.android.server.people.data.ConversationStore.ConversationInfosProtoDiskReadWriter mConversationInfosProtoDiskReadWriter;
    private final java.io.File mPackageDir;
    private final java.util.concurrent.ScheduledExecutorService mScheduledExecutorService;
    private final java.util.Map<java.lang.String, com.android.server.people.data.ConversationInfo> mConversationInfoMap = new android.util.ArrayMap();
    private final java.util.Map<android.content.LocusId, java.lang.String> mLocusIdToShortcutIdMap = new android.util.ArrayMap();
    private final java.util.Map<android.net.Uri, java.lang.String> mContactUriToShortcutIdMap = new android.util.ArrayMap();
    private final java.util.Map<java.lang.String, java.lang.String> mPhoneNumberToShortcutIdMap = new android.util.ArrayMap();
    private final java.util.Map<java.lang.String, java.lang.String> mNotifChannelIdToShortcutIdMap = new android.util.ArrayMap();

    ConversationStore(java.io.File packageDir, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
        this.mScheduledExecutorService = scheduledExecutorService;
        this.mPackageDir = packageDir;
    }

    void loadConversationsFromDisk() {
        java.util.List<com.android.server.people.data.ConversationInfo> conversationsOnDisk;
        com.android.server.people.data.ConversationStore.ConversationInfosProtoDiskReadWriter conversationInfosProtoDiskReadWriter = getConversationInfosProtoDiskReadWriter();
        if (conversationInfosProtoDiskReadWriter == null || (conversationsOnDisk = conversationInfosProtoDiskReadWriter.read(CONVERSATIONS_FILE_NAME)) == null) {
            return;
        }
        for (com.android.server.people.data.ConversationInfo conversationInfo : conversationsOnDisk) {
            updateConversationsInMemory(conversationInfo);
        }
    }

    void saveConversationsToDisk() {
        java.util.List<com.android.server.people.data.ConversationInfo> conversations;
        com.android.server.people.data.ConversationStore.ConversationInfosProtoDiskReadWriter conversationInfosProtoDiskReadWriter = getConversationInfosProtoDiskReadWriter();
        if (conversationInfosProtoDiskReadWriter != null) {
            synchronized (this) {
                conversations = new java.util.ArrayList<>(this.mConversationInfoMap.values());
            }
            conversationInfosProtoDiskReadWriter.saveConversationsImmediately(conversations);
        }
    }

    void addOrUpdate(com.android.server.people.data.ConversationInfo conversationInfo) {
        updateConversationsInMemory(conversationInfo);
        scheduleUpdateConversationsOnDisk();
    }

    com.android.server.people.data.ConversationInfo deleteConversation(java.lang.String shortcutId) {
        synchronized (this) {
            com.android.server.people.data.ConversationInfo conversationInfo = this.mConversationInfoMap.remove(shortcutId);
            if (conversationInfo == null) {
                return null;
            }
            android.content.LocusId locusId = conversationInfo.getLocusId();
            if (locusId != null) {
                this.mLocusIdToShortcutIdMap.remove(locusId);
            }
            android.net.Uri contactUri = conversationInfo.getContactUri();
            if (contactUri != null) {
                this.mContactUriToShortcutIdMap.remove(contactUri);
            }
            java.lang.String phoneNumber = conversationInfo.getContactPhoneNumber();
            if (phoneNumber != null) {
                this.mPhoneNumberToShortcutIdMap.remove(phoneNumber);
            }
            java.lang.String notifChannelId = conversationInfo.getNotificationChannelId();
            if (notifChannelId != null) {
                this.mNotifChannelIdToShortcutIdMap.remove(notifChannelId);
            }
            scheduleUpdateConversationsOnDisk();
            return conversationInfo;
        }
    }

    void forAllConversations(java.util.function.Consumer<com.android.server.people.data.ConversationInfo> consumer) {
        java.util.List<com.android.server.people.data.ConversationInfo> conversations;
        synchronized (this) {
            conversations = new java.util.ArrayList<>(this.mConversationInfoMap.values());
        }
        for (com.android.server.people.data.ConversationInfo ci : conversations) {
            consumer.accept(ci);
        }
    }

    synchronized com.android.server.people.data.ConversationInfo getConversation(java.lang.String shortcutId) {
        return shortcutId != null ? this.mConversationInfoMap.get(shortcutId) : null;
    }

    synchronized com.android.server.people.data.ConversationInfo getConversationByLocusId(android.content.LocusId locusId) {
        return getConversation(this.mLocusIdToShortcutIdMap.get(locusId));
    }

    synchronized com.android.server.people.data.ConversationInfo getConversationByContactUri(android.net.Uri contactUri) {
        return getConversation(this.mContactUriToShortcutIdMap.get(contactUri));
    }

    synchronized com.android.server.people.data.ConversationInfo getConversationByPhoneNumber(java.lang.String phoneNumber) {
        return getConversation(this.mPhoneNumberToShortcutIdMap.get(phoneNumber));
    }

    synchronized com.android.server.people.data.ConversationInfo getConversationByNotificationChannelId(java.lang.String notifChannelId) {
        return getConversation(this.mNotifChannelIdToShortcutIdMap.get(notifChannelId));
    }

    void onDestroy() {
        synchronized (this) {
            this.mConversationInfoMap.clear();
            this.mContactUriToShortcutIdMap.clear();
            this.mLocusIdToShortcutIdMap.clear();
            this.mNotifChannelIdToShortcutIdMap.clear();
            this.mPhoneNumberToShortcutIdMap.clear();
        }
        com.android.server.people.data.ConversationStore.ConversationInfosProtoDiskReadWriter writer = getConversationInfosProtoDiskReadWriter();
        if (writer != null) {
            writer.deleteConversationsFile();
        }
    }

    byte[] getBackupPayload() {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        final java.io.DataOutputStream conversationInfosOut = new java.io.DataOutputStream(baos);
        forAllConversations(new java.util.function.Consumer() { // from class: com.android.server.people.data.ConversationStore$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.people.data.ConversationStore.lambda$getBackupPayload$0(conversationInfosOut, (com.android.server.people.data.ConversationInfo) obj);
            }
        });
        try {
            conversationInfosOut.writeInt(-1);
            return baos.toByteArray();
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write conversation infos end token to backup payload.", e);
            return null;
        }
    }

    static /* synthetic */ void lambda$getBackupPayload$0(java.io.DataOutputStream conversationInfosOut, com.android.server.people.data.ConversationInfo conversationInfo) {
        byte[] backupPayload = conversationInfo.getBackupPayload();
        if (backupPayload == null) {
            return;
        }
        try {
            conversationInfosOut.writeInt(backupPayload.length);
            conversationInfosOut.write(backupPayload);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write conversation info to backup payload.", e);
        }
    }

    void restore(byte[] payload) {
        java.io.DataInputStream in = new java.io.DataInputStream(new java.io.ByteArrayInputStream(payload));
        try {
            for (int conversationInfoSize = in.readInt(); conversationInfoSize != -1; conversationInfoSize = in.readInt()) {
                byte[] conversationInfoPayload = new byte[conversationInfoSize];
                in.readFully(conversationInfoPayload, 0, conversationInfoSize);
                com.android.server.people.data.ConversationInfo conversationInfo = com.android.server.people.data.ConversationInfo.readFromBackupPayload(conversationInfoPayload);
                if (conversationInfo != null) {
                    addOrUpdate(conversationInfo);
                }
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to read conversation info from payload.", e);
        }
    }

    private synchronized void updateConversationsInMemory(com.android.server.people.data.ConversationInfo conversationInfo) {
        this.mConversationInfoMap.put(conversationInfo.getShortcutId(), conversationInfo);
        android.content.LocusId locusId = conversationInfo.getLocusId();
        if (locusId != null) {
            this.mLocusIdToShortcutIdMap.put(locusId, conversationInfo.getShortcutId());
        }
        android.net.Uri contactUri = conversationInfo.getContactUri();
        if (contactUri != null) {
            this.mContactUriToShortcutIdMap.put(contactUri, conversationInfo.getShortcutId());
        }
        java.lang.String phoneNumber = conversationInfo.getContactPhoneNumber();
        if (phoneNumber != null) {
            this.mPhoneNumberToShortcutIdMap.put(phoneNumber, conversationInfo.getShortcutId());
        }
        java.lang.String notifChannelId = conversationInfo.getNotificationChannelId();
        if (notifChannelId != null) {
            this.mNotifChannelIdToShortcutIdMap.put(notifChannelId, conversationInfo.getShortcutId());
        }
    }

    private void scheduleUpdateConversationsOnDisk() {
        java.util.List<com.android.server.people.data.ConversationInfo> conversations;
        com.android.server.people.data.ConversationStore.ConversationInfosProtoDiskReadWriter conversationInfosProtoDiskReadWriter = getConversationInfosProtoDiskReadWriter();
        if (conversationInfosProtoDiskReadWriter != null) {
            synchronized (this) {
                conversations = new java.util.ArrayList<>(this.mConversationInfoMap.values());
            }
            conversationInfosProtoDiskReadWriter.scheduleConversationsSave(conversations);
        }
    }

    private com.android.server.people.data.ConversationStore.ConversationInfosProtoDiskReadWriter getConversationInfosProtoDiskReadWriter() {
        if (!this.mPackageDir.exists()) {
            android.util.Slog.e(TAG, "Package data directory does not exist: " + this.mPackageDir.getAbsolutePath());
            return null;
        }
        if (this.mConversationInfosProtoDiskReadWriter == null) {
            this.mConversationInfosProtoDiskReadWriter = new com.android.server.people.data.ConversationStore.ConversationInfosProtoDiskReadWriter(this.mPackageDir, CONVERSATIONS_FILE_NAME, this.mScheduledExecutorService);
        }
        return this.mConversationInfosProtoDiskReadWriter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ConversationInfosProtoDiskReadWriter extends com.android.server.people.data.AbstractProtoDiskReadWriter<java.util.List<com.android.server.people.data.ConversationInfo>> {
        private final java.lang.String mConversationInfoFileName;

        ConversationInfosProtoDiskReadWriter(java.io.File rootDir, java.lang.String conversationInfoFileName, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
            super(rootDir, scheduledExecutorService);
            this.mConversationInfoFileName = conversationInfoFileName;
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter<java.util.List<com.android.server.people.data.ConversationInfo>> protoStreamWriter() {
            return new com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter() { // from class: com.android.server.people.data.ConversationStore$ConversationInfosProtoDiskReadWriter$$ExternalSyntheticLambda1
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter
                public final void write(android.util.proto.ProtoOutputStream protoOutputStream, java.lang.Object obj) {
                    com.android.server.people.data.ConversationStore.ConversationInfosProtoDiskReadWriter.lambda$protoStreamWriter$0(protoOutputStream, (java.util.List) obj);
                }
            };
        }

        static /* synthetic */ void lambda$protoStreamWriter$0(android.util.proto.ProtoOutputStream protoOutputStream, java.util.List data) {
            java.util.Iterator it = data.iterator();
            while (it.hasNext()) {
                com.android.server.people.data.ConversationInfo conversationInfo = (com.android.server.people.data.ConversationInfo) it.next();
                long token = protoOutputStream.start(2246267895809L);
                conversationInfo.writeToProto(protoOutputStream);
                protoOutputStream.end(token);
            }
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader<java.util.List<com.android.server.people.data.ConversationInfo>> protoStreamReader() {
            return new com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader() { // from class: com.android.server.people.data.ConversationStore$ConversationInfosProtoDiskReadWriter$$ExternalSyntheticLambda0
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader
                public final java.lang.Object read(android.util.proto.ProtoInputStream protoInputStream) {
                    return com.android.server.people.data.ConversationStore.ConversationInfosProtoDiskReadWriter.lambda$protoStreamReader$1(protoInputStream);
                }
            };
        }

        static /* synthetic */ java.util.List lambda$protoStreamReader$1(android.util.proto.ProtoInputStream protoInputStream) {
            java.util.List<com.android.server.people.data.ConversationInfo> results = com.google.android.collect.Lists.newArrayList();
            while (protoInputStream.nextField() != -1) {
                try {
                    if (protoInputStream.getFieldNumber() == 1) {
                        long token = protoInputStream.start(2246267895809L);
                        com.android.server.people.data.ConversationInfo conversationInfo = com.android.server.people.data.ConversationInfo.readFromProto(protoInputStream);
                        protoInputStream.end(token);
                        results.add(conversationInfo);
                    }
                } catch (java.io.IOException e) {
                    android.util.Slog.e(com.android.server.people.data.ConversationStore.TAG, "Failed to read protobuf input stream.", e);
                }
            }
            return results;
        }

        void scheduleConversationsSave(java.util.List<com.android.server.people.data.ConversationInfo> conversationInfos) {
            scheduleSave(this.mConversationInfoFileName, conversationInfos);
        }

        void saveConversationsImmediately(java.util.List<com.android.server.people.data.ConversationInfo> conversationInfos) {
            saveImmediately(this.mConversationInfoFileName, conversationInfos);
        }

        void deleteConversationsFile() {
            delete(this.mConversationInfoFileName);
        }
    }
}
