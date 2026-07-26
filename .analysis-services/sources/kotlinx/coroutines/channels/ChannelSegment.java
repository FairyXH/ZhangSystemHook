package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: BufferedChannel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00000\u0002B5\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u000e\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000\u0012\u000e\u0010\u0006\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ)\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\t2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00102\b\u0010\u0018\u001a\u0004\u0018\u00010\u0010H\u0000¢\u0006\u0002\b\u0019J\u0015\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0016\u001a\u00020\tH\u0000¢\u0006\u0002\b\u001cJ!\u0010\u001d\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0016\u001a\u00020\t2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0010H\u0000¢\u0006\u0002\b\u001fJ\u0017\u0010 \u001a\u00028\u00002\u0006\u0010\u0016\u001a\u00020\tH\u0000¢\u0006\u0004\b!\u0010\"J\u0017\u0010#\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0016\u001a\u00020\tH\u0000¢\u0006\u0002\b$J\"\u0010%\u001a\u00020\u001b2\u0006\u0010\u0016\u001a\u00020\t2\b\u0010&\u001a\u0004\u0018\u00010'2\u0006\u0010(\u001a\u00020)H\u0016J\u0016\u0010*\u001a\u00020\u001b2\u0006\u0010\u0016\u001a\u00020\t2\u0006\u0010+\u001a\u00020\u0015J\u0017\u0010,\u001a\u00028\u00002\u0006\u0010\u0016\u001a\u00020\tH\u0000¢\u0006\u0004\b-\u0010\"J\u001a\u0010.\u001a\u00020\u001b2\u0006\u0010\u0016\u001a\u00020\t2\b\u0010/\u001a\u0004\u0018\u00010\u0010H\u0002J\u001f\u00100\u001a\u00020\u001b2\u0006\u0010\u0016\u001a\u00020\t2\b\u0010/\u001a\u0004\u0018\u00010\u0010H\u0000¢\u0006\u0002\b1J\u001f\u00102\u001a\u00020\u001b2\u0006\u0010\u0016\u001a\u00020\t2\u0006\u00103\u001a\u00028\u0000H\u0000¢\u0006\u0004\b4\u00105R\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u00078F¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0016\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u00066"}, d2 = {"Lkotlinx/coroutines/channels/ChannelSegment;", "E", "Lkotlinx/coroutines/internal/Segment;", "id", "", "prev", "channel", "Lkotlinx/coroutines/channels/BufferedChannel;", "pointers", "", "(JLkotlinx/coroutines/channels/ChannelSegment;Lkotlinx/coroutines/channels/BufferedChannel;I)V", "_channel", "getChannel", "()Lkotlinx/coroutines/channels/BufferedChannel;", "data", "Lkotlinx/atomicfu/AtomicArray;", "", "numberOfSlots", "getNumberOfSlots", "()I", "casState", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "from", "to", "casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "cleanElement", "", "cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "getAndSetState", "update", "getAndSetState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "getElement", "getElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(I)Ljava/lang/Object;", "getState", "getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "onCancellation", "cause", "", "context", "Lkotlin/coroutines/CoroutineContext;", "onCancelledRequest", "receiver", "retrieveElement", "retrieveElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "setElementLazy", "value", "setState", "setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "storeElement", "element", "storeElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(ILjava/lang/Object;)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ChannelSegment<E> extends kotlinx.coroutines.internal.Segment<kotlinx.coroutines.channels.ChannelSegment<E>> {
    private final kotlinx.coroutines.channels.BufferedChannel<E> _channel;
    private final kotlinx.atomicfu.AtomicArray<java.lang.Object> data;

    public ChannelSegment(long id, kotlinx.coroutines.channels.ChannelSegment<E> channelSegment, kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel, int pointers) {
        super(id, channelSegment, pointers);
        this._channel = bufferedChannel;
        this.data = kotlinx.atomicfu.AtomicFU_commonKt.atomicArrayOfNulls(kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE * 2);
    }

    public final kotlinx.coroutines.channels.BufferedChannel<E> getChannel() {
        kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel = this._channel;
        kotlin.jvm.internal.Intrinsics.checkNotNull(bufferedChannel);
        return bufferedChannel;
    }

    @Override // kotlinx.coroutines.internal.Segment
    public int getNumberOfSlots() {
        return kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE;
    }

    public final void storeElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(int index, E element) {
        setElementLazy(index, element);
    }

    public final E getElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(int index) {
        return (E) this.data.get(index * 2).getValue();
    }

    public final E retrieveElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(int index) {
        E element$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = getElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
        cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
        return element$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host;
    }

    public final void cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(int index) {
        setElementLazy(index, null);
    }

    private final void setElementLazy(int index, java.lang.Object value) {
        this.data.get(index * 2).lazySet(value);
    }

    public final java.lang.Object getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(int index) {
        return this.data.get((index * 2) + 1).getValue();
    }

    public final void setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(int index, java.lang.Object value) {
        this.data.get((index * 2) + 1).setValue(value);
    }

    public final boolean casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(int index, java.lang.Object from, java.lang.Object to) {
        return this.data.get((index * 2) + 1).compareAndSet(from, to);
    }

    public final java.lang.Object getAndSetState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(int index, java.lang.Object update) {
        return this.data.get((index * 2) + 1).getAndSet(update);
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0074, code lost:
    
        cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0077, code lost:
    
        if (r0 == false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0079, code lost:
    
        r1 = getChannel().onUndeliveredElement;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x007f, code lost:
    
        if (r1 == null) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0081, code lost:
    
        kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElement(r1, r4, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0084, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:?, code lost:
    
        return;
     */
    @Override // kotlinx.coroutines.internal.Segment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onCancellation(int r9, java.lang.Throwable r10, kotlin.coroutines.CoroutineContext r11) {
        /*
            r8 = this;
            java.lang.String r0 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            int r0 = kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE
            r1 = 1
            r2 = 0
            if (r9 < r0) goto Ld
            r0 = r1
            goto Le
        Ld:
            r0 = r2
        Le:
            if (r0 == 0) goto L15
            int r3 = kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE
            int r3 = r9 - r3
            goto L16
        L15:
            r3 = r9
        L16:
            java.lang.Object r4 = r8.getElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r3)
        L1a:
            java.lang.Object r5 = r8.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r3)
            boolean r6 = r5 instanceof kotlinx.coroutines.Waiter
            if (r6 != 0) goto L85
            boolean r6 = r5 instanceof kotlinx.coroutines.channels.WaiterEB
            if (r6 == 0) goto L29
            goto L85
        L29:
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.BufferedChannelKt.access$getINTERRUPTED_SEND$p()
            if (r5 == r6) goto L74
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.BufferedChannelKt.access$getINTERRUPTED_RCV$p()
            if (r5 != r6) goto L36
            goto L74
        L36:
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.BufferedChannelKt.access$getRESUMING_BY_EB$p()
            if (r5 == r6) goto L1a
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.BufferedChannelKt.access$getRESUMING_BY_RCV$p()
            if (r5 != r6) goto L43
            goto L1a
        L43:
            kotlinx.coroutines.internal.Symbol r1 = kotlinx.coroutines.channels.BufferedChannelKt.access$getDONE_RCV$p()
            if (r5 == r1) goto L73
            kotlinx.coroutines.internal.Symbol r1 = kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED
            if (r5 != r1) goto L4e
            goto L73
        L4e:
            kotlinx.coroutines.internal.Symbol r1 = kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()
            if (r5 != r1) goto L55
            return
        L55:
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r6 = "unexpected state: "
            java.lang.StringBuilder r2 = r2.append(r6)
            java.lang.StringBuilder r2 = r2.append(r5)
            java.lang.String r2 = r2.toString()
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L73:
            return
        L74:
            r8.cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r3)
            if (r0 == 0) goto L84
            kotlinx.coroutines.channels.BufferedChannel r1 = r8.getChannel()
            kotlin.jvm.functions.Function1<E, kotlin.Unit> r1 = r1.onUndeliveredElement
            if (r1 == 0) goto L84
            kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElement(r1, r4, r11)
        L84:
            return
        L85:
            if (r0 == 0) goto L8c
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.BufferedChannelKt.access$getINTERRUPTED_SEND$p()
            goto L90
        L8c:
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.BufferedChannelKt.access$getINTERRUPTED_RCV$p()
        L90:
            boolean r7 = r8.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r3, r5, r6)
            if (r7 == 0) goto L1a
            r8.cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r3)
            if (r0 != 0) goto L9c
            goto L9d
        L9c:
            r1 = r2
        L9d:
            r8.onCancelledRequest(r3, r1)
            if (r0 == 0) goto Lad
            kotlinx.coroutines.channels.BufferedChannel r1 = r8.getChannel()
            kotlin.jvm.functions.Function1<E, kotlin.Unit> r1 = r1.onUndeliveredElement
            if (r1 == 0) goto Lad
            kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElement(r1, r4, r11)
        Lad:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ChannelSegment.onCancellation(int, java.lang.Throwable, kotlin.coroutines.CoroutineContext):void");
    }

    public final void onCancelledRequest(int index, boolean receiver) {
        if (receiver) {
            getChannel().waitExpandBufferCompletion$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host((this.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE)) + ((long) index));
        }
        onSlotCleaned();
    }
}
