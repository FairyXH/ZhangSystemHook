package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class LogicalDisplay {
    private static final int BLANK_LAYER_STACK = -1;
    static final float BRIGHTNESS_OFF_FLOAT = -1.0f;
    static final int DISPLAY_PHASE_DISABLED = -1;
    static final int DISPLAY_PHASE_ENABLED = 1;
    static final int DISPLAY_PHASE_ENABLED_DISPLAY_OFF = 10;
    static final int DISPLAY_PHASE_LAYOUT_TRANSITION = 0;
    private static final android.view.DisplayInfo EMPTY_DISPLAY_INFO = new android.view.DisplayInfo();
    private static final java.lang.String TAG = "LogicalDisplay";
    private boolean mAlwaysRotateDisplayDeviceEnabled;
    private final android.view.DisplayInfo mBaseDisplayInfo;
    private com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs mDesiredDisplayModeSpecs;
    private int mDevicePosition;
    private boolean mDirty;
    private int mDisplayGroupId;
    private java.lang.String mDisplayGroupName;
    private final int mDisplayId;
    private com.android.server.display.DisplayOffloadSessionImpl mDisplayOffloadSession;
    private int mDisplayOffsetX;
    private int mDisplayOffsetY;
    private final android.graphics.Point mDisplayPosition;
    private boolean mDisplayScalingDisabled;
    private android.view.DisplayEventReceiver.FrameRateOverride[] mFrameRateOverrides;
    private boolean mHasContent;
    private final com.android.server.display.DisplayInfoProxy mInfo;
    private final boolean mIsAnisotropyCorrectionEnabled;
    private boolean mIsEnabled;
    private boolean mIsInTransition;
    private final int mLayerStack;
    private android.view.SurfaceControl.RefreshRateRange mLayoutLimitedRefreshRate;
    private int mLeadDisplayId;
    private com.android.server.display.ILogicalDisplayExt mLogicalDisplayExt;
    private android.view.DisplayInfo mOverrideDisplayInfo;
    private android.util.ArraySet<java.lang.Integer> mPendingFrameRateOverrideUids;
    private java.lang.String mPowerThrottlingDataId;
    private com.android.server.display.DisplayDevice mPrimaryDisplayDevice;
    private com.android.server.display.DisplayDeviceInfo mPrimaryDisplayDeviceInfo;
    private int mRequestedColorMode;
    private boolean mRequestedMinimalPostProcessing;
    private final android.graphics.Rect mTempDisplayRect;
    private final android.util.SparseArray<java.lang.Float> mTempFrameRateOverride;
    private final android.graphics.Rect mTempLayerStackRect;
    private java.lang.String mThermalBrightnessThrottlingDataId;
    private android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> mThermalRefreshRateThrottling;
    private int[] mUserDisabledHdrTypes;

    @interface DisplayPhase {
    }

    public com.android.server.display.ILogicalDisplayExt getLogicalDisplayExt() {
        return this.mLogicalDisplayExt;
    }

    LogicalDisplay(int displayId, int layerStack, com.android.server.display.DisplayDevice primaryDisplayDevice) {
        this(displayId, layerStack, primaryDisplayDevice, false, false);
    }

    LogicalDisplay(int displayId, int layerStack, com.android.server.display.DisplayDevice primaryDisplayDevice, boolean isAnisotropyCorrectionEnabled, boolean isAlwaysRotateDisplayDeviceEnabled) {
        this.mBaseDisplayInfo = new android.view.DisplayInfo();
        this.mLeadDisplayId = -1;
        this.mDisplayGroupId = -1;
        this.mInfo = new com.android.server.display.DisplayInfoProxy(null);
        this.mUserDisabledHdrTypes = new int[0];
        this.mDesiredDisplayModeSpecs = new com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs();
        this.mDisplayPosition = new android.graphics.Point();
        this.mTempLayerStackRect = new android.graphics.Rect();
        this.mTempDisplayRect = new android.graphics.Rect();
        this.mLogicalDisplayExt = (com.android.server.display.ILogicalDisplayExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.ILogicalDisplayExt.class).base(this).create();
        this.mDevicePosition = -1;
        this.mDirty = false;
        this.mThermalRefreshRateThrottling = new android.util.SparseArray<>();
        this.mDisplayId = displayId;
        this.mLayerStack = layerStack;
        this.mPrimaryDisplayDevice = primaryDisplayDevice;
        this.mPendingFrameRateOverrideUids = new android.util.ArraySet<>();
        this.mTempFrameRateOverride = new android.util.SparseArray<>();
        this.mIsEnabled = true;
        this.mIsInTransition = false;
        this.mThermalBrightnessThrottlingDataId = "default";
        this.mPowerThrottlingDataId = "default";
        this.mBaseDisplayInfo.thermalBrightnessThrottlingDataId = this.mThermalBrightnessThrottlingDataId;
        this.mIsAnisotropyCorrectionEnabled = isAnisotropyCorrectionEnabled;
        this.mAlwaysRotateDisplayDeviceEnabled = isAlwaysRotateDisplayDeviceEnabled;
    }

    public void setDevicePositionLocked(int position) {
        if (this.mDevicePosition != position) {
            this.mDevicePosition = position;
            this.mDirty = true;
        }
    }

    public int getDevicePositionLocked() {
        return this.mDevicePosition;
    }

    public int getDisplayIdLocked() {
        return this.mDisplayId;
    }

    public com.android.server.display.DisplayDevice getPrimaryDisplayDeviceLocked() {
        return this.mPrimaryDisplayDevice;
    }

    public android.view.DisplayInfo getDisplayInfoLocked() {
        if (this.mInfo.get() == null) {
            android.view.DisplayInfo info = new android.view.DisplayInfo();
            com.android.server.wm.utils.DisplayInfoOverrides.copyDisplayInfoFields(info, this.mBaseDisplayInfo, this.mOverrideDisplayInfo, com.android.server.wm.utils.DisplayInfoOverrides.WM_OVERRIDE_FIELDS);
            this.mInfo.set(info);
        }
        return this.mInfo.get();
    }

    public android.view.DisplayEventReceiver.FrameRateOverride[] getFrameRateOverrides() {
        return this.mFrameRateOverrides;
    }

    public android.util.ArraySet<java.lang.Integer> getPendingFrameRateOverrideUids() {
        return this.mPendingFrameRateOverrideUids;
    }

    public void clearPendingFrameRateOverrideUids() {
        this.mPendingFrameRateOverrideUids = new android.util.ArraySet<>();
    }

    void getNonOverrideDisplayInfoLocked(android.view.DisplayInfo outInfo) {
        outInfo.copyFrom(this.mBaseDisplayInfo);
    }

    public boolean setDisplayInfoOverrideFromWindowManagerLocked(android.view.DisplayInfo info) {
        if (info != null) {
            if (!android.text.TextUtils.equals(info.uniqueId, this.mBaseDisplayInfo.uniqueId)) {
                android.util.Slog.i(TAG, "OverrideDisplayInfo uniqueId not match baseDisplayInfo");
                return false;
            }
            if (this.mOverrideDisplayInfo == null) {
                this.mOverrideDisplayInfo = new android.view.DisplayInfo(info);
                this.mInfo.set(null);
                return true;
            }
            if (!this.mOverrideDisplayInfo.equals(info)) {
                this.mOverrideDisplayInfo.copyFrom(info);
                this.mInfo.set(null);
                return true;
            }
        } else if (this.mOverrideDisplayInfo != null) {
            this.mOverrideDisplayInfo = null;
            this.mInfo.set(null);
            return true;
        }
        return false;
    }

    public boolean isValidLocked() {
        return this.mPrimaryDisplayDevice != null;
    }

    boolean isDirtyLocked() {
        return this.mDirty;
    }

    public void updateDisplayGroupIdLocked(int groupId) {
        if (groupId != this.mDisplayGroupId) {
            this.mDisplayGroupId = groupId;
            this.mDirty = true;
        }
    }

    public void updateLayoutLimitedRefreshRateLocked(android.view.SurfaceControl.RefreshRateRange layoutLimitedRefreshRate) {
        if (!java.util.Objects.equals(layoutLimitedRefreshRate, this.mLayoutLimitedRefreshRate)) {
            this.mLayoutLimitedRefreshRate = layoutLimitedRefreshRate;
            this.mDirty = true;
        }
    }

    public void updateThermalRefreshRateThrottling(android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> refreshRanges) {
        if (refreshRanges == null) {
            refreshRanges = new android.util.SparseArray<>();
        }
        if (!this.mThermalRefreshRateThrottling.contentEquals(refreshRanges)) {
            this.mThermalRefreshRateThrottling = refreshRanges;
            this.mDirty = true;
        }
    }

    public void updateLocked(com.android.server.display.DisplayDeviceRepository deviceRepo, com.android.server.display.mode.SyntheticModeManager syntheticModeManager) {
        if (this.mPrimaryDisplayDevice == null) {
            android.util.Slog.w(TAG, "updateLocked primaryDisplayDevice null");
            return;
        }
        if (!deviceRepo.containsLocked(this.mPrimaryDisplayDevice)) {
            setPrimaryDisplayDeviceLocked(null);
            return;
        }
        com.android.server.display.DisplayDeviceInfo deviceInfo = this.mPrimaryDisplayDevice.getDisplayDeviceInfoLocked();
        com.android.server.display.DisplayDeviceConfig config = this.mPrimaryDisplayDevice.getDisplayDeviceConfig();
        if (!java.util.Objects.equals(this.mPrimaryDisplayDeviceInfo, deviceInfo) || this.mDirty) {
            this.mBaseDisplayInfo.layerStack = this.mLayerStack;
            this.mBaseDisplayInfo.flags = 0;
            this.mBaseDisplayInfo.removeMode = 0;
            if ((deviceInfo.flags & 8) != 0) {
                this.mBaseDisplayInfo.flags |= 1;
            }
            if ((deviceInfo.flags & 4) != 0) {
                this.mBaseDisplayInfo.flags |= 2;
            }
            if ((deviceInfo.flags & 16) != 0) {
                this.mBaseDisplayInfo.flags |= 4;
                this.mBaseDisplayInfo.removeMode = 1;
            }
            if ((deviceInfo.flags & 1024) != 0) {
                this.mBaseDisplayInfo.removeMode = 1;
            }
            if ((deviceInfo.flags & 64) != 0) {
                this.mBaseDisplayInfo.flags |= 8;
            }
            if ((deviceInfo.flags & 256) != 0) {
                this.mBaseDisplayInfo.flags |= 16;
            }
            if ((deviceInfo.flags & 512) != 0) {
                this.mBaseDisplayInfo.flags |= 32;
            }
            if ((deviceInfo.flags & 4096) != 0) {
                this.mBaseDisplayInfo.flags |= 64;
            }
            if ((deviceInfo.flags & 8192) != 0) {
                this.mBaseDisplayInfo.flags |= 128;
            }
            if ((deviceInfo.flags & 16384) != 0) {
                this.mBaseDisplayInfo.flags |= 256;
            }
            if ((deviceInfo.flags & 32768) != 0) {
                this.mBaseDisplayInfo.flags |= 512;
            }
            if ((deviceInfo.flags & 2) != 0) {
                this.mBaseDisplayInfo.flags |= 16384;
            }
            if ((deviceInfo.flags & 65536) != 0) {
                this.mBaseDisplayInfo.flags |= 1024;
            }
            if ((deviceInfo.flags & 131072) != 0) {
                this.mBaseDisplayInfo.flags |= 2048;
            }
            this.mLogicalDisplayExt.setDisplayInfoFlags(deviceInfo, this.mBaseDisplayInfo, this.mDisplayId);
            if ((deviceInfo.flags & 524288) != 0) {
                this.mBaseDisplayInfo.flags |= 4096;
            }
            android.graphics.Rect maskingInsets = getMaskingInsets(deviceInfo);
            int maskedWidth = (deviceInfo.width - maskingInsets.left) - maskingInsets.right;
            int maskedHeight = (deviceInfo.height - maskingInsets.top) - maskingInsets.bottom;
            if (this.mIsAnisotropyCorrectionEnabled && deviceInfo.type == 2 && deviceInfo.xDpi > 0.0f && deviceInfo.yDpi > 0.0f) {
                if (deviceInfo.xDpi > deviceInfo.yDpi * 1.025f) {
                    maskedHeight = (int) (((double) ((maskedHeight * deviceInfo.xDpi) / deviceInfo.yDpi)) + 0.5d);
                } else if (deviceInfo.xDpi * 1.025f < deviceInfo.yDpi) {
                    maskedWidth = (int) (((double) ((maskedWidth * deviceInfo.yDpi) / deviceInfo.xDpi)) + 0.5d);
                }
            }
            this.mBaseDisplayInfo.type = deviceInfo.type;
            this.mBaseDisplayInfo.address = deviceInfo.address;
            this.mBaseDisplayInfo.deviceProductInfo = deviceInfo.deviceProductInfo;
            this.mBaseDisplayInfo.name = deviceInfo.name;
            this.mBaseDisplayInfo.uniqueId = deviceInfo.uniqueId;
            this.mBaseDisplayInfo.appWidth = maskedWidth;
            this.mBaseDisplayInfo.appHeight = maskedHeight;
            this.mBaseDisplayInfo.logicalWidth = maskedWidth;
            this.mBaseDisplayInfo.logicalHeight = maskedHeight;
            this.mBaseDisplayInfo.rotation = 0;
            this.mBaseDisplayInfo.modeId = deviceInfo.modeId;
            this.mBaseDisplayInfo.renderFrameRate = deviceInfo.renderFrameRate;
            this.mBaseDisplayInfo.defaultModeId = deviceInfo.defaultModeId;
            this.mBaseDisplayInfo.userPreferredModeId = deviceInfo.userPreferredModeId;
            this.mBaseDisplayInfo.supportedModes = (android.view.Display.Mode[]) java.util.Arrays.copyOf(deviceInfo.supportedModes, deviceInfo.supportedModes.length);
            this.mBaseDisplayInfo.appsSupportedModes = syntheticModeManager.createAppSupportedModes(config, this.mBaseDisplayInfo.supportedModes);
            this.mBaseDisplayInfo.colorMode = deviceInfo.colorMode;
            this.mBaseDisplayInfo.supportedColorModes = java.util.Arrays.copyOf(deviceInfo.supportedColorModes, deviceInfo.supportedColorModes.length);
            this.mBaseDisplayInfo.hdrCapabilities = deviceInfo.hdrCapabilities;
            this.mBaseDisplayInfo.userDisabledHdrTypes = this.mUserDisabledHdrTypes;
            this.mBaseDisplayInfo.minimalPostProcessingSupported = deviceInfo.allmSupported || deviceInfo.gameContentTypeSupported;
            this.mBaseDisplayInfo.logicalDensityDpi = deviceInfo.densityDpi;
            this.mBaseDisplayInfo.physicalXDpi = deviceInfo.xDpi;
            this.mBaseDisplayInfo.physicalYDpi = deviceInfo.yDpi;
            this.mBaseDisplayInfo.appVsyncOffsetNanos = deviceInfo.appVsyncOffsetNanos;
            this.mBaseDisplayInfo.presentationDeadlineNanos = deviceInfo.presentationDeadlineNanos;
            this.mBaseDisplayInfo.state = deviceInfo.state;
            this.mBaseDisplayInfo.committedState = deviceInfo.committedState;
            this.mBaseDisplayInfo.smallestNominalAppWidth = maskedWidth;
            this.mBaseDisplayInfo.smallestNominalAppHeight = maskedHeight;
            this.mBaseDisplayInfo.largestNominalAppWidth = maskedWidth;
            this.mBaseDisplayInfo.largestNominalAppHeight = maskedHeight;
            this.mBaseDisplayInfo.ownerUid = deviceInfo.ownerUid;
            this.mBaseDisplayInfo.ownerPackageName = deviceInfo.ownerPackageName;
            boolean maskCutout = (deviceInfo.flags & 2048) != 0;
            this.mBaseDisplayInfo.displayCutout = maskCutout ? null : deviceInfo.displayCutout;
            this.mBaseDisplayInfo.displayId = this.mDisplayId;
            this.mBaseDisplayInfo.displayGroupId = this.mDisplayGroupId;
            updateFrameRateOverrides(deviceInfo);
            this.mBaseDisplayInfo.brightnessMinimum = deviceInfo.brightnessMinimum;
            this.mBaseDisplayInfo.brightnessMaximum = deviceInfo.brightnessMaximum;
            this.mBaseDisplayInfo.brightnessDefault = deviceInfo.brightnessDefault;
            this.mBaseDisplayInfo.hdrSdrRatio = deviceInfo.hdrSdrRatio;
            this.mBaseDisplayInfo.backlightType = deviceInfo.backlightType;
            this.mBaseDisplayInfo.roundedCorners = deviceInfo.roundedCorners;
            this.mBaseDisplayInfo.installOrientation = deviceInfo.installOrientation;
            this.mBaseDisplayInfo.displayShape = deviceInfo.displayShape;
            if (this.mDevicePosition == 1) {
                this.mBaseDisplayInfo.flags |= 8192;
                this.mBaseDisplayInfo.flags |= 8;
                this.mBaseDisplayInfo.removeMode = 1;
            }
            this.mBaseDisplayInfo.layoutLimitedRefreshRate = this.mLayoutLimitedRefreshRate;
            this.mBaseDisplayInfo.thermalRefreshRateThrottling = this.mThermalRefreshRateThrottling;
            this.mBaseDisplayInfo.thermalBrightnessThrottlingDataId = this.mThermalBrightnessThrottlingDataId;
            this.mPrimaryDisplayDeviceInfo = deviceInfo;
            this.mInfo.set(null);
            this.mDirty = false;
        }
    }

    private void updateFrameRateOverrides(com.android.server.display.DisplayDeviceInfo deviceInfo) {
        this.mTempFrameRateOverride.clear();
        if (this.mFrameRateOverrides != null) {
            for (android.view.DisplayEventReceiver.FrameRateOverride frameRateOverride : this.mFrameRateOverrides) {
                this.mTempFrameRateOverride.put(frameRateOverride.uid, java.lang.Float.valueOf(frameRateOverride.frameRateHz));
            }
        }
        this.mFrameRateOverrides = deviceInfo.frameRateOverrides;
        if (this.mFrameRateOverrides != null) {
            for (android.view.DisplayEventReceiver.FrameRateOverride frameRateOverride2 : this.mFrameRateOverrides) {
                float refreshRate = this.mTempFrameRateOverride.get(frameRateOverride2.uid, java.lang.Float.valueOf(0.0f)).floatValue();
                if (refreshRate == 0.0f || frameRateOverride2.frameRateHz != refreshRate) {
                    this.mTempFrameRateOverride.put(frameRateOverride2.uid, java.lang.Float.valueOf(frameRateOverride2.frameRateHz));
                } else {
                    this.mTempFrameRateOverride.delete(frameRateOverride2.uid);
                }
            }
        }
        for (int i = 0; i < this.mTempFrameRateOverride.size(); i++) {
            this.mPendingFrameRateOverrideUids.add(java.lang.Integer.valueOf(this.mTempFrameRateOverride.keyAt(i)));
        }
    }

    public android.graphics.Rect getInsets() {
        return getMaskingInsets(this.mPrimaryDisplayDeviceInfo);
    }

    private static android.graphics.Rect getMaskingInsets(com.android.server.display.DisplayDeviceInfo deviceInfo) {
        boolean maskCutout = (deviceInfo.flags & 2048) != 0;
        if (maskCutout && deviceInfo.displayCutout != null) {
            return deviceInfo.displayCutout.getSafeInsets();
        }
        return new android.graphics.Rect();
    }

    android.graphics.Point getDisplayPosition() {
        return new android.graphics.Point(this.mDisplayPosition);
    }

    public void configureDisplayLocked(android.view.SurfaceControl.Transaction t, com.android.server.display.DisplayDevice device, boolean isBlanked) {
        int i;
        int physHeight;
        int displayRectWidth;
        int displayRectHeight;
        device.setLayerStackLocked(t, isBlanked ? -1 : this.mLayerStack, this.mDisplayId);
        if (isEnabledLocked() && device.getDisplayDeviceInfoLocked().touch != 0) {
            i = 1;
        } else {
            i = 0;
        }
        device.setDisplayFlagsLocked(t, i);
        if (device == this.mPrimaryDisplayDevice) {
            device.setDesiredDisplayModeSpecsLocked(this.mDesiredDisplayModeSpecs);
            device.setRequestedColorModeLocked(this.mRequestedColorMode);
        } else {
            device.setDesiredDisplayModeSpecsLocked(new com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs());
            device.setRequestedColorModeLocked(0);
        }
        device.setAutoLowLatencyModeLocked(this.mRequestedMinimalPostProcessing);
        device.setGameContentTypeLocked(this.mRequestedMinimalPostProcessing);
        android.view.DisplayInfo displayInfo = getDisplayInfoLocked();
        com.android.server.display.DisplayDeviceInfo displayDeviceInfo = device.getDisplayDeviceInfoLocked();
        this.mTempLayerStackRect.set(0, 0, displayInfo.logicalWidth, displayInfo.logicalHeight);
        int orientation = 0;
        if ((displayDeviceInfo.flags & 2) != 0 || this.mLogicalDisplayExt.isAlwaysRotateDisplayDeviceEnabled(this.mAlwaysRotateDisplayDeviceEnabled, device, displayInfo)) {
            orientation = displayInfo.rotation;
            if (this.mLogicalDisplayExt.isMirageMirrorMode(this.mDisplayId)) {
                orientation = 0;
            }
        }
        int orientation2 = (displayDeviceInfo.rotation + orientation) % 4;
        boolean rotated = this.mLogicalDisplayExt.adjustRotatedForMirage(orientation2 == 1 || orientation2 == 3, this.mDisplayId, displayInfo);
        int physWidth = rotated ? displayDeviceInfo.height : displayDeviceInfo.width;
        int physHeight2 = rotated ? displayDeviceInfo.width : displayDeviceInfo.height;
        android.graphics.Rect maskingInsets = getMaskingInsets(displayDeviceInfo);
        com.android.server.wm.utils.InsetUtils.rotateInsets(maskingInsets, orientation2);
        int physWidth2 = physWidth - (maskingInsets.left + maskingInsets.right);
        int physHeight3 = physHeight2 - (maskingInsets.top + maskingInsets.bottom);
        int displayLogicalWidth = displayInfo.logicalWidth;
        int displayLogicalHeight = displayInfo.logicalHeight;
        if (!this.mIsAnisotropyCorrectionEnabled || displayDeviceInfo.type != 2 || displayDeviceInfo.xDpi <= 0.0f || displayDeviceInfo.yDpi <= 0.0f) {
            physHeight = physHeight3;
        } else if (displayDeviceInfo.xDpi > displayDeviceInfo.yDpi * 1.025f) {
            float scalingFactor = displayDeviceInfo.yDpi / displayDeviceInfo.xDpi;
            if (rotated) {
                physHeight = physHeight3;
                displayLogicalWidth = (int) (((double) (displayLogicalWidth * scalingFactor)) + 0.5d);
            } else {
                physHeight = physHeight3;
                displayLogicalHeight = (int) (((double) (displayLogicalHeight * scalingFactor)) + 0.5d);
            }
        } else {
            physHeight = physHeight3;
            if (displayDeviceInfo.xDpi * 1.025f < displayDeviceInfo.yDpi) {
                float scalingFactor2 = displayDeviceInfo.xDpi / displayDeviceInfo.yDpi;
                if (rotated) {
                    displayLogicalHeight = (int) (((double) (displayLogicalHeight * scalingFactor2)) + 0.5d);
                } else {
                    displayLogicalWidth = (int) (((double) (displayLogicalWidth * scalingFactor2)) + 0.5d);
                }
            }
        }
        if ((displayInfo.flags & 1073741824) != 0 || this.mDisplayScalingDisabled) {
            displayRectWidth = displayLogicalWidth;
            displayRectHeight = displayLogicalHeight;
        } else if (physWidth2 * displayLogicalHeight < physHeight * displayLogicalWidth) {
            displayRectWidth = physWidth2;
            displayRectHeight = (displayLogicalHeight * physWidth2) / displayLogicalWidth;
        } else {
            int displayRectHeight2 = displayLogicalWidth * physHeight;
            displayRectWidth = displayRectHeight2 / displayLogicalHeight;
            displayRectHeight = physHeight;
        }
        int displayRectTop = (physHeight - displayRectHeight) / 2;
        int displayRectLeft = (physWidth2 - displayRectWidth) / 2;
        this.mTempDisplayRect.set(displayRectLeft, displayRectTop, displayRectLeft + displayRectWidth, displayRectTop + displayRectHeight);
        this.mTempDisplayRect.offset(maskingInsets.left, maskingInsets.top);
        if (orientation2 == 0) {
            this.mTempDisplayRect.offset(this.mDisplayOffsetX, this.mDisplayOffsetY);
        } else if (orientation2 == 1) {
            this.mTempDisplayRect.offset(this.mDisplayOffsetY, -this.mDisplayOffsetX);
        } else if (orientation2 == 2) {
            this.mTempDisplayRect.offset(-this.mDisplayOffsetX, -this.mDisplayOffsetY);
        } else {
            this.mTempDisplayRect.offset(-this.mDisplayOffsetY, this.mDisplayOffsetX);
        }
        this.mDisplayPosition.set(this.mTempDisplayRect.left, this.mTempDisplayRect.top);
        device.setProjectionLocked(t, orientation2, this.mTempLayerStackRect, this.mTempDisplayRect);
    }

    public boolean hasContentLocked() {
        return this.mHasContent;
    }

    public void setHasContentLocked(boolean hasContent) {
        this.mHasContent = hasContent;
    }

    public void setDesiredDisplayModeSpecsLocked(com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs specs) {
        this.mDesiredDisplayModeSpecs = specs;
    }

    public com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs getDesiredDisplayModeSpecsLocked() {
        return this.mDesiredDisplayModeSpecs;
    }

    public void setRequestedColorModeLocked(int colorMode) {
        this.mRequestedColorMode = colorMode;
    }

    public boolean getRequestedMinimalPostProcessingLocked() {
        return this.mRequestedMinimalPostProcessing;
    }

    public void setRequestedMinimalPostProcessingLocked(boolean on) {
        this.mRequestedMinimalPostProcessing = on;
    }

    public int getRequestedColorModeLocked() {
        return this.mRequestedColorMode;
    }

    public int getDisplayOffsetXLocked() {
        return this.mDisplayOffsetX;
    }

    public int getDisplayOffsetYLocked() {
        return this.mDisplayOffsetY;
    }

    public void setDisplayOffsetsLocked(int x, int y) {
        this.mDisplayOffsetX = x;
        this.mDisplayOffsetY = y;
    }

    public boolean isDisplayScalingDisabled() {
        return this.mDisplayScalingDisabled;
    }

    public void setDisplayScalingDisabledLocked(boolean disableScaling) {
        this.mDisplayScalingDisabled = disableScaling;
    }

    public void setUserDisabledHdrTypes(int[] userDisabledHdrTypes) {
        if (this.mUserDisabledHdrTypes != userDisabledHdrTypes) {
            this.mUserDisabledHdrTypes = userDisabledHdrTypes;
            this.mBaseDisplayInfo.userDisabledHdrTypes = userDisabledHdrTypes;
            this.mInfo.set(null);
        }
    }

    public void swapDisplaysLocked(com.android.server.display.LogicalDisplay targetDisplay) {
        com.android.server.display.DisplayDevice oldTargetDevice = targetDisplay.setPrimaryDisplayDeviceLocked(this.mPrimaryDisplayDevice);
        setPrimaryDisplayDeviceLocked(oldTargetDevice);
    }

    public com.android.server.display.DisplayDevice setPrimaryDisplayDeviceLocked(com.android.server.display.DisplayDevice device) {
        com.android.server.display.DisplayDevice old = this.mPrimaryDisplayDevice;
        this.mPrimaryDisplayDevice = device;
        this.mPrimaryDisplayDeviceInfo = null;
        this.mBaseDisplayInfo.copyFrom(EMPTY_DISPLAY_INFO);
        this.mOverrideDisplayInfo = null;
        this.mInfo.set(null);
        android.util.Slog.w(TAG, "setPrimaryDisplayDeviceLocked device changed:" + old + "->" + device);
        return old;
    }

    public boolean isEnabledLocked() {
        return this.mIsEnabled;
    }

    public void setEnabledLocked(boolean enabled) {
        java.lang.Runnable setStateRunnable;
        if (enabled != this.mIsEnabled) {
            this.mDirty = true;
            this.mIsEnabled = enabled;
        }
        if (!enabled && this.mPrimaryDisplayDevice != null && (setStateRunnable = this.mPrimaryDisplayDevice.requestDisplayStateLocked(1, -1.0f, -1.0f, null)) != null) {
            setStateRunnable.run();
        }
    }

    public boolean isInTransitionLocked() {
        return this.mIsInTransition;
    }

    public void setIsInTransitionLocked(boolean isInTransition) {
        this.mIsInTransition = isInTransition;
    }

    public void setThermalBrightnessThrottlingDataIdLocked(java.lang.String brightnessThrottlingDataId) {
        if (!java.util.Objects.equals(brightnessThrottlingDataId, this.mThermalBrightnessThrottlingDataId)) {
            this.mThermalBrightnessThrottlingDataId = brightnessThrottlingDataId;
            this.mDirty = true;
        }
    }

    public void setPowerThrottlingDataIdLocked(java.lang.String powerThrottlingDataId) {
        if (!java.util.Objects.equals(powerThrottlingDataId, this.mPowerThrottlingDataId)) {
            this.mPowerThrottlingDataId = powerThrottlingDataId;
            this.mDirty = true;
        }
    }

    public java.lang.String getPowerThrottlingDataIdLocked() {
        return this.mPowerThrottlingDataId;
    }

    public void setLeadDisplayLocked(int displayId) {
        if (this.mDisplayId != this.mLeadDisplayId && this.mDisplayId != displayId) {
            this.mLeadDisplayId = displayId;
        }
    }

    public int getLeadDisplayIdLocked() {
        return this.mLeadDisplayId;
    }

    public void setDisplayGroupNameLocked(java.lang.String displayGroupName) {
        this.mDisplayGroupName = displayGroupName;
    }

    public java.lang.String getDisplayGroupNameLocked() {
        return this.mDisplayGroupName;
    }

    public void setDisplayOffloadSessionLocked(com.android.server.display.DisplayOffloadSessionImpl session) {
        this.mDisplayOffloadSession = session;
    }

    public com.android.server.display.DisplayOffloadSessionImpl getDisplayOffloadSessionLocked() {
        return this.mDisplayOffloadSession;
    }

    public void dumpLocked(java.io.PrintWriter pw) {
        pw.println("mDisplayId=" + this.mDisplayId);
        pw.println("mIsEnabled=" + this.mIsEnabled);
        pw.println("mIsInTransition=" + this.mIsInTransition);
        pw.println("mLayerStack=" + this.mLayerStack);
        pw.println("mPosition=" + this.mDevicePosition);
        pw.println("mHasContent=" + this.mHasContent);
        pw.println("mDesiredDisplayModeSpecs={" + this.mDesiredDisplayModeSpecs + "}");
        pw.println("mRequestedColorMode=" + this.mRequestedColorMode);
        pw.println("mDisplayOffset=(" + this.mDisplayOffsetX + ", " + this.mDisplayOffsetY + ")");
        pw.println("mDisplayScalingDisabled=" + this.mDisplayScalingDisabled);
        pw.println("mPrimaryDisplayDevice=" + (this.mPrimaryDisplayDevice != null ? this.mPrimaryDisplayDevice.getNameLocked() : "null"));
        pw.println("mBaseDisplayInfo=" + this.mBaseDisplayInfo);
        pw.println("mOverrideDisplayInfo=" + this.mOverrideDisplayInfo);
        pw.println("mRequestedMinimalPostProcessing=" + this.mRequestedMinimalPostProcessing);
        pw.println("mFrameRateOverrides=" + java.util.Arrays.toString(this.mFrameRateOverrides));
        pw.println("mPendingFrameRateOverrideUids=" + this.mPendingFrameRateOverrideUids);
        pw.println("mDisplayGroupName=" + this.mDisplayGroupName);
        pw.println("mThermalBrightnessThrottlingDataId=" + this.mThermalBrightnessThrottlingDataId);
        pw.println("mLeadDisplayId=" + this.mLeadDisplayId);
        pw.println("mLayoutLimitedRefreshRate=" + this.mLayoutLimitedRefreshRate);
        pw.println("mThermalRefreshRateThrottling=" + this.mThermalRefreshRateThrottling);
        pw.println("mPowerThrottlingDataId=" + this.mPowerThrottlingDataId);
    }

    public java.lang.String toStringMini() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("id=").append(this.mDisplayId).append(",stack=");
        sb.append(this.mLayerStack).append(",content=").append(this.mHasContent).append(",displayInfo{stack=");
        sb.append(this.mBaseDisplayInfo.layerStack).append(",type=").append(this.mBaseDisplayInfo.type).append(",id=");
        sb.append(this.mBaseDisplayInfo.displayId).append(",uId=").append(this.mBaseDisplayInfo.uniqueId).append(",state=");
        sb.append(android.view.Display.stateToString(this.mBaseDisplayInfo.state)).append(",pkg=");
        sb.append(this.mBaseDisplayInfo.ownerPackageName).append("}");
        return sb.toString();
    }

    public java.lang.String toString() {
        java.io.StringWriter sw = new java.io.StringWriter();
        dumpLocked(new java.io.PrintWriter(sw));
        return sw.toString();
    }
}
