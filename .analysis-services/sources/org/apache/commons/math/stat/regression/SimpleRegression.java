package org.apache.commons.math.stat.regression;

/* JADX INFO: loaded from: classes4.dex */
public class SimpleRegression implements java.io.Serializable {
    private static final long serialVersionUID = -3004689053607543335L;
    private org.apache.commons.math.distribution.TDistribution distribution;
    private long n;
    private double sumX;
    private double sumXX;
    private double sumXY;
    private double sumY;
    private double sumYY;
    private double xbar;
    private double ybar;

    public SimpleRegression() {
        this(new org.apache.commons.math.distribution.TDistributionImpl(1.0d));
    }

    @java.lang.Deprecated
    public SimpleRegression(org.apache.commons.math.distribution.TDistribution t) {
        this.sumX = 0.0d;
        this.sumXX = 0.0d;
        this.sumY = 0.0d;
        this.sumYY = 0.0d;
        this.sumXY = 0.0d;
        this.n = 0L;
        this.xbar = 0.0d;
        this.ybar = 0.0d;
        setDistribution(t);
    }

    public SimpleRegression(int degrees) {
        this.sumX = 0.0d;
        this.sumXX = 0.0d;
        this.sumY = 0.0d;
        this.sumYY = 0.0d;
        this.sumXY = 0.0d;
        this.n = 0L;
        this.xbar = 0.0d;
        this.ybar = 0.0d;
        setDistribution(new org.apache.commons.math.distribution.TDistributionImpl(degrees));
    }

    public void addData(double x, double y) {
        if (this.n == 0) {
            this.xbar = x;
            this.ybar = y;
        } else {
            double dx = x - this.xbar;
            double dy = y - this.ybar;
            this.sumXX += ((dx * dx) * this.n) / (this.n + 1.0d);
            this.sumYY += ((dy * dy) * this.n) / (this.n + 1.0d);
            this.sumXY += ((dx * dy) * this.n) / (this.n + 1.0d);
            this.xbar += dx / (this.n + 1.0d);
            this.ybar += dy / (this.n + 1.0d);
        }
        this.sumX += x;
        this.sumY += y;
        this.n++;
        if (this.n > 2) {
            this.distribution.setDegreesOfFreedom(this.n - 2);
        }
    }

    public void removeData(double x, double y) {
        if (this.n > 0) {
            double dx = x - this.xbar;
            double dy = y - this.ybar;
            this.sumXX -= ((dx * dx) * this.n) / (this.n - 1.0d);
            this.sumYY -= ((dy * dy) * this.n) / (this.n - 1.0d);
            this.sumXY -= ((dx * dy) * this.n) / (this.n - 1.0d);
            this.xbar -= dx / (this.n - 1.0d);
            this.ybar -= dy / (this.n - 1.0d);
            this.sumX -= x;
            this.sumY -= y;
            this.n--;
            if (this.n > 2) {
                this.distribution.setDegreesOfFreedom(this.n - 2);
            }
        }
    }

    public void addData(double[][] data) {
        for (int i = 0; i < data.length; i++) {
            addData(data[i][0], data[i][1]);
        }
    }

    public void removeData(double[][] data) {
        for (int i = 0; i < data.length && this.n > 0; i++) {
            removeData(data[i][0], data[i][1]);
        }
    }

    public void clear() {
        this.sumX = 0.0d;
        this.sumXX = 0.0d;
        this.sumY = 0.0d;
        this.sumYY = 0.0d;
        this.sumXY = 0.0d;
        this.n = 0L;
    }

    public long getN() {
        return this.n;
    }

    public double predict(double x) {
        double b1 = getSlope();
        return getIntercept(b1) + (b1 * x);
    }

    public double getIntercept() {
        return getIntercept(getSlope());
    }

    public double getSlope() {
        if (this.n >= 2 && org.apache.commons.math.util.FastMath.abs(this.sumXX) >= 4.9E-323d) {
            return this.sumXY / this.sumXX;
        }
        return Double.NaN;
    }

    public double getSumSquaredErrors() {
        return org.apache.commons.math.util.FastMath.max(0.0d, this.sumYY - ((this.sumXY * this.sumXY) / this.sumXX));
    }

    public double getTotalSumSquares() {
        if (this.n < 2) {
            return Double.NaN;
        }
        return this.sumYY;
    }

    public double getXSumSquares() {
        if (this.n < 2) {
            return Double.NaN;
        }
        return this.sumXX;
    }

    public double getSumOfCrossProducts() {
        return this.sumXY;
    }

    public double getRegressionSumSquares() {
        return getRegressionSumSquares(getSlope());
    }

    public double getMeanSquareError() {
        if (this.n < 3) {
            return Double.NaN;
        }
        return getSumSquaredErrors() / (this.n - 2);
    }

    public double getR() {
        double b1 = getSlope();
        double result = org.apache.commons.math.util.FastMath.sqrt(getRSquare());
        if (b1 < 0.0d) {
            return -result;
        }
        return result;
    }

    public double getRSquare() {
        double ssto = getTotalSumSquares();
        return (ssto - getSumSquaredErrors()) / ssto;
    }

    public double getInterceptStdErr() {
        return org.apache.commons.math.util.FastMath.sqrt(getMeanSquareError() * ((1.0d / this.n) + ((this.xbar * this.xbar) / this.sumXX)));
    }

    public double getSlopeStdErr() {
        return org.apache.commons.math.util.FastMath.sqrt(getMeanSquareError() / this.sumXX);
    }

    public double getSlopeConfidenceInterval() throws org.apache.commons.math.MathException {
        return getSlopeConfidenceInterval(0.05d);
    }

    public double getSlopeConfidenceInterval(double alpha) throws org.apache.commons.math.MathException {
        if (alpha >= 1.0d || alpha <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, java.lang.Double.valueOf(alpha), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(1.0d));
        }
        return getSlopeStdErr() * this.distribution.inverseCumulativeProbability(1.0d - (alpha / 2.0d));
    }

    public double getSignificance() throws org.apache.commons.math.MathException {
        return (1.0d - this.distribution.cumulativeProbability(org.apache.commons.math.util.FastMath.abs(getSlope()) / getSlopeStdErr())) * 2.0d;
    }

    private double getIntercept(double slope) {
        return (this.sumY - (this.sumX * slope)) / this.n;
    }

    private double getRegressionSumSquares(double slope) {
        return slope * slope * this.sumXX;
    }

    @java.lang.Deprecated
    public void setDistribution(org.apache.commons.math.distribution.TDistribution value) {
        this.distribution = value;
        if (this.n > 2) {
            this.distribution.setDegreesOfFreedom(this.n - 2);
        }
    }
}
