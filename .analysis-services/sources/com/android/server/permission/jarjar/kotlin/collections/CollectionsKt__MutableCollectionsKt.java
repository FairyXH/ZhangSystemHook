package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: MutableCollections.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000P\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0000\u001a9\u0010\u000b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u0010\u001a9\u0010\u000b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u0010\u001a(\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0015\u001a.\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0016\u001a)\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a(\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0015\u001a.\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0016\u001a)\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a-\u0010\u0018\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u001a\u001a&\u0010\u0018\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u001cH\u0087\b¢\u0006\u0002\u0010\u001d\u001a-\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001e\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH\u0087\b\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a\u001d\u0010\u001f\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a\u001f\u0010!\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a\u001d\u0010\"\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a\u001f\u0010#\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a-\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010$\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH\u0087\b\u001a*\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a*\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a\u0015\u0010%\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0003H\u0002¢\u0006\u0002\b&¨\u0006'"}, d2 = {"addAll", "", "T", "", "elements", "", "(Ljava/util/Collection;[Ljava/lang/Object;)Z", "", "Lkotlin/sequences/Sequence;", "convertToListIfNotCollection", "", "filterInPlace", "", "predicate", "Lkotlin/Function1;", "predicateResultToRemove", "filterInPlace$CollectionsKt__MutableCollectionsKt", "", "minusAssign", "", "element", "(Ljava/util/Collection;Ljava/lang/Object;)V", "(Ljava/util/Collection;[Ljava/lang/Object;)V", "plusAssign", "remove", "Lkotlin/internal/OnlyInputTypes;", "(Ljava/util/Collection;Ljava/lang/Object;)Z", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(Ljava/util/List;I)Ljava/lang/Object;", "removeAll", "removeFirst", "(Ljava/util/List;)Ljava/lang/Object;", "removeFirstOrNull", "removeLast", "removeLastOrNull", "retainAll", "retainNothing", "retainNothing$CollectionsKt__MutableCollectionsKt", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/CollectionsKt")
public class CollectionsKt__MutableCollectionsKt extends com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__MutableCollectionsJVMKt {
    private static final <T> boolean remove(java.util.Collection<? extends T> collection, T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        return com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.asMutableCollection(collection).remove(t);
    }

