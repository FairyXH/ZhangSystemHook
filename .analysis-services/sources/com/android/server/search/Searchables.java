package com.android.server.search;

/* JADX INFO: loaded from: classes3.dex */
public class Searchables {
    private static final java.lang.String LOG_TAG = "Searchables";
    private static final java.lang.String MD_LABEL_DEFAULT_SEARCHABLE = "android.app.default_searchable";
    private static final java.lang.String MD_SEARCHABLE_SYSTEM_SEARCH = "*";
    private android.content.Context mContext;
    private java.util.List<android.content.pm.ResolveInfo> mGlobalSearchActivities;
    private int mUserId;
    public static java.lang.String GOOGLE_SEARCH_COMPONENT_NAME = "com.android.googlesearch/.GoogleSearch";
    public static java.lang.String ENHANCED_GOOGLE_SEARCH_COMPONENT_NAME = "com.google.android.providers.enhancedgooglesearch/.Launcher";
    private static final java.util.Comparator<android.content.pm.ResolveInfo> GLOBAL_SEARCH_RANKER = new java.util.Comparator<android.content.pm.ResolveInfo>() { // from class: com.android.server.search.Searchables.1
        @Override // java.util.Comparator
        public int compare(android.content.pm.ResolveInfo lhs, android.content.pm.ResolveInfo rhs) {
            if (lhs == rhs) {
                return 0;
            }
            boolean lhsSystem = com.android.server.search.Searchables.isSystemApp(lhs);
            boolean rhsSystem = com.android.server.search.Searchables.isSystemApp(rhs);
            if (lhsSystem && !rhsSystem) {
                return -1;
            }
            if (rhsSystem && !lhsSystem) {
                return 1;
            }
            return rhs.priority - lhs.priority;
        }
    };
    private java.util.HashMap<android.content.ComponentName, android.app.SearchableInfo> mSearchablesMap = null;
    private java.util.ArrayList<android.app.SearchableInfo> mSearchablesList = null;
    private java.util.ArrayList<android.app.SearchableInfo> mSearchablesInGlobalSearchList = null;
    private android.content.ComponentName mCurrentGlobalSearchActivity = null;
    private android.content.ComponentName mWebSearchActivity = null;
    private boolean mRebuildSearchables = true;
    private android.util.ArraySet<java.lang.String> mKnownSearchablePackageNames = new android.util.ArraySet<>();
    private final com.android.server.search.ISearchablesWrapper mSearchablesWrapper = new com.android.server.search.Searchables.SearchablesWrapper();
    private final android.content.pm.IPackageManager mPm = android.app.AppGlobals.getPackageManager();

    public Searchables(android.content.Context context, int userId) {
        this.mContext = context;
        this.mUserId = userId;
    }

    public android.app.SearchableInfo getSearchableInfo(android.content.ComponentName activity) {
        android.content.ComponentName referredActivity;
        android.app.SearchableInfo result;
        android.os.Bundle md;
        synchronized (this) {
            android.app.SearchableInfo result2 = this.mSearchablesMap.get(activity);
            if (result2 != null) {
                android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                if (pm.canAccessComponent(android.os.Binder.getCallingUid(), result2.getSearchActivity(), android.os.UserHandle.getCallingUserId())) {
                    return result2;
                }
                return null;
            }
            try {
                android.content.pm.ActivityInfo ai = this.mPm.getActivityInfo(activity, 128L, this.mUserId);
                if (ai == null) {
                    return null;
                }
                java.lang.String refActivityName = null;
                android.os.Bundle md2 = ai.metaData;
                if (md2 != null) {
                    refActivityName = md2.getString(MD_LABEL_DEFAULT_SEARCHABLE);
                }
                if (refActivityName == null && (md = ai.applicationInfo.metaData) != null) {
                    refActivityName = md.getString(MD_LABEL_DEFAULT_SEARCHABLE);
                }
                if (refActivityName == null || refActivityName.equals("*")) {
                    return null;
                }
                java.lang.String pkg = activity.getPackageName();
                if (refActivityName.charAt(0) == '.') {
                    referredActivity = new android.content.ComponentName(pkg, pkg + refActivityName);
                } else {
                    referredActivity = new android.content.ComponentName(pkg, refActivityName);
                }
                synchronized (this) {
                    result = this.mSearchablesMap.get(referredActivity);
                    if (result != null) {
                        this.mSearchablesMap.put(activity, result);
                    }
                }
                if (result != null) {
                    android.content.pm.PackageManagerInternal pm2 = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                    if (pm2.canAccessComponent(android.os.Binder.getCallingUid(), result.getSearchActivity(), android.os.UserHandle.getCallingUserId())) {
                        return result;
                    }
                    return null;
                }
                return null;
            } catch (android.os.RemoteException re) {
                android.util.Log.e(LOG_TAG, "Error getting activity info " + re);
                return null;
            }
        }
    }

