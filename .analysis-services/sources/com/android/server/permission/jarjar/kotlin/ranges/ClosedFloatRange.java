package com.android.server.permission.jarjar.kotlin.ranges;

/* JADX INFO: compiled from: Ranges.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002¢\u0006\u0002\u0010\u0005J\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0002H\u0096\u0002J\u0013\u0010\u000e\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\fH\u0016J\u0018\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u0002H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\u0006\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\u0003\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\t¨\u0006\u0019"}, d2 = {"Lkotlin/ranges/ClosedFloatRange;", "Lkotlin/ranges/ClosedFloatingPointRange;", "", "start", "endInclusive", "(FF)V", "_endInclusive", "_start", "getEndInclusive", "()Ljava/lang/Float;", "getStart", "contains", "", "value", "equals", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "isEmpty", "lessThanOrEquals", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class ClosedFloatRange implements com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange<java.lang.Float> {
    private final float _endInclusive;
    private final float _start;

    public ClosedFloatRange(float start, float endInclusive) {
        this._start = start;
        this._endInclusive = endInclusive;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange, com.android.server.permission.jarjar.kotlin.ranges.ClosedRange, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ boolean contains(java.lang.Comparable comparable) {
        return contains(((java.lang.Number) comparable).floatValue());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange
    public /* bridge */ /* synthetic */ boolean lessThanOrEquals(java.lang.Comparable comparable, java.lang.Comparable comparable2) {
        return lessThanOrEquals(((java.lang.Number) comparable).floatValue(), ((java.lang.Number) comparable2).floatValue());
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedRange, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public java.lang.Float getStart() {
        return java.lang.Float.valueOf(this._start);
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedRange
    public java.lang.Float getEndInclusive() {
        return java.lang.Float.valueOf(this._endInclusive);
    }

    public boolean lessThanOrEquals(float a, float b) {
        return a <= b;
    }

    public boolean contains(float value) {
        return value >= this._start && value <= this._endInclusive;
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange, com.android.server.permission.jarjar.kotlin.ranges.ClosedRange, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public boolean isEmpty() {
        return this._start > this._endInclusive;
    }

    public boolean equals(java.lang.Object other) {
        if (!(other instanceof com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatRange)) {
            return false;
        }
        if (!isEmpty() || !((com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatRange) other).isEmpty()) {
            if (!(this._start == ((com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatRange) other)._start)) {
                return false;
            }
            if (!(this._endInclusive == ((com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatRange) other)._endInclusive)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (java.lang.Float.hashCode(this._start) * 31) + java.lang.Float.hashCode(this._endInclusive);
    }

    public java.lang.String toString() {
        return this._start + ".." + this._endInclusive;
    }
}
