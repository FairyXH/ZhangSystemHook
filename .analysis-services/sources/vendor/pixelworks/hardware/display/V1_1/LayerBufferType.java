package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class LayerBufferType {
    public static final int BUFFER_TYPE_HDR_VIDEO = 7;
    public static final int BUFFER_TYPE_HW_VIDEO = 3;
    public static final int BUFFER_TYPE_UI = 0;
    public static final int BUFFER_TYPE_VIDEO = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "BUFFER_TYPE_UI";
        }
        if (o == 1) {
            return "BUFFER_TYPE_VIDEO";
        }
        if (o == 3) {
            return "BUFFER_TYPE_HW_VIDEO";
        }
        if (o == 7) {
            return "BUFFER_TYPE_HDR_VIDEO";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("BUFFER_TYPE_UI");
        if ((o & 1) == 1) {
            list.add("BUFFER_TYPE_VIDEO");
            flipped = 0 | 1;
        }
        if ((o & 3) == 3) {
            list.add("BUFFER_TYPE_HW_VIDEO");
            flipped |= 3;
        }
        if ((o & 7) == 7) {
            list.add("BUFFER_TYPE_HDR_VIDEO");
            flipped |= 7;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
