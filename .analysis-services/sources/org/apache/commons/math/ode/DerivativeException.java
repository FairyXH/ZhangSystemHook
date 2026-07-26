package org.apache.commons.math.ode;

/* JADX INFO: loaded from: classes4.dex */
public class DerivativeException extends org.apache.commons.math.MathException {
    private static final long serialVersionUID = 5666710788967425123L;

    public DerivativeException(java.lang.String specifier, java.lang.Object... parts) {
        this(new org.apache.commons.math.exception.util.DummyLocalizable(specifier), parts);
    }

    public DerivativeException(org.apache.commons.math.exception.util.Localizable specifier, java.lang.Object... parts) {
        super(specifier, parts);
    }

    public DerivativeException(java.lang.Throwable cause) {
        super(cause);
    }
}
