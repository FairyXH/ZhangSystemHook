package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
abstract class DisplayDevice {
    private static final android.view.Display.Mode EMPTY_DISPLAY_MODE = new android.view.Display.Mode.Builder().build();
    static final float MAX_ANISOTROPY = 1.025f;
    private static final java.lang.String TAG = "DisplayDevice";
    private final android.content.Context mContext;
    private android.graphics.Rect mCurrentDisplayRect;
    private int mCurrentFlags;
    private int mCurrentLayerStack;
    private android.graphics.Rect mCurrentLayerStackRect;
    private int mCurrentOrientation;
    private android.view.Surface mCurrentSurface;
    com.android.server.display.DisplayDeviceInfo mDebugLastLoggedDeviceInfo;
    private final com.android.server.display.DisplayAdapter mDisplayAdapter;
    protected com.android.server.display.DisplayDeviceConfig mDisplayDeviceConfig;
    public com.android.server.display.IDisplayDeviceExt mDisplayDeviceExt;
    private final android.os.IBinder mDisplayToken;
    private final boolean mIsAnisotropyCorrectionEnabled;
    private final java.lang.String mUniqueId;

    public abstract com.android.server.display.DisplayDeviceInfo getDisplayDeviceInfoLocked();

    public abstract boolean hasStableUniqueId();

    DisplayDevice(com.android.server.display.DisplayAdapter displayAdapter, android.os.IBinder displayToken, java.lang.String uniqueId, android.content.Context context) {
        this(displayAdapter, displayToken, uniqueId, context, false);
    }

    DisplayDevice(com.android.server.display.DisplayAdapter displayAdapter, android.os.IBinder displayToken, java.lang.String uniqueId, android.content.Context context, boolean isAnisotropyCorrectionEnabled) {
        this.mCurrentLayerStack = -2;
        this.mCurrentFlags = 0;
        this.mCurrentOrientation = -1;
        this.mDisplayDeviceExt = (com.android.server.display.IDisplayDeviceExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IDisplayDeviceExt.class).base(this).create();
        this.mDisplayAdapter = displayAdapter;
        this.mDisplayToken = displayToken;
        this.mUniqueId = uniqueId;
        this.mDisplayDeviceConfig = null;
        this.mContext = context;
        this.mIsAnisotropyCorrectionEnabled = isAnisotropyCorrectionEnabled;
    }

    public final com.android.server.display.DisplayAdapter getAdapterLocked() {
        return this.mDisplayAdapter;
    }

    public com.android.server.display.DisplayDeviceConfig getDisplayDeviceConfig() {
        if (this.mDisplayDeviceConfig == null) {
            this.mDisplayDeviceConfig = loadDisplayDeviceConfig();
        }
        return this.mDisplayDeviceConfig;
    }

    public final android.os.IBinder getDisplayTokenLocked() {
        return this.mDisplayToken;
    }

    public int getDisplayIdToMirrorLocked() {
        return 0;
    }

    public boolean isWindowManagerMirroringLocked() {
        return false;
    }

    public void setWindowManagerMirroringLocked(boolean isMirroring) {
    }

    public android.graphics.Point getDisplaySurfaceDefaultSizeLocked() {
        com.android.server.display.DisplayDeviceInfo displayDeviceInfo = getDisplayDeviceInfoLocked();
        int width = displayDeviceInfo.width;
        int height = displayDeviceInfo.height;
        if (this.mIsAnisotropyCorrectionEnabled && displayDeviceInfo.type == 2 && displayDeviceInfo.yDpi > 0.0f && displayDeviceInfo.xDpi > 0.0f) {
            if (displayDeviceInfo.xDpi > displayDeviceInfo.yDpi * MAX_ANISOTROPY) {
                height = (int) (((double) ((height * displayDeviceInfo.xDpi) / displayDeviceInfo.yDpi)) + 0.5d);
            } else if (displayDeviceInfo.xDpi * MAX_ANISOTROPY < displayDeviceInfo.yDpi) {
                width = (int) (((double) ((width * displayDeviceInfo.yDpi) / displayDeviceInfo.xDpi)) + 0.5d);
            }
        }
        return isRotatedLocked() ? new android.graphics.Point(height, width) : new android.graphics.Point(width, height);
    }

