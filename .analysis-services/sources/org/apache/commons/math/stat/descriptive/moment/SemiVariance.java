package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class SemiVariance extends org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = -2653430366886024994L;
    private boolean biasCorrected;
    private org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction varianceDirection;
    public static final org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction UPSIDE_VARIANCE = org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction.UPSIDE;
    public static final org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction DOWNSIDE_VARIANCE = org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction.DOWNSIDE;

    public SemiVariance() {
        this.biasCorrected = true;
        this.varianceDirection = org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction.DOWNSIDE;
    }

    public SemiVariance(boolean biasCorrected) {
        this.biasCorrected = true;
        this.varianceDirection = org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction.DOWNSIDE;
        this.biasCorrected = biasCorrected;
    }

    public SemiVariance(org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction direction) {
        this.biasCorrected = true;
        this.varianceDirection = org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction.DOWNSIDE;
        this.varianceDirection = direction;
    }

    public SemiVariance(boolean corrected, org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction direction) {
        this.biasCorrected = true;
        this.varianceDirection = org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction.DOWNSIDE;
        this.biasCorrected = corrected;
        this.varianceDirection = direction;
    }

    public SemiVariance(org.apache.commons.math.stat.descriptive.moment.SemiVariance original) {
        this.biasCorrected = true;
        this.varianceDirection = org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction.DOWNSIDE;
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.SemiVariance copy() {
        org.apache.commons.math.stat.descriptive.moment.SemiVariance result = new org.apache.commons.math.stat.descriptive.moment.SemiVariance();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.SemiVariance source, org.apache.commons.math.stat.descriptive.moment.SemiVariance dest) {
        dest.setData(source.getDataRef());
        dest.biasCorrected = source.biasCorrected;
        dest.varianceDirection = source.varianceDirection;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values) {
        if (values == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        return evaluate(values, 0, values.length);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int start, int length) {
        double m = new org.apache.commons.math.stat.descriptive.moment.Mean().evaluate(values, start, length);
        return evaluate(values, m, this.varianceDirection, this.biasCorrected, 0, values.length);
    }

    public double evaluate(double[] values, org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction direction) {
        double m = new org.apache.commons.math.stat.descriptive.moment.Mean().evaluate(values);
        return evaluate(values, m, direction, this.biasCorrected, 0, values.length);
    }

    public double evaluate(double[] values, double cutoff) {
        return evaluate(values, cutoff, this.varianceDirection, this.biasCorrected, 0, values.length);
    }

    public double evaluate(double[] values, double cutoff, org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction direction) {
        return evaluate(values, cutoff, direction, this.biasCorrected, 0, values.length);
    }

    public double evaluate(double[] values, double cutoff, org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction direction, boolean corrected, int start, int length) {
        test(values, start, length);
        if (values.length == 0) {
            return Double.NaN;
        }
        if (values.length == 1) {
            return 0.0d;
        }
        boolean booleanDirection = direction.getDirection();
        double sumsq = 0.0d;
        for (int i = start; i < length; i++) {
            if ((values[i] > cutoff) == booleanDirection) {
                double dev = values[i] - cutoff;
                sumsq += dev * dev;
            }
        }
        if (corrected) {
            return sumsq / (((double) length) - 1.0d);
        }
        return sumsq / ((double) length);
    }

    public boolean isBiasCorrected() {
        return this.biasCorrected;
    }

    public void setBiasCorrected(boolean biasCorrected) {
        this.biasCorrected = biasCorrected;
    }

    public org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction getVarianceDirection() {
        return this.varianceDirection;
    }

    public void setVarianceDirection(org.apache.commons.math.stat.descriptive.moment.SemiVariance.Direction varianceDirection) {
        this.varianceDirection = varianceDirection;
    }

    public enum Direction {
        UPSIDE(true),
        DOWNSIDE(false);

        private boolean direction;

        Direction(boolean b) {
            this.direction = b;
        }

        boolean getDirection() {
            return this.direction;
        }
    }
}
