package com.android.server.companion.utils;

/* JADX INFO: loaded from: classes.dex */
public final class PackageUtils {
    private static final android.content.Intent COMPANION_SERVICE_INTENT = new android.content.Intent("android.companion.CompanionDeviceService");
    private static final java.lang.String PROPERTY_PRIMARY_TAG = "android.companion.PROPERTY_PRIMARY_COMPANION_DEVICE_SERVICE";
    private static final java.lang.String TAG = "CDM_PackageUtils";

    public static android.content.pm.PackageInfo getPackageInfo(android.content.Context context, final int userId, final java.lang.String packageName) {
        final android.content.pm.PackageManager pm = context.getPackageManager();
        final android.content.pm.PackageManager.PackageInfoFlags flags = android.content.pm.PackageManager.PackageInfoFlags.of(20480L);
        return (android.content.pm.PackageInfo) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.companion.utils.PackageUtils$$ExternalSyntheticLambda1
            public final java.lang.Object getOrThrow() {
                return com.android.server.companion.utils.PackageUtils.lambda$getPackageInfo$0(pm, packageName, flags, userId);
            }
        });
    }

    static /* synthetic */ android.content.pm.PackageInfo lambda$getPackageInfo$0(android.content.pm.PackageManager pm, java.lang.String packageName, android.content.pm.PackageManager.PackageInfoFlags flags, int userId) throws java.lang.Exception {
        try {
            return pm.getPackageInfoAsUser(packageName, flags, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Package [" + packageName + "] is not found.");
            return null;
        }
    }

    public static void enforceUsesCompanionDeviceFeature(android.content.Context context, int userId, java.lang.String packageName) {
        if (android.os.Binder.getCallingUid() == 1000) {
            return;
        }
        android.content.pm.PackageInfo packageInfo = getPackageInfo(context, userId, packageName);
        if (packageInfo == null) {
            throw new java.lang.IllegalArgumentException("Package " + packageName + " doesn't exist.");
        }
        android.content.pm.FeatureInfo[] requestedFeatures = packageInfo.reqFeatures;
        if (requestedFeatures != null) {
            for (android.content.pm.FeatureInfo requestedFeature : requestedFeatures) {
                if ("android.software.companion_device_setup".equals(requestedFeature.name)) {
                    return;
                }
            }
        }
        throw new java.lang.IllegalStateException("Must declare uses-feature android.software.companion_device_setup in manifest to use this API");
    }

    public static java.util.Map<java.lang.String, java.util.List<android.content.ComponentName>> getCompanionServicesForUser(android.content.Context context, int userId) {
        android.content.pm.PackageManager pm = context.getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> companionServices = pm.queryIntentServicesAsUser(COMPANION_SERVICE_INTENT, android.content.pm.PackageManager.ResolveInfoFlags.of(0L), userId);
        java.util.Map<java.lang.String, java.util.List<android.content.ComponentName>> packageNameToServiceInfoList = new java.util.HashMap<>(companionServices.size());
        for (android.content.pm.ResolveInfo resolveInfo : companionServices) {
            android.content.pm.ServiceInfo service = resolveInfo.serviceInfo;
            boolean requiresPermission = "android.permission.BIND_COMPANION_DEVICE_SERVICE".equals(resolveInfo.serviceInfo.permission);
            if (!requiresPermission) {
                android.util.Slog.w(TAG, "CompanionDeviceService " + service.getComponentName().flattenToShortString() + " must require android.permission.BIND_COMPANION_DEVICE_SERVICE");
            } else {
                java.util.ArrayList<android.content.ComponentName> services = (java.util.ArrayList) packageNameToServiceInfoList.computeIfAbsent(service.packageName, new java.util.function.Function() { // from class: com.android.server.companion.utils.PackageUtils$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.companion.utils.PackageUtils.lambda$getCompanionServicesForUser$1((java.lang.String) obj);
                    }
                });
                android.content.ComponentName componentName = service.getComponentName();
                if (isPrimaryCompanionDeviceService(pm, componentName, userId)) {
                    services.add(0, componentName);
                } else {
                    services.add(componentName);
                }
            }
        }
        return packageNameToServiceInfoList;
    }

    static /* synthetic */ java.util.List lambda$getCompanionServicesForUser$1(java.lang.String it) {
        return new java.util.ArrayList(1);
    }

    private static boolean isPrimaryCompanionDeviceService(android.content.pm.PackageManager pm, android.content.ComponentName componentName, int userId) {
        try {
            return pm.getPropertyAsUser(PROPERTY_PRIMARY_TAG, componentName.getPackageName(), componentName.getClassName(), userId).getBoolean();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isPackageAllowlisted(android.content.Context context, android.content.pm.PackageManagerInternal packageManagerInternal, java.lang.String packageName) {
        return isPackageAllowlisted(context, packageManagerInternal, packageName, android.R.array.config_cdma_international_roaming_indicators, android.R.array.config_cdma_home_system);
    }

    public static boolean isPermSyncAutoEnabled(android.content.Context context, android.content.pm.PackageManagerInternal packageManagerInternal, java.lang.String packageName) {
        return isPackageAllowlisted(context, packageManagerInternal, packageName, android.R.array.config_clockTickVibePattern, android.R.array.config_cell_retries_per_error_code);
    }

    private static boolean isPackageAllowlisted(android.content.Context context, android.content.pm.PackageManagerInternal packageManagerInternal, java.lang.String packageName, int packagesConfig, int certsConfig) {
        java.lang.String[] allowlistedPackages = context.getResources().getStringArray(packagesConfig);
        int i = 0;
        if (!com.android.internal.util.ArrayUtils.contains(allowlistedPackages, packageName)) {
            android.util.Slog.d(TAG, packageName + " is not allowlisted.");
            return false;
        }
        java.lang.String[] allowlistedPackagesSignatureDigests = context.getResources().getStringArray(certsConfig);
        java.util.Set<java.lang.String> allowlistedSignatureDigestsForRequestingPackage = new java.util.HashSet<>();
        for (int i2 = 0; i2 < allowlistedPackages.length; i2++) {
            if (allowlistedPackages[i2].equals(packageName)) {
                java.lang.String digest = allowlistedPackagesSignatureDigests[i2].replaceAll(":", "");
                allowlistedSignatureDigestsForRequestingPackage.add(digest);
            }
        }
        android.content.pm.Signature[] requestingPackageSignatures = packageManagerInternal.getPackage(packageName).getSigningDetails().getSignatures();
        java.lang.String[] requestingPackageSignatureDigests = android.util.PackageUtils.computeSignaturesSha256Digests(requestingPackageSignatures);
        boolean requestingPackageSignatureAllowlisted = false;
        int length = requestingPackageSignatureDigests.length;
        while (true) {
            if (i >= length) {
                break;
            }
            java.lang.String signatureDigest = requestingPackageSignatureDigests[i];
            if (!allowlistedSignatureDigestsForRequestingPackage.contains(signatureDigest)) {
                i++;
            } else {
                requestingPackageSignatureAllowlisted = true;
                break;
            }
        }
        if (!requestingPackageSignatureAllowlisted) {
            android.util.Slog.w(TAG, "Certificate mismatch for allowlisted package " + packageName);
        }
        return requestingPackageSignatureAllowlisted;
    }

    public static boolean isRestrictedSettingsAllowed(android.content.Context context, java.lang.String packageName, int uid) {
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.enhancedConfirmationModeApisEnabled()) {
            android.app.ecm.EnhancedConfirmationManager ecm = (android.app.ecm.EnhancedConfirmationManager) context.getSystemService(android.app.ecm.EnhancedConfirmationManager.class);
            try {
                return true ^ ecm.isRestricted(packageName, "android:access_notifications");
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return true;
            }
        }
        int mode = ((android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class)).noteOpNoThrow(119, uid, packageName, (java.lang.String) null, (java.lang.String) null);
        return mode == 0 || mode == 3;
    }
}
