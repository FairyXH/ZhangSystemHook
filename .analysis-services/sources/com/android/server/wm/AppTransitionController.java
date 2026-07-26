package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class AppTransitionController {
    private static final java.lang.String TAG = "WindowManager";
    private static final int TYPE_ACTIVITY = 1;
    private static final int TYPE_NONE = 0;
    private static final int TYPE_TASK = 3;
    private static final int TYPE_TASK_FRAGMENT = 2;
    public static com.android.server.wm.IAppTransitionControllerExt sAppTransControllerExt = (com.android.server.wm.IAppTransitionControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IAppTransitionControllerExt.class).create();
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private final com.android.server.wm.WindowManagerService mService;
    private final com.android.server.wm.WallpaperController mWallpaperControllerLocked;
    private android.view.RemoteAnimationDefinition mRemoteAnimationDefinition = null;
    private final android.util.ArrayMap<com.android.server.wm.WindowContainer, java.lang.Integer> mTempTransitionReasons = new android.util.ArrayMap<>();
    private final java.util.ArrayList<com.android.server.wm.WindowContainer> mTempTransitionWindows = new java.util.ArrayList<>();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface TransitContainerType {
    }

    AppTransitionController(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent) {
        this.mService = service;
        this.mDisplayContent = displayContent;
        this.mWallpaperControllerLocked = this.mDisplayContent.mWallpaperController;
    }

    void registerRemoteAnimations(android.view.RemoteAnimationDefinition definition) {
        this.mRemoteAnimationDefinition = definition;
    }

    private com.android.server.wm.WindowState getOldWallpaper() {
        com.android.server.wm.WindowState wallpaperTarget = this.mWallpaperControllerLocked.getWallpaperTarget();
        int firstTransit = this.mDisplayContent.mAppTransition.getFirstAppTransition();
        boolean z = true;
        android.util.ArraySet<com.android.server.wm.WindowContainer> openingWcs = getAnimationTargets(this.mDisplayContent.mOpeningApps, this.mDisplayContent.mClosingApps, true);
        if (wallpaperTarget == null || (!wallpaperTarget.hasWallpaper() && ((firstTransit != 1 && firstTransit != 3) || openingWcs.isEmpty() || openingWcs.valueAt(0).asTask() == null || !this.mWallpaperControllerLocked.isWallpaperVisible()))) {
            z = false;
        }
        boolean showWallpaper = z;
        if (this.mWallpaperControllerLocked.isWallpaperTargetAnimating() || !showWallpaper) {
            return null;
        }
        return wallpaperTarget;
    }

    void handleAppTransitionReady() {
        android.util.ArraySet<com.android.server.wm.ActivityRecord> tmpOpenApps;
        android.util.ArraySet<com.android.server.wm.ActivityRecord> tmpCloseApps;
        this.mTempTransitionReasons.clear();
        if (!transitionGoodToGo(this.mDisplayContent.mOpeningApps, this.mTempTransitionReasons) || !transitionGoodToGo(this.mDisplayContent.mChangingContainers, this.mTempTransitionReasons) || !transitionGoodToGoForTaskFragments()) {
            return;
        }
        boolean isRecentsInOpening = this.mDisplayContent.mOpeningApps.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRecord) obj).isActivityTypeRecents();
            }
        });
        if (!isRecentsInOpening) {
            android.util.ArraySet<com.android.server.wm.WindowContainer> participants = new android.util.ArraySet<>();
            participants.addAll(this.mDisplayContent.mOpeningApps);
            participants.addAll(this.mDisplayContent.mChangingContainers);
            boolean deferForRecents = false;
            int i = 0;
            while (true) {
                if (i >= participants.size()) {
                    break;
                }
                com.android.server.wm.WindowContainer wc = participants.valueAt(i);
                com.android.server.wm.ActivityRecord activity = getAppFromContainer(wc);
                if (activity != null) {
                    if (!activity.isAnimating(2, 8)) {
                        deferForRecents = false;
                        break;
                    }
                    deferForRecents = true;
                }
                i++;
            }
            if (deferForRecents) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -5726018006883159788L, 0, null, null);
                    return;
                }
                return;
            }
        }
        android.os.Trace.traceBegin(32L, "AppTransitionReady");
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 6514556033257323299L, 0, null, null);
        }
        this.mDisplayContent.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.WindowState) obj).cleanupAnimatingExitWindow();
            }
        }, true);
        sAppTransControllerExt.onAppTransitionReady(this.mDisplayContent);
        com.android.server.wm.AppTransition appTransition = this.mDisplayContent.mAppTransition;
        this.mDisplayContent.mNoAnimationNotifyOnTransitionFinished.clear();
        appTransition.removeAppTransitionTimeoutCallbacks();
        this.mDisplayContent.mWallpaperMayChange = false;
        int appCount = this.mDisplayContent.mOpeningApps.size();
        for (int i2 = 0; i2 < appCount; i2++) {
            ((com.android.server.wm.ActivityRecord) this.mDisplayContent.mOpeningApps.valueAtUnchecked(i2)).clearAnimatingFlags();
        }
        int appCount2 = this.mDisplayContent.mChangingContainers.size();
        for (int i3 = 0; i3 < appCount2; i3++) {
            com.android.server.wm.ActivityRecord activity2 = getAppFromContainer((com.android.server.wm.WindowContainer) this.mDisplayContent.mChangingContainers.valueAtUnchecked(i3));
            if (activity2 != null) {
                activity2.clearAnimatingFlags();
            }
        }
        this.mWallpaperControllerLocked.adjustWallpaperWindowsForAppTransitionIfNeeded(this.mDisplayContent.mOpeningApps);
        android.util.ArraySet<com.android.server.wm.ActivityRecord> tmpOpenApps2 = this.mDisplayContent.mOpeningApps;
        android.util.ArraySet<com.android.server.wm.ActivityRecord> tmpCloseApps2 = this.mDisplayContent.mClosingApps;
        if (!this.mDisplayContent.mAtmService.mBackNavigationController.isMonitoringTransition()) {
            tmpOpenApps = tmpOpenApps2;
            tmpCloseApps = tmpCloseApps2;
        } else {
            android.util.ArraySet<com.android.server.wm.ActivityRecord> tmpOpenApps3 = new android.util.ArraySet<>(this.mDisplayContent.mOpeningApps);
            android.util.ArraySet<com.android.server.wm.ActivityRecord> tmpCloseApps3 = new android.util.ArraySet<>(this.mDisplayContent.mClosingApps);
            if (this.mDisplayContent.mAtmService.mBackNavigationController.removeIfContainsBackAnimationTargets(tmpOpenApps3, tmpCloseApps3)) {
                this.mDisplayContent.mAtmService.mBackNavigationController.clearBackAnimations(false);
            }
            tmpOpenApps = tmpOpenApps3;
            tmpCloseApps = tmpCloseApps3;
        }
        int transit = getTransitCompatType(this.mDisplayContent.mAppTransition, tmpOpenApps, tmpCloseApps, this.mDisplayContent.mChangingContainers, this.mWallpaperControllerLocked.getWallpaperTarget(), getOldWallpaper(), this.mDisplayContent.mSkipAppTransitionAnimation);
        if (!sAppTransControllerExt.skipAppTransitionAnimation()) {
            this.mDisplayContent.mSkipAppTransitionAnimation = false;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
            long protoLogParam0 = this.mDisplayContent.mDisplayId;
            java.lang.String protoLogParam1 = java.lang.String.valueOf(appTransition.toString());
            java.lang.String protoLogParam2 = java.lang.String.valueOf(tmpOpenApps);
            java.lang.String protoLogParam3 = java.lang.String.valueOf(tmpCloseApps);
            java.lang.String protoLogParam4 = java.lang.String.valueOf(com.android.server.wm.AppTransition.appTransitionOldToString(transit));
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 3518082157667760495L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1, protoLogParam2, protoLogParam3, protoLogParam4);
        }
        android.util.ArraySet<java.lang.Integer> activityTypes = collectActivityTypes(tmpOpenApps, tmpCloseApps, this.mDisplayContent.mChangingContainers);
        android.util.ArraySet<com.android.server.wm.ActivityRecord> tmpCloseApps4 = tmpCloseApps;
        android.util.ArraySet<com.android.server.wm.ActivityRecord> tmpOpenApps4 = tmpOpenApps;
        com.android.server.wm.ActivityRecord animLpActivity = findAnimLayoutParamsToken(transit, activityTypes, tmpOpenApps, tmpCloseApps, this.mDisplayContent.mChangingContainers);
        com.android.server.wm.ActivityRecord topOpeningApp = getTopApp(tmpOpenApps4, false);
        com.android.server.wm.ActivityRecord topClosingApp = getTopApp(tmpCloseApps4, false);
        com.android.server.wm.ActivityRecord topChangingApp = getTopApp(this.mDisplayContent.mChangingContainers, false);
        android.view.WindowManager.LayoutParams animLp = getAnimLp(animLpActivity);
        if (!sAppTransControllerExt.overrideWithRemoteAnimationIfNeed(this.mDisplayContent, transit, activityTypes, topClosingApp) && !overrideWithTaskFragmentRemoteAnimation(transit, activityTypes, animLpActivity)) {
            unfreezeEmbeddedChangingWindows();
            overrideWithRemoteAnimationIfSet(animLpActivity, transit, activityTypes);
        }
        boolean voiceInteraction = containsVoiceInteraction(this.mDisplayContent.mClosingApps) || containsVoiceInteraction(this.mDisplayContent.mOpeningApps);
        this.mService.mSurfaceAnimationRunner.deferStartingAnimations();
        try {
            applyAnimations(tmpOpenApps4, tmpCloseApps4, transit, animLp, voiceInteraction);
            handleClosingApps();
            handleOpeningApps();
            handleChangingApps(transit);
            handleClosingChangingContainers();
            appTransition.setLastAppTransition(transit, topOpeningApp, topClosingApp, topChangingApp);
            appTransition.getTransitFlags();
            int layoutRedo = appTransition.goodToGo(transit, topOpeningApp);
            appTransition.postAnimationCallback();
            sAppTransControllerExt.handleAppTransitionReady(transit);
            this.mService.mSnapshotController.onTransitionStarting(this.mDisplayContent);
            this.mDisplayContent.mOpeningApps.clear();
            this.mDisplayContent.mClosingApps.clear();
            this.mDisplayContent.mChangingContainers.clear();
            this.mDisplayContent.mUnknownAppVisibilityController.clear();
            this.mDisplayContent.mClosingChangingContainers.clear();
            this.mDisplayContent.setLayoutNeeded();
            this.mDisplayContent.computeImeTarget(true);
            this.mService.mAtmService.mTaskSupervisor.getActivityMetricsLogger().notifyTransitionStarting(this.mTempTransitionReasons);
            android.os.Trace.traceEnd(32L);
            this.mDisplayContent.pendingLayoutChanges |= layoutRedo | 1 | 2;
        } finally {
            appTransition.clear();
            this.mService.mSurfaceAnimationRunner.continueStartingAnimations();
            if (sAppTransControllerExt.skipAppTransitionAnimation()) {
                this.mDisplayContent.mSkipAppTransitionAnimation = false;
            }
        }
    }

    static int getTransitCompatType(com.android.server.wm.AppTransition appTransition, android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps, android.util.ArraySet<com.android.server.wm.WindowContainer> changingContainers, com.android.server.wm.WindowState wallpaperTarget, com.android.server.wm.WindowState oldWallpaper, boolean skipAppTransitionAnimation) {
        int i;
        com.android.server.wm.ActivityRecord topOpeningApp = getTopApp(openingApps, false);
        com.android.server.wm.ActivityRecord topClosingApp = getTopApp(closingApps, true);
        boolean openingAppHasWallpaper = canBeWallpaperTarget(openingApps) && wallpaperTarget != null;
        boolean closingAppHasWallpaper = canBeWallpaperTarget(closingApps) && wallpaperTarget != null;
        switch (appTransition.getKeyguardTransition()) {
            case 7:
                return openingAppHasWallpaper ? 21 : 20;
            case 8:
                if (!closingApps.isEmpty()) {
                    return 6;
                }
                if (!openingApps.isEmpty() && openingApps.valueAt(0).getActivityType() == 5) {
                    return 33;
                }
                return 22;
            case 9:
                return 23;
            default:
                if (topOpeningApp != null && topOpeningApp.getActivityType() == 5) {
                    return 31;
                }
                if (topClosingApp != null && topClosingApp.getActivityType() == 5) {
                    return 32;
                }
                if (skipAppTransitionAnimation) {
                    return -1;
                }
                int flags = appTransition.getTransitFlags();
                int firstTransit = appTransition.getFirstAppTransition();
                if (appTransition.containsTransitRequest(6) && !changingContainers.isEmpty()) {
                    int changingType = getTransitContainerType(changingContainers.valueAt(0));
                    switch (changingType) {
                        case 2:
                            return 30;
                        case 3:
                            return 27;
                        default:
                            throw new java.lang.IllegalStateException("TRANSIT_CHANGE with unrecognized changing type=" + changingType);
                    }
                }
                if ((flags & 16) != 0) {
                    return 26;
                }
                if (firstTransit == 0) {
                    return 0;
                }
                if (com.android.server.wm.AppTransition.isNormalTransit(firstTransit)) {
                    boolean allOpeningVisible = true;
                    boolean allTranslucentOpeningApps = !openingApps.isEmpty();
                    for (int i2 = openingApps.size() - 1; i2 >= 0; i2--) {
                        com.android.server.wm.ActivityRecord activity = openingApps.valueAt(i2);
                        if (!activity.isVisible()) {
                            allOpeningVisible = false;
                            if (activity.fillsParent()) {
                                allTranslucentOpeningApps = false;
                            }
                        }
                    }
                    boolean allTranslucentClosingApps = !closingApps.isEmpty();
                    int i3 = closingApps.size() - 1;
                    while (true) {
                        if (i3 >= 0) {
                            if (closingApps.valueAt(i3).fillsParent()) {
                                allTranslucentClosingApps = false;
                            } else {
                                i3--;
                            }
                        }
                    }
                    if (allTranslucentClosingApps && allOpeningVisible) {
                        return 25;
                    }
                    if (allTranslucentOpeningApps && closingApps.isEmpty()) {
                        return 24;
                    }
                }
                if (sAppTransControllerExt.shouldDoPuttTransition(openingApps)) {
                    return 100;
                }
                if (closingAppHasWallpaper && openingAppHasWallpaper) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -2503124388387340567L, 0, null, null);
                    }
                    switch (firstTransit) {
                        case 1:
                        case 3:
                            return 14;
                        case 2:
                        case 4:
                            return 15;
                    }
                }
                if (oldWallpaper != null && !openingApps.isEmpty() && !openingApps.contains(oldWallpaper.mActivityRecord) && closingApps.contains(oldWallpaper.mActivityRecord) && topClosingApp == oldWallpaper.mActivityRecord) {
                    return 12;
                }
                if (wallpaperTarget != null && wallpaperTarget.isVisible() && openingApps.contains(wallpaperTarget.mActivityRecord) && topOpeningApp == wallpaperTarget.mActivityRecord) {
                    return 13;
                }
                android.util.ArraySet<com.android.server.wm.WindowContainer> openingWcs = getAnimationTargets(openingApps, closingApps, true);
                android.util.ArraySet<com.android.server.wm.WindowContainer> closingWcs = getAnimationTargets(openingApps, closingApps, false);
                com.android.server.wm.WindowContainer<?> openingContainer = !openingWcs.isEmpty() ? openingWcs.valueAt(0) : null;
                com.android.server.wm.WindowContainer<?> closingContainer = closingWcs.isEmpty() ? null : closingWcs.valueAt(0);
                int openingType = getTransitContainerType(openingContainer);
                int closingType = getTransitContainerType(closingContainer);
                if (appTransition.containsTransitRequest(3) && openingType == 3) {
                    return (topOpeningApp == null || !topOpeningApp.isActivityTypeHome()) ? 10 : 11;
                }
                if (appTransition.containsTransitRequest(4) && closingType == 3) {
                    return 11;
                }
                if (!appTransition.containsTransitRequest(1) || sAppTransControllerExt.isPrimaryActivityCloseInCompactWindow(closingApps)) {
                    i = 2;
                } else {
                    if (openingType == 3) {
                        return (appTransition.getTransitFlags() & 32) != 0 ? 16 : 8;
                    }
                    if (openingType == 1) {
                        return 6;
                    }
                    i = 2;
                    if (openingType == 2) {
                        return 28;
                    }
                }
                if (appTransition.containsTransitRequest(i)) {
                    if (closingType == 3) {
                        return 9;
                    }
                    if (closingType == i) {
                        return 29;
                    }
                    if (closingType == 1) {
                        for (int i4 = closingApps.size() - 1; i4 >= 0; i4--) {
                            if (closingApps.valueAt(i4).visibleIgnoringKeyguard) {
                                return 7;
                            }
                        }
                        return -1;
                    }
                }
                if (appTransition.containsTransitRequest(5) && !openingWcs.isEmpty() && !openingApps.isEmpty()) {
                    return 18;
                }
                return 0;
        }
    }

    private static int getTransitContainerType(com.android.server.wm.WindowContainer<?> container) {
        if (container == null) {
            return 0;
        }
        if (container.asTask() != null) {
            return 3;
        }
        if (container.asTaskFragment() != null) {
            return 2;
        }
        if (container.asActivityRecord() == null) {
            return 0;
        }
        return 1;
    }

    private static android.view.WindowManager.LayoutParams getAnimLp(com.android.server.wm.ActivityRecord activity) {
        com.android.server.wm.WindowState mainWindow = activity != null ? activity.findMainWindow() : null;
        if (mainWindow != null) {
            return mainWindow.mAttrs;
        }
        return null;
    }

    android.view.RemoteAnimationAdapter getRemoteAnimationOverride(com.android.server.wm.WindowContainer container, int transit, android.util.ArraySet<java.lang.Integer> activityTypes) {
        android.view.RemoteAnimationDefinition definition;
        if (container != null && (definition = container.getRemoteAnimationDefinition()) != null) {
            android.view.RemoteAnimationAdapter adapter = definition.getAdapter(transit, activityTypes);
            if (adapter != null && com.android.server.wm.AppTransition.isKeyguardOccludeTransitOld(transit)) {
                android.util.Slog.e(TAG, "getRemoteAnimationOverride skip container RemoteAnimation,transit=" + transit + ",container=" + container);
            } else if (adapter != null) {
                return adapter;
            }
        }
        if (this.mRemoteAnimationDefinition != null) {
            return this.mRemoteAnimationDefinition.getAdapter(transit, activityTypes);
        }
        return null;
    }

    private void unfreezeEmbeddedChangingWindows() {
        android.util.ArraySet<com.android.server.wm.WindowContainer> changingContainers = this.mDisplayContent.mChangingContainers;
        for (int i = changingContainers.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = changingContainers.valueAt(i);
            if (wc.isEmbedded()) {
                wc.mSurfaceFreezer.unfreeze(wc.getSyncTransaction());
            }
        }
    }

    private boolean transitionMayContainNonAppWindows(int transit) {
        return com.android.server.wm.NonAppWindowAnimationAdapter.shouldStartNonAppWindowAnimationsForKeyguardExit(transit) || com.android.server.wm.NonAppWindowAnimationAdapter.shouldAttachNavBarToApp(this.mService, this.mDisplayContent, transit) || com.android.server.wm.WallpaperAnimationAdapter.shouldStartWallpaperAnimation(this.mDisplayContent);
    }

    private boolean transitionContainsTaskFragmentWithBoundsOverride() {
        for (int i = this.mDisplayContent.mChangingContainers.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mDisplayContent.mChangingContainers.valueAt(i);
            if (wc.isEmbedded()) {
                return true;
            }
        }
        this.mTempTransitionWindows.clear();
        this.mTempTransitionWindows.addAll(this.mDisplayContent.mClosingApps);
        this.mTempTransitionWindows.addAll(this.mDisplayContent.mOpeningApps);
        boolean containsTaskFragmentWithBoundsOverride = false;
        int i2 = this.mTempTransitionWindows.size() - 1;
        while (true) {
            if (i2 < 0) {
                break;
            }
            com.android.server.wm.ActivityRecord r = this.mTempTransitionWindows.get(i2).asActivityRecord();
            com.android.server.wm.TaskFragment tf = r.getTaskFragment();
            if (tf != null && tf.isEmbeddedWithBoundsOverride()) {
                containsTaskFragmentWithBoundsOverride = true;
                break;
            }
            i2--;
        }
        this.mTempTransitionWindows.clear();
        return containsTaskFragmentWithBoundsOverride;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x006a, code lost:
    
        r0 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.android.server.wm.Task findParentTaskForAllEmbeddedWindows() {
        /*
            r7 = this;
            java.util.ArrayList<com.android.server.wm.WindowContainer> r0 = r7.mTempTransitionWindows
            r0.clear()
            java.util.ArrayList<com.android.server.wm.WindowContainer> r0 = r7.mTempTransitionWindows
            com.android.server.wm.DisplayContent r1 = r7.mDisplayContent
            android.util.ArraySet<com.android.server.wm.ActivityRecord> r1 = r1.mClosingApps
            r0.addAll(r1)
            java.util.ArrayList<com.android.server.wm.WindowContainer> r0 = r7.mTempTransitionWindows
            com.android.server.wm.DisplayContent r1 = r7.mDisplayContent
            android.util.ArraySet<com.android.server.wm.ActivityRecord> r1 = r1.mOpeningApps
            r0.addAll(r1)
            java.util.ArrayList<com.android.server.wm.WindowContainer> r0 = r7.mTempTransitionWindows
            com.android.server.wm.DisplayContent r1 = r7.mDisplayContent
            android.util.ArraySet<com.android.server.wm.WindowContainer> r1 = r1.mChangingContainers
            r0.addAll(r1)
            r0 = 0
            java.util.ArrayList<com.android.server.wm.WindowContainer> r1 = r7.mTempTransitionWindows
            int r1 = r1.size()
            int r1 = r1 + (-1)
        L29:
            if (r1 < 0) goto L6c
            java.util.ArrayList<com.android.server.wm.WindowContainer> r2 = r7.mTempTransitionWindows
            java.lang.Object r2 = r2.get(r1)
            com.android.server.wm.WindowContainer r2 = (com.android.server.wm.WindowContainer) r2
            com.android.server.wm.ActivityRecord r2 = getAppFromContainer(r2)
            if (r2 != 0) goto L3b
            r0 = 0
            goto L6c
        L3b:
            com.android.server.wm.Task r3 = r2.getTask()
            if (r3 == 0) goto L6a
            boolean r4 = r3.inPinnedWindowingMode()
            if (r4 == 0) goto L48
            goto L6a
        L48:
            if (r0 == 0) goto L4e
            if (r0 == r3) goto L4e
            r0 = 0
            goto L6c
        L4e:
            com.android.server.wm.ActivityRecord r4 = r3.getRootActivity()
            if (r4 != 0) goto L56
            r0 = 0
            goto L6c
        L56:
            int r5 = r2.getUid()
            int r6 = r3.effectiveUid
            if (r5 == r6) goto L66
            boolean r5 = r2.isEmbedded()
            if (r5 != 0) goto L66
            r0 = 0
            goto L6c
        L66:
            r0 = r3
            int r1 = r1 + (-1)
            goto L29
        L6a:
            r0 = 0
        L6c:
            java.util.ArrayList<com.android.server.wm.WindowContainer> r1 = r7.mTempTransitionWindows
            r1.clear()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.AppTransitionController.findParentTaskForAllEmbeddedWindows():com.android.server.wm.Task");
    }

    private android.window.ITaskFragmentOrganizer findTaskFragmentOrganizer(com.android.server.wm.Task task) {
        if (task == null) {
            return null;
        }
        final android.window.ITaskFragmentOrganizer[] organizer = new android.window.ITaskFragmentOrganizer[1];
        boolean hasMultipleOrganizers = task.forAllLeafTaskFragments(new java.util.function.Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.AppTransitionController.lambda$findTaskFragmentOrganizer$0(organizer, (com.android.server.wm.TaskFragment) obj);
            }
        });
        if (hasMultipleOrganizers) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[4]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 855146509305002043L, 0, null, null);
            }
            return null;
        }
        return organizer[0];
    }

    static /* synthetic */ boolean lambda$findTaskFragmentOrganizer$0(android.window.ITaskFragmentOrganizer[] organizer, com.android.server.wm.TaskFragment taskFragment) {
        android.window.ITaskFragmentOrganizer tfOrganizer = taskFragment.getTaskFragmentOrganizer();
        if (tfOrganizer == null) {
            return false;
        }
        if (organizer[0] != null && !organizer[0].asBinder().equals(tfOrganizer.asBinder())) {
            return true;
        }
        organizer[0] = tfOrganizer;
        return false;
    }

    private boolean overrideWithTaskFragmentRemoteAnimation(int transit, android.util.ArraySet<java.lang.Integer> activityTypes, com.android.server.wm.ActivityRecord animLpActivity) {
        android.view.RemoteAnimationDefinition definition;
        if (transitionMayContainNonAppWindows(transit) || !transitionContainsTaskFragmentWithBoundsOverride()) {
            return false;
        }
        final com.android.server.wm.Task task = findParentTaskForAllEmbeddedWindows();
        android.window.ITaskFragmentOrganizer organizer = findTaskFragmentOrganizer(task);
        android.view.RemoteAnimationAdapter adapter = null;
        if (organizer != null) {
            definition = this.mDisplayContent.mAtmService.mTaskFragmentOrganizerController.getRemoteAnimationDefinition(organizer);
        } else {
            definition = null;
        }
        if (definition != null) {
            adapter = definition.getAdapter(transit, activityTypes);
        }
        if (adapter == null) {
            return false;
        }
        if (com.android.server.wm.AppTransition.isKeyguardOccludeTransitOld(transit)) {
            android.util.Slog.e(TAG, "Override with TaskFragment remote animation for transit=" + com.android.server.wm.AppTransition.appTransitionOldToString(transit) + ",task=" + task);
        }
        this.mDisplayContent.mAppTransition.overridePendingAppTransitionRemote(adapter, false, true);
        sAppTransControllerExt.overrideTaskFragmentAnimationIfNeed(this.mDisplayContent, task, animLpActivity);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(com.android.server.wm.AppTransition.appTransitionOldToString(transit));
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 59396412370137517L, 0, null, protoLogParam0);
        }
        int organizerUid = this.mDisplayContent.mAtmService.mTaskFragmentOrganizerController.getTaskFragmentOrganizerUid(organizer);
        boolean shouldDisableInputForRemoteAnimation = !task.isFullyTrustedEmbedding(organizerUid);
        com.android.server.wm.RemoteAnimationController remoteAnimationController = this.mDisplayContent.mAppTransition.getRemoteAnimationController();
        if (shouldDisableInputForRemoteAnimation && remoteAnimationController != null) {
            remoteAnimationController.setOnRemoteAnimationReady(new java.lang.Runnable() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.wm.AppTransitionController.lambda$overrideWithTaskFragmentRemoteAnimation$2(task);
                }
            });
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[0]) {
                long protoLogParam02 = task.mTaskId;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 2280055488397326910L, 1, null, java.lang.Long.valueOf(protoLogParam02));
            }
        }
        return true;
    }

    static /* synthetic */ void lambda$overrideWithTaskFragmentRemoteAnimation$2(com.android.server.wm.Task task) {
        java.util.function.Consumer<com.android.server.wm.ActivityRecord> updateActivities = new java.util.function.Consumer() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.ActivityRecord) obj).setDropInputForAnimation(true);
            }
        };
        task.forAllActivities(updateActivities);
    }

    private void overrideWithRemoteAnimationIfSet(com.android.server.wm.ActivityRecord animLpActivity, int transit, android.util.ArraySet<java.lang.Integer> activityTypes) {
        android.view.RemoteAnimationAdapter adapter;
        if (com.android.server.wm.AppTransition.isKeyguardOccludeTransitOld(transit) && this.mDisplayContent.mAppTransition.getRemoteAnimationController() != null) {
            android.util.Slog.e(TAG, "overrideWithRemoteAnimationIfSet RemoteAnimationController not null !! ,transit=" + com.android.server.wm.AppTransition.appTransitionOldToString(transit));
        }
        android.view.RemoteAnimationAdapter adapter2 = null;
        if (transit != 26) {
            if (com.android.server.wm.AppTransition.isKeyguardGoingAwayTransitOld(transit)) {
                if (this.mRemoteAnimationDefinition != null) {
                    adapter = this.mRemoteAnimationDefinition.getAdapter(transit, activityTypes);
                } else {
                    adapter = null;
                }
                adapter2 = adapter;
            } else if (this.mDisplayContent.mAppTransition.getRemoteAnimationController() == null) {
                adapter2 = getRemoteAnimationOverride(animLpActivity, transit, activityTypes);
            }
        }
        if (adapter2 != null) {
            this.mDisplayContent.mAppTransition.overridePendingAppTransitionRemote(adapter2);
        }
    }

    static com.android.server.wm.Task findRootTaskFromContainer(com.android.server.wm.WindowContainer wc) {
        return wc.asTaskFragment() != null ? wc.asTaskFragment().getRootTask() : wc.asActivityRecord().getRootTask();
    }

    static com.android.server.wm.ActivityRecord getAppFromContainer(com.android.server.wm.WindowContainer wc) {
        return wc.asTaskFragment() != null ? wc.asTaskFragment().getTopNonFinishingActivity() : wc.asActivityRecord();
    }

    private com.android.server.wm.ActivityRecord findAnimLayoutParamsToken(final int transit, final android.util.ArraySet<java.lang.Integer> activityTypes, android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps, android.util.ArraySet<com.android.server.wm.WindowContainer> changingApps) {
        com.android.server.wm.ActivityRecord result = lookForHighestTokenWithFilter(closingApps, openingApps, changingApps, new java.util.function.Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.AppTransitionController.lambda$findAnimLayoutParamsToken$3(transit, activityTypes, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (result != null) {
            return result;
        }
        com.android.server.wm.ActivityRecord result2 = lookForHighestTokenWithFilter(closingApps, openingApps, changingApps, new java.util.function.Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.AppTransitionController.lambda$findAnimLayoutParamsToken$4((com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (result2 != null) {
            return result2;
        }
        return lookForHighestTokenWithFilter(closingApps, openingApps, changingApps, new java.util.function.Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.AppTransitionController.lambda$findAnimLayoutParamsToken$5((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$findAnimLayoutParamsToken$3(int transit, android.util.ArraySet activityTypes, com.android.server.wm.ActivityRecord w) {
        return w.getRemoteAnimationDefinition() != null && w.getRemoteAnimationDefinition().hasTransition(transit, activityTypes);
    }

    static /* synthetic */ boolean lambda$findAnimLayoutParamsToken$4(com.android.server.wm.ActivityRecord w) {
        return w.fillsParent() && w.findMainWindow() != null;
    }

    static /* synthetic */ boolean lambda$findAnimLayoutParamsToken$5(com.android.server.wm.ActivityRecord w) {
        return w.findMainWindow() != null;
    }

    private static android.util.ArraySet<java.lang.Integer> collectActivityTypes(android.util.ArraySet<com.android.server.wm.ActivityRecord> array1, android.util.ArraySet<com.android.server.wm.ActivityRecord> array2, android.util.ArraySet<com.android.server.wm.WindowContainer> array3) {
        android.util.ArraySet<java.lang.Integer> result = new android.util.ArraySet<>();
        for (int i = array1.size() - 1; i >= 0; i--) {
            result.add(java.lang.Integer.valueOf(array1.valueAt(i).getActivityType()));
        }
        int i2 = array2.size();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            result.add(java.lang.Integer.valueOf(array2.valueAt(i3).getActivityType()));
        }
        int i4 = array3.size();
        for (int i5 = i4 - 1; i5 >= 0; i5--) {
            result.add(java.lang.Integer.valueOf(array3.valueAt(i5).getActivityType()));
        }
        return result;
    }

    private static com.android.server.wm.ActivityRecord lookForHighestTokenWithFilter(android.util.ArraySet<com.android.server.wm.ActivityRecord> array1, android.util.ArraySet<com.android.server.wm.ActivityRecord> array2, android.util.ArraySet<com.android.server.wm.WindowContainer> array3, java.util.function.Predicate<com.android.server.wm.ActivityRecord> filter) {
        com.android.server.wm.WindowContainer wtoken;
        int array2base = array1.size();
        int array3base = array2.size() + array2base;
        int count = array3.size() + array3base;
        int bestPrefixOrderIndex = Integer.MIN_VALUE;
        com.android.server.wm.ActivityRecord bestToken = null;
        for (int i = 0; i < count; i++) {
            if (i < array2base) {
                wtoken = array1.valueAt(i);
            } else if (i < array3base) {
                wtoken = array2.valueAt(i - array2base);
            } else {
                wtoken = array3.valueAt(i - array3base);
            }
            int prefixOrderIndex = wtoken.getPrefixOrderIndex();
            com.android.server.wm.ActivityRecord r = getAppFromContainer(wtoken);
            if (r != null && filter.test(r) && prefixOrderIndex > bestPrefixOrderIndex) {
                bestPrefixOrderIndex = prefixOrderIndex;
                bestToken = r;
            }
        }
        return bestToken;
    }

    private boolean containsVoiceInteraction(android.util.ArraySet<com.android.server.wm.ActivityRecord> apps) {
        for (int i = apps.size() - 1; i >= 0; i--) {
            if (apps.valueAt(i).mVoiceInteraction) {
                return true;
            }
        }
        return false;
    }

    private void applyAnimations(android.util.ArraySet<com.android.server.wm.WindowContainer> wcs, android.util.ArraySet<com.android.server.wm.ActivityRecord> apps, int transit, boolean visible, android.view.WindowManager.LayoutParams animLp, boolean voiceInteraction) {
        int wcsCount = wcs.size();
        for (int i = 0; i < wcsCount; i++) {
            com.android.server.wm.WindowContainer wc = wcs.valueAt(i);
            java.util.ArrayList<com.android.server.wm.ActivityRecord> transitioningDescendants = new java.util.ArrayList<>();
            for (int j = 0; j < apps.size(); j++) {
                com.android.server.wm.ActivityRecord app = apps.valueAt(j);
                if (app.isDescendantOf(wc)) {
                    transitioningDescendants.add(app);
                }
            }
            wc.applyAnimation(animLp, transit, visible, voiceInteraction, transitioningDescendants);
        }
    }

    static android.util.ArraySet<com.android.server.wm.WindowContainer> getAnimationTargets(android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps, boolean visible) {
        return getAnimationTargets(openingApps, closingApps, visible, -1);
    }

    static boolean isTaskViewTask(com.android.server.wm.WindowContainer wc) {
        boolean isTaskViewTask = (wc instanceof com.android.server.wm.Task) && ((com.android.server.wm.Task) wc).mRemoveWithTaskOrganizer;
        if (isTaskViewTask) {
            return true;
        }
        com.android.server.wm.WindowContainer parent = wc.getParent();
        return parent != null && (parent instanceof com.android.server.wm.Task) && ((com.android.server.wm.Task) parent).mRemoveWithTaskOrganizer;
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x0217 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01da  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static android.util.ArraySet<com.android.server.wm.WindowContainer> getAnimationTargets(android.util.ArraySet<com.android.server.wm.ActivityRecord> r22, android.util.ArraySet<com.android.server.wm.ActivityRecord> r23, boolean r24, int r25) {
        /*
            Method dump skipped, instruction units count: 591
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.AppTransitionController.getAnimationTargets(android.util.ArraySet, android.util.ArraySet, boolean, int):android.util.ArraySet");
    }

    private void applyAnimations(android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps, int transit, android.view.WindowManager.LayoutParams animLp, boolean voiceInteraction) {
        com.android.server.wm.RecentsAnimationController rac = this.mService.getRecentsAnimationController();
        if (transit == -1 || (openingApps.isEmpty() && closingApps.isEmpty())) {
            if (rac != null) {
                rac.sendTasksAppeared();
                return;
            }
            return;
        }
        if (com.android.server.wm.AppTransition.isActivityTransitOld(transit)) {
            java.util.ArrayList<android.util.Pair<com.android.server.wm.ActivityRecord, android.graphics.Rect>> closingLetterboxes = new java.util.ArrayList<>();
            for (int i = 0; i < closingApps.size(); i++) {
                com.android.server.wm.ActivityRecord closingApp = closingApps.valueAt(i);
                if (closingApp.areBoundsLetterboxed()) {
                    android.graphics.Rect insets = closingApp.getLetterboxInsets();
                    closingLetterboxes.add(new android.util.Pair<>(closingApp, insets));
                }
            }
            for (int i2 = 0; i2 < openingApps.size(); i2++) {
                com.android.server.wm.ActivityRecord openingApp = openingApps.valueAt(i2);
                if (openingApp.areBoundsLetterboxed()) {
                    android.graphics.Rect openingInsets = openingApp.getLetterboxInsets();
                    for (android.util.Pair<com.android.server.wm.ActivityRecord, android.graphics.Rect> closingLetterbox : closingLetterboxes) {
                        android.graphics.Rect closingInsets = (android.graphics.Rect) closingLetterbox.second;
                        if (openingInsets.equals(closingInsets)) {
                            com.android.server.wm.ActivityRecord closingApp2 = (com.android.server.wm.ActivityRecord) closingLetterbox.first;
                            openingApp.setNeedsLetterboxedAnimation(true);
                            closingApp2.setNeedsLetterboxedAnimation(true);
                        }
                    }
                }
            }
        }
        android.util.ArraySet<com.android.server.wm.WindowContainer> openingWcs = getAnimationTargets(openingApps, closingApps, true, transit);
        android.util.ArraySet<com.android.server.wm.WindowContainer> closingWcs = getAnimationTargets(openingApps, closingApps, false, transit);
        sAppTransControllerExt.collectWcs(openingWcs, closingWcs);
        applyAnimations(openingWcs, openingApps, transit, true, animLp, voiceInteraction);
        if (sAppTransControllerExt.applyAnimations(openingWcs, openingApps)) {
            return;
        }
        applyAnimations(closingWcs, closingApps, transit, false, animLp, voiceInteraction);
        if (rac != null) {
            rac.sendTasksAppeared();
        }
        for (int i3 = 0; i3 < openingApps.size(); i3++) {
            ((com.android.server.wm.ActivityRecord) openingApps.valueAtUnchecked(i3)).mOverrideTaskTransition = false;
        }
        for (int i4 = 0; i4 < closingApps.size(); i4++) {
            ((com.android.server.wm.ActivityRecord) closingApps.valueAtUnchecked(i4)).mOverrideTaskTransition = false;
        }
        com.android.server.wm.AccessibilityController accessibilityController = this.mDisplayContent.mWmService.mAccessibilityController;
        if (accessibilityController.hasCallbacks()) {
            accessibilityController.onAppWindowTransition(this.mDisplayContent.getDisplayId(), transit);
        }
    }

    private void handleOpeningApps() {
        android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps = this.mDisplayContent.mOpeningApps;
        int appsCount = openingApps.size();
        for (int i = 0; i < appsCount; i++) {
            if (i >= openingApps.size()) {
                android.util.Slog.d(TAG, "handleOpeningApps IndexOutOfBoundsE this = " + this);
                return;
            }
            com.android.server.wm.ActivityRecord app = openingApps.valueAt(i);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(app);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 4418653408751596915L, 0, null, protoLogParam0);
            }
            app.commitVisibility(true, false);
            com.android.server.wm.WindowContainer wc = app.getAnimatingContainer(2, 1);
            if (wc == null || !wc.getAnimationSources().contains(app)) {
                this.mDisplayContent.mNoAnimationNotifyOnTransitionFinished.add(app.token);
            }
            app.updateReportedVisibilityLocked();
            app.showAllWindowsLocked();
            if (this.mDisplayContent.mAppTransition.isNextAppTransitionThumbnailUp()) {
                app.attachThumbnailAnimation();
            } else if (this.mDisplayContent.mAppTransition.isNextAppTransitionOpenCrossProfileApps()) {
                app.attachCrossProfileAppsThumbnailAnimation();
            }
        }
    }

    private void handleClosingApps() {
        android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps = this.mDisplayContent.mClosingApps;
        int appsCount = closingApps.size();
        for (int i = 0; i < appsCount; i++) {
            if (i >= closingApps.size()) {
                android.util.Slog.d(TAG, "handleClosingApps IndexOutOfBoundsE this = " + this);
                return;
            }
            com.android.server.wm.ActivityRecord app = closingApps.valueAt(i);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(app);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -8367738619313176909L, 0, null, protoLogParam0);
            }
            app.commitVisibility(false, false);
            app.updateReportedVisibilityLocked();
            app.allDrawn = true;
            if (app.mStartingWindow != null && !app.mStartingWindow.mAnimatingExit) {
                app.removeStartingWindow();
            }
            if (this.mDisplayContent.mAppTransition.isNextAppTransitionThumbnailDown()) {
                app.attachThumbnailAnimation();
            }
        }
    }

    private void handleClosingChangingContainers() {
        android.util.ArrayMap<com.android.server.wm.WindowContainer, android.graphics.Rect> containers = this.mDisplayContent.mClosingChangingContainers;
        while (!containers.isEmpty()) {
            com.android.server.wm.WindowContainer container = containers.keyAt(0);
            containers.remove(container);
            com.android.server.wm.TaskFragment taskFragment = container.asTaskFragment();
            if (taskFragment != null) {
                taskFragment.updateOrganizedTaskFragmentSurface();
            }
        }
    }

    private void handleChangingApps(int transit) {
        android.util.ArraySet<com.android.server.wm.WindowContainer> apps = this.mDisplayContent.mChangingContainers;
        int appsCount = apps.size();
        for (int i = 0; i < appsCount; i++) {
            com.android.server.wm.WindowContainer wc = apps.valueAt(i);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(wc);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1855459282905873641L, 0, null, protoLogParam0);
            }
            wc.applyAnimation(null, transit, true, false, null);
        }
    }

    private boolean transitionGoodToGo(android.util.ArraySet<? extends com.android.server.wm.WindowContainer> apps, android.util.ArrayMap<com.android.server.wm.WindowContainer, java.lang.Integer> outReasons) {
        int i;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
            long protoLogParam0 = apps.size();
            boolean protoLogParam1 = this.mService.mDisplayFrozen;
            boolean protoLogParam2 = this.mDisplayContent.mAppTransition.isTimeout();
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 2951634988136738868L, 61, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2));
        }
        if (this.mDisplayContent.mAppTransition.isTimeout()) {
            return true;
        }
        if (!sAppTransControllerExt.isAllInSplitOpening(apps)) {
            return false;
        }
        com.android.server.wm.ScreenRotationAnimation screenRotationAnimation = this.mService.mRoot.getDisplayContent(0).getRotationAnimation();
        if (screenRotationAnimation != null && screenRotationAnimation.isAnimating() && this.mDisplayContent.getDisplayRotation().needsUpdate()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 4963754906024950916L, 0, null, null);
            }
            return false;
        }
        for (int i2 = 0; i2 < apps.size(); i2++) {
            com.android.server.wm.WindowContainer wc = apps.valueAt(i2);
            com.android.server.wm.ActivityRecord activity = getAppFromContainer(wc);
            if (activity != null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(activity);
                    boolean protoLogParam12 = activity.allDrawn;
                    boolean protoLogParam22 = activity.isStartingWindowDisplayed();
                    boolean protoLogParam3 = activity.startingMoved;
                    boolean protoLogParam4 = activity.isRelaunching();
                    java.lang.String protoLogParam5 = java.lang.String.valueOf(activity.mStartingWindow);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 5073676463280304697L, 1020, null, protoLogParam02, java.lang.Boolean.valueOf(protoLogParam12), java.lang.Boolean.valueOf(protoLogParam22), java.lang.Boolean.valueOf(protoLogParam3), java.lang.Boolean.valueOf(protoLogParam4), protoLogParam5);
                }
                boolean allDrawn = activity.allDrawn && !activity.isRelaunching();
                if (sAppTransControllerExt.isTransferStartingWindow(activity)) {
                    return false;
                }
                if (!allDrawn && ((!activity.isStartingWindowDisplayed() || !sAppTransControllerExt.isGoodToGoWhenStartTasks(activity)) && !activity.startingMoved && !sAppTransControllerExt.transitionGoodToGo(activity, this.mDisplayContent.mOpeningApps) && !sAppTransControllerExt.isGoodToGoWhenEnterCompactWindowApp(activity))) {
                    return false;
                }
                if (allDrawn) {
                    outReasons.put(activity, 2);
                } else {
                    if (activity.mStartingData instanceof com.android.server.wm.SplashScreenStartingData) {
                        i = 1;
                    } else {
                        i = 4;
                    }
                    outReasons.put(activity, java.lang.Integer.valueOf(i));
                }
            }
        }
        if (this.mDisplayContent.mAppTransition.isFetchingAppTransitionsSpecs()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 3437142041296647115L, 0, null, null);
            }
            return false;
        }
        if (this.mDisplayContent.mUnknownAppVisibilityController.allResolved()) {
            return !this.mWallpaperControllerLocked.isWallpaperVisible() || this.mWallpaperControllerLocked.wallpaperTransitionReady();
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam03 = java.lang.String.valueOf(this.mDisplayContent.mUnknownAppVisibilityController.getDebugMessage());
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1461079689316480707L, 0, null, protoLogParam03);
        }
        return false;
    }

    private boolean transitionGoodToGoForTaskFragments() {
        if (this.mDisplayContent.mAppTransition.isTimeout()) {
            return true;
        }
        android.util.ArraySet<com.android.server.wm.Task> rootTasks = new android.util.ArraySet<>();
        for (int i = this.mDisplayContent.mOpeningApps.size() - 1; i >= 0; i--) {
            rootTasks.add(this.mDisplayContent.mOpeningApps.valueAt(i).getRootTask());
        }
        for (int i2 = this.mDisplayContent.mClosingApps.size() - 1; i2 >= 0; i2--) {
            rootTasks.add(this.mDisplayContent.mClosingApps.valueAt(i2).getRootTask());
        }
        for (int i3 = this.mDisplayContent.mChangingContainers.size() - 1; i3 >= 0; i3--) {
            rootTasks.add(findRootTaskFromContainer(this.mDisplayContent.mChangingContainers.valueAt(i3)));
        }
        int i4 = rootTasks.size();
        for (int i5 = i4 - 1; i5 >= 0; i5--) {
            com.android.server.wm.Task rootTask = rootTasks.valueAt(i5);
            if (rootTask != null) {
                boolean notReady = rootTask.forAllLeafTaskFragments(new java.util.function.Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda8
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.wm.AppTransitionController.lambda$transitionGoodToGoForTaskFragments$6((com.android.server.wm.TaskFragment) obj);
                    }
                });
                if (notReady) {
                    return false;
                }
            }
        }
        return true;
    }

    static /* synthetic */ boolean lambda$transitionGoodToGoForTaskFragments$6(com.android.server.wm.TaskFragment taskFragment) {
        if (!taskFragment.isReadyToTransit()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(taskFragment);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 3579533288018884842L, 0, null, protoLogParam0);
            }
            return true;
        }
        return false;
    }

    boolean isTransitWithinTask(int transit, com.android.server.wm.Task task) {
        if (task == null || !this.mDisplayContent.mChangingContainers.isEmpty()) {
            return false;
        }
        if (transit != 6 && transit != 7 && transit != 18) {
            return false;
        }
        for (com.android.server.wm.ActivityRecord activity : this.mDisplayContent.mOpeningApps) {
            com.android.server.wm.Task activityTask = activity.getTask();
            if (activityTask != task) {
                return false;
            }
        }
        for (com.android.server.wm.ActivityRecord activity2 : this.mDisplayContent.mClosingApps) {
            if (activity2.getTask() != task) {
                return false;
            }
        }
        return true;
    }

    private static boolean canBeWallpaperTarget(android.util.ArraySet<com.android.server.wm.ActivityRecord> apps) {
        for (int i = apps.size() - 1; i >= 0; i--) {
            if (apps.valueAt(i).windowsCanBeWallpaperTarget()) {
                return true;
            }
        }
        return false;
    }

    private static com.android.server.wm.ActivityRecord getTopApp(android.util.ArraySet<? extends com.android.server.wm.WindowContainer> apps, boolean ignoreInvisible) {
        int prefixOrderIndex;
        int topPrefixOrderIndex = Integer.MIN_VALUE;
        com.android.server.wm.ActivityRecord topApp = null;
        for (int i = apps.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord app = getAppFromContainer(apps.valueAt(i));
            if (app != null && ((!ignoreInvisible || app.isVisible()) && (prefixOrderIndex = app.getPrefixOrderIndex()) > topPrefixOrderIndex)) {
                topPrefixOrderIndex = prefixOrderIndex;
                topApp = app;
            }
        }
        return topApp;
    }
}
