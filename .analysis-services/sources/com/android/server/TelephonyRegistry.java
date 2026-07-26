package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyRegistry extends com.android.internal.telephony.ITelephonyRegistry.Stub {
    private static final java.lang.String ACTION_ANY_DATA_CONNECTION_STATE_CHANGED = "android.intent.action.ANY_DATA_STATE";
    private static final java.lang.String ACTION_RADIO_POWER_STATE_CHANGED = "org.codeaurora.intent.action.RADIO_POWER_STATE";
    public static final java.lang.String ACTION_SIGNAL_STRENGTH_CHANGED = "android.intent.action.SIG_STR";
    private static final java.lang.String ACTION_SUBSCRIPTION_PHONE_STATE_CHANGED = "android.intent.action.SUBSCRIPTION_PHONE_STATE";
    private static final boolean DBG = false;
    private static final boolean DBG_LOC = false;
    private static final long DISPLAY_INFO_NR_ADVANCED_SUPPORTED = 181658987;
    private static final java.lang.String EXTRA_SUBSCRIPTION_INDEX = "android.telephony.extra.SUBSCRIPTION_INDEX";
    private static final int MSG_UPDATE_DEFAULT_SUB = 2;
    private static final int MSG_USER_SWITCHED = 1;
    private static final java.lang.String PHONE_CONSTANTS_DATA_APN_KEY = "apn";
    private static final java.lang.String PHONE_CONSTANTS_DATA_APN_TYPE_KEY = "apnType";
    private static final java.lang.String PHONE_CONSTANTS_SLOT_KEY = "slot";
    private static final java.lang.String PHONE_CONSTANTS_STATE_KEY = "state";
    private static final java.lang.String PHONE_CONSTANTS_SUBSCRIPTION_KEY = "subscription";
    private static final long REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_ACTIVE_DATA_SUB_ID = 182478738;
    private static final long REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_CELL_INFO = 184323934;
    private static final long REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_DISPLAY_INFO = 183164979;
    private static final java.lang.String TAG = "TelephonyRegistry";
    private static final boolean VDBG = false;
    private int[] mAllowedNetworkTypeReason;
    private long[] mAllowedNetworkTypeValue;
    private final android.app.AppOpsManager mAppOps;
    private int[] mBackgroundCallState;
    private java.util.List<android.telephony.BarringInfo> mBarringInfo;
    private int[] mCallDisconnectCause;
    private boolean[] mCallForwarding;
    private java.lang.String[] mCallIncomingNumber;
    private int[] mCallNetworkType;
    private int[] mCallPreciseDisconnectCause;
    private android.telephony.CallQuality[] mCallQuality;
    private int[] mCallState;
    private java.util.ArrayList<java.util.List<android.telephony.CallState>> mCallStateLists;
    private boolean[] mCarrierNetworkChangeState;
    private java.util.List<android.util.Pair<java.util.List<java.lang.String>, int[]>> mCarrierPrivilegeStates;
    private boolean[] mCarrierRoamingNtnMode;
    private java.util.List<android.util.Pair<java.lang.String, java.lang.Integer>> mCarrierServiceStates;
    private android.telephony.CellIdentity[] mCellIdentity;
    private java.util.ArrayList<java.util.List<android.telephony.CellInfo>> mCellInfo;
    private com.android.server.TelephonyRegistry.ConfigurationProvider mConfigurationProvider;
    private final android.content.Context mContext;
    private int[] mDataActivationState;
    private int[] mDataActivity;
    private int[] mDataConnectionNetworkType;
    private int[] mDataConnectionState;
    private int[] mDataEnabledReason;
    private int[] mECBMReason;
    private boolean[] mECBMStarted;
    private java.util.Map<java.lang.Integer, java.util.List<android.telephony.emergency.EmergencyNumber>> mEmergencyNumberList;
    private int[] mForegroundCallState;
    private java.util.List<android.telephony.ims.ImsReasonInfo> mImsReasonInfo;
    private boolean[] mIsDataEnabled;
    private java.util.List<java.util.List<android.telephony.LinkCapacityEstimate>> mLinkCapacityEstimateLists;
    private java.util.List<android.util.SparseArray<android.telephony.ims.MediaQualityStatus>> mMediaQualityStatus;
    private boolean[] mMessageWaiting;
    private int mNumPhones;
    private android.telephony.emergency.EmergencyNumber[] mOutgoingCallEmergencyNumber;
    private android.telephony.emergency.EmergencyNumber[] mOutgoingSmsEmergencyNumber;
    private java.util.List<java.util.List<android.telephony.PhysicalChannelConfig>> mPhysicalChannelConfigs;
    private android.telephony.PreciseCallState[] mPreciseCallState;
    private java.util.List<java.util.Map<android.util.Pair<java.lang.Integer, android.telephony.data.ApnSetting>, android.telephony.PreciseDataConnectionState>> mPreciseDataConnectionStates;
    private int[] mRingingCallState;
    private int[] mSCBMReason;
    private boolean[] mSCBMStarted;
    private android.telephony.ServiceState[] mServiceState;
    private android.telephony.SignalStrength[] mSignalStrength;
    private int[] mSrvccState;
    private android.telephony.TelephonyDisplayInfo[] mTelephonyDisplayInfos;
    private boolean[] mUserMobileDataState;
    private int[] mVoiceActivationState;
    private static final java.util.List<android.telephony.LinkCapacityEstimate> INVALID_LCE_LIST = new java.util.ArrayList(java.util.Arrays.asList(new android.telephony.LinkCapacityEstimate(2, -1, -1)));
    private static final java.util.Set<java.lang.Integer> REQUIRE_PRECISE_PHONE_STATE_PERMISSION = new java.util.HashSet();
    private com.android.server.ITelephonyRegistryExt mTelephonyRegistryExt = (com.android.server.ITelephonyRegistryExt) system.ext.loader.core.ExtLoader.type(com.android.server.ITelephonyRegistryExt.class).base(this).create();
    private final java.util.ArrayList<android.os.IBinder> mRemoveList = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.TelephonyRegistry.Record> mRecords = new java.util.ArrayList<>();
    private boolean mHasNotifySubscriptionInfoChangedOccurred = false;
    private boolean mHasNotifyOpportunisticSubscriptionInfoChangedOccurred = false;
    private int mDefaultSubId = -1;
    private int mDefaultPhoneId = -1;
    private android.telephony.PhoneCapability mPhoneCapability = null;
    private int mActiveDataSubId = -1;
    private int mRadioPowerState = 2;
    private final android.util.LocalLog mLocalLog = new android.util.LocalLog(200);
    private final android.util.LocalLog mListenLog = new android.util.LocalLog(200);
    private int[] mSimultaneousCellularCallingSubIds = new int[0];
    private final android.os.Handler mHandler = new android.os.Handler() { // from class: com.android.server.TelephonyRegistry.1
        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    int numPhones = com.android.server.TelephonyRegistry.this.getTelephonyManager().getActiveModemCount();
                    for (int phoneId = 0; phoneId < numPhones; phoneId++) {
                        int subId = android.telephony.SubscriptionManager.getSubscriptionId(phoneId);
                        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
                            subId = Integer.MAX_VALUE;
                        }
                        com.android.server.TelephonyRegistry.this.notifyCellLocationForSubscriber(subId, com.android.server.TelephonyRegistry.this.mCellIdentity[phoneId], true);
                    }
                    return;
                case 2:
                    int newDefaultPhoneId = msg.arg1;
                    int newDefaultSubId = msg.arg2;
                    synchronized (com.android.server.TelephonyRegistry.this.mRecords) {
                        for (com.android.server.TelephonyRegistry.Record r : com.android.server.TelephonyRegistry.this.mRecords) {
                            if (r.subId == Integer.MAX_VALUE) {
                                com.android.server.TelephonyRegistry.this.checkPossibleMissNotify(r, newDefaultPhoneId);
                            }
                        }
                        com.android.server.TelephonyRegistry.this.handleRemoveListLocked();
                        break;
                    }
                    com.android.server.TelephonyRegistry.this.mDefaultSubId = newDefaultSubId;
                    com.android.server.TelephonyRegistry.this.mDefaultPhoneId = newDefaultPhoneId;
                    com.android.server.TelephonyRegistry.this.mLocalLog.log("Default subscription updated: mDefaultPhoneId=" + com.android.server.TelephonyRegistry.this.mDefaultPhoneId + ", mDefaultSubId=" + com.android.server.TelephonyRegistry.this.mDefaultSubId);
                    return;
                default:
                    return;
            }
        }
    };
    private final android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.TelephonyRegistry.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if ("android.intent.action.USER_SWITCHED".equals(action)) {
                int userHandle = intent.getIntExtra("android.intent.extra.user_handle", 0);
                com.android.server.TelephonyRegistry.this.mHandler.sendMessage(com.android.server.TelephonyRegistry.this.mHandler.obtainMessage(1, userHandle, 0));
                return;
            }
            if (action.equals("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED")) {
                int newDefaultSubId = intent.getIntExtra(com.android.server.TelephonyRegistry.EXTRA_SUBSCRIPTION_INDEX, android.telephony.SubscriptionManager.getDefaultSubscriptionId());
                int newDefaultPhoneId = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", com.android.server.TelephonyRegistry.this.getPhoneIdFromSubId(newDefaultSubId));
                if (com.android.server.TelephonyRegistry.this.validatePhoneId(newDefaultPhoneId)) {
                    if (newDefaultSubId != com.android.server.TelephonyRegistry.this.mDefaultSubId || newDefaultPhoneId != com.android.server.TelephonyRegistry.this.mDefaultPhoneId) {
                        com.android.server.TelephonyRegistry.this.mHandler.sendMessage(com.android.server.TelephonyRegistry.this.mHandler.obtainMessage(2, newDefaultPhoneId, newDefaultSubId));
                        return;
                    }
                    return;
                }
                return;
            }
            if (action.equals("android.telephony.action.MULTI_SIM_CONFIG_CHANGED")) {
                com.android.server.TelephonyRegistry.this.onMultiSimConfigChanged();
            }
        }
    };
    private final com.android.internal.app.IBatteryStats mBatteryStats = com.android.server.am.BatteryStatsService.getService();

    private static class Record {
        android.os.IBinder binder;
        com.android.internal.telephony.IPhoneStateListener callback;
        int callerPid;
        int callerUid;
        java.lang.String callingFeatureId;
        java.lang.String callingPackage;
        com.android.internal.telephony.ICarrierConfigChangeListener carrierConfigChangeListener;
        com.android.internal.telephony.ICarrierPrivilegesCallback carrierPrivilegesCallback;
        android.content.Context context;
        com.android.server.TelephonyRegistry.TelephonyRegistryDeathRecipient deathRecipient;
        java.util.Set<java.lang.Integer> eventList;
        com.android.internal.telephony.IOnSubscriptionsChangedListener onOpportunisticSubscriptionsChangedListenerCallback;
        com.android.internal.telephony.IOnSubscriptionsChangedListener onSubscriptionsChangedListenerCallback;
        int phoneId;
        boolean renounceCoarseLocationAccess;
        boolean renounceFineLocationAccess;
        int subId;

        private Record() {
            this.subId = -1;
            this.phoneId = -1;
        }

        boolean matchTelephonyCallbackEvent(int event) {
            return this.callback != null && this.eventList.contains(java.lang.Integer.valueOf(event));
        }

        boolean matchOnSubscriptionsChangedListener() {
            return this.onSubscriptionsChangedListenerCallback != null;
        }

        boolean matchOnOpportunisticSubscriptionsChangedListener() {
            return this.onOpportunisticSubscriptionsChangedListenerCallback != null;
        }

        boolean matchCarrierPrivilegesCallback() {
            return this.carrierPrivilegesCallback != null;
        }

        boolean matchCarrierConfigChangeListener() {
            return this.carrierConfigChangeListener != null;
        }

        boolean canReadCallLog() {
            try {
                return com.android.internal.telephony.TelephonyPermissions.checkReadCallLog(this.context, this.subId, this.callerPid, this.callerUid, this.callingPackage, this.callingFeatureId);
            } catch (java.lang.SecurityException e) {
                return false;
            }
        }

        public java.lang.String toString() {
            return "{callingPackage=" + com.android.server.TelephonyRegistry.pii(this.callingPackage) + " callerUid=" + this.callerUid + " binder=" + this.binder + " callback=" + this.callback + " onSubscriptionsChangedListenererCallback=" + this.onSubscriptionsChangedListenerCallback + " onOpportunisticSubscriptionsChangedListenererCallback=" + this.onOpportunisticSubscriptionsChangedListenerCallback + " carrierPrivilegesCallback=" + this.carrierPrivilegesCallback + " carrierConfigChangeListener=" + this.carrierConfigChangeListener + " subId=" + this.subId + " phoneId=" + this.phoneId + " events=" + this.eventList + "}";
        }
    }

    public static class ConfigurationProvider {
        public int getRegistrationLimit() {
            return ((java.lang.Integer) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda6
                public final java.lang.Object getOrThrow() {
                    return java.lang.Integer.valueOf(android.provider.DeviceConfig.getInt("telephony", "phone_state_listener_per_pid_registration_limit", 50));
                }
            })).intValue();
        }

        public boolean isRegistrationLimitEnabledInPlatformCompat(final int uid) {
            return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda1
                public final java.lang.Object getOrThrow() {
                    return java.lang.Boolean.valueOf(android.app.compat.CompatChanges.isChangeEnabled(150880553L, uid));
                }
            })).booleanValue();
        }

        public boolean isCallStateReadPhoneStateEnforcedInPlatformCompat(final java.lang.String packageName, final android.os.UserHandle userHandle) {
            return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda3
                public final java.lang.Object getOrThrow() {
                    return java.lang.Boolean.valueOf(android.app.compat.CompatChanges.isChangeEnabled(157233955L, packageName, userHandle));
                }
            })).booleanValue();
        }

        public boolean isActiveDataSubIdReadPhoneStateEnforcedInPlatformCompat(final java.lang.String packageName, final android.os.UserHandle userHandle) {
            return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda0
                public final java.lang.Object getOrThrow() {
                    return java.lang.Boolean.valueOf(android.app.compat.CompatChanges.isChangeEnabled(com.android.server.TelephonyRegistry.REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_ACTIVE_DATA_SUB_ID, packageName, userHandle));
                }
            })).booleanValue();
        }

        public boolean isCellInfoReadPhoneStateEnforcedInPlatformCompat(final java.lang.String packageName, final android.os.UserHandle userHandle) {
            return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda4
                public final java.lang.Object getOrThrow() {
                    return java.lang.Boolean.valueOf(android.app.compat.CompatChanges.isChangeEnabled(com.android.server.TelephonyRegistry.REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_CELL_INFO, packageName, userHandle));
                }
            })).booleanValue();
        }

        public boolean isDisplayInfoReadPhoneStateEnforcedInPlatformCompat(final java.lang.String packageName, final android.os.UserHandle userHandle) {
            return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda5
                public final java.lang.Object getOrThrow() {
                    return java.lang.Boolean.valueOf(android.app.compat.CompatChanges.isChangeEnabled(com.android.server.TelephonyRegistry.REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_DISPLAY_INFO, packageName, userHandle));
                }
            })).booleanValue();
        }

        public boolean isDisplayInfoNrAdvancedSupported(final java.lang.String packageName, final android.os.UserHandle userHandle) {
            return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda2
                public final java.lang.Object getOrThrow() {
                    return java.lang.Boolean.valueOf(android.app.compat.CompatChanges.isChangeEnabled(com.android.server.TelephonyRegistry.DISPLAY_INFO_NR_ADVANCED_SUPPORTED, packageName, userHandle));
                }
            })).booleanValue();
        }
    }

    static {
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(13);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(14);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(12);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(26);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(27);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(28);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(31);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(32);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(33);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(34);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(37);
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION.add(39);
    }

    private boolean isLocationPermissionRequired(java.util.Set<java.lang.Integer> events) {
        return events.contains(5) || events.contains(11);
    }

    private boolean isPhoneStatePermissionRequired(java.util.Set<java.lang.Integer> events, java.lang.String callingPackage, android.os.UserHandle userHandle) {
        if (events.contains(4) || events.contains(3) || events.contains(25)) {
            return true;
        }
        if ((events.contains(36) || events.contains(6)) && this.mConfigurationProvider.isCallStateReadPhoneStateEnforcedInPlatformCompat(callingPackage, userHandle)) {
            return true;
        }
        if (events.contains(23) && this.mConfigurationProvider.isActiveDataSubIdReadPhoneStateEnforcedInPlatformCompat(callingPackage, userHandle)) {
            return true;
        }
        if (events.contains(11) && this.mConfigurationProvider.isCellInfoReadPhoneStateEnforcedInPlatformCompat(callingPackage, userHandle)) {
            return true;
        }
        return events.contains(21) && !this.mConfigurationProvider.isDisplayInfoReadPhoneStateEnforcedInPlatformCompat(callingPackage, userHandle);
    }

    private boolean isPrecisePhoneStatePermissionRequired(java.util.Set<java.lang.Integer> events) {
        for (java.lang.Integer requireEvent : REQUIRE_PRECISE_PHONE_STATE_PERMISSION) {
            if (events.contains(requireEvent)) {
                return true;
            }
        }
        return false;
    }

    private boolean isActiveEmergencySessionPermissionRequired(java.util.Set<java.lang.Integer> events) {
        return events.contains(29) || events.contains(30);
    }

    private boolean isPrivilegedPhoneStatePermissionRequired(java.util.Set<java.lang.Integer> events) {
        return events.contains(16) || events.contains(18) || events.contains(24) || events.contains(35) || events.contains(40) || events.contains(41);
    }

    private class TelephonyRegistryDeathRecipient implements android.os.IBinder.DeathRecipient {
        private final android.os.IBinder binder;

        TelephonyRegistryDeathRecipient(android.os.IBinder binder) {
            this.binder = binder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.TelephonyRegistry.this.remove(this.binder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.telephony.TelephonyManager getTelephonyManager() {
        return (android.telephony.TelephonyManager) this.mContext.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiSimConfigChanged() {
        synchronized (this.mRecords) {
            int oldNumPhones = this.mNumPhones;
            this.mNumPhones = getTelephonyManager().getActiveModemCount();
            if (oldNumPhones == this.mNumPhones) {
                return;
            }
            this.mCallState = java.util.Arrays.copyOf(this.mCallState, this.mNumPhones);
            this.mDataActivity = java.util.Arrays.copyOf(this.mCallState, this.mNumPhones);
            this.mDataConnectionState = java.util.Arrays.copyOf(this.mCallState, this.mNumPhones);
            this.mDataConnectionNetworkType = java.util.Arrays.copyOf(this.mCallState, this.mNumPhones);
            this.mCallIncomingNumber = (java.lang.String[]) java.util.Arrays.copyOf(this.mCallIncomingNumber, this.mNumPhones);
            this.mServiceState = (android.telephony.ServiceState[]) java.util.Arrays.copyOf(this.mServiceState, this.mNumPhones);
            this.mVoiceActivationState = java.util.Arrays.copyOf(this.mVoiceActivationState, this.mNumPhones);
            this.mDataActivationState = java.util.Arrays.copyOf(this.mDataActivationState, this.mNumPhones);
            this.mUserMobileDataState = java.util.Arrays.copyOf(this.mUserMobileDataState, this.mNumPhones);
            if (this.mSignalStrength != null) {
                this.mSignalStrength = (android.telephony.SignalStrength[]) java.util.Arrays.copyOf(this.mSignalStrength, this.mNumPhones);
            } else {
                this.mSignalStrength = new android.telephony.SignalStrength[this.mNumPhones];
            }
            this.mMessageWaiting = java.util.Arrays.copyOf(this.mMessageWaiting, this.mNumPhones);
            this.mCallForwarding = java.util.Arrays.copyOf(this.mCallForwarding, this.mNumPhones);
            this.mCellIdentity = (android.telephony.CellIdentity[]) java.util.Arrays.copyOf(this.mCellIdentity, this.mNumPhones);
            this.mSrvccState = java.util.Arrays.copyOf(this.mSrvccState, this.mNumPhones);
            this.mPreciseCallState = (android.telephony.PreciseCallState[]) java.util.Arrays.copyOf(this.mPreciseCallState, this.mNumPhones);
            this.mForegroundCallState = java.util.Arrays.copyOf(this.mForegroundCallState, this.mNumPhones);
            this.mBackgroundCallState = java.util.Arrays.copyOf(this.mBackgroundCallState, this.mNumPhones);
            this.mRingingCallState = java.util.Arrays.copyOf(this.mRingingCallState, this.mNumPhones);
            this.mCallDisconnectCause = java.util.Arrays.copyOf(this.mCallDisconnectCause, this.mNumPhones);
            this.mCallPreciseDisconnectCause = java.util.Arrays.copyOf(this.mCallPreciseDisconnectCause, this.mNumPhones);
            this.mCallQuality = (android.telephony.CallQuality[]) java.util.Arrays.copyOf(this.mCallQuality, this.mNumPhones);
            this.mCallNetworkType = java.util.Arrays.copyOf(this.mCallNetworkType, this.mNumPhones);
            this.mOutgoingCallEmergencyNumber = (android.telephony.emergency.EmergencyNumber[]) java.util.Arrays.copyOf(this.mOutgoingCallEmergencyNumber, this.mNumPhones);
            this.mOutgoingSmsEmergencyNumber = (android.telephony.emergency.EmergencyNumber[]) java.util.Arrays.copyOf(this.mOutgoingSmsEmergencyNumber, this.mNumPhones);
            this.mTelephonyDisplayInfos = (android.telephony.TelephonyDisplayInfo[]) java.util.Arrays.copyOf(this.mTelephonyDisplayInfos, this.mNumPhones);
            this.mCarrierNetworkChangeState = java.util.Arrays.copyOf(this.mCarrierNetworkChangeState, this.mNumPhones);
            this.mIsDataEnabled = java.util.Arrays.copyOf(this.mIsDataEnabled, this.mNumPhones);
            this.mDataEnabledReason = java.util.Arrays.copyOf(this.mDataEnabledReason, this.mNumPhones);
            this.mAllowedNetworkTypeReason = java.util.Arrays.copyOf(this.mAllowedNetworkTypeReason, this.mNumPhones);
            this.mAllowedNetworkTypeValue = java.util.Arrays.copyOf(this.mAllowedNetworkTypeValue, this.mNumPhones);
            this.mECBMReason = java.util.Arrays.copyOf(this.mECBMReason, this.mNumPhones);
            this.mECBMStarted = java.util.Arrays.copyOf(this.mECBMStarted, this.mNumPhones);
            this.mSCBMReason = java.util.Arrays.copyOf(this.mSCBMReason, this.mNumPhones);
            this.mSCBMStarted = java.util.Arrays.copyOf(this.mSCBMStarted, this.mNumPhones);
            this.mCarrierRoamingNtnMode = java.util.Arrays.copyOf(this.mCarrierRoamingNtnMode, this.mNumPhones);
            if (this.mNumPhones < oldNumPhones) {
                cutListToSize(this.mCellInfo, this.mNumPhones);
                cutListToSize(this.mImsReasonInfo, this.mNumPhones);
                cutListToSize(this.mPreciseDataConnectionStates, this.mNumPhones);
                cutListToSize(this.mBarringInfo, this.mNumPhones);
                cutListToSize(this.mPhysicalChannelConfigs, this.mNumPhones);
                cutListToSize(this.mLinkCapacityEstimateLists, this.mNumPhones);
                cutListToSize(this.mCarrierPrivilegeStates, this.mNumPhones);
                cutListToSize(this.mCarrierServiceStates, this.mNumPhones);
                cutListToSize(this.mCallStateLists, this.mNumPhones);
                cutListToSize(this.mMediaQualityStatus, this.mNumPhones);
                return;
            }
            for (int i = oldNumPhones; i < this.mNumPhones; i++) {
                this.mCallState[i] = 0;
                this.mDataActivity[i] = 0;
                this.mDataConnectionState[i] = -1;
                this.mVoiceActivationState[i] = 0;
                this.mDataActivationState[i] = 0;
                this.mCallIncomingNumber[i] = "";
                this.mServiceState[i] = new android.telephony.ServiceState();
                this.mSignalStrength[i] = null;
                this.mUserMobileDataState[i] = false;
                this.mMessageWaiting[i] = false;
                this.mCallForwarding[i] = false;
                this.mCellIdentity[i] = null;
                this.mCellInfo.add(i, java.util.Collections.EMPTY_LIST);
                this.mImsReasonInfo.add(i, null);
                this.mSrvccState[i] = -1;
                this.mCallDisconnectCause[i] = -1;
                this.mCallPreciseDisconnectCause[i] = -1;
                this.mCallQuality[i] = createCallQuality();
                this.mMediaQualityStatus.add(i, new android.util.SparseArray<>());
                this.mCallStateLists.add(i, new java.util.ArrayList());
                this.mCallNetworkType[i] = 0;
                this.mPreciseCallState[i] = createPreciseCallState();
                this.mRingingCallState[i] = 0;
                this.mForegroundCallState[i] = 0;
                this.mBackgroundCallState[i] = 0;
                this.mPreciseDataConnectionStates.add(new android.util.ArrayMap());
                this.mBarringInfo.add(i, new android.telephony.BarringInfo());
                this.mCarrierNetworkChangeState[i] = false;
                this.mTelephonyDisplayInfos[i] = null;
                this.mIsDataEnabled[i] = false;
                this.mDataEnabledReason[i] = 0;
                this.mPhysicalChannelConfigs.add(i, new java.util.ArrayList());
                this.mAllowedNetworkTypeReason[i] = -1;
                this.mAllowedNetworkTypeValue[i] = -1;
                this.mLinkCapacityEstimateLists.add(i, INVALID_LCE_LIST);
                this.mCarrierPrivilegeStates.add(i, new android.util.Pair<>(java.util.Collections.emptyList(), new int[0]));
                this.mCarrierServiceStates.add(i, new android.util.Pair<>(null, -1));
                this.mECBMReason[i] = 0;
                this.mECBMStarted[i] = false;
                this.mSCBMReason[i] = 0;
                this.mSCBMStarted[i] = false;
                this.mCarrierRoamingNtnMode[i] = false;
            }
        }
    }

    private void cutListToSize(java.util.List list, int size) {
        if (list == null) {
            return;
        }
        while (list.size() > size) {
            list.remove(list.size() - 1);
        }
    }

    public TelephonyRegistry(android.content.Context context, com.android.server.TelephonyRegistry.ConfigurationProvider configurationProvider) {
        this.mImsReasonInfo = null;
        this.mBarringInfo = null;
        this.mCarrierNetworkChangeState = null;
        this.mCarrierRoamingNtnMode = null;
        this.mContext = context;
        this.mConfigurationProvider = configurationProvider;
        int numPhones = getTelephonyManager().getActiveModemCount();
        this.mNumPhones = numPhones;
        this.mCallState = new int[numPhones];
        this.mDataActivity = new int[numPhones];
        this.mDataConnectionState = new int[numPhones];
        this.mDataConnectionNetworkType = new int[numPhones];
        this.mCallIncomingNumber = new java.lang.String[numPhones];
        this.mServiceState = new android.telephony.ServiceState[numPhones];
        this.mVoiceActivationState = new int[numPhones];
        this.mDataActivationState = new int[numPhones];
        this.mUserMobileDataState = new boolean[numPhones];
        this.mSignalStrength = new android.telephony.SignalStrength[numPhones];
        this.mMessageWaiting = new boolean[numPhones];
        this.mCallForwarding = new boolean[numPhones];
        this.mCellIdentity = new android.telephony.CellIdentity[numPhones];
        this.mSrvccState = new int[numPhones];
        this.mPreciseCallState = new android.telephony.PreciseCallState[numPhones];
        this.mForegroundCallState = new int[numPhones];
        this.mBackgroundCallState = new int[numPhones];
        this.mRingingCallState = new int[numPhones];
        this.mCallDisconnectCause = new int[numPhones];
        this.mCallPreciseDisconnectCause = new int[numPhones];
        this.mCallQuality = new android.telephony.CallQuality[numPhones];
        this.mMediaQualityStatus = new java.util.ArrayList();
        this.mCallNetworkType = new int[numPhones];
        this.mCallStateLists = new java.util.ArrayList<>();
        this.mPreciseDataConnectionStates = new java.util.ArrayList();
        this.mCellInfo = new java.util.ArrayList<>(numPhones);
        this.mImsReasonInfo = new java.util.ArrayList();
        this.mEmergencyNumberList = new java.util.HashMap();
        this.mOutgoingCallEmergencyNumber = new android.telephony.emergency.EmergencyNumber[numPhones];
        this.mOutgoingSmsEmergencyNumber = new android.telephony.emergency.EmergencyNumber[numPhones];
        this.mBarringInfo = new java.util.ArrayList();
        this.mCarrierNetworkChangeState = new boolean[numPhones];
        this.mTelephonyDisplayInfos = new android.telephony.TelephonyDisplayInfo[numPhones];
        this.mPhysicalChannelConfigs = new java.util.ArrayList();
        this.mAllowedNetworkTypeReason = new int[numPhones];
        this.mAllowedNetworkTypeValue = new long[numPhones];
        this.mIsDataEnabled = new boolean[numPhones];
        this.mDataEnabledReason = new int[numPhones];
        this.mLinkCapacityEstimateLists = new java.util.ArrayList();
        this.mCarrierPrivilegeStates = new java.util.ArrayList();
        this.mCarrierServiceStates = new java.util.ArrayList();
        this.mECBMReason = new int[numPhones];
        this.mECBMStarted = new boolean[numPhones];
        this.mSCBMReason = new int[numPhones];
        this.mSCBMStarted = new boolean[numPhones];
        this.mCarrierRoamingNtnMode = new boolean[numPhones];
        for (int i = 0; i < numPhones; i++) {
            this.mCallState[i] = 0;
            this.mDataActivity[i] = 0;
            this.mDataConnectionState[i] = -1;
            this.mVoiceActivationState[i] = 0;
            this.mDataActivationState[i] = 0;
            this.mCallIncomingNumber[i] = "";
            this.mServiceState[i] = new android.telephony.ServiceState();
            this.mSignalStrength[i] = null;
            this.mUserMobileDataState[i] = false;
            this.mMessageWaiting[i] = false;
            this.mCallForwarding[i] = false;
            this.mCellIdentity[i] = null;
            this.mCellInfo.add(i, java.util.Collections.EMPTY_LIST);
            this.mImsReasonInfo.add(i, new android.telephony.ims.ImsReasonInfo());
            this.mSrvccState[i] = -1;
            this.mCallDisconnectCause[i] = -1;
            this.mCallPreciseDisconnectCause[i] = -1;
            this.mCallQuality[i] = createCallQuality();
            this.mMediaQualityStatus.add(i, new android.util.SparseArray<>());
            this.mCallStateLists.add(i, new java.util.ArrayList());
            this.mCallNetworkType[i] = 0;
            this.mPreciseCallState[i] = createPreciseCallState();
            this.mRingingCallState[i] = 0;
            this.mForegroundCallState[i] = 0;
            this.mBackgroundCallState[i] = 0;
            this.mPreciseDataConnectionStates.add(new android.util.ArrayMap());
            this.mBarringInfo.add(i, new android.telephony.BarringInfo());
            this.mCarrierNetworkChangeState[i] = false;
            this.mTelephonyDisplayInfos[i] = null;
            this.mIsDataEnabled[i] = false;
            this.mDataEnabledReason[i] = 0;
            this.mPhysicalChannelConfigs.add(i, new java.util.ArrayList());
            this.mAllowedNetworkTypeReason[i] = -1;
            this.mAllowedNetworkTypeValue[i] = -1;
            this.mLinkCapacityEstimateLists.add(i, INVALID_LCE_LIST);
            this.mCarrierPrivilegeStates.add(i, new android.util.Pair<>(java.util.Collections.emptyList(), new int[0]));
            this.mCarrierServiceStates.add(i, new android.util.Pair<>(null, -1));
            this.mECBMReason[i] = 0;
            this.mECBMStarted[i] = false;
            this.mSCBMReason[i] = 0;
            this.mSCBMStarted[i] = false;
            this.mCarrierRoamingNtnMode[i] = false;
        }
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
    }

    public void systemRunning() {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_SWITCHED");
        filter.addAction("android.intent.action.USER_REMOVED");
        filter.addAction("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED");
        filter.addAction("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
        log("systemRunning register for intents");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
    }

    private boolean doesLimitApplyForListeners(int callingUid, int exemptUid) {
        return (callingUid == 1000 || callingUid == 1001 || callingUid == exemptUid) ? false : true;
    }

    public void addOnSubscriptionsChangedListener(java.lang.String callingPackage, java.lang.String callingFeatureId, com.android.internal.telephony.IOnSubscriptionsChangedListener callback) {
        android.os.UserHandle.getCallingUserId();
        this.mAppOps.checkPackage(android.os.Binder.getCallingUid(), callingPackage);
        synchronized (this.mRecords) {
            android.os.IBinder b = callback.asBinder();
            boolean doesLimitApply = doesLimitApplyForListeners(android.os.Binder.getCallingUid(), android.os.Process.myUid());
            com.android.server.TelephonyRegistry.Record r = add(b, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), doesLimitApply);
            if (r == null) {
                return;
            }
            r.context = this.mContext;
            r.onSubscriptionsChangedListenerCallback = callback;
            r.callingPackage = callingPackage;
            r.callingFeatureId = callingFeatureId;
            r.callerUid = android.os.Binder.getCallingUid();
            r.callerPid = android.os.Binder.getCallingPid();
            r.eventList = new android.util.ArraySet();
            if (this.mHasNotifySubscriptionInfoChangedOccurred) {
                try {
                    r.onSubscriptionsChangedListenerCallback.onSubscriptionsChanged();
                } catch (android.os.RemoteException e) {
                    remove(r.binder);
                }
            } else {
                log("listen oscl: mHasNotifySubscriptionInfoChangedOccurred==false no callback");
            }
        }
    }

    public void removeOnSubscriptionsChangedListener(java.lang.String pkgForDebug, com.android.internal.telephony.IOnSubscriptionsChangedListener callback) {
        remove(callback.asBinder());
    }

    public void addOnOpportunisticSubscriptionsChangedListener(java.lang.String callingPackage, java.lang.String callingFeatureId, com.android.internal.telephony.IOnSubscriptionsChangedListener callback) {
        android.os.UserHandle.getCallingUserId();
        this.mAppOps.checkPackage(android.os.Binder.getCallingUid(), callingPackage);
        synchronized (this.mRecords) {
            android.os.IBinder b = callback.asBinder();
            boolean doesLimitApply = doesLimitApplyForListeners(android.os.Binder.getCallingUid(), android.os.Process.myUid());
            com.android.server.TelephonyRegistry.Record r = add(b, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), doesLimitApply);
            if (r == null) {
                return;
            }
            r.context = this.mContext;
            r.onOpportunisticSubscriptionsChangedListenerCallback = callback;
            r.callingPackage = callingPackage;
            r.callingFeatureId = callingFeatureId;
            r.callerUid = android.os.Binder.getCallingUid();
            r.callerPid = android.os.Binder.getCallingPid();
            r.eventList = new android.util.ArraySet();
            if (this.mHasNotifyOpportunisticSubscriptionInfoChangedOccurred) {
                try {
                    r.onOpportunisticSubscriptionsChangedListenerCallback.onSubscriptionsChanged();
                } catch (android.os.RemoteException e) {
                    remove(r.binder);
                }
            } else {
                log("listen ooscl: hasNotifyOpptSubInfoChangedOccurred==false no callback");
            }
        }
    }

    public void notifySubscriptionInfoChanged() {
        if (!checkNotifyPermission("notifySubscriptionInfoChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (!this.mHasNotifySubscriptionInfoChangedOccurred) {
                log("notifySubscriptionInfoChanged: first invocation mRecords.size=" + this.mRecords.size());
            }
            this.mHasNotifySubscriptionInfoChangedOccurred = true;
            this.mRemoveList.clear();
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchOnSubscriptionsChangedListener()) {
                    try {
                        r.onSubscriptionsChangedListenerCallback.onSubscriptionsChanged();
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyOpportunisticSubscriptionInfoChanged() {
        if (!checkNotifyPermission("notifyOpportunisticSubscriptionInfoChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (!this.mHasNotifyOpportunisticSubscriptionInfoChangedOccurred) {
                log("notifyOpptSubscriptionInfoChanged: first invocation mRecords.size=" + this.mRecords.size());
            }
            this.mHasNotifyOpportunisticSubscriptionInfoChangedOccurred = true;
            this.mRemoveList.clear();
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchOnOpportunisticSubscriptionsChangedListener()) {
                    try {
                        r.onOpportunisticSubscriptionsChangedListenerCallback.onSubscriptionsChanged();
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void listenWithEventList(boolean renounceFineLocationAccess, boolean renounceCoarseLocationAccess, int subId, java.lang.String callingPackage, java.lang.String callingFeatureId, com.android.internal.telephony.IPhoneStateListener callback, int[] events, boolean notifyNow) throws java.lang.Throwable {
        java.util.Set<java.lang.Integer> eventList = (java.util.Set) java.util.Arrays.stream(events).boxed().collect(java.util.stream.Collectors.toSet());
        listen(renounceFineLocationAccess, renounceCoarseLocationAccess, callingPackage, callingFeatureId, callback, eventList, notifyNow, subId);
    }

    /* JADX WARN: Removed duplicated region for block: B:363:0x06c5  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x06ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:457:0x0660 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void listen(boolean r23, boolean r24, java.lang.String r25, java.lang.String r26, com.android.internal.telephony.IPhoneStateListener r27, java.util.Set<java.lang.Integer> r28, boolean r29, int r30) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1762
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.TelephonyRegistry.listen(boolean, boolean, java.lang.String, java.lang.String, com.android.internal.telephony.IPhoneStateListener, java.util.Set, boolean, int):void");
    }

    private java.lang.String getCallIncomingNumber(com.android.server.TelephonyRegistry.Record record, int phoneId) {
        return record.canReadCallLog() ? this.mCallIncomingNumber[phoneId] : "";
    }

    private com.android.server.TelephonyRegistry.Record add(android.os.IBinder binder, int callingUid, int callingPid, boolean doesLimitApply) {
        synchronized (this.mRecords) {
            int N = this.mRecords.size();
            int numRecordsForPid = 0;
            for (int i = 0; i < N; i++) {
                com.android.server.TelephonyRegistry.Record r = this.mRecords.get(i);
                if (binder == r.binder) {
                    return r;
                }
                if (r.callerPid == callingPid) {
                    numRecordsForPid++;
                }
            }
            int registrationLimit = this.mConfigurationProvider.getRegistrationLimit();
            if (doesLimitApply && registrationLimit >= 1 && numRecordsForPid >= registrationLimit) {
                java.lang.String errorMsg = "Pid " + callingPid + " has exceeded the number of permissible registered listeners. Ignoring request to add.";
                loge(errorMsg);
                if (this.mConfigurationProvider.isRegistrationLimitEnabledInPlatformCompat(callingUid)) {
                    throw new java.lang.IllegalStateException(errorMsg);
                }
            } else if (numRecordsForPid >= 25) {
                android.telephony.Rlog.w(TAG, "Pid " + callingPid + " has exceeded half the number of permissible registered listeners. Now at " + numRecordsForPid);
            }
            com.android.server.TelephonyRegistry.Record r2 = new com.android.server.TelephonyRegistry.Record();
            r2.binder = binder;
            r2.deathRecipient = new com.android.server.TelephonyRegistry.TelephonyRegistryDeathRecipient(binder);
            try {
                binder.linkToDeath(r2.deathRecipient, 0);
                this.mRecords.add(r2);
                this.mTelephonyRegistryExt.addProxyBinder(binder, callingUid, callingPid);
                return r2;
            } catch (android.os.RemoteException e) {
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void remove(android.os.IBinder binder) {
        synchronized (this.mRecords) {
            int recordCount = this.mRecords.size();
            for (int i = 0; i < recordCount; i++) {
                com.android.server.TelephonyRegistry.Record r = this.mRecords.get(i);
                if (r.binder == binder) {
                    if (r.deathRecipient != null) {
                        try {
                            binder.unlinkToDeath(r.deathRecipient, 0);
                        } catch (java.util.NoSuchElementException e) {
                        }
                    }
                    this.mRecords.remove(i);
                    this.mTelephonyRegistryExt.removeProxyBinder(binder, r.callerUid);
                    return;
                }
            }
        }
    }

    public void notifyCallStateForAllSubs(int state, java.lang.String phoneNumber) {
        if (!checkNotifyPermission("notifyCallState()")) {
            return;
        }
        synchronized (this.mRecords) {
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(36) && r.subId == Integer.MAX_VALUE) {
                    try {
                        java.lang.String phoneNumberOrEmpty = r.canReadCallLog() ? phoneNumber : "";
                        r.callback.onLegacyCallStateChanged(state, phoneNumberOrEmpty);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                    if (!r.matchTelephonyCallbackEvent(6)) {
                    }
                } else if (!r.matchTelephonyCallbackEvent(6) && r.subId == Integer.MAX_VALUE) {
                    try {
                        r.callback.onCallStateChanged(state);
                    } catch (android.os.RemoteException e2) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
        broadcastCallStateChanged(state, phoneNumber, -1, -1);
    }

    public void notifyCallState(int phoneId, int subId, int state, java.lang.String incomingNumber) {
        if (!checkNotifyPermission("notifyCallState()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mCallState[phoneId] = state;
                this.mCallIncomingNumber[phoneId] = incomingNumber;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(36) && r.subId == subId && r.subId != Integer.MAX_VALUE) {
                        try {
                            java.lang.String incomingNumberOrEmpty = getCallIncomingNumber(r, phoneId);
                            r.callback.onLegacyCallStateChanged(state, incomingNumberOrEmpty);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                        if (!r.matchTelephonyCallbackEvent(6)) {
                        }
                    } else if (!r.matchTelephonyCallbackEvent(6) && r.subId == subId && r.subId != Integer.MAX_VALUE) {
                        try {
                            r.callback.onCallStateChanged(state);
                        } catch (android.os.RemoteException e2) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
        broadcastCallStateChanged(state, incomingNumber, phoneId, subId);
    }

    public void notifyServiceStateForPhoneId(int phoneId, int subId, android.telephony.ServiceState state) {
        android.telephony.ServiceState stateToSend;
        if (!checkNotifyPermission("notifyServiceState()")) {
            return;
        }
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mRecords) {
                java.lang.String str = "notifyServiceStateForSubscriber: subId=" + subId + " phoneId=" + phoneId + " state=" + state;
                this.mLocalLog.log(str);
                if (validatePhoneId(phoneId)) {
                    this.mServiceState[phoneId] = state;
                    if (android.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
                        for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                            if (r.matchTelephonyCallbackEvent(1) && idMatch(r, subId, phoneId)) {
                                try {
                                    if (checkFineLocationAccess(r, 29)) {
                                        stateToSend = new android.telephony.ServiceState(state);
                                    } else if (checkCoarseLocationAccess(r, 29)) {
                                        stateToSend = state.createLocationInfoSanitizedCopy(false);
                                    } else {
                                        stateToSend = state.createLocationInfoSanitizedCopy(true);
                                    }
                                    r.callback.onServiceStateChanged(stateToSend);
                                } catch (android.os.RemoteException e) {
                                    this.mRemoveList.add(r.binder);
                                }
                            }
                        }
                    } else {
                        log("notifyServiceStateForSubscriber: INVALID subId=" + subId);
                    }
                } else {
                    log("notifyServiceStateForSubscriber: INVALID phoneId=" + phoneId);
                }
                handleRemoveListLocked();
            }
            broadcastServiceStateChanged(state, phoneId, subId);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }

    public void notifySimActivationStateChangedForPhoneId(int phoneId, int subId, int activationType, int activationState) {
        if (!checkNotifyPermission("notifySimActivationState()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                switch (activationType) {
                    case 0:
                        this.mVoiceActivationState[phoneId] = activationState;
                        break;
                    case 1:
                        this.mDataActivationState[phoneId] = activationState;
                        break;
                    default:
                        return;
                }
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (activationType == 0) {
                        try {
                            if (r.matchTelephonyCallbackEvent(18) && idMatch(r, subId, phoneId)) {
                                r.callback.onVoiceActivationStateChanged(activationState);
                            }
                            if (activationType != 1 && r.matchTelephonyCallbackEvent(19) && idMatch(r, subId, phoneId)) {
                                r.callback.onDataActivationStateChanged(activationState);
                            }
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    } else if (activationType != 1) {
                    }
                }
            } else {
                log("notifySimActivationStateForPhoneId: INVALID phoneId=" + phoneId);
            }
            handleRemoveListLocked();
        }
    }

    public void notifySignalStrengthForPhoneId(int phoneId, int subId, android.telephony.SignalStrength signalStrength) {
        if (!checkNotifyPermission("notifySignalStrength()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mSignalStrength[phoneId] = signalStrength;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(9) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onSignalStrengthsChanged(new android.telephony.SignalStrength(signalStrength));
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                        if (!r.matchTelephonyCallbackEvent(2)) {
                        }
                    } else if (!r.matchTelephonyCallbackEvent(2) && idMatch(r, subId, phoneId)) {
                        try {
                            int gsmSignalStrength = signalStrength.getGsmSignalStrength();
                            int ss = gsmSignalStrength == 99 ? -1 : gsmSignalStrength;
                            r.callback.onSignalStrengthChanged(ss);
                        } catch (android.os.RemoteException e2) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
            }
            log("notifySignalStrengthForPhoneId: invalid phoneId=" + phoneId);
            handleRemoveListLocked();
        }
        broadcastSignalStrengthChanged(signalStrength, phoneId, subId);
    }

    public void notifyCarrierNetworkChange(boolean active) {
        int[] subIds = java.util.Arrays.stream(android.telephony.SubscriptionManager.from(this.mContext).getCompleteActiveSubscriptionIdList()).filter(new java.util.function.IntPredicate() { // from class: com.android.server.TelephonyRegistry$$ExternalSyntheticLambda1
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return this.f$0.lambda$notifyCarrierNetworkChange$0(i);
            }
        }).toArray();
        if (com.android.internal.util.ArrayUtils.isEmpty(subIds)) {
            loge("notifyCarrierNetworkChange without carrier privilege");
            throw new java.lang.SecurityException("notifyCarrierNetworkChange without carrier privilege");
        }
        for (int subId : subIds) {
            notifyCarrierNetworkChangeWithPermission(subId, active);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$notifyCarrierNetworkChange$0(int i) {
        return com.android.internal.telephony.TelephonyPermissions.checkCarrierPrivilegeForSubId(this.mContext, i);
    }

    public void notifyCarrierNetworkChangeWithSubId(int subId, boolean active) {
        if (!com.android.internal.telephony.TelephonyPermissions.checkCarrierPrivilegeForSubId(this.mContext, subId)) {
            throw new java.lang.SecurityException("notifyCarrierNetworkChange without carrier privilege on subId " + subId);
        }
        notifyCarrierNetworkChangeWithPermission(subId, active);
    }

    private void notifyCarrierNetworkChangeWithPermission(int subId, boolean active) {
        int phoneId = getPhoneIdFromSubId(subId);
        synchronized (this.mRecords) {
            this.mCarrierNetworkChangeState[phoneId] = active;
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(17) && idMatch(r, subId, phoneId)) {
                    try {
                        r.callback.onCarrierNetworkChange(active);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyCellInfo(java.util.List<android.telephony.CellInfo> cellInfo) {
        notifyCellInfoForSubscriber(Integer.MAX_VALUE, cellInfo);
    }

    public void notifyCellInfoForSubscriber(int subId, java.util.List<android.telephony.CellInfo> cellInfo) {
        if (!checkNotifyPermission("notifyCellInfoForSubscriber()")) {
            return;
        }
        if (cellInfo == null) {
            loge("notifyCellInfoForSubscriber() received a null list");
            cellInfo = java.util.Collections.EMPTY_LIST;
        }
        int phoneId = getPhoneIdFromSubId(subId);
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mCellInfo.set(phoneId, cellInfo);
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (validateEventAndUserLocked(r, 11) && idMatch(r, subId, phoneId) && checkCoarseLocationAccess(r, 1) && checkFineLocationAccess(r, 29)) {
                        try {
                            r.callback.onCellInfoChanged(cellInfo);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyMessageWaitingChangedForPhoneId(int phoneId, int subId, boolean mwi) {
        if (!checkNotifyPermission("notifyMessageWaitingChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mMessageWaiting[phoneId] = mwi;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(3) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onMessageWaitingIndicatorChanged(mwi);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyUserMobileDataStateChangedForPhoneId(int phoneId, int subId, boolean state) {
        if (!checkNotifyPermission("notifyUserMobileDataStateChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mUserMobileDataState[phoneId] = state;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(20) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onUserMobileDataStateChanged(state);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyDisplayInfoChanged(int phoneId, int subId, android.telephony.TelephonyDisplayInfo telephonyDisplayInfo) {
        if (!checkNotifyPermission("notifyDisplayInfoChanged()")) {
            return;
        }
        java.lang.String str = "notifyDisplayInfoChanged: PhoneId=" + phoneId + " subId=" + subId + " telephonyDisplayInfo=" + telephonyDisplayInfo;
        this.mLocalLog.log(str);
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mTelephonyDisplayInfos[phoneId] = telephonyDisplayInfo;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(21) && idMatch(r, subId, phoneId)) {
                        try {
                            if (!this.mConfigurationProvider.isDisplayInfoNrAdvancedSupported(r.callingPackage, android.os.Binder.getCallingUserHandle())) {
                                r.callback.onDisplayInfoChanged(getBackwardCompatibleTelephonyDisplayInfo(telephonyDisplayInfo));
                            } else {
                                r.callback.onDisplayInfoChanged(telephonyDisplayInfo);
                            }
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    private android.telephony.TelephonyDisplayInfo getBackwardCompatibleTelephonyDisplayInfo(android.telephony.TelephonyDisplayInfo telephonyDisplayInfo) {
        int networkType = telephonyDisplayInfo.getNetworkType();
        int overrideNetworkType = telephonyDisplayInfo.getOverrideNetworkType();
        if (networkType == 20) {
            overrideNetworkType = 0;
        } else if (networkType == 13 && overrideNetworkType == 5) {
            overrideNetworkType = 4;
        }
        boolean isRoaming = telephonyDisplayInfo.isRoaming();
        return new android.telephony.TelephonyDisplayInfo(networkType, overrideNetworkType, isRoaming);
    }

    public void notifyCallForwardingChanged(boolean cfi) {
        notifyCallForwardingChangedForSubscriber(Integer.MAX_VALUE, cfi);
    }

    public void notifyCallForwardingChangedForSubscriber(int subId, boolean cfi) {
        if (!checkNotifyPermission("notifyCallForwardingChanged()")) {
            return;
        }
        int phoneId = getPhoneIdFromSubId(subId);
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mCallForwarding[phoneId] = cfi;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(4) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onCallForwardingIndicatorChanged(cfi);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyDataActivityForSubscriber(int subId, int state) {
        if (!checkNotifyPermission("notifyDataActivity()")) {
            return;
        }
        int phoneId = getPhoneIdFromSubId(subId);
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mDataActivity[phoneId] = state;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(8) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onDataActivity(state);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyDataActivityForSubscriberWithSlot(int phoneId, int subId, int state) {
        if (!checkNotifyPermission("notifyDataActivityWithSlot()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mDataActivity[phoneId] = state;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(8) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onDataActivity(state);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00b3 A[Catch: all -> 0x01ff, TryCatch #2 {, blocks: (B:7:0x0015, B:9:0x001b, B:11:0x0021, B:13:0x0047, B:14:0x004d, B:16:0x0053, B:18:0x0062, B:20:0x0068, B:23:0x006f, B:25:0x0077, B:26:0x00ad, B:28:0x00b3, B:29:0x00be, B:31:0x00cd, B:33:0x00df, B:34:0x00e6, B:35:0x00f6, B:37:0x00fd, B:39:0x0113, B:41:0x012b, B:43:0x0143, B:45:0x0150, B:47:0x015c, B:50:0x0172, B:52:0x0178, B:54:0x017e, B:55:0x01c7, B:57:0x01cd, B:59:0x01db, B:61:0x01e1, B:64:0x01e8, B:66:0x01f2, B:48:0x016e, B:67:0x01fd), top: B:76:0x0015, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00fd A[Catch: all -> 0x01ff, TryCatch #2 {, blocks: (B:7:0x0015, B:9:0x001b, B:11:0x0021, B:13:0x0047, B:14:0x004d, B:16:0x0053, B:18:0x0062, B:20:0x0068, B:23:0x006f, B:25:0x0077, B:26:0x00ad, B:28:0x00b3, B:29:0x00be, B:31:0x00cd, B:33:0x00df, B:34:0x00e6, B:35:0x00f6, B:37:0x00fd, B:39:0x0113, B:41:0x012b, B:43:0x0143, B:45:0x0150, B:47:0x015c, B:50:0x0172, B:52:0x0178, B:54:0x017e, B:55:0x01c7, B:57:0x01cd, B:59:0x01db, B:61:0x01e1, B:64:0x01e8, B:66:0x01f2, B:48:0x016e, B:67:0x01fd), top: B:76:0x0015, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0150 A[Catch: all -> 0x01ff, TryCatch #2 {, blocks: (B:7:0x0015, B:9:0x001b, B:11:0x0021, B:13:0x0047, B:14:0x004d, B:16:0x0053, B:18:0x0062, B:20:0x0068, B:23:0x006f, B:25:0x0077, B:26:0x00ad, B:28:0x00b3, B:29:0x00be, B:31:0x00cd, B:33:0x00df, B:34:0x00e6, B:35:0x00f6, B:37:0x00fd, B:39:0x0113, B:41:0x012b, B:43:0x0143, B:45:0x0150, B:47:0x015c, B:50:0x0172, B:52:0x0178, B:54:0x017e, B:55:0x01c7, B:57:0x01cd, B:59:0x01db, B:61:0x01e1, B:64:0x01e8, B:66:0x01f2, B:48:0x016e, B:67:0x01fd), top: B:76:0x0015, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x017e A[Catch: all -> 0x01ff, TryCatch #2 {, blocks: (B:7:0x0015, B:9:0x001b, B:11:0x0021, B:13:0x0047, B:14:0x004d, B:16:0x0053, B:18:0x0062, B:20:0x0068, B:23:0x006f, B:25:0x0077, B:26:0x00ad, B:28:0x00b3, B:29:0x00be, B:31:0x00cd, B:33:0x00df, B:34:0x00e6, B:35:0x00f6, B:37:0x00fd, B:39:0x0113, B:41:0x012b, B:43:0x0143, B:45:0x0150, B:47:0x015c, B:50:0x0172, B:52:0x0178, B:54:0x017e, B:55:0x01c7, B:57:0x01cd, B:59:0x01db, B:61:0x01e1, B:64:0x01e8, B:66:0x01f2, B:48:0x016e, B:67:0x01fd), top: B:76:0x0015, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01cd A[Catch: all -> 0x01ff, TryCatch #2 {, blocks: (B:7:0x0015, B:9:0x001b, B:11:0x0021, B:13:0x0047, B:14:0x004d, B:16:0x0053, B:18:0x0062, B:20:0x0068, B:23:0x006f, B:25:0x0077, B:26:0x00ad, B:28:0x00b3, B:29:0x00be, B:31:0x00cd, B:33:0x00df, B:34:0x00e6, B:35:0x00f6, B:37:0x00fd, B:39:0x0113, B:41:0x012b, B:43:0x0143, B:45:0x0150, B:47:0x015c, B:50:0x0172, B:52:0x0178, B:54:0x017e, B:55:0x01c7, B:57:0x01cd, B:59:0x01db, B:61:0x01e1, B:64:0x01e8, B:66:0x01f2, B:48:0x016e, B:67:0x01fd), top: B:76:0x0015, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01fd A[Catch: all -> 0x01ff, DONT_GENERATE, TryCatch #2 {, blocks: (B:7:0x0015, B:9:0x001b, B:11:0x0021, B:13:0x0047, B:14:0x004d, B:16:0x0053, B:18:0x0062, B:20:0x0068, B:23:0x006f, B:25:0x0077, B:26:0x00ad, B:28:0x00b3, B:29:0x00be, B:31:0x00cd, B:33:0x00df, B:34:0x00e6, B:35:0x00f6, B:37:0x00fd, B:39:0x0113, B:41:0x012b, B:43:0x0143, B:45:0x0150, B:47:0x015c, B:50:0x0172, B:52:0x0178, B:54:0x017e, B:55:0x01c7, B:57:0x01cd, B:59:0x01db, B:61:0x01e1, B:64:0x01e8, B:66:0x01f2, B:48:0x016e, B:67:0x01fd), top: B:76:0x0015, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0171 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void notifyDataConnectionForSubscriber(int r18, int r19, android.telephony.PreciseDataConnectionState r20) {
        /*
            Method dump skipped, instruction units count: 514
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.TelephonyRegistry.notifyDataConnectionForSubscriber(int, int, android.telephony.PreciseDataConnectionState):void");
    }

    public void notifyCellLocationForSubscriber(int subId, android.telephony.CellIdentity cellIdentity) {
        notifyCellLocationForSubscriber(subId, cellIdentity, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCellLocationForSubscriber(int subId, android.telephony.CellIdentity cellIdentity, boolean hasUserSwitched) {
        log("notifyCellLocationForSubscriber: subId=" + subId + " cellIdentity=" + android.telephony.Rlog.pii(false, cellIdentity));
        if (!checkNotifyPermission("notifyCellLocation()")) {
            return;
        }
        int phoneId = getPhoneIdFromSubId(subId);
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId) && (hasUserSwitched || !java.util.Objects.equals(cellIdentity, this.mCellIdentity[phoneId]))) {
                this.mCellIdentity[phoneId] = cellIdentity;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (validateEventAndUserLocked(r, 5) && idMatch(r, subId, phoneId) && checkCoarseLocationAccess(r, 1) && checkFineLocationAccess(r, 29)) {
                        try {
                            r.callback.onCellLocationChanged(cellIdentity);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:69:0x01dd A[Catch: all -> 0x021a, TryCatch #2 {, blocks: (B:7:0x001c, B:9:0x0022, B:11:0x0048, B:12:0x004e, B:14:0x0053, B:56:0x01a7, B:57:0x01ad, B:59:0x01b3, B:61:0x01c2, B:63:0x01c8, B:66:0x01d3, B:69:0x01dd, B:70:0x01e3, B:72:0x01e9, B:74:0x01f8, B:76:0x01fe, B:79:0x020d, B:15:0x005d, B:17:0x0067, B:18:0x0073, B:21:0x0093, B:25:0x00b4, B:26:0x00c7, B:29:0x00da, B:33:0x00fd, B:34:0x0110, B:37:0x0123, B:41:0x0146, B:42:0x0159, B:43:0x0168, B:46:0x0175, B:47:0x0182, B:49:0x0188, B:54:0x019a, B:81:0x0215, B:82:0x0218), top: B:91:0x001c, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0215 A[Catch: all -> 0x021a, TryCatch #2 {, blocks: (B:7:0x001c, B:9:0x0022, B:11:0x0048, B:12:0x004e, B:14:0x0053, B:56:0x01a7, B:57:0x01ad, B:59:0x01b3, B:61:0x01c2, B:63:0x01c8, B:66:0x01d3, B:69:0x01dd, B:70:0x01e3, B:72:0x01e9, B:74:0x01f8, B:76:0x01fe, B:79:0x020d, B:15:0x005d, B:17:0x0067, B:18:0x0073, B:21:0x0093, B:25:0x00b4, B:26:0x00c7, B:29:0x00da, B:33:0x00fd, B:34:0x0110, B:37:0x0123, B:41:0x0146, B:42:0x0159, B:43:0x0168, B:46:0x0175, B:47:0x0182, B:49:0x0188, B:54:0x019a, B:81:0x0215, B:82:0x0218), top: B:91:0x001c, inners: #0, #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void notifyPreciseCallState(int r19, int r20, int[] r21, java.lang.String[] r22, int[] r23, int[] r24) {
        /*
            Method dump skipped, instruction units count: 541
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.TelephonyRegistry.notifyPreciseCallState(int, int, int[], java.lang.String[], int[], int[]):void");
    }

    public void notifyDisconnectCause(int phoneId, int subId, int disconnectCause, int preciseDisconnectCause) {
        if (!checkNotifyPermission("notifyDisconnectCause()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mCallDisconnectCause[phoneId] = disconnectCause;
                this.mCallPreciseDisconnectCause[phoneId] = preciseDisconnectCause;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(26) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onCallDisconnectCauseChanged(this.mCallDisconnectCause[phoneId], this.mCallPreciseDisconnectCause[phoneId]);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyImsDisconnectCause(int subId, android.telephony.ims.ImsReasonInfo imsReasonInfo) {
        if (!checkNotifyPermission("notifyImsCallDisconnectCause()")) {
            return;
        }
        int phoneId = getPhoneIdFromSubId(subId);
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                if (imsReasonInfo == null) {
                    loge("ImsReasonInfo is null, subId=" + subId + ", phoneId=" + phoneId);
                    this.mImsReasonInfo.set(phoneId, new android.telephony.ims.ImsReasonInfo());
                    return;
                }
                this.mImsReasonInfo.set(phoneId, imsReasonInfo);
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(28) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onImsCallDisconnectCauseChanged(this.mImsReasonInfo.get(phoneId));
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifySrvccStateChanged(int subId, int state) {
        if (!checkNotifyPermission("notifySrvccStateChanged()")) {
            return;
        }
        int phoneId = getPhoneIdFromSubId(subId);
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mSrvccState[phoneId] = state;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(16) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onSrvccStateChanged(state);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyOemHookRawEventForSubscriber(int phoneId, int subId, byte[] rawData) {
        if (!checkNotifyPermission("notifyOemHookRawEventForSubscriber")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(15) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onOemHookRawEvent(rawData);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyPhoneCapabilityChanged(android.telephony.PhoneCapability capability) {
        if (!checkNotifyPermission("notifyPhoneCapabilityChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            this.mPhoneCapability = capability;
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(22)) {
                    try {
                        r.callback.onPhoneCapabilityChanged(capability);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyActiveDataSubIdChanged(int activeDataSubId) {
        if (!checkNotifyPermission("notifyActiveDataSubIdChanged()")) {
            return;
        }
        log("notifyActiveDataSubIdChanged: activeDataSubId=" + activeDataSubId);
        this.mLocalLog.log("notifyActiveDataSubIdChanged: activeDataSubId=" + activeDataSubId);
        this.mActiveDataSubId = activeDataSubId;
        synchronized (this.mRecords) {
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(23)) {
                    try {
                        r.callback.onActiveDataSubIdChanged(activeDataSubId);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyRadioPowerStateChanged(int phoneId, int subId, int state) {
        if (!checkNotifyPermission("notifyRadioPowerStateChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mRadioPowerState = state;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(24) && idMatchRelaxed(r, subId, phoneId)) {
                        try {
                            r.callback.onRadioPowerStateChanged(state);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
        broadcastRadioPowerStateChanged(state, phoneId, subId);
    }

    public void notifyEmergencyNumberList(int phoneId, int subId) {
        if (!checkNotifyPermission("notifyEmergencyNumberList()")) {
            return;
        }
        if (com.android.internal.telephony.flags.Flags.enforceTelephonyFeatureMappingForPublicApis() && !this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony.calling")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) this.mContext.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
                this.mEmergencyNumberList = tm.getEmergencyNumberList();
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(25) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onEmergencyNumberListChanged(this.mEmergencyNumberList);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyOutgoingEmergencyCall(int phoneId, int subId, android.telephony.emergency.EmergencyNumber emergencyNumber) {
        if (!checkNotifyPermission("notifyOutgoingEmergencyCall()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mOutgoingCallEmergencyNumber[phoneId] = emergencyNumber;
            }
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(29)) {
                    try {
                        r.callback.onOutgoingEmergencyCall(emergencyNumber, subId);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
        }
        handleRemoveListLocked();
    }

    public void notifyOutgoingEmergencySms(int phoneId, int subId, android.telephony.emergency.EmergencyNumber emergencyNumber) {
        if (!checkNotifyPermission("notifyOutgoingEmergencySms()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mOutgoingSmsEmergencyNumber[phoneId] = emergencyNumber;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(30)) {
                        try {
                            r.callback.onOutgoingEmergencySms(emergencyNumber, subId);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyCallQualityChanged(android.telephony.CallQuality callQuality, int phoneId, int subId, int callNetworkType) {
        if (!checkNotifyPermission("notifyCallQualityChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mCallQuality[phoneId] = callQuality;
                this.mCallNetworkType[phoneId] = callNetworkType;
                if (this.mCallStateLists.get(phoneId).size() > 0 && this.mCallStateLists.get(phoneId).get(0).getCallState() == 1) {
                    android.telephony.CallState prev = this.mCallStateLists.get(phoneId).remove(0);
                    this.mCallStateLists.get(phoneId).add(0, new android.telephony.CallState.Builder(prev.getCallState()).setNetworkType(callNetworkType).setCallQuality(callQuality).setCallClassification(prev.getCallClassification()).setImsCallSessionId(prev.getImsCallSessionId()).setImsCallServiceType(prev.getImsCallServiceType()).setImsCallType(prev.getImsCallType()).build());
                    for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                        if (r.matchTelephonyCallbackEvent(27) && idMatch(r, subId, phoneId)) {
                            try {
                                r.callback.onCallStatesChanged(this.mCallStateLists.get(phoneId));
                            } catch (android.os.RemoteException e) {
                                this.mRemoveList.add(r.binder);
                            }
                        }
                    }
                } else {
                    log("There is no active call to report CallQuality");
                    return;
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyRegistrationFailed(int phoneId, int subId, android.telephony.CellIdentity cellIdentity, java.lang.String chosenPlmn, int domain, int causeCode, int additionalCauseCode) throws java.lang.Throwable {
        java.util.ArrayList<com.android.server.TelephonyRegistry.Record> arrayList;
        java.util.ArrayList<com.android.server.TelephonyRegistry.Record> arrayList2;
        java.lang.String logStr;
        com.android.server.TelephonyRegistry.Record r;
        int i = phoneId;
        if (!checkNotifyPermission("notifyRegistrationFailed()")) {
            return;
        }
        android.telephony.CellIdentity noLocationCi = cellIdentity.sanitizeLocationInfo();
        java.lang.String primaryPlmn = cellIdentity != null ? cellIdentity.getPlmn() : "<UNKNOWN>";
        java.lang.String logStr2 = "Registration Failed for phoneId=" + i + " subId=" + subId + "primaryPlmn=" + primaryPlmn + " chosenPlmn=" + chosenPlmn + " domain=" + domain + " causeCode=" + causeCode + " additionalCauseCode=" + additionalCauseCode;
        this.mLocalLog.log(logStr2);
        java.util.ArrayList<com.android.server.TelephonyRegistry.Record> arrayList3 = this.mRecords;
        synchronized (arrayList3) {
            try {
                if (!validatePhoneId(phoneId)) {
                    arrayList2 = arrayList3;
                } else {
                    for (com.android.server.TelephonyRegistry.Record r2 : this.mRecords) {
                        if (!r2.matchTelephonyCallbackEvent(31)) {
                            arrayList = arrayList3;
                            logStr = logStr2;
                        } else if (idMatch(r2, subId, i)) {
                            try {
                                r = r2;
                                arrayList = arrayList3;
                                logStr = logStr2;
                                try {
                                    try {
                                        r2.callback.onRegistrationFailed(checkFineLocationAccess(r2, 1) ? cellIdentity : noLocationCi, chosenPlmn, domain, causeCode, additionalCauseCode);
                                    } catch (android.os.RemoteException e) {
                                        this.mRemoveList.add(r.binder);
                                    }
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            } catch (android.os.RemoteException e2) {
                                r = r2;
                                arrayList = arrayList3;
                                logStr = logStr2;
                            }
                        } else {
                            arrayList = arrayList3;
                            logStr = logStr2;
                        }
                        i = phoneId;
                        arrayList3 = arrayList;
                        logStr2 = logStr;
                    }
                    arrayList2 = arrayList3;
                }
                handleRemoveListLocked();
            } catch (java.lang.Throwable th2) {
                th = th2;
                arrayList = arrayList3;
            }
        }
    }

    public void notifyBarringInfoChanged(int phoneId, int subId, android.telephony.BarringInfo barringInfo) {
        if (!checkNotifyPermission("notifyBarringInfo()")) {
            return;
        }
        if (!validatePhoneId(phoneId)) {
            loge("Received invalid phoneId for BarringInfo = " + phoneId);
            return;
        }
        synchronized (this.mRecords) {
            if (barringInfo == null) {
                loge("Received null BarringInfo for subId=" + subId + ", phoneId=" + phoneId);
                this.mBarringInfo.set(phoneId, new android.telephony.BarringInfo());
                return;
            }
            if (barringInfo.equals(this.mBarringInfo.get(phoneId))) {
                return;
            }
            this.mBarringInfo.set(phoneId, barringInfo);
            android.telephony.BarringInfo biNoLocation = barringInfo.createLocationInfoSanitizedCopy();
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(32) && idMatch(r, subId, phoneId)) {
                    try {
                        r.callback.onBarringInfoChanged(checkFineLocationAccess(r, 1) ? barringInfo : biNoLocation);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyPhysicalChannelConfigForSubscriber(int phoneId, int subId, java.util.List<android.telephony.PhysicalChannelConfig> configs) {
        if (!checkNotifyPermission("notifyPhysicalChannelConfig()")) {
            return;
        }
        java.util.List<android.telephony.PhysicalChannelConfig> sanitizedConfigs = getLocationSanitizedConfigs(configs);
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mPhysicalChannelConfigs.set(phoneId, configs);
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(33) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onPhysicalChannelConfigChanged(shouldSanitizeLocationForPhysicalChannelConfig(r) ? sanitizedConfigs : configs);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    private static boolean shouldSanitizeLocationForPhysicalChannelConfig(com.android.server.TelephonyRegistry.Record record) {
        return (record.callerUid == 1001 || record.callerUid == 1000) ? false : true;
    }

    private static java.util.List<android.telephony.PhysicalChannelConfig> getLocationSanitizedConfigs(java.util.List<android.telephony.PhysicalChannelConfig> configs) {
        java.util.List<android.telephony.PhysicalChannelConfig> sanitizedConfigs = new java.util.ArrayList<>(configs.size());
        for (android.telephony.PhysicalChannelConfig config : configs) {
            sanitizedConfigs.add(config.createLocationInfoSanitizedCopy());
        }
        return sanitizedConfigs;
    }

    public void notifyDataEnabled(int phoneId, int subId, boolean enabled, int reason) {
        if (!checkNotifyPermission("notifyDataEnabled()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mIsDataEnabled[phoneId] = enabled;
                this.mDataEnabledReason[phoneId] = reason;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(34) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onDataEnabledChanged(enabled, reason);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyAllowedNetworkTypesChanged(int phoneId, int subId, int reason, long allowedNetworkType) {
        if (!checkNotifyPermission("notifyAllowedNetworkTypesChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mAllowedNetworkTypeReason[phoneId] = reason;
                this.mAllowedNetworkTypeValue[phoneId] = allowedNetworkType;
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(35) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onAllowedNetworkTypesChanged(reason, allowedNetworkType);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifyLinkCapacityEstimateChanged(int phoneId, int subId, java.util.List<android.telephony.LinkCapacityEstimate> linkCapacityEstimateList) {
        if (!checkNotifyPermission("notifyLinkCapacityEstimateChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                this.mLinkCapacityEstimateLists.set(phoneId, linkCapacityEstimateList);
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(37) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onLinkCapacityEstimateChanged(linkCapacityEstimateList);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            } else {
                handleRemoveListLocked();
            }
        }
    }

    public void notifySimultaneousCellularCallingSubscriptionsChanged(int[] subIds) {
        if (!checkNotifyPermission("notifySimultaneousCellularCallingSubscriptionsChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            this.mSimultaneousCellularCallingSubIds = subIds;
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(41)) {
                    try {
                        r.callback.onSimultaneousCallingStateChanged(subIds);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void addCarrierPrivilegesCallback(int phoneId, com.android.internal.telephony.ICarrierPrivilegesCallback callback, java.lang.String callingPackage, java.lang.String callingFeatureId) {
        android.os.UserHandle.getCallingUserId();
        this.mAppOps.checkPackage(android.os.Binder.getCallingUid(), callingPackage);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "addCarrierPrivilegesCallback");
        onMultiSimConfigChanged();
        synchronized (this.mRecords) {
            if (!validatePhoneId(phoneId)) {
                throw new java.lang.IllegalArgumentException("Invalid slot index: " + phoneId);
            }
            com.android.server.TelephonyRegistry.Record r = add(callback.asBinder(), android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), false);
            if (r == null) {
                return;
            }
            r.context = this.mContext;
            r.carrierPrivilegesCallback = callback;
            r.callingPackage = callingPackage;
            r.callingFeatureId = callingFeatureId;
            r.callerUid = android.os.Binder.getCallingUid();
            r.callerPid = android.os.Binder.getCallingPid();
            r.phoneId = phoneId;
            r.eventList = new android.util.ArraySet();
            android.util.Pair<java.util.List<java.lang.String>, int[]> state = this.mCarrierPrivilegeStates.get(phoneId);
            android.util.Pair<java.lang.String, java.lang.Integer> carrierServiceState = this.mCarrierServiceStates.get(phoneId);
            try {
                if (r.matchCarrierPrivilegesCallback()) {
                    r.carrierPrivilegesCallback.onCarrierPrivilegesChanged(java.util.Collections.unmodifiableList((java.util.List) state.first), java.util.Arrays.copyOf((int[]) state.second, ((int[]) state.second).length));
                    r.carrierPrivilegesCallback.onCarrierServiceChanged((java.lang.String) carrierServiceState.first, ((java.lang.Integer) carrierServiceState.second).intValue());
                }
            } catch (android.os.RemoteException e) {
                remove(r.binder);
            }
        }
    }

    public void removeCarrierPrivilegesCallback(com.android.internal.telephony.ICarrierPrivilegesCallback callback, java.lang.String callingPackage) {
        this.mAppOps.checkPackage(android.os.Binder.getCallingUid(), callingPackage);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "removeCarrierPrivilegesCallback");
        remove(callback.asBinder());
    }

    public void notifyCarrierPrivilegesChanged(int phoneId, java.util.List<java.lang.String> privilegedPackageNames, int[] privilegedUids) {
        if (!checkNotifyPermission("notifyCarrierPrivilegesChanged")) {
            return;
        }
        onMultiSimConfigChanged();
        synchronized (this.mRecords) {
            if (!validatePhoneId(phoneId)) {
                throw new java.lang.IllegalArgumentException("Invalid slot index: " + phoneId);
            }
            this.mCarrierPrivilegeStates.set(phoneId, new android.util.Pair<>(privilegedPackageNames, privilegedUids));
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchCarrierPrivilegesCallback() && idMatch(r, -1, phoneId)) {
                    try {
                        r.carrierPrivilegesCallback.onCarrierPrivilegesChanged(java.util.Collections.unmodifiableList(privilegedPackageNames), java.util.Arrays.copyOf(privilegedUids, privilegedUids.length));
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyCarrierServiceChanged(int phoneId, java.lang.String packageName, int uid) {
        if (checkNotifyPermission("notifyCarrierServiceChanged") && validatePhoneId(phoneId)) {
            onMultiSimConfigChanged();
            synchronized (this.mRecords) {
                this.mCarrierServiceStates.set(phoneId, new android.util.Pair<>(packageName, java.lang.Integer.valueOf(uid)));
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchCarrierPrivilegesCallback() && idMatch(r, -1, phoneId)) {
                        try {
                            r.carrierPrivilegesCallback.onCarrierServiceChanged(packageName, uid);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void addCarrierConfigChangeListener(com.android.internal.telephony.ICarrierConfigChangeListener listener, java.lang.String pkg, java.lang.String featureId) {
        android.os.UserHandle.getCallingUserId();
        this.mAppOps.checkPackage(android.os.Binder.getCallingUid(), pkg);
        synchronized (this.mRecords) {
            android.os.IBinder b = listener.asBinder();
            boolean doesLimitApply = doesLimitApplyForListeners(android.os.Binder.getCallingUid(), android.os.Process.myUid());
            com.android.server.TelephonyRegistry.Record r = add(b, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), doesLimitApply);
            if (r == null) {
                loge("Can not create Record instance!");
                return;
            }
            r.context = this.mContext;
            r.carrierConfigChangeListener = listener;
            r.callingPackage = pkg;
            r.callingFeatureId = featureId;
            r.callerUid = android.os.Binder.getCallingUid();
            r.callerPid = android.os.Binder.getCallingPid();
            r.eventList = new android.util.ArraySet();
        }
    }

    public void removeCarrierConfigChangeListener(com.android.internal.telephony.ICarrierConfigChangeListener listener, java.lang.String pkg) {
        this.mAppOps.checkPackage(android.os.Binder.getCallingUid(), pkg);
        remove(listener.asBinder());
    }

    public void notifyCarrierConfigChanged(int phoneId, int subId, int carrierId, int specificCarrierId) {
        if (!validatePhoneId(phoneId)) {
            throw new java.lang.IllegalArgumentException("Invalid phoneId: " + phoneId);
        }
        if (!checkNotifyPermission("notifyCarrierConfigChanged")) {
            loge("Caller has no notify permission!");
            return;
        }
        synchronized (this.mRecords) {
            this.mRemoveList.clear();
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchCarrierConfigChangeListener()) {
                    try {
                        r.carrierConfigChangeListener.onCarrierConfigChanged(phoneId, subId, carrierId, specificCarrierId);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyMediaQualityStatusChanged(int phoneId, int subId, android.telephony.ims.MediaQualityStatus status) {
        if (!checkNotifyPermission("notifyMediaQualityStatusChanged()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                if (this.mCallStateLists.get(phoneId).size() > 0) {
                    android.telephony.CallState callState = null;
                    java.util.Iterator<android.telephony.CallState> it = this.mCallStateLists.get(phoneId).iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        android.telephony.CallState cs = it.next();
                        if (cs.getCallState() == 1) {
                            callState = cs;
                            break;
                        }
                    }
                    if (callState != null) {
                        java.lang.String callSessionId = callState.getImsCallSessionId();
                        if (callSessionId != null && callSessionId.equals(status.getCallSessionId())) {
                            this.mMediaQualityStatus.get(phoneId).put(status.getMediaSessionType(), status);
                        } else {
                            log("SessionId mismatch active call:" + callSessionId + " media quality:" + status.getCallSessionId());
                            return;
                        }
                    } else {
                        log("There is no active call to report CallQaulity");
                        return;
                    }
                }
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    if (r.matchTelephonyCallbackEvent(39) && idMatch(r, subId, phoneId)) {
                        try {
                            r.callback.onMediaQualityStatusChanged(status);
                        } catch (android.os.RemoteException e) {
                            this.mRemoveList.add(r.binder);
                        }
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyCallbackModeStarted(int phoneId, int subId, int type) {
        if (!checkNotifyPermission("notifyCallbackModeStarted()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                if (type == 1) {
                    this.mECBMStarted[phoneId] = true;
                } else if (type == 2) {
                    this.mSCBMStarted[phoneId] = true;
                }
            }
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(40)) {
                    try {
                        r.callback.onCallBackModeStarted(type);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
        }
        handleRemoveListLocked();
    }

    public void notifyCallbackModeStopped(int phoneId, int subId, int type, int reason) {
        if (!checkNotifyPermission("notifyCallbackModeStopped()")) {
            return;
        }
        synchronized (this.mRecords) {
            if (validatePhoneId(phoneId)) {
                if (type == 1) {
                    this.mECBMStarted[phoneId] = false;
                    this.mECBMReason[phoneId] = reason;
                } else if (type == 2) {
                    this.mSCBMStarted[phoneId] = false;
                    this.mSCBMReason[phoneId] = reason;
                }
            }
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(40)) {
                    try {
                        r.callback.onCallBackModeStopped(type, reason);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
        }
        handleRemoveListLocked();
    }

    public void notifyCarrierRoamingNtnModeChanged(int subId, boolean active) {
        if (!checkNotifyPermission("notifyCarrierRoamingNtnModeChanged")) {
            return;
        }
        synchronized (this.mRecords) {
            int phoneId = getPhoneIdFromSubId(subId);
            this.mCarrierRoamingNtnMode[phoneId] = active;
            for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                if (r.matchTelephonyCallbackEvent(42) && idMatch(r, subId, phoneId)) {
                    try {
                        r.callback.onCarrierRoamingNtnModeChanged(active);
                    } catch (android.os.RemoteException e) {
                        this.mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            synchronized (this.mRecords) {
                int recordCount = this.mRecords.size();
                pw.println("last known state:");
                pw.increaseIndent();
                for (int i = 0; i < getTelephonyManager().getActiveModemCount(); i++) {
                    pw.println("Phone Id=" + i);
                    pw.increaseIndent();
                    pw.println("mCallState=" + this.mCallState[i]);
                    pw.println("mRingingCallState=" + this.mRingingCallState[i]);
                    pw.println("mForegroundCallState=" + this.mForegroundCallState[i]);
                    pw.println("mBackgroundCallState=" + this.mBackgroundCallState[i]);
                    pw.println("mPreciseCallState=" + this.mPreciseCallState[i]);
                    pw.println("mCallDisconnectCause=" + this.mCallDisconnectCause[i]);
                    pw.println("mCallIncomingNumber=" + this.mCallIncomingNumber[i]);
                    pw.println("mServiceState=" + this.mServiceState[i]);
                    pw.println("mVoiceActivationState= " + this.mVoiceActivationState[i]);
                    pw.println("mDataActivationState= " + this.mDataActivationState[i]);
                    pw.println("mUserMobileDataState= " + this.mUserMobileDataState[i]);
                    pw.println("mSignalStrength=" + this.mSignalStrength[i]);
                    pw.println("mMessageWaiting=" + this.mMessageWaiting[i]);
                    pw.println("mCallForwarding=" + this.mCallForwarding[i]);
                    pw.println("mDataActivity=" + this.mDataActivity[i]);
                    pw.println("mDataConnectionState=" + this.mDataConnectionState[i]);
                    pw.println("mCellIdentity=" + this.mCellIdentity[i]);
                    pw.println("mCellInfo=" + this.mCellInfo.get(i));
                    pw.println("mImsCallDisconnectCause=" + this.mImsReasonInfo.get(i));
                    pw.println("mSrvccState=" + this.mSrvccState[i]);
                    pw.println("mCallPreciseDisconnectCause=" + this.mCallPreciseDisconnectCause[i]);
                    pw.println("mCallQuality=" + this.mCallQuality[i]);
                    pw.println("mCallNetworkType=" + this.mCallNetworkType[i]);
                    pw.println("mPreciseDataConnectionStates=" + this.mPreciseDataConnectionStates.get(i));
                    pw.println("mOutgoingCallEmergencyNumber=" + this.mOutgoingCallEmergencyNumber[i]);
                    pw.println("mOutgoingSmsEmergencyNumber=" + this.mOutgoingSmsEmergencyNumber[i]);
                    pw.println("mBarringInfo=" + this.mBarringInfo.get(i));
                    pw.println("mCarrierNetworkChangeState=" + this.mCarrierNetworkChangeState[i]);
                    pw.println("mTelephonyDisplayInfo=" + this.mTelephonyDisplayInfos[i]);
                    pw.println("mIsDataEnabled=" + this.mIsDataEnabled[i]);
                    pw.println("mDataEnabledReason=" + this.mDataEnabledReason[i]);
                    pw.println("mAllowedNetworkTypeReason=" + this.mAllowedNetworkTypeReason[i]);
                    pw.println("mAllowedNetworkTypeValue=" + this.mAllowedNetworkTypeValue[i]);
                    pw.println("mPhysicalChannelConfigs=" + this.mPhysicalChannelConfigs.get(i));
                    pw.println("mLinkCapacityEstimateList=" + this.mLinkCapacityEstimateLists.get(i));
                    pw.println("mECBMReason=" + this.mECBMReason[i]);
                    pw.println("mECBMStarted=" + this.mECBMStarted[i]);
                    pw.println("mSCBMReason=" + this.mSCBMReason[i]);
                    pw.println("mSCBMStarted=" + this.mSCBMStarted[i]);
                    android.util.Pair<java.util.List<java.lang.String>, int[]> carrierPrivilegeState = this.mCarrierPrivilegeStates.get(i);
                    pw.println("mCarrierPrivilegeState=<packages=" + pii((java.util.List<java.lang.String>) carrierPrivilegeState.first) + ", uids=" + java.util.Arrays.toString((int[]) carrierPrivilegeState.second) + ">");
                    android.util.Pair<java.lang.String, java.lang.Integer> carrierServiceState = this.mCarrierServiceStates.get(i);
                    pw.println("mCarrierServiceState=<package=" + pii((java.lang.String) carrierServiceState.first) + ", uid=" + carrierServiceState.second + ">");
                    pw.decreaseIndent();
                }
                pw.println("mPhoneCapability=" + this.mPhoneCapability);
                pw.println("mActiveDataSubId=" + this.mActiveDataSubId);
                pw.println("mRadioPowerState=" + this.mRadioPowerState);
                pw.println("mEmergencyNumberList=" + this.mEmergencyNumberList);
                pw.println("mDefaultPhoneId=" + this.mDefaultPhoneId);
                pw.println("mDefaultSubId=" + this.mDefaultSubId);
                pw.decreaseIndent();
                pw.println("local logs:");
                pw.increaseIndent();
                this.mLocalLog.dump(fd, pw, args);
                pw.decreaseIndent();
                pw.println("listen logs:");
                pw.increaseIndent();
                this.mListenLog.dump(fd, pw, args);
                pw.decreaseIndent();
                pw.println("registrations: count=" + recordCount);
                pw.increaseIndent();
                for (com.android.server.TelephonyRegistry.Record r : this.mRecords) {
                    pw.println(r);
                }
                pw.decreaseIndent();
            }
        }
    }

    private void broadcastServiceStateChanged(android.telephony.ServiceState state, int phoneId, int subId) {
        try {
            this.mBatteryStats.notePhoneState(state.getState());
        } catch (android.os.RemoteException e) {
        }
        if (!android.telephony.LocationAccessPolicy.isLocationModeEnabled(this.mContext, this.mContext.getUserId())) {
            java.lang.String[] locationBypassPackages = (java.lang.String[]) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$$ExternalSyntheticLambda0
                public final java.lang.Object getOrThrow() {
                    return this.f$0.lambda$broadcastServiceStateChanged$1();
                }
            });
            for (java.lang.String locationBypassPackage : locationBypassPackages) {
                android.content.Intent fullIntent = createServiceStateIntent(state, subId, phoneId, false);
                fullIntent.setPackage(locationBypassPackage);
                this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0).sendBroadcastMultiplePermissions(fullIntent, new java.lang.String[]{"android.permission.READ_PHONE_STATE"}, createServiceStateBroadcastOptions(subId, phoneId, "I:R"));
                this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0).sendBroadcastMultiplePermissions(fullIntent, new java.lang.String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE"}, new java.lang.String[]{"android.permission.READ_PHONE_STATE"}, null, createServiceStateBroadcastOptions(subId, phoneId, "I:RP,E:R"));
            }
            android.content.Intent sanitizedIntent = createServiceStateIntent(state, subId, phoneId, true);
            this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0).sendBroadcastMultiplePermissions(sanitizedIntent, new java.lang.String[]{"android.permission.READ_PHONE_STATE"}, new java.lang.String[0], locationBypassPackages, createServiceStateBroadcastOptions(subId, phoneId, "I:R,lbp"));
            this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0).sendBroadcastMultiplePermissions(sanitizedIntent, new java.lang.String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE"}, new java.lang.String[]{"android.permission.READ_PHONE_STATE"}, locationBypassPackages, createServiceStateBroadcastOptions(subId, phoneId, "I:RP,E:R,lbp"));
            return;
        }
        android.content.Intent fullIntent2 = createServiceStateIntent(state, subId, phoneId, false);
        this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0).sendBroadcastMultiplePermissions(fullIntent2, new java.lang.String[]{"android.permission.READ_PHONE_STATE", "android.permission.ACCESS_FINE_LOCATION"}, createServiceStateBroadcastOptions(subId, phoneId, "I:RA"));
        this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0).sendBroadcastMultiplePermissions(fullIntent2, new java.lang.String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE", "android.permission.ACCESS_FINE_LOCATION"}, new java.lang.String[]{"android.permission.READ_PHONE_STATE"}, null, createServiceStateBroadcastOptions(subId, phoneId, "I:RPA,E:R"));
        android.content.Intent sanitizedIntent2 = createServiceStateIntent(state, subId, phoneId, true);
        this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0).sendBroadcastMultiplePermissions(sanitizedIntent2, new java.lang.String[]{"android.permission.READ_PHONE_STATE"}, new java.lang.String[]{"android.permission.ACCESS_FINE_LOCATION"}, null, createServiceStateBroadcastOptions(subId, phoneId, "I:R,E:A"));
        this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0).sendBroadcastMultiplePermissions(sanitizedIntent2, new java.lang.String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE"}, new java.lang.String[]{"android.permission.READ_PHONE_STATE", "android.permission.ACCESS_FINE_LOCATION"}, null, createServiceStateBroadcastOptions(subId, phoneId, "I:RP,E:RA"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String[] lambda$broadcastServiceStateChanged$1() throws java.lang.Exception {
        return android.telephony.LocationAccessPolicy.getLocationBypassPackages(this.mContext);
    }

    private android.content.Intent createServiceStateIntent(android.telephony.ServiceState state, int subId, int phoneId, boolean sanitizeLocation) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.SERVICE_STATE");
        intent.addFlags(16777216);
        android.os.Bundle data = new android.os.Bundle();
        if (sanitizeLocation) {
            state.createLocationInfoSanitizedCopy(true).fillInNotifierBundle(data);
        } else {
            state.fillInNotifierBundle(data);
        }
        intent.putExtras(data);
        intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, subId);
        intent.putExtra(EXTRA_SUBSCRIPTION_INDEX, subId);
        intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, phoneId);
        intent.putExtra("android.telephony.extra.SLOT_INDEX", phoneId);
        return intent;
    }

    private void broadcastRadioPowerStateChanged(int state, int phoneId, int subId) {
        android.content.Intent intent = new android.content.Intent(ACTION_RADIO_POWER_STATE_CHANGED);
        intent.addFlags(16777216);
        intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, subId);
        intent.putExtra(EXTRA_SUBSCRIPTION_INDEX, subId);
        intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, phoneId);
        intent.putExtra("android.telephony.extra.SLOT_INDEX", phoneId);
        intent.putExtra("state", state);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    private android.app.BroadcastOptions createServiceStateBroadcastOptions(int subId, int phoneId, java.lang.String tag) {
        return new android.app.BroadcastOptions().setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey("android.intent.action.SERVICE_STATE", subId + "-" + phoneId + "-" + tag).setDeferralPolicy(2);
    }

    private void broadcastSignalStrengthChanged(android.telephony.SignalStrength signalStrength, int phoneId, int subId) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mBatteryStats.notePhoneSignalStrength(signalStrength);
        } catch (android.os.RemoteException e) {
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
        android.os.Binder.restoreCallingIdentity(ident);
        android.content.Intent intent = new android.content.Intent(ACTION_SIGNAL_STRENGTH_CHANGED);
        android.os.Bundle data = new android.os.Bundle();
        fillInSignalStrengthNotifierBundle(signalStrength, data);
        intent.putExtras(data);
        intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, subId);
        intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, phoneId);
        this.mContext.sendStickyBroadcastAsUser(intent, android.os.UserHandle.ALL);
    }

    private void fillInSignalStrengthNotifierBundle(android.telephony.SignalStrength signalStrength, android.os.Bundle bundle) {
        java.util.List<android.telephony.CellSignalStrength> cellSignalStrengths = signalStrength.getCellSignalStrengths();
        for (android.telephony.CellSignalStrength cellSignalStrength : cellSignalStrengths) {
            if (cellSignalStrength instanceof android.telephony.CellSignalStrengthLte) {
                bundle.putParcelable("Lte", (android.telephony.CellSignalStrengthLte) cellSignalStrength);
            } else if (cellSignalStrength instanceof android.telephony.CellSignalStrengthCdma) {
                bundle.putParcelable("Cdma", (android.telephony.CellSignalStrengthCdma) cellSignalStrength);
            } else if (cellSignalStrength instanceof android.telephony.CellSignalStrengthGsm) {
                bundle.putParcelable("Gsm", (android.telephony.CellSignalStrengthGsm) cellSignalStrength);
            } else if (cellSignalStrength instanceof android.telephony.CellSignalStrengthWcdma) {
                bundle.putParcelable("Wcdma", (android.telephony.CellSignalStrengthWcdma) cellSignalStrength);
            } else if (cellSignalStrength instanceof android.telephony.CellSignalStrengthTdscdma) {
                bundle.putParcelable("Tdscdma", (android.telephony.CellSignalStrengthTdscdma) cellSignalStrength);
            } else if (cellSignalStrength instanceof android.telephony.CellSignalStrengthNr) {
                bundle.putParcelable("Nr", (android.telephony.CellSignalStrengthNr) cellSignalStrength);
            }
        }
    }

    private void broadcastCallStateChanged(int state, java.lang.String incomingNumber, int phoneId, int subId) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (state == 0) {
                this.mBatteryStats.notePhoneOff();
                com.android.internal.util.FrameworkStatsLog.write(95, 0);
            } else {
                this.mBatteryStats.notePhoneOn();
                com.android.internal.util.FrameworkStatsLog.write(95, 1);
            }
        } catch (android.os.RemoteException e) {
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
        android.os.Binder.restoreCallingIdentity(ident);
        android.content.Intent intent = new android.content.Intent("android.intent.action.PHONE_STATE");
        intent.putExtra("state", callStateToString(state));
        if (subId != -1) {
            intent.setAction(ACTION_SUBSCRIPTION_PHONE_STATE_CHANGED);
            intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, subId);
            intent.putExtra(EXTRA_SUBSCRIPTION_INDEX, subId);
        }
        if (phoneId != -1) {
            intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, phoneId);
            intent.putExtra("android.telephony.extra.SLOT_INDEX", phoneId);
        }
        intent.addFlags(16777216);
        android.content.Intent intentWithPhoneNumber = new android.content.Intent(intent);
        intentWithPhoneNumber.putExtra("incoming_number", incomingNumber);
        this.mContext.sendBroadcastAsUser(intentWithPhoneNumber, android.os.UserHandle.ALL, "android.permission.READ_PRIVILEGED_PHONE_STATE");
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.READ_PHONE_STATE", 51);
        this.mContext.sendBroadcastAsUserMultiplePermissions(intentWithPhoneNumber, android.os.UserHandle.ALL, new java.lang.String[]{"android.permission.READ_PHONE_STATE", "android.permission.READ_CALL_LOG"});
    }

    private static java.lang.String callStateToString(int callState) {
        switch (callState) {
            case 1:
                return android.telephony.TelephonyManager.EXTRA_STATE_RINGING;
            case 2:
                return android.telephony.TelephonyManager.EXTRA_STATE_OFFHOOK;
            default:
                return android.telephony.TelephonyManager.EXTRA_STATE_IDLE;
        }
    }

    private void broadcastDataConnectionStateChanged(int slotIndex, int subId, android.telephony.PreciseDataConnectionState pdcs) {
        android.content.Intent intent = new android.content.Intent(ACTION_ANY_DATA_CONNECTION_STATE_CHANGED);
        intent.putExtra("state", com.android.internal.telephony.util.TelephonyUtils.dataStateToString(pdcs.getState()));
        intent.putExtra(PHONE_CONSTANTS_DATA_APN_KEY, pdcs.getApnSetting().getApnName());
        intent.putExtra(PHONE_CONSTANTS_DATA_APN_TYPE_KEY, getApnTypesStringFromBitmask(pdcs.getApnSetting().getApnTypeBitmask()));
        intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, slotIndex);
        intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, subId);
        intent.putExtra(EXTRA_SUBSCRIPTION_INDEX, subId);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.READ_PHONE_STATE");
        this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0).sendBroadcastMultiplePermissions(intent, new java.lang.String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE"}, new java.lang.String[]{"android.permission.READ_PHONE_STATE"});
    }

    public static java.lang.String getApnTypesStringFromBitmask(int apnTypeBitmask) {
        java.util.List<java.lang.String> types = new java.util.ArrayList<>();
        int remainingApnTypes = apnTypeBitmask;
        if ((remainingApnTypes & 17) == 17) {
            types.add("default");
            remainingApnTypes &= -18;
        }
        while (remainingApnTypes != 0) {
            int highestApnTypeBit = java.lang.Integer.highestOneBit(remainingApnTypes);
            java.lang.String apnString = android.telephony.data.ApnSetting.getApnTypeString(highestApnTypeBit);
            if (!android.text.TextUtils.isEmpty(apnString)) {
                types.add(apnString);
            }
            remainingApnTypes &= ~highestApnTypeBit;
        }
        return android.text.TextUtils.join(",", types);
    }

    private void enforceNotifyPermissionOrCarrierPrivilege(java.lang.String method) {
        if (checkNotifyPermission()) {
            return;
        }
        com.android.internal.telephony.TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mContext, android.telephony.SubscriptionManager.getDefaultSubscriptionId(), method);
    }

    private boolean checkNotifyPermission(java.lang.String method) {
        if (checkNotifyPermission()) {
            return true;
        }
        java.lang.String str = "Modify Phone State Permission Denial: " + method + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid();
        return false;
    }

    private boolean checkNotifyPermission() {
        return this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") == 0;
    }

    private boolean checkListenerPermission(java.util.Set<java.lang.Integer> events, int subId, java.lang.String callingPackage, java.lang.String callingFeatureId, java.lang.String message) {
        boolean isPermissionCheckSuccessful = true;
        if (isLocationPermissionRequired(events)) {
            android.telephony.LocationAccessPolicy.LocationPermissionQuery.Builder locationQueryBuilder = new android.telephony.LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(callingPackage).setCallingFeatureId(callingFeatureId).setMethod(message + " events: " + events).setCallingPid(android.os.Binder.getCallingPid()).setCallingUid(android.os.Binder.getCallingUid());
            locationQueryBuilder.setMinSdkVersionForFine(29);
            locationQueryBuilder.setMinSdkVersionForCoarse(0);
            locationQueryBuilder.setMinSdkVersionForEnforcement(0);
            android.telephony.LocationAccessPolicy.LocationPermissionResult result = android.telephony.LocationAccessPolicy.checkLocationPermission(this.mContext, locationQueryBuilder.build());
            switch (com.android.server.TelephonyRegistry.AnonymousClass3.$SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[result.ordinal()]) {
                case 1:
                    throw new java.lang.SecurityException("Unable to listen for events " + events + " due to insufficient location permissions.");
                case 2:
                    isPermissionCheckSuccessful = false;
                    break;
            }
        }
        if (isPhoneStatePermissionRequired(events, callingPackage, android.os.Binder.getCallingUserHandle()) && !com.android.internal.telephony.TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, subId, callingPackage, callingFeatureId, message)) {
            isPermissionCheckSuccessful = false;
        }
        if (isPrecisePhoneStatePermissionRequired(events)) {
            try {
                this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRECISE_PHONE_STATE", null);
            } catch (java.lang.SecurityException e) {
                com.android.internal.telephony.TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mContext, subId, message);
            }
        }
        if (isActiveEmergencySessionPermissionRequired(events)) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_ACTIVE_EMERGENCY_SESSION", null);
        }
        if (isPrivilegedPhoneStatePermissionRequired(events)) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", null);
        }
        return isPermissionCheckSuccessful;
    }

    /* JADX INFO: renamed from: com.android.server.TelephonyRegistry$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult = new int[android.telephony.LocationAccessPolicy.LocationPermissionResult.values().length];

        static {
            try {
                $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[android.telephony.LocationAccessPolicy.LocationPermissionResult.DENIED_HARD.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[android.telephony.LocationAccessPolicy.LocationPermissionResult.DENIED_SOFT.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemoveListLocked() {
        int size = this.mRemoveList.size();
        if (size > 0) {
            for (android.os.IBinder b : this.mRemoveList) {
                remove(b);
            }
            this.mRemoveList.clear();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean validateEventAndUserLocked(com.android.server.TelephonyRegistry.Record r6, int r7) {
        /*
            r5 = this;
            long r0 = android.os.Binder.clearCallingIdentity()
            r2 = 0
            int r3 = android.app.ActivityManager.getCurrentUser()     // Catch: java.lang.Throwable -> L20
            int r4 = r6.callerUid     // Catch: java.lang.Throwable -> L20
            int r4 = android.os.UserHandle.getUserId(r4)     // Catch: java.lang.Throwable -> L20
            if (r4 != r3) goto L19
            boolean r4 = r6.matchTelephonyCallbackEvent(r7)     // Catch: java.lang.Throwable -> L20
            if (r4 == 0) goto L19
            r4 = 1
            goto L1a
        L19:
            r4 = 0
        L1a:
            r2 = r4
            android.os.Binder.restoreCallingIdentity(r0)
            return r2
        L20:
            r3 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.TelephonyRegistry.validateEventAndUserLocked(com.android.server.TelephonyRegistry$Record, int):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validatePhoneId(int phoneId) {
        return phoneId >= 0 && phoneId < getTelephonyManager().getActiveModemCount();
    }

    private static void log(java.lang.String s) {
        android.telephony.Rlog.d(TAG, s);
    }

    private static void loge(java.lang.String s) {
        android.telephony.Rlog.e(TAG, s);
    }

    boolean idMatch(com.android.server.TelephonyRegistry.Record r, int subId, int phoneId) {
        return subId < 0 ? r.phoneId == phoneId : r.subId == Integer.MAX_VALUE ? subId == this.mDefaultSubId : r.subId == subId;
    }

    boolean idMatchRelaxed(com.android.server.TelephonyRegistry.Record r, int subId, int phoneId) {
        if (com.android.internal.telephony.flags.Flags.useRelaxedIdMatch()) {
            return subId < 0 ? r.phoneId == -1 ? phoneId == 0 : r.phoneId == phoneId : r.subId == Integer.MAX_VALUE ? r.phoneId == -1 ? phoneId == 0 : subId == this.mDefaultSubId : r.subId == subId;
        }
        return idMatch(r, subId, phoneId);
    }

    private boolean checkFineLocationAccess(com.android.server.TelephonyRegistry.Record r) {
        return checkFineLocationAccess(r, 1);
    }

    private boolean checkCoarseLocationAccess(com.android.server.TelephonyRegistry.Record r) {
        return checkCoarseLocationAccess(r, 1);
    }

    private boolean checkFineLocationAccess(com.android.server.TelephonyRegistry.Record r, int minSdk) {
        if (r.renounceFineLocationAccess) {
            return false;
        }
        final android.telephony.LocationAccessPolicy.LocationPermissionQuery query = new android.telephony.LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(r.callingPackage).setCallingFeatureId(r.callingFeatureId).setCallingPid(r.callerPid).setCallingUid(r.callerUid).setMethod("TelephonyRegistry push").setLogAsInfo(true).setMinSdkVersionForFine(minSdk).setMinSdkVersionForCoarse(minSdk).setMinSdkVersionForEnforcement(minSdk).build();
        return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$$ExternalSyntheticLambda2
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$checkFineLocationAccess$2(query);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$checkFineLocationAccess$2(android.telephony.LocationAccessPolicy.LocationPermissionQuery query) throws java.lang.Exception {
        android.telephony.LocationAccessPolicy.LocationPermissionResult locationResult = android.telephony.LocationAccessPolicy.checkLocationPermission(this.mContext, query);
        return java.lang.Boolean.valueOf(locationResult == android.telephony.LocationAccessPolicy.LocationPermissionResult.ALLOWED);
    }

    private boolean checkCoarseLocationAccess(com.android.server.TelephonyRegistry.Record r, int minSdk) {
        if (r.renounceCoarseLocationAccess) {
            return false;
        }
        final android.telephony.LocationAccessPolicy.LocationPermissionQuery query = new android.telephony.LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(r.callingPackage).setCallingFeatureId(r.callingFeatureId).setCallingPid(r.callerPid).setCallingUid(r.callerUid).setMethod("TelephonyRegistry push").setLogAsInfo(true).setMinSdkVersionForCoarse(minSdk).setMinSdkVersionForFine(Integer.MAX_VALUE).setMinSdkVersionForEnforcement(minSdk).build();
        return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$$ExternalSyntheticLambda3
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$checkCoarseLocationAccess$3(query);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$checkCoarseLocationAccess$3(android.telephony.LocationAccessPolicy.LocationPermissionQuery query) throws java.lang.Exception {
        android.telephony.LocationAccessPolicy.LocationPermissionResult locationResult = android.telephony.LocationAccessPolicy.checkLocationPermission(this.mContext, query);
        return java.lang.Boolean.valueOf(locationResult == android.telephony.LocationAccessPolicy.LocationPermissionResult.ALLOWED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPossibleMissNotify(com.android.server.TelephonyRegistry.Record r, int phoneId) {
        java.util.Set<java.lang.Integer> events = r.eventList;
        if (events == null || events.isEmpty()) {
            log("checkPossibleMissNotify: events = null.");
            return;
        }
        if (events.contains(1)) {
            try {
                android.telephony.ServiceState ss = new android.telephony.ServiceState(this.mServiceState[phoneId]);
                if (checkFineLocationAccess(r, 29)) {
                    r.callback.onServiceStateChanged(ss);
                } else if (checkCoarseLocationAccess(r, 29)) {
                    r.callback.onServiceStateChanged(ss.createLocationInfoSanitizedCopy(false));
                } else {
                    r.callback.onServiceStateChanged(ss.createLocationInfoSanitizedCopy(true));
                }
            } catch (android.os.RemoteException e) {
                this.mRemoveList.add(r.binder);
            }
        }
        if (events.contains(9)) {
            try {
                if (this.mSignalStrength[phoneId] != null) {
                    android.telephony.SignalStrength signalStrength = this.mSignalStrength[phoneId];
                    r.callback.onSignalStrengthsChanged(new android.telephony.SignalStrength(signalStrength));
                }
            } catch (android.os.RemoteException e2) {
                this.mRemoveList.add(r.binder);
            }
        }
        if (events.contains(2)) {
            try {
                if (this.mSignalStrength[phoneId] != null) {
                    int gsmSignalStrength = this.mSignalStrength[phoneId].getGsmSignalStrength();
                    r.callback.onSignalStrengthChanged(gsmSignalStrength == 99 ? -1 : gsmSignalStrength);
                }
            } catch (android.os.RemoteException e3) {
                this.mRemoveList.add(r.binder);
            }
        }
        if (validateEventAndUserLocked(r, 11)) {
            try {
                if (checkCoarseLocationAccess(r, 1) && checkFineLocationAccess(r, 29)) {
                    r.callback.onCellInfoChanged(this.mCellInfo.get(phoneId));
                }
            } catch (android.os.RemoteException e4) {
                this.mRemoveList.add(r.binder);
            }
        }
        if (events.contains(20)) {
            try {
                r.callback.onUserMobileDataStateChanged(this.mUserMobileDataState[phoneId]);
            } catch (android.os.RemoteException e5) {
                this.mRemoveList.add(r.binder);
            }
        }
        if (events.contains(21)) {
            try {
                if (this.mTelephonyDisplayInfos[phoneId] != null) {
                    r.callback.onDisplayInfoChanged(this.mTelephonyDisplayInfos[phoneId]);
                }
            } catch (android.os.RemoteException e6) {
                this.mRemoveList.add(r.binder);
            }
        }
        if (events.contains(3)) {
            try {
                r.callback.onMessageWaitingIndicatorChanged(this.mMessageWaiting[phoneId]);
            } catch (android.os.RemoteException e7) {
                this.mRemoveList.add(r.binder);
            }
        }
        if (events.contains(4)) {
            try {
                r.callback.onCallForwardingIndicatorChanged(this.mCallForwarding[phoneId]);
            } catch (android.os.RemoteException e8) {
                this.mRemoveList.add(r.binder);
            }
        }
        if (validateEventAndUserLocked(r, 5)) {
            try {
                if (checkCoarseLocationAccess(r, 1) && checkFineLocationAccess(r, 29)) {
                    r.callback.onCellLocationChanged(this.mCellIdentity[phoneId]);
                }
            } catch (android.os.RemoteException e9) {
                this.mRemoveList.add(r.binder);
            }
        }
        if (events.contains(7)) {
            try {
                r.callback.onDataConnectionStateChanged(this.mDataConnectionState[phoneId], this.mDataConnectionNetworkType[phoneId]);
            } catch (android.os.RemoteException e10) {
                this.mRemoveList.add(r.binder);
            }
        }
    }

    private java.lang.String getNetworkTypeName(int type) {
        switch (type) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "CDMA - EvDo rev. 0";
            case 6:
                return "CDMA - EvDo rev. A";
            case 7:
                return "CDMA - 1xRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "iDEN";
            case 12:
                return "CDMA - EvDo rev. B";
            case 13:
                return "LTE";
            case 14:
                return "CDMA - eHRPD";
            case 15:
                return "HSPA+";
            case 16:
                return "GSM";
            case 17:
                return "TD_SCDMA";
            case 18:
                return "IWLAN";
            case 19:
            default:
                return "UNKNOWN";
            case 20:
                return "NR";
        }
    }

    private static android.telephony.PreciseCallState createPreciseCallState() {
        return new android.telephony.PreciseCallState(-1, -1, -1, -1, -1);
    }

    private static android.telephony.CallQuality createCallQuality() {
        return new android.telephony.CallQuality(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPhoneIdFromSubId(int subId) {
        android.telephony.SubscriptionManager subManager = (android.telephony.SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        if (subManager == null) {
            return -1;
        }
        if (subId == Integer.MAX_VALUE) {
            subId = android.telephony.SubscriptionManager.getDefaultSubscriptionId();
        }
        android.telephony.SubscriptionInfo info = subManager.getActiveSubscriptionInfo(subId);
        if (info == null) {
            return -1;
        }
        return info.getSimSlotIndex();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String pii(java.lang.String packageName) {
        return android.os.Build.IS_DEBUGGABLE ? packageName : "***";
    }

    private static java.lang.String pii(java.util.List<java.lang.String> packageNames) {
        if (packageNames.isEmpty() || android.os.Build.IS_DEBUGGABLE) {
            return packageNames.toString();
        }
        return "[***, size=" + packageNames.size() + "]";
    }
}
