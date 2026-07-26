package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: LockFreeLinkedList.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\b\u0017\u0018\u00002\u00020\u0001:\u00010B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0014\u001a\u00020\u00152\n\u0010\u0016\u001a\u00060\u0000j\u0002`\u000fJ(\u0010\u0017\u001a\u00020\t2\n\u0010\u0016\u001a\u00060\u0000j\u0002`\u000f2\u000e\b\u0004\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\t0\u0019H\u0086\bø\u0001\u0000J \u0010\u001a\u001a\u00020\t2\n\u0010\u0016\u001a\u00060\u0000j\u0002`\u000f2\n\u0010\u000b\u001a\u00060\u0000j\u0002`\u000fH\u0001J\u0012\u0010\u001b\u001a\u00020\t2\n\u0010\u0016\u001a\u00060\u0000j\u0002`\u000fJ\u001b\u0010\u001c\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u000f2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0082\u0010J\u0019\u0010\u001f\u001a\u00060\u0000j\u0002`\u000f2\n\u0010 \u001a\u00060\u0000j\u0002`\u000fH\u0082\u0010J\u0014\u0010!\u001a\u00020\u00152\n\u0010\u000b\u001a\u00060\u0000j\u0002`\u000fH\u0002J(\u0010\"\u001a\u00020#2\n\u0010\u0016\u001a\u00060\u0000j\u0002`\u000f2\u000e\b\u0004\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\t0\u0019H\u0081\bø\u0001\u0000J\u0010\u0010$\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u000fH\u0014J\b\u0010%\u001a\u00020\tH\u0016J\u0010\u0010&\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u000fH\u0001J\b\u0010'\u001a\u00020\u0007H\u0002J\b\u0010(\u001a\u00020)H\u0016J(\u0010*\u001a\u00020+2\n\u0010\u0016\u001a\u00060\u0000j\u0002`\u000f2\n\u0010\u000b\u001a\u00060\u0000j\u0002`\u000f2\u0006\u0010,\u001a\u00020#H\u0001J%\u0010-\u001a\u00020\u00152\n\u0010.\u001a\u00060\u0000j\u0002`\u000f2\n\u0010\u000b\u001a\u00060\u0000j\u0002`\u000fH\u0000¢\u0006\u0002\b/R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\nR\u0011\u0010\u000b\u001a\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0015\u0010\u000e\u001a\u00060\u0000j\u0002`\u000f8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0015\u0010\u0012\u001a\u00060\u0000j\u0002`\u000f8F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0011\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u00061"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "", "()V", "_next", "Lkotlinx/atomicfu/AtomicRef;", "_prev", "_removedRef", "Lkotlinx/coroutines/internal/Removed;", "isRemoved", "", "()Z", "next", "getNext", "()Ljava/lang/Object;", "nextNode", "Lkotlinx/coroutines/internal/Node;", "getNextNode", "()Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "prevNode", "getPrevNode", "addLast", "", "node", "addLastIf", "condition", "Lkotlin/Function0;", "addNext", "addOneIfEmpty", "correctPrev", "op", "Lkotlinx/coroutines/internal/OpDescriptor;", "findPrevNonRemoved", "current", "finishAdd", "makeCondAddOp", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$CondAddOp;", "nextIfRemoved", "remove", "removeOrNext", "removed", "toString", "", "tryCondAddNext", "", "condAdd", "validateNode", "prev", "validateNode$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "CondAddOp", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class LockFreeLinkedListNode {
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> _next = kotlinx.atomicfu.AtomicFU.atomic(this);
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeLinkedListNode> _prev = kotlinx.atomicfu.AtomicFU.atomic(this);
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.Removed> _removedRef = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);

    private final kotlinx.coroutines.internal.Removed removed() {
        kotlinx.coroutines.internal.Removed value = this._removedRef.getValue();
        if (value != null) {
            return value;
        }
        kotlinx.coroutines.internal.Removed it = new kotlinx.coroutines.internal.Removed(this);
        this._removedRef.lazySet(it);
        return it;
    }

    /* JADX INFO: compiled from: LockFreeLinkedList.kt */
    @kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\b!\u0018\u00002\f\u0012\b\u0012\u00060\u0002j\u0002`\u00030\u0001B\u0011\u0012\n\u0010\u0004\u001a\u00060\u0002j\u0002`\u0003¢\u0006\u0002\u0010\u0005J\u001e\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u00060\u0002j\u0002`\u00032\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016R\u0014\u0010\u0004\u001a\u00060\u0002j\u0002`\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\n\u0018\u00010\u0002j\u0004\u0018\u0001`\u00038\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeLinkedListNode$CondAddOp;", "Lkotlinx/coroutines/internal/AtomicOp;", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/internal/Node;", "newNode", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)V", "oldNext", "complete", "", "affected", "failure", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static abstract class CondAddOp extends kotlinx.coroutines.internal.AtomicOp<kotlinx.coroutines.internal.LockFreeLinkedListNode> {
        public final kotlinx.coroutines.internal.LockFreeLinkedListNode newNode;
        public kotlinx.coroutines.internal.LockFreeLinkedListNode oldNext;

        public CondAddOp(kotlinx.coroutines.internal.LockFreeLinkedListNode newNode) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(newNode, "newNode");
            this.newNode = newNode;
        }

        @Override // kotlinx.coroutines.internal.AtomicOp
        public void complete(kotlinx.coroutines.internal.LockFreeLinkedListNode affected, java.lang.Object failure) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(affected, "affected");
            boolean success = failure == null;
            kotlinx.coroutines.internal.LockFreeLinkedListNode update = success ? this.newNode : this.oldNext;
            if (update != null && affected._next.compareAndSet(this, update) && success) {
                kotlinx.coroutines.internal.LockFreeLinkedListNode lockFreeLinkedListNode = this.newNode;
                kotlinx.coroutines.internal.LockFreeLinkedListNode lockFreeLinkedListNode2 = this.oldNext;
                kotlin.jvm.internal.Intrinsics.checkNotNull(lockFreeLinkedListNode2);
                lockFreeLinkedListNode.finishAdd(lockFreeLinkedListNode2);
            }
        }
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.internal.LockFreeLinkedListNode$makeCondAddOp$1, reason: invalid class name */
    /* JADX INFO: compiled from: LockFreeLinkedList.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006H\u0016¨\u0006\u0007"}, d2 = {"kotlinx/coroutines/internal/LockFreeLinkedListNode$makeCondAddOp$1", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$CondAddOp;", "prepare", "", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/internal/Node;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass1 extends kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp {
        final /* synthetic */ kotlin.jvm.functions.Function0<java.lang.Boolean> $condition;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(kotlinx.coroutines.internal.LockFreeLinkedListNode $node, kotlin.jvm.functions.Function0<java.lang.Boolean> function0) {
            super($node);
            this.$condition = function0;
        }

        @Override // kotlinx.coroutines.internal.AtomicOp
        public java.lang.Object prepare(kotlinx.coroutines.internal.LockFreeLinkedListNode affected) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(affected, "affected");
            if (this.$condition.invoke().booleanValue()) {
                return null;
            }
            return kotlinx.coroutines.internal.LockFreeLinkedListKt.getCONDITION_FALSE();
        }
    }

    public final kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp makeCondAddOp(kotlinx.coroutines.internal.LockFreeLinkedListNode node, kotlin.jvm.functions.Function0<java.lang.Boolean> condition) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(condition, "condition");
        return new kotlinx.coroutines.internal.LockFreeLinkedListNode.AnonymousClass1(node, condition);
    }

    public boolean isRemoved() {
        return getNext() instanceof kotlinx.coroutines.internal.Removed;
    }

    public final java.lang.Object getNext() {
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._next;
        while (true) {
            java.lang.Object next = atomicRef.getValue();
            if (!(next instanceof kotlinx.coroutines.internal.OpDescriptor)) {
                return next;
            }
            ((kotlinx.coroutines.internal.OpDescriptor) next).perform(this);
        }
    }

    public final kotlinx.coroutines.internal.LockFreeLinkedListNode getNextNode() {
        return kotlinx.coroutines.internal.LockFreeLinkedListKt.unwrap(getNext());
    }

    public final kotlinx.coroutines.internal.LockFreeLinkedListNode getPrevNode() {
        kotlinx.coroutines.internal.LockFreeLinkedListNode lockFreeLinkedListNodeCorrectPrev = correctPrev(null);
        return lockFreeLinkedListNodeCorrectPrev == null ? findPrevNonRemoved(this._prev.getValue()) : lockFreeLinkedListNodeCorrectPrev;
    }

    private final kotlinx.coroutines.internal.LockFreeLinkedListNode findPrevNonRemoved(kotlinx.coroutines.internal.LockFreeLinkedListNode current) {
        while (current.isRemoved()) {
            current = current._prev.getValue();
        }
        return current;
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0015 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean addOneIfEmpty(kotlinx.coroutines.internal.LockFreeLinkedListNode r3) {
        /*
            r2 = this;
            java.lang.String r0 = "node"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeLinkedListNode> r0 = r3._prev
            r0.lazySet(r2)
            kotlinx.atomicfu.AtomicRef<java.lang.Object> r0 = r3._next
            r0.lazySet(r2)
        L10:
            java.lang.Object r0 = r2.getNext()
            if (r0 == r2) goto L19
            r1 = 0
            return r1
        L19:
            kotlinx.atomicfu.AtomicRef<java.lang.Object> r1 = r2._next
            boolean r1 = r1.compareAndSet(r2, r3)
            if (r1 == 0) goto L10
            r3.finishAdd(r2)
            r1 = 1
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeLinkedListNode.addOneIfEmpty(kotlinx.coroutines.internal.LockFreeLinkedListNode):boolean");
    }

    public final void addLast(kotlinx.coroutines.internal.LockFreeLinkedListNode node) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        while (!getPrevNode().addNext(node, this)) {
        }
    }

    public final boolean addLastIf(kotlinx.coroutines.internal.LockFreeLinkedListNode node, kotlin.jvm.functions.Function0<java.lang.Boolean> condition) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(condition, "condition");
        kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp condAdd = new kotlinx.coroutines.internal.LockFreeLinkedListNode.AnonymousClass1(node, condition);
        while (true) {
            kotlinx.coroutines.internal.LockFreeLinkedListNode prev = getPrevNode();
            switch (prev.tryCondAddNext(node, this, condAdd)) {
                case 1:
                    return true;
                case 2:
                    return false;
            }
        }
    }

    public final boolean addNext(kotlinx.coroutines.internal.LockFreeLinkedListNode node, kotlinx.coroutines.internal.LockFreeLinkedListNode next) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(next, "next");
        node._prev.lazySet(this);
        node._next.lazySet(next);
        if (!this._next.compareAndSet(next, node)) {
            return false;
        }
        node.finishAdd(next);
        return true;
    }

    public final int tryCondAddNext(kotlinx.coroutines.internal.LockFreeLinkedListNode node, kotlinx.coroutines.internal.LockFreeLinkedListNode next, kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp condAdd) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(next, "next");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(condAdd, "condAdd");
        node._prev.lazySet(this);
        node._next.lazySet(next);
        condAdd.oldNext = next;
        if (this._next.compareAndSet(next, condAdd)) {
            return condAdd.perform(this) == null ? 1 : 2;
        }
        return 0;
    }

    /* JADX INFO: renamed from: remove */
    public boolean mo12863remove() {
        return removeOrNext() == null;
    }

    public final kotlinx.coroutines.internal.LockFreeLinkedListNode removeOrNext() {
        java.lang.Object next;
        kotlinx.coroutines.internal.Removed removed;
        do {
            next = getNext();
            if (next instanceof kotlinx.coroutines.internal.Removed) {
                return ((kotlinx.coroutines.internal.Removed) next).ref;
            }
            if (next == this) {
                return (kotlinx.coroutines.internal.LockFreeLinkedListNode) next;
            }
            kotlin.jvm.internal.Intrinsics.checkNotNull(next, "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }");
            removed = ((kotlinx.coroutines.internal.LockFreeLinkedListNode) next).removed();
        } while (!this._next.compareAndSet(next, removed));
        ((kotlinx.coroutines.internal.LockFreeLinkedListNode) next).correctPrev(null);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void finishAdd(kotlinx.coroutines.internal.LockFreeLinkedListNode next) {
        kotlinx.coroutines.internal.LockFreeLinkedListNode nextPrev;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeLinkedListNode> atomicRef = next._prev;
        do {
            nextPrev = atomicRef.getValue();
            if (getNext() != next) {
                return;
            }
        } while (!next._prev.compareAndSet(nextPrev, this));
        if (isRemoved()) {
            next.correctPrev(null);
        }
    }

    protected kotlinx.coroutines.internal.LockFreeLinkedListNode nextIfRemoved() {
        java.lang.Object next = getNext();
        kotlinx.coroutines.internal.Removed removed = next instanceof kotlinx.coroutines.internal.Removed ? (kotlinx.coroutines.internal.Removed) next : null;
        if (removed != null) {
            return removed.ref;
        }
        return null;
    }

    private final kotlinx.coroutines.internal.LockFreeLinkedListNode correctPrev(kotlinx.coroutines.internal.OpDescriptor op) {
        while (true) {
            kotlinx.coroutines.internal.LockFreeLinkedListNode oldPrev = this._prev.getValue();
            kotlinx.coroutines.internal.LockFreeLinkedListNode prev = oldPrev;
            kotlinx.coroutines.internal.LockFreeLinkedListNode last = null;
            while (true) {
                java.lang.Object prevNext = prev._next.getValue();
                if (prevNext == this) {
                    if (oldPrev == prev) {
                        return prev;
                    }
                    if (this._prev.compareAndSet(oldPrev, prev)) {
                        return prev;
                    }
                } else {
                    if (isRemoved()) {
                        return null;
                    }
                    if (prevNext == op) {
                        return prev;
                    }
                    if (prevNext instanceof kotlinx.coroutines.internal.OpDescriptor) {
                        ((kotlinx.coroutines.internal.OpDescriptor) prevNext).perform(prev);
                        break;
                    }
                    if (prevNext instanceof kotlinx.coroutines.internal.Removed) {
                        if (last != null) {
                            if (!last._next.compareAndSet(prev, ((kotlinx.coroutines.internal.Removed) prevNext).ref)) {
                                break;
                            }
                            prev = last;
                            last = null;
                        } else {
                            prev = prev._prev.getValue();
                        }
                    } else {
                        last = prev;
                        kotlin.jvm.internal.Intrinsics.checkNotNull(prevNext, "null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }");
                        prev = (kotlinx.coroutines.internal.LockFreeLinkedListNode) prevNext;
                    }
                }
            }
        }
    }

    public final void validateNode$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.internal.LockFreeLinkedListNode prev, kotlinx.coroutines.internal.LockFreeLinkedListNode next) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prev, "prev");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(next, "next");
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((prev == this._prev.getValue() ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(next == this._next.getValue())) {
                throw new java.lang.AssertionError();
            }
        }
    }

    public java.lang.String toString() {
        return new kotlin.jvm.internal.PropertyReference0Impl(this) { // from class: kotlinx.coroutines.internal.LockFreeLinkedListNode.toString.1
            @Override // kotlin.jvm.internal.PropertyReference0Impl, kotlin.reflect.KProperty0
            public java.lang.Object get() {
                return kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this.receiver);
            }
        } + "@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this);
    }
}
