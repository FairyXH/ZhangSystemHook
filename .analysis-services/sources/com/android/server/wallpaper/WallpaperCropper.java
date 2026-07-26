package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
public class WallpaperCropper {
    static final int ADD = 1;
    static final int BALANCE = 3;
    private static final boolean DEBUG_CROP = true;
    static final float MAX_PARALLAX = 1.0f;
    static final int REMOVE = 2;
    private static final java.lang.String TAG = com.android.server.wallpaper.WallpaperCropper.class.getSimpleName();
    private final com.android.server.wallpaper.WallpaperDisplayHelper mWallpaperDisplayHelper;
    private com.android.server.wallpaper.IWallpaperManagerServiceExt mWallpaperManagerServiceExt;

    public interface WallpaperCropUtils {
        android.graphics.Rect getCrop(android.graphics.Point point, android.graphics.Point point2, android.util.SparseArray<android.graphics.Rect> sparseArray, boolean z);
    }

    WallpaperCropper(com.android.server.wallpaper.WallpaperDisplayHelper wallpaperDisplayHelper, com.android.server.wallpaper.IWallpaperManagerServiceExt wallpaperManagerServiceExt) {
        this.mWallpaperDisplayHelper = wallpaperDisplayHelper;
        this.mWallpaperManagerServiceExt = wallpaperManagerServiceExt;
    }

