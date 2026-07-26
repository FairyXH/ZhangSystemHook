package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedList.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\b6\u0018\u0000*\u0004\b\u0000\u0010\u00012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002B\u001f\b\u0004\u0012\u0016\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0005j\b\u0012\u0004\u0012\u00028\u0000`\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00028\u0000H\u0086\u0002¢\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00028\u00002\u0006\u0010\u0013\u001a\u00020\u000bH\u0086\u0002¢\u0006\u0002\u0010\u0014J\u0006\u0010\u0015\u001a\u00020\u000fJ\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R$\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0005j\b\u0012\u0004\u0012\u00028\u0000`\u0006X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b8F¢\u0006\u0006\u001a\u0004\b\f\u0010\r\u0082\u0001\u0001\u0003¨\u0006\u0019"}, d2 = {"Lcom/android/server/permission/access/immutable/IndexedList;", "T", "Lcom/android/server/permission/access/immutable/Immutable;", "Lcom/android/server/permission/access/immutable/MutableIndexedList;", "list", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "(Ljava/util/ArrayList;)V", "getList$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "()Ljava/util/ArrayList;", "size", "", "getSize", "()I", "contains", "", "element", "(Ljava/lang/Object;)Z", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Object;", "isEmpty", "toMutable", "toString", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class IndexedList<T> implements com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.immutable.MutableIndexedList<T>> {
    private final java.util.ArrayList<T> list;

    public /* synthetic */ IndexedList(java.util.ArrayList arrayList, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(arrayList);
    }

    private IndexedList(java.util.ArrayList<T> arrayList) {
        this.list = arrayList;
    }

    public final java.util.ArrayList<T> getList$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
        return this.list;
    }

    public final int getSize() {
        return this.list.size();
    }

    public final boolean isEmpty() {
        return this.list.isEmpty();
    }

    public final boolean contains(T t) {
        return this.list.contains(t);
    }

    public final T get(int index) {
        return this.list.get(index);
    }

    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.immutable.MutableIndexedList<T> toMutable() {
        return new com.android.server.permission.access.immutable.MutableIndexedList<>(this);
    }

    public java.lang.String toString() {
        return this.list.toString();
    }
}
