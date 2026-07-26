package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class GroupHelper {
    private static final int ALL_CHILDREN_FLAG = 16;
    private static final int ANY_CHILDREN_FLAGS = 34;
    protected static final java.lang.String AUTOGROUP_KEY = "ranker_group";
    protected static final int BASE_FLAGS = 1792;
    protected static final int FLAG_INVALID = -1;
    private static final java.lang.String TAG = "GroupHelper";
    private final int mAutoGroupAtCount;
    private final com.android.server.notification.GroupHelper.Callback mCallback;
    private final android.content.Context mContext;
    private com.android.server.notification.INotificationManagerServiceWrapper mNMSWrapper;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, com.android.server.notification.GroupHelper.NotificationAttributes>> mUngroupedNotifications = new android.util.ArrayMap<>();

    protected interface Callback {
        void addAutoGroup(java.lang.String str, boolean z);

        void addAutoGroupSummary(int i, java.lang.String str, java.lang.String str2, com.android.server.notification.GroupHelper.NotificationAttributes notificationAttributes);

        void removeAutoGroup(java.lang.String str);

        void removeAutoGroupSummary(int i, java.lang.String str);

        void updateAutogroupSummary(int i, java.lang.String str, com.android.server.notification.GroupHelper.NotificationAttributes notificationAttributes);
    }

    public GroupHelper(android.content.Context context, android.content.pm.PackageManager packageManager, int autoGroupAtCount, com.android.server.notification.GroupHelper.Callback callback) {
        this.mAutoGroupAtCount = autoGroupAtCount;
        this.mCallback = callback;
        this.mContext = context;
        this.mPackageManager = packageManager;
    }

    public GroupHelper(com.android.server.notification.INotificationManagerServiceWrapper wrapper, android.content.Context context, android.content.pm.PackageManager packageManager, int autoGroupAtCount, com.android.server.notification.GroupHelper.Callback callback) {
        this.mNMSWrapper = wrapper;
        this.mAutoGroupAtCount = autoGroupAtCount;
        this.mCallback = callback;
        this.mContext = context;
        this.mPackageManager = packageManager;
    }

    private java.lang.String generatePackageKey(int userId, java.lang.String pkg) {
        return userId + "|" + pkg;
    }

    protected int getAutogroupSummaryFlags(android.util.ArrayMap<java.lang.String, com.android.server.notification.GroupHelper.NotificationAttributes> children) {
        boolean allChildrenHasFlag = children.size() > 0;
        int anyChildFlagSet = 0;
        for (int i = 0; i < children.size(); i++) {
            if (!hasAnyFlag(children.valueAt(i).flags, 16)) {
                allChildrenHasFlag = false;
            }
            if (hasAnyFlag(children.valueAt(i).flags, 34)) {
                anyChildFlagSet |= children.valueAt(i).flags & 34;
            }
        }
        return (allChildrenHasFlag ? 16 : 0) | 1792 | anyChildFlagSet;
    }

    private boolean hasAnyFlag(int flags, int mask) {
        return (flags & mask) != 0;
    }

    public boolean onNotificationPosted(android.service.notification.StatusBarNotification sbn, boolean autogroupSummaryExists) {
        boolean isForceGroup = false;
        try {
            if (this.mNMSWrapper != null && this.mNMSWrapper.getNMSExt() != null) {
                isForceGroup = this.mNMSWrapper.getNMSExt().isForceGroup(sbn);
            }
            if (sbn.isAppGroup() && !isForceGroup) {
                maybeUngroup(sbn, false, sbn.getUserId());
                return false;
            }
            boolean sbnToBeAutogrouped = maybeGroup(sbn, autogroupSummaryExists);
            return sbnToBeAutogrouped;
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failure processing new notification", e);
            return false;
        }
    }

    public void onNotificationRemoved(android.service.notification.StatusBarNotification sbn) {
        try {
            maybeUngroup(sbn, true, sbn.getUserId());
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Error processing canceled notification", e);
        }
    }

    private boolean maybeGroup(android.service.notification.StatusBarNotification sbn, boolean autogroupSummaryExists) {
        int flags = 0;
        java.util.List<java.lang.String> notificationsToGroup = new java.util.ArrayList<>();
        java.util.List<com.android.server.notification.GroupHelper.NotificationAttributes> childrenAttr = new java.util.ArrayList<>();
        boolean sbnToBeAutogrouped = false;
        synchronized (this.mUngroupedNotifications) {
            java.lang.String packageKey = generatePackageKey(sbn.getUserId(), sbn.getPackageName());
            android.util.ArrayMap<java.lang.String, com.android.server.notification.GroupHelper.NotificationAttributes> children = this.mUngroupedNotifications.getOrDefault(packageKey, new android.util.ArrayMap<>());
            com.android.server.notification.GroupHelper.NotificationAttributes attr = new com.android.server.notification.GroupHelper.NotificationAttributes(sbn.getNotification().flags, sbn.getNotification().getSmallIcon(), sbn.getNotification().color, sbn.getNotification().visibility);
            children.put(sbn.getKey(), attr);
            this.mUngroupedNotifications.put(packageKey, children);
            if (children.size() >= this.mAutoGroupAtCount || autogroupSummaryExists) {
                flags = getAutogroupSummaryFlags(children);
                notificationsToGroup.addAll(children.keySet());
                childrenAttr.addAll(children.values());
            }
        }
        if (notificationsToGroup.size() > 0) {
            if (autogroupSummaryExists) {
                com.android.server.notification.GroupHelper.NotificationAttributes attr2 = new com.android.server.notification.GroupHelper.NotificationAttributes(flags, sbn.getNotification().getSmallIcon(), sbn.getNotification().color, 0);
                if (com.android.server.notification.Flags.autogroupSummaryIconUpdate()) {
                    attr2 = updateAutobundledSummaryAttributes(sbn.getPackageName(), childrenAttr, attr2);
                }
                this.mCallback.updateAutogroupSummary(sbn.getUserId(), sbn.getPackageName(), attr2);
            } else {
                android.graphics.drawable.Icon summaryIcon = sbn.getNotification().getSmallIcon();
                int summaryIconColor = sbn.getNotification().color;
                int summaryVisibility = 0;
                if (com.android.server.notification.Flags.autogroupSummaryIconUpdate()) {
                    com.android.server.notification.GroupHelper.NotificationAttributes iconAttr = getAutobundledSummaryAttributes(sbn.getPackageName(), childrenAttr);
                    summaryIcon = iconAttr.icon;
                    summaryIconColor = iconAttr.iconColor;
                    summaryVisibility = iconAttr.visibility;
                }
                com.android.server.notification.GroupHelper.NotificationAttributes attr3 = new com.android.server.notification.GroupHelper.NotificationAttributes(flags, summaryIcon, summaryIconColor, summaryVisibility);
                this.mCallback.addAutoGroupSummary(sbn.getUserId(), sbn.getPackageName(), sbn.getKey(), attr3);
            }
            for (java.lang.String keyToGroup : notificationsToGroup) {
                if (android.app.Flags.checkAutogroupBeforePost()) {
                    if (keyToGroup.equals(sbn.getKey())) {
                        sbnToBeAutogrouped = true;
                    } else {
                        this.mCallback.addAutoGroup(keyToGroup, true);
                    }
                } else {
                    this.mCallback.addAutoGroup(keyToGroup, true);
                }
            }
        }
        return sbnToBeAutogrouped;
    }

    private void maybeUngroup(android.service.notification.StatusBarNotification sbn, boolean notificationGone, int userId) {
        boolean removeSummary = false;
        int summaryFlags = -1;
        boolean updateSummaryFlags = false;
        boolean removeAutogroupOverlay = false;
        java.util.List<com.android.server.notification.GroupHelper.NotificationAttributes> childrenAttrs = new java.util.ArrayList<>();
        synchronized (this.mUngroupedNotifications) {
            java.lang.String key = generatePackageKey(sbn.getUserId(), sbn.getPackageName());
            android.util.ArrayMap<java.lang.String, com.android.server.notification.GroupHelper.NotificationAttributes> children = this.mUngroupedNotifications.getOrDefault(key, new android.util.ArrayMap<>());
            if (children.size() == 0) {
                return;
            }
            if (children.containsKey(sbn.getKey())) {
                int flags = children.remove(sbn.getKey()).flags;
                if (hasAnyFlag(flags, 34)) {
                    updateSummaryFlags = true;
                    summaryFlags = getAutogroupSummaryFlags(children);
                }
                if (!notificationGone && sbn.getOverrideGroupKey() != null) {
                    removeAutogroupOverlay = true;
                }
                if (children.size() == 0) {
                    removeSummary = true;
                } else {
                    childrenAttrs.addAll(children.values());
                }
            }
            if (removeSummary) {
                this.mCallback.removeAutoGroupSummary(userId, sbn.getPackageName());
            } else {
                com.android.server.notification.GroupHelper.NotificationAttributes attr = new com.android.server.notification.GroupHelper.NotificationAttributes(summaryFlags, sbn.getNotification().getSmallIcon(), sbn.getNotification().color, 0);
                boolean attributesUpdated = false;
                if (com.android.server.notification.Flags.autogroupSummaryIconUpdate()) {
                    com.android.server.notification.GroupHelper.NotificationAttributes newAttr = updateAutobundledSummaryAttributes(sbn.getPackageName(), childrenAttrs, attr);
                    if (!newAttr.equals(attr)) {
                        attributesUpdated = true;
                        attr = newAttr;
                    }
                }
                if (updateSummaryFlags || attributesUpdated) {
                    this.mCallback.updateAutogroupSummary(userId, sbn.getPackageName(), attr);
                }
            }
            if (removeAutogroupOverlay) {
                this.mCallback.removeAutoGroup(sbn.getKey());
            }
        }
    }

    int getNotGroupedByAppCount(int userId, java.lang.String pkg) {
        int size;
        synchronized (this.mUngroupedNotifications) {
            java.lang.String key = generatePackageKey(userId, pkg);
            android.util.ArrayMap<java.lang.String, com.android.server.notification.GroupHelper.NotificationAttributes> children = this.mUngroupedNotifications.getOrDefault(key, new android.util.ArrayMap<>());
            size = children.size();
        }
        return size;
    }

    com.android.server.notification.GroupHelper.NotificationAttributes getAutobundledSummaryAttributes(java.lang.String packageName, java.util.List<com.android.server.notification.GroupHelper.NotificationAttributes> childrenAttr) {
        android.graphics.drawable.Icon newIcon = null;
        boolean childrenHaveSameIcon = true;
        int newColor = 1;
        boolean childrenHaveSameColor = true;
        int newVisibility = 0;
        for (com.android.server.notification.GroupHelper.NotificationAttributes state : childrenAttr) {
            if (newIcon == null) {
                newIcon = state.icon;
            } else if (!newIcon.sameAs(state.icon)) {
                childrenHaveSameIcon = false;
            }
            if (newColor == 1) {
                newColor = state.iconColor;
            } else if (newColor != state.iconColor) {
                childrenHaveSameColor = false;
            }
            if (state.visibility == 1) {
                newVisibility = 1;
            }
        }
        if (!childrenHaveSameIcon) {
            newIcon = getMonochromeAppIcon(packageName);
        }
        if (!childrenHaveSameColor) {
            newColor = 0;
        }
        return new com.android.server.notification.GroupHelper.NotificationAttributes(0, newIcon, newColor, newVisibility);
    }

    com.android.server.notification.GroupHelper.NotificationAttributes updateAutobundledSummaryAttributes(java.lang.String packageName, java.util.List<com.android.server.notification.GroupHelper.NotificationAttributes> childrenAttr, com.android.server.notification.GroupHelper.NotificationAttributes oldAttr) {
        com.android.server.notification.GroupHelper.NotificationAttributes newAttr = getAutobundledSummaryAttributes(packageName, childrenAttr);
        android.graphics.drawable.Icon newIcon = newAttr.icon;
        int newColor = newAttr.iconColor;
        if (newAttr.icon == null) {
            newIcon = oldAttr.icon;
        }
        if (newAttr.iconColor == 1) {
            newColor = oldAttr.iconColor;
        }
        return new com.android.server.notification.GroupHelper.NotificationAttributes(oldAttr.flags, newIcon, newColor, newAttr.visibility);
    }

    android.graphics.drawable.Icon getMonochromeAppIcon(java.lang.String pkg) {
        android.graphics.drawable.Icon monochromeIcon = null;
        try {
            android.graphics.drawable.Drawable appIcon = this.mPackageManager.getApplicationIcon(pkg);
            if ((appIcon instanceof android.graphics.drawable.AdaptiveIconDrawable) && ((android.graphics.drawable.AdaptiveIconDrawable) appIcon).getMonochrome() != null) {
                monochromeIcon = android.graphics.drawable.Icon.createWithResourceAdaptiveDrawable(pkg, ((android.graphics.drawable.AdaptiveIconDrawable) appIcon).getSourceDrawableResId(), true, android.graphics.drawable.AdaptiveIconDrawable.getExtraInsetFraction() * (-2.0f));
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Failed to getApplicationIcon() in getMonochromeAppIcon()", e);
        }
        if (monochromeIcon != null) {
            return monochromeIcon;
        }
        return android.graphics.drawable.Icon.createWithResource(this.mContext, android.R.drawable.ic_menu_emoticons);
    }

    protected static class NotificationAttributes {
        public final int flags;
        public final android.graphics.drawable.Icon icon;
        public final int iconColor;
        public final int visibility;

        public NotificationAttributes(int flags, android.graphics.drawable.Icon icon, int iconColor, int visibility) {
            this.flags = flags;
            this.icon = icon;
            this.iconColor = iconColor;
            this.visibility = visibility;
        }

        public NotificationAttributes(com.android.server.notification.GroupHelper.NotificationAttributes attr) {
            this.flags = attr.flags;
            this.icon = attr.icon;
            this.iconColor = attr.iconColor;
            this.visibility = attr.visibility;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.notification.GroupHelper.NotificationAttributes)) {
                return false;
            }
            com.android.server.notification.GroupHelper.NotificationAttributes that = (com.android.server.notification.GroupHelper.NotificationAttributes) o;
            return this.flags == that.flags && this.iconColor == that.iconColor && this.icon.sameAs(that.icon) && this.visibility == that.visibility;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.flags), java.lang.Integer.valueOf(this.iconColor), this.icon, java.lang.Integer.valueOf(this.visibility));
        }
    }
}
