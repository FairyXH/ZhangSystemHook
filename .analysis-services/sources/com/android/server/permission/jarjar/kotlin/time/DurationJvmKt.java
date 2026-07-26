package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: DurationJvm.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\u001a\u0010\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0002\u001a\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000bH\u0000\u001a\u0018\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000bH\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u001c\u0010\u0004\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0005X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\b¨\u0006\u0011"}, d2 = {"durationAssertionsEnabled", "", "getDurationAssertionsEnabled", "()Z", "precisionFormats", "", "Ljava/lang/ThreadLocal;", "Ljava/text/DecimalFormat;", "[Ljava/lang/ThreadLocal;", "createFormatForDecimals", "decimals", "", "formatToExactDecimals", "", "value", "", "formatUpToDecimals", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class DurationJvmKt {
    private static final boolean durationAssertionsEnabled = false;
    private static final java.lang.ThreadLocal<java.text.DecimalFormat>[] precisionFormats;

    static {
        java.lang.ThreadLocal<java.text.DecimalFormat>[] threadLocalArr = new java.lang.ThreadLocal[4];
        for (int i = 0; i < 4; i++) {
            threadLocalArr[i] = new java.lang.ThreadLocal<>();
        }
        precisionFormats = threadLocalArr;
    }

    public static final boolean getDurationAssertionsEnabled() {
        return durationAssertionsEnabled;
    }

    private static final java.text.DecimalFormat createFormatForDecimals(int decimals) {
        java.text.DecimalFormat $this$createFormatForDecimals_u24lambda_u240 = new java.text.DecimalFormat("0");
        if (decimals > 0) {
            $this$createFormatForDecimals_u24lambda_u240.setMinimumFractionDigits(decimals);
        }
        $this$createFormatForDecimals_u24lambda_u240.setRoundingMode(java.math.RoundingMode.HALF_UP);
        return $this$createFormatForDecimals_u24lambda_u240;
    }

    public static final java.lang.String formatToExactDecimals(double value, int decimals) {
        java.text.DecimalFormat decimalFormatCreateFormatForDecimals;
        if (decimals < precisionFormats.length) {
            java.lang.ThreadLocal<java.text.DecimalFormat> threadLocal = precisionFormats[decimals];
            java.text.DecimalFormat decimalFormatCreateFormatForDecimals2 = threadLocal.get();
            if (decimalFormatCreateFormatForDecimals2 == null) {
                decimalFormatCreateFormatForDecimals2 = createFormatForDecimals(decimals);
                threadLocal.set(decimalFormatCreateFormatForDecimals2);
            } else {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(decimalFormatCreateFormatForDecimals2);
            }
            decimalFormatCreateFormatForDecimals = decimalFormatCreateFormatForDecimals2;
        } else {
            decimalFormatCreateFormatForDecimals = createFormatForDecimals(decimals);
        }
        java.text.DecimalFormat format = decimalFormatCreateFormatForDecimals;
        java.lang.String str = format.format(value);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    public static final java.lang.String formatUpToDecimals(double value, int decimals) {
        java.text.DecimalFormat $this$formatUpToDecimals_u24lambda_u242 = createFormatForDecimals(0);
        $this$formatUpToDecimals_u24lambda_u242.setMaximumFractionDigits(decimals);
        java.lang.String str = $this$formatUpToDecimals_u24lambda_u242.format(value);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }
}
