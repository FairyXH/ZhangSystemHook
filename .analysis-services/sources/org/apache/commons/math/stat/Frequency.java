package org.apache.commons.math.stat;

/* JADX INFO: loaded from: classes4.dex */
public class Frequency implements java.io.Serializable {
    private static final long serialVersionUID = -3845586908418844111L;
    private final java.util.TreeMap<java.lang.Comparable<?>, java.lang.Long> freqTable;

    public Frequency() {
        this.freqTable = new java.util.TreeMap<>();
    }

    public Frequency(java.util.Comparator<?> comparator) {
        this.freqTable = new java.util.TreeMap<>(comparator);
    }

    public java.lang.String toString() {
        java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance();
        java.lang.StringBuilder outBuffer = new java.lang.StringBuilder();
        outBuffer.append("Value \t Freq. \t Pct. \t Cum Pct. \n");
        for (java.lang.Comparable<?> value : this.freqTable.keySet()) {
            outBuffer.append(value);
            outBuffer.append('\t');
            outBuffer.append(getCount(value));
            outBuffer.append('\t');
            outBuffer.append(nf.format(getPct(value)));
            outBuffer.append('\t');
            outBuffer.append(nf.format(getCumPct(value)));
            outBuffer.append('\n');
        }
        return outBuffer.toString();
    }

    @java.lang.Deprecated
    public void addValue(java.lang.Object v) {
        if (v instanceof java.lang.Comparable) {
            addValue((java.lang.Comparable<?>) v);
            return;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CLASS_DOESNT_IMPLEMENT_COMPARABLE, v.getClass().getName());
    }

    public void addValue(java.lang.Comparable<?> v) {
        java.lang.Comparable<?> obj = v;
        if (v instanceof java.lang.Integer) {
            obj = java.lang.Long.valueOf(((java.lang.Integer) v).longValue());
        }
        try {
            java.lang.Long count = this.freqTable.get(obj);
            if (count == null) {
                this.freqTable.put(obj, 1L);
            } else {
                this.freqTable.put(obj, java.lang.Long.valueOf(count.longValue() + 1));
            }
        } catch (java.lang.ClassCastException e) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSTANCES_NOT_COMPARABLE_TO_EXISTING_VALUES, v.getClass().getName());
        }
    }

    public void addValue(int v) {
        addValue((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    @java.lang.Deprecated
    public void addValue(java.lang.Integer v) {
        addValue((java.lang.Comparable<?>) java.lang.Long.valueOf(v.longValue()));
    }

    public void addValue(long v) {
        addValue((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    public void addValue(char v) {
        addValue((java.lang.Comparable<?>) java.lang.Character.valueOf(v));
    }

    public void clear() {
        this.freqTable.clear();
    }

    public java.util.Iterator<java.lang.Comparable<?>> valuesIterator() {
        return this.freqTable.keySet().iterator();
    }

    public long getSumFreq() {
        long result = 0;
        java.util.Iterator<java.lang.Long> iterator = this.freqTable.values().iterator();
        while (iterator.hasNext()) {
            result += iterator.next().longValue();
        }
        return result;
    }

    @java.lang.Deprecated
    public long getCount(java.lang.Object v) {
        return getCount((java.lang.Comparable<?>) v);
    }

    public long getCount(java.lang.Comparable<?> v) {
        if (v instanceof java.lang.Integer) {
            return getCount(((java.lang.Integer) v).longValue());
        }
        try {
            java.lang.Long count = this.freqTable.get(v);
            if (count == null) {
                return 0L;
            }
            long result = count.longValue();
            return result;
        } catch (java.lang.ClassCastException e) {
            return 0L;
        }
    }

    public long getCount(int v) {
        return getCount((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    public long getCount(long v) {
        return getCount((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    public long getCount(char v) {
        return getCount((java.lang.Comparable<?>) java.lang.Character.valueOf(v));
    }

    public int getUniqueCount() {
        return this.freqTable.keySet().size();
    }

    @java.lang.Deprecated
    public double getPct(java.lang.Object v) {
        return getPct((java.lang.Comparable<?>) v);
    }

    public double getPct(java.lang.Comparable<?> v) {
        long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return getCount(v) / sumFreq;
    }

    public double getPct(int v) {
        return getPct((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    public double getPct(long v) {
        return getPct((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    public double getPct(char v) {
        return getPct((java.lang.Comparable<?>) java.lang.Character.valueOf(v));
    }

    @java.lang.Deprecated
    public long getCumFreq(java.lang.Object v) {
        return getCumFreq((java.lang.Comparable<?>) v);
    }

    public long getCumFreq(java.lang.Comparable<?> v) {
        if (getSumFreq() == 0) {
            return 0L;
        }
        if (v instanceof java.lang.Integer) {
            return getCumFreq(((java.lang.Integer) v).longValue());
        }
        java.util.Comparator<? super java.lang.Comparable<?>> comparator = this.freqTable.comparator();
        if (comparator == null) {
            comparator = new org.apache.commons.math.stat.Frequency.NaturalComparator();
        }
        long result = 0;
        try {
            java.lang.Long value = this.freqTable.get(v);
            if (value != null) {
                result = value.longValue();
            }
            if (comparator.compare(v, this.freqTable.firstKey()) < 0) {
                return 0L;
            }
            if (comparator.compare(v, this.freqTable.lastKey()) >= 0) {
                return getSumFreq();
            }
            java.util.Iterator<java.lang.Comparable<?>> values = valuesIterator();
            while (values.hasNext()) {
                java.lang.Comparable<?> nextValue = values.next();
                if (comparator.compare(v, nextValue) > 0) {
                    result += getCount(nextValue);
                } else {
                    return result;
                }
            }
            return result;
        } catch (java.lang.ClassCastException e) {
            return 0L;
        }
    }

    public long getCumFreq(int v) {
        return getCumFreq((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    public long getCumFreq(long v) {
        return getCumFreq((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    public long getCumFreq(char v) {
        return getCumFreq((java.lang.Comparable<?>) java.lang.Character.valueOf(v));
    }

    @java.lang.Deprecated
    public double getCumPct(java.lang.Object v) {
        return getCumPct((java.lang.Comparable<?>) v);
    }

    public double getCumPct(java.lang.Comparable<?> v) {
        long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return getCumFreq(v) / sumFreq;
    }

    public double getCumPct(int v) {
        return getCumPct((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    public double getCumPct(long v) {
        return getCumPct((java.lang.Comparable<?>) java.lang.Long.valueOf(v));
    }

    public double getCumPct(char v) {
        return getCumPct((java.lang.Comparable<?>) java.lang.Character.valueOf(v));
    }

    private static class NaturalComparator<T extends java.lang.Comparable<T>> implements java.util.Comparator<java.lang.Comparable<T>>, java.io.Serializable {
        private static final long serialVersionUID = -3852193713161395148L;

        private NaturalComparator() {
        }

        @Override // java.util.Comparator
        public int compare(java.lang.Comparable<T> o1, java.lang.Comparable<T> o2) {
            return o1.compareTo(o2);
        }
    }

    public int hashCode() {
        int result = (1 * 31) + (this.freqTable == null ? 0 : this.freqTable.hashCode());
        return result;
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof org.apache.commons.math.stat.Frequency)) {
            return false;
        }
        org.apache.commons.math.stat.Frequency other = (org.apache.commons.math.stat.Frequency) obj;
        if (this.freqTable == null) {
            if (other.freqTable != null) {
                return false;
            }
        } else if (!this.freqTable.equals(other.freqTable)) {
            return false;
        }
        return true;
    }
}