    public android.graphics.Rect getCrop(android.graphics.Point displaySize, android.graphics.Point bitmapSize, android.util.SparseArray<android.graphics.Rect> suggestedCrops, boolean rtl) {
        android.graphics.Rect adjustedCrop;
        int orientation = android.app.WallpaperManager.getOrientation(displaySize);
        int i = 0;
        if (suggestedCrops == null || suggestedCrops.size() == 0) {
            android.graphics.Rect crop = new android.graphics.Rect(0, 0, bitmapSize.x, bitmapSize.y);
            int unfoldedOrientation = this.mWallpaperDisplayHelper.getUnfoldedOrientation(orientation);
            if (unfoldedOrientation != -1) {
                android.util.SparseArray<android.graphics.Rect> newSuggestedCrops = new android.util.SparseArray<>();
                newSuggestedCrops.put(unfoldedOrientation, crop);
                return getCrop(displaySize, bitmapSize, newSuggestedCrops, rtl);
            }
            if (this.mWallpaperDisplayHelper.isLargeScreen() && !this.mWallpaperDisplayHelper.isFoldable()) {
                i = 1;
            }
            if (i != 0 && displaySize.x < displaySize.y) {
                android.graphics.Point rotatedDisplaySize = new android.graphics.Point(displaySize.y, displaySize.x);
                android.graphics.Rect landscapeCrop = noParallax(getCrop(rotatedDisplaySize, bitmapSize, suggestedCrops, rtl), rotatedDisplaySize, bitmapSize, rtl);
                crop = getAdjustedCrop(landscapeCrop, bitmapSize, displaySize, false, rtl, 1);
                if (rtl) {
                    crop.left = landscapeCrop.left;
                } else {
                    crop.right = landscapeCrop.right;
                }
            }
            return getAdjustedCrop(crop, bitmapSize, displaySize, true, rtl, 1);
        }
        for (int i2 = 0; i2 < suggestedCrops.size(); i2++) {
            android.graphics.Rect testCrop = suggestedCrops.valueAt(i2);
            if (testCrop == null || testCrop.left < 0 || testCrop.top < 0 || testCrop.right > bitmapSize.x || testCrop.bottom > bitmapSize.y) {
                android.util.Slog.w(TAG, "invalid crop: " + testCrop + " for bitmap size: " + bitmapSize);
                return getCrop(displaySize, bitmapSize, new android.util.SparseArray<>(), rtl);
            }
        }
        android.graphics.Rect suggestedCrop = suggestedCrops.get(orientation);
        if (suggestedCrop != null) {
            return getAdjustedCrop(suggestedCrop, bitmapSize, displaySize, true, rtl, 1);
        }
        android.util.SparseArray<android.graphics.Point> defaultDisplaySizes = this.mWallpaperDisplayHelper.getDefaultDisplaySizes();
        int rotatedOrientation = android.app.WallpaperManager.getRotatedOrientation(orientation);
        android.graphics.Rect suggestedCrop2 = suggestedCrops.get(rotatedOrientation);
        android.graphics.Point suggestedDisplaySize = defaultDisplaySizes.get(rotatedOrientation);
        if (suggestedCrop2 != null) {
            return getAdjustedCrop(noParallax(suggestedCrop2, suggestedDisplaySize, bitmapSize, rtl), bitmapSize, displaySize, false, rtl, 3);
        }
        int unfoldedOrientation2 = this.mWallpaperDisplayHelper.getUnfoldedOrientation(orientation);
        android.graphics.Rect suggestedCrop3 = suggestedCrops.get(unfoldedOrientation2);
        android.graphics.Point suggestedDisplaySize2 = defaultDisplaySizes.get(unfoldedOrientation2);
        android.graphics.Point suggestedDisplaySize3 = suggestedDisplaySize2;
        if (suggestedCrop3 == null) {
            int foldedOrientation = this.mWallpaperDisplayHelper.getFoldedOrientation(orientation);
            android.graphics.Rect suggestedCrop4 = suggestedCrops.get(foldedOrientation);
            android.graphics.Point suggestedDisplaySize4 = defaultDisplaySizes.get(foldedOrientation);
            android.graphics.Point suggestedDisplaySize5 = suggestedDisplaySize4;
            if (suggestedCrop4 != null) {
                return getAdjustedCrop(noParallax(suggestedCrop4, suggestedDisplaySize5, bitmapSize, rtl), bitmapSize, displaySize, false, rtl, 1);
            }
            android.graphics.Point rotatedDisplaySize2 = defaultDisplaySizes.get(rotatedOrientation);
            if (rotatedDisplaySize2 != null) {
                int rotatedFolded = this.mWallpaperDisplayHelper.getFoldedOrientation(rotatedOrientation);
                int rotateUnfolded = this.mWallpaperDisplayHelper.getUnfoldedOrientation(rotatedOrientation);
                int[] iArr = {rotatedFolded, rotateUnfolded};
                while (i < 2) {
                    int suggestedOrientation = iArr[i];
                    if (suggestedCrops.get(suggestedOrientation) == null) {
                        i++;
                    } else {
                        android.graphics.Rect rotatedCrop = getCrop(rotatedDisplaySize2, bitmapSize, suggestedCrops, rtl);
                        android.util.SparseArray<android.graphics.Rect> rotatedCropMap = new android.util.SparseArray<>();
                        rotatedCropMap.put(rotatedOrientation, rotatedCrop);
                        return getCrop(displaySize, bitmapSize, rotatedCropMap, rtl);
                    }
                }
            }
            android.util.Slog.w(TAG, "Could not find a proper default crop for display: " + displaySize + ", bitmap size: " + bitmapSize + ", suggested crops: " + suggestedCrops + ", orientation: " + orientation + ", rtl: " + rtl + ", defaultDisplaySizes: " + defaultDisplaySizes);
            return getCrop(displaySize, bitmapSize, new android.util.SparseArray<>(), rtl);
        }
        android.graphics.Rect adjustedCrop2 = noParallax(suggestedCrop3, suggestedDisplaySize3, bitmapSize, rtl);
        android.graphics.Rect res = getAdjustedCrop(adjustedCrop2, bitmapSize, displaySize, false, rtl, 2);
        if (res.width() < adjustedCrop2.width()) {
            if (rtl) {
                adjustedCrop = adjustedCrop2;
                res.left = java.lang.Math.min(res.left, adjustedCrop.left);
            } else {
                adjustedCrop = adjustedCrop2;
                res.right = java.lang.Math.max(res.right, adjustedCrop.right);
            }
            return getAdjustedCrop(res, bitmapSize, displaySize, true, rtl, 1);
        }
        return res;
    }

    static android.graphics.Rect noParallax(android.graphics.Rect crop, android.graphics.Point displaySize, android.graphics.Point bitmapSize, boolean rtl) {
        if (displaySize == null) {
            return crop;
        }
        android.graphics.Rect adjustedCrop = getAdjustedCrop(crop, bitmapSize, displaySize, true, rtl, 1);
        float suggestedDisplayRatio = (displaySize.x * 1.0f) / displaySize.y;
        int widthToRemove = (int) ((adjustedCrop.width() - (adjustedCrop.height() * suggestedDisplayRatio)) + 0.5f);
        if (rtl) {
            adjustedCrop.left += widthToRemove;
        } else {
            adjustedCrop.right -= widthToRemove;
        }
        return adjustedCrop;
    }

