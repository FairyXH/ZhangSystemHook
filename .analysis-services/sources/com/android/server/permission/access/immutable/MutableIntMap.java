package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\b\u0016\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002¢\u0006\u0002\u0010\u0004B\u0015\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\u0006\u0010\b\u001a\u00020\tJ\u001d\u0010\n\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00028\u0000¢\u0006\u0002\u0010\u000eJ\u001b\u0010\u000f\u001a\u00028\u00002\u0006\u0010\u0010\u001a\u00020\f2\u0006\u0010\r\u001a\u00028\u0000¢\u0006\u0002\u0010\u000eJ\u0015\u0010\u0011\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\u0012J\u0013\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0010\u001a\u00020\f¢\u0006\u0002\u0010\u0012¨\u0006\u0014"}, d2 = {"Lcom/android/server/permission/access/immutable/MutableIntMap;", "T", "Lcom/android/server/permission/access/immutable/IntMap;", "intMap", "(Lcom/android/server/permission/access/immutable/IntMap;)V", "array", "Landroid/util/SparseArray;", "(Landroid/util/SparseArray;)V", "clear", "", "put", "key", "", "value", "(ILjava/lang/Object;)Ljava/lang/Object;", "putAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "remove", "(I)Ljava/lang/Object;", "removeAt", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableIntMap<T> extends com.android.server.permission.access.immutable.IntMap<T> {
    /* JADX WARN: Multi-variable type inference failed */
    public MutableIntMap() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public MutableIntMap(android.util.SparseArray<T> sparseArray) {
        super(sparseArray, null);
    }

    public /* synthetic */ MutableIntMap(android.util.SparseArray sparseArray, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new android.util.SparseArray() : sparseArray);
    }

    public MutableIntMap(com.android.server.permission.access.immutable.IntMap<T> intMap) {
        this(intMap.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clone());
    }

    public final T put(int i, T t) {
        return (T) com.android.server.permission.access.immutable.IntMapKt.putReturnOld(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(), i, t);
    }

    public final T remove(int i) {
        T t = (T) getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().removeReturnOld(i);
        com.android.server.permission.access.immutable.IntMapKt.gc(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar());
        return t;
    }

    public final void clear() {
        getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().clear();
    }

    public final T putAt(int i, T t) {
        return (T) com.android.server.permission.access.immutable.IntMapKt.setValueAtReturnOld(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(), i, t);
    }

    public final T removeAt(int i) {
        T t = (T) com.android.server.permission.access.immutable.IntMapKt.removeAtReturnOld(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(), i);
        com.android.server.permission.access.immutable.IntMapKt.gc(getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar());
        return t;
    }
}
