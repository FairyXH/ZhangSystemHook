package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public final class AppOpsPolicy implements android.app.AppOpsManagerInternal.CheckOpsDelegate {
    private static final java.lang.String ACTIVITY_RECOGNITION_TAGS = "android:activity_recognition_allow_listed_tags";
    private static final java.lang.String ACTIVITY_RECOGNITION_TAGS_SEPARATOR = ";";
    private static final java.lang.String LOG_TAG = com.android.server.policy.AppOpsPolicy.class.getName();
    private static final boolean SYSPROP_HOTWORD_DETECTION_SERVICE_REQUIRED = android.os.SystemProperties.getBoolean("ro.hotword.detection_service_required", false);
    private final android.content.Context mContext;
    private final boolean mIsHotwordDetectionServiceRequired;
    private final android.app.role.RoleManager mRoleManager;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.os.IBinder mToken = new android.os.Binder();
    private final java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.os.PackageTagsList> mLocationTags = new java.util.concurrent.ConcurrentHashMap<>();
    private final android.util.SparseArray<android.os.PackageTagsList> mPerUidLocationTags = new android.util.SparseArray<>();
    private final java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.os.PackageTagsList> mActivityRecognitionTags = new java.util.concurrent.ConcurrentHashMap<>();
    private final android.service.voice.VoiceInteractionManagerInternal mVoiceInteractionManagerInternal = (android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class);

    public AppOpsPolicy(android.content.Context context) {
        this.mContext = context;
        this.mRoleManager = (android.app.role.RoleManager) this.mContext.getSystemService(android.app.role.RoleManager.class);
        this.mIsHotwordDetectionServiceRequired = isHotwordDetectionServiceRequired(this.mContext.getPackageManager());
        android.location.LocationManagerInternal locationManagerInternal = (android.location.LocationManagerInternal) com.android.server.LocalServices.getService(android.location.LocationManagerInternal.class);
        locationManagerInternal.setLocationPackageTagsListener(new android.location.LocationManagerInternal.LocationPackageTagsListener() { // from class: com.android.server.policy.AppOpsPolicy$$ExternalSyntheticLambda0
            public final void onLocationPackageTagsChanged(int i, android.os.PackageTagsList packageTagsList) {
                this.f$0.lambda$new$0(i, packageTagsList);
            }
        });
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
        intentFilter.addDataScheme("package");
        context.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.policy.AppOpsPolicy.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                android.net.Uri uri = intent.getData();
                if (uri == null) {
                    return;
                }
                java.lang.String packageName = uri.getSchemeSpecificPart();
                if (android.text.TextUtils.isEmpty(packageName)) {
                    return;
                }
                java.util.List<java.lang.String> activityRecognizers = com.android.server.policy.AppOpsPolicy.this.mRoleManager.getRoleHolders("android.app.role.SYSTEM_ACTIVITY_RECOGNIZER");
                if (activityRecognizers.contains(packageName)) {
                    com.android.server.policy.AppOpsPolicy.this.updateActivityRecognizerTags(packageName);
                }
            }
        }, android.os.UserHandle.SYSTEM, intentFilter, null, null);
        this.mRoleManager.addOnRoleHoldersChangedListenerAsUser(context.getMainExecutor(), new android.app.role.OnRoleHoldersChangedListener() { // from class: com.android.server.policy.AppOpsPolicy$$ExternalSyntheticLambda1
            public final void onRoleHoldersChanged(java.lang.String str, android.os.UserHandle userHandle) {
                this.f$0.lambda$new$1(str, userHandle);
            }
        }, android.os.UserHandle.SYSTEM);
        initializeActivityRecognizersTags();
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (!pm.hasSystemFeature("android.hardware.telephony") && !pm.hasSystemFeature("android.hardware.microphone") && !pm.hasSystemFeature("android.software.telecom")) {
            android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
            appOps.setUserRestrictionForUser(100, true, this.mToken, null, -1);
            appOps.setUserRestrictionForUser(101, true, this.mToken, null, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int uid, android.os.PackageTagsList packageTagsList) {
        synchronized (this.mLock) {
            if (packageTagsList.isEmpty()) {
                this.mPerUidLocationTags.remove(uid);
            } else {
                this.mPerUidLocationTags.set(uid, packageTagsList);
            }
            int appId = android.os.UserHandle.getAppId(uid);
            android.os.PackageTagsList.Builder appIdTags = new android.os.PackageTagsList.Builder(1);
            int size = this.mPerUidLocationTags.size();
            for (int i = 0; i < size; i++) {
                if (android.os.UserHandle.getAppId(this.mPerUidLocationTags.keyAt(i)) == appId) {
                    appIdTags.add(this.mPerUidLocationTags.valueAt(i));
                }
            }
            updateAllowListedTagsForPackageLocked(appId, appIdTags.build(), this.mLocationTags);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(java.lang.String roleName, android.os.UserHandle user) {
        if ("android.app.role.SYSTEM_ACTIVITY_RECOGNIZER".equals(roleName)) {
            initializeActivityRecognizersTags();
        }
    }

    public static int getVoiceActivationOp() {
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.voiceActivationPermissionApis()) {
            return 136;
        }
        return 102;
    }

    public static boolean isHotwordDetectionServiceRequired(android.content.pm.PackageManager pm) {
        if (pm.hasSystemFeature("android.hardware.type.automotive") || pm.hasSystemFeature("android.software.leanback")) {
            return false;
        }
        return SYSPROP_HOTWORD_DETECTION_SERVICE_REQUIRED;
    }

    public int checkOperation(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean raw, com.android.internal.util.function.HexFunction<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.Integer> superImpl) {
        return ((java.lang.Integer) superImpl.apply(java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(resolveUid(code, uid)), packageName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(raw))).intValue();
    }

    public int checkAudioOperation(int code, int usage, int uid, java.lang.String packageName, com.android.internal.util.function.QuadFunction<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer> superImpl) {
        return ((java.lang.Integer) superImpl.apply(java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(usage), java.lang.Integer.valueOf(uid), packageName)).intValue();
    }

    public android.app.SyncNotedAppOp noteOperation(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, com.android.internal.util.function.OctFunction<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.Boolean, android.app.SyncNotedAppOp> superImpl) {
        return (android.app.SyncNotedAppOp) superImpl.apply(java.lang.Integer.valueOf(resolveDatasourceOp(code, uid, packageName, attributionTag)), java.lang.Integer.valueOf(resolveUid(code, uid)), packageName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage));
    }

    public android.app.SyncNotedAppOp noteProxyOperation(int code, android.content.AttributionSource attributionSource, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation, com.android.internal.util.function.HexFunction<java.lang.Integer, android.content.AttributionSource, java.lang.Boolean, java.lang.String, java.lang.Boolean, java.lang.Boolean, android.app.SyncNotedAppOp> superImpl) {
        return (android.app.SyncNotedAppOp) superImpl.apply(java.lang.Integer.valueOf(resolveDatasourceOp(code, attributionSource.getUid(), attributionSource.getPackageName(), attributionSource.getAttributionTag())), attributionSource, java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage), java.lang.Boolean.valueOf(skipProxyOperation));
    }

    public android.app.SyncNotedAppOp startOperation(android.os.IBinder token, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, int attributionFlags, int attributionChainId, com.android.internal.util.function.DodecFunction<android.os.IBinder, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Integer, android.app.SyncNotedAppOp> superImpl) {
        return (android.app.SyncNotedAppOp) superImpl.apply(token, java.lang.Integer.valueOf(resolveDatasourceOp(code, uid, packageName, attributionTag)), java.lang.Integer.valueOf(resolveUid(code, uid)), packageName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(startIfModeDefault), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage), java.lang.Integer.valueOf(attributionFlags), java.lang.Integer.valueOf(attributionChainId));
    }

    public android.app.SyncNotedAppOp startProxyOperation(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation, int proxyAttributionFlags, int proxiedAttributionFlags, int attributionChainId, com.android.internal.util.function.UndecFunction<android.os.IBinder, java.lang.Integer, android.content.AttributionSource, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.Integer, android.app.SyncNotedAppOp> superImpl) {
        return (android.app.SyncNotedAppOp) superImpl.apply(clientId, java.lang.Integer.valueOf(resolveDatasourceOp(code, attributionSource.getUid(), attributionSource.getPackageName(), attributionSource.getAttributionTag())), attributionSource, java.lang.Boolean.valueOf(startIfModeDefault), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage), java.lang.Boolean.valueOf(skipProxyOperation), java.lang.Integer.valueOf(proxyAttributionFlags), java.lang.Integer.valueOf(proxiedAttributionFlags), java.lang.Integer.valueOf(attributionChainId));
    }

    public void finishOperation(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, com.android.internal.util.function.HexConsumer<android.os.IBinder, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer> superImpl) {
        superImpl.accept(clientId, java.lang.Integer.valueOf(resolveDatasourceOp(code, uid, packageName, attributionTag)), java.lang.Integer.valueOf(resolveUid(code, uid)), packageName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId));
    }

    public void finishProxyOperation(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean skipProxyOperation, com.android.internal.util.function.QuadFunction<android.os.IBinder, java.lang.Integer, android.content.AttributionSource, java.lang.Boolean, java.lang.Void> superImpl) {
        superImpl.apply(clientId, java.lang.Integer.valueOf(resolveDatasourceOp(code, attributionSource.getUid(), attributionSource.getPackageName(), attributionSource.getAttributionTag())), attributionSource, java.lang.Boolean.valueOf(skipProxyOperation));
    }

    public void dumpTags(java.io.PrintWriter writer) {
        if (!this.mLocationTags.isEmpty()) {
            writer.println("  AppOps policy location tags:");
            writeTags(this.mLocationTags, writer);
            writer.println();
        }
        if (!this.mActivityRecognitionTags.isEmpty()) {
            writer.println("  AppOps policy activity recognition tags:");
            writeTags(this.mActivityRecognitionTags, writer);
            writer.println();
        }
    }

    private void writeTags(java.util.Map<java.lang.Integer, android.os.PackageTagsList> tags, java.io.PrintWriter writer) {
        int counter = 0;
        for (java.util.Map.Entry<java.lang.Integer, android.os.PackageTagsList> tagEntry : tags.entrySet()) {
            writer.print("    #");
            writer.print(counter);
            writer.print(": ");
            writer.print(tagEntry.getKey().toString());
            writer.print("=");
            tagEntry.getValue().dump(writer);
            counter++;
        }
    }

    private int resolveDatasourceOp(int code, int uid, java.lang.String packageName, java.lang.String attributionTag) {
        int code2 = resolveSandboxedServiceOp(resolveRecordAudioOp(code, uid), uid);
        if (attributionTag == null) {
            return code2;
        }
        int resolvedCode = resolveLocationOp(code2);
        if (resolvedCode != code2) {
            if (isDatasourceAttributionTag(uid, packageName, attributionTag, this.mLocationTags)) {
                return resolvedCode;
            }
        } else {
            int resolvedCode2 = resolveArOp(code2);
            if (resolvedCode2 != code2 && isDatasourceAttributionTag(uid, packageName, attributionTag, this.mActivityRecognitionTags)) {
                return resolvedCode2;
            }
        }
        return code2;
    }

    private void initializeActivityRecognizersTags() {
        java.util.List<java.lang.String> activityRecognizers = this.mRoleManager.getRoleHolders("android.app.role.SYSTEM_ACTIVITY_RECOGNIZER");
        int recognizerCount = activityRecognizers.size();
        if (recognizerCount > 0) {
            for (int i = 0; i < recognizerCount; i++) {
                java.lang.String activityRecognizer = activityRecognizers.get(i);
                updateActivityRecognizerTags(activityRecognizer);
            }
            return;
        }
        clearActivityRecognitionTags();
    }

    private void clearActivityRecognitionTags() {
        synchronized (this.mLock) {
            this.mActivityRecognitionTags.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActivityRecognizerTags(java.lang.String activityRecognizer) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.ACTIVITY_RECOGNIZER");
        intent.setPackage(activityRecognizer);
        android.content.pm.ResolveInfo resolvedService = this.mContext.getPackageManager().resolveServiceAsUser(intent, 819332, 0);
        if (resolvedService == null || resolvedService.serviceInfo == null) {
            android.util.Log.w(LOG_TAG, "Service recognizer doesn't handle android.intent.action.ACTIVITY_RECOGNIZER, ignoring!");
            return;
        }
        android.os.Bundle metaData = resolvedService.serviceInfo.metaData;
        if (metaData == null) {
            return;
        }
        java.lang.String tagsList = metaData.getString(ACTIVITY_RECOGNITION_TAGS);
        if (!android.text.TextUtils.isEmpty(tagsList)) {
            android.os.PackageTagsList packageTagsList = new android.os.PackageTagsList.Builder(1).add(resolvedService.serviceInfo.packageName, java.util.Arrays.asList(tagsList.split(ACTIVITY_RECOGNITION_TAGS_SEPARATOR))).build();
            synchronized (this.mLock) {
                updateAllowListedTagsForPackageLocked(android.os.UserHandle.getAppId(resolvedService.serviceInfo.applicationInfo.uid), packageTagsList, this.mActivityRecognitionTags);
            }
        }
    }

    private static void updateAllowListedTagsForPackageLocked(int appId, android.os.PackageTagsList packageTagsList, java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.os.PackageTagsList> datastore) {
        datastore.put(java.lang.Integer.valueOf(appId), packageTagsList);
    }

    private static boolean isDatasourceAttributionTag(int uid, java.lang.String packageName, java.lang.String attributionTag, java.util.Map<java.lang.Integer, android.os.PackageTagsList> mappedOps) {
        android.os.PackageTagsList appIdTags = mappedOps.get(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(uid)));
        return appIdTags != null && appIdTags.contains(packageName, attributionTag);
    }

    private static int resolveLocationOp(int code) {
        switch (code) {
            case 0:
                return 109;
            case 1:
                return 108;
            default:
                return code;
        }
    }

    private static int resolveArOp(int code) {
        if (code == 79) {
            return 113;
        }
        return code;
    }

    private int resolveRecordAudioOp(int code, int uid) {
        if (code != 102 || !this.mIsHotwordDetectionServiceRequired) {
            return code;
        }
        android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity hotwordDetectionServiceIdentity = this.mVoiceInteractionManagerInternal.getHotwordDetectionServiceIdentity();
        if (hotwordDetectionServiceIdentity != null && uid == hotwordDetectionServiceIdentity.getIsolatedUid()) {
            return code;
        }
        return 27;
    }

    private int resolveSandboxedServiceOp(int code, int uid) {
        android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity hotwordDetectionServiceIdentity;
        if (android.os.Process.isIsolated(uid) && ((code == 27 || code == 26) && (hotwordDetectionServiceIdentity = this.mVoiceInteractionManagerInternal.getHotwordDetectionServiceIdentity()) != null && uid == hotwordDetectionServiceIdentity.getIsolatedUid())) {
            switch (code) {
            }
        }
        return code;
    }

    private int resolveUid(int code, int uid) {
        android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity hotwordDetectionServiceIdentity;
        if (android.os.Process.isIsolated(uid)) {
            if ((code == 27 || code == 102 || code == 26) && (hotwordDetectionServiceIdentity = this.mVoiceInteractionManagerInternal.getHotwordDetectionServiceIdentity()) != null && uid == hotwordDetectionServiceIdentity.getIsolatedUid()) {
                return hotwordDetectionServiceIdentity.getOwnerUid();
            }
            return uid;
        }
        return uid;
    }
}
