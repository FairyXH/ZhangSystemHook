package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class OpenMapRealMatrix extends org.apache.commons.math.linear.AbstractRealMatrix implements org.apache.commons.math.linear.SparseRealMatrix, java.io.Serializable {
    private static final long serialVersionUID = -5962461716457143437L;
    private final int columns;
    private final org.apache.commons.math.util.OpenIntToDoubleHashMap entries;
    private final int rows;

    public OpenMapRealMatrix(int rowDimension, int columnDimension) {
        super(rowDimension, columnDimension);
        this.rows = rowDimension;
        this.columns = columnDimension;
        this.entries = new org.apache.commons.math.util.OpenIntToDoubleHashMap(0.0d);
    }

    public OpenMapRealMatrix(org.apache.commons.math.linear.OpenMapRealMatrix matrix) {
        this.rows = matrix.rows;
        this.columns = matrix.columns;
        this.entries = new org.apache.commons.math.util.OpenIntToDoubleHashMap(matrix.entries);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.OpenMapRealMatrix copy() {
        return new org.apache.commons.math.linear.OpenMapRealMatrix(this);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.OpenMapRealMatrix createMatrix(int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        return new org.apache.commons.math.linear.OpenMapRealMatrix(rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getColumnDimension() {
        return this.columns;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.OpenMapRealMatrix add(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return add((org.apache.commons.math.linear.OpenMapRealMatrix) m);
        } catch (java.lang.ClassCastException e) {
            return (org.apache.commons.math.linear.OpenMapRealMatrix) super.add(m);
        }
    }

    public org.apache.commons.math.linear.OpenMapRealMatrix add(org.apache.commons.math.linear.OpenMapRealMatrix m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkAdditionCompatible(this, m);
        org.apache.commons.math.linear.OpenMapRealMatrix out = new org.apache.commons.math.linear.OpenMapRealMatrix(this);
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iterator = m.entries.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            int row = iterator.key() / this.columns;
            int col = iterator.key() - (this.columns * row);
            out.setEntry(row, col, getEntry(row, col) + iterator.value());
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.OpenMapRealMatrix subtract(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return subtract((org.apache.commons.math.linear.OpenMapRealMatrix) m);
        } catch (java.lang.ClassCastException e) {
            return (org.apache.commons.math.linear.OpenMapRealMatrix) super.subtract(m);
        }
    }

    public org.apache.commons.math.linear.OpenMapRealMatrix subtract(org.apache.commons.math.linear.OpenMapRealMatrix m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkAdditionCompatible(this, m);
        org.apache.commons.math.linear.OpenMapRealMatrix out = new org.apache.commons.math.linear.OpenMapRealMatrix(this);
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iterator = m.entries.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            int row = iterator.key() / this.columns;
            int col = iterator.key() - (this.columns * row);
            out.setEntry(row, col, getEntry(row, col) - iterator.value());
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix multiply(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return multiply((org.apache.commons.math.linear.OpenMapRealMatrix) m);
        } catch (java.lang.ClassCastException e) {
            org.apache.commons.math.linear.MatrixUtils.checkMultiplicationCompatible(this, m);
            int outCols = m.getColumnDimension();
            org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(this.rows, outCols);
            org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iterator = this.entries.iterator();
            while (iterator.hasNext()) {
                iterator.advance();
                double value = iterator.value();
                int key = iterator.key();
                int i = key / this.columns;
                int k = key % this.columns;
                for (int j = 0; j < outCols; j++) {
                    out.addToEntry(i, j, m.getEntry(k, j) * value);
                }
            }
            return out;
        }
    }

    public org.apache.commons.math.linear.OpenMapRealMatrix multiply(org.apache.commons.math.linear.OpenMapRealMatrix m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkMultiplicationCompatible(this, m);
        int outCols = m.getColumnDimension();
        org.apache.commons.math.linear.OpenMapRealMatrix out = new org.apache.commons.math.linear.OpenMapRealMatrix(this.rows, outCols);
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iterator = this.entries.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            double value = iterator.value();
            int key = iterator.key();
            int i = key / this.columns;
            int k = key % this.columns;
            for (int j = 0; j < outCols; j++) {
                int rightKey = m.computeKey(k, j);
                if (m.entries.containsKey(rightKey)) {
                    int outKey = out.computeKey(i, j);
                    double outValue = out.entries.get(outKey) + (m.entries.get(rightKey) * value);
                    if (outValue == 0.0d) {
                        out.entries.remove(outKey);
                    } else {
                        out.entries.put(outKey, outValue);
                    }
                }
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double getEntry(int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        return this.entries.get(computeKey(row, column));
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getRowDimension() {
        return this.rows;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setEntry(int row, int column, double value) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        if (value == 0.0d) {
            this.entries.remove(computeKey(row, column));
        } else {
            this.entries.put(computeKey(row, column), value);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void addToEntry(int row, int column, double increment) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int key = computeKey(row, column);
        double value = this.entries.get(key) + increment;
        if (value == 0.0d) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, value);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void multiplyEntry(int row, int column, double factor) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int key = computeKey(row, column);
        double value = this.entries.get(key) * factor;
        if (value == 0.0d) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, value);
        }
    }

    private int computeKey(int row, int column) {
        return (this.columns * row) + column;
    }
}
