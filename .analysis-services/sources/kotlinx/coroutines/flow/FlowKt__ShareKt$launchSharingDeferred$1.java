package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Share.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharingDeferred$1", f = "Share.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.REBOOT_ESCROW_LSKF_CAPTURE_REPORTED}, m = "invokeSuspend", n = {}, s = {})
final class FlowKt__ShareKt$launchSharingDeferred$1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
    final /* synthetic */ kotlinx.coroutines.CompletableDeferred<kotlinx.coroutines.flow.StateFlow<T>> $result;
    final /* synthetic */ kotlinx.coroutines.flow.Flow<T> $upstream;
    private /* synthetic */ java.lang.Object L$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    FlowKt__ShareKt$launchSharingDeferred$1(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CompletableDeferred<kotlinx.coroutines.flow.StateFlow<T>> completableDeferred, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharingDeferred$1> continuation) {
        super(2, continuation);
        this.$upstream = flow;
        this.$result = completableDeferred;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
        kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharingDeferred$1 flowKt__ShareKt$launchSharingDeferred$1 = new kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharingDeferred$1(this.$upstream, this.$result, continuation);
        flowKt__ShareKt$launchSharingDeferred$1.L$0 = obj;
        return flowKt__ShareKt$launchSharingDeferred$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return ((kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharingDeferred$1) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
        java.lang.Throwable th;
        kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharingDeferred$1 flowKt__ShareKt$launchSharingDeferred$1;
        java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(obj);
                final kotlinx.coroutines.CoroutineScope coroutineScope = (kotlinx.coroutines.CoroutineScope) this.L$0;
                try {
                    final kotlin.jvm.internal.Ref.ObjectRef objectRef = new kotlin.jvm.internal.Ref.ObjectRef();
                    kotlinx.coroutines.flow.Flow<T> flow = this.$upstream;
                    final kotlinx.coroutines.CompletableDeferred<kotlinx.coroutines.flow.StateFlow<T>> completableDeferred = this.$result;
                    kotlinx.coroutines.flow.FlowCollector flowCollector = new kotlinx.coroutines.flow.FlowCollector() { // from class: kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharingDeferred$1.1
                        @Override // kotlinx.coroutines.flow.FlowCollector
                        public final java.lang.Object emit(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                            kotlin.Unit unit;
                            kotlinx.coroutines.flow.MutableStateFlow<T> mutableStateFlow = objectRef.element;
                            if (mutableStateFlow != null) {
                                mutableStateFlow.setValue(t);
                                unit = kotlin.Unit.INSTANCE;
                            } else {
                                unit = null;
                            }
                            if (unit == null) {
                                kotlinx.coroutines.CoroutineScope coroutineScope2 = coroutineScope;
                                kotlin.jvm.internal.Ref.ObjectRef<kotlinx.coroutines.flow.MutableStateFlow<T>> objectRef2 = objectRef;
                                kotlinx.coroutines.CompletableDeferred<kotlinx.coroutines.flow.StateFlow<T>> completableDeferred2 = completableDeferred;
                                T t2 = (T) kotlinx.coroutines.flow.StateFlowKt.MutableStateFlow(t);
                                completableDeferred2.complete(new kotlinx.coroutines.flow.ReadonlyStateFlow((kotlinx.coroutines.flow.StateFlow) t2, kotlinx.coroutines.JobKt.getJob(coroutineScope2.getCoroutineContext())));
                                objectRef2.element = t2;
                            }
                            return kotlin.Unit.INSTANCE;
                        }
                    };
                    this.label = 1;
                    if (flow.collect((kotlinx.coroutines.flow.FlowCollector<? super T>) flowCollector, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return kotlin.Unit.INSTANCE;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    flowKt__ShareKt$launchSharingDeferred$1 = this;
                    flowKt__ShareKt$launchSharingDeferred$1.$result.completeExceptionally(th);
                    throw th;
                }
            case 1:
                flowKt__ShareKt$launchSharingDeferred$1 = this;
                try {
                    kotlin.ResultKt.throwOnFailure(obj);
                    return kotlin.Unit.INSTANCE;
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    flowKt__ShareKt$launchSharingDeferred$1.$result.completeExceptionally(th);
                    throw th;
                }
            default:
                throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
