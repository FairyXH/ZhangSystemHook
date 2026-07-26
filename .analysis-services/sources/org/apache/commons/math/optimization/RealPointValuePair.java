package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public class RealPointValuePair implements java.io.Serializable {
    private static final long serialVersionUID = 1003888396256744753L;
    private final double[] point;
    private final double value;

    public RealPointValuePair(double[] point, double value) {
        this.point = point == null ? null : (double[]) point.clone();
        this.value = value;
    }

    public RealPointValuePair(double[] point, double value, boolean copyArray) {
        double[] dArr;
        if (copyArray) {
            dArr = point == null ? null : (double[]) point.clone();
        } else {
            dArr = point;
        }
        this.point = dArr;
        this.value = value;
    }

    public double[] getPoint() {
        if (this.point == null) {
            return null;
        }
        return (double[]) this.point.clone();
    }

    public double[] getPointRef() {
        return this.point;
    }

    public double getValue() {
        return this.value;
    }
}
