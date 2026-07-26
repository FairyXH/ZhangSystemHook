package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: UArraySorting.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0006\u0010\u0007\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\t\u0010\n\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\f\u0010\r\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u000f\u0010\u0010\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0013\u0010\u0014\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0015\u0010\u0016\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0017\u0010\u0018\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0019\u0010\u001a\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u001e\u0010\u0014\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u001f\u0010\u0016\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b \u0010\u0018\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b!\u0010\u001a¨\u0006\""}, d2 = {"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "fromIndex", "toIndex", "sortArray-4UcCI2c", "sortArray-oBK06Vg", "sortArray--nroSd4", "sortArray-Aa5vz7o", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class UArraySortingKt {
    /* JADX INFO: renamed from: partition-4UcCI2c, reason: not valid java name */
    private static final int m6545partition4UcCI2c(byte[] array, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ(array, (left + right) / 2);
        while (i <= j) {
            while (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ(array, i) & 255, pivot & 255) < 0) {
                i++;
            }
            while (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ(array, j) & 255, pivot & 255) > 0) {
                j--;
            }
            if (i <= j) {
                byte tmp = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ(array, i);
                com.android.server.permission.jarjar.kotlin.UByteArray.m6170setVurrAj0(array, i, com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ(array, j));
                com.android.server.permission.jarjar.kotlin.UByteArray.m6170setVurrAj0(array, j, tmp);
                i++;
                j--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-4UcCI2c, reason: not valid java name */
    private static final void m6549quickSort4UcCI2c(byte[] array, int left, int right) {
        int index = m6545partition4UcCI2c(array, left, right);
        if (left < index - 1) {
            m6549quickSort4UcCI2c(array, left, index - 1);
        }
        if (index < right) {
            m6549quickSort4UcCI2c(array, index, right);
        }
    }

    /* JADX INFO: renamed from: partition-Aa5vz7o, reason: not valid java name */
    private static final int m6546partitionAa5vz7o(short[] array, int left, int right) {
        int i = left;
        int j = right;
        short pivot = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg(array, (left + right) / 2);
        while (i <= j) {
            while (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg(array, i) & 65535, pivot & 65535) < 0) {
                i++;
            }
            while (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg(array, j) & 65535, pivot & 65535) > 0) {
                j--;
            }
            if (i <= j) {
                short tmp = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg(array, i);
                com.android.server.permission.jarjar.kotlin.UShortArray.m6433set01HTLdE(array, i, com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg(array, j));
                com.android.server.permission.jarjar.kotlin.UShortArray.m6433set01HTLdE(array, j, tmp);
                i++;
                j--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-Aa5vz7o, reason: not valid java name */
    private static final void m6550quickSortAa5vz7o(short[] array, int left, int right) {
        int index = m6546partitionAa5vz7o(array, left, right);
        if (left < index - 1) {
            m6550quickSortAa5vz7o(array, left, index - 1);
        }
        if (index < right) {
            m6550quickSortAa5vz7o(array, index, right);
        }
    }

    /* JADX INFO: renamed from: partition-oBK06Vg, reason: not valid java name */
    private static final int m6547partitionoBK06Vg(int[] array, int left, int right) {
        int i = left;
        int j = right;
        int pivot = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA(array, (left + right) / 2);
        while (i <= j) {
            while (java.lang.Integer.compareUnsigned(com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA(array, i), pivot) < 0) {
                i++;
            }
            while (java.lang.Integer.compareUnsigned(com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA(array, j), pivot) > 0) {
                j--;
            }
            if (i <= j) {
                int tmp = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA(array, i);
                com.android.server.permission.jarjar.kotlin.UIntArray.m6249setVXSXFK8(array, i, com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA(array, j));
                com.android.server.permission.jarjar.kotlin.UIntArray.m6249setVXSXFK8(array, j, tmp);
                i++;
                j--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-oBK06Vg, reason: not valid java name */
    private static final void m6551quickSortoBK06Vg(int[] array, int left, int right) {
        int index = m6547partitionoBK06Vg(array, left, right);
        if (left < index - 1) {
            m6551quickSortoBK06Vg(array, left, index - 1);
        }
        if (index < right) {
            m6551quickSortoBK06Vg(array, index, right);
        }
    }

    /* JADX INFO: renamed from: partition--nroSd4, reason: not valid java name */
    private static final int m6544partitionnroSd4(long[] array, int left, int right) {
        int i = left;
        int j = right;
        long pivot = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU(array, (left + right) / 2);
        while (i <= j) {
            while (java.lang.Long.compareUnsigned(com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU(array, i), pivot) < 0) {
                i++;
            }
            while (java.lang.Long.compareUnsigned(com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU(array, j), pivot) > 0) {
                j--;
            }
            if (i <= j) {
                long tmp = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU(array, i);
                com.android.server.permission.jarjar.kotlin.ULongArray.m6328setk8EXiF4(array, i, com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU(array, j));
                com.android.server.permission.jarjar.kotlin.ULongArray.m6328setk8EXiF4(array, j, tmp);
                i++;
                j--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort--nroSd4, reason: not valid java name */
    private static final void m6548quickSortnroSd4(long[] array, int left, int right) {
        int index = m6544partitionnroSd4(array, left, right);
        if (left < index - 1) {
            m6548quickSortnroSd4(array, left, index - 1);
        }
        if (index < right) {
            m6548quickSortnroSd4(array, index, right);
        }
    }

    /* JADX INFO: renamed from: sortArray-4UcCI2c, reason: not valid java name */
    public static final void m6553sortArray4UcCI2c(byte[] array, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        m6549quickSort4UcCI2c(array, fromIndex, toIndex - 1);
    }

    /* JADX INFO: renamed from: sortArray-Aa5vz7o, reason: not valid java name */
    public static final void m6554sortArrayAa5vz7o(short[] array, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        m6550quickSortAa5vz7o(array, fromIndex, toIndex - 1);
    }

    /* JADX INFO: renamed from: sortArray-oBK06Vg, reason: not valid java name */
    public static final void m6555sortArrayoBK06Vg(int[] array, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        m6551quickSortoBK06Vg(array, fromIndex, toIndex - 1);
    }

    /* JADX INFO: renamed from: sortArray--nroSd4, reason: not valid java name */
    public static final void m6552sortArraynroSd4(long[] array, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        m6548quickSortnroSd4(array, fromIndex, toIndex - 1);
    }
}
