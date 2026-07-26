package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
class WallpaperDisplayHelper {
    private static final float LARGE_SCREEN_MIN_DP = 600.0f;
    private static final java.lang.String TAG = com.android.server.wallpaper.WallpaperDisplayHelper.class.getSimpleName();
    private final android.hardware.display.DisplayManager mDisplayManager;
    private final boolean mIsFoldable;
    private com.android.server.wallpaper.IWallpaperManagerServiceExt mWallpaperManagerServiceExt;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private final android.util.SparseArray<com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData> mDisplayDatas = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.graphics.Point> mDefaultDisplaySizes = new android.util.SparseArray<>();
    private final java.util.List<android.util.Pair<java.lang.Integer, java.lang.Integer>> mFoldableOrientationPairs = new java.util.ArrayList();
    private boolean mIsLargeScreen = false;

    static final class DisplayData {
        final int mDisplayId;
        int mWidth = -1;
        int mHeight = -1;
        final android.graphics.Rect mPadding = new android.graphics.Rect(0, 0, 0, 0);

        DisplayData(int displayId) {
            this.mDisplayId = displayId;
        }
    }

    WallpaperDisplayHelper(android.hardware.display.DisplayManager displayManager, android.view.WindowManager windowManager, com.android.server.wm.WindowManagerInternal windowManagerInternal, boolean isFoldable, com.android.server.wallpaper.IWallpaperManagerServiceExt wallpaperManagerServiceExt) {
        android.util.Pair<java.lang.Integer, java.lang.Integer> pair;
        this.mDisplayManager = displayManager;
        this.mWindowManagerInternal = windowManagerInternal;
        this.mWallpaperManagerServiceExt = wallpaperManagerServiceExt;
        this.mIsFoldable = isFoldable;
        if (com.android.window.flags.Flags.multiCrop()) {
            java.util.Set<android.view.WindowMetrics> metrics = windowManager.getPossibleMaximumWindowMetrics(0);
            boolean populateOrientationPairs = isFoldable && metrics.size() == 2;
            float surface = 0.0f;
            int firstOrientation = -1;
            for (android.view.WindowMetrics metric : metrics) {
                android.graphics.Rect bounds = metric.getBounds();
                android.graphics.Point displaySize = new android.graphics.Point(bounds.width(), bounds.height());
                android.graphics.Point reversedDisplaySize = new android.graphics.Point(displaySize.y, displaySize.x);
                for (android.graphics.Point point : java.util.List.of(displaySize, reversedDisplaySize)) {
                    android.graphics.Point reversedDisplaySize2 = reversedDisplaySize;
                    int orientation = android.app.WallpaperManager.getOrientation(point);
                    android.graphics.Point display = this.mDefaultDisplaySizes.get(orientation);
                    if (display == null || display.x * display.y < point.x * point.y) {
                        this.mDefaultDisplaySizes.put(orientation, point);
                    }
                    reversedDisplaySize = reversedDisplaySize2;
                }
                this.mIsLargeScreen |= ((float) displaySize.x) / metric.getDensity() >= LARGE_SCREEN_MIN_DP;
                if (populateOrientationPairs) {
                    int orientation2 = android.app.WallpaperManager.getOrientation(displaySize);
                    float newSurface = (displaySize.x * displaySize.y) / (metric.getDensity() * metric.getDensity());
                    if (surface <= 0.0f) {
                        surface = newSurface;
                        firstOrientation = orientation2;
                    } else {
                        if (newSurface > surface) {
                            pair = new android.util.Pair<>(java.lang.Integer.valueOf(firstOrientation), java.lang.Integer.valueOf(orientation2));
                        } else {
                            pair = new android.util.Pair<>(java.lang.Integer.valueOf(orientation2), java.lang.Integer.valueOf(firstOrientation));
                        }
                        android.util.Pair<java.lang.Integer, java.lang.Integer> rotatedPair = new android.util.Pair<>(java.lang.Integer.valueOf(android.app.WallpaperManager.getRotatedOrientation(((java.lang.Integer) pair.first).intValue())), java.lang.Integer.valueOf(android.app.WallpaperManager.getRotatedOrientation(((java.lang.Integer) pair.second).intValue())));
                        this.mFoldableOrientationPairs.add(pair);
                        this.mFoldableOrientationPairs.add(rotatedPair);
                    }
                }
            }
        }
    }

