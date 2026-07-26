package kotlin.collections;

/* JADX INFO: compiled from: _MapsJvm.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0000\n\u0002\u0010&\n\u0002\b\u0003\n\u0002\u0010\u000f\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001ah\u0010\u0000\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00062\u001e\u0010\u0007\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0004\u0012\u0002H\u00040\bH\u0087\bø\u0001\u0000\u001ai\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000622\u0010\n\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00010\u000bj\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001`\fH\u0087\b\u001ah\u0010\r\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00062\u001e\u0010\u0007\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0004\u0012\u0002H\u00040\bH\u0087\bø\u0001\u0000\u001ah\u0010\u000e\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000622\u0010\n\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00010\u000bj\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001`\fH\u0007\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u000f"}, d2 = {"maxBy", "", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "R", "", "", "selector", "Lkotlin/Function1;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "minBy", "minWith", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/collections/MapsKt")
class MapsKt___MapsJvmKt extends kotlin.collections.MapsKt__MapsKt {
    @kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    private static final /* synthetic */ <K, V, R extends java.lang.Comparable<? super R>> java.util.Map.Entry<K, V> maxBy(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        java.util.Map.Entry<K, V> entry;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (it.hasNext()) {
            java.util.Map.Entry<K, V> entry2 = (java.lang.Object) it.next();
            if (it.hasNext()) {
                R rInvoke = selector.invoke(entry2);
                do {
                    java.util.Map.Entry<K, V> entry3 = (java.lang.Object) it.next();
                    R rInvoke2 = selector.invoke(entry3);
                    if (rInvoke.compareTo(rInvoke2) < 0) {
                        entry2 = entry3;
                        rInvoke = rInvoke2;
                    }
                } while (it.hasNext());
            }
            entry = entry2;
        } else {
            entry = null;
        }
        return entry;
    }

    @kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    private static final /* synthetic */ <K, V> java.util.Map.Entry<K, V> maxWith(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super java.util.Map.Entry<? extends K, ? extends V>> comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (java.util.Map.Entry) kotlin.collections.CollectionsKt.maxWithOrNull(map.entrySet(), comparator);
    }

    @kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <K, V, R extends java.lang.Comparable<? super R>> java.util.Map.Entry<K, V> minBy(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        java.util.Map.Entry<K, V> entry;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (it.hasNext()) {
            java.util.Map.Entry<K, V> entry2 = (java.lang.Object) it.next();
            if (it.hasNext()) {
                R rInvoke = selector.invoke(entry2);
                do {
                    java.util.Map.Entry<K, V> entry3 = (java.lang.Object) it.next();
                    R rInvoke2 = selector.invoke(entry3);
                    if (rInvoke.compareTo(rInvoke2) > 0) {
                        entry2 = entry3;
                        rInvoke = rInvoke2;
                    }
                } while (it.hasNext());
            }
            entry = entry2;
        } else {
            entry = null;
        }
        return entry;
    }

    @kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.util.Map.Entry minWith(java.util.Map $this$minWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (java.util.Map.Entry) kotlin.collections.CollectionsKt.minWithOrNull($this$minWith.entrySet(), comparator);
    }
}
