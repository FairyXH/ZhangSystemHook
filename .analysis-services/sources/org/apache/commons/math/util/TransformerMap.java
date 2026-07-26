package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public class TransformerMap implements org.apache.commons.math.util.NumberTransformer, java.io.Serializable {
    private static final long serialVersionUID = 4605318041528645258L;
    private org.apache.commons.math.util.NumberTransformer defaultTransformer;
    private java.util.Map<java.lang.Class<?>, org.apache.commons.math.util.NumberTransformer> map;

    public TransformerMap() {
        this.defaultTransformer = null;
        this.map = null;
        this.map = new java.util.HashMap();
        this.defaultTransformer = new org.apache.commons.math.util.DefaultTransformer();
    }

    public boolean containsClass(java.lang.Class<?> key) {
        return this.map.containsKey(key);
    }

    public boolean containsTransformer(org.apache.commons.math.util.NumberTransformer value) {
        return this.map.containsValue(value);
    }

    public org.apache.commons.math.util.NumberTransformer getTransformer(java.lang.Class<?> key) {
        return this.map.get(key);
    }

    public org.apache.commons.math.util.NumberTransformer putTransformer(java.lang.Class<?> key, org.apache.commons.math.util.NumberTransformer transformer) {
        return this.map.put(key, transformer);
    }

    public org.apache.commons.math.util.NumberTransformer removeTransformer(java.lang.Class<?> key) {
        return this.map.remove(key);
    }

    public void clear() {
        this.map.clear();
    }

    public java.util.Set<java.lang.Class<?>> classes() {
        return this.map.keySet();
    }

    public java.util.Collection<org.apache.commons.math.util.NumberTransformer> transformers() {
        return this.map.values();
    }

    @Override // org.apache.commons.math.util.NumberTransformer
    public double transform(java.lang.Object o) throws org.apache.commons.math.MathException {
        if ((o instanceof java.lang.Number) || (o instanceof java.lang.String)) {
            double value = this.defaultTransformer.transform(o);
            return value;
        }
        org.apache.commons.math.util.NumberTransformer trans = getTransformer(o.getClass());
        if (trans == null) {
            return Double.NaN;
        }
        double value2 = trans.transform(o);
        return value2;
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof org.apache.commons.math.util.TransformerMap)) {
            return false;
        }
        org.apache.commons.math.util.TransformerMap rhs = (org.apache.commons.math.util.TransformerMap) other;
        if (!this.defaultTransformer.equals(rhs.defaultTransformer) || this.map.size() != rhs.map.size()) {
            return false;
        }
        for (java.util.Map.Entry<java.lang.Class<?>, org.apache.commons.math.util.NumberTransformer> entry : this.map.entrySet()) {
            if (!entry.getValue().equals(rhs.map.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = this.defaultTransformer.hashCode();
        for (org.apache.commons.math.util.NumberTransformer t : this.map.values()) {
            hash = (hash * 31) + t.hashCode();
        }
        return hash;
    }
}
