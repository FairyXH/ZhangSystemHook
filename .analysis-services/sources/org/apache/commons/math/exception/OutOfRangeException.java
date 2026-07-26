package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class OutOfRangeException extends org.apache.commons.math.exception.MathIllegalNumberException {
    private static final long serialVersionUID = 111601815794403609L;
    private final java.lang.Number hi;
    private final java.lang.Number lo;

    public OutOfRangeException(java.lang.Number wrong, java.lang.Number lo, java.lang.Number hi) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_RANGE_SIMPLE, wrong, lo, hi);
        this.lo = lo;
        this.hi = hi;
    }

    public java.lang.Number getLo() {
        return this.lo;
    }

    public java.lang.Number getHi() {
        return this.hi;
    }
}
