package kotlinx.coroutines;

/* JADX INFO: compiled from: Await.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002:\u0002\r\u000eB\u001b\u0012\u0014\u0010\u0003\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u00028\u00000\u00050\u0004¢\u0006\u0002\u0010\u0006J\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bH\u0086@¢\u0006\u0002\u0010\fR\u001e\u0010\u0003\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u00028\u00000\u00050\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/AwaitAll;", "T", "", "deferreds", "", "Lkotlinx/coroutines/Deferred;", "([Lkotlinx/coroutines/Deferred;)V", "[Lkotlinx/coroutines/Deferred;", "notCompletedCount", "Lkotlinx/atomicfu/AtomicInt;", "await", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "AwaitAllNode", "DisposeHandlersOnCancel", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class AwaitAll<T> {
    private final kotlinx.coroutines.Deferred<T>[] deferreds;
    private final kotlinx.atomicfu.AtomicInt notCompletedCount;

    /* JADX WARN: Multi-variable type inference failed */
    public AwaitAll(kotlinx.coroutines.Deferred<? extends T>[] deferreds) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(deferreds, "deferreds");
        this.deferreds = deferreds;
        this.notCompletedCount = kotlinx.atomicfu.AtomicFU.atomic(this.deferreds.length);
    }

    public final java.lang.Object await(kotlin.coroutines.Continuation<? super java.util.List<? extends T>> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        kotlinx.coroutines.CancellableContinuationImpl cont = cancellable$iv;
        int length = this.deferreds.length;
        kotlinx.coroutines.AwaitAll.AwaitAllNode[] nodes = new kotlinx.coroutines.AwaitAll.AwaitAllNode[length];
        for (int i = 0; i < length; i++) {
            kotlinx.coroutines.Deferred deferred = this.deferreds[i];
            deferred.start();
            kotlinx.coroutines.AwaitAll.AwaitAllNode $this$await_u24lambda_u242_u24lambda_u240 = new kotlinx.coroutines.AwaitAll.AwaitAllNode(this, cont);
            kotlinx.coroutines.AwaitAll.AwaitAllNode $this$asHandler$iv = $this$await_u24lambda_u242_u24lambda_u240;
            $this$await_u24lambda_u242_u24lambda_u240.setHandle(deferred.invokeOnCompletion($this$asHandler$iv));
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
            nodes[i] = $this$await_u24lambda_u242_u24lambda_u240;
        }
        kotlinx.coroutines.AwaitAll<T>.DisposeHandlersOnCancel disposeHandlersOnCancel = new kotlinx.coroutines.AwaitAll.DisposeHandlersOnCancel(this, nodes);
        for (kotlinx.coroutines.AwaitAll.AwaitAllNode awaitAllNode : nodes) {
            awaitAllNode.setDisposer(disposeHandlersOnCancel);
        }
        if (cont.isCompleted()) {
            disposeHandlersOnCancel.disposeAll();
        } else {
            kotlinx.coroutines.AwaitAll<T>.DisposeHandlersOnCancel $this$asHandler$iv2 = disposeHandlersOnCancel;
            cont.invokeOnCancellation($this$asHandler$iv2);
        }
        java.lang.Object result = cancellable$iv.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    /* JADX INFO: compiled from: Await.kt */
    @kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u001d\u0012\u0016\u0010\u0002\u001a\u0012\u0012\u000e\u0012\f0\u0004R\b\u0012\u0004\u0012\u00028\u00000\u00050\u0003¢\u0006\u0002\u0010\u0006J\u0006\u0010\b\u001a\u00020\tJ\u0013\u0010\n\u001a\u00020\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0096\u0002J\b\u0010\r\u001a\u00020\u000eH\u0016R \u0010\u0002\u001a\u0012\u0012\u000e\u0012\f0\u0004R\b\u0012\u0004\u0012\u00028\u00000\u00050\u0003X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel;", "Lkotlinx/coroutines/CancelHandler;", "nodes", "", "Lkotlinx/coroutines/AwaitAll$AwaitAllNode;", "Lkotlinx/coroutines/AwaitAll;", "(Lkotlinx/coroutines/AwaitAll;[Lkotlinx/coroutines/AwaitAll$AwaitAllNode;)V", "[Lkotlinx/coroutines/AwaitAll$AwaitAllNode;", "disposeAll", "", "invoke", "cause", "", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class DisposeHandlersOnCancel extends kotlinx.coroutines.CancelHandler {
        private final kotlinx.coroutines.AwaitAll<T>.AwaitAllNode[] nodes;
        final /* synthetic */ kotlinx.coroutines.AwaitAll<T> this$0;

        public DisposeHandlersOnCancel(kotlinx.coroutines.AwaitAll this$0, kotlinx.coroutines.AwaitAll<T>.AwaitAllNode[] nodes) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(nodes, "nodes");
            this.this$0 = this$0;
            this.nodes = nodes;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
            invoke2(th);
            return kotlin.Unit.INSTANCE;
        }

        public final void disposeAll() {
            for (kotlinx.coroutines.AwaitAll<T>.AwaitAllNode awaitAllNode : this.nodes) {
                awaitAllNode.getHandle().dispose();
            }
        }

        @Override // kotlinx.coroutines.CancelHandlerBase
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public void invoke2(java.lang.Throwable cause) {
            disposeAll();
        }

        public java.lang.String toString() {
            return "DisposeHandlersOnCancel[" + this.nodes + "]";
        }
    }

    /* JADX INFO: compiled from: Await.kt */
    @kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00040\u0003¢\u0006\u0002\u0010\u0005J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0096\u0002R \u0010\u0006\u001a\u0014\u0012\u0010\u0012\u000e\u0018\u00010\bR\b\u0012\u0004\u0012\u00028\u00000\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00040\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R<\u0010\u000b\u001a\u000e\u0018\u00010\bR\b\u0012\u0004\u0012\u00028\u00000\t2\u0012\u0010\n\u001a\u000e\u0018\u00010\bR\b\u0012\u0004\u0012\u00028\u00000\t8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015¨\u0006\u001a"}, d2 = {"Lkotlinx/coroutines/AwaitAll$AwaitAllNode;", "Lkotlinx/coroutines/JobNode;", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Lkotlinx/coroutines/AwaitAll;Lkotlinx/coroutines/CancellableContinuation;)V", "_disposer", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel;", "Lkotlinx/coroutines/AwaitAll;", "value", "disposer", "getDisposer", "()Lkotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel;", "setDisposer", "(Lkotlinx/coroutines/AwaitAll$DisposeHandlersOnCancel;)V", "handle", "Lkotlinx/coroutines/DisposableHandle;", "getHandle", "()Lkotlinx/coroutines/DisposableHandle;", "setHandle", "(Lkotlinx/coroutines/DisposableHandle;)V", "invoke", "", "cause", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class AwaitAllNode extends kotlinx.coroutines.JobNode {
        private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.AwaitAll<T>.DisposeHandlersOnCancel> _disposer;
        private final kotlinx.coroutines.CancellableContinuation<java.util.List<? extends T>> continuation;
        public kotlinx.coroutines.DisposableHandle handle;
        final /* synthetic */ kotlinx.coroutines.AwaitAll<T> this$0;

        /* JADX WARN: Multi-variable type inference failed */
        public AwaitAllNode(kotlinx.coroutines.AwaitAll this$0, kotlinx.coroutines.CancellableContinuation<? super java.util.List<? extends T>> continuation) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
            this.this$0 = this$0;
            this.continuation = continuation;
            this._disposer = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
            invoke2(th);
            return kotlin.Unit.INSTANCE;
        }

        public final kotlinx.coroutines.DisposableHandle getHandle() {
            kotlinx.coroutines.DisposableHandle disposableHandle = this.handle;
            if (disposableHandle != null) {
                return disposableHandle;
            }
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("handle");
            return null;
        }

        public final void setHandle(kotlinx.coroutines.DisposableHandle disposableHandle) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(disposableHandle, "<set-?>");
            this.handle = disposableHandle;
        }

        public final kotlinx.coroutines.AwaitAll<T>.DisposeHandlersOnCancel getDisposer() {
            return this._disposer.getValue();
        }

        public final void setDisposer(kotlinx.coroutines.AwaitAll<T>.DisposeHandlersOnCancel disposeHandlersOnCancel) {
            this._disposer.setValue(disposeHandlersOnCancel);
        }

        @Override // kotlinx.coroutines.CompletionHandlerBase
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public void invoke2(java.lang.Throwable cause) {
            if (cause == null) {
                if (((kotlinx.coroutines.AwaitAll) this.this$0).notCompletedCount.decrementAndGet() == 0) {
                    kotlinx.coroutines.CancellableContinuation<java.util.List<? extends T>> cancellableContinuation = this.continuation;
                    kotlinx.coroutines.Deferred[] deferredArr = ((kotlinx.coroutines.AwaitAll) this.this$0).deferreds;
                    java.util.Collection destination$iv$iv = new java.util.ArrayList(deferredArr.length);
                    for (kotlinx.coroutines.Deferred deferred : deferredArr) {
                        destination$iv$iv.add(deferred.getCompleted());
                    }
                    kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                    cancellableContinuation.resumeWith(kotlin.Result.m11307constructorimpl((java.util.List) destination$iv$iv));
                    return;
                }
                return;
            }
            java.lang.Object token = this.continuation.tryResumeWithException(cause);
            if (token != null) {
                this.continuation.completeResume(token);
                kotlinx.coroutines.AwaitAll<T>.DisposeHandlersOnCancel disposer = getDisposer();
                if (disposer != null) {
                    disposer.disposeAll();
                }
            }
        }
    }
}
