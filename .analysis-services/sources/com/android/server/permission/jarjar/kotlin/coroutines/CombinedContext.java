package com.android.server.permission.jarjar.kotlin.coroutines;

/* JADX INFO: compiled from: CoroutineContextImpl.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0001\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001!B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u0000H\u0002J\u0013\u0010\f\u001a\u00020\t2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J5\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u00102\u0006\u0010\u0011\u001a\u0002H\u00102\u0018\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u00100\u0013H\u0016¢\u0006\u0002\u0010\u0014J(\u0010\u0015\u001a\u0004\u0018\u0001H\u0016\"\b\b\u0000\u0010\u0016*\u00020\u00062\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0018H\u0096\u0002¢\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u00020\u00012\n\u0010\u0017\u001a\u0006\u0012\u0002\b\u00030\u0018H\u0016J\b\u0010\u001d\u001a\u00020\u001bH\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020\u000eH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lkotlin/coroutines/CombinedContext;", "Lkotlin/coroutines/CoroutineContext;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "left", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/CoroutineContext$Element;)V", "contains", "", "containsAll", "context", "equals", "other", "", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "minusKey", "size", "toString", "", "writeReplace", "Serialized", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CombinedContext implements com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext, java.io.Serializable {
    private final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element element;
    private final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext left;

    public CombinedContext(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext left, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element element) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(left, "left");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
        this.left = left;
        this.element = element;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext plus(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext context) {
        return com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.DefaultImpls.plus(this, context);
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public <E extends com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element> E get(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<E> key) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext combinedContext = this;
        while (true) {
            E e = (E) combinedContext.element.get(key);
            if (e != null) {
                return e;
            }
            com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext coroutineContext = combinedContext.left;
            if (coroutineContext instanceof com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext) {
                combinedContext = (com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext) coroutineContext;
            } else {
                return (E) coroutineContext.get(key);
            }
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super R, ? super com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, ? extends R> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "operation");
        return function2.invoke((java.lang.Object) this.left.fold(r, function2), this.element);
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext minusKey(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Key<?> key) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        if (this.element.get(key) != null) {
            return this.left;
        }
        com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext newLeft = this.left.minusKey(key);
        return newLeft == this.left ? this : newLeft == com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext.INSTANCE ? this.element : new com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext(newLeft, this.element);
    }

    private final int size() {
        com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext cur = this;
        int size = 2;
        while (true) {
            com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext coroutineContext = cur.left;
            com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext combinedContext = coroutineContext instanceof com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext ? (com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext) coroutineContext : null;
            if (combinedContext == null) {
                return size;
            }
            cur = combinedContext;
            size++;
        }
    }

    private final boolean contains(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element element) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(get(element.getKey()), element);
    }

    private final boolean containsAll(com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext context) {
        com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext cur = context;
        while (contains(cur.element)) {
            com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext next = cur.left;
            if (next instanceof com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext) {
                cur = (com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext) next;
            } else {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(next, "null cannot be cast to non-null type kotlin.coroutines.CoroutineContext.Element");
                return contains((com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element) next);
            }
        }
        return false;
    }

    public boolean equals(java.lang.Object other) {
        return this == other || ((other instanceof com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext) && ((com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext) other).size() == size() && ((com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext) other).containsAll(this));
    }

    public int hashCode() {
        return this.left.hashCode() + this.element.hashCode();
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext$toString$1, reason: invalid class name */
    /* JADX INFO: compiled from: CoroutineContextImpl.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "acc", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    static final class AnonymousClass1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.lang.String, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, java.lang.String> {
        public static final com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext.AnonymousClass1 INSTANCE = new com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext.AnonymousClass1();

        AnonymousClass1() {
            super(2);
        }

        @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function2
        public final java.lang.String invoke(java.lang.String acc, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element element) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(acc, "acc");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
            return acc.length() == 0 ? element.toString() : acc + ", " + element;
        }
    }

    public java.lang.String toString() {
        return '[' + ((java.lang.String) fold("", com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext.AnonymousClass1.INSTANCE)) + ']';
    }

    private final java.lang.Object writeReplace() {
        int n = size();
        com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext[] elements = new com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext[n];
        com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.IntRef index = new com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.IntRef();
        fold(com.android.server.permission.jarjar.kotlin.Unit.INSTANCE, new com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext.C00351(elements, index));
        if (!(index.element == n)) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
        return new com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext.Serialized(elements);
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext$writeReplace$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CoroutineContextImpl.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "invoke", "(Lkotlin/Unit;Lkotlin/coroutines/CoroutineContext$Element;)V"}, k = 3, mv = {1, 9, 0}, xi = 48)
    static final class C00351 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<com.android.server.permission.jarjar.kotlin.Unit, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element, com.android.server.permission.jarjar.kotlin.Unit> {
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext[] $elements;
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.IntRef $index;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00351(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext[] coroutineContextArr, com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.IntRef intRef) {
            super(2);
            this.$elements = coroutineContextArr;
            this.$index = intRef;
        }

        @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.Unit invoke(com.android.server.permission.jarjar.kotlin.Unit unit, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element element) {
            invoke2(unit, element);
            return com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(com.android.server.permission.jarjar.kotlin.Unit unit, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element element) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "<anonymous parameter 0>");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
            com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext[] coroutineContextArr = this.$elements;
            int i = this.$index.element;
            this.$index.element = i + 1;
            coroutineContextArr[i] = element;
        }
    }

    /* JADX INFO: compiled from: CoroutineContextImpl.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u0000 \f2\u00060\u0001j\u0002`\u0002:\u0001\fB\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\u0002\u0010\u0006J\b\u0010\n\u001a\u00020\u000bH\u0002R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\b¨\u0006\r"}, d2 = {"Lkotlin/coroutines/CombinedContext$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "elements", "", "Lkotlin/coroutines/CoroutineContext;", "([Lkotlin/coroutines/CoroutineContext;)V", "getElements", "()[Lkotlin/coroutines/CoroutineContext;", "[Lkotlin/coroutines/CoroutineContext;", "readResolve", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Serialized implements java.io.Serializable {
        public static final com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext.Serialized.Companion Companion = new com.android.server.permission.jarjar.kotlin.coroutines.CombinedContext.Serialized.Companion(null);
        private static final long serialVersionUID = 0;
        private final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext[] elements;

        /* JADX INFO: compiled from: CoroutineContextImpl.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlin/coroutines/CombinedContext$Serialized$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        public static final class Companion {
            public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }
        }

        public Serialized(com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext[] elements) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
            this.elements = elements;
        }

        public final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext[] getElements() {
            return this.elements;
        }

        private final java.lang.Object readResolve() {
            com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext[] coroutineContextArr = this.elements;
            java.lang.Object initial$iv = com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
            java.lang.Object accumulator$iv = initial$iv;
            for (com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext coroutineContext : coroutineContextArr) {
                com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext p0 = (com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext) accumulator$iv;
                accumulator$iv = p0.plus(coroutineContext);
            }
            return accumulator$iv;
        }
    }
}
