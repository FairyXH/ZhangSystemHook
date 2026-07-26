package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class LayerCompositionType {
    public static final int COMPOSITION_TYPE_GPU = 0;
    public static final int COMPOSITION_TYPE_HWC = 2;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "COMPOSITION_TYPE_GPU";
        }
        if (o == 2) {
            return "COMPOSITION_TYPE_HWC";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("COMPOSITION_TYPE_GPU");
        if ((o & 2) == 2) {
            list.add("COMPOSITION_TYPE_HWC");
            flipped = 0 | 2;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
