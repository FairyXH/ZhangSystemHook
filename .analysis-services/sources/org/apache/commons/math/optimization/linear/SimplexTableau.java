package org.apache.commons.math.optimization.linear;

/* JADX INFO: loaded from: classes4.dex */
class SimplexTableau implements java.io.Serializable {
    private static final java.lang.String NEGATIVE_VAR_COLUMN_LABEL = "x-";
    private static final long serialVersionUID = -1369660067587938365L;
    private final java.util.List<org.apache.commons.math.optimization.linear.LinearConstraint> constraints;
    private final double epsilon;
    private final org.apache.commons.math.optimization.linear.LinearObjectiveFunction f;
    private final int numDecisionVariables;
    private final boolean restrictToNonNegative;
    private transient org.apache.commons.math.linear.RealMatrix tableau;
    private final java.util.List<java.lang.String> columnLabels = new java.util.ArrayList();
    private final int numSlackVariables = getConstraintTypeCounts(org.apache.commons.math.optimization.linear.Relationship.LEQ) + getConstraintTypeCounts(org.apache.commons.math.optimization.linear.Relationship.GEQ);
    private int numArtificialVariables = getConstraintTypeCounts(org.apache.commons.math.optimization.linear.Relationship.EQ) + getConstraintTypeCounts(org.apache.commons.math.optimization.linear.Relationship.GEQ);

    SimplexTableau(org.apache.commons.math.optimization.linear.LinearObjectiveFunction linearObjectiveFunction, java.util.Collection<org.apache.commons.math.optimization.linear.LinearConstraint> collection, org.apache.commons.math.optimization.GoalType goalType, boolean z, double d) {
        this.f = linearObjectiveFunction;
        this.constraints = normalizeConstraints(collection);
        this.restrictToNonNegative = z;
        this.epsilon = d;
        this.numDecisionVariables = linearObjectiveFunction.getCoefficients().getDimension() + (!z ? 1 : 0);
        this.tableau = createTableau(goalType == org.apache.commons.math.optimization.GoalType.MAXIMIZE);
        initializeColumnLabels();
    }

    protected void initializeColumnLabels() {
        if (getNumObjectiveFunctions() == 2) {
            this.columnLabels.add("W");
        }
        this.columnLabels.add("Z");
        for (int i = 0; i < getOriginalNumDecisionVariables(); i++) {
            this.columnLabels.add("x" + i);
        }
        if (!this.restrictToNonNegative) {
            this.columnLabels.add(NEGATIVE_VAR_COLUMN_LABEL);
        }
        for (int i2 = 0; i2 < getNumSlackVariables(); i2++) {
            this.columnLabels.add("s" + i2);
        }
        for (int i3 = 0; i3 < getNumArtificialVariables(); i3++) {
            this.columnLabels.add(com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD + i3);
        }
        this.columnLabels.add("RHS");
    }

    protected org.apache.commons.math.linear.RealMatrix createTableau(boolean maximize) {
        int height;
        boolean z;
        int i = 1;
        int width = this.numDecisionVariables + this.numSlackVariables + this.numArtificialVariables + getNumObjectiveFunctions() + 1;
        int height2 = this.constraints.size() + getNumObjectiveFunctions();
        org.apache.commons.math.linear.Array2DRowRealMatrix matrix = new org.apache.commons.math.linear.Array2DRowRealMatrix(height2, width);
        if (getNumObjectiveFunctions() == 2) {
            matrix.setEntry(0, 0, -1.0d);
        }
        int zIndex = getNumObjectiveFunctions() == 1 ? 0 : 1;
        matrix.setEntry(zIndex, zIndex, maximize ? 1.0d : -1.0d);
        org.apache.commons.math.linear.RealVector objectiveCoefficients = this.f.getCoefficients();
        if (maximize) {
            objectiveCoefficients = objectiveCoefficients.mapMultiply(-1.0d);
        }
        copyArray(objectiveCoefficients.getData(), matrix.getDataRef()[zIndex]);
        int i2 = width - 1;
        double constantTerm = this.f.getConstantTerm();
        if (!maximize) {
            constantTerm *= -1.0d;
        }
        matrix.setEntry(zIndex, i2, constantTerm);
        if (!this.restrictToNonNegative) {
            matrix.setEntry(zIndex, getSlackVariableOffset() - 1, getInvertedCoeffiecientSum(objectiveCoefficients));
        }
        int slackVar = 0;
        int artificialVar = 0;
        int i3 = 0;
        while (i3 < this.constraints.size()) {
            org.apache.commons.math.optimization.linear.LinearConstraint constraint = this.constraints.get(i3);
            int row = getNumObjectiveFunctions() + i3;
            copyArray(constraint.getCoefficients().getData(), matrix.getDataRef()[row]);
            if (this.restrictToNonNegative) {
                height = height2;
            } else {
                height = height2;
                matrix.setEntry(row, getSlackVariableOffset() - i, getInvertedCoeffiecientSum(constraint.getCoefficients()));
            }
            matrix.setEntry(row, width - 1, constraint.getValue());
            if (constraint.getRelationship() == org.apache.commons.math.optimization.linear.Relationship.LEQ) {
                matrix.setEntry(row, getSlackVariableOffset() + slackVar, 1.0d);
                slackVar++;
            } else if (constraint.getRelationship() == org.apache.commons.math.optimization.linear.Relationship.GEQ) {
                matrix.setEntry(row, getSlackVariableOffset() + slackVar, -1.0d);
                slackVar++;
            }
            if (constraint.getRelationship() == org.apache.commons.math.optimization.linear.Relationship.EQ || constraint.getRelationship() == org.apache.commons.math.optimization.linear.Relationship.GEQ) {
                z = false;
                matrix.setEntry(0, getArtificialVariableOffset() + artificialVar, 1.0d);
                matrix.setEntry(row, getArtificialVariableOffset() + artificialVar, 1.0d);
                matrix.setRowVector(0, matrix.getRowVector(0).subtract(matrix.getRowVector(row)));
                artificialVar++;
            } else {
                z = false;
            }
            i3++;
            height2 = height;
            i = 1;
        }
        return matrix;
    }

