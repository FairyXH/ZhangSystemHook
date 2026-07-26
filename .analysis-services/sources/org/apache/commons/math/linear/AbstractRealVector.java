package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractRealVector implements org.apache.commons.math.linear.RealVector {
    @Override // org.apache.commons.math.linear.RealVector
    public abstract org.apache.commons.math.linear.AbstractRealVector copy();

    protected void checkVectorDimensions(org.apache.commons.math.linear.RealVector v) {
        checkVectorDimensions(v.getDimension());
    }

    protected void checkVectorDimensions(int n) throws org.apache.commons.math.exception.DimensionMismatchException {
        int d = getDimension();
        if (d != n) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(d, n);
        }
    }

    protected void checkIndex(int index) throws org.apache.commons.math.linear.MatrixIndexException {
        if (index < 0 || index >= getDimension()) {
            throw new org.apache.commons.math.linear.MatrixIndexException(org.apache.commons.math.exception.util.LocalizedFormats.INDEX_OUT_OF_RANGE, java.lang.Integer.valueOf(index), 0, java.lang.Integer.valueOf(getDimension() - 1));
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public void setSubVector(int index, org.apache.commons.math.linear.RealVector v) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        checkIndex((v.getDimension() + index) - 1);
        setSubVector(index, v.getData());
    }

    @Override // org.apache.commons.math.linear.RealVector
    public void setSubVector(int index, double[] v) throws org.apache.commons.math.linear.MatrixIndexException {
        checkIndex(index);
        checkIndex((v.length + index) - 1);
        for (int i = 0; i < v.length; i++) {
            setEntry(i + index, v[i]);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector add(double[] v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        double[] result = (double[]) v.clone();
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            int index = e.getIndex();
            result[index] = result[index] + e.getValue();
        }
        return new org.apache.commons.math.linear.ArrayRealVector(result, false);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector add(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            double[] values = ((org.apache.commons.math.linear.ArrayRealVector) v).getDataRef();
            return add(values);
        }
        org.apache.commons.math.linear.RealVector result = v.copy();
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            int index = e.getIndex();
            result.setEntry(index, e.getValue() + result.getEntry(index));
        }
        return result;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector subtract(double[] v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        double[] result = (double[]) v.clone();
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            int index = e.getIndex();
            result[index] = e.getValue() - result[index];
        }
        return new org.apache.commons.math.linear.ArrayRealVector(result, false);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector subtract(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        if (v instanceof org.apache.commons.math.linear.ArrayRealVector) {
            double[] values = ((org.apache.commons.math.linear.ArrayRealVector) v).getDataRef();
            return add(values);
        }
        org.apache.commons.math.linear.RealVector result = v.copy();
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            int index = e.getIndex();
            v.setEntry(index, e.getValue() - result.getEntry(index));
        }
        return result;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAdd(double d) {
        return copy().mapAddToSelf(d);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAddToSelf(double d) {
        if (d != 0.0d) {
            try {
                return mapToSelf(org.apache.commons.math.analysis.BinaryFunction.ADD.fix1stArgument(d));
            } catch (org.apache.commons.math.FunctionEvaluationException e) {
                throw new java.lang.IllegalArgumentException(e);
            }
        }
        return this;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double dotProduct(double[] v) throws java.lang.IllegalArgumentException {
        return dotProduct(new org.apache.commons.math.linear.ArrayRealVector(v, false));
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double dotProduct(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        checkVectorDimensions(v);
        double d = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            d += e.getValue() * v.getEntry(e.getIndex());
        }
        return d;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector ebeDivide(double[] v) throws java.lang.IllegalArgumentException {
        return ebeDivide(new org.apache.commons.math.linear.ArrayRealVector(v, false));
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector ebeMultiply(double[] v) throws java.lang.IllegalArgumentException {
        return ebeMultiply(new org.apache.commons.math.linear.ArrayRealVector(v, false));
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getDistance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        checkVectorDimensions(v);
        double d = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = iterator();
        while (it.hasNext() && (e = it.next()) != null) {
            double diff = e.getValue() - v.getEntry(e.getIndex());
            d += diff * diff;
        }
        return org.apache.commons.math.util.FastMath.sqrt(d);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getNorm() {
        org.apache.commons.math.linear.RealVector.Entry e;
        double sum = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            double value = e.getValue();
            sum += value * value;
        }
        return org.apache.commons.math.util.FastMath.sqrt(sum);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getL1Norm() {
        org.apache.commons.math.linear.RealVector.Entry e;
        double norm = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            norm += org.apache.commons.math.util.FastMath.abs(e.getValue());
        }
        return norm;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getLInfNorm() {
        org.apache.commons.math.linear.RealVector.Entry e;
        double norm = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = sparseIterator();
        while (it.hasNext() && (e = it.next()) != null) {
            norm = org.apache.commons.math.util.FastMath.max(norm, org.apache.commons.math.util.FastMath.abs(e.getValue()));
        }
        return norm;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getDistance(double[] v) throws java.lang.IllegalArgumentException {
        return getDistance(new org.apache.commons.math.linear.ArrayRealVector(v, false));
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getL1Distance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        checkVectorDimensions(v);
        double d = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = iterator();
        while (it.hasNext() && (e = it.next()) != null) {
            d += org.apache.commons.math.util.FastMath.abs(e.getValue() - v.getEntry(e.getIndex()));
        }
        return d;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getL1Distance(double[] v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        checkVectorDimensions(v.length);
        double d = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = iterator();
        while (it.hasNext() && (e = it.next()) != null) {
            d += org.apache.commons.math.util.FastMath.abs(e.getValue() - v[e.getIndex()]);
        }
        return d;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getLInfDistance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        checkVectorDimensions(v);
        double d = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = iterator();
        while (it.hasNext() && (e = it.next()) != null) {
            d = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(e.getValue() - v.getEntry(e.getIndex())), d);
        }
        return d;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double getLInfDistance(double[] v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealVector.Entry e;
        checkVectorDimensions(v.length);
        double d = 0.0d;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = iterator();
        while (it.hasNext() && (e = it.next()) != null) {
            d = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(e.getValue() - v[e.getIndex()]), d);
        }
        return d;
    }

    public int getMinIndex() {
        int minIndex = -1;
        double minValue = Double.POSITIVE_INFINITY;
        for (org.apache.commons.math.linear.RealVector.Entry entry : this) {
            if (entry.getValue() <= minValue) {
                minIndex = entry.getIndex();
                minValue = entry.getValue();
            }
        }
        return minIndex;
    }

    public double getMinValue() {
        int minIndex = getMinIndex();
        if (minIndex < 0) {
            return Double.NaN;
        }
        return getEntry(minIndex);
    }

    public int getMaxIndex() {
        int maxIndex = -1;
        double maxValue = Double.NEGATIVE_INFINITY;
        for (org.apache.commons.math.linear.RealVector.Entry entry : this) {
            if (entry.getValue() >= maxValue) {
                maxIndex = entry.getIndex();
                maxValue = entry.getValue();
            }
        }
        return maxIndex;
    }

    public double getMaxValue() {
        int maxIndex = getMaxIndex();
        if (maxIndex < 0) {
            return Double.NaN;
        }
        return getEntry(maxIndex);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAbs() {
        return copy().mapAbsToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAbsToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.ABS);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAcos() {
        return copy().mapAcosToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAcosToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.ACOS);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAsin() {
        return copy().mapAsinToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAsinToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.ASIN);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAtan() {
        return copy().mapAtanToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapAtanToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.ATAN);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCbrt() {
        return copy().mapCbrtToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCbrtToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.CBRT);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCeil() {
        return copy().mapCeilToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCeilToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.CEIL);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCos() {
        return copy().mapCosToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCosToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.COS);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCosh() {
        return copy().mapCoshToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapCoshToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.COSH);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapDivide(double d) {
        return copy().mapDivideToSelf(d);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapDivideToSelf(double d) {
        try {
            return mapToSelf(org.apache.commons.math.analysis.BinaryFunction.DIVIDE.fix2ndArgument(d));
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapExp() {
        return copy().mapExpToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapExpToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.EXP);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapExpm1() {
        return copy().mapExpm1ToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapExpm1ToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.EXPM1);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapFloor() {
        return copy().mapFloorToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapFloorToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.FLOOR);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapInv() {
        return copy().mapInvToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapInvToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.INVERT);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapLog() {
        return copy().mapLogToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapLogToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.LOG);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapLog10() {
        return copy().mapLog10ToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapLog10ToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.LOG10);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapLog1p() {
        return copy().mapLog1pToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapLog1pToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.LOG1P);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapMultiply(double d) {
        return copy().mapMultiplyToSelf(d);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapMultiplyToSelf(double d) {
        try {
            return mapToSelf(org.apache.commons.math.analysis.BinaryFunction.MULTIPLY.fix1stArgument(d));
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapPow(double d) {
        return copy().mapPowToSelf(d);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapPowToSelf(double d) {
        try {
            return mapToSelf(org.apache.commons.math.analysis.BinaryFunction.POW.fix2ndArgument(d));
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapRint() {
        return copy().mapRintToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapRintToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.RINT);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSignum() {
        return copy().mapSignumToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSignumToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.SIGNUM);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSin() {
        return copy().mapSinToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSinToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.SIN);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSinh() {
        return copy().mapSinhToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSinhToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.SINH);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSqrt() {
        return copy().mapSqrtToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSqrtToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.SQRT);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSubtract(double d) {
        return copy().mapSubtractToSelf(d);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapSubtractToSelf(double d) {
        return mapAddToSelf(-d);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapTan() {
        return copy().mapTanToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapTanToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.TAN);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapTanh() {
        return copy().mapTanhToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapTanhToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.TANH);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapUlp() {
        return copy().mapUlpToSelf();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapUlpToSelf() {
        try {
            return mapToSelf(org.apache.commons.math.analysis.ComposableFunction.ULP);
        } catch (org.apache.commons.math.FunctionEvaluationException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealMatrix outerProduct(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.linear.RealMatrix product;
        org.apache.commons.math.linear.RealVector.Entry thisE;
        org.apache.commons.math.linear.RealVector.Entry otherE;
        if ((v instanceof org.apache.commons.math.linear.SparseRealVector) || (this instanceof org.apache.commons.math.linear.SparseRealVector)) {
            product = new org.apache.commons.math.linear.OpenMapRealMatrix(getDimension(), v.getDimension());
        } else {
            product = new org.apache.commons.math.linear.Array2DRowRealMatrix(getDimension(), v.getDimension());
        }
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> thisIt = sparseIterator();
        while (thisIt.hasNext() && (thisE = thisIt.next()) != null) {
            java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> otherIt = v.sparseIterator();
            while (otherIt.hasNext() && (otherE = otherIt.next()) != null) {
                product.setEntry(thisE.getIndex(), otherE.getIndex(), thisE.getValue() * otherE.getValue());
            }
        }
        return product;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealMatrix outerProduct(double[] v) throws java.lang.IllegalArgumentException {
        return outerProduct(new org.apache.commons.math.linear.ArrayRealVector(v, false));
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector projection(double[] v) throws java.lang.IllegalArgumentException {
        return projection(new org.apache.commons.math.linear.ArrayRealVector(v, false));
    }

    @Override // org.apache.commons.math.linear.RealVector
    public void set(double value) {
        org.apache.commons.math.linear.RealVector.Entry e;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = iterator();
        while (it.hasNext() && (e = it.next()) != null) {
            e.setValue(value);
        }
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double[] toArray() {
        int dim = getDimension();
        double[] values = new double[dim];
        for (int i = 0; i < dim; i++) {
            values[i] = getEntry(i);
        }
        return values;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public double[] getData() {
        return toArray();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector unitVector() {
        org.apache.commons.math.linear.RealVector copy = copy();
        copy.unitize();
        return copy;
    }

    @Override // org.apache.commons.math.linear.RealVector
    public void unitize() {
        mapDivideToSelf(getNorm());
    }

    @Override // org.apache.commons.math.linear.RealVector
    public java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> sparseIterator() {
        return new org.apache.commons.math.linear.AbstractRealVector.SparseEntryIterator();
    }

    @Override // org.apache.commons.math.linear.RealVector
    public java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> iterator() {
        final int dim = getDimension();
        return new java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry>() { // from class: org.apache.commons.math.linear.AbstractRealVector.1
            private org.apache.commons.math.linear.AbstractRealVector.EntryImpl e;
            private int i = 0;

            {
                this.e = org.apache.commons.math.linear.AbstractRealVector.this.new EntryImpl();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.i < dim;
            }

            @Override // java.util.Iterator
            public org.apache.commons.math.linear.RealVector.Entry next() {
                org.apache.commons.math.linear.AbstractRealVector.EntryImpl entryImpl = this.e;
                int i = this.i;
                this.i = i + 1;
                entryImpl.setIndex(i);
                return this.e;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new org.apache.commons.math.exception.MathUnsupportedOperationException(new java.lang.Object[0]);
            }
        };
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector map(org.apache.commons.math.analysis.UnivariateRealFunction function) throws org.apache.commons.math.FunctionEvaluationException {
        return copy().mapToSelf(function);
    }

    @Override // org.apache.commons.math.linear.RealVector
    public org.apache.commons.math.linear.RealVector mapToSelf(org.apache.commons.math.analysis.UnivariateRealFunction function) throws org.apache.commons.math.FunctionEvaluationException {
        org.apache.commons.math.linear.RealVector.Entry e;
        java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> it = function.value(0.0d) == 0.0d ? sparseIterator() : iterator();
        while (it.hasNext() && (e = it.next()) != null) {
            e.setValue(function.value(e.getValue()));
        }
        return this;
    }

    protected class EntryImpl extends org.apache.commons.math.linear.RealVector.Entry {
        public EntryImpl() {
            setIndex(0);
        }

        @Override // org.apache.commons.math.linear.RealVector.Entry
        public double getValue() {
            return org.apache.commons.math.linear.AbstractRealVector.this.getEntry(getIndex());
        }

        @Override // org.apache.commons.math.linear.RealVector.Entry
        public void setValue(double newValue) {
            org.apache.commons.math.linear.AbstractRealVector.this.setEntry(getIndex(), newValue);
        }
    }

    protected class SparseEntryIterator implements java.util.Iterator<org.apache.commons.math.linear.RealVector.Entry> {
        private org.apache.commons.math.linear.AbstractRealVector.EntryImpl current;
        private final int dim;
        private org.apache.commons.math.linear.AbstractRealVector.EntryImpl next;

        protected SparseEntryIterator() {
            this.dim = org.apache.commons.math.linear.AbstractRealVector.this.getDimension();
            this.current = org.apache.commons.math.linear.AbstractRealVector.this.new EntryImpl();
            this.next = org.apache.commons.math.linear.AbstractRealVector.this.new EntryImpl();
            if (this.next.getValue() == 0.0d) {
                advance(this.next);
            }
        }

        protected void advance(org.apache.commons.math.linear.AbstractRealVector.EntryImpl e) {
            if (e == null) {
                return;
            }
            do {
                e.setIndex(e.getIndex() + 1);
                if (e.getIndex() >= this.dim) {
                    break;
                }
            } while (e.getValue() == 0.0d);
            if (e.getIndex() >= this.dim) {
                e.setIndex(-1);
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next.getIndex() >= 0;
        }

        @Override // java.util.Iterator
        public org.apache.commons.math.linear.RealVector.Entry next() {
            int index = this.next.getIndex();
            if (index < 0) {
                throw new java.util.NoSuchElementException();
            }
            this.current.setIndex(index);
            advance(this.next);
            return this.current;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new org.apache.commons.math.exception.MathUnsupportedOperationException(new java.lang.Object[0]);
        }
    }
}
