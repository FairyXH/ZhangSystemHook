package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioHandleConsts {
    public static final int AUDIO_IO_HANDLE_NONE = 0;
    public static final int AUDIO_MODULE_HANDLE_NONE = 0;
    public static final int AUDIO_PATCH_HANDLE_NONE = 0;
    public static final int AUDIO_PORT_HANDLE_NONE = 0;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "AUDIO_IO_HANDLE_NONE";
        }
        if (o == 0) {
            return "AUDIO_MODULE_HANDLE_NONE";
        }
        if (o == 0) {
            return "AUDIO_PORT_HANDLE_NONE";
        }
        if (o == 0) {
            return "AUDIO_PATCH_HANDLE_NONE";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        list.add("AUDIO_IO_HANDLE_NONE");
        list.add("AUDIO_MODULE_HANDLE_NONE");
        list.add("AUDIO_PORT_HANDLE_NONE");
        list.add("AUDIO_PATCH_HANDLE_NONE");
        if (o != 0) {
            list.add("0x" + java.lang.Integer.toHexString((~0) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
