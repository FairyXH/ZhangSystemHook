package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Limit.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aS\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000323\b\u0004\u0010\u0004\u001a-\b\u0001\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0005H\u0080H¢\u0006\u0002\u0010\f\u001a$\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000f\u001aE\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\"\u0010\u0004\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0005¢\u0006\u0002\u0010\u0011\u001a(\u0010\u0012\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00132\u0006\u0010\b\u001a\u0002H\u0002H\u0082@¢\u0006\u0004\b\u0014\u0010\u0015\u001a$\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000f\u001aE\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\"\u0010\u0004\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0005¢\u0006\u0002\u0010\u0011\u001am\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00190\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0019*\b\u0012\u0004\u0012\u0002H\u00020\u00032D\b\u0001\u0010\u001a\u001a>\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00190\u0013\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u001b¢\u0006\u0002\b\u001c¢\u0006\u0002\u0010\u001d¨\u0006\u001e"}, d2 = {"collectWhile", "", "T", "Lkotlinx/coroutines/flow/Flow;", "predicate", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "value", "Lkotlin/coroutines/Continuation;", "", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "drop", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, "", "dropWhile", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "emitAbort", "Lkotlinx/coroutines/flow/FlowCollector;", "emitAbort$FlowKt__LimitKt", "(Lkotlinx/coroutines/flow/FlowCollector;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "take", "takeWhile", "transformWhile", "R", "transform", "Lkotlin/Function3;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__LimitKt {

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$1, reason: invalid class name */
    /* JADX INFO: compiled from: Limit.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 176)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__LimitKt", f = "Limit.kt", i = {0}, l = {136}, m = "collectWhile", n = {"collector"}, s = {"L$0"})
    static final class AnonymousClass1<T> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        int label;
        /* synthetic */ java.lang.Object result;

        AnonymousClass1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__LimitKt.AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.flow.FlowKt__LimitKt.collectWhile(null, null, this);
        }
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> drop(final kotlinx.coroutines.flow.Flow<? extends T> flow, final int count) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        if (!(count >= 0)) {
            throw new java.lang.IllegalArgumentException(("Drop count should be non-negative, but had " + count).toString());
        }
        return new kotlinx.coroutines.flow.Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__LimitKt$drop$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                kotlin.jvm.internal.Ref.IntRef skipped = new kotlin.jvm.internal.Ref.IntRef();
                java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__LimitKt$drop$2$1(skipped, count, flowCollector), continuation);
                return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> dropWhile(final kotlinx.coroutines.flow.Flow<? extends T> flow, final kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return new kotlinx.coroutines.flow.Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                kotlin.jvm.internal.Ref.BooleanRef matched = new kotlin.jvm.internal.Ref.BooleanRef();
                java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1(matched, flowCollector, predicate), continuation);
                return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> take(kotlinx.coroutines.flow.Flow<? extends T> flow, int count) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        if (!(count > 0)) {
            throw new java.lang.IllegalArgumentException(("Requested element count " + count + " should be positive").toString());
        }
        return new kotlinx.coroutines.flow.FlowKt__LimitKt$take$$inlined$unsafeFlow$1(flow, count);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T> java.lang.Object emitAbort$FlowKt__LimitKt(kotlinx.coroutines.flow.FlowCollector<? super T> r3, T r4, kotlin.coroutines.Continuation<? super kotlin.Unit> r5) throws java.lang.Throwable {
        /*
            boolean r0 = r5 instanceof kotlinx.coroutines.flow.FlowKt__LimitKt$emitAbort$1
            if (r0 == 0) goto L14
            r0 = r5
            kotlinx.coroutines.flow.FlowKt__LimitKt$emitAbort$1 r0 = (kotlinx.coroutines.flow.FlowKt__LimitKt$emitAbort$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r5 = r0.label
            int r5 = r5 - r2
            r0.label = r5
            goto L19
        L14:
            kotlinx.coroutines.flow.FlowKt__LimitKt$emitAbort$1 r0 = new kotlinx.coroutines.flow.FlowKt__LimitKt$emitAbort$1
            r0.<init>(r5)
        L19:
            r5 = r0
            java.lang.Object r0 = r5.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r5.label
            switch(r2) {
                case 0: goto L35;
                case 1: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException
            java.lang.String r4 = "call to 'resume' before 'invoke' with coroutine"
            r3.<init>(r4)
            throw r3
        L2d:
            java.lang.Object r3 = r5.L$0
            kotlinx.coroutines.flow.FlowCollector r3 = (kotlinx.coroutines.flow.FlowCollector) r3
            kotlin.ResultKt.throwOnFailure(r0)
            goto L44
        L35:
            kotlin.ResultKt.throwOnFailure(r0)
            r5.L$0 = r3
            r2 = 1
            r5.label = r2
            java.lang.Object r4 = r3.emit(r4, r5)
            if (r4 != r1) goto L44
            return r1
        L44:
            kotlinx.coroutines.flow.internal.AbortFlowException r4 = new kotlinx.coroutines.flow.internal.AbortFlowException
            r4.<init>(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__LimitKt.emitAbort$FlowKt__LimitKt(kotlinx.coroutines.flow.FlowCollector, java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> takeWhile(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return new kotlinx.coroutines.flow.FlowKt__LimitKt$takeWhile$$inlined$unsafeFlow$1(flow, predicate);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__LimitKt$transformWhile$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Limit.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__LimitKt$transformWhile$1", f = "Limit.kt", i = {0}, l = {152}, m = "invokeSuspend", n = {"collector$iv"}, s = {"L$0"})
    static final class C01621<R> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super R>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlinx.coroutines.flow.Flow<T> $this_transformWhile;
        final /* synthetic */ kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T, kotlin.coroutines.Continuation<? super java.lang.Boolean>, java.lang.Object> $transform;
        private /* synthetic */ java.lang.Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C01621(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__LimitKt.C01621> continuation) {
            super(2, continuation);
            this.$this_transformWhile = flow;
            this.$transform = function3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.FlowKt__LimitKt.C01621 c01621 = new kotlinx.coroutines.flow.FlowKt__LimitKt.C01621(this.$this_transformWhile, this.$transform, continuation);
            c01621.L$0 = obj;
            return c01621;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__LimitKt.C01621) create(flowCollector, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            kotlinx.coroutines.flow.FlowKt__LimitKt$transformWhile$1$invokeSuspend$$inlined$collectWhile$1 collector$iv;
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    kotlinx.coroutines.flow.FlowCollector $this$flow = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                    kotlinx.coroutines.flow.Flow $this$collectWhile$iv = this.$this_transformWhile;
                    kotlinx.coroutines.flow.FlowKt__LimitKt$transformWhile$1$invokeSuspend$$inlined$collectWhile$1 collector$iv2 = new kotlinx.coroutines.flow.FlowKt__LimitKt$transformWhile$1$invokeSuspend$$inlined$collectWhile$1(this.$transform, $this$flow);
                    try {
                        this.L$0 = collector$iv2;
                        this.label = 1;
                        break;
                    } catch (kotlinx.coroutines.flow.internal.AbortFlowException e) {
                        e$iv = e;
                        collector$iv = collector$iv2;
                        kotlinx.coroutines.flow.internal.FlowExceptions_commonKt.checkOwnership(e$iv, collector$iv);
                    }
                    return $this$collectWhile$iv.collect(collector$iv2, this) == coroutine_suspended ? coroutine_suspended : kotlin.Unit.INSTANCE;
                case 1:
                    collector$iv = (kotlinx.coroutines.flow.FlowKt__LimitKt$transformWhile$1$invokeSuspend$$inlined$collectWhile$1) this.L$0;
                    try {
                        kotlin.ResultKt.throwOnFailure($result);
                    } catch (kotlinx.coroutines.flow.internal.AbortFlowException e2) {
                        e$iv = e2;
                        kotlinx.coroutines.flow.internal.FlowExceptions_commonKt.checkOwnership(e$iv, collector$iv);
                    }
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> transformWhile(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__LimitKt.C01621(flow, transform, null));
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T> java.lang.Object collectWhile(kotlinx.coroutines.flow.Flow<? extends T> r5, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> r6, kotlin.coroutines.Continuation<? super kotlin.Unit> r7) throws java.lang.Throwable {
        /*
            boolean r0 = r7 instanceof kotlinx.coroutines.flow.FlowKt__LimitKt.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r7
            kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$1 r0 = (kotlinx.coroutines.flow.FlowKt__LimitKt.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r7 = r0.label
            int r7 = r7 - r2
            r0.label = r7
            goto L19
        L14:
            kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$1 r0 = new kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$1
            r0.<init>(r7)
        L19:
            r7 = r0
            java.lang.Object r0 = r7.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r7.label
            switch(r2) {
                case 0: goto L38;
                case 1: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L2d:
            r5 = 0
            java.lang.Object r6 = r7.L$0
            kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1 r6 = (kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1) r6
            kotlin.ResultKt.throwOnFailure(r0)     // Catch: kotlinx.coroutines.flow.internal.AbortFlowException -> L36
            goto L53
        L36:
            r1 = move-exception
            goto L57
        L38:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = 0
            kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1 r3 = new kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1
            r3.<init>(r6)
            r6 = r3
            kotlinx.coroutines.flow.FlowCollector r6 = (kotlinx.coroutines.flow.FlowCollector) r6     // Catch: kotlinx.coroutines.flow.internal.AbortFlowException -> L54
            r7.L$0 = r3     // Catch: kotlinx.coroutines.flow.internal.AbortFlowException -> L54
            r4 = 1
            r7.label = r4     // Catch: kotlinx.coroutines.flow.internal.AbortFlowException -> L54
            java.lang.Object r6 = r5.collect(r6, r7)     // Catch: kotlinx.coroutines.flow.internal.AbortFlowException -> L54
            if (r6 != r1) goto L51
            return r1
        L51:
            r5 = r2
            r6 = r3
        L53:
            goto L5d
        L54:
            r1 = move-exception
            r5 = r2
            r6 = r3
        L57:
            r2 = r6
            kotlinx.coroutines.flow.FlowCollector r2 = (kotlinx.coroutines.flow.FlowCollector) r2
            kotlinx.coroutines.flow.internal.FlowExceptions_commonKt.checkOwnership(r1, r2)
        L5d:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__LimitKt.collectWhile(kotlinx.coroutines.flow.Flow, kotlin.jvm.functions.Function2, kotlin.coroutines.Continuation):java.lang.Object");
    }

    private static final <T> java.lang.Object collectWhile$$forInline(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1 collector = new kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1(function2);
        try {
            kotlin.jvm.internal.InlineMarker.mark(0);
            flow.collect(collector, continuation);
            kotlin.jvm.internal.InlineMarker.mark(1);
        } catch (kotlinx.coroutines.flow.internal.AbortFlowException e) {
            kotlinx.coroutines.flow.internal.FlowExceptions_commonKt.checkOwnership(e, collector);
        }
        return kotlin.Unit.INSTANCE;
    }
}
