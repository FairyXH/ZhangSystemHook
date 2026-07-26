package com.android.systemui;

/* JADX INFO: loaded from: classes3.dex */
public final class FeatureFlagsImpl implements com.android.systemui.FeatureFlags {
    private static boolean accessibility_is_cached = false;
    private static boolean biometrics_framework_is_cached = false;
    private static boolean communal_is_cached = false;
    private static boolean systemui_is_cached = false;
    private static boolean createWindowlessWindowMagnifier = false;

    private void load_overrides_accessibility() {
        try {
            android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("accessibility", new java.lang.String[0]);
            createWindowlessWindowMagnifier = properties.getBoolean(com.android.systemui.Flags.FLAG_CREATE_WINDOWLESS_WINDOW_MAGNIFIER, false);
            accessibility_is_cached = true;
        } catch (java.lang.NullPointerException e) {
            throw new java.lang.RuntimeException("Cannot read value from namespace accessibility from DeviceConfig. It could be that the code using flag executed before SettingsProvider initialization. Please use fixed read-only flag by adding is_fixed_read_only: true in flag declaration.", e);
        }
    }

    private void load_overrides_biometrics_framework() {
        try {
            android.provider.DeviceConfig.getProperties("biometrics_framework", new java.lang.String[0]);
            biometrics_framework_is_cached = true;
        } catch (java.lang.NullPointerException e) {
            throw new java.lang.RuntimeException("Cannot read value from namespace biometrics_framework from DeviceConfig. It could be that the code using flag executed before SettingsProvider initialization. Please use fixed read-only flag by adding is_fixed_read_only: true in flag declaration.", e);
        }
    }

    private void load_overrides_communal() {
        try {
            android.provider.DeviceConfig.getProperties("communal", new java.lang.String[0]);
            communal_is_cached = true;
        } catch (java.lang.NullPointerException e) {
            throw new java.lang.RuntimeException("Cannot read value from namespace communal from DeviceConfig. It could be that the code using flag executed before SettingsProvider initialization. Please use fixed read-only flag by adding is_fixed_read_only: true in flag declaration.", e);
        }
    }

