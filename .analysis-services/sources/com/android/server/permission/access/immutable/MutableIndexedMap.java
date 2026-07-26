package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003B\u001b\b\u0016\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0003¢\u0006\u0002\u0010\u0005B\u001b\u0012\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010\t\u001a\u00020\nJ\u001d\u0010\u000b\u001a\u0004\u0018\u00018\u00012\u0006\u0010\f\u001a\u00028\u00002\u0006\u0010\r\u001a\u00028\u0001¢\u0006\u0002\u0010\u000eJ\u001b\u0010\u000f\u001a\u00028\u00012\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\r\u001a\u00028\u0001¢\u0006\u0002\u0010\u0012J\u0015\u0010\u0013\u001a\u0004\u0018\u00018\u00012\u0006\u0010\f\u001a\u00028\u0000¢\u0006\u0002\u0010\u0014J\u0013\u0010\u0015\u001a\u00028\u00012\u0006\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0016¨\u0006\u0017"}, d2 = {"Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lcom/android/server/permission/access/immutable/IndexedMap;", "indexedMap", "(Lcom/android/server/permission/access/immutable/IndexedMap;)V", "map", "Landroid/util/ArrayMap;", "(Landroid/util/ArrayMap;)V", "clear", "", "put", "key", "value", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "putAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(ILjava/lang/Object;)Ljava/lang/Object;", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", "removeAt", "(I)Ljava/lang/Object;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableIndexedMap<K, V> extends com.android.server.permission.access.immutable.IndexedMap<K, V> {
    /* JADX WARN: Multi-variable type inference failed */
    public MutableIndexedMap() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public MutableIndexedMap(android.util.ArrayMap<K, V> arrayMap) {
        super(arrayMap, null);
    }

    public /* synthetic */ MutableIndexedMap(android.util.ArrayMap arrayMap, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new android.util.ArrayMap() : arrayMap);
    }

    public MutableIndexedMap(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap) {
        this(new android.util.ArrayMap(indexedMap.getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar()));
    }

    public final V put(K k, V v) {
        return getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().put(k, v);
    }

    public final V remove(K k) {
        return getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().remove(k);
    }

    public final void clear() {
        getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clear();
    }

    public final V putAt(int index, V v) {
        return getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().setValueAt(index, v);
    }

    public final V removeAt(int index) {
        return getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().removeAt(index);
    }
}
