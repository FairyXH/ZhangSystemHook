package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public class BigRealField implements org.apache.commons.math.Field<org.apache.commons.math.util.BigReal>, java.io.Serializable {
    private static final long serialVersionUID = 4756431066541037559L;

    private BigRealField() {
    }

    public static org.apache.commons.math.util.BigRealField getInstance() {
        return org.apache.commons.math.util.BigRealField.LazyHolder.INSTANCE;
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.util.BigReal getOne() {
        return org.apache.commons.math.util.BigReal.ONE;
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.util.BigReal getZero() {
        return org.apache.commons.math.util.BigReal.ZERO;
    }

    private static class LazyHolder {
        private static final org.apache.commons.math.util.BigRealField INSTANCE = new org.apache.commons.math.util.BigRealField();

        private LazyHolder() {
        }
    }

    private java.lang.Object readResolve() {
        return org.apache.commons.math.util.BigRealField.LazyHolder.INSTANCE;
    }
}
