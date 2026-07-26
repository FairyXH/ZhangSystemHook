package com.android.server.bluetooth;

/* JADX INFO: compiled from: AutoOnFeature.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0001\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dBE\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\f\u0012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\r\u0010\u0016\u001a\u00020\nH\u0001¢\u0006\u0002\b\u0017J\b\u0010\u0018\u001a\u00020\nH\u0016J\r\u0010\u0019\u001a\u00020\nH\u0000¢\u0006\u0002\b\u001aJ\b\u0010\u001b\u001a\u00020\u001cH\u0016R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\u00020\u000fX\u0082\u0004ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\u0015\u0082\u0002\u000b\n\u0005\b¡\u001e0\u0001\n\u0002\b!¨\u0006\u001e"}, d2 = {"Lcom/android/server/bluetooth/Timer;", "Landroid/app/AlarmManager$OnAlarmListener;", "looper", "Landroid/os/Looper;", "context", "Landroid/content/Context;", "receiver", "Landroid/content/BroadcastReceiver;", "callback_on", "Lkotlin/Function0;", "", "now", "Ljava/time/LocalDateTime;", "target", "timeToSleep", "Lkotlin/time/Duration;", "(Landroid/os/Looper;Landroid/content/Context;Landroid/content/BroadcastReceiver;Lkotlin/jvm/functions/Function0;Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;J)V", "alarmManager", "Landroid/app/AlarmManager;", "handler", "Landroid/os/Handler;", "J", "cancel", "cancel$frameworks__base__services__android_common__services", "onAlarm", "pause", "pause$frameworks__base__services__android_common__services", "toString", "", "Companion", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class Timer implements android.app.AlarmManager.OnAlarmListener {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.android.server.bluetooth.Timer.Companion INSTANCE = new com.android.server.bluetooth.Timer.Companion(null);
    private static final java.lang.String STORAGE_KEY = "bluetooth_internal_automatic_turn_on_timer";
    private final android.app.AlarmManager alarmManager;
    private final kotlin.jvm.functions.Function0<kotlin.Unit> callback_on;
    private final android.content.Context context;
    private final android.os.Handler handler;
    private final java.time.LocalDateTime now;
    private final android.content.BroadcastReceiver receiver;
    private final java.time.LocalDateTime target;
    private final long timeToSleep;

    public /* synthetic */ Timer(android.os.Looper looper, android.content.Context context, android.content.BroadcastReceiver broadcastReceiver, kotlin.jvm.functions.Function0 function0, java.time.LocalDateTime localDateTime, java.time.LocalDateTime localDateTime2, long j, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(looper, context, broadcastReceiver, function0, localDateTime, localDateTime2, j);
    }

    private Timer(android.os.Looper looper, android.content.Context context, android.content.BroadcastReceiver receiver, kotlin.jvm.functions.Function0<kotlin.Unit> function0, java.time.LocalDateTime now, java.time.LocalDateTime target, long timeToSleep) {
        this.context = context;
        this.receiver = receiver;
        this.callback_on = function0;
        this.now = now;
        this.target = target;
        this.timeToSleep = timeToSleep;
        java.lang.Object systemService = this.context.getSystemService((java.lang.Class<java.lang.Object>) android.app.AlarmManager.class);
        kotlin.jvm.internal.Intrinsics.checkNotNull(systemService);
        this.alarmManager = (android.app.AlarmManager) systemService;
        this.handler = new android.os.Handler(looper);
        INSTANCE.writeDateToStorage(this.target, this.context.getContentResolver());
        this.alarmManager.set(3, android.os.SystemClock.elapsedRealtime() + kotlin.time.Duration.m12651getInWholeMillisecondsimpl(this.timeToSleep), "Bluetooth AutoOnFeature", this, this.handler);
        com.android.server.bluetooth.Log.INSTANCE.i("AutoOnFeature", "[" + this + "]: Scheduling next Bluetooth restart");
        android.content.Context context2 = this.context;
        android.content.BroadcastReceiver broadcastReceiver = this.receiver;
        android.content.IntentFilter $this$_init__u24lambda_u240 = new android.content.IntentFilter();
        $this$_init__u24lambda_u240.addAction("android.intent.action.DATE_CHANGED");
        $this$_init__u24lambda_u240.addAction("android.intent.action.TIMEZONE_CHANGED");
        $this$_init__u24lambda_u240.addAction("android.intent.action.TIME_SET");
        kotlin.Unit unit = kotlin.Unit.INSTANCE;
        context2.registerReceiver(broadcastReceiver, $this$_init__u24lambda_u240, null, this.handler);
    }

    @Override // android.app.AlarmManager.OnAlarmListener
    public void onAlarm() {
        com.android.server.bluetooth.Log.INSTANCE.i("AutoOnFeature", "[" + this + "]: Bluetooth restarting now");
        this.callback_on.invoke();
        cancel$frameworks__base__services__android_common__services();
        com.android.server.bluetooth.AutoOnFeature.setTimer(null);
    }

    /* JADX INFO: compiled from: AutoOnFeature.kt */
    @kotlin.Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u0018\u0010\f\u001a\n \r*\u0004\u0018\u00010\t0\t2\u0006\u0010\u000e\u001a\u00020\tH\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u000bJ.\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00100\u001aJ\u0018\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002R\u001c\u0010\u0003\u001a\u00020\u00048\u0000X\u0081D¢\u0006\u000e\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u001e"}, d2 = {"Lcom/android/server/bluetooth/Timer$Companion;", "", "()V", "STORAGE_KEY", "", "getSTORAGE_KEY$frameworks__base__services__android_common__services$annotations", "getSTORAGE_KEY$frameworks__base__services__android_common__services", "()Ljava/lang/String;", "getDateFromStorage", "Ljava/time/LocalDateTime;", "resolver", "Landroid/content/ContentResolver;", "nextTimeout", "kotlin.jvm.PlatformType", "now", "resetStorage", "", "start", "Lcom/android/server/bluetooth/Timer;", "looper", "Landroid/os/Looper;", "context", "Landroid/content/Context;", "receiver", "Landroid/content/BroadcastReceiver;", "callback_on", "Lkotlin/Function0;", "writeDateToStorage", "", "date", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void getSTORAGE_KEY$frameworks__base__services__android_common__services$annotations() {
        }

        private Companion() {
        }

        public final java.lang.String getSTORAGE_KEY$frameworks__base__services__android_common__services() {
            return com.android.server.bluetooth.Timer.STORAGE_KEY;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean writeDateToStorage(java.time.LocalDateTime date, android.content.ContentResolver resolver) {
            return android.provider.Settings.Secure.putString(resolver, getSTORAGE_KEY$frameworks__base__services__android_common__services(), date.toString());
        }

        private final java.time.LocalDateTime getDateFromStorage(android.content.ContentResolver resolver) {
            java.lang.String date = android.provider.Settings.Secure.getString(resolver, getSTORAGE_KEY$frameworks__base__services__android_common__services());
            if (date != null) {
                return java.time.LocalDateTime.parse(date);
            }
            return null;
        }

        public final void resetStorage(android.content.ContentResolver resolver) {
            android.provider.Settings.Secure.putString(resolver, getSTORAGE_KEY$frameworks__base__services__android_common__services(), null);
        }

        public final com.android.server.bluetooth.Timer start(android.os.Looper looper, android.content.Context context, android.content.BroadcastReceiver receiver, kotlin.jvm.functions.Function0<kotlin.Unit> callback_on) {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.LocalDateTime dateFromStorage = getDateFromStorage(context.getContentResolver());
            if (dateFromStorage == null) {
                dateFromStorage = nextTimeout(now);
            }
            java.time.LocalDateTime target = dateFromStorage;
            long timeToSleep = kotlin.time.DurationKt.toDuration(now.until(target, java.time.temporal.ChronoUnit.NANOS), kotlin.time.DurationUnit.NANOSECONDS);
            if (kotlin.time.Duration.m12666isNegativeimpl(timeToSleep)) {
                com.android.server.bluetooth.Log.INSTANCE.i("AutoOnFeature", "Starting now (" + now + ") as it was scheduled for " + target);
                callback_on.invoke();
                resetStorage(context.getContentResolver());
                return null;
            }
            return new com.android.server.bluetooth.Timer(looper, context, receiver, callback_on, now, target, timeToSleep, null);
        }

        private final java.time.LocalDateTime nextTimeout(java.time.LocalDateTime now) {
            return java.time.LocalDateTime.of(now.toLocalDate(), java.time.LocalTime.of(5, 0)).plusDays(1L);
        }
    }

    public final void pause$frameworks__base__services__android_common__services() {
        com.android.server.bluetooth.Log.INSTANCE.i("AutoOnFeature", "[" + this + "]: Pausing timer");
        this.context.unregisterReceiver(this.receiver);
        this.alarmManager.cancel(this);
        this.handler.removeCallbacksAndMessages(null);
    }

    public final void cancel$frameworks__base__services__android_common__services() {
        com.android.server.bluetooth.Log.INSTANCE.i("AutoOnFeature", "[" + this + "]: Cancelling timer");
        this.context.unregisterReceiver(this.receiver);
        this.alarmManager.cancel(this);
        this.handler.removeCallbacksAndMessages(null);
        INSTANCE.resetStorage(this.context.getContentResolver());
    }

    public java.lang.String toString() {
        return "Timer was scheduled at " + this.now + " and should expire at " + this.target + ". (sleep for " + kotlin.time.Duration.m12682toStringimpl(this.timeToSleep) + ").";
    }
}
