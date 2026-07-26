package kotlinx.coroutines.flow;

/* JADX INFO: Add missing generic type declarations: [R] */
/* JADX INFO: compiled from: SafeCollector.common.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u0006¨\u0006\u0007¸\u0006\u0000"}, d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 176)
public final class FlowKt__ZipKt$combine$$inlined$unsafeFlow$3<R> implements kotlinx.coroutines.flow.Flow<R> {
    final /* synthetic */ kotlinx.coroutines.flow.Flow[] $flowArray$inlined;
    final /* synthetic */ kotlin.jvm.functions.Function2 $transform$inlined;

    public FlowKt__ZipKt$combine$$inlined$unsafeFlow$3(kotlinx.coroutines.flow.Flow[] flowArr, kotlin.jvm.functions.Function2 function2) {
        this.$flowArray$inlined = flowArr;
        this.$transform$inlined = function2;
    }

    @Override // kotlinx.coroutines.flow.Flow
    public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.flow.Flow[] flowArr = this.$flowArray$inlined;
        kotlin.jvm.internal.Intrinsics.needClassReification();
        kotlinx.coroutines.flow.FlowKt__ZipKt$combine$6$1 flowKt__ZipKt$combine$6$1 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$6$1(this.$flowArray$inlined);
        kotlin.jvm.internal.Intrinsics.needClassReification();
        java.lang.Object objCombineInternal = kotlinx.coroutines.flow.internal.CombineKt.combineInternal(flowCollector, flowArr, flowKt__ZipKt$combine$6$1, new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$6$2(this.$transform$inlined, null), continuation);
        return objCombineInternal == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCombineInternal : kotlin.Unit.INSTANCE;
    }

    public java.lang.Object collect$$forInline(kotlinx.coroutines.flow.FlowCollector collector, kotlin.coroutines.Continuation $completion) {
        kotlin.jvm.internal.InlineMarker.mark(4);
        new kotlin.coroutines.jvm.internal.ContinuationImpl($completion) { // from class: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$unsafeFlow$3.1
            int label;
            /* synthetic */ java.lang.Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final java.lang.Object invokeSuspend(java.lang.Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$unsafeFlow$3.this.collect(null, this);
            }
        };
        kotlin.jvm.internal.InlineMarker.mark(5);
        kotlinx.coroutines.flow.Flow[] flowArr = this.$flowArray$inlined;
        kotlin.jvm.internal.Intrinsics.needClassReification();
        kotlinx.coroutines.flow.FlowKt__ZipKt$combine$6$1 flowKt__ZipKt$combine$6$1 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$6$1(this.$flowArray$inlined);
        kotlin.jvm.internal.Intrinsics.needClassReification();
        kotlinx.coroutines.flow.FlowKt__ZipKt$combine$6$2 flowKt__ZipKt$combine$6$2 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$6$2(this.$transform$inlined, null);
        kotlin.jvm.internal.InlineMarker.mark(0);
        kotlinx.coroutines.flow.internal.CombineKt.combineInternal(collector, flowArr, flowKt__ZipKt$combine$6$1, flowKt__ZipKt$combine$6$2, $completion);
        kotlin.jvm.internal.InlineMarker.mark(1);
        return kotlin.Unit.INSTANCE;
    }
}
