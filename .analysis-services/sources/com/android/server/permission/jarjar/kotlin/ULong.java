package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: ULong.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\"\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 ~2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001~B\u0011\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b\n\u0010\u000bJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b\u000f\u0010\u0010J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u0012\u0010\u0013J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\n¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u0018\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b\u001c\u0010\u001dJ\u0018\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u001e\u0010\u001fJ\u0018\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b \u0010\u000bJ\u0018\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b!\u0010\"J\u001a\u0010#\u001a\u00020$2\b\u0010\t\u001a\u0004\u0018\u00010%HÖ\u0003¢\u0006\u0004\b&\u0010'J\u0018\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\b¢\u0006\u0004\b)\u0010\u001dJ\u0018\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b*\u0010\u001fJ\u0018\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b+\u0010\u000bJ\u0018\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\b¢\u0006\u0004\b,\u0010\"J\u0010\u0010-\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b.\u0010/J\u0013\u00100\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b1\u0010\u0005J\u0013\u00102\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b3\u0010\u0005J\u0018\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b5\u0010\u001dJ\u0018\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b6\u0010\u001fJ\u0018\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b7\u0010\u000bJ\u0018\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b8\u0010\"J\u0018\u00109\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000eH\u0087\b¢\u0006\u0004\b:\u0010;J\u0018\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b<\u0010\u0013J\u0018\u00109\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b=\u0010\u000bJ\u0018\u00109\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\u0016H\u0087\b¢\u0006\u0004\b>\u0010?J\u0018\u0010@\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bA\u0010\u000bJ\u0018\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bC\u0010\u001dJ\u0018\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bD\u0010\u001fJ\u0018\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bE\u0010\u000bJ\u0018\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bF\u0010\"J\u0018\u0010G\u001a\u00020H2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bI\u0010JJ\u0018\u0010K\u001a\u00020H2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bL\u0010JJ\u0018\u0010M\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bN\u0010\u001dJ\u0018\u0010M\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bO\u0010\u001fJ\u0018\u0010M\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bP\u0010\u000bJ\u0018\u0010M\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bQ\u0010\"J\u001b\u0010R\u001a\u00020\u00002\u0006\u0010S\u001a\u00020\rH\u0087\fø\u0001\u0000¢\u0006\u0004\bT\u0010\u001fJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010S\u001a\u00020\rH\u0087\fø\u0001\u0000¢\u0006\u0004\bV\u0010\u001fJ\u0018\u0010W\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bX\u0010\u001dJ\u0018\u0010W\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bY\u0010\u001fJ\u0018\u0010W\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bZ\u0010\u000bJ\u0018\u0010W\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b[\u0010\"J\u0010\u0010\\\u001a\u00020]H\u0087\b¢\u0006\u0004\b^\u0010_J\u0010\u0010`\u001a\u00020aH\u0087\b¢\u0006\u0004\bb\u0010cJ\u0010\u0010d\u001a\u00020eH\u0087\b¢\u0006\u0004\bf\u0010gJ\u0010\u0010h\u001a\u00020\rH\u0087\b¢\u0006\u0004\bi\u0010/J\u0010\u0010j\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bk\u0010\u0005J\u0010\u0010l\u001a\u00020mH\u0087\b¢\u0006\u0004\bn\u0010oJ\u000f\u0010p\u001a\u00020qH\u0016¢\u0006\u0004\br\u0010sJ\u0013\u0010t\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\bu\u0010_J\u0013\u0010v\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\bw\u0010/J\u0013\u0010x\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\by\u0010\u0005J\u0013\u0010z\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\b{\u0010oJ\u0018\u0010|\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b}\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u0082\u0002\u0004\n\u0002\b!¨\u0006\u007f"}, d2 = {"Lkotlin/ULong;", "", "data", "", "constructor-impl", "(J)J", "getData$annotations", "()V", "and", "other", "and-VKZWuLQ", "(JJ)J", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(JB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(JI)I", "compareTo-VKZWuLQ", "(JJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(JS)I", "dec", "dec-s-VKNKU", "div", "div-7apg3OU", "(JB)J", "div-WZ4Q5Ns", "(JI)J", "div-VKZWuLQ", "div-xj2QHRw", "(JS)J", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "(J)I", "inc", "inc-s-VKNKU", "inv", "inv-s-VKNKU", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(JB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "(JS)S", "or", "or-VKZWuLQ", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/ULongRange;", "rangeTo-VKZWuLQ", "(JJ)Lkotlin/ranges/ULongRange;", "rangeUntil", "rangeUntil-VKZWuLQ", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-s-VKNKU", "shr", "shr-s-VKNKU", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(J)B", "toDouble", "", "toDouble-impl", "(J)D", "toFloat", "", "toFloat-impl", "(J)F", "toInt", "toInt-impl", "toLong", "toLong-impl", "toShort", "", "toShort-impl", "(J)S", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-VKZWuLQ", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@com.android.server.permission.jarjar.kotlin.jvm.JvmInline
public final class ULong implements java.lang.Comparable<com.android.server.permission.jarjar.kotlin.ULong> {
    public static final com.android.server.permission.jarjar.kotlin.ULong.Companion Companion = new com.android.server.permission.jarjar.kotlin.ULong.Companion(null);
    public static final long MAX_VALUE = -1;
    public static final long MIN_VALUE = 0;
    public static final int SIZE_BITS = 64;
    public static final int SIZE_BYTES = 8;
    private final long data;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.ULong m6257boximpl(long j) {
        return new com.android.server.permission.jarjar.kotlin.ULong(j);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long m6263constructorimpl(long j) {
        return j;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m6269equalsimpl(long j, java.lang.Object obj) {
        return (obj instanceof com.android.server.permission.jarjar.kotlin.ULong) && j == ((com.android.server.permission.jarjar.kotlin.ULong) obj).m6315unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m6270equalsimpl0(long j, long j2) {
        return j == j2;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m6275hashCodeimpl(long j) {
        return java.lang.Long.hashCode(j);
    }

    public boolean equals(java.lang.Object obj) {
        return m6269equalsimpl(this.data, obj);
    }

    public int hashCode() {
        return m6275hashCodeimpl(this.data);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ long m6315unboximpl() {
        return this.data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(com.android.server.permission.jarjar.kotlin.ULong uLong) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.ulongCompare(m6315unboximpl(), uLong.m6315unboximpl());
    }

    private /* synthetic */ ULong(long data) {
        this.data = data;
    }

    /* JADX INFO: compiled from: ULong.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\n"}, d2 = {"Lkotlin/ULong$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/ULong;", "J", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private static final int m6258compareTo7apg3OU(long arg0, byte other) {
        return java.lang.Long.compareUnsigned(arg0, m6263constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private static final int m6262compareToxj2QHRw(long arg0, short other) {
        return java.lang.Long.compareUnsigned(arg0, m6263constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private static final int m6261compareToWZ4Q5Ns(long arg0, int other) {
        return java.lang.Long.compareUnsigned(arg0, m6263constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private int m6259compareToVKZWuLQ(long other) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.ulongCompare(m6315unboximpl(), other);
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private static int m6260compareToVKZWuLQ(long arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.ulongCompare(arg0, other);
    }

    /* JADX INFO: renamed from: plus-7apg3OU, reason: not valid java name */
    private static final long m6287plus7apg3OU(long arg0, byte other) {
        return m6263constructorimpl(m6263constructorimpl(((long) other) & 255) + arg0);
    }

    /* JADX INFO: renamed from: plus-xj2QHRw, reason: not valid java name */
    private static final long m6290plusxj2QHRw(long arg0, short other) {
        return m6263constructorimpl(m6263constructorimpl(((long) other) & 65535) + arg0);
    }

    /* JADX INFO: renamed from: plus-WZ4Q5Ns, reason: not valid java name */
    private static final long m6289plusWZ4Q5Ns(long arg0, int other) {
        return m6263constructorimpl(m6263constructorimpl(((long) other) & 4294967295L) + arg0);
    }

    /* JADX INFO: renamed from: plus-VKZWuLQ, reason: not valid java name */
    private static final long m6288plusVKZWuLQ(long arg0, long other) {
        return m6263constructorimpl(arg0 + other);
    }

    /* JADX INFO: renamed from: minus-7apg3OU, reason: not valid java name */
    private static final long m6278minus7apg3OU(long arg0, byte other) {
        return m6263constructorimpl(arg0 - m6263constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: minus-xj2QHRw, reason: not valid java name */
    private static final long m6281minusxj2QHRw(long arg0, short other) {
        return m6263constructorimpl(arg0 - m6263constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: minus-WZ4Q5Ns, reason: not valid java name */
    private static final long m6280minusWZ4Q5Ns(long arg0, int other) {
        return m6263constructorimpl(arg0 - m6263constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: minus-VKZWuLQ, reason: not valid java name */
    private static final long m6279minusVKZWuLQ(long arg0, long other) {
        return m6263constructorimpl(arg0 - other);
    }

    /* JADX INFO: renamed from: times-7apg3OU, reason: not valid java name */
    private static final long m6299times7apg3OU(long arg0, byte other) {
        return m6263constructorimpl(m6263constructorimpl(((long) other) & 255) * arg0);
    }

    /* JADX INFO: renamed from: times-xj2QHRw, reason: not valid java name */
    private static final long m6302timesxj2QHRw(long arg0, short other) {
        return m6263constructorimpl(m6263constructorimpl(((long) other) & 65535) * arg0);
    }

    /* JADX INFO: renamed from: times-WZ4Q5Ns, reason: not valid java name */
    private static final long m6301timesWZ4Q5Ns(long arg0, int other) {
        return m6263constructorimpl(m6263constructorimpl(((long) other) & 4294967295L) * arg0);
    }

    /* JADX INFO: renamed from: times-VKZWuLQ, reason: not valid java name */
    private static final long m6300timesVKZWuLQ(long arg0, long other) {
        return m6263constructorimpl(arg0 * other);
    }

    /* JADX INFO: renamed from: div-7apg3OU, reason: not valid java name */
    private static final long m6265div7apg3OU(long arg0, byte other) {
        return java.lang.Long.divideUnsigned(arg0, m6263constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: div-xj2QHRw, reason: not valid java name */
    private static final long m6268divxj2QHRw(long arg0, short other) {
        return java.lang.Long.divideUnsigned(arg0, m6263constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: div-WZ4Q5Ns, reason: not valid java name */
    private static final long m6267divWZ4Q5Ns(long arg0, int other) {
        return java.lang.Long.divideUnsigned(arg0, m6263constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: div-VKZWuLQ, reason: not valid java name */
    private static final long m6266divVKZWuLQ(long arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.m6442ulongDivideeb3DHEI(arg0, other);
    }

    /* JADX INFO: renamed from: rem-7apg3OU, reason: not valid java name */
    private static final long m6293rem7apg3OU(long arg0, byte other) {
        return java.lang.Long.remainderUnsigned(arg0, m6263constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: rem-xj2QHRw, reason: not valid java name */
    private static final long m6296remxj2QHRw(long arg0, short other) {
        return java.lang.Long.remainderUnsigned(arg0, m6263constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: rem-WZ4Q5Ns, reason: not valid java name */
    private static final long m6295remWZ4Q5Ns(long arg0, int other) {
        return java.lang.Long.remainderUnsigned(arg0, m6263constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: rem-VKZWuLQ, reason: not valid java name */
    private static final long m6294remVKZWuLQ(long arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.m6443ulongRemaindereb3DHEI(arg0, other);
    }

    /* JADX INFO: renamed from: floorDiv-7apg3OU, reason: not valid java name */
    private static final long m6271floorDiv7apg3OU(long arg0, byte other) {
        return java.lang.Long.divideUnsigned(arg0, m6263constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: floorDiv-xj2QHRw, reason: not valid java name */
    private static final long m6274floorDivxj2QHRw(long arg0, short other) {
        return java.lang.Long.divideUnsigned(arg0, m6263constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: floorDiv-WZ4Q5Ns, reason: not valid java name */
    private static final long m6273floorDivWZ4Q5Ns(long arg0, int other) {
        return java.lang.Long.divideUnsigned(arg0, m6263constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: floorDiv-VKZWuLQ, reason: not valid java name */
    private static final long m6272floorDivVKZWuLQ(long arg0, long other) {
        return java.lang.Long.divideUnsigned(arg0, other);
    }

    /* JADX INFO: renamed from: mod-7apg3OU, reason: not valid java name */
    private static final byte m6282mod7apg3OU(long arg0, byte other) {
        return com.android.server.permission.jarjar.kotlin.UByte.m6107constructorimpl((byte) java.lang.Long.remainderUnsigned(arg0, m6263constructorimpl(((long) other) & 255)));
    }

    /* JADX INFO: renamed from: mod-xj2QHRw, reason: not valid java name */
    private static final short m6285modxj2QHRw(long arg0, short other) {
        return com.android.server.permission.jarjar.kotlin.UShort.m6370constructorimpl((short) java.lang.Long.remainderUnsigned(arg0, m6263constructorimpl(((long) other) & 65535)));
    }

    /* JADX INFO: renamed from: mod-WZ4Q5Ns, reason: not valid java name */
    private static final int m6284modWZ4Q5Ns(long arg0, int other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl((int) java.lang.Long.remainderUnsigned(arg0, m6263constructorimpl(((long) other) & 4294967295L)));
    }

    /* JADX INFO: renamed from: mod-VKZWuLQ, reason: not valid java name */
    private static final long m6283modVKZWuLQ(long arg0, long other) {
        return java.lang.Long.remainderUnsigned(arg0, other);
    }

    /* JADX INFO: renamed from: inc-s-VKNKU, reason: not valid java name */
    private static final long m6276incsVKNKU(long arg0) {
        return m6263constructorimpl(1 + arg0);
    }

    /* JADX INFO: renamed from: dec-s-VKNKU, reason: not valid java name */
    private static final long m6264decsVKNKU(long arg0) {
        return m6263constructorimpl((-1) + arg0);
    }

    /* JADX INFO: renamed from: rangeTo-VKZWuLQ, reason: not valid java name */
    private static final com.android.server.permission.jarjar.kotlin.ranges.ULongRange m6291rangeToVKZWuLQ(long arg0, long other) {
        return new com.android.server.permission.jarjar.kotlin.ranges.ULongRange(arg0, other, null);
    }

    /* JADX INFO: renamed from: rangeUntil-VKZWuLQ, reason: not valid java name */
    private static final com.android.server.permission.jarjar.kotlin.ranges.ULongRange m6292rangeUntilVKZWuLQ(long arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.ranges.URangesKt.m7359untileb3DHEI(arg0, other);
    }

    /* JADX INFO: renamed from: shl-s-VKNKU, reason: not valid java name */
    private static final long m6297shlsVKNKU(long arg0, int bitCount) {
        return m6263constructorimpl(arg0 << bitCount);
    }

    /* JADX INFO: renamed from: shr-s-VKNKU, reason: not valid java name */
    private static final long m6298shrsVKNKU(long arg0, int bitCount) {
        return m6263constructorimpl(arg0 >>> bitCount);
    }

    /* JADX INFO: renamed from: and-VKZWuLQ, reason: not valid java name */
    private static final long m6256andVKZWuLQ(long arg0, long other) {
        return m6263constructorimpl(arg0 & other);
    }

    /* JADX INFO: renamed from: or-VKZWuLQ, reason: not valid java name */
    private static final long m6286orVKZWuLQ(long arg0, long other) {
        return m6263constructorimpl(arg0 | other);
    }

    /* JADX INFO: renamed from: xor-VKZWuLQ, reason: not valid java name */
    private static final long m6314xorVKZWuLQ(long arg0, long other) {
        return m6263constructorimpl(arg0 ^ other);
    }

    /* JADX INFO: renamed from: inv-s-VKNKU, reason: not valid java name */
    private static final long m6277invsVKNKU(long arg0) {
        return m6263constructorimpl(~arg0);
    }

    /* JADX INFO: renamed from: toByte-impl, reason: not valid java name */
    private static final byte m6303toByteimpl(long arg0) {
        return (byte) arg0;
    }

    /* JADX INFO: renamed from: toShort-impl, reason: not valid java name */
    private static final short m6308toShortimpl(long arg0) {
        return (short) arg0;
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    private static final int m6306toIntimpl(long arg0) {
        return (int) arg0;
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    private static final long m6307toLongimpl(long arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toUByte-w2LRezQ, reason: not valid java name */
    private static final byte m6310toUBytew2LRezQ(long arg0) {
        return com.android.server.permission.jarjar.kotlin.UByte.m6107constructorimpl((byte) arg0);
    }

    /* JADX INFO: renamed from: toUShort-Mh2AYeg, reason: not valid java name */
    private static final short m6313toUShortMh2AYeg(long arg0) {
        return com.android.server.permission.jarjar.kotlin.UShort.m6370constructorimpl((short) arg0);
    }

    /* JADX INFO: renamed from: toUInt-pVg5ArA, reason: not valid java name */
    private static final int m6311toUIntpVg5ArA(long arg0) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl((int) arg0);
    }

    /* JADX INFO: renamed from: toULong-s-VKNKU, reason: not valid java name */
    private static final long m6312toULongsVKNKU(long arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toFloat-impl, reason: not valid java name */
    private static final float m6305toFloatimpl(long arg0) {
        return (float) com.android.server.permission.jarjar.kotlin.UnsignedKt.ulongToDouble(arg0);
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    private static final double m6304toDoubleimpl(long arg0) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.ulongToDouble(arg0);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m6309toStringimpl(long arg0) {
        return com.android.server.permission.jarjar.kotlin.UnsignedKt.ulongToString(arg0);
    }

    public java.lang.String toString() {
        return m6309toStringimpl(this.data);
    }
}
