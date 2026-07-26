package com.android.server.contextualsearch;

/* JADX INFO: loaded from: classes.dex */
public interface IContextualManagerServiceExt {
    public static final java.lang.String TAG = "IContextualManagerServiceExt";

    default void initContextualExAndInner(android.content.Context context) {
    }

    default int beforeStartContextualSearchGetUserId(int entrypoint) {
        return 0;
    }
}
