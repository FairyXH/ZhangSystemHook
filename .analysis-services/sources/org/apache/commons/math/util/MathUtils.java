package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public final class MathUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final double EPSILON = 1.1102230246251565E-16d;
    private static final long[] FACTORIALS = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    private static final int NAN_GAP = 4194304;
    private static final byte NB = -1;
    private static final short NS = -1;
    private static final byte PB = 1;
    private static final short PS = 1;
    public static final double SAFE_MIN = Double.MIN_NORMAL;
    private static final long SGN_MASK = Long.MIN_VALUE;
    private static final int SGN_MASK_FLOAT = Integer.MIN_VALUE;
    public static final double TWO_PI = 6.283185307179586d;
    private static final byte ZB = 0;
    private static final short ZS = 0;

    public enum OrderDirection {
        INCREASING,
        DECREASING
    }

    private MathUtils() {
    }

    public static int addAndCheck(int x, int y) {
        long s = ((long) x) + ((long) y);
        if (s < -2147483648L || s > 2147483647L) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.OVERFLOW_IN_ADDITION, java.lang.Integer.valueOf(x), java.lang.Integer.valueOf(y));
        }
        return (int) s;
    }

    public static long addAndCheck(long a, long b) {
        return addAndCheck(a, b, org.apache.commons.math.exception.util.LocalizedFormats.OVERFLOW_IN_ADDITION);
    }

    private static long addAndCheck(long a, long b, org.apache.commons.math.exception.util.Localizable pattern) {
        if (a > b) {
            long ret = addAndCheck(b, a, pattern);
            return ret;
        }
        if (a >= 0) {
            if (a <= Long.MAX_VALUE - b) {
                long ret2 = a + b;
                return ret2;
            }
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(pattern, java.lang.Long.valueOf(a), java.lang.Long.valueOf(b));
        }
        if (b >= 0) {
            long ret3 = a + b;
            return ret3;
        }
        if (Long.MIN_VALUE - b <= a) {
            long ret4 = a + b;
            return ret4;
        }
        throw org.apache.commons.math.MathRuntimeException.createArithmeticException(pattern, java.lang.Long.valueOf(a), java.lang.Long.valueOf(b));
    }

    public static long binomialCoefficient(int n, int k) {
        checkBinomial(n, k);
        if (n == k || k == 0) {
            return 1L;
        }
        if (k == 1 || k == n - 1) {
            return n;
        }
        if (k > n / 2) {
            return binomialCoefficient(n, n - k);
        }
        long result = 1;
        if (n <= 61) {
            int i = (n - k) + 1;
            for (int j = 1; j <= k; j++) {
                result = (((long) i) * result) / ((long) j);
                i++;
            }
        } else if (n <= 66) {
            int i2 = (n - k) + 1;
            for (int j2 = 1; j2 <= k; j2++) {
                long d = gcd(i2, j2);
                result = (result / (((long) j2) / d)) * (((long) i2) / d);
                i2++;
            }
        } else {
            int i3 = (n - k) + 1;
            for (int j3 = 1; j3 <= k; j3++) {
                long d2 = gcd(i3, j3);
                result = mulAndCheck(result / (((long) j3) / d2), ((long) i3) / d2);
                i3++;
            }
        }
        return result;
    }

    public static double binomialCoefficientDouble(int n, int k) {
        checkBinomial(n, k);
        if (n == k || k == 0) {
            return 1.0d;
        }
        if (k == 1 || k == n - 1) {
            double result = n;
            return result;
        }
        if (k > n / 2) {
            return binomialCoefficientDouble(n, n - k);
        }
        if (n < 67) {
            return binomialCoefficient(n, k);
        }
        double result2 = 1.0d;
        for (int i = 1; i <= k; i++) {
            result2 *= ((double) ((n - k) + i)) / ((double) i);
        }
        return org.apache.commons.math.util.FastMath.floor(0.5d + result2);
    }

    public static double binomialCoefficientLog(int n, int k) {
        checkBinomial(n, k);
        if (n == k || k == 0) {
            return 0.0d;
        }
        if (k == 1 || k == n - 1) {
            return org.apache.commons.math.util.FastMath.log(n);
        }
        if (n < 67) {
            return org.apache.commons.math.util.FastMath.log(binomialCoefficient(n, k));
        }
        if (n < 1030) {
            return org.apache.commons.math.util.FastMath.log(binomialCoefficientDouble(n, k));
        }
        if (k > n / 2) {
            return binomialCoefficientLog(n, n - k);
        }
        double logSum = 0.0d;
        for (int i = (n - k) + 1; i <= n; i++) {
            logSum += org.apache.commons.math.util.FastMath.log(i);
        }
        for (int i2 = 2; i2 <= k; i2++) {
            logSum -= org.apache.commons.math.util.FastMath.log(i2);
        }
        return logSum;
    }

    private static void checkBinomial(int n, int k) throws java.lang.IllegalArgumentException {
        if (n < k) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.BINOMIAL_INVALID_PARAMETERS_ORDER, java.lang.Integer.valueOf(n), java.lang.Integer.valueOf(k));
        }
        if (n < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.BINOMIAL_NEGATIVE_PARAMETER, java.lang.Integer.valueOf(n));
        }
    }

    public static int compareTo(double x, double y, double eps) {
        if (equals(x, y, eps)) {
            return 0;
        }
        if (x < y) {
            return -1;
        }
        return 1;
    }

    public static double cosh(double x) {
        return (org.apache.commons.math.util.FastMath.exp(x) + org.apache.commons.math.util.FastMath.exp(-x)) / 2.0d;
    }

    @java.lang.Deprecated
    public static boolean equals(float x, float y) {
        return (java.lang.Float.isNaN(x) && java.lang.Float.isNaN(y)) || x == y;
    }

    public static boolean equalsIncludingNaN(float x, float y) {
        return (java.lang.Float.isNaN(x) && java.lang.Float.isNaN(y)) || equals(x, y, 1);
    }

    public static boolean equals(float x, float y, float eps) {
        return equals(x, y, 1) || org.apache.commons.math.util.FastMath.abs(y - x) <= eps;
    }

    public static boolean equalsIncludingNaN(float x, float y, float eps) {
        return equalsIncludingNaN(x, y) || org.apache.commons.math.util.FastMath.abs(y - x) <= eps;
    }

    public static boolean equals(float x, float y, int maxUlps) {
        int xInt = java.lang.Float.floatToIntBits(x);
        int yInt = java.lang.Float.floatToIntBits(y);
        if (xInt < 0) {
            xInt = Integer.MIN_VALUE - xInt;
        }
        if (yInt < 0) {
            yInt = Integer.MIN_VALUE - yInt;
        }
        boolean isEqual = org.apache.commons.math.util.FastMath.abs(xInt - yInt) <= maxUlps;
        return (!isEqual || java.lang.Float.isNaN(x) || java.lang.Float.isNaN(y)) ? false : true;
    }

    public static boolean equalsIncludingNaN(float x, float y, int maxUlps) {
        return (java.lang.Float.isNaN(x) && java.lang.Float.isNaN(y)) || equals(x, y, maxUlps);
    }

    @java.lang.Deprecated
    public static boolean equals(float[] x, float[] y) {
        if (x == null || y == null) {
            return !((y == null) ^ (x == null));
        }
        if (x.length != y.length) {
            return false;
        }
        for (int i = 0; i < x.length; i++) {
            if (!equals(x[i], y[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalsIncludingNaN(float[] x, float[] y) {
        if (x == null || y == null) {
            return !((y == null) ^ (x == null));
        }
        if (x.length != y.length) {
            return false;
        }
        for (int i = 0; i < x.length; i++) {
            if (!equalsIncludingNaN(x[i], y[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(double x, double y) {
        return (java.lang.Double.isNaN(x) && java.lang.Double.isNaN(y)) || x == y;
    }

    public static boolean equalsIncludingNaN(double x, double y) {
        return (java.lang.Double.isNaN(x) && java.lang.Double.isNaN(y)) || equals(x, y, 1);
    }

    public static boolean equals(double x, double y, double eps) {
        return equals(x, y) || org.apache.commons.math.util.FastMath.abs(y - x) <= eps;
    }

    public static boolean equalsIncludingNaN(double x, double y, double eps) {
        return equalsIncludingNaN(x, y) || org.apache.commons.math.util.FastMath.abs(y - x) <= eps;
    }

    public static boolean equals(double x, double y, int maxUlps) {
        long xInt = java.lang.Double.doubleToLongBits(x);
        long yInt = java.lang.Double.doubleToLongBits(y);
        if (xInt < 0) {
            xInt = Long.MIN_VALUE - xInt;
        }
        if (yInt < 0) {
            yInt = Long.MIN_VALUE - yInt;
        }
        return org.apache.commons.math.util.FastMath.abs(xInt - yInt) <= ((long) maxUlps);
    }

    public static boolean equalsIncludingNaN(double x, double y, int maxUlps) {
        return (java.lang.Double.isNaN(x) && java.lang.Double.isNaN(y)) || equals(x, y, maxUlps);
    }

    public static boolean equals(double[] x, double[] y) {
        if (x == null || y == null) {
            return !((y == null) ^ (x == null));
        }
        if (x.length != y.length) {
            return false;
        }
        for (int i = 0; i < x.length; i++) {
            if (!equals(x[i], y[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalsIncludingNaN(double[] x, double[] y) {
        if (x == null || y == null) {
            return !((y == null) ^ (x == null));
        }
        if (x.length != y.length) {
            return false;
        }
        for (int i = 0; i < x.length; i++) {
            if (!equalsIncludingNaN(x[i], y[i])) {
                return false;
            }
        }
        return true;
    }

    public static long factorial(int n) {
        if (n < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, java.lang.Integer.valueOf(n));
        }
        if (n > 20) {
            throw new java.lang.ArithmeticException("factorial value is too large to fit in a long");
        }
        return FACTORIALS[n];
    }

    public static double factorialDouble(int n) {
        if (n < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, java.lang.Integer.valueOf(n));
        }
        if (n < 21) {
            return factorial(n);
        }
        return org.apache.commons.math.util.FastMath.floor(org.apache.commons.math.util.FastMath.exp(factorialLog(n)) + 0.5d);
    }

    public static double factorialLog(int n) {
        if (n < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, java.lang.Integer.valueOf(n));
        }
        if (n < 21) {
            return org.apache.commons.math.util.FastMath.log(factorial(n));
        }
        double logSum = 0.0d;
        for (int i = 2; i <= n; i++) {
            logSum += org.apache.commons.math.util.FastMath.log(i);
        }
        return logSum;
    }

    public static int gcd(int p, int q) {
        int u = p;
        int v = q;
        if (u == 0 || v == 0) {
            if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE) {
                throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.GCD_OVERFLOW_32_BITS, java.lang.Integer.valueOf(p), java.lang.Integer.valueOf(q));
            }
            return org.apache.commons.math.util.FastMath.abs(u) + org.apache.commons.math.util.FastMath.abs(v);
        }
        if (u > 0) {
            u = -u;
        }
        if (v > 0) {
            v = -v;
        }
        int k = 0;
        while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
            u /= 2;
            v /= 2;
            k++;
        }
        if (k == 31) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.GCD_OVERFLOW_32_BITS, java.lang.Integer.valueOf(p), java.lang.Integer.valueOf(q));
        }
        int t = (u & 1) == 1 ? v : -(u / 2);
        while (true) {
            if ((t & 1) == 0) {
                t /= 2;
            } else {
                if (t > 0) {
                    u = -t;
                } else {
                    v = t;
                }
                t = (v - u) / 2;
                if (t == 0) {
                    return (-u) * (1 << k);
                }
            }
        }
    }

    public static long gcd(long p, long q) {
        long u = p;
        long v = q;
        if (u == 0 || v == 0) {
            if (u == Long.MIN_VALUE || v == Long.MIN_VALUE) {
                throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.GCD_OVERFLOW_64_BITS, java.lang.Long.valueOf(p), java.lang.Long.valueOf(q));
            }
            return org.apache.commons.math.util.FastMath.abs(u) + org.apache.commons.math.util.FastMath.abs(v);
        }
        if (u > 0) {
            u = -u;
        }
        if (v > 0) {
            v = -v;
        }
        int k = 0;
        while ((u & 1) == 0 && (v & 1) == 0 && k < 63) {
            u /= 2;
            v /= 2;
            k++;
        }
        if (k == 63) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.GCD_OVERFLOW_64_BITS, java.lang.Long.valueOf(p), java.lang.Long.valueOf(q));
        }
        long t = (u & 1) == 1 ? v : -(u / 2);
        while (true) {
            if ((t & 1) == 0) {
                t /= 2;
            } else {
                if (t > 0) {
                    u = -t;
                } else {
                    v = t;
                }
                t = (v - u) / 2;
                if (t == 0) {
                    return (-u) * (1 << k);
                }
            }
        }
    }

    public static int hash(double value) {
        return new java.lang.Double(value).hashCode();
    }

    public static int hash(double[] value) {
        return java.util.Arrays.hashCode(value);
    }

    public static byte indicator(byte x) {
        return x >= 0 ? (byte) 1 : (byte) -1;
    }

    public static double indicator(double x) {
        if (java.lang.Double.isNaN(x)) {
            return Double.NaN;
        }
        return x >= 0.0d ? 1.0d : -1.0d;
    }

    public static float indicator(float x) {
        if (java.lang.Float.isNaN(x)) {
            return Float.NaN;
        }
        return x >= 0.0f ? 1.0f : -1.0f;
    }

    public static int indicator(int x) {
        return x >= 0 ? 1 : -1;
    }

    public static long indicator(long x) {
        return x >= 0 ? 1L : -1L;
    }

    public static short indicator(short x) {
        if (x >= 0) {
            return PS;
        }
        return (short) -1;
    }

    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        int lcm = org.apache.commons.math.util.FastMath.abs(mulAndCheck(a / gcd(a, b), b));
        if (lcm == Integer.MIN_VALUE) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.LCM_OVERFLOW_32_BITS, java.lang.Integer.valueOf(a), java.lang.Integer.valueOf(b));
        }
        return lcm;
    }

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) {
            return 0L;
        }
        long lcm = org.apache.commons.math.util.FastMath.abs(mulAndCheck(a / gcd(a, b), b));
        if (lcm == Long.MIN_VALUE) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.LCM_OVERFLOW_64_BITS, java.lang.Long.valueOf(a), java.lang.Long.valueOf(b));
        }
        return lcm;
    }

    public static double log(double base, double x) {
        return org.apache.commons.math.util.FastMath.log(x) / org.apache.commons.math.util.FastMath.log(base);
    }

    public static int mulAndCheck(int x, int y) {
        long m = ((long) x) * ((long) y);
        if (m < -2147483648L || m > 2147483647L) {
            throw new java.lang.ArithmeticException("overflow: mul");
        }
        return (int) m;
    }

    public static long mulAndCheck(long a, long b) {
        if (a > b) {
            long ret = mulAndCheck(b, a);
            return ret;
        }
        if (a >= 0) {
            if (a <= 0) {
                return 0L;
            }
            if (a <= Long.MAX_VALUE / b) {
                long ret2 = a * b;
                return ret2;
            }
            throw new java.lang.ArithmeticException("overflow: multiply");
        }
        if (b < 0) {
            if (a >= Long.MAX_VALUE / b) {
                long ret3 = a * b;
                return ret3;
            }
            throw new java.lang.ArithmeticException("overflow: multiply");
        }
        if (b <= 0) {
            return 0L;
        }
        if (Long.MIN_VALUE / b <= a) {
            long ret4 = a * b;
            return ret4;
        }
        throw new java.lang.ArithmeticException("overflow: multiply");
    }

    @java.lang.Deprecated
    public static double nextAfter(double d, double direction) {
        if (java.lang.Double.isNaN(d) || java.lang.Double.isInfinite(d)) {
            return d;
        }
        if (d == 0.0d) {
            return direction < 0.0d ? -4.9E-324d : Double.MIN_VALUE;
        }
        long bits = java.lang.Double.doubleToLongBits(d);
        long sign = Long.MIN_VALUE & bits;
        long exponent = 9218868437227405312L & bits;
        long mantissa = bits & 4503599627370495L;
        if ((direction - d) * d >= 0.0d) {
            if (mantissa == 4503599627370495L) {
                return java.lang.Double.longBitsToDouble(sign | (4503599627370496L + exponent));
            }
            return java.lang.Double.longBitsToDouble(sign | exponent | (1 + mantissa));
        }
        if (mantissa == 0) {
            return java.lang.Double.longBitsToDouble((exponent - 4503599627370496L) | sign | 4503599627370495L);
        }
        return java.lang.Double.longBitsToDouble(sign | exponent | (mantissa - 1));
    }

    @java.lang.Deprecated
    public static double scalb(double d, int scaleFactor) {
        return org.apache.commons.math.util.FastMath.scalb(d, scaleFactor);
    }

    public static double normalizeAngle(double a, double center) {
        return a - (org.apache.commons.math.util.FastMath.floor(((3.141592653589793d + a) - center) / 6.283185307179586d) * 6.283185307179586d);
    }

    public static double[] normalizeArray(double[] values, double normalizedSum) throws java.lang.ArithmeticException, java.lang.IllegalArgumentException {
        if (java.lang.Double.isInfinite(normalizedSum)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NORMALIZE_INFINITE, new java.lang.Object[0]);
        }
        if (java.lang.Double.isNaN(normalizedSum)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NORMALIZE_NAN, new java.lang.Object[0]);
        }
        double sum = 0.0d;
        int len = values.length;
        double[] out = new double[len];
        for (int i = 0; i < len; i++) {
            if (java.lang.Double.isInfinite(values[i])) {
                throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.INFINITE_ARRAY_ELEMENT, java.lang.Double.valueOf(values[i]), java.lang.Integer.valueOf(i));
            }
            if (!java.lang.Double.isNaN(values[i])) {
                sum += values[i];
            }
        }
        if (sum == 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ARRAY_SUMS_TO_ZERO, new java.lang.Object[0]);
        }
        for (int i2 = 0; i2 < len; i2++) {
            if (java.lang.Double.isNaN(values[i2])) {
                out[i2] = Double.NaN;
            } else {
                out[i2] = (values[i2] * normalizedSum) / sum;
            }
        }
        return out;
    }

    public static double round(double x, int scale) {
        return round(x, scale, 4);
    }

    public static double round(double x, int scale, int roundingMethod) {
        try {
            return new java.math.BigDecimal(java.lang.Double.toString(x)).setScale(scale, roundingMethod).doubleValue();
        } catch (java.lang.NumberFormatException e) {
            if (java.lang.Double.isInfinite(x)) {
                return x;
            }
            return Double.NaN;
        }
    }

    public static float round(float x, int scale) {
        return round(x, scale, 4);
    }

    public static float round(float x, int scale, int roundingMethod) {
        float sign = indicator(x);
        float factor = ((float) org.apache.commons.math.util.FastMath.pow(10.0d, scale)) * sign;
        return ((float) roundUnscaled(x * factor, sign, roundingMethod)) / factor;
    }

    private static double roundUnscaled(double unscaled, double sign, int roundingMethod) {
        switch (roundingMethod) {
            case 0:
                return org.apache.commons.math.util.FastMath.ceil(nextAfter(unscaled, Double.POSITIVE_INFINITY));
            case 1:
                return org.apache.commons.math.util.FastMath.floor(nextAfter(unscaled, Double.NEGATIVE_INFINITY));
            case 2:
                if (sign == -1.0d) {
                    return org.apache.commons.math.util.FastMath.floor(nextAfter(unscaled, Double.NEGATIVE_INFINITY));
                }
                return org.apache.commons.math.util.FastMath.ceil(nextAfter(unscaled, Double.POSITIVE_INFINITY));
            case 3:
                if (sign == -1.0d) {
                    return org.apache.commons.math.util.FastMath.ceil(nextAfter(unscaled, Double.POSITIVE_INFINITY));
                }
                return org.apache.commons.math.util.FastMath.floor(nextAfter(unscaled, Double.NEGATIVE_INFINITY));
            case 4:
                double unscaled2 = nextAfter(unscaled, Double.POSITIVE_INFINITY);
                double fraction = unscaled2 - org.apache.commons.math.util.FastMath.floor(unscaled2);
                if (fraction >= 0.5d) {
                    return org.apache.commons.math.util.FastMath.ceil(unscaled2);
                }
                return org.apache.commons.math.util.FastMath.floor(unscaled2);
            case 5:
                double unscaled3 = nextAfter(unscaled, Double.NEGATIVE_INFINITY);
                double fraction2 = unscaled3 - org.apache.commons.math.util.FastMath.floor(unscaled3);
                if (fraction2 > 0.5d) {
                    return org.apache.commons.math.util.FastMath.ceil(unscaled3);
                }
                return org.apache.commons.math.util.FastMath.floor(unscaled3);
            case 6:
                double fraction3 = unscaled - org.apache.commons.math.util.FastMath.floor(unscaled);
                if (fraction3 > 0.5d) {
                    return org.apache.commons.math.util.FastMath.ceil(unscaled);
                }
                if (fraction3 < 0.5d) {
                    return org.apache.commons.math.util.FastMath.floor(unscaled);
                }
                if (org.apache.commons.math.util.FastMath.floor(unscaled) / 2.0d == org.apache.commons.math.util.FastMath.floor(java.lang.Math.floor(unscaled) / 2.0d)) {
                    return org.apache.commons.math.util.FastMath.floor(unscaled);
                }
                return org.apache.commons.math.util.FastMath.ceil(unscaled);
            case 7:
                if (unscaled == org.apache.commons.math.util.FastMath.floor(unscaled)) {
                    return unscaled;
                }
                throw new java.lang.ArithmeticException("Inexact result from rounding");
            default:
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INVALID_ROUNDING_METHOD, java.lang.Integer.valueOf(roundingMethod), "ROUND_CEILING", 2, "ROUND_DOWN", 1, "ROUND_FLOOR", 3, "ROUND_HALF_DOWN", 5, "ROUND_HALF_EVEN", 6, "ROUND_HALF_UP", 4, "ROUND_UNNECESSARY", 7, "ROUND_UP", 0);
        }
    }

    public static byte sign(byte x) {
        if (x == 0) {
            return (byte) 0;
        }
        return x > 0 ? (byte) 1 : (byte) -1;
    }

    public static double sign(double x) {
        if (java.lang.Double.isNaN(x)) {
            return Double.NaN;
        }
        if (x == 0.0d) {
            return 0.0d;
        }
        return x > 0.0d ? 1.0d : -1.0d;
    }

    public static float sign(float x) {
        if (java.lang.Float.isNaN(x)) {
            return Float.NaN;
        }
        if (x == 0.0f) {
            return 0.0f;
        }
        return x > 0.0f ? 1.0f : -1.0f;
    }

    public static int sign(int x) {
        if (x == 0) {
            return 0;
        }
        return x > 0 ? 1 : -1;
    }

    public static long sign(long x) {
        if (x == 0) {
            return 0L;
        }
        return x > 0 ? 1L : -1L;
    }

    public static short sign(short x) {
        if (x == 0) {
            return (short) 0;
        }
        if (x > 0) {
            return PS;
        }
        return (short) -1;
    }

    public static double sinh(double x) {
        return (org.apache.commons.math.util.FastMath.exp(x) - org.apache.commons.math.util.FastMath.exp(-x)) / 2.0d;
    }

    public static int subAndCheck(int x, int y) {
        long s = ((long) x) - ((long) y);
        if (s < -2147483648L || s > 2147483647L) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.OVERFLOW_IN_SUBTRACTION, java.lang.Integer.valueOf(x), java.lang.Integer.valueOf(y));
        }
        return (int) s;
    }

    public static long subAndCheck(long a, long b) {
        if (b != Long.MIN_VALUE) {
            long ret = addAndCheck(a, -b, org.apache.commons.math.exception.util.LocalizedFormats.OVERFLOW_IN_ADDITION);
            return ret;
        }
        if (a < 0) {
            long ret2 = a - b;
            return ret2;
        }
        throw new java.lang.ArithmeticException("overflow: subtract");
    }

    public static int pow(int k, int e) throws java.lang.IllegalArgumentException {
        if (e < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POWER_NEGATIVE_PARAMETERS, java.lang.Integer.valueOf(k), java.lang.Integer.valueOf(e));
        }
        int result = 1;
        int k2p = k;
        while (e != 0) {
            if ((e & 1) != 0) {
                result *= k2p;
            }
            k2p *= k2p;
            e >>= 1;
        }
        return result;
    }

    public static int pow(int k, long e) throws java.lang.IllegalArgumentException {
        if (e < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POWER_NEGATIVE_PARAMETERS, java.lang.Integer.valueOf(k), java.lang.Long.valueOf(e));
        }
        int result = 1;
        int k2p = k;
        while (e != 0) {
            if ((1 & e) != 0) {
                result *= k2p;
            }
            k2p *= k2p;
            e >>= 1;
        }
        return result;
    }

    public static long pow(long k, int e) throws java.lang.IllegalArgumentException {
        if (e < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POWER_NEGATIVE_PARAMETERS, java.lang.Long.valueOf(k), java.lang.Integer.valueOf(e));
        }
        long result = 1;
        long k2p = k;
        while (e != 0) {
            if ((e & 1) != 0) {
                result *= k2p;
            }
            k2p *= k2p;
            e >>= 1;
        }
        return result;
    }

    public static long pow(long k, long e) throws java.lang.IllegalArgumentException {
        if (e < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POWER_NEGATIVE_PARAMETERS, java.lang.Long.valueOf(k), java.lang.Long.valueOf(e));
        }
        long result = 1;
        long k2p = k;
        while (e != 0) {
            if ((1 & e) != 0) {
                result *= k2p;
            }
            k2p *= k2p;
            e >>= 1;
        }
        return result;
    }

    public static java.math.BigInteger pow(java.math.BigInteger k, int e) throws java.lang.IllegalArgumentException {
        if (e < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POWER_NEGATIVE_PARAMETERS, k, java.lang.Integer.valueOf(e));
        }
        return k.pow(e);
    }

    public static java.math.BigInteger pow(java.math.BigInteger k, long e) throws java.lang.IllegalArgumentException {
        if (e < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POWER_NEGATIVE_PARAMETERS, k, java.lang.Long.valueOf(e));
        }
        java.math.BigInteger result = java.math.BigInteger.ONE;
        java.math.BigInteger k2p = k;
        while (e != 0) {
            if ((1 & e) != 0) {
                result = result.multiply(k2p);
            }
            k2p = k2p.multiply(k2p);
            e >>= 1;
        }
        return result;
    }

    public static java.math.BigInteger pow(java.math.BigInteger k, java.math.BigInteger e) throws java.lang.IllegalArgumentException {
        if (e.compareTo(java.math.BigInteger.ZERO) < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POWER_NEGATIVE_PARAMETERS, k, e);
        }
        java.math.BigInteger result = java.math.BigInteger.ONE;
        java.math.BigInteger k2p = k;
        while (!java.math.BigInteger.ZERO.equals(e)) {
            if (e.testBit(0)) {
                result = result.multiply(k2p);
            }
            k2p = k2p.multiply(k2p);
            e = e.shiftRight(1);
        }
        return result;
    }

    public static double distance1(double[] p1, double[] p2) {
        double sum = 0.0d;
        for (int i = 0; i < p1.length; i++) {
            sum += org.apache.commons.math.util.FastMath.abs(p1[i] - p2[i]);
        }
        return sum;
    }

    public static int distance1(int[] p1, int[] p2) {
        int sum = 0;
        for (int i = 0; i < p1.length; i++) {
            sum += org.apache.commons.math.util.FastMath.abs(p1[i] - p2[i]);
        }
        return sum;
    }

    public static double distance(double[] p1, double[] p2) {
        double sum = 0.0d;
        for (int i = 0; i < p1.length; i++) {
            double dp = p1[i] - p2[i];
            sum += dp * dp;
        }
        return org.apache.commons.math.util.FastMath.sqrt(sum);
    }

    public static double distance(int[] p1, int[] p2) {
        double sum = 0.0d;
        for (int i = 0; i < p1.length; i++) {
            double dp = p1[i] - p2[i];
            sum += dp * dp;
        }
        return org.apache.commons.math.util.FastMath.sqrt(sum);
    }

    public static double distanceInf(double[] p1, double[] p2) {
        double max = 0.0d;
        for (int i = 0; i < p1.length; i++) {
            max = org.apache.commons.math.util.FastMath.max(max, org.apache.commons.math.util.FastMath.abs(p1[i] - p2[i]));
        }
        return max;
    }

    public static int distanceInf(int[] p1, int[] p2) {
        int max = 0;
        for (int i = 0; i < p1.length; i++) {
            max = org.apache.commons.math.util.FastMath.max(max, org.apache.commons.math.util.FastMath.abs(p1[i] - p2[i]));
        }
        return max;
    }

    public static void checkOrder(double[] val, org.apache.commons.math.util.MathUtils.OrderDirection dir, boolean strict) {
        double previous = val[0];
        boolean ok = true;
        int max = val.length;
        for (int i = 1; i < max; i++) {
            switch (dir) {
                case INCREASING:
                    if (strict) {
                        if (val[i] <= previous) {
                            ok = false;
                        }
                    } else if (val[i] < previous) {
                        ok = false;
                    }
                    break;
                case DECREASING:
                    if (strict) {
                        if (val[i] >= previous) {
                            ok = false;
                        }
                    } else if (val[i] > previous) {
                        ok = false;
                    }
                    break;
                default:
                    throw new java.lang.IllegalArgumentException();
            }
            if (!ok) {
                throw new org.apache.commons.math.exception.NonMonotonousSequenceException(java.lang.Double.valueOf(val[i]), java.lang.Double.valueOf(previous), i, dir, strict);
            }
            previous = val[i];
        }
    }

    public static void checkOrder(double[] val) {
        checkOrder(val, org.apache.commons.math.util.MathUtils.OrderDirection.INCREASING, true);
    }

    @java.lang.Deprecated
    public static void checkOrder(double[] val, int dir, boolean strict) {
        if (dir > 0) {
            checkOrder(val, org.apache.commons.math.util.MathUtils.OrderDirection.INCREASING, strict);
        } else {
            checkOrder(val, org.apache.commons.math.util.MathUtils.OrderDirection.DECREASING, strict);
        }
    }

    public static double safeNorm(double[] v) {
        double rgiant = 1.304E19d;
        double s2 = 0.0d;
        double s3 = 0.0d;
        double x1max = 0.0d;
        double x3max = 0.0d;
        double s1 = 0.0d;
        double s12 = v.length;
        double agiant = 1.304E19d / s12;
        int i = 0;
        while (true) {
            double rgiant2 = rgiant;
            if (i >= v.length) {
                break;
            }
            double xabs = java.lang.Math.abs(v[i]);
            if (xabs >= 3.834E-20d && xabs <= agiant) {
                s2 += xabs * xabs;
            } else if (xabs > 3.834E-20d) {
                if (xabs > x1max) {
                    double r = x1max / xabs;
                    x1max = xabs;
                    s1 = (s1 * r * r) + 1.0d;
                } else {
                    double r2 = xabs / x1max;
                    s1 += r2 * r2;
                }
            } else if (xabs > x3max) {
                double r3 = x3max / xabs;
                x3max = xabs;
                s3 = (s3 * r3 * r3) + 1.0d;
            } else if (xabs != 0.0d) {
                double r4 = xabs / x3max;
                s3 += r4 * r4;
            }
            i++;
            rgiant = rgiant2;
        }
        if (s1 != 0.0d) {
            double norm = java.lang.Math.sqrt(s1 + ((s2 / x1max) / x1max)) * x1max;
            return norm;
        }
        if (s2 == 0.0d) {
            double norm2 = java.lang.Math.sqrt(s3) * x3max;
            return norm2;
        }
        if (s2 >= x3max) {
            double norm3 = java.lang.Math.sqrt((((x3max / s2) * x3max * s3) + 1.0d) * s2);
            return norm3;
        }
        double norm4 = s2 / x3max;
        return java.lang.Math.sqrt((norm4 + (x3max * s3)) * x3max);
    }
}
