package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
class AccessibilityUserState {
    private static final boolean DEBUG_ACCESSBILITY;
    private static final java.lang.String LOG_TAG = com.android.server.accessibility.AccessibilityUserState.class.getSimpleName();
    private boolean mAccessibilityFocusOnlyInActiveWindow;
    private boolean mBindInstantServiceAllowed;
    private android.content.Context mContext;
    private int mFocusColor;
    private final int mFocusColorDefaultValue;
    private int mFocusStrokeWidth;
    private final int mFocusStrokeWidthDefaultValue;
    private boolean mIsAudioDescriptionByDefaultRequested;
    private boolean mIsAutoclickEnabled;
    private boolean mIsFilterKeyEventsEnabled;
    private boolean mIsMagnificationSingleFingerTripleTapEnabled;
    private boolean mIsPerformGesturesEnabled;
    private boolean mIsTextHighContrastEnabled;
    private boolean mIsTouchExplorationEnabled;
    private boolean mMagnificationTwoFingerTripleTapEnabled;
    private boolean mRequestMultiFingerGestures;
    private boolean mRequestTwoFingerPassthrough;
    private boolean mSendMotionEventsEnabled;
    private android.content.ComponentName mServiceChangingSoftKeyboardMode;
    private boolean mServiceHandlesDoubleTap;
    private final com.android.server.accessibility.AccessibilityUserState.ServiceInfoChangeListener mServiceInfoChangeListener;
    private final boolean mSupportWindowMagnification;
    private java.lang.String mTargetAssignedToAccessibilityButton;
    final int mUserId;
    private int mUserInteractiveUiTimeout;
    private int mUserNonInteractiveUiTimeout;
    final android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> mUserClients = new android.os.RemoteCallbackList<>();
    final java.util.ArrayList<com.android.server.accessibility.AccessibilityServiceConnection> mBoundServices = new java.util.ArrayList<>();
    final java.util.Map<android.content.ComponentName, com.android.server.accessibility.AccessibilityServiceConnection> mComponentNameToServiceMap = new java.util.HashMap();
    final java.util.List<android.accessibilityservice.AccessibilityServiceInfo> mInstalledServices = new java.util.ArrayList();
    final java.util.List<android.accessibilityservice.AccessibilityShortcutInfo> mInstalledShortcuts = new java.util.ArrayList();
    final java.util.Set<android.content.ComponentName> mBindingServices = new java.util.HashSet();
    final java.util.Set<android.content.ComponentName> mCrashedServices = new java.util.HashSet();
    final java.util.Set<android.content.ComponentName> mEnabledServices = new java.util.HashSet();
    final java.util.Set<android.content.ComponentName> mTouchExplorationGrantedServices = new java.util.HashSet();
    final android.util.ArraySet<java.lang.String> mAccessibilityShortcutKeyTargets = new android.util.ArraySet<>();
    final android.util.ArraySet<java.lang.String> mAccessibilityButtonTargets = new android.util.ArraySet<>();
    private final android.util.ArraySet<java.lang.String> mAccessibilityQsTargets = new android.util.ArraySet<>();
    private final android.util.ArraySet<android.content.ComponentName> mA11yTilesInQsPanel = new android.util.ArraySet<>();
    private android.util.SparseArray<java.lang.Boolean> mServiceDetectsGestures = new android.util.SparseArray<>(0);
    private int mNonInteractiveUiTimeout = 0;
    private int mInteractiveUiTimeout = 0;
    private int mLastSentClientState = -1;
    private final android.util.SparseIntArray mMagnificationModes = new android.util.SparseIntArray();
    private int mMagnificationCapabilities = 1;
    private boolean mMagnificationFollowTypingEnabled = true;
    private boolean mAlwaysOnMagnificationEnabled = false;
    private final java.util.Map<android.content.ComponentName, android.content.ComponentName> mA11yServiceToTileService = new android.util.ArrayMap();
    private final java.util.Map<android.content.ComponentName, android.content.ComponentName> mA11yActivityToTileService = new android.util.ArrayMap();
    private int mSoftKeyboardShowMode = 0;
    private android.server.accessibility.IAccessibilityUserStateExt mIAccessibilityUserStateExt = (android.server.accessibility.IAccessibilityUserStateExt) system.ext.loader.core.ExtLoader.type(android.server.accessibility.IAccessibilityUserStateExt.class).base(this).create();

    interface ServiceInfoChangeListener {
        void onServiceInfoChangedLocked(com.android.server.accessibility.AccessibilityUserState accessibilityUserState);
    }

