package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
final class PointerIconCache {
    private static final java.lang.String TAG = com.android.server.input.PointerIconCache.class.getSimpleName();
    private final android.content.Context mContext;
    private final com.android.server.input.NativeInputManagerService mNative;
    private final android.os.Handler mUiThreadHandler = com.android.server.UiThread.getHandler();
    private final android.util.SparseArray<android.util.SparseArray<android.view.PointerIcon>> mLoadedPointerIconsByDisplayAndType = new android.util.SparseArray<>();
    private boolean mUseLargePointerIcons = false;
    private final android.util.SparseArray<android.content.Context> mDisplayContexts = new android.util.SparseArray<>();
    private final android.util.SparseIntArray mDisplayDensities = new android.util.SparseIntArray();
    private int mPointerIconFillStyle = 0;
    private float mPointerIconScale = 1.0f;
    private final android.hardware.display.DisplayManager.DisplayListener mDisplayListener = new android.hardware.display.DisplayManager.DisplayListener() { // from class: com.android.server.input.PointerIconCache.1
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
            synchronized (com.android.server.input.PointerIconCache.this.mLoadedPointerIconsByDisplayAndType) {
                com.android.server.input.PointerIconCache.this.updateDisplayDensityLocked(displayId);
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
            synchronized (com.android.server.input.PointerIconCache.this.mLoadedPointerIconsByDisplayAndType) {
                com.android.server.input.PointerIconCache.this.mLoadedPointerIconsByDisplayAndType.remove(displayId);
                com.android.server.input.PointerIconCache.this.mDisplayContexts.remove(displayId);
                com.android.server.input.PointerIconCache.this.mDisplayDensities.delete(displayId);
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            com.android.server.input.PointerIconCache.this.handleDisplayChanged(displayId);
        }
    };

    PointerIconCache(android.content.Context context, com.android.server.input.NativeInputManagerService nativeService) {
        this.mContext = context;
        this.mNative = nativeService;
    }

    public void systemRunning() {
        android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) java.util.Objects.requireNonNull((android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class));
        displayManager.registerDisplayListener(this.mDisplayListener, this.mUiThreadHandler);
        android.view.Display[] displays = displayManager.getDisplays();
        for (android.view.Display display : displays) {
            this.mDisplayListener.onDisplayAdded(display.getDisplayId());
        }
    }

    public void monitor() {
        synchronized (this.mLoadedPointerIconsByDisplayAndType) {
        }
    }

