package com.android.server.permission.jarjar.kotlin.coroutines;

/* JADX INFO: compiled from: CoroutineContextImpl.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\bÇ\u0002\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0004J5\u0010\u0007\u001a\u0002H\b\"\u0004\b\u0000\u0010\b2\u0006\u0010\t\u001a\u0002H\b2\u0018\u0010\n\u001a\u0014\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u0002H\b0\u000bH\u0016¢\u0006\u0002\u0010\rJ(\u0010\u000e\u001a\u0004\u0018\u0001H\u000f\"\b\b\u0000\u0010\u000f*\u00020\f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0011H\u0096\u0002¢\u0006\u0002\u0010\u0012J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u0014\u0010\u0015\u001a\u00020\u00012\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0011H\u0016J\u0011\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0096\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lkotlin/coroutines/EmptyCoroutineContext;", "Lkotlin/coroutines/CoroutineContext;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "serialVersionUID", "", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "Lkotlin/coroutines/CoroutineContext$Element;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "minusKey", "plus", "context", "readResolve", "", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class EmptyCoroutineContext implements com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext, java.io.Serializable {
    public static final com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext INSTANCE = new com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext();
    private static final long serialVersionUID = 0;

    private EmptyCoroutineContext() {
    }

    private final java.lang.Object readResolve() {
        return INSTANCE;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public <E extends com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element> E get(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<E> key) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        return null;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super R, ? super com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, ? extends R> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "operation");
        return r;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext plus(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext context) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        return context;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext minusKey(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> key) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        return this;
    }

    public int hashCode() {
        return 0;
    }

    public java.lang.String toString() {
        return "EmptyCoroutineContext";
    }
}
