package com.android.server.permission.jarjar.kotlin.coroutines;

/* JADX INFO: compiled from: CoroutineContextImpl.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b'\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0002\u0010\u0004R\u0018\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/coroutines/AbstractCoroutineContextElement;", "Lkotlin/coroutines/CoroutineContext$Element;", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)V", "getKey", "()Lkotlin/coroutines/CoroutineContext$Key;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractCoroutineContextElement implements com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element {
    private final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> key;

    public AbstractCoroutineContextElement(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> key) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        this.key = key;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super R, ? super com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, ? extends R> function2) {
        return (R) com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element.DefaultImpls.fold(this, r, function2);
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public <E extends com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element> E get(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<E> key) {
        return (E) com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element.DefaultImpls.get(this, key);
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext minusKey(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> key) {
        return com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element.DefaultImpls.minusKey(this, key);
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext plus(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext context) {
        return com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element.DefaultImpls.plus(this, context);
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> getKey() {
        return this.key;
    }
}
