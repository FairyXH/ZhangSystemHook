package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractFieldMatrix<T extends org.apache.commons.math.FieldElement<T>> implements org.apache.commons.math.linear.FieldMatrix<T> {
    private final org.apache.commons.math.Field<T> field;

    @Override // org.apache.commons.math.linear.FieldMatrix
    public abstract void addToEntry(int i, int i2, T t) throws org.apache.commons.math.linear.MatrixIndexException;

    @Override // org.apache.commons.math.linear.FieldMatrix
    public abstract org.apache.commons.math.linear.FieldMatrix<T> copy();

    @Override // org.apache.commons.math.linear.FieldMatrix
    public abstract org.apache.commons.math.linear.FieldMatrix<T> createMatrix(int i, int i2) throws java.lang.IllegalArgumentException;

    @Override // org.apache.commons.math.linear.AnyMatrix
    public abstract int getColumnDimension();

    @Override // org.apache.commons.math.linear.FieldMatrix
    public abstract T getEntry(int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException;

    @Override // org.apache.commons.math.linear.AnyMatrix
    public abstract int getRowDimension();

    @Override // org.apache.commons.math.linear.FieldMatrix
    public abstract void multiplyEntry(int i, int i2, T t) throws org.apache.commons.math.linear.MatrixIndexException;

    @Override // org.apache.commons.math.linear.FieldMatrix
    public abstract void setEntry(int i, int i2, T t) throws org.apache.commons.math.linear.MatrixIndexException;

    protected AbstractFieldMatrix() {
        this.field = null;
    }

    protected AbstractFieldMatrix(org.apache.commons.math.Field<T> field) {
        this.field = field;
    }

    protected AbstractFieldMatrix(org.apache.commons.math.Field<T> field, int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        if (rowDimension < 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(rowDimension), 1);
        }
        if (columnDimension < 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(columnDimension), 1);
        }
        this.field = field;
    }

    protected static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.Field<T> extractField(T[][] d) throws java.lang.IllegalArgumentException {
        if (d.length == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_ROW, new java.lang.Object[0]);
        }
        if (d[0].length == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
        }
        return d[0][0].getField();
    }

    protected static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.Field<T> extractField(T[] d) throws java.lang.IllegalArgumentException {
        if (d.length == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_ROW, new java.lang.Object[0]);
        }
        return d[0].getField();
    }

    protected static <T extends org.apache.commons.math.FieldElement<T>> T[][] buildArray(org.apache.commons.math.Field<T> field, int i, int i2) {
        if (i2 < 0) {
            return (T[][]) ((org.apache.commons.math.FieldElement[][]) java.lang.reflect.Array.newInstance(((org.apache.commons.math.FieldElement[]) java.lang.reflect.Array.newInstance(field.getZero().getClass(), 0)).getClass(), i));
        }
        T[][] tArr = (T[][]) ((org.apache.commons.math.FieldElement[][]) java.lang.reflect.Array.newInstance(field.getZero().getClass(), i, i2));
        for (T[] tArr2 : tArr) {
            java.util.Arrays.fill(tArr2, field.getZero());
        }
        return tArr;
    }

    protected static <T extends org.apache.commons.math.FieldElement<T>> T[] buildArray(org.apache.commons.math.Field<T> field, int i) {
        T[] tArr = (T[]) ((org.apache.commons.math.FieldElement[]) java.lang.reflect.Array.newInstance(field.getZero().getClass(), i));
        java.util.Arrays.fill(tArr, field.getZero());
        return tArr;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.Field<T> getField() {
        return this.field;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> add(org.apache.commons.math.linear.FieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        checkAdditionCompatible(m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.linear.FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (org.apache.commons.math.FieldElement) getEntry(row, col).add(m.getEntry(row, col)));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> subtract(org.apache.commons.math.linear.FieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        checkSubtractionCompatible(m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.linear.FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (org.apache.commons.math.FieldElement) getEntry(row, col).subtract(m.getEntry(row, col)));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> scalarAdd(T d) {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.linear.FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (org.apache.commons.math.FieldElement) getEntry(row, col).add(d));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> scalarMultiply(T d) {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.linear.FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (org.apache.commons.math.FieldElement) getEntry(row, col).multiply(d));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> multiply(org.apache.commons.math.linear.FieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        checkMultiplicationCompatible(m);
        int nRows = getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = getColumnDimension();
        org.apache.commons.math.linear.FieldMatrix<T> out = createMatrix(nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                T zero = this.field.getZero();
                for (int i = 0; i < nSum; i++) {
                    zero = (org.apache.commons.math.FieldElement) zero.add((org.apache.commons.math.FieldElement) getEntry(row, i).multiply(m.getEntry(i, col)));
                }
                out.setEntry(row, col, zero);
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> preMultiply(org.apache.commons.math.linear.FieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        return m.multiply(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.FieldMatrix
    public T[][] getData() {
        T[][] tArr = (T[][]) buildArray(this.field, getRowDimension(), getColumnDimension());
        for (int i = 0; i < tArr.length; i++) {
            org.apache.commons.math.FieldElement[] fieldElementArr = tArr[i];
            for (int i2 = 0; i2 < fieldElementArr.length; i2++) {
                fieldElementArr[i2] = getEntry(i, i2);
            }
        }
        return tArr;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        org.apache.commons.math.linear.FieldMatrix<T> subMatrix = createMatrix((endRow - startRow) + 1, (endColumn - startColumn) + 1);
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                subMatrix.setEntry(i - startRow, j - startColumn, getEntry(i, j));
            }
        }
        return subMatrix;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> getSubMatrix(final int[] selectedRows, final int[] selectedColumns) throws org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(selectedRows, selectedColumns);
        org.apache.commons.math.linear.FieldMatrix<T> subMatrix = createMatrix(selectedRows.length, selectedColumns.length);
        subMatrix.walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultFieldMatrixChangingVisitor<T>(this.field.getZero()) { // from class: org.apache.commons.math.linear.AbstractFieldMatrix.1
            @Override // org.apache.commons.math.linear.DefaultFieldMatrixChangingVisitor, org.apache.commons.math.linear.FieldMatrixChangingVisitor
            public T visit(int i, int i2, T t) {
                return (T) org.apache.commons.math.linear.AbstractFieldMatrix.this.getEntry(selectedRows[i], selectedColumns[i2]);
            }
        });
        return subMatrix;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public void copySubMatrix(int startRow, int endRow, int startColumn, int endColumn, final T[][] destination) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        int rowsCount = (endRow + 1) - startRow;
        int columnsCount = (endColumn + 1) - startColumn;
        if (destination.length < rowsCount || destination[0].length < columnsCount) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(destination.length), java.lang.Integer.valueOf(destination[0].length), java.lang.Integer.valueOf(rowsCount), java.lang.Integer.valueOf(columnsCount));
        }
        walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor<T>(this.field.getZero()) { // from class: org.apache.commons.math.linear.AbstractFieldMatrix.2
            private int startColumn;
            private int startRow;

            @Override // org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math.linear.FieldMatrixPreservingVisitor
            public void start(int rows, int columns, int startRow2, int endRow2, int startColumn2, int endColumn2) {
                this.startRow = startRow2;
                this.startColumn = startColumn2;
            }

            @Override // org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math.linear.FieldMatrixPreservingVisitor
            public void visit(int row, int column, T value) {
                destination[row - this.startRow][column - this.startColumn] = value;
            }
        }, startRow, endRow, startColumn, endColumn);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.FieldMatrix
    public void copySubMatrix(int[] selectedRows, int[] selectedColumns, T[][] tArr) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException {
        checkSubMatrixIndex(selectedRows, selectedColumns);
        if (tArr.length < selectedRows.length || tArr[0].length < selectedColumns.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(tArr.length), java.lang.Integer.valueOf(tArr[0].length), java.lang.Integer.valueOf(selectedRows.length), java.lang.Integer.valueOf(selectedColumns.length));
        }
        for (int i = 0; i < selectedRows.length; i++) {
            org.apache.commons.math.FieldElement[] fieldElementArr = tArr[i];
            for (int j = 0; j < selectedColumns.length; j++) {
                fieldElementArr[j] = getEntry(selectedRows[i], selectedColumns[j]);
            }
        }
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public void setSubMatrix(T[][] subMatrix, int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
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
        checkRowIndex(row);
        checkColumnIndex(column);
        checkRowIndex((nRows + row) - 1);
        checkColumnIndex((nCols + column) - 1);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                setEntry(row + i, column + j, subMatrix[i][j]);
            }
        }
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> getRowMatrix(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        org.apache.commons.math.linear.FieldMatrix<T> out = createMatrix(1, nCols);
        for (int i = 0; i < nCols; i++) {
            out.setEntry(0, i, getEntry(row, i));
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public void setRowMatrix(int row, org.apache.commons.math.linear.FieldMatrix<T> matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() != 1 || matrix.getColumnDimension() != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(matrix.getRowDimension()), java.lang.Integer.valueOf(matrix.getColumnDimension()), 1, java.lang.Integer.valueOf(nCols));
        }
        for (int i = 0; i < nCols; i++) {
            setEntry(row, i, matrix.getEntry(0, i));
        }
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> getColumnMatrix(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        org.apache.commons.math.linear.FieldMatrix<T> out = createMatrix(nRows, 1);
        for (int i = 0; i < nRows; i++) {
            out.setEntry(i, 0, getEntry(i, column));
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public void setColumnMatrix(int column, org.apache.commons.math.linear.FieldMatrix<T> matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() != nRows || matrix.getColumnDimension() != 1) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(matrix.getRowDimension()), java.lang.Integer.valueOf(matrix.getColumnDimension()), java.lang.Integer.valueOf(nRows), 1);
        }
        for (int i = 0; i < nRows; i++) {
            setEntry(i, column, matrix.getEntry(i, 0));
        }
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldVector<T> getRowVector(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        return new org.apache.commons.math.linear.ArrayFieldVector(getRow(row), false);
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public void setRowVector(int row, org.apache.commons.math.linear.FieldVector<T> vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (vector.getDimension() != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, 1, java.lang.Integer.valueOf(vector.getDimension()), 1, java.lang.Integer.valueOf(nCols));
        }
        for (int i = 0; i < nCols; i++) {
            setEntry(row, i, vector.getEntry(i));
        }
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldVector<T> getColumnVector(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        return new org.apache.commons.math.linear.ArrayFieldVector(getColumn(column), false);
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public void setColumnVector(int column, org.apache.commons.math.linear.FieldVector<T> vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (vector.getDimension() != nRows) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(vector.getDimension()), 1, java.lang.Integer.valueOf(nRows), 1);
        }
        for (int i = 0; i < nRows; i++) {
            setEntry(i, column, vector.getEntry(i));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.FieldMatrix
    public T[] getRow(int i) throws org.apache.commons.math.linear.MatrixIndexException {
        checkRowIndex(i);
        int columnDimension = getColumnDimension();
        T[] tArr = (T[]) buildArray(this.field, columnDimension);
        for (int i2 = 0; i2 < columnDimension; i2++) {
            tArr[i2] = getEntry(i, i2);
        }
        return tArr;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public void setRow(int row, T[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, 1, java.lang.Integer.valueOf(array.length), 1, java.lang.Integer.valueOf(nCols));
        }
        for (int i = 0; i < nCols; i++) {
            setEntry(row, i, array[i]);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.FieldMatrix
    public T[] getColumn(int i) throws org.apache.commons.math.linear.MatrixIndexException {
        checkColumnIndex(i);
        int rowDimension = getRowDimension();
        T[] tArr = (T[]) buildArray(this.field, rowDimension);
        for (int i2 = 0; i2 < rowDimension; i2++) {
            tArr[i2] = getEntry(i2, i);
        }
        return tArr;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public void setColumn(int column, T[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(array.length), 1, java.lang.Integer.valueOf(nRows), 1);
        }
        for (int i = 0; i < nRows; i++) {
            setEntry(i, column, array[i]);
        }
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> transpose() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        final org.apache.commons.math.linear.FieldMatrix<T> out = createMatrix(nCols, nRows);
        walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor<T>(this.field.getZero()) { // from class: org.apache.commons.math.linear.AbstractFieldMatrix.3
            @Override // org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math.linear.FieldMatrixPreservingVisitor
            public void visit(int row, int column, T value) {
                out.setEntry(column, row, value);
            }
        });
        return out;
    }

    @Override // org.apache.commons.math.linear.AnyMatrix
    public boolean isSquare() {
        return getColumnDimension() == getRowDimension();
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T getTrace() throws org.apache.commons.math.linear.NonSquareMatrixException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (nRows != nCols) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(nRows, nCols);
        }
        T zero = this.field.getZero();
        for (int i = 0; i < nRows; i++) {
            zero = (T) zero.add(getEntry(i, i));
        }
        return zero;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T[] operate(T[] tArr) throws java.lang.IllegalArgumentException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        if (tArr.length != columnDimension) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(tArr.length), java.lang.Integer.valueOf(columnDimension));
        }
        T[] tArr2 = (T[]) buildArray(this.field, rowDimension);
        for (int i = 0; i < rowDimension; i++) {
            T zero = this.field.getZero();
            for (int i2 = 0; i2 < columnDimension; i2++) {
                zero = (T) zero.add((org.apache.commons.math.FieldElement) getEntry(i, i2).multiply(tArr[i2]));
            }
            tArr2[i] = zero;
        }
        return tArr2;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldVector<T> operate(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        try {
            return new org.apache.commons.math.linear.ArrayFieldVector(operate(((org.apache.commons.math.linear.ArrayFieldVector) v).getDataRef()), false);
        } catch (java.lang.ClassCastException e) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v.getDimension() != nCols) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.getDimension()), java.lang.Integer.valueOf(nCols));
            }
            org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.field, nRows);
            for (int row = 0; row < nRows; row++) {
                T zero = this.field.getZero();
                for (int i = 0; i < nCols; i++) {
                    zero = (org.apache.commons.math.FieldElement) zero.add((org.apache.commons.math.FieldElement) getEntry(row, i).multiply(v.getEntry(i)));
                }
                fieldElementArrBuildArray[row] = zero;
            }
            return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray, false);
        }
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T[] preMultiply(T[] tArr) throws java.lang.IllegalArgumentException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        if (tArr.length != rowDimension) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(tArr.length), java.lang.Integer.valueOf(rowDimension));
        }
        T[] tArr2 = (T[]) buildArray(this.field, columnDimension);
        for (int i = 0; i < columnDimension; i++) {
            T zero = this.field.getZero();
            for (int i2 = 0; i2 < rowDimension; i2++) {
                zero = (T) zero.add((org.apache.commons.math.FieldElement) getEntry(i2, i).multiply(tArr[i2]));
            }
            tArr2[i] = zero;
        }
        return tArr2;
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldVector<T> preMultiply(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        try {
            return new org.apache.commons.math.linear.ArrayFieldVector(preMultiply(((org.apache.commons.math.linear.ArrayFieldVector) v).getDataRef()), false);
        } catch (java.lang.ClassCastException e) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v.getDimension() != nRows) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.getDimension()), java.lang.Integer.valueOf(nRows));
            }
            org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.field, nCols);
            for (int col = 0; col < nCols; col++) {
                T zero = this.field.getZero();
                for (int i = 0; i < nRows; i++) {
                    zero = (org.apache.commons.math.FieldElement) zero.add((org.apache.commons.math.FieldElement) getEntry(i, col).multiply(v.getEntry(i)));
                }
                fieldElementArrBuildArray[col] = zero;
            }
            return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
        }
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixChangingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i = 0; i < rowDimension; i++) {
            for (int i2 = 0; i2 < columnDimension; i2++) {
                setEntry(i, i2, fieldMatrixChangingVisitor.visit(i, i2, getEntry(i, i2)));
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixPreservingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i = 0; i < rowDimension; i++) {
            for (int i2 = 0; i2 < columnDimension; i2++) {
                fieldMatrixPreservingVisitor.visit(i, i2, getEntry(i, i2));
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixChangingVisitor.start(getRowDimension(), getColumnDimension(), i, i2, i3, i4);
        for (int i5 = i; i5 <= i2; i5++) {
            for (int i6 = i3; i6 <= i4; i6++) {
                setEntry(i5, i6, fieldMatrixChangingVisitor.visit(i5, i6, getEntry(i5, i6)));
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixPreservingVisitor.start(getRowDimension(), getColumnDimension(), i, i2, i3, i4);
        for (int i5 = i; i5 <= i2; i5++) {
            for (int i6 = i3; i6 <= i4; i6++) {
                fieldMatrixPreservingVisitor.visit(i5, i6, getEntry(i5, i6));
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixChangingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i = 0; i < columnDimension; i++) {
            for (int i2 = 0; i2 < rowDimension; i2++) {
                setEntry(i2, i, fieldMatrixChangingVisitor.visit(i2, i, getEntry(i2, i)));
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixPreservingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i = 0; i < columnDimension; i++) {
            for (int i2 = 0; i2 < rowDimension; i2++) {
                fieldMatrixPreservingVisitor.visit(i2, i, getEntry(i2, i));
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixChangingVisitor.start(getRowDimension(), getColumnDimension(), i, i2, i3, i4);
        for (int i5 = i3; i5 <= i4; i5++) {
            for (int i6 = i; i6 <= i2; i6++) {
                setEntry(i6, i5, fieldMatrixChangingVisitor.visit(i6, i5, getEntry(i6, i5)));
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixPreservingVisitor.start(getRowDimension(), getColumnDimension(), i, i2, i3, i4);
        for (int i5 = i3; i5 <= i4; i5++) {
            for (int i6 = i; i6 <= i2; i6++) {
                fieldMatrixPreservingVisitor.visit(i6, i5, getEntry(i6, i5));
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        return (T) walkInRowOrder(fieldMatrixChangingVisitor);
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        return (T) walkInRowOrder(fieldMatrixPreservingVisitor);
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        return (T) walkInRowOrder(fieldMatrixChangingVisitor, i, i2, i3, i4);
    }

    @Override // org.apache.commons.math.linear.FieldMatrix
    public T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        return (T) walkInRowOrder(fieldMatrixPreservingVisitor, i, i2, i3, i4);
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
        if (!(object instanceof org.apache.commons.math.linear.FieldMatrix)) {
            return false;
        }
        org.apache.commons.math.linear.FieldMatrix<?> m = (org.apache.commons.math.linear.FieldMatrix) object;
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (m.getColumnDimension() != nCols || m.getRowDimension() != nRows) {
            return false;
        }
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                if (!getEntry(row, col).equals(m.getEntry(row, col))) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        int ret = (322562 * 31) + nRows;
        int ret2 = (ret * 31) + nCols;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                ret2 = (ret2 * 31) + ((((row + 1) * 11) + ((col + 1) * 17)) * getEntry(row, col).hashCode());
            }
        }
        return ret2;
    }

    protected void checkRowIndex(int row) {
        if (row < 0 || row >= getRowDimension()) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.ROW_INDEX_OUT_OF_RANGE, java.lang.Integer.valueOf(row), 0, java.lang.Integer.valueOf(getRowDimension() - 1));
        }
    }

    protected void checkColumnIndex(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        if (column < 0 || column >= getColumnDimension()) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.COLUMN_INDEX_OUT_OF_RANGE, java.lang.Integer.valueOf(column), 0, java.lang.Integer.valueOf(getColumnDimension() - 1));
        }
    }

    protected void checkSubMatrixIndex(int startRow, int endRow, int startColumn, int endColumn) {
        checkRowIndex(startRow);
        checkRowIndex(endRow);
        if (startRow > endRow) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, java.lang.Integer.valueOf(startRow), java.lang.Integer.valueOf(endRow));
        }
        checkColumnIndex(startColumn);
        checkColumnIndex(endColumn);
        if (startColumn > endColumn) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.INITIAL_COLUMN_AFTER_FINAL_COLUMN, java.lang.Integer.valueOf(startColumn), java.lang.Integer.valueOf(endColumn));
        }
    }

    protected void checkSubMatrixIndex(int[] selectedRows, int[] selectedColumns) {
        if (selectedRows.length * selectedColumns.length == 0) {
            if (selectedRows.length == 0) {
                throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_SELECTED_ROW_INDEX_ARRAY, new java.lang.Object[0]);
            }
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_SELECTED_COLUMN_INDEX_ARRAY, new java.lang.Object[0]);
        }
        for (int row : selectedRows) {
            checkRowIndex(row);
        }
        for (int column : selectedColumns) {
            checkColumnIndex(column);
        }
    }

    protected void checkAdditionCompatible(org.apache.commons.math.linear.FieldMatrix<T> m) {
        if (getRowDimension() != m.getRowDimension() || getColumnDimension() != m.getColumnDimension()) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_ADDITION_COMPATIBLE_MATRICES, java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()), java.lang.Integer.valueOf(m.getRowDimension()), java.lang.Integer.valueOf(m.getColumnDimension()));
        }
    }

    protected void checkSubtractionCompatible(org.apache.commons.math.linear.FieldMatrix<T> m) {
        if (getRowDimension() != m.getRowDimension() || getColumnDimension() != m.getColumnDimension()) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_SUBTRACTION_COMPATIBLE_MATRICES, java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()), java.lang.Integer.valueOf(m.getRowDimension()), java.lang.Integer.valueOf(m.getColumnDimension()));
        }
    }

    protected void checkMultiplicationCompatible(org.apache.commons.math.linear.FieldMatrix<T> m) {
        if (getColumnDimension() != m.getRowDimension()) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_MULTIPLICATION_COMPATIBLE_MATRICES, java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()), java.lang.Integer.valueOf(m.getRowDimension()), java.lang.Integer.valueOf(m.getColumnDimension()));
        }
    }
}
