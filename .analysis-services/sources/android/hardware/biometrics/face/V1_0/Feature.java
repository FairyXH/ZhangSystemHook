package android.hardware.biometrics.face.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class Feature {
    public static final int REQUIRE_ATTENTION = 1;
    public static final int REQUIRE_DIVERSITY = 2;

    public static final java.lang.String toString(int o) {
        if (o == 1) {
            return "REQUIRE_ATTENTION";
        }
        if (o == 2) {
            return "REQUIRE_DIVERSITY";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 1) == 1) {
            list.add("REQUIRE_ATTENTION");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("REQUIRE_DIVERSITY");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
