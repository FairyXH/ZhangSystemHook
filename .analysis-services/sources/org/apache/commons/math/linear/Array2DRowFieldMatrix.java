package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class Array2DRowFieldMatrix<T extends org.apache.commons.math.FieldElement<T>> extends org.apache.commons.math.linear.AbstractFieldMatrix<T> implements java.io.Serializable {
    private static final long serialVersionUID = 7260756672015356458L;
    protected T[][] data;

    public Array2DRowFieldMatrix(org.apache.commons.math.Field<T> field) {
        super(field);
    }

    public Array2DRowFieldMatrix(org.apache.commons.math.Field<T> field, int i, int i2) throws java.lang.IllegalArgumentException {
        super(field, i, i2);
        this.data = (T[][]) buildArray(field, i, i2);
    }

    public Array2DRowFieldMatrix(T[][] d) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
        super(extractField(d));
        copyIn(d);
    }

    public Array2DRowFieldMatrix(T[][] d, boolean copyArray) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
        super(extractField(d));
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

    public Array2DRowFieldMatrix(T[] tArr) {
        super(extractField(tArr));
        int length = tArr.length;
        this.data = (T[][]) buildArray(getField(), length, 1);
        for (int i = 0; i < length; i++) {
            this.data[i][0] = tArr[i];
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> createMatrix(int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        return new org.apache.commons.math.linear.Array2DRowFieldMatrix(getField(), rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> copy() {
        return new org.apache.commons.math.linear.Array2DRowFieldMatrix(copyOut(), false);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> add(org.apache.commons.math.linear.FieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        try {
            return add((org.apache.commons.math.linear.Array2DRowFieldMatrix) m);
        } catch (java.lang.ClassCastException e) {
            return super.add(m);
        }
    }

    public org.apache.commons.math.linear.Array2DRowFieldMatrix<T> add(org.apache.commons.math.linear.Array2DRowFieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        checkAdditionCompatible(m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.FieldElement[][] fieldElementArrBuildArray = buildArray(getField(), rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            T[] dataRow = this.data[row];
            T[] mRow = m.data[row];
            org.apache.commons.math.FieldElement[] fieldElementArr = fieldElementArrBuildArray[row];
            for (int col = 0; col < columnCount; col++) {
                fieldElementArr[col] = (org.apache.commons.math.FieldElement) dataRow[col].add(mRow[col]);
            }
        }
        return new org.apache.commons.math.linear.Array2DRowFieldMatrix<>(fieldElementArrBuildArray, false);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> subtract(org.apache.commons.math.linear.FieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        try {
            return subtract((org.apache.commons.math.linear.Array2DRowFieldMatrix) m);
        } catch (java.lang.ClassCastException e) {
            return super.subtract(m);
        }
    }

    public org.apache.commons.math.linear.Array2DRowFieldMatrix<T> subtract(org.apache.commons.math.linear.Array2DRowFieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        checkSubtractionCompatible(m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        org.apache.commons.math.FieldElement[][] fieldElementArrBuildArray = buildArray(getField(), rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            T[] dataRow = this.data[row];
            T[] mRow = m.data[row];
            org.apache.commons.math.FieldElement[] fieldElementArr = fieldElementArrBuildArray[row];
            for (int col = 0; col < columnCount; col++) {
                fieldElementArr[col] = (org.apache.commons.math.FieldElement) dataRow[col].subtract(mRow[col]);
            }
        }
        return new org.apache.commons.math.linear.Array2DRowFieldMatrix<>(fieldElementArrBuildArray, false);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> multiply(org.apache.commons.math.linear.FieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        try {
            return multiply((org.apache.commons.math.linear.Array2DRowFieldMatrix) m);
        } catch (java.lang.ClassCastException e) {
            return super.multiply(m);
        }
    }

    public org.apache.commons.math.linear.Array2DRowFieldMatrix<T> multiply(org.apache.commons.math.linear.Array2DRowFieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        checkMultiplicationCompatible(m);
        int nRows = getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = getColumnDimension();
        org.apache.commons.math.FieldElement[][] fieldElementArrBuildArray = buildArray(getField(), nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            T[] dataRow = this.data[row];
            org.apache.commons.math.FieldElement[] fieldElementArr = fieldElementArrBuildArray[row];
            for (int col = 0; col < nCols; col++) {
                T zero = getField().getZero();
                for (int i = 0; i < nSum; i++) {
                    zero = (org.apache.commons.math.FieldElement) zero.add((org.apache.commons.math.FieldElement) dataRow[i].multiply(m.data[i][col]));
                }
                fieldElementArr[col] = zero;
            }
        }
        return new org.apache.commons.math.linear.Array2DRowFieldMatrix<>(fieldElementArrBuildArray, false);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T[][] getData() {
        return (T[][]) copyOut();
    }

    public T[][] getDataRef() {
        return this.data;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setSubMatrix(T[][] tArr, int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException {
        if (this.data == null) {
            if (i > 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.FIRST_ROWS_NOT_INITIALIZED_YET, java.lang.Integer.valueOf(i));
            }
            if (i2 > 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.FIRST_COLUMNS_NOT_INITIALIZED_YET, java.lang.Integer.valueOf(i2));
            }
            if (tArr.length == 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_ROW, new java.lang.Object[0]);
            }
            int length = tArr[0].length;
            if (length == 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
            }
            this.data = (T[][]) buildArray(getField(), tArr.length, length);
            for (int i3 = 0; i3 < this.data.length; i3++) {
                if (tArr[i3].length == length) {
                    java.lang.System.arraycopy(tArr[i3], 0, this.data[i3 + i], i2, length);
                } else {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(length), java.lang.Integer.valueOf(tArr[i3].length));
                }
            }
            return;
        }
        super.setSubMatrix(tArr, i, i2);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T getEntry(int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            return this.data[row][column];
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setEntry(int row, int column, T value) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            this.data[row][column] = value;
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void addToEntry(int i, int i2, T t) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            ((T[][]) this.data)[i][i2] = (org.apache.commons.math.FieldElement) this.data[i][i2].add(t);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void multiplyEntry(int i, int i2, T t) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            ((T[][]) this.data)[i][i2] = (org.apache.commons.math.FieldElement) this.data[i][i2].multiply(t);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getRowDimension() {
        if (this.data == null) {
            return 0;
        }
        return this.data.length;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getColumnDimension() {
        if (this.data == null || this.data[0] == null) {
            return 0;
        }
        return this.data[0].length;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T[] operate(T[] tArr) throws java.lang.IllegalArgumentException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        if (tArr.length != columnDimension) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(tArr.length), java.lang.Integer.valueOf(columnDimension));
        }
        T[] tArr2 = (T[]) buildArray(getField(), rowDimension);
        for (int i = 0; i < rowDimension; i++) {
            T[] tArr3 = this.data[i];
            T zero = getField().getZero();
            for (int i2 = 0; i2 < columnDimension; i2++) {
                zero = (T) zero.add((org.apache.commons.math.FieldElement) tArr3[i2].multiply(tArr[i2]));
            }
            tArr2[i] = zero;
        }
        return tArr2;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T[] preMultiply(T[] tArr) throws java.lang.IllegalArgumentException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        if (tArr.length != rowDimension) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(tArr.length), java.lang.Integer.valueOf(rowDimension));
        }
        T[] tArr2 = (T[]) buildArray(getField(), columnDimension);
        for (int i = 0; i < columnDimension; i++) {
            T zero = getField().getZero();
            for (int i2 = 0; i2 < rowDimension; i2++) {
                zero = (T) zero.add((org.apache.commons.math.FieldElement) this.data[i2][i].multiply(tArr[i2]));
            }
            tArr2[i] = zero;
        }
        return tArr2;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixChangingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i = 0; i < rowDimension; i++) {
            java.lang.Object[] objArr = this.data[i];
            for (int i2 = 0; i2 < columnDimension; i2++) {
                objArr[i2] = fieldMatrixChangingVisitor.visit(i, i2, objArr[i2]);
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixPreservingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i = 0; i < rowDimension; i++) {
            T[] tArr = this.data[i];
            for (int i2 = 0; i2 < columnDimension; i2++) {
                fieldMatrixPreservingVisitor.visit(i, i2, tArr[i2]);
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixChangingVisitor.start(getRowDimension(), getColumnDimension(), i, i2, i3, i4);
        for (int i5 = i; i5 <= i2; i5++) {
            java.lang.Object[] objArr = this.data[i5];
            for (int i6 = i3; i6 <= i4; i6++) {
                objArr[i6] = fieldMatrixChangingVisitor.visit(i5, i6, objArr[i6]);
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixPreservingVisitor.start(getRowDimension(), getColumnDimension(), i, i2, i3, i4);
        for (int i5 = i; i5 <= i2; i5++) {
            T[] tArr = this.data[i5];
            for (int i6 = i3; i6 <= i4; i6++) {
                fieldMatrixPreservingVisitor.visit(i5, i6, tArr[i6]);
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixChangingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i = 0; i < columnDimension; i++) {
            for (int i2 = 0; i2 < rowDimension; i2++) {
                java.lang.Object[] objArr = this.data[i2];
                objArr[i] = fieldMatrixChangingVisitor.visit(i2, i, objArr[i]);
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixPreservingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i = 0; i < columnDimension; i++) {
            for (int i2 = 0; i2 < rowDimension; i2++) {
                fieldMatrixPreservingVisitor.visit(i2, i, this.data[i2][i]);
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixChangingVisitor.start(getRowDimension(), getColumnDimension(), i, i2, i3, i4);
        for (int i5 = i3; i5 <= i4; i5++) {
            for (int i6 = i; i6 <= i2; i6++) {
                java.lang.Object[] objArr = this.data[i6];
                objArr[i5] = fieldMatrixChangingVisitor.visit(i6, i5, objArr[i5]);
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixPreservingVisitor.start(getRowDimension(), getColumnDimension(), i, i2, i3, i4);
        for (int i5 = i3; i5 <= i4; i5++) {
            for (int i6 = i; i6 <= i2; i6++) {
                fieldMatrixPreservingVisitor.visit(i6, i5, this.data[i6][i5]);
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    private T[][] copyOut() {
        int rowDimension = getRowDimension();
        T[][] tArr = (T[][]) buildArray(getField(), rowDimension, getColumnDimension());
        for (int i = 0; i < rowDimension; i++) {
            java.lang.System.arraycopy(this.data[i], 0, tArr[i], 0, this.data[i].length);
        }
        return tArr;
    }

    private void copyIn(T[][] in) {
        setSubMatrix(in, 0, 0);
    }
}
