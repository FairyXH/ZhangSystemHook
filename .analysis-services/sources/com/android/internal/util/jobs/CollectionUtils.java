package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public class CollectionUtils {
    private CollectionUtils() {
    }

    public static <T> boolean contains(java.util.Collection<T> collection, T element) {
        return collection != null && collection.contains(element);
    }

    public static <T> java.util.List<T> filter(java.util.List<T> list, java.util.function.Predicate<? super T> predicate) {
        java.util.ArrayList<T> result = null;
        for (int i = 0; i < size(list); i++) {
            T item = list.get(i);
            if (predicate.test(item)) {
                result = com.android.internal.util.jobs.ArrayUtils.add(result, item);
            }
        }
        return emptyIfNull(result);
    }

    public static <T> java.util.Set<T> filter(java.util.Set<T> set, java.util.function.Predicate<? super T> predicate) {
        if (set == null || set.size() == 0) {
            return java.util.Collections.emptySet();
        }
        android.util.ArraySet<T> arraySetAdd = null;
        if (set instanceof android.util.ArraySet) {
            android.util.ArraySet arraySet = (android.util.ArraySet) set;
            int size = arraySet.size();
            for (int i = 0; i < size; i++) {
                android.R.anim animVar = (T) arraySet.valueAt(i);
                if (predicate.test(animVar)) {
                    arraySetAdd = com.android.internal.util.jobs.ArrayUtils.add((android.util.ArraySet<android.R.anim>) arraySetAdd, animVar);
                }
            }
        } else {
            for (java.lang.Object obj : set) {
                if (predicate.test(obj)) {
                    arraySetAdd = com.android.internal.util.jobs.ArrayUtils.add(arraySetAdd, obj);
                }
            }
        }
        return emptyIfNull(arraySetAdd);
    }

    public static <T> void addIf(java.util.List<T> source, java.util.Collection<? super T> dest, java.util.function.Predicate<? super T> predicate) {
        for (int i = 0; i < size(source); i++) {
            T item = source.get(i);
            if (predicate.test(item)) {
                dest.add(item);
            }
        }
    }

    public static <I, O> java.util.List<O> map(java.util.List<I> cur, java.util.function.Function<? super I, ? extends O> f) {
        if (isEmpty(cur)) {
            return java.util.Collections.emptyList();
        }
        int size = cur.size();
        java.util.ArrayList<O> result = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(f.apply(cur.get(i)));
        }
        return result;
    }

    public static <I, O> java.util.Set<O> map(java.util.Set<I> set, java.util.function.Function<? super I, ? extends O> function) {
        if (isEmpty(set)) {
            return java.util.Collections.emptySet();
        }
        android.util.ArraySet arraySet = new android.util.ArraySet(set.size());
        if (set instanceof android.util.ArraySet) {
            android.util.ArraySet arraySet2 = (android.util.ArraySet) set;
            int size = arraySet2.size();
            for (int i = 0; i < size; i++) {
                arraySet.add(function.apply((I) arraySet2.valueAt(i)));
            }
        } else {
            java.util.Iterator<I> it = set.iterator();
            while (it.hasNext()) {
                arraySet.add(function.apply(it.next()));
            }
        }
        return arraySet;
    }

    public static <I, O> java.util.List<O> mapNotNull(java.util.List<I> cur, java.util.function.Function<? super I, ? extends O> f) {
        if (isEmpty(cur)) {
            return java.util.Collections.emptyList();
        }
        java.util.List<O> result = null;
        int size = cur.size();
        for (int i = 0; i < size; i++) {
            O transformed = f.apply(cur.get(i));
            if (transformed != null) {
                result = add(result, transformed);
            }
        }
        return emptyIfNull(result);
    }

    public static <T> java.util.List<T> emptyIfNull(java.util.List<T> cur) {
        return cur == null ? java.util.Collections.emptyList() : cur;
    }

    public static <T> java.util.Set<T> emptyIfNull(java.util.Set<T> cur) {
        return cur == null ? java.util.Collections.emptySet() : cur;
    }

    public static <K, V> java.util.Map<K, V> emptyIfNull(java.util.Map<K, V> cur) {
        return cur == null ? java.util.Collections.emptyMap() : cur;
    }

    public static int size(java.util.Collection<?> cur) {
        if (cur != null) {
            return cur.size();
        }
        return 0;
    }

    public static int size(java.util.Map<?, ?> cur) {
        if (cur != null) {
            return cur.size();
        }
        return 0;
    }

    public static boolean isEmpty(java.util.Collection<?> cur) {
        return size(cur) == 0;
    }

    public static boolean isEmpty(java.util.Map<?, ?> cur) {
        return size(cur) == 0;
    }

    public static <T> java.util.List<T> filter(java.util.List<?> list, java.lang.Class<T> c) {
        if (isEmpty(list)) {
            return java.util.Collections.emptyList();
        }
        java.util.ArrayList<T> result = null;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            java.lang.Object item = list.get(i);
            if (c.isInstance(item)) {
                result = com.android.internal.util.jobs.ArrayUtils.add(result, item);
            }
        }
        return emptyIfNull(result);
    }

    public static <T> boolean any(java.util.List<T> items, java.util.function.Predicate<T> predicate) {
        return find(items, predicate) != null;
    }

    public static <T> boolean any(java.util.Set<T> items, java.util.function.Predicate<T> predicate) {
        return find(items, predicate) != null;
    }

    public static <T> T find(java.util.List<T> items, java.util.function.Predicate<T> predicate) {
        if (isEmpty(items)) {
            return null;
        }
        int size = items.size();
        for (int i = 0; i < size; i++) {
            T item = items.get(i);
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public static <T> T find(java.util.Set<T> cur, java.util.function.Predicate<T> predicate) {
        int size;
        if (cur == null || predicate == null || (size = cur.size()) == 0) {
            return null;
        }
        try {
            if (cur instanceof android.util.ArraySet) {
                android.util.ArraySet<T> arraySet = (android.util.ArraySet) cur;
                for (int i = 0; i < size; i++) {
                    T item = arraySet.valueAt(i);
                    if (predicate.test(item)) {
                        return item;
                    }
                }
            } else {
                for (T t : cur) {
                    if (predicate.test(t)) {
                        return t;
                    }
                }
            }
            return null;
        } catch (java.lang.Exception e) {
            throw android.util.ExceptionUtils.propagate(e);
        }
    }

    public static <T> java.util.List<T> add(java.util.List<T> cur, T val) {
        if (cur == null || cur == java.util.Collections.emptyList()) {
            cur = new java.util.ArrayList();
        }
        cur.add(val);
        return cur;
    }

    public static <T> java.util.List<T> add(java.util.List<T> cur, int index, T val) {
        if (cur == null || cur == java.util.Collections.emptyList()) {
            cur = new java.util.ArrayList();
        }
        cur.add(index, val);
        return cur;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> java.util.Set<T> addAll(java.util.Set<T> cur, java.util.Collection<T> collection) {
        if (isEmpty((java.util.Collection<?>) collection)) {
            return cur != null ? cur : java.util.Collections.emptySet();
        }
        if (cur == null || cur == java.util.Collections.emptySet()) {
            cur = new android.util.ArraySet();
        }
        cur.addAll(collection);
        return cur;
    }

    public static <T> java.util.Set<T> add(java.util.Set<T> cur, T val) {
        if (cur == null || cur == java.util.Collections.emptySet()) {
            cur = new android.util.ArraySet();
        }
        cur.add(val);
        return cur;
    }

    public static <K, V> java.util.Map<K, V> add(java.util.Map<K, V> map, K key, V value) {
        if (map == null || map == java.util.Collections.emptyMap()) {
            map = new android.util.ArrayMap();
        }
        map.put(key, value);
        return map;
    }

    public static <T> java.util.List<T> remove(java.util.List<T> cur, T val) {
        if (isEmpty(cur)) {
            return emptyIfNull(cur);
        }
        cur.remove(val);
        return cur;
    }

    public static <T> java.util.Set<T> remove(java.util.Set<T> cur, T val) {
        if (isEmpty(cur)) {
            return emptyIfNull(cur);
        }
        cur.remove(val);
        return cur;
    }

    public static <T> java.util.List<T> copyOf(java.util.List<T> cur) {
        return isEmpty(cur) ? java.util.Collections.emptyList() : new java.util.ArrayList(cur);
    }

    public static <T> java.util.Set<T> copyOf(java.util.Set<T> cur) {
        return isEmpty(cur) ? java.util.Collections.emptySet() : new android.util.ArraySet(cur);
    }

    public static <T> java.util.Set<T> toSet(java.util.Collection<T> cur) {
        return isEmpty((java.util.Collection<?>) cur) ? java.util.Collections.emptySet() : new android.util.ArraySet(cur);
    }

    public static <T> void forEach(java.util.Set<T> cur, com.android.internal.util.jobs.FunctionalUtils.ThrowingConsumer<T> throwingConsumer) {
        int size;
        if (cur == null || throwingConsumer == null || (size = cur.size()) == 0) {
            return;
        }
        try {
            if (cur instanceof android.util.ArraySet) {
                android.util.ArraySet<T> arraySet = (android.util.ArraySet) cur;
                for (int i = 0; i < size; i++) {
                    throwingConsumer.acceptOrThrow(arraySet.valueAt(i));
                }
                return;
            }
            for (T t : cur) {
                throwingConsumer.acceptOrThrow(t);
            }
        } catch (java.lang.Exception e) {
            throw android.util.ExceptionUtils.propagate(e);
        }
    }

    public static <K, V> void forEach(java.util.Map<K, V> cur, java.util.function.BiConsumer<K, V> biConsumer) {
        int size;
        if (cur == null || biConsumer == null || (size = cur.size()) == 0) {
            return;
        }
        if (cur instanceof android.util.ArrayMap) {
            android.util.ArrayMap<K, V> arrayMap = (android.util.ArrayMap) cur;
            for (int i = 0; i < size; i++) {
                biConsumer.accept(arrayMap.keyAt(i), arrayMap.valueAt(i));
            }
            return;
        }
        for (K key : cur.keySet()) {
            biConsumer.accept(key, cur.get(key));
        }
    }

    public static <T> T firstOrNull(java.util.List<T> cur) {
        if (isEmpty(cur)) {
            return null;
        }
        return cur.get(0);
    }

    public static <T> T firstOrNull(java.util.Collection<T> cur) {
        if (isEmpty((java.util.Collection<?>) cur)) {
            return null;
        }
        return cur.iterator().next();
    }

    public static <T> java.util.List<T> singletonOrEmpty(T item) {
        return item == null ? java.util.Collections.emptyList() : java.util.Collections.singletonList(item);
    }
}
