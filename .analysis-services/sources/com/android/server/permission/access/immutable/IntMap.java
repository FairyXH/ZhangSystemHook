package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0002\b6\u0018\u0000*\u0004\b\u0000\u0010\u00012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002B\u0015\b\u0004\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005¢\u0006\u0002\u0010\u0006J\u0011\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nH\u0086\u0002J\u0018\u0010\u0010\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u000f\u001a\u00020\nH\u0086\u0002¢\u0006\u0002\u0010\u0011J\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\nJ\u0006\u0010\u0013\u001a\u00020\u000eJ\u000e\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\nJ\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\u0013\u0010\u0019\u001a\u00028\u00002\u0006\u0010\u0015\u001a\u00020\n¢\u0006\u0002\u0010\u0011R\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u0082\u0001\u0001\u0003¨\u0006\u001a"}, d2 = {"Lcom/android/server/permission/access/immutable/IntMap;", "T", "Lcom/android/server/permission/access/immutable/Immutable;", "Lcom/android/server/permission/access/immutable/MutableIntMap;", "array", "Landroid/util/SparseArray;", "(Landroid/util/SparseArray;)V", "getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "()Landroid/util/SparseArray;", "size", "", "getSize", "()I", "contains", "", "key", "get", "(I)Ljava/lang/Object;", "indexOfKey", "isEmpty", "keyAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "toMutable", "toString", "", "valueAt", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class IntMap<T> implements com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.immutable.MutableIntMap<T>> {
    private final android.util.SparseArray<T> array;

    public /* synthetic */ IntMap(android.util.SparseArray sparseArray, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(sparseArray);
    }

    private IntMap(android.util.SparseArray<T> sparseArray) {
        this.array = sparseArray;
    }

    public final android.util.SparseArray<T> getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
        return this.array;
    }

    public final int getSize() {
        return this.array.size();
    }

    public final boolean isEmpty() {
        return this.array.size() == 0;
    }

    public final boolean contains(int key) {
        return this.array.contains(key);
    }

    public final T get(int key) {
        return this.array.get(key);
    }

    public final int indexOfKey(int key) {
        return this.array.indexOfKey(key);
    }

    public final int keyAt(int index) {
        return this.array.keyAt(index);
    }

    public final T valueAt(int index) {
        return this.array.valueAt(index);
    }

    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.immutable.MutableIntMap<T> toMutable() {
        return new com.android.server.permission.access.immutable.MutableIntMap<>(this);
    }

    public java.lang.String toString() {
        return this.array.toString();
    }
}
