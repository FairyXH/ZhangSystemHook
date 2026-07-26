package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public class RealMatrixImpl extends org.apache.commons.math.linear.AbstractRealMatrix implements java.io.Serializable {
    private static final long serialVersionUID = -1067294169172445528L;
    protected double[][] data;

    public RealMatrixImpl() {
    }

    public RealMatrixImpl(int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        super(rowDimension, columnDimension);
        this.data = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, rowDimension, columnDimension);
    }

    public RealMatrixImpl(double[][] d) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
        copyIn(d);
    }

    public RealMatrixImpl(double[][] d, boolean copyArray) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
        if (copyArray) {
            copyIn(d);
            return;
        }
        if (d == null) {
            throw new java.lang.NullPointerException();
        }
        int nRows = d.length;
        if (nRows == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_ROW, new java.lang.Object[0]);
        }
        int nCols = d[0].length;
        if (nCols == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
        }
        for (int r = 1; r < nRows; r++) {
            if (d[r].length != nCols) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(nCols), java.lang.Integer.valueOf(d[r].length));
            }
        }
        this.data = d;
    }

    public RealMatrixImpl(double[] v) {
        int nRows = v.length;
        this.data = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, nRows, 1);
        for (int row = 0; row < nRows; row++) {
            this.data[row][0] = v[row];
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix createMatrix(int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        return new org.apache.commons.math.linear.RealMatrixImpl(rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix copy() {
        return new org.apache.commons.math.linear.RealMatrixImpl(copyOut(), false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix add(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return add((org.apache.commons.math.linear.RealMatrixImpl) m);
        } catch (java.lang.ClassCastException e) {
            return super.add(m);
        }
    }

    public org.apache.commons.math.linear.RealMatrixImpl add(org.apache.commons.math.linear.RealMatrixImpl m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkAdditionCompatible(this, m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        double[][] outData = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            double[] dataRow = this.data[row];
            double[] mRow = m.data[row];
            double[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = dataRow[col] + mRow[col];
            }
        }
        return new org.apache.commons.math.linear.RealMatrixImpl(outData, false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix subtract(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return subtract((org.apache.commons.math.linear.RealMatrixImpl) m);
        } catch (java.lang.ClassCastException e) {
            return super.subtract(m);
        }
    }

    public org.apache.commons.math.linear.RealMatrixImpl subtract(org.apache.commons.math.linear.RealMatrixImpl m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkSubtractionCompatible(this, m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        double[][] outData = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            double[] dataRow = this.data[row];
            double[] mRow = m.data[row];
            double[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = dataRow[col] - mRow[col];
            }
        }
        return new org.apache.commons.math.linear.RealMatrixImpl(outData, false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix multiply(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return multiply((org.apache.commons.math.linear.RealMatrixImpl) m);
        } catch (java.lang.ClassCastException e) {
            return super.multiply(m);
        }
    }

    public org.apache.commons.math.linear.RealMatrixImpl multiply(org.apache.commons.math.linear.RealMatrixImpl m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkMultiplicationCompatible(this, m);
        int nRows = getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = getColumnDimension();
        double[][] outData = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            double[] dataRow = this.data[row];
            double[] outDataRow = outData[row];
            for (int col = 0; col < nCols; col++) {
                double sum = 0.0d;
                for (int i = 0; i < nSum; i++) {
                    sum += dataRow[i] * m.data[i][col];
                }
                outDataRow[col] = sum;
            }
        }
        return new org.apache.commons.math.linear.RealMatrixImpl(outData, false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double[][] getData() {
        return copyOut();
    }

    public double[][] getDataRef() {
        return this.data;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setSubMatrix(double[][] subMatrix, int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        if (this.data == null) {
            if (row > 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.FIRST_ROWS_NOT_INITIALIZED_YET, java.lang.Integer.valueOf(row));
            }
            if (column > 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.FIRST_COLUMNS_NOT_INITIALIZED_YET, java.lang.Integer.valueOf(column));
            }
            int nRows = subMatrix.length;
            if (nRows == 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_ROW, new java.lang.Object[0]);
            }
            int nCols = subMatrix[0].length;
            if (nCols == 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
            }
            this.data = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, subMatrix.length, nCols);
            for (int i = 0; i < this.data.length; i++) {
                if (subMatrix[i].length == nCols) {
                    java.lang.System.arraycopy(subMatrix[i], 0, this.data[i + row], column, nCols);
                } else {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(nCols), java.lang.Integer.valueOf(subMatrix[i].length));
                }
            }
            return;
        }
        super.setSubMatrix(subMatrix, row, column);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double getEntry(int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            return this.data[row][column];
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setEntry(int row, int column, double value) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            this.data[row][column] = value;
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void addToEntry(int row, int column, double increment) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            double[] dArr = this.data[row];
            dArr[column] = dArr[column] + increment;
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void multiplyEntry(int row, int column, double factor) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            double[] dArr = this.data[row];
            dArr[column] = dArr[column] * factor;
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getRowDimension() {
        if (this.data == null) {
            return 0;
        }
        return this.data.length;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getColumnDimension() {
        if (this.data == null || this.data[0] == null) {
            return 0;
        }
        return this.data[0].length;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double[] operate(double[] v) throws java.lang.IllegalArgumentException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v.length != nCols) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.length), java.lang.Integer.valueOf(nCols));
        }
        double[] out = new double[nRows];
        for (int row = 0; row < nRows; row++) {
            double[] dataRow = this.data[row];
            double sum = 0.0d;
            for (int i = 0; i < nCols; i++) {
                sum += dataRow[i] * v[i];
            }
            out[row] = sum;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
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
                sum += this.data[i][col] * v[i];
            }
            out[col] = sum;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int i = 0; i < rows; i++) {
            double[] rowI = this.data[i];
            for (int j = 0; j < columns; j++) {
                rowI[j] = visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int i = 0; i < rows; i++) {
            double[] rowI = this.data[i];
            for (int j = 0; j < columns; j++) {
                visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int i = startRow; i <= endRow; i++) {
            double[] rowI = this.data[i];
            for (int j = startColumn; j <= endColumn; j++) {
                rowI[j] = visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int i = startRow; i <= endRow; i++) {
            double[] rowI = this.data[i];
            for (int j = startColumn; j <= endColumn; j++) {
                visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                double[] rowI = this.data[i];
                rowI[j] = visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                visitor.visit(i, j, this.data[i][j]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int j = startColumn; j <= endColumn; j++) {
            for (int i = startRow; i <= endRow; i++) {
                double[] rowI = this.data[i];
                rowI[j] = visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int j = startColumn; j <= endColumn; j++) {
            for (int i = startRow; i <= endRow; i++) {
                visitor.visit(i, j, this.data[i][j]);
            }
        }
        return visitor.end();
    }

    private double[][] copyOut() {
        int nRows = getRowDimension();
        double[][] out = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, nRows, getColumnDimension());
        for (int i = 0; i < nRows; i++) {
            java.lang.System.arraycopy(this.data[i], 0, out[i], 0, this.data[i].length);
        }
        return out;
    }

    private void copyIn(double[][] in) {
        setSubMatrix(in, 0, 0);
    }
}
