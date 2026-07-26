package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractRealMatrix implements org.apache.commons.math.linear.RealMatrix {

    @java.lang.Deprecated
    private org.apache.commons.math.linear.DecompositionSolver lu;

    @Override // org.apache.commons.math.linear.RealMatrix
    public abstract void addToEntry(int i, int i2, double d) throws org.apache.commons.math.linear.MatrixIndexException;

    @Override // org.apache.commons.math.linear.RealMatrix
    public abstract org.apache.commons.math.linear.RealMatrix copy();

    @Override // org.apache.commons.math.linear.RealMatrix
    public abstract org.apache.commons.math.linear.RealMatrix createMatrix(int i, int i2) throws java.lang.IllegalArgumentException;

    @Override // org.apache.commons.math.linear.AnyMatrix
    public abstract int getColumnDimension();

    @Override // org.apache.commons.math.linear.RealMatrix
    public abstract double getEntry(int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException;

    @Override // org.apache.commons.math.linear.AnyMatrix
    public abstract int getRowDimension();

    @Override // org.apache.commons.math.linear.RealMatrix
    public abstract void multiplyEntry(int i, int i2, double d) throws org.apache.commons.math.linear.MatrixIndexException;

    @Override // org.apache.commons.math.linear.RealMatrix
    public abstract void setEntry(int i, int i2, double d) throws org.apache.commons.math.linear.MatrixIndexException;

    protected AbstractRealMatrix() {
        this.lu = null;
    }

    protected AbstractRealMatrix(int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        if (rowDimension < 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(rowDimension), 1);
        }
        if (columnDimension <= 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(columnDimension), 1);
        }
        this.lu = null;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix add(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkAdditionCompatible(this, m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.linear.RealMatrix out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, getEntry(row, col) + m.getEntry(row, col));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix subtract(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkSubtractionCompatible(this, m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.linear.RealMatrix out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, getEntry(row, col) - m.getEntry(row, col));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix scalarAdd(double d) {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.linear.RealMatrix out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, getEntry(row, col) + d);
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix scalarMultiply(double d) {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.linear.RealMatrix out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, getEntry(row, col) * d);
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix multiply(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkMultiplicationCompatible(this, m);
        int nRows = getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = getColumnDimension();
        org.apache.commons.math.linear.RealMatrix out = createMatrix(nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                double sum = 0.0d;
                for (int i = 0; i < nSum; i++) {
                    sum += getEntry(row, i) * m.getEntry(i, col);
                }
                out.setEntry(row, col, sum);
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix preMultiply(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        return m.multiply(this);
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double[][] getData() {
        double[][] data = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, getRowDimension(), getColumnDimension());
        for (int i = 0; i < data.length; i++) {
            double[] dataI = data[i];
            for (int j = 0; j < dataI.length; j++) {
                dataI[j] = getEntry(i, j);
            }
        }
        return data;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double getNorm() {
        return walkInColumnOrder(new org.apache.commons.math.linear.RealMatrixPreservingVisitor() { // from class: org.apache.commons.math.linear.AbstractRealMatrix.1
            private double columnSum;
            private double endRow;
            private double maxColSum;

            @Override // org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
                this.endRow = endRow;
                this.columnSum = 0.0d;
                this.maxColSum = 0.0d;
            }

            @Override // org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) {
                this.columnSum += org.apache.commons.math.util.FastMath.abs(value);
                if (row == this.endRow) {
                    this.maxColSum = org.apache.commons.math.util.FastMath.max(this.maxColSum, this.columnSum);
                    this.columnSum = 0.0d;
                }
            }

            @Override // org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public double end() {
                return this.maxColSum;
            }
        });
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double getFrobeniusNorm() {
        return walkInOptimizedOrder(new org.apache.commons.math.linear.RealMatrixPreservingVisitor() { // from class: org.apache.commons.math.linear.AbstractRealMatrix.2
            private double sum;

            @Override // org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
                this.sum = 0.0d;
            }

            @Override // org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) {
                this.sum += value * value;
            }

            @Override // org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public double end() {
                return org.apache.commons.math.util.FastMath.sqrt(this.sum);
            }
        });
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        org.apache.commons.math.linear.RealMatrix subMatrix = createMatrix((endRow - startRow) + 1, (endColumn - startColumn) + 1);
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                subMatrix.setEntry(i - startRow, j - startColumn, getEntry(i, j));
            }
        }
        return subMatrix;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix getSubMatrix(final int[] selectedRows, final int[] selectedColumns) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, selectedRows, selectedColumns);
        org.apache.commons.math.linear.RealMatrix subMatrix = createMatrix(selectedRows.length, selectedColumns.length);
        subMatrix.walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultRealMatrixChangingVisitor() { // from class: org.apache.commons.math.linear.AbstractRealMatrix.3
            @Override // org.apache.commons.math.linear.DefaultRealMatrixChangingVisitor, org.apache.commons.math.linear.RealMatrixChangingVisitor
            public double visit(int row, int column, double value) {
                return org.apache.commons.math.linear.AbstractRealMatrix.this.getEntry(selectedRows[row], selectedColumns[column]);
            }
        });
        return subMatrix;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public void copySubMatrix(int startRow, int endRow, int startColumn, int endColumn, final double[][] destination) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        int rowsCount = (endRow + 1) - startRow;
        int columnsCount = (endColumn + 1) - startColumn;
        if (destination.length < rowsCount || destination[0].length < columnsCount) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(destination.length), java.lang.Integer.valueOf(destination[0].length), java.lang.Integer.valueOf(rowsCount), java.lang.Integer.valueOf(columnsCount));
        }
        walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultRealMatrixPreservingVisitor() { // from class: org.apache.commons.math.linear.AbstractRealMatrix.4
            private int startColumn;
            private int startRow;

            @Override // org.apache.commons.math.linear.DefaultRealMatrixPreservingVisitor, org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public void start(int rows, int columns, int startRow2, int endRow2, int startColumn2, int endColumn2) {
                this.startRow = startRow2;
                this.startColumn = startColumn2;
            }

            @Override // org.apache.commons.math.linear.DefaultRealMatrixPreservingVisitor, org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) {
                destination[row - this.startRow][column - this.startColumn] = value;
            }
        }, startRow, endRow, startColumn, endColumn);
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public void copySubMatrix(int[] selectedRows, int[] selectedColumns, double[][] destination) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, selectedRows, selectedColumns);
        if (destination.length < selectedRows.length || destination[0].length < selectedColumns.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(destination.length), java.lang.Integer.valueOf(destination[0].length), java.lang.Integer.valueOf(selectedRows.length), java.lang.Integer.valueOf(selectedColumns.length));
        }
        for (int i = 0; i < selectedRows.length; i++) {
            double[] destinationI = destination[i];
            for (int j = 0; j < selectedColumns.length; j++) {
                destinationI[j] = getEntry(selectedRows[i], selectedColumns[j]);
            }
        }
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public void setSubMatrix(double[][] subMatrix, int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        int nRows = subMatrix.length;
        if (nRows == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_ROW, new java.lang.Object[0]);
        }
        int nCols = subMatrix[0].length;
        if (nCols == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
        }
        for (int r = 1; r < nRows; r++) {
            if (subMatrix[r].length != nCols) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(nCols), java.lang.Integer.valueOf(subMatrix[r].length));
            }
        }
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, (nRows + row) - 1);
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, (nCols + column) - 1);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                setEntry(row + i, column + j, subMatrix[i][j]);
            }
        }
        this.lu = null;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix getRowMatrix(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        org.apache.commons.math.linear.RealMatrix out = createMatrix(1, nCols);
        for (int i = 0; i < nCols; i++) {
            out.setEntry(0, i, getEntry(row, i));
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public void setRowMatrix(int row, org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() != 1 || matrix.getColumnDimension() != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(matrix.getRowDimension()), java.lang.Integer.valueOf(matrix.getColumnDimension()), 1, java.lang.Integer.valueOf(nCols));
        }
        for (int i = 0; i < nCols; i++) {
            setEntry(row, i, matrix.getEntry(0, i));
        }
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix getColumnMatrix(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        org.apache.commons.math.linear.RealMatrix out = createMatrix(nRows, 1);
        for (int i = 0; i < nRows; i++) {
            out.setEntry(i, 0, getEntry(i, column));
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public void setColumnMatrix(int column, org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() != nRows || matrix.getColumnDimension() != 1) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(matrix.getRowDimension()), java.lang.Integer.valueOf(matrix.getColumnDimension()), java.lang.Integer.valueOf(nRows), 1);
        }
        for (int i = 0; i < nRows; i++) {
            setEntry(i, column, matrix.getEntry(i, 0));
        }
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealVector getRowVector(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        return new org.apache.commons.math.linear.ArrayRealVector(getRow(row), false);
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public void setRowVector(int row, org.apache.commons.math.linear.RealVector vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (vector.getDimension() != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, 1, java.lang.Integer.valueOf(vector.getDimension()), 1, java.lang.Integer.valueOf(nCols));
        }
        for (int i = 0; i < nCols; i++) {
            setEntry(row, i, vector.getEntry(i));
        }
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealVector getColumnVector(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        return new org.apache.commons.math.linear.ArrayRealVector(getColumn(column), false);
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public void setColumnVector(int column, org.apache.commons.math.linear.RealVector vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (vector.getDimension() != nRows) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(vector.getDimension()), 1, java.lang.Integer.valueOf(nRows), 1);
        }
        for (int i = 0; i < nRows; i++) {
            setEntry(i, column, vector.getEntry(i));
        }
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double[] getRow(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        double[] out = new double[nCols];
        for (int i = 0; i < nCols; i++) {
            out[i] = getEntry(row, i);
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public void setRow(int row, double[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, 1, java.lang.Integer.valueOf(array.length), 1, java.lang.Integer.valueOf(nCols));
        }
        for (int i = 0; i < nCols; i++) {
            setEntry(row, i, array[i]);
        }
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double[] getColumn(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        double[] out = new double[nRows];
        for (int i = 0; i < nRows; i++) {
            out[i] = getEntry(i, column);
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public void setColumn(int column, double[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(array.length), 1, java.lang.Integer.valueOf(nRows), 1);
        }
        for (int i = 0; i < nRows; i++) {
            setEntry(i, column, array[i]);
        }
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix transpose() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        final org.apache.commons.math.linear.RealMatrix out = createMatrix(nCols, nRows);
        walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultRealMatrixPreservingVisitor() { // from class: org.apache.commons.math.linear.AbstractRealMatrix.5
            @Override // org.apache.commons.math.linear.DefaultRealMatrixPreservingVisitor, org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) {
                out.setEntry(column, row, value);
            }
        });
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    @java.lang.Deprecated
    public org.apache.commons.math.linear.RealMatrix inverse() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (this.lu == null) {
            this.lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, Double.MIN_NORMAL).getSolver();
        }
        return this.lu.getInverse();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    @java.lang.Deprecated
    public double getDeterminant() throws org.apache.commons.math.linear.InvalidMatrixException {
        return new org.apache.commons.math.linear.LUDecompositionImpl(this, Double.MIN_NORMAL).getDeterminant();
    }

    @Override // org.apache.commons.math.linear.AnyMatrix
    public boolean isSquare() {
        return getColumnDimension() == getRowDimension();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    @java.lang.Deprecated
    public boolean isSingular() {
        if (this.lu == null) {
            this.lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, Double.MIN_NORMAL).getSolver();
        }
        return !this.lu.isNonSingular();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double getTrace() throws org.apache.commons.math.linear.NonSquareMatrixException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (nRows != nCols) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(nRows, nCols);
        }
        double trace = 0.0d;
        for (int i = 0; i < nRows; i++) {
            trace += getEntry(i, i);
        }
        return trace;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double[] operate(double[] v) throws java.lang.IllegalArgumentException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v.length != nCols) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.length), java.lang.Integer.valueOf(nCols));
        }
        double[] out = new double[nRows];
        for (int row = 0; row < nRows; row++) {
            double sum = 0.0d;
            for (int i = 0; i < nCols; i++) {
                sum += getEntry(row, i) * v[i];
            }
            out[row] = sum;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealVector operate(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        try {
            return new org.apache.commons.math.linear.ArrayRealVector(operate(((org.apache.commons.math.linear.ArrayRealVector) v).getDataRef()), false);
        } catch (java.lang.ClassCastException e) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v.getDimension() != nCols) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.getDimension()), java.lang.Integer.valueOf(nCols));
            }
            double[] out = new double[nRows];
            for (int row = 0; row < nRows; row++) {
                double sum = 0.0d;
                for (int i = 0; i < nCols; i++) {
                    sum += getEntry(row, i) * v.getEntry(i);
                }
                out[row] = sum;
            }
            return new org.apache.commons.math.linear.ArrayRealVector(out, false);
        }
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double[] preMultiply(double[] v) throws java.lang.IllegalArgumentException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v.length != nRows) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.length), java.lang.Integer.valueOf(nRows));
        }
        double[] out = new double[nCols];
        for (int col = 0; col < nCols; col++) {
            double sum = 0.0d;
            for (int i = 0; i < nRows; i++) {
                sum += getEntry(i, col) * v[i];
            }
            out[col] = sum;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealVector preMultiply(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        try {
            return new org.apache.commons.math.linear.ArrayRealVector(preMultiply(((org.apache.commons.math.linear.ArrayRealVector) v).getDataRef()), false);
        } catch (java.lang.ClassCastException e) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v.getDimension() != nRows) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.getDimension()), java.lang.Integer.valueOf(nRows));
            }
            double[] out = new double[nCols];
            for (int col = 0; col < nCols; col++) {
                double sum = 0.0d;
                for (int i = 0; i < nRows; i++) {
                    sum += getEntry(i, col) * v.getEntry(i);
                }
                out[col] = sum;
            }
            return new org.apache.commons.math.linear.ArrayRealVector(out);
        }
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                double oldValue = getEntry(row, column);
                double newValue = visitor.visit(row, column, oldValue);
                setEntry(row, column, newValue);
            }
        }
        this.lu = null;
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                double oldValue = getEntry(row, column);
                double newValue = visitor.visit(row, column, oldValue);
                setEntry(row, column, newValue);
            }
        }
        this.lu = null;
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                double oldValue = getEntry(row, column);
                double newValue = visitor.visit(row, column, oldValue);
                setEntry(row, column, newValue);
            }
        }
        this.lu = null;
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int column = startColumn; column <= endColumn; column++) {
            for (int row = startRow; row <= endRow; row++) {
                double oldValue = getEntry(row, column);
                double newValue = visitor.visit(row, column, oldValue);
                setEntry(row, column, newValue);
            }
        }
        this.lu = null;
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int column = startColumn; column <= endColumn; column++) {
            for (int row = startRow; row <= endRow; row++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        return walkInRowOrder(visitor);
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        return walkInRowOrder(visitor);
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    public double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    @java.lang.Deprecated
    public double[] solve(double[] b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
        if (this.lu == null) {
            this.lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, Double.MIN_NORMAL).getSolver();
        }
        return this.lu.solve(b);
    }

    @Override // org.apache.commons.math.linear.RealMatrix
    @java.lang.Deprecated
    public org.apache.commons.math.linear.RealMatrix solve(org.apache.commons.math.linear.RealMatrix b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
        if (this.lu == null) {
            this.lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, Double.MIN_NORMAL).getSolver();
        }
        return this.lu.solve(b);
    }

    @java.lang.Deprecated
    public void luDecompose() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (this.lu == null) {
            this.lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, Double.MIN_NORMAL).getSolver();
        }
    }

    public java.lang.String toString() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        java.lang.StringBuilder res = new java.lang.StringBuilder();
        java.lang.String fullClassName = getClass().getName();
        java.lang.String shortClassName = fullClassName.substring(fullClassName.lastIndexOf(46) + 1);
        res.append(shortClassName).append("{");
        for (int i = 0; i < nRows; i++) {
            if (i > 0) {
                res.append(",");
            }
            res.append("{");
            for (int j = 0; j < nCols; j++) {
                if (j > 0) {
                    res.append(",");
                }
                res.append(getEntry(i, j));
            }
            res.append("}");
        }
        res.append("}");
        return res.toString();
    }

    public boolean equals(java.lang.Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof org.apache.commons.math.linear.RealMatrix)) {
            return false;
        }
        org.apache.commons.math.linear.RealMatrix m = (org.apache.commons.math.linear.RealMatrix) object;
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (m.getColumnDimension() != nCols || m.getRowDimension() != nRows) {
            return false;
        }
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                if (getEntry(row, col) != m.getEntry(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        int ret = (7 * 31) + nRows;
        int ret2 = (ret * 31) + nCols;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                ret2 = (ret2 * 31) + ((((row + 1) * 11) + ((col + 1) * 17)) * org.apache.commons.math.util.MathUtils.hash(getEntry(row, col)));
            }
        }
        return ret2;
    }
}
