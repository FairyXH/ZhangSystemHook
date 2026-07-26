package com.android.server.bluetooth;

/* JADX INFO: compiled from: AdapterState.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0006\u001a\u00020\u0005J\u0012\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u00020\n\"\u00020\u0005J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0005J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u001a\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00122\n\u0010\t\u001a\u00020\n\"\u00020\u0005J'\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00132\n\u0010\t\u001a\u00020\n\"\u00020\u0005H\u0086@ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b¡\u001e0\u0001¨\u0006\u0016"}, d2 = {"Lcom/android/server/bluetooth/BluetoothAdapterState;", "", "()V", "_uiState", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "", "get", "oneOf", "", "states", "", "set", "", "s", "toString", "", "waitForState", "timeout", "Ljava/time/Duration;", "Lkotlin/time/Duration;", "waitForState-KLykuaI", "(J[ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class BluetoothAdapterState {
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.Integer> _uiState = kotlinx.coroutines.flow.SharedFlowKt.MutableSharedFlow$default(1, 0, null, 6, null);

    /* JADX INFO: renamed from: com.android.server.bluetooth.BluetoothAdapterState$waitForState$2, reason: invalid class name */
    /* JADX INFO: compiled from: AdapterState.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.android.server.bluetooth.BluetoothAdapterState", f = "AdapterState.kt", i = {}, l = {50}, m = "waitForState-KLykuaI", n = {}, s = {})
    static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        int label;
        /* synthetic */ java.lang.Object result;

        AnonymousClass2(kotlin.coroutines.Continuation<? super com.android.server.bluetooth.BluetoothAdapterState.AnonymousClass2> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return com.android.server.bluetooth.BluetoothAdapterState.this.m2589waitForStateKLykuaI(0L, null, this);
        }
    }

    public BluetoothAdapterState() {
        set(10);
    }

    /* JADX INFO: renamed from: com.android.server.bluetooth.BluetoothAdapterState$set$1, reason: invalid class name */
    /* JADX INFO: compiled from: AdapterState.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.android.server.bluetooth.BluetoothAdapterState$set$1", f = "AdapterState.kt", i = {}, l = {37}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ int $s;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(int i, kotlin.coroutines.Continuation<? super com.android.server.bluetooth.BluetoothAdapterState.AnonymousClass1> continuation) {
            super(2, continuation);
            this.$s = i;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return com.android.server.bluetooth.BluetoothAdapterState.this.new AnonymousClass1(this.$s, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((com.android.server.bluetooth.BluetoothAdapterState.AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (com.android.server.bluetooth.BluetoothAdapterState.this._uiState.emit(kotlin.coroutines.jvm.internal.Boxing.boxInt(this.$s), this) == coroutine_suspended) {
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

    public final void set(int s) {
        kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(null, new com.android.server.bluetooth.BluetoothAdapterState.AnonymousClass1(s, null), 1, null);
    }

    public final int get() {
        return this._uiState.getReplayCache().get(0).intValue();
    }

    public final boolean oneOf(int... states) {
        return kotlin.collections.ArraysKt.contains(states, get());
    }

    public java.lang.String toString() {
        return android.bluetooth.BluetoothAdapter.nameForState(get());
    }

    /* JADX INFO: renamed from: com.android.server.bluetooth.BluetoothAdapterState$waitForState$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: AdapterState.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.android.server.bluetooth.BluetoothAdapterState$waitForState$1", f = "AdapterState.kt", i = {}, l = {46}, m = "invokeSuspend", n = {}, s = {})
    static final class C00241 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super java.lang.Boolean>, java.lang.Object> {
        final /* synthetic */ int[] $states;
        final /* synthetic */ java.time.Duration $timeout;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00241(java.time.Duration duration, int[] iArr, kotlin.coroutines.Continuation<? super com.android.server.bluetooth.BluetoothAdapterState.C00241> continuation) {
            super(2, continuation);
            this.$timeout = duration;
            this.$states = iArr;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return com.android.server.bluetooth.BluetoothAdapterState.this.new C00241(this.$timeout, this.$states, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
            return ((com.android.server.bluetooth.BluetoothAdapterState.C00241) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    com.android.server.bluetooth.BluetoothAdapterState bluetoothAdapterState = com.android.server.bluetooth.BluetoothAdapterState.this;
                    java.time.Duration duration = this.$timeout;
                    this.label = 1;
                    java.lang.Object objM2589waitForStateKLykuaI = bluetoothAdapterState.m2589waitForStateKLykuaI(kotlin.time.Duration.m12669plusLRDsOJo(kotlin.time.DurationKt.toDuration(duration.getSeconds(), kotlin.time.DurationUnit.SECONDS), kotlin.time.DurationKt.toDuration(duration.getNano(), kotlin.time.DurationUnit.NANOSECONDS)), java.util.Arrays.copyOf(this.$states, this.$states.length), this);
                    return objM2589waitForStateKLykuaI == coroutine_suspended ? coroutine_suspended : objM2589waitForStateKLykuaI;
                case 1:
                    kotlin.ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final boolean waitForState(java.time.Duration timeout, int... states) {
        return ((java.lang.Boolean) kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(null, new com.android.server.bluetooth.BluetoothAdapterState.C00241(timeout, states, null), 1, null)).booleanValue();
    }

    /* JADX INFO: renamed from: com.android.server.bluetooth.BluetoothAdapterState$waitForState$3, reason: invalid class name */
    /* JADX INFO: compiled from: AdapterState.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.android.server.bluetooth.BluetoothAdapterState$waitForState$3", f = "AdapterState.kt", i = {}, l = {50}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass3 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super java.lang.Integer>, java.lang.Object> {
        final /* synthetic */ int[] $states;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(int[] iArr, kotlin.coroutines.Continuation<? super com.android.server.bluetooth.BluetoothAdapterState.AnonymousClass3> continuation) {
            super(2, continuation);
            this.$states = iArr;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return com.android.server.bluetooth.BluetoothAdapterState.this.new AnonymousClass3(this.$states, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super java.lang.Integer> continuation) {
            return ((com.android.server.bluetooth.BluetoothAdapterState.AnonymousClass3) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    final kotlinx.coroutines.flow.Flow $this$filter$iv = com.android.server.bluetooth.BluetoothAdapterState.this._uiState;
                    final int[] iArr = this.$states;
                    this.label = 1;
                    java.lang.Object objFirst = kotlinx.coroutines.flow.FlowKt.first(new kotlinx.coroutines.flow.Flow<java.lang.Integer>() { // from class: com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1

                        /* JADX INFO: renamed from: com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1$2, reason: invalid class name */
                        /* JADX INFO: compiled from: Emitters.kt */
                        @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\b"}, d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$filter$$inlined$unsafeTransform$1$2"}, k = 3, mv = {1, 9, 0}, xi = 48)
                        public static final class AnonymousClass2<T> implements kotlinx.coroutines.flow.FlowCollector {
                            final /* synthetic */ int[] $states$inlined;
                            final /* synthetic */ kotlinx.coroutines.flow.FlowCollector $this_unsafeFlow;

                            /* JADX INFO: renamed from: com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1$2$1, reason: invalid class name */
                            /* JADX INFO: compiled from: Emitters.kt */
                            @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                            @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1$2", f = "AdapterState.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED}, m = "emit", n = {}, s = {})
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
                                    return com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1.AnonymousClass2.this.emit(null, this);
                                }
                            }

                            public AnonymousClass2(kotlinx.coroutines.flow.FlowCollector flowCollector, int[] iArr) {
                                this.$this_unsafeFlow = flowCollector;
                                this.$states$inlined = iArr;
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
                                    boolean r0 = r10 instanceof com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1.AnonymousClass2.AnonymousClass1
                                    if (r0 == 0) goto L14
                                    r0 = r10
                                    com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1$2$1 r0 = (com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1.AnonymousClass2.AnonymousClass1) r0
                                    int r1 = r0.label
                                    r2 = -2147483648(0xffffffff80000000, float:-0.0)
                                    r1 = r1 & r2
                                    if (r1 == 0) goto L14
                                    int r10 = r0.label
                                    int r10 = r10 - r2
                                    r0.label = r10
                                    goto L19
                                L14:
                                    com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1$2$1 r0 = new com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1$2$1
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
                                    goto L57
                                L32:
                                    kotlin.ResultKt.throwOnFailure(r0)
                                    r2 = r8
                                    kotlinx.coroutines.flow.FlowCollector r3 = r2.$this_unsafeFlow
                                    r4 = 0
                                    r5 = r10
                                    kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                                    r5 = r9
                                    java.lang.Number r5 = (java.lang.Number) r5
                                    int r5 = r5.intValue()
                                    r6 = 0
                                    int[] r7 = r2.$states$inlined
                                    boolean r2 = kotlin.collections.ArraysKt.contains(r7, r5)
                                    if (r2 == 0) goto L58
                                    r2 = 1
                                    r10.label = r2
                                    java.lang.Object r9 = r3.emit(r9, r10)
                                    if (r9 != r1) goto L56
                                    return r1
                                L56:
                                    r9 = r4
                                L57:
                                    goto L59
                                L58:
                                L59:
                                    kotlin.Unit r9 = kotlin.Unit.INSTANCE
                                    return r9
                                */
                                throw new UnsupportedOperationException("Method not decompiled: com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
                            }
                        }

                        @Override // kotlinx.coroutines.flow.Flow
                        public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super java.lang.Integer> flowCollector, kotlin.coroutines.Continuation $completion) {
                            java.lang.Object objCollect = $this$filter$iv.collect(new com.android.server.bluetooth.BluetoothAdapterState$waitForState$3$invokeSuspend$$inlined$filter$1.AnonymousClass2(flowCollector, iArr), $completion);
                            return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
                        }
                    }, this);
                    return objFirst == coroutine_suspended ? coroutine_suspended : objFirst;
                case 1:
                    kotlin.ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX INFO: renamed from: waitForState-KLykuaI, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object m2589waitForStateKLykuaI(long r7, int[] r9, kotlin.coroutines.Continuation<? super java.lang.Boolean> r10) throws java.lang.Throwable {
        /*
            r6 = this;
            boolean r0 = r10 instanceof com.android.server.bluetooth.BluetoothAdapterState.AnonymousClass2
            if (r0 == 0) goto L14
            r0 = r10
            com.android.server.bluetooth.BluetoothAdapterState$waitForState$2 r0 = (com.android.server.bluetooth.BluetoothAdapterState.AnonymousClass2) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r10 = r0.label
            int r10 = r10 - r2
            r0.label = r10
            goto L19
        L14:
            com.android.server.bluetooth.BluetoothAdapterState$waitForState$2 r0 = new com.android.server.bluetooth.BluetoothAdapterState$waitForState$2
            r0.<init>(r10)
        L19:
            r10 = r0
            java.lang.Object r0 = r10.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r10.label
            r3 = 1
            switch(r2) {
                case 0: goto L33;
                case 1: goto L2e;
                default: goto L26;
            }
        L26:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r8 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>(r8)
            throw r7
        L2e:
            kotlin.ResultKt.throwOnFailure(r0)
            r7 = r0
            goto L48
        L33:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = r6
            com.android.server.bluetooth.BluetoothAdapterState$waitForState$3 r4 = new com.android.server.bluetooth.BluetoothAdapterState$waitForState$3
            r5 = 0
            r4.<init>(r9, r5)
            kotlin.jvm.functions.Function2 r4 = (kotlin.jvm.functions.Function2) r4
            r10.label = r3
            java.lang.Object r7 = kotlinx.coroutines.TimeoutKt.m12808withTimeoutOrNullKLykuaI(r7, r4, r10)
            if (r7 != r1) goto L48
            return r1
        L48:
            if (r7 == 0) goto L4b
            goto L4c
        L4b:
            r3 = 0
        L4c:
            java.lang.Boolean r7 = kotlin.coroutines.jvm.internal.Boxing.boxBoolean(r3)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.bluetooth.BluetoothAdapterState.m2589waitForStateKLykuaI(long, int[], kotlin.coroutines.Continuation):java.lang.Object");
    }
}
