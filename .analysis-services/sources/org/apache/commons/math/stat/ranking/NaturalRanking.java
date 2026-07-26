package org.apache.commons.math.stat.ranking;

/* JADX INFO: loaded from: classes4.dex */
public class NaturalRanking implements org.apache.commons.math.stat.ranking.RankingAlgorithm {
    public static final org.apache.commons.math.stat.ranking.NaNStrategy DEFAULT_NAN_STRATEGY = org.apache.commons.math.stat.ranking.NaNStrategy.MAXIMAL;
    public static final org.apache.commons.math.stat.ranking.TiesStrategy DEFAULT_TIES_STRATEGY = org.apache.commons.math.stat.ranking.TiesStrategy.AVERAGE;
    private final org.apache.commons.math.stat.ranking.NaNStrategy nanStrategy;
    private final org.apache.commons.math.random.RandomData randomData;
    private final org.apache.commons.math.stat.ranking.TiesStrategy tiesStrategy;

    public NaturalRanking() {
        this.tiesStrategy = DEFAULT_TIES_STRATEGY;
        this.nanStrategy = DEFAULT_NAN_STRATEGY;
        this.randomData = null;
    }

    public NaturalRanking(org.apache.commons.math.stat.ranking.TiesStrategy tiesStrategy) {
        this.tiesStrategy = tiesStrategy;
        this.nanStrategy = DEFAULT_NAN_STRATEGY;
        this.randomData = new org.apache.commons.math.random.RandomDataImpl();
    }

    public NaturalRanking(org.apache.commons.math.stat.ranking.NaNStrategy nanStrategy) {
        this.nanStrategy = nanStrategy;
        this.tiesStrategy = DEFAULT_TIES_STRATEGY;
        this.randomData = null;
    }

    public NaturalRanking(org.apache.commons.math.stat.ranking.NaNStrategy nanStrategy, org.apache.commons.math.stat.ranking.TiesStrategy tiesStrategy) {
        this.nanStrategy = nanStrategy;
        this.tiesStrategy = tiesStrategy;
        this.randomData = new org.apache.commons.math.random.RandomDataImpl();
    }

    public NaturalRanking(org.apache.commons.math.random.RandomGenerator randomGenerator) {
        this.tiesStrategy = org.apache.commons.math.stat.ranking.TiesStrategy.RANDOM;
        this.nanStrategy = DEFAULT_NAN_STRATEGY;
        this.randomData = new org.apache.commons.math.random.RandomDataImpl(randomGenerator);
    }

    public NaturalRanking(org.apache.commons.math.stat.ranking.NaNStrategy nanStrategy, org.apache.commons.math.random.RandomGenerator randomGenerator) {
        this.nanStrategy = nanStrategy;
        this.tiesStrategy = org.apache.commons.math.stat.ranking.TiesStrategy.RANDOM;
        this.randomData = new org.apache.commons.math.random.RandomDataImpl(randomGenerator);
    }

    public org.apache.commons.math.stat.ranking.NaNStrategy getNanStrategy() {
        return this.nanStrategy;
    }

    public org.apache.commons.math.stat.ranking.TiesStrategy getTiesStrategy() {
        return this.tiesStrategy;
    }

    @Override // org.apache.commons.math.stat.ranking.RankingAlgorithm
    public double[] rank(double[] data) {
        org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[] ranks = new org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[data.length];
        for (int i = 0; i < data.length; i++) {
            ranks[i] = new org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair(data[i], i);
        }
        java.util.List<java.lang.Integer> nanPositions = null;
        switch (this.nanStrategy) {
            case MAXIMAL:
                recodeNaNs(ranks, Double.POSITIVE_INFINITY);
                break;
            case MINIMAL:
                recodeNaNs(ranks, Double.NEGATIVE_INFINITY);
                break;
            case REMOVED:
                ranks = removeNaNs(ranks);
                break;
            case FIXED:
                nanPositions = getNanPositions(ranks);
                break;
            default:
                throw new org.apache.commons.math.exception.MathInternalError();
        }
        java.util.Arrays.sort(ranks);
        double[] out = new double[ranks.length];
        int pos = 1;
        out[ranks[0].getPosition()] = 1;
        java.util.List<java.lang.Integer> tiesTrace = new java.util.ArrayList<>();
        tiesTrace.add(java.lang.Integer.valueOf(ranks[0].getPosition()));
        for (int i2 = 1; i2 < ranks.length; i2++) {
            if (java.lang.Double.compare(ranks[i2].getValue(), ranks[i2 - 1].getValue()) > 0) {
                pos = i2 + 1;
                if (tiesTrace.size() > 1) {
                    resolveTie(out, tiesTrace);
                }
                tiesTrace = new java.util.ArrayList<>();
                tiesTrace.add(java.lang.Integer.valueOf(ranks[i2].getPosition()));
            } else {
                tiesTrace.add(java.lang.Integer.valueOf(ranks[i2].getPosition()));
            }
            out[ranks[i2].getPosition()] = pos;
        }
        int i3 = tiesTrace.size();
        if (i3 > 1) {
            resolveTie(out, tiesTrace);
        }
        if (this.nanStrategy == org.apache.commons.math.stat.ranking.NaNStrategy.FIXED) {
            restoreNaNs(out, nanPositions);
        }
        return out;
    }

