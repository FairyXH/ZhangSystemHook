package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class NumberIsTooSmallException extends org.apache.commons.math.exception.MathIllegalNumberException {
    private static final long serialVersionUID = -6100997100383932834L;
    private final boolean boundIsAllowed;
    private final java.lang.Number min;

    public NumberIsTooSmallException(java.lang.Number wrong, java.lang.Number min, boolean boundIsAllowed) {
        this(null, wrong, min, boundIsAllowed);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public NumberIsTooSmallException(org.apache.commons.math.exception.util.Localizable specific, java.lang.Number wrong, java.lang.Number min, boolean boundIsAllowed) {
        org.apache.commons.math.exception.util.LocalizedFormats localizedFormats;
        if (boundIsAllowed) {
            localizedFormats = org.apache.commons.math.exception.util.LocalizedFormats.NUMBER_TOO_SMALL;
        } else {
            localizedFormats = org.apache.commons.math.exception.util.LocalizedFormats.NUMBER_TOO_SMALL_BOUND_EXCLUDED;
        }
        super(specific, localizedFormats, wrong, min);
        this.min = min;
        this.boundIsAllowed = boundIsAllowed;
    }

    public boolean getBoundIsAllowed() {
        return this.boundIsAllowed;
    }

    public java.lang.Number getMin() {
        return this.min;
    }
}
