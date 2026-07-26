package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Share.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1", f = "Share.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__PLATFORM_ROLE_HOLDER_UPDATE_FINISHED, com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_CONTENT_PROTECTION_POLICY, 219, com.android.server.am.ProcessList.PERCEPTIBLE_MEDIUM_APP_ADJ}, m = "invokeSuspend", n = {}, s = {})
final class FlowKt__ShareKt$launchSharing$1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
    final /* synthetic */ T $initialValue;
    final /* synthetic */ kotlinx.coroutines.flow.MutableSharedFlow<T> $shared;
    final /* synthetic */ kotlinx.coroutines.flow.SharingStarted $started;
    final /* synthetic */ kotlinx.coroutines.flow.Flow<T> $upstream;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    FlowKt__ShareKt$launchSharing$1(kotlinx.coroutines.flow.SharingStarted sharingStarted, kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.MutableSharedFlow<T> mutableSharedFlow, T t, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1> continuation) {
        super(2, continuation);
        this.$started = sharingStarted;
        this.$upstream = flow;
        this.$shared = mutableSharedFlow;
        this.$initialValue = t;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
        return new kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1(this.$started, this.$upstream, this.$shared, this.$initialValue, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return ((kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0084 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0085  */
    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r9) throws java.lang.Throwable {
        /*
            r8 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r8.label
            switch(r1) {
                case 0: goto L27;
                case 1: goto L22;
                case 2: goto L1d;
                case 3: goto L17;
                case 4: goto L11;
                default: goto L9;
            }
        L9:
            java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r9.<init>(r0)
            throw r9
        L11:
            r0 = r8
            kotlin.ResultKt.throwOnFailure(r9)
            goto Lb2
        L17:
            r0 = r8
            kotlin.ResultKt.throwOnFailure(r9)
            goto L86
        L1d:
            r1 = r8
            kotlin.ResultKt.throwOnFailure(r9)
            goto L72
        L22:
            r0 = r8
            kotlin.ResultKt.throwOnFailure(r9)
            goto L4a
        L27:
            kotlin.ResultKt.throwOnFailure(r9)
            r1 = r8
            kotlinx.coroutines.flow.SharingStarted r2 = r1.$started
            kotlinx.coroutines.flow.SharingStarted$Companion r3 = kotlinx.coroutines.flow.SharingStarted.INSTANCE
            kotlinx.coroutines.flow.SharingStarted r3 = r3.getEagerly()
            if (r2 != r3) goto L4b
            kotlinx.coroutines.flow.Flow<T> r2 = r1.$upstream
            kotlinx.coroutines.flow.MutableSharedFlow<T> r3 = r1.$shared
            kotlinx.coroutines.flow.FlowCollector r3 = (kotlinx.coroutines.flow.FlowCollector) r3
            r4 = r1
            kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
            r5 = 1
            r1.label = r5
            java.lang.Object r2 = r2.collect(r3, r4)
            if (r2 != r0) goto L49
            return r0
        L49:
            r0 = r1
        L4a:
            goto Lb3
        L4b:
            kotlinx.coroutines.flow.SharingStarted r2 = r1.$started
            kotlinx.coroutines.flow.SharingStarted$Companion r3 = kotlinx.coroutines.flow.SharingStarted.INSTANCE
            kotlinx.coroutines.flow.SharingStarted r3 = r3.getLazily()
            r4 = 0
            if (r2 != r3) goto L87
            kotlinx.coroutines.flow.MutableSharedFlow<T> r2 = r1.$shared
            kotlinx.coroutines.flow.StateFlow r2 = r2.getSubscriptionCount()
            kotlinx.coroutines.flow.Flow r2 = (kotlinx.coroutines.flow.Flow) r2
            kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$1 r3 = new kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$1
            r3.<init>(r4)
            kotlin.jvm.functions.Function2 r3 = (kotlin.jvm.functions.Function2) r3
            r4 = r1
            kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
            r5 = 2
            r1.label = r5
            java.lang.Object r2 = kotlinx.coroutines.flow.FlowKt.first(r2, r3, r4)
            if (r2 != r0) goto L72
            return r0
        L72:
            kotlinx.coroutines.flow.Flow<T> r2 = r1.$upstream
            kotlinx.coroutines.flow.MutableSharedFlow<T> r3 = r1.$shared
            kotlinx.coroutines.flow.FlowCollector r3 = (kotlinx.coroutines.flow.FlowCollector) r3
            r4 = r1
            kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
            r5 = 3
            r1.label = r5
            java.lang.Object r2 = r2.collect(r3, r4)
            if (r2 != r0) goto L85
            return r0
        L85:
            r0 = r1
        L86:
            goto Lb3
        L87:
            kotlinx.coroutines.flow.SharingStarted r2 = r1.$started
            kotlinx.coroutines.flow.MutableSharedFlow<T> r3 = r1.$shared
            kotlinx.coroutines.flow.StateFlow r3 = r3.getSubscriptionCount()
            kotlinx.coroutines.flow.Flow r2 = r2.command(r3)
            kotlinx.coroutines.flow.Flow r2 = kotlinx.coroutines.flow.FlowKt.distinctUntilChanged(r2)
            kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$2 r3 = new kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$2
            kotlinx.coroutines.flow.Flow<T> r5 = r1.$upstream
            kotlinx.coroutines.flow.MutableSharedFlow<T> r6 = r1.$shared
            T r7 = r1.$initialValue
            r3.<init>(r5, r6, r7, r4)
            kotlin.jvm.functions.Function2 r3 = (kotlin.jvm.functions.Function2) r3
            r4 = r1
            kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
            r5 = 4
            r1.label = r5
            java.lang.Object r2 = kotlinx.coroutines.flow.FlowKt.collectLatest(r2, r3, r4)
            if (r2 != r0) goto Lb1
            return r0
        Lb1:
            r0 = r1
        Lb2:
        Lb3:
            kotlin.Unit r1 = kotlin.Unit.INSTANCE
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$1, reason: invalid class name */
    /* JADX INFO: compiled from: Share.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "it", ""}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$1", f = "Share.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<java.lang.Integer, kotlin.coroutines.Continuation<? super java.lang.Boolean>, java.lang.Object> {
        /* synthetic */ int I$0;
        int label;

        AnonymousClass1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass1> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass1 anonymousClass1 = new kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass1(continuation);
            anonymousClass1.I$0 = ((java.lang.Number) obj).intValue();
            return anonymousClass1;
        }

        public final java.lang.Object invoke(int i, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass1) create(java.lang.Integer.valueOf(i), continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ java.lang.Object invoke(java.lang.Integer num, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
            return invoke(num.intValue(), continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
            kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure(obj);
                    int it = this.I$0;
                    return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(it > 0);
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$2, reason: invalid class name */
    /* JADX INFO: compiled from: Share.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "it", "Lkotlinx/coroutines/flow/SharingCommand;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$2", f = "Share.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.CAMERA_ACTION_EVENT}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.SharingCommand, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ T $initialValue;
        final /* synthetic */ kotlinx.coroutines.flow.MutableSharedFlow<T> $shared;
        final /* synthetic */ kotlinx.coroutines.flow.Flow<T> $upstream;
        /* synthetic */ java.lang.Object L$0;
        int label;

        /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$2$WhenMappings */
        /* JADX INFO: compiled from: Share.kt */
        @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[kotlinx.coroutines.flow.SharingCommand.values().length];
                try {
                    iArr[kotlinx.coroutines.flow.SharingCommand.START.ordinal()] = 1;
                } catch (java.lang.NoSuchFieldError e) {
                }
                try {
                    iArr[kotlinx.coroutines.flow.SharingCommand.STOP.ordinal()] = 2;
                } catch (java.lang.NoSuchFieldError e2) {
                }
                try {
                    iArr[kotlinx.coroutines.flow.SharingCommand.STOP_AND_RESET_REPLAY_CACHE.ordinal()] = 3;
                } catch (java.lang.NoSuchFieldError e3) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass2(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.MutableSharedFlow<T> mutableSharedFlow, T t, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass2> continuation) {
            super(2, continuation);
            this.$upstream = flow;
            this.$shared = mutableSharedFlow;
            this.$initialValue = t;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass2(this.$upstream, this.$shared, this.$initialValue, continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.flow.SharingCommand sharingCommand, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass2) create(sharingCommand, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        /*  JADX ERROR: JadxRuntimeException in pass: FinishTypeInference
            jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r3v3 boolean
            	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:236)
            	at jadx.core.dex.visitors.typeinference.FinishTypeInference.lambda$visit$0(FinishTypeInference.java:27)
            	at java.base/java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.typeinference.FinishTypeInference.visit(FinishTypeInference.java:22)
            */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object r7) {
            /*
                r6 = this;
                java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r1 = r6.label
                switch(r1) {
                    case 0: goto L16;
                    case 1: goto L11;
                    default: goto L9;
                }
            L9:
                java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
                java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                r7.<init>(r0)
                throw r7
            L11:
                r0 = r6
                kotlin.ResultKt.throwOnFailure(r7)
                goto L54
            L16:
                kotlin.ResultKt.throwOnFailure(r7)
                r1 = r6
                java.lang.Object r2 = r1.L$0
                kotlinx.coroutines.flow.SharingCommand r2 = (kotlinx.coroutines.flow.SharingCommand) r2
                int[] r3 = kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass2.WhenMappings.$EnumSwitchMapping$0
                int r2 = r2.ordinal()
                r2 = r3[r2]
                switch(r2) {
                    case 1: goto L40;
                    case 2: goto L3f;
                    case 3: goto L2b;
                    default: goto L2a;
                }
            L2a:
                goto L55
            L2b:
                T r0 = r1.$initialValue
                kotlinx.coroutines.internal.Symbol r2 = kotlinx.coroutines.flow.SharedFlowKt.NO_VALUE
                if (r0 != r2) goto L37
                kotlinx.coroutines.flow.MutableSharedFlow<T> r0 = r1.$shared
                r0.resetReplayCache()
                goto L55
            L37:
                kotlinx.coroutines.flow.MutableSharedFlow<T> r0 = r1.$shared
                T r2 = r1.$initialValue
                r0.tryEmit(r2)
                goto L55
            L3f:
                goto L55
            L40:
                kotlinx.coroutines.flow.Flow<T> r2 = r1.$upstream
                kotlinx.coroutines.flow.MutableSharedFlow<T> r3 = r1.$shared
                kotlinx.coroutines.flow.FlowCollector r3 = (kotlinx.coroutines.flow.FlowCollector) r3
                r4 = r1
                kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
                r5 = 1
                r1.label = r5
                java.lang.Object r2 = r2.collect(r3, r4)
                if (r2 != r0) goto L53
                return r0
            L53:
                r0 = r1
            L54:
                r1 = r0
            L55:
                kotlin.Unit r0 = kotlin.Unit.INSTANCE
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1.AnonymousClass2.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }
}