    static android.graphics.Rect getAdjustedCrop(android.graphics.Rect crop, android.graphics.Point bitmapSize, android.graphics.Point screenSize, boolean parallax, boolean rtl, int mode) {
        int widthToAdd;
        android.graphics.Rect adjustedCrop = new android.graphics.Rect(crop);
        float cropRatio = crop.width() / crop.height();
        float screenRatio = screenSize.x / screenSize.y;
        if (cropRatio == screenRatio) {
            return crop;
        }
        if (cropRatio > screenRatio) {
            if (!parallax) {
                int newLeft = bitmapSize.y - crop.bottom;
                int newRight = newLeft + crop.height();
                int newTop = crop.left;
                int newBottom = newTop + crop.width();
                android.graphics.Rect rotatedCrop = new android.graphics.Rect(newLeft, newTop, newRight, newBottom);
                android.graphics.Point rotatedBitmap = new android.graphics.Point(bitmapSize.y, bitmapSize.x);
                android.graphics.Point rotatedScreen = new android.graphics.Point(screenSize.y, screenSize.x);
                android.graphics.Rect rect = getAdjustedCrop(rotatedCrop, rotatedBitmap, rotatedScreen, false, rtl, mode);
                int resultLeft = rect.top;
                int resultRight = rect.height() + resultLeft;
                int resultTop = rotatedBitmap.x - rect.right;
                int resultBottom = rect.width() + resultTop;
                return new android.graphics.Rect(resultLeft, resultTop, resultRight, resultBottom);
            }
            float additionalWidthForParallax = (cropRatio / screenRatio) - 1.0f;
            if (additionalWidthForParallax > 1.0f) {
                int widthToRemove = (int) java.lang.Math.ceil((additionalWidthForParallax - 1.0f) * screenRatio * crop.height());
                if (rtl) {
                    adjustedCrop.left += widthToRemove;
                } else {
                    adjustedCrop.right -= widthToRemove;
                }
            }
        } else {
            if (mode == 2) {
                widthToAdd = 0;
            } else {
                widthToAdd = mode == 1 ? (int) ((crop.height() * screenRatio) - crop.width()) : (int) (((double) (-crop.width())) + java.lang.Math.sqrt(crop.width() * crop.height() * screenRatio));
            }
            int availableWidth = bitmapSize.x - crop.width();
            if (availableWidth >= widthToAdd) {
                int widthToAddLeft = widthToAdd / 2;
                int widthToAddRight = (widthToAdd / 2) + (widthToAdd % 2);
                if (crop.left >= widthToAddLeft) {
                    if (bitmapSize.x - crop.right < widthToAddRight) {
                        widthToAddLeft += widthToAddRight - (bitmapSize.x - crop.right);
                        widthToAddRight = bitmapSize.x - crop.right;
                    }
                } else {
                    widthToAddRight += widthToAddLeft - crop.left;
                    widthToAddLeft = crop.left;
                }
                adjustedCrop.left -= widthToAddLeft;
                adjustedCrop.right += widthToAddRight;
            } else {
                adjustedCrop.left = 0;
                adjustedCrop.right = bitmapSize.x;
            }
            int heightToRemove = (int) (crop.height() - (adjustedCrop.width() / screenRatio));
            adjustedCrop.top += (heightToRemove / 2) + (heightToRemove % 2);
            adjustedCrop.bottom -= heightToRemove / 2;
        }
        return adjustedCrop;
    }

    public static android.graphics.Rect getTotalCrop(android.util.SparseArray<android.graphics.Rect> crops) {
        int left = Integer.MAX_VALUE;
        int top = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int bottom = Integer.MIN_VALUE;
        for (int i = 0; i < crops.size(); i++) {
            android.graphics.Rect rect = crops.valueAt(i);
            left = java.lang.Math.min(left, rect.left);
            top = java.lang.Math.min(top, rect.top);
            right = java.lang.Math.max(right, rect.right);
            bottom = java.lang.Math.max(bottom, rect.bottom);
        }
        return new android.graphics.Rect(left, top, right, bottom);
    }

    android.util.SparseArray<android.graphics.Rect> getRelativeCropHints(com.android.server.wallpaper.WallpaperData wallpaper) {
        android.util.SparseArray<android.graphics.Rect> result = new android.util.SparseArray<>();
        for (int i = 0; i < wallpaper.mCropHints.size(); i++) {
            android.graphics.Rect adjustedRect = new android.graphics.Rect(wallpaper.mCropHints.valueAt(i));
            adjustedRect.offset(-wallpaper.cropHint.left, -wallpaper.cropHint.top);
            adjustedRect.scale(1.0f / wallpaper.mSampleSize);
            result.put(wallpaper.mCropHints.keyAt(i), adjustedRect);
        }
        return result;
    }

