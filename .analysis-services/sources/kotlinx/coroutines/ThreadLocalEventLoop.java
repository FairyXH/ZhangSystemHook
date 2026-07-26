package kotlinx.coroutines;

/* JADX INFO: compiled from: EventLoop.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000f\u0010\n\u001a\u0004\u0018\u00010\u0004H\u0000¢\u0006\u0002\b\u000bJ\r\u0010\f\u001a\u00020\rH\u0000¢\u0006\u0002\b\u000eJ\u0015\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u0010R\u0014\u0010\u0003\u001a\u00020\u00048@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\"\u0010\u0007\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\u00040\bj\n\u0012\u0006\u0012\u0004\u0018\u00010\u0004`\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/ThreadLocalEventLoop;", "", "()V", "eventLoop", "Lkotlinx/coroutines/EventLoop;", "getEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Lkotlinx/coroutines/EventLoop;", "ref", "Ljava/lang/ThreadLocal;", "Lkotlinx/coroutines/internal/CommonThreadLocal;", "currentOrNull", "currentOrNull$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "resetEventLoop", "", "resetEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "setEventLoop", "setEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ThreadLocalEventLoop {
    public static final kotlinx.coroutines.ThreadLocalEventLoop INSTANCE = new kotlinx.coroutines.ThreadLocalEventLoop();
    private static final java.lang.ThreadLocal<kotlinx.coroutines.EventLoop> ref = kotlinx.coroutines.internal.ThreadLocalKt.commonThreadLocal(new kotlinx.coroutines.internal.Symbol("ThreadLocalEventLoop"));

    private ThreadLocalEventLoop() {
    }

    public final kotlinx.coroutines.EventLoop getEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        kotlinx.coroutines.EventLoop eventLoop = ref.get();
        if (eventLoop != null) {
            return eventLoop;
        }
        kotlinx.coroutines.EventLoop it = kotlinx.coroutines.EventLoopKt.createEventLoop();
        ref.set(it);
        return it;
    }

    public final kotlinx.coroutines.EventLoop currentOrNull$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return ref.get();
    }

    public final void resetEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        ref.set(null);
    }

    public final void setEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.EventLoop eventLoop) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(eventLoop, "eventLoop");
        ref.set(eventLoop);
    }
}
