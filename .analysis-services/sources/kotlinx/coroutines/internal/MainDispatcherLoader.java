package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: MainDispatchers.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/internal/MainDispatcherLoader;", "", "()V", "FAST_SERVICE_LOADER_ENABLED", "", "dispatcher", "Lkotlinx/coroutines/MainCoroutineDispatcher;", "loadMainDispatcher", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MainDispatcherLoader {
    public static final kotlinx.coroutines.internal.MainDispatcherLoader INSTANCE = new kotlinx.coroutines.internal.MainDispatcherLoader();
    private static final boolean FAST_SERVICE_LOADER_ENABLED = kotlinx.coroutines.internal.SystemPropsKt.systemProp("kotlinx.coroutines.fast.service.loader", true);
    public static final kotlinx.coroutines.MainCoroutineDispatcher dispatcher = INSTANCE.loadMainDispatcher();

    private MainDispatcherLoader() {
    }

    private final kotlinx.coroutines.MainCoroutineDispatcher loadMainDispatcher() {
        java.util.List<kotlinx.coroutines.internal.MainDispatcherFactory> list;
        java.lang.Object maxElem$iv;
        kotlinx.coroutines.MainCoroutineDispatcher mainCoroutineDispatcherTryCreateDispatcher;
        try {
            if (FAST_SERVICE_LOADER_ENABLED) {
                list = kotlinx.coroutines.internal.FastServiceLoader.INSTANCE.loadMainDispatcherFactory$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            } else {
                java.util.Iterator it = java.util.ServiceLoader.load(kotlinx.coroutines.internal.MainDispatcherFactory.class, kotlinx.coroutines.internal.MainDispatcherFactory.class.getClassLoader()).iterator();
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                list = kotlin.sequences.SequencesKt.toList(kotlin.sequences.SequencesKt.asSequence(it));
            }
            java.lang.Iterable $this$maxByOrNull$iv = list;
            java.util.Iterator iterator$iv = $this$maxByOrNull$iv.iterator();
            if (iterator$iv.hasNext()) {
                maxElem$iv = iterator$iv.next();
                if (iterator$iv.hasNext()) {
                    kotlinx.coroutines.internal.MainDispatcherFactory it2 = (kotlinx.coroutines.internal.MainDispatcherFactory) maxElem$iv;
                    int maxValue$iv = it2.getLoadPriority();
                    do {
                        java.lang.Object e$iv = iterator$iv.next();
                        kotlinx.coroutines.internal.MainDispatcherFactory it3 = (kotlinx.coroutines.internal.MainDispatcherFactory) e$iv;
                        int v$iv = it3.getLoadPriority();
                        if (maxValue$iv < v$iv) {
                            maxElem$iv = e$iv;
                            maxValue$iv = v$iv;
                        }
                    } while (iterator$iv.hasNext());
                }
            } else {
                maxElem$iv = null;
            }
            kotlinx.coroutines.internal.MainDispatcherFactory mainDispatcherFactory = (kotlinx.coroutines.internal.MainDispatcherFactory) maxElem$iv;
            return (mainDispatcherFactory == null || (mainCoroutineDispatcherTryCreateDispatcher = kotlinx.coroutines.internal.MainDispatchersKt.tryCreateDispatcher(mainDispatcherFactory, list)) == null) ? kotlinx.coroutines.internal.MainDispatchersKt.createMissingDispatcher$default(null, null, 3, null) : mainCoroutineDispatcherTryCreateDispatcher;
        } catch (java.lang.Throwable e) {
            return kotlinx.coroutines.internal.MainDispatchersKt.createMissingDispatcher$default(e, null, 2, null);
        }
    }
}
