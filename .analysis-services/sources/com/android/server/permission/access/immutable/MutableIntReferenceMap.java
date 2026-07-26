package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntReferenceMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00030\u0002*\b\b\u0001\u0010\u0003*\u0002H\u00012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00030\u0004B\u001b\b\u0016\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0004¢\u0006\u0002\u0010\u0006B!\u0012\u001a\b\u0002\u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\t0\b¢\u0006\u0002\u0010\nJ\u0006\u0010\u000b\u001a\u00020\fJ\u0015\u0010\r\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\u0013\u0010\u0011\u001a\u00028\u00012\u0006\u0010\u0012\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\u001d\u0010\u0013\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00028\u0001¢\u0006\u0002\u0010\u0015J\u001b\u0010\u0016\u001a\u00028\u00002\u0006\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00028\u0001¢\u0006\u0002\u0010\u0015J\u0015\u0010\u0017\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\u0013\u0010\u0018\u001a\u00028\u00002\u0006\u0010\u0012\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010¨\u0006\u0019"}, d2 = {"Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "I", "Lcom/android/server/permission/access/immutable/Immutable;", "M", "Lcom/android/server/permission/access/immutable/IntReferenceMap;", "intReferenceMap", "(Lcom/android/server/permission/access/immutable/IntReferenceMap;)V", "array", "Landroid/util/SparseArray;", "Lcom/android/server/permission/access/immutable/MutableReference;", "(Landroid/util/SparseArray;)V", "clear", "", "mutate", "key", "", "(I)Lcom/android/server/permission/access/immutable/Immutable;", "mutateAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "put", "value", "(ILcom/android/server/permission/access/immutable/Immutable;)Lcom/android/server/permission/access/immutable/Immutable;", "putAt", "remove", "removeAt", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableIntReferenceMap<I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> extends com.android.server.permission.access.immutable.IntReferenceMap<I, M> {
    /* JADX WARN: Multi-variable type inference failed */
    public MutableIntReferenceMap() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public /* synthetic */ MutableIntReferenceMap(android.util.SparseArray sparseArray, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new android.util.SparseArray() : sparseArray);
    }

    public MutableIntReferenceMap(android.util.SparseArray<com.android.server.permission.access.immutable.MutableReference<I, M>> sparseArray) {
        super(sparseArray, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public MutableIntReferenceMap(com.android.server.permission.access.immutable.IntReferenceMap<I, M> intReferenceMap) {
        android.util.SparseArray<com.android.server.permission.access.immutable.MutableReference<I, M>> sparseArrayClone = intReferenceMap.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clone();
        int size = sparseArrayClone.size();
        for (int i = 0; i < size; i++) {
            sparseArrayClone.setValueAt(i, sparseArrayClone.valueAt(i).toImmutable());
        }
        this(sparseArrayClone);
    }

    /* JADX WARN: Incorrect return type in method signature: (I)TM; */
    public final com.android.server.permission.access.immutable.Immutable mutate(int key) {
        com.android.server.permission.access.immutable.MutableReference<I, M> mutableReference = getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().get(key);
        if (mutableReference != null) {
            return mutableReference.mutate();
        }
        return null;
    }

    /* JADX WARN: Incorrect types in method signature: (ITM;)TI; */
    public final com.android.server.permission.access.immutable.Immutable put(int key, com.android.server.permission.access.immutable.Immutable value) {
        com.android.server.permission.access.immutable.MutableReference mutableReference = (com.android.server.permission.access.immutable.MutableReference) com.android.server.permission.access.immutable.IntMapKt.putReturnOld(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(), key, new com.android.server.permission.access.immutable.MutableReference(value));
        if (mutableReference != null) {
            return mutableReference.get();
        }
        return null;
    }

    public final I remove(int i) {
        java.lang.Object objRemoveReturnOld = getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().removeReturnOld(i);
        com.android.server.permission.access.immutable.IntMapKt.gc(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar());
        com.android.server.permission.access.immutable.MutableReference mutableReference = (com.android.server.permission.access.immutable.MutableReference) objRemoveReturnOld;
        if (mutableReference != null) {
            return (I) mutableReference.get();
        }
        return null;
    }

    public final void clear() {
        getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clear();
    }

    /* JADX WARN: Incorrect return type in method signature: (I)TM; */
    public final com.android.server.permission.access.immutable.Immutable mutateAt(int index) {
        return getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().valueAt(index).mutate();
    }

    /* JADX WARN: Incorrect types in method signature: (ITM;)TI; */
    public final com.android.server.permission.access.immutable.Immutable putAt(int index, com.android.server.permission.access.immutable.Immutable value) {
        return ((com.android.server.permission.access.immutable.MutableReference) com.android.server.permission.access.immutable.IntMapKt.setValueAtReturnOld(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(), index, new com.android.server.permission.access.immutable.MutableReference(value))).get();
    }

    public final I removeAt(int i) {
        java.lang.Object objRemoveAtReturnOld = com.android.server.permission.access.immutable.IntMapKt.removeAtReturnOld(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(), i);
        com.android.server.permission.access.immutable.IntMapKt.gc(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar());
        return (I) ((com.android.server.permission.access.immutable.MutableReference) objRemoveAtReturnOld).get();
    }
}
