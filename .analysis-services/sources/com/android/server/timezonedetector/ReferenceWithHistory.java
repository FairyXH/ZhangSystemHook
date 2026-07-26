package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class ReferenceWithHistory<V> {
    private final int mMaxHistorySize;
    private int mSetCount;
    private java.util.ArrayDeque<android.os.TimestampedValue<V>> mValues;

    public ReferenceWithHistory(int maxHistorySize) {
        if (maxHistorySize < 1) {
            throw new java.lang.IllegalArgumentException("maxHistorySize < 1: " + maxHistorySize);
        }
        this.mMaxHistorySize = maxHistorySize;
    }

    public V get() {
        if (this.mValues == null || this.mValues.isEmpty()) {
            return null;
        }
        return (V) this.mValues.getFirst().getValue();
    }

    public V set(V newValue) {
        if (this.mValues == null) {
            this.mValues = new java.util.ArrayDeque<>(this.mMaxHistorySize);
        }
        if (this.mValues.size() >= this.mMaxHistorySize) {
            this.mValues.removeLast();
        }
        V previous = get();
        android.os.TimestampedValue<V> valueHolder = new android.os.TimestampedValue<>(android.os.SystemClock.elapsedRealtime(), newValue);
        this.mValues.addFirst(valueHolder);
        this.mSetCount++;
        return previous;
    }

    public void dump(android.util.IndentingPrintWriter ipw) {
        if (this.mValues == null) {
            ipw.println("{Empty}");
        } else {
            int i = this.mSetCount - this.mValues.size();
            java.util.Iterator<android.os.TimestampedValue<V>> reverseIterator = this.mValues.descendingIterator();
            while (reverseIterator.hasNext()) {
                android.os.TimestampedValue<V> valueHolder = reverseIterator.next();
                ipw.print(i);
                ipw.print("@");
                ipw.print(java.time.Duration.ofMillis(valueHolder.getReferenceTimeMillis()).toString());
                ipw.print(": ");
                ipw.println(valueHolder.getValue());
                i++;
            }
        }
        ipw.flush();
    }

    public int getHistoryCount() {
        if (this.mValues == null) {
            return 0;
        }
        return this.mValues.size();
    }

    public java.lang.String toString() {
        return java.lang.String.valueOf(get());
    }
}