    public java.util.List<org.apache.commons.math.optimization.linear.LinearConstraint> normalizeConstraints(java.util.Collection<org.apache.commons.math.optimization.linear.LinearConstraint> originalConstraints) {
        java.util.List<org.apache.commons.math.optimization.linear.LinearConstraint> normalized = new java.util.ArrayList<>();
        for (org.apache.commons.math.optimization.linear.LinearConstraint constraint : originalConstraints) {
            normalized.add(normalize(constraint));
        }
        return normalized;
    }

    private org.apache.commons.math.optimization.linear.LinearConstraint normalize(org.apache.commons.math.optimization.linear.LinearConstraint constraint) {
        if (constraint.getValue() < 0.0d) {
            return new org.apache.commons.math.optimization.linear.LinearConstraint(constraint.getCoefficients().mapMultiply(-1.0d), constraint.getRelationship().oppositeRelationship(), constraint.getValue() * (-1.0d));
        }
        return new org.apache.commons.math.optimization.linear.LinearConstraint(constraint.getCoefficients(), constraint.getRelationship(), constraint.getValue());
    }

    protected final int getNumObjectiveFunctions() {
        return this.numArtificialVariables > 0 ? 2 : 1;
    }

    private int getConstraintTypeCounts(org.apache.commons.math.optimization.linear.Relationship relationship) {
        int count = 0;
        for (org.apache.commons.math.optimization.linear.LinearConstraint constraint : this.constraints) {
            if (constraint.getRelationship() == relationship) {
                count++;
            }
        }
        return count;
    }

    protected static double getInvertedCoeffiecientSum(org.apache.commons.math.linear.RealVector coefficients) {
        double sum = 0.0d;
        for (double coefficient : coefficients.getData()) {
            sum -= coefficient;
        }
        return sum;
    }

    protected java.lang.Integer getBasicRow(int col) {
        java.lang.Integer row = null;
        for (int i = 0; i < getHeight(); i++) {
            if (org.apache.commons.math.util.MathUtils.equals(getEntry(i, col), 1.0d, this.epsilon) && row == null) {
                row = java.lang.Integer.valueOf(i);
            } else if (!org.apache.commons.math.util.MathUtils.equals(getEntry(i, col), 0.0d, this.epsilon)) {
                return null;
            }
        }
        return row;
    }

