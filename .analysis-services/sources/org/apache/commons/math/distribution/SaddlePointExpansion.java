package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
final class SaddlePointExpansion {
    private static final double HALF_LOG_2_PI = org.apache.commons.math.util.FastMath.log(6.283185307179586d) * 0.5d;
    private static final double[] EXACT_STIRLING_ERRORS = {0.0d, 0.15342640972002736d, 0.08106146679532726d, 0.05481412105191765d, 0.0413406959554093d, 0.03316287351993629d, 0.02767792568499834d, 0.023746163656297496d, 0.020790672103765093d, 0.018488450532673187d, 0.016644691189821193d, 0.015134973221917378d, 0.013876128823070748d, 0.012810465242920227d, 0.01189670994589177d, 0.011104559758206917d, 0.010411265261972096d, 0.009799416126158804d, 0.009255462182712733d, 0.008768700134139386d, 0.00833056343336287d, 0.00793411456431402d, 0.007573675487951841d, 0.007244554301320383d, 0.00694284010720953d, 0.006665247032707682d, 0.006408994188004207d, 0.006171712263039458d, 0.0059513701127588475d, 0.0057462165130101155d, 0.005554733551962801d};

    private SaddlePointExpansion() {
    }

    static double getStirlingError(double z) {
        if (z < 15.0d) {
            double z2 = 2.0d * z;
            if (org.apache.commons.math.util.FastMath.floor(z2) == z2) {
                double ret = EXACT_STIRLING_ERRORS[(int) z2];
                return ret;
            }
            double ret2 = ((org.apache.commons.math.special.Gamma.logGamma(1.0d + z) - ((0.5d + z) * org.apache.commons.math.util.FastMath.log(z))) + z) - HALF_LOG_2_PI;
            return ret2;
        }
        double z22 = z * z;
        double ret3 = (0.08333333333333333d - ((0.002777777777777778d - ((7.936507936507937E-4d - ((5.952380952380953E-4d - (8.417508417508417E-4d / z22)) / z22)) / z22)) / z22)) / z;
        return ret3;
    }

    static double getDeviancePart(double x, double mu) {
        if (org.apache.commons.math.util.FastMath.abs(x - mu) < (x + mu) * 0.1d) {
            double d = x - mu;
            double v = d / (x + mu);
            double s1 = v * d;
            double s = Double.NaN;
            double ej = 2.0d * x * v;
            double v2 = v * v;
            int j = 1;
            while (s1 != s) {
                s = s1;
                ej *= v2;
                s1 = s + (ej / ((double) ((j * 2) + 1)));
                j++;
            }
            return s1;
        }
        double ret = x / mu;
        return ((org.apache.commons.math.util.FastMath.log(ret) * x) + mu) - x;
    }

    static double logBinomialProbability(int x, int n, double p, double q) {
        if (x == 0) {
            if (p < 0.1d) {
                double ret = (-getDeviancePart(n, ((double) n) * q)) - (((double) n) * p);
                return ret;
            }
            double ret2 = n;
            return ret2 * org.apache.commons.math.util.FastMath.log(q);
        }
        if (x != n) {
            double ret3 = n;
            double f = ((((double) x) * 6.283185307179586d) * ((double) (n - x))) / ((double) n);
            return ((((getStirlingError(ret3) - getStirlingError(x)) - getStirlingError(n - x)) - getDeviancePart(x, ((double) n) * p)) - getDeviancePart(n - x, ((double) n) * q)) + (org.apache.commons.math.util.FastMath.log(f) * (-0.5d));
        }
        if (q < 0.1d) {
            double ret4 = (-getDeviancePart(n, ((double) n) * p)) - (((double) n) * q);
            return ret4;
        }
        double ret5 = n;
        return ret5 * org.apache.commons.math.util.FastMath.log(p);
    }
}
