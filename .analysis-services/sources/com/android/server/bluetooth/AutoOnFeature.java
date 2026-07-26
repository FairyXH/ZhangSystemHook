package com.android.server.bluetooth;

/* JADX INFO: compiled from: AutoOnFeature.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000D\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002\u001a\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002\u001a\u000e\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u0015\u001a\u000e\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011\u001a\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015\u001a\u0006\u0010\u0019\u001a\u00020\u0018\u001a,\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00180 \u001a\u0018\u0010!\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\"\u001a\u00020\u000fH\u0002\u001a4\u0010#\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020\u000f2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00180 \"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u001c\u0010\u0002\u001a\u00020\u00018\u0000X\u0081D¢\u0006\u000e\n\u0000\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"&\u0010\u0007\u001a\u0004\u0018\u00010\b8\u0000@\u0000X\u0081\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\t\u0010\u0004\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r¨\u0006$"}, d2 = {"TAG", "", "USER_SETTINGS_KEY", "getUSER_SETTINGS_KEY$annotations", "()V", "getUSER_SETTINGS_KEY", "()Ljava/lang/String;", "timer", "Lcom/android/server/bluetooth/Timer;", "getTimer$annotations", "getTimer", "()Lcom/android/server/bluetooth/Timer;", "setTimer", "(Lcom/android/server/bluetooth/Timer;)V", "isFeatureEnabledForUser", "", "resolver", "Landroid/content/ContentResolver;", "isFeatureSupportedForUser", "isUserEnabled", "context", "Landroid/content/Context;", "isUserSupported", "notifyBluetoothOn", "", "pause", "resetAutoOnTimerForUser", "looper", "Landroid/os/Looper;", "state", "Lcom/android/server/bluetooth/BluetoothAdapterState;", "callback_on", "Lkotlin/Function0;", "setFeatureEnabledForUserUnchecked", "status", "setUserEnabled", "frameworks__base__services__android_common__services"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class AutoOnFeature {
    private static final java.lang.String TAG = "AutoOnFeature";
    private static final java.lang.String USER_SETTINGS_KEY = "bluetooth_automatic_turn_on";
    private static com.android.server.bluetooth.Timer timer;

    public static /* synthetic */ void getTimer$annotations() {
    }

    public static /* synthetic */ void getUSER_SETTINGS_KEY$annotations() {
    }

    public static final void resetAutoOnTimerForUser(final android.os.Looper looper, final android.content.Context context, final com.android.server.bluetooth.BluetoothAdapterState state, final kotlin.jvm.functions.Function0<kotlin.Unit> function0) {
        com.android.server.bluetooth.Timer timer2 = timer;
        if (timer2 != null) {
            timer2.cancel$frameworks__base__services__android_common__services();
        }
        timer = null;
        if (!isFeatureEnabledForUser(context.getContentResolver())) {
            com.android.server.bluetooth.Log.INSTANCE.d(TAG, "Not Enabled for current user: " + context.getUser());
        } else {
            if (state.oneOf(12)) {
                com.android.server.bluetooth.Log.INSTANCE.d(TAG, "Bluetooth already in " + state + ", no need for timer");
                return;
            }
            if (com.android.server.bluetooth.satellite.SatelliteModeListener.isOn()) {
                com.android.server.bluetooth.Log.INSTANCE.d(TAG, "Satellite prevent feature activation");
                return;
            }
            if (com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode()) {
                if (!com.android.server.bluetooth.airplane.AirplaneModeListener.hasUserToggledApm(context)) {
                    com.android.server.bluetooth.Log.INSTANCE.d(TAG, "Airplane prevent feature activation");
                    return;
                }
                com.android.server.bluetooth.Log.INSTANCE.d(TAG, "Airplane bypassed as airplane enhanced mode has been activated previously");
            }
            timer = com.android.server.bluetooth.Timer.INSTANCE.start(looper, context, new android.content.BroadcastReceiver() { // from class: com.android.server.bluetooth.AutoOnFeature$resetAutoOnTimerForUser$receiver$1
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context ctx, android.content.Intent intent) {
                    com.android.server.bluetooth.Log.INSTANCE.i("AutoOnFeature", "Received " + intent.getAction() + " that trigger a new alarm scheduling");
                    com.android.server.bluetooth.AutoOnFeature.pause();
                    com.android.server.bluetooth.AutoOnFeature.resetAutoOnTimerForUser(looper, context, state, function0);
                }
            }, function0);
        }
    }

    public static final void pause() {
        com.android.server.bluetooth.Timer timer2 = timer;
        if (timer2 != null) {
            timer2.pause$frameworks__base__services__android_common__services();
        }
        timer = null;
    }

    public static final void notifyBluetoothOn(android.content.Context context) {
        com.android.server.bluetooth.Timer timer2 = timer;
        if (timer2 != null) {
            timer2.cancel$frameworks__base__services__android_common__services();
        }
        timer = null;
        if (!isFeatureSupportedForUser(context.getContentResolver())) {
            if (!setFeatureEnabledForUserUnchecked(context, true)) {
                com.android.server.bluetooth.Log.INSTANCE.e(TAG, "Failed to set feature to its default value true");
                return;
            } else {
                com.android.server.bluetooth.Log.INSTANCE.i(TAG, "Feature was set to its default value true");
                return;
            }
        }
        com.android.server.bluetooth.Timer.INSTANCE.resetStorage(context.getContentResolver());
    }

    public static final boolean isUserSupported(android.content.ContentResolver resolver) {
        return isFeatureSupportedForUser(resolver);
    }

    public static final boolean isUserEnabled(android.content.Context context) {
        if (!isUserSupported(context.getContentResolver())) {
            throw new java.lang.IllegalStateException("AutoOnFeature not supported for user: " + context.getUser());
        }
        return isFeatureEnabledForUser(context.getContentResolver());
    }

    public static final void setUserEnabled(android.os.Looper looper, android.content.Context context, com.android.server.bluetooth.BluetoothAdapterState state, boolean status, kotlin.jvm.functions.Function0<kotlin.Unit> function0) {
        if (!isUserSupported(context.getContentResolver())) {
            throw new java.lang.IllegalStateException("AutoOnFeature not supported for user: " + context.getUser());
        }
        if (!setFeatureEnabledForUserUnchecked(context, status)) {
            throw new java.lang.IllegalStateException("AutoOnFeature database failure for user: " + context.getUser());
        }
        com.android.modules.expresslog.Counter.logIncrement(status ? "bluetooth.value_auto_on_enabled" : "bluetooth.value_auto_on_disabled");
        com.android.server.bluetooth.Timer.INSTANCE.resetStorage(context.getContentResolver());
        resetAutoOnTimerForUser(looper, context, state, function0);
    }

    public static final com.android.server.bluetooth.Timer getTimer() {
        return timer;
    }

    public static final void setTimer(com.android.server.bluetooth.Timer timer2) {
        timer = timer2;
    }

    public static final java.lang.String getUSER_SETTINGS_KEY() {
        return USER_SETTINGS_KEY;
    }

    private static final boolean isFeatureEnabledForUser(android.content.ContentResolver resolver) {
        return android.provider.Settings.Secure.getInt(resolver, USER_SETTINGS_KEY, 0) == 1;
    }

    private static final boolean isFeatureSupportedForUser(android.content.ContentResolver resolver) {
        return android.provider.Settings.Secure.getInt(resolver, USER_SETTINGS_KEY, -1) != -1;
    }

    private static final boolean setFeatureEnabledForUserUnchecked(android.content.Context context, boolean z) {
        boolean zPutInt = android.provider.Settings.Secure.putInt(context.getContentResolver(), USER_SETTINGS_KEY, z ? 1 : 0);
        if (zPutInt) {
            context.sendBroadcast(new android.content.Intent("android.bluetooth.action.AUTO_ON_STATE_CHANGED").addFlags(1073741824).putExtra("android.bluetooth.extra.AUTO_ON_STATE", z ? 2 : 1), "android.permission.BLUETOOTH_PRIVILEGED", android.app.BroadcastOptions.makeBasic().setDeferralPolicy(2).toBundle());
        }
        return zPutInt;
    }
}
