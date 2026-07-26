package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public class FractionConversionException extends org.apache.commons.math.ConvergenceException {
    private static final long serialVersionUID = -4661812640132576263L;

    public FractionConversionException(double value, int maxIterations) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.FAILED_FRACTION_CONVERSION, java.lang.Double.valueOf(value), java.lang.Integer.valueOf(maxIterations));
    }

    public FractionConversionException(double value, long p, long q) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION_CONVERSION_OVERFLOW, java.lang.Double.valueOf(value), java.lang.Long.valueOf(p), java.lang.Long.valueOf(q));
    }
}
