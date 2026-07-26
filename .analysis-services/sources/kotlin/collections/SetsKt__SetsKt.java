package kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Sets.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000J\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010#\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0005\u001aN\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u001f\b\u0001\u0010\u0005\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006¢\u0006\u0002\b\tH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001\u001aF\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u001f\b\u0001\u0010\u0005\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006¢\u0006\u0002\b\tH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a\u0012\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0001\"\u0004\b\u0000\u0010\u000b\u001a\u001f\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u000b0\rj\b\u0012\u0004\u0012\u0002H\u000b`\u000e\"\u0004\b\u0000\u0010\u000bH\u0087\b\u001a5\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u000b0\rj\b\u0012\u0004\u0012\u0002H\u000b`\u000e\"\u0004\b\u0000\u0010\u000b2\u0012\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u000b0\u0010\"\u0002H\u000b¢\u0006\u0002\u0010\u0011\u001a\u001f\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u0002H\u000b0\u0013j\b\u0012\u0004\u0012\u0002H\u000b`\u0014\"\u0004\b\u0000\u0010\u000bH\u0087\b\u001a5\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u0002H\u000b0\u0013j\b\u0012\u0004\u0012\u0002H\u000b`\u0014\"\u0004\b\u0000\u0010\u000b2\u0012\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u000b0\u0010\"\u0002H\u000b¢\u0006\u0002\u0010\u0015\u001a\u0015\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0007\"\u0004\b\u0000\u0010\u000bH\u0087\b\u001a+\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0007\"\u0004\b\u0000\u0010\u000b2\u0012\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u000b0\u0010\"\u0002H\u000b¢\u0006\u0002\u0010\u0017\u001a\u0015\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0001\"\u0004\b\u0000\u0010\u000bH\u0087\b\u001a+\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0001\"\u0004\b\u0000\u0010\u000b2\u0012\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u000b0\u0010\"\u0002H\u000b¢\u0006\u0002\u0010\u0017\u001a'\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0001\"\b\b\u0000\u0010\u000b*\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u0001H\u000bH\u0007¢\u0006\u0002\u0010\u001c\u001a5\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0001\"\b\b\u0000\u0010\u000b*\u00020\u001a2\u0016\u0010\u000f\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u0001H\u000b0\u0010\"\u0004\u0018\u0001H\u000bH\u0007¢\u0006\u0002\u0010\u0017\u001a\u001e\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0001\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u0001H\u0000\u001a!\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0001\"\u0004\b\u0000\u0010\u000b*\n\u0012\u0004\u0012\u0002H\u000b\u0018\u00010\u0001H\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001f"}, d2 = {"buildSet", "", "E", "capacity", "", "builderAction", "Lkotlin/Function1;", "", "", "Lkotlin/ExtensionFunctionType;", "emptySet", "T", "hashSetOf", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "elements", "", "([Ljava/lang/Object;)Ljava/util/HashSet;", "linkedSetOf", "Ljava/util/LinkedHashSet;", "Lkotlin/collections/LinkedHashSet;", "([Ljava/lang/Object;)Ljava/util/LinkedHashSet;", "mutableSetOf", "([Ljava/lang/Object;)Ljava/util/Set;", "setOf", "setOfNotNull", "", "element", "(Ljava/lang/Object;)Ljava/util/Set;", "optimizeReadOnlySet", "orEmpty", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/collections/SetsKt")
public class SetsKt__SetsKt extends kotlin.collections.SetsKt__SetsJVMKt {
    public static final <T> java.util.Set<T> emptySet() {
        return kotlin.collections.EmptySet.INSTANCE;
    }

    public static final <T> java.util.Set<T> setOf(T... elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return elements.length > 0 ? kotlin.collections.ArraysKt.toSet(elements) : kotlin.collections.SetsKt.emptySet();
    }

    private static final <T> java.util.Set<T> setOf() {
        return kotlin.collections.SetsKt.emptySet();
    }

    private static final <T> java.util.Set<T> mutableSetOf() {
        return new java.util.LinkedHashSet();
    }

    public static final <T> java.util.Set<T> mutableSetOf(T... elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return (java.util.Set) kotlin.collections.ArraysKt.toCollection(elements, new java.util.LinkedHashSet(kotlin.collections.MapsKt.mapCapacity(elements.length)));
    }

    private static final <T> java.util.HashSet<T> hashSetOf() {
        return new java.util.HashSet<>();
    }

    public static final <T> java.util.HashSet<T> hashSetOf(T... elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return (java.util.HashSet) kotlin.collections.ArraysKt.toCollection(elements, new java.util.HashSet(kotlin.collections.MapsKt.mapCapacity(elements.length)));
    }

    private static final <T> java.util.LinkedHashSet<T> linkedSetOf() {
        return new java.util.LinkedHashSet<>();
    }

    public static final <T> java.util.LinkedHashSet<T> linkedSetOf(T... elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return (java.util.LinkedHashSet) kotlin.collections.ArraysKt.toCollection(elements, new java.util.LinkedHashSet(kotlin.collections.MapsKt.mapCapacity(elements.length)));
    }

    public static final <T> java.util.Set<T> setOfNotNull(T t) {
        return t != null ? kotlin.collections.SetsKt.setOf(t) : kotlin.collections.SetsKt.emptySet();
    }

    public static final <T> java.util.Set<T> setOfNotNull(T... elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return (java.util.Set) kotlin.collections.ArraysKt.filterNotNullTo(elements, new java.util.LinkedHashSet());
    }

    private static final <E> java.util.Set<E> buildSet(kotlin.jvm.functions.Function1<? super java.util.Set<E>, kotlin.Unit> builderAction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        java.util.Set setCreateSetBuilder = kotlin.collections.SetsKt.createSetBuilder();
        builderAction.invoke(setCreateSetBuilder);
        return kotlin.collections.SetsKt.build(setCreateSetBuilder);
    }

    private static final <E> java.util.Set<E> buildSet(int capacity, kotlin.jvm.functions.Function1<? super java.util.Set<E>, kotlin.Unit> builderAction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        java.util.Set setCreateSetBuilder = kotlin.collections.SetsKt.createSetBuilder(capacity);
        builderAction.invoke(setCreateSetBuilder);
        return kotlin.collections.SetsKt.build(setCreateSetBuilder);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> java.util.Set<T> orEmpty(java.util.Set<? extends T> set) {
        return set == 0 ? kotlin.collections.SetsKt.emptySet() : set;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> java.util.Set<T> optimizeReadOnlySet(java.util.Set<? extends T> set) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        switch (set.size()) {
            case 0:
                return kotlin.collections.SetsKt.emptySet();
            case 1:
                return kotlin.collections.SetsKt.setOf(set.iterator().next());
            default:
                return set;
        }
    }
}
