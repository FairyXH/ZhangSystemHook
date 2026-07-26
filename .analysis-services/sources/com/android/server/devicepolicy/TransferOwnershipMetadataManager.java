package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class TransferOwnershipMetadataManager {
    static final java.lang.String ADMIN_TYPE_DEVICE_OWNER = "device-owner";
    static final java.lang.String ADMIN_TYPE_PROFILE_OWNER = "profile-owner";
    public static final java.lang.String OWNER_TRANSFER_METADATA_XML = "owner-transfer-metadata.xml";
    private static final java.lang.String TAG = com.android.server.devicepolicy.TransferOwnershipMetadataManager.class.getName();
    static final java.lang.String TAG_ADMIN_TYPE = "admin-type";
    static final java.lang.String TAG_SOURCE_COMPONENT = "source-component";
    static final java.lang.String TAG_TARGET_COMPONENT = "target-component";
    static final java.lang.String TAG_USER_ID = "user-id";
    private final com.android.server.devicepolicy.TransferOwnershipMetadataManager.Injector mInjector;

    TransferOwnershipMetadataManager() {
        this(new com.android.server.devicepolicy.TransferOwnershipMetadataManager.Injector());
    }

    TransferOwnershipMetadataManager(com.android.server.devicepolicy.TransferOwnershipMetadataManager.Injector injector) {
        this.mInjector = injector;
    }

    boolean saveMetadataFile(com.android.server.devicepolicy.TransferOwnershipMetadataManager.Metadata params) {
        java.io.File transferOwnershipMetadataFile = new java.io.File(this.mInjector.getOwnerTransferMetadataDir(), OWNER_TRANSFER_METADATA_XML);
        android.util.AtomicFile atomicFile = new android.util.AtomicFile(transferOwnershipMetadataFile);
        java.io.FileOutputStream stream = null;
        try {
            stream = atomicFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(stream);
            serializer.startDocument((java.lang.String) null, true);
            insertSimpleTag(serializer, TAG_USER_ID, java.lang.Integer.toString(params.userId));
            insertSimpleTag(serializer, TAG_SOURCE_COMPONENT, params.sourceComponent.flattenToString());
            insertSimpleTag(serializer, TAG_TARGET_COMPONENT, params.targetComponent.flattenToString());
            insertSimpleTag(serializer, TAG_ADMIN_TYPE, params.adminType);
            serializer.endDocument();
            atomicFile.finishWrite(stream);
            return true;
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Caught exception while trying to save Owner Transfer Params to file " + transferOwnershipMetadataFile, e);
            transferOwnershipMetadataFile.delete();
            atomicFile.failWrite(stream);
            return false;
        }
    }

    private void insertSimpleTag(com.android.modules.utils.TypedXmlSerializer serializer, java.lang.String tagName, java.lang.String value) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, tagName);
        serializer.text(value);
        serializer.endTag((java.lang.String) null, tagName);
    }

    com.android.server.devicepolicy.TransferOwnershipMetadataManager.Metadata loadMetadataFile() {
        java.io.File transferOwnershipMetadataFile = new java.io.File(this.mInjector.getOwnerTransferMetadataDir(), OWNER_TRANSFER_METADATA_XML);
        if (!transferOwnershipMetadataFile.exists()) {
            return null;
        }
        android.util.Slog.d(TAG, "Loading TransferOwnershipMetadataManager from " + transferOwnershipMetadataFile);
        try {
            java.io.FileInputStream stream = new java.io.FileInputStream(transferOwnershipMetadataFile);
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(stream);
                com.android.server.devicepolicy.TransferOwnershipMetadataManager.Metadata metadataFile = parseMetadataFile(parser);
                stream.close();
                return metadataFile;
            } catch (java.lang.Throwable th) {
                try {
                    stream.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | java.lang.IllegalArgumentException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Caught exception while trying to load the owner transfer params from file " + transferOwnershipMetadataFile, e);
            return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0055  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.android.server.devicepolicy.TransferOwnershipMetadataManager.Metadata parseMetadataFile(com.android.modules.utils.TypedXmlPullParser r11) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r10 = this;
            int r0 = r11.getDepth()
            r1 = 0
            r2 = 0
            r3 = 0
            r4 = 0
        L8:
            int r5 = r11.next()
            r6 = r5
            r7 = 1
            if (r5 == r7) goto L7f
            r5 = 3
            if (r6 != r5) goto L19
            int r8 = r11.getDepth()
            if (r8 <= r0) goto L7f
        L19:
            if (r6 == r5) goto L8
            r8 = 4
            if (r6 != r8) goto L1f
            goto L8
        L1f:
            java.lang.String r8 = r11.getName()
            int r9 = r8.hashCode()
            switch(r9) {
                case -337219647: goto L4b;
                case -147180963: goto L40;
                case 281362891: goto L35;
                case 641951480: goto L2b;
                default: goto L2a;
            }
        L2a:
            goto L55
        L2b:
            java.lang.String r7 = "admin-type"
            boolean r7 = r8.equals(r7)
            if (r7 == 0) goto L2a
            r7 = r5
            goto L56
        L35:
            java.lang.String r5 = "source-component"
            boolean r5 = r8.equals(r5)
            if (r5 == 0) goto L2a
            r7 = 2
            goto L56
        L40:
            java.lang.String r5 = "user-id"
            boolean r5 = r8.equals(r5)
            if (r5 == 0) goto L2a
            r7 = 0
            goto L56
        L4b:
            java.lang.String r5 = "target-component"
            boolean r5 = r8.equals(r5)
            if (r5 == 0) goto L2a
            goto L56
        L55:
            r7 = -1
        L56:
            switch(r7) {
                case 0: goto L72;
                case 1: goto L6a;
                case 2: goto L62;
                case 3: goto L5a;
                default: goto L59;
            }
        L59:
            goto L7e
        L5a:
            r11.next()
            java.lang.String r4 = r11.getText()
            goto L7e
        L62:
            r11.next()
            java.lang.String r2 = r11.getText()
            goto L7e
        L6a:
            r11.next()
            java.lang.String r3 = r11.getText()
            goto L7e
        L72:
            r11.next()
            java.lang.String r5 = r11.getText()
            int r1 = java.lang.Integer.parseInt(r5)
        L7e:
            goto L8
        L7f:
            com.android.server.devicepolicy.TransferOwnershipMetadataManager$Metadata r5 = new com.android.server.devicepolicy.TransferOwnershipMetadataManager$Metadata
            r5.<init>(r2, r3, r1, r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.TransferOwnershipMetadataManager.parseMetadataFile(com.android.modules.utils.TypedXmlPullParser):com.android.server.devicepolicy.TransferOwnershipMetadataManager$Metadata");
    }

    void deleteMetadataFile() {
        new java.io.File(this.mInjector.getOwnerTransferMetadataDir(), OWNER_TRANSFER_METADATA_XML).delete();
    }

    boolean metadataFileExists() {
        return new java.io.File(this.mInjector.getOwnerTransferMetadataDir(), OWNER_TRANSFER_METADATA_XML).exists();
    }

    static class Metadata {
        final java.lang.String adminType;
        final android.content.ComponentName sourceComponent;
        final android.content.ComponentName targetComponent;
        final int userId;

        Metadata(android.content.ComponentName sourceComponent, android.content.ComponentName targetComponent, int userId, java.lang.String adminType) {
            this.sourceComponent = sourceComponent;
            this.targetComponent = targetComponent;
            java.util.Objects.requireNonNull(sourceComponent);
            java.util.Objects.requireNonNull(targetComponent);
            com.android.internal.util.Preconditions.checkStringNotEmpty(adminType);
            this.userId = userId;
            this.adminType = adminType;
        }

        Metadata(java.lang.String flatSourceComponent, java.lang.String flatTargetComponent, int userId, java.lang.String adminType) {
            this(unflattenComponentUnchecked(flatSourceComponent), unflattenComponentUnchecked(flatTargetComponent), userId, adminType);
        }

        private static android.content.ComponentName unflattenComponentUnchecked(java.lang.String flatComponent) {
            java.util.Objects.requireNonNull(flatComponent);
            return android.content.ComponentName.unflattenFromString(flatComponent);
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.devicepolicy.TransferOwnershipMetadataManager.Metadata)) {
                return false;
            }
            com.android.server.devicepolicy.TransferOwnershipMetadataManager.Metadata params = (com.android.server.devicepolicy.TransferOwnershipMetadataManager.Metadata) obj;
            return this.userId == params.userId && this.sourceComponent.equals(params.sourceComponent) && this.targetComponent.equals(params.targetComponent) && android.text.TextUtils.equals(this.adminType, params.adminType);
        }

        public int hashCode() {
            int hashCode = (1 * 31) + this.userId;
            return (((((hashCode * 31) + this.sourceComponent.hashCode()) * 31) + this.targetComponent.hashCode()) * 31) + this.adminType.hashCode();
        }
    }

    static class Injector {
        Injector() {
        }

        public java.io.File getOwnerTransferMetadataDir() {
            return android.os.Environment.getDataSystemDirectory();
        }
    }
}
