package com.android.server.accounts;

/* JADX INFO: loaded from: classes.dex */
public interface IAccountManagerServiceExt {
    default boolean isMultiAppUserId(int userId) {
        return false;
    }
}
