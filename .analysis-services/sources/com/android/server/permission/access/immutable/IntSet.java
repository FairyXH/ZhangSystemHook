package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntSet.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\b6\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\b\u0004\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0011\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tH\u0086\u0002J\u000e\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\tJ\u000e\u0010\u0011\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\tJ\u0006\u0010\u0012\u001a\u00020\rJ\b\u0010\u0013\u001a\u00020\u0002H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016R\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b\u0082\u0001\u0001\u0002¨\u0006\u0016"}, d2 = {"Lcom/android/server/permission/access/immutable/IntSet;", "Lcom/android/server/permission/access/immutable/Immutable;", "Lcom/android/server/permission/access/immutable/MutableIntSet;", "array", "Landroid/util/SparseBooleanArray;", "(Landroid/util/SparseBooleanArray;)V", "getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "()Landroid/util/SparseBooleanArray;", "size", "", "getSize", "()I", "contains", "", "element", "elementAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "indexOf", "isEmpty", "toMutable", "toString", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class IntSet implements com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.immutable.MutableIntSet> {
    private final android.util.SparseBooleanArray array;

    public /* synthetic */ IntSet(android.util.SparseBooleanArray sparseBooleanArray, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(sparseBooleanArray);
    }

    private IntSet(android.util.SparseBooleanArray array) {
        this.array = array;
    }

    public final android.util.SparseBooleanArray getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
        return this.array;
    }

    public final int getSize() {
        return this.array.size();
    }

    public final boolean isEmpty() {
        return this.array.size() == 0;
    }

    public final boolean contains(int element) {
        return com.android.server.permission.access.immutable.IntSetKt.contains(this.array, element);
    }

    public final int indexOf(int element) {
        return this.array.indexOfKey(element);
    }

    public final int elementAt(int index) {
        return this.array.keyAt(index);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.immutable.MutableIntSet toMutable() {
        return new com.android.server.permission.access.immutable.MutableIntSet(this);
    }

    public java.lang.String toString() {
        return this.array.toString();
    }
}
