package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class NumberIsTooLargeException extends org.apache.commons.math.exception.MathIllegalNumberException {
    private static final long serialVersionUID = 4330003017885151975L;
    private final boolean boundIsAllowed;
    private final java.lang.Number max;

    public NumberIsTooLargeException(java.lang.Number wrong, java.lang.Number max, boolean boundIsAllowed) {
        this(null, wrong, max, boundIsAllowed);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public NumberIsTooLargeException(org.apache.commons.math.exception.util.Localizable specific, java.lang.Number wrong, java.lang.Number max, boolean boundIsAllowed) {
        org.apache.commons.math.exception.util.LocalizedFormats localizedFormats;
        if (boundIsAllowed) {
            localizedFormats = org.apache.commons.math.exception.util.LocalizedFormats.NUMBER_TOO_LARGE;
        } else {
            localizedFormats = org.apache.commons.math.exception.util.LocalizedFormats.NUMBER_TOO_LARGE_BOUND_EXCLUDED;
        }
        super(specific, localizedFormats, wrong, max);
        this.max = max;
        this.boundIsAllowed = boundIsAllowed;
    }

    public boolean getBoundIsAllowed() {
        return this.boundIsAllowed;
    }

    public java.lang.Number getMax() {
        return this.max;
    }
}
