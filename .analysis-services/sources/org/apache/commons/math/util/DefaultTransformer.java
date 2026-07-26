package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public class DefaultTransformer implements org.apache.commons.math.util.NumberTransformer, java.io.Serializable {
    private static final long serialVersionUID = 4019938025047800455L;

    @Override // org.apache.commons.math.util.NumberTransformer
    public double transform(java.lang.Object o) throws org.apache.commons.math.MathException {
        if (o == null) {
            throw new org.apache.commons.math.MathException(org.apache.commons.math.exception.util.LocalizedFormats.OBJECT_TRANSFORMATION, new java.lang.Object[0]);
        }
        if (o instanceof java.lang.Number) {
            return ((java.lang.Number) o).doubleValue();
        }
        try {
            return java.lang.Double.valueOf(o.toString()).doubleValue();
        } catch (java.lang.NumberFormatException e) {
            throw new org.apache.commons.math.MathException(e, org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_TRANSFORM_TO_DOUBLE, e.getMessage());
        }
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        return other instanceof org.apache.commons.math.util.DefaultTransformer;
    }

    public int hashCode() {
        return 401993047;
    }
}
