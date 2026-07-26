package kotlinx.coroutines.intrinsics;

/* JADX INFO: compiled from: Undispatched.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000@\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a9\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00042\u001a\u0010\u0005\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006H\u0082\b\u001a;\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0000¢\u0006\u0002\u0010\t\u001aO\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\n\"\u0004\b\u0001\u0010\u0002*\u001e\b\u0001\u0012\u0004\u0012\u0002H\n\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000b2\u0006\u0010\f\u001a\u0002H\n2\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0000¢\u0006\u0002\u0010\r\u001a;\u0010\u000e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0000¢\u0006\u0002\u0010\t\u001aV\u0010\u000f\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\b\u0012\u0004\u0012\u0002H\u00020\u00102\u0006\u0010\f\u001a\u0002H\n2'\u0010\u0005\u001a#\b\u0001\u0012\u0004\u0012\u0002H\n\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000b¢\u0006\u0002\b\u0011H\u0000¢\u0006\u0002\u0010\u0012\u001aV\u0010\u0013\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\b\u0012\u0004\u0012\u0002H\u00020\u00102\u0006\u0010\f\u001a\u0002H\n2'\u0010\u0005\u001a#\b\u0001\u0012\u0004\u0012\u0002H\n\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000b¢\u0006\u0002\b\u0011H\u0000¢\u0006\u0002\u0010\u0012\u001a?\u0010\u0014\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00102\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00170\u00062\u000e\u0010\u0018\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0019H\u0082\b¨\u0006\u001a"}, d2 = {"startDirect", "", "T", "completion", "Lkotlin/coroutines/Continuation;", "block", "Lkotlin/Function1;", "", "startCoroutineUndispatched", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)V", "R", "Lkotlin/Function2;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)V", "startCoroutineUnintercepted", "startUndispatchedOrReturn", "Lkotlinx/coroutines/internal/ScopeCoroutine;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/internal/ScopeCoroutine;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "startUndispatchedOrReturnIgnoreTimeout", "undispatchedResult", "shouldThrow", "", "", "startBlock", "Lkotlin/Function0;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class UndispatchedKt {
    public static final <T> void startCoroutineUnintercepted(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        kotlin.coroutines.Continuation actualCompletion$iv = kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(completion);
        try {
            java.lang.Object value$iv = !(function1 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(function1, actualCompletion$iv) : ((kotlin.jvm.functions.Function1) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(actualCompletion$iv);
            if (value$iv == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return;
            }
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            actualCompletion$iv.resumeWith(kotlin.Result.m11307constructorimpl(value$iv));
        } catch (java.lang.Throwable e$iv) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            actualCompletion$iv.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(e$iv)));
        }
    }

    public static final <T> void startCoroutineUndispatched(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        kotlin.coroutines.Continuation actualCompletion$iv = kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(completion);
        try {
            kotlin.coroutines.CoroutineContext context$iv = completion.getContext();
            java.lang.Object oldValue$iv = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(context$iv, null);
            try {
                java.lang.Object value$iv = !(function1 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(function1, actualCompletion$iv) : ((kotlin.jvm.functions.Function1) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(actualCompletion$iv);
                if (value$iv == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    return;
                }
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                actualCompletion$iv.resumeWith(kotlin.Result.m11307constructorimpl(value$iv));
            } finally {
                kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
            }
        } catch (java.lang.Throwable e$iv) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            actualCompletion$iv.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(e$iv)));
        }
    }

    public static final <R, T> void startCoroutineUndispatched(kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, R r, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        kotlin.coroutines.Continuation actualCompletion$iv = kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(completion);
        try {
            kotlin.coroutines.CoroutineContext context$iv = completion.getContext();
            java.lang.Object oldValue$iv = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(context$iv, null);
            try {
                java.lang.Object value$iv = !(function2 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(function2, r, actualCompletion$iv) : ((kotlin.jvm.functions.Function2) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(r, actualCompletion$iv);
                if (value$iv == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    return;
                }
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                actualCompletion$iv.resumeWith(kotlin.Result.m11307constructorimpl(value$iv));
            } finally {
                kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
            }
        } catch (java.lang.Throwable e$iv) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            actualCompletion$iv.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(e$iv)));
        }
    }

    private static final <T> void startDirect(kotlin.coroutines.Continuation<? super T> continuation, kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1) {
        kotlin.coroutines.Continuation actualCompletion = kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(continuation);
        try {
            java.lang.Object value = function1.invoke(actualCompletion);
            if (value != kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                actualCompletion.resumeWith(kotlin.Result.m11307constructorimpl(value));
            }
        } catch (java.lang.Throwable e) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            actualCompletion.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(e)));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1, types: [int] */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v3 */
    public static final <T, R> java.lang.Object startUndispatchedOrReturn(kotlinx.coroutines.internal.ScopeCoroutine<? super T> scopeCoroutine, R r, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> block) {
        java.lang.Object objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scopeCoroutine, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        ?? completedExceptionally = 2;
        try {
            completedExceptionally = !(block instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(block, r, scopeCoroutine) : ((kotlin.jvm.functions.Function2) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(block, 2)).invoke(r, scopeCoroutine);
        } catch (java.lang.Throwable th) {
            completedExceptionally = new kotlinx.coroutines.CompletedExceptionally(th, false, completedExceptionally, null);
        }
        ?? r2 = completedExceptionally;
        if (r2 != kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() && (objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = scopeCoroutine.makeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r2)) != kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            if (objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host instanceof kotlinx.coroutines.CompletedExceptionally) {
                java.lang.Throwable th2 = ((kotlinx.coroutines.CompletedExceptionally) objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).cause;
                java.lang.Throwable th3 = ((kotlinx.coroutines.CompletedExceptionally) objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).cause;
                kotlin.coroutines.Continuation<? super T> continuation = scopeCoroutine.uCont;
                if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                    throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(th3, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation);
                }
                throw th3;
            }
            return kotlinx.coroutines.JobSupportKt.unboxState(objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host);
        }
        return kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1, types: [int] */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r4v16 */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v6 */
    public static final <T, R> java.lang.Object startUndispatchedOrReturnIgnoreTimeout(kotlinx.coroutines.internal.ScopeCoroutine<? super T> scopeCoroutine, R r, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> block) throws java.lang.Throwable {
        java.lang.Object objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host;
        ?? UnboxState;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scopeCoroutine, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        ?? completedExceptionally = 2;
        try {
            completedExceptionally = !(block instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(block, r, scopeCoroutine) : ((kotlin.jvm.functions.Function2) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(block, 2)).invoke(r, scopeCoroutine);
        } catch (java.lang.Throwable th) {
            completedExceptionally = new kotlinx.coroutines.CompletedExceptionally(th, false, completedExceptionally, null);
        }
        ?? r2 = completedExceptionally;
        if (r2 != kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() && (objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = scopeCoroutine.makeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r2)) != kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            if (objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host instanceof kotlinx.coroutines.CompletedExceptionally) {
                java.lang.Throwable th2 = ((kotlinx.coroutines.CompletedExceptionally) objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).cause;
                if (((th2 instanceof kotlinx.coroutines.TimeoutCancellationException) && ((kotlinx.coroutines.TimeoutCancellationException) th2).coroutine == scopeCoroutine) ? false : true) {
                    java.lang.Throwable th3 = ((kotlinx.coroutines.CompletedExceptionally) objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).cause;
                    kotlin.coroutines.Continuation<? super T> continuation = scopeCoroutine.uCont;
                    if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                        throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(th3, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation);
                    }
                    throw th3;
                }
                if (r2 instanceof kotlinx.coroutines.CompletedExceptionally) {
                    java.lang.Throwable th4 = ((kotlinx.coroutines.CompletedExceptionally) r2).cause;
                    kotlin.coroutines.Continuation<? super T> continuation2 = scopeCoroutine.uCont;
                    if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation2 instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                        throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(th4, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation2);
                    }
                    throw th4;
                }
                UnboxState = r2;
            } else {
                UnboxState = kotlinx.coroutines.JobSupportKt.unboxState(objMakeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host);
            }
            return UnboxState;
        }
        return kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }

    private static final <T> java.lang.Object undispatchedResult(kotlinx.coroutines.internal.ScopeCoroutine<? super T> scopeCoroutine, kotlin.jvm.functions.Function1<? super java.lang.Throwable, java.lang.Boolean> function1, kotlin.jvm.functions.Function0<? extends java.lang.Object> function0) throws java.lang.Throwable {
        java.lang.Object result;
        java.lang.Object state;
        try {
            result = function0.invoke();
        } catch (java.lang.Throwable e) {
            result = new kotlinx.coroutines.CompletedExceptionally(e, false, 2, null);
        }
        if (result != kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() && (state = scopeCoroutine.makeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(result)) != kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
                if (!function1.invoke(((kotlinx.coroutines.CompletedExceptionally) state).cause).booleanValue()) {
                    if (!(result instanceof kotlinx.coroutines.CompletedExceptionally)) {
                        return result;
                    }
                    java.lang.Throwable exception$iv = ((kotlinx.coroutines.CompletedExceptionally) result).cause;
                    kotlin.coroutines.Continuation<? super T> continuation = scopeCoroutine.uCont;
                    if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                        throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(exception$iv, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation);
                    }
                    throw exception$iv;
                }
                java.lang.Throwable exception$iv2 = ((kotlinx.coroutines.CompletedExceptionally) state).cause;
                kotlin.coroutines.Continuation<? super T> continuation2 = scopeCoroutine.uCont;
                if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation2 instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                    throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(exception$iv2, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation2);
                }
                throw exception$iv2;
            }
            return kotlinx.coroutines.JobSupportKt.unboxState(state);
        }
        return kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }
}
