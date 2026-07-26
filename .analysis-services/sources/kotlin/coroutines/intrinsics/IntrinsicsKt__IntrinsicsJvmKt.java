package kotlin.coroutines.intrinsics;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: IntrinsicsJvm.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aF\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u00012\u001c\b\u0004\u0010\u0005\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006H\u0083\b¢\u0006\u0002\b\b\u001a'\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0002¢\u0006\u0002\b\n\u001aA\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007¢\u0006\u0002\u0010\f\u001aZ\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000e¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007¢\u0006\u0002\u0010\u0011\u001a\u001e\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001a>\u0010\u0013\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b¢\u0006\u0002\u0010\u0014\u001aW\u0010\u0013\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000e¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b¢\u0006\u0002\u0010\u0015\u001ak\u0010\u0013\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0016\"\u0004\b\u0002\u0010\u0003*)\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0017¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\u0006\u0010\u0018\u001a\u0002H\u00162\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0081\b¢\u0006\u0002\u0010\u0019\u001a=\u0010\u001a\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0001¢\u0006\u0002\u0010\u0014\u001aV\u0010\u001a\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000e¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0001¢\u0006\u0002\u0010\u0015\u001aj\u0010\u001a\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0016\"\u0004\b\u0002\u0010\u0003*)\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0017¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\u0006\u0010\u0018\u001a\u0002H\u00162\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0001¢\u0006\u0002\u0010\u0019¨\u0006\u001b"}, d2 = {"createCoroutineFromSuspendFunction", "Lkotlin/coroutines/Continuation;", "", "T", "completion", "block", "Lkotlin/Function1;", "", "createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt", "createSimpleCoroutineForSuspendFunction", "createSimpleCoroutineForSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt", "createCoroutineUnintercepted", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "intercepted", "startCoroutineUninterceptedOrReturn", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", com.android.server.integrity.parser.RuleMetadataParser.RULE_PROVIDER_TAG, "Lkotlin/Function3;", "param", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "wrapWithContinuationImpl", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/coroutines/intrinsics/IntrinsicsKt")
public class IntrinsicsKt__IntrinsicsJvmKt {
    private static final <T> java.lang.Object startCoroutineUninterceptedOrReturn(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        return !(function1 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(function1, completion) : ((kotlin.jvm.functions.Function1) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(completion);
    }

    public static final <T> java.lang.Object wrapWithContinuationImpl(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        kotlin.coroutines.Continuation newCompletion = createSimpleCoroutineForSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(completion));
        return ((kotlin.jvm.functions.Function1) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(newCompletion);
    }

    private static final <R, T> java.lang.Object startCoroutineUninterceptedOrReturn(kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, R r, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        return !(function2 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(function2, r, completion) : ((kotlin.jvm.functions.Function2) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(r, completion);
    }

    public static final <R, T> java.lang.Object wrapWithContinuationImpl(kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, R r, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        kotlin.coroutines.Continuation newCompletion = createSimpleCoroutineForSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(completion));
        return ((kotlin.jvm.functions.Function2) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(r, newCompletion);
    }

    private static final <R, P, T> java.lang.Object startCoroutineUninterceptedOrReturn(kotlin.jvm.functions.Function3<? super R, ? super P, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function3, R r, P p, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function3, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        return !(function3 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(function3, r, p, completion) : ((kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function3, 3)).invoke(r, p, completion);
    }

    public static final <R, P, T> java.lang.Object wrapWithContinuationImpl(kotlin.jvm.functions.Function3<? super R, ? super P, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function3, R r, P p, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function3, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        kotlin.coroutines.Continuation newCompletion = createSimpleCoroutineForSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(completion));
        return ((kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function3, 3)).invoke(r, p, newCompletion);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> kotlin.coroutines.Continuation<kotlin.Unit> createCoroutineUnintercepted(final kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.coroutines.jvm.internal.ContinuationImpl continuationImpl;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        final kotlin.coroutines.Continuation<?> continuationProbeCoroutineCreated = kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(completion);
        if (function1 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) {
            return ((kotlin.coroutines.jvm.internal.BaseContinuationImpl) function1).create(continuationProbeCoroutineCreated);
        }
        final kotlin.coroutines.CoroutineContext context$iv = continuationProbeCoroutineCreated.getContext();
        if (context$iv == kotlin.coroutines.EmptyCoroutineContext.INSTANCE) {
            continuationImpl = new kotlin.coroutines.jvm.internal.RestrictedContinuationImpl(continuationProbeCoroutineCreated, function1) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$1
                final /* synthetic */ kotlin.jvm.functions.Function1 $this_createCoroutineUnintercepted$inlined;
                private int label;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(continuationProbeCoroutineCreated);
                    this.$this_createCoroutineUnintercepted$inlined = function1;
                    kotlin.jvm.internal.Intrinsics.checkNotNull(continuationProbeCoroutineCreated, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                protected java.lang.Object invokeSuspend(java.lang.Object result) throws java.lang.Throwable {
                    switch (this.label) {
                        case 0:
                            this.label = 1;
                            kotlin.ResultKt.throwOnFailure(result);
                            kotlin.jvm.internal.Intrinsics.checkNotNull(this.$this_createCoroutineUnintercepted$inlined, "null cannot be cast to non-null type kotlin.Function1<kotlin.coroutines.Continuation<T of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$0>, kotlin.Any?>");
                            return ((kotlin.jvm.functions.Function1) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(this.$this_createCoroutineUnintercepted$inlined, 1)).invoke(this);
                        case 1:
                            this.label = 2;
                            kotlin.ResultKt.throwOnFailure(result);
                            return result;
                        default:
                            throw new java.lang.IllegalStateException("This coroutine had already completed".toString());
                    }
                }
            };
        } else {
            continuationImpl = new kotlin.coroutines.jvm.internal.ContinuationImpl(continuationProbeCoroutineCreated, context$iv, function1) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$2
                final /* synthetic */ kotlin.jvm.functions.Function1 $this_createCoroutineUnintercepted$inlined;
                private int label;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(continuationProbeCoroutineCreated, context$iv);
                    this.$this_createCoroutineUnintercepted$inlined = function1;
                    kotlin.jvm.internal.Intrinsics.checkNotNull(continuationProbeCoroutineCreated, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                protected java.lang.Object invokeSuspend(java.lang.Object result) throws java.lang.Throwable {
                    switch (this.label) {
                        case 0:
                            this.label = 1;
                            kotlin.ResultKt.throwOnFailure(result);
                            kotlin.jvm.internal.Intrinsics.checkNotNull(this.$this_createCoroutineUnintercepted$inlined, "null cannot be cast to non-null type kotlin.Function1<kotlin.coroutines.Continuation<T of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$0>, kotlin.Any?>");
                            return ((kotlin.jvm.functions.Function1) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(this.$this_createCoroutineUnintercepted$inlined, 1)).invoke(this);
                        case 1:
                            this.label = 2;
                            kotlin.ResultKt.throwOnFailure(result);
                            return result;
                        default:
                            throw new java.lang.IllegalStateException("This coroutine had already completed".toString());
                    }
                }
            };
        }
        return continuationImpl;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <R, T> kotlin.coroutines.Continuation<kotlin.Unit> createCoroutineUnintercepted(final kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, final R r, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.coroutines.jvm.internal.ContinuationImpl continuationImpl;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        final kotlin.coroutines.Continuation<?> continuationProbeCoroutineCreated = kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(completion);
        if (function2 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) {
            return ((kotlin.coroutines.jvm.internal.BaseContinuationImpl) function2).create(r, continuationProbeCoroutineCreated);
        }
        final kotlin.coroutines.CoroutineContext context$iv = continuationProbeCoroutineCreated.getContext();
        if (context$iv == kotlin.coroutines.EmptyCoroutineContext.INSTANCE) {
            continuationImpl = new kotlin.coroutines.jvm.internal.RestrictedContinuationImpl(continuationProbeCoroutineCreated, function2, r) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$3
                final /* synthetic */ java.lang.Object $receiver$inlined;
                final /* synthetic */ kotlin.jvm.functions.Function2 $this_createCoroutineUnintercepted$inlined;
                private int label;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(continuationProbeCoroutineCreated);
                    this.$this_createCoroutineUnintercepted$inlined = function2;
                    this.$receiver$inlined = r;
                    kotlin.jvm.internal.Intrinsics.checkNotNull(continuationProbeCoroutineCreated, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                protected java.lang.Object invokeSuspend(java.lang.Object result) throws java.lang.Throwable {
                    switch (this.label) {
                        case 0:
                            this.label = 1;
                            kotlin.ResultKt.throwOnFailure(result);
                            kotlin.jvm.internal.Intrinsics.checkNotNull(this.$this_createCoroutineUnintercepted$inlined, "null cannot be cast to non-null type kotlin.Function2<R of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$1, kotlin.coroutines.Continuation<T of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$1>, kotlin.Any?>");
                            return ((kotlin.jvm.functions.Function2) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(this.$this_createCoroutineUnintercepted$inlined, 2)).invoke(this.$receiver$inlined, this);
                        case 1:
                            this.label = 2;
                            kotlin.ResultKt.throwOnFailure(result);
                            return result;
                        default:
                            throw new java.lang.IllegalStateException("This coroutine had already completed".toString());
                    }
                }
            };
        } else {
            continuationImpl = new kotlin.coroutines.jvm.internal.ContinuationImpl(continuationProbeCoroutineCreated, context$iv, function2, r) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$4
                final /* synthetic */ java.lang.Object $receiver$inlined;
                final /* synthetic */ kotlin.jvm.functions.Function2 $this_createCoroutineUnintercepted$inlined;
                private int label;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(continuationProbeCoroutineCreated, context$iv);
                    this.$this_createCoroutineUnintercepted$inlined = function2;
                    this.$receiver$inlined = r;
                    kotlin.jvm.internal.Intrinsics.checkNotNull(continuationProbeCoroutineCreated, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                protected java.lang.Object invokeSuspend(java.lang.Object result) throws java.lang.Throwable {
                    switch (this.label) {
                        case 0:
                            this.label = 1;
                            kotlin.ResultKt.throwOnFailure(result);
                            kotlin.jvm.internal.Intrinsics.checkNotNull(this.$this_createCoroutineUnintercepted$inlined, "null cannot be cast to non-null type kotlin.Function2<R of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$1, kotlin.coroutines.Continuation<T of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$1>, kotlin.Any?>");
                            return ((kotlin.jvm.functions.Function2) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(this.$this_createCoroutineUnintercepted$inlined, 2)).invoke(this.$receiver$inlined, this);
                        case 1:
                            this.label = 2;
                            kotlin.ResultKt.throwOnFailure(result);
                            return result;
                        default:
                            throw new java.lang.IllegalStateException("This coroutine had already completed".toString());
                    }
                }
            };
        }
        return continuationImpl;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> kotlin.coroutines.Continuation<T> intercepted(kotlin.coroutines.Continuation<? super T> continuation) {
        kotlin.coroutines.Continuation<T> continuation2;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "<this>");
        kotlin.coroutines.jvm.internal.ContinuationImpl continuationImpl = continuation instanceof kotlin.coroutines.jvm.internal.ContinuationImpl ? (kotlin.coroutines.jvm.internal.ContinuationImpl) continuation : null;
        return (continuationImpl == null || (continuation2 = (kotlin.coroutines.Continuation<T>) continuationImpl.intercepted()) == null) ? continuation : continuation2;
    }

    private static final <T> kotlin.coroutines.Continuation<kotlin.Unit> createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(final kotlin.coroutines.Continuation<? super T> continuation, final kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1) {
        final kotlin.coroutines.CoroutineContext context = continuation.getContext();
        if (context == kotlin.coroutines.EmptyCoroutineContext.INSTANCE) {
            return new kotlin.coroutines.jvm.internal.RestrictedContinuationImpl(continuation, function1) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction$1
                final /* synthetic */ kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super T>, java.lang.Object> $block;
                private int label;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                /* JADX WARN: Multi-variable type inference failed */
                {
                    super(continuation);
                    this.$block = function1;
                    kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }

                /* JADX WARN: Type inference incomplete: some casts might be missing */
                /*  JADX ERROR: JadxRuntimeException in pass: ModVisitor
                    jadx.core.utils.exceptions.JadxRuntimeException: Can't change immutable type java.lang.Object to kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction$1 for r2v1 'this'  java.lang.Object
                    	at jadx.core.dex.instructions.args.SSAVar.setType(SSAVar.java:114)
                    	at jadx.core.dex.instructions.args.RegisterArg.setType(RegisterArg.java:52)
                    	at jadx.core.dex.visitors.ModVisitor.removeCheckCast(ModVisitor.java:417)
                    	at jadx.core.dex.visitors.ModVisitor.replaceStep(ModVisitor.java:152)
                    	at jadx.core.dex.visitors.ModVisitor.visit(ModVisitor.java:96)
                    */
                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                protected java.lang.Object invokeSuspend(java.lang.Object r3) {
                    /*
                        r2 = this;
                        int r0 = r2.label
                        switch(r0) {
                            case 0: goto L19;
                            case 1: goto L11;
                            default: goto L5;
                        }
                    L5:
                        java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                        java.lang.String r1 = "This coroutine had already completed"
                        java.lang.String r1 = r1.toString()
                        r0.<init>(r1)
                        throw r0
                    L11:
                        r0 = 2
                        r2.label = r0
                        kotlin.ResultKt.throwOnFailure(r3)
                        r0 = r3
                        goto L25
                    L19:
                        r0 = 1
                        r2.label = r0
                        kotlin.ResultKt.throwOnFailure(r3)
                        kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super T>, java.lang.Object> r0 = r2.$block
                        java.lang.Object r0 = r0.invoke(r2)
                    L25:
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction$1.invokeSuspend(java.lang.Object):java.lang.Object");
                }
            };
        }
        return new kotlin.coroutines.jvm.internal.ContinuationImpl(continuation, context, function1) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction$2
            final /* synthetic */ kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super T>, java.lang.Object> $block;
            private int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(continuation, context);
                this.$block = function1;
                kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            }

            /* JADX WARN: Type inference incomplete: some casts might be missing */
            /*  JADX ERROR: JadxRuntimeException in pass: ModVisitor
                jadx.core.utils.exceptions.JadxRuntimeException: Can't change immutable type java.lang.Object to kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction$2 for r2v1 'this'  java.lang.Object
                	at jadx.core.dex.instructions.args.SSAVar.setType(SSAVar.java:114)
                	at jadx.core.dex.instructions.args.RegisterArg.setType(RegisterArg.java:52)
                	at jadx.core.dex.visitors.ModVisitor.removeCheckCast(ModVisitor.java:417)
                	at jadx.core.dex.visitors.ModVisitor.replaceStep(ModVisitor.java:152)
                	at jadx.core.dex.visitors.ModVisitor.visit(ModVisitor.java:96)
                */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            protected java.lang.Object invokeSuspend(java.lang.Object r3) {
                /*
                    r2 = this;
                    int r0 = r2.label
                    switch(r0) {
                        case 0: goto L19;
                        case 1: goto L11;
                        default: goto L5;
                    }
                L5:
                    java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                    java.lang.String r1 = "This coroutine had already completed"
                    java.lang.String r1 = r1.toString()
                    r0.<init>(r1)
                    throw r0
                L11:
                    r0 = 2
                    r2.label = r0
                    kotlin.ResultKt.throwOnFailure(r3)
                    r0 = r3
                    goto L25
                L19:
                    r0 = 1
                    r2.label = r0
                    kotlin.ResultKt.throwOnFailure(r3)
                    kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super T>, java.lang.Object> r0 = r2.$block
                    java.lang.Object r0 = r0.invoke(r2)
                L25:
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction$2.invokeSuspend(java.lang.Object):java.lang.Object");
            }
        };
    }

    private static final <T> kotlin.coroutines.Continuation<T> createSimpleCoroutineForSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(final kotlin.coroutines.Continuation<? super T> continuation) {
        final kotlin.coroutines.CoroutineContext context = continuation.getContext();
        if (context == kotlin.coroutines.EmptyCoroutineContext.INSTANCE) {
            return new kotlin.coroutines.jvm.internal.RestrictedContinuationImpl(continuation) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createSimpleCoroutineForSuspendFunction$1
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(continuation);
                    kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                protected java.lang.Object invokeSuspend(java.lang.Object result) throws java.lang.Throwable {
                    kotlin.ResultKt.throwOnFailure(result);
                    return result;
                }
            };
        }
        return new kotlin.coroutines.jvm.internal.ContinuationImpl(continuation, context) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createSimpleCoroutineForSuspendFunction$2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(continuation, context);
                kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            protected java.lang.Object invokeSuspend(java.lang.Object result) throws java.lang.Throwable {
                kotlin.ResultKt.throwOnFailure(result);
                return result;
            }
        };
    }
}
