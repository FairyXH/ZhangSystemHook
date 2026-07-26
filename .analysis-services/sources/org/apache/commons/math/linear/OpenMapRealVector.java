package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class OpenMapRealVector extends org.apache.commons.math.linear.AbstractRealVector implements org.apache.commons.math.linear.SparseRealVector, java.io.Serializable {
    public static final double DEFAULT_ZERO_TOLERANCE = 1.0E-12d;
    private static final long serialVersionUID = 8772222695580707260L;
    private final org.apache.commons.math.util.OpenIntToDoubleHashMap entries;
    private final double epsilon;
    private final int virtualSize;

    public OpenMapRealVector() {
        this(0, 1.0E-12d);
    }

    public OpenMapRealVector(int dimension) {
        this(dimension, 1.0E-12d);
    }

    public OpenMapRealVector(int dimension, double epsilon) {
        this.virtualSize = dimension;
        this.entries = new org.apache.commons.math.util.OpenIntToDoubleHashMap(0.0d);
        this.epsilon = epsilon;
    }

    protected OpenMapRealVector(org.apache.commons.math.linear.OpenMapRealVector v, int resize) {
        this.virtualSize = v.getDimension() + resize;
        this.entries = new org.apache.commons.math.util.OpenIntToDoubleHashMap(v.entries);
        this.epsilon = v.epsilon;
    }

    public OpenMapRealVector(int dimension, int expectedSize) {
        this(dimension, expectedSize, 1.0E-12d);
    }

    public OpenMapRealVector(int dimension, int expectedSize, double epsilon) {
        this.virtualSize = dimension;
        this.entries = new org.apache.commons.math.util.OpenIntToDoubleHashMap(expectedSize, 0.0d);
        this.epsilon = epsilon;
    }

    public OpenMapRealVector(double[] values) {
        this(values, 1.0E-12d);
    }

    public OpenMapRealVector(double[] values, double epsilon) {
        this.virtualSize = values.length;
        this.entries = new org.apache.commons.math.util.OpenIntToDoubleHashMap(0.0d);
        this.epsilon = epsilon;
        for (int key = 0; key < values.length; key++) {
            double value = values[key];
            if (!isDefaultValue(value)) {
                this.entries.put(key, value);
            }
        }
    }

    public OpenMapRealVector(java.lang.Double[] values) {
        this(values, 1.0E-12d);
    }

    public OpenMapRealVector(java.lang.Double[] values, double epsilon) {
        this.virtualSize = values.length;
        this.entries = new org.apache.commons.math.util.OpenIntToDoubleHashMap(0.0d);
        this.epsilon = epsilon;
        for (int key = 0; key < values.length; key++) {
            double value = values[key].doubleValue();
            if (!isDefaultValue(value)) {
                this.entries.put(key, value);
            }
        }
    }

    public OpenMapRealVector(org.apache.commons.math.linear.OpenMapRealVector v) {
        this.virtualSize = v.getDimension();
        this.entries = new org.apache.commons.math.util.OpenIntToDoubleHashMap(v.getEntries());
        this.epsilon = v.epsilon;
    }

    public OpenMapRealVector(org.apache.commons.math.linear.RealVector v) {
        this.virtualSize = v.getDimension();
        this.entries = new org.apache.commons.math.util.OpenIntToDoubleHashMap(0.0d);
        this.epsilon = 1.0E-12d;
        for (int key = 0; key < this.virtualSize; key++) {
            double value = v.getEntry(key);
            if (!isDefaultValue(value)) {
                this.entries.put(key, value);
            }
        }
    }

    private org.apache.commons.math.util.OpenIntToDoubleHashMap getEntries() {
        return this.entries;
    }

    protected boolean isDefaultValue(double value) {
        return org.apache.commons.math.util.FastMath.abs(value) < this.epsilon;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector add(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        if (v instanceof org.apache.commons.math.linear.OpenMapRealVector) {
            return add((org.apache.commons.math.linear.OpenMapRealVector) v);
        }
        return super.add(v);
    }

    public org.apache.commons.math.linear.OpenMapRealVector add(org.apache.commons.math.linear.OpenMapRealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        boolean copyThis = this.entries.size() > v.entries.size();
        org.apache.commons.math.linear.OpenMapRealVector res = copyThis ? copy() : v.copy();
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = (copyThis ? v.entries : this.entries).iterator();
        org.apache.commons.math.util.OpenIntToDoubleHashMap randomAccess = copyThis ? this.entries : v.entries;
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (randomAccess.containsKey(key)) {
                res.setEntry(key, randomAccess.get(key) + iter.value());
            } else {
                res.setEntry(key, iter.value());
            }
        }
        return res;
    }

    public org.apache.commons.math.linear.OpenMapRealVector append(org.apache.commons.math.linear.OpenMapRealVector v) {
        org.apache.commons.math.linear.OpenMapRealVector res = new org.apache.commons.math.linear.OpenMapRealVector(this, v.getDimension());
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = v.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key() + this.virtualSize, iter.value());
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector append(org.apache.commons.math.linear.RealVector v) {
        if (v instanceof org.apache.commons.math.linear.OpenMapRealVector) {
            return append((org.apache.commons.math.linear.OpenMapRealVector) v);
        }
        return append(v.getData());
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector append(double d) {
        org.apache.commons.math.linear.OpenMapRealVector res = new org.apache.commons.math.linear.OpenMapRealVector(this, 1);
        res.setEntry(this.virtualSize, d);
        return res;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector append(double[] a) {
        org.apache.commons.math.linear.OpenMapRealVector res = new org.apache.commons.math.linear.OpenMapRealVector(this, a.length);
        for (int i = 0; i < a.length; i++) {
            res.setEntry(this.virtualSize + i, a[i]);
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector copy() {
        return new org.apache.commons.math.linear.OpenMapRealVector(this);
    }

    public double dotProduct(org.apache.commons.math.linear.OpenMapRealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        boolean thisIsSmaller = this.entries.size() < v.entries.size();
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = (thisIsSmaller ? this.entries : v.entries).iterator();
        org.apache.commons.math.util.OpenIntToDoubleHashMap larger = thisIsSmaller ? v.entries : this.entries;
        double d = 0.0d;
        while (iter.hasNext()) {
            iter.advance();
            d += iter.value() * larger.get(iter.key());
        }
        return d;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double dotProduct(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.OpenMapRealVector) {
            return dotProduct((org.apache.commons.math.linear.OpenMapRealVector) v);
        }
        return super.dotProduct(v);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector ebeDivide(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        org.apache.commons.math.linear.OpenMapRealVector res = new org.apache.commons.math.linear.OpenMapRealVector(this);
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), iter.value() / v.getEntry(iter.key()));
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector ebeDivide(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.linear.OpenMapRealVector res = new org.apache.commons.math.linear.OpenMapRealVector(this);
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), iter.value() / v[iter.key()]);
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector ebeMultiply(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        org.apache.commons.math.linear.OpenMapRealVector res = new org.apache.commons.math.linear.OpenMapRealVector(this);
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), iter.value() * v.getEntry(iter.key()));
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector ebeMultiply(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.linear.OpenMapRealVector res = new org.apache.commons.math.linear.OpenMapRealVector(this);
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), iter.value() * v[iter.key()]);
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector getSubVector(int index, int n) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        checkIndex((index + n) - 1);
        org.apache.commons.math.linear.OpenMapRealVector res = new org.apache.commons.math.linear.OpenMapRealVector(n);
        int end = index + n;
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (key >= index && key < end) {
                res.setEntry(key - index, iter.value());
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double[] getData() {
        double[] res = new double[this.virtualSize];
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res[iter.key()] = iter.value();
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public int getDimension() {
        return this.virtualSize;
    }

    public double getDistance(org.apache.commons.math.linear.OpenMapRealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        double res = 0.0d;
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            double delta = iter.value() - v.getEntry(key);
            res += delta * delta;
        }
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter2 = v.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            int key2 = iter2.key();
            if (!this.entries.containsKey(key2)) {
                double value = iter2.value();
                res += value * value;
            }
        }
        return org.apache.commons.math.util.FastMath.sqrt(res);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getDistance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        if (v instanceof org.apache.commons.math.linear.OpenMapRealVector) {
            return getDistance((org.apache.commons.math.linear.OpenMapRealVector) v);
        }
        return getDistance(v.getData());
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getDistance(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double res = 0.0d;
        for (int i = 0; i < v.length; i++) {
            double delta = this.entries.get(i) - v[i];
            res += delta * delta;
        }
        return org.apache.commons.math.util.FastMath.sqrt(res);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getEntry(int index) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        return this.entries.get(index);
    }

    public double getL1Distance(org.apache.commons.math.linear.OpenMapRealVector v) {
        double max = 0.0d;
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            double delta = org.apache.commons.math.util.FastMath.abs(iter.value() - v.getEntry(iter.key()));
            max += delta;
        }
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter2 = v.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            int key = iter2.key();
            if (!this.entries.containsKey(key)) {
                double delta2 = org.apache.commons.math.util.FastMath.abs(iter2.value());
                max += org.apache.commons.math.util.FastMath.abs(delta2);
            }
        }
        return max;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getL1Distance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        if (v instanceof org.apache.commons.math.linear.OpenMapRealVector) {
            return getL1Distance((org.apache.commons.math.linear.OpenMapRealVector) v);
        }
        return getL1Distance(v.getData());
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getL1Distance(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double max = 0.0d;
        for (int i = 0; i < v.length; i++) {
            double delta = org.apache.commons.math.util.FastMath.abs(getEntry(i) - v[i]);
            max += delta;
        }
        return max;
    }

    private double getLInfDistance(org.apache.commons.math.linear.OpenMapRealVector v) {
        double max = 0.0d;
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            double delta = org.apache.commons.math.util.FastMath.abs(iter.value() - v.getEntry(iter.key()));
            if (delta > max) {
                max = delta;
            }
        }
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter2 = v.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            int key = iter2.key();
            if (!this.entries.containsKey(key) && iter2.value() > max) {
                max = iter2.value();
            }
        }
        return max;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getLInfDistance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        if (v instanceof org.apache.commons.math.linear.OpenMapRealVector) {
            return getLInfDistance((org.apache.commons.math.linear.OpenMapRealVector) v);
        }
        return getLInfDistance(v.getData());
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double getLInfDistance(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        double max = 0.0d;
        for (int i = 0; i < v.length; i++) {
            double delta = org.apache.commons.math.util.FastMath.abs(getEntry(i) - v[i]);
            if (delta > max) {
                max = delta;
            }
        }
        return max;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public boolean isInfinite() {
        boolean infiniteFound = false;
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            double value = iter.value();
            if (java.lang.Double.isNaN(value)) {
                return false;
            }
            if (java.lang.Double.isInfinite(value)) {
                infiniteFound = true;
            }
        }
        return infiniteFound;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public boolean isNaN() {
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            if (java.lang.Double.isNaN(iter.value())) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector mapAdd(double d) {
        return copy().mapAddToSelf(d);
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector mapAddToSelf(double d) {
        for (int i = 0; i < this.virtualSize; i++) {
            setEntry(i, getEntry(i) + d);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealMatrix outerProduct(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.linear.RealMatrix res = new org.apache.commons.math.linear.OpenMapRealMatrix(this.virtualSize, this.virtualSize);
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int row = iter.key();
            double value = iter.value();
            for (int col = 0; col < this.virtualSize; col++) {
                res.setEntry(row, col, v[col] * value);
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector projection(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        return v.mapMultiply(dotProduct(v) / v.dotProduct(v));
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector projection(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        return (org.apache.commons.math.linear.OpenMapRealVector) projection(new org.apache.commons.math.linear.OpenMapRealVector(v));
    }

    @Override // org.apache.commons.math.linear.RealVector
    public void setEntry(int index, double value) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        if (!isDefaultValue(value)) {
            this.entries.put(index, value);
        } else if (this.entries.containsKey(index)) {
            this.entries.remove(index);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public void setSubVector(int index, org.apache.commons.math.linear.RealVector v) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        checkIndex((v.getDimension() + index) - 1);
        setSubVector(index, v.getData());
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public void setSubVector(int index, double[] v) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        checkIndex((v.length + index) - 1);
        for (int i = 0; i < v.length; i++) {
            setEntry(i + index, v[i]);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public void set(double value) {
        for (int i = 0; i < this.virtualSize; i++) {
            setEntry(i, value);
        }
    }

    public org.apache.commons.math.linear.OpenMapRealVector subtract(org.apache.commons.math.linear.OpenMapRealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        org.apache.commons.math.linear.OpenMapRealVector res = copy();
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = v.getEntries().iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (this.entries.containsKey(key)) {
                res.setEntry(key, this.entries.get(key) - iter.value());
            } else {
                res.setEntry(key, -iter.value());
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector subtract(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        if (v instanceof org.apache.commons.math.linear.OpenMapRealVector) {
            return subtract((org.apache.commons.math.linear.OpenMapRealVector) v);
        }
        return subtract(v.getData());
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector subtract(double[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.linear.OpenMapRealVector res = new org.apache.commons.math.linear.OpenMapRealVector(this);
        for (int i = 0; i < v.length; i++) {
            if (this.entries.containsKey(i)) {
                res.setEntry(i, this.entries.get(i) - v[i]);
            } else {
                res.setEntry(i, -v[i]);
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.OpenMapRealVector unitVector() {
        org.apache.commons.math.linear.OpenMapRealVector res = copy();
        res.unitize();
        return res;
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public void unitize() {
        double norm = getNorm();
        if (isDefaultValue(norm)) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new java.lang.Object[0]);
        }
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            this.entries.put(iter.key(), iter.value() / norm);
        }
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public double[] toArray() {
        return getData();
    }

    public int hashCode() {
        long temp = java.lang.Double.doubleToLongBits(this.epsilon);
        int result = (1 * 31) + ((int) ((temp >>> 32) ^ temp));
        int result2 = (result * 31) + this.virtualSize;
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            long temp2 = java.lang.Double.doubleToLongBits(iter.value());
            result2 = (result2 * 31) + ((int) ((temp2 >> 32) ^ temp2));
        }
        return result2;
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof org.apache.commons.math.linear.OpenMapRealVector)) {
            return false;
        }
        org.apache.commons.math.linear.OpenMapRealVector other = (org.apache.commons.math.linear.OpenMapRealVector) obj;
        if (this.virtualSize != other.virtualSize || java.lang.Double.doubleToLongBits(this.epsilon) != java.lang.Double.doubleToLongBits(other.epsilon)) {
            return false;
        }
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            double test = other.getEntry(iter.key());
            if (java.lang.Double.doubleToLongBits(test) != java.lang.Double.doubleToLongBits(iter.value())) {
                return false;
            }
        }
        org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter2 = other.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            double test2 = iter2.value();
            if (java.lang.Double.doubleToLongBits(test2) != java.lang.Double.doubleToLongBits(getEntry(iter2.key()))) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Deprecated
    public double getSparcity() {
        return getSparsity();
    }

    public double getSparsity() {
        return ((double) this.entries.size()) / ((double) getDimension());
    }

    @Override // org.apache.commons.math.linear.AbstractRealVector, org.apache.commons.math.linear.RealVector
    public java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> sparseIterator() {
        return new org.apache.commons.math.linear.OpenMapRealVector.OpenMapSparseIterator();
    }

    protected class OpenMapEntry extends org.apache.commons.math.linear.RealVector.Entry {
        private final org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter;

        protected OpenMapEntry(org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter) {
            this.iter = iter;
        }

        @Override // org.apache.commons.math.linear.RealVector.Entry
        public double getValue() {
            return this.iter.value();
        }

        @Override // org.apache.commons.math.linear.RealVector.Entry
        public void setValue(double value) {
            org.apache.commons.math.linear.OpenMapRealVector.this.entries.put(this.iter.key(), value);
        }

        @Override // org.apache.commons.math.linear.RealVector.Entry
        public int getIndex() {
            return this.iter.key();
        }
    }

    protected class OpenMapSparseIterator implements java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> {
        private final org.apache.commons.math.linear.RealVector.Entry current;
        private final org.apache.commons.math.util.OpenIntToDoubleHashMap.Iterator iter;

        protected OpenMapSparseIterator() {
            this.iter = org.apache.commons.math.linear.OpenMapRealVector.this.entries.iterator();
            this.current = org.apache.commons.math.linear.OpenMapRealVector.this.new OpenMapEntry(this.iter);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        @Override // java.util.Iterator
        public org.apache.commons.math.linear.RealVector.Entry next() {
            this.iter.advance();
            return this.current;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Not supported");
        }
    }
}
