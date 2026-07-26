package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: DurationUnitJvm.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0001\u001a \u0010\u0000\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0001\u001a \u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0001\u001a\f\u0010\b\u001a\u00020\u0004*\u00020\tH\u0007\u001a\f\u0010\n\u001a\u00020\t*\u00020\u0004H\u0007¨\u0006\u000b"}, d2 = {"convertDurationUnit", "", "value", "sourceUnit", "Lkotlin/time/DurationUnit;", "targetUnit", "", "convertDurationUnitOverflow", "toDurationUnit", "Ljava/util/concurrent/TimeUnit;", "toTimeUnit", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/time/DurationUnitKt")
class DurationUnitKt__DurationUnitJvmKt {

    /* JADX INFO: compiled from: DurationUnitJvm.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[java.util.concurrent.TimeUnit.values().length];
            try {
                iArr[java.util.concurrent.TimeUnit.NANOSECONDS.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                iArr[java.util.concurrent.TimeUnit.MICROSECONDS.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                iArr[java.util.concurrent.TimeUnit.MILLISECONDS.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            try {
                iArr[java.util.concurrent.TimeUnit.SECONDS.ordinal()] = 4;
            } catch (java.lang.NoSuchFieldError e4) {
            }
            try {
                iArr[java.util.concurrent.TimeUnit.MINUTES.ordinal()] = 5;
            } catch (java.lang.NoSuchFieldError e5) {
            }
            try {
                iArr[java.util.concurrent.TimeUnit.HOURS.ordinal()] = 6;
            } catch (java.lang.NoSuchFieldError e6) {
            }
            try {
                iArr[java.util.concurrent.TimeUnit.DAYS.ordinal()] = 7;
            } catch (java.lang.NoSuchFieldError e7) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static final java.util.concurrent.TimeUnit toTimeUnit(com.android.server.permission.jarjar.kotlin.time.DurationUnit $this$toTimeUnit) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toTimeUnit, "<this>");
        return $this$toTimeUnit.getTimeUnit$kotlin_stdlib();
    }

    public static final com.android.server.permission.jarjar.kotlin.time.DurationUnit toDurationUnit(java.util.concurrent.TimeUnit $this$toDurationUnit) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toDurationUnit, "<this>");
        switch (com.android.server.permission.jarjar.kotlin.time.DurationUnitKt__DurationUnitJvmKt.WhenMappings.$EnumSwitchMapping$0[$this$toDurationUnit.ordinal()]) {
            case 1:
                return com.android.server.permission.jarjar.kotlin.time.DurationUnit.NANOSECONDS;
            case 2:
                return com.android.server.permission.jarjar.kotlin.time.DurationUnit.MICROSECONDS;
            case 3:
                return com.android.server.permission.jarjar.kotlin.time.DurationUnit.MILLISECONDS;
            case 4:
                return com.android.server.permission.jarjar.kotlin.time.DurationUnit.SECONDS;
            case 5:
                return com.android.server.permission.jarjar.kotlin.time.DurationUnit.MINUTES;
            case 6:
                return com.android.server.permission.jarjar.kotlin.time.DurationUnit.HOURS;
            case 7:
                return com.android.server.permission.jarjar.kotlin.time.DurationUnit.DAYS;
            default:
                throw new com.android.server.permission.jarjar.kotlin.NoWhenBranchMatchedException();
        }
    }

    public static final double convertDurationUnit(double value, com.android.server.permission.jarjar.kotlin.time.DurationUnit sourceUnit, com.android.server.permission.jarjar.kotlin.time.DurationUnit targetUnit) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sourceUnit, "sourceUnit");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(targetUnit, "targetUnit");
        long sourceInTargets = targetUnit.getTimeUnit$kotlin_stdlib().convert(1L, sourceUnit.getTimeUnit$kotlin_stdlib());
        if (sourceInTargets > 0) {
            return sourceInTargets * value;
        }
        long otherInThis = sourceUnit.getTimeUnit$kotlin_stdlib().convert(1L, targetUnit.getTimeUnit$kotlin_stdlib());
        return value / otherInThis;
    }

    public static final long convertDurationUnitOverflow(long value, com.android.server.permission.jarjar.kotlin.time.DurationUnit sourceUnit, com.android.server.permission.jarjar.kotlin.time.DurationUnit targetUnit) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sourceUnit, "sourceUnit");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(targetUnit, "targetUnit");
        return targetUnit.getTimeUnit$kotlin_stdlib().convert(value, sourceUnit.getTimeUnit$kotlin_stdlib());
    }

    public static final long convertDurationUnit(long value, com.android.server.permission.jarjar.kotlin.time.DurationUnit sourceUnit, com.android.server.permission.jarjar.kotlin.time.DurationUnit targetUnit) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sourceUnit, "sourceUnit");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(targetUnit, "targetUnit");
        return targetUnit.getTimeUnit$kotlin_stdlib().convert(value, sourceUnit.getTimeUnit$kotlin_stdlib());
    }
}
