package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public class MathRuntimeException extends java.lang.RuntimeException implements org.apache.commons.math.exception.MathThrowable {
    private static final long serialVersionUID = 9058794795027570002L;
    private final java.lang.Object[] arguments;
    private final org.apache.commons.math.exception.util.Localizable pattern;

    @java.lang.Deprecated
    public MathRuntimeException(java.lang.String pattern, java.lang.Object... arguments) {
        this(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public MathRuntimeException(org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        this.pattern = pattern;
        this.arguments = arguments == null ? new java.lang.Object[0] : (java.lang.Object[]) arguments.clone();
    }

    public MathRuntimeException(java.lang.Throwable rootCause) {
        super(rootCause);
        this.pattern = org.apache.commons.math.exception.util.LocalizedFormats.SIMPLE_MESSAGE;
        this.arguments = new java.lang.Object[]{rootCause == null ? "" : rootCause.getMessage()};
    }

    @java.lang.Deprecated
    public MathRuntimeException(java.lang.Throwable rootCause, java.lang.String pattern, java.lang.Object... arguments) {
        this(rootCause, new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public MathRuntimeException(java.lang.Throwable rootCause, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(rootCause);
        this.pattern = pattern;
        this.arguments = arguments == null ? new java.lang.Object[0] : (java.lang.Object[]) arguments.clone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String buildMessage(java.util.Locale locale, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        return new java.text.MessageFormat(pattern.getLocalizedString(locale), locale).format(arguments);
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
            return buildMessage(locale, this.pattern, this.arguments);
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

    @java.lang.Deprecated
    public static java.lang.ArithmeticException createArithmeticException(java.lang.String pattern, java.lang.Object... arguments) {
        return createArithmeticException(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public static java.lang.ArithmeticException createArithmeticException(final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.lang.ArithmeticException() { // from class: org.apache.commons.math.MathRuntimeException.1
            private static final long serialVersionUID = 5305498554076846637L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    @java.lang.Deprecated
    public static java.lang.ArrayIndexOutOfBoundsException createArrayIndexOutOfBoundsException(java.lang.String pattern, java.lang.Object... arguments) {
        return createArrayIndexOutOfBoundsException(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public static java.lang.ArrayIndexOutOfBoundsException createArrayIndexOutOfBoundsException(final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.lang.ArrayIndexOutOfBoundsException() { // from class: org.apache.commons.math.MathRuntimeException.2
            private static final long serialVersionUID = 6718518191249632175L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    @java.lang.Deprecated
    public static java.io.EOFException createEOFException(java.lang.String pattern, java.lang.Object... arguments) {
        return createEOFException(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public static java.io.EOFException createEOFException(final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.io.EOFException() { // from class: org.apache.commons.math.MathRuntimeException.3
            private static final long serialVersionUID = 6067985859347601503L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    public static java.io.IOException createIOException(java.lang.Throwable rootCause) {
        java.io.IOException ioe = new java.io.IOException(rootCause.getLocalizedMessage());
        ioe.initCause(rootCause);
        return ioe;
    }

    @java.lang.Deprecated
    public static java.lang.IllegalArgumentException createIllegalArgumentException(java.lang.String pattern, java.lang.Object... arguments) {
        return createIllegalArgumentException(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public static java.lang.IllegalArgumentException createIllegalArgumentException(final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.lang.IllegalArgumentException() { // from class: org.apache.commons.math.MathRuntimeException.4
            private static final long serialVersionUID = -4284649691002411505L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    public static java.lang.IllegalArgumentException createIllegalArgumentException(java.lang.Throwable rootCause) {
        java.lang.IllegalArgumentException iae = new java.lang.IllegalArgumentException(rootCause.getLocalizedMessage());
        iae.initCause(rootCause);
        return iae;
    }

    @java.lang.Deprecated
    public static java.lang.IllegalStateException createIllegalStateException(java.lang.String pattern, java.lang.Object... arguments) {
        return createIllegalStateException(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public static java.lang.IllegalStateException createIllegalStateException(final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.lang.IllegalStateException() { // from class: org.apache.commons.math.MathRuntimeException.5
            private static final long serialVersionUID = 6880901520234515725L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    @java.lang.Deprecated
    public static java.util.ConcurrentModificationException createConcurrentModificationException(java.lang.String pattern, java.lang.Object... arguments) {
        return createConcurrentModificationException(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public static java.util.ConcurrentModificationException createConcurrentModificationException(final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.util.ConcurrentModificationException() { // from class: org.apache.commons.math.MathRuntimeException.6
            private static final long serialVersionUID = -1878427236170442052L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    @java.lang.Deprecated
    public static java.util.NoSuchElementException createNoSuchElementException(java.lang.String pattern, java.lang.Object... arguments) {
        return createNoSuchElementException(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public static java.util.NoSuchElementException createNoSuchElementException(final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.util.NoSuchElementException() { // from class: org.apache.commons.math.MathRuntimeException.7
            private static final long serialVersionUID = 1632410088350355086L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    @java.lang.Deprecated
    public static java.lang.UnsupportedOperationException createUnsupportedOperationException(final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.lang.UnsupportedOperationException() { // from class: org.apache.commons.math.MathRuntimeException.8
            private static final long serialVersionUID = -4284649691002411505L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    @java.lang.Deprecated
    public static java.lang.NullPointerException createNullPointerException(java.lang.String pattern, java.lang.Object... arguments) {
        return createNullPointerException(new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    @java.lang.Deprecated
    public static java.lang.NullPointerException createNullPointerException(final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.lang.NullPointerException() { // from class: org.apache.commons.math.MathRuntimeException.9
            private static final long serialVersionUID = 451965530686593945L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    @java.lang.Deprecated
    public static java.text.ParseException createParseException(int offset, java.lang.String pattern, java.lang.Object... arguments) {
        return createParseException(offset, new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public static java.text.ParseException createParseException(int offset, final org.apache.commons.math.exception.util.Localizable pattern, final java.lang.Object... arguments) {
        return new java.text.ParseException(null, offset) { // from class: org.apache.commons.math.MathRuntimeException.10
            private static final long serialVersionUID = 8153587599409010120L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
            }
        };
    }

    public static java.lang.RuntimeException createInternalError(java.lang.Throwable cause) {
        return new java.lang.RuntimeException(cause) { // from class: org.apache.commons.math.MathRuntimeException.11
            private static final long serialVersionUID = -201865440834027016L;

            @Override // java.lang.Throwable
            public java.lang.String getMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, org.apache.commons.math.exception.util.LocalizedFormats.INTERNAL_ERROR, "https://issues.apache.org/jira/browse/MATH");
            }

            @Override // java.lang.Throwable
            public java.lang.String getLocalizedMessage() {
                return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), org.apache.commons.math.exception.util.LocalizedFormats.INTERNAL_ERROR, "https://issues.apache.org/jira/browse/MATH");
            }
        };
    }
}
