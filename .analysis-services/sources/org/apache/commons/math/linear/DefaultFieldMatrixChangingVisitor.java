package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class DefaultFieldMatrixChangingVisitor<T extends org.apache.commons.math.FieldElement<T>> implements org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> {
    private final T zero;

    public DefaultFieldMatrixChangingVisitor(T zero) {
        this.zero = zero;
    }

    @Override // org.apache.commons.math.linear.FieldMatrixChangingVisitor
    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
    }

    @Override // org.apache.commons.math.linear.FieldMatrixChangingVisitor
    public T visit(int row, int column, T value) throws org.apache.commons.math.linear.MatrixVisitorException {
        return value;
    }

    @Override // org.apache.commons.math.linear.FieldMatrixChangingVisitor
    public T end() {
        return this.zero;
    }
}
