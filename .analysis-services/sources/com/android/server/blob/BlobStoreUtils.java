package com.android.server.blob;

/* JADX INFO: loaded from: classes.dex */
class BlobStoreUtils {
    private static final java.lang.String DESC_RES_TYPE_STRING = "string";
    private static final java.lang.Object sLock = new java.lang.Object();
    private static android.os.Handler sRevocableFdHandler;

    BlobStoreUtils() {
    }

    static android.content.res.Resources getPackageResources(android.content.Context context, java.lang.String packageName, int userId) {
        try {
            return context.createContextAsUser(android.os.UserHandle.of(userId), 0).getPackageManager().getResourcesForApplication(packageName);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.d(com.android.server.blob.BlobStoreConfig.TAG, "Unknown package in user " + userId + ": " + packageName, e);
            return null;
        }
    }

    static int getDescriptionResourceId(android.content.res.Resources resources, java.lang.String resourceEntryName, java.lang.String packageName) {
        return resources.getIdentifier(resourceEntryName, DESC_RES_TYPE_STRING, packageName);
    }

    static int getDescriptionResourceId(android.content.Context context, java.lang.String resourceEntryName, java.lang.String packageName, int userId) {
        android.content.res.Resources resources = getPackageResources(context, packageName, userId);
        if (resources == null) {
            return 0;
        }
        return getDescriptionResourceId(resources, resourceEntryName, packageName);
    }

    static java.lang.String formatTime(long timeMs) {
        return android.text.format.TimeMigrationUtils.formatMillisWithFixedFormat(timeMs);
    }

    static android.os.Handler getRevocableFdHandler() {
        synchronized (sLock) {
            if (sRevocableFdHandler != null) {
                return sRevocableFdHandler;
            }
            android.os.HandlerThread t = new android.os.HandlerThread("BlobFuseLooper");
            t.start();
            sRevocableFdHandler = new android.os.Handler(t.getLooper());
            return sRevocableFdHandler;
        }
    }
}
