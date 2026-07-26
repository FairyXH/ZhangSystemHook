package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: AbstractSet.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b'\u0018\u0000 \u000b*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0001\u000bB\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0096\u0002J\b\u0010\t\u001a\u00020\nH\u0016¨\u0006\f"}, d2 = {"Lkotlin/collections/AbstractSet;", "E", "Lkotlin/collections/AbstractCollection;", "", "()V", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractSet<E> extends com.android.server.permission.jarjar.kotlin.collections.AbstractCollection<E> implements java.util.Set<E>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    public static final com.android.server.permission.jarjar.kotlin.collections.AbstractSet.Companion Companion = new com.android.server.permission.jarjar.kotlin.collections.AbstractSet.Companion(null);

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
    public java.util.Iterator<E> iterator() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    protected AbstractSet() {
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof java.util.Set) {
            return Companion.setEquals$kotlin_stdlib(this, (java.util.Set) other);
        }
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        return Companion.unorderedHashCode$kotlin_stdlib(this);
    }

    /* JADX INFO: compiled from: AbstractSet.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\u0010\u001e\n\u0002\b\u0002\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\bJ\u0019\u0010\t\u001a\u00020\n2\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u000bH\u0000¢\u0006\u0002\b\f¨\u0006\r"}, d2 = {"Lkotlin/collections/AbstractSet$Companion;", "", "()V", "setEquals", "", "c", "", "other", "setEquals$kotlin_stdlib", "unorderedHashCode", "", "", "unorderedHashCode$kotlin_stdlib", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int unorderedHashCode$kotlin_stdlib(java.util.Collection<?> collection) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "c");
            int hashCode = 0;
            java.util.Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                java.lang.Object element = it.next();
                hashCode += element != null ? element.hashCode() : 0;
            }
            return hashCode;
        }

        public final boolean setEquals$kotlin_stdlib(java.util.Set<?> set, java.util.Set<?> set2) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "c");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set2, "other");
            if (set.size() != set2.size()) {
                return false;
            }
            return set.containsAll(set2);
        }
    }
}
