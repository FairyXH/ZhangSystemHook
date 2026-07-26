package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: longSaturatedMath.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0000\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001H\u0002¢\u0006\u0004\b\u0006\u0010\u0007\u001a\u0015\u0010\b\u001a\u00020\u00042\u0006\u0010\u0002\u001a\u00020\u0001H\u0002¢\u0006\u0002\u0010\t\u001a'\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0004H\u0000¢\u0006\u0004\b\r\u0010\u000e\u001a'\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\u0010\u0010\u000e\u001a%\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\fH\u0000¢\u0006\u0002\u0010\u0014\u001a%\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\fH\u0002¢\u0006\u0002\u0010\u0014\u001a%\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\fH\u0000¢\u0006\u0002\u0010\u0014\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0001H\u0080\b¨\u0006\u001d"}, d2 = {"checkInfiniteSumDefined", "", "value", "duration", "Lkotlin/time/Duration;", "durationInUnit", "checkInfiniteSumDefined-PjuGub4", "(JJJ)J", "infinityOfSign", "(J)J", "saturatingAdd", "unit", "Lkotlin/time/DurationUnit;", "saturatingAdd-NuflL3o", "(JLkotlin/time/DurationUnit;J)J", "saturatingAddInHalves", "saturatingAddInHalves-NuflL3o", "saturatingDiff", "valueNs", "origin", "(JJLkotlin/time/DurationUnit;)J", "saturatingFiniteDiff", "value1", "value2", "saturatingOriginsDiff", "origin1", "origin2", "isSaturated", "", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class LongSaturatedMathKt {
    /* JADX INFO: renamed from: saturatingAdd-NuflL3o, reason: not valid java name */
    public static final long m7543saturatingAddNuflL3o(long value, com.android.server.permission.jarjar.kotlin.time.DurationUnit unit, long duration) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        long durationInUnit = com.android.server.permission.jarjar.kotlin.time.Duration.m7460toLongimpl(duration, unit);
        if (((value - 1) | 1) == Long.MAX_VALUE) {
            return m7542checkInfiniteSumDefinedPjuGub4(value, duration, durationInUnit);
        }
        if ((1 | (durationInUnit - 1)) == Long.MAX_VALUE) {
            return m7544saturatingAddInHalvesNuflL3o(value, unit, duration);
        }
        long result = value + durationInUnit;
        if (((value ^ result) & (durationInUnit ^ result)) < 0) {
            return value < 0 ? Long.MIN_VALUE : Long.MAX_VALUE;
        }
        return result;
    }

    /* JADX INFO: renamed from: checkInfiniteSumDefined-PjuGub4, reason: not valid java name */
    private static final long m7542checkInfiniteSumDefinedPjuGub4(long value, long duration, long durationInUnit) {
        if (!com.android.server.permission.jarjar.kotlin.time.Duration.m7446isInfiniteimpl(duration) || (value ^ durationInUnit) >= 0) {
            return value;
        }
        throw new java.lang.IllegalArgumentException("Summing infinities of different signs");
    }

    /* JADX INFO: renamed from: saturatingAddInHalves-NuflL3o, reason: not valid java name */
    private static final long m7544saturatingAddInHalvesNuflL3o(long value, com.android.server.permission.jarjar.kotlin.time.DurationUnit unit, long duration) {
        long half = com.android.server.permission.jarjar.kotlin.time.Duration.m7417divUwyO8pc(duration, 2);
        long halfInUnit = com.android.server.permission.jarjar.kotlin.time.Duration.m7460toLongimpl(half, unit);
        if ((1 | (halfInUnit - 1)) == Long.MAX_VALUE) {
            return halfInUnit;
        }
        return m7543saturatingAddNuflL3o(m7543saturatingAddNuflL3o(value, unit, half), unit, com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(duration, half));
    }

    private static final long infinityOfSign(long value) {
        return value < 0 ? com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7516getNEG_INFINITEUwyO8pc$kotlin_stdlib() : com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7515getINFINITEUwyO8pc();
    }

    public static final long saturatingDiff(long valueNs, long origin, com.android.server.permission.jarjar.kotlin.time.DurationUnit unit) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        if ((1 | (origin - 1)) == Long.MAX_VALUE) {
            return com.android.server.permission.jarjar.kotlin.time.Duration.m7467unaryMinusUwyO8pc(infinityOfSign(origin));
        }
        return saturatingFiniteDiff(valueNs, origin, unit);
    }

    public static final long saturatingOriginsDiff(long origin1, long origin2, com.android.server.permission.jarjar.kotlin.time.DurationUnit unit) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        int $i$f$isSaturated = ((origin2 - 1) | 1) == Long.MAX_VALUE ? 1 : 0;
        if ($i$f$isSaturated != 0) {
            return origin1 == origin2 ? com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc() : com.android.server.permission.jarjar.kotlin.time.Duration.m7467unaryMinusUwyO8pc(infinityOfSign(origin2));
        }
        if ((1 | (origin1 - 1)) == Long.MAX_VALUE) {
            return infinityOfSign(origin1);
        }
        return saturatingFiniteDiff(origin1, origin2, unit);
    }

    private static final long saturatingFiniteDiff(long value1, long value2, com.android.server.permission.jarjar.kotlin.time.DurationUnit unit) {
        long result = value1 - value2;
        if (((result ^ value1) & (~(result ^ value2))) < 0) {
            if (unit.compareTo(com.android.server.permission.jarjar.kotlin.time.DurationUnit.MILLISECONDS) < 0) {
                long unitsInMilli = com.android.server.permission.jarjar.kotlin.time.DurationUnitKt.convertDurationUnit(1L, com.android.server.permission.jarjar.kotlin.time.DurationUnit.MILLISECONDS, unit);
                long resultMs = (value1 / unitsInMilli) - (value2 / unitsInMilli);
                long resultUnit = (value1 % unitsInMilli) - (value2 % unitsInMilli);
                com.android.server.permission.jarjar.kotlin.time.Duration.Companion companion = com.android.server.permission.jarjar.kotlin.time.Duration.Companion;
                return com.android.server.permission.jarjar.kotlin.time.Duration.m7450plusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.DurationKt.toDuration(resultMs, com.android.server.permission.jarjar.kotlin.time.DurationUnit.MILLISECONDS), com.android.server.permission.jarjar.kotlin.time.DurationKt.toDuration(resultUnit, unit));
            }
            return com.android.server.permission.jarjar.kotlin.time.Duration.m7467unaryMinusUwyO8pc(infinityOfSign(result));
        }
        return com.android.server.permission.jarjar.kotlin.time.DurationKt.toDuration(result, unit);
    }

    public static final boolean isSaturated(long $this$isSaturated) {
        return (1 | ($this$isSaturated - 1)) == Long.MAX_VALUE;
    }
}
