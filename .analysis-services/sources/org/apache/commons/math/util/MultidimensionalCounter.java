package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public class MultidimensionalCounter implements java.lang.Iterable<java.lang.Integer> {
    private final int dimension;
    private final int last;
    private final int[] size;
    private final int totalSize;
    private final int[] uniCounterOffset;

    public class Iterator implements java.util.Iterator<java.lang.Integer> {
        private int count = -1;
        private final int[] counter;

        Iterator() {
            this.counter = new int[org.apache.commons.math.util.MultidimensionalCounter.this.dimension];
            this.counter[org.apache.commons.math.util.MultidimensionalCounter.this.last] = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            for (int i = 0; i < org.apache.commons.math.util.MultidimensionalCounter.this.dimension; i++) {
                if (this.counter[i] != org.apache.commons.math.util.MultidimensionalCounter.this.size[i] - 1) {
                    return true;
                }
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public java.lang.Integer next() {
            int i = org.apache.commons.math.util.MultidimensionalCounter.this.last;
            while (true) {
                if (i >= 0) {
                    if (this.counter[i] == org.apache.commons.math.util.MultidimensionalCounter.this.size[i] - 1) {
                        this.counter[i] = 0;
                        i--;
                    } else {
                        int[] iArr = this.counter;
                        iArr[i] = iArr[i] + 1;
                        break;
                    }
                } else {
                    break;
                }
            }
            int i2 = this.count;
            int i3 = i2 + 1;
            this.count = i3;
            return java.lang.Integer.valueOf(i3);
        }

        public int getCount() {
            return this.count;
        }

        public int[] getCounts() {
            return org.apache.commons.math.util.MultidimensionalCounter.this.copyOf(this.counter, org.apache.commons.math.util.MultidimensionalCounter.this.dimension);
        }

        public int getCount(int dim) {
            return this.counter[dim];
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public MultidimensionalCounter(int... size) {
        this.dimension = size.length;
        this.size = copyOf(size, this.dimension);
        this.uniCounterOffset = new int[this.dimension];
        this.last = this.dimension - 1;
        int tS = size[this.last];
        for (int i = 0; i < this.last; i++) {
            int count = 1;
            for (int j = i + 1; j < this.dimension; j++) {
                count *= size[j];
            }
            this.uniCounterOffset[i] = count;
            tS *= size[i];
        }
        this.uniCounterOffset[this.last] = 0;
        if (tS <= 0) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(java.lang.Integer.valueOf(tS));
        }
        this.totalSize = tS;
    }

    @Override // java.lang.Iterable
    public java.util.Iterator<java.lang.Integer> iterator() {
        return new org.apache.commons.math.util.MultidimensionalCounter.Iterator();
    }

    public int getDimension() {
        return this.dimension;
    }

    public int[] getCounts(int index) {
        if (index < 0 || index >= this.totalSize) {
            throw new org.apache.commons.math.exception.OutOfRangeException(java.lang.Integer.valueOf(index), 0, java.lang.Integer.valueOf(this.totalSize));
        }
        int[] indices = new int[this.dimension];
        int count = 0;
        for (int i = 0; i < this.last; i++) {
            int idx = 0;
            int offset = this.uniCounterOffset[i];
            while (count <= index) {
                count += offset;
                idx++;
            }
            count -= offset;
            indices[i] = idx - 1;
        }
        int idx2 = 1;
        while (count < index) {
            count += idx2;
            idx2++;
        }
        indices[this.last] = idx2 - 1;
        return indices;
    }

    public int getCount(int... c) throws org.apache.commons.math.exception.OutOfRangeException {
        if (c.length != this.dimension) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(c.length, this.dimension);
        }
        int count = 0;
        for (int i = 0; i < this.dimension; i++) {
            int index = c[i];
            if (index < 0 || index >= this.size[i]) {
                throw new org.apache.commons.math.exception.OutOfRangeException(java.lang.Integer.valueOf(index), 0, java.lang.Integer.valueOf(this.size[i] - 1));
            }
            count += this.uniCounterOffset[i] * c[i];
        }
        int i2 = this.last;
        return c[i2] + count;
    }

    public int getSize() {
        return this.totalSize;
    }

    public int[] getSizes() {
        return copyOf(this.size, this.dimension);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < this.dimension; i++) {
            sb.append("[").append(getCount(i)).append("]");
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] copyOf(int[] source, int newLen) {
        int[] output = new int[newLen];
        java.lang.System.arraycopy(source, 0, output, 0, java.lang.Math.min(source.length, newLen));
        return output;
    }
}
