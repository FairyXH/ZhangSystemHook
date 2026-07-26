package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: DurationUnit.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001a\u0010\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\bH\u0001\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\u0001H\u0001¨\u0006\t"}, d2 = {"durationUnitByIsoChar", "Lkotlin/time/DurationUnit;", "isoChar", "", "isTimeComponent", "", "durationUnitByShortName", "shortName", "", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/time/DurationUnitKt")
class DurationUnitKt__DurationUnitKt extends com.android.server.permission.jarjar.kotlin.time.DurationUnitKt__DurationUnitJvmKt {

    /* JADX INFO: compiled from: DurationUnit.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[com.android.server.permission.jarjar.kotlin.time.DurationUnit.values().length];
            try {
                iArr[com.android.server.permission.jarjar.kotlin.time.DurationUnit.NANOSECONDS.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.time.DurationUnit.MICROSECONDS.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.time.DurationUnit.MILLISECONDS.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.time.DurationUnit.SECONDS.ordinal()] = 4;
            } catch (java.lang.NoSuchFieldError e4) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.time.DurationUnit.MINUTES.ordinal()] = 5;
            } catch (java.lang.NoSuchFieldError e5) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.time.DurationUnit.HOURS.ordinal()] = 6;
            } catch (java.lang.NoSuchFieldError e6) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.time.DurationUnit.DAYS.ordinal()] = 7;
            } catch (java.lang.NoSuchFieldError e7) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static final java.lang.String shortName(com.android.server.permission.jarjar.kotlin.time.DurationUnit $this$shortName) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$shortName, "<this>");
        switch (com.android.server.permission.jarjar.kotlin.time.DurationUnitKt__DurationUnitKt.WhenMappings.$EnumSwitchMapping$0[$this$shortName.ordinal()]) {
            case 1:
                return "ns";
            case 2:
                return "us";
            case 3:
                return "ms";
            case 4:
                return "s";
            case 5:
                return "m";
            case 6:
                return "h";
            case 7:
                return "d";
            default:
                throw new java.lang.IllegalStateException(("Unknown unit: " + $this$shortName).toString());
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static final com.android.server.permission.jarjar.kotlin.time.DurationUnit durationUnitByShortName(java.lang.String shortName) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(shortName, "shortName");
        switch (shortName.hashCode()) {
            case 100:
                if (shortName.equals("d")) {
                    return com.android.server.permission.jarjar.kotlin.time.DurationUnit.DAYS;
                }
                break;
            case 104:
                if (shortName.equals("h")) {
                    return com.android.server.permission.jarjar.kotlin.time.DurationUnit.HOURS;
                }
                break;
            case 109:
                if (shortName.equals("m")) {
                    return com.android.server.permission.jarjar.kotlin.time.DurationUnit.MINUTES;
                }
                break;
            case 115:
                if (shortName.equals("s")) {
                    return com.android.server.permission.jarjar.kotlin.time.DurationUnit.SECONDS;
                }
                break;
            case 3494:
                if (shortName.equals("ms")) {
                    return com.android.server.permission.jarjar.kotlin.time.DurationUnit.MILLISECONDS;
                }
                break;
            case 3525:
                if (shortName.equals("ns")) {
                    return com.android.server.permission.jarjar.kotlin.time.DurationUnit.NANOSECONDS;
                }
                break;
            case 3742:
                if (shortName.equals("us")) {
                    return com.android.server.permission.jarjar.kotlin.time.DurationUnit.MICROSECONDS;
                }
                break;
        }
        throw new java.lang.IllegalArgumentException("Unknown duration unit short name: " + shortName);
    }

    public static final com.android.server.permission.jarjar.kotlin.time.DurationUnit durationUnitByIsoChar(char isoChar, boolean isTimeComponent) {
        if (!isTimeComponent) {
            if (isoChar == 'D') {
                return com.android.server.permission.jarjar.kotlin.time.DurationUnit.DAYS;
            }
            throw new java.lang.IllegalArgumentException("Invalid or unsupported duration ISO non-time unit: " + isoChar);
        }
        if (isoChar == 'H') {
            return com.android.server.permission.jarjar.kotlin.time.DurationUnit.HOURS;
        }
        if (isoChar == 'M') {
            return com.android.server.permission.jarjar.kotlin.time.DurationUnit.MINUTES;
        }
        if (isoChar == 'S') {
            return com.android.server.permission.jarjar.kotlin.time.DurationUnit.SECONDS;
        }
        throw new java.lang.IllegalArgumentException("Invalid duration ISO time unit: " + isoChar);
    }
}
