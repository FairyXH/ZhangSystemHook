package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class BlockRealMatrix extends org.apache.commons.math.linear.AbstractRealMatrix implements java.io.Serializable {
    public static final int BLOCK_SIZE = 52;
    private static final long serialVersionUID = 4991895511313664478L;
    private final int blockColumns;
    private final int blockRows;
    private final double[][] blocks;
    private final int columns;
    private final int rows;

    public BlockRealMatrix(int rows, int columns) throws java.lang.IllegalArgumentException {
        super(rows, columns);
        this.rows = rows;
        this.columns = columns;
        this.blockRows = ((rows + 52) - 1) / 52;
        this.blockColumns = ((columns + 52) - 1) / 52;
        this.blocks = createBlocksLayout(rows, columns);
    }

    public BlockRealMatrix(double[][] rawData) throws java.lang.IllegalArgumentException {
        this(rawData.length, rawData[0].length, toBlocksLayout(rawData), false);
    }

    public BlockRealMatrix(int rows, int columns, double[][] blockData, boolean copyArray) throws java.lang.IllegalArgumentException {
        super(rows, columns);
        this.rows = rows;
        this.columns = columns;
        this.blockRows = ((rows + 52) - 1) / 52;
        this.blockColumns = ((columns + 52) - 1) / 52;
        if (copyArray) {
            this.blocks = new double[this.blockRows * this.blockColumns][];
        } else {
            this.blocks = blockData;
        }
        int index = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            int jBlock = 0;
            while (jBlock < this.blockColumns) {
                if (blockData[index].length != blockWidth(jBlock) * iHeight) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.WRONG_BLOCK_LENGTH, java.lang.Integer.valueOf(blockData[index].length), java.lang.Integer.valueOf(blockWidth(jBlock) * iHeight));
                }
                if (copyArray) {
                    this.blocks[index] = (double[]) blockData[index].clone();
                }
                jBlock++;
                index++;
            }
        }
    }

    public static double[][] toBlocksLayout(double[][] rawData) throws java.lang.IllegalArgumentException {
        int rows = rawData.length;
        int columns = rawData[0].length;
        int blockRows = ((rows + 52) - 1) / 52;
        int blockColumns = ((columns + 52) - 1) / 52;
        for (double[] dArr : rawData) {
            int length = dArr.length;
            if (length != columns) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(columns), java.lang.Integer.valueOf(length));
            }
        }
        int i = blockRows * blockColumns;
        double[][] blocks = new double[i][];
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, rows);
            int iHeight = pEnd - pStart;
            int jBlock = 0;
            while (jBlock < blockColumns) {
                int qStart = jBlock * 52;
                int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, columns);
                int jWidth = qEnd - qStart;
                double[] block = new double[iHeight * jWidth];
                blocks[blockIndex] = block;
                int rows2 = rows;
                int rows3 = 0;
                int index = columns;
                int columns2 = pStart;
                while (columns2 < pEnd) {
                    java.lang.System.arraycopy(rawData[columns2], qStart, block, rows3, jWidth);
                    rows3 += jWidth;
                    columns2++;
                    blockRows = blockRows;
                }
                blockIndex++;
                jBlock++;
                columns = index;
                rows = rows2;
            }
        }
        return blocks;
    }

    public static double[][] createBlocksLayout(int rows, int columns) {
        int blockRows = ((rows + 52) - 1) / 52;
        int blockColumns = ((columns + 52) - 1) / 52;
        double[][] blocks = new double[blockRows * blockColumns][];
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, rows);
            int iHeight = pEnd - pStart;
            for (int jBlock = 0; jBlock < blockColumns; jBlock++) {
                int qStart = jBlock * 52;
                int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, columns);
                int jWidth = qEnd - qStart;
                blocks[blockIndex] = new double[iHeight * jWidth];
                blockIndex++;
            }
        }
        return blocks;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.BlockRealMatrix createMatrix(int rowDimension, int columnDimension) throws java.lang.IllegalArgumentException {
        return new org.apache.commons.math.linear.BlockRealMatrix(rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.BlockRealMatrix copy() {
        org.apache.commons.math.linear.BlockRealMatrix copied = new org.apache.commons.math.linear.BlockRealMatrix(this.rows, this.columns);
        for (int i = 0; i < this.blocks.length; i++) {
            java.lang.System.arraycopy(this.blocks[i], 0, copied.blocks[i], 0, this.blocks[i].length);
        }
        return copied;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.BlockRealMatrix add(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return add((org.apache.commons.math.linear.BlockRealMatrix) m);
        } catch (java.lang.ClassCastException e) {
            org.apache.commons.math.linear.MatrixUtils.checkAdditionCompatible(this, m);
            org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(this.rows, this.columns);
            int blockIndex = 0;
            for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
                for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                    double[] outBlock = out.blocks[blockIndex];
                    double[] tBlock = this.blocks[blockIndex];
                    int pStart = iBlock * 52;
                    int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, this.rows);
                    int qStart = jBlock * 52;
                    int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, this.columns);
                    int k = 0;
                    for (int p = pStart; p < pEnd; p++) {
                        for (int q = qStart; q < qEnd; q++) {
                            outBlock[k] = tBlock[k] + m.getEntry(p, q);
                            k++;
                        }
                    }
                    blockIndex++;
                }
            }
            return out;
        }
    }

    public org.apache.commons.math.linear.BlockRealMatrix add(org.apache.commons.math.linear.BlockRealMatrix m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkAdditionCompatible(this, m);
        org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            double[] mBlock = m.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = tBlock[k] + mBlock[k];
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.BlockRealMatrix subtract(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        try {
            return subtract((org.apache.commons.math.linear.BlockRealMatrix) m);
        } catch (java.lang.ClassCastException e) {
            org.apache.commons.math.linear.MatrixUtils.checkSubtractionCompatible(this, m);
            org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(this.rows, this.columns);
            int blockIndex = 0;
            for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
                for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                    double[] outBlock = out.blocks[blockIndex];
                    double[] tBlock = this.blocks[blockIndex];
                    int pStart = iBlock * 52;
                    int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, this.rows);
                    int qStart = jBlock * 52;
                    int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, this.columns);
                    int k = 0;
                    for (int p = pStart; p < pEnd; p++) {
                        for (int q = qStart; q < qEnd; q++) {
                            outBlock[k] = tBlock[k] - m.getEntry(p, q);
                            k++;
                        }
                    }
                    blockIndex++;
                }
            }
            return out;
        }
    }

    public org.apache.commons.math.linear.BlockRealMatrix subtract(org.apache.commons.math.linear.BlockRealMatrix m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.MatrixUtils.checkSubtractionCompatible(this, m);
        org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            double[] mBlock = m.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = tBlock[k] - mBlock[k];
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.BlockRealMatrix scalarAdd(double d) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = tBlock[k] + d;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealMatrix scalarMultiply(double d) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = tBlock[k] * d;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.BlockRealMatrix multiply(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.BlockRealMatrix blockRealMatrix = this;
        try {
            return blockRealMatrix.multiply((org.apache.commons.math.linear.BlockRealMatrix) m);
        } catch (java.lang.ClassCastException e) {
            cce = e;
            org.apache.commons.math.linear.MatrixUtils.checkMultiplicationCompatible(this, m);
            org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(blockRealMatrix.rows, m.getColumnDimension());
            int blockIndex = 0;
            int iBlock = 0;
            while (iBlock < out.blockRows) {
                int pStart = iBlock * 52;
                int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, blockRealMatrix.rows);
                int jBlock = 0;
                while (jBlock < out.blockColumns) {
                    int qStart = jBlock * 52;
                    int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, m.getColumnDimension());
                    double[] outBlock = out.blocks[blockIndex];
                    int kBlock = 0;
                    while (kBlock < blockRealMatrix.blockColumns) {
                        int kWidth = blockRealMatrix.blockWidth(kBlock);
                        double[] tBlock = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + kBlock];
                        int rStart = kBlock * 52;
                        int k = 0;
                        java.lang.ClassCastException cce = cce;
                        int p = pStart;
                        while (p < pEnd) {
                            int lStart = (p - pStart) * kWidth;
                            int lEnd = lStart + kWidth;
                            int pStart2 = pStart;
                            int pStart3 = qStart;
                            while (pStart3 < qEnd) {
                                double sum = 0.0d;
                                int r = pEnd;
                                int pEnd2 = rStart;
                                int qStart2 = qStart;
                                for (int qStart3 = lStart; qStart3 < lEnd; qStart3++) {
                                    sum += tBlock[qStart3] * m.getEntry(pEnd2, pStart3);
                                    pEnd2++;
                                }
                                outBlock[k] = outBlock[k] + sum;
                                k++;
                                pStart3++;
                                pEnd = r;
                                qStart = qStart2;
                            }
                            p++;
                            pStart = pStart2;
                        }
                        kBlock++;
                        blockRealMatrix = this;
                        cce = cce;
                    }
                    blockIndex++;
                    jBlock++;
                    blockRealMatrix = this;
                }
                iBlock++;
                blockRealMatrix = this;
            }
            return out;
        }
    }

    public org.apache.commons.math.linear.BlockRealMatrix multiply(org.apache.commons.math.linear.BlockRealMatrix m) throws java.lang.IllegalArgumentException {
        int iBlock;
        org.apache.commons.math.linear.BlockRealMatrix blockRealMatrix = this;
        org.apache.commons.math.linear.BlockRealMatrix blockRealMatrix2 = m;
        org.apache.commons.math.linear.MatrixUtils.checkMultiplicationCompatible(this, m);
        org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(blockRealMatrix.rows, blockRealMatrix2.columns);
        int blockIndex = 0;
        int iBlock2 = 0;
        while (iBlock2 < out.blockRows) {
            int pStart = iBlock2 * 52;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, blockRealMatrix.rows);
            int jBlock = 0;
            while (jBlock < out.blockColumns) {
                int jWidth = out.blockWidth(jBlock);
                int jWidth2 = jWidth + jWidth;
                int jWidth3 = jWidth2 + jWidth;
                int jWidth4 = jWidth3 + jWidth;
                double[] outBlock = out.blocks[blockIndex];
                int kBlock = 0;
                while (kBlock < blockRealMatrix.blockColumns) {
                    int kWidth = blockRealMatrix.blockWidth(kBlock);
                    org.apache.commons.math.linear.BlockRealMatrix out2 = out;
                    double[] tBlock = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock2) + kBlock];
                    double[] mBlock = blockRealMatrix2.blocks[(blockRealMatrix2.blockColumns * kBlock) + jBlock];
                    int k = 0;
                    int p = pStart;
                    while (p < pEnd) {
                        int lStart = (p - pStart) * kWidth;
                        int pStart2 = pStart;
                        int pStart3 = lStart + kWidth;
                        int pEnd2 = pEnd;
                        int pEnd3 = 0;
                        while (pEnd3 < jWidth) {
                            double sum = 0.0d;
                            int n = pEnd3;
                            int l = kWidth;
                            int kWidth2 = lStart;
                            while (true) {
                                iBlock = iBlock2;
                                int iBlock3 = pStart3 - 3;
                                if (kWidth2 >= iBlock3) {
                                    break;
                                }
                                sum += (tBlock[kWidth2] * mBlock[n]) + (tBlock[kWidth2 + 1] * mBlock[n + jWidth]) + (tBlock[kWidth2 + 2] * mBlock[n + jWidth2]) + (tBlock[kWidth2 + 3] * mBlock[n + jWidth3]);
                                kWidth2 += 4;
                                n += jWidth4;
                                iBlock2 = iBlock;
                            }
                            while (kWidth2 < pStart3) {
                                int l2 = kWidth2 + 1;
                                sum += tBlock[kWidth2] * mBlock[n];
                                n += jWidth;
                                kWidth2 = l2;
                            }
                            outBlock[k] = outBlock[k] + sum;
                            k++;
                            pEnd3++;
                            kWidth = l;
                            iBlock2 = iBlock;
                        }
                        p++;
                        pStart = pStart2;
                        pEnd = pEnd2;
                    }
                    kBlock++;
                    blockRealMatrix = this;
                    blockRealMatrix2 = m;
                    out = out2;
                }
                blockIndex++;
                jBlock++;
                blockRealMatrix = this;
                blockRealMatrix2 = m;
            }
            iBlock2++;
            blockRealMatrix = this;
            blockRealMatrix2 = m;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double[][] getData() {
        double[][] data = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, getRowDimension(), getColumnDimension());
        int lastColumns = this.columns - ((this.blockColumns - 1) * 52);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, this.rows);
            int regularPos = 0;
            int lastPos = 0;
            for (int p = pStart; p < pEnd; p++) {
                double[] dataP = data[p];
                int blockIndex = this.blockColumns * iBlock;
                int dataPos = 0;
                int jBlock = 0;
                while (jBlock < this.blockColumns - 1) {
                    java.lang.System.arraycopy(this.blocks[blockIndex], regularPos, dataP, dataPos, 52);
                    dataPos += 52;
                    jBlock++;
                    blockIndex++;
                }
                java.lang.System.arraycopy(this.blocks[blockIndex], lastPos, dataP, dataPos, lastColumns);
                regularPos += 52;
                lastPos += lastColumns;
            }
        }
        return data;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double getNorm() {
        double[] colSums = new double[52];
        double maxColSum = 0.0d;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            java.util.Arrays.fill(colSums, 0, jWidth, 0.0d);
            for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
                int iHeight = blockHeight(iBlock);
                double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                for (int j = 0; j < jWidth; j++) {
                    double sum = 0.0d;
                    for (int i = 0; i < iHeight; i++) {
                        sum += org.apache.commons.math.util.FastMath.abs(block[(i * jWidth) + j]);
                    }
                    colSums[j] = colSums[j] + sum;
                }
            }
            for (int j2 = 0; j2 < jWidth; j2++) {
                maxColSum = org.apache.commons.math.util.FastMath.max(maxColSum, colSums[j2]);
            }
        }
        return maxColSum;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double getFrobeniusNorm() {
        double sum2 = 0.0d;
        for (int blockIndex = 0; blockIndex < this.blocks.length; blockIndex++) {
            for (double entry : this.blocks[blockIndex]) {
                sum2 += entry * entry;
            }
        }
        return org.apache.commons.math.util.FastMath.sqrt(sum2);
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0022 */
    /* JADX WARN: Incorrect condition in loop: B:7:0x002e */
    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public org.apache.commons.math.linear.BlockRealMatrix getSubMatrix(int r32, int r33, int r34, int r35) throws org.apache.commons.math.linear.MatrixIndexException {
        /*
            Method dump skipped, instruction units count: 352
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.linear.BlockRealMatrix.getSubMatrix(int, int, int, int):org.apache.commons.math.linear.BlockRealMatrix");
    }

    private void copyBlockPart(double[] srcBlock, int srcWidth, int srcStartRow, int srcEndRow, int srcStartColumn, int srcEndColumn, double[] dstBlock, int dstWidth, int dstStartRow, int dstStartColumn) {
        int length = srcEndColumn - srcStartColumn;
        int srcPos = (srcStartRow * srcWidth) + srcStartColumn;
        int dstPos = (dstStartRow * dstWidth) + dstStartColumn;
        for (int srcRow = srcStartRow; srcRow < srcEndRow; srcRow++) {
            java.lang.System.arraycopy(srcBlock, srcPos, dstBlock, dstPos, length);
            srcPos += srcWidth;
            dstPos += dstWidth;
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setSubMatrix(double[][] subMatrix, int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockRealMatrix blockRealMatrix = this;
        double[][] dArr = subMatrix;
        int i = row;
        int refLength = dArr[0].length;
        if (refLength >= 1) {
            int endRow = (dArr.length + i) - 1;
            int endColumn = (column + refLength) - 1;
            org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(blockRealMatrix, i, endRow, column, endColumn);
            for (double[] subRow : dArr) {
                if (subRow.length != refLength) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(refLength), java.lang.Integer.valueOf(subRow.length));
                }
            }
            int blockStartRow = i / 52;
            int blockEndRow = (endRow + 52) / 52;
            int blockStartColumn = column / 52;
            int blockEndColumn = (endColumn + 52) / 52;
            int iBlock = blockStartRow;
            while (iBlock < blockEndRow) {
                int iHeight = blockRealMatrix.blockHeight(iBlock);
                int firstRow = iBlock * 52;
                int iStart = org.apache.commons.math.util.FastMath.max(i, firstRow);
                int blockStartRow2 = blockStartRow;
                int iEnd = org.apache.commons.math.util.FastMath.min(endRow + 1, firstRow + iHeight);
                int jBlock = blockStartColumn;
                while (jBlock < blockEndColumn) {
                    int jWidth = blockRealMatrix.blockWidth(jBlock);
                    int refLength2 = refLength;
                    int refLength3 = jBlock * 52;
                    int jStart = org.apache.commons.math.util.FastMath.max(column, refLength3);
                    int blockEndRow2 = blockEndRow;
                    int blockEndRow3 = endColumn + 1;
                    int endRow2 = endRow;
                    int jEnd = org.apache.commons.math.util.FastMath.min(blockEndRow3, refLength3 + jWidth);
                    int jLength = jEnd - jStart;
                    int endColumn2 = endColumn;
                    double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                    int i2 = iStart;
                    while (i2 < iEnd) {
                        java.lang.System.arraycopy(dArr[i2 - i], jStart - column, block, ((i2 - firstRow) * jWidth) + (jStart - refLength3), jLength);
                        i2++;
                        dArr = subMatrix;
                        i = row;
                    }
                    jBlock++;
                    blockRealMatrix = this;
                    dArr = subMatrix;
                    i = row;
                    refLength = refLength2;
                    blockEndRow = blockEndRow2;
                    endRow = endRow2;
                    endColumn = endColumn2;
                }
                iBlock++;
                blockRealMatrix = this;
                dArr = subMatrix;
                i = row;
                blockStartRow = blockStartRow2;
            }
            return;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.AT_LEAST_ONE_COLUMN, new java.lang.Object[0]);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.BlockRealMatrix getRowMatrix(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(1, this.columns);
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outBlockIndex = 0;
        int outIndex = 0;
        double[] outBlock = out.blocks[0];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setRowMatrix(int row, org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        try {
            setRowMatrix(row, (org.apache.commons.math.linear.BlockRealMatrix) matrix);
        } catch (java.lang.ClassCastException e) {
            super.setRowMatrix(row, matrix);
        }
    }

    public void setRowMatrix(int row, org.apache.commons.math.linear.BlockRealMatrix matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() != 1 || matrix.getColumnDimension() != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(matrix.getRowDimension()), java.lang.Integer.valueOf(matrix.getColumnDimension()), 1, java.lang.Integer.valueOf(nCols));
        }
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int mBlockIndex = 0;
        int mIndex = 0;
        double[] mBlock = matrix.blocks[0];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.BlockRealMatrix getColumnMatrix(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(this.rows, 1);
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outBlockIndex = 0;
        int outIndex = 0;
        double[] outBlock = out.blocks[0];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setColumnMatrix(int column, org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        try {
            setColumnMatrix(column, (org.apache.commons.math.linear.BlockRealMatrix) matrix);
        } catch (java.lang.ClassCastException e) {
            super.setColumnMatrix(column, matrix);
        }
    }

    void setColumnMatrix(int column, org.apache.commons.math.linear.BlockRealMatrix matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() != nRows || matrix.getColumnDimension() != 1) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(matrix.getRowDimension()), java.lang.Integer.valueOf(matrix.getColumnDimension()), java.lang.Integer.valueOf(nRows), 1);
        }
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int mBlockIndex = 0;
        int mIndex = 0;
        double[] mBlock = matrix.blocks[0];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealVector getRowVector(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        double[] outData = new double[this.columns];
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            java.lang.System.arraycopy(block, iRow * jWidth, outData, outIndex, jWidth);
            outIndex += jWidth;
        }
        return new org.apache.commons.math.linear.ArrayRealVector(outData, false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setRowVector(int row, org.apache.commons.math.linear.RealVector vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        try {
            setRow(row, ((org.apache.commons.math.linear.ArrayRealVector) vector).getDataRef());
        } catch (java.lang.ClassCastException e) {
            super.setRowVector(row, vector);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.RealVector getColumnVector(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        double[] outData = new double[this.rows];
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int i = 0;
            while (i < iHeight) {
                outData[outIndex] = block[(i * jWidth) + jColumn];
                i++;
                outIndex++;
            }
        }
        return new org.apache.commons.math.linear.ArrayRealVector(outData, false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setColumnVector(int column, org.apache.commons.math.linear.RealVector vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        try {
            setColumn(column, ((org.apache.commons.math.linear.ArrayRealVector) vector).getDataRef());
        } catch (java.lang.ClassCastException e) {
            super.setColumnVector(column, vector);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double[] getRow(int row) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        double[] out = new double[this.columns];
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            java.lang.System.arraycopy(block, iRow * jWidth, out, outIndex, jWidth);
            outIndex += jWidth;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setRow(int row, double[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, 1, java.lang.Integer.valueOf(array.length), 1, java.lang.Integer.valueOf(nCols));
        }
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            java.lang.System.arraycopy(array, outIndex, block, iRow * jWidth, jWidth);
            outIndex += jWidth;
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double[] getColumn(int column) throws org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        double[] out = new double[this.rows];
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int i = 0;
            while (i < iHeight) {
                out[outIndex] = block[(i * jWidth) + jColumn];
                i++;
                outIndex++;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setColumn(int column, double[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
        org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(array.length), 1, java.lang.Integer.valueOf(nRows), 1);
        }
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int i = 0;
            while (i < iHeight) {
                block[(i * jWidth) + jColumn] = array[outIndex];
                i++;
                outIndex++;
            }
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double getEntry(int row, int column) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            int iBlock = row / 52;
            int jBlock = column / 52;
            int k = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
            return this.blocks[(this.blockColumns * iBlock) + jBlock][k];
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void setEntry(int row, int column, double value) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            int iBlock = row / 52;
            int jBlock = column / 52;
            int k = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
            this.blocks[(this.blockColumns * iBlock) + jBlock][k] = value;
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void addToEntry(int row, int column, double increment) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            int iBlock = row / 52;
            int jBlock = column / 52;
            int k = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
            double[] dArr = this.blocks[(this.blockColumns * iBlock) + jBlock];
            dArr[k] = dArr[k] + increment;
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public void multiplyEntry(int row, int column, double factor) throws org.apache.commons.math.linear.MatrixIndexException {
        try {
            int iBlock = row / 52;
            int jBlock = column / 52;
            int k = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
            double[] dArr = this.blocks[(this.blockColumns * iBlock) + jBlock];
            dArr[k] = dArr[k] * factor;
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.NO_SUCH_MATRIX_ENTRY, java.lang.Integer.valueOf(row), java.lang.Integer.valueOf(column), java.lang.Integer.valueOf(getRowDimension()), java.lang.Integer.valueOf(getColumnDimension()));
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public org.apache.commons.math.linear.BlockRealMatrix transpose() {
        org.apache.commons.math.linear.BlockRealMatrix blockRealMatrix = this;
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        org.apache.commons.math.linear.BlockRealMatrix out = new org.apache.commons.math.linear.BlockRealMatrix(nCols, nRows);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < blockRealMatrix.blockColumns) {
            int jBlock = 0;
            while (jBlock < blockRealMatrix.blockRows) {
                double[] outBlock = out.blocks[blockIndex];
                double[] tBlock = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * jBlock) + iBlock];
                int pStart = iBlock * 52;
                int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, blockRealMatrix.columns);
                int qStart = jBlock * 52;
                int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, blockRealMatrix.rows);
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
                blockRealMatrix = this;
            }
            iBlock++;
            blockRealMatrix = this;
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getRowDimension() {
        return this.rows;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.AnyMatrix
    public int getColumnDimension() {
        return this.columns;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double[] operate(double[] v) throws java.lang.IllegalArgumentException {
        if (v.length != this.columns) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.length), java.lang.Integer.valueOf(this.columns));
        }
        double[] out = new double[this.rows];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, this.rows);
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                int qStart = jBlock * 52;
                int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, this.columns);
                int k = 0;
                for (int p = pStart; p < pEnd; p++) {
                    double sum = 0.0d;
                    int q = qStart;
                    while (q < qEnd - 3) {
                        sum += (block[k] * v[q]) + (block[k + 1] * v[q + 1]) + (block[k + 2] * v[q + 2]) + (block[k + 3] * v[q + 3]);
                        k += 4;
                        q += 4;
                    }
                    while (q < qEnd) {
                        sum += block[k] * v[q];
                        q++;
                        k++;
                    }
                    out[p] = out[p] + sum;
                }
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double[] preMultiply(double[] v) throws java.lang.IllegalArgumentException {
        int qEnd;
        if (v.length != this.rows) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(v.length), java.lang.Integer.valueOf(this.rows));
        }
        double[] out = new double[this.columns];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            int jWidth2 = jWidth + jWidth;
            int jWidth3 = jWidth2 + jWidth;
            int jWidth4 = jWidth3 + jWidth;
            int qStart = jBlock * 52;
            int qEnd2 = org.apache.commons.math.util.FastMath.min(qStart + 52, this.columns);
            for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
                double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                int pStart = iBlock * 52;
                int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, this.rows);
                int q = qStart;
                while (q < qEnd2) {
                    int k = q - qStart;
                    double sum = 0.0d;
                    int qStart2 = qStart;
                    int qStart3 = pStart;
                    while (true) {
                        qEnd = qEnd2;
                        int qEnd3 = pEnd - 3;
                        if (qStart3 >= qEnd3) {
                            break;
                        }
                        sum += (block[k] * v[qStart3]) + (block[k + jWidth] * v[qStart3 + 1]) + (block[k + jWidth2] * v[qStart3 + 2]) + (block[k + jWidth3] * v[qStart3 + 3]);
                        k += jWidth4;
                        qStart3 += 4;
                        qEnd2 = qEnd;
                    }
                    while (qStart3 < pEnd) {
                        int p = qStart3 + 1;
                        sum += block[k] * v[qStart3];
                        k += jWidth;
                        qStart3 = p;
                    }
                    out[q] = out[q] + sum;
                    q++;
                    qStart = qStart2;
                    qEnd2 = qEnd;
                }
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, this.rows);
            for (int p = pStart; p < pEnd; p++) {
                for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int qStart = jBlock * 52;
                    int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, this.columns);
                    double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                    int k = (p - pStart) * jWidth;
                    for (int q = qStart; q < qEnd; q++) {
                        block[k] = visitor.visit(p, q, block[k]);
                        k++;
                    }
                }
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, this.rows);
            for (int p = pStart; p < pEnd; p++) {
                for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int qStart = jBlock * 52;
                    int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, this.columns);
                    double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                    int k = (p - pStart) * jWidth;
                    for (int q = qStart; q < qEnd; q++) {
                        visitor.visit(p, q, block[k]);
                        k++;
                    }
                }
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockRealMatrix blockRealMatrix = this;
        int i = startRow;
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(blockRealMatrix, i, endRow, startColumn, endColumn);
        visitor.start(blockRealMatrix.rows, blockRealMatrix.columns, startRow, endRow, startColumn, endColumn);
        int iBlock = i / 52;
        while (iBlock < (endRow / 52) + 1) {
            int p0 = iBlock * 52;
            int pStart = org.apache.commons.math.util.FastMath.max(i, p0);
            int pEnd = org.apache.commons.math.util.FastMath.min((iBlock + 1) * 52, endRow + 1);
            int p = pStart;
            while (p < pEnd) {
                int jBlock = startColumn / 52;
                while (jBlock < (endColumn / 52) + 1) {
                    int jWidth = blockRealMatrix.blockWidth(jBlock);
                    int q0 = jBlock * 52;
                    int qStart = org.apache.commons.math.util.FastMath.max(startColumn, q0);
                    int qEnd = org.apache.commons.math.util.FastMath.min((jBlock + 1) * 52, endColumn + 1);
                    int pStart2 = pStart;
                    double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int q = qStart;
                    while (q < qEnd) {
                        block[k] = visitor.visit(p, q, block[k]);
                        k++;
                        q++;
                        jWidth = jWidth;
                        p0 = p0;
                    }
                    jBlock++;
                    blockRealMatrix = this;
                    pStart = pStart2;
                    p0 = p0;
                }
                p++;
                blockRealMatrix = this;
                p0 = p0;
            }
            iBlock++;
            blockRealMatrix = this;
            i = startRow;
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInRowOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockRealMatrix blockRealMatrix = this;
        int i = startRow;
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(blockRealMatrix, i, endRow, startColumn, endColumn);
        visitor.start(blockRealMatrix.rows, blockRealMatrix.columns, startRow, endRow, startColumn, endColumn);
        int iBlock = i / 52;
        while (iBlock < (endRow / 52) + 1) {
            int p0 = iBlock * 52;
            int pStart = org.apache.commons.math.util.FastMath.max(i, p0);
            int pEnd = org.apache.commons.math.util.FastMath.min((iBlock + 1) * 52, endRow + 1);
            int p = pStart;
            while (p < pEnd) {
                int jBlock = startColumn / 52;
                while (jBlock < (endColumn / 52) + 1) {
                    int jWidth = blockRealMatrix.blockWidth(jBlock);
                    int q0 = jBlock * 52;
                    int qStart = org.apache.commons.math.util.FastMath.max(startColumn, q0);
                    int qEnd = org.apache.commons.math.util.FastMath.min((jBlock + 1) * 52, endColumn + 1);
                    int pStart2 = pStart;
                    double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int q = qStart;
                    while (q < qEnd) {
                        visitor.visit(p, q, block[k]);
                        k++;
                        q++;
                        jWidth = jWidth;
                        p0 = p0;
                    }
                    jBlock++;
                    blockRealMatrix = this;
                    pStart = pStart2;
                    p0 = p0;
                }
                p++;
                blockRealMatrix = this;
                p0 = p0;
            }
            iBlock++;
            blockRealMatrix = this;
            i = startRow;
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, this.rows);
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int qStart = jBlock * 52;
                int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, this.columns);
                double[] block = this.blocks[blockIndex];
                int k = 0;
                for (int p = pStart; p < pEnd; p++) {
                    for (int q = qStart; q < qEnd; q++) {
                        block[k] = visitor.visit(p, q, block[k]);
                        k++;
                    }
                }
                blockIndex++;
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = org.apache.commons.math.util.FastMath.min(pStart + 52, this.rows);
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int qStart = jBlock * 52;
                int qEnd = org.apache.commons.math.util.FastMath.min(qStart + 52, this.columns);
                double[] block = this.blocks[blockIndex];
                int k = 0;
                for (int p = pStart; p < pEnd; p++) {
                    for (int q = qStart; q < qEnd; q++) {
                        visitor.visit(p, q, block[k]);
                        k++;
                    }
                }
                blockIndex++;
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockRealMatrix blockRealMatrix = this;
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(blockRealMatrix, startRow, endRow, startColumn, endColumn);
        visitor.start(blockRealMatrix.rows, blockRealMatrix.columns, startRow, endRow, startColumn, endColumn);
        int iBlock = startRow / 52;
        while (iBlock < (endRow / 52) + 1) {
            int p0 = iBlock * 52;
            int pStart = org.apache.commons.math.util.FastMath.max(startRow, p0);
            int pEnd = org.apache.commons.math.util.FastMath.min((iBlock + 1) * 52, endRow + 1);
            int jBlock = startColumn / 52;
            while (jBlock < (endColumn / 52) + 1) {
                int jWidth = blockRealMatrix.blockWidth(jBlock);
                int q0 = jBlock * 52;
                int qStart = org.apache.commons.math.util.FastMath.max(startColumn, q0);
                int qEnd = org.apache.commons.math.util.FastMath.min((jBlock + 1) * 52, endColumn + 1);
                double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                int p = pStart;
                while (p < pEnd) {
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int q = qStart;
                    while (q < qEnd) {
                        block[k] = visitor.visit(p, q, block[k]);
                        k++;
                        q++;
                        p0 = p0;
                        pStart = pStart;
                        pEnd = pEnd;
                    }
                    p++;
                    pEnd = pEnd;
                }
                jBlock++;
                blockRealMatrix = this;
                pEnd = pEnd;
            }
            iBlock++;
            blockRealMatrix = this;
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math.linear.AbstractRealMatrix, org.apache.commons.math.linear.RealMatrix
    public double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException {
        org.apache.commons.math.linear.BlockRealMatrix blockRealMatrix = this;
        org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(blockRealMatrix, startRow, endRow, startColumn, endColumn);
        visitor.start(blockRealMatrix.rows, blockRealMatrix.columns, startRow, endRow, startColumn, endColumn);
        int iBlock = startRow / 52;
        while (iBlock < (endRow / 52) + 1) {
            int p0 = iBlock * 52;
            int pStart = org.apache.commons.math.util.FastMath.max(startRow, p0);
            int pEnd = org.apache.commons.math.util.FastMath.min((iBlock + 1) * 52, endRow + 1);
            int jBlock = startColumn / 52;
            while (jBlock < (endColumn / 52) + 1) {
                int jWidth = blockRealMatrix.blockWidth(jBlock);
                int q0 = jBlock * 52;
                int qStart = org.apache.commons.math.util.FastMath.max(startColumn, q0);
                int qEnd = org.apache.commons.math.util.FastMath.min((jBlock + 1) * 52, endColumn + 1);
                double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                int p = pStart;
                while (p < pEnd) {
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int q = qStart;
                    while (q < qEnd) {
                        visitor.visit(p, q, block[k]);
                        k++;
                        q++;
                        p0 = p0;
                        pStart = pStart;
                        pEnd = pEnd;
                    }
                    p++;
                    pEnd = pEnd;
                }
                jBlock++;
                blockRealMatrix = this;
                pEnd = pEnd;
            }
            iBlock++;
            blockRealMatrix = this;
        }
        return visitor.end();
    }

    private int blockHeight(int blockRow) {
        if (blockRow == this.blockRows - 1) {
            return this.rows - (blockRow * 52);
        }
        return 52;
    }

    private int blockWidth(int blockColumn) {
        if (blockColumn == this.blockColumns - 1) {
            return this.columns - (blockColumn * 52);
        }
        return 52;
    }
}
