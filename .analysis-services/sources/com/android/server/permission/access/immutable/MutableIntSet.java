package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntSet.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003B\u000f\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\n¨\u0006\u0010"}, d2 = {"Lcom/android/server/permission/access/immutable/MutableIntSet;", "Lcom/android/server/permission/access/immutable/IntSet;", "intSet", "(Lcom/android/server/permission/access/immutable/IntSet;)V", "array", "Landroid/util/SparseBooleanArray;", "(Landroid/util/SparseBooleanArray;)V", "add", "", "element", "", "clear", "", "remove", "removeAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableIntSet extends com.android.server.permission.access.immutable.IntSet {
    /* JADX WARN: Multi-variable type inference failed */
    public MutableIntSet() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public MutableIntSet(android.util.SparseBooleanArray array) {
        super(array, null);
    }

    public /* synthetic */ MutableIntSet(android.util.SparseBooleanArray sparseBooleanArray, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new android.util.SparseBooleanArray() : sparseBooleanArray);
    }

    public MutableIntSet(com.android.server.permission.access.immutable.IntSet intSet) {
        this(intSet.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clone());
    }

    public final boolean add(int element) {
        if (com.android.server.permission.access.immutable.IntSetKt.contains(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(), element)) {
            return false;
        }
        getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().put(element, true);
        return true;
    }

    public final boolean remove(int element) {
        int index = getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().indexOfKey(element);
        if (index >= 0) {
            getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().removeAt(index);
            return true;
        }
        return false;
    }

    public final void clear() {
        getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clear();
    }

    public final void removeAt(int index) {
        getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().removeAt(index);
    }
}
