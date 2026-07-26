package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public class DimensionMismatchException extends org.apache.commons.math.MathException {
    private static final long serialVersionUID = -1316089546353786411L;
    private final int dimension1;
    private final int dimension2;

    public DimensionMismatchException(int dimension1, int dimension2) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(dimension1), java.lang.Integer.valueOf(dimension2));
        this.dimension1 = dimension1;
        this.dimension2 = dimension2;
    }

    public int getDimension1() {
        return this.dimension1;
    }

    public int getDimension2() {
        return this.dimension2;
    }
}
