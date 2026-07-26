package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioOutputFlag {
    public static final int COMPRESS_OFFLOAD = 16;
    public static final int DEEP_BUFFER = 8;
    public static final int DIRECT = 1;
    public static final int DIRECT_PCM = 8192;
    public static final int FAST = 4;
    public static final int HW_AV_SYNC = 64;
    public static final int IEC958_NONAUDIO = 1024;
    public static final int MMAP_NOIRQ = 16384;
    public static final int NONE = 0;
    public static final int NON_BLOCKING = 32;
    public static final int PRIMARY = 2;
    public static final int RAW = 256;
    public static final int SYNC = 512;
    public static final int TTS = 128;
    public static final int VOIP_RX = 32768;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "NONE";
        }
        if (o == 1) {
            return "DIRECT";
        }
        if (o == 2) {
            return "PRIMARY";
        }
        if (o == 4) {
            return "FAST";
        }
        if (o == 8) {
            return "DEEP_BUFFER";
        }
        if (o == 16) {
            return "COMPRESS_OFFLOAD";
        }
        if (o == 32) {
            return "NON_BLOCKING";
        }
        if (o == 64) {
            return "HW_AV_SYNC";
        }
        if (o == 128) {
            return "TTS";
        }
        if (o == 256) {
            return "RAW";
        }
        if (o == 512) {
            return "SYNC";
        }
        if (o == 1024) {
            return "IEC958_NONAUDIO";
        }
        if (o == 8192) {
            return "DIRECT_PCM";
        }
        if (o == 16384) {
            return "MMAP_NOIRQ";
        }
        if (o == 32768) {
            return "VOIP_RX";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("NONE");
        if ((o & 1) == 1) {
            list.add("DIRECT");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("PRIMARY");
            flipped |= 2;
        }
        if ((o & 4) == 4) {
            list.add("FAST");
            flipped |= 4;
        }
        if ((o & 8) == 8) {
            list.add("DEEP_BUFFER");
            flipped |= 8;
        }
        if ((o & 16) == 16) {
            list.add("COMPRESS_OFFLOAD");
            flipped |= 16;
        }
        if ((o & 32) == 32) {
            list.add("NON_BLOCKING");
            flipped |= 32;
        }
        if ((o & 64) == 64) {
            list.add("HW_AV_SYNC");
            flipped |= 64;
        }
        if ((o & 128) == 128) {
            list.add("TTS");
            flipped |= 128;
        }
        if ((o & 256) == 256) {
            list.add("RAW");
            flipped |= 256;
        }
        if ((o & 512) == 512) {
            list.add("SYNC");
            flipped |= 512;
        }
        if ((o & 1024) == 1024) {
            list.add("IEC958_NONAUDIO");
            flipped |= 1024;
        }
        if ((o & 8192) == 8192) {
            list.add("DIRECT_PCM");
            flipped |= 8192;
        }
        if ((o & 16384) == 16384) {
            list.add("MMAP_NOIRQ");
            flipped |= 16384;
        }
        if ((o & 32768) == 32768) {
            list.add("VOIP_RX");
            flipped |= 32768;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
