package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: LockFreeLinkedList.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0001\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J0\u0010\u0007\u001a\u00020\b\"\u000e\b\u0000\u0010\t\u0018\u0001*\u00060\u0001j\u0002`\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u00020\b0\fH\u0086\bø\u0001\u0000J\u0010\u0010\r\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\nH\u0014J\u0006\u0010\u000e\u001a\u00020\u000fJ\r\u0010\u0010\u001a\u00020\bH\u0000¢\u0006\u0002\b\u0011R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0005\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "()V", "isEmpty", "", "()Z", "isRemoved", "forEach", "", "T", "Lkotlinx/coroutines/internal/Node;", "block", "Lkotlin/Function1;", "nextIfRemoved", "remove", "", "validate", "validate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class LockFreeLinkedListHead extends kotlinx.coroutines.internal.LockFreeLinkedListNode {
    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    /* JADX INFO: renamed from: remove, reason: collision with other method in class */
    public /* bridge */ /* synthetic */ boolean mo12863remove() {
        return ((java.lang.Boolean) remove()).booleanValue();
    }

    public final boolean isEmpty() {
        return getNext() == this;
    }

    public final /* synthetic */ <T extends kotlinx.coroutines.internal.LockFreeLinkedListNode> void forEach(kotlin.jvm.functions.Function1<? super T, kotlin.Unit> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        java.lang.Object next = getNext();
        kotlin.jvm.internal.Intrinsics.checkNotNull(next, "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }");
        for (kotlinx.coroutines.internal.LockFreeLinkedListNode cur = (kotlinx.coroutines.internal.LockFreeLinkedListNode) next; !kotlin.jvm.internal.Intrinsics.areEqual(cur, this); cur = cur.getNextNode()) {
            kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(3, "T");
            if (cur instanceof kotlinx.coroutines.internal.LockFreeLinkedListNode) {
                block.invoke(cur);
            }
        }
    }

    public final java.lang.Void remove() {
        throw new java.lang.IllegalStateException("head cannot be removed".toString());
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public boolean isRemoved() {
        return false;
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    protected kotlinx.coroutines.internal.LockFreeLinkedListNode nextIfRemoved() {
        return null;
    }

    public final void validate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        kotlinx.coroutines.internal.LockFreeLinkedListHead prev = this;
        java.lang.Object next = getNext();
        kotlin.jvm.internal.Intrinsics.checkNotNull(next, "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }");
        kotlinx.coroutines.internal.LockFreeLinkedListNode cur = (kotlinx.coroutines.internal.LockFreeLinkedListNode) next;
        while (!kotlin.jvm.internal.Intrinsics.areEqual(cur, this)) {
            kotlinx.coroutines.internal.LockFreeLinkedListNode next2 = cur.getNextNode();
            cur.validateNode$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(prev, next2);
            prev = cur;
            cur = next2;
        }
        java.lang.Object next3 = getNext();
        kotlin.jvm.internal.Intrinsics.checkNotNull(next3, "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }");
        validateNode$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(prev, (kotlinx.coroutines.internal.LockFreeLinkedListNode) next3);
    }
}
