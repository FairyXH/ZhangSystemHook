package android.hardware.tv.cec.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class CecMessageType {
    public static final int ABORT = 255;
    public static final int ACTIVE_SOURCE = 130;
    public static final int CEC_VERSION = 158;
    public static final int CLEAR_ANALOG_TIMER = 51;
    public static final int CLEAR_DIGITAL_TIMER = 153;
    public static final int CLEAR_EXTERNAL_TIMER = 161;
    public static final int DECK_CONTROL = 66;
    public static final int DECK_STATUS = 27;
    public static final int DEVICE_VENDOR_ID = 135;
    public static final int FEATURE_ABORT = 0;
    public static final int GET_CEC_VERSION = 159;
    public static final int GET_MENU_LANGUAGE = 145;
    public static final int GIVE_AUDIO_STATUS = 113;
    public static final int GIVE_DECK_STATUS = 26;
    public static final int GIVE_DEVICE_POWER_STATUS = 143;
    public static final int GIVE_DEVICE_VENDOR_ID = 140;
    public static final int GIVE_OSD_NAME = 70;
    public static final int GIVE_PHYSICAL_ADDRESS = 131;
    public static final int GIVE_SYSTEM_AUDIO_MODE_STATUS = 125;
    public static final int GIVE_TUNER_DEVICE_STATUS = 8;
    public static final int IMAGE_VIEW_ON = 4;
    public static final int INACTIVE_SOURCE = 157;
    public static final int INITIATE_ARC = 192;
    public static final int MENU_REQUEST = 141;
    public static final int MENU_STATUS = 142;
    public static final int PLAY = 65;
    public static final int RECORD_OFF = 11;
    public static final int RECORD_ON = 9;
    public static final int RECORD_STATUS = 10;
    public static final int RECORD_TV_SCREEN = 15;
    public static final int REPORT_ARC_INITIATED = 193;
    public static final int REPORT_ARC_TERMINATED = 194;
    public static final int REPORT_AUDIO_STATUS = 122;
    public static final int REPORT_PHYSICAL_ADDRESS = 132;
    public static final int REPORT_POWER_STATUS = 144;
    public static final int REPORT_SHORT_AUDIO_DESCRIPTOR = 163;
    public static final int REQUEST_ACTIVE_SOURCE = 133;
    public static final int REQUEST_ARC_INITIATION = 195;
    public static final int REQUEST_ARC_TERMINATION = 196;
    public static final int REQUEST_SHORT_AUDIO_DESCRIPTOR = 164;
    public static final int ROUTING_CHANGE = 128;
    public static final int ROUTING_INFORMATION = 129;
    public static final int SELECT_ANALOG_SERVICE = 146;
    public static final int SELECT_DIGITAL_SERVICE = 147;
    public static final int SET_ANALOG_TIMER = 52;
    public static final int SET_AUDIO_RATE = 154;
    public static final int SET_DIGITAL_TIMER = 151;
    public static final int SET_EXTERNAL_TIMER = 162;
    public static final int SET_MENU_LANGUAGE = 50;
    public static final int SET_OSD_NAME = 71;
    public static final int SET_OSD_STRING = 100;
    public static final int SET_STREAM_PATH = 134;
    public static final int SET_SYSTEM_AUDIO_MODE = 114;
    public static final int SET_TIMER_PROGRAM_TITLE = 103;
    public static final int STANDBY = 54;
    public static final int SYSTEM_AUDIO_MODE_REQUEST = 112;
    public static final int SYSTEM_AUDIO_MODE_STATUS = 126;
    public static final int TERMINATE_ARC = 197;
    public static final int TEXT_VIEW_ON = 13;
    public static final int TIMER_CLEARED_STATUS = 67;
    public static final int TIMER_STATUS = 53;
    public static final int TUNER_DEVICE_STATUS = 7;
    public static final int TUNER_STEP_DECREMENT = 6;
    public static final int TUNER_STEP_INCREMENT = 5;
    public static final int USER_CONTROL_PRESSED = 68;
    public static final int USER_CONTROL_RELEASED = 69;
    public static final int VENDOR_COMMAND = 137;
    public static final int VENDOR_COMMAND_WITH_ID = 160;
    public static final int VENDOR_REMOTE_BUTTON_DOWN = 138;
    public static final int VENDOR_REMOTE_BUTTON_UP = 139;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "FEATURE_ABORT";
        }
        if (o == 4) {
            return "IMAGE_VIEW_ON";
        }
        if (o == 5) {
            return "TUNER_STEP_INCREMENT";
        }
        if (o == 6) {
            return "TUNER_STEP_DECREMENT";
        }
        if (o == 7) {
            return "TUNER_DEVICE_STATUS";
        }
        if (o == 8) {
            return "GIVE_TUNER_DEVICE_STATUS";
        }
        if (o == 9) {
            return "RECORD_ON";
        }
        if (o == 10) {
            return "RECORD_STATUS";
        }
        if (o == 11) {
            return "RECORD_OFF";
        }
        if (o == 13) {
            return "TEXT_VIEW_ON";
        }
        if (o == 15) {
            return "RECORD_TV_SCREEN";
        }
        if (o == 26) {
            return "GIVE_DECK_STATUS";
        }
        if (o == 27) {
            return "DECK_STATUS";
        }
        if (o == 50) {
            return "SET_MENU_LANGUAGE";
        }
        if (o == 51) {
            return "CLEAR_ANALOG_TIMER";
        }
        if (o == 52) {
            return "SET_ANALOG_TIMER";
        }
        if (o == 53) {
            return "TIMER_STATUS";
        }
        if (o == 54) {
            return "STANDBY";
        }
        if (o == 65) {
            return "PLAY";
        }
        if (o == 66) {
            return "DECK_CONTROL";
        }
        if (o == 67) {
            return "TIMER_CLEARED_STATUS";
        }
        if (o == 68) {
            return "USER_CONTROL_PRESSED";
        }
        if (o == 69) {
            return "USER_CONTROL_RELEASED";
        }
        if (o == 70) {
            return "GIVE_OSD_NAME";
        }
        if (o == 71) {
            return "SET_OSD_NAME";
        }
        if (o == 100) {
            return "SET_OSD_STRING";
        }
        if (o == 103) {
            return "SET_TIMER_PROGRAM_TITLE";
        }
        if (o == 112) {
            return "SYSTEM_AUDIO_MODE_REQUEST";
        }
        if (o == 113) {
            return "GIVE_AUDIO_STATUS";
        }
        if (o == 114) {
            return "SET_SYSTEM_AUDIO_MODE";
        }
        if (o == 122) {
            return "REPORT_AUDIO_STATUS";
        }
        if (o == 125) {
            return "GIVE_SYSTEM_AUDIO_MODE_STATUS";
        }
        if (o == 126) {
            return "SYSTEM_AUDIO_MODE_STATUS";
        }
        if (o == 128) {
            return "ROUTING_CHANGE";
        }
        if (o == 129) {
            return "ROUTING_INFORMATION";
        }
        if (o == 130) {
            return "ACTIVE_SOURCE";
        }
        if (o == 131) {
            return "GIVE_PHYSICAL_ADDRESS";
        }
        if (o == 132) {
            return "REPORT_PHYSICAL_ADDRESS";
        }
        if (o == 133) {
            return "REQUEST_ACTIVE_SOURCE";
        }
        if (o == 134) {
            return "SET_STREAM_PATH";
        }
        if (o == 135) {
            return "DEVICE_VENDOR_ID";
        }
        if (o == 137) {
            return "VENDOR_COMMAND";
        }
        if (o == 138) {
            return "VENDOR_REMOTE_BUTTON_DOWN";
        }
        if (o == 139) {
            return "VENDOR_REMOTE_BUTTON_UP";
        }
        if (o == 140) {
            return "GIVE_DEVICE_VENDOR_ID";
        }
        if (o == 141) {
            return "MENU_REQUEST";
        }
        if (o == 142) {
            return "MENU_STATUS";
        }
        if (o == 143) {
            return "GIVE_DEVICE_POWER_STATUS";
        }
        if (o == 144) {
            return "REPORT_POWER_STATUS";
        }
        if (o == 145) {
            return "GET_MENU_LANGUAGE";
        }
        if (o == 146) {
            return "SELECT_ANALOG_SERVICE";
        }
        if (o == 147) {
            return "SELECT_DIGITAL_SERVICE";
        }
        if (o == 151) {
            return "SET_DIGITAL_TIMER";
        }
        if (o == 153) {
            return "CLEAR_DIGITAL_TIMER";
        }
        if (o == 154) {
            return "SET_AUDIO_RATE";
        }
        if (o == 157) {
            return "INACTIVE_SOURCE";
        }
        if (o == 158) {
            return "CEC_VERSION";
        }
        if (o == 159) {
            return "GET_CEC_VERSION";
        }
        if (o == 160) {
            return "VENDOR_COMMAND_WITH_ID";
        }
        if (o == 161) {
            return "CLEAR_EXTERNAL_TIMER";
        }
        if (o == 162) {
            return "SET_EXTERNAL_TIMER";
        }
        if (o == 163) {
            return "REPORT_SHORT_AUDIO_DESCRIPTOR";
        }
        if (o == 164) {
            return "REQUEST_SHORT_AUDIO_DESCRIPTOR";
        }
        if (o == 192) {
            return "INITIATE_ARC";
        }
        if (o == 193) {
            return "REPORT_ARC_INITIATED";
        }
        if (o == 194) {
            return "REPORT_ARC_TERMINATED";
        }
        if (o == 195) {
            return "REQUEST_ARC_INITIATION";
        }
        if (o == 196) {
            return "REQUEST_ARC_TERMINATION";
        }
        if (o == 197) {
            return "TERMINATE_ARC";
        }
        if (o == 255) {
            return "ABORT";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("FEATURE_ABORT");
        if ((o & 4) == 4) {
            list.add("IMAGE_VIEW_ON");
            flipped = 0 | 4;
        }
        if ((o & 5) == 5) {
            list.add("TUNER_STEP_INCREMENT");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("TUNER_STEP_DECREMENT");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("TUNER_DEVICE_STATUS");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("GIVE_TUNER_DEVICE_STATUS");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("RECORD_ON");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("RECORD_STATUS");
            flipped |= 10;
        }
        if ((o & 11) == 11) {
            list.add("RECORD_OFF");
            flipped |= 11;
        }
        if ((o & 13) == 13) {
            list.add("TEXT_VIEW_ON");
            flipped |= 13;
        }
        if ((o & 15) == 15) {
            list.add("RECORD_TV_SCREEN");
            flipped |= 15;
        }
        if ((o & 26) == 26) {
            list.add("GIVE_DECK_STATUS");
            flipped |= 26;
        }
        if ((o & 27) == 27) {
            list.add("DECK_STATUS");
            flipped |= 27;
        }
        if ((o & 50) == 50) {
            list.add("SET_MENU_LANGUAGE");
            flipped |= 50;
        }
        if ((o & 51) == 51) {
            list.add("CLEAR_ANALOG_TIMER");
            flipped |= 51;
        }
        if ((o & 52) == 52) {
            list.add("SET_ANALOG_TIMER");
            flipped |= 52;
        }
        if ((o & 53) == 53) {
            list.add("TIMER_STATUS");
            flipped |= 53;
        }
        if ((o & 54) == 54) {
            list.add("STANDBY");
            flipped |= 54;
        }
        if ((o & 65) == 65) {
            list.add("PLAY");
            flipped |= 65;
        }
        if ((o & 66) == 66) {
            list.add("DECK_CONTROL");
            flipped |= 66;
        }
        if ((o & 67) == 67) {
            list.add("TIMER_CLEARED_STATUS");
            flipped |= 67;
        }
        if ((o & 68) == 68) {
            list.add("USER_CONTROL_PRESSED");
            flipped |= 68;
        }
        if ((o & 69) == 69) {
            list.add("USER_CONTROL_RELEASED");
            flipped |= 69;
        }
        if ((o & 70) == 70) {
            list.add("GIVE_OSD_NAME");
            flipped |= 70;
        }
        if ((o & 71) == 71) {
            list.add("SET_OSD_NAME");
            flipped |= 71;
        }
        if ((o & 100) == 100) {
            list.add("SET_OSD_STRING");
            flipped |= 100;
        }
        if ((o & 103) == 103) {
            list.add("SET_TIMER_PROGRAM_TITLE");
            flipped |= 103;
        }
        if ((o & 112) == 112) {
            list.add("SYSTEM_AUDIO_MODE_REQUEST");
            flipped |= 112;
        }
        if ((o & 113) == 113) {
            list.add("GIVE_AUDIO_STATUS");
            flipped |= 113;
        }
        if ((o & 114) == 114) {
            list.add("SET_SYSTEM_AUDIO_MODE");
            flipped |= 114;
        }
        if ((o & 122) == 122) {
            list.add("REPORT_AUDIO_STATUS");
            flipped |= 122;
        }
        if ((o & 125) == 125) {
            list.add("GIVE_SYSTEM_AUDIO_MODE_STATUS");
            flipped |= 125;
        }
        if ((o & 126) == 126) {
            list.add("SYSTEM_AUDIO_MODE_STATUS");
            flipped |= 126;
        }
        if ((o & 128) == 128) {
            list.add("ROUTING_CHANGE");
            flipped |= 128;
        }
        if ((o & 129) == 129) {
            list.add("ROUTING_INFORMATION");
            flipped |= 129;
        }
        if ((o & 130) == 130) {
            list.add("ACTIVE_SOURCE");
            flipped |= 130;
        }
        if ((o & 131) == 131) {
            list.add("GIVE_PHYSICAL_ADDRESS");
            flipped |= 131;
        }
        if ((o & 132) == 132) {
            list.add("REPORT_PHYSICAL_ADDRESS");
            flipped |= 132;
        }
        if ((o & 133) == 133) {
            list.add("REQUEST_ACTIVE_SOURCE");
            flipped |= 133;
        }
        if ((o & 134) == 134) {
            list.add("SET_STREAM_PATH");
            flipped |= 134;
        }
        if ((o & 135) == 135) {
            list.add("DEVICE_VENDOR_ID");
            flipped |= 135;
        }
        if ((o & 137) == 137) {
            list.add("VENDOR_COMMAND");
            flipped |= 137;
        }
        if ((o & 138) == 138) {
            list.add("VENDOR_REMOTE_BUTTON_DOWN");
            flipped |= 138;
        }
        if ((o & 139) == 139) {
            list.add("VENDOR_REMOTE_BUTTON_UP");
            flipped |= 139;
        }
        if ((o & 140) == 140) {
            list.add("GIVE_DEVICE_VENDOR_ID");
            flipped |= 140;
        }
        if ((o & 141) == 141) {
            list.add("MENU_REQUEST");
            flipped |= 141;
        }
        if ((o & 142) == 142) {
            list.add("MENU_STATUS");
            flipped |= 142;
        }
        if ((o & 143) == 143) {
            list.add("GIVE_DEVICE_POWER_STATUS");
            flipped |= 143;
        }
        if ((o & 144) == 144) {
            list.add("REPORT_POWER_STATUS");
            flipped |= 144;
        }
        if ((o & 145) == 145) {
            list.add("GET_MENU_LANGUAGE");
            flipped |= 145;
        }
        if ((o & 146) == 146) {
            list.add("SELECT_ANALOG_SERVICE");
            flipped |= 146;
        }
        if ((o & 147) == 147) {
            list.add("SELECT_DIGITAL_SERVICE");
            flipped |= 147;
        }
        if ((o & 151) == 151) {
            list.add("SET_DIGITAL_TIMER");
            flipped |= 151;
        }
        if ((o & 153) == 153) {
            list.add("CLEAR_DIGITAL_TIMER");
            flipped |= 153;
        }
        if ((o & 154) == 154) {
            list.add("SET_AUDIO_RATE");
            flipped |= 154;
        }
        if ((o & 157) == 157) {
            list.add("INACTIVE_SOURCE");
            flipped |= 157;
        }
        if ((o & 158) == 158) {
            list.add("CEC_VERSION");
            flipped |= 158;
        }
        if ((o & 159) == 159) {
            list.add("GET_CEC_VERSION");
            flipped |= 159;
        }
        if ((o & 160) == 160) {
            list.add("VENDOR_COMMAND_WITH_ID");
            flipped |= 160;
        }
        if ((o & 161) == 161) {
            list.add("CLEAR_EXTERNAL_TIMER");
            flipped |= 161;
        }
        if ((o & 162) == 162) {
            list.add("SET_EXTERNAL_TIMER");
            flipped |= 162;
        }
        if ((o & 163) == 163) {
            list.add("REPORT_SHORT_AUDIO_DESCRIPTOR");
            flipped |= 163;
        }
        if ((o & 164) == 164) {
            list.add("REQUEST_SHORT_AUDIO_DESCRIPTOR");
            flipped |= 164;
        }
        if ((o & 192) == 192) {
            list.add("INITIATE_ARC");
            flipped |= 192;
        }
        if ((o & 193) == 193) {
            list.add("REPORT_ARC_INITIATED");
            flipped |= 193;
        }
        if ((o & 194) == 194) {
            list.add("REPORT_ARC_TERMINATED");
            flipped |= 194;
        }
        if ((o & 195) == 195) {
            list.add("REQUEST_ARC_INITIATION");
            flipped |= 195;
        }
        if ((o & 196) == 196) {
            list.add("REQUEST_ARC_TERMINATION");
            flipped |= 196;
        }
        if ((o & 197) == 197) {
            list.add("TERMINATE_ARC");
            flipped |= 197;
        }
        if ((o & 255) == 255) {
            list.add("ABORT");
            flipped |= 255;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
