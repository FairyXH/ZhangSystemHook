package com.android.server.stats.pull;

/* JADX INFO: loaded from: classes3.dex */
public final class IonMemoryUtil {
    private static final java.lang.String DEBUG_SYSTEM_ION_HEAP_FILE = "/sys/kernel/debug/ion/heaps/system";
    private static final java.util.regex.Pattern ION_HEAP_SIZE_IN_BYTES = java.util.regex.Pattern.compile("\n\\s*total\\s*(\\d+)\\s*\n");
    private static final java.util.regex.Pattern PROCESS_ION_HEAP_SIZE_IN_BYTES = java.util.regex.Pattern.compile("\n\\s+\\S+\\s+(\\d+)\\s+(\\d+)");
    private static final java.lang.String TAG = "IonMemoryUtil";

    private IonMemoryUtil() {
    }

    public static long readSystemIonHeapSizeFromDebugfs() {
        return parseIonHeapSizeFromDebugfs(readFile(DEBUG_SYSTEM_ION_HEAP_FILE));
    }

    static long parseIonHeapSizeFromDebugfs(java.lang.String contents) {
        if (contents.isEmpty()) {
            return 0L;
        }
        java.util.regex.Matcher matcher = ION_HEAP_SIZE_IN_BYTES.matcher(contents);
        try {
            if (matcher.find()) {
                return java.lang.Long.parseLong(matcher.group(1));
            }
            return 0L;
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "Failed to parse value", e);
            return 0L;
        }
    }

    public static java.util.List<com.android.server.stats.pull.IonMemoryUtil.IonAllocations> readProcessSystemIonHeapSizesFromDebugfs() {
        return parseProcessIonHeapSizesFromDebugfs(readFile(DEBUG_SYSTEM_ION_HEAP_FILE));
    }

    static java.util.List<com.android.server.stats.pull.IonMemoryUtil.IonAllocations> parseProcessIonHeapSizesFromDebugfs(java.lang.String contents) {
        if (contents.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        java.util.regex.Matcher m = PROCESS_ION_HEAP_SIZE_IN_BYTES.matcher(contents);
        android.util.SparseArray<com.android.server.stats.pull.IonMemoryUtil.IonAllocations> entries = new android.util.SparseArray<>();
        while (m.find()) {
            try {
                int pid = java.lang.Integer.parseInt(m.group(1));
                long sizeInBytes = java.lang.Long.parseLong(m.group(2));
                com.android.server.stats.pull.IonMemoryUtil.IonAllocations allocations = entries.get(pid);
                if (allocations == null) {
                    allocations = new com.android.server.stats.pull.IonMemoryUtil.IonAllocations();
                    entries.put(pid, allocations);
                }
                allocations.pid = pid;
                allocations.totalSizeInBytes += sizeInBytes;
                allocations.count++;
                allocations.maxSizeInBytes = java.lang.Math.max(allocations.maxSizeInBytes, sizeInBytes);
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.e(TAG, "Failed to parse value", e);
            }
        }
        java.util.List<com.android.server.stats.pull.IonMemoryUtil.IonAllocations> result = new java.util.ArrayList<>(entries.size());
        for (int i = 0; i < entries.size(); i++) {
            result.add(entries.valueAt(i));
        }
        return result;
    }

    private static java.lang.String readFile(java.lang.String path) {
        try {
            java.io.File file = new java.io.File(path);
            return android.os.FileUtils.readTextFile(file, 0, null);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to read file", e);
            return "";
        }
    }

    public static final class IonAllocations {
        public int count;
        public long maxSizeInBytes;
        public int pid;
        public long totalSizeInBytes;

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.stats.pull.IonMemoryUtil.IonAllocations that = (com.android.server.stats.pull.IonMemoryUtil.IonAllocations) o;
            if (this.pid == that.pid && this.totalSizeInBytes == that.totalSizeInBytes && this.count == that.count && this.maxSizeInBytes == that.maxSizeInBytes) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.pid), java.lang.Long.valueOf(this.totalSizeInBytes), java.lang.Integer.valueOf(this.count), java.lang.Long.valueOf(this.maxSizeInBytes));
        }

        public java.lang.String toString() {
            return "IonAllocations{pid=" + this.pid + ", totalSizeInBytes=" + this.totalSizeInBytes + ", count=" + this.count + ", maxSizeInBytes=" + this.maxSizeInBytes + '}';
        }
    }
}
