package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class EmergencyNumberDbInstallReceiver extends com.android.server.updates.ConfigUpdateInstallReceiver {
    private static final java.lang.String TAG = "EmergencyNumberDbInstallReceiver";

    public EmergencyNumberDbInstallReceiver() {
        super("/data/misc/emergencynumberdb", "emergency_number_db", "metadata/", "version");
    }

    @Override // com.android.server.updates.ConfigUpdateInstallReceiver
    protected void postInstall(android.content.Context context, android.content.Intent intent) {
        android.util.Slog.i(TAG, "Emergency number database is updated in file partition");
        android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) context.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
        telephonyManager.notifyOtaEmergencyNumberDbInstalled();
    }
}
