package kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SetsJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000B\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0001\u001a?\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0006\u001a\u00020\u00072\u001d\u0010\b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0002\b\u000bH\u0081\bø\u0001\u0000\u001a7\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u001d\u0010\b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0002\b\u000bH\u0081\bø\u0001\u0000\u001a\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\"\u0004\b\u0000\u0010\u0002H\u0001\u001a\u001c\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0006\u001a\u00020\u0007H\u0001\u001a\u001f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u0001\"\u0004\b\u0000\u0010\u000e2\u0006\u0010\u000f\u001a\u0002H\u000e¢\u0006\u0002\u0010\u0010\u001a+\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u0012\"\u0004\b\u0000\u0010\u000e2\u0012\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u000e0\u0014\"\u0002H\u000e¢\u0006\u0002\u0010\u0015\u001aG\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u0012\"\u0004\b\u0000\u0010\u000e2\u001a\u0010\u0016\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0017j\n\u0012\u0006\b\u0000\u0012\u0002H\u000e`\u00182\u0012\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u000e0\u0014\"\u0002H\u000e¢\u0006\u0002\u0010\u0019\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001a"}, d2 = {"build", "", "E", "builder", "", "buildSetInternal", "capacity", "", "builderAction", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "createSetBuilder", "setOf", "T", "element", "(Ljava/lang/Object;)Ljava/util/Set;", "sortedSetOf", "Ljava/util/TreeSet;", "elements", "", "([Ljava/lang/Object;)Ljava/util/TreeSet;", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/Comparator;[Ljava/lang/Object;)Ljava/util/TreeSet;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/collections/SetsKt")
public class SetsKt__SetsJVMKt {
    public static final <T> java.util.Set<T> setOf(T t) {
        java.util.Set<T> setSingleton = java.util.Collections.singleton(t);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(setSingleton, "singleton(...)");
        return setSingleton;
    }

    private static final <E> java.util.Set<E> buildSetInternal(kotlin.jvm.functions.Function1<? super java.util.Set<E>, kotlin.Unit> builderAction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        java.util.Set setCreateSetBuilder = kotlin.collections.SetsKt.createSetBuilder();
        builderAction.invoke(setCreateSetBuilder);
        return kotlin.collections.SetsKt.build(setCreateSetBuilder);
    }

    private static final <E> java.util.Set<E> buildSetInternal(int capacity, kotlin.jvm.functions.Function1<? super java.util.Set<E>, kotlin.Unit> builderAction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        java.util.Set setCreateSetBuilder = kotlin.collections.SetsKt.createSetBuilder(capacity);
        builderAction.invoke(setCreateSetBuilder);
        return kotlin.collections.SetsKt.build(setCreateSetBuilder);
    }

    public static final <E> java.util.Set<E> createSetBuilder() {
        return new kotlin.collections.builders.SetBuilder();
    }

    public static final <E> java.util.Set<E> createSetBuilder(int capacity) {
        return new kotlin.collections.builders.SetBuilder(capacity);
    }

    public static final <E> java.util.Set<E> build(java.util.Set<E> builder) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builder, "builder");
        return ((kotlin.collections.builders.SetBuilder) builder).build();
    }

    public static final <T> java.util.TreeSet<T> sortedSetOf(T... elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return (java.util.TreeSet) kotlin.collections.ArraysKt.toCollection(elements, new java.util.TreeSet());
    }

    public static final <T> java.util.TreeSet<T> sortedSetOf(java.util.Comparator<? super T> comparator, T... elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return (java.util.TreeSet) kotlin.collections.ArraysKt.toCollection(elements, new java.util.TreeSet(comparator));
    }
}
