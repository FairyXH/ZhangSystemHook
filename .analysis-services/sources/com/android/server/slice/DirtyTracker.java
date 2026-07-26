package com.android.server.slice;

/* JADX INFO: loaded from: classes3.dex */
public interface DirtyTracker {

    public interface Persistable {
        java.lang.String getFileName();

        void writeTo(org.xmlpull.v1.XmlSerializer xmlSerializer) throws java.io.IOException;
    }

    void onPersistableDirty(com.android.server.slice.DirtyTracker.Persistable persistable);
}
