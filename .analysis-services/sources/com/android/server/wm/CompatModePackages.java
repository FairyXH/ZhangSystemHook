package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public final class CompatModePackages {
    private static final int COMPAT_FLAG_DONT_ASK = 1;
    private static final int COMPAT_FLAG_ENABLED = 2;
    public static final long DOWNSCALED = 168419799;
    public static final long DOWNSCALED_INVERSE = 273564678;
    public static final long DOWNSCALE_30 = 189970040;
    public static final long DOWNSCALE_35 = 189969749;
    public static final long DOWNSCALE_40 = 189970038;
    public static final long DOWNSCALE_45 = 189969782;
    public static final long DOWNSCALE_50 = 176926741;
    public static final long DOWNSCALE_55 = 189970036;
    public static final long DOWNSCALE_60 = 176926771;
    public static final long DOWNSCALE_65 = 189969744;
    public static final long DOWNSCALE_70 = 176926829;
    public static final long DOWNSCALE_75 = 189969779;
    public static final long DOWNSCALE_80 = 176926753;
    public static final long DOWNSCALE_85 = 189969734;
    public static final long DOWNSCALE_90 = 182811243;
    private static final long DO_NOT_DOWNSCALE_TO_1080P_ON_TV = 157629738;
    private static final int MSG_WRITE = 300;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private final android.util.AtomicFile mFile;
    private final com.android.server.wm.CompatModePackages.CompatHandler mHandler;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final java.util.HashMap<java.lang.String, java.lang.Integer> mPackages = new java.util.HashMap<>();
    private final android.util.SparseBooleanArray mLegacyScreenCompatPackages = new android.util.SparseBooleanArray();
    private final android.util.SparseArray<com.android.server.wm.CompatScaleProvider> mProviders = new android.util.SparseArray<>();
    com.android.server.wm.ICompatModePackagesExt mCompatModeExt = (com.android.server.wm.ICompatModePackagesExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ICompatModePackagesExt.class).create();

    private final class CompatHandler extends android.os.Handler {
        public CompatHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 300:
                    com.android.server.wm.CompatModePackages.this.saveCompatModes();
                    break;
            }
        }
    }

    public CompatModePackages(com.android.server.wm.ActivityTaskManagerService service, java.io.File systemDir, android.os.Handler handler) {
        java.lang.String pkg;
        this.mService = service;
        this.mFile = new android.util.AtomicFile(new java.io.File(systemDir, "packages-compat.xml"), "compat-mode");
        this.mHandler = new com.android.server.wm.CompatModePackages.CompatHandler(handler.getLooper());
        java.io.FileInputStream fis = null;
        try {
            try {
                try {
                    fis = this.mFile.openRead();
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(fis);
                    int eventType = parser.getEventType();
                    while (eventType != 2 && eventType != 1) {
                        eventType = parser.next();
                    }
                    if (eventType == 1) {
                        if (fis != null) {
                            try {
                                fis.close();
                                return;
                            } catch (java.io.IOException e) {
                                return;
                            }
                        }
                        return;
                    }
                    java.lang.String tagName = parser.getName();
                    if ("compat-packages".equals(tagName)) {
                        int eventType2 = parser.next();
                        do {
                            if (eventType2 == 2) {
                                java.lang.String tagName2 = parser.getName();
                                if (parser.getDepth() == 2 && "pkg".equals(tagName2) && (pkg = parser.getAttributeValue((java.lang.String) null, "name")) != null) {
                                    int modeInt = parser.getAttributeInt((java.lang.String) null, com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, 0);
                                    this.mPackages.put(pkg, java.lang.Integer.valueOf(modeInt));
                                }
                            }
                            eventType2 = parser.next();
                        } while (eventType2 != 1);
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (java.io.IOException e2) {
                }
            } catch (java.io.IOException e3) {
                if (fis != null) {
                    android.util.Slog.w(TAG, "Error reading compat-packages", e3);
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (org.xmlpull.v1.XmlPullParserException e4) {
                android.util.Slog.w(TAG, "Error reading compat-packages", e4);
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (java.lang.Throwable th) {
            if (0 != 0) {
                try {
                    fis.close();
                } catch (java.io.IOException e5) {
                }
            }
            throw th;
        }
    }

    public java.util.HashMap<java.lang.String, java.lang.Integer> getPackages() {
        return this.mPackages;
    }

    private int getPackageFlags(java.lang.String packageName) {
        java.lang.Integer flags = this.mPackages.get(packageName);
        if (flags != null) {
            return flags.intValue();
        }
        return 0;
    }

    public void handlePackageDataClearedLocked(java.lang.String packageName) {
        removePackage(packageName);
    }

    public void handlePackageUninstalledLocked(java.lang.String packageName) {
        removePackage(packageName);
    }

    private void removePackage(java.lang.String packageName) {
        if (this.mPackages.containsKey(packageName)) {
            this.mPackages.remove(packageName);
            scheduleWrite();
        }
        this.mLegacyScreenCompatPackages.delete(packageName.hashCode());
    }

    public void handlePackageAddedLocked(java.lang.String packageName, boolean updated) {
        android.content.pm.ApplicationInfo ai = null;
        boolean mayCompat = false;
        try {
            ai = android.app.AppGlobals.getPackageManager().getApplicationInfo(packageName, 0L, 0);
        } catch (android.os.RemoteException e) {
        }
        if (ai == null) {
            return;
        }
        android.content.res.CompatibilityInfo ci = compatibilityInfoForPackageLocked(ai);
        if (!ci.alwaysSupportsScreen() && !ci.neverSupportsScreen()) {
            mayCompat = true;
        }
        if (updated && !mayCompat && this.mPackages.containsKey(packageName)) {
            this.mPackages.remove(packageName);
            scheduleWrite();
        }
    }

    private void scheduleWrite() {
        this.mHandler.removeMessages(300);
        android.os.Message msg = this.mHandler.obtainMessage(300);
        this.mHandler.sendMessageDelayed(msg, 10000L);
    }

    boolean useLegacyScreenCompatMode(java.lang.String packageName) {
        if (this.mLegacyScreenCompatPackages.size() == 0) {
            return false;
        }
        return this.mLegacyScreenCompatPackages.get(packageName.hashCode());
    }

    public android.content.res.CompatibilityInfo compatibilityInfoForPackageLocked(android.content.pm.ApplicationInfo ai) {
        float appScale;
        boolean forceCompat = getPackageCompatModeEnabledLocked(ai);
        android.content.res.CompatibilityInfo.CompatScale compatScale = getCompatScaleFromProvider(ai.packageName, ai.uid);
        if (compatScale != null) {
            appScale = compatScale.mScaleFactor;
        } else {
            appScale = getCompatScale(ai.packageName, ai.uid, false);
        }
        float densityScale = compatScale != null ? compatScale.mDensityScaleFactor : appScale;
        android.content.res.Configuration config = this.mService.getGlobalConfiguration();
        android.content.res.CompatibilityInfo info = new android.content.res.CompatibilityInfo(ai, config.screenLayout, config.smallestScreenWidthDp, forceCompat, appScale, densityScale);
        if (ai.flags != 0 && ai.sourceDir != null) {
            if (!info.supportsScreen() && !com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(ai.packageName)) {
                android.util.Slog.i(TAG, "Use legacy screen compat mode: " + ai.packageName);
                this.mLegacyScreenCompatPackages.put(ai.packageName.hashCode(), true);
            } else if (this.mLegacyScreenCompatPackages.size() > 0) {
                this.mLegacyScreenCompatPackages.delete(ai.packageName.hashCode());
            }
        }
        return info;
    }

    float getCompatScale(java.lang.String packageName, int uid) {
        return getCompatScale(packageName, uid, true);
    }

    private android.content.res.CompatibilityInfo.CompatScale getCompatScaleFromProvider(java.lang.String packageName, int uid) {
        for (int i = 0; i < this.mProviders.size(); i++) {
            com.android.server.wm.CompatScaleProvider provider = this.mProviders.valueAt(i);
            android.content.res.CompatibilityInfo.CompatScale compatScale = provider.getCompatScale(packageName, uid);
            if (compatScale != null) {
                return compatScale;
            }
        }
        return null;
    }

    private float getCompatScale(java.lang.String packageName, int uid, boolean checkProviders) {
        android.content.res.CompatibilityInfo.CompatScale compatScale;
        float appScale = this.mCompatModeExt != null ? this.mCompatModeExt.getCompatScale(packageName, uid) : 1.0f;
        if (appScale != 1.0f) {
            return appScale;
        }
        if (checkProviders && (compatScale = getCompatScaleFromProvider(packageName, uid)) != null) {
            return compatScale.mScaleFactor;
        }
        android.os.UserHandle userHandle = android.os.UserHandle.getUserHandleForUid(uid);
        boolean isDownscaledEnabled = android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALED, packageName, userHandle);
        boolean isDownscaledInverseEnabled = android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALED_INVERSE, packageName, userHandle);
        if (isDownscaledEnabled || isDownscaledInverseEnabled) {
            float scalingFactor = getScalingFactor(packageName, userHandle);
            if (scalingFactor != 1.0f) {
                return isDownscaledInverseEnabled ? scalingFactor : 1.0f / scalingFactor;
            }
        }
        if (this.mService.mHasLeanbackFeature) {
            android.content.res.Configuration config = this.mService.getGlobalConfiguration();
            float density = config.densityDpi / 160.0f;
            int smallestScreenWidthPx = (int) ((config.smallestScreenWidthDp * density) + 0.5f);
            if (smallestScreenWidthPx > 1080 && !android.app.compat.CompatChanges.isChangeEnabled(DO_NOT_DOWNSCALE_TO_1080P_ON_TV, packageName, userHandle)) {
                return smallestScreenWidthPx / 1080.0f;
            }
        }
        return 1.0f;
    }

    void registerCompatScaleProvider(int id, com.android.server.wm.CompatScaleProvider provider) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mProviders.contains(id)) {
                    throw new java.lang.IllegalArgumentException("Duplicate id provided: " + id);
                }
                if (provider == null) {
                    throw new java.lang.IllegalArgumentException("The passed CompatScaleProvider can not be null");
                }
                if (!com.android.server.wm.CompatScaleProvider.isValidOrderId(id)) {
                    throw new java.lang.IllegalArgumentException("Provided id " + id + " is not in range of valid ids for system services [0,2]");
                }
                this.mProviders.put(id, provider);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void unregisterCompatScaleProvider(int id) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!this.mProviders.contains(id)) {
                    throw new java.lang.IllegalArgumentException("CompatScaleProvider with id (" + id + ") is not registered");
                }
                this.mProviders.remove(id);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    private static float getScalingFactor(java.lang.String packageName, android.os.UserHandle userHandle) {
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_90, packageName, userHandle)) {
            return 0.9f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_85, packageName, userHandle)) {
            return 0.85f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_80, packageName, userHandle)) {
            return 0.8f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_75, packageName, userHandle)) {
            return 0.75f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_70, packageName, userHandle)) {
            return 0.7f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_65, packageName, userHandle)) {
            return 0.65f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_60, packageName, userHandle)) {
            return 0.6f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_55, packageName, userHandle)) {
            return 0.55f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_50, packageName, userHandle)) {
            return 0.5f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_45, packageName, userHandle)) {
            return 0.45f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_40, packageName, userHandle)) {
            return 0.4f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_35, packageName, userHandle)) {
            return 0.35f;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(DOWNSCALE_30, packageName, userHandle)) {
            return 0.3f;
        }
        return 1.0f;
    }

    public int computeCompatModeLocked(android.content.pm.ApplicationInfo ai) {
        android.content.res.CompatibilityInfo info = compatibilityInfoForPackageLocked(ai);
        if (info.alwaysSupportsScreen()) {
            return -2;
        }
        if (info.neverSupportsScreen()) {
            return -1;
        }
        return getPackageCompatModeEnabledLocked(ai) ? 1 : 0;
    }

    public boolean getPackageAskCompatModeLocked(java.lang.String packageName) {
        return (getPackageFlags(packageName) & 1) == 0;
    }

    public void setPackageAskCompatModeLocked(java.lang.String packageName, boolean ask) {
        setPackageFlagLocked(packageName, 1, ask);
    }

    private boolean getPackageCompatModeEnabledLocked(android.content.pm.ApplicationInfo ai) {
        return (getPackageFlags(ai.packageName) & 2) != 0;
    }

    private void setPackageFlagLocked(java.lang.String packageName, int flag, boolean set) {
        int curFlags = getPackageFlags(packageName);
        int newFlags = set ? (~flag) & curFlags : curFlags | flag;
        if (curFlags != newFlags) {
            if (newFlags != 0) {
                this.mPackages.put(packageName, java.lang.Integer.valueOf(newFlags));
            } else {
                this.mPackages.remove(packageName);
            }
            scheduleWrite();
        }
    }

    public int getPackageScreenCompatModeLocked(java.lang.String packageName) {
        android.content.pm.ApplicationInfo ai = null;
        try {
            ai = android.app.AppGlobals.getPackageManager().getApplicationInfo(packageName, 0L, 0);
        } catch (android.os.RemoteException e) {
        }
        if (ai == null) {
            return -3;
        }
        return computeCompatModeLocked(ai);
    }

    public void setPackageScreenCompatModeLocked(java.lang.String packageName, int mode) {
        android.content.pm.ApplicationInfo ai = null;
        try {
            ai = android.app.AppGlobals.getPackageManager().getApplicationInfo(packageName, 0L, 0);
        } catch (android.os.RemoteException e) {
        }
        if (ai == null) {
            android.util.Slog.w(TAG, "setPackageScreenCompatMode failed: unknown package " + packageName);
        } else {
            setPackageScreenCompatModeLocked(ai, mode);
        }
    }

    void setPackageScreenCompatModeLocked(android.content.pm.ApplicationInfo ai, int mode) {
        boolean enable;
        int newFlags;
        final java.lang.String packageName = ai.packageName;
        int curFlags = getPackageFlags(packageName);
        switch (mode) {
            case 0:
                enable = false;
                break;
            case 1:
                enable = true;
                break;
            case 2:
                enable = (curFlags & 2) == 0;
                break;
            default:
                android.util.Slog.w(TAG, "Unknown screen compat mode req #" + mode + "; ignoring");
                return;
        }
        if (enable) {
            newFlags = curFlags | 2;
        } else {
            newFlags = curFlags & (-3);
        }
        android.content.res.CompatibilityInfo ci = compatibilityInfoForPackageLocked(ai);
        if (ci.alwaysSupportsScreen()) {
            android.util.Slog.w(TAG, "Ignoring compat mode change of " + packageName + "; compatibility never needed");
            newFlags = 0;
        }
        if (ci.neverSupportsScreen()) {
            android.util.Slog.w(TAG, "Ignoring compat mode change of " + packageName + "; compatibility always needed");
            newFlags = 0;
        }
        if (newFlags != curFlags) {
            if (newFlags != 0) {
                this.mPackages.put(packageName, java.lang.Integer.valueOf(newFlags));
            } else {
                this.mPackages.remove(packageName);
            }
            android.content.res.CompatibilityInfo ci2 = compatibilityInfoForPackageLocked(ai);
            scheduleWrite();
            final java.util.ArrayList<com.android.server.wm.WindowProcessController> restartedApps = new java.util.ArrayList<>();
            this.mService.mRootWindowContainer.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.CompatModePackages$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.CompatModePackages.lambda$setPackageScreenCompatModeLocked$0(packageName, restartedApps, (com.android.server.wm.WindowState) obj);
                }
            }, true);
            android.util.SparseArray<com.android.server.wm.WindowProcessController> pidMap = this.mService.mProcessMap.getPidMap();
            for (int i = pidMap.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowProcessController app = pidMap.valueAt(i);
                if (app.containsPackage(packageName) && !restartedApps.contains(app)) {
                    try {
                        if (app.hasThread()) {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                                java.lang.String protoLogParam0 = java.lang.String.valueOf(app.mName);
                                java.lang.String protoLogParam1 = java.lang.String.valueOf(ci2);
                                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -74949168947384056L, 0, null, protoLogParam0, protoLogParam1);
                            }
                            app.getThread().updatePackageCompatibilityInfo(packageName, ci2);
                        }
                    } catch (java.lang.Exception e) {
                    }
                }
            }
        }
    }

    static /* synthetic */ void lambda$setPackageScreenCompatModeLocked$0(java.lang.String packageName, java.util.ArrayList restartedApps, com.android.server.wm.WindowState w) {
        com.android.server.wm.ActivityRecord ar = w.mActivityRecord;
        if (ar != null) {
            if (ar.packageName.equals(packageName) && !restartedApps.contains(ar.app)) {
                ar.restartProcessIfVisible();
                restartedApps.add(ar.app);
                return;
            }
            return;
        }
        if (w.getProcess().mInfo.packageName.equals(packageName)) {
            w.updateGlobalScale();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCompatModes() {
        java.util.HashMap<java.lang.String, java.lang.Integer> pkgs;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                pkgs = new java.util.HashMap<>(this.mPackages);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        java.io.FileOutputStream fos = null;
        try {
            fos = this.mFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
            out.startDocument((java.lang.String) null, true);
            out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            out.startTag((java.lang.String) null, "compat-packages");
            android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : pkgs.entrySet()) {
                java.lang.String pkg = entry.getKey();
                int mode = entry.getValue().intValue();
                if (mode != 0) {
                    android.content.pm.ApplicationInfo ai = null;
                    try {
                        ai = pm.getApplicationInfo(pkg, 0L, 0);
                    } catch (android.os.RemoteException e) {
                    }
                    if (ai != null) {
                        android.content.res.CompatibilityInfo info = compatibilityInfoForPackageLocked(ai);
                        if (!info.alwaysSupportsScreen() && !info.neverSupportsScreen()) {
                            out.startTag((java.lang.String) null, "pkg");
                            out.attribute((java.lang.String) null, "name", pkg);
                            out.attributeInt((java.lang.String) null, com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, mode);
                            out.endTag((java.lang.String) null, "pkg");
                        }
                    }
                }
            }
            out.endTag((java.lang.String) null, "compat-packages");
            out.endDocument();
            this.mFile.finishWrite(fos);
        } catch (java.io.IOException e1) {
            android.util.Slog.w(TAG, "Error writing compat packages", e1);
            if (fos != null) {
                this.mFile.failWrite(fos);
            }
        }
    }
}
