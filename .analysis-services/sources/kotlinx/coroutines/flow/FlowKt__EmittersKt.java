package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Emitters.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u0010\u0010\u0000\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0002H\u0000\u001ap\u0010\u0003\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00022D\u0010\u0005\u001a@\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u0002\u0012\u0015\u0012\u0013\u0018\u00010\u0007¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0006¢\u0006\u0002\b\r2\b\u0010\n\u001a\u0004\u0018\u00010\u0007H\u0082@¢\u0006\u0004\b\u000e\u0010\u000f\u001ag\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0011\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00112D\u0010\u0005\u001a@\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u0002\u0012\u0015\u0012\u0013\u0018\u00010\u0007¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0006¢\u0006\u0002\b\r¢\u0006\u0002\u0010\u0012\u001aP\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0011\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00112-\u0010\u0005\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0014¢\u0006\u0002\b\r¢\u0006\u0002\u0010\u0015\u001aP\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0011\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00112-\u0010\u0005\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0014¢\u0006\u0002\b\r¢\u0006\u0002\u0010\u0015\u001ap\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0011\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0018*\b\u0012\u0004\u0012\u0002H\u00040\u00112D\b\u0005\u0010\u0017\u001a>\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180\u0002\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0006¢\u0006\u0002\b\rH\u0086\b¢\u0006\u0002\u0010\u0012\u001ap\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0011\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0018*\b\u0012\u0004\u0012\u0002H\u00040\u00112D\b\u0005\u0010\u0017\u001a>\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180\u0002\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0006¢\u0006\u0002\b\rH\u0081\b¢\u0006\u0002\u0010\u0012¨\u0006\u001b"}, d2 = {"ensureActive", "", "Lkotlinx/coroutines/flow/FlowCollector;", "invokeSafely", "T", "action", "Lkotlin/Function3;", "", "Lkotlin/ParameterName;", "name", "cause", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "invokeSafely$FlowKt__EmittersKt", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/jvm/functions/Function3;Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onCompletion", "Lkotlinx/coroutines/flow/Flow;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "onEmpty", "Lkotlin/Function2;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "onStart", "transform", "R", "value", "unsafeTransform", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__EmittersKt {

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__EmittersKt$transform$1, reason: invalid class name */
    /* JADX INFO: compiled from: Emitters.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;"}, k = 3, mv = {1, 9, 0}, xi = 176)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__EmittersKt$transform$1", f = "Emitters.kt", i = {}, l = {40}, m = "invokeSuspend", n = {}, s = {})
    public static final class AnonymousClass1<R> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super R>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlinx.coroutines.flow.Flow<T> $this_transform;
        final /* synthetic */ kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $transform;
        private /* synthetic */ java.lang.Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass1(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1> continuation) {
            super(2, continuation);
            this.$this_transform = flow;
            this.$transform = function3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1 anonymousClass1 = new kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1(this.$this_transform, this.$transform, continuation);
            anonymousClass1.L$0 = obj;
            return anonymousClass1;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1) create(flowCollector, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__EmittersKt$transform$1$1, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: compiled from: Emitters.kt */
        @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 9, 0}, xi = 176)
        public static final class C00201<T> implements kotlinx.coroutines.flow.FlowCollector {
            final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<R> $$this$flow;
            final /* synthetic */ kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $transform;

            /* JADX WARN: Multi-variable type inference failed */
            public C00201(kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector) {
                this.$transform = function3;
                this.$$this$flow = flowCollector;
            }

            /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
            /* JADX WARN: Type inference incomplete: some casts might be missing */
            @Override // kotlinx.coroutines.flow.FlowCollector
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final java.lang.Object emit(T r7, kotlin.coroutines.Continuation<? super kotlin.Unit> r8) throws java.lang.Throwable {
                /*
                    r6 = this;
                    boolean r0 = r8 instanceof kotlinx.coroutines.flow.FlowKt__EmittersKt$transform$1$1$emit$1
                    if (r0 == 0) goto L14
                    r0 = r8
                    kotlinx.coroutines.flow.FlowKt__EmittersKt$transform$1$1$emit$1 r0 = (kotlinx.coroutines.flow.FlowKt__EmittersKt$transform$1$1$emit$1) r0
                    int r1 = r0.label
                    r2 = -2147483648(0xffffffff80000000, float:-0.0)
                    r1 = r1 & r2
                    if (r1 == 0) goto L14
                    int r8 = r0.label
                    int r8 = r8 - r2
                    r0.label = r8
                    goto L19
                L14:
                    kotlinx.coroutines.flow.FlowKt__EmittersKt$transform$1$1$emit$1 r0 = new kotlinx.coroutines.flow.FlowKt__EmittersKt$transform$1$1$emit$1
                    r0.<init>(r6, r8)
                L19:
                    r8 = r0
                    java.lang.Object r0 = r8.result
                    java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                    int r2 = r8.label
                    switch(r2) {
                        case 0: goto L31;
                        case 1: goto L2d;
                        default: goto L25;
                    }
                L25:
                    java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
                    java.lang.String r8 = "call to 'resume' before 'invoke' with coroutine"
                    r7.<init>(r8)
                    throw r7
                L2d:
                    kotlin.ResultKt.throwOnFailure(r0)
                    goto L43
                L31:
                    kotlin.ResultKt.throwOnFailure(r0)
                    r2 = r6
                    kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> r3 = r2.$transform
                    kotlinx.coroutines.flow.FlowCollector<R> r4 = r2.$$this$flow
                    r5 = 1
                    r8.label = r5
                    java.lang.Object r7 = r3.invoke(r4, r7, r8)
                    if (r7 != r1) goto L43
                    return r1
                L43:
                    kotlin.Unit r7 = kotlin.Unit.INSTANCE
                    return r7
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1.C00201.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
            }

            /* JADX WARN: Type inference incomplete: some casts might be missing */
            public final java.lang.Object emit$$forInline(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                kotlin.jvm.internal.InlineMarker.mark(4);
                new kotlinx.coroutines.flow.FlowKt__EmittersKt$transform$1$1$emit$1(this, continuation);
                kotlin.jvm.internal.InlineMarker.mark(5);
                this.$transform.invoke((kotlinx.coroutines.flow.FlowCollector<? super R>) this.$$this$flow, t, continuation);
                return kotlin.Unit.INSTANCE;
            }
        }

        /* JADX WARN: Type inference incomplete: some casts might be missing */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure(obj);
                    kotlinx.coroutines.flow.FlowCollector flowCollector = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                    kotlinx.coroutines.flow.Flow<T> flow = this.$this_transform;
                    kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1.C00201 c00201 = new kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1.C00201(this.$transform, flowCollector);
                    this.label = 1;
                    if (flow.collect(c00201, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    kotlin.ResultKt.throwOnFailure(obj);
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return kotlin.Unit.INSTANCE;
        }

        /* JADX WARN: Type inference incomplete: some casts might be missing */
        public final java.lang.Object invokeSuspend$$forInline(java.lang.Object obj) {
            kotlinx.coroutines.flow.FlowCollector flowCollector = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
            kotlinx.coroutines.flow.Flow<T> flow = this.$this_transform;
            kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1.C00201 c00201 = new kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1.C00201(this.$transform, flowCollector);
            kotlin.jvm.internal.InlineMarker.mark(0);
            flow.collect(c00201, this);
            kotlin.jvm.internal.InlineMarker.mark(1);
            return kotlin.Unit.INSTANCE;
        }
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> transform(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__EmittersKt.AnonymousClass1(flow, transform, null));
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> unsafeTransform(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return new kotlinx.coroutines.flow.FlowKt__EmittersKt$unsafeTransform$$inlined$unsafeFlow$1(flow, transform);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> onStart(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return new kotlinx.coroutines.flow.FlowKt__EmittersKt$onStart$$inlined$unsafeFlow$1(action, flow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> onCompletion(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super java.lang.Throwable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return new kotlinx.coroutines.flow.FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1(flow, action);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> onEmpty(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return new kotlinx.coroutines.flow.FlowKt__EmittersKt$onEmpty$$inlined$unsafeFlow$1(flow, action);
    }

    public static final void ensureActive(kotlinx.coroutines.flow.FlowCollector<?> flowCollector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flowCollector, "<this>");
        if (flowCollector instanceof kotlinx.coroutines.flow.ThrowingCollector) {
            throw ((kotlinx.coroutines.flow.ThrowingCollector) flowCollector).e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T> java.lang.Object invokeSafely$FlowKt__EmittersKt(kotlinx.coroutines.flow.FlowCollector<? super T> r3, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super java.lang.Throwable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> r4, java.lang.Throwable r5, kotlin.coroutines.Continuation<? super kotlin.Unit> r6) throws java.lang.Throwable {
        /*
            boolean r0 = r6 instanceof kotlinx.coroutines.flow.FlowKt__EmittersKt$invokeSafely$1
            if (r0 == 0) goto L14
            r0 = r6
            kotlinx.coroutines.flow.FlowKt__EmittersKt$invokeSafely$1 r0 = (kotlinx.coroutines.flow.FlowKt__EmittersKt$invokeSafely$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r6 = r0.label
            int r6 = r6 - r2
            r0.label = r6
            goto L19
        L14:
            kotlinx.coroutines.flow.FlowKt__EmittersKt$invokeSafely$1 r0 = new kotlinx.coroutines.flow.FlowKt__EmittersKt$invokeSafely$1
            r0.<init>(r6)
        L19:
            r6 = r0
            java.lang.Object r0 = r6.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r6.label
            switch(r2) {
                case 0: goto L37;
                case 1: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException
            java.lang.String r4 = "call to 'resume' before 'invoke' with coroutine"
            r3.<init>(r4)
            throw r3
        L2d:
            java.lang.Object r3 = r6.L$0
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            kotlin.ResultKt.throwOnFailure(r0)     // Catch: java.lang.Throwable -> L35
            goto L48
        L35:
            r4 = move-exception
            goto L4e
        L37:
            kotlin.ResultKt.throwOnFailure(r0)
            r6.L$0 = r5     // Catch: java.lang.Throwable -> L4c
            r2 = 1
            r6.label = r2     // Catch: java.lang.Throwable -> L4c
            java.lang.Object r2 = r4.invoke(r3, r5, r6)     // Catch: java.lang.Throwable -> L4c
            if (r2 != r1) goto L47
            return r1
        L47:
            r3 = r5
        L48:
            kotlin.Unit r3 = kotlin.Unit.INSTANCE
            return r3
        L4c:
            r4 = move-exception
            r3 = r5
        L4e:
            if (r3 == 0) goto L57
            if (r3 == r4) goto L57
            r5 = r4
            r1 = 0
            kotlin.ExceptionsKt.addSuppressed(r5, r3)
        L57:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt(kotlinx.coroutines.flow.FlowCollector, kotlin.jvm.functions.Function3, java.lang.Throwable, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
