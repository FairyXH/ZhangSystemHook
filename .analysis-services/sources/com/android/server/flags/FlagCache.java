package com.android.server.flags;

/* JADX INFO: loaded from: classes2.dex */
public class FlagCache<V> {
    private final java.util.function.Function<java.lang.String, java.util.HashMap<java.lang.String, V>> mNewHashMap = new java.util.function.Function() { // from class: com.android.server.flags.FlagCache$$ExternalSyntheticLambda0
        @Override // java.util.function.Function
        public final java.lang.Object apply(java.lang.Object obj) {
            return com.android.server.flags.FlagCache.lambda$new$0((java.lang.String) obj);
        }
    };
    final java.util.Map<java.lang.String, java.util.Map<java.lang.String, V>> mCache = new java.util.HashMap();

    static /* synthetic */ java.util.HashMap lambda$new$0(java.lang.String k) {
        return new java.util.HashMap();
    }

    FlagCache() {
    }

    boolean containsNamespace(java.lang.String namespace) {
        boolean zContainsKey;
        synchronized (this.mCache) {
            zContainsKey = this.mCache.containsKey(namespace);
        }
        return zContainsKey;
    }

    boolean contains(java.lang.String namespace, java.lang.String name) {
        boolean z;
        synchronized (this.mCache) {
            java.util.Map<java.lang.String, V> nsCache = this.mCache.get(namespace);
            z = nsCache != null && nsCache.containsKey(name);
        }
        return z;
    }

    boolean setIfChanged(java.lang.String namespace, java.lang.String name, V value) {
        synchronized (this.mCache) {
            java.util.Map<java.lang.String, V> nsCache = this.mCache.computeIfAbsent(namespace, this.mNewHashMap);
            V curValue = nsCache.get(name);
            if (curValue != null && curValue.equals(value)) {
                return false;
            }
            nsCache.put(name, value);
            return true;
        }
    }

    V getOrSet(java.lang.String namespace, java.lang.String name, V defaultValue) {
        V v;
        synchronized (this.mCache) {
            java.util.Map<java.lang.String, V> nsCache = this.mCache.computeIfAbsent(namespace, this.mNewHashMap);
            V value = nsCache.putIfAbsent(name, defaultValue);
            v = value == null ? defaultValue : value;
        }
        return v;
    }

    V getOrNull(java.lang.String namespace, java.lang.String name) {
        synchronized (this.mCache) {
            java.util.Map<java.lang.String, V> nsCache = this.mCache.get(namespace);
            if (nsCache == null) {
                return null;
            }
            return nsCache.get(name);
        }
    }
}
