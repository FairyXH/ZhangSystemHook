package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
public class GameManagerSettings {
    private static final java.lang.String ATTR_FPS = "fps";
    private static final java.lang.String ATTR_GAME_MODE = "gameMode";
    private static final java.lang.String ATTR_LOADING_BOOST_DURATION = "loadingBoost";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_SCALING = "scaling";
    private static final java.lang.String ATTR_USE_ANGLE = "useAngle";
    private static final java.lang.String GAME_SERVICE_FILE_NAME = "game-manager-service.xml";
    public static final java.lang.String TAG = "GameManagerService_GameManagerSettings";
    private static final java.lang.String TAG_GAME_MODE_CONFIG = "gameModeConfig";
    private static final java.lang.String TAG_PACKAGE = "package";
    private static final java.lang.String TAG_PACKAGES = "packages";
    final android.util.AtomicFile mSettingsFile;
    private final java.io.File mSystemDir;
    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mGameModes = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, com.android.server.app.GameManagerService.GamePackageConfiguration> mConfigOverrides = new android.util.ArrayMap<>();

    GameManagerSettings(java.io.File dataDir) {
        this.mSystemDir = new java.io.File(dataDir, "system");
        this.mSystemDir.mkdirs();
        android.os.FileUtils.setPermissions(this.mSystemDir.toString(), 509, -1, -1);
        this.mSettingsFile = new android.util.AtomicFile(new java.io.File(this.mSystemDir, GAME_SERVICE_FILE_NAME));
    }

    int getGameModeLocked(java.lang.String packageName) {
        int gameMode;
        if (!this.mGameModes.containsKey(packageName) || (gameMode = this.mGameModes.get(packageName).intValue()) == 0) {
            return 1;
        }
        return gameMode;
    }

    void setGameModeLocked(java.lang.String packageName, int gameMode) {
        this.mGameModes.put(packageName, java.lang.Integer.valueOf(gameMode));
    }

    void removeGame(java.lang.String packageName) {
        this.mGameModes.remove(packageName);
        this.mConfigOverrides.remove(packageName);
    }

    com.android.server.app.GameManagerService.GamePackageConfiguration getConfigOverride(java.lang.String packageName) {
        return this.mConfigOverrides.get(packageName);
    }

    void setConfigOverride(java.lang.String packageName, com.android.server.app.GameManagerService.GamePackageConfiguration configOverride) {
        this.mConfigOverrides.put(packageName, configOverride);
    }

    void removeConfigOverride(java.lang.String packageName) {
        this.mConfigOverrides.remove(packageName);
    }

