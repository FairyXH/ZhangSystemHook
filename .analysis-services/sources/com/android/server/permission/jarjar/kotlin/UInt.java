package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: UInt.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 {2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001{B\u0011\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b\n\u0010\u000bJ\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\rH\u0087\n¢\u0006\u0004\b\u000e\u0010\u000fJ\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0000H\u0097\n¢\u0006\u0004\b\u0010\u0010\u000bJ\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u0012\u0010\u0013J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\b\u0015\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0005J\u0018\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n¢\u0006\u0004\b\u001a\u0010\u000fJ\u0018\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b\u001b\u0010\u000bJ\u0018\u0010\u0019\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u001c\u0010\u001dJ\u0018\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\b\u001e\u0010\u0016J\u001a\u0010\u001f\u001a\u00020 2\b\u0010\t\u001a\u0004\u0018\u00010!HÖ\u0003¢\u0006\u0004\b\"\u0010#J\u0018\u0010$\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\b¢\u0006\u0004\b%\u0010\u000fJ\u0018\u0010$\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b&\u0010\u000bJ\u0018\u0010$\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b'\u0010\u001dJ\u0018\u0010$\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\b¢\u0006\u0004\b(\u0010\u0016J\u0010\u0010)\u001a\u00020\u0003HÖ\u0001¢\u0006\u0004\b*\u0010\u0005J\u0013\u0010+\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b,\u0010\u0005J\u0013\u0010-\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b.\u0010\u0005J\u0018\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n¢\u0006\u0004\b0\u0010\u000fJ\u0018\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b1\u0010\u000bJ\u0018\u0010/\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b2\u0010\u001dJ\u0018\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\b3\u0010\u0016J\u0018\u00104\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\rH\u0087\b¢\u0006\u0004\b5\u00106J\u0018\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b7\u0010\u000bJ\u0018\u00104\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b8\u0010\u001dJ\u0018\u00104\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\b¢\u0006\u0004\b9\u0010:J\u0018\u0010;\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b<\u0010\u000bJ\u0018\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n¢\u0006\u0004\b>\u0010\u000fJ\u0018\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b?\u0010\u000bJ\u0018\u0010=\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b@\u0010\u001dJ\u0018\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\bA\u0010\u0016J\u0018\u0010B\u001a\u00020C2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bD\u0010EJ\u0018\u0010F\u001a\u00020C2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bG\u0010EJ\u0018\u0010H\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n¢\u0006\u0004\bI\u0010\u000fJ\u0018\u0010H\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bJ\u0010\u000bJ\u0018\u0010H\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bK\u0010\u001dJ\u0018\u0010H\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\bL\u0010\u0016J\u001b\u0010M\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\u0003H\u0087\fø\u0001\u0000¢\u0006\u0004\bO\u0010\u000bJ\u001b\u0010P\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\u0003H\u0087\fø\u0001\u0000¢\u0006\u0004\bQ\u0010\u000bJ\u0018\u0010R\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n¢\u0006\u0004\bS\u0010\u000fJ\u0018\u0010R\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bT\u0010\u000bJ\u0018\u0010R\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bU\u0010\u001dJ\u0018\u0010R\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\bV\u0010\u0016J\u0010\u0010W\u001a\u00020XH\u0087\b¢\u0006\u0004\bY\u0010ZJ\u0010\u0010[\u001a\u00020\\H\u0087\b¢\u0006\u0004\b]\u0010^J\u0010\u0010_\u001a\u00020`H\u0087\b¢\u0006\u0004\ba\u0010bJ\u0010\u0010c\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bd\u0010\u0005J\u0010\u0010e\u001a\u00020fH\u0087\b¢\u0006\u0004\bg\u0010hJ\u0010\u0010i\u001a\u00020jH\u0087\b¢\u0006\u0004\bk\u0010lJ\u000f\u0010m\u001a\u00020nH\u0016¢\u0006\u0004\bo\u0010pJ\u0013\u0010q\u001a\u00020\rH\u0087\bø\u0001\u0000¢\u0006\u0004\br\u0010ZJ\u0013\u0010s\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\bt\u0010\u0005J\u0013\u0010u\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\bv\u0010hJ\u0013\u0010w\u001a\u00020\u0014H\u0087\bø\u0001\u0000¢\u0006\u0004\bx\u0010lJ\u0018\u0010y\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bz\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u0082\u0002\u0004\n\u0002\b!¨\u0006|"}, d2 = {"Lkotlin/UInt;", "", "data", "", "constructor-impl", "(I)I", "getData$annotations", "()V", "and", "other", "and-WZ4Q5Ns", "(II)I", "compareTo", "Lkotlin/UByte;", "compareTo-7apg3OU", "(IB)I", "compareTo-WZ4Q5Ns", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(IJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(IS)I", "dec", "dec-pVg5ArA", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(IJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(ILjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "inc", "inc-pVg5ArA", "inv", "inv-pVg5ArA", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(IB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "(IS)S", "or", "or-WZ4Q5Ns", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-WZ4Q5Ns", "(II)Lkotlin/ranges/UIntRange;", "rangeUntil", "rangeUntil-WZ4Q5Ns", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-pVg5ArA", "shr", "shr-pVg5ArA", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(I)B", "toDouble", "", "toDouble-impl", "(I)D", "toFloat", "", "toFloat-impl", "(I)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(I)J", "toShort", "", "toShort-impl", "(I)S", "toString", "", "toString-impl", "(I)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-WZ4Q5Ns", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@com.android.server.permission.jarjar.kotlin.jvm.JvmInline
public final class UInt implements java.lang.Comparable<com.android.server.permission.jarjar.kotlin.UInt> {
    public static final com.android.server.permission.jarjar.kotlin.UInt.Companion Companion = new com.android.server.permission.jarjar.kotlin.UInt.Companion(null);
    public static final int MAX_VALUE = -1;
    public static final int MIN_VALUE = 0;
    public static final int SIZE_BITS = 32;
    public static final int SIZE_BYTES = 4;
    private final int data;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UInt m6178boximpl(int i) {
        return new com.android.server.permission.jarjar.kotlin.UInt(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m6184constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m6190equalsimpl(int i, java.lang.Object obj) {
        return (obj instanceof com.android.server.permission.jarjar.kotlin.UInt) && i == ((com.android.server.permission.jarjar.kotlin.UInt) obj).m6236unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m6191equalsimpl0(int i, int i2) {
        return i == i2;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m6196hashCodeimpl(int i) {
        return java.lang.Integer.hashCode(i);
    }

    public boolean equals(java.lang.Object obj) {
        return m6190equalsimpl(this.data, obj);
    }

    public int hashCode() {
        return m6196hashCodeimpl(this.data);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m6236unboximpl() {
        return this.data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(com.android.server.permission.jarjar.kotlin.UInt uInt) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.uintCompare(m6236unboximpl(), uInt.m6236unboximpl());
    }

    private /* synthetic */ UInt(int data) {
        this.data = data;
    }

    /* JADX INFO: compiled from: UInt.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\n"}, d2 = {"Lkotlin/UInt$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UInt;", "I", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private static final int m6179compareTo7apg3OU(int arg0, byte other) {
        return java.lang.Integer.compareUnsigned(arg0, m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private static final int m6183compareToxj2QHRw(int arg0, short other) {
        return java.lang.Integer.compareUnsigned(arg0, m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private int m6181compareToWZ4Q5Ns(int other) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.uintCompare(m6236unboximpl(), other);
    }

    /* JADX INFO: renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private static int m6182compareToWZ4Q5Ns(int arg0, int other) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.uintCompare(arg0, other);
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private static final int m6180compareToVKZWuLQ(int arg0, long other) {
        return java.lang.Long.compareUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* JADX INFO: renamed from: plus-7apg3OU, reason: not valid java name */
    private static final int m6208plus7apg3OU(int arg0, byte other) {
        return m6184constructorimpl(m6184constructorimpl(other & 255) + arg0);
    }

    /* JADX INFO: renamed from: plus-xj2QHRw, reason: not valid java name */
    private static final int m6211plusxj2QHRw(int arg0, short other) {
        return m6184constructorimpl(m6184constructorimpl(65535 & other) + arg0);
    }

    /* JADX INFO: renamed from: plus-WZ4Q5Ns, reason: not valid java name */
    private static final int m6210plusWZ4Q5Ns(int arg0, int other) {
        return m6184constructorimpl(arg0 + other);
    }

    /* JADX INFO: renamed from: plus-VKZWuLQ, reason: not valid java name */
    private static final long m6209plusVKZWuLQ(int arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 4294967295L) + other);
    }

    /* JADX INFO: renamed from: minus-7apg3OU, reason: not valid java name */
    private static final int m6199minus7apg3OU(int arg0, byte other) {
        return m6184constructorimpl(arg0 - m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: minus-xj2QHRw, reason: not valid java name */
    private static final int m6202minusxj2QHRw(int arg0, short other) {
        return m6184constructorimpl(arg0 - m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: minus-WZ4Q5Ns, reason: not valid java name */
    private static final int m6201minusWZ4Q5Ns(int arg0, int other) {
        return m6184constructorimpl(arg0 - other);
    }

    /* JADX INFO: renamed from: minus-VKZWuLQ, reason: not valid java name */
    private static final long m6200minusVKZWuLQ(int arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 4294967295L) - other);
    }

    /* JADX INFO: renamed from: times-7apg3OU, reason: not valid java name */
    private static final int m6220times7apg3OU(int arg0, byte other) {
        return m6184constructorimpl(m6184constructorimpl(other & 255) * arg0);
    }

    /* JADX INFO: renamed from: times-xj2QHRw, reason: not valid java name */
    private static final int m6223timesxj2QHRw(int arg0, short other) {
        return m6184constructorimpl(m6184constructorimpl(65535 & other) * arg0);
    }

    /* JADX INFO: renamed from: times-WZ4Q5Ns, reason: not valid java name */
    private static final int m6222timesWZ4Q5Ns(int arg0, int other) {
        return m6184constructorimpl(arg0 * other);
    }

    /* JADX INFO: renamed from: times-VKZWuLQ, reason: not valid java name */
    private static final long m6221timesVKZWuLQ(int arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 4294967295L) * other);
    }

    /* JADX INFO: renamed from: div-7apg3OU, reason: not valid java name */
    private static final int m6186div7apg3OU(int arg0, byte other) {
        return java.lang.Integer.divideUnsigned(arg0, m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: div-xj2QHRw, reason: not valid java name */
    private static final int m6189divxj2QHRw(int arg0, short other) {
        return java.lang.Integer.divideUnsigned(arg0, m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: div-WZ4Q5Ns, reason: not valid java name */
    private static final int m6188divWZ4Q5Ns(int arg0, int other) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.m6440uintDivideJ1ME1BU(arg0, other);
    }

    /* JADX INFO: renamed from: div-VKZWuLQ, reason: not valid java name */
    private static final long m6187divVKZWuLQ(int arg0, long other) {
        return java.lang.Long.divideUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* JADX INFO: renamed from: rem-7apg3OU, reason: not valid java name */
    private static final int m6214rem7apg3OU(int arg0, byte other) {
        return java.lang.Integer.remainderUnsigned(arg0, m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: rem-xj2QHRw, reason: not valid java name */
    private static final int m6217remxj2QHRw(int arg0, short other) {
        return java.lang.Integer.remainderUnsigned(arg0, m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: rem-WZ4Q5Ns, reason: not valid java name */
    private static final int m6216remWZ4Q5Ns(int arg0, int other) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.m6441uintRemainderJ1ME1BU(arg0, other);
    }

    /* JADX INFO: renamed from: rem-VKZWuLQ, reason: not valid java name */
    private static final long m6215remVKZWuLQ(int arg0, long other) {
        return java.lang.Long.remainderUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* JADX INFO: renamed from: floorDiv-7apg3OU, reason: not valid java name */
    private static final int m6192floorDiv7apg3OU(int arg0, byte other) {
        return java.lang.Integer.divideUnsigned(arg0, m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: floorDiv-xj2QHRw, reason: not valid java name */
    private static final int m6195floorDivxj2QHRw(int arg0, short other) {
        return java.lang.Integer.divideUnsigned(arg0, m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: floorDiv-WZ4Q5Ns, reason: not valid java name */
    private static final int m6194floorDivWZ4Q5Ns(int arg0, int other) {
        return java.lang.Integer.divideUnsigned(arg0, other);
    }

    /* JADX INFO: renamed from: floorDiv-VKZWuLQ, reason: not valid java name */
    private static final long m6193floorDivVKZWuLQ(int arg0, long other) {
        return java.lang.Long.divideUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* JADX INFO: renamed from: mod-7apg3OU, reason: not valid java name */
    private static final byte m6203mod7apg3OU(int arg0, byte other) {
        return com.android.server.permission.jarjar.kotlin.UByte.m6107constructorimpl((byte) java.lang.Integer.remainderUnsigned(arg0, m6184constructorimpl(other & 255)));
    }

    /* JADX INFO: renamed from: mod-xj2QHRw, reason: not valid java name */
    private static final short m6206modxj2QHRw(int arg0, short other) {
        return com.android.server.permission.jarjar.kotlin.UShort.m6370constructorimpl((short) java.lang.Integer.remainderUnsigned(arg0, m6184constructorimpl(65535 & other)));
    }

    /* JADX INFO: renamed from: mod-WZ4Q5Ns, reason: not valid java name */
    private static final int m6205modWZ4Q5Ns(int arg0, int other) {
        return java.lang.Integer.remainderUnsigned(arg0, other);
    }

    /* JADX INFO: renamed from: mod-VKZWuLQ, reason: not valid java name */
    private static final long m6204modVKZWuLQ(int arg0, long other) {
        return java.lang.Long.remainderUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* JADX INFO: renamed from: inc-pVg5ArA, reason: not valid java name */
    private static final int m6197incpVg5ArA(int arg0) {
        return m6184constructorimpl(arg0 + 1);
    }

    /* JADX INFO: renamed from: dec-pVg5ArA, reason: not valid java name */
    private static final int m6185decpVg5ArA(int arg0) {
        return m6184constructorimpl(arg0 - 1);
    }

    /* JADX INFO: renamed from: rangeTo-WZ4Q5Ns, reason: not valid java name */
    private static final com.android.server.permission.jarjar.kotlin.ranges.UIntRange m6212rangeToWZ4Q5Ns(int arg0, int other) {
        return new com.android.server.permission.jarjar.kotlin.ranges.UIntRange(arg0, other, null);
    }

    /* JADX INFO: renamed from: rangeUntil-WZ4Q5Ns, reason: not valid java name */
    private static final com.android.server.permission.jarjar.kotlin.ranges.UIntRange m6213rangeUntilWZ4Q5Ns(int arg0, int other) {
        return com.android.server.permission.jarjar.kotlin.ranges.URangesKt.m7357untilJ1ME1BU(arg0, other);
    }

    /* JADX INFO: renamed from: shl-pVg5ArA, reason: not valid java name */
    private static final int m6218shlpVg5ArA(int arg0, int bitCount) {
        return m6184constructorimpl(arg0 << bitCount);
    }

    /* JADX INFO: renamed from: shr-pVg5ArA, reason: not valid java name */
    private static final int m6219shrpVg5ArA(int arg0, int bitCount) {
        return m6184constructorimpl(arg0 >>> bitCount);
    }

    /* JADX INFO: renamed from: and-WZ4Q5Ns, reason: not valid java name */
    private static final int m6177andWZ4Q5Ns(int arg0, int other) {
        return m6184constructorimpl(arg0 & other);
    }

    /* JADX INFO: renamed from: or-WZ4Q5Ns, reason: not valid java name */
    private static final int m6207orWZ4Q5Ns(int arg0, int other) {
        return m6184constructorimpl(arg0 | other);
    }

    /* JADX INFO: renamed from: xor-WZ4Q5Ns, reason: not valid java name */
    private static final int m6235xorWZ4Q5Ns(int arg0, int other) {
        return m6184constructorimpl(arg0 ^ other);
    }

    /* JADX INFO: renamed from: inv-pVg5ArA, reason: not valid java name */
    private static final int m6198invpVg5ArA(int arg0) {
        return m6184constructorimpl(~arg0);
    }

    /* JADX INFO: renamed from: toByte-impl, reason: not valid java name */
    private static final byte m6224toByteimpl(int arg0) {
        return (byte) arg0;
    }

    /* JADX INFO: renamed from: toShort-impl, reason: not valid java name */
    private static final short m6229toShortimpl(int arg0) {
        return (short) arg0;
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    private static final int m6227toIntimpl(int arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    private static final long m6228toLongimpl(int arg0) {
        return ((long) arg0) & 4294967295L;
    }

    /* JADX INFO: renamed from: toUByte-w2LRezQ, reason: not valid java name */
    private static final byte m6231toUBytew2LRezQ(int arg0) {
        return com.android.server.permission.jarjar.kotlin.UByte.m6107constructorimpl((byte) arg0);
    }

    /* JADX INFO: renamed from: toUShort-Mh2AYeg, reason: not valid java name */
    private static final short m6234toUShortMh2AYeg(int arg0) {
        return com.android.server.permission.jarjar.kotlin.UShort.m6370constructorimpl((short) arg0);
    }

    /* JADX INFO: renamed from: toUInt-pVg5ArA, reason: not valid java name */
    private static final int m6232toUIntpVg5ArA(int arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toULong-s-VKNKU, reason: not valid java name */
    private static final long m6233toULongsVKNKU(int arg0) {
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 4294967295L);
    }

    /* JADX INFO: renamed from: toFloat-impl, reason: not valid java name */
    private static final float m6226toFloatimpl(int arg0) {
        return (float) com.android.server.permission.jarjar.kotlin.UnsignedKt.uintToDouble(arg0);
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    private static final double m6225toDoubleimpl(int arg0) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.uintToDouble(arg0);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m6230toStringimpl(int arg0) {
        return java.lang.String.valueOf(((long) arg0) & 4294967295L);
    }

    public java.lang.String toString() {
        return m6230toStringimpl(this.data);
    }
}
