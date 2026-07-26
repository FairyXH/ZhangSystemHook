package kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: MutableCollections.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000P\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0000\u001a9\u0010\u000b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u0010\u001a9\u0010\u000b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u0010\u001a(\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0015\u001a.\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0016\u001a)\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a(\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0015\u001a.\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0016\u001a)\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a-\u0010\u0018\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u001a\u001a&\u0010\u0018\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u001cH\u0087\b¢\u0006\u0002\u0010\u001d\u001a-\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001e\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH\u0087\b\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a\u001d\u0010\u001f\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a\u001f\u0010!\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a\u001d\u0010\"\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a\u001f\u0010#\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a-\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010$\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH\u0087\b\u001a*\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a*\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a\u0015\u0010%\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0003H\u0002¢\u0006\u0002\b&¨\u0006'"}, d2 = {"addAll", "", "T", "", "elements", "", "(Ljava/util/Collection;[Ljava/lang/Object;)Z", "", "Lkotlin/sequences/Sequence;", "convertToListIfNotCollection", "", "filterInPlace", "", "predicate", "Lkotlin/Function1;", "predicateResultToRemove", "filterInPlace$CollectionsKt__MutableCollectionsKt", "", "minusAssign", "", "element", "(Ljava/util/Collection;Ljava/lang/Object;)V", "(Ljava/util/Collection;[Ljava/lang/Object;)V", "plusAssign", "remove", "Lkotlin/internal/OnlyInputTypes;", "(Ljava/util/Collection;Ljava/lang/Object;)Z", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(Ljava/util/List;I)Ljava/lang/Object;", "removeAll", "removeFirst", "(Ljava/util/List;)Ljava/lang/Object;", "removeFirstOrNull", "removeLast", "removeLastOrNull", "retainAll", "retainNothing", "retainNothing$CollectionsKt__MutableCollectionsKt", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/collections/CollectionsKt")
public class CollectionsKt__MutableCollectionsKt extends kotlin.collections.CollectionsKt__MutableCollectionsJVMKt {
    private static final <T> boolean remove(java.util.Collection<? extends T> collection, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        return kotlin.jvm.internal.TypeIntrinsics.asMutableCollection(collection).remove(t);
    }

    private static final <T> boolean removeAll(java.util.Collection<? extends T> collection, java.util.Collection<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return kotlin.jvm.internal.TypeIntrinsics.asMutableCollection(collection).removeAll(elements);
    }

    private static final <T> boolean retainAll(java.util.Collection<? extends T> collection, java.util.Collection<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return kotlin.jvm.internal.TypeIntrinsics.asMutableCollection(collection).retainAll(elements);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> void plusAssign(java.util.Collection<? super T> collection, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        collection.add(t);
    }

    private static final <T> void plusAssign(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        kotlin.collections.CollectionsKt.addAll(collection, elements);
    }

    private static final <T> void plusAssign(java.util.Collection<? super T> collection, T[] elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        kotlin.collections.CollectionsKt.addAll(collection, elements);
    }

    private static final <T> void plusAssign(java.util.Collection<? super T> collection, kotlin.sequences.Sequence<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        kotlin.collections.CollectionsKt.addAll(collection, elements);
    }

    private static final <T> void minusAssign(java.util.Collection<? super T> collection, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        collection.remove(t);
    }

    private static final <T> void minusAssign(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        kotlin.collections.CollectionsKt.removeAll(collection, elements);
    }

    private static final <T> void minusAssign(java.util.Collection<? super T> collection, T[] elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        kotlin.collections.CollectionsKt.removeAll(collection, elements);
    }

    private static final <T> void minusAssign(java.util.Collection<? super T> collection, kotlin.sequences.Sequence<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        kotlin.collections.CollectionsKt.removeAll(collection, elements);
    }

    public static final <T> boolean addAll(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        if (elements instanceof java.util.Collection) {
            return collection.addAll((java.util.Collection) elements);
        }
        boolean result = false;
        for (java.lang.Object item : elements) {
            if (collection.add(item)) {
                result = true;
            }
        }
        return result;
    }

    public static final <T> boolean addAll(java.util.Collection<? super T> collection, kotlin.sequences.Sequence<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        boolean result = false;
        for (java.lang.Object item : elements) {
            if (collection.add(item)) {
                result = true;
            }
        }
        return result;
    }

    public static final <T> boolean addAll(java.util.Collection<? super T> collection, T[] elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return collection.addAll(kotlin.collections.ArraysKt.asList(elements));
    }

    public static final <T> java.util.Collection<T> convertToListIfNotCollection(java.lang.Iterable<? extends T> iterable) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        return iterable instanceof java.util.Collection ? (java.util.Collection) iterable : kotlin.collections.CollectionsKt.toList(iterable);
    }

