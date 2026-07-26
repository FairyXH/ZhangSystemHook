package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
public interface IIdmapManagerExt {
    default int checkSignaturesMatching(java.lang.String overlay, java.lang.String target, int fulfilledPolicies, int flag) {
        return fulfilledPolicies;
    }
}
