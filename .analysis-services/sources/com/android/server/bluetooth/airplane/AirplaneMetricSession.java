package com.android.server.bluetooth.airplane;

/* JADX INFO: compiled from: ModeListener.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB@\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012!\u0010\u0004\u001a\u001d\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0004\u0012\u00020\n0\u0005\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u001e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0003J\u001c\u0010\u0018\u001a\u00020\n2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00160\u001a2\u0006\u0010\u0017\u001a\u00020\u0003R\u000e\u0010\u000f\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R)\u0010\u0004\u001a\u001d\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0004\u0012\u00020\n0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lcom/android/server/bluetooth/airplane/AirplaneMetricSession;", "", "isBluetoothOnBeforeApmToggle", "", "sendAirplaneModeNotification", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "state", "", "isMediaProfileConnectedBeforeApmToggle", "sessionStartTime", "Lkotlin/time/TimeMark;", "(ZLkotlin/jvm/functions/Function1;ZLkotlin/time/TimeMark;)V", "isBluetoothOnAfterApmToggle", "userToggledBluetoothDuringApm", "userToggledBluetoothDuringApmWithinMinute", "notifyUserToggledBluetooth", "resolver", "Landroid/content/ContentResolver;", "userContext", "Landroid/content/Context;", "isBluetoothOn", "terminate", "getUser", "Lkotlin/Function0;", "Companion", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class AirplaneMetricSession {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.android.server.bluetooth.airplane.AirplaneMetricSession.Companion INSTANCE = new com.android.server.bluetooth.airplane.AirplaneMetricSession.Companion(null);
    private static com.android.server.bluetooth.airplane.AirplaneMetricSession session;
    private final boolean isBluetoothOnAfterApmToggle = !com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode();
    private final boolean isBluetoothOnBeforeApmToggle;
    private final boolean isMediaProfileConnectedBeforeApmToggle;
    private final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> sendAirplaneModeNotification;
    private final kotlin.time.TimeMark sessionStartTime;
    private boolean userToggledBluetoothDuringApm;
    private boolean userToggledBluetoothDuringApmWithinMinute;

    /* JADX WARN: Multi-variable type inference failed */
    public AirplaneMetricSession(boolean isBluetoothOnBeforeApmToggle, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> function1, boolean isMediaProfileConnectedBeforeApmToggle, kotlin.time.TimeMark sessionStartTime) {
        this.isBluetoothOnBeforeApmToggle = isBluetoothOnBeforeApmToggle;
        this.sendAirplaneModeNotification = function1;
        this.isMediaProfileConnectedBeforeApmToggle = isMediaProfileConnectedBeforeApmToggle;
        this.sessionStartTime = sessionStartTime;
    }

    /* JADX INFO: compiled from: ModeListener.kt */
    @kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002JW\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2!\u0010\n\u001a\u001d\u0012\u0013\u0012\u00110\f¢\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00060\u000b2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015J\u001e\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00122\u0006\u0010\t\u001a\u00020\bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/android/server/bluetooth/airplane/AirplaneMetricSession$Companion;", "", "()V", "session", "Lcom/android/server/bluetooth/airplane/AirplaneMetricSession;", "handleModeChange", "", "isAirplaneModeOn", "", "isBluetoothOn", "sendAirplaneModeNotification", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "state", "getUser", "Lkotlin/Function0;", "Landroid/content/Context;", "isMediaProfileConnected", "startTime", "Lkotlin/time/TimeMark;", "notifyUserToggledBluetooth", "resolver", "Landroid/content/ContentResolver;", "userContext", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void handleModeChange(boolean isAirplaneModeOn, boolean isBluetoothOn, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> sendAirplaneModeNotification, kotlin.jvm.functions.Function0<? extends android.content.Context> getUser, boolean isMediaProfileConnected, kotlin.time.TimeMark startTime) {
            if (isAirplaneModeOn) {
                com.android.server.bluetooth.airplane.AirplaneMetricSession.session = new com.android.server.bluetooth.airplane.AirplaneMetricSession(isBluetoothOn, sendAirplaneModeNotification, isMediaProfileConnected, startTime);
                return;
            }
            com.android.server.bluetooth.airplane.AirplaneMetricSession it = com.android.server.bluetooth.airplane.AirplaneMetricSession.session;
            if (it != null) {
                it.terminate(getUser, isBluetoothOn);
            }
            com.android.server.bluetooth.airplane.AirplaneMetricSession.session = null;
        }

        public final void notifyUserToggledBluetooth(android.content.ContentResolver resolver, android.content.Context userContext, boolean isBluetoothOn) {
            com.android.server.bluetooth.airplane.AirplaneMetricSession it = com.android.server.bluetooth.airplane.AirplaneMetricSession.session;
            if (it != null) {
                it.notifyUserToggledBluetooth(resolver, userContext, isBluetoothOn);
            }
        }
    }

    public final void notifyUserToggledBluetooth(android.content.ContentResolver resolver, android.content.Context userContext, boolean isBluetoothOn) {
        boolean z = !this.userToggledBluetoothDuringApm;
        this.userToggledBluetoothDuringApm = true;
        if (z) {
            kotlin.time.TimeMark timeMark = this.sessionStartTime;
            kotlin.time.Duration.Companion companion = kotlin.time.Duration.INSTANCE;
            this.userToggledBluetoothDuringApmWithinMinute = !timeMark.mo12626plusLRDsOJo(kotlin.time.DurationKt.toDuration(1, kotlin.time.DurationUnit.MINUTES)).hasPassedNow();
        }
        if (com.android.server.bluetooth.airplane.AirplaneModeListener.isApmEnhancementEnabled(resolver)) {
            com.android.server.bluetooth.airplane.AirplaneModeListener.setUserSettingsSecure(userContext, com.android.server.bluetooth.airplane.AirplaneModeListener.BLUETOOTH_APM_STATE, isBluetoothOn ? 1 : 0);
            com.android.server.bluetooth.airplane.AirplaneModeListener.setUserSettingsSecure(userContext, com.android.server.bluetooth.airplane.AirplaneModeListener.APM_USER_TOGGLED_BLUETOOTH, 1);
            if (isBluetoothOn) {
                this.sendAirplaneModeNotification.invoke(com.android.server.bluetooth.airplane.AirplaneModeListener.APM_BT_ENABLED_NOTIFICATION);
            }
        }
    }

    public final void terminate(kotlin.jvm.functions.Function0<? extends android.content.Context> getUser, boolean isBluetoothOn) {
        com.android.bluetooth.BluetoothStatsLog.write(521, 1, this.isBluetoothOnBeforeApmToggle, this.isBluetoothOnAfterApmToggle, isBluetoothOn, com.android.server.bluetooth.airplane.AirplaneModeListener.hasUserToggledApm(getUser.invoke()), this.userToggledBluetoothDuringApm, this.userToggledBluetoothDuringApmWithinMinute, this.isMediaProfileConnectedBeforeApmToggle);
    }
}
