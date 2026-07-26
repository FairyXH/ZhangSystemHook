package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public class SimpleRealPointChecker implements org.apache.commons.math.optimization.RealConvergenceChecker {
    private static final double DEFAULT_ABSOLUTE_THRESHOLD = 2.2250738585072014E-306d;
    private static final double DEFAULT_RELATIVE_THRESHOLD = 1.1102230246251565E-14d;
    private final double absoluteThreshold;
    private final double relativeThreshold;

    public SimpleRealPointChecker() {
        this.relativeThreshold = DEFAULT_RELATIVE_THRESHOLD;
        this.absoluteThreshold = DEFAULT_ABSOLUTE_THRESHOLD;
    }

    public SimpleRealPointChecker(double relativeThreshold, double absoluteThreshold) {
        this.relativeThreshold = relativeThreshold;
        this.absoluteThreshold = absoluteThreshold;
    }

    @Override // org.apache.commons.math.optimization.RealConvergenceChecker
    public boolean converged(int iteration, org.apache.commons.math.optimization.RealPointValuePair previous, org.apache.commons.math.optimization.RealPointValuePair current) {
        double[] p = previous.getPoint();
        double[] c = current.getPoint();
        for (int i = 0; i < p.length; i++) {
            double difference = org.apache.commons.math.util.FastMath.abs(p[i] - c[i]);
            double size = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(p[i]), org.apache.commons.math.util.FastMath.abs(c[i]));
            if (difference > this.relativeThreshold * size && difference > this.absoluteThreshold) {
                return false;
            }
        }
        return true;
    }
}
