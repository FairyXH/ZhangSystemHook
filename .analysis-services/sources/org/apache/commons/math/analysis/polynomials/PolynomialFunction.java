package org.apache.commons.math.analysis.polynomials;

/* JADX INFO: loaded from: classes4.dex */
public class PolynomialFunction implements org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction, java.io.Serializable {
    private static final long serialVersionUID = -7726511984200295583L;
    private final double[] coefficients;

    public PolynomialFunction(double[] c) {
        int n = c.length;
        if (n == 0) {
            throw new org.apache.commons.math.exception.NoDataException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        while (n > 1 && c[n - 1] == 0.0d) {
            n--;
        }
        this.coefficients = new double[n];
        java.lang.System.arraycopy(c, 0, this.coefficients, 0, n);
    }

    @Override // org.apache.commons.math.analysis.UnivariateRealFunction
    public double value(double x) {
        return evaluate(this.coefficients, x);
    }

    public int degree() {
        return this.coefficients.length - 1;
    }

    public double[] getCoefficients() {
        return (double[]) this.coefficients.clone();
    }

    protected static double evaluate(double[] coefficients, double argument) {
        int n = coefficients.length;
        if (n == 0) {
            throw new org.apache.commons.math.exception.NoDataException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        double result = coefficients[n - 1];
        for (int j = n - 2; j >= 0; j--) {
            result = (argument * result) + coefficients[j];
        }
        return result;
    }

    public org.apache.commons.math.analysis.polynomials.PolynomialFunction add(org.apache.commons.math.analysis.polynomials.PolynomialFunction p) {
        int lowLength = org.apache.commons.math.util.FastMath.min(this.coefficients.length, p.coefficients.length);
        int highLength = org.apache.commons.math.util.FastMath.max(this.coefficients.length, p.coefficients.length);
        double[] newCoefficients = new double[highLength];
        for (int i = 0; i < lowLength; i++) {
            newCoefficients[i] = this.coefficients[i] + p.coefficients[i];
        }
        java.lang.System.arraycopy(this.coefficients.length < p.coefficients.length ? p.coefficients : this.coefficients, lowLength, newCoefficients, lowLength, highLength - lowLength);
        return new org.apache.commons.math.analysis.polynomials.PolynomialFunction(newCoefficients);
    }

    public org.apache.commons.math.analysis.polynomials.PolynomialFunction subtract(org.apache.commons.math.analysis.polynomials.PolynomialFunction p) {
        int lowLength = org.apache.commons.math.util.FastMath.min(this.coefficients.length, p.coefficients.length);
        int highLength = org.apache.commons.math.util.FastMath.max(this.coefficients.length, p.coefficients.length);
        double[] newCoefficients = new double[highLength];
        for (int i = 0; i < lowLength; i++) {
            newCoefficients[i] = this.coefficients[i] - p.coefficients[i];
        }
        if (this.coefficients.length < p.coefficients.length) {
            for (int i2 = lowLength; i2 < highLength; i2++) {
                newCoefficients[i2] = -p.coefficients[i2];
            }
        } else {
            java.lang.System.arraycopy(this.coefficients, lowLength, newCoefficients, lowLength, highLength - lowLength);
        }
        return new org.apache.commons.math.analysis.polynomials.PolynomialFunction(newCoefficients);
    }

    public org.apache.commons.math.analysis.polynomials.PolynomialFunction negate() {
        double[] newCoefficients = new double[this.coefficients.length];
        for (int i = 0; i < this.coefficients.length; i++) {
            newCoefficients[i] = -this.coefficients[i];
        }
        return new org.apache.commons.math.analysis.polynomials.PolynomialFunction(newCoefficients);
    }

    public org.apache.commons.math.analysis.polynomials.PolynomialFunction multiply(org.apache.commons.math.analysis.polynomials.PolynomialFunction p) {
        double[] newCoefficients = new double[(this.coefficients.length + p.coefficients.length) - 1];
        for (int i = 0; i < newCoefficients.length; i++) {
            newCoefficients[i] = 0.0d;
            for (int j = org.apache.commons.math.util.FastMath.max(0, (i + 1) - p.coefficients.length); j < org.apache.commons.math.util.FastMath.min(this.coefficients.length, i + 1); j++) {
                newCoefficients[i] = newCoefficients[i] + (this.coefficients[j] * p.coefficients[i - j]);
            }
        }
        return new org.apache.commons.math.analysis.polynomials.PolynomialFunction(newCoefficients);
    }

    protected static double[] differentiate(double[] coefficients) {
        int n = coefficients.length;
        if (n == 0) {
            throw new org.apache.commons.math.exception.NoDataException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        if (n == 1) {
            return new double[]{0.0d};
        }
        double[] result = new double[n - 1];
        for (int i = n - 1; i > 0; i--) {
            result[i - 1] = ((double) i) * coefficients[i];
        }
        return result;
    }

    public org.apache.commons.math.analysis.polynomials.PolynomialFunction polynomialDerivative() {
        return new org.apache.commons.math.analysis.polynomials.PolynomialFunction(differentiate(this.coefficients));
    }

    @Override // org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction
    public org.apache.commons.math.analysis.UnivariateRealFunction derivative() {
        return polynomialDerivative();
    }

    public java.lang.String toString() {
        java.lang.StringBuilder s = new java.lang.StringBuilder();
        if (this.coefficients[0] == 0.0d) {
            if (this.coefficients.length == 1) {
                return "0";
            }
        } else {
            s.append(java.lang.Double.toString(this.coefficients[0]));
        }
        for (int i = 1; i < this.coefficients.length; i++) {
            if (this.coefficients[i] != 0.0d) {
                if (s.length() > 0) {
                    if (this.coefficients[i] < 0.0d) {
                        s.append(" - ");
                    } else {
                        s.append(" + ");
                    }
                } else if (this.coefficients[i] < 0.0d) {
                    s.append("-");
                }
                double absAi = org.apache.commons.math.util.FastMath.abs(this.coefficients[i]);
                if (absAi - 1.0d != 0.0d) {
                    s.append(java.lang.Double.toString(absAi));
                    s.append(' ');
                }
                s.append("x");
                if (i > 1) {
                    s.append('^');
                    s.append(java.lang.Integer.toString(i));
                }
            }
        }
        return s.toString();
    }

    public int hashCode() {
        int result = (1 * 31) + java.util.Arrays.hashCode(this.coefficients);
        return result;
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof org.apache.commons.math.analysis.polynomials.PolynomialFunction)) {
            return false;
        }
        org.apache.commons.math.analysis.polynomials.PolynomialFunction other = (org.apache.commons.math.analysis.polynomials.PolynomialFunction) obj;
        return java.util.Arrays.equals(this.coefficients, other.coefficients);
    }
}
