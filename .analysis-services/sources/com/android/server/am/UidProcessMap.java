package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class UidProcessMap<E> {
    final android.util.SparseArray<android.util.ArrayMap<java.lang.String, E>> mMap = new android.util.SparseArray<>();

    public E get(int uid, java.lang.String name) {
        android.util.ArrayMap<java.lang.String, E> names = this.mMap.get(uid);
        if (names == null) {
            return null;
        }
        return names.get(name);
    }

    public E put(int uid, java.lang.String name, E value) {
        android.util.ArrayMap<java.lang.String, E> names = this.mMap.get(uid);
        if (names == null) {
            names = new android.util.ArrayMap<>(2);
            this.mMap.put(uid, names);
        }
        names.put(name, value);
        return value;
    }

    public E remove(int uid, java.lang.String name) {
        android.util.ArrayMap<java.lang.String, E> names;
        int index = this.mMap.indexOfKey(uid);
        if (index < 0 || (names = this.mMap.valueAt(index)) == null) {
            return null;
        }
        E old = names.remove(name);
        if (names.isEmpty()) {
            this.mMap.removeAt(index);
        }
        return old;
    }

    public android.util.SparseArray<android.util.ArrayMap<java.lang.String, E>> getMap() {
        return this.mMap;
    }

    public int size() {
        return this.mMap.size();
    }

    public void clear() {
        this.mMap.clear();
    }

    public void putAll(com.android.server.am.UidProcessMap<E> other) {
        for (int i = other.mMap.size() - 1; i >= 0; i--) {
            int uid = other.mMap.keyAt(i);
            android.util.ArrayMap<java.lang.String, E> names = this.mMap.get(uid);
            if (names != null) {
                names.putAll((android.util.ArrayMap<? extends java.lang.String, ? extends E>) other.mMap.valueAt(i));
            } else {
                this.mMap.put(uid, new android.util.ArrayMap<>(other.mMap.valueAt(i)));
            }
        }
    }
}
