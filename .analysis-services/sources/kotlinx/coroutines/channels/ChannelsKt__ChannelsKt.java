package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Channels.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a%\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u0007¢\u0006\u0002\u0010\u0005\u001a)\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002¢\u0006\u0002\u0010\b¨\u0006\t"}, d2 = {"sendBlocking", "", "E", "Lkotlinx/coroutines/channels/SendChannel;", "element", "(Lkotlinx/coroutines/channels/SendChannel;Ljava/lang/Object;)V", "trySendBlocking", "Lkotlinx/coroutines/channels/ChannelResult;", "(Lkotlinx/coroutines/channels/SendChannel;Ljava/lang/Object;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/channels/ChannelsKt")
final /* synthetic */ class ChannelsKt__ChannelsKt {

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$trySendBlocking$2, reason: invalid class name */
    /* JADX INFO: compiled from: Channels.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", "Lkotlinx/coroutines/channels/ChannelResult;", "", "E", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$trySendBlocking$2", f = "Channels.kt", i = {}, l = {39}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelResult<? extends kotlin.Unit>>, java.lang.Object> {
        final /* synthetic */ E $element;
        final /* synthetic */ kotlinx.coroutines.channels.SendChannel<E> $this_trySendBlocking;
        private /* synthetic */ java.lang.Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass2(kotlinx.coroutines.channels.SendChannel<? super E> sendChannel, E e, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass2> continuation) {
            super(2, continuation);
            this.$this_trySendBlocking = sendChannel;
            this.$element = e;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass2(this.$this_trySendBlocking, this.$element, continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelResult<? extends kotlin.Unit>> continuation) {
            return invoke2(coroutineScope, (kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelResult<kotlin.Unit>>) continuation);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final java.lang.Object invoke2(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelResult<kotlin.Unit>> continuation) {
            return ((kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x004f  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0058  */
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
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r8) throws java.lang.Throwable {
            /*
                r7 = this;
                java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r1 = r7.label
                switch(r1) {
                    case 0: goto L19;
                    case 1: goto L11;
                    default: goto L9;
                }
            L9:
                java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
                java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                r8.<init>(r0)
                throw r8
            L11:
                r0 = r7
                r1 = 0
                kotlin.ResultKt.throwOnFailure(r8)     // Catch: java.lang.Throwable -> L17
                goto L34
            L17:
                r1 = move-exception
                goto L3f
            L19:
                kotlin.ResultKt.throwOnFailure(r8)
                r1 = r7
                java.lang.Object r2 = r1.L$0
                kotlinx.coroutines.CoroutineScope r2 = (kotlinx.coroutines.CoroutineScope) r2
                kotlinx.coroutines.channels.SendChannel<E> r3 = r1.$this_trySendBlocking
                E r4 = r1.$element
                kotlin.Result$Companion r2 = kotlin.Result.INSTANCE     // Catch: java.lang.Throwable -> L3b
                r2 = 0
                r5 = 1
                r1.label = r5     // Catch: java.lang.Throwable -> L3b
                java.lang.Object r3 = r3.send(r4, r1)     // Catch: java.lang.Throwable -> L3b
                if (r3 != r0) goto L32
                return r0
            L32:
                r0 = r1
                r1 = r2
            L34:
                kotlin.Unit r1 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L17
                java.lang.Object r1 = kotlin.Result.m11307constructorimpl(r1)     // Catch: java.lang.Throwable -> L17
                goto L49
            L3b:
                r0 = move-exception
                r6 = r1
                r1 = r0
                r0 = r6
            L3f:
                kotlin.Result$Companion r2 = kotlin.Result.INSTANCE
                java.lang.Object r1 = kotlin.ResultKt.createFailure(r1)
                java.lang.Object r1 = kotlin.Result.m11307constructorimpl(r1)
            L49:
                boolean r2 = kotlin.Result.m11314isSuccessimpl(r1)
                if (r2 == 0) goto L58
                kotlinx.coroutines.channels.ChannelResult$Companion r2 = kotlinx.coroutines.channels.ChannelResult.INSTANCE
                kotlin.Unit r3 = kotlin.Unit.INSTANCE
                java.lang.Object r2 = r2.m12834successJP2dKIU(r3)
                goto L62
            L58:
                kotlinx.coroutines.channels.ChannelResult$Companion r2 = kotlinx.coroutines.channels.ChannelResult.INSTANCE
                java.lang.Throwable r3 = kotlin.Result.m11310exceptionOrNullimpl(r1)
                java.lang.Object r2 = r2.m12832closedJP2dKIU(r3)
            L62:
                kotlinx.coroutines.channels.ChannelResult r1 = kotlinx.coroutines.channels.ChannelResult.m12819boximpl(r2)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass2.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <E> java.lang.Object trySendBlocking(kotlinx.coroutines.channels.SendChannel<? super E> sendChannel, E e) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sendChannel, "<this>");
        java.lang.Object $this$onSuccess_u2dWpGqRn0$iv = sendChannel.mo12809trySendJP2dKIU(e);
        if ($this$onSuccess_u2dWpGqRn0$iv instanceof kotlinx.coroutines.channels.ChannelResult.Failed) {
            return ((kotlinx.coroutines.channels.ChannelResult) kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(null, new kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass2(sendChannel, e, null), 1, null)).getHolder();
        }
        return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12834successJP2dKIU(kotlin.Unit.INSTANCE);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Deprecated in the favour of 'trySendBlocking'. Consider handling the result of 'trySendBlocking' explicitly and rethrow exception if necessary", replaceWith = @kotlin.ReplaceWith(expression = "trySendBlocking(element)", imports = {}))
    public static final /* synthetic */ void sendBlocking(kotlinx.coroutines.channels.SendChannel $this$sendBlocking, java.lang.Object element) throws java.lang.InterruptedException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sendBlocking, "<this>");
        if (!kotlinx.coroutines.channels.ChannelResult.m12829isSuccessimpl($this$sendBlocking.mo12809trySendJP2dKIU(element))) {
            kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(null, new kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass1($this$sendBlocking, element, null), 1, null);
        }
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$sendBlocking$1, reason: invalid class name */
    /* JADX INFO: compiled from: Channels.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$sendBlocking$1", f = "Channels.kt", i = {}, l = {58}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ E $element;
        final /* synthetic */ kotlinx.coroutines.channels.SendChannel<E> $this_sendBlocking;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass1(kotlinx.coroutines.channels.SendChannel<? super E> sendChannel, E e, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass1> continuation) {
            super(2, continuation);
            this.$this_sendBlocking = sendChannel;
            this.$element = e;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return new kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass1(this.$this_sendBlocking, this.$element, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
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
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (this.$this_sendBlocking.send(this.$element, this) == coroutine_suspended) {
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
}