    private void load_overrides_systemui() {
        try {
            android.provider.DeviceConfig.getProperties("systemui", new java.lang.String[0]);
            systemui_is_cached = true;
        } catch (java.lang.NullPointerException e) {
            throw new java.lang.RuntimeException("Cannot read value from namespace systemui from DeviceConfig. It could be that the code using flag executed before SettingsProvider initialization. Please use fixed read-only flag by adding is_fixed_read_only: true in flag declaration.", e);
        }
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean activityTransitionUseLargestWindow() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean ambientTouchMonitorListenToDisplayChanges() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean appClipsBacklinks() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean bindKeyguardMediaVisibility() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean bpTalkback() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean brightnessSliderFocusState() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean centralizedStatusBarHeightFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean clipboardNoninteractiveOnLockscreen() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean clockReactiveVariants() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean communalBouncerDoNotModifyPluginOpen() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean communalHub() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean composeBouncer() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean composeLockscreen() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean confineNotificationTouchToViewWidth() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean constraintBp() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean contextualTipsAssistantDismissFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean coroutineTracing() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean createWindowlessWindowMagnifier() {
        if (!accessibility_is_cached) {
            load_overrides_accessibility();
        }
        return createWindowlessWindowMagnifier;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dedicatedNotifInflationThread() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean delayShowMagnificationButton() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean delayedWakelockReleaseOnBackgroundThread() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean deviceEntryUdfpsRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean disableContextualTipsFrequencyCheck() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean disableContextualTipsIosSwitcherCheck() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dozeuiSchedulingAlarmsBackgroundExecution() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dreamInputSessionPilferOnce() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dreamOverlayBouncerSwipeDirectionFiltering() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dualShade() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean edgeBackGestureHandlerThread() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean edgebackGestureHandlerGetRunningTasksBackground() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableBackgroundKeyguardOndrawnCallback() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableContextualTipForMuteVolume() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableContextualTipForPowerOff() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableContextualTipForTakeScreenshot() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableContextualTips() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableEfficientDisplayRepository() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableLayoutTracing() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableViewCaptureTracing() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableWidgetPickerSizeFilter() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enforceBrightnessBaseUserRestriction() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean exampleFlag() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean fastUnlockTransition() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean fixImageWallpaperCrashSurfaceAlreadyReleased() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean fixScreenshotActionDismissSystemWindows() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuAnimatedTuck() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuDragToEdit() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuDragToHide() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuImeDisplacementAnimation() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuNarrowTargetContentObserver() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuOverlapsNavBarsFlag() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuRadiiAnimation() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean getConnectedDeviceNameUnsynchronized() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean glanceableHubAllowKeyguardWhenDreaming() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean glanceableHubFullscreenSwipe() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean glanceableHubGestureHandle() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean glanceableHubShortcutButton() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean hapticBrightnessSlider() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean hapticVolumeSlider() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean hearingAidsQsTileDialog() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean hearingDevicesDialogRelatedTools() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean keyboardDockingIndicator() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean keyboardShortcutHelperRewrite() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean keyguardBottomAreaRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean keyguardWmStateRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean lightRevealMigration() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean mediaControlsLockscreenShadeBugFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean mediaControlsRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean mediaControlsUserInitiatedDeleteintent() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean migrateClocksToBlueprint() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean newAodTransition() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean newTouchpadGesturesTutorial() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean newVolumePanel() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationAsyncGroupHeaderInflation() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationAsyncHybridViewInflation() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationAvalancheSuppression() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationAvalancheThrottleHun() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationBackgroundTintOptimization() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationColorUpdateLogger() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationContentAlphaOptimization() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationFooterBackgroundTintOptimization() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationMediaManagerBackgroundExecution() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationMinimalismPrototype() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationOverExpansionClippingFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationPulsingFix() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationRowContentBinderRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationRowUserContext() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationViewFlipperPausingV2() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsBackgroundIcons() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsFooterViewRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsHeadsUpRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsHideOnDisplaySwitch() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsIconContainerRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsImprovedHunAnimation() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsLiveDataStoreRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notifyPowerManagerUserActivityBackground() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean pinInputFieldStyledFocusState() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean predictiveBackAnimateBouncer() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean predictiveBackAnimateDialogs() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean predictiveBackAnimateShade() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean predictiveBackSysui() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean priorityPeopleSection() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean privacyDotUnfoldWrongCornerFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean pssAppSelectorAbruptExitFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean pssAppSelectorRecentsSplitScreen() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean pssTaskSwitcher() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsCustomTileClickGuaranteedBugFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsNewPipeline() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsNewTiles() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsNewTilesFuture() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsTileFocusState() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsUiRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean quickSettingsVisualHapticsLongpress() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean recordIssueQsTile() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean refactorGetCurrentUser() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean registerBatteryControllerReceiversInCorestartable() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean registerNewWalletCardInBackground() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean registerWallpaperNotifierBackground() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean registerZenModeContentObserverBackground() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean removeDreamOverlayHideOnTouch() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean restToUnlock() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean restartDreamOnUnocclude() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean revampedBouncerMessages() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean runFingerprintDetectOnDismissibleKeyguard() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean saveAndRestoreMagnificationSettingsButtons() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean sceneContainer() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshareNotificationHidingBugFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotActionDismissSystemWindows() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotPrivateProfileAccessibilityAnnouncementFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotPrivateProfileBehaviorFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotScrollCropViewCrashFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotShelfUi2() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean shadeCollapseActivityLaunchFix() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean shaderlibLoadingEffectRefactor() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean sliceBroadcastRelayInBackground() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean sliceManagerBinderCallBackground() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean smartspaceLockscreenViewmodel() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean smartspaceRelocateToBottom() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean smartspaceRemoteviewsRendering() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean statusBarMonochromeIconsFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean statusBarScreenSharingChips() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean statusBarStaticInoutIndicators() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean switchUserOnBg() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean sysuiTeamfood() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean themeOverlayControllerWakefulnessDeprecation() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean threeButtonCornerSwipe() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean truncatedStatusBarIconsFix() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean udfpsViewPerformance() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean unfoldAnimationBackgroundProgress() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean updateUserSwitcherBackground() {
        return true;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean validateKeyboardShortcutHelperIconUri() {
        return false;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean visualInterruptionsRefactor() {
        return false;
    }
}
