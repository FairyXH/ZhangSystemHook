package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DisplayWindowSettingsProvider implements com.android.server.wm.DisplayWindowSettings.SettingsProvider {
    private static final java.lang.String DATA_DISPLAY_SETTINGS_FILE_PATH = "system/display_settings.xml";
    private static final int IDENTIFIER_PORT = 1;
    private static final int IDENTIFIER_UNIQUE_ID = 0;
    private static final java.lang.String TAG = "WindowManager";
    private static final java.lang.String VENDOR_DISPLAY_SETTINGS_FILE_PATH = "etc/display_settings.xml";
    private static final java.lang.String WM_DISPLAY_COMMIT_TAG = "wm-displays";
    private com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettings mBaseSettings;
    private final com.android.server.wm.DisplayWindowSettingsProvider.WritableSettings mOverrideSettings;

    @interface DisplayIdentifierType {
    }

    interface ReadableSettingsStorage {
        java.io.InputStream openRead() throws java.io.IOException;
    }

    interface WritableSettingsStorage extends com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettingsStorage {
        void finishWrite(java.io.OutputStream outputStream, boolean z);

        java.io.OutputStream startWrite() throws java.io.IOException;
    }

    DisplayWindowSettingsProvider() {
        this(new com.android.server.wm.DisplayWindowSettingsProvider.AtomicFileStorage(getVendorSettingsFile()), new com.android.server.wm.DisplayWindowSettingsProvider.AtomicFileStorage(getOverrideSettingsFile()));
    }

    DisplayWindowSettingsProvider(com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettingsStorage baseSettingsStorage, com.android.server.wm.DisplayWindowSettingsProvider.WritableSettingsStorage overrideSettingsStorage) {
        this.mBaseSettings = new com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettings(baseSettingsStorage);
        this.mOverrideSettings = new com.android.server.wm.DisplayWindowSettingsProvider.WritableSettings(overrideSettingsStorage);
    }

    void setBaseSettingsFilePath(java.lang.String path) {
        android.util.AtomicFile settingsFile;
        java.io.File file = path != null ? new java.io.File(path) : null;
        if (file != null && file.exists()) {
            settingsFile = new android.util.AtomicFile(file, WM_DISPLAY_COMMIT_TAG);
        } else {
            android.util.Slog.w(TAG, "display settings " + path + " does not exist, using vendor defaults");
            settingsFile = getVendorSettingsFile();
        }
        setBaseSettingsStorage(new com.android.server.wm.DisplayWindowSettingsProvider.AtomicFileStorage(settingsFile));
    }

    void setBaseSettingsStorage(com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettingsStorage baseSettingsStorage) {
        this.mBaseSettings = new com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettings(baseSettingsStorage);
    }

    @Override // com.android.server.wm.DisplayWindowSettings.SettingsProvider
    public com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry getSettings(android.view.DisplayInfo info) {
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry baseSettings = this.mBaseSettings.getSettingsEntry(info);
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mOverrideSettings.getOrCreateSettingsEntry(info);
        if (baseSettings == null) {
            return new com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry(overrideSettings);
        }
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry mergedSettings = new com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry(baseSettings);
        mergedSettings.updateFrom(overrideSettings);
        return mergedSettings;
    }

    @Override // com.android.server.wm.DisplayWindowSettings.SettingsProvider
    public com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry getOverrideSettings(android.view.DisplayInfo info) {
        return new com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry(this.mOverrideSettings.getOrCreateSettingsEntry(info));
    }

    @Override // com.android.server.wm.DisplayWindowSettings.SettingsProvider
    public void updateOverrideSettings(android.view.DisplayInfo info, com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrides) {
        this.mOverrideSettings.updateSettingsEntry(info, overrides);
    }

    @Override // com.android.server.wm.DisplayWindowSettings.SettingsProvider
    public void onDisplayRemoved(android.view.DisplayInfo info) {
        this.mOverrideSettings.onDisplayRemoved(info);
    }

    @Override // com.android.server.wm.DisplayWindowSettings.SettingsProvider
    public void clearDisplaySettings(android.view.DisplayInfo info) {
        this.mOverrideSettings.clearDisplaySettings(info);
    }

    int getOverrideSettingsSize() {
        return this.mOverrideSettings.mSettings.size();
    }

    private static class ReadableSettings {
        protected int mIdentifierType;
        protected final android.util.ArrayMap<java.lang.String, com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry> mSettings = new android.util.ArrayMap<>();

        ReadableSettings(com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettingsStorage settingsStorage) {
            loadSettings(settingsStorage);
        }

        final com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry getSettingsEntry(android.view.DisplayInfo info) {
            java.lang.String identifier = getIdentifier(info);
            com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettings.get(identifier);
            if (settings != null) {
                return settings;
            }
            com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings2 = this.mSettings.get(info.name);
            if (settings2 != null) {
                this.mSettings.remove(info.name);
                this.mSettings.put(identifier, settings2);
                return settings2;
            }
            return null;
        }

        protected final java.lang.String getIdentifier(android.view.DisplayInfo displayInfo) {
            if (this.mIdentifierType == 1 && displayInfo.address != null && (displayInfo.address instanceof android.view.DisplayAddress.Physical)) {
                return "port:" + displayInfo.address.getPort();
            }
            return displayInfo.uniqueId;
        }

        private void loadSettings(com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettingsStorage settingsStorage) {
            com.android.server.wm.DisplayWindowSettingsProvider.FileData fileData = com.android.server.wm.DisplayWindowSettingsProvider.readSettings(settingsStorage);
            if (fileData != null) {
                this.mIdentifierType = fileData.mIdentifierType;
                this.mSettings.putAll(fileData.mSettings);
            }
        }
    }

    private static final class WritableSettings extends com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettings {
        private final com.android.server.wm.DisplayWindowSettingsProvider.WritableSettingsStorage mSettingsStorage;
        private final android.util.ArraySet<java.lang.String> mVirtualDisplayIdentifiers;

        WritableSettings(com.android.server.wm.DisplayWindowSettingsProvider.WritableSettingsStorage settingsStorage) {
            super(settingsStorage);
            this.mVirtualDisplayIdentifiers = new android.util.ArraySet<>();
            this.mSettingsStorage = settingsStorage;
        }

        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry getOrCreateSettingsEntry(android.view.DisplayInfo info) {
            java.lang.String identifier = getIdentifier(info);
            com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettings.get(identifier);
            if (settings != null) {
                return settings;
            }
            com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings2 = this.mSettings.get(info.name);
            if (settings2 != null) {
                this.mSettings.remove(info.name);
                this.mSettings.put(identifier, settings2);
                writeSettings();
                return settings2;
            }
            com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings3 = new com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry();
            this.mSettings.put(identifier, settings3);
            if (info.type == 5) {
                this.mVirtualDisplayIdentifiers.add(identifier);
            }
            return settings3;
        }

        void updateSettingsEntry(android.view.DisplayInfo info, com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings) {
            com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = getOrCreateSettingsEntry(info);
            boolean changed = overrideSettings.setTo(settings);
            if (changed && info.type != 5) {
                writeSettings();
            }
        }

        void onDisplayRemoved(android.view.DisplayInfo info) {
            java.lang.String identifier = getIdentifier(info);
            if (!this.mSettings.containsKey(identifier)) {
                return;
            }
            if (this.mVirtualDisplayIdentifiers.remove(identifier) || this.mSettings.get(identifier).isEmpty()) {
                this.mSettings.remove(identifier);
            }
        }

        void clearDisplaySettings(android.view.DisplayInfo info) {
            java.lang.String identifier = getIdentifier(info);
            this.mSettings.remove(identifier);
            this.mVirtualDisplayIdentifiers.remove(identifier);
        }

        private void writeSettings() {
            com.android.server.wm.DisplayWindowSettingsProvider.FileData fileData = new com.android.server.wm.DisplayWindowSettingsProvider.FileData();
            fileData.mIdentifierType = this.mIdentifierType;
            int size = this.mSettings.size();
            for (int i = 0; i < size; i++) {
                java.lang.String identifier = this.mSettings.keyAt(i);
                if (!this.mVirtualDisplayIdentifiers.contains(identifier)) {
                    fileData.mSettings.put(identifier, this.mSettings.get(identifier));
                }
            }
            com.android.server.wm.DisplayWindowSettingsProvider.writeSettings(this.mSettingsStorage, fileData);
        }
    }

    private static android.util.AtomicFile getVendorSettingsFile() {
        java.io.File vendorFile = new java.io.File(android.os.Environment.getProductDirectory(), VENDOR_DISPLAY_SETTINGS_FILE_PATH);
        if (!vendorFile.exists()) {
            vendorFile = new java.io.File(android.os.Environment.getVendorDirectory(), VENDOR_DISPLAY_SETTINGS_FILE_PATH);
        }
        return new android.util.AtomicFile(vendorFile, WM_DISPLAY_COMMIT_TAG);
    }

    private static android.util.AtomicFile getOverrideSettingsFile() {
        java.io.File overrideSettingsFile = new java.io.File(android.os.Environment.getDataDirectory(), DATA_DISPLAY_SETTINGS_FILE_PATH);
        return new android.util.AtomicFile(overrideSettingsFile, WM_DISPLAY_COMMIT_TAG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.android.server.wm.DisplayWindowSettingsProvider.FileData readSettings(com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettingsStorage storage) {
        com.android.modules.utils.TypedXmlPullParser parser;
        int type;
        try {
            java.io.InputStream stream = storage.openRead();
            com.android.server.wm.DisplayWindowSettingsProvider.FileData fileData = new com.android.server.wm.DisplayWindowSettingsProvider.FileData();
            boolean success = false;
            try {
                try {
                    try {
                        try {
                            try {
                                parser = android.util.Xml.resolvePullParser(stream);
                                do {
                                    type = parser.next();
                                    if (type == 2) {
                                        break;
                                    }
                                } while (type != 1);
                            } catch (java.lang.Throwable th) {
                                try {
                                    stream.close();
                                } catch (java.io.IOException e) {
                                }
                                throw th;
                            }
                        } catch (java.lang.IndexOutOfBoundsException e2) {
                            android.util.Slog.w(TAG, "Failed parsing " + e2);
                            stream.close();
                        } catch (java.lang.NumberFormatException e3) {
                            android.util.Slog.w(TAG, "Failed parsing " + e3);
                            stream.close();
                        }
                    } catch (java.io.IOException e4) {
                        android.util.Slog.w(TAG, "Failed parsing " + e4);
                        stream.close();
                    } catch (org.xmlpull.v1.XmlPullParserException e5) {
                        android.util.Slog.w(TAG, "Failed parsing " + e5);
                        stream.close();
                    }
                } catch (java.lang.IllegalStateException e6) {
                    android.util.Slog.w(TAG, "Failed parsing " + e6);
                    stream.close();
                } catch (java.lang.NullPointerException e7) {
                    android.util.Slog.w(TAG, "Failed parsing " + e7);
                    stream.close();
                }
            } catch (java.io.IOException e8) {
            }
            if (type != 2) {
                throw new java.lang.IllegalStateException("no start tag found");
            }
            int outerDepth = parser.getDepth();
            while (true) {
                int type2 = parser.next();
                if (type2 == 1 || (type2 == 3 && parser.getDepth() <= outerDepth)) {
                    break;
                }
                if (type2 != 3 && type2 != 4) {
                    java.lang.String tagName = parser.getName();
                    if (tagName.equals("display")) {
                        readDisplay(parser, fileData);
                    } else if (tagName.equals("config")) {
                        readConfig(parser, fileData);
                    } else {
                        android.util.Slog.w(TAG, "Unknown element under <display-settings>: " + parser.getName());
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
            success = true;
            stream.close();
            if (!success) {
                fileData.mSettings.clear();
            }
            return fileData;
        } catch (java.io.IOException e9) {
            android.util.Slog.i(TAG, "No existing display settings, starting empty");
            return null;
        }
    }

    private static int getIntAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String name, int defaultValue) {
        return parser.getAttributeInt((java.lang.String) null, name, defaultValue);
    }

    private static java.lang.Integer getIntegerAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String name, java.lang.Integer defaultValue) {
        try {
            return java.lang.Integer.valueOf(parser.getAttributeInt((java.lang.String) null, name));
        } catch (java.lang.Exception e) {
            return defaultValue;
        }
    }

    private static java.lang.Boolean getBooleanAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String name, java.lang.Boolean defaultValue) {
        try {
            return java.lang.Boolean.valueOf(parser.getAttributeBoolean((java.lang.String) null, name));
        } catch (java.lang.Exception e) {
            return defaultValue;
        }
    }

    private static void readDisplay(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.wm.DisplayWindowSettingsProvider.FileData fileData) throws org.xmlpull.v1.XmlPullParserException, java.lang.NumberFormatException, java.io.IOException {
        java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
        if (name != null) {
            com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settingsEntry = new com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry();
            settingsEntry.mWindowingMode = getIntAttribute(parser, "windowingMode", 0);
            settingsEntry.mUserRotationMode = getIntegerAttribute(parser, "userRotationMode", null);
            settingsEntry.mUserRotation = getIntegerAttribute(parser, "userRotation", null);
            settingsEntry.mForcedWidth = getIntAttribute(parser, "forcedWidth", 0);
            settingsEntry.mForcedHeight = getIntAttribute(parser, "forcedHeight", 0);
            settingsEntry.mForcedDensity = getIntAttribute(parser, "forcedDensity", 0);
            settingsEntry.mForcedScalingMode = getIntegerAttribute(parser, "forcedScalingMode", null);
            settingsEntry.mRemoveContentMode = getIntAttribute(parser, "removeContentMode", 0);
            settingsEntry.mShouldShowWithInsecureKeyguard = getBooleanAttribute(parser, "shouldShowWithInsecureKeyguard", null);
            settingsEntry.mShouldShowSystemDecors = getBooleanAttribute(parser, "shouldShowSystemDecors", null);
            java.lang.Boolean shouldShowIme = getBooleanAttribute(parser, "shouldShowIme", null);
            if (shouldShowIme != null) {
                settingsEntry.mImePolicy = java.lang.Integer.valueOf(shouldShowIme.booleanValue() ? 0 : 1);
            } else {
                settingsEntry.mImePolicy = getIntegerAttribute(parser, "imePolicy", null);
            }
            settingsEntry.mFixedToUserRotation = getIntegerAttribute(parser, "fixedToUserRotation", null);
            settingsEntry.mIgnoreOrientationRequest = getBooleanAttribute(parser, "ignoreOrientationRequest", null);
            settingsEntry.mIgnoreDisplayCutout = getBooleanAttribute(parser, "ignoreDisplayCutout", null);
            settingsEntry.mDontMoveToTop = getBooleanAttribute(parser, "dontMoveToTop", null);
            fileData.mSettings.put(name, settingsEntry);
        }
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
    }

    private static void readConfig(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.wm.DisplayWindowSettingsProvider.FileData fileData) throws org.xmlpull.v1.XmlPullParserException, java.lang.NumberFormatException, java.io.IOException {
        fileData.mIdentifierType = getIntAttribute(parser, "identifier", 0);
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void writeSettings(com.android.server.wm.DisplayWindowSettingsProvider.WritableSettingsStorage storage, com.android.server.wm.DisplayWindowSettingsProvider.FileData data) {
        try {
            java.io.OutputStream stream = storage.startWrite();
            boolean success = false;
            try {
                try {
                    com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
                    out.startDocument((java.lang.String) null, true);
                    out.startTag((java.lang.String) null, "display-settings");
                    out.startTag((java.lang.String) null, "config");
                    out.attributeInt((java.lang.String) null, "identifier", data.mIdentifierType);
                    out.endTag((java.lang.String) null, "config");
                    for (java.util.Map.Entry<java.lang.String, com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry> entry : data.mSettings.entrySet()) {
                        java.lang.String displayIdentifier = entry.getKey();
                        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settingsEntry = entry.getValue();
                        if (!settingsEntry.isEmpty()) {
                            out.startTag((java.lang.String) null, "display");
                            out.attribute((java.lang.String) null, "name", displayIdentifier);
                            if (settingsEntry.mWindowingMode != 0) {
                                out.attributeInt((java.lang.String) null, "windowingMode", settingsEntry.mWindowingMode);
                            }
                            if (settingsEntry.mUserRotationMode != null) {
                                out.attributeInt((java.lang.String) null, "userRotationMode", settingsEntry.mUserRotationMode.intValue());
                            }
                            if (settingsEntry.mUserRotation != null) {
                                out.attributeInt((java.lang.String) null, "userRotation", settingsEntry.mUserRotation.intValue());
                            }
                            if (settingsEntry.mForcedWidth != 0 && settingsEntry.mForcedHeight != 0) {
                                out.attributeInt((java.lang.String) null, "forcedWidth", settingsEntry.mForcedWidth);
                                out.attributeInt((java.lang.String) null, "forcedHeight", settingsEntry.mForcedHeight);
                            }
                            if (settingsEntry.mForcedDensity != 0) {
                                out.attributeInt((java.lang.String) null, "forcedDensity", settingsEntry.mForcedDensity);
                            }
                            if (settingsEntry.mForcedScalingMode != null) {
                                out.attributeInt((java.lang.String) null, "forcedScalingMode", settingsEntry.mForcedScalingMode.intValue());
                            }
                            if (settingsEntry.mRemoveContentMode != 0) {
                                out.attributeInt((java.lang.String) null, "removeContentMode", settingsEntry.mRemoveContentMode);
                            }
                            if (settingsEntry.mShouldShowWithInsecureKeyguard != null) {
                                out.attributeBoolean((java.lang.String) null, "shouldShowWithInsecureKeyguard", settingsEntry.mShouldShowWithInsecureKeyguard.booleanValue());
                            }
                            if (settingsEntry.mShouldShowSystemDecors != null) {
                                out.attributeBoolean((java.lang.String) null, "shouldShowSystemDecors", settingsEntry.mShouldShowSystemDecors.booleanValue());
                            }
                            if (settingsEntry.mImePolicy != null) {
                                out.attributeInt((java.lang.String) null, "imePolicy", settingsEntry.mImePolicy.intValue());
                            }
                            if (settingsEntry.mFixedToUserRotation != null) {
                                out.attributeInt((java.lang.String) null, "fixedToUserRotation", settingsEntry.mFixedToUserRotation.intValue());
                            }
                            if (settingsEntry.mIgnoreOrientationRequest != null) {
                                out.attributeBoolean((java.lang.String) null, "ignoreOrientationRequest", settingsEntry.mIgnoreOrientationRequest.booleanValue());
                            }
                            if (settingsEntry.mIgnoreDisplayCutout != null) {
                                out.attributeBoolean((java.lang.String) null, "ignoreDisplayCutout", settingsEntry.mIgnoreDisplayCutout.booleanValue());
                            }
                            if (settingsEntry.mDontMoveToTop != null) {
                                out.attributeBoolean((java.lang.String) null, "dontMoveToTop", settingsEntry.mDontMoveToTop.booleanValue());
                            }
                            out.endTag((java.lang.String) null, "display");
                        }
                    }
                    out.endTag((java.lang.String) null, "display-settings");
                    out.endDocument();
                    success = true;
                } catch (java.io.IOException e) {
                    android.util.Slog.w(TAG, "Failed to write display window settings.", e);
                }
            } finally {
                storage.finishWrite(stream, false);
            }
        } catch (java.io.IOException e2) {
            android.util.Slog.w(TAG, "Failed to write display settings: " + e2);
        }
    }

    private static final class FileData {
        int mIdentifierType;
        final java.util.Map<java.lang.String, com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry> mSettings;

        private FileData() {
            this.mSettings = new android.util.ArrayMap();
        }

        public java.lang.String toString() {
            return "FileData{mIdentifierType=" + this.mIdentifierType + ", mSettings=" + this.mSettings + '}';
        }
    }

    private static final class AtomicFileStorage implements com.android.server.wm.DisplayWindowSettingsProvider.WritableSettingsStorage {
        private final android.util.AtomicFile mAtomicFile;

        AtomicFileStorage(android.util.AtomicFile atomicFile) {
            this.mAtomicFile = atomicFile;
        }

        @Override // com.android.server.wm.DisplayWindowSettingsProvider.ReadableSettingsStorage
        public java.io.InputStream openRead() throws java.io.FileNotFoundException {
            return this.mAtomicFile.openRead();
        }

        @Override // com.android.server.wm.DisplayWindowSettingsProvider.WritableSettingsStorage
        public java.io.OutputStream startWrite() throws java.io.IOException {
            return this.mAtomicFile.startWrite();
        }

        @Override // com.android.server.wm.DisplayWindowSettingsProvider.WritableSettingsStorage
        public void finishWrite(java.io.OutputStream os, boolean success) {
            if (!(os instanceof java.io.FileOutputStream)) {
                throw new java.lang.IllegalArgumentException("Unexpected OutputStream as argument: " + os);
            }
            java.io.FileOutputStream fos = (java.io.FileOutputStream) os;
            if (success) {
                this.mAtomicFile.finishWrite(fos);
            } else {
                this.mAtomicFile.failWrite(fos);
            }
        }
    }
}
