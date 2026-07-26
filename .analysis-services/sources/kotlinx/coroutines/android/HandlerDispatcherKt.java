package kotlinx.coroutines.android;

/* JADX INFO: compiled from: HandlerDispatcher.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000@\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u001a\u000e\u0010\b\u001a\u00020\u0001H\u0086@¢\u0006\u0002\u0010\t\u001a\u000e\u0010\n\u001a\u00020\u0001H\u0082@¢\u0006\u0002\u0010\t\u001a\u001e\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0002\u001a\u0016\u0010\u000f\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0002\u001a\u001d\u0010\u0010\u001a\u00020\u0003*\u00020\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0007¢\u0006\u0002\b\u0014\u001a\u0014\u0010\u0015\u001a\u00020\u0011*\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0001\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0004\u0010\u0005\"\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"MAX_DELAY", "", "Main", "Lkotlinx/coroutines/android/HandlerDispatcher;", "getMain$annotations", "()V", "choreographer", "Landroid/view/Choreographer;", "awaitFrame", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "awaitFrameSlowPath", "postFrameCallback", "", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "updateChoreographerAndPostFrameCallback", "asCoroutineDispatcher", "Landroid/os/Handler;", "name", "", "from", "asHandler", "Landroid/os/Looper;", "async", "", "external__kotlinx.coroutines__android_common__kotlinx_coroutines_android"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class HandlerDispatcherKt {
    private static final long MAX_DELAY = 4611686018427387903L;
    public static final kotlinx.coroutines.android.HandlerDispatcher Main;
    private static volatile android.view.Choreographer choreographer;

    public static final kotlinx.coroutines.android.HandlerDispatcher from(android.os.Handler handler) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "<this>");
        return from$default(handler, null, 1, null);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Use Dispatchers.Main instead")
    public static /* synthetic */ void getMain$annotations() {
    }

    public static /* synthetic */ kotlinx.coroutines.android.HandlerDispatcher from$default(android.os.Handler handler, java.lang.String str, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        return from(handler, str);
    }

    public static final kotlinx.coroutines.android.HandlerDispatcher from(android.os.Handler $this$asCoroutineDispatcher, java.lang.String name) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asCoroutineDispatcher, "<this>");
        return new kotlinx.coroutines.android.HandlerContext($this$asCoroutineDispatcher, name);
    }

    public static final android.os.Handler asHandler(android.os.Looper $this$asHandler, boolean async) throws java.lang.IllegalAccessException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asHandler, "<this>");
        if (!async) {
            return new android.os.Handler($this$asHandler);
        }
        java.lang.reflect.Method factoryMethod = android.os.Handler.class.getDeclaredMethod("createAsync", android.os.Looper.class);
        java.lang.Object objInvoke = factoryMethod.invoke(null, $this$asHandler);
        kotlin.jvm.internal.Intrinsics.checkNotNull(objInvoke, "null cannot be cast to non-null type android.os.Handler");
        return (android.os.Handler) objInvoke;
    }

    static {
        java.lang.Object objM11307constructorimpl;
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            android.os.Looper mainLooper = android.os.Looper.getMainLooper();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(mainLooper, "getMainLooper(...)");
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(new kotlinx.coroutines.android.HandlerContext(asHandler(mainLooper, true), null, 2, null));
        } catch (java.lang.Throwable th) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
        }
        Main = (kotlinx.coroutines.android.HandlerDispatcher) (kotlin.Result.m11313isFailureimpl(objM11307constructorimpl) ? null : objM11307constructorimpl);
    }

    public static final java.lang.Object awaitFrame(kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        android.view.Choreographer choreographer2 = choreographer;
        if (choreographer2 == null) {
            return awaitFrameSlowPath(continuation);
        }
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        kotlinx.coroutines.CancellableContinuationImpl cont = cancellable$iv;
        postFrameCallback(choreographer2, cont);
        java.lang.Object result = cancellable$iv.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final java.lang.Object awaitFrameSlowPath(kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        final kotlinx.coroutines.CancellableContinuationImpl cont = cancellable$iv;
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            updateChoreographerAndPostFrameCallback(cont);
        } else {
            kotlinx.coroutines.Dispatchers.getMain().mo12864dispatch(cont.getContext(), new java.lang.Runnable() { // from class: kotlinx.coroutines.android.HandlerDispatcherKt$awaitFrameSlowPath$lambda$3$$inlined$Runnable$1
                @Override // java.lang.Runnable
                public final void run() {
                    kotlinx.coroutines.android.HandlerDispatcherKt.updateChoreographerAndPostFrameCallback(cont);
                }
            });
        }
        java.lang.Object result = cancellable$iv.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void updateChoreographerAndPostFrameCallback(kotlinx.coroutines.CancellableContinuation<? super java.lang.Long> cancellableContinuation) {
        android.view.Choreographer it = choreographer;
        if (it == null) {
            it = android.view.Choreographer.getInstance();
            kotlin.jvm.internal.Intrinsics.checkNotNull(it);
            choreographer = it;
        }
        postFrameCallback(it, cancellableContinuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void postFrameCallback(android.view.Choreographer choreographer2, final kotlinx.coroutines.CancellableContinuation<? super java.lang.Long> cancellableContinuation) {
        choreographer2.postFrameCallback(new android.view.Choreographer.FrameCallback() { // from class: kotlinx.coroutines.android.HandlerDispatcherKt.postFrameCallback.1
            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long nanos) {
                cancellableContinuation.resumeUndispatched(kotlinx.coroutines.Dispatchers.getMain(), java.lang.Long.valueOf(nanos));
            }
        });
    }
}
