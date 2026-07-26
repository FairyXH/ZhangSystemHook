package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class MathIllegalNumberException extends org.apache.commons.math.exception.MathIllegalArgumentException {
    private static final long serialVersionUID = -7447085893598031110L;
    private final java.lang.Number argument;

    protected MathIllegalNumberException(org.apache.commons.math.exception.util.Localizable specific, org.apache.commons.math.exception.util.Localizable general, java.lang.Number wrong, java.lang.Object... arguments) {
        super(specific, general, wrong, arguments);
        this.argument = wrong;
    }

    protected MathIllegalNumberException(org.apache.commons.math.exception.util.Localizable general, java.lang.Number wrong, java.lang.Object... arguments) {
        super(general, wrong, arguments);
        this.argument = wrong;
    }

    public java.lang.Number getArgument() {
        return this.argument;
    }
}
