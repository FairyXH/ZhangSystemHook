package kotlin.ranges;

/* JADX INFO: compiled from: Ranges.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\bg\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003J\u0016\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\u0005H\u0016J\u001d\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00028\u00002\u0006\u0010\u000b\u001a\u00028\u0000H&¢\u0006\u0002\u0010\f¨\u0006\r"}, d2 = {"Lkotlin/ranges/ClosedFloatingPointRange;", "T", "", "Lkotlin/ranges/ClosedRange;", "contains", "", "value", "(Ljava/lang/Comparable;)Z", "isEmpty", "lessThanOrEquals", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Z", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ClosedFloatingPointRange<T extends java.lang.Comparable<? super T>> extends kotlin.ranges.ClosedRange<T> {
    @Override // kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    boolean contains(T value);

    @Override // kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    boolean isEmpty();

    boolean lessThanOrEquals(T a, T b);

    /* JADX INFO: compiled from: Ranges.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static <T extends java.lang.Comparable<? super T>> boolean contains(kotlin.ranges.ClosedFloatingPointRange<T> closedFloatingPointRange, T value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            return closedFloatingPointRange.lessThanOrEquals(closedFloatingPointRange.getStart(), value) && closedFloatingPointRange.lessThanOrEquals(value, closedFloatingPointRange.getEndInclusive());
        }

        public static <T extends java.lang.Comparable<? super T>> boolean isEmpty(kotlin.ranges.ClosedFloatingPointRange<T> closedFloatingPointRange) {
            return !closedFloatingPointRange.lessThanOrEquals(closedFloatingPointRange.getStart(), closedFloatingPointRange.getEndInclusive());
        }
    }
}
