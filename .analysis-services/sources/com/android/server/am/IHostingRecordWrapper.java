package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IHostingRecordWrapper {
    default com.android.server.am.IHostingRecordExt getExtImpl() {
        return new com.android.server.am.IHostingRecordExt() { // from class: com.android.server.am.IHostingRecordWrapper.1
        };
    }
}
