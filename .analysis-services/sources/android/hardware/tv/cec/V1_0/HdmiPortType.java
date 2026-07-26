package android.hardware.tv.cec.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class HdmiPortType {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "INPUT";
        }
        if (o == 1) {
            return "OUTPUT";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("INPUT");
        if ((o & 1) == 1) {
            list.add("OUTPUT");
            flipped = 0 | 1;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
