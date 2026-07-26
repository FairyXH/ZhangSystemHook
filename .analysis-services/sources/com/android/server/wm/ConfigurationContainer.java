package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ConfigurationContainer<E extends com.android.server.wm.ConfigurationContainer> {
    static final int BOUNDS_CHANGE_NONE = 0;
    static final int BOUNDS_CHANGE_POSITION = 1;
    static final int BOUNDS_CHANGE_SIZE = 2;
    private boolean mHasOverrideConfiguration;
    private android.graphics.Rect mReturnBounds = new android.graphics.Rect();
    private android.content.res.Configuration mRequestedOverrideConfiguration = new android.content.res.Configuration();
    private android.content.res.Configuration mResolvedOverrideConfiguration = new android.content.res.Configuration();
    private android.content.res.Configuration mFullConfiguration = new android.content.res.Configuration();
    private android.content.res.Configuration mMergedOverrideConfiguration = new android.content.res.Configuration();
    private java.util.ArrayList<com.android.server.wm.ConfigurationContainerListener> mChangeListeners = new java.util.ArrayList<>();
    private final android.content.res.Configuration mRequestsTmpConfig = new android.content.res.Configuration();
    private final android.content.res.Configuration mResolvedTmpConfig = new android.content.res.Configuration();
    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    protected abstract E getChildAt(int i);

    protected abstract int getChildCount();

    protected abstract com.android.server.wm.ConfigurationContainer getParent();

    public android.content.res.Configuration getConfiguration() {
        return this.mFullConfiguration;
    }

    public void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
        this.mResolvedTmpConfig.setTo(this.mResolvedOverrideConfiguration);
        resolveOverrideConfiguration(newParentConfig);
        this.mFullConfiguration.setTo(newParentConfig);
        this.mFullConfiguration.windowConfiguration.unsetAlwaysOnTop();
        this.mFullConfiguration.updateFrom(this.mResolvedOverrideConfiguration);
        onMergedOverrideConfigurationChanged();
        if (!this.mResolvedTmpConfig.equals(this.mResolvedOverrideConfiguration)) {
            for (int i = this.mChangeListeners.size() - 1; i >= 0; i--) {
                this.mChangeListeners.get(i).onRequestedOverrideConfigurationChanged(this.mResolvedOverrideConfiguration);
            }
        }
        for (int i2 = this.mChangeListeners.size() - 1; i2 >= 0; i2--) {
            this.mChangeListeners.get(i2).onMergedOverrideConfigurationChanged(this.mMergedOverrideConfiguration);
        }
        int i3 = getChildCount();
        for (int i4 = i3 - 1; i4 >= 0; i4--) {
            dispatchConfigurationToChild(getChildAt(i4), this.mFullConfiguration);
        }
    }

    void dispatchConfigurationToChild(E child, android.content.res.Configuration config) {
        child.onConfigurationChanged(config);
    }

    void resolveOverrideConfiguration(android.content.res.Configuration newParentConfig) {
        this.mResolvedOverrideConfiguration.setTo(this.mRequestedOverrideConfiguration);
    }

    static void applySizeOverrideIfNeeded(com.android.server.wm.DisplayContent displayContent, android.content.pm.ApplicationInfo appInfo, android.content.res.Configuration newParentConfiguration, android.content.res.Configuration inOutConfig, boolean optsOutEdgeToEdge, boolean hasFixedRotationTransform, boolean hasCompatDisplayInsets) {
        applySizeOverrideIfNeeded(displayContent, appInfo, newParentConfiguration, inOutConfig, optsOutEdgeToEdge, hasFixedRotationTransform, hasCompatDisplayInsets, null);
    }

    static void applySizeOverrideIfNeeded(com.android.server.wm.DisplayContent displayContent, android.content.pm.ApplicationInfo appInfo, android.content.res.Configuration newParentConfiguration, android.content.res.Configuration inOutConfig, boolean optsOutEdgeToEdge, boolean hasFixedRotationTransform, boolean hasCompatDisplayInsets, com.android.server.wm.IActivityRecordExt activityRecordExt) {
        boolean zIsChangeEnabled;
        int rotation;
        int i;
        int i2;
        android.graphics.Rect outAppBounds;
        if (displayContent == null) {
            return;
        }
        if (displayContent.mWmService.mFlags.mInsetsDecoupledConfiguration) {
            zIsChangeEnabled = (appInfo.isChangeEnabled(151861875L) || appInfo.isChangeEnabled(327313645L)) ? false : true;
        } else {
            zIsChangeEnabled = appInfo.isChangeEnabled(327313645L);
        }
        boolean useOverrideInsetsForConfig = zIsChangeEnabled;
        int parentWindowingMode = newParentConfiguration.windowConfiguration.getWindowingMode();
        boolean isFloating = android.app.WindowConfiguration.isFloating(parentWindowingMode) && (inOutConfig.windowConfiguration.getWindowingMode() == 0 || android.app.WindowConfiguration.isFloating(inOutConfig.windowConfiguration.getWindowingMode()));
        int rotation2 = newParentConfiguration.windowConfiguration.getRotation();
        if (rotation2 == -1 && !hasFixedRotationTransform) {
            rotation = displayContent.getRotation();
        } else {
            rotation = rotation2;
        }
        boolean skipApplySizeOverride = activityRecordExt != null && activityRecordExt.skipApplySizeOverride(newParentConfiguration);
        if (!optsOutEdgeToEdge && (!useOverrideInsetsForConfig || hasCompatDisplayInsets || isFloating || rotation == -1)) {
            return;
        }
        if (hasFixedRotationTransform) {
            inOutConfig.windowConfiguration.setAppBounds((android.graphics.Rect) null);
            inOutConfig.screenWidthDp = 0;
            inOutConfig.screenHeightDp = 0;
            inOutConfig.smallestScreenWidthDp = 0;
            inOutConfig.orientation = 0;
        }
        boolean rotated = rotation == 1 || rotation == 3;
        if (rotated) {
            i = displayContent.mBaseDisplayHeight;
        } else {
            i = displayContent.mBaseDisplayWidth;
        }
        int dw = i;
        if (rotated) {
            i2 = displayContent.mBaseDisplayWidth;
        } else {
            i2 = displayContent.mBaseDisplayHeight;
        }
        int dh = i2;
        android.graphics.Rect outAppBounds2 = inOutConfig.windowConfiguration.getAppBounds();
        if (outAppBounds2 == null || outAppBounds2.isEmpty()) {
            inOutConfig.windowConfiguration.setAppBounds(newParentConfiguration.windowConfiguration.getBounds());
            android.graphics.Rect outAppBounds3 = inOutConfig.windowConfiguration.getAppBounds();
            if (!skipApplySizeOverride) {
                outAppBounds3.inset(displayContent.getDisplayPolicy().getDecorInsetsInfo(rotation, dw, dh).mOverrideNonDecorInsets);
            }
            outAppBounds = outAppBounds3;
        } else {
            outAppBounds = outAppBounds2;
        }
        float density = inOutConfig.densityDpi;
        if (density == 0.0f) {
            density = newParentConfiguration.densityDpi;
        }
        float density2 = density * 0.00625f;
        if (inOutConfig.screenWidthDp == 0) {
            inOutConfig.screenWidthDp = (int) ((outAppBounds.width() / density2) + 0.5f);
        }
        if (inOutConfig.screenHeightDp == 0) {
            inOutConfig.screenHeightDp = (int) ((outAppBounds.height() / density2) + 0.5f);
        }
        if (inOutConfig.smallestScreenWidthDp == 0 && parentWindowingMode == 1 && !skipApplySizeOverride) {
            android.view.DisplayInfo info = new android.view.DisplayInfo(displayContent.getDisplayInfo());
            displayContent.computeSizeRanges(info, rotated, dw, dh, displayContent.getDisplayMetrics().density, inOutConfig, true);
        }
        if (inOutConfig.orientation == 0) {
            inOutConfig.orientation = inOutConfig.screenWidthDp > inOutConfig.screenHeightDp ? 2 : 1;
        }
    }

    boolean hasRequestedOverrideConfiguration() {
        return this.mHasOverrideConfiguration;
    }

    public android.content.res.Configuration getRequestedOverrideConfiguration() {
        return this.mRequestedOverrideConfiguration;
    }

    android.content.res.Configuration getResolvedOverrideConfiguration() {
        return this.mResolvedOverrideConfiguration;
    }

    public void onRequestedOverrideConfigurationChanged(android.content.res.Configuration overrideConfiguration) {
        updateRequestedOverrideConfiguration(overrideConfiguration);
        com.android.server.wm.ConfigurationContainer parent = getParent();
        onConfigurationChanged(parent != null ? parent.getConfiguration() : android.content.res.Configuration.EMPTY);
    }

    void updateRequestedOverrideConfiguration(android.content.res.Configuration overrideConfiguration) {
        this.mHasOverrideConfiguration = !android.content.res.Configuration.EMPTY.equals(overrideConfiguration);
        this.mRequestedOverrideConfiguration.setTo(overrideConfiguration);
        android.graphics.Rect newBounds = this.mRequestedOverrideConfiguration.windowConfiguration.getBounds();
        if (this.mHasOverrideConfiguration && providesMaxBounds() && diffRequestedOverrideMaxBounds(newBounds) != 0) {
            this.mRequestedOverrideConfiguration.windowConfiguration.setMaxBounds(newBounds);
        }
    }

    public android.content.res.Configuration getMergedOverrideConfiguration() {
        return this.mMergedOverrideConfiguration;
    }

    void onMergedOverrideConfigurationChanged() {
        com.android.server.wm.ConfigurationContainer parent = getParent();
        if (parent != null) {
            this.mMergedOverrideConfiguration.setTo(parent.getMergedOverrideConfiguration());
            this.mMergedOverrideConfiguration.windowConfiguration.unsetAlwaysOnTop();
            this.mMergedOverrideConfiguration.updateFrom(this.mResolvedOverrideConfiguration);
            return;
        }
        this.mMergedOverrideConfiguration.setTo(this.mResolvedOverrideConfiguration);
    }

    public boolean matchParentBounds() {
        return getResolvedOverrideBounds().isEmpty();
    }

    public boolean equivalentRequestedOverrideBounds(android.graphics.Rect bounds) {
        return equivalentBounds(getRequestedOverrideBounds(), bounds);
    }

    public boolean equivalentRequestedOverrideMaxBounds(android.graphics.Rect bounds) {
        return equivalentBounds(getRequestedOverrideMaxBounds(), bounds);
    }

    public static boolean equivalentBounds(android.graphics.Rect bounds, android.graphics.Rect other) {
        return bounds == other || (bounds != null && (bounds.equals(other) || (bounds.isEmpty() && other == null))) || (other != null && other.isEmpty() && bounds == null);
    }

    public android.graphics.Rect getBounds() {
        this.mReturnBounds.set(getConfiguration().windowConfiguration.getBounds());
        return this.mReturnBounds;
    }

    public void getBounds(android.graphics.Rect outBounds) {
        outBounds.set(getBounds());
    }

    public android.graphics.Rect getMaxBounds() {
        this.mReturnBounds.set(getConfiguration().windowConfiguration.getMaxBounds());
        return this.mReturnBounds;
    }

    public void getPosition(android.graphics.Point out) {
        android.graphics.Rect bounds = getBounds();
        out.set(bounds.left, bounds.top);
    }

    android.graphics.Rect getResolvedOverrideBounds() {
        this.mReturnBounds.set(getResolvedOverrideConfiguration().windowConfiguration.getBounds());
        return this.mReturnBounds;
    }

    public android.graphics.Rect getRequestedOverrideBounds() {
        this.mReturnBounds.set(getRequestedOverrideConfiguration().windowConfiguration.getBounds());
        return this.mReturnBounds;
    }

    public android.graphics.Rect getRequestedOverrideMaxBounds() {
        this.mReturnBounds.set(getRequestedOverrideConfiguration().windowConfiguration.getMaxBounds());
        return this.mReturnBounds;
    }

    public boolean hasOverrideBounds() {
        return !getRequestedOverrideBounds().isEmpty();
    }

    public void getRequestedOverrideBounds(android.graphics.Rect outBounds) {
        outBounds.set(getRequestedOverrideBounds());
    }

    public int setBounds(android.graphics.Rect bounds) {
        int boundsChange = diffRequestedOverrideBounds(bounds);
        boolean overrideMaxBounds = providesMaxBounds() && diffRequestedOverrideMaxBounds(bounds) != 0;
        if (boundsChange == 0 && !overrideMaxBounds) {
            return boundsChange;
        }
        this.mRequestsTmpConfig.setTo(getRequestedOverrideConfiguration());
        this.mRequestsTmpConfig.windowConfiguration.setBounds(bounds);
        onRequestedOverrideConfigurationChanged(this.mRequestsTmpConfig);
        return boundsChange;
    }

    public int setBounds(int left, int top, int right, int bottom) {
        this.mTmpRect.set(left, top, right, bottom);
        return setBounds(this.mTmpRect);
    }

    protected boolean providesMaxBounds() {
        return false;
    }

    int diffRequestedOverrideMaxBounds(android.graphics.Rect bounds) {
        if (equivalentRequestedOverrideMaxBounds(bounds)) {
            return 0;
        }
        int boundsChange = 0;
        android.graphics.Rect existingBounds = getRequestedOverrideMaxBounds();
        if (bounds == null || existingBounds.left != bounds.left || existingBounds.top != bounds.top) {
            boundsChange = 0 | 1;
        }
        if (bounds == null || existingBounds.width() != bounds.width() || existingBounds.height() != bounds.height()) {
            return boundsChange | 2;
        }
        return boundsChange;
    }

    int diffRequestedOverrideBounds(android.graphics.Rect bounds) {
        if (equivalentRequestedOverrideBounds(bounds)) {
            return 0;
        }
        int boundsChange = 0;
        android.graphics.Rect existingBounds = getRequestedOverrideBounds();
        if (bounds == null || existingBounds.left != bounds.left || existingBounds.top != bounds.top) {
            boundsChange = 0 | 1;
        }
        if (bounds == null || existingBounds.width() != bounds.width() || existingBounds.height() != bounds.height()) {
            return boundsChange | 2;
        }
        return boundsChange;
    }

    public android.app.WindowConfiguration getWindowConfiguration() {
        return this.mFullConfiguration.windowConfiguration;
    }

    public int getWindowingMode() {
        return this.mFullConfiguration.windowConfiguration.getWindowingMode();
    }

    public int getRequestedOverrideWindowingMode() {
        return this.mRequestedOverrideConfiguration.windowConfiguration.getWindowingMode();
    }

    public void setWindowingMode(int windowingMode) {
        this.mRequestsTmpConfig.setTo(getRequestedOverrideConfiguration());
        this.mRequestsTmpConfig.windowConfiguration.setWindowingMode(windowingMode);
        onRequestedOverrideConfigurationChanged(this.mRequestsTmpConfig);
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.mRequestsTmpConfig.setTo(getRequestedOverrideConfiguration());
        this.mRequestsTmpConfig.windowConfiguration.setAlwaysOnTop(alwaysOnTop);
        onRequestedOverrideConfigurationChanged(this.mRequestsTmpConfig);
    }

    public boolean inMultiWindowMode() {
        int windowingMode = this.mFullConfiguration.windowConfiguration.getWindowingMode();
        return android.app.WindowConfiguration.inMultiWindowMode(windowingMode);
    }

    public boolean supportsSplitScreenWindowingMode() {
        return this.mFullConfiguration.windowConfiguration.supportSplitScreenWindowingMode();
    }

    public boolean inPinnedWindowingMode() {
        return this.mFullConfiguration.windowConfiguration.getWindowingMode() == 2;
    }

    public boolean inFreeformWindowingMode() {
        return this.mFullConfiguration.windowConfiguration.getWindowingMode() == 5;
    }

    public int getActivityType() {
        return this.mFullConfiguration.windowConfiguration.getActivityType();
    }

    public void setActivityType(int activityType) {
        int currentActivityType = getActivityType();
        if (currentActivityType == activityType) {
            return;
        }
        if (currentActivityType != 0) {
            throw new java.lang.IllegalStateException("Can't change activity type once set: " + this + " activityType=" + android.app.WindowConfiguration.activityTypeToString(activityType));
        }
        this.mRequestsTmpConfig.setTo(getRequestedOverrideConfiguration());
        this.mRequestsTmpConfig.windowConfiguration.setActivityType(activityType);
        onRequestedOverrideConfigurationChanged(this.mRequestsTmpConfig);
    }

    public boolean isActivityTypeHome() {
        return getActivityType() == 2;
    }

    public boolean isActivityTypeRecents() {
        return getActivityType() == 3;
    }

    final boolean isActivityTypeHomeOrRecents() {
        int activityType = getActivityType();
        return activityType == 2 || activityType == 3;
    }

    public boolean isActivityTypeAssistant() {
        return getActivityType() == 4;
    }

    public boolean applyAppSpecificConfig(java.lang.Integer nightMode, android.os.LocaleList locales, java.lang.Integer gender) {
        this.mRequestsTmpConfig.setTo(getRequestedOverrideConfiguration());
        boolean newNightModeSet = nightMode != null && setOverrideNightMode(this.mRequestsTmpConfig, nightMode.intValue());
        boolean newLocalesSet = locales != null && setOverrideLocales(this.mRequestsTmpConfig, locales);
        boolean newGenderSet = setOverrideGender(this.mRequestsTmpConfig, gender == null ? 0 : gender.intValue());
        if (newNightModeSet || newLocalesSet || newGenderSet) {
            onRequestedOverrideConfigurationChanged(this.mRequestsTmpConfig);
        }
        return newNightModeSet || newLocalesSet || newGenderSet;
    }

    private boolean setOverrideNightMode(android.content.res.Configuration requestsTmpConfig, int nightMode) {
        int currentUiMode = this.mRequestedOverrideConfiguration.uiMode;
        int currentNightMode = currentUiMode & 48;
        int validNightMode = nightMode & 48;
        if (currentNightMode == validNightMode) {
            return false;
        }
        requestsTmpConfig.uiMode = (currentUiMode & (-49)) | validNightMode;
        return true;
    }

    private boolean setOverrideLocales(android.content.res.Configuration requestsTmpConfig, android.os.LocaleList overrideLocales) {
        if (this.mRequestedOverrideConfiguration.getLocales().equals(overrideLocales)) {
            return false;
        }
        requestsTmpConfig.setLocales(overrideLocales);
        requestsTmpConfig.userSetLocale = true;
        return true;
    }

    protected boolean setOverrideGender(android.content.res.Configuration requestsTmpConfig, int gender) {
        return false;
    }

    public boolean isActivityTypeDream() {
        return getActivityType() == 5;
    }

    public boolean isActivityTypeStandard() {
        return getActivityType() == 1;
    }

    public boolean isActivityTypeStandardOrUndefined() {
        int activityType = getActivityType();
        return activityType == 1 || activityType == 0;
    }

    public static boolean isCompatibleActivityType(int currentType, int otherType) {
        if (currentType == otherType) {
            return true;
        }
        if (currentType == 4) {
            return false;
        }
        return currentType == 0 || otherType == 0;
    }

    public boolean isCompatible(int windowingMode, int activityType) {
        int thisActivityType = getActivityType();
        int thisWindowingMode = getWindowingMode();
        boolean sameActivityType = thisActivityType == activityType;
        boolean sameWindowingMode = thisWindowingMode == windowingMode;
        if (sameActivityType && sameWindowingMode) {
            return true;
        }
        if ((activityType != 0 && activityType != 1) || !isActivityTypeStandardOrUndefined()) {
            return sameActivityType;
        }
        return sameWindowingMode;
    }

    void registerConfigurationChangeListener(com.android.server.wm.ConfigurationContainerListener listener) {
        registerConfigurationChangeListener(listener, true);
    }

    void registerConfigurationChangeListener(com.android.server.wm.ConfigurationContainerListener listener, boolean shouldDispatchConfig) {
        if (this.mChangeListeners.contains(listener)) {
            return;
        }
        this.mChangeListeners.add(listener);
        if (shouldDispatchConfig) {
            listener.onRequestedOverrideConfigurationChanged(this.mResolvedOverrideConfiguration);
            listener.onMergedOverrideConfigurationChanged(this.mMergedOverrideConfiguration);
        }
    }

    void unregisterConfigurationChangeListener(com.android.server.wm.ConfigurationContainerListener listener) {
        this.mChangeListeners.remove(listener);
    }

    boolean containsListener(com.android.server.wm.ConfigurationContainerListener listener) {
        return this.mChangeListeners.contains(listener);
    }

    void onParentChanged(com.android.server.wm.ConfigurationContainer newParent, com.android.server.wm.ConfigurationContainer oldParent) {
        if (newParent != null) {
            onConfigurationChanged(newParent.mFullConfiguration);
        }
    }

    protected void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        long token = proto.start(fieldId);
        if (logLevel == 0 || this.mHasOverrideConfiguration) {
            this.mRequestedOverrideConfiguration.dumpDebug(proto, 1146756268033L, logLevel == 2);
        }
        if (logLevel == 0) {
            this.mFullConfiguration.dumpDebug(proto, 1146756268034L, false);
            this.mMergedOverrideConfiguration.dumpDebug(proto, 1146756268035L, false);
        }
        if (logLevel == 1) {
            dumpDebugWindowingMode(proto);
        }
        proto.end(token);
    }

    private void dumpDebugWindowingMode(android.util.proto.ProtoOutputStream proto) {
        long fullConfigToken = proto.start(1146756268034L);
        long windowConfigToken = proto.start(1146756268051L);
        int windowingMode = this.mFullConfiguration.windowConfiguration.getWindowingMode();
        proto.write(1120986464258L, windowingMode);
        proto.end(windowConfigToken);
        proto.end(fullConfigToken);
    }

    public void dumpChildrenNames(java.io.PrintWriter pw, java.lang.String prefix) {
        dumpChildrenNames(pw, prefix, true);
    }

    public void dumpChildrenNames(java.io.PrintWriter pw, java.lang.String prefix, boolean isLastChild) {
        int curWinMode = getWindowingMode();
        java.lang.String winMode = android.app.WindowConfiguration.windowingModeToString(curWinMode);
        if (curWinMode != 0 && curWinMode != 1) {
            winMode = winMode.toUpperCase();
        }
        int requestedWinMode = getRequestedOverrideWindowingMode();
        java.lang.String overrideWinMode = android.app.WindowConfiguration.windowingModeToString(requestedWinMode);
        if (requestedWinMode != 0 && requestedWinMode != 1) {
            overrideWinMode = overrideWinMode.toUpperCase();
        }
        java.lang.String actType = android.app.WindowConfiguration.activityTypeToString(getActivityType());
        if (getActivityType() != 0 && getActivityType() != 1) {
            actType = actType.toUpperCase();
        }
        pw.print(prefix + (isLastChild ? "└─ " : "├─ "));
        pw.println(getName() + " type=" + actType + " mode=" + winMode + " override-mode=" + overrideWinMode + " requested-bounds=" + getRequestedOverrideBounds().toShortString() + " bounds=" + getBounds().toShortString());
        java.lang.String childPrefix = prefix + (isLastChild ? "   " : "│  ");
        int i = getChildCount() - 1;
        while (i >= 0) {
            getChildAt(i).dumpChildrenNames(pw, childPrefix, i == 0);
            i--;
        }
    }

    java.lang.String getName() {
        return toString();
    }

    public boolean isAlwaysOnTop() {
        return this.mFullConfiguration.windowConfiguration.isAlwaysOnTop();
    }

    boolean hasChild() {
        return getChildCount() > 0;
    }
}