    public void updateSearchableListIfNeeded() throws java.lang.Throwable {
        android.content.pm.ResolveInfo info;
        android.app.SearchableInfo searchable;
        synchronized (this) {
            if (!this.mRebuildSearchables) {
                return;
            }
            java.util.HashMap<android.content.ComponentName, android.app.SearchableInfo> newSearchablesMap = new java.util.HashMap<>();
            java.util.ArrayList<android.app.SearchableInfo> newSearchablesList = new java.util.ArrayList<>();
            java.util.ArrayList<android.app.SearchableInfo> newSearchablesInGlobalSearchList = new java.util.ArrayList<>();
            android.util.ArraySet<java.lang.String> newKnownSearchablePackageNames = new android.util.ArraySet<>();
            android.content.Intent intent = new android.content.Intent("android.intent.action.SEARCH");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                java.util.List<android.content.pm.ResolveInfo> searchList = queryIntentActivities(intent, 268435584);
                android.content.Intent webSearchIntent = new android.content.Intent("android.intent.action.WEB_SEARCH");
                java.util.List<android.content.pm.ResolveInfo> webSearchInfoList = queryIntentActivities(webSearchIntent, 268435584);
                if (searchList != null || webSearchInfoList != null) {
                    int search_count = searchList == null ? 0 : searchList.size();
                    int web_search_count = webSearchInfoList == null ? 0 : webSearchInfoList.size();
                    int count = search_count + web_search_count;
                    int ii = 0;
                    while (ii < count) {
                        if (ii < search_count) {
                            try {
                                info = searchList.get(ii);
                            } catch (java.lang.Throwable th) {
                                th = th;
                                android.os.Binder.restoreCallingIdentity(ident);
                                throw th;
                            }
                        } else {
                            info = webSearchInfoList.get(ii - search_count);
                        }
                        android.content.Intent intent2 = intent;
                        try {
                            android.content.pm.ActivityInfo ai = info.activityInfo;
                            java.util.List<android.content.pm.ResolveInfo> searchList2 = searchList;
                            android.content.Intent webSearchIntent2 = webSearchIntent;
                            if (newSearchablesMap.get(new android.content.ComponentName(ai.packageName, ai.name)) == null && (searchable = android.app.SearchableInfo.getActivityMetaData(this.mContext, ai, this.mUserId)) != null) {
                                newSearchablesList.add(searchable);
                                newKnownSearchablePackageNames.add(ai.packageName);
                                newSearchablesMap.put(searchable.getSearchActivity(), searchable);
                                if (searchable.shouldIncludeInGlobalSearch()) {
                                    newSearchablesInGlobalSearchList.add(searchable);
                                }
                            }
                            ii++;
                            intent = intent2;
                            searchList = searchList2;
                            webSearchIntent = webSearchIntent2;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            android.os.Binder.restoreCallingIdentity(ident);
                            throw th;
                        }
                    }
                }
                java.util.List<android.content.pm.ResolveInfo> newGlobalSearchActivities = findGlobalSearchActivities();
                android.content.ComponentName newGlobalSearchActivity = findGlobalSearchActivity(newGlobalSearchActivities);
                android.content.ComponentName newWebSearchActivity = findWebSearchActivity(newGlobalSearchActivity);
                synchronized (this) {
                    this.mSearchablesMap = newSearchablesMap;
                    this.mSearchablesList = newSearchablesList;
                    this.mKnownSearchablePackageNames = newKnownSearchablePackageNames;
                    this.mSearchablesInGlobalSearchList = newSearchablesInGlobalSearchList;
                    this.mGlobalSearchActivities = newGlobalSearchActivities;
                    this.mCurrentGlobalSearchActivity = newGlobalSearchActivity;
                    this.mWebSearchActivity = newWebSearchActivity;
                    for (android.content.pm.ResolveInfo globalSearchActivity : this.mGlobalSearchActivities) {
                        this.mKnownSearchablePackageNames.add(globalSearchActivity.getComponentInfo().packageName);
                    }
                    if (this.mCurrentGlobalSearchActivity != null) {
                        this.mKnownSearchablePackageNames.add(this.mCurrentGlobalSearchActivity.getPackageName());
                    }
                    if (this.mWebSearchActivity != null) {
                        this.mKnownSearchablePackageNames.add(this.mWebSearchActivity.getPackageName());
                    }
                    this.mRebuildSearchables = false;
                }
                android.os.Binder.restoreCallingIdentity(ident);
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    synchronized android.util.ArraySet<java.lang.String> getKnownSearchablePackageNames() {
        return this.mKnownSearchablePackageNames;
    }

    synchronized void invalidateSearchableList() {
        this.mRebuildSearchables = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.content.pm.ResolveInfo> findGlobalSearchActivities() {
        android.content.Intent intent = new android.content.Intent("android.search.action.GLOBAL_SEARCH");
        java.util.List<android.content.pm.ResolveInfo> activities = queryIntentActivities(intent, 268500992);
        if (activities != null && !activities.isEmpty()) {
            java.util.Collections.sort(activities, GLOBAL_SEARCH_RANKER);
        }
        return activities;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.ComponentName findGlobalSearchActivity(java.util.List<android.content.pm.ResolveInfo> installed) {
        android.content.ComponentName globalSearchComponent;
        java.lang.String searchProviderSetting = getGlobalSearchProviderSetting();
        if (!android.text.TextUtils.isEmpty(searchProviderSetting) && (globalSearchComponent = android.content.ComponentName.unflattenFromString(searchProviderSetting)) != null && isInstalled(globalSearchComponent)) {
            return globalSearchComponent;
        }
        return getDefaultGlobalSearchProvider(installed);
    }

    private boolean isInstalled(android.content.ComponentName globalSearch) {
        android.content.Intent intent = new android.content.Intent("android.search.action.GLOBAL_SEARCH");
        intent.setComponent(globalSearch);
        java.util.List<android.content.pm.ResolveInfo> activities = queryIntentActivities(intent, 65536);
        if (activities != null && !activities.isEmpty()) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean isSystemApp(android.content.pm.ResolveInfo res) {
        return (res.activityInfo.applicationInfo.flags & 1) != 0;
    }

    private android.content.ComponentName getDefaultGlobalSearchProvider(java.util.List<android.content.pm.ResolveInfo> providerList) {
        if (providerList != null && !providerList.isEmpty()) {
            android.content.pm.ActivityInfo ai = providerList.get(0).activityInfo;
            return new android.content.ComponentName(ai.packageName, ai.name);
        }
        android.util.Log.w(LOG_TAG, "No global search activity found");
        return null;
    }

    private java.lang.String getGlobalSearchProviderSetting() {
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        return android.provider.Settings.Secure.getStringForUser(cr, "search_global_search_activity", cr.getUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.ComponentName findWebSearchActivity(android.content.ComponentName globalSearchActivity) {
        if (globalSearchActivity == null) {
            return null;
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.WEB_SEARCH");
        intent.setPackage(globalSearchActivity.getPackageName());
        java.util.List<android.content.pm.ResolveInfo> activities = queryIntentActivities(intent, 65536);
        if (activities != null && !activities.isEmpty()) {
            android.content.pm.ActivityInfo ai = activities.get(0).activityInfo;
            return new android.content.ComponentName(ai.packageName, ai.name);
        }
        android.util.Log.w(LOG_TAG, "No web search activity found");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, int flags) {
        try {
            java.util.List<android.content.pm.ResolveInfo> activities = this.mPm.queryIntentActivities(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 8388608 | flags, this.mUserId).getList();
            return activities;
        } catch (android.os.RemoteException e) {
            return null;
        }
    }

    public synchronized java.util.ArrayList<android.app.SearchableInfo> getSearchablesList() {
        return createFilterdSearchableInfoList(this.mSearchablesList);
    }

    public synchronized java.util.ArrayList<android.app.SearchableInfo> getSearchablesInGlobalSearchList() {
        return createFilterdSearchableInfoList(this.mSearchablesInGlobalSearchList);
    }

    public synchronized java.util.ArrayList<android.content.pm.ResolveInfo> getGlobalSearchActivities() {
        return createFilterdResolveInfoList(this.mGlobalSearchActivities);
    }

    private java.util.ArrayList<android.app.SearchableInfo> createFilterdSearchableInfoList(java.util.List<android.app.SearchableInfo> list) {
        if (list == null) {
            return null;
        }
        java.util.ArrayList<android.app.SearchableInfo> resultList = new java.util.ArrayList<>(list.size());
        android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getCallingUserId();
        for (android.app.SearchableInfo info : list) {
            if (pm.canAccessComponent(callingUid, info.getSearchActivity(), callingUserId)) {
                resultList.add(info);
            }
        }
        return resultList;
    }

    private java.util.ArrayList<android.content.pm.ResolveInfo> createFilterdResolveInfoList(java.util.List<android.content.pm.ResolveInfo> list) {
        if (list == null) {
            return null;
        }
        java.util.ArrayList<android.content.pm.ResolveInfo> resultList = new java.util.ArrayList<>(list.size());
        android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getCallingUserId();
        for (android.content.pm.ResolveInfo info : list) {
            if (pm.canAccessComponent(callingUid, info.activityInfo.getComponentName(), callingUserId)) {
                resultList.add(info);
            }
        }
        return resultList;
    }

    public synchronized android.content.ComponentName getGlobalSearchActivity() {
        android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (this.mCurrentGlobalSearchActivity == null || !pm.canAccessComponent(callingUid, this.mCurrentGlobalSearchActivity, callingUserId)) {
            return null;
        }
        return this.mCurrentGlobalSearchActivity;
    }

    public synchronized android.content.ComponentName getWebSearchActivity() {
        android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (this.mWebSearchActivity == null || !pm.canAccessComponent(callingUid, this.mWebSearchActivity, callingUserId)) {
            return null;
        }
        return this.mWebSearchActivity;
    }

    void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        pw.println("Searchable authorities:");
        synchronized (this) {
            if (this.mSearchablesList != null) {
                for (android.app.SearchableInfo info : this.mSearchablesList) {
                    pw.print("  ");
                    pw.println(info.getSuggestAuthority());
                }
            }
            pw.println("mRebuildSearchables = " + this.mRebuildSearchables);
        }
    }

    com.android.server.search.ISearchablesWrapper getWrapper() {
        return this.mSearchablesWrapper;
    }

    private class SearchablesWrapper implements com.android.server.search.ISearchablesWrapper {
        private SearchablesWrapper() {
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public java.util.List<android.app.SearchableInfo> getSearchablesList() {
            return com.android.server.search.Searchables.this.mSearchablesList;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public java.util.List<android.app.SearchableInfo> getSearchablesInGlobalSearchList() {
            return com.android.server.search.Searchables.this.mSearchablesInGlobalSearchList;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public java.util.Map<android.content.ComponentName, android.app.SearchableInfo> getSearchablesMap() {
            return com.android.server.search.Searchables.this.mSearchablesMap;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public void setSearchablesMap(java.util.HashMap<android.content.ComponentName, android.app.SearchableInfo> searchablesMap) {
            com.android.server.search.Searchables.this.mSearchablesMap = searchablesMap;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, int flags) {
            return com.android.server.search.Searchables.this.queryIntentActivities(intent, flags);
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public java.util.List<android.content.pm.ResolveInfo> findGlobalSearchActivities() {
            return com.android.server.search.Searchables.this.findGlobalSearchActivities();
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public android.content.ComponentName findGlobalSearchActivity(java.util.List<android.content.pm.ResolveInfo> installed) {
            return com.android.server.search.Searchables.this.findGlobalSearchActivity(installed);
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public android.content.ComponentName findWebSearchActivity(android.content.ComponentName globalSearchActivity) {
            return com.android.server.search.Searchables.this.findWebSearchActivity(globalSearchActivity);
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public void setGlobalSearchActivities(java.util.List<android.content.pm.ResolveInfo> globalSearchActivities) {
            com.android.server.search.Searchables.this.mGlobalSearchActivities = globalSearchActivities;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public void setCurrentGlobalSearchActivity(android.content.ComponentName currentGlobalSearchActivity) {
            com.android.server.search.Searchables.this.mCurrentGlobalSearchActivity = currentGlobalSearchActivity;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public void setWebSearchActivity(android.content.ComponentName webSearchActivity) {
            com.android.server.search.Searchables.this.mWebSearchActivity = webSearchActivity;
        }
    }
}
