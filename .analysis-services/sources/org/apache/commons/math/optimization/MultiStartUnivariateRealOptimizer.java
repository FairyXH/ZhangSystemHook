package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public class MultiStartUnivariateRealOptimizer implements org.apache.commons.math.optimization.UnivariateRealOptimizer {
    private static final long serialVersionUID = 5983375963110961019L;
    private org.apache.commons.math.random.RandomGenerator generator;
    private int maxEvaluations;
    private int maxIterations;
    private double[] optimaValues;
    private final org.apache.commons.math.optimization.UnivariateRealOptimizer optimizer;
    private int starts;
    private int totalEvaluations;
    private int totalIterations = 0;
    private double[] optima = null;

    public MultiStartUnivariateRealOptimizer(org.apache.commons.math.optimization.UnivariateRealOptimizer optimizer, int starts, org.apache.commons.math.random.RandomGenerator generator) {
        this.optimizer = optimizer;
        this.starts = starts;
        this.generator = generator;
        setMaximalIterationCount(Integer.MAX_VALUE);
        setMaxEvaluations(Integer.MAX_VALUE);
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public double getFunctionValue() {
        return this.optimaValues[0];
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public double getResult() {
        return this.optima[0];
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public double getAbsoluteAccuracy() {
        return this.optimizer.getAbsoluteAccuracy();
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public int getIterationCount() {
        return this.totalIterations;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public int getMaximalIterationCount() {
        return this.maxIterations;
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public int getEvaluations() {
        return this.totalEvaluations;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public double getRelativeAccuracy() {
        return this.optimizer.getRelativeAccuracy();
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void resetAbsoluteAccuracy() {
        this.optimizer.resetAbsoluteAccuracy();
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void resetMaximalIterationCount() {
        this.optimizer.resetMaximalIterationCount();
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void resetRelativeAccuracy() {
        this.optimizer.resetRelativeAccuracy();
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void setAbsoluteAccuracy(double accuracy) {
        this.optimizer.setAbsoluteAccuracy(accuracy);
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void setMaximalIterationCount(int count) {
        this.maxIterations = count;
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void setRelativeAccuracy(double accuracy) {
        this.optimizer.setRelativeAccuracy(accuracy);
    }

    public double[] getOptima() throws java.lang.IllegalStateException {
        if (this.optima == null) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new java.lang.Object[0]);
        }
        return (double[]) this.optima.clone();
    }

    public double[] getOptimaValues() throws java.lang.IllegalStateException {
        if (this.optimaValues == null) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new java.lang.Object[0]);
        }
        return (double[]) this.optimaValues.clone();
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public double optimize(org.apache.commons.math.analysis.UnivariateRealFunction f, org.apache.commons.math.optimization.GoalType goalType, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        this.optima = new double[this.starts];
        this.optimaValues = new double[this.starts];
        this.totalIterations = 0;
        this.totalEvaluations = 0;
        int i = 0;
        while (i < this.starts) {
            try {
                this.optimizer.setMaximalIterationCount(this.maxIterations - this.totalIterations);
                this.optimizer.setMaxEvaluations(this.maxEvaluations - this.totalEvaluations);
                double bound1 = i == 0 ? min : min + (this.generator.nextDouble() * (max - min));
                double bound2 = i == 0 ? max : min + (this.generator.nextDouble() * (max - min));
                this.optima[i] = this.optimizer.optimize(f, goalType, org.apache.commons.math.util.FastMath.min(bound1, bound2), org.apache.commons.math.util.FastMath.max(bound1, bound2));
                this.optimaValues[i] = this.optimizer.getFunctionValue();
            } catch (org.apache.commons.math.ConvergenceException e) {
                this.optima[i] = Double.NaN;
                this.optimaValues[i] = Double.NaN;
            } catch (org.apache.commons.math.FunctionEvaluationException e2) {
                this.optima[i] = Double.NaN;
                this.optimaValues[i] = Double.NaN;
            }
            this.totalIterations += this.optimizer.getIterationCount();
            this.totalEvaluations += this.optimizer.getEvaluations();
            i++;
        }
        int lastNaN = this.optima.length;
        for (int i2 = 0; i2 < lastNaN; i2++) {
            if (java.lang.Double.isNaN(this.optima[i2])) {
                int lastNaN2 = lastNaN - 1;
                this.optima[i2] = this.optima[lastNaN2];
                this.optima[lastNaN2 + 1] = Double.NaN;
                lastNaN = lastNaN2 - 1;
                this.optimaValues[i2] = this.optimaValues[lastNaN];
                this.optimaValues[lastNaN + 1] = Double.NaN;
            }
        }
        double d = this.optima[0];
        double currY = this.optimaValues[0];
        for (int j = 1; j < lastNaN; j++) {
            double prevY = currY;
            double currX = this.optima[j];
            currY = this.optimaValues[j];
            if ((goalType == org.apache.commons.math.optimization.GoalType.MAXIMIZE) ^ (currY < prevY)) {
                int i3 = j - 1;
                double mIX = this.optima[i3];
                double mIY = this.optimaValues[i3];
                while (i3 >= 0) {
                    if (!((goalType == org.apache.commons.math.optimization.GoalType.MAXIMIZE) ^ (currY < mIY))) {
                        break;
                    }
                    this.optima[i3 + 1] = mIX;
                    this.optimaValues[i3 + 1] = mIY;
                    int i4 = i3 - 1;
                    if (i3 != 0) {
                        mIX = this.optima[i4];
                        mIY = this.optimaValues[i4];
                        i3 = i4;
                    } else {
                        mIX = Double.NaN;
                        mIY = Double.NaN;
                        i3 = i4;
                    }
                }
                this.optima[i3 + 1] = currX;
                this.optimaValues[i3 + 1] = currY;
                double currX2 = this.optima[j];
                currY = this.optimaValues[j];
            }
        }
        if (!java.lang.Double.isNaN(this.optima[0])) {
            return this.optima[0];
        }
        throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, java.lang.Integer.valueOf(this.starts));
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public double optimize(org.apache.commons.math.analysis.UnivariateRealFunction f, org.apache.commons.math.optimization.GoalType goalType, double min, double max, double startValue) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        return optimize(f, goalType, min, max);
    }
}