    static java.util.List<android.graphics.Rect> getOriginalCropHints(com.android.server.wallpaper.WallpaperData wallpaper, java.util.List<android.graphics.Rect> relativeCropHints) {
        java.util.List<android.graphics.Rect> result = new java.util.ArrayList<>();
        for (android.graphics.Rect crop : relativeCropHints) {
            android.graphics.Rect originalRect = new android.graphics.Rect(crop);
            originalRect.scale(wallpaper.mSampleSize);
            originalRect.offset(wallpaper.cropHint.left, wallpaper.cropHint.top);
            result.add(originalRect);
        }
        return result;
    }

    android.util.SparseArray<android.graphics.Rect> getDefaultCrops(android.util.SparseArray<android.graphics.Rect> suggestedCrops, android.graphics.Point bitmapSize) {
        android.graphics.Rect cropHint = suggestedCrops.get(-1);
        if (cropHint != null) {
            android.graphics.Rect bitmapRect = new android.graphics.Rect(0, 0, bitmapSize.x, bitmapSize.y);
            if (suggestedCrops.size() != 1 || !bitmapRect.contains(cropHint)) {
                android.util.Slog.w(TAG, "Couldn't get default crops from suggested crops " + suggestedCrops + " for bitmap of size " + bitmapSize + "; ignoring suggested crops");
                return getDefaultCrops(new android.util.SparseArray<>(), bitmapSize);
            }
            android.graphics.Point cropSize = new android.graphics.Point(cropHint.width(), cropHint.height());
            android.util.SparseArray<android.graphics.Rect> relativeDefaultCrops = getDefaultCrops(new android.util.SparseArray<>(), cropSize);
            for (int i = 0; i < relativeDefaultCrops.size(); i++) {
                relativeDefaultCrops.valueAt(i).offset(cropHint.left, cropHint.top);
            }
            return relativeDefaultCrops;
        }
        android.util.SparseArray<android.graphics.Point> defaultDisplaySizes = this.mWallpaperDisplayHelper.getDefaultDisplaySizes();
        boolean rtl = android.text.TextUtils.getLayoutDirectionFromLocale(java.util.Locale.getDefault()) == 1;
        android.util.SparseArray<android.graphics.Rect> adjustedSuggestedCrops = new android.util.SparseArray<>();
        for (int i2 = 0; i2 < defaultDisplaySizes.size(); i2++) {
            int orientation = defaultDisplaySizes.keyAt(i2);
            android.graphics.Point displaySize = defaultDisplaySizes.valueAt(i2);
            android.graphics.Rect suggestedCrop = suggestedCrops.get(orientation);
            if (suggestedCrop != null) {
                adjustedSuggestedCrops.put(orientation, getCrop(displaySize, bitmapSize, suggestedCrops, rtl));
            }
        }
        android.util.SparseArray<android.graphics.Rect> result = adjustedSuggestedCrops.clone();
        for (int i3 = 0; i3 < defaultDisplaySizes.size(); i3++) {
            int orientation2 = defaultDisplaySizes.keyAt(i3);
            if (!result.contains(orientation2)) {
                android.graphics.Point displaySize2 = defaultDisplaySizes.valueAt(i3);
                android.graphics.Rect newCrop = getCrop(displaySize2, bitmapSize, adjustedSuggestedCrops, rtl);
                result.put(orientation2, newCrop);
            }
        }
        return result;
    }

