package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: SafeCollector.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aK\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022/\b\u0005\u0010\u0003\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0004¢\u0006\u0002\b\tH\u0081\b¢\u0006\u0002\u0010\n\u001a\u0018\u0010\u000b\u001a\u00020\u0007*\u0006\u0012\u0002\b\u00030\f2\u0006\u0010\r\u001a\u00020\u000eH\u0001\u001a\u001b\u0010\u000f\u001a\u0004\u0018\u00010\u0010*\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0010H\u0080\u0010¨\u0006\u0012"}, d2 = {"unsafeFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "checkContext", "Lkotlinx/coroutines/flow/internal/SafeCollector;", "currentContext", "Lkotlin/coroutines/CoroutineContext;", "transitiveCoroutineParent", "Lkotlinx/coroutines/Job;", "collectJob", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SafeCollector_commonKt {
    public static final void checkContext(final kotlinx.coroutines.flow.internal.SafeCollector<?> safeCollector, kotlin.coroutines.CoroutineContext currentContext) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(safeCollector, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(currentContext, "currentContext");
        int result = ((java.lang.Number) currentContext.fold(0, new kotlin.jvm.functions.Function2<java.lang.Integer, kotlin.coroutines.CoroutineContext.Element, java.lang.Integer>() { // from class: kotlinx.coroutines.flow.internal.SafeCollector_commonKt$checkContext$result$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ java.lang.Integer invoke(java.lang.Integer num, kotlin.coroutines.CoroutineContext.Element element) {
                return invoke(num.intValue(), element);
            }

            public final java.lang.Integer invoke(int count, kotlin.coroutines.CoroutineContext.Element element) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
                kotlin.coroutines.CoroutineContext.Key<?> key = element.getKey();
                kotlin.coroutines.CoroutineContext.Element collectElement = safeCollector.collectContext.get(key);
                if (key != kotlinx.coroutines.Job.INSTANCE) {
                    return java.lang.Integer.valueOf(element != collectElement ? Integer.MIN_VALUE : count + 1);
                }
                kotlinx.coroutines.Job collectJob = (kotlinx.coroutines.Job) collectElement;
                kotlinx.coroutines.Job emissionParentJob = kotlinx.coroutines.flow.internal.SafeCollector_commonKt.transitiveCoroutineParent((kotlinx.coroutines.Job) element, collectJob);
                if (emissionParentJob == collectJob) {
                    return java.lang.Integer.valueOf(collectJob == null ? count : count + 1);
                }
                throw new java.lang.IllegalStateException(("Flow invariant is violated:\n\t\tEmission from another coroutine is detected.\n\t\tChild of " + emissionParentJob + ", expected child of " + collectJob + ".\n\t\tFlowCollector is not thread-safe and concurrent emissions are prohibited.\n\t\tTo mitigate this restriction please use 'channelFlow' builder instead of 'flow'").toString());
            }
        })).intValue();
        if (result != safeCollector.collectContextSize) {
            throw new java.lang.IllegalStateException(("Flow invariant is violated:\n\t\tFlow was collected in " + safeCollector.collectContext + ",\n\t\tbut emission happened in " + currentContext + ".\n\t\tPlease refer to 'flow' documentation or use 'flowOn' instead").toString());
        }
    }

    public static final kotlinx.coroutines.Job transitiveCoroutineParent(kotlinx.coroutines.Job $this$transitiveCoroutineParent, kotlinx.coroutines.Job collectJob) {
        while ($this$transitiveCoroutineParent != null) {
            if ($this$transitiveCoroutineParent == collectJob || !($this$transitiveCoroutineParent instanceof kotlinx.coroutines.internal.ScopeCoroutine)) {
                return $this$transitiveCoroutineParent;
            }
            $this$transitiveCoroutineParent = $this$transitiveCoroutineParent.getParent();
        }
        return null;
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.SafeCollector_commonKt$unsafeFlow$1, reason: invalid class name */
    /* JADX INFO: compiled from: SafeCollector.common.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u0006¨\u0006\u0007"}, d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass1<T> implements kotlinx.coroutines.flow.Flow<T> {
        final /* synthetic */ kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $block;

        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass1(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
            this.$block = function2;
        }

        @Override // kotlinx.coroutines.flow.Flow
        public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            java.lang.Object objInvoke = this.$block.invoke(flowCollector, continuation);
            return objInvoke == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objInvoke : kotlin.Unit.INSTANCE;
        }

        public java.lang.Object collect$$forInline(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, final kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            kotlin.jvm.internal.InlineMarker.mark(4);
            new kotlin.coroutines.jvm.internal.ContinuationImpl(this, continuation) { // from class: kotlinx.coroutines.flow.internal.SafeCollector_commonKt$unsafeFlow$1$collect$1
                int label;
                /* synthetic */ java.lang.Object result;
                final /* synthetic */ kotlinx.coroutines.flow.internal.SafeCollector_commonKt.AnonymousClass1<T> this$0;

                {
                    this.this$0 = this;
                }

                /* JADX WARN: Type inference incomplete: some casts might be missing */
                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                public final java.lang.Object invokeSuspend(java.lang.Object obj) {
                    this.result = obj;
                    this.label |= Integer.MIN_VALUE;
                    return this.this$0.collect(null, this);
                }
            };
            kotlin.jvm.internal.InlineMarker.mark(5);
            this.$block.invoke(flowCollector, continuation);
            return kotlin.Unit.INSTANCE;
        }
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> unsafeFlow(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        return new kotlinx.coroutines.flow.internal.SafeCollector_commonKt.AnonymousClass1(block);
    }
}
