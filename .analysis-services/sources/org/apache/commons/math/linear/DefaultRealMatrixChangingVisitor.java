package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class DefaultRealMatrixChangingVisitor implements org.apache.commons.math.linear.RealMatrixChangingVisitor {
    @Override // org.apache.commons.math.linear.RealMatrixChangingVisitor
    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
    }

    @Override // org.apache.commons.math.linear.RealMatrixChangingVisitor
    public double visit(int row, int column, double value) throws org.apache.commons.math.linear.MatrixVisitorException {
        return value;
    }

    @Override // org.apache.commons.math.linear.RealMatrixChangingVisitor
    public double end() {
        return 0.0d;
    }
}
