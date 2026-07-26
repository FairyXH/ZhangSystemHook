package kotlinx.coroutines.intrinsics;

/* JADX INFO: compiled from: Cancellable.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u001c\u0010\u0000\u001a\u00020\u00012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a#\u0010\u0006\u001a\u00020\u00012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0082\b\u001a\u001e\u0010\t\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u00032\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u0003H\u0000\u001a;\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u000b*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0003\u0012\u0006\u0012\u0004\u0018\u00010\r0\f2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0003H\u0007¢\u0006\u0002\u0010\u000e\u001av\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u000f\"\u0004\b\u0001\u0010\u000b*\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0003\u0012\u0006\u0012\u0004\u0018\u00010\r0\u00102\u0006\u0010\u0011\u001a\u0002H\u000f2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u00032%\b\u0002\u0010\u0012\u001a\u001f\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0004\u0012\u00020\u0001\u0018\u00010\fH\u0000¢\u0006\u0002\u0010\u0016¨\u0006\u0017"}, d2 = {"dispatcherFailure", "", "completion", "Lkotlin/coroutines/Continuation;", "e", "", "runSafely", "block", "Lkotlin/Function0;", "startCoroutineCancellable", "fatalCompletion", "T", "Lkotlin/Function1;", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)V", "R", "Lkotlin/Function2;", "receiver", "onCancellation", "Lkotlin/ParameterName;", "name", "cause", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;Lkotlin/jvm/functions/Function1;)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CancellableKt {
    public static final <T> void startCoroutineCancellable(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1, kotlin.coroutines.Continuation<? super T> completion) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        try {
            kotlin.coroutines.Continuation continuationIntercepted = kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(kotlin.coroutines.intrinsics.IntrinsicsKt.createCoroutineUnintercepted(function1, completion));
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            kotlinx.coroutines.internal.DispatchedContinuationKt.resumeCancellableWith$default(continuationIntercepted, kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE), null, 2, null);
        } catch (java.lang.Throwable e$iv) {
            dispatcherFailure(completion, e$iv);
        }
    }

    public static /* synthetic */ void startCoroutineCancellable$default(kotlin.jvm.functions.Function2 function2, java.lang.Object obj, kotlin.coroutines.Continuation continuation, kotlin.jvm.functions.Function1 function1, int i, java.lang.Object obj2) throws java.lang.Throwable {
        if ((i & 4) != 0) {
            function1 = null;
        }
        startCoroutineCancellable(function2, obj, continuation, function1);
    }

    public static final <R, T> void startCoroutineCancellable(kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, R r, kotlin.coroutines.Continuation<? super T> completion, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> function1) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        try {
            kotlin.coroutines.Continuation continuationIntercepted = kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(kotlin.coroutines.intrinsics.IntrinsicsKt.createCoroutineUnintercepted(function2, r, completion));
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            kotlinx.coroutines.internal.DispatchedContinuationKt.resumeCancellableWith(continuationIntercepted, kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE), function1);
        } catch (java.lang.Throwable e$iv) {
            dispatcherFailure(completion, e$iv);
        }
    }

    public static final void startCoroutineCancellable(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation, kotlin.coroutines.Continuation<?> fatalCompletion) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(fatalCompletion, "fatalCompletion");
        try {
            kotlin.coroutines.Continuation continuationIntercepted = kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation);
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            kotlinx.coroutines.internal.DispatchedContinuationKt.resumeCancellableWith$default(continuationIntercepted, kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE), null, 2, null);
        } catch (java.lang.Throwable e$iv) {
            dispatcherFailure(fatalCompletion, e$iv);
        }
    }

    private static final void runSafely(kotlin.coroutines.Continuation<?> continuation, kotlin.jvm.functions.Function0<kotlin.Unit> function0) throws java.lang.Throwable {
        try {
            function0.invoke();
        } catch (java.lang.Throwable e) {
            dispatcherFailure(continuation, e);
        }
    }

    private static final void dispatcherFailure(kotlin.coroutines.Continuation<?> continuation, java.lang.Throwable e) throws java.lang.Throwable {
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        continuation.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(e)));
        throw e;
    }
}
