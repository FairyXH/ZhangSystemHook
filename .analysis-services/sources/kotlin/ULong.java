package kotlin;

/* JADX INFO: compiled from: ULong.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\"\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 ~2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001~B\u0011\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b\n\u0010\u000bJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b\u000f\u0010\u0010J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u0012\u0010\u0013J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\n¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u0018\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b\u001c\u0010\u001dJ\u0018\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u001e\u0010\u001fJ\u0018\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b \u0010\u000bJ\u0018\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b!\u0010\"J\u001a\u0010#\u001a\u00020$2\b\u0010\t\u001a\u0004\u0018\u00010%HÖ\u0003¢\u0006\u0004\b&\u0010'J\u0018\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\b¢\u0006\u0004\b)\u0010\u001dJ\u0018\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b*\u0010\u001fJ\u0018\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b+\u0010\u000bJ\u0018\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\b¢\u0006\u0004\b,\u0010\"J\u0010\u0010-\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b.\u0010/J\u0013\u00100\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b1\u0010\u0005J\u0013\u00102\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b3\u0010\u0005J\u0018\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b5\u0010\u001dJ\u0018\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b6\u0010\u001fJ\u0018\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b7\u0010\u000bJ\u0018\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b8\u0010\"J\u0018\u00109\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000eH\u0087\b¢\u0006\u0004\b:\u0010;J\u0018\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b<\u0010\u0013J\u0018\u00109\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b=\u0010\u000bJ\u0018\u00109\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\u0016H\u0087\b¢\u0006\u0004\b>\u0010?J\u0018\u0010@\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bA\u0010\u000bJ\u0018\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bC\u0010\u001dJ\u0018\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bD\u0010\u001fJ\u0018\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bE\u0010\u000bJ\u0018\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bF\u0010\"J\u0018\u0010G\u001a\u00020H2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bI\u0010JJ\u0018\u0010K\u001a\u00020H2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bL\u0010JJ\u0018\u0010M\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bN\u0010\u001dJ\u0018\u0010M\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bO\u0010\u001fJ\u0018\u0010M\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bP\u0010\u000bJ\u0018\u0010M\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bQ\u0010\"J\u001b\u0010R\u001a\u00020\u00002\u0006\u0010S\u001a\u00020\rH\u0087\fø\u0001\u0000¢\u0006\u0004\bT\u0010\u001fJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010S\u001a\u00020\rH\u0087\fø\u0001\u0000¢\u0006\u0004\bV\u0010\u001fJ\u0018\u0010W\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bX\u0010\u001dJ\u0018\u0010W\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bY\u0010\u001fJ\u0018\u0010W\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bZ\u0010\u000bJ\u0018\u0010W\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b[\u0010\"J\u0010\u0010\\\u001a\u00020]H\u0087\b¢\u0006\u0004\b^\u0010_J\u0010\u0010`\u001a\u00020aH\u0087\b¢\u0006\u0004\bb\u0010cJ\u0010\u0010d\u001a\u00020eH\u0087\b¢\u0006\u0004\bf\u0010gJ\u0010\u0010h\u001a\u00020\rH\u0087\b¢\u0006\u0004\bi\u0010/J\u0010\u0010j\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bk\u0010\u0005J\u0010\u0010l\u001a\u00020mH\u0087\b¢\u0006\u0004\bn\u0010oJ\u000f\u0010p\u001a\u00020qH\u0016¢\u0006\u0004\br\u0010sJ\u0013\u0010t\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\bu\u0010_J\u0013\u0010v\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\bw\u0010/J\u0013\u0010x\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\by\u0010\u0005J\u0013\u0010z\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\b{\u0010oJ\u0018\u0010|\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b}\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u0082\u0002\u0004\n\u0002\b!¨\u0006\u007f"}, d2 = {"Lkotlin/ULong;", "", "data", "", "constructor-impl", "(J)J", "getData$annotations", "()V", "and", "other", "and-VKZWuLQ", "(JJ)J", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(JB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(JI)I", "compareTo-VKZWuLQ", "(JJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(JS)I", "dec", "dec-s-VKNKU", "div", "div-7apg3OU", "(JB)J", "div-WZ4Q5Ns", "(JI)J", "div-VKZWuLQ", "div-xj2QHRw", "(JS)J", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "(J)I", "inc", "inc-s-VKNKU", "inv", "inv-s-VKNKU", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(JB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "(JS)S", "or", "or-VKZWuLQ", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/ULongRange;", "rangeTo-VKZWuLQ", "(JJ)Lkotlin/ranges/ULongRange;", "rangeUntil", "rangeUntil-VKZWuLQ", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-s-VKNKU", "shr", "shr-s-VKNKU", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(J)B", "toDouble", "", "toDouble-impl", "(J)D", "toFloat", "", "toFloat-impl", "(J)F", "toInt", "toInt-impl", "toLong", "toLong-impl", "toShort", "", "toShort-impl", "(J)S", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-VKZWuLQ", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@kotlin.jvm.JvmInline
public final class ULong implements java.lang.Comparable<kotlin.ULong> {
    public static final long MAX_VALUE = -1;
    public static final long MIN_VALUE = 0;
    public static final int SIZE_BITS = 64;
    public static final int SIZE_BYTES = 8;
    private final long data;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ kotlin.ULong m11475boximpl(long j) {
        return new kotlin.ULong(j);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long m11481constructorimpl(long j) {
        return j;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m11487equalsimpl(long j, java.lang.Object obj) {
        return (obj instanceof kotlin.ULong) && j == ((kotlin.ULong) obj).getData();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m11488equalsimpl0(long j, long j2) {
        return j == j2;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m11493hashCodeimpl(long j) {
        return java.lang.Long.hashCode(j);
    }

    public boolean equals(java.lang.Object obj) {
        return m11487equalsimpl(this.data, obj);
    }

    public int hashCode() {
        return m11493hashCodeimpl(this.data);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ long getData() {
        return this.data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(kotlin.ULong uLong) {
        return kotlin.UnsignedKt.ulongCompare(getData(), uLong.getData());
    }

    private /* synthetic */ ULong(long data) {
        this.data = data;
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private static final int m11476compareTo7apg3OU(long arg0, byte other) {
        return java.lang.Long.compareUnsigned(arg0, m11481constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private static final int m11480compareToxj2QHRw(long arg0, short other) {
        return java.lang.Long.compareUnsigned(arg0, m11481constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private static final int m11479compareToWZ4Q5Ns(long arg0, int other) {
        return java.lang.Long.compareUnsigned(arg0, m11481constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private int m11477compareToVKZWuLQ(long other) {
        return kotlin.UnsignedKt.ulongCompare(getData(), other);
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private static int m11478compareToVKZWuLQ(long arg0, long other) {
        return kotlin.UnsignedKt.ulongCompare(arg0, other);
    }

    /* JADX INFO: renamed from: plus-7apg3OU, reason: not valid java name */
    private static final long m11505plus7apg3OU(long arg0, byte other) {
        return m11481constructorimpl(m11481constructorimpl(((long) other) & 255) + arg0);
    }

    /* JADX INFO: renamed from: plus-xj2QHRw, reason: not valid java name */
    private static final long m11508plusxj2QHRw(long arg0, short other) {
        return m11481constructorimpl(m11481constructorimpl(((long) other) & 65535) + arg0);
    }

    /* JADX INFO: renamed from: plus-WZ4Q5Ns, reason: not valid java name */
    private static final long m11507plusWZ4Q5Ns(long arg0, int other) {
        return m11481constructorimpl(m11481constructorimpl(((long) other) & 4294967295L) + arg0);
    }

    /* JADX INFO: renamed from: plus-VKZWuLQ, reason: not valid java name */
    private static final long m11506plusVKZWuLQ(long arg0, long other) {
        return m11481constructorimpl(arg0 + other);
    }

    /* JADX INFO: renamed from: minus-7apg3OU, reason: not valid java name */
    private static final long m11496minus7apg3OU(long arg0, byte other) {
        return m11481constructorimpl(arg0 - m11481constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: minus-xj2QHRw, reason: not valid java name */
    private static final long m11499minusxj2QHRw(long arg0, short other) {
        return m11481constructorimpl(arg0 - m11481constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: minus-WZ4Q5Ns, reason: not valid java name */
    private static final long m11498minusWZ4Q5Ns(long arg0, int other) {
        return m11481constructorimpl(arg0 - m11481constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: minus-VKZWuLQ, reason: not valid java name */
    private static final long m11497minusVKZWuLQ(long arg0, long other) {
        return m11481constructorimpl(arg0 - other);
    }

    /* JADX INFO: renamed from: times-7apg3OU, reason: not valid java name */
    private static final long m11517times7apg3OU(long arg0, byte other) {
        return m11481constructorimpl(m11481constructorimpl(((long) other) & 255) * arg0);
    }

    /* JADX INFO: renamed from: times-xj2QHRw, reason: not valid java name */
    private static final long m11520timesxj2QHRw(long arg0, short other) {
        return m11481constructorimpl(m11481constructorimpl(((long) other) & 65535) * arg0);
    }

    /* JADX INFO: renamed from: times-WZ4Q5Ns, reason: not valid java name */
    private static final long m11519timesWZ4Q5Ns(long arg0, int other) {
        return m11481constructorimpl(m11481constructorimpl(((long) other) & 4294967295L) * arg0);
    }

    /* JADX INFO: renamed from: times-VKZWuLQ, reason: not valid java name */
    private static final long m11518timesVKZWuLQ(long arg0, long other) {
        return m11481constructorimpl(arg0 * other);
    }

    /* JADX INFO: renamed from: div-7apg3OU, reason: not valid java name */
    private static final long m11483div7apg3OU(long arg0, byte other) {
        return java.lang.Long.divideUnsigned(arg0, m11481constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: div-xj2QHRw, reason: not valid java name */
    private static final long m11486divxj2QHRw(long arg0, short other) {
        return java.lang.Long.divideUnsigned(arg0, m11481constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: div-WZ4Q5Ns, reason: not valid java name */
    private static final long m11485divWZ4Q5Ns(long arg0, int other) {
        return java.lang.Long.divideUnsigned(arg0, m11481constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: div-VKZWuLQ, reason: not valid java name */
    private static final long m11484divVKZWuLQ(long arg0, long other) {
        return kotlin.UnsignedKt.m11660ulongDivideeb3DHEI(arg0, other);
    }

    /* JADX INFO: renamed from: rem-7apg3OU, reason: not valid java name */
    private static final long m11511rem7apg3OU(long arg0, byte other) {
        return java.lang.Long.remainderUnsigned(arg0, m11481constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: rem-xj2QHRw, reason: not valid java name */
    private static final long m11514remxj2QHRw(long arg0, short other) {
        return java.lang.Long.remainderUnsigned(arg0, m11481constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: rem-WZ4Q5Ns, reason: not valid java name */
    private static final long m11513remWZ4Q5Ns(long arg0, int other) {
        return java.lang.Long.remainderUnsigned(arg0, m11481constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: rem-VKZWuLQ, reason: not valid java name */
    private static final long m11512remVKZWuLQ(long arg0, long other) {
        return kotlin.UnsignedKt.m11661ulongRemaindereb3DHEI(arg0, other);
    }

    /* JADX INFO: renamed from: floorDiv-7apg3OU, reason: not valid java name */
    private static final long m11489floorDiv7apg3OU(long arg0, byte other) {
        return java.lang.Long.divideUnsigned(arg0, m11481constructorimpl(((long) other) & 255));
    }

    /* JADX INFO: renamed from: floorDiv-xj2QHRw, reason: not valid java name */
    private static final long m11492floorDivxj2QHRw(long arg0, short other) {
        return java.lang.Long.divideUnsigned(arg0, m11481constructorimpl(((long) other) & 65535));
    }

    /* JADX INFO: renamed from: floorDiv-WZ4Q5Ns, reason: not valid java name */
    private static final long m11491floorDivWZ4Q5Ns(long arg0, int other) {
        return java.lang.Long.divideUnsigned(arg0, m11481constructorimpl(((long) other) & 4294967295L));
    }

    /* JADX INFO: renamed from: floorDiv-VKZWuLQ, reason: not valid java name */
    private static final long m11490floorDivVKZWuLQ(long arg0, long other) {
        return java.lang.Long.divideUnsigned(arg0, other);
    }

    /* JADX INFO: renamed from: mod-7apg3OU, reason: not valid java name */
    private static final byte m11500mod7apg3OU(long arg0, byte other) {
        return kotlin.UByte.m11325constructorimpl((byte) java.lang.Long.remainderUnsigned(arg0, m11481constructorimpl(((long) other) & 255)));
    }

    /* JADX INFO: renamed from: mod-xj2QHRw, reason: not valid java name */
    private static final short m11503modxj2QHRw(long arg0, short other) {
        return kotlin.UShort.m11588constructorimpl((short) java.lang.Long.remainderUnsigned(arg0, m11481constructorimpl(((long) other) & 65535)));
    }

    /* JADX INFO: renamed from: mod-WZ4Q5Ns, reason: not valid java name */
    private static final int m11502modWZ4Q5Ns(long arg0, int other) {
        return kotlin.UInt.m11402constructorimpl((int) java.lang.Long.remainderUnsigned(arg0, m11481constructorimpl(((long) other) & 4294967295L)));
    }

    /* JADX INFO: renamed from: mod-VKZWuLQ, reason: not valid java name */
    private static final long m11501modVKZWuLQ(long arg0, long other) {
        return java.lang.Long.remainderUnsigned(arg0, other);
    }

    /* JADX INFO: renamed from: inc-s-VKNKU, reason: not valid java name */
    private static final long m11494incsVKNKU(long arg0) {
        return m11481constructorimpl(1 + arg0);
    }

    /* JADX INFO: renamed from: dec-s-VKNKU, reason: not valid java name */
    private static final long m11482decsVKNKU(long arg0) {
        return m11481constructorimpl((-1) + arg0);
    }

    /* JADX INFO: renamed from: rangeTo-VKZWuLQ, reason: not valid java name */
    private static final kotlin.ranges.ULongRange m11509rangeToVKZWuLQ(long arg0, long other) {
        return new kotlin.ranges.ULongRange(arg0, other, null);
    }

    /* JADX INFO: renamed from: rangeUntil-VKZWuLQ, reason: not valid java name */
    private static final kotlin.ranges.ULongRange m11510rangeUntilVKZWuLQ(long arg0, long other) {
        return kotlin.ranges.URangesKt.m12577untileb3DHEI(arg0, other);
    }

    /* JADX INFO: renamed from: shl-s-VKNKU, reason: not valid java name */
    private static final long m11515shlsVKNKU(long arg0, int bitCount) {
        return m11481constructorimpl(arg0 << bitCount);
    }

    /* JADX INFO: renamed from: shr-s-VKNKU, reason: not valid java name */
    private static final long m11516shrsVKNKU(long arg0, int bitCount) {
        return m11481constructorimpl(arg0 >>> bitCount);
    }

    /* JADX INFO: renamed from: and-VKZWuLQ, reason: not valid java name */
    private static final long m11474andVKZWuLQ(long arg0, long other) {
        return m11481constructorimpl(arg0 & other);
    }

    /* JADX INFO: renamed from: or-VKZWuLQ, reason: not valid java name */
    private static final long m11504orVKZWuLQ(long arg0, long other) {
        return m11481constructorimpl(arg0 | other);
    }

    /* JADX INFO: renamed from: xor-VKZWuLQ, reason: not valid java name */
    private static final long m11532xorVKZWuLQ(long arg0, long other) {
        return m11481constructorimpl(arg0 ^ other);
    }

    /* JADX INFO: renamed from: inv-s-VKNKU, reason: not valid java name */
    private static final long m11495invsVKNKU(long arg0) {
        return m11481constructorimpl(~arg0);
    }

    /* JADX INFO: renamed from: toByte-impl, reason: not valid java name */
    private static final byte m11521toByteimpl(long arg0) {
        return (byte) arg0;
    }

    /* JADX INFO: renamed from: toShort-impl, reason: not valid java name */
    private static final short m11526toShortimpl(long arg0) {
        return (short) arg0;
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    private static final int m11524toIntimpl(long arg0) {
        return (int) arg0;
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    private static final long m11525toLongimpl(long arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toUByte-w2LRezQ, reason: not valid java name */
    private static final byte m11528toUBytew2LRezQ(long arg0) {
        return kotlin.UByte.m11325constructorimpl((byte) arg0);
    }

    /* JADX INFO: renamed from: toUShort-Mh2AYeg, reason: not valid java name */
    private static final short m11531toUShortMh2AYeg(long arg0) {
        return kotlin.UShort.m11588constructorimpl((short) arg0);
    }

    /* JADX INFO: renamed from: toUInt-pVg5ArA, reason: not valid java name */
    private static final int m11529toUIntpVg5ArA(long arg0) {
        return kotlin.UInt.m11402constructorimpl((int) arg0);
    }

    /* JADX INFO: renamed from: toULong-s-VKNKU, reason: not valid java name */
    private static final long m11530toULongsVKNKU(long arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toFloat-impl, reason: not valid java name */
    private static final float m11523toFloatimpl(long arg0) {
        return (float) kotlin.UnsignedKt.ulongToDouble(arg0);
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    private static final double m11522toDoubleimpl(long arg0) {
        return kotlin.UnsignedKt.ulongToDouble(arg0);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m11527toStringimpl(long arg0) {
        return kotlin.UnsignedKt.ulongToString(arg0);
    }

    public java.lang.String toString() {
        return m11527toStringimpl(this.data);
    }
}
