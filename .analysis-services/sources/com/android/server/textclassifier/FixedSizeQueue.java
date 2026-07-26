package com.android.server.textclassifier;

/* JADX INFO: loaded from: classes3.dex */
public final class FixedSizeQueue<E> {
    private final java.util.Queue<E> mDelegate;
    private final int mMaxSize;
    private final com.android.server.textclassifier.FixedSizeQueue.OnEntryEvictedListener<E> mOnEntryEvictedListener;

    public interface OnEntryEvictedListener<E> {
        void onEntryEvicted(E e);
    }

    public FixedSizeQueue(int maxSize, com.android.server.textclassifier.FixedSizeQueue.OnEntryEvictedListener<E> onEntryEvictedListener) {
        com.android.internal.util.Preconditions.checkArgument(maxSize > 0, "maxSize (%s) must > 0", new java.lang.Object[]{java.lang.Integer.valueOf(maxSize)});
        this.mDelegate = new java.util.ArrayDeque(maxSize);
        this.mMaxSize = maxSize;
        this.mOnEntryEvictedListener = onEntryEvictedListener;
    }

    public int size() {
        return this.mDelegate.size();
    }

    public boolean add(E element) {
        java.util.Objects.requireNonNull(element);
        if (size() == this.mMaxSize) {
            E removed = this.mDelegate.remove();
            if (this.mOnEntryEvictedListener != null) {
                this.mOnEntryEvictedListener.onEntryEvicted(removed);
            }
        }
        this.mDelegate.add(element);
        return true;
    }

    public E poll() {
        return this.mDelegate.poll();
    }

    public boolean remove(E element) {
        java.util.Objects.requireNonNull(element);
        return this.mDelegate.remove(element);
    }

    public boolean isEmpty() {
        return this.mDelegate.isEmpty();
    }
}
