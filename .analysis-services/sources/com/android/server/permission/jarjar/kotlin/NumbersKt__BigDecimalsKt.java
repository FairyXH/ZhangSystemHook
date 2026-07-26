package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: BigDecimals.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0002\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\nH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u000eH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u000fH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\u0010\u001a\u00020\u0001*\u00020\u0001H\u0087\n¨\u0006\u0011"}, d2 = {"dec", "Ljava/math/BigDecimal;", "div", "other", "inc", "minus", "plus", "rem", "times", "toBigDecimal", "", "mathContext", "Ljava/math/MathContext;", "", "", "", "unaryMinus", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/NumbersKt")
class NumbersKt__BigDecimalsKt {
    private static final java.math.BigDecimal plus(java.math.BigDecimal $this$plus, java.math.BigDecimal other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$plus, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigDecimal bigDecimalAdd = $this$plus.add(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
        return bigDecimalAdd;
    }

    private static final java.math.BigDecimal minus(java.math.BigDecimal $this$minus, java.math.BigDecimal other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minus, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigDecimal bigDecimalSubtract = $this$minus.subtract(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalSubtract, "subtract(...)");
        return bigDecimalSubtract;
    }

    private static final java.math.BigDecimal times(java.math.BigDecimal $this$times, java.math.BigDecimal other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$times, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigDecimal bigDecimalMultiply = $this$times.multiply(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalMultiply, "multiply(...)");
        return bigDecimalMultiply;
    }

    private static final java.math.BigDecimal div(java.math.BigDecimal $this$div, java.math.BigDecimal other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$div, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigDecimal bigDecimalDivide = $this$div.divide(other, java.math.RoundingMode.HALF_EVEN);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalDivide, "divide(...)");
        return bigDecimalDivide;
    }

    private static final java.math.BigDecimal rem(java.math.BigDecimal $this$rem, java.math.BigDecimal other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$rem, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigDecimal bigDecimalRemainder = $this$rem.remainder(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalRemainder, "remainder(...)");
        return bigDecimalRemainder;
    }

    private static final java.math.BigDecimal unaryMinus(java.math.BigDecimal $this$unaryMinus) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        java.math.BigDecimal bigDecimalNegate = $this$unaryMinus.negate();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalNegate, "negate(...)");
        return bigDecimalNegate;
    }

    private static final java.math.BigDecimal inc(java.math.BigDecimal $this$inc) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$inc, "<this>");
        java.math.BigDecimal bigDecimalAdd = $this$inc.add(java.math.BigDecimal.ONE);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
        return bigDecimalAdd;
    }

    private static final java.math.BigDecimal dec(java.math.BigDecimal $this$dec) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$dec, "<this>");
        java.math.BigDecimal bigDecimalSubtract = $this$dec.subtract(java.math.BigDecimal.ONE);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalSubtract, "subtract(...)");
        return bigDecimalSubtract;
    }

    private static final java.math.BigDecimal toBigDecimal(int $this$toBigDecimal) {
        java.math.BigDecimal bigDecimalValueOf = java.math.BigDecimal.valueOf($this$toBigDecimal);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalValueOf, "valueOf(...)");
        return bigDecimalValueOf;
    }

    private static final java.math.BigDecimal toBigDecimal(int $this$toBigDecimal, java.math.MathContext mathContext) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new java.math.BigDecimal($this$toBigDecimal, mathContext);
    }

    private static final java.math.BigDecimal toBigDecimal(long $this$toBigDecimal) {
        java.math.BigDecimal bigDecimalValueOf = java.math.BigDecimal.valueOf($this$toBigDecimal);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalValueOf, "valueOf(...)");
        return bigDecimalValueOf;
    }

    private static final java.math.BigDecimal toBigDecimal(long $this$toBigDecimal, java.math.MathContext mathContext) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new java.math.BigDecimal($this$toBigDecimal, mathContext);
    }

    private static final java.math.BigDecimal toBigDecimal(float $this$toBigDecimal) {
        return new java.math.BigDecimal(java.lang.String.valueOf($this$toBigDecimal));
    }

    private static final java.math.BigDecimal toBigDecimal(float $this$toBigDecimal, java.math.MathContext mathContext) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new java.math.BigDecimal(java.lang.String.valueOf($this$toBigDecimal), mathContext);
    }

    private static final java.math.BigDecimal toBigDecimal(double $this$toBigDecimal) {
        return new java.math.BigDecimal(java.lang.String.valueOf($this$toBigDecimal));
    }

    private static final java.math.BigDecimal toBigDecimal(double $this$toBigDecimal, java.math.MathContext mathContext) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new java.math.BigDecimal(java.lang.String.valueOf($this$toBigDecimal), mathContext);
    }
}
