package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public class UnivariateRealSolverUtils {
    private UnivariateRealSolverUtils() {
    }

    public static double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double x0, double x1) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        setup(f);
        return org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.LazyHolder.FACTORY.newDefaultSolver().solve(f, x0, x1);
    }

    public static double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double x0, double x1, double absoluteAccuracy) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        setup(f);
        org.apache.commons.math.analysis.solvers.UnivariateRealSolver solver = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.LazyHolder.FACTORY.newDefaultSolver();
        solver.setAbsoluteAccuracy(absoluteAccuracy);
        return solver.solve(f, x0, x1);
    }

    public static double[] bracket(org.apache.commons.math.analysis.UnivariateRealFunction function, double initial, double lowerBound, double upperBound) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        return bracket(function, initial, lowerBound, upperBound, Integer.MAX_VALUE);
    }

    public static double[] bracket(org.apache.commons.math.analysis.UnivariateRealFunction function, double initial, double lowerBound, double upperBound, int maximumIterations) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        double fa;
        double fb;
        if (function == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FUNCTION);
        }
        if (maximumIterations <= 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INVALID_MAX_ITERATIONS, java.lang.Integer.valueOf(maximumIterations));
        }
        if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INVALID_BRACKETING_PARAMETERS, java.lang.Double.valueOf(lowerBound), java.lang.Double.valueOf(initial), java.lang.Double.valueOf(upperBound));
        }
        double a = initial;
        double b = initial;
        int numIterations = 0;
        while (true) {
            a = org.apache.commons.math.util.FastMath.max(a - 1.0d, lowerBound);
            b = org.apache.commons.math.util.FastMath.min(1.0d + b, upperBound);
            fa = function.value(a);
            fb = function.value(b);
            numIterations++;
            if (fa * fb <= 0.0d || numIterations >= maximumIterations || (a <= lowerBound && b >= upperBound)) {
                break;
            }
        }
        if (fa * fb > 0.0d) {
            throw new org.apache.commons.math.ConvergenceException(org.apache.commons.math.exception.util.LocalizedFormats.FAILED_BRACKETING, java.lang.Integer.valueOf(numIterations), java.lang.Integer.valueOf(maximumIterations), java.lang.Double.valueOf(initial), java.lang.Double.valueOf(lowerBound), java.lang.Double.valueOf(upperBound), java.lang.Double.valueOf(a), java.lang.Double.valueOf(b), java.lang.Double.valueOf(fa), java.lang.Double.valueOf(fb));
        }
        return new double[]{a, b};
    }

    public static double midpoint(double a, double b) {
        return (a + b) * 0.5d;
    }

    private static void setup(org.apache.commons.math.analysis.UnivariateRealFunction f) {
        if (f == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FUNCTION);
        }
    }

    private static class LazyHolder {
        private static final org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory FACTORY = org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory.newInstance();

        private LazyHolder() {
        }
    }
}
