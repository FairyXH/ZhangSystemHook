package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class SystemBackupAgent extends android.app.backup.BackupAgentHelper {
    private static final java.lang.String PEOPLE_HELPER = "people";
    private static final java.lang.String SLICES_HELPER = "slices";
    private static final java.lang.String TAG = "SystemBackupAgent";
    private static final java.lang.String WALLPAPER_HELPER = "wallpaper";
    private static final java.lang.String WALLPAPER_IMAGE_FILENAME = "wallpaper";
    private static final java.lang.String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";
    private android.app.backup.BackupRestoreEventLogger mLogger;
    private static final java.lang.String WALLPAPER_IMAGE_DIR = android.os.Environment.getUserSystemDirectory(0).getAbsolutePath();
    public static final java.lang.String WALLPAPER_IMAGE = new java.io.File(android.os.Environment.getUserSystemDirectory(0), com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER).getAbsolutePath();
    private static final java.lang.String WALLPAPER_INFO_DIR = android.os.Environment.getUserSystemDirectory(0).getAbsolutePath();
    private static final java.lang.String WALLPAPER_INFO_FILENAME = "wallpaper_info.xml";
    public static final java.lang.String WALLPAPER_INFO = new java.io.File(android.os.Environment.getUserSystemDirectory(0), WALLPAPER_INFO_FILENAME).getAbsolutePath();
    private static final java.lang.String PERMISSION_HELPER = "permissions";
    private static final java.lang.String NOTIFICATION_HELPER = "notifications";
    private static final java.lang.String SYNC_SETTINGS_HELPER = "account_sync_settings";
    private static final java.lang.String APP_LOCALES_HELPER = "app_locales";
    private static final java.lang.String COMPANION_HELPER = "companion";
    private static final java.lang.String APP_GENDER_HELPER = "app_gender";
    private static final java.lang.String SYSTEM_GENDER_HELPER = "system_gender";
    private static final java.util.Set<java.lang.String> sEligibleHelpersForProfileUser = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{PERMISSION_HELPER, NOTIFICATION_HELPER, SYNC_SETTINGS_HELPER, APP_LOCALES_HELPER, COMPANION_HELPER, APP_GENDER_HELPER, SYSTEM_GENDER_HELPER});
    private static final java.lang.String ACCOUNT_MANAGER_HELPER = "account_manager";
    private static final java.lang.String USAGE_STATS_HELPER = "usage_stats";
    private static final java.lang.String PREFERRED_HELPER = "preferred_activities";
    private static final java.lang.String SHORTCUT_MANAGER_HELPER = "shortcut_manager";
    private static final java.util.Set<java.lang.String> sEligibleHelpersForNonSystemUser = com.android.server.backup.SetUtils.union(sEligibleHelpersForProfileUser, com.google.android.collect.Sets.newArraySet(new java.lang.String[]{ACCOUNT_MANAGER_HELPER, USAGE_STATS_HELPER, PREFERRED_HELPER, SHORTCUT_MANAGER_HELPER}));
    private int mUserId = 0;
    private boolean mIsProfileUser = false;

    public void onCreate(android.os.UserHandle user, int backupDestination) {
        super.onCreate(user, backupDestination);
        this.mLogger = getBackupRestoreEventLogger();
        this.mUserId = user.getIdentifier();
        if (this.mUserId != 0) {
            android.content.Context context = createContextAsUser(user, 0);
            android.os.UserManager userManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
            this.mIsProfileUser = userManager.isProfile();
        }
        addHelperIfEligibleForUser(SYNC_SETTINGS_HELPER, new com.android.server.backup.AccountSyncSettingsBackupHelper(this, this.mUserId));
        addHelperIfEligibleForUser(PREFERRED_HELPER, new com.android.server.backup.PreferredActivityBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(NOTIFICATION_HELPER, new com.android.server.backup.NotificationBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(PERMISSION_HELPER, new com.android.server.backup.PermissionBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(USAGE_STATS_HELPER, new com.android.server.backup.UsageStatsBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(SHORTCUT_MANAGER_HELPER, new com.android.server.backup.ShortcutBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(ACCOUNT_MANAGER_HELPER, new com.android.server.backup.AccountManagerBackupHelper(this.mUserId));
        if (!getPackageManager().hasSystemFeature("android.software.slices_disabled")) {
            addHelperIfEligibleForUser(SLICES_HELPER, new com.android.server.backup.SliceBackupHelper(this));
        }
        addHelperIfEligibleForUser(PEOPLE_HELPER, new com.android.server.backup.PeopleBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(APP_LOCALES_HELPER, new com.android.server.backup.AppSpecificLocalesBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(APP_GENDER_HELPER, new com.android.server.backup.AppGrammaticalGenderBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(COMPANION_HELPER, new com.android.server.backup.CompanionBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(SYSTEM_GENDER_HELPER, new com.android.server.backup.SystemGrammaticalGenderBackupHelper(this.mUserId));
    }

    @Override // android.app.backup.BackupAgent
    public void onFullBackup(android.app.backup.FullBackupDataOutput data) throws java.io.IOException {
    }

    @Override // android.app.backup.BackupAgentHelper, android.app.backup.BackupAgent
    public void onRestore(android.app.backup.BackupDataInput data, int appVersionCode, android.os.ParcelFileDescriptor newState) throws java.io.IOException {
        addHelper(com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER, new android.app.backup.WallpaperBackupHelper(this, new java.lang.String[]{WALLPAPER_IMAGE_KEY}));
        addHelper("system_files", new android.app.backup.WallpaperBackupHelper(this, new java.lang.String[]{WALLPAPER_IMAGE_KEY}));
        super.onRestore(data, appVersionCode, newState);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0059  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onRestoreFile(android.os.ParcelFileDescriptor r17, long r18, int r20, java.lang.String r21, java.lang.String r22, long r23, long r25) throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 211
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.SystemBackupAgent.onRestoreFile(android.os.ParcelFileDescriptor, long, int, java.lang.String, java.lang.String, long, long):void");
    }

    private void addHelperIfEligibleForUser(java.lang.String keyPrefix, android.app.backup.BackupHelperWithLogger helper) {
        if (isHelperEligibleForUser(keyPrefix)) {
            addHelper(keyPrefix, helper);
            if (com.android.server.backup.Flags.enableMetricsSystemBackupAgents()) {
                helper.setLogger(this.mLogger);
            }
        }
    }

    private boolean isHelperEligibleForUser(java.lang.String keyPrefix) {
        if (this.mUserId == 0) {
            return true;
        }
        if (this.mIsProfileUser) {
            return sEligibleHelpersForProfileUser.contains(keyPrefix);
        }
        return sEligibleHelpersForNonSystemUser.contains(keyPrefix);
    }
}
