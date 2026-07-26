package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public class VectorialPointValuePair implements java.io.Serializable {
    private static final long serialVersionUID = 1003888396256744753L;
    private final double[] point;
    private final double[] value;

    public VectorialPointValuePair(double[] point, double[] value) {
        this.point = point == null ? null : (double[]) point.clone();
        this.value = value != null ? (double[]) value.clone() : null;
    }

    public VectorialPointValuePair(double[] point, double[] value, boolean copyArray) {
        double[] dArr;
        double[] dArr2 = null;
        if (copyArray) {
            dArr = point == null ? null : (double[]) point.clone();
        } else {
            dArr = point;
        }
        this.point = dArr;
        if (copyArray) {
            if (value != null) {
                dArr2 = (double[]) value.clone();
            }
        } else {
            dArr2 = value;
        }
        this.value = dArr2;
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

    public double[] getValue() {
        if (this.value == null) {
            return null;
        }
        return (double[]) this.value.clone();
    }

    public double[] getValueRef() {
        return this.value;
    }
}
