package com.android.server.broadcastradio.aidl;

/* JADX INFO: loaded from: classes.dex */
final class Utils {

    public enum FrequencyBand {
        UNKNOWN,
        FM,
        AM_LW,
        AM_MW,
        AM_SW
    }

    private Utils() {
        throw new java.lang.UnsupportedOperationException("Utils class is noninstantiable");
    }

    static com.android.server.broadcastradio.aidl.Utils.FrequencyBand getBand(int freq) {
        return freq < 30 ? com.android.server.broadcastradio.aidl.Utils.FrequencyBand.UNKNOWN : freq < 500 ? com.android.server.broadcastradio.aidl.Utils.FrequencyBand.AM_LW : freq < 1705 ? com.android.server.broadcastradio.aidl.Utils.FrequencyBand.AM_MW : freq < 30000 ? com.android.server.broadcastradio.aidl.Utils.FrequencyBand.AM_SW : freq < 60000 ? com.android.server.broadcastradio.aidl.Utils.FrequencyBand.UNKNOWN : freq < 110000 ? com.android.server.broadcastradio.aidl.Utils.FrequencyBand.FM : com.android.server.broadcastradio.aidl.Utils.FrequencyBand.UNKNOWN;
    }
}
