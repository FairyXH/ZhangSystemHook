package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class UserBackupPreferences {
    private static final java.lang.String PREFERENCES_FILE = "backup_preferences";
    private final android.content.SharedPreferences.Editor mEditor;
    private final android.content.SharedPreferences mPreferences;

    UserBackupPreferences(android.content.Context conext, java.io.File storageDir) {
        java.io.File excludedKeysFile = new java.io.File(storageDir, PREFERENCES_FILE);
        this.mPreferences = conext.getSharedPreferences(excludedKeysFile, 0);
        this.mEditor = this.mPreferences.edit();
    }

    void addExcludedKeys(java.lang.String packageName, java.util.List<java.lang.String> keys) {
        java.util.Set<java.lang.String> existingKeys = new java.util.HashSet<>(this.mPreferences.getStringSet(packageName, java.util.Collections.emptySet()));
        existingKeys.addAll(keys);
        this.mEditor.putStringSet(packageName, existingKeys);
        this.mEditor.commit();
    }

    java.util.Set<java.lang.String> getExcludedRestoreKeysForPackage(java.lang.String packageName) {
        return this.mPreferences.getStringSet(packageName, java.util.Collections.emptySet());
    }
}
