package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class DensityMapping {
    private final com.android.server.display.DensityMapping.Entry[] mSortedDensityMappingEntries;

    static com.android.server.display.DensityMapping createByOwning(com.android.server.display.DensityMapping.Entry[] densityMappingEntries) {
        return new com.android.server.display.DensityMapping(densityMappingEntries);
    }

    private DensityMapping(com.android.server.display.DensityMapping.Entry[] densityMappingEntries) {
        java.util.Arrays.sort(densityMappingEntries, java.util.Comparator.comparingInt(new java.util.function.ToIntFunction() { // from class: com.android.server.display.DensityMapping$$ExternalSyntheticLambda0
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return ((com.android.server.display.DensityMapping.Entry) obj).squaredDiagonal;
            }
        }));
        this.mSortedDensityMappingEntries = densityMappingEntries;
        verifyDensityMapping(this.mSortedDensityMappingEntries);
    }

    public int getDensityForResolution(int width, int height) {
        int squaredDiagonal = (width * width) + (height * height);
        com.android.server.display.DensityMapping.Entry left = com.android.server.display.DensityMapping.Entry.ZEROES;
        com.android.server.display.DensityMapping.Entry right = null;
        com.android.server.display.DensityMapping.Entry[] entryArr = this.mSortedDensityMappingEntries;
        int length = entryArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            com.android.server.display.DensityMapping.Entry entry = entryArr[i];
            if (entry.squaredDiagonal <= squaredDiagonal) {
                left = entry;
                i++;
            } else {
                right = entry;
                break;
            }
        }
        if (left.squaredDiagonal == squaredDiagonal) {
            return left.density;
        }
        if (right == null) {
            right = left;
            left = com.android.server.display.DensityMapping.Entry.ZEROES;
        }
        double leftDiagonal = java.lang.Math.sqrt(left.squaredDiagonal);
        double rightDiagonal = java.lang.Math.sqrt(right.squaredDiagonal);
        double diagonal = java.lang.Math.sqrt(squaredDiagonal);
        return (int) java.lang.Math.round((((diagonal - leftDiagonal) * ((double) (right.density - left.density))) / (rightDiagonal - leftDiagonal)) + ((double) left.density));
    }

    private static void verifyDensityMapping(com.android.server.display.DensityMapping.Entry[] sortedEntries) {
        for (int i = 1; i < sortedEntries.length; i++) {
            com.android.server.display.DensityMapping.Entry prev = sortedEntries[i - 1];
            com.android.server.display.DensityMapping.Entry curr = sortedEntries[i];
            if (prev.squaredDiagonal == curr.squaredDiagonal) {
                throw new java.lang.IllegalStateException("Found two entries in the density mapping with the same diagonal: " + prev + ", " + curr);
            }
            if (prev.density > curr.density) {
                throw new java.lang.IllegalStateException("Found two entries in the density mapping with increasing diagonal but decreasing density: " + prev + ", " + curr);
            }
        }
    }

    public java.lang.String toString() {
        return "DensityMapping{mDensityMappingEntries=" + java.util.Arrays.toString(this.mSortedDensityMappingEntries) + '}';
    }

    static class Entry {
        public static final com.android.server.display.DensityMapping.Entry ZEROES = new com.android.server.display.DensityMapping.Entry(0, 0, 0);
        public final int density;
        public final int squaredDiagonal;

        Entry(int width, int height, int density) {
            this.squaredDiagonal = (width * width) + (height * height);
            this.density = density;
        }

        public java.lang.String toString() {
            return "DensityMappingEntry{squaredDiagonal=" + this.squaredDiagonal + ", density=" + this.density + '}';
        }
    }
}
