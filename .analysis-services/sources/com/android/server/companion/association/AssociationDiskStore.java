package com.android.server.companion.association;

/* JADX INFO: loaded from: classes.dex */
public final class AssociationDiskStore {
    private static final int CURRENT_PERSISTENCE_VERSION = 1;
    private static final java.lang.String FILE_NAME = "companion_device_manager.xml";
    private static final java.lang.String FILE_NAME_LEGACY = "companion_device_manager_associations.xml";
    private static final java.lang.String LEGACY_XML_ATTR_DEVICE = "device";
    private static final java.lang.String TAG = "CDM_AssociationDiskStore";
    private static final java.lang.String XML_ATTR_DISPLAY_NAME = "display_name";
    private static final java.lang.String XML_ATTR_ID = "id";
    private static final java.lang.String XML_ATTR_LAST_TIME_CONNECTED = "last_time_connected";
    private static final java.lang.String XML_ATTR_MAC_ADDRESS = "mac_address";
    private static final java.lang.String XML_ATTR_MAX_ID = "max-id";
    private static final java.lang.String XML_ATTR_NOTIFY_DEVICE_NEARBY = "notify_device_nearby";
    private static final java.lang.String XML_ATTR_PACKAGE = "package";
    private static final java.lang.String XML_ATTR_PENDING = "pending";
    private static final java.lang.String XML_ATTR_PERSISTENCE_VERSION = "persistence-version";
    private static final java.lang.String XML_ATTR_PROFILE = "profile";
    private static final java.lang.String XML_ATTR_REVOKED = "revoked";
    private static final java.lang.String XML_ATTR_SELF_MANAGED = "self_managed";
    private static final java.lang.String XML_ATTR_SYSTEM_DATA_SYNC_FLAGS = "system_data_sync_flags";
    private static final java.lang.String XML_ATTR_TIME_APPROVED = "time_approved";
    private static final java.lang.String XML_TAG_ASSOCIATION = "association";
    private static final java.lang.String XML_TAG_ASSOCIATIONS = "associations";
    private static final java.lang.String XML_TAG_STATE = "state";
    private static final java.lang.String XML_TAG_TAG = "tag";
    private final java.util.concurrent.ConcurrentMap<java.lang.Integer, android.util.AtomicFile> mUserIdToStorageFile = new java.util.concurrent.ConcurrentHashMap();

    public java.util.Map<java.lang.Integer, com.android.server.companion.association.Associations> readAssociationsByUsers(java.util.List<java.lang.Integer> userIds) {
        java.util.Map<java.lang.Integer, com.android.server.companion.association.Associations> userToAssociationsMap = new java.util.HashMap<>();
        java.util.Iterator<java.lang.Integer> it = userIds.iterator();
        while (it.hasNext()) {
            int userId = it.next().intValue();
            userToAssociationsMap.put(java.lang.Integer.valueOf(userId), readAssociationsByUser(userId));
        }
        return userToAssociationsMap;
    }

    private com.android.server.companion.association.Associations readAssociationsByUser(int userId) {
        android.util.AtomicFile readFrom;
        java.lang.String rootTag;
        android.util.Slog.i(TAG, "Reading associations for user " + userId + " from disk.");
        android.util.AtomicFile file = getStorageFileForUser(userId);
        synchronized (file) {
            java.io.File legacyBaseFile = null;
            if (!file.getBaseFile().exists()) {
                legacyBaseFile = getBaseLegacyStorageFileForUser(userId);
                if (!legacyBaseFile.exists()) {
                    return new com.android.server.companion.association.Associations();
                }
                readFrom = new android.util.AtomicFile(legacyBaseFile);
                rootTag = XML_TAG_ASSOCIATIONS;
            } else {
                readFrom = file;
                rootTag = "state";
            }
            com.android.server.companion.association.Associations associations = readAssociationsFromFile(userId, readFrom, rootTag);
            if (legacyBaseFile != null || associations.getVersion() < 1) {
                writeAssociationsToFile(file, associations);
                if (legacyBaseFile != null) {
                    legacyBaseFile.delete();
                }
            }
            return associations;
        }
    }

    public void writeAssociationsForUser(int userId, com.android.server.companion.association.Associations associations) {
        android.util.Slog.i(TAG, "Writing associations for user " + userId + " to disk");
        android.util.AtomicFile file = getStorageFileForUser(userId);
        synchronized (file) {
            writeAssociationsToFile(file, associations);
        }
    }

