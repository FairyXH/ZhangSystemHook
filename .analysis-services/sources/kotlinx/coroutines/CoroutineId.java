package kotlinx.coroutines;

/* JADX INFO: compiled from: CoroutineContext.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0081\b\u0018\u0000 \u00182\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001\u0018B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\t\u001a\u00020\u0005HÆ\u0003J\u0013\u0010\n\u001a\u00020\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eHÖ\u0003J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0002H\u0016J\b\u0010\u0016\u001a\u00020\u0002H\u0016J\u0010\u0010\u0017\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u0019"}, d2 = {"Lkotlinx/coroutines/CoroutineId;", "Lkotlinx/coroutines/ThreadContextElement;", "", "Lkotlin/coroutines/AbstractCoroutineContextElement;", "id", "", "(J)V", "getId", "()J", "component1", "copy", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "restoreThreadContext", "", "context", "Lkotlin/coroutines/CoroutineContext;", "oldState", "toString", "updateThreadContext", "Key", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class CoroutineId extends kotlin.coroutines.AbstractCoroutineContextElement implements kotlinx.coroutines.ThreadContextElement<java.lang.String> {

    /* JADX INFO: renamed from: Key, reason: from kotlin metadata */
    public static final kotlinx.coroutines.CoroutineId.Companion INSTANCE = new kotlinx.coroutines.CoroutineId.Companion(null);
    private final long id;

    public static /* synthetic */ kotlinx.coroutines.CoroutineId copy$default(kotlinx.coroutines.CoroutineId coroutineId, long j, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            j = coroutineId.id;
        }
        return coroutineId.copy(j);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final long getId() {
        return this.id;
    }

    public final kotlinx.coroutines.CoroutineId copy(long id) {
        return new kotlinx.coroutines.CoroutineId(id);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof kotlinx.coroutines.CoroutineId) && this.id == ((kotlinx.coroutines.CoroutineId) other).id;
    }

    public int hashCode() {
        return java.lang.Long.hashCode(this.id);
    }

    public final long getId() {
        return this.id;
    }

    public CoroutineId(long id) {
        super(INSTANCE);
        this.id = id;
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.CoroutineId$Key, reason: from kotlin metadata */
    /* JADX INFO: compiled from: CoroutineContext.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Lkotlinx/coroutines/CoroutineId$Key;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlinx/coroutines/CoroutineId;", "()V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion implements kotlin.coroutines.CoroutineContext.Key<kotlinx.coroutines.CoroutineId> {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public java.lang.String toString() {
        return "CoroutineId(" + this.id + ")";
    }

    @Override // kotlinx.coroutines.ThreadContextElement
    public java.lang.String updateThreadContext(kotlin.coroutines.CoroutineContext context) {
        java.lang.String coroutineName;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlinx.coroutines.CoroutineName coroutineName2 = (kotlinx.coroutines.CoroutineName) context.get(kotlinx.coroutines.CoroutineName.INSTANCE);
        if (coroutineName2 == null || (coroutineName = coroutineName2.getName()) == null) {
            coroutineName = "coroutine";
        }
        java.lang.Thread currentThread = java.lang.Thread.currentThread();
        java.lang.String oldName = currentThread.getName();
        kotlin.jvm.internal.Intrinsics.checkNotNull(oldName);
        int lastIndex = kotlin.text.StringsKt.lastIndexOf$default((java.lang.CharSequence) oldName, " @", 0, false, 6, (java.lang.Object) null);
        if (lastIndex < 0) {
            lastIndex = oldName.length();
        }
        java.lang.StringBuilder $this$updateThreadContext_u24lambda_u240 = new java.lang.StringBuilder(coroutineName.length() + lastIndex + 10);
        java.lang.String strSubstring = oldName.substring(0, lastIndex);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        $this$updateThreadContext_u24lambda_u240.append(strSubstring);
        $this$updateThreadContext_u24lambda_u240.append(" @");
        $this$updateThreadContext_u24lambda_u240.append(coroutineName);
        $this$updateThreadContext_u24lambda_u240.append('#');
        $this$updateThreadContext_u24lambda_u240.append(this.id);
        java.lang.String string = $this$updateThreadContext_u24lambda_u240.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        currentThread.setName(string);
        return oldName;
    }

    @Override // kotlinx.coroutines.ThreadContextElement
    public void restoreThreadContext(kotlin.coroutines.CoroutineContext context, java.lang.String oldState) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(oldState, "oldState");
        java.lang.Thread.currentThread().setName(oldState);
    }
}
