package org.apache.commons.math.geometry;

/* JADX INFO: loaded from: classes4.dex */
public class Vector3D implements java.io.Serializable {
    private static final long serialVersionUID = 5133268763396045979L;
    private final double x;
    private final double y;
    private final double z;
    public static final org.apache.commons.math.geometry.Vector3D ZERO = new org.apache.commons.math.geometry.Vector3D(0.0d, 0.0d, 0.0d);
    public static final org.apache.commons.math.geometry.Vector3D PLUS_I = new org.apache.commons.math.geometry.Vector3D(1.0d, 0.0d, 0.0d);
    public static final org.apache.commons.math.geometry.Vector3D MINUS_I = new org.apache.commons.math.geometry.Vector3D(-1.0d, 0.0d, 0.0d);
    public static final org.apache.commons.math.geometry.Vector3D PLUS_J = new org.apache.commons.math.geometry.Vector3D(0.0d, 1.0d, 0.0d);
    public static final org.apache.commons.math.geometry.Vector3D MINUS_J = new org.apache.commons.math.geometry.Vector3D(0.0d, -1.0d, 0.0d);
    public static final org.apache.commons.math.geometry.Vector3D PLUS_K = new org.apache.commons.math.geometry.Vector3D(0.0d, 0.0d, 1.0d);
    public static final org.apache.commons.math.geometry.Vector3D MINUS_K = new org.apache.commons.math.geometry.Vector3D(0.0d, 0.0d, -1.0d);
    public static final org.apache.commons.math.geometry.Vector3D NaN = new org.apache.commons.math.geometry.Vector3D(Double.NaN, Double.NaN, Double.NaN);
    public static final org.apache.commons.math.geometry.Vector3D POSITIVE_INFINITY = new org.apache.commons.math.geometry.Vector3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final org.apache.commons.math.geometry.Vector3D NEGATIVE_INFINITY = new org.apache.commons.math.geometry.Vector3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    private static final org.apache.commons.math.geometry.Vector3DFormat DEFAULT_FORMAT = org.apache.commons.math.geometry.Vector3DFormat.getInstance();

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(double alpha, double delta) {
        double cosDelta = org.apache.commons.math.util.FastMath.cos(delta);
        this.x = org.apache.commons.math.util.FastMath.cos(alpha) * cosDelta;
        this.y = org.apache.commons.math.util.FastMath.sin(alpha) * cosDelta;
        this.z = org.apache.commons.math.util.FastMath.sin(delta);
    }

    public Vector3D(double a, org.apache.commons.math.geometry.Vector3D u) {
        this.x = u.x * a;
        this.y = u.y * a;
        this.z = u.z * a;
    }

    public Vector3D(double a1, org.apache.commons.math.geometry.Vector3D u1, double a2, org.apache.commons.math.geometry.Vector3D u2) {
        this.x = (u1.x * a1) + (u2.x * a2);
        this.y = (u1.y * a1) + (u2.y * a2);
        this.z = (u1.z * a1) + (u2.z * a2);
    }

    public Vector3D(double a1, org.apache.commons.math.geometry.Vector3D u1, double a2, org.apache.commons.math.geometry.Vector3D u2, double a3, org.apache.commons.math.geometry.Vector3D u3) {
        this.x = (u1.x * a1) + (u2.x * a2) + (u3.x * a3);
        this.y = (u1.y * a1) + (u2.y * a2) + (u3.y * a3);
        this.z = (u1.z * a1) + (u2.z * a2) + (u3.z * a3);
    }

