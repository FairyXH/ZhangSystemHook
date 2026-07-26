package org.apache.commons.math.ode;

/* JADX INFO: loaded from: classes4.dex */
public class IntegratorException extends org.apache.commons.math.MathException {
    private static final long serialVersionUID = -1607588949778036796L;

    @java.lang.Deprecated
    public IntegratorException(java.lang.String specifier, java.lang.Object... parts) {
        super(specifier, parts);
    }

    public IntegratorException(org.apache.commons.math.exception.util.Localizable specifier, java.lang.Object... parts) {
        super(specifier, parts);
    }

    public IntegratorException(java.lang.Throwable cause) {
        super(cause);
    }
}
