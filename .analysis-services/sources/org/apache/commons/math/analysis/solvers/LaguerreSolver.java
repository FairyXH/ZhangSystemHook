package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public class LaguerreSolver extends org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl {

    @java.lang.Deprecated
    private final org.apache.commons.math.analysis.polynomials.PolynomialFunction p;

    @java.lang.Deprecated
    public LaguerreSolver(org.apache.commons.math.analysis.UnivariateRealFunction f) throws java.lang.IllegalArgumentException {
        super(f, 100, 1.0E-6d);
        if (f instanceof org.apache.commons.math.analysis.polynomials.PolynomialFunction) {
            this.p = (org.apache.commons.math.analysis.polynomials.PolynomialFunction) f;
            return;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FUNCTION_NOT_POLYNOMIAL, new java.lang.Object[0]);
    }

    @java.lang.Deprecated
    public LaguerreSolver() {
        super(100, 1.0E-6d);
        this.p = null;
    }

    @java.lang.Deprecated
    public org.apache.commons.math.analysis.polynomials.PolynomialFunction getPolynomialFunction() {
        return new org.apache.commons.math.analysis.polynomials.PolynomialFunction(this.p.getCoefficients());
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        return solve(this.p, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        return solve(this.p, min, max, initial);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max, initial);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        if (f.value(min) == 0.0d) {
            return min;
        }
        if (f.value(max) == 0.0d) {
            return max;
        }
        if (f.value(initial) == 0.0d) {
            return initial;
        }
        verifyBracketing(min, max, f);
        verifySequence(min, initial, max);
        if (isBracketing(min, initial, f)) {
            return solve(f, min, initial);
        }
        return solve(f, initial, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        if (!(f instanceof org.apache.commons.math.analysis.polynomials.PolynomialFunction)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FUNCTION_NOT_POLYNOMIAL, new java.lang.Object[0]);
        }
        if (f.value(min) == 0.0d) {
            return min;
        }
        if (f.value(max) == 0.0d) {
            return max;
        }
        verifyBracketing(min, max, f);
        double[] coefficients = ((org.apache.commons.math.analysis.polynomials.PolynomialFunction) f).getCoefficients();
        org.apache.commons.math.complex.Complex[] c = new org.apache.commons.math.complex.Complex[coefficients.length];
        for (int i = 0; i < coefficients.length; i++) {
            c[i] = new org.apache.commons.math.complex.Complex(coefficients[i], 0.0d);
        }
        org.apache.commons.math.complex.Complex initial = new org.apache.commons.math.complex.Complex((min + max) * 0.5d, 0.0d);
        org.apache.commons.math.complex.Complex z = solve(c, initial);
        if (isRootOK(min, max, z)) {
            setResult(z.getReal(), this.iterationCount);
            return this.result;
        }
        org.apache.commons.math.complex.Complex[] root = solveAll(c, initial);
        for (int i2 = 0; i2 < root.length; i2++) {
            if (isRootOK(min, max, root[i2])) {
                setResult(root[i2].getReal(), this.iterationCount);
                return this.result;
            }
        }
        throw new org.apache.commons.math.ConvergenceException();
    }

    protected boolean isRootOK(double min, double max, org.apache.commons.math.complex.Complex z) {
        double tolerance = org.apache.commons.math.util.FastMath.max(this.relativeAccuracy * z.abs(), this.absoluteAccuracy);
        return isSequence(min, z.getReal(), max) && (org.apache.commons.math.util.FastMath.abs(z.getImaginary()) <= tolerance || z.abs() <= this.functionValueAccuracy);
    }

    @java.lang.Deprecated
    public org.apache.commons.math.complex.Complex[] solveAll(double[] coefficients, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        org.apache.commons.math.complex.Complex[] c = new org.apache.commons.math.complex.Complex[coefficients.length];
        org.apache.commons.math.complex.Complex z = new org.apache.commons.math.complex.Complex(initial, 0.0d);
        for (int i = 0; i < c.length; i++) {
            c[i] = new org.apache.commons.math.complex.Complex(coefficients[i], 0.0d);
        }
        return solveAll(c, z);
    }

    @java.lang.Deprecated
    public org.apache.commons.math.complex.Complex[] solveAll(org.apache.commons.math.complex.Complex[] coefficients, org.apache.commons.math.complex.Complex initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        int n = coefficients.length - 1;
        int iterationCount = 0;
        if (n < 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NON_POSITIVE_POLYNOMIAL_DEGREE, java.lang.Integer.valueOf(n));
        }
        org.apache.commons.math.complex.Complex[] c = new org.apache.commons.math.complex.Complex[n + 1];
        for (int i = 0; i <= n; i++) {
            c[i] = coefficients[i];
        }
        org.apache.commons.math.complex.Complex[] root = new org.apache.commons.math.complex.Complex[n];
        for (int i2 = 0; i2 < n; i2++) {
            org.apache.commons.math.complex.Complex[] subarray = new org.apache.commons.math.complex.Complex[(n - i2) + 1];
            java.lang.System.arraycopy(c, 0, subarray, 0, subarray.length);
            root[i2] = solve(subarray, initial);
            org.apache.commons.math.complex.Complex newc = c[n - i2];
            for (int j = (n - i2) - 1; j >= 0; j--) {
                org.apache.commons.math.complex.Complex oldc = c[j];
                c[j] = newc;
                newc = oldc.add(newc.multiply(root[i2]));
            }
            int j2 = this.iterationCount;
            iterationCount += j2;
        }
        this.resultComputed = true;
        this.iterationCount = iterationCount;
        return root;
    }

    @java.lang.Deprecated
    public org.apache.commons.math.complex.Complex solve(org.apache.commons.math.complex.Complex[] coefficients, org.apache.commons.math.complex.Complex initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        org.apache.commons.math.complex.Complex G;
        org.apache.commons.math.complex.Complex d2v;
        org.apache.commons.math.complex.Complex z;
        org.apache.commons.math.complex.Complex[] complexArr = coefficients;
        int n = complexArr.length - 1;
        if (n < 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NON_POSITIVE_POLYNOMIAL_DEGREE, java.lang.Integer.valueOf(n));
        }
        org.apache.commons.math.complex.Complex N = new org.apache.commons.math.complex.Complex(n, 0.0d);
        org.apache.commons.math.complex.Complex N1 = new org.apache.commons.math.complex.Complex(n - 1, 0.0d);
        int i = 1;
        org.apache.commons.math.complex.Complex G2 = null;
        org.apache.commons.math.complex.Complex G22 = null;
        org.apache.commons.math.complex.Complex H = null;
        org.apache.commons.math.complex.Complex G23 = null;
        org.apache.commons.math.complex.Complex oldz = new org.apache.commons.math.complex.Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        org.apache.commons.math.complex.Complex z2 = initial;
        while (i <= this.maximalIterationCount) {
            org.apache.commons.math.complex.Complex pv = complexArr[n];
            org.apache.commons.math.complex.Complex pv2 = org.apache.commons.math.complex.Complex.ZERO;
            org.apache.commons.math.complex.Complex d2v2 = org.apache.commons.math.complex.Complex.ZERO;
            int j = n - 1;
            org.apache.commons.math.complex.Complex pv3 = pv;
            org.apache.commons.math.complex.Complex dv = pv2;
            while (j >= 0) {
                org.apache.commons.math.complex.Complex G3 = G2;
                org.apache.commons.math.complex.Complex G4 = z2.multiply(d2v2);
                d2v2 = dv.add(G4);
                dv = pv3.add(z2.multiply(dv));
                pv3 = complexArr[j].add(z2.multiply(pv3));
                j--;
                complexArr = coefficients;
                G2 = G3;
            }
            org.apache.commons.math.complex.Complex d2v3 = d2v2.multiply(new org.apache.commons.math.complex.Complex(2.0d, 0.0d));
            double tolerance = org.apache.commons.math.util.FastMath.max(this.relativeAccuracy * z2.abs(), this.absoluteAccuracy);
            if (z2.subtract(oldz).abs() <= tolerance) {
                this.resultComputed = true;
                this.iterationCount = i;
                return z2;
            }
            if (pv3.abs() <= this.functionValueAccuracy) {
                this.resultComputed = true;
                this.iterationCount = i;
                return z2;
            }
            org.apache.commons.math.complex.Complex G5 = dv.divide(pv3);
            org.apache.commons.math.complex.Complex G24 = G5.multiply(G5);
            H = G24.subtract(d2v3.divide(pv3));
            org.apache.commons.math.complex.Complex delta = N1.multiply(N.multiply(H).subtract(G24));
            org.apache.commons.math.complex.Complex deltaSqrt = delta.sqrt();
            org.apache.commons.math.complex.Complex dplus = G5.add(deltaSqrt);
            org.apache.commons.math.complex.Complex dminus = G5.subtract(deltaSqrt);
            org.apache.commons.math.complex.Complex denominator = dplus.abs() > dminus.abs() ? dplus : dminus;
            if (denominator.equals(new org.apache.commons.math.complex.Complex(0.0d, 0.0d))) {
                G = G5;
                d2v = d2v3;
                z2 = z2.add(new org.apache.commons.math.complex.Complex(this.absoluteAccuracy, this.absoluteAccuracy));
                z = new org.apache.commons.math.complex.Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            } else {
                G = G5;
                d2v = d2v3;
                z = z2;
                z2 = z2.subtract(N.divide(denominator));
            }
            i++;
            oldz = z;
            G22 = G24;
            G23 = delta;
            G2 = G;
            complexArr = coefficients;
        }
        throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
    }
}
