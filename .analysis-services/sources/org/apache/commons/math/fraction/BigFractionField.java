package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public class BigFractionField implements org.apache.commons.math.Field<org.apache.commons.math.fraction.BigFraction>, java.io.Serializable {
    private static final long serialVersionUID = -1699294557189741703L;

    private BigFractionField() {
    }

    public static org.apache.commons.math.fraction.BigFractionField getInstance() {
        return org.apache.commons.math.fraction.BigFractionField.LazyHolder.INSTANCE;
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.fraction.BigFraction getOne() {
        return org.apache.commons.math.fraction.BigFraction.ONE;
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.fraction.BigFraction getZero() {
        return org.apache.commons.math.fraction.BigFraction.ZERO;
    }

    private static class LazyHolder {
        private static final org.apache.commons.math.fraction.BigFractionField INSTANCE = new org.apache.commons.math.fraction.BigFractionField();

        private LazyHolder() {
        }
    }

    private java.lang.Object readResolve() {
        return org.apache.commons.math.fraction.BigFractionField.LazyHolder.INSTANCE;
    }
}
