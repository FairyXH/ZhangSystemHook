package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u000e\n\u0002\b\u0002\b6\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00040\u0003B\u001b\b\u0004\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00028\u0000H\u0086\u0002¢\u0006\u0002\u0010\u0011J\u0018\u0010\u0012\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0010\u001a\u00028\u0000H\u0086\u0002¢\u0006\u0002\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00028\u0000¢\u0006\u0002\u0010\u0015J\u0006\u0010\u0016\u001a\u00020\u000fJ\u0013\u0010\u0017\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u000b¢\u0006\u0002\u0010\u0019J\u0014\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0004H\u0016J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0013\u0010\u001d\u001a\u00028\u00012\u0006\u0010\u0018\u001a\u00020\u000b¢\u0006\u0002\u0010\u0019R \u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0006X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b8F¢\u0006\u0006\u001a\u0004\b\f\u0010\r\u0082\u0001\u0001\u0004¨\u0006\u001e"}, d2 = {"Lcom/android/server/permission/access/immutable/IndexedMap;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lcom/android/server/permission/access/immutable/Immutable;", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "map", "Landroid/util/ArrayMap;", "(Landroid/util/ArrayMap;)V", "getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "()Landroid/util/ArrayMap;", "size", "", "getSize", "()I", "contains", "", "key", "(Ljava/lang/Object;)Z", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "indexOfKey", "(Ljava/lang/Object;)I", "isEmpty", "keyAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Object;", "toMutable", "toString", "", "valueAt", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class IndexedMap<K, V> implements com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.immutable.MutableIndexedMap<K, V>> {
    private final android.util.ArrayMap<K, V> map;

    public /* synthetic */ IndexedMap(android.util.ArrayMap arrayMap, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(arrayMap);
    }

    private IndexedMap(android.util.ArrayMap<K, V> arrayMap) {
        this.map = arrayMap;
    }

    public final android.util.ArrayMap<K, V> getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
        return this.map;
    }

    public final int getSize() {
        return this.map.size();
    }

    public final boolean isEmpty() {
        return this.map.isEmpty();
    }

    public final boolean contains(K k) {
        return this.map.containsKey(k);
    }

    public final V get(K k) {
        return this.map.get(k);
    }

    public final int indexOfKey(K k) {
        return this.map.indexOfKey(k);
    }

    public final K keyAt(int index) {
        return this.map.keyAt(index);
    }

    public final V valueAt(int index) {
        return this.map.valueAt(index);
    }

    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.immutable.MutableIndexedMap<K, V> toMutable() {
        return new com.android.server.permission.access.immutable.MutableIndexedMap<>(this);
    }

    public java.lang.String toString() {
        return this.map.toString();
    }
}
