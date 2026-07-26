package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ShortcutHelper {
    private static final android.content.IntentFilter SHARING_FILTER = new android.content.IntentFilter();
    private static final java.lang.String TAG = "ShortcutHelper";
    private java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>> mActiveShortcutBubbles = new java.util.HashMap<>();
    private final android.content.pm.LauncherApps.Callback mLauncherAppsCallback = new android.content.pm.LauncherApps.Callback() { // from class: com.android.server.notification.ShortcutHelper.1
        @Override // android.content.pm.LauncherApps.Callback
        public void onPackageRemoved(java.lang.String packageName, android.os.UserHandle user) {
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackageAdded(java.lang.String packageName, android.os.UserHandle user) {
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackageChanged(java.lang.String packageName, android.os.UserHandle user) {
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackagesAvailable(java.lang.String[] packageNames, android.os.UserHandle user, boolean replacing) {
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackagesUnavailable(java.lang.String[] packageNames, android.os.UserHandle user, boolean replacing) {
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onShortcutsChanged(java.lang.String packageName, java.util.List<android.content.pm.ShortcutInfo> shortcuts, android.os.UserHandle user) {
            java.util.HashMap<java.lang.String, java.lang.String> shortcutBubbles = (java.util.HashMap) com.android.server.notification.ShortcutHelper.this.mActiveShortcutBubbles.get(packageName);
            java.util.ArrayList<java.lang.String> bubbleKeysToRemove = new java.util.ArrayList<>();
            if (shortcutBubbles != null) {
                java.util.Set<java.lang.String> shortcutIds = new java.util.HashSet<>(shortcutBubbles.keySet());
                for (java.lang.String shortcutId : shortcutIds) {
                    boolean foundShortcut = false;
                    int i = 0;
                    while (true) {
                        if (i >= shortcuts.size()) {
                            break;
                        }
                        if (!shortcuts.get(i).getId().equals(shortcutId)) {
                            i++;
                        } else {
                            foundShortcut = true;
                            break;
                        }
                    }
                    if (!foundShortcut) {
                        bubbleKeysToRemove.add(shortcutBubbles.get(shortcutId));
                        shortcutBubbles.remove(shortcutId);
                        if (shortcutBubbles.isEmpty()) {
                            com.android.server.notification.ShortcutHelper.this.mActiveShortcutBubbles.remove(packageName);
                            if (com.android.server.notification.ShortcutHelper.this.mLauncherAppsCallbackRegistered && com.android.server.notification.ShortcutHelper.this.mActiveShortcutBubbles.isEmpty()) {
                                com.android.server.notification.ShortcutHelper.this.mLauncherAppsService.unregisterCallback(com.android.server.notification.ShortcutHelper.this.mLauncherAppsCallback);
                                com.android.server.notification.ShortcutHelper.this.mLauncherAppsCallbackRegistered = false;
                            }
                        }
                    }
                }
            }
            for (int i2 = 0; i2 < bubbleKeysToRemove.size(); i2++) {
                java.lang.String bubbleKey = bubbleKeysToRemove.get(i2);
                if (com.android.server.notification.ShortcutHelper.this.mShortcutListener != null) {
                    com.android.server.notification.ShortcutHelper.this.mShortcutListener.onShortcutRemoved(bubbleKey);
                }
            }
        }
    };
    private boolean mLauncherAppsCallbackRegistered;
    private android.content.pm.LauncherApps mLauncherAppsService;
    private com.android.server.notification.ShortcutHelper.ShortcutListener mShortcutListener;
    private android.content.pm.ShortcutServiceInternal mShortcutServiceInternal;
    private android.os.UserManager mUserManager;

    interface ShortcutListener {
        void onShortcutRemoved(java.lang.String str);
    }

    static {
        try {
            SHARING_FILTER.addDataType("*/*");
        } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
            android.util.Slog.e(TAG, "Bad mime type", e);
        }
    }

    ShortcutHelper(android.content.pm.LauncherApps launcherApps, com.android.server.notification.ShortcutHelper.ShortcutListener listener, android.content.pm.ShortcutServiceInternal shortcutServiceInternal, android.os.UserManager userManager) {
        this.mLauncherAppsService = launcherApps;
        this.mShortcutListener = listener;
        this.mShortcutServiceInternal = shortcutServiceInternal;
        this.mUserManager = userManager;
    }

    void setLauncherApps(android.content.pm.LauncherApps launcherApps) {
        this.mLauncherAppsService = launcherApps;
    }

    void setShortcutServiceInternal(android.content.pm.ShortcutServiceInternal shortcutServiceInternal) {
        this.mShortcutServiceInternal = shortcutServiceInternal;
    }

    void setUserManager(android.os.UserManager userManager) {
        this.mUserManager = userManager;
    }

    public static boolean isConversationShortcut(android.content.pm.ShortcutInfo shortcutInfo, android.content.pm.ShortcutServiceInternal mShortcutServiceInternal, int callingUserId) {
        if (shortcutInfo == null || !shortcutInfo.isLongLived() || !shortcutInfo.isEnabled()) {
            return false;
        }
        return true;
    }

    android.content.pm.ShortcutInfo getValidShortcutInfo(java.lang.String shortcutId, java.lang.String packageName, android.os.UserHandle user) {
        if (this.mLauncherAppsService == null || !this.mUserManager.isUserUnlocked(user)) {
            return null;
        }
        long token = android.os.Binder.clearCallingIdentity();
        if (shortcutId == null || packageName == null || user == null) {
            return null;
        }
        try {
            android.content.pm.LauncherApps.ShortcutQuery query = new android.content.pm.LauncherApps.ShortcutQuery();
            query.setPackage(packageName);
            query.setShortcutIds(java.util.Arrays.asList(shortcutId));
            query.setQueryFlags(3089);
            java.util.List<android.content.pm.ShortcutInfo> shortcuts = this.mLauncherAppsService.getShortcuts(query, user);
            android.content.pm.ShortcutInfo info = (shortcuts == null || shortcuts.size() <= 0) ? null : shortcuts.get(0);
            if (isConversationShortcut(info, this.mShortcutServiceInternal, user.getIdentifier())) {
                return info;
            }
            return null;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void cacheShortcut(android.content.pm.ShortcutInfo shortcutInfo, android.os.UserHandle user) {
        if (shortcutInfo.isLongLived() && !shortcutInfo.isCached()) {
            this.mShortcutServiceInternal.cacheShortcuts(user.getIdentifier(), com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, shortcutInfo.getPackage(), java.util.Collections.singletonList(shortcutInfo.getId()), shortcutInfo.getUserId(), 16384);
        }
    }

    void maybeListenForShortcutChangesForBubbles(com.android.server.notification.NotificationRecord r, boolean removedNotification, android.os.Handler handler) {
        java.lang.String shortcutId;
        if (r.getNotification().getBubbleMetadata() != null) {
            shortcutId = r.getNotification().getBubbleMetadata().getShortcutId();
        } else {
            shortcutId = null;
        }
        if (!removedNotification && !android.text.TextUtils.isEmpty(shortcutId) && r.getShortcutInfo() != null && r.getShortcutInfo().getId().equals(shortcutId)) {
            java.util.HashMap<java.lang.String, java.lang.String> packageBubbles = this.mActiveShortcutBubbles.get(r.getSbn().getPackageName());
            if (packageBubbles == null) {
                packageBubbles = new java.util.HashMap<>();
            }
            packageBubbles.put(shortcutId, r.getKey());
            this.mActiveShortcutBubbles.put(r.getSbn().getPackageName(), packageBubbles);
            if (!this.mLauncherAppsCallbackRegistered) {
                this.mLauncherAppsService.registerCallback(this.mLauncherAppsCallback, handler);
                this.mLauncherAppsCallbackRegistered = true;
                return;
            }
            return;
        }
        java.util.HashMap<java.lang.String, java.lang.String> packageBubbles2 = this.mActiveShortcutBubbles.get(r.getSbn().getPackageName());
        if (packageBubbles2 != null) {
            if (!android.text.TextUtils.isEmpty(shortcutId)) {
                packageBubbles2.remove(shortcutId);
            } else {
                java.util.Set<java.lang.String> shortcutIds = new java.util.HashSet<>(packageBubbles2.keySet());
                for (java.lang.String pkgShortcutId : shortcutIds) {
                    java.lang.String entryKey = packageBubbles2.get(pkgShortcutId);
                    if (r.getKey().equals(entryKey)) {
                        packageBubbles2.remove(pkgShortcutId);
                    }
                }
            }
            if (packageBubbles2.isEmpty()) {
                this.mActiveShortcutBubbles.remove(r.getSbn().getPackageName());
            }
        }
        if (this.mLauncherAppsCallbackRegistered && this.mActiveShortcutBubbles.isEmpty()) {
            this.mLauncherAppsService.unregisterCallback(this.mLauncherAppsCallback);
            this.mLauncherAppsCallbackRegistered = false;
        }
    }

    void destroy() {
        if (this.mLauncherAppsCallbackRegistered) {
            this.mLauncherAppsService.unregisterCallback(this.mLauncherAppsCallback);
            this.mLauncherAppsCallbackRegistered = false;
        }
    }
}
