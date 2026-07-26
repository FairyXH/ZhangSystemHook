package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public class SimpleVectorialPointChecker implements org.apache.commons.math.optimization.VectorialConvergenceChecker {
    private static final double DEFAULT_ABSOLUTE_THRESHOLD = 2.2250738585072014E-306d;
    private static final double DEFAULT_RELATIVE_THRESHOLD = 1.1102230246251565E-14d;
    private final double absoluteThreshold;
    private final double relativeThreshold;

    public SimpleVectorialPointChecker() {
        this.relativeThreshold = DEFAULT_RELATIVE_THRESHOLD;
        this.absoluteThreshold = DEFAULT_ABSOLUTE_THRESHOLD;
    }

    public SimpleVectorialPointChecker(double relativeThreshold, double absoluteThreshold) {
        this.relativeThreshold = relativeThreshold;
        this.absoluteThreshold = absoluteThreshold;
    }

    @Override // org.apache.commons.math.optimization.VectorialConvergenceChecker
    public boolean converged(int iteration, org.apache.commons.math.optimization.VectorialPointValuePair previous, org.apache.commons.math.optimization.VectorialPointValuePair current) {
        double[] p = previous.getPointRef();
        double[] c = current.getPointRef();
        for (int i = 0; i < p.length; i++) {
            double pi = p[i];
            double ci = c[i];
            double difference = org.apache.commons.math.util.FastMath.abs(pi - ci);
            double size = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(pi), org.apache.commons.math.util.FastMath.abs(ci));
            if (difference > this.relativeThreshold * size && difference > this.absoluteThreshold) {
                return false;
            }
        }
        return true;
    }
}
