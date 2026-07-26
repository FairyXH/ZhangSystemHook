package kotlinx.coroutines;

/* JADX INFO: compiled from: JobSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/IncompleteStateBox;", "", "state", "Lkotlinx/coroutines/Incomplete;", "(Lkotlinx/coroutines/Incomplete;)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class IncompleteStateBox {
    public final kotlinx.coroutines.Incomplete state;

    public IncompleteStateBox(kotlinx.coroutines.Incomplete state) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(state, "state");
        this.state = state;
    }
}
