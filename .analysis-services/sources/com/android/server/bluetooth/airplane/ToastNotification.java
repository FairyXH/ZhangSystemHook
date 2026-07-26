package com.android.server.bluetooth.airplane;

/* JADX INFO: compiled from: ModeListener.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0000\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/android/server/bluetooth/airplane/ToastNotification;", "", "()V", "Companion", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ToastNotification {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.android.server.bluetooth.airplane.ToastNotification.Companion INSTANCE = new com.android.server.bluetooth.airplane.ToastNotification.Companion(null);
    public static final int MAX_TOAST_COUNT = 10;
    private static final java.lang.String TOAST_COUNT = "bluetooth_airplane_toast_count";

    /* JADX INFO: compiled from: ModeListener.kt */
    @kotlin.Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/android/server/bluetooth/airplane/ToastNotification$Companion;", "", "()V", "MAX_TOAST_COUNT", "", "TOAST_COUNT", "", "displayIfNeeded", "", "resolver", "Landroid/content/ContentResolver;", "getUser", "Lkotlin/Function0;", "Landroid/content/Context;", "userNeedToBeNotified", "", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        private final boolean userNeedToBeNotified(android.content.ContentResolver resolver) {
            int currentToastCount = android.provider.Settings.Global.getInt(resolver, com.android.server.bluetooth.airplane.ToastNotification.TOAST_COUNT, 0);
            if (currentToastCount >= 10) {
                return false;
            }
            android.provider.Settings.Global.putInt(resolver, com.android.server.bluetooth.airplane.ToastNotification.TOAST_COUNT, currentToastCount + 1);
            return true;
        }

        public final void displayIfNeeded(android.content.ContentResolver resolver, kotlin.jvm.functions.Function0<? extends android.content.Context> getUser) {
            if (!userNeedToBeNotified(resolver)) {
                com.android.server.bluetooth.Log.INSTANCE.d("AirplaneModeListener", "Dismissed Toast notification");
                return;
            }
            android.content.Context userContext = getUser.invoke();
            android.content.res.Resources r = userContext.getResources();
            java.lang.CharSequence text = r.getString(android.content.res.Resources.getSystem().getIdentifier("bluetooth_airplane_mode_toast", "string", com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME));
            android.widget.Toast.makeText(userContext, text, 1).show();
            com.android.server.bluetooth.Log.INSTANCE.d("AirplaneModeListener", "Displayed Toast notification");
        }
    }

    private ToastNotification() {
    }
}
