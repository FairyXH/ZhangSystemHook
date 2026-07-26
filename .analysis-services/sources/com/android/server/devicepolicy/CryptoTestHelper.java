package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class CryptoTestHelper {
    private static native int runSelfTest();

    public static void runAndLogSelfTest() {
        int result = runSelfTest();
        android.app.admin.SecurityLog.writeEvent(210031, new java.lang.Object[]{java.lang.Integer.valueOf(result)});
    }
}
