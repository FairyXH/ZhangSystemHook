package kotlin;

/* JADX INFO: compiled from: UByte.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0005\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 v2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001vB\u0011\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b\n\u0010\u000bJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\n¢\u0006\u0004\b\u000e\u0010\u000fJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b\u0011\u0010\u0012J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u0018\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b\u001c\u0010\u000fJ\u0018\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b\u001d\u0010\u0012J\u0018\u0010\u001b\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b\u001e\u0010\u001fJ\u0018\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b \u0010\u0018J\u001a\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#HÖ\u0003¢\u0006\u0004\b$\u0010%J\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b'\u0010\u000fJ\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\b¢\u0006\u0004\b(\u0010\u0012J\u0018\u0010&\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b)\u0010\u001fJ\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\b¢\u0006\u0004\b*\u0010\u0018J\u0010\u0010+\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b,\u0010-J\u0013\u0010.\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b/\u0010\u0005J\u0013\u00100\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b1\u0010\u0005J\u0018\u00102\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b3\u0010\u000fJ\u0018\u00102\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b4\u0010\u0012J\u0018\u00102\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b5\u0010\u001fJ\u0018\u00102\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b6\u0010\u0018J\u0018\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b8\u0010\u000bJ\u0018\u00107\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\b¢\u0006\u0004\b9\u0010\u0012J\u0018\u00107\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b:\u0010\u001fJ\u0018\u00107\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\u0016H\u0087\b¢\u0006\u0004\b;\u0010<J\u0018\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b>\u0010\u000bJ\u0018\u0010?\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b@\u0010\u000fJ\u0018\u0010?\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\bA\u0010\u0012J\u0018\u0010?\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\bB\u0010\u001fJ\u0018\u0010?\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bC\u0010\u0018J\u0018\u0010D\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bF\u0010GJ\u0018\u0010H\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bI\u0010GJ\u0018\u0010J\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bK\u0010\u000fJ\u0018\u0010J\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\bL\u0010\u0012J\u0018\u0010J\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\bM\u0010\u001fJ\u0018\u0010J\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bN\u0010\u0018J\u0018\u0010O\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bP\u0010\u000fJ\u0018\u0010O\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\bQ\u0010\u0012J\u0018\u0010O\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\bR\u0010\u001fJ\u0018\u0010O\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bS\u0010\u0018J\u0010\u0010T\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bU\u0010\u0005J\u0010\u0010V\u001a\u00020WH\u0087\b¢\u0006\u0004\bX\u0010YJ\u0010\u0010Z\u001a\u00020[H\u0087\b¢\u0006\u0004\b\\\u0010]J\u0010\u0010^\u001a\u00020\rH\u0087\b¢\u0006\u0004\b_\u0010-J\u0010\u0010`\u001a\u00020aH\u0087\b¢\u0006\u0004\bb\u0010cJ\u0010\u0010d\u001a\u00020eH\u0087\b¢\u0006\u0004\bf\u0010gJ\u000f\u0010h\u001a\u00020iH\u0016¢\u0006\u0004\bj\u0010kJ\u0013\u0010l\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\bm\u0010\u0005J\u0013\u0010n\u001a\u00020\u0010H\u0087\bø\u0001\u0000¢\u0006\u0004\bo\u0010-J\u0013\u0010p\u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\bq\u0010cJ\u0013\u0010r\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\bs\u0010gJ\u0018\u0010t\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bu\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u0082\u0002\u0004\n\u0002\b!¨\u0006w"}, d2 = {"Lkotlin/UByte;", "", "data", "", "constructor-impl", "(B)B", "getData$annotations", "()V", "and", "other", "and-7apg3OU", "(BB)B", "compareTo", "", "compareTo-7apg3OU", "(BB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(BI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(BJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(BS)I", "dec", "dec-w2LRezQ", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(BJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(BLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "(B)I", "inc", "inc-w2LRezQ", "inv", "inv-w2LRezQ", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "(BS)S", "or", "or-7apg3OU", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-7apg3OU", "(BB)Lkotlin/ranges/UIntRange;", "rangeUntil", "rangeUntil-7apg3OU", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "toByte-impl", "toDouble", "", "toDouble-impl", "(B)D", "toFloat", "", "toFloat-impl", "(B)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(B)J", "toShort", "", "toShort-impl", "(B)S", "toString", "", "toString-impl", "(B)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-7apg3OU", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@kotlin.jvm.JvmInline
public final class UByte implements java.lang.Comparable<kotlin.UByte> {
    public static final byte MAX_VALUE = -1;
    public static final byte MIN_VALUE = 0;
    public static final int SIZE_BITS = 8;
    public static final int SIZE_BYTES = 1;
    private final byte data;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ kotlin.UByte m11319boximpl(byte b) {
        return new kotlin.UByte(b);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static byte m11325constructorimpl(byte b) {
        return b;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m11331equalsimpl(byte b, java.lang.Object obj) {
        return (obj instanceof kotlin.UByte) && b == ((kotlin.UByte) obj).getData();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m11332equalsimpl0(byte b, byte b2) {
        return b == b2;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m11337hashCodeimpl(byte b) {
        return java.lang.Byte.hashCode(b);
    }

    public boolean equals(java.lang.Object obj) {
        return m11331equalsimpl(this.data, obj);
    }

    public int hashCode() {
        return m11337hashCodeimpl(this.data);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ byte getData() {
        return this.data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(kotlin.UByte uByte) {
        return kotlin.jvm.internal.Intrinsics.compare(getData() & 255, uByte.getData() & 255);
    }

    private /* synthetic */ UByte(byte data) {
        this.data = data;
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private int m11320compareTo7apg3OU(byte other) {
        return kotlin.jvm.internal.Intrinsics.compare(getData() & 255, other & 255);
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private static int m11321compareTo7apg3OU(byte arg0, byte other) {
        return kotlin.jvm.internal.Intrinsics.compare(arg0 & 255, other & 255);
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private static final int m11324compareToxj2QHRw(byte arg0, short other) {
        return kotlin.jvm.internal.Intrinsics.compare(arg0 & 255, 65535 & other);
    }

    /* JADX INFO: renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private static final int m11323compareToWZ4Q5Ns(byte arg0, int other) {
        return java.lang.Integer.compareUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), other);
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private static final int m11322compareToVKZWuLQ(byte arg0, long other) {
        return java.lang.Long.compareUnsigned(kotlin.ULong.m11481constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: plus-7apg3OU, reason: not valid java name */
    private static final int m11349plus7apg3OU(byte arg0, byte other) {
        return kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(arg0 & 255) + kotlin.UInt.m11402constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: plus-xj2QHRw, reason: not valid java name */
    private static final int m11352plusxj2QHRw(byte arg0, short other) {
        return kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(arg0 & 255) + kotlin.UInt.m11402constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: plus-WZ4Q5Ns, reason: not valid java name */
    private static final int m11351plusWZ4Q5Ns(byte arg0, int other) {
        return kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(arg0 & 255) + other);
    }

    /* JADX INFO: renamed from: plus-VKZWuLQ, reason: not valid java name */
    private static final long m11350plusVKZWuLQ(byte arg0, long other) {
        return kotlin.ULong.m11481constructorimpl(kotlin.ULong.m11481constructorimpl(((long) arg0) & 255) + other);
    }

    /* JADX INFO: renamed from: minus-7apg3OU, reason: not valid java name */
    private static final int m11340minus7apg3OU(byte arg0, byte other) {
        return kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(arg0 & 255) - kotlin.UInt.m11402constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: minus-xj2QHRw, reason: not valid java name */
    private static final int m11343minusxj2QHRw(byte arg0, short other) {
        return kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(arg0 & 255) - kotlin.UInt.m11402constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: minus-WZ4Q5Ns, reason: not valid java name */
    private static final int m11342minusWZ4Q5Ns(byte arg0, int other) {
        return kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(arg0 & 255) - other);
    }

    /* JADX INFO: renamed from: minus-VKZWuLQ, reason: not valid java name */
    private static final long m11341minusVKZWuLQ(byte arg0, long other) {
        return kotlin.ULong.m11481constructorimpl(kotlin.ULong.m11481constructorimpl(((long) arg0) & 255) - other);
    }

    /* JADX INFO: renamed from: times-7apg3OU, reason: not valid java name */
    private static final int m11359times7apg3OU(byte arg0, byte other) {
        return kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(arg0 & 255) * kotlin.UInt.m11402constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: times-xj2QHRw, reason: not valid java name */
    private static final int m11362timesxj2QHRw(byte arg0, short other) {
        return kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(arg0 & 255) * kotlin.UInt.m11402constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: times-WZ4Q5Ns, reason: not valid java name */
    private static final int m11361timesWZ4Q5Ns(byte arg0, int other) {
        return kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(arg0 & 255) * other);
    }

    /* JADX INFO: renamed from: times-VKZWuLQ, reason: not valid java name */
    private static final long m11360timesVKZWuLQ(byte arg0, long other) {
        return kotlin.ULong.m11481constructorimpl(kotlin.ULong.m11481constructorimpl(((long) arg0) & 255) * other);
    }

    /* JADX INFO: renamed from: div-7apg3OU, reason: not valid java name */
    private static final int m11327div7apg3OU(byte arg0, byte other) {
        return java.lang.Integer.divideUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: div-xj2QHRw, reason: not valid java name */
    private static final int m11330divxj2QHRw(byte arg0, short other) {
        return java.lang.Integer.divideUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: div-WZ4Q5Ns, reason: not valid java name */
    private static final int m11329divWZ4Q5Ns(byte arg0, int other) {
        return java.lang.Integer.divideUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), other);
    }

    /* JADX INFO: renamed from: div-VKZWuLQ, reason: not valid java name */
    private static final long m11328divVKZWuLQ(byte arg0, long other) {
        return java.lang.Long.divideUnsigned(kotlin.ULong.m11481constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: rem-7apg3OU, reason: not valid java name */
    private static final int m11355rem7apg3OU(byte arg0, byte other) {
        return java.lang.Integer.remainderUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: rem-xj2QHRw, reason: not valid java name */
    private static final int m11358remxj2QHRw(byte arg0, short other) {
        return java.lang.Integer.remainderUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: rem-WZ4Q5Ns, reason: not valid java name */
    private static final int m11357remWZ4Q5Ns(byte arg0, int other) {
        return java.lang.Integer.remainderUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), other);
    }

    /* JADX INFO: renamed from: rem-VKZWuLQ, reason: not valid java name */
    private static final long m11356remVKZWuLQ(byte arg0, long other) {
        return java.lang.Long.remainderUnsigned(kotlin.ULong.m11481constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: floorDiv-7apg3OU, reason: not valid java name */
    private static final int m11333floorDiv7apg3OU(byte arg0, byte other) {
        return java.lang.Integer.divideUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: floorDiv-xj2QHRw, reason: not valid java name */
    private static final int m11336floorDivxj2QHRw(byte arg0, short other) {
        return java.lang.Integer.divideUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: floorDiv-WZ4Q5Ns, reason: not valid java name */
    private static final int m11335floorDivWZ4Q5Ns(byte arg0, int other) {
        return java.lang.Integer.divideUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), other);
    }

    /* JADX INFO: renamed from: floorDiv-VKZWuLQ, reason: not valid java name */
    private static final long m11334floorDivVKZWuLQ(byte arg0, long other) {
        return java.lang.Long.divideUnsigned(kotlin.ULong.m11481constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: mod-7apg3OU, reason: not valid java name */
    private static final byte m11344mod7apg3OU(byte arg0, byte other) {
        return m11325constructorimpl((byte) java.lang.Integer.remainderUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(other & 255)));
    }

    /* JADX INFO: renamed from: mod-xj2QHRw, reason: not valid java name */
    private static final short m11347modxj2QHRw(byte arg0, short other) {
        return kotlin.UShort.m11588constructorimpl((short) java.lang.Integer.remainderUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(65535 & other)));
    }

    /* JADX INFO: renamed from: mod-WZ4Q5Ns, reason: not valid java name */
    private static final int m11346modWZ4Q5Ns(byte arg0, int other) {
        return java.lang.Integer.remainderUnsigned(kotlin.UInt.m11402constructorimpl(arg0 & 255), other);
    }

    /* JADX INFO: renamed from: mod-VKZWuLQ, reason: not valid java name */
    private static final long m11345modVKZWuLQ(byte arg0, long other) {
        return java.lang.Long.remainderUnsigned(kotlin.ULong.m11481constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: inc-w2LRezQ, reason: not valid java name */
    private static final byte m11338incw2LRezQ(byte arg0) {
        return m11325constructorimpl((byte) (arg0 + 1));
    }

    /* JADX INFO: renamed from: dec-w2LRezQ, reason: not valid java name */
    private static final byte m11326decw2LRezQ(byte arg0) {
        return m11325constructorimpl((byte) (arg0 - 1));
    }

    /* JADX INFO: renamed from: rangeTo-7apg3OU, reason: not valid java name */
    private static final kotlin.ranges.UIntRange m11353rangeTo7apg3OU(byte arg0, byte other) {
        return new kotlin.ranges.UIntRange(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(other & 255), null);
    }

    /* JADX INFO: renamed from: rangeUntil-7apg3OU, reason: not valid java name */
    private static final kotlin.ranges.UIntRange m11354rangeUntil7apg3OU(byte arg0, byte other) {
        return kotlin.ranges.URangesKt.m12575untilJ1ME1BU(kotlin.UInt.m11402constructorimpl(arg0 & 255), kotlin.UInt.m11402constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: and-7apg3OU, reason: not valid java name */
    private static final byte m11318and7apg3OU(byte arg0, byte other) {
        return m11325constructorimpl((byte) (arg0 & other));
    }

    /* JADX INFO: renamed from: or-7apg3OU, reason: not valid java name */
    private static final byte m11348or7apg3OU(byte arg0, byte other) {
        return m11325constructorimpl((byte) (arg0 | other));
    }

    /* JADX INFO: renamed from: xor-7apg3OU, reason: not valid java name */
    private static final byte m11374xor7apg3OU(byte arg0, byte other) {
        return m11325constructorimpl((byte) (arg0 ^ other));
    }

    /* JADX INFO: renamed from: inv-w2LRezQ, reason: not valid java name */
    private static final byte m11339invw2LRezQ(byte arg0) {
        return m11325constructorimpl((byte) (~arg0));
    }

    /* JADX INFO: renamed from: toByte-impl, reason: not valid java name */
    private static final byte m11363toByteimpl(byte arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toShort-impl, reason: not valid java name */
    private static final short m11368toShortimpl(byte arg0) {
        return (short) (arg0 & 255);
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    private static final int m11366toIntimpl(byte arg0) {
        return arg0 & 255;
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    private static final long m11367toLongimpl(byte arg0) {
        return ((long) arg0) & 255;
    }

    /* JADX INFO: renamed from: toUByte-w2LRezQ, reason: not valid java name */
    private static final byte m11370toUBytew2LRezQ(byte arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toUShort-Mh2AYeg, reason: not valid java name */
    private static final short m11373toUShortMh2AYeg(byte arg0) {
        return kotlin.UShort.m11588constructorimpl((short) (arg0 & 255));
    }

    /* JADX INFO: renamed from: toUInt-pVg5ArA, reason: not valid java name */
    private static final int m11371toUIntpVg5ArA(byte arg0) {
        return kotlin.UInt.m11402constructorimpl(arg0 & 255);
    }

    /* JADX INFO: renamed from: toULong-s-VKNKU, reason: not valid java name */
    private static final long m11372toULongsVKNKU(byte arg0) {
        return kotlin.ULong.m11481constructorimpl(((long) arg0) & 255);
    }

    /* JADX INFO: renamed from: toFloat-impl, reason: not valid java name */
    private static final float m11365toFloatimpl(byte arg0) {
        return arg0 & 255;
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    private static final double m11364toDoubleimpl(byte arg0) {
        return arg0 & 255;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m11369toStringimpl(byte arg0) {
        return java.lang.String.valueOf(arg0 & 255);
    }

    public java.lang.String toString() {
        return m11369toStringimpl(this.data);
    }
}
