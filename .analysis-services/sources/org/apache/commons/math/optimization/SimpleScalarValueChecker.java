package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public class SimpleScalarValueChecker implements org.apache.commons.math.optimization.RealConvergenceChecker {
    private static final double DEFAULT_ABSOLUTE_THRESHOLD = 2.2250738585072014E-306d;
    private static final double DEFAULT_RELATIVE_THRESHOLD = 1.1102230246251565E-14d;
    private final double absoluteThreshold;
    private final double relativeThreshold;

    public SimpleScalarValueChecker() {
        this.relativeThreshold = DEFAULT_RELATIVE_THRESHOLD;
        this.absoluteThreshold = DEFAULT_ABSOLUTE_THRESHOLD;
    }

    public SimpleScalarValueChecker(double relativeThreshold, double absoluteThreshold) {
        this.relativeThreshold = relativeThreshold;
        this.absoluteThreshold = absoluteThreshold;
    }

    @Override // org.apache.commons.math.optimization.RealConvergenceChecker
    public boolean converged(int iteration, org.apache.commons.math.optimization.RealPointValuePair previous, org.apache.commons.math.optimization.RealPointValuePair current) {
        double p = previous.getValue();
        double c = current.getValue();
        double difference = org.apache.commons.math.util.FastMath.abs(p - c);
        double size = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(p), org.apache.commons.math.util.FastMath.abs(c));
        return difference <= this.relativeThreshold * size || difference <= this.absoluteThreshold;
    }
}
