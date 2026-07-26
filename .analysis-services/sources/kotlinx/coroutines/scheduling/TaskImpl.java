package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u00002\u00020\u0001B!\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0016R\u0014\u0010\u0002\u001a\u00060\u0003j\u0002`\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/scheduling/TaskImpl;", "Lkotlinx/coroutines/scheduling/Task;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "submissionTime", "", "taskContext", "Lkotlinx/coroutines/scheduling/TaskContext;", "(Ljava/lang/Runnable;JLkotlinx/coroutines/scheduling/TaskContext;)V", "run", "", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TaskImpl extends kotlinx.coroutines.scheduling.Task {
    public final java.lang.Runnable block;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TaskImpl(java.lang.Runnable block, long submissionTime, kotlinx.coroutines.scheduling.TaskContext taskContext) {
        super(submissionTime, taskContext);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(taskContext, "taskContext");
        this.block = block;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.block.run();
        } finally {
            this.taskContext.afterTask();
        }
    }

    public java.lang.String toString() {
        return "Task[" + kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this.block) + "@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this.block) + ", " + this.submissionTime + ", " + this.taskContext + "]";
    }
}
