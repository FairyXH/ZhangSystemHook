package com.android.server.search;

/* JADX INFO: loaded from: classes3.dex */
public interface ISearchablesExt {
    default void updateSearchableList(android.content.Context context, int userId, com.android.server.search.Searchables searchables, java.lang.String... pkg) {
    }

    default void removeFromSearchableList(com.android.server.search.Searchables searchables, java.lang.String... removePkgs) {
    }
}
