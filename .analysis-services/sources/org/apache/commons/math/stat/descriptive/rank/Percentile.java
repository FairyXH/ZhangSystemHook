package org.apache.commons.math.stat.descriptive.rank;

/* JADX INFO: loaded from: classes4.dex */
public class Percentile extends org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic implements java.io.Serializable {
    private static final int MAX_CACHED_LEVELS = 10;
    private static final int MIN_SELECT_SIZE = 15;
    private static final long serialVersionUID = -8091216485095130416L;
    private int[] cachedPivots;
    private double quantile;

    public Percentile() {
        this(50.0d);
    }

    public Percentile(double p) {
        this.quantile = 0.0d;
        setQuantile(p);
        this.cachedPivots = null;
    }

    public Percentile(org.apache.commons.math.stat.descriptive.rank.Percentile original) {
        this.quantile = 0.0d;
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic
    public void setData(double[] values) {
        if (values == null) {
            this.cachedPivots = null;
        } else {
            this.cachedPivots = new int[1023];
            java.util.Arrays.fill(this.cachedPivots, -1);
        }
        super.setData(values);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic
    public void setData(double[] values, int begin, int length) {
        if (values == null) {
            this.cachedPivots = null;
        } else {
            this.cachedPivots = new int[1023];
            java.util.Arrays.fill(this.cachedPivots, -1);
        }
        super.setData(values, begin, length);
    }

    public double evaluate(double p) {
        return evaluate(getDataRef(), p);
    }

    public double evaluate(double[] values, double p) {
        test(values, 0, 0);
        return evaluate(values, 0, values.length, p);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int start, int length) {
        return evaluate(values, start, length, this.quantile);
    }

    public double evaluate(double[] values, int begin, int length, double p) {
        double[] work;
        int[] pivotsHeap;
        test(values, begin, length);
        if (p > 100.0d || p <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_BOUNDS_QUANTILE_VALUE, java.lang.Double.valueOf(p));
        }
        if (length == 0) {
            return Double.NaN;
        }
        if (length == 1) {
            return values[begin];
        }
        double n = length;
        double pos = ((n + 1.0d) * p) / 100.0d;
        double fpos = org.apache.commons.math.util.FastMath.floor(pos);
        int intPos = (int) fpos;
        double dif = pos - fpos;
        if (values == getDataRef()) {
            work = getDataRef();
            pivotsHeap = this.cachedPivots;
        } else {
            work = new double[length];
            java.lang.System.arraycopy(values, begin, work, 0, length);
            pivotsHeap = new int[1023];
            java.util.Arrays.fill(pivotsHeap, -1);
        }
        if (pos < 1.0d) {
            return select(work, pivotsHeap, 0);
        }
        if (pos >= n) {
            return select(work, pivotsHeap, length - 1);
        }
        double lower = select(work, pivotsHeap, intPos - 1);
        double upper = select(work, pivotsHeap, intPos);
        return lower + ((upper - lower) * dif);
    }

    private double select(double[] work, int[] pivotsHeap, int k) {
        int pivot;
        int begin = 0;
        int end = work.length;
        int node = 0;
        while (end - begin > 15) {
            if (node < pivotsHeap.length && pivotsHeap[node] >= 0) {
                pivot = pivotsHeap[node];
            } else {
                pivot = partition(work, begin, end, medianOf3(work, begin, end));
                if (node < pivotsHeap.length) {
                    pivotsHeap[node] = pivot;
                }
            }
            if (k == pivot) {
                return work[k];
            }
            if (k < pivot) {
                end = pivot;
                node = java.lang.Math.min((node * 2) + 1, pivotsHeap.length);
            } else {
                begin = pivot + 1;
                node = java.lang.Math.min((node * 2) + 2, pivotsHeap.length);
            }
        }
        insertionSort(work, begin, end);
        return work[k];
    }

    int medianOf3(double[] work, int begin, int end) {
        int inclusiveEnd = end - 1;
        int middle = ((inclusiveEnd - begin) / 2) + begin;
        double wBegin = work[begin];
        double wMiddle = work[middle];
        double wEnd = work[inclusiveEnd];
        if (wBegin < wMiddle) {
            if (wMiddle < wEnd) {
                return middle;
            }
            return wBegin < wEnd ? inclusiveEnd : begin;
        }
        if (wBegin < wEnd) {
            return begin;
        }
        return wMiddle < wEnd ? inclusiveEnd : middle;
    }

    private int partition(double[] work, int begin, int end, int pivot) {
        double value = work[pivot];
        work[pivot] = work[begin];
        int i = begin + 1;
        int j = end - 1;
        while (i < j) {
            while (i < j && work[j] >= value) {
                j--;
            }
            while (i < j && work[i] <= value) {
                i++;
            }
            if (i < j) {
                double tmp = work[i];
                work[i] = work[j];
                work[j] = tmp;
                j--;
                i++;
            }
        }
        if (i >= end || work[i] > value) {
            i--;
        }
        work[begin] = work[i];
        work[i] = value;
        return i;
    }

    private void insertionSort(double[] work, int begin, int end) {
        for (int j = begin + 1; j < end; j++) {
            double saved = work[j];
            int i = j - 1;
            while (i >= begin && saved < work[i]) {
                work[i + 1] = work[i];
                i--;
            }
            work[i + 1] = saved;
        }
    }

    public double getQuantile() {
        return this.quantile;
    }

    public void setQuantile(double p) {
        if (p <= 0.0d || p > 100.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_BOUNDS_QUANTILE_VALUE, java.lang.Double.valueOf(p));
        }
        this.quantile = p;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.rank.Percentile copy() {
        org.apache.commons.math.stat.descriptive.rank.Percentile result = new org.apache.commons.math.stat.descriptive.rank.Percentile();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.rank.Percentile source, org.apache.commons.math.stat.descriptive.rank.Percentile dest) {
        dest.setData(source.getDataRef());
        if (source.cachedPivots != null) {
            java.lang.System.arraycopy(source.cachedPivots, 0, dest.cachedPivots, 0, source.cachedPivots.length);
        }
        dest.quantile = source.quantile;
    }
}
