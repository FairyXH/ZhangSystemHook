package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class MatrixVisitorException extends org.apache.commons.math.MathRuntimeException {
    private static final long serialVersionUID = 3814333035048617048L;

    public MatrixVisitorException(java.lang.String pattern, java.lang.Object[] arguments) {
        super(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public MatrixVisitorException(org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object[] arguments) {
        super(pattern, arguments);
    }
}
