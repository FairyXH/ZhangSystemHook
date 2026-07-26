package org.apache.commons.math.optimization.general;

/* JADX INFO: loaded from: classes4.dex */
public class GaussNewtonOptimizer extends org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer {
    private final boolean useLU;

    public GaussNewtonOptimizer(boolean useLU) {
        this.useLU = useLU;
    }

    @Override // org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer
    public org.apache.commons.math.optimization.VectorialPointValuePair doOptimize() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.DecompositionSolver solver;
        double weight;
        org.apache.commons.math.optimization.VectorialPointValuePair current = null;
        boolean converged = false;
        while (!converged) {
            incrementIterationsCounter();
            org.apache.commons.math.optimization.VectorialPointValuePair previous = current;
            updateResidualsAndCost();
            updateJacobian();
            org.apache.commons.math.optimization.VectorialPointValuePair current2 = new org.apache.commons.math.optimization.VectorialPointValuePair(this.point, this.objective);
            double[] b = new double[this.cols];
            double[][] a = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, this.cols, this.cols);
            for (int i = 0; i < this.rows; i++) {
                double[] grad = this.jacobian[i];
                double weight2 = this.residualsWeights[i];
                double residual = this.objective[i] - this.targetValues[i];
                double wr = weight2 * residual;
                for (int j = 0; j < this.cols; j++) {
                    b[j] = b[j] + (grad[j] * wr);
                }
                int k = 0;
                while (k < this.cols) {
                    double[] ak = a[k];
                    double wgk = grad[k] * weight2;
                    boolean converged2 = converged;
                    int l = 0;
                    while (true) {
                        weight = weight2;
                        if (l < this.cols) {
                            ak[l] = ak[l] + (grad[l] * wgk);
                            l++;
                            weight2 = weight;
                        }
                    }
                    k++;
                    converged = converged2;
                    weight2 = weight;
                }
            }
            boolean converged3 = converged;
            try {
                org.apache.commons.math.linear.RealMatrix mA = new org.apache.commons.math.linear.BlockRealMatrix(a);
                if (this.useLU) {
                    solver = new org.apache.commons.math.linear.LUDecompositionImpl(mA).getSolver();
                } else {
                    solver = new org.apache.commons.math.linear.QRDecompositionImpl(mA).getSolver();
                }
                double[] dX = solver.solve(b);
                for (int i2 = 0; i2 < this.cols; i2++) {
                    double[] dArr = this.point;
                    dArr[i2] = dArr[i2] + dX[i2];
                }
                if (previous == null) {
                    converged = converged3;
                } else {
                    converged = this.checker.converged(getIterations(), previous, current2);
                }
                current = current2;
            } catch (org.apache.commons.math.linear.InvalidMatrixException e) {
                throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, new java.lang.Object[0]);
            }
        }
        return current;
    }
}
