package kotlinx.coroutines;

/* JADX INFO: compiled from: EventLoop.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b \u0018\u00002\u00020\u00012\u00020\u0002:\u00043456B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\u0010\u0010\u0018\u001a\n\u0018\u00010\u0019j\u0004\u0018\u0001`\u001aH\u0002J\u001a\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u001d2\n\u0010\u001e\u001a\u00060\u0019j\u0002`\u001aJ\u0014\u0010\u001f\u001a\u00020\u00172\n\u0010 \u001a\u00060\u0019j\u0002`\u001aH\u0016J\u0014\u0010!\u001a\u00020\f2\n\u0010 \u001a\u00060\u0019j\u0002`\u001aH\u0002J\b\u0010\"\u001a\u00020\u0013H\u0016J\b\u0010#\u001a\u00020\u0017H\u0002J\b\u0010$\u001a\u00020\u0017H\u0004J\u0016\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020(J\u0018\u0010)\u001a\u00020*2\u0006\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020(H\u0002J\u001c\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u00132\n\u0010\u001e\u001a\u00060\u0019j\u0002`\u001aH\u0004J\u001e\u0010.\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u00132\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u001700H\u0016J\u0010\u00101\u001a\u00020\f2\u0006\u0010 \u001a\u00020(H\u0002J\b\u00102\u001a\u00020\u0017H\u0016R\u0016\u0010\u0004\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R$\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8B@BX\u0082\u000e¢\u0006\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\f8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000eR\u0014\u0010\u0012\u001a\u00020\u00138TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015¨\u00067"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase;", "Lkotlinx/coroutines/EventLoopImplPlatform;", "Lkotlinx/coroutines/Delay;", "()V", "_delayed", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue;", "_isCompleted", "Lkotlinx/atomicfu/AtomicBoolean;", "_queue", "", "value", "", "isCompleted", "()Z", "setCompleted", "(Z)V", "isEmpty", "nextTime", "", "getNextTime", "()J", "closeQueue", "", "dequeue", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "dispatch", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "enqueue", "task", "enqueueImpl", "processNextEvent", "rescheduleAllDelayed", "resetAll", "schedule", "now", "delayedTask", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "scheduleImpl", "", "scheduleInvokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "shouldUnpark", "shutdown", "DelayedResumeTask", "DelayedRunnableTask", "DelayedTask", "DelayedTaskQueue", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class EventLoopImplBase extends kotlinx.coroutines.EventLoopImplPlatform implements kotlinx.coroutines.Delay {
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> _queue = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue> _delayed = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
    private final kotlinx.atomicfu.AtomicBoolean _isCompleted = kotlinx.atomicfu.AtomicFU.atomic(false);

    @Override // kotlinx.coroutines.Delay
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated without replacement as an internal method never intended for public use")
    public java.lang.Object delay(long time, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.Delay.DefaultImpls.delay(this, time, continuation);
    }

    public kotlinx.coroutines.DisposableHandle invokeOnTimeout(long timeMillis, java.lang.Runnable block, kotlin.coroutines.CoroutineContext context) {
        return kotlinx.coroutines.Delay.DefaultImpls.invokeOnTimeout(this, timeMillis, block, context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isCompleted() {
        return this._isCompleted.getValue();
    }

    private final void setCompleted(boolean value) {
        this._isCompleted.setValue(value);
    }

    @Override // kotlinx.coroutines.EventLoop
    protected boolean isEmpty() {
        if (!isUnconfinedQueueEmpty()) {
            return false;
        }
        kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue delayed = this._delayed.getValue();
        if (delayed != null && !delayed.isEmpty()) {
            return false;
        }
        java.lang.Object queue = this._queue.getValue();
        if (queue == null) {
            return true;
        }
        return queue instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore ? ((kotlinx.coroutines.internal.LockFreeTaskQueueCore) queue).isEmpty() : queue == kotlinx.coroutines.EventLoop_commonKt.CLOSED_EMPTY;
    }

    @Override // kotlinx.coroutines.EventLoop
    protected long getNextTime() {
        kotlinx.coroutines.EventLoopImplBase.DelayedTask nextDelayedTask;
        if (super.getNextTime() == 0) {
            return 0L;
        }
        java.lang.Object queue = this._queue.getValue();
        if (queue != null) {
            if (!(queue instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore)) {
                return queue == kotlinx.coroutines.EventLoop_commonKt.CLOSED_EMPTY ? Long.MAX_VALUE : 0L;
            }
            if (!((kotlinx.coroutines.internal.LockFreeTaskQueueCore) queue).isEmpty()) {
                return 0L;
            }
        }
        kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue value = this._delayed.getValue();
        if (value == null || (nextDelayedTask = value.peek()) == null) {
            return Long.MAX_VALUE;
        }
        long j = nextDelayedTask.nanoTime;
        kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
        return kotlin.ranges.RangesKt.coerceAtLeast(j - (timeSource != null ? timeSource.nanoTime() : java.lang.System.nanoTime()), 0L);
    }

    @Override // kotlinx.coroutines.EventLoop
    public void shutdown() {
        kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE.resetEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        setCompleted(true);
        closeQueue();
        while (processNextEvent() <= 0) {
        }
        rescheduleAllDelayed();
    }

    @Override // kotlinx.coroutines.Delay
    /* JADX INFO: renamed from: scheduleResumeAfterDelay */
    public void mo12865scheduleResumeAfterDelay(long timeMillis, kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        long timeNanos = kotlinx.coroutines.EventLoop_commonKt.delayToNanos(timeMillis);
        if (timeNanos < 4611686018427387903L) {
            kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
            long now = timeSource != null ? timeSource.nanoTime() : java.lang.System.nanoTime();
            kotlinx.coroutines.EventLoopImplBase.DelayedResumeTask task = new kotlinx.coroutines.EventLoopImplBase.DelayedResumeTask(this, now + timeNanos, continuation);
            schedule(now, task);
            kotlinx.coroutines.CancellableContinuationKt.disposeOnCancellation(continuation, task);
        }
    }

    protected final kotlinx.coroutines.DisposableHandle scheduleInvokeOnTimeout(long timeMillis, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        long timeNanos = kotlinx.coroutines.EventLoop_commonKt.delayToNanos(timeMillis);
        if (timeNanos < 4611686018427387903L) {
            kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
            long now = timeSource != null ? timeSource.nanoTime() : java.lang.System.nanoTime();
            kotlinx.coroutines.EventLoopImplBase.DelayedRunnableTask task = new kotlinx.coroutines.EventLoopImplBase.DelayedRunnableTask(now + timeNanos, block);
            schedule(now, task);
            return task;
        }
        return kotlinx.coroutines.NonDisposableHandle.INSTANCE;
    }

    @Override // kotlinx.coroutines.EventLoop
    public long processNextEvent() {
        boolean zEnqueueImpl;
        kotlinx.coroutines.EventLoopImplBase.DelayedTask delayedTaskRemoveAtImpl;
        if (processUnconfinedEvent()) {
            return 0L;
        }
        kotlinx.coroutines.internal.ThreadSafeHeap delayed = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) this._delayed.getValue();
        if (delayed != null && !delayed.isEmpty()) {
            kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
            long now = timeSource != null ? timeSource.nanoTime() : java.lang.System.nanoTime();
            do {
                kotlinx.coroutines.internal.ThreadSafeHeap this_$iv = delayed;
                synchronized (this_$iv) {
                    kotlinx.coroutines.internal.ThreadSafeHeapNode first$iv = this_$iv.firstImpl();
                    if (first$iv == null) {
                        delayedTaskRemoveAtImpl = null;
                    } else {
                        kotlinx.coroutines.EventLoopImplBase.DelayedTask it = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) first$iv;
                        if (it.timeToExecute(now)) {
                            zEnqueueImpl = enqueueImpl(it);
                        } else {
                            zEnqueueImpl = false;
                        }
                        if (zEnqueueImpl) {
                            delayedTaskRemoveAtImpl = this_$iv.removeAtImpl(0);
                        } else {
                            delayedTaskRemoveAtImpl = null;
                        }
                    }
                }
            } while (delayedTaskRemoveAtImpl != null);
        }
        java.lang.Runnable task = dequeue();
        if (task != null) {
            task.run();
            return 0L;
        }
        return getNextTime();
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    /* JADX INFO: renamed from: dispatch */
    public final void mo12864dispatch(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        enqueue(block);
    }

    public void enqueue(java.lang.Runnable task) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(task, "task");
        if (enqueueImpl(task)) {
            unpark();
        } else {
            kotlinx.coroutines.DefaultExecutor.INSTANCE.enqueue(task);
        }
    }

    private final boolean enqueueImpl(java.lang.Runnable task) {
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._queue;
        while (true) {
            java.lang.Object queue = atomicRef.getValue();
            if (isCompleted()) {
                return false;
            }
            if (queue == null) {
                if (this._queue.compareAndSet(null, task)) {
                    return true;
                }
            } else if (!(queue instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore)) {
                if (queue == kotlinx.coroutines.EventLoop_commonKt.CLOSED_EMPTY) {
                    return false;
                }
                kotlinx.coroutines.internal.LockFreeTaskQueueCore newQueue = new kotlinx.coroutines.internal.LockFreeTaskQueueCore(8, true);
                kotlin.jvm.internal.Intrinsics.checkNotNull(queue, "null cannot be cast to non-null type java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }");
                newQueue.addLast((java.lang.Runnable) queue);
                newQueue.addLast(task);
                if (this._queue.compareAndSet(queue, newQueue)) {
                    return true;
                }
            } else {
                kotlin.jvm.internal.Intrinsics.checkNotNull(queue, "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeTaskQueueCore<java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }>{ kotlinx.coroutines.EventLoop_commonKt.Queue<java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }> }");
                switch (((kotlinx.coroutines.internal.LockFreeTaskQueueCore) queue).addLast(task)) {
                    case 0:
                        return true;
                    case 1:
                        this._queue.compareAndSet(queue, ((kotlinx.coroutines.internal.LockFreeTaskQueueCore) queue).next());
                        break;
                    case 2:
                        return false;
                }
            }
        }
    }

    private final java.lang.Runnable dequeue() {
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._queue;
        while (true) {
            java.lang.Object queue = atomicRef.getValue();
            if (queue == null) {
                return null;
            }
            if (!(queue instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore)) {
                if (queue == kotlinx.coroutines.EventLoop_commonKt.CLOSED_EMPTY) {
                    return null;
                }
                if (this._queue.compareAndSet(queue, null)) {
                    kotlin.jvm.internal.Intrinsics.checkNotNull(queue, "null cannot be cast to non-null type java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }");
                    return (java.lang.Runnable) queue;
                }
            } else {
                kotlin.jvm.internal.Intrinsics.checkNotNull(queue, "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeTaskQueueCore<java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }>{ kotlinx.coroutines.EventLoop_commonKt.Queue<java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }> }");
                java.lang.Object result = ((kotlinx.coroutines.internal.LockFreeTaskQueueCore) queue).removeFirstOrNull();
                if (result != kotlinx.coroutines.internal.LockFreeTaskQueueCore.REMOVE_FROZEN) {
                    return (java.lang.Runnable) result;
                }
                this._queue.compareAndSet(queue, ((kotlinx.coroutines.internal.LockFreeTaskQueueCore) queue).next());
            }
        }
    }

    private final void closeQueue() {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !isCompleted()) {
            throw new java.lang.AssertionError();
        }
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._queue;
        while (true) {
            java.lang.Object queue = atomicRef.getValue();
            if (queue == null) {
                if (this._queue.compareAndSet(null, kotlinx.coroutines.EventLoop_commonKt.CLOSED_EMPTY)) {
                    return;
                }
            } else if (!(queue instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore)) {
                if (queue == kotlinx.coroutines.EventLoop_commonKt.CLOSED_EMPTY) {
                    return;
                }
                kotlinx.coroutines.internal.LockFreeTaskQueueCore newQueue = new kotlinx.coroutines.internal.LockFreeTaskQueueCore(8, true);
                kotlin.jvm.internal.Intrinsics.checkNotNull(queue, "null cannot be cast to non-null type java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }");
                newQueue.addLast((java.lang.Runnable) queue);
                if (this._queue.compareAndSet(queue, newQueue)) {
                    return;
                }
            } else {
                ((kotlinx.coroutines.internal.LockFreeTaskQueueCore) queue).close();
                return;
            }
        }
    }

    public final void schedule(long now, kotlinx.coroutines.EventLoopImplBase.DelayedTask delayedTask) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delayedTask, "delayedTask");
        switch (scheduleImpl(now, delayedTask)) {
            case 0:
                if (shouldUnpark(delayedTask)) {
                    unpark();
                    return;
                }
                return;
            case 1:
                reschedule(now, delayedTask);
                return;
            case 2:
                return;
            default:
                throw new java.lang.IllegalStateException("unexpected result".toString());
        }
    }

    private final boolean shouldUnpark(kotlinx.coroutines.EventLoopImplBase.DelayedTask task) {
        kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue value = this._delayed.getValue();
        return (value != null ? value.peek() : null) == task;
    }

    private final int scheduleImpl(long now, kotlinx.coroutines.EventLoopImplBase.DelayedTask delayedTask) {
        if (isCompleted()) {
            return 1;
        }
        kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue delayedQueue = this._delayed.getValue();
        if (delayedQueue == null) {
            kotlinx.coroutines.EventLoopImplBase $this$scheduleImpl_u24lambda_u248 = this;
            $this$scheduleImpl_u24lambda_u248._delayed.compareAndSet(null, new kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue(now));
            kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue value = $this$scheduleImpl_u24lambda_u248._delayed.getValue();
            kotlin.jvm.internal.Intrinsics.checkNotNull(value);
            delayedQueue = value;
        }
        return delayedTask.scheduleTask(now, delayedQueue, this);
    }

    protected final void resetAll() {
        this._queue.setValue(null);
        this._delayed.setValue(null);
    }

    private final void rescheduleAllDelayed() {
        kotlinx.coroutines.EventLoopImplBase.DelayedTask delayedTask;
        kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
        long now = timeSource != null ? timeSource.nanoTime() : java.lang.System.nanoTime();
        while (true) {
            kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue value = this._delayed.getValue();
            if (value != null && (delayedTask = value.removeFirstOrNull()) != null) {
                reschedule(now, delayedTask);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: compiled from: EventLoop.common.kt */
    @kotlin.Metadata(d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\b \u0018\u00002\u00060\u0001j\u0002`\u00022\b\u0012\u0004\u0012\u00020\u00000\u00032\u00020\u00042\u00020\u00052\u00060\u0006j\u0002`\u0007B\r\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0011\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u0000H\u0096\u0002J\u0006\u0010\u001b\u001a\u00020\u001cJ\u001e\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"J\u000e\u0010#\u001a\u00020$2\u0006\u0010\u001e\u001a\u00020\tJ\b\u0010%\u001a\u00020&H\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R0\u0010\u000e\u001a\b\u0012\u0002\b\u0003\u0018\u00010\r2\f\u0010\f\u001a\b\u0012\u0002\b\u0003\u0018\u00010\r8V@VX\u0096\u000e¢\u0006\f\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0014X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u0012\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "", "Lkotlinx/coroutines/DisposableHandle;", "Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "nanoTime", "", "(J)V", "_heap", "value", "Lkotlinx/coroutines/internal/ThreadSafeHeap;", "heap", "getHeap", "()Lkotlinx/coroutines/internal/ThreadSafeHeap;", "setHeap", "(Lkotlinx/coroutines/internal/ThreadSafeHeap;)V", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "getIndex", "()I", "setIndex", "(I)V", "compareTo", "other", "dispose", "", "scheduleTask", "now", "delayed", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue;", "eventLoop", "Lkotlinx/coroutines/EventLoopImplBase;", "timeToExecute", "", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static abstract class DelayedTask implements java.lang.Runnable, java.lang.Comparable<kotlinx.coroutines.EventLoopImplBase.DelayedTask>, kotlinx.coroutines.DisposableHandle, kotlinx.coroutines.internal.ThreadSafeHeapNode {
        private volatile java.lang.Object _heap;
        private int index = -1;
        public long nanoTime;

        public DelayedTask(long nanoTime) {
            this.nanoTime = nanoTime;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public kotlinx.coroutines.internal.ThreadSafeHeap<?> getHeap() {
            java.lang.Object obj = this._heap;
            if (obj instanceof kotlinx.coroutines.internal.ThreadSafeHeap) {
                return (kotlinx.coroutines.internal.ThreadSafeHeap) obj;
            }
            return null;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public void setHeap(kotlinx.coroutines.internal.ThreadSafeHeap<?> threadSafeHeap) {
            if (!(this._heap != kotlinx.coroutines.EventLoop_commonKt.DISPOSED_TASK)) {
                throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
            }
            this._heap = threadSafeHeap;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public int getIndex() {
            return this.index;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public void setIndex(int i) {
            this.index = i;
        }

        @Override // java.lang.Comparable
        public int compareTo(kotlinx.coroutines.EventLoopImplBase.DelayedTask other) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            long dTime = this.nanoTime - other.nanoTime;
            if (dTime > 0) {
                return 1;
            }
            return dTime < 0 ? -1 : 0;
        }

        public final boolean timeToExecute(long now) {
            return now - this.nanoTime >= 0;
        }

        public final int scheduleTask(long now, kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue delayed, kotlinx.coroutines.EventLoopImplBase eventLoop) throws java.lang.Throwable {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delayed, "delayed");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(eventLoop, "eventLoop");
            synchronized (this) {
                try {
                    if (this._heap == kotlinx.coroutines.EventLoop_commonKt.DISPOSED_TASK) {
                        return 2;
                    }
                    kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue this_$iv = delayed;
                    synchronized (this_$iv) {
                        try {
                            try {
                                kotlinx.coroutines.EventLoopImplBase.DelayedTask firstTask = this_$iv.firstImpl();
                                if (!eventLoop.isCompleted()) {
                                    if (firstTask == null) {
                                        try {
                                            delayed.timeNow = now;
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            throw th;
                                        }
                                    } else {
                                        try {
                                            long firstTime = firstTask.nanoTime;
                                            long minTime = firstTime - now >= 0 ? now : firstTime;
                                            if (minTime - delayed.timeNow > 0) {
                                                delayed.timeNow = minTime;
                                            }
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                            throw th;
                                        }
                                    }
                                    if (this.nanoTime - delayed.timeNow < 0) {
                                        this.nanoTime = delayed.timeNow;
                                    }
                                    this_$iv.addImpl(this);
                                    return 0;
                                }
                                try {
                                    return 1;
                                } catch (java.lang.Throwable th3) {
                                    th = th3;
                                }
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                        }
                    }
                } catch (java.lang.Throwable th6) {
                    th = th6;
                }
                throw th;
            }
        }

        @Override // kotlinx.coroutines.DisposableHandle
        public final void dispose() {
            synchronized (this) {
                java.lang.Object heap = this._heap;
                if (heap == kotlinx.coroutines.EventLoop_commonKt.DISPOSED_TASK) {
                    return;
                }
                kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue delayedTaskQueue = heap instanceof kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue ? (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) heap : null;
                if (delayedTaskQueue != null) {
                    delayedTaskQueue.remove(this);
                }
                this._heap = kotlinx.coroutines.EventLoop_commonKt.DISPOSED_TASK;
                kotlin.Unit unit = kotlin.Unit.INSTANCE;
            }
        }

        public java.lang.String toString() {
            return "Delayed[nanos=" + this.nanoTime + "]";
        }
    }

    /* JADX INFO: compiled from: EventLoop.common.kt */
    @kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\u0006H\u0016J\b\u0010\t\u001a\u00020\nH\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase$DelayedResumeTask;", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "nanoTime", "", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Lkotlinx/coroutines/EventLoopImplBase;JLkotlinx/coroutines/CancellableContinuation;)V", "run", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class DelayedResumeTask extends kotlinx.coroutines.EventLoopImplBase.DelayedTask {
        private final kotlinx.coroutines.CancellableContinuation<kotlin.Unit> cont;
        final /* synthetic */ kotlinx.coroutines.EventLoopImplBase this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public DelayedResumeTask(kotlinx.coroutines.EventLoopImplBase this$0, long nanoTime, kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> cont) {
            super(nanoTime);
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cont, "cont");
            this.this$0 = this$0;
            this.cont = cont;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.cont.resumeUndispatched(this.this$0, kotlin.Unit.INSTANCE);
        }

        @Override // kotlinx.coroutines.EventLoopImplBase.DelayedTask
        public java.lang.String toString() {
            return super.toString() + this.cont;
        }
    }

    /* JADX INFO: compiled from: EventLoop.common.kt */
    @kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016R\u0012\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase$DelayedRunnableTask;", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "nanoTime", "", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "(JLjava/lang/Runnable;)V", "run", "", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class DelayedRunnableTask extends kotlinx.coroutines.EventLoopImplBase.DelayedTask {
        private final java.lang.Runnable block;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DelayedRunnableTask(long nanoTime, java.lang.Runnable block) {
            super(nanoTime);
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
            this.block = block;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.block.run();
        }

        @Override // kotlinx.coroutines.EventLoopImplBase.DelayedTask
        public java.lang.String toString() {
            return super.toString() + this.block;
        }
    }

    /* JADX INFO: compiled from: EventLoop.common.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005R\u0012\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue;", "Lkotlinx/coroutines/internal/ThreadSafeHeap;", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "timeNow", "", "(J)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class DelayedTaskQueue extends kotlinx.coroutines.internal.ThreadSafeHeap<kotlinx.coroutines.EventLoopImplBase.DelayedTask> {
        public long timeNow;

        public DelayedTaskQueue(long timeNow) {
            this.timeNow = timeNow;
        }
    }
}