    static {
        DEBUG_ACCESSBILITY = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false) || android.os.SystemProperties.getBoolean("persist.sys.alwayson.enable", false);
    }

    boolean isValidMagnificationModeLocked(int displayId) {
        int mode = getMagnificationModeLocked(displayId);
        return (this.mSupportWindowMagnification || mode != 2) && (this.mMagnificationCapabilities & mode) != 0;
    }

    AccessibilityUserState(int userId, android.content.Context context, com.android.server.accessibility.AccessibilityUserState.ServiceInfoChangeListener serviceInfoChangeListener) {
        boolean z = false;
        this.mUserId = userId;
        this.mContext = context;
        this.mServiceInfoChangeListener = serviceInfoChangeListener;
        this.mFocusStrokeWidthDefaultValue = this.mContext.getResources().getDimensionPixelSize(android.R.dimen.accessibility_autoclick_scroll_panel_button_size);
        this.mFocusColorDefaultValue = this.mContext.getResources().getColor(android.R.color.accessibility_feature_background);
        this.mFocusStrokeWidth = this.mFocusStrokeWidthDefaultValue;
        this.mFocusColor = this.mFocusColorDefaultValue;
        if (this.mContext.getResources().getBoolean(android.R.bool.config_launchCameraOnCameraLensCoverToggle) && this.mContext.getPackageManager().hasSystemFeature("android.software.window_magnification")) {
            z = true;
        }
        this.mSupportWindowMagnification = z;
    }

    boolean isHandlingAccessibilityEventsLocked() {
        return (this.mBoundServices.isEmpty() && this.mBindingServices.isEmpty()) ? false : true;
    }

    void onSwitchToAnotherUserLocked() {
        unbindAllServicesLocked();
        this.mBoundServices.clear();
        this.mBindingServices.clear();
        this.mCrashedServices.clear();
        this.mLastSentClientState = -1;
        this.mNonInteractiveUiTimeout = 0;
        this.mInteractiveUiTimeout = 0;
        this.mEnabledServices.clear();
        this.mTouchExplorationGrantedServices.clear();
        this.mAccessibilityShortcutKeyTargets.clear();
        this.mAccessibilityButtonTargets.clear();
        this.mTargetAssignedToAccessibilityButton = null;
        this.mIsTouchExplorationEnabled = false;
        this.mServiceHandlesDoubleTap = false;
        this.mRequestMultiFingerGestures = false;
        this.mRequestTwoFingerPassthrough = false;
        this.mSendMotionEventsEnabled = false;
        this.mIsMagnificationSingleFingerTripleTapEnabled = false;
        this.mMagnificationTwoFingerTripleTapEnabled = false;
        this.mIsAutoclickEnabled = false;
        this.mUserNonInteractiveUiTimeout = 0;
        this.mUserInteractiveUiTimeout = 0;
        this.mMagnificationModes.clear();
        this.mFocusStrokeWidth = this.mFocusStrokeWidthDefaultValue;
        this.mFocusColor = this.mFocusColorDefaultValue;
        this.mMagnificationFollowTypingEnabled = true;
        this.mAlwaysOnMagnificationEnabled = false;
    }

    void addServiceLocked(com.android.server.accessibility.AccessibilityServiceConnection serviceConnection) {
        if (!this.mBoundServices.contains(serviceConnection)) {
            this.mBoundServices.add(serviceConnection);
            this.mComponentNameToServiceMap.put(serviceConnection.getComponentName(), serviceConnection);
            this.mServiceInfoChangeListener.onServiceInfoChangedLocked(this);
        }
    }

    void removeServiceLocked(com.android.server.accessibility.AccessibilityServiceConnection serviceConnection) {
        this.mBoundServices.remove(serviceConnection);
        serviceConnection.onRemoved();
        if (this.mServiceChangingSoftKeyboardMode != null && this.mServiceChangingSoftKeyboardMode.equals(serviceConnection.getServiceInfo().getComponentName())) {
            setSoftKeyboardModeLocked(0, null);
        }
        this.mComponentNameToServiceMap.clear();
        for (int i = 0; i < this.mBoundServices.size(); i++) {
            com.android.server.accessibility.AccessibilityServiceConnection boundClient = this.mBoundServices.get(i);
            this.mComponentNameToServiceMap.put(boundClient.getComponentName(), boundClient);
        }
        this.mServiceInfoChangeListener.onServiceInfoChangedLocked(this);
    }

    void serviceDisconnectedLocked(com.android.server.accessibility.AccessibilityServiceConnection serviceConnection) {
        removeServiceLocked(serviceConnection);
        android.util.Slog.d(LOG_TAG, "[serviceDisconnectedLocked]add mCrashedServices, getComponentName = " + serviceConnection.getComponentName());
        this.mCrashedServices.add(serviceConnection.getComponentName());
    }

    boolean setSoftKeyboardModeLocked(int i, android.content.ComponentName componentName) {
        if (i != 0 && i != 1 && i != 2) {
            android.util.Slog.w(LOG_TAG, "Invalid soft keyboard mode");
            return false;
        }
        if (this.mSoftKeyboardShowMode == i) {
            return true;
        }
        if (i == 2) {
            if (hasUserOverriddenHardKeyboardSetting()) {
                return false;
            }
            if (getSoftKeyboardValueFromSettings() != 2) {
                setOriginalHardKeyboardValue(getSecureIntForUser("show_ime_with_hard_keyboard", 0, this.mUserId) != 0);
            }
            putSecureIntForUser("show_ime_with_hard_keyboard", 1, this.mUserId);
        } else if (this.mSoftKeyboardShowMode == 2) {
            putSecureIntForUser("show_ime_with_hard_keyboard", getOriginalHardKeyboardValue() ? 1 : 0, this.mUserId);
        }
        saveSoftKeyboardValueToSettings(i);
        this.mSoftKeyboardShowMode = i;
        this.mServiceChangingSoftKeyboardMode = componentName;
        for (int size = this.mBoundServices.size() - 1; size >= 0; size--) {
            this.mBoundServices.get(size).notifySoftKeyboardShowModeChangedLocked(this.mSoftKeyboardShowMode);
        }
        return true;
    }

    int getSoftKeyboardShowModeLocked() {
        return this.mSoftKeyboardShowMode;
    }

    void reconcileSoftKeyboardModeWithSettingsLocked() {
        boolean showWithHardKeyboardSettings = getSecureIntForUser("show_ime_with_hard_keyboard", 0, this.mUserId) != 0;
        if (this.mSoftKeyboardShowMode == 2 && !showWithHardKeyboardSettings) {
            setSoftKeyboardModeLocked(0, null);
            setUserOverridesHardKeyboardSetting();
        }
        if (getSoftKeyboardValueFromSettings() != this.mSoftKeyboardShowMode) {
            android.util.Slog.e(LOG_TAG, "Show IME setting inconsistent with internal state. Overwriting");
            setSoftKeyboardModeLocked(0, null);
            putSecureIntForUser("accessibility_soft_keyboard_mode", 0, this.mUserId);
        }
    }

    boolean getBindInstantServiceAllowedLocked() {
        return this.mBindInstantServiceAllowed;
    }

    void setBindInstantServiceAllowedLocked(boolean allowed) {
        this.mBindInstantServiceAllowed = allowed;
    }

    java.util.Set<android.content.ComponentName> getBindingServicesLocked() {
        return this.mBindingServices;
    }

    java.util.Set<android.content.ComponentName> getCrashedServicesLocked() {
        return this.mCrashedServices;
    }

    java.util.Set<android.content.ComponentName> getEnabledServicesLocked() {
        return this.mEnabledServices;
    }

    void removeDisabledServicesFromTemporaryStatesLocked() {
        int count = this.mInstalledServices.size();
        for (int i = 0; i < count; i++) {
            android.accessibilityservice.AccessibilityServiceInfo installedService = this.mInstalledServices.get(i);
            android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(installedService.getId());
            if (!this.mEnabledServices.contains(componentName)) {
                if (this.mCrashedServices.contains(componentName) || this.mBindingServices.contains(componentName)) {
                    android.util.Slog.d(LOG_TAG, "[removeDisabledServicesFromTemporaryStatesLocked]componentName = " + componentName);
                }
                this.mCrashedServices.remove(componentName);
                this.mBindingServices.remove(componentName);
            }
        }
    }

    java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> getBoundServicesLocked() {
        return this.mBoundServices;
    }

    int getClientStateLocked(boolean uiAutomationCanIntrospect, int traceClientState) {
        int clientState = 0;
        boolean a11yEnabled = uiAutomationCanIntrospect || isHandlingAccessibilityEventsLocked();
        if (a11yEnabled) {
            clientState = 0 | 1;
        }
        if (a11yEnabled && this.mIsTouchExplorationEnabled) {
            clientState = clientState | 2 | 8 | 16;
        }
        if (this.mIAccessibilityUserStateExt.getAccessibilityUserState(this.mEnabledServices)) {
            clientState |= 128;
        }
        if (this.mIsTextHighContrastEnabled) {
            clientState |= 4;
        }
        if (this.mIsAudioDescriptionByDefaultRequested) {
            clientState |= 4096;
        }
        return clientState | traceClientState;
    }

    private void setUserOverridesHardKeyboardSetting() {
        int softKeyboardSetting = getSecureIntForUser("accessibility_soft_keyboard_mode", 0, this.mUserId);
        putSecureIntForUser("accessibility_soft_keyboard_mode", 1073741824 | softKeyboardSetting, this.mUserId);
    }

    private boolean hasUserOverriddenHardKeyboardSetting() {
        int softKeyboardSetting = getSecureIntForUser("accessibility_soft_keyboard_mode", 0, this.mUserId);
        return (1073741824 & softKeyboardSetting) != 0;
    }

    private void setOriginalHardKeyboardValue(boolean originalHardKeyboardValue) {
        int oldSoftKeyboardSetting = getSecureIntForUser("accessibility_soft_keyboard_mode", 0, this.mUserId);
        int newSoftKeyboardSetting = (originalHardKeyboardValue ? 536870912 : 0) | ((-536870913) & oldSoftKeyboardSetting);
        putSecureIntForUser("accessibility_soft_keyboard_mode", newSoftKeyboardSetting, this.mUserId);
    }

    private void saveSoftKeyboardValueToSettings(int softKeyboardShowMode) {
        int oldSoftKeyboardSetting = getSecureIntForUser("accessibility_soft_keyboard_mode", 0, this.mUserId);
        int newSoftKeyboardSetting = (oldSoftKeyboardSetting & (-4)) | softKeyboardShowMode;
        putSecureIntForUser("accessibility_soft_keyboard_mode", newSoftKeyboardSetting, this.mUserId);
    }

    private int getSoftKeyboardValueFromSettings() {
        return getSecureIntForUser("accessibility_soft_keyboard_mode", 0, this.mUserId) & 3;
    }

    private boolean getOriginalHardKeyboardValue() {
        return (getSecureIntForUser("accessibility_soft_keyboard_mode", 0, this.mUserId) & 536870912) != 0;
    }

    private void unbindAllServicesLocked() {
        java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> services = this.mBoundServices;
        for (int count = services.size(); count > 0; count--) {
            services.get(0).unbindLocked();
        }
    }

    private int getSecureIntForUser(java.lang.String key, int def, int userId) {
        return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), key, def, userId);
    }

    private void putSecureIntForUser(java.lang.String key, int value, int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), key, value, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        pw.append("User state[");
        pw.println();
        pw.append("     attributes:{id=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mUserId));
        pw.append(", touchExplorationEnabled=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mIsTouchExplorationEnabled));
        pw.append(", serviceHandlesDoubleTap=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mServiceHandlesDoubleTap));
        pw.append(", requestMultiFingerGestures=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mRequestMultiFingerGestures));
        pw.append(", requestTwoFingerPassthrough=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mRequestTwoFingerPassthrough));
        pw.append(", sendMotionEventsEnabled").append((java.lang.CharSequence) java.lang.String.valueOf(this.mSendMotionEventsEnabled));
        pw.append(", displayMagnificationEnabled=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mIsMagnificationSingleFingerTripleTapEnabled));
        pw.append(", autoclickEnabled=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mIsAutoclickEnabled));
        pw.append(", nonInteractiveUiTimeout=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mNonInteractiveUiTimeout));
        pw.append(", interactiveUiTimeout=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mInteractiveUiTimeout));
        pw.append(", installedServiceCount=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mInstalledServices.size()));
        pw.append(", magnificationModes=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mMagnificationModes));
        pw.append(", magnificationCapabilities=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mMagnificationCapabilities));
        pw.append(", audioDescriptionByDefaultEnabled=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mIsAudioDescriptionByDefaultRequested));
        pw.append(", magnificationFollowTypingEnabled=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mMagnificationFollowTypingEnabled));
        pw.append(", alwaysOnMagnificationEnabled=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mAlwaysOnMagnificationEnabled));
        pw.append("}");
        pw.println();
        pw.append("     shortcut key:{");
        int size = this.mAccessibilityShortcutKeyTargets.size();
        for (int i = 0; i < size; i++) {
            java.lang.String componentId = this.mAccessibilityShortcutKeyTargets.valueAt(i);
            pw.append((java.lang.CharSequence) componentId);
            if (i + 1 < size) {
                pw.append(", ");
            }
        }
        pw.println("}");
        pw.append("     button:{");
        int size2 = this.mAccessibilityButtonTargets.size();
        for (int i2 = 0; i2 < size2; i2++) {
            java.lang.String componentId2 = this.mAccessibilityButtonTargets.valueAt(i2);
            pw.append((java.lang.CharSequence) componentId2);
            if (i2 + 1 < size2) {
                pw.append(", ");
            }
        }
        pw.println("}");
        pw.append("     button target:{").append((java.lang.CharSequence) this.mTargetAssignedToAccessibilityButton);
        pw.println("}");
        pw.append("     qs shortcut targets:").append((java.lang.CharSequence) this.mAccessibilityQsTargets.toString());
        pw.println();
        pw.append("     a11y tiles in QS panel:").append((java.lang.CharSequence) this.mA11yTilesInQsPanel.toString());
        pw.println();
        pw.append("     Bound services:{");
        int serviceCount = this.mBoundServices.size();
        for (int j = 0; j < serviceCount; j++) {
            if (j > 0) {
                pw.append(", ");
                pw.println();
                pw.append("                     ");
            }
            com.android.server.accessibility.AccessibilityServiceConnection service = this.mBoundServices.get(j);
            service.dump(fd, pw, args);
        }
        pw.println("}");
        pw.append("     Enabled services:{");
        java.util.Iterator<android.content.ComponentName> it = this.mEnabledServices.iterator();
        if (it.hasNext()) {
            android.content.ComponentName componentName = it.next();
            pw.append((java.lang.CharSequence) componentName.toShortString());
            while (it.hasNext()) {
                android.content.ComponentName componentName2 = it.next();
                pw.append(", ");
                pw.append((java.lang.CharSequence) componentName2.toShortString());
            }
        }
        pw.println("}");
        pw.append("     Binding services:{");
        java.util.Iterator<android.content.ComponentName> it2 = this.mBindingServices.iterator();
        if (it2.hasNext()) {
            android.content.ComponentName componentName3 = it2.next();
            pw.append((java.lang.CharSequence) componentName3.toShortString());
            while (it2.hasNext()) {
                android.content.ComponentName componentName4 = it2.next();
                pw.append(", ");
                pw.append((java.lang.CharSequence) componentName4.toShortString());
            }
        }
        pw.println("}");
        pw.append("     Crashed services:{");
        java.util.Iterator<android.content.ComponentName> it3 = this.mCrashedServices.iterator();
        if (it3.hasNext()) {
            android.content.ComponentName componentName5 = it3.next();
            pw.append((java.lang.CharSequence) componentName5.toShortString());
            while (it3.hasNext()) {
                android.content.ComponentName componentName6 = it3.next();
                pw.append(", ");
                pw.append((java.lang.CharSequence) componentName6.toShortString());
            }
        }
        pw.println("}");
        pw.println("     Client list info:{");
        this.mUserClients.dump(pw, "          Client list ");
        pw.println("          Registered clients:{");
        for (int i3 = 0; i3 < this.mUserClients.getRegisteredCallbackCount(); i3++) {
            com.android.server.accessibility.AccessibilityManagerService.Client client = (com.android.server.accessibility.AccessibilityManagerService.Client) this.mUserClients.getRegisteredCallbackCookie(i3);
            pw.append((java.lang.CharSequence) java.util.Arrays.toString(client.mPackageNames));
        }
        pw.println("}]");
    }

    public boolean isAutoclickEnabledLocked() {
        return this.mIsAutoclickEnabled;
    }

    public void setAutoclickEnabledLocked(boolean enabled) {
        this.mIsAutoclickEnabled = enabled;
    }

    public boolean isMagnificationSingleFingerTripleTapEnabledLocked() {
        return this.mIsMagnificationSingleFingerTripleTapEnabled;
    }

    public void setMagnificationSingleFingerTripleTapEnabledLocked(boolean enabled) {
        this.mIsMagnificationSingleFingerTripleTapEnabled = enabled;
    }

    public boolean isMagnificationTwoFingerTripleTapEnabledLocked() {
        return this.mMagnificationTwoFingerTripleTapEnabled;
    }

    public void setMagnificationTwoFingerTripleTapEnabledLocked(boolean enabled) {
        this.mMagnificationTwoFingerTripleTapEnabled = enabled;
    }

    public boolean isFilterKeyEventsEnabledLocked() {
        return this.mIsFilterKeyEventsEnabled;
    }

    public void setFilterKeyEventsEnabledLocked(boolean enabled) {
        this.mIsFilterKeyEventsEnabled = enabled;
    }

    public int getInteractiveUiTimeoutLocked() {
        return this.mInteractiveUiTimeout;
    }

    public void setInteractiveUiTimeoutLocked(int timeout) {
        this.mInteractiveUiTimeout = timeout;
    }

    public int getLastSentClientStateLocked() {
        return this.mLastSentClientState;
    }

    public void setLastSentClientStateLocked(int state) {
        this.mLastSentClientState = state;
    }

    public boolean isShortcutMagnificationEnabledLocked() {
        return this.mAccessibilityShortcutKeyTargets.contains("com.android.server.accessibility.MagnificationController") || this.mAccessibilityButtonTargets.contains("com.android.server.accessibility.MagnificationController");
    }

    public int getMagnificationModeLocked(int displayId) {
        int mode = this.mMagnificationModes.get(displayId, 0);
        if (mode == 0) {
            setMagnificationModeLocked(displayId, 1);
            return 1;
        }
        return mode;
    }

    int getMagnificationCapabilitiesLocked() {
        return this.mMagnificationCapabilities;
    }

    public void setMagnificationCapabilitiesLocked(int capabilities) {
        this.mMagnificationCapabilities = capabilities;
    }

    public void setMagnificationFollowTypingEnabled(boolean enabled) {
        this.mMagnificationFollowTypingEnabled = enabled;
    }

    public boolean isMagnificationFollowTypingEnabled() {
        return this.mMagnificationFollowTypingEnabled;
    }

    public void setAlwaysOnMagnificationEnabled(boolean enabled) {
        this.mAlwaysOnMagnificationEnabled = enabled;
    }

    public boolean isAlwaysOnMagnificationEnabled() {
        return this.mAlwaysOnMagnificationEnabled;
    }

    public void setMagnificationModeLocked(int displayId, int mode) {
        this.mMagnificationModes.put(displayId, mode);
    }

    public void disableShortcutMagnificationLocked() {
        this.mAccessibilityShortcutKeyTargets.remove("com.android.server.accessibility.MagnificationController");
        this.mAccessibilityButtonTargets.remove("com.android.server.accessibility.MagnificationController");
    }

    public android.util.ArraySet<java.lang.String> getShortcutTargetsLocked(int shortcutType) {
        if (shortcutType == 2) {
            return this.mAccessibilityShortcutKeyTargets;
        }
        if (shortcutType == 1) {
            return this.mAccessibilityButtonTargets;
        }
        if (shortcutType == 16) {
            return getA11yQsTargets();
        }
        if ((shortcutType == 4 && isMagnificationSingleFingerTripleTapEnabledLocked()) || (shortcutType == 8 && isMagnificationTwoFingerTripleTapEnabledLocked())) {
            android.util.ArraySet<java.lang.String> targets = new android.util.ArraySet<>();
            targets.add("com.android.server.accessibility.MagnificationController");
            return targets;
        }
        return new android.util.ArraySet<>();
    }

    boolean updateShortcutTargetsLocked(java.util.Set<java.lang.String> newTargets, int shortcutType) {
        if ((shortcutType & 12) != 0) {
            throw new java.lang.IllegalArgumentException("Tap shortcuts cannot be updated with target sets.");
        }
        java.util.Set<java.lang.String> currentTargets = getShortcutTargetsLocked(shortcutType);
        if (newTargets.equals(currentTargets)) {
            return false;
        }
        currentTargets.clear();
        currentTargets.addAll(newTargets);
        return true;
    }

    public boolean isShortcutTargetInstalledLocked(java.lang.String name) {
        if (android.text.TextUtils.isEmpty(name)) {
            return false;
        }
        if ("com.android.server.accessibility.MagnificationController".equals(name)) {
            return true;
        }
        android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(name);
        if (componentName == null) {
            return false;
        }
        if (com.android.internal.accessibility.AccessibilityShortcutController.getFrameworkShortcutFeaturesMap().containsKey(componentName) || getInstalledServiceInfoLocked(componentName) != null) {
            return true;
        }
        for (int i = 0; i < this.mInstalledShortcuts.size(); i++) {
            if (this.mInstalledShortcuts.get(i).getComponentName().equals(componentName)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeShortcutTargetLocked(int shortcutType, final android.content.ComponentName target) {
        if (shortcutType == 4 || shortcutType == 8) {
            throw new java.lang.UnsupportedOperationException("removeShortcutTargetLocked only support shortcut type: software and hardware and quick settings for now");
        }
        java.util.Set<java.lang.String> targets = getShortcutTargetsLocked(shortcutType);
        boolean result = targets.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityUserState$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityUserState.lambda$removeShortcutTargetLocked$0(target, (java.lang.String) obj);
            }
        });
        if (shortcutType == 16) {
            updateA11yQsTargetLocked(targets);
        }
        return result;
    }

    static /* synthetic */ boolean lambda$removeShortcutTargetLocked$0(android.content.ComponentName target, java.lang.String name) {
        android.content.ComponentName componentName;
        if (name == null || (componentName = android.content.ComponentName.unflattenFromString(name)) == null) {
            return false;
        }
        return componentName.equals(target);
    }

    public android.accessibilityservice.AccessibilityServiceInfo getInstalledServiceInfoLocked(android.content.ComponentName componentName) {
        for (int i = 0; i < this.mInstalledServices.size(); i++) {
            android.accessibilityservice.AccessibilityServiceInfo serviceInfo = this.mInstalledServices.get(i);
            if (serviceInfo.getComponentName().equals(componentName)) {
                return serviceInfo;
            }
        }
        return null;
    }

    public com.android.server.accessibility.AccessibilityServiceConnection getServiceConnectionLocked(android.content.ComponentName componentName) {
        return this.mComponentNameToServiceMap.get(componentName);
    }

    public int getNonInteractiveUiTimeoutLocked() {
        return this.mNonInteractiveUiTimeout;
    }

    public void setNonInteractiveUiTimeoutLocked(int timeout) {
        this.mNonInteractiveUiTimeout = timeout;
    }

    public boolean isPerformGesturesEnabledLocked() {
        return this.mIsPerformGesturesEnabled;
    }

    public void setPerformGesturesEnabledLocked(boolean enabled) {
        this.mIsPerformGesturesEnabled = enabled;
    }

    public boolean isAccessibilityFocusOnlyInActiveWindow() {
        return this.mAccessibilityFocusOnlyInActiveWindow;
    }

    public void setAccessibilityFocusOnlyInActiveWindow(boolean enabled) {
        this.mAccessibilityFocusOnlyInActiveWindow = enabled;
    }

    public android.content.ComponentName getServiceChangingSoftKeyboardModeLocked() {
        return this.mServiceChangingSoftKeyboardMode;
    }

    public void setServiceChangingSoftKeyboardModeLocked(android.content.ComponentName serviceChangingSoftKeyboardMode) {
        this.mServiceChangingSoftKeyboardMode = serviceChangingSoftKeyboardMode;
    }

    public boolean isTextHighContrastEnabledLocked() {
        return this.mIsTextHighContrastEnabled;
    }

    public void setTextHighContrastEnabledLocked(boolean enabled) {
        this.mIsTextHighContrastEnabled = enabled;
    }

    public boolean isAudioDescriptionByDefaultEnabledLocked() {
        return this.mIsAudioDescriptionByDefaultRequested;
    }

    public void setAudioDescriptionByDefaultEnabledLocked(boolean enabled) {
        this.mIsAudioDescriptionByDefaultRequested = enabled;
    }

    public boolean isTouchExplorationEnabledLocked() {
        return this.mIsTouchExplorationEnabled;
    }

    public void setTouchExplorationEnabledLocked(boolean enabled) {
        this.mIsTouchExplorationEnabled = enabled;
    }

    public boolean isServiceHandlesDoubleTapEnabledLocked() {
        return this.mServiceHandlesDoubleTap;
    }

    public void setServiceHandlesDoubleTapLocked(boolean enabled) {
        this.mServiceHandlesDoubleTap = enabled;
    }

    public boolean isMultiFingerGesturesEnabledLocked() {
        return this.mRequestMultiFingerGestures;
    }

    public void setMultiFingerGesturesLocked(boolean enabled) {
        this.mRequestMultiFingerGestures = enabled;
    }

    public boolean isTwoFingerPassthroughEnabledLocked() {
        return this.mRequestTwoFingerPassthrough;
    }

    public void setTwoFingerPassthroughLocked(boolean enabled) {
        this.mRequestTwoFingerPassthrough = enabled;
    }

    public boolean isSendMotionEventsEnabled() {
        return this.mSendMotionEventsEnabled;
    }

    public void setSendMotionEventsEnabled(boolean mode) {
        this.mSendMotionEventsEnabled = mode;
    }

    public int getUserInteractiveUiTimeoutLocked() {
        return this.mUserInteractiveUiTimeout;
    }

    public void setUserInteractiveUiTimeoutLocked(int timeout) {
        this.mUserInteractiveUiTimeout = timeout;
    }

    public int getUserNonInteractiveUiTimeoutLocked() {
        return this.mUserNonInteractiveUiTimeout;
    }

    public void setUserNonInteractiveUiTimeoutLocked(int timeout) {
        this.mUserNonInteractiveUiTimeout = timeout;
    }

    public java.lang.String getTargetAssignedToAccessibilityButton() {
        return this.mTargetAssignedToAccessibilityButton;
    }

    public void setTargetAssignedToAccessibilityButton(java.lang.String target) {
        this.mTargetAssignedToAccessibilityButton = target;
    }

    public static boolean doesShortcutTargetsStringContain(java.util.Collection<java.lang.String> shortcutTargets, java.lang.String targetName) {
        if (shortcutTargets == null || targetName == null) {
            return false;
        }
        if (shortcutTargets.contains(targetName)) {
            return true;
        }
        android.content.ComponentName targetComponentName = android.content.ComponentName.unflattenFromString(targetName);
        if (targetComponentName == null) {
            return false;
        }
        for (java.lang.String stringName : shortcutTargets) {
            if (!android.text.TextUtils.isEmpty(stringName) && targetComponentName.equals(android.content.ComponentName.unflattenFromString(stringName))) {
                return true;
            }
        }
        return false;
    }

    public int getFocusStrokeWidthLocked() {
        return this.mFocusStrokeWidth;
    }

    public int getFocusColorLocked() {
        return this.mFocusColor;
    }

    public void setFocusAppearanceLocked(int strokeWidth, int color) {
        this.mFocusStrokeWidth = strokeWidth;
        this.mFocusColor = color;
    }

    public void setServiceDetectsGesturesEnabled(int displayId, boolean mode) {
        this.mServiceDetectsGestures.put(displayId, java.lang.Boolean.valueOf(mode));
    }

    public void resetServiceDetectsGestures() {
        this.mServiceDetectsGestures.clear();
    }

    public boolean isServiceDetectsGesturesEnabled(int displayId) {
        if (this.mServiceDetectsGestures.contains(displayId)) {
            return this.mServiceDetectsGestures.get(displayId).booleanValue();
        }
        return false;
    }

    public void updateTileServiceMapForAccessibilityServiceLocked() {
        this.mA11yServiceToTileService.clear();
        this.mInstalledServices.forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AccessibilityUserState$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$updateTileServiceMapForAccessibilityServiceLocked$1((android.accessibilityservice.AccessibilityServiceInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTileServiceMapForAccessibilityServiceLocked$1(android.accessibilityservice.AccessibilityServiceInfo a11yServiceInfo) {
        java.lang.String tileServiceName = a11yServiceInfo.getTileServiceName();
        if (!android.text.TextUtils.isEmpty(tileServiceName)) {
            android.content.pm.ResolveInfo resolveInfo = a11yServiceInfo.getResolveInfo();
            android.content.ComponentName a11yFeature = new android.content.ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
            android.content.ComponentName tileService = new android.content.ComponentName(a11yFeature.getPackageName(), tileServiceName);
            this.mA11yServiceToTileService.put(a11yFeature, tileService);
        }
    }

    public void updateTileServiceMapForAccessibilityActivityLocked() {
        this.mA11yActivityToTileService.clear();
        this.mInstalledShortcuts.forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AccessibilityUserState$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$updateTileServiceMapForAccessibilityActivityLocked$2((android.accessibilityservice.AccessibilityShortcutInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTileServiceMapForAccessibilityActivityLocked$2(android.accessibilityservice.AccessibilityShortcutInfo a11yShortcutInfo) {
        java.lang.String tileServiceName = a11yShortcutInfo.getTileServiceName();
        if (!android.text.TextUtils.isEmpty(tileServiceName)) {
            android.content.ComponentName a11yFeature = a11yShortcutInfo.getComponentName();
            android.content.ComponentName tileService = new android.content.ComponentName(a11yFeature.getPackageName(), tileServiceName);
            this.mA11yActivityToTileService.put(a11yFeature, tileService);
        }
    }

    public void updateA11yQsTargetLocked(java.util.Set<java.lang.String> targets) {
        this.mAccessibilityQsTargets.clear();
        this.mAccessibilityQsTargets.addAll(targets);
    }

    public android.util.ArraySet<java.lang.String> getA11yQsTargets() {
        return new android.util.ArraySet<>((android.util.ArraySet) this.mAccessibilityQsTargets);
    }

    public void updateA11yTilesInQsPanelLocked(java.util.Set<android.content.ComponentName> componentNames) {
        this.mA11yTilesInQsPanel.clear();
        this.mA11yTilesInQsPanel.addAll(componentNames);
    }

    public android.util.ArraySet<android.content.ComponentName> getA11yQsTilesInQsPanel() {
        return new android.util.ArraySet<>((android.util.ArraySet) this.mA11yTilesInQsPanel);
    }

    public java.util.Map<android.content.ComponentName, android.content.ComponentName> getA11yFeatureToTileService() {
        java.util.Map<android.content.ComponentName, android.content.ComponentName> featureToTileServiceMap = new android.util.ArrayMap<>();
        featureToTileServiceMap.putAll(this.mA11yServiceToTileService);
        featureToTileServiceMap.putAll(this.mA11yActivityToTileService);
        return featureToTileServiceMap;
    }

    public java.util.Map<android.content.ComponentName, android.accessibilityservice.AccessibilityServiceInfo> getTileServiceToA11yServiceInfoMapLocked() {
        java.util.Map<android.content.ComponentName, android.accessibilityservice.AccessibilityServiceInfo> tileServiceToA11yServiceInfoMap = new android.util.ArrayMap<>();
        java.util.Map<android.content.ComponentName, android.accessibilityservice.AccessibilityServiceInfo> a11yServiceToServiceInfoMap = (java.util.Map) this.mInstalledServices.stream().collect(java.util.stream.Collectors.toMap(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda51(), java.util.function.Function.identity()));
        for (java.util.Map.Entry<android.content.ComponentName, android.content.ComponentName> serviceToTile : this.mA11yServiceToTileService.entrySet()) {
            if (a11yServiceToServiceInfoMap.containsKey(serviceToTile.getKey())) {
                tileServiceToA11yServiceInfoMap.put(serviceToTile.getValue(), a11yServiceToServiceInfoMap.get(serviceToTile.getKey()));
            }
        }
        return tileServiceToA11yServiceInfoMap;
    }
}
