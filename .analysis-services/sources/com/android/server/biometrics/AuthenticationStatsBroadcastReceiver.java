package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class AuthenticationStatsBroadcastReceiver extends android.content.BroadcastReceiver {
    private static final java.lang.String TAG = "AuthenticationStatsBroadcastReceiver";
    private final java.util.function.Consumer<com.android.server.biometrics.AuthenticationStatsCollector> mCollectorConsumer;
    private final int mModality;

    public AuthenticationStatsBroadcastReceiver(android.content.Context context, int modality, java.util.function.Consumer<com.android.server.biometrics.AuthenticationStatsCollector> callback) {
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        context.registerReceiver(this, intentFilter);
        this.mCollectorConsumer = callback;
        this.mModality = modality;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
        if (userId != -10000 && "android.intent.action.USER_UNLOCKED".equals(intent.getAction())) {
            android.util.Slog.d(TAG, "Received: " + intent.getAction());
            this.mCollectorConsumer.accept(new com.android.server.biometrics.AuthenticationStatsCollector(context, this.mModality, new com.android.server.biometrics.sensors.BiometricNotificationImpl()));
            context.unregisterReceiver(this);
        }
    }
}
