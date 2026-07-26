package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class VirtualDisplayAdapter extends com.android.server.display.DisplayAdapter {
    static final java.lang.String TAG = "VirtualDisplayAdapter";
    static final java.lang.String UNIQUE_ID_PREFIX = "virtual:";
    private static final java.util.concurrent.atomic.AtomicInteger sNextUniqueIndex = new java.util.concurrent.atomic.AtomicInteger(0);
    private final android.os.Handler mHandler;
    private final com.android.server.display.VirtualDisplayAdapter.SurfaceControlDisplayFactory mSurfaceControlDisplayFactory;
    private com.android.server.display.IVirtualDisplayAdapterExt mVdaExt;
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice> mVirtualDisplayDevices;

    public interface SurfaceControlDisplayFactory {
        android.os.IBinder createDisplay(java.lang.String str, boolean z, java.lang.String str2, float f);

        void destroyDisplay(android.os.IBinder iBinder);
    }

    @Override // com.android.server.display.DisplayAdapter
    public /* bridge */ /* synthetic */ void dumpLocked(java.io.PrintWriter printWriter) {
        super.dumpLocked(printWriter);
    }

    @Override // com.android.server.display.DisplayAdapter
    public /* bridge */ /* synthetic */ void registerLocked() {
        super.registerLocked();
    }

    public VirtualDisplayAdapter(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.content.Context context, android.os.Handler handler, com.android.server.display.DisplayAdapter.Listener listener, com.android.server.display.feature.DisplayManagerFlags featureFlags) {
        this(syncRoot, context, handler, listener, new com.android.server.display.VirtualDisplayAdapter.SurfaceControlDisplayFactory() { // from class: com.android.server.display.VirtualDisplayAdapter.1
            @Override // com.android.server.display.VirtualDisplayAdapter.SurfaceControlDisplayFactory
            public android.os.IBinder createDisplay(java.lang.String name, boolean secure, java.lang.String uniqueId, float requestedRefreshRate) {
                return com.android.server.display.DisplayControl.createVirtualDisplay(name, secure, uniqueId, requestedRefreshRate);
            }

            @Override // com.android.server.display.VirtualDisplayAdapter.SurfaceControlDisplayFactory
            public void destroyDisplay(android.os.IBinder displayToken) {
                com.android.server.display.DisplayControl.destroyVirtualDisplay(displayToken);
            }
        }, featureFlags);
    }

    VirtualDisplayAdapter(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.content.Context context, android.os.Handler handler, com.android.server.display.DisplayAdapter.Listener listener, com.android.server.display.VirtualDisplayAdapter.SurfaceControlDisplayFactory surfaceControlDisplayFactory, com.android.server.display.feature.DisplayManagerFlags featureFlags) {
        super(syncRoot, context, handler, listener, TAG, featureFlags);
        this.mVirtualDisplayDevices = new android.util.ArrayMap<>();
        this.mVdaExt = (com.android.server.display.IVirtualDisplayAdapterExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IVirtualDisplayAdapterExt.class).create();
        this.mHandler = handler;
        this.mSurfaceControlDisplayFactory = surfaceControlDisplayFactory;
    }

    public com.android.server.display.DisplayDevice createVirtualDisplayLocked(android.hardware.display.IVirtualDisplayCallback callback, android.media.projection.IMediaProjection projection, int ownerUid, java.lang.String ownerPackageName, java.lang.String uniqueId, android.view.Surface surface, int flags, android.hardware.display.VirtualDisplayConfig virtualDisplayConfig) {
        com.android.server.display.VirtualDisplayAdapter.MediaProjectionCallback mediaProjectionCallback;
        java.lang.String str;
        boolean z;
        android.os.RemoteException ex;
        android.os.IBinder appToken = callback.asBinder();
        if (this.mVirtualDisplayDevices.containsKey(appToken)) {
            android.util.Slog.wtfStack(TAG, "Can't create virtual display, display with same appToken already exists");
            return null;
        }
        java.lang.String name = virtualDisplayConfig.getName();
        boolean secure = (flags & 4) != 0;
        android.os.IBinder displayToken = this.mSurfaceControlDisplayFactory.createDisplay(name, secure, uniqueId, virtualDisplayConfig.getRequestedRefreshRate());
        if (projection != null) {
            com.android.server.display.VirtualDisplayAdapter.MediaProjectionCallback mediaProjectionCallback2 = new com.android.server.display.VirtualDisplayAdapter.MediaProjectionCallback(appToken);
            mediaProjectionCallback = mediaProjectionCallback2;
        } else {
            mediaProjectionCallback = null;
        }
        com.android.server.display.VirtualDisplayAdapter.MediaProjectionCallback mediaProjectionCallback3 = mediaProjectionCallback;
        com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice device = new com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice(displayToken, appToken, ownerUid, ownerPackageName, surface, flags, new com.android.server.display.VirtualDisplayAdapter.Callback(callback, this.mHandler), projection, mediaProjectionCallback3, uniqueId, virtualDisplayConfig);
        this.mVirtualDisplayDevices.put(appToken, device);
        if (projection != null) {
            try {
                projection.registerCallback(mediaProjectionCallback3);
                str = TAG;
            } catch (android.os.RemoteException e) {
                ex = e;
                str = TAG;
            }
            try {
                android.util.Slog.d(str, "Virtual Display: registered media projection callback for new VirtualDisplayDevice");
            } catch (android.os.RemoteException e2) {
                ex = e2;
                z = false;
                android.util.Slog.e(str, "Virtual Display: error while setting up VirtualDisplayDevice", ex);
                this.mVirtualDisplayDevices.remove(appToken);
                device.destroyLocked(z);
                return null;
            }
        } else {
            str = TAG;
        }
        z = false;
        try {
            appToken.linkToDeath(device, 0);
            return device;
        } catch (android.os.RemoteException e3) {
            ex = e3;
            android.util.Slog.e(str, "Virtual Display: error while setting up VirtualDisplayDevice", ex);
            this.mVirtualDisplayDevices.remove(appToken);
            device.destroyLocked(z);
            return null;
        }
    }

    public void resizeVirtualDisplayLocked(android.os.IBinder appToken, int width, int height, int densityDpi) {
        com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice device = this.mVirtualDisplayDevices.get(appToken);
        if (device != null) {
            android.util.Slog.v(TAG, "Resize VirtualDisplay " + device.mName + " to " + width + " " + height);
            device.resizeLocked(width, height, densityDpi);
        }
    }

    android.view.Surface getVirtualDisplaySurfaceLocked(android.os.IBinder appToken) {
        com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice device = this.mVirtualDisplayDevices.get(appToken);
        if (device != null) {
            return device.getSurfaceLocked();
        }
        return null;
    }

    public void setVirtualDisplaySurfaceLocked(android.os.IBinder appToken, android.view.Surface surface) {
        com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice device = this.mVirtualDisplayDevices.get(appToken);
        if (device != null) {
            android.util.Slog.v(TAG, "Update surface for VirtualDisplay " + device.mName);
            device.setSurfaceLocked(surface);
        }
    }

    void setDisplayIdToMirror(android.os.IBinder appToken, int displayId) {
        com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice device = this.mVirtualDisplayDevices.get(appToken);
        if (device != null) {
            device.setDisplayIdToMirror(displayId);
        }
    }

    public com.android.server.display.DisplayDevice releaseVirtualDisplayLocked(android.os.IBinder appToken) {
        com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice device = this.mVirtualDisplayDevices.remove(appToken);
        if (device != null) {
            android.util.Slog.v(TAG, "Release VirtualDisplay " + device.mName);
            device.destroyLocked(true);
            appToken.unlinkToDeath(device, 0);
        }
        return device;
    }

    void setVirtualDisplayStateLocked(android.os.IBinder appToken, boolean isOn) {
        com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice device = this.mVirtualDisplayDevices.get(appToken);
        if (device != null) {
            device.setDisplayState(isOn);
        }
    }

    static java.lang.String generateDisplayUniqueId(java.lang.String packageName, int uid, android.hardware.display.VirtualDisplayConfig config) {
        java.lang.String str;
        java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append(UNIQUE_ID_PREFIX).append(packageName);
        if (config.getUniqueId() != null) {
            str = ":" + config.getUniqueId();
        } else {
            str = "," + uid + "," + config.getName() + "," + sNextUniqueIndex.getAndIncrement();
        }
        return sbAppend.append(str).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBinderDiedLocked(android.os.IBinder appToken) {
        this.mVirtualDisplayDevices.remove(appToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMediaProjectionStoppedLocked(android.os.IBinder appToken) {
        com.android.server.display.VirtualDisplayAdapter.VirtualDisplayDevice device = this.mVirtualDisplayDevices.get(appToken);
        if (device != null) {
            android.util.Slog.i(TAG, "Virtual display device released because media projection stopped: " + device.mName);
            device.stopLocked();
        }
    }

    private final class VirtualDisplayDevice extends com.android.server.display.DisplayDevice implements android.os.IBinder.DeathRecipient {
        private static final int PENDING_RESIZE = 2;
        private static final int PENDING_SURFACE_CHANGE = 1;
        private static final float REFRESH_RATE = 60.0f;
        private final android.os.IBinder mAppToken;
        private final com.android.server.display.VirtualDisplayAdapter.Callback mCallback;
        private int mDensityDpi;
        private int mDisplayIdToMirror;
        private int mDisplayState;
        private final int mFlags;
        private int mHeight;
        private com.android.server.display.DisplayDeviceInfo mInfo;
        private boolean mIsDisplayOn;
        private boolean mIsWindowManagerMirroring;
        private final android.media.projection.IMediaProjectionCallback mMediaProjectionCallback;
        private android.view.Display.Mode mMode;
        final java.lang.String mName;
        final java.lang.String mOwnerPackageName;
        private final int mOwnerUid;
        private int mPendingChanges;
        private final android.media.projection.IMediaProjection mProjection;
        private float mRequestedRefreshRate;
        private boolean mStopped;
        private android.view.Surface mSurface;
        private int mWidth;

        public VirtualDisplayDevice(android.os.IBinder displayToken, android.os.IBinder appToken, int ownerUid, java.lang.String ownerPackageName, android.view.Surface surface, int flags, com.android.server.display.VirtualDisplayAdapter.Callback callback, android.media.projection.IMediaProjection projection, android.media.projection.IMediaProjectionCallback mediaProjectionCallback, java.lang.String uniqueId, android.hardware.display.VirtualDisplayConfig virtualDisplayConfig) {
            super(com.android.server.display.VirtualDisplayAdapter.this, displayToken, uniqueId, com.android.server.display.VirtualDisplayAdapter.this.getContext());
            this.mAppToken = appToken;
            this.mOwnerUid = ownerUid;
            this.mOwnerPackageName = ownerPackageName;
            this.mName = virtualDisplayConfig.getName();
            this.mWidth = virtualDisplayConfig.getWidth();
            this.mHeight = virtualDisplayConfig.getHeight();
            this.mDensityDpi = virtualDisplayConfig.getDensityDpi();
            this.mRequestedRefreshRate = virtualDisplayConfig.getRequestedRefreshRate();
            this.mMode = com.android.server.display.DisplayAdapter.createMode(this.mWidth, this.mHeight, getRefreshRate());
            this.mSurface = surface;
            this.mFlags = flags;
            this.mCallback = callback;
            this.mProjection = projection;
            this.mMediaProjectionCallback = mediaProjectionCallback;
            this.mDisplayState = 0;
            this.mPendingChanges |= 1;
            this.mIsDisplayOn = surface != null;
            this.mDisplayIdToMirror = virtualDisplayConfig.getDisplayIdToMirror();
            this.mIsWindowManagerMirroring = virtualDisplayConfig.isWindowManagerMirroringEnabled();
            com.android.server.display.VirtualDisplayAdapter.this.mVdaExt.createDisplayLocked(this.mOwnerPackageName);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.display.VirtualDisplayAdapter.this.getSyncRoot()) {
                com.android.server.display.VirtualDisplayAdapter.this.handleBinderDiedLocked(this.mAppToken);
                android.util.Slog.i(com.android.server.display.VirtualDisplayAdapter.TAG, "Virtual display device released because application token died: " + this.mOwnerPackageName);
                destroyLocked(false);
                if (this.mProjection != null && this.mMediaProjectionCallback != null) {
                    try {
                        this.mProjection.unregisterCallback(this.mMediaProjectionCallback);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(com.android.server.display.VirtualDisplayAdapter.TAG, "Failed to unregister callback in binderDied", e);
                    }
                    com.android.server.display.VirtualDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 3);
                } else {
                    com.android.server.display.VirtualDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 3);
                }
            }
        }

        public void destroyLocked(boolean binderAlive) {
            if (this.mSurface != null) {
                this.mSurface.release();
                this.mSurface = null;
            }
            com.android.server.display.VirtualDisplayAdapter.this.mSurfaceControlDisplayFactory.destroyDisplay(getDisplayTokenLocked());
            if (this.mProjection != null && this.mMediaProjectionCallback != null) {
                try {
                    this.mProjection.unregisterCallback(this.mMediaProjectionCallback);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.display.VirtualDisplayAdapter.TAG, "Failed to unregister callback in destroy", e);
                }
            }
            if (binderAlive) {
                this.mCallback.dispatchDisplayStopped();
            }
            com.android.server.display.VirtualDisplayAdapter.this.mVdaExt.destroyDisplayLocked(this.mOwnerPackageName);
        }

        @Override // com.android.server.display.DisplayDevice
        public int getDisplayIdToMirrorLocked() {
            return this.mDisplayIdToMirror;
        }

        void setDisplayIdToMirror(int displayIdToMirror) {
            if (this.mDisplayIdToMirror != displayIdToMirror) {
                this.mDisplayIdToMirror = displayIdToMirror;
                this.mInfo = null;
                com.android.server.display.VirtualDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2);
                com.android.server.display.VirtualDisplayAdapter.this.sendTraversalRequestLocked();
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public boolean isWindowManagerMirroringLocked() {
            return this.mIsWindowManagerMirroring;
        }

        @Override // com.android.server.display.DisplayDevice
        public void setWindowManagerMirroringLocked(boolean mirroring) {
            if (this.mIsWindowManagerMirroring != mirroring) {
                this.mIsWindowManagerMirroring = mirroring;
                com.android.server.display.VirtualDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2);
                com.android.server.display.VirtualDisplayAdapter.this.sendTraversalRequestLocked();
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public android.graphics.Point getDisplaySurfaceDefaultSizeLocked() {
            if (this.mSurface == null) {
                return null;
            }
            if (!this.mSurface.isValid()) {
                android.util.Slog.e(com.android.server.display.VirtualDisplayAdapter.TAG, "Surface has already been released.");
                return null;
            }
            android.graphics.Point surfaceSize = this.mSurface.getDefaultSize();
            return isRotatedLocked() ? new android.graphics.Point(surfaceSize.y, surfaceSize.x) : surfaceSize;
        }

        android.view.Surface getSurfaceLocked() {
            return this.mSurface;
        }

        @Override // com.android.server.display.DisplayDevice
        public boolean hasStableUniqueId() {
            return false;
        }

        @Override // com.android.server.display.DisplayDevice
        public java.lang.Runnable requestDisplayStateLocked(int state, float brightnessState, float sdrBrightnessState, com.android.server.display.DisplayOffloadSessionImpl displayOffloadSession) {
            if (state != this.mDisplayState) {
                this.mDisplayState = state;
                if (state == 1) {
                    this.mCallback.dispatchDisplayPaused();
                    return null;
                }
                this.mCallback.dispatchDisplayResumed();
                return null;
            }
            return null;
        }

        @Override // com.android.server.display.DisplayDevice
        public void performTraversalLocked(android.view.SurfaceControl.Transaction t) {
            if ((this.mPendingChanges & 2) != 0) {
                android.graphics.Rect displayRect = new android.graphics.Rect(0, 0, this.mWidth, this.mHeight);
                this.mDisplayDeviceExt.setProjectionLocked(displayRect, this);
                t.setDisplaySize(getDisplayTokenLocked(), displayRect.right, displayRect.bottom);
            }
            if ((this.mPendingChanges & 1) != 0) {
                try {
                    setSurfaceLocked(t, this.mSurface);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.e(com.android.server.display.VirtualDisplayAdapter.TAG, "setSurfaceLocked() illegal Surface", e);
                }
            }
            this.mPendingChanges = 0;
        }

        public void setSurfaceLocked(android.view.Surface surface) {
            if (!this.mStopped && this.mSurface != surface) {
                if ((this.mSurface != null) != (surface != null)) {
                    com.android.server.display.VirtualDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2);
                }
                com.android.server.display.VirtualDisplayAdapter.this.sendTraversalRequestLocked();
                this.mSurface = surface;
                this.mInfo = null;
                this.mPendingChanges |= 1;
            }
        }

        public void resizeLocked(int width, int height, int densityDpi) {
            if (this.mWidth != width || this.mHeight != height || this.mDensityDpi != densityDpi) {
                com.android.server.display.VirtualDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2);
                com.android.server.display.VirtualDisplayAdapter.this.sendTraversalRequestLocked();
                this.mWidth = width;
                this.mHeight = height;
                this.mMode = com.android.server.display.DisplayAdapter.createMode(width, height, getRefreshRate());
                this.mDensityDpi = densityDpi;
                this.mInfo = null;
                this.mPendingChanges |= 2;
            }
        }

        void setDisplayState(boolean isOn) {
            if (this.mIsDisplayOn != isOn) {
                this.mIsDisplayOn = isOn;
                this.mInfo = null;
                com.android.server.display.VirtualDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2);
            }
        }

        public void stopLocked() {
            android.util.Slog.d(com.android.server.display.VirtualDisplayAdapter.TAG, "Virtual Display: stopping device " + this.mName);
            setSurfaceLocked(null);
            this.mStopped = true;
        }

        @Override // com.android.server.display.DisplayDevice
        public void dumpLocked(java.io.PrintWriter pw) {
            super.dumpLocked(pw);
            pw.println("mFlags=" + this.mFlags);
            pw.println("mDisplayState=" + android.view.Display.stateToString(this.mDisplayState));
            pw.println("mStopped=" + this.mStopped);
            pw.println("mDisplayIdToMirror=" + this.mDisplayIdToMirror);
            pw.println("mWindowManagerMirroring=" + this.mIsWindowManagerMirroring);
            pw.println("mRequestedRefreshRate=" + this.mRequestedRefreshRate);
        }

        @Override // com.android.server.display.DisplayDevice
        public com.android.server.display.DisplayDeviceInfo getDisplayDeviceInfoLocked() {
            if (this.mInfo == null) {
                this.mInfo = new com.android.server.display.DisplayDeviceInfo();
                this.mInfo.name = this.mName;
                this.mInfo.uniqueId = getUniqueId();
                this.mInfo.width = this.mWidth;
                this.mInfo.height = this.mHeight;
                this.mInfo.modeId = this.mMode.getModeId();
                this.mInfo.renderFrameRate = this.mMode.getRefreshRate();
                this.mInfo.defaultModeId = this.mMode.getModeId();
                this.mInfo.supportedModes = new android.view.Display.Mode[]{this.mMode};
                this.mInfo.densityDpi = this.mDensityDpi;
                this.mInfo.xDpi = this.mDensityDpi;
                this.mInfo.yDpi = this.mDensityDpi;
                this.mInfo.presentationDeadlineNanos = 1000000000 / ((long) ((int) getRefreshRate()));
                this.mInfo.flags = 0;
                if ((this.mFlags & 1) == 0) {
                    this.mInfo.flags |= 48;
                }
                if ((this.mFlags & 16) != 0) {
                    this.mInfo.flags &= -33;
                } else {
                    this.mInfo.flags |= 128;
                    if ((this.mFlags & 2048) != 0) {
                        this.mInfo.flags |= 16384;
                    }
                }
                if ((this.mFlags & 32768) != 0) {
                    this.mInfo.flags |= 262144;
                }
                if ((this.mFlags & 4) != 0) {
                    this.mInfo.flags |= 4;
                }
                if ((this.mFlags & 2) != 0) {
                    this.mInfo.flags |= 64;
                    if ((this.mFlags & 1) != 0 && "portrait".equals(android.os.SystemProperties.get("persist.demo.remoterotation"))) {
                        this.mInfo.rotation = 3;
                    }
                }
                if ((this.mFlags & 32) != 0) {
                    this.mInfo.flags |= 512;
                }
                if ((this.mFlags & 128) != 0) {
                    this.mInfo.flags |= 2;
                }
                if ((this.mFlags & 256) != 0) {
                    this.mInfo.flags |= 1024;
                }
                if ((this.mFlags & 512) != 0) {
                    this.mInfo.flags |= 4096;
                }
                if ((this.mFlags & 1024) != 0) {
                    this.mInfo.flags |= 8192;
                }
                if ((this.mFlags & 4096) != 0) {
                    if ((this.mInfo.flags & 16384) != 0 || (this.mFlags & 32768) != 0) {
                        this.mInfo.flags |= 32768;
                    } else {
                        android.util.Slog.w(com.android.server.display.VirtualDisplayAdapter.TAG, "Ignoring VIRTUAL_DISPLAY_FLAG_ALWAYS_UNLOCKED as it requires VIRTUAL_DISPLAY_FLAG_DEVICE_DISPLAY_GROUP or VIRTUAL_DISPLAY_FLAG_OWN_DISPLAY_GROUP.");
                    }
                }
                if ((this.mFlags & 8192) != 0) {
                    this.mInfo.flags |= 65536;
                }
                if ((this.mFlags & 16384) != 0) {
                    if ((this.mFlags & 1024) != 0) {
                        this.mInfo.flags |= 131072;
                    } else {
                        android.util.Slog.w(com.android.server.display.VirtualDisplayAdapter.TAG, "Ignoring VIRTUAL_DISPLAY_FLAG_OWN_FOCUS as it requires VIRTUAL_DISPLAY_FLAG_TRUSTED.");
                    }
                }
                if ("OplusPuttDisplay".equals(this.mName)) {
                    com.android.server.display.DisplayDeviceInfo displayDeviceInfo = this.mInfo;
                    displayDeviceInfo.flags = 32768 | displayDeviceInfo.flags;
                }
                if ((this.mFlags & 65536) != 0) {
                    if ((this.mFlags & 1024) != 0 && (this.mFlags & 16384) != 0) {
                        this.mInfo.flags |= 524288;
                    } else {
                        android.util.Slog.w(com.android.server.display.VirtualDisplayAdapter.TAG, "Ignoring VIRTUAL_DISPLAY_FLAG_STEAL_TOP_FOCUS_DISABLED as it requires VIRTUAL_DISPLAY_FLAG_OWN_FOCUS which requires VIRTUAL_DISPLAY_FLAG_TRUSTED.");
                    }
                }
                this.mInfo.type = 5;
                this.mInfo.touch = (this.mFlags & 64) == 0 ? 0 : 3;
                this.mInfo.state = this.mIsDisplayOn ? 2 : 1;
                this.mInfo.ownerUid = this.mOwnerUid;
                this.mInfo.ownerPackageName = this.mOwnerPackageName;
                this.mInfo.displayShape = android.view.DisplayShape.createDefaultDisplayShape(this.mInfo.width, this.mInfo.height, false);
            }
            return this.mInfo;
        }

        private float getRefreshRate() {
            if (this.mRequestedRefreshRate != 0.0f) {
                return this.mRequestedRefreshRate;
            }
            return 60.0f;
        }
    }

    private static class Callback extends android.os.Handler {
        private static final int MSG_ON_DISPLAY_PAUSED = 0;
        private static final int MSG_ON_DISPLAY_RESUMED = 1;
        private static final int MSG_ON_DISPLAY_STOPPED = 2;
        private final android.hardware.display.IVirtualDisplayCallback mCallback;

        public Callback(android.hardware.display.IVirtualDisplayCallback callback, android.os.Handler handler) {
            super(handler.getLooper());
            this.mCallback = callback;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        this.mCallback.onPaused();
                        break;
                    case 1:
                        this.mCallback.onResumed();
                        break;
                    case 2:
                        this.mCallback.onStopped();
                        break;
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.display.VirtualDisplayAdapter.TAG, "Failed to notify listener of virtual display event.", e);
            }
        }

        public void dispatchDisplayPaused() {
            sendEmptyMessage(0);
        }

        public void dispatchDisplayResumed() {
            sendEmptyMessage(1);
        }

        public void dispatchDisplayStopped() {
            sendEmptyMessage(2);
        }
    }

    private final class MediaProjectionCallback extends android.media.projection.IMediaProjectionCallback.Stub {
        private android.os.IBinder mAppToken;

        public MediaProjectionCallback(android.os.IBinder appToken) {
            this.mAppToken = appToken;
        }

        public void onStop() {
            synchronized (com.android.server.display.VirtualDisplayAdapter.this.getSyncRoot()) {
                com.android.server.display.VirtualDisplayAdapter.this.handleMediaProjectionStoppedLocked(this.mAppToken);
            }
        }

        public void onCapturedContentResize(int width, int height) {
        }

        public void onCapturedContentVisibilityChanged(boolean isVisible) {
        }
    }
}
