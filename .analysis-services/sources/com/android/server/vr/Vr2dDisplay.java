package com.android.server.vr;

/* JADX INFO: loaded from: classes3.dex */
class Vr2dDisplay {
    private static final boolean DEBUG = false;
    private static final java.lang.String DEBUG_ACTION_SET_MODE = "com.android.server.vr.Vr2dDisplay.SET_MODE";
    private static final java.lang.String DEBUG_ACTION_SET_SURFACE = "com.android.server.vr.Vr2dDisplay.SET_SURFACE";
    private static final java.lang.String DEBUG_EXTRA_MODE_ON = "com.android.server.vr.Vr2dDisplay.EXTRA_MODE_ON";
    private static final java.lang.String DEBUG_EXTRA_SURFACE = "com.android.server.vr.Vr2dDisplay.EXTRA_SURFACE";
    public static final int DEFAULT_VIRTUAL_DISPLAY_DPI = 320;
    public static final int DEFAULT_VIRTUAL_DISPLAY_HEIGHT = 1800;
    public static final int DEFAULT_VIRTUAL_DISPLAY_WIDTH = 1400;
    private static final java.lang.String DISPLAY_NAME = "VR 2D Display";
    public static final int MIN_VR_DISPLAY_DPI = 1;
    public static final int MIN_VR_DISPLAY_HEIGHT = 1;
    public static final int MIN_VR_DISPLAY_WIDTH = 1;
    private static final int STOP_VIRTUAL_DISPLAY_DELAY_MILLIS = 2000;
    private static final java.lang.String TAG = "Vr2dDisplay";
    private static final java.lang.String UNIQUE_DISPLAY_ID = "277f1a09-b88d-4d1e-8716-796f114d080b";
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final android.hardware.display.DisplayManager mDisplayManager;
    private android.media.ImageReader mImageReader;
    private boolean mIsPersistentVrModeEnabled;
    private boolean mIsVrModeOverrideEnabled;
    private java.lang.Runnable mStopVDRunnable;
    private android.view.Surface mSurface;
    private android.hardware.display.VirtualDisplay mVirtualDisplay;
    private final android.service.vr.IVrManager mVrManager;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private final java.lang.Object mVdLock = new java.lang.Object();
    private final android.os.Handler mHandler = new android.os.Handler();
    private final android.service.vr.IPersistentVrStateCallbacks mVrStateCallbacks = new android.service.vr.IPersistentVrStateCallbacks.Stub() { // from class: com.android.server.vr.Vr2dDisplay.1
        public void onPersistentVrStateChanged(boolean enabled) {
            if (enabled != com.android.server.vr.Vr2dDisplay.this.mIsPersistentVrModeEnabled) {
                com.android.server.vr.Vr2dDisplay.this.mIsPersistentVrModeEnabled = enabled;
                com.android.server.vr.Vr2dDisplay.this.updateVirtualDisplay();
            }
        }
    };
    private boolean mIsVirtualDisplayAllowed = true;
    private boolean mBootsToVr = false;
    private int mVirtualDisplayWidth = DEFAULT_VIRTUAL_DISPLAY_WIDTH;
    private int mVirtualDisplayHeight = 1800;
    private int mVirtualDisplayDpi = 320;

    public Vr2dDisplay(android.hardware.display.DisplayManager displayManager, android.app.ActivityManagerInternal activityManagerInternal, com.android.server.wm.WindowManagerInternal windowManagerInternal, android.service.vr.IVrManager vrManager) {
        this.mDisplayManager = displayManager;
        this.mActivityManagerInternal = activityManagerInternal;
        this.mWindowManagerInternal = windowManagerInternal;
        this.mVrManager = vrManager;
    }

