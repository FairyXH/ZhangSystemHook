package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractDistribution implements org.apache.commons.math.distribution.Distribution, java.io.Serializable {
    private static final long serialVersionUID = -38038050983108802L;

    protected AbstractDistribution() {
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x0, double x1) throws org.apache.commons.math.MathException {
        if (x0 > x1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, java.lang.Double.valueOf(x0), java.lang.Double.valueOf(x1));
        }
        return cumulativeProbability(x1) - cumulativeProbability(x0);
    }
}
