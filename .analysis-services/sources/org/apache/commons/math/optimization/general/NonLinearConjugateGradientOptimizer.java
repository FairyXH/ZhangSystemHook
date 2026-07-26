package org.apache.commons.math.optimization.general;

/* JADX INFO: loaded from: classes4.dex */
public class NonLinearConjugateGradientOptimizer extends org.apache.commons.math.optimization.general.AbstractScalarDifferentiableOptimizer {
    private final org.apache.commons.math.optimization.general.ConjugateGradientFormula updateFormula;
    private org.apache.commons.math.optimization.general.Preconditioner preconditioner = null;
    private org.apache.commons.math.analysis.solvers.UnivariateRealSolver solver = null;
    private double initialStep = 1.0d;

    public NonLinearConjugateGradientOptimizer(org.apache.commons.math.optimization.general.ConjugateGradientFormula updateFormula) {
        this.updateFormula = updateFormula;
    }

    public void setPreconditioner(org.apache.commons.math.optimization.general.Preconditioner preconditioner) {
        this.preconditioner = preconditioner;
    }

    public void setLineSearchSolver(org.apache.commons.math.analysis.solvers.UnivariateRealSolver lineSearchSolver) {
        this.solver = lineSearchSolver;
    }

    public void setInitialStep(double initialStep) {
        if (initialStep <= 0.0d) {
            this.initialStep = 1.0d;
        } else {
            this.initialStep = initialStep;
        }
    }

    @Override // org.apache.commons.math.optimization.general.AbstractScalarDifferentiableOptimizer
    protected org.apache.commons.math.optimization.RealPointValuePair doOptimize() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException {
        double deltaMid;
        org.apache.commons.math.optimization.general.NonLinearConjugateGradientOptimizer nonLinearConjugateGradientOptimizer = this;
        try {
            if (nonLinearConjugateGradientOptimizer.preconditioner == null) {
                nonLinearConjugateGradientOptimizer.preconditioner = new org.apache.commons.math.optimization.general.NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
            }
            if (nonLinearConjugateGradientOptimizer.solver == null) {
                nonLinearConjugateGradientOptimizer.solver = new org.apache.commons.math.analysis.solvers.BrentSolver();
            }
            int n = nonLinearConjugateGradientOptimizer.point.length;
            double[] r = nonLinearConjugateGradientOptimizer.computeObjectiveGradient(nonLinearConjugateGradientOptimizer.point);
            if (nonLinearConjugateGradientOptimizer.goal == org.apache.commons.math.optimization.GoalType.MINIMIZE) {
                for (int i = 0; i < n; i++) {
                    r[i] = -r[i];
                }
            }
            double[] steepestDescent = nonLinearConjugateGradientOptimizer.preconditioner.precondition(nonLinearConjugateGradientOptimizer.point, r);
            double[] searchDirection = (double[]) steepestDescent.clone();
            double delta = 0.0d;
            for (int i2 = 0; i2 < n; i2++) {
                delta += r[i2] * searchDirection[i2];
            }
            org.apache.commons.math.optimization.RealPointValuePair current = null;
            double[] steepestDescent2 = steepestDescent;
            double[] searchDirection2 = searchDirection;
            double delta2 = delta;
            while (true) {
                double objective = nonLinearConjugateGradientOptimizer.computeObjectiveValue(nonLinearConjugateGradientOptimizer.point);
                org.apache.commons.math.optimization.RealPointValuePair previous = current;
                org.apache.commons.math.optimization.RealPointValuePair current2 = new org.apache.commons.math.optimization.RealPointValuePair(nonLinearConjugateGradientOptimizer.point, objective);
                if (previous != null && nonLinearConjugateGradientOptimizer.checker.converged(getIterations(), previous, current2)) {
                    return current2;
                }
                incrementIterationsCounter();
                double dTd = 0.0d;
                for (double di : searchDirection2) {
                    dTd += di * di;
                }
                org.apache.commons.math.analysis.UnivariateRealFunction lsf = nonLinearConjugateGradientOptimizer.new LineSearchFunction(searchDirection2);
                double step = nonLinearConjugateGradientOptimizer.solver.solve(lsf, 0.0d, findUpperBound(lsf, 0.0d, nonLinearConjugateGradientOptimizer.initialStep));
                for (int i3 = 0; i3 < nonLinearConjugateGradientOptimizer.point.length; i3++) {
                    double[] dArr = nonLinearConjugateGradientOptimizer.point;
                    dArr[i3] = dArr[i3] + (searchDirection2[i3] * step);
                }
                double[] r2 = nonLinearConjugateGradientOptimizer.computeObjectiveGradient(nonLinearConjugateGradientOptimizer.point);
                if (nonLinearConjugateGradientOptimizer.goal == org.apache.commons.math.optimization.GoalType.MINIMIZE) {
                    for (int i4 = 0; i4 < n; i4++) {
                        r2[i4] = -r2[i4];
                    }
                }
                double deltaOld = delta2;
                double[] newSteepestDescent = nonLinearConjugateGradientOptimizer.preconditioner.precondition(nonLinearConjugateGradientOptimizer.point, r2);
                delta2 = 0.0d;
                for (int i5 = 0; i5 < n; i5++) {
                    delta2 += r2[i5] * newSteepestDescent[i5];
                }
                if (nonLinearConjugateGradientOptimizer.updateFormula == org.apache.commons.math.optimization.general.ConjugateGradientFormula.FLETCHER_REEVES) {
                    deltaMid = delta2 / deltaOld;
                } else {
                    double deltaMid2 = 0.0d;
                    for (int i6 = 0; i6 < r2.length; i6++) {
                        deltaMid2 += r2[i6] * steepestDescent2[i6];
                    }
                    deltaMid = (delta2 - deltaMid2) / deltaOld;
                }
                steepestDescent2 = newSteepestDescent;
                if (getIterations() % n == 0 || deltaMid < 0.0d) {
                    searchDirection2 = (double[]) steepestDescent2.clone();
                } else {
                    for (int i7 = 0; i7 < n; i7++) {
                        searchDirection2[i7] = steepestDescent2[i7] + (searchDirection2[i7] * deltaMid);
                    }
                }
                nonLinearConjugateGradientOptimizer = this;
                current = current2;
            }
        } catch (org.apache.commons.math.ConvergenceException ce) {
            throw new org.apache.commons.math.optimization.OptimizationException(ce);
        }
    }

