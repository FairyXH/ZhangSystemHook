package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: compiled from: SequenceBuilder.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u00020\u00050\u0004B\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\t\u0010\u0018\u001a\u00020\u0019H\u0096\u0002J\u000e\u0010\u001a\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u001bJ\r\u0010\u001c\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u001bJ\u001b\u0010\u001d\u001a\u00020\u00052\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00050\u001fH\u0016¢\u0006\u0002\u0010 J\u0016\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010#J\u001c\u0010$\u001a\u00020\u00052\f\u0010%\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096@¢\u0006\u0002\u0010&R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0012\u0010\u0011\u001a\u0004\u0018\u00018\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0012R\u0012\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lkotlin/sequences/SequenceBuilderIterator;", "T", "Lkotlin/sequences/SequenceScope;", "", "Lkotlin/coroutines/Continuation;", "", "()V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "nextIterator", "nextStep", "getNextStep", "()Lkotlin/coroutines/Continuation;", "setNextStep", "(Lkotlin/coroutines/Continuation;)V", "nextValue", "Ljava/lang/Object;", "state", "", "Lkotlin/sequences/State;", "exceptionalState", "", "hasNext", "", "next", "()Ljava/lang/Object;", "nextNotReady", "resumeWith", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "yield", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "yieldAll", "iterator", "(Ljava/util/Iterator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class SequenceBuilderIterator<T> extends com.android.server.permission.jarjar.kotlin.sequences.SequenceScope<T> implements java.util.Iterator<T>, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<com.android.server.permission.jarjar.kotlin.Unit>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    private java.util.Iterator<? extends T> nextIterator;
    private com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> nextStep;
    private T nextValue;
    private int state;

    @Override // java.util.Iterator
    public void remove() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final com.android.server.permission.jarjar.kotlin.coroutines.Continuation<com.android.server.permission.jarjar.kotlin.Unit> getNextStep() {
        return this.nextStep;
    }

    public final void setNextStep(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation) {
        this.nextStep = continuation;
    }

    @Override // java.util.Iterator
    public boolean hasNext() throws java.lang.Throwable {
        while (true) {
            switch (this.state) {
                case 0:
                    break;
                case 1:
                    java.util.Iterator<? extends T> it = this.nextIterator;
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(it);
                    if (it.hasNext()) {
                        this.state = 2;
                        return true;
                    }
                    this.nextIterator = null;
                    break;
                    break;
                case 2:
                case 3:
                    return true;
                case 4:
                    return false;
                default:
                    throw exceptionalState();
            }
            this.state = 5;
            com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation = this.nextStep;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(continuation);
            this.nextStep = null;
            com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
            continuation.resumeWith(com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(com.android.server.permission.jarjar.kotlin.Unit.INSTANCE));
        }
    }

    @Override // java.util.Iterator
    public T next() throws java.lang.Throwable {
        switch (this.state) {
            case 0:
            case 1:
                return nextNotReady();
            case 2:
                this.state = 1;
                java.util.Iterator<? extends T> it = this.nextIterator;
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(it);
                return it.next();
            case 3:
                this.state = 0;
                T t = this.nextValue;
                this.nextValue = null;
                return t;
            default:
                throw exceptionalState();
        }
    }

    private final T nextNotReady() {
        if (hasNext()) {
            return next();
        }
        throw new java.util.NoSuchElementException();
    }

    private final java.lang.Throwable exceptionalState() {
        switch (this.state) {
            case 4:
                return new java.util.NoSuchElementException();
            case 5:
                return new java.lang.IllegalStateException("Iterator has failed.");
            default:
                return new java.lang.IllegalStateException("Unexpected state of the iterator: " + this.state);
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.SequenceScope
    public java.lang.Object yield(T t, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation) {
        this.nextValue = t;
        this.state = 3;
        this.nextStep = continuation;
        java.lang.Object coroutine_suspended = com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (coroutine_suspended == com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return coroutine_suspended == com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? coroutine_suspended : com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.SequenceScope
    public java.lang.Object yieldAll(java.util.Iterator<? extends T> it, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation) {
        if (!it.hasNext()) {
            return com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
        this.nextIterator = it;
        this.state = 2;
        this.nextStep = continuation;
        java.lang.Object coroutine_suspended = com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (coroutine_suspended == com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return coroutine_suspended == com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? coroutine_suspended : com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
    public void resumeWith(java.lang.Object result) throws java.lang.Throwable {
        com.android.server.permission.jarjar.kotlin.ResultKt.throwOnFailure(result);
        this.state = 4;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext getContext() {
        return com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
    }
}
