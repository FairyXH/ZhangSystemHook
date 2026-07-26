package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class AdditionalSubtypeMap {
    static final com.android.server.inputmethod.AdditionalSubtypeMap EMPTY_MAP = new com.android.server.inputmethod.AdditionalSubtypeMap(new android.util.ArrayMap());
    private final android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> mMap;

    private static com.android.server.inputmethod.AdditionalSubtypeMap createOrEmpty(android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> map) {
        return map.isEmpty() ? EMPTY_MAP : new com.android.server.inputmethod.AdditionalSubtypeMap(map);
    }

    static com.android.server.inputmethod.AdditionalSubtypeMap of(android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> map) {
        return createOrEmpty(map);
    }

    com.android.server.inputmethod.AdditionalSubtypeMap cloneWithRemoveOrSelf(java.lang.String key) {
        if (isEmpty() || !containsKey(key)) {
            return this;
        }
        android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> newMap = new android.util.ArrayMap<>(this.mMap);
        newMap.remove(key);
        return createOrEmpty(newMap);
    }

    com.android.server.inputmethod.AdditionalSubtypeMap cloneWithRemoveOrSelf(java.util.Collection<java.lang.String> keys) {
        if (isEmpty()) {
            return this;
        }
        android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> newMap = new android.util.ArrayMap<>(this.mMap);
        return newMap.removeAll(keys) ? createOrEmpty(newMap) : this;
    }

    com.android.server.inputmethod.AdditionalSubtypeMap cloneWithPut(java.lang.String key, java.util.List<android.view.inputmethod.InputMethodSubtype> value) {
        android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> newMap = new android.util.ArrayMap<>(this.mMap);
        newMap.put(key, value);
        return new com.android.server.inputmethod.AdditionalSubtypeMap(newMap);
    }

    private AdditionalSubtypeMap(android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> map) {
        this.mMap = map;
    }

    java.util.List<android.view.inputmethod.InputMethodSubtype> get(java.lang.String key) {
        return this.mMap.get(key);
    }

    boolean containsKey(java.lang.String key) {
        return this.mMap.containsKey(key);
    }

    boolean isEmpty() {
        return this.mMap.isEmpty();
    }

    java.util.Collection<java.lang.String> keySet() {
        return this.mMap.keySet();
    }

    int size() {
        return this.mMap.size();
    }
}
