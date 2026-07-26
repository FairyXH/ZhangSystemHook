package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class RandomDataImpl implements org.apache.commons.math.random.RandomData, java.io.Serializable {
    private static final long serialVersionUID = -626730818244969716L;
    private org.apache.commons.math.random.RandomGenerator rand;
    private java.security.SecureRandom secRand;

    public RandomDataImpl() {
        this.rand = null;
        this.secRand = null;
    }

    public RandomDataImpl(org.apache.commons.math.random.RandomGenerator rand) {
        this.rand = null;
        this.secRand = null;
        this.rand = rand;
    }

    @Override // org.apache.commons.math.random.RandomData
    public java.lang.String nextHexString(int len) {
        if (len <= 0) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.LENGTH, java.lang.Integer.valueOf(len));
        }
        org.apache.commons.math.random.RandomGenerator ran = getRan();
        java.lang.StringBuilder outBuffer = new java.lang.StringBuilder();
        byte[] randomBytes = new byte[(len / 2) + 1];
        ran.nextBytes(randomBytes);
        for (byte b : randomBytes) {
            java.lang.Integer c = java.lang.Integer.valueOf(b);
            java.lang.String hex = java.lang.Integer.toHexString(c.intValue() + 128);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            outBuffer.append(hex);
        }
        return outBuffer.toString().substring(0, len);
    }

    @Override // org.apache.commons.math.random.RandomData
    public int nextInt(int lower, int upper) {
        if (lower >= upper) {
            throw new org.apache.commons.math.exception.NumberIsTooLargeException(org.apache.commons.math.exception.util.LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, java.lang.Integer.valueOf(lower), java.lang.Integer.valueOf(upper), false);
        }
        double r = getRan().nextDouble();
        return (int) ((((double) upper) * r) + ((1.0d - r) * ((double) lower)) + r);
    }

    @Override // org.apache.commons.math.random.RandomData
    public long nextLong(long lower, long upper) {
        if (lower >= upper) {
            throw new org.apache.commons.math.exception.NumberIsTooLargeException(org.apache.commons.math.exception.util.LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, java.lang.Long.valueOf(lower), java.lang.Long.valueOf(upper), false);
        }
        double r = getRan().nextDouble();
        return (long) ((upper * r) + ((1.0d - r) * lower) + r);
    }

    @Override // org.apache.commons.math.random.RandomData
    public java.lang.String nextSecureHexString(int len) {
        if (len <= 0) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.LENGTH, java.lang.Integer.valueOf(len));
        }
        java.security.SecureRandom secRan = getSecRan();
        try {
            java.security.MessageDigest alg = java.security.MessageDigest.getInstance("SHA-1");
            alg.reset();
            int numIter = (len / 40) + 1;
            java.lang.StringBuilder outBuffer = new java.lang.StringBuilder();
            for (int iter = 1; iter < numIter + 1; iter++) {
                byte[] randomBytes = new byte[40];
                secRan.nextBytes(randomBytes);
                alg.update(randomBytes);
                byte[] hash = alg.digest();
                for (byte b : hash) {
                    java.lang.Integer c = java.lang.Integer.valueOf(b);
                    java.lang.String hex = java.lang.Integer.toHexString(c.intValue() + 128);
                    if (hex.length() == 1) {
                        hex = "0" + hex;
                    }
                    outBuffer.append(hex);
                }
            }
            return outBuffer.toString().substring(0, len);
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new org.apache.commons.math.exception.MathInternalError(ex);
        }
    }

    @Override // org.apache.commons.math.random.RandomData
    public int nextSecureInt(int lower, int upper) {
        if (lower >= upper) {
            throw new org.apache.commons.math.exception.NumberIsTooLargeException(org.apache.commons.math.exception.util.LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, java.lang.Integer.valueOf(lower), java.lang.Integer.valueOf(upper), false);
        }
        java.security.SecureRandom sec = getSecRan();
        return ((int) (sec.nextDouble() * ((double) ((upper - lower) + 1)))) + lower;
    }

    @Override // org.apache.commons.math.random.RandomData
    public long nextSecureLong(long lower, long upper) {
        if (lower >= upper) {
            throw new org.apache.commons.math.exception.NumberIsTooLargeException(org.apache.commons.math.exception.util.LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, java.lang.Long.valueOf(lower), java.lang.Long.valueOf(upper), false);
        }
        java.security.SecureRandom sec = getSecRan();
        return ((long) (sec.nextDouble() * ((upper - lower) + 1))) + lower;
    }

    @Override // org.apache.commons.math.random.RandomData
    public long nextPoisson(double mean) {
        double y;
        double y2;
        double v;
        double x;
        double x2;
        if (mean <= 0.0d) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.MEAN, java.lang.Double.valueOf(mean));
        }
        org.apache.commons.math.random.RandomGenerator generator = getRan();
        double pivot = 40.0d;
        if (mean < 40.0d) {
            double p = org.apache.commons.math.util.FastMath.exp(-mean);
            long n = 0;
            double r = 1.0d;
            while (n < 1000.0d * mean) {
                double rnd = generator.nextDouble();
                r *= rnd;
                if (r >= p) {
                    n++;
                } else {
                    return n;
                }
            }
            return n;
        }
        double lambda = org.apache.commons.math.util.FastMath.floor(mean);
        double lambdaFractional = mean - lambda;
        double logLambda = org.apache.commons.math.util.FastMath.log(lambda);
        double logLambdaFactorial = org.apache.commons.math.util.MathUtils.factorialLog((int) lambda);
        long y22 = lambdaFractional < Double.MIN_VALUE ? 0L : nextPoisson(lambdaFractional);
        double delta = org.apache.commons.math.util.FastMath.sqrt(org.apache.commons.math.util.FastMath.log(((32.0d * lambda) / 3.141592653589793d) + 1.0d) * lambda);
        double halfDelta = delta / 2.0d;
        double twolpd = (lambda * 2.0d) + delta;
        double a1 = org.apache.commons.math.util.FastMath.sqrt(3.141592653589793d * twolpd) * org.apache.commons.math.util.FastMath.exp(lambda * 0.0d);
        double a2 = (twolpd / delta) * org.apache.commons.math.util.FastMath.exp(((-delta) * (delta + 1.0d)) / twolpd);
        double aSum = a1 + a2 + 1.0d;
        double p1 = a1 / aSum;
        double p2 = a2 / aSum;
        double c1 = 1.0d / (8.0d * lambda);
        int a = 0;
        double t = 0.0d;
        while (true) {
            double aSum2 = aSum;
            org.apache.commons.math.random.RandomGenerator generator2 = generator;
            double pivot2 = pivot;
            double u = nextUniform(0.0d, 1.0d);
            if (u <= p1) {
                double n2 = nextGaussian(0.0d, 1.0d);
                x2 = (org.apache.commons.math.util.FastMath.sqrt(lambda + halfDelta) * n2) - 0.5d;
                if (x2 > delta || x2 < (-lambda)) {
                    aSum = aSum2;
                    generator = generator2;
                    pivot = pivot2;
                } else {
                    double y3 = x2 < 0.0d ? org.apache.commons.math.util.FastMath.floor(x2) : org.apache.commons.math.util.FastMath.ceil(x2);
                    double y4 = y3;
                    double e = nextExponential(1.0d);
                    double v2 = ((-e) - ((n2 * n2) / 2.0d)) + c1;
                    v = v2;
                    x = y4;
                    y2 = lambdaFractional;
                }
            } else {
                if (u > p1 + p2) {
                    y = lambda;
                    break;
                }
                double y5 = twolpd / delta;
                double x3 = (y5 * nextExponential(1.0d)) + delta;
                double y6 = org.apache.commons.math.util.FastMath.ceil(x3);
                y2 = lambdaFractional;
                v = (-nextExponential(1.0d)) - (((x3 + 1.0d) * delta) / twolpd);
                x = y6;
                x2 = x3;
            }
            int a3 = x2 < 0.0d ? 1 : 0;
            double t2 = ((x + 1.0d) * x) / (lambda * 2.0d);
            if (v < (-t2) && a3 == 0) {
                y = x + lambda;
                break;
            }
            double qr = t2 * ((((x * 2.0d) + 1.0d) / (6.0d * lambda)) - 1.0d);
            double delta2 = delta;
            double qa = qr - ((t2 * t2) / (((((double) a3) * (x + 1.0d)) + lambda) * 3.0d));
            if (v < qa) {
                y = x + lambda;
                break;
            }
            if (v > qr || v >= ((x * logLambda) - org.apache.commons.math.util.MathUtils.factorialLog((int) (x + lambda))) + logLambdaFactorial) {
                delta = delta2;
                generator = generator2;
                a = a3;
                t = t2;
                lambdaFractional = y2;
                pivot = pivot2;
                aSum = aSum2;
            } else {
                y = x + lambda;
                break;
            }
        }
        return y22 + ((long) y);
    }

    @Override // org.apache.commons.math.random.RandomData
    public double nextGaussian(double mu, double sigma) {
        if (sigma <= 0.0d) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.STANDARD_DEVIATION, java.lang.Double.valueOf(sigma));
        }
        return (getRan().nextGaussian() * sigma) + mu;
    }

    @Override // org.apache.commons.math.random.RandomData
    public double nextExponential(double mean) {
        if (mean <= 0.0d) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.MEAN, java.lang.Double.valueOf(mean));
        }
        org.apache.commons.math.random.RandomGenerator generator = getRan();
        double unif = generator.nextDouble();
        while (unif == 0.0d) {
            unif = generator.nextDouble();
        }
        return (-mean) * org.apache.commons.math.util.FastMath.log(unif);
    }

    @Override // org.apache.commons.math.random.RandomData
    public double nextUniform(double lower, double upper) {
        if (lower >= upper) {
            throw new org.apache.commons.math.exception.NumberIsTooLargeException(org.apache.commons.math.exception.util.LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, java.lang.Double.valueOf(lower), java.lang.Double.valueOf(upper), false);
        }
        org.apache.commons.math.random.RandomGenerator generator = getRan();
        double u = generator.nextDouble();
        while (u <= 0.0d) {
            u = generator.nextDouble();
        }
        return ((upper - lower) * u) + lower;
    }

    public double nextBeta(double alpha, double beta) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.BetaDistributionImpl(alpha, beta));
    }

    public int nextBinomial(int numberOfTrials, double probabilityOfSuccess) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.BinomialDistributionImpl(numberOfTrials, probabilityOfSuccess));
    }

    public double nextCauchy(double median, double scale) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.CauchyDistributionImpl(median, scale));
    }

    public double nextChiSquare(double df) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.ChiSquaredDistributionImpl(df));
    }

    public double nextF(double numeratorDf, double denominatorDf) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.FDistributionImpl(numeratorDf, denominatorDf));
    }

    public double nextGamma(double shape, double scale) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.GammaDistributionImpl(shape, scale));
    }

    public int nextHypergeometric(int populationSize, int numberOfSuccesses, int sampleSize) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.HypergeometricDistributionImpl(populationSize, numberOfSuccesses, sampleSize));
    }

    public int nextPascal(int r, double p) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.PascalDistributionImpl(r, p));
    }

    public double nextT(double df) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.TDistributionImpl(df));
    }

    public double nextWeibull(double shape, double scale) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.WeibullDistributionImpl(shape, scale));
    }

    public int nextZipf(int numberOfElements, double exponent) throws org.apache.commons.math.MathException {
        return nextInversionDeviate(new org.apache.commons.math.distribution.ZipfDistributionImpl(numberOfElements, exponent));
    }

    private org.apache.commons.math.random.RandomGenerator getRan() {
        if (this.rand == null) {
            this.rand = new org.apache.commons.math.random.JDKRandomGenerator();
            this.rand.setSeed(java.lang.System.currentTimeMillis());
        }
        return this.rand;
    }

    private java.security.SecureRandom getSecRan() {
        if (this.secRand == null) {
            this.secRand = new java.security.SecureRandom();
            this.secRand.setSeed(java.lang.System.currentTimeMillis());
        }
        return this.secRand;
    }

    public void reSeed(long seed) {
        if (this.rand == null) {
            this.rand = new org.apache.commons.math.random.JDKRandomGenerator();
        }
        this.rand.setSeed(seed);
    }

    public void reSeedSecure() {
        if (this.secRand == null) {
            this.secRand = new java.security.SecureRandom();
        }
        this.secRand.setSeed(java.lang.System.currentTimeMillis());
    }

    public void reSeedSecure(long seed) {
        if (this.secRand == null) {
            this.secRand = new java.security.SecureRandom();
        }
        this.secRand.setSeed(seed);
    }

    public void reSeed() {
        if (this.rand == null) {
            this.rand = new org.apache.commons.math.random.JDKRandomGenerator();
        }
        this.rand.setSeed(java.lang.System.currentTimeMillis());
    }

    public void setSecureAlgorithm(java.lang.String algorithm, java.lang.String provider) throws java.security.NoSuchAlgorithmException, java.security.NoSuchProviderException {
        this.secRand = java.security.SecureRandom.getInstance(algorithm, provider);
    }

    @Override // org.apache.commons.math.random.RandomData
    public int[] nextPermutation(int n, int k) {
        if (k > n) {
            throw new org.apache.commons.math.exception.NumberIsTooLargeException(org.apache.commons.math.exception.util.LocalizedFormats.PERMUTATION_EXCEEDS_N, java.lang.Integer.valueOf(k), java.lang.Integer.valueOf(n), true);
        }
        if (k == 0) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.PERMUTATION_SIZE, java.lang.Integer.valueOf(k));
        }
        int[] index = getNatural(n);
        shuffle(index, n - k);
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = index[(n - i) - 1];
        }
        return result;
    }

    @Override // org.apache.commons.math.random.RandomData
    public java.lang.Object[] nextSample(java.util.Collection<?> c, int k) {
        int len = c.size();
        if (k > len) {
            throw new org.apache.commons.math.exception.NumberIsTooLargeException(org.apache.commons.math.exception.util.LocalizedFormats.SAMPLE_SIZE_EXCEEDS_COLLECTION_SIZE, java.lang.Integer.valueOf(k), java.lang.Integer.valueOf(len), true);
        }
        if (k <= 0) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.NUMBER_OF_SAMPLES, java.lang.Integer.valueOf(k));
        }
        java.lang.Object[] objects = c.toArray();
        int[] index = nextPermutation(len, k);
        java.lang.Object[] result = new java.lang.Object[k];
        for (int i = 0; i < k; i++) {
            result[i] = objects[index[i]];
        }
        return result;
    }

    public double nextInversionDeviate(org.apache.commons.math.distribution.ContinuousDistribution distribution) throws org.apache.commons.math.MathException {
        return distribution.inverseCumulativeProbability(nextUniform(0.0d, 1.0d));
    }

    public int nextInversionDeviate(org.apache.commons.math.distribution.IntegerDistribution distribution) throws org.apache.commons.math.MathException {
        double target = nextUniform(0.0d, 1.0d);
        int glb = distribution.inverseCumulativeProbability(target);
        if (distribution.cumulativeProbability(glb) == 1.0d) {
            return glb;
        }
        return glb + 1;
    }

    private void shuffle(int[] list, int end) {
        int target;
        for (int i = list.length - 1; i >= end; i--) {
            if (i == 0) {
                target = 0;
            } else {
                target = nextInt(0, i);
            }
            int temp = list[target];
            list[target] = list[i];
            list[i] = temp;
        }
    }

    private int[] getNatural(int n) {
        int[] natural = new int[n];
        for (int i = 0; i < n; i++) {
            natural[i] = i;
        }
        return natural;
    }
}
