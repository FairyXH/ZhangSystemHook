package kotlin.collections;

/* JADX INFO: compiled from: AbstractMap.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010&\n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\b'\u0018\u0000 )*\u0004\b\u0000\u0010\u0001*\u0006\b\u0001\u0010\u0002 \u00012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003:\u0001)B\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u001f\u0010\u0013\u001a\u00020\u00142\u0010\u0010\u0015\u001a\f\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u0016H\u0000¢\u0006\u0002\b\u0017J\u0015\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001aJ\u0015\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\u001aJ\u0013\u0010\u001d\u001a\u00020\u00142\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0096\u0002J\u0018\u0010 \u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0019\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010!J\b\u0010\"\u001a\u00020\rH\u0016J#\u0010#\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u00162\u0006\u0010\u0019\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010$J\b\u0010%\u001a\u00020\u0014H\u0016J\b\u0010&\u001a\u00020'H\u0016J\u0012\u0010&\u001a\u00020'2\b\u0010(\u001a\u0004\u0018\u00010\u001fH\u0002J\u001c\u0010&\u001a\u00020'2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0016H\bR\u0016\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\bX\u0088\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00010\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012¨\u0006*"}, d2 = {"Lkotlin/collections/AbstractMap;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "", "()V", "_keys", "", "_values", "", "keys", "getKeys", "()Ljava/util/Set;", "size", "", "getSize", "()I", "values", "getValues", "()Ljava/util/Collection;", "containsEntry", "", "entry", "", "containsEntry$kotlin_stdlib", "containsKey", "key", "(Ljava/lang/Object;)Z", "containsValue", "value", "equals", "other", "", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "implFindEntry", "(Ljava/lang/Object;)Ljava/util/Map$Entry;", "isEmpty", "toString", "", "o", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractMap<K, V> implements java.util.Map<K, V>, kotlin.jvm.internal.markers.KMappedMarker {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.collections.AbstractMap.Companion INSTANCE = new kotlin.collections.AbstractMap.Companion(null);
    private volatile java.util.Set<? extends K> _keys;
    private volatile java.util.Collection<? extends V> _values;

    @Override // java.util.Map
    public void clear() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public abstract java.util.Set getEntries();

    @Override // java.util.Map
    public V put(K k, V v) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public void putAll(java.util.Map<? extends K, ? extends V> map) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public V remove(java.lang.Object obj) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    protected AbstractMap() {
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Set<java.util.Map.Entry<K, V>> entrySet() {
        return getEntries();
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Set<K> keySet() {
        return getKeys();
    }

    @Override // java.util.Map
    public final /* bridge */ int size() {
        return getSize();
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Collection<V> values() {
        return getValues();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public boolean containsKey(java.lang.Object key) {
        return implFindEntry(key) != null;
    }

    @Override // java.util.Map
    public boolean containsValue(java.lang.Object value) {
        java.lang.Iterable $this$any$iv = entrySet();
        if (($this$any$iv instanceof java.util.Collection) && ((java.util.Collection) $this$any$iv).isEmpty()) {
            return false;
        }
        for (java.lang.Object element$iv : $this$any$iv) {
            java.util.Map.Entry it = (java.util.Map.Entry) element$iv;
            if (kotlin.jvm.internal.Intrinsics.areEqual(it.getValue(), value)) {
                return true;
            }
        }
        return false;
    }

    public final boolean containsEntry$kotlin_stdlib(java.util.Map.Entry<?, ?> entry) {
        if (entry == null) {
            return false;
        }
        java.lang.Object key = entry.getKey();
        java.lang.Object value = entry.getValue();
        kotlin.collections.AbstractMap<K, V> abstractMap = this;
        kotlin.jvm.internal.Intrinsics.checkNotNull(abstractMap, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.get, V of kotlin.collections.MapsKt__MapsKt.get>");
        java.lang.Object ourValue = abstractMap.get(key);
        if (!kotlin.jvm.internal.Intrinsics.areEqual(value, ourValue)) {
            return false;
        }
        if (ourValue != null) {
            return true;
        }
        kotlin.collections.AbstractMap<K, V> abstractMap2 = this;
        kotlin.jvm.internal.Intrinsics.checkNotNull(abstractMap2, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.containsKey, *>");
        if (!abstractMap2.containsKey(key)) {
            return false;
        }
        return true;
    }

    @Override // java.util.Map
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof java.util.Map) || size() != ((java.util.Map) other).size()) {
            return false;
        }
        java.lang.Iterable $this$all$iv = ((java.util.Map) other).entrySet();
        if (($this$all$iv instanceof java.util.Collection) && ((java.util.Collection) $this$all$iv).isEmpty()) {
            return true;
        }
        for (java.lang.Object element$iv : $this$all$iv) {
            if (!containsEntry$kotlin_stdlib((java.util.Map.Entry) element$iv)) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public V get(java.lang.Object key) {
        java.util.Map.Entry<K, V> entryImplFindEntry = implFindEntry(key);
        if (entryImplFindEntry != null) {
            return entryImplFindEntry.getValue();
        }
        return null;
    }

    @Override // java.util.Map
    public int hashCode() {
        return entrySet().hashCode();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    public int getSize() {
        return entrySet().size();
    }

    public java.util.Set<K> getKeys() {
        if (this._keys == null) {
            this._keys = new kotlin.collections.AbstractSet<K>(this) { // from class: kotlin.collections.AbstractMap$keys$1
                final /* synthetic */ kotlin.collections.AbstractMap<K, V> this$0;

                /* JADX WARN: Multi-variable type inference failed */
                {
                    this.this$0 = this;
                }

                @Override // kotlin.collections.AbstractCollection, java.util.Collection
                public boolean contains(java.lang.Object element) {
                    return this.this$0.containsKey(element);
                }

                @Override // kotlin.collections.AbstractSet, kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
                public java.util.Iterator<K> iterator() {
                    java.util.Iterator entryIterator = this.this$0.entrySet().iterator();
                    return new kotlin.collections.AbstractMap$keys$1$iterator$1(entryIterator);
                }

                @Override // kotlin.collections.AbstractCollection
                public int getSize() {
                    return this.this$0.size();
                }
            };
        }
        java.util.Set<? extends K> set = this._keys;
        kotlin.jvm.internal.Intrinsics.checkNotNull(set);
        return set;
    }

    public java.lang.String toString() {
        return kotlin.collections.CollectionsKt.joinToString$default(entrySet(), ", ", "{", "}", 0, null, new kotlin.jvm.functions.Function1<java.util.Map.Entry<? extends K, ? extends V>, java.lang.CharSequence>(this) { // from class: kotlin.collections.AbstractMap.toString.1
            final /* synthetic */ kotlin.collections.AbstractMap<K, V> this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
                this.this$0 = this;
            }

            @Override // kotlin.jvm.functions.Function1
            public final java.lang.CharSequence invoke(java.util.Map.Entry<? extends K, ? extends V> it) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                return this.this$0.toString((java.util.Map.Entry) it);
            }
        }, 24, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.String toString(java.util.Map.Entry<? extends K, ? extends V> entry) {
        return toString(entry.getKey()) + '=' + toString(entry.getValue());
    }

    private final java.lang.String toString(java.lang.Object o) {
        return o == this ? "(this Map)" : java.lang.String.valueOf(o);
    }

    public java.util.Collection<V> getValues() {
        if (this._values == null) {
            this._values = new kotlin.collections.AbstractCollection<V>(this) { // from class: kotlin.collections.AbstractMap.values.1
                final /* synthetic */ kotlin.collections.AbstractMap<K, V> this$0;

                /* JADX WARN: Multi-variable type inference failed */
                {
                    this.this$0 = this;
                }

                @Override // kotlin.collections.AbstractCollection, java.util.Collection
                public boolean contains(java.lang.Object element) {
                    return this.this$0.containsValue(element);
                }

                @Override // kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
                public java.util.Iterator<V> iterator() {
                    return new kotlin.collections.AbstractMap$values$1$iterator$1(this.this$0.entrySet().iterator());
                }

                @Override // kotlin.collections.AbstractCollection
                public int getSize() {
                    return this.this$0.size();
                }
            };
        }
        java.util.Collection<? extends V> collection = this._values;
        kotlin.jvm.internal.Intrinsics.checkNotNull(collection);
        return collection;
    }

    private final java.util.Map.Entry<K, V> implFindEntry(K key) {
        java.lang.Object element$iv;
        java.lang.Iterable $this$firstOrNull$iv = entrySet();
        java.util.Iterator it = $this$firstOrNull$iv.iterator();
        while (true) {
            if (it.hasNext()) {
                element$iv = it.next();
                java.util.Map.Entry it2 = (java.util.Map.Entry) element$iv;
                if (kotlin.jvm.internal.Intrinsics.areEqual(it2.getKey(), key)) {
                    break;
                }
            } else {
                element$iv = null;
                break;
            }
        }
        return (java.util.Map.Entry) element$iv;
    }

    /* JADX INFO: compiled from: AbstractMap.kt */
    @kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010&\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J'\u0010\u0003\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001H\u0000¢\u0006\u0002\b\bJ\u001d\u0010\t\u001a\u00020\n2\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\u000bJ\u001d\u0010\f\u001a\u00020\r2\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\u000e¨\u0006\u000f"}, d2 = {"Lkotlin/collections/AbstractMap$Companion;", "", "()V", "entryEquals", "", "e", "", "other", "entryEquals$kotlin_stdlib", "entryHashCode", "", "entryHashCode$kotlin_stdlib", "entryToString", "", "entryToString$kotlin_stdlib", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int entryHashCode$kotlin_stdlib(java.util.Map.Entry<?, ?> e) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
            java.lang.Object key = e.getKey();
            int iHashCode = key != null ? key.hashCode() : 0;
            java.lang.Object value = e.getValue();
            return iHashCode ^ (value != null ? value.hashCode() : 0);
        }

        public final java.lang.String entryToString$kotlin_stdlib(java.util.Map.Entry<?, ?> e) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
            return new java.lang.StringBuilder().append(e.getKey()).append('=').append(e.getValue()).toString();
        }

        public final boolean entryEquals$kotlin_stdlib(java.util.Map.Entry<?, ?> e, java.lang.Object other) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
            return (other instanceof java.util.Map.Entry) && kotlin.jvm.internal.Intrinsics.areEqual(e.getKey(), ((java.util.Map.Entry) other).getKey()) && kotlin.jvm.internal.Intrinsics.areEqual(e.getValue(), ((java.util.Map.Entry) other).getValue());
        }
    }
}
