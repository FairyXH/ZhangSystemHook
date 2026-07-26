package com.android.server.permission.jarjar.kotlin.ranges;

/* JADX INFO: compiled from: Progressions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00192\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0019B\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0006H\u0016J\b\u0010\u0014\u001a\u00020\u0010H\u0016J\t\u0010\u0015\u001a\u00020\u0016H\u0096\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u0011\u0010\b\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a"}, d2 = {"Lkotlin/ranges/CharProgression;", "", "", "start", "endInclusive", "step", "", "(CCI)V", "first", "getFirst", "()C", "last", "getLast", "getStep", "()I", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "isEmpty", "iterator", "Lkotlin/collections/CharIterator;", "toString", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class CharProgression implements java.lang.Iterable<java.lang.Character>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    public static final com.android.server.permission.jarjar.kotlin.ranges.CharProgression.Companion Companion = new com.android.server.permission.jarjar.kotlin.ranges.CharProgression.Companion(null);
    private final char first;
    private final char last;
    private final int step;

    public CharProgression(char start, char endInclusive, int step) {
        if (step == 0) {
            throw new java.lang.IllegalArgumentException("Step must be non-zero.");
        }
        if (step == Integer.MIN_VALUE) {
            throw new java.lang.IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
        }
        this.first = start;
        this.last = (char) com.android.server.permission.jarjar.kotlin.internal.ProgressionUtilKt.getProgressionLastElement((int) start, (int) endInclusive, step);
        this.step = step;
    }

    public final char getFirst() {
        return this.first;
    }

    public final char getLast() {
        return this.last;
    }

    public final int getStep() {
        return this.step;
    }

    @Override // java.lang.Iterable
    /* JADX INFO: renamed from: iterator, reason: merged with bridge method [inline-methods] */
    public java.util.Iterator<java.lang.Character> iterator2() {
        return new com.android.server.permission.jarjar.kotlin.ranges.CharProgressionIterator(this.first, this.last, this.step);
    }

    public boolean isEmpty() {
        if (this.step > 0) {
            if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare((int) this.first, (int) this.last) > 0) {
                return true;
            }
        } else if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare((int) this.first, (int) this.last) < 0) {
            return true;
        }
        return false;
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof com.android.server.permission.jarjar.kotlin.ranges.CharProgression) && ((isEmpty() && ((com.android.server.permission.jarjar.kotlin.ranges.CharProgression) other).isEmpty()) || (this.first == ((com.android.server.permission.jarjar.kotlin.ranges.CharProgression) other).first && this.last == ((com.android.server.permission.jarjar.kotlin.ranges.CharProgression) other).last && this.step == ((com.android.server.permission.jarjar.kotlin.ranges.CharProgression) other).step));
    }

    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (((this.first * 31) + this.last) * 31) + this.step;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sbAppend;
        int i;
        if (this.step > 0) {
            sbAppend = new java.lang.StringBuilder().append(this.first).append("..").append(this.last).append(" step ");
            i = this.step;
        } else {
            sbAppend = new java.lang.StringBuilder().append(this.first).append(" downTo ").append(this.last).append(" step ");
            i = -this.step;
        }
        return sbAppend.append(i).toString();
    }

    /* JADX INFO: compiled from: Progressions.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¨\u0006\n"}, d2 = {"Lkotlin/ranges/CharProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/CharProgression;", "rangeStart", "", "rangeEnd", "step", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final com.android.server.permission.jarjar.kotlin.ranges.CharProgression fromClosedRange(char rangeStart, char rangeEnd, int step) {
            return new com.android.server.permission.jarjar.kotlin.ranges.CharProgression(rangeStart, rangeEnd, step);
        }
    }
}
