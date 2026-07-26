package kotlinx.coroutines;

/* JADX INFO: compiled from: AbstractCoroutine.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u00022\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00010\u00042\u00020\u0005B\u001d\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t¢\u0006\u0002\u0010\u000bJ\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\b\u0010\u0019\u001a\u00020\u001aH\u0014J\u0015\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001dH\u0000¢\u0006\u0002\b\u001eJ\r\u0010\u001f\u001a\u00020\u001aH\u0010¢\u0006\u0002\b J\u0018\u0010!\u001a\u00020\u00162\u0006\u0010\"\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020\tH\u0014J\u0015\u0010$\u001a\u00020\u00162\u0006\u0010%\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010&J\u0012\u0010'\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0004J\u0019\u0010(\u001a\u00020\u00162\f\u0010)\u001a\b\u0012\u0004\u0012\u00028\u00000*¢\u0006\u0002\u0010&JJ\u0010+\u001a\u00020\u0016\"\u0004\b\u0001\u0010,2\u0006\u0010+\u001a\u00020-2\u0006\u0010.\u001a\u0002H,2'\u0010/\u001a#\b\u0001\u0012\u0004\u0012\u0002H,\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u001800¢\u0006\u0002\b1¢\u0006\u0002\u00102R\u0017\u0010\f\u001a\u00020\u0007¢\u0006\u000e\n\u0000\u0012\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u0014\u0010\u0013\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014¨\u00063"}, d2 = {"Lkotlinx/coroutines/AbstractCoroutine;", "T", "Lkotlinx/coroutines/JobSupport;", "Lkotlinx/coroutines/Job;", "Lkotlin/coroutines/Continuation;", "Lkotlinx/coroutines/CoroutineScope;", "parentContext", "Lkotlin/coroutines/CoroutineContext;", "initParentJob", "", com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE, "(Lkotlin/coroutines/CoroutineContext;ZZ)V", "context", "getContext$annotations", "()V", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "coroutineContext", "getCoroutineContext", "isActive", "()Z", "afterResume", "", "state", "", "cancellationExceptionMessage", "", "handleOnCompletionException", "exception", "", "handleOnCompletionException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "nameString", "nameString$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "onCancelled", "cause", "handled", "onCompleted", "value", "(Ljava/lang/Object;)V", "onCompletionInternal", "resumeWith", "result", "Lkotlin/Result;", "start", "R", "Lkotlinx/coroutines/CoroutineStart;", "receiver", "block", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/CoroutineStart;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractCoroutine<T> extends kotlinx.coroutines.JobSupport implements kotlinx.coroutines.Job, kotlin.coroutines.Continuation<T>, kotlinx.coroutines.CoroutineScope {
    private final kotlin.coroutines.CoroutineContext context;

    public static /* synthetic */ void getContext$annotations() {
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AbstractCoroutine(kotlin.coroutines.CoroutineContext parentContext, boolean initParentJob, boolean active) {
        super(active);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parentContext, "parentContext");
        if (initParentJob) {
            initParentJob((kotlinx.coroutines.Job) parentContext.get(kotlinx.coroutines.Job.INSTANCE));
        }
        this.context = parentContext.plus(this);
    }

    @Override // kotlin.coroutines.Continuation
    /* JADX INFO: renamed from: getContext, reason: from getter */
    public final kotlin.coroutines.CoroutineContext get$context() {
        return this.context;
    }

    @Override // kotlinx.coroutines.CoroutineScope
    public kotlin.coroutines.CoroutineContext getCoroutineContext() {
        return this.context;
    }

    @Override // kotlinx.coroutines.JobSupport, kotlinx.coroutines.Job
    public boolean isActive() {
        return super.isActive();
    }

    protected void onCompleted(T value) {
    }

    protected void onCancelled(java.lang.Throwable cause, boolean handled) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.JobSupport
    public java.lang.String cancellationExceptionMessage() {
        return kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this) + " was cancelled";
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.JobSupport
    protected final void onCompletionInternal(java.lang.Object state) {
        if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
            onCancelled(((kotlinx.coroutines.CompletedExceptionally) state).cause, ((kotlinx.coroutines.CompletedExceptionally) state).getHandled());
        } else {
            onCompleted(state);
        }
    }

    @Override // kotlin.coroutines.Continuation
    public final void resumeWith(java.lang.Object result) {
        java.lang.Object state = makeCompletingOnce$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.CompletionStateKt.toState$default(result, null, 1, null));
        if (state == kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            return;
        }
        afterResume(state);
    }

    protected void afterResume(java.lang.Object state) {
        afterCompletion(state);
    }

    @Override // kotlinx.coroutines.JobSupport
    public final void handleOnCompletionException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(this.context, exception);
    }

    @Override // kotlinx.coroutines.JobSupport
    public java.lang.String nameString$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        java.lang.String coroutineName = kotlinx.coroutines.CoroutineContextKt.getCoroutineName(this.context);
        if (coroutineName == null) {
            return super.nameString$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        }
        return "\"" + coroutineName + "\":" + super.nameString$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
    }

    public final <R> void start(kotlinx.coroutines.CoroutineStart start, R receiver, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(start, "start");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        start.invoke(block, receiver, this);
    }
}
