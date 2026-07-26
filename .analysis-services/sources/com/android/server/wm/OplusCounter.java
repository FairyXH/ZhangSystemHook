package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class OplusCounter {
    private static final java.lang.String TAG = "OplusCounter";
    private com.android.server.wm.OplusCounter.EffectiveInteger count;
    private final java.util.HashMap<java.lang.String, com.android.server.wm.OplusCounter.EffectiveInteger> mMap = new java.util.HashMap<>();
    private java.lang.String name;

    public OplusCounter(java.lang.String string, int val) {
        this.name = string;
        this.count = new com.android.server.wm.OplusCounter.EffectiveInteger(val);
        this.mMap.put(this.name, this.count);
    }

    public OplusCounter(com.android.server.wm.OplusCounter copy) {
    }

    public OplusCounter() {
    }

    public static final class EffectiveInteger {
        private int val;

        public EffectiveInteger(int val) {
            this.val = val;
        }

        public int getInt() {
            return this.val;
        }

        public void setInt(int val) {
            this.val = val;
        }
    }

    public boolean containsKey(java.lang.String name) {
        boolean zContainsKey;
        synchronized (this.mMap) {
            zContainsKey = this.mMap.containsKey(name);
        }
        return zContainsKey;
    }

    public boolean plus(java.lang.String name) {
        if (android.text.TextUtils.isEmpty(name)) {
            android.util.Slog.w(TAG, "plus: name is null");
            return false;
        }
        synchronized (this.mMap) {
            com.android.server.wm.OplusCounter.EffectiveInteger initValue = new com.android.server.wm.OplusCounter.EffectiveInteger(1);
            com.android.server.wm.OplusCounter.EffectiveInteger oldValue = this.mMap.put(name, initValue);
            if (oldValue != null) {
                initValue.setInt(oldValue.getInt() + 1);
            }
        }
        return true;
    }

    public boolean minus(java.lang.String name) {
        if (android.text.TextUtils.isEmpty(name)) {
            android.util.Slog.w(TAG, "minus: name is null");
            return false;
        }
        synchronized (this.mMap) {
            com.android.server.wm.OplusCounter.EffectiveInteger oldValue = this.mMap.get(name);
            if (oldValue != null) {
                if (oldValue.getInt() != 0) {
                    oldValue.setInt(oldValue.getInt() - 1);
                    if (oldValue.getInt() == 0) {
                        this.mMap.remove(name);
                    }
                } else {
                    android.util.Slog.w(TAG, "minus: Val is 0");
                    return false;
                }
            }
            return true;
        }
    }

    public boolean clear() {
        synchronized (this.mMap) {
            this.mMap.clear();
        }
        return true;
    }

    public boolean remove(java.lang.String name) {
        if (android.text.TextUtils.isEmpty(name)) {
            android.util.Slog.w(TAG, "resetValue: name is null");
            return false;
        }
        synchronized (this.mMap) {
            if (this.mMap.containsKey(name)) {
                this.mMap.remove(name);
                return true;
            }
            android.util.Slog.w(TAG, "remove: name is not in map");
            return false;
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        stringBuilder.append("Counter{ ");
        synchronized (this.mMap) {
            for (java.util.Map.Entry<java.lang.String, com.android.server.wm.OplusCounter.EffectiveInteger> entry : this.mMap.entrySet()) {
                stringBuilder.append(entry.getKey() + "(" + entry.getValue().getInt() + ") ");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public int size() {
        int size;
        synchronized (this.mMap) {
            size = this.mMap.size();
        }
        return size;
    }

    public int size(java.lang.String name) {
        synchronized (this.mMap) {
            com.android.server.wm.OplusCounter.EffectiveInteger value = this.mMap.get(name);
            if (value == null) {
                return 0;
            }
            return value.getInt();
        }
    }

    public int getSingleMaxSize() {
        int tmp = 0;
        synchronized (this.mMap) {
            for (java.util.Map.Entry<java.lang.String, com.android.server.wm.OplusCounter.EffectiveInteger> entry : this.mMap.entrySet()) {
                tmp = java.lang.Math.max(entry.getValue().getInt(), tmp);
            }
        }
        return tmp;
    }
}
