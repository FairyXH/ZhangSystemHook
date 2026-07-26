package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedSet.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\b\u0016\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002¢\u0006\u0002\u0010\u0004B\u0015\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00028\u0000¢\u0006\u0002\u0010\u000bJ\u0006\u0010\f\u001a\u00020\rJ\u0013\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00028\u0000¢\u0006\u0002\u0010\u000bJ\u0013\u0010\u000f\u001a\u00028\u00002\u0006\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012¨\u0006\u0013"}, d2 = {"Lcom/android/server/permission/access/immutable/MutableIndexedSet;", "T", "Lcom/android/server/permission/access/immutable/IndexedSet;", "indexedSet", "(Lcom/android/server/permission/access/immutable/IndexedSet;)V", "set", "Landroid/util/ArraySet;", "(Landroid/util/ArraySet;)V", "add", "", "element", "(Ljava/lang/Object;)Z", "clear", "", "remove", "removeAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(I)Ljava/lang/Object;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableIndexedSet<T> extends com.android.server.permission.access.immutable.IndexedSet<T> {
    /* JADX WARN: Multi-variable type inference failed */
    public MutableIndexedSet() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public MutableIndexedSet(android.util.ArraySet<T> arraySet) {
        super(arraySet, null);
    }

    public /* synthetic */ MutableIndexedSet(android.util.ArraySet arraySet, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new android.util.ArraySet() : arraySet);
    }

    public MutableIndexedSet(com.android.server.permission.access.immutable.IndexedSet<T> indexedSet) {
        this(new android.util.ArraySet((android.util.ArraySet) indexedSet.getSet$frameworks__base__services__permission__android_common__services_permission_pre_jarjar()));
    }

    public final boolean add(T t) {
        return getSet$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().add(t);
    }

    public final boolean remove(T t) {
        return getSet$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().remove(t);
    }

    public final void clear() {
        getSet$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clear();
    }

    public final T removeAt(int index) {
        return getSet$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().removeAt(index);
    }
}
