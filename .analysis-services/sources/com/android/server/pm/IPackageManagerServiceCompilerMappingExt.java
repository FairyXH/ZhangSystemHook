package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageManagerServiceCompilerMappingExt {
    default java.lang.String[] modifyReasonList(java.lang.String[] reason) {
        return reason;
    }

    default boolean getAndCheckValidityForOplus(int reason) {
        return false;
    }

    default boolean checkPropertiesForOplus(int reason) {
        return false;
    }
}
