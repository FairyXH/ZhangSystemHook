package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: compiled from: CollectionToArray.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a#\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\bH\u0007¢\u0006\u0004\b\t\u0010\n\u001a5\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u0001H\u0007¢\u0006\u0004\b\t\u0010\f\u001a~\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0014\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u000f2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u00112(\u0010\u0012\u001a$\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u0013H\u0082\b¢\u0006\u0002\u0010\u0014\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"EMPTY", "", "", "[Ljava/lang/Object;", "MAX_SIZE", "", "collectionToArray", "collection", "", "toArray", "(Ljava/util/Collection;)[Ljava/lang/Object;", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "toArrayImpl", "empty", "Lkotlin/Function0;", "alloc", "Lkotlin/Function1;", "trim", "Lkotlin/Function2;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)[Ljava/lang/Object;", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CollectionToArray {
    private static final java.lang.Object[] EMPTY = new java.lang.Object[0];
    private static final int MAX_SIZE = 2147483645;

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(warningSince = "1.9")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This function will be made internal in a future release")
    public static final java.lang.Object[] toArray(java.util.Collection<?> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "collection");
        int size$iv = collection.size();
        if (size$iv == 0) {
            return EMPTY;
        }
        java.util.Iterator<?> it = collection.iterator();
        if (!it.hasNext()) {
            return EMPTY;
        }
        java.lang.Object[] result$iv = new java.lang.Object[size$iv];
        int i$iv = 0;
        while (true) {
            int i$iv2 = i$iv + 1;
            result$iv[i$iv] = it.next();
            if (i$iv2 >= result$iv.length) {
                if (!it.hasNext()) {
                    return result$iv;
                }
                int newSize$iv = ((i$iv2 * 3) + 1) >>> 1;
                if (newSize$iv <= i$iv2) {
                    if (i$iv2 >= MAX_SIZE) {
                        throw new java.lang.OutOfMemoryError();
                    }
                    newSize$iv = MAX_SIZE;
                }
                java.lang.Object[] objArrCopyOf = java.util.Arrays.copyOf(result$iv, newSize$iv);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf, "copyOf(...)");
                result$iv = objArrCopyOf;
                i$iv = i$iv2;
            } else {
                if (!it.hasNext()) {
                    java.lang.Object[] result = result$iv;
                    java.lang.Object[] objArrCopyOf2 = java.util.Arrays.copyOf(result, i$iv2);
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf2, "copyOf(...)");
                    return objArrCopyOf2;
                }
                i$iv = i$iv2;
            }
        }
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(warningSince = "1.9")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This function will be made internal in a future release")
    public static final java.lang.Object[] toArray(java.util.Collection<?> collection, java.lang.Object[] a) {
        java.lang.Object[] objArr;
        java.lang.Object[] objArrCopyOf;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "collection");
        if (a == null) {
            throw new java.lang.NullPointerException();
        }
        int size$iv = collection.size();
        if (size$iv == 0) {
            if (a.length > 0) {
                a[0] = null;
            }
        } else {
            java.util.Iterator<?> it = collection.iterator();
            if (!it.hasNext()) {
                if (a.length > 0) {
                    a[0] = null;
                }
            } else {
                if (size$iv <= a.length) {
                    objArr = a;
                } else {
                    java.lang.Object objNewInstance = java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size$iv);
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(objNewInstance, "null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
                    objArr = (java.lang.Object[]) objNewInstance;
                }
                java.lang.Object[] result$iv = objArr;
                int i$iv = 0;
                while (true) {
                    int i$iv2 = i$iv + 1;
                    result$iv[i$iv] = it.next();
                    if (i$iv2 >= result$iv.length) {
                        if (!it.hasNext()) {
                            return result$iv;
                        }
                        int newSize$iv = ((i$iv2 * 3) + 1) >>> 1;
                        if (newSize$iv <= i$iv2) {
                            if (i$iv2 >= MAX_SIZE) {
                                throw new java.lang.OutOfMemoryError();
                            }
                            newSize$iv = MAX_SIZE;
                        }
                        java.lang.Object[] objArrCopyOf2 = java.util.Arrays.copyOf(result$iv, newSize$iv);
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf2, "copyOf(...)");
                        result$iv = objArrCopyOf2;
                        i$iv = i$iv2;
                    } else {
                        if (!it.hasNext()) {
                            java.lang.Object[] result = result$iv;
                            if (result == a) {
                                a[i$iv2] = null;
                                objArrCopyOf = a;
                            } else {
                                objArrCopyOf = java.util.Arrays.copyOf(result, i$iv2);
                                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf, "copyOf(...)");
                            }
                            java.lang.Object[] result$iv2 = objArrCopyOf;
                            return result$iv2;
                        }
                        i$iv = i$iv2;
                    }
                }
            }
        }
        return a;
    }

    /* JADX WARN: Type inference failed for: r3v3, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r3v4, types: [java.lang.Object, java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    private static final java.lang.Object[] toArrayImpl(java.util.Collection<?> collection, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<java.lang.Object[]> function0, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.Integer, java.lang.Object[]> function1, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Object[], ? super java.lang.Integer, java.lang.Object[]> function2) {
        int size = collection.size();
        if (size == 0) {
            return function0.invoke();
        }
        java.util.Iterator<?> it = collection.iterator();
        if (!it.hasNext()) {
            return function0.invoke();
        }
        java.lang.Object[] objArrInvoke = function1.invoke(java.lang.Integer.valueOf(size));
        int i = 0;
        while (true) {
            int i2 = i + 1;
            objArrInvoke[i] = it.next();
            if (i2 >= objArrInvoke.length) {
                if (!it.hasNext()) {
                    return objArrInvoke;
                }
                int newSize = ((i2 * 3) + 1) >>> 1;
                if (newSize <= i2) {
                    if (i2 >= MAX_SIZE) {
                        throw new java.lang.OutOfMemoryError();
                    }
                    newSize = MAX_SIZE;
                }
                java.lang.Object[] objArrCopyOf = java.util.Arrays.copyOf((java.lang.Object[]) objArrInvoke, newSize);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf, "copyOf(...)");
                objArrInvoke = objArrCopyOf;
                i = i2;
            } else {
                if (!it.hasNext()) {
                    return function2.invoke(objArrInvoke, java.lang.Integer.valueOf(i2));
                }
                i = i2;
            }
        }
    }
}
