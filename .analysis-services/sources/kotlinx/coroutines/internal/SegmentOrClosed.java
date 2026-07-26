package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ConcurrentLinkedList.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0081@\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003B\u0011\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\u001a\u0010\u0010\u001a\u00020\b2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0003HÖ\u0003¢\u0006\u0004\b\u0012\u0010\u0013J\u0010\u0010\u0014\u001a\u00020\u0015HÖ\u0001¢\u0006\u0004\b\u0016\u0010\u0017J\u0010\u0010\u0018\u001a\u00020\u0019HÖ\u0001¢\u0006\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0017\u0010\u000b\u001a\u00028\u00008F¢\u0006\f\u0012\u0004\b\f\u0010\r\u001a\u0004\b\u000e\u0010\u000fR\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\u0088\u0001\u0004\u0092\u0001\u0004\u0018\u00010\u0003¨\u0006\u001c"}, d2 = {"Lkotlinx/coroutines/internal/SegmentOrClosed;", "S", "Lkotlinx/coroutines/internal/Segment;", "", "value", "constructor-impl", "(Ljava/lang/Object;)Ljava/lang/Object;", "isClosed", "", "isClosed-impl", "(Ljava/lang/Object;)Z", "segment", "getSegment$annotations", "()V", "getSegment-impl", "(Ljava/lang/Object;)Lkotlinx/coroutines/internal/Segment;", "equals", "other", "equals-impl", "(Ljava/lang/Object;Ljava/lang/Object;)Z", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "hashCode-impl", "(Ljava/lang/Object;)I", "toString", "", "toString-impl", "(Ljava/lang/Object;)Ljava/lang/String;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
@kotlin.jvm.JvmInline
public final class SegmentOrClosed<S extends kotlinx.coroutines.internal.Segment<S>> {
    private final java.lang.Object value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ kotlinx.coroutines.internal.SegmentOrClosed m12866boximpl(java.lang.Object obj) {
        return new kotlinx.coroutines.internal.SegmentOrClosed(obj);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static <S extends kotlinx.coroutines.internal.Segment<S>> java.lang.Object m12867constructorimpl(java.lang.Object obj) {
        return obj;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m12868equalsimpl(java.lang.Object obj, java.lang.Object obj2) {
        return (obj2 instanceof kotlinx.coroutines.internal.SegmentOrClosed) && kotlin.jvm.internal.Intrinsics.areEqual(obj, ((kotlinx.coroutines.internal.SegmentOrClosed) obj2).getValue());
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m12869equalsimpl0(java.lang.Object obj, java.lang.Object obj2) {
        return kotlin.jvm.internal.Intrinsics.areEqual(obj, obj2);
    }

    public static /* synthetic */ void getSegment$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m12871hashCodeimpl(java.lang.Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m12873toStringimpl(java.lang.Object obj) {
        return "SegmentOrClosed(value=" + obj + ")";
    }

    public boolean equals(java.lang.Object obj) {
        return m12868equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m12871hashCodeimpl(this.value);
    }

    public java.lang.String toString() {
        return m12873toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ java.lang.Object getValue() {
        return this.value;
    }

    private /* synthetic */ SegmentOrClosed(java.lang.Object value) {
        this.value = value;
    }

    /* JADX INFO: renamed from: isClosed-impl, reason: not valid java name */
    public static final boolean m12872isClosedimpl(java.lang.Object arg0) {
        return arg0 == kotlinx.coroutines.internal.ConcurrentLinkedListKt.CLOSED;
    }

    /* JADX INFO: renamed from: getSegment-impl, reason: not valid java name */
    public static final S m12870getSegmentimpl(java.lang.Object arg0) {
        if (arg0 == kotlinx.coroutines.internal.ConcurrentLinkedListKt.CLOSED) {
            throw new java.lang.IllegalStateException("Does not contain segment".toString());
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(arg0, "null cannot be cast to non-null type S of kotlinx.coroutines.internal.SegmentOrClosed");
        return (S) arg0;
    }
}
