package com.android.server.bluetooth;

/* JADX INFO: compiled from: Log.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/android/server/bluetooth/Log;", "", "()V", "Companion", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class Log {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.android.server.bluetooth.Log.Companion INSTANCE = new com.android.server.bluetooth.Log.Companion(null);

    @kotlin.jvm.JvmStatic
    public static final int d(java.lang.String str, java.lang.String str2) {
        return INSTANCE.d(str, str2);
    }

    @kotlin.jvm.JvmStatic
    public static final int e(java.lang.String str, java.lang.String str2) {
        return INSTANCE.e(str, str2);
    }

    @kotlin.jvm.JvmStatic
    public static final int e(java.lang.String str, java.lang.String str2, java.lang.Throwable th) {
        return INSTANCE.e(str, str2, th);
    }

    @kotlin.jvm.JvmStatic
    public static final int i(java.lang.String str, java.lang.String str2) {
        return INSTANCE.i(str, str2);
    }

    @kotlin.jvm.JvmStatic
    public static final int v(java.lang.String str, java.lang.String str2) {
        return INSTANCE.v(str, str2);
    }

    @kotlin.jvm.JvmStatic
    public static final int w(java.lang.String str, java.lang.String str2) {
        return INSTANCE.w(str, str2);
    }

    @kotlin.jvm.JvmStatic
    public static final int w(java.lang.String str, java.lang.String str2, java.lang.Throwable th) {
        return INSTANCE.w(str, str2, th);
    }

    /* JADX INFO: compiled from: Log.kt */
    @kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0007J\u0018\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0007J \u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0007J\u0018\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0007J\u0018\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0007J\u0018\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0007J \u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0007¨\u0006\u000e"}, d2 = {"Lcom/android/server/bluetooth/Log$Companion;", "", "()V", "d", "", "subtag", "", "msg", "e", "tr", "", "i", "v", "w", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @kotlin.jvm.JvmStatic
        public final int v(java.lang.String subtag, java.lang.String msg) {
            return android.util.Log.v("BluetoothSystemServer", subtag + ": " + msg);
        }

        @kotlin.jvm.JvmStatic
        public final int d(java.lang.String subtag, java.lang.String msg) {
            return android.util.Log.d("BluetoothSystemServer", subtag + ": " + msg);
        }

        @kotlin.jvm.JvmStatic
        public final int i(java.lang.String subtag, java.lang.String msg) {
            return android.util.Log.i("BluetoothSystemServer", subtag + ": " + msg);
        }

        @kotlin.jvm.JvmStatic
        public final int w(java.lang.String subtag, java.lang.String msg) {
            return android.util.Log.w("BluetoothSystemServer", subtag + ": " + msg);
        }

        @kotlin.jvm.JvmStatic
        public final int w(java.lang.String subtag, java.lang.String msg, java.lang.Throwable tr) {
            return android.util.Log.w("BluetoothSystemServer", subtag + ": " + msg, tr);
        }

        @kotlin.jvm.JvmStatic
        public final int e(java.lang.String subtag, java.lang.String msg) {
            return android.util.Log.e("BluetoothSystemServer", subtag + ": " + msg);
        }

        @kotlin.jvm.JvmStatic
        public final int e(java.lang.String subtag, java.lang.String msg, java.lang.Throwable tr) {
            return android.util.Log.e("BluetoothSystemServer", subtag + ": " + msg, tr);
        }
    }

    private Log() {
    }
}
