package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class MatrixUtils {
    private MatrixUtils() {
    }

    public static org.apache.commons.math.linear.RealMatrix createRealMatrix(int rows, int columns) {
        return rows * columns <= 4096 ? new org.apache.commons.math.linear.Array2DRowRealMatrix(rows, columns) : new org.apache.commons.math.linear.BlockRealMatrix(rows, columns);
    }

    public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createFieldMatrix(org.apache.commons.math.Field<T> field, int rows, int columns) {
        return rows * columns <= 4096 ? new org.apache.commons.math.linear.Array2DRowFieldMatrix(field, rows, columns) : new org.apache.commons.math.linear.BlockFieldMatrix(field, rows, columns);
    }

    public static org.apache.commons.math.linear.RealMatrix createRealMatrix(double[][] data) {
        return data.length * data[0].length <= 4096 ? new org.apache.commons.math.linear.Array2DRowRealMatrix(data) : new org.apache.commons.math.linear.BlockRealMatrix(data);
    }

    public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createFieldMatrix(T[][] data) {
        return data.length * data[0].length <= 4096 ? new org.apache.commons.math.linear.Array2DRowFieldMatrix(data) : new org.apache.commons.math.linear.BlockFieldMatrix(data);
    }

    public static org.apache.commons.math.linear.RealMatrix createRealIdentityMatrix(int dimension) {
        org.apache.commons.math.linear.RealMatrix m = createRealMatrix(dimension, dimension);
        for (int i = 0; i < dimension; i++) {
            m.setEntry(i, i, 1.0d);
        }
        return m;
    }

    public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createFieldIdentityMatrix(org.apache.commons.math.Field<T> field, int dimension) {
        T zero = field.getZero();
        T one = field.getOne();
        org.apache.commons.math.FieldElement[][] fieldElementArr = (org.apache.commons.math.FieldElement[][]) java.lang.reflect.Array.newInstance(zero.getClass(), dimension, dimension);
        for (int row = 0; row < dimension; row++) {
            org.apache.commons.math.FieldElement[] fieldElementArr2 = fieldElementArr[row];
            java.util.Arrays.fill(fieldElementArr2, zero);
            fieldElementArr2[row] = one;
        }
        return new org.apache.commons.math.linear.Array2DRowFieldMatrix(fieldElementArr, false);
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createBigIdentityMatrix(int dimension) {
        java.math.BigDecimal[][] d = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, dimension, dimension);
        for (int row = 0; row < dimension; row++) {
            java.math.BigDecimal[] dRow = d[row];
            java.util.Arrays.fill(dRow, org.apache.commons.math.linear.BigMatrixImpl.ZERO);
            dRow[row] = org.apache.commons.math.linear.BigMatrixImpl.ONE;
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(d, false);
    }

    public static org.apache.commons.math.linear.RealMatrix createRealDiagonalMatrix(double[] diagonal) {
        org.apache.commons.math.linear.RealMatrix m = createRealMatrix(diagonal.length, diagonal.length);
        for (int i = 0; i < diagonal.length; i++) {
            m.setEntry(i, i, diagonal[i]);
        }
        return m;
    }

    public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createFieldDiagonalMatrix(T[] diagonal) {
        org.apache.commons.math.linear.FieldMatrix<T> m = createFieldMatrix(diagonal[0].getField(), diagonal.length, diagonal.length);
        for (int i = 0; i < diagonal.length; i++) {
            m.setEntry(i, i, diagonal[i]);
        }
        return m;
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createBigMatrix(double[][] data) {
        return new org.apache.commons.math.linear.BigMatrixImpl(data);
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createBigMatrix(java.math.BigDecimal[][] data) {
        return new org.apache.commons.math.linear.BigMatrixImpl(data);
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createBigMatrix(java.math.BigDecimal[][] data, boolean copyArray) {
        return new org.apache.commons.math.linear.BigMatrixImpl(data, copyArray);
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createBigMatrix(java.lang.String[][] data) {
        return new org.apache.commons.math.linear.BigMatrixImpl(data);
    }

    public static org.apache.commons.math.linear.RealVector createRealVector(double[] data) {
        return new org.apache.commons.math.linear.ArrayRealVector(data, true);
    }

    public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldVector<T> createFieldVector(T[] data) {
        return new org.apache.commons.math.linear.ArrayFieldVector((org.apache.commons.math.FieldElement[]) data, true);
    }

    public static org.apache.commons.math.linear.RealMatrix createRowRealMatrix(double[] rowData) {
        int nCols = rowData.length;
        org.apache.commons.math.linear.RealMatrix m = createRealMatrix(1, nCols);
        for (int i = 0; i < nCols; i++) {
            m.setEntry(0, i, rowData[i]);
        }
        return m;
    }

    public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createRowFieldMatrix(T[] rowData) {
        int nCols = rowData.length;
        if (nCols == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
        }
        org.apache.commons.math.linear.FieldMatrix<T> m = createFieldMatrix(rowData[0].getField(), 1, nCols);
        for (int i = 0; i < nCols; i++) {
            m.setEntry(0, i, rowData[i]);
        }
        return m;
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createRowBigMatrix(double[] rowData) {
        int nCols = rowData.length;
        java.math.BigDecimal[][] data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, 1, nCols);
        for (int i = 0; i < nCols; i++) {
            data[0][i] = new java.math.BigDecimal(rowData[i]);
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createRowBigMatrix(java.math.BigDecimal[] rowData) {
        int nCols = rowData.length;
        java.math.BigDecimal[][] data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, 1, nCols);
        java.lang.System.arraycopy(rowData, 0, data[0], 0, nCols);
        return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createRowBigMatrix(java.lang.String[] rowData) {
        int nCols = rowData.length;
        java.math.BigDecimal[][] data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, 1, nCols);
        for (int i = 0; i < nCols; i++) {
            data[0][i] = new java.math.BigDecimal(rowData[i]);
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
    }

    public static org.apache.commons.math.linear.RealMatrix createColumnRealMatrix(double[] columnData) {
        int nRows = columnData.length;
        org.apache.commons.math.linear.RealMatrix m = createRealMatrix(nRows, 1);
        for (int i = 0; i < nRows; i++) {
            m.setEntry(i, 0, columnData[i]);
        }
        return m;
    }

    public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createColumnFieldMatrix(T[] columnData) {
        int nRows = columnData.length;
        if (nRows == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_ROW, new java.lang.Object[0]);
        }
        org.apache.commons.math.linear.FieldMatrix<T> m = createFieldMatrix(columnData[0].getField(), nRows, 1);
        for (int i = 0; i < nRows; i++) {
            m.setEntry(i, 0, columnData[i]);
        }
        return m;
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createColumnBigMatrix(double[] columnData) {
        int nRows = columnData.length;
        java.math.BigDecimal[][] data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, 1);
        for (int row = 0; row < nRows; row++) {
            data[row][0] = new java.math.BigDecimal(columnData[row]);
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createColumnBigMatrix(java.math.BigDecimal[] columnData) {
        int nRows = columnData.length;
        java.math.BigDecimal[][] data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, 1);
        for (int row = 0; row < nRows; row++) {
            data[row][0] = columnData[row];
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.linear.BigMatrix createColumnBigMatrix(java.lang.String[] columnData) {
        int nRows = columnData.length;
        java.math.BigDecimal[][] data = (java.math.BigDecimal[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.math.BigDecimal.class, nRows, 1);
        for (int row = 0; row < nRows; row++) {
            data[row][0] = new java.math.BigDecimal(columnData[row]);
        }
        return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
    }

    public static void checkRowIndex(org.apache.commons.math.linear.AnyMatrix m, int row) {
        if (row < 0 || row >= m.getRowDimension()) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.ROW_INDEX_OUT_OF_RANGE, java.lang.Integer.valueOf(row), 0, java.lang.Integer.valueOf(m.getRowDimension() - 1));
        }
    }

    public static void checkColumnIndex(org.apache.commons.math.linear.AnyMatrix m, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        if (column < 0 || column >= m.getColumnDimension()) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.COLUMN_INDEX_OUT_OF_RANGE, java.lang.Integer.valueOf(column), 0, java.lang.Integer.valueOf(m.getColumnDimension() - 1));
        }
    }

    public static void checkSubMatrixIndex(org.apache.commons.math.linear.AnyMatrix m, int startRow, int endRow, int startColumn, int endColumn) {
        checkRowIndex(m, startRow);
        checkRowIndex(m, endRow);
        if (startRow > endRow) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, java.lang.Integer.valueOf(startRow), java.lang.Integer.valueOf(endRow));
        }
        checkColumnIndex(m, startColumn);
        checkColumnIndex(m, endColumn);
        if (startColumn > endColumn) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.INITIAL_COLUMN_AFTER_FINAL_COLUMN, java.lang.Integer.valueOf(startColumn), java.lang.Integer.valueOf(endColumn));
        }
    }

    public static void checkSubMatrixIndex(org.apache.commons.math.linear.AnyMatrix m, int[] selectedRows, int[] selectedColumns) throws org.apache.commons.math.linear.MatrixIndexException {
        if (selectedRows.length * selectedColumns.length == 0) {
            if (selectedRows.length == 0) {
                throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_SELECTED_ROW_INDEX_ARRAY, new java.lang.Object[0]);
            }
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_SELECTED_COLUMN_INDEX_ARRAY, new java.lang.Object[0]);
        }
        for (int row : selectedRows) {
            checkRowIndex(m, row);
        }
        for (int column : selectedColumns) {
            checkColumnIndex(m, column);
        }
    }

    public static void checkAdditionCompatible(org.apache.commons.math.linear.AnyMatrix left, org.apache.commons.math.linear.AnyMatrix right) throws java.lang.IllegalArgumentException {
        if (left.getRowDimension() != right.getRowDimension() || left.getColumnDimension() != right.getColumnDimension()) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_ADDITION_COMPATIBLE_MATRICES, java.lang.Integer.valueOf(left.getRowDimension()), java.lang.Integer.valueOf(left.getColumnDimension()), java.lang.Integer.valueOf(right.getRowDimension()), java.lang.Integer.valueOf(right.getColumnDimension()));
        }
    }

    public static void checkSubtractionCompatible(org.apache.commons.math.linear.AnyMatrix left, org.apache.commons.math.linear.AnyMatrix right) throws java.lang.IllegalArgumentException {
        if (left.getRowDimension() != right.getRowDimension() || left.getColumnDimension() != right.getColumnDimension()) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_SUBTRACTION_COMPATIBLE_MATRICES, java.lang.Integer.valueOf(left.getRowDimension()), java.lang.Integer.valueOf(left.getColumnDimension()), java.lang.Integer.valueOf(right.getRowDimension()), java.lang.Integer.valueOf(right.getColumnDimension()));
        }
    }

    public static void checkMultiplicationCompatible(org.apache.commons.math.linear.AnyMatrix left, org.apache.commons.math.linear.AnyMatrix right) throws java.lang.IllegalArgumentException {
        if (left.getColumnDimension() != right.getRowDimension()) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_MULTIPLICATION_COMPATIBLE_MATRICES, java.lang.Integer.valueOf(left.getRowDimension()), java.lang.Integer.valueOf(left.getColumnDimension()), java.lang.Integer.valueOf(right.getRowDimension()), java.lang.Integer.valueOf(right.getColumnDimension()));
        }
    }

    public static org.apache.commons.math.linear.Array2DRowRealMatrix fractionMatrixToRealMatrix(org.apache.commons.math.linear.FieldMatrix<org.apache.commons.math.fraction.Fraction> m) {
        org.apache.commons.math.linear.MatrixUtils.FractionMatrixConverter converter = new org.apache.commons.math.linear.MatrixUtils.FractionMatrixConverter();
        m.walkInOptimizedOrder(converter);
        return converter.getConvertedMatrix();
    }

    private static class FractionMatrixConverter extends org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor<org.apache.commons.math.fraction.Fraction> {
        private double[][] data;

        public FractionMatrixConverter() {
            super(org.apache.commons.math.fraction.Fraction.ZERO);
        }

        @Override // org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math.linear.FieldMatrixPreservingVisitor
        public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
            this.data = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, rows, columns);
        }

        @Override // org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math.linear.FieldMatrixPreservingVisitor
        public void visit(int row, int column, org.apache.commons.math.fraction.Fraction value) {
            this.data[row][column] = value.doubleValue();
        }

        org.apache.commons.math.linear.Array2DRowRealMatrix getConvertedMatrix() {
            return new org.apache.commons.math.linear.Array2DRowRealMatrix(this.data, false);
        }
    }

    public static org.apache.commons.math.linear.Array2DRowRealMatrix bigFractionMatrixToRealMatrix(org.apache.commons.math.linear.FieldMatrix<org.apache.commons.math.fraction.BigFraction> m) {
        org.apache.commons.math.linear.MatrixUtils.BigFractionMatrixConverter converter = new org.apache.commons.math.linear.MatrixUtils.BigFractionMatrixConverter();
        m.walkInOptimizedOrder(converter);
        return converter.getConvertedMatrix();
    }

    private static class BigFractionMatrixConverter extends org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor<org.apache.commons.math.fraction.BigFraction> {
        private double[][] data;

        public BigFractionMatrixConverter() {
            super(org.apache.commons.math.fraction.BigFraction.ZERO);
        }

        @Override // org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math.linear.FieldMatrixPreservingVisitor
        public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
            this.data = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, rows, columns);
        }

        @Override // org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math.linear.FieldMatrixPreservingVisitor
        public void visit(int row, int column, org.apache.commons.math.fraction.BigFraction value) {
            this.data[row][column] = value.doubleValue();
        }

        org.apache.commons.math.linear.Array2DRowRealMatrix getConvertedMatrix() {
            return new org.apache.commons.math.linear.Array2DRowRealMatrix(this.data, false);
        }
    }

    public static void serializeRealVector(org.apache.commons.math.linear.RealVector vector, java.io.ObjectOutputStream oos) throws java.io.IOException {
        int n = vector.getDimension();
        oos.writeInt(n);
        for (int i = 0; i < n; i++) {
            oos.writeDouble(vector.getEntry(i));
        }
    }

    public static void deserializeRealVector(java.lang.Object instance, java.lang.String fieldName, java.io.ObjectInputStream ois) throws java.io.IOException, java.lang.ClassNotFoundException {
        try {
            int n = ois.readInt();
            double[] data = new double[n];
            for (int i = 0; i < n; i++) {
                data[i] = ois.readDouble();
            }
            org.apache.commons.math.linear.RealVector vector = new org.apache.commons.math.linear.ArrayRealVector(data, false);
            java.lang.reflect.Field f = instance.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(instance, vector);
        } catch (java.lang.IllegalAccessException iae) {
            java.io.IOException ioe = new java.io.IOException();
            ioe.initCause(iae);
            throw ioe;
        } catch (java.lang.NoSuchFieldException nsfe) {
            java.io.IOException ioe2 = new java.io.IOException();
            ioe2.initCause(nsfe);
            throw ioe2;
        }
    }

    public static void serializeRealMatrix(org.apache.commons.math.linear.RealMatrix matrix, java.io.ObjectOutputStream oos) throws java.io.IOException {
        int n = matrix.getRowDimension();
        int m = matrix.getColumnDimension();
        oos.writeInt(n);
        oos.writeInt(m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                oos.writeDouble(matrix.getEntry(i, j));
            }
        }
    }

    public static void deserializeRealMatrix(java.lang.Object instance, java.lang.String fieldName, java.io.ObjectInputStream ois) throws java.io.IOException, java.lang.ClassNotFoundException {
        try {
            int n = ois.readInt();
            int m = ois.readInt();
            double[][] data = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, m);
            for (int i = 0; i < n; i++) {
                double[] dataI = data[i];
                for (int j = 0; j < m; j++) {
                    dataI[j] = ois.readDouble();
                }
            }
            org.apache.commons.math.linear.RealMatrix matrix = new org.apache.commons.math.linear.Array2DRowRealMatrix(data, false);
            java.lang.reflect.Field f = instance.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(instance, matrix);
        } catch (java.lang.IllegalAccessException iae) {
            java.io.IOException ioe = new java.io.IOException();
            ioe.initCause(iae);
            throw ioe;
        } catch (java.lang.NoSuchFieldException nsfe) {
            java.io.IOException ioe2 = new java.io.IOException();
            ioe2.initCause(nsfe);
            throw ioe2;
        }
    }
}
