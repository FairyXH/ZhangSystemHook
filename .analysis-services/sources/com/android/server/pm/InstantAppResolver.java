package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public abstract class InstantAppResolver {
    private static final boolean DEBUG_INSTANT = android.os.Build.IS_DEBUGGABLE;
    private static final int RESOLUTION_BIND_TIMEOUT = 2;
    private static final int RESOLUTION_CALL_TIMEOUT = 3;
    private static final int RESOLUTION_FAILURE = 1;
    private static final int RESOLUTION_SUCCESS = 0;
    private static final java.lang.String TAG = "PackageManager";
    private static com.android.internal.logging.MetricsLogger sMetricsLogger;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ResolutionStatus {
    }

    private static com.android.internal.logging.MetricsLogger getLogger() {
        if (sMetricsLogger == null) {
            sMetricsLogger = new com.android.internal.logging.MetricsLogger();
        }
        return sMetricsLogger;
    }

    public static android.content.Intent sanitizeIntent(android.content.Intent origIntent) {
        android.net.Uri sanitizedUri;
        android.content.Intent sanitizedIntent = new android.content.Intent(origIntent.getAction());
        java.util.Set<java.lang.String> categories = origIntent.getCategories();
        if (categories != null) {
            for (java.lang.String category : categories) {
                sanitizedIntent.addCategory(category);
            }
        }
        if (origIntent.getData() == null) {
            sanitizedUri = null;
        } else {
            sanitizedUri = android.net.Uri.fromParts(origIntent.getScheme(), "", "");
        }
        sanitizedIntent.setDataAndType(sanitizedUri, origIntent.getType());
        sanitizedIntent.addFlags(origIntent.getFlags());
        sanitizedIntent.setPackage(origIntent.getPackage());
        return sanitizedIntent;
    }

    public static android.content.pm.InstantAppResolveInfo.InstantAppDigest parseDigest(android.content.Intent origIntent) {
        if (origIntent.getData() != null && !android.text.TextUtils.isEmpty(origIntent.getData().getHost())) {
            return new android.content.pm.InstantAppResolveInfo.InstantAppDigest(origIntent.getData().getHost(), 5);
        }
        return android.content.pm.InstantAppResolveInfo.InstantAppDigest.UNDEFINED;
    }

    public static android.content.pm.AuxiliaryResolveInfo doInstantAppResolutionPhaseOne(com.android.server.pm.Computer computer, com.android.server.pm.UserManagerService userManager, com.android.server.pm.InstantAppResolverConnection connection, android.content.pm.InstantAppRequest requestObj) throws java.util.concurrent.TimeoutException {
        android.content.Intent origIntent;
        java.lang.String str;
        int i;
        int resolutionStatus;
        long startTime = java.lang.System.currentTimeMillis();
        java.lang.String token = requestObj.token;
        if (DEBUG_INSTANT) {
            android.util.Log.d(TAG, "[" + token + "] Phase1; resolving");
        }
        android.content.pm.AuxiliaryResolveInfo resolveInfo = null;
        android.content.Intent origIntent2 = requestObj.origIntent;
        try {
            java.util.List<android.content.pm.InstantAppResolveInfo> instantAppResolveInfoList = connection.getInstantAppResolveInfoList(buildRequestInfo(requestObj));
            if (instantAppResolveInfoList == null || instantAppResolveInfoList.size() <= 0) {
                origIntent = origIntent2;
                str = TAG;
                i = 2;
            } else {
                java.lang.String str2 = requestObj.resolvedType;
                int i2 = requestObj.userId;
                java.lang.String str3 = origIntent2.getPackage();
                int[] iArr = requestObj.hostDigestPrefixSecure;
                str = TAG;
                i = 2;
                origIntent = origIntent2;
                try {
                    resolveInfo = filterInstantAppIntent(computer, userManager, instantAppResolveInfoList, origIntent2, str2, i2, str3, token, iArr);
                } catch (com.android.server.pm.InstantAppResolverConnection.ConnectionException e) {
                    e = e;
                    if (e.failure == 1) {
                        resolutionStatus = 2;
                    } else if (e.failure == i) {
                        resolutionStatus = 3;
                    } else {
                        resolutionStatus = 1;
                    }
                }
            }
            resolutionStatus = 0;
        } catch (com.android.server.pm.InstantAppResolverConnection.ConnectionException e2) {
            e = e2;
            origIntent = origIntent2;
            str = TAG;
            i = 2;
        }
        if (requestObj.resolveForStart && resolutionStatus == 0) {
            logMetrics(899, startTime, token, resolutionStatus);
        }
        if (DEBUG_INSTANT && resolveInfo == null) {
            if (resolutionStatus == i) {
                android.util.Log.d(str, "[" + token + "] Phase1; bind timed out");
            } else {
                java.lang.String str4 = str;
                if (resolutionStatus == 3) {
                    android.util.Log.d(str4, "[" + token + "] Phase1; call timed out");
                } else if (resolutionStatus != 0) {
                    android.util.Log.d(str4, "[" + token + "] Phase1; service connection error");
                } else {
                    android.util.Log.d(str4, "[" + token + "] Phase1; No results matched");
                }
            }
        }
        if (resolveInfo == null && (origIntent.getFlags() & 2048) != 0) {
            return new android.content.pm.AuxiliaryResolveInfo(token, false, createFailureIntent(origIntent, token), (java.util.List) null, requestObj.hostDigestPrefixSecure);
        }
        return resolveInfo;
    }

    public static void doInstantAppResolutionPhaseTwo(final android.content.Context context, final com.android.server.pm.Computer computer, final com.android.server.pm.UserManagerService userManager, com.android.server.pm.InstantAppResolverConnection connection, final android.content.pm.InstantAppRequest requestObj, final android.content.pm.ActivityInfo instantAppInstaller, android.os.Handler callbackHandler) {
        long startTime = java.lang.System.currentTimeMillis();
        final java.lang.String token = requestObj.token;
        if (DEBUG_INSTANT) {
            android.util.Log.d(TAG, "[" + token + "] Phase2; resolving");
        }
        final android.content.Intent origIntent = requestObj.origIntent;
        final android.content.Intent sanitizedIntent = sanitizeIntent(origIntent);
        com.android.server.pm.InstantAppResolverConnection.PhaseTwoCallback callback = new com.android.server.pm.InstantAppResolverConnection.PhaseTwoCallback() { // from class: com.android.server.pm.InstantAppResolver.1
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.pm.InstantAppResolverConnection.PhaseTwoCallback
            public void onPhaseTwoResolved(java.util.List<android.content.pm.InstantAppResolveInfo> instantAppResolveInfoList, long startTime2) {
                android.content.Intent failureIntent;
                android.content.pm.AuxiliaryResolveInfo instantAppIntentInfo;
                if (instantAppResolveInfoList != null && instantAppResolveInfoList.size() > 0 && (instantAppIntentInfo = com.android.server.pm.InstantAppResolver.filterInstantAppIntent(computer, userManager, instantAppResolveInfoList, origIntent, null, 0, origIntent.getPackage(), token, requestObj.hostDigestPrefixSecure)) != null) {
                    failureIntent = instantAppIntentInfo.failureIntent;
                } else {
                    failureIntent = null;
                }
                android.content.Intent installerIntent = com.android.server.pm.InstantAppResolver.buildEphemeralInstallerIntent(requestObj.origIntent, sanitizedIntent, failureIntent, requestObj.callingPackage, requestObj.callingFeatureId, requestObj.verificationBundle, requestObj.resolvedType, requestObj.userId, requestObj.responseObj.installFailureActivity, token, false, requestObj.responseObj.filters);
                installerIntent.setComponent(new android.content.ComponentName(instantAppInstaller.packageName, instantAppInstaller.name));
                com.android.server.pm.InstantAppResolver.logMetrics(900, startTime2, token, requestObj.responseObj.filters != null ? 0 : 1);
                context.startActivity(installerIntent);
            }
        };
        try {
            connection.getInstantAppIntentFilterList(buildRequestInfo(requestObj), callback, callbackHandler, startTime);
        } catch (com.android.server.pm.InstantAppResolverConnection.ConnectionException e) {
            int resolutionStatus = 1;
            if (e.failure == 1) {
                resolutionStatus = 2;
            }
            logMetrics(900, startTime, token, resolutionStatus);
            if (DEBUG_INSTANT) {
                if (resolutionStatus == 2) {
                    android.util.Log.d(TAG, "[" + token + "] Phase2; bind timed out");
                } else {
                    android.util.Log.d(TAG, "[" + token + "] Phase2; service connection error");
                }
            }
        }
    }

    public static android.content.Intent buildEphemeralInstallerIntent(android.content.Intent origIntent, android.content.Intent sanitizedIntent, android.content.Intent failureIntent, java.lang.String callingPackage, java.lang.String callingFeatureId, android.os.Bundle verificationBundle, java.lang.String resolvedType, int userId, android.content.ComponentName installFailureActivity, java.lang.String token, boolean needsPhaseTwo, java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> filters) {
        android.content.Intent onFailureIntent;
        java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> list = filters;
        int flags = origIntent.getFlags();
        android.content.Intent intent = new android.content.Intent();
        intent.setFlags(1073741824 | flags | 8388608);
        if (token != null) {
            intent.putExtra("android.intent.extra.INSTANT_APP_TOKEN", token);
        }
        if (origIntent.getData() != null) {
            intent.putExtra("android.intent.extra.INSTANT_APP_HOSTNAME", origIntent.getData().getHost());
        }
        intent.putExtra("android.intent.extra.INSTANT_APP_ACTION", origIntent.getAction());
        intent.putExtra("android.intent.extra.INTENT", sanitizedIntent);
        if (needsPhaseTwo) {
            intent.setAction("android.intent.action.RESOLVE_INSTANT_APP_PACKAGE");
        } else {
            android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(1);
            if (failureIntent != null || installFailureActivity != null) {
                if (installFailureActivity != null) {
                    try {
                        onFailureIntent = new android.content.Intent();
                        onFailureIntent.setComponent(installFailureActivity);
                        if (list != null && filters.size() == 1) {
                            onFailureIntent.putExtra("android.intent.extra.SPLIT_NAME", list.get(0).splitName);
                        }
                        onFailureIntent.putExtra("android.intent.extra.INTENT", origIntent);
                    } catch (android.os.RemoteException e) {
                    }
                } else {
                    onFailureIntent = failureIntent;
                }
                android.content.IIntentSender failureIntentTarget = android.app.ActivityManager.getService().getIntentSenderWithFeature(2, callingPackage, callingFeatureId, (android.os.IBinder) null, (java.lang.String) null, 1, new android.content.Intent[]{onFailureIntent}, new java.lang.String[]{resolvedType}, 1409286144, options.toBundle(), userId);
                android.content.IntentSender failureSender = new android.content.IntentSender(failureIntentTarget);
                intent.putExtra("android.intent.extra.INSTANT_APP_FAILURE", failureSender);
            }
            android.content.Intent successIntent = new android.content.Intent(origIntent);
            successIntent.setLaunchToken(token);
            try {
                android.content.IIntentSender successIntentTarget = android.app.ActivityManager.getService().getIntentSenderWithFeature(2, callingPackage, callingFeatureId, (android.os.IBinder) null, (java.lang.String) null, 0, new android.content.Intent[]{successIntent}, new java.lang.String[]{resolvedType}, 1409286144, options.toBundle(), userId);
                android.content.IntentSender successSender = new android.content.IntentSender(successIntentTarget);
                intent.putExtra("android.intent.extra.INSTANT_APP_SUCCESS", successSender);
            } catch (android.os.RemoteException e2) {
            }
            if (verificationBundle != null) {
                intent.putExtra("android.intent.extra.VERIFICATION_BUNDLE", verificationBundle);
            }
            intent.putExtra("android.intent.extra.CALLING_PACKAGE", callingPackage);
            if (list != null) {
                android.os.Bundle[] resolvableFilters = new android.os.Bundle[filters.size()];
                int i = 0;
                int max = filters.size();
                while (i < max) {
                    android.os.Bundle resolvableFilter = new android.os.Bundle();
                    android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter filter = list.get(i);
                    resolvableFilter.putBoolean("android.intent.extra.UNKNOWN_INSTANT_APP", filter.resolveInfo != null && filter.resolveInfo.shouldLetInstallerDecide());
                    resolvableFilter.putString("android.intent.extra.PACKAGE_NAME", filter.packageName);
                    resolvableFilter.putString("android.intent.extra.SPLIT_NAME", filter.splitName);
                    resolvableFilter.putLong("android.intent.extra.LONG_VERSION_CODE", filter.versionCode);
                    resolvableFilter.putBundle("android.intent.extra.INSTANT_APP_EXTRAS", filter.extras);
                    resolvableFilters[i] = resolvableFilter;
                    if (i == 0) {
                        intent.putExtras(resolvableFilter);
                        intent.putExtra("android.intent.extra.VERSION_CODE", (int) filter.versionCode);
                    }
                    i++;
                    list = filters;
                }
                intent.putExtra("android.intent.extra.INSTANT_APP_BUNDLES", resolvableFilters);
            }
            intent.setAction("android.intent.action.INSTALL_INSTANT_APP_PACKAGE");
        }
        return intent;
    }

    private static android.content.pm.InstantAppRequestInfo buildRequestInfo(android.content.pm.InstantAppRequest request) {
        return new android.content.pm.InstantAppRequestInfo(sanitizeIntent(request.origIntent), request.hostDigestPrefixSecure, android.os.UserHandle.of(request.userId), request.isRequesterInstantApp, request.token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.content.pm.AuxiliaryResolveInfo filterInstantAppIntent(com.android.server.pm.Computer computer, com.android.server.pm.UserManagerService userManager, java.util.List<android.content.pm.InstantAppResolveInfo> instantAppResolveInfoList, android.content.Intent origIntent, java.lang.String resolvedType, int userId, java.lang.String packageName, java.lang.String token, int[] hostDigestPrefixSecure) {
        android.content.pm.InstantAppResolveInfo.InstantAppDigest digest = parseDigest(origIntent);
        int[] shaPrefix = digest.getDigestPrefix();
        byte[][] digestBytes = digest.getDigestBytes();
        boolean requiresSecondPhase = false;
        java.util.ArrayList<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> filters = null;
        boolean requiresPrefixMatch = origIntent.isWebIntent() || (shaPrefix.length > 0 && (origIntent.getFlags() & 2048) == 0);
        for (android.content.pm.InstantAppResolveInfo instantAppResolveInfo : instantAppResolveInfoList) {
            if (requiresPrefixMatch && instantAppResolveInfo.shouldLetInstallerDecide()) {
                android.util.Slog.d(TAG, "InstantAppResolveInfo with mShouldLetInstallerDecide=true when digest required; ignoring");
            } else {
                byte[] filterDigestBytes = instantAppResolveInfo.getDigestBytes();
                if (shaPrefix.length > 0 && (requiresPrefixMatch || filterDigestBytes.length > 0)) {
                    boolean matchFound = false;
                    int i = shaPrefix.length - 1;
                    while (true) {
                        if (i < 0) {
                            break;
                        }
                        if (java.util.Arrays.equals(digestBytes[i], filterDigestBytes)) {
                            matchFound = true;
                            break;
                        }
                        i--;
                    }
                    if (!matchFound) {
                    }
                }
                java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> matchFilters = computeResolveFilters(computer, userManager, origIntent, resolvedType, userId, packageName, token, instantAppResolveInfo);
                if (matchFilters != null) {
                    if (matchFilters.isEmpty()) {
                        requiresSecondPhase = true;
                    }
                    if (filters == null) {
                        filters = new java.util.ArrayList<>(matchFilters);
                    } else {
                        filters.addAll(matchFilters);
                    }
                }
            }
        }
        if (filters != null && !filters.isEmpty()) {
            return new android.content.pm.AuxiliaryResolveInfo(token, requiresSecondPhase, createFailureIntent(origIntent, token), filters, hostDigestPrefixSecure);
        }
        return null;
    }

    private static android.content.Intent createFailureIntent(android.content.Intent origIntent, java.lang.String token) {
        android.content.Intent failureIntent = new android.content.Intent(origIntent);
        failureIntent.setFlags(failureIntent.getFlags() | Integer.MIN_VALUE);
        failureIntent.setFlags(failureIntent.getFlags() & (-2049));
        failureIntent.setLaunchToken(token);
        return failureIntent;
    }

    private static java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> computeResolveFilters(com.android.server.pm.Computer computer, com.android.server.pm.UserManagerService userManager, android.content.Intent origIntent, java.lang.String resolvedType, int userId, java.lang.String packageName, java.lang.String token, android.content.pm.InstantAppResolveInfo instantAppInfo) {
        if (instantAppInfo.shouldLetInstallerDecide()) {
            return java.util.Collections.singletonList(new android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter(instantAppInfo, (java.lang.String) null, instantAppInfo.getExtras()));
        }
        if (packageName != null && !packageName.equals(instantAppInfo.getPackageName())) {
            return null;
        }
        java.util.List<android.content.pm.InstantAppIntentFilter> instantAppFilters = instantAppInfo.getIntentFilters();
        if (instantAppFilters == null || instantAppFilters.isEmpty()) {
            java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> list = null;
            if (origIntent.isWebIntent()) {
                return list;
            }
            if (DEBUG_INSTANT) {
                android.util.Log.d(TAG, "No app filters; go to phase 2");
            }
            return java.util.Collections.emptyList();
        }
        com.android.server.pm.resolution.ComponentResolver.InstantAppIntentResolver instantAppResolver = new com.android.server.pm.resolution.ComponentResolver.InstantAppIntentResolver(userManager);
        for (int j = instantAppFilters.size() - 1; j >= 0; j--) {
            android.content.pm.InstantAppIntentFilter instantAppFilter = instantAppFilters.get(j);
            java.util.List<android.content.IntentFilter> splitFilters = instantAppFilter.getFilters();
            if (splitFilters != null && !splitFilters.isEmpty()) {
                for (int k = splitFilters.size() - 1; k >= 0; k--) {
                    android.content.IntentFilter filter = splitFilters.get(k);
                    java.util.Iterator<android.content.IntentFilter.AuthorityEntry> authorities = filter.authoritiesIterator();
                    if ((authorities != null && authorities.hasNext()) || ((!filter.hasDataScheme("http") && !filter.hasDataScheme("https")) || !filter.hasAction("android.intent.action.VIEW") || !filter.hasCategory("android.intent.category.BROWSABLE"))) {
                        instantAppResolver.addFilter(computer, new android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter(filter, instantAppInfo, instantAppFilter.getSplitName(), instantAppInfo.getExtras()));
                    }
                }
            }
        }
        java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> matchedResolveInfoList = instantAppResolver.queryIntent(computer, origIntent, resolvedType, false, userId);
        if (!matchedResolveInfoList.isEmpty()) {
            if (DEBUG_INSTANT) {
                android.util.Log.d(TAG, "[" + token + "] Found match(es); " + matchedResolveInfoList);
            }
            return matchedResolveInfoList;
        }
        if (DEBUG_INSTANT) {
            android.util.Log.d(TAG, "[" + token + "] No matches found package: " + instantAppInfo.getPackageName() + ", versionCode: " + instantAppInfo.getVersionCode());
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logMetrics(int action, long startTime, java.lang.String token, int status) {
        android.metrics.LogMaker logMaker = new android.metrics.LogMaker(action).setType(4).addTaggedData(901, new java.lang.Long(java.lang.System.currentTimeMillis() - startTime)).addTaggedData(903, token).addTaggedData(902, new java.lang.Integer(status));
        getLogger().write(logMaker);
    }
}
