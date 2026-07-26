package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class Constants {
    static final int ABORT_CANNOT_PROVIDE_SOURCE = 2;
    static final int ABORT_INVALID_OPERAND = 3;
    static final int ABORT_NOT_IN_CORRECT_MODE = 1;
    static final int ABORT_REFUSED = 4;
    static final int ABORT_UNABLE_TO_DETERMINE = 5;
    static final int ABORT_UNRECOGNIZED_OPCODE = 0;
    public static final int ADDR_AUDIO_SYSTEM = 5;
    public static final int ADDR_BACKUP_1 = 12;
    public static final int ADDR_BACKUP_2 = 13;
    public static final int ADDR_BROADCAST = 15;
    public static final int ADDR_INTERNAL = 0;
    public static final int ADDR_INVALID = -1;
    public static final int ADDR_PLAYBACK_1 = 4;
    public static final int ADDR_PLAYBACK_2 = 8;
    public static final int ADDR_PLAYBACK_3 = 11;
    public static final int ADDR_RECORDER_1 = 1;
    public static final int ADDR_RECORDER_2 = 2;
    public static final int ADDR_RECORDER_3 = 9;
    public static final int ADDR_SPECIFIC_USE = 14;
    public static final int ADDR_TUNER_1 = 3;
    public static final int ADDR_TUNER_2 = 6;
    public static final int ADDR_TUNER_3 = 7;
    public static final int ADDR_TUNER_4 = 10;
    public static final int ADDR_TV = 0;
    public static final int ADDR_UNREGISTERED = 15;
    static final int ALL_DEVICE_TYPES_AUDIO_SYSTEM = 3;
    static final int ALL_DEVICE_TYPES_PLAYBACK = 4;
    static final int ALL_DEVICE_TYPES_RECORDER = 6;
    static final int ALL_DEVICE_TYPES_SWITCH = 2;
    static final int ALL_DEVICE_TYPES_TUNER = 5;
    static final int ALL_DEVICE_TYPES_TV = 7;
    static final int ALWAYS_SYSTEM_AUDIO_CONTROL_ON_POWER_ON = 0;
    static final int AUDIO_CODEC_AAC = 6;
    static final int AUDIO_CODEC_ATRAC = 8;
    static final int AUDIO_CODEC_DD = 2;
    static final int AUDIO_CODEC_DDP = 10;
    static final int AUDIO_CODEC_DST = 13;
    static final int AUDIO_CODEC_DTS = 7;
    static final int AUDIO_CODEC_DTSHD = 11;
    static final int AUDIO_CODEC_LPCM = 1;
    static final int AUDIO_CODEC_MAX = 15;
    static final int AUDIO_CODEC_MP3 = 4;
    static final int AUDIO_CODEC_MPEG1 = 3;
    static final int AUDIO_CODEC_MPEG2 = 5;
    static final int AUDIO_CODEC_NONE = 0;
    static final int AUDIO_CODEC_ONEBITAUDIO = 9;
    static final int AUDIO_CODEC_TRUEHD = 12;
    static final int AUDIO_CODEC_WMAPRO = 14;
    static final java.lang.String AUDIO_DEVICE_ARC_IN = "ARC_IN";
    static final java.lang.String AUDIO_DEVICE_SPDIF = "SPDIF";
    static final int AUDIO_FORMAT_MASK = 120;
    static final int AUDIO_VOLUME_STATUS_UNKNOWN = 127;
    static final int CEC_SWITCH_ARC = 17;
    static final int CEC_SWITCH_AUX = 20;
    static final int CEC_SWITCH_BLUETOOTH = 18;
    static final int CEC_SWITCH_HDMI1 = 1;
    static final int CEC_SWITCH_HDMI2 = 2;
    static final int CEC_SWITCH_HDMI3 = 3;
    static final int CEC_SWITCH_HDMI4 = 4;
    static final int CEC_SWITCH_HDMI5 = 5;
    static final int CEC_SWITCH_HDMI6 = 6;
    static final int CEC_SWITCH_HDMI7 = 7;
    static final int CEC_SWITCH_HDMI8 = 8;
    static final int CEC_SWITCH_HOME = 0;
    static final int CEC_SWITCH_OPTICAL = 19;
    static final int CEC_SWITCH_PORT_MAX = 21;
    static final java.lang.String DEVICE_CONFIG_FEATURE_FLAG_ENABLE_EARC_TX = "enable_earc_tx";
    static final java.lang.String DEVICE_CONFIG_FEATURE_FLAG_SOUNDBAR_MODE = "enable_soundbar_mode";
    static final java.lang.String DEVICE_CONFIG_FEATURE_FLAG_TRANSITION_ARC_TO_EARC_TX = "transition_arc_to_earc_tx";
    static final java.lang.String DEVICE_CONFIG_FEATURE_FLAG_TV_NUMERIC_SOUNDBAR_VOLUME_UI = "enable_numeric_soundbar_volume_ui_on_tv";
    static final int DEVICE_FEATURE_SINK_SUPPORTS_ARC_TX = 2;
    static final int DEVICE_FEATURE_SOURCE_SUPPORTS_ARC_RX = 1;
    static final int DEVICE_FEATURE_SUPPORTS_DECK_CONTROL = 4;
    static final int DEVICE_FEATURE_SUPPORTS_SET_AUDIO_RATE = 3;
    static final int DEVICE_FEATURE_TV_SUPPORTS_RECORD_TV_SCREEN = 6;
    static final int DEVICE_FEATURE_TV_SUPPORTS_SET_OSD_STRING = 5;
    static final int DISABLED = 0;
    static final int ENABLED = 1;
    static final int FALSE = 0;
    static final int HANDLED = -1;
    static final int HDMI_EARC_STATUS_ARC_PENDING = 2;
    static final int HDMI_EARC_STATUS_EARC_CONNECTED = 3;
    static final int HDMI_EARC_STATUS_EARC_PENDING = 1;
    static final int HDMI_EARC_STATUS_IDLE = 0;
    static final int HDMI_EARC_STATUS_UNKNOWN = -1;
    static final int HDMI_HPD_TYPE_PHYSICAL = 0;
    static final int HDMI_HPD_TYPE_STATUS_BIT = 1;
    static final int INVALID_PHYSICAL_ADDRESS = 65535;
    static final int INVALID_PORT_ID = -1;
    static final int MENU_STATE_ACTIVATED = 0;
    static final int MENU_STATE_DEACTIVATED = 1;
    static final int MESSAGE_ABORT = 255;
    static final int MESSAGE_ACTIVE_SOURCE = 130;
    static final int MESSAGE_CDC_MESSAGE = 248;
    static final int MESSAGE_CEC_VERSION = 158;
    static final int MESSAGE_CLEAR_ANALOG_TIMER = 51;
    static final int MESSAGE_CLEAR_DIGITAL_TIMER = 153;
    static final int MESSAGE_CLEAR_EXTERNAL_TIMER = 161;
    static final int MESSAGE_DECK_CONTROL = 66;
    static final int MESSAGE_DECK_STATUS = 27;
    static final int MESSAGE_DEVICE_VENDOR_ID = 135;
    static final int MESSAGE_FEATURE_ABORT = 0;
    static final int MESSAGE_GET_CEC_VERSION = 159;
    static final int MESSAGE_GET_MENU_LANGUAGE = 145;
    static final int MESSAGE_GIVE_AUDIO_STATUS = 113;
    static final int MESSAGE_GIVE_DECK_STATUS = 26;
    static final int MESSAGE_GIVE_DEVICE_POWER_STATUS = 143;
    static final int MESSAGE_GIVE_DEVICE_VENDOR_ID = 140;
    static final int MESSAGE_GIVE_FEATURES = 165;
    static final int MESSAGE_GIVE_OSD_NAME = 70;
    static final int MESSAGE_GIVE_PHYSICAL_ADDRESS = 131;
    static final int MESSAGE_GIVE_SYSTEM_AUDIO_MODE_STATUS = 125;
    static final int MESSAGE_GIVE_TUNER_DEVICE_STATUS = 8;
    static final int MESSAGE_IMAGE_VIEW_ON = 4;
    static final int MESSAGE_INACTIVE_SOURCE = 157;
    static final int MESSAGE_INITIATE_ARC = 192;
    static final int MESSAGE_MENU_REQUEST = 141;
    static final int MESSAGE_MENU_STATUS = 142;
    static final int MESSAGE_PLAY = 65;
    static final int MESSAGE_RECORD_OFF = 11;
    static final int MESSAGE_RECORD_ON = 9;
    static final int MESSAGE_RECORD_STATUS = 10;
    static final int MESSAGE_RECORD_TV_SCREEN = 15;
    static final int MESSAGE_REPORT_ARC_INITIATED = 193;
    static final int MESSAGE_REPORT_ARC_TERMINATED = 194;
    static final int MESSAGE_REPORT_AUDIO_STATUS = 122;
    static final int MESSAGE_REPORT_CURRENT_LATENCY = 168;
    static final int MESSAGE_REPORT_FEATURES = 166;
    static final int MESSAGE_REPORT_PHYSICAL_ADDRESS = 132;
    static final int MESSAGE_REPORT_POWER_STATUS = 144;
    static final int MESSAGE_REPORT_SHORT_AUDIO_DESCRIPTOR = 163;
    static final int MESSAGE_REQUEST_ACTIVE_SOURCE = 133;
    static final int MESSAGE_REQUEST_ARC_INITIATION = 195;
    static final int MESSAGE_REQUEST_ARC_TERMINATION = 196;
    static final int MESSAGE_REQUEST_CURRENT_LATENCY = 167;
    static final int MESSAGE_REQUEST_SHORT_AUDIO_DESCRIPTOR = 164;
    static final int MESSAGE_ROUTING_CHANGE = 128;
    static final int MESSAGE_ROUTING_INFORMATION = 129;
    static final int MESSAGE_SELECT_ANALOG_SERVICE = 146;
    static final int MESSAGE_SELECT_DIGITAL_SERVICE = 147;
    static final int MESSAGE_SET_ANALOG_TIMER = 52;
    static final int MESSAGE_SET_AUDIO_RATE = 154;
    static final int MESSAGE_SET_AUDIO_VOLUME_LEVEL = 115;
    static final int MESSAGE_SET_DIGITAL_TIMER = 151;
    static final int MESSAGE_SET_EXTERNAL_TIMER = 162;
    static final int MESSAGE_SET_MENU_LANGUAGE = 50;
    static final int MESSAGE_SET_OSD_NAME = 71;
    static final int MESSAGE_SET_OSD_STRING = 100;
    static final int MESSAGE_SET_STREAM_PATH = 134;
    static final int MESSAGE_SET_SYSTEM_AUDIO_MODE = 114;
    static final int MESSAGE_SET_TIMER_PROGRAM_TITLE = 103;
    static final int MESSAGE_STANDBY = 54;
    static final int MESSAGE_SYSTEM_AUDIO_MODE_REQUEST = 112;
    static final int MESSAGE_SYSTEM_AUDIO_MODE_STATUS = 126;
    static final int MESSAGE_TERMINATE_ARC = 197;
    static final int MESSAGE_TEXT_VIEW_ON = 13;
    static final int MESSAGE_TIMER_CLEARED_STATUS = 67;
    static final int MESSAGE_TIMER_STATUS = 53;
    static final int MESSAGE_TUNER_DEVICE_STATUS = 7;
    static final int MESSAGE_TUNER_STEP_DECREMENT = 6;
    static final int MESSAGE_TUNER_STEP_INCREMENT = 5;
    static final int MESSAGE_USER_CONTROL_PRESSED = 68;
    static final int MESSAGE_USER_CONTROL_RELEASED = 69;
    static final int MESSAGE_VENDOR_COMMAND = 137;
    static final int MESSAGE_VENDOR_COMMAND_WITH_ID = 160;
    static final int MESSAGE_VENDOR_REMOTE_BUTTON_DOWN = 138;
    static final int MESSAGE_VENDOR_REMOTE_BUTTON_UP = 139;
    static final int NEVER_SYSTEM_AUDIO_CONTROL_ON_POWER_ON = 2;
    static final int NOT_HANDLED = -2;
    static final int OPTION_MHL_ENABLE = 103;
    static final int OPTION_MHL_INPUT_SWITCHING = 101;
    static final int OPTION_MHL_POWER_CHARGE = 102;
    static final int OPTION_MHL_SERVICE_CONTROL = 104;
    static final int PATH_RELATIONSHIP_ANCESTOR = 2;
    static final int PATH_RELATIONSHIP_DESCENDANT = 3;
    static final int PATH_RELATIONSHIP_DIFFERENT_BRANCH = 1;
    static final int PATH_RELATIONSHIP_SAME = 5;
    static final int PATH_RELATIONSHIP_SIBLING = 4;
    static final int PATH_RELATIONSHIP_UNKNOWN = 0;
    static final int POLL_ITERATION_IN_ORDER = 65536;
    static final int POLL_ITERATION_REVERSE_ORDER = 131072;
    static final int POLL_ITERATION_STRATEGY_MASK = 196608;
    static final int POLL_STRATEGY_MASK = 3;
    static final int POLL_STRATEGY_REMOTES_DEVICES = 1;
    static final int POLL_STRATEGY_SYSTEM_AUDIO = 2;
    static final java.lang.String PROPERTY_ARC_SUPPORT = "persist.sys.hdmi.property_arc_support";
    static final java.lang.String PROPERTY_KEEP_AWAKE = "persist.sys.hdmi.keep_awake";
    static final java.lang.String PROPERTY_LAST_SYSTEM_AUDIO_CONTROL = "persist.sys.hdmi.last_system_audio_control";
    static final java.lang.String PROPERTY_PREFERRED_ADDRESS_AUDIO_SYSTEM = "persist.sys.hdmi.addr.audiosystem";
    static final java.lang.String PROPERTY_PREFERRED_ADDRESS_PLAYBACK = "persist.sys.hdmi.addr.playback";
    static final java.lang.String PROPERTY_PREFERRED_ADDRESS_TV = "persist.sys.hdmi.addr.tv";
    static final java.lang.String PROPERTY_STRIP_AUDIO_TV_NO_SYSTEM_AUDIO = "persist.sys.hdmi.property_strip_audio_tv_no_system_audio";
    static final java.lang.String PROPERTY_SYSTEM_AUDIO_CONTROL_ON_POWER_ON = "persist.sys.hdmi.system_audio_control_on_power_on";
    static final java.lang.String PROPERTY_SYSTEM_AUDIO_MODE_AUDIO_PORT = "persist.sys.hdmi.property_sytem_audio_mode_audio_port";
    static final int RC_PROFILE_SOURCE = 1;
    static final int RC_PROFILE_SOURCE_HANDLES_CONTENTS_MENU = 2;
    static final int RC_PROFILE_SOURCE_HANDLES_MEDIA_CONTEXT_SENSITIVE_MENU = 0;
    static final int RC_PROFILE_SOURCE_HANDLES_ROOT_MENU = 4;
    static final int RC_PROFILE_SOURCE_HANDLES_SETUP_MENU = 3;
    static final int RC_PROFILE_SOURCE_HANDLES_TOP_MENU = 1;
    static final int RC_PROFILE_TV = 0;
    static final int RC_PROFILE_TV_FOUR = 14;
    static final int RC_PROFILE_TV_NONE = 0;
    static final int RC_PROFILE_TV_ONE = 2;
    static final int RC_PROFILE_TV_THREE = 10;
    static final int RC_PROFILE_TV_TWO = 6;
    static final int RECORDING_TYPE_ANALOGUE_RF = 2;
    static final int RECORDING_TYPE_DIGITAL_RF = 1;
    static final int RECORDING_TYPE_EXTERNAL_PHYSICAL_ADDRESS = 3;
    static final int RECORDING_TYPE_OWN_SOURCE = 4;
    static final int ROUTING_PATH_TOP_MASK = 61440;
    static final int ROUTING_PATH_TOP_SHIFT = 12;
    static final int SYSTEM_AUDIO_STATUS_OFF = 0;
    static final int SYSTEM_AUDIO_STATUS_ON = 1;
    static final int TRUE = 1;
    static final int TV_PHYSICAL_ADDRESS = 0;
    static final int UNKNOWN_VOLUME = -1;
    static final int USE_LAST_STATE_SYSTEM_AUDIO_CONTROL_ON_POWER_ON = 1;
    static final int VENDOR_ID_UNKNOWN = 16777215;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AbortReason {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AudioCodec {
    }

    public @interface AudioDevice {
    }

    @interface DeviceFeature {
    }

    @interface DeviceType {
    }

    @interface EarcStatus {
    }

    @interface FeatureFlag {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface FeatureOpcode {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface HandleMessageResult {
    }

    @interface HpdSignalType {
    }

    @interface LocalActivePort {
    }

    @interface PathRelationship {
    }

    @interface RcProfile {
    }

    @interface RcProfileSource {
    }

    @interface RcProfileTv {
    }

    @interface SystemAudioControlOnPowerOn {
    }

    private Constants() {
    }
}
