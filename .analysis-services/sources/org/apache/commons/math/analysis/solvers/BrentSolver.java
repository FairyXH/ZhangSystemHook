package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public class BrentSolver extends org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl {
    public static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;
    public static final int DEFAULT_MAXIMUM_ITERATIONS = 100;
    private static final long serialVersionUID = 7694577816772532779L;

    @java.lang.Deprecated
    public BrentSolver(org.apache.commons.math.analysis.UnivariateRealFunction f) {
        super(f, 100, 1.0E-6d);
    }

    @java.lang.Deprecated
    public BrentSolver() {
        super(100, 1.0E-6d);
    }

    public BrentSolver(double absoluteAccuracy) {
        super(100, absoluteAccuracy);
    }

    public BrentSolver(int maximumIterations, double absoluteAccuracy) {
        super(maximumIterations, absoluteAccuracy);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve(this.f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve(this.f, min, max, initial);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        clearResult();
        if (initial < min || initial > max) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INVALID_INTERVAL_INITIAL_VALUE_PARAMETERS, java.lang.Double.valueOf(min), java.lang.Double.valueOf(initial), java.lang.Double.valueOf(max));
        }
        double yInitial = f.value(initial);
        if (org.apache.commons.math.util.FastMath.abs(yInitial) <= this.functionValueAccuracy) {
            setResult(initial, 0);
            return this.result;
        }
        double yMin = f.value(min);
        if (org.apache.commons.math.util.FastMath.abs(yMin) <= this.functionValueAccuracy) {
            setResult(min, 0);
            return this.result;
        }
        if (yInitial * yMin < 0.0d) {
            return solve(f, min, yMin, initial, yInitial, min, yMin);
        }
        double yMax = f.value(max);
        if (org.apache.commons.math.util.FastMath.abs(yMax) <= this.functionValueAccuracy) {
            setResult(max, 0);
            return this.result;
        }
        if (yInitial * yMax < 0.0d) {
            return solve(f, initial, yInitial, max, yMax, initial, yInitial);
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.SAME_SIGN_AT_ENDPOINTS, java.lang.Double.valueOf(min), java.lang.Double.valueOf(max), java.lang.Double.valueOf(yMin), java.lang.Double.valueOf(yMax));
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max, initial);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        clearResult();
        verifyInterval(min, max);
        double yMin = f.value(min);
        double yMax = f.value(max);
        double sign = yMin * yMax;
        if (sign <= 0.0d) {
            if (sign < 0.0d) {
                double ret = solve(f, min, yMin, max, yMax, min, yMin);
                return ret;
            }
            if (yMin == 0.0d) {
                return min;
            }
            return max;
        }
        if (org.apache.commons.math.util.FastMath.abs(yMin) <= this.functionValueAccuracy) {
            setResult(min, 0);
            return min;
        }
        if (org.apache.commons.math.util.FastMath.abs(yMax) <= this.functionValueAccuracy) {
            setResult(max, 0);
            return max;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.SAME_SIGN_AT_ENDPOINTS, java.lang.Double.valueOf(min), java.lang.Double.valueOf(max), java.lang.Double.valueOf(yMin), java.lang.Double.valueOf(yMax));
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max);
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x013f  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x014f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private double solve(org.apache.commons.math.analysis.UnivariateRealFunction r42, double r43, double r45, double r47, double r49, double r51, double r53) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        /*
            Method dump skipped, instruction units count: 365
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.analysis.solvers.BrentSolver.solve(org.apache.commons.math.analysis.UnivariateRealFunction, double, double, double, double, double, double):double");
    }
}
