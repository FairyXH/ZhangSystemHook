package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
public class DividedDifferenceInterpolator implements org.apache.commons.math.analysis.interpolation.UnivariateRealInterpolator, java.io.Serializable {
    private static final long serialVersionUID = 107049519551235069L;

    @Override // org.apache.commons.math.analysis.interpolation.UnivariateRealInterpolator
    public org.apache.commons.math.analysis.polynomials.PolynomialFunctionNewtonForm interpolate(double[] x, double[] y) throws org.apache.commons.math.DuplicateSampleAbscissaException {
        org.apache.commons.math.analysis.polynomials.PolynomialFunctionLagrangeForm.verifyInterpolationArray(x, y);
        double[] c = new double[x.length - 1];
        java.lang.System.arraycopy(x, 0, c, 0, c.length);
        double[] a = computeDividedDifference(x, y);
        return new org.apache.commons.math.analysis.polynomials.PolynomialFunctionNewtonForm(a, c);
    }

    protected static double[] computeDividedDifference(double[] x, double[] y) throws org.apache.commons.math.DuplicateSampleAbscissaException {
        org.apache.commons.math.analysis.polynomials.PolynomialFunctionLagrangeForm.verifyInterpolationArray(x, y);
        double[] divdiff = (double[]) y.clone();
        int n = x.length;
        double[] a = new double[n];
        a[0] = divdiff[0];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                double denominator = x[j + i] - x[j];
                if (denominator == 0.0d) {
                    throw new org.apache.commons.math.DuplicateSampleAbscissaException(x[j], j, j + i);
                }
                divdiff[j] = (divdiff[j + 1] - divdiff[j]) / denominator;
            }
            a[i] = divdiff[0];
        }
        return a;
    }
}
