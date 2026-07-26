package kotlinx.coroutines.android;

/* JADX INFO: compiled from: HandlerDispatcher.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u001b\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007B!\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u001c\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015H\u0002J\u001c\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015H\u0016J\u0013\u0010\u0017\u001a\u00020\t2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0096\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016J$\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u00152\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010 \u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u001e\u0010!\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u001f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00100#H\u0016J\b\u0010$\u001a\u00020\u0006H\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u0000X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lkotlinx/coroutines/android/HandlerContext;", "Lkotlinx/coroutines/android/HandlerDispatcher;", "Lkotlinx/coroutines/Delay;", "handler", "Landroid/os/Handler;", "name", "", "(Landroid/os/Handler;Ljava/lang/String;)V", "invokeImmediately", "", "(Landroid/os/Handler;Ljava/lang/String;Z)V", "_immediate", "immediate", "getImmediate", "()Lkotlinx/coroutines/android/HandlerContext;", "cancelOnRejection", "", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "dispatch", "equals", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "invokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "", "isDispatchNeeded", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "toString", "external__kotlinx.coroutines__android_common__kotlinx_coroutines_android"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class HandlerContext extends kotlinx.coroutines.android.HandlerDispatcher implements kotlinx.coroutines.Delay {
    private volatile kotlinx.coroutines.android.HandlerContext _immediate;
    private final android.os.Handler handler;
    private final kotlinx.coroutines.android.HandlerContext immediate;
    private final boolean invokeImmediately;
    private final java.lang.String name;

    private HandlerContext(android.os.Handler handler, java.lang.String name, boolean invokeImmediately) {
        super(null);
        this.handler = handler;
        this.name = name;
        this.invokeImmediately = invokeImmediately;
        this._immediate = this.invokeImmediately ? this : null;
        kotlinx.coroutines.android.HandlerContext it = this._immediate;
        if (it == null) {
            it = new kotlinx.coroutines.android.HandlerContext(this.handler, this.name, true);
            this._immediate = it;
        }
        this.immediate = it;
    }

    public /* synthetic */ HandlerContext(android.os.Handler handler, java.lang.String str, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(handler, (i & 2) != 0 ? null : str);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public HandlerContext(android.os.Handler handler, java.lang.String name) {
        this(handler, name, false);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "handler");
    }

    @Override // kotlinx.coroutines.android.HandlerDispatcher, kotlinx.coroutines.MainCoroutineDispatcher
    public kotlinx.coroutines.android.HandlerContext getImmediate() {
        return this.immediate;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public boolean isDispatchNeeded(kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        return (this.invokeImmediately && kotlin.jvm.internal.Intrinsics.areEqual(android.os.Looper.myLooper(), this.handler.getLooper())) ? false : true;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    /* JADX INFO: renamed from: dispatch */
    public void mo12864dispatch(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        if (!this.handler.post(block)) {
            cancelOnRejection(context, block);
        }
    }

    @Override // kotlinx.coroutines.Delay
    /* JADX INFO: renamed from: scheduleResumeAfterDelay */
    public void mo12865scheduleResumeAfterDelay(long timeMillis, final kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        final java.lang.Runnable block = new java.lang.Runnable() { // from class: kotlinx.coroutines.android.HandlerContext$scheduleResumeAfterDelay$$inlined$Runnable$1
            @Override // java.lang.Runnable
            public final void run() {
                kotlinx.coroutines.CancellableContinuation $this$scheduleResumeAfterDelay_u24lambda_u242_u24lambda_u241 = continuation;
                $this$scheduleResumeAfterDelay_u24lambda_u242_u24lambda_u241.resumeUndispatched(this, kotlin.Unit.INSTANCE);
            }
        };
        if (this.handler.postDelayed(block, kotlin.ranges.RangesKt.coerceAtMost(timeMillis, 4611686018427387903L))) {
            continuation.invokeOnCancellation(new kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>() { // from class: kotlinx.coroutines.android.HandlerContext.scheduleResumeAfterDelay.1
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
                    invoke2(th);
                    return kotlin.Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2(java.lang.Throwable it) {
                    kotlinx.coroutines.android.HandlerContext.this.handler.removeCallbacks(block);
                }
            });
        } else {
            cancelOnRejection(continuation.getContext(), block);
        }
    }

    @Override // kotlinx.coroutines.android.HandlerDispatcher, kotlinx.coroutines.Delay
    public kotlinx.coroutines.DisposableHandle invokeOnTimeout(long timeMillis, final java.lang.Runnable block, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        if (this.handler.postDelayed(block, kotlin.ranges.RangesKt.coerceAtMost(timeMillis, 4611686018427387903L))) {
            return new kotlinx.coroutines.DisposableHandle() { // from class: kotlinx.coroutines.android.HandlerContext.invokeOnTimeout.1
                @Override // kotlinx.coroutines.DisposableHandle
                public final void dispose() {
                    kotlinx.coroutines.android.HandlerContext.this.handler.removeCallbacks(block);
                }
            };
        }
        cancelOnRejection(context, block);
        return kotlinx.coroutines.NonDisposableHandle.INSTANCE;
    }

    private final void cancelOnRejection(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlinx.coroutines.JobKt.cancel(context, new java.util.concurrent.CancellationException("The task was rejected, the handler underlying the dispatcher '" + this + "' was closed"));
        kotlinx.coroutines.Dispatchers.getIO().mo12864dispatch(context, block);
    }

    @Override // kotlinx.coroutines.MainCoroutineDispatcher, kotlinx.coroutines.CoroutineDispatcher
    public java.lang.String toString() {
        java.lang.String stringInternalImpl = toStringInternalImpl();
        if (stringInternalImpl == null) {
            kotlinx.coroutines.android.HandlerContext $this$toString_u24lambda_u243 = this;
            java.lang.String str = $this$toString_u24lambda_u243.name;
            if (str == null) {
                str = $this$toString_u24lambda_u243.handler.toString();
            }
            return $this$toString_u24lambda_u243.invokeImmediately ? str + ".immediate" : str;
        }
        return stringInternalImpl;
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof kotlinx.coroutines.android.HandlerContext) && ((kotlinx.coroutines.android.HandlerContext) other).handler == this.handler;
    }

    public int hashCode() {
        return java.lang.System.identityHashCode(this.handler);
    }
}
