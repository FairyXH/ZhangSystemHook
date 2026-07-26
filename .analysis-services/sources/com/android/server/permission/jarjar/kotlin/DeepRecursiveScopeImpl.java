package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: DeepRecursive.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00020\u0004BH\u00129\u0010\u0005\u001a5\b\u0001\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0003\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006¢\u0006\u0002\b\b\u0012\u0006\u0010\t\u001a\u00028\u0000¢\u0006\u0002\u0010\nJ\u0016\u0010\u0015\u001a\u00028\u00012\u0006\u0010\t\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010\u0016J`\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000429\u0010\u0018\u001a5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006¢\u0006\u0002\b\b2\u000e\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0002¢\u0006\u0002\u0010\u0019J\u001b\u0010\u001a\u001a\u00020\u001b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00010\u0013H\u0016¢\u0006\u0002\u0010\u001cJ\u000b\u0010\u001d\u001a\u00028\u0001¢\u0006\u0002\u0010\u001eJ2\u0010\u0015\u001a\u0002H\u001f\"\u0004\b\u0002\u0010 \"\u0004\b\u0003\u0010\u001f*\u000e\u0012\u0004\u0012\u0002H \u0012\u0004\u0012\u0002H\u001f0!2\u0006\u0010\t\u001a\u0002H H\u0096@¢\u0006\u0002\u0010\"R\u0018\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fRC\u0010\u0010\u001a5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006¢\u0006\u0002\b\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0011R\u001b\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0013X\u0082\u000eø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0014R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006#"}, d2 = {"Lkotlin/DeepRecursiveScopeImpl;", "T", "R", "Lkotlin/DeepRecursiveScope;", "Lkotlin/coroutines/Continuation;", "block", "Lkotlin/Function3;", "", "Lkotlin/ExtensionFunctionType;", "value", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;)V", "cont", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "function", "Lkotlin/jvm/functions/Function3;", "result", "Lkotlin/Result;", "Ljava/lang/Object;", "callRecursive", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "crossFunctionCompletion", "currentFunction", "(Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "resumeWith", "", "(Ljava/lang/Object;)V", "runCallLoop", "()Ljava/lang/Object;", "S", "U", "Lkotlin/DeepRecursiveFunction;", "(Lkotlin/DeepRecursiveFunction;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class DeepRecursiveScopeImpl<T, R> extends com.android.server.permission.jarjar.kotlin.DeepRecursiveScope<T, R> implements com.android.server.permission.jarjar.kotlin.coroutines.Continuation<R> {
    private com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> cont;
    private com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super com.android.server.permission.jarjar.kotlin.DeepRecursiveScope<?, ?>, java.lang.Object, ? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object>, ? extends java.lang.Object> function;
    private java.lang.Object result;
    private java.lang.Object value;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public DeepRecursiveScopeImpl(com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super com.android.server.permission.jarjar.kotlin.DeepRecursiveScope<T, R>, ? super T, ? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3, T t) {
        super(null);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function3, "block");
        this.function = function3;
        this.value = t;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(this, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        this.cont = this;
        this.result = com.android.server.permission.jarjar.kotlin.DeepRecursiveKt.UNDEFINED_RESULT;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext getContext() {
        return com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
    public void resumeWith(java.lang.Object result) {
        this.cont = null;
        this.result = result;
    }

    @Override // com.android.server.permission.jarjar.kotlin.DeepRecursiveScope
    public java.lang.Object callRecursive(T t, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super R> continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        this.cont = continuation;
        this.value = t;
        java.lang.Object coroutine_suspended = com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (coroutine_suspended == com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return coroutine_suspended;
    }

    @Override // com.android.server.permission.jarjar.kotlin.DeepRecursiveScope
    public <U, S> java.lang.Object callRecursive(com.android.server.permission.jarjar.kotlin.DeepRecursiveFunction<U, S> deepRecursiveFunction, U u, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super S> continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<com.android.server.permission.jarjar.kotlin.DeepRecursiveScope<U, S>, U, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super S>, java.lang.Object> block$kotlin_stdlib = deepRecursiveFunction.getBlock$kotlin_stdlib();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(block$kotlin_stdlib, "null cannot be cast to non-null type @[ExtensionFunctionType] kotlin.coroutines.SuspendFunction2<kotlin.DeepRecursiveScope<*, *>, kotlin.Any?, kotlin.Any?>{ kotlin.DeepRecursiveKt.DeepRecursiveFunctionBlock }");
        com.android.server.permission.jarjar.kotlin.DeepRecursiveScopeImpl<T, R> deepRecursiveScopeImpl = this;
        com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super com.android.server.permission.jarjar.kotlin.DeepRecursiveScope<?, ?>, java.lang.Object, ? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object>, ? extends java.lang.Object> function3 = deepRecursiveScopeImpl.function;
        if (block$kotlin_stdlib != function3) {
            deepRecursiveScopeImpl.function = block$kotlin_stdlib;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            deepRecursiveScopeImpl.cont = deepRecursiveScopeImpl.crossFunctionCompletion(function3, continuation);
        } else {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            deepRecursiveScopeImpl.cont = continuation;
        }
        deepRecursiveScopeImpl.value = u;
        java.lang.Object coroutine_suspended = com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (coroutine_suspended == com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return coroutine_suspended;
    }

    private final com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> crossFunctionCompletion(final com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super com.android.server.permission.jarjar.kotlin.DeepRecursiveScope<?, ?>, java.lang.Object, ? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object>, ? extends java.lang.Object> function3, final com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> continuation) {
        final com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext emptyCoroutineContext = com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
        return new com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object>() { // from class: com.android.server.permission.jarjar.kotlin.DeepRecursiveScopeImpl$crossFunctionCompletion$$inlined$Continuation$1
            @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
            public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext getContext() {
                return emptyCoroutineContext;
            }

            @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
            public void resumeWith(java.lang.Object result) {
                this.function = function3;
                this.cont = continuation;
                this.result = result;
            }
        };
    }

    public final R runCallLoop() throws java.lang.Throwable {
        while (true) {
            R r = (R) this.result;
            com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> continuation = this.cont;
            if (continuation != null) {
                if (!com.android.server.permission.jarjar.kotlin.Result.m6091equalsimpl0(com.android.server.permission.jarjar.kotlin.DeepRecursiveKt.UNDEFINED_RESULT, r)) {
                    this.result = com.android.server.permission.jarjar.kotlin.DeepRecursiveKt.UNDEFINED_RESULT;
                    continuation.resumeWith(r);
                } else {
                    try {
                        com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super com.android.server.permission.jarjar.kotlin.DeepRecursiveScope<?, ?>, java.lang.Object, ? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object>, ? extends java.lang.Object> function3 = this.function;
                        java.lang.Object obj = this.value;
                        java.lang.Object objWrapWithContinuationImpl = !(function3 instanceof com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(function3, this, obj, continuation) : ((com.android.server.permission.jarjar.kotlin.jvm.functions.Function3) com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function3, 3)).invoke(this, obj, continuation);
                        if (objWrapWithContinuationImpl != com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                            com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
                            continuation.resumeWith(com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(objWrapWithContinuationImpl));
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.permission.jarjar.kotlin.Result.Companion companion2 = com.android.server.permission.jarjar.kotlin.Result.Companion;
                        continuation.resumeWith(com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(com.android.server.permission.jarjar.kotlin.ResultKt.createFailure(th)));
                    }
                }
            } else {
                com.android.server.permission.jarjar.kotlin.ResultKt.throwOnFailure(r);
                return r;
            }
        }
    }
}
