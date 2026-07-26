package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: Arrays.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000H\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a5\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u00032\u0010\u0010\u0004\u001a\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001¢\u0006\u0004\b\u0005\u0010\u0006\u001a#\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001¢\u0006\u0004\b\t\u0010\n\u001a?\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\n\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\u0010\u0010\u0010\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00030\u0011H\u0002¢\u0006\u0004\b\u0012\u0010\u0013\u001a+\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0015\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00030\u0003¢\u0006\u0002\u0010\u0016\u001a;\u0010\u0017\u001a\u0002H\u0018\"\u0010\b\u0000\u0010\u0019*\u0006\u0012\u0002\b\u00030\u0003*\u0002H\u0018\"\u0004\b\u0001\u0010\u0018*\u0002H\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00180\u001bH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a)\u0010\u001d\u001a\u00020\u0001*\b\u0012\u0002\b\u0003\u0018\u00010\u0003H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000¢\u0006\u0002\u0010\u001e\u001aG\u0010\u001f\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180\u00150 \"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0018*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00180 0\u0003¢\u0006\u0002\u0010!\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\""}, d2 = {"contentDeepEqualsImpl", "", "T", "", "other", "contentDeepEquals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepToStringImpl", "", "contentDeepToString", "([Ljava/lang/Object;)Ljava/lang/String;", "contentDeepToStringInternal", "", "result", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "processed", "", "contentDeepToStringInternal$ArraysKt__ArraysKt", "([Ljava/lang/Object;Ljava/lang/StringBuilder;Ljava/util/List;)V", "flatten", "", "([[Ljava/lang/Object;)Ljava/util/List;", "ifEmpty", "R", "C", "defaultValue", "Lkotlin/Function0;", "([Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNullOrEmpty", "([Ljava/lang/Object;)Z", "unzip", "Lkotlin/Pair;", "([Lkotlin/Pair;)Lkotlin/Pair;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/ArraysKt")
class ArraysKt__ArraysKt extends com.android.server.permission.jarjar.kotlin.collections.ArraysKt__ArraysJVMKt {
    public static final <T> java.util.List<T> flatten(T[][] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "<this>");
        int length = 0;
        for (java.lang.Object[] it : tArr) {
            length += it.length;
        }
        java.util.ArrayList result = new java.util.ArrayList(length);
        for (T[] tArr2 : tArr) {
            com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.addAll(result, tArr2);
        }
        return result;
    }

    public static final <T, R> com.android.server.permission.jarjar.kotlin.Pair<java.util.List<T>, java.util.List<R>> unzip(com.android.server.permission.jarjar.kotlin.Pair<? extends T, ? extends R>[] pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "<this>");
        java.util.ArrayList listT = new java.util.ArrayList(pairArr.length);
        java.util.ArrayList listR = new java.util.ArrayList(pairArr.length);
        for (com.android.server.permission.jarjar.kotlin.Pair<? extends T, ? extends R> pair : pairArr) {
            listT.add(pair.getFirst());
            listR.add(pair.getSecond());
        }
        return com.android.server.permission.jarjar.kotlin.TuplesKt.to(listT, listR);
    }

    private static final boolean isNullOrEmpty(java.lang.Object[] $this$isNullOrEmpty) {
        if ($this$isNullOrEmpty != null) {
            return $this$isNullOrEmpty.length == 0;
        }
        return true;
    }

    /* JADX WARN: Incorrect types in method signature: <C:[Ljava/lang/Object;:TR;R:Ljava/lang/Object;>(TC;Lcom/android/server/permission/jarjar/kotlin/jvm/functions/Function0<+TR;>;)TR; */
    private static final java.lang.Object ifEmpty(java.lang.Object[] $this$ifEmpty, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0 defaultValue) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return $this$ifEmpty.length == 0 ? defaultValue.invoke() : $this$ifEmpty;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> boolean contentDeepEquals(T[] tArr, T[] tArr2) {
        if (tArr == tArr2) {
            return true;
        }
        if (tArr == 0 || tArr2 == 0 || tArr.length != tArr2.length) {
            return false;
        }
        int length = tArr.length;
        for (int i = 0; i < length; i++) {
            java.lang.Object[] objArr = tArr[i];
            java.lang.Object[] objArr2 = tArr2[i];
            if (objArr != objArr2) {
                if (objArr == 0 || objArr2 == 0) {
                    return false;
                }
                if ((objArr instanceof java.lang.Object[]) && (objArr2 instanceof java.lang.Object[])) {
                    if (!com.android.server.permission.jarjar.kotlin.collections.ArraysKt.contentDeepEquals(objArr, objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof byte[]) && (objArr2 instanceof byte[])) {
                    if (!java.util.Arrays.equals((byte[]) objArr, (byte[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof short[]) && (objArr2 instanceof short[])) {
                    if (!java.util.Arrays.equals((short[]) objArr, (short[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof int[]) && (objArr2 instanceof int[])) {
                    if (!java.util.Arrays.equals((int[]) objArr, (int[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof long[]) && (objArr2 instanceof long[])) {
                    if (!java.util.Arrays.equals((long[]) objArr, (long[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof float[]) && (objArr2 instanceof float[])) {
                    if (!java.util.Arrays.equals((float[]) objArr, (float[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof double[]) && (objArr2 instanceof double[])) {
                    if (!java.util.Arrays.equals((double[]) objArr, (double[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof char[]) && (objArr2 instanceof char[])) {
                    if (!java.util.Arrays.equals((char[]) objArr, (char[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof boolean[]) && (objArr2 instanceof boolean[])) {
                    if (!java.util.Arrays.equals((boolean[]) objArr, (boolean[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof com.android.server.permission.jarjar.kotlin.UByteArray) && (objArr2 instanceof com.android.server.permission.jarjar.kotlin.UByteArray)) {
                    if (!com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6659contentEqualskV0jMPg(((com.android.server.permission.jarjar.kotlin.UByteArray) objArr).m6174unboximpl(), ((com.android.server.permission.jarjar.kotlin.UByteArray) objArr2).m6174unboximpl())) {
                        return false;
                    }
                } else if ((objArr instanceof com.android.server.permission.jarjar.kotlin.UShortArray) && (objArr2 instanceof com.android.server.permission.jarjar.kotlin.UShortArray)) {
                    if (!com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6657contentEqualsFGO6Aew(((com.android.server.permission.jarjar.kotlin.UShortArray) objArr).m6437unboximpl(), ((com.android.server.permission.jarjar.kotlin.UShortArray) objArr2).m6437unboximpl())) {
                        return false;
                    }
                } else if ((objArr instanceof com.android.server.permission.jarjar.kotlin.UIntArray) && (objArr2 instanceof com.android.server.permission.jarjar.kotlin.UIntArray)) {
                    if (!com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6658contentEqualsKJPZfPQ(((com.android.server.permission.jarjar.kotlin.UIntArray) objArr).m6253unboximpl(), ((com.android.server.permission.jarjar.kotlin.UIntArray) objArr2).m6253unboximpl())) {
                        return false;
                    }
                } else if ((objArr instanceof com.android.server.permission.jarjar.kotlin.ULongArray) && (objArr2 instanceof com.android.server.permission.jarjar.kotlin.ULongArray)) {
                    if (!com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6660contentEqualslec5QzE(((com.android.server.permission.jarjar.kotlin.ULongArray) objArr).m6332unboximpl(), ((com.android.server.permission.jarjar.kotlin.ULongArray) objArr2).m6332unboximpl())) {
                        return false;
                    }
                } else if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(objArr, objArr2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static final <T> java.lang.String contentDeepToString(T[] tArr) {
        if (tArr == null) {
            return "null";
        }
        int length = (com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceAtMost(tArr.length, 429496729) * 5) + 2;
        java.lang.StringBuilder $this$contentDeepToStringImpl_u24lambda_u242 = new java.lang.StringBuilder(length);
        contentDeepToStringInternal$ArraysKt__ArraysKt(tArr, $this$contentDeepToStringImpl_u24lambda_u242, new java.util.ArrayList());
        java.lang.String string = $this$contentDeepToStringImpl_u24lambda_u242.toString();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> void contentDeepToStringInternal$ArraysKt__ArraysKt(T[] tArr, java.lang.StringBuilder sb, java.util.List<java.lang.Object[]> list) {
        if (list.contains(tArr)) {
            sb.append("[...]");
            return;
        }
        list.add(tArr);
        sb.append('[');
        int length = tArr.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            java.lang.Object[] objArr = tArr[i];
            if (objArr == 0) {
                sb.append("null");
            } else if (objArr instanceof java.lang.Object[]) {
                contentDeepToStringInternal$ArraysKt__ArraysKt(objArr, sb, list);
            } else if (objArr instanceof byte[]) {
                java.lang.String string = java.util.Arrays.toString((byte[]) objArr);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                sb.append(string);
            } else if (objArr instanceof short[]) {
                java.lang.String string2 = java.util.Arrays.toString((short[]) objArr);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
                sb.append(string2);
            } else if (objArr instanceof int[]) {
                java.lang.String string3 = java.util.Arrays.toString((int[]) objArr);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string3, "toString(...)");
                sb.append(string3);
            } else if (objArr instanceof long[]) {
                java.lang.String string4 = java.util.Arrays.toString((long[]) objArr);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string4, "toString(...)");
                sb.append(string4);
            } else if (objArr instanceof float[]) {
                java.lang.String string5 = java.util.Arrays.toString((float[]) objArr);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string5, "toString(...)");
                sb.append(string5);
            } else if (objArr instanceof double[]) {
                java.lang.String string6 = java.util.Arrays.toString((double[]) objArr);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string6, "toString(...)");
                sb.append(string6);
            } else if (objArr instanceof char[]) {
                java.lang.String string7 = java.util.Arrays.toString((char[]) objArr);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string7, "toString(...)");
                sb.append(string7);
            } else if (objArr instanceof boolean[]) {
                java.lang.String string8 = java.util.Arrays.toString((boolean[]) objArr);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string8, "toString(...)");
                sb.append(string8);
            } else if (objArr instanceof com.android.server.permission.jarjar.kotlin.UByteArray) {
                com.android.server.permission.jarjar.kotlin.UByteArray uByteArray = (com.android.server.permission.jarjar.kotlin.UByteArray) objArr;
                sb.append(com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6665contentToString2csIQuQ(uByteArray != null ? uByteArray.m6174unboximpl() : null));
            } else if (objArr instanceof com.android.server.permission.jarjar.kotlin.UShortArray) {
                com.android.server.permission.jarjar.kotlin.UShortArray uShortArray = (com.android.server.permission.jarjar.kotlin.UShortArray) objArr;
                sb.append(com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6667contentToStringd6D3K8(uShortArray != null ? uShortArray.m6437unboximpl() : null));
            } else if (objArr instanceof com.android.server.permission.jarjar.kotlin.UIntArray) {
                com.android.server.permission.jarjar.kotlin.UIntArray uIntArray = (com.android.server.permission.jarjar.kotlin.UIntArray) objArr;
                sb.append(com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6666contentToStringXUkPCBk(uIntArray != null ? uIntArray.m6253unboximpl() : null));
            } else if (objArr instanceof com.android.server.permission.jarjar.kotlin.ULongArray) {
                com.android.server.permission.jarjar.kotlin.ULongArray uLongArray = (com.android.server.permission.jarjar.kotlin.ULongArray) objArr;
                sb.append(com.android.server.permission.jarjar.kotlin.collections.unsigned.UArraysKt.m6668contentToStringuLth9ew(uLongArray != null ? uLongArray.m6332unboximpl() : null));
            } else {
                sb.append(objArr.toString());
            }
        }
        sb.append(']');
        list.remove(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list));
    }
}
