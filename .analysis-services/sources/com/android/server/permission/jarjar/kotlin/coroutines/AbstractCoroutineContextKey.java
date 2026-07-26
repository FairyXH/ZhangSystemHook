package com.android.server.permission.jarjar.kotlin.coroutines;

/* JADX INFO: compiled from: CoroutineContextImpl.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\b'\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u0002*\b\b\u0001\u0010\u0003*\u0002H\u00012\b\u0012\u0004\u0012\u0002H\u00030\u0004B8\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012#\u0010\u0006\u001a\u001f\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0006\u0012\u0004\u0018\u00018\u00010\u0007¢\u0006\u0002\u0010\u000bJ\u0019\u0010\r\u001a\u00020\u000e2\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0000¢\u0006\u0002\b\u0010J\u0019\u0010\u0011\u001a\u0004\u0018\u00018\u00012\u0006\u0010\n\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u0012\u0010\u0013R+\u0010\u0006\u001a\u001f\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0006\u0012\u0004\u0018\u00018\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u0006\u0012\u0002\b\u00030\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lkotlin/coroutines/AbstractCoroutineContextKey;", "B", "Lkotlin/coroutines/CoroutineContext$Element;", "E", "Lkotlin/coroutines/CoroutineContext$Key;", "baseKey", "safeCast", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "element", "(Lkotlin/coroutines/CoroutineContext$Key;Lkotlin/jvm/functions/Function1;)V", "topmostKey", "isSubKey", "", "key", "isSubKey$kotlin_stdlib", "tryCast", "tryCast$kotlin_stdlib", "(Lkotlin/coroutines/CoroutineContext$Element;)Lkotlin/coroutines/CoroutineContext$Element;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractCoroutineContextKey<B extends com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, E extends B> implements com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<E> {
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, E> safeCast;
    private final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> topmostKey;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext$Element, ? extends E extends B>, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext$Element, E extends B>, java.lang.Object] */
    public AbstractCoroutineContextKey(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<B> key, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, ? extends E> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "baseKey");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "safeCast");
        this.safeCast = function1;
        this.topmostKey = key instanceof com.android.server.permission.jarjar.kotlin.coroutines.AbstractCoroutineContextKey ? ((com.android.server.permission.jarjar.kotlin.coroutines.AbstractCoroutineContextKey) key).topmostKey : key;
    }

    /* JADX WARN: Incorrect return type in method signature: (Lcom/android/server/permission/jarjar/kotlin/coroutines/CoroutineContext$Element;)TE; */
    public final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element tryCast$kotlin_stdlib(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element element) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
        return (com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element) this.safeCast.invoke(element);
    }

    public final boolean isSubKey$kotlin_stdlib(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> key) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        return key == this || this.topmostKey == key;
    }
}
