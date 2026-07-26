package com.android.server.search;

/* JADX INFO: loaded from: classes3.dex */
public interface ISearchablesWrapper {
    default java.util.List<android.app.SearchableInfo> getSearchablesList() {
        return null;
    }

    default java.util.List<android.app.SearchableInfo> getSearchablesInGlobalSearchList() {
        return null;
    }

    default java.util.Map<android.content.ComponentName, android.app.SearchableInfo> getSearchablesMap() {
        return null;
    }

    default void setSearchablesMap(java.util.HashMap<android.content.ComponentName, android.app.SearchableInfo> searchablesMap) {
    }

    default java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, int flags) {
        return null;
    }

    default java.util.List<android.content.pm.ResolveInfo> findGlobalSearchActivities() {
        return null;
    }

    default android.content.ComponentName findGlobalSearchActivity(java.util.List<android.content.pm.ResolveInfo> installed) {
        return null;
    }

    default android.content.ComponentName findWebSearchActivity(android.content.ComponentName globalSearchActivity) {
        return null;
    }

    default void setGlobalSearchActivities(java.util.List<android.content.pm.ResolveInfo> globalSearchActivities) {
    }

    default void setCurrentGlobalSearchActivity(android.content.ComponentName currentGlobalSearchActivity) {
    }

    default void setWebSearchActivity(android.content.ComponentName webSearchActivity) {
    }
}
