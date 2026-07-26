package com.android.server.broadcastradio.hal2;

/* JADX INFO: loaded from: classes.dex */
class Utils {
    private static final java.lang.String TAG = "BcRadio2Srv.utils";

    interface FuncThrowingRemoteException<T> {
        T exec() throws android.os.RemoteException;
    }

    interface VoidFuncThrowingRemoteException {
        void exec() throws android.os.RemoteException;
    }

    private Utils() {
        throw new java.lang.UnsupportedOperationException("Utils class is noninstantiable");
    }

    static com.android.server.broadcastradio.hal2.FrequencyBand getBand(int freq) {
        return freq < 30 ? com.android.server.broadcastradio.hal2.FrequencyBand.UNKNOWN : freq < 500 ? com.android.server.broadcastradio.hal2.FrequencyBand.AM_LW : freq < 1705 ? com.android.server.broadcastradio.hal2.FrequencyBand.AM_MW : freq < 30000 ? com.android.server.broadcastradio.hal2.FrequencyBand.AM_SW : freq < 60000 ? com.android.server.broadcastradio.hal2.FrequencyBand.UNKNOWN : freq < 110000 ? com.android.server.broadcastradio.hal2.FrequencyBand.FM : com.android.server.broadcastradio.hal2.FrequencyBand.UNKNOWN;
    }

    static <T> T maybeRethrow(com.android.server.broadcastradio.hal2.Utils.FuncThrowingRemoteException<T> r) {
        try {
            return r.exec();
        } catch (android.os.RemoteException ex) {
            ex.rethrowFromSystemServer();
            return null;
        }
    }

    static void maybeRethrow(com.android.server.broadcastradio.hal2.Utils.VoidFuncThrowingRemoteException r) {
        try {
            r.exec();
        } catch (android.os.RemoteException ex) {
            ex.rethrowFromSystemServer();
        }
    }
}