    private double findUpperBound(org.apache.commons.math.analysis.UnivariateRealFunction f, double a, double h) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
        double yA = f.value(a);
        double step = h;
        while (step < Double.MAX_VALUE) {
            double b = a + step;
            double yB = f.value(b);
            if (yA * yB > 0.0d) {
                step *= org.apache.commons.math.util.FastMath.max(2.0d, yA / yB);
            } else {
                return b;
            }
        }
        throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_BRACKET_OPTIMUM_IN_LINE_SEARCH, new java.lang.Object[0]);
    }

    private static class IdentityPreconditioner implements org.apache.commons.math.optimization.general.Preconditioner {
        private IdentityPreconditioner() {
        }

        @Override // org.apache.commons.math.optimization.general.Preconditioner
        public double[] precondition(double[] variables, double[] r) {
            return (double[]) r.clone();
        }
    }

    private class LineSearchFunction implements org.apache.commons.math.analysis.UnivariateRealFunction {
        private final double[] searchDirection;

        public LineSearchFunction(double[] searchDirection) {
            this.searchDirection = searchDirection;
        }

        @Override // org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
            double[] shiftedPoint = (double[]) org.apache.commons.math.optimization.general.NonLinearConjugateGradientOptimizer.this.point.clone();
            for (int i = 0; i < shiftedPoint.length; i++) {
                shiftedPoint[i] = shiftedPoint[i] + (this.searchDirection[i] * x);
            }
            double[] gradient = org.apache.commons.math.optimization.general.NonLinearConjugateGradientOptimizer.this.computeObjectiveGradient(shiftedPoint);
            double dotProduct = 0.0d;
            for (int i2 = 0; i2 < gradient.length; i2++) {
                dotProduct += gradient[i2] * this.searchDirection[i2];
            }
            return dotProduct;
        }
    }
}
