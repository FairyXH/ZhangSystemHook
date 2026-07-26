package org.apache.commons.math.optimization.linear;

/* JADX INFO: loaded from: classes4.dex */
public class LinearObjectiveFunction implements java.io.Serializable {
    private static final long serialVersionUID = -4531815507568396090L;
    private final transient org.apache.commons.math.linear.RealVector coefficients;
    private final double constantTerm;

    public LinearObjectiveFunction(double[] coefficients, double constantTerm) {
        this(new org.apache.commons.math.linear.ArrayRealVector(coefficients), constantTerm);
    }

    public LinearObjectiveFunction(org.apache.commons.math.linear.RealVector coefficients, double constantTerm) {
        this.coefficients = coefficients;
        this.constantTerm = constantTerm;
    }

    public org.apache.commons.math.linear.RealVector getCoefficients() {
        return this.coefficients;
    }

    public double getConstantTerm() {
        return this.constantTerm;
    }

    public double getValue(double[] point) {
        return this.coefficients.dotProduct(point) + this.constantTerm;
    }

    public double getValue(org.apache.commons.math.linear.RealVector point) {
        return this.coefficients.dotProduct(point) + this.constantTerm;
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof org.apache.commons.math.optimization.linear.LinearObjectiveFunction)) {
            return false;
        }
        org.apache.commons.math.optimization.linear.LinearObjectiveFunction rhs = (org.apache.commons.math.optimization.linear.LinearObjectiveFunction) other;
        return this.constantTerm == rhs.constantTerm && this.coefficients.equals(rhs.coefficients);
    }

    public int hashCode() {
        return java.lang.Double.valueOf(this.constantTerm).hashCode() ^ this.coefficients.hashCode();
    }

    private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException {
        oos.defaultWriteObject();
        org.apache.commons.math.linear.MatrixUtils.serializeRealVector(this.coefficients, oos);
    }

    private void readObject(java.io.ObjectInputStream ois) throws java.lang.ClassNotFoundException, java.io.IOException {
        ois.defaultReadObject();
        org.apache.commons.math.linear.MatrixUtils.deserializeRealVector(this, "coefficients", ois);
    }
}
