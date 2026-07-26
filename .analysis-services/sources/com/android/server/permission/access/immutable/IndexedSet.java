package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedSet.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u000e\n\u0000\b6\u0018\u0000*\u0004\b\u0000\u0010\u00012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002B\u0015\b\u0004\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00028\u0000H\u0086\u0002¢\u0006\u0002\u0010\u0010J\u0013\u0010\u0011\u001a\u00028\u00002\u0006\u0010\u0012\u001a\u00020\n¢\u0006\u0002\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00028\u0000¢\u0006\u0002\u0010\u0015J\u0006\u0010\u0016\u001a\u00020\u000eJ\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016R\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u0082\u0001\u0001\u0003¨\u0006\u001a"}, d2 = {"Lcom/android/server/permission/access/immutable/IndexedSet;", "T", "Lcom/android/server/permission/access/immutable/Immutable;", "Lcom/android/server/permission/access/immutable/MutableIndexedSet;", "set", "Landroid/util/ArraySet;", "(Landroid/util/ArraySet;)V", "getSet$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "()Landroid/util/ArraySet;", "size", "", "getSize", "()I", "contains", "", "element", "(Ljava/lang/Object;)Z", "elementAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Object;", "indexOf", "(Ljava/lang/Object;)I", "isEmpty", "toMutable", "toString", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class IndexedSet<T> implements com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.immutable.MutableIndexedSet<T>> {
    private final android.util.ArraySet<T> set;

    public /* synthetic */ IndexedSet(android.util.ArraySet arraySet, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(arraySet);
    }

    private IndexedSet(android.util.ArraySet<T> arraySet) {
        this.set = arraySet;
    }

    public final android.util.ArraySet<T> getSet$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
        return this.set;
    }

    public final int getSize() {
        return this.set.size();
    }

    public final boolean isEmpty() {
        return this.set.isEmpty();
    }

    public final boolean contains(T t) {
        return this.set.contains(t);
    }

    public final int indexOf(T t) {
        return this.set.indexOf(t);
    }

    public final T elementAt(int i) {
        return (T) com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.elementAt(this.set, i);
    }

    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.immutable.MutableIndexedSet<T> toMutable() {
        return new com.android.server.permission.access.immutable.MutableIndexedSet<>(this);
    }

    public java.lang.String toString() {
        return this.set.toString();
    }
}
