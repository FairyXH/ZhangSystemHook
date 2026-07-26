package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class ActiveAdmin {
    private static final java.lang.String ATTR_LAST_NETWORK_LOGGING_NOTIFICATION = "last-notification";
    private static final java.lang.String ATTR_NUM_NETWORK_LOGGING_NOTIFICATIONS = "num-notifications";
    private static final java.lang.String ATTR_PACKAGE_POLICY_MODE = "package-policy-type";
    private static final java.lang.String ATTR_VALUE = "value";
    static final int DEF_KEYGUARD_FEATURES_DISABLED = 0;
    static final int DEF_MAXIMUM_FAILED_PASSWORDS_FOR_WIPE = 0;
    static final int DEF_MAXIMUM_NETWORK_LOGGING_NOTIFICATIONS_SHOWN = 2;
    static final long DEF_MAXIMUM_TIME_TO_UNLOCK = 0;
    static final int DEF_ORGANIZATION_COLOR = android.graphics.Color.parseColor("#00796B");
    static final long DEF_PASSWORD_EXPIRATION_DATE = 0;
    static final long DEF_PASSWORD_EXPIRATION_TIMEOUT = 0;
    static final int DEF_PASSWORD_HISTORY_LENGTH = 0;
    private static final java.lang.String TAG_ACCOUNT_TYPE = "account-type";
    private static final java.lang.String TAG_ADMIN_CAN_GRANT_SENSORS_PERMISSIONS = "admin-can-grant-sensors-permissions";
    private static final java.lang.String TAG_ALWAYS_ON_VPN_LOCKDOWN = "vpn-lockdown";
    private static final java.lang.String TAG_ALWAYS_ON_VPN_PACKAGE = "vpn-package";
    private static final java.lang.String TAG_COMMON_CRITERIA_MODE = "common-criteria-mode";
    private static final java.lang.String TAG_CREDENTIAL_MANAGER_POLICY = "credential-manager-policy";
    private static final java.lang.String TAG_CROSS_PROFILE_CALENDAR_PACKAGES = "cross-profile-calendar-packages";
    private static final java.lang.String TAG_CROSS_PROFILE_CALENDAR_PACKAGES_NULL = "cross-profile-calendar-packages-null";
    private static final java.lang.String TAG_CROSS_PROFILE_CALLER_ID_POLICY = "caller-id-policy";
    private static final java.lang.String TAG_CROSS_PROFILE_CONTACTS_SEARCH_POLICY = "contacts-policy";
    private static final java.lang.String TAG_CROSS_PROFILE_PACKAGES = "cross-profile-packages";
    private static final java.lang.String TAG_CROSS_PROFILE_WIDGET_PROVIDERS = "cross-profile-widget-providers";
    private static final java.lang.String TAG_DEFAULT_ENABLED_USER_RESTRICTIONS = "default-enabled-user-restrictions";
    private static final java.lang.String TAG_DIALER_PACKAGE = "dialer_package";
    private static final java.lang.String TAG_DISABLE_ACCOUNT_MANAGEMENT = "disable-account-management";
    private static final java.lang.String TAG_DISABLE_BLUETOOTH_CONTACT_SHARING = "disable-bt-contacts-sharing";
    private static final java.lang.String TAG_DISABLE_CALLER_ID = "disable-caller-id";
    private static final java.lang.String TAG_DISABLE_CAMERA = "disable-camera";
    private static final java.lang.String TAG_DISABLE_CONTACTS_SEARCH = "disable-contacts-search";
    private static final java.lang.String TAG_DISABLE_KEYGUARD_FEATURES = "disable-keyguard-features";
    private static final java.lang.String TAG_DISABLE_SCREEN_CAPTURE = "disable-screen-capture";
    private static final java.lang.String TAG_ENCRYPTION_REQUESTED = "encryption-requested";
    private static final java.lang.String TAG_END_USER_SESSION_MESSAGE = "end_user_session_message";
    private static final java.lang.String TAG_ENROLLMENT_SPECIFIC_ID = "enrollment-specific-id";
    private static final java.lang.String TAG_FACTORY_RESET_PROTECTION_POLICY = "factory_reset_protection_policy";
    private static final java.lang.String TAG_FORCE_EPHEMERAL_USERS = "force_ephemeral_users";
    private static final java.lang.String TAG_GLOBAL_PROXY_EXCLUSION_LIST = "global-proxy-exclusion-list";
    private static final java.lang.String TAG_GLOBAL_PROXY_SPEC = "global-proxy-spec";
    private static final java.lang.String TAG_IS_LOGOUT_ENABLED = "is_logout_enabled";
    private static final java.lang.String TAG_IS_NETWORK_LOGGING_ENABLED = "is_network_logging_enabled";
    private static final java.lang.String TAG_KEEP_UNINSTALLED_PACKAGES = "keep-uninstalled-packages";
    private static final java.lang.String TAG_LONG_SUPPORT_MESSAGE = "long-support-message";
    private static final java.lang.String TAG_MANAGED_SUBSCRIPTIONS_POLICY = "managed_subscriptions_policy";
    private static final java.lang.String TAG_MANAGE_TRUST_AGENT_FEATURES = "manage-trust-agent-features";
    private static final java.lang.String TAG_MAX_FAILED_PASSWORD_WIPE = "max-failed-password-wipe";
    private static final java.lang.String TAG_MAX_TIME_TO_UNLOCK = "max-time-to-unlock";
    private static final java.lang.String TAG_METERED_DATA_DISABLED_PACKAGES = "metered_data_disabled_packages";
    private static final java.lang.String TAG_MIN_PASSWORD_LENGTH = "min-password-length";
    private static final java.lang.String TAG_MIN_PASSWORD_LETTERS = "min-password-letters";
    private static final java.lang.String TAG_MIN_PASSWORD_LOWERCASE = "min-password-lowercase";
    private static final java.lang.String TAG_MIN_PASSWORD_NONLETTER = "min-password-nonletter";
    private static final java.lang.String TAG_MIN_PASSWORD_NUMERIC = "min-password-numeric";
    private static final java.lang.String TAG_MIN_PASSWORD_SYMBOLS = "min-password-symbols";
    private static final java.lang.String TAG_MIN_PASSWORD_UPPERCASE = "min-password-uppercase";
    private static final java.lang.String TAG_MTE_POLICY = "mte-policy";
    private static final java.lang.String TAG_NEARBY_APP_STREAMING_POLICY = "nearby-app-streaming-policy";
    private static final java.lang.String TAG_NEARBY_NOTIFICATION_STREAMING_POLICY = "nearby-notification-streaming-policy";
    private static final java.lang.String TAG_ORGANIZATION_COLOR = "organization-color";
    private static final java.lang.String TAG_ORGANIZATION_ID = "organization-id";
    private static final java.lang.String TAG_ORGANIZATION_NAME = "organization-name";
    private static final java.lang.String TAG_PACKAGE_LIST_ITEM = "item";
    private static final java.lang.String TAG_PACKAGE_POLICY_PACKAGE_NAMES = "package-policy-packages";
    private static final java.lang.String TAG_PARENT_ADMIN = "parent-admin";
    private static final java.lang.String TAG_PASSWORD_COMPLEXITY = "password-complexity";
    private static final java.lang.String TAG_PASSWORD_EXPIRATION_DATE = "password-expiration-date";
    private static final java.lang.String TAG_PASSWORD_EXPIRATION_TIMEOUT = "password-expiration-timeout";
    private static final java.lang.String TAG_PASSWORD_HISTORY_LENGTH = "password-history-length";
    private static final java.lang.String TAG_PASSWORD_QUALITY = "password-quality";
    private static final java.lang.String TAG_PERMITTED_ACCESSIBILITY_SERVICES = "permitted-accessiblity-services";
    private static final java.lang.String TAG_PERMITTED_IMES = "permitted-imes";
    private static final java.lang.String TAG_PERMITTED_NOTIFICATION_LISTENERS = "permitted-notification-listeners";
    private static final java.lang.String TAG_POLICIES = "policies";
    private static final java.lang.String TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIG = "preferential_network_service_config";
    private static final java.lang.String TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIGS = "preferential_network_service_configs";
    private static final java.lang.String TAG_PREFERENTIAL_NETWORK_SERVICE_ENABLED = "preferential-network-service-enabled";
    private static final java.lang.String TAG_PROFILE_MAXIMUM_TIME_OFF = "profile-max-time-off";
    private static final java.lang.String TAG_PROFILE_OFF_DEADLINE = "profile-off-deadline";
    private static final java.lang.String TAG_PROTECTED_PACKAGES = "protected_packages";
    private static final java.lang.String TAG_PROVIDER = "provider";
    private static final java.lang.String TAG_REQUIRE_AUTO_TIME = "require_auto_time";
    private static final java.lang.String TAG_RESTRICTION = "restriction";
    private static final java.lang.String TAG_SHORT_SUPPORT_MESSAGE = "short-support-message";
    private static final java.lang.String TAG_SMS_PACKAGE = "sms_package";
    private static final java.lang.String TAG_SPECIFIES_GLOBAL_PROXY = "specifies-global-proxy";
    private static final java.lang.String TAG_SSID = "ssid";
    private static final java.lang.String TAG_SSID_ALLOWLIST = "ssid-allowlist";
    private static final java.lang.String TAG_SSID_DENYLIST = "ssid-denylist";
    private static final java.lang.String TAG_START_USER_SESSION_MESSAGE = "start_user_session_message";
    private static final java.lang.String TAG_STRONG_AUTH_UNLOCK_TIMEOUT = "strong-auth-unlock-timeout";
    private static final java.lang.String TAG_SUSPENDED_PACKAGES = "suspended-packages";
    private static final java.lang.String TAG_SUSPEND_PERSONAL_APPS = "suspend-personal-apps";
    private static final java.lang.String TAG_TEST_ONLY_ADMIN = "test-only-admin";
    private static final java.lang.String TAG_TRUST_AGENT_COMPONENT = "component";
    private static final java.lang.String TAG_TRUST_AGENT_COMPONENT_OPTIONS = "trust-agent-component-options";
    private static final java.lang.String TAG_USB_DATA_SIGNALING = "usb-data-signaling";
    private static final java.lang.String TAG_USER_RESTRICTIONS = "user-restrictions";
    private static final java.lang.String TAG_WIFI_MIN_SECURITY = "wifi-min-security";
    private static final boolean USB_DATA_SIGNALING_ENABLED_DEFAULT = true;
    final java.util.Set<java.lang.String> accountTypesWithManagementDisabled;
    java.util.List<java.lang.String> crossProfileWidgetProviders;
    final java.util.Set<java.lang.String> defaultEnabledRestrictionsAlreadySet;
    boolean disableBluetoothContactSharing;
    boolean disableCallerId;
    boolean disableCamera;
    boolean disableContactsSearch;
    boolean disableScreenCapture;
    int disabledKeyguardFeatures;
    boolean encryptionRequested;
    java.lang.String endUserSessionMessage;
    boolean forceEphemeralUsers;
    java.lang.String globalProxyExclusionList;
    java.lang.String globalProxySpec;
    android.app.admin.DeviceAdminInfo info;
    boolean isLogoutEnabled;
    boolean isNetworkLoggingEnabled;
    final boolean isParent;
    public final boolean isPermissionBased;
    java.util.List<java.lang.String> keepUninstalledPackages;
    long lastNetworkLoggingNotificationTimeMs;
    java.lang.CharSequence longSupportMessage;
    public boolean mAdminCanGrantSensorsPermissions;
    public boolean mAlwaysOnVpnLockdown;
    public java.lang.String mAlwaysOnVpnPackage;
    boolean mCommonCriteriaMode;
    android.app.admin.PackagePolicy mCredentialManagerPolicy;
    java.util.List<java.lang.String> mCrossProfileCalendarPackages;
    java.util.List<java.lang.String> mCrossProfilePackages;
    java.lang.String mDialerPackage;
    public java.lang.String mEnrollmentSpecificId;
    android.app.admin.FactoryResetProtectionPolicy mFactoryResetProtectionPolicy;
    android.app.admin.PackagePolicy mManagedProfileCallerIdAccess;
    android.app.admin.PackagePolicy mManagedProfileContactsAccess;
    android.app.admin.ManagedSubscriptionsPolicy mManagedSubscriptionsPolicy;
    int mNearbyAppStreamingPolicy;
    int mNearbyNotificationStreamingPolicy;
    public java.lang.String mOrganizationId;
    int mPasswordComplexity;
    android.app.admin.PasswordPolicy mPasswordPolicy;
    public java.util.List<android.app.admin.PreferentialNetworkServiceConfig> mPreferentialNetworkServiceConfigs;
    long mProfileMaximumTimeOffMillis;
    long mProfileOffDeadline;
    java.lang.String mSmsPackage;
    boolean mSuspendPersonalApps;
    boolean mUsbDataSignalingEnabled;
    int mWifiMinimumSecurityLevel;
    android.app.admin.WifiSsidPolicy mWifiSsidPolicy;
    int maximumFailedPasswordsForWipe;
    long maximumTimeToUnlock;
    java.util.List<java.lang.String> meteredDisabledPackages;
    int mtePolicy;
    int numNetworkLoggingNotifications;
    int organizationColor;
    java.lang.String organizationName;
    com.android.server.devicepolicy.ActiveAdmin parentAdmin;
    long passwordExpirationDate;
    long passwordExpirationTimeout;
    int passwordHistoryLength;
    java.util.List<java.lang.String> permittedAccessiblityServices;
    java.util.List<java.lang.String> permittedInputMethods;
    java.util.List<java.lang.String> permittedNotificationListeners;
    java.util.List<java.lang.String> protectedPackages;
    boolean requireAutoTime;
    java.lang.CharSequence shortSupportMessage;
    boolean specifiesGlobalProxy;
    java.lang.String startUserSessionMessage;
    long strongAuthUnlockTimeout;
    java.util.List<java.lang.String> suspendedPackages;
    boolean testOnlyAdmin;
    android.util.ArrayMap<java.lang.String, com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo> trustAgentInfos;
    private final int userId;
    android.os.Bundle userRestrictions;

    static class TrustAgentInfo {
        public android.os.PersistableBundle options;

        TrustAgentInfo(android.os.PersistableBundle bundle) {
            this.options = bundle;
        }
    }

    ActiveAdmin(android.app.admin.DeviceAdminInfo info, boolean isParent) {
        this.passwordHistoryLength = 0;
        this.mPasswordPolicy = new android.app.admin.PasswordPolicy();
        this.mPasswordComplexity = 0;
        this.mNearbyNotificationStreamingPolicy = 3;
        this.mNearbyAppStreamingPolicy = 3;
        this.mFactoryResetProtectionPolicy = null;
        this.maximumTimeToUnlock = 0L;
        this.strongAuthUnlockTimeout = 0L;
        this.maximumFailedPasswordsForWipe = 0;
        this.passwordExpirationTimeout = 0L;
        this.passwordExpirationDate = 0L;
        this.disabledKeyguardFeatures = 0;
        this.encryptionRequested = false;
        this.testOnlyAdmin = false;
        this.disableCamera = false;
        this.disableCallerId = false;
        this.disableContactsSearch = false;
        this.disableBluetoothContactSharing = true;
        this.disableScreenCapture = false;
        this.requireAutoTime = false;
        this.forceEphemeralUsers = false;
        this.isNetworkLoggingEnabled = false;
        this.isLogoutEnabled = false;
        this.numNetworkLoggingNotifications = 0;
        this.lastNetworkLoggingNotificationTimeMs = 0L;
        this.mtePolicy = 0;
        this.accountTypesWithManagementDisabled = new android.util.ArraySet();
        this.specifiesGlobalProxy = false;
        this.globalProxySpec = null;
        this.globalProxyExclusionList = null;
        this.trustAgentInfos = new android.util.ArrayMap<>();
        this.defaultEnabledRestrictionsAlreadySet = new android.util.ArraySet();
        this.shortSupportMessage = null;
        this.longSupportMessage = null;
        this.organizationColor = DEF_ORGANIZATION_COLOR;
        this.organizationName = null;
        this.startUserSessionMessage = null;
        this.endUserSessionMessage = null;
        this.mCrossProfileCalendarPackages = java.util.Collections.emptyList();
        this.mCrossProfilePackages = java.util.Collections.emptyList();
        this.mSuspendPersonalApps = false;
        this.mProfileMaximumTimeOffMillis = 0L;
        this.mProfileOffDeadline = 0L;
        this.mManagedProfileCallerIdAccess = null;
        this.mManagedProfileContactsAccess = null;
        this.mCredentialManagerPolicy = null;
        this.mPreferentialNetworkServiceConfigs = java.util.List.of(android.app.admin.PreferentialNetworkServiceConfig.DEFAULT);
        this.mUsbDataSignalingEnabled = true;
        this.mWifiMinimumSecurityLevel = 0;
        this.userId = -1;
        this.info = info;
        this.isParent = isParent;
        this.isPermissionBased = false;
    }

    ActiveAdmin(int userId, boolean permissionBased) {
        this.passwordHistoryLength = 0;
        this.mPasswordPolicy = new android.app.admin.PasswordPolicy();
        this.mPasswordComplexity = 0;
        this.mNearbyNotificationStreamingPolicy = 3;
        this.mNearbyAppStreamingPolicy = 3;
        this.mFactoryResetProtectionPolicy = null;
        this.maximumTimeToUnlock = 0L;
        this.strongAuthUnlockTimeout = 0L;
        this.maximumFailedPasswordsForWipe = 0;
        this.passwordExpirationTimeout = 0L;
        this.passwordExpirationDate = 0L;
        this.disabledKeyguardFeatures = 0;
        this.encryptionRequested = false;
        this.testOnlyAdmin = false;
        this.disableCamera = false;
        this.disableCallerId = false;
        this.disableContactsSearch = false;
        this.disableBluetoothContactSharing = true;
        this.disableScreenCapture = false;
        this.requireAutoTime = false;
        this.forceEphemeralUsers = false;
        this.isNetworkLoggingEnabled = false;
        this.isLogoutEnabled = false;
        this.numNetworkLoggingNotifications = 0;
        this.lastNetworkLoggingNotificationTimeMs = 0L;
        this.mtePolicy = 0;
        this.accountTypesWithManagementDisabled = new android.util.ArraySet();
        this.specifiesGlobalProxy = false;
        this.globalProxySpec = null;
        this.globalProxyExclusionList = null;
        this.trustAgentInfos = new android.util.ArrayMap<>();
        this.defaultEnabledRestrictionsAlreadySet = new android.util.ArraySet();
        this.shortSupportMessage = null;
        this.longSupportMessage = null;
        this.organizationColor = DEF_ORGANIZATION_COLOR;
        this.organizationName = null;
        this.startUserSessionMessage = null;
        this.endUserSessionMessage = null;
        this.mCrossProfileCalendarPackages = java.util.Collections.emptyList();
        this.mCrossProfilePackages = java.util.Collections.emptyList();
        this.mSuspendPersonalApps = false;
        this.mProfileMaximumTimeOffMillis = 0L;
        this.mProfileOffDeadline = 0L;
        this.mManagedProfileCallerIdAccess = null;
        this.mManagedProfileContactsAccess = null;
        this.mCredentialManagerPolicy = null;
        this.mPreferentialNetworkServiceConfigs = java.util.List.of(android.app.admin.PreferentialNetworkServiceConfig.DEFAULT);
        this.mUsbDataSignalingEnabled = true;
        this.mWifiMinimumSecurityLevel = 0;
        if (!permissionBased) {
            throw new java.lang.IllegalArgumentException("Can only pass true for permissionBased admin");
        }
        this.userId = userId;
        this.isPermissionBased = permissionBased;
        this.isParent = false;
        this.info = null;
    }

    com.android.server.devicepolicy.ActiveAdmin getParentActiveAdmin() {
        com.android.internal.util.Preconditions.checkState(!this.isParent);
        if (this.parentAdmin == null) {
            this.parentAdmin = new com.android.server.devicepolicy.ActiveAdmin(this.info, true);
        }
        return this.parentAdmin;
    }

    boolean hasParentActiveAdmin() {
        return this.parentAdmin != null;
    }

    int getUid() {
        if (this.isPermissionBased) {
            return -1;
        }
        return this.info.getActivityInfo().applicationInfo.uid;
    }

    public android.os.UserHandle getUserHandle() {
        if (this.isPermissionBased) {
            return android.os.UserHandle.of(this.userId);
        }
        return android.os.UserHandle.of(android.os.UserHandle.getUserId(this.info.getActivityInfo().applicationInfo.uid));
    }

    void writeToXml(com.android.modules.utils.TypedXmlSerializer out) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        if (this.info != null) {
            out.startTag((java.lang.String) null, TAG_POLICIES);
            this.info.writePoliciesToXml(out);
            out.endTag((java.lang.String) null, TAG_POLICIES);
        }
        if (this.mPasswordPolicy.quality != 0) {
            writeAttributeValueToXml(out, TAG_PASSWORD_QUALITY, this.mPasswordPolicy.quality);
            if (this.mPasswordPolicy.length != 0) {
                writeAttributeValueToXml(out, TAG_MIN_PASSWORD_LENGTH, this.mPasswordPolicy.length);
            }
            if (this.mPasswordPolicy.upperCase != 0) {
                writeAttributeValueToXml(out, TAG_MIN_PASSWORD_UPPERCASE, this.mPasswordPolicy.upperCase);
            }
            if (this.mPasswordPolicy.lowerCase != 0) {
                writeAttributeValueToXml(out, TAG_MIN_PASSWORD_LOWERCASE, this.mPasswordPolicy.lowerCase);
            }
            if (this.mPasswordPolicy.letters != 1) {
                writeAttributeValueToXml(out, TAG_MIN_PASSWORD_LETTERS, this.mPasswordPolicy.letters);
            }
            if (this.mPasswordPolicy.numeric != 1) {
                writeAttributeValueToXml(out, TAG_MIN_PASSWORD_NUMERIC, this.mPasswordPolicy.numeric);
            }
            if (this.mPasswordPolicy.symbols != 1) {
                writeAttributeValueToXml(out, TAG_MIN_PASSWORD_SYMBOLS, this.mPasswordPolicy.symbols);
            }
            if (this.mPasswordPolicy.nonLetter > 0) {
                writeAttributeValueToXml(out, TAG_MIN_PASSWORD_NONLETTER, this.mPasswordPolicy.nonLetter);
            }
        }
        if (this.passwordHistoryLength != 0) {
            writeAttributeValueToXml(out, TAG_PASSWORD_HISTORY_LENGTH, this.passwordHistoryLength);
        }
        if (this.maximumTimeToUnlock != 0) {
            writeAttributeValueToXml(out, TAG_MAX_TIME_TO_UNLOCK, this.maximumTimeToUnlock);
        }
        if (this.strongAuthUnlockTimeout != 259200000) {
            writeAttributeValueToXml(out, TAG_STRONG_AUTH_UNLOCK_TIMEOUT, this.strongAuthUnlockTimeout);
        }
        if (this.maximumFailedPasswordsForWipe != 0) {
            writeAttributeValueToXml(out, TAG_MAX_FAILED_PASSWORD_WIPE, this.maximumFailedPasswordsForWipe);
        }
        if (this.specifiesGlobalProxy) {
            writeAttributeValueToXml(out, TAG_SPECIFIES_GLOBAL_PROXY, this.specifiesGlobalProxy);
            if (this.globalProxySpec != null) {
                writeAttributeValueToXml(out, TAG_GLOBAL_PROXY_SPEC, this.globalProxySpec);
            }
            if (this.globalProxyExclusionList != null) {
                writeAttributeValueToXml(out, TAG_GLOBAL_PROXY_EXCLUSION_LIST, this.globalProxyExclusionList);
            }
        }
        if (this.passwordExpirationTimeout != 0) {
            writeAttributeValueToXml(out, TAG_PASSWORD_EXPIRATION_TIMEOUT, this.passwordExpirationTimeout);
        }
        if (this.passwordExpirationDate != 0) {
            writeAttributeValueToXml(out, TAG_PASSWORD_EXPIRATION_DATE, this.passwordExpirationDate);
        }
        if (this.encryptionRequested) {
            writeAttributeValueToXml(out, TAG_ENCRYPTION_REQUESTED, this.encryptionRequested);
        }
        if (this.testOnlyAdmin) {
            writeAttributeValueToXml(out, TAG_TEST_ONLY_ADMIN, this.testOnlyAdmin);
        }
        if (this.disableCamera) {
            writeAttributeValueToXml(out, TAG_DISABLE_CAMERA, this.disableCamera);
        }
        if (this.disableCallerId) {
            writeAttributeValueToXml(out, TAG_DISABLE_CALLER_ID, this.disableCallerId);
        }
        if (this.disableContactsSearch) {
            writeAttributeValueToXml(out, TAG_DISABLE_CONTACTS_SEARCH, this.disableContactsSearch);
        }
        if (!this.disableBluetoothContactSharing) {
            writeAttributeValueToXml(out, TAG_DISABLE_BLUETOOTH_CONTACT_SHARING, this.disableBluetoothContactSharing);
        }
        if (this.disableScreenCapture) {
            writeAttributeValueToXml(out, TAG_DISABLE_SCREEN_CAPTURE, this.disableScreenCapture);
        }
        if (this.requireAutoTime) {
            writeAttributeValueToXml(out, TAG_REQUIRE_AUTO_TIME, this.requireAutoTime);
        }
        if (this.forceEphemeralUsers) {
            writeAttributeValueToXml(out, TAG_FORCE_EPHEMERAL_USERS, this.forceEphemeralUsers);
        }
        if (this.isNetworkLoggingEnabled) {
            out.startTag((java.lang.String) null, TAG_IS_NETWORK_LOGGING_ENABLED);
            out.attributeBoolean((java.lang.String) null, ATTR_VALUE, this.isNetworkLoggingEnabled);
            out.attributeInt((java.lang.String) null, ATTR_NUM_NETWORK_LOGGING_NOTIFICATIONS, this.numNetworkLoggingNotifications);
            out.attributeLong((java.lang.String) null, ATTR_LAST_NETWORK_LOGGING_NOTIFICATION, this.lastNetworkLoggingNotificationTimeMs);
            out.endTag((java.lang.String) null, TAG_IS_NETWORK_LOGGING_ENABLED);
        }
        if (this.disabledKeyguardFeatures != 0) {
            writeAttributeValueToXml(out, TAG_DISABLE_KEYGUARD_FEATURES, this.disabledKeyguardFeatures);
        }
        if (!this.accountTypesWithManagementDisabled.isEmpty()) {
            writeAttributeValuesToXml(out, TAG_DISABLE_ACCOUNT_MANAGEMENT, TAG_ACCOUNT_TYPE, this.accountTypesWithManagementDisabled);
        }
        if (!this.trustAgentInfos.isEmpty()) {
            java.util.Set<java.util.Map.Entry<java.lang.String, com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo>> set = this.trustAgentInfos.entrySet();
            out.startTag((java.lang.String) null, TAG_MANAGE_TRUST_AGENT_FEATURES);
            for (java.util.Map.Entry<java.lang.String, com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo> entry : set) {
                com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo trustAgentInfo = entry.getValue();
                out.startTag((java.lang.String) null, TAG_TRUST_AGENT_COMPONENT);
                out.attribute((java.lang.String) null, ATTR_VALUE, entry.getKey());
                if (trustAgentInfo.options != null) {
                    out.startTag((java.lang.String) null, TAG_TRUST_AGENT_COMPONENT_OPTIONS);
                    try {
                        trustAgentInfo.options.saveToXml(out);
                    } catch (org.xmlpull.v1.XmlPullParserException e) {
                        com.android.server.utils.Slogf.e("DevicePolicyManager", e, "Failed to save TrustAgent options", new java.lang.Object[0]);
                    }
                    out.endTag((java.lang.String) null, TAG_TRUST_AGENT_COMPONENT_OPTIONS);
                }
                out.endTag((java.lang.String) null, TAG_TRUST_AGENT_COMPONENT);
            }
            out.endTag((java.lang.String) null, TAG_MANAGE_TRUST_AGENT_FEATURES);
        }
        if (this.crossProfileWidgetProviders != null && !this.crossProfileWidgetProviders.isEmpty()) {
            writeAttributeValuesToXml(out, TAG_CROSS_PROFILE_WIDGET_PROVIDERS, TAG_PROVIDER, this.crossProfileWidgetProviders);
        }
        writePackageListToXml(out, TAG_PERMITTED_ACCESSIBILITY_SERVICES, this.permittedAccessiblityServices);
        writePackageListToXml(out, TAG_PERMITTED_IMES, this.permittedInputMethods);
        writePackageListToXml(out, TAG_PERMITTED_NOTIFICATION_LISTENERS, this.permittedNotificationListeners);
        writePackageListToXml(out, TAG_KEEP_UNINSTALLED_PACKAGES, this.keepUninstalledPackages);
        writePackageListToXml(out, TAG_METERED_DATA_DISABLED_PACKAGES, this.meteredDisabledPackages);
        writePackageListToXml(out, TAG_PROTECTED_PACKAGES, this.protectedPackages);
        writePackageListToXml(out, TAG_SUSPENDED_PACKAGES, this.suspendedPackages);
        if (hasUserRestrictions()) {
            com.android.server.pm.UserRestrictionsUtils.writeRestrictions(out, this.userRestrictions, TAG_USER_RESTRICTIONS);
        }
        if (!this.defaultEnabledRestrictionsAlreadySet.isEmpty()) {
            writeAttributeValuesToXml(out, TAG_DEFAULT_ENABLED_USER_RESTRICTIONS, TAG_RESTRICTION, this.defaultEnabledRestrictionsAlreadySet);
        }
        if (!android.text.TextUtils.isEmpty(this.shortSupportMessage)) {
            writeTextToXml(out, TAG_SHORT_SUPPORT_MESSAGE, this.shortSupportMessage.toString());
        }
        if (!android.text.TextUtils.isEmpty(this.longSupportMessage)) {
            writeTextToXml(out, TAG_LONG_SUPPORT_MESSAGE, this.longSupportMessage.toString());
        }
        if (this.parentAdmin != null) {
            out.startTag((java.lang.String) null, TAG_PARENT_ADMIN);
            this.parentAdmin.writeToXml(out);
            out.endTag((java.lang.String) null, TAG_PARENT_ADMIN);
        }
        if (this.organizationColor != DEF_ORGANIZATION_COLOR) {
            writeAttributeValueToXml(out, TAG_ORGANIZATION_COLOR, this.organizationColor);
        }
        if (this.organizationName != null) {
            writeTextToXml(out, TAG_ORGANIZATION_NAME, this.organizationName);
        }
        if (this.isLogoutEnabled) {
            writeAttributeValueToXml(out, TAG_IS_LOGOUT_ENABLED, this.isLogoutEnabled);
        }
        if (this.startUserSessionMessage != null) {
            writeTextToXml(out, TAG_START_USER_SESSION_MESSAGE, this.startUserSessionMessage);
        }
        if (this.endUserSessionMessage != null) {
            writeTextToXml(out, TAG_END_USER_SESSION_MESSAGE, this.endUserSessionMessage);
        }
        if (this.mCrossProfileCalendarPackages != null) {
            writePackageListToXml(out, TAG_CROSS_PROFILE_CALENDAR_PACKAGES, this.mCrossProfileCalendarPackages);
        } else {
            out.startTag((java.lang.String) null, TAG_CROSS_PROFILE_CALENDAR_PACKAGES_NULL);
            out.endTag((java.lang.String) null, TAG_CROSS_PROFILE_CALENDAR_PACKAGES_NULL);
        }
        writePackageListToXml(out, TAG_CROSS_PROFILE_PACKAGES, this.mCrossProfilePackages);
        if (this.mFactoryResetProtectionPolicy != null) {
            out.startTag((java.lang.String) null, TAG_FACTORY_RESET_PROTECTION_POLICY);
            this.mFactoryResetProtectionPolicy.writeToXml(out);
            out.endTag((java.lang.String) null, TAG_FACTORY_RESET_PROTECTION_POLICY);
        }
        if (this.mSuspendPersonalApps) {
            writeAttributeValueToXml(out, TAG_SUSPEND_PERSONAL_APPS, this.mSuspendPersonalApps);
        }
        if (this.mProfileMaximumTimeOffMillis != 0) {
            writeAttributeValueToXml(out, TAG_PROFILE_MAXIMUM_TIME_OFF, this.mProfileMaximumTimeOffMillis);
        }
        if (this.mProfileMaximumTimeOffMillis != 0) {
            writeAttributeValueToXml(out, TAG_PROFILE_OFF_DEADLINE, this.mProfileOffDeadline);
        }
        if (!android.text.TextUtils.isEmpty(this.mAlwaysOnVpnPackage)) {
            writeAttributeValueToXml(out, TAG_ALWAYS_ON_VPN_PACKAGE, this.mAlwaysOnVpnPackage);
        }
        if (this.mAlwaysOnVpnLockdown) {
            writeAttributeValueToXml(out, TAG_ALWAYS_ON_VPN_LOCKDOWN, this.mAlwaysOnVpnLockdown);
        }
        if (this.mCommonCriteriaMode) {
            writeAttributeValueToXml(out, TAG_COMMON_CRITERIA_MODE, this.mCommonCriteriaMode);
        }
        if (this.mPasswordComplexity != 0) {
            writeAttributeValueToXml(out, TAG_PASSWORD_COMPLEXITY, this.mPasswordComplexity);
        }
        if (this.mNearbyNotificationStreamingPolicy != 3) {
            writeAttributeValueToXml(out, TAG_NEARBY_NOTIFICATION_STREAMING_POLICY, this.mNearbyNotificationStreamingPolicy);
        }
        if (this.mNearbyAppStreamingPolicy != 3) {
            writeAttributeValueToXml(out, TAG_NEARBY_APP_STREAMING_POLICY, this.mNearbyAppStreamingPolicy);
        }
        if (!android.text.TextUtils.isEmpty(this.mOrganizationId)) {
            writeTextToXml(out, TAG_ORGANIZATION_ID, this.mOrganizationId);
        }
        if (!android.text.TextUtils.isEmpty(this.mEnrollmentSpecificId)) {
            writeTextToXml(out, TAG_ENROLLMENT_SPECIFIC_ID, this.mEnrollmentSpecificId);
        }
        writeAttributeValueToXml(out, TAG_ADMIN_CAN_GRANT_SENSORS_PERMISSIONS, this.mAdminCanGrantSensorsPermissions);
        if (!this.mUsbDataSignalingEnabled) {
            writeAttributeValueToXml(out, TAG_USB_DATA_SIGNALING, this.mUsbDataSignalingEnabled);
        }
        if (this.mWifiMinimumSecurityLevel != 0) {
            writeAttributeValueToXml(out, TAG_WIFI_MIN_SECURITY, this.mWifiMinimumSecurityLevel);
        }
        if (this.mWifiSsidPolicy != null) {
            java.util.List<java.lang.String> ssids = ssidsToStrings(this.mWifiSsidPolicy.getSsids());
            if (this.mWifiSsidPolicy.getPolicyType() == 0) {
                writeAttributeValuesToXml(out, TAG_SSID_ALLOWLIST, TAG_SSID, ssids);
            } else if (this.mWifiSsidPolicy.getPolicyType() == 1) {
                writeAttributeValuesToXml(out, TAG_SSID_DENYLIST, TAG_SSID, ssids);
            }
        }
        if (!this.mPreferentialNetworkServiceConfigs.isEmpty()) {
            out.startTag((java.lang.String) null, TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIGS);
            for (android.app.admin.PreferentialNetworkServiceConfig config : this.mPreferentialNetworkServiceConfigs) {
                config.writeToXml(out);
            }
            out.endTag((java.lang.String) null, TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIGS);
        }
        if (this.mtePolicy != 0) {
            writeAttributeValueToXml(out, TAG_MTE_POLICY, this.mtePolicy);
        }
        writePackagePolicy(out, TAG_CROSS_PROFILE_CALLER_ID_POLICY, this.mManagedProfileCallerIdAccess);
        writePackagePolicy(out, TAG_CROSS_PROFILE_CONTACTS_SEARCH_POLICY, this.mManagedProfileContactsAccess);
        writePackagePolicy(out, TAG_CREDENTIAL_MANAGER_POLICY, this.mCredentialManagerPolicy);
        if (this.mManagedSubscriptionsPolicy != null) {
            out.startTag((java.lang.String) null, TAG_MANAGED_SUBSCRIPTIONS_POLICY);
            this.mManagedSubscriptionsPolicy.saveToXml(out);
            out.endTag((java.lang.String) null, TAG_MANAGED_SUBSCRIPTIONS_POLICY);
        }
        if (!android.text.TextUtils.isEmpty(this.mDialerPackage)) {
            writeAttributeValueToXml(out, TAG_DIALER_PACKAGE, this.mDialerPackage);
        }
        if (!android.text.TextUtils.isEmpty(this.mSmsPackage)) {
            writeAttributeValueToXml(out, TAG_SMS_PACKAGE, this.mSmsPackage);
        }
    }

    private void writePackagePolicy(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, android.app.admin.PackagePolicy packagePolicy) throws java.io.IOException {
        if (packagePolicy == null) {
            return;
        }
        out.startTag((java.lang.String) null, tag);
        out.attributeInt((java.lang.String) null, ATTR_PACKAGE_POLICY_MODE, packagePolicy.getPolicyType());
        writePackageListToXml(out, TAG_PACKAGE_POLICY_PACKAGE_NAMES, new java.util.ArrayList(packagePolicy.getPackageNames()));
        out.endTag((java.lang.String) null, tag);
    }

    private java.util.List<java.lang.String> ssidsToStrings(java.util.Set<android.net.wifi.WifiSsid> ssids) {
        return (java.util.List) ssids.stream().map(new java.util.function.Function() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.devicepolicy.ActiveAdmin.lambda$ssidsToStrings$0((android.net.wifi.WifiSsid) obj);
            }
        }).collect(java.util.stream.Collectors.toList());
    }

    static /* synthetic */ java.lang.String lambda$ssidsToStrings$0(android.net.wifi.WifiSsid ssid) {
        return new java.lang.String(ssid.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
    }

    void writeTextToXml(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, java.lang.String text) throws java.io.IOException {
        out.startTag((java.lang.String) null, tag);
        out.text(text);
        out.endTag((java.lang.String) null, tag);
    }

    void writePackageListToXml(com.android.modules.utils.TypedXmlSerializer out, java.lang.String outerTag, java.util.List<java.lang.String> packageList) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        if (packageList == null) {
            return;
        }
        writeAttributeValuesToXml(out, outerTag, "item", packageList);
    }

    void writeAttributeValueToXml(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, java.lang.String value) throws java.io.IOException {
        out.startTag((java.lang.String) null, tag);
        out.attribute((java.lang.String) null, ATTR_VALUE, value);
        out.endTag((java.lang.String) null, tag);
    }

    void writeAttributeValueToXml(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, int value) throws java.io.IOException {
        out.startTag((java.lang.String) null, tag);
        out.attributeInt((java.lang.String) null, ATTR_VALUE, value);
        out.endTag((java.lang.String) null, tag);
    }

    void writeAttributeValueToXml(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, long value) throws java.io.IOException {
        out.startTag((java.lang.String) null, tag);
        out.attributeLong((java.lang.String) null, ATTR_VALUE, value);
        out.endTag((java.lang.String) null, tag);
    }

    void writeAttributeValueToXml(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, boolean value) throws java.io.IOException {
        out.startTag((java.lang.String) null, tag);
        out.attributeBoolean((java.lang.String) null, ATTR_VALUE, value);
        out.endTag((java.lang.String) null, tag);
    }

    void writeAttributeValuesToXml(com.android.modules.utils.TypedXmlSerializer out, java.lang.String outerTag, java.lang.String innerTag, java.util.Collection<java.lang.String> values) throws java.io.IOException {
        out.startTag((java.lang.String) null, outerTag);
        for (java.lang.String value : values) {
            out.startTag((java.lang.String) null, innerTag);
            out.attribute((java.lang.String) null, ATTR_VALUE, value);
            out.endTag((java.lang.String) null, innerTag);
        }
        out.endTag((java.lang.String) null, outerTag);
    }

    void readFromXml(com.android.modules.utils.TypedXmlPullParser parser, boolean shouldOverridePolicies) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tag = parser.getName();
                        if (TAG_POLICIES.equals(tag)) {
                            if (shouldOverridePolicies) {
                                com.android.server.utils.Slogf.d("DevicePolicyManager", "Overriding device admin policies from XML.");
                                this.info.readPoliciesFromXml(parser);
                            }
                        } else if (TAG_PASSWORD_QUALITY.equals(tag)) {
                            this.mPasswordPolicy.quality = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_MIN_PASSWORD_LENGTH.equals(tag)) {
                            this.mPasswordPolicy.length = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_PASSWORD_HISTORY_LENGTH.equals(tag)) {
                            this.passwordHistoryLength = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_MIN_PASSWORD_UPPERCASE.equals(tag)) {
                            this.mPasswordPolicy.upperCase = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_MIN_PASSWORD_LOWERCASE.equals(tag)) {
                            this.mPasswordPolicy.lowerCase = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_MIN_PASSWORD_LETTERS.equals(tag)) {
                            this.mPasswordPolicy.letters = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_MIN_PASSWORD_NUMERIC.equals(tag)) {
                            this.mPasswordPolicy.numeric = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_MIN_PASSWORD_SYMBOLS.equals(tag)) {
                            this.mPasswordPolicy.symbols = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_MIN_PASSWORD_NONLETTER.equals(tag)) {
                            this.mPasswordPolicy.nonLetter = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_MAX_TIME_TO_UNLOCK.equals(tag)) {
                            this.maximumTimeToUnlock = parser.getAttributeLong((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_STRONG_AUTH_UNLOCK_TIMEOUT.equals(tag)) {
                            this.strongAuthUnlockTimeout = parser.getAttributeLong((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_MAX_FAILED_PASSWORD_WIPE.equals(tag)) {
                            this.maximumFailedPasswordsForWipe = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_SPECIFIES_GLOBAL_PROXY.equals(tag)) {
                            this.specifiesGlobalProxy = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_GLOBAL_PROXY_SPEC.equals(tag)) {
                            this.globalProxySpec = parser.getAttributeValue((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_GLOBAL_PROXY_EXCLUSION_LIST.equals(tag)) {
                            this.globalProxyExclusionList = parser.getAttributeValue((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_PASSWORD_EXPIRATION_TIMEOUT.equals(tag)) {
                            this.passwordExpirationTimeout = parser.getAttributeLong((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_PASSWORD_EXPIRATION_DATE.equals(tag)) {
                            this.passwordExpirationDate = parser.getAttributeLong((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_ENCRYPTION_REQUESTED.equals(tag)) {
                            this.encryptionRequested = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_TEST_ONLY_ADMIN.equals(tag)) {
                            this.testOnlyAdmin = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_DISABLE_CAMERA.equals(tag)) {
                            this.disableCamera = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_DISABLE_CALLER_ID.equals(tag)) {
                            this.disableCallerId = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_DISABLE_CONTACTS_SEARCH.equals(tag)) {
                            this.disableContactsSearch = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_DISABLE_BLUETOOTH_CONTACT_SHARING.equals(tag)) {
                            this.disableBluetoothContactSharing = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_DISABLE_SCREEN_CAPTURE.equals(tag)) {
                            this.disableScreenCapture = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_REQUIRE_AUTO_TIME.equals(tag)) {
                            this.requireAutoTime = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_FORCE_EPHEMERAL_USERS.equals(tag)) {
                            this.forceEphemeralUsers = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_IS_NETWORK_LOGGING_ENABLED.equals(tag)) {
                            this.isNetworkLoggingEnabled = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                            this.lastNetworkLoggingNotificationTimeMs = parser.getAttributeLong((java.lang.String) null, ATTR_LAST_NETWORK_LOGGING_NOTIFICATION);
                            this.numNetworkLoggingNotifications = parser.getAttributeInt((java.lang.String) null, ATTR_NUM_NETWORK_LOGGING_NOTIFICATIONS);
                        } else if (TAG_DISABLE_KEYGUARD_FEATURES.equals(tag)) {
                            this.disabledKeyguardFeatures = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_DISABLE_ACCOUNT_MANAGEMENT.equals(tag)) {
                            readAttributeValues(parser, TAG_ACCOUNT_TYPE, this.accountTypesWithManagementDisabled);
                        } else if (TAG_MANAGE_TRUST_AGENT_FEATURES.equals(tag)) {
                            this.trustAgentInfos = getAllTrustAgentInfos(parser, tag);
                        } else if (TAG_CROSS_PROFILE_WIDGET_PROVIDERS.equals(tag)) {
                            this.crossProfileWidgetProviders = new java.util.ArrayList();
                            readAttributeValues(parser, TAG_PROVIDER, this.crossProfileWidgetProviders);
                        } else if (TAG_PERMITTED_ACCESSIBILITY_SERVICES.equals(tag)) {
                            this.permittedAccessiblityServices = readPackageList(parser, tag);
                        } else if (TAG_PERMITTED_IMES.equals(tag)) {
                            this.permittedInputMethods = readPackageList(parser, tag);
                        } else if (TAG_PERMITTED_NOTIFICATION_LISTENERS.equals(tag)) {
                            this.permittedNotificationListeners = readPackageList(parser, tag);
                        } else if (TAG_KEEP_UNINSTALLED_PACKAGES.equals(tag)) {
                            this.keepUninstalledPackages = readPackageList(parser, tag);
                        } else if (TAG_METERED_DATA_DISABLED_PACKAGES.equals(tag)) {
                            this.meteredDisabledPackages = readPackageList(parser, tag);
                        } else if (TAG_PROTECTED_PACKAGES.equals(tag)) {
                            this.protectedPackages = readPackageList(parser, tag);
                        } else if (TAG_SUSPENDED_PACKAGES.equals(tag)) {
                            this.suspendedPackages = readPackageList(parser, tag);
                        } else if (TAG_USER_RESTRICTIONS.equals(tag)) {
                            this.userRestrictions = com.android.server.pm.UserRestrictionsUtils.readRestrictions(parser);
                        } else if (TAG_DEFAULT_ENABLED_USER_RESTRICTIONS.equals(tag)) {
                            readAttributeValues(parser, TAG_RESTRICTION, this.defaultEnabledRestrictionsAlreadySet);
                        } else if (TAG_SHORT_SUPPORT_MESSAGE.equals(tag)) {
                            if (parser.next() == 4) {
                                this.shortSupportMessage = parser.getText();
                            } else {
                                com.android.server.utils.Slogf.w("DevicePolicyManager", "Missing text when loading short support message");
                            }
                        } else if (TAG_LONG_SUPPORT_MESSAGE.equals(tag)) {
                            if (parser.next() == 4) {
                                this.longSupportMessage = parser.getText();
                            } else {
                                com.android.server.utils.Slogf.w("DevicePolicyManager", "Missing text when loading long support message");
                            }
                        } else if (TAG_PARENT_ADMIN.equals(tag)) {
                            com.android.internal.util.Preconditions.checkState(!this.isParent);
                            this.parentAdmin = new com.android.server.devicepolicy.ActiveAdmin(this.info, true);
                            this.parentAdmin.readFromXml(parser, shouldOverridePolicies);
                        } else if (TAG_ORGANIZATION_COLOR.equals(tag)) {
                            this.organizationColor = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_ORGANIZATION_NAME.equals(tag)) {
                            if (parser.next() == 4) {
                                this.organizationName = parser.getText();
                            } else {
                                com.android.server.utils.Slogf.w("DevicePolicyManager", "Missing text when loading organization name");
                            }
                        } else if (TAG_IS_LOGOUT_ENABLED.equals(tag)) {
                            this.isLogoutEnabled = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_START_USER_SESSION_MESSAGE.equals(tag)) {
                            if (parser.next() == 4) {
                                this.startUserSessionMessage = parser.getText();
                            } else {
                                com.android.server.utils.Slogf.w("DevicePolicyManager", "Missing text when loading start session message");
                            }
                        } else if (TAG_END_USER_SESSION_MESSAGE.equals(tag)) {
                            if (parser.next() == 4) {
                                this.endUserSessionMessage = parser.getText();
                            } else {
                                com.android.server.utils.Slogf.w("DevicePolicyManager", "Missing text when loading end session message");
                            }
                        } else if (TAG_CROSS_PROFILE_CALENDAR_PACKAGES.equals(tag)) {
                            this.mCrossProfileCalendarPackages = readPackageList(parser, tag);
                        } else if (TAG_CROSS_PROFILE_CALENDAR_PACKAGES_NULL.equals(tag)) {
                            this.mCrossProfileCalendarPackages = null;
                        } else if (TAG_CROSS_PROFILE_PACKAGES.equals(tag)) {
                            this.mCrossProfilePackages = readPackageList(parser, tag);
                        } else if (TAG_FACTORY_RESET_PROTECTION_POLICY.equals(tag)) {
                            this.mFactoryResetProtectionPolicy = android.app.admin.FactoryResetProtectionPolicy.readFromXml(parser);
                        } else if (TAG_SUSPEND_PERSONAL_APPS.equals(tag)) {
                            this.mSuspendPersonalApps = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_PROFILE_MAXIMUM_TIME_OFF.equals(tag)) {
                            this.mProfileMaximumTimeOffMillis = parser.getAttributeLong((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_PROFILE_OFF_DEADLINE.equals(tag)) {
                            this.mProfileOffDeadline = parser.getAttributeLong((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_ALWAYS_ON_VPN_PACKAGE.equals(tag)) {
                            this.mAlwaysOnVpnPackage = parser.getAttributeValue((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_ALWAYS_ON_VPN_LOCKDOWN.equals(tag)) {
                            this.mAlwaysOnVpnLockdown = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_PREFERENTIAL_NETWORK_SERVICE_ENABLED.equals(tag)) {
                            boolean preferentialNetworkServiceEnabled = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                            if (preferentialNetworkServiceEnabled) {
                                android.app.admin.PreferentialNetworkServiceConfig.Builder configBuilder = new android.app.admin.PreferentialNetworkServiceConfig.Builder();
                                configBuilder.setEnabled(preferentialNetworkServiceEnabled);
                                configBuilder.setNetworkId(1);
                                this.mPreferentialNetworkServiceConfigs = java.util.List.of(configBuilder.build());
                            }
                        } else if (TAG_COMMON_CRITERIA_MODE.equals(tag)) {
                            this.mCommonCriteriaMode = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_PASSWORD_COMPLEXITY.equals(tag)) {
                            this.mPasswordComplexity = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_NEARBY_NOTIFICATION_STREAMING_POLICY.equals(tag)) {
                            this.mNearbyNotificationStreamingPolicy = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_NEARBY_APP_STREAMING_POLICY.equals(tag)) {
                            this.mNearbyAppStreamingPolicy = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_ORGANIZATION_ID.equals(tag)) {
                            if (parser.next() == 4) {
                                this.mOrganizationId = parser.getText();
                            } else {
                                com.android.server.utils.Slogf.w("DevicePolicyManager", "Missing Organization ID.");
                            }
                        } else if (TAG_ENROLLMENT_SPECIFIC_ID.equals(tag)) {
                            if (parser.next() == 4) {
                                this.mEnrollmentSpecificId = parser.getText();
                            } else {
                                com.android.server.utils.Slogf.w("DevicePolicyManager", "Missing Enrollment-specific ID.");
                            }
                        } else if (TAG_ADMIN_CAN_GRANT_SENSORS_PERMISSIONS.equals(tag)) {
                            this.mAdminCanGrantSensorsPermissions = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, false);
                        } else if (TAG_USB_DATA_SIGNALING.equals(tag)) {
                            this.mUsbDataSignalingEnabled = parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE, true);
                        } else if (TAG_WIFI_MIN_SECURITY.equals(tag)) {
                            this.mWifiMinimumSecurityLevel = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_SSID_ALLOWLIST.equals(tag)) {
                            java.util.List<android.net.wifi.WifiSsid> ssids = readWifiSsids(parser, TAG_SSID);
                            this.mWifiSsidPolicy = new android.app.admin.WifiSsidPolicy(0, new android.util.ArraySet(ssids));
                        } else if (TAG_SSID_DENYLIST.equals(tag)) {
                            java.util.List<android.net.wifi.WifiSsid> ssids2 = readWifiSsids(parser, TAG_SSID);
                            this.mWifiSsidPolicy = new android.app.admin.WifiSsidPolicy(1, new android.util.ArraySet(ssids2));
                        } else if (TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIGS.equals(tag)) {
                            java.util.List<android.app.admin.PreferentialNetworkServiceConfig> configs = getPreferentialNetworkServiceConfigs(parser, tag);
                            if (!configs.isEmpty()) {
                                this.mPreferentialNetworkServiceConfigs = configs;
                            }
                        } else if (TAG_MTE_POLICY.equals(tag)) {
                            this.mtePolicy = parser.getAttributeInt((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_CROSS_PROFILE_CALLER_ID_POLICY.equals(tag)) {
                            this.mManagedProfileCallerIdAccess = readPackagePolicy(parser);
                        } else if (TAG_CROSS_PROFILE_CONTACTS_SEARCH_POLICY.equals(tag)) {
                            this.mManagedProfileContactsAccess = readPackagePolicy(parser);
                        } else if (TAG_MANAGED_SUBSCRIPTIONS_POLICY.equals(tag)) {
                            this.mManagedSubscriptionsPolicy = android.app.admin.ManagedSubscriptionsPolicy.readFromXml(parser);
                        } else if (TAG_CREDENTIAL_MANAGER_POLICY.equals(tag)) {
                            this.mCredentialManagerPolicy = readPackagePolicy(parser);
                        } else if (TAG_DIALER_PACKAGE.equals(tag)) {
                            this.mDialerPackage = parser.getAttributeValue((java.lang.String) null, ATTR_VALUE);
                        } else if (TAG_SMS_PACKAGE.equals(tag)) {
                            this.mSmsPackage = parser.getAttributeValue((java.lang.String) null, ATTR_VALUE);
                        } else {
                            com.android.server.utils.Slogf.w("DevicePolicyManager", "Unknown admin tag: %s", tag);
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private android.app.admin.PackagePolicy readPackagePolicy(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int policy = parser.getAttributeInt((java.lang.String) null, ATTR_PACKAGE_POLICY_MODE);
        java.util.Set<java.lang.String> packageNames = new android.util.ArraySet<>(readPackageList(parser, TAG_PACKAGE_POLICY_PACKAGE_NAMES));
        return new android.app.admin.PackagePolicy(policy, packageNames);
    }

    private java.util.List<android.net.wifi.WifiSsid> readWifiSsids(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tag) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.List<java.lang.String> ssidStrings = new java.util.ArrayList<>();
        readAttributeValues(parser, tag, ssidStrings);
        java.util.List<android.net.wifi.WifiSsid> ssids = (java.util.List) ssidStrings.stream().map(new java.util.function.Function() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.net.wifi.WifiSsid.fromBytes(((java.lang.String) obj).getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
        }).collect(java.util.stream.Collectors.toList());
        return ssids;
    }

    private java.util.List<java.lang.String> readPackageList(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tag) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        int outerDepth = parser.getDepth();
        while (true) {
            int outerType = parser.next();
            if (outerType == 1 || (outerType == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (outerType != 3 && outerType != 4) {
                java.lang.String outerTag = parser.getName();
                if ("item".equals(outerTag)) {
                    java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, ATTR_VALUE);
                    if (packageName != null) {
                        result.add(packageName);
                    } else {
                        com.android.server.utils.Slogf.w("DevicePolicyManager", "Package name missing under %s", outerTag);
                    }
                } else {
                    com.android.server.utils.Slogf.w("DevicePolicyManager", "Unknown tag under %s: ", tag, outerTag);
                }
            }
        }
        return result;
    }

    private void readAttributeValues(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tag, java.util.Collection<java.lang.String> result) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        result.clear();
        int outerDepthDAM = parser.getDepth();
        while (true) {
            int typeDAM = parser.next();
            if (typeDAM != 1) {
                if (typeDAM != 3 || parser.getDepth() > outerDepthDAM) {
                    if (typeDAM != 3 && typeDAM != 4) {
                        java.lang.String tagDAM = parser.getName();
                        if (tag.equals(tagDAM)) {
                            result.add(parser.getAttributeValue((java.lang.String) null, ATTR_VALUE));
                        } else {
                            com.android.server.utils.Slogf.e("DevicePolicyManager", "Expected tag %s but found %s", tag, tagDAM);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private android.util.ArrayMap<java.lang.String, com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo> getAllTrustAgentInfos(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tag) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepthDAM = parser.getDepth();
        android.util.ArrayMap<java.lang.String, com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo> result = new android.util.ArrayMap<>();
        while (true) {
            int typeDAM = parser.next();
            if (typeDAM == 1 || (typeDAM == 3 && parser.getDepth() <= outerDepthDAM)) {
                break;
            }
            if (typeDAM != 3 && typeDAM != 4) {
                java.lang.String tagDAM = parser.getName();
                if (TAG_TRUST_AGENT_COMPONENT.equals(tagDAM)) {
                    java.lang.String component = parser.getAttributeValue((java.lang.String) null, ATTR_VALUE);
                    com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo trustAgentInfo = getTrustAgentInfo(parser, tag);
                    result.put(component, trustAgentInfo);
                } else {
                    com.android.server.utils.Slogf.w("DevicePolicyManager", "Unknown tag under %s: %s", tag, tagDAM);
                }
            }
        }
        return result;
    }

    private com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo getTrustAgentInfo(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String outerTag) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo result = new com.android.server.devicepolicy.ActiveAdmin.TrustAgentInfo(null);
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tag = parser.getName();
                if (TAG_TRUST_AGENT_COMPONENT_OPTIONS.equals(tag)) {
                    result.options = android.os.PersistableBundle.restoreFromXml(parser);
                } else {
                    com.android.server.utils.Slogf.w("DevicePolicyManager", "Unknown tag under %s: %s", outerTag, tag);
                }
            }
        }
        return result;
    }

    private java.util.List<android.app.admin.PreferentialNetworkServiceConfig> getPreferentialNetworkServiceConfigs(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tag) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        java.util.List<android.app.admin.PreferentialNetworkServiceConfig> result = new java.util.ArrayList<>();
        while (true) {
            int typeDAM = parser.next();
            if (typeDAM == 1 || (typeDAM == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (typeDAM != 3 && typeDAM != 4) {
                java.lang.String tagDAM = parser.getName();
                if (TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIG.equals(tagDAM)) {
                    android.app.admin.PreferentialNetworkServiceConfig preferentialNetworkServiceConfig = android.app.admin.PreferentialNetworkServiceConfig.getPreferentialNetworkServiceConfig(parser, tag);
                    result.add(preferentialNetworkServiceConfig);
                } else {
                    com.android.server.utils.Slogf.w("DevicePolicyManager", "Unknown tag under %s: %s", tag, tagDAM);
                }
            }
        }
        return result;
    }

    boolean hasUserRestrictions() {
        return this.userRestrictions != null && this.userRestrictions.size() > 0;
    }

    android.os.Bundle ensureUserRestrictions() {
        if (this.userRestrictions == null) {
            this.userRestrictions = new android.os.Bundle();
        }
        return this.userRestrictions;
    }

    public void transfer(android.app.admin.DeviceAdminInfo deviceAdminInfo) {
        if (hasParentActiveAdmin()) {
            this.parentAdmin.info = deviceAdminInfo;
        }
        this.info = deviceAdminInfo;
    }

    android.os.Bundle addSyntheticRestrictions(android.os.Bundle restrictions) {
        if (this.disableCamera) {
            restrictions.putBoolean("no_camera", true);
        }
        if (this.requireAutoTime) {
            restrictions.putBoolean("no_config_date_time", true);
        }
        return restrictions;
    }

    static android.os.Bundle removeDeprecatedRestrictions(android.os.Bundle restrictions) {
        for (java.lang.String deprecatedRestriction : com.android.server.pm.UserRestrictionsUtils.DEPRECATED_USER_RESTRICTIONS) {
            restrictions.remove(deprecatedRestriction);
        }
        return restrictions;
    }

    static android.os.Bundle filterRestrictions(android.os.Bundle restrictions, java.util.function.Predicate<java.lang.String> filter) {
        android.os.Bundle result = new android.os.Bundle();
        for (java.lang.String key : restrictions.keySet()) {
            if (restrictions.getBoolean(key) && filter.test(key)) {
                result.putBoolean(key, true);
            }
        }
        return result;
    }

    android.os.Bundle getEffectiveRestrictions() {
        return addSyntheticRestrictions(removeDeprecatedRestrictions(new android.os.Bundle(ensureUserRestrictions())));
    }

    android.os.Bundle getLocalUserRestrictions(final int adminType) {
        return filterRestrictions(getEffectiveRestrictions(), new java.util.function.Predicate() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.pm.UserRestrictionsUtils.isLocal(adminType, (java.lang.String) obj);
            }
        });
    }

    android.os.Bundle getGlobalUserRestrictions(final int adminType) {
        return filterRestrictions(getEffectiveRestrictions(), new java.util.function.Predicate() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.pm.UserRestrictionsUtils.isGlobal(adminType, (java.lang.String) obj);
            }
        });
    }

    void dumpPackagePolicy(final android.util.IndentingPrintWriter pw, java.lang.String name, android.app.admin.PackagePolicy policy) {
        pw.print(name);
        pw.println(":");
        if (policy != null) {
            pw.increaseIndent();
            pw.print("policyType=");
            pw.println(policy.getPolicyType());
            pw.println("packageNames:");
            pw.increaseIndent();
            policy.getPackageNames().forEach(new java.util.function.Consumer() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    pw.println((java.lang.String) obj);
                }
            });
            pw.decreaseIndent();
            pw.decreaseIndent();
        }
    }

    void dump(android.util.IndentingPrintWriter pw) {
        pw.print("uid=");
        pw.println(getUid());
        pw.print("testOnlyAdmin=");
        pw.println(this.testOnlyAdmin);
        if (this.info != null) {
            pw.println("policies:");
            java.util.ArrayList<android.app.admin.DeviceAdminInfo.PolicyInfo> pols = this.info.getUsedPolicies();
            if (pols != null) {
                pw.increaseIndent();
                for (int i = 0; i < pols.size(); i++) {
                    pw.println(pols.get(i).tag);
                }
                pw.decreaseIndent();
            }
        }
        pw.print("passwordQuality=0x");
        pw.println(java.lang.Integer.toHexString(this.mPasswordPolicy.quality));
        pw.print("minimumPasswordLength=");
        pw.println(this.mPasswordPolicy.length);
        pw.print("passwordHistoryLength=");
        pw.println(this.passwordHistoryLength);
        pw.print("minimumPasswordUpperCase=");
        pw.println(this.mPasswordPolicy.upperCase);
        pw.print("minimumPasswordLowerCase=");
        pw.println(this.mPasswordPolicy.lowerCase);
        pw.print("minimumPasswordLetters=");
        pw.println(this.mPasswordPolicy.letters);
        pw.print("minimumPasswordNumeric=");
        pw.println(this.mPasswordPolicy.numeric);
        pw.print("minimumPasswordSymbols=");
        pw.println(this.mPasswordPolicy.symbols);
        pw.print("minimumPasswordNonLetter=");
        pw.println(this.mPasswordPolicy.nonLetter);
        pw.print("maximumTimeToUnlock=");
        pw.println(this.maximumTimeToUnlock);
        pw.print("strongAuthUnlockTimeout=");
        pw.println(this.strongAuthUnlockTimeout);
        pw.print("maximumFailedPasswordsForWipe=");
        pw.println(this.maximumFailedPasswordsForWipe);
        pw.print("specifiesGlobalProxy=");
        pw.println(this.specifiesGlobalProxy);
        pw.print("passwordExpirationTimeout=");
        pw.println(this.passwordExpirationTimeout);
        pw.print("passwordExpirationDate=");
        pw.println(this.passwordExpirationDate);
        if (this.globalProxySpec != null) {
            pw.print("globalProxySpec=");
            pw.println(this.globalProxySpec);
        }
        if (this.globalProxyExclusionList != null) {
            pw.print("globalProxyEclusionList=");
            pw.println(this.globalProxyExclusionList);
        }
        pw.print("encryptionRequested=");
        pw.println(this.encryptionRequested);
        if (!android.app.admin.flags.Flags.dumpsysPolicyEngineMigrationEnabled()) {
            pw.print("disableCamera=");
            pw.println(this.disableCamera);
            pw.print("disableScreenCapture=");
            pw.println(this.disableScreenCapture);
            pw.print("requireAutoTime=");
            pw.println(this.requireAutoTime);
            if (this.permittedInputMethods != null) {
                pw.print("permittedInputMethods=");
                pw.println(this.permittedInputMethods);
            }
            pw.println("userRestrictions:");
            com.android.server.pm.UserRestrictionsUtils.dumpRestrictions(pw, "  ", this.userRestrictions);
        }
        if (!android.app.admin.flags.Flags.policyEngineMigrationV2Enabled() || !android.app.admin.flags.Flags.dumpsysPolicyEngineMigrationEnabled()) {
            pw.print("mUsbDataSignaling=");
            pw.println(this.mUsbDataSignalingEnabled);
        }
        pw.print("disableCallerId=");
        pw.println(this.disableCallerId);
        pw.print("disableContactsSearch=");
        pw.println(this.disableContactsSearch);
        pw.print("disableBluetoothContactSharing=");
        pw.println(this.disableBluetoothContactSharing);
        pw.print("forceEphemeralUsers=");
        pw.println(this.forceEphemeralUsers);
        pw.print("isNetworkLoggingEnabled=");
        pw.println(this.isNetworkLoggingEnabled);
        pw.print("disabledKeyguardFeatures=");
        pw.println(this.disabledKeyguardFeatures);
        pw.print("crossProfileWidgetProviders=");
        pw.println(this.crossProfileWidgetProviders);
        if (this.permittedAccessiblityServices != null) {
            pw.print("permittedAccessibilityServices=");
            pw.println(this.permittedAccessiblityServices);
        }
        if (this.permittedNotificationListeners != null) {
            pw.print("permittedNotificationListeners=");
            pw.println(this.permittedNotificationListeners);
        }
        if (this.keepUninstalledPackages != null) {
            pw.print("keepUninstalledPackages=");
            pw.println(this.keepUninstalledPackages);
        }
        if (this.meteredDisabledPackages != null) {
            pw.print("meteredDisabledPackages=");
            pw.println(this.meteredDisabledPackages);
        }
        if (this.protectedPackages != null) {
            pw.print("protectedPackages=");
            pw.println(this.protectedPackages);
        }
        if (this.suspendedPackages != null) {
            pw.print("suspendedPackages=");
            pw.println(this.suspendedPackages);
        }
        pw.print("organizationColor=");
        pw.println(this.organizationColor);
        if (this.organizationName != null) {
            pw.print("organizationName=");
            pw.println(this.organizationName);
        }
        pw.print("defaultEnabledRestrictionsAlreadySet=");
        pw.println(this.defaultEnabledRestrictionsAlreadySet);
        dumpPackagePolicy(pw, "managedProfileCallerIdPolicy", this.mManagedProfileCallerIdAccess);
        dumpPackagePolicy(pw, "managedProfileContactsPolicy", this.mManagedProfileContactsAccess);
        dumpPackagePolicy(pw, "credentialManagerPolicy", this.mCredentialManagerPolicy);
        pw.print("isParent=");
        pw.println(this.isParent);
        if (this.parentAdmin != null) {
            pw.println("parentAdmin:");
            pw.increaseIndent();
            this.parentAdmin.dump(pw);
            pw.decreaseIndent();
        }
        if (this.mCrossProfileCalendarPackages != null) {
            pw.print("mCrossProfileCalendarPackages=");
            pw.println(this.mCrossProfileCalendarPackages);
        }
        pw.print("mCrossProfilePackages=");
        pw.println(this.mCrossProfilePackages);
        pw.print("mSuspendPersonalApps=");
        pw.println(this.mSuspendPersonalApps);
        pw.print("mProfileMaximumTimeOffMillis=");
        pw.println(this.mProfileMaximumTimeOffMillis);
        pw.print("mProfileOffDeadline=");
        pw.println(this.mProfileOffDeadline);
        pw.print("mAlwaysOnVpnPackage=");
        pw.println(this.mAlwaysOnVpnPackage);
        pw.print("mAlwaysOnVpnLockdown=");
        pw.println(this.mAlwaysOnVpnLockdown);
        pw.print("mCommonCriteriaMode=");
        pw.println(this.mCommonCriteriaMode);
        pw.print("mPasswordComplexity=");
        pw.println(this.mPasswordComplexity);
        pw.print("mNearbyNotificationStreamingPolicy=");
        pw.println(this.mNearbyNotificationStreamingPolicy);
        pw.print("mNearbyAppStreamingPolicy=");
        pw.println(this.mNearbyAppStreamingPolicy);
        if (!android.text.TextUtils.isEmpty(this.mOrganizationId)) {
            pw.print("mOrganizationId=");
            pw.println(this.mOrganizationId);
        }
        if (!android.text.TextUtils.isEmpty(this.mEnrollmentSpecificId)) {
            pw.print("mEnrollmentSpecificId=");
            pw.println(this.mEnrollmentSpecificId);
        }
        pw.print("mAdminCanGrantSensorsPermissions=");
        pw.println(this.mAdminCanGrantSensorsPermissions);
        pw.print("mWifiMinimumSecurityLevel=");
        pw.println(this.mWifiMinimumSecurityLevel);
        if (this.mWifiSsidPolicy != null) {
            if (this.mWifiSsidPolicy.getPolicyType() == 0) {
                pw.print("mSsidAllowlist=");
            } else {
                pw.print("mSsidDenylist=");
            }
            pw.println(ssidsToStrings(this.mWifiSsidPolicy.getSsids()));
        }
        if (this.mFactoryResetProtectionPolicy != null) {
            pw.println("mFactoryResetProtectionPolicy:");
            pw.increaseIndent();
            this.mFactoryResetProtectionPolicy.dump(pw);
            pw.decreaseIndent();
        }
        if (this.mPreferentialNetworkServiceConfigs != null) {
            pw.println("mPreferentialNetworkServiceConfigs:");
            pw.increaseIndent();
            for (android.app.admin.PreferentialNetworkServiceConfig config : this.mPreferentialNetworkServiceConfigs) {
                config.dump(pw);
            }
            pw.decreaseIndent();
        }
        pw.print("mtePolicy=");
        pw.println(this.mtePolicy);
        pw.print("accountTypesWithManagementDisabled=");
        pw.println(this.accountTypesWithManagementDisabled);
        if (this.mManagedSubscriptionsPolicy != null) {
            pw.println("mManagedSubscriptionsPolicy:");
            pw.increaseIndent();
            pw.println(this.mManagedSubscriptionsPolicy);
            pw.decreaseIndent();
        }
        pw.print("mDialerPackage=");
        pw.println(this.mDialerPackage);
        pw.print("mSmsPackage=");
        pw.println(this.mSmsPackage);
    }
}
