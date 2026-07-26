package org.apache.commons.math.optimization.direct;

/* JADX INFO: loaded from: classes4.dex */
public class PowellOptimizer extends org.apache.commons.math.optimization.general.AbstractScalarDifferentiableOptimizer {
    public static final double DEFAULT_LS_ABSOLUTE_TOLERANCE = 1.0E-11d;
    public static final double DEFAULT_LS_RELATIVE_TOLERANCE = 1.0E-7d;
    private final org.apache.commons.math.optimization.direct.PowellOptimizer.LineSearch line;

    public PowellOptimizer() {
        this(1.0E-7d, 1.0E-11d);
    }

    public PowellOptimizer(double lsRelativeTolerance) {
        this(lsRelativeTolerance, 1.0E-11d);
    }

    public PowellOptimizer(double lsRelativeTolerance, double lsAbsoluteTolerance) {
        this.line = new org.apache.commons.math.optimization.direct.PowellOptimizer.LineSearch(lsRelativeTolerance, lsAbsoluteTolerance);
    }

    @Override // org.apache.commons.math.optimization.general.AbstractScalarDifferentiableOptimizer
    protected org.apache.commons.math.optimization.RealPointValuePair doOptimize() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
        double fX;
        org.apache.commons.math.optimization.RealPointValuePair previous;
        org.apache.commons.math.optimization.RealPointValuePair current;
        int i;
        boolean z;
        double[] guess = (double[]) this.point.clone();
        int n = guess.length;
        double[][] direc = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, n);
        for (int i2 = 0; i2 < n; i2++) {
            direc[i2][i2] = 1.0d;
        }
        double[] x = guess;
        double fVal = computeObjectiveValue(x);
        double[] x1 = (double[]) x.clone();
        while (true) {
            incrementIterationsCounter();
            fX = fVal;
            double delta = 0.0d;
            int bigInd = 0;
            int i3 = 0;
            while (i3 < n) {
                double[] d = copyOf(direc[i3], n);
                double fX2 = fVal;
                double[] guess2 = guess;
                this.line.search(x, d);
                fVal = this.line.getValueAtOptimum();
                int n2 = n;
                double alphaMin = this.line.getOptimum();
                x = newPointAndDirection(x, d, alphaMin)[0];
                if (fX2 - fVal > delta) {
                    delta = fX2 - fVal;
                    bigInd = i3;
                }
                i3++;
                guess = guess2;
                n = n2;
            }
            double[] guess3 = guess;
            int n3 = n;
            previous = new org.apache.commons.math.optimization.RealPointValuePair(x1, fX);
            current = new org.apache.commons.math.optimization.RealPointValuePair(x, fVal);
            if (getConvergenceChecker().converged(getIterations(), previous, current)) {
                break;
            }
            double[] d2 = new double[n3];
            double[] x2 = new double[n3];
            for (int i4 = 0; i4 < n3; i4++) {
                d2[i4] = x[i4] - x1[i4];
                x2[i4] = (x[i4] * 2.0d) - x1[i4];
            }
            x1 = (double[]) x.clone();
            double fX22 = computeObjectiveValue(x2);
            if (fX > fX22) {
                double t = ((fX + fX22) - (fVal * 2.0d)) * 2.0d;
                double temp = (fX - fVal) - delta;
                double t2 = t * temp * temp;
                double temp2 = fX - fX22;
                if (t2 - ((delta * temp2) * temp2) < 0.0d) {
                    this.line.search(x, d2);
                    fVal = this.line.getValueAtOptimum();
                    double alphaMin2 = this.line.getOptimum();
                    double[][] result = newPointAndDirection(x, d2, alphaMin2);
                    z = false;
                    x = result[0];
                    int lastInd = n3 - 1;
                    direc[bigInd] = direc[lastInd];
                    i = 1;
                    direc[lastInd] = result[1];
                } else {
                    i = 1;
                    z = false;
                }
            } else {
                i = 1;
                z = false;
            }
            n = n3;
            guess = guess3;
        }
        return this.goal == org.apache.commons.math.optimization.GoalType.MINIMIZE ? fVal < fX ? current : previous : fVal > fX ? current : previous;
    }

    private double[][] newPointAndDirection(double[] p, double[] d, double optimum) {
        int n = p.length;
        double[][] result = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, 2, n);
        double[] nP = result[0];
        double[] nD = result[1];
        for (int i = 0; i < n; i++) {
            nD[i] = d[i] * optimum;
            nP[i] = p[i] + nD[i];
        }
        return result;
    }

    private class LineSearch {
        private final org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer optim = new org.apache.commons.math.optimization.univariate.BrentOptimizer();
        private final org.apache.commons.math.optimization.univariate.BracketFinder bracket = new org.apache.commons.math.optimization.univariate.BracketFinder();
        private double optimum = Double.NaN;
        private double valueAtOptimum = Double.NaN;

        public LineSearch(double relativeTolerance, double absoluteTolerance) {
            this.optim.setRelativeAccuracy(relativeTolerance);
            this.optim.setAbsoluteAccuracy(absoluteTolerance);
        }

        public void search(final double[] p, final double[] d) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
            this.optimum = Double.NaN;
            this.valueAtOptimum = Double.NaN;
            try {
                final int n = p.length;
                try {
                    org.apache.commons.math.analysis.UnivariateRealFunction f = new org.apache.commons.math.analysis.UnivariateRealFunction() { // from class: org.apache.commons.math.optimization.direct.PowellOptimizer.LineSearch.1
                        @Override // org.apache.commons.math.analysis.UnivariateRealFunction
                        public double value(double alpha) throws org.apache.commons.math.FunctionEvaluationException {
                            double[] x = new double[n];
                            for (int i = 0; i < n; i++) {
                                x[i] = p[i] + (d[i] * alpha);
                            }
                            double obj = org.apache.commons.math.optimization.direct.PowellOptimizer.this.computeObjectiveValue(x);
                            return obj;
                        }
                    };
                    this.bracket.search(f, org.apache.commons.math.optimization.direct.PowellOptimizer.this.goal, 0.0d, 1.0d);
                    this.optimum = this.optim.optimize(f, org.apache.commons.math.optimization.direct.PowellOptimizer.this.goal, this.bracket.getLo(), this.bracket.getHi(), this.bracket.getMid());
                    this.valueAtOptimum = this.optim.getFunctionValue();
                } catch (org.apache.commons.math.MaxIterationsExceededException e) {
                    e = e;
                    throw new org.apache.commons.math.optimization.OptimizationException(e);
                }
            } catch (org.apache.commons.math.MaxIterationsExceededException e2) {
                e = e2;
            }
        }

        public double getOptimum() {
            return this.optimum;
        }

        public double getValueAtOptimum() {
            return this.valueAtOptimum;
        }
    }

    private double[] copyOf(double[] source, int newLen) {
        double[] output = new double[newLen];
        java.lang.System.arraycopy(source, 0, output, 0, java.lang.Math.min(source.length, newLen));
        return output;
    }
}