    private org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[] removeNaNs(org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[] ranks) {
        if (!containsNaNs(ranks)) {
            return ranks;
        }
        org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[] outRanks = new org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[ranks.length];
        int j = 0;
        for (int i = 0; i < ranks.length; i++) {
            if (java.lang.Double.isNaN(ranks[i].getValue())) {
                for (int k = i + 1; k < ranks.length; k++) {
                    ranks[k] = new org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair(ranks[k].getValue(), ranks[k].getPosition() - 1);
                }
            } else {
                outRanks[j] = new org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair(ranks[i].getValue(), ranks[i].getPosition());
                j++;
            }
        }
        org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[] returnRanks = new org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[j];
        java.lang.System.arraycopy(outRanks, 0, returnRanks, 0, j);
        return returnRanks;
    }

    private void recodeNaNs(org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[] ranks, double value) {
        for (int i = 0; i < ranks.length; i++) {
            if (java.lang.Double.isNaN(ranks[i].getValue())) {
                ranks[i] = new org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair(value, ranks[i].getPosition());
            }
        }
    }

    private boolean containsNaNs(org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[] ranks) {
        for (org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair intDoublePair : ranks) {
            if (java.lang.Double.isNaN(intDoublePair.getValue())) {
                return true;
            }
        }
        return false;
    }

    private void resolveTie(double[] ranks, java.util.List<java.lang.Integer> tiesTrace) {
        double c = ranks[tiesTrace.get(0).intValue()];
        int length = tiesTrace.size();
        switch (this.tiesStrategy) {
            case AVERAGE:
                fill(ranks, tiesTrace, (((c * 2.0d) + ((double) length)) - 1.0d) / 2.0d);
                return;
            case MAXIMUM:
                fill(ranks, tiesTrace, (((double) length) + c) - 1.0d);
                return;
            case MINIMUM:
                fill(ranks, tiesTrace, c);
                return;
            case RANDOM:
                java.util.Iterator<java.lang.Integer> iterator = tiesTrace.iterator();
                long f = org.apache.commons.math.util.FastMath.round(c);
                while (iterator.hasNext()) {
                    ranks[iterator.next().intValue()] = this.randomData.nextLong(f, (((long) length) + f) - 1);
                }
                return;
            case SEQUENTIAL:
                java.util.Iterator<java.lang.Integer> iterator2 = tiesTrace.iterator();
                long f2 = org.apache.commons.math.util.FastMath.round(c);
                int i = 0;
                while (iterator2.hasNext()) {
                    ranks[iterator2.next().intValue()] = ((long) i) + f2;
                    i++;
                }
                return;
            default:
                throw new org.apache.commons.math.exception.MathInternalError();
        }
    }

    private void fill(double[] data, java.util.List<java.lang.Integer> tiesTrace, double value) {
        java.util.Iterator<java.lang.Integer> iterator = tiesTrace.iterator();
        while (iterator.hasNext()) {
            data[iterator.next().intValue()] = value;
        }
    }

    private void restoreNaNs(double[] ranks, java.util.List<java.lang.Integer> nanPositions) {
        if (nanPositions.size() == 0) {
            return;
        }
        java.util.Iterator<java.lang.Integer> iterator = nanPositions.iterator();
        while (iterator.hasNext()) {
            ranks[iterator.next().intValue()] = Double.NaN;
        }
    }

    private java.util.List<java.lang.Integer> getNanPositions(org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair[] ranks) {
        java.util.ArrayList<java.lang.Integer> out = new java.util.ArrayList<>();
        for (int i = 0; i < ranks.length; i++) {
            if (java.lang.Double.isNaN(ranks[i].getValue())) {
                out.add(java.lang.Integer.valueOf(i));
            }
        }
        return out;
    }

    private static class IntDoublePair implements java.lang.Comparable<org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair> {
        private final int position;
        private final double value;

        public IntDoublePair(double value, int position) {
            this.value = value;
            this.position = position;
        }

        @Override // java.lang.Comparable
        public int compareTo(org.apache.commons.math.stat.ranking.NaturalRanking.IntDoublePair other) {
            return java.lang.Double.compare(this.value, other.value);
        }

        public double getValue() {
            return this.value;
        }

        public int getPosition() {
            return this.position;
        }
    }
}
