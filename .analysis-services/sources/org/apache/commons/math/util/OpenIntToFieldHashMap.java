package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public class OpenIntToFieldHashMap<T extends org.apache.commons.math.FieldElement<T>> implements java.io.Serializable {
    private static final int DEFAULT_EXPECTED_SIZE = 16;
    protected static final byte FREE = 0;
    protected static final byte FULL = 1;
    private static final float LOAD_FACTOR = 0.5f;
    private static final int PERTURB_SHIFT = 5;
    protected static final byte REMOVED = 2;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final long serialVersionUID = -9179080286849120720L;
    private transient int count;
    private final org.apache.commons.math.Field<T> field;
    private int[] keys;
    private int mask;
    private final T missingEntries;
    private int size;
    private byte[] states;
    private T[] values;

    public OpenIntToFieldHashMap(org.apache.commons.math.Field<T> field) {
        this(field, 16, field.getZero());
    }

    public OpenIntToFieldHashMap(org.apache.commons.math.Field<T> field, T missingEntries) {
        this(field, 16, missingEntries);
    }

    public OpenIntToFieldHashMap(org.apache.commons.math.Field<T> field, int expectedSize) {
        this(field, expectedSize, field.getZero());
    }

    public OpenIntToFieldHashMap(org.apache.commons.math.Field<T> field, int i, T t) {
        this.field = field;
        int iComputeCapacity = computeCapacity(i);
        this.keys = new int[iComputeCapacity];
        this.values = (T[]) buildArray(iComputeCapacity);
        this.states = new byte[iComputeCapacity];
        this.missingEntries = t;
        this.mask = iComputeCapacity - 1;
    }

    public OpenIntToFieldHashMap(org.apache.commons.math.util.OpenIntToFieldHashMap<T> openIntToFieldHashMap) {
        this.field = openIntToFieldHashMap.field;
        int length = openIntToFieldHashMap.keys.length;
        this.keys = new int[length];
        java.lang.System.arraycopy(openIntToFieldHashMap.keys, 0, this.keys, 0, length);
        this.values = (T[]) buildArray(length);
        java.lang.System.arraycopy(openIntToFieldHashMap.values, 0, this.values, 0, length);
        this.states = new byte[length];
        java.lang.System.arraycopy(openIntToFieldHashMap.states, 0, this.states, 0, length);
        this.missingEntries = openIntToFieldHashMap.missingEntries;
        this.size = openIntToFieldHashMap.size;
        this.mask = openIntToFieldHashMap.mask;
        this.count = openIntToFieldHashMap.count;
    }

    private static int computeCapacity(int expectedSize) {
        if (expectedSize == 0) {
            return 1;
        }
        int capacity = (int) org.apache.commons.math.util.FastMath.ceil(expectedSize / 0.5f);
        int powerOfTwo = java.lang.Integer.highestOneBit(capacity);
        if (powerOfTwo == capacity) {
            return capacity;
        }
        return nextPowerOfTwo(capacity);
    }

    private static int nextPowerOfTwo(int i) {
        return java.lang.Integer.highestOneBit(i) << 1;
    }

    public T get(int key) {
        int hash = hashOf(key);
        int index = this.mask & hash;
        if (containsKey(key, index)) {
            return this.values[index];
        }
        if (this.states[index] == 0) {
            return this.missingEntries;
        }
        int j = index;
        int perturb = perturb(hash);
        while (this.states[index] != 0) {
            j = probe(perturb, j);
            index = j & this.mask;
            if (!containsKey(key, index)) {
                perturb >>= 5;
            } else {
                return this.values[index];
            }
        }
        return this.missingEntries;
    }

    public boolean containsKey(int key) {
        int hash = hashOf(key);
        int index = this.mask & hash;
        if (containsKey(key, index)) {
            return true;
        }
        if (this.states[index] == 0) {
            return false;
        }
        int j = index;
        int perturb = perturb(hash);
        while (this.states[index] != 0) {
            j = probe(perturb, j);
            index = j & this.mask;
            if (containsKey(key, index)) {
                return true;
            }
            perturb >>= 5;
        }
        return false;
    }

    public org.apache.commons.math.util.OpenIntToFieldHashMap<T>.Iterator iterator() {
        return new org.apache.commons.math.util.OpenIntToFieldHashMap.Iterator();
    }

    private static int perturb(int hash) {
        return Integer.MAX_VALUE & hash;
    }

    private int findInsertionIndex(int key) {
        return findInsertionIndex(this.keys, this.states, key, this.mask);
    }

    private static int findInsertionIndex(int[] keys, byte[] states, int key, int mask) {
        int hash = hashOf(key);
        int index = hash & mask;
        if (states[index] == 0) {
            return index;
        }
        if (states[index] == 1 && keys[index] == key) {
            return changeIndexSign(index);
        }
        int perturb = perturb(hash);
        int j = index;
        if (states[index] == 1) {
            do {
                j = probe(perturb, j);
                index = j & mask;
                perturb >>= 5;
                if (states[index] != 1) {
                    break;
                }
            } while (keys[index] != key);
        }
        if (states[index] == 0) {
            return index;
        }
        if (states[index] == 1) {
            return changeIndexSign(index);
        }
        int firstRemoved = index;
        while (true) {
            j = probe(perturb, j);
            int index2 = j & mask;
            if (states[index2] == 0) {
                return firstRemoved;
            }
            if (states[index2] == 1 && keys[index2] == key) {
                return changeIndexSign(index2);
            }
            perturb >>= 5;
        }
    }

    private static int probe(int perturb, int j) {
        return (j << 2) + j + perturb + 1;
    }

    private static int changeIndexSign(int index) {
        return (-index) - 1;
    }

    public int size() {
        return this.size;
    }

    public T remove(int i) {
        int iHashOf = hashOf(i);
        int i2 = this.mask & iHashOf;
        if (containsKey(i, i2)) {
            return (T) doRemove(i2);
        }
        if (this.states[i2] == 0) {
            return this.missingEntries;
        }
        int iProbe = i2;
        int iPerturb = perturb(iHashOf);
        while (this.states[i2] != 0) {
            iProbe = probe(iPerturb, iProbe);
            i2 = iProbe & this.mask;
            if (!containsKey(i, i2)) {
                iPerturb >>= 5;
            } else {
                return (T) doRemove(i2);
            }
        }
        return this.missingEntries;
    }

    private boolean containsKey(int key, int index) {
        return (key != 0 || this.states[index] == 1) && this.keys[index] == key;
    }

    private T doRemove(int index) {
        this.keys[index] = 0;
        this.states[index] = 2;
        T previous = this.values[index];
        this.values[index] = this.missingEntries;
        this.size--;
        this.count++;
        return previous;
    }

    public T put(int key, T value) {
        int index = findInsertionIndex(key);
        T previous = this.missingEntries;
        boolean newMapping = true;
        if (index < 0) {
            index = changeIndexSign(index);
            previous = this.values[index];
            newMapping = false;
        }
        this.keys[index] = key;
        this.states[index] = 1;
        this.values[index] = value;
        if (newMapping) {
            this.size++;
            if (shouldGrowTable()) {
                growTable();
            }
            this.count++;
        }
        return previous;
    }

    private void growTable() {
        int length = this.states.length;
        int[] iArr = this.keys;
        T[] tArr = this.values;
        byte[] bArr = this.states;
        int i = length * 2;
        int[] iArr2 = new int[i];
        T[] tArr2 = (T[]) buildArray(i);
        byte[] bArr2 = new byte[i];
        int i2 = i - 1;
        for (int i3 = 0; i3 < length; i3++) {
            if (bArr[i3] == 1) {
                int i4 = iArr[i3];
                int iFindInsertionIndex = findInsertionIndex(iArr2, bArr2, i4, i2);
                iArr2[iFindInsertionIndex] = i4;
                tArr2[iFindInsertionIndex] = tArr[i3];
                bArr2[iFindInsertionIndex] = 1;
            }
        }
        this.mask = i2;
        this.keys = iArr2;
        this.values = tArr2;
        this.states = bArr2;
    }

    private boolean shouldGrowTable() {
        return ((float) this.size) > ((float) (this.mask + 1)) * 0.5f;
    }

    private static int hashOf(int key) {
        int h = ((key >>> 20) ^ (key >>> 12)) ^ key;
        return ((h >>> 7) ^ h) ^ (h >>> 4);
    }

    public class Iterator {
        private int current;
        private int next;
        private final int referenceCount;

        private Iterator() {
            this.referenceCount = org.apache.commons.math.util.OpenIntToFieldHashMap.this.count;
            this.next = -1;
            try {
                advance();
            } catch (java.util.NoSuchElementException e) {
            }
        }

        public boolean hasNext() {
            return this.next >= 0;
        }

        public int key() throws java.util.NoSuchElementException, java.util.ConcurrentModificationException {
            if (this.referenceCount != org.apache.commons.math.util.OpenIntToFieldHashMap.this.count) {
                throw org.apache.commons.math.MathRuntimeException.createConcurrentModificationException(org.apache.commons.math.exception.util.LocalizedFormats.MAP_MODIFIED_WHILE_ITERATING, new java.lang.Object[0]);
            }
            if (this.current >= 0) {
                return org.apache.commons.math.util.OpenIntToFieldHashMap.this.keys[this.current];
            }
            throw org.apache.commons.math.MathRuntimeException.createNoSuchElementException(org.apache.commons.math.exception.util.LocalizedFormats.ITERATOR_EXHAUSTED, new java.lang.Object[0]);
        }

        public T value() throws java.util.NoSuchElementException, java.util.ConcurrentModificationException {
            if (this.referenceCount != org.apache.commons.math.util.OpenIntToFieldHashMap.this.count) {
                throw org.apache.commons.math.MathRuntimeException.createConcurrentModificationException(org.apache.commons.math.exception.util.LocalizedFormats.MAP_MODIFIED_WHILE_ITERATING, new java.lang.Object[0]);
            }
            if (this.current >= 0) {
                return (T) org.apache.commons.math.util.OpenIntToFieldHashMap.this.values[this.current];
            }
            throw org.apache.commons.math.MathRuntimeException.createNoSuchElementException(org.apache.commons.math.exception.util.LocalizedFormats.ITERATOR_EXHAUSTED, new java.lang.Object[0]);
        }

        public void advance() throws java.util.NoSuchElementException, java.util.ConcurrentModificationException {
            byte[] bArr;
            int i;
            if (this.referenceCount != org.apache.commons.math.util.OpenIntToFieldHashMap.this.count) {
                throw org.apache.commons.math.MathRuntimeException.createConcurrentModificationException(org.apache.commons.math.exception.util.LocalizedFormats.MAP_MODIFIED_WHILE_ITERATING, new java.lang.Object[0]);
            }
            this.current = this.next;
            do {
                try {
                    bArr = org.apache.commons.math.util.OpenIntToFieldHashMap.this.states;
                    i = this.next + 1;
                    this.next = i;
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    this.next = -2;
                    if (this.current < 0) {
                        throw org.apache.commons.math.MathRuntimeException.createNoSuchElementException(org.apache.commons.math.exception.util.LocalizedFormats.ITERATOR_EXHAUSTED, new java.lang.Object[0]);
                    }
                    return;
                }
            } while (bArr[i] != 1);
        }
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.lang.ClassNotFoundException, java.io.IOException {
        stream.defaultReadObject();
        this.count = 0;
    }

    private T[] buildArray(int i) {
        return (T[]) ((org.apache.commons.math.FieldElement[]) java.lang.reflect.Array.newInstance(this.field.getZero().getClass(), i));
    }
}
