package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntReferenceMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0002\b6\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00030\u0002*\b\b\u0001\u0010\u0003*\u0002H\u00012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00030\u00040\u0002B!\b\u0004\u0012\u0018\u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00070\u0006¢\u0006\u0002\u0010\bJ\u0011\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\fH\u0086\u0002J\u0018\u0010\u0012\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u0011\u001a\u00020\fH\u0086\u0002¢\u0006\u0002\u0010\u0013J\u000e\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\fJ\u0006\u0010\u0015\u001a\u00020\u0010J\u000e\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\fJ\u0014\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0004H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0013\u0010\u001b\u001a\u00028\u00002\u0006\u0010\u0017\u001a\u00020\f¢\u0006\u0002\u0010\u0013R&\u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00070\u0006X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e\u0082\u0001\u0001\u0004¨\u0006\u001c"}, d2 = {"Lcom/android/server/permission/access/immutable/IntReferenceMap;", "I", "Lcom/android/server/permission/access/immutable/Immutable;", "M", "Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "array", "Landroid/util/SparseArray;", "Lcom/android/server/permission/access/immutable/MutableReference;", "(Landroid/util/SparseArray;)V", "getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "()Landroid/util/SparseArray;", "size", "", "getSize", "()I", "contains", "", "key", "get", "(I)Lcom/android/server/permission/access/immutable/Immutable;", "indexOfKey", "isEmpty", "keyAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "toMutable", "toString", "", "valueAt", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class IntReferenceMap<I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> implements com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.immutable.MutableIntReferenceMap<I, M>> {
    private final android.util.SparseArray<com.android.server.permission.access.immutable.MutableReference<I, M>> array;

    public /* synthetic */ IntReferenceMap(android.util.SparseArray sparseArray, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(sparseArray);
    }

    private IntReferenceMap(android.util.SparseArray<com.android.server.permission.access.immutable.MutableReference<I, M>> sparseArray) {
        this.array = sparseArray;
    }

    public final android.util.SparseArray<com.android.server.permission.access.immutable.MutableReference<I, M>> getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
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

    public final I get(int i) {
        com.android.server.permission.access.immutable.MutableReference<I, M> mutableReference = this.array.get(i);
        if (mutableReference != null) {
            return (I) mutableReference.get();
        }
        return null;
    }

    public final int indexOfKey(int key) {
        return this.array.indexOfKey(key);
    }

    public final int keyAt(int index) {
        return this.array.keyAt(index);
    }

    public final I valueAt(int i) {
        return (I) this.array.valueAt(i).get();
    }

    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.immutable.MutableIntReferenceMap<I, M> toMutable() {
        return new com.android.server.permission.access.immutable.MutableIntReferenceMap<>(this);
    }

    public java.lang.String toString() {
        return this.array.toString();
    }
}