    com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData getDisplayDataOrCreate(int displayId) {
        com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = this.mDisplayDatas.get(displayId);
        if (wpdData == null) {
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData2 = new com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData(displayId);
            ensureSaneWallpaperDisplaySize(wpdData2, displayId);
            this.mDisplayDatas.append(displayId, wpdData2);
            return wpdData2;
        }
        return wpdData;
    }

    int getDefaultDisplayCurrentOrientation() {
        android.graphics.Point displaySize = new android.graphics.Point();
        this.mDisplayManager.getDisplay(0).getSize(displaySize);
        return android.app.WallpaperManager.getOrientation(displaySize);
    }

    void removeDisplayData(int displayId) {
        this.mDisplayDatas.remove(displayId);
    }

    void ensureSaneWallpaperDisplaySize(com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData, int displayId) {
        int baseSize = getMaximumSizeDimension(displayId);
        if (wpdData.mWidth < baseSize) {
            wpdData.mWidth = baseSize;
        }
        if (wpdData.mHeight < baseSize) {
            wpdData.mHeight = baseSize;
        }
    }

    int getMaximumSizeDimension(int displayId) {
        android.view.Display display = this.mDisplayManager.getDisplay(displayId);
        if (display == null) {
            android.util.Slog.w(TAG, "Invalid displayId=" + displayId + " " + android.os.Debug.getCallers(4));
            display = this.mDisplayManager.getDisplay(0);
        }
        return display.getMaximumSizeDimension();
    }

    void forEachDisplayData(java.util.function.Consumer<com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData> action) {
        for (int i = this.mDisplayDatas.size() - 1; i >= 0; i--) {
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = this.mDisplayDatas.valueAt(i);
            action.accept(wpdData);
        }
    }

    android.view.Display[] getDisplays(com.android.server.wallpaper.WallpaperData wallpaperData) {
        return this.mWallpaperManagerServiceExt.getDisplays(this.mDisplayManager, wallpaperData);
    }

    android.hardware.display.DisplayManager getDisplayManager() {
        return this.mDisplayManager;
    }

    android.view.DisplayInfo getDisplayInfo(int displayId) {
        android.view.DisplayInfo displayInfo = new android.view.DisplayInfo();
        this.mDisplayManager.getDisplay(displayId).getDisplayInfo(displayInfo);
        return displayInfo;
    }

    boolean isUsableDisplay(int displayId, int clientUid) {
        return isUsableDisplay(this.mDisplayManager.getDisplay(displayId), clientUid);
    }

    boolean isUsableDisplay(android.view.Display display, int clientUid) {
        if (display == null || !display.hasAccess(clientUid)) {
            return false;
        }
        int displayId = display.getDisplayId();
        if (displayId == 0 || this.mWallpaperManagerServiceExt.isUsableDisplay(display)) {
            return true;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mWindowManagerInternal.isHomeSupportedOnDisplay(displayId);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    boolean isValidDisplay(int displayId) {
        return this.mDisplayManager.getDisplay(displayId) != null;
    }

    android.util.SparseArray<android.graphics.Point> getDefaultDisplaySizes() {
        return this.mDefaultDisplaySizes;
    }

    int getDefaultDisplayLargestDimension() {
        int result = -1;
        for (int i = 0; i < this.mDefaultDisplaySizes.size(); i++) {
            android.graphics.Point size = this.mDefaultDisplaySizes.valueAt(i);
            result = java.lang.Math.max(result, java.lang.Math.max(size.x, size.y));
        }
        return result;
    }

    boolean isFoldable() {
        return this.mIsFoldable;
    }

    boolean isLargeScreen() {
        return this.mIsLargeScreen;
    }

    int getFoldedOrientation(int orientation) {
        for (android.util.Pair<java.lang.Integer, java.lang.Integer> pair : this.mFoldableOrientationPairs) {
            if (((java.lang.Integer) pair.second).equals(java.lang.Integer.valueOf(orientation))) {
                return ((java.lang.Integer) pair.first).intValue();
            }
        }
        return -1;
    }

    int getUnfoldedOrientation(int orientation) {
        for (android.util.Pair<java.lang.Integer, java.lang.Integer> pair : this.mFoldableOrientationPairs) {
            if (((java.lang.Integer) pair.first).equals(java.lang.Integer.valueOf(orientation))) {
                return ((java.lang.Integer) pair.second).intValue();
            }
        }
        return -1;
    }
}
