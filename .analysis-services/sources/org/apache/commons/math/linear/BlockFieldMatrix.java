package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class BlockFieldMatrix<T extends org.apache.commons.math.FieldElement<T>> extends org.apache.commons.math.linear.AbstractFieldMatrix<T> implements java.io.Serializable {
    public static final int BLOCK_SIZE = 36;
    private static final long serialVersionUID = -4602336630143123183L;
    private final int blockColumns;
    private final int blockRows;
    private final T[][] blocks;
    private final int columns;
    private final int rows;

    public BlockFieldMatrix(org.apache.commons.math.Field<T> field, int i, int i2) throws java.lang.IllegalArgumentException {
        super(field, i, i2);
        this.rows = i;
        this.columns = i2;
        this.blockRows = ((i + 36) - 1) / 36;
        this.blockColumns = ((i2 + 36) - 1) / 36;
        this.blocks = (T[][]) createBlocksLayout(field, i, i2);
    }

    public BlockFieldMatrix(T[][] rawData) throws java.lang.IllegalArgumentException {
        this(rawData.length, rawData[0].length, toBlocksLayout(rawData), false);
    }

    public BlockFieldMatrix(int i, int i2, T[][] tArr, boolean z) throws java.lang.IllegalArgumentException {
        super(extractField(tArr), i, i2);
        this.rows = i;
        this.columns = i2;
        this.blockRows = ((i + 36) - 1) / 36;
        this.blockColumns = ((i2 + 36) - 1) / 36;
        if (z) {
            this.blocks = (T[][]) buildArray(getField(), this.blockRows * this.blockColumns, -1);
        } else {
            this.blocks = tArr;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.blockRows; i4++) {
            int iBlockHeight = blockHeight(i4);
            int i5 = 0;
            while (i5 < this.blockColumns) {
                if (tArr[i3].length != blockWidth(i5) * iBlockHeight) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.WRONG_BLOCK_LENGTH, java.lang.Integer.valueOf(tArr[i3].length), java.lang.Integer.valueOf(blockWidth(i5) * iBlockHeight));
                }
                if (z) {
                    ((T[][]) this.blocks)[i3] = (org.apache.commons.math.FieldElement[]) tArr[i3].clone();
                }
                i5++;
                i3++;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T extends org.apache.commons.math.FieldElement<T>> T[][] toBlocksLayout(T[][] tArr) throws java.lang.IllegalArgumentException {
        int length = tArr.length;
        int length2 = tArr[0].length;
        int i = ((length + 36) - 1) / 36;
        int i2 = ((length2 + 36) - 1) / 36;
        for (T[] tArr2 : tArr) {
            int length3 = tArr2.length;
            if (length3 != length2) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(length2), java.lang.Integer.valueOf(length3));
            }
        }
        org.apache.commons.math.Field fieldExtractField = extractField(tArr);
        T[][] tArr3 = (T[][]) buildArray(fieldExtractField, i * i2, -1);
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            int i5 = i4 * 36;
            int iMin = org.apache.commons.math.util.FastMath.min(i5 + 36, length);
            int i6 = iMin - i5;
            int i7 = 0;
            while (i7 < i2) {
                int i8 = i7 * 36;
                int iMin2 = org.apache.commons.math.util.FastMath.min(i8 + 36, length2) - i8;
                int i9 = length;
                org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(fieldExtractField, i6 * iMin2);
                tArr3[i3] = fieldElementArrBuildArray;
                int i10 = length2;
                int i11 = 0;
                int i12 = i;
                int i13 = i5;
                while (i13 < iMin) {
                    java.lang.System.arraycopy(tArr[i13], i8, fieldElementArrBuildArray, i11, iMin2);
                    i11 += iMin2;
                    i13++;
                    i2 = i2;
                }
                i3++;
                i7++;
                length = i9;
                i = i12;
                length2 = i10;
            }
        }
        return tArr3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T extends org.apache.commons.math.FieldElement<T>> T[][] createBlocksLayout(org.apache.commons.math.Field<T> field, int i, int i2) {
        int i3 = ((i + 36) - 1) / 36;
        int i4 = ((i2 + 36) - 1) / 36;
        T[][] tArr = (T[][]) buildArray(field, i3 * i4, -1);
        int i5 = 0;
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = i6 * 36;
            int iMin = org.apache.commons.math.util.FastMath.min(i7 + 36, i) - i7;
            for (int i8 = 0; i8 < i4; i8++) {
                int i9 = i8 * 36;
                tArr[i5] = buildArray(field, iMin * (org.apache.commons.math.util.FastMath.min(i9 + 36, i2) - i9));
                i5++;
            }
        }
        return tArr;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> createMatrix(int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        return new org.apache.commons.math.linear.BlockFieldMatrix(getField(), rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> copy() {
        org.apache.commons.math.linear.BlockFieldMatrix<T> copied = new org.apache.commons.math.linear.BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int i = 0; i < this.blocks.length; i++) {
            java.lang.System.arraycopy(this.blocks[i], 0, copied.blocks[i], 0, this.blocks[i].length);
        }
        return copied;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> add(org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        try {
            return blockFieldMatrix.add((org.apache.commons.math.linear.BlockFieldMatrix) fieldMatrix);
        } catch (java.lang.ClassCastException e) {
            e = e;
            checkAdditionCompatible(fieldMatrix);
            org.apache.commons.math.linear.BlockFieldMatrix blockFieldMatrix2 = new org.apache.commons.math.linear.BlockFieldMatrix(getField(), blockFieldMatrix.rows, blockFieldMatrix.columns);
            int i = 0;
            int i2 = 0;
            while (i2 < blockFieldMatrix2.blockRows) {
                int i3 = 0;
                while (i3 < blockFieldMatrix2.blockColumns) {
                    org.apache.commons.math.FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix2.blocks)[i];
                    T[] tArr = blockFieldMatrix.blocks[i];
                    int i4 = i2 * 36;
                    int iMin = org.apache.commons.math.util.FastMath.min(i4 + 36, blockFieldMatrix.rows);
                    int i5 = i3 * 36;
                    int iMin2 = org.apache.commons.math.util.FastMath.min(i5 + 36, blockFieldMatrix.columns);
                    int i6 = 0;
                    for (int i7 = i4; i7 < iMin; i7++) {
                        int i8 = i5;
                        while (i8 < iMin2) {
                            fieldElementArr[i6] = (org.apache.commons.math.FieldElement) tArr[i6].add(fieldMatrix.getEntry(i7, i8));
                            i6++;
                            i8++;
                            e = e;
                        }
                    }
                    i++;
                    i3++;
                    blockFieldMatrix = this;
                }
                i2++;
                blockFieldMatrix = this;
            }
            return blockFieldMatrix2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public org.apache.commons.math.linear.BlockFieldMatrix<T> add(org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix) throws java.lang.IllegalArgumentException {
        checkAdditionCompatible(blockFieldMatrix);
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix2 = new org.apache.commons.math.linear.BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int i = 0; i < blockFieldMatrix2.blocks.length; i++) {
            org.apache.commons.math.FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix2.blocks)[i];
            T[] tArr = this.blocks[i];
            T[] tArr2 = blockFieldMatrix.blocks[i];
            for (int i2 = 0; i2 < fieldElementArr.length; i2++) {
                fieldElementArr[i2] = (org.apache.commons.math.FieldElement) tArr[i2].add(tArr2[i2]);
            }
        }
        return blockFieldMatrix2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> subtract(org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        try {
            return blockFieldMatrix.subtract((org.apache.commons.math.linear.BlockFieldMatrix) fieldMatrix);
        } catch (java.lang.ClassCastException e) {
            e = e;
            checkSubtractionCompatible(fieldMatrix);
            org.apache.commons.math.linear.BlockFieldMatrix blockFieldMatrix2 = new org.apache.commons.math.linear.BlockFieldMatrix(getField(), blockFieldMatrix.rows, blockFieldMatrix.columns);
            int i = 0;
            int i2 = 0;
            while (i2 < blockFieldMatrix2.blockRows) {
                int i3 = 0;
                while (i3 < blockFieldMatrix2.blockColumns) {
                    org.apache.commons.math.FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix2.blocks)[i];
                    T[] tArr = blockFieldMatrix.blocks[i];
                    int i4 = i2 * 36;
                    int iMin = org.apache.commons.math.util.FastMath.min(i4 + 36, blockFieldMatrix.rows);
                    int i5 = i3 * 36;
                    int iMin2 = org.apache.commons.math.util.FastMath.min(i5 + 36, blockFieldMatrix.columns);
                    int i6 = 0;
                    for (int i7 = i4; i7 < iMin; i7++) {
                        int i8 = i5;
                        while (i8 < iMin2) {
                            fieldElementArr[i6] = (org.apache.commons.math.FieldElement) tArr[i6].subtract(fieldMatrix.getEntry(i7, i8));
                            i6++;
                            i8++;
                            e = e;
                        }
                    }
                    i++;
                    i3++;
                    blockFieldMatrix = this;
                }
                i2++;
                blockFieldMatrix = this;
            }
            return blockFieldMatrix2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public org.apache.commons.math.linear.BlockFieldMatrix<T> subtract(org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix) throws java.lang.IllegalArgumentException {
        checkSubtractionCompatible(blockFieldMatrix);
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix2 = new org.apache.commons.math.linear.BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int i = 0; i < blockFieldMatrix2.blocks.length; i++) {
            org.apache.commons.math.FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix2.blocks)[i];
            T[] tArr = this.blocks[i];
            T[] tArr2 = blockFieldMatrix.blocks[i];
            for (int i2 = 0; i2 < fieldElementArr.length; i2++) {
                fieldElementArr[i2] = (org.apache.commons.math.FieldElement) tArr[i2].subtract(tArr2[i2]);
            }
        }
        return blockFieldMatrix2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> scalarAdd(T t) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.BlockFieldMatrix blockFieldMatrix = new org.apache.commons.math.linear.BlockFieldMatrix(getField(), this.rows, this.columns);
        for (int i = 0; i < blockFieldMatrix.blocks.length; i++) {
            org.apache.commons.math.FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix.blocks)[i];
            T[] tArr = this.blocks[i];
            for (int i2 = 0; i2 < fieldElementArr.length; i2++) {
                fieldElementArr[i2] = (org.apache.commons.math.FieldElement) tArr[i2].add(t);
            }
        }
        return blockFieldMatrix;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> scalarMultiply(T t) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.BlockFieldMatrix blockFieldMatrix = new org.apache.commons.math.linear.BlockFieldMatrix(getField(), this.rows, this.columns);
        for (int i = 0; i < blockFieldMatrix.blocks.length; i++) {
            org.apache.commons.math.FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix.blocks)[i];
            T[] tArr = this.blocks[i];
            for (int i2 = 0; i2 < fieldElementArr.length; i2++) {
                fieldElementArr[i2] = (org.apache.commons.math.FieldElement) tArr[i2].multiply(t);
            }
        }
        return blockFieldMatrix;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> multiply(org.apache.commons.math.linear.FieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        try {
            return blockFieldMatrix.multiply((org.apache.commons.math.linear.BlockFieldMatrix) m);
        } catch (java.lang.ClassCastException e) {
            cce = e;
            checkMultiplicationCompatible(m);
            org.apache.commons.math.linear.BlockFieldMatrix<T> out = new org.apache.commons.math.linear.BlockFieldMatrix<>(getField(), blockFieldMatrix.rows, m.getColumnDimension());
            T zero = getField().getZero();
            int blockIndex = 0;
            int iBlock = 0;
            while (iBlock < out.blockRows) {
                int pStart = iBlock * 36;
                int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 36, blockFieldMatrix.rows);
                int jBlock = 0;
                while (jBlock < out.blockColumns) {
                    int qStart = jBlock * 36;
                    int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 36, m.getColumnDimension());
                    org.apache.commons.math.FieldElement[] fieldElementArr = out.blocks[blockIndex];
                    int kBlock = 0;
                    while (kBlock < blockFieldMatrix.blockColumns) {
                        int kWidth = blockFieldMatrix.blockWidth(kBlock);
                        java.lang.ClassCastException cce = cce;
                        T[] tBlock = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + kBlock];
                        int rStart = kBlock * 36;
                        int k = 0;
                        int p = pStart;
                        while (p < pEnd) {
                            int lStart = (p - pStart) * kWidth;
                            int pStart2 = pStart;
                            int lEnd = lStart + kWidth;
                            int pEnd2 = pEnd;
                            int pEnd3 = qStart;
                            while (pEnd3 < qEnd) {
                                org.apache.commons.math.FieldElement fieldElement = zero;
                                int qStart2 = qStart;
                                int qStart3 = rStart;
                                int r = qEnd;
                                int qEnd2 = lStart;
                                while (qEnd2 < lEnd) {
                                    zero = (org.apache.commons.math.FieldElement) zero.add((org.apache.commons.math.FieldElement) tBlock[qEnd2].multiply(m.getEntry(qStart3, pEnd3)));
                                    qStart3++;
                                    qEnd2++;
                                    lEnd = lEnd;
                                    tBlock = tBlock;
                                }
                                fieldElementArr[k] = (org.apache.commons.math.FieldElement) fieldElementArr[k].add(zero);
                                k++;
                                pEnd3++;
                                qStart = qStart2;
                                qEnd = r;
                                zero = fieldElement;
                                tBlock = tBlock;
                            }
                            p++;
                            pStart = pStart2;
                            pEnd = pEnd2;
                        }
                        kBlock++;
                        blockFieldMatrix = this;
                        cce = cce;
                    }
                    blockIndex++;
                    jBlock++;
                    blockFieldMatrix = this;
                }
                iBlock++;
                blockFieldMatrix = this;
            }
            return out;
        }
    }

    public org.apache.commons.math.linear.BlockFieldMatrix<T> multiply(org.apache.commons.math.linear.BlockFieldMatrix<T> m) throws java.lang.IllegalArgumentException {
        int l;
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix2 = m;
        checkMultiplicationCompatible(m);
        org.apache.commons.math.linear.BlockFieldMatrix<T> out = new org.apache.commons.math.linear.BlockFieldMatrix<>(getField(), blockFieldMatrix.rows, blockFieldMatrix2.columns);
        T zero = getField().getZero();
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < out.blockRows) {
            int pStart = iBlock * 36;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 36, blockFieldMatrix.rows);
            int jBlock = 0;
            while (jBlock < out.blockColumns) {
                int jWidth = out.blockWidth(jBlock);
                int jWidth2 = jWidth + jWidth;
                int jWidth3 = jWidth2 + jWidth;
                int jWidth4 = jWidth3 + jWidth;
                org.apache.commons.math.FieldElement[] fieldElementArr = out.blocks[blockIndex];
                int kBlock = 0;
                while (kBlock < blockFieldMatrix.blockColumns) {
                    int kWidth = blockFieldMatrix.blockWidth(kBlock);
                    org.apache.commons.math.linear.BlockFieldMatrix<T> out2 = out;
                    T[][] tArr = blockFieldMatrix.blocks;
                    int blockIndex2 = blockIndex;
                    int blockIndex3 = blockFieldMatrix.blockColumns;
                    T[] tBlock = tArr[(blockIndex3 * iBlock) + kBlock];
                    T[] mBlock = blockFieldMatrix2.blocks[(blockFieldMatrix2.blockColumns * kBlock) + jBlock];
                    int k = 0;
                    int p = pStart;
                    while (p < pEnd) {
                        int lStart = (p - pStart) * kWidth;
                        int k2 = k;
                        int k3 = lStart + kWidth;
                        int pStart2 = pStart;
                        int pStart3 = 0;
                        while (pStart3 < jWidth) {
                            int n = pStart3;
                            org.apache.commons.math.FieldElement fieldElement = zero;
                            int pEnd2 = pEnd;
                            int pEnd3 = lStart;
                            while (true) {
                                l = kWidth;
                                int kWidth2 = k3 - 3;
                                if (pEnd3 >= kWidth2) {
                                    break;
                                }
                                int iBlock2 = iBlock;
                                org.apache.commons.math.FieldElement fieldElement2 = (org.apache.commons.math.FieldElement) zero.add((org.apache.commons.math.FieldElement) tBlock[pEnd3].multiply(mBlock[n]));
                                T t = tBlock[pEnd3 + 1];
                                T sum = mBlock[n + jWidth];
                                zero = (org.apache.commons.math.FieldElement) ((org.apache.commons.math.FieldElement) ((org.apache.commons.math.FieldElement) fieldElement2.add((org.apache.commons.math.FieldElement) t.multiply(sum))).add((org.apache.commons.math.FieldElement) tBlock[pEnd3 + 2].multiply(mBlock[n + jWidth2]))).add((org.apache.commons.math.FieldElement) tBlock[pEnd3 + 3].multiply(mBlock[n + jWidth3]));
                                pEnd3 += 4;
                                n += jWidth4;
                                kWidth = l;
                                iBlock = iBlock2;
                            }
                            int iBlock3 = iBlock;
                            while (pEnd3 < k3) {
                                zero = (org.apache.commons.math.FieldElement) zero.add((org.apache.commons.math.FieldElement) tBlock[pEnd3].multiply(mBlock[n]));
                                n += jWidth;
                                pEnd3++;
                            }
                            fieldElementArr[k2] = (org.apache.commons.math.FieldElement) fieldElementArr[k2].add(zero);
                            k2++;
                            pStart3++;
                            pEnd = pEnd2;
                            kWidth = l;
                            zero = fieldElement;
                            iBlock = iBlock3;
                        }
                        p++;
                        k = k2;
                        pStart = pStart2;
                    }
                    kBlock++;
                    blockFieldMatrix = this;
                    blockFieldMatrix2 = m;
                    out = out2;
                    blockIndex = blockIndex2;
                }
                blockIndex++;
                jBlock++;
                blockFieldMatrix = this;
                blockFieldMatrix2 = m;
            }
            iBlock++;
            blockFieldMatrix = this;
            blockFieldMatrix2 = m;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T[][] getData() {
        T[][] tArr = (T[][]) buildArray(getField(), getRowDimension(), getColumnDimension());
        int i = this.columns - ((this.blockColumns - 1) * 36);
        for (int i2 = 0; i2 < this.blockRows; i2++) {
            int i3 = i2 * 36;
            int iMin = org.apache.commons.math.util.FastMath.min(i3 + 36, this.rows);
            int i4 = 0;
            int i5 = 0;
            for (int i6 = i3; i6 < iMin; i6++) {
                T[] tArr2 = tArr[i6];
                int i7 = this.blockColumns * i2;
                int i8 = 0;
                int i9 = 0;
                while (i9 < this.blockColumns - 1) {
                    java.lang.System.arraycopy(this.blocks[i7], i4, tArr2, i8, 36);
                    i8 += 36;
                    i9++;
                    i7++;
                }
                java.lang.System.arraycopy(this.blocks[i7], i5, tArr2, i8, i);
                i4 += 36;
                i5 += i;
            }
        }
        return tArr;
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0026 */
    /* JADX WARN: Incorrect condition in loop: B:7:0x0032 */
    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public org.apache.commons.math.linear.FieldMatrix<T> getSubMatrix(int r32, int r33, int r34, int r35) throws org.apache.commons.math.linear.MatrixIndexException {
        /*
            Method dump skipped, instruction units count: 356
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.linear.BlockFieldMatrix.getSubMatrix(int, int, int, int):org.apache.commons.math.linear.FieldMatrix");
    }

    private void copyBlockPart(T[] srcBlock, int srcWidth, int srcStartRow, int srcEndRow, int srcStartColumn, int srcEndColumn, T[] dstBlock, int dstWidth, int dstStartRow, int dstStartColumn) {
        int length = srcEndColumn - srcStartColumn;
        int srcPos = (srcStartRow * srcWidth) + srcStartColumn;
        int dstPos = (dstStartRow * dstWidth) + dstStartColumn;
        for (int srcRow = srcStartRow; srcRow < srcEndRow; srcRow++) {
            java.lang.System.arraycopy(srcBlock, srcPos, dstBlock, dstPos, length);
            srcPos += srcWidth;
            dstPos += dstWidth;
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setSubMatrix(T[][] subMatrix, int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        T[][] tArr = subMatrix;
        int i = row;
        int refLength = tArr[0].length;
        if (refLength >= 1) {
            int endRow = (tArr.length + i) - 1;
            int endColumn = (column + refLength) - 1;
            blockFieldMatrix.checkSubMatrixIndex(i, endRow, column, endColumn);
            for (T[] subRow : tArr) {
                if (subRow.length != refLength) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(refLength), java.lang.Integer.valueOf(subRow.length));
                }
            }
            int blockStartRow = i / 36;
            int blockEndRow = (endRow + 36) / 36;
            int blockStartColumn = column / 36;
            int blockEndColumn = (endColumn + 36) / 36;
            int iBlock = blockStartRow;
            while (iBlock < blockEndRow) {
                int iHeight = blockFieldMatrix.blockHeight(iBlock);
                int firstRow = iBlock * 36;
                int iStart = org.apache.commons.math.util.FastMath.max(i, firstRow);
                int blockStartRow2 = blockStartRow;
                int iEnd = org.apache.commons.math.util.FastMath.min(endRow + 1, firstRow + iHeight);
                int jBlock = blockStartColumn;
                while (jBlock < blockEndColumn) {
                    int jWidth = blockFieldMatrix.blockWidth(jBlock);
                    int refLength2 = refLength;
                    int refLength3 = jBlock * 36;
                    int jStart = org.apache.commons.math.util.FastMath.max(column, refLength3);
                    int blockEndRow2 = blockEndRow;
                    int blockEndRow3 = endColumn + 1;
                    int endRow2 = endRow;
                    int jEnd = org.apache.commons.math.util.FastMath.min(blockEndRow3, refLength3 + jWidth);
                    int jLength = jEnd - jStart;
                    int endColumn2 = endColumn;
                    T[] block = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + jBlock];
                    int i2 = iStart;
                    while (i2 < iEnd) {
                        java.lang.System.arraycopy(tArr[i2 - i], jStart - column, block, ((i2 - firstRow) * jWidth) + (jStart - refLength3), jLength);
                        i2++;
                        tArr = subMatrix;
                        i = row;
                    }
                    jBlock++;
                    blockFieldMatrix = this;
                    tArr = subMatrix;
                    i = row;
                    refLength = refLength2;
                    blockEndRow = blockEndRow2;
                    endRow = endRow2;
                    endColumn = endColumn2;
                }
                iBlock++;
                blockFieldMatrix = this;
                tArr = subMatrix;
                i = row;
                blockStartRow = blockStartRow2;
            }
            return;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> getRowMatrix(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        checkRowIndex(row);
        org.apache.commons.math.linear.BlockFieldMatrix<T> out = new org.apache.commons.math.linear.BlockFieldMatrix<>(getField(), 1, this.columns);
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outBlockIndex = 0;
        int outIndex = 0;
        T[] outBlock = out.blocks[0];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int available = outBlock.length - outIndex;
            if (jWidth > available) {
                java.lang.System.arraycopy(block, iRow * jWidth, outBlock, outIndex, available);
                outBlockIndex++;
                outBlock = out.blocks[outBlockIndex];
                java.lang.System.arraycopy(block, iRow * jWidth, outBlock, 0, jWidth - available);
                outIndex = jWidth - available;
            } else {
                java.lang.System.arraycopy(block, iRow * jWidth, outBlock, outIndex, jWidth);
                outIndex += jWidth;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setRowMatrix(int row, org.apache.commons.math.linear.FieldMatrix<T> matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        try {
            setRowMatrix(row, (org.apache.commons.math.linear.BlockFieldMatrix) matrix);
        } catch (java.lang.ClassCastException e) {
            super.setRowMatrix(row, matrix);
        }
    }

    public void setRowMatrix(int row, org.apache.commons.math.linear.BlockFieldMatrix<T> matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() != 1 || matrix.getColumnDimension() != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(matrix.getRowDimension()), java.lang.Integer.valueOf(matrix.getColumnDimension()), 1, java.lang.Integer.valueOf(nCols));
        }
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int mBlockIndex = 0;
        int mIndex = 0;
        T[] mBlock = matrix.blocks[0];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int available = mBlock.length - mIndex;
            if (jWidth > available) {
                java.lang.System.arraycopy(mBlock, mIndex, block, iRow * jWidth, available);
                mBlockIndex++;
                mBlock = matrix.blocks[mBlockIndex];
                java.lang.System.arraycopy(mBlock, 0, block, iRow * jWidth, jWidth - available);
                mIndex = jWidth - available;
            } else {
                java.lang.System.arraycopy(mBlock, mIndex, block, iRow * jWidth, jWidth);
                mIndex += jWidth;
            }
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> getColumnMatrix(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        checkColumnIndex(column);
        org.apache.commons.math.linear.BlockFieldMatrix<T> out = new org.apache.commons.math.linear.BlockFieldMatrix<>(getField(), this.rows, 1);
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int outBlockIndex = 0;
        int outIndex = 0;
        T[] outBlock = out.blocks[0];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int i = 0;
            while (i < iHeight) {
                if (outIndex >= outBlock.length) {
                    outBlockIndex++;
                    outBlock = out.blocks[outBlockIndex];
                    outIndex = 0;
                }
                outBlock[outIndex] = block[(i * jWidth) + jColumn];
                i++;
                outIndex++;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setColumnMatrix(int column, org.apache.commons.math.linear.FieldMatrix<T> matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        try {
            setColumnMatrix(column, (org.apache.commons.math.linear.BlockFieldMatrix) matrix);
        } catch (java.lang.ClassCastException e) {
            super.setColumnMatrix(column, matrix);
        }
    }

    void setColumnMatrix(int column, org.apache.commons.math.linear.BlockFieldMatrix<T> matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() != nRows || matrix.getColumnDimension() != 1) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(matrix.getRowDimension()), java.lang.Integer.valueOf(matrix.getColumnDimension()), java.lang.Integer.valueOf(nRows), 1);
        }
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int mBlockIndex = 0;
        int mIndex = 0;
        T[] mBlock = matrix.blocks[0];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int i = 0;
            while (i < iHeight) {
                if (mIndex >= mBlock.length) {
                    mBlockIndex++;
                    mBlock = matrix.blocks[mBlockIndex];
                    mIndex = 0;
                }
                block[(i * jWidth) + jColumn] = mBlock[mIndex];
                i++;
                mIndex++;
            }
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldVector<T> getRowVector(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        checkRowIndex(row);
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(getField(), this.columns);
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            java.lang.System.arraycopy(block, iRow * jWidth, fieldElementArrBuildArray, outIndex, jWidth);
            outIndex += jWidth;
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray, false);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setRowVector(int row, org.apache.commons.math.linear.FieldVector<T> vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        try {
            setRow(row, ((org.apache.commons.math.linear.ArrayFieldVector) vector).getDataRef());
        } catch (java.lang.ClassCastException e) {
            super.setRowVector(row, vector);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldVector<T> getColumnVector(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        checkColumnIndex(column);
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(getField(), this.rows);
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int i = 0;
            while (i < iHeight) {
                fieldElementArrBuildArray[outIndex] = block[(i * jWidth) + jColumn];
                i++;
                outIndex++;
            }
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray, false);
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setColumnVector(int column, org.apache.commons.math.linear.FieldVector<T> vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        try {
            setColumn(column, ((org.apache.commons.math.linear.ArrayFieldVector) vector).getDataRef());
        } catch (java.lang.ClassCastException e) {
            super.setColumnVector(column, vector);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T[] getRow(int i) throws org.apache.commons.math.linear.MatrixIndexException {
        checkRowIndex(i);
        T[] tArr = (T[]) buildArray(getField(), this.columns);
        int i2 = i / 36;
        int i3 = i - (i2 * 36);
        int i4 = 0;
        for (int i5 = 0; i5 < this.blockColumns; i5++) {
            int iBlockWidth = blockWidth(i5);
            java.lang.System.arraycopy(this.blocks[(this.blockColumns * i2) + i5], i3 * iBlockWidth, tArr, i4, iBlockWidth);
            i4 += iBlockWidth;
        }
        return tArr;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setRow(int row, T[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, 1, java.lang.Integer.valueOf(array.length), 1, java.lang.Integer.valueOf(nCols));
        }
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            java.lang.System.arraycopy(array, outIndex, block, iRow * jWidth, jWidth);
            outIndex += jWidth;
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T[] getColumn(int i) throws org.apache.commons.math.linear.MatrixIndexException {
        checkColumnIndex(i);
        T[] tArr = (T[]) buildArray(getField(), this.rows);
        int i2 = i / 36;
        int i3 = i - (i2 * 36);
        int iBlockWidth = blockWidth(i2);
        int i4 = 0;
        for (int i5 = 0; i5 < this.blockRows; i5++) {
            int iBlockHeight = blockHeight(i5);
            T[] tArr2 = this.blocks[(this.blockColumns * i5) + i2];
            int i6 = 0;
            while (i6 < iBlockHeight) {
                tArr[i4] = tArr2[(i6 * iBlockWidth) + i3];
                i6++;
                i4++;
            }
        }
        return tArr;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setColumn(int column, T[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(array.length), 1, java.lang.Integer.valueOf(nRows), 1);
        }
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int i = 0;
            while (i < iHeight) {
                block[(i * jWidth) + jColumn] = array[outIndex];
                i++;
                outIndex++;
            }
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T getEntry(int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            int iBlock = row / 36;
            int jBlock = column / 36;
            int k = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
            return this.blocks[(this.blockColumns * iBlock) + jBlock][k];
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void setEntry(int row, int column, T value) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            int iBlock = row / 36;
            int jBlock = column / 36;
            int k = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
            this.blocks[(this.blockColumns * iBlock) + jBlock][k] = value;
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void addToEntry(int row, int column, T increment) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            int iBlock = row / 36;
            int jBlock = column / 36;
            int k = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
            org.apache.commons.math.FieldElement[] fieldElementArr = this.blocks[(this.blockColumns * iBlock) + jBlock];
            fieldElementArr[k] = (org.apache.commons.math.FieldElement) fieldElementArr[k].add(increment);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public void multiplyEntry(int row, int column, T factor) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            int iBlock = row / 36;
            int jBlock = column / 36;
            int k = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
            org.apache.commons.math.FieldElement[] fieldElementArr = this.blocks[(this.blockColumns * iBlock) + jBlock];
            fieldElementArr[k] = (org.apache.commons.math.FieldElement) fieldElementArr[k].multiply(factor);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public org.apache.commons.math.linear.FieldMatrix<T> transpose() {
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        org.apache.commons.math.linear.BlockFieldMatrix<T> out = new org.apache.commons.math.linear.BlockFieldMatrix<>(getField(), nCols, nRows);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < blockFieldMatrix.blockColumns) {
            int jBlock = 0;
            while (jBlock < blockFieldMatrix.blockRows) {
                T[] outBlock = out.blocks[blockIndex];
                T[] tBlock = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * jBlock) + iBlock];
                int pStart = iBlock * 36;
                int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 36, blockFieldMatrix.columns);
                int qStart = jBlock * 36;
                int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 36, blockFieldMatrix.rows);
                int k = 0;
                for (int p = pStart; p < pEnd; p++) {
                    int lInc = pEnd - pStart;
                    int l = p - pStart;
                    for (int q = qStart; q < qEnd; q++) {
                        outBlock[k] = tBlock[l];
                        k++;
                        l += lInc;
                    }
                }
                blockIndex++;
                jBlock++;
                blockFieldMatrix = this;
            }
            iBlock++;
            blockFieldMatrix = this;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getRowDimension() {
        return this.rows;
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getColumnDimension() {
        return this.columns;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T[] operate(T[] tArr) throws java.lang.IllegalArgumentException {
        if (tArr.length == this.columns) {
            T[] tArr2 = (T[]) buildArray(getField(), this.rows);
            T zero = getField().getZero();
            for (int i = 0; i < this.blockRows; i++) {
                int i2 = i * 36;
                int iMin = org.apache.commons.math.util.FastMath.min(i2 + 36, this.rows);
                for (int i3 = 0; i3 < this.blockColumns; i3++) {
                    T[] tArr3 = this.blocks[(this.blockColumns * i) + i3];
                    int i4 = i3 * 36;
                    int iMin2 = org.apache.commons.math.util.FastMath.min(i4 + 36, this.columns);
                    int i5 = 0;
                    int i6 = i2;
                    while (i6 < iMin) {
                        org.apache.commons.math.FieldElement fieldElement = zero;
                        int i7 = i4;
                        while (i7 < iMin2 - 3) {
                            fieldElement = (org.apache.commons.math.FieldElement) ((org.apache.commons.math.FieldElement) ((org.apache.commons.math.FieldElement) ((org.apache.commons.math.FieldElement) fieldElement.add((org.apache.commons.math.FieldElement) tArr3[i5].multiply(tArr[i7]))).add((org.apache.commons.math.FieldElement) tArr3[i5 + 1].multiply(tArr[i7 + 1]))).add((org.apache.commons.math.FieldElement) tArr3[i5 + 2].multiply(tArr[i7 + 2]))).add((org.apache.commons.math.FieldElement) tArr3[i5 + 3].multiply(tArr[i7 + 3]));
                            i5 += 4;
                            i7 += 4;
                            zero = zero;
                            i2 = i2;
                        }
                        T t = zero;
                        int i8 = i2;
                        while (i7 < iMin2) {
                            fieldElement = (org.apache.commons.math.FieldElement) fieldElement.add((org.apache.commons.math.FieldElement) tArr3[i5].multiply(tArr[i7]));
                            i7++;
                            i5++;
                        }
                        tArr2[i6] = (org.apache.commons.math.FieldElement) tArr2[i6].add(fieldElement);
                        i6++;
                        zero = t;
                        i2 = i8;
                    }
                }
            }
            return tArr2;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(tArr.length), java.lang.Integer.valueOf(this.columns));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T[] preMultiply(T[] tArr) throws java.lang.IllegalArgumentException {
        int i;
        if (tArr.length == this.rows) {
            T[] tArr2 = (T[]) buildArray(getField(), this.columns);
            T zero = getField().getZero();
            for (int i2 = 0; i2 < this.blockColumns; i2++) {
                int iBlockWidth = blockWidth(i2);
                int i3 = iBlockWidth + iBlockWidth;
                int i4 = i3 + iBlockWidth;
                int i5 = i4 + iBlockWidth;
                int i6 = i2 * 36;
                int iMin = org.apache.commons.math.util.FastMath.min(i6 + 36, this.columns);
                for (int i7 = 0; i7 < this.blockRows; i7++) {
                    T[] tArr3 = this.blocks[(this.blockColumns * i7) + i2];
                    int i8 = i7 * 36;
                    int iMin2 = org.apache.commons.math.util.FastMath.min(i8 + 36, this.rows);
                    int i9 = i6;
                    while (i9 < iMin) {
                        int i10 = i9 - i6;
                        T t = zero;
                        org.apache.commons.math.FieldElement fieldElement = t;
                        int i11 = i6;
                        int i12 = i8;
                        while (true) {
                            i = iMin;
                            if (i12 >= iMin2 - 3) {
                                break;
                            }
                            fieldElement = (org.apache.commons.math.FieldElement) ((org.apache.commons.math.FieldElement) ((org.apache.commons.math.FieldElement) ((org.apache.commons.math.FieldElement) fieldElement.add((org.apache.commons.math.FieldElement) tArr3[i10].multiply(tArr[i12]))).add((org.apache.commons.math.FieldElement) tArr3[i10 + iBlockWidth].multiply(tArr[i12 + 1]))).add((org.apache.commons.math.FieldElement) tArr3[i10 + i3].multiply(tArr[i12 + 2]))).add((org.apache.commons.math.FieldElement) tArr3[i10 + i4].multiply(tArr[i12 + 3]));
                            i10 += i5;
                            i12 += 4;
                            iMin = i;
                            i8 = i8;
                        }
                        int i13 = i8;
                        while (i12 < iMin2) {
                            fieldElement = (org.apache.commons.math.FieldElement) fieldElement.add((org.apache.commons.math.FieldElement) tArr3[i10].multiply(tArr[i12]));
                            i10 += iBlockWidth;
                            i12++;
                        }
                        tArr2[i9] = (org.apache.commons.math.FieldElement) tArr2[i9].add(fieldElement);
                        i9++;
                        zero = t;
                        i6 = i11;
                        iMin = i;
                        i8 = i13;
                    }
                }
            }
            return tArr2;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(tArr.length), java.lang.Integer.valueOf(this.rows));
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        fieldMatrixChangingVisitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int i = 0; i < this.blockRows; i++) {
            int i2 = i * 36;
            int iMin = org.apache.commons.math.util.FastMath.min(i2 + 36, this.rows);
            for (int i3 = i2; i3 < iMin; i3++) {
                for (int i4 = 0; i4 < this.blockColumns; i4++) {
                    int iBlockWidth = blockWidth(i4);
                    int i5 = i4 * 36;
                    int iMin2 = org.apache.commons.math.util.FastMath.min(i5 + 36, this.columns);
                    java.lang.Object[] objArr = this.blocks[(this.blockColumns * i) + i4];
                    int i6 = (i3 - i2) * iBlockWidth;
                    for (int i7 = i5; i7 < iMin2; i7++) {
                        objArr[i6] = fieldMatrixChangingVisitor.visit(i3, i7, objArr[i6]);
                        i6++;
                    }
                }
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        fieldMatrixPreservingVisitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int i = 0; i < this.blockRows; i++) {
            int i2 = i * 36;
            int iMin = org.apache.commons.math.util.FastMath.min(i2 + 36, this.rows);
            for (int i3 = i2; i3 < iMin; i3++) {
                for (int i4 = 0; i4 < this.blockColumns; i4++) {
                    int iBlockWidth = blockWidth(i4);
                    int i5 = i4 * 36;
                    int iMin2 = org.apache.commons.math.util.FastMath.min(i5 + 36, this.columns);
                    T[] tArr = this.blocks[(this.blockColumns * i) + i4];
                    int i6 = (i3 - i2) * iBlockWidth;
                    for (int i7 = i5; i7 < iMin2; i7++) {
                        fieldMatrixPreservingVisitor.visit(i3, i7, tArr[i6]);
                        i6++;
                    }
                }
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        blockFieldMatrix.checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixChangingVisitor.start(blockFieldMatrix.rows, blockFieldMatrix.columns, i, i2, i3, i4);
        int i5 = i / 36;
        while (i5 < (i2 / 36) + 1) {
            int i6 = i5 * 36;
            int iMax = org.apache.commons.math.util.FastMath.max(i, i6);
            int iMin = org.apache.commons.math.util.FastMath.min((i5 + 1) * 36, i2 + 1);
            int i7 = iMax;
            while (i7 < iMin) {
                int i8 = i3 / 36;
                while (i8 < (i4 / 36) + 1) {
                    int iBlockWidth = blockFieldMatrix.blockWidth(i8);
                    int i9 = i8 * 36;
                    int iMax2 = org.apache.commons.math.util.FastMath.max(i3, i9);
                    int iMin2 = org.apache.commons.math.util.FastMath.min((i8 + 1) * 36, i4 + 1);
                    int i10 = iMax;
                    java.lang.Object[] objArr = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * i5) + i8];
                    int i11 = (((i7 - i6) * iBlockWidth) + iMax2) - i9;
                    int i12 = iMax2;
                    while (i12 < iMin2) {
                        objArr[i11] = fieldMatrixChangingVisitor.visit(i7, i12, objArr[i11]);
                        i11++;
                        i12++;
                        i6 = i6;
                        iMin = iMin;
                    }
                    i8++;
                    blockFieldMatrix = this;
                    iMax = i10;
                    iMin = iMin;
                }
                i7++;
                blockFieldMatrix = this;
                iMin = iMin;
            }
            i5++;
            blockFieldMatrix = this;
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        blockFieldMatrix.checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixPreservingVisitor.start(blockFieldMatrix.rows, blockFieldMatrix.columns, i, i2, i3, i4);
        int i5 = i / 36;
        while (i5 < (i2 / 36) + 1) {
            int i6 = i5 * 36;
            int iMax = org.apache.commons.math.util.FastMath.max(i, i6);
            int iMin = org.apache.commons.math.util.FastMath.min((i5 + 1) * 36, i2 + 1);
            int i7 = iMax;
            while (i7 < iMin) {
                int i8 = i3 / 36;
                while (i8 < (i4 / 36) + 1) {
                    int iBlockWidth = blockFieldMatrix.blockWidth(i8);
                    int i9 = i8 * 36;
                    int iMax2 = org.apache.commons.math.util.FastMath.max(i3, i9);
                    int iMin2 = org.apache.commons.math.util.FastMath.min((i8 + 1) * 36, i4 + 1);
                    int i10 = iMax;
                    T[] tArr = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * i5) + i8];
                    int i11 = (((i7 - i6) * iBlockWidth) + iMax2) - i9;
                    int i12 = iMax2;
                    while (i12 < iMin2) {
                        fieldMatrixPreservingVisitor.visit(i7, i12, tArr[i11]);
                        i11++;
                        i12++;
                        i6 = i6;
                        tArr = tArr;
                    }
                    i8++;
                    blockFieldMatrix = this;
                    iMax = i10;
                }
                i7++;
                blockFieldMatrix = this;
                iMax = iMax;
            }
            i5++;
            blockFieldMatrix = this;
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        fieldMatrixChangingVisitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int i = 0;
        for (int i2 = 0; i2 < this.blockRows; i2++) {
            int i3 = i2 * 36;
            int iMin = org.apache.commons.math.util.FastMath.min(i3 + 36, this.rows);
            for (int i4 = 0; i4 < this.blockColumns; i4++) {
                int i5 = i4 * 36;
                int iMin2 = org.apache.commons.math.util.FastMath.min(i5 + 36, this.columns);
                java.lang.Object[] objArr = this.blocks[i];
                int i6 = 0;
                for (int i7 = i3; i7 < iMin; i7++) {
                    for (int i8 = i5; i8 < iMin2; i8++) {
                        objArr[i6] = fieldMatrixChangingVisitor.visit(i7, i8, objArr[i6]);
                        i6++;
                    }
                }
                i++;
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        fieldMatrixPreservingVisitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int i = 0;
        for (int i2 = 0; i2 < this.blockRows; i2++) {
            int i3 = i2 * 36;
            int iMin = org.apache.commons.math.util.FastMath.min(i3 + 36, this.rows);
            for (int i4 = 0; i4 < this.blockColumns; i4++) {
                int i5 = i4 * 36;
                int iMin2 = org.apache.commons.math.util.FastMath.min(i5 + 36, this.columns);
                T[] tArr = this.blocks[i];
                int i6 = 0;
                for (int i7 = i3; i7 < iMin; i7++) {
                    for (int i8 = i5; i8 < iMin2; i8++) {
                        fieldMatrixPreservingVisitor.visit(i7, i8, tArr[i6]);
                        i6++;
                    }
                }
                i++;
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        blockFieldMatrix.checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixChangingVisitor.start(blockFieldMatrix.rows, blockFieldMatrix.columns, i, i2, i3, i4);
        int i5 = i / 36;
        while (i5 < (i2 / 36) + 1) {
            int i6 = i5 * 36;
            int iMax = org.apache.commons.math.util.FastMath.max(i, i6);
            int iMin = org.apache.commons.math.util.FastMath.min((i5 + 1) * 36, i2 + 1);
            int i7 = i3 / 36;
            while (i7 < (i4 / 36) + 1) {
                int iBlockWidth = blockFieldMatrix.blockWidth(i7);
                int i8 = i7 * 36;
                int iMax2 = org.apache.commons.math.util.FastMath.max(i3, i8);
                int iMin2 = org.apache.commons.math.util.FastMath.min((i7 + 1) * 36, i4 + 1);
                java.lang.Object[] objArr = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * i5) + i7];
                int i9 = iMax;
                while (i9 < iMin) {
                    int i10 = (((i9 - i6) * iBlockWidth) + iMax2) - i8;
                    int i11 = iMax2;
                    while (i11 < iMin2) {
                        objArr[i10] = fieldMatrixChangingVisitor.visit(i9, i11, objArr[i10]);
                        i10++;
                        i11++;
                        i6 = i6;
                        iMax = iMax;
                    }
                    i9++;
                    iMax = iMax;
                }
                i7++;
                blockFieldMatrix = this;
                iMax = iMax;
            }
            i5++;
            blockFieldMatrix = this;
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractFieldMatrix, org.apache.commons.math.linear.FieldMatrix
    public T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockFieldMatrix<T> blockFieldMatrix = this;
        blockFieldMatrix.checkSubMatrixIndex(i, i2, i3, i4);
        fieldMatrixPreservingVisitor.start(blockFieldMatrix.rows, blockFieldMatrix.columns, i, i2, i3, i4);
        int i5 = i / 36;
        while (i5 < (i2 / 36) + 1) {
            int i6 = i5 * 36;
            int iMax = org.apache.commons.math.util.FastMath.max(i, i6);
            int iMin = org.apache.commons.math.util.FastMath.min((i5 + 1) * 36, i2 + 1);
            int i7 = i3 / 36;
            while (i7 < (i4 / 36) + 1) {
                int iBlockWidth = blockFieldMatrix.blockWidth(i7);
                int i8 = i7 * 36;
                int iMax2 = org.apache.commons.math.util.FastMath.max(i3, i8);
                int iMin2 = org.apache.commons.math.util.FastMath.min((i7 + 1) * 36, i4 + 1);
                T[] tArr = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * i5) + i7];
                int i9 = iMax;
                while (i9 < iMin) {
                    int i10 = (((i9 - i6) * iBlockWidth) + iMax2) - i8;
                    int i11 = iMax2;
                    while (i11 < iMin2) {
                        fieldMatrixPreservingVisitor.visit(i9, i11, tArr[i10]);
                        i10++;
                        i11++;
                        i6 = i6;
                        iMax = iMax;
                    }
                    i9++;
                    iMax = iMax;
                }
                i7++;
                blockFieldMatrix = this;
                iMax = iMax;
            }
            i5++;
            blockFieldMatrix = this;
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    private int blockHeight(int blockRow) {
        if (blockRow == this.blockRows - 1) {
            return this.rows - (blockRow * 36);
        }
        return 36;
    }

    private int blockWidth(int blockColumn) {
        if (blockColumn == this.blockColumns - 1) {
            return this.columns - (blockColumn * 36);
        }
        return 36;
    }
}
