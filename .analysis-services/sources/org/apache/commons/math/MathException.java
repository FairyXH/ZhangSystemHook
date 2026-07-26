package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public class MathException extends java.lang.Exception implements org.apache.commons.math.exception.MathThrowable {
    private static final long serialVersionUID = 7428019509644517071L;
    private final java.lang.Object[] arguments;
    private final org.apache.commons.math.exception.util.Localizable pattern;

    public MathException() {
        this.pattern = org.apache.commons.math.exception.util.LocalizedFormats.SIMPLE_MESSAGE;
        this.arguments = new java.lang.Object[]{""};
    }

    @java.lang.Deprecated
    public MathException(java.lang.String pattern, java.lang.Object... arguments) {
        this(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public MathException(org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        this.pattern = pattern;
        this.arguments = arguments == null ? new java.lang.Object[0] : (java.lang.Object[]) arguments.clone();
    }

    public MathException(java.lang.Throwable rootCause) {
        super(rootCause);
        this.pattern = org.apache.commons.math.exception.util.LocalizedFormats.SIMPLE_MESSAGE;
        this.arguments = new java.lang.Object[]{rootCause == null ? "" : rootCause.getMessage()};
    }

    @java.lang.Deprecated
    public MathException(java.lang.Throwable rootCause, java.lang.String pattern, java.lang.Object... arguments) {
        this(rootCause, new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public MathException(java.lang.Throwable rootCause, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(rootCause);
        this.pattern = pattern;
        this.arguments = arguments == null ? new java.lang.Object[0] : (java.lang.Object[]) arguments.clone();
    }

    @java.lang.Deprecated
    public java.lang.String getPattern() {
        return this.pattern.getSourceString();
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public org.apache.commons.math.exception.util.Localizable getSpecificPattern() {
        return null;
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public org.apache.commons.math.exception.util.Localizable getGeneralPattern() {
        return this.pattern;
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public java.lang.Object[] getArguments() {
        return (java.lang.Object[]) this.arguments.clone();
    }

    @Override // org.apache.commons.math.exception.MathThrowable
    public java.lang.String getMessage(java.util.Locale locale) {
        if (this.pattern != null) {
            return new java.text.MessageFormat(this.pattern.getLocalizedString(locale), locale).format(this.arguments);
        }
        return "";
    }

    @Override // java.lang.Throwable, org.apache.commons.math.exception.MathThrowable
    public java.lang.String getMessage() {
        return getMessage(java.util.Locale.US);
    }

    @Override // java.lang.Throwable, org.apache.commons.math.exception.MathThrowable
    public java.lang.String getLocalizedMessage() {
        return getMessage(java.util.Locale.getDefault());
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(java.lang.System.err);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(java.io.PrintStream out) {
        synchronized (out) {
            java.io.PrintWriter pw = new java.io.PrintWriter((java.io.OutputStream) out, false);
            printStackTrace(pw);
            pw.flush();
        }
    }
}
