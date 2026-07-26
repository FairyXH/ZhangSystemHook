package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface FieldVector<T extends org.apache.commons.math.FieldElement<T>> {
    org.apache.commons.math.linear.FieldVector<T> add(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> add(T[] tArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> append(T t);

    org.apache.commons.math.linear.FieldVector<T> append(org.apache.commons.math.linear.FieldVector<T> fieldVector);

    org.apache.commons.math.linear.FieldVector<T> append(T[] tArr);

    org.apache.commons.math.linear.FieldVector<T> copy();

    T dotProduct(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException;

    T dotProduct(T[] tArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> ebeDivide(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> ebeDivide(T[] tArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> ebeMultiply(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> ebeMultiply(T[] tArr) throws java.lang.IllegalArgumentException;

    T[] getData();

    int getDimension();

    T getEntry(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.Field<T> getField();

    org.apache.commons.math.linear.FieldVector<T> getSubVector(int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldVector<T> mapAdd(T t);

    org.apache.commons.math.linear.FieldVector<T> mapAddToSelf(T t);

    org.apache.commons.math.linear.FieldVector<T> mapDivide(T t);

    org.apache.commons.math.linear.FieldVector<T> mapDivideToSelf(T t);

    org.apache.commons.math.linear.FieldVector<T> mapInv();

    org.apache.commons.math.linear.FieldVector<T> mapInvToSelf();

    org.apache.commons.math.linear.FieldVector<T> mapMultiply(T t);

    org.apache.commons.math.linear.FieldVector<T> mapMultiplyToSelf(T t);

    org.apache.commons.math.linear.FieldVector<T> mapSubtract(T t);

    org.apache.commons.math.linear.FieldVector<T> mapSubtractToSelf(T t);

    org.apache.commons.math.linear.FieldMatrix<T> outerProduct(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldMatrix<T> outerProduct(T[] tArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> projection(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> projection(T[] tArr) throws java.lang.IllegalArgumentException;

    void set(T t);

    void setEntry(int i, T t) throws org.apache.commons.math.linear.MatrixIndexException;

    void setSubVector(int i, org.apache.commons.math.linear.FieldVector<T> fieldVector) throws org.apache.commons.math.linear.MatrixIndexException;

    void setSubVector(int i, T[] tArr) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldVector<T> subtract(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> subtract(T[] tArr) throws java.lang.IllegalArgumentException;

    T[] toArray();
}
