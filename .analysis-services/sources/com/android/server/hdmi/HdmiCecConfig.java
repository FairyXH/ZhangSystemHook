package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiCecConfig {
    private static final java.lang.String CONFIG_FILE = "cec_config.xml";
    private static final java.lang.String ETC_DIR = "etc";
    private static final java.lang.String SHARED_PREFS_DIR = "shared_prefs";
    private static final java.lang.String SHARED_PREFS_NAME = "cec_config.xml";
    private static final int STORAGE_GLOBAL_SETTINGS = 1;
    private static final int STORAGE_SHARED_PREFS = 2;
    private static final int STORAGE_SYSPROPS = 0;
    private static final java.lang.String TAG = "HdmiCecConfig";
    private static final java.lang.String VALUE_TYPE_INT = "int";
    private static final java.lang.String VALUE_TYPE_STRING = "string";
    private final android.content.Context mContext;
    private final java.lang.Object mLock;
    private final android.util.ArrayMap<com.android.server.hdmi.HdmiCecConfig.Setting, android.util.ArrayMap<com.android.server.hdmi.HdmiCecConfig.SettingChangeListener, java.util.concurrent.Executor>> mSettingChangeListeners;
    private java.util.LinkedHashMap<java.lang.String, com.android.server.hdmi.HdmiCecConfig.Setting> mSettings;
    private final com.android.server.hdmi.HdmiCecConfig.StorageAdapter mStorageAdapter;

    public interface SettingChangeListener {
        void onChange(java.lang.String str);
    }

    private @interface Storage {
    }

    private @interface ValueType {
    }

    public static class VerificationException extends java.lang.RuntimeException {
        public VerificationException(java.lang.String message) {
            super(message);
        }
    }

    public static class StorageAdapter {
        private final android.content.Context mContext;
        private final android.content.SharedPreferences mSharedPrefs;

        StorageAdapter(android.content.Context context) {
            this.mContext = context;
            android.content.Context deviceContext = this.mContext.createDeviceProtectedStorageContext();
            java.io.File prefsFile = new java.io.File(new java.io.File(android.os.Environment.getDataSystemDirectory(), com.android.server.hdmi.HdmiCecConfig.SHARED_PREFS_DIR), "cec_config.xml");
            this.mSharedPrefs = deviceContext.getSharedPreferences(prefsFile, 0);
        }

        public java.lang.String retrieveSystemProperty(java.lang.String storageKey, java.lang.String defaultValue) {
            return android.os.SystemProperties.get(storageKey, defaultValue);
        }

        public void storeSystemProperty(java.lang.String storageKey, java.lang.String value) {
            android.os.SystemProperties.set(storageKey, value);
        }

        public java.lang.String retrieveGlobalSetting(java.lang.String storageKey, java.lang.String defaultValue) {
            java.lang.String value = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), storageKey);
            return value != null ? value : defaultValue;
        }

        public void storeGlobalSetting(java.lang.String storageKey, java.lang.String value) {
            android.provider.Settings.Global.putString(this.mContext.getContentResolver(), storageKey, value);
        }

        public java.lang.String retrieveSharedPref(java.lang.String storageKey, java.lang.String defaultValue) {
            return this.mSharedPrefs.getString(storageKey, defaultValue);
        }

        public void storeSharedPref(java.lang.String storageKey, java.lang.String value) {
            this.mSharedPrefs.edit().putString(storageKey, value).apply();
        }
    }

    private class Value {
        private final java.lang.Integer mIntValue;
        private final java.lang.String mStringValue;

        Value(java.lang.String value) {
            this.mStringValue = value;
            this.mIntValue = null;
        }

        Value(java.lang.Integer value) {
            this.mStringValue = null;
            this.mIntValue = value;
        }

        java.lang.String getStringValue() {
            return this.mStringValue;
        }

        java.lang.Integer getIntValue() {
            return this.mIntValue;
        }
    }

    protected class Setting {
        private final android.content.Context mContext;
        private final java.lang.String mName;
        private final boolean mUserConfigurable;
        private com.android.server.hdmi.HdmiCecConfig.Value mDefaultValue = null;
        private java.util.List<com.android.server.hdmi.HdmiCecConfig.Value> mAllowedValues = new java.util.ArrayList();

        Setting(android.content.Context context, java.lang.String name, int userConfResId) {
            this.mContext = context;
            this.mName = name;
            this.mUserConfigurable = this.mContext.getResources().getBoolean(userConfResId);
        }

        public java.lang.String getName() {
            return this.mName;
        }

        public java.lang.String getValueType() {
            if (getDefaultValue().getStringValue() != null) {
                return com.android.server.hdmi.HdmiCecConfig.VALUE_TYPE_STRING;
            }
            return com.android.server.hdmi.HdmiCecConfig.VALUE_TYPE_INT;
        }

        public com.android.server.hdmi.HdmiCecConfig.Value getDefaultValue() {
            if (this.mDefaultValue == null) {
                throw new com.android.server.hdmi.HdmiCecConfig.VerificationException("Invalid CEC setup for '" + getName() + "' setting. Setting has no default value.");
            }
            return this.mDefaultValue;
        }

        public boolean getUserConfigurable() {
            return this.mUserConfigurable;
        }

        private void registerValue(com.android.server.hdmi.HdmiCecConfig.Value value, int allowedResId, int defaultResId) {
            if (this.mContext.getResources().getBoolean(allowedResId)) {
                this.mAllowedValues.add(value);
                if (this.mContext.getResources().getBoolean(defaultResId)) {
                    if (this.mDefaultValue != null) {
                        android.util.Slog.e(com.android.server.hdmi.HdmiCecConfig.TAG, "Failed to set '" + value + "' as a default for '" + getName() + "': Setting already has a default ('" + this.mDefaultValue + "').");
                    } else {
                        this.mDefaultValue = value;
                    }
                }
            }
        }

        public void registerValue(java.lang.String value, int allowedResId, int defaultResId) {
            registerValue(com.android.server.hdmi.HdmiCecConfig.this.new Value(value), allowedResId, defaultResId);
        }

        public void registerValue(int value, int allowedResId, int defaultResId) {
            registerValue(com.android.server.hdmi.HdmiCecConfig.this.new Value(java.lang.Integer.valueOf(value)), allowedResId, defaultResId);
        }

        public java.util.List<com.android.server.hdmi.HdmiCecConfig.Value> getAllowedValues() {
            return this.mAllowedValues;
        }
    }

    HdmiCecConfig(android.content.Context context, com.android.server.hdmi.HdmiCecConfig.StorageAdapter storageAdapter) {
        this.mLock = new java.lang.Object();
        this.mSettingChangeListeners = new android.util.ArrayMap<>();
        this.mSettings = new java.util.LinkedHashMap<>();
        this.mContext = context;
        this.mStorageAdapter = storageAdapter;
        com.android.server.hdmi.HdmiCecConfig.Setting hdmiCecEnabled = registerSetting("hdmi_cec_enabled", android.R.bool.config_cecHdmiCecVersion14b_allowed);
        hdmiCecEnabled.registerValue(1, android.R.bool.config_cecHdmiCecControlEnabled_default, android.R.bool.config_cecHdmiCecEnabled_userConfigurable);
        hdmiCecEnabled.registerValue(0, android.R.bool.config_cecHdmiCecControlDisabled_default, android.R.bool.config_cecHdmiCecControlEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting hdmiCecVersion = registerSetting("hdmi_cec_version", android.R.bool.config_cecPowerControlModeBroadcast_allowed);
        hdmiCecVersion.registerValue(5, android.R.bool.config_cecHdmiCecVersion14b_default, android.R.bool.config_cecHdmiCecVersion20_allowed);
        hdmiCecVersion.registerValue(6, android.R.bool.config_cecHdmiCecVersion20_default, android.R.bool.config_cecHdmiCecVersion_userConfigurable);
        com.android.server.hdmi.HdmiCecConfig.Setting routingControlControl = registerSetting("routing_control", android.R.bool.config_cecSetMenuLanguageDisabled_allowed);
        routingControlControl.registerValue(1, android.R.bool.config_cecRoutingControlEnabled_default, android.R.bool.config_cecRoutingControl_userConfigurable);
        routingControlControl.registerValue(0, android.R.bool.config_cecRoutingControlDisabled_default, android.R.bool.config_cecRoutingControlEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting soundbarMode = registerSetting("soundbar_mode", android.R.bool.config_cecSystemAudioControlDisabled_allowed);
        soundbarMode.registerValue(1, android.R.bool.config_cecSoundbarModeEnabled_default, android.R.bool.config_cecSoundbarMode_userConfigurable);
        soundbarMode.registerValue(0, android.R.bool.config_cecSoundbarModeDisabled_default, android.R.bool.config_cecSoundbarModeEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting powerControlMode = registerSetting("power_control_mode", android.R.bool.config_cecPowerStateChangeOnActiveSourceLostNone_allowed);
        powerControlMode.registerValue("to_tv", android.R.bool.config_cecPowerControlModeTv_default, android.R.bool.config_cecPowerControlMode_userConfigurable);
        powerControlMode.registerValue("broadcast", android.R.bool.config_cecPowerControlModeBroadcast_default, android.R.bool.config_cecPowerControlModeNone_allowed);
        powerControlMode.registerValue("none", android.R.bool.config_cecPowerControlModeNone_default, android.R.bool.config_cecPowerControlModeTvAndAudioSystem_allowed);
        powerControlMode.registerValue("to_tv_and_audio_system", android.R.bool.config_cecPowerControlModeTvAndAudioSystem_default, android.R.bool.config_cecPowerControlModeTv_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting powerStateChangeOnActiveSourceLost = registerSetting("power_state_change_on_active_source_lost", android.R.bool.config_cecQuerySadAacDisabled_allowed);
        powerStateChangeOnActiveSourceLost.registerValue("none", android.R.bool.config_cecPowerStateChangeOnActiveSourceLostNone_default, android.R.bool.config_cecPowerStateChangeOnActiveSourceLostStandbyNow_allowed);
        powerStateChangeOnActiveSourceLost.registerValue("standby_now", android.R.bool.config_cecPowerStateChangeOnActiveSourceLostStandbyNow_default, android.R.bool.config_cecPowerStateChangeOnActiveSourceLost_userConfigurable);
        com.android.server.hdmi.HdmiCecConfig.Setting systemAudioControl = registerSetting("system_audio_control", android.R.bool.config_cecSystemAudioModeMutingDisabled_allowed);
        systemAudioControl.registerValue(1, android.R.bool.config_cecSystemAudioControlEnabled_default, android.R.bool.config_cecSystemAudioControl_userConfigurable);
        systemAudioControl.registerValue(0, android.R.bool.config_cecSystemAudioControlDisabled_default, android.R.bool.config_cecSystemAudioControlEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting systemAudioModeMuting = registerSetting("system_audio_mode_muting", android.R.bool.config_cecTvSendStandbyOnSleepDisabled_allowed);
        systemAudioModeMuting.registerValue(1, android.R.bool.config_cecSystemAudioModeMutingEnabled_default, android.R.bool.config_cecSystemAudioModeMuting_userConfigurable);
        systemAudioModeMuting.registerValue(0, android.R.bool.config_cecSystemAudioModeMutingDisabled_default, android.R.bool.config_cecSystemAudioModeMutingEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting volumeControlMode = registerSetting("volume_control_enabled", android.R.bool.config_cellBroadcastAppLinks);
        volumeControlMode.registerValue(1, android.R.bool.config_cecVolumeControlModeEnabled_default, android.R.bool.config_cecVolumeControlMode_userConfigurable);
        volumeControlMode.registerValue(0, android.R.bool.config_cecVolumeControlModeDisabled_default, android.R.bool.config_cecVolumeControlModeEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting tvWakeOnOneTouchPlay = registerSetting("tv_wake_on_one_touch_play", android.R.bool.config_cecVolumeControlModeDisabled_allowed);
        tvWakeOnOneTouchPlay.registerValue(1, android.R.bool.config_cecTvWakeOnOneTouchPlayEnabled_default, android.R.bool.config_cecTvWakeOnOneTouchPlay_userConfigurable);
        tvWakeOnOneTouchPlay.registerValue(0, android.R.bool.config_cecTvWakeOnOneTouchPlayDisabled_default, android.R.bool.config_cecTvWakeOnOneTouchPlayEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting tvSendStandbyOnSleep = registerSetting("tv_send_standby_on_sleep", android.R.bool.config_cecTvWakeOnOneTouchPlayDisabled_allowed);
        tvSendStandbyOnSleep.registerValue(1, android.R.bool.config_cecTvSendStandbyOnSleepEnabled_default, android.R.bool.config_cecTvSendStandbyOnSleep_userConfigurable);
        tvSendStandbyOnSleep.registerValue(0, android.R.bool.config_cecTvSendStandbyOnSleepDisabled_default, android.R.bool.config_cecTvSendStandbyOnSleepEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting setMenuLanguage = registerSetting("set_menu_language", android.R.bool.config_cecSoundbarModeDisabled_allowed);
        setMenuLanguage.registerValue(1, android.R.bool.config_cecSetMenuLanguageEnabled_default, android.R.bool.config_cecSetMenuLanguage_userConfigurable);
        setMenuLanguage.registerValue(0, android.R.bool.config_cecSetMenuLanguageDisabled_default, android.R.bool.config_cecSetMenuLanguageEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting rcProfileTv = registerSetting("rc_profile_tv", android.R.bool.config_cecRoutingControlDisabled_allowed);
        rcProfileTv.registerValue(0, android.R.bool.config_cecRcProfileTvNone_default, android.R.bool.config_cecRcProfileTvOne_allowed);
        rcProfileTv.registerValue(2, android.R.bool.config_cecRcProfileTvOne_default, android.R.bool.config_cecRcProfileTvThree_allowed);
        rcProfileTv.registerValue(6, android.R.bool.config_cecRcProfileTvTwo_default, android.R.bool.config_cecRcProfileTv_userConfigurable);
        rcProfileTv.registerValue(10, android.R.bool.config_cecRcProfileTvThree_default, android.R.bool.config_cecRcProfileTvTwo_allowed);
        rcProfileTv.registerValue(14, android.R.bool.config_cecRcProfileTvFour_default, android.R.bool.config_cecRcProfileTvNone_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting rcProfileSourceRootMenu = registerSetting("rc_profile_source_handles_root_menu", android.R.bool.config_cecRcProfileSourceSetupMenuHandled_allowed);
        rcProfileSourceRootMenu.registerValue(1, android.R.bool.config_cecRcProfileSourceRootMenuHandled_default, android.R.bool.config_cecRcProfileSourceRootMenuNotHandled_allowed);
        rcProfileSourceRootMenu.registerValue(0, android.R.bool.config_cecRcProfileSourceRootMenuNotHandled_default, android.R.bool.config_cecRcProfileSourceRootMenu_userConfigurable);
        com.android.server.hdmi.HdmiCecConfig.Setting rcProfileSourceSetupMenu = registerSetting("rc_profile_source_handles_setup_menu", android.R.bool.config_cecRcProfileSourceTopMenuHandled_allowed);
        rcProfileSourceSetupMenu.registerValue(1, android.R.bool.config_cecRcProfileSourceSetupMenuHandled_default, android.R.bool.config_cecRcProfileSourceSetupMenuNotHandled_allowed);
        rcProfileSourceSetupMenu.registerValue(0, android.R.bool.config_cecRcProfileSourceSetupMenuNotHandled_default, android.R.bool.config_cecRcProfileSourceSetupMenu_userConfigurable);
        com.android.server.hdmi.HdmiCecConfig.Setting rcProfileSourceContentsMenu = registerSetting("rc_profile_source_handles_contents_menu", android.R.bool.config_cecRcProfileSourceMediaContextSensitiveMenuHandled_allowed);
        rcProfileSourceContentsMenu.registerValue(1, android.R.bool.config_cecRcProfileSourceContentsMenuHandled_default, android.R.bool.config_cecRcProfileSourceContentsMenuNotHandled_allowed);
        rcProfileSourceContentsMenu.registerValue(0, android.R.bool.config_cecRcProfileSourceContentsMenuNotHandled_default, android.R.bool.config_cecRcProfileSourceContentsMenu_userConfigurable);
        com.android.server.hdmi.HdmiCecConfig.Setting rcProfileSourceTopMenu = registerSetting("rc_profile_source_handles_top_menu", android.R.bool.config_cecRcProfileTvFour_allowed);
        rcProfileSourceTopMenu.registerValue(1, android.R.bool.config_cecRcProfileSourceTopMenuHandled_default, android.R.bool.config_cecRcProfileSourceTopMenuNotHandled_allowed);
        rcProfileSourceTopMenu.registerValue(0, android.R.bool.config_cecRcProfileSourceTopMenuNotHandled_default, android.R.bool.config_cecRcProfileSourceTopMenu_userConfigurable);
        com.android.server.hdmi.HdmiCecConfig.Setting rcProfileSourceMediaContextSensitiveMenu = registerSetting("rc_profile_source_handles_media_context_sensitive_menu", android.R.bool.config_cecRcProfileSourceRootMenuHandled_allowed);
        rcProfileSourceMediaContextSensitiveMenu.registerValue(1, android.R.bool.config_cecRcProfileSourceMediaContextSensitiveMenuHandled_default, android.R.bool.config_cecRcProfileSourceMediaContextSensitiveMenuNotHandled_allowed);
        rcProfileSourceMediaContextSensitiveMenu.registerValue(0, android.R.bool.config_cecRcProfileSourceMediaContextSensitiveMenuNotHandled_default, android.R.bool.config_cecRcProfileSourceMediaContextSensitiveMenu_userConfigurable);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadLpcm = registerSetting("query_sad_lpcm", android.R.bool.config_cecQuerySadMaxDisabled_allowed);
        querySadLpcm.registerValue(1, android.R.bool.config_cecQuerySadLpcmEnabled_default, android.R.bool.config_cecQuerySadLpcm_userConfigurable);
        querySadLpcm.registerValue(0, android.R.bool.config_cecQuerySadLpcmDisabled_default, android.R.bool.config_cecQuerySadLpcmEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadDd = registerSetting("query_sad_dd", android.R.bool.config_cecQuerySadDdpDisabled_allowed);
        querySadDd.registerValue(1, android.R.bool.config_cecQuerySadDdEnabled_default, android.R.bool.config_cecQuerySadDd_userConfigurable);
        querySadDd.registerValue(0, android.R.bool.config_cecQuerySadDdDisabled_default, android.R.bool.config_cecQuerySadDdEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadMpeg1 = registerSetting("query_sad_mpeg1", android.R.bool.config_cecQuerySadMpeg2Disabled_allowed);
        querySadMpeg1.registerValue(1, android.R.bool.config_cecQuerySadMpeg1Enabled_default, android.R.bool.config_cecQuerySadMpeg1_userConfigurable);
        querySadMpeg1.registerValue(0, android.R.bool.config_cecQuerySadMpeg1Disabled_default, android.R.bool.config_cecQuerySadMpeg1Enabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadMp3 = registerSetting("query_sad_mp3", android.R.bool.config_cecQuerySadMpeg1Disabled_allowed);
        querySadMp3.registerValue(1, android.R.bool.config_cecQuerySadMp3Enabled_default, android.R.bool.config_cecQuerySadMp3_userConfigurable);
        querySadMp3.registerValue(0, android.R.bool.config_cecQuerySadMp3Disabled_default, android.R.bool.config_cecQuerySadMp3Enabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadMpeg2 = registerSetting("query_sad_mpeg2", android.R.bool.config_cecQuerySadOnebitaudioDisabled_allowed);
        querySadMpeg2.registerValue(1, android.R.bool.config_cecQuerySadMpeg2Enabled_default, android.R.bool.config_cecQuerySadMpeg2_userConfigurable);
        querySadMpeg2.registerValue(0, android.R.bool.config_cecQuerySadMpeg2Disabled_default, android.R.bool.config_cecQuerySadMpeg2Enabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadAac = registerSetting("query_sad_aac", android.R.bool.config_cecQuerySadAtracDisabled_allowed);
        querySadAac.registerValue(1, android.R.bool.config_cecQuerySadAacEnabled_default, android.R.bool.config_cecQuerySadAac_userConfigurable);
        querySadAac.registerValue(0, android.R.bool.config_cecQuerySadAacDisabled_default, android.R.bool.config_cecQuerySadAacEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadDts = registerSetting("query_sad_dts", android.R.bool.config_cecQuerySadDtshdDisabled_allowed);
        querySadDts.registerValue(1, android.R.bool.config_cecQuerySadDtsEnabled_default, android.R.bool.config_cecQuerySadDts_userConfigurable);
        querySadDts.registerValue(0, android.R.bool.config_cecQuerySadDtsDisabled_default, android.R.bool.config_cecQuerySadDtsEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadAtrac = registerSetting("query_sad_atrac", android.R.bool.config_cecQuerySadDdDisabled_allowed);
        querySadAtrac.registerValue(1, android.R.bool.config_cecQuerySadAtracEnabled_default, android.R.bool.config_cecQuerySadAtrac_userConfigurable);
        querySadAtrac.registerValue(0, android.R.bool.config_cecQuerySadAtracDisabled_default, android.R.bool.config_cecQuerySadAtracEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadOnebitaudio = registerSetting("query_sad_onebitaudio", android.R.bool.config_cecQuerySadTruehdDisabled_allowed);
        querySadOnebitaudio.registerValue(1, android.R.bool.config_cecQuerySadOnebitaudioEnabled_default, android.R.bool.config_cecQuerySadOnebitaudio_userConfigurable);
        querySadOnebitaudio.registerValue(0, android.R.bool.config_cecQuerySadOnebitaudioDisabled_default, android.R.bool.config_cecQuerySadOnebitaudioEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadDdp = registerSetting("query_sad_ddp", android.R.bool.config_cecQuerySadDstDisabled_allowed);
        querySadDdp.registerValue(1, android.R.bool.config_cecQuerySadDdpEnabled_default, android.R.bool.config_cecQuerySadDdp_userConfigurable);
        querySadDdp.registerValue(0, android.R.bool.config_cecQuerySadDdpDisabled_default, android.R.bool.config_cecQuerySadDdpEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadDtshd = registerSetting("query_sad_dtshd", android.R.bool.config_cecQuerySadLpcmDisabled_allowed);
        querySadDtshd.registerValue(1, android.R.bool.config_cecQuerySadDtshdEnabled_default, android.R.bool.config_cecQuerySadDtshd_userConfigurable);
        querySadDtshd.registerValue(0, android.R.bool.config_cecQuerySadDtshdDisabled_default, android.R.bool.config_cecQuerySadDtshdEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadTruehd = registerSetting("query_sad_truehd", android.R.bool.config_cecQuerySadWmaproDisabled_allowed);
        querySadTruehd.registerValue(1, android.R.bool.config_cecQuerySadTruehdEnabled_default, android.R.bool.config_cecQuerySadTruehd_userConfigurable);
        querySadTruehd.registerValue(0, android.R.bool.config_cecQuerySadTruehdDisabled_default, android.R.bool.config_cecQuerySadTruehdEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadDst = registerSetting("query_sad_dst", android.R.bool.config_cecQuerySadDtsDisabled_allowed);
        querySadDst.registerValue(1, android.R.bool.config_cecQuerySadDstEnabled_default, android.R.bool.config_cecQuerySadDst_userConfigurable);
        querySadDst.registerValue(0, android.R.bool.config_cecQuerySadDstDisabled_default, android.R.bool.config_cecQuerySadDstEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadWmapro = registerSetting("query_sad_wmapro", android.R.bool.config_cecRcProfileSourceContentsMenuHandled_allowed);
        querySadWmapro.registerValue(1, android.R.bool.config_cecQuerySadWmaproEnabled_default, android.R.bool.config_cecQuerySadWmapro_userConfigurable);
        querySadWmapro.registerValue(0, android.R.bool.config_cecQuerySadWmaproDisabled_default, android.R.bool.config_cecQuerySadWmaproEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting querySadMax = registerSetting("query_sad_max", android.R.bool.config_cecQuerySadMp3Disabled_allowed);
        querySadMax.registerValue(1, android.R.bool.config_cecQuerySadMaxEnabled_default, android.R.bool.config_cecQuerySadMax_userConfigurable);
        querySadMax.registerValue(0, android.R.bool.config_cecQuerySadMaxDisabled_default, android.R.bool.config_cecQuerySadMaxEnabled_allowed);
        com.android.server.hdmi.HdmiCecConfig.Setting earcEnabled = registerSetting("earc_enabled", android.R.bool.config_dropboxmanager_persistent_logging_enabled);
        earcEnabled.registerValue(1, android.R.bool.config_earcEnabled_userConfigurable, android.R.bool.config_earcFeatureDisabled_allowed);
        earcEnabled.registerValue(0, android.R.bool.config_duplicate_port_omadm_wappush, android.R.bool.config_eap_sim_based_auth_supported);
        verifySettings();
    }

    HdmiCecConfig(android.content.Context context) {
        this(context, new com.android.server.hdmi.HdmiCecConfig.StorageAdapter(context));
    }

    private com.android.server.hdmi.HdmiCecConfig.Setting registerSetting(java.lang.String name, int userConfResId) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = new com.android.server.hdmi.HdmiCecConfig.Setting(this.mContext, name, userConfResId);
        this.mSettings.put(name, setting);
        return setting;
    }

    private void verifySettings() {
        for (com.android.server.hdmi.HdmiCecConfig.Setting setting : this.mSettings.values()) {
            setting.getDefaultValue();
            getStorage(setting);
            getStorageKey(setting);
        }
    }

    private com.android.server.hdmi.HdmiCecConfig.Setting getSetting(java.lang.String name) {
        if (this.mSettings.containsKey(name)) {
            return this.mSettings.get(name);
        }
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:107:0x01b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int getStorage(com.android.server.hdmi.HdmiCecConfig.Setting r4) {
        /*
            Method dump skipped, instruction units count: 722
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiCecConfig.getStorage(com.android.server.hdmi.HdmiCecConfig$Setting):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:107:0x01b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String getStorageKey(com.android.server.hdmi.HdmiCecConfig.Setting r4) {
        /*
            Method dump skipped, instruction units count: 856
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiCecConfig.getStorageKey(com.android.server.hdmi.HdmiCecConfig$Setting):java.lang.String");
    }

    protected java.lang.String retrieveValue(com.android.server.hdmi.HdmiCecConfig.Setting setting, java.lang.String defaultValue) {
        int storage = getStorage(setting);
        java.lang.String storageKey = getStorageKey(setting);
        if (storage == 0) {
            com.android.server.hdmi.HdmiLogger.debug("Reading '" + storageKey + "' sysprop.", new java.lang.Object[0]);
            return this.mStorageAdapter.retrieveSystemProperty(storageKey, defaultValue);
        }
        if (storage == 1) {
            com.android.server.hdmi.HdmiLogger.debug("Reading '" + storageKey + "' global setting.", new java.lang.Object[0]);
            return this.mStorageAdapter.retrieveGlobalSetting(storageKey, defaultValue);
        }
        if (storage == 2) {
            com.android.server.hdmi.HdmiLogger.debug("Reading '" + storageKey + "' shared preference.", new java.lang.Object[0]);
            return this.mStorageAdapter.retrieveSharedPref(storageKey, defaultValue);
        }
        return null;
    }

    protected void storeValue(com.android.server.hdmi.HdmiCecConfig.Setting setting, java.lang.String value) {
        int storage = getStorage(setting);
        java.lang.String storageKey = getStorageKey(setting);
        if (storage == 0) {
            com.android.server.hdmi.HdmiLogger.debug("Setting '" + storageKey + "' sysprop.", new java.lang.Object[0]);
            this.mStorageAdapter.storeSystemProperty(storageKey, value);
        } else if (storage == 1) {
            com.android.server.hdmi.HdmiLogger.debug("Setting '" + storageKey + "' global setting.", new java.lang.Object[0]);
            this.mStorageAdapter.storeGlobalSetting(storageKey, value);
        } else if (storage == 2) {
            com.android.server.hdmi.HdmiLogger.debug("Setting '" + storageKey + "' shared pref.", new java.lang.Object[0]);
            this.mStorageAdapter.storeSharedPref(storageKey, value);
            notifySettingChanged(setting);
        }
    }

    protected void notifySettingChanged(final com.android.server.hdmi.HdmiCecConfig.Setting setting) {
        synchronized (this.mLock) {
            android.util.ArrayMap<com.android.server.hdmi.HdmiCecConfig.SettingChangeListener, java.util.concurrent.Executor> listeners = this.mSettingChangeListeners.get(setting);
            if (listeners == null) {
                return;
            }
            for (java.util.Map.Entry<com.android.server.hdmi.HdmiCecConfig.SettingChangeListener, java.util.concurrent.Executor> entry : listeners.entrySet()) {
                final com.android.server.hdmi.HdmiCecConfig.SettingChangeListener listener = entry.getKey();
                java.util.concurrent.Executor executor = entry.getValue();
                executor.execute(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecConfig.1
                    @Override // java.lang.Runnable
                    public void run() {
                        listener.onChange(setting.getName());
                    }
                });
            }
        }
    }

    public void registerChangeListener(java.lang.String name, com.android.server.hdmi.HdmiCecConfig.SettingChangeListener listener) {
        registerChangeListener(name, listener, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR);
    }

    public void registerChangeListener(java.lang.String name, com.android.server.hdmi.HdmiCecConfig.SettingChangeListener listener, java.util.concurrent.Executor executor) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        int storage = getStorage(setting);
        if (storage != 1 && storage != 2) {
            throw new java.lang.IllegalArgumentException("Change listeners for setting '" + name + "' not supported.");
        }
        synchronized (this.mLock) {
            if (!this.mSettingChangeListeners.containsKey(setting)) {
                this.mSettingChangeListeners.put(setting, new android.util.ArrayMap<>());
            }
            this.mSettingChangeListeners.get(setting).put(listener, executor);
        }
    }

    public void removeChangeListener(java.lang.String name, com.android.server.hdmi.HdmiCecConfig.SettingChangeListener listener) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        synchronized (this.mLock) {
            if (this.mSettingChangeListeners.containsKey(setting)) {
                android.util.ArrayMap<com.android.server.hdmi.HdmiCecConfig.SettingChangeListener, java.util.concurrent.Executor> listeners = this.mSettingChangeListeners.get(setting);
                listeners.remove(listener);
                if (listeners.isEmpty()) {
                    this.mSettingChangeListeners.remove(setting);
                }
            }
        }
    }

    public java.util.List<java.lang.String> getAllSettings() {
        return new java.util.ArrayList(this.mSettings.keySet());
    }

    public java.util.List<java.lang.String> getUserSettings() {
        java.util.List<java.lang.String> settings = new java.util.ArrayList<>();
        for (com.android.server.hdmi.HdmiCecConfig.Setting setting : this.mSettings.values()) {
            if (setting.getUserConfigurable()) {
                settings.add(setting.getName());
            }
        }
        return settings;
    }

    public boolean isStringValueType(java.lang.String name) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        return getSetting(name).getValueType().equals(VALUE_TYPE_STRING);
    }

    public boolean isIntValueType(java.lang.String name) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        return getSetting(name).getValueType().equals(VALUE_TYPE_INT);
    }

    public java.util.List<java.lang.String> getAllowedStringValues(java.lang.String name) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_STRING)) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' is not a string-type setting.");
        }
        java.util.List<java.lang.String> allowedValues = new java.util.ArrayList<>();
        for (com.android.server.hdmi.HdmiCecConfig.Value allowedValue : setting.getAllowedValues()) {
            allowedValues.add(allowedValue.getStringValue());
        }
        return allowedValues;
    }

    public java.util.List<java.lang.Integer> getAllowedIntValues(java.lang.String name) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_INT)) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' is not a string-type setting.");
        }
        java.util.List<java.lang.Integer> allowedValues = new java.util.ArrayList<>();
        for (com.android.server.hdmi.HdmiCecConfig.Value allowedValue : setting.getAllowedValues()) {
            allowedValues.add(allowedValue.getIntValue());
        }
        return allowedValues;
    }

    public java.lang.String getDefaultStringValue(java.lang.String name) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_STRING)) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' is not a string-type setting.");
        }
        return getSetting(name).getDefaultValue().getStringValue();
    }

    public int getDefaultIntValue(java.lang.String name) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_INT)) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' is not a string-type setting.");
        }
        return getSetting(name).getDefaultValue().getIntValue().intValue();
    }

    public java.lang.String getStringValue(java.lang.String name) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_STRING)) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' is not a string-type setting.");
        }
        com.android.server.hdmi.HdmiLogger.debug("Getting CEC setting value '" + name + "'.", new java.lang.Object[0]);
        return retrieveValue(setting, setting.getDefaultValue().getStringValue());
    }

    public int getIntValue(java.lang.String name) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_INT)) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' is not a int-type setting.");
        }
        com.android.server.hdmi.HdmiLogger.debug("Getting CEC setting value '" + name + "'.", new java.lang.Object[0]);
        java.lang.String defaultValue = java.lang.Integer.toString(setting.getDefaultValue().getIntValue().intValue());
        java.lang.String value = retrieveValue(setting, defaultValue);
        return java.lang.Integer.parseInt(value);
    }

    public void setStringValue(java.lang.String name, java.lang.String value) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        if (!setting.getUserConfigurable()) {
            throw new java.lang.IllegalArgumentException("Updating CEC setting '" + name + "' prohibited.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_STRING)) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' is not a string-type setting.");
        }
        if (!getAllowedStringValues(name).contains(value)) {
            throw new java.lang.IllegalArgumentException("Invalid CEC setting '" + name + "' value: '" + value + "'.");
        }
        com.android.server.hdmi.HdmiLogger.debug("Updating CEC setting '" + name + "' to '" + value + "'.", new java.lang.Object[0]);
        storeValue(setting, value);
    }

    public void setIntValue(java.lang.String name, int value) {
        com.android.server.hdmi.HdmiCecConfig.Setting setting = getSetting(name);
        if (setting == null) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' does not exist.");
        }
        if (!setting.getUserConfigurable()) {
            throw new java.lang.IllegalArgumentException("Updating CEC setting '" + name + "' prohibited.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_INT)) {
            throw new java.lang.IllegalArgumentException("Setting '" + name + "' is not a int-type setting.");
        }
        if (!getAllowedIntValues(name).contains(java.lang.Integer.valueOf(value))) {
            throw new java.lang.IllegalArgumentException("Invalid CEC setting '" + name + "' value: '" + value + "'.");
        }
        com.android.server.hdmi.HdmiLogger.debug("Updating CEC setting '" + name + "' to '" + value + "'.", new java.lang.Object[0]);
        storeValue(setting, java.lang.Integer.toString(value));
    }
}
