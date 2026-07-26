package kotlin.collections.unsigned;

/* JADX INFO: compiled from: _UArraysJvm.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u000e*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0096\u0002¢\u0006\u0004\b\f\u0010\rJ\u001b\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0006H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0011J\u0017\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0013\u0010\u0014J\b\u0010\u0015\u001a\u00020\nH\u0016J\u0017\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0017\u0010\u0014R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0004\n\u0002\b!¨\u0006\u0018"}, d2 = {"kotlin/collections/unsigned/UArraysKt___UArraysJvmKt$asList$2", "Lkotlin/collections/AbstractList;", "Lkotlin/ULong;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "size", "", "getSize", "()I", "contains", "", "element", "contains-VKZWuLQ", "(J)Z", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "get-s-VKNKU", "(I)J", "indexOf", "indexOf-VKZWuLQ", "(J)I", "isEmpty", "lastIndexOf", "lastIndexOf-VKZWuLQ", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class UArraysKt___UArraysJvmKt$asList$2 extends kotlin.collections.AbstractList<kotlin.ULong> implements java.util.RandomAccess {
    final /* synthetic */ long[] $this_asList;

    UArraysKt___UArraysJvmKt$asList$2(long[] $receiver) {
        this.$this_asList = $receiver;
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public final /* bridge */ boolean contains(java.lang.Object element) {
        if (element instanceof kotlin.ULong) {
            return m11819containsVKZWuLQ(((kotlin.ULong) element).getData());
        }
        return false;
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ java.lang.Object get(int index) {
        return kotlin.ULong.m11475boximpl(m11820getsVKNKU(index));
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int indexOf(java.lang.Object element) {
        if (element instanceof kotlin.ULong) {
            return m11821indexOfVKZWuLQ(((kotlin.ULong) element).getData());
        }
        return -1;
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int lastIndexOf(java.lang.Object element) {
        if (element instanceof kotlin.ULong) {
            return m11822lastIndexOfVKZWuLQ(((kotlin.ULong) element).getData());
        }
        return -1;
    }

    @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
    public int getSize() {
        return kotlin.ULongArray.m11542getSizeimpl(this.$this_asList);
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return kotlin.ULongArray.m11544isEmptyimpl(this.$this_asList);
    }

    /* JADX INFO: renamed from: contains-VKZWuLQ, reason: not valid java name */
    public boolean m11819containsVKZWuLQ(long element) {
        return kotlin.ULongArray.m11537containsVKZWuLQ(this.$this_asList, element);
    }

    /* JADX INFO: renamed from: get-s-VKNKU, reason: not valid java name */
    public long m11820getsVKNKU(int index) {
        return kotlin.ULongArray.m11541getsVKNKU(this.$this_asList, index);
    }

    /* JADX INFO: renamed from: indexOf-VKZWuLQ, reason: not valid java name */
    public int m11821indexOfVKZWuLQ(long element) {
        return kotlin.collections.ArraysKt.indexOf(this.$this_asList, element);
    }

    /* JADX INFO: renamed from: lastIndexOf-VKZWuLQ, reason: not valid java name */
    public int m11822lastIndexOfVKZWuLQ(long element) {
        return kotlin.collections.ArraysKt.lastIndexOf(this.$this_asList, element);
    }
}
