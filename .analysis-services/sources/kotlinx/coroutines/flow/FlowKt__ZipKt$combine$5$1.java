package kotlinx.coroutines.flow;

/* JADX INFO: Add missing generic type declarations: [T] */
/* JADX INFO: compiled from: Zip.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\u0010\u0000\u001a\f\u0012\u0006\u0012\u0004\u0018\u0001H\u0002\u0018\u00010\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "R", "invoke", "()[Ljava/lang/Object;"}, k = 3, mv = {1, 9, 0}, xi = 176)
final class FlowKt__ZipKt$combine$5$1<T> extends kotlin.jvm.internal.Lambda implements kotlin.jvm.functions.Function0<T[]> {
    final /* synthetic */ kotlinx.coroutines.flow.Flow<T>[] $flows;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public FlowKt__ZipKt$combine$5$1(kotlinx.coroutines.flow.Flow<? extends T>[] flowArr) {
        super(0);
        this.$flows = flowArr;
    }

    @Override // kotlin.jvm.functions.Function0
    public final T[] invoke() {
        int length = this.$flows.length;
        kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(0, "T?");
        return (T[]) new java.lang.Object[length];
    }
}
