package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedReferenceMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\t\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003*\b\b\u0002\u0010\u0004*\u0002H\u00022\u0014\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u0005B!\b\u0016\u0012\u0018\u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u0005¢\u0006\u0002\u0010\u0007B'\u0012 \b\u0002\u0010\b\u001a\u001a\u0012\u0004\u0012\u00028\u0000\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\n0\t¢\u0006\u0002\u0010\u000bJ\u0006\u0010\f\u001a\u00020\rJ\u0015\u0010\u000e\u001a\u0004\u0018\u00018\u00022\u0006\u0010\u000f\u001a\u00028\u0000¢\u0006\u0002\u0010\u0010J\u0013\u0010\u0011\u001a\u00028\u00022\u0006\u0010\u0012\u001a\u00020\u0013¢\u0006\u0002\u0010\u0014J\u001d\u0010\u0015\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u000f\u001a\u00028\u00002\u0006\u0010\u0016\u001a\u00028\u0002¢\u0006\u0002\u0010\u0017J\u001b\u0010\u0018\u001a\u00028\u00012\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00028\u0002¢\u0006\u0002\u0010\u0019J\u0015\u0010\u001a\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u000f\u001a\u00028\u0000¢\u0006\u0002\u0010\u0010J\u0013\u0010\u001b\u001a\u00028\u00012\u0006\u0010\u0012\u001a\u00020\u0013¢\u0006\u0002\u0010\u0014¨\u0006\u001c"}, d2 = {"Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap;", "K", "I", "Lcom/android/server/permission/access/immutable/Immutable;", "M", "Lcom/android/server/permission/access/immutable/IndexedReferenceMap;", "indexedReferenceMap", "(Lcom/android/server/permission/access/immutable/IndexedReferenceMap;)V", "map", "Landroid/util/ArrayMap;", "Lcom/android/server/permission/access/immutable/MutableReference;", "(Landroid/util/ArrayMap;)V", "clear", "", "mutate", "key", "(Ljava/lang/Object;)Lcom/android/server/permission/access/immutable/Immutable;", "mutateAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(I)Lcom/android/server/permission/access/immutable/Immutable;", "put", "value", "(Ljava/lang/Object;Lcom/android/server/permission/access/immutable/Immutable;)Lcom/android/server/permission/access/immutable/Immutable;", "putAt", "(ILcom/android/server/permission/access/immutable/Immutable;)Lcom/android/server/permission/access/immutable/Immutable;", "remove", "removeAt", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableIndexedReferenceMap<K, I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> extends com.android.server.permission.access.immutable.IndexedReferenceMap<K, I, M> {
    /* JADX WARN: Multi-variable type inference failed */
    public MutableIndexedReferenceMap() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public /* synthetic */ MutableIndexedReferenceMap(android.util.ArrayMap arrayMap, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new android.util.ArrayMap() : arrayMap);
    }

    public MutableIndexedReferenceMap(android.util.ArrayMap<K, com.android.server.permission.access.immutable.MutableReference<I, M>> arrayMap) {
        super(arrayMap, null);
    }

    public MutableIndexedReferenceMap(com.android.server.permission.access.immutable.IndexedReferenceMap<K, I, M> indexedReferenceMap) {
        android.util.ArrayMap $this$_init__u24lambda_u240 = new android.util.ArrayMap(indexedReferenceMap.getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar());
        int size = $this$_init__u24lambda_u240.size();
        for (int i = 0; i < size; i++) {
            $this$_init__u24lambda_u240.setValueAt(i, ((com.android.server.permission.access.immutable.MutableReference) $this$_init__u24lambda_u240.valueAt(i)).toImmutable());
        }
        this($this$_init__u24lambda_u240);
    }

    /* JADX WARN: Incorrect return type in method signature: (TK;)TM; */
    public final com.android.server.permission.access.immutable.Immutable mutate(java.lang.Object key) {
        com.android.server.permission.access.immutable.MutableReference<I, M> mutableReference = getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().get(key);
        if (mutableReference != null) {
            return mutableReference.mutate();
        }
        return null;
    }

    /* JADX WARN: Incorrect types in method signature: (TK;TM;)TI; */
    /* JADX WARN: Multi-variable type inference failed */
    public final com.android.server.permission.access.immutable.Immutable put(java.lang.Object obj, com.android.server.permission.access.immutable.Immutable value) {
        com.android.server.permission.access.immutable.MutableReference<I, M> mutableReferencePut = getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().put(obj, new com.android.server.permission.access.immutable.MutableReference<>(value));
        if (mutableReferencePut != null) {
            return mutableReferencePut.get();
        }
        return null;
    }

    public final I remove(K k) {
        com.android.server.permission.access.immutable.MutableReference<I, M> mutableReferenceRemove = getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().remove(k);
        if (mutableReferenceRemove != null) {
            return (I) mutableReferenceRemove.get();
        }
        return null;
    }

    public final void clear() {
        getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clear();
    }

    /* JADX WARN: Incorrect return type in method signature: (I)TM; */
    public final com.android.server.permission.access.immutable.Immutable mutateAt(int index) {
        return getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().valueAt(index).mutate();
    }

    /* JADX WARN: Incorrect types in method signature: (ITM;)TI; */
    public final com.android.server.permission.access.immutable.Immutable putAt(int index, com.android.server.permission.access.immutable.Immutable value) {
        return getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().setValueAt(index, new com.android.server.permission.access.immutable.MutableReference<>(value)).get();
    }

    public final I removeAt(int i) {
        return (I) getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().removeAt(i).get();
    }
}