    public final java.lang.String getNameLocked() {
        return getDisplayDeviceInfoLocked().name;
    }

    public final java.lang.String getUniqueId() {
        return this.mUniqueId;
    }

    public void applyPendingDisplayDeviceInfoChangesLocked() {
    }

    public void performTraversalLocked(android.view.SurfaceControl.Transaction t) {
    }

    public java.lang.Runnable requestDisplayStateLocked(int state, float brightnessState, float sdrBrightnessState, com.android.server.display.DisplayOffloadSessionImpl displayOffloadSession) {
        return null;
    }

    public void setDesiredDisplayModeSpecsLocked(com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs displayModeSpecs) {
    }

    public void setUserPreferredDisplayModeLocked(android.view.Display.Mode mode) {
    }

    public android.view.Display.Mode getUserPreferredDisplayModeLocked() {
        return EMPTY_DISPLAY_MODE;
    }

    public android.view.Display.Mode getSystemPreferredDisplayModeLocked() {
        return EMPTY_DISPLAY_MODE;
    }

    public android.view.Display.Mode getActiveDisplayModeAtStartLocked() {
        return EMPTY_DISPLAY_MODE;
    }

    public void setRequestedColorModeLocked(int colorMode) {
    }

    public void setAutoLowLatencyModeLocked(boolean on) {
    }

    public void setGameContentTypeLocked(boolean on) {
    }

    public void onOverlayChangedLocked() {
    }

    public final void setLayerStackLocked(android.view.SurfaceControl.Transaction t, int layerStack, int layerStackTag) {
        android.util.Slog.d(TAG, "setLayerStackLocked id=" + this.mUniqueId + " token=" + this.mDisplayToken + " stack=" + this.mCurrentLayerStack + "->" + layerStack);
        if (this.mCurrentLayerStack != layerStack) {
            this.mCurrentLayerStack = layerStack;
            this.mDisplayDeviceExt.setLayerStack(layerStack);
            t.setDisplayLayerStack(this.mDisplayToken, layerStack);
            android.util.Slog.i(TAG, "[" + layerStackTag + "] Layerstack set to " + layerStack + " for " + this.mUniqueId);
        }
    }

    public final void setDisplayFlagsLocked(android.view.SurfaceControl.Transaction t, int flags) {
        if (this.mCurrentFlags != flags) {
            this.mCurrentFlags = flags;
            t.setDisplayFlags(this.mDisplayToken, flags);
        }
    }

    public void setProjectionLocked(android.view.SurfaceControl.Transaction t, int orientation, android.graphics.Rect layerStackRect, android.graphics.Rect displayRect) {
        this.mDisplayDeviceExt.setProjectionLocked(displayRect, this);
        if (this.mCurrentOrientation != orientation || this.mCurrentLayerStackRect == null || !this.mCurrentLayerStackRect.equals(layerStackRect) || this.mCurrentDisplayRect == null || !this.mCurrentDisplayRect.equals(displayRect)) {
            this.mCurrentOrientation = orientation;
            if (this.mCurrentLayerStackRect == null) {
                this.mCurrentLayerStackRect = new android.graphics.Rect();
            }
            this.mCurrentLayerStackRect.set(layerStackRect);
            if (this.mCurrentDisplayRect == null) {
                this.mCurrentDisplayRect = new android.graphics.Rect();
            }
            this.mCurrentDisplayRect.set(displayRect);
            t.setDisplayProjection(this.mDisplayToken, orientation, layerStackRect, displayRect);
        }
    }

