package org.apache.commons.math.analysis.polynomials;

/* JADX INFO: loaded from: classes4.dex */
public class PolynomialsUtils {
    private static final java.util.ArrayList<org.apache.commons.math.fraction.BigFraction> CHEBYSHEV_COEFFICIENTS = new java.util.ArrayList<>();
    private static final java.util.ArrayList<org.apache.commons.math.fraction.BigFraction> HERMITE_COEFFICIENTS;
    private static final java.util.ArrayList<org.apache.commons.math.fraction.BigFraction> LAGUERRE_COEFFICIENTS;
    private static final java.util.ArrayList<org.apache.commons.math.fraction.BigFraction> LEGENDRE_COEFFICIENTS;

    private interface RecurrenceCoefficientsGenerator {
        org.apache.commons.math.fraction.BigFraction[] generate(int i);
    }

    static {
        CHEBYSHEV_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ONE);
        CHEBYSHEV_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ZERO);
        CHEBYSHEV_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ONE);
        HERMITE_COEFFICIENTS = new java.util.ArrayList<>();
        HERMITE_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ONE);
        HERMITE_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ZERO);
        HERMITE_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.TWO);
        LAGUERRE_COEFFICIENTS = new java.util.ArrayList<>();
        LAGUERRE_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ONE);
        LAGUERRE_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ONE);
        LAGUERRE_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.MINUS_ONE);
        LEGENDRE_COEFFICIENTS = new java.util.ArrayList<>();
        LEGENDRE_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ONE);
        LEGENDRE_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ZERO);
        LEGENDRE_COEFFICIENTS.add(org.apache.commons.math.fraction.BigFraction.ONE);
    }

    private PolynomialsUtils() {
    }

    public static org.apache.commons.math.analysis.polynomials.PolynomialFunction createChebyshevPolynomial(int degree) {
        return buildPolynomial(degree, CHEBYSHEV_COEFFICIENTS, new org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator() { // from class: org.apache.commons.math.analysis.polynomials.PolynomialsUtils.1
            private final org.apache.commons.math.fraction.BigFraction[] coeffs = {org.apache.commons.math.fraction.BigFraction.ZERO, org.apache.commons.math.fraction.BigFraction.TWO, org.apache.commons.math.fraction.BigFraction.ONE};

            @Override // org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator
            public org.apache.commons.math.fraction.BigFraction[] generate(int k) {
                return this.coeffs;
            }
        });
    }

    public static org.apache.commons.math.analysis.polynomials.PolynomialFunction createHermitePolynomial(int degree) {
        return buildPolynomial(degree, HERMITE_COEFFICIENTS, new org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator() { // from class: org.apache.commons.math.analysis.polynomials.PolynomialsUtils.2
            @Override // org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator
            public org.apache.commons.math.fraction.BigFraction[] generate(int k) {
                return new org.apache.commons.math.fraction.BigFraction[]{org.apache.commons.math.fraction.BigFraction.ZERO, org.apache.commons.math.fraction.BigFraction.TWO, new org.apache.commons.math.fraction.BigFraction(k * 2)};
            }
        });
    }

    public static org.apache.commons.math.analysis.polynomials.PolynomialFunction createLaguerrePolynomial(int degree) {
        return buildPolynomial(degree, LAGUERRE_COEFFICIENTS, new org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator() { // from class: org.apache.commons.math.analysis.polynomials.PolynomialsUtils.3
            @Override // org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator
            public org.apache.commons.math.fraction.BigFraction[] generate(int k) {
                int kP1 = k + 1;
                return new org.apache.commons.math.fraction.BigFraction[]{new org.apache.commons.math.fraction.BigFraction((k * 2) + 1, kP1), new org.apache.commons.math.fraction.BigFraction(-1, kP1), new org.apache.commons.math.fraction.BigFraction(k, kP1)};
            }
        });
    }

    public static org.apache.commons.math.analysis.polynomials.PolynomialFunction createLegendrePolynomial(int degree) {
        return buildPolynomial(degree, LEGENDRE_COEFFICIENTS, new org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator() { // from class: org.apache.commons.math.analysis.polynomials.PolynomialsUtils.4
            @Override // org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator
            public org.apache.commons.math.fraction.BigFraction[] generate(int k) {
                int kP1 = k + 1;
                return new org.apache.commons.math.fraction.BigFraction[]{org.apache.commons.math.fraction.BigFraction.ZERO, new org.apache.commons.math.fraction.BigFraction(k + kP1, kP1), new org.apache.commons.math.fraction.BigFraction(k, kP1)};
            }
        });
    }

    private static org.apache.commons.math.analysis.polynomials.PolynomialFunction buildPolynomial(int degree, java.util.ArrayList<org.apache.commons.math.fraction.BigFraction> coefficients, org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator generator) {
        int maxDegree = ((int) org.apache.commons.math.util.FastMath.floor(org.apache.commons.math.util.FastMath.sqrt(coefficients.size() * 2))) - 1;
        synchronized (org.apache.commons.math.analysis.polynomials.PolynomialsUtils.class) {
            if (degree > maxDegree) {
                computeUpToDegree(degree, maxDegree, generator, coefficients);
            }
        }
        int start = ((degree + 1) * degree) / 2;
        double[] a = new double[degree + 1];
        for (int i = 0; i <= degree; i++) {
            a[i] = coefficients.get(start + i).doubleValue();
        }
        return new org.apache.commons.math.analysis.polynomials.PolynomialFunction(a);
    }

    private static void computeUpToDegree(int degree, int maxDegree, org.apache.commons.math.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator generator, java.util.ArrayList<org.apache.commons.math.fraction.BigFraction> coefficients) {
        int startK = ((maxDegree - 1) * maxDegree) / 2;
        for (int k = maxDegree; k < degree; k++) {
            int startKm1 = startK;
            startK += k;
            org.apache.commons.math.fraction.BigFraction[] ai = generator.generate(k);
            org.apache.commons.math.fraction.BigFraction ck = coefficients.get(startK);
            org.apache.commons.math.fraction.BigFraction ckm1 = coefficients.get(startKm1);
            coefficients.add(ck.multiply(ai[0]).subtract(ckm1.multiply(ai[2])));
            for (int i = 1; i < k; i++) {
                org.apache.commons.math.fraction.BigFraction ckPrev = ck;
                ck = coefficients.get(startK + i);
                org.apache.commons.math.fraction.BigFraction ckm12 = coefficients.get(startKm1 + i);
                coefficients.add(ck.multiply(ai[0]).add(ckPrev.multiply(ai[1])).subtract(ckm12.multiply(ai[2])));
            }
            org.apache.commons.math.fraction.BigFraction ckPrev2 = ck;
            org.apache.commons.math.fraction.BigFraction ck2 = coefficients.get(startK + k);
            coefficients.add(ck2.multiply(ai[0]).add(ckPrev2.multiply(ai[1])));
            coefficients.add(ck2.multiply(ai[1]));
        }
    }
}
