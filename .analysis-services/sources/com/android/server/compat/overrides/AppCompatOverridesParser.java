package com.android.server.compat.overrides;

/* JADX INFO: loaded from: classes.dex */
final class AppCompatOverridesParser {
    private static final java.util.regex.Pattern BOOLEAN_PATTERN = java.util.regex.Pattern.compile("true|false", 2);
    static final java.lang.String FLAG_OWNED_CHANGE_IDS = "owned_change_ids";
    static final java.lang.String FLAG_REMOVE_OVERRIDES = "remove_overrides";
    private static final java.lang.String TAG = "AppCompatOverridesParser";
    private static final java.lang.String WILDCARD_NO_OWNED_CHANGE_IDS_WARNING = "Wildcard can't be used in 'remove_overrides' flag with an empty owned_change_ids' flag";
    private static final java.lang.String WILDCARD_SYMBOL = "*";
    private final android.content.pm.PackageManager mPackageManager;

    AppCompatOverridesParser(android.content.pm.PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    java.util.Map<java.lang.String, java.util.Set<java.lang.Long>> parseRemoveOverrides(java.lang.String configStr, java.util.Set<java.lang.Long> ownedChangeIds) {
        java.lang.String str;
        java.util.Set<java.lang.Long> set = ownedChangeIds;
        if (configStr.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        java.util.Map<java.lang.String, java.util.Set<java.lang.Long>> result = new android.util.ArrayMap<>();
        java.lang.String str2 = "*";
        if (configStr.equals("*")) {
            if (ownedChangeIds.isEmpty()) {
                android.util.Slog.w(TAG, WILDCARD_NO_OWNED_CHANGE_IDS_WARNING);
                return java.util.Collections.emptyMap();
            }
            java.util.List<android.content.pm.ApplicationInfo> installedApps = this.mPackageManager.getInstalledApplications(4194304);
            for (android.content.pm.ApplicationInfo appInfo : installedApps) {
                result.put(appInfo.packageName, set);
            }
            return result;
        }
        android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
        try {
            parser.setString(configStr);
            int i = 0;
            while (i < parser.size()) {
                java.lang.String packageName = parser.keyAt(i);
                java.lang.String changeIdsStr = parser.getString(packageName, "");
                if (changeIdsStr.equals(str2)) {
                    if (ownedChangeIds.isEmpty()) {
                        android.util.Slog.w(TAG, WILDCARD_NO_OWNED_CHANGE_IDS_WARNING);
                    } else {
                        result.put(packageName, set);
                    }
                } else {
                    java.lang.String[] strArrSplit = changeIdsStr.split(":");
                    int length = strArrSplit.length;
                    int i2 = 0;
                    while (i2 < length) {
                        java.lang.String changeIdStr = strArrSplit[i2];
                        try {
                            long changeId = java.lang.Long.parseLong(changeIdStr);
                            result.computeIfAbsent(packageName, new java.util.function.Function() { // from class: com.android.server.compat.overrides.AppCompatOverridesParser$$ExternalSyntheticLambda0
                                @Override // java.util.function.Function
                                public final java.lang.Object apply(java.lang.Object obj) {
                                    return com.android.server.compat.overrides.AppCompatOverridesParser.lambda$parseRemoveOverrides$0((java.lang.String) obj);
                                }
                            }).add(java.lang.Long.valueOf(changeId));
                            str = str2;
                        } catch (java.lang.NumberFormatException e) {
                            str = str2;
                            android.util.Slog.w(TAG, "Invalid change ID in 'remove_overrides' flag: " + changeIdStr, e);
                        }
                        i2++;
                        str2 = str;
                    }
                }
                i++;
                set = ownedChangeIds;
                str2 = str2;
            }
            return result;
        } catch (java.lang.IllegalArgumentException e2) {
            android.util.Slog.w(TAG, "Invalid format in 'remove_overrides' flag: " + configStr, e2);
            return java.util.Collections.emptyMap();
        }
    }

    static /* synthetic */ java.util.Set lambda$parseRemoveOverrides$0(java.lang.String k) {
        return new android.util.ArraySet();
    }

    static java.util.Set<java.lang.Long> parseOwnedChangeIds(java.lang.String configStr) {
        if (configStr.isEmpty()) {
            return java.util.Collections.emptySet();
        }
        java.util.Set<java.lang.Long> result = new android.util.ArraySet<>();
        for (java.lang.String changeIdStr : configStr.split(",")) {
            try {
                result.add(java.lang.Long.valueOf(java.lang.Long.parseLong(changeIdStr)));
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.w(TAG, "Invalid change ID in 'owned_change_ids' flag: " + changeIdStr, e);
            }
        }
        return result;
    }

    java.util.Map<java.lang.Long, android.app.compat.PackageOverride> parsePackageOverrides(java.lang.String configStr, java.lang.String packageName, long versionCode, java.util.Set<java.lang.Long> changeIdsToSkip) {
        android.util.Pair<java.lang.String, java.lang.String> signatureAndConfig;
        java.lang.String signature;
        if (configStr.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        com.android.server.compat.overrides.AppCompatOverridesParser.PackageOverrideComparator comparator = new com.android.server.compat.overrides.AppCompatOverridesParser.PackageOverrideComparator(versionCode);
        java.util.Map<java.lang.Long, android.app.compat.PackageOverride> overridesToAdd = new android.util.ArrayMap<>();
        android.util.Pair<java.lang.String, java.lang.String> signatureAndConfig2 = extractSignatureFromConfig(configStr);
        if (signatureAndConfig2 == null) {
            return java.util.Collections.emptyMap();
        }
        java.lang.String signature2 = (java.lang.String) signatureAndConfig2.first;
        java.lang.String overridesConfig = (java.lang.String) signatureAndConfig2.second;
        if (!verifySignature(packageName, signature2)) {
            return java.util.Collections.emptyMap();
        }
        java.lang.String[] strArrSplit = overridesConfig.split(",");
        int length = strArrSplit.length;
        int i = 0;
        while (i < length) {
            java.lang.String overrideEntryString = strArrSplit[i];
            java.util.List<java.lang.String> changeIdAndVersions = java.util.Arrays.asList(overrideEntryString.split(":", 4));
            if (changeIdAndVersions.size() != 4) {
                android.util.Slog.w(TAG, "Invalid change override entry: " + overrideEntryString);
                signatureAndConfig = signatureAndConfig2;
                signature = signature2;
            } else {
                try {
                    long changeId = java.lang.Long.parseLong(changeIdAndVersions.get(0));
                    if (changeIdsToSkip.contains(java.lang.Long.valueOf(changeId))) {
                        signatureAndConfig = signatureAndConfig2;
                        signature = signature2;
                    } else {
                        java.lang.String minVersionCodeStr = changeIdAndVersions.get(1);
                        java.lang.String maxVersionCodeStr = changeIdAndVersions.get(2);
                        java.lang.String enabledStr = changeIdAndVersions.get(3);
                        if (!BOOLEAN_PATTERN.matcher(enabledStr).matches()) {
                            signatureAndConfig = signatureAndConfig2;
                            android.util.Slog.w(TAG, "Invalid enabled string in override entry: " + overrideEntryString);
                            signature = signature2;
                        } else {
                            signatureAndConfig = signatureAndConfig2;
                            boolean enabled = java.lang.Boolean.parseBoolean(enabledStr);
                            android.app.compat.PackageOverride.Builder overrideBuilder = new android.app.compat.PackageOverride.Builder().setEnabled(enabled);
                            try {
                                if (minVersionCodeStr.isEmpty()) {
                                    signature = signature2;
                                } else {
                                    signature = signature2;
                                    try {
                                        overrideBuilder.setMinVersionCode(java.lang.Long.parseLong(minVersionCodeStr));
                                    } catch (java.lang.NumberFormatException e) {
                                        e = e;
                                        android.util.Slog.w(TAG, "Invalid min/max version code in override entry: " + overrideEntryString, e);
                                    }
                                }
                                if (!maxVersionCodeStr.isEmpty()) {
                                    overrideBuilder.setMaxVersionCode(java.lang.Long.parseLong(maxVersionCodeStr));
                                }
                                try {
                                    android.app.compat.PackageOverride override = overrideBuilder.build();
                                    if (!overridesToAdd.containsKey(java.lang.Long.valueOf(changeId)) || comparator.compare(override, overridesToAdd.get(java.lang.Long.valueOf(changeId))) < 0) {
                                        overridesToAdd.put(java.lang.Long.valueOf(changeId), override);
                                    }
                                } catch (java.lang.IllegalArgumentException e2) {
                                    android.util.Slog.w(TAG, "Failed to build PackageOverride", e2);
                                }
                            } catch (java.lang.NumberFormatException e3) {
                                e = e3;
                                signature = signature2;
                            }
                        }
                    }
                } catch (java.lang.NumberFormatException e4) {
                    signatureAndConfig = signatureAndConfig2;
                    signature = signature2;
                    android.util.Slog.w(TAG, "Invalid change ID in override entry: " + overrideEntryString, e4);
                }
            }
            i++;
            signatureAndConfig2 = signatureAndConfig;
            signature2 = signature;
        }
        return overridesToAdd;
    }

    private static android.util.Pair<java.lang.String, java.lang.String> extractSignatureFromConfig(java.lang.String configStr) {
        java.util.List<java.lang.String> signatureAndConfig = java.util.Arrays.asList(configStr.split("~"));
        if (signatureAndConfig.size() == 1) {
            return android.util.Pair.create("", configStr);
        }
        if (signatureAndConfig.size() > 2) {
            android.util.Slog.w(TAG, "Only one signature per config is supported. Config: " + configStr);
            return null;
        }
        return android.util.Pair.create(signatureAndConfig.get(0), signatureAndConfig.get(1));
    }

    private boolean verifySignature(java.lang.String packageName, java.lang.String signature) {
        try {
            boolean z = true;
            if (!signature.isEmpty() && !this.mPackageManager.hasSigningCertificate(packageName, libcore.util.HexEncoding.decode(signature), 1)) {
                z = false;
            }
            boolean signatureValid = z;
            if (!signatureValid) {
                android.util.Slog.w(TAG, packageName + " did not have expected signature: " + signature);
            }
            return signatureValid;
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.w(TAG, "Unable to verify signature " + signature + " for " + packageName, e);
            return false;
        }
    }

    private static final class PackageOverrideComparator implements java.util.Comparator<android.app.compat.PackageOverride> {
        private final long mVersionCode;

        PackageOverrideComparator(long versionCode) {
            this.mVersionCode = versionCode;
        }

        @Override // java.util.Comparator
        public int compare(android.app.compat.PackageOverride o1, android.app.compat.PackageOverride o2) {
            boolean isVersionInRange1 = isVersionInRange(o1, this.mVersionCode);
            boolean isVersionInRange2 = isVersionInRange(o2, this.mVersionCode);
            if (isVersionInRange1 != isVersionInRange2) {
                return isVersionInRange1 ? -1 : 1;
            }
            boolean isVersionAfterRange1 = isVersionAfterRange(o1, this.mVersionCode);
            boolean isVersionAfterRange2 = isVersionAfterRange(o2, this.mVersionCode);
            if (isVersionAfterRange1 != isVersionAfterRange2) {
                return isVersionAfterRange1 ? -1 : 1;
            }
            return java.lang.Long.compare(getVersionProximity(o1, this.mVersionCode), getVersionProximity(o2, this.mVersionCode));
        }

        private static boolean isVersionInRange(android.app.compat.PackageOverride override, long versionCode) {
            return override.getMinVersionCode() <= versionCode && versionCode <= override.getMaxVersionCode();
        }

        private static boolean isVersionAfterRange(android.app.compat.PackageOverride override, long versionCode) {
            return override.getMaxVersionCode() < versionCode;
        }

        private static boolean isVersionBeforeRange(android.app.compat.PackageOverride override, long versionCode) {
            return override.getMinVersionCode() > versionCode;
        }

        private static long getVersionProximity(android.app.compat.PackageOverride override, long versionCode) {
            if (isVersionAfterRange(override, versionCode)) {
                return versionCode - override.getMaxVersionCode();
            }
            if (isVersionBeforeRange(override, versionCode)) {
                return override.getMinVersionCode() - versionCode;
            }
            return 0L;
        }
    }
}
