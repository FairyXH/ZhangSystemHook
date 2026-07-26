package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public class MaxEvaluationsExceededException extends org.apache.commons.math.ConvergenceException {
    private static final long serialVersionUID = -5921271447220129118L;
    private final int maxEvaluations;

    public MaxEvaluationsExceededException(int maxEvaluations) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.MAX_EVALUATIONS_EXCEEDED, java.lang.Integer.valueOf(maxEvaluations));
        this.maxEvaluations = maxEvaluations;
    }

    @java.lang.Deprecated
    public MaxEvaluationsExceededException(int maxEvaluations, java.lang.String pattern, java.lang.Object... arguments) {
        this(maxEvaluations, new org.apache.commons.math.exception.util.DummyLocalizable(pattern), arguments);
    }

    public MaxEvaluationsExceededException(int maxEvaluations, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        super(pattern, arguments);
        this.maxEvaluations = maxEvaluations;
    }

    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }
}
