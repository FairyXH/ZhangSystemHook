package com.android.server.bluetooth.airplane;

/* JADX INFO: compiled from: ModeListener.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000V\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\b\u001ab\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\f2\b\u0010\u0014\u001a\u0004\u0018\u00010\f2#\u0010\u0015\u001a\u001f\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0017\u0012\b\b\u0018\u0012\u0004\b\b(\u0019\u0012\u0004\u0012\u00020\u001a\u0018\u00010\u00162\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c2\u0006\u0010\u001e\u001a\u00020\fH\u0002¢\u0006\u0002\u0010\u001f\u001a\u000e\u0010 \u001a\u00020\f2\u0006\u0010!\u001a\u00020\u001d\u001a\u008a\u0001\u0010\"\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020&2!\u0010'\u001a\u001d\u0012\u0013\u0012\u00110\f¢\u0006\f\b\u0017\u0012\b\b\u0018\u0012\u0004\b\b((\u0012\u0004\u0012\u00020\u001a0\u00162!\u0010)\u001a\u001d\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0017\u0012\b\b\u0018\u0012\u0004\b\b(\u0019\u0012\u0004\u0012\u00020\u001a0\u00162\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\f0\u001c2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c2\u0006\u0010,\u001a\u00020-H\u0007\u001a\u0010\u0010.\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0002\u001a\u0016\u0010/\u001a\u00020\f2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cH\u0002\u001a\u001e\u00100\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cH\u0002\u001a \u00101\u001a\u00020\u001a2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010!\u001a\u00020\u001d2\u0006\u00102\u001a\u00020\fH\u0007\u001a \u00103\u001a\u00020\f2\u0006\u0010!\u001a\u00020\u001d2\u0006\u0010\u0018\u001a\u00020\u00012\u0006\u00104\u001a\u00020\bH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u001e\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u001e\u0010\u000f\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000e¨\u00065"}, d2 = {"APM_BT_ENABLED_NOTIFICATION", "", "APM_BT_NOTIFICATION", "APM_ENHANCEMENT", "APM_USER_TOGGLED_BLUETOOTH", "APM_WIFI_BT_NOTIFICATION", "BLUETOOTH_APM_STATE", "DEFAULT_APM_ENHANCEMENT_STATE", "", "TAG", "WIFI_APM_STATE", "<set-?>", "", "isOn", "()Z", "isOnOverrode", "airplaneModeValueOverride", "resolver", "Landroid/content/ContentResolver;", "currentAirplaneMode", "currentBluetoothStatus", "sendAirplaneModeNotification", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "state", "", "getUser", "Lkotlin/Function0;", "Landroid/content/Context;", "isMediaConnected", "(Landroid/content/ContentResolver;ZLjava/lang/Boolean;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Z)Z", "hasUserToggledApm", "userContext", "initialize", "looper", "Landroid/os/Looper;", "systemResolver", "Lcom/android/server/bluetooth/BluetoothAdapterState;", "modeCallback", "m", "notificationCallback", "mediaCallback", "userCallback", "timeSource", "Lkotlin/time/TimeSource;", "isApmEnhancementEnabled", "isBluetoothOnAPM", "isWifiOnApm", "notifyUserToggledBluetooth", "isBluetoothOn", "setUserSettingsSecure", "value", "frameworks__base__services__android_common__services"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class AirplaneModeListener {
    public static final java.lang.String APM_BT_ENABLED_NOTIFICATION = "apm_bt_enabled_notification";
    public static final java.lang.String APM_BT_NOTIFICATION = "apm_bt_notification";
    public static final java.lang.String APM_ENHANCEMENT = "apm_enhancement_enabled";
    public static final java.lang.String APM_USER_TOGGLED_BLUETOOTH = "apm_user_toggled_bluetooth";
    public static final java.lang.String APM_WIFI_BT_NOTIFICATION = "apm_wifi_bt_notification";
    public static final java.lang.String BLUETOOTH_APM_STATE = "bluetooth_apm_state";
    private static final int DEFAULT_APM_ENHANCEMENT_STATE = 1;
    private static final java.lang.String TAG = "AirplaneModeListener";
    public static final java.lang.String WIFI_APM_STATE = "wifi_apm_state";
    private static boolean isOn;
    private static boolean isOnOverrode;

    public static final boolean isOnOverrode() {
        return isOnOverrode;
    }

    public static final boolean isOn() {
        return isOn;
    }

    public static final void initialize(android.os.Looper looper, final android.content.ContentResolver systemResolver, final com.android.server.bluetooth.BluetoothAdapterState state, final kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> function1, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> function12, final kotlin.jvm.functions.Function0<java.lang.Boolean> function0, final kotlin.jvm.functions.Function0<? extends android.content.Context> function02, final kotlin.time.TimeSource timeSource) {
        android.provider.Settings.Global.putInt(systemResolver, APM_ENHANCEMENT, android.provider.Settings.Global.getInt(systemResolver, APM_ENHANCEMENT, 1));
        boolean airplaneModeAtBoot = com.android.server.bluetooth.RadioModeListenerKt.initializeRadioModeListener(looper, systemResolver, "airplane_mode_radios", "airplane_mode_on", new kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit>() { // from class: com.android.server.bluetooth.airplane.AirplaneModeListener$initialize$airplaneModeAtBoot$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Boolean bool) {
                invoke(bool.booleanValue());
                return kotlin.Unit.INSTANCE;
            }

            public final void invoke(boolean newMode) {
                com.android.server.bluetooth.airplane.AirplaneModeListener.isOn = newMode;
                boolean previousMode = com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode();
                boolean isBluetoothOn = state.oneOf(12, 11, 13);
                boolean isMediaConnected = isBluetoothOn && function0.invoke().booleanValue();
                boolean z = isMediaConnected;
                com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode = com.android.server.bluetooth.airplane.AirplaneModeListener.airplaneModeValueOverride(systemResolver, newMode, java.lang.Boolean.valueOf(isBluetoothOn), function12, function02, z);
                com.android.server.bluetooth.airplane.AirplaneMetricSession.Companion.handleModeChange(newMode, isBluetoothOn, function12, function02, z, timeSource.markNow());
                java.lang.String description = "previousMode=" + previousMode + ", isOn=" + com.android.server.bluetooth.airplane.AirplaneModeListener.isOn() + ", isOnOverrode=" + com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode() + ", isMediaConnected=" + isMediaConnected;
                if (previousMode == com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode()) {
                    com.android.server.bluetooth.Log.INSTANCE.d("AirplaneModeListener", "Ignore mode change to same state. " + description);
                    return;
                }
                if (com.android.bluetooth.flags.Flags.airplaneModeXBleOn() && !com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode() && state.oneOf(12)) {
                    com.android.server.bluetooth.Log.INSTANCE.d("AirplaneModeListener", "Ignore mode change as Bluetooth is ON. " + description);
                } else {
                    com.android.server.bluetooth.Log.INSTANCE.i("AirplaneModeListener", "Trigger callback. " + description);
                    function1.invoke(java.lang.Boolean.valueOf(com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode()));
                }
            }
        });
        isOn = airplaneModeAtBoot;
        isOnOverrode = airplaneModeValueOverride(systemResolver, airplaneModeAtBoot, null, null, function02, false);
        com.android.server.bluetooth.airplane.AirplaneMetricSession.Companion.handleModeChange(airplaneModeAtBoot, false, function12, function02, false, timeSource.markNow());
        com.android.server.bluetooth.Log.INSTANCE.i(TAG, "Init completed. isOn=" + isOn + ", isOnOverrode=" + isOnOverrode);
    }

    public static final void notifyUserToggledBluetooth(android.content.ContentResolver resolver, android.content.Context userContext, boolean isBluetoothOn) {
        com.android.server.bluetooth.airplane.AirplaneMetricSession.Companion.notifyUserToggledBluetooth(resolver, userContext, isBluetoothOn);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean airplaneModeValueOverride(android.content.ContentResolver resolver, boolean currentAirplaneMode, java.lang.Boolean currentBluetoothStatus, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> function1, kotlin.jvm.functions.Function0<? extends android.content.Context> function0, boolean isMediaConnected) {
        if (!currentAirplaneMode || kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) currentBluetoothStatus, (java.lang.Object) false)) {
            return currentAirplaneMode;
        }
        if (isApmEnhancementEnabled(resolver) && hasUserToggledApm(function0.invoke())) {
            if (isBluetoothOnAPM(function0)) {
                boolean isWifiOn = isWifiOnApm(resolver, function0);
                if (function1 != null) {
                    function1.invoke(isWifiOn ? APM_WIFI_BT_NOTIFICATION : APM_BT_NOTIFICATION);
                }
                com.android.server.bluetooth.Log.INSTANCE.i(TAG, "Enhancement Mode: override and stays ON");
                return false;
            }
            com.android.server.bluetooth.Log.INSTANCE.i(TAG, "Enhancement Mode: override and turns OFF");
            return true;
        }
        if (isMediaConnected) {
            com.android.server.bluetooth.Log.INSTANCE.i(TAG, "Legacy Mode: override and stays ON since media profile are connected");
            com.android.server.bluetooth.airplane.ToastNotification.INSTANCE.displayIfNeeded(resolver, function0);
            return false;
        }
        com.android.server.bluetooth.Log.INSTANCE.i(TAG, "Legacy Mode: no override, turns OFF");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean setUserSettingsSecure(android.content.Context userContext, java.lang.String name, int value) {
        return android.provider.Settings.Secure.putInt(userContext.getContentResolver(), name, value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean isApmEnhancementEnabled(android.content.ContentResolver resolver) {
        return android.provider.Settings.Global.getInt(resolver, APM_ENHANCEMENT, 1) == 1;
    }

    private static final boolean isWifiOnApm(android.content.ContentResolver resolver, kotlin.jvm.functions.Function0<? extends android.content.Context> function0) {
        return android.provider.Settings.Global.getInt(resolver, "wifi_on", 0) != 0 && android.provider.Settings.Secure.getInt(function0.invoke().getContentResolver(), WIFI_APM_STATE, 0) == 1;
    }

    public static final boolean hasUserToggledApm(android.content.Context userContext) {
        return android.provider.Settings.Secure.getInt(userContext.getContentResolver(), APM_USER_TOGGLED_BLUETOOTH, 0) == 1;
    }

    private static final boolean isBluetoothOnAPM(kotlin.jvm.functions.Function0<? extends android.content.Context> function0) {
        return android.provider.Settings.Secure.getInt(function0.invoke().getContentResolver(), BLUETOOTH_APM_STATE, 0) == 1;
    }
}
