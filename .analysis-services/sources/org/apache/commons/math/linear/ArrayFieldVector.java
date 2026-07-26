package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class ArrayFieldVector<T extends org.apache.commons.math.FieldElement<T>> implements org.apache.commons.math.linear.FieldVector<T>, java.io.Serializable {
    private static final long serialVersionUID = 7648186910365927050L;
    protected T[] data;
    private final org.apache.commons.math.Field<T> field;

    public ArrayFieldVector(org.apache.commons.math.Field<T> field) {
        this(field, 0);
    }

    public ArrayFieldVector(org.apache.commons.math.Field<T> field, int i) {
        this.field = field;
        this.data = (T[]) buildArray(i);
        java.util.Arrays.fill(this.data, field.getZero());
    }

    public ArrayFieldVector(int size, T preset) {
        this(preset.getField(), size);
        java.util.Arrays.fill(this.data, preset);
    }

    public ArrayFieldVector(T[] tArr) throws java.lang.IllegalArgumentException {
        try {
            this.field = tArr[0].getField();
            this.data = (T[]) ((org.apache.commons.math.FieldElement[]) tArr.clone());
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new java.lang.Object[0]);
        }
    }

    public ArrayFieldVector(org.apache.commons.math.Field<T> field, T[] tArr) {
        this.field = field;
        this.data = (T[]) ((org.apache.commons.math.FieldElement[]) tArr.clone());
    }

    public ArrayFieldVector(T[] tArr, boolean z) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
        if (tArr.length == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new java.lang.Object[0]);
        }
        this.field = tArr[0].getField();
        this.data = z ? (T[]) ((org.apache.commons.math.FieldElement[]) tArr.clone()) : tArr;
    }

    public ArrayFieldVector(org.apache.commons.math.Field<T> field, T[] tArr, boolean z) {
        this.field = field;
        this.data = z ? (T[]) ((org.apache.commons.math.FieldElement[]) tArr.clone()) : tArr;
    }

    public ArrayFieldVector(T[] tArr, int i, int i2) {
        if (tArr.length < i + i2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POSITION_SIZE_MISMATCH_INPUT_ARRAY, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2), java.lang.Integer.valueOf(tArr.length));
        }
        this.field = tArr[0].getField();
        this.data = (T[]) buildArray(i2);
        java.lang.System.arraycopy(tArr, i, this.data, 0, i2);
    }

    public ArrayFieldVector(org.apache.commons.math.linear.FieldVector<T> fieldVector) {
        this.field = fieldVector.getField();
        this.data = (T[]) buildArray(fieldVector.getDimension());
        for (int i = 0; i < this.data.length; i++) {
            ((T[]) this.data)[i] = fieldVector.getEntry(i);
        }
    }

    public ArrayFieldVector(org.apache.commons.math.linear.ArrayFieldVector<T> arrayFieldVector) {
        this.field = arrayFieldVector.getField();
        this.data = (T[]) ((org.apache.commons.math.FieldElement[]) arrayFieldVector.data.clone());
    }

    public ArrayFieldVector(org.apache.commons.math.linear.ArrayFieldVector<T> arrayFieldVector, boolean z) {
        this.field = arrayFieldVector.getField();
        T[] tArr = arrayFieldVector.data;
        this.data = z ? (T[]) ((org.apache.commons.math.FieldElement[]) tArr.clone()) : tArr;
    }

    public ArrayFieldVector(org.apache.commons.math.linear.ArrayFieldVector<T> arrayFieldVector, org.apache.commons.math.linear.ArrayFieldVector<T> arrayFieldVector2) {
        this.field = arrayFieldVector.getField();
        this.data = (T[]) buildArray(arrayFieldVector.data.length + arrayFieldVector2.data.length);
        java.lang.System.arraycopy(arrayFieldVector.data, 0, this.data, 0, arrayFieldVector.data.length);
        java.lang.System.arraycopy(arrayFieldVector2.data, 0, this.data, arrayFieldVector.data.length, arrayFieldVector2.data.length);
    }

    public ArrayFieldVector(org.apache.commons.math.linear.ArrayFieldVector<T> arrayFieldVector, T[] tArr) {
        this.field = arrayFieldVector.getField();
        this.data = (T[]) buildArray(arrayFieldVector.data.length + tArr.length);
        java.lang.System.arraycopy(arrayFieldVector.data, 0, this.data, 0, arrayFieldVector.data.length);
        java.lang.System.arraycopy(tArr, 0, this.data, arrayFieldVector.data.length, tArr.length);
    }

    public ArrayFieldVector(T[] tArr, org.apache.commons.math.linear.ArrayFieldVector<T> arrayFieldVector) {
        this.field = arrayFieldVector.getField();
        this.data = (T[]) buildArray(tArr.length + arrayFieldVector.data.length);
        java.lang.System.arraycopy(tArr, 0, this.data, 0, tArr.length);
        java.lang.System.arraycopy(arrayFieldVector.data, 0, this.data, tArr.length, arrayFieldVector.data.length);
    }

    public ArrayFieldVector(T[] tArr, T[] tArr2) {
        try {
            this.data = (T[]) buildArray(tArr.length + tArr2.length);
            java.lang.System.arraycopy(tArr, 0, this.data, 0, tArr.length);
            java.lang.System.arraycopy(tArr2, 0, this.data, tArr.length, tArr2.length);
            this.field = this.data[0].getField();
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new java.lang.Object[0]);
        }
    }

    public ArrayFieldVector(org.apache.commons.math.Field<T> field, T[] tArr, T[] tArr2) {
        if (tArr.length + tArr2.length == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new java.lang.Object[0]);
        }
        this.data = (T[]) buildArray(tArr.length + tArr2.length);
        java.lang.System.arraycopy(tArr, 0, this.data, 0, tArr.length);
        java.lang.System.arraycopy(tArr2, 0, this.data, tArr.length, tArr2.length);
        this.field = this.data[0].getField();
    }

    private T[] buildArray(int i) {
        return (T[]) ((org.apache.commons.math.FieldElement[]) java.lang.reflect.Array.newInstance(this.field.getZero().getClass(), i));
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.Field<T> getField() {
        return this.field;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> copy() {
        return new org.apache.commons.math.linear.ArrayFieldVector((org.apache.commons.math.linear.ArrayFieldVector) this, true);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> add(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        try {
            return add((org.apache.commons.math.linear.ArrayFieldVector) v);
        } catch (java.lang.ClassCastException e) {
            checkVectorDimensions(v);
            org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
            for (int i = 0; i < this.data.length; i++) {
                fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].add(v.getEntry(i));
            }
            return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
        }
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> add(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
        for (int i = 0; i < this.data.length; i++) {
            fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].add(v[i]);
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    public org.apache.commons.math.linear.ArrayFieldVector<T> add(org.apache.commons.math.linear.ArrayFieldVector<T> v) throws java.lang.IllegalArgumentException {
        return (org.apache.commons.math.linear.ArrayFieldVector) add(v.data);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> subtract(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        try {
            return subtract((org.apache.commons.math.linear.ArrayFieldVector) v);
        } catch (java.lang.ClassCastException e) {
            checkVectorDimensions(v);
            org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
            for (int i = 0; i < this.data.length; i++) {
                fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].subtract(v.getEntry(i));
            }
            return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
        }
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> subtract(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
        for (int i = 0; i < this.data.length; i++) {
            fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].subtract(v[i]);
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    public org.apache.commons.math.linear.ArrayFieldVector<T> subtract(org.apache.commons.math.linear.ArrayFieldVector<T> v) throws java.lang.IllegalArgumentException {
        return (org.apache.commons.math.linear.ArrayFieldVector) subtract(v.data);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapAdd(T d) {
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
        for (int i = 0; i < this.data.length; i++) {
            fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].add(d);
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapAddToSelf(T t) {
        for (int i = 0; i < this.data.length; i++) {
            ((T[]) this.data)[i] = (org.apache.commons.math.FieldElement) this.data[i].add(t);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapSubtract(T d) {
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
        for (int i = 0; i < this.data.length; i++) {
            fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].subtract(d);
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapSubtractToSelf(T t) {
        for (int i = 0; i < this.data.length; i++) {
            ((T[]) this.data)[i] = (org.apache.commons.math.FieldElement) this.data[i].subtract(t);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapMultiply(T d) {
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
        for (int i = 0; i < this.data.length; i++) {
            fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].multiply(d);
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapMultiplyToSelf(T t) {
        for (int i = 0; i < this.data.length; i++) {
            ((T[]) this.data)[i] = (org.apache.commons.math.FieldElement) this.data[i].multiply(t);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapDivide(T d) {
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
        for (int i = 0; i < this.data.length; i++) {
            fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].divide(d);
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapDivideToSelf(T t) {
        for (int i = 0; i < this.data.length; i++) {
            ((T[]) this.data)[i] = (org.apache.commons.math.FieldElement) this.data[i].divide(t);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapInv() {
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
        T one = this.field.getOne();
        for (int i = 0; i < this.data.length; i++) {
            fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) one.divide(this.data[i]);
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> mapInvToSelf() {
        T one = this.field.getOne();
        for (int i = 0; i < this.data.length; i++) {
            ((T[]) this.data)[i] = (org.apache.commons.math.FieldElement) one.divide(this.data[i]);
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> ebeMultiply(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        try {
            return ebeMultiply((org.apache.commons.math.linear.ArrayFieldVector) v);
        } catch (java.lang.ClassCastException e) {
            checkVectorDimensions(v);
            org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
            for (int i = 0; i < this.data.length; i++) {
                fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].multiply(v.getEntry(i));
            }
            return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
        }
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> ebeMultiply(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
        for (int i = 0; i < this.data.length; i++) {
            fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].multiply(v[i]);
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    public org.apache.commons.math.linear.ArrayFieldVector<T> ebeMultiply(org.apache.commons.math.linear.ArrayFieldVector<T> v) throws java.lang.IllegalArgumentException {
        return (org.apache.commons.math.linear.ArrayFieldVector) ebeMultiply(v.data);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> ebeDivide(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        try {
            return ebeDivide((org.apache.commons.math.linear.ArrayFieldVector) v);
        } catch (java.lang.ClassCastException e) {
            checkVectorDimensions(v);
            org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
            for (int i = 0; i < this.data.length; i++) {
                fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].divide(v.getEntry(i));
            }
            return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
        }
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> ebeDivide(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length);
        for (int i = 0; i < this.data.length; i++) {
            fieldElementArrBuildArray[i] = (org.apache.commons.math.FieldElement) this.data[i].divide(v[i]);
        }
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    public org.apache.commons.math.linear.ArrayFieldVector<T> ebeDivide(org.apache.commons.math.linear.ArrayFieldVector<T> v) throws java.lang.IllegalArgumentException {
        return (org.apache.commons.math.linear.ArrayFieldVector) ebeDivide(v.data);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public T[] getData() {
        return (T[]) ((org.apache.commons.math.FieldElement[]) this.data.clone());
    }

    public T[] getDataRef() {
        return this.data;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public T dotProduct(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException {
        try {
            return (T) dotProduct((org.apache.commons.math.linear.ArrayFieldVector) fieldVector);
        } catch (java.lang.ClassCastException e) {
            checkVectorDimensions(fieldVector);
            T zero = this.field.getZero();
            for (int i = 0; i < this.data.length; i++) {
                zero = (T) zero.add((org.apache.commons.math.FieldElement) this.data[i].multiply(fieldVector.getEntry(i)));
            }
            return zero;
        }
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public T dotProduct(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        T zero = this.field.getZero();
        for (int i = 0; i < this.data.length; i++) {
            zero = (T) zero.add((org.apache.commons.math.FieldElement) this.data[i].multiply(v[i]));
        }
        return zero;
    }

    public T dotProduct(org.apache.commons.math.linear.ArrayFieldVector<T> arrayFieldVector) throws java.lang.IllegalArgumentException {
        return (T) dotProduct(arrayFieldVector.data);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> projection(org.apache.commons.math.linear.FieldVector<T> v) {
        return v.mapMultiply((org.apache.commons.math.FieldElement) dotProduct(v).divide(v.dotProduct(v)));
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> projection(T[] v) {
        return projection((org.apache.commons.math.linear.ArrayFieldVector) new org.apache.commons.math.linear.ArrayFieldVector<>((org.apache.commons.math.FieldElement[]) v, false));
    }

    public org.apache.commons.math.linear.ArrayFieldVector<T> projection(org.apache.commons.math.linear.ArrayFieldVector<T> v) {
        return (org.apache.commons.math.linear.ArrayFieldVector) v.mapMultiply((org.apache.commons.math.FieldElement) dotProduct((org.apache.commons.math.linear.ArrayFieldVector) v).divide(v.dotProduct((org.apache.commons.math.linear.ArrayFieldVector) v)));
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldMatrix<T> outerProduct(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        try {
            return outerProduct((org.apache.commons.math.linear.ArrayFieldVector) v);
        } catch (java.lang.ClassCastException e) {
            checkVectorDimensions(v);
            int m = this.data.length;
            org.apache.commons.math.linear.FieldMatrix<T> out = new org.apache.commons.math.linear.Array2DRowFieldMatrix<>(this.field, m, m);
            for (int i = 0; i < this.data.length; i++) {
                for (int j = 0; j < this.data.length; j++) {
                    out.setEntry(i, j, (org.apache.commons.math.FieldElement) this.data[i].multiply(v.getEntry(j)));
                }
            }
            return out;
        }
    }

    public org.apache.commons.math.linear.FieldMatrix<T> outerProduct(org.apache.commons.math.linear.ArrayFieldVector<T> v) throws java.lang.IllegalArgumentException {
        return outerProduct(v.data);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldMatrix<T> outerProduct(T[] v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.length);
        int m = this.data.length;
        org.apache.commons.math.linear.FieldMatrix<T> out = new org.apache.commons.math.linear.Array2DRowFieldMatrix<>(this.field, m, m);
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data.length; j++) {
                out.setEntry(i, j, (org.apache.commons.math.FieldElement) this.data[i].multiply(v[j]));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public T getEntry(int index) throws org.apache.commons.math.linear.MatrixIndexException {
        return this.data[index];
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public int getDimension() {
        return this.data.length;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> append(org.apache.commons.math.linear.FieldVector<T> v) {
        try {
            return append((org.apache.commons.math.linear.ArrayFieldVector) v);
        } catch (java.lang.ClassCastException e) {
            return new org.apache.commons.math.linear.ArrayFieldVector(this, new org.apache.commons.math.linear.ArrayFieldVector(v));
        }
    }

    public org.apache.commons.math.linear.ArrayFieldVector<T> append(org.apache.commons.math.linear.ArrayFieldVector<T> v) {
        return new org.apache.commons.math.linear.ArrayFieldVector<>(this, v);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> append(T in) {
        org.apache.commons.math.FieldElement[] fieldElementArrBuildArray = buildArray(this.data.length + 1);
        java.lang.System.arraycopy(this.data, 0, fieldElementArrBuildArray, 0, this.data.length);
        fieldElementArrBuildArray[this.data.length] = in;
        return new org.apache.commons.math.linear.ArrayFieldVector(fieldElementArrBuildArray);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> append(T[] in) {
        return new org.apache.commons.math.linear.ArrayFieldVector(this, in);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public org.apache.commons.math.linear.FieldVector<T> getSubVector(int index, int n) {
        org.apache.commons.math.linear.ArrayFieldVector<T> out = new org.apache.commons.math.linear.ArrayFieldVector<>(this.field, n);
        try {
            java.lang.System.arraycopy(this.data, index, out.data, 0, n);
        } catch (java.lang.IndexOutOfBoundsException e) {
            checkIndex(index);
            checkIndex((index + n) - 1);
        }
        return out;
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public void setEntry(int index, T value) {
        try {
            this.data[index] = value;
        } catch (java.lang.IndexOutOfBoundsException e) {
            checkIndex(index);
        }
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public void setSubVector(int i, org.apache.commons.math.linear.FieldVector<T> fieldVector) {
        try {
            try {
                set(i, (org.apache.commons.math.linear.ArrayFieldVector) fieldVector);
            } catch (java.lang.ClassCastException e) {
                for (int i2 = i; i2 < fieldVector.getDimension() + i; i2++) {
                    ((T[]) this.data)[i2] = fieldVector.getEntry(i2 - i);
                }
            }
        } catch (java.lang.IndexOutOfBoundsException e2) {
            checkIndex(i);
            checkIndex((fieldVector.getDimension() + i) - 1);
        }
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public void setSubVector(int index, T[] v) {
        try {
            java.lang.System.arraycopy(v, 0, this.data, index, v.length);
        } catch (java.lang.IndexOutOfBoundsException e) {
            checkIndex(index);
            checkIndex((v.length + index) - 1);
        }
    }

    public void set(int index, org.apache.commons.math.linear.ArrayFieldVector<T> v) throws org.apache.commons.math.linear.MatrixIndexException {
        setSubVector(index, v.data);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public void set(T value) {
        java.util.Arrays.fill(this.data, value);
    }

    @Override // org.apache.commons.math.linear.FieldVector
    public T[] toArray() {
        return (T[]) ((org.apache.commons.math.FieldElement[]) this.data.clone());
    }

    protected void checkVectorDimensions(org.apache.commons.math.linear.FieldVector<T> v) throws java.lang.IllegalArgumentException {
        checkVectorDimensions(v.getDimension());
    }

    protected void checkVectorDimensions(int n) throws java.lang.IllegalArgumentException {
        if (this.data.length != n) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(this.data.length), java.lang.Integer.valueOf(n));
        }
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        try {
            org.apache.commons.math.linear.FieldVector<T> rhs = (org.apache.commons.math.linear.FieldVector) other;
            if (this.data.length != rhs.getDimension()) {
                return false;
            }
            for (int i = 0; i < this.data.length; i++) {
                if (!this.data[i].equals(rhs.getEntry(i))) {
                    return false;
                }
            }
            return true;
        } catch (java.lang.ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        int h = 3542;
        for (T a : this.data) {
            h ^= a.hashCode();
        }
        return h;
    }

    private void checkIndex(int index) throws org.apache.commons.math.linear.MatrixIndexException {
        if (index < 0 || index >= getDimension()) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.INDEX_OUT_OF_RANGE, java.lang.Integer.valueOf(index), 0, java.lang.Integer.valueOf(getDimension() - 1));
        }
    }
}
