package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public class DuplicateSampleAbscissaException extends org.apache.commons.math.MathException {
    private static final long serialVersionUID = -2271007547170169872L;

    public DuplicateSampleAbscissaException(double abscissa, int i1, int i2) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.DUPLICATED_ABSCISSA, java.lang.Double.valueOf(abscissa), java.lang.Integer.valueOf(i1), java.lang.Integer.valueOf(i2));
    }

    public double getDuplicateAbscissa() {
        return ((java.lang.Double) getArguments()[0]).doubleValue();
    }
}
