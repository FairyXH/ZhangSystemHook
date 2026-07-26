package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class OwnersData {
    private static final java.lang.String ATTR_CAN_ACCESS_DEVICE_IDS = "canAccessDeviceIds";
    private static final java.lang.String ATTR_COMPONENT_NAME = "component";
    private static final java.lang.String ATTR_DEVICE_OWNER_TYPE_VALUE = "value";
    private static final java.lang.String ATTR_FREEZE_RECORD_END = "end";
    private static final java.lang.String ATTR_FREEZE_RECORD_START = "start";
    private static final java.lang.String ATTR_MIGRATED_POST_UPGRADE = "migratedPostUpgrade";
    private static final java.lang.String ATTR_MIGRATED_TO_POLICY_ENGINE = "migratedToPolicyEngine";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_PACKAGE = "package";
    private static final java.lang.String ATTR_PROFILE_OWNER_OF_ORG_OWNED_DEVICE = "isPoOrganizationOwnedDevice";
    private static final java.lang.String ATTR_REMOTE_BUGREPORT_HASH = "remoteBugreportHash";
    private static final java.lang.String ATTR_REMOTE_BUGREPORT_URI = "remoteBugreportUri";
    private static final java.lang.String ATTR_REQUIRED_PASSWORD_COMPLEXITY_MIGRATED = "passwordComplexityMigrated";
    private static final java.lang.String ATTR_SECURITY_LOG_MIGRATED = "securityLogMigrated";
    private static final java.lang.String ATTR_SIZE = "size";
    private static final java.lang.String ATTR_SUSPENDED_PACKAGES_MIGRATED = "suspendedPackagesMigrated";
    private static final java.lang.String ATTR_USERID = "userId";
    private static final boolean DEBUG = false;
    private static final java.lang.String DEVICE_OWNER_XML = "device_owner_2.xml";
    private static final java.lang.String PROFILE_OWNER_XML = "profile_owner.xml";
    private static final java.lang.String TAG = "DevicePolicyManagerService";
    private static final java.lang.String TAG_DEVICE_OWNER = "device-owner";
    private static final java.lang.String TAG_DEVICE_OWNER_CONTEXT = "device-owner-context";
    private static final java.lang.String TAG_DEVICE_OWNER_PROTECTED_PACKAGES = "device-owner-protected-packages";
    private static final java.lang.String TAG_DEVICE_OWNER_TYPE = "device-owner-type";
    private static final java.lang.String TAG_FREEZE_PERIOD_RECORD = "freeze-record";
    private static final java.lang.String TAG_PENDING_OTA_INFO = "pending-ota-info";
    private static final java.lang.String TAG_POLICY_ENGINE_MIGRATION = "policy-engine-migration";
    private static final java.lang.String TAG_PROFILE_OWNER = "profile-owner";
    private static final java.lang.String TAG_ROOT = "root";
    private static final java.lang.String TAG_SYSTEM_UPDATE_POLICY = "system-update-policy";
    com.android.server.devicepolicy.OwnersData.OwnerInfo mDeviceOwner;

    @java.lang.Deprecated
    android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> mDeviceOwnerProtectedPackages;
    private final com.android.server.devicepolicy.PolicyPathProvider mPathProvider;
    java.time.LocalDate mSystemUpdateFreezeEnd;
    java.time.LocalDate mSystemUpdateFreezeStart;
    android.app.admin.SystemUpdateInfo mSystemUpdateInfo;
    android.app.admin.SystemUpdatePolicy mSystemUpdatePolicy;
    int mDeviceOwnerUserId = -10000;
    final android.util.ArrayMap<java.lang.String, java.lang.Integer> mDeviceOwnerTypes = new android.util.ArrayMap<>();
    final android.util.ArrayMap<java.lang.Integer, com.android.server.devicepolicy.OwnersData.OwnerInfo> mProfileOwners = new android.util.ArrayMap<>();
    boolean mMigratedToPolicyEngine = false;
    boolean mSecurityLoggingMigrated = false;
    boolean mRequiredPasswordComplexityMigrated = false;
    boolean mSuspendedPackagesMigrated = false;
    boolean mPoliciesMigratedPostUpdate = false;

    OwnersData(com.android.server.devicepolicy.PolicyPathProvider pathProvider) {
        this.mPathProvider = pathProvider;
    }

    void load(int[] allUsers) {
        new com.android.server.devicepolicy.OwnersData.DeviceOwnerReadWriter().readFromFileLocked();
        for (int userId : allUsers) {
            new com.android.server.devicepolicy.OwnersData.ProfileOwnerReadWriter(userId).readFromFileLocked();
        }
        com.android.server.devicepolicy.OwnersData.OwnerInfo profileOwner = this.mProfileOwners.get(java.lang.Integer.valueOf(this.mDeviceOwnerUserId));
        android.content.ComponentName admin = profileOwner != null ? profileOwner.admin : null;
        if (this.mDeviceOwner != null && admin != null) {
            android.util.Slog.w(TAG, java.lang.String.format("User %d has both DO and PO, which is not supported", java.lang.Integer.valueOf(this.mDeviceOwnerUserId)));
        }
    }

    boolean writeDeviceOwner() {
        return new com.android.server.devicepolicy.OwnersData.DeviceOwnerReadWriter().writeToFileLocked();
    }

    boolean writeProfileOwner(int userId) {
        return new com.android.server.devicepolicy.OwnersData.ProfileOwnerReadWriter(userId).writeToFileLocked();
    }

    void dump(android.util.IndentingPrintWriter pw) {
        boolean needBlank = false;
        if (this.mDeviceOwner != null) {
            pw.println("Device Owner: ");
            pw.increaseIndent();
            this.mDeviceOwner.dump(pw);
            pw.println("User ID: " + this.mDeviceOwnerUserId);
            pw.decreaseIndent();
            needBlank = true;
        }
        if (this.mSystemUpdatePolicy != null) {
            if (needBlank) {
                pw.println();
            }
            pw.println("System Update Policy: " + this.mSystemUpdatePolicy);
            needBlank = true;
        }
        if (this.mProfileOwners != null) {
            for (java.util.Map.Entry<java.lang.Integer, com.android.server.devicepolicy.OwnersData.OwnerInfo> entry : this.mProfileOwners.entrySet()) {
                if (needBlank) {
                    pw.println();
                }
                pw.println("Profile Owner (User " + entry.getKey() + "): ");
                pw.increaseIndent();
                entry.getValue().dump(pw);
                pw.decreaseIndent();
                needBlank = true;
            }
        }
        if (this.mSystemUpdateInfo != null) {
            if (needBlank) {
                pw.println();
            }
            pw.println("Pending System Update: " + this.mSystemUpdateInfo);
            needBlank = true;
        }
        if (this.mSystemUpdateFreezeStart != null || this.mSystemUpdateFreezeEnd != null) {
            if (needBlank) {
                pw.println();
            }
            pw.println("System update freeze record: " + getSystemUpdateFreezePeriodRecordAsString());
        }
    }

    java.lang.String getSystemUpdateFreezePeriodRecordAsString() {
        java.lang.StringBuilder freezePeriodRecord = new java.lang.StringBuilder();
        freezePeriodRecord.append("start: ");
        if (this.mSystemUpdateFreezeStart != null) {
            freezePeriodRecord.append(this.mSystemUpdateFreezeStart.toString());
        } else {
            freezePeriodRecord.append("null");
        }
        freezePeriodRecord.append("; end: ");
        if (this.mSystemUpdateFreezeEnd != null) {
            freezePeriodRecord.append(this.mSystemUpdateFreezeEnd.toString());
        } else {
            freezePeriodRecord.append("null");
        }
        return freezePeriodRecord.toString();
    }

    java.io.File getDeviceOwnerFile() {
        return new java.io.File(this.mPathProvider.getDataSystemDirectory(), DEVICE_OWNER_XML);
    }

    java.io.File getProfileOwnerFile(int userId) {
        return new java.io.File(this.mPathProvider.getUserSystemDirectory(userId), PROFILE_OWNER_XML);
    }

    private static abstract class FileReadWriter {
        private final java.io.File mFile;

        abstract boolean readInner(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser, int i, java.lang.String str);

        abstract boolean shouldWrite();

        abstract void writeInner(com.android.modules.utils.TypedXmlSerializer typedXmlSerializer) throws java.io.IOException;

        protected FileReadWriter(java.io.File file) {
            this.mFile = file;
        }

        boolean writeToFileLocked() {
            if (!shouldWrite()) {
                if (this.mFile.exists() && !this.mFile.delete()) {
                    android.util.Slog.e(com.android.server.devicepolicy.OwnersData.TAG, "Failed to remove " + this.mFile.getPath());
                }
                return true;
            }
            android.util.AtomicFile f = new android.util.AtomicFile(this.mFile);
            java.io.FileOutputStream outputStream = null;
            try {
                outputStream = f.startWrite();
                com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(outputStream);
                out.startDocument((java.lang.String) null, true);
                out.startTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_ROOT);
                writeInner(out);
                out.endTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_ROOT);
                out.endDocument();
                out.flush();
                f.finishWrite(outputStream);
                return true;
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.devicepolicy.OwnersData.TAG, "Exception when writing", e);
                if (outputStream != null) {
                    f.failWrite(outputStream);
                    return false;
                }
                return false;
            }
        }

        void readFromFileLocked() {
            if (!this.mFile.exists()) {
                return;
            }
            android.util.AtomicFile f = new android.util.AtomicFile(this.mFile);
            java.io.InputStream input = null;
            try {
                try {
                    input = f.openRead();
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(input);
                    int depth = 0;
                    while (true) {
                        int type = parser.next();
                        if (type != 1) {
                            switch (type) {
                                case 2:
                                    depth++;
                                    java.lang.String tag = parser.getName();
                                    if (depth == 1) {
                                        if (!com.android.server.devicepolicy.OwnersData.TAG_ROOT.equals(tag)) {
                                            android.util.Slog.e(com.android.server.devicepolicy.OwnersData.TAG, "Invalid root tag: " + tag);
                                            return;
                                        }
                                    } else if (!readInner(parser, depth, tag)) {
                                        return;
                                    }
                                case 3:
                                    depth--;
                                    break;
                            }
                        }
                    }
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    android.util.Slog.e(com.android.server.devicepolicy.OwnersData.TAG, "Error parsing owners information file", e);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(input);
            }
        }
    }

    private class DeviceOwnerReadWriter extends com.android.server.devicepolicy.OwnersData.FileReadWriter {
        protected DeviceOwnerReadWriter() {
            super(com.android.server.devicepolicy.OwnersData.this.getDeviceOwnerFile());
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        boolean shouldWrite() {
            return (!android.app.admin.flags.Flags.alwaysPersistDo() && com.android.server.devicepolicy.OwnersData.this.mDeviceOwner == null && com.android.server.devicepolicy.OwnersData.this.mSystemUpdatePolicy == null && com.android.server.devicepolicy.OwnersData.this.mSystemUpdateInfo == null) ? false : true;
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        void writeInner(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            if (com.android.server.devicepolicy.OwnersData.this.mDeviceOwner != null) {
                com.android.server.devicepolicy.OwnersData.this.mDeviceOwner.writeToXml(out, com.android.server.devicepolicy.OwnersData.TAG_DEVICE_OWNER);
                out.startTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_DEVICE_OWNER_CONTEXT);
                out.attributeInt((java.lang.String) null, "userId", com.android.server.devicepolicy.OwnersData.this.mDeviceOwnerUserId);
                out.endTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_DEVICE_OWNER_CONTEXT);
            }
            if (!com.android.server.devicepolicy.OwnersData.this.mDeviceOwnerTypes.isEmpty()) {
                for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : com.android.server.devicepolicy.OwnersData.this.mDeviceOwnerTypes.entrySet()) {
                    out.startTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_DEVICE_OWNER_TYPE);
                    out.attribute((java.lang.String) null, "package", entry.getKey());
                    out.attributeInt((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_DEVICE_OWNER_TYPE_VALUE, entry.getValue().intValue());
                    out.endTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_DEVICE_OWNER_TYPE);
                }
            }
            if (com.android.server.devicepolicy.OwnersData.this.mSystemUpdatePolicy != null) {
                out.startTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_SYSTEM_UPDATE_POLICY);
                com.android.server.devicepolicy.OwnersData.this.mSystemUpdatePolicy.saveToXml(out);
                out.endTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_SYSTEM_UPDATE_POLICY);
            }
            if (com.android.server.devicepolicy.OwnersData.this.mSystemUpdateInfo != null) {
                com.android.server.devicepolicy.OwnersData.this.mSystemUpdateInfo.writeToXml(out, com.android.server.devicepolicy.OwnersData.TAG_PENDING_OTA_INFO);
            }
            if (com.android.server.devicepolicy.OwnersData.this.mSystemUpdateFreezeStart != null || com.android.server.devicepolicy.OwnersData.this.mSystemUpdateFreezeEnd != null) {
                out.startTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_FREEZE_PERIOD_RECORD);
                if (com.android.server.devicepolicy.OwnersData.this.mSystemUpdateFreezeStart != null) {
                    out.attribute((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_FREEZE_RECORD_START, com.android.server.devicepolicy.OwnersData.this.mSystemUpdateFreezeStart.toString());
                }
                if (com.android.server.devicepolicy.OwnersData.this.mSystemUpdateFreezeEnd != null) {
                    out.attribute((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_FREEZE_RECORD_END, com.android.server.devicepolicy.OwnersData.this.mSystemUpdateFreezeEnd.toString());
                }
                out.endTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_FREEZE_PERIOD_RECORD);
            }
            out.startTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_POLICY_ENGINE_MIGRATION);
            out.attributeBoolean((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_MIGRATED_TO_POLICY_ENGINE, com.android.server.devicepolicy.OwnersData.this.mMigratedToPolicyEngine);
            out.attributeBoolean((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_MIGRATED_POST_UPGRADE, com.android.server.devicepolicy.OwnersData.this.mPoliciesMigratedPostUpdate);
            if (android.app.admin.flags.Flags.securityLogV2Enabled()) {
                out.attributeBoolean((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_SECURITY_LOG_MIGRATED, com.android.server.devicepolicy.OwnersData.this.mSecurityLoggingMigrated);
            }
            if (android.app.admin.flags.Flags.unmanagedModeMigration()) {
                out.attributeBoolean((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_REQUIRED_PASSWORD_COMPLEXITY_MIGRATED, com.android.server.devicepolicy.OwnersData.this.mRequiredPasswordComplexityMigrated);
                out.attributeBoolean((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_SUSPENDED_PACKAGES_MIGRATED, com.android.server.devicepolicy.OwnersData.this.mSuspendedPackagesMigrated);
            }
            out.endTag((java.lang.String) null, com.android.server.devicepolicy.OwnersData.TAG_POLICY_ENGINE_MIGRATION);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:32:0x0061  */
        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        boolean readInner(com.android.modules.utils.TypedXmlPullParser r9, int r10, java.lang.String r11) {
            /*
                Method dump skipped, instruction units count: 480
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.OwnersData.DeviceOwnerReadWriter.readInner(com.android.modules.utils.TypedXmlPullParser, int, java.lang.String):boolean");
        }
    }

    private class ProfileOwnerReadWriter extends com.android.server.devicepolicy.OwnersData.FileReadWriter {
        private final int mUserId;

        ProfileOwnerReadWriter(int userId) {
            super(com.android.server.devicepolicy.OwnersData.this.getProfileOwnerFile(userId));
            this.mUserId = userId;
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        boolean shouldWrite() {
            return com.android.server.devicepolicy.OwnersData.this.mProfileOwners.get(java.lang.Integer.valueOf(this.mUserId)) != null;
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        void writeInner(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            com.android.server.devicepolicy.OwnersData.OwnerInfo profileOwner = com.android.server.devicepolicy.OwnersData.this.mProfileOwners.get(java.lang.Integer.valueOf(this.mUserId));
            if (profileOwner != null) {
                profileOwner.writeToXml(out, com.android.server.devicepolicy.OwnersData.TAG_PROFILE_OWNER);
            }
        }

        @Override // com.android.server.devicepolicy.OwnersData.FileReadWriter
        boolean readInner(com.android.modules.utils.TypedXmlPullParser parser, int depth, java.lang.String tag) {
            byte b;
            if (depth > 2) {
                return true;
            }
            switch (tag.hashCode()) {
                case 2145316239:
                    if (tag.equals(com.android.server.devicepolicy.OwnersData.TAG_PROFILE_OWNER)) {
                        b = 0;
                        break;
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    com.android.server.devicepolicy.OwnersData.this.mProfileOwners.put(java.lang.Integer.valueOf(this.mUserId), com.android.server.devicepolicy.OwnersData.OwnerInfo.readFromXml(parser));
                    return true;
                default:
                    android.util.Slog.e(com.android.server.devicepolicy.OwnersData.TAG, "Unexpected tag: " + tag);
                    return false;
            }
        }
    }

    static class OwnerInfo {
        public final android.content.ComponentName admin;
        public boolean isOrganizationOwnedDevice;
        public final java.lang.String packageName;
        public java.lang.String remoteBugreportHash;
        public java.lang.String remoteBugreportUri;

        OwnerInfo(android.content.ComponentName admin, java.lang.String remoteBugreportUri, java.lang.String remoteBugreportHash, boolean isOrganizationOwnedDevice) {
            this.admin = admin;
            this.packageName = admin.getPackageName();
            this.remoteBugreportUri = remoteBugreportUri;
            this.remoteBugreportHash = remoteBugreportHash;
            this.isOrganizationOwnedDevice = isOrganizationOwnedDevice;
        }

        public void writeToXml(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag) throws java.io.IOException {
            out.startTag((java.lang.String) null, tag);
            if (this.admin != null) {
                out.attribute((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_COMPONENT_NAME, this.admin.flattenToString());
            }
            if (this.remoteBugreportUri != null) {
                out.attribute((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_REMOTE_BUGREPORT_URI, this.remoteBugreportUri);
            }
            if (this.remoteBugreportHash != null) {
                out.attribute((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_REMOTE_BUGREPORT_HASH, this.remoteBugreportHash);
            }
            if (this.isOrganizationOwnedDevice) {
                out.attributeBoolean((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_PROFILE_OWNER_OF_ORG_OWNED_DEVICE, this.isOrganizationOwnedDevice);
            }
            out.endTag((java.lang.String) null, tag);
        }

        public static com.android.server.devicepolicy.OwnersData.OwnerInfo readFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
            java.lang.String componentName = parser.getAttributeValue((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_COMPONENT_NAME);
            java.lang.String remoteBugreportUri = parser.getAttributeValue((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_REMOTE_BUGREPORT_URI);
            java.lang.String remoteBugreportHash = parser.getAttributeValue((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_REMOTE_BUGREPORT_HASH);
            java.lang.String canAccessDeviceIdsStr = parser.getAttributeValue((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_CAN_ACCESS_DEVICE_IDS);
            boolean canAccessDeviceIds = "true".equals(canAccessDeviceIdsStr);
            java.lang.String isOrgOwnedDeviceStr = parser.getAttributeValue((java.lang.String) null, com.android.server.devicepolicy.OwnersData.ATTR_PROFILE_OWNER_OF_ORG_OWNED_DEVICE);
            boolean isOrgOwnedDevice = "true".equals(isOrgOwnedDeviceStr) | canAccessDeviceIds;
            if (componentName == null) {
                android.util.Slog.e(com.android.server.devicepolicy.OwnersData.TAG, "Owner component not found");
                return null;
            }
            android.content.ComponentName admin = android.content.ComponentName.unflattenFromString(componentName);
            if (admin == null) {
                android.util.Slog.e(com.android.server.devicepolicy.OwnersData.TAG, "Owner component not parsable: " + componentName);
                return null;
            }
            return new com.android.server.devicepolicy.OwnersData.OwnerInfo(admin, remoteBugreportUri, remoteBugreportHash, isOrgOwnedDevice);
        }

        public void dump(android.util.IndentingPrintWriter pw) {
            pw.println("admin=" + this.admin);
            pw.println("package=" + this.packageName);
            pw.println("isOrganizationOwnedDevice=" + this.isOrganizationOwnedDevice);
        }
    }
}
