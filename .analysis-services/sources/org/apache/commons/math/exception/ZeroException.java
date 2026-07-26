package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class ZeroException extends org.apache.commons.math.exception.MathIllegalNumberException {
    private static final long serialVersionUID = -1960874856936000015L;

    public ZeroException() {
        this(null);
    }

    public ZeroException(org.apache.commons.math.exception.util.Localizable specific) {
        super(specific, org.apache.commons.math.exception.util.LocalizedFormats.ZERO_NOT_ALLOWED, 0, new java.lang.Object[0]);
    }
}
