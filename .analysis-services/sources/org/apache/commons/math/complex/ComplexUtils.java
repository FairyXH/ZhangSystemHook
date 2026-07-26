package org.apache.commons.math.complex;

/* JADX INFO: loaded from: classes4.dex */
public class ComplexUtils {
    private ComplexUtils() {
    }

    public static org.apache.commons.math.complex.Complex polar2Complex(double r, double theta) {
        if (r < 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NEGATIVE_COMPLEX_MODULE, java.lang.Double.valueOf(r));
        }
        return new org.apache.commons.math.complex.Complex(org.apache.commons.math.util.FastMath.cos(theta) * r, org.apache.commons.math.util.FastMath.sin(theta) * r);
    }
}
