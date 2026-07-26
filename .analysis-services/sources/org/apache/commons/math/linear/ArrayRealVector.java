package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class ArrayRealVector extends org.apache.commons.math.linear.AbstractRealVector implements java.io.Serializable {
    private static final org.apache.commons.math.linear.RealVectorFormat DEFAULT_FORMAT = org.apache.commons.math.linear.RealVectorFormat.getInstance();
    private static final long serialVersionUID = -1097961340710804027L;
    protected double[] data;

    public ArrayRealVector() {
        this.data = new double[0];
    }

    public ArrayRealVector(int size) {
        this.data = new double[size];
    }

    public ArrayRealVector(int size, double preset) {
        this.data = new double[size];
        java.util.Arrays.fill(this.data, preset);
    }

    public ArrayRealVector(double[] d) {
        this.data = (double[]) d.clone();
    }

    public ArrayRealVector(double[] d, boolean copyArray) {
        this.data = copyArray ? (double[]) d.clone() : d;
    }

    public ArrayRealVector(double[] d, int pos, int size) {
        if (d.length < pos + size) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POSITION_SIZE_MISMATCH_INPUT_ARRAY, java.lang.Integer.valueOf(pos), java.lang.Integer.valueOf(size), java.lang.Integer.valueOf(d.length));
        }
        this.data = new double[size];
        java.lang.System.arraycopy(d, pos, this.data, 0, size);
    }

    public ArrayRealVector(java.lang.Double[] d) {
        this.data = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            this.data[i] = d[i].doubleValue();
        }
    }

    public ArrayRealVector(java.lang.Double[] d, int pos, int size) {
        if (d.length < pos + size) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POSITION_SIZE_MISMATCH_INPUT_ARRAY, java.lang.Integer.valueOf(pos), java.lang.Integer.valueOf(size), java.lang.Integer.valueOf(d.length));
        }
        this.data = new double[size];
        for (int i = pos; i < pos + size; i++) {
            this.data[i - pos] = d[i].doubleValue();
        }
    }

    public ArrayRealVector(org.apache.commons.math.linear.RealVector v) {
        this.data = new double[v.getDimension()];
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = v.getEntry(i);
        }
    }

    public ArrayRealVector(org.apache.commons.math.linear.ArrayRealVector v) {
        this(v, true);
    }

    public ArrayRealVector(org.apache.commons.math.linear.ArrayRealVector v, boolean deep) {
        double[] dArr = v.data;
        this.data = deep ? (double[]) dArr.clone() : dArr;
    }

    public ArrayRealVector(org.apache.commons.math.linear.ArrayRealVector v1, org.apache.commons.math.linear.ArrayRealVector v2) {
        this.data = new double[v1.data.length + v2.data.length];
        java.lang.System.arraycopy(v1.data, 0, this.data, 0, v1.data.length);
        java.lang.System.arraycopy(v2.data, 0, this.data, v1.data.length, v2.data.length);
    }

    public ArrayRealVector(org.apache.commons.math.linear.ArrayRealVector v1, org.apache.commons.math.linear.RealVector v2) {
        int l1 = v1.data.length;
        int l2 = v2.getDimension();
        this.data = new double[l1 + l2];
        java.lang.System.arraycopy(v1.data, 0, this.data, 0, l1);
        for (int i = 0; i < l2; i++) {
            this.data[l1 + i] = v2.getEntry(i);
        }
    }

    public ArrayRealVector(org.apache.commons.math.linear.RealVector v1, org.apache.commons.math.linear.ArrayRealVector v2) {
        int l1 = v1.getDimension();
        int l2 = v2.data.length;
        this.data = new double[l1 + l2];
        for (int i = 0; i < l1; i++) {
            this.data[i] = v1.getEntry(i);
        }
        java.lang.System.arraycopy(v2.data, 0, this.data, l1, l2);
    }

    public ArrayRealVector(org.apache.commons.math.linear.ArrayRealVector v1, double[] v2) {
        int l1 = v1.getDimension();
        int l2 = v2.length;
        this.data = new double[l1 + l2];
        java.lang.System.arraycopy(v1.data, 0, this.data, 0, l1);
        java.lang.System.arraycopy(v2, 0, this.data, l1, l2);
    }

    public ArrayRealVector(double[] v1, org.apache.commons.math.linear.ArrayRealVector v2) {
        int l1 = v1.length;
        int l2 = v2.getDimension();
        this.data = new double[l1 + l2];
        java.lang.System.arraycopy(v1, 0, this.data, 0, l1);
        java.lang.System.arraycopy(v2.data, 0, this.data, l1, l2);
    }

    public ArrayRealVector(double[] v1, double[] v2) {
        int l1 = v1.length;
        int l2 = v2.length;
        this.data = new double[l1 + l2];
        java.lang.System.arraycopy(v1, 0, this.data, 0, l1);
        java.lang.System.arraycopy(v2, 0, this.data, l1, l2);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.AbstractRealVector copy() {
        return new org.apache.commons.math.linear.ArrayRealVector(this, true);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector add(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            return add((org.apache.commons.math.linear.ArrayRealVector) v);
        }
        checkVectorDimensions(v);
        double[] out = (double[]) this.data.clone();
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = v.sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            int index = e.getIndex();
            out[index] = out[index] + e.getValue();
        }
        return new org.apache.commons.math.linear.ArrayRealVector(out, false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector add(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double[] out = (double[]) this.data.clone();
        for (int i = 0; i < this.data.length; i++) {
            out[i] = out[i] + v[i];
        }
        return new org.apache.commons.math.linear.ArrayRealVector(out, false);
    }

    public org.apache.commons.math.linear.ArrayRealVector add(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException {
        return (org.apache.commons.math.linear.ArrayRealVector) add(v.data);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector subtract(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            return subtract((org.apache.commons.math.linear.ArrayRealVector) v);
        }
        checkVectorDimensions(v);
        double[] out = (double[]) this.data.clone();
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = v.sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            int index = e.getIndex();
            out[index] = out[index] - e.getValue();
        }
        return new org.apache.commons.math.linear.ArrayRealVector(out, false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector subtract(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double[] out = (double[]) this.data.clone();
        for (int i = 0; i < this.data.length; i++) {
            out[i] = out[i] - v[i];
        }
        return new org.apache.commons.math.linear.ArrayRealVector(out, false);
    }

    public org.apache.commons.math.linear.ArrayRealVector subtract(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException {
        return (org.apache.commons.math.linear.ArrayRealVector) subtract(v.data);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAddToSelf(double d) {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = this.data[i] + d;
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSubtractToSelf(double d) {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = this.data[i] - d;
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapMultiplyToSelf(double d) {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = this.data[i] * d;
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapDivideToSelf(double d) {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = this.data[i] / d;
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapPowToSelf(double d) {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.pow(this.data[i], d);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapExpToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.exp(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapExpm1ToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.expm1(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapLogToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.log(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapLog10ToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.log10(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapLog1pToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.log1p(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCoshToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.cosh(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSinhToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.sinh(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapTanhToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.tanh(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCosToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.cos(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSinToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.sin(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapTanToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.tan(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAcosToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.acos(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAsinToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.asin(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAtanToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.atan(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapInvToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = 1.0d / this.data[i];
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAbsToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.abs(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSqrtToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.sqrt(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCbrtToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.cbrt(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCeilToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.ceil(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapFloorToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.floor(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapRintToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.rint(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSignumToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.signum(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapUlpToSelf() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = org.apache.commons.math.util.FastMath.ulp(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector ebeMultiply(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            return ebeMultiply((org.apache.commons.math.linear.ArrayRealVector) v);
        }
        checkVectorDimensions(v);
        double[] out = (double[]) this.data.clone();
        for (int i = 0; i < this.data.length; i++) {
            out[i] = out[i] * v.getEntry(i);
        }
        return new org.apache.commons.math.linear.ArrayRealVector(out, false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector ebeMultiply(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double[] out = (double[]) this.data.clone();
        for (int i = 0; i < this.data.length; i++) {
            out[i] = out[i] * v[i];
        }
        return new org.apache.commons.math.linear.ArrayRealVector(out, false);
    }

    public org.apache.commons.math.linear.ArrayRealVector ebeMultiply(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException {
        return (org.apache.commons.math.linear.ArrayRealVector) ebeMultiply(v.data);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector ebeDivide(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            return ebeDivide((org.apache.commons.math.linear.ArrayRealVector) v);
        }
        checkVectorDimensions(v);
        double[] out = (double[]) this.data.clone();
        for (int i = 0; i < this.data.length; i++) {
            out[i] = out[i] / v.getEntry(i);
        }
        return new org.apache.commons.math.linear.ArrayRealVector(out, false);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector ebeDivide(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double[] out = (double[]) this.data.clone();
        for (int i = 0; i < this.data.length; i++) {
            out[i] = out[i] / v[i];
        }
        return new org.apache.commons.math.linear.ArrayRealVector(out, false);
    }

    public org.apache.commons.math.linear.ArrayRealVector ebeDivide(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException {
        return (org.apache.commons.math.linear.ArrayRealVector) ebeDivide(v.data);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double[] getData() {
        return (double[]) this.data.clone();
    }

    public double[] getDataRef() {
        return this.data;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double dotProduct(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            return dotProduct((org.apache.commons.math.linear.ArrayRealVector) v);
        }
        checkVectorDimensions(v);
        double dot = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = v.sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            dot += this.data[e.getIndex()] * e.getValue();
        }
        return dot;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double dotProduct(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double dot = 0.0d;
        for (int i = 0; i < this.data.length; i++) {
            dot += this.data[i] * v[i];
        }
        return dot;
    }

    public double dotProduct(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException {
        return dotProduct(v.data);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getNorm() {
        double sum = 0.0d;
        for (double a : this.data) {
            sum += a * a;
        }
        return org.apache.commons.math.util.FastMath.sqrt(sum);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getL1Norm() {
        double sum = 0.0d;
        for (double a : this.data) {
            sum += org.apache.commons.math.util.FastMath.abs(a);
        }
        return sum;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getLInfNorm() {
        double max = 0.0d;
        for (double a : this.data) {
            max = org.apache.commons.math.util.FastMath.max(max, org.apache.commons.math.util.FastMath.abs(a));
        }
        return max;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getDistance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            return getDistance((org.apache.commons.math.linear.ArrayRealVector) v);
        }
        checkVectorDimensions(v);
        double sum = 0.0d;
        for (int i = 0; i < this.data.length; i++) {
            double delta = this.data[i] - v.getEntry(i);
            sum += delta * delta;
        }
        return org.apache.commons.math.util.FastMath.sqrt(sum);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getDistance(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double sum = 0.0d;
        for (int i = 0; i < this.data.length; i++) {
            double delta = this.data[i] - v[i];
            sum += delta * delta;
        }
        return org.apache.commons.math.util.FastMath.sqrt(sum);
    }

    public double getDistance(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException {
        return getDistance(v.data);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getL1Distance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            return getL1Distance((org.apache.commons.math.linear.ArrayRealVector) v);
        }
        checkVectorDimensions(v);
        double sum = 0.0d;
        for (int i = 0; i < this.data.length; i++) {
            double delta = this.data[i] - v.getEntry(i);
            sum += org.apache.commons.math.util.FastMath.abs(delta);
        }
        return sum;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getL1Distance(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double sum = 0.0d;
        for (int i = 0; i < this.data.length; i++) {
            double delta = this.data[i] - v[i];
            sum += org.apache.commons.math.util.FastMath.abs(delta);
        }
        return sum;
    }

    public double getL1Distance(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException {
        return getL1Distance(v.data);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getLInfDistance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            return getLInfDistance((org.apache.commons.math.linear.ArrayRealVector) v);
        }
        checkVectorDimensions(v);
        double max = 0.0d;
        for (int i = 0; i < this.data.length; i++) {
            double delta = this.data[i] - v.getEntry(i);
            max = org.apache.commons.math.util.FastMath.max(max, org.apache.commons.math.util.FastMath.abs(delta));
        }
        return max;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getLInfDistance(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double max = 0.0d;
        for (int i = 0; i < this.data.length; i++) {
            double delta = this.data[i] - v[i];
            max = org.apache.commons.math.util.FastMath.max(max, org.apache.commons.math.util.FastMath.abs(delta));
        }
        return max;
    }

    public double getLInfDistance(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException {
        return getLInfDistance(v.data);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector unitVector() throws java.lang.ArithmeticException {
        double norm = getNorm();
        if (norm == 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_NORM, new java.lang.Object[0]);
        }
        return mapDivide(norm);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public void unitize() throws java.lang.ArithmeticException {
        double norm = getNorm();
        if (norm == 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new java.lang.Object[0]);
        }
        mapDivideToSelf(norm);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector projection(org.apache.commons.math.linear.RealVector v) {
        return v.mapMultiply(dotProduct(v) / v.dotProduct(v));
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector projection(double[] v) {
        return projection(new org.apache.commons.math.linear.ArrayRealVector(v, false));
    }

    public org.apache.commons.math.linear.ArrayRealVector projection(org.apache.commons.math.linear.ArrayRealVector v) {
        return (org.apache.commons.math.linear.ArrayRealVector) v.mapMultiply(dotProduct(v) / v.dotProduct(v));
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealMatrix outerProduct(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            return outerProduct((org.apache.commons.math.linear.ArrayRealVector) v);
        }
        checkVectorDimensions(v);
        int m = this.data.length;
        org.apache.commons.math.linear.RealMatrix out = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data.length; j++) {
                out.setEntry(i, j, this.data[i] * v.getEntry(j));
            }
        }
        return out;
    }

    public org.apache.commons.math.linear.RealMatrix outerProduct(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException {
        return outerProduct(v.data);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealMatrix outerProduct(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        int m = this.data.length;
        org.apache.commons.math.linear.RealMatrix out = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data.length; j++) {
                out.setEntry(i, j, this.data[i] * v[j]);
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getEntry(int index) throws org.apache.commons.math.linear.MatrixIndexException {
        return this.data[index];
    }

    @Override // org.apache.commons.math.linear.RealVector
    public int getDimension() {
        return this.data.length;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector append(org.apache.commons.math.linear.RealVector v) {
        try {
            return new org.apache.commons.math.linear.ArrayRealVector(this, (org.apache.commons.math.linear.ArrayRealVector) v);
        } catch (java.lang.ClassCastException e) {
            return new org.apache.commons.math.linear.ArrayRealVector(this, v);
        }
    }

    public org.apache.commons.math.linear.ArrayRealVector append(org.apache.commons.math.linear.ArrayRealVector v) {
        return new org.apache.commons.math.linear.ArrayRealVector(this, v);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector append(double in) {
        double[] out = new double[this.data.length + 1];
        java.lang.System.arraycopy(this.data, 0, out, 0, this.data.length);
        out[this.data.length] = in;
        return new org.apache.commons.math.linear.ArrayRealVector(out, false);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector append(double[] in) {
        return new org.apache.commons.math.linear.ArrayRealVector(this, in);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector getSubVector(int index, int n) {
        org.apache.commons.math.linear.ArrayRealVector out = new org.apache.commons.math.linear.ArrayRealVector(n);
        try {
            java.lang.System.arraycopy(this.data, index, out.data, 0, n);
        } catch (java.lang.IndexOutOfBoundsException e) {
            checkIndex(index);
            checkIndex((index + n) - 1);
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public void setEntry(int index, double value) {
        try {
            this.data[index] = value;
        } catch (java.lang.IndexOutOfBoundsException e) {
            checkIndex(index);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public void setSubVector(int index, org.apache.commons.math.linear.RealVector v) {
        try {
            try {
                set(index, (org.apache.commons.math.linear.ArrayRealVector) v);
            } catch (java.lang.ClassCastException e) {
                for (int i = index; i < v.getDimension() + index; i++) {
                    this.data[i] = v.getEntry(i - index);
                }
            }
        } catch (java.lang.IndexOutOfBoundsException e2) {
            checkIndex(index);
            checkIndex((v.getDimension() + index) - 1);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public void setSubVector(int index, double[] v) {
        try {
            java.lang.System.arraycopy(v, 0, this.data, index, v.length);
        } catch (java.lang.IndexOutOfBoundsException e) {
            checkIndex(index);
            checkIndex((v.length + index) - 1);
        }
    }

    public void set(int index, org.apache.commons.math.linear.ArrayRealVector v) throws org.apache.commons.math.linear.MatrixIndexException {
        setSubVector(index, v.data);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public void set(double value) {
        java.util.Arrays.fill(this.data, value);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double[] toArray() {
        return (double[]) this.data.clone();
    }

    public java.lang.String toString() {
        return DEFAULT_FORMAT.format(this);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector
    protected void checkVectorDimensions(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector
    protected void checkVectorDimensions(int n) throws java.lang.IllegalArgumentException {
        if (this.data.length != n) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(this.data.length), java.lang.Integer.valueOf(n));
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public boolean isNaN() {
        for (double v : this.data) {
            if (java.lang.Double.isNaN(v)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public boolean isInfinite() {
        if (isNaN()) {
            return false;
        }
        for (double v : this.data) {
            if (java.lang.Double.isInfinite(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof org.apache.commons.math.linear.RealVector)) {
            return false;
        }
        org.apache.commons.math.linear.RealVector rhs = (org.apache.commons.math.linear.RealVector) other;
        if (this.data.length != rhs.getDimension()) {
            return false;
        }
        if (rhs.isNaN()) {
            return isNaN();
        }
        for (int i = 0; i < this.data.length; i++) {
            if (this.data[i] != rhs.getEntry(i)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        if (isNaN()) {
            return 9;
        }
        return org.apache.commons.math.util.MathUtils.hash(this.data);
    }
}
