package kotlinx.coroutines;

/* JADX INFO: compiled from: CancellableContinuationImpl.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001BZ\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012%\b\u0002\u0010\u0005\u001a\u001f\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0006\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\u000eJ\u000b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\u000b\u0010\u0014\u001a\u0004\u0018\u00010\u0004HÆ\u0003J&\u0010\u0015\u001a\u001f\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0006HÆ\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0007HÆ\u0003J`\u0010\u0018\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u00042%\b\u0002\u0010\u0005\u001a\u001f\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u00062\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0007HÆ\u0001J\u0013\u0010\u0019\u001a\u00020\u00102\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u001cHÖ\u0001J\u001a\u0010\u001d\u001a\u00020\u000b2\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u001f2\u0006\u0010\n\u001a\u00020\u0007J\t\u0010 \u001a\u00020!HÖ\u0001R\u0012\u0010\r\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u00108F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0012\u0010\f\u001a\u0004\u0018\u00010\u00018\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R-\u0010\u0005\u001a\u001f\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00018\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lkotlinx/coroutines/CompletedContinuation;", "", "result", "cancelHandler", "Lkotlinx/coroutines/CancelHandler;", "onCancellation", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "", "idempotentResume", "cancelCause", "(Ljava/lang/Object;Lkotlinx/coroutines/CancelHandler;Lkotlin/jvm/functions/Function1;Ljava/lang/Object;Ljava/lang/Throwable;)V", "cancelled", "", "getCancelled", "()Z", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "invokeHandlers", "cont", "Lkotlinx/coroutines/CancellableContinuationImpl;", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final /* data */ class CompletedContinuation {
    public final java.lang.Throwable cancelCause;
    public final kotlinx.coroutines.CancelHandler cancelHandler;
    public final java.lang.Object idempotentResume;
    public final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> onCancellation;
    public final java.lang.Object result;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ kotlinx.coroutines.CompletedContinuation copy$default(kotlinx.coroutines.CompletedContinuation completedContinuation, java.lang.Object obj, kotlinx.coroutines.CancelHandler cancelHandler, kotlin.jvm.functions.Function1 function1, java.lang.Object obj2, java.lang.Throwable th, int i, java.lang.Object obj3) {
        if ((i & 1) != 0) {
            obj = completedContinuation.result;
        }
        if ((i & 2) != 0) {
            cancelHandler = completedContinuation.cancelHandler;
        }
        kotlinx.coroutines.CancelHandler cancelHandler2 = cancelHandler;
        if ((i & 4) != 0) {
            function1 = completedContinuation.onCancellation;
        }
        kotlin.jvm.functions.Function1 function12 = function1;
        if ((i & 8) != 0) {
            obj2 = completedContinuation.idempotentResume;
        }
        java.lang.Object obj4 = obj2;
        if ((i & 16) != 0) {
            th = completedContinuation.cancelCause;
        }
        return completedContinuation.copy(obj, cancelHandler2, function12, obj4, th);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.Object getResult() {
        return this.result;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final kotlinx.coroutines.CancelHandler getCancelHandler() {
        return this.cancelHandler;
    }

    public final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> component3() {
        return this.onCancellation;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final java.lang.Object getIdempotentResume() {
        return this.idempotentResume;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final java.lang.Throwable getCancelCause() {
        return this.cancelCause;
    }

    public final kotlinx.coroutines.CompletedContinuation copy(java.lang.Object result, kotlinx.coroutines.CancelHandler cancelHandler, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation, java.lang.Object idempotentResume, java.lang.Throwable cancelCause) {
        return new kotlinx.coroutines.CompletedContinuation(result, cancelHandler, onCancellation, idempotentResume, cancelCause);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof kotlinx.coroutines.CompletedContinuation)) {
            return false;
        }
        kotlinx.coroutines.CompletedContinuation completedContinuation = (kotlinx.coroutines.CompletedContinuation) other;
        return kotlin.jvm.internal.Intrinsics.areEqual(this.result, completedContinuation.result) && kotlin.jvm.internal.Intrinsics.areEqual(this.cancelHandler, completedContinuation.cancelHandler) && kotlin.jvm.internal.Intrinsics.areEqual(this.onCancellation, completedContinuation.onCancellation) && kotlin.jvm.internal.Intrinsics.areEqual(this.idempotentResume, completedContinuation.idempotentResume) && kotlin.jvm.internal.Intrinsics.areEqual(this.cancelCause, completedContinuation.cancelCause);
    }

    public int hashCode() {
        return ((((((((this.result == null ? 0 : this.result.hashCode()) * 31) + (this.cancelHandler == null ? 0 : this.cancelHandler.hashCode())) * 31) + (this.onCancellation == null ? 0 : this.onCancellation.hashCode())) * 31) + (this.idempotentResume == null ? 0 : this.idempotentResume.hashCode())) * 31) + (this.cancelCause != null ? this.cancelCause.hashCode() : 0);
    }

    public java.lang.String toString() {
        return "CompletedContinuation(result=" + this.result + ", cancelHandler=" + this.cancelHandler + ", onCancellation=" + this.onCancellation + ", idempotentResume=" + this.idempotentResume + ", cancelCause=" + this.cancelCause + ")";
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CompletedContinuation(java.lang.Object result, kotlinx.coroutines.CancelHandler cancelHandler, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> function1, java.lang.Object idempotentResume, java.lang.Throwable cancelCause) {
        this.result = result;
        this.cancelHandler = cancelHandler;
        this.onCancellation = function1;
        this.idempotentResume = idempotentResume;
        this.cancelCause = cancelCause;
    }

    public /* synthetic */ CompletedContinuation(java.lang.Object obj, kotlinx.coroutines.CancelHandler cancelHandler, kotlin.jvm.functions.Function1 function1, java.lang.Object obj2, java.lang.Throwable th, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(obj, (i & 2) != 0 ? null : cancelHandler, (i & 4) != 0 ? null : function1, (i & 8) != 0 ? null : obj2, (i & 16) != 0 ? null : th);
    }

    public final boolean getCancelled() {
        return this.cancelCause != null;
    }

    public final void invokeHandlers(kotlinx.coroutines.CancellableContinuationImpl<?> cont, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cont, "cont");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        kotlinx.coroutines.CancelHandler it = this.cancelHandler;
        if (it != null) {
            cont.callCancelHandler(it, cause);
        }
        kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> function1 = this.onCancellation;
        if (function1 != null) {
            cont.callOnCancellation(function1, cause);
        }
    }
}
