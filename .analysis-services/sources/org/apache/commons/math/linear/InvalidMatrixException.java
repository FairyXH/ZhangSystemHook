package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class InvalidMatrixException extends org.apache.commons.math.MathRuntimeException {
    private static final long serialVersionUID = -2068020346562029801L;

    @java.lang.Deprecated
    public InvalidMatrixException(java.lang.String pattern, java.lang.Object... arguments) {
        this(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public InvalidMatrixException(org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(pattern, arguments);
    }

    public InvalidMatrixException(java.lang.Throwable cause) {
        super(cause);
    }
}
