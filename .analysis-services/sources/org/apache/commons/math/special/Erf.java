package org.apache.commons.math.special;

/* JADX INFO: loaded from: classes4.dex */
public class Erf {
    private Erf() {
    }

    public static double erf(double x) throws org.apache.commons.math.MathException {
        if (org.apache.commons.math.util.FastMath.abs(x) > 40.0d) {
            return x > 0.0d ? 1.0d : -1.0d;
        }
        double ret = org.apache.commons.math.special.Gamma.regularizedGammaP(0.5d, x * x, 1.0E-15d, 10000);
        if (x < 0.0d) {
            return -ret;
        }
        return ret;
    }

    public static double erfc(double x) throws org.apache.commons.math.MathException {
        if (org.apache.commons.math.util.FastMath.abs(x) > 40.0d) {
            return x > 0.0d ? 0.0d : 2.0d;
        }
        double ret = org.apache.commons.math.special.Gamma.regularizedGammaQ(0.5d, x * x, 1.0E-15d, 10000);
        return x < 0.0d ? 2.0d - ret : ret;
    }
}
