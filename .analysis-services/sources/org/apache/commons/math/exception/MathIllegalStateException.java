package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class MathIllegalStateException extends java.lang.IllegalStateException implements org.apache.commons.math.exception.MathThrowable {
    private static final long serialVersionUID = -6024911025449780478L;
    private final java.lang.Object[] arguments;
    private final org.apache.commons.math.exception.util.Localizable general;
    private final org.apache.commons.math.exception.util.Localizable specific;

    public MathIllegalStateException(org.apache.commons.math.exception.util.Localizable specific, org.apache.commons.math.exception.util.Localizable general, java.lang.Object... args) {
        this(null, specific, general, args);
    }

    public MathIllegalStateException(java.lang.Throwable cause, org.apache.commons.math.exception.util.Localizable specific, org.apache.commons.math.exception.util.Localizable general, java.lang.Object... args) {
        super(cause);
        this.specific = specific;
        this.general = general;
        this.arguments = org.apache.commons.math.exception.util.ArgUtils.flatten(args);
    }

    public MathIllegalStateException(org.apache.commons.math.exception.util.Localizable general, java.lang.Object... args) {
        this(null, null, general, args);
    }

    public MathIllegalStateException(java.lang.Throwable cause, org.apache.commons.math.exception.util.Localizable general, java.lang.Object... args) {
        this(cause, null, general, args);
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public org.apache.commons.math.exception.util.Localizable getSpecificPattern() {
        return this.specific;
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public org.apache.commons.math.exception.util.Localizable getGeneralPattern() {
        return this.general;
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public java.lang.Object[] getArguments() {
        return (java.lang.Object[]) this.arguments.clone();
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public java.lang.String getMessage(java.util.Locale locale) {
        return org.apache.commons.math.exception.util.MessageFactory.buildMessage(locale, this.specific, this.general, this.arguments);
    }

    @Override // java.lang.Throwable, org.apache.commons.math.exception.MathThrowable
    public java.lang.String getMessage() {
        return getMessage(java.util.Locale.US);
    }

    @Override // java.lang.Throwable, org.apache.commons.math.exception.MathThrowable
    public java.lang.String getLocalizedMessage() {
        return getMessage(java.util.Locale.getDefault());
    }
}
