package kotlinx.coroutines.selects;

/* JADX INFO: compiled from: Select.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000\u0092\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0011\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u0002H\u00010\u0004:\u0001HB\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000eH\u0002J\u001a\u0010\u001d\u001a\u00020\u001b2\u0010\u0010\u001e\u001a\f0\nR\b\u0012\u0004\u0012\u00028\u00000\u0000H\u0002J\u000e\u0010\u001f\u001a\u00028\u0000H\u0082@¢\u0006\u0002\u0010 J\u0010\u0010!\u001a\u00020\u001b2\u0006\u0010\"\u001a\u00020#H\u0016J\u000e\u0010$\u001a\u00028\u0000H\u0091@¢\u0006\u0002\u0010 J\u000e\u0010%\u001a\u00028\u0000H\u0082@¢\u0006\u0002\u0010 J\u001c\u0010&\u001a\u000e\u0018\u00010\nR\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u001c\u001a\u00020\u000eH\u0002J\u0013\u0010'\u001a\u00020\u001b2\b\u0010(\u001a\u0004\u0018\u00010)H\u0096\u0002J\u001c\u0010*\u001a\u00020\u001b2\n\u0010+\u001a\u0006\u0012\u0002\b\u00030,2\u0006\u0010-\u001a\u00020\u0014H\u0016J*\u0010.\u001a\u00028\u00002\u0010\u0010/\u001a\f0\nR\b\u0012\u0004\u0012\u00028\u00000\u00002\b\u0010\u0015\u001a\u0004\u0018\u00010\u000eH\u0082@¢\u0006\u0002\u00100J\u0010\u00101\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000eH\u0002J\u0012\u00102\u001a\u00020\u001b2\b\u0010\u0015\u001a\u0004\u0018\u00010\u000eH\u0016J\u001a\u00103\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u000e2\b\u00104\u001a\u0004\u0018\u00010\u000eH\u0016J\u0018\u00105\u001a\u0002062\u0006\u0010\u001c\u001a\u00020\u000e2\b\u00104\u001a\u0004\u0018\u00010\u000eJ\u001a\u00107\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u000e2\b\u0010\u0015\u001a\u0004\u0018\u00010\u000eH\u0002J\u000e\u00108\u001a\u00020\u001bH\u0082@¢\u0006\u0002\u0010 J0\u0010'\u001a\u00020\u001b*\u0002092\u001c\u0010:\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000<\u0012\u0006\u0012\u0004\u0018\u00010\u000e0;H\u0096\u0002¢\u0006\u0002\u0010=JB\u0010'\u001a\u00020\u001b\"\u0004\b\u0001\u0010>*\b\u0012\u0004\u0012\u0002H>0?2\"\u0010:\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H>\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000<\u0012\u0006\u0012\u0004\u0018\u00010\u000e0@H\u0096\u0002¢\u0006\u0002\u0010AJV\u0010'\u001a\u00020\u001b\"\u0004\b\u0001\u0010B\"\u0004\b\u0002\u0010>*\u000e\u0012\u0004\u0012\u0002HB\u0012\u0004\u0012\u0002H>0C2\u0006\u0010D\u001a\u0002HB2\"\u0010:\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H>\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000<\u0012\u0006\u0012\u0004\u0018\u00010\u000e0@H\u0096\u0002¢\u0006\u0002\u0010EJ \u0010F\u001a\u00020\u001b*\f0\nR\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010G\u001a\u00020\u0010H\u0001R \u0010\b\u001a\u0014\u0012\u000e\u0012\f0\nR\b\u0012\u0004\u0012\u00028\u00000\u0000\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u0006X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\u00020\u00108BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u00020\u00108BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0012R\u0014\u0010\u0017\u001a\u00020\u00108BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0012R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0019X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006I"}, d2 = {"Lkotlinx/coroutines/selects/SelectImplementation;", "R", "Lkotlinx/coroutines/CancelHandler;", "Lkotlinx/coroutines/selects/SelectBuilder;", "Lkotlinx/coroutines/selects/SelectInstanceInternal;", "context", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "clauses", "", "Lkotlinx/coroutines/selects/SelectImplementation$ClauseData;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "disposableHandleOrSegment", "", "inRegistrationPhase", "", "getInRegistrationPhase", "()Z", "indexInSegment", "", "internalResult", "isCancelled", "isSelected", "state", "Lkotlinx/atomicfu/AtomicRef;", "checkClauseObject", "", "clauseObject", "cleanup", "selectedClause", "complete", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disposeOnCompletion", "disposableHandle", "Lkotlinx/coroutines/DisposableHandle;", "doSelect", "doSelectSuspend", "findClause", "invoke", "cause", "", "invokeOnCancellation", "segment", "Lkotlinx/coroutines/internal/Segment;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "processResultAndInvokeBlockRecoveringException", "clause", "(Lkotlinx/coroutines/selects/SelectImplementation$ClauseData;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reregisterClause", "selectInRegistrationPhase", "trySelect", "result", "trySelectDetailed", "Lkotlinx/coroutines/selects/TrySelectDetailedResult;", "trySelectInternal", "waitUntilSelected", "Lkotlinx/coroutines/selects/SelectClause0;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/selects/SelectClause0;Lkotlin/jvm/functions/Function1;)V", "Q", "Lkotlinx/coroutines/selects/SelectClause1;", "Lkotlin/Function2;", "(Lkotlinx/coroutines/selects/SelectClause1;Lkotlin/jvm/functions/Function2;)V", com.android.server.integrity.parser.RuleMetadataParser.RULE_PROVIDER_TAG, "Lkotlinx/coroutines/selects/SelectClause2;", "param", "(Lkotlinx/coroutines/selects/SelectClause2;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "register", "reregister", "ClauseData", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class SelectImplementation<R> extends kotlinx.coroutines.CancelHandler implements kotlinx.coroutines.selects.SelectBuilder<R>, kotlinx.coroutines.selects.SelectInstanceInternal<R> {
    private java.util.List<kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData> clauses;
    private final kotlin.coroutines.CoroutineContext context;
    private java.lang.Object disposableHandleOrSegment;
    private int indexInSegment;
    private java.lang.Object internalResult;
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> state;

    /* JADX INFO: renamed from: kotlinx.coroutines.selects.SelectImplementation$doSelectSuspend$1, reason: invalid class name */
    /* JADX INFO: compiled from: Select.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.selects.SelectImplementation", f = "Select.kt", i = {0}, l = {com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTION_SERVICE_INIT_RESULT_REPORTED, com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTOR_EVENTS}, m = "doSelectSuspend", n = {"this"}, s = {"L$0"})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        int label;
        /* synthetic */ java.lang.Object result;
        final /* synthetic */ kotlinx.coroutines.selects.SelectImplementation<R> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(kotlinx.coroutines.selects.SelectImplementation<R> selectImplementation, kotlin.coroutines.Continuation<? super kotlinx.coroutines.selects.SelectImplementation.AnonymousClass1> continuation) {
            super(continuation);
            this.this$0 = selectImplementation;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return this.this$0.doSelectSuspend(this);
        }
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.selects.SelectImplementation$processResultAndInvokeBlockRecoveringException$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Select.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.selects.SelectImplementation", f = "Select.kt", i = {}, l = {706}, m = "processResultAndInvokeBlockRecoveringException", n = {}, s = {})
    static final class C01791 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        int label;
        /* synthetic */ java.lang.Object result;
        final /* synthetic */ kotlinx.coroutines.selects.SelectImplementation<R> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01791(kotlinx.coroutines.selects.SelectImplementation<R> selectImplementation, kotlin.coroutines.Continuation<? super kotlinx.coroutines.selects.SelectImplementation.C01791> continuation) {
            super(continuation);
            this.this$0 = selectImplementation;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return this.this$0.processResultAndInvokeBlockRecoveringException(null, null, this);
        }
    }

    public java.lang.Object doSelect(kotlin.coroutines.Continuation<? super R> continuation) {
        return doSelect$suspendImpl(this, continuation);
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
        invoke2(th);
        return kotlin.Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.selects.SelectBuilder
    public <P, Q> void invoke(kotlinx.coroutines.selects.SelectClause2<? super P, ? extends Q> selectClause2, kotlin.jvm.functions.Function2<? super Q, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function2) {
        kotlinx.coroutines.selects.SelectBuilder.DefaultImpls.invoke(this, selectClause2, function2);
    }

    @Override // kotlinx.coroutines.selects.SelectBuilder
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Replaced with the same extension function", replaceWith = @kotlin.ReplaceWith(expression = "onTimeout", imports = {"kotlinx.coroutines.selects.onTimeout"}))
    public void onTimeout(long timeMillis, kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function1) {
        kotlinx.coroutines.selects.SelectBuilder.DefaultImpls.onTimeout(this, timeMillis, function1);
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    public kotlin.coroutines.CoroutineContext getContext() {
        return this.context;
    }

    public SelectImplementation(kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.state = kotlinx.atomicfu.AtomicFU.atomic(kotlinx.coroutines.selects.SelectKt.STATE_REG);
        this.clauses = new java.util.ArrayList(2);
        this.indexInSegment = -1;
        this.internalResult = kotlinx.coroutines.selects.SelectKt.NO_RESULT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean getInRegistrationPhase() {
        java.lang.Object it = this.state.getValue();
        return it == kotlinx.coroutines.selects.SelectKt.STATE_REG || (it instanceof java.util.List);
    }

    private final boolean isSelected() {
        return this.state.getValue() instanceof kotlinx.coroutines.selects.SelectImplementation.ClauseData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isCancelled() {
        return this.state.getValue() == kotlinx.coroutines.selects.SelectKt.STATE_CANCELLED;
    }

    static /* synthetic */ <R> java.lang.Object doSelect$suspendImpl(kotlinx.coroutines.selects.SelectImplementation<R> selectImplementation, kotlin.coroutines.Continuation<? super R> continuation) {
        return selectImplementation.isSelected() ? selectImplementation.complete(continuation) : selectImplementation.doSelectSuspend(continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object doSelectSuspend(kotlin.coroutines.Continuation<? super R> r5) throws java.lang.Throwable {
        /*
            r4 = this;
            boolean r0 = r5 instanceof kotlinx.coroutines.selects.SelectImplementation.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r5
            kotlinx.coroutines.selects.SelectImplementation$doSelectSuspend$1 r0 = (kotlinx.coroutines.selects.SelectImplementation.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r5 = r0.label
            int r5 = r5 - r2
            r0.label = r5
            goto L19
        L14:
            kotlinx.coroutines.selects.SelectImplementation$doSelectSuspend$1 r0 = new kotlinx.coroutines.selects.SelectImplementation$doSelectSuspend$1
            r0.<init>(r4, r5)
        L19:
            r5 = r0
            java.lang.Object r0 = r5.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r5.label
            switch(r2) {
                case 0: goto L3a;
                case 1: goto L32;
                case 2: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r0)
            throw r5
        L2d:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = r0
            goto L57
        L32:
            java.lang.Object r2 = r5.L$0
            kotlinx.coroutines.selects.SelectImplementation r2 = (kotlinx.coroutines.selects.SelectImplementation) r2
            kotlin.ResultKt.throwOnFailure(r0)
            goto L4a
        L3a:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = r4
            r5.L$0 = r2
            r3 = 1
            r5.label = r3
            java.lang.Object r3 = r2.waitUntilSelected(r5)
            if (r3 != r1) goto L4a
            return r1
        L4a:
            r3 = 0
            r5.L$0 = r3
            r3 = 2
            r5.label = r3
            java.lang.Object r2 = r2.complete(r5)
            if (r2 != r1) goto L57
            return r1
        L57:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.selects.SelectImplementation.doSelectSuspend(kotlin.coroutines.Continuation):java.lang.Object");
    }

    @Override // kotlinx.coroutines.selects.SelectBuilder
    public void invoke(kotlinx.coroutines.selects.SelectClause0 $this$invoke, kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$invoke, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        register$default(this, new kotlinx.coroutines.selects.SelectImplementation.ClauseData(this, $this$invoke.getClauseObject(), $this$invoke.getRegFunc(), $this$invoke.getProcessResFunc(), kotlinx.coroutines.selects.SelectKt.getPARAM_CLAUSE_0(), block, $this$invoke.getOnCancellationConstructor()), false, 1, null);
    }

    @Override // kotlinx.coroutines.selects.SelectBuilder
    public <Q> void invoke(kotlinx.coroutines.selects.SelectClause1<? extends Q> selectClause1, kotlin.jvm.functions.Function2<? super Q, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selectClause1, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        register$default(this, new kotlinx.coroutines.selects.SelectImplementation.ClauseData(this, selectClause1.getClauseObject(), selectClause1.getRegFunc(), selectClause1.getProcessResFunc(), null, block, selectClause1.getOnCancellationConstructor()), false, 1, null);
    }

    @Override // kotlinx.coroutines.selects.SelectBuilder
    public <P, Q> void invoke(kotlinx.coroutines.selects.SelectClause2<? super P, ? extends Q> selectClause2, P p, kotlin.jvm.functions.Function2<? super Q, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selectClause2, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        register$default(this, new kotlinx.coroutines.selects.SelectImplementation.ClauseData(this, selectClause2.getClauseObject(), selectClause2.getRegFunc(), selectClause2.getProcessResFunc(), p, block, selectClause2.getOnCancellationConstructor()), false, 1, null);
    }

    public static /* synthetic */ void register$default(kotlinx.coroutines.selects.SelectImplementation selectImplementation, kotlinx.coroutines.selects.SelectImplementation.ClauseData clauseData, boolean z, int i, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: register");
        }
        if ((i & 1) != 0) {
            z = false;
        }
        selectImplementation.register(clauseData, z);
    }

    public final void register(kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData clauseData, boolean reregister) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clauseData, "<this>");
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this.state.getValue() != kotlinx.coroutines.selects.SelectKt.STATE_CANCELLED)) {
                throw new java.lang.AssertionError();
            }
        }
        java.lang.Object it = this.state.getValue();
        if (it instanceof kotlinx.coroutines.selects.SelectImplementation.ClauseData) {
            return;
        }
        if (!reregister) {
            checkClauseObject(clauseData.clauseObject);
        }
        if (clauseData.tryRegisterAsWaiter(this)) {
            if (!reregister) {
                java.util.List<kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData> list = this.clauses;
                kotlin.jvm.internal.Intrinsics.checkNotNull(list);
                list.add(clauseData);
            }
            clauseData.disposableHandleOrSegment = this.disposableHandleOrSegment;
            clauseData.indexInSegment = this.indexInSegment;
            this.disposableHandleOrSegment = null;
            this.indexInSegment = -1;
            return;
        }
        this.state.setValue(clauseData);
    }

    private final void checkClauseObject(java.lang.Object clauseObject) {
        java.lang.Iterable iterable = this.clauses;
        kotlin.jvm.internal.Intrinsics.checkNotNull(iterable);
        java.lang.Iterable $this$none$iv = iterable;
        boolean z = true;
        if (!($this$none$iv instanceof java.util.Collection) || !((java.util.Collection) $this$none$iv).isEmpty()) {
            java.util.Iterator it = $this$none$iv.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                java.lang.Object element$iv = it.next();
                kotlinx.coroutines.selects.SelectImplementation.ClauseData it2 = (kotlinx.coroutines.selects.SelectImplementation.ClauseData) element$iv;
                if (it2.clauseObject == clauseObject) {
                    z = false;
                    break;
                }
            }
        }
        if (!z) {
            throw new java.lang.IllegalStateException(("Cannot use select clauses on the same object: " + clauseObject).toString());
        }
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    public void disposeOnCompletion(kotlinx.coroutines.DisposableHandle disposableHandle) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(disposableHandle, "disposableHandle");
        this.disposableHandleOrSegment = disposableHandle;
    }

    @Override // kotlinx.coroutines.Waiter
    public void invokeOnCancellation(kotlinx.coroutines.internal.Segment<?> segment, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(segment, "segment");
        this.disposableHandleOrSegment = segment;
        this.indexInSegment = index;
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    public void selectInRegistrationPhase(java.lang.Object internalResult) {
        this.internalResult = internalResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0087, code lost:
    
        r2 = r4.getResult();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0090, code lost:
    
        if (r2 != kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0092, code lost:
    
        kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(r18);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0099, code lost:
    
        if (r2 != kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x009b, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x009f, code lost:
    
        return kotlin.Unit.INSTANCE;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object waitUntilSelected(kotlin.coroutines.Continuation<? super kotlin.Unit> r18) {
        /*
            r17 = this;
            r0 = r17
            r1 = 0
            r2 = r18
            r3 = 0
            kotlinx.coroutines.CancellableContinuationImpl r4 = new kotlinx.coroutines.CancellableContinuationImpl
            kotlin.coroutines.Continuation r5 = kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(r2)
            r6 = 1
            r4.<init>(r5, r6)
            r4.initCancellability()
            r5 = r4
            kotlinx.coroutines.CancellableContinuation r5 = (kotlinx.coroutines.CancellableContinuation) r5
            r6 = 0
            kotlinx.atomicfu.AtomicRef r7 = access$getState$p(r17)
            r8 = 0
        L1c:
            java.lang.Object r9 = r7.getValue()
            r10 = 0
            kotlinx.coroutines.internal.Symbol r11 = kotlinx.coroutines.selects.SelectKt.access$getSTATE_REG$p()
            if (r9 != r11) goto L3d
            kotlinx.atomicfu.AtomicRef r11 = access$getState$p(r17)
            boolean r11 = r11.compareAndSet(r9, r5)
            if (r11 == 0) goto L6d
            r11 = r0
            kotlinx.coroutines.CancelHandlerBase r11 = (kotlinx.coroutines.CancelHandlerBase) r11
            r12 = 0
            kotlin.jvm.functions.Function1 r11 = (kotlin.jvm.functions.Function1) r11
            r5.invokeOnCancellation(r11)
            goto L87
        L3d:
            boolean r11 = r9 instanceof java.util.List
            if (r11 == 0) goto L6f
            kotlinx.atomicfu.AtomicRef r11 = access$getState$p(r17)
            kotlinx.coroutines.internal.Symbol r12 = kotlinx.coroutines.selects.SelectKt.access$getSTATE_REG$p()
            boolean r11 = r11.compareAndSet(r9, r12)
            if (r11 == 0) goto L6d
            r11 = r9
            java.util.List r11 = (java.util.List) r11
            r11 = r9
            java.lang.Iterable r11 = (java.lang.Iterable) r11
            r12 = 0
            java.util.Iterator r13 = r11.iterator()
        L5a:
            boolean r14 = r13.hasNext()
            if (r14 == 0) goto L6c
            java.lang.Object r14 = r13.next()
            r15 = r14
            r16 = 0
            access$reregisterClause(r0, r15)
            goto L5a
        L6c:
        L6d:
            goto L1c
        L6f:
            boolean r11 = r9 instanceof kotlinx.coroutines.selects.SelectImplementation.ClauseData
            if (r11 == 0) goto La0
            kotlin.Unit r11 = kotlin.Unit.INSTANCE
            r12 = r9
            kotlinx.coroutines.selects.SelectImplementation$ClauseData r12 = (kotlinx.coroutines.selects.SelectImplementation.ClauseData) r12
            r13 = r0
            kotlinx.coroutines.selects.SelectInstance r13 = (kotlinx.coroutines.selects.SelectInstance) r13
            java.lang.Object r14 = access$getInternalResult$p(r17)
            kotlin.jvm.functions.Function1 r12 = r12.createOnCancellationAction(r13, r14)
            r5.resume(r11, r12)
        L87:
            java.lang.Object r2 = r4.getResult()
            java.lang.Object r3 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            if (r2 != r3) goto L95
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(r18)
        L95:
            java.lang.Object r3 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            if (r2 != r3) goto L9c
            return r2
        L9c:
            kotlin.Unit r1 = kotlin.Unit.INSTANCE
            return r1
        La0:
            java.lang.IllegalStateException r11 = new java.lang.IllegalStateException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "unexpected state: "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.StringBuilder r12 = r12.append(r9)
            java.lang.String r12 = r12.toString()
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.selects.SelectImplementation.waitUntilSelected(kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void reregisterClause(java.lang.Object clauseObject) {
        kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData clauseDataFindClause = findClause(clauseObject);
        kotlin.jvm.internal.Intrinsics.checkNotNull(clauseDataFindClause);
        clauseDataFindClause.disposableHandleOrSegment = null;
        clauseDataFindClause.indexInSegment = -1;
        register(clauseDataFindClause, true);
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    public boolean trySelect(java.lang.Object clauseObject, java.lang.Object result) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clauseObject, "clauseObject");
        return trySelectInternal(clauseObject, result) == 0;
    }

    public final kotlinx.coroutines.selects.TrySelectDetailedResult trySelectDetailed(java.lang.Object clauseObject, java.lang.Object result) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clauseObject, "clauseObject");
        return kotlinx.coroutines.selects.SelectKt.TrySelectDetailedResult(trySelectInternal(clauseObject, result));
    }

    private final int trySelectInternal(java.lang.Object clauseObject, java.lang.Object internalResult) {
        while (true) {
            java.lang.Object curState = this.state.getValue();
            if (!(curState instanceof kotlinx.coroutines.CancellableContinuation)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(curState, kotlinx.coroutines.selects.SelectKt.STATE_COMPLETED) ? true : curState instanceof kotlinx.coroutines.selects.SelectImplementation.ClauseData) {
                    return 3;
                }
                if (kotlin.jvm.internal.Intrinsics.areEqual(curState, kotlinx.coroutines.selects.SelectKt.STATE_CANCELLED)) {
                    return 2;
                }
                if (kotlin.jvm.internal.Intrinsics.areEqual(curState, kotlinx.coroutines.selects.SelectKt.STATE_REG)) {
                    if (this.state.compareAndSet(curState, kotlin.collections.CollectionsKt.listOf(clauseObject))) {
                        return 1;
                    }
                } else {
                    if (!(curState instanceof java.util.List)) {
                        throw new java.lang.IllegalStateException(("Unexpected state: " + curState).toString());
                    }
                    if (this.state.compareAndSet(curState, kotlin.collections.CollectionsKt.plus((java.util.Collection<? extends java.lang.Object>) curState, clauseObject))) {
                        return 1;
                    }
                }
            } else {
                kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData clauseDataFindClause = findClause(clauseObject);
                if (clauseDataFindClause == null) {
                    continue;
                } else {
                    kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> function1CreateOnCancellationAction = clauseDataFindClause.createOnCancellationAction(this, internalResult);
                    if (this.state.compareAndSet(curState, clauseDataFindClause)) {
                        kotlinx.coroutines.CancellableContinuation cont = (kotlinx.coroutines.CancellableContinuation) curState;
                        this.internalResult = internalResult;
                        if (kotlinx.coroutines.selects.SelectKt.tryResume(cont, function1CreateOnCancellationAction)) {
                            return 0;
                        }
                        this.internalResult = null;
                        return 2;
                    }
                }
            }
        }
    }

    private final kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData findClause(java.lang.Object clauseObject) {
        java.util.List<kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData> list = this.clauses;
        java.lang.Object obj = null;
        if (list == null) {
            return null;
        }
        java.util.Iterator<T> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            java.lang.Object next = it.next();
            kotlinx.coroutines.selects.SelectImplementation.ClauseData it2 = (kotlinx.coroutines.selects.SelectImplementation.ClauseData) next;
            if (it2.clauseObject == clauseObject) {
                obj = next;
                break;
            }
        }
        kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData clauseData = (kotlinx.coroutines.selects.SelectImplementation.ClauseData) obj;
        if (clauseData != null) {
            return clauseData;
        }
        throw new java.lang.IllegalStateException(("Clause with object " + clauseObject + " is not found").toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object complete(kotlin.coroutines.Continuation<? super R> continuation) throws java.lang.Throwable {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !isSelected()) {
            throw new java.lang.AssertionError();
        }
        java.lang.Object value = this.state.getValue();
        kotlin.jvm.internal.Intrinsics.checkNotNull(value, "null cannot be cast to non-null type kotlinx.coroutines.selects.SelectImplementation.ClauseData<R of kotlinx.coroutines.selects.SelectImplementation>");
        kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData clauseData = (kotlinx.coroutines.selects.SelectImplementation.ClauseData) value;
        java.lang.Object internalResult = this.internalResult;
        cleanup(clauseData);
        if (!kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES()) {
            java.lang.Object blockArgument = clauseData.processResult(internalResult);
            return clauseData.invokeBlock(blockArgument, continuation);
        }
        java.lang.Object blockArgument2 = processResultAndInvokeBlockRecoveringException(clauseData, internalResult, continuation);
        return blockArgument2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object processResultAndInvokeBlockRecoveringException(kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData r5, java.lang.Object r6, kotlin.coroutines.Continuation<? super R> r7) throws java.lang.Throwable {
        /*
            r4 = this;
            boolean r0 = r7 instanceof kotlinx.coroutines.selects.SelectImplementation.C01791
            if (r0 == 0) goto L14
            r0 = r7
            kotlinx.coroutines.selects.SelectImplementation$processResultAndInvokeBlockRecoveringException$1 r0 = (kotlinx.coroutines.selects.SelectImplementation.C01791) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r7 = r0.label
            int r7 = r7 - r2
            r0.label = r7
            goto L19
        L14:
            kotlinx.coroutines.selects.SelectImplementation$processResultAndInvokeBlockRecoveringException$1 r0 = new kotlinx.coroutines.selects.SelectImplementation$processResultAndInvokeBlockRecoveringException$1
            r0.<init>(r4, r7)
        L19:
            r7 = r0
            java.lang.Object r0 = r7.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r7.label
            switch(r2) {
                case 0: goto L34;
                case 1: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L2d:
            kotlin.ResultKt.throwOnFailure(r0)     // Catch: java.lang.Throwable -> L32
            r2 = r0
            goto L47
        L32:
            r5 = move-exception
            goto L49
        L34:
            kotlin.ResultKt.throwOnFailure(r0)
            java.lang.Object r2 = r5.processResult(r6)     // Catch: java.lang.Throwable -> L32
            r6 = r2
            r2 = 1
            r7.label = r2     // Catch: java.lang.Throwable -> L32
            java.lang.Object r2 = r5.invokeBlock(r6, r7)     // Catch: java.lang.Throwable -> L32
            if (r2 != r1) goto L47
            return r1
        L47:
            return r2
        L49:
            r6 = 0
            boolean r1 = kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES()
            if (r1 == 0) goto L5f
            r1 = r7
            r2 = 0
            boolean r3 = r1 instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame
            if (r3 != 0) goto L57
            throw r5
        L57:
            r3 = r1
            kotlin.coroutines.jvm.internal.CoroutineStackFrame r3 = (kotlin.coroutines.jvm.internal.CoroutineStackFrame) r3
            java.lang.Throwable r3 = kotlinx.coroutines.internal.StackTraceRecoveryKt.access$recoverFromStackFrame(r5, r3)
            throw r3
        L5f:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.selects.SelectImplementation.processResultAndInvokeBlockRecoveringException(kotlinx.coroutines.selects.SelectImplementation$ClauseData, java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }

    private final void cleanup(kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData selectedClause) {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !kotlin.jvm.internal.Intrinsics.areEqual(this.state.getValue(), selectedClause)) {
            throw new java.lang.AssertionError();
        }
        java.lang.Iterable iterable = this.clauses;
        if (iterable == null) {
            return;
        }
        java.lang.Iterable $this$forEach$iv = iterable;
        for (java.lang.Object element$iv : $this$forEach$iv) {
            kotlinx.coroutines.selects.SelectImplementation<R>.ClauseData clauseData = (kotlinx.coroutines.selects.SelectImplementation.ClauseData) element$iv;
            if (clauseData != selectedClause) {
                clauseData.dispose();
            }
        }
        this.state.setValue(kotlinx.coroutines.selects.SelectKt.STATE_COMPLETED);
        this.internalResult = kotlinx.coroutines.selects.SelectKt.NO_RESULT;
        this.clauses = null;
    }

    @Override // kotlinx.coroutines.CancelHandlerBase
    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public void invoke2(java.lang.Throwable cause) {
        java.lang.Object cur$iv;
        java.lang.Object cur;
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this.state;
        do {
            cur$iv = atomicRef.getValue();
            if (cur$iv == kotlinx.coroutines.selects.SelectKt.STATE_COMPLETED) {
                return;
            } else {
                cur = kotlinx.coroutines.selects.SelectKt.STATE_CANCELLED;
            }
        } while (!atomicRef.compareAndSet(cur$iv, cur));
        java.lang.Iterable iterable = this.clauses;
        if (iterable == null) {
            return;
        }
        java.lang.Iterable $this$forEach$iv = iterable;
        for (java.lang.Object element$iv : $this$forEach$iv) {
            kotlinx.coroutines.selects.SelectImplementation.ClauseData it = (kotlinx.coroutines.selects.SelectImplementation.ClauseData) element$iv;
            it.dispose();
        }
        this.internalResult = kotlinx.coroutines.selects.SelectKt.NO_RESULT;
        this.clauses = null;
    }

    /* JADX INFO: compiled from: Select.kt */
    @kotlin.Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\b\u0080\u0004\u0018\u00002\u00020\u0001B¶\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012U\u0010\u0003\u001aQ\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0002\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\u0007¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\b\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\t\u0012\u0004\u0012\u00020\n0\u0004j\u0002`\u000b\u0012U\u0010\f\u001aQ\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0002\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0004j\u0002`\u000e\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0001\u0012\u0006\u0010\u000f\u001a\u00020\u0001\u0012g\u0010\u0010\u001ac\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\u0007¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\b\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0011\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\n0\u0012\u0018\u00010\u0004j\u0004\u0018\u0001`\u0014¢\u0006\u0002\u0010\u0015J*\u0010\u0019\u001a\u0010\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\n\u0018\u00010\u00122\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\u00072\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001J\u0006\u0010\u001a\u001a\u00020\nJ\u0018\u0010\u001b\u001a\u00028\u00002\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u0086@¢\u0006\u0002\u0010\u001dJ\u0012\u0010\u001e\u001a\u0004\u0018\u00010\u00012\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001J\u0014\u0010 \u001a\u00020!2\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\"R\u000e\u0010\u000f\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00018\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u0004\u0018\u00010\u00018\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0017\u001a\u00020\u00188\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000Rq\u0010\u0010\u001ac\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\u0007¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\b\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0011\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\n0\u0012\u0018\u00010\u0004j\u0004\u0018\u0001`\u00148\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R]\u0010\f\u001aQ\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0002\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0004j\u0002`\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R]\u0010\u0003\u001aQ\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0002\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\u0007¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\b\u0012\u0015\u0012\u0013\u0018\u00010\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\t\u0012\u0004\u0012\u00020\n0\u0004j\u0002`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lkotlinx/coroutines/selects/SelectImplementation$ClauseData;", "", "clauseObject", "regFunc", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "param", "", "Lkotlinx/coroutines/selects/RegistrationFunction;", "processResFunc", "clauseResult", "Lkotlinx/coroutines/selects/ProcessResultFunction;", "block", "onCancellationConstructor", "internalResult", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/selects/OnCancellationConstructor;", "(Lkotlinx/coroutines/selects/SelectImplementation;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)V", "disposableHandleOrSegment", "indexInSegment", "", "createOnCancellationAction", "dispose", "invokeBlock", "argument", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "processResult", "result", "tryRegisterAsWaiter", "", "Lkotlinx/coroutines/selects/SelectImplementation;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public final class ClauseData {
        private final java.lang.Object block;
        public final java.lang.Object clauseObject;
        public java.lang.Object disposableHandleOrSegment;
        public int indexInSegment;
        public final kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>> onCancellationConstructor;
        private final java.lang.Object param;
        private final kotlin.jvm.functions.Function3<java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object> processResFunc;
        private final kotlin.jvm.functions.Function3<java.lang.Object, kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> regFunc;
        final /* synthetic */ kotlinx.coroutines.selects.SelectImplementation<R> this$0;

        /* JADX WARN: Multi-variable type inference failed */
        public ClauseData(kotlinx.coroutines.selects.SelectImplementation this$0, java.lang.Object clauseObject, kotlin.jvm.functions.Function3<java.lang.Object, ? super kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> regFunc, kotlin.jvm.functions.Function3<java.lang.Object, java.lang.Object, java.lang.Object, ? extends java.lang.Object> processResFunc, java.lang.Object param, java.lang.Object block, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, ? extends kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit>> function3) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clauseObject, "clauseObject");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regFunc, "regFunc");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(processResFunc, "processResFunc");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
            this.this$0 = this$0;
            this.clauseObject = clauseObject;
            this.regFunc = regFunc;
            this.processResFunc = processResFunc;
            this.param = param;
            this.block = block;
            this.onCancellationConstructor = function3;
            this.indexInSegment = -1;
        }

        public final boolean tryRegisterAsWaiter(kotlinx.coroutines.selects.SelectImplementation<R> select) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if (((select.getInRegistrationPhase() || select.isCancelled()) ? 1 : 0) == 0) {
                    throw new java.lang.AssertionError();
                }
            }
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if ((((kotlinx.coroutines.selects.SelectImplementation) select).internalResult == kotlinx.coroutines.selects.SelectKt.NO_RESULT ? 1 : 0) == 0) {
                    throw new java.lang.AssertionError();
                }
            }
            this.regFunc.invoke(this.clauseObject, select, this.param);
            return ((kotlinx.coroutines.selects.SelectImplementation) select).internalResult == kotlinx.coroutines.selects.SelectKt.NO_RESULT;
        }

        public final java.lang.Object processResult(java.lang.Object result) {
            return this.processResFunc.invoke(this.clauseObject, this.param, result);
        }

        public final java.lang.Object invokeBlock(java.lang.Object argument, kotlin.coroutines.Continuation<? super R> continuation) {
            java.lang.Object block = this.block;
            if (this.param == kotlinx.coroutines.selects.SelectKt.getPARAM_CLAUSE_0()) {
                kotlin.jvm.internal.Intrinsics.checkNotNull(block, "null cannot be cast to non-null type kotlin.coroutines.SuspendFunction0<R of kotlinx.coroutines.selects.SelectImplementation>");
                return ((kotlin.jvm.functions.Function1) block).invoke(continuation);
            }
            kotlin.jvm.internal.Intrinsics.checkNotNull(block, "null cannot be cast to non-null type kotlin.coroutines.SuspendFunction1<kotlin.Any?, R of kotlinx.coroutines.selects.SelectImplementation>");
            return ((kotlin.jvm.functions.Function2) block).invoke(argument, continuation);
        }

        public final void dispose() {
            java.lang.Object $this$dispose_u24lambda_u242 = this.disposableHandleOrSegment;
            kotlinx.coroutines.selects.SelectImplementation<R> selectImplementation = this.this$0;
            if ($this$dispose_u24lambda_u242 instanceof kotlinx.coroutines.internal.Segment) {
                ((kotlinx.coroutines.internal.Segment) $this$dispose_u24lambda_u242).onCancellation(this.indexInSegment, null, selectImplementation.getContext());
                return;
            }
            kotlinx.coroutines.DisposableHandle disposableHandle = $this$dispose_u24lambda_u242 instanceof kotlinx.coroutines.DisposableHandle ? (kotlinx.coroutines.DisposableHandle) $this$dispose_u24lambda_u242 : null;
            if (disposableHandle != null) {
                disposableHandle.dispose();
            }
        }

        public final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> createOnCancellationAction(kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object internalResult) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
            kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>> function3 = this.onCancellationConstructor;
            if (function3 != null) {
                return function3.invoke(select, this.param, internalResult);
            }
            return null;
        }
    }
}
