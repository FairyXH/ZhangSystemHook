package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class OplusPairTaskManager {
    public static final boolean DEBUG = true;
    public static final com.android.server.wm.OplusPairTaskManager EMPTY = new com.android.server.wm.OplusPairTaskManager();
    private static final boolean PAIR_TASK_ENABLED = android.os.SystemProperties.getBoolean("persist.sys.pair_split_feature_enable", true);

    public static boolean isPairTaskEnabled() {
        return PAIR_TASK_ENABLED;
    }

    public static com.android.server.wm.OplusPairTaskManager getInstance(com.android.server.wm.ActivityTaskManagerService atms) {
        if (isPairTaskEnabled()) {
            try {
                com.android.server.wm.OplusPairTaskManager instance = (com.android.server.wm.OplusPairTaskManager) java.lang.Class.forName("com.android.server.wm.OplusPairTaskManagerImpl").getDeclaredConstructor(com.android.server.wm.ActivityTaskManagerService.class).newInstance(atms);
                return instance;
            } catch (java.lang.Exception e) {
                com.android.server.wm.OplusPairTaskManager instance2 = EMPTY;
                return instance2;
            }
        }
        com.android.server.wm.OplusPairTaskManager instance3 = EMPTY;
        return instance3;
    }

    public static android.os.Bundle prepareOptionsBeforeStartShortcut(android.os.Bundle options, android.content.pm.ShortcutServiceInternal shortcutService, java.lang.String shortcutId, int callingUserId, java.lang.String callingPackage, java.lang.String packageName, int targetUserId, int callingPid, int callingUid) {
        android.content.pm.ShortcutInfo si;
        android.os.Bundle options2;
        if (!isPairTaskEnabled()) {
            return options;
        }
        java.util.ArrayList<java.lang.String> shortcutIds = new java.util.ArrayList<>();
        shortcutIds.add(shortcutId);
        java.util.List<android.content.pm.ShortcutInfo> list = shortcutService.getShortcuts(callingUserId, callingPackage, 0L, packageName, shortcutIds, (java.util.List) null, (android.content.ComponentName) null, 1026, targetUserId, callingPid, callingUid);
        if (list != null && list.size() > 0 && (si = list.get(0)) != null && si.getExtras() != null) {
            if (options != null) {
                options2 = options;
            } else {
                options2 = new android.os.Bundle();
            }
            options2.putAll(si.getExtras());
            return options2;
        }
        return options;
    }

    public static boolean isSplitScreenCombination(android.content.pm.ShortcutInfo shortcutInfo) {
        if (!isPairTaskEnabled()) {
            return false;
        }
        return isSplitScreenCombination(new android.os.Bundle(shortcutInfo.getExtras()));
    }

    public static boolean isSplitScreenCombination(android.os.Bundle extras) {
        if (extras != null) {
            return extras.getBoolean("isSplitScreenCombination", false) || extras.getBoolean("isPsSplitScreenCombination", false);
        }
        return false;
    }
}
