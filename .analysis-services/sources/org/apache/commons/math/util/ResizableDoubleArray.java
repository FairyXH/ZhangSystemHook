package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public class ResizableDoubleArray implements org.apache.commons.math.util.DoubleArray, java.io.Serializable {
    public static final int ADDITIVE_MODE = 1;
    public static final int MULTIPLICATIVE_MODE = 0;
    private static final long serialVersionUID = -3485529955529426875L;
    protected float contractionCriteria;
    protected float expansionFactor;
    protected int expansionMode;
    protected int initialCapacity;
    protected double[] internalArray;
    protected int numElements;
    protected int startIndex;

    public ResizableDoubleArray() {
        this.contractionCriteria = 2.5f;
        this.expansionFactor = 2.0f;
        this.expansionMode = 0;
        this.initialCapacity = 16;
        this.numElements = 0;
        this.startIndex = 0;
        this.internalArray = new double[this.initialCapacity];
    }

    public ResizableDoubleArray(int initialCapacity) {
        this.contractionCriteria = 2.5f;
        this.expansionFactor = 2.0f;
        this.expansionMode = 0;
        this.initialCapacity = 16;
        this.numElements = 0;
        this.startIndex = 0;
        setInitialCapacity(initialCapacity);
        this.internalArray = new double[this.initialCapacity];
    }

    public ResizableDoubleArray(double[] initialArray) {
        this.contractionCriteria = 2.5f;
        this.expansionFactor = 2.0f;
        this.expansionMode = 0;
        this.initialCapacity = 16;
        this.numElements = 0;
        this.startIndex = 0;
        if (initialArray == null) {
            this.internalArray = new double[this.initialCapacity];
            return;
        }
        this.internalArray = new double[initialArray.length];
        java.lang.System.arraycopy(initialArray, 0, this.internalArray, 0, initialArray.length);
        this.initialCapacity = initialArray.length;
        this.numElements = initialArray.length;
    }

    public ResizableDoubleArray(int initialCapacity, float expansionFactor) {
        this.contractionCriteria = 2.5f;
        this.expansionFactor = 2.0f;
        this.expansionMode = 0;
        this.initialCapacity = 16;
        this.numElements = 0;
        this.startIndex = 0;
        this.expansionFactor = expansionFactor;
        setInitialCapacity(initialCapacity);
        this.internalArray = new double[initialCapacity];
        setContractionCriteria(0.5f + expansionFactor);
    }

    public ResizableDoubleArray(int initialCapacity, float expansionFactor, float contractionCriteria) {
        this.contractionCriteria = 2.5f;
        this.expansionFactor = 2.0f;
        this.expansionMode = 0;
        this.initialCapacity = 16;
        this.numElements = 0;
        this.startIndex = 0;
        this.expansionFactor = expansionFactor;
        setContractionCriteria(contractionCriteria);
        setInitialCapacity(initialCapacity);
        this.internalArray = new double[initialCapacity];
    }

    public ResizableDoubleArray(int initialCapacity, float expansionFactor, float contractionCriteria, int expansionMode) {
        this.contractionCriteria = 2.5f;
        this.expansionFactor = 2.0f;
        this.expansionMode = 0;
        this.initialCapacity = 16;
        this.numElements = 0;
        this.startIndex = 0;
        this.expansionFactor = expansionFactor;
        setContractionCriteria(contractionCriteria);
        setInitialCapacity(initialCapacity);
        setExpansionMode(expansionMode);
        this.internalArray = new double[initialCapacity];
    }

    public ResizableDoubleArray(org.apache.commons.math.util.ResizableDoubleArray original) {
        this.contractionCriteria = 2.5f;
        this.expansionFactor = 2.0f;
        this.expansionMode = 0;
        this.initialCapacity = 16;
        this.numElements = 0;
        this.startIndex = 0;
        copy(original, this);
    }

    @Override // org.apache.commons.math.util.DoubleArray
    public synchronized void addElement(double value) {
        this.numElements++;
        if (this.startIndex + this.numElements > this.internalArray.length) {
            expand();
        }
        this.internalArray[this.startIndex + (this.numElements - 1)] = value;
        if (shouldContract()) {
            contract();
        }
    }

    public synchronized void addElements(double[] values) {
        double[] tempArray = new double[this.numElements + values.length + 1];
        java.lang.System.arraycopy(this.internalArray, this.startIndex, tempArray, 0, this.numElements);
        java.lang.System.arraycopy(values, 0, tempArray, this.numElements, values.length);
        this.internalArray = tempArray;
        this.startIndex = 0;
        this.numElements += values.length;
    }

    @Override // org.apache.commons.math.util.DoubleArray
    public synchronized double addElementRolling(double value) {
        double discarded;
        discarded = this.internalArray[this.startIndex];
        if (this.startIndex + this.numElements + 1 > this.internalArray.length) {
            expand();
        }
        this.startIndex++;
        this.internalArray[this.startIndex + (this.numElements - 1)] = value;
        if (shouldContract()) {
            contract();
        }
        return discarded;
    }

    public synchronized double substituteMostRecentElement(double value) {
        double discarded;
        if (this.numElements < 1) {
            throw org.apache.commons.math.MathRuntimeException.createArrayIndexOutOfBoundsException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_SUBSTITUTE_ELEMENT_FROM_EMPTY_ARRAY, new java.lang.Object[0]);
        }
        discarded = this.internalArray[this.startIndex + (this.numElements - 1)];
        this.internalArray[this.startIndex + (this.numElements - 1)] = value;
        return discarded;
    }

    protected void checkContractExpand(float contraction, float expansion) {
        if (contraction < expansion) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CONTRACTION_CRITERIA_SMALLER_THAN_EXPANSION_FACTOR, java.lang.Float.valueOf(contraction), java.lang.Float.valueOf(expansion));
        }
        if (contraction <= 1.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CONTRACTION_CRITERIA_SMALLER_THAN_ONE, java.lang.Float.valueOf(contraction));
        }
        if (expansion <= 1.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.EXPANSION_FACTOR_SMALLER_THAN_ONE, java.lang.Float.valueOf(expansion));
        }
    }

    @Override // org.apache.commons.math.util.DoubleArray
    public synchronized void clear() {
        this.numElements = 0;
        this.startIndex = 0;
        this.internalArray = new double[this.initialCapacity];
    }

    public synchronized void contract() {
        double[] tempArray = new double[this.numElements + 1];
        java.lang.System.arraycopy(this.internalArray, this.startIndex, tempArray, 0, this.numElements);
        this.internalArray = tempArray;
        this.startIndex = 0;
    }

    public synchronized void discardFrontElements(int i) {
        discardExtremeElements(i, true);
    }

    public synchronized void discardMostRecentElements(int i) {
        discardExtremeElements(i, false);
    }

    private synchronized void discardExtremeElements(int i, boolean front) {
        if (i > this.numElements) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.TOO_MANY_ELEMENTS_TO_DISCARD_FROM_ARRAY, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(this.numElements));
        }
        if (i < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_DISCARD_NEGATIVE_NUMBER_OF_ELEMENTS, java.lang.Integer.valueOf(i));
        }
        this.numElements -= i;
        if (front) {
            this.startIndex += i;
        }
        if (shouldContract()) {
            contract();
        }
    }

    protected synchronized void expand() {
        int newSize;
        if (this.expansionMode == 0) {
            newSize = (int) org.apache.commons.math.util.FastMath.ceil(this.internalArray.length * this.expansionFactor);
        } else {
            newSize = this.internalArray.length + org.apache.commons.math.util.FastMath.round(this.expansionFactor);
        }
        double[] tempArray = new double[newSize];
        java.lang.System.arraycopy(this.internalArray, 0, tempArray, 0, this.internalArray.length);
        this.internalArray = tempArray;
    }

    private synchronized void expandTo(int size) {
        double[] tempArray = new double[size];
        java.lang.System.arraycopy(this.internalArray, 0, tempArray, 0, this.internalArray.length);
        this.internalArray = tempArray;
    }

    public float getContractionCriteria() {
        return this.contractionCriteria;
    }

    @Override // org.apache.commons.math.util.DoubleArray
    public synchronized double getElement(int index) {
        if (index >= this.numElements) {
            throw org.apache.commons.math.MathRuntimeException.createArrayIndexOutOfBoundsException(org.apache.commons.math.exception.util.LocalizedFormats.INDEX_LARGER_THAN_MAX, java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(this.numElements - 1));
        }
        if (index >= 0) {
        } else {
            throw org.apache.commons.math.MathRuntimeException.createArrayIndexOutOfBoundsException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_RETRIEVE_AT_NEGATIVE_INDEX, java.lang.Integer.valueOf(index));
        }
        return this.internalArray[this.startIndex + index];
    }

    @Override // org.apache.commons.math.util.DoubleArray
    public synchronized double[] getElements() {
        double[] elementArray;
        elementArray = new double[this.numElements];
        java.lang.System.arraycopy(this.internalArray, this.startIndex, elementArray, 0, this.numElements);
        return elementArray;
    }

    public float getExpansionFactor() {
        return this.expansionFactor;
    }

    public int getExpansionMode() {
        return this.expansionMode;
    }

    synchronized int getInternalLength() {
        return this.internalArray.length;
    }

    @Override // org.apache.commons.math.util.DoubleArray
    public synchronized int getNumElements() {
        return this.numElements;
    }

    @java.lang.Deprecated
    public synchronized double[] getValues() {
        return this.internalArray;
    }

    public synchronized double[] getInternalValues() {
        return this.internalArray;
    }

    public void setContractionCriteria(float contractionCriteria) {
        checkContractExpand(contractionCriteria, getExpansionFactor());
        synchronized (this) {
            this.contractionCriteria = contractionCriteria;
        }
    }

    @Override // org.apache.commons.math.util.DoubleArray
    public synchronized void setElement(int index, double value) {
        if (index < 0) {
            throw org.apache.commons.math.MathRuntimeException.createArrayIndexOutOfBoundsException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_SET_AT_NEGATIVE_INDEX, java.lang.Integer.valueOf(index));
        }
        if (index + 1 > this.numElements) {
            this.numElements = index + 1;
        }
        if (this.startIndex + index >= this.internalArray.length) {
            expandTo(this.startIndex + index + 1);
        }
        this.internalArray[this.startIndex + index] = value;
    }

    public void setExpansionFactor(float expansionFactor) {
        checkContractExpand(getContractionCriteria(), expansionFactor);
        synchronized (this) {
            this.expansionFactor = expansionFactor;
        }
    }

    public void setExpansionMode(int expansionMode) {
        if (expansionMode != 0 && expansionMode != 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.UNSUPPORTED_EXPANSION_MODE, java.lang.Integer.valueOf(expansionMode), 0, "MULTIPLICATIVE_MODE", 1, "ADDITIVE_MODE");
        }
        synchronized (this) {
            this.expansionMode = expansionMode;
        }
    }

    protected void setInitialCapacity(int initialCapacity) {
        if (initialCapacity > 0) {
            synchronized (this) {
                this.initialCapacity = initialCapacity;
            }
            return;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INITIAL_CAPACITY_NOT_POSITIVE, java.lang.Integer.valueOf(initialCapacity));
    }

    public synchronized void setNumElements(int i) {
        if (i < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INDEX_NOT_POSITIVE, java.lang.Integer.valueOf(i));
        }
        if (this.startIndex + i > this.internalArray.length) {
            expandTo(this.startIndex + i);
        }
        this.numElements = i;
    }

    private synchronized boolean shouldContract() {
        if (this.expansionMode == 0) {
            return ((float) this.internalArray.length) / ((float) this.numElements) > this.contractionCriteria;
        }
        return ((float) (this.internalArray.length - this.numElements)) > this.contractionCriteria;
    }

    public synchronized int start() {
        return this.startIndex;
    }

    public static void copy(org.apache.commons.math.util.ResizableDoubleArray source, org.apache.commons.math.util.ResizableDoubleArray dest) {
        synchronized (source) {
            synchronized (dest) {
                dest.initialCapacity = source.initialCapacity;
                dest.contractionCriteria = source.contractionCriteria;
                dest.expansionFactor = source.expansionFactor;
                dest.expansionMode = source.expansionMode;
                dest.internalArray = new double[source.internalArray.length];
                java.lang.System.arraycopy(source.internalArray, 0, dest.internalArray, 0, dest.internalArray.length);
                dest.numElements = source.numElements;
                dest.startIndex = source.startIndex;
            }
        }
    }

    public synchronized org.apache.commons.math.util.ResizableDoubleArray copy() {
        org.apache.commons.math.util.ResizableDoubleArray result;
        result = new org.apache.commons.math.util.ResizableDoubleArray();
        copy(this, result);
        return result;
    }

    public boolean equals(java.lang.Object object) {
        boolean result = true;
        if (object == this) {
            return true;
        }
        if (!(object instanceof org.apache.commons.math.util.ResizableDoubleArray)) {
            return false;
        }
        synchronized (this) {
            synchronized (object) {
                org.apache.commons.math.util.ResizableDoubleArray other = (org.apache.commons.math.util.ResizableDoubleArray) object;
                boolean result2 = 1 != 0 && other.initialCapacity == this.initialCapacity;
                boolean result3 = result2 && other.contractionCriteria == this.contractionCriteria;
                boolean result4 = result3 && other.expansionFactor == this.expansionFactor;
                boolean result5 = result4 && other.expansionMode == this.expansionMode;
                boolean result6 = result5 && other.numElements == this.numElements;
                if (!result6 || other.startIndex != this.startIndex) {
                    result = false;
                }
                if (result) {
                    return java.util.Arrays.equals(this.internalArray, other.internalArray);
                }
                return false;
            }
        }
    }

    public synchronized int hashCode() {
        int[] hashData;
        hashData = new int[]{new java.lang.Float(this.expansionFactor).hashCode(), new java.lang.Float(this.contractionCriteria).hashCode(), this.expansionMode, java.util.Arrays.hashCode(this.internalArray), this.initialCapacity, this.numElements, this.startIndex};
        return java.util.Arrays.hashCode(hashData);
    }
}
