package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class NonSquareMatrixException extends org.apache.commons.math.linear.InvalidMatrixException {
    private static final long serialVersionUID = 8996207526636673730L;

    public NonSquareMatrixException(int rows, int columns) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.NON_SQUARE_MATRIX, java.lang.Integer.valueOf(rows), java.lang.Integer.valueOf(columns));
    }
}
