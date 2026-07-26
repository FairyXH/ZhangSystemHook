package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
class HarmfulDigests {
    private final java.util.Set<java.lang.String> mDigestSet;

    HarmfulDigests(java.util.List<byte[]> digests) {
        java.util.HashSet<java.lang.String> tmpDigestSet = new java.util.HashSet<>();
        int size = digests.size();
        for (int i = 0; i < size; i++) {
            tmpDigestSet.add(com.android.internal.util.HexDump.toHexString(digests.get(i)));
        }
        this.mDigestSet = java.util.Collections.unmodifiableSet(tmpDigestSet);
    }

    public boolean contains(byte[] digest) {
        return this.mDigestSet.contains(com.android.internal.util.HexDump.toHexString(digest));
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        for (java.lang.String digest : this.mDigestSet) {
            pw.println(digest);
        }
        pw.println("");
    }
}