    private static com.android.server.companion.association.Associations readAssociationsFromFile(int userId, android.util.AtomicFile file, java.lang.String rootTag) {
        try {
            java.io.FileInputStream in = file.openRead();
            try {
                com.android.server.companion.association.Associations associationsFromInputStream = readAssociationsFromInputStream(userId, in, rootTag);
                if (in != null) {
                    in.close();
                }
                return associationsFromInputStream;
            } catch (java.lang.Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Error while reading associations file", e);
            return new com.android.server.companion.association.Associations();
        }
    }

    private static com.android.server.companion.association.Associations readAssociationsFromInputStream(int userId, java.io.InputStream in, java.lang.String rootTag) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
        com.android.internal.util.XmlUtils.beginDocument(parser, rootTag);
        int version = com.android.internal.util.XmlUtils.readIntAttribute(parser, XML_ATTR_PERSISTENCE_VERSION, 0);
        com.android.server.companion.association.Associations associations = new com.android.server.companion.association.Associations();
        switch (version) {
            case 0:
                return readAssociationsV0(parser, userId);
            case 1:
                break;
            default:
                return associations;
        }
        while (true) {
            parser.nextTag();
            if (com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, XML_TAG_ASSOCIATIONS)) {
                associations = readAssociationsV1(parser, userId);
            } else if (com.android.server.companion.utils.DataStoreUtils.isEndOfTag(parser, rootTag)) {
                return associations;
            }
        }
    }

    private void writeAssociationsToFile(android.util.AtomicFile file, final com.android.server.companion.association.Associations associations) {
        com.android.server.companion.utils.DataStoreUtils.writeToFileSafely(file, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.companion.association.AssociationDiskStore$$ExternalSyntheticLambda0
            public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                com.android.server.companion.association.AssociationDiskStore.lambda$writeAssociationsToFile$0(associations, (java.io.FileOutputStream) obj);
            }
        });
    }

    static /* synthetic */ void lambda$writeAssociationsToFile$0(com.android.server.companion.association.Associations associations, java.io.FileOutputStream out) throws java.lang.Exception {
        com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(out);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startDocument((java.lang.String) null, true);
        serializer.startTag((java.lang.String) null, "state");
        com.android.internal.util.XmlUtils.writeIntAttribute(serializer, XML_ATTR_PERSISTENCE_VERSION, 1);
        writeAssociations(serializer, associations);
        serializer.endTag((java.lang.String) null, "state");
        serializer.endDocument();
    }

    private android.util.AtomicFile getStorageFileForUser(final int userId) {
        return this.mUserIdToStorageFile.computeIfAbsent(java.lang.Integer.valueOf(userId), new java.util.function.Function() { // from class: com.android.server.companion.association.AssociationDiskStore$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.companion.utils.DataStoreUtils.createStorageFileForUser(userId, com.android.server.companion.association.AssociationDiskStore.FILE_NAME);
            }
        });
    }

    public byte[] getBackupPayload(int userId) {
        byte[] bArrFileToByteArray;
        android.util.Slog.i(TAG, "Fetching stored state data for user " + userId + " from disk");
        android.util.AtomicFile file = getStorageFileForUser(userId);
        synchronized (file) {
            bArrFileToByteArray = com.android.server.companion.utils.DataStoreUtils.fileToByteArray(file);
        }
        return bArrFileToByteArray;
    }

    public static com.android.server.companion.association.Associations readAssociationsFromPayload(byte[] payload, int userId) {
        try {
            java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(payload);
            try {
                com.android.server.companion.association.Associations associationsFromInputStream = readAssociationsFromInputStream(userId, in, "state");
                in.close();
                return associationsFromInputStream;
            } catch (java.lang.Throwable th) {
                try {
                    in.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Error while reading associations file", e);
            return new com.android.server.companion.association.Associations();
        }
    }

    private static java.io.File getBaseLegacyStorageFileForUser(int userId) {
        return new java.io.File(android.os.Environment.getUserSystemDirectory(userId), FILE_NAME_LEGACY);
    }

    private static com.android.server.companion.association.Associations readAssociationsV0(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        requireStartOfTag(parser, XML_TAG_ASSOCIATIONS);
        int associationId = com.android.server.companion.utils.AssociationUtils.getFirstAssociationIdForUser(userId);
        com.android.server.companion.association.Associations associations = new com.android.server.companion.association.Associations();
        associations.setVersion(0);
        while (true) {
            parser.nextTag();
            if (!com.android.server.companion.utils.DataStoreUtils.isEndOfTag(parser, XML_TAG_ASSOCIATIONS)) {
                if (com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, XML_TAG_ASSOCIATION)) {
                    associations.addAssociation(readAssociationV0(parser, userId, associationId));
                    associationId++;
                }
            } else {
                associations.setMaxId(associationId - 1);
                return associations;
            }
        }
    }

    private static android.companion.AssociationInfo readAssociationV0(com.android.modules.utils.TypedXmlPullParser parser, int userId, int associationId) throws org.xmlpull.v1.XmlPullParserException {
        requireStartOfTag(parser, XML_TAG_ASSOCIATION);
        java.lang.String appPackage = com.android.internal.util.XmlUtils.readStringAttribute(parser, "package");
        java.lang.String tag = com.android.internal.util.XmlUtils.readStringAttribute(parser, XML_TAG_TAG);
        java.lang.String deviceAddress = com.android.internal.util.XmlUtils.readStringAttribute(parser, LEGACY_XML_ATTR_DEVICE);
        java.lang.String profile = com.android.internal.util.XmlUtils.readStringAttribute(parser, XML_ATTR_PROFILE);
        boolean notify = com.android.internal.util.XmlUtils.readBooleanAttribute(parser, XML_ATTR_NOTIFY_DEVICE_NEARBY);
        long timeApproved = com.android.internal.util.XmlUtils.readLongAttribute(parser, XML_ATTR_TIME_APPROVED, 0L);
        return new android.companion.AssociationInfo(associationId, userId, appPackage, tag, android.net.MacAddress.fromString(deviceAddress), null, profile, null, false, notify, false, false, timeApproved, Long.MAX_VALUE, 0);
    }

    private static com.android.server.companion.association.Associations readAssociationsV1(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        requireStartOfTag(parser, XML_TAG_ASSOCIATIONS);
        int maxId = com.android.internal.util.XmlUtils.readIntAttribute(parser, XML_ATTR_MAX_ID, 0);
        com.android.server.companion.association.Associations associations = new com.android.server.companion.association.Associations();
        associations.setVersion(1);
        while (true) {
            parser.nextTag();
            if (!com.android.server.companion.utils.DataStoreUtils.isEndOfTag(parser, XML_TAG_ASSOCIATIONS)) {
                if (com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, XML_TAG_ASSOCIATION)) {
                    android.companion.AssociationInfo association = readAssociationV1(parser, userId);
                    associations.addAssociation(association);
                    maxId = java.lang.Math.max(maxId, association.getId());
                }
            } else {
                associations.setMaxId(maxId);
                return associations;
            }
        }
    }

    private static android.companion.AssociationInfo readAssociationV1(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        requireStartOfTag(parser, XML_TAG_ASSOCIATION);
        int associationId = com.android.internal.util.XmlUtils.readIntAttribute(parser, XML_ATTR_ID);
        java.lang.String profile = com.android.internal.util.XmlUtils.readStringAttribute(parser, XML_ATTR_PROFILE);
        java.lang.String appPackage = com.android.internal.util.XmlUtils.readStringAttribute(parser, "package");
        java.lang.String tag = com.android.internal.util.XmlUtils.readStringAttribute(parser, XML_TAG_TAG);
        android.net.MacAddress macAddress = stringToMacAddress(com.android.internal.util.XmlUtils.readStringAttribute(parser, XML_ATTR_MAC_ADDRESS));
        java.lang.String displayName = com.android.internal.util.XmlUtils.readStringAttribute(parser, XML_ATTR_DISPLAY_NAME);
        boolean selfManaged = com.android.internal.util.XmlUtils.readBooleanAttribute(parser, XML_ATTR_SELF_MANAGED);
        boolean notify = com.android.internal.util.XmlUtils.readBooleanAttribute(parser, XML_ATTR_NOTIFY_DEVICE_NEARBY);
        boolean revoked = com.android.internal.util.XmlUtils.readBooleanAttribute(parser, XML_ATTR_REVOKED, false);
        boolean pending = com.android.internal.util.XmlUtils.readBooleanAttribute(parser, XML_ATTR_PENDING, false);
        long timeApproved = com.android.internal.util.XmlUtils.readLongAttribute(parser, XML_ATTR_TIME_APPROVED, 0L);
        long lastTimeConnected = com.android.internal.util.XmlUtils.readLongAttribute(parser, XML_ATTR_LAST_TIME_CONNECTED, Long.MAX_VALUE);
        int systemDataSyncFlags = com.android.internal.util.XmlUtils.readIntAttribute(parser, XML_ATTR_SYSTEM_DATA_SYNC_FLAGS, 0);
        return new android.companion.AssociationInfo(associationId, userId, appPackage, tag, macAddress, displayName, profile, null, selfManaged, notify, revoked, pending, timeApproved, lastTimeConnected, systemDataSyncFlags);
    }

    private static void writeAssociations(org.xmlpull.v1.XmlSerializer parent, com.android.server.companion.association.Associations associations) throws java.io.IOException {
        org.xmlpull.v1.XmlSerializer serializer = parent.startTag(null, XML_TAG_ASSOCIATIONS);
        for (android.companion.AssociationInfo association : associations.getAssociations()) {
            writeAssociation(serializer, association);
        }
        com.android.internal.util.XmlUtils.writeIntAttribute(serializer, XML_ATTR_MAX_ID, associations.getMaxId());
        serializer.endTag(null, XML_TAG_ASSOCIATIONS);
    }

    private static void writeAssociation(org.xmlpull.v1.XmlSerializer parent, android.companion.AssociationInfo a) throws java.io.IOException {
        org.xmlpull.v1.XmlSerializer serializer = parent.startTag(null, XML_TAG_ASSOCIATION);
        com.android.internal.util.XmlUtils.writeIntAttribute(serializer, XML_ATTR_ID, a.getId());
        com.android.internal.util.XmlUtils.writeStringAttribute(serializer, XML_ATTR_PROFILE, a.getDeviceProfile());
        com.android.internal.util.XmlUtils.writeStringAttribute(serializer, "package", a.getPackageName());
        com.android.internal.util.XmlUtils.writeStringAttribute(serializer, XML_TAG_TAG, a.getTag());
        com.android.internal.util.XmlUtils.writeStringAttribute(serializer, XML_ATTR_MAC_ADDRESS, a.getDeviceMacAddressAsString());
        com.android.internal.util.XmlUtils.writeStringAttribute(serializer, XML_ATTR_DISPLAY_NAME, a.getDisplayName());
        com.android.internal.util.XmlUtils.writeBooleanAttribute(serializer, XML_ATTR_SELF_MANAGED, a.isSelfManaged());
        com.android.internal.util.XmlUtils.writeBooleanAttribute(serializer, XML_ATTR_NOTIFY_DEVICE_NEARBY, a.isNotifyOnDeviceNearby());
        com.android.internal.util.XmlUtils.writeBooleanAttribute(serializer, XML_ATTR_REVOKED, a.isRevoked());
        com.android.internal.util.XmlUtils.writeBooleanAttribute(serializer, XML_ATTR_PENDING, a.isPending());
        com.android.internal.util.XmlUtils.writeLongAttribute(serializer, XML_ATTR_TIME_APPROVED, a.getTimeApprovedMs());
        com.android.internal.util.XmlUtils.writeLongAttribute(serializer, XML_ATTR_LAST_TIME_CONNECTED, a.getLastTimeConnectedMs());
        com.android.internal.util.XmlUtils.writeIntAttribute(serializer, XML_ATTR_SYSTEM_DATA_SYNC_FLAGS, a.getSystemDataSyncFlags());
        serializer.endTag(null, XML_TAG_ASSOCIATION);
    }

    private static void requireStartOfTag(org.xmlpull.v1.XmlPullParser parser, java.lang.String tag) throws org.xmlpull.v1.XmlPullParserException {
        if (!com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, tag)) {
            throw new org.xmlpull.v1.XmlPullParserException("Should be at the start of \"associations\" tag");
        }
    }

    private static android.net.MacAddress stringToMacAddress(java.lang.String address) {
        if (address != null) {
            return android.net.MacAddress.fromString(address);
        }
        return null;
    }
}
