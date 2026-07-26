package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Produce.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000b\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a&\u0010\u0000\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u00022\u000e\b\u0002\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00010\u0004H\u0086@¢\u0006\u0002\u0010\u0005\u001a\u009b\u0001\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2-\b\u0002\u0010\u0010\u001a'\u0012\u0015\u0012\u0013\u0018\u00010\u0012¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0011j\u0004\u0018\u0001`\u00162/\b\u0001\u0010\u0003\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0017¢\u0006\u0002\b\u001aH\u0007¢\u0006\u0002\u0010\u001b\u001a¥\u0001\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2-\b\u0002\u0010\u0010\u001a'\u0012\u0015\u0012\u0013\u0018\u00010\u0012¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0011j\u0004\u0018\u0001`\u00162/\b\u0001\u0010\u0003\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0017¢\u0006\u0002\b\u001aH\u0000¢\u0006\u0002\u0010\u001e\u001ab\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2/\b\u0001\u0010\u0003\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0017¢\u0006\u0002\b\u001aH\u0007¢\u0006\u0002\u0010\u001f¨\u0006 "}, d2 = {"awaitClose", "", "Lkotlinx/coroutines/channels/ProducerScope;", "block", "Lkotlin/Function0;", "(Lkotlinx/coroutines/channels/ProducerScope;Lkotlin/jvm/functions/Function0;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "produce", "Lkotlinx/coroutines/channels/ReceiveChannel;", "E", "Lkotlinx/coroutines/CoroutineScope;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "start", "Lkotlinx/coroutines/CoroutineStart;", "onCompletion", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "Lkotlinx/coroutines/CompletionHandler;", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;ILkotlinx/coroutines/CoroutineStart;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/channels/ReceiveChannel;", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;ILkotlinx/coroutines/channels/BufferOverflow;Lkotlinx/coroutines/CoroutineStart;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/channels/ReceiveChannel;", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;ILkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/channels/ReceiveChannel;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ProduceKt {

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.ProduceKt$awaitClose$1, reason: invalid class name */
    /* JADX INFO: compiled from: Produce.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.ProduceKt", f = "Produce.kt", i = {0, 0}, l = {153}, m = "awaitClose", n = {"$this$awaitClose", "block"}, s = {"L$0", "L$1"})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        java.lang.Object L$1;
        int label;
        /* synthetic */ java.lang.Object result;

        AnonymousClass1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ProduceKt.AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.channels.ProduceKt.awaitClose(null, null, this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final java.lang.Object awaitClose(kotlinx.coroutines.channels.ProducerScope<?> r9, kotlin.jvm.functions.Function0<kotlin.Unit> r10, kotlin.coroutines.Continuation<? super kotlin.Unit> r11) throws java.lang.Throwable {
        /*
            boolean r0 = r11 instanceof kotlinx.coroutines.channels.ProduceKt.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r11
            kotlinx.coroutines.channels.ProduceKt$awaitClose$1 r0 = (kotlinx.coroutines.channels.ProduceKt.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r11 = r0.label
            int r11 = r11 - r2
            r0.label = r11
            goto L19
        L14:
            kotlinx.coroutines.channels.ProduceKt$awaitClose$1 r0 = new kotlinx.coroutines.channels.ProduceKt$awaitClose$1
            r0.<init>(r11)
        L19:
            r11 = r0
            java.lang.Object r0 = r11.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r11.label
            switch(r2) {
                case 0: goto L3c;
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
            java.lang.Object r10 = r11.L$1
            kotlin.jvm.functions.Function0 r10 = (kotlin.jvm.functions.Function0) r10
            java.lang.Object r1 = r11.L$0
            kotlinx.coroutines.channels.ProducerScope r1 = (kotlinx.coroutines.channels.ProducerScope) r1
            kotlin.ResultKt.throwOnFailure(r0)     // Catch: java.lang.Throwable -> L3a
            goto L90
        L3a:
            r9 = move-exception
            goto L98
        L3c:
            kotlin.ResultKt.throwOnFailure(r0)
            kotlin.coroutines.CoroutineContext r2 = r11.get$context()
            kotlinx.coroutines.Job$Key r3 = kotlinx.coroutines.Job.INSTANCE
            kotlin.coroutines.CoroutineContext$Key r3 = (kotlin.coroutines.CoroutineContext.Key) r3
            kotlin.coroutines.CoroutineContext$Element r2 = r2.get(r3)
            r3 = 1
            if (r2 != r9) goto L50
            r2 = r3
            goto L51
        L50:
            r2 = 0
        L51:
            if (r2 == 0) goto L9c
        L54:
            r2 = 0
            r11.L$0 = r9     // Catch: java.lang.Throwable -> L3a
            r11.L$1 = r10     // Catch: java.lang.Throwable -> L3a
            r11.label = r3     // Catch: java.lang.Throwable -> L3a
            r4 = r11
            kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4     // Catch: java.lang.Throwable -> L3a
            r5 = 0
            kotlinx.coroutines.CancellableContinuationImpl r6 = new kotlinx.coroutines.CancellableContinuationImpl     // Catch: java.lang.Throwable -> L3a
            kotlin.coroutines.Continuation r7 = kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(r4)     // Catch: java.lang.Throwable -> L3a
            r6.<init>(r7, r3)     // Catch: java.lang.Throwable -> L3a
            r3 = r6
            r3.initCancellability()     // Catch: java.lang.Throwable -> L3a
            r6 = r3
            kotlinx.coroutines.CancellableContinuation r6 = (kotlinx.coroutines.CancellableContinuation) r6     // Catch: java.lang.Throwable -> L3a
            r7 = 0
            kotlinx.coroutines.channels.ProduceKt$awaitClose$4$1 r8 = new kotlinx.coroutines.channels.ProduceKt$awaitClose$4$1     // Catch: java.lang.Throwable -> L3a
            r8.<init>()     // Catch: java.lang.Throwable -> L3a
            kotlin.jvm.functions.Function1 r8 = (kotlin.jvm.functions.Function1) r8     // Catch: java.lang.Throwable -> L3a
            r9.invokeOnClose(r8)     // Catch: java.lang.Throwable -> L3a
            java.lang.Object r9 = r3.getResult()     // Catch: java.lang.Throwable -> L3a
            java.lang.Object r3 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()     // Catch: java.lang.Throwable -> L3a
            if (r9 != r3) goto L8c
            r3 = r11
            kotlin.coroutines.Continuation r3 = (kotlin.coroutines.Continuation) r3     // Catch: java.lang.Throwable -> L3a
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(r3)     // Catch: java.lang.Throwable -> L3a
        L8c:
            if (r9 != r1) goto L8f
            return r1
        L8f:
            r9 = r2
        L90:
            r10.invoke()
            r9 = r10
            kotlin.Unit r10 = kotlin.Unit.INSTANCE
            return r10
        L98:
            r10.invoke()
            throw r9
        L9c:
            r9 = 0
            java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
            java.lang.String r10 = "awaitClose() can only be invoked from the producer context"
            java.lang.String r10 = r10.toString()
            r9.<init>(r10)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ProduceKt.awaitClose(kotlinx.coroutines.channels.ProducerScope, kotlin.jvm.functions.Function0, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static /* synthetic */ java.lang.Object awaitClose$default(kotlinx.coroutines.channels.ProducerScope producerScope, kotlin.jvm.functions.Function0 function0, kotlin.coroutines.Continuation continuation, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            function0 = new kotlin.jvm.functions.Function0<kotlin.Unit>() { // from class: kotlinx.coroutines.channels.ProduceKt.awaitClose.2
                @Override // kotlin.jvm.functions.Function0
                public /* bridge */ /* synthetic */ kotlin.Unit invoke() {
                    invoke2();
                    return kotlin.Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2() {
                }
            };
        }
        return awaitClose(producerScope, function0, continuation);
    }

    public static /* synthetic */ kotlinx.coroutines.channels.ReceiveChannel produce$default(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.CoroutineContext coroutineContext, int i, kotlin.jvm.functions.Function2 function2, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            coroutineContext = kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
        }
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return produce(coroutineScope, coroutineContext, i, function2);
    }

    public static final <E> kotlinx.coroutines.channels.ReceiveChannel<E> produce(kotlinx.coroutines.CoroutineScope $this$produce, kotlin.coroutines.CoroutineContext context, int capacity, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ProducerScope<? super E>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$produce, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        return produce($this$produce, context, capacity, kotlinx.coroutines.channels.BufferOverflow.SUSPEND, kotlinx.coroutines.CoroutineStart.DEFAULT, null, block);
    }

    public static final <E> kotlinx.coroutines.channels.ReceiveChannel<E> produce(kotlinx.coroutines.CoroutineScope $this$produce, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.CoroutineStart start, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> function1, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ProducerScope<? super E>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$produce, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(start, "start");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        return produce($this$produce, context, capacity, kotlinx.coroutines.channels.BufferOverflow.SUSPEND, start, function1, block);
    }

    public static final <E> kotlinx.coroutines.channels.ReceiveChannel<E> produce(kotlinx.coroutines.CoroutineScope $this$produce, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow, kotlinx.coroutines.CoroutineStart start, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> function1, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ProducerScope<? super E>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$produce, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(start, "start");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlinx.coroutines.channels.Channel channel = kotlinx.coroutines.channels.ChannelKt.Channel$default(capacity, onBufferOverflow, null, 4, null);
        kotlin.coroutines.CoroutineContext newContext = kotlinx.coroutines.CoroutineContextKt.newCoroutineContext($this$produce, context);
        kotlinx.coroutines.channels.ProducerCoroutine coroutine = new kotlinx.coroutines.channels.ProducerCoroutine(newContext, channel);
        if (function1 != null) {
            coroutine.invokeOnCompletion(function1);
        }
        coroutine.start(start, coroutine, block);
        return coroutine;
    }
}
