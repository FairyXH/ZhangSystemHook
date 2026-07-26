package com.android.server.contentsuggestions;

/* JADX INFO: loaded from: classes.dex */
public interface IContentSuggestionsManagerServiceExt {
    public static final java.lang.String TAG = "IContentSuggestionsManagerServiceExt";

    default void initContentSuggestionsExAndInner(android.content.Context context) {
    }

    default boolean enforceCallerExt(int userId, java.lang.String func) {
        return false;
    }
}