    public Vector3D(double a1, org.apache.commons.math.geometry.Vector3D u1, double a2, org.apache.commons.math.geometry.Vector3D u2, double a3, org.apache.commons.math.geometry.Vector3D u3, double a4, org.apache.commons.math.geometry.Vector3D u4) {
        this.x = (u1.x * a1) + (u2.x * a2) + (u3.x * a3) + (u4.x * a4);
        this.y = (u1.y * a1) + (u2.y * a2) + (u3.y * a3) + (u4.y * a4);
        this.z = (u1.z * a1) + (u2.z * a2) + (u3.z * a3) + (u4.z * a4);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getNorm1() {
        return org.apache.commons.math.util.FastMath.abs(this.x) + org.apache.commons.math.util.FastMath.abs(this.y) + org.apache.commons.math.util.FastMath.abs(this.z);
    }

    public double getNorm() {
        return org.apache.commons.math.util.FastMath.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }

    public double getNormSq() {
        return (this.x * this.x) + (this.y * this.y) + (this.z * this.z);
    }

    public double getNormInf() {
        return org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(this.x), org.apache.commons.math.util.FastMath.abs(this.y)), org.apache.commons.math.util.FastMath.abs(this.z));
    }

    public double getAlpha() {
        return org.apache.commons.math.util.FastMath.atan2(this.y, this.x);
    }

    public double getDelta() {
        return org.apache.commons.math.util.FastMath.asin(this.z / getNorm());
    }

    public org.apache.commons.math.geometry.Vector3D add(org.apache.commons.math.geometry.Vector3D v) {
        return new org.apache.commons.math.geometry.Vector3D(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public org.apache.commons.math.geometry.Vector3D add(double factor, org.apache.commons.math.geometry.Vector3D v) {
        return new org.apache.commons.math.geometry.Vector3D(this.x + (v.x * factor), this.y + (v.y * factor), this.z + (v.z * factor));
    }

    public org.apache.commons.math.geometry.Vector3D subtract(org.apache.commons.math.geometry.Vector3D v) {
        return new org.apache.commons.math.geometry.Vector3D(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public org.apache.commons.math.geometry.Vector3D subtract(double factor, org.apache.commons.math.geometry.Vector3D v) {
        return new org.apache.commons.math.geometry.Vector3D(this.x - (v.x * factor), this.y - (v.y * factor), this.z - (v.z * factor));
    }

    public org.apache.commons.math.geometry.Vector3D normalize() {
        double s = getNorm();
        if (s == 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new java.lang.Object[0]);
        }
        return scalarMultiply(1.0d / s);
    }

    public org.apache.commons.math.geometry.Vector3D orthogonal() {
        double threshold = getNorm() * 0.6d;
        if (threshold == 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_NORM, new java.lang.Object[0]);
        }
        if (this.x >= (-threshold) && this.x <= threshold) {
            double inverse = 1.0d / org.apache.commons.math.util.FastMath.sqrt((this.y * this.y) + (this.z * this.z));
            return new org.apache.commons.math.geometry.Vector3D(0.0d, inverse * this.z, this.y * (-inverse));
        }
        if (this.y >= (-threshold) && this.y <= threshold) {
            double inverse2 = 1.0d / org.apache.commons.math.util.FastMath.sqrt((this.x * this.x) + (this.z * this.z));
            return new org.apache.commons.math.geometry.Vector3D((-inverse2) * this.z, 0.0d, inverse2 * this.x);
        }
        double inverse3 = 1.0d / org.apache.commons.math.util.FastMath.sqrt((this.x * this.x) + (this.y * this.y));
        return new org.apache.commons.math.geometry.Vector3D(inverse3 * this.y, (-inverse3) * this.x, 0.0d);
    }

    public static double angle(org.apache.commons.math.geometry.Vector3D v1, org.apache.commons.math.geometry.Vector3D v2) {
        double normProduct = v1.getNorm() * v2.getNorm();
        if (normProduct == 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_NORM, new java.lang.Object[0]);
        }
        double dot = dotProduct(v1, v2);
        double threshold = 0.9999d * normProduct;
        if (dot < (-threshold) || dot > threshold) {
            org.apache.commons.math.geometry.Vector3D v3 = crossProduct(v1, v2);
            if (dot >= 0.0d) {
                return org.apache.commons.math.util.FastMath.asin(v3.getNorm() / normProduct);
            }
            return 3.141592653589793d - org.apache.commons.math.util.FastMath.asin(v3.getNorm() / normProduct);
        }
        return org.apache.commons.math.util.FastMath.acos(dot / normProduct);
    }

    public org.apache.commons.math.geometry.Vector3D negate() {
        return new org.apache.commons.math.geometry.Vector3D(-this.x, -this.y, -this.z);
    }

    public org.apache.commons.math.geometry.Vector3D scalarMultiply(double a) {
        return new org.apache.commons.math.geometry.Vector3D(a * this.x, this.y * a, this.z * a);
    }

    public boolean isNaN() {
        return java.lang.Double.isNaN(this.x) || java.lang.Double.isNaN(this.y) || java.lang.Double.isNaN(this.z);
    }

    public boolean isInfinite() {
        return !isNaN() && (java.lang.Double.isInfinite(this.x) || java.lang.Double.isInfinite(this.y) || java.lang.Double.isInfinite(this.z));
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof org.apache.commons.math.geometry.Vector3D)) {
            return false;
        }
        org.apache.commons.math.geometry.Vector3D rhs = (org.apache.commons.math.geometry.Vector3D) other;
        if (rhs.isNaN()) {
            return isNaN();
        }
        return this.x == rhs.x && this.y == rhs.y && this.z == rhs.z;
    }

    public int hashCode() {
        if (isNaN()) {
            return 8;
        }
        return ((org.apache.commons.math.util.MathUtils.hash(this.x) * 23) + (org.apache.commons.math.util.MathUtils.hash(this.y) * 19) + org.apache.commons.math.util.MathUtils.hash(this.z)) * 31;
    }

    public static double dotProduct(org.apache.commons.math.geometry.Vector3D v1, org.apache.commons.math.geometry.Vector3D v2) {
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
    }

    public static org.apache.commons.math.geometry.Vector3D crossProduct(org.apache.commons.math.geometry.Vector3D v1, org.apache.commons.math.geometry.Vector3D v2) {
        return new org.apache.commons.math.geometry.Vector3D((v1.y * v2.z) - (v1.z * v2.y), (v1.z * v2.x) - (v1.x * v2.z), (v1.x * v2.y) - (v1.y * v2.x));
    }

    public static double distance1(org.apache.commons.math.geometry.Vector3D v1, org.apache.commons.math.geometry.Vector3D v2) {
        double dx = org.apache.commons.math.util.FastMath.abs(v2.x - v1.x);
        double dy = org.apache.commons.math.util.FastMath.abs(v2.y - v1.y);
        double dz = org.apache.commons.math.util.FastMath.abs(v2.z - v1.z);
        return dx + dy + dz;
    }

    public static double distance(org.apache.commons.math.geometry.Vector3D v1, org.apache.commons.math.geometry.Vector3D v2) {
        double dx = v2.x - v1.x;
        double dy = v2.y - v1.y;
        double dz = v2.z - v1.z;
        return org.apache.commons.math.util.FastMath.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public static double distanceInf(org.apache.commons.math.geometry.Vector3D v1, org.apache.commons.math.geometry.Vector3D v2) {
        double dx = org.apache.commons.math.util.FastMath.abs(v2.x - v1.x);
        double dy = org.apache.commons.math.util.FastMath.abs(v2.y - v1.y);
        double dz = org.apache.commons.math.util.FastMath.abs(v2.z - v1.z);
        return org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.max(dx, dy), dz);
    }

    public static double distanceSq(org.apache.commons.math.geometry.Vector3D v1, org.apache.commons.math.geometry.Vector3D v2) {
        double dx = v2.x - v1.x;
        double dy = v2.y - v1.y;
        double dz = v2.z - v1.z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public java.lang.String toString() {
        return DEFAULT_FORMAT.format(this);
    }
}
