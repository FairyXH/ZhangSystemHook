package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
public class ConcurrentLinkedEvictingDeque<E> extends java.util.concurrent.ConcurrentLinkedDeque<E> {
    private int mSize;

    ConcurrentLinkedEvictingDeque(int size) {
        this.mSize = size;
    }

    @Override // java.util.concurrent.ConcurrentLinkedDeque, java.util.AbstractCollection, java.util.Collection, java.util.Deque, java.util.Queue
    public boolean add(E elem) {
        boolean zAdd;
        synchronized (this) {
            if (size() == this.mSize) {
                poll();
            }
            zAdd = super.add(elem);
        }
        return zAdd;
    }
}
