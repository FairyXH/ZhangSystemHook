package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public class MaxIterationsExceededException extends org.apache.commons.math.ConvergenceException {
    private static final long serialVersionUID = -7821226672760574694L;
    private final int maxIterations;

    public MaxIterationsExceededException(int maxIterations) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.MAX_ITERATIONS_EXCEEDED, java.lang.Integer.valueOf(maxIterations));
        this.maxIterations = maxIterations;
    }

    @java.lang.Deprecated
    public MaxIterationsExceededException(int maxIterations, java.lang.String pattern, java.lang.Object... arguments) {
        this(maxIterations, new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public MaxIterationsExceededException(int maxIterations, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(pattern, arguments);
        this.maxIterations = maxIterations;
    }

    public int getMaxIterations() {
        return this.maxIterations;
    }
}
