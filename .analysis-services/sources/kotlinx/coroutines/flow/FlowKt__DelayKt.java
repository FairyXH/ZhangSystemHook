package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Delay.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u001a2\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00050\u0004H\u0007\u001a7\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00070\u0004H\u0007¢\u0006\u0002\b\b\u001a&\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0005H\u0007\u001a0\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a7\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00050\u0004H\u0002¢\u0006\u0002\b\r\u001a$\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u0005H\u0000\u001a&\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u0005H\u0007\u001a0\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\n\u001a0\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\n\u001a0\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0007H\u0002ø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\n\u0082\u0002\u0007\n\u0005\b¡\u001e0\u0001¨\u0006\u001b"}, d2 = {"debounce", "Lkotlinx/coroutines/flow/Flow;", "T", "timeoutMillis", "Lkotlin/Function1;", "", "timeout", "Lkotlin/time/Duration;", "debounceDuration", "debounce-HG0u8IE", "(Lkotlinx/coroutines/flow/Flow;J)Lkotlinx/coroutines/flow/Flow;", "debounceInternal", "timeoutMillisSelector", "debounceInternal$FlowKt__DelayKt", "fixedPeriodTicker", "Lkotlinx/coroutines/channels/ReceiveChannel;", "", "Lkotlinx/coroutines/CoroutineScope;", "delayMillis", "initialDelayMillis", "sample", "periodMillis", "period", "sample-HG0u8IE", "timeout-HG0u8IE", "timeoutInternal", "timeoutInternal-HG0u8IE$FlowKt__DelayKt", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__DelayKt {
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> kotlinx.coroutines.flow.Flow<T> debounce(kotlinx.coroutines.flow.Flow<? extends T> flow, final long timeoutMillis) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        if (timeoutMillis >= 0) {
            return timeoutMillis == 0 ? flow : debounceInternal$FlowKt__DelayKt(flow, new kotlin.jvm.functions.Function1<T, java.lang.Long>() { // from class: kotlinx.coroutines.flow.FlowKt__DelayKt.debounce.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(1);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // kotlin.jvm.functions.Function1
                public final java.lang.Long invoke(T t) {
                    return java.lang.Long.valueOf(timeoutMillis);
                }
            });
        }
        throw new java.lang.IllegalArgumentException("Debounce timeout should not be negative".toString());
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> debounce(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function1<? super T, java.lang.Long> timeoutMillis) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(timeoutMillis, "timeoutMillis");
        return debounceInternal$FlowKt__DelayKt(flow, timeoutMillis);
    }

    /* JADX INFO: renamed from: debounce-HG0u8IE, reason: not valid java name */
    public static final <T> kotlinx.coroutines.flow.Flow<T> m12842debounceHG0u8IE(kotlinx.coroutines.flow.Flow<? extends T> debounce, long timeout) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(debounce, "$this$debounce");
        return kotlinx.coroutines.flow.FlowKt.debounce(debounce, kotlinx.coroutines.DelayKt.m12799toDelayMillisLRDsOJo(timeout));
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> debounceDuration(kotlinx.coroutines.flow.Flow<? extends T> flow, final kotlin.jvm.functions.Function1<? super T, kotlin.time.Duration> timeout) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(timeout, "timeout");
        return debounceInternal$FlowKt__DelayKt(flow, new kotlin.jvm.functions.Function1<T, java.lang.Long>() { // from class: kotlinx.coroutines.flow.FlowKt__DelayKt.debounce.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function1
            public final java.lang.Long invoke(T t) {
                return java.lang.Long.valueOf(kotlinx.coroutines.DelayKt.m12799toDelayMillisLRDsOJo(timeout.invoke(t).getRawValue()));
            }
        });
    }

    private static final <T> kotlinx.coroutines.flow.Flow<T> debounceInternal$FlowKt__DelayKt(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function1<? super T, java.lang.Long> function1) {
        return kotlinx.coroutines.flow.internal.FlowCoroutineKt.scopedFlow(new kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1(function1, flow, null));
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Delay.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u008a@"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;", "downstream", "Lkotlinx/coroutines/flow/FlowCollector;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2", f = "Delay.kt", i = {0, 0, 0, 0}, l = {413}, m = "invokeSuspend", n = {"downstream", "values", "lastValue", "ticker"}, s = {"L$0", "L$1", "L$2", "L$3"})
    static final class C01602<T> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function3<kotlinx.coroutines.CoroutineScope, kotlinx.coroutines.flow.FlowCollector<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ long $periodMillis;
        final /* synthetic */ kotlinx.coroutines.flow.Flow<T> $this_sample;
        private /* synthetic */ java.lang.Object L$0;
        /* synthetic */ java.lang.Object L$1;
        java.lang.Object L$2;
        java.lang.Object L$3;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C01602(long j, kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__DelayKt.C01602> continuation) {
            super(3, continuation);
            this.$periodMillis = j;
            this.$this_sample = flow;
        }

        @Override // kotlin.jvm.functions.Function3
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            kotlinx.coroutines.flow.FlowKt__DelayKt.C01602 c01602 = new kotlinx.coroutines.flow.FlowKt__DelayKt.C01602(this.$periodMillis, this.$this_sample, continuation);
            c01602.L$0 = coroutineScope;
            c01602.L$1 = flowCollector;
            return c01602.invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            kotlinx.coroutines.flow.FlowKt__DelayKt.C01602<T> c01602;
            kotlinx.coroutines.channels.ReceiveChannel ticker;
            kotlinx.coroutines.flow.FlowCollector downstream;
            kotlinx.coroutines.channels.ReceiveChannel values;
            kotlin.jvm.internal.Ref.ObjectRef lastValue;
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    c01602 = this;
                    kotlinx.coroutines.CoroutineScope $this$scopedFlow = (kotlinx.coroutines.CoroutineScope) c01602.L$0;
                    kotlinx.coroutines.flow.FlowCollector downstream2 = (kotlinx.coroutines.flow.FlowCollector) c01602.L$1;
                    kotlinx.coroutines.channels.ReceiveChannel values2 = kotlinx.coroutines.channels.ProduceKt.produce$default($this$scopedFlow, null, -1, new kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$values$1(c01602.$this_sample, null), 1, null);
                    kotlin.jvm.internal.Ref.ObjectRef lastValue2 = new kotlin.jvm.internal.Ref.ObjectRef();
                    ticker = kotlinx.coroutines.flow.FlowKt__DelayKt.fixedPeriodTicker$default($this$scopedFlow, c01602.$periodMillis, 0L, 2, null);
                    downstream = downstream2;
                    values = values2;
                    lastValue = lastValue2;
                    break;
                case 1:
                    c01602 = this;
                    ticker = (kotlinx.coroutines.channels.ReceiveChannel) c01602.L$3;
                    lastValue = (kotlin.jvm.internal.Ref.ObjectRef) c01602.L$2;
                    values = (kotlinx.coroutines.channels.ReceiveChannel) c01602.L$1;
                    downstream = (kotlinx.coroutines.flow.FlowCollector) c01602.L$0;
                    kotlin.ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            while (lastValue.element != kotlinx.coroutines.flow.internal.NullSurrogateKt.DONE) {
                kotlinx.coroutines.selects.SelectImplementation $this$select_u24lambda_u241$iv = new kotlinx.coroutines.selects.SelectImplementation(c01602.get$context());
                kotlinx.coroutines.selects.SelectImplementation $this$invokeSuspend_u24lambda_u240 = $this$select_u24lambda_u241$iv;
                $this$invokeSuspend_u24lambda_u240.invoke(values.getOnReceiveCatching(), new kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$1(lastValue, ticker, null));
                $this$invokeSuspend_u24lambda_u240.invoke(ticker.getOnReceive(), new kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$2(lastValue, downstream, null));
                c01602.L$0 = downstream;
                c01602.L$1 = values;
                c01602.L$2 = lastValue;
                c01602.L$3 = ticker;
                c01602.label = 1;
                if ($this$select_u24lambda_u241$iv.doSelect(c01602) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
            return kotlin.Unit.INSTANCE;
        }
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> sample(kotlinx.coroutines.flow.Flow<? extends T> flow, long periodMillis) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        if (periodMillis > 0) {
            return kotlinx.coroutines.flow.internal.FlowCoroutineKt.scopedFlow(new kotlinx.coroutines.flow.FlowKt__DelayKt.C01602(periodMillis, flow, null));
        }
        throw new java.lang.IllegalArgumentException("Sample period should be positive".toString());
    }

    public static /* synthetic */ kotlinx.coroutines.channels.ReceiveChannel fixedPeriodTicker$default(kotlinx.coroutines.CoroutineScope coroutineScope, long j, long j2, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            j2 = j;
        }
        return kotlinx.coroutines.flow.FlowKt.fixedPeriodTicker(coroutineScope, j, j2);
    }

    public static final kotlinx.coroutines.channels.ReceiveChannel<kotlin.Unit> fixedPeriodTicker(kotlinx.coroutines.CoroutineScope $this$fixedPeriodTicker, long delayMillis, long initialDelayMillis) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$fixedPeriodTicker, "<this>");
        if (!(delayMillis >= 0)) {
            throw new java.lang.IllegalArgumentException(("Expected non-negative delay, but has " + delayMillis + " ms").toString());
        }
        if (!(initialDelayMillis >= 0)) {
            throw new java.lang.IllegalArgumentException(("Expected non-negative initial delay, but has " + initialDelayMillis + " ms").toString());
        }
        return kotlinx.coroutines.channels.ProduceKt.produce$default($this$fixedPeriodTicker, null, 0, new kotlinx.coroutines.flow.FlowKt__DelayKt.C01593(initialDelayMillis, delayMillis, null), 1, null);
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__DelayKt$fixedPeriodTicker$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Delay.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/channels/ProducerScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$fixedPeriodTicker$3", f = "Delay.kt", i = {0, 1, 2}, l = {com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_MEDIA_BUTTON, com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_EVENT_MMS, com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_SHELL}, m = "invokeSuspend", n = {"$this$produce", "$this$produce", "$this$produce"}, s = {"L$0", "L$0", "L$0"})
    static final class C01593 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.channels.ProducerScope<? super kotlin.Unit>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ long $delayMillis;
        final /* synthetic */ long $initialDelayMillis;
        private /* synthetic */ java.lang.Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01593(long j, long j2, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__DelayKt.C01593> continuation) {
            super(2, continuation);
            this.$initialDelayMillis = j;
            this.$delayMillis = j2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.FlowKt__DelayKt.C01593 c01593 = new kotlinx.coroutines.flow.FlowKt__DelayKt.C01593(this.$initialDelayMillis, this.$delayMillis, continuation);
            c01593.L$0 = obj;
            return c01593;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.channels.ProducerScope<? super kotlin.Unit> producerScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__DelayKt.C01593) create(producerScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x005a A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:18:0x006b A[RETURN] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x0069 -> B:13:0x0046). Please report as a decompilation issue!!! */
        /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
            	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
            	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
            */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r8) {
            /*
                r7 = this;
                java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r1 = r7.label
                switch(r1) {
                    case 0: goto L2c;
                    case 1: goto L23;
                    case 2: goto L1a;
                    case 3: goto L11;
                    default: goto L9;
                }
            L9:
                java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
                java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                r8.<init>(r0)
                throw r8
            L11:
                r1 = r7
                java.lang.Object r2 = r1.L$0
                kotlinx.coroutines.channels.ProducerScope r2 = (kotlinx.coroutines.channels.ProducerScope) r2
                kotlin.ResultKt.throwOnFailure(r8)
                goto L6c
            L1a:
                r1 = r7
                java.lang.Object r2 = r1.L$0
                kotlinx.coroutines.channels.ProducerScope r2 = (kotlinx.coroutines.channels.ProducerScope) r2
                kotlin.ResultKt.throwOnFailure(r8)
                goto L5b
            L23:
                r1 = r7
                java.lang.Object r2 = r1.L$0
                kotlinx.coroutines.channels.ProducerScope r2 = (kotlinx.coroutines.channels.ProducerScope) r2
                kotlin.ResultKt.throwOnFailure(r8)
                goto L45
            L2c:
                kotlin.ResultKt.throwOnFailure(r8)
                r1 = r7
                java.lang.Object r2 = r1.L$0
                kotlinx.coroutines.channels.ProducerScope r2 = (kotlinx.coroutines.channels.ProducerScope) r2
                long r3 = r1.$initialDelayMillis
                r5 = r1
                kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                r1.L$0 = r2
                r6 = 1
                r1.label = r6
                java.lang.Object r3 = kotlinx.coroutines.DelayKt.delay(r3, r5)
                if (r3 != r0) goto L45
                return r0
            L45:
            L46:
                kotlinx.coroutines.channels.SendChannel r3 = r2.getChannel()
                kotlin.Unit r4 = kotlin.Unit.INSTANCE
                r5 = r1
                kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                r1.L$0 = r2
                r6 = 2
                r1.label = r6
                java.lang.Object r3 = r3.send(r4, r5)
                if (r3 != r0) goto L5b
                return r0
            L5b:
                long r3 = r1.$delayMillis
                r5 = r1
                kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                r1.L$0 = r2
                r6 = 3
                r1.label = r6
                java.lang.Object r3 = kotlinx.coroutines.DelayKt.delay(r3, r5)
                if (r3 != r0) goto L6c
                return r0
            L6c:
                goto L46
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__DelayKt.C01593.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* JADX INFO: renamed from: sample-HG0u8IE, reason: not valid java name */
    public static final <T> kotlinx.coroutines.flow.Flow<T> m12843sampleHG0u8IE(kotlinx.coroutines.flow.Flow<? extends T> sample, long period) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sample, "$this$sample");
        return kotlinx.coroutines.flow.FlowKt.sample(sample, kotlinx.coroutines.DelayKt.m12799toDelayMillisLRDsOJo(period));
    }

    /* JADX INFO: renamed from: timeout-HG0u8IE, reason: not valid java name */
    public static final <T> kotlinx.coroutines.flow.Flow<T> m12844timeoutHG0u8IE(kotlinx.coroutines.flow.Flow<? extends T> timeout, long timeout2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(timeout, "$this$timeout");
        return m12845timeoutInternalHG0u8IE$FlowKt__DelayKt(timeout, timeout2);
    }

    /* JADX INFO: renamed from: timeoutInternal-HG0u8IE$FlowKt__DelayKt, reason: not valid java name */
    private static final <T> kotlinx.coroutines.flow.Flow<T> m12845timeoutInternalHG0u8IE$FlowKt__DelayKt(kotlinx.coroutines.flow.Flow<? extends T> flow, long timeout) {
        return kotlinx.coroutines.flow.internal.FlowCoroutineKt.scopedFlow(new kotlinx.coroutines.flow.FlowKt__DelayKt$timeoutInternal$1(timeout, flow, null));
    }
}
