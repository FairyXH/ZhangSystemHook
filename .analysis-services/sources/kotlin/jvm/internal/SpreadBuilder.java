package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class SpreadBuilder {
    private final java.util.ArrayList<java.lang.Object> list;

    public SpreadBuilder(int size) {
        this.list = new java.util.ArrayList<>(size);
    }

    public void addSpread(java.lang.Object container) {
        if (container == null) {
            return;
        }
        if (container instanceof java.lang.Object[]) {
            java.lang.Object[] array = (java.lang.Object[]) container;
            if (array.length > 0) {
                this.list.ensureCapacity(this.list.size() + array.length);
                java.util.Collections.addAll(this.list, array);
                return;
            }
            return;
        }
        if (container instanceof java.util.Collection) {
            this.list.addAll((java.util.Collection) container);
            return;
        }
        if (container instanceof java.lang.Iterable) {
            for (java.lang.Object element : (java.lang.Iterable) container) {
                this.list.add(element);
            }
            return;
        }
        if (container instanceof java.util.Iterator) {
            java.util.Iterator iterator = (java.util.Iterator) container;
            while (iterator.hasNext()) {
                this.list.add(iterator.next());
            }
            return;
        }
        throw new java.lang.UnsupportedOperationException("Don't know how to spread " + container.getClass());
    }

    public int size() {
        return this.list.size();
    }

    public void add(java.lang.Object element) {
        this.list.add(element);
    }

    public java.lang.Object[] toArray(java.lang.Object[] a) {
        return this.list.toArray(a);
    }
}
