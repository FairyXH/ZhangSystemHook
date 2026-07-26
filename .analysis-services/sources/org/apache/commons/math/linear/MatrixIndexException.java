package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class MatrixIndexException extends org.apache.commons.math.MathRuntimeException {
    private static final long serialVersionUID = 8120540015829487660L;

    @java.lang.Deprecated
    public MatrixIndexException(java.lang.String pattern, java.lang.Object... arguments) {
        this(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public MatrixIndexException(org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(pattern, arguments);
    }
}