    protected void dropPhase1Objective() {
        if (getNumObjectiveFunctions() == 1) {
            return;
        }
        java.util.List<java.lang.Integer> columnsToDrop = new java.util.ArrayList<>();
        columnsToDrop.add(0);
        for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
            if (org.apache.commons.math.util.MathUtils.compareTo(this.tableau.getEntry(0, i), 0.0d, this.epsilon) > 0) {
                columnsToDrop.add(java.lang.Integer.valueOf(i));
            }
        }
        for (int i2 = 0; i2 < getNumArtificialVariables(); i2++) {
            int col = getArtificialVariableOffset() + i2;
            if (getBasicRow(col) == null) {
                columnsToDrop.add(java.lang.Integer.valueOf(col));
            }
        }
        int i3 = getHeight();
        double[][] matrix = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, i3 - 1, getWidth() - columnsToDrop.size());
        for (int i4 = 1; i4 < getHeight(); i4++) {
            int col2 = 0;
            for (int j = 0; j < getWidth(); j++) {
                if (!columnsToDrop.contains(java.lang.Integer.valueOf(j))) {
                    matrix[i4 - 1][col2] = this.tableau.getEntry(i4, j);
                    col2++;
                }
            }
        }
        int i5 = columnsToDrop.size();
        for (int i6 = i5 - 1; i6 >= 0; i6--) {
            this.columnLabels.remove(columnsToDrop.get(i6).intValue());
        }
        this.tableau = new org.apache.commons.math.linear.Array2DRowRealMatrix(matrix);
        this.numArtificialVariables = 0;
    }

    private void copyArray(double[] src, double[] dest) {
        java.lang.System.arraycopy(src, 0, dest, getNumObjectiveFunctions(), src.length);
    }

    boolean isOptimal() {
        for (int i = getNumObjectiveFunctions(); i < getWidth() - 1; i++) {
            if (org.apache.commons.math.util.MathUtils.compareTo(this.tableau.getEntry(0, i), 0.0d, this.epsilon) < 0) {
                return false;
            }
        }
        return true;
    }

    protected org.apache.commons.math.optimization.RealPointValuePair getSolution() {
        int negativeVarColumn = this.columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
        java.lang.Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
        double mostNegative = negativeVarBasicRow == null ? 0.0d : getEntry(negativeVarBasicRow.intValue(), getRhsOffset());
        java.util.Set<java.lang.Integer> basicRows = new java.util.HashSet<>();
        double[] coefficients = new double[getOriginalNumDecisionVariables()];
        for (int i = 0; i < coefficients.length; i++) {
            int colIndex = this.columnLabels.indexOf("x" + i);
            if (colIndex < 0) {
                coefficients[i] = 0.0d;
            } else {
                java.lang.Integer basicRow = getBasicRow(colIndex);
                if (basicRows.contains(basicRow)) {
                    coefficients[i] = 0.0d;
                } else {
                    basicRows.add(basicRow);
                    coefficients[i] = (basicRow == null ? 0.0d : getEntry(basicRow.intValue(), getRhsOffset())) - (this.restrictToNonNegative ? 0.0d : mostNegative);
                }
            }
        }
        return new org.apache.commons.math.optimization.RealPointValuePair(coefficients, this.f.getValue(coefficients));
    }

    protected void divideRow(int dividendRow, double divisor) {
        for (int j = 0; j < getWidth(); j++) {
            this.tableau.setEntry(dividendRow, j, this.tableau.getEntry(dividendRow, j) / divisor);
        }
    }

    protected void subtractRow(int minuendRow, int subtrahendRow, double multiple) {
        this.tableau.setRowVector(minuendRow, this.tableau.getRowVector(minuendRow).subtract(this.tableau.getRowVector(subtrahendRow).mapMultiply(multiple)));
    }

    protected final int getWidth() {
        return this.tableau.getColumnDimension();
    }

    protected final int getHeight() {
        return this.tableau.getRowDimension();
    }

    protected final double getEntry(int row, int column) {
        return this.tableau.getEntry(row, column);
    }

    protected final void setEntry(int row, int column, double value) {
        this.tableau.setEntry(row, column, value);
    }

    protected final int getSlackVariableOffset() {
        return getNumObjectiveFunctions() + this.numDecisionVariables;
    }

    protected final int getArtificialVariableOffset() {
        return getNumObjectiveFunctions() + this.numDecisionVariables + this.numSlackVariables;
    }

    protected final int getRhsOffset() {
        return getWidth() - 1;
    }

    protected final int getNumDecisionVariables() {
        return this.numDecisionVariables;
    }

    protected final int getOriginalNumDecisionVariables() {
        return this.f.getCoefficients().getDimension();
    }

    protected final int getNumSlackVariables() {
        return this.numSlackVariables;
    }

    protected final int getNumArtificialVariables() {
        return this.numArtificialVariables;
    }

    protected final double[][] getData() {
        return this.tableau.getData();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof org.apache.commons.math.optimization.linear.SimplexTableau)) {
            return false;
        }
        org.apache.commons.math.optimization.linear.SimplexTableau rhs = (org.apache.commons.math.optimization.linear.SimplexTableau) other;
        return this.restrictToNonNegative == rhs.restrictToNonNegative && this.numDecisionVariables == rhs.numDecisionVariables && this.numSlackVariables == rhs.numSlackVariables && this.numArtificialVariables == rhs.numArtificialVariables && this.epsilon == rhs.epsilon && this.f.equals(rhs.f) && this.constraints.equals(rhs.constraints) && this.tableau.equals(rhs.tableau);
    }

    public int hashCode() {
        return ((((((java.lang.Boolean.valueOf(this.restrictToNonNegative).hashCode() ^ this.numDecisionVariables) ^ this.numSlackVariables) ^ this.numArtificialVariables) ^ java.lang.Double.valueOf(this.epsilon).hashCode()) ^ this.f.hashCode()) ^ this.constraints.hashCode()) ^ this.tableau.hashCode();
    }

    private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException {
        oos.defaultWriteObject();
        org.apache.commons.math.linear.MatrixUtils.serializeRealMatrix(this.tableau, oos);
    }

    private void readObject(java.io.ObjectInputStream ois) throws java.lang.ClassNotFoundException, java.io.IOException {
        ois.defaultReadObject();
        org.apache.commons.math.linear.MatrixUtils.deserializeRealMatrix(this, "tableau", ois);
    }
}
