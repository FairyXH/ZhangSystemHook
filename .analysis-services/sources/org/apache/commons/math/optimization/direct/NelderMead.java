package org.apache.commons.math.optimization.direct;

/* JADX INFO: loaded from: classes4.dex */
public class NelderMead extends org.apache.commons.math.optimization.direct.DirectSearchOptimizer {
    private final double gamma;
    private final double khi;
    private final double rho;
    private final double sigma;

    public NelderMead() {
        this.rho = 1.0d;
        this.khi = 2.0d;
        this.gamma = 0.5d;
        this.sigma = 0.5d;
    }

    public NelderMead(double rho, double khi, double gamma, double sigma) {
        this.rho = rho;
        this.khi = khi;
        this.gamma = gamma;
        this.sigma = sigma;
    }

    @Override // org.apache.commons.math.optimization.direct.DirectSearchOptimizer
    protected void iterateSimplex(java.util.Comparator<org.apache.commons.math.optimization.RealPointValuePair> comparator) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
        incrementIterationsCounter();
        int n = this.simplex.length - 1;
        org.apache.commons.math.optimization.RealPointValuePair best = this.simplex[0];
        org.apache.commons.math.optimization.RealPointValuePair secondBest = this.simplex[n - 1];
        org.apache.commons.math.optimization.RealPointValuePair worst = this.simplex[n];
        double[] xWorst = worst.getPointRef();
        double[] centroid = new double[n];
        for (int i = 0; i < n; i++) {
            double[] x = this.simplex[i].getPointRef();
            for (int j = 0; j < n; j++) {
                centroid[j] = centroid[j] + x[j];
            }
        }
        double scaling = 1.0d / ((double) n);
        for (int j2 = 0; j2 < n; j2++) {
            centroid[j2] = centroid[j2] * scaling;
        }
        double[] xR = new double[n];
        int j3 = 0;
        while (j3 < n) {
            xR[j3] = centroid[j3] + (this.rho * (centroid[j3] - xWorst[j3]));
            j3++;
            secondBest = secondBest;
        }
        org.apache.commons.math.optimization.RealPointValuePair secondBest2 = secondBest;
        org.apache.commons.math.optimization.RealPointValuePair reflected = new org.apache.commons.math.optimization.RealPointValuePair(xR, evaluate(xR), false);
        if (comparator.compare(best, reflected) <= 0 && comparator.compare(reflected, secondBest2) < 0) {
            replaceWorstPoint(reflected, comparator);
            return;
        }
        if (comparator.compare(reflected, best) < 0) {
            double[] xE = new double[n];
            int j4 = 0;
            while (j4 < n) {
                xE[j4] = centroid[j4] + (this.khi * (xR[j4] - centroid[j4]));
                j4++;
                scaling = scaling;
            }
            org.apache.commons.math.optimization.RealPointValuePair expanded = new org.apache.commons.math.optimization.RealPointValuePair(xE, evaluate(xE), false);
            if (comparator.compare(expanded, reflected) < 0) {
                replaceWorstPoint(expanded, comparator);
            } else {
                replaceWorstPoint(reflected, comparator);
            }
            return;
        }
        if (comparator.compare(reflected, worst) < 0) {
            double[] xC = new double[n];
            for (int j5 = 0; j5 < n; j5++) {
                xC[j5] = centroid[j5] + (this.gamma * (xR[j5] - centroid[j5]));
            }
            org.apache.commons.math.optimization.RealPointValuePair outContracted = new org.apache.commons.math.optimization.RealPointValuePair(xC, evaluate(xC), false);
            if (comparator.compare(outContracted, reflected) <= 0) {
                replaceWorstPoint(outContracted, comparator);
                return;
            }
        } else {
            double[] xC2 = new double[n];
            int j6 = 0;
            while (j6 < n) {
                xC2[j6] = centroid[j6] - (this.gamma * (centroid[j6] - xWorst[j6]));
                j6++;
                best = best;
                reflected = reflected;
            }
            org.apache.commons.math.optimization.RealPointValuePair inContracted = new org.apache.commons.math.optimization.RealPointValuePair(xC2, evaluate(xC2), false);
            if (comparator.compare(inContracted, worst) < 0) {
                replaceWorstPoint(inContracted, comparator);
                return;
            }
        }
        double[] xSmallest = this.simplex[0].getPointRef();
        int i2 = 1;
        while (i2 < this.simplex.length) {
            double[] x2 = this.simplex[i2].getPoint();
            int j7 = 0;
            while (j7 < n) {
                x2[j7] = xSmallest[j7] + (this.sigma * (x2[j7] - xSmallest[j7]));
                j7++;
                n = n;
            }
            this.simplex[i2] = new org.apache.commons.math.optimization.RealPointValuePair(x2, Double.NaN, false);
            i2++;
            n = n;
        }
        evaluateSimplex(comparator);
    }
}
