package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class DefaultFieldMatrixPreservingVisitor<T extends org.apache.commons.math.FieldElement<T>> implements org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> {
    private final T zero;

    public DefaultFieldMatrixPreservingVisitor(T zero) {
        this.zero = zero;
    }

    @Override // org.apache.commons.math.linear.FieldMatrixPreservingVisitor
    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
    }

    @Override // org.apache.commons.math.linear.FieldMatrixPreservingVisitor
    public void visit(int row, int column, T value) throws org.apache.commons.math.linear.MatrixVisitorException {
    }

    @Override // org.apache.commons.math.linear.FieldMatrixPreservingVisitor
    public T end() {
        return this.zero;
    }
}
