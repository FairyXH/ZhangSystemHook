package org.apache.commons.math.optimization.linear;

/* JADX INFO: loaded from: classes4.dex */
public class SimplexSolver extends org.apache.commons.math.optimization.linear.AbstractLinearOptimizer {
    private static final double DEFAULT_EPSILON = 1.0E-6d;
    protected final double epsilon;

    public SimplexSolver() {
        this(1.0E-6d);
    }

    public SimplexSolver(double epsilon) {
        this.epsilon = epsilon;
    }

    private java.lang.Integer getPivotColumn(org.apache.commons.math.optimization.linear.SimplexTableau tableau) {
        double minValue = 0.0d;
        java.lang.Integer minPos = null;
        for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getWidth() - 1; i++) {
            if (org.apache.commons.math.util.MathUtils.compareTo(tableau.getEntry(0, i), minValue, this.epsilon) < 0) {
                minValue = tableau.getEntry(0, i);
                minPos = java.lang.Integer.valueOf(i);
            }
        }
        return minPos;
    }

    private java.lang.Integer getPivotRow(org.apache.commons.math.optimization.linear.SimplexTableau tableau, int col) {
        java.util.List<java.lang.Integer> minRatioPositions = new java.util.ArrayList<>();
        double minRatio = Double.MAX_VALUE;
        for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
            double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
            double entry = tableau.getEntry(i, col);
            if (org.apache.commons.math.util.MathUtils.compareTo(entry, 0.0d, this.epsilon) > 0) {
                double ratio = rhs / entry;
                if (org.apache.commons.math.util.MathUtils.equals(ratio, minRatio, this.epsilon)) {
                    minRatioPositions.add(java.lang.Integer.valueOf(i));
                } else if (ratio < minRatio) {
                    minRatioPositions = new java.util.ArrayList<>();
                    minRatioPositions.add(java.lang.Integer.valueOf(i));
                    minRatio = ratio;
                }
            }
        }
        if (minRatioPositions.size() == 0) {
            return null;
        }
        if (minRatioPositions.size() > 1) {
            for (java.lang.Integer row : minRatioPositions) {
                for (int i2 = 0; i2 < tableau.getNumArtificialVariables(); i2++) {
                    int column = tableau.getArtificialVariableOffset() + i2;
                    if (org.apache.commons.math.util.MathUtils.equals(tableau.getEntry(row.intValue(), column), 1.0d, this.epsilon) && row.equals(tableau.getBasicRow(column))) {
                        return row;
                    }
                }
            }
        }
        return minRatioPositions.get(0);
    }

    protected void doIteration(org.apache.commons.math.optimization.linear.SimplexTableau tableau) throws org.apache.commons.math.optimization.OptimizationException {
        incrementIterationsCounter();
        java.lang.Integer pivotCol = getPivotColumn(tableau);
        java.lang.Integer pivotRow = getPivotRow(tableau, pivotCol.intValue());
        if (pivotRow == null) {
            throw new org.apache.commons.math.optimization.linear.UnboundedSolutionException();
        }
        double pivotVal = tableau.getEntry(pivotRow.intValue(), pivotCol.intValue());
        tableau.divideRow(pivotRow.intValue(), pivotVal);
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow.intValue()) {
                double multiplier = tableau.getEntry(i, pivotCol.intValue());
                tableau.subtractRow(i, pivotRow.intValue(), multiplier);
            }
        }
    }

    protected void solvePhase1(org.apache.commons.math.optimization.linear.SimplexTableau tableau) throws org.apache.commons.math.optimization.OptimizationException {
        if (tableau.getNumArtificialVariables() == 0) {
            return;
        }
        while (!tableau.isOptimal()) {
            doIteration(tableau);
        }
        if (!org.apache.commons.math.util.MathUtils.equals(tableau.getEntry(0, tableau.getRhsOffset()), 0.0d, this.epsilon)) {
            throw new org.apache.commons.math.optimization.linear.NoFeasibleSolutionException();
        }
    }

    @Override // org.apache.commons.math.optimization.linear.AbstractLinearOptimizer
    public org.apache.commons.math.optimization.RealPointValuePair doOptimize() throws org.apache.commons.math.optimization.OptimizationException {
        org.apache.commons.math.optimization.linear.SimplexTableau tableau = new org.apache.commons.math.optimization.linear.SimplexTableau(this.function, this.linearConstraints, this.goal, this.nonNegative, this.epsilon);
        solvePhase1(tableau);
        tableau.dropPhase1Objective();
        while (!tableau.isOptimal()) {
            doIteration(tableau);
        }
        return tableau.getSolution();
    }
}