    public static final <T> boolean removeAll(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return collection.removeAll(kotlin.collections.CollectionsKt.convertToListIfNotCollection(elements));
    }

    public static final <T> boolean removeAll(java.util.Collection<? super T> collection, kotlin.sequences.Sequence<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        java.util.List list = kotlin.sequences.SequencesKt.toList(elements);
        return (list.isEmpty() ^ true) && collection.removeAll(list);
    }

    public static final <T> boolean removeAll(java.util.Collection<? super T> collection, T[] elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return ((elements.length == 0) ^ true) && collection.removeAll(kotlin.collections.ArraysKt.asList(elements));
    }

    public static final <T> boolean retainAll(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return collection.retainAll(kotlin.collections.CollectionsKt.convertToListIfNotCollection(elements));
    }

    public static final <T> boolean retainAll(java.util.Collection<? super T> collection, T[] elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        if (!(elements.length == 0)) {
            return collection.retainAll(kotlin.collections.ArraysKt.asList(elements));
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt(collection);
    }

    public static final <T> boolean retainAll(java.util.Collection<? super T> collection, kotlin.sequences.Sequence<? extends T> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        java.util.List list = kotlin.sequences.SequencesKt.toList(elements);
        if (!list.isEmpty()) {
            return collection.retainAll(list);
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt(collection);
    }

    private static final boolean retainNothing$CollectionsKt__MutableCollectionsKt(java.util.Collection<?> collection) {
        boolean result = !collection.isEmpty();
        collection.clear();
        return result;
    }

    public static final <T> boolean removeAll(java.lang.Iterable<? extends T> iterable, kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((java.lang.Iterable) iterable, (kotlin.jvm.functions.Function1) predicate, true);
    }

    public static final <T> boolean retainAll(java.lang.Iterable<? extends T> iterable, kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((java.lang.Iterable) iterable, (kotlin.jvm.functions.Function1) predicate, false);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(java.lang.Iterable<? extends T> iterable, kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> function1, boolean z) {
        boolean z2 = false;
        java.util.Iterator<? extends T> it = iterable.iterator();
        while (it.hasNext()) {
            if (function1.invoke(it.next()).booleanValue() == z) {
                it.remove();
                z2 = true;
            }
        }
        return z2;
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use removeAt(index) instead.", replaceWith = @kotlin.ReplaceWith(expression = "removeAt(index)", imports = {}))
    private static final <T> T remove(java.util.List<T> list, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        return list.remove(index);
    }

    public static final <T> T removeFirst(java.util.List<T> list) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        if (list.isEmpty()) {
            throw new java.util.NoSuchElementException("List is empty.");
        }
        return list.remove(0);
    }

    public static final <T> T removeFirstOrNull(java.util.List<T> list) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }

    public static final <T> T removeLast(java.util.List<T> list) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        if (list.isEmpty()) {
            throw new java.util.NoSuchElementException("List is empty.");
        }
        return list.remove(kotlin.collections.CollectionsKt.getLastIndex(list));
    }

    public static final <T> T removeLastOrNull(java.util.List<T> list) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(kotlin.collections.CollectionsKt.getLastIndex(list));
    }

    public static final <T> boolean removeAll(java.util.List<T> list, kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((java.util.List) list, (kotlin.jvm.functions.Function1) predicate, true);
    }

    public static final <T> boolean retainAll(java.util.List<T> list, kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((java.util.List) list, (kotlin.jvm.functions.Function1) predicate, false);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(java.util.List<T> list, kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> function1, boolean predicateResultToRemove) {
        if (!(list instanceof java.util.RandomAccess)) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(list, "null cannot be cast to non-null type kotlin.collections.MutableIterable<T of kotlin.collections.CollectionsKt__MutableCollectionsKt.filterInPlace>");
            return filterInPlace$CollectionsKt__MutableCollectionsKt(kotlin.jvm.internal.TypeIntrinsics.asMutableIterable(list), function1, predicateResultToRemove);
        }
        int writeIndex = 0;
        ?? it = new kotlin.ranges.IntRange(0, kotlin.collections.CollectionsKt.getLastIndex(list)).iterator();
        while (it.hasNext()) {
            int readIndex = it.nextInt();
            T t = list.get(readIndex);
            if (function1.invoke(t).booleanValue() != predicateResultToRemove) {
                if (writeIndex != readIndex) {
                    list.set(writeIndex, t);
                }
                writeIndex++;
            }
        }
        if (writeIndex >= list.size()) {
            return false;
        }
        int removeIndex = kotlin.collections.CollectionsKt.getLastIndex(list);
        if (writeIndex > removeIndex) {
            return true;
        }
        while (true) {
            list.remove(removeIndex);
            if (removeIndex == writeIndex) {
                return true;
            }
            removeIndex--;
        }
    }
}
