package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: IndexedValue.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0000¢\u0006\u0002\u0010\u0006J\t\u0010\f\u001a\u00020\u0004HÆ\u0003J\u000e\u0010\r\u001a\u00028\u0000HÆ\u0003¢\u0006\u0002\u0010\nJ(\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00028\u0000HÆ\u0001¢\u0006\u0002\u0010\u000fJ\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0002HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0004HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0005\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\n¨\u0006\u0016"}, d2 = {"Lkotlin/collections/IndexedValue;", "T", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "value", "(ILjava/lang/Object;)V", "getIndex", "()I", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "component2", "copy", "(ILjava/lang/Object;)Lkotlin/collections/IndexedValue;", "equals", "", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class IndexedValue<T> {
    private final int index;
    private final T value;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ com.android.server.permission.jarjar.kotlin.collections.IndexedValue copy$default(com.android.server.permission.jarjar.kotlin.collections.IndexedValue indexedValue, int i, java.lang.Object obj, int i2, java.lang.Object obj2) {
        if ((i2 & 1) != 0) {
            i = indexedValue.index;
        }
        if ((i2 & 2) != 0) {
            obj = indexedValue.value;
        }
        return indexedValue.copy(i, obj);
    }

    public final int component1() {
        return this.index;
    }

    public final T component2() {
        return this.value;
    }

    public final com.android.server.permission.jarjar.kotlin.collections.IndexedValue<T> copy(int i, T t) {
        return new com.android.server.permission.jarjar.kotlin.collections.IndexedValue<>(i, t);
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.permission.jarjar.kotlin.collections.IndexedValue)) {
            return false;
        }
        com.android.server.permission.jarjar.kotlin.collections.IndexedValue indexedValue = (com.android.server.permission.jarjar.kotlin.collections.IndexedValue) obj;
        return this.index == indexedValue.index && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.value, indexedValue.value);
    }

    public int hashCode() {
        return (java.lang.Integer.hashCode(this.index) * 31) + (this.value == null ? 0 : this.value.hashCode());
    }

    public java.lang.String toString() {
        return "IndexedValue(index=" + this.index + ", value=" + this.value + ')';
    }

    public IndexedValue(int index, T t) {
        this.index = index;
        this.value = t;
    }

    public final int getIndex() {
        return this.index;
    }

    public final T getValue() {
        return this.value;
    }
}
