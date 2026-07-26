package kotlin.collections;

/* JADX INFO: compiled from: UArraySorting.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0006\u0010\u0007\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\t\u0010\n\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\f\u0010\r\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u000f\u0010\u0010\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0013\u0010\u0014\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0015\u0010\u0016\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0017\u0010\u0018\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0019\u0010\u001a\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u001e\u0010\u0014\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u001f\u0010\u0016\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b \u0010\u0018\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b!\u0010\u001a¨\u0006\""}, d2 = {"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "fromIndex", "toIndex", "sortArray-4UcCI2c", "sortArray-oBK06Vg", "sortArray--nroSd4", "sortArray-Aa5vz7o", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class UArraySortingKt {
    /* JADX INFO: renamed from: partition-4UcCI2c, reason: not valid java name */
    private static final int m11763partition4UcCI2c(byte[] array, int left, int right) {
        int i = left;
        int j = right;
        byte pivot = kotlin.UByteArray.m11383getw2LRezQ(array, (left + right) / 2);
        while (i <= j) {
            while (kotlin.jvm.internal.Intrinsics.compare(kotlin.UByteArray.m11383getw2LRezQ(array, i) & 255, pivot & 255) < 0) {
                i++;
            }
            while (kotlin.jvm.internal.Intrinsics.compare(kotlin.UByteArray.m11383getw2LRezQ(array, j) & 255, pivot & 255) > 0) {
                j--;
            }
            if (i <= j) {
                byte tmp = kotlin.UByteArray.m11383getw2LRezQ(array, i);
                kotlin.UByteArray.m11388setVurrAj0(array, i, kotlin.UByteArray.m11383getw2LRezQ(array, j));
                kotlin.UByteArray.m11388setVurrAj0(array, j, tmp);
                i++;
                j--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-4UcCI2c, reason: not valid java name */
    private static final void m11767quickSort4UcCI2c(byte[] array, int left, int right) {
        int index = m11763partition4UcCI2c(array, left, right);
        if (left < index - 1) {
            m11767quickSort4UcCI2c(array, left, index - 1);
        }
        if (index < right) {
            m11767quickSort4UcCI2c(array, index, right);
        }
    }

    /* JADX INFO: renamed from: partition-Aa5vz7o, reason: not valid java name */
    private static final int m11764partitionAa5vz7o(short[] array, int left, int right) {
        int i = left;
        int j = right;
        short pivot = kotlin.UShortArray.m11646getMh2AYeg(array, (left + right) / 2);
        while (i <= j) {
            while (kotlin.jvm.internal.Intrinsics.compare(kotlin.UShortArray.m11646getMh2AYeg(array, i) & 65535, pivot & 65535) < 0) {
                i++;
            }
            while (kotlin.jvm.internal.Intrinsics.compare(kotlin.UShortArray.m11646getMh2AYeg(array, j) & 65535, pivot & 65535) > 0) {
                j--;
            }
            if (i <= j) {
                short tmp = kotlin.UShortArray.m11646getMh2AYeg(array, i);
                kotlin.UShortArray.m11651set01HTLdE(array, i, kotlin.UShortArray.m11646getMh2AYeg(array, j));
                kotlin.UShortArray.m11651set01HTLdE(array, j, tmp);
                i++;
                j--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-Aa5vz7o, reason: not valid java name */
    private static final void m11768quickSortAa5vz7o(short[] array, int left, int right) {
        int index = m11764partitionAa5vz7o(array, left, right);
        if (left < index - 1) {
            m11768quickSortAa5vz7o(array, left, index - 1);
        }
        if (index < right) {
            m11768quickSortAa5vz7o(array, index, right);
        }
    }

    /* JADX INFO: renamed from: partition-oBK06Vg, reason: not valid java name */
    private static final int m11765partitionoBK06Vg(int[] array, int left, int right) {
        int i = left;
        int j = right;
        int pivot = kotlin.UIntArray.m11462getpVg5ArA(array, (left + right) / 2);
        while (i <= j) {
            while (java.lang.Integer.compareUnsigned(kotlin.UIntArray.m11462getpVg5ArA(array, i), pivot) < 0) {
                i++;
            }
            while (java.lang.Integer.compareUnsigned(kotlin.UIntArray.m11462getpVg5ArA(array, j), pivot) > 0) {
                j--;
            }
            if (i <= j) {
                int tmp = kotlin.UIntArray.m11462getpVg5ArA(array, i);
                kotlin.UIntArray.m11467setVXSXFK8(array, i, kotlin.UIntArray.m11462getpVg5ArA(array, j));
                kotlin.UIntArray.m11467setVXSXFK8(array, j, tmp);
                i++;
                j--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-oBK06Vg, reason: not valid java name */
    private static final void m11769quickSortoBK06Vg(int[] array, int left, int right) {
        int index = m11765partitionoBK06Vg(array, left, right);
        if (left < index - 1) {
            m11769quickSortoBK06Vg(array, left, index - 1);
        }
        if (index < right) {
            m11769quickSortoBK06Vg(array, index, right);
        }
    }

    /* JADX INFO: renamed from: partition--nroSd4, reason: not valid java name */
    private static final int m11762partitionnroSd4(long[] array, int left, int right) {
        int i = left;
        int j = right;
        long pivot = kotlin.ULongArray.m11541getsVKNKU(array, (left + right) / 2);
        while (i <= j) {
            while (java.lang.Long.compareUnsigned(kotlin.ULongArray.m11541getsVKNKU(array, i), pivot) < 0) {
                i++;
            }
            while (java.lang.Long.compareUnsigned(kotlin.ULongArray.m11541getsVKNKU(array, j), pivot) > 0) {
                j--;
            }
            if (i <= j) {
                long tmp = kotlin.ULongArray.m11541getsVKNKU(array, i);
                kotlin.ULongArray.m11546setk8EXiF4(array, i, kotlin.ULongArray.m11541getsVKNKU(array, j));
                kotlin.ULongArray.m11546setk8EXiF4(array, j, tmp);
                i++;
                j--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort--nroSd4, reason: not valid java name */
    private static final void m11766quickSortnroSd4(long[] array, int left, int right) {
        int index = m11762partitionnroSd4(array, left, right);
        if (left < index - 1) {
            m11766quickSortnroSd4(array, left, index - 1);
        }
        if (index < right) {
            m11766quickSortnroSd4(array, index, right);
        }
    }

    /* JADX INFO: renamed from: sortArray-4UcCI2c, reason: not valid java name */
    public static final void m11771sortArray4UcCI2c(byte[] array, int fromIndex, int toIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        m11767quickSort4UcCI2c(array, fromIndex, toIndex - 1);
    }

    /* JADX INFO: renamed from: sortArray-Aa5vz7o, reason: not valid java name */
    public static final void m11772sortArrayAa5vz7o(short[] array, int fromIndex, int toIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        m11768quickSortAa5vz7o(array, fromIndex, toIndex - 1);
    }

    /* JADX INFO: renamed from: sortArray-oBK06Vg, reason: not valid java name */
    public static final void m11773sortArrayoBK06Vg(int[] array, int fromIndex, int toIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        m11769quickSortoBK06Vg(array, fromIndex, toIndex - 1);
    }

    /* JADX INFO: renamed from: sortArray--nroSd4, reason: not valid java name */
    public static final void m11770sortArraynroSd4(long[] array, int fromIndex, int toIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        m11766quickSortnroSd4(array, fromIndex, toIndex - 1);
    }
}
