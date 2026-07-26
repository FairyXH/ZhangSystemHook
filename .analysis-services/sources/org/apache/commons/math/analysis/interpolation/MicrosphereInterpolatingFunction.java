package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
public class MicrosphereInterpolatingFunction implements org.apache.commons.math.analysis.MultivariateRealFunction {
    private final double brightnessExponent;
    private final int dimension;
    private final java.util.List<org.apache.commons.math.analysis.interpolation.MicrosphereInterpolatingFunction.MicrosphereSurfaceElement> microsphere;
    private final java.util.Map<org.apache.commons.math.linear.RealVector, java.lang.Double> samples;

    private static class MicrosphereSurfaceElement {
        private double brightestIllumination;
        private java.util.Map.Entry<org.apache.commons.math.linear.RealVector, java.lang.Double> brightestSample;
        private final org.apache.commons.math.linear.RealVector normal;

        MicrosphereSurfaceElement(double[] n) {
            this.normal = new org.apache.commons.math.linear.ArrayRealVector(n);
        }

        org.apache.commons.math.linear.RealVector normal() {
            return this.normal;
        }

        void reset() {
            this.brightestIllumination = 0.0d;
            this.brightestSample = null;
        }

        void store(double illuminationFromSample, java.util.Map.Entry<org.apache.commons.math.linear.RealVector, java.lang.Double> sample) {
            if (illuminationFromSample > this.brightestIllumination) {
                this.brightestIllumination = illuminationFromSample;
                this.brightestSample = sample;
            }
        }

        double illumination() {
            return this.brightestIllumination;
        }

        java.util.Map.Entry<org.apache.commons.math.linear.RealVector, java.lang.Double> sample() {
            return this.brightestSample;
        }
    }

    public MicrosphereInterpolatingFunction(double[][] xval, double[] yval, int brightnessExponent, int microsphereElements, org.apache.commons.math.random.UnitSphereRandomVectorGenerator rand) throws org.apache.commons.math.exception.NoDataException, org.apache.commons.math.DimensionMismatchException {
        if (xval.length == 0 || xval[0] == null) {
            throw new org.apache.commons.math.exception.NoDataException();
        }
        if (xval.length != yval.length) {
            throw new org.apache.commons.math.DimensionMismatchException(xval.length, yval.length);
        }
        this.dimension = xval[0].length;
        this.brightnessExponent = brightnessExponent;
        this.samples = new java.util.HashMap(yval.length);
        for (int i = 0; i < xval.length; i++) {
            double[] xvalI = xval[i];
            if (xvalI.length != this.dimension) {
                throw new org.apache.commons.math.DimensionMismatchException(xvalI.length, this.dimension);
            }
            this.samples.put(new org.apache.commons.math.linear.ArrayRealVector(xvalI), java.lang.Double.valueOf(yval[i]));
        }
        this.microsphere = new java.util.ArrayList(microsphereElements);
        for (int i2 = 0; i2 < microsphereElements; i2++) {
            this.microsphere.add(new org.apache.commons.math.analysis.interpolation.MicrosphereInterpolatingFunction.MicrosphereSurfaceElement(rand.nextVector()));
        }
    }

    @Override // org.apache.commons.math.analysis.MultivariateRealFunction
    public double value(double[] point) {
        org.apache.commons.math.linear.RealVector p = new org.apache.commons.math.linear.ArrayRealVector(point);
        java.util.Iterator<org.apache.commons.math.analysis.interpolation.MicrosphereInterpolatingFunction.MicrosphereSurfaceElement> it = this.microsphere.iterator();
        while (it.hasNext()) {
            it.next().reset();
        }
        for (java.util.Map.Entry<org.apache.commons.math.linear.RealVector, java.lang.Double> sd : this.samples.entrySet()) {
            org.apache.commons.math.linear.RealVector diff = sd.getKey().subtract(p);
            double diffNorm = diff.getNorm();
            if (org.apache.commons.math.util.FastMath.abs(diffNorm) < org.apache.commons.math.util.FastMath.ulp(1.0d)) {
                return sd.getValue().doubleValue();
            }
            for (org.apache.commons.math.analysis.interpolation.MicrosphereInterpolatingFunction.MicrosphereSurfaceElement md : this.microsphere) {
                double w = org.apache.commons.math.util.FastMath.pow(diffNorm, -this.brightnessExponent);
                md.store(cosAngle(diff, md.normal()) * w, sd);
            }
        }
        double value = 0.0d;
        double totalWeight = 0.0d;
        for (org.apache.commons.math.analysis.interpolation.MicrosphereInterpolatingFunction.MicrosphereSurfaceElement md2 : this.microsphere) {
            double iV = md2.illumination();
            java.util.Map.Entry<org.apache.commons.math.linear.RealVector, java.lang.Double> sd2 = md2.sample();
            if (sd2 != null) {
                value += sd2.getValue().doubleValue() * iV;
                totalWeight += iV;
            }
        }
        return value / totalWeight;
    }

    private double cosAngle(org.apache.commons.math.linear.RealVector v, org.apache.commons.math.linear.RealVector w) {
        return v.dotProduct(w) / (v.getNorm() * w.getNorm());
    }
}
