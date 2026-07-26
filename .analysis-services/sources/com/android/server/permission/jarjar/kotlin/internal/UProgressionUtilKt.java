package com.android.server.permission.jarjar.kotlin.internal;

/* JADX INFO: compiled from: UProgressionUtil.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002¢\u0006\u0004\b\u0005\u0010\u0006\u001a'\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002¢\u0006\u0004\b\b\u0010\t\u001a'\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001¢\u0006\u0004\b\u000f\u0010\u0006\u001a'\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001¢\u0006\u0004\b\u0011\u0010\t¨\u0006\u0012"}, d2 = {"differenceModulo", "Lkotlin/UInt;", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class UProgressionUtilKt {
    /* JADX INFO: renamed from: differenceModulo-WZ9TVnA, reason: not valid java name */
    private static final int m7295differenceModuloWZ9TVnA(int a, int b, int c) {
        int ac = java.lang.Integer.remainderUnsigned(a, c);
        int bc = java.lang.Integer.remainderUnsigned(b, c);
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(java.lang.Integer.compareUnsigned(ac, bc) >= 0 ? ac - bc : com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(ac - bc) + c);
    }

    /* JADX INFO: renamed from: differenceModulo-sambcqE, reason: not valid java name */
    private static final long m7296differenceModulosambcqE(long a, long b, long c) {
        long ac = java.lang.Long.remainderUnsigned(a, c);
        long bc = java.lang.Long.remainderUnsigned(b, c);
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(java.lang.Long.compareUnsigned(ac, bc) >= 0 ? ac - bc : com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(ac - bc) + c);
    }

    /* JADX INFO: renamed from: getProgressionLastElement-Nkh28Cs, reason: not valid java name */
    public static final int m7298getProgressionLastElementNkh28Cs(int start, int end, int step) {
        if (step > 0) {
            if (java.lang.Integer.compareUnsigned(start, end) < 0) {
                return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(end - m7295differenceModuloWZ9TVnA(end, start, com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(step)));
            }
        } else {
            if (step >= 0) {
                throw new java.lang.IllegalArgumentException("Step is zero.");
            }
            if (java.lang.Integer.compareUnsigned(start, end) > 0) {
                return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(m7295differenceModuloWZ9TVnA(start, end, com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(-step)) + end);
            }
        }
        return end;
    }

    /* JADX INFO: renamed from: getProgressionLastElement-7ftBX0g, reason: not valid java name */
    public static final long m7297getProgressionLastElement7ftBX0g(long start, long end, long step) {
        if (step > 0) {
            if (java.lang.Long.compareUnsigned(start, end) < 0) {
                return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(end - m7296differenceModulosambcqE(end, start, com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(step)));
            }
        } else {
            if (step >= 0) {
                throw new java.lang.IllegalArgumentException("Step is zero.");
            }
            if (java.lang.Long.compareUnsigned(start, end) > 0) {
                return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(m7296differenceModulosambcqE(start, end, com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(-step)) + end);
            }
        }
        return end;
    }
}
