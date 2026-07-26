package com.android.server.permission.access.util;

/* JADX INFO: compiled from: IntExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\u0004\u001a!\u0010\u0003\u001a\u00020\u0004*\u00020\u00012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00040\u0006H\u0086\b\u001a\u0012\u0010\u0007\u001a\u00020\b*\u00020\u00012\u0006\u0010\t\u001a\u00020\u0001\u001a\u0012\u0010\n\u001a\u00020\b*\u00020\u00012\u0006\u0010\t\u001a\u00020\u0001¨\u0006\u000b"}, d2 = {"andInv", "", "other", "flagsToString", "", "flagToString", "Lkotlin/Function1;", "hasAnyBit", "", "bits", "hasBits", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IntExtensionsKt {
    public static final boolean hasAnyBit(int $this$hasAnyBit, int bits) {
        return ($this$hasAnyBit & bits) != 0;
    }

    public static final boolean hasBits(int $this$hasBits, int bits) {
        return ($this$hasBits & bits) == bits;
    }

    public static final int andInv(int $this$andInv, int other) {
        return (~other) & $this$andInv;
    }

    public static final java.lang.String flagsToString(int $this$flagsToString, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.Integer, java.lang.String> function1) {
        int flags = $this$flagsToString;
        java.lang.StringBuilder $this$flagsToString_u24lambda_u240 = new java.lang.StringBuilder();
        $this$flagsToString_u24lambda_u240.append("[");
        while (flags != 0) {
            int flag = 1 << java.lang.Integer.numberOfTrailingZeros(flags);
            flags = andInv(flags, flag);
            $this$flagsToString_u24lambda_u240.append(function1.invoke(java.lang.Integer.valueOf(flag)));
            if (flags != 0) {
                $this$flagsToString_u24lambda_u240.append('|');
            }
        }
        $this$flagsToString_u24lambda_u240.append("]");
        java.lang.String string = $this$flagsToString_u24lambda_u240.toString();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }
}
