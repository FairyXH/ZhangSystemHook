package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class CircularQueue<K, V> extends java.util.LinkedList<K> {
    private final android.util.ArrayMap<K, V> mArrayMap = new android.util.ArrayMap<>();
    private final int mLimit;

    public CircularQueue(int limit) {
        this.mLimit = limit;
    }

    @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
    public boolean add(K k) throws java.lang.IllegalArgumentException {
        throw new java.lang.IllegalArgumentException("Call of add(key) prohibited. Please call put(key, value) instead. ");
    }

    public V put(K key, V value) {
        super.add(key);
        this.mArrayMap.put(key, value);
        V removedValue = null;
        while (size() > this.mLimit) {
            removedValue = this.mArrayMap.remove(super.remove());
        }
        return removedValue;
    }

    public V removeElement(K key) {
        super.remove(key);
        return this.mArrayMap.remove(key);
    }

    public V getElement(K key) {
        return this.mArrayMap.get(key);
    }

    public boolean containsKey(K key) {
        return this.mArrayMap.containsKey(key);
    }

    public java.util.Collection<V> values() {
        return this.mArrayMap.values();
    }
}
