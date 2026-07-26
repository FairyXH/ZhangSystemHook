package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class TypeIntrinsics {
    private static <T extends java.lang.Throwable> T sanitizeStackTrace(T t) {
        return (T) kotlin.jvm.internal.Intrinsics.sanitizeStackTrace(t, kotlin.jvm.internal.TypeIntrinsics.class.getName());
    }

    public static void throwCce(java.lang.Object argument, java.lang.String requestedClassName) {
        java.lang.String argumentClassName = argument == null ? "null" : argument.getClass().getName();
        throwCce(argumentClassName + " cannot be cast to " + requestedClassName);
    }

    public static void throwCce(java.lang.String message) {
        throw throwCce(new java.lang.ClassCastException(message));
    }

    public static java.lang.ClassCastException throwCce(java.lang.ClassCastException e) {
        throw ((java.lang.ClassCastException) sanitizeStackTrace(e));
    }

    public static boolean isMutableIterator(java.lang.Object obj) {
        return (obj instanceof java.util.Iterator) && (!(obj instanceof kotlin.jvm.internal.markers.KMappedMarker) || (obj instanceof kotlin.jvm.internal.markers.KMutableIterator));
    }

    public static java.util.Iterator asMutableIterator(java.lang.Object obj) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableIterator)) {
            throwCce(obj, "kotlin.collections.MutableIterator");
        }
        return castToIterator(obj);
    }

    public static java.util.Iterator asMutableIterator(java.lang.Object obj, java.lang.String message) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableIterator)) {
            throwCce(message);
        }
        return castToIterator(obj);
    }

    public static java.util.Iterator castToIterator(java.lang.Object obj) {
        try {
            return (java.util.Iterator) obj;
        } catch (java.lang.ClassCastException e) {
            throw throwCce(e);
        }
    }

    public static boolean isMutableListIterator(java.lang.Object obj) {
        return (obj instanceof java.util.ListIterator) && (!(obj instanceof kotlin.jvm.internal.markers.KMappedMarker) || (obj instanceof kotlin.jvm.internal.markers.KMutableListIterator));
    }

    public static java.util.ListIterator asMutableListIterator(java.lang.Object obj) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableListIterator)) {
            throwCce(obj, "kotlin.collections.MutableListIterator");
        }
        return castToListIterator(obj);
    }

    public static java.util.ListIterator asMutableListIterator(java.lang.Object obj, java.lang.String message) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableListIterator)) {
            throwCce(message);
        }
        return castToListIterator(obj);
    }

    public static java.util.ListIterator castToListIterator(java.lang.Object obj) {
        try {
            return (java.util.ListIterator) obj;
        } catch (java.lang.ClassCastException e) {
            throw throwCce(e);
        }
    }

    public static boolean isMutableIterable(java.lang.Object obj) {
        return (obj instanceof java.lang.Iterable) && (!(obj instanceof kotlin.jvm.internal.markers.KMappedMarker) || (obj instanceof kotlin.jvm.internal.markers.KMutableIterable));
    }

    public static java.lang.Iterable asMutableIterable(java.lang.Object obj) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableIterable)) {
            throwCce(obj, "kotlin.collections.MutableIterable");
        }
        return castToIterable(obj);
    }

    public static java.lang.Iterable asMutableIterable(java.lang.Object obj, java.lang.String message) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableIterable)) {
            throwCce(message);
        }
        return castToIterable(obj);
    }

    public static java.lang.Iterable castToIterable(java.lang.Object obj) {
        try {
            return (java.lang.Iterable) obj;
        } catch (java.lang.ClassCastException e) {
            throw throwCce(e);
        }
    }

    public static boolean isMutableCollection(java.lang.Object obj) {
        return (obj instanceof java.util.Collection) && (!(obj instanceof kotlin.jvm.internal.markers.KMappedMarker) || (obj instanceof kotlin.jvm.internal.markers.KMutableCollection));
    }

    public static java.util.Collection asMutableCollection(java.lang.Object obj) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableCollection)) {
            throwCce(obj, "kotlin.collections.MutableCollection");
        }
        return castToCollection(obj);
    }

    public static java.util.Collection asMutableCollection(java.lang.Object obj, java.lang.String message) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableCollection)) {
            throwCce(message);
        }
        return castToCollection(obj);
    }

    public static java.util.Collection castToCollection(java.lang.Object obj) {
        try {
            return (java.util.Collection) obj;
        } catch (java.lang.ClassCastException e) {
            throw throwCce(e);
        }
    }

    public static boolean isMutableList(java.lang.Object obj) {
        return (obj instanceof java.util.List) && (!(obj instanceof kotlin.jvm.internal.markers.KMappedMarker) || (obj instanceof kotlin.jvm.internal.markers.KMutableList));
    }

    public static java.util.List asMutableList(java.lang.Object obj) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableList)) {
            throwCce(obj, "kotlin.collections.MutableList");
        }
        return castToList(obj);
    }

    public static java.util.List asMutableList(java.lang.Object obj, java.lang.String message) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableList)) {
            throwCce(message);
        }
        return castToList(obj);
    }

    public static java.util.List castToList(java.lang.Object obj) {
        try {
            return (java.util.List) obj;
        } catch (java.lang.ClassCastException e) {
            throw throwCce(e);
        }
    }

    public static boolean isMutableSet(java.lang.Object obj) {
        return (obj instanceof java.util.Set) && (!(obj instanceof kotlin.jvm.internal.markers.KMappedMarker) || (obj instanceof kotlin.jvm.internal.markers.KMutableSet));
    }

    public static java.util.Set asMutableSet(java.lang.Object obj) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableSet)) {
            throwCce(obj, "kotlin.collections.MutableSet");
        }
        return castToSet(obj);
    }

    public static java.util.Set asMutableSet(java.lang.Object obj, java.lang.String message) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableSet)) {
            throwCce(message);
        }
        return castToSet(obj);
    }

    public static java.util.Set castToSet(java.lang.Object obj) {
        try {
            return (java.util.Set) obj;
        } catch (java.lang.ClassCastException e) {
            throw throwCce(e);
        }
    }

    public static boolean isMutableMap(java.lang.Object obj) {
        return (obj instanceof java.util.Map) && (!(obj instanceof kotlin.jvm.internal.markers.KMappedMarker) || (obj instanceof kotlin.jvm.internal.markers.KMutableMap));
    }

    public static java.util.Map asMutableMap(java.lang.Object obj) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableMap)) {
            throwCce(obj, "kotlin.collections.MutableMap");
        }
        return castToMap(obj);
    }

    public static java.util.Map asMutableMap(java.lang.Object obj, java.lang.String message) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableMap)) {
            throwCce(message);
        }
        return castToMap(obj);
    }

    public static java.util.Map castToMap(java.lang.Object obj) {
        try {
            return (java.util.Map) obj;
        } catch (java.lang.ClassCastException e) {
            throw throwCce(e);
        }
    }

    public static boolean isMutableMapEntry(java.lang.Object obj) {
        return (obj instanceof java.util.Map.Entry) && (!(obj instanceof kotlin.jvm.internal.markers.KMappedMarker) || (obj instanceof kotlin.jvm.internal.markers.KMutableMap.Entry));
    }

    public static java.util.Map.Entry asMutableMapEntry(java.lang.Object obj) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableMap.Entry)) {
            throwCce(obj, "kotlin.collections.MutableMap.MutableEntry");
        }
        return castToMapEntry(obj);
    }

    public static java.util.Map.Entry asMutableMapEntry(java.lang.Object obj, java.lang.String message) {
        if ((obj instanceof kotlin.jvm.internal.markers.KMappedMarker) && !(obj instanceof kotlin.jvm.internal.markers.KMutableMap.Entry)) {
            throwCce(message);
        }
        return castToMapEntry(obj);
    }

    public static java.util.Map.Entry castToMapEntry(java.lang.Object obj) {
        try {
            return (java.util.Map.Entry) obj;
        } catch (java.lang.ClassCastException e) {
            throw throwCce(e);
        }
    }

    public static int getFunctionArity(java.lang.Object obj) {
        if (obj instanceof kotlin.jvm.internal.FunctionBase) {
            return ((kotlin.jvm.internal.FunctionBase) obj).getArity();
        }
        if (obj instanceof kotlin.jvm.functions.Function0) {
            return 0;
        }
        if (obj instanceof kotlin.jvm.functions.Function1) {
            return 1;
        }
        if (obj instanceof kotlin.jvm.functions.Function2) {
            return 2;
        }
        if (obj instanceof kotlin.jvm.functions.Function3) {
            return 3;
        }
        if (obj instanceof kotlin.jvm.functions.Function4) {
            return 4;
        }
        if (obj instanceof kotlin.jvm.functions.Function5) {
            return 5;
        }
        if (obj instanceof kotlin.jvm.functions.Function6) {
            return 6;
        }
        if (obj instanceof kotlin.jvm.functions.Function7) {
            return 7;
        }
        if (obj instanceof kotlin.jvm.functions.Function8) {
            return 8;
        }
        if (obj instanceof kotlin.jvm.functions.Function9) {
            return 9;
        }
        if (obj instanceof kotlin.jvm.functions.Function10) {
            return 10;
        }
        if (obj instanceof kotlin.jvm.functions.Function11) {
            return 11;
        }
        if (obj instanceof kotlin.jvm.functions.Function12) {
            return 12;
        }
        if (obj instanceof kotlin.jvm.functions.Function13) {
            return 13;
        }
        if (obj instanceof kotlin.jvm.functions.Function14) {
            return 14;
        }
        if (obj instanceof kotlin.jvm.functions.Function15) {
            return 15;
        }
        if (obj instanceof kotlin.jvm.functions.Function16) {
            return 16;
        }
        if (obj instanceof kotlin.jvm.functions.Function17) {
            return 17;
        }
        if (obj instanceof kotlin.jvm.functions.Function18) {
            return 18;
        }
        if (obj instanceof kotlin.jvm.functions.Function19) {
            return 19;
        }
        if (obj instanceof kotlin.jvm.functions.Function20) {
            return 20;
        }
        if (obj instanceof kotlin.jvm.functions.Function21) {
            return 21;
        }
        if (obj instanceof kotlin.jvm.functions.Function22) {
            return 22;
        }
        return -1;
    }

    public static boolean isFunctionOfArity(java.lang.Object obj, int arity) {
        return (obj instanceof kotlin.Function) && getFunctionArity(obj) == arity;
    }

    public static java.lang.Object beforeCheckcastToFunctionOfArity(java.lang.Object obj, int arity) {
        if (obj != null && !isFunctionOfArity(obj, arity)) {
            throwCce(obj, "kotlin.jvm.functions.Function" + arity);
        }
        return obj;
    }

    public static java.lang.Object beforeCheckcastToFunctionOfArity(java.lang.Object obj, int arity, java.lang.String message) {
        if (obj != null && !isFunctionOfArity(obj, arity)) {
            throwCce(message);
        }
        return obj;
    }
}
