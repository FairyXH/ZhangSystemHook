package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class NotPositiveException extends org.apache.commons.math.exception.NumberIsTooSmallException {
    private static final long serialVersionUID = -2250556892093726375L;

    public NotPositiveException(java.lang.Number value) {
        super(value, 0, true);
    }

    public NotPositiveException(org.apache.commons.math.exception.util.Localizable specific, java.lang.Number value) {
        super(specific, value, 0, true);
    }
}