    public final void setSurfaceLocked(android.view.SurfaceControl.Transaction t, android.view.Surface surface) {
        this.mDisplayDeviceExt.cacheSurfaceForDisplay(this, surface);
        if (!this.mDisplayDeviceExt.shouldSetDisplayDeviceSurface(this)) {
            android.util.Slog.d(TAG, "Skip setSurface for privacy.");
        } else if (this.mCurrentSurface != surface) {
            this.mCurrentSurface = surface;
            t.setDisplaySurface(this.mDisplayToken, surface);
        }
    }

    public final void populateViewportLocked(android.hardware.display.DisplayViewport viewport) {
        viewport.orientation = this.mCurrentOrientation;
        if (this.mCurrentLayerStackRect != null) {
            viewport.logicalFrame.set(this.mCurrentLayerStackRect);
        } else {
            viewport.logicalFrame.setEmpty();
        }
        if (this.mCurrentDisplayRect != null) {
            viewport.physicalFrame.set(this.mCurrentDisplayRect);
        } else {
            viewport.physicalFrame.setEmpty();
        }
        boolean isRotated = isRotatedLocked();
        com.android.server.display.DisplayDeviceInfo info = getDisplayDeviceInfoLocked();
        viewport.deviceWidth = isRotated ? info.height : info.width;
        viewport.deviceHeight = isRotated ? info.width : info.height;
        viewport.uniqueId = info.uniqueId;
        if (info.address instanceof android.view.DisplayAddress.Physical) {
            viewport.physicalPort = java.lang.Integer.valueOf(info.address.getPort());
        } else {
            viewport.physicalPort = null;
        }
    }

    public void dumpLocked(java.io.PrintWriter pw) {
        pw.println("mAdapter=" + this.mDisplayAdapter.getName());
        pw.println("mUniqueId=" + this.mUniqueId);
        pw.println("mDisplayToken=" + this.mDisplayToken);
        pw.println("mCurrentLayerStack=" + this.mCurrentLayerStack);
        pw.println("mCurrentFlags=" + this.mCurrentFlags);
        pw.println("mCurrentOrientation=" + this.mCurrentOrientation);
        pw.println("mCurrentLayerStackRect=" + this.mCurrentLayerStackRect);
        pw.println("mCurrentDisplayRect=" + this.mCurrentDisplayRect);
        pw.println("mCurrentSurface=" + this.mCurrentSurface);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[");
        sb.append("id=").append(this.mUniqueId).append(",stack=").append(this.mCurrentLayerStack);
        sb.append("]");
        return sb.toString();
    }

    public com.android.server.display.IDisplayDeviceExt getExtImpl() {
        return this.mDisplayDeviceExt;
    }

    boolean isRotatedLocked() {
        return this.mCurrentOrientation == 1 || this.mCurrentOrientation == 3;
    }

    android.graphics.Point[] getSupportedResolutionsLocked() {
        android.util.ArraySet<android.graphics.Point> resolutions = new android.util.ArraySet<>(2);
        android.view.Display.Mode[] supportedModes = getDisplayDeviceInfoLocked().supportedModes;
        for (android.view.Display.Mode mode : supportedModes) {
            resolutions.add(new android.graphics.Point(mode.getPhysicalWidth(), mode.getPhysicalHeight()));
        }
        android.graphics.Point[] sortedArray = new android.graphics.Point[resolutions.size()];
        resolutions.toArray(sortedArray);
        java.util.Arrays.sort(sortedArray, new java.util.Comparator() { // from class: com.android.server.display.DisplayDevice$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.display.DisplayDevice.lambda$getSupportedResolutionsLocked$0((android.graphics.Point) obj, (android.graphics.Point) obj2);
            }
        });
        return sortedArray;
    }

    static /* synthetic */ int lambda$getSupportedResolutionsLocked$0(android.graphics.Point p1, android.graphics.Point p2) {
        return (p1.x * p1.y) - (p2.x * p2.y);
    }

    private com.android.server.display.DisplayDeviceConfig loadDisplayDeviceConfig() {
        return com.android.server.display.DisplayDeviceConfig.create(this.mContext, false, this.mDisplayAdapter.getFeatureFlags());
    }
}
