package kotlinx.coroutines;

/* JADX INFO: compiled from: CompletionState.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a1\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0000¢\u0006\u0002\u0010\u0007\u001aF\u0010\b\u001a\u0004\u0018\u00010\u0004\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012%\b\u0002\u0010\t\u001a\u001f\u0012\u0013\u0012\u00110\u000b¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\u000f\u0018\u00010\nH\u0000¢\u0006\u0002\u0010\u0010\u001a+\u0010\b\u001a\u0004\u0018\u00010\u0004\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\n\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u0012H\u0000¢\u0006\u0002\u0010\u0013¨\u0006\u0014"}, d2 = {"recoverResult", "Lkotlin/Result;", "T", "state", "", "uCont", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toState", "onCancellation", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "caller", "Lkotlinx/coroutines/CancellableContinuation;", "(Ljava/lang/Object;Lkotlinx/coroutines/CancellableContinuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CompletionStateKt {
    public static /* synthetic */ java.lang.Object toState$default(java.lang.Object obj, kotlin.jvm.functions.Function1 function1, int i, java.lang.Object obj2) {
        if ((i & 1) != 0) {
            function1 = null;
        }
        return toState(obj, (kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit>) function1);
    }

    public static final <T> java.lang.Object toState(java.lang.Object $this$toState, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> function1) {
        java.lang.Throwable it = kotlin.Result.m11310exceptionOrNullimpl($this$toState);
        if (it == null) {
            return function1 != null ? new kotlinx.coroutines.CompletedWithCancellation($this$toState, function1) : $this$toState;
        }
        return new kotlinx.coroutines.CompletedExceptionally(it, false, 2, null);
    }

    public static final <T> java.lang.Object toState(java.lang.Object $this$toState, kotlinx.coroutines.CancellableContinuation<?> caller) {
        java.lang.Throwable thRecoverFromStackFrame;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(caller, "caller");
        java.lang.Throwable it = kotlin.Result.m11310exceptionOrNullimpl($this$toState);
        if (it == null) {
            return $this$toState;
        }
        if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (caller instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
            thRecoverFromStackFrame = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(it, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) caller);
        } else {
            thRecoverFromStackFrame = it;
        }
        int $i$f$recoverStackTrace = 2;
        return new kotlinx.coroutines.CompletedExceptionally(thRecoverFromStackFrame, false, $i$f$recoverStackTrace, null);
    }

    public static final <T> java.lang.Object recoverResult(java.lang.Object state, kotlin.coroutines.Continuation<? super T> uCont) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uCont, "uCont");
        if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            java.lang.Throwable exception$iv = ((kotlinx.coroutines.CompletedExceptionally) state).cause;
            if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (uCont instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                exception$iv = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(exception$iv, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) uCont);
            }
            return kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(exception$iv));
        }
        kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
        return kotlin.Result.m11307constructorimpl(state);
    }
}