    public void init(android.content.Context context, boolean bootsToVr) {
        startVrModeListener();
        startDebugOnlyBroadcastReceiver(context);
        this.mBootsToVr = bootsToVr;
        if (this.mBootsToVr) {
            updateVirtualDisplay();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVirtualDisplay() {
        if (shouldRunVirtualDisplay()) {
            android.util.Log.i(TAG, "Attempting to start virtual display");
            startVirtualDisplay();
        } else {
            stopVirtualDisplay();
        }
    }

    private void startDebugOnlyBroadcastReceiver(android.content.Context context) {
    }

    private void startVrModeListener() {
        if (this.mVrManager != null) {
            try {
                this.mVrManager.registerPersistentVrStateListener(this.mVrStateCallbacks);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Could not register VR State listener.", e);
            }
        }
    }

    public void setVirtualDisplayProperties(android.app.Vr2dDisplayProperties displayProperties) {
        synchronized (this.mVdLock) {
            int width = displayProperties.getWidth();
            int height = displayProperties.getHeight();
            int dpi = displayProperties.getDpi();
            boolean resized = false;
            if (width < 1 || height < 1 || dpi < 1) {
                android.util.Log.i(TAG, "Ignoring Width/Height/Dpi values of " + width + "," + height + "," + dpi);
            } else {
                android.util.Log.i(TAG, "Setting width/height/dpi to " + width + "," + height + "," + dpi);
                this.mVirtualDisplayWidth = width;
                this.mVirtualDisplayHeight = height;
                this.mVirtualDisplayDpi = dpi;
                resized = true;
            }
            if ((displayProperties.getAddedFlags() & 1) != 1) {
                if ((displayProperties.getRemovedFlags() & 1) == 1) {
                    this.mIsVirtualDisplayAllowed = false;
                }
            } else {
                this.mIsVirtualDisplayAllowed = true;
            }
            if (this.mVirtualDisplay != null && resized && this.mIsVirtualDisplayAllowed) {
                this.mVirtualDisplay.resize(this.mVirtualDisplayWidth, this.mVirtualDisplayHeight, this.mVirtualDisplayDpi);
                android.media.ImageReader oldImageReader = this.mImageReader;
                this.mImageReader = null;
                startImageReader();
                oldImageReader.close();
            }
            updateVirtualDisplay();
        }
    }

    public int getVirtualDisplayId() {
        synchronized (this.mVdLock) {
            if (this.mVirtualDisplay != null) {
                int virtualDisplayId = this.mVirtualDisplay.getDisplay().getDisplayId();
                return virtualDisplayId;
            }
            return -1;
        }
    }

    private void startVirtualDisplay() {
        if (this.mDisplayManager == null) {
            android.util.Log.w(TAG, "Cannot create virtual display because mDisplayManager == null");
            return;
        }
        synchronized (this.mVdLock) {
            if (this.mVirtualDisplay != null) {
                android.util.Log.i(TAG, "VD already exists, ignoring request");
                return;
            }
            int flags = 64 | 128;
            android.hardware.display.VirtualDisplayConfig.Builder builder = new android.hardware.display.VirtualDisplayConfig.Builder(DISPLAY_NAME, this.mVirtualDisplayWidth, this.mVirtualDisplayHeight, this.mVirtualDisplayDpi);
            builder.setUniqueId(UNIQUE_DISPLAY_ID);
            builder.setFlags(flags | 1 | 8 | 256 | 4 | 1024);
            this.mVirtualDisplay = this.mDisplayManager.createVirtualDisplay(null, builder.build(), null, null);
            if (this.mVirtualDisplay != null) {
                updateDisplayId(this.mVirtualDisplay.getDisplay().getDisplayId());
                startImageReader();
                android.util.Log.i(TAG, "VD created: " + this.mVirtualDisplay);
            } else {
                android.util.Log.w(TAG, "Virtual display id is null after createVirtualDisplay");
                updateDisplayId(-1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplayId(int displayId) {
        ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).setVr2dDisplayId(displayId);
        this.mWindowManagerInternal.setVr2dDisplayId(displayId);
    }

    private void stopVirtualDisplay() {
        if (this.mStopVDRunnable == null) {
            this.mStopVDRunnable = new java.lang.Runnable() { // from class: com.android.server.vr.Vr2dDisplay.3
                @Override // java.lang.Runnable
                public void run() {
                    if (com.android.server.vr.Vr2dDisplay.this.shouldRunVirtualDisplay()) {
                        android.util.Log.i(com.android.server.vr.Vr2dDisplay.TAG, "Virtual Display destruction stopped: VrMode is back on.");
                        return;
                    }
                    android.util.Log.i(com.android.server.vr.Vr2dDisplay.TAG, "Stopping Virtual Display");
                    synchronized (com.android.server.vr.Vr2dDisplay.this.mVdLock) {
                        com.android.server.vr.Vr2dDisplay.this.updateDisplayId(-1);
                        com.android.server.vr.Vr2dDisplay.this.setSurfaceLocked(null);
                        if (com.android.server.vr.Vr2dDisplay.this.mVirtualDisplay != null) {
                            com.android.server.vr.Vr2dDisplay.this.mVirtualDisplay.release();
                            com.android.server.vr.Vr2dDisplay.this.mVirtualDisplay = null;
                        }
                        com.android.server.vr.Vr2dDisplay.this.stopImageReader();
                    }
                }
            };
        }
        this.mHandler.removeCallbacks(this.mStopVDRunnable);
        this.mHandler.postDelayed(this.mStopVDRunnable, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSurfaceLocked(android.view.Surface surface) {
        if (this.mSurface != surface) {
            if (surface == null || surface.isValid()) {
                android.util.Log.i(TAG, "Setting the new surface from " + this.mSurface + " to " + surface);
                if (this.mVirtualDisplay != null) {
                    this.mVirtualDisplay.setSurface(surface);
                }
                if (this.mSurface != null) {
                    this.mSurface.release();
                }
                this.mSurface = surface;
            }
        }
    }

    private void startImageReader() {
        if (this.mImageReader == null) {
            this.mImageReader = android.media.ImageReader.newInstance(this.mVirtualDisplayWidth, this.mVirtualDisplayHeight, 1, 2);
            android.util.Log.i(TAG, "VD startImageReader: res = " + this.mVirtualDisplayWidth + "X" + this.mVirtualDisplayHeight + ", dpi = " + this.mVirtualDisplayDpi);
        }
        synchronized (this.mVdLock) {
            setSurfaceLocked(this.mImageReader.getSurface());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopImageReader() {
        if (this.mImageReader != null) {
            this.mImageReader.close();
            this.mImageReader = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldRunVirtualDisplay() {
        return this.mIsVirtualDisplayAllowed && (this.mBootsToVr || this.mIsPersistentVrModeEnabled || this.mIsVrModeOverrideEnabled);
    }
}
