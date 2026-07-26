package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class MathUnsupportedOperationException extends java.lang.UnsupportedOperationException implements org.apache.commons.math.exception.MathThrowable {
    private static final long serialVersionUID = -6024911025449780478L;
    private final java.lang.Object[] arguments;
    private final org.apache.commons.math.exception.util.Localizable specific;

    public MathUnsupportedOperationException(java.lang.Object... args) {
        this(null, args);
    }

    public MathUnsupportedOperationException(org.apache.commons.math.exception.util.Localizable specific, java.lang.Object... args) {
        this.specific = specific;
        this.arguments = org.apache.commons.math.exception.util.ArgUtils.flatten(args);
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public org.apache.commons.math.exception.util.Localizable getSpecificPattern() {
        return this.specific;
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public org.apache.commons.math.exception.util.Localizable getGeneralPattern() {
        return org.apache.commons.math.exception.util.LocalizedFormats.UNSUPPORTED_OPERATION;
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public java.lang.Object[] getArguments() {
        return (java.lang.Object[]) this.arguments.clone();
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public java.lang.String getMessage(java.util.Locale locale) {
        return org.apache.commons.math.exception.util.MessageFactory.buildMessage(locale, this.specific, org.apache.commons.math.exception.util.LocalizedFormats.UNSUPPORTED_OPERATION, this.arguments);
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
