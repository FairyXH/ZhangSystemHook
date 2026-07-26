package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputMethodMap {
    private static final android.util.ArrayMap<java.lang.String, android.view.inputmethod.InputMethodInfo> EMPTY_MAP = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.view.inputmethod.InputMethodInfo> mMap;

    static com.android.server.inputmethod.InputMethodMap emptyMap() {
        return new com.android.server.inputmethod.InputMethodMap(EMPTY_MAP);
    }

    static com.android.server.inputmethod.InputMethodMap of(android.util.ArrayMap<java.lang.String, android.view.inputmethod.InputMethodInfo> map) {
        return new com.android.server.inputmethod.InputMethodMap(map);
    }

    private InputMethodMap(android.util.ArrayMap<java.lang.String, android.view.inputmethod.InputMethodInfo> map) {
        this.mMap = map.isEmpty() ? EMPTY_MAP : new android.util.ArrayMap<>(map);
    }

    android.view.inputmethod.InputMethodInfo get(java.lang.String imeId) {
        return this.mMap.get(imeId);
    }

    java.util.List<android.view.inputmethod.InputMethodInfo> values() {
        return java.util.List.copyOf(this.mMap.values());
    }

    android.view.inputmethod.InputMethodInfo valueAt(int index) {
        return this.mMap.valueAt(index);
    }

    boolean containsKey(java.lang.String imeId) {
        return this.mMap.containsKey(imeId);
    }

    int size() {
        return this.mMap.size();
    }

    public com.android.server.inputmethod.InputMethodMap applyAdditionalSubtypes(com.android.server.inputmethod.AdditionalSubtypeMap additionalSubtypeMap) {
        if (additionalSubtypeMap.isEmpty()) {
            return this;
        }
        int size = size();
        android.util.ArrayMap<java.lang.String, android.view.inputmethod.InputMethodInfo> newMethodMap = new android.util.ArrayMap<>(size);
        boolean updated = false;
        for (int i = 0; i < size; i++) {
            android.view.inputmethod.InputMethodInfo imi = valueAt(i);
            java.lang.String imeId = imi.getId();
            java.util.List<android.view.inputmethod.InputMethodSubtype> newAdditionalSubtypes = additionalSubtypeMap.get(imeId);
            if (newAdditionalSubtypes == null || newAdditionalSubtypes.isEmpty()) {
                newMethodMap.put(imi.getId(), imi);
            } else {
                newMethodMap.put(imi.getId(), new android.view.inputmethod.InputMethodInfo(imi, newAdditionalSubtypes));
                updated = true;
            }
        }
        return updated ? of(newMethodMap) : this;
    }

    static boolean areSame(com.android.server.inputmethod.InputMethodMap map1, com.android.server.inputmethod.InputMethodMap map2) {
        if (map1 == map2) {
            return true;
        }
        int size = map1.size();
        if (size != map2.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            android.view.inputmethod.InputMethodInfo imi1 = map1.valueAt(i);
            java.lang.String imeId = imi1.getId();
            android.view.inputmethod.InputMethodInfo imi2 = map2.get(imeId);
            if (imi2 == null) {
                return false;
            }
            byte[] marshaled1 = com.android.server.inputmethod.InputMethodInfoUtils.marshal(imi1);
            byte[] marshaled2 = com.android.server.inputmethod.InputMethodInfoUtils.marshal(imi2);
            if (!java.util.Arrays.equals(marshaled1, marshaled2)) {
                return false;
            }
        }
        return true;
    }
}
