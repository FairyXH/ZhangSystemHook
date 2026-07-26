package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: TimeSource.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u0000 \u00042\u00020\u0001:\u0003\u0004\u0005\u0006J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0007"}, d2 = {"Lkotlin/time/TimeSource;", "", "markNow", "Lkotlin/time/TimeMark;", "Companion", "Monotonic", "WithComparableMarks", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface TimeSource {
    public static final com.android.server.permission.jarjar.kotlin.time.TimeSource.Companion Companion = com.android.server.permission.jarjar.kotlin.time.TimeSource.Companion.$$INSTANCE;

    /* JADX INFO: compiled from: TimeSource.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0004"}, d2 = {"Lkotlin/time/TimeSource$WithComparableMarks;", "Lkotlin/time/TimeSource;", "markNow", "Lkotlin/time/ComparableTimeMark;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface WithComparableMarks extends com.android.server.permission.jarjar.kotlin.time.TimeSource {
        @Override // com.android.server.permission.jarjar.kotlin.time.TimeSource
        com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark markNow();
    }

    com.android.server.permission.jarjar.kotlin.time.TimeMark markNow();

    /* JADX INFO: compiled from: TimeSource.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\tB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u0004H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016\u0082\u0002\u0004\n\u0002\b!¨\u0006\n"}, d2 = {"Lkotlin/time/TimeSource$Monotonic;", "Lkotlin/time/TimeSource$WithComparableMarks;", "()V", "markNow", "Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "markNow-z9LOYto", "()J", "toString", "", "ValueTimeMark", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Monotonic implements com.android.server.permission.jarjar.kotlin.time.TimeSource.WithComparableMarks {
        public static final com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic INSTANCE = new com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic();

        private Monotonic() {
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.TimeSource.WithComparableMarks, com.android.server.permission.jarjar.kotlin.time.TimeSource
        public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark markNow() {
            return com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7554boximpl(m7553markNowz9LOYto());
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.TimeSource
        public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.time.TimeMark markNow() {
            return com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7554boximpl(m7553markNowz9LOYto());
        }

        /* JADX INFO: renamed from: markNow-z9LOYto, reason: not valid java name */
        public long m7553markNowz9LOYto() {
            return com.android.server.permission.jarjar.kotlin.time.MonotonicTimeSource.INSTANCE.m7548markNowz9LOYto();
        }

        public java.lang.String toString() {
            return com.android.server.permission.jarjar.kotlin.time.MonotonicTimeSource.INSTANCE.toString();
        }

        /* JADX INFO: compiled from: TimeSource.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0014\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0087@\u0018\u00002\u00020\u0001B\u0015\b\u0000\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004¢\u0006\u0004\b\u0005\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0012\u0010\f\u001a\u00020\rH\u0016ø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u0006J\u001a\u0010\u000f\u001a\u00020\u00102\b\u0010\t\u001a\u0004\u0018\u00010\u0011HÖ\u0003¢\u0006\u0004\b\u0012\u0010\u0013J\u000f\u0010\u0014\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0015\u0010\u0016J\u000f\u0010\u0017\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0018\u0010\u0016J\u0010\u0010\u0019\u001a\u00020\bHÖ\u0001¢\u0006\u0004\b\u001a\u0010\u001bJ\u001b\u0010\u001c\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0001H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001eJ\u0018\u0010\u001c\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\rH\u0096\u0002¢\u0006\u0004\b \u0010!J\u0018\u0010\u001c\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\b\"\u0010!J\u0018\u0010#\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\rH\u0096\u0002¢\u0006\u0004\b$\u0010!J\u0010\u0010%\u001a\u00020&HÖ\u0001¢\u0006\u0004\b'\u0010(R\u0012\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004X\u0080\u0004¢\u0006\u0002\n\u0000\u0088\u0001\u0002\u0092\u0001\u00060\u0003j\u0002`\u0004\u0082\u0002\u0004\n\u0002\b!¨\u0006)"}, d2 = {"Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "Lkotlin/time/ComparableTimeMark;", "reading", "", "Lkotlin/time/ValueTimeMarkReading;", "constructor-impl", "(J)J", "compareTo", "", "other", "compareTo-6eNON_k", "(JJ)I", "elapsedNow", "Lkotlin/time/Duration;", "elapsedNow-UwyO8pc", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "hasNotPassedNow", "hasNotPassedNow-impl", "(J)Z", "hasPassedNow", "hasPassedNow-impl", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "(J)I", "minus", "minus-UwyO8pc", "(JLkotlin/time/ComparableTimeMark;)J", "duration", "minus-LRDsOJo", "(JJ)J", "minus-6eNON_k", "plus", "plus-LRDsOJo", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        @com.android.server.permission.jarjar.kotlin.jvm.JvmInline
        public static final class ValueTimeMark implements com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark {
            private final long reading;

            /* JADX INFO: renamed from: box-impl, reason: not valid java name */
            public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark m7554boximpl(long j) {
                return new com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark(j);
            }

            /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
            public static long m7557constructorimpl(long j) {
                return j;
            }

            /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
            public static boolean m7559equalsimpl(long j, java.lang.Object obj) {
                return (obj instanceof com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark) && j == ((com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark) obj).m7571unboximpl();
            }

            /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
            public static final boolean m7560equalsimpl0(long j, long j2) {
                return j == j2;
            }

            /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
            public static int m7563hashCodeimpl(long j) {
                return java.lang.Long.hashCode(j);
            }

            /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
            public static java.lang.String m7568toStringimpl(long j) {
                return "ValueTimeMark(reading=" + j + ')';
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark
            public boolean equals(java.lang.Object obj) {
                return m7559equalsimpl(this.reading, obj);
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark
            public int hashCode() {
                return m7563hashCodeimpl(this.reading);
            }

            public java.lang.String toString() {
                return m7568toStringimpl(this.reading);
            }

            /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
            public final /* synthetic */ long m7571unboximpl() {
                return this.reading;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.Comparable
            public int compareTo(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark other) {
                return com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark.DefaultImpls.compareTo(this, other);
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark, com.android.server.permission.jarjar.kotlin.time.TimeMark
            /* JADX INFO: renamed from: minus-LRDsOJo */
            public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark mo7405minusLRDsOJo(long duration) {
                return m7554boximpl(m7569minusLRDsOJo(duration));
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
            /* JADX INFO: renamed from: minus-LRDsOJo */
            public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.time.TimeMark mo7405minusLRDsOJo(long duration) {
                return m7554boximpl(m7569minusLRDsOJo(duration));
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark, com.android.server.permission.jarjar.kotlin.time.TimeMark
            /* JADX INFO: renamed from: plus-LRDsOJo */
            public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark mo7407plusLRDsOJo(long duration) {
                return m7554boximpl(m7570plusLRDsOJo(duration));
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
            /* JADX INFO: renamed from: plus-LRDsOJo */
            public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.time.TimeMark mo7407plusLRDsOJo(long duration) {
                return m7554boximpl(m7570plusLRDsOJo(duration));
            }

            private /* synthetic */ ValueTimeMark(long reading) {
                this.reading = reading;
            }

            /* JADX INFO: renamed from: compareTo-impl, reason: not valid java name */
            public static int m7556compareToimpl(long arg0, com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark other) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
                return m7554boximpl(arg0).compareTo(other);
            }

            /* JADX INFO: renamed from: elapsedNow-UwyO8pc, reason: not valid java name */
            public static long m7558elapsedNowUwyO8pc(long arg0) {
                return com.android.server.permission.jarjar.kotlin.time.MonotonicTimeSource.INSTANCE.m7547elapsedFrom6eNON_k(arg0);
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
            /* JADX INFO: renamed from: elapsedNow-UwyO8pc */
            public long mo7404elapsedNowUwyO8pc() {
                return m7558elapsedNowUwyO8pc(this.reading);
            }

            /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
            public static long m7567plusLRDsOJo(long arg0, long duration) {
                return com.android.server.permission.jarjar.kotlin.time.MonotonicTimeSource.INSTANCE.m7545adjustReading6QKq23U(arg0, duration);
            }

            /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
            public long m7570plusLRDsOJo(long duration) {
                return m7567plusLRDsOJo(this.reading, duration);
            }

            /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
            public static long m7565minusLRDsOJo(long arg0, long duration) {
                return com.android.server.permission.jarjar.kotlin.time.MonotonicTimeSource.INSTANCE.m7545adjustReading6QKq23U(arg0, com.android.server.permission.jarjar.kotlin.time.Duration.m7467unaryMinusUwyO8pc(duration));
            }

            /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
            public long m7569minusLRDsOJo(long duration) {
                return m7565minusLRDsOJo(this.reading, duration);
            }

            /* JADX INFO: renamed from: hasPassedNow-impl, reason: not valid java name */
            public static boolean m7562hasPassedNowimpl(long arg0) {
                return !com.android.server.permission.jarjar.kotlin.time.Duration.m7447isNegativeimpl(m7558elapsedNowUwyO8pc(arg0));
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
            public boolean hasPassedNow() {
                return m7562hasPassedNowimpl(this.reading);
            }

            /* JADX INFO: renamed from: hasNotPassedNow-impl, reason: not valid java name */
            public static boolean m7561hasNotPassedNowimpl(long arg0) {
                return com.android.server.permission.jarjar.kotlin.time.Duration.m7447isNegativeimpl(m7558elapsedNowUwyO8pc(arg0));
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
            public boolean hasNotPassedNow() {
                return m7561hasNotPassedNowimpl(this.reading);
            }

            @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark
            /* JADX INFO: renamed from: minus-UwyO8pc */
            public long mo7406minusUwyO8pc(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark other) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
                return m7566minusUwyO8pc(this.reading, other);
            }

            /* JADX INFO: renamed from: minus-UwyO8pc, reason: not valid java name */
            public static long m7566minusUwyO8pc(long arg0, com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark other) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
                if (!(other instanceof com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark)) {
                    throw new java.lang.IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + ((java.lang.Object) m7568toStringimpl(arg0)) + " and " + other);
                }
                return m7564minus6eNON_k(arg0, ((com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark) other).m7571unboximpl());
            }

            /* JADX INFO: renamed from: minus-6eNON_k, reason: not valid java name */
            public static final long m7564minus6eNON_k(long arg0, long other) {
                return com.android.server.permission.jarjar.kotlin.time.MonotonicTimeSource.INSTANCE.m7546differenceBetweenfRLX17w(arg0, other);
            }

            /* JADX INFO: renamed from: compareTo-6eNON_k, reason: not valid java name */
            public static final int m7555compareTo6eNON_k(long arg0, long other) {
                return com.android.server.permission.jarjar.kotlin.time.Duration.m7413compareToLRDsOJo(m7564minus6eNON_k(arg0, other), com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc());
            }
        }
    }

    /* JADX INFO: compiled from: TimeSource.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lkotlin/time/TimeSource$Companion;", "", "()V", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        static final /* synthetic */ com.android.server.permission.jarjar.kotlin.time.TimeSource.Companion $$INSTANCE = new com.android.server.permission.jarjar.kotlin.time.TimeSource.Companion();

        private Companion() {
        }
    }
}
