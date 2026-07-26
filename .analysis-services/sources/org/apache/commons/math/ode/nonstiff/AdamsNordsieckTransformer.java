package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public class AdamsNordsieckTransformer {
    private static final java.util.Map<java.lang.Integer, org.apache.commons.math.ode.nonstiff.AdamsNordsieckTransformer> CACHE = new java.util.HashMap();
    private final double[] c1;
    private final org.apache.commons.math.linear.Array2DRowRealMatrix initialization;
    private final org.apache.commons.math.linear.Array2DRowRealMatrix update;

    private AdamsNordsieckTransformer(int nSteps) {
        org.apache.commons.math.linear.FieldMatrix<org.apache.commons.math.fraction.BigFraction> bigP = buildP(nSteps);
        org.apache.commons.math.linear.FieldDecompositionSolver<org.apache.commons.math.fraction.BigFraction> pSolver = new org.apache.commons.math.linear.FieldLUDecompositionImpl(bigP).getSolver();
        org.apache.commons.math.fraction.BigFraction[] u = new org.apache.commons.math.fraction.BigFraction[nSteps];
        java.util.Arrays.fill(u, org.apache.commons.math.fraction.BigFraction.ONE);
        org.apache.commons.math.fraction.BigFraction[] bigC1 = (org.apache.commons.math.fraction.BigFraction[]) pSolver.solve(u);
        org.apache.commons.math.fraction.BigFraction[][] shiftedP = (org.apache.commons.math.fraction.BigFraction[][]) bigP.getData();
        for (int i = shiftedP.length - 1; i > 0; i--) {
            shiftedP[i] = shiftedP[i - 1];
        }
        shiftedP[0] = new org.apache.commons.math.fraction.BigFraction[nSteps];
        java.util.Arrays.fill(shiftedP[0], org.apache.commons.math.fraction.BigFraction.ZERO);
        org.apache.commons.math.linear.FieldMatrix fieldMatrixSolve = pSolver.solve(new org.apache.commons.math.linear.Array2DRowFieldMatrix(shiftedP, false));
        bigP.walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultFieldMatrixChangingVisitor<org.apache.commons.math.fraction.BigFraction>(org.apache.commons.math.fraction.BigFraction.ZERO) { // from class: org.apache.commons.math.ode.nonstiff.AdamsNordsieckTransformer.1
            @Override // org.apache.commons.math.linear.DefaultFieldMatrixChangingVisitor, org.apache.commons.math.linear.FieldMatrixChangingVisitor
            public org.apache.commons.math.fraction.BigFraction visit(int row, int column, org.apache.commons.math.fraction.BigFraction value) {
                return (column & 1) == 1 ? value : value.negate();
            }
        });
        org.apache.commons.math.linear.FieldMatrix<org.apache.commons.math.fraction.BigFraction> bigRInverse = new org.apache.commons.math.linear.FieldLUDecompositionImpl(bigP).getSolver().getInverse();
        this.initialization = org.apache.commons.math.linear.MatrixUtils.bigFractionMatrixToRealMatrix(bigRInverse);
        this.update = org.apache.commons.math.linear.MatrixUtils.bigFractionMatrixToRealMatrix(fieldMatrixSolve);
        this.c1 = new double[nSteps];
        for (int i2 = 0; i2 < nSteps; i2++) {
            this.c1[i2] = bigC1[i2].doubleValue();
        }
    }

    public static org.apache.commons.math.ode.nonstiff.AdamsNordsieckTransformer getInstance(int nSteps) {
        org.apache.commons.math.ode.nonstiff.AdamsNordsieckTransformer t;
        synchronized (CACHE) {
            t = CACHE.get(java.lang.Integer.valueOf(nSteps));
            if (t == null) {
                t = new org.apache.commons.math.ode.nonstiff.AdamsNordsieckTransformer(nSteps);
                CACHE.put(java.lang.Integer.valueOf(nSteps), t);
            }
        }
        return t;
    }

    public int getNSteps() {
        return this.c1.length;
    }

    private org.apache.commons.math.linear.FieldMatrix<org.apache.commons.math.fraction.BigFraction> buildP(int nSteps) {
        org.apache.commons.math.fraction.BigFraction[][] pData = (org.apache.commons.math.fraction.BigFraction[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) org.apache.commons.math.fraction.BigFraction.class, nSteps, nSteps);
        for (int i = 0; i < pData.length; i++) {
            org.apache.commons.math.fraction.BigFraction[] pI = pData[i];
            int factor = -(i + 1);
            int aj = factor;
            for (int j = 0; j < pI.length; j++) {
                pI[j] = new org.apache.commons.math.fraction.BigFraction((j + 2) * aj);
                aj *= factor;
            }
        }
        return new org.apache.commons.math.linear.Array2DRowFieldMatrix(pData, false);
    }

    public org.apache.commons.math.linear.Array2DRowRealMatrix initializeHighOrderDerivatives(double[] first, double[][] multistep) {
        for (double[] msI : multistep) {
            for (int j = 0; j < first.length; j++) {
                msI[j] = msI[j] - first[j];
            }
        }
        return this.initialization.multiply(new org.apache.commons.math.linear.Array2DRowRealMatrix(multistep, false));
    }

    public org.apache.commons.math.linear.Array2DRowRealMatrix updateHighOrderDerivativesPhase1(org.apache.commons.math.linear.Array2DRowRealMatrix highOrder) {
        return this.update.multiply(highOrder);
    }

    public void updateHighOrderDerivativesPhase2(double[] start, double[] end, org.apache.commons.math.linear.Array2DRowRealMatrix highOrder) {
        double[][] data = highOrder.getDataRef();
        for (int i = 0; i < data.length; i++) {
            double[] dataI = data[i];
            double c1I = this.c1[i];
            for (int j = 0; j < dataI.length; j++) {
                dataI[j] = dataI[j] + ((start[j] - end[j]) * c1I);
            }
        }
    }
}
