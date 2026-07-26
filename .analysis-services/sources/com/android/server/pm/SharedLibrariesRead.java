package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface SharedLibrariesRead {
    void dump(java.io.PrintWriter printWriter, com.android.server.pm.DumpState dumpState);

    void dumpProto(android.util.proto.ProtoOutputStream protoOutputStream);

    com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> getAll();

    android.content.pm.SharedLibraryInfo getSharedLibraryInfo(java.lang.String str, long j);

    com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> getSharedLibraryInfos(java.lang.String str);

    com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> getStaticLibraryInfos(java.lang.String str);
}
