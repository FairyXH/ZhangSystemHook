package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: LazyJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0002\u0018\u0000 \u0013*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004:\u0001\u0013B\u0013\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\tH\u0002R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0006X\u0088\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00028\u00008VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u0014"}, d2 = {"Lkotlin/SafePublicationLazyImpl;", "T", "Lkotlin/Lazy;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "initializer", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)V", "_value", "", "final", "value", "getValue", "()Ljava/lang/Object;", "isInitialized", "", "toString", "", "writeReplace", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class SafePublicationLazyImpl<T> implements com.android.server.permission.jarjar.kotlin.Lazy<T>, java.io.Serializable {
    public static final com.android.server.permission.jarjar.kotlin.SafePublicationLazyImpl.Companion Companion = new com.android.server.permission.jarjar.kotlin.SafePublicationLazyImpl.Companion(null);
    private static final java.util.concurrent.atomic.AtomicReferenceFieldUpdater<com.android.server.permission.jarjar.kotlin.SafePublicationLazyImpl<?>, java.lang.Object> valueUpdater = java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater(com.android.server.permission.jarjar.kotlin.SafePublicationLazyImpl.class, java.lang.Object.class, "_value");
    private volatile java.lang.Object _value;

    /* JADX INFO: renamed from: final, reason: not valid java name */
    private final java.lang.Object f0final;
    private volatile com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> initializer;

    public SafePublicationLazyImpl(com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "initializer");
        this.initializer = function0;
        this._value = com.android.server.permission.jarjar.kotlin.UNINITIALIZED_VALUE.INSTANCE;
        this.f0final = com.android.server.permission.jarjar.kotlin.UNINITIALIZED_VALUE.INSTANCE;
    }

    @Override // com.android.server.permission.jarjar.kotlin.Lazy
    public T getValue() {
        T t = (T) this._value;
        if (t != com.android.server.permission.jarjar.kotlin.UNINITIALIZED_VALUE.INSTANCE) {
            return t;
        }
        com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0 = this.initializer;
        if (function0 != null) {
            T tInvoke = function0.invoke();
            if (valueUpdater.compareAndSet(this, com.android.server.permission.jarjar.kotlin.UNINITIALIZED_VALUE.INSTANCE, tInvoke)) {
                this.initializer = null;
                return tInvoke;
            }
        }
        return (T) this._value;
    }

    @Override // com.android.server.permission.jarjar.kotlin.Lazy
    public boolean isInitialized() {
        return this._value != com.android.server.permission.jarjar.kotlin.UNINITIALIZED_VALUE.INSTANCE;
    }

    public java.lang.String toString() {
        return isInitialized() ? java.lang.String.valueOf(getValue()) : "Lazy value not initialized yet.";
    }

    private final java.lang.Object writeReplace() {
        return new com.android.server.permission.jarjar.kotlin.InitializedLazyImpl(getValue());
    }

    /* JADX INFO: compiled from: LazyJVM.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R^\u0010\u0003\u001aR\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00010\u0001 \u0006*(\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00010\u0001\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlin/SafePublicationLazyImpl$Companion;", "", "()V", "valueUpdater", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlin/SafePublicationLazyImpl;", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
