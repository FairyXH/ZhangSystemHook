package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: compiled from: PrimitiveSpreadBuilders.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\t\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012J\b\u0010\u0003\u001a\u00020\u0004H\u0004J\u001d\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00028\u00002\u0006\u0010\u0015\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u0016J\u0011\u0010\u0017\u001a\u00020\u0004*\u00028\u0000H$¢\u0006\u0002\u0010\u0018R\u001a\u0010\u0006\u001a\u00020\u0004X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\u0005R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000bX\u0082\u0004¢\u0006\n\n\u0002\u0010\u000e\u0012\u0004\b\f\u0010\r¨\u0006\u0019"}, d2 = {"Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", "T", "", "size", "", "(I)V", "position", "getPosition", "()I", "setPosition", "spreads", "", "getSpreads$annotations", "()V", "[Ljava/lang/Object;", "addSpread", "", "spreadArgument", "(Ljava/lang/Object;)V", "toArray", "values", "result", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "getSize", "(Ljava/lang/Object;)I", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class PrimitiveSpreadBuilder<T> {
    private int position;
    private final int size;
    private final T[] spreads;

    private static /* synthetic */ void getSpreads$annotations() {
    }

    protected abstract int getSize(T t);

    public PrimitiveSpreadBuilder(int i) {
        this.size = i;
        this.spreads = (T[]) new java.lang.Object[this.size];
    }

    protected final int getPosition() {
        return this.position;
    }

    protected final void setPosition(int i) {
        this.position = i;
    }

    public final void addSpread(T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "spreadArgument");
        T[] tArr = this.spreads;
        int i = this.position;
        this.position = i + 1;
        tArr[i] = t;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    protected final int size() {
        int totalLength = 0;
        ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(0, this.size - 1).iterator2();
        while (it.hasNext()) {
            int i = it.nextInt();
            T t = this.spreads[i];
            totalLength += t != null ? getSize(t) : 1;
        }
        return totalLength;
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.server.permission.jarjar.kotlin.collections.IntIterator] */
    protected final T toArray(T t, T t2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "values");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t2, "result");
        int dstIndex = 0;
        int copyValuesFrom = 0;
        ?? it = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(0, this.size - 1).iterator2();
        while (it.hasNext()) {
            int i = it.nextInt();
            T t3 = this.spreads[i];
            if (t3 != null) {
                if (copyValuesFrom < i) {
                    java.lang.System.arraycopy(t, copyValuesFrom, t2, dstIndex, i - copyValuesFrom);
                    dstIndex += i - copyValuesFrom;
                }
                int spreadSize = getSize(t3);
                java.lang.System.arraycopy(t3, 0, t2, dstIndex, spreadSize);
                dstIndex += spreadSize;
                copyValuesFrom = i + 1;
            }
        }
        if (copyValuesFrom < this.size) {
            java.lang.System.arraycopy(t, copyValuesFrom, t2, dstIndex, this.size - copyValuesFrom);
        }
        return t2;
    }
}
