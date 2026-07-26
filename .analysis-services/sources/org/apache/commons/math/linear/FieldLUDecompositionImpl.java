package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class FieldLUDecompositionImpl<T extends org.apache.commons.math.FieldElement<T>> implements org.apache.commons.math.linear.FieldLUDecomposition<T> {
    private org.apache.commons.math.linear.FieldMatrix<T> cachedL;
    private org.apache.commons.math.linear.FieldMatrix<T> cachedP;
    private org.apache.commons.math.linear.FieldMatrix<T> cachedU;
    private boolean even;
    private final org.apache.commons.math.Field<T> field;
    private T[][] lu;
    private int[] pivot;
    private boolean singular;

    public FieldLUDecompositionImpl(org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws org.apache.commons.math.linear.NonSquareMatrixException {
        if (!fieldMatrix.isSquare()) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(fieldMatrix.getRowDimension(), fieldMatrix.getColumnDimension());
        }
        int columnDimension = fieldMatrix.getColumnDimension();
        this.field = fieldMatrix.getField();
        this.lu = (T[][]) fieldMatrix.getData();
        this.pivot = new int[columnDimension];
        this.cachedL = null;
        this.cachedU = null;
        this.cachedP = null;
        for (int i = 0; i < columnDimension; i++) {
            this.pivot[i] = i;
        }
        this.even = true;
        this.singular = false;
        for (int i2 = 0; i2 < columnDimension; i2++) {
            this.field.getZero();
            for (int i3 = 0; i3 < i2; i3++) {
                org.apache.commons.math.FieldElement[] fieldElementArr = this.lu[i3];
                org.apache.commons.math.FieldElement fieldElement = fieldElementArr[i2];
                for (int i4 = 0; i4 < i3; i4++) {
                    fieldElement = (org.apache.commons.math.FieldElement) fieldElement.subtract((org.apache.commons.math.FieldElement) fieldElementArr[i4].multiply(this.lu[i4][i2]));
                }
                fieldElementArr[i2] = fieldElement;
            }
            int i5 = i2;
            for (int i6 = i2; i6 < columnDimension; i6++) {
                org.apache.commons.math.FieldElement[] fieldElementArr2 = this.lu[i6];
                org.apache.commons.math.FieldElement fieldElement2 = fieldElementArr2[i2];
                for (int i7 = 0; i7 < i2; i7++) {
                    fieldElement2 = (org.apache.commons.math.FieldElement) fieldElement2.subtract((org.apache.commons.math.FieldElement) fieldElementArr2[i7].multiply(this.lu[i7][i2]));
                }
                fieldElementArr2[i2] = fieldElement2;
                if (this.lu[i5][i2].equals(this.field.getZero())) {
                    i5++;
                }
            }
            if (i5 >= columnDimension) {
                this.singular = true;
                return;
            }
            if (i5 != i2) {
                this.field.getZero();
                for (int i8 = 0; i8 < columnDimension; i8++) {
                    T t = this.lu[i5][i8];
                    this.lu[i5][i8] = this.lu[i2][i8];
                    this.lu[i2][i8] = t;
                }
                int i9 = this.pivot[i5];
                this.pivot[i5] = this.pivot[i2];
                this.pivot[i2] = i9;
                this.even = !this.even;
            }
            T t2 = this.lu[i2][i2];
            for (int i10 = i2 + 1; i10 < columnDimension; i10++) {
                org.apache.commons.math.FieldElement[] fieldElementArr3 = this.lu[i10];
                fieldElementArr3[i2] = (org.apache.commons.math.FieldElement) fieldElementArr3[i2].divide(t2);
            }
        }
    }

    @Override // org.apache.commons.math.linear.FieldLUDecomposition
    public org.apache.commons.math.linear.FieldMatrix<T> getL() {
        if (this.cachedL == null && !this.singular) {
            int m = this.pivot.length;
            this.cachedL = new org.apache.commons.math.linear.Array2DRowFieldMatrix(this.field, m, m);
            for (int i = 0; i < m; i++) {
                T[] luI = this.lu[i];
                for (int j = 0; j < i; j++) {
                    this.cachedL.setEntry(i, j, luI[j]);
                }
                this.cachedL.setEntry(i, i, this.field.getOne());
            }
        }
        return this.cachedL;
    }

    @Override // org.apache.commons.math.linear.FieldLUDecomposition
    public org.apache.commons.math.linear.FieldMatrix<T> getU() {
        if (this.cachedU == null && !this.singular) {
            int m = this.pivot.length;
            this.cachedU = new org.apache.commons.math.linear.Array2DRowFieldMatrix(this.field, m, m);
            for (int i = 0; i < m; i++) {
                T[] luI = this.lu[i];
                for (int j = i; j < m; j++) {
                    this.cachedU.setEntry(i, j, luI[j]);
                }
            }
        }
        return this.cachedU;
    }

    @Override // org.apache.commons.math.linear.FieldLUDecomposition
    public org.apache.commons.math.linear.FieldMatrix<T> getP() {
        if (this.cachedP == null && !this.singular) {
            int m = this.pivot.length;
            this.cachedP = new org.apache.commons.math.linear.Array2DRowFieldMatrix(this.field, m, m);
            for (int i = 0; i < m; i++) {
                this.cachedP.setEntry(i, this.pivot[i], this.field.getOne());
            }
        }
        return this.cachedP;
    }

    @Override // org.apache.commons.math.linear.FieldLUDecomposition
    public int[] getPivot() {
        return (int[]) this.pivot.clone();
    }

    @Override // org.apache.commons.math.linear.FieldLUDecomposition
    public T getDeterminant() {
        if (this.singular) {
            return this.field.getZero();
        }
        int m = this.pivot.length;
        T t = (T) (this.even ? this.field.getOne() : this.field.getZero().subtract(this.field.getOne()));
        for (int i = 0; i < m; i++) {
            t = (T) t.multiply(this.lu[i][i]);
        }
        return t;
    }

    @Override // org.apache.commons.math.linear.FieldLUDecomposition
    public org.apache.commons.math.linear.FieldDecompositionSolver<T> getSolver() {
        return new org.apache.commons.math.linear.FieldLUDecompositionImpl.Solver(this.field, this.lu, this.pivot, this.singular);
    }

    private static class Solver<T extends org.apache.commons.math.FieldElement<T>> implements org.apache.commons.math.linear.FieldDecompositionSolver<T> {
        private static final long serialVersionUID = -6353105415121373022L;
        private final org.apache.commons.math.Field<T> field;
        private final T[][] lu;
        private final int[] pivot;
        private final boolean singular;

        private Solver(org.apache.commons.math.Field<T> field, T[][] lu, int[] pivot, boolean singular) {
            this.field = field;
            this.lu = lu;
            this.pivot = pivot;
            this.singular = singular;
        }

        @Override // org.apache.commons.math.linear.FieldDecompositionSolver
        public boolean isNonSingular() {
            return !this.singular;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.apache.commons.math.linear.FieldDecompositionSolver
        public T[] solve(T[] tArr) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            int length = this.pivot.length;
            if (tArr.length != length) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(tArr.length), java.lang.Integer.valueOf(length));
            }
            if (this.singular) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            T[] tArr2 = (T[]) ((org.apache.commons.math.FieldElement[]) java.lang.reflect.Array.newInstance(this.field.getZero().getClass(), length));
            for (int i = 0; i < length; i++) {
                tArr2[i] = tArr[this.pivot[i]];
            }
            for (int i2 = 0; i2 < length; i2++) {
                org.apache.commons.math.util.BigReal bigReal = tArr2[i2];
                for (int i3 = i2 + 1; i3 < length; i3++) {
                    tArr2[i3] = (org.apache.commons.math.FieldElement) tArr2[i3].subtract((org.apache.commons.math.FieldElement) bigReal.multiply(this.lu[i3][i2]));
                }
            }
            for (int i4 = length - 1; i4 >= 0; i4--) {
                tArr2[i4] = (org.apache.commons.math.FieldElement) tArr2[i4].divide(this.lu[i4][i4]);
                org.apache.commons.math.util.BigReal bigReal2 = tArr2[i4];
                for (int i5 = 0; i5 < i4; i5++) {
                    tArr2[i5] = (org.apache.commons.math.FieldElement) tArr2[i5].subtract((org.apache.commons.math.FieldElement) bigReal2.multiply(this.lu[i5][i4]));
                }
            }
            return tArr2;
        }

        @Override // org.apache.commons.math.linear.FieldDecompositionSolver
        public org.apache.commons.math.linear.FieldVector<T> solve(org.apache.commons.math.linear.FieldVector<T> b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            try {
                return solve((org.apache.commons.math.linear.ArrayFieldVector) b);
            } catch (java.lang.ClassCastException e) {
                int m = this.pivot.length;
                if (b.getDimension() != m) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(b.getDimension()), java.lang.Integer.valueOf(m));
                }
                if (this.singular) {
                    throw new org.apache.commons.math.linear.SingularMatrixException();
                }
                org.apache.commons.math.FieldElement[] fieldElementArr = (org.apache.commons.math.FieldElement[]) java.lang.reflect.Array.newInstance(this.field.getZero().getClass(), m);
                for (int row = 0; row < m; row++) {
                    fieldElementArr[row] = b.getEntry(this.pivot[row]);
                }
                for (int col = 0; col < m; col++) {
                    org.apache.commons.math.FieldElement fieldElement = fieldElementArr[col];
                    for (int i = col + 1; i < m; i++) {
                        fieldElementArr[i] = (org.apache.commons.math.FieldElement) fieldElementArr[i].subtract((org.apache.commons.math.FieldElement) fieldElement.multiply(this.lu[i][col]));
                    }
                }
                for (int col2 = m - 1; col2 >= 0; col2--) {
                    fieldElementArr[col2] = (org.apache.commons.math.FieldElement) fieldElementArr[col2].divide(this.lu[col2][col2]);
                    org.apache.commons.math.FieldElement fieldElement2 = fieldElementArr[col2];
                    for (int i2 = 0; i2 < col2; i2++) {
                        fieldElementArr[i2] = (org.apache.commons.math.FieldElement) fieldElementArr[i2].subtract((org.apache.commons.math.FieldElement) fieldElement2.multiply(this.lu[i2][col2]));
                    }
                }
                return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArr, false);
            }
        }

        public org.apache.commons.math.linear.ArrayFieldVector<T> solve(org.apache.commons.math.linear.ArrayFieldVector<T> b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            return new org.apache.commons.math.linear.ArrayFieldVector<>(solve(b.getDataRef()), false);
        }

        @Override // org.apache.commons.math.linear.FieldDecompositionSolver
        public org.apache.commons.math.linear.FieldMatrix<T> solve(org.apache.commons.math.linear.FieldMatrix<T> b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            int m = this.pivot.length;
            if (b.getRowDimension() != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(b.getRowDimension()), java.lang.Integer.valueOf(b.getColumnDimension()), java.lang.Integer.valueOf(m), "n");
            }
            if (this.singular) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            int nColB = b.getColumnDimension();
            org.apache.commons.math.FieldElement[][] fieldElementArr = (org.apache.commons.math.FieldElement[][]) java.lang.reflect.Array.newInstance(this.field.getZero().getClass(), m, nColB);
            for (int row = 0; row < m; row++) {
                org.apache.commons.math.FieldElement[] fieldElementArr2 = fieldElementArr[row];
                int pRow = this.pivot[row];
                for (int col = 0; col < nColB; col++) {
                    fieldElementArr2[col] = b.getEntry(pRow, col);
                }
            }
            for (int col2 = 0; col2 < m; col2++) {
                org.apache.commons.math.FieldElement[] fieldElementArr3 = fieldElementArr[col2];
                for (int i = col2 + 1; i < m; i++) {
                    org.apache.commons.math.FieldElement[] fieldElementArr4 = fieldElementArr[i];
                    T luICol = this.lu[i][col2];
                    for (int j = 0; j < nColB; j++) {
                        fieldElementArr4[j] = (org.apache.commons.math.FieldElement) fieldElementArr4[j].subtract((org.apache.commons.math.FieldElement) fieldElementArr3[j].multiply(luICol));
                    }
                }
            }
            for (int col3 = m - 1; col3 >= 0; col3--) {
                org.apache.commons.math.FieldElement[] fieldElementArr5 = fieldElementArr[col3];
                T luDiag = this.lu[col3][col3];
                for (int j2 = 0; j2 < nColB; j2++) {
                    fieldElementArr5[j2] = (org.apache.commons.math.FieldElement) fieldElementArr5[j2].divide(luDiag);
                }
                for (int i2 = 0; i2 < col3; i2++) {
                    org.apache.commons.math.FieldElement[] fieldElementArr6 = fieldElementArr[i2];
                    T luICol2 = this.lu[i2][col3];
                    for (int j3 = 0; j3 < nColB; j3++) {
                        fieldElementArr6[j3] = (org.apache.commons.math.FieldElement) fieldElementArr6[j3].subtract((org.apache.commons.math.FieldElement) fieldElementArr5[j3].multiply(luICol2));
                    }
                }
            }
            return new org.apache.commons.math.linear.Array2DRowFieldMatrix(fieldElementArr, false);
        }

        @Override // org.apache.commons.math.linear.FieldDecompositionSolver
        public org.apache.commons.math.linear.FieldMatrix<T> getInverse() throws org.apache.commons.math.linear.InvalidMatrixException {
            int m = this.pivot.length;
            T one = this.field.getOne();
            org.apache.commons.math.linear.FieldMatrix<T> identity = new org.apache.commons.math.linear.Array2DRowFieldMatrix<>(this.field, m, m);
            for (int i = 0; i < m; i++) {
                identity.setEntry(i, i, one);
            }
            return solve(identity);
        }
    }
}
