package kotlinx.coroutines.selects;

/* JADX INFO: compiled from: SelectOld.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0000\u001a5\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001f\b\u0004\u0010\u0002\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0081H¢\u0006\u0002\u0010\u0007\u001a5\u0010\b\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001f\b\u0004\u0010\u0002\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0081H¢\u0006\u0002\u0010\u0007\u001a%\u0010\t\u001a\u00020\u0005\"\u0004\b\u0000\u0010\n*\b\u0012\u0004\u0012\u0002H\n0\u000b2\u0006\u0010\f\u001a\u0002H\nH\u0002¢\u0006\u0002\u0010\r\u001a\u0018\u0010\u000e\u001a\u00020\u0005*\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002¨\u0006\u0011"}, d2 = {"selectOld", "R", "builder", "Lkotlin/Function1;", "Lkotlinx/coroutines/selects/SelectBuilder;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "selectUnbiasedOld", "resumeUndispatched", "T", "Lkotlinx/coroutines/CancellableContinuation;", "result", "(Lkotlinx/coroutines/CancellableContinuation;Ljava/lang/Object;)V", "resumeUndispatchedWithException", "exception", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SelectOldKt {
    public static final <R> java.lang.Object selectOld(kotlin.jvm.functions.Function1<? super kotlinx.coroutines.selects.SelectBuilder<? super R>, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super R> continuation) {
        kotlinx.coroutines.selects.SelectBuilderImpl scope = new kotlinx.coroutines.selects.SelectBuilderImpl(continuation);
        try {
            function1.invoke(scope);
        } catch (java.lang.Throwable e) {
            scope.handleBuilderException(e);
        }
        java.lang.Object result = scope.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    private static final <R> java.lang.Object selectOld$$forInline(kotlin.jvm.functions.Function1<? super kotlinx.coroutines.selects.SelectBuilder<? super R>, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super R> continuation) {
        kotlin.jvm.internal.InlineMarker.mark(0);
        kotlinx.coroutines.selects.SelectBuilderImpl scope = new kotlinx.coroutines.selects.SelectBuilderImpl(continuation);
        try {
            function1.invoke(scope);
        } catch (java.lang.Throwable e) {
            scope.handleBuilderException(e);
        }
        java.lang.Object result = scope.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        kotlin.jvm.internal.InlineMarker.mark(1);
        return result;
    }

    public static final <R> java.lang.Object selectUnbiasedOld(kotlin.jvm.functions.Function1<? super kotlinx.coroutines.selects.SelectBuilder<? super R>, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super R> continuation) {
        kotlinx.coroutines.selects.UnbiasedSelectBuilderImpl scope = new kotlinx.coroutines.selects.UnbiasedSelectBuilderImpl(continuation);
        try {
            function1.invoke(scope);
        } catch (java.lang.Throwable e) {
            scope.handleBuilderException(e);
        }
        java.lang.Object objInitSelectResult = scope.initSelectResult();
        if (objInitSelectResult == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return objInitSelectResult;
    }

    private static final <R> java.lang.Object selectUnbiasedOld$$forInline(kotlin.jvm.functions.Function1<? super kotlinx.coroutines.selects.SelectBuilder<? super R>, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super R> continuation) {
        kotlin.jvm.internal.InlineMarker.mark(0);
        kotlinx.coroutines.selects.UnbiasedSelectBuilderImpl scope = new kotlinx.coroutines.selects.UnbiasedSelectBuilderImpl(continuation);
        try {
            function1.invoke(scope);
        } catch (java.lang.Throwable e) {
            scope.handleBuilderException(e);
        }
        java.lang.Object objInitSelectResult = scope.initSelectResult();
        if (objInitSelectResult == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        kotlin.jvm.internal.InlineMarker.mark(1);
        return objInitSelectResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> void resumeUndispatched(kotlinx.coroutines.CancellableContinuation<? super T> cancellableContinuation, T t) {
        kotlinx.coroutines.CoroutineDispatcher dispatcher = (kotlinx.coroutines.CoroutineDispatcher) cancellableContinuation.getContext().get(kotlinx.coroutines.CoroutineDispatcher.INSTANCE);
        if (dispatcher != null) {
            cancellableContinuation.resumeUndispatched(dispatcher, t);
        } else {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            cancellableContinuation.resumeWith(kotlin.Result.m11307constructorimpl(t));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void resumeUndispatchedWithException(kotlinx.coroutines.CancellableContinuation<?> cancellableContinuation, java.lang.Throwable exception) {
        kotlinx.coroutines.CoroutineDispatcher dispatcher = (kotlinx.coroutines.CoroutineDispatcher) cancellableContinuation.getContext().get(kotlinx.coroutines.CoroutineDispatcher.INSTANCE);
        if (dispatcher != null) {
            cancellableContinuation.resumeUndispatchedWithException(dispatcher, exception);
        } else {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            cancellableContinuation.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(exception)));
        }
    }
}
