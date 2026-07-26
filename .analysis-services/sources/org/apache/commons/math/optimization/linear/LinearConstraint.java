package org.apache.commons.math.optimization.linear;

/* JADX INFO: loaded from: classes4.dex */
public class LinearConstraint implements java.io.Serializable {
    private static final long serialVersionUID = -764632794033034092L;
    private final transient org.apache.commons.math.linear.RealVector coefficients;
    private final org.apache.commons.math.optimization.linear.Relationship relationship;
    private final double value;

    public LinearConstraint(double[] coefficients, org.apache.commons.math.optimization.linear.Relationship relationship, double value) {
        this(new org.apache.commons.math.linear.ArrayRealVector(coefficients), relationship, value);
    }

    public LinearConstraint(org.apache.commons.math.linear.RealVector coefficients, org.apache.commons.math.optimization.linear.Relationship relationship, double value) {
        this.coefficients = coefficients;
        this.relationship = relationship;
        this.value = value;
    }

    public LinearConstraint(double[] lhsCoefficients, double lhsConstant, org.apache.commons.math.optimization.linear.Relationship relationship, double[] rhsCoefficients, double rhsConstant) {
        double[] sub = new double[lhsCoefficients.length];
        for (int i = 0; i < sub.length; i++) {
            sub[i] = lhsCoefficients[i] - rhsCoefficients[i];
        }
        this.coefficients = new org.apache.commons.math.linear.ArrayRealVector(sub, false);
        this.relationship = relationship;
        this.value = rhsConstant - lhsConstant;
    }

    public LinearConstraint(org.apache.commons.math.linear.RealVector lhsCoefficients, double lhsConstant, org.apache.commons.math.optimization.linear.Relationship relationship, org.apache.commons.math.linear.RealVector rhsCoefficients, double rhsConstant) {
        this.coefficients = lhsCoefficients.subtract(rhsCoefficients);
        this.relationship = relationship;
        this.value = rhsConstant - lhsConstant;
    }

    public org.apache.commons.math.linear.RealVector getCoefficients() {
        return this.coefficients;
    }

    public org.apache.commons.math.optimization.linear.Relationship getRelationship() {
        return this.relationship;
    }

    public double getValue() {
        return this.value;
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof org.apache.commons.math.optimization.linear.LinearConstraint)) {
            return false;
        }
        org.apache.commons.math.optimization.linear.LinearConstraint rhs = (org.apache.commons.math.optimization.linear.LinearConstraint) other;
        return this.relationship == rhs.relationship && this.value == rhs.value && this.coefficients.equals(rhs.coefficients);
    }

    public int hashCode() {
        return (this.relationship.hashCode() ^ java.lang.Double.valueOf(this.value).hashCode()) ^ this.coefficients.hashCode();
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
