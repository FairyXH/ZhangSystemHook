package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: SafeCollector.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u00032\u00020\u0004B\u001b\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ'\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u00072\b\u0010\u0017\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0018\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010\u001bJ%\u0010\u001a\u001a\u0004\u0018\u00010\u001c2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0018\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u001eJ\u001a\u0010\u001f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020!2\b\u0010\u0018\u001a\u0004\u0018\u00010\u001cH\u0002J\n\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\u001d\u0010$\u001a\u00020\u001c2\u000e\u0010%\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001c0&H\u0016¢\u0006\u0002\u0010'J\b\u0010(\u001a\u00020\u0010H\u0016R\u0016\u0010\t\u001a\u0004\u0018\u00010\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\u0006\u001a\u00020\u00078\u0000X\u0081\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u00020\r8\u0000X\u0081\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028\u0000X\u0081\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lkotlinx/coroutines/flow/internal/SafeCollector;", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlin/coroutines/jvm/internal/ContinuationImpl;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "collector", "collectContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/CoroutineContext;)V", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "collectContextSize", "", "completion", "Lkotlin/coroutines/Continuation;", "", "context", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "lastEmissionContext", "checkContext", "currentContext", "previousContext", "value", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "", "uCont", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;)Ljava/lang/Object;", "exceptionTransparencyViolated", "exception", "Lkotlinx/coroutines/flow/internal/DownstreamExceptionContext;", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "invokeSuspend", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)Ljava/lang/Object;", "releaseIntercepted", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SafeCollector<T> extends kotlin.coroutines.jvm.internal.ContinuationImpl implements kotlinx.coroutines.flow.FlowCollector<T>, kotlin.coroutines.jvm.internal.CoroutineStackFrame {
    public final kotlin.coroutines.CoroutineContext collectContext;
    public final int collectContextSize;
    public final kotlinx.coroutines.flow.FlowCollector<T> collector;
    private kotlin.coroutines.Continuation<? super kotlin.Unit> completion;
    private kotlin.coroutines.CoroutineContext lastEmissionContext;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public SafeCollector(kotlinx.coroutines.flow.FlowCollector<? super T> collector, kotlin.coroutines.CoroutineContext collectContext) {
        super(kotlinx.coroutines.flow.internal.NoOpContinuation.INSTANCE, kotlin.coroutines.EmptyCoroutineContext.INSTANCE);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collector, "collector");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collectContext, "collectContext");
        this.collector = collector;
        this.collectContext = collectContext;
        this.collectContextSize = ((java.lang.Number) this.collectContext.fold(0, new kotlin.jvm.functions.Function2<java.lang.Integer, kotlin.coroutines.CoroutineContext.Element, java.lang.Integer>() { // from class: kotlinx.coroutines.flow.internal.SafeCollector$collectContextSize$1
            public final java.lang.Integer invoke(int count, kotlin.coroutines.CoroutineContext.Element element) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "<anonymous parameter 1>");
                return java.lang.Integer.valueOf(count + 1);
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ java.lang.Integer invoke(java.lang.Integer num, kotlin.coroutines.CoroutineContext.Element element) {
                return invoke(num.intValue(), element);
            }
        })).intValue();
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl, kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public kotlin.coroutines.jvm.internal.CoroutineStackFrame getCallerFrame() {
        kotlin.coroutines.Continuation<? super kotlin.Unit> continuation = this.completion;
        if (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame) {
            return (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation;
        }
        return null;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl, kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public java.lang.StackTraceElement getStackTraceElement() {
        return null;
    }

    @Override // kotlin.coroutines.jvm.internal.ContinuationImpl, kotlin.coroutines.Continuation
    public kotlin.coroutines.CoroutineContext getContext() {
        kotlin.coroutines.CoroutineContext coroutineContext = this.lastEmissionContext;
        return coroutineContext == null ? kotlin.coroutines.EmptyCoroutineContext.INSTANCE : coroutineContext;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public java.lang.Object invokeSuspend(java.lang.Object result) {
        java.lang.Throwable it = kotlin.Result.m11310exceptionOrNullimpl(result);
        if (it != null) {
            this.lastEmissionContext = new kotlinx.coroutines.flow.internal.DownstreamExceptionContext(it, getContext());
        }
        kotlin.coroutines.Continuation<? super kotlin.Unit> continuation = this.completion;
        if (continuation != null) {
            continuation.resumeWith(result);
        }
        return kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }

    @Override // kotlin.coroutines.jvm.internal.ContinuationImpl, kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public void releaseIntercepted() {
        super.releaseIntercepted();
    }

    @Override // kotlinx.coroutines.flow.FlowCollector
    public java.lang.Object emit(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        try {
            java.lang.Object objEmit = emit(continuation, t);
            if (objEmit == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return objEmit == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEmit : kotlin.Unit.INSTANCE;
        } catch (java.lang.Throwable e) {
            this.lastEmissionContext = new kotlinx.coroutines.flow.internal.DownstreamExceptionContext(e, continuation.getContext());
            throw e;
        }
    }

    private final java.lang.Object emit(kotlin.coroutines.Continuation<? super kotlin.Unit> uCont, T value) {
        kotlin.coroutines.CoroutineContext currentContext = uCont.getContext();
        kotlinx.coroutines.JobKt.ensureActive(currentContext);
        kotlin.coroutines.CoroutineContext previousContext = this.lastEmissionContext;
        if (previousContext != currentContext) {
            checkContext(currentContext, previousContext, value);
            this.lastEmissionContext = currentContext;
        }
        this.completion = uCont;
        kotlin.jvm.functions.Function3 function3 = kotlinx.coroutines.flow.internal.SafeCollectorKt.emitFun;
        kotlinx.coroutines.flow.FlowCollector<T> flowCollector = this.collector;
        kotlin.jvm.internal.Intrinsics.checkNotNull(flowCollector, "null cannot be cast to non-null type kotlinx.coroutines.flow.FlowCollector<kotlin.Any?>");
        kotlin.jvm.internal.Intrinsics.checkNotNull(this, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Unit>");
        java.lang.Object result = function3.invoke(flowCollector, value, this);
        if (!kotlin.jvm.internal.Intrinsics.areEqual(result, kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED())) {
            this.completion = null;
        }
        return result;
    }

    private final void checkContext(kotlin.coroutines.CoroutineContext currentContext, kotlin.coroutines.CoroutineContext previousContext, T value) {
        if (previousContext instanceof kotlinx.coroutines.flow.internal.DownstreamExceptionContext) {
            exceptionTransparencyViolated((kotlinx.coroutines.flow.internal.DownstreamExceptionContext) previousContext, value);
        }
        kotlinx.coroutines.flow.internal.SafeCollector_commonKt.checkContext(this, currentContext);
    }

    private final void exceptionTransparencyViolated(kotlinx.coroutines.flow.internal.DownstreamExceptionContext exception, java.lang.Object value) {
        throw new java.lang.IllegalStateException(kotlin.text.StringsKt.trimIndent("\n            Flow exception transparency is violated:\n                Previous 'emit' call has thrown exception " + exception.e + ", but then emission attempt of value '" + value + "' has been detected.\n                Emissions from 'catch' blocks are prohibited in order to avoid unspecified behaviour, 'Flow.catch' operator can be used instead.\n                For a more detailed explanation, please refer to Flow documentation.\n            ").toString());
    }
}
