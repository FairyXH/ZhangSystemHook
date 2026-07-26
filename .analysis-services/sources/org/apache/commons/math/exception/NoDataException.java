package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class NoDataException extends org.apache.commons.math.exception.MathIllegalStateException {
    private static final long serialVersionUID = -3629324471511904459L;

    public NoDataException() {
        this(null);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NoDataException(org.apache.commons.math.exception.util.Localizable specific) {
        super(specific, org.apache.commons.math.exception.util.LocalizedFormats.NO_DATA, (java.lang.Object[]) null);
    }
}
