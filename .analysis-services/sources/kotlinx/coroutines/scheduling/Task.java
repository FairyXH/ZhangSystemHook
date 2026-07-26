package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b!\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0007\b\u0010¢\u0006\u0002\u0010\u0003B\u0017\b\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bR\u0015\u0010\t\u001a\u00020\n8À\u0002X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0012\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u00020\u00078\u0000@\u0000X\u0081\u000e¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/scheduling/Task;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "()V", "submissionTime", "", "taskContext", "Lkotlinx/coroutines/scheduling/TaskContext;", "(JLkotlinx/coroutines/scheduling/TaskContext;)V", com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, "", "getMode$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()I", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class Task implements java.lang.Runnable {
    public long submissionTime;
    public kotlinx.coroutines.scheduling.TaskContext taskContext;

    public Task(long submissionTime, kotlinx.coroutines.scheduling.TaskContext taskContext) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(taskContext, "taskContext");
        this.submissionTime = submissionTime;
        this.taskContext = taskContext;
    }

    public Task() {
        this(0L, kotlinx.coroutines.scheduling.TasksKt.NonBlockingContext);
    }

    public final int getMode$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return this.taskContext.getTaskMode();
    }
}
