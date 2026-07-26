package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000-\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u00012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0002J\u0013\u0010\b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\t\u001a\u00020\u0005H\u0096\u0002J\u0013\u0010\b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0096\u0002J\b\u0010\f\u001a\u00020\rH\u0016J\u0011\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u000fH\u0096\u0002R\u0014\u0010\u0004\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0010"}, d2 = {"com/android/server/permission/jarjar/kotlin/text/MatcherMatchResult$groups$1", "Lkotlin/text/MatchNamedGroupCollection;", "Lkotlin/collections/AbstractCollection;", "Lkotlin/text/MatchGroup;", "size", "", "getSize", "()I", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "name", "", "isEmpty", "", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MatcherMatchResult$groups$1 extends com.android.server.permission.jarjar.kotlin.collections.AbstractCollection<com.android.server.permission.jarjar.kotlin.text.MatchGroup> implements com.android.server.permission.jarjar.kotlin.text.MatchNamedGroupCollection {
    final /* synthetic */ com.android.server.permission.jarjar.kotlin.text.MatcherMatchResult this$0;

    MatcherMatchResult$groups$1(com.android.server.permission.jarjar.kotlin.text.MatcherMatchResult $receiver) {
        this.this$0 = $receiver;
    }

    public /* bridge */ boolean contains(com.android.server.permission.jarjar.kotlin.text.MatchGroup element) {
        return super.contains(element);
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection
    public final /* bridge */ boolean contains(java.lang.Object element) {
        if (element == null ? true : element instanceof com.android.server.permission.jarjar.kotlin.text.MatchGroup) {
            return contains((com.android.server.permission.jarjar.kotlin.text.MatchGroup) element);
        }
        return false;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection
    public int getSize() {
        return this.this$0.getMatchResult().groupCount() + 1;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return false;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
    public java.util.Iterator<com.android.server.permission.jarjar.kotlin.text.MatchGroup> iterator() {
        return com.android.server.permission.jarjar.kotlin.sequences.SequencesKt.map(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.asSequence(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getIndices(this)), new com.android.server.permission.jarjar.kotlin.text.MatcherMatchResult$groups$1$iterator$1(this)).iterator();
    }

    @Override // com.android.server.permission.jarjar.kotlin.text.MatchGroupCollection
    public com.android.server.permission.jarjar.kotlin.text.MatchGroup get(int index) {
        com.android.server.permission.jarjar.kotlin.ranges.IntRange range = com.android.server.permission.jarjar.kotlin.text.RegexKt.range(this.this$0.getMatchResult(), index);
        if (range.getStart().intValue() >= 0) {
            java.lang.String strGroup = this.this$0.getMatchResult().group(index);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strGroup, "group(...)");
            return new com.android.server.permission.jarjar.kotlin.text.MatchGroup(strGroup, range);
        }
        return null;
    }

    @Override // com.android.server.permission.jarjar.kotlin.text.MatchNamedGroupCollection
    public com.android.server.permission.jarjar.kotlin.text.MatchGroup get(java.lang.String name) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        return com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.IMPLEMENTATIONS.getMatchResultNamedGroup(this.this$0.getMatchResult(), name);
    }
}
