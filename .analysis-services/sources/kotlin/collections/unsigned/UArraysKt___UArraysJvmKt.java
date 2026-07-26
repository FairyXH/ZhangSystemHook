package kotlin.collections.unsigned;

/* JADX INFO: compiled from: _UArraysJvm.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000h\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b \n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0019\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u0019\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u0007H\u0007¢\u0006\u0004\b\b\u0010\t\u001a\u0019\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\n0\u0001*\u00020\u000bH\u0007¢\u0006\u0004\b\f\u0010\r\u001a\u0019\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0001*\u00020\u000fH\u0007¢\u0006\u0004\b\u0010\u0010\u0011\u001a/\u0010\u0012\u001a\u00020\u0013*\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007¢\u0006\u0004\b\u0017\u0010\u0018\u001a/\u0010\u0012\u001a\u00020\u0013*\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00062\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007¢\u0006\u0004\b\u0019\u0010\u001a\u001a/\u0010\u0012\u001a\u00020\u0013*\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007¢\u0006\u0004\b\u001b\u0010\u001c\u001a/\u0010\u0012\u001a\u00020\u0013*\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007¢\u0006\u0004\b\u001d\u0010\u001e\u001a\u001c\u0010\u001f\u001a\u00020\u0002*\u00020\u00032\u0006\u0010 \u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b!\u0010\"\u001a\u001c\u0010\u001f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010 \u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b#\u0010$\u001a\u001c\u0010\u001f\u001a\u00020\n*\u00020\u000b2\u0006\u0010 \u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b%\u0010&\u001a\u001c\u0010\u001f\u001a\u00020\u000e*\u00020\u000f2\u0006\u0010 \u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b'\u0010(\u001a\u0015\u0010)\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007¢\u0006\u0004\b*\u0010+\u001a\u0015\u0010)\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007¢\u0006\u0004\b,\u0010-\u001a\u0015\u0010)\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007¢\u0006\u0004\b.\u0010/\u001a\u0015\u0010)\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007¢\u0006\u0004\b0\u00101\u001a=\u00102\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\b7\u00108\u001a=\u00102\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\b9\u0010:\u001a=\u00102\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\b;\u0010<\u001a=\u00102\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\b=\u0010>\u001a1\u0010?\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007¢\u0006\u0004\bC\u0010D\u001a1\u0010?\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007¢\u0006\u0004\bE\u0010F\u001a1\u0010?\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007¢\u0006\u0004\bG\u0010H\u001a1\u0010?\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007¢\u0006\u0004\bI\u0010J\u001a\u0015\u0010K\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007¢\u0006\u0004\bL\u0010+\u001a\u0015\u0010K\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007¢\u0006\u0004\bM\u0010-\u001a\u0015\u0010K\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007¢\u0006\u0004\bN\u0010/\u001a\u0015\u0010K\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007¢\u0006\u0004\bO\u00101\u001a=\u0010P\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\bQ\u00108\u001a=\u0010P\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\bR\u0010:\u001a=\u0010P\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\bS\u0010<\u001a=\u0010P\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0000¢\u0006\u0004\bT\u0010>\u001a1\u0010U\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007¢\u0006\u0004\bV\u0010D\u001a1\u0010U\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007¢\u0006\u0004\bW\u0010F\u001a1\u0010U\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007¢\u0006\u0004\bX\u0010H\u001a1\u0010U\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007¢\u0006\u0004\bY\u0010J\u001a+\u0010Z\u001a\u00020[*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0000¢\u0006\u0004\b\\\u0010]\u001a+\u0010Z\u001a\u00020^*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010`\u001a+\u0010Z\u001a\u00020[*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0000¢\u0006\u0004\b\\\u0010a\u001a+\u0010Z\u001a\u00020^*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010b\u001a+\u0010Z\u001a\u00020[*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0000¢\u0006\u0004\b\\\u0010c\u001a+\u0010Z\u001a\u00020^*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010d\u001a+\u0010Z\u001a\u00020[*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0000¢\u0006\u0004\b\\\u0010e\u001a+\u0010Z\u001a\u00020^*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010f\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006g"}, d2 = {"asList", "", "Lkotlin/UByte;", "Lkotlin/UByteArray;", "asList-GBYM_sE", "([B)Ljava/util/List;", "Lkotlin/UInt;", "Lkotlin/UIntArray;", "asList--ajY-9A", "([I)Ljava/util/List;", "Lkotlin/ULong;", "Lkotlin/ULongArray;", "asList-QwZRm1k", "([J)Ljava/util/List;", "Lkotlin/UShort;", "Lkotlin/UShortArray;", "asList-rL5Bavg", "([S)Ljava/util/List;", "binarySearch", "", "element", "fromIndex", "toIndex", "binarySearch-WpHrYlw", "([BBII)I", "binarySearch-2fe2U9s", "([IIII)I", "binarySearch-K6DWlUc", "([JJII)I", "binarySearch-EtDCXyQ", "([SSII)I", "elementAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "elementAt-PpDY95g", "([BI)B", "elementAt-qFRl0hI", "([II)I", "elementAt-r7IrZao", "([JI)J", "elementAt-nggk6HY", "([SI)S", "max", "max-GBYM_sE", "([B)Lkotlin/UByte;", "max--ajY-9A", "([I)Lkotlin/UInt;", "max-QwZRm1k", "([J)Lkotlin/ULong;", "max-rL5Bavg", "([S)Lkotlin/UShort;", "maxBy", "R", "", "selector", "Lkotlin/Function1;", "maxBy-JOV_ifY", "([BLkotlin/jvm/functions/Function1;)Lkotlin/UByte;", "maxBy-jgv0xPQ", "([ILkotlin/jvm/functions/Function1;)Lkotlin/UInt;", "maxBy-MShoTSo", "([JLkotlin/jvm/functions/Function1;)Lkotlin/ULong;", "maxBy-xTcfx_M", "([SLkotlin/jvm/functions/Function1;)Lkotlin/UShort;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "maxWith-XMRcp5o", "([BLjava/util/Comparator;)Lkotlin/UByte;", "maxWith-YmdZ_VM", "([ILjava/util/Comparator;)Lkotlin/UInt;", "maxWith-zrEWJaI", "([JLjava/util/Comparator;)Lkotlin/ULong;", "maxWith-eOHTfZs", "([SLjava/util/Comparator;)Lkotlin/UShort;", "min", "min-GBYM_sE", "min--ajY-9A", "min-QwZRm1k", "min-rL5Bavg", "minBy", "minBy-JOV_ifY", "minBy-jgv0xPQ", "minBy-MShoTSo", "minBy-xTcfx_M", "minWith", "minWith-XMRcp5o", "minWith-YmdZ_VM", "minWith-zrEWJaI", "minWith-eOHTfZs", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "Ljava/math/BigInteger;", "sumOfBigInteger", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, pn = "kotlin.collections", xi = 49, xs = "kotlin/collections/unsigned/UArraysKt")
class UArraysKt___UArraysJvmKt {
    /* JADX INFO: renamed from: elementAt-qFRl0hI, reason: not valid java name */
    private static final int m11789elementAtqFRl0hI(int[] elementAt, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elementAt, "$this$elementAt");
        return kotlin.UIntArray.m11462getpVg5ArA(elementAt, index);
    }

    /* JADX INFO: renamed from: elementAt-r7IrZao, reason: not valid java name */
    private static final long m11790elementAtr7IrZao(long[] elementAt, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elementAt, "$this$elementAt");
        return kotlin.ULongArray.m11541getsVKNKU(elementAt, index);
    }

    /* JADX INFO: renamed from: elementAt-PpDY95g, reason: not valid java name */
    private static final byte m11787elementAtPpDY95g(byte[] elementAt, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elementAt, "$this$elementAt");
        return kotlin.UByteArray.m11383getw2LRezQ(elementAt, index);
    }

    /* JADX INFO: renamed from: elementAt-nggk6HY, reason: not valid java name */
    private static final short m11788elementAtnggk6HY(short[] elementAt, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elementAt, "$this$elementAt");
        return kotlin.UShortArray.m11646getMh2AYeg(elementAt, index);
    }

    /* JADX INFO: renamed from: asList--ajY-9A, reason: not valid java name */
    public static final java.util.List<kotlin.UInt> m11775asListajY9A(int[] asList) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(asList, "$this$asList");
        return new kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$1(asList);
    }

    /* JADX INFO: renamed from: asList-QwZRm1k, reason: not valid java name */
    public static final java.util.List<kotlin.ULong> m11777asListQwZRm1k(long[] asList) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(asList, "$this$asList");
        return new kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$2(asList);
    }

    /* JADX INFO: renamed from: asList-GBYM_sE, reason: not valid java name */
    public static final java.util.List<kotlin.UByte> m11776asListGBYM_sE(byte[] asList) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(asList, "$this$asList");
        return new kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$3(asList);
    }

    /* JADX INFO: renamed from: asList-rL5Bavg, reason: not valid java name */
    public static final java.util.List<kotlin.UShort> m11778asListrL5Bavg(short[] asList) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(asList, "$this$asList");
        return new kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$4(asList);
    }

    /* JADX INFO: renamed from: binarySearch-2fe2U9s$default, reason: not valid java name */
    public static /* synthetic */ int m11780binarySearch2fe2U9s$default(int[] iArr, int i, int i2, int i3, int i4, java.lang.Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = kotlin.UIntArray.m11463getSizeimpl(iArr);
        }
        return kotlin.collections.unsigned.UArraysKt.m11779binarySearch2fe2U9s(iArr, i, i2, i3);
    }

    /* JADX INFO: renamed from: binarySearch-2fe2U9s, reason: not valid java name */
    public static final int m11779binarySearch2fe2U9s(int[] binarySearch, int element, int fromIndex, int toIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(binarySearch, "$this$binarySearch");
        kotlin.collections.AbstractList.INSTANCE.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, kotlin.UIntArray.m11463getSizeimpl(binarySearch));
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = binarySearch[mid];
            int cmp = kotlin.UnsignedKt.uintCompare(midVal, element);
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
    public static /* synthetic */ int m11784binarySearchK6DWlUc$default(long[] jArr, long j, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = kotlin.ULongArray.m11542getSizeimpl(jArr);
        }
        return kotlin.collections.unsigned.UArraysKt.m11783binarySearchK6DWlUc(jArr, j, i, i2);
    }

    /* JADX INFO: renamed from: binarySearch-K6DWlUc, reason: not valid java name */
    public static final int m11783binarySearchK6DWlUc(long[] binarySearch, long element, int fromIndex, int toIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(binarySearch, "$this$binarySearch");
        kotlin.collections.AbstractList.INSTANCE.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, kotlin.ULongArray.m11542getSizeimpl(binarySearch));
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            long midVal = binarySearch[mid];
            int cmp = kotlin.UnsignedKt.ulongCompare(midVal, element);
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
    public static /* synthetic */ int m11786binarySearchWpHrYlw$default(byte[] bArr, byte b, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = kotlin.UByteArray.m11384getSizeimpl(bArr);
        }
        return kotlin.collections.unsigned.UArraysKt.m11785binarySearchWpHrYlw(bArr, b, i, i2);
    }

    /* JADX INFO: renamed from: binarySearch-WpHrYlw, reason: not valid java name */
    public static final int m11785binarySearchWpHrYlw(byte[] binarySearch, byte element, int fromIndex, int toIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(binarySearch, "$this$binarySearch");
        kotlin.collections.AbstractList.INSTANCE.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, kotlin.UByteArray.m11384getSizeimpl(binarySearch));
        int signedElement = element & 255;
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            byte midVal = binarySearch[mid];
            int cmp = kotlin.UnsignedKt.uintCompare(midVal, signedElement);
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
    public static /* synthetic */ int m11782binarySearchEtDCXyQ$default(short[] sArr, short s, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = kotlin.UShortArray.m11647getSizeimpl(sArr);
        }
        return kotlin.collections.unsigned.UArraysKt.m11781binarySearchEtDCXyQ(sArr, s, i, i2);
    }

    /* JADX INFO: renamed from: binarySearch-EtDCXyQ, reason: not valid java name */
    public static final int m11781binarySearchEtDCXyQ(short[] binarySearch, short element, int fromIndex, int toIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(binarySearch, "$this$binarySearch");
        kotlin.collections.AbstractList.INSTANCE.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, kotlin.UShortArray.m11647getSizeimpl(binarySearch));
        int signedElement = 65535 & element;
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            short midVal = binarySearch[mid];
            int cmp = kotlin.UnsignedKt.uintCompare(midVal, signedElement);
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

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: max--ajY-9A, reason: not valid java name */
    public static final /* synthetic */ kotlin.UInt m11791maxajY9A(int[] max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(max, "$this$max");
        return kotlin.collections.unsigned.UArraysKt.m12159maxOrNullajY9A(max);
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: max-QwZRm1k, reason: not valid java name */
    public static final /* synthetic */ kotlin.ULong m11793maxQwZRm1k(long[] max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(max, "$this$max");
        return kotlin.collections.unsigned.UArraysKt.m12161maxOrNullQwZRm1k(max);
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: max-GBYM_sE, reason: not valid java name */
    public static final /* synthetic */ kotlin.UByte m11792maxGBYM_sE(byte[] max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(max, "$this$max");
        return kotlin.collections.unsigned.UArraysKt.m12160maxOrNullGBYM_sE(max);
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: max-rL5Bavg, reason: not valid java name */
    public static final /* synthetic */ kotlin.UShort m11794maxrL5Bavg(short[] max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(max, "$this$max");
        return kotlin.collections.unsigned.UArraysKt.m12162maxOrNullrL5Bavg(max);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: maxBy-jgv0xPQ, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> kotlin.UInt m11797maxByjgv0xPQ(int[] maxBy, kotlin.jvm.functions.Function1<? super kotlin.UInt, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(maxBy, "$this$maxBy");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if (kotlin.UIntArray.m11465isEmptyimpl(maxBy)) {
            return null;
        }
        int iM11462getpVg5ArA = kotlin.UIntArray.m11462getpVg5ArA(maxBy, 0);
        int lastIndex = kotlin.collections.ArraysKt.getLastIndex(maxBy);
        if (lastIndex != 0) {
            R rInvoke = selector.invoke(kotlin.UInt.m11396boximpl(iM11462getpVg5ArA));
            ?? it = new kotlin.ranges.IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                int iM11462getpVg5ArA2 = kotlin.UIntArray.m11462getpVg5ArA(maxBy, it.nextInt());
                R rInvoke2 = selector.invoke(kotlin.UInt.m11396boximpl(iM11462getpVg5ArA2));
                if (rInvoke.compareTo(rInvoke2) < 0) {
                    iM11462getpVg5ArA = iM11462getpVg5ArA2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return kotlin.UInt.m11396boximpl(iM11462getpVg5ArA);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: maxBy-MShoTSo, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> kotlin.ULong m11796maxByMShoTSo(long[] maxBy, kotlin.jvm.functions.Function1<? super kotlin.ULong, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(maxBy, "$this$maxBy");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if (kotlin.ULongArray.m11544isEmptyimpl(maxBy)) {
            return null;
        }
        long jM11541getsVKNKU = kotlin.ULongArray.m11541getsVKNKU(maxBy, 0);
        int lastIndex = kotlin.collections.ArraysKt.getLastIndex(maxBy);
        if (lastIndex != 0) {
            R rInvoke = selector.invoke(kotlin.ULong.m11475boximpl(jM11541getsVKNKU));
            ?? it = new kotlin.ranges.IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                long jM11541getsVKNKU2 = kotlin.ULongArray.m11541getsVKNKU(maxBy, it.nextInt());
                R rInvoke2 = selector.invoke(kotlin.ULong.m11475boximpl(jM11541getsVKNKU2));
                if (rInvoke.compareTo(rInvoke2) < 0) {
                    jM11541getsVKNKU = jM11541getsVKNKU2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return kotlin.ULong.m11475boximpl(jM11541getsVKNKU);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: maxBy-JOV_ifY, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> kotlin.UByte m11795maxByJOV_ifY(byte[] maxBy, kotlin.jvm.functions.Function1<? super kotlin.UByte, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(maxBy, "$this$maxBy");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if (kotlin.UByteArray.m11386isEmptyimpl(maxBy)) {
            return null;
        }
        byte bM11383getw2LRezQ = kotlin.UByteArray.m11383getw2LRezQ(maxBy, 0);
        int lastIndex = kotlin.collections.ArraysKt.getLastIndex(maxBy);
        if (lastIndex != 0) {
            R rInvoke = selector.invoke(kotlin.UByte.m11319boximpl(bM11383getw2LRezQ));
            ?? it = new kotlin.ranges.IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                byte bM11383getw2LRezQ2 = kotlin.UByteArray.m11383getw2LRezQ(maxBy, it.nextInt());
                R rInvoke2 = selector.invoke(kotlin.UByte.m11319boximpl(bM11383getw2LRezQ2));
                if (rInvoke.compareTo(rInvoke2) < 0) {
                    bM11383getw2LRezQ = bM11383getw2LRezQ2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return kotlin.UByte.m11319boximpl(bM11383getw2LRezQ);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: maxBy-xTcfx_M, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> kotlin.UShort m11798maxByxTcfx_M(short[] maxBy, kotlin.jvm.functions.Function1<? super kotlin.UShort, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(maxBy, "$this$maxBy");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if (kotlin.UShortArray.m11649isEmptyimpl(maxBy)) {
            return null;
        }
        short sM11646getMh2AYeg = kotlin.UShortArray.m11646getMh2AYeg(maxBy, 0);
        int lastIndex = kotlin.collections.ArraysKt.getLastIndex(maxBy);
        if (lastIndex != 0) {
            R rInvoke = selector.invoke(kotlin.UShort.m11582boximpl(sM11646getMh2AYeg));
            ?? it = new kotlin.ranges.IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                short sM11646getMh2AYeg2 = kotlin.UShortArray.m11646getMh2AYeg(maxBy, it.nextInt());
                R rInvoke2 = selector.invoke(kotlin.UShort.m11582boximpl(sM11646getMh2AYeg2));
                if (rInvoke.compareTo(rInvoke2) < 0) {
                    sM11646getMh2AYeg = sM11646getMh2AYeg2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return kotlin.UShort.m11582boximpl(sM11646getMh2AYeg);
    }

    @kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: maxWith-YmdZ_VM, reason: not valid java name */
    public static final /* synthetic */ kotlin.UInt m11800maxWithYmdZ_VM(int[] maxWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(maxWith, "$this$maxWith");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.unsigned.UArraysKt.m12168maxWithOrNullYmdZ_VM(maxWith, comparator);
    }

    @kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: maxWith-zrEWJaI, reason: not valid java name */
    public static final /* synthetic */ kotlin.ULong m11802maxWithzrEWJaI(long[] maxWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(maxWith, "$this$maxWith");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.unsigned.UArraysKt.m12170maxWithOrNullzrEWJaI(maxWith, comparator);
    }

    @kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: maxWith-XMRcp5o, reason: not valid java name */
    public static final /* synthetic */ kotlin.UByte m11799maxWithXMRcp5o(byte[] maxWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(maxWith, "$this$maxWith");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.unsigned.UArraysKt.m12167maxWithOrNullXMRcp5o(maxWith, comparator);
    }

    @kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: maxWith-eOHTfZs, reason: not valid java name */
    public static final /* synthetic */ kotlin.UShort m11801maxWitheOHTfZs(short[] maxWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(maxWith, "$this$maxWith");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.unsigned.UArraysKt.m12169maxWithOrNulleOHTfZs(maxWith, comparator);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: min--ajY-9A, reason: not valid java name */
    public static final /* synthetic */ kotlin.UInt m11803minajY9A(int[] min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(min, "$this$min");
        return kotlin.collections.unsigned.UArraysKt.m12215minOrNullajY9A(min);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: min-QwZRm1k, reason: not valid java name */
    public static final /* synthetic */ kotlin.ULong m11805minQwZRm1k(long[] min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(min, "$this$min");
        return kotlin.collections.unsigned.UArraysKt.m12217minOrNullQwZRm1k(min);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: min-GBYM_sE, reason: not valid java name */
    public static final /* synthetic */ kotlin.UByte m11804minGBYM_sE(byte[] min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(min, "$this$min");
        return kotlin.collections.unsigned.UArraysKt.m12216minOrNullGBYM_sE(min);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: min-rL5Bavg, reason: not valid java name */
    public static final /* synthetic */ kotlin.UShort m11806minrL5Bavg(short[] min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(min, "$this$min");
        return kotlin.collections.unsigned.UArraysKt.m12218minOrNullrL5Bavg(min);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: minBy-jgv0xPQ, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> kotlin.UInt m11809minByjgv0xPQ(int[] minBy, kotlin.jvm.functions.Function1<? super kotlin.UInt, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(minBy, "$this$minBy");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if (kotlin.UIntArray.m11465isEmptyimpl(minBy)) {
            return null;
        }
        int iM11462getpVg5ArA = kotlin.UIntArray.m11462getpVg5ArA(minBy, 0);
        int lastIndex = kotlin.collections.ArraysKt.getLastIndex(minBy);
        if (lastIndex != 0) {
            R rInvoke = selector.invoke(kotlin.UInt.m11396boximpl(iM11462getpVg5ArA));
            ?? it = new kotlin.ranges.IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                int iM11462getpVg5ArA2 = kotlin.UIntArray.m11462getpVg5ArA(minBy, it.nextInt());
                R rInvoke2 = selector.invoke(kotlin.UInt.m11396boximpl(iM11462getpVg5ArA2));
                if (rInvoke.compareTo(rInvoke2) > 0) {
                    iM11462getpVg5ArA = iM11462getpVg5ArA2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return kotlin.UInt.m11396boximpl(iM11462getpVg5ArA);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: minBy-MShoTSo, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> kotlin.ULong m11808minByMShoTSo(long[] minBy, kotlin.jvm.functions.Function1<? super kotlin.ULong, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(minBy, "$this$minBy");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if (kotlin.ULongArray.m11544isEmptyimpl(minBy)) {
            return null;
        }
        long jM11541getsVKNKU = kotlin.ULongArray.m11541getsVKNKU(minBy, 0);
        int lastIndex = kotlin.collections.ArraysKt.getLastIndex(minBy);
        if (lastIndex != 0) {
            R rInvoke = selector.invoke(kotlin.ULong.m11475boximpl(jM11541getsVKNKU));
            ?? it = new kotlin.ranges.IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                long jM11541getsVKNKU2 = kotlin.ULongArray.m11541getsVKNKU(minBy, it.nextInt());
                R rInvoke2 = selector.invoke(kotlin.ULong.m11475boximpl(jM11541getsVKNKU2));
                if (rInvoke.compareTo(rInvoke2) > 0) {
                    jM11541getsVKNKU = jM11541getsVKNKU2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return kotlin.ULong.m11475boximpl(jM11541getsVKNKU);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: minBy-JOV_ifY, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> kotlin.UByte m11807minByJOV_ifY(byte[] minBy, kotlin.jvm.functions.Function1<? super kotlin.UByte, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(minBy, "$this$minBy");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if (kotlin.UByteArray.m11386isEmptyimpl(minBy)) {
            return null;
        }
        byte bM11383getw2LRezQ = kotlin.UByteArray.m11383getw2LRezQ(minBy, 0);
        int lastIndex = kotlin.collections.ArraysKt.getLastIndex(minBy);
        if (lastIndex != 0) {
            R rInvoke = selector.invoke(kotlin.UByte.m11319boximpl(bM11383getw2LRezQ));
            ?? it = new kotlin.ranges.IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                byte bM11383getw2LRezQ2 = kotlin.UByteArray.m11383getw2LRezQ(minBy, it.nextInt());
                R rInvoke2 = selector.invoke(kotlin.UByte.m11319boximpl(bM11383getw2LRezQ2));
                if (rInvoke.compareTo(rInvoke2) > 0) {
                    bM11383getw2LRezQ = bM11383getw2LRezQ2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return kotlin.UByte.m11319boximpl(bM11383getw2LRezQ);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: minBy-xTcfx_M, reason: not valid java name */
    private static final /* synthetic */ <R extends java.lang.Comparable<? super R>> kotlin.UShort m11810minByxTcfx_M(short[] minBy, kotlin.jvm.functions.Function1<? super kotlin.UShort, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(minBy, "$this$minBy");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if (kotlin.UShortArray.m11649isEmptyimpl(minBy)) {
            return null;
        }
        short sM11646getMh2AYeg = kotlin.UShortArray.m11646getMh2AYeg(minBy, 0);
        int lastIndex = kotlin.collections.ArraysKt.getLastIndex(minBy);
        if (lastIndex != 0) {
            R rInvoke = selector.invoke(kotlin.UShort.m11582boximpl(sM11646getMh2AYeg));
            ?? it = new kotlin.ranges.IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                short sM11646getMh2AYeg2 = kotlin.UShortArray.m11646getMh2AYeg(minBy, it.nextInt());
                R rInvoke2 = selector.invoke(kotlin.UShort.m11582boximpl(sM11646getMh2AYeg2));
                if (rInvoke.compareTo(rInvoke2) > 0) {
                    sM11646getMh2AYeg = sM11646getMh2AYeg2;
                    rInvoke = rInvoke2;
                }
            }
        }
        return kotlin.UShort.m11582boximpl(sM11646getMh2AYeg);
    }

    @kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: minWith-YmdZ_VM, reason: not valid java name */
    public static final /* synthetic */ kotlin.UInt m11812minWithYmdZ_VM(int[] minWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(minWith, "$this$minWith");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.unsigned.UArraysKt.m12224minWithOrNullYmdZ_VM(minWith, comparator);
    }

    @kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: minWith-zrEWJaI, reason: not valid java name */
    public static final /* synthetic */ kotlin.ULong m11814minWithzrEWJaI(long[] minWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(minWith, "$this$minWith");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.unsigned.UArraysKt.m12226minWithOrNullzrEWJaI(minWith, comparator);
    }

    @kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: minWith-XMRcp5o, reason: not valid java name */
    public static final /* synthetic */ kotlin.UByte m11811minWithXMRcp5o(byte[] minWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(minWith, "$this$minWith");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.unsigned.UArraysKt.m12223minWithOrNullXMRcp5o(minWith, comparator);
    }

    @kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: minWith-eOHTfZs, reason: not valid java name */
    public static final /* synthetic */ kotlin.UShort m11813minWitheOHTfZs(short[] minWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(minWith, "$this$minWith");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.unsigned.UArraysKt.m12225minWithOrNulleOHTfZs(minWith, comparator);
    }

    private static final java.math.BigDecimal sumOfBigDecimal(int[] sumOf, kotlin.jvm.functions.Function1<? super kotlin.UInt, ? extends java.math.BigDecimal> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM11463getSizeimpl = kotlin.UIntArray.m11463getSizeimpl(sumOf);
        for (int i = 0; i < iM11463getSizeimpl; i++) {
            int element = kotlin.UIntArray.m11462getpVg5ArA(sumOf, i);
            java.math.BigDecimal bigDecimalAdd = sum.add(selector.invoke(kotlin.UInt.m11396boximpl(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final java.math.BigDecimal sumOfBigDecimal(long[] sumOf, kotlin.jvm.functions.Function1<? super kotlin.ULong, ? extends java.math.BigDecimal> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM11542getSizeimpl = kotlin.ULongArray.m11542getSizeimpl(sumOf);
        for (int i = 0; i < iM11542getSizeimpl; i++) {
            long element = kotlin.ULongArray.m11541getsVKNKU(sumOf, i);
            java.math.BigDecimal bigDecimalAdd = sum.add(selector.invoke(kotlin.ULong.m11475boximpl(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final java.math.BigDecimal sumOfBigDecimal(byte[] sumOf, kotlin.jvm.functions.Function1<? super kotlin.UByte, ? extends java.math.BigDecimal> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM11384getSizeimpl = kotlin.UByteArray.m11384getSizeimpl(sumOf);
        for (int i = 0; i < iM11384getSizeimpl; i++) {
            byte element = kotlin.UByteArray.m11383getw2LRezQ(sumOf, i);
            java.math.BigDecimal bigDecimalAdd = sum.add(selector.invoke(kotlin.UByte.m11319boximpl(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final java.math.BigDecimal sumOfBigDecimal(short[] sumOf, kotlin.jvm.functions.Function1<? super kotlin.UShort, ? extends java.math.BigDecimal> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM11647getSizeimpl = kotlin.UShortArray.m11647getSizeimpl(sumOf);
        for (int i = 0; i < iM11647getSizeimpl; i++) {
            short element = kotlin.UShortArray.m11646getMh2AYeg(sumOf, i);
            java.math.BigDecimal bigDecimalAdd = sum.add(selector.invoke(kotlin.UShort.m11582boximpl(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final java.math.BigInteger sumOfBigInteger(int[] sumOf, kotlin.jvm.functions.Function1<? super kotlin.UInt, ? extends java.math.BigInteger> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM11463getSizeimpl = kotlin.UIntArray.m11463getSizeimpl(sumOf);
        for (int i = 0; i < iM11463getSizeimpl; i++) {
            int element = kotlin.UIntArray.m11462getpVg5ArA(sumOf, i);
            java.math.BigInteger bigIntegerAdd = sum.add(selector.invoke(kotlin.UInt.m11396boximpl(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }

    private static final java.math.BigInteger sumOfBigInteger(long[] sumOf, kotlin.jvm.functions.Function1<? super kotlin.ULong, ? extends java.math.BigInteger> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM11542getSizeimpl = kotlin.ULongArray.m11542getSizeimpl(sumOf);
        for (int i = 0; i < iM11542getSizeimpl; i++) {
            long element = kotlin.ULongArray.m11541getsVKNKU(sumOf, i);
            java.math.BigInteger bigIntegerAdd = sum.add(selector.invoke(kotlin.ULong.m11475boximpl(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }

    private static final java.math.BigInteger sumOfBigInteger(byte[] sumOf, kotlin.jvm.functions.Function1<? super kotlin.UByte, ? extends java.math.BigInteger> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM11384getSizeimpl = kotlin.UByteArray.m11384getSizeimpl(sumOf);
        for (int i = 0; i < iM11384getSizeimpl; i++) {
            byte element = kotlin.UByteArray.m11383getw2LRezQ(sumOf, i);
            java.math.BigInteger bigIntegerAdd = sum.add(selector.invoke(kotlin.UByte.m11319boximpl(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }

    private static final java.math.BigInteger sumOfBigInteger(short[] sumOf, kotlin.jvm.functions.Function1<? super kotlin.UShort, ? extends java.math.BigInteger> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        int iM11647getSizeimpl = kotlin.UShortArray.m11647getSizeimpl(sumOf);
        for (int i = 0; i < iM11647getSizeimpl; i++) {
            short element = kotlin.UShortArray.m11646getMh2AYeg(sumOf, i);
            java.math.BigInteger bigIntegerAdd = sum.add(selector.invoke(kotlin.UShort.m11582boximpl(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }
}
