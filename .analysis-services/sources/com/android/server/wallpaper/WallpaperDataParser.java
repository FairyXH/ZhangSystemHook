package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
public class WallpaperDataParser {
    private static final java.lang.String TAG = com.android.server.wallpaper.WallpaperDataParser.class.getSimpleName();
    private final android.content.Context mContext;
    private final android.content.ComponentName mImageWallpaper;
    private final com.android.server.wallpaper.WallpaperCropper mWallpaperCropper;
    private final com.android.server.wallpaper.WallpaperDisplayHelper mWallpaperDisplayHelper;
    private com.android.server.wallpaper.IWallpaperManagerServiceExt mWallpaperManagerServiceExt;

    WallpaperDataParser(android.content.Context context, com.android.server.wallpaper.WallpaperDisplayHelper wallpaperDisplayHelper, com.android.server.wallpaper.WallpaperCropper wallpaperCropper, com.android.server.wallpaper.IWallpaperManagerServiceExt wallpaperManagerServiceExt) {
        this.mContext = context;
        this.mWallpaperDisplayHelper = wallpaperDisplayHelper;
        this.mWallpaperCropper = wallpaperCropper;
        this.mImageWallpaper = android.content.ComponentName.unflattenFromString(context.getResources().getString(android.R.string.imProtocolAim));
        this.mWallpaperManagerServiceExt = wallpaperManagerServiceExt;
    }

    private com.android.internal.util.JournaledFile makeJournaledFile(int userId) {
        int phyDisplayId = this.mWallpaperManagerServiceExt.getCachePhysicalDisplayId();
        java.lang.String base = new java.io.File(this.mWallpaperManagerServiceExt.getWallpaperDir(userId, phyDisplayId, com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(userId)), "wallpaper_info.xml").getAbsolutePath();
        return new com.android.internal.util.JournaledFile(new java.io.File(base), new java.io.File(base + ".tmp"));
    }

    static class WallpaperLoadingResult {
        private final com.android.server.wallpaper.WallpaperData mLockWallpaperData;
        private final boolean mSuccess;
        private final com.android.server.wallpaper.WallpaperData mSystemWallpaperData;

        private WallpaperLoadingResult(com.android.server.wallpaper.WallpaperData systemWallpaperData, com.android.server.wallpaper.WallpaperData lockWallpaperData, boolean success) {
            this.mSystemWallpaperData = systemWallpaperData;
            this.mLockWallpaperData = lockWallpaperData;
            this.mSuccess = success;
        }

        public com.android.server.wallpaper.WallpaperData getSystemWallpaperData() {
            return this.mSystemWallpaperData;
        }

        public com.android.server.wallpaper.WallpaperData getLockWallpaperData() {
            return this.mLockWallpaperData;
        }

