package com.android.server.search;

/* JADX INFO: loaded from: classes3.dex */
public class SearchManagerService extends android.app.ISearchManager.Stub {
    private static final java.lang.String TAG = "SearchManagerService";
    private final android.content.Context mContext;
    final android.os.Handler mHandler;
    private final com.android.server.search.SearchManagerService.MyPackageMonitor mMyPackageMonitor;
    private final com.android.server.search.ISearchManagerServiceExt mServiceExtImpl = (com.android.server.search.ISearchManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.search.ISearchManagerServiceExt.class).create();
    private final android.util.SparseArray<com.android.server.search.Searchables> mSearchables = new android.util.SparseArray<>();

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.search.SearchManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mService = new com.android.server.search.SearchManagerService(getContext());
            publishBinderService("search", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onUserStopped(com.android.server.SystemService.TargetUser user) {
            this.mService.onCleanupUser(user.getUserIdentifier());
        }
    }

    public SearchManagerService(android.content.Context context) {
        this.mContext = context;
        this.mMyPackageMonitor = this.mServiceExtImpl.loadingReductionEnable() ? new com.android.server.search.SearchManagerService.MyPackageMonitorExt() : new com.android.server.search.SearchManagerService.MyPackageMonitor();
        this.mMyPackageMonitor.register(context, null, android.os.UserHandle.ALL, true);
        new com.android.server.search.SearchManagerService.GlobalSearchProviderObserver(context.getContentResolver());
        this.mHandler = com.android.server.IoThread.getHandler();
    }

