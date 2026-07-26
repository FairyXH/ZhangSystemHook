package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: CoroutineScheduler.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b!\b\u0000\u0018\u0000 I2\u00020\u00012\u00020\u0002:\u0003IJKB)\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0010\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0011\u0010\r\u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u0007H\u0086\bJ\u0011\u0010\"\u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u0007H\u0082\bJ\b\u0010#\u001a\u00020$H\u0016J\b\u0010%\u001a\u00020\u0004H\u0002J\u001a\u0010&\u001a\u00020 2\n\u0010'\u001a\u00060(j\u0002`)2\u0006\u0010*\u001a\u00020+J\u0011\u0010\u0012\u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u0007H\u0082\bJ\u000e\u0010,\u001a\b\u0018\u00010\u001dR\u00020\u0000H\u0002J\t\u0010-\u001a\u00020$H\u0082\bJ\t\u0010.\u001a\u00020\u0004H\u0082\bJ&\u0010/\u001a\u00020$2\n\u0010'\u001a\u00060(j\u0002`)2\b\b\u0002\u0010*\u001a\u00020+2\b\b\u0002\u00100\u001a\u00020\u0018J\u0014\u00101\u001a\u00020$2\n\u00102\u001a\u00060(j\u0002`)H\u0016J\t\u00103\u001a\u00020\u0007H\u0082\bJ\t\u00104\u001a\u00020\u0004H\u0082\bJ\u0014\u00105\u001a\u00020\u00042\n\u00106\u001a\u00060\u001dR\u00020\u0000H\u0002J\u000e\u00107\u001a\b\u0018\u00010\u001dR\u00020\u0000H\u0002J\u0012\u00108\u001a\u00020\u00182\n\u00106\u001a\u00060\u001dR\u00020\u0000J\"\u00109\u001a\u00020$2\n\u00106\u001a\u00060\u001dR\u00020\u00002\u0006\u0010:\u001a\u00020\u00042\u0006\u0010;\u001a\u00020\u0004J\t\u0010<\u001a\u00020\u0007H\u0082\bJ\u000e\u0010=\u001a\u00020$2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010>\u001a\u00020$2\u0006\u0010?\u001a\u00020\u0007J\u0018\u0010@\u001a\u00020$2\u0006\u0010A\u001a\u00020\u00072\u0006\u0010B\u001a\u00020\u0018H\u0002J\u0006\u0010C\u001a\u00020$J\b\u0010D\u001a\u00020\tH\u0016J\t\u0010E\u001a\u00020\u0018H\u0082\bJ\u0012\u0010F\u001a\u00020\u00182\b\b\u0002\u0010!\u001a\u00020\u0007H\u0002J\b\u0010G\u001a\u00020\u0018H\u0002J$\u0010H\u001a\u0004\u0018\u00010 *\b\u0018\u00010\u001dR\u00020\u00002\u0006\u0010\u001f\u001a\u00020 2\u0006\u00100\u001a\u00020\u0018H\u0002R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0015\u0010\r\u001a\u00020\u00048Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0015\u0010\u0012\u001a\u00020\u00048Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u000fR\u0010\u0010\u0014\u001a\u00020\u00158\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u00020\u00158\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0017\u001a\u00020\u00188F¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0019R\u0010\u0010\u0005\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\t8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001b\u001a\f\u0012\b\u0012\u00060\u001dR\u00020\u00000\u001c8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006L"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "Ljava/util/concurrent/Executor;", "Ljava/io/Closeable;", "corePoolSize", "", "maxPoolSize", "idleWorkerKeepAliveNs", "", "schedulerName", "", "(IIJLjava/lang/String;)V", "_isTerminated", "Lkotlinx/atomicfu/AtomicBoolean;", "availableCpuPermits", "getAvailableCpuPermits", "()I", "controlState", "Lkotlinx/atomicfu/AtomicLong;", "createdWorkers", "getCreatedWorkers", "globalBlockingQueue", "Lkotlinx/coroutines/scheduling/GlobalQueue;", "globalCpuQueue", "isTerminated", "", "()Z", "parkedWorkersStack", "workers", "Lkotlinx/coroutines/internal/ResizableAtomicArray;", "Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "addToGlobalQueue", "task", "Lkotlinx/coroutines/scheduling/Task;", "state", "blockingTasks", "close", "", "createNewWorker", "createTask", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "taskContext", "Lkotlinx/coroutines/scheduling/TaskContext;", "currentWorker", "decrementBlockingTasks", "decrementCreatedWorkers", "dispatch", "tailDispatch", "execute", "command", "incrementBlockingTasks", "incrementCreatedWorkers", "parkedWorkersStackNextIndex", "worker", "parkedWorkersStackPop", "parkedWorkersStackPush", "parkedWorkersStackTopUpdate", "oldIndex", "newIndex", "releaseCpuPermit", "runSafely", "shutdown", "timeout", "signalBlockingWork", "stateSnapshot", "skipUnpark", "signalCpuWork", "toString", "tryAcquireCpuPermit", "tryCreateWorker", "tryUnpark", "submitToLocalQueue", "Companion", "Worker", "WorkerState", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CoroutineScheduler implements java.util.concurrent.Executor, java.io.Closeable {
    private static final long BLOCKING_MASK = 4398044413952L;
    private static final int BLOCKING_SHIFT = 21;
    private static final int CLAIMED = 0;
    private static final long CPU_PERMITS_MASK = 9223367638808264704L;
    private static final int CPU_PERMITS_SHIFT = 42;
    private static final long CREATED_MASK = 2097151;
    public static final int MAX_SUPPORTED_POOL_SIZE = 2097150;
    public static final int MIN_SUPPORTED_POOL_SIZE = 1;
    private static final int PARKED = -1;
    private static final long PARKED_INDEX_MASK = 2097151;
    private static final long PARKED_VERSION_INC = 2097152;
    private static final long PARKED_VERSION_MASK = -2097152;
    private static final int TERMINATED = 1;
    private final kotlinx.atomicfu.AtomicBoolean _isTerminated;
    private final kotlinx.atomicfu.AtomicLong controlState;
    public final int corePoolSize;
    public final kotlinx.coroutines.scheduling.GlobalQueue globalBlockingQueue;
    public final kotlinx.coroutines.scheduling.GlobalQueue globalCpuQueue;
    public final long idleWorkerKeepAliveNs;
    public final int maxPoolSize;
    private final kotlinx.atomicfu.AtomicLong parkedWorkersStack;
    public final java.lang.String schedulerName;
    public final kotlinx.coroutines.internal.ResizableAtomicArray<kotlinx.coroutines.scheduling.CoroutineScheduler.Worker> workers;
    public static final kotlinx.coroutines.internal.Symbol NOT_IN_STACK = new kotlinx.coroutines.internal.Symbol("NOT_IN_STACK");

    /* JADX INFO: compiled from: CoroutineScheduler.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.values().length];
            try {
                iArr[kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.PARKING.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                iArr[kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.BLOCKING.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                iArr[kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.CPU_ACQUIRED.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            try {
                iArr[kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.DORMANT.ordinal()] = 4;
            } catch (java.lang.NoSuchFieldError e4) {
            }
            try {
                iArr[kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED.ordinal()] = 5;
            } catch (java.lang.NoSuchFieldError e5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: compiled from: CoroutineScheduler.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "", "(Ljava/lang/String;I)V", "CPU_ACQUIRED", "BLOCKING", "PARKING", "DORMANT", "TERMINATED", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum WorkerState {
        CPU_ACQUIRED,
        BLOCKING,
        PARKING,
        DORMANT,
        TERMINATED;

        private static final /* synthetic */ kotlin.enums.EnumEntries $ENTRIES = kotlin.enums.EnumEntriesKt.enumEntries($VALUES);

        public static kotlin.enums.EnumEntries<kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState> getEntries() {
            return $ENTRIES;
        }
    }

    public CoroutineScheduler(int corePoolSize, int maxPoolSize, long idleWorkerKeepAliveNs, java.lang.String schedulerName) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(schedulerName, "schedulerName");
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.idleWorkerKeepAliveNs = idleWorkerKeepAliveNs;
        this.schedulerName = schedulerName;
        if (!(this.corePoolSize >= 1)) {
            throw new java.lang.IllegalArgumentException(("Core pool size " + this.corePoolSize + " should be at least 1").toString());
        }
        if (!(this.maxPoolSize >= this.corePoolSize)) {
            throw new java.lang.IllegalArgumentException(("Max pool size " + this.maxPoolSize + " should be greater than or equals to core pool size " + this.corePoolSize).toString());
        }
        if (!(this.maxPoolSize <= 2097150)) {
            throw new java.lang.IllegalArgumentException(("Max pool size " + this.maxPoolSize + " should not exceed maximal supported number of threads 2097150").toString());
        }
        if (!(this.idleWorkerKeepAliveNs > 0)) {
            throw new java.lang.IllegalArgumentException(("Idle worker keep alive time " + this.idleWorkerKeepAliveNs + " must be positive").toString());
        }
        this.globalCpuQueue = new kotlinx.coroutines.scheduling.GlobalQueue();
        this.globalBlockingQueue = new kotlinx.coroutines.scheduling.GlobalQueue();
        this.parkedWorkersStack = kotlinx.atomicfu.AtomicFU.atomic(0L);
        this.workers = new kotlinx.coroutines.internal.ResizableAtomicArray<>((this.corePoolSize + 1) * 2);
        this.controlState = kotlinx.atomicfu.AtomicFU.atomic(((long) this.corePoolSize) << 42);
        this._isTerminated = kotlinx.atomicfu.AtomicFU.atomic(false);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ CoroutineScheduler(int i, int i2, long j, java.lang.String str, int i3, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        long j2;
        java.lang.String str2;
        if ((i3 & 4) == 0) {
            j2 = j;
        } else {
            j2 = kotlinx.coroutines.scheduling.TasksKt.IDLE_WORKER_KEEP_ALIVE_NS;
        }
        if ((i3 & 8) == 0) {
            str2 = str;
        } else {
            str2 = kotlinx.coroutines.scheduling.TasksKt.DEFAULT_SCHEDULER_NAME;
        }
        this(i, i2, j2, str2);
    }

    private final boolean addToGlobalQueue(kotlinx.coroutines.scheduling.Task task) {
        if (task.taskContext.getTaskMode() == 1) {
            return this.globalBlockingQueue.addLast(task);
        }
        return this.globalCpuQueue.addLast(task);
    }

    public final void parkedWorkersStackTopUpdate(kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker, int oldIndex, int newIndex) {
        int updIndex;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(worker, "worker");
        kotlinx.atomicfu.AtomicLong $this$loop$iv = this.parkedWorkersStack;
        while (true) {
            long top = $this$loop$iv.getValue();
            int index = (int) (2097151 & top);
            long updVersion = (PARKED_VERSION_INC + top) & PARKED_VERSION_MASK;
            if (index == oldIndex) {
                if (newIndex == 0) {
                    updIndex = parkedWorkersStackNextIndex(worker);
                } else {
                    updIndex = newIndex;
                }
            } else {
                updIndex = index;
            }
            if (updIndex >= 0 && this.parkedWorkersStack.compareAndSet(top, ((long) updIndex) | updVersion)) {
                return;
            }
        }
    }

    public final boolean parkedWorkersStackPush(kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker) {
        long top;
        long updVersion;
        int updIndex;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(worker, "worker");
        if (worker.getNextParkedWorker() != NOT_IN_STACK) {
            return false;
        }
        kotlinx.atomicfu.AtomicLong $this$loop$iv = this.parkedWorkersStack;
        do {
            top = $this$loop$iv.getValue();
            int index = (int) (2097151 & top);
            updVersion = (PARKED_VERSION_INC + top) & PARKED_VERSION_MASK;
            updIndex = worker.getIndexInArray();
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if ((updIndex != 0 ? 1 : 0) == 0) {
                    throw new java.lang.AssertionError();
                }
            }
            worker.setNextParkedWorker(this.workers.get(index));
        } while (!this.parkedWorkersStack.compareAndSet(top, ((long) updIndex) | updVersion));
        return true;
    }

    private final kotlinx.coroutines.scheduling.CoroutineScheduler.Worker parkedWorkersStackPop() {
        kotlinx.atomicfu.AtomicLong $this$loop$iv = this.parkedWorkersStack;
        while (true) {
            long top = $this$loop$iv.getValue();
            int index = (int) (2097151 & top);
            kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker = this.workers.get(index);
            if (worker == null) {
                return null;
            }
            long updVersion = (PARKED_VERSION_INC + top) & PARKED_VERSION_MASK;
            int updIndex = parkedWorkersStackNextIndex(worker);
            if (updIndex >= 0 && this.parkedWorkersStack.compareAndSet(top, ((long) updIndex) | updVersion)) {
                worker.setNextParkedWorker(NOT_IN_STACK);
                return worker;
            }
        }
    }

    private final int parkedWorkersStackNextIndex(kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker) {
        java.lang.Object next = worker.getNextParkedWorker();
        while (next != NOT_IN_STACK) {
            if (next == null) {
                return 0;
            }
            kotlinx.coroutines.scheduling.CoroutineScheduler.Worker nextWorker = (kotlinx.coroutines.scheduling.CoroutineScheduler.Worker) next;
            int updIndex = nextWorker.getIndexInArray();
            if (updIndex != 0) {
                return updIndex;
            }
            next = nextWorker.getNextParkedWorker();
        }
        return -1;
    }

    private final int getCreatedWorkers() {
        return (int) (this.controlState.getValue() & 2097151);
    }

    private final int getAvailableCpuPermits() {
        long state$iv = this.controlState.getValue();
        return (int) ((CPU_PERMITS_MASK & state$iv) >> 42);
    }

    private final int createdWorkers(long state) {
        return (int) (2097151 & state);
    }

    private final int blockingTasks(long state) {
        return (int) ((BLOCKING_MASK & state) >> 21);
    }

    public final int availableCpuPermits(long state) {
        return (int) ((CPU_PERMITS_MASK & state) >> 42);
    }

    private final int incrementCreatedWorkers() {
        long state$iv = this.controlState.incrementAndGet();
        return (int) (2097151 & state$iv);
    }

    private final int decrementCreatedWorkers() {
        long state$iv = this.controlState.getAndDecrement();
        return (int) (2097151 & state$iv);
    }

    private final long incrementBlockingTasks() {
        return this.controlState.addAndGet(PARKED_VERSION_INC);
    }

    private final void decrementBlockingTasks() {
        this.controlState.addAndGet(PARKED_VERSION_MASK);
    }

    private final boolean tryAcquireCpuPermit() {
        long state;
        long update;
        kotlinx.atomicfu.AtomicLong $this$loop$iv = this.controlState;
        do {
            state = $this$loop$iv.getValue();
            int available = (int) ((CPU_PERMITS_MASK & state) >> 42);
            if (available == 0) {
                return false;
            }
            update = state - 4398046511104L;
        } while (!this.controlState.compareAndSet(state, update));
        return true;
    }

    private final long releaseCpuPermit() {
        return this.controlState.addAndGet(4398046511104L);
    }

    public final boolean isTerminated() {
        return this._isTerminated.getValue();
    }

    @Override // java.util.concurrent.Executor
    public void execute(java.lang.Runnable command) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(command, "command");
        dispatch$default(this, command, null, false, 6, null);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        shutdown(10000L);
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void shutdown(long r18) {
        /*
            Method dump skipped, instruction units count: 235
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.CoroutineScheduler.shutdown(long):void");
    }

    public static /* synthetic */ void dispatch$default(kotlinx.coroutines.scheduling.CoroutineScheduler coroutineScheduler, java.lang.Runnable runnable, kotlinx.coroutines.scheduling.TaskContext taskContext, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            taskContext = kotlinx.coroutines.scheduling.TasksKt.NonBlockingContext;
        }
        if ((i & 4) != 0) {
            z = false;
        }
        coroutineScheduler.dispatch(runnable, taskContext, z);
    }

    public final void dispatch(java.lang.Runnable block, kotlinx.coroutines.scheduling.TaskContext taskContext, boolean tailDispatch) {
        long stateSnapshot;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(taskContext, "taskContext");
        kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
        if (timeSource != null) {
            timeSource.trackTask();
        }
        kotlinx.coroutines.scheduling.Task task = createTask(block, taskContext);
        boolean skipUnpark = false;
        boolean isBlockingTask = task.taskContext.getTaskMode() == 1;
        if (!isBlockingTask) {
            stateSnapshot = 0;
        } else {
            stateSnapshot = this.controlState.addAndGet(PARKED_VERSION_INC);
        }
        kotlinx.coroutines.scheduling.CoroutineScheduler.Worker currentWorker = currentWorker();
        kotlinx.coroutines.scheduling.Task notAdded = submitToLocalQueue(currentWorker, task, tailDispatch);
        if (notAdded != null && !addToGlobalQueue(notAdded)) {
            throw new java.util.concurrent.RejectedExecutionException(this.schedulerName + " was terminated");
        }
        if (tailDispatch && currentWorker != null) {
            skipUnpark = true;
        }
        if (isBlockingTask) {
            signalBlockingWork(stateSnapshot, skipUnpark);
        } else {
            if (skipUnpark) {
                return;
            }
            signalCpuWork();
        }
    }

    public final kotlinx.coroutines.scheduling.Task createTask(java.lang.Runnable block, kotlinx.coroutines.scheduling.TaskContext taskContext) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(taskContext, "taskContext");
        long nanoTime = kotlinx.coroutines.scheduling.TasksKt.schedulerTimeSource.nanoTime();
        if (block instanceof kotlinx.coroutines.scheduling.Task) {
            ((kotlinx.coroutines.scheduling.Task) block).submissionTime = nanoTime;
            ((kotlinx.coroutines.scheduling.Task) block).taskContext = taskContext;
            return (kotlinx.coroutines.scheduling.Task) block;
        }
        return new kotlinx.coroutines.scheduling.TaskImpl(block, nanoTime, taskContext);
    }

    private final void signalBlockingWork(long stateSnapshot, boolean skipUnpark) {
        if (skipUnpark || tryUnpark() || tryCreateWorker(stateSnapshot)) {
            return;
        }
        tryUnpark();
    }

    public final void signalCpuWork() {
        if (tryUnpark() || tryCreateWorker$default(this, 0L, 1, null)) {
            return;
        }
        tryUnpark();
    }

    static /* synthetic */ boolean tryCreateWorker$default(kotlinx.coroutines.scheduling.CoroutineScheduler coroutineScheduler, long j, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            j = coroutineScheduler.controlState.getValue();
        }
        return coroutineScheduler.tryCreateWorker(j);
    }

    private final boolean tryCreateWorker(long state) {
        int created = (int) (2097151 & state);
        int blocking = (int) ((BLOCKING_MASK & state) >> 21);
        int cpuWorkers = kotlin.ranges.RangesKt.coerceAtLeast(created - blocking, 0);
        if (cpuWorkers < this.corePoolSize) {
            int newCpuWorkers = createNewWorker();
            if (newCpuWorkers == 1 && this.corePoolSize > 1) {
                createNewWorker();
            }
            if (newCpuWorkers > 0) {
                return true;
            }
        }
        return false;
    }

    private final boolean tryUnpark() {
        kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker;
        do {
            worker = parkedWorkersStackPop();
            if (worker == null) {
                return false;
            }
        } while (!worker.getWorkerCtl().compareAndSet(-1, 0));
        java.util.concurrent.locks.LockSupport.unpark(worker);
        return true;
    }

    private final int createNewWorker() {
        java.lang.Object lock$iv = this.workers;
        synchronized (lock$iv) {
            if (isTerminated()) {
                return -1;
            }
            long state = this.controlState.getValue();
            int created = (int) (state & 2097151);
            int blocking = (int) ((BLOCKING_MASK & state) >> 21);
            int cpuWorkers = kotlin.ranges.RangesKt.coerceAtLeast(created - blocking, 0);
            if (cpuWorkers >= this.corePoolSize) {
                return 0;
            }
            if (created >= this.maxPoolSize) {
                return 0;
            }
            int newIndex = ((int) (this.controlState.getValue() & 2097151)) + 1;
            if (newIndex > 0 && this.workers.get(newIndex) == null) {
                kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker = new kotlinx.coroutines.scheduling.CoroutineScheduler.Worker(this, newIndex);
                this.workers.setSynchronized(newIndex, worker);
                long state$iv$iv = this.controlState.incrementAndGet();
                if (!(newIndex == ((int) (state$iv$iv & 2097151)))) {
                    throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
                }
                int cpuWorkers2 = cpuWorkers + 1;
                worker.start();
                return cpuWorkers2;
            }
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
    }

    private final kotlinx.coroutines.scheduling.Task submitToLocalQueue(kotlinx.coroutines.scheduling.CoroutineScheduler.Worker $this$submitToLocalQueue, kotlinx.coroutines.scheduling.Task task, boolean tailDispatch) {
        if ($this$submitToLocalQueue == null || $this$submitToLocalQueue.state == kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED) {
            return task;
        }
        if (task.taskContext.getTaskMode() == 0 && $this$submitToLocalQueue.state == kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.BLOCKING) {
            return task;
        }
        $this$submitToLocalQueue.mayHaveLocalTasks = true;
        return $this$submitToLocalQueue.localQueue.add(task, tailDispatch);
    }

    private final kotlinx.coroutines.scheduling.CoroutineScheduler.Worker currentWorker() {
        java.lang.Thread threadCurrentThread = java.lang.Thread.currentThread();
        kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker = threadCurrentThread instanceof kotlinx.coroutines.scheduling.CoroutineScheduler.Worker ? (kotlinx.coroutines.scheduling.CoroutineScheduler.Worker) threadCurrentThread : null;
        if (worker == null) {
            return null;
        }
        kotlinx.coroutines.scheduling.CoroutineScheduler.Worker it = worker;
        if (kotlin.jvm.internal.Intrinsics.areEqual(kotlinx.coroutines.scheduling.CoroutineScheduler.this, this)) {
            return worker;
        }
        return null;
    }

    public java.lang.String toString() {
        int parkedWorkers = 0;
        int blockingWorkers = 0;
        int cpuWorkers = 0;
        int dormant = 0;
        int terminated = 0;
        java.util.ArrayList queueSizes = new java.util.ArrayList();
        int iCurrentLength = this.workers.currentLength();
        for (int index = 1; index < iCurrentLength; index++) {
            kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker = this.workers.get(index);
            if (worker != null) {
                int queueSize = worker.localQueue.getSize$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
                switch (kotlinx.coroutines.scheduling.CoroutineScheduler.WhenMappings.$EnumSwitchMapping$0[worker.state.ordinal()]) {
                    case 1:
                        parkedWorkers++;
                        break;
                    case 2:
                        blockingWorkers++;
                        queueSizes.add(queueSize + "b");
                        break;
                    case 3:
                        cpuWorkers++;
                        queueSizes.add(queueSize + "c");
                        break;
                    case 4:
                        dormant++;
                        if (queueSize > 0) {
                            queueSizes.add(queueSize + "d");
                        }
                        break;
                    case 5:
                        terminated++;
                        break;
                }
            }
        }
        long state = this.controlState.getValue();
        return this.schedulerName + "@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this) + "[Pool Size {core = " + this.corePoolSize + ", max = " + this.maxPoolSize + "}, Worker States {CPU = " + cpuWorkers + ", blocking = " + blockingWorkers + ", parked = " + parkedWorkers + ", dormant = " + dormant + ", terminated = " + terminated + "}, running workers queues = " + queueSizes + ", global CPU queue size = " + this.globalCpuQueue.getSize() + ", global blocking queue size = " + this.globalBlockingQueue.getSize() + ", Control State {created workers= " + ((int) (state & 2097151)) + ", blocking tasks = " + ((int) ((state & BLOCKING_MASK) >> 21)) + ", CPUs acquired = " + (this.corePoolSize - ((int) ((state & CPU_PERMITS_MASK) >> 42))) + "}]";
    }

    public final void runSafely(kotlinx.coroutines.scheduling.Task task) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(task, "task");
        try {
            task.run();
        } catch (java.lang.Throwable e) {
            try {
                java.lang.Thread thread = java.lang.Thread.currentThread();
                thread.getUncaughtExceptionHandler().uncaughtException(thread, e);
                kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
                if (timeSource == null) {
                }
            } finally {
                kotlinx.coroutines.AbstractTimeSource timeSource2 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
                if (timeSource2 != null) {
                    timeSource2.unTrackTask();
                }
            }
        }
    }

    /* JADX INFO: compiled from: CoroutineScheduler.kt */
    @kotlin.Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u001a\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0080\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0007\b\u0002¢\u0006\u0002\u0010\u0005J\u0010\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\u0003H\u0002J\u0010\u0010)\u001a\u00020'2\u0006\u0010(\u001a\u00020\u0003H\u0002J\u0010\u0010*\u001a\u00020'2\u0006\u0010+\u001a\u00020 H\u0002J\u0012\u0010,\u001a\u0004\u0018\u00010 2\u0006\u0010-\u001a\u00020\u000eH\u0002J\n\u0010.\u001a\u0004\u0018\u00010 H\u0002J\n\u0010/\u001a\u0004\u0018\u00010 H\u0002J\u0010\u00100\u001a\u0004\u0018\u00010 2\u0006\u0010\r\u001a\u00020\u000eJ\u0010\u00101\u001a\u00020'2\u0006\u00102\u001a\u00020\u0003H\u0002J\b\u00103\u001a\u00020\u000eH\u0002J\u0006\u00104\u001a\u00020\u000eJ\u000e\u00105\u001a\u00020\u00032\u0006\u00106\u001a\u00020\u0003J\b\u00107\u001a\u00020'H\u0002J\n\u00108\u001a\u0004\u0018\u00010 H\u0002J\b\u00109\u001a\u00020'H\u0016J\u0006\u0010:\u001a\u00020\u0010J\b\u0010;\u001a\u00020'H\u0002J\b\u0010<\u001a\u00020\u000eH\u0002J\b\u0010=\u001a\u00020'H\u0002J\u000e\u0010>\u001a\u00020\u000e2\u0006\u0010?\u001a\u00020\u001dJ\u0016\u0010@\u001a\u0004\u0018\u00010 2\n\u0010A\u001a\u00060\u0003j\u0002`BH\u0002J\b\u0010C\u001a\u00020'H\u0002R$\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0010\u0010\u000b\u001a\u00020\f8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u00020\u000e8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0018\u001a\u00020\u00198Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0012\u0010\u001c\u001a\u00020\u001d8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u001e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010 0\u001fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\"\u001a\u00020#¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%¨\u0006D"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "Ljava/lang/Thread;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler;I)V", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler;)V", "indexInArray", "getIndexInArray", "()I", "setIndexInArray", "(I)V", "localQueue", "Lkotlinx/coroutines/scheduling/WorkQueue;", "mayHaveLocalTasks", "", "minDelayUntilStealableTaskNs", "", "nextParkedWorker", "", "getNextParkedWorker", "()Ljava/lang/Object;", "setNextParkedWorker", "(Ljava/lang/Object;)V", "rngState", "scheduler", "Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "getScheduler", "()Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "state", "Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "stolenTask", "Lkotlin/jvm/internal/Ref$ObjectRef;", "Lkotlinx/coroutines/scheduling/Task;", "terminationDeadline", "workerCtl", "Lkotlinx/atomicfu/AtomicInt;", "getWorkerCtl", "()Lkotlinx/atomicfu/AtomicInt;", "afterTask", "", "taskMode", "beforeTask", "executeTask", "task", "findAnyTask", "scanLocalQueue", "findBlockingTask", "findCpuTask", "findTask", "idleReset", com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, "inStack", "isIo", "nextInt", "upperBound", "park", "pollGlobalQueues", "run", "runSingleTask", "runWorker", "tryAcquireCpuPermit", "tryPark", "tryReleaseCpu", "newState", "trySteal", "stealingMode", "Lkotlinx/coroutines/scheduling/StealingMode;", "tryTerminateWorker", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public final class Worker extends java.lang.Thread {
        private volatile int indexInArray;
        public final kotlinx.coroutines.scheduling.WorkQueue localQueue;
        public boolean mayHaveLocalTasks;
        private long minDelayUntilStealableTaskNs;
        private volatile java.lang.Object nextParkedWorker;
        private int rngState;
        public kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState state;
        private final kotlin.jvm.internal.Ref.ObjectRef<kotlinx.coroutines.scheduling.Task> stolenTask;
        private long terminationDeadline;
        private final kotlinx.atomicfu.AtomicInt workerCtl;

        private Worker() {
            setDaemon(true);
            this.localQueue = new kotlinx.coroutines.scheduling.WorkQueue();
            this.stolenTask = new kotlin.jvm.internal.Ref.ObjectRef<>();
            this.state = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.DORMANT;
            this.workerCtl = kotlinx.atomicfu.AtomicFU.atomic(0);
            this.nextParkedWorker = kotlinx.coroutines.scheduling.CoroutineScheduler.NOT_IN_STACK;
            this.rngState = kotlin.random.Random.INSTANCE.nextInt();
        }

        public final int getIndexInArray() {
            return this.indexInArray;
        }

        public final void setIndexInArray(int index) {
            setName(kotlinx.coroutines.scheduling.CoroutineScheduler.this.schedulerName + "-worker-" + (index == 0 ? "TERMINATED" : java.lang.String.valueOf(index)));
            this.indexInArray = index;
        }

        public Worker(kotlinx.coroutines.scheduling.CoroutineScheduler this$0, int index) {
            this();
            setIndexInArray(index);
        }

        public final kotlinx.coroutines.scheduling.CoroutineScheduler getScheduler() {
            return kotlinx.coroutines.scheduling.CoroutineScheduler.this;
        }

        public final kotlinx.atomicfu.AtomicInt getWorkerCtl() {
            return this.workerCtl;
        }

        public final java.lang.Object getNextParkedWorker() {
            return this.nextParkedWorker;
        }

        public final void setNextParkedWorker(java.lang.Object obj) {
            this.nextParkedWorker = obj;
        }

        private final boolean tryAcquireCpuPermit() {
            boolean z;
            if (this.state == kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.CPU_ACQUIRED) {
                return true;
            }
            kotlinx.coroutines.scheduling.CoroutineScheduler this_$iv = kotlinx.coroutines.scheduling.CoroutineScheduler.this;
            kotlinx.atomicfu.AtomicLong $this$loop$iv$iv = this_$iv.controlState;
            while (true) {
                long state$iv = $this$loop$iv$iv.getValue();
                int available$iv = (int) ((kotlinx.coroutines.scheduling.CoroutineScheduler.CPU_PERMITS_MASK & state$iv) >> 42);
                if (available$iv == 0) {
                    z = false;
                    break;
                }
                long update$iv = state$iv - 4398046511104L;
                if (this_$iv.controlState.compareAndSet(state$iv, update$iv)) {
                    z = true;
                    break;
                }
            }
            if (z) {
                this.state = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.CPU_ACQUIRED;
                return true;
            }
            return false;
        }

        public final boolean tryReleaseCpu(kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState newState) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(newState, "newState");
            kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState previousState = this.state;
            boolean hadCpu = previousState == kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.CPU_ACQUIRED;
            if (hadCpu) {
                kotlinx.coroutines.scheduling.CoroutineScheduler this_$iv = kotlinx.coroutines.scheduling.CoroutineScheduler.this;
                this_$iv.controlState.addAndGet(4398046511104L);
            }
            if (previousState != newState) {
                this.state = newState;
            }
            return hadCpu;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            runWorker();
        }

        private final void runWorker() {
            boolean rescanned = false;
            while (!kotlinx.coroutines.scheduling.CoroutineScheduler.this.isTerminated() && this.state != kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED) {
                kotlinx.coroutines.scheduling.Task task = findTask(this.mayHaveLocalTasks);
                if (task != null) {
                    rescanned = false;
                    this.minDelayUntilStealableTaskNs = 0L;
                    executeTask(task);
                } else {
                    this.mayHaveLocalTasks = false;
                    if (this.minDelayUntilStealableTaskNs != 0) {
                        if (!rescanned) {
                            rescanned = true;
                        } else {
                            rescanned = false;
                            tryReleaseCpu(kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.PARKING);
                            java.lang.Thread.interrupted();
                            java.util.concurrent.locks.LockSupport.parkNanos(this.minDelayUntilStealableTaskNs);
                            this.minDelayUntilStealableTaskNs = 0L;
                        }
                    } else {
                        tryPark();
                    }
                }
            }
            tryReleaseCpu(kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED);
        }

        public final long runSingleTask() {
            kotlinx.coroutines.scheduling.Task task;
            kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState stateSnapshot = this.state;
            boolean isCpuThread = this.state == kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.CPU_ACQUIRED;
            if (isCpuThread) {
                task = findCpuTask();
            } else {
                task = findBlockingTask();
            }
            if (task == null) {
                if (this.minDelayUntilStealableTaskNs == 0) {
                    return -1L;
                }
                return this.minDelayUntilStealableTaskNs;
            }
            kotlinx.coroutines.scheduling.CoroutineScheduler.this.runSafely(task);
            if (!isCpuThread) {
                kotlinx.coroutines.scheduling.CoroutineScheduler this_$iv = kotlinx.coroutines.scheduling.CoroutineScheduler.this;
                this_$iv.controlState.addAndGet(kotlinx.coroutines.scheduling.CoroutineScheduler.PARKED_VERSION_MASK);
            }
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if (!(this.state == stateSnapshot)) {
                    throw new java.lang.AssertionError();
                }
            }
            return 0L;
        }

        public final boolean isIo() {
            return this.state == kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.BLOCKING;
        }

        private final void tryPark() {
            if (!inStack()) {
                kotlinx.coroutines.scheduling.CoroutineScheduler.this.parkedWorkersStackPush(this);
                return;
            }
            this.workerCtl.setValue(-1);
            while (inStack() && this.workerCtl.getValue() == -1 && !kotlinx.coroutines.scheduling.CoroutineScheduler.this.isTerminated() && this.state != kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED) {
                tryReleaseCpu(kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.PARKING);
                java.lang.Thread.interrupted();
                park();
            }
        }

        private final boolean inStack() {
            return this.nextParkedWorker != kotlinx.coroutines.scheduling.CoroutineScheduler.NOT_IN_STACK;
        }

        private final void executeTask(kotlinx.coroutines.scheduling.Task task) {
            int taskMode = task.taskContext.getTaskMode();
            idleReset(taskMode);
            beforeTask(taskMode);
            kotlinx.coroutines.scheduling.CoroutineScheduler.this.runSafely(task);
            afterTask(taskMode);
        }

        private final void beforeTask(int taskMode) {
            if (taskMode != 0 && tryReleaseCpu(kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.BLOCKING)) {
                kotlinx.coroutines.scheduling.CoroutineScheduler.this.signalCpuWork();
            }
        }

        private final void afterTask(int taskMode) {
            if (taskMode == 0) {
                return;
            }
            kotlinx.coroutines.scheduling.CoroutineScheduler this_$iv = kotlinx.coroutines.scheduling.CoroutineScheduler.this;
            this_$iv.controlState.addAndGet(kotlinx.coroutines.scheduling.CoroutineScheduler.PARKED_VERSION_MASK);
            kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState currentState = this.state;
            if (currentState != kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED) {
                if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(currentState == kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.BLOCKING)) {
                        throw new java.lang.AssertionError();
                    }
                }
                this.state = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.DORMANT;
            }
        }

        public final int nextInt(int upperBound) {
            int r = this.rngState;
            int r2 = r ^ (r << 13);
            int r3 = r2 ^ (r2 >> 17);
            int r4 = r3 ^ (r3 << 5);
            this.rngState = r4;
            int mask = upperBound - 1;
            if ((mask & upperBound) == 0) {
                return r4 & mask;
            }
            return (Integer.MAX_VALUE & r4) % upperBound;
        }

        private final void park() {
            if (this.terminationDeadline == 0) {
                this.terminationDeadline = java.lang.System.nanoTime() + kotlinx.coroutines.scheduling.CoroutineScheduler.this.idleWorkerKeepAliveNs;
            }
            java.util.concurrent.locks.LockSupport.parkNanos(kotlinx.coroutines.scheduling.CoroutineScheduler.this.idleWorkerKeepAliveNs);
            if (java.lang.System.nanoTime() - this.terminationDeadline >= 0) {
                this.terminationDeadline = 0L;
                tryTerminateWorker();
            }
        }

        private final void tryTerminateWorker() {
            java.lang.Object lock$iv = kotlinx.coroutines.scheduling.CoroutineScheduler.this.workers;
            kotlinx.coroutines.scheduling.CoroutineScheduler this_$iv = kotlinx.coroutines.scheduling.CoroutineScheduler.this;
            synchronized (lock$iv) {
                if (this_$iv.isTerminated()) {
                    return;
                }
                int value = (int) (this_$iv.controlState.getValue() & 2097151);
                int $i$f$getCreatedWorkers = this_$iv.corePoolSize;
                if (value <= $i$f$getCreatedWorkers) {
                    return;
                }
                if (this.workerCtl.compareAndSet(-1, 1)) {
                    int oldIndex = this.indexInArray;
                    setIndexInArray(0);
                    this_$iv.parkedWorkersStackTopUpdate(this, oldIndex, 0);
                    long state$iv$iv = this_$iv.controlState.getAndDecrement();
                    int lastIndex = (int) (2097151 & state$iv$iv);
                    if (lastIndex != oldIndex) {
                        kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker = this_$iv.workers.get(lastIndex);
                        kotlin.jvm.internal.Intrinsics.checkNotNull(worker);
                        kotlinx.coroutines.scheduling.CoroutineScheduler.Worker lastWorker = worker;
                        this_$iv.workers.setSynchronized(oldIndex, lastWorker);
                        lastWorker.setIndexInArray(oldIndex);
                        this_$iv.parkedWorkersStackTopUpdate(lastWorker, lastIndex, oldIndex);
                    }
                    this_$iv.workers.setSynchronized(lastIndex, null);
                    kotlin.Unit unit = kotlin.Unit.INSTANCE;
                    this.state = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED;
                }
            }
        }

        private final void idleReset(int mode) {
            this.terminationDeadline = 0L;
            if (this.state == kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.PARKING) {
                if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(mode == 1)) {
                        throw new java.lang.AssertionError();
                    }
                }
                this.state = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.BLOCKING;
            }
        }

        public final kotlinx.coroutines.scheduling.Task findTask(boolean mayHaveLocalTasks) {
            return tryAcquireCpuPermit() ? findAnyTask(mayHaveLocalTasks) : findBlockingTask();
        }

        private final kotlinx.coroutines.scheduling.Task findBlockingTask() {
            kotlinx.coroutines.scheduling.Task taskPollBlocking = this.localQueue.pollBlocking();
            if (taskPollBlocking != null) {
                return taskPollBlocking;
            }
            kotlinx.coroutines.scheduling.Task taskRemoveFirstOrNull = kotlinx.coroutines.scheduling.CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            if (taskRemoveFirstOrNull != null) {
                return taskRemoveFirstOrNull;
            }
            return trySteal(1);
        }

        private final kotlinx.coroutines.scheduling.Task findCpuTask() {
            kotlinx.coroutines.scheduling.Task taskPollCpu = this.localQueue.pollCpu();
            if (taskPollCpu != null) {
                return taskPollCpu;
            }
            kotlinx.coroutines.scheduling.Task taskRemoveFirstOrNull = kotlinx.coroutines.scheduling.CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            if (taskRemoveFirstOrNull != null) {
                return taskRemoveFirstOrNull;
            }
            return trySteal(2);
        }

        private final kotlinx.coroutines.scheduling.Task findAnyTask(boolean scanLocalQueue) {
            kotlinx.coroutines.scheduling.Task it;
            kotlinx.coroutines.scheduling.Task it2;
            if (scanLocalQueue) {
                boolean globalFirst = nextInt(kotlinx.coroutines.scheduling.CoroutineScheduler.this.corePoolSize * 2) == 0;
                if (globalFirst && (it2 = pollGlobalQueues()) != null) {
                    return it2;
                }
                kotlinx.coroutines.scheduling.Task it3 = this.localQueue.poll();
                if (it3 != null) {
                    return it3;
                }
                if (!globalFirst && (it = pollGlobalQueues()) != null) {
                    return it;
                }
            } else {
                kotlinx.coroutines.scheduling.Task it4 = pollGlobalQueues();
                if (it4 != null) {
                    return it4;
                }
            }
            return trySteal(3);
        }

        private final kotlinx.coroutines.scheduling.Task pollGlobalQueues() {
            if (nextInt(2) == 0) {
                kotlinx.coroutines.scheduling.Task it = kotlinx.coroutines.scheduling.CoroutineScheduler.this.globalCpuQueue.removeFirstOrNull();
                if (it != null) {
                    return it;
                }
                return kotlinx.coroutines.scheduling.CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            }
            kotlinx.coroutines.scheduling.Task it2 = kotlinx.coroutines.scheduling.CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            if (it2 != null) {
                return it2;
            }
            return kotlinx.coroutines.scheduling.CoroutineScheduler.this.globalCpuQueue.removeFirstOrNull();
        }

        private final kotlinx.coroutines.scheduling.Task trySteal(int stealingMode) {
            kotlinx.coroutines.scheduling.CoroutineScheduler this_$iv = kotlinx.coroutines.scheduling.CoroutineScheduler.this;
            int created = (int) (this_$iv.controlState.getValue() & 2097151);
            if (created < 2) {
                return null;
            }
            int currentIndex = nextInt(created);
            long minDelay = Long.MAX_VALUE;
            kotlinx.coroutines.scheduling.CoroutineScheduler coroutineScheduler = kotlinx.coroutines.scheduling.CoroutineScheduler.this;
            int i = 0;
            while (true) {
                if (i >= created) {
                    this.minDelayUntilStealableTaskNs = minDelay != Long.MAX_VALUE ? minDelay : 0L;
                    return null;
                }
                currentIndex++;
                if (currentIndex > created) {
                    currentIndex = 1;
                }
                kotlinx.coroutines.scheduling.CoroutineScheduler.Worker worker = coroutineScheduler.workers.get(currentIndex);
                if (worker != null && worker != this) {
                    long stealResult = worker.localQueue.trySteal(stealingMode, this.stolenTask);
                    if (stealResult == -1) {
                        kotlinx.coroutines.scheduling.Task result = this.stolenTask.element;
                        this.stolenTask.element = null;
                        return result;
                    }
                    if (stealResult > 0) {
                        minDelay = java.lang.Math.min(minDelay, stealResult);
                    }
                }
                i++;
            }
        }
    }
}