        public boolean success() {
            return this.mSuccess;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:182:0x0475  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x04c4  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x04d3  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0185 A[Catch: IndexOutOfBoundsException -> 0x022f, IOException -> 0x0236, XmlPullParserException -> 0x023f, NumberFormatException -> 0x0248, NullPointerException -> 0x0251, FileNotFoundException -> 0x025a, TryCatch #16 {FileNotFoundException -> 0x025a, IOException -> 0x0236, IndexOutOfBoundsException -> 0x022f, NullPointerException -> 0x0251, NumberFormatException -> 0x0248, XmlPullParserException -> 0x023f, blocks: (B:43:0x00b0, B:71:0x0140, B:75:0x014a, B:77:0x0150, B:78:0x0153, B:80:0x015c, B:82:0x0162, B:84:0x0168, B:88:0x017b, B:89:0x017f, B:91:0x0185, B:92:0x0188, B:94:0x018c), top: B:209:0x00b0 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x018c A[Catch: IndexOutOfBoundsException -> 0x022f, IOException -> 0x0236, XmlPullParserException -> 0x023f, NumberFormatException -> 0x0248, NullPointerException -> 0x0251, FileNotFoundException -> 0x025a, TRY_LEAVE, TryCatch #16 {FileNotFoundException -> 0x025a, IOException -> 0x0236, IndexOutOfBoundsException -> 0x022f, NullPointerException -> 0x0251, NumberFormatException -> 0x0248, XmlPullParserException -> 0x023f, blocks: (B:43:0x00b0, B:71:0x0140, B:75:0x014a, B:77:0x0150, B:78:0x0153, B:80:0x015c, B:82:0x0162, B:84:0x0168, B:88:0x017b, B:89:0x017f, B:91:0x0185, B:92:0x0188, B:94:0x018c), top: B:209:0x00b0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.android.server.wallpaper.WallpaperDataParser.WallpaperLoadingResult loadSettingsLocked(int r25, boolean r26, boolean r27, int r28) {
        /*
            Method dump skipped, instruction units count: 1244
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wallpaper.WallpaperDataParser.loadSettingsLocked(int, boolean, boolean, int):com.android.server.wallpaper.WallpaperDataParser$WallpaperLoadingResult");
    }

    private void ensureSaneWallpaperData(com.android.server.wallpaper.WallpaperData wallpaper) {
        if (wallpaper.cropHint.width() < 0 || wallpaper.cropHint.height() < 0) {
            wallpaper.cropHint.set(0, 0, 0, 0);
        }
    }

    private void migrateFromOld() {
        java.io.File preNWallpaper = new java.io.File(com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(0), com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER);
        java.io.File originalWallpaper = new java.io.File("/data/data/com.android.settings/files/wallpaper");
        java.io.File newWallpaper = new java.io.File(com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(0), "wallpaper_orig");
        if (preNWallpaper.exists()) {
            if (!newWallpaper.exists()) {
                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                    android.util.Slog.i(TAG, "Migrating wallpaper schema");
                }
                android.os.FileUtils.copyFile(preNWallpaper, newWallpaper);
                return;
            }
            return;
        }
        if (originalWallpaper.exists()) {
            if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                android.util.Slog.i(TAG, "Migrating antique wallpaper schema");
            }
            java.io.File oldInfo = new java.io.File("/data/system/wallpaper_info.xml");
            if (oldInfo.exists()) {
                java.io.File newInfo = new java.io.File(com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(0), "wallpaper_info.xml");
                oldInfo.renameTo(newInfo);
            }
            android.os.FileUtils.copyFile(originalWallpaper, preNWallpaper);
            originalWallpaper.renameTo(newWallpaper);
        }
    }

    void parseWallpaperAttributes(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.wallpaper.WallpaperData wallpaper, boolean keepDimensionHints) throws org.xmlpull.v1.XmlPullParserException {
        com.android.server.wallpaper.WallpaperData.BindSource bindSource;
        int id = parser.getAttributeInt((java.lang.String) null, "id", -1);
        if (id != -1) {
            wallpaper.wallpaperId = id;
            if (id > com.android.server.wallpaper.WallpaperUtils.getCurrentWallpaperId()) {
                com.android.server.wallpaper.WallpaperUtils.setCurrentWallpaperId(id);
            }
        } else {
            wallpaper.wallpaperId = com.android.server.wallpaper.WallpaperUtils.makeWallpaperIdLocked();
        }
        java.lang.String str = "cropLeft";
        int i = 0;
        java.lang.String str2 = "cropTop";
        android.graphics.Rect legacyCropHint = new android.graphics.Rect(getAttributeInt(parser, "cropLeft", 0), getAttributeInt(parser, "cropTop", 0), getAttributeInt(parser, "cropRight", 0), getAttributeInt(parser, "cropBottom", 0));
        android.graphics.Rect totalCropHint = new android.graphics.Rect(getAttributeInt(parser, "totalCropLeft", 0), getAttributeInt(parser, "totalCropTop", 0), getAttributeInt(parser, "totalCropRight", 0), getAttributeInt(parser, "totalCropBottom", 0));
        if (com.android.window.flags.Flags.multiCrop() && this.mImageWallpaper.equals(wallpaper.nextWallpaperComponent)) {
            wallpaper.mCropHints = new android.util.SparseArray<>();
            for (android.util.Pair<java.lang.Integer, java.lang.String> pair : screenDimensionPairs()) {
                java.lang.String str3 = str;
                int id2 = id;
                java.lang.String str4 = str2;
                android.graphics.Rect cropHint = new android.graphics.Rect(parser.getAttributeInt((java.lang.String) null, str + ((java.lang.String) pair.second), i), parser.getAttributeInt((java.lang.String) null, str2 + ((java.lang.String) pair.second), 0), parser.getAttributeInt((java.lang.String) null, "cropRight" + ((java.lang.String) pair.second), 0), parser.getAttributeInt((java.lang.String) null, "cropBottom" + ((java.lang.String) pair.second), 0));
                if (!cropHint.isEmpty()) {
                    wallpaper.mCropHints.put(((java.lang.Integer) pair.first).intValue(), cropHint);
                }
                if (!cropHint.isEmpty() && cropHint.equals(legacyCropHint)) {
                    wallpaper.mOrientationWhenSet = ((java.lang.Integer) pair.first).intValue();
                }
                str = str3;
                id = id2;
                str2 = str4;
                i = 0;
            }
            if (wallpaper.mCropHints.size() == 0 && totalCropHint.isEmpty()) {
                if (!legacyCropHint.isEmpty()) {
                    wallpaper.cropHint.set(legacyCropHint);
                }
            } else {
                wallpaper.cropHint.set(totalCropHint);
            }
            wallpaper.mSampleSize = parser.getAttributeFloat((java.lang.String) null, "sampleSize", 1.0f);
        } else if (!com.android.window.flags.Flags.multiCrop()) {
            wallpaper.cropHint.set(legacyCropHint);
        }
        com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpData = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(0);
        if (!keepDimensionHints && !com.android.window.flags.Flags.multiCrop()) {
            wpData.mWidth = parser.getAttributeInt((java.lang.String) null, "width", 0);
            wpData.mHeight = parser.getAttributeInt((java.lang.String) null, "height", 0);
        }
        if (!com.android.window.flags.Flags.multiCrop()) {
            wpData.mPadding.left = getAttributeInt(parser, "paddingLeft", 0);
            wpData.mPadding.top = getAttributeInt(parser, "paddingTop", 0);
            wpData.mPadding.right = getAttributeInt(parser, "paddingRight", 0);
            wpData.mPadding.bottom = getAttributeInt(parser, "paddingBottom", 0);
        }
        wallpaper.mWallpaperDimAmount = getAttributeFloat(parser, "dimAmount", 0.0f);
        try {
            bindSource = (com.android.server.wallpaper.WallpaperData.BindSource) java.lang.Enum.valueOf(com.android.server.wallpaper.WallpaperData.BindSource.class, getAttributeString(parser, "bindSource", com.android.server.wallpaper.WallpaperData.BindSource.UNKNOWN.name()));
        } catch (java.lang.IllegalArgumentException | java.lang.NullPointerException e) {
            bindSource = com.android.server.wallpaper.WallpaperData.BindSource.UNKNOWN;
        }
        wallpaper.mBindSource = bindSource;
        int dimAmountsCount = getAttributeInt(parser, "dimAmountsCount", 0);
        if (dimAmountsCount > 0) {
            android.util.SparseArray<java.lang.Float> allDimAmounts = new android.util.SparseArray<>(dimAmountsCount);
            for (int i2 = 0; i2 < dimAmountsCount; i2++) {
                int uid = getAttributeInt(parser, "dimUID" + i2, 0);
                float dimValue = getAttributeFloat(parser, "dimValue" + i2, 0.0f);
                allDimAmounts.put(uid, java.lang.Float.valueOf(dimValue));
            }
            wallpaper.mUidToDimAmount = allDimAmounts;
        }
        int colorsCount = getAttributeInt(parser, "colorsCount", 0);
        int allColorsCount = getAttributeInt(parser, "allColorsCount", 0);
        if (allColorsCount > 0) {
            java.util.Map<java.lang.Integer, java.lang.Integer> allColors = new java.util.HashMap<>(allColorsCount);
            int i3 = 0;
            while (i3 < allColorsCount) {
                int colorInt = getAttributeInt(parser, "allColorsValue" + i3, 0);
                int population = getAttributeInt(parser, "allColorsPopulation" + i3, 0);
                allColors.put(java.lang.Integer.valueOf(colorInt), java.lang.Integer.valueOf(population));
                i3++;
                bindSource = bindSource;
            }
            int colorHints = getAttributeInt(parser, "colorHints", 0);
            wallpaper.primaryColors = new android.app.WallpaperColors(allColors, colorHints);
        } else if (colorsCount > 0) {
            android.graphics.Color primary = null;
            android.graphics.Color secondary = null;
            android.graphics.Color tertiary = null;
            for (int i4 = 0; i4 < colorsCount; i4++) {
                android.graphics.Color color = android.graphics.Color.valueOf(getAttributeInt(parser, "colorValue" + i4, 0));
                if (i4 == 0) {
                    primary = color;
                } else if (i4 == 1) {
                    secondary = color;
                } else if (i4 != 2) {
                    break;
                } else {
                    tertiary = color;
                }
            }
            int colorHints2 = getAttributeInt(parser, "colorHints", 0);
            wallpaper.primaryColors = new android.app.WallpaperColors(primary, secondary, tertiary, colorHints2);
        }
        wallpaper.name = parser.getAttributeValue((java.lang.String) null, "name");
        if (wallpaper.name == null) {
            wallpaper.name = "";
        }
        wallpaper.allowBackup = parser.getAttributeBoolean((java.lang.String) null, com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP, false);
    }

    private static int getAttributeInt(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String name, int defValue) {
        return parser.getAttributeInt((java.lang.String) null, name, defValue);
    }

    private static float getAttributeFloat(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String name, float defValue) {
        return parser.getAttributeFloat((java.lang.String) null, name, defValue);
    }

    private java.lang.String getAttributeString(org.xmlpull.v1.XmlPullParser parser, java.lang.String name, java.lang.String defValue) {
        java.lang.String s = parser.getAttributeValue(null, name);
        return s != null ? s : defValue;
    }

    void saveSettingsLocked(int userId, com.android.server.wallpaper.WallpaperData wallpaper, com.android.server.wallpaper.WallpaperData lockWallpaper) {
        com.android.internal.util.JournaledFile journal = makeJournaledFile(userId);
        java.io.FileOutputStream fstream = null;
        try {
            fstream = new java.io.FileOutputStream(journal.chooseForWrite(), false);
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fstream);
            out.startDocument((java.lang.String) null, true);
            if (wallpaper != null) {
                writeWallpaperAttributes(out, "wp", wallpaper);
            }
            if (lockWallpaper != null) {
                writeWallpaperAttributes(out, "kwp", lockWallpaper);
            }
            out.endDocument();
            fstream.flush();
            android.os.FileUtils.sync(fstream);
            fstream.close();
            journal.commit();
        } catch (java.io.IOException e) {
            libcore.io.IoUtils.closeQuietly(fstream);
            journal.rollback();
        }
    }

    void writeWallpaperAttributes(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, com.android.server.wallpaper.WallpaperData wallpaper) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        int unfoldedOrientation;
        if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
            android.util.Slog.v(TAG, "writeWallpaperAttributes id=" + wallpaper.wallpaperId);
        }
        out.startTag((java.lang.String) null, tag);
        out.attributeInt((java.lang.String) null, "id", wallpaper.wallpaperId);
        if (com.android.window.flags.Flags.multiCrop() && this.mImageWallpaper.equals(wallpaper.wallpaperComponent)) {
            if (wallpaper.mCropHints == null) {
                android.util.Slog.e(TAG, "cropHints should not be null when saved");
                wallpaper.mCropHints = new android.util.SparseArray<>();
            }
            android.graphics.Rect rectToPutInLegacyCrop = new android.graphics.Rect(wallpaper.cropHint);
            for (android.util.Pair<java.lang.Integer, java.lang.String> pair : screenDimensionPairs()) {
                android.graphics.Rect cropHint = wallpaper.mCropHints.get(((java.lang.Integer) pair.first).intValue());
                if (cropHint != null) {
                    out.attributeInt((java.lang.String) null, "cropLeft" + ((java.lang.String) pair.second), cropHint.left);
                    out.attributeInt((java.lang.String) null, "cropTop" + ((java.lang.String) pair.second), cropHint.top);
                    out.attributeInt((java.lang.String) null, "cropRight" + ((java.lang.String) pair.second), cropHint.right);
                    out.attributeInt((java.lang.String) null, "cropBottom" + ((java.lang.String) pair.second), cropHint.bottom);
                    int orientationToPutInLegacyCrop = wallpaper.mOrientationWhenSet;
                    if (this.mWallpaperDisplayHelper.isFoldable() && (unfoldedOrientation = this.mWallpaperDisplayHelper.getUnfoldedOrientation(orientationToPutInLegacyCrop)) != -1) {
                        orientationToPutInLegacyCrop = unfoldedOrientation;
                    }
                    if (((java.lang.Integer) pair.first).intValue() == orientationToPutInLegacyCrop) {
                        rectToPutInLegacyCrop.set(cropHint);
                    }
                }
            }
            out.attributeInt((java.lang.String) null, "cropLeft", rectToPutInLegacyCrop.left);
            out.attributeInt((java.lang.String) null, "cropTop", rectToPutInLegacyCrop.top);
            out.attributeInt((java.lang.String) null, "cropRight", rectToPutInLegacyCrop.right);
            out.attributeInt((java.lang.String) null, "cropBottom", rectToPutInLegacyCrop.bottom);
            out.attributeInt((java.lang.String) null, "totalCropLeft", wallpaper.cropHint.left);
            out.attributeInt((java.lang.String) null, "totalCropTop", wallpaper.cropHint.top);
            out.attributeInt((java.lang.String) null, "totalCropRight", wallpaper.cropHint.right);
            out.attributeInt((java.lang.String) null, "totalCropBottom", wallpaper.cropHint.bottom);
            out.attributeFloat((java.lang.String) null, "sampleSize", wallpaper.mSampleSize);
        } else if (!com.android.window.flags.Flags.multiCrop()) {
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(0);
            out.attributeInt((java.lang.String) null, "width", wpdData.mWidth);
            out.attributeInt((java.lang.String) null, "height", wpdData.mHeight);
            out.attributeInt((java.lang.String) null, "cropLeft", wallpaper.cropHint.left);
            out.attributeInt((java.lang.String) null, "cropTop", wallpaper.cropHint.top);
            out.attributeInt((java.lang.String) null, "cropRight", wallpaper.cropHint.right);
            out.attributeInt((java.lang.String) null, "cropBottom", wallpaper.cropHint.bottom);
            if (wpdData.mPadding.left != 0) {
                out.attributeInt((java.lang.String) null, "paddingLeft", wpdData.mPadding.left);
            }
            if (wpdData.mPadding.top != 0) {
                out.attributeInt((java.lang.String) null, "paddingTop", wpdData.mPadding.top);
            }
            if (wpdData.mPadding.right != 0) {
                out.attributeInt((java.lang.String) null, "paddingRight", wpdData.mPadding.right);
            }
            if (wpdData.mPadding.bottom != 0) {
                out.attributeInt((java.lang.String) null, "paddingBottom", wpdData.mPadding.bottom);
            }
        }
        out.attributeFloat((java.lang.String) null, "dimAmount", wallpaper.mWallpaperDimAmount);
        out.attribute((java.lang.String) null, "bindSource", wallpaper.mBindSource.name());
        int dimAmountsCount = wallpaper.mUidToDimAmount.size();
        out.attributeInt((java.lang.String) null, "dimAmountsCount", dimAmountsCount);
        if (dimAmountsCount > 0) {
            int index = 0;
            for (int i = 0; i < wallpaper.mUidToDimAmount.size(); i++) {
                out.attributeInt((java.lang.String) null, "dimUID" + index, wallpaper.mUidToDimAmount.keyAt(i));
                out.attributeFloat((java.lang.String) null, "dimValue" + index, wallpaper.mUidToDimAmount.valueAt(i).floatValue());
                index++;
            }
        }
        if (wallpaper.primaryColors != null) {
            int colorsCount = wallpaper.primaryColors.getMainColors().size();
            out.attributeInt((java.lang.String) null, "colorsCount", colorsCount);
            if (colorsCount > 0) {
                for (int i2 = 0; i2 < colorsCount; i2++) {
                    android.graphics.Color wc = (android.graphics.Color) wallpaper.primaryColors.getMainColors().get(i2);
                    out.attributeInt((java.lang.String) null, "colorValue" + i2, wc.toArgb());
                }
            }
            int allColorsCount = wallpaper.primaryColors.getAllColors().size();
            out.attributeInt((java.lang.String) null, "allColorsCount", allColorsCount);
            if (allColorsCount > 0) {
                int index2 = 0;
                for (java.util.Map.Entry<java.lang.Integer, java.lang.Integer> entry : wallpaper.primaryColors.getAllColors().entrySet()) {
                    out.attributeInt((java.lang.String) null, "allColorsValue" + index2, entry.getKey().intValue());
                    out.attributeInt((java.lang.String) null, "allColorsPopulation" + index2, entry.getValue().intValue());
                    index2++;
                }
            }
            out.attributeInt((java.lang.String) null, "colorHints", wallpaper.primaryColors.getColorHints());
        }
        out.attribute((java.lang.String) null, "name", wallpaper.name);
        if (wallpaper.wallpaperComponent != null && !wallpaper.wallpaperComponent.equals(this.mImageWallpaper)) {
            out.attribute((java.lang.String) null, "component", wallpaper.wallpaperComponent.flattenToShortString());
        }
        if (wallpaper.allowBackup) {
            out.attributeBoolean((java.lang.String) null, com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP, true);
        }
        out.endTag((java.lang.String) null, tag);
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0142  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean restoreNamedResourceLocked(com.android.server.wallpaper.WallpaperData r19) {
        /*
            Method dump skipped, instruction units count: 439
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wallpaper.WallpaperDataParser.restoreNamedResourceLocked(com.android.server.wallpaper.WallpaperData):boolean");
    }

    private static java.util.List<android.util.Pair<java.lang.Integer, java.lang.String>> screenDimensionPairs() {
        return java.util.List.of(new android.util.Pair(0, "Portrait"), new android.util.Pair(1, "Landscape"), new android.util.Pair(2, "SquarePortrait"), new android.util.Pair(3, "SquareLandscape"));
    }
}
