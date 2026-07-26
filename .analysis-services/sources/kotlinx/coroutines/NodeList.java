package kotlinx.coroutines;

/* JADX INFO: compiled from: JobSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bJ\b\u0010\r\u001a\u00020\u000bH\u0016R\u0014\u0010\u0004\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u00008VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/NodeList;", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "Lkotlinx/coroutines/Incomplete;", "()V", "isActive", "", "()Z", "list", "getList", "()Lkotlinx/coroutines/NodeList;", "getString", "", "state", "toString", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class NodeList extends kotlinx.coroutines.internal.LockFreeLinkedListHead implements kotlinx.coroutines.Incomplete {
    @Override // kotlinx.coroutines.Incomplete
    public boolean isActive() {
        return true;
    }

    @Override // kotlinx.coroutines.Incomplete
    public kotlinx.coroutines.NodeList getList() {
        return this;
    }

    public final java.lang.String getString(java.lang.String state) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(state, "state");
        java.lang.StringBuilder $this$getString_u24lambda_u241 = new java.lang.StringBuilder();
        $this$getString_u24lambda_u241.append("List{");
        $this$getString_u24lambda_u241.append(state);
        $this$getString_u24lambda_u241.append("}[");
        boolean first = true;
        kotlinx.coroutines.NodeList this_$iv = this;
        java.lang.Object next = this_$iv.getNext();
        kotlin.jvm.internal.Intrinsics.checkNotNull(next, "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }");
        for (kotlinx.coroutines.internal.LockFreeLinkedListNode cur$iv = (kotlinx.coroutines.internal.LockFreeLinkedListNode) next; !kotlin.jvm.internal.Intrinsics.areEqual(cur$iv, this_$iv); cur$iv = cur$iv.getNextNode()) {
            if (cur$iv instanceof kotlinx.coroutines.JobNode) {
                kotlinx.coroutines.JobNode node = (kotlinx.coroutines.JobNode) cur$iv;
                if (first) {
                    first = false;
                } else {
                    $this$getString_u24lambda_u241.append(", ");
                }
                $this$getString_u24lambda_u241.append(node);
            }
        }
        $this$getString_u24lambda_u241.append("]");
        java.lang.String string = $this$getString_u24lambda_u241.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public java.lang.String toString() {
        return kotlinx.coroutines.DebugKt.getDEBUG() ? getString("Active") : super.toString();
    }
}