    void writePersistentDataLocked() {
        java.io.FileOutputStream fstr = null;
        try {
            fstr = this.mSettingsFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(fstr);
            serializer.startDocument((java.lang.String) null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag((java.lang.String) null, TAG_PACKAGES);
            android.util.ArraySet<java.lang.String> packageNames = new android.util.ArraySet<>(this.mGameModes.keySet());
            packageNames.addAll(this.mConfigOverrides.keySet());
            for (java.lang.String packageName : packageNames) {
                serializer.startTag((java.lang.String) null, "package");
                serializer.attribute((java.lang.String) null, "name", packageName);
                if (this.mGameModes.containsKey(packageName)) {
                    serializer.attributeInt((java.lang.String) null, ATTR_GAME_MODE, this.mGameModes.get(packageName).intValue());
                }
                writeGameModeConfigTags(serializer, this.mConfigOverrides.get(packageName));
                serializer.endTag((java.lang.String) null, "package");
            }
            serializer.endTag((java.lang.String) null, TAG_PACKAGES);
            serializer.endDocument();
            this.mSettingsFile.finishWrite(fstr);
            android.os.FileUtils.setPermissions(this.mSettingsFile.toString(), com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTION_SERVICE_RESTARTED, -1, -1);
        } catch (java.io.IOException e) {
            this.mSettingsFile.failWrite(fstr);
            android.util.Slog.wtf(TAG, "Unable to write game manager service settings, current changes will be lost at reboot", e);
        }
    }

    private void writeGameModeConfigTags(com.android.modules.utils.TypedXmlSerializer serializer, com.android.server.app.GameManagerService.GamePackageConfiguration config) throws java.io.IOException {
        if (config == null) {
            return;
        }
        int[] gameModes = config.getAvailableGameModes();
        for (int mode : gameModes) {
            com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration modeConfig = config.getGameModeConfiguration(mode);
            if (modeConfig != null) {
                serializer.startTag((java.lang.String) null, TAG_GAME_MODE_CONFIG);
                serializer.attributeInt((java.lang.String) null, ATTR_GAME_MODE, mode);
                serializer.attributeBoolean((java.lang.String) null, "useAngle", modeConfig.getUseAngle());
                serializer.attribute((java.lang.String) null, "fps", modeConfig.getFpsStr());
                serializer.attributeFloat((java.lang.String) null, ATTR_SCALING, modeConfig.getScaling());
                serializer.attributeInt((java.lang.String) null, "loadingBoost", modeConfig.getLoadingBoostDuration());
                serializer.endTag((java.lang.String) null, TAG_GAME_MODE_CONFIG);
            }
        }
    }

    boolean readPersistentDataLocked() {
        int type;
        this.mGameModes.clear();
        if (!this.mSettingsFile.exists()) {
            android.util.Slog.v(TAG, "Settings file doesn't exist, skip reading");
            return false;
        }
        try {
            java.io.FileInputStream str = this.mSettingsFile.openRead();
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(str);
            do {
                type = parser.next();
                if (type == 2) {
                    break;
                }
            } while (type != 1);
            if (type != 2) {
                android.util.Slog.wtf(TAG, "No start tag found in game manager settings");
                return false;
            }
            int outerDepth = parser.getDepth();
            while (true) {
                int type2 = parser.next();
                if (type2 == 1 || (type2 == 3 && parser.getDepth() <= outerDepth)) {
                    break;
                }
                if (type2 != 3 && type2 != 4) {
                    java.lang.String tagName = parser.getName();
                    if (type2 == 2 && "package".equals(tagName)) {
                        readPackage(parser);
                    } else {
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        android.util.Slog.w(TAG, "Unknown element under packages tag: " + tagName + " with type: " + type2);
                    }
                }
            }
            str.close();
            return true;
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.wtf(TAG, "Error reading game manager settings", e);
            return false;
        }
    }

    private void readPackage(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
        if (name == null) {
            android.util.Slog.wtf(TAG, "No package name found in package tag");
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            return;
        }
        try {
            int gameMode = parser.getAttributeInt((java.lang.String) null, ATTR_GAME_MODE);
            this.mGameModes.put(name, java.lang.Integer.valueOf(gameMode));
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.v(TAG, "No game mode selected by user for package" + name);
        }
        int packageTagDepth = parser.getDepth();
        com.android.server.app.GameManagerService.GamePackageConfiguration config = new com.android.server.app.GameManagerService.GamePackageConfiguration(name);
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= packageTagDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tagName = parser.getName();
                if (type == 2 && TAG_GAME_MODE_CONFIG.equals(tagName)) {
                    readGameModeConfig(parser, config);
                } else {
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    android.util.Slog.w(TAG, "Unknown element under package tag: " + tagName + " with type: " + type);
                }
            }
        }
        if (config.hasActiveGameModeConfig()) {
            this.mConfigOverrides.put(name, config);
        }
    }

    private void readGameModeConfig(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.app.GameManagerService.GamePackageConfiguration config) {
        try {
            int gameMode = parser.getAttributeInt((java.lang.String) null, ATTR_GAME_MODE);
            com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration modeConfig = config.getOrAddDefaultGameModeConfiguration(gameMode);
            try {
                float scaling = parser.getAttributeFloat((java.lang.String) null, ATTR_SCALING);
                modeConfig.setScaling(scaling);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                java.lang.String rawScaling = parser.getAttributeValue((java.lang.String) null, ATTR_SCALING);
                if (rawScaling != null) {
                    android.util.Slog.wtf(TAG, "Invalid scaling value in config tag: " + rawScaling, e);
                }
            }
            java.lang.String fps = parser.getAttributeValue((java.lang.String) null, "fps");
            modeConfig.setFpsStr(fps != null ? fps : "");
            try {
                boolean useAngle = parser.getAttributeBoolean((java.lang.String) null, "useAngle");
                modeConfig.setUseAngle(useAngle);
            } catch (org.xmlpull.v1.XmlPullParserException e2) {
                java.lang.String rawUseAngle = parser.getAttributeValue((java.lang.String) null, "useAngle");
                if (rawUseAngle != null) {
                    android.util.Slog.wtf(TAG, "Invalid useAngle value in config tag: " + rawUseAngle, e2);
                }
            }
            try {
                int loadingBoostDuration = parser.getAttributeInt((java.lang.String) null, "loadingBoost");
                modeConfig.setLoadingBoostDuration(loadingBoostDuration);
            } catch (org.xmlpull.v1.XmlPullParserException e3) {
                java.lang.String rawLoadingBoost = parser.getAttributeValue((java.lang.String) null, "loadingBoost");
                if (rawLoadingBoost != null) {
                    android.util.Slog.wtf(TAG, "Invalid loading boost in config tag: " + rawLoadingBoost, e3);
                }
            }
        } catch (org.xmlpull.v1.XmlPullParserException e4) {
            android.util.Slog.wtf(TAG, "Invalid game mode value in config tag: " + parser.getAttributeValue((java.lang.String) null, ATTR_GAME_MODE), e4);
        }
    }
}
