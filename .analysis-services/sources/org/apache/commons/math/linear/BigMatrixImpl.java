package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public class BigMatrixImpl implements org.apache.commons.math.linear.BigMatrix, java.io.Serializable {
    private static final long serialVersionUID = -1011428905656140431L;
    protected java.math.BigDecimal[][] data;
    protected java.math.BigDecimal[][] lu;
    protected int parity;
    protected int[] permutation;
    private int roundingMode;
    private int scale;
    static final java.math.BigDecimal ZERO = new java.math.BigDecimal(0);
    static final java.math.BigDecimal ONE = new java.math.BigDecimal(1);
    private static final java.math.BigDecimal TOO_SMALL = new java.math.BigDecimal(1.0E-11d);

    public BigMatrixImpl() {
        this.data = null;
        this.lu = null;
        this.permutation = null;
        this.parity = 1;
        this.roundingMode = 4;
        this.scale = 64;
    }

    public BigMatrixImpl(int rowDimension, int columnDimension) {
        this.data = null;
        this.lu = null;
        this.permutation = null;
        this.parity = 1;
        this.roundingMode = 4;
        this.scale = 64;
        if (rowDimension < 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(rowDimension), 1);
        }
        if (columnDimension < 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(columnDimension), 1);
        }
        this.data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, rowDimension, columnDimension);
        this.lu = null;
    }

    public BigMatrixImpl(java.math.BigDecimal[][] d) {
        this.data = null;
        this.lu = null;
        this.permutation = null;
        this.parity = 1;
        this.roundingMode = 4;
        this.scale = 64;
        copyIn(d);
        this.lu = null;
    }

    public BigMatrixImpl(java.math.BigDecimal[][] d, boolean copyArray) {
        this.data = null;
        this.lu = null;
        this.permutation = null;
        this.parity = 1;
        this.roundingMode = 4;
        this.scale = 64;
        if (copyArray) {
            copyIn(d);
        } else {
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
        this.lu = null;
    }

    public BigMatrixImpl(double[][] d) {
        this.data = null;
        this.lu = null;
        this.permutation = null;
        this.parity = 1;
        this.roundingMode = 4;
        this.scale = 64;
        int nRows = d.length;
        if (nRows == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_ROW, new java.lang.Object[0]);
        }
        int nCols = d[0].length;
        if (nCols == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
        }
        for (int row = 1; row < nRows; row++) {
            if (d[row].length != nCols) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(nCols), java.lang.Integer.valueOf(d[row].length));
            }
        }
        copyIn(d);
        this.lu = null;
    }

    public BigMatrixImpl(java.lang.String[][] d) {
        this.data = null;
        this.lu = null;
        this.permutation = null;
        this.parity = 1;
        this.roundingMode = 4;
        this.scale = 64;
        int nRows = d.length;
        if (nRows == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_ROW, new java.lang.Object[0]);
        }
        int nCols = d[0].length;
        if (nCols == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
        }
        for (int row = 1; row < nRows; row++) {
            if (d[row].length != nCols) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(nCols), java.lang.Integer.valueOf(d[row].length));
            }
        }
        copyIn(d);
        this.lu = null;
    }

    public BigMatrixImpl(java.math.BigDecimal[] v) {
        this.data = null;
        this.lu = null;
        this.permutation = null;
        this.parity = 1;
        this.roundingMode = 4;
        this.scale = 64;
        int nRows = v.length;
        this.data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, 1);
        for (int row = 0; row < nRows; row++) {
            this.data[row][0] = v[row];
        }
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix copy() {
        return new org.apache.commons.math.linear.BigMatrixImpl(copyOut(), false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix add(org.apache.commons.math.linear.BigMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return add((org.apache.commons.math.linear.BigMatrixImpl) m);
        } catch (java.lang.ClassCastException e) {
            org.apache.commons.math.linear.MatrixUtils.checkAdditionCompatible(this, m);
            int rowCount = getRowDimension();
            int columnCount = getColumnDimension();
            java.math.BigDecimal[][] outData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, rowCount, columnCount);
            for (int row = 0; row < rowCount; row++) {
                java.math.BigDecimal[] dataRow = this.data[row];
                java.math.BigDecimal[] outDataRow = outData[row];
                for (int col = 0; col < columnCount; col++) {
                    outDataRow[col] = dataRow[col].add(m.getEntry(row, col));
                }
            }
            return new org.apache.commons.math.linear.BigMatrixImpl(outData, false);
        }
    }

    public org.apache.commons.math.linear.BigMatrixImpl add(org.apache.commons.math.linear.BigMatrixImpl m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkAdditionCompatible(this, m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        java.math.BigDecimal[][] outData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            java.math.BigDecimal[] dataRow = this.data[row];
            java.math.BigDecimal[] mRow = m.data[row];
            java.math.BigDecimal[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = dataRow[col].add(mRow[col]);
            }
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(outData, false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix subtract(org.apache.commons.math.linear.BigMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return subtract((org.apache.commons.math.linear.BigMatrixImpl) m);
        } catch (java.lang.ClassCastException e) {
            org.apache.commons.math.linear.MatrixUtils.checkSubtractionCompatible(this, m);
            int rowCount = getRowDimension();
            int columnCount = getColumnDimension();
            java.math.BigDecimal[][] outData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, rowCount, columnCount);
            for (int row = 0; row < rowCount; row++) {
                java.math.BigDecimal[] dataRow = this.data[row];
                java.math.BigDecimal[] outDataRow = outData[row];
                for (int col = 0; col < columnCount; col++) {
                    outDataRow[col] = dataRow[col].subtract(getEntry(row, col));
                }
            }
            return new org.apache.commons.math.linear.BigMatrixImpl(outData, false);
        }
    }

    public org.apache.commons.math.linear.BigMatrixImpl subtract(org.apache.commons.math.linear.BigMatrixImpl m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkSubtractionCompatible(this, m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        java.math.BigDecimal[][] outData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            java.math.BigDecimal[] dataRow = this.data[row];
            java.math.BigDecimal[] mRow = m.data[row];
            java.math.BigDecimal[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = dataRow[col].subtract(mRow[col]);
            }
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(outData, false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix scalarAdd(java.math.BigDecimal d) {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        java.math.BigDecimal[][] outData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            java.math.BigDecimal[] dataRow = this.data[row];
            java.math.BigDecimal[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = dataRow[col].add(d);
            }
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(outData, false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix scalarMultiply(java.math.BigDecimal d) {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        java.math.BigDecimal[][] outData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            java.math.BigDecimal[] dataRow = this.data[row];
            java.math.BigDecimal[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = dataRow[col].multiply(d);
            }
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(outData, false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix multiply(org.apache.commons.math.linear.BigMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return multiply((org.apache.commons.math.linear.BigMatrixImpl) m);
        } catch (java.lang.ClassCastException e) {
            org.apache.commons.math.linear.MatrixUtils.checkMultiplicationCompatible(this, m);
            int nRows = getRowDimension();
            int nCols = m.getColumnDimension();
            int nSum = getColumnDimension();
            java.math.BigDecimal[][] outData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, nCols);
            for (int row = 0; row < nRows; row++) {
                java.math.BigDecimal[] dataRow = this.data[row];
                java.math.BigDecimal[] outDataRow = outData[row];
                for (int col = 0; col < nCols; col++) {
                    java.math.BigDecimal sum = ZERO;
                    for (int i = 0; i < nSum; i++) {
                        sum = sum.add(dataRow[i].multiply(m.getEntry(i, col)));
                    }
                    outDataRow[col] = sum;
                }
            }
            return new org.apache.commons.math.linear.BigMatrixImpl(outData, false);
        }
    }

    public org.apache.commons.math.linear.BigMatrixImpl multiply(org.apache.commons.math.linear.BigMatrixImpl m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkMultiplicationCompatible(this, m);
        int nRows = getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = getColumnDimension();
        java.math.BigDecimal[][] outData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            java.math.BigDecimal[] dataRow = this.data[row];
            java.math.BigDecimal[] outDataRow = outData[row];
            for (int col = 0; col < nCols; col++) {
                java.math.BigDecimal sum = ZERO;
                for (int i = 0; i < nSum; i++) {
                    sum = sum.add(dataRow[i].multiply(m.data[i][col]));
                }
                outDataRow[col] = sum;
            }
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(outData, false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix preMultiply(org.apache.commons.math.linear.BigMatrix m) throws java.lang.IllegalArgumentException {
        return m.multiply(this);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal[][] getData() {
        return copyOut();
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public double[][] getDataAsDoubleArray() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        double[][] d = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, nRows, nCols);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = this.data[i][j].doubleValue();
            }
        }
        return d;
    }

    public java.math.BigDecimal[][] getDataRef() {
        return this.data;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public int getRoundingMode() {
        return this.roundingMode;
    }

    public void setRoundingMode(int roundingMode) {
        this.roundingMode = roundingMode;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal getNorm() {
        java.math.BigDecimal maxColSum = ZERO;
        for (int col = 0; col < getColumnDimension(); col++) {
            java.math.BigDecimal sum = ZERO;
            for (int row = 0; row < getRowDimension(); row++) {
                sum = sum.add(this.data[row][col].abs());
            }
            maxColSum = maxColSum.max(sum);
        }
        return maxColSum;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, startRow);
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, endRow);
        if (startRow > endRow) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, java.lang.Integer.valueOf(startRow), java.lang.Integer.valueOf(endRow));
        }
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, startColumn);
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, endColumn);
        if (startColumn > endColumn) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.INITIAL_COLUMN_AFTER_FINAL_COLUMN, java.lang.Integer.valueOf(startColumn), java.lang.Integer.valueOf(endColumn));
        }
        java.math.BigDecimal[][] subMatrixData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, (endRow - startRow) + 1, (endColumn - startColumn) + 1);
        for (int i = startRow; i <= endRow; i++) {
            java.lang.System.arraycopy(this.data[i], startColumn, subMatrixData[i - startRow], 0, (endColumn - startColumn) + 1);
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(subMatrixData, false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix getSubMatrix(int[] selectedRows, int[] selectedColumns) throws org.apache.commons.math.linear.MatrixIndexException {
        if (selectedRows.length * selectedColumns.length == 0) {
            if (selectedRows.length == 0) {
                throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_SELECTED_ROW_INDEX_ARRAY, new java.lang.Object[0]);
            }
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_SELECTED_COLUMN_INDEX_ARRAY, new java.lang.Object[0]);
        }
        java.math.BigDecimal[][] subMatrixData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, selectedRows.length, selectedColumns.length);
        for (int i = 0; i < selectedRows.length; i++) {
            try {
                java.math.BigDecimal[] subI = subMatrixData[i];
                java.math.BigDecimal[] dataSelectedI = this.data[selectedRows[i]];
                for (int j = 0; j < selectedColumns.length; j++) {
                    subI[j] = dataSelectedI[selectedColumns[j]];
                }
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                for (int row : selectedRows) {
                    org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
                }
                for (int column : selectedColumns) {
                    org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
                }
            }
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(subMatrixData, false);
    }

    public void setSubMatrix(java.math.BigDecimal[][] subMatrix, int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
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
        if (this.data == null) {
            if (row > 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.FIRST_ROWS_NOT_INITIALIZED_YET, java.lang.Integer.valueOf(row));
            }
            if (column <= 0) {
                this.data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, nCols);
                java.lang.System.arraycopy(subMatrix, 0, this.data, 0, subMatrix.length);
            } else {
                throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.FIRST_COLUMNS_NOT_INITIALIZED_YET, java.lang.Integer.valueOf(column));
            }
        } else {
            org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
            org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
            org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, (nRows + row) - 1);
            org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, (nCols + column) - 1);
        }
        for (int i = 0; i < nRows; i++) {
            java.lang.System.arraycopy(subMatrix[i], 0, this.data[row + i], column, nCols);
        }
        this.lu = null;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix getRowMatrix(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int ncols = getColumnDimension();
        java.math.BigDecimal[][] out = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, 1, ncols);
        java.lang.System.arraycopy(this.data[row], 0, out[0], 0, ncols);
        return new org.apache.commons.math.linear.BigMatrixImpl(out, false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix getColumnMatrix(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        java.math.BigDecimal[][] out = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, 1);
        for (int row = 0; row < nRows; row++) {
            out[row][0] = this.data[row][column];
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(out, false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal[] getRow(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int ncols = getColumnDimension();
        java.math.BigDecimal[] out = new java.math.BigDecimal[ncols];
        java.lang.System.arraycopy(this.data[row], 0, out, 0, ncols);
        return out;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public double[] getRowAsDoubleArray(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int ncols = getColumnDimension();
        double[] out = new double[ncols];
        for (int i = 0; i < ncols; i++) {
            out[i] = this.data[row][i].doubleValue();
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal[] getColumn(int col) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, col);
        int nRows = getRowDimension();
        java.math.BigDecimal[] out = new java.math.BigDecimal[nRows];
        for (int i = 0; i < nRows; i++) {
            out[i] = this.data[i][col];
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public double[] getColumnAsDoubleArray(int col) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, col);
        int nrows = getRowDimension();
        double[] out = new double[nrows];
        for (int i = 0; i < nrows; i++) {
            out[i] = this.data[i][col].doubleValue();
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal getEntry(int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            return this.data[row][column];
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public double getEntryAsDouble(int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        return getEntry(row, column).doubleValue();
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix transpose() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        java.math.BigDecimal[][] outData = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nCols, nRows);
        for (int row = 0; row < nRows; row++) {
            java.math.BigDecimal[] dataRow = this.data[row];
            for (int col = 0; col < nCols; col++) {
                outData[col][row] = dataRow[col];
            }
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(outData, false);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix inverse() throws org.apache.commons.math.linear.InvalidMatrixException {
        return solve(org.apache.commons.math.linear.MatrixUtils.createBigIdentityMatrix(getRowDimension()));
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal getDeterminant() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (!isSquare()) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(getRowDimension(), getColumnDimension());
        }
        if (isSingular()) {
            return ZERO;
        }
        java.math.BigDecimal det = this.parity == 1 ? ONE : ONE.negate();
        for (int i = 0; i < getRowDimension(); i++) {
            det = det.multiply(this.lu[i][i]);
        }
        return det;
    }

    @Override // org.apache.commons.math.linear.AnyMatrix
    public boolean isSquare() {
        return getColumnDimension() == getRowDimension();
    }

    public boolean isSingular() {
        if (this.lu != null) {
            return false;
        }
        try {
            luDecompose();
            return false;
        } catch (org.apache.commons.math.linear.InvalidMatrixException e) {
            return true;
        }
    }

    @Override // org.apache.commons.math.linear.AnyMatrix
    public int getRowDimension() {
        return this.data.length;
    }

    @Override // org.apache.commons.math.linear.AnyMatrix
    public int getColumnDimension() {
        return this.data[0].length;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal getTrace() throws java.lang.IllegalArgumentException {
        if (!isSquare()) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(getRowDimension(), getColumnDimension());
        }
        java.math.BigDecimal trace = this.data[0][0];
        for (int i = 1; i < getRowDimension(); i++) {
            trace = trace.add(this.data[i][i]);
        }
        return trace;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal[] operate(java.math.BigDecimal[] v) throws java.lang.IllegalArgumentException {
        if (v.length != getColumnDimension()) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.length), java.lang.Integer.valueOf(getColumnDimension()));
        }
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        java.math.BigDecimal[] out = new java.math.BigDecimal[nRows];
        for (int row = 0; row < nRows; row++) {
            java.math.BigDecimal sum = ZERO;
            for (int i = 0; i < nCols; i++) {
                sum = sum.add(this.data[row][i].multiply(v[i]));
            }
            out[row] = sum;
        }
        return out;
    }

    public java.math.BigDecimal[] operate(double[] v) throws java.lang.IllegalArgumentException {
        java.math.BigDecimal[] bd = new java.math.BigDecimal[v.length];
        for (int i = 0; i < bd.length; i++) {
            bd[i] = new java.math.BigDecimal(v[i]);
        }
        return operate(bd);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal[] preMultiply(java.math.BigDecimal[] v) throws java.lang.IllegalArgumentException {
        int nRows = getRowDimension();
        if (v.length != nRows) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.length), java.lang.Integer.valueOf(nRows));
        }
        int nCols = getColumnDimension();
        java.math.BigDecimal[] out = new java.math.BigDecimal[nCols];
        for (int col = 0; col < nCols; col++) {
            java.math.BigDecimal sum = ZERO;
            for (int i = 0; i < nRows; i++) {
                sum = sum.add(this.data[i][col].multiply(v[i]));
            }
            out[col] = sum;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public java.math.BigDecimal[] solve(java.math.BigDecimal[] b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
        int nRows = getRowDimension();
        if (b.length != nRows) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(b.length), java.lang.Integer.valueOf(nRows));
        }
        org.apache.commons.math.linear.BigMatrix bMatrix = new org.apache.commons.math.linear.BigMatrixImpl(b);
        java.math.BigDecimal[][] solution = ((org.apache.commons.math.linear.BigMatrixImpl) solve(bMatrix)).getDataRef();
        java.math.BigDecimal[] out = new java.math.BigDecimal[nRows];
        for (int row = 0; row < nRows; row++) {
            out[row] = solution[row][0];
        }
        return out;
    }

    public java.math.BigDecimal[] solve(double[] b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
        java.math.BigDecimal[] bd = new java.math.BigDecimal[b.length];
        for (int i = 0; i < bd.length; i++) {
            bd[i] = new java.math.BigDecimal(b[i]);
        }
        return solve(bd);
    }

    @Override // org.apache.commons.math.linear.BigMatrix
    public org.apache.commons.math.linear.BigMatrix solve(org.apache.commons.math.linear.BigMatrix b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
        if (b.getRowDimension() != getRowDimension()) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(b.getRowDimension()), java.lang.Integer.valueOf(b.getColumnDimension()), java.lang.Integer.valueOf(getRowDimension()), "n");
        }
        if (!isSquare()) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(getRowDimension(), getColumnDimension());
        }
        if (isSingular()) {
            throw new org.apache.commons.math.linear.SingularMatrixException();
        }
        int nCol = getColumnDimension();
        int nColB = b.getColumnDimension();
        int nRowB = b.getRowDimension();
        java.math.BigDecimal[][] bp = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRowB, nColB);
        for (int row = 0; row < nRowB; row++) {
            java.math.BigDecimal[] bpRow = bp[row];
            for (int col = 0; col < nColB; col++) {
                bpRow[col] = b.getEntry(this.permutation[row], col);
            }
        }
        for (int col2 = 0; col2 < nCol; col2++) {
            for (int i = col2 + 1; i < nCol; i++) {
                java.math.BigDecimal[] bpI = bp[i];
                java.math.BigDecimal[] luI = this.lu[i];
                for (int j = 0; j < nColB; j++) {
                    bpI[j] = bpI[j].subtract(bp[col2][j].multiply(luI[col2]));
                }
            }
        }
        for (int col3 = nCol - 1; col3 >= 0; col3--) {
            java.math.BigDecimal[] bpCol = bp[col3];
            java.math.BigDecimal luDiag = this.lu[col3][col3];
            for (int j2 = 0; j2 < nColB; j2++) {
                bpCol[j2] = bpCol[j2].divide(luDiag, this.scale, this.roundingMode);
            }
            for (int i2 = 0; i2 < col3; i2++) {
                java.math.BigDecimal[] bpI2 = bp[i2];
                java.math.BigDecimal[] luI2 = this.lu[i2];
                for (int j3 = 0; j3 < nColB; j3++) {
                    bpI2[j3] = bpI2[j3].subtract(bp[col3][j3].multiply(luI2[col3]));
                }
            }
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(bp, false);
    }

    public void luDecompose() throws org.apache.commons.math.linear.InvalidMatrixException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (nRows != nCols) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(getRowDimension(), getColumnDimension());
        }
        this.lu = getData();
        this.permutation = new int[nRows];
        for (int row = 0; row < nRows; row++) {
            this.permutation[row] = row;
        }
        this.parity = 1;
        for (int col = 0; col < nCols; col++) {
            java.math.BigDecimal bigDecimal = ZERO;
            for (int row2 = 0; row2 < col; row2++) {
                java.math.BigDecimal[] luRow = this.lu[row2];
                java.math.BigDecimal sum = luRow[col];
                for (int i = 0; i < row2; i++) {
                    sum = sum.subtract(luRow[i].multiply(this.lu[i][col]));
                }
                luRow[col] = sum;
            }
            int max = col;
            java.math.BigDecimal largest = ZERO;
            for (int row3 = col; row3 < nRows; row3++) {
                java.math.BigDecimal[] luRow2 = this.lu[row3];
                java.math.BigDecimal sum2 = luRow2[col];
                for (int i2 = 0; i2 < col; i2++) {
                    sum2 = sum2.subtract(luRow2[i2].multiply(this.lu[i2][col]));
                }
                luRow2[col] = sum2;
                if (sum2.abs().compareTo(largest) == 1) {
                    largest = sum2.abs();
                    max = row3;
                }
            }
            if (this.lu[max][col].abs().compareTo(TOO_SMALL) <= 0) {
                this.lu = null;
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            if (max != col) {
                java.math.BigDecimal bigDecimal2 = ZERO;
                for (int i3 = 0; i3 < nCols; i3++) {
                    java.math.BigDecimal tmp = this.lu[max][i3];
                    this.lu[max][i3] = this.lu[col][i3];
                    this.lu[col][i3] = tmp;
                }
                int temp = this.permutation[max];
                this.permutation[max] = this.permutation[col];
                this.permutation[col] = temp;
                this.parity = -this.parity;
            }
            java.math.BigDecimal luDiag = this.lu[col][col];
            for (int row4 = col + 1; row4 < nRows; row4++) {
                java.math.BigDecimal[] luRow3 = this.lu[row4];
                luRow3[col] = luRow3[col].divide(luDiag, this.scale, this.roundingMode);
            }
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuilder res = new java.lang.StringBuilder();
        res.append("BigMatrixImpl{");
        if (this.data != null) {
            for (int i = 0; i < this.data.length; i++) {
                if (i > 0) {
                    res.append(",");
                }
                res.append("{");
                for (int j = 0; j < this.data[0].length; j++) {
                    if (j > 0) {
                        res.append(",");
                    }
                    res.append(this.data[i][j]);
                }
                res.append("}");
            }
        }
        res.append("}");
        return res.toString();
    }

    public boolean equals(java.lang.Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof org.apache.commons.math.linear.BigMatrixImpl)) {
            return false;
        }
        org.apache.commons.math.linear.BigMatrix m = (org.apache.commons.math.linear.BigMatrix) object;
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (m.getColumnDimension() != nCols || m.getRowDimension() != nRows) {
            return false;
        }
        for (int row = 0; row < nRows; row++) {
            java.math.BigDecimal[] dataRow = this.data[row];
            for (int col = 0; col < nCols; col++) {
                if (!dataRow[col].equals(m.getEntry(row, col))) {
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
            java.math.BigDecimal[] dataRow = this.data[row];
            for (int col = 0; col < nCols; col++) {
                ret2 = (ret2 * 31) + ((((row + 1) * 11) + ((col + 1) * 17)) * dataRow[col].hashCode());
            }
        }
        return ret2;
    }

    protected org.apache.commons.math.linear.BigMatrix getLUMatrix() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (this.lu == null) {
            luDecompose();
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(this.lu);
    }

    protected int[] getPermutation() {
        int[] out = new int[this.permutation.length];
        java.lang.System.arraycopy(this.permutation, 0, out, 0, this.permutation.length);
        return out;
    }

    private java.math.BigDecimal[][] copyOut() {
        int nRows = getRowDimension();
        java.math.BigDecimal[][] out = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, getColumnDimension());
        for (int i = 0; i < nRows; i++) {
            java.lang.System.arraycopy(this.data[i], 0, out[i], 0, this.data[i].length);
        }
        return out;
    }

    private void copyIn(java.math.BigDecimal[][] in) {
        setSubMatrix(in, 0, 0);
    }

    private void copyIn(double[][] in) {
        int nRows = in.length;
        int nCols = in[0].length;
        this.data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, nCols);
        for (int i = 0; i < nRows; i++) {
            java.math.BigDecimal[] dataI = this.data[i];
            double[] inI = in[i];
            for (int j = 0; j < nCols; j++) {
                dataI[j] = new java.math.BigDecimal(inI[j]);
            }
        }
        this.lu = null;
    }

    private void copyIn(java.lang.String[][] in) {
        int nRows = in.length;
        int nCols = in[0].length;
        this.data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, nCols);
        for (int i = 0; i < nRows; i++) {
            java.math.BigDecimal[] dataI = this.data[i];
            java.lang.String[] inI = in[i];
            for (int j = 0; j < nCols; j++) {
                dataI[j] = new java.math.BigDecimal(inI[j]);
            }
        }
        this.lu = null;
    }
}
