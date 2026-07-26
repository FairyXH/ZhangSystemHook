package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class HarmonicCoefficientsGuesser {
    private final org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] observations;
    private double phi;
    private double a = Double.NaN;
    private double omega = Double.NaN;

    public HarmonicCoefficientsGuesser(org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] observations) {
        this.observations = (org.apache.commons.math.optimization.fitting.WeightedObservedPoint[]) observations.clone();
    }

    public void guess() throws org.apache.commons.math.optimization.OptimizationException {
        sortObservations();
        guessAOmega();
        guessPhi();
    }

    private void sortObservations() {
        org.apache.commons.math.optimization.fitting.WeightedObservedPoint curr = this.observations[0];
        for (int j = 1; j < this.observations.length; j++) {
            org.apache.commons.math.optimization.fitting.WeightedObservedPoint prec = curr;
            curr = this.observations[j];
            if (curr.getX() < prec.getX()) {
                int i = j - 1;
                org.apache.commons.math.optimization.fitting.WeightedObservedPoint mI = this.observations[i];
                while (i >= 0 && curr.getX() < mI.getX()) {
                    this.observations[i + 1] = mI;
                    int i2 = i - 1;
                    if (i == 0) {
                        i = i2;
                    } else {
                        mI = this.observations[i2];
                        i = i2;
                    }
                }
                this.observations[i + 1] = curr;
                curr = this.observations[j];
            }
        }
    }

    private void guessAOmega() throws org.apache.commons.math.optimization.OptimizationException {
        double sx2 = 0.0d;
        double sy2 = 0.0d;
        double sxy = 0.0d;
        double sxz = 0.0d;
        double syz = 0.0d;
        double currentX = this.observations[0].getX();
        double currentY = this.observations[0].getY();
        double f2Integral = 0.0d;
        double fPrime2Integral = 0.0d;
        for (int i = 1; i < this.observations.length; i++) {
            double previousX = currentX;
            double previousY = currentY;
            currentX = this.observations[i].getX();
            currentY = this.observations[i].getY();
            double dx = currentX - previousX;
            double dy = currentY - previousY;
            double f2StepIntegral = ((((previousY * previousY) + (previousY * currentY)) + (currentY * currentY)) * dx) / 3.0d;
            double fPrime2StepIntegral = (dy * dy) / dx;
            double x = currentX - currentX;
            f2Integral += f2StepIntegral;
            fPrime2Integral += fPrime2StepIntegral;
            sx2 += x * x;
            sy2 += f2Integral * f2Integral;
            sxy += x * f2Integral;
            sxz += x * fPrime2Integral;
            syz += f2Integral * fPrime2Integral;
        }
        double c1 = (sy2 * sxz) - (sxy * syz);
        double c2 = (sxy * sxz) - (sx2 * syz);
        double c3 = (sx2 * sy2) - (sxy * sxy);
        if (c1 / c2 < 0.0d || c2 / c3 < 0.0d) {
            throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_FIRST_GUESS_HARMONIC_COEFFICIENTS, new java.lang.Object[0]);
        }
        double sx22 = org.apache.commons.math.util.FastMath.sqrt(c1 / c2);
        this.a = sx22;
        this.omega = org.apache.commons.math.util.FastMath.sqrt(c2 / c3);
    }

    private void guessPhi() {
        double fcMean = 0.0d;
        double fsMean = 0.0d;
        double currentX = this.observations[0].getX();
        double currentY = this.observations[0].getY();
        int i = 1;
        while (i < this.observations.length) {
            double previousX = currentX;
            double previousY = currentY;
            double currentX2 = this.observations[i].getX();
            currentY = this.observations[i].getY();
            double currentYPrime = (currentY - previousY) / (currentX2 - previousX);
            double previousX2 = this.omega;
            double omegaX = previousX2 * currentX2;
            double cosine = org.apache.commons.math.util.FastMath.cos(omegaX);
            double sine = org.apache.commons.math.util.FastMath.sin(omegaX);
            double currentX3 = this.omega;
            fcMean += ((currentX3 * currentY) * cosine) - (currentYPrime * sine);
            fsMean += (this.omega * currentY * sine) + (currentYPrime * cosine);
            i++;
            currentX = currentX2;
        }
        this.phi = org.apache.commons.math.util.FastMath.atan2(-fsMean, fcMean);
    }

    public double getGuessedAmplitude() {
        return this.a;
    }

    public double getGuessedPulsation() {
        return this.omega;
    }

    public double getGuessedPhase() {
        return this.phi;
    }
}
