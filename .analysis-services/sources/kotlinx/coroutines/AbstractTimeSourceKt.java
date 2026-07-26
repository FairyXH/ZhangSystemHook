package kotlinx.coroutines;

/* JADX INFO: compiled from: AbstractTimeSource.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\t\u0010\u0006\u001a\u00020\u0007H\u0081\b\u001a\t\u0010\b\u001a\u00020\u0007H\u0081\b\u001a\u0019\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0007H\u0081\b\u001a\t\u0010\u000e\u001a\u00020\nH\u0081\b\u001a\t\u0010\u000f\u001a\u00020\nH\u0081\b\u001a\t\u0010\u0010\u001a\u00020\nH\u0081\b\u001a\u0011\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013H\u0081\b\u001a\t\u0010\u0014\u001a\u00020\nH\u0081\b\u001a\u0019\u0010\u0015\u001a\u00060\u0016j\u0002`\u00172\n\u0010\u0018\u001a\u00060\u0016j\u0002`\u0017H\u0081\b\"\u001c\u0010\u0000\u001a\u0004\u0018\u00010\u0001X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u0004\b\u0004\u0010\u0005¨\u0006\u0019"}, d2 = {"timeSource", "Lkotlinx/coroutines/AbstractTimeSource;", "getTimeSource", "()Lkotlinx/coroutines/AbstractTimeSource;", "setTimeSource", "(Lkotlinx/coroutines/AbstractTimeSource;)V", "currentTimeMillis", "", "nanoTime", "parkNanos", "", "blocker", "", "nanos", "registerTimeLoopThread", "trackTask", "unTrackTask", "unpark", "thread", "Ljava/lang/Thread;", "unregisterTimeLoopThread", "wrapTask", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "block", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class AbstractTimeSourceKt {
    private static kotlinx.coroutines.AbstractTimeSource timeSource;

    public static final kotlinx.coroutines.AbstractTimeSource getTimeSource() {
        return timeSource;
    }

    public static final void setTimeSource(kotlinx.coroutines.AbstractTimeSource abstractTimeSource) {
        timeSource = abstractTimeSource;
    }

    private static final long currentTimeMillis() {
        kotlinx.coroutines.AbstractTimeSource timeSource2 = getTimeSource();
        return timeSource2 != null ? timeSource2.currentTimeMillis() : java.lang.System.currentTimeMillis();
    }

    private static final long nanoTime() {
        kotlinx.coroutines.AbstractTimeSource timeSource2 = getTimeSource();
        return timeSource2 != null ? timeSource2.nanoTime() : java.lang.System.nanoTime();
    }

    private static final java.lang.Runnable wrapTask(java.lang.Runnable block) {
        java.lang.Runnable runnableWrapTask;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlinx.coroutines.AbstractTimeSource timeSource2 = getTimeSource();
        return (timeSource2 == null || (runnableWrapTask = timeSource2.wrapTask(block)) == null) ? block : runnableWrapTask;
    }

    private static final void trackTask() {
        kotlinx.coroutines.AbstractTimeSource timeSource2 = getTimeSource();
        if (timeSource2 != null) {
            timeSource2.trackTask();
        }
    }

    private static final void unTrackTask() {
        kotlinx.coroutines.AbstractTimeSource timeSource2 = getTimeSource();
        if (timeSource2 != null) {
            timeSource2.unTrackTask();
        }
    }

    private static final void registerTimeLoopThread() {
        kotlinx.coroutines.AbstractTimeSource timeSource2 = getTimeSource();
        if (timeSource2 != null) {
            timeSource2.registerTimeLoopThread();
        }
    }

    private static final void unregisterTimeLoopThread() {
        kotlinx.coroutines.AbstractTimeSource timeSource2 = getTimeSource();
        if (timeSource2 != null) {
            timeSource2.unregisterTimeLoopThread();
        }
    }

    private static final void parkNanos(java.lang.Object blocker, long nanos) {
        kotlin.Unit unit;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(blocker, "blocker");
        kotlinx.coroutines.AbstractTimeSource timeSource2 = getTimeSource();
        if (timeSource2 != null) {
            timeSource2.parkNanos(blocker, nanos);
            unit = kotlin.Unit.INSTANCE;
        } else {
            unit = null;
        }
        if (unit == null) {
            java.util.concurrent.locks.LockSupport.parkNanos(blocker, nanos);
        }
    }

    private static final void unpark(java.lang.Thread thread) {
        kotlin.Unit unit;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(thread, "thread");
        kotlinx.coroutines.AbstractTimeSource timeSource2 = getTimeSource();
        if (timeSource2 != null) {
            timeSource2.unpark(thread);
            unit = kotlin.Unit.INSTANCE;
        } else {
            unit = null;
        }
        if (unit == null) {
            java.util.concurrent.locks.LockSupport.unpark(thread);
        }
    }
}
