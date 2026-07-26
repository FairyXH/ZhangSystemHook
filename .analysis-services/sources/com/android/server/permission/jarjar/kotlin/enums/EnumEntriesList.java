package com.android.server.permission.jarjar.kotlin.enums;

/* JADX INFO: compiled from: EnumEntries.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0000\b\u0003\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u0002H\u00010\u00042\u00060\u0005j\u0002`\u0006B\u0013\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b¢\u0006\u0002\u0010\tJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00020\fH\u0096\u0002¢\u0006\u0002\u0010\u0015J\u0015\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0017J\u0015\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0017J\b\u0010\u0019\u001a\u00020\u001aH\u0002R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\nR\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u0006\u001b"}, d2 = {"Lkotlin/enums/EnumEntriesList;", "T", "", "Lkotlin/enums/EnumEntries;", "Lkotlin/collections/AbstractList;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "entries", "", "([Ljava/lang/Enum;)V", "[Ljava/lang/Enum;", "size", "", "getSize", "()I", "contains", "", "element", "(Ljava/lang/Enum;)Z", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Enum;", "indexOf", "(Ljava/lang/Enum;)I", "lastIndexOf", "writeReplace", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class EnumEntriesList<T extends java.lang.Enum<T>> extends com.android.server.permission.jarjar.kotlin.collections.AbstractList<T> implements com.android.server.permission.jarjar.kotlin.enums.EnumEntries<T>, java.io.Serializable {
    private final T[] entries;

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection
    public final /* bridge */ boolean contains(java.lang.Object element) {
        if (element instanceof java.lang.Enum) {
            return contains((java.lang.Enum) element);
        }
        return false;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int indexOf(java.lang.Object element) {
        if (element instanceof java.lang.Enum) {
            return indexOf((java.lang.Enum) element);
        }
        return -1;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int lastIndexOf(java.lang.Object element) {
        if (element instanceof java.lang.Enum) {
            return lastIndexOf((java.lang.Enum) element);
        }
        return -1;
    }

    public EnumEntriesList(T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "entries");
        this.entries = tArr;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, com.android.server.permission.jarjar.kotlin.collections.AbstractCollection
    public int getSize() {
        return this.entries.length;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public T get(int index) {
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, this.entries.length);
        return this.entries[index];
    }

    public boolean contains(T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "element");
        java.lang.Enum target = (java.lang.Enum) com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getOrNull(this.entries, t.ordinal());
        return target == t;
    }

    public int indexOf(T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "element");
        int ordinal = t.ordinal();
        java.lang.Enum target = (java.lang.Enum) com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getOrNull(this.entries, ordinal);
        if (target == t) {
            return ordinal;
        }
        return -1;
    }

    public int lastIndexOf(T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "element");
        return indexOf((java.lang.Object) t);
    }

    private final java.lang.Object writeReplace() {
        return new com.android.server.permission.jarjar.kotlin.enums.EnumEntriesSerializationProxy(this.entries);
    }
}
