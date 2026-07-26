package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedReferenceMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u000e\n\u0002\b\u0003\b6\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003*\b\b\u0002\u0010\u0004*\u0002H\u00022\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00050\u0003B'\b\u0004\u0012\u001e\u0010\u0006\u001a\u001a\u0012\u0004\u0012\u00028\u0000\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\b0\u0007¢\u0006\u0002\u0010\tJ\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00028\u0000H\u0086\u0002¢\u0006\u0002\u0010\u0013J\u0018\u0010\u0014\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0012\u001a\u00028\u0000H\u0086\u0002¢\u0006\u0002\u0010\u0015J\u0013\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00028\u0000¢\u0006\u0002\u0010\u0017J\u0006\u0010\u0018\u001a\u00020\u0011J\u0013\u0010\u0019\u001a\u00028\u00002\u0006\u0010\u001a\u001a\u00020\r¢\u0006\u0002\u0010\u001bJ\u001a\u0010\u001c\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u0005H\u0016J\b\u0010\u001d\u001a\u00020\u001eH\u0016J\u0013\u0010\u001f\u001a\u00028\u00012\u0006\u0010\u001a\u001a\u00020\r¢\u0006\u0002\u0010 R,\u0010\u0006\u001a\u001a\u0012\u0004\u0012\u00028\u0000\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\b0\u0007X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f\u0082\u0001\u0001\u0005¨\u0006!"}, d2 = {"Lcom/android/server/permission/access/immutable/IndexedReferenceMap;", "K", "I", "Lcom/android/server/permission/access/immutable/Immutable;", "M", "Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap;", "map", "Landroid/util/ArrayMap;", "Lcom/android/server/permission/access/immutable/MutableReference;", "(Landroid/util/ArrayMap;)V", "getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "()Landroid/util/ArrayMap;", "size", "", "getSize", "()I", "contains", "", "key", "(Ljava/lang/Object;)Z", "get", "(Ljava/lang/Object;)Lcom/android/server/permission/access/immutable/Immutable;", "indexOfKey", "(Ljava/lang/Object;)I", "isEmpty", "keyAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Object;", "toMutable", "toString", "", "valueAt", "(I)Lcom/android/server/permission/access/immutable/Immutable;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class IndexedReferenceMap<K, I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> implements com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.immutable.MutableIndexedReferenceMap<K, I, M>> {
    private final android.util.ArrayMap<K, com.android.server.permission.access.immutable.MutableReference<I, M>> map;

    public /* synthetic */ IndexedReferenceMap(android.util.ArrayMap arrayMap, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(arrayMap);
    }

    private IndexedReferenceMap(android.util.ArrayMap<K, com.android.server.permission.access.immutable.MutableReference<I, M>> arrayMap) {
        this.map = arrayMap;
    }

    public final android.util.ArrayMap<K, com.android.server.permission.access.immutable.MutableReference<I, M>> getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
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

    public final I get(K k) {
        com.android.server.permission.access.immutable.MutableReference<I, M> mutableReference = this.map.get(k);
        if (mutableReference != null) {
            return (I) mutableReference.get();
        }
        return null;
    }

    public final int indexOfKey(K k) {
        return this.map.indexOfKey(k);
    }

    public final K keyAt(int index) {
        return this.map.keyAt(index);
    }

    public final I valueAt(int i) {
        return (I) this.map.valueAt(i).get();
    }

    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.immutable.MutableIndexedReferenceMap<K, I, M> toMutable() {
        return new com.android.server.permission.access.immutable.MutableIndexedReferenceMap<>(this);
    }

    public java.lang.String toString() {
        return this.map.toString();
    }
}
