package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: WorkQueue.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0012\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0013\u001a\u00020\u00072\b\b\u0002\u0010\u0014\u001a\u00020\u0015J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0002J\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\b\u0010\u001b\u001a\u0004\u0018\u00010\u0007J\b\u0010\u001c\u001a\u0004\u0018\u00010\u0007J\n\u0010\u001d\u001a\u0004\u0018\u00010\u0007H\u0002J\b\u0010\u001e\u001a\u0004\u0018\u00010\u0007J\u0010\u0010\u001f\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u001aH\u0002J\u0012\u0010!\u001a\u0004\u0018\u00010\u00072\u0006\u0010\"\u001a\u00020\u0015H\u0002J\u0016\u0010#\u001a\u0004\u0018\u00010\u00072\n\u0010$\u001a\u00060\tj\u0002`%H\u0002J\u001a\u0010&\u001a\u0004\u0018\u00010\u00072\u0006\u0010'\u001a\u00020\t2\u0006\u0010\"\u001a\u00020\u0015H\u0002J\"\u0010(\u001a\u00020)2\n\u0010$\u001a\u00060\tj\u0002`%2\u000e\u0010*\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070+J$\u0010,\u001a\u00020)2\n\u0010$\u001a\u00060\tj\u0002`%2\u000e\u0010*\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070+H\u0002J\u000e\u0010-\u001a\u00020\u0018*\u0004\u0018\u00010\u0007H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\u00020\t8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000b¨\u0006."}, d2 = {"Lkotlinx/coroutines/scheduling/WorkQueue;", "", "()V", "blockingTasksInBuffer", "Lkotlinx/atomicfu/AtomicInt;", "buffer", "Ljava/util/concurrent/atomic/AtomicReferenceArray;", "Lkotlinx/coroutines/scheduling/Task;", "bufferSize", "", "getBufferSize", "()I", "consumerIndex", "lastScheduledTask", "Lkotlinx/atomicfu/AtomicRef;", "producerIndex", "size", "getSize$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "add", "task", "fair", "", "addLast", "offloadAllWorkTo", "", "globalQueue", "Lkotlinx/coroutines/scheduling/GlobalQueue;", "poll", "pollBlocking", "pollBuffer", "pollCpu", "pollTo", "queue", "pollWithExclusiveMode", "onlyBlocking", "stealWithExclusiveMode", "stealingMode", "Lkotlinx/coroutines/scheduling/StealingMode;", "tryExtractFromTheMiddle", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "trySteal", "", "stolenTaskRef", "Lkotlin/jvm/internal/Ref$ObjectRef;", "tryStealLastScheduled", "decrementIfBlocking", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class WorkQueue {
    private final java.util.concurrent.atomic.AtomicReferenceArray<kotlinx.coroutines.scheduling.Task> buffer = new java.util.concurrent.atomic.AtomicReferenceArray<>(128);
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.scheduling.Task> lastScheduledTask = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
    private final kotlinx.atomicfu.AtomicInt producerIndex = kotlinx.atomicfu.AtomicFU.atomic(0);
    private final kotlinx.atomicfu.AtomicInt consumerIndex = kotlinx.atomicfu.AtomicFU.atomic(0);
    private final kotlinx.atomicfu.AtomicInt blockingTasksInBuffer = kotlinx.atomicfu.AtomicFU.atomic(0);

    private final int getBufferSize() {
        return this.producerIndex.getValue() - this.consumerIndex.getValue();
    }

    public final int getSize$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return this.lastScheduledTask.getValue() != null ? getBufferSize() + 1 : getBufferSize();
    }

    public final kotlinx.coroutines.scheduling.Task poll() {
        kotlinx.coroutines.scheduling.Task andSet = this.lastScheduledTask.getAndSet(null);
        return andSet == null ? pollBuffer() : andSet;
    }

    public static /* synthetic */ kotlinx.coroutines.scheduling.Task add$default(kotlinx.coroutines.scheduling.WorkQueue workQueue, kotlinx.coroutines.scheduling.Task task, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return workQueue.add(task, z);
    }

    public final kotlinx.coroutines.scheduling.Task add(kotlinx.coroutines.scheduling.Task task, boolean fair) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(task, "task");
        if (fair) {
            return addLast(task);
        }
        kotlinx.coroutines.scheduling.Task previous = this.lastScheduledTask.getAndSet(task);
        if (previous == null) {
            return null;
        }
        return addLast(previous);
    }

    private final kotlinx.coroutines.scheduling.Task addLast(kotlinx.coroutines.scheduling.Task task) {
        if (getBufferSize() == 127) {
            return task;
        }
        if (task.taskContext.getTaskMode() == 1) {
            this.blockingTasksInBuffer.incrementAndGet();
        }
        int nextIndex = this.producerIndex.getValue() & 127;
        while (this.buffer.get(nextIndex) != null) {
            java.lang.Thread.yield();
        }
        this.buffer.lazySet(nextIndex, task);
        this.producerIndex.incrementAndGet();
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final long trySteal(int stealingMode, kotlin.jvm.internal.Ref.ObjectRef<kotlinx.coroutines.scheduling.Task> stolenTaskRef) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stolenTaskRef, "stolenTaskRef");
        T tPollBuffer = stealingMode == 3 ? pollBuffer() : stealWithExclusiveMode(stealingMode);
        if (tPollBuffer != 0) {
            stolenTaskRef.element = tPollBuffer;
            return -1L;
        }
        return tryStealLastScheduled(stealingMode, stolenTaskRef);
    }

    private final kotlinx.coroutines.scheduling.Task stealWithExclusiveMode(int stealingMode) {
        int start = this.consumerIndex.getValue();
        int end = this.producerIndex.getValue();
        boolean onlyBlocking = stealingMode == 1;
        while (start != end) {
            if (onlyBlocking && this.blockingTasksInBuffer.getValue() == 0) {
                return null;
            }
            int start2 = start + 1;
            kotlinx.coroutines.scheduling.Task taskTryExtractFromTheMiddle = tryExtractFromTheMiddle(start, onlyBlocking);
            if (taskTryExtractFromTheMiddle != null) {
                return taskTryExtractFromTheMiddle;
            }
            start = start2;
        }
        return null;
    }

    public final kotlinx.coroutines.scheduling.Task pollBlocking() {
        return pollWithExclusiveMode(true);
    }

    public final kotlinx.coroutines.scheduling.Task pollCpu() {
        return pollWithExclusiveMode(false);
    }

    private final kotlinx.coroutines.scheduling.Task pollWithExclusiveMode(boolean onlyBlocking) {
        kotlinx.coroutines.scheduling.Task lastScheduled;
        do {
            lastScheduled = this.lastScheduledTask.getValue();
            if (lastScheduled != null) {
                if ((lastScheduled.taskContext.getTaskMode() == 1) == onlyBlocking) {
                }
            }
            int start = this.consumerIndex.getValue();
            int end = this.producerIndex.getValue();
            while (start != end) {
                if (onlyBlocking && this.blockingTasksInBuffer.getValue() == 0) {
                    return null;
                }
                end--;
                kotlinx.coroutines.scheduling.Task task = tryExtractFromTheMiddle(end, onlyBlocking);
                if (task != null) {
                    return task;
                }
            }
            return null;
        } while (!this.lastScheduledTask.compareAndSet(lastScheduled, null));
        return lastScheduled;
    }

    private final kotlinx.coroutines.scheduling.Task tryExtractFromTheMiddle(int index, boolean onlyBlocking) {
        int arrayIndex = index & 127;
        kotlinx.coroutines.scheduling.Task value = this.buffer.get(arrayIndex);
        if (value != null) {
            if ((value.taskContext.getTaskMode() == 1) == onlyBlocking && this.buffer.compareAndSet(arrayIndex, value, null)) {
                if (onlyBlocking) {
                    this.blockingTasksInBuffer.decrementAndGet();
                }
                return value;
            }
        }
        return null;
    }

    public final void offloadAllWorkTo(kotlinx.coroutines.scheduling.GlobalQueue globalQueue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(globalQueue, "globalQueue");
        kotlinx.coroutines.scheduling.Task it = this.lastScheduledTask.getAndSet(null);
        if (it != null) {
            globalQueue.addLast(it);
        }
        while (pollTo(globalQueue)) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object, kotlinx.coroutines.scheduling.Task] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private final long tryStealLastScheduled(int stealingMode, kotlin.jvm.internal.Ref.ObjectRef<kotlinx.coroutines.scheduling.Task> stolenTaskRef) {
        kotlinx.coroutines.scheduling.Task value;
        do {
            value = this.lastScheduledTask.getValue();
            if (value == 0) {
                return -2L;
            }
            if (((value.taskContext.getTaskMode() == 1 ? 1 : 2) & stealingMode) == 0) {
                return -2L;
            }
            long time = kotlinx.coroutines.scheduling.TasksKt.schedulerTimeSource.nanoTime();
            long staleness = time - value.submissionTime;
            if (staleness < kotlinx.coroutines.scheduling.TasksKt.WORK_STEALING_TIME_RESOLUTION_NS) {
                return kotlinx.coroutines.scheduling.TasksKt.WORK_STEALING_TIME_RESOLUTION_NS - staleness;
            }
        } while (!this.lastScheduledTask.compareAndSet(value, null));
        stolenTaskRef.element = value;
        return -1L;
    }

    private final boolean pollTo(kotlinx.coroutines.scheduling.GlobalQueue queue) {
        kotlinx.coroutines.scheduling.Task task = pollBuffer();
        if (task == null) {
            return false;
        }
        queue.addLast(task);
        return true;
    }

    private final kotlinx.coroutines.scheduling.Task pollBuffer() {
        kotlinx.coroutines.scheduling.Task value;
        while (true) {
            int tailLocal = this.consumerIndex.getValue();
            if (tailLocal - this.producerIndex.getValue() == 0) {
                return null;
            }
            int index = tailLocal & 127;
            if (this.consumerIndex.compareAndSet(tailLocal, tailLocal + 1) && (value = this.buffer.getAndSet(index, null)) != null) {
                decrementIfBlocking(value);
                return value;
            }
        }
    }

    private final void decrementIfBlocking(kotlinx.coroutines.scheduling.Task $this$decrementIfBlocking) {
        if ($this$decrementIfBlocking != null) {
            if ($this$decrementIfBlocking.taskContext.getTaskMode() == 1) {
                int value = this.blockingTasksInBuffer.decrementAndGet();
                if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(value >= 0)) {
                        throw new java.lang.AssertionError();
                    }
                }
            }
        }
    }
}
