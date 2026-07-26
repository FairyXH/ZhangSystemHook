package kotlinx.coroutines;

/* JADX INFO: compiled from: DefaultExecutor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a\b\u0010\u0006\u001a\u00020\u0001H\u0002\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"DefaultDelay", "Lkotlinx/coroutines/Delay;", "getDefaultDelay", "()Lkotlinx/coroutines/Delay;", "defaultMainDelayOptIn", "", "initializeDefaultDelay", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class DefaultExecutorKt {
    private static final boolean defaultMainDelayOptIn = kotlinx.coroutines.internal.SystemPropsKt.systemProp("kotlinx.coroutines.main.delay", false);
    private static final kotlinx.coroutines.Delay DefaultDelay = initializeDefaultDelay();

    public static final kotlinx.coroutines.Delay getDefaultDelay() {
        return DefaultDelay;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final kotlinx.coroutines.Delay initializeDefaultDelay() {
        if (!defaultMainDelayOptIn) {
            return kotlinx.coroutines.DefaultExecutor.INSTANCE;
        }
        kotlinx.coroutines.MainCoroutineDispatcher main = kotlinx.coroutines.Dispatchers.getMain();
        return (kotlinx.coroutines.internal.MainDispatchersKt.isMissing(main) || !(main instanceof kotlinx.coroutines.Delay)) ? kotlinx.coroutines.DefaultExecutor.INSTANCE : (kotlinx.coroutines.Delay) main;
    }
}
