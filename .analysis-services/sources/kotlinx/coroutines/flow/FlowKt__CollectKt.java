package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Collect.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0016\u0010\u0000\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0002H\u0086@¢\u0006\u0002\u0010\u0003\u001aS\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u000223\b\u0004\u0010\u0005\u001a-\b\u0001\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0006H\u0087H¢\u0006\u0002\u0010\f\u001ah\u0010\r\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00022H\b\u0004\u0010\u0005\u001aB\b\u0001\u0012\u0013\u0012\u00110\u000f¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u000eH\u0086H¢\u0006\u0002\u0010\u0011\u001aQ\u0010\u0012\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u000221\u0010\u0005\u001a-\b\u0001\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0006H\u0086@¢\u0006\u0002\u0010\f\u001a,\u0010\u0013\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0002H\u0086@¢\u0006\u0002\u0010\u0016\u001a\u001e\u0010\u0017\u001a\u00020\u0018\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00022\u0006\u0010\u0019\u001a\u00020\u001a¨\u0006\u001b"}, d2 = {"collect", "", "Lkotlinx/coroutines/flow/Flow;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "T", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "value", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "collectIndexed", "Lkotlin/Function3;", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "collectLatest", "emitAll", "Lkotlinx/coroutines/flow/FlowCollector;", "flow", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/flow/Flow;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "launchIn", "Lkotlinx/coroutines/Job;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__CollectKt {
    public static final java.lang.Object collect(kotlinx.coroutines.flow.Flow<?> flow, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objCollect = flow.collect(kotlinx.coroutines.flow.internal.NopCollector.INSTANCE, continuation);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__CollectKt$launchIn$1, reason: invalid class name */
    /* JADX INFO: compiled from: Collect.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__CollectKt$launchIn$1", f = "Collect.kt", i = {}, l = {50}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlinx.coroutines.flow.Flow<T> $this_launchIn;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass1(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass1> continuation) {
            super(2, continuation);
            this.$this_launchIn = flow;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return new kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass1(this.$this_launchIn, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (kotlinx.coroutines.flow.FlowKt.collect(this.$this_launchIn, this) == coroutine_suspended) {
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

    public static final <T> kotlinx.coroutines.Job launchIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        return kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default(scope, null, null, new kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass1(flow, null), 3, null);
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__CollectKt$collectIndexed$2, reason: invalid class name */
    /* JADX INFO: compiled from: Collect.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0016\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"kotlinx/coroutines/flow/FlowKt__CollectKt$collectIndexed$2", "Lkotlinx/coroutines/flow/FlowCollector;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass2<T> implements kotlinx.coroutines.flow.FlowCollector<T> {
        final /* synthetic */ kotlin.jvm.functions.Function3<java.lang.Integer, T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $action;
        private int index;

        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass2(kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3) {
            this.$action = function3;
        }

        @Override // kotlinx.coroutines.flow.FlowCollector
        public java.lang.Object emit(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            kotlin.jvm.functions.Function3<java.lang.Integer, T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> function3 = this.$action;
            int index$iv = this.index;
            this.index = index$iv + 1;
            if (index$iv >= 0) {
                java.lang.Object objInvoke = function3.invoke(kotlin.coroutines.jvm.internal.Boxing.boxInt(index$iv), t, continuation);
                return objInvoke == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objInvoke : kotlin.Unit.INSTANCE;
            }
            throw new java.lang.ArithmeticException("Index overflow has happened");
        }

        public java.lang.Object emit$$forInline(T t, final kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            kotlin.jvm.internal.InlineMarker.mark(4);
            new kotlin.coroutines.jvm.internal.ContinuationImpl(this, continuation) { // from class: kotlinx.coroutines.flow.FlowKt__CollectKt$collectIndexed$2$emit$1
                int label;
                /* synthetic */ java.lang.Object result;
                final /* synthetic */ kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass2<T> this$0;

                {
                    this.this$0 = this;
                }

                /* JADX WARN: Type inference incomplete: some casts might be missing */
                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                public final java.lang.Object invokeSuspend(java.lang.Object obj) {
                    this.result = obj;
                    this.label |= Integer.MIN_VALUE;
                    return this.this$0.emit(null, this);
                }
            };
            kotlin.jvm.internal.InlineMarker.mark(5);
            kotlin.jvm.functions.Function3<java.lang.Integer, T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> function3 = this.$action;
            int index$iv = this.index;
            this.index = index$iv + 1;
            if (index$iv >= 0) {
                function3.invoke(java.lang.Integer.valueOf(index$iv), t, continuation);
                return kotlin.Unit.INSTANCE;
            }
            throw new java.lang.ArithmeticException("Index overflow has happened");
        }
    }

    public static final <T> java.lang.Object collectIndexed(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass2(function3), continuation);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }

    private static final <T> java.lang.Object collectIndexed$$forInline(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass2(function3);
        kotlin.jvm.internal.InlineMarker.mark(0);
        flow.collect(anonymousClass2, continuation);
        kotlin.jvm.internal.InlineMarker.mark(1);
        return kotlin.Unit.INSTANCE;
    }

    public static final <T> java.lang.Object collectLatest(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objCollect = kotlinx.coroutines.flow.FlowKt.collect(kotlinx.coroutines.flow.FlowKt__ContextKt.buffer$default(kotlinx.coroutines.flow.FlowKt.mapLatest(flow, function2), 0, null, 2, null), continuation);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }

    public static final <T> java.lang.Object emitAll(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.flow.FlowKt.ensureActive(flowCollector);
        java.lang.Object objCollect = flow.collect(flowCollector, continuation);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__CollectKt$collect$3, reason: invalid class name */
    /* JADX INFO: compiled from: Collect.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, d2 = {"kotlinx/coroutines/flow/FlowKt__CollectKt$collect$3", "Lkotlinx/coroutines/flow/FlowCollector;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass3<T> implements kotlinx.coroutines.flow.FlowCollector<T> {
        final /* synthetic */ kotlin.jvm.functions.Function2<T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $action;

        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass3(kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
            this.$action = function2;
        }

        @Override // kotlinx.coroutines.flow.FlowCollector
        public java.lang.Object emit(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            java.lang.Object objInvoke = this.$action.invoke(t, continuation);
            return objInvoke == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objInvoke : kotlin.Unit.INSTANCE;
        }

        public java.lang.Object emit$$forInline(T t, final kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            kotlin.jvm.internal.InlineMarker.mark(4);
            new kotlin.coroutines.jvm.internal.ContinuationImpl(this, continuation) { // from class: kotlinx.coroutines.flow.FlowKt__CollectKt$collect$3$emit$1
                int label;
                /* synthetic */ java.lang.Object result;
                final /* synthetic */ kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass3<T> this$0;

                {
                    this.this$0 = this;
                }

                /* JADX WARN: Type inference incomplete: some casts might be missing */
                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                public final java.lang.Object invokeSuspend(java.lang.Object obj) {
                    this.result = obj;
                    this.label |= Integer.MIN_VALUE;
                    return this.this$0.emit(null, this);
                }
            };
            kotlin.jvm.internal.InlineMarker.mark(5);
            this.$action.invoke(t, continuation);
            return kotlin.Unit.INSTANCE;
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Backwards compatibility with JS and K/N")
    public static final /* synthetic */ <T> java.lang.Object collect(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass3(function2), continuation);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Backwards compatibility with JS and K/N")
    private static final /* synthetic */ <T> java.lang.Object collect$$forInline(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass3 anonymousClass3 = new kotlinx.coroutines.flow.FlowKt__CollectKt.AnonymousClass3(function2);
        kotlin.jvm.internal.InlineMarker.mark(0);
        flow.collect(anonymousClass3, continuation);
        kotlin.jvm.internal.InlineMarker.mark(1);
        return kotlin.Unit.INSTANCE;
    }
}