    public void setUseLargePointerIcons(final boolean useLargeIcons) {
        this.mUiThreadHandler.post(new java.lang.Runnable() { // from class: com.android.server.input.PointerIconCache$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setUseLargePointerIcons$0(useLargeIcons);
            }
        });
    }

    public void setPointerFillStyle(final int fillStyle) {
        this.mUiThreadHandler.post(new java.lang.Runnable() { // from class: com.android.server.input.PointerIconCache$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setPointerFillStyle$1(fillStyle);
            }
        });
    }

    public void setPointerScale(final float scale) {
        this.mUiThreadHandler.post(new java.lang.Runnable() { // from class: com.android.server.input.PointerIconCache$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setPointerScale$2(scale);
            }
        });
    }

    public android.view.PointerIcon getLoadedPointerIcon(int displayId, int type) {
        android.view.PointerIcon pointerIcon;
        synchronized (this.mLoadedPointerIconsByDisplayAndType) {
            android.util.SparseArray<android.view.PointerIcon> iconsByType = this.mLoadedPointerIconsByDisplayAndType.get(displayId);
            if (iconsByType == null) {
                iconsByType = new android.util.SparseArray<>();
                this.mLoadedPointerIconsByDisplayAndType.put(displayId, iconsByType);
            }
            android.view.PointerIcon icon = iconsByType.get(type);
            if (icon == null) {
                android.content.Context context = getContextForDisplayLocked(displayId);
                android.content.res.Resources.Theme theme = context.getResources().newTheme();
                theme.setTo(context.getTheme());
                theme.applyStyle(android.view.PointerIcon.vectorFillStyleToResource(this.mPointerIconFillStyle), true);
                icon = android.view.PointerIcon.getLoadedSystemIcon(new android.view.ContextThemeWrapper(context, theme), type, this.mUseLargePointerIcons, this.mPointerIconScale);
                iconsByType.put(type, icon);
            }
            pointerIcon = (android.view.PointerIcon) java.util.Objects.requireNonNull(icon);
        }
        return pointerIcon;
    }

    private android.content.Context getContextForDisplayLocked(int displayId) {
        if (displayId == -1) {
            return this.mContext;
        }
        if (displayId == this.mContext.getDisplay().getDisplayId()) {
            return this.mContext;
        }
        android.content.Context displayContext = this.mDisplayContexts.get(displayId);
        if (displayContext == null) {
            android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) java.util.Objects.requireNonNull((android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class));
            android.view.Display display = displayManager.getDisplay(displayId);
            if (display == null) {
                return this.mContext;
            }
            android.content.Context displayContext2 = this.mContext.createDisplayContext(display);
            this.mDisplayContexts.put(displayId, displayContext2);
            return displayContext2;
        }
        return displayContext;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDisplayChanged(int displayId) {
        synchronized (this.mLoadedPointerIconsByDisplayAndType) {
            if (updateDisplayDensityLocked(displayId)) {
                android.util.Slog.i(TAG, "Reloading pointer icons due to density change on display: " + displayId);
                android.util.SparseArray<android.view.PointerIcon> iconsByType = this.mLoadedPointerIconsByDisplayAndType.get(displayId);
                if (iconsByType == null) {
                    return;
                }
                iconsByType.clear();
                this.mDisplayContexts.remove(displayId);
                this.mNative.reloadPointerIcons();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleSetUseLargePointerIcons, reason: merged with bridge method [inline-methods] */
    public void lambda$setUseLargePointerIcons$0(boolean useLargeIcons) {
        synchronized (this.mLoadedPointerIconsByDisplayAndType) {
            if (this.mUseLargePointerIcons == useLargeIcons) {
                return;
            }
            this.mUseLargePointerIcons = useLargeIcons;
            this.mLoadedPointerIconsByDisplayAndType.clear();
            this.mNative.reloadPointerIcons();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleSetPointerFillStyle, reason: merged with bridge method [inline-methods] */
    public void lambda$setPointerFillStyle$1(int fillStyle) {
        synchronized (this.mLoadedPointerIconsByDisplayAndType) {
            if (this.mPointerIconFillStyle == fillStyle) {
                return;
            }
            this.mPointerIconFillStyle = fillStyle;
            this.mLoadedPointerIconsByDisplayAndType.clear();
            this.mNative.reloadPointerIcons();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleSetPointerScale, reason: merged with bridge method [inline-methods] */
    public void lambda$setPointerScale$2(float scale) {
        synchronized (this.mLoadedPointerIconsByDisplayAndType) {
            if (this.mPointerIconScale == scale) {
                return;
            }
            this.mPointerIconScale = scale;
            this.mLoadedPointerIconsByDisplayAndType.clear();
            this.mNative.reloadPointerIcons();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateDisplayDensityLocked(int displayId) {
        android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) java.util.Objects.requireNonNull((android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class));
        android.view.Display display = displayManager.getDisplay(displayId);
        if (display == null) {
            return false;
        }
        android.view.DisplayInfo info = new android.view.DisplayInfo();
        display.getDisplayInfo(info);
        int oldDensity = this.mDisplayDensities.get(displayId, 0);
        if (oldDensity == info.logicalDensityDpi) {
            return false;
        }
        this.mDisplayDensities.put(displayId, info.logicalDensityDpi);
        return true;
    }
}
