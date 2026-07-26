package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class ArrayMapWithHistory<K, V> {
    private static final java.lang.String TAG = "ArrayMapWithHistory";
    private android.util.ArrayMap<K, com.android.server.timezonedetector.ReferenceWithHistory<V>> mMap;
    private final int mMaxHistorySize;

    public ArrayMapWithHistory(int maxHistorySize) {
        if (maxHistorySize < 1) {
            throw new java.lang.IllegalArgumentException("maxHistorySize < 1: " + maxHistorySize);
        }
        this.mMaxHistorySize = maxHistorySize;
    }

    public V put(K key, V value) {
        if (this.mMap == null) {
            this.mMap = new android.util.ArrayMap<>();
        }
        com.android.server.timezonedetector.ReferenceWithHistory<V> valueHolder = this.mMap.get(key);
        if (valueHolder == null) {
            valueHolder = new com.android.server.timezonedetector.ReferenceWithHistory<>(this.mMaxHistorySize);
            this.mMap.put(key, valueHolder);
        } else if (valueHolder.getHistoryCount() == 0) {
            android.util.Log.w(TAG, "History for \"" + key + "\" was unexpectedly empty");
        }
        return valueHolder.set(value);
    }

    public V get(java.lang.Object key) {
        com.android.server.timezonedetector.ReferenceWithHistory<V> valueHolder;
        if (this.mMap == null || (valueHolder = this.mMap.get(key)) == null) {
            return null;
        }
        if (valueHolder.getHistoryCount() == 0) {
            android.util.Log.w(TAG, "History for \"" + key + "\" was unexpectedly empty");
        }
        return valueHolder.get();
    }

    public int size() {
        if (this.mMap == null) {
            return 0;
        }
        return this.mMap.size();
    }

    public K keyAt(int index) {
        if (this.mMap == null) {
            throw new java.lang.ArrayIndexOutOfBoundsException(index);
        }
        return this.mMap.keyAt(index);
    }

    public V valueAt(int index) {
        if (this.mMap == null) {
            throw new java.lang.ArrayIndexOutOfBoundsException(index);
        }
        com.android.server.timezonedetector.ReferenceWithHistory<V> valueHolder = this.mMap.valueAt(index);
        if (valueHolder == null || valueHolder.getHistoryCount() == 0) {
            android.util.Log.w(TAG, "valueAt(" + index + ") was unexpectedly null or empty");
            return null;
        }
        return valueHolder.get();
    }

    public void dump(android.util.IndentingPrintWriter ipw) {
        if (this.mMap == null) {
            ipw.println("{Empty}");
        } else {
            for (int i = 0; i < this.mMap.size(); i++) {
                ipw.println("key idx: " + i + "=" + this.mMap.keyAt(i));
                com.android.server.timezonedetector.ReferenceWithHistory<V> value = this.mMap.valueAt(i);
                ipw.println("val idx: " + i + "=" + value);
                ipw.increaseIndent();
                ipw.println("Historic values=[");
                ipw.increaseIndent();
                value.dump(ipw);
                ipw.decreaseIndent();
                ipw.println("]");
                ipw.decreaseIndent();
            }
        }
        ipw.flush();
    }

    public int getHistoryCountForKeyForTests(K key) {
        com.android.server.timezonedetector.ReferenceWithHistory<V> valueHolder;
        if (this.mMap == null || (valueHolder = this.mMap.get(key)) == null) {
            return 0;
        }
        if (valueHolder.getHistoryCount() == 0) {
            android.util.Log.w(TAG, "getValuesSizeForKeyForTests(\"" + key + "\") was unexpectedly empty");
            return 0;
        }
        return valueHolder.getHistoryCount();
    }

    public java.lang.String toString() {
        return "ArrayMapWithHistory{mHistorySize=" + this.mMaxHistorySize + ", mMap=" + this.mMap + '}';
    }
}
