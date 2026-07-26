package com.android.server.permission.jarjar.kotlin.collections.unsigned;

/* JADX INFO: compiled from: _UArraysJvm.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000h\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b \n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0019\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u0019\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u0007H\u0007¢\u0006\u0004\b\b\u0010\t\u001a\u0019\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\n0\u0001*\u00020\u000bH\u0007¢\u0006\u0004\b\f\u0010\r\u001a\u0019\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0001*\u00020\u000fH\u0007¢\u0006\u0004\b\u0010\u0010\u0011\u001a/\u0010\u0012\u001a\u00020\u0013*\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007¢\u0006\u0004\b\u0017\u0010\u0018\u001a/\u0010\u0012\u001a\u00020\u0013*\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00062\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007¢\u0006\u0004\b\u0019\u0010\u001a\u001a/\u0010\u0012\u001a\u00020\u0013*\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007¢\u0006\u0004\b\u001b\u0010\u001c\u001a/\u0010\u0012\u001a\u00020\u0013*\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007¢\u0006\u0004\b\u001d\u0010\u001e\u001a\u001c\u0010\u001f\u001a\u00020\u0002*\u00020\u00032\u0006\u0010 \u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b!\u0010\"\u001a\u001c\u0010\u001f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010 \u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b#\u0010$\u001a\u001c\u0010\u001f\u001a\u00020\n*\u00020\u000b2\u0006\u0010 \u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b%\u0010&\u001a\u001c\u0010\u001f\u001a\u00020\u000e*\u00020\u000f2\u0006\u0010 \u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b'\u0010(\u001a\u0015\u0010)\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007¢\u0006\u0004\b*\u0010+\u001a\u0015\u0010)\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007¢\u0006\u0004\b,\u0010-\u001a\u0015\u0010)\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007¢\u0006\u0004\b.\u0010/\u001a\u0015\u0010)\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007¢\u0006\u0004\b0\u00101\u001a=\u00102\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\b7\u00108\u001a=\u00102\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\b9\u0010:\u001a=\u00102\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\b;\u0010<\u001a=\u00102\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\b=\u0010>\u001a1\u0010?\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007¢\u0006\u0004\bC\u0010D\u001a1\u0010?\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007¢\u0006\u0004\bE\u0010F\u001a1\u0010?\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007¢\u0006\u0004\bG\u0010H\u001a1\u0010?\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007¢\u0006\u0004\bI\u0010J\u001a\u0015\u0010K\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007¢\u0006\u0004\bL\u0010+\u001a\u0015\u0010K\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007¢\u0006\u0004\bM\u0010-\u001a\u0015\u0010K\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007¢\u0006\u0004\bN\u0010/\u001a\u0015\u0010K\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007¢\u0006\u0004\bO\u00101\u001a=\u0010P\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\bQ\u00108\u001a=\u0010P\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\bR\u0010:\u001a=\u0010P\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\bS\u0010<\u001a=\u0010P\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\bT\u0010>\u001a1\u0010U\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007¢\u0006\u0004\bV\u0010D\u001a1\u0010U\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007¢\u0006\u0004\bW\u0010F\u001a1\u0010U\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007¢\u0006\u0004\bX\u0010H\u001a1\u0010U\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007¢\u0006\u0004\bY\u0010J\u001a+\u0010Z\u001a\u00020[*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0000¢\u0006\u0004\b\\\u0010]\u001a+\u0010Z\u001a\u00020^*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010`\u001a+\u0010Z\u001a\u00020[*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0000¢\u0006\u0004\b\\\u0010a\u001a+\u0010Z\u001a\u00020^*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010b\u001a+\u0010Z\u001a\u00020[*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0000¢\u0006\u0004\b\\\u0010c\u001a+\u0010Z\u001a\u00020^*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010d\u001a+\u0010Z\u001a\u00020[*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0000¢\u0006\u0004\b\\\u0010e\u001a+\u0010Z\u001a\u00020^*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010f\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006g"}, d2 = {"asList", "", "Lkotlin/UByte;", "Lkotlin/UByteArray;", "asList-GBYM_sE", "([B)Ljava/util/List;", "Lkotlin/UInt;", "Lkotlin/UIntArray;", "asList--ajY-9A", "([I)Ljava/util/List;", "Lkotlin/ULong;", "Lkotlin/ULongArray;", "asList-QwZRm1k", "([J)Ljava/util/List;", "Lkotlin/UShort;", "Lkotlin/UShortArray;", "asList-rL5Bavg", "([S)Ljava/util/List;", "binarySearch", "", "element", "fromIndex", "toIndex", "binarySearch-WpHrYlw", "([BBII)I", "binarySearch-2fe2U9s", "([IIII)I", "binarySearch-K6DWlUc", "([JJII)I", "binarySearch-EtDCXyQ", "([SSII)I", "elementAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "elementAt-PpDY95g", "([BI)B", "elementAt-qFRl0hI", "([II)I", "elementAt-r7IrZao", "([JI)J", "elementAt-nggk6HY", "([SI)S", "max", "max-GBYM_sE", "([B)Lkotlin/UByte;", "max--ajY-9A", "([I)Lkotlin/UInt;", "max-QwZRm1k", "([J)Lkotlin/ULong;", "max-rL5Bavg", "([S)Lkotlin/UShort;", "maxBy", "R", "", "selector", "Lkotlin/Function1;", "maxBy-JOV_ifY", "([BLkotlin/jvm/functions/Function1;)Lkotlin/UByte;", "maxBy-jgv0xPQ", "([ILkotlin/jvm/functions/Function1;)Lkotlin/UInt;", "maxBy-MShoTSo", "([JLkotlin/jvm/functions/Function1;)Lkotlin/ULong;", "maxBy-xTcfx_M", "([SLkotlin/jvm/functions/Function1;)Lkotlin/UShort;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "maxWith-XMRcp5o", "([BLjava/util/Comparator;)Lkotlin/UByte;", "maxWith-YmdZ_VM", "([ILjava/util/Comparator;)Lkotlin/UInt;", "maxWith-zrEWJaI", "([JLjava/util/Comparator;)Lkotlin/ULong;", "maxWith-eOHTfZs", "([SLjava/util/Comparator;)Lkotlin/UShort;", "min", "min-GBYM_sE", "min--ajY-9A", "min-QwZRm1k", "min-rL5Bavg", "minBy", "minBy-JOV_ifY", "minBy-jgv0xPQ", "minBy-MShoTSo", "minBy-xTcfx_M", "minWith", "minWith-XMRcp5o", "minWith-YmdZ_VM", "minWith-zrEWJaI", "minWith-eOHTfZs", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "Ljava/math/BigInteger;", "sumOfBigInteger", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, pn = "com.android.server.permission.jarjar.kotlin.collections", xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/unsigned/UArraysKt")
class UArraysKt___UArraysJvmKt {
    /* JADX INFO: renamed from: elementAt-qFRl0hI, reason: not valid java name */
    private static final int m6571elementAtqFRl0hI(int[] $this$elementAt_u2dqFRl0hI, int index) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$elementAt_u2dqFRl0hI, "$this$elementAt");
        return com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA($this$elementAt_u2dqFRl0hI, index);
    }

    /* JADX INFO: renamed from: elementAt-r7IrZao, reason: not valid java name */
    private static final long m6572elementAtr7IrZao(long[] $this$elementAt_u2dr7IrZao, int index) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$elementAt_u2dr7IrZao, "$this$elementAt");
        return com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU($this$elementAt_u2dr7IrZao, index);
    }

    /* JADX INFO: renamed from: elementAt-PpDY95g, reason: not valid java name */
    private static final byte m6569elementAtPpDY95g(byte[] $this$elementAt_u2dPpDY95g, int index) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$elementAt_u2dPpDY95g, "$this$elementAt");
        return com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ($this$elementAt_u2dPpDY95g, index);
    }

    /* JADX INFO: renamed from: elementAt-nggk6HY, reason: not valid java name */
    private static final short m6570elementAtnggk6HY(short[] $this$elementAt_u2dnggk6HY, int index) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$elementAt_u2dnggk6HY, "$this$elementAt");
        return com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg($this$elementAt_u2dnggk6HY, index);
    }

    /* JADX INFO: renamed from: asList--ajY-9A, reason: not valid java name */
    public static final java.util.List<com.android.server.permission.jarjar.kotlin.UInt> m6557asListajY9A(int[] $this$asList_u2d_u2dajY_u2d9A) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asList_u2d_u2dajY_u2d9A, "$this$asList");
        return new com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$1($this$asList_u2d_u2dajY_u2d9A);
    }

    /* JADX INFO: renamed from: asList-QwZRm1k, reason: not valid java name */
    public static final java.util.List<com.android.server.permission.jarjar.kotlin.ULong> m6559asListQwZRm1k(long[] $this$asList_u2dQwZRm1k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asList_u2dQwZRm1k, "$this$asList");
        return new com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$2($this$asList_u2dQwZRm1k);
    }

    /* JADX INFO: renamed from: asList-GBYM_sE, reason: not valid java name */
    public static final java.util.List<com.android.server.permission.jarjar.kotlin.UByte> m6558asListGBYM_sE(byte[] $this$asList_u2dGBYM_sE) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asList_u2dGBYM_sE, "$this$asList");
        return new com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$3($this$asList_u2dGBYM_sE);
    }

    /* JADX INFO: renamed from: asList-rL5Bavg, reason: not valid java name */
    public static final java.util.List<com.android.server.permission.jarjar.kotlin.UShort> m6560asListrL5Bavg(short[] $this$asList_u2drL5Bavg) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asList_u2drL5Bavg, "$this$asList");
        return new com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$4($this$asList_u2drL5Bavg);
    }

    /* JADX INFO: renamed from: binarySearch-2fe2U9s$default, reason: not valid java name */
    public static /* synthetic */ int m6562binarySearch2fe2U9s$default(int[] iArr, int i, int i2, int i3, int i4, java.lang.Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = com.android.server.permission.jarjar.kotlin.UIntArray.m6245getSizeimpl(iArr);
        }
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6561binarySearch2fe2U9s(iArr, i, i2, i3);
    }

    /* JADX INFO: renamed from: binarySearch-2fe2U9s, reason: not valid java name */
    public static final int m6561binarySearch2fe2U9s(int[] $this$binarySearch_u2d2fe2U9s, int element, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$binarySearch_u2d2fe2U9s, "$this$binarySearch");
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, com.android.server.permission.jarjar.kotlin.UIntArray.m6245getSizeimpl($this$binarySearch_u2d2fe2U9s));
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = $this$binarySearch_u2d2fe2U9s[mid];
            int cmp = com.android.server.permission.jarjar.kotlin.UnsignedKt.uintCompare(midVal, element);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    /* JADX INFO: renamed from: binarySearch-K6DWlUc$default, reason: not valid java name */
    public static /* synthetic */ int m6566binarySearchK6DWlUc$default(long[] jArr, long j, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = com.android.server.permission.jarjar.kotlin.ULongArray.m6324getSizeimpl(jArr);
        }
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6565binarySearchK6DWlUc(jArr, j, i, i2);
    }

    /* JADX INFO: renamed from: binarySearch-K6DWlUc, reason: not valid java name */
    public static final int m6565binarySearchK6DWlUc(long[] $this$binarySearch_u2dK6DWlUc, long element, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$binarySearch_u2dK6DWlUc, "$this$binarySearch");
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, com.android.server.permission.jarjar.kotlin.ULongArray.m6324getSizeimpl($this$binarySearch_u2dK6DWlUc));
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            long midVal = $this$binarySearch_u2dK6DWlUc[mid];
            int cmp = com.android.server.permission.jarjar.kotlin.UnsignedKt.ulongCompare(midVal, element);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    /* JADX INFO: renamed from: binarySearch-WpHrYlw$default, reason: not valid java name */
    public static /* synthetic */ int m6568binarySearchWpHrYlw$default(byte[] bArr, byte b, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = com.android.server.permission.jarjar.kotlin.UByteArray.m6166getSizeimpl(bArr);
        }
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6567binarySearchWpHrYlw(bArr, b, i, i2);
    }

    /* JADX INFO: renamed from: binarySearch-WpHrYlw, reason: not valid java name */
    public static final int m6567binarySearchWpHrYlw(byte[] $this$binarySearch_u2dWpHrYlw, byte element, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$binarySearch_u2dWpHrYlw, "$this$binarySearch");
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, com.android.server.permission.jarjar.kotlin.UByteArray.m6166getSizeimpl($this$binarySearch_u2dWpHrYlw));
        int signedElement = element & 255;
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            byte midVal = $this$binarySearch_u2dWpHrYlw[mid];
            int cmp = com.android.server.permission.jarjar.kotlin.UnsignedKt.uintCompare(midVal, signedElement);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    /* JADX INFO: renamed from: binarySearch-EtDCXyQ$default, reason: not valid java name */
    public static /* synthetic */ int m6564binarySearchEtDCXyQ$default(short[] sArr, short s, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = com.android.server.permission.jarjar.kotlin.UShortArray.m6429getSizeimpl(sArr);
        }
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6563binarySearchEtDCXyQ(sArr, s, i, i2);
    }

    /* JADX INFO: renamed from: binarySearch-EtDCXyQ, reason: not valid java name */
    public static final int m6563binarySearchEtDCXyQ(short[] $this$binarySearch_u2dEtDCXyQ, short element, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$binarySearch_u2dEtDCXyQ, "$this$binarySearch");
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, com.android.server.permission.jarjar.kotlin.UShortArray.m6429getSizeimpl($this$binarySearch_u2dEtDCXyQ));
        int signedElement = 65535 & element;
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            short midVal = $this$binarySearch_u2dEtDCXyQ[mid];
            int cmp = com.android.server.permission.jarjar.kotlin.UnsignedKt.uintCompare(midVal, signedElement);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    /* JADX INFO: renamed from: max--ajY-9A, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UInt m6573maxajY9A(int[] $this$max_u2d_u2dajY_u2d9A) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max_u2d_u2dajY_u2d9A, "$this$max");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6941maxOrNullajY9A($this$max_u2d_u2dajY_u2d9A);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    /* JADX INFO: renamed from: max-QwZRm1k, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.ULong m6575maxQwZRm1k(long[] $this$max_u2dQwZRm1k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max_u2dQwZRm1k, "$this$max");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6943maxOrNullQwZRm1k($this$max_u2dQwZRm1k);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    /* JADX INFO: renamed from: max-GBYM_sE, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UByte m6574maxGBYM_sE(byte[] $this$max_u2dGBYM_sE) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max_u2dGBYM_sE, "$this$max");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6942maxOrNullGBYM_sE($this$max_u2dGBYM_sE);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    /* JADX INFO: renamed from: max-rL5Bavg, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UShort m6576maxrL5Bavg(short[] $this$max_u2drL5Bavg) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max_u2drL5Bavg, "$this$max");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6944maxOrNullrL5Bavg($this$max_u2drL5Bavg);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    /* JADX INFO: renamed from: maxBy-jgv0xPQ, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> com.android.server.permission.jarjar.kotlin.UInt m6579maxByjgv0xPQ(int[] $this$maxBy_u2djgv0xPQ, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UInt, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxBy_u2djgv0xPQ, "$this$maxBy");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        if (com.android.server.permission.jarjar.kotlin.UIntArray.m6247isEmptyimpl($this$maxBy_u2djgv0xPQ)) {
            return null;
        }
        int iM6244getpVg5ArA = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA($this$maxBy_u2djgv0xPQ, 0);
        int lastIndex = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex($this$maxBy_u2djgv0xPQ);
        if (lastIndex != 0) {
            R rInvoke = function1.invoke(com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(iM6244getpVg5ArA));
            ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(1, lastIndex).iterator2();
            while (it.hasNext()) {
                int iM6244getpVg5ArA2 = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA($this$maxBy_u2djgv0xPQ, it.nextInt());
                R rInvoke2 = function1.invoke(com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(iM6244getpVg5ArA2));
                if (rInvoke.compareTo(rInvoke2) < 0) {
                    iM6244getpVg5ArA = iM6244getpVg5ArA2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(iM6244getpVg5ArA);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    /* JADX INFO: renamed from: maxBy-MShoTSo, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> com.android.server.permission.jarjar.kotlin.ULong m6578maxByMShoTSo(long[] $this$maxBy_u2dMShoTSo, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.ULong, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxBy_u2dMShoTSo, "$this$maxBy");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        if (com.android.server.permission.jarjar.kotlin.ULongArray.m6326isEmptyimpl($this$maxBy_u2dMShoTSo)) {
            return null;
        }
        long jM6323getsVKNKU = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU($this$maxBy_u2dMShoTSo, 0);
        int lastIndex = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex($this$maxBy_u2dMShoTSo);
        if (lastIndex != 0) {
            R rInvoke = function1.invoke(com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(jM6323getsVKNKU));
            ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(1, lastIndex).iterator2();
            while (it.hasNext()) {
                long jM6323getsVKNKU2 = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU($this$maxBy_u2dMShoTSo, it.nextInt());
                R rInvoke2 = function1.invoke(com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(jM6323getsVKNKU2));
                if (rInvoke.compareTo(rInvoke2) < 0) {
                    jM6323getsVKNKU = jM6323getsVKNKU2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(jM6323getsVKNKU);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    /* JADX INFO: renamed from: maxBy-JOV_ifY, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> com.android.server.permission.jarjar.kotlin.UByte m6577maxByJOV_ifY(byte[] $this$maxBy_u2dJOV_ifY, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UByte, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxBy_u2dJOV_ifY, "$this$maxBy");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        if (com.android.server.permission.jarjar.kotlin.UByteArray.m6168isEmptyimpl($this$maxBy_u2dJOV_ifY)) {
            return null;
        }
        byte bM6165getw2LRezQ = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ($this$maxBy_u2dJOV_ifY, 0);
        int lastIndex = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex($this$maxBy_u2dJOV_ifY);
        if (lastIndex != 0) {
            R rInvoke = function1.invoke(com.android.server.permission.jarjar.kotlin.UByte.m6101boximpl(bM6165getw2LRezQ));
            ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(1, lastIndex).iterator2();
            while (it.hasNext()) {
                byte bM6165getw2LRezQ2 = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ($this$maxBy_u2dJOV_ifY, it.nextInt());
                R rInvoke2 = function1.invoke(com.android.server.permission.jarjar.kotlin.UByte.m6101boximpl(bM6165getw2LRezQ2));
                if (rInvoke.compareTo(rInvoke2) < 0) {
                    bM6165getw2LRezQ = bM6165getw2LRezQ2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return com.android.server.permission.jarjar.kotlin.UByte.m6101boximpl(bM6165getw2LRezQ);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    /* JADX INFO: renamed from: maxBy-xTcfx_M, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> com.android.server.permission.jarjar.kotlin.UShort m6580maxByxTcfx_M(short[] $this$maxBy_u2dxTcfx_M, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UShort, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxBy_u2dxTcfx_M, "$this$maxBy");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        if (com.android.server.permission.jarjar.kotlin.UShortArray.m6431isEmptyimpl($this$maxBy_u2dxTcfx_M)) {
            return null;
        }
        short sM6428getMh2AYeg = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg($this$maxBy_u2dxTcfx_M, 0);
        int lastIndex = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex($this$maxBy_u2dxTcfx_M);
        if (lastIndex != 0) {
            R rInvoke = function1.invoke(com.android.server.permission.jarjar.kotlin.UShort.m6364boximpl(sM6428getMh2AYeg));
            ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(1, lastIndex).iterator2();
            while (it.hasNext()) {
                short sM6428getMh2AYeg2 = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg($this$maxBy_u2dxTcfx_M, it.nextInt());
                R rInvoke2 = function1.invoke(com.android.server.permission.jarjar.kotlin.UShort.m6364boximpl(sM6428getMh2AYeg2));
                if (rInvoke.compareTo(rInvoke2) < 0) {
                    sM6428getMh2AYeg = sM6428getMh2AYeg2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return com.android.server.permission.jarjar.kotlin.UShort.m6364boximpl(sM6428getMh2AYeg);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    /* JADX INFO: renamed from: maxWith-YmdZ_VM, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UInt m6582maxWithYmdZ_VM(int[] $this$maxWith_u2dYmdZ_VM, java.util.Comparator comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxWith_u2dYmdZ_VM, "$this$maxWith");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6950maxWithOrNullYmdZ_VM($this$maxWith_u2dYmdZ_VM, comparator);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    /* JADX INFO: renamed from: maxWith-zrEWJaI, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.ULong m6584maxWithzrEWJaI(long[] $this$maxWith_u2dzrEWJaI, java.util.Comparator comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxWith_u2dzrEWJaI, "$this$maxWith");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6952maxWithOrNullzrEWJaI($this$maxWith_u2dzrEWJaI, comparator);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    /* JADX INFO: renamed from: maxWith-XMRcp5o, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UByte m6581maxWithXMRcp5o(byte[] $this$maxWith_u2dXMRcp5o, java.util.Comparator comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxWith_u2dXMRcp5o, "$this$maxWith");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6949maxWithOrNullXMRcp5o($this$maxWith_u2dXMRcp5o, comparator);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    /* JADX INFO: renamed from: maxWith-eOHTfZs, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UShort m6583maxWitheOHTfZs(short[] $this$maxWith_u2deOHTfZs, java.util.Comparator comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxWith_u2deOHTfZs, "$this$maxWith");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6951maxWithOrNulleOHTfZs($this$maxWith_u2deOHTfZs, comparator);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    /* JADX INFO: renamed from: min--ajY-9A, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UInt m6585minajY9A(int[] $this$min_u2d_u2dajY_u2d9A) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min_u2d_u2dajY_u2d9A, "$this$min");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6997minOrNullajY9A($this$min_u2d_u2dajY_u2d9A);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    /* JADX INFO: renamed from: min-QwZRm1k, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.ULong m6587minQwZRm1k(long[] $this$min_u2dQwZRm1k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min_u2dQwZRm1k, "$this$min");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6999minOrNullQwZRm1k($this$min_u2dQwZRm1k);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    /* JADX INFO: renamed from: min-GBYM_sE, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UByte m6586minGBYM_sE(byte[] $this$min_u2dGBYM_sE) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min_u2dGBYM_sE, "$this$min");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6998minOrNullGBYM_sE($this$min_u2dGBYM_sE);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    /* JADX INFO: renamed from: min-rL5Bavg, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UShort m6588minrL5Bavg(short[] $this$min_u2drL5Bavg) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min_u2drL5Bavg, "$this$min");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m7000minOrNullrL5Bavg($this$min_u2drL5Bavg);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    /* JADX INFO: renamed from: minBy-jgv0xPQ, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> com.android.server.permission.jarjar.kotlin.UInt m6591minByjgv0xPQ(int[] $this$minBy_u2djgv0xPQ, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UInt, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minBy_u2djgv0xPQ, "$this$minBy");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        if (com.android.server.permission.jarjar.kotlin.UIntArray.m6247isEmptyimpl($this$minBy_u2djgv0xPQ)) {
            return null;
        }
        int iM6244getpVg5ArA = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA($this$minBy_u2djgv0xPQ, 0);
        int lastIndex = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex($this$minBy_u2djgv0xPQ);
        if (lastIndex != 0) {
            R rInvoke = function1.invoke(com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(iM6244getpVg5ArA));
            ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(1, lastIndex).iterator2();
            while (it.hasNext()) {
                int iM6244getpVg5ArA2 = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA($this$minBy_u2djgv0xPQ, it.nextInt());
                R rInvoke2 = function1.invoke(com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(iM6244getpVg5ArA2));
                if (rInvoke.compareTo(rInvoke2) > 0) {
                    iM6244getpVg5ArA = iM6244getpVg5ArA2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(iM6244getpVg5ArA);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    /* JADX INFO: renamed from: minBy-MShoTSo, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> com.android.server.permission.jarjar.kotlin.ULong m6590minByMShoTSo(long[] $this$minBy_u2dMShoTSo, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.ULong, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minBy_u2dMShoTSo, "$this$minBy");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        if (com.android.server.permission.jarjar.kotlin.ULongArray.m6326isEmptyimpl($this$minBy_u2dMShoTSo)) {
            return null;
        }
        long jM6323getsVKNKU = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU($this$minBy_u2dMShoTSo, 0);
        int lastIndex = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex($this$minBy_u2dMShoTSo);
        if (lastIndex != 0) {
            R rInvoke = function1.invoke(com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(jM6323getsVKNKU));
            ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(1, lastIndex).iterator2();
            while (it.hasNext()) {
                long jM6323getsVKNKU2 = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU($this$minBy_u2dMShoTSo, it.nextInt());
                R rInvoke2 = function1.invoke(com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(jM6323getsVKNKU2));
                if (rInvoke.compareTo(rInvoke2) > 0) {
                    jM6323getsVKNKU = jM6323getsVKNKU2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(jM6323getsVKNKU);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    /* JADX INFO: renamed from: minBy-JOV_ifY, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> com.android.server.permission.jarjar.kotlin.UByte m6589minByJOV_ifY(byte[] $this$minBy_u2dJOV_ifY, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UByte, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minBy_u2dJOV_ifY, "$this$minBy");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        if (com.android.server.permission.jarjar.kotlin.UByteArray.m6168isEmptyimpl($this$minBy_u2dJOV_ifY)) {
            return null;
        }
        byte bM6165getw2LRezQ = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ($this$minBy_u2dJOV_ifY, 0);
        int lastIndex = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex($this$minBy_u2dJOV_ifY);
        if (lastIndex != 0) {
            R rInvoke = function1.invoke(com.android.server.permission.jarjar.kotlin.UByte.m6101boximpl(bM6165getw2LRezQ));
            ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(1, lastIndex).iterator2();
            while (it.hasNext()) {
                byte bM6165getw2LRezQ2 = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ($this$minBy_u2dJOV_ifY, it.nextInt());
                R rInvoke2 = function1.invoke(com.android.server.permission.jarjar.kotlin.UByte.m6101boximpl(bM6165getw2LRezQ2));
                if (rInvoke.compareTo(rInvoke2) > 0) {
                    bM6165getw2LRezQ = bM6165getw2LRezQ2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return com.android.server.permission.jarjar.kotlin.UByte.m6101boximpl(bM6165getw2LRezQ);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    /* JADX INFO: renamed from: minBy-xTcfx_M, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> com.android.server.permission.jarjar.kotlin.UShort m6592minByxTcfx_M(short[] $this$minBy_u2dxTcfx_M, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UShort, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minBy_u2dxTcfx_M, "$this$minBy");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        if (com.android.server.permission.jarjar.kotlin.UShortArray.m6431isEmptyimpl($this$minBy_u2dxTcfx_M)) {
            return null;
        }
        short sM6428getMh2AYeg = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg($this$minBy_u2dxTcfx_M, 0);
        int lastIndex = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex($this$minBy_u2dxTcfx_M);
        if (lastIndex != 0) {
            R rInvoke = function1.invoke(com.android.server.permission.jarjar.kotlin.UShort.m6364boximpl(sM6428getMh2AYeg));
            ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(1, lastIndex).iterator2();
            while (it.hasNext()) {
                short sM6428getMh2AYeg2 = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg($this$minBy_u2dxTcfx_M, it.nextInt());
                R rInvoke2 = function1.invoke(com.android.server.permission.jarjar.kotlin.UShort.m6364boximpl(sM6428getMh2AYeg2));
                if (rInvoke.compareTo(rInvoke2) > 0) {
                    sM6428getMh2AYeg = sM6428getMh2AYeg2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return com.android.server.permission.jarjar.kotlin.UShort.m6364boximpl(sM6428getMh2AYeg);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    /* JADX INFO: renamed from: minWith-YmdZ_VM, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UInt m6594minWithYmdZ_VM(int[] $this$minWith_u2dYmdZ_VM, java.util.Comparator comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minWith_u2dYmdZ_VM, "$this$minWith");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m7006minWithOrNullYmdZ_VM($this$minWith_u2dYmdZ_VM, comparator);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    /* JADX INFO: renamed from: minWith-zrEWJaI, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.ULong m6596minWithzrEWJaI(long[] $this$minWith_u2dzrEWJaI, java.util.Comparator comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minWith_u2dzrEWJaI, "$this$minWith");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m7008minWithOrNullzrEWJaI($this$minWith_u2dzrEWJaI, comparator);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    /* JADX INFO: renamed from: minWith-XMRcp5o, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UByte m6593minWithXMRcp5o(byte[] $this$minWith_u2dXMRcp5o, java.util.Comparator comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minWith_u2dXMRcp5o, "$this$minWith");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m7005minWithOrNullXMRcp5o($this$minWith_u2dXMRcp5o, comparator);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    /* JADX INFO: renamed from: minWith-eOHTfZs, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UShort m6595minWitheOHTfZs(short[] $this$minWith_u2deOHTfZs, java.util.Comparator comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minWith_u2deOHTfZs, "$this$minWith");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m7007minWithOrNulleOHTfZs($this$minWith_u2deOHTfZs, comparator);
    }

    private static final java.math.BigDecimal sumOfBigDecimal(int[] $this$sumOf_u2djgv0xPQ, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UInt, ? extends java.math.BigDecimal> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf_u2djgv0xPQ, "$this$sumOf");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM6245getSizeimpl = com.android.server.permission.jarjar.kotlin.UIntArray.m6245getSizeimpl($this$sumOf_u2djgv0xPQ);
        for (int i = 0; i < iM6245getSizeimpl; i++) {
            int element = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA($this$sumOf_u2djgv0xPQ, i);
            java.math.BigDecimal bigDecimalAdd = sum.add(function1.invoke(com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(element)));
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final java.math.BigDecimal sumOfBigDecimal(long[] $this$sumOf_u2dMShoTSo, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.ULong, ? extends java.math.BigDecimal> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf_u2dMShoTSo, "$this$sumOf");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM6324getSizeimpl = com.android.server.permission.jarjar.kotlin.ULongArray.m6324getSizeimpl($this$sumOf_u2dMShoTSo);
        for (int i = 0; i < iM6324getSizeimpl; i++) {
            long element = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU($this$sumOf_u2dMShoTSo, i);
            java.math.BigDecimal bigDecimalAdd = sum.add(function1.invoke(com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(element)));
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final java.math.BigDecimal sumOfBigDecimal(byte[] $this$sumOf_u2dJOV_ifY, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UByte, ? extends java.math.BigDecimal> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf_u2dJOV_ifY, "$this$sumOf");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM6166getSizeimpl = com.android.server.permission.jarjar.kotlin.UByteArray.m6166getSizeimpl($this$sumOf_u2dJOV_ifY);
        for (int i = 0; i < iM6166getSizeimpl; i++) {
            byte element = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ($this$sumOf_u2dJOV_ifY, i);
            java.math.BigDecimal bigDecimalAdd = sum.add(function1.invoke(com.android.server.permission.jarjar.kotlin.UByte.m6101boximpl(element)));
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final java.math.BigDecimal sumOfBigDecimal(short[] $this$sumOf_u2dxTcfx_M, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UShort, ? extends java.math.BigDecimal> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf_u2dxTcfx_M, "$this$sumOf");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM6429getSizeimpl = com.android.server.permission.jarjar.kotlin.UShortArray.m6429getSizeimpl($this$sumOf_u2dxTcfx_M);
        for (int i = 0; i < iM6429getSizeimpl; i++) {
            short element = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg($this$sumOf_u2dxTcfx_M, i);
            java.math.BigDecimal bigDecimalAdd = sum.add(function1.invoke(com.android.server.permission.jarjar.kotlin.UShort.m6364boximpl(element)));
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final java.math.BigInteger sumOfBigInteger(int[] $this$sumOf_u2djgv0xPQ, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UInt, ? extends java.math.BigInteger> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf_u2djgv0xPQ, "$this$sumOf");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM6245getSizeimpl = com.android.server.permission.jarjar.kotlin.UIntArray.m6245getSizeimpl($this$sumOf_u2djgv0xPQ);
        for (int i = 0; i < iM6245getSizeimpl; i++) {
            int element = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA($this$sumOf_u2djgv0xPQ, i);
            java.math.BigInteger bigIntegerAdd = sum.add(function1.invoke(com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(element)));
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }

    private static final java.math.BigInteger sumOfBigInteger(long[] $this$sumOf_u2dMShoTSo, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.ULong, ? extends java.math.BigInteger> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf_u2dMShoTSo, "$this$sumOf");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM6324getSizeimpl = com.android.server.permission.jarjar.kotlin.ULongArray.m6324getSizeimpl($this$sumOf_u2dMShoTSo);
        for (int i = 0; i < iM6324getSizeimpl; i++) {
            long element = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU($this$sumOf_u2dMShoTSo, i);
            java.math.BigInteger bigIntegerAdd = sum.add(function1.invoke(com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(element)));
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }

    private static final java.math.BigInteger sumOfBigInteger(byte[] $this$sumOf_u2dJOV_ifY, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UByte, ? extends java.math.BigInteger> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf_u2dJOV_ifY, "$this$sumOf");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM6166getSizeimpl = com.android.server.permission.jarjar.kotlin.UByteArray.m6166getSizeimpl($this$sumOf_u2dJOV_ifY);
        for (int i = 0; i < iM6166getSizeimpl; i++) {
            byte element = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ($this$sumOf_u2dJOV_ifY, i);
            java.math.BigInteger bigIntegerAdd = sum.add(function1.invoke(com.android.server.permission.jarjar.kotlin.UByte.m6101boximpl(element)));
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }

    private static final java.math.BigInteger sumOfBigInteger(short[] $this$sumOf_u2dxTcfx_M, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.UShort, ? extends java.math.BigInteger> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf_u2dxTcfx_M, "$this$sumOf");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM6429getSizeimpl = com.android.server.permission.jarjar.kotlin.UShortArray.m6429getSizeimpl($this$sumOf_u2dxTcfx_M);
        for (int i = 0; i < iM6429getSizeimpl; i++) {
            short element = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg($this$sumOf_u2dxTcfx_M, i);
            java.math.BigInteger bigIntegerAdd = sum.add(function1.invoke(com.android.server.permission.jarjar.kotlin.UShort.m6364boximpl(element)));
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }
}
