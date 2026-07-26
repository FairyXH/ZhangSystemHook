package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class SparseFieldMatrix<T extends org.apache.commons.math.FieldElement<T>> extends org.apache.commons.math.linear.AbstractFieldMatrix<T> {
    private static final long serialVersionUID = 9078068119297757342L;
    private final int columns;
    private final org.apache.commons.math.util.OpenIntToFieldHashMap<T> entries;
    private final int rows;

    public SparseFieldMatrix(org.apache.commons.math.Field<T> field) {
        super(field);
        this.rows = 0;
        this.columns = 0;
        this.entries = new org.apache.commons.math.util.OpenIntToFieldHashMap<>(field);
    }

    public SparseFieldMatrix(org.apache.commons.math.Field<T> field, int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        super(field, rowDimension, columnDimension);
        this.rows = rowDimension;
        this.columns = columnDimension;
        this.entries = new org.apache.commons.math.util.OpenIntToFieldHashMap<>(field);
    }

    public SparseFieldMatrix(org.apache.commons.math.linear.SparseFieldMatrix<T> other) {
        super(other.getField(), other.getRowDimension(), other.getColumnDimension());
        this.rows = other.getRowDimension();
        this.columns = other.getColumnDimension();
        this.entries = new org.apache.commons.math.util.OpenIntToFieldHashMap<>(other.entries);
    }

    public SparseFieldMatrix(org.apache.commons.math.linear.FieldMatrix<T> other) {
        super(other.getField(), other.getRowDimension(), other.getColumnDimension());
        this.rows = other.getRowDimension();
        this.columns = other.getColumnDimension();
        this.entries = new org.apache.commons.math.util.OpenIntToFieldHashMap<>(getField());
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                setEntry(i, j, other.getEntry(i, j));
            }
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void addToEntry(int row, int column, T increment) throws org.apache.commons.math.linear.MatrixIndexException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int key = computeKey(row, column);
        org.apache.commons.math.FieldElement fieldElement = (org.apache.commons.math.FieldElement) this.entries.get(key).add(increment);
        if (getField().getZero().equals(fieldElement)) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, fieldElement);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> copy() {
        return new org.apache.commons.math.linear.SparseFieldMatrix((org.apache.commons.math.linear.SparseFieldMatrix) this);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> createMatrix(int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        return new org.apache.commons.math.linear.SparseFieldMatrix(getField(), rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getColumnDimension() {
        return this.columns;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T getEntry(int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException {
        checkRowIndex(i);
        checkColumnIndex(i2);
        return (T) this.entries.get(computeKey(i, i2));
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getRowDimension() {
        return this.rows;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void multiplyEntry(int row, int column, T factor) throws org.apache.commons.math.linear.MatrixIndexException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int key = computeKey(row, column);
        org.apache.commons.math.FieldElement fieldElement = (org.apache.commons.math.FieldElement) this.entries.get(key).multiply(factor);
        if (getField().getZero().equals(fieldElement)) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, fieldElement);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setEntry(int row, int column, T value) throws org.apache.commons.math.linear.MatrixIndexException {
        checkRowIndex(row);
        checkColumnIndex(column);
        if (getField().getZero().equals(value)) {
            this.entries.remove(computeKey(row, column));
        } else {
            this.entries.put(computeKey(row, column), value);
        }
    }

    private int computeKey(int row, int column) {
        return (this.columns * row) + column;
    }
}
