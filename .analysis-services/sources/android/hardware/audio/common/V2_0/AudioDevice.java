package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioDevice {
    public static final int BIT_DEFAULT = 1073741824;
    public static final int BIT_IN = Integer.MIN_VALUE;
    public static final int IN_ALL = -1021313025;
    public static final int IN_ALL_SCO = -2147483640;
    public static final int IN_ALL_USB = -2113923072;
    public static final int IN_AMBIENT = -2147483646;
    public static final int IN_ANLG_DOCK_HEADSET = -2147483136;
    public static final int IN_AUX_DIGITAL = -2147483616;
    public static final int IN_BACK_MIC = -2147483520;
    public static final int IN_BLUETOOTH_A2DP = -2147352576;
    public static final int IN_BLUETOOTH_SCO_HEADSET = -2147483640;
    public static final int IN_BUILTIN_MIC = -2147483644;
    public static final int IN_BUS = -2146435072;
    public static final int IN_COMMUNICATION = -2147483647;
    public static final int IN_DEFAULT = -1073741824;
    public static final int IN_DGTL_DOCK_HEADSET = -2147482624;
    public static final int IN_FM_TUNER = -2147475456;
    public static final int IN_HDMI = -2147483616;
    public static final int IN_IP = -2146959360;
    public static final int IN_LINE = -2147450880;
    public static final int IN_LOOPBACK = -2147221504;
    public static final int IN_PROXY = -2130706432;
    public static final int IN_REMOTE_SUBMIX = -2147483392;
    public static final int IN_SPDIF = -2147418112;
    public static final int IN_TELEPHONY_RX = -2147483584;
    public static final int IN_TV_TUNER = -2147467264;
    public static final int IN_USB_ACCESSORY = -2147481600;
    public static final int IN_USB_DEVICE = -2147479552;
    public static final int IN_USB_HEADSET = -2113929216;
    public static final int IN_VOICE_CALL = -2147483584;
    public static final int IN_WIRED_HEADSET = -2147483632;
    public static final int NONE = 0;
    public static final int OUT_ALL = 1207959551;
    public static final int OUT_ALL_A2DP = 896;
    public static final int OUT_ALL_SCO = 112;
    public static final int OUT_ALL_USB = 67133440;
    public static final int OUT_ANLG_DOCK_HEADSET = 2048;
    public static final int OUT_AUX_DIGITAL = 1024;
    public static final int OUT_AUX_LINE = 2097152;
    public static final int OUT_BLUETOOTH_A2DP = 128;
    public static final int OUT_BLUETOOTH_A2DP_HEADPHONES = 256;
    public static final int OUT_BLUETOOTH_A2DP_SPEAKER = 512;
    public static final int OUT_BLUETOOTH_SCO = 16;
    public static final int OUT_BLUETOOTH_SCO_CARKIT = 64;
    public static final int OUT_BLUETOOTH_SCO_HEADSET = 32;
    public static final int OUT_BUS = 16777216;
    public static final int OUT_DEFAULT = 1073741824;
    public static final int OUT_DGTL_DOCK_HEADSET = 4096;
    public static final int OUT_EARPIECE = 1;
    public static final int OUT_FM = 1048576;
    public static final int OUT_HDMI = 1024;
    public static final int OUT_HDMI_ARC = 262144;
    public static final int OUT_IP = 8388608;
    public static final int OUT_LINE = 131072;
    public static final int OUT_PROXY = 33554432;
    public static final int OUT_REMOTE_SUBMIX = 32768;
    public static final int OUT_SPDIF = 524288;
    public static final int OUT_SPEAKER = 2;
    public static final int OUT_SPEAKER_SAFE = 4194304;
    public static final int OUT_TELEPHONY_TX = 65536;
    public static final int OUT_USB_ACCESSORY = 8192;
    public static final int OUT_USB_DEVICE = 16384;
    public static final int OUT_USB_HEADSET = 67108864;
    public static final int OUT_WIRED_HEADPHONE = 8;
    public static final int OUT_WIRED_HEADSET = 4;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "NONE";
        }
        if (o == Integer.MIN_VALUE) {
            return "BIT_IN";
        }
        if (o == 1073741824) {
            return "BIT_DEFAULT";
        }
        if (o == 1) {
            return "OUT_EARPIECE";
        }
        if (o == 2) {
            return "OUT_SPEAKER";
        }
        if (o == 4) {
            return "OUT_WIRED_HEADSET";
        }
        if (o == 8) {
            return "OUT_WIRED_HEADPHONE";
        }
        if (o == 16) {
            return "OUT_BLUETOOTH_SCO";
        }
        if (o == 32) {
            return "OUT_BLUETOOTH_SCO_HEADSET";
        }
        if (o == 64) {
            return "OUT_BLUETOOTH_SCO_CARKIT";
        }
        if (o == 128) {
            return "OUT_BLUETOOTH_A2DP";
        }
        if (o == 256) {
            return "OUT_BLUETOOTH_A2DP_HEADPHONES";
        }
        if (o == 512) {
            return "OUT_BLUETOOTH_A2DP_SPEAKER";
        }
        if (o == 1024) {
            return "OUT_AUX_DIGITAL";
        }
        if (o == 1024) {
            return "OUT_HDMI";
        }
        if (o == 2048) {
            return "OUT_ANLG_DOCK_HEADSET";
        }
        if (o == 4096) {
            return "OUT_DGTL_DOCK_HEADSET";
        }
        if (o == 8192) {
            return "OUT_USB_ACCESSORY";
        }
        if (o == 16384) {
            return "OUT_USB_DEVICE";
        }
        if (o == 32768) {
            return "OUT_REMOTE_SUBMIX";
        }
        if (o == 65536) {
            return "OUT_TELEPHONY_TX";
        }
        if (o == 131072) {
            return "OUT_LINE";
        }
        if (o == 262144) {
            return "OUT_HDMI_ARC";
        }
        if (o == 524288) {
            return "OUT_SPDIF";
        }
        if (o == 1048576) {
            return "OUT_FM";
        }
        if (o == 2097152) {
            return "OUT_AUX_LINE";
        }
        if (o == 4194304) {
            return "OUT_SPEAKER_SAFE";
        }
        if (o == 8388608) {
            return "OUT_IP";
        }
        if (o == 16777216) {
            return "OUT_BUS";
        }
        if (o == 33554432) {
            return "OUT_PROXY";
        }
        if (o == 67108864) {
            return "OUT_USB_HEADSET";
        }
        if (o == 1073741824) {
            return "OUT_DEFAULT";
        }
        if (o == 1207959551) {
            return "OUT_ALL";
        }
        if (o == 896) {
            return "OUT_ALL_A2DP";
        }
        if (o == 112) {
            return "OUT_ALL_SCO";
        }
        if (o == 67133440) {
            return "OUT_ALL_USB";
        }
        if (o == -2147483647) {
            return "IN_COMMUNICATION";
        }
        if (o == -2147483646) {
            return "IN_AMBIENT";
        }
        if (o == -2147483644) {
            return "IN_BUILTIN_MIC";
        }
        if (o == -2147483640) {
            return "IN_BLUETOOTH_SCO_HEADSET";
        }
        if (o == -2147483632) {
            return "IN_WIRED_HEADSET";
        }
        if (o == -2147483616) {
            return "IN_AUX_DIGITAL";
        }
        if (o == -2147483616) {
            return "IN_HDMI";
        }
        if (o == -2147483584) {
            return "IN_VOICE_CALL";
        }
        if (o == -2147483584) {
            return "IN_TELEPHONY_RX";
        }
        if (o == -2147483520) {
            return "IN_BACK_MIC";
        }
        if (o == -2147483392) {
            return "IN_REMOTE_SUBMIX";
        }
        if (o == -2147483136) {
            return "IN_ANLG_DOCK_HEADSET";
        }
        if (o == -2147482624) {
            return "IN_DGTL_DOCK_HEADSET";
        }
        if (o == -2147481600) {
            return "IN_USB_ACCESSORY";
        }
        if (o == -2147479552) {
            return "IN_USB_DEVICE";
        }
        if (o == -2147475456) {
            return "IN_FM_TUNER";
        }
        if (o == -2147467264) {
            return "IN_TV_TUNER";
        }
        if (o == -2147450880) {
            return "IN_LINE";
        }
        if (o == -2147418112) {
            return "IN_SPDIF";
        }
        if (o == -2147352576) {
            return "IN_BLUETOOTH_A2DP";
        }
        if (o == -2147221504) {
            return "IN_LOOPBACK";
        }
        if (o == -2146959360) {
            return "IN_IP";
        }
        if (o == -2146435072) {
            return "IN_BUS";
        }
        if (o == -2130706432) {
            return "IN_PROXY";
        }
        if (o == -2113929216) {
            return "IN_USB_HEADSET";
        }
        if (o == -1073741824) {
            return "IN_DEFAULT";
        }
        if (o == -1021313025) {
            return "IN_ALL";
        }
        if (o == -2147483640) {
            return "IN_ALL_SCO";
        }
        if (o == -2113923072) {
            return "IN_ALL_USB";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("NONE");
        if ((o & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
            list.add("BIT_IN");
            flipped = 0 | Integer.MIN_VALUE;
        }
        if ((o & 1073741824) == 1073741824) {
            list.add("BIT_DEFAULT");
            flipped |= 1073741824;
        }
        if ((o & 1) == 1) {
            list.add("OUT_EARPIECE");
            flipped |= 1;
        }
        if ((o & 2) == 2) {
            list.add("OUT_SPEAKER");
            flipped |= 2;
        }
        if ((o & 4) == 4) {
            list.add("OUT_WIRED_HEADSET");
            flipped |= 4;
        }
        if ((o & 8) == 8) {
            list.add("OUT_WIRED_HEADPHONE");
            flipped |= 8;
        }
        if ((o & 16) == 16) {
            list.add("OUT_BLUETOOTH_SCO");
            flipped |= 16;
        }
        if ((o & 32) == 32) {
            list.add("OUT_BLUETOOTH_SCO_HEADSET");
            flipped |= 32;
        }
        if ((o & 64) == 64) {
            list.add("OUT_BLUETOOTH_SCO_CARKIT");
            flipped |= 64;
        }
        if ((o & 128) == 128) {
            list.add("OUT_BLUETOOTH_A2DP");
            flipped |= 128;
        }
        if ((o & 256) == 256) {
            list.add("OUT_BLUETOOTH_A2DP_HEADPHONES");
            flipped |= 256;
        }
        if ((o & 512) == 512) {
            list.add("OUT_BLUETOOTH_A2DP_SPEAKER");
            flipped |= 512;
        }
        if ((o & 1024) == 1024) {
            list.add("OUT_AUX_DIGITAL");
            flipped |= 1024;
        }
        if ((o & 1024) == 1024) {
            list.add("OUT_HDMI");
            flipped |= 1024;
        }
        if ((o & 2048) == 2048) {
            list.add("OUT_ANLG_DOCK_HEADSET");
            flipped |= 2048;
        }
        if ((o & 4096) == 4096) {
            list.add("OUT_DGTL_DOCK_HEADSET");
            flipped |= 4096;
        }
        if ((o & 8192) == 8192) {
            list.add("OUT_USB_ACCESSORY");
            flipped |= 8192;
        }
        if ((o & 16384) == 16384) {
            list.add("OUT_USB_DEVICE");
            flipped |= 16384;
        }
        if ((o & 32768) == 32768) {
            list.add("OUT_REMOTE_SUBMIX");
            flipped |= 32768;
        }
        if ((o & 65536) == 65536) {
            list.add("OUT_TELEPHONY_TX");
            flipped |= 65536;
        }
        if ((131072 & o) == 131072) {
            list.add("OUT_LINE");
            flipped |= 131072;
        }
        if ((262144 & o) == 262144) {
            list.add("OUT_HDMI_ARC");
            flipped |= 262144;
        }
        if ((524288 & o) == 524288) {
            list.add("OUT_SPDIF");
            flipped |= 524288;
        }
        if ((1048576 & o) == 1048576) {
            list.add("OUT_FM");
            flipped |= 1048576;
        }
        if ((2097152 & o) == 2097152) {
            list.add("OUT_AUX_LINE");
            flipped |= 2097152;
        }
        if ((4194304 & o) == 4194304) {
            list.add("OUT_SPEAKER_SAFE");
            flipped |= 4194304;
        }
        if ((8388608 & o) == 8388608) {
            list.add("OUT_IP");
            flipped |= 8388608;
        }
        if ((16777216 & o) == 16777216) {
            list.add("OUT_BUS");
            flipped |= 16777216;
        }
        if ((33554432 & o) == 33554432) {
            list.add("OUT_PROXY");
            flipped |= 33554432;
        }
        if ((67108864 & o) == 67108864) {
            list.add("OUT_USB_HEADSET");
            flipped |= 67108864;
        }
        if ((o & 1073741824) == 1073741824) {
            list.add("OUT_DEFAULT");
            flipped |= 1073741824;
        }
        if ((1207959551 & o) == 1207959551) {
            list.add("OUT_ALL");
            flipped |= OUT_ALL;
        }
        if ((o & OUT_ALL_A2DP) == 896) {
            list.add("OUT_ALL_A2DP");
            flipped |= OUT_ALL_A2DP;
        }
        if ((o & 112) == 112) {
            list.add("OUT_ALL_SCO");
            flipped |= 112;
        }
        if ((67133440 & o) == 67133440) {
            list.add("OUT_ALL_USB");
            flipped |= OUT_ALL_USB;
        }
        if (((-2147483647) & o) == -2147483647) {
            list.add("IN_COMMUNICATION");
            flipped |= -2147483647;
        }
        if (((-2147483646) & o) == -2147483646) {
            list.add("IN_AMBIENT");
            flipped |= -2147483646;
        }
        if (((-2147483644) & o) == -2147483644) {
            list.add("IN_BUILTIN_MIC");
            flipped |= -2147483644;
        }
        if ((o & (-2147483640)) == -2147483640) {
            list.add("IN_BLUETOOTH_SCO_HEADSET");
            flipped |= -2147483640;
        }
        if (((-2147483632) & o) == -2147483632) {
            list.add("IN_WIRED_HEADSET");
            flipped |= IN_WIRED_HEADSET;
        }
        if ((o & (-2147483616)) == -2147483616) {
            list.add("IN_AUX_DIGITAL");
            flipped |= -2147483616;
        }
        if ((o & (-2147483616)) == -2147483616) {
            list.add("IN_HDMI");
            flipped |= -2147483616;
        }
        if ((o & (-2147483584)) == -2147483584) {
            list.add("IN_VOICE_CALL");
            flipped |= -2147483584;
        }
        if ((o & (-2147483584)) == -2147483584) {
            list.add("IN_TELEPHONY_RX");
            flipped |= -2147483584;
        }
        if (((-2147483520) & o) == -2147483520) {
            list.add("IN_BACK_MIC");
            flipped |= IN_BACK_MIC;
        }
        if (((-2147483392) & o) == -2147483392) {
            list.add("IN_REMOTE_SUBMIX");
            flipped |= IN_REMOTE_SUBMIX;
        }
        if (((-2147483136) & o) == -2147483136) {
            list.add("IN_ANLG_DOCK_HEADSET");
            flipped |= IN_ANLG_DOCK_HEADSET;
        }
        if (((-2147482624) & o) == -2147482624) {
            list.add("IN_DGTL_DOCK_HEADSET");
            flipped |= IN_DGTL_DOCK_HEADSET;
        }
        if (((-2147481600) & o) == -2147481600) {
            list.add("IN_USB_ACCESSORY");
            flipped |= IN_USB_ACCESSORY;
        }
        if (((-2147479552) & o) == -2147479552) {
            list.add("IN_USB_DEVICE");
            flipped |= IN_USB_DEVICE;
        }
        if (((-2147475456) & o) == -2147475456) {
            list.add("IN_FM_TUNER");
            flipped |= IN_FM_TUNER;
        }
        if (((-2147467264) & o) == -2147467264) {
            list.add("IN_TV_TUNER");
            flipped |= IN_TV_TUNER;
        }
        if (((-2147450880) & o) == -2147450880) {
            list.add("IN_LINE");
            flipped |= IN_LINE;
        }
        if (((-2147418112) & o) == -2147418112) {
            list.add("IN_SPDIF");
            flipped |= IN_SPDIF;
        }
        if (((-2147352576) & o) == -2147352576) {
            list.add("IN_BLUETOOTH_A2DP");
            flipped |= IN_BLUETOOTH_A2DP;
        }
        if (((-2147221504) & o) == -2147221504) {
            list.add("IN_LOOPBACK");
            flipped |= IN_LOOPBACK;
        }
        if (((-2146959360) & o) == -2146959360) {
            list.add("IN_IP");
            flipped |= IN_IP;
        }
        if (((-2146435072) & o) == -2146435072) {
            list.add("IN_BUS");
            flipped |= IN_BUS;
        }
        if (((-2130706432) & o) == -2130706432) {
            list.add("IN_PROXY");
            flipped |= IN_PROXY;
        }
        if (((-2113929216) & o) == -2113929216) {
            list.add("IN_USB_HEADSET");
            flipped |= IN_USB_HEADSET;
        }
        if (((-1073741824) & o) == -1073741824) {
            list.add("IN_DEFAULT");
            flipped |= -1073741824;
        }
        if (((-1021313025) & o) == -1021313025) {
            list.add("IN_ALL");
            flipped |= IN_ALL;
        }
        if ((o & (-2147483640)) == -2147483640) {
            list.add("IN_ALL_SCO");
            flipped |= -2147483640;
        }
        if (((-2113923072) & o) == -2113923072) {
            list.add("IN_ALL_USB");
            flipped |= IN_ALL_USB;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
