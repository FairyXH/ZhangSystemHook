package kotlinx.coroutines;

/* JADX INFO: compiled from: JobSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Lkotlinx/coroutines/Job;"}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.JobSupport$children$1", f = "JobSupport.kt", i = {1, 1, 1}, l = {956, 958}, m = "invokeSuspend", n = {"$this$sequence", "this_$iv", "cur$iv"}, s = {"L$0", "L$1", "L$2"})
final class JobSupport$children$1 extends kotlin.coroutines.jvm.internal.RestrictedSuspendLambda implements kotlin.jvm.functions.Function2<kotlin.sequences.SequenceScope<? super kotlinx.coroutines.Job>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
    private /* synthetic */ java.lang.Object L$0;
    java.lang.Object L$1;
    java.lang.Object L$2;
    int label;
    final /* synthetic */ kotlinx.coroutines.JobSupport this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    JobSupport$children$1(kotlinx.coroutines.JobSupport jobSupport, kotlin.coroutines.Continuation<? super kotlinx.coroutines.JobSupport$children$1> continuation) {
        super(2, continuation);
        this.this$0 = jobSupport;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
        kotlinx.coroutines.JobSupport$children$1 jobSupport$children$1 = new kotlinx.coroutines.JobSupport$children$1(this.this$0, continuation);
        jobSupport$children$1.L$0 = obj;
        return jobSupport$children$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final java.lang.Object invoke(kotlin.sequences.SequenceScope<? super kotlinx.coroutines.Job> sequenceScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return ((kotlinx.coroutines.JobSupport$children$1) create(sequenceScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x007b  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x007d -> B:29:0x0097). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x0095 -> B:29:0x0097). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r13) throws java.lang.Throwable {
        /*
            r12 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r12.label
            switch(r1) {
                case 0: goto L2b;
                case 1: goto L26;
                case 2: goto L11;
                default: goto L9;
            }
        L9:
            java.lang.IllegalStateException r13 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r13.<init>(r0)
            throw r13
        L11:
            r1 = r12
            r2 = 0
            r3 = 0
            r4 = 0
            java.lang.Object r5 = r1.L$2
            kotlinx.coroutines.internal.LockFreeLinkedListNode r5 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r5
            java.lang.Object r6 = r1.L$1
            kotlinx.coroutines.internal.LockFreeLinkedListHead r6 = (kotlinx.coroutines.internal.LockFreeLinkedListHead) r6
            java.lang.Object r7 = r1.L$0
            kotlin.sequences.SequenceScope r7 = (kotlin.sequences.SequenceScope) r7
            kotlin.ResultKt.throwOnFailure(r13)
            goto L96
        L26:
            r0 = r12
            kotlin.ResultKt.throwOnFailure(r13)
            goto L50
        L2b:
            kotlin.ResultKt.throwOnFailure(r13)
            r1 = r12
            java.lang.Object r2 = r1.L$0
            kotlin.sequences.SequenceScope r2 = (kotlin.sequences.SequenceScope) r2
            kotlinx.coroutines.JobSupport r3 = r1.this$0
            java.lang.Object r3 = r3.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()
            boolean r4 = r3 instanceof kotlinx.coroutines.ChildHandleNode
            if (r4 == 0) goto L52
            r4 = r3
            kotlinx.coroutines.ChildHandleNode r4 = (kotlinx.coroutines.ChildHandleNode) r4
            kotlinx.coroutines.ChildJob r4 = r4.childJob
            r5 = r1
            kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
            r6 = 1
            r1.label = r6
            java.lang.Object r2 = r2.yield(r4, r5)
            if (r2 != r0) goto L4f
            return r0
        L4f:
            r0 = r1
        L50:
            r1 = r0
            goto La0
        L52:
            boolean r4 = r3 instanceof kotlinx.coroutines.Incomplete
            if (r4 == 0) goto La0
            r4 = r3
            kotlinx.coroutines.Incomplete r4 = (kotlinx.coroutines.Incomplete) r4
            kotlinx.coroutines.NodeList r3 = r4.getList()
            if (r3 == 0) goto La0
            r4 = 0
            kotlinx.coroutines.internal.LockFreeLinkedListHead r3 = (kotlinx.coroutines.internal.LockFreeLinkedListHead) r3
            r5 = 0
            java.lang.Object r6 = r3.getNext()
            java.lang.String r7 = "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }"
            kotlin.jvm.internal.Intrinsics.checkNotNull(r6, r7)
            kotlinx.coroutines.internal.LockFreeLinkedListNode r6 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r6
            r7 = r2
            r2 = r4
            r11 = r6
            r6 = r3
            r3 = r5
            r5 = r11
        L75:
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r5, r6)
            if (r4 != 0) goto L9d
            boolean r4 = r5 instanceof kotlinx.coroutines.ChildHandleNode
            if (r4 == 0) goto L97
            r4 = r5
            kotlinx.coroutines.ChildHandleNode r4 = (kotlinx.coroutines.ChildHandleNode) r4
            r8 = 0
            kotlinx.coroutines.ChildJob r9 = r4.childJob
            r1.L$0 = r7
            r1.L$1 = r6
            r1.L$2 = r5
            r10 = 2
            r1.label = r10
            java.lang.Object r4 = r7.yield(r9, r1)
            if (r4 != r0) goto L95
            return r0
        L95:
            r4 = r8
        L96:
        L97:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r5 = r5.getNextNode()
            goto L75
        L9d:
        La0:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport$children$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
