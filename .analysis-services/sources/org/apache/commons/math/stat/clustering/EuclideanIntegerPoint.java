package org.apache.commons.math.stat.clustering;

/* JADX INFO: loaded from: classes4.dex */
public class EuclideanIntegerPoint implements org.apache.commons.math.stat.clustering.Clusterable<org.apache.commons.math.stat.clustering.EuclideanIntegerPoint>, java.io.Serializable {
    private static final long serialVersionUID = 3946024775784901369L;
    private final int[] point;

    public EuclideanIntegerPoint(int[] point) {
        this.point = point;
    }

    public int[] getPoint() {
        return this.point;
    }

    @Override // org.apache.commons.math.stat.clustering.Clusterable
    public double distanceFrom(org.apache.commons.math.stat.clustering.EuclideanIntegerPoint p) {
        return org.apache.commons.math.util.MathUtils.distance(this.point, p.getPoint());
    }

    @Override // org.apache.commons.math.stat.clustering.Clusterable
    public org.apache.commons.math.stat.clustering.EuclideanIntegerPoint centroidOf(java.util.Collection<org.apache.commons.math.stat.clustering.EuclideanIntegerPoint> points) {
        int[] centroid = new int[getPoint().length];
        for (org.apache.commons.math.stat.clustering.EuclideanIntegerPoint p : points) {
            for (int i = 0; i < centroid.length; i++) {
                centroid[i] = centroid[i] + p.getPoint()[i];
            }
        }
        for (int i2 = 0; i2 < centroid.length; i2++) {
            centroid[i2] = centroid[i2] / points.size();
        }
        return new org.apache.commons.math.stat.clustering.EuclideanIntegerPoint(centroid);
    }

    public boolean equals(java.lang.Object other) {
        if (!(other instanceof org.apache.commons.math.stat.clustering.EuclideanIntegerPoint)) {
            return false;
        }
        int[] otherPoint = ((org.apache.commons.math.stat.clustering.EuclideanIntegerPoint) other).getPoint();
        if (this.point.length != otherPoint.length) {
            return false;
        }
        for (int i = 0; i < this.point.length; i++) {
            if (this.point[i] != otherPoint[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hashCode = 0;
        for (int i : this.point) {
            java.lang.Integer i2 = java.lang.Integer.valueOf(i);
            hashCode += (i2.hashCode() * 13) + 7;
        }
        return hashCode;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder buff = new java.lang.StringBuilder("(");
        int[] coordinates = getPoint();
        for (int i = 0; i < coordinates.length; i++) {
            buff.append(coordinates[i]);
            if (i < coordinates.length - 1) {
                buff.append(",");
            }
        }
        buff.append(")");
        return buff.toString();
    }
}
