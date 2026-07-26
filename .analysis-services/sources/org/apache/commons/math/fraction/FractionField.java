package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public class FractionField implements org.apache.commons.math.Field<org.apache.commons.math.fraction.Fraction>, java.io.Serializable {
    private static final long serialVersionUID = -1257768487499119313L;

    private FractionField() {
    }

    public static org.apache.commons.math.fraction.FractionField getInstance() {
        return org.apache.commons.math.fraction.FractionField.LazyHolder.INSTANCE;
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.fraction.Fraction getOne() {
        return org.apache.commons.math.fraction.Fraction.ONE;
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.fraction.Fraction getZero() {
        return org.apache.commons.math.fraction.Fraction.ZERO;
    }

    private static class LazyHolder {
        private static final org.apache.commons.math.fraction.FractionField INSTANCE = new org.apache.commons.math.fraction.FractionField();

        private LazyHolder() {
        }
    }

    private java.lang.Object readResolve() {
        return org.apache.commons.math.fraction.FractionField.LazyHolder.INSTANCE;
    }
}
