package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: AbstractSharedFlow.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b \u0018\u0000*\f\b\u0000\u0010\u0001*\u0006\u0012\u0002\b\u00030\u00022\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\r\u0010\u0017\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u0018J\r\u0010\u0019\u001a\u00028\u0000H$¢\u0006\u0002\u0010\u0018J\u001d\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000e2\u0006\u0010\u001b\u001a\u00020\tH$¢\u0006\u0002\u0010\u001cJ \u0010\u001d\u001a\u00020\u001e2\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u001e0 H\u0084\bø\u0001\u0000J\u0015\u0010!\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010#R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t@BX\u0084\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R4\u0010\u000f\u001a\f\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0018\u00010\u000e2\u0010\u0010\b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0018\u00010\u000e@BX\u0084\u000e¢\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\t0\u00148F¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006$"}, d2 = {"Lkotlinx/coroutines/flow/internal/AbstractSharedFlow;", "S", "Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "()V", "_subscriptionCount", "Lkotlinx/coroutines/flow/internal/SubscriptionCountStateFlow;", "<set-?>", "", "nCollectors", "getNCollectors", "()I", "nextIndex", "", "slots", "getSlots", "()[Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "[Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "subscriptionCount", "Lkotlinx/coroutines/flow/StateFlow;", "getSubscriptionCount", "()Lkotlinx/coroutines/flow/StateFlow;", "allocateSlot", "()Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "createSlot", "createSlotArray", "size", "(I)[Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "forEachSlotLocked", "", "block", "Lkotlin/Function1;", "freeSlot", "slot", "(Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractSharedFlow<S extends kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot<?>> {
    private kotlinx.coroutines.flow.internal.SubscriptionCountStateFlow _subscriptionCount;
    private int nCollectors;
    private int nextIndex;
    private S[] slots;

    protected abstract S createSlot();

    protected abstract S[] createSlotArray(int size);

    protected final S[] getSlots() {
        return this.slots;
    }

    protected final int getNCollectors() {
        return this.nCollectors;
    }

    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getSubscriptionCount() {
        kotlinx.coroutines.flow.internal.SubscriptionCountStateFlow it;
        synchronized (this) {
            it = this._subscriptionCount;
            if (it == null) {
                it = new kotlinx.coroutines.flow.internal.SubscriptionCountStateFlow(this.nCollectors);
                this._subscriptionCount = it;
            }
        }
        return it;
    }

    protected final S allocateSlot() {
        S s;
        kotlinx.coroutines.flow.internal.SubscriptionCountStateFlow subscriptionCountStateFlow;
        synchronized (this) {
            S[] sArr = this.slots;
            if (sArr == null) {
                S[] sArr2 = (S[]) createSlotArray(2);
                this.slots = sArr2;
                sArr = sArr2;
            } else if (this.nCollectors >= sArr.length) {
                java.lang.Object[] objArrCopyOf = java.util.Arrays.copyOf(sArr, sArr.length * 2);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf, "copyOf(...)");
                this.slots = (S[]) ((kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot[]) objArrCopyOf);
                sArr = (S[]) ((kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot[]) objArrCopyOf);
            }
            int i = this.nextIndex;
            do {
                kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot abstractSharedFlowSlotCreateSlot = sArr[i];
                if (abstractSharedFlowSlotCreateSlot == null) {
                    abstractSharedFlowSlotCreateSlot = createSlot();
                    sArr[i] = abstractSharedFlowSlotCreateSlot;
                }
                s = (S) abstractSharedFlowSlotCreateSlot;
                i++;
                if (i >= sArr.length) {
                    i = 0;
                }
                kotlin.jvm.internal.Intrinsics.checkNotNull(s, "null cannot be cast to non-null type kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot<kotlin.Any>");
            } while (!s.allocateLocked(this));
            this.nextIndex = i;
            this.nCollectors++;
            subscriptionCountStateFlow = this._subscriptionCount;
        }
        if (subscriptionCountStateFlow != null) {
            subscriptionCountStateFlow.increment(1);
        }
        return s;
    }

    protected final void freeSlot(S slot) {
        kotlinx.coroutines.flow.internal.SubscriptionCountStateFlow subscriptionCountStateFlow;
        int i;
        kotlin.coroutines.Continuation<kotlin.Unit>[] continuationArrFreeLocked;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(slot, "slot");
        synchronized (this) {
            this.nCollectors--;
            subscriptionCountStateFlow = this._subscriptionCount;
            if (this.nCollectors == 0) {
                this.nextIndex = 0;
            }
            continuationArrFreeLocked = slot.freeLocked(this);
        }
        for (kotlin.coroutines.Continuation<kotlin.Unit> continuation : continuationArrFreeLocked) {
            if (continuation != null) {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                continuation.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE));
            }
        }
        if (subscriptionCountStateFlow != null) {
            subscriptionCountStateFlow.increment(-1);
        }
    }

    protected final void forEachSlotLocked(kotlin.jvm.functions.Function1<? super S, kotlin.Unit> block) {
        java.lang.Object[] $this$forEach$iv;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        if (this.nCollectors == 0 || ($this$forEach$iv = this.slots) == null) {
            return;
        }
        for (java.lang.Object element$iv : $this$forEach$iv) {
            if (element$iv != null) {
                block.invoke(element$iv);
            }
        }
    }
}
