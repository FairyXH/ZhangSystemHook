package org.apache.commons.math.geometry;

/* JADX INFO: loaded from: classes4.dex */
public class Rotation implements java.io.Serializable {
    public static final org.apache.commons.math.geometry.Rotation IDENTITY = new org.apache.commons.math.geometry.Rotation(1.0d, 0.0d, 0.0d, 0.0d, false);
    private static final long serialVersionUID = -2153622329907944313L;
    private final double q0;
    private final double q1;
    private final double q2;
    private final double q3;

    public Rotation(double q0, double q1, double q2, double q3, boolean needsNormalization) {
        if (needsNormalization) {
            double inv = 1.0d / org.apache.commons.math.util.FastMath.sqrt((((q0 * q0) + (q1 * q1)) + (q2 * q2)) + (q3 * q3));
            q0 *= inv;
            q1 *= inv;
            q2 *= inv;
            q3 *= inv;
        }
        this.q0 = q0;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }

    public Rotation(org.apache.commons.math.geometry.Vector3D axis, double angle) {
        double norm = axis.getNorm();
        if (norm == 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS, new java.lang.Object[0]);
        }
        double halfAngle = (-0.5d) * angle;
        double coeff = org.apache.commons.math.util.FastMath.sin(halfAngle) / norm;
        this.q0 = org.apache.commons.math.util.FastMath.cos(halfAngle);
        this.q1 = axis.getX() * coeff;
        this.q2 = axis.getY() * coeff;
        this.q3 = axis.getZ() * coeff;
    }

    public Rotation(double[][] m, double threshold) throws org.apache.commons.math.geometry.NotARotationMatrixException {
        if (m.length != 3 || m[0].length != 3 || m[1].length != 3 || m[2].length != 3) {
            throw new org.apache.commons.math.geometry.NotARotationMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.ROTATION_MATRIX_DIMENSIONS, java.lang.Integer.valueOf(m.length), java.lang.Integer.valueOf(m[0].length));
        }
        double[][] ort = orthogonalizeMatrix(m, threshold);
        double det = ((ort[0][0] * ((ort[1][1] * ort[2][2]) - (ort[2][1] * ort[1][2]))) - (ort[1][0] * ((ort[0][1] * ort[2][2]) - (ort[2][1] * ort[0][2])))) + (ort[2][0] * ((ort[0][1] * ort[1][2]) - (ort[1][1] * ort[0][2])));
        if (det < 0.0d) {
            throw new org.apache.commons.math.geometry.NotARotationMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.CLOSEST_ORTHOGONAL_MATRIX_HAS_NEGATIVE_DETERMINANT, java.lang.Double.valueOf(det));
        }
        double s = ort[0][0] + ort[1][1] + ort[2][2];
        if (s > -0.19d) {
            this.q0 = org.apache.commons.math.util.FastMath.sqrt(1.0d + s) * 0.5d;
            double inv = 0.25d / this.q0;
            this.q1 = (ort[1][2] - ort[2][1]) * inv;
            this.q2 = (ort[2][0] - ort[0][2]) * inv;
            this.q3 = (ort[0][1] - ort[1][0]) * inv;
            return;
        }
        double s2 = (ort[0][0] - ort[1][1]) - ort[2][2];
        if (s2 > -0.19d) {
            this.q1 = org.apache.commons.math.util.FastMath.sqrt(1.0d + s2) * 0.5d;
            double inv2 = 0.25d / this.q1;
            this.q0 = (ort[1][2] - ort[2][1]) * inv2;
            this.q2 = (ort[0][1] + ort[1][0]) * inv2;
            this.q3 = (ort[0][2] + ort[2][0]) * inv2;
            return;
        }
        double s3 = (ort[1][1] - ort[0][0]) - ort[2][2];
        if (s3 > -0.19d) {
            this.q2 = org.apache.commons.math.util.FastMath.sqrt(1.0d + s3) * 0.5d;
            double inv3 = 0.25d / this.q2;
            this.q0 = (ort[2][0] - ort[0][2]) * inv3;
            this.q1 = (ort[0][1] + ort[1][0]) * inv3;
            this.q3 = (ort[2][1] + ort[1][2]) * inv3;
            return;
        }
        this.q3 = org.apache.commons.math.util.FastMath.sqrt(1.0d + ((ort[2][2] - ort[0][0]) - ort[1][1])) * 0.5d;
        double inv4 = 0.25d / this.q3;
        this.q0 = (ort[0][1] - ort[1][0]) * inv4;
        this.q1 = (ort[0][2] + ort[2][0]) * inv4;
        this.q2 = (ort[2][1] + ort[1][2]) * inv4;
    }

    public Rotation(org.apache.commons.math.geometry.Vector3D u1, org.apache.commons.math.geometry.Vector3D u2, org.apache.commons.math.geometry.Vector3D v1, org.apache.commons.math.geometry.Vector3D v2) {
        org.apache.commons.math.geometry.Vector3D vRef;
        double u1u1 = org.apache.commons.math.geometry.Vector3D.dotProduct(u1, u1);
        double u2u2 = org.apache.commons.math.geometry.Vector3D.dotProduct(u2, u2);
        double v1v1 = org.apache.commons.math.geometry.Vector3D.dotProduct(v1, v1);
        double v2v2 = org.apache.commons.math.geometry.Vector3D.dotProduct(v2, v2);
        if (u1u1 == 0.0d || u2u2 == 0.0d || v1v1 == 0.0d || v2v2 == 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR, new java.lang.Object[0]);
        }
        double u1x = u1.getX();
        double u1y = u1.getY();
        double u1z = u1.getZ();
        double u2x = u2.getX();
        double u2y = u2.getY();
        double u2z = u2.getZ();
        double coeff = org.apache.commons.math.util.FastMath.sqrt(u1u1 / v1v1);
        double v1x = v1.getX() * coeff;
        double v1y = coeff * v1.getY();
        double v1z = coeff * v1.getZ();
        org.apache.commons.math.geometry.Vector3D v12 = new org.apache.commons.math.geometry.Vector3D(v1x, v1y, v1z);
        double u1u2 = org.apache.commons.math.geometry.Vector3D.dotProduct(u1, u2);
        double v1v2 = org.apache.commons.math.geometry.Vector3D.dotProduct(v12, v2);
        double coeffU = u1u2 / u1u1;
        double coeffV = v1v2 / u1u1;
        double beta = org.apache.commons.math.util.FastMath.sqrt((u2u2 - (u1u2 * coeffU)) / (v2v2 - (v1v2 * coeffV)));
        double alpha = coeffU - (beta * coeffV);
        double v2x = (alpha * v1x) + (v2.getX() * beta);
        double v2y = (alpha * v1y) + (v2.getY() * beta);
        double v2z = (alpha * v1z) + (v2.getZ() * beta);
        org.apache.commons.math.geometry.Vector3D v22 = new org.apache.commons.math.geometry.Vector3D(v2x, v2y, v2z);
        org.apache.commons.math.geometry.Vector3D uRef = u1;
        double dx1 = v1x - u1.getX();
        double dy1 = v1y - u1.getY();
        double dz1 = v1z - u1.getZ();
        double dx2 = v2x - u2.getX();
        double dy2 = v2y - u2.getY();
        double dz2 = v2z - u2.getZ();
        org.apache.commons.math.geometry.Vector3D k = new org.apache.commons.math.geometry.Vector3D((dy1 * dz2) - (dz1 * dy2), (dz1 * dx2) - (dx1 * dz2), (dx1 * dy2) - (dy1 * dx2));
        double c = (k.getX() * ((u1y * u2z) - (u1z * u2y))) + (k.getY() * ((u1z * u2x) - (u1x * u2z))) + (k.getZ() * ((u1x * u2y) - (u1y * u2x)));
        if (c != 0.0d) {
            vRef = v12;
        } else {
            org.apache.commons.math.geometry.Vector3D u3 = org.apache.commons.math.geometry.Vector3D.crossProduct(u1, u2);
            org.apache.commons.math.geometry.Vector3D v3 = org.apache.commons.math.geometry.Vector3D.crossProduct(v12, v22);
            double u3x = u3.getX();
            double u3y = u3.getY();
            double u3z = u3.getZ();
            double v3x = v3.getX();
            double v3y = v3.getY();
            double v3z = v3.getZ();
            double dx3 = v3x - u3x;
            double dy3 = v3y - u3y;
            double dz3 = v3z - u3z;
            k = new org.apache.commons.math.geometry.Vector3D((dy1 * dz3) - (dz1 * dy3), (dz1 * dx3) - (dx1 * dz3), (dx1 * dy3) - (dy1 * dx3));
            c = (k.getX() * ((u1y * u3z) - (u1z * u3y))) + (k.getY() * ((u1z * u3x) - (u1x * u3z))) + (k.getZ() * ((u1x * u3y) - (u1y * u3x)));
            if (c != 0.0d) {
                vRef = v12;
            } else {
                k = new org.apache.commons.math.geometry.Vector3D((dy2 * dz3) - (dz2 * dy3), (dz2 * dx3) - (dx2 * dz3), (dx2 * dy3) - (dy2 * dx3));
                c = (k.getX() * ((u2y * u3z) - (u2z * u3y))) + (k.getY() * ((u2z * u3x) - (u2x * u3z))) + (k.getZ() * ((u2x * u3y) - (u2y * u3x)));
                if (c == 0.0d) {
                    this.q0 = 1.0d;
                    this.q1 = 0.0d;
                    this.q2 = 0.0d;
                    this.q3 = 0.0d;
                    return;
                }
                uRef = u2;
                vRef = v22;
            }
        }
        double c2 = org.apache.commons.math.util.FastMath.sqrt(c);
        double inv = 1.0d / (c2 + c2);
        this.q1 = inv * k.getX();
        this.q2 = k.getY() * inv;
        this.q3 = k.getZ() * inv;
        org.apache.commons.math.geometry.Vector3D k2 = new org.apache.commons.math.geometry.Vector3D((uRef.getY() * this.q3) - (uRef.getZ() * this.q2), (uRef.getZ() * this.q1) - (uRef.getX() * this.q3), (uRef.getX() * this.q2) - (uRef.getY() * this.q1));
        double c3 = org.apache.commons.math.geometry.Vector3D.dotProduct(k2, k2);
        this.q0 = org.apache.commons.math.geometry.Vector3D.dotProduct(vRef, k2) / (c3 + c3);
    }

    public Rotation(org.apache.commons.math.geometry.Vector3D u, org.apache.commons.math.geometry.Vector3D v) {
        double normProduct = u.getNorm() * v.getNorm();
        if (normProduct == 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR, new java.lang.Object[0]);
        }
        double dot = org.apache.commons.math.geometry.Vector3D.dotProduct(u, v);
        if (dot < (-0.999999999999998d) * normProduct) {
            org.apache.commons.math.geometry.Vector3D w = u.orthogonal();
            this.q0 = 0.0d;
            this.q1 = -w.getX();
            this.q2 = -w.getY();
            this.q3 = -w.getZ();
            return;
        }
        this.q0 = org.apache.commons.math.util.FastMath.sqrt(((dot / normProduct) + 1.0d) * 0.5d);
        double coeff = 1.0d / ((this.q0 * 2.0d) * normProduct);
        this.q1 = ((v.getY() * u.getZ()) - (v.getZ() * u.getY())) * coeff;
        this.q2 = ((v.getZ() * u.getX()) - (v.getX() * u.getZ())) * coeff;
        this.q3 = ((v.getX() * u.getY()) - (v.getY() * u.getX())) * coeff;
    }

    public Rotation(org.apache.commons.math.geometry.RotationOrder order, double alpha1, double alpha2, double alpha3) {
        org.apache.commons.math.geometry.Rotation r1 = new org.apache.commons.math.geometry.Rotation(order.getA1(), alpha1);
        org.apache.commons.math.geometry.Rotation r2 = new org.apache.commons.math.geometry.Rotation(order.getA2(), alpha2);
        org.apache.commons.math.geometry.Rotation r3 = new org.apache.commons.math.geometry.Rotation(order.getA3(), alpha3);
        org.apache.commons.math.geometry.Rotation composed = r1.applyTo(r2.applyTo(r3));
        this.q0 = composed.q0;
        this.q1 = composed.q1;
        this.q2 = composed.q2;
        this.q3 = composed.q3;
    }

    public org.apache.commons.math.geometry.Rotation revert() {
        return new org.apache.commons.math.geometry.Rotation(-this.q0, this.q1, this.q2, this.q3, false);
    }

    public double getQ0() {
        return this.q0;
    }

    public double getQ1() {
        return this.q1;
    }

    public double getQ2() {
        return this.q2;
    }

    public double getQ3() {
        return this.q3;
    }

    public org.apache.commons.math.geometry.Vector3D getAxis() {
        double squaredSine = (this.q1 * this.q1) + (this.q2 * this.q2) + (this.q3 * this.q3);
        if (squaredSine == 0.0d) {
            return new org.apache.commons.math.geometry.Vector3D(1.0d, 0.0d, 0.0d);
        }
        if (this.q0 < 0.0d) {
            double inverse = 1.0d / org.apache.commons.math.util.FastMath.sqrt(squaredSine);
            return new org.apache.commons.math.geometry.Vector3D(this.q1 * inverse, this.q2 * inverse, this.q3 * inverse);
        }
        double inverse2 = (-1.0d) / org.apache.commons.math.util.FastMath.sqrt(squaredSine);
        return new org.apache.commons.math.geometry.Vector3D(this.q1 * inverse2, this.q2 * inverse2, this.q3 * inverse2);
    }

    public double getAngle() {
        if (this.q0 < -0.1d || this.q0 > 0.1d) {
            return org.apache.commons.math.util.FastMath.asin(org.apache.commons.math.util.FastMath.sqrt((this.q1 * this.q1) + (this.q2 * this.q2) + (this.q3 * this.q3))) * 2.0d;
        }
        if (this.q0 < 0.0d) {
            return org.apache.commons.math.util.FastMath.acos(-this.q0) * 2.0d;
        }
        return org.apache.commons.math.util.FastMath.acos(this.q0) * 2.0d;
    }

    public double[] getAngles(org.apache.commons.math.geometry.RotationOrder order) throws org.apache.commons.math.geometry.CardanEulerSingularityException {
        if (order == org.apache.commons.math.geometry.RotationOrder.XYZ) {
            org.apache.commons.math.geometry.Vector3D v1 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_K);
            org.apache.commons.math.geometry.Vector3D v2 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_I);
            if (v2.getZ() < -0.9999999999d || v2.getZ() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(true);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(-v1.getY(), v1.getZ()), org.apache.commons.math.util.FastMath.asin(v2.getZ()), org.apache.commons.math.util.FastMath.atan2(-v2.getY(), v2.getX())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.XZY) {
            org.apache.commons.math.geometry.Vector3D v12 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_J);
            org.apache.commons.math.geometry.Vector3D v22 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_I);
            if (v22.getY() < -0.9999999999d || v22.getY() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(true);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(v12.getZ(), v12.getY()), -org.apache.commons.math.util.FastMath.asin(v22.getY()), org.apache.commons.math.util.FastMath.atan2(v22.getZ(), v22.getX())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.YXZ) {
            org.apache.commons.math.geometry.Vector3D v13 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_K);
            org.apache.commons.math.geometry.Vector3D v23 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_J);
            if (v23.getZ() < -0.9999999999d || v23.getZ() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(true);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(v13.getX(), v13.getZ()), -org.apache.commons.math.util.FastMath.asin(v23.getZ()), org.apache.commons.math.util.FastMath.atan2(v23.getX(), v23.getY())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.YZX) {
            org.apache.commons.math.geometry.Vector3D v14 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_I);
            org.apache.commons.math.geometry.Vector3D v24 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_J);
            if (v24.getX() < -0.9999999999d || v24.getX() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(true);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(-v14.getZ(), v14.getX()), org.apache.commons.math.util.FastMath.asin(v24.getX()), org.apache.commons.math.util.FastMath.atan2(-v24.getZ(), v24.getY())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.ZXY) {
            org.apache.commons.math.geometry.Vector3D v15 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_J);
            org.apache.commons.math.geometry.Vector3D v25 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_K);
            if (v25.getY() < -0.9999999999d || v25.getY() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(true);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(-v15.getX(), v15.getY()), org.apache.commons.math.util.FastMath.asin(v25.getY()), org.apache.commons.math.util.FastMath.atan2(-v25.getX(), v25.getZ())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.ZYX) {
            org.apache.commons.math.geometry.Vector3D v16 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_I);
            org.apache.commons.math.geometry.Vector3D v26 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_K);
            if (v26.getX() < -0.9999999999d || v26.getX() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(true);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(v16.getY(), v16.getX()), -org.apache.commons.math.util.FastMath.asin(v26.getX()), org.apache.commons.math.util.FastMath.atan2(v26.getY(), v26.getZ())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.XYX) {
            org.apache.commons.math.geometry.Vector3D v17 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_I);
            org.apache.commons.math.geometry.Vector3D v27 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_I);
            if (v27.getX() < -0.9999999999d || v27.getX() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(false);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(v17.getY(), -v17.getZ()), org.apache.commons.math.util.FastMath.acos(v27.getX()), org.apache.commons.math.util.FastMath.atan2(v27.getY(), v27.getZ())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.XZX) {
            org.apache.commons.math.geometry.Vector3D v18 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_I);
            org.apache.commons.math.geometry.Vector3D v28 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_I);
            if (v28.getX() < -0.9999999999d || v28.getX() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(false);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(v18.getZ(), v18.getY()), org.apache.commons.math.util.FastMath.acos(v28.getX()), org.apache.commons.math.util.FastMath.atan2(v28.getZ(), -v28.getY())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.YXY) {
            org.apache.commons.math.geometry.Vector3D v19 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_J);
            org.apache.commons.math.geometry.Vector3D v29 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_J);
            if (v29.getY() < -0.9999999999d || v29.getY() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(false);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(v19.getX(), v19.getZ()), org.apache.commons.math.util.FastMath.acos(v29.getY()), org.apache.commons.math.util.FastMath.atan2(v29.getX(), -v29.getZ())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.YZY) {
            org.apache.commons.math.geometry.Vector3D v110 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_J);
            org.apache.commons.math.geometry.Vector3D v210 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_J);
            if (v210.getY() < -0.9999999999d || v210.getY() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(false);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(v110.getZ(), -v110.getX()), org.apache.commons.math.util.FastMath.acos(v210.getY()), org.apache.commons.math.util.FastMath.atan2(v210.getZ(), v210.getX())};
        }
        if (order == org.apache.commons.math.geometry.RotationOrder.ZXZ) {
            org.apache.commons.math.geometry.Vector3D v111 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_K);
            org.apache.commons.math.geometry.Vector3D v211 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_K);
            if (v211.getZ() < -0.9999999999d || v211.getZ() > 0.9999999999d) {
                throw new org.apache.commons.math.geometry.CardanEulerSingularityException(false);
            }
            return new double[]{org.apache.commons.math.util.FastMath.atan2(v111.getX(), -v111.getY()), org.apache.commons.math.util.FastMath.acos(v211.getZ()), org.apache.commons.math.util.FastMath.atan2(v211.getX(), v211.getY())};
        }
        org.apache.commons.math.geometry.Vector3D v112 = applyTo(org.apache.commons.math.geometry.Vector3D.PLUS_K);
        org.apache.commons.math.geometry.Vector3D v212 = applyInverseTo(org.apache.commons.math.geometry.Vector3D.PLUS_K);
        if (v212.getZ() < -0.9999999999d || v212.getZ() > 0.9999999999d) {
            throw new org.apache.commons.math.geometry.CardanEulerSingularityException(false);
        }
        return new double[]{org.apache.commons.math.util.FastMath.atan2(v112.getY(), v112.getX()), org.apache.commons.math.util.FastMath.acos(v212.getZ()), org.apache.commons.math.util.FastMath.atan2(v212.getY(), -v212.getX())};
    }

    public double[][] getMatrix() {
        double q0q0 = this.q0 * this.q0;
        double q0q1 = this.q0 * this.q1;
        double q0q2 = this.q0 * this.q2;
        double q0q3 = this.q0 * this.q3;
        double q1q1 = this.q1 * this.q1;
        double q1q2 = this.q1 * this.q2;
        double q1q3 = this.q1 * this.q3;
        double q2q2 = this.q2 * this.q2;
        double d = this.q2;
        double q2q22 = this.q3;
        double q2q3 = d * q2q22;
        double q3q3 = this.q3 * this.q3;
        double[][] m = {new double[3], new double[3], new double[3]};
        m[0][0] = ((q0q0 + q1q1) * 2.0d) - 1.0d;
        m[1][0] = (q1q2 - q0q3) * 2.0d;
        m[2][0] = (q1q3 + q0q2) * 2.0d;
        m[0][1] = (q1q2 + q0q3) * 2.0d;
        m[1][1] = ((q0q0 + q2q2) * 2.0d) - 1.0d;
        m[2][1] = (q2q3 - q0q1) * 2.0d;
        m[0][2] = (q1q3 - q0q2) * 2.0d;
        m[1][2] = (q2q3 + q0q1) * 2.0d;
        m[2][2] = ((q0q0 + q3q3) * 2.0d) - 1.0d;
        return m;
    }

    public org.apache.commons.math.geometry.Vector3D applyTo(org.apache.commons.math.geometry.Vector3D u) {
        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();
        double s = (this.q1 * x) + (this.q2 * y) + (this.q3 * z);
        return new org.apache.commons.math.geometry.Vector3D((((this.q0 * ((this.q0 * x) - ((this.q2 * z) - (this.q3 * y)))) + (this.q1 * s)) * 2.0d) - x, (((this.q0 * ((this.q0 * y) - ((this.q3 * x) - (this.q1 * z)))) + (this.q2 * s)) * 2.0d) - y, (((this.q0 * ((this.q0 * z) - ((this.q1 * y) - (this.q2 * x)))) + (this.q3 * s)) * 2.0d) - z);
    }

    public org.apache.commons.math.geometry.Vector3D applyInverseTo(org.apache.commons.math.geometry.Vector3D u) {
        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();
        double s = (this.q1 * x) + (this.q2 * y) + (this.q3 * z);
        double m0 = -this.q0;
        return new org.apache.commons.math.geometry.Vector3D((((((x * m0) - ((this.q2 * z) - (this.q3 * y))) * m0) + (this.q1 * s)) * 2.0d) - x, (((((y * m0) - ((this.q3 * x) - (this.q1 * z))) * m0) + (this.q2 * s)) * 2.0d) - y, (((((z * m0) - ((this.q1 * y) - (this.q2 * x))) * m0) + (this.q3 * s)) * 2.0d) - z);
    }

    public org.apache.commons.math.geometry.Rotation applyTo(org.apache.commons.math.geometry.Rotation r) {
        return new org.apache.commons.math.geometry.Rotation((r.q0 * this.q0) - (((r.q1 * this.q1) + (r.q2 * this.q2)) + (r.q3 * this.q3)), (r.q1 * this.q0) + (r.q0 * this.q1) + ((r.q2 * this.q3) - (r.q3 * this.q2)), (r.q2 * this.q0) + (r.q0 * this.q2) + ((r.q3 * this.q1) - (r.q1 * this.q3)), ((r.q1 * this.q2) - (r.q2 * this.q1)) + (r.q3 * this.q0) + (r.q0 * this.q3), false);
    }

    public org.apache.commons.math.geometry.Rotation applyInverseTo(org.apache.commons.math.geometry.Rotation r) {
        return new org.apache.commons.math.geometry.Rotation(((-r.q0) * this.q0) - (((r.q1 * this.q1) + (r.q2 * this.q2)) + (r.q3 * this.q3)), ((-r.q1) * this.q0) + (r.q0 * this.q1) + ((r.q2 * this.q3) - (r.q3 * this.q2)), ((-r.q2) * this.q0) + (r.q0 * this.q2) + ((r.q3 * this.q1) - (r.q1 * this.q3)), ((r.q1 * this.q2) - (r.q2 * this.q1)) + ((-r.q3) * this.q0) + (r.q0 * this.q3), false);
    }

    private double[][] orthogonalizeMatrix(double[][] m, double threshold) throws org.apache.commons.math.geometry.NotARotationMatrixException {
        double[] m0 = m[0];
        double[] m1 = m[1];
        double[] m2 = m[2];
        double x00 = m0[0];
        double x01 = m0[1];
        double x02 = m0[2];
        double x10 = m1[0];
        double x11 = m1[1];
        double x12 = m1[2];
        double x20 = m2[0];
        double x21 = m2[1];
        double x22 = m2[2];
        double fn = 0.0d;
        double[][] o = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, 3, 3);
        double[] o0 = o[0];
        double[] o1 = o[1];
        double[] o2 = o[2];
        int i = 0;
        while (true) {
            double[][] o3 = o;
            int i2 = i + 1;
            if (i2 < 11) {
                double mx00 = (m0[0] * x00) + (m1[0] * x10) + (m2[0] * x20);
                double mx10 = (m0[1] * x00) + (m1[1] * x10) + (m2[1] * x20);
                double mx20 = (m0[2] * x00) + (m1[2] * x10) + (m2[2] * x20);
                double mx01 = (m0[0] * x01) + (m1[0] * x11) + (m2[0] * x21);
                double mx11 = (m0[1] * x01) + (m1[1] * x11) + (m2[1] * x21);
                double mx21 = (m0[2] * x01) + (m1[2] * x11) + (m2[2] * x21);
                double mx02 = (m0[0] * x02) + (m1[0] * x12) + (m2[0] * x22);
                double mx12 = (m0[1] * x02) + (m1[1] * x12) + (m2[1] * x22);
                double mx22 = (m0[2] * x02) + (m1[2] * x12) + (m2[2] * x22);
                o0[0] = x00 - (((((x00 * mx00) + (x01 * mx10)) + (x02 * mx20)) - m0[0]) * 0.5d);
                o0[1] = x01 - (((((x00 * mx01) + (x01 * mx11)) + (x02 * mx21)) - m0[1]) * 0.5d);
                o0[2] = x02 - (((((x00 * mx02) + (x01 * mx12)) + (x02 * mx22)) - m0[2]) * 0.5d);
                o1[0] = x10 - (((((x10 * mx00) + (x11 * mx10)) + (x12 * mx20)) - m1[0]) * 0.5d);
                o1[1] = x11 - (((((x10 * mx01) + (x11 * mx11)) + (x12 * mx21)) - m1[1]) * 0.5d);
                o1[2] = x12 - (((((x10 * mx02) + (x11 * mx12)) + (x12 * mx22)) - m1[2]) * 0.5d);
                o2[0] = x20 - (((((x20 * mx00) + (x21 * mx10)) + (x22 * mx20)) - m2[0]) * 0.5d);
                o2[1] = x21 - (((((x20 * mx01) + (x21 * mx11)) + (x22 * mx21)) - m2[1]) * 0.5d);
                o2[2] = x22 - (((((x20 * mx02) + (x21 * mx12)) + (x22 * mx22)) - m2[2]) * 0.5d);
                double corr00 = o0[0] - m0[0];
                double corr01 = o0[1] - m0[1];
                double corr02 = o0[2] - m0[2];
                double corr10 = o1[0] - m1[0];
                double corr11 = o1[1] - m1[1];
                double corr12 = o1[2] - m1[2];
                double corr20 = o2[0] - m2[0];
                double corr21 = o2[1] - m2[1];
                double corr22 = o2[2] - m2[2];
                double fn1 = (corr00 * corr00) + (corr01 * corr01) + (corr02 * corr02) + (corr10 * corr10) + (corr11 * corr11) + (corr12 * corr12) + (corr20 * corr20) + (corr21 * corr21) + (corr22 * corr22);
                if (org.apache.commons.math.util.FastMath.abs(fn1 - fn) <= threshold) {
                    return o3;
                }
                x00 = o0[0];
                x01 = o0[1];
                x02 = o0[2];
                x10 = o1[0];
                x11 = o1[1];
                x12 = o1[2];
                x20 = o2[0];
                x21 = o2[1];
                x22 = o2[2];
                fn = fn1;
                i = i2;
                o = o3;
            } else {
                throw new org.apache.commons.math.geometry.NotARotationMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_ORTHOGONOLIZE_MATRIX, java.lang.Integer.valueOf(i2 - 1));
            }
        }
    }

    public static double distance(org.apache.commons.math.geometry.Rotation r1, org.apache.commons.math.geometry.Rotation r2) {
        return r1.applyInverseTo(r2).getAngle();
    }
}
