package kotlinx.coroutines.flow;

/* JADX INFO: Add missing generic type declarations: [R] */
/* JADX INFO: compiled from: Zip.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@¨\u0006\u0005"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "kotlinx/coroutines/flow/FlowKt__ZipKt$combineTransformUnsafe$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1", f = "Zip.kt", i = {}, l = {273}, m = "invokeSuspend", n = {}, s = {})
public final class FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1<R> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super R>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
    final /* synthetic */ kotlinx.coroutines.flow.Flow[] $flows;
    final /* synthetic */ kotlin.jvm.functions.Function4 $transform$inlined;
    private /* synthetic */ java.lang.Object L$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1(kotlinx.coroutines.flow.Flow[] flowArr, kotlin.coroutines.Continuation continuation, kotlin.jvm.functions.Function4 function4) {
        super(2, continuation);
        this.$flows = flowArr;
        this.$transform$inlined = function4;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
        kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1 flowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1(this.$flows, continuation, this.$transform$inlined);
        flowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1.L$0 = obj;
        return flowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return ((kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1) create(flowCollector, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1$1, reason: invalid class name */
    /* JADX INFO: compiled from: Zip.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u008a@¨\u0006\u0007"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "it", "", "kotlinx/coroutines/flow/FlowKt__ZipKt$combineTransformUnsafe$1$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1$1", f = "Zip.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.DEVICE_ROTATED}, m = "invokeSuspend", n = {}, s = {})
    public static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, java.lang.Object[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlin.jvm.functions.Function4 $transform$inlined;
        private /* synthetic */ java.lang.Object L$0;
        /* synthetic */ java.lang.Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(kotlin.coroutines.Continuation continuation, kotlin.jvm.functions.Function4 function4) {
            super(3, continuation);
            this.$transform$inlined = function4;
        }

        @Override // kotlin.jvm.functions.Function3
        public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, java.lang.Object[] objArr, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1.AnonymousClass1 anonymousClass1 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1.AnonymousClass1(continuation, this.$transform$inlined);
            anonymousClass1.L$0 = flowCollector;
            anonymousClass1.L$1 = objArr;
            return anonymousClass1.invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    kotlinx.coroutines.flow.FlowCollector flowCollector = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                    java.lang.Object[] args = (java.lang.Object[]) this.L$1;
                    kotlin.jvm.functions.Function4 function4 = this.$transform$inlined;
                    java.lang.Object obj = args[0];
                    java.lang.Object obj2 = args[1];
                    this.label = 1;
                    kotlin.jvm.internal.InlineMarker.mark(6);
                    java.lang.Object objInvoke = function4.invoke(flowCollector, obj, obj2, this);
                    kotlin.jvm.internal.InlineMarker.mark(7);
                    if (objInvoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    kotlin.ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return kotlin.Unit.INSTANCE;
        }
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
        java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure($result);
                kotlinx.coroutines.flow.FlowCollector $this$flow = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                this.label = 1;
                if (kotlinx.coroutines.flow.internal.CombineKt.combineInternal($this$flow, this.$flows, kotlinx.coroutines.flow.FlowKt__ZipKt.nullArrayFactory$FlowKt__ZipKt(), new kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1.AnonymousClass1(null, this.$transform$inlined), this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                kotlin.ResultKt.throwOnFailure($result);
                break;
            default:
                throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return kotlin.Unit.INSTANCE;
    }
}
