package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class EmpiricalDistributionImpl implements java.io.Serializable, org.apache.commons.math.random.EmpiricalDistribution {
    private static final long serialVersionUID = 5729073523949762654L;
    private final int binCount;
    private final java.util.List<org.apache.commons.math.stat.descriptive.SummaryStatistics> binStats;
    private double delta;
    private boolean loaded;
    private double max;
    private double min;
    private final org.apache.commons.math.random.RandomData randomData;
    private org.apache.commons.math.stat.descriptive.SummaryStatistics sampleStats;
    private double[] upperBounds;

    public EmpiricalDistributionImpl() {
        this.sampleStats = null;
        this.max = Double.NEGATIVE_INFINITY;
        this.min = Double.POSITIVE_INFINITY;
        this.delta = 0.0d;
        this.loaded = false;
        this.upperBounds = null;
        this.randomData = new org.apache.commons.math.random.RandomDataImpl();
        this.binCount = 1000;
        this.binStats = new java.util.ArrayList();
    }

    public EmpiricalDistributionImpl(int binCount) {
        this.sampleStats = null;
        this.max = Double.NEGATIVE_INFINITY;
        this.min = Double.POSITIVE_INFINITY;
        this.delta = 0.0d;
        this.loaded = false;
        this.upperBounds = null;
        this.randomData = new org.apache.commons.math.random.RandomDataImpl();
        this.binCount = binCount;
        this.binStats = new java.util.ArrayList();
    }

    @Override // org.apache.commons.math.random.EmpiricalDistribution
    public void load(double[] in) {
        org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter da = new org.apache.commons.math.random.EmpiricalDistributionImpl.ArrayDataAdapter(in);
        try {
            da.computeStats();
            fillBinStats(in);
            this.loaded = true;
        } catch (java.io.IOException e) {
            throw new org.apache.commons.math.MathRuntimeException(e);
        }
    }

    @Override // org.apache.commons.math.random.EmpiricalDistribution
    public void load(java.net.URL url) throws java.io.IOException {
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream()));
        try {
            org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter da = new org.apache.commons.math.random.EmpiricalDistributionImpl.StreamDataAdapter(in);
            da.computeStats();
            if (this.sampleStats.getN() == 0) {
                throw org.apache.commons.math.MathRuntimeException.createEOFException(org.apache.commons.math.exception.util.LocalizedFormats.URL_CONTAINS_NO_DATA, url);
            }
            java.io.BufferedReader in2 = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream()));
            fillBinStats(in2);
            this.loaded = true;
            try {
                in2.close();
            } catch (java.io.IOException e) {
            }
        } catch (java.lang.Throwable th) {
            try {
                in.close();
            } catch (java.io.IOException e2) {
            }
            throw th;
        }
    }

    @Override // org.apache.commons.math.random.EmpiricalDistribution
    public void load(java.io.File file) throws java.io.IOException {
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(file));
        try {
            org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter da = new org.apache.commons.math.random.EmpiricalDistributionImpl.StreamDataAdapter(in);
            da.computeStats();
            in = new java.io.BufferedReader(new java.io.FileReader(file));
            fillBinStats(in);
            this.loaded = true;
            try {
                in.close();
            } catch (java.io.IOException e) {
            }
        } catch (java.lang.Throwable th) {
            try {
                in.close();
            } catch (java.io.IOException e2) {
            }
            throw th;
        }
    }

    private abstract class DataAdapter {
        public abstract void computeBinStats() throws java.io.IOException;

        public abstract void computeStats() throws java.io.IOException;

        private DataAdapter() {
        }
    }

    private class DataAdapterFactory {
        private DataAdapterFactory() {
        }

        public org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter getAdapter(java.lang.Object in) {
            if (in instanceof java.io.BufferedReader) {
                java.io.BufferedReader inputStream = (java.io.BufferedReader) in;
                return org.apache.commons.math.random.EmpiricalDistributionImpl.this.new StreamDataAdapter(inputStream);
            }
            if (in instanceof double[]) {
                double[] inputArray = (double[]) in;
                return org.apache.commons.math.random.EmpiricalDistributionImpl.this.new ArrayDataAdapter(inputArray);
            }
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_DATA_FROM_UNSUPPORTED_DATASOURCE, in.getClass().getName(), java.io.BufferedReader.class.getName(), double[].class.getName());
        }
    }

    private class StreamDataAdapter extends org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter {
        private java.io.BufferedReader inputStream;

        public StreamDataAdapter(java.io.BufferedReader in) {
            super();
            this.inputStream = in;
        }

        @Override // org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter
        public void computeBinStats() throws java.io.IOException {
            while (true) {
                java.lang.String str = this.inputStream.readLine();
                if (str != null) {
                    double val = java.lang.Double.parseDouble(str);
                    org.apache.commons.math.stat.descriptive.SummaryStatistics stats = (org.apache.commons.math.stat.descriptive.SummaryStatistics) org.apache.commons.math.random.EmpiricalDistributionImpl.this.binStats.get(org.apache.commons.math.random.EmpiricalDistributionImpl.this.findBin(val));
                    stats.addValue(val);
                } else {
                    this.inputStream.close();
                    this.inputStream = null;
                    return;
                }
            }
        }

        @Override // org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter
        public void computeStats() throws java.io.IOException {
            org.apache.commons.math.random.EmpiricalDistributionImpl.this.sampleStats = new org.apache.commons.math.stat.descriptive.SummaryStatistics();
            while (true) {
                java.lang.String str = this.inputStream.readLine();
                if (str != null) {
                    double val = java.lang.Double.valueOf(str).doubleValue();
                    org.apache.commons.math.random.EmpiricalDistributionImpl.this.sampleStats.addValue(val);
                } else {
                    this.inputStream.close();
                    this.inputStream = null;
                    return;
                }
            }
        }
    }

    private class ArrayDataAdapter extends org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter {
        private double[] inputArray;

        public ArrayDataAdapter(double[] in) {
            super();
            this.inputArray = in;
        }

        @Override // org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter
        public void computeStats() throws java.io.IOException {
            org.apache.commons.math.random.EmpiricalDistributionImpl.this.sampleStats = new org.apache.commons.math.stat.descriptive.SummaryStatistics();
            for (int i = 0; i < this.inputArray.length; i++) {
                org.apache.commons.math.random.EmpiricalDistributionImpl.this.sampleStats.addValue(this.inputArray[i]);
            }
        }

        @Override // org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter
        public void computeBinStats() throws java.io.IOException {
            for (int i = 0; i < this.inputArray.length; i++) {
                org.apache.commons.math.stat.descriptive.SummaryStatistics stats = (org.apache.commons.math.stat.descriptive.SummaryStatistics) org.apache.commons.math.random.EmpiricalDistributionImpl.this.binStats.get(org.apache.commons.math.random.EmpiricalDistributionImpl.this.findBin(this.inputArray[i]));
                stats.addValue(this.inputArray[i]);
            }
        }
    }

    private void fillBinStats(java.lang.Object in) throws java.io.IOException {
        this.min = this.sampleStats.getMin();
        this.max = this.sampleStats.getMax();
        this.delta = (this.max - this.min) / java.lang.Double.valueOf(this.binCount).doubleValue();
        if (!this.binStats.isEmpty()) {
            this.binStats.clear();
        }
        for (int i = 0; i < this.binCount; i++) {
            org.apache.commons.math.stat.descriptive.SummaryStatistics stats = new org.apache.commons.math.stat.descriptive.SummaryStatistics();
            this.binStats.add(i, stats);
        }
        org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapterFactory aFactory = new org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapterFactory();
        org.apache.commons.math.random.EmpiricalDistributionImpl.DataAdapter da = aFactory.getAdapter(in);
        da.computeBinStats();
        this.upperBounds = new double[this.binCount];
        this.upperBounds[0] = this.binStats.get(0).getN() / this.sampleStats.getN();
        for (int i2 = 1; i2 < this.binCount - 1; i2++) {
            this.upperBounds[i2] = this.upperBounds[i2 - 1] + (this.binStats.get(i2).getN() / this.sampleStats.getN());
        }
        this.upperBounds[this.binCount - 1] = 1.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findBin(double value) {
        return org.apache.commons.math.util.FastMath.min(org.apache.commons.math.util.FastMath.max(((int) org.apache.commons.math.util.FastMath.ceil((value - this.min) / this.delta)) - 1, 0), this.binCount - 1);
    }

    @Override // org.apache.commons.math.random.EmpiricalDistribution
    public double getNextValue() throws java.lang.IllegalStateException {
        if (!this.loaded) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.DISTRIBUTION_NOT_LOADED, new java.lang.Object[0]);
        }
        double x = org.apache.commons.math.util.FastMath.random();
        for (int i = 0; i < this.binCount; i++) {
            if (x <= this.upperBounds[i]) {
                org.apache.commons.math.stat.descriptive.SummaryStatistics stats = this.binStats.get(i);
                if (stats.getN() > 0) {
                    if (stats.getStandardDeviation() > 0.0d) {
                        return this.randomData.nextGaussian(stats.getMean(), stats.getStandardDeviation());
                    }
                    return stats.getMean();
                }
            }
        }
        throw new org.apache.commons.math.MathRuntimeException(org.apache.commons.math.exception.util.LocalizedFormats.NO_BIN_SELECTED, new java.lang.Object[0]);
    }

    @Override // org.apache.commons.math.random.EmpiricalDistribution
    public org.apache.commons.math.stat.descriptive.StatisticalSummary getSampleStats() {
        return this.sampleStats;
    }

    @Override // org.apache.commons.math.random.EmpiricalDistribution
    public int getBinCount() {
        return this.binCount;
    }

    @Override // org.apache.commons.math.random.EmpiricalDistribution
    public java.util.List<org.apache.commons.math.stat.descriptive.SummaryStatistics> getBinStats() {
        return this.binStats;
    }

    @Override // org.apache.commons.math.random.EmpiricalDistribution
    public double[] getUpperBounds() {
        double[] binUpperBounds = new double[this.binCount];
        binUpperBounds[0] = this.min + this.delta;
        for (int i = 1; i < this.binCount - 1; i++) {
            binUpperBounds[i] = binUpperBounds[i - 1] + this.delta;
        }
        int i2 = this.binCount;
        binUpperBounds[i2 - 1] = this.max;
        return binUpperBounds;
    }

    public double[] getGeneratorUpperBounds() {
        int len = this.upperBounds.length;
        double[] out = new double[len];
        java.lang.System.arraycopy(this.upperBounds, 0, out, 0, len);
        return out;
    }

    @Override // org.apache.commons.math.random.EmpiricalDistribution
    public boolean isLoaded() {
        return this.loaded;
    }
}
