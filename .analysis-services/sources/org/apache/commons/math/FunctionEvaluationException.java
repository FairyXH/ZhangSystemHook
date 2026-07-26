package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public class FunctionEvaluationException extends org.apache.commons.math.MathException {
    private static final long serialVersionUID = 1384427981840836868L;
    private double[] argument;

    public FunctionEvaluationException(double argument) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.EVALUATION_FAILED, java.lang.Double.valueOf(argument));
        this.argument = new double[]{argument};
    }

    public FunctionEvaluationException(double[] argument) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.EVALUATION_FAILED, new org.apache.commons.math.linear.ArrayRealVector(argument));
        this.argument = (double[]) argument.clone();
    }

    public FunctionEvaluationException(double argument, java.lang.String pattern, java.lang.Object... arguments) {
        this(argument, new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public FunctionEvaluationException(double argument, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(pattern, arguments);
        this.argument = new double[]{argument};
    }

    public FunctionEvaluationException(double[] argument, java.lang.String pattern, java.lang.Object... arguments) {
        this(argument, new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public FunctionEvaluationException(double[] argument, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(pattern, arguments);
        this.argument = (double[]) argument.clone();
    }

    public FunctionEvaluationException(java.lang.Throwable cause, double argument) {
        super(cause);
        this.argument = new double[]{argument};
    }

    public FunctionEvaluationException(java.lang.Throwable cause, double[] argument) {
        super(cause);
        this.argument = (double[]) argument.clone();
    }

    public FunctionEvaluationException(java.lang.Throwable cause, double argument, java.lang.String pattern, java.lang.Object... arguments) {
        this(cause, argument, new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public FunctionEvaluationException(java.lang.Throwable cause, double argument, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(cause, pattern, arguments);
        this.argument = new double[]{argument};
    }

    public FunctionEvaluationException(java.lang.Throwable cause, double[] argument, java.lang.String pattern, java.lang.Object... arguments) {
        this(cause, argument, new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public FunctionEvaluationException(java.lang.Throwable cause, double[] argument, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(cause, pattern, arguments);
        this.argument = (double[]) argument.clone();
    }

    public double[] getArgument() {
        return (double[]) this.argument.clone();
    }
}
