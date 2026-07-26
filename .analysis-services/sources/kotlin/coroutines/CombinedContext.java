package kotlin.coroutines;

/* JADX INFO: compiled from: CoroutineContextImpl.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0001\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001!B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u0000H\u0002J\u0013\u0010\f\u001a\u00020\t2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J5\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u00102\u0006\u0010\u0011\u001a\u0002H\u00102\u0018\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u00100\u0013H\u0016¢\u0006\u0002\u0010\u0014J(\u0010\u0015\u001a\u0004\u0018\u0001H\u0016\"\b\b\u0000\u0010\u0016*\u00020\u00062\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0018H\u0096\u0002¢\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u00020\u00012\n\u0010\u0017\u001a\u0006\u0012\u0002\b\u00030\u0018H\u0016J\b\u0010\u001d\u001a\u00020\u001bH\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020\u000eH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lkotlin/coroutines/CombinedContext;", "Lkotlin/coroutines/CoroutineContext;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "left", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/CoroutineContext$Element;)V", "contains", "", "containsAll", "context", "equals", "other", "", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "minusKey", "size", "toString", "", "writeReplace", "Serialized", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CombinedContext implements kotlin.coroutines.CoroutineContext, java.io.Serializable {
    private final kotlin.coroutines.CoroutineContext.Element element;
    private final kotlin.coroutines.CoroutineContext left;

    public CombinedContext(kotlin.coroutines.CoroutineContext left, kotlin.coroutines.CoroutineContext.Element element) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(left, "left");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
        this.left = left;
        this.element = element;
    }

    @Override // kotlin.coroutines.CoroutineContext
    public kotlin.coroutines.CoroutineContext plus(kotlin.coroutines.CoroutineContext context) {
        return kotlin.coroutines.CoroutineContext.DefaultImpls.plus(this, context);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.CoroutineContext.Key<E> key) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        kotlin.coroutines.CombinedContext combinedContext = this;
        while (true) {
            E e = (E) combinedContext.element.get(key);
            if (e != null) {
                return e;
            }
            kotlin.coroutines.CoroutineContext coroutineContext = combinedContext.left;
            if (coroutineContext instanceof kotlin.coroutines.CombinedContext) {
                combinedContext = (kotlin.coroutines.CombinedContext) coroutineContext;
            } else {
                return (E) coroutineContext.get(key);
            }
        }
    }

    @Override // kotlin.coroutines.CoroutineContext
    public <R> R fold(R initial, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
        return operation.invoke((java.lang.Object) this.left.fold(initial, operation), this.element);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public kotlin.coroutines.CoroutineContext minusKey(kotlin.coroutines.CoroutineContext.Key<?> key) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        if (this.element.get(key) != null) {
            return this.left;
        }
        kotlin.coroutines.CoroutineContext newLeft = this.left.minusKey(key);
        return newLeft == this.left ? this : newLeft == kotlin.coroutines.EmptyCoroutineContext.INSTANCE ? this.element : new kotlin.coroutines.CombinedContext(newLeft, this.element);
    }

    private final int size() {
        kotlin.coroutines.CombinedContext cur = this;
        int size = 2;
        while (true) {
            kotlin.coroutines.CoroutineContext coroutineContext = cur.left;
            kotlin.coroutines.CombinedContext combinedContext = coroutineContext instanceof kotlin.coroutines.CombinedContext ? (kotlin.coroutines.CombinedContext) coroutineContext : null;
            if (combinedContext == null) {
                return size;
            }
            cur = combinedContext;
            size++;
        }
    }

    private final boolean contains(kotlin.coroutines.CoroutineContext.Element element) {
        return kotlin.jvm.internal.Intrinsics.areEqual(get(element.getKey()), element);
    }

    private final boolean containsAll(kotlin.coroutines.CombinedContext context) {
        kotlin.coroutines.CombinedContext cur = context;
        while (contains(cur.element)) {
            kotlin.coroutines.CoroutineContext next = cur.left;
            if (next instanceof kotlin.coroutines.CombinedContext) {
                cur = (kotlin.coroutines.CombinedContext) next;
            } else {
                kotlin.jvm.internal.Intrinsics.checkNotNull(next, "null cannot be cast to non-null type kotlin.coroutines.CoroutineContext.Element");
                return contains((kotlin.coroutines.CoroutineContext.Element) next);
            }
        }
        return false;
    }

    public boolean equals(java.lang.Object other) {
        return this == other || ((other instanceof kotlin.coroutines.CombinedContext) && ((kotlin.coroutines.CombinedContext) other).size() == size() && ((kotlin.coroutines.CombinedContext) other).containsAll(this));
    }

    public int hashCode() {
        return this.left.hashCode() + this.element.hashCode();
    }

    public java.lang.String toString() {
        return '[' + ((java.lang.String) fold("", new kotlin.jvm.functions.Function2<java.lang.String, kotlin.coroutines.CoroutineContext.Element, java.lang.String>() { // from class: kotlin.coroutines.CombinedContext.toString.1
            @Override // kotlin.jvm.functions.Function2
            public final java.lang.String invoke(java.lang.String acc, kotlin.coroutines.CoroutineContext.Element element) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(acc, "acc");
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
                return acc.length() == 0 ? element.toString() : acc + ", " + element;
            }
        })) + ']';
    }

    private final java.lang.Object writeReplace() {
        int n = size();
        final kotlin.coroutines.CoroutineContext[] elements = new kotlin.coroutines.CoroutineContext[n];
        final kotlin.jvm.internal.Ref.IntRef index = new kotlin.jvm.internal.Ref.IntRef();
        fold(kotlin.Unit.INSTANCE, new kotlin.jvm.functions.Function2<kotlin.Unit, kotlin.coroutines.CoroutineContext.Element, kotlin.Unit>() { // from class: kotlin.coroutines.CombinedContext.writeReplace.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ kotlin.Unit invoke(kotlin.Unit unit, kotlin.coroutines.CoroutineContext.Element element) {
                invoke2(unit, element);
                return kotlin.Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(kotlin.Unit unit, kotlin.coroutines.CoroutineContext.Element element) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "<anonymous parameter 0>");
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
                kotlin.coroutines.CoroutineContext[] coroutineContextArr = elements;
                int i = index.element;
                index.element = i + 1;
                coroutineContextArr[i] = element;
            }
        });
        if (!(index.element == n)) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
        return new kotlin.coroutines.CombinedContext.Serialized(elements);
    }

    /* JADX INFO: compiled from: CoroutineContextImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u0000 \f2\u00060\u0001j\u0002`\u0002:\u0001\fB\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\u0002\u0010\u0006J\b\u0010\n\u001a\u00020\u000bH\u0002R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\b¨\u0006\r"}, d2 = {"Lkotlin/coroutines/CombinedContext$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "elements", "", "Lkotlin/coroutines/CoroutineContext;", "([Lkotlin/coroutines/CoroutineContext;)V", "getElements", "()[Lkotlin/coroutines/CoroutineContext;", "[Lkotlin/coroutines/CoroutineContext;", "readResolve", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Serialized implements java.io.Serializable {
        private static final long serialVersionUID = 0;
        private final kotlin.coroutines.CoroutineContext[] elements;

        public Serialized(kotlin.coroutines.CoroutineContext[] elements) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
            this.elements = elements;
        }

        public final kotlin.coroutines.CoroutineContext[] getElements() {
            return this.elements;
        }

        private final java.lang.Object readResolve() {
            kotlin.coroutines.CoroutineContext[] coroutineContextArr = this.elements;
            java.lang.Object initial$iv = kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
            java.lang.Object accumulator$iv = initial$iv;
            for (kotlin.coroutines.CoroutineContext coroutineContext : coroutineContextArr) {
                kotlin.coroutines.CoroutineContext p0 = (kotlin.coroutines.CoroutineContext) accumulator$iv;
                accumulator$iv = p0.plus(coroutineContext);
            }
            return accumulator$iv;
        }
    }
}
