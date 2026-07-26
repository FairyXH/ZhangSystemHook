package kotlin.time;

/* JADX INFO: compiled from: TimeSource.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u0000 \u00042\u00020\u0001:\u0003\u0004\u0005\u0006J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0007"}, d2 = {"Lkotlin/time/TimeSource;", "", "markNow", "Lkotlin/time/TimeMark;", "Companion", "Monotonic", "WithComparableMarks", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface TimeSource {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.time.TimeSource.Companion INSTANCE = kotlin.time.TimeSource.Companion.$$INSTANCE;

    /* JADX INFO: compiled from: TimeSource.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0004"}, d2 = {"Lkotlin/time/TimeSource$WithComparableMarks;", "Lkotlin/time/TimeSource;", "markNow", "Lkotlin/time/ComparableTimeMark;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface WithComparableMarks extends kotlin.time.TimeSource {
        @Override // kotlin.time.TimeSource
        kotlin.time.ComparableTimeMark markNow();
    }

    kotlin.time.TimeMark markNow();

    /* JADX INFO: compiled from: TimeSource.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\tB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u0004H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016\u0082\u0002\u0004\n\u0002\b!¨\u0006\n"}, d2 = {"Lkotlin/time/TimeSource$Monotonic;", "Lkotlin/time/TimeSource$WithComparableMarks;", "()V", "markNow", "Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "markNow-z9LOYto", "()J", "toString", "", "ValueTimeMark", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Monotonic implements kotlin.time.TimeSource.WithComparableMarks {
        public static final kotlin.time.TimeSource.Monotonic INSTANCE = new kotlin.time.TimeSource.Monotonic();

        private Monotonic() {
        }

        @Override // kotlin.time.TimeSource.WithComparableMarks, kotlin.time.TimeSource
        public /* bridge */ /* synthetic */ kotlin.time.ComparableTimeMark markNow() {
            return kotlin.time.TimeSource.Monotonic.ValueTimeMark.m12773boximpl(m12772markNowz9LOYto());
        }

        @Override // kotlin.time.TimeSource
        public /* bridge */ /* synthetic */ kotlin.time.TimeMark markNow() {
            return kotlin.time.TimeSource.Monotonic.ValueTimeMark.m12773boximpl(m12772markNowz9LOYto());
        }

        /* JADX INFO: renamed from: markNow-z9LOYto, reason: not valid java name */
        public long m12772markNowz9LOYto() {
            return kotlin.time.MonotonicTimeSource.INSTANCE.m12767markNowz9LOYto();
        }

        public java.lang.String toString() {
            return kotlin.time.MonotonicTimeSource.INSTANCE.toString();
        }

        /* JADX INFO: compiled from: TimeSource.kt */
        @kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0014\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0087@\u0018\u00002\u00020\u0001B\u0015\b\u0000\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004¢\u0006\u0004\b\u0005\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0012\u0010\f\u001a\u00020\rH\u0016ø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u0006J\u001a\u0010\u000f\u001a\u00020\u00102\b\u0010\t\u001a\u0004\u0018\u00010\u0011HÖ\u0003¢\u0006\u0004\b\u0012\u0010\u0013J\u000f\u0010\u0014\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0015\u0010\u0016J\u000f\u0010\u0017\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0018\u0010\u0016J\u0010\u0010\u0019\u001a\u00020\bHÖ\u0001¢\u0006\u0004\b\u001a\u0010\u001bJ\u001b\u0010\u001c\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0001H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001eJ\u0018\u0010\u001c\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\rH\u0096\u0002¢\u0006\u0004\b \u0010!J\u0018\u0010\u001c\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\b\"\u0010!J\u0018\u0010#\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\rH\u0096\u0002¢\u0006\u0004\b$\u0010!J\u0010\u0010%\u001a\u00020&HÖ\u0001¢\u0006\u0004\b'\u0010(R\u0012\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004X\u0080\u0004¢\u0006\u0002\n\u0000\u0088\u0001\u0002\u0092\u0001\u00060\u0003j\u0002`\u0004\u0082\u0002\u0004\n\u0002\b!¨\u0006)"}, d2 = {"Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "Lkotlin/time/ComparableTimeMark;", "reading", "", "Lkotlin/time/ValueTimeMarkReading;", "constructor-impl", "(J)J", "compareTo", "", "other", "compareTo-6eNON_k", "(JJ)I", "elapsedNow", "Lkotlin/time/Duration;", "elapsedNow-UwyO8pc", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "hasNotPassedNow", "hasNotPassedNow-impl", "(J)Z", "hasPassedNow", "hasPassedNow-impl", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "(J)I", "minus", "minus-UwyO8pc", "(JLkotlin/time/ComparableTimeMark;)J", "duration", "minus-LRDsOJo", "(JJ)J", "minus-6eNON_k", "plus", "plus-LRDsOJo", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        @kotlin.jvm.JvmInline
        public static final class ValueTimeMark implements kotlin.time.ComparableTimeMark {
            private final long reading;

            /* JADX INFO: renamed from: box-impl, reason: not valid java name */
            public static final /* synthetic */ kotlin.time.TimeSource.Monotonic.ValueTimeMark m12773boximpl(long j) {
                return new kotlin.time.TimeSource.Monotonic.ValueTimeMark(j);
            }

            /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
            public static long m12776constructorimpl(long j) {
                return j;
            }

            /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
            public static boolean m12778equalsimpl(long j, java.lang.Object obj) {
                return (obj instanceof kotlin.time.TimeSource.Monotonic.ValueTimeMark) && j == ((kotlin.time.TimeSource.Monotonic.ValueTimeMark) obj).getReading();
            }

            /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
            public static final boolean m12779equalsimpl0(long j, long j2) {
                return j == j2;
            }

            /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
            public static int m12782hashCodeimpl(long j) {
                return java.lang.Long.hashCode(j);
            }

            /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
            public static java.lang.String m12787toStringimpl(long j) {
                return "ValueTimeMark(reading=" + j + ')';
            }

            @Override // kotlin.time.ComparableTimeMark
            public boolean equals(java.lang.Object obj) {
                return m12778equalsimpl(this.reading, obj);
            }

            @Override // kotlin.time.ComparableTimeMark
            public int hashCode() {
                return m12782hashCodeimpl(this.reading);
            }

            public java.lang.String toString() {
                return m12787toStringimpl(this.reading);
            }

            /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
            public final /* synthetic */ long getReading() {
                return this.reading;
            }

            @Override // java.lang.Comparable
            public int compareTo(kotlin.time.ComparableTimeMark other) {
                return kotlin.time.ComparableTimeMark.DefaultImpls.compareTo(this, other);
            }

            @Override // kotlin.time.ComparableTimeMark, kotlin.time.TimeMark
            /* JADX INFO: renamed from: minus-LRDsOJo */
            public /* bridge */ /* synthetic */ kotlin.time.ComparableTimeMark mo12624minusLRDsOJo(long duration) {
                return m12773boximpl(m12788minusLRDsOJo(duration));
            }

            @Override // kotlin.time.TimeMark
            /* JADX INFO: renamed from: minus-LRDsOJo */
            public /* bridge */ /* synthetic */ kotlin.time.TimeMark mo12624minusLRDsOJo(long duration) {
                return m12773boximpl(m12788minusLRDsOJo(duration));
            }

            @Override // kotlin.time.ComparableTimeMark, kotlin.time.TimeMark
            /* JADX INFO: renamed from: plus-LRDsOJo */
            public /* bridge */ /* synthetic */ kotlin.time.ComparableTimeMark mo12626plusLRDsOJo(long duration) {
                return m12773boximpl(m12789plusLRDsOJo(duration));
            }

            @Override // kotlin.time.TimeMark
            /* JADX INFO: renamed from: plus-LRDsOJo */
            public /* bridge */ /* synthetic */ kotlin.time.TimeMark mo12626plusLRDsOJo(long duration) {
                return m12773boximpl(m12789plusLRDsOJo(duration));
            }

            private /* synthetic */ ValueTimeMark(long reading) {
                this.reading = reading;
            }

            /* JADX INFO: renamed from: compareTo-impl, reason: not valid java name */
            public static int m12775compareToimpl(long arg0, kotlin.time.ComparableTimeMark other) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
                return m12773boximpl(arg0).compareTo(other);
            }

            /* JADX INFO: renamed from: elapsedNow-UwyO8pc, reason: not valid java name */
            public static long m12777elapsedNowUwyO8pc(long arg0) {
                return kotlin.time.MonotonicTimeSource.INSTANCE.m12766elapsedFrom6eNON_k(arg0);
            }

            @Override // kotlin.time.TimeMark
            /* JADX INFO: renamed from: elapsedNow-UwyO8pc */
            public long mo12623elapsedNowUwyO8pc() {
                return m12777elapsedNowUwyO8pc(this.reading);
            }

            /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
            public static long m12786plusLRDsOJo(long arg0, long duration) {
                return kotlin.time.MonotonicTimeSource.INSTANCE.m12764adjustReading6QKq23U(arg0, duration);
            }

            /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
            public long m12789plusLRDsOJo(long duration) {
                return m12786plusLRDsOJo(this.reading, duration);
            }

            /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
            public static long m12784minusLRDsOJo(long arg0, long duration) {
                return kotlin.time.MonotonicTimeSource.INSTANCE.m12764adjustReading6QKq23U(arg0, kotlin.time.Duration.m12686unaryMinusUwyO8pc(duration));
            }

            /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
            public long m12788minusLRDsOJo(long duration) {
                return m12784minusLRDsOJo(this.reading, duration);
            }

            /* JADX INFO: renamed from: hasPassedNow-impl, reason: not valid java name */
            public static boolean m12781hasPassedNowimpl(long arg0) {
                return !kotlin.time.Duration.m12666isNegativeimpl(m12777elapsedNowUwyO8pc(arg0));
            }

            @Override // kotlin.time.TimeMark
            public boolean hasPassedNow() {
                return m12781hasPassedNowimpl(this.reading);
            }

            /* JADX INFO: renamed from: hasNotPassedNow-impl, reason: not valid java name */
            public static boolean m12780hasNotPassedNowimpl(long arg0) {
                return kotlin.time.Duration.m12666isNegativeimpl(m12777elapsedNowUwyO8pc(arg0));
            }

            @Override // kotlin.time.TimeMark
            public boolean hasNotPassedNow() {
                return m12780hasNotPassedNowimpl(this.reading);
            }

            @Override // kotlin.time.ComparableTimeMark
            /* JADX INFO: renamed from: minus-UwyO8pc */
            public long mo12625minusUwyO8pc(kotlin.time.ComparableTimeMark other) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
                return m12785minusUwyO8pc(this.reading, other);
            }

            /* JADX INFO: renamed from: minus-UwyO8pc, reason: not valid java name */
            public static long m12785minusUwyO8pc(long arg0, kotlin.time.ComparableTimeMark other) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
                if (!(other instanceof kotlin.time.TimeSource.Monotonic.ValueTimeMark)) {
                    throw new java.lang.IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + ((java.lang.Object) m12787toStringimpl(arg0)) + " and " + other);
                }
                return m12783minus6eNON_k(arg0, ((kotlin.time.TimeSource.Monotonic.ValueTimeMark) other).getReading());
            }

            /* JADX INFO: renamed from: minus-6eNON_k, reason: not valid java name */
            public static final long m12783minus6eNON_k(long arg0, long other) {
                return kotlin.time.MonotonicTimeSource.INSTANCE.m12765differenceBetweenfRLX17w(arg0, other);
            }

            /* JADX INFO: renamed from: compareTo-6eNON_k, reason: not valid java name */
            public static final int m12774compareTo6eNON_k(long arg0, long other) {
                return kotlin.time.Duration.m12632compareToLRDsOJo(m12783minus6eNON_k(arg0, other), kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc());
            }
        }
    }

    /* JADX INFO: compiled from: TimeSource.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lkotlin/time/TimeSource$Companion;", "", "()V", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        static final /* synthetic */ kotlin.time.TimeSource.Companion $$INSTANCE = new kotlin.time.TimeSource.Companion();

        private Companion() {
        }
    }
}
