package kotlin.coroutines;

/* JADX INFO: compiled from: CoroutineContext.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001:\u0002\u0011\u0012J5\u0010\u0002\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u00032\u0018\u0010\u0005\u001a\u0014\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u0002H\u00030\u0006H&¢\u0006\u0002\u0010\bJ(\u0010\t\u001a\u0004\u0018\u0001H\n\"\b\b\u0000\u0010\n*\u00020\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\n0\fH¦\u0002¢\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u00020\u00002\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\fH&J\u0011\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u0000H\u0096\u0002¨\u0006\u0013"}, d2 = {"Lkotlin/coroutines/CoroutineContext;", "", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "Lkotlin/coroutines/CoroutineContext$Element;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusKey", "plus", "context", "Element", "Key", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface CoroutineContext {

    /* JADX INFO: compiled from: CoroutineContext.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\bf\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003¨\u0006\u0004"}, d2 = {"Lkotlin/coroutines/CoroutineContext$Key;", "E", "Lkotlin/coroutines/CoroutineContext$Element;", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface Key<E extends kotlin.coroutines.CoroutineContext.Element> {
    }

    <R> R fold(R initial, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation);

    <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.CoroutineContext.Key<E> key);

    kotlin.coroutines.CoroutineContext minusKey(kotlin.coroutines.CoroutineContext.Key<?> key);

    kotlin.coroutines.CoroutineContext plus(kotlin.coroutines.CoroutineContext context);

    /* JADX INFO: compiled from: CoroutineContext.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static kotlin.coroutines.CoroutineContext plus(kotlin.coroutines.CoroutineContext $this, kotlin.coroutines.CoroutineContext context) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
            return context == kotlin.coroutines.EmptyCoroutineContext.INSTANCE ? $this : (kotlin.coroutines.CoroutineContext) context.fold($this, new kotlin.jvm.functions.Function2<kotlin.coroutines.CoroutineContext, kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext>() { // from class: kotlin.coroutines.CoroutineContext.plus.1
                @Override // kotlin.jvm.functions.Function2
                public final kotlin.coroutines.CoroutineContext invoke(kotlin.coroutines.CoroutineContext acc, kotlin.coroutines.CoroutineContext.Element element) {
                    kotlin.coroutines.CoroutineContext left;
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(acc, "acc");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
                    kotlin.coroutines.CoroutineContext removed = acc.minusKey(element.getKey());
                    if (removed == kotlin.coroutines.EmptyCoroutineContext.INSTANCE) {
                        return element;
                    }
                    kotlin.coroutines.ContinuationInterceptor interceptor = (kotlin.coroutines.ContinuationInterceptor) removed.get(kotlin.coroutines.ContinuationInterceptor.INSTANCE);
                    if (interceptor == null) {
                        left = new kotlin.coroutines.CombinedContext(removed, element);
                    } else {
                        kotlin.coroutines.CoroutineContext left2 = removed.minusKey(kotlin.coroutines.ContinuationInterceptor.INSTANCE);
                        left = left2 == kotlin.coroutines.EmptyCoroutineContext.INSTANCE ? new kotlin.coroutines.CombinedContext(element, interceptor) : new kotlin.coroutines.CombinedContext(new kotlin.coroutines.CombinedContext(left2, element), interceptor);
                    }
                    return left;
                }
            });
        }
    }

    /* JADX INFO: compiled from: CoroutineContext.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J5\u0010\u0006\u001a\u0002H\u0007\"\u0004\b\u0000\u0010\u00072\u0006\u0010\b\u001a\u0002H\u00072\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u0000\u0012\u0004\u0012\u0002H\u00070\nH\u0016¢\u0006\u0002\u0010\u000bJ(\u0010\f\u001a\u0004\u0018\u0001H\r\"\b\b\u0000\u0010\r*\u00020\u00002\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\r0\u0003H\u0096\u0002¢\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u00020\u00012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003H\u0016R\u0016\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u0010"}, d2 = {"Lkotlin/coroutines/CoroutineContext$Element;", "Lkotlin/coroutines/CoroutineContext;", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/CoroutineContext$Key;", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusKey", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface Element extends kotlin.coroutines.CoroutineContext {
        @Override // kotlin.coroutines.CoroutineContext
        <R> R fold(R initial, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation);

        @Override // kotlin.coroutines.CoroutineContext
        <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.CoroutineContext.Key<E> key);

        kotlin.coroutines.CoroutineContext.Key<?> getKey();

        @Override // kotlin.coroutines.CoroutineContext
        kotlin.coroutines.CoroutineContext minusKey(kotlin.coroutines.CoroutineContext.Key<?> key);

        /* JADX INFO: compiled from: CoroutineContext.kt */
        @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
        public static final class DefaultImpls {
            public static kotlin.coroutines.CoroutineContext plus(kotlin.coroutines.CoroutineContext.Element $this, kotlin.coroutines.CoroutineContext context) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
                return kotlin.coroutines.CoroutineContext.DefaultImpls.plus($this, context);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public static <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.CoroutineContext.Element element, kotlin.coroutines.CoroutineContext.Key<E> key) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
                if (!kotlin.jvm.internal.Intrinsics.areEqual(element.getKey(), key)) {
                    return null;
                }
                kotlin.jvm.internal.Intrinsics.checkNotNull(element, "null cannot be cast to non-null type E of kotlin.coroutines.CoroutineContext.Element.get");
                return element;
            }

            public static <R> R fold(kotlin.coroutines.CoroutineContext.Element $this, R r, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
                return operation.invoke(r, $this);
            }

            public static kotlin.coroutines.CoroutineContext minusKey(kotlin.coroutines.CoroutineContext.Element $this, kotlin.coroutines.CoroutineContext.Key<?> key) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
                return kotlin.jvm.internal.Intrinsics.areEqual($this.getKey(), key) ? kotlin.coroutines.EmptyCoroutineContext.INSTANCE : $this;
            }
        }
    }
}
