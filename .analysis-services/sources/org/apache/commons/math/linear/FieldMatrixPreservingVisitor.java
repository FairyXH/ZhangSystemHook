package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface FieldMatrixPreservingVisitor<T extends org.apache.commons.math.FieldElement<?>> {
    T end();

    void start(int i, int i2, int i3, int i4, int i5, int i6);

    void visit(int i, int i2, T t) throws org.apache.commons.math.linear.MatrixVisitorException;
}
