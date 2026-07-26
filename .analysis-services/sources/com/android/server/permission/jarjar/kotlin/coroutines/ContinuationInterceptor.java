package com.android.server.permission.jarjar.kotlin.coroutines;

/* JADX INFO: compiled from: ContinuationInterceptor.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bg\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fJ(\u0010\u0002\u001a\u0004\u0018\u0001H\u0003\"\b\b\u0000\u0010\u0003*\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0005H\u0096\u0002¢\u0006\u0002\u0010\u0006J\"\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\bH&J\u0014\u0010\u000b\u001a\u00020\f2\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\u0014\u0010\r\u001a\u00020\u000e2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\bH\u0016¨\u0006\u0010"}, d2 = {"Lkotlin/coroutines/ContinuationInterceptor;", "Lkotlin/coroutines/CoroutineContext$Element;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "interceptContinuation", "Lkotlin/coroutines/Continuation;", "T", "continuation", "minusKey", "Lkotlin/coroutines/CoroutineContext;", "releaseInterceptedContinuation", "", "Key", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ContinuationInterceptor extends com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element {
    public static final com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor.Key Key = com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor.Key.$$INSTANCE;

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    <E extends com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element> E get(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<E> key);

    <T> com.android.server.permission.jarjar.kotlin.coroutines.Continuation<T> interceptContinuation(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T> continuation);

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext minusKey(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> key);

    void releaseInterceptedContinuation(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<?> continuation);

    /* JADX INFO: compiled from: ContinuationInterceptor.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static <R> R fold(com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor continuationInterceptor, R r, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super R, ? super com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, ? extends R> function2) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "operation");
            return (R) com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element.DefaultImpls.fold(continuationInterceptor, r, function2);
        }

        public static com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext plus(com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor $this, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext context) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
            return com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element.DefaultImpls.plus($this, context);
        }

        public static void releaseInterceptedContinuation(com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor $this, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<?> continuation) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        }

        public static <E extends com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element> E get(com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor continuationInterceptor, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<E> key) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            if (key instanceof com.android.server.permission.jarjar.kotlin.coroutines.AbstractCoroutineContextKey) {
                if (!((com.android.server.permission.jarjar.kotlin.coroutines.AbstractCoroutineContextKey) key).isSubKey$kotlin_stdlib(continuationInterceptor.getKey())) {
                    return null;
                }
                E e = (E) ((com.android.server.permission.jarjar.kotlin.coroutines.AbstractCoroutineContextKey) key).tryCast$kotlin_stdlib(continuationInterceptor);
                if (e instanceof com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element) {
                    return e;
                }
                return null;
            }
            if (com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor.Key != key) {
                return null;
            }
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(continuationInterceptor, "null cannot be cast to non-null type E of kotlin.coroutines.ContinuationInterceptor.get");
            return continuationInterceptor;
        }

        public static com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext minusKey(com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor $this, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> key) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return key instanceof com.android.server.permission.jarjar.kotlin.coroutines.AbstractCoroutineContextKey ? (!((com.android.server.permission.jarjar.kotlin.coroutines.AbstractCoroutineContextKey) key).isSubKey$kotlin_stdlib($this.getKey()) || ((com.android.server.permission.jarjar.kotlin.coroutines.AbstractCoroutineContextKey) key).tryCast$kotlin_stdlib($this) == null) ? $this : com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext.INSTANCE : com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor.Key == key ? com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext.INSTANCE : $this;
        }
    }

    /* JADX INFO: compiled from: ContinuationInterceptor.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Lkotlin/coroutines/ContinuationInterceptor$Key;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlin/coroutines/ContinuationInterceptor;", "()V", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Key implements com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor> {
        static final /* synthetic */ com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor.Key $$INSTANCE = new com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor.Key();

        private Key() {
        }
    }
}
