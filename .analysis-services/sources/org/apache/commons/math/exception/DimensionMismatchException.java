package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class DimensionMismatchException extends org.apache.commons.math.exception.MathIllegalNumberException {
    private static final long serialVersionUID = -8415396756375798143L;
    private final int dimension;

    public DimensionMismatchException(int wrong, int expected) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(wrong), java.lang.Integer.valueOf(expected));
        this.dimension = expected;
    }

    public int getDimension() {
        return this.dimension;
    }
}
