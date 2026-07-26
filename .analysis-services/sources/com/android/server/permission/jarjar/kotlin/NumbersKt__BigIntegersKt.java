package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: BigIntegers.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0010\u001a\u00020\u0011*\u00020\u0001H\u0087\b\u001a!\u0010\u0010\u001a\u00020\u0011*\u00020\u00012\b\b\u0002\u0010\u0012\u001a\u00020\r2\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\u0016H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0018\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f¨\u0006\u0019"}, d2 = {"and", "Ljava/math/BigInteger;", "other", "dec", "div", "inc", "inv", "minus", "or", "plus", "rem", "shl", "n", "", "shr", "times", "toBigDecimal", "Ljava/math/BigDecimal;", "scale", "mathContext", "Ljava/math/MathContext;", "toBigInteger", "", "unaryMinus", "xor", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/NumbersKt")
class NumbersKt__BigIntegersKt extends com.android.server.permission.jarjar.kotlin.NumbersKt__BigDecimalsKt {
    private static final java.math.BigInteger plus(java.math.BigInteger $this$plus, java.math.BigInteger other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$plus, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigInteger bigIntegerAdd = $this$plus.add(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
        return bigIntegerAdd;
    }

    private static final java.math.BigInteger minus(java.math.BigInteger $this$minus, java.math.BigInteger other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minus, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigInteger bigIntegerSubtract = $this$minus.subtract(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerSubtract, "subtract(...)");
        return bigIntegerSubtract;
    }

    private static final java.math.BigInteger times(java.math.BigInteger $this$times, java.math.BigInteger other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$times, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigInteger bigIntegerMultiply = $this$times.multiply(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerMultiply, "multiply(...)");
        return bigIntegerMultiply;
    }

    private static final java.math.BigInteger div(java.math.BigInteger $this$div, java.math.BigInteger other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$div, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigInteger bigIntegerDivide = $this$div.divide(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerDivide, "divide(...)");
        return bigIntegerDivide;
    }

    private static final java.math.BigInteger rem(java.math.BigInteger $this$rem, java.math.BigInteger other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$rem, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigInteger bigIntegerRemainder = $this$rem.remainder(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerRemainder, "remainder(...)");
        return bigIntegerRemainder;
    }

    private static final java.math.BigInteger unaryMinus(java.math.BigInteger $this$unaryMinus) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        java.math.BigInteger bigIntegerNegate = $this$unaryMinus.negate();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerNegate, "negate(...)");
        return bigIntegerNegate;
    }

    private static final java.math.BigInteger inc(java.math.BigInteger $this$inc) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$inc, "<this>");
        java.math.BigInteger bigIntegerAdd = $this$inc.add(java.math.BigInteger.ONE);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
        return bigIntegerAdd;
    }

    private static final java.math.BigInteger dec(java.math.BigInteger $this$dec) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$dec, "<this>");
        java.math.BigInteger bigIntegerSubtract = $this$dec.subtract(java.math.BigInteger.ONE);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerSubtract, "subtract(...)");
        return bigIntegerSubtract;
    }

    private static final java.math.BigInteger inv(java.math.BigInteger $this$inv) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$inv, "<this>");
        java.math.BigInteger bigIntegerNot = $this$inv.not();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerNot, "not(...)");
        return bigIntegerNot;
    }

    private static final java.math.BigInteger and(java.math.BigInteger $this$and, java.math.BigInteger other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$and, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigInteger bigIntegerAnd = $this$and.and(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAnd, "and(...)");
        return bigIntegerAnd;
    }

    private static final java.math.BigInteger or(java.math.BigInteger $this$or, java.math.BigInteger other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$or, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigInteger bigIntegerOr = $this$or.or(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerOr, "or(...)");
        return bigIntegerOr;
    }

    private static final java.math.BigInteger xor(java.math.BigInteger $this$xor, java.math.BigInteger other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$xor, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        java.math.BigInteger bigIntegerXor = $this$xor.xor(other);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerXor, "xor(...)");
        return bigIntegerXor;
    }

    private static final java.math.BigInteger shl(java.math.BigInteger $this$shl, int n) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$shl, "<this>");
        java.math.BigInteger bigIntegerShiftLeft = $this$shl.shiftLeft(n);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerShiftLeft, "shiftLeft(...)");
        return bigIntegerShiftLeft;
    }

    private static final java.math.BigInteger shr(java.math.BigInteger $this$shr, int n) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$shr, "<this>");
        java.math.BigInteger bigIntegerShiftRight = $this$shr.shiftRight(n);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerShiftRight, "shiftRight(...)");
        return bigIntegerShiftRight;
    }

    private static final java.math.BigInteger toBigInteger(int $this$toBigInteger) {
        java.math.BigInteger bigIntegerValueOf = java.math.BigInteger.valueOf($this$toBigInteger);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerValueOf, "valueOf(...)");
        return bigIntegerValueOf;
    }

    private static final java.math.BigInteger toBigInteger(long $this$toBigInteger) {
        java.math.BigInteger bigIntegerValueOf = java.math.BigInteger.valueOf($this$toBigInteger);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerValueOf, "valueOf(...)");
        return bigIntegerValueOf;
    }

    private static final java.math.BigDecimal toBigDecimal(java.math.BigInteger $this$toBigDecimal) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigDecimal, "<this>");
        return new java.math.BigDecimal($this$toBigDecimal);
    }

    static /* synthetic */ java.math.BigDecimal toBigDecimal$default(java.math.BigInteger $this$toBigDecimal_u24default, int scale, java.math.MathContext mathContext, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            scale = 0;
        }
        if ((i & 2) != 0) {
            java.math.MathContext mathContext2 = java.math.MathContext.UNLIMITED;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(mathContext2, "UNLIMITED");
            mathContext = mathContext2;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigDecimal_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new java.math.BigDecimal($this$toBigDecimal_u24default, scale, mathContext);
    }

    private static final java.math.BigDecimal toBigDecimal(java.math.BigInteger $this$toBigDecimal, int scale, java.math.MathContext mathContext) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigDecimal, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new java.math.BigDecimal($this$toBigDecimal, scale, mathContext);
    }
}
