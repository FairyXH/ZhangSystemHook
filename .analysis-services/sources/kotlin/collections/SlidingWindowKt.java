package kotlin.collections;

/* JADX INFO: compiled from: SlidingWindow.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aH\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u001aD\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u000e\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000¨\u0006\u000f"}, d2 = {"checkWindowSizeStep", "", "size", "", "step", "windowedIterator", "", "", "T", "iterator", "partialWindows", "", "reuseBuffer", "windowedSequence", "Lkotlin/sequences/Sequence;", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SlidingWindowKt {
    public static final void checkWindowSizeStep(int size, int step) {
        if (!(size > 0 && step > 0)) {
            throw new java.lang.IllegalArgumentException((size != step ? "Both size " + size + " and step " + step + " must be greater than zero." : "size " + size + " must be greater than zero.").toString());
        }
    }

    public static final <T> kotlin.sequences.Sequence<java.util.List<T>> windowedSequence(final kotlin.sequences.Sequence<? extends T> sequence, final int i, final int i2, final boolean z, final boolean z2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        checkWindowSizeStep(i, i2);
        return new kotlin.sequences.Sequence<java.util.List<? extends T>>() { // from class: kotlin.collections.SlidingWindowKt$windowedSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            public java.util.Iterator<java.util.List<? extends T>> iterator() {
                return kotlin.collections.SlidingWindowKt.windowedIterator(sequence.iterator(), i, i2, z, z2);
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlin.collections.SlidingWindowKt$windowedIterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: SlidingWindow.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\u008a@"}, d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;", ""}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlin.collections.SlidingWindowKt$windowedIterator$1", f = "SlidingWindow.kt", i = {0, 0, 0, 2, 2, 3, 3}, l = {34, 40, 49, 55, 58}, m = "invokeSuspend", n = {"$this$iterator", "buffer", "gap", "$this$iterator", "buffer", "$this$iterator", "buffer"}, s = {"L$0", "L$1", "I$0", "L$0", "L$1", "L$0", "L$1"})
    static final class AnonymousClass1<T> extends kotlin.coroutines.jvm.internal.RestrictedSuspendLambda implements kotlin.jvm.functions.Function2<kotlin.sequences.SequenceScope<? super java.util.List<? extends T>>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ java.util.Iterator<T> $iterator;
        final /* synthetic */ boolean $partialWindows;
        final /* synthetic */ boolean $reuseBuffer;
        final /* synthetic */ int $size;
        final /* synthetic */ int $step;
        int I$0;
        private /* synthetic */ java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass1(int i, int i2, java.util.Iterator<? extends T> it, boolean z, boolean z2, kotlin.coroutines.Continuation<? super kotlin.collections.SlidingWindowKt.AnonymousClass1> continuation) {
            super(2, continuation);
            this.$size = i;
            this.$step = i2;
            this.$iterator = it;
            this.$reuseBuffer = z;
            this.$partialWindows = z2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlin.collections.SlidingWindowKt.AnonymousClass1 anonymousClass1 = new kotlin.collections.SlidingWindowKt.AnonymousClass1(this.$size, this.$step, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
            anonymousClass1.L$0 = obj;
            return anonymousClass1;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlin.sequences.SequenceScope<? super java.util.List<? extends T>> sequenceScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlin.collections.SlidingWindowKt.AnonymousClass1) create(sequenceScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0087  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x00b3  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x00b7  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00e8 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00fc  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0147  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x0154  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x017f  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x019d  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x00ac -> B:24:0x00af). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:52:0x013a -> B:54:0x013d). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:65:0x0176 -> B:67:0x0179). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r14) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 434
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.SlidingWindowKt.AnonymousClass1.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    public static final <T> java.util.Iterator<java.util.List<T>> windowedIterator(java.util.Iterator<? extends T> iterator, int size, int step, boolean partialWindows, boolean reuseBuffer) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterator, "iterator");
        return !iterator.hasNext() ? kotlin.collections.EmptyIterator.INSTANCE : kotlin.sequences.SequencesKt.iterator(new kotlin.collections.SlidingWindowKt.AnonymousClass1(size, step, iterator, reuseBuffer, partialWindows, null));
    }
}
