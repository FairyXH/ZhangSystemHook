package com.android.media.audio;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    private static com.android.media.audio.FeatureFlags FEATURE_FLAGS = new com.android.media.audio.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ABS_VOLUME_INDEX_FIX = "com.android.media.audio.abs_volume_index_fix";
    public static final java.lang.String FLAG_ALARM_MIN_VOLUME_ZERO = "com.android.media.audio.alarm_min_volume_zero";
    public static final java.lang.String FLAG_AS_DEVICE_CONNECTION_FAILURE = "com.android.media.audio.as_device_connection_failure";
    public static final java.lang.String FLAG_AUDIOSERVER_PERMISSIONS = "com.android.media.audio.audioserver_permissions";
    public static final java.lang.String FLAG_BLUETOOTH_MAC_ADDRESS_ANONYMIZATION = "com.android.media.audio.bluetooth_mac_address_anonymization";
    public static final java.lang.String FLAG_DISABLE_PRESCALE_ABSOLUTE_VOLUME = "com.android.media.audio.disable_prescale_absolute_volume";
    public static final java.lang.String FLAG_DSA_OVER_BT_LE_AUDIO = "com.android.media.audio.dsa_over_bt_le_audio";
    public static final java.lang.String FLAG_MUSIC_FX_EDGE_TO_EDGE = "com.android.media.audio.music_fx_edge_to_edge";
    public static final java.lang.String FLAG_PORT_TO_PIID_SIMPLIFICATION = "com.android.media.audio.port_to_piid_simplification";
    public static final java.lang.String FLAG_REPLACE_STREAM_BT_SCO = "com.android.media.audio.replace_stream_bt_sco";
    public static final java.lang.String FLAG_RINGER_MODE_AFFECTS_ALARM = "com.android.media.audio.ringer_mode_affects_alarm";
    public static final java.lang.String FLAG_SET_STREAM_VOLUME_ORDER = "com.android.media.audio.set_stream_volume_order";
    public static final java.lang.String FLAG_SPATIALIZER_OFFLOAD = "com.android.media.audio.spatializer_offload";
    public static final java.lang.String FLAG_SPATIALIZER_UPMIX = "com.android.media.audio.spatializer_upmix";
    public static final java.lang.String FLAG_STEREO_SPATIALIZATION = "com.android.media.audio.stereo_spatialization";
    public static final java.lang.String FLAG_VGS_VSS_SYNC_MUTE_ORDER = "com.android.media.audio.vgs_vss_sync_mute_order";
    public static final java.lang.String FLAG_VOLUME_REFACTORING = "com.android.media.audio.volume_refactoring";

    public static boolean absVolumeIndexFix() {
        return FEATURE_FLAGS.absVolumeIndexFix();
    }

    public static boolean alarmMinVolumeZero() {
        return FEATURE_FLAGS.alarmMinVolumeZero();
    }

    public static boolean asDeviceConnectionFailure() {
        return FEATURE_FLAGS.asDeviceConnectionFailure();
    }

    public static boolean audioserverPermissions() {
        return FEATURE_FLAGS.audioserverPermissions();
    }

    public static boolean bluetoothMacAddressAnonymization() {
        return FEATURE_FLAGS.bluetoothMacAddressAnonymization();
    }

    public static boolean disablePrescaleAbsoluteVolume() {
        return FEATURE_FLAGS.disablePrescaleAbsoluteVolume();
    }

    public static boolean dsaOverBtLeAudio() {
        return FEATURE_FLAGS.dsaOverBtLeAudio();
    }

    public static boolean musicFxEdgeToEdge() {
        return FEATURE_FLAGS.musicFxEdgeToEdge();
    }

    public static boolean portToPiidSimplification() {
        return FEATURE_FLAGS.portToPiidSimplification();
    }

    public static boolean replaceStreamBtSco() {
        return FEATURE_FLAGS.replaceStreamBtSco();
    }

    public static boolean ringerModeAffectsAlarm() {
        return FEATURE_FLAGS.ringerModeAffectsAlarm();
    }

    public static boolean setStreamVolumeOrder() {
        return FEATURE_FLAGS.setStreamVolumeOrder();
    }

    public static boolean spatializerOffload() {
        return FEATURE_FLAGS.spatializerOffload();
    }

    public static boolean spatializerUpmix() {
        return FEATURE_FLAGS.spatializerUpmix();
    }

    public static boolean stereoSpatialization() {
        return FEATURE_FLAGS.stereoSpatialization();
    }

    public static boolean vgsVssSyncMuteOrder() {
        return FEATURE_FLAGS.vgsVssSyncMuteOrder();
    }

    public static boolean volumeRefactoring() {
        return FEATURE_FLAGS.volumeRefactoring();
    }
}
