package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: SafeCollector.common.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u0006¨\u0006\u0007¸\u0006\t"}, d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$$inlined$unsafeFlow$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$filter$$inlined$unsafeTransform$1"}, k = 1, mv = {1, 9, 0}, xi = 176)
public final class FlowKt__TransformKt$filterIsInstance$$inlined$filter$1 implements kotlinx.coroutines.flow.Flow<java.lang.Object> {
    final /* synthetic */ kotlinx.coroutines.flow.Flow $this_unsafeTransform$inlined;

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1$2, reason: invalid class name */
    /* JADX INFO: compiled from: Emitters.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\b"}, d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$filter$$inlined$unsafeTransform$1$2"}, k = 3, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass2<T> implements kotlinx.coroutines.flow.FlowCollector {
        final /* synthetic */ kotlinx.coroutines.flow.FlowCollector $this_unsafeFlow;

        /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: Emitters.kt */
        @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 176)
        @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1$2", f = "Transform.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED}, m = "emit", n = {}, s = {})
        public static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
            java.lang.Object L$0;
            java.lang.Object L$1;
            int label;
            /* synthetic */ java.lang.Object result;

            public AnonymousClass1(kotlin.coroutines.Continuation continuation) {
                super(continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final java.lang.Object invokeSuspend(java.lang.Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.AnonymousClass2.this.emit(null, this);
            }
        }

        public AnonymousClass2(kotlinx.coroutines.flow.FlowCollector flowCollector) {
            this.$this_unsafeFlow = flowCollector;
        }

        /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
        @Override // kotlinx.coroutines.flow.FlowCollector
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object emit(java.lang.Object r9, kotlin.coroutines.Continuation r10) throws java.lang.Throwable {
            /*
                r8 = this;
                boolean r0 = r10 instanceof kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.AnonymousClass2.AnonymousClass1
                if (r0 == 0) goto L14
                r0 = r10
                kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1$2$1 r0 = (kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.AnonymousClass2.AnonymousClass1) r0
                int r1 = r0.label
                r2 = -2147483648(0xffffffff80000000, float:-0.0)
                r1 = r1 & r2
                if (r1 == 0) goto L14
                int r10 = r0.label
                int r10 = r10 - r2
                r0.label = r10
                goto L19
            L14:
                kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1$2$1 r0 = new kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1$2$1
                r0.<init>(r10)
            L19:
                r10 = r0
                java.lang.Object r0 = r10.result
                java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r2 = r10.label
                switch(r2) {
                    case 0: goto L32;
                    case 1: goto L2d;
                    default: goto L25;
                }
            L25:
                java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
                java.lang.String r10 = "call to 'resume' before 'invoke' with coroutine"
                r9.<init>(r10)
                throw r9
            L2d:
                r9 = 0
                kotlin.ResultKt.throwOnFailure(r0)
                goto L53
            L32:
                kotlin.ResultKt.throwOnFailure(r0)
                r2 = r8
                kotlinx.coroutines.flow.FlowCollector r2 = r2.$this_unsafeFlow
                r3 = 0
                r4 = r10
                kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
                r4 = r9
                r5 = 0
                r6 = 3
                java.lang.String r7 = "R"
                kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(r6, r7)
                boolean r4 = r4 instanceof java.lang.Object
                if (r4 == 0) goto L54
                r4 = 1
                r10.label = r4
                java.lang.Object r9 = r2.emit(r9, r10)
                if (r9 != r1) goto L52
                return r1
            L52:
                r9 = r3
            L53:
                goto L55
            L54:
            L55:
                kotlin.Unit r9 = kotlin.Unit.INSTANCE
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
        }

        public final java.lang.Object emit$$forInline(java.lang.Object value, kotlin.coroutines.Continuation $completion) {
            kotlin.jvm.internal.InlineMarker.mark(4);
            new kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.AnonymousClass2.AnonymousClass1($completion);
            kotlin.jvm.internal.InlineMarker.mark(5);
            kotlinx.coroutines.flow.FlowCollector $this$filter_u24lambda_u240 = this.$this_unsafeFlow;
            kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(3, "R");
            if (value instanceof java.lang.Object) {
                kotlin.jvm.internal.InlineMarker.mark(0);
                $this$filter_u24lambda_u240.emit(value, $completion);
                kotlin.jvm.internal.InlineMarker.mark(1);
            }
            return kotlin.Unit.INSTANCE;
        }
    }

    public FlowKt__TransformKt$filterIsInstance$$inlined$filter$1(kotlinx.coroutines.flow.Flow flow) {
        this.$this_unsafeTransform$inlined = flow;
    }

    @Override // kotlinx.coroutines.flow.Flow
    public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super java.lang.Object> flowCollector, kotlin.coroutines.Continuation $completion) {
        kotlinx.coroutines.flow.Flow flow = this.$this_unsafeTransform$inlined;
        kotlin.jvm.internal.Intrinsics.needClassReification();
        java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.AnonymousClass2(flowCollector), $completion);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }

    public java.lang.Object collect$$forInline(kotlinx.coroutines.flow.FlowCollector collector, kotlin.coroutines.Continuation $completion) {
        kotlin.jvm.internal.InlineMarker.mark(4);
        new kotlin.coroutines.jvm.internal.ContinuationImpl($completion) { // from class: kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.1
            int label;
            /* synthetic */ java.lang.Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final java.lang.Object invokeSuspend(java.lang.Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.this.collect(null, this);
            }
        };
        kotlin.jvm.internal.InlineMarker.mark(5);
        kotlinx.coroutines.flow.Flow flow = this.$this_unsafeTransform$inlined;
        kotlin.jvm.internal.Intrinsics.needClassReification();
        kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1.AnonymousClass2(collector);
        kotlin.jvm.internal.InlineMarker.mark(0);
        flow.collect(anonymousClass2, $completion);
        kotlin.jvm.internal.InlineMarker.mark(1);
        return kotlin.Unit.INSTANCE;
    }
}
