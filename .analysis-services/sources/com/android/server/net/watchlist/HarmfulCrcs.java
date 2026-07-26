package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
class HarmfulCrcs {
    private final java.util.Set<java.lang.Integer> mCrcSet;

    HarmfulCrcs(java.util.List<byte[]> digests) {
        java.util.HashSet<java.lang.Integer> crcSet = new java.util.HashSet<>();
        int size = digests.size();
        for (int i = 0; i < size; i++) {
            byte[] bytes = digests.get(i);
            if (bytes.length <= 4) {
                int crc = 0;
                for (byte b : bytes) {
                    crc = (crc << 8) | (b & 255);
                }
                crcSet.add(java.lang.Integer.valueOf(crc));
            }
        }
        this.mCrcSet = java.util.Collections.unmodifiableSet(crcSet);
    }

    public boolean contains(int crc) {
        return this.mCrcSet.contains(java.lang.Integer.valueOf(crc));
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        java.util.Iterator<java.lang.Integer> it = this.mCrcSet.iterator();
        while (it.hasNext()) {
            int crc = it.next().intValue();
            pw.println(com.android.internal.util.HexDump.toHexString(crc));
        }
        pw.println("");
    }
}
