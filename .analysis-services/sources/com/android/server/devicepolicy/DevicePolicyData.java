package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class DevicePolicyData {
    private static final java.lang.String ATTR_ALIAS = "alias";
    private static final java.lang.String ATTR_DEVICE_PAIRED = "device-paired";
    private static final java.lang.String ATTR_DEVICE_PROVISIONING_CONFIG_APPLIED = "device-provisioning-config-applied";
    private static final java.lang.String ATTR_DISABLED = "disabled";
    private static final java.lang.String ATTR_FACTORY_RESET_FLAGS = "factory-reset-flags";
    private static final java.lang.String ATTR_FACTORY_RESET_REASON = "factory-reset-reason";
    private static final java.lang.String ATTR_ID = "id";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_NEW_USER_DISCLAIMER = "new-user-disclaimer";
    private static final java.lang.String ATTR_PERMISSION_POLICY = "permission-policy";
    private static final java.lang.String ATTR_PERMISSION_PROVIDER = "permission-provider";
    private static final java.lang.String ATTR_PROVISIONING_STATE = "provisioning-state";
    private static final java.lang.String ATTR_SETUP_COMPLETE = "setup-complete";
    private static final java.lang.String ATTR_VALUE = "value";
    public static final int FACTORY_RESET_FLAG_ON_BOOT = 1;
    public static final int FACTORY_RESET_FLAG_WIPE_EUICC = 4;
    public static final int FACTORY_RESET_FLAG_WIPE_EXTERNAL_STORAGE = 2;
    public static final int FACTORY_RESET_FLAG_WIPE_FACTORY_RESET_PROTECTION = 8;
    static final java.lang.String NEW_USER_DISCLAIMER_ACKNOWLEDGED = "acked";
    static final java.lang.String NEW_USER_DISCLAIMER_NEEDED = "needed";
    static final java.lang.String NEW_USER_DISCLAIMER_NOT_NEEDED = "not_needed";
    private static final java.lang.String TAG = "DevicePolicyManager";
    private static final java.lang.String TAG_ACCEPTED_CA_CERTIFICATES = "accepted-ca-certificate";
    private static final java.lang.String TAG_ADMIN_BROADCAST_PENDING = "admin-broadcast-pending";
    private static final java.lang.String TAG_AFFILIATION_ID = "affiliation-id";
    private static final java.lang.String TAG_APPS_SUSPENDED = "apps-suspended";
    private static final java.lang.String TAG_BYPASS_ROLE_QUALIFICATIONS = "bypass-role-qualifications";
    private static final java.lang.String TAG_CURRENT_INPUT_METHOD_SET = "current-ime-set";
    private static final java.lang.String TAG_DO_NOT_ASK_CREDENTIALS_ON_BOOT = "do-not-ask-credentials-on-boot";
    private static final java.lang.String TAG_INITIALIZATION_BUNDLE = "initialization-bundle";
    private static final java.lang.String TAG_KEEP_PROFILES_RUNNING = "keep-profiles-running";
    private static final java.lang.String TAG_LAST_BUG_REPORT_REQUEST = "last-bug-report-request";
    private static final java.lang.String TAG_LAST_NETWORK_LOG_RETRIEVAL = "last-network-log-retrieval";
    private static final java.lang.String TAG_LAST_SECURITY_LOG_RETRIEVAL = "last-security-log-retrieval";
    private static final java.lang.String TAG_LOCK_TASK_COMPONENTS = "lock-task-component";
    private static final java.lang.String TAG_LOCK_TASK_FEATURES = "lock-task-features";
    private static final java.lang.String TAG_OWNER_INSTALLED_CA_CERT = "owner-installed-ca-cert";
    private static final java.lang.String TAG_PASSWORD_TOKEN_HANDLE = "password-token";
    private static final java.lang.String TAG_PROTECTED_PACKAGES = "protected-packages";
    private static final java.lang.String TAG_SECONDARY_LOCK_SCREEN = "secondary-lock-screen";
    private static final java.lang.String TAG_STATUS_BAR = "statusbar";
    private static final boolean VERBOSE_LOG = false;
    java.lang.String mCurrentRoleHolder;
    int mFactoryResetFlags;
    java.lang.String mFactoryResetReason;
    com.android.server.devicepolicy.ActiveAdmin mPermissionBasedAdmin;
    int mPermissionPolicy;
    android.content.ComponentName mRestrictionsProvider;

    @java.lang.Deprecated
    java.util.List<java.lang.String> mUserControlDisabledPackages;
    final int mUserId;
    int mUserProvisioningState;
    int mFailedPasswordAttempts = 0;
    boolean mPasswordValidAtLastCheckpoint = true;
    int mPasswordOwner = -1;
    long mLastMaximumTimeToLock = -1;
    boolean mUserSetupComplete = false;
    boolean mBypassDevicePolicyManagementRoleQualifications = false;
    boolean mPaired = false;
    boolean mDeviceProvisioningConfigApplied = false;
    final android.util.ArrayMap<android.content.ComponentName, com.android.server.devicepolicy.ActiveAdmin> mAdminMap = new android.util.ArrayMap<>();
    final java.util.ArrayList<com.android.server.devicepolicy.ActiveAdmin> mAdminList = new java.util.ArrayList<>();
    final java.util.ArrayList<android.content.ComponentName> mRemovingAdmins = new java.util.ArrayList<>();
    final android.util.ArraySet<java.lang.String> mAcceptedCaCertificates = new android.util.ArraySet<>();
    java.util.List<java.lang.String> mLockTaskPackages = new java.util.ArrayList();
    int mLockTaskFeatures = 16;
    boolean mStatusBarDisabled = false;
    final android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> mDelegationMap = new android.util.ArrayMap<>();
    boolean mDoNotAskCredentialsOnBoot = false;
    java.util.Set<java.lang.String> mAffiliationIds = new android.util.ArraySet();
    long mLastSecurityLogRetrievalTime = -1;
    long mLastBugReportRequestTime = -1;
    long mLastNetworkLogsRetrievalTime = -1;
    boolean mCurrentInputMethodSet = false;
    boolean mSecondaryLockscreenEnabled = false;
    java.util.Set<java.lang.String> mOwnerInstalledCaCerts = new android.util.ArraySet();
    boolean mAdminBroadcastPending = false;
    android.os.PersistableBundle mInitBundle = null;
    long mPasswordTokenHandle = 0;
    boolean mAppsSuspended = false;
    java.lang.String mNewUserDisclaimer = NEW_USER_DISCLAIMER_NOT_NEEDED;
    boolean mEffectiveKeepProfilesRunning = false;

    com.android.server.devicepolicy.ActiveAdmin createOrGetPermissionBasedAdmin(int userId) {
        if (this.mPermissionBasedAdmin == null) {
            this.mPermissionBasedAdmin = new com.android.server.devicepolicy.ActiveAdmin(userId, true);
        }
        return this.mPermissionBasedAdmin;
    }

    DevicePolicyData(int userId) {
        this.mUserId = userId;
    }

    static boolean store(com.android.server.devicepolicy.DevicePolicyData policyData, com.android.internal.util.JournaledFile file) {
        java.lang.String str;
        int n;
        java.lang.String str2 = TAG_DO_NOT_ASK_CREDENTIALS_ON_BOOT;
        java.io.FileOutputStream stream = null;
        java.io.File chooseForWrite = null;
        try {
            java.io.File chooseForWrite2 = file.chooseForWrite();
            java.lang.String str3 = TAG_AFFILIATION_ID;
            try {
                stream = new java.io.FileOutputStream(chooseForWrite2, false);
                com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
                chooseForWrite = chooseForWrite2;
                out.startDocument((java.lang.String) null, true);
                out.startTag((java.lang.String) null, "policies");
                if (policyData.mRestrictionsProvider == null) {
                    str = "policies";
                } else {
                    str = "policies";
                    out.attribute((java.lang.String) null, ATTR_PERMISSION_PROVIDER, policyData.mRestrictionsProvider.flattenToString());
                }
                if (policyData.mUserSetupComplete) {
                    out.attributeBoolean((java.lang.String) null, ATTR_SETUP_COMPLETE, true);
                }
                if (policyData.mPaired) {
                    out.attributeBoolean((java.lang.String) null, ATTR_DEVICE_PAIRED, true);
                }
                if (policyData.mDeviceProvisioningConfigApplied) {
                    out.attributeBoolean((java.lang.String) null, ATTR_DEVICE_PROVISIONING_CONFIG_APPLIED, true);
                }
                if (policyData.mUserProvisioningState != 0) {
                    out.attributeInt((java.lang.String) null, ATTR_PROVISIONING_STATE, policyData.mUserProvisioningState);
                }
                if (policyData.mPermissionPolicy != 0) {
                    out.attributeInt((java.lang.String) null, ATTR_PERMISSION_POLICY, policyData.mPermissionPolicy);
                }
                if (NEW_USER_DISCLAIMER_NEEDED.equals(policyData.mNewUserDisclaimer)) {
                    out.attribute((java.lang.String) null, ATTR_NEW_USER_DISCLAIMER, policyData.mNewUserDisclaimer);
                }
                if (policyData.mFactoryResetFlags != 0) {
                    out.attributeInt((java.lang.String) null, ATTR_FACTORY_RESET_FLAGS, policyData.mFactoryResetFlags);
                }
                if (policyData.mFactoryResetReason != null) {
                    out.attribute((java.lang.String) null, ATTR_FACTORY_RESET_REASON, policyData.mFactoryResetReason);
                }
                for (int i = 0; i < policyData.mDelegationMap.size(); i++) {
                    java.lang.String scope = policyData.mDelegationMap.keyAt(i);
                    java.util.List<java.lang.String> scopes = policyData.mDelegationMap.valueAt(i);
                    for (java.lang.String scope2 : scopes) {
                        out.startTag((java.lang.String) null, "delegation");
                        out.attribute((java.lang.String) null, "delegatePackage", scope);
                        java.lang.String delegatePackage = scope;
                        out.attribute((java.lang.String) null, "scope", scope2);
                        out.endTag((java.lang.String) null, "delegation");
                        scopes = scopes;
                        str2 = str2;
                        scope = delegatePackage;
                    }
                }
                java.lang.String str4 = str2;
                int n2 = policyData.mAdminList.size();
                int i2 = 0;
                while (i2 < n2) {
                    com.android.server.devicepolicy.ActiveAdmin ap = policyData.mAdminList.get(i2);
                    if (ap == null) {
                        n = n2;
                    } else {
                        out.startTag((java.lang.String) null, "admin");
                        n = n2;
                        out.attribute((java.lang.String) null, "name", ap.info.getComponent().flattenToString());
                        ap.writeToXml(out);
                        out.endTag((java.lang.String) null, "admin");
                    }
                    i2++;
                    n2 = n;
                }
                if (policyData.mPermissionBasedAdmin != null) {
                    out.startTag((java.lang.String) null, "permission-based-admin");
                    policyData.mPermissionBasedAdmin.writeToXml(out);
                    out.endTag((java.lang.String) null, "permission-based-admin");
                }
                if (policyData.mPasswordOwner >= 0) {
                    out.startTag((java.lang.String) null, "password-owner");
                    out.attributeInt((java.lang.String) null, ATTR_VALUE, policyData.mPasswordOwner);
                    out.endTag((java.lang.String) null, "password-owner");
                }
                if (policyData.mFailedPasswordAttempts != 0) {
                    out.startTag((java.lang.String) null, "failed-password-attempts");
                    out.attributeInt((java.lang.String) null, ATTR_VALUE, policyData.mFailedPasswordAttempts);
                    out.endTag((java.lang.String) null, "failed-password-attempts");
                }
                for (int i3 = 0; i3 < policyData.mAcceptedCaCertificates.size(); i3++) {
                    out.startTag((java.lang.String) null, TAG_ACCEPTED_CA_CERTIFICATES);
                    out.attribute((java.lang.String) null, "name", policyData.mAcceptedCaCertificates.valueAt(i3));
                    out.endTag((java.lang.String) null, TAG_ACCEPTED_CA_CERTIFICATES);
                }
                for (int i4 = 0; i4 < policyData.mLockTaskPackages.size(); i4++) {
                    java.lang.String component = policyData.mLockTaskPackages.get(i4);
                    out.startTag((java.lang.String) null, TAG_LOCK_TASK_COMPONENTS);
                    out.attribute((java.lang.String) null, "name", component);
                    out.endTag((java.lang.String) null, TAG_LOCK_TASK_COMPONENTS);
                }
                int i5 = policyData.mLockTaskFeatures;
                if (i5 != 0) {
                    out.startTag((java.lang.String) null, TAG_LOCK_TASK_FEATURES);
                    out.attributeInt((java.lang.String) null, ATTR_VALUE, policyData.mLockTaskFeatures);
                    out.endTag((java.lang.String) null, TAG_LOCK_TASK_FEATURES);
                }
                if (policyData.mSecondaryLockscreenEnabled) {
                    out.startTag((java.lang.String) null, TAG_SECONDARY_LOCK_SCREEN);
                    out.attributeBoolean((java.lang.String) null, ATTR_VALUE, true);
                    out.endTag((java.lang.String) null, TAG_SECONDARY_LOCK_SCREEN);
                }
                if (policyData.mStatusBarDisabled) {
                    out.startTag((java.lang.String) null, TAG_STATUS_BAR);
                    out.attributeBoolean((java.lang.String) null, "disabled", policyData.mStatusBarDisabled);
                    out.endTag((java.lang.String) null, TAG_STATUS_BAR);
                }
                if (policyData.mDoNotAskCredentialsOnBoot) {
                    out.startTag((java.lang.String) null, str4);
                    out.endTag((java.lang.String) null, str4);
                }
                for (java.lang.String id : policyData.mAffiliationIds) {
                    java.lang.String str5 = str3;
                    out.startTag((java.lang.String) null, str5);
                    out.attribute((java.lang.String) null, ATTR_ID, id);
                    out.endTag((java.lang.String) null, str5);
                    str3 = str5;
                }
                if (policyData.mLastSecurityLogRetrievalTime >= 0) {
                    out.startTag((java.lang.String) null, TAG_LAST_SECURITY_LOG_RETRIEVAL);
                    out.attributeLong((java.lang.String) null, ATTR_VALUE, policyData.mLastSecurityLogRetrievalTime);
                    out.endTag((java.lang.String) null, TAG_LAST_SECURITY_LOG_RETRIEVAL);
                }
                if (policyData.mLastBugReportRequestTime >= 0) {
                    out.startTag((java.lang.String) null, TAG_LAST_BUG_REPORT_REQUEST);
                    out.attributeLong((java.lang.String) null, ATTR_VALUE, policyData.mLastBugReportRequestTime);
                    out.endTag((java.lang.String) null, TAG_LAST_BUG_REPORT_REQUEST);
                }
                if (policyData.mLastNetworkLogsRetrievalTime >= 0) {
                    out.startTag((java.lang.String) null, TAG_LAST_NETWORK_LOG_RETRIEVAL);
                    out.attributeLong((java.lang.String) null, ATTR_VALUE, policyData.mLastNetworkLogsRetrievalTime);
                    out.endTag((java.lang.String) null, TAG_LAST_NETWORK_LOG_RETRIEVAL);
                }
                if (policyData.mAdminBroadcastPending) {
                    out.startTag((java.lang.String) null, TAG_ADMIN_BROADCAST_PENDING);
                    out.attributeBoolean((java.lang.String) null, ATTR_VALUE, policyData.mAdminBroadcastPending);
                    out.endTag((java.lang.String) null, TAG_ADMIN_BROADCAST_PENDING);
                }
                if (policyData.mInitBundle != null) {
                    out.startTag((java.lang.String) null, TAG_INITIALIZATION_BUNDLE);
                    policyData.mInitBundle.saveToXml(out);
                    out.endTag((java.lang.String) null, TAG_INITIALIZATION_BUNDLE);
                }
                if (policyData.mPasswordTokenHandle != 0) {
                    out.startTag((java.lang.String) null, TAG_PASSWORD_TOKEN_HANDLE);
                    out.attributeLong((java.lang.String) null, ATTR_VALUE, policyData.mPasswordTokenHandle);
                    out.endTag((java.lang.String) null, TAG_PASSWORD_TOKEN_HANDLE);
                }
                if (policyData.mCurrentInputMethodSet) {
                    out.startTag((java.lang.String) null, TAG_CURRENT_INPUT_METHOD_SET);
                    out.endTag((java.lang.String) null, TAG_CURRENT_INPUT_METHOD_SET);
                }
                for (java.lang.String cert : policyData.mOwnerInstalledCaCerts) {
                    out.startTag((java.lang.String) null, TAG_OWNER_INSTALLED_CA_CERT);
                    out.attribute((java.lang.String) null, ATTR_ALIAS, cert);
                    out.endTag((java.lang.String) null, TAG_OWNER_INSTALLED_CA_CERT);
                }
                if (policyData.mAppsSuspended) {
                    out.startTag((java.lang.String) null, TAG_APPS_SUSPENDED);
                    out.attributeBoolean((java.lang.String) null, ATTR_VALUE, policyData.mAppsSuspended);
                    out.endTag((java.lang.String) null, TAG_APPS_SUSPENDED);
                }
                if (policyData.mBypassDevicePolicyManagementRoleQualifications) {
                    out.startTag((java.lang.String) null, TAG_BYPASS_ROLE_QUALIFICATIONS);
                    out.attribute((java.lang.String) null, ATTR_VALUE, policyData.mCurrentRoleHolder);
                    out.endTag((java.lang.String) null, TAG_BYPASS_ROLE_QUALIFICATIONS);
                }
                if (policyData.mEffectiveKeepProfilesRunning) {
                    out.startTag((java.lang.String) null, TAG_KEEP_PROFILES_RUNNING);
                    out.attributeBoolean((java.lang.String) null, ATTR_VALUE, policyData.mEffectiveKeepProfilesRunning);
                    out.endTag((java.lang.String) null, TAG_KEEP_PROFILES_RUNNING);
                }
                out.endTag((java.lang.String) null, str);
                out.endDocument();
                stream.flush();
                android.os.FileUtils.sync(stream);
                stream.close();
                file.commit();
                return true;
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                e = e;
                chooseForWrite = chooseForWrite2;
                com.android.server.utils.Slogf.w(TAG, e, "failed writing file %s", chooseForWrite);
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (java.io.IOException e2) {
                    }
                }
                file.rollback();
                return false;
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e3) {
            e = e3;
        }
    }

    /* JADX WARN: Not initialized variable reg: 17, insn: 0x0410: MOVE (r3 I:??[OBJECT, ARRAY]) = (r17 I:??[OBJECT, ARRAY] A[D('stream' java.io.FileInputStream)]), block:B:180:0x040f */
    /* JADX WARN: Not initialized variable reg: 17, insn: 0x0414: MOVE (r3 I:??[OBJECT, ARRAY]) = (r17 I:??[OBJECT, ARRAY] A[D('stream' java.io.FileInputStream)]), block:B:182:0x0414 */
    /* JADX WARN: Removed duplicated region for block: B:169:0x03c8  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0438 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00fb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static void load(com.android.server.devicepolicy.DevicePolicyData r22, com.android.internal.util.JournaledFile r23, java.util.function.Function<android.content.ComponentName, android.app.admin.DeviceAdminInfo> r24, android.content.ComponentName r25) {
        /*
            Method dump skipped, instruction units count: 1099
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.DevicePolicyData.load(com.android.server.devicepolicy.DevicePolicyData, com.android.internal.util.JournaledFile, java.util.function.Function, android.content.ComponentName):void");
    }

    void validatePasswordOwner() {
        if (this.mPasswordOwner >= 0) {
            boolean haveOwner = false;
            int i = this.mAdminList.size() - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                if (this.mAdminList.get(i).getUid() != this.mPasswordOwner) {
                    i--;
                } else {
                    haveOwner = true;
                    break;
                }
            }
            if (!haveOwner) {
                com.android.server.utils.Slogf.w(TAG, "Previous password owner %s no longer active; disabling", java.lang.Integer.valueOf(this.mPasswordOwner));
                this.mPasswordOwner = -1;
            }
        }
    }

    void setDelayedFactoryReset(java.lang.String reason, boolean wipeExtRequested, boolean wipeEuicc, boolean wipeResetProtectionData) {
        this.mFactoryResetReason = reason;
        this.mFactoryResetFlags = 1;
        if (wipeExtRequested) {
            this.mFactoryResetFlags |= 2;
        }
        if (wipeEuicc) {
            this.mFactoryResetFlags |= 4;
        }
        if (wipeResetProtectionData) {
            this.mFactoryResetFlags |= 8;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean isNewUserDisclaimerAcknowledged() {
        /*
            r5 = this;
            java.lang.String r0 = r5.mNewUserDisclaimer
            java.lang.String r1 = "DevicePolicyManager"
            r2 = 1
            r3 = 0
            if (r0 != 0) goto L1e
            int r0 = r5.mUserId
            if (r0 != 0) goto Ld
            return r2
        Ld:
            int r0 = r5.mUserId
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            java.lang.Object[] r0 = new java.lang.Object[]{r0}
            java.lang.String r2 = "isNewUserDisclaimerAcknowledged(%d): mNewUserDisclaimer is null"
            com.android.server.utils.Slogf.w(r1, r2, r0)
            return r3
        L1e:
            java.lang.String r0 = r5.mNewUserDisclaimer
            int r4 = r0.hashCode()
            switch(r4) {
                case -1238968671: goto L3d;
                case -1049376843: goto L32;
                case 92636904: goto L28;
                default: goto L27;
            }
        L27:
            goto L48
        L28:
            java.lang.String r4 = "acked"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L27
            r0 = r3
            goto L49
        L32:
            java.lang.String r4 = "needed"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L27
            r0 = 2
            goto L49
        L3d:
            java.lang.String r4 = "not_needed"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L27
            r0 = r2
            goto L49
        L48:
            r0 = -1
        L49:
            switch(r0) {
                case 0: goto L60;
                case 1: goto L60;
                case 2: goto L5f;
                default: goto L4c;
            }
        L4c:
            int r0 = r5.mUserId
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            java.lang.String r2 = r5.mNewUserDisclaimer
            java.lang.Object[] r0 = new java.lang.Object[]{r0, r2}
            java.lang.String r2 = "isNewUserDisclaimerAcknowledged(%d): invalid value %d"
            com.android.server.utils.Slogf.w(r1, r2, r0)
            return r3
        L5f:
            return r3
        L60:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.DevicePolicyData.isNewUserDisclaimerAcknowledged():boolean");
    }

    void dump(android.util.IndentingPrintWriter pw) {
        pw.println();
        pw.println("Enabled Device Admins (User " + this.mUserId + ", provisioningState: " + this.mUserProvisioningState + "):");
        int n = this.mAdminList.size();
        for (int i = 0; i < n; i++) {
            com.android.server.devicepolicy.ActiveAdmin ap = this.mAdminList.get(i);
            if (ap != null) {
                pw.increaseIndent();
                pw.print(ap.info.getComponent().flattenToShortString());
                pw.println(":");
                pw.increaseIndent();
                ap.dump(pw);
                pw.decreaseIndent();
                pw.decreaseIndent();
            }
        }
        if (!this.mRemovingAdmins.isEmpty()) {
            pw.increaseIndent();
            pw.println("Removing Device Admins (User " + this.mUserId + "): " + this.mRemovingAdmins);
            pw.decreaseIndent();
        }
        pw.println();
        pw.increaseIndent();
        pw.print("mPasswordOwner=");
        pw.println(this.mPasswordOwner);
        pw.print("mPasswordTokenHandle=");
        pw.println(java.lang.Long.toHexString(this.mPasswordTokenHandle));
        pw.print("mAppsSuspended=");
        pw.println(this.mAppsSuspended);
        pw.print("mUserSetupComplete=");
        pw.println(this.mUserSetupComplete);
        pw.print("mAffiliationIds=");
        pw.println(this.mAffiliationIds);
        pw.print("mNewUserDisclaimer=");
        pw.println(this.mNewUserDisclaimer);
        if (this.mFactoryResetFlags != 0) {
            pw.print("mFactoryResetFlags=");
            pw.print(this.mFactoryResetFlags);
            pw.print(" (");
            pw.print(factoryResetFlagsToString(this.mFactoryResetFlags));
            pw.println(')');
        }
        if (this.mFactoryResetReason != null) {
            pw.print("mFactoryResetReason=");
            pw.println(this.mFactoryResetReason);
        }
        if (this.mDelegationMap.size() != 0) {
            pw.println("mDelegationMap=");
            pw.increaseIndent();
            for (int i2 = 0; i2 < this.mDelegationMap.size(); i2++) {
                java.util.List<java.lang.String> delegationScopes = this.mDelegationMap.valueAt(i2);
                pw.println(this.mDelegationMap.keyAt(i2) + "[size=" + delegationScopes.size() + "]");
                pw.increaseIndent();
                for (int j = 0; j < delegationScopes.size(); j++) {
                    pw.println(j + ": " + delegationScopes.get(j));
                }
                pw.decreaseIndent();
            }
            pw.decreaseIndent();
        }
        pw.decreaseIndent();
    }

    static java.lang.String factoryResetFlagsToString(int flags) {
        return android.util.DebugUtils.flagsToString(com.android.server.devicepolicy.DevicePolicyData.class, "FACTORY_RESET_FLAG_", flags);
    }
}
