package com.android.server.bluetooth.satellite;

/* JADX INFO: compiled from: ModeListener.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a9\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2!\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b\u0010\u0012\b\b\u0011\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\t0\u000f\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u001e\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0013"}, d2 = {"SETTINGS_SATELLITE_MODE_ENABLED", "", "SETTINGS_SATELLITE_MODE_RADIOS", "TAG", "<set-?>", "", "isOn", "()Z", "initialize", "", "looper", "Landroid/os/Looper;", "resolver", "Landroid/content/ContentResolver;", "callback", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "m", "frameworks__base__services__android_common__services"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SatelliteModeListener {
    public static final java.lang.String SETTINGS_SATELLITE_MODE_ENABLED = "satellite_mode_enabled";
    public static final java.lang.String SETTINGS_SATELLITE_MODE_RADIOS = "satellite_mode_radios";
    private static final java.lang.String TAG = "SatelliteModeListener";
    private static boolean isOn;

    public static final boolean isOn() {
        return isOn;
    }

    public static final void initialize(android.os.Looper looper, android.content.ContentResolver resolver, final kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> function1) {
        isOn = com.android.server.bluetooth.RadioModeListenerKt.initializeRadioModeListener(looper, resolver, SETTINGS_SATELLITE_MODE_RADIOS, SETTINGS_SATELLITE_MODE_ENABLED, new kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit>() { // from class: com.android.server.bluetooth.satellite.SatelliteModeListener.initialize.1
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
                boolean previousMode = com.android.server.bluetooth.satellite.SatelliteModeListener.isOn();
                com.android.server.bluetooth.satellite.SatelliteModeListener.isOn = newMode;
                if (previousMode == com.android.server.bluetooth.satellite.SatelliteModeListener.isOn()) {
                    com.android.server.bluetooth.Log.INSTANCE.d(com.android.server.bluetooth.satellite.SatelliteModeListener.TAG, "Ignore satellite mode change because is already: " + com.android.server.bluetooth.satellite.SatelliteModeListener.isOn());
                } else {
                    com.android.server.bluetooth.Log.INSTANCE.i(com.android.server.bluetooth.satellite.SatelliteModeListener.TAG, "Trigger callback with state: " + com.android.server.bluetooth.satellite.SatelliteModeListener.isOn());
                    function1.invoke(java.lang.Boolean.valueOf(com.android.server.bluetooth.satellite.SatelliteModeListener.isOn()));
                }
            }
        });
        com.android.server.bluetooth.Log.INSTANCE.i(TAG, "Initialized successfully with state: " + isOn);
    }
}