    private com.android.server.search.Searchables getSearchables(int userId) {
        com.android.server.search.Searchables searchables;
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.os.UserManager um = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
            if (um.getUserInfo(userId) == null) {
                throw new java.lang.IllegalStateException("User " + userId + " doesn't exist");
            }
            if (!um.isUserUnlockingOrUnlocked(userId)) {
                throw new java.lang.IllegalStateException("User " + userId + " isn't unlocked");
            }
            android.os.Binder.restoreCallingIdentity(token);
            synchronized (this.mSearchables) {
                searchables = this.mSearchables.get(userId);
                if (searchables == null) {
                    searchables = new com.android.server.search.Searchables(this.mContext, userId);
                    this.mSearchables.put(userId, searchables);
                }
                searchables.updateSearchableListIfNeeded();
            }
            return searchables;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCleanupUser(int userId) {
        synchronized (this.mSearchables) {
            this.mSearchables.remove(userId);
        }
    }

    class MyPackageMonitor extends com.android.internal.content.PackageMonitor {
        private final java.util.ArrayList<java.lang.String> mChangedPackages = new java.util.ArrayList<>();
        private boolean mSearchablePackageAppeared = false;

        MyPackageMonitor() {
        }

        public void onBeginPackageChanges() {
            clearPackageChangeState();
        }

        public void onPackageAppeared(java.lang.String packageName, int reason) {
            if (!this.mSearchablePackageAppeared) {
                this.mSearchablePackageAppeared = hasSearchableForPackage(packageName, getChangingUserId());
            }
            this.mChangedPackages.add(packageName);
        }

        public void onPackageDisappeared(java.lang.String packageName, int reason) {
            this.mChangedPackages.add(packageName);
        }

        public void onPackageModified(java.lang.String packageName) {
            this.mChangedPackages.add(packageName);
        }

        public void onFinishPackageChanges() {
            onFinishPackageChangesInternal();
            clearPackageChangeState();
        }

        private void clearPackageChangeState() {
            this.mChangedPackages.clear();
            this.mSearchablePackageAppeared = false;
        }

        private boolean hasSearchableForPackage(java.lang.String packageName, int userId) {
            java.util.List<android.content.pm.ResolveInfo> searchList = com.android.server.search.SearchManagerService.querySearchableActivities(com.android.server.search.SearchManagerService.this.mContext, new android.content.Intent("android.intent.action.SEARCH").setPackage(packageName), userId);
            if (!searchList.isEmpty()) {
                return true;
            }
            java.util.List<android.content.pm.ResolveInfo> webSearchList = com.android.server.search.SearchManagerService.querySearchableActivities(com.android.server.search.SearchManagerService.this.mContext, new android.content.Intent("android.intent.action.WEB_SEARCH").setPackage(packageName), userId);
            if (!webSearchList.isEmpty()) {
                return true;
            }
            java.util.List<android.content.pm.ResolveInfo> globalSearchList = com.android.server.search.SearchManagerService.querySearchableActivities(com.android.server.search.SearchManagerService.this.mContext, new android.content.Intent("android.search.action.GLOBAL_SEARCH").setPackage(packageName), userId);
            return true ^ globalSearchList.isEmpty();
        }

        private boolean shouldRebuildSearchableList(int changingUserId) {
            if (this.mSearchablePackageAppeared) {
                return true;
            }
            android.util.ArraySet<java.lang.String> knownSearchablePackageNames = new android.util.ArraySet<>();
            synchronized (com.android.server.search.SearchManagerService.this.mSearchables) {
                com.android.server.search.Searchables searchables = (com.android.server.search.Searchables) com.android.server.search.SearchManagerService.this.mSearchables.get(changingUserId);
                if (searchables != null) {
                    knownSearchablePackageNames = searchables.getKnownSearchablePackageNames();
                }
            }
            int numOfPackages = this.mChangedPackages.size();
            for (int i = 0; i < numOfPackages; i++) {
                java.lang.String packageName = this.mChangedPackages.get(i);
                if (knownSearchablePackageNames.contains(packageName)) {
                    return true;
                }
            }
            return false;
        }

        private void onFinishPackageChangesInternal() {
            int changingUserId = getChangingUserId();
            if (!shouldRebuildSearchableList(changingUserId)) {
                return;
            }
            synchronized (com.android.server.search.SearchManagerService.this.mSearchables) {
                com.android.server.search.Searchables searchables = (com.android.server.search.Searchables) com.android.server.search.SearchManagerService.this.mSearchables.get(changingUserId);
                if (searchables != null) {
                    searchables.invalidateSearchableList();
                }
            }
            android.content.Intent intent = new android.content.Intent("android.search.action.SEARCHABLES_CHANGED");
            intent.addFlags(603979776);
            com.android.server.search.SearchManagerService.this.mContext.sendBroadcastAsUser(intent, new android.os.UserHandle(changingUserId));
        }
    }

    class MyPackageMonitorExt extends com.android.server.search.SearchManagerService.MyPackageMonitor {
        MyPackageMonitorExt() {
            super();
        }

        @Override // com.android.server.search.SearchManagerService.MyPackageMonitor
        public void onPackageModified(java.lang.String pkg) {
            updateSearchables(pkg);
        }

        public void onPackagesSuspended(java.lang.String[] packages) {
            updateSearchables(packages);
        }

        public void onPackagesUnsuspended(java.lang.String[] packages) {
            updateSearchables(packages);
        }

        public void onPackagesAvailable(java.lang.String[] packages) {
            updateSearchables(packages);
        }

        public void onPackagesUnavailable(java.lang.String[] packages) {
            updateSearchables(true, packages);
        }

        public void onPackageAdded(java.lang.String packageName, int uid) {
            updateSearchables(packageName);
        }

        public void onPackageRemoved(java.lang.String packageName, int uid) {
            updateSearchables(true, packageName);
        }

        private void updateSearchables(java.lang.String... pkgs) {
            updateSearchables(false, pkgs);
        }

        private void updateSearchables(boolean removeOnly, java.lang.String... pkgs) {
            int changingUserId = getChangingUserId();
            android.os.Trace.traceBegin(524288L, "updateSearchables");
            synchronized (com.android.server.search.SearchManagerService.this.mSearchables) {
                com.android.server.search.Searchables searchables = (com.android.server.search.Searchables) com.android.server.search.SearchManagerService.this.mSearchables.get(changingUserId);
                if (searchables != null) {
                    com.android.server.search.ISearchablesExt impl = (com.android.server.search.ISearchablesExt) system.ext.loader.core.ExtLoader.type(com.android.server.search.ISearchablesExt.class).create();
                    if (removeOnly) {
                        impl.removeFromSearchableList(searchables, pkgs);
                    } else {
                        impl.updateSearchableList(com.android.server.search.SearchManagerService.this.mContext, changingUserId, searchables, pkgs);
                    }
                }
            }
            android.content.Intent intent = new android.content.Intent("android.search.action.SEARCHABLES_CHANGED");
            intent.addFlags(603979776);
            com.android.server.search.SearchManagerService.this.mContext.sendBroadcastAsUser(intent, new android.os.UserHandle(changingUserId));
            android.os.Trace.traceEnd(524288L);
        }
    }

    static java.util.List<android.content.pm.ResolveInfo> querySearchableActivities(android.content.Context context, android.content.Intent searchIntent, int userId) {
        java.util.List<android.content.pm.ResolveInfo> activities = context.getPackageManager().queryIntentActivitiesAsUser(searchIntent, 276824192, userId);
        return activities;
    }

    class GlobalSearchProviderObserver extends android.database.ContentObserver {
        private final android.content.ContentResolver mResolver;

        public GlobalSearchProviderObserver(android.content.ContentResolver resolver) {
            super(null);
            this.mResolver = resolver;
            this.mResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("search_global_search_activity"), false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            synchronized (com.android.server.search.SearchManagerService.this.mSearchables) {
                for (int i = 0; i < com.android.server.search.SearchManagerService.this.mSearchables.size(); i++) {
                    ((com.android.server.search.Searchables) com.android.server.search.SearchManagerService.this.mSearchables.valueAt(i)).invalidateSearchableList();
                }
            }
            android.content.Intent intent = new android.content.Intent("android.search.action.GLOBAL_SEARCH_ACTIVITY_CHANGED");
            intent.addFlags(536870912);
            com.android.server.search.SearchManagerService.this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
        }
    }

    public android.app.SearchableInfo getSearchableInfo(android.content.ComponentName launchActivity) {
        if (launchActivity == null) {
            android.util.Log.e(TAG, "getSearchableInfo(), activity == null");
            return null;
        }
        return getSearchables(android.os.UserHandle.getCallingUserId()).getSearchableInfo(launchActivity);
    }

    public java.util.List<android.app.SearchableInfo> getSearchablesInGlobalSearch() {
        return getSearchables(android.os.UserHandle.getCallingUserId()).getSearchablesInGlobalSearchList();
    }

    public java.util.List<android.content.pm.ResolveInfo> getGlobalSearchActivities() {
        return getSearchables(android.os.UserHandle.getCallingUserId()).getGlobalSearchActivities();
    }

    public android.content.ComponentName getGlobalSearchActivity() {
        return getSearchables(android.os.UserHandle.getCallingUserId()).getGlobalSearchActivity();
    }

    public android.content.ComponentName getWebSearchActivity() {
        return getSearchables(android.os.UserHandle.getCallingUserId()).getWebSearchActivity();
    }

    public void launchAssist(int userHandle, android.os.Bundle args) {
        com.android.server.statusbar.StatusBarManagerInternal statusBarManager = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
        if (statusBarManager != null) {
            statusBarManager.startAssist(args);
        }
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            java.io.PrintWriter indentingPrintWriter = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
            synchronized (this.mSearchables) {
                for (int i = 0; i < this.mSearchables.size(); i++) {
                    indentingPrintWriter.print("\nUser: ");
                    indentingPrintWriter.println(this.mSearchables.keyAt(i));
                    indentingPrintWriter.increaseIndent();
                    this.mSearchables.valueAt(i).dump(fd, indentingPrintWriter, args);
                    indentingPrintWriter.decreaseIndent();
                }
            }
            if (android.os.SystemProperties.getBoolean("persist.sys.osense.dump", false)) {
                if (this.mServiceExtImpl.loadingReductionEnable()) {
                    indentingPrintWriter.println("SearchManagerService:load reduction enabled");
                } else {
                    indentingPrintWriter.println("SearchManagerService:load reduction disabled");
                }
            }
        }
    }
}
