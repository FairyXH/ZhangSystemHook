package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchedSparseBooleanMatrix extends com.android.server.utils.WatchableImpl implements com.android.server.utils.Snappable {
    private static final int PACKING = 32;
    static final int STEP = 64;
    static final int STRING_INUSE_INDEX = 2;
    static final int STRING_KEY_INDEX = 0;
    static final int STRING_MAP_INDEX = 1;
    private boolean[] mInUse;
    private int[] mKeys;
    private int[] mMap;
    private int mOrder;
    private int mSize;
    private int[] mValues;

    private void onChanged() {
        dispatchChange(this);
    }

    public WatchedSparseBooleanMatrix() {
        this(64);
    }

    public WatchedSparseBooleanMatrix(int initialCapacity) {
        this.mOrder = initialCapacity;
        if (this.mOrder < 64) {
            this.mOrder = 64;
        }
        if (this.mOrder % 64 != 0) {
            this.mOrder = ((initialCapacity / 64) + 1) * 64;
        }
        if (this.mOrder < 64 || this.mOrder % 64 != 0) {
            throw new java.lang.RuntimeException("mOrder is " + this.mOrder + " initCap is " + initialCapacity);
        }
        this.mInUse = com.android.internal.util.ArrayUtils.newUnpaddedBooleanArray(this.mOrder);
        this.mKeys = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(this.mOrder);
        this.mMap = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(this.mOrder);
        this.mValues = com.android.internal.util.ArrayUtils.newUnpaddedIntArray((this.mOrder * this.mOrder) / 32);
        this.mSize = 0;
    }

    private WatchedSparseBooleanMatrix(com.android.server.utils.WatchedSparseBooleanMatrix r) {
        copyFrom(r);
    }

    public void copyFrom(com.android.server.utils.WatchedSparseBooleanMatrix src) {
        this.mOrder = src.mOrder;
        this.mSize = src.mSize;
        this.mKeys = (int[]) src.mKeys.clone();
        this.mMap = (int[]) src.mMap.clone();
        this.mInUse = (boolean[]) src.mInUse.clone();
        this.mValues = (int[]) src.mValues.clone();
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.utils.WatchedSparseBooleanMatrix snapshot() {
        return new com.android.server.utils.WatchedSparseBooleanMatrix(this);
    }

    public boolean get(int row, int col) {
        return get(row, col, false);
    }

    public boolean get(int row, int col, boolean valueIfKeyNotFound) {
        int r = indexOfKey(row, false);
        int c = indexOfKey(col, false);
        if (r >= 0 && c >= 0) {
            return valueAt(r, c);
        }
        return valueIfKeyNotFound;
    }

    public void put(int row, int col, boolean value) {
        int r = indexOfKey(row);
        int c = indexOfKey(col);
        if (r < 0 || c < 0) {
            if (r < 0) {
                indexOfKey(row, true);
            }
            if (c < 0) {
                indexOfKey(col, true);
            }
            r = indexOfKey(row);
            c = indexOfKey(col);
        }
        if (r >= 0 && c >= 0) {
            setValueAt(r, c, value);
            return;
        }
        throw new java.lang.RuntimeException("matrix overflow");
    }

    public void deleteKey(int key) {
        int i = indexOfKey(key, false);
        if (i >= 0) {
            removeAt(i);
        }
    }

    public void removeAt(int index) {
        validateIndex(index);
        this.mInUse[this.mMap[index]] = false;
        java.lang.System.arraycopy(this.mKeys, index + 1, this.mKeys, index, this.mSize - (index + 1));
        this.mKeys[this.mSize - 1] = 0;
        java.lang.System.arraycopy(this.mMap, index + 1, this.mMap, index, this.mSize - (index + 1));
        this.mMap[this.mSize - 1] = 0;
        this.mSize--;
        onChanged();
    }

    public void removeRange(int fromIndex, int toIndex) {
        if (toIndex < fromIndex) {
            throw new java.lang.ArrayIndexOutOfBoundsException("toIndex < fromIndex");
        }
        int num = toIndex - fromIndex;
        if (num == 0) {
            return;
        }
        validateIndex(fromIndex);
        validateIndex(toIndex - 1);
        for (int i = fromIndex; i < toIndex; i++) {
            this.mInUse[this.mMap[i]] = false;
        }
        java.lang.System.arraycopy(this.mKeys, toIndex, this.mKeys, fromIndex, this.mSize - toIndex);
        java.lang.System.arraycopy(this.mMap, toIndex, this.mMap, fromIndex, this.mSize - toIndex);
        for (int i2 = this.mSize - num; i2 < this.mSize; i2++) {
            this.mKeys[i2] = 0;
            this.mMap[i2] = 0;
        }
        int i3 = this.mSize;
        this.mSize = i3 - num;
        onChanged();
    }

    public int size() {
        return this.mSize;
    }

    public void clear() {
        this.mSize = 0;
        java.util.Arrays.fill(this.mInUse, false);
        onChanged();
    }

    public int keyAt(int index) {
        validateIndex(index);
        return this.mKeys[index];
    }

    private boolean valueAtInternal(int row, int col) {
        int element = (this.mOrder * row) + col;
        int offset = element / 32;
        int mask = 1 << (element % 32);
        return (this.mValues[offset] & mask) != 0;
    }

    public boolean valueAt(int rowIndex, int colIndex) {
        validateIndex(rowIndex, colIndex);
        int r = this.mMap[rowIndex];
        int c = this.mMap[colIndex];
        return valueAtInternal(r, c);
    }

    private void setValueAtInternal(int row, int col, boolean value) {
        int element = (this.mOrder * row) + col;
        int offset = element / 32;
        int mask = 1 << (element % 32);
        if (value) {
            int[] iArr = this.mValues;
            iArr[offset] = iArr[offset] | mask;
        } else {
            int[] iArr2 = this.mValues;
            iArr2[offset] = iArr2[offset] & (~mask);
        }
    }

    public void setValueAt(int rowIndex, int colIndex, boolean value) {
        validateIndex(rowIndex, colIndex);
        int r = this.mMap[rowIndex];
        int c = this.mMap[colIndex];
        setValueAtInternal(r, c, value);
        onChanged();
    }

    public int indexOfKey(int key) {
        return binarySearch(this.mKeys, this.mSize, key);
    }

    public boolean contains(int key) {
        return indexOfKey(key) >= 0;
    }

    private int indexOfKey(int key, boolean grow) {
        int i = binarySearch(this.mKeys, this.mSize, key);
        if (i < 0 && grow) {
            i = ~i;
            if (this.mSize >= this.mOrder) {
                growMatrix();
            }
            int newIndex = nextFree(true);
            this.mKeys = com.android.internal.util.GrowingArrayUtils.insert(this.mKeys, this.mSize, i, key);
            this.mMap = com.android.internal.util.GrowingArrayUtils.insert(this.mMap, this.mSize, i, newIndex);
            this.mSize++;
            int valueRow = this.mOrder / 32;
            int offset = newIndex / 32;
            int mask = ~(1 << (newIndex % 32));
            java.util.Arrays.fill(this.mValues, newIndex * valueRow, (newIndex + 1) * valueRow, 0);
            for (int n = 0; n < this.mSize; n++) {
                int[] iArr = this.mValues;
                int i2 = (n * valueRow) + offset;
                iArr[i2] = iArr[i2] & mask;
            }
        }
        return i;
    }

    private void validateIndex(int index) {
        if (index >= this.mSize) {
            throw new java.lang.ArrayIndexOutOfBoundsException(index);
        }
    }

    private void validateIndex(int row, int col) {
        validateIndex(row);
        validateIndex(col);
    }

    private void growMatrix() {
        resizeMatrix(this.mOrder + 64);
    }

    private void resizeMatrix(int newOrder) {
        if (newOrder % 64 != 0) {
            throw new java.lang.IllegalArgumentException("matrix order " + newOrder + " is not a multiple of 64");
        }
        int minOrder = java.lang.Math.min(this.mOrder, newOrder);
        boolean[] newInUse = com.android.internal.util.ArrayUtils.newUnpaddedBooleanArray(newOrder);
        java.lang.System.arraycopy(this.mInUse, 0, newInUse, 0, minOrder);
        int[] newMap = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(newOrder);
        java.lang.System.arraycopy(this.mMap, 0, newMap, 0, minOrder);
        int[] newKeys = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(newOrder);
        java.lang.System.arraycopy(this.mKeys, 0, newKeys, 0, minOrder);
        int[] newValues = com.android.internal.util.ArrayUtils.newUnpaddedIntArray((newOrder * newOrder) / 32);
        for (int i = 0; i < minOrder; i++) {
            int row = (this.mOrder * i) / 32;
            int newRow = (newOrder * i) / 32;
            java.lang.System.arraycopy(this.mValues, row, newValues, newRow, minOrder / 32);
        }
        this.mInUse = newInUse;
        this.mMap = newMap;
        this.mKeys = newKeys;
        this.mValues = newValues;
        this.mOrder = newOrder;
    }

    private int nextFree(boolean acquire) {
        for (int i = 0; i < this.mInUse.length; i++) {
            if (!this.mInUse[i]) {
                this.mInUse[i] = acquire;
                return i;
            }
        }
        throw new java.lang.RuntimeException();
    }

    private int lastInuse() {
        for (int i = this.mOrder - 1; i >= 0; i--) {
            if (this.mInUse[i]) {
                for (int j = 0; j < this.mSize; j++) {
                    if (this.mMap[j] == i) {
                        return j;
                    }
                }
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        return -1;
    }

    private void pack() {
        if (this.mSize == 0 || this.mSize == this.mOrder) {
            return;
        }
        int dst = nextFree(false);
        while (dst < this.mSize) {
            this.mInUse[dst] = true;
            int srcIndex = lastInuse();
            int src = this.mMap[srcIndex];
            this.mInUse[src] = false;
            this.mMap[srcIndex] = dst;
            java.lang.System.arraycopy(this.mValues, (this.mOrder * src) / 32, this.mValues, (this.mOrder * dst) / 32, this.mOrder / 32);
            int srcOffset = src / 32;
            int srcMask = 1 << (src % 32);
            int dstOffset = dst / 32;
            int dstMask = 1 << (dst % 32);
            for (int i = 0; i < this.mOrder; i++) {
                if ((this.mValues[srcOffset] & srcMask) == 0) {
                    int[] iArr = this.mValues;
                    iArr[dstOffset] = iArr[dstOffset] & (~dstMask);
                } else {
                    int[] iArr2 = this.mValues;
                    iArr2[dstOffset] = iArr2[dstOffset] | dstMask;
                }
                srcOffset += this.mOrder / 32;
                dstOffset += this.mOrder / 32;
            }
            dst = nextFree(false);
        }
    }

    public void compact() {
        pack();
        int unused = (this.mOrder - this.mSize) / 64;
        if (unused > 0) {
            resizeMatrix(this.mOrder - (unused * 64));
        }
    }

    public int[] keys() {
        return java.util.Arrays.copyOf(this.mKeys, this.mSize);
    }

    public int capacity() {
        return this.mOrder;
    }

    public void setCapacity(int capacity) {
        if (capacity <= this.mOrder) {
            return;
        }
        if (capacity % 64 != 0) {
            capacity = ((capacity / 64) + 1) * 64;
        }
        resizeMatrix(capacity);
    }

    public int hashCode() {
        int iHashCode = (((this.mSize * 31) + java.util.Arrays.hashCode(this.mKeys)) * 31) + java.util.Arrays.hashCode(this.mMap);
        for (int i = 0; i < this.mSize; i++) {
            int i2 = this.mMap[i];
            for (int i3 = 0; i3 < this.mSize; i3++) {
                iHashCode = (iHashCode * 31) + (valueAtInternal(i2, this.mMap[i3]) ? 1 : 0);
            }
        }
        return iHashCode;
    }

    public boolean equals(java.lang.Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof com.android.server.utils.WatchedSparseBooleanMatrix)) {
            return false;
        }
        com.android.server.utils.WatchedSparseBooleanMatrix other = (com.android.server.utils.WatchedSparseBooleanMatrix) that;
        if (this.mSize != other.mSize || !java.util.Arrays.equals(this.mKeys, other.mKeys)) {
            return false;
        }
        for (int i = 0; i < this.mSize; i++) {
            int row = this.mMap[i];
            for (int j = 0; j < this.mSize; j++) {
                int col = this.mMap[j];
                if (valueAtInternal(row, col) != other.valueAtInternal(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    java.lang.String[] matrixToStringMeta() {
        java.lang.String[] result = new java.lang.String[3];
        java.lang.StringBuilder k = new java.lang.StringBuilder();
        for (int i = 0; i < this.mSize; i++) {
            k.append(this.mKeys[i]);
            if (i < this.mSize - 1) {
                k.append(" ");
            }
        }
        result[0] = k.substring(0);
        java.lang.StringBuilder m = new java.lang.StringBuilder();
        for (int i2 = 0; i2 < this.mSize; i2++) {
            m.append(this.mMap[i2]);
            if (i2 < this.mSize - 1) {
                m.append(" ");
            }
        }
        result[1] = m.substring(0);
        java.lang.StringBuilder u = new java.lang.StringBuilder();
        for (int i3 = 0; i3 < this.mOrder; i3++) {
            u.append(this.mInUse[i3] ? "1" : "0");
        }
        result[2] = u.substring(0);
        return result;
    }

    java.lang.String[] matrixToStringRaw() {
        java.lang.String[] result = new java.lang.String[this.mOrder];
        for (int i = 0; i < this.mOrder; i++) {
            java.lang.StringBuilder line = new java.lang.StringBuilder(this.mOrder);
            for (int j = 0; j < this.mOrder; j++) {
                line.append(valueAtInternal(i, j) ? "1" : "0");
            }
            result[i] = line.substring(0);
        }
        return result;
    }

    java.lang.String[] matrixToStringCooked() {
        java.lang.String[] result = new java.lang.String[this.mSize];
        for (int i = 0; i < this.mSize; i++) {
            int row = this.mMap[i];
            java.lang.StringBuilder line = new java.lang.StringBuilder(this.mSize);
            for (int j = 0; j < this.mSize; j++) {
                line.append(valueAtInternal(row, this.mMap[j]) ? "1" : "0");
            }
            result[i] = line.substring(0);
        }
        return result;
    }

    public java.lang.String[] matrixToString(boolean raw) {
        java.lang.String[] data;
        java.lang.String[] meta = matrixToStringMeta();
        if (raw) {
            data = matrixToStringRaw();
        } else {
            data = matrixToStringCooked();
        }
        java.lang.String[] result = new java.lang.String[meta.length + data.length];
        java.lang.System.arraycopy(meta, 0, result, 0, meta.length);
        java.lang.System.arraycopy(data, 0, result, meta.length, data.length);
        return result;
    }

    public java.lang.String toString() {
        return "{" + this.mSize + "x" + this.mSize + "}";
    }

    private static int binarySearch(int[] array, int size, int value) {
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int midVal = array[mid];
            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal > value) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return ~lo;
    }
}
