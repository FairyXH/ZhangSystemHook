package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class SparseFieldVector<T extends org.apache.commons.math.FieldElement<T>> implements org.apache.commons.math.linear.FieldVector<T>, java.io.Serializable {
    private static final long serialVersionUID = 7841233292190413362L;
    private final org.apache.commons.math.util.OpenIntToFieldHashMap<T> entries;
    private final org.apache.commons.math.Field<T> field;
    private final int virtualSize;

    public SparseFieldVector(org.apache.commons.math.Field<T> field) {
        this(field, 0);
    }

    public SparseFieldVector(org.apache.commons.math.Field<T> field, int dimension) {
        this.field = field;
        this.virtualSize = dimension;
        this.entries = new org.apache.commons.math.util.OpenIntToFieldHashMap<>(field);
    }

    protected SparseFieldVector(org.apache.commons.math.linear.SparseFieldVector<T> v, int resize) {
        this.field = v.field;
        this.virtualSize = v.getDimension() + resize;
        this.entries = new org.apache.commons.math.util.OpenIntToFieldHashMap<>(v.entries);
    }

    public SparseFieldVector(org.apache.commons.math.Field<T> field, int dimension, int expectedSize) {
        this.field = field;
        this.virtualSize = dimension;
        this.entries = new org.apache.commons.math.util.OpenIntToFieldHashMap<>(field, expectedSize);
    }

    public SparseFieldVector(org.apache.commons.math.Field<T> field, T[] values) {
        this.field = field;
        this.virtualSize = values.length;
        this.entries = new org.apache.commons.math.util.OpenIntToFieldHashMap<>(field);
        for (int key = 0; key < values.length; key++) {
            T value = values[key];
            this.entries.put(key, value);
        }
    }

    public SparseFieldVector(org.apache.commons.math.linear.SparseFieldVector<T> v) {
        this.field = v.field;
        this.virtualSize = v.getDimension();
        this.entries = new org.apache.commons.math.util.OpenIntToFieldHashMap<>(v.getEntries());
    }

    private org.apache.commons.math.util.OpenIntToFieldHashMap<T> getEntries() {
        return this.entries;
    }

    public org.apache.commons.math.linear.FieldVector<T> add(org.apache.commons.math.linear.SparseFieldVector<T> v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        org.apache.commons.math.linear.SparseFieldVector<T> res = (org.apache.commons.math.linear.SparseFieldVector) copy();
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = v.getEntries().iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            org.apache.commons.math.FieldElement fieldElementValue = iter.value();
            if (this.entries.containsKey(key)) {
                res.setEntry(key, (org.apache.commons.math.FieldElement) this.entries.get(key).add(fieldElementValue));
            } else {
                res.setEntry(key, fieldElementValue);
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> add(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.linear.SparseFieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this.field, getDimension());
        for (int i = 0; i < v.length; i++) {
            res.setEntry(i, (org.apache.commons.math.FieldElement) v[i].add(getEntry(i)));
        }
        return res;
    }

    public org.apache.commons.math.linear.FieldVector<T> append(org.apache.commons.math.linear.SparseFieldVector<T> v) {
        org.apache.commons.math.linear.SparseFieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this, v.getDimension());
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = v.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key() + this.virtualSize, iter.value());
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> append(org.apache.commons.math.linear.FieldVector<T> v) {
        if (v instanceof org.apache.commons.math.linear.SparseFieldVector) {
            return append((org.apache.commons.math.linear.SparseFieldVector) v);
        }
        return append(v.toArray());
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> append(T d) {
        org.apache.commons.math.linear.FieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this, 1);
        res.setEntry(this.virtualSize, d);
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> append(T[] a) {
        org.apache.commons.math.linear.FieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this, a.length);
        for (int i = 0; i < a.length; i++) {
            res.setEntry(this.virtualSize + i, a[i]);
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> copy() {
        return new org.apache.commons.math.linear.SparseFieldVector(this);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public T dotProduct(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        T zero = this.field.getZero();
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            zero = (T) zero.add((org.apache.commons.math.FieldElement) v.getEntry(iter.key()).multiply(iter.value()));
        }
        return zero;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public T dotProduct(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        T zero = this.field.getZero();
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            int idx = iter.key();
            T value = this.field.getZero();
            if (idx < v.length) {
                value = v[idx];
            }
            zero = (T) zero.add((org.apache.commons.math.FieldElement) value.multiply(iter.value()));
        }
        return zero;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> ebeDivide(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        org.apache.commons.math.linear.SparseFieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this);
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), (org.apache.commons.math.FieldElement) iter.value().divide(v.getEntry(iter.key())));
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> ebeDivide(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.linear.SparseFieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this);
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), (org.apache.commons.math.FieldElement) iter.value().divide(v[iter.key()]));
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> ebeMultiply(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        org.apache.commons.math.linear.SparseFieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this);
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), (org.apache.commons.math.FieldElement) iter.value().multiply(v.getEntry(iter.key())));
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> ebeMultiply(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.linear.SparseFieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this);
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), (org.apache.commons.math.FieldElement) iter.value().multiply(v[iter.key()]));
        }
        return res;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math.linear.FieldVector
    public T[] getData() {
        T[] tArr = (T[]) buildArray(this.virtualSize);
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator it = this.entries.iterator();
        while (it.hasNext()) {
            it.advance();
            tArr[it.key()] = it.value();
        }
        return tArr;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public int getDimension() {
        return this.virtualSize;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public T getEntry(int i) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(i);
        return (T) this.entries.get(i);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.Field<T> getField() {
        return this.field;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> getSubVector(int index, int n) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        checkIndex((index + n) - 1);
        org.apache.commons.math.linear.SparseFieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this.field, n);
        int end = index + n;
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (key >= index && key < end) {
                res.setEntry(key - index, iter.value());
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapAdd(T d) {
        return copy().mapAddToSelf(d);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapAddToSelf(T d) {
        for (int i = 0; i < this.virtualSize; i++) {
            setEntry(i, (org.apache.commons.math.FieldElement) getEntry(i).add(d));
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapDivide(T d) {
        return copy().mapDivideToSelf(d);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapDivideToSelf(T d) {
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            this.entries.put(iter.key(), (org.apache.commons.math.FieldElement) iter.value().divide(d));
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapInv() {
        return copy().mapInvToSelf();
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapInvToSelf() {
        for (int i = 0; i < this.virtualSize; i++) {
            setEntry(i, (org.apache.commons.math.FieldElement) this.field.getOne().divide(getEntry(i)));
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapMultiply(T d) {
        return copy().mapMultiplyToSelf(d);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapMultiplyToSelf(T d) {
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            this.entries.put(iter.key(), (org.apache.commons.math.FieldElement) iter.value().multiply(d));
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapSubtract(T d) {
        return copy().mapSubtractToSelf(d);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapSubtractToSelf(T d) {
        return mapAddToSelf((org.apache.commons.math.FieldElement) this.field.getZero().subtract(d));
    }

    public org.apache.commons.math.linear.FieldMatrix<T> outerProduct(org.apache.commons.math.linear.SparseFieldVector<T> v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        org.apache.commons.math.linear.SparseFieldMatrix<T> res = new org.apache.commons.math.linear.SparseFieldMatrix<>(this.field, this.virtualSize, this.virtualSize);
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter2 = v.entries.iterator();
            while (iter2.hasNext()) {
                iter2.advance();
                res.setEntry(iter.key(), iter2.key(), (org.apache.commons.math.FieldElement) iter.value().multiply(iter2.value()));
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldMatrix<T> outerProduct(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.linear.FieldMatrix<T> res = new org.apache.commons.math.linear.SparseFieldMatrix<>(this.field, this.virtualSize, this.virtualSize);
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int row = iter.key();
            org.apache.commons.math.FieldElement<T> value = iter.value();
            for (int col = 0; col < this.virtualSize; col++) {
                res.setEntry(row, col, value.multiply(v[col]));
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldMatrix<T> outerProduct(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.SparseFieldVector) {
            return outerProduct((org.apache.commons.math.linear.SparseFieldVector) v);
        }
        return outerProduct(v.toArray());
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> projection(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        return v.mapMultiply((org.apache.commons.math.FieldElement) dotProduct(v).divide(v.dotProduct(v)));
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> projection(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        return projection(new org.apache.commons.math.linear.SparseFieldVector(this.field, v));
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public void set(T value) {
        for (int i = 0; i < this.virtualSize; i++) {
            setEntry(i, value);
        }
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public void setEntry(int index, T value) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        this.entries.put(index, value);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public void setSubVector(int index, org.apache.commons.math.linear.FieldVector<T> v) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        checkIndex((v.getDimension() + index) - 1);
        setSubVector(index, v.getData());
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public void setSubVector(int index, T[] v) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        checkIndex((v.length + index) - 1);
        for (int i = 0; i < v.length; i++) {
            setEntry(i + index, v[i]);
        }
    }

    public org.apache.commons.math.linear.SparseFieldVector<T> subtract(org.apache.commons.math.linear.SparseFieldVector<T> v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
        org.apache.commons.math.linear.SparseFieldVector<T> res = (org.apache.commons.math.linear.SparseFieldVector) copy();
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = v.getEntries().iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (this.entries.containsKey(key)) {
                res.setEntry(key, (org.apache.commons.math.FieldElement) this.entries.get(key).subtract(iter.value()));
            } else {
                res.setEntry(key, (org.apache.commons.math.FieldElement) this.field.getZero().subtract(iter.value()));
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> subtract(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.SparseFieldVector) {
            return subtract((org.apache.commons.math.linear.SparseFieldVector) v);
        }
        return subtract(v.toArray());
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> subtract(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.linear.SparseFieldVector<T> res = new org.apache.commons.math.linear.SparseFieldVector<>(this);
        for (int i = 0; i < v.length; i++) {
            if (this.entries.containsKey(i)) {
                res.setEntry(i, (org.apache.commons.math.FieldElement) this.entries.get(i).subtract(v[i]));
            } else {
                res.setEntry(i, (org.apache.commons.math.FieldElement) this.field.getZero().subtract(v[i]));
            }
        }
        return res;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public T[] toArray() {
        return (T[]) getData();
    }

    private void checkIndex(int index) throws org.apache.commons.math.linear.MatrixIndexException {
        if (index < 0 || index >= getDimension()) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.INDEX_OUT_OF_RANGE, java.lang.Integer.valueOf(index), 0, java.lang.Integer.valueOf(getDimension() - 1));
        }
    }

    protected void checkVectorDimensions(int n) throws java.lang.IllegalArgumentException {
        if (getDimension() != n) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(getDimension()), java.lang.Integer.valueOf(n));
        }
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> add(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        if (v instanceof org.apache.commons.math.linear.SparseFieldVector) {
            return add((org.apache.commons.math.linear.SparseFieldVector) v);
        }
        return add(v.toArray());
    }

    private T[] buildArray(int i) {
        return (T[]) ((org.apache.commons.math.FieldElement[]) java.lang.reflect.Array.newInstance(this.field.getZero().getClass(), i));
    }

    public int hashCode() {
        int result = (1 * 31) + (this.field == null ? 0 : this.field.hashCode());
        int result2 = (result * 31) + this.virtualSize;
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int temp = iter.value().hashCode();
            result2 = (result2 * 31) + temp;
        }
        return result2;
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof org.apache.commons.math.linear.SparseFieldVector)) {
            return false;
        }
        org.apache.commons.math.linear.SparseFieldVector<T> other = (org.apache.commons.math.linear.SparseFieldVector) obj;
        if (this.field == null) {
            if (other.field != null) {
                return false;
            }
        } else if (!this.field.equals(other.field)) {
            return false;
        }
        if (this.virtualSize != other.virtualSize) {
            return false;
        }
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            if (!other.getEntry(iter.key()).equals(iter.value())) {
                return false;
            }
        }
        org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iter2 = other.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            if (!iter2.value().equals(getEntry(iter2.key()))) {
                return false;
            }
        }
        return true;
    }
}
