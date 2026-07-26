package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ShortcutParser {
    private static final boolean DEBUG;
    static final java.lang.String METADATA_KEY = "android.app.shortcuts";
    private static final java.lang.String TAG = "ShortcutService";
    private static final java.lang.String TAG_CATEGORIES = "categories";
    private static final java.lang.String TAG_CATEGORY = "category";
    private static final java.lang.String TAG_DATA = "data";
    private static final java.lang.String TAG_INTENT = "intent";
    private static final java.lang.String TAG_SHARE_TARGET = "share-target";
    private static final java.lang.String TAG_SHORTCUT = "shortcut";
    private static final java.lang.String TAG_SHORTCUTS = "shortcuts";

    static {
        DEBUG = com.android.server.pm.ShortcutService.DEBUG;
    }

    public static java.util.List<android.content.pm.ShortcutInfo> parseShortcuts(com.android.server.pm.ShortcutService service, java.lang.String packageName, int userId, java.util.List<com.android.server.pm.ShareTargetInfo> outShareTargets) throws java.lang.Throwable {
        if (com.android.server.pm.ShortcutService.DEBUG) {
            android.util.Slog.d(TAG, java.lang.String.format("Scanning package %s for manifest shortcuts on user %d", packageName, java.lang.Integer.valueOf(userId)));
        }
        java.util.List<android.content.pm.ResolveInfo> activities = service.injectGetMainActivities(packageName, userId);
        if (activities == null || activities.size() == 0) {
            return null;
        }
        outShareTargets.clear();
        try {
            int size = activities.size();
            java.util.List<android.content.pm.ShortcutInfo> result = null;
            for (int i = 0; i < size; i++) {
                try {
                    android.content.pm.ActivityInfo activityInfoNoMetadata = activities.get(i).activityInfo;
                    if (activityInfoNoMetadata != null) {
                        try {
                            android.content.pm.ActivityInfo activityInfoWithMetadata = service.getActivityInfoWithMetadata(activityInfoNoMetadata.getComponentName(), userId);
                            if (activityInfoWithMetadata != null) {
                                result = parseShortcutsOneFile(service, activityInfoWithMetadata, packageName, userId, result, outShareTargets);
                            }
                        } catch (java.lang.RuntimeException e) {
                            e = e;
                            service.wtf("Exception caught while parsing shortcut XML for package=" + packageName, e);
                            return null;
                        }
                    }
                } catch (java.lang.RuntimeException e2) {
                    e = e2;
                }
            }
            return result;
        } catch (java.lang.RuntimeException e3) {
            e = e3;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:199:0x04d3, code lost:
    
        if (r0 == null) goto L201;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x04d5, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x04d8, code lost:
    
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00e3, code lost:
    
        android.util.Log.e(com.android.server.pm.ShortcutParser.TAG, "More than " + r14 + " shortcuts found for " + r27.getComponentName() + ". Skipping the rest.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x010e, code lost:
    
        if (r0 == null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0110, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0113, code lost:
    
        return r5;
     */
    /* JADX WARN: Removed duplicated region for block: B:265:0x025d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0265  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.util.List<android.content.pm.ShortcutInfo> parseShortcutsOneFile(com.android.server.pm.ShortcutService r26, android.content.pm.ActivityInfo r27, java.lang.String r28, int r29, java.util.List<android.content.pm.ShortcutInfo> r30, java.util.List<com.android.server.pm.ShareTargetInfo> r31) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1267
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutParser.parseShortcutsOneFile(com.android.server.pm.ShortcutService, android.content.pm.ActivityInfo, java.lang.String, int, java.util.List, java.util.List):java.util.List");
    }

    private static java.lang.String parseCategories(com.android.server.pm.ShortcutService service, android.util.AttributeSet attrs) {
        android.content.res.TypedArray sa = service.mContext.getResources().obtainAttributes(attrs, com.android.internal.R.styleable.ShortcutCategories);
        try {
            if (sa.getType(0) == 3) {
                return sa.getNonResourceString(0);
            }
            android.util.Log.w(TAG, "android:name for shortcut category must be string literal.");
            sa.recycle();
            return null;
        } finally {
            sa.recycle();
        }
    }

    private static android.content.pm.ShortcutInfo parseShortcutAttributes(com.android.server.pm.ShortcutService service, android.util.AttributeSet attrs, java.lang.String packageName, android.content.ComponentName activity, int userId, int rank) throws java.lang.Throwable {
        android.content.res.TypedArray sa;
        android.content.res.TypedArray sa2 = service.mContext.getResources().obtainAttributes(attrs, com.android.internal.R.styleable.Shortcut);
        try {
            try {
                if (sa2.getType(2) != 3) {
                    android.util.Log.w(TAG, "android:shortcutId must be string literal. activity=" + activity);
                    sa2.recycle();
                    return null;
                }
                java.lang.String id = sa2.getNonResourceString(2);
                boolean enabled = sa2.getBoolean(1, true);
                int iconResId = sa2.getResourceId(0, 0);
                int titleResId = sa2.getResourceId(3, 0);
                int textResId = sa2.getResourceId(4, 0);
                int disabledMessageResId = sa2.getResourceId(5, 0);
                int splashScreenThemeResId = sa2.getResourceId(6, 0);
                java.lang.String splashScreenThemeResName = splashScreenThemeResId != 0 ? service.mContext.getResources().getResourceName(splashScreenThemeResId) : null;
                if (android.text.TextUtils.isEmpty(id)) {
                    android.util.Log.w(TAG, "android:shortcutId must be provided. activity=" + activity);
                    sa2.recycle();
                    return null;
                }
                if (titleResId == 0) {
                    android.util.Log.w(TAG, "android:shortcutShortLabel must be provided. activity=" + activity);
                    sa2.recycle();
                    return null;
                }
                sa = sa2;
                try {
                    android.content.pm.ShortcutInfo shortcutInfoCreateShortcutFromManifest = createShortcutFromManifest(service, userId, id, packageName, activity, titleResId, textResId, disabledMessageResId, rank, iconResId, enabled, splashScreenThemeResName);
                    sa.recycle();
                    return shortcutInfoCreateShortcutFromManifest;
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                sa = sa2;
            }
        } catch (java.lang.Throwable th3) {
            th = th3;
            sa = sa2;
        }
        sa.recycle();
        throw th;
    }

    private static android.content.pm.ShortcutInfo createShortcutFromManifest(com.android.server.pm.ShortcutService service, int userId, java.lang.String id, java.lang.String packageName, android.content.ComponentName activityComponent, int titleResId, int textResId, int disabledMessageResId, int rank, int iconResId, boolean enabled, java.lang.String splashScreenThemeResName) {
        int disabledReason;
        int flags = (enabled ? 32 : 64) | 256 | (iconResId != 0 ? 4 : 0);
        if (enabled) {
            disabledReason = 0;
        } else {
            disabledReason = 1;
        }
        return new android.content.pm.ShortcutInfo(userId, id, packageName, activityComponent, null, null, titleResId, null, null, textResId, null, null, disabledMessageResId, null, null, null, rank, null, service.injectCurrentTimeMillis(), flags, iconResId, null, null, null, disabledReason, null, null, splashScreenThemeResName, null);
    }

    private static java.lang.String parseCategory(com.android.server.pm.ShortcutService service, android.util.AttributeSet attrs) {
        android.content.res.TypedArray sa = service.mContext.getResources().obtainAttributes(attrs, com.android.internal.R.styleable.IntentCategory);
        try {
            if (sa.getType(0) != 3) {
                android.util.Log.w(TAG, "android:name must be string literal.");
                sa.recycle();
                return null;
            }
            return sa.getString(0);
        } finally {
            sa.recycle();
        }
    }

    private static com.android.server.pm.ShareTargetInfo parseShareTargetAttributes(com.android.server.pm.ShortcutService service, android.util.AttributeSet attrs) {
        android.content.res.TypedArray sa = service.mContext.getResources().obtainAttributes(attrs, com.android.internal.R.styleable.Intent);
        try {
            java.lang.String targetClass = sa.getString(4);
            if (android.text.TextUtils.isEmpty(targetClass)) {
                android.util.Log.w(TAG, "android:targetClass must be provided.");
                return null;
            }
            return new com.android.server.pm.ShareTargetInfo(null, targetClass, null);
        } finally {
            sa.recycle();
        }
    }

    private static com.android.server.pm.ShareTargetInfo.TargetData parseShareTargetData(com.android.server.pm.ShortcutService service, android.util.AttributeSet attrs) {
        android.content.res.TypedArray sa = service.mContext.getResources().obtainAttributes(attrs, com.android.internal.R.styleable.AndroidManifestData);
        try {
            if (sa.getType(0) != 3) {
                android.util.Log.w(TAG, "android:mimeType must be string literal.");
                sa.recycle();
                return null;
            }
            java.lang.String scheme = sa.getString(1);
            java.lang.String host = sa.getString(2);
            java.lang.String port = sa.getString(3);
            java.lang.String path = sa.getString(4);
            java.lang.String pathPattern = sa.getString(6);
            java.lang.String pathPrefix = sa.getString(5);
            java.lang.String mimeType = sa.getString(0);
            return new com.android.server.pm.ShareTargetInfo.TargetData(scheme, host, port, path, pathPattern, pathPrefix, mimeType);
        } finally {
            sa.recycle();
        }
    }
}
