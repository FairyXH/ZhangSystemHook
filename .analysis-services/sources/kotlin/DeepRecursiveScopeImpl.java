package kotlin;

/* JADX INFO: compiled from: DeepRecursive.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00020\u0004BH\u00129\u0010\u0005\u001a5\b\u0001\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0003\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006¢\u0006\u0002\b\b\u0012\u0006\u0010\t\u001a\u00028\u0000¢\u0006\u0002\u0010\nJ\u0016\u0010\u0015\u001a\u00028\u00012\u0006\u0010\t\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010\u0016J`\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000429\u0010\u0018\u001a5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006¢\u0006\u0002\b\b2\u000e\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0002¢\u0006\u0002\u0010\u0019J\u001b\u0010\u001a\u001a\u00020\u001b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00010\u0013H\u0016¢\u0006\u0002\u0010\u001cJ\u000b\u0010\u001d\u001a\u00028\u0001¢\u0006\u0002\u0010\u001eJ2\u0010\u0015\u001a\u0002H\u001f\"\u0004\b\u0002\u0010 \"\u0004\b\u0003\u0010\u001f*\u000e\u0012\u0004\u0012\u0002H \u0012\u0004\u0012\u0002H\u001f0!2\u0006\u0010\t\u001a\u0002H H\u0096@¢\u0006\u0002\u0010\"R\u0018\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fRC\u0010\u0010\u001a5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006¢\u0006\u0002\b\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0011R\u001b\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0013X\u0082\u000eø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0014R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006#"}, d2 = {"Lkotlin/DeepRecursiveScopeImpl;", "T", "R", "Lkotlin/DeepRecursiveScope;", "Lkotlin/coroutines/Continuation;", "block", "Lkotlin/Function3;", "", "Lkotlin/ExtensionFunctionType;", "value", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;)V", "cont", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "function", "Lkotlin/jvm/functions/Function3;", "result", "Lkotlin/Result;", "Ljava/lang/Object;", "callRecursive", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "crossFunctionCompletion", "currentFunction", "(Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "resumeWith", "", "(Ljava/lang/Object;)V", "runCallLoop", "()Ljava/lang/Object;", "S", "U", "Lkotlin/DeepRecursiveFunction;", "(Lkotlin/DeepRecursiveFunction;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class DeepRecursiveScopeImpl<T, R> extends kotlin.DeepRecursiveScope<T, R> implements kotlin.coroutines.Continuation<R> {
    private kotlin.coroutines.Continuation<java.lang.Object> cont;
    private kotlin.jvm.functions.Function3<? super kotlin.DeepRecursiveScope<?, ?>, java.lang.Object, ? super kotlin.coroutines.Continuation<java.lang.Object>, ? extends java.lang.Object> function;
    private java.lang.Object result;
    private java.lang.Object value;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public DeepRecursiveScopeImpl(kotlin.jvm.functions.Function3<? super kotlin.DeepRecursiveScope<T, R>, ? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> block, T t) {
        super(null);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        this.function = block;
        this.value = t;
        kotlin.jvm.internal.Intrinsics.checkNotNull(this, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        this.cont = this;
        this.result = kotlin.DeepRecursiveKt.UNDEFINED_RESULT;
    }

    @Override // kotlin.coroutines.Continuation
    /* JADX INFO: renamed from: getContext */
    public kotlin.coroutines.CoroutineContext get$context() {
        return kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(java.lang.Object result) {
        this.cont = null;
        this.result = result;
    }

    @Override // kotlin.DeepRecursiveScope
    public java.lang.Object callRecursive(T t, kotlin.coroutines.Continuation<? super R> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        this.cont = continuation;
        this.value = t;
        java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (coroutine_suspended == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return coroutine_suspended;
    }

    @Override // kotlin.DeepRecursiveScope
    public <U, S> java.lang.Object callRecursive(kotlin.DeepRecursiveFunction<U, S> deepRecursiveFunction, U u, kotlin.coroutines.Continuation<? super S> continuation) {
        kotlin.jvm.functions.Function3<kotlin.DeepRecursiveScope<U, S>, U, kotlin.coroutines.Continuation<? super S>, java.lang.Object> block$kotlin_stdlib = deepRecursiveFunction.getBlock$kotlin_stdlib();
        kotlin.jvm.internal.Intrinsics.checkNotNull(block$kotlin_stdlib, "null cannot be cast to non-null type @[ExtensionFunctionType] kotlin.coroutines.SuspendFunction2<kotlin.DeepRecursiveScope<*, *>, kotlin.Any?, kotlin.Any?>{ kotlin.DeepRecursiveKt.DeepRecursiveFunctionBlock }");
        kotlin.DeepRecursiveScopeImpl<T, R> deepRecursiveScopeImpl = this;
        kotlin.jvm.functions.Function3<? super kotlin.DeepRecursiveScope<?, ?>, java.lang.Object, ? super kotlin.coroutines.Continuation<java.lang.Object>, ? extends java.lang.Object> function3 = deepRecursiveScopeImpl.function;
        if (block$kotlin_stdlib != function3) {
            deepRecursiveScopeImpl.function = block$kotlin_stdlib;
            kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            deepRecursiveScopeImpl.cont = deepRecursiveScopeImpl.crossFunctionCompletion(function3, continuation);
        } else {
            kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            deepRecursiveScopeImpl.cont = continuation;
        }
        deepRecursiveScopeImpl.value = u;
        java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (coroutine_suspended == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return coroutine_suspended;
    }

    private final kotlin.coroutines.Continuation<java.lang.Object> crossFunctionCompletion(final kotlin.jvm.functions.Function3<? super kotlin.DeepRecursiveScope<?, ?>, java.lang.Object, ? super kotlin.coroutines.Continuation<java.lang.Object>, ? extends java.lang.Object> currentFunction, final kotlin.coroutines.Continuation<java.lang.Object> cont) {
        final kotlin.coroutines.EmptyCoroutineContext emptyCoroutineContext = kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
        return new kotlin.coroutines.Continuation<java.lang.Object>() { // from class: kotlin.DeepRecursiveScopeImpl$crossFunctionCompletion$$inlined$Continuation$1
            @Override // kotlin.coroutines.Continuation
            /* JADX INFO: renamed from: getContext, reason: from getter */
            public kotlin.coroutines.CoroutineContext get$context() {
                return emptyCoroutineContext;
            }

            @Override // kotlin.coroutines.Continuation
            public void resumeWith(java.lang.Object result) {
                this.function = currentFunction;
                this.cont = cont;
                this.result = result;
            }
        };
    }

    public final R runCallLoop() throws java.lang.Throwable {
        while (true) {
            R r = (R) this.result;
            kotlin.coroutines.Continuation<java.lang.Object> continuation = this.cont;
            if (continuation != null) {
                if (!kotlin.Result.m11309equalsimpl0(kotlin.DeepRecursiveKt.UNDEFINED_RESULT, r)) {
                    this.result = kotlin.DeepRecursiveKt.UNDEFINED_RESULT;
                    continuation.resumeWith(r);
                } else {
                    try {
                        kotlin.jvm.functions.Function3<? super kotlin.DeepRecursiveScope<?, ?>, java.lang.Object, ? super kotlin.coroutines.Continuation<java.lang.Object>, ? extends java.lang.Object> function3 = this.function;
                        java.lang.Object obj = this.value;
                        java.lang.Object objWrapWithContinuationImpl = !(function3 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(function3, this, obj, continuation) : ((kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function3, 3)).invoke(this, obj, continuation);
                        if (objWrapWithContinuationImpl != kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                            continuation.resumeWith(kotlin.Result.m11307constructorimpl(objWrapWithContinuationImpl));
                        }
                    } catch (java.lang.Throwable th) {
                        kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                        continuation.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th)));
                    }
                }
            } else {
                kotlin.ResultKt.throwOnFailure(r);
                return r;
            }
        }
    }
}
