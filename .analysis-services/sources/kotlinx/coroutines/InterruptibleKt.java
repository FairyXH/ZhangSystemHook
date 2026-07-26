package kotlinx.coroutines;

/* JADX INFO: compiled from: Interruptible.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a,\u0010\u0005\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00060\nH\u0086@¢\u0006\u0002\u0010\u000b\u001a)\u0010\f\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u00062\u0006\u0010\r\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00060\nH\u0002¢\u0006\u0002\u0010\u000e\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"FINISHED", "", "INTERRUPTED", "INTERRUPTING", "WORKING", "runInterruptible", "T", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function0;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function0;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "runInterruptibleInExpectedContext", "coroutineContext", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class InterruptibleKt {
    private static final int FINISHED = 1;
    private static final int INTERRUPTED = 3;
    private static final int INTERRUPTING = 2;
    private static final int WORKING = 0;

    public static /* synthetic */ java.lang.Object runInterruptible$default(kotlin.coroutines.CoroutineContext coroutineContext, kotlin.jvm.functions.Function0 function0, kotlin.coroutines.Continuation continuation, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
        }
        return runInterruptible(coroutineContext, function0, continuation);
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlinx.coroutines.InterruptibleKt$runInterruptible$2, reason: invalid class name */
    /* JADX INFO: compiled from: Interruptible.kt */
    @kotlin.Metadata(d1 = {"\u0000\b\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "T", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.InterruptibleKt$runInterruptible$2", f = "Interruptible.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2<T> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super T>, java.lang.Object> {
        final /* synthetic */ kotlin.jvm.functions.Function0<T> $block;
        private /* synthetic */ java.lang.Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass2(kotlin.jvm.functions.Function0<? extends T> function0, kotlin.coroutines.Continuation<? super kotlinx.coroutines.InterruptibleKt.AnonymousClass2> continuation) {
            super(2, continuation);
            this.$block = function0;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.InterruptibleKt.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.InterruptibleKt.AnonymousClass2(this.$block, continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super T> continuation) {
            return ((kotlinx.coroutines.InterruptibleKt.AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
            kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure(obj);
                    kotlinx.coroutines.CoroutineScope $this$withContext = (kotlinx.coroutines.CoroutineScope) this.L$0;
                    return kotlinx.coroutines.InterruptibleKt.runInterruptibleInExpectedContext($this$withContext.getCoroutineContext(), this.$block);
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static final <T> java.lang.Object runInterruptible(kotlin.coroutines.CoroutineContext context, kotlin.jvm.functions.Function0<? extends T> function0, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.BuildersKt.withContext(context, new kotlinx.coroutines.InterruptibleKt.AnonymousClass2(function0, null), continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <T> T runInterruptibleInExpectedContext(kotlin.coroutines.CoroutineContext coroutineContext, kotlin.jvm.functions.Function0<? extends T> function0) throws java.lang.Throwable {
        try {
            kotlinx.coroutines.ThreadState threadState = new kotlinx.coroutines.ThreadState(kotlinx.coroutines.JobKt.getJob(coroutineContext));
            threadState.setup();
            try {
                return function0.invoke();
            } finally {
                threadState.clearInterrupt();
            }
        } catch (java.lang.InterruptedException e) {
            java.lang.Throwable thInitCause = new java.util.concurrent.CancellationException("Blocking call was interrupted due to parent cancellation").initCause(e);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(thInitCause, "initCause(...)");
            throw thInitCause;
        }
    }
}
