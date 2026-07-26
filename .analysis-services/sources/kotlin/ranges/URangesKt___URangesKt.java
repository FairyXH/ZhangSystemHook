package kotlin.ranges;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: _URanges.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\n\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0003\u0010\u0004\u001a\u001b\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u0005H\u0007¢\u0006\u0004\b\u0006\u0010\u0007\u001a\u001b\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\bH\u0007¢\u0006\u0004\b\t\u0010\n\u001a\u001b\u0010\u0000\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000bH\u0007¢\u0006\u0004\b\f\u0010\r\u001a\u001b\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0010\u0010\u0004\u001a\u001b\u0010\u000e\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007¢\u0006\u0004\b\u0011\u0010\u0007\u001a\u001b\u0010\u000e\u001a\u00020\b*\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007¢\u0006\u0004\b\u0012\u0010\n\u001a\u001b\u0010\u000e\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007¢\u0006\u0004\b\u0013\u0010\r\u001a#\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0015\u0010\u0016\u001a#\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007¢\u0006\u0004\b\u0017\u0010\u0018\u001a!\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u001aH\u0007¢\u0006\u0004\b\u001b\u0010\u001c\u001a#\u0010\u0014\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007¢\u0006\u0004\b\u001d\u0010\u001e\u001a!\u0010\u0014\u001a\u00020\b*\u00020\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001aH\u0007¢\u0006\u0004\b\u001f\u0010 \u001a#\u0010\u0014\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007¢\u0006\u0004\b!\u0010\"\u001a\u001c\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u0001H\u0087\u0002¢\u0006\u0004\b'\u0010(\u001a\u001c\u0010#\u001a\u00020$*\u00020%2\b\u0010)\u001a\u0004\u0018\u00010\u0005H\u0087\n¢\u0006\u0002\b*\u001a\u001c\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\bH\u0087\u0002¢\u0006\u0004\b+\u0010,\u001a\u001c\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u000bH\u0087\u0002¢\u0006\u0004\b-\u0010.\u001a\u001c\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0001H\u0087\u0002¢\u0006\u0004\b0\u00101\u001a\u001c\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0004\b2\u00103\u001a\u001c\u0010#\u001a\u00020$*\u00020/2\b\u0010)\u001a\u0004\u0018\u00010\bH\u0087\n¢\u0006\u0002\b4\u001a\u001c\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u000bH\u0087\u0002¢\u0006\u0004\b5\u00106\u001a\u001c\u00107\u001a\u000208*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0087\u0004¢\u0006\u0004\b:\u0010;\u001a\u001c\u00107\u001a\u000208*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0087\u0004¢\u0006\u0004\b<\u0010=\u001a\u001c\u00107\u001a\u00020>*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0087\u0004¢\u0006\u0004\b?\u0010@\u001a\u001c\u00107\u001a\u000208*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0087\u0004¢\u0006\u0004\bA\u0010B\u001a\u0011\u0010C\u001a\u00020\u0005*\u000208H\u0007¢\u0006\u0002\u0010D\u001a\u0011\u0010C\u001a\u00020\b*\u00020>H\u0007¢\u0006\u0002\u0010E\u001a\u000e\u0010F\u001a\u0004\u0018\u00010\u0005*\u000208H\u0007\u001a\u000e\u0010F\u001a\u0004\u0018\u00010\b*\u00020>H\u0007\u001a\u0011\u0010G\u001a\u00020\u0005*\u000208H\u0007¢\u0006\u0002\u0010D\u001a\u0011\u0010G\u001a\u00020\b*\u00020>H\u0007¢\u0006\u0002\u0010E\u001a\u000e\u0010H\u001a\u0004\u0018\u00010\u0005*\u000208H\u0007\u001a\u000e\u0010H\u001a\u0004\u0018\u00010\b*\u00020>H\u0007\u001a\u0012\u0010I\u001a\u00020\u0005*\u00020%H\u0087\b¢\u0006\u0002\u0010J\u001a\u0019\u0010I\u001a\u00020\u0005*\u00020%2\u0006\u0010I\u001a\u00020KH\u0007¢\u0006\u0002\u0010L\u001a\u0012\u0010I\u001a\u00020\b*\u00020/H\u0087\b¢\u0006\u0002\u0010M\u001a\u0019\u0010I\u001a\u00020\b*\u00020/2\u0006\u0010I\u001a\u00020KH\u0007¢\u0006\u0002\u0010N\u001a\u000f\u0010O\u001a\u0004\u0018\u00010\u0005*\u00020%H\u0087\b\u001a\u0016\u0010O\u001a\u0004\u0018\u00010\u0005*\u00020%2\u0006\u0010I\u001a\u00020KH\u0007\u001a\u000f\u0010O\u001a\u0004\u0018\u00010\b*\u00020/H\u0087\b\u001a\u0016\u0010O\u001a\u0004\u0018\u00010\b*\u00020/2\u0006\u0010I\u001a\u00020KH\u0007\u001a\f\u0010P\u001a\u000208*\u000208H\u0007\u001a\f\u0010P\u001a\u00020>*\u00020>H\u0007\u001a\u0015\u0010Q\u001a\u000208*\u0002082\u0006\u0010Q\u001a\u00020RH\u0087\u0004\u001a\u0015\u0010Q\u001a\u00020>*\u00020>2\u0006\u0010Q\u001a\u00020SH\u0087\u0004\u001a\u001c\u0010T\u001a\u00020%*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0087\u0004¢\u0006\u0004\bU\u0010V\u001a\u001c\u0010T\u001a\u00020%*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0087\u0004¢\u0006\u0004\bW\u0010X\u001a\u001c\u0010T\u001a\u00020/*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0087\u0004¢\u0006\u0004\bY\u0010Z\u001a\u001c\u0010T\u001a\u00020%*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0087\u0004¢\u0006\u0004\b[\u0010\\¨\u0006]"}, d2 = {"coerceAtLeast", "Lkotlin/UByte;", "minimumValue", "coerceAtLeast-Kr8caGY", "(BB)B", "Lkotlin/UInt;", "coerceAtLeast-J1ME1BU", "(II)I", "Lkotlin/ULong;", "coerceAtLeast-eb3DHEI", "(JJ)J", "Lkotlin/UShort;", "coerceAtLeast-5PvTz6A", "(SS)S", "coerceAtMost", "maximumValue", "coerceAtMost-Kr8caGY", "coerceAtMost-J1ME1BU", "coerceAtMost-eb3DHEI", "coerceAtMost-5PvTz6A", "coerceIn", "coerceIn-b33U2AM", "(BBB)B", "coerceIn-WZ9TVnA", "(III)I", "range", "Lkotlin/ranges/ClosedRange;", "coerceIn-wuiCnnA", "(ILkotlin/ranges/ClosedRange;)I", "coerceIn-sambcqE", "(JJJ)J", "coerceIn-JPwROB0", "(JLkotlin/ranges/ClosedRange;)J", "coerceIn-VKSA0NQ", "(SSS)S", "contains", "", "Lkotlin/ranges/UIntRange;", "value", "contains-68kG9v0", "(Lkotlin/ranges/UIntRange;B)Z", "element", "contains-biwQdVI", "contains-fz5IDCE", "(Lkotlin/ranges/UIntRange;J)Z", "contains-ZsK3CEQ", "(Lkotlin/ranges/UIntRange;S)Z", "Lkotlin/ranges/ULongRange;", "contains-ULb-yJY", "(Lkotlin/ranges/ULongRange;B)Z", "contains-Gab390E", "(Lkotlin/ranges/ULongRange;I)Z", "contains-GYNo2lE", "contains-uhHAxoY", "(Lkotlin/ranges/ULongRange;S)Z", "downTo", "Lkotlin/ranges/UIntProgression;", "to", "downTo-Kr8caGY", "(BB)Lkotlin/ranges/UIntProgression;", "downTo-J1ME1BU", "(II)Lkotlin/ranges/UIntProgression;", "Lkotlin/ranges/ULongProgression;", "downTo-eb3DHEI", "(JJ)Lkotlin/ranges/ULongProgression;", "downTo-5PvTz6A", "(SS)Lkotlin/ranges/UIntProgression;", "first", "(Lkotlin/ranges/UIntProgression;)I", "(Lkotlin/ranges/ULongProgression;)J", "firstOrNull", "last", "lastOrNull", "random", "(Lkotlin/ranges/UIntRange;)I", "Lkotlin/random/Random;", "(Lkotlin/ranges/UIntRange;Lkotlin/random/Random;)I", "(Lkotlin/ranges/ULongRange;)J", "(Lkotlin/ranges/ULongRange;Lkotlin/random/Random;)J", "randomOrNull", "reversed", "step", "", "", "until", "until-Kr8caGY", "(BB)Lkotlin/ranges/UIntRange;", "until-J1ME1BU", "(II)Lkotlin/ranges/UIntRange;", "until-eb3DHEI", "(JJ)Lkotlin/ranges/ULongRange;", "until-5PvTz6A", "(SS)Lkotlin/ranges/UIntRange;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/ranges/URangesKt")
public class URangesKt___URangesKt {
    public static final int first(kotlin.ranges.UIntProgression $this$first) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$first, "<this>");
        if ($this$first.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$first + " is empty.");
        }
        return $this$first.getFirst();
    }

    public static final long first(kotlin.ranges.ULongProgression $this$first) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$first, "<this>");
        if ($this$first.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$first + " is empty.");
        }
        return $this$first.getFirst();
    }

    public static final kotlin.UInt firstOrNull(kotlin.ranges.UIntProgression $this$firstOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$firstOrNull, "<this>");
        if ($this$firstOrNull.isEmpty()) {
            return null;
        }
        return kotlin.UInt.m11396boximpl($this$firstOrNull.getFirst());
    }

    public static final kotlin.ULong firstOrNull(kotlin.ranges.ULongProgression $this$firstOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$firstOrNull, "<this>");
        if ($this$firstOrNull.isEmpty()) {
            return null;
        }
        return kotlin.ULong.m11475boximpl($this$firstOrNull.getFirst());
    }

    public static final int last(kotlin.ranges.UIntProgression $this$last) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$last, "<this>");
        if ($this$last.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$last + " is empty.");
        }
        return $this$last.getLast();
    }

    public static final long last(kotlin.ranges.ULongProgression $this$last) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$last, "<this>");
        if ($this$last.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$last + " is empty.");
        }
        return $this$last.getLast();
    }

    public static final kotlin.UInt lastOrNull(kotlin.ranges.UIntProgression $this$lastOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastOrNull, "<this>");
        if ($this$lastOrNull.isEmpty()) {
            return null;
        }
        return kotlin.UInt.m11396boximpl($this$lastOrNull.getLast());
    }

    public static final kotlin.ULong lastOrNull(kotlin.ranges.ULongProgression $this$lastOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastOrNull, "<this>");
        if ($this$lastOrNull.isEmpty()) {
            return null;
        }
        return kotlin.ULong.m11475boximpl($this$lastOrNull.getLast());
    }

    private static final int random(kotlin.ranges.UIntRange $this$random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        return kotlin.ranges.URangesKt.random($this$random, kotlin.random.Random.INSTANCE);
    }

    private static final long random(kotlin.ranges.ULongRange $this$random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        return kotlin.ranges.URangesKt.random($this$random, kotlin.random.Random.INSTANCE);
    }

    public static final int random(kotlin.ranges.UIntRange $this$random, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        try {
            return kotlin.random.URandomKt.nextUInt(random, $this$random);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.util.NoSuchElementException(e.getMessage());
        }
    }

    public static final long random(kotlin.ranges.ULongRange $this$random, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        try {
            return kotlin.random.URandomKt.nextULong(random, $this$random);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.util.NoSuchElementException(e.getMessage());
        }
    }

    private static final kotlin.UInt randomOrNull(kotlin.ranges.UIntRange $this$randomOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        return kotlin.ranges.URangesKt.randomOrNull($this$randomOrNull, kotlin.random.Random.INSTANCE);
    }

    private static final kotlin.ULong randomOrNull(kotlin.ranges.ULongRange $this$randomOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        return kotlin.ranges.URangesKt.randomOrNull($this$randomOrNull, kotlin.random.Random.INSTANCE);
    }

    public static final kotlin.UInt randomOrNull(kotlin.ranges.UIntRange $this$randomOrNull, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return kotlin.UInt.m11396boximpl(kotlin.random.URandomKt.nextUInt(random, $this$randomOrNull));
    }

    public static final kotlin.ULong randomOrNull(kotlin.ranges.ULongRange $this$randomOrNull, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return kotlin.ULong.m11475boximpl(kotlin.random.URandomKt.nextULong(random, $this$randomOrNull));
    }

    /* JADX INFO: renamed from: contains-biwQdVI, reason: not valid java name */
    private static final boolean m12567containsbiwQdVI(kotlin.ranges.UIntRange contains, kotlin.UInt element) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return element != null && contains.m12535containsWZ4Q5Ns(element.getData());
    }

    /* JADX INFO: renamed from: contains-GYNo2lE, reason: not valid java name */
    private static final boolean m12563containsGYNo2lE(kotlin.ranges.ULongRange contains, kotlin.ULong element) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return element != null && contains.m12544containsVKZWuLQ(element.getData());
    }

    /* JADX INFO: renamed from: contains-68kG9v0, reason: not valid java name */
    public static final boolean m12562contains68kG9v0(kotlin.ranges.UIntRange contains, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return contains.m12535containsWZ4Q5Ns(kotlin.UInt.m11402constructorimpl(value & 255));
    }

    /* JADX INFO: renamed from: contains-ULb-yJY, reason: not valid java name */
    public static final boolean m12565containsULbyJY(kotlin.ranges.ULongRange contains, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return contains.m12544containsVKZWuLQ(kotlin.ULong.m11481constructorimpl(((long) value) & 255));
    }

    /* JADX INFO: renamed from: contains-Gab390E, reason: not valid java name */
    public static final boolean m12564containsGab390E(kotlin.ranges.ULongRange contains, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return contains.m12544containsVKZWuLQ(kotlin.ULong.m11481constructorimpl(((long) value) & 4294967295L));
    }

    /* JADX INFO: renamed from: contains-fz5IDCE, reason: not valid java name */
    public static final boolean m12568containsfz5IDCE(kotlin.ranges.UIntRange contains, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return kotlin.ULong.m11481constructorimpl(value >>> 32) == 0 && contains.m12535containsWZ4Q5Ns(kotlin.UInt.m11402constructorimpl((int) value));
    }

    /* JADX INFO: renamed from: contains-ZsK3CEQ, reason: not valid java name */
    public static final boolean m12566containsZsK3CEQ(kotlin.ranges.UIntRange contains, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return contains.m12535containsWZ4Q5Ns(kotlin.UInt.m11402constructorimpl(65535 & value));
    }

    /* JADX INFO: renamed from: contains-uhHAxoY, reason: not valid java name */
    public static final boolean m12569containsuhHAxoY(kotlin.ranges.ULongRange contains, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return contains.m12544containsVKZWuLQ(kotlin.ULong.m11481constructorimpl(((long) value) & 65535));
    }

    /* JADX INFO: renamed from: downTo-Kr8caGY, reason: not valid java name */
    public static final kotlin.ranges.UIntProgression m12572downToKr8caGY(byte $this$downTo_u2dKr8caGY, byte to) {
        return kotlin.ranges.UIntProgression.INSTANCE.m12532fromClosedRangeNkh28Cs(kotlin.UInt.m11402constructorimpl($this$downTo_u2dKr8caGY & 255), kotlin.UInt.m11402constructorimpl(to & 255), -1);
    }

    /* JADX INFO: renamed from: downTo-J1ME1BU, reason: not valid java name */
    public static final kotlin.ranges.UIntProgression m12571downToJ1ME1BU(int $this$downTo_u2dJ1ME1BU, int to) {
        return kotlin.ranges.UIntProgression.INSTANCE.m12532fromClosedRangeNkh28Cs($this$downTo_u2dJ1ME1BU, to, -1);
    }

    /* JADX INFO: renamed from: downTo-eb3DHEI, reason: not valid java name */
    public static final kotlin.ranges.ULongProgression m12573downToeb3DHEI(long $this$downTo_u2deb3DHEI, long to) {
        return kotlin.ranges.ULongProgression.INSTANCE.m12541fromClosedRange7ftBX0g($this$downTo_u2deb3DHEI, to, -1L);
    }

    /* JADX INFO: renamed from: downTo-5PvTz6A, reason: not valid java name */
    public static final kotlin.ranges.UIntProgression m12570downTo5PvTz6A(short $this$downTo_u2d5PvTz6A, short to) {
        return kotlin.ranges.UIntProgression.INSTANCE.m12532fromClosedRangeNkh28Cs(kotlin.UInt.m11402constructorimpl($this$downTo_u2d5PvTz6A & 65535), kotlin.UInt.m11402constructorimpl(65535 & to), -1);
    }

    public static final kotlin.ranges.UIntProgression reversed(kotlin.ranges.UIntProgression $this$reversed) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reversed, "<this>");
        return kotlin.ranges.UIntProgression.INSTANCE.m12532fromClosedRangeNkh28Cs($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final kotlin.ranges.ULongProgression reversed(kotlin.ranges.ULongProgression $this$reversed) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reversed, "<this>");
        return kotlin.ranges.ULongProgression.INSTANCE.m12541fromClosedRange7ftBX0g($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final kotlin.ranges.UIntProgression step(kotlin.ranges.UIntProgression $this$step, int step) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$step, "<this>");
        kotlin.ranges.RangesKt.checkStepIsPositive(step > 0, java.lang.Integer.valueOf(step));
        return kotlin.ranges.UIntProgression.INSTANCE.m12532fromClosedRangeNkh28Cs($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    public static final kotlin.ranges.ULongProgression step(kotlin.ranges.ULongProgression $this$step, long step) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$step, "<this>");
        kotlin.ranges.RangesKt.checkStepIsPositive(step > 0, java.lang.Long.valueOf(step));
        return kotlin.ranges.ULongProgression.INSTANCE.m12541fromClosedRange7ftBX0g($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    /* JADX INFO: renamed from: until-Kr8caGY, reason: not valid java name */
    public static final kotlin.ranges.UIntRange m12576untilKr8caGY(byte $this$until_u2dKr8caGY, byte to) {
        return kotlin.jvm.internal.Intrinsics.compare(to & 255, 0) <= 0 ? kotlin.ranges.UIntRange.INSTANCE.getEMPTY() : new kotlin.ranges.UIntRange(kotlin.UInt.m11402constructorimpl($this$until_u2dKr8caGY & 255), kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(to & 255) - 1), null);
    }

    /* JADX INFO: renamed from: until-J1ME1BU, reason: not valid java name */
    public static final kotlin.ranges.UIntRange m12575untilJ1ME1BU(int $this$until_u2dJ1ME1BU, int to) {
        return java.lang.Integer.compareUnsigned(to, 0) <= 0 ? kotlin.ranges.UIntRange.INSTANCE.getEMPTY() : new kotlin.ranges.UIntRange($this$until_u2dJ1ME1BU, kotlin.UInt.m11402constructorimpl(to - 1), null);
    }

    /* JADX INFO: renamed from: until-eb3DHEI, reason: not valid java name */
    public static final kotlin.ranges.ULongRange m12577untileb3DHEI(long $this$until_u2deb3DHEI, long to) {
        return java.lang.Long.compareUnsigned(to, 0L) <= 0 ? kotlin.ranges.ULongRange.INSTANCE.getEMPTY() : new kotlin.ranges.ULongRange($this$until_u2deb3DHEI, kotlin.ULong.m11481constructorimpl(to - kotlin.ULong.m11481constructorimpl(((long) 1) & 4294967295L)), null);
    }

    /* JADX INFO: renamed from: until-5PvTz6A, reason: not valid java name */
    public static final kotlin.ranges.UIntRange m12574until5PvTz6A(short $this$until_u2d5PvTz6A, short to) {
        return kotlin.jvm.internal.Intrinsics.compare(to & 65535, 0) <= 0 ? kotlin.ranges.UIntRange.INSTANCE.getEMPTY() : new kotlin.ranges.UIntRange(kotlin.UInt.m11402constructorimpl($this$until_u2d5PvTz6A & 65535), kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(65535 & to) - 1), null);
    }

    /* JADX INFO: renamed from: coerceAtLeast-J1ME1BU, reason: not valid java name */
    public static final int m12549coerceAtLeastJ1ME1BU(int $this$coerceAtLeast_u2dJ1ME1BU, int minimumValue) {
        return java.lang.Integer.compareUnsigned($this$coerceAtLeast_u2dJ1ME1BU, minimumValue) < 0 ? minimumValue : $this$coerceAtLeast_u2dJ1ME1BU;
    }

    /* JADX INFO: renamed from: coerceAtLeast-eb3DHEI, reason: not valid java name */
    public static final long m12551coerceAtLeasteb3DHEI(long $this$coerceAtLeast_u2deb3DHEI, long minimumValue) {
        return java.lang.Long.compareUnsigned($this$coerceAtLeast_u2deb3DHEI, minimumValue) < 0 ? minimumValue : $this$coerceAtLeast_u2deb3DHEI;
    }

    /* JADX INFO: renamed from: coerceAtLeast-Kr8caGY, reason: not valid java name */
    public static final byte m12550coerceAtLeastKr8caGY(byte $this$coerceAtLeast_u2dKr8caGY, byte minimumValue) {
        return kotlin.jvm.internal.Intrinsics.compare($this$coerceAtLeast_u2dKr8caGY & 255, minimumValue & 255) < 0 ? minimumValue : $this$coerceAtLeast_u2dKr8caGY;
    }

    /* JADX INFO: renamed from: coerceAtLeast-5PvTz6A, reason: not valid java name */
    public static final short m12548coerceAtLeast5PvTz6A(short $this$coerceAtLeast_u2d5PvTz6A, short minimumValue) {
        return kotlin.jvm.internal.Intrinsics.compare($this$coerceAtLeast_u2d5PvTz6A & 65535, 65535 & minimumValue) < 0 ? minimumValue : $this$coerceAtLeast_u2d5PvTz6A;
    }

    /* JADX INFO: renamed from: coerceAtMost-J1ME1BU, reason: not valid java name */
    public static final int m12553coerceAtMostJ1ME1BU(int $this$coerceAtMost_u2dJ1ME1BU, int maximumValue) {
        return java.lang.Integer.compareUnsigned($this$coerceAtMost_u2dJ1ME1BU, maximumValue) > 0 ? maximumValue : $this$coerceAtMost_u2dJ1ME1BU;
    }

    /* JADX INFO: renamed from: coerceAtMost-eb3DHEI, reason: not valid java name */
    public static final long m12555coerceAtMosteb3DHEI(long $this$coerceAtMost_u2deb3DHEI, long maximumValue) {
        return java.lang.Long.compareUnsigned($this$coerceAtMost_u2deb3DHEI, maximumValue) > 0 ? maximumValue : $this$coerceAtMost_u2deb3DHEI;
    }

    /* JADX INFO: renamed from: coerceAtMost-Kr8caGY, reason: not valid java name */
    public static final byte m12554coerceAtMostKr8caGY(byte $this$coerceAtMost_u2dKr8caGY, byte maximumValue) {
        return kotlin.jvm.internal.Intrinsics.compare($this$coerceAtMost_u2dKr8caGY & 255, maximumValue & 255) > 0 ? maximumValue : $this$coerceAtMost_u2dKr8caGY;
    }

    /* JADX INFO: renamed from: coerceAtMost-5PvTz6A, reason: not valid java name */
    public static final short m12552coerceAtMost5PvTz6A(short $this$coerceAtMost_u2d5PvTz6A, short maximumValue) {
        return kotlin.jvm.internal.Intrinsics.compare($this$coerceAtMost_u2d5PvTz6A & 65535, 65535 & maximumValue) > 0 ? maximumValue : $this$coerceAtMost_u2d5PvTz6A;
    }

    /* JADX INFO: renamed from: coerceIn-WZ9TVnA, reason: not valid java name */
    public static final int m12558coerceInWZ9TVnA(int $this$coerceIn_u2dWZ9TVnA, int minimumValue, int maximumValue) {
        if (java.lang.Integer.compareUnsigned(minimumValue, maximumValue) <= 0) {
            return java.lang.Integer.compareUnsigned($this$coerceIn_u2dWZ9TVnA, minimumValue) < 0 ? minimumValue : java.lang.Integer.compareUnsigned($this$coerceIn_u2dWZ9TVnA, maximumValue) > 0 ? maximumValue : $this$coerceIn_u2dWZ9TVnA;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ((java.lang.Object) kotlin.UInt.m11448toStringimpl(maximumValue)) + " is less than minimum " + ((java.lang.Object) kotlin.UInt.m11448toStringimpl(minimumValue)) + '.');
    }

    /* JADX INFO: renamed from: coerceIn-sambcqE, reason: not valid java name */
    public static final long m12560coerceInsambcqE(long $this$coerceIn_u2dsambcqE, long minimumValue, long maximumValue) {
        if (java.lang.Long.compareUnsigned(minimumValue, maximumValue) <= 0) {
            return java.lang.Long.compareUnsigned($this$coerceIn_u2dsambcqE, minimumValue) < 0 ? minimumValue : java.lang.Long.compareUnsigned($this$coerceIn_u2dsambcqE, maximumValue) > 0 ? maximumValue : $this$coerceIn_u2dsambcqE;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ((java.lang.Object) kotlin.ULong.m11527toStringimpl(maximumValue)) + " is less than minimum " + ((java.lang.Object) kotlin.ULong.m11527toStringimpl(minimumValue)) + '.');
    }

    /* JADX INFO: renamed from: coerceIn-b33U2AM, reason: not valid java name */
    public static final byte m12559coerceInb33U2AM(byte $this$coerceIn_u2db33U2AM, byte minimumValue, byte maximumValue) {
        if (kotlin.jvm.internal.Intrinsics.compare(minimumValue & 255, maximumValue & 255) <= 0) {
            return kotlin.jvm.internal.Intrinsics.compare($this$coerceIn_u2db33U2AM & 255, minimumValue & 255) < 0 ? minimumValue : kotlin.jvm.internal.Intrinsics.compare($this$coerceIn_u2db33U2AM & 255, maximumValue & 255) > 0 ? maximumValue : $this$coerceIn_u2db33U2AM;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ((java.lang.Object) kotlin.UByte.m11369toStringimpl(maximumValue)) + " is less than minimum " + ((java.lang.Object) kotlin.UByte.m11369toStringimpl(minimumValue)) + '.');
    }

    /* JADX INFO: renamed from: coerceIn-VKSA0NQ, reason: not valid java name */
    public static final short m12557coerceInVKSA0NQ(short $this$coerceIn_u2dVKSA0NQ, short minimumValue, short maximumValue) {
        if (kotlin.jvm.internal.Intrinsics.compare(minimumValue & 65535, maximumValue & 65535) <= 0) {
            return kotlin.jvm.internal.Intrinsics.compare($this$coerceIn_u2dVKSA0NQ & 65535, minimumValue & 65535) < 0 ? minimumValue : kotlin.jvm.internal.Intrinsics.compare($this$coerceIn_u2dVKSA0NQ & 65535, 65535 & maximumValue) > 0 ? maximumValue : $this$coerceIn_u2dVKSA0NQ;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ((java.lang.Object) kotlin.UShort.m11632toStringimpl(maximumValue)) + " is less than minimum " + ((java.lang.Object) kotlin.UShort.m11632toStringimpl(minimumValue)) + '.');
    }

    /* JADX INFO: renamed from: coerceIn-wuiCnnA, reason: not valid java name */
    public static final int m12561coerceInwuiCnnA(int $this$coerceIn_u2dwuiCnnA, kotlin.ranges.ClosedRange<kotlin.UInt> range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range instanceof kotlin.ranges.ClosedFloatingPointRange) {
            return ((kotlin.UInt) kotlin.ranges.RangesKt.coerceIn(kotlin.UInt.m11396boximpl($this$coerceIn_u2dwuiCnnA), (kotlin.ranges.ClosedFloatingPointRange<kotlin.UInt>) range)).getData();
        }
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return java.lang.Integer.compareUnsigned($this$coerceIn_u2dwuiCnnA, ((kotlin.UInt) range.getStart()).getData()) < 0 ? ((kotlin.UInt) range.getStart()).getData() : java.lang.Integer.compareUnsigned($this$coerceIn_u2dwuiCnnA, ((kotlin.UInt) range.getEndInclusive()).getData()) > 0 ? ((kotlin.UInt) range.getEndInclusive()).getData() : $this$coerceIn_u2dwuiCnnA;
    }

    /* JADX INFO: renamed from: coerceIn-JPwROB0, reason: not valid java name */
    public static final long m12556coerceInJPwROB0(long $this$coerceIn_u2dJPwROB0, kotlin.ranges.ClosedRange<kotlin.ULong> range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range instanceof kotlin.ranges.ClosedFloatingPointRange) {
            return ((kotlin.ULong) kotlin.ranges.RangesKt.coerceIn(kotlin.ULong.m11475boximpl($this$coerceIn_u2dJPwROB0), (kotlin.ranges.ClosedFloatingPointRange<kotlin.ULong>) range)).getData();
        }
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return java.lang.Long.compareUnsigned($this$coerceIn_u2dJPwROB0, ((kotlin.ULong) range.getStart()).getData()) < 0 ? ((kotlin.ULong) range.getStart()).getData() : java.lang.Long.compareUnsigned($this$coerceIn_u2dJPwROB0, ((kotlin.ULong) range.getEndInclusive()).getData()) > 0 ? ((kotlin.ULong) range.getEndInclusive()).getData() : $this$coerceIn_u2dJPwROB0;
    }
}