    private static final <T> boolean removeAll(java.util.Collection<? extends T> collection, java.util.Collection<? extends T> collection2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection2, "elements");
        return com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.asMutableCollection(collection).removeAll(collection2);
    }

    private static final <T> boolean retainAll(java.util.Collection<? extends T> collection, java.util.Collection<? extends T> collection2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection2, "elements");
        return com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.asMutableCollection(collection).retainAll(collection2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> void plusAssign(java.util.Collection<? super T> collection, T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        collection.add(t);
    }

    private static final <T> void plusAssign(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "elements");
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.addAll(collection, iterable);
    }

    private static final <T> void plusAssign(java.util.Collection<? super T> collection, T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "elements");
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.addAll(collection, tArr);
    }

    private static final <T> void plusAssign(java.util.Collection<? super T> collection, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "elements");
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.addAll(collection, sequence);
    }

    private static final <T> void minusAssign(java.util.Collection<? super T> collection, T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        collection.remove(t);
    }

    private static final <T> void minusAssign(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "elements");
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll(collection, iterable);
    }

    private static final <T> void minusAssign(java.util.Collection<? super T> collection, T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "elements");
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll(collection, tArr);
    }

    private static final <T> void minusAssign(java.util.Collection<? super T> collection, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "elements");
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll(collection, sequence);
    }

    public static final <T> boolean addAll(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "elements");
        if (iterable instanceof java.util.Collection) {
            return collection.addAll((java.util.Collection) iterable);
        }
        boolean result = false;
        for (java.lang.Object item : iterable) {
            if (collection.add(item)) {
                result = true;
            }
        }
        return result;
    }

    public static final <T> boolean addAll(java.util.Collection<? super T> collection, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "elements");
        boolean result = false;
        for (java.lang.Object item : sequence) {
            if (collection.add(item)) {
                result = true;
            }
        }
        return result;
    }

    public static final <T> boolean addAll(java.util.Collection<? super T> collection, T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "elements");
        return collection.addAll(com.android.server.permission.jarjar.kotlin.collections.ArraysKt.asList(tArr));
    }

    public static final <T> java.util.Collection<T> convertToListIfNotCollection(java.lang.Iterable<? extends T> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        return iterable instanceof java.util.Collection ? (java.util.Collection) iterable : com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.toList(iterable);
    }

    public static final <T> boolean removeAll(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "elements");
        return collection.removeAll(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.convertToListIfNotCollection(iterable));
    }

    public static final <T> boolean removeAll(java.util.Collection<? super T> collection, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "elements");
        java.util.List list = com.android.server.permission.jarjar.kotlin.sequences.SequencesKt.toList(sequence);
        return (list.isEmpty() ^ true) && collection.removeAll(list);
    }

    public static final <T> boolean removeAll(java.util.Collection<? super T> collection, T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "elements");
        return ((tArr.length == 0) ^ true) && collection.removeAll(com.android.server.permission.jarjar.kotlin.collections.ArraysKt.asList(tArr));
    }

    public static final <T> boolean retainAll(java.util.Collection<? super T> collection, java.lang.Iterable<? extends T> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "elements");
        return collection.retainAll(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.convertToListIfNotCollection(iterable));
    }

    public static final <T> boolean retainAll(java.util.Collection<? super T> collection, T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "elements");
        if (!(tArr.length == 0)) {
            return collection.retainAll(com.android.server.permission.jarjar.kotlin.collections.ArraysKt.asList(tArr));
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt(collection);
    }

    public static final <T> boolean retainAll(java.util.Collection<? super T> collection, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "elements");
        java.util.List list = com.android.server.permission.jarjar.kotlin.sequences.SequencesKt.toList(sequence);
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

    public static final <T> boolean removeAll(java.lang.Iterable<? extends T> iterable, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((java.lang.Iterable) iterable, (com.android.server.permission.jarjar.kotlin.jvm.functions.Function1) function1, true);
    }

    public static final <T> boolean retainAll(java.lang.Iterable<? extends T> iterable, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((java.lang.Iterable) iterable, (com.android.server.permission.jarjar.kotlin.jvm.functions.Function1) function1, false);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(java.lang.Iterable<? extends T> iterable, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> function1, boolean z) {
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

    @com.android.server.permission.jarjar.kotlin.Deprecated(level = com.android.server.permission.jarjar.kotlin.DeprecationLevel.ERROR, message = "Use removeAt(index) instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "removeAt(index)", imports = {}))
    private static final <T> T remove(java.util.List<T> list, int index) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        return list.remove(index);
    }

    public static final <T> T removeFirst(java.util.List<T> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        if (list.isEmpty()) {
            throw new java.util.NoSuchElementException("List is empty.");
        }
        return list.remove(0);
    }

    public static final <T> T removeFirstOrNull(java.util.List<T> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }

    public static final <T> T removeLast(java.util.List<T> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        if (list.isEmpty()) {
            throw new java.util.NoSuchElementException("List is empty.");
        }
        return list.remove(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list));
    }

    public static final <T> T removeLastOrNull(java.util.List<T> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list));
    }

    public static final <T> boolean removeAll(java.util.List<T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((java.util.List) list, (com.android.server.permission.jarjar.kotlin.jvm.functions.Function1) function1, true);
    }

    public static final <T> boolean retainAll(java.util.List<T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((java.util.List) list, (com.android.server.permission.jarjar.kotlin.jvm.functions.Function1) function1, false);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(java.util.List<T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> function1, boolean predicateResultToRemove) {
        if (!(list instanceof java.util.RandomAccess)) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(list, "null cannot be cast to non-null type kotlin.collections.MutableIterable<T of kotlin.collections.CollectionsKt__MutableCollectionsKt.filterInPlace>");
            return filterInPlace$CollectionsKt__MutableCollectionsKt(com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.asMutableIterable(list), function1, predicateResultToRemove);
        }
        int writeIndex = 0;
        ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(0, com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list)).iterator2();
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
        int removeIndex = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list);
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
