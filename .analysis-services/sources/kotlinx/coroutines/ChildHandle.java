package kotlinx.coroutines;

/* JADX INFO: compiled from: Job.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "This is internal API and may be removed in the future releases")
@kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH'R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u00038&X§\u0004¢\u0006\f\u0012\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/ChildHandle;", "Lkotlinx/coroutines/DisposableHandle;", "parent", "Lkotlinx/coroutines/Job;", "getParent$annotations", "()V", "getParent", "()Lkotlinx/coroutines/Job;", "childCancelled", "", "cause", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ChildHandle extends kotlinx.coroutines.DisposableHandle {

    /* JADX INFO: compiled from: Job.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ void getParent$annotations() {
        }
    }

    boolean childCancelled(java.lang.Throwable cause);

    kotlinx.coroutines.Job getParent();
}
