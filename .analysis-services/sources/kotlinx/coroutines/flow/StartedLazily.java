package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: SharingStarted.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0016J\b\u0010\t\u001a\u00020\nH\u0016¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/flow/StartedLazily;", "Lkotlinx/coroutines/flow/SharingStarted;", "()V", "command", "Lkotlinx/coroutines/flow/Flow;", "Lkotlinx/coroutines/flow/SharingCommand;", "subscriptionCount", "Lkotlinx/coroutines/flow/StateFlow;", "", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class StartedLazily implements kotlinx.coroutines.flow.SharingStarted {

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.StartedLazily$command$1, reason: invalid class name */
    /* JADX INFO: compiled from: SharingStarted.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlinx/coroutines/flow/SharingCommand;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.StartedLazily$command$1", f = "SharingStarted.kt", i = {}, l = {155}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super kotlinx.coroutines.flow.SharingCommand>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlinx.coroutines.flow.StateFlow<java.lang.Integer> $subscriptionCount;
        private /* synthetic */ java.lang.Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(kotlinx.coroutines.flow.StateFlow<java.lang.Integer> stateFlow, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.StartedLazily.AnonymousClass1> continuation) {
            super(2, continuation);
            this.$subscriptionCount = stateFlow;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.StartedLazily.AnonymousClass1 anonymousClass1 = new kotlinx.coroutines.flow.StartedLazily.AnonymousClass1(this.$subscriptionCount, continuation);
            anonymousClass1.L$0 = obj;
            return anonymousClass1;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super kotlinx.coroutines.flow.SharingCommand> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.StartedLazily.AnonymousClass1) create(flowCollector, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    kotlinx.coroutines.flow.FlowCollector $this$flow = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                    kotlin.jvm.internal.Ref.BooleanRef started = new kotlin.jvm.internal.Ref.BooleanRef();
                    this.label = 1;
                    if (this.$subscriptionCount.collect(new kotlinx.coroutines.flow.StartedLazily.AnonymousClass1.C00211(started, $this$flow), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    kotlin.ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            throw new kotlin.KotlinNothingValueException();
        }

        /* JADX INFO: renamed from: kotlinx.coroutines.flow.StartedLazily$command$1$1, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: compiled from: SharingStarted.kt */
        @kotlin.Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, "", "emit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 9, 0}, xi = 48)
        static final class C00211<T> implements kotlinx.coroutines.flow.FlowCollector {
            final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<kotlinx.coroutines.flow.SharingCommand> $$this$flow;
            final /* synthetic */ kotlin.jvm.internal.Ref.BooleanRef $started;

            /* JADX WARN: Multi-variable type inference failed */
            C00211(kotlin.jvm.internal.Ref.BooleanRef booleanRef, kotlinx.coroutines.flow.FlowCollector<? super kotlinx.coroutines.flow.SharingCommand> flowCollector) {
                this.$started = booleanRef;
                this.$$this$flow = flowCollector;
            }

            /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final java.lang.Object emit(int r6, kotlin.coroutines.Continuation<? super kotlin.Unit> r7) throws java.lang.Throwable {
                /*
                    r5 = this;
                    boolean r0 = r7 instanceof kotlinx.coroutines.flow.StartedLazily$command$1$1$emit$1
                    if (r0 == 0) goto L14
                    r0 = r7
                    kotlinx.coroutines.flow.StartedLazily$command$1$1$emit$1 r0 = (kotlinx.coroutines.flow.StartedLazily$command$1$1$emit$1) r0
                    int r1 = r0.label
                    r2 = -2147483648(0xffffffff80000000, float:-0.0)
                    r1 = r1 & r2
                    if (r1 == 0) goto L14
                    int r7 = r0.label
                    int r7 = r7 - r2
                    r0.label = r7
                    goto L19
                L14:
                    kotlinx.coroutines.flow.StartedLazily$command$1$1$emit$1 r0 = new kotlinx.coroutines.flow.StartedLazily$command$1$1$emit$1
                    r0.<init>(r5, r7)
                L19:
                    r7 = r0
                    java.lang.Object r0 = r7.result
                    java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                    int r2 = r7.label
                    switch(r2) {
                        case 0: goto L31;
                        case 1: goto L2d;
                        default: goto L25;
                    }
                L25:
                    java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
                    java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
                    r6.<init>(r7)
                    throw r6
                L2d:
                    kotlin.ResultKt.throwOnFailure(r0)
                    goto L4f
                L31:
                    kotlin.ResultKt.throwOnFailure(r0)
                    r2 = r5
                    if (r6 <= 0) goto L4f
                    kotlin.jvm.internal.Ref$BooleanRef r3 = r2.$started
                    boolean r3 = r3.element
                    if (r3 != 0) goto L4f
                    kotlin.jvm.internal.Ref$BooleanRef r6 = r2.$started
                    r3 = 1
                    r6.element = r3
                    kotlinx.coroutines.flow.FlowCollector<kotlinx.coroutines.flow.SharingCommand> r6 = r2.$$this$flow
                    kotlinx.coroutines.flow.SharingCommand r4 = kotlinx.coroutines.flow.SharingCommand.START
                    r7.label = r3
                    java.lang.Object r6 = r6.emit(r4, r7)
                    if (r6 != r1) goto L4f
                    return r1
                L4f:
                    kotlin.Unit r6 = kotlin.Unit.INSTANCE
                    return r6
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.StartedLazily.AnonymousClass1.C00211.emit(int, kotlin.coroutines.Continuation):java.lang.Object");
            }

            @Override // kotlinx.coroutines.flow.FlowCollector
            public /* bridge */ /* synthetic */ java.lang.Object emit(java.lang.Object value, kotlin.coroutines.Continuation $completion) {
                return emit(((java.lang.Number) value).intValue(), (kotlin.coroutines.Continuation<? super kotlin.Unit>) $completion);
            }
        }
    }

    @Override // kotlinx.coroutines.flow.SharingStarted
    public kotlinx.coroutines.flow.Flow<kotlinx.coroutines.flow.SharingCommand> command(kotlinx.coroutines.flow.StateFlow<java.lang.Integer> subscriptionCount) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(subscriptionCount, "subscriptionCount");
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.StartedLazily.AnonymousClass1(subscriptionCount, null));
    }

    public java.lang.String toString() {
        return "SharingStarted.Lazily";
    }
}
