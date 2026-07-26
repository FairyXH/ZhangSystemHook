package kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: CollectionsJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000T\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\"\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0001\u001a?\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0006\u001a\u00020\u00072\u001d\u0010\b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0002\b\u000bH\u0081\bø\u0001\u0000\u001a7\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u001d\u0010\b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0002\b\u000bH\u0081\bø\u0001\u0000\u001a\u0011\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007H\u0081\b\u001a\u0011\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0081\b\u001a\"\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00112\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0014H\u0081\b¢\u0006\u0002\u0010\u0015\u001a4\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0011\"\u0004\b\u0000\u0010\u00162\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u00142\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0011H\u0081\b¢\u0006\u0002\u0010\u0018\u001a\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\"\u0004\b\u0000\u0010\u0002H\u0001\u001a\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0006\u001a\u00020\u0007H\u0001\u001a\u001f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0001\"\u0004\b\u0000\u0010\u00162\u0006\u0010\u001b\u001a\u0002H\u0016¢\u0006\u0002\u0010\u001c\u001a/\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0011\"\u0004\b\u0000\u0010\u00162\u0006\u0010\u001e\u001a\u00020\u00072\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0011H\u0000¢\u0006\u0002\u0010\u001f\u001a1\u0010 \u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00120\u0011\"\u0004\b\u0000\u0010\u0016*\n\u0012\u0006\b\u0001\u0012\u0002H\u00160\u00112\u0006\u0010!\u001a\u00020\"H\u0000¢\u0006\u0002\u0010#\u001a\u001e\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0001\"\u0004\b\u0000\u0010\u0016*\b\u0012\u0004\u0012\u0002H\u00160%H\u0007\u001a&\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0001\"\u0004\b\u0000\u0010\u0016*\b\u0012\u0004\u0012\u0002H\u00160%2\u0006\u0010&\u001a\u00020'H\u0007\u001a\u001f\u0010(\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0001\"\u0004\b\u0000\u0010\u0016*\b\u0012\u0004\u0012\u0002H\u00160)H\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006*"}, d2 = {"build", "", "E", "builder", "", "buildListInternal", "capacity", "", "builderAction", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "checkCountOverflow", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, "checkIndexOverflow", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "collectionToArray", "", "", "collection", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "T", "array", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "createListBuilder", "listOf", "element", "(Ljava/lang/Object;)Ljava/util/List;", "terminateCollectionToArray", "collectionSize", "(I[Ljava/lang/Object;)[Ljava/lang/Object;", "copyToArrayOfAny", "isVarargs", "", "([Ljava/lang/Object;Z)[Ljava/lang/Object;", "shuffled", "", "random", "Ljava/util/Random;", "toList", "Ljava/util/Enumeration;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/collections/CollectionsKt")
public class CollectionsKt__CollectionsJVMKt {
    public static final <T> java.util.List<T> listOf(T t) {
        java.util.List<T> listSingletonList = java.util.Collections.singletonList(t);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(listSingletonList, "singletonList(...)");
        return listSingletonList;
    }

    private static final <E> java.util.List<E> buildListInternal(kotlin.jvm.functions.Function1<? super java.util.List<E>, kotlin.Unit> builderAction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        java.util.List listCreateListBuilder = kotlin.collections.CollectionsKt.createListBuilder();
        builderAction.invoke(listCreateListBuilder);
        return kotlin.collections.CollectionsKt.build(listCreateListBuilder);
    }

    private static final <E> java.util.List<E> buildListInternal(int capacity, kotlin.jvm.functions.Function1<? super java.util.List<E>, kotlin.Unit> builderAction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        java.util.List listCreateListBuilder = kotlin.collections.CollectionsKt.createListBuilder(capacity);
        builderAction.invoke(listCreateListBuilder);
        return kotlin.collections.CollectionsKt.build(listCreateListBuilder);
    }

    public static final <E> java.util.List<E> createListBuilder() {
        return new kotlin.collections.builders.ListBuilder();
    }

    public static final <E> java.util.List<E> createListBuilder(int capacity) {
        return new kotlin.collections.builders.ListBuilder(capacity);
    }

    public static final <E> java.util.List<E> build(java.util.List<E> builder) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builder, "builder");
        return ((kotlin.collections.builders.ListBuilder) builder).build();
    }

    private static final <T> java.util.List<T> toList(java.util.Enumeration<T> enumeration) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(enumeration, "<this>");
        java.util.ArrayList list = java.util.Collections.list(enumeration);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(list, "list(...)");
        return list;
    }

    public static final <T> java.util.List<T> shuffled(java.lang.Iterable<? extends T> iterable) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        java.util.List<T> mutableList = kotlin.collections.CollectionsKt.toMutableList(iterable);
        java.util.Collections.shuffle(mutableList);
        return mutableList;
    }

    public static final <T> java.util.List<T> shuffled(java.lang.Iterable<? extends T> iterable, java.util.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        java.util.List<T> mutableList = kotlin.collections.CollectionsKt.toMutableList(iterable);
        java.util.Collections.shuffle(mutableList, random);
        return mutableList;
    }

    private static final java.lang.Object[] collectionToArray(java.util.Collection<?> collection) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "collection");
        return kotlin.jvm.internal.CollectionToArray.toArray(collection);
    }

    private static final <T> T[] collectionToArray(java.util.Collection<?> collection, T[] array) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "collection");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return (T[]) kotlin.jvm.internal.CollectionToArray.toArray(collection, array);
    }

    public static final <T> T[] terminateCollectionToArray(int collectionSize, T[] array) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        if (collectionSize < array.length) {
            array[collectionSize] = null;
        }
        return array;
    }

    public static final <T> java.lang.Object[] copyToArrayOfAny(T[] tArr, boolean isVarargs) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "<this>");
        if (isVarargs && kotlin.jvm.internal.Intrinsics.areEqual(tArr.getClass(), java.lang.Object[].class)) {
            return tArr;
        }
        java.lang.Object[] objArrCopyOf = java.util.Arrays.copyOf(tArr, tArr.length, java.lang.Object[].class);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf, "copyOf(...)");
        return objArrCopyOf;
    }

    private static final int checkIndexOverflow(int index) {
        if (index < 0) {
            if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                kotlin.collections.CollectionsKt.throwIndexOverflow();
            } else {
                throw new java.lang.ArithmeticException("Index overflow has happened.");
            }
        }
        return index;
    }

    private static final int checkCountOverflow(int count) {
        if (count < 0) {
            if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                kotlin.collections.CollectionsKt.throwCountOverflow();
            } else {
                throw new java.lang.ArithmeticException("Count overflow has happened.");
            }
        }
        return count;
    }
}
