package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedListSet.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\b\u0016\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002¢\u0006\u0002\u0010\u0004B\u001f\u0012\u0018\b\u0002\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0006j\b\u0012\u0004\u0012\u00028\u0000`\u0007¢\u0006\u0002\u0010\bJ\u0013\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0000¢\u0006\u0002\u0010\fJ\u0006\u0010\r\u001a\u00020\u000eJ\u0013\u0010\u000f\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0000¢\u0006\u0002\u0010\fJ\u0013\u0010\u0010\u001a\u00028\u00002\u0006\u0010\u0011\u001a\u00020\u0012¢\u0006\u0002\u0010\u0013¨\u0006\u0014"}, d2 = {"Lcom/android/server/permission/access/immutable/MutableIndexedListSet;", "T", "Lcom/android/server/permission/access/immutable/IndexedListSet;", "indexedListSet", "(Lcom/android/server/permission/access/immutable/IndexedListSet;)V", "list", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "(Ljava/util/ArrayList;)V", "add", "", "element", "(Ljava/lang/Object;)Z", "clear", "", "remove", "removeAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(I)Ljava/lang/Object;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableIndexedListSet<T> extends com.android.server.permission.access.immutable.IndexedListSet<T> {
    /* JADX WARN: Multi-variable type inference failed */
    public MutableIndexedListSet() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public MutableIndexedListSet(java.util.ArrayList<T> arrayList) {
        super(arrayList, null);
    }

    public /* synthetic */ MutableIndexedListSet(java.util.ArrayList arrayList, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new java.util.ArrayList() : arrayList);
    }

    public MutableIndexedListSet(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet) {
        this(new java.util.ArrayList(indexedListSet.getList$frameworks__base__services__permission__android_common__services_permission_pre_jarjar()));
    }

    public final boolean add(T t) {
        if (getList$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().contains(t)) {
            return false;
        }
        getList$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().add(t);
        return true;
    }

    public final boolean remove(T t) {
        return getList$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().remove(t);
    }

    public final void clear() {
        getList$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clear();
    }

    public final T removeAt(int index) {
        return getList$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().remove(index);
    }
}
