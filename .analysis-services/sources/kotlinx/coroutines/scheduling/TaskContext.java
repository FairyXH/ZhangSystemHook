package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b`\u0018\u00002\u00020\u0001J\b\u0010\u0006\u001a\u00020\u0007H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/scheduling/TaskContext;", "", "taskMode", "", "getTaskMode", "()I", "afterTask", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface TaskContext {
    void afterTask();

    int getTaskMode();
}
