package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class AppStateHelper {
    private static final long ACTIVE_NETWORK_DURATION_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(10);
    private final android.content.Context mContext;

    public AppStateHelper(android.content.Context context) {
        this.mContext = context;
    }

    private static boolean isPackageLoaded(android.app.ActivityManager.RunningAppProcessInfo info, java.lang.String packageName) {
        return com.android.internal.util.ArrayUtils.contains(info.pkgList, packageName) || com.android.internal.util.ArrayUtils.contains(info.pkgDeps, packageName);
    }

    private int getImportance(java.lang.String packageName) {
        android.app.ActivityManager am = (android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class);
        return am.getPackageImportance(packageName);
    }

    private boolean hasAudioFocus(java.lang.String packageName) {
        android.media.IAudioService audioService = android.media.IAudioService.Stub.asInterface(android.os.ServiceManager.getService("audio"));
        try {
            java.util.List<android.media.AudioFocusInfo> focusInfos = audioService.getFocusStack();
            int size = focusInfos.size();
            java.lang.String audioFocusPackage = size > 0 ? focusInfos.get(size - 1).getPackageName() : null;
            return android.text.TextUtils.equals(packageName, audioFocusPackage);
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    private boolean hasVoiceCall() {
        android.media.AudioManager am = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
        try {
            int audioMode = am.getMode();
            return audioMode == 2 || audioMode == 3;
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    private boolean isRecordingAudio(java.lang.String packageName) {
        android.media.AudioManager am = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
        try {
            for (android.media.AudioRecordingConfiguration arc : am.getActiveRecordingConfigurations()) {
                if (android.text.TextUtils.equals(arc.getClientPackageName(), packageName)) {
                    return true;
                }
            }
            return false;
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    private boolean isAppForeground(java.lang.String packageName) {
        return getImportance(packageName) <= 125;
    }

    public boolean isAppTopVisible(java.lang.String packageName) {
        return getImportance(packageName) <= 100;
    }

    private boolean hasActiveAudio(java.lang.String packageName) {
        return hasAudioFocus(packageName) || isRecordingAudio(packageName);
    }

    private boolean hasActiveNetwork(java.util.List<java.lang.String> packageNames, int networkType) {
        android.content.pm.IPackageManager pm = android.app.ActivityThread.getPackageManager();
        android.app.usage.NetworkStatsManager nsm = (android.app.usage.NetworkStatsManager) this.mContext.getSystemService(android.app.usage.NetworkStatsManager.class);
        long endTime = java.lang.System.currentTimeMillis();
        long startTime = endTime - ACTIVE_NETWORK_DURATION_MILLIS;
        try {
            android.app.usage.NetworkStats stats = nsm.querySummary(networkType, null, startTime, endTime);
            try {
                android.app.usage.NetworkStats.Bucket bucket = new android.app.usage.NetworkStats.Bucket();
                while (stats.hasNextBucket()) {
                    stats.getNextBucket(bucket);
                    java.lang.String packageName = pm.getNameForUid(bucket.getUid());
                    if (packageNames.contains(packageName) && (bucket.getRxPackets() > 0 || bucket.getTxPackets() > 0)) {
                        if (stats != null) {
                            stats.close();
                            return true;
                        }
                        return true;
                    }
                }
                if (stats != null) {
                    stats.close();
                    return false;
                }
                return false;
            } finally {
            }
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    private static boolean containsAny(java.lang.String[] arr, java.util.List<java.lang.String> which) {
        int s1 = arr.length;
        int s2 = which.size();
        int i = 0;
        int j = 0;
        while (i < s1 && j < s2) {
            int val = arr[i].compareTo(which.get(j));
            if (val == 0) {
                return true;
            }
            if (val < 0) {
                i++;
            } else {
                j++;
            }
        }
        return false;
    }

    private void addLibraryDependency(final android.util.ArraySet<java.lang.String> results, java.util.List<java.lang.String> libPackageNames) {
        android.content.pm.PackageManagerInternal pmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        final java.util.ArrayList<java.lang.String> libraryNames = new java.util.ArrayList<>();
        final java.util.ArrayList<java.lang.String> staticSharedLibraryNames = new java.util.ArrayList<>();
        final java.util.ArrayList<java.lang.String> sdkLibraryNames = new java.util.ArrayList<>();
        for (java.lang.String packageName : libPackageNames) {
            com.android.server.pm.pkg.AndroidPackage pkg = pmInternal.getAndroidPackage(packageName);
            if (pkg != null) {
                libraryNames.addAll(pkg.getLibraryNames());
                java.lang.String libraryName = pkg.getStaticSharedLibraryName();
                if (libraryName != null) {
                    staticSharedLibraryNames.add(libraryName);
                }
                java.lang.String libraryName2 = pkg.getSdkLibraryName();
                if (libraryName2 != null) {
                    sdkLibraryNames.add(libraryName2);
                }
            }
        }
        if (libraryNames.isEmpty() && staticSharedLibraryNames.isEmpty() && sdkLibraryNames.isEmpty()) {
            return;
        }
        java.util.Collections.sort(libraryNames);
        java.util.Collections.sort(sdkLibraryNames);
        java.util.Collections.sort(staticSharedLibraryNames);
        pmInternal.forEachPackageState(new java.util.function.Consumer() { // from class: com.android.server.pm.AppStateHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.AppStateHelper.lambda$addLibraryDependency$0(libraryNames, staticSharedLibraryNames, sdkLibraryNames, results, (com.android.server.pm.pkg.PackageStateInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$addLibraryDependency$0(java.util.ArrayList libraryNames, java.util.ArrayList staticSharedLibraryNames, java.util.ArrayList sdkLibraryNames, android.util.ArraySet results, com.android.server.pm.pkg.PackageStateInternal pkgState) {
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = pkgState.getPkg();
        if (pkg == null) {
            return;
        }
        if (containsAny(pkg.getUsesLibrariesSorted(), libraryNames) || containsAny(pkg.getUsesOptionalLibrariesSorted(), libraryNames) || containsAny(pkg.getUsesStaticLibrariesSorted(), staticSharedLibraryNames) || containsAny(pkg.getUsesSdkLibrariesSorted(), sdkLibraryNames)) {
            results.add(pkg.getPackageName());
        }
    }

    private boolean hasActiveNetwork(java.util.List<java.lang.String> packageNames) {
        if (hasActiveNetwork(packageNames, 1) || hasActiveNetwork(packageNames, 0)) {
            return true;
        }
        return false;
    }

    public boolean hasInteractingApp(java.util.List<java.lang.String> packageNames) {
        for (java.lang.String packageName : packageNames) {
            if (hasActiveAudio(packageName) || isAppTopVisible(packageName)) {
                return true;
            }
        }
        return hasActiveNetwork(packageNames);
    }

    public boolean hasForegroundApp(java.util.List<java.lang.String> packageNames) {
        for (java.lang.String packageName : packageNames) {
            if (isAppForeground(packageName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasTopVisibleApp(java.util.List<java.lang.String> packageNames) {
        for (java.lang.String packageName : packageNames) {
            if (isAppTopVisible(packageName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCall() {
        if (android.os.SystemProperties.getBoolean("debug.pm.gentle_update_test.is_in_call", false)) {
            return true;
        }
        android.telecom.TelecomManager tm = (android.telecom.TelecomManager) this.mContext.getSystemService(android.telecom.TelecomManager.class);
        return tm.isInCall() || hasVoiceCall();
    }

    public java.util.List<java.lang.String> getDependencyPackages(java.util.List<java.lang.String> packageNames) {
        android.util.ArraySet<java.lang.String> results = new android.util.ArraySet<>();
        android.app.ActivityManager am = (android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class);
        for (android.app.ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            for (java.lang.String packageName : packageNames) {
                if (isPackageLoaded(info, packageName)) {
                    for (java.lang.String pkg : info.pkgList) {
                        results.add(pkg);
                    }
                }
            }
        }
        android.app.ActivityManagerInternal amInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        for (java.lang.String packageName2 : packageNames) {
            results.addAll(amInternal.getClientPackages(packageName2));
        }
        addLibraryDependency(results, packageNames);
        return new java.util.ArrayList(results);
    }
}
