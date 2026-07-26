package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: Dispatcher.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\n\u0010\u0007\u001a\u00060\bj\u0002`\tH\u0016J\u001c\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\n\u0010\u0007\u001a\u00060\bj\u0002`\tH\u0017J\u0010\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0017¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/scheduling/UnlimitedIoScheduler;", "Lkotlinx/coroutines/CoroutineDispatcher;", "()V", "dispatch", "", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "dispatchYield", "limitedParallelism", "parallelism", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class UnlimitedIoScheduler extends kotlinx.coroutines.CoroutineDispatcher {
    public static final kotlinx.coroutines.scheduling.UnlimitedIoScheduler INSTANCE = new kotlinx.coroutines.scheduling.UnlimitedIoScheduler();

    private UnlimitedIoScheduler() {
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatchYield(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlinx.coroutines.scheduling.DefaultScheduler.INSTANCE.dispatchWithContext$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(block, kotlinx.coroutines.scheduling.TasksKt.BlockingContext, true);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    /* JADX INFO: renamed from: dispatch */
    public void mo12864dispatch(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlinx.coroutines.scheduling.DefaultScheduler.INSTANCE.dispatchWithContext$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(block, kotlinx.coroutines.scheduling.TasksKt.BlockingContext, false);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public kotlinx.coroutines.CoroutineDispatcher limitedParallelism(int parallelism) {
        kotlinx.coroutines.internal.LimitedDispatcherKt.checkParallelism(parallelism);
        return parallelism >= kotlinx.coroutines.scheduling.TasksKt.MAX_POOL_SIZE ? this : super.limitedParallelism(parallelism);
    }
}
