package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: LimitedDispatcher.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002:\u0001(B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0001\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0097A¢\u0006\u0002\u0010\u0014J\u001c\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00172\n\u0010\u0018\u001a\u00060\tj\u0002`\nH\u0016J-\u0010\u0019\u001a\u00020\u00112\n\u0010\u0018\u001a\u00060\tj\u0002`\n2\u0016\u0010\u001a\u001a\u0012\u0012\b\u0012\u00060\u001cR\u00020\u0000\u0012\u0004\u0012\u00020\u00110\u001bH\u0082\bJ\u001c\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00172\n\u0010\u0018\u001a\u00060\tj\u0002`\nH\u0017J%\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00132\n\u0010\u0018\u001a\u00060\tj\u0002`\n2\u0006\u0010\u0016\u001a\u00020\u0017H\u0096\u0001J\u0010\u0010!\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\u0017J\u0010\u0010\"\u001a\n\u0018\u00010\tj\u0004\u0018\u0001`\nH\u0002J\u001f\u0010#\u001a\u00020\u00112\u0006\u0010 \u001a\u00020\u00132\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00110%H\u0096\u0001J\b\u0010&\u001a\u00020'H\u0002R\u000e\u0010\u0003\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\u0007\u001a\f\u0012\b\u0012\u00060\tj\u0002`\n0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u00060\u000ej\u0002`\u000fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lkotlinx/coroutines/internal/LimitedDispatcher;", "Lkotlinx/coroutines/CoroutineDispatcher;", "Lkotlinx/coroutines/Delay;", "dispatcher", "parallelism", "", "(Lkotlinx/coroutines/CoroutineDispatcher;I)V", "queue", "Lkotlinx/coroutines/internal/LockFreeTaskQueue;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "runningWorkers", "Lkotlinx/atomicfu/AtomicInt;", "workerAllocationLock", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "delay", "", "time", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "dispatch", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "dispatchInternal", "startWorker", "Lkotlin/Function1;", "Lkotlinx/coroutines/internal/LimitedDispatcher$Worker;", "dispatchYield", "invokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "limitedParallelism", "obtainTaskOrDeallocateWorker", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "tryAllocateWorker", "", "Worker", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class LimitedDispatcher extends kotlinx.coroutines.CoroutineDispatcher implements kotlinx.coroutines.Delay {
    private final /* synthetic */ kotlinx.coroutines.Delay $$delegate_0;
    private final kotlinx.coroutines.CoroutineDispatcher dispatcher;
    private final int parallelism;
    private final kotlinx.coroutines.internal.LockFreeTaskQueue<java.lang.Runnable> queue;
    private final kotlinx.atomicfu.AtomicInt runningWorkers;
    private final java.lang.Object workerAllocationLock;

    @Override // kotlinx.coroutines.Delay
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated without replacement as an internal method never intended for public use")
    public java.lang.Object delay(long j, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return this.$$delegate_0.delay(j, continuation);
    }

    @Override // kotlinx.coroutines.Delay
    public kotlinx.coroutines.DisposableHandle invokeOnTimeout(long timeMillis, java.lang.Runnable block, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        return this.$$delegate_0.invokeOnTimeout(timeMillis, block, context);
    }

    @Override // kotlinx.coroutines.Delay
    /* JADX INFO: renamed from: scheduleResumeAfterDelay */
    public void mo12865scheduleResumeAfterDelay(long timeMillis, kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        this.$$delegate_0.mo12865scheduleResumeAfterDelay(timeMillis, continuation);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public LimitedDispatcher(kotlinx.coroutines.CoroutineDispatcher dispatcher, int parallelism) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dispatcher, "dispatcher");
        this.dispatcher = dispatcher;
        this.parallelism = parallelism;
        kotlinx.coroutines.Delay delay = dispatcher instanceof kotlinx.coroutines.Delay ? (kotlinx.coroutines.Delay) dispatcher : null;
        this.$$delegate_0 = delay == null ? kotlinx.coroutines.DefaultExecutorKt.getDefaultDelay() : delay;
        this.runningWorkers = kotlinx.atomicfu.AtomicFU.atomic(0);
        this.queue = new kotlinx.coroutines.internal.LockFreeTaskQueue<>(false);
        this.workerAllocationLock = new java.lang.Object();
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public kotlinx.coroutines.CoroutineDispatcher limitedParallelism(int parallelism) {
        kotlinx.coroutines.internal.LimitedDispatcherKt.checkParallelism(parallelism);
        return parallelism >= this.parallelism ? this : super.limitedParallelism(parallelism);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    /* JADX INFO: renamed from: dispatch */
    public void mo12864dispatch(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        java.lang.Runnable task$iv;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        this.queue.addLast(block);
        if (this.runningWorkers.getValue() < this.parallelism && tryAllocateWorker() && (task$iv = obtainTaskOrDeallocateWorker()) != null) {
            kotlinx.coroutines.internal.LimitedDispatcher.Worker worker = new kotlinx.coroutines.internal.LimitedDispatcher.Worker(this, task$iv);
            this.dispatcher.mo12864dispatch(this, worker);
        }
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatchYield(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        java.lang.Runnable task$iv;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        this.queue.addLast(block);
        if (this.runningWorkers.getValue() < this.parallelism && tryAllocateWorker() && (task$iv = obtainTaskOrDeallocateWorker()) != null) {
            kotlinx.coroutines.internal.LimitedDispatcher.Worker worker = new kotlinx.coroutines.internal.LimitedDispatcher.Worker(this, task$iv);
            this.dispatcher.dispatchYield(this, worker);
        }
    }

    private final void dispatchInternal(java.lang.Runnable block, kotlin.jvm.functions.Function1<? super kotlinx.coroutines.internal.LimitedDispatcher.Worker, kotlin.Unit> startWorker) {
        java.lang.Runnable task;
        this.queue.addLast(block);
        if (this.runningWorkers.getValue() < this.parallelism && tryAllocateWorker() && (task = obtainTaskOrDeallocateWorker()) != null) {
            startWorker.invoke(new kotlinx.coroutines.internal.LimitedDispatcher.Worker(this, task));
        }
    }

    private final boolean tryAllocateWorker() {
        java.lang.Object lock$iv = this.workerAllocationLock;
        synchronized (lock$iv) {
            if (this.runningWorkers.getValue() >= this.parallelism) {
                return false;
            }
            this.runningWorkers.incrementAndGet();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Runnable obtainTaskOrDeallocateWorker() {
        while (true) {
            java.lang.Runnable nextTask = this.queue.removeFirstOrNull();
            if (nextTask != null) {
                return nextTask;
            }
            java.lang.Object lock$iv = this.workerAllocationLock;
            synchronized (lock$iv) {
                this.runningWorkers.decrementAndGet();
                if (this.queue.getSize() == 0) {
                    return null;
                }
                this.runningWorkers.incrementAndGet();
            }
        }
    }

    /* JADX INFO: compiled from: LimitedDispatcher.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0011\u0012\n\u0010\u0003\u001a\u00060\u0001j\u0002`\u0002¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016R\u0012\u0010\u0003\u001a\u00060\u0001j\u0002`\u0002X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/internal/LimitedDispatcher$Worker;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "currentTask", "(Lkotlinx/coroutines/internal/LimitedDispatcher;Ljava/lang/Runnable;)V", "run", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class Worker implements java.lang.Runnable {
        private java.lang.Runnable currentTask;
        final /* synthetic */ kotlinx.coroutines.internal.LimitedDispatcher this$0;

        public Worker(kotlinx.coroutines.internal.LimitedDispatcher this$0, java.lang.Runnable currentTask) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(currentTask, "currentTask");
            this.this$0 = this$0;
            this.currentTask = currentTask;
        }

        @Override // java.lang.Runnable
        public void run() {
            int fairnessCounter = 0;
            while (true) {
                try {
                    this.currentTask.run();
                } catch (java.lang.Throwable e) {
                    kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(kotlin.coroutines.EmptyCoroutineContext.INSTANCE, e);
                }
                java.lang.Runnable runnableObtainTaskOrDeallocateWorker = this.this$0.obtainTaskOrDeallocateWorker();
                if (runnableObtainTaskOrDeallocateWorker == null) {
                    return;
                }
                this.currentTask = runnableObtainTaskOrDeallocateWorker;
                fairnessCounter++;
                if (fairnessCounter >= 16 && this.this$0.dispatcher.isDispatchNeeded(this.this$0)) {
                    this.this$0.dispatcher.mo12864dispatch(this.this$0, this);
                    return;
                }
            }
        }
    }
}
