package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowList<E> extends java.util.ArrayList<E> {
    WindowList() {
    }

    public void addFirst(E e) {
        add(0, e);
    }

    E peekLast() {
        if (size() > 0) {
            return get(size() - 1);
        }
        return null;
    }

    E peekFirst() {
        if (size() > 0) {
            return get(0);
        }
        return null;
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(java.lang.Object obj) {
        boolean willRemove = super.remove(obj);
        if (obj instanceof com.android.server.wm.DisplayContent) {
            android.util.Slog.w("WindowManager", "obj = " + obj + ", willRemove = " + willRemove, new java.lang.Throwable());
        }
        return willRemove;
    }
}
