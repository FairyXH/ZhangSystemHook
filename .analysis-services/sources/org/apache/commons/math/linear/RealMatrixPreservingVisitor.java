package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface RealMatrixPreservingVisitor {
    double end();

    void start(int i, int i2, int i3, int i4, int i5, int i6);

    void visit(int i, int i2, double d) throws org.apache.commons.math.linear.MatrixVisitorException;
}
