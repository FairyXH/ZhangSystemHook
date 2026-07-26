package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
final class GameClassifierImpl implements com.android.server.app.GameClassifier {
    private final android.content.pm.PackageManager mPackageManager;

    GameClassifierImpl(android.content.pm.PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    @Override // com.android.server.app.GameClassifier
    public boolean isGame(java.lang.String packageName, android.os.UserHandle userHandle) {
        try {
            int applicationCategory = this.mPackageManager.getApplicationInfoAsUser(packageName, 0, userHandle.getIdentifier()).category;
            return applicationCategory == 0;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
