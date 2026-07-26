package com.android.server.textclassifier;

/* JADX INFO: loaded from: classes3.dex */
public final class IconsUriHelper {
    public static final java.lang.String AUTHORITY = "com.android.textclassifier.icons";
    private static final java.lang.String TAG = "IconsUriHelper";
    private final java.util.function.Supplier<java.lang.String> mIdSupplier;
    private final java.util.Map<java.lang.String, java.lang.String> mPackageIds = new android.util.ArrayMap();
    private static final java.util.function.Supplier<java.lang.String> DEFAULT_ID_SUPPLIER = new java.util.function.Supplier() { // from class: com.android.server.textclassifier.IconsUriHelper$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final java.lang.Object get() {
            return java.util.UUID.randomUUID().toString();
        }
    };
    private static final com.android.server.textclassifier.IconsUriHelper sSingleton = new com.android.server.textclassifier.IconsUriHelper(null);

    private IconsUriHelper(java.util.function.Supplier<java.lang.String> idSupplier) {
        this.mIdSupplier = idSupplier != null ? idSupplier : DEFAULT_ID_SUPPLIER;
        this.mPackageIds.put(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
    }

    public static com.android.server.textclassifier.IconsUriHelper newInstanceForTesting(java.util.function.Supplier<java.lang.String> idSupplier) {
        return new com.android.server.textclassifier.IconsUriHelper(idSupplier);
    }

    static com.android.server.textclassifier.IconsUriHelper getInstance() {
        return sSingleton;
    }

    public android.net.Uri getContentUri(java.lang.String packageName, int resId) {
        android.net.Uri uriBuild;
        java.util.Objects.requireNonNull(packageName);
        synchronized (this.mPackageIds) {
            if (!this.mPackageIds.containsKey(packageName)) {
                this.mPackageIds.put(packageName, this.mIdSupplier.get());
            }
            uriBuild = new android.net.Uri.Builder().scheme(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT).authority(AUTHORITY).path(this.mPackageIds.get(packageName)).appendPath(java.lang.Integer.toString(resId)).build();
        }
        return uriBuild;
    }

    public com.android.server.textclassifier.IconsUriHelper.ResourceInfo getResourceInfo(android.net.Uri uri) {
        if (!com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme()) || !AUTHORITY.equals(uri.getAuthority())) {
            return null;
        }
        java.util.List<java.lang.String> pathItems = uri.getPathSegments();
        try {
        } catch (java.lang.Exception e) {
            android.util.Log.v(TAG, "Could not get resource info. Reason: " + e.getMessage());
        }
        synchronized (this.mPackageIds) {
            java.lang.String packageId = pathItems.get(0);
            int resId = java.lang.Integer.parseInt(pathItems.get(1));
            for (java.lang.String packageName : this.mPackageIds.keySet()) {
                if (packageId.equals(this.mPackageIds.get(packageName))) {
                    return new com.android.server.textclassifier.IconsUriHelper.ResourceInfo(packageName, resId);
                }
            }
            return null;
        }
    }

    public static final class ResourceInfo {
        public final int id;
        public final java.lang.String packageName;

        private ResourceInfo(java.lang.String packageName, int id) {
            this.packageName = (java.lang.String) java.util.Objects.requireNonNull(packageName);
            this.id = id;
        }
    }
}
