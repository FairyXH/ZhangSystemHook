package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class OverlayDisplayAdapter extends com.android.server.display.DisplayAdapter {
    static final boolean DEBUG = false;
    private static final java.lang.String DISPLAY_SPLITTER = ";";
    private static final java.lang.String FLAG_SPLITTER = ",";
    private static final int MAX_HEIGHT = 4096;
    private static final int MAX_WIDTH = 4096;
    private static final int MIN_HEIGHT = 100;
    private static final int MIN_WIDTH = 100;
    private static final java.lang.String MODE_SPLITTER = "\\|";
    private static final java.lang.String OVERLAY_DISPLAY_FLAG_OWN_CONTENT_ONLY = "own_content_only";
    private static final java.lang.String OVERLAY_DISPLAY_FLAG_SECURE = "secure";
    private static final java.lang.String OVERLAY_DISPLAY_FLAG_SHOULD_SHOW_SYSTEM_DECORATIONS = "should_show_system_decorations";
    static final java.lang.String TAG = "OverlayDisplayAdapter";
    private static final java.lang.String UNIQUE_ID_PREFIX = "overlay:";
    private java.lang.String mCurrentOverlaySetting;
    private final java.util.ArrayList<com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle> mOverlays;
    private final android.os.Handler mUiHandler;
    private static final java.util.regex.Pattern DISPLAY_PATTERN = java.util.regex.Pattern.compile("([^,]+)(,[,_a-z]+)*");
    private static final java.util.regex.Pattern MODE_PATTERN = java.util.regex.Pattern.compile("(\\d+)x(\\d+)/(\\d+)");

    public OverlayDisplayAdapter(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.content.Context context, android.os.Handler handler, com.android.server.display.DisplayAdapter.Listener listener, android.os.Handler uiHandler, com.android.server.display.feature.DisplayManagerFlags featureFlags) {
        super(syncRoot, context, handler, listener, TAG, featureFlags);
        this.mOverlays = new java.util.ArrayList<>();
        this.mCurrentOverlaySetting = "";
        this.mUiHandler = uiHandler;
        ((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).init(this, syncRoot, handler, context, uiHandler);
    }

    @Override // com.android.server.display.DisplayAdapter
    public void dumpLocked(java.io.PrintWriter pw) {
        super.dumpLocked(pw);
        pw.println("mCurrentOverlaySetting=" + this.mCurrentOverlaySetting);
        pw.println("mOverlays: size=" + this.mOverlays.size());
        for (com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle overlay : this.mOverlays) {
            overlay.dumpLocked(pw);
        }
    }

    @Override // com.android.server.display.DisplayAdapter
    public void registerLocked() {
        super.registerLocked();
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.OverlayDisplayAdapter.1
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.OverlayDisplayAdapter.this.getContext().getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("overlay_display_devices"), true, new android.database.ContentObserver(com.android.server.display.OverlayDisplayAdapter.this.getHandler()) { // from class: com.android.server.display.OverlayDisplayAdapter.1.1
                    @Override // android.database.ContentObserver
                    public void onChange(boolean selfChange) {
                        com.android.server.display.OverlayDisplayAdapter.this.updateOverlayDisplayDevices();
                    }
                });
                com.android.server.display.OverlayDisplayAdapter.this.updateOverlayDisplayDevices();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOverlayDisplayDevices() {
        synchronized (getSyncRoot()) {
            updateOverlayDisplayDevicesLocked();
        }
    }

    private void updateOverlayDisplayDevicesLocked() {
        java.lang.String value;
        java.lang.String modeString;
        java.util.regex.Matcher displayMatcher;
        com.android.server.display.OverlayDisplayAdapter overlayDisplayAdapter = this;
        java.lang.String value2 = android.provider.Settings.Global.getString(getContext().getContentResolver(), "overlay_display_devices");
        if (value2 != null) {
            value = value2;
        } else {
            value = "";
        }
        if (value.equals(overlayDisplayAdapter.mCurrentOverlaySetting)) {
            return;
        }
        overlayDisplayAdapter.mCurrentOverlaySetting = value;
        if (!overlayDisplayAdapter.mOverlays.isEmpty()) {
            android.util.Slog.i(TAG, "Dismissing all overlay display devices.");
            for (com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle overlay : overlayDisplayAdapter.mOverlays) {
                overlay.dismissLocked();
            }
            overlayDisplayAdapter.mOverlays.clear();
        }
        java.lang.String[] strArrSplit = value.split(DISPLAY_SPLITTER);
        int length = strArrSplit.length;
        int count = 0;
        int i = 0;
        while (i < length) {
            java.lang.String part = strArrSplit[i];
            java.util.regex.Matcher displayMatcher2 = DISPLAY_PATTERN.matcher(part);
            if (displayMatcher2.matches()) {
                if (count >= 4) {
                    android.util.Slog.w(TAG, "Too many overlay display devices specified: " + value);
                    return;
                }
                java.lang.String modeString2 = displayMatcher2.group(1);
                java.lang.String flagString = displayMatcher2.group(2);
                java.util.ArrayList<com.android.server.display.OverlayDisplayAdapter.OverlayMode> modes = new java.util.ArrayList<>();
                java.lang.String[] strArrSplit2 = modeString2.split(MODE_SPLITTER);
                int length2 = strArrSplit2.length;
                int i2 = 0;
                while (i2 < length2) {
                    java.lang.String mode = strArrSplit2[i2];
                    java.lang.String[] strArr = strArrSplit2;
                    java.util.regex.Matcher modeMatcher = MODE_PATTERN.matcher(mode);
                    if (modeMatcher.matches()) {
                        modeString = modeString2;
                        try {
                            int width = java.lang.Integer.parseInt(modeMatcher.group(1), 10);
                            displayMatcher = displayMatcher2;
                            try {
                                int height = java.lang.Integer.parseInt(modeMatcher.group(2), 10);
                                try {
                                    int densityDpi = java.lang.Integer.parseInt(modeMatcher.group(3), 10);
                                    if (width >= 100 && width <= 4096 && height >= 100 && height <= 4096 && densityDpi >= 120 && densityDpi <= 640) {
                                        modes.add(new com.android.server.display.OverlayDisplayAdapter.OverlayMode(width, height, densityDpi));
                                    } else {
                                        android.util.Slog.w(TAG, "Ignoring out-of-range overlay display mode: " + mode);
                                    }
                                } catch (java.lang.NumberFormatException e) {
                                }
                            } catch (java.lang.NumberFormatException e2) {
                            }
                        } catch (java.lang.NumberFormatException e3) {
                            displayMatcher = displayMatcher2;
                        }
                    } else {
                        modeString = modeString2;
                        displayMatcher = displayMatcher2;
                        mode.isEmpty();
                    }
                    i2++;
                    displayMatcher2 = displayMatcher;
                    strArrSplit2 = strArr;
                    modeString2 = modeString;
                }
                if (!modes.isEmpty()) {
                    int count2 = count + 1;
                    java.lang.String name = getContext().getResources().getString(android.R.string.dump_heap_system_text, java.lang.Integer.valueOf(count2));
                    int gravity = chooseOverlayGravity(count2);
                    com.android.server.display.OverlayDisplayAdapter.OverlayFlags flags = com.android.server.display.OverlayDisplayAdapter.OverlayFlags.parseFlags(flagString);
                    android.util.Slog.i(TAG, "Showing overlay display device #" + count2 + ": name=" + name + ", modes=" + java.util.Arrays.toString(modes.toArray()) + ", flags=" + flags);
                    overlayDisplayAdapter.mOverlays.add(new com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle(name, modes, gravity, flags, count2));
                    count = count2;
                    i++;
                    overlayDisplayAdapter = this;
                }
            }
            android.util.Slog.w(TAG, "Malformed overlay display devices setting: " + value);
            i++;
            overlayDisplayAdapter = this;
        }
    }

    private static int chooseOverlayGravity(int overlayNumber) {
        switch (overlayNumber) {
            case 1:
                return 51;
            case 2:
                return 85;
            case 3:
                return 53;
            default:
                return 83;
        }
    }

    private abstract class OverlayDisplayDevice extends com.android.server.display.DisplayDevice {
        private int mActiveMode;
        private final int mDefaultMode;
        private final long mDisplayPresentationDeadlineNanos;
        private final com.android.server.display.OverlayDisplayAdapter.OverlayFlags mFlags;
        private com.android.server.display.DisplayDeviceInfo mInfo;
        private final android.view.Display.Mode[] mModes;
        private final java.lang.String mName;
        private final java.util.List<com.android.server.display.OverlayDisplayAdapter.OverlayMode> mRawModes;
        private final float mRefreshRate;
        private int mState;
        private android.view.Surface mSurface;
        private android.graphics.SurfaceTexture mSurfaceTexture;

        public abstract void onModeChangedLocked(int i);

        OverlayDisplayDevice(android.os.IBinder displayToken, java.lang.String name, java.util.List<com.android.server.display.OverlayDisplayAdapter.OverlayMode> modes, int activeMode, int defaultMode, float refreshRate, long presentationDeadlineNanos, com.android.server.display.OverlayDisplayAdapter.OverlayFlags flags, int state, android.graphics.SurfaceTexture surfaceTexture, int number) {
            super(com.android.server.display.OverlayDisplayAdapter.this, displayToken, com.android.server.display.OverlayDisplayAdapter.UNIQUE_ID_PREFIX + number, com.android.server.display.OverlayDisplayAdapter.this.getContext());
            this.mName = name;
            this.mRefreshRate = refreshRate;
            this.mDisplayPresentationDeadlineNanos = presentationDeadlineNanos;
            this.mFlags = flags;
            this.mState = state;
            this.mSurfaceTexture = surfaceTexture;
            this.mRawModes = modes;
            this.mModes = new android.view.Display.Mode[modes.size()];
            for (int i = 0; i < modes.size(); i++) {
                com.android.server.display.OverlayDisplayAdapter.OverlayMode mode = modes.get(i);
                this.mModes[i] = com.android.server.display.DisplayAdapter.createMode(mode.mWidth, mode.mHeight, refreshRate);
            }
            this.mActiveMode = activeMode;
            this.mDefaultMode = defaultMode;
        }

        public void destroyLocked() {
            this.mSurfaceTexture = null;
            if (this.mSurface != null) {
                this.mSurface.release();
                this.mSurface = null;
            }
            com.android.server.display.DisplayControl.destroyVirtualDisplay(getDisplayTokenLocked());
        }

        @Override // com.android.server.display.DisplayDevice
        public boolean hasStableUniqueId() {
            return false;
        }

        @Override // com.android.server.display.DisplayDevice
        public void performTraversalLocked(android.view.SurfaceControl.Transaction t) {
            if (this.mSurfaceTexture != null) {
                if (this.mSurface == null) {
                    this.mSurface = new android.view.Surface(this.mSurfaceTexture);
                }
                setSurfaceLocked(t, this.mSurface);
            }
        }

        public void setStateLocked(int state) {
            this.mState = state;
            this.mInfo = null;
        }

        @Override // com.android.server.display.DisplayDevice
        public com.android.server.display.DisplayDeviceInfo getDisplayDeviceInfoLocked() {
            if (this.mInfo == null) {
                android.view.Display.Mode mode = this.mModes[this.mActiveMode];
                com.android.server.display.OverlayDisplayAdapter.OverlayMode rawMode = this.mRawModes.get(this.mActiveMode);
                this.mInfo = new com.android.server.display.DisplayDeviceInfo();
                this.mInfo.name = this.mName;
                this.mInfo.uniqueId = getUniqueId();
                this.mInfo.width = mode.getPhysicalWidth();
                this.mInfo.height = mode.getPhysicalHeight();
                this.mInfo.modeId = mode.getModeId();
                this.mInfo.renderFrameRate = mode.getRefreshRate();
                this.mInfo.defaultModeId = this.mModes[0].getModeId();
                this.mInfo.supportedModes = this.mModes;
                this.mInfo.densityDpi = rawMode.mDensityDpi;
                this.mInfo.xDpi = rawMode.mDensityDpi;
                this.mInfo.yDpi = rawMode.mDensityDpi;
                this.mInfo.presentationDeadlineNanos = this.mDisplayPresentationDeadlineNanos + (1000000000 / ((long) ((int) this.mRefreshRate)));
                this.mInfo.flags = 64;
                if (this.mFlags.mSecure) {
                    this.mInfo.flags |= 4;
                }
                if (this.mFlags.mOwnContentOnly) {
                    this.mInfo.flags |= 128;
                }
                if (this.mFlags.mShouldShowSystemDecorations) {
                    this.mInfo.flags |= 4096;
                }
                this.mInfo.type = 4;
                this.mInfo.touch = 3;
                this.mInfo.state = this.mState;
                this.mInfo.flags |= 8192;
                this.mInfo.displayShape = android.view.DisplayShape.createDefaultDisplayShape(this.mInfo.width, this.mInfo.height, false);
            }
            return this.mInfo;
        }

        @Override // com.android.server.display.DisplayDevice
        public void setDesiredDisplayModeSpecsLocked(com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs displayModeSpecs) {
            int id = displayModeSpecs.baseModeId;
            int index = -1;
            if (id == 0) {
                index = 0;
            } else {
                int i = 0;
                while (true) {
                    if (i >= this.mModes.length) {
                        break;
                    }
                    if (this.mModes[i].getModeId() != id) {
                        i++;
                    } else {
                        index = i;
                        break;
                    }
                }
            }
            if (index == -1) {
                android.util.Slog.w(com.android.server.display.OverlayDisplayAdapter.TAG, "Unable to locate mode " + id + ", reverting to default.");
                index = this.mDefaultMode;
            }
            if (this.mActiveMode == index) {
                return;
            }
            this.mActiveMode = index;
            this.mInfo = null;
            com.android.server.display.OverlayDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2);
            onModeChangedLocked(index);
        }
    }

    private final class OverlayDisplayHandle implements com.android.server.display.OverlayDisplayWindow.Listener {
        private static final int DEFAULT_MODE_INDEX = 0;
        private com.android.server.display.OverlayDisplayAdapter.OverlayDisplayDevice mDevice;
        private final com.android.server.display.OverlayDisplayAdapter.OverlayFlags mFlags;
        private final int mGravity;
        private final java.util.List<com.android.server.display.OverlayDisplayAdapter.OverlayMode> mModes;
        private final java.lang.String mName;
        private final int mNumber;
        private com.android.server.display.OverlayDisplayWindow mWindow;
        private final java.lang.Runnable mShowRunnable = new java.lang.Runnable() { // from class: com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.2
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.OverlayDisplayAdapter.OverlayMode mode = (com.android.server.display.OverlayDisplayAdapter.OverlayMode) com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mModes.get(com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mActiveMode);
                com.android.server.display.OverlayDisplayWindow window = new com.android.server.display.OverlayDisplayWindow(com.android.server.display.OverlayDisplayAdapter.this.getContext(), com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mName, mode.mWidth, mode.mHeight, mode.mDensityDpi, com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mGravity, com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mFlags.mSecure, com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this);
                window.show();
                synchronized (com.android.server.display.OverlayDisplayAdapter.this.getSyncRoot()) {
                    com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mWindow = window;
                }
            }
        };
        private final java.lang.Runnable mDismissRunnable = new java.lang.Runnable() { // from class: com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.3
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.OverlayDisplayWindow window;
                synchronized (com.android.server.display.OverlayDisplayAdapter.this.getSyncRoot()) {
                    window = com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mWindow;
                    com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mWindow = null;
                }
                if (window != null) {
                    window.dismiss();
                }
            }
        };
        private final java.lang.Runnable mResizeRunnable = new java.lang.Runnable() { // from class: com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.4
            @Override // java.lang.Runnable
            public void run() {
                synchronized (com.android.server.display.OverlayDisplayAdapter.this.getSyncRoot()) {
                    if (com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mWindow == null) {
                        return;
                    }
                    com.android.server.display.OverlayDisplayAdapter.OverlayMode mode = (com.android.server.display.OverlayDisplayAdapter.OverlayMode) com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mModes.get(com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mActiveMode);
                    com.android.server.display.OverlayDisplayWindow window = com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.mWindow;
                    window.resize(mode.mWidth, mode.mHeight, mode.mDensityDpi);
                }
            }
        };
        private int mActiveMode = 0;

        OverlayDisplayHandle(java.lang.String name, java.util.List<com.android.server.display.OverlayDisplayAdapter.OverlayMode> modes, int gravity, com.android.server.display.OverlayDisplayAdapter.OverlayFlags flags, int number) {
            this.mName = name;
            this.mModes = modes;
            this.mGravity = gravity;
            this.mFlags = flags;
            this.mNumber = number;
            showLocked();
        }

        private void showLocked() {
            com.android.server.display.OverlayDisplayAdapter.this.mUiHandler.post(this.mShowRunnable);
        }

        public void dismissLocked() {
            com.android.server.display.OverlayDisplayAdapter.this.mUiHandler.removeCallbacks(this.mShowRunnable);
            com.android.server.display.OverlayDisplayAdapter.this.mUiHandler.post(this.mDismissRunnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onActiveModeChangedLocked(int index) {
            com.android.server.display.OverlayDisplayAdapter.this.mUiHandler.removeCallbacks(this.mResizeRunnable);
            this.mActiveMode = index;
            if (this.mWindow != null) {
                com.android.server.display.OverlayDisplayAdapter.this.mUiHandler.post(this.mResizeRunnable);
            }
        }

        @Override // com.android.server.display.OverlayDisplayWindow.Listener
        public void onWindowCreated(android.graphics.SurfaceTexture surfaceTexture, float refreshRate, long presentationDeadlineNanos, int state) {
            synchronized (com.android.server.display.OverlayDisplayAdapter.this.getSyncRoot()) {
                android.os.IBinder displayToken = com.android.server.display.DisplayControl.createVirtualDisplay(this.mName, this.mFlags.mSecure);
                this.mDevice = new com.android.server.display.OverlayDisplayAdapter.OverlayDisplayDevice(displayToken, this.mName, this.mModes, this.mActiveMode, 0, refreshRate, presentationDeadlineNanos, this.mFlags, state, surfaceTexture, this.mNumber) { // from class: com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.1
                    {
                        com.android.server.display.OverlayDisplayAdapter overlayDisplayAdapter = com.android.server.display.OverlayDisplayAdapter.this;
                    }

                    @Override // com.android.server.display.OverlayDisplayAdapter.OverlayDisplayDevice
                    public void onModeChangedLocked(int index) {
                        com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.this.onActiveModeChangedLocked(index);
                    }
                };
                com.android.server.display.OverlayDisplayAdapter.this.sendDisplayDeviceEventLocked(this.mDevice, 1);
            }
        }

        @Override // com.android.server.display.OverlayDisplayWindow.Listener
        public void onWindowDestroyed() {
            synchronized (com.android.server.display.OverlayDisplayAdapter.this.getSyncRoot()) {
                if (this.mDevice != null) {
                    this.mDevice.destroyLocked();
                    com.android.server.display.OverlayDisplayAdapter.this.sendDisplayDeviceEventLocked(this.mDevice, 3);
                }
            }
        }

        @Override // com.android.server.display.OverlayDisplayWindow.Listener
        public void onStateChanged(int state) {
            synchronized (com.android.server.display.OverlayDisplayAdapter.this.getSyncRoot()) {
                if (this.mDevice != null) {
                    this.mDevice.setStateLocked(state);
                    com.android.server.display.OverlayDisplayAdapter.this.sendDisplayDeviceEventLocked(this.mDevice, 2);
                }
            }
        }

        public void dumpLocked(java.io.PrintWriter pw) {
            pw.println("  " + this.mName + ":");
            pw.println("    mModes=" + java.util.Arrays.toString(this.mModes.toArray()));
            pw.println("    mActiveMode=" + this.mActiveMode);
            pw.println("    mGravity=" + this.mGravity);
            pw.println("    mFlags=" + this.mFlags);
            pw.println("    mNumber=" + this.mNumber);
            if (this.mWindow != null) {
                com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "    ");
                ipw.increaseIndent();
                com.android.internal.util.DumpUtils.dumpAsync(com.android.server.display.OverlayDisplayAdapter.this.mUiHandler, this.mWindow, ipw, "", 200L);
            }
        }
    }

    private static final class OverlayMode {
        final int mDensityDpi;
        final int mHeight;
        final int mWidth;

        OverlayMode(int width, int height, int densityDpi) {
            this.mWidth = width;
            this.mHeight = height;
            this.mDensityDpi = densityDpi;
        }

        public java.lang.String toString() {
            return "{width=" + this.mWidth + ", height=" + this.mHeight + ", densityDpi=" + this.mDensityDpi + "}";
        }
    }

    private static final class OverlayFlags {
        final boolean mOwnContentOnly;
        final boolean mSecure;
        final boolean mShouldShowSystemDecorations;

        OverlayFlags(boolean secure, boolean ownContentOnly, boolean shouldShowSystemDecorations) {
            this.mSecure = secure;
            this.mOwnContentOnly = ownContentOnly;
            this.mShouldShowSystemDecorations = shouldShowSystemDecorations;
        }

        static com.android.server.display.OverlayDisplayAdapter.OverlayFlags parseFlags(java.lang.String flagString) {
            if (android.text.TextUtils.isEmpty(flagString)) {
                return new com.android.server.display.OverlayDisplayAdapter.OverlayFlags(false, false, false);
            }
            boolean secure = false;
            boolean ownContentOnly = false;
            boolean shouldShowSystemDecorations = false;
            for (java.lang.String flag : flagString.split(com.android.server.display.OverlayDisplayAdapter.FLAG_SPLITTER)) {
                if (com.android.server.display.OverlayDisplayAdapter.OVERLAY_DISPLAY_FLAG_SECURE.equals(flag)) {
                    secure = true;
                }
                if (com.android.server.display.OverlayDisplayAdapter.OVERLAY_DISPLAY_FLAG_OWN_CONTENT_ONLY.equals(flag)) {
                    ownContentOnly = true;
                }
                if (com.android.server.display.OverlayDisplayAdapter.OVERLAY_DISPLAY_FLAG_SHOULD_SHOW_SYSTEM_DECORATIONS.equals(flag)) {
                    shouldShowSystemDecorations = true;
                }
            }
            return new com.android.server.display.OverlayDisplayAdapter.OverlayFlags(secure, ownContentOnly, shouldShowSystemDecorations);
        }

        public java.lang.String toString() {
            return "{secure=" + this.mSecure + ", ownContentOnly=" + this.mOwnContentOnly + ", shouldShowSystemDecorations=" + this.mShouldShowSystemDecorations + "}";
        }
    }
}
