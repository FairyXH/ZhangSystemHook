package kotlinx.coroutines.selects;

/* JADX INFO: compiled from: SelectOld.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0001\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\n\u0010\b\u001a\u0004\u0018\u00010\tH\u0001J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0001R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/selects/SelectBuilderImpl;", "R", "Lkotlinx/coroutines/selects/SelectImplementation;", "uCont", "Lkotlin/coroutines/Continuation;", "(Lkotlin/coroutines/Continuation;)V", "cont", "Lkotlinx/coroutines/CancellableContinuationImpl;", "getResult", "", "handleBuilderException", "", "e", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SelectBuilderImpl<R> extends kotlinx.coroutines.selects.SelectImplementation<R> {
    private final kotlinx.coroutines.CancellableContinuationImpl<R> cont;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SelectBuilderImpl(kotlin.coroutines.Continuation<? super R> uCont) {
        super(uCont.get$context());
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uCont, "uCont");
        this.cont = new kotlinx.coroutines.CancellableContinuationImpl<>(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(uCont), 1);
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.selects.SelectBuilderImpl$getResult$1, reason: invalid class name */
    /* JADX INFO: compiled from: SelectOld.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", "", "R", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.selects.SelectBuilderImpl$getResult$1", f = "SelectOld.kt", i = {}, l = {43}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        int label;
        final /* synthetic */ kotlinx.coroutines.selects.SelectBuilderImpl<R> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(kotlinx.coroutines.selects.SelectBuilderImpl<R> selectBuilderImpl, kotlin.coroutines.Continuation<? super kotlinx.coroutines.selects.SelectBuilderImpl.AnonymousClass1> continuation) {
            super(2, continuation);
            this.this$0 = selectBuilderImpl;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return new kotlinx.coroutines.selects.SelectBuilderImpl.AnonymousClass1(this.this$0, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.selects.SelectBuilderImpl.AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object result) throws java.lang.Throwable {
            kotlinx.coroutines.selects.SelectBuilderImpl.AnonymousClass1 anonymousClass1;
            java.lang.Throwable e;
            kotlinx.coroutines.selects.SelectBuilderImpl.AnonymousClass1 anonymousClass12;
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure(result);
                    anonymousClass1 = this;
                    try {
                        anonymousClass1.label = 1;
                        java.lang.Object objDoSelect = anonymousClass1.this$0.doSelect(anonymousClass1);
                        if (objDoSelect != coroutine_suspended) {
                            result = objDoSelect;
                            kotlinx.coroutines.selects.SelectOldKt.resumeUndispatched(((kotlinx.coroutines.selects.SelectBuilderImpl) anonymousClass1.this$0).cont, result);
                            return kotlin.Unit.INSTANCE;
                        }
                        return coroutine_suspended;
                    } catch (java.lang.Throwable th) {
                        e = th;
                        anonymousClass12 = anonymousClass1;
                        kotlinx.coroutines.selects.SelectOldKt.resumeUndispatchedWithException(((kotlinx.coroutines.selects.SelectBuilderImpl) anonymousClass12.this$0).cont, e);
                        return kotlin.Unit.INSTANCE;
                    }
                case 1:
                    anonymousClass12 = this;
                    try {
                        kotlin.ResultKt.throwOnFailure(result);
                        anonymousClass1 = anonymousClass12;
                        kotlinx.coroutines.selects.SelectOldKt.resumeUndispatched(((kotlinx.coroutines.selects.SelectBuilderImpl) anonymousClass1.this$0).cont, result);
                        return kotlin.Unit.INSTANCE;
                    } catch (java.lang.Throwable th2) {
                        e = th2;
                        kotlinx.coroutines.selects.SelectOldKt.resumeUndispatchedWithException(((kotlinx.coroutines.selects.SelectBuilderImpl) anonymousClass12.this$0).cont, e);
                        return kotlin.Unit.INSTANCE;
                    }
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final java.lang.Object getResult() {
        if (this.cont.isCompleted()) {
            return this.cont.getResult();
        }
        kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default(kotlinx.coroutines.CoroutineScopeKt.CoroutineScope(getContext()), null, kotlinx.coroutines.CoroutineStart.UNDISPATCHED, new kotlinx.coroutines.selects.SelectBuilderImpl.AnonymousClass1(this, null), 1, null);
        return this.cont.getResult();
    }

    public final void handleBuilderException(java.lang.Throwable e) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
        kotlinx.coroutines.CancellableContinuationImpl<R> cancellableContinuationImpl = this.cont;
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        cancellableContinuationImpl.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(e)));
    }
}
