package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Lint.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\u0094\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0010\u0001\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010#\n\u0002\b\u0002\u001a\"\u0010\u000b\u001a\u00020\f*\u0006\u0012\u0002\b\u00030\u00022\u0010\b\u0002\u0010\r\u001a\n\u0018\u00010\u000ej\u0004\u0018\u0001`\u000fH\u0007\u001a\u001e\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u0013H\u0007\u001aj\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132D\b\b\u0010\u0015\u001a>\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00120\u0002\u0012\u0013\u0012\u00110\u0017¢\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0016¢\u0006\u0002\b\u001cH\u0087\b¢\u0006\u0002\u0010\u001d\u001a\u001e\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u001fH\u0007\u001a\u001e\u0010 \u001a\u00020!\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u0013H\u0087H¢\u0006\u0002\u0010\"\u001a\u001e\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u001fH\u0007\u001a&\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132\u0006\u0010%\u001a\u00020\u0001H\u0007\u001ac\u0010&\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132\b\b\u0002\u0010'\u001a\u00020(23\b\n\u0010)\u001a-\b\u0001\u0012\u0013\u0012\u00110\u0017¢\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0*H\u0087\b¢\u0006\u0002\u0010+\u001a\u007f\u0010,\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132Y\b\b\u0010)\u001aS\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00120\u0002\u0012\u0013\u0012\u00110\u0017¢\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110(¢\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(.\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0-¢\u0006\u0002\b\u001cH\u0087\b¢\u0006\u0002\u0010/\u001a$\u00100\u001a\b\u0012\u0004\u0012\u0002H\u001201\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u0013H\u0087H¢\u0006\u0002\u0010\"\u001a,\u00100\u001a\u000202\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132\f\u00103\u001a\b\u0012\u0004\u0012\u0002H\u001204H\u0087H¢\u0006\u0002\u00105\u001a$\u00106\u001a\b\u0012\u0004\u0012\u0002H\u001207\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u0013H\u0087H¢\u0006\u0002\u0010\"\u001a,\u00106\u001a\u000202\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132\f\u00103\u001a\b\u0012\u0004\u0012\u0002H\u001208H\u0087H¢\u0006\u0002\u00109\"\"\u0010\u0000\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u00028FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\"\u0010\u0007\u001a\u00020\b*\u0006\u0012\u0002\b\u00030\u00028FX\u0087\u0004¢\u0006\f\u0012\u0004\b\t\u0010\u0004\u001a\u0004\b\u0007\u0010\n¨\u0006:"}, d2 = {"coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "Lkotlinx/coroutines/flow/FlowCollector;", "getCoroutineContext$annotations", "(Lkotlinx/coroutines/flow/FlowCollector;)V", "getCoroutineContext", "(Lkotlinx/coroutines/flow/FlowCollector;)Lkotlin/coroutines/CoroutineContext;", "isActive", "", "isActive$annotations", "(Lkotlinx/coroutines/flow/FlowCollector;)Z", "cancel", "", "cause", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "cancellable", "Lkotlinx/coroutines/flow/Flow;", "T", "Lkotlinx/coroutines/flow/SharedFlow;", "catch", "action", "Lkotlin/Function3;", "", "Lkotlin/ParameterName;", "name", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/flow/SharedFlow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "conflate", "Lkotlinx/coroutines/flow/StateFlow;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, "", "(Lkotlinx/coroutines/flow/SharedFlow;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "distinctUntilChanged", "flowOn", "context", "retry", "retries", "", "predicate", "Lkotlin/Function2;", "(Lkotlinx/coroutines/flow/SharedFlow;JLkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "retryWhen", "Lkotlin/Function4;", "attempt", "(Lkotlinx/coroutines/flow/SharedFlow;Lkotlin/jvm/functions/Function4;)Lkotlinx/coroutines/flow/Flow;", "toList", "", "", "destination", "", "(Lkotlinx/coroutines/flow/SharedFlow;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toSet", "", "", "(Lkotlinx/coroutines/flow/SharedFlow;Ljava/util/Set;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class LintKt {
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "coroutineContext is resolved into the property of outer CoroutineScope which is likely to be an error.Use currentCoroutineContext() instead or specify the receiver of coroutineContext explicitly", replaceWith = @kotlin.ReplaceWith(expression = "currentCoroutineContext()", imports = {}))
    public static /* synthetic */ void getCoroutineContext$annotations(kotlinx.coroutines.flow.FlowCollector flowCollector) {
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "isActive is resolved into the extension of outer CoroutineScope which is likely to be an error.Use currentCoroutineContext().isActive or cancellable() operator instead or specify the receiver of isActive explicitly. Additionally, flow {} builder emissions are cancellable by default.", replaceWith = @kotlin.ReplaceWith(expression = "currentCoroutineContext().isActive", imports = {}))
    public static /* synthetic */ void isActive$annotations(kotlinx.coroutines.flow.FlowCollector flowCollector) {
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Applying 'cancellable' to a SharedFlow has no effect. See the SharedFlow documentation on Operator Fusion.", replaceWith = @kotlin.ReplaceWith(expression = "this", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> cancellable(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sharedFlow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Applying 'flowOn' to SharedFlow has no effect. See the SharedFlow documentation on Operator Fusion.", replaceWith = @kotlin.ReplaceWith(expression = "this", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> flowOn(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sharedFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Applying 'conflate' to StateFlow has no effect. See the StateFlow documentation on Operator Fusion.", replaceWith = @kotlin.ReplaceWith(expression = "this", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> conflate(kotlinx.coroutines.flow.StateFlow<? extends T> stateFlow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stateFlow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Applying 'distinctUntilChanged' to StateFlow has no effect. See the StateFlow documentation on Operator Fusion.", replaceWith = @kotlin.ReplaceWith(expression = "this", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> distinctUntilChanged(kotlinx.coroutines.flow.StateFlow<? extends T> stateFlow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stateFlow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    public static final boolean isActive(kotlinx.coroutines.flow.FlowCollector<?> flowCollector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flowCollector, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "cancel() is resolved into the extension of outer CoroutineScope which is likely to be an error.Use currentCoroutineContext().cancel() instead or specify the receiver of cancel() explicitly", replaceWith = @kotlin.ReplaceWith(expression = "currentCoroutineContext().cancel(cause)", imports = {}))
    public static final void cancel(kotlinx.coroutines.flow.FlowCollector<?> flowCollector, java.util.concurrent.CancellationException cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flowCollector, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    public static /* synthetic */ void cancel$default(kotlinx.coroutines.flow.FlowCollector flowCollector, java.util.concurrent.CancellationException cancellationException, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            cancellationException = null;
        }
        cancel(flowCollector, cancellationException);
    }

    public static final kotlin.coroutines.CoroutineContext getCoroutineContext(kotlinx.coroutines.flow.FlowCollector<?> flowCollector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flowCollector, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "SharedFlow never completes, so this operator typically has not effect, it can only catch exceptions from 'onSubscribe' operator", replaceWith = @kotlin.ReplaceWith(expression = "this", imports = {}))
    /* JADX INFO: renamed from: catch, reason: not valid java name */
    private static final <T> kotlinx.coroutines.flow.Flow<T> m12850catch(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super java.lang.Throwable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sharedFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return kotlinx.coroutines.flow.FlowKt.m12838catch(sharedFlow, action);
    }

    static /* synthetic */ kotlinx.coroutines.flow.Flow retry$default(kotlinx.coroutines.flow.SharedFlow $this$retry_u24default, long retries, kotlin.jvm.functions.Function2 predicate, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            retries = Long.MAX_VALUE;
        }
        if ((i & 2) != 0) {
            kotlin.jvm.functions.Function2 predicate2 = new kotlinx.coroutines.flow.LintKt.AnonymousClass1(null);
            predicate = predicate2;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$retry_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return kotlinx.coroutines.flow.FlowKt.retry($this$retry_u24default, retries, predicate);
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.LintKt$retry$1, reason: invalid class name */
    /* JADX INFO: compiled from: Lint.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "it", ""}, k = 3, mv = {1, 9, 0}, xi = 176)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.LintKt$retry$1", f = "Lint.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    public static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<java.lang.Throwable, kotlin.coroutines.Continuation<? super java.lang.Boolean>, java.lang.Object> {
        int label;

        public AnonymousClass1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.LintKt.AnonymousClass1> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return new kotlinx.coroutines.flow.LintKt.AnonymousClass1(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(java.lang.Throwable th, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
            return ((kotlinx.coroutines.flow.LintKt.AnonymousClass1) create(th, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
            kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure(obj);
                    return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(true);
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "SharedFlow never completes, so this operator has no effect.", replaceWith = @kotlin.ReplaceWith(expression = "this", imports = {}))
    private static final <T> kotlinx.coroutines.flow.Flow<T> retry(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, long retries, kotlin.jvm.functions.Function2<? super java.lang.Throwable, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sharedFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return kotlinx.coroutines.flow.FlowKt.retry(sharedFlow, retries, predicate);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "SharedFlow never completes, so this operator has no effect.", replaceWith = @kotlin.ReplaceWith(expression = "this", imports = {}))
    private static final <T> kotlinx.coroutines.flow.Flow<T> retryWhen(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, kotlin.jvm.functions.Function4<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super java.lang.Throwable, ? super java.lang.Long, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sharedFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return kotlinx.coroutines.flow.FlowKt.retryWhen(sharedFlow, predicate);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "SharedFlow never completes, so this terminal operation never completes.")
    private static final <T> java.lang.Object toList(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, kotlin.coroutines.Continuation<? super java.util.List<? extends T>> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNull(sharedFlow, "null cannot be cast to non-null type kotlinx.coroutines.flow.Flow<T of kotlinx.coroutines.flow.LintKt.toList>");
        kotlin.jvm.internal.InlineMarker.mark(0);
        java.lang.Object list$default = kotlinx.coroutines.flow.FlowKt__CollectionKt.toList$default(sharedFlow, null, continuation, 1, null);
        kotlin.jvm.internal.InlineMarker.mark(1);
        return list$default;
    }

    private static final <T> java.lang.Object toList(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, java.util.List<T> list, kotlin.coroutines.Continuation<?> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNull(sharedFlow, "null cannot be cast to non-null type kotlinx.coroutines.flow.Flow<T of kotlinx.coroutines.flow.LintKt.toList>");
        kotlin.jvm.internal.InlineMarker.mark(0);
        kotlinx.coroutines.flow.FlowKt.toList(sharedFlow, list, continuation);
        kotlin.jvm.internal.InlineMarker.mark(1);
        throw new java.lang.IllegalStateException("this code is supposed to be unreachable");
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "SharedFlow never completes, so this terminal operation never completes.")
    private static final <T> java.lang.Object toSet(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, kotlin.coroutines.Continuation<? super java.util.Set<? extends T>> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNull(sharedFlow, "null cannot be cast to non-null type kotlinx.coroutines.flow.Flow<T of kotlinx.coroutines.flow.LintKt.toSet>");
        kotlin.jvm.internal.InlineMarker.mark(0);
        java.lang.Object set$default = kotlinx.coroutines.flow.FlowKt__CollectionKt.toSet$default(sharedFlow, null, continuation, 1, null);
        kotlin.jvm.internal.InlineMarker.mark(1);
        return set$default;
    }

    private static final <T> java.lang.Object toSet(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, java.util.Set<T> set, kotlin.coroutines.Continuation<?> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNull(sharedFlow, "null cannot be cast to non-null type kotlinx.coroutines.flow.Flow<T of kotlinx.coroutines.flow.LintKt.toSet>");
        kotlin.jvm.internal.InlineMarker.mark(0);
        kotlinx.coroutines.flow.FlowKt.toSet(sharedFlow, set, continuation);
        kotlin.jvm.internal.InlineMarker.mark(1);
        throw new java.lang.IllegalStateException("this code is supposed to be unreachable");
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "SharedFlow never completes, so this terminal operation never completes.")
    private static final <T> java.lang.Object count(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, kotlin.coroutines.Continuation<? super java.lang.Integer> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNull(sharedFlow, "null cannot be cast to non-null type kotlinx.coroutines.flow.Flow<T of kotlinx.coroutines.flow.LintKt.count>");
        kotlin.jvm.internal.InlineMarker.mark(0);
        java.lang.Object objCount = kotlinx.coroutines.flow.FlowKt.count(sharedFlow, continuation);
        kotlin.jvm.internal.InlineMarker.mark(1);
        return objCount;
    }
}
