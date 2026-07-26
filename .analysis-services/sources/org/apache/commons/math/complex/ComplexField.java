package org.apache.commons.math.complex;

/* JADX INFO: loaded from: classes4.dex */
public class ComplexField implements org.apache.commons.math.Field<org.apache.commons.math.complex.Complex>, java.io.Serializable {
    private static final long serialVersionUID = -6130362688700788798L;

    private ComplexField() {
    }

    public static org.apache.commons.math.complex.ComplexField getInstance() {
        return org.apache.commons.math.complex.ComplexField.LazyHolder.INSTANCE;
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.complex.Complex getOne() {
        return org.apache.commons.math.complex.Complex.ONE;
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.complex.Complex getZero() {
        return org.apache.commons.math.complex.Complex.ZERO;
    }

    private static class LazyHolder {
        private static final org.apache.commons.math.complex.ComplexField INSTANCE = new org.apache.commons.math.complex.ComplexField();

        private LazyHolder() {
        }
    }

    private java.lang.Object readResolve() {
        return org.apache.commons.math.complex.ComplexField.LazyHolder.INSTANCE;
    }
}
