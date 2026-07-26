package com.android.server.bluetooth;

/* JADX INFO: compiled from: RadioModeListener.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0001H\u0002\u001aK\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012!\u0010\u000b\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00100\fH\u0000\u001a\u0018\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0001H\u0002\u001a\u0018\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0001H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"TAG", "", "getRadioModeValue", "", "resolver", "Landroid/content/ContentResolver;", "radio", "modeKey", "initializeRadioModeListener", "looper", "Landroid/os/Looper;", "callback", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "m", "", "isGlobalModeOn", "isSensitive", "frameworks__base__services__android_common__services"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class RadioModeListenerKt {
    private static final java.lang.String TAG = "BluetoothRadioModeListener";

    public static final boolean initializeRadioModeListener(android.os.Looper looper, final android.content.ContentResolver resolver, final java.lang.String radio, final java.lang.String modeKey, final kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> function1) {
        final android.os.Handler handler = new android.os.Handler(looper);
        android.database.ContentObserver contentObserver = new android.database.ContentObserver(handler) { // from class: com.android.server.bluetooth.RadioModeListenerKt$initializeRadioModeListener$observer$1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                function1.invoke(java.lang.Boolean.valueOf(com.android.server.bluetooth.RadioModeListenerKt.getRadioModeValue(resolver, radio, modeKey)));
            }
        };
        resolver.registerContentObserver(android.provider.Settings.Global.getUriFor(radio), false, contentObserver);
        resolver.registerContentObserver(android.provider.Settings.Global.getUriFor(modeKey), false, contentObserver);
        return getRadioModeValue(resolver, radio, modeKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean getRadioModeValue(android.content.ContentResolver resolver, java.lang.String radio, java.lang.String modeKey) {
        if (isSensitive(resolver, radio)) {
            return isGlobalModeOn(resolver, modeKey);
        }
        com.android.server.bluetooth.Log.INSTANCE.d(TAG, "Not sensitive to " + radio + " change. Forced to false");
        return false;
    }

    private static final boolean isSensitive(android.content.ContentResolver resolver, java.lang.String radio) {
        java.lang.String radios = android.provider.Settings.Global.getString(resolver, radio);
        return radios != null && kotlin.text.StringsKt.contains$default((java.lang.CharSequence) radios, (java.lang.CharSequence) "bluetooth", false, 2, (java.lang.Object) null);
    }

    private static final boolean isGlobalModeOn(android.content.ContentResolver resolver, java.lang.String modeKey) {
        return android.provider.Settings.Global.getInt(resolver, modeKey, 0) == 1;
    }
}
