package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public interface FieldElement<T> {
    T add(T t);

    T divide(T t) throws java.lang.ArithmeticException;

    org.apache.commons.math.Field<T> getField();

    T multiply(T t);

    T subtract(T t);
}
