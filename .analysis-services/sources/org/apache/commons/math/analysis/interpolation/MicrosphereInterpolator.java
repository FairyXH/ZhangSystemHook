package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
public class MicrosphereInterpolator implements org.apache.commons.math.analysis.interpolation.MultivariateRealInterpolator {
    public static final int DEFAULT_BRIGHTNESS_EXPONENT = 2;
    public static final int DEFAULT_MICROSPHERE_ELEMENTS = 2000;
    private int brightnessExponent;
    private int microsphereElements;

    public MicrosphereInterpolator() {
        this(2000, 2);
    }

    public MicrosphereInterpolator(int microsphereElements, int brightnessExponent) {
        setMicropshereElements(microsphereElements);
        setBrightnessExponent(brightnessExponent);
    }

    @Override // org.apache.commons.math.analysis.interpolation.MultivariateRealInterpolator
    public org.apache.commons.math.analysis.MultivariateRealFunction interpolate(double[][] xval, double[] yval) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        org.apache.commons.math.random.UnitSphereRandomVectorGenerator rand = new org.apache.commons.math.random.UnitSphereRandomVectorGenerator(xval[0].length);
        return new org.apache.commons.math.analysis.interpolation.MicrosphereInterpolatingFunction(xval, yval, this.brightnessExponent, this.microsphereElements, rand);
    }

    public void setBrightnessExponent(int exponent) {
        if (exponent < 0) {
            throw new org.apache.commons.math.exception.NotPositiveException(java.lang.Integer.valueOf(exponent));
        }
        this.brightnessExponent = exponent;
    }

    public void setMicropshereElements(int elements) {
        if (elements <= 0) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(java.lang.Integer.valueOf(elements));
        }
        this.microsphereElements = elements;
    }
}
