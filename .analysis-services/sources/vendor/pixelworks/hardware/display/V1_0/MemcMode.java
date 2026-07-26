package vendor.pixelworks.hardware.display.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class MemcMode {
    public static final int BYPASS = 21;
    public static final int BYPASS2PT = 22;
    public static final int DSI_SWITCH_2PT = 24;
    public static final int DSI_SWITCH_2RFB = 25;
    public static final int FRC = 3;
    public static final int FRC2RFB = 11;
    public static final int FRC_CANCEL = 4;
    public static final int FRC_POST = 26;
    public static final int FRC_PREPARE = 1;
    public static final int FRC_PREPARE_DONE = 2;
    public static final int FRC_PREPARE_RFB = 5;
    public static final int FRC_PREPARE_TIMEOUT = 6;
    public static final int KICKOFF60_DISABLE = 19;
    public static final int KICKOFF60_ENABLE = 18;
    public static final int PT = 17;
    public static final int PT2BYPASS = 20;
    public static final int PT2RFB = 16;
    public static final int PTLOW_PREPARE = 23;
    public static final int PT_PREPARE = 12;
    public static final int PT_PREPARE_DONE = 13;
    public static final int PT_PREPARE_TIMEOUT = 14;
    public static final int RFB = 0;
    public static final int RFB2FRC = 7;
    public static final int RFB2PT = 15;
    public static final int RFB_POST = 28;
    public static final int RFB_PREPARE = 8;
    public static final int RFB_PREPARE_DELAY = 27;
    public static final int RFB_PREPARE_DONE = 9;
    public static final int RFB_PREPARE_TIMEOUT = 10;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "RFB";
        }
        if (o == 1) {
            return "FRC_PREPARE";
        }
        if (o == 2) {
            return "FRC_PREPARE_DONE";
        }
        if (o == 3) {
            return "FRC";
        }
        if (o == 4) {
            return "FRC_CANCEL";
        }
        if (o == 5) {
            return "FRC_PREPARE_RFB";
        }
        if (o == 6) {
            return "FRC_PREPARE_TIMEOUT";
        }
        if (o == 7) {
            return "RFB2FRC";
        }
        if (o == 8) {
            return "RFB_PREPARE";
        }
        if (o == 9) {
            return "RFB_PREPARE_DONE";
        }
        if (o == 10) {
            return "RFB_PREPARE_TIMEOUT";
        }
        if (o == 11) {
            return "FRC2RFB";
        }
        if (o == 12) {
            return "PT_PREPARE";
        }
        if (o == 13) {
            return "PT_PREPARE_DONE";
        }
        if (o == 14) {
            return "PT_PREPARE_TIMEOUT";
        }
        if (o == 15) {
            return "RFB2PT";
        }
        if (o == 16) {
            return "PT2RFB";
        }
        if (o == 17) {
            return "PT";
        }
        if (o == 18) {
            return "KICKOFF60_ENABLE";
        }
        if (o == 19) {
            return "KICKOFF60_DISABLE";
        }
        if (o == 20) {
            return "PT2BYPASS";
        }
        if (o == 21) {
            return "BYPASS";
        }
        if (o == 22) {
            return "BYPASS2PT";
        }
        if (o == 23) {
            return "PTLOW_PREPARE";
        }
        if (o == 24) {
            return "DSI_SWITCH_2PT";
        }
        if (o == 25) {
            return "DSI_SWITCH_2RFB";
        }
        if (o == 26) {
            return "FRC_POST";
        }
        if (o == 27) {
            return "RFB_PREPARE_DELAY";
        }
        if (o == 28) {
            return "RFB_POST";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("RFB");
        if ((o & 1) == 1) {
            list.add("FRC_PREPARE");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("FRC_PREPARE_DONE");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("FRC");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("FRC_CANCEL");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("FRC_PREPARE_RFB");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("FRC_PREPARE_TIMEOUT");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("RFB2FRC");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("RFB_PREPARE");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("RFB_PREPARE_DONE");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("RFB_PREPARE_TIMEOUT");
            flipped |= 10;
        }
        if ((o & 11) == 11) {
            list.add("FRC2RFB");
            flipped |= 11;
        }
        if ((o & 12) == 12) {
            list.add("PT_PREPARE");
            flipped |= 12;
        }
        if ((o & 13) == 13) {
            list.add("PT_PREPARE_DONE");
            flipped |= 13;
        }
        if ((o & 14) == 14) {
            list.add("PT_PREPARE_TIMEOUT");
            flipped |= 14;
        }
        if ((o & 15) == 15) {
            list.add("RFB2PT");
            flipped |= 15;
        }
        if ((o & 16) == 16) {
            list.add("PT2RFB");
            flipped |= 16;
        }
        if ((o & 17) == 17) {
            list.add("PT");
            flipped |= 17;
        }
        if ((o & 18) == 18) {
            list.add("KICKOFF60_ENABLE");
            flipped |= 18;
        }
        if ((o & 19) == 19) {
            list.add("KICKOFF60_DISABLE");
            flipped |= 19;
        }
        if ((o & 20) == 20) {
            list.add("PT2BYPASS");
            flipped |= 20;
        }
        if ((o & 21) == 21) {
            list.add("BYPASS");
            flipped |= 21;
        }
        if ((o & 22) == 22) {
            list.add("BYPASS2PT");
            flipped |= 22;
        }
        if ((o & 23) == 23) {
            list.add("PTLOW_PREPARE");
            flipped |= 23;
        }
        if ((o & 24) == 24) {
            list.add("DSI_SWITCH_2PT");
            flipped |= 24;
        }
        if ((o & 25) == 25) {
            list.add("DSI_SWITCH_2RFB");
            flipped |= 25;
        }
        if ((o & 26) == 26) {
            list.add("FRC_POST");
            flipped |= 26;
        }
        if ((o & 27) == 27) {
            list.add("RFB_PREPARE_DELAY");
            flipped |= 27;
        }
        if ((o & 28) == 28) {
            list.add("RFB_POST");
            flipped |= 28;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
