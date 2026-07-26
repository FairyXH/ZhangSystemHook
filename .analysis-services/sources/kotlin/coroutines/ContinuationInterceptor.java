package kotlin.coroutines;

/* JADX INFO: compiled from: ContinuationInterceptor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bg\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fJ(\u0010\u0002\u001a\u0004\u0018\u0001H\u0003\"\b\b\u0000\u0010\u0003*\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0005H\u0096\u0002¢\u0006\u0002\u0010\u0006J\"\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\bH&J\u0014\u0010\u000b\u001a\u00020\f2\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\u0014\u0010\r\u001a\u00020\u000e2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\bH\u0016¨\u0006\u0010"}, d2 = {"Lkotlin/coroutines/ContinuationInterceptor;", "Lkotlin/coroutines/CoroutineContext$Element;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "interceptContinuation", "Lkotlin/coroutines/Continuation;", "T", "continuation", "minusKey", "Lkotlin/coroutines/CoroutineContext;", "releaseInterceptedContinuation", "", "Key", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ContinuationInterceptor extends kotlin.coroutines.CoroutineContext.Element {

    /* JADX INFO: renamed from: Key, reason: from kotlin metadata */
    public static final kotlin.coroutines.ContinuationInterceptor.Companion INSTANCE = kotlin.coroutines.ContinuationInterceptor.Companion.$$INSTANCE;

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.CoroutineContext.Key<E> key);

    <T> kotlin.coroutines.Continuation<T> interceptContinuation(kotlin.coroutines.Continuation<? super T> continuation);

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    kotlin.coroutines.CoroutineContext minusKey(kotlin.coroutines.CoroutineContext.Key<?> key);

    void releaseInterceptedContinuation(kotlin.coroutines.Continuation<?> continuation);

    /* JADX INFO: compiled from: ContinuationInterceptor.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static <R> R fold(kotlin.coroutines.ContinuationInterceptor continuationInterceptor, R r, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
            return (R) kotlin.coroutines.CoroutineContext.Element.DefaultImpls.fold(continuationInterceptor, r, operation);
        }

        public static kotlin.coroutines.CoroutineContext plus(kotlin.coroutines.ContinuationInterceptor $this, kotlin.coroutines.CoroutineContext context) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
            return kotlin.coroutines.CoroutineContext.Element.DefaultImpls.plus($this, context);
        }

        public static void releaseInterceptedContinuation(kotlin.coroutines.ContinuationInterceptor $this, kotlin.coroutines.Continuation<?> continuation) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        }

        public static <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.ContinuationInterceptor continuationInterceptor, kotlin.coroutines.CoroutineContext.Key<E> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            if (key instanceof kotlin.coroutines.AbstractCoroutineContextKey) {
                if (!((kotlin.coroutines.AbstractCoroutineContextKey) key).isSubKey$kotlin_stdlib(continuationInterceptor.getKey())) {
                    return null;
                }
                E e = (E) ((kotlin.coroutines.AbstractCoroutineContextKey) key).tryCast$kotlin_stdlib(continuationInterceptor);
                if (e instanceof kotlin.coroutines.CoroutineContext.Element) {
                    return e;
                }
                return null;
            }
            if (kotlin.coroutines.ContinuationInterceptor.INSTANCE != key) {
                return null;
            }
            kotlin.jvm.internal.Intrinsics.checkNotNull(continuationInterceptor, "null cannot be cast to non-null type E of kotlin.coroutines.ContinuationInterceptor.get");
            return continuationInterceptor;
        }

        public static kotlin.coroutines.CoroutineContext minusKey(kotlin.coroutines.ContinuationInterceptor $this, kotlin.coroutines.CoroutineContext.Key<?> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return key instanceof kotlin.coroutines.AbstractCoroutineContextKey ? (!((kotlin.coroutines.AbstractCoroutineContextKey) key).isSubKey$kotlin_stdlib($this.getKey()) || ((kotlin.coroutines.AbstractCoroutineContextKey) key).tryCast$kotlin_stdlib($this) == null) ? $this : kotlin.coroutines.EmptyCoroutineContext.INSTANCE : kotlin.coroutines.ContinuationInterceptor.INSTANCE == key ? kotlin.coroutines.EmptyCoroutineContext.INSTANCE : $this;
        }
    }

    /* JADX INFO: renamed from: kotlin.coroutines.ContinuationInterceptor$Key, reason: from kotlin metadata */
    /* JADX INFO: compiled from: ContinuationInterceptor.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Lkotlin/coroutines/ContinuationInterceptor$Key;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlin/coroutines/ContinuationInterceptor;", "()V", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion implements kotlin.coroutines.CoroutineContext.Key<kotlin.coroutines.ContinuationInterceptor> {
        static final /* synthetic */ kotlin.coroutines.ContinuationInterceptor.Companion $$INSTANCE = new kotlin.coroutines.ContinuationInterceptor.Companion();

        private Companion() {
        }
    }
}
