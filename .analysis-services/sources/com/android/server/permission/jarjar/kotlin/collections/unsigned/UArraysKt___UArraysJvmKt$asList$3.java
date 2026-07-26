package com.android.server.permission.jarjar.kotlin.collections.unsigned;

/* JADX INFO: compiled from: _UArraysJvm.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u000e*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0096\u0002¢\u0006\u0004\b\f\u0010\rJ\u001b\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0006H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0011J\u0017\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0013\u0010\u0014J\b\u0010\u0015\u001a\u00020\nH\u0016J\u0017\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0017\u0010\u0014R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0004\n\u0002\b!¨\u0006\u0018"}, d2 = {"com/android/server/permission/jarjar/kotlin/collections/unsigned/UArraysKt___UArraysJvmKt$asList$3", "Lkotlin/collections/AbstractList;", "Lkotlin/UByte;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "size", "", "getSize", "()I", "contains", "", "element", "contains-7apg3OU", "(B)Z", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "get-w2LRezQ", "(I)B", "indexOf", "indexOf-7apg3OU", "(B)I", "isEmpty", "lastIndexOf", "lastIndexOf-7apg3OU", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class UArraysKt___UArraysJvmKt$asList$3 extends com.android.server.permission.jarjar.kotlin.collections.AbstractList<com.android.server.permission.jarjar.kotlin.UByte> implements java.util.RandomAccess {
    final /* synthetic */ byte[] $this_asList;

    UArraysKt___UArraysJvmKt$asList$3(byte[] $receiver) {
        this.$this_asList = $receiver;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection
    public final /* bridge */ boolean contains(java.lang.Object element) {
        if (element instanceof com.android.server.permission.jarjar.kotlin.UByte) {
            return m6605contains7apg3OU(((com.android.server.permission.jarjar.kotlin.UByte) element).m6157unboximpl());
        }
        return false;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ java.lang.Object get(int index) {
        return com.android.server.permission.jarjar.kotlin.UByte.m6101boximpl(m6606getw2LRezQ(index));
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int indexOf(java.lang.Object element) {
        if (element instanceof com.android.server.permission.jarjar.kotlin.UByte) {
            return m6607indexOf7apg3OU(((com.android.server.permission.jarjar.kotlin.UByte) element).m6157unboximpl());
        }
        return -1;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int lastIndexOf(java.lang.Object element) {
        if (element instanceof com.android.server.permission.jarjar.kotlin.UByte) {
            return m6608lastIndexOf7apg3OU(((com.android.server.permission.jarjar.kotlin.UByte) element).m6157unboximpl());
        }
        return -1;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, com.android.server.permission.jarjar.kotlin.collections.AbstractCollection
    public int getSize() {
        return com.android.server.permission.jarjar.kotlin.UByteArray.m6166getSizeimpl(this.$this_asList);
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return com.android.server.permission.jarjar.kotlin.UByteArray.m6168isEmptyimpl(this.$this_asList);
    }

    /* JADX INFO: renamed from: contains-7apg3OU, reason: not valid java name */
    public boolean m6605contains7apg3OU(byte element) {
        return com.android.server.permission.jarjar.kotlin.UByteArray.m6161contains7apg3OU(this.$this_asList, element);
    }

    /* JADX INFO: renamed from: get-w2LRezQ, reason: not valid java name */
    public byte m6606getw2LRezQ(int index) {
        return com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ(this.$this_asList, index);
    }

    /* JADX INFO: renamed from: indexOf-7apg3OU, reason: not valid java name */
    public int m6607indexOf7apg3OU(byte element) {
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.indexOf(this.$this_asList, element);
    }

    /* JADX INFO: renamed from: lastIndexOf-7apg3OU, reason: not valid java name */
    public int m6608lastIndexOf7apg3OU(byte element) {
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.lastIndexOf(this.$this_asList, element);
    }
}
