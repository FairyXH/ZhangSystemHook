package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: UShort.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\n\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 v2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001vB\u0011\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b\n\u0010\u000bJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b\u000f\u0010\u0010J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u0012\u0010\u0013J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\b\u0015\u0010\u0016J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\n¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u0018\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b\u001c\u0010\u0010J\u0018\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u001d\u0010\u0013J\u0018\u0010\u001b\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\b\u001e\u0010\u001fJ\u0018\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b \u0010\u0018J\u001a\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#HÖ\u0003¢\u0006\u0004\b$\u0010%J\u0018\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\b¢\u0006\u0004\b'\u0010\u0010J\u0018\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b(\u0010\u0013J\u0018\u0010&\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\b¢\u0006\u0004\b)\u0010\u001fJ\u0018\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b*\u0010\u0018J\u0010\u0010+\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b,\u0010-J\u0013\u0010.\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b/\u0010\u0005J\u0013\u00100\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b1\u0010\u0005J\u0018\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b3\u0010\u0010J\u0018\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b4\u0010\u0013J\u0018\u00102\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\b5\u0010\u001fJ\u0018\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b6\u0010\u0018J\u0018\u00107\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000eH\u0087\b¢\u0006\u0004\b8\u00109J\u0018\u00107\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b:\u0010\u0013J\u0018\u00107\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\b¢\u0006\u0004\b;\u0010\u001fJ\u0018\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b<\u0010\u000bJ\u0018\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b>\u0010\u000bJ\u0018\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b@\u0010\u0010J\u0018\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bA\u0010\u0013J\u0018\u0010?\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\bB\u0010\u001fJ\u0018\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bC\u0010\u0018J\u0018\u0010D\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bF\u0010GJ\u0018\u0010H\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bI\u0010GJ\u0018\u0010J\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bK\u0010\u0010J\u0018\u0010J\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bL\u0010\u0013J\u0018\u0010J\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\bM\u0010\u001fJ\u0018\u0010J\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bN\u0010\u0018J\u0018\u0010O\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bP\u0010\u0010J\u0018\u0010O\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bQ\u0010\u0013J\u0018\u0010O\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\bR\u0010\u001fJ\u0018\u0010O\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bS\u0010\u0018J\u0010\u0010T\u001a\u00020UH\u0087\b¢\u0006\u0004\bV\u0010WJ\u0010\u0010X\u001a\u00020YH\u0087\b¢\u0006\u0004\bZ\u0010[J\u0010\u0010\\\u001a\u00020]H\u0087\b¢\u0006\u0004\b^\u0010_J\u0010\u0010`\u001a\u00020\rH\u0087\b¢\u0006\u0004\ba\u0010-J\u0010\u0010b\u001a\u00020cH\u0087\b¢\u0006\u0004\bd\u0010eJ\u0010\u0010f\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bg\u0010\u0005J\u000f\u0010h\u001a\u00020iH\u0016¢\u0006\u0004\bj\u0010kJ\u0013\u0010l\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\bm\u0010WJ\u0013\u0010n\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\bo\u0010-J\u0013\u0010p\u001a\u00020\u0014H\u0087\bø\u0001\u0000¢\u0006\u0004\bq\u0010eJ\u0013\u0010r\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\bs\u0010\u0005J\u0018\u0010t\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bu\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u0082\u0002\u0004\n\u0002\b!¨\u0006w"}, d2 = {"Lkotlin/UShort;", "", "data", "", "constructor-impl", "(S)S", "getData$annotations", "()V", "and", "other", "and-xj2QHRw", "(SS)S", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(SB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(SI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(SJ)I", "compareTo-xj2QHRw", "(SS)I", "dec", "dec-Mh2AYeg", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(SJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(SLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "(S)I", "inc", "inc-Mh2AYeg", "inv", "inv-Mh2AYeg", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(SB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "or", "or-xj2QHRw", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-xj2QHRw", "(SS)Lkotlin/ranges/UIntRange;", "rangeUntil", "rangeUntil-xj2QHRw", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(S)B", "toDouble", "", "toDouble-impl", "(S)D", "toFloat", "", "toFloat-impl", "(S)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(S)J", "toShort", "toShort-impl", "toString", "", "toString-impl", "(S)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-xj2QHRw", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@com.android.server.permission.jarjar.kotlin.jvm.JvmInline
public final class UShort implements java.lang.Comparable<com.android.server.permission.jarjar.kotlin.UShort> {
    public static final com.android.server.permission.jarjar.kotlin.UShort.Companion Companion = new com.android.server.permission.jarjar.kotlin.UShort.Companion(null);
    public static final short MAX_VALUE = -1;
    public static final short MIN_VALUE = 0;
    public static final int SIZE_BITS = 16;
    public static final int SIZE_BYTES = 2;
    private final short data;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UShort m6364boximpl(short s) {
        return new com.android.server.permission.jarjar.kotlin.UShort(s);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static short m6370constructorimpl(short s) {
        return s;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m6376equalsimpl(short s, java.lang.Object obj) {
        return (obj instanceof com.android.server.permission.jarjar.kotlin.UShort) && s == ((com.android.server.permission.jarjar.kotlin.UShort) obj).m6420unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m6377equalsimpl0(short s, short s2) {
        return s == s2;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m6382hashCodeimpl(short s) {
        return java.lang.Short.hashCode(s);
    }

    public boolean equals(java.lang.Object obj) {
        return m6376equalsimpl(this.data, obj);
    }

    public int hashCode() {
        return m6382hashCodeimpl(this.data);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ short m6420unboximpl() {
        return this.data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(com.android.server.permission.jarjar.kotlin.UShort uShort) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(m6420unboximpl() & 65535, uShort.m6420unboximpl() & 65535);
    }

    private /* synthetic */ UShort(short data) {
        this.data = data;
    }

    /* JADX INFO: compiled from: UShort.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\n"}, d2 = {"Lkotlin/UShort$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UShort;", "S", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private static final int m6365compareTo7apg3OU(short arg0, byte other) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(65535 & arg0, other & 255);
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private int m6368compareToxj2QHRw(short other) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(m6420unboximpl() & 65535, 65535 & other);
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private static int m6369compareToxj2QHRw(short arg0, short other) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(arg0 & 65535, 65535 & other);
    }

    /* JADX INFO: renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private static final int m6367compareToWZ4Q5Ns(short arg0, int other) {
        return java.lang.Integer.compareUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0), other);
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private static final int m6366compareToVKZWuLQ(short arg0, long other) {
        return java.lang.Long.compareUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 65535), other);
    }

    /* JADX INFO: renamed from: plus-7apg3OU, reason: not valid java name */
    private static final int m6394plus7apg3OU(short arg0, byte other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0) + com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: plus-xj2QHRw, reason: not valid java name */
    private static final int m6397plusxj2QHRw(short arg0, short other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0 & 65535) + com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: plus-WZ4Q5Ns, reason: not valid java name */
    private static final int m6396plusWZ4Q5Ns(short arg0, int other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0) + other);
    }

    /* JADX INFO: renamed from: plus-VKZWuLQ, reason: not valid java name */
    private static final long m6395plusVKZWuLQ(short arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 65535) + other);
    }

    /* JADX INFO: renamed from: minus-7apg3OU, reason: not valid java name */
    private static final int m6385minus7apg3OU(short arg0, byte other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0) - com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: minus-xj2QHRw, reason: not valid java name */
    private static final int m6388minusxj2QHRw(short arg0, short other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0 & 65535) - com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: minus-WZ4Q5Ns, reason: not valid java name */
    private static final int m6387minusWZ4Q5Ns(short arg0, int other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0) - other);
    }

    /* JADX INFO: renamed from: minus-VKZWuLQ, reason: not valid java name */
    private static final long m6386minusVKZWuLQ(short arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 65535) - other);
    }

    /* JADX INFO: renamed from: times-7apg3OU, reason: not valid java name */
    private static final int m6404times7apg3OU(short arg0, byte other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0) * com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: times-xj2QHRw, reason: not valid java name */
    private static final int m6407timesxj2QHRw(short arg0, short other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0 & 65535) * com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: times-WZ4Q5Ns, reason: not valid java name */
    private static final int m6406timesWZ4Q5Ns(short arg0, int other) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0) * other);
    }

    /* JADX INFO: renamed from: times-VKZWuLQ, reason: not valid java name */
    private static final long m6405timesVKZWuLQ(short arg0, long other) {
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 65535) * other);
    }

    /* JADX INFO: renamed from: div-7apg3OU, reason: not valid java name */
    private static final int m6372div7apg3OU(short arg0, byte other) {
        return java.lang.Integer.divideUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: div-xj2QHRw, reason: not valid java name */
    private static final int m6375divxj2QHRw(short arg0, short other) {
        return java.lang.Integer.divideUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0 & 65535), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: div-WZ4Q5Ns, reason: not valid java name */
    private static final int m6374divWZ4Q5Ns(short arg0, int other) {
        return java.lang.Integer.divideUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0), other);
    }

    /* JADX INFO: renamed from: div-VKZWuLQ, reason: not valid java name */
    private static final long m6373divVKZWuLQ(short arg0, long other) {
        return java.lang.Long.divideUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 65535), other);
    }

    /* JADX INFO: renamed from: rem-7apg3OU, reason: not valid java name */
    private static final int m6400rem7apg3OU(short arg0, byte other) {
        return java.lang.Integer.remainderUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: rem-xj2QHRw, reason: not valid java name */
    private static final int m6403remxj2QHRw(short arg0, short other) {
        return java.lang.Integer.remainderUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0 & 65535), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: rem-WZ4Q5Ns, reason: not valid java name */
    private static final int m6402remWZ4Q5Ns(short arg0, int other) {
        return java.lang.Integer.remainderUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0), other);
    }

    /* JADX INFO: renamed from: rem-VKZWuLQ, reason: not valid java name */
    private static final long m6401remVKZWuLQ(short arg0, long other) {
        return java.lang.Long.remainderUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 65535), other);
    }

    /* JADX INFO: renamed from: floorDiv-7apg3OU, reason: not valid java name */
    private static final int m6378floorDiv7apg3OU(short arg0, byte other) {
        return java.lang.Integer.divideUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(other & 255));
    }

    /* JADX INFO: renamed from: floorDiv-xj2QHRw, reason: not valid java name */
    private static final int m6381floorDivxj2QHRw(short arg0, short other) {
        return java.lang.Integer.divideUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0 & 65535), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: floorDiv-WZ4Q5Ns, reason: not valid java name */
    private static final int m6380floorDivWZ4Q5Ns(short arg0, int other) {
        return java.lang.Integer.divideUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0), other);
    }

    /* JADX INFO: renamed from: floorDiv-VKZWuLQ, reason: not valid java name */
    private static final long m6379floorDivVKZWuLQ(short arg0, long other) {
        return java.lang.Long.divideUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 65535), other);
    }

    /* JADX INFO: renamed from: mod-7apg3OU, reason: not valid java name */
    private static final byte m6389mod7apg3OU(short arg0, byte other) {
        return com.android.server.permission.jarjar.kotlin.UByte.m6107constructorimpl((byte) java.lang.Integer.remainderUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(other & 255)));
    }

    /* JADX INFO: renamed from: mod-xj2QHRw, reason: not valid java name */
    private static final short m6392modxj2QHRw(short arg0, short other) {
        return m6370constructorimpl((short) java.lang.Integer.remainderUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0 & 65535), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & other)));
    }

    /* JADX INFO: renamed from: mod-WZ4Q5Ns, reason: not valid java name */
    private static final int m6391modWZ4Q5Ns(short arg0, int other) {
        return java.lang.Integer.remainderUnsigned(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0), other);
    }

    /* JADX INFO: renamed from: mod-VKZWuLQ, reason: not valid java name */
    private static final long m6390modVKZWuLQ(short arg0, long other) {
        return java.lang.Long.remainderUnsigned(com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 65535), other);
    }

    /* JADX INFO: renamed from: inc-Mh2AYeg, reason: not valid java name */
    private static final short m6383incMh2AYeg(short arg0) {
        return m6370constructorimpl((short) (arg0 + 1));
    }

    /* JADX INFO: renamed from: dec-Mh2AYeg, reason: not valid java name */
    private static final short m6371decMh2AYeg(short arg0) {
        return m6370constructorimpl((short) (arg0 - 1));
    }

    /* JADX INFO: renamed from: rangeTo-xj2QHRw, reason: not valid java name */
    private static final com.android.server.permission.jarjar.kotlin.ranges.UIntRange m6398rangeToxj2QHRw(short arg0, short other) {
        return new com.android.server.permission.jarjar.kotlin.ranges.UIntRange(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0 & 65535), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & other), null);
    }

    /* JADX INFO: renamed from: rangeUntil-xj2QHRw, reason: not valid java name */
    private static final com.android.server.permission.jarjar.kotlin.ranges.UIntRange m6399rangeUntilxj2QHRw(short arg0, short other) {
        return com.android.server.permission.jarjar.kotlin.ranges.URangesKt.m7357untilJ1ME1BU(com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0 & 65535), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: and-xj2QHRw, reason: not valid java name */
    private static final short m6363andxj2QHRw(short arg0, short other) {
        return m6370constructorimpl((short) (arg0 & other));
    }

    /* JADX INFO: renamed from: or-xj2QHRw, reason: not valid java name */
    private static final short m6393orxj2QHRw(short arg0, short other) {
        return m6370constructorimpl((short) (arg0 | other));
    }

    /* JADX INFO: renamed from: xor-xj2QHRw, reason: not valid java name */
    private static final short m6419xorxj2QHRw(short arg0, short other) {
        return m6370constructorimpl((short) (arg0 ^ other));
    }

    /* JADX INFO: renamed from: inv-Mh2AYeg, reason: not valid java name */
    private static final short m6384invMh2AYeg(short arg0) {
        return m6370constructorimpl((short) (~arg0));
    }

    /* JADX INFO: renamed from: toByte-impl, reason: not valid java name */
    private static final byte m6408toByteimpl(short arg0) {
        return (byte) arg0;
    }

    /* JADX INFO: renamed from: toShort-impl, reason: not valid java name */
    private static final short m6413toShortimpl(short arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    private static final int m6411toIntimpl(short arg0) {
        return 65535 & arg0;
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    private static final long m6412toLongimpl(short arg0) {
        return ((long) arg0) & 65535;
    }

    /* JADX INFO: renamed from: toUByte-w2LRezQ, reason: not valid java name */
    private static final byte m6415toUBytew2LRezQ(short arg0) {
        return com.android.server.permission.jarjar.kotlin.UByte.m6107constructorimpl((byte) arg0);
    }

    /* JADX INFO: renamed from: toUShort-Mh2AYeg, reason: not valid java name */
    private static final short m6418toUShortMh2AYeg(short arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toUInt-pVg5ArA, reason: not valid java name */
    private static final int m6416toUIntpVg5ArA(short arg0) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(65535 & arg0);
    }

    /* JADX INFO: renamed from: toULong-s-VKNKU, reason: not valid java name */
    private static final long m6417toULongsVKNKU(short arg0) {
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) arg0) & 65535);
    }

    /* JADX INFO: renamed from: toFloat-impl, reason: not valid java name */
    private static final float m6410toFloatimpl(short arg0) {
        return 65535 & arg0;
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    private static final double m6409toDoubleimpl(short arg0) {
        return 65535 & arg0;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m6414toStringimpl(short arg0) {
        return java.lang.String.valueOf(65535 & arg0);
    }

    public java.lang.String toString() {
        return m6414toStringimpl(this.data);
    }
}
