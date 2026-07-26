package org.apache.commons.math.optimization.univariate;

/* JADX INFO: loaded from: classes4.dex */
public class BracketFinder {
    private static final double EPS_MIN = 1.0E-21d;
    private static final double GOLD = 1.618034d;
    private int evaluations;
    private double fHi;
    private double fLo;
    private double fMid;
    private final double growLimit;
    private double hi;
    private int iterations;
    private double lo;
    private final int maxIterations;
    private double mid;

    public BracketFinder() {
        this(100.0d, 50);
    }

    public BracketFinder(double growLimit, int maxIterations) {
        if (growLimit <= 0.0d) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(java.lang.Double.valueOf(growLimit));
        }
        if (maxIterations <= 0) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(java.lang.Integer.valueOf(maxIterations));
        }
        this.growLimit = growLimit;
        this.maxIterations = maxIterations;
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00ac, code lost:
    
        r15 = r9;
        r9 = r3;
        r7 = r11;
        r11 = r27;
        r5 = r15;
        r3 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0159, code lost:
    
        r5 = r38;
        r3 = r17;
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x014f A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void search(org.apache.commons.math.analysis.UnivariateRealFunction r34, org.apache.commons.math.optimization.GoalType r35, double r36, double r38) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        /*
            Method dump skipped, instruction units count: 362
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.optimization.univariate.BracketFinder.search(org.apache.commons.math.analysis.UnivariateRealFunction, org.apache.commons.math.optimization.GoalType, double, double):void");
    }

    public int getIterations() {
        return this.iterations;
    }

    public int getEvaluations() {
        return this.evaluations;
    }

    public double getLo() {
        return this.lo;
    }

    public double getFLow() {
        return this.fLo;
    }

    public double getHi() {
        return this.hi;
    }

    public double getFHi() {
        return this.fHi;
    }

    public double getMid() {
        return this.mid;
    }

    public double getFMid() {
        return this.fMid;
    }

    private double eval(org.apache.commons.math.analysis.UnivariateRealFunction f, double x) throws org.apache.commons.math.FunctionEvaluationException {
        this.evaluations++;
        return f.value(x);
    }

    private void reset() {
        this.iterations = 0;
        this.evaluations = 0;
    }
}
