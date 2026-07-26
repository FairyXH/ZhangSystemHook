package kotlinx.coroutines;

/* JADX INFO: compiled from: CompletionState.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B2\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001\u0012!\u0010\u0003\u001a\u001d\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0004\u0012\u00020\t0\u0004¢\u0006\u0002\u0010\nJ\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0001HÆ\u0003J$\u0010\f\u001a\u001d\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0004\u0012\u00020\t0\u0004HÆ\u0003J:\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00012#\b\u0002\u0010\u0003\u001a\u001d\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0004\u0012\u00020\t0\u0004HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R+\u0010\u0003\u001a\u001d\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0004\u0012\u00020\t0\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00018\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/CompletedWithCancellation;", "", "result", "onCancellation", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "component1", "component2", "copy", "equals", "", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class CompletedWithCancellation {
    public final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> onCancellation;
    public final java.lang.Object result;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ kotlinx.coroutines.CompletedWithCancellation copy$default(kotlinx.coroutines.CompletedWithCancellation completedWithCancellation, java.lang.Object obj, kotlin.jvm.functions.Function1 function1, int i, java.lang.Object obj2) {
        if ((i & 1) != 0) {
            obj = completedWithCancellation.result;
        }
        if ((i & 2) != 0) {
            function1 = completedWithCancellation.onCancellation;
        }
        return completedWithCancellation.copy(obj, function1);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.Object getResult() {
        return this.result;
    }

    public final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> component2() {
        return this.onCancellation;
    }

    public final kotlinx.coroutines.CompletedWithCancellation copy(java.lang.Object result, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onCancellation, "onCancellation");
        return new kotlinx.coroutines.CompletedWithCancellation(result, onCancellation);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof kotlinx.coroutines.CompletedWithCancellation)) {
            return false;
        }
        kotlinx.coroutines.CompletedWithCancellation completedWithCancellation = (kotlinx.coroutines.CompletedWithCancellation) other;
        return kotlin.jvm.internal.Intrinsics.areEqual(this.result, completedWithCancellation.result) && kotlin.jvm.internal.Intrinsics.areEqual(this.onCancellation, completedWithCancellation.onCancellation);
    }

    public int hashCode() {
        return ((this.result == null ? 0 : this.result.hashCode()) * 31) + this.onCancellation.hashCode();
    }

    public java.lang.String toString() {
        return "CompletedWithCancellation(result=" + this.result + ", onCancellation=" + this.onCancellation + ")";
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CompletedWithCancellation(java.lang.Object result, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onCancellation, "onCancellation");
        this.result = result;
        this.onCancellation = onCancellation;
    }
}
