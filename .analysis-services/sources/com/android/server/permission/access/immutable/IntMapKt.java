package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\u001a\u0018\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0000\u001a/\u0010\u0004\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u0002H\u0002H\u0000¢\u0006\u0002\u0010\b\u001a%\u0010\t\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\n\u001a\u00020\u0006H\u0000¢\u0006\u0002\u0010\u000b\u001a'\u0010\f\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0000¢\u0006\u0002\u0010\u000b\u001a-\u0010\r\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u0002H\u0002H\u0000¢\u0006\u0002\u0010\b¨\u0006\u000e"}, d2 = {"gc", "", "T", "Landroid/util/SparseArray;", "putReturnOld", "key", "", "value", "(Landroid/util/SparseArray;ILjava/lang/Object;)Ljava/lang/Object;", "removeAtReturnOld", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(Landroid/util/SparseArray;I)Ljava/lang/Object;", "removeReturnOld", "setValueAtReturnOld", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IntMapKt {
    public static final <T> T putReturnOld(android.util.SparseArray<T> sparseArray, int key, T t) {
        int index = sparseArray.indexOfKey(key);
        if (index >= 0) {
            T tValueAt = sparseArray.valueAt(index);
            sparseArray.setValueAt(index, t);
            return tValueAt;
        }
        sparseArray.put(key, t);
        return null;
    }

    public static final <T> T removeReturnOld(android.util.SparseArray<T> sparseArray, int key) {
        int index = sparseArray.indexOfKey(key);
        if (index >= 0) {
            T tValueAt = sparseArray.valueAt(index);
            sparseArray.removeAt(index);
            return tValueAt;
        }
        return null;
    }

    public static final <T> T setValueAtReturnOld(android.util.SparseArray<T> sparseArray, int index, T t) {
        T tValueAt = sparseArray.valueAt(index);
        sparseArray.setValueAt(index, t);
        return tValueAt;
    }

    public static final <T> T removeAtReturnOld(android.util.SparseArray<T> sparseArray, int index) {
        T tValueAt = sparseArray.valueAt(index);
        sparseArray.removeAt(index);
        return tValueAt;
    }

    public static final <T> void gc(android.util.SparseArray<T> sparseArray) {
        sparseArray.size();
    }
}