    void generateCrop(com.android.server.wallpaper.WallpaperData wallpaper) throws java.lang.Throwable {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog(TAG);
        t.traceBegin("WPMS.generateCrop");
        generateCropInternal(wallpaper);
        t.traceEnd();
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x03ec  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x040a  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0477 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:133:0x04d1  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x04e7 A[Catch: all -> 0x09e7, Exception -> 0x09fb, LOOP:3: B:135:0x04e3->B:137:0x04e7, LOOP_END, TryCatch #5 {all -> 0x09e7, blocks: (B:134:0x04da, B:135:0x04e3, B:137:0x04e7, B:138:0x04ea, B:140:0x04ed, B:150:0x0537, B:149:0x0534), top: B:308:0x04da }] */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0530  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0557  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x064c  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x065a A[Catch: all -> 0x0663, Exception -> 0x0672, TRY_ENTER, TRY_LEAVE, TryCatch #35 {Exception -> 0x0672, all -> 0x0663, blocks: (B:172:0x05c8, B:187:0x065a, B:196:0x0691), top: B:319:0x05c8 }] */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0682 A[Catch: all -> 0x09b4, Exception -> 0x09c3, TRY_ENTER, TryCatch #32 {Exception -> 0x09c3, all -> 0x09b4, blocks: (B:184:0x0652, B:194:0x068a, B:199:0x06a2, B:198:0x069a, B:193:0x0682), top: B:325:0x0652 }] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0691 A[Catch: all -> 0x0663, Exception -> 0x0672, TRY_ENTER, TRY_LEAVE, TryCatch #35 {Exception -> 0x0672, all -> 0x0663, blocks: (B:172:0x05c8, B:187:0x065a, B:196:0x0691), top: B:319:0x05c8 }] */
    /* JADX WARN: Removed duplicated region for block: B:198:0x069a A[Catch: all -> 0x09b4, Exception -> 0x09c3, TRY_ENTER, TryCatch #32 {Exception -> 0x09c3, all -> 0x09b4, blocks: (B:184:0x0652, B:194:0x068a, B:199:0x06a2, B:198:0x069a, B:193:0x0682), top: B:325:0x0652 }] */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0760  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x076a A[Catch: all -> 0x0722, Exception -> 0x0730, TRY_ENTER, TRY_LEAVE, TryCatch #37 {Exception -> 0x0730, all -> 0x0722, blocks: (B:206:0x06bd, B:219:0x076a), top: B:315:0x06bd }] */
    /* JADX WARN: Removed duplicated region for block: B:222:0x07e5  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x07e8  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x089a A[Catch: all -> 0x0955, Exception -> 0x095a, TRY_ENTER, TryCatch #31 {Exception -> 0x095a, all -> 0x0955, blocks: (B:229:0x0873, B:238:0x089a, B:242:0x08a7, B:245:0x08b0, B:247:0x08b4, B:241:0x08a2), top: B:327:0x0873 }] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x08a0  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x08a2 A[Catch: all -> 0x0955, Exception -> 0x095a, TryCatch #31 {Exception -> 0x095a, all -> 0x0955, blocks: (B:229:0x0873, B:238:0x089a, B:242:0x08a7, B:245:0x08b0, B:247:0x08b4, B:241:0x08a2), top: B:327:0x0873 }] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x08ae A[Catch: all -> 0x0890, Exception -> 0x0895, TRY_ENTER, TRY_LEAVE, TryCatch #22 {Exception -> 0x0895, all -> 0x0890, blocks: (B:232:0x0885, B:244:0x08ae), top: B:345:0x0881 }] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x08b4 A[Catch: all -> 0x0955, Exception -> 0x095a, TRY_LEAVE, TryCatch #31 {Exception -> 0x095a, all -> 0x0955, blocks: (B:229:0x0873, B:238:0x089a, B:242:0x08a7, B:245:0x08b0, B:247:0x08b4, B:241:0x08a2), top: B:327:0x0873 }] */
    /* JADX WARN: Removed duplicated region for block: B:250:0x090c  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0a3b  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x0a61  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x04fa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:341:0x06b9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:356:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void generateCropInternal(com.android.server.wallpaper.WallpaperData r42) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2699
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wallpaper.WallpaperCropper.generateCropInternal(com.android.server.wallpaper.WallpaperData):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateCropInternal$0(int finalScale, android.graphics.BitmapFactory.Options options, int rescaledBitmapWidth, int rescaledBitmapHeight, android.graphics.Rect estimateCrop, android.graphics.ImageDecoder decoder, android.graphics.ImageDecoder.ImageInfo info, android.graphics.ImageDecoder.Source src) {
        if (!com.android.window.flags.Flags.multiCrop() && !this.mWallpaperManagerServiceExt.setDecoderSampleSize(decoder, finalScale, options)) {
            decoder.setTargetSampleSize(finalScale);
        }
        if (com.android.window.flags.Flags.multiCrop()) {
            decoder.setTargetSize(rescaledBitmapWidth, rescaledBitmapHeight);
        }
        decoder.setCrop(estimateCrop);
    }
}
