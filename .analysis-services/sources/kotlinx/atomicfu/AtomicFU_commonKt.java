package kotlinx.atomicfu;

/* JADX INFO: compiled from: AtomicFU.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a\u001e\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001a$\u0010\u0005\u001a\u00020\u0006*\u00020\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\tH\u0086\bø\u0001\u0000\u001a$\u0010\u0005\u001a\u00020\u0004*\u00020\n2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\tH\u0086\bø\u0001\u0000\u001a$\u0010\u0005\u001a\u00020\u000b*\u00020\f2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\tH\u0086\bø\u0001\u0000\u001a5\u0010\u0005\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00020\tH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u000e\u001a$\u0010\u000f\u001a\u00020\u0010*\u00020\u00072\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00120\tH\u0086\bø\u0001\u0000\u001a$\u0010\u000f\u001a\u00020\u0010*\u00020\n2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00120\tH\u0086\bø\u0001\u0000\u001a$\u0010\u000f\u001a\u00020\u0010*\u00020\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00120\tH\u0086\bø\u0001\u0000\u001a0\u0010\u000f\u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00120\tH\u0086\bø\u0001\u0000\u001a$\u0010\u0013\u001a\u00020\u0012*\u00020\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\tH\u0086\bø\u0001\u0000\u001a$\u0010\u0013\u001a\u00020\u0012*\u00020\n2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\tH\u0086\bø\u0001\u0000\u001a$\u0010\u0013\u001a\u00020\u0012*\u00020\f2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\tH\u0086\bø\u0001\u0000\u001a0\u0010\u0013\u001a\u00020\u0012\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00020\tH\u0086\bø\u0001\u0000\u001a$\u0010\u0014\u001a\u00020\u0006*\u00020\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\tH\u0086\bø\u0001\u0000\u001a$\u0010\u0014\u001a\u00020\u0004*\u00020\n2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\tH\u0086\bø\u0001\u0000\u001a$\u0010\u0014\u001a\u00020\u000b*\u00020\f2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\tH\u0086\bø\u0001\u0000\u001a5\u0010\u0014\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00020\tH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u000e\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0015"}, d2 = {"atomicArrayOfNulls", "Lkotlinx/atomicfu/AtomicArray;", "T", "size", "", "getAndUpdate", "", "Lkotlinx/atomicfu/AtomicBoolean;", "function", "Lkotlin/Function1;", "Lkotlinx/atomicfu/AtomicInt;", "", "Lkotlinx/atomicfu/AtomicLong;", "Lkotlinx/atomicfu/AtomicRef;", "(Lkotlinx/atomicfu/AtomicRef;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "loop", "", "action", "", "update", "updateAndGet", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class AtomicFU_commonKt {
    public static final <T> kotlinx.atomicfu.AtomicArray<T> atomicArrayOfNulls(int size) {
        return new kotlinx.atomicfu.AtomicArray<>(size);
    }

    public static final <T> java.lang.Void loop(kotlinx.atomicfu.AtomicRef<T> atomicRef, kotlin.jvm.functions.Function1<? super T, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(atomicRef, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        while (true) {
            action.invoke(atomicRef.getValue());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> void update(kotlinx.atomicfu.AtomicRef<T> atomicRef, kotlin.jvm.functions.Function1<? super T, ? extends T> function) {
        java.lang.Object cur;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(atomicRef, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = atomicRef.getValue();
        } while (!atomicRef.compareAndSet(cur, function.invoke(cur)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [T, java.lang.Object] */
    public static final <T> T getAndUpdate(kotlinx.atomicfu.AtomicRef<T> atomicRef, kotlin.jvm.functions.Function1<? super T, ? extends T> function) {
        ?? r1;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(atomicRef, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            r1 = (java.lang.Object) atomicRef.getValue();
        } while (!atomicRef.compareAndSet(r1, function.invoke(r1)));
        return r1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> T updateAndGet(kotlinx.atomicfu.AtomicRef<T> atomicRef, kotlin.jvm.functions.Function1<? super T, ? extends T> function) {
        java.lang.Object cur;
        T tInvoke;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(atomicRef, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = atomicRef.getValue();
            tInvoke = function.invoke(cur);
        } while (!atomicRef.compareAndSet(cur, tInvoke));
        return tInvoke;
    }

    public static final java.lang.Void loop(kotlinx.atomicfu.AtomicBoolean $this$loop, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$loop, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        while (true) {
            action.invoke(java.lang.Boolean.valueOf($this$loop.getValue()));
        }
    }

    public static final void update(kotlinx.atomicfu.AtomicBoolean $this$update, kotlin.jvm.functions.Function1<? super java.lang.Boolean, java.lang.Boolean> function) {
        boolean cur;
        boolean upd;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$update, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = $this$update.getValue();
            upd = function.invoke(java.lang.Boolean.valueOf(cur)).booleanValue();
        } while (!$this$update.compareAndSet(cur, upd));
    }

    public static final boolean getAndUpdate(kotlinx.atomicfu.AtomicBoolean $this$getAndUpdate, kotlin.jvm.functions.Function1<? super java.lang.Boolean, java.lang.Boolean> function) {
        boolean cur;
        boolean upd;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$getAndUpdate, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = $this$getAndUpdate.getValue();
            upd = function.invoke(java.lang.Boolean.valueOf(cur)).booleanValue();
        } while (!$this$getAndUpdate.compareAndSet(cur, upd));
        return cur;
    }

    public static final boolean updateAndGet(kotlinx.atomicfu.AtomicBoolean $this$updateAndGet, kotlin.jvm.functions.Function1<? super java.lang.Boolean, java.lang.Boolean> function) {
        boolean cur;
        boolean upd;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$updateAndGet, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = $this$updateAndGet.getValue();
            upd = function.invoke(java.lang.Boolean.valueOf(cur)).booleanValue();
        } while (!$this$updateAndGet.compareAndSet(cur, upd));
        return upd;
    }

    public static final java.lang.Void loop(kotlinx.atomicfu.AtomicInt $this$loop, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$loop, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        while (true) {
            action.invoke(java.lang.Integer.valueOf($this$loop.getValue()));
        }
    }

    public static final void update(kotlinx.atomicfu.AtomicInt $this$update, kotlin.jvm.functions.Function1<? super java.lang.Integer, java.lang.Integer> function) {
        int cur;
        int upd;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$update, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = $this$update.getValue();
            upd = function.invoke(java.lang.Integer.valueOf(cur)).intValue();
        } while (!$this$update.compareAndSet(cur, upd));
    }

    public static final int getAndUpdate(kotlinx.atomicfu.AtomicInt $this$getAndUpdate, kotlin.jvm.functions.Function1<? super java.lang.Integer, java.lang.Integer> function) {
        int cur;
        int upd;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$getAndUpdate, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = $this$getAndUpdate.getValue();
            upd = function.invoke(java.lang.Integer.valueOf(cur)).intValue();
        } while (!$this$getAndUpdate.compareAndSet(cur, upd));
        return cur;
    }

    public static final int updateAndGet(kotlinx.atomicfu.AtomicInt $this$updateAndGet, kotlin.jvm.functions.Function1<? super java.lang.Integer, java.lang.Integer> function) {
        int cur;
        int upd;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$updateAndGet, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = $this$updateAndGet.getValue();
            upd = function.invoke(java.lang.Integer.valueOf(cur)).intValue();
        } while (!$this$updateAndGet.compareAndSet(cur, upd));
        return upd;
    }

    public static final java.lang.Void loop(kotlinx.atomicfu.AtomicLong $this$loop, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$loop, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        while (true) {
            action.invoke(java.lang.Long.valueOf($this$loop.getValue()));
        }
    }

    public static final void update(kotlinx.atomicfu.AtomicLong $this$update, kotlin.jvm.functions.Function1<? super java.lang.Long, java.lang.Long> function) {
        long cur;
        long upd;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$update, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = $this$update.getValue();
            upd = function.invoke(java.lang.Long.valueOf(cur)).longValue();
        } while (!$this$update.compareAndSet(cur, upd));
    }

    public static final long getAndUpdate(kotlinx.atomicfu.AtomicLong $this$getAndUpdate, kotlin.jvm.functions.Function1<? super java.lang.Long, java.lang.Long> function) {
        long cur;
        long upd;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$getAndUpdate, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = $this$getAndUpdate.getValue();
            upd = function.invoke(java.lang.Long.valueOf(cur)).longValue();
        } while (!$this$getAndUpdate.compareAndSet(cur, upd));
        return cur;
    }

    public static final long updateAndGet(kotlinx.atomicfu.AtomicLong $this$updateAndGet, kotlin.jvm.functions.Function1<? super java.lang.Long, java.lang.Long> function) {
        long cur;
        long upd;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$updateAndGet, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            cur = $this$updateAndGet.getValue();
            upd = function.invoke(java.lang.Long.valueOf(cur)).longValue();
        } while (!$this$updateAndGet.compareAndSet(cur, upd));
        return upd;
    }
}
