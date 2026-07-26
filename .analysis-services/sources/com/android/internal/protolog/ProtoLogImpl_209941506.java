package com.android.internal.protolog;

/* JADX INFO: loaded from: classes.dex */
public class ProtoLogImpl_209941506 {
    private static final java.lang.String sLegacyOutputFilePath = "/data/misc/wmtrace/wm_log.winscope";
    private static final java.lang.String sLegacyViewerConfigPath = "/system/etc/protolog.conf.json.gz";
    private static final java.lang.String sViewerConfigPath = "/etc/core.protolog.pb";
    private static com.android.internal.protolog.common.IProtoLog sServiceInstance = null;
    private static final java.util.TreeMap<java.lang.String, com.android.internal.protolog.common.IProtoLogGroup> sLogGroups = new java.util.TreeMap<java.lang.String, com.android.internal.protolog.common.IProtoLogGroup>() { // from class: com.android.internal.protolog.ProtoLogImpl_209941506.1
        {
            put("WM_ERROR", com.android.internal.protolog.ProtoLogGroup.WM_ERROR);
            put("WM_DEBUG_ORIENTATION", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION);
            put("WM_DEBUG_FOCUS_LIGHT", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT);
            put("WM_DEBUG_BOOT", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT);
            put("WM_DEBUG_RESIZE", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE);
            put("WM_DEBUG_ADD_REMOVE", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE);
            put("WM_DEBUG_CONFIGURATION", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION);
            put("WM_DEBUG_SWITCH", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SWITCH);
            put("WM_DEBUG_CONTAINERS", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTAINERS);
            put("WM_DEBUG_FOCUS", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS);
            put("WM_DEBUG_IMMERSIVE", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IMMERSIVE);
            put("WM_DEBUG_LOCKTASK", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK);
            put("WM_DEBUG_STATES", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES);
            put("WM_DEBUG_TASKS", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS);
            put("WM_DEBUG_STARTING_WINDOW", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW);
            put("WM_SHOW_TRANSACTIONS", com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS);
            put("WM_SHOW_SURFACE_ALLOC", com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC);
            put("WM_DEBUG_APP_TRANSITIONS", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS);
            put("WM_DEBUG_ANIM", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM);
            put("WM_DEBUG_APP_TRANSITIONS_ANIM", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM);
            put("WM_DEBUG_RECENTS_ANIMATIONS", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS);
            put("WM_DEBUG_DRAW", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DRAW);
            put("WM_DEBUG_REMOTE_ANIMATIONS", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS);
            put("WM_DEBUG_SCREEN_ON", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON);
            put("WM_DEBUG_KEEP_SCREEN_ON", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON);
            put("WM_DEBUG_WINDOW_MOVEMENT", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT);
            put("WM_DEBUG_IME", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME);
            put("WM_DEBUG_WINDOW_ORGANIZER", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER);
            put("WM_DEBUG_SYNC_ENGINE", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE);
            put("WM_DEBUG_WINDOW_TRANSITIONS", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS);
            put("WM_DEBUG_WINDOW_TRANSITIONS_MIN", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN);
            put("WM_DEBUG_WINDOW_INSETS", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS);
            put("WM_DEBUG_CONTENT_RECORDING", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING);
            put("WM_DEBUG_WALLPAPER", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER);
            put("WM_DEBUG_BACK_PREVIEW", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW);
            put("WM_DEBUG_DREAM", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DREAM);
            put("WM_DEBUG_DIMMER", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER);
            put("WM_DEBUG_TPL", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL);
            put("WM_DEBUG_EMBEDDED_WINDOWS", com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS);
            put("TEST_GROUP", com.android.internal.protolog.ProtoLogGroup.TEST_GROUP);
        }
    };
    private static final java.lang.Runnable sCacheUpdater = new java.lang.Runnable() { // from class: com.android.internal.protolog.ProtoLogImpl_209941506$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            com.android.internal.protolog.ProtoLogImpl_209941506.Cache.update();
        }
    };

    public static void d(com.android.internal.protolog.common.IProtoLogGroup group, long messageHash, int paramsMask, java.lang.String messageString, java.lang.Object... args) {
        getSingleInstance().log(com.android.internal.protolog.common.LogLevel.DEBUG, group, messageHash, paramsMask, messageString, args);
    }

    public static void v(com.android.internal.protolog.common.IProtoLogGroup group, long messageHash, int paramsMask, java.lang.String messageString, java.lang.Object... args) {
        getSingleInstance().log(com.android.internal.protolog.common.LogLevel.VERBOSE, group, messageHash, paramsMask, messageString, args);
    }

    public static void i(com.android.internal.protolog.common.IProtoLogGroup group, long messageHash, int paramsMask, java.lang.String messageString, java.lang.Object... args) {
        getSingleInstance().log(com.android.internal.protolog.common.LogLevel.INFO, group, messageHash, paramsMask, messageString, args);
    }

    public static void w(com.android.internal.protolog.common.IProtoLogGroup group, long messageHash, int paramsMask, java.lang.String messageString, java.lang.Object... args) {
        getSingleInstance().log(com.android.internal.protolog.common.LogLevel.WARN, group, messageHash, paramsMask, messageString, args);
    }

    public static void e(com.android.internal.protolog.common.IProtoLogGroup group, long messageHash, int paramsMask, java.lang.String messageString, java.lang.Object... args) {
        getSingleInstance().log(com.android.internal.protolog.common.LogLevel.ERROR, group, messageHash, paramsMask, messageString, args);
    }

    public static void wtf(com.android.internal.protolog.common.IProtoLogGroup group, long messageHash, int paramsMask, java.lang.String messageString, java.lang.Object... args) {
        getSingleInstance().log(com.android.internal.protolog.common.LogLevel.WTF, group, messageHash, paramsMask, messageString, args);
    }

    public static boolean isEnabled(com.android.internal.protolog.common.IProtoLogGroup group, com.android.internal.protolog.common.LogLevel level) {
        return getSingleInstance().isEnabled(group, level);
    }

    public static synchronized com.android.internal.protolog.common.IProtoLog getSingleInstance() {
        if (sServiceInstance == null) {
            if (android.tracing.Flags.perfettoProtologTracing()) {
                sServiceInstance = new com.android.internal.protolog.PerfettoProtoLogImpl(sViewerConfigPath, sLogGroups, sCacheUpdater);
            } else {
                sServiceInstance = new com.android.internal.protolog.LegacyProtoLogImpl(sLegacyOutputFilePath, sLegacyViewerConfigPath, sLogGroups, sCacheUpdater);
            }
            sCacheUpdater.run();
        }
        return sServiceInstance;
    }

    public static synchronized void setSingleInstance(com.android.internal.protolog.common.IProtoLog instance) {
        sServiceInstance = instance;
    }

    public static class Cache {
        public static boolean[] WM_ERROR_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_ORIENTATION_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_FOCUS_LIGHT_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_BOOT_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_RESIZE_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_ADD_REMOVE_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_CONFIGURATION_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_SWITCH_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_CONTAINERS_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_FOCUS_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_IMMERSIVE_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_LOCKTASK_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_STATES_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_TASKS_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_STARTING_WINDOW_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_SHOW_TRANSACTIONS_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_SHOW_SURFACE_ALLOC_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_APP_TRANSITIONS_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_ANIM_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_APP_TRANSITIONS_ANIM_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_RECENTS_ANIMATIONS_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_DRAW_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_REMOTE_ANIMATIONS_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_SCREEN_ON_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_KEEP_SCREEN_ON_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_WINDOW_MOVEMENT_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_IME_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_WINDOW_ORGANIZER_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_SYNC_ENGINE_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_WINDOW_TRANSITIONS_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_WINDOW_INSETS_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_CONTENT_RECORDING_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_WALLPAPER_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_BACK_PREVIEW_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_DREAM_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_DIMMER_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_TPL_enabled = {true, true, true, true, true, true};
        public static boolean[] WM_DEBUG_EMBEDDED_WINDOWS_enabled = {true, true, true, true, true, true};
        public static boolean[] TEST_GROUP_enabled = {true, true, true, true, true, true};

        /* JADX INFO: Access modifiers changed from: private */
        public static void update() {
            WM_ERROR_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_ERROR_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_ERROR_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, com.android.internal.protolog.common.LogLevel.INFO);
            WM_ERROR_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, com.android.internal.protolog.common.LogLevel.WARN);
            WM_ERROR_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_ERROR_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_ORIENTATION_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_ORIENTATION_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_ORIENTATION_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_ORIENTATION_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_ORIENTATION_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_ORIENTATION_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_FOCUS_LIGHT_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_FOCUS_LIGHT_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_FOCUS_LIGHT_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_FOCUS_LIGHT_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_FOCUS_LIGHT_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_FOCUS_LIGHT_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_BOOT_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_BOOT_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_BOOT_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_BOOT_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_BOOT_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_BOOT_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_RESIZE_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_RESIZE_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_RESIZE_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_RESIZE_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_RESIZE_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_RESIZE_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_ADD_REMOVE_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_ADD_REMOVE_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_ADD_REMOVE_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_ADD_REMOVE_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_ADD_REMOVE_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_ADD_REMOVE_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_CONFIGURATION_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_CONFIGURATION_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_CONFIGURATION_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_CONFIGURATION_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_CONFIGURATION_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_CONFIGURATION_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_SWITCH_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SWITCH, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_SWITCH_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SWITCH, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_SWITCH_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SWITCH, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_SWITCH_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SWITCH, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_SWITCH_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SWITCH, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_SWITCH_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SWITCH, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_CONTAINERS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTAINERS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_CONTAINERS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTAINERS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_CONTAINERS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTAINERS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_CONTAINERS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTAINERS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_CONTAINERS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTAINERS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_CONTAINERS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTAINERS, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_FOCUS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_FOCUS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_FOCUS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_FOCUS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_FOCUS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_FOCUS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_IMMERSIVE_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IMMERSIVE, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_IMMERSIVE_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IMMERSIVE, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_IMMERSIVE_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IMMERSIVE, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_IMMERSIVE_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IMMERSIVE, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_IMMERSIVE_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IMMERSIVE, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_IMMERSIVE_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IMMERSIVE, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_LOCKTASK_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_LOCKTASK_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_LOCKTASK_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_LOCKTASK_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_LOCKTASK_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_LOCKTASK_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_STATES_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_STATES_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_STATES_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_STATES_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_STATES_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_STATES_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_TASKS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_TASKS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_TASKS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_TASKS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_TASKS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_TASKS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_STARTING_WINDOW_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_STARTING_WINDOW_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_STARTING_WINDOW_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_STARTING_WINDOW_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_STARTING_WINDOW_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_STARTING_WINDOW_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, com.android.internal.protolog.common.LogLevel.WTF);
            WM_SHOW_TRANSACTIONS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_SHOW_TRANSACTIONS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_SHOW_TRANSACTIONS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_SHOW_TRANSACTIONS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_SHOW_TRANSACTIONS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_SHOW_TRANSACTIONS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, com.android.internal.protolog.common.LogLevel.WTF);
            WM_SHOW_SURFACE_ALLOC_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_SHOW_SURFACE_ALLOC_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_SHOW_SURFACE_ALLOC_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, com.android.internal.protolog.common.LogLevel.INFO);
            WM_SHOW_SURFACE_ALLOC_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, com.android.internal.protolog.common.LogLevel.WARN);
            WM_SHOW_SURFACE_ALLOC_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_SHOW_SURFACE_ALLOC_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_APP_TRANSITIONS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_APP_TRANSITIONS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_APP_TRANSITIONS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_APP_TRANSITIONS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_APP_TRANSITIONS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_APP_TRANSITIONS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_ANIM_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_ANIM_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_ANIM_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_ANIM_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_ANIM_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_ANIM_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_RECENTS_ANIMATIONS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_RECENTS_ANIMATIONS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_RECENTS_ANIMATIONS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_RECENTS_ANIMATIONS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_RECENTS_ANIMATIONS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_RECENTS_ANIMATIONS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_DRAW_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DRAW, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_DRAW_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DRAW, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_DRAW_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DRAW, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_DRAW_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DRAW, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_DRAW_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DRAW, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_DRAW_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DRAW, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_REMOTE_ANIMATIONS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_REMOTE_ANIMATIONS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_REMOTE_ANIMATIONS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_REMOTE_ANIMATIONS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_REMOTE_ANIMATIONS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_REMOTE_ANIMATIONS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_SCREEN_ON_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_SCREEN_ON_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_SCREEN_ON_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_SCREEN_ON_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_SCREEN_ON_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_SCREEN_ON_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_KEEP_SCREEN_ON_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_KEEP_SCREEN_ON_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_KEEP_SCREEN_ON_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_KEEP_SCREEN_ON_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_KEEP_SCREEN_ON_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_KEEP_SCREEN_ON_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_WINDOW_MOVEMENT_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_WINDOW_MOVEMENT_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_WINDOW_MOVEMENT_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_WINDOW_MOVEMENT_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_WINDOW_MOVEMENT_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_WINDOW_MOVEMENT_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_IME_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_IME_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_IME_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_IME_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_IME_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_IME_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_WINDOW_ORGANIZER_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_WINDOW_ORGANIZER_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_WINDOW_ORGANIZER_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_WINDOW_ORGANIZER_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_WINDOW_ORGANIZER_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_WINDOW_ORGANIZER_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_SYNC_ENGINE_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_SYNC_ENGINE_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_SYNC_ENGINE_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_SYNC_ENGINE_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_SYNC_ENGINE_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_SYNC_ENGINE_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_WINDOW_TRANSITIONS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_WINDOW_TRANSITIONS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_WINDOW_TRANSITIONS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_WINDOW_TRANSITIONS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_WINDOW_TRANSITIONS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_WINDOW_TRANSITIONS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_WINDOW_INSETS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_WINDOW_INSETS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_WINDOW_INSETS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_WINDOW_INSETS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_WINDOW_INSETS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_WINDOW_INSETS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_CONTENT_RECORDING_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_CONTENT_RECORDING_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_CONTENT_RECORDING_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_CONTENT_RECORDING_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_CONTENT_RECORDING_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_CONTENT_RECORDING_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_WALLPAPER_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_WALLPAPER_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_WALLPAPER_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_WALLPAPER_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_WALLPAPER_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_WALLPAPER_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_BACK_PREVIEW_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_BACK_PREVIEW_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_BACK_PREVIEW_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_BACK_PREVIEW_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_BACK_PREVIEW_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_BACK_PREVIEW_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_DREAM_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DREAM, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_DREAM_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DREAM, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_DREAM_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DREAM, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_DREAM_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DREAM, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_DREAM_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DREAM, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_DREAM_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DREAM, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_DIMMER_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_DIMMER_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_DIMMER_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_DIMMER_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_DIMMER_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_DIMMER_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_TPL_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_TPL_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_TPL_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_TPL_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_TPL_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_TPL_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, com.android.internal.protolog.common.LogLevel.WTF);
            WM_DEBUG_EMBEDDED_WINDOWS_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, com.android.internal.protolog.common.LogLevel.DEBUG);
            WM_DEBUG_EMBEDDED_WINDOWS_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, com.android.internal.protolog.common.LogLevel.VERBOSE);
            WM_DEBUG_EMBEDDED_WINDOWS_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, com.android.internal.protolog.common.LogLevel.INFO);
            WM_DEBUG_EMBEDDED_WINDOWS_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, com.android.internal.protolog.common.LogLevel.WARN);
            WM_DEBUG_EMBEDDED_WINDOWS_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, com.android.internal.protolog.common.LogLevel.ERROR);
            WM_DEBUG_EMBEDDED_WINDOWS_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, com.android.internal.protolog.common.LogLevel.WTF);
            TEST_GROUP_enabled[0] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.TEST_GROUP, com.android.internal.protolog.common.LogLevel.DEBUG);
            TEST_GROUP_enabled[1] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.TEST_GROUP, com.android.internal.protolog.common.LogLevel.VERBOSE);
            TEST_GROUP_enabled[2] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.TEST_GROUP, com.android.internal.protolog.common.LogLevel.INFO);
            TEST_GROUP_enabled[3] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.TEST_GROUP, com.android.internal.protolog.common.LogLevel.WARN);
            TEST_GROUP_enabled[4] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.TEST_GROUP, com.android.internal.protolog.common.LogLevel.ERROR);
            TEST_GROUP_enabled[5] = com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.TEST_GROUP, com.android.internal.protolog.common.LogLevel.WTF);
        }
    }
}
