package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface RealVector {
    org.apache.commons.math.linear.RealVector add(org.apache.commons.math.linear.RealVector realVector);

    org.apache.commons.math.linear.RealVector add(double[] dArr);

    org.apache.commons.math.linear.RealVector append(double d);

    org.apache.commons.math.linear.RealVector append(org.apache.commons.math.linear.RealVector realVector);

    org.apache.commons.math.linear.RealVector append(double[] dArr);

    org.apache.commons.math.linear.RealVector copy();

    double dotProduct(org.apache.commons.math.linear.RealVector realVector);

    double dotProduct(double[] dArr);

    org.apache.commons.math.linear.RealVector ebeDivide(org.apache.commons.math.linear.RealVector realVector);

    org.apache.commons.math.linear.RealVector ebeDivide(double[] dArr);

    org.apache.commons.math.linear.RealVector ebeMultiply(org.apache.commons.math.linear.RealVector realVector);

    org.apache.commons.math.linear.RealVector ebeMultiply(double[] dArr);

    double[] getData();

    int getDimension();

    double getDistance(org.apache.commons.math.linear.RealVector realVector);

    double getDistance(double[] dArr);

    double getEntry(int i);

    double getL1Distance(org.apache.commons.math.linear.RealVector realVector);

    double getL1Distance(double[] dArr);

    double getL1Norm();

    double getLInfDistance(org.apache.commons.math.linear.RealVector realVector);

    double getLInfDistance(double[] dArr);

    double getLInfNorm();

    double getNorm();

    org.apache.commons.math.linear.RealVector getSubVector(int i, int i2);

    boolean isInfinite();

    boolean isNaN();

    java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> iterator();

    org.apache.commons.math.linear.RealVector map(org.apache.commons.math.analysis.UnivariateRealFunction univariateRealFunction) throws org.apache.commons.math.FunctionEvaluationException;

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapAbs();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapAbsToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapAcos();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapAcosToSelf();

    org.apache.commons.math.linear.RealVector mapAdd(double d);

    org.apache.commons.math.linear.RealVector mapAddToSelf(double d);

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapAsin();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapAsinToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapAtan();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapAtanToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapCbrt();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapCbrtToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapCeil();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapCeilToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapCos();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapCosToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapCosh();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapCoshToSelf();

    org.apache.commons.math.linear.RealVector mapDivide(double d);

    org.apache.commons.math.linear.RealVector mapDivideToSelf(double d);

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapExp();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapExpToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapExpm1();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapExpm1ToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapFloor();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapFloorToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapInv();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapInvToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapLog();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapLog10();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapLog10ToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapLog1p();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapLog1pToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapLogToSelf();

    org.apache.commons.math.linear.RealVector mapMultiply(double d);

    org.apache.commons.math.linear.RealVector mapMultiplyToSelf(double d);

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapPow(double d);

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapPowToSelf(double d);

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapRint();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapRintToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapSignum();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapSignumToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapSin();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapSinToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapSinh();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapSinhToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapSqrt();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapSqrtToSelf();

    org.apache.commons.math.linear.RealVector mapSubtract(double d);

    org.apache.commons.math.linear.RealVector mapSubtractToSelf(double d);

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapTan();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapTanToSelf();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapTanh();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapTanhToSelf();

    org.apache.commons.math.linear.RealVector mapToSelf(org.apache.commons.math.analysis.UnivariateRealFunction univariateRealFunction) throws org.apache.commons.math.FunctionEvaluationException;

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapUlp();

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealVector mapUlpToSelf();

    org.apache.commons.math.linear.RealMatrix outerProduct(org.apache.commons.math.linear.RealVector realVector);

    org.apache.commons.math.linear.RealMatrix outerProduct(double[] dArr);

    org.apache.commons.math.linear.RealVector projection(org.apache.commons.math.linear.RealVector realVector);

    org.apache.commons.math.linear.RealVector projection(double[] dArr);

    void set(double d);

    void setEntry(int i, double d);

    void setSubVector(int i, org.apache.commons.math.linear.RealVector realVector);

    void setSubVector(int i, double[] dArr);

    java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> sparseIterator();

    org.apache.commons.math.linear.RealVector subtract(org.apache.commons.math.linear.RealVector realVector);

    org.apache.commons.math.linear.RealVector subtract(double[] dArr);

    double[] toArray();

    org.apache.commons.math.linear.RealVector unitVector();

    void unitize();

    public static abstract class Entry {
        private int index;

        public abstract double getValue();

        public abstract void setValue(double d);

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
