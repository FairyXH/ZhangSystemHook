package kotlinx.coroutines;

/* JADX INFO: compiled from: JobSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "This is internal API and may be removed in the future releases")
@kotlin.Metadata(d1 = {"\u0000â\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0001\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\b\u0017\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\n²\u0001³\u0001´\u0001µ\u0001¶\u0001B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J \u0010A\u001a\u00020\u00052\u0006\u0010B\u001a\u00020\u000b2\u0006\u0010C\u001a\u00020D2\u0006\u0010E\u001a\u00020FH\u0002J\u001e\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020\u00112\f\u0010J\u001a\b\u0012\u0004\u0012\u00020\u00110KH\u0002J\u0012\u0010L\u001a\u00020H2\b\u00108\u001a\u0004\u0018\u00010\u000bH\u0014J\u000e\u0010M\u001a\u00020\t2\u0006\u0010N\u001a\u00020\u0002J\u0010\u0010O\u001a\u0004\u0018\u00010\u000bH\u0084@¢\u0006\u0002\u0010PJ\u0010\u0010Q\u001a\u0004\u0018\u00010\u000bH\u0082@¢\u0006\u0002\u0010PJ\u0012\u0010R\u001a\u00020\u00052\b\u0010S\u001a\u0004\u0018\u00010\u0011H\u0017J\u0018\u0010R\u001a\u00020H2\u000e\u0010S\u001a\n\u0018\u00010Tj\u0004\u0018\u0001`UH\u0016J\u0010\u0010V\u001a\u00020\u00052\b\u0010S\u001a\u0004\u0018\u00010\u0011J\u0017\u0010W\u001a\u00020\u00052\b\u0010S\u001a\u0004\u0018\u00010\u000bH\u0000¢\u0006\u0002\bXJ\u0010\u0010Y\u001a\u00020H2\u0006\u0010S\u001a\u00020\u0011H\u0016J\u0014\u0010Z\u001a\u0004\u0018\u00010\u000b2\b\u0010S\u001a\u0004\u0018\u00010\u000bH\u0002J\u0010\u0010[\u001a\u00020\u00052\u0006\u0010S\u001a\u00020\u0011H\u0002J\b\u0010\\\u001a\u00020]H\u0014J\u0010\u0010^\u001a\u00020\u00052\u0006\u0010S\u001a\u00020\u0011H\u0016J\u001a\u0010_\u001a\u00020H2\u0006\u00108\u001a\u00020?2\b\u0010`\u001a\u0004\u0018\u00010\u000bH\u0002J\"\u0010a\u001a\u00020H2\u0006\u00108\u001a\u00020b2\u0006\u0010c\u001a\u00020d2\b\u0010e\u001a\u0004\u0018\u00010\u000bH\u0002J\u0012\u0010f\u001a\u00020\u00112\b\u0010S\u001a\u0004\u0018\u00010\u000bH\u0002J&\u0010g\u001a\u00020h2\n\b\u0002\u0010i\u001a\u0004\u0018\u00010]2\n\b\u0002\u0010S\u001a\u0004\u0018\u00010\u0011H\u0080\b¢\u0006\u0002\bjJ\u001c\u0010k\u001a\u0004\u0018\u00010\u000b2\u0006\u00108\u001a\u00020b2\b\u0010e\u001a\u0004\u0018\u00010\u000bH\u0002J\u0012\u0010l\u001a\u0004\u0018\u00010d2\u0006\u00108\u001a\u00020?H\u0002J\n\u0010m\u001a\u00060Tj\u0002`UJ\f\u0010n\u001a\u00060Tj\u0002`UH\u0016J\u000f\u0010o\u001a\u0004\u0018\u00010\u000bH\u0000¢\u0006\u0002\bpJ\b\u0010q\u001a\u0004\u0018\u00010\u0011J \u0010r\u001a\u0004\u0018\u00010\u00112\u0006\u00108\u001a\u00020b2\f\u0010J\u001a\b\u0012\u0004\u0012\u00020\u00110KH\u0002J\u0012\u0010s\u001a\u0004\u0018\u00010D2\u0006\u00108\u001a\u00020?H\u0002J\u0010\u0010t\u001a\u00020\u00052\u0006\u0010u\u001a\u00020\u0011H\u0014J\u0015\u0010v\u001a\u00020H2\u0006\u0010u\u001a\u00020\u0011H\u0010¢\u0006\u0002\bwJ\u0012\u0010x\u001a\u00020H2\b\u0010/\u001a\u0004\u0018\u00010\u0001H\u0004JA\u0010y\u001a\u00020z2\u0006\u0010{\u001a\u00020\u00052\u0006\u0010|\u001a\u00020\u00052)\u0010}\u001a%\u0012\u0016\u0012\u0014\u0018\u00010\u0011¢\u0006\r\b\u007f\u0012\t\b\u0080\u0001\u0012\u0004\b\b(S\u0012\u0004\u0012\u00020H0~j\u0003`\u0081\u0001J1\u0010y\u001a\u00020z2)\u0010}\u001a%\u0012\u0016\u0012\u0014\u0018\u00010\u0011¢\u0006\r\b\u007f\u0012\t\b\u0080\u0001\u0012\u0004\b\b(S\u0012\u0004\u0012\u00020H0~j\u0003`\u0081\u0001J\u000f\u0010\u0082\u0001\u001a\u00020HH\u0086@¢\u0006\u0002\u0010PJ\t\u0010\u0083\u0001\u001a\u00020\u0005H\u0002J\u000f\u0010\u0084\u0001\u001a\u00020HH\u0082@¢\u0006\u0002\u0010PJ\"\u0010\u0085\u0001\u001a\u00030\u0086\u00012\u0015\u0010\u0087\u0001\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0012\u0004\u0012\u00020H0~H\u0082\bJ\u0015\u0010\u0088\u0001\u001a\u0004\u0018\u00010\u000b2\b\u0010S\u001a\u0004\u0018\u00010\u000bH\u0002J\u0019\u0010\u0089\u0001\u001a\u00020\u00052\b\u0010e\u001a\u0004\u0018\u00010\u000bH\u0000¢\u0006\u0003\b\u008a\u0001J\u001b\u0010\u008b\u0001\u001a\u0004\u0018\u00010\u000b2\b\u0010e\u001a\u0004\u0018\u00010\u000bH\u0000¢\u0006\u0003\b\u008c\u0001J<\u0010\u008d\u0001\u001a\u00020F2)\u0010}\u001a%\u0012\u0016\u0012\u0014\u0018\u00010\u0011¢\u0006\r\b\u007f\u0012\t\b\u0080\u0001\u0012\u0004\b\b(S\u0012\u0004\u0012\u00020H0~j\u0003`\u0081\u00012\u0006\u0010{\u001a\u00020\u0005H\u0002J\u000f\u0010\u008e\u0001\u001a\u00020]H\u0010¢\u0006\u0003\b\u008f\u0001J\u0019\u0010\u0090\u0001\u001a\u00020H2\u0006\u0010C\u001a\u00020D2\u0006\u0010S\u001a\u00020\u0011H\u0002J)\u0010\u0091\u0001\u001a\u00020H\"\u000b\b\u0000\u0010\u0092\u0001\u0018\u0001*\u00020F2\u0006\u0010C\u001a\u00020D2\b\u0010S\u001a\u0004\u0018\u00010\u0011H\u0082\bJ!\u0010\u0093\u0001\u001a\u0004\u0018\u00010\u000b2\t\u0010\u0094\u0001\u001a\u0004\u0018\u00010\u000b2\t\u0010\u0095\u0001\u001a\u0004\u0018\u00010\u000bH\u0002J\"\u0010\u0096\u0001\u001a\u00020H2\f\u0010\u0097\u0001\u001a\u0007\u0012\u0002\b\u00030\u0098\u00012\t\u0010\u0094\u0001\u001a\u0004\u0018\u00010\u000bH\u0002J\u0012\u0010{\u001a\u00020H2\b\u0010S\u001a\u0004\u0018\u00010\u0011H\u0014J\u0013\u0010\u0099\u0001\u001a\u00020H2\b\u00108\u001a\u0004\u0018\u00010\u000bH\u0014J\t\u0010\u009a\u0001\u001a\u00020HH\u0014J\u0010\u0010\u009b\u0001\u001a\u00020H2\u0007\u0010\u009c\u0001\u001a\u00020\u0003J\u0012\u0010\u009d\u0001\u001a\u00020H2\u0007\u00108\u001a\u00030\u009e\u0001H\u0002J\u0011\u0010\u009f\u0001\u001a\u00020H2\u0006\u00108\u001a\u00020FH\u0002J\"\u0010 \u0001\u001a\u00020H2\f\u0010\u0097\u0001\u001a\u0007\u0012\u0002\b\u00030\u0098\u00012\t\u0010\u0094\u0001\u001a\u0004\u0018\u00010\u000bH\u0002J\u0017\u0010¡\u0001\u001a\u00020H2\u0006\u0010E\u001a\u00020FH\u0000¢\u0006\u0003\b¢\u0001J\u0007\u0010£\u0001\u001a\u00020\u0005J\u0014\u0010¤\u0001\u001a\u00030¥\u00012\b\u00108\u001a\u0004\u0018\u00010\u000bH\u0002J\u0013\u0010¦\u0001\u001a\u00020]2\b\u00108\u001a\u0004\u0018\u00010\u000bH\u0002J\t\u0010§\u0001\u001a\u00020]H\u0007J\t\u0010¨\u0001\u001a\u00020]H\u0016J\u001b\u0010©\u0001\u001a\u00020\u00052\u0006\u00108\u001a\u00020?2\b\u0010`\u001a\u0004\u0018\u00010\u000bH\u0002J\u0019\u0010ª\u0001\u001a\u00020\u00052\u0006\u00108\u001a\u00020?2\u0006\u0010I\u001a\u00020\u0011H\u0002J\u001f\u0010«\u0001\u001a\u0004\u0018\u00010\u000b2\b\u00108\u001a\u0004\u0018\u00010\u000b2\b\u0010e\u001a\u0004\u0018\u00010\u000bH\u0002J\u001d\u0010¬\u0001\u001a\u0004\u0018\u00010\u000b2\u0006\u00108\u001a\u00020?2\b\u0010e\u001a\u0004\u0018\u00010\u000bH\u0002J$\u0010\u00ad\u0001\u001a\u00020\u00052\u0006\u00108\u001a\u00020b2\u0006\u0010N\u001a\u00020d2\b\u0010e\u001a\u0004\u0018\u00010\u000bH\u0082\u0010J\u0010\u0010®\u0001\u001a\u0004\u0018\u00010d*\u00030¯\u0001H\u0002J\u0017\u0010°\u0001\u001a\u00020H*\u00020D2\b\u0010S\u001a\u0004\u0018\u00010\u0011H\u0002J\u001d\u0010±\u0001\u001a\u00060Tj\u0002`U*\u00020\u00112\n\b\u0002\u0010i\u001a\u0004\u0018\u00010]H\u0004R\u0016\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0010\u001a\u0004\u0018\u00010\u00118DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u00058DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0017\u001a\u00020\u00058PX\u0090\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0016R\u0014\u0010\u0019\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0016R\u0011\u0010\u001a\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u0016R\u0011\u0010\u001b\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0016R\u0011\u0010\u001c\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u0016R\u0014\u0010\u001d\u001a\u00020\u00058TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u0016R\u0015\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u001f8F¢\u0006\u0006\u001a\u0004\b \u0010!R\u001e\u0010\"\u001a\u0006\u0012\u0002\b\u00030#8DX\u0084\u0004¢\u0006\f\u0012\u0004\b$\u0010%\u001a\u0004\b&\u0010'R\u0014\u0010(\u001a\u00020\u00058PX\u0090\u0004¢\u0006\u0006\u001a\u0004\b)\u0010\u0016R\u0017\u0010*\u001a\u00020+8F¢\u0006\f\u0012\u0004\b,\u0010%\u001a\u0004\b-\u0010.R\u0016\u0010/\u001a\u0004\u0018\u00010\u00018VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b0\u00101R(\u00103\u001a\u0004\u0018\u00010\t2\b\u00102\u001a\u0004\u0018\u00010\t8@@@X\u0080\u000e¢\u0006\f\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u0016\u00108\u001a\u0004\u0018\u00010\u000b8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b9\u0010:R\u001c\u0010;\u001a\u0004\u0018\u00010\u0011*\u0004\u0018\u00010\u000b8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b<\u0010=R\u0018\u0010>\u001a\u00020\u0005*\u00020?8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b>\u0010@¨\u0006·\u0001"}, d2 = {"Lkotlinx/coroutines/JobSupport;", "Lkotlinx/coroutines/Job;", "Lkotlinx/coroutines/ChildJob;", "Lkotlinx/coroutines/ParentJob;", com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE, "", "(Z)V", "_parentHandle", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/ChildHandle;", "_state", "", "children", "Lkotlin/sequences/Sequence;", "getChildren", "()Lkotlin/sequences/Sequence;", "completionCause", "", "getCompletionCause", "()Ljava/lang/Throwable;", "completionCauseHandled", "getCompletionCauseHandled", "()Z", "handlesException", "getHandlesException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "isActive", "isCancelled", "isCompleted", "isCompletedExceptionally", "isScopedCoroutine", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/CoroutineContext$Key;", "onAwaitInternal", "Lkotlinx/coroutines/selects/SelectClause1;", "getOnAwaitInternal$annotations", "()V", "getOnAwaitInternal", "()Lkotlinx/coroutines/selects/SelectClause1;", "onCancelComplete", "getOnCancelComplete$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "onJoin", "Lkotlinx/coroutines/selects/SelectClause0;", "getOnJoin$annotations", "getOnJoin", "()Lkotlinx/coroutines/selects/SelectClause0;", "parent", "getParent", "()Lkotlinx/coroutines/Job;", "value", "parentHandle", "getParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Lkotlinx/coroutines/ChildHandle;", "setParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(Lkotlinx/coroutines/ChildHandle;)V", "state", "getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Ljava/lang/Object;", "exceptionOrNull", "getExceptionOrNull", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "isCancelling", "Lkotlinx/coroutines/Incomplete;", "(Lkotlinx/coroutines/Incomplete;)Z", "addLastAtomic", "expect", "list", "Lkotlinx/coroutines/NodeList;", "node", "Lkotlinx/coroutines/JobNode;", "addSuppressedExceptions", "", "rootCause", "exceptions", "", "afterCompletion", "attachChild", "child", "awaitInternal", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "awaitSuspend", "cancel", "cause", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "cancelCoroutine", "cancelImpl", "cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "cancelInternal", "cancelMakeCompleting", "cancelParent", "cancellationExceptionMessage", "", "childCancelled", "completeStateFinalization", "update", "continueCompleting", "Lkotlinx/coroutines/JobSupport$Finishing;", "lastChild", "Lkotlinx/coroutines/ChildHandleNode;", "proposedUpdate", "createCauseException", "defaultCancellationException", "Lkotlinx/coroutines/JobCancellationException;", "message", "defaultCancellationException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "finalizeFinishingState", "firstChild", "getCancellationException", "getChildJobCancellationCause", "getCompletedInternal", "getCompletedInternal$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "getCompletionExceptionOrNull", "getFinalRootCause", "getOrPromoteCancellingList", "handleJobException", "exception", "handleOnCompletionException", "handleOnCompletionException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "initParentJob", "invokeOnCompletion", "Lkotlinx/coroutines/DisposableHandle;", "onCancelling", "invokeImmediately", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/CompletionHandler;", "join", "joinInternal", "joinSuspend", "loopOnState", "", "block", "makeCancelling", "makeCompleting", "makeCompleting$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "makeCompletingOnce", "makeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "makeNode", "nameString", "nameString$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "notifyCancelling", "notifyHandlers", "T", "onAwaitInternalProcessResFunc", "ignoredParam", "result", "onAwaitInternalRegFunc", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "onCompletionInternal", "onStart", "parentCancelled", "parentJob", "promoteEmptyToNodeList", "Lkotlinx/coroutines/Empty;", "promoteSingleToNodeList", "registerSelectForOnJoin", "removeNode", "removeNode$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "start", "startInternal", "", "stateString", "toDebugString", "toString", "tryFinalizeSimpleState", "tryMakeCancelling", "tryMakeCompleting", "tryMakeCompletingSlowPath", "tryWaitForChild", "nextChild", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "notifyCompletion", "toCancellationException", "AwaitContinuation", "ChildCompletion", "Finishing", "SelectOnAwaitCompletionHandler", "SelectOnJoinCompletionHandler", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class JobSupport implements kotlinx.coroutines.Job, kotlinx.coroutines.ChildJob, kotlinx.coroutines.ParentJob {
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.ChildHandle> _parentHandle;
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> _state;

    protected static /* synthetic */ void getOnAwaitInternal$annotations() {
    }

    public static /* synthetic */ void getOnJoin$annotations() {
    }

    public JobSupport(boolean active) {
        this._state = kotlinx.atomicfu.AtomicFU.atomic(active ? kotlinx.coroutines.JobSupportKt.EMPTY_ACTIVE : kotlinx.coroutines.JobSupportKt.EMPTY_NEW);
        this._parentHandle = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
    }

    @Override // kotlinx.coroutines.Job
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public /* synthetic */ void cancel() {
        cancel((java.util.concurrent.CancellationException) null);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> function2) {
        return (R) kotlinx.coroutines.Job.DefaultImpls.fold(this, r, function2);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.CoroutineContext.Key<E> key) {
        return (E) kotlinx.coroutines.Job.DefaultImpls.get(this, key);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public kotlin.coroutines.CoroutineContext minusKey(kotlin.coroutines.CoroutineContext.Key<?> key) {
        return kotlinx.coroutines.Job.DefaultImpls.minusKey(this, key);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public kotlin.coroutines.CoroutineContext plus(kotlin.coroutines.CoroutineContext context) {
        return kotlinx.coroutines.Job.DefaultImpls.plus(this, context);
    }

    @Override // kotlinx.coroutines.Job
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
    public kotlinx.coroutines.Job plus(kotlinx.coroutines.Job other) {
        return kotlinx.coroutines.Job.DefaultImpls.plus((kotlinx.coroutines.Job) this, other);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element
    public final kotlin.coroutines.CoroutineContext.Key<?> getKey() {
        return kotlinx.coroutines.Job.INSTANCE;
    }

    public final kotlinx.coroutines.ChildHandle getParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return this._parentHandle.getValue();
    }

    public final void setParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.ChildHandle value) {
        this._parentHandle.setValue(value);
    }

    @Override // kotlinx.coroutines.Job
    public kotlinx.coroutines.Job getParent() {
        kotlinx.coroutines.ChildHandle parentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = getParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (parentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host != null) {
            return parentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host.getParent();
        }
        return null;
    }

    protected final void initParentJob(kotlinx.coroutines.Job parent) {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() == null)) {
                throw new java.lang.AssertionError();
            }
        }
        if (parent == null) {
            setParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.NonDisposableHandle.INSTANCE);
            return;
        }
        parent.start();
        kotlinx.coroutines.ChildHandle handle = parent.attachChild(this);
        setParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(handle);
        if (isCompleted()) {
            handle.dispose();
            setParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.NonDisposableHandle.INSTANCE);
        }
    }

    public final java.lang.Object getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._state;
        while (true) {
            java.lang.Object state = atomicRef.getValue();
            if (!(state instanceof kotlinx.coroutines.internal.OpDescriptor)) {
                return state;
            }
            ((kotlinx.coroutines.internal.OpDescriptor) state).perform(this);
        }
    }

    private final java.lang.Void loopOnState(kotlin.jvm.functions.Function1<java.lang.Object, kotlin.Unit> block) {
        while (true) {
            block.invoke(getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host());
        }
    }

    @Override // kotlinx.coroutines.Job
    public boolean isActive() {
        java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        return (state instanceof kotlinx.coroutines.Incomplete) && ((kotlinx.coroutines.Incomplete) state).getIsActive();
    }

    @Override // kotlinx.coroutines.Job
    public final boolean isCompleted() {
        return !(getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() instanceof kotlinx.coroutines.Incomplete);
    }

    @Override // kotlinx.coroutines.Job
    public final boolean isCancelled() {
        java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        return (state instanceof kotlinx.coroutines.CompletedExceptionally) || ((state instanceof kotlinx.coroutines.JobSupport.Finishing) && ((kotlinx.coroutines.JobSupport.Finishing) state).isCancelling());
    }

    private final java.lang.Object finalizeFinishingState(kotlinx.coroutines.JobSupport.Finishing state, java.lang.Object proposedUpdate) throws java.lang.Throwable {
        boolean wasCancelling;
        java.lang.Throwable finalCause;
        boolean handled = true;
        boolean z = false;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() == state ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!state.isSealed())) {
            throw new java.lang.AssertionError();
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !state.isCompleting()) {
            throw new java.lang.AssertionError();
        }
        kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker = null;
        kotlinx.coroutines.CompletedExceptionally completedExceptionally = proposedUpdate instanceof kotlinx.coroutines.CompletedExceptionally ? (kotlinx.coroutines.CompletedExceptionally) proposedUpdate : null;
        java.lang.Throwable proposedException = completedExceptionally != null ? completedExceptionally.cause : null;
        synchronized (state) {
            wasCancelling = state.isCancelling();
            java.util.List<java.lang.Throwable> listSealLocked = state.sealLocked(proposedException);
            finalCause = getFinalRootCause(state, listSealLocked);
            if (finalCause != null) {
                addSuppressedExceptions(finalCause, listSealLocked);
            }
        }
        java.lang.Object finalState = (finalCause == null || finalCause == proposedException) ? proposedUpdate : new kotlinx.coroutines.CompletedExceptionally(finalCause, z, 2, defaultConstructorMarker);
        if (finalCause != null) {
            if (!cancelParent(finalCause) && !handleJobException(finalCause)) {
                handled = false;
            }
            if (handled) {
                kotlin.jvm.internal.Intrinsics.checkNotNull(finalState, "null cannot be cast to non-null type kotlinx.coroutines.CompletedExceptionally");
                ((kotlinx.coroutines.CompletedExceptionally) finalState).makeHandled();
            }
        }
        if (!wasCancelling) {
            onCancelling(finalCause);
        }
        onCompletionInternal(finalState);
        boolean casSuccess = this._state.compareAndSet(state, kotlinx.coroutines.JobSupportKt.boxIncomplete(finalState));
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !casSuccess) {
            throw new java.lang.AssertionError();
        }
        completeStateFinalization(state, finalState);
        return finalState;
    }

    private final java.lang.Throwable getFinalRootCause(kotlinx.coroutines.JobSupport.Finishing state, java.util.List<? extends java.lang.Throwable> exceptions) {
        java.lang.Object element$iv;
        java.lang.Object obj = null;
        if (exceptions.isEmpty()) {
            if (state.isCancelling()) {
                return new kotlinx.coroutines.JobCancellationException(cancellationExceptionMessage(), null, this);
            }
            return null;
        }
        java.util.List<? extends java.lang.Throwable> $this$firstOrNull$iv = exceptions;
        java.util.Iterator it = $this$firstOrNull$iv.iterator();
        while (true) {
            if (it.hasNext()) {
                element$iv = it.next();
                if (!(((java.lang.Throwable) element$iv) instanceof java.util.concurrent.CancellationException)) {
                    break;
                }
            } else {
                element$iv = null;
                break;
            }
        }
        java.lang.Throwable firstNonCancellation = (java.lang.Throwable) element$iv;
        if (firstNonCancellation != null) {
            return firstNonCancellation;
        }
        java.lang.Throwable first = exceptions.get(0);
        if (first instanceof kotlinx.coroutines.TimeoutCancellationException) {
            java.util.List<? extends java.lang.Throwable> $this$firstOrNull$iv2 = exceptions;
            java.util.Iterator it2 = $this$firstOrNull$iv2.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                java.lang.Object element$iv2 = it2.next();
                java.lang.Throwable it3 = (java.lang.Throwable) element$iv2;
                if (it3 != first && (it3 instanceof kotlinx.coroutines.TimeoutCancellationException)) {
                    obj = element$iv2;
                    break;
                }
            }
            java.lang.Throwable detailedTimeoutException = (java.lang.Throwable) obj;
            if (detailedTimeoutException != null) {
                return detailedTimeoutException;
            }
        }
        return first;
    }

    private final void addSuppressedExceptions(java.lang.Throwable rootCause, java.util.List<? extends java.lang.Throwable> exceptions) {
        if (exceptions.size() <= 1) {
            return;
        }
        int expectedSize$iv = exceptions.size();
        java.util.Set seenExceptions = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap(expectedSize$iv));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(seenExceptions, "newSetFromMap(...)");
        java.lang.Throwable unwrappedCause = !kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() ? rootCause : kotlinx.coroutines.internal.StackTraceRecoveryKt.unwrapImpl(rootCause);
        for (java.lang.Throwable exception : exceptions) {
            java.lang.Throwable unwrapped = !kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() ? exception : kotlinx.coroutines.internal.StackTraceRecoveryKt.unwrapImpl(exception);
            if (unwrapped != rootCause && unwrapped != unwrappedCause && !(unwrapped instanceof java.util.concurrent.CancellationException) && seenExceptions.add(unwrapped)) {
                kotlin.ExceptionsKt.addSuppressed(rootCause, unwrapped);
            }
        }
    }

    private final boolean tryFinalizeSimpleState(kotlinx.coroutines.Incomplete state, java.lang.Object update) throws java.lang.Throwable {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((((state instanceof kotlinx.coroutines.Empty) || (state instanceof kotlinx.coroutines.JobNode)) ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!(update instanceof kotlinx.coroutines.CompletedExceptionally))) {
            throw new java.lang.AssertionError();
        }
        if (!this._state.compareAndSet(state, kotlinx.coroutines.JobSupportKt.boxIncomplete(update))) {
            return false;
        }
        onCancelling(null);
        onCompletionInternal(update);
        completeStateFinalization(state, update);
        return true;
    }

    private final void completeStateFinalization(kotlinx.coroutines.Incomplete state, java.lang.Object update) throws java.lang.Throwable {
        kotlinx.coroutines.ChildHandle it = getParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (it != null) {
            it.dispose();
            setParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.NonDisposableHandle.INSTANCE);
        }
        kotlinx.coroutines.CompletedExceptionally completedExceptionally = update instanceof kotlinx.coroutines.CompletedExceptionally ? (kotlinx.coroutines.CompletedExceptionally) update : null;
        java.lang.Throwable cause = completedExceptionally != null ? completedExceptionally.cause : null;
        if (state instanceof kotlinx.coroutines.JobNode) {
            try {
                ((kotlinx.coroutines.JobNode) state).invoke(cause);
                return;
            } catch (java.lang.Throwable ex) {
                handleOnCompletionException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(new kotlinx.coroutines.CompletionHandlerException("Exception in completion handler " + state + " for " + this, ex));
                return;
            }
        }
        kotlinx.coroutines.NodeList list = state.getList();
        if (list != null) {
            notifyCompletion(list, cause);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void notifyCancelling(kotlinx.coroutines.NodeList r17, java.lang.Throwable r18) throws java.lang.Throwable {
        /*
            r16 = this;
            r1 = r16
            r2 = r18
            r1.onCancelling(r2)
            r3 = r16
            r4 = 0
            r0 = 0
            r5 = r17
            kotlinx.coroutines.internal.LockFreeLinkedListHead r5 = (kotlinx.coroutines.internal.LockFreeLinkedListHead) r5
            r6 = 0
            java.lang.Object r7 = r5.getNext()
            java.lang.String r8 = "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }"
            kotlin.jvm.internal.Intrinsics.checkNotNull(r7, r8)
            kotlinx.coroutines.internal.LockFreeLinkedListNode r7 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r7
            r8 = r7
            r7 = r0
        L1e:
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r8, r5)
            if (r0 != 0) goto L72
            boolean r0 = r8 instanceof kotlinx.coroutines.JobCancellingNode
            if (r0 == 0) goto L6d
            r9 = r8
            kotlinx.coroutines.JobNode r9 = (kotlinx.coroutines.JobNode) r9
            r10 = 0
            r9.invoke(r2)     // Catch: java.lang.Throwable -> L31
            goto L6b
        L31:
            r0 = move-exception
            r11 = r0
            r0 = r11
            r11 = r7
            java.lang.Throwable r11 = (java.lang.Throwable) r11
            if (r11 == 0) goto L43
            r12 = r11
            r13 = 0
            r14 = r12
            r15 = 0
            kotlin.ExceptionsKt.addSuppressed(r14, r0)
            if (r11 != 0) goto L6b
        L43:
            r11 = r3
            r12 = 0
            kotlinx.coroutines.CompletionHandlerException r13 = new kotlinx.coroutines.CompletionHandlerException
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "Exception in completion handler "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.StringBuilder r14 = r14.append(r9)
            java.lang.String r15 = " for "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.StringBuilder r14 = r14.append(r11)
            java.lang.String r14 = r14.toString()
            r13.<init>(r14, r0)
            r7 = r13
            kotlin.Unit r11 = kotlin.Unit.INSTANCE
        L6b:
        L6d:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r8 = r8.getNextNode()
            goto L1e
        L72:
            r0 = r7
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            if (r0 == 0) goto L7c
            r5 = 0
            r3.handleOnCompletionException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r0)
        L7c:
            r1.cancelParent(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.notifyCancelling(kotlinx.coroutines.NodeList, java.lang.Throwable):void");
    }

    private final boolean cancelParent(java.lang.Throwable cause) {
        if (isScopedCoroutine()) {
            return true;
        }
        boolean isCancellation = cause instanceof java.util.concurrent.CancellationException;
        kotlinx.coroutines.ChildHandle parent = getParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (parent == null || parent == kotlinx.coroutines.NonDisposableHandle.INSTANCE) {
            return isCancellation;
        }
        return parent.childCancelled(cause) || isCancellation;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x003d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void notifyCompletion(kotlinx.coroutines.NodeList r16, java.lang.Throwable r17) throws java.lang.Throwable {
        /*
            r15 = this;
            r1 = r15
            r2 = 0
            r0 = 0
            r3 = r16
            kotlinx.coroutines.internal.LockFreeLinkedListHead r3 = (kotlinx.coroutines.internal.LockFreeLinkedListHead) r3
            r4 = 0
            java.lang.Object r5 = r3.getNext()
            java.lang.String r6 = "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }"
            kotlin.jvm.internal.Intrinsics.checkNotNull(r5, r6)
            kotlinx.coroutines.internal.LockFreeLinkedListNode r5 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r5
            r6 = r5
            r5 = r0
        L16:
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r6, r3)
            if (r0 != 0) goto L6f
            boolean r0 = r6 instanceof kotlinx.coroutines.JobNode
            if (r0 == 0) goto L67
            r7 = r6
            kotlinx.coroutines.JobNode r7 = (kotlinx.coroutines.JobNode) r7
            r8 = 0
            r9 = r17
            r7.invoke(r9)     // Catch: java.lang.Throwable -> L2b
            goto L65
        L2b:
            r0 = move-exception
            r10 = r0
            r0 = r10
            r10 = r5
            java.lang.Throwable r10 = (java.lang.Throwable) r10
            if (r10 == 0) goto L3d
            r11 = r10
            r12 = 0
            r13 = r11
            r14 = 0
            kotlin.ExceptionsKt.addSuppressed(r13, r0)
            if (r10 != 0) goto L65
        L3d:
            r10 = r1
            r11 = 0
            kotlinx.coroutines.CompletionHandlerException r12 = new kotlinx.coroutines.CompletionHandlerException
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "Exception in completion handler "
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.StringBuilder r13 = r13.append(r7)
            java.lang.String r14 = " for "
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.StringBuilder r13 = r13.append(r10)
            java.lang.String r13 = r13.toString()
            r12.<init>(r13, r0)
            r5 = r12
            kotlin.Unit r10 = kotlin.Unit.INSTANCE
        L65:
            goto L69
        L67:
            r9 = r17
        L69:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r6 = r6.getNextNode()
            goto L16
        L6f:
            r9 = r17
            r0 = r5
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            if (r0 == 0) goto L7a
            r3 = 0
            r1.handleOnCompletionException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r0)
        L7a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.notifyCompletion(kotlinx.coroutines.NodeList, java.lang.Throwable):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final /* synthetic */ <T extends kotlinx.coroutines.JobNode> void notifyHandlers(kotlinx.coroutines.NodeList r14, java.lang.Throwable r15) throws java.lang.Throwable {
        /*
            r13 = this;
            r0 = 0
            r1 = 0
            r2 = r14
            kotlinx.coroutines.internal.LockFreeLinkedListHead r2 = (kotlinx.coroutines.internal.LockFreeLinkedListHead) r2
            r3 = 0
            java.lang.Object r4 = r2.getNext()
            java.lang.String r5 = "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }"
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4, r5)
            kotlinx.coroutines.internal.LockFreeLinkedListNode r4 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r4
        L12:
            boolean r5 = kotlin.jvm.internal.Intrinsics.areEqual(r4, r2)
            if (r5 != 0) goto L71
            r5 = 3
            java.lang.String r6 = "T"
            kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(r5, r6)
            boolean r5 = r4 instanceof kotlinx.coroutines.internal.LockFreeLinkedListNode
            if (r5 == 0) goto L6c
            r5 = r4
            kotlinx.coroutines.JobNode r5 = (kotlinx.coroutines.JobNode) r5
            r6 = 0
            r5.invoke(r15)     // Catch: java.lang.Throwable -> L2b
            goto L6a
        L2b:
            r7 = move-exception
            r8 = r1
            java.lang.Throwable r8 = (java.lang.Throwable) r8
            if (r8 == 0) goto L40
            r9 = r8
            java.lang.Throwable r9 = (java.lang.Throwable) r9
            r10 = 0
            r11 = r9
            r12 = 0
            kotlin.ExceptionsKt.addSuppressed(r11, r7)
            r9 = r8
            java.lang.Throwable r9 = (java.lang.Throwable) r9
            if (r8 != 0) goto L6a
        L40:
            r8 = r13
            kotlinx.coroutines.JobSupport r8 = (kotlinx.coroutines.JobSupport) r8
            r9 = 0
            kotlinx.coroutines.CompletionHandlerException r10 = new kotlinx.coroutines.CompletionHandlerException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "Exception in completion handler "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.StringBuilder r11 = r11.append(r5)
            java.lang.String r12 = " for "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.StringBuilder r11 = r11.append(r8)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11, r7)
            r1 = r10
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
        L6a:
        L6c:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r4 = r4.getNextNode()
            goto L12
        L71:
            r2 = r1
            java.lang.Throwable r2 = (java.lang.Throwable) r2
            if (r2 == 0) goto L7d
            java.lang.Throwable r2 = (java.lang.Throwable) r2
            r3 = 0
            r13.handleOnCompletionException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r2)
        L7d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.notifyHandlers(kotlinx.coroutines.NodeList, java.lang.Throwable):void");
    }

    @Override // kotlinx.coroutines.Job
    public final boolean start() {
        while (true) {
            java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            switch (startInternal(state)) {
                case 0:
                    return false;
                case 1:
                    return true;
            }
        }
    }

    private final int startInternal(java.lang.Object state) {
        if (state instanceof kotlinx.coroutines.Empty) {
            if (((kotlinx.coroutines.Empty) state).getIsActive()) {
                return 0;
            }
            if (!this._state.compareAndSet(state, kotlinx.coroutines.JobSupportKt.EMPTY_ACTIVE)) {
                return -1;
            }
            onStart();
            return 1;
        }
        if (!(state instanceof kotlinx.coroutines.InactiveNodeList)) {
            return 0;
        }
        if (!this._state.compareAndSet(state, ((kotlinx.coroutines.InactiveNodeList) state).getList())) {
            return -1;
        }
        onStart();
        return 1;
    }

    protected void onStart() {
    }

    @Override // kotlinx.coroutines.Job
    public final java.util.concurrent.CancellationException getCancellationException() {
        java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (state instanceof kotlinx.coroutines.JobSupport.Finishing) {
            java.lang.Throwable rootCause = ((kotlinx.coroutines.JobSupport.Finishing) state).getRootCause();
            if (rootCause != null) {
                java.util.concurrent.CancellationException cancellationException = toCancellationException(rootCause, kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this) + " is cancelling");
                if (cancellationException != null) {
                    return cancellationException;
                }
            }
            throw new java.lang.IllegalStateException(("Job is still new or active: " + this).toString());
        }
        if (state instanceof kotlinx.coroutines.Incomplete) {
            throw new java.lang.IllegalStateException(("Job is still new or active: " + this).toString());
        }
        if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
            return toCancellationException$default(this, ((kotlinx.coroutines.CompletedExceptionally) state).cause, null, 1, null);
        }
        return new kotlinx.coroutines.JobCancellationException(kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this) + " has completed normally", null, this);
    }

    public static /* synthetic */ java.util.concurrent.CancellationException toCancellationException$default(kotlinx.coroutines.JobSupport jobSupport, java.lang.Throwable th, java.lang.String str, int i, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: toCancellationException");
        }
        if ((i & 1) != 0) {
            str = null;
        }
        return jobSupport.toCancellationException(th, str);
    }

    protected final java.util.concurrent.CancellationException toCancellationException(java.lang.Throwable $this$toCancellationException, java.lang.String message) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toCancellationException, "<this>");
        java.util.concurrent.CancellationException cancellationException = $this$toCancellationException instanceof java.util.concurrent.CancellationException ? (java.util.concurrent.CancellationException) $this$toCancellationException : null;
        if (cancellationException != null) {
            return cancellationException;
        }
        return new kotlinx.coroutines.JobCancellationException(message == null ? cancellationExceptionMessage() : message, $this$toCancellationException, this);
    }

    protected final java.lang.Throwable getCompletionCause() {
        java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (state instanceof kotlinx.coroutines.JobSupport.Finishing) {
            java.lang.Throwable rootCause = ((kotlinx.coroutines.JobSupport.Finishing) state).getRootCause();
            if (rootCause == null) {
                throw new java.lang.IllegalStateException(("Job is still new or active: " + this).toString());
            }
            return rootCause;
        }
        if (state instanceof kotlinx.coroutines.Incomplete) {
            throw new java.lang.IllegalStateException(("Job is still new or active: " + this).toString());
        }
        if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
            return ((kotlinx.coroutines.CompletedExceptionally) state).cause;
        }
        return null;
    }

    protected final boolean getCompletionCauseHandled() {
        java.lang.Object it = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        return (it instanceof kotlinx.coroutines.CompletedExceptionally) && ((kotlinx.coroutines.CompletedExceptionally) it).getHandled();
    }

    @Override // kotlinx.coroutines.Job
    public final kotlinx.coroutines.DisposableHandle invokeOnCompletion(kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> handler) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "handler");
        return invokeOnCompletion(false, true, handler);
    }

    @Override // kotlinx.coroutines.Job
    public final kotlinx.coroutines.DisposableHandle invokeOnCompletion(boolean onCancelling, boolean invokeImmediately, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> handler) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "handler");
        kotlinx.coroutines.JobNode node = makeNode(handler, onCancelling);
        while (true) {
            java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            if (state instanceof kotlinx.coroutines.Empty) {
                if (((kotlinx.coroutines.Empty) state).getIsActive()) {
                    if (this._state.compareAndSet(state, node)) {
                        return node;
                    }
                } else {
                    promoteEmptyToNodeList((kotlinx.coroutines.Empty) state);
                }
            } else if (state instanceof kotlinx.coroutines.Incomplete) {
                kotlinx.coroutines.NodeList list = ((kotlinx.coroutines.Incomplete) state).getList();
                if (list == null) {
                    kotlin.jvm.internal.Intrinsics.checkNotNull(state, "null cannot be cast to non-null type kotlinx.coroutines.JobNode");
                    promoteSingleToNodeList((kotlinx.coroutines.JobNode) state);
                } else {
                    java.lang.Object rootCause = null;
                    java.lang.Object handle = kotlinx.coroutines.NonDisposableHandle.INSTANCE;
                    if (onCancelling && (state instanceof kotlinx.coroutines.JobSupport.Finishing)) {
                        synchronized (state) {
                            rootCause = ((kotlinx.coroutines.JobSupport.Finishing) state).getRootCause();
                            if (rootCause != null && (!(handler instanceof kotlinx.coroutines.ChildHandleNode) || ((kotlinx.coroutines.JobSupport.Finishing) state).isCompleting())) {
                                kotlin.Unit unit = kotlin.Unit.INSTANCE;
                            } else if (addLastAtomic(state, list, node)) {
                                if (rootCause == null) {
                                    return node;
                                }
                                handle = node;
                                kotlin.Unit unit2 = kotlin.Unit.INSTANCE;
                            }
                        }
                    }
                    if (rootCause != null) {
                        if (invokeImmediately) {
                            java.lang.Object cause$iv = rootCause;
                            handler.invoke(cause$iv);
                        }
                        return (kotlinx.coroutines.DisposableHandle) handle;
                    }
                    if (addLastAtomic(state, list, node)) {
                        return node;
                    }
                }
            } else {
                if (invokeImmediately) {
                    kotlinx.coroutines.CompletedExceptionally completedExceptionally = state instanceof kotlinx.coroutines.CompletedExceptionally ? (kotlinx.coroutines.CompletedExceptionally) state : null;
                    java.lang.Throwable cause$iv2 = completedExceptionally != null ? completedExceptionally.cause : null;
                    handler.invoke(cause$iv2);
                }
                return kotlinx.coroutines.NonDisposableHandle.INSTANCE;
            }
        }
    }

    private final kotlinx.coroutines.JobNode makeNode(kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> handler, boolean onCancelling) {
        kotlinx.coroutines.InvokeOnCompletion node;
        if (onCancelling) {
            node = handler instanceof kotlinx.coroutines.JobCancellingNode ? (kotlinx.coroutines.JobCancellingNode) handler : null;
            if (node == null) {
                node = new kotlinx.coroutines.InvokeOnCancelling(handler);
            }
            node = node;
        } else {
            node = handler instanceof kotlinx.coroutines.JobNode ? (kotlinx.coroutines.JobNode) handler : null;
            if (node != null) {
                kotlinx.coroutines.JobNode it = node;
                if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!(it instanceof kotlinx.coroutines.JobCancellingNode))) {
                    throw new java.lang.AssertionError();
                }
            } else {
                node = new kotlinx.coroutines.InvokeOnCompletion(handler);
            }
        }
        node.setJob(this);
        return node;
    }

    private final boolean addLastAtomic(final java.lang.Object expect, kotlinx.coroutines.NodeList list, kotlinx.coroutines.JobNode node) {
        kotlinx.coroutines.NodeList this_$iv = list;
        final kotlinx.coroutines.JobNode jobNode = node;
        kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp condAdd$iv = new kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp(jobNode) { // from class: kotlinx.coroutines.JobSupport$addLastAtomic$$inlined$addLastIf$1
            @Override // kotlinx.coroutines.internal.AtomicOp
            public java.lang.Object prepare(kotlinx.coroutines.internal.LockFreeLinkedListNode affected) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(affected, "affected");
                if (this.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() == expect) {
                    return null;
                }
                return kotlinx.coroutines.internal.LockFreeLinkedListKt.getCONDITION_FALSE();
            }
        };
        while (true) {
            kotlinx.coroutines.internal.LockFreeLinkedListNode prev$iv = this_$iv.getPrevNode();
            switch (prev$iv.tryCondAddNext(node, this_$iv, condAdd$iv)) {
                case 1:
                    return true;
                case 2:
                    return false;
            }
        }
    }

    private final void promoteEmptyToNodeList(kotlinx.coroutines.Empty state) {
        kotlinx.coroutines.NodeList list = new kotlinx.coroutines.NodeList();
        kotlinx.coroutines.Incomplete update = state.getIsActive() ? list : new kotlinx.coroutines.InactiveNodeList(list);
        this._state.compareAndSet(state, update);
    }

    private final void promoteSingleToNodeList(kotlinx.coroutines.JobNode state) {
        state.addOneIfEmpty(new kotlinx.coroutines.NodeList());
        kotlinx.coroutines.internal.LockFreeLinkedListNode list = state.getNextNode();
        this._state.compareAndSet(state, list);
    }

    @Override // kotlinx.coroutines.Job
    public final java.lang.Object join(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        if (!joinInternal()) {
            kotlinx.coroutines.JobKt.ensureActive(continuation.getContext());
            return kotlin.Unit.INSTANCE;
        }
        java.lang.Object objJoinSuspend = joinSuspend(continuation);
        return objJoinSuspend == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objJoinSuspend : kotlin.Unit.INSTANCE;
    }

    private final boolean joinInternal() {
        java.lang.Object state;
        do {
            state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            if (!(state instanceof kotlinx.coroutines.Incomplete)) {
                return false;
            }
        } while (startInternal(state) < 0);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object joinSuspend(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        kotlinx.coroutines.CancellableContinuationImpl cont = cancellable$iv;
        kotlinx.coroutines.CompletionHandlerBase $this$asHandler$iv = new kotlinx.coroutines.ResumeOnCompletion(cont);
        kotlinx.coroutines.CancellableContinuationKt.disposeOnCancellation(cont, invokeOnCompletion($this$asHandler$iv));
        java.lang.Object result = cancellable$iv.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : kotlin.Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.Job
    public final kotlinx.coroutines.selects.SelectClause0 getOnJoin() {
        kotlinx.coroutines.JobSupport$onJoin$1 jobSupport$onJoin$1 = kotlinx.coroutines.JobSupport$onJoin$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(jobSupport$onJoin$1, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'select')] kotlinx.coroutines.selects.SelectInstance<*>, @[ParameterName(name = 'param')] kotlin.Any?, kotlin.Unit>{ kotlinx.coroutines.selects.SelectKt.RegistrationFunction }");
        return new kotlinx.coroutines.selects.SelectClause0Impl(this, (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(jobSupport$onJoin$1, 3), null, 4, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void registerSelectForOnJoin(kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object ignoredParam) {
        if (!joinInternal()) {
            select.selectInRegistrationPhase(kotlin.Unit.INSTANCE);
            return;
        }
        kotlinx.coroutines.CompletionHandlerBase $this$asHandler$iv = new kotlinx.coroutines.JobSupport.SelectOnJoinCompletionHandler(this, select);
        kotlinx.coroutines.DisposableHandle disposableHandle = invokeOnCompletion($this$asHandler$iv);
        select.disposeOnCompletion(disposableHandle);
    }

    /* JADX INFO: compiled from: JobSupport.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0096\u0002R\u0012\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/JobSupport$SelectOnJoinCompletionHandler;", "Lkotlinx/coroutines/JobNode;", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Lkotlinx/coroutines/JobSupport;Lkotlinx/coroutines/selects/SelectInstance;)V", "invoke", "", "cause", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class SelectOnJoinCompletionHandler extends kotlinx.coroutines.JobNode {
        private final kotlinx.coroutines.selects.SelectInstance<?> select;
        final /* synthetic */ kotlinx.coroutines.JobSupport this$0;

        public SelectOnJoinCompletionHandler(kotlinx.coroutines.JobSupport this$0, kotlinx.coroutines.selects.SelectInstance<?> select) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
            this.this$0 = this$0;
            this.select = select;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
            invoke2(th);
            return kotlin.Unit.INSTANCE;
        }

        @Override // kotlinx.coroutines.CompletionHandlerBase
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public void invoke2(java.lang.Throwable cause) {
            this.select.trySelect(this.this$0, kotlin.Unit.INSTANCE);
        }
    }

    public final void removeNode$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.JobNode node) {
        java.lang.Object state;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        do {
            state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            if (state instanceof kotlinx.coroutines.JobNode) {
                if (state != node) {
                    return;
                }
            } else {
                if (!(state instanceof kotlinx.coroutines.Incomplete) || ((kotlinx.coroutines.Incomplete) state).getList() == null) {
                    return;
                }
                node.mo12863remove();
                return;
            }
        } while (!this._state.compareAndSet(state, kotlinx.coroutines.JobSupportKt.EMPTY_ACTIVE));
    }

    public boolean getOnCancelComplete$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return false;
    }

    @Override // kotlinx.coroutines.Job
    public void cancel(java.util.concurrent.CancellationException cause) throws java.lang.Throwable {
        kotlinx.coroutines.JobCancellationException jobCancellationException;
        if (cause != null) {
            jobCancellationException = cause;
        } else {
            jobCancellationException = new kotlinx.coroutines.JobCancellationException(cancellationExceptionMessage(), null, this);
        }
        cancelInternal(jobCancellationException);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public java.lang.String cancellationExceptionMessage() {
        return "Job was cancelled";
    }

    @Override // kotlinx.coroutines.Job
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Added since 1.2.0 for binary compatibility with versions <= 1.1.x")
    public /* synthetic */ boolean cancel(java.lang.Throwable cause) throws java.lang.Throwable {
        kotlinx.coroutines.JobCancellationException jobCancellationException;
        if (cause == null || (jobCancellationException = toCancellationException$default(this, cause, null, 1, null)) == null) {
            jobCancellationException = new kotlinx.coroutines.JobCancellationException(cancellationExceptionMessage(), null, this);
        }
        cancelInternal(jobCancellationException);
        return true;
    }

    public void cancelInternal(java.lang.Throwable cause) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause);
    }

    @Override // kotlinx.coroutines.ChildJob
    public final void parentCancelled(kotlinx.coroutines.ParentJob parentJob) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parentJob, "parentJob");
        cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(parentJob);
    }

    public boolean childCancelled(java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        if (cause instanceof java.util.concurrent.CancellationException) {
            return true;
        }
        return cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause) && getHandlesException();
    }

    public final boolean cancelCoroutine(java.lang.Throwable cause) {
        return cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause);
    }

    public final boolean cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object cause) throws java.lang.Throwable {
        java.lang.Object finalState = kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY;
        if (getOnCancelComplete$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() && (finalState = cancelMakeCompleting(cause)) == kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            return true;
        }
        if (finalState == kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY) {
            finalState = makeCancelling(cause);
        }
        if (finalState == kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY || finalState == kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            return true;
        }
        if (finalState == kotlinx.coroutines.JobSupportKt.TOO_LATE_TO_CANCEL) {
            return false;
        }
        afterCompletion(finalState);
        return true;
    }

    private final java.lang.Object cancelMakeCompleting(java.lang.Object cause) {
        java.lang.Object finalState;
        do {
            java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            if (!(state instanceof kotlinx.coroutines.Incomplete) || ((state instanceof kotlinx.coroutines.JobSupport.Finishing) && ((kotlinx.coroutines.JobSupport.Finishing) state).isCompleting())) {
                return kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY;
            }
            kotlinx.coroutines.CompletedExceptionally proposedUpdate = new kotlinx.coroutines.CompletedExceptionally(createCauseException(cause), false, 2, null);
            finalState = tryMakeCompleting(state, proposedUpdate);
        } while (finalState == kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY);
        return finalState;
    }

    public static /* synthetic */ kotlinx.coroutines.JobCancellationException defaultCancellationException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host$default(kotlinx.coroutines.JobSupport $this, java.lang.String message, java.lang.Throwable cause, int i, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: defaultCancellationException");
        }
        if ((i & 1) != 0) {
            message = null;
        }
        if ((i & 2) != 0) {
            cause = null;
        }
        return new kotlinx.coroutines.JobCancellationException(message == null ? $this.cancellationExceptionMessage() : message, cause, $this);
    }

    public final kotlinx.coroutines.JobCancellationException defaultCancellationException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.String message, java.lang.Throwable cause) {
        return new kotlinx.coroutines.JobCancellationException(message == null ? cancellationExceptionMessage() : message, cause, this);
    }

    @Override // kotlinx.coroutines.ParentJob
    public java.util.concurrent.CancellationException getChildJobCancellationCause() {
        java.lang.Throwable rootCause;
        java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (state instanceof kotlinx.coroutines.JobSupport.Finishing) {
            rootCause = ((kotlinx.coroutines.JobSupport.Finishing) state).getRootCause();
        } else if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
            rootCause = ((kotlinx.coroutines.CompletedExceptionally) state).cause;
        } else {
            if (state instanceof kotlinx.coroutines.Incomplete) {
                throw new java.lang.IllegalStateException(("Cannot be cancelling child in this state: " + state).toString());
            }
            rootCause = null;
        }
        java.util.concurrent.CancellationException cancellationException = rootCause instanceof java.util.concurrent.CancellationException ? (java.util.concurrent.CancellationException) rootCause : null;
        if (cancellationException == null) {
            return new kotlinx.coroutines.JobCancellationException("Parent job is " + stateString(state), rootCause, this);
        }
        return cancellationException;
    }

    private final java.lang.Throwable createCauseException(java.lang.Object cause) {
        if (!(cause == null ? true : cause instanceof java.lang.Throwable)) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(cause, "null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
            return ((kotlinx.coroutines.ParentJob) cause).getChildJobCancellationCause();
        }
        java.lang.Throwable th = (java.lang.Throwable) cause;
        if (th != null) {
            return th;
        }
        return new kotlinx.coroutines.JobCancellationException(cancellationExceptionMessage(), null, this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final java.lang.Object makeCancelling(java.lang.Object cause) throws java.lang.Throwable {
        java.lang.Throwable th;
        java.lang.Throwable th2 = null;
        while (true) {
            java.lang.Throwable th3 = th2;
            java.lang.Object state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            java.lang.Object[] objArr = 0;
            if (state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host instanceof kotlinx.coroutines.JobSupport.Finishing) {
                synchronized (state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host) {
                    try {
                        if (((kotlinx.coroutines.JobSupport.Finishing) state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).isSealed()) {
                            return kotlinx.coroutines.JobSupportKt.TOO_LATE_TO_CANCEL;
                        }
                        boolean zIsCancelling = ((kotlinx.coroutines.JobSupport.Finishing) state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).isCancelling();
                        if (cause != null || !zIsCancelling) {
                            if (th3 == null) {
                                java.lang.Throwable thCreateCauseException = createCauseException(cause);
                                th = thCreateCauseException;
                                th3 = thCreateCauseException;
                            } else {
                                th = th3;
                            }
                            try {
                                ((kotlinx.coroutines.JobSupport.Finishing) state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).addExceptionLocked(th3);
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                                throw th;
                            }
                        }
                        java.lang.Throwable rootCause = zIsCancelling ? false : true ? ((kotlinx.coroutines.JobSupport.Finishing) state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).getRootCause() : null;
                        if (rootCause != null) {
                            notifyCancelling(((kotlinx.coroutines.JobSupport.Finishing) state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).getList(), rootCause);
                        }
                        return kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY;
                    } catch (java.lang.Throwable th5) {
                        th = th5;
                    }
                }
            } else {
                if (!(state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host instanceof kotlinx.coroutines.Incomplete)) {
                    return kotlinx.coroutines.JobSupportKt.TOO_LATE_TO_CANCEL;
                }
                if (th3 == null) {
                    java.lang.Throwable thCreateCauseException2 = createCauseException(cause);
                    th3 = thCreateCauseException2;
                    th2 = thCreateCauseException2;
                } else {
                    th2 = th3;
                }
                if (!((kotlinx.coroutines.Incomplete) state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).getIsActive()) {
                    java.lang.Object objTryMakeCompleting = tryMakeCompleting(state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host, new kotlinx.coroutines.CompletedExceptionally(th3, z, 2, objArr == true ? 1 : 0));
                    if (objTryMakeCompleting == kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY) {
                        throw new java.lang.IllegalStateException(("Cannot happen in " + state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).toString());
                    }
                    if (objTryMakeCompleting != kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY) {
                        return objTryMakeCompleting;
                    }
                } else if (tryMakeCancelling((kotlinx.coroutines.Incomplete) state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host, th3)) {
                    return kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY;
                }
            }
        }
    }

    private final kotlinx.coroutines.NodeList getOrPromoteCancellingList(kotlinx.coroutines.Incomplete state) {
        kotlinx.coroutines.NodeList list = state.getList();
        if (list == null) {
            if (state instanceof kotlinx.coroutines.Empty) {
                return new kotlinx.coroutines.NodeList();
            }
            if (state instanceof kotlinx.coroutines.JobNode) {
                promoteSingleToNodeList((kotlinx.coroutines.JobNode) state);
                return null;
            }
            throw new java.lang.IllegalStateException(("State should have list: " + state).toString());
        }
        return list;
    }

    private final boolean tryMakeCancelling(kotlinx.coroutines.Incomplete state, java.lang.Throwable rootCause) throws java.lang.Throwable {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!(state instanceof kotlinx.coroutines.JobSupport.Finishing))) {
            throw new java.lang.AssertionError();
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !state.getIsActive()) {
            throw new java.lang.AssertionError();
        }
        kotlinx.coroutines.NodeList list = getOrPromoteCancellingList(state);
        if (list == null) {
            return false;
        }
        kotlinx.coroutines.JobSupport.Finishing cancelling = new kotlinx.coroutines.JobSupport.Finishing(list, false, rootCause);
        if (!this._state.compareAndSet(state, cancelling)) {
            return false;
        }
        notifyCancelling(list, rootCause);
        return true;
    }

    public final boolean makeCompleting$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object proposedUpdate) {
        java.lang.Object finalState;
        do {
            java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            finalState = tryMakeCompleting(state, proposedUpdate);
            if (finalState == kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY) {
                return false;
            }
            if (finalState == kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN) {
                return true;
            }
        } while (finalState == kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY);
        afterCompletion(finalState);
        return true;
    }

    public final java.lang.Object makeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object proposedUpdate) {
        java.lang.Object finalState;
        do {
            java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            finalState = tryMakeCompleting(state, proposedUpdate);
            if (finalState == kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY) {
                throw new java.lang.IllegalStateException("Job " + this + " is already complete or completing, but is being completed with " + proposedUpdate, getExceptionOrNull(proposedUpdate));
            }
        } while (finalState == kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY);
        return finalState;
    }

    private final java.lang.Object tryMakeCompleting(java.lang.Object state, java.lang.Object proposedUpdate) {
        if (!(state instanceof kotlinx.coroutines.Incomplete)) {
            return kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY;
        }
        if (((state instanceof kotlinx.coroutines.Empty) || (state instanceof kotlinx.coroutines.JobNode)) && !(state instanceof kotlinx.coroutines.ChildHandleNode) && !(proposedUpdate instanceof kotlinx.coroutines.CompletedExceptionally)) {
            if (!tryFinalizeSimpleState((kotlinx.coroutines.Incomplete) state, proposedUpdate)) {
                return kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY;
            }
            return proposedUpdate;
        }
        return tryMakeCompletingSlowPath((kotlinx.coroutines.Incomplete) state, proposedUpdate);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final java.lang.Object tryMakeCompletingSlowPath(kotlinx.coroutines.Incomplete state, java.lang.Object proposedUpdate) throws java.lang.Throwable {
        kotlinx.coroutines.NodeList list = getOrPromoteCancellingList(state);
        if (list == null) {
            return kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY;
        }
        kotlinx.coroutines.JobSupport.Finishing finishing = state instanceof kotlinx.coroutines.JobSupport.Finishing ? (kotlinx.coroutines.JobSupport.Finishing) state : null;
        if (finishing == null) {
            finishing = new kotlinx.coroutines.JobSupport.Finishing(list, false, null);
        }
        kotlin.jvm.internal.Ref.ObjectRef notifyRootCause = new kotlin.jvm.internal.Ref.ObjectRef();
        synchronized (finishing) {
            if (finishing.isCompleting()) {
                return kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY;
            }
            finishing.setCompleting(true);
            if (finishing != state && !this._state.compareAndSet(state, finishing)) {
                return kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY;
            }
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!finishing.isSealed())) {
                throw new java.lang.AssertionError();
            }
            boolean wasCancelling = finishing.isCancelling();
            kotlinx.coroutines.CompletedExceptionally it = proposedUpdate instanceof kotlinx.coroutines.CompletedExceptionally ? (kotlinx.coroutines.CompletedExceptionally) proposedUpdate : null;
            if (it != null) {
                finishing.addExceptionLocked(it.cause);
            }
            notifyRootCause.element = java.lang.Boolean.valueOf(wasCancelling ? false : true).booleanValue() ? finishing.getRootCause() : 0;
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
            java.lang.Throwable it2 = (java.lang.Throwable) notifyRootCause.element;
            if (it2 != null) {
                notifyCancelling(list, it2);
            }
            kotlinx.coroutines.ChildHandleNode child = firstChild(state);
            return (child == null || !tryWaitForChild(finishing, child, proposedUpdate)) ? finalizeFinishingState(finishing, proposedUpdate) : kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN;
        }
    }

    private final java.lang.Throwable getExceptionOrNull(java.lang.Object $this$exceptionOrNull) {
        kotlinx.coroutines.CompletedExceptionally completedExceptionally = $this$exceptionOrNull instanceof kotlinx.coroutines.CompletedExceptionally ? (kotlinx.coroutines.CompletedExceptionally) $this$exceptionOrNull : null;
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }

    private final kotlinx.coroutines.ChildHandleNode firstChild(kotlinx.coroutines.Incomplete state) {
        kotlinx.coroutines.ChildHandleNode childHandleNode = state instanceof kotlinx.coroutines.ChildHandleNode ? (kotlinx.coroutines.ChildHandleNode) state : null;
        if (childHandleNode != null) {
            return childHandleNode;
        }
        kotlinx.coroutines.NodeList list = state.getList();
        if (list != null) {
            return nextChild(list);
        }
        return null;
    }

    private final boolean tryWaitForChild(kotlinx.coroutines.JobSupport.Finishing state, kotlinx.coroutines.ChildHandleNode child, java.lang.Object proposedUpdate) {
        while (true) {
            kotlinx.coroutines.ChildJob childJob = child.childJob;
            kotlinx.coroutines.CompletionHandlerBase $this$asHandler$iv = new kotlinx.coroutines.JobSupport.ChildCompletion(this, state, child, proposedUpdate);
            kotlinx.coroutines.DisposableHandle handle = kotlinx.coroutines.Job.DefaultImpls.invokeOnCompletion$default(childJob, false, false, $this$asHandler$iv, 1, null);
            if (handle != kotlinx.coroutines.NonDisposableHandle.INSTANCE) {
                return true;
            }
            kotlinx.coroutines.ChildHandleNode nextChild = nextChild(child);
            if (nextChild == null) {
                return false;
            }
            child = nextChild;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void continueCompleting(kotlinx.coroutines.JobSupport.Finishing state, kotlinx.coroutines.ChildHandleNode lastChild, java.lang.Object proposedUpdate) throws java.lang.Throwable {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() == state)) {
                throw new java.lang.AssertionError();
            }
        }
        kotlinx.coroutines.ChildHandleNode waitChild = nextChild(lastChild);
        if (waitChild == null || !tryWaitForChild(state, waitChild, proposedUpdate)) {
            java.lang.Object finalState = finalizeFinishingState(state, proposedUpdate);
            afterCompletion(finalState);
        }
    }

    private final kotlinx.coroutines.ChildHandleNode nextChild(kotlinx.coroutines.internal.LockFreeLinkedListNode $this$nextChild) {
        kotlinx.coroutines.internal.LockFreeLinkedListNode cur = $this$nextChild;
        while (cur.isRemoved()) {
            cur = cur.getPrevNode();
        }
        while (true) {
            cur = cur.getNextNode();
            if (!cur.isRemoved()) {
                if (cur instanceof kotlinx.coroutines.ChildHandleNode) {
                    return (kotlinx.coroutines.ChildHandleNode) cur;
                }
                if (cur instanceof kotlinx.coroutines.NodeList) {
                    return null;
                }
            }
        }
    }

    @Override // kotlinx.coroutines.Job
    public final kotlin.sequences.Sequence<kotlinx.coroutines.Job> getChildren() {
        return kotlin.sequences.SequencesKt.sequence(new kotlinx.coroutines.JobSupport$children$1(this, null));
    }

    @Override // kotlinx.coroutines.Job
    public final kotlinx.coroutines.ChildHandle attachChild(kotlinx.coroutines.ChildJob child) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(child, "child");
        kotlinx.coroutines.CompletionHandlerBase $this$asHandler$iv = new kotlinx.coroutines.ChildHandleNode(child);
        kotlinx.coroutines.DisposableHandle disposableHandleInvokeOnCompletion$default = kotlinx.coroutines.Job.DefaultImpls.invokeOnCompletion$default(this, true, false, $this$asHandler$iv, 2, null);
        kotlin.jvm.internal.Intrinsics.checkNotNull(disposableHandleInvokeOnCompletion$default, "null cannot be cast to non-null type kotlinx.coroutines.ChildHandle");
        return (kotlinx.coroutines.ChildHandle) disposableHandleInvokeOnCompletion$default;
    }

    public void handleOnCompletionException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Throwable exception) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        throw exception;
    }

    protected void onCancelling(java.lang.Throwable cause) {
    }

    protected boolean isScopedCoroutine() {
        return false;
    }

    /* JADX INFO: renamed from: getHandlesException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host */
    public boolean getHandlesException() {
        return true;
    }

    protected boolean handleJobException(java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        return false;
    }

    protected void onCompletionInternal(java.lang.Object state) {
    }

    protected void afterCompletion(java.lang.Object state) {
    }

    public java.lang.String toString() {
        return toDebugString() + "@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this);
    }

    public final java.lang.String toDebugString() {
        return nameString$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() + "{" + stateString(getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) + "}";
    }

    public java.lang.String nameString$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this);
    }

    private final java.lang.String stateString(java.lang.Object state) {
        return state instanceof kotlinx.coroutines.JobSupport.Finishing ? ((kotlinx.coroutines.JobSupport.Finishing) state).isCancelling() ? "Cancelling" : ((kotlinx.coroutines.JobSupport.Finishing) state).isCompleting() ? "Completing" : "Active" : state instanceof kotlinx.coroutines.Incomplete ? ((kotlinx.coroutines.Incomplete) state).getIsActive() ? "Active" : "New" : state instanceof kotlinx.coroutines.CompletedExceptionally ? "Cancelled" : "Completed";
    }

    /* JADX INFO: compiled from: JobSupport.kt */
    @kotlin.Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u00022\u00020\u0003B\u001f\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\nJ\u000e\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\tJ\u0018\u0010%\u001a\u0012\u0012\u0004\u0012\u00020\t0&j\b\u0012\u0004\u0012\u00020\t`'H\u0002J\u0016\u0010(\u001a\b\u0012\u0004\u0012\u00020\t0)2\b\u0010*\u001a\u0004\u0018\u00010\tJ\b\u0010+\u001a\u00020,H\u0016R\u0016\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\fX\u0082\u0004¢\u0006\u0002\n\u0000R(\u0010\u0011\u001a\u0004\u0018\u00010\u00012\b\u0010\u0010\u001a\u0004\u0018\u00010\u00018B@BX\u0082\u000e¢\u0006\f\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0017R$\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u00078F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u0006\u0010\u0017\"\u0004\b\u0019\u0010\u001aR\u0011\u0010\u001b\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0017R\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR(\u0010\b\u001a\u0004\u0018\u00010\t2\b\u0010\u0010\u001a\u0004\u0018\u00010\t8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!¨\u0006-"}, d2 = {"Lkotlinx/coroutines/JobSupport$Finishing;", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "Lkotlinx/coroutines/Incomplete;", "list", "Lkotlinx/coroutines/NodeList;", "isCompleting", "", "rootCause", "", "(Lkotlinx/coroutines/NodeList;ZLjava/lang/Throwable;)V", "_exceptionsHolder", "Lkotlinx/atomicfu/AtomicRef;", "_isCompleting", "Lkotlinx/atomicfu/AtomicBoolean;", "_rootCause", "value", "exceptionsHolder", "getExceptionsHolder", "()Ljava/lang/Object;", "setExceptionsHolder", "(Ljava/lang/Object;)V", "isActive", "()Z", "isCancelling", "setCompleting", "(Z)V", "isSealed", "getList", "()Lkotlinx/coroutines/NodeList;", "getRootCause", "()Ljava/lang/Throwable;", "setRootCause", "(Ljava/lang/Throwable;)V", "addExceptionLocked", "", "exception", "allocateList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "sealLocked", "", "proposedException", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Finishing implements kotlinx.coroutines.Incomplete {
        private final kotlinx.atomicfu.AtomicRef<java.lang.Object> _exceptionsHolder;
        private final kotlinx.atomicfu.AtomicBoolean _isCompleting;
        private final kotlinx.atomicfu.AtomicRef<java.lang.Throwable> _rootCause;
        private final kotlinx.coroutines.NodeList list;

        @Override // kotlinx.coroutines.Incomplete
        public kotlinx.coroutines.NodeList getList() {
            return this.list;
        }

        public Finishing(kotlinx.coroutines.NodeList list, boolean isCompleting, java.lang.Throwable rootCause) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "list");
            this.list = list;
            this._isCompleting = kotlinx.atomicfu.AtomicFU.atomic(isCompleting);
            this._rootCause = kotlinx.atomicfu.AtomicFU.atomic(rootCause);
            this._exceptionsHolder = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
        }

        public final boolean isCompleting() {
            return this._isCompleting.getValue();
        }

        public final void setCompleting(boolean value) {
            this._isCompleting.setValue(value);
        }

        public final java.lang.Throwable getRootCause() {
            return this._rootCause.getValue();
        }

        public final void setRootCause(java.lang.Throwable value) {
            this._rootCause.setValue(value);
        }

        private final java.lang.Object getExceptionsHolder() {
            return this._exceptionsHolder.getValue();
        }

        private final void setExceptionsHolder(java.lang.Object value) {
            this._exceptionsHolder.setValue(value);
        }

        public final boolean isSealed() {
            return getExceptionsHolder() == kotlinx.coroutines.JobSupportKt.SEALED;
        }

        public final boolean isCancelling() {
            return getRootCause() != null;
        }

        @Override // kotlinx.coroutines.Incomplete
        /* JADX INFO: renamed from: isActive */
        public boolean getIsActive() {
            return getRootCause() == null;
        }

        public final java.util.List<java.lang.Throwable> sealLocked(java.lang.Throwable proposedException) {
            java.util.ArrayList<java.lang.Throwable> arrayListAllocateList;
            java.lang.Object eh = getExceptionsHolder();
            if (eh == null) {
                arrayListAllocateList = allocateList();
            } else if (eh instanceof java.lang.Throwable) {
                arrayListAllocateList = allocateList();
                arrayListAllocateList.add(eh);
            } else {
                if (!(eh instanceof java.util.ArrayList)) {
                    throw new java.lang.IllegalStateException(("State is " + eh).toString());
                }
                arrayListAllocateList = (java.util.ArrayList) eh;
            }
            java.util.ArrayList<java.lang.Throwable> arrayList = arrayListAllocateList;
            java.lang.Throwable rootCause = getRootCause();
            if (rootCause != null) {
                arrayList.add(0, rootCause);
            }
            if (proposedException != null && !kotlin.jvm.internal.Intrinsics.areEqual(proposedException, rootCause)) {
                arrayList.add(proposedException);
            }
            setExceptionsHolder(kotlinx.coroutines.JobSupportKt.SEALED);
            return arrayList;
        }

        public final void addExceptionLocked(java.lang.Throwable exception) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
            java.lang.Throwable rootCause = getRootCause();
            if (rootCause == null) {
                setRootCause(exception);
                return;
            }
            if (exception == rootCause) {
                return;
            }
            java.lang.Object eh = getExceptionsHolder();
            if (eh != null) {
                if (eh instanceof java.lang.Throwable) {
                    if (exception == eh) {
                        return;
                    }
                    java.util.ArrayList<java.lang.Throwable> arrayListAllocateList = allocateList();
                    arrayListAllocateList.add(eh);
                    arrayListAllocateList.add(exception);
                    setExceptionsHolder(arrayListAllocateList);
                    return;
                }
                if (!(eh instanceof java.util.ArrayList)) {
                    throw new java.lang.IllegalStateException(("State is " + eh).toString());
                }
                ((java.util.ArrayList) eh).add(exception);
                return;
            }
            setExceptionsHolder(exception);
        }

        private final java.util.ArrayList<java.lang.Throwable> allocateList() {
            return new java.util.ArrayList<>(4);
        }

        public java.lang.String toString() {
            return "Finishing[cancelling=" + isCancelling() + ", completing=" + isCompleting() + ", rootCause=" + getRootCause() + ", exceptions=" + getExceptionsHolder() + ", list=" + getList() + "]";
        }
    }

    private final boolean isCancelling(kotlinx.coroutines.Incomplete $this$isCancelling) {
        return ($this$isCancelling instanceof kotlinx.coroutines.JobSupport.Finishing) && ((kotlinx.coroutines.JobSupport.Finishing) $this$isCancelling).isCancelling();
    }

    /* JADX INFO: compiled from: JobSupport.kt */
    @kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0002\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\nJ\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/JobSupport$ChildCompletion;", "Lkotlinx/coroutines/JobNode;", "parent", "Lkotlinx/coroutines/JobSupport;", "state", "Lkotlinx/coroutines/JobSupport$Finishing;", "child", "Lkotlinx/coroutines/ChildHandleNode;", "proposedUpdate", "", "(Lkotlinx/coroutines/JobSupport;Lkotlinx/coroutines/JobSupport$Finishing;Lkotlinx/coroutines/ChildHandleNode;Ljava/lang/Object;)V", "invoke", "", "cause", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class ChildCompletion extends kotlinx.coroutines.JobNode {
        private final kotlinx.coroutines.ChildHandleNode child;
        private final kotlinx.coroutines.JobSupport parent;
        private final java.lang.Object proposedUpdate;
        private final kotlinx.coroutines.JobSupport.Finishing state;

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) throws java.lang.Throwable {
            invoke2(th);
            return kotlin.Unit.INSTANCE;
        }

        public ChildCompletion(kotlinx.coroutines.JobSupport parent, kotlinx.coroutines.JobSupport.Finishing state, kotlinx.coroutines.ChildHandleNode child, java.lang.Object proposedUpdate) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parent, "parent");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(state, "state");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(child, "child");
            this.parent = parent;
            this.state = state;
            this.child = child;
            this.proposedUpdate = proposedUpdate;
        }

        @Override // kotlinx.coroutines.CompletionHandlerBase
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public void invoke2(java.lang.Throwable cause) throws java.lang.Throwable {
            this.parent.continueCompleting(this.state, this.child, this.proposedUpdate);
        }
    }

    /* JADX INFO: compiled from: JobSupport.kt */
    @kotlin.Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/JobSupport$AwaitContinuation;", "T", "Lkotlinx/coroutines/CancellableContinuationImpl;", "delegate", "Lkotlin/coroutines/Continuation;", com.android.server.am.HostingRecord.TRIGGER_TYPE_JOB, "Lkotlinx/coroutines/JobSupport;", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/JobSupport;)V", "getContinuationCancellationCause", "", "parent", "Lkotlinx/coroutines/Job;", "nameString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class AwaitContinuation<T> extends kotlinx.coroutines.CancellableContinuationImpl<T> {
        private final kotlinx.coroutines.JobSupport job;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AwaitContinuation(kotlin.coroutines.Continuation<? super T> delegate, kotlinx.coroutines.JobSupport job) {
            super(delegate, 1);
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delegate, "delegate");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(job, "job");
            this.job = job;
        }

        @Override // kotlinx.coroutines.CancellableContinuationImpl
        public java.lang.Throwable getContinuationCancellationCause(kotlinx.coroutines.Job parent) {
            java.lang.Throwable it;
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parent, "parent");
            java.lang.Object state = this.job.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            if (!(state instanceof kotlinx.coroutines.JobSupport.Finishing) || (it = ((kotlinx.coroutines.JobSupport.Finishing) state).getRootCause()) == null) {
                return state instanceof kotlinx.coroutines.CompletedExceptionally ? ((kotlinx.coroutines.CompletedExceptionally) state).cause : parent.getCancellationException();
            }
            return it;
        }

        @Override // kotlinx.coroutines.CancellableContinuationImpl
        protected java.lang.String nameString() {
            return "AwaitContinuation";
        }
    }

    public final boolean isCompletedExceptionally() {
        return getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() instanceof kotlinx.coroutines.CompletedExceptionally;
    }

    public final java.lang.Throwable getCompletionExceptionOrNull() {
        java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (!(state instanceof kotlinx.coroutines.Incomplete)) {
            return getExceptionOrNull(state);
        }
        throw new java.lang.IllegalStateException("This job has not completed yet".toString());
    }

    public final java.lang.Object getCompletedInternal$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() throws java.lang.Throwable {
        java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (!(!(state instanceof kotlinx.coroutines.Incomplete))) {
            throw new java.lang.IllegalStateException("This job has not completed yet".toString());
        }
        if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
            throw ((kotlinx.coroutines.CompletedExceptionally) state).cause;
        }
        return kotlinx.coroutines.JobSupportKt.unboxState(state);
    }

    protected final java.lang.Object awaitInternal(kotlin.coroutines.Continuation<java.lang.Object> continuation) throws java.lang.Throwable {
        java.lang.Object state;
        do {
            state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            if (!(state instanceof kotlinx.coroutines.Incomplete)) {
                if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
                    java.lang.Throwable exception$iv = ((kotlinx.coroutines.CompletedExceptionally) state).cause;
                    if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                        throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(exception$iv, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation);
                    }
                    throw exception$iv;
                }
                return kotlinx.coroutines.JobSupportKt.unboxState(state);
            }
        } while (startInternal(state) < 0);
        return awaitSuspend(continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object awaitSuspend(kotlin.coroutines.Continuation<java.lang.Object> continuation) {
        kotlinx.coroutines.JobSupport.AwaitContinuation cont = new kotlinx.coroutines.JobSupport.AwaitContinuation(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), this);
        cont.initCancellability();
        kotlinx.coroutines.CompletionHandlerBase $this$asHandler$iv = new kotlinx.coroutines.ResumeAwaitOnCompletion(cont);
        kotlinx.coroutines.CancellableContinuationKt.disposeOnCancellation(cont, invokeOnCompletion($this$asHandler$iv));
        java.lang.Object result = cont.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    protected final kotlinx.coroutines.selects.SelectClause1<?> getOnAwaitInternal() {
        kotlinx.coroutines.JobSupport$onAwaitInternal$1 jobSupport$onAwaitInternal$1 = kotlinx.coroutines.JobSupport$onAwaitInternal$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(jobSupport$onAwaitInternal$1, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'select')] kotlinx.coroutines.selects.SelectInstance<*>, @[ParameterName(name = 'param')] kotlin.Any?, kotlin.Unit>{ kotlinx.coroutines.selects.SelectKt.RegistrationFunction }");
        kotlin.jvm.functions.Function3 function3 = (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(jobSupport$onAwaitInternal$1, 3);
        kotlinx.coroutines.JobSupport$onAwaitInternal$2 jobSupport$onAwaitInternal$2 = kotlinx.coroutines.JobSupport$onAwaitInternal$2.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(jobSupport$onAwaitInternal$2, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'param')] kotlin.Any?, @[ParameterName(name = 'clauseResult')] kotlin.Any?, kotlin.Any?>{ kotlinx.coroutines.selects.SelectKt.ProcessResultFunction }");
        return new kotlinx.coroutines.selects.SelectClause1Impl(this, function3, (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(jobSupport$onAwaitInternal$2, 3), null, 8, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onAwaitInternalRegFunc(kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object ignoredParam) {
        java.lang.Object state;
        do {
            state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            if (!(state instanceof kotlinx.coroutines.Incomplete)) {
                java.lang.Object result = state instanceof kotlinx.coroutines.CompletedExceptionally ? state : kotlinx.coroutines.JobSupportKt.unboxState(state);
                select.selectInRegistrationPhase(result);
                return;
            }
        } while (startInternal(state) < 0);
        kotlinx.coroutines.CompletionHandlerBase $this$asHandler$iv = new kotlinx.coroutines.JobSupport.SelectOnAwaitCompletionHandler(this, select);
        kotlinx.coroutines.DisposableHandle disposableHandle = invokeOnCompletion($this$asHandler$iv);
        select.disposeOnCompletion(disposableHandle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object onAwaitInternalProcessResFunc(java.lang.Object ignoredParam, java.lang.Object result) throws java.lang.Throwable {
        if (result instanceof kotlinx.coroutines.CompletedExceptionally) {
            throw ((kotlinx.coroutines.CompletedExceptionally) result).cause;
        }
        return result;
    }

    /* JADX INFO: compiled from: JobSupport.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0096\u0002R\u0012\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/JobSupport$SelectOnAwaitCompletionHandler;", "Lkotlinx/coroutines/JobNode;", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Lkotlinx/coroutines/JobSupport;Lkotlinx/coroutines/selects/SelectInstance;)V", "invoke", "", "cause", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class SelectOnAwaitCompletionHandler extends kotlinx.coroutines.JobNode {
        private final kotlinx.coroutines.selects.SelectInstance<?> select;
        final /* synthetic */ kotlinx.coroutines.JobSupport this$0;

        public SelectOnAwaitCompletionHandler(kotlinx.coroutines.JobSupport this$0, kotlinx.coroutines.selects.SelectInstance<?> select) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
            this.this$0 = this$0;
            this.select = select;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
            invoke2(th);
            return kotlin.Unit.INSTANCE;
        }

        @Override // kotlinx.coroutines.CompletionHandlerBase
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public void invoke2(java.lang.Throwable cause) {
            java.lang.Object state = this.this$0.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            java.lang.Object result = state instanceof kotlinx.coroutines.CompletedExceptionally ? state : kotlinx.coroutines.JobSupportKt.unboxState(state);
            this.select.trySelect(this.this$0, result);
        }
    }
}
