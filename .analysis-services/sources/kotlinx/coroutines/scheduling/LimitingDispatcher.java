package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: Deprecated.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0002\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B'\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\u0006\u0010\n\u001a\u00020\u0007¢\u0006\u0002\u0010\u000bJ\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0018H\u0016J\u001c\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001c2\n\u0010\u001d\u001a\u00060\u0013j\u0002`\u0014H\u0016J\u001c\u0010\u001a\u001a\u00020\u00182\n\u0010\u001d\u001a\u00060\u0013j\u0002`\u00142\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u001c\u0010 \u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001c2\n\u0010\u001d\u001a\u00060\u0013j\u0002`\u0014H\u0016J\u0014\u0010!\u001a\u00020\u00182\n\u0010\"\u001a\u00060\u0013j\u0002`\u0014H\u0016J\b\u0010#\u001a\u00020\tH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\u0011\u001a\f\u0012\b\u0012\u00060\u0013j\u0002`\u00140\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u0007X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016¨\u0006$"}, d2 = {"Lkotlinx/coroutines/scheduling/LimitingDispatcher;", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "Lkotlinx/coroutines/scheduling/TaskContext;", "Ljava/util/concurrent/Executor;", "dispatcher", "Lkotlinx/coroutines/scheduling/ExperimentalCoroutineDispatcher;", "parallelism", "", "name", "", "taskMode", "(Lkotlinx/coroutines/scheduling/ExperimentalCoroutineDispatcher;ILjava/lang/String;I)V", "executor", "getExecutor", "()Ljava/util/concurrent/Executor;", "inFlightTasks", "Lkotlinx/atomicfu/AtomicInt;", "queue", "Ljava/util/concurrent/ConcurrentLinkedQueue;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "getTaskMode", "()I", "afterTask", "", "close", "dispatch", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "tailDispatch", "", "dispatchYield", "execute", "command", "toString", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class LimitingDispatcher extends kotlinx.coroutines.ExecutorCoroutineDispatcher implements kotlinx.coroutines.scheduling.TaskContext, java.util.concurrent.Executor {
    private final kotlinx.coroutines.scheduling.ExperimentalCoroutineDispatcher dispatcher;
    private final kotlinx.atomicfu.AtomicInt inFlightTasks;
    private final java.lang.String name;
    private final int parallelism;
    private final java.util.concurrent.ConcurrentLinkedQueue<java.lang.Runnable> queue;
    private final int taskMode;

    @Override // kotlinx.coroutines.scheduling.TaskContext
    public int getTaskMode() {
        return this.taskMode;
    }

    public LimitingDispatcher(kotlinx.coroutines.scheduling.ExperimentalCoroutineDispatcher dispatcher, int parallelism, java.lang.String name, int taskMode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dispatcher, "dispatcher");
        this.dispatcher = dispatcher;
        this.parallelism = parallelism;
        this.name = name;
        this.taskMode = taskMode;
        this.queue = new java.util.concurrent.ConcurrentLinkedQueue<>();
        this.inFlightTasks = kotlinx.atomicfu.AtomicFU.atomic(0);
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher
    public java.util.concurrent.Executor getExecutor() {
        return this;
    }

    @Override // java.util.concurrent.Executor
    public void execute(java.lang.Runnable command) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(command, "command");
        dispatch(command, false);
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new java.lang.IllegalStateException("Close cannot be invoked on LimitingBlockingDispatcher".toString());
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    /* JADX INFO: renamed from: dispatch */
    public void mo12864dispatch(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        dispatch(block, false);
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x000a */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void dispatch(java.lang.Runnable r5, boolean r6) {
        /*
            r4 = this;
            r0 = r5
        L1:
            kotlinx.atomicfu.AtomicInt r1 = r4.inFlightTasks
            int r1 = r1.incrementAndGet()
            int r2 = r4.parallelism
            if (r1 > r2) goto L15
            kotlinx.coroutines.scheduling.ExperimentalCoroutineDispatcher r2 = r4.dispatcher
            r3 = r4
            kotlinx.coroutines.scheduling.TaskContext r3 = (kotlinx.coroutines.scheduling.TaskContext) r3
            r2.dispatchWithContext$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r0, r3, r6)
            return
        L15:
            java.util.concurrent.ConcurrentLinkedQueue<java.lang.Runnable> r2 = r4.queue
            r2.add(r0)
            kotlinx.atomicfu.AtomicInt r2 = r4.inFlightTasks
            int r2 = r2.decrementAndGet()
            int r3 = r4.parallelism
            if (r2 < r3) goto L25
            return
        L25:
            java.util.concurrent.ConcurrentLinkedQueue<java.lang.Runnable> r2 = r4.queue
            java.lang.Object r2 = r2.poll()
            java.lang.Runnable r2 = (java.lang.Runnable) r2
            if (r2 != 0) goto L30
            return
        L30:
            r0 = r2
            goto L1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.LimitingDispatcher.dispatch(java.lang.Runnable, boolean):void");
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatchYield(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        dispatch(block, true);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public java.lang.String toString() {
        java.lang.String str = this.name;
        if (str != null) {
            return str;
        }
        return super.toString() + "[dispatcher = " + this.dispatcher + "]";
    }

    @Override // kotlinx.coroutines.scheduling.TaskContext
    public void afterTask() {
        java.lang.Runnable next = this.queue.poll();
        if (next != null) {
            this.dispatcher.dispatchWithContext$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(next, this, true);
            return;
        }
        this.inFlightTasks.decrementAndGet();
        java.lang.Runnable next2 = this.queue.poll();
        if (next2 == null) {
            return;
        }
        dispatch(next2, true);
    }
}
