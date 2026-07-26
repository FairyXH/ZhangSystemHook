package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IIntentBindRecordWrapper {
    default com.android.server.am.IIntentBindRecordExt getExtImpl() {
        return new com.android.server.am.IIntentBindRecordExt() { // from class: com.android.server.am.IIntentBindRecordWrapper.1
        };
    }
}
