package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: MapWithDefault.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010#\n\u0002\u0010'\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003B<\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005\u0012!\u0010\u0006\u001a\u001d\u0012\u0013\u0012\u00118\u0000¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00028\u00010\u0007¢\u0006\u0002\u0010\u000bJ\b\u0010\u001d\u001a\u00020\u001eH\u0016J\u0015\u0010\u001f\u001a\u00020 2\u0006\u0010\n\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010!J\u0015\u0010\"\u001a\u00020 2\u0006\u0010#\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010!J\u0013\u0010$\u001a\u00020 2\b\u0010%\u001a\u0004\u0018\u00010&H\u0096\u0002J\u0018\u0010'\u001a\u0004\u0018\u00018\u00012\u0006\u0010\n\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010(J\u0015\u0010)\u001a\u00028\u00012\u0006\u0010\n\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010(J\b\u0010*\u001a\u00020\u0016H\u0016J\b\u0010+\u001a\u00020 H\u0016J\u001f\u0010,\u001a\u0004\u0018\u00018\u00012\u0006\u0010\n\u001a\u00028\u00002\u0006\u0010#\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010-J\u001e\u0010.\u001a\u00020\u001e2\u0014\u0010/\u001a\u0010\u0012\u0006\b\u0001\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u000100H\u0016J\u0017\u00101\u001a\u0004\u0018\u00018\u00012\u0006\u0010\n\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010(J\b\u00102\u001a\u000203H\u0016R)\u0010\u0006\u001a\u001d\u0012\u0013\u0012\u00118\u0000¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00028\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R&\u0010\f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u000e0\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R \u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u00168VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00010\u001a8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u001c¨\u00064"}, d2 = {"Lkotlin/collections/MutableMapWithDefaultImpl;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/collections/MutableMapWithDefault;", "map", "", "default", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "key", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)V", "entries", "", "", "getEntries", "()Ljava/util/Set;", "keys", "getKeys", "getMap", "()Ljava/util/Map;", "size", "", "getSize", "()I", "values", "", "getValues", "()Ljava/util/Collection;", "clear", "", "containsKey", "", "(Ljava/lang/Object;)Z", "containsValue", "value", "equals", "other", "", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "getOrImplicitDefault", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "isEmpty", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "putAll", "from", "", "remove", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class MutableMapWithDefaultImpl<K, V> implements com.android.server.permission.jarjar.kotlin.collections.MutableMapWithDefault<K, V> {

    /* JADX INFO: renamed from: default, reason: not valid java name */
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<K, V> f2default;
    private final java.util.Map<K, V> map;

    /* JADX WARN: Multi-variable type inference failed */
    public MutableMapWithDefaultImpl(java.util.Map<K, V> map, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super K, ? extends V> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "default");
        this.map = map;
        this.f2default = function1;
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Set<java.util.Map.Entry<K, V>> entrySet() {
        return getEntries();
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.MutableMapWithDefault, com.android.server.permission.jarjar.kotlin.collections.MapWithDefault
    public java.util.Map<K, V> getMap() {
        return this.map;
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

    @Override // java.util.Map
    public boolean equals(java.lang.Object other) {
        return getMap().equals(other);
    }

    @Override // java.util.Map
    public int hashCode() {
        return getMap().hashCode();
    }

    public java.lang.String toString() {
        return getMap().toString();
    }

    public int getSize() {
        return getMap().size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return getMap().isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(java.lang.Object key) {
        return getMap().containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(java.lang.Object value) {
        return getMap().containsValue(value);
    }

    @Override // java.util.Map
    public V get(java.lang.Object key) {
        return getMap().get(key);
    }

    public java.util.Set<K> getKeys() {
        return getMap().keySet();
    }

    public java.util.Collection<V> getValues() {
        return getMap().values();
    }

    public java.util.Set<java.util.Map.Entry<K, V>> getEntries() {
        return getMap().entrySet();
    }

    @Override // java.util.Map
    public V put(K k, V v) {
        return getMap().put(k, v);
    }

    @Override // java.util.Map
    public V remove(java.lang.Object key) {
        return getMap().remove(key);
    }

    @Override // java.util.Map
    public void putAll(java.util.Map<? extends K, ? extends V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "from");
        getMap().putAll(map);
    }

    @Override // java.util.Map
    public void clear() {
        getMap().clear();
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.MapWithDefault
    public V getOrImplicitDefault(K k) {
        java.util.Map<K, V> map = getMap();
        V v = map.get(k);
        return (v != null || map.containsKey(k)) ? v : this.f2default.invoke(k);
    }
}
