package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class PersistentDataStore {
    private static final java.lang.String ATTR_DEVICE_ADDRESS = "deviceAddress";
    private static final java.lang.String ATTR_DEVICE_ALIAS = "deviceAlias";
    private static final java.lang.String ATTR_DEVICE_NAME = "deviceName";
    private static final java.lang.String ATTR_PACKAGE_NAME = "package-name";
    private static final java.lang.String ATTR_TIME_STAMP = "timestamp";
    private static final java.lang.String ATTR_UNIQUE_ID = "unique-id";
    private static final java.lang.String ATTR_USER_SERIAL = "user-serial";
    public static final int DEFAULT_USER_ID = -1;
    static final java.lang.String TAG = "DisplayManager.PersistentDataStore";
    private static final java.lang.String TAG_BRIGHTNESS_CONFIGURATION = "brightness-configuration";
    private static final java.lang.String TAG_BRIGHTNESS_CONFIGURATIONS = "brightness-configurations";
    private static final java.lang.String TAG_BRIGHTNESS_NITS_FOR_DEFAULT_DISPLAY = "brightness-nits-for-default-display";
    private static final java.lang.String TAG_BRIGHTNESS_VALUE = "brightness-value";
    private static final java.lang.String TAG_COLOR_MODE = "color-mode";
    private static final java.lang.String TAG_DISPLAY = "display";
    private static final java.lang.String TAG_DISPLAY_MANAGER_STATE = "display-manager-state";
    private static final java.lang.String TAG_DISPLAY_STATES = "display-states";
    private static final java.lang.String TAG_REFRESH_RATE = "refresh-rate";
    private static final java.lang.String TAG_REMEMBERED_WIFI_DISPLAYS = "remembered-wifi-displays";
    private static final java.lang.String TAG_RESOLUTION_HEIGHT = "resolution-height";
    private static final java.lang.String TAG_RESOLUTION_WIDTH = "resolution-width";
    private static final java.lang.String TAG_STABLE_DEVICE_VALUES = "stable-device-values";
    private static final java.lang.String TAG_STABLE_DISPLAY_HEIGHT = "stable-display-height";
    private static final java.lang.String TAG_STABLE_DISPLAY_WIDTH = "stable-display-width";
    private static final java.lang.String TAG_WIFI_DISPLAY = "wifi-display";
    private float mBrightnessNitsForDefaultDisplay;
    private boolean mDirty;
    private final java.util.HashMap<java.lang.String, com.android.server.display.PersistentDataStore.DisplayState> mDisplayStates;
    private final java.lang.Object mFileAccessLock;
    private com.android.server.display.PersistentDataStore.BrightnessConfigurations mGlobalBrightnessConfigurations;
    private final android.os.Handler mHandler;
    private com.android.server.display.PersistentDataStore.Injector mInjector;
    private boolean mLoaded;
    private java.util.ArrayList<android.hardware.display.WifiDisplay> mRememberedWifiDisplays;
    private final com.android.server.display.PersistentDataStore.StableDeviceValues mStableDeviceValues;

    public PersistentDataStore() {
        this(new com.android.server.display.PersistentDataStore.Injector());
    }

    PersistentDataStore(com.android.server.display.PersistentDataStore.Injector injector) {
        this(injector, new android.os.Handler(com.android.internal.os.BackgroundThread.getHandler().getLooper()));
    }

    PersistentDataStore(com.android.server.display.PersistentDataStore.Injector injector, android.os.Handler handler) {
        this.mRememberedWifiDisplays = new java.util.ArrayList<>();
        this.mDisplayStates = new java.util.HashMap<>();
        this.mBrightnessNitsForDefaultDisplay = -1.0f;
        this.mStableDeviceValues = new com.android.server.display.PersistentDataStore.StableDeviceValues();
        this.mGlobalBrightnessConfigurations = new com.android.server.display.PersistentDataStore.BrightnessConfigurations();
        this.mFileAccessLock = new java.lang.Object();
        this.mInjector = injector;
        this.mHandler = handler;
    }

    public void saveIfNeeded() {
        if (this.mDirty) {
            save();
            this.mDirty = false;
        }
    }

    public android.hardware.display.WifiDisplay getRememberedWifiDisplay(java.lang.String deviceAddress) {
        loadIfNeeded();
        int index = findRememberedWifiDisplay(deviceAddress);
        if (index >= 0) {
            return this.mRememberedWifiDisplays.get(index);
        }
        return null;
    }

    public android.hardware.display.WifiDisplay[] getRememberedWifiDisplays() {
        loadIfNeeded();
        return (android.hardware.display.WifiDisplay[]) this.mRememberedWifiDisplays.toArray(new android.hardware.display.WifiDisplay[this.mRememberedWifiDisplays.size()]);
    }

    public android.hardware.display.WifiDisplay applyWifiDisplayAlias(android.hardware.display.WifiDisplay display) {
        if (display != null) {
            loadIfNeeded();
            java.lang.String alias = null;
            int index = findRememberedWifiDisplay(display.getDeviceAddress());
            if (index >= 0) {
                alias = this.mRememberedWifiDisplays.get(index).getDeviceAlias();
            }
            if (!java.util.Objects.equals(display.getDeviceAlias(), alias)) {
                return new android.hardware.display.WifiDisplay(display.getDeviceAddress(), display.getDeviceName(), alias, display.isAvailable(), display.canConnect(), display.isRemembered());
            }
        }
        return display;
    }

    public android.hardware.display.WifiDisplay[] applyWifiDisplayAliases(android.hardware.display.WifiDisplay[] displays) {
        android.hardware.display.WifiDisplay[] results = displays;
        if (results != null) {
            int count = displays.length;
            for (int i = 0; i < count; i++) {
                android.hardware.display.WifiDisplay result = applyWifiDisplayAlias(displays[i]);
                if (result != displays[i]) {
                    if (results == displays) {
                        results = new android.hardware.display.WifiDisplay[count];
                        java.lang.System.arraycopy(displays, 0, results, 0, count);
                    }
                    results[i] = result;
                }
            }
        }
        return results;
    }

    public boolean rememberWifiDisplay(android.hardware.display.WifiDisplay display) {
        loadIfNeeded();
        int index = findRememberedWifiDisplay(display.getDeviceAddress());
        if (index >= 0) {
            android.hardware.display.WifiDisplay other = this.mRememberedWifiDisplays.get(index);
            if (other.equals(display)) {
                return false;
            }
            this.mRememberedWifiDisplays.set(index, display);
        } else {
            this.mRememberedWifiDisplays.add(display);
        }
        setDirty();
        return true;
    }

    public boolean forgetWifiDisplay(java.lang.String deviceAddress) {
        loadIfNeeded();
        int index = findRememberedWifiDisplay(deviceAddress);
        if (index >= 0) {
            this.mRememberedWifiDisplays.remove(index);
            setDirty();
            return true;
        }
        return false;
    }

    private int findRememberedWifiDisplay(java.lang.String deviceAddress) {
        int count = this.mRememberedWifiDisplays.size();
        for (int i = 0; i < count; i++) {
            if (this.mRememberedWifiDisplays.get(i).getDeviceAddress().equals(deviceAddress)) {
                return i;
            }
        }
        return -1;
    }

    public int getColorMode(com.android.server.display.DisplayDevice device) {
        com.android.server.display.PersistentDataStore.DisplayState state;
        if (device.hasStableUniqueId() && (state = getDisplayState(device.getUniqueId(), false)) != null) {
            return state.getColorMode();
        }
        return -1;
    }

    public boolean setColorMode(com.android.server.display.DisplayDevice device, int colorMode) {
        if (!device.hasStableUniqueId()) {
            return false;
        }
        com.android.server.display.PersistentDataStore.DisplayState state = getDisplayState(device.getUniqueId(), true);
        if (!state.setColorMode(colorMode)) {
            return false;
        }
        setDirty();
        return true;
    }

    public float getBrightness(com.android.server.display.DisplayDevice device, int userSerial) {
        com.android.server.display.PersistentDataStore.DisplayState state;
        if (device == null || !device.hasStableUniqueId() || (state = getDisplayState(device.getUniqueId(), false)) == null) {
            return Float.NaN;
        }
        return state.getBrightness(userSerial);
    }

    public boolean setBrightness(com.android.server.display.DisplayDevice displayDevice, float brightness, int userSerial) {
        java.lang.String displayDeviceUniqueId;
        if (displayDevice == null || !displayDevice.hasStableUniqueId() || (displayDeviceUniqueId = displayDevice.getUniqueId()) == null) {
            return false;
        }
        com.android.server.display.PersistentDataStore.DisplayState state = getDisplayState(displayDeviceUniqueId, true);
        if (!state.setBrightness(brightness, userSerial)) {
            return false;
        }
        setDirty();
        return true;
    }

    public float getBrightnessNitsForDefaultDisplay() {
        return this.mBrightnessNitsForDefaultDisplay;
    }

    public boolean setBrightnessNitsForDefaultDisplay(float nits) {
        if (nits != this.mBrightnessNitsForDefaultDisplay) {
            this.mBrightnessNitsForDefaultDisplay = nits;
            setDirty();
            return true;
        }
        return false;
    }

    public boolean setUserPreferredRefreshRate(com.android.server.display.DisplayDevice displayDevice, float refreshRate) {
        java.lang.String displayDeviceUniqueId = displayDevice.getUniqueId();
        if (!displayDevice.hasStableUniqueId() || displayDeviceUniqueId == null) {
            return false;
        }
        com.android.server.display.PersistentDataStore.DisplayState state = getDisplayState(displayDevice.getUniqueId(), true);
        if (!state.setRefreshRate(refreshRate)) {
            return false;
        }
        setDirty();
        return true;
    }

    public float getUserPreferredRefreshRate(com.android.server.display.DisplayDevice device) {
        com.android.server.display.PersistentDataStore.DisplayState state;
        if (device == null || !device.hasStableUniqueId() || (state = getDisplayState(device.getUniqueId(), false)) == null) {
            return Float.NaN;
        }
        return state.getRefreshRate();
    }

    public boolean setUserPreferredResolution(com.android.server.display.DisplayDevice displayDevice, int width, int height) {
        java.lang.String displayDeviceUniqueId = displayDevice.getUniqueId();
        if (!displayDevice.hasStableUniqueId() || displayDeviceUniqueId == null) {
            return false;
        }
        com.android.server.display.PersistentDataStore.DisplayState state = getDisplayState(displayDevice.getUniqueId(), true);
        if (!state.setResolution(width, height)) {
            return false;
        }
        setDirty();
        return true;
    }

    public android.graphics.Point getUserPreferredResolution(com.android.server.display.DisplayDevice displayDevice) {
        com.android.server.display.PersistentDataStore.DisplayState state;
        if (displayDevice == null || !displayDevice.hasStableUniqueId() || (state = getDisplayState(displayDevice.getUniqueId(), false)) == null) {
            return null;
        }
        return state.getResolution();
    }

    public android.graphics.Point getStableDisplaySize() {
        loadIfNeeded();
        return this.mStableDeviceValues.getDisplaySize();
    }

    public void setStableDisplaySize(android.graphics.Point size) {
        loadIfNeeded();
        if (this.mStableDeviceValues.setDisplaySize(size)) {
            setDirty();
        }
    }

    public void setBrightnessConfigurationForUser(android.hardware.display.BrightnessConfiguration c, int userSerial, java.lang.String packageName) {
        loadIfNeeded();
        if (this.mGlobalBrightnessConfigurations.setBrightnessConfigurationForUser(c, userSerial, packageName)) {
            setDirty();
        }
    }

    public boolean setBrightnessConfigurationForDisplayLocked(android.hardware.display.BrightnessConfiguration configuration, com.android.server.display.DisplayDevice device, int userSerial, java.lang.String packageName) {
        if (device == null || !device.hasStableUniqueId()) {
            return false;
        }
        com.android.server.display.PersistentDataStore.DisplayState state = getDisplayState(device.getUniqueId(), true);
        if (!state.setBrightnessConfiguration(configuration, userSerial, packageName)) {
            return false;
        }
        setDirty();
        return true;
    }

    public android.hardware.display.BrightnessConfiguration getBrightnessConfigurationForDisplayLocked(java.lang.String uniqueDisplayId, int userSerial) {
        loadIfNeeded();
        com.android.server.display.PersistentDataStore.DisplayState state = this.mDisplayStates.get(uniqueDisplayId);
        if (state != null) {
            return state.getBrightnessConfiguration(userSerial);
        }
        return null;
    }

    public android.hardware.display.BrightnessConfiguration getBrightnessConfiguration(int userSerial) {
        loadIfNeeded();
        return this.mGlobalBrightnessConfigurations.getBrightnessConfiguration(userSerial);
    }

    private com.android.server.display.PersistentDataStore.DisplayState getDisplayState(java.lang.String uniqueId, boolean createIfAbsent) {
        loadIfNeeded();
        com.android.server.display.PersistentDataStore.DisplayState state = this.mDisplayStates.get(uniqueId);
        if (state == null && createIfAbsent) {
            com.android.server.display.PersistentDataStore.DisplayState state2 = new com.android.server.display.PersistentDataStore.DisplayState();
            this.mDisplayStates.put(uniqueId, state2);
            setDirty();
            return state2;
        }
        return state;
    }

    public void loadIfNeeded() {
        if (!this.mLoaded) {
            load();
            this.mLoaded = true;
        }
    }

    private void setDirty() {
        this.mDirty = true;
    }

    private void clearState() {
        this.mRememberedWifiDisplays.clear();
    }

    private void load() {
        java.io.InputStream is;
        synchronized (this.mFileAccessLock) {
            clearState();
            try {
                try {
                    is = this.mInjector.openRead();
                    try {
                        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(is);
                        loadFromXml(parser);
                    } catch (java.io.IOException ex) {
                        android.util.Slog.w(TAG, "Failed to load display manager persistent store data.", ex);
                        clearState();
                    } catch (org.xmlpull.v1.XmlPullParserException ex2) {
                        android.util.Slog.w(TAG, "Failed to load display manager persistent store data.", ex2);
                        clearState();
                        libcore.io.IoUtils.closeQuietly(is);
                    }
                } catch (java.io.FileNotFoundException e) {
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(is);
            }
        }
    }

    private void save() {
        try {
            final java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(os);
            saveToXml(serializer);
            serializer.flush();
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.PersistentDataStore$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$save$0(os);
                }
            });
        } catch (java.io.IOException ex) {
            android.util.Slog.w(TAG, "Failed to process the XML serializer.", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$save$0(java.io.ByteArrayOutputStream os) {
        com.android.server.display.PersistentDataStore.Injector injector;
        synchronized (this.mFileAccessLock) {
            java.io.OutputStream fileOutput = null;
            try {
                try {
                    fileOutput = this.mInjector.startWrite();
                    os.writeTo(fileOutput);
                    fileOutput.flush();
                } catch (java.io.IOException ex) {
                    android.util.Slog.w(TAG, "Failed to save display manager persistent store data.", ex);
                    if (fileOutput != null) {
                        injector = this.mInjector;
                    }
                }
                if (fileOutput != null) {
                    injector = this.mInjector;
                    injector.finishWrite(fileOutput, true);
                }
            } catch (java.lang.Throwable th) {
                if (fileOutput != null) {
                    this.mInjector.finishWrite(fileOutput, true);
                }
                throw th;
            }
        }
    }

    private void loadFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.internal.util.XmlUtils.beginDocument(parser, TAG_DISPLAY_MANAGER_STATE);
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals(TAG_REMEMBERED_WIFI_DISPLAYS)) {
                loadRememberedWifiDisplaysFromXml(parser);
            }
            if (parser.getName().equals(TAG_DISPLAY_STATES)) {
                loadDisplaysFromXml(parser);
            }
            if (parser.getName().equals(TAG_STABLE_DEVICE_VALUES)) {
                this.mStableDeviceValues.loadFromXml(parser);
            }
            if (parser.getName().equals(TAG_BRIGHTNESS_CONFIGURATIONS)) {
                this.mGlobalBrightnessConfigurations.loadFromXml(parser);
            }
            if (parser.getName().equals(TAG_BRIGHTNESS_NITS_FOR_DEFAULT_DISPLAY)) {
                java.lang.String value = parser.nextText();
                this.mBrightnessNitsForDefaultDisplay = java.lang.Float.parseFloat(value);
            }
        }
    }

    private void loadRememberedWifiDisplaysFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals(TAG_WIFI_DISPLAY)) {
                java.lang.String deviceAddress = parser.getAttributeValue((java.lang.String) null, ATTR_DEVICE_ADDRESS);
                java.lang.String deviceName = parser.getAttributeValue((java.lang.String) null, ATTR_DEVICE_NAME);
                java.lang.String deviceAlias = parser.getAttributeValue((java.lang.String) null, ATTR_DEVICE_ALIAS);
                if (deviceAddress == null || deviceName == null) {
                    throw new org.xmlpull.v1.XmlPullParserException("Missing deviceAddress or deviceName attribute on wifi-display.");
                }
                if (findRememberedWifiDisplay(deviceAddress) >= 0) {
                    throw new org.xmlpull.v1.XmlPullParserException("Found duplicate wifi display device address.");
                }
                this.mRememberedWifiDisplays.add(new android.hardware.display.WifiDisplay(deviceAddress, deviceName, deviceAlias, false, false, false));
            }
        }
    }

    private void loadDisplaysFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals(TAG_DISPLAY)) {
                java.lang.String uniqueId = parser.getAttributeValue((java.lang.String) null, ATTR_UNIQUE_ID);
                if (uniqueId == null) {
                    throw new org.xmlpull.v1.XmlPullParserException("Missing unique-id attribute on display.");
                }
                if (this.mDisplayStates.containsKey(uniqueId)) {
                    throw new org.xmlpull.v1.XmlPullParserException("Found duplicate display.");
                }
                com.android.server.display.PersistentDataStore.DisplayState state = new com.android.server.display.PersistentDataStore.DisplayState();
                state.loadFromXml(parser);
                this.mDisplayStates.put(uniqueId, state);
            }
        }
    }

    private void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.startDocument((java.lang.String) null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startTag((java.lang.String) null, TAG_DISPLAY_MANAGER_STATE);
        serializer.startTag((java.lang.String) null, TAG_REMEMBERED_WIFI_DISPLAYS);
        for (android.hardware.display.WifiDisplay display : this.mRememberedWifiDisplays) {
            serializer.startTag((java.lang.String) null, TAG_WIFI_DISPLAY);
            serializer.attribute((java.lang.String) null, ATTR_DEVICE_ADDRESS, display.getDeviceAddress());
            serializer.attribute((java.lang.String) null, ATTR_DEVICE_NAME, display.getDeviceName());
            if (display.getDeviceAlias() != null) {
                serializer.attribute((java.lang.String) null, ATTR_DEVICE_ALIAS, display.getDeviceAlias());
            }
            serializer.endTag((java.lang.String) null, TAG_WIFI_DISPLAY);
        }
        serializer.endTag((java.lang.String) null, TAG_REMEMBERED_WIFI_DISPLAYS);
        serializer.startTag((java.lang.String) null, TAG_DISPLAY_STATES);
        for (java.util.Map.Entry<java.lang.String, com.android.server.display.PersistentDataStore.DisplayState> entry : this.mDisplayStates.entrySet()) {
            java.lang.String uniqueId = entry.getKey();
            com.android.server.display.PersistentDataStore.DisplayState state = entry.getValue();
            serializer.startTag((java.lang.String) null, TAG_DISPLAY);
            serializer.attribute((java.lang.String) null, ATTR_UNIQUE_ID, uniqueId);
            state.saveToXml(serializer);
            serializer.endTag((java.lang.String) null, TAG_DISPLAY);
        }
        serializer.endTag((java.lang.String) null, TAG_DISPLAY_STATES);
        serializer.startTag((java.lang.String) null, TAG_STABLE_DEVICE_VALUES);
        this.mStableDeviceValues.saveToXml(serializer);
        serializer.endTag((java.lang.String) null, TAG_STABLE_DEVICE_VALUES);
        serializer.startTag((java.lang.String) null, TAG_BRIGHTNESS_CONFIGURATIONS);
        this.mGlobalBrightnessConfigurations.saveToXml(serializer);
        serializer.endTag((java.lang.String) null, TAG_BRIGHTNESS_CONFIGURATIONS);
        serializer.startTag((java.lang.String) null, TAG_BRIGHTNESS_NITS_FOR_DEFAULT_DISPLAY);
        serializer.text(java.lang.Float.toString(this.mBrightnessNitsForDefaultDisplay));
        serializer.endTag((java.lang.String) null, TAG_BRIGHTNESS_NITS_FOR_DEFAULT_DISPLAY);
        serializer.endTag((java.lang.String) null, TAG_DISPLAY_MANAGER_STATE);
        serializer.endDocument();
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("PersistentDataStore");
        pw.println("  mLoaded=" + this.mLoaded);
        pw.println("  mDirty=" + this.mDirty);
        pw.println("  RememberedWifiDisplays:");
        int i = 0;
        for (android.hardware.display.WifiDisplay display : this.mRememberedWifiDisplays) {
            pw.println("    " + i + ": " + display);
            i++;
        }
        pw.println("  DisplayStates:");
        int i2 = 0;
        for (java.util.Map.Entry<java.lang.String, com.android.server.display.PersistentDataStore.DisplayState> entry : this.mDisplayStates.entrySet()) {
            pw.println("    " + i2 + ": " + entry.getKey());
            entry.getValue().dump(pw, "      ");
            i2++;
        }
        pw.println("  StableDeviceValues:");
        this.mStableDeviceValues.dump(pw, "      ");
        pw.println("  GlobalBrightnessConfigurations:");
        this.mGlobalBrightnessConfigurations.dump(pw, "      ");
        pw.println("  mBrightnessNitsForDefaultDisplay=" + this.mBrightnessNitsForDefaultDisplay);
    }

    private static final class DisplayState {
        private int mColorMode;
        private com.android.server.display.PersistentDataStore.BrightnessConfigurations mDisplayBrightnessConfigurations;
        private int mHeight;
        private android.util.SparseArray<java.lang.Float> mPerUserBrightness;
        private float mRefreshRate;
        private int mWidth;

        private DisplayState() {
            this.mPerUserBrightness = new android.util.SparseArray<>();
            this.mDisplayBrightnessConfigurations = new com.android.server.display.PersistentDataStore.BrightnessConfigurations();
        }

        public boolean setColorMode(int colorMode) {
            if (colorMode == this.mColorMode) {
                return false;
            }
            this.mColorMode = colorMode;
            return true;
        }

        public int getColorMode() {
            return this.mColorMode;
        }

        public boolean setBrightness(float brightness, int userSerial) {
            this.mPerUserBrightness.remove(-1);
            if (getBrightness(userSerial) == brightness) {
                return false;
            }
            this.mPerUserBrightness.set(userSerial, java.lang.Float.valueOf(brightness));
            return true;
        }

        public float getBrightness(int userSerial) {
            android.util.SparseArray<java.lang.Float> sparseArray = this.mPerUserBrightness;
            java.lang.Float fValueOf = java.lang.Float.valueOf(Float.NaN);
            float brightness = sparseArray.get(userSerial, fValueOf).floatValue();
            if (java.lang.Float.isNaN(brightness)) {
                return this.mPerUserBrightness.get(-1, fValueOf).floatValue();
            }
            return brightness;
        }

        public boolean setBrightnessConfiguration(android.hardware.display.BrightnessConfiguration configuration, int userSerial, java.lang.String packageName) {
            this.mDisplayBrightnessConfigurations.setBrightnessConfigurationForUser(configuration, userSerial, packageName);
            return true;
        }

        public android.hardware.display.BrightnessConfiguration getBrightnessConfiguration(int userSerial) {
            return (android.hardware.display.BrightnessConfiguration) this.mDisplayBrightnessConfigurations.mConfigurations.get(userSerial);
        }

        public boolean setResolution(int width, int height) {
            if (width == this.mWidth && height == this.mHeight) {
                return false;
            }
            this.mWidth = width;
            this.mHeight = height;
            return true;
        }

        public android.graphics.Point getResolution() {
            return new android.graphics.Point(this.mWidth, this.mHeight);
        }

        public boolean setRefreshRate(float refreshRate) {
            if (refreshRate == this.mRefreshRate) {
                return false;
            }
            this.mRefreshRate = refreshRate;
            return true;
        }

        public float getRefreshRate() {
            return this.mRefreshRate;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0055  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void loadFromXml(com.android.modules.utils.TypedXmlPullParser r4) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            /*
                r3 = this;
                int r0 = r4.getDepth()
            L4:
                boolean r1 = com.android.internal.util.XmlUtils.nextElementWithin(r4, r0)
                if (r1 == 0) goto L92
                java.lang.String r1 = r4.getName()
                int r2 = r1.hashCode()
                switch(r2) {
                    case -1377859227: goto L4a;
                    case -1321967815: goto L40;
                    case -945778443: goto L36;
                    case -196957848: goto L2b;
                    case -92443502: goto L20;
                    case 1243304397: goto L16;
                    default: goto L15;
                }
            L15:
                goto L55
            L16:
                java.lang.String r2 = "color-mode"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L15
                r1 = 0
                goto L56
            L20:
                java.lang.String r2 = "refresh-rate"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L15
                r1 = 5
                goto L56
            L2b:
                java.lang.String r2 = "resolution-height"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L15
                r1 = 4
                goto L56
            L36:
                java.lang.String r2 = "brightness-value"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L15
                r1 = 1
                goto L56
            L40:
                java.lang.String r2 = "brightness-configurations"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L15
                r1 = 2
                goto L56
            L4a:
                java.lang.String r2 = "resolution-width"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L15
                r1 = 3
                goto L56
            L55:
                r1 = -1
            L56:
                switch(r1) {
                    case 0: goto L85;
                    case 1: goto L81;
                    case 2: goto L7b;
                    case 3: goto L70;
                    case 4: goto L65;
                    case 5: goto L5a;
                    default: goto L59;
                }
            L59:
                goto L90
            L5a:
                java.lang.String r1 = r4.nextText()
                float r2 = java.lang.Float.parseFloat(r1)
                r3.mRefreshRate = r2
                goto L90
            L65:
                java.lang.String r1 = r4.nextText()
                int r2 = java.lang.Integer.parseInt(r1)
                r3.mHeight = r2
                goto L90
            L70:
                java.lang.String r1 = r4.nextText()
                int r2 = java.lang.Integer.parseInt(r1)
                r3.mWidth = r2
                goto L90
            L7b:
                com.android.server.display.PersistentDataStore$BrightnessConfigurations r1 = r3.mDisplayBrightnessConfigurations
                r1.loadFromXml(r4)
                goto L90
            L81:
                r3.loadBrightnessFromXml(r4)
                goto L90
            L85:
                java.lang.String r1 = r4.nextText()
                int r2 = java.lang.Integer.parseInt(r1)
                r3.mColorMode = r2
            L90:
                goto L4
            L92:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.PersistentDataStore.DisplayState.loadFromXml(com.android.modules.utils.TypedXmlPullParser):void");
        }

        public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            serializer.startTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_COLOR_MODE);
            serializer.text(java.lang.Integer.toString(this.mColorMode));
            serializer.endTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_COLOR_MODE);
            for (int i = 0; i < this.mPerUserBrightness.size(); i++) {
                serializer.startTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_BRIGHTNESS_VALUE);
                serializer.attributeInt((java.lang.String) null, com.android.server.display.PersistentDataStore.ATTR_USER_SERIAL, this.mPerUserBrightness.keyAt(i));
                serializer.text(java.lang.Float.toString(this.mPerUserBrightness.valueAt(i).floatValue()));
                serializer.endTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_BRIGHTNESS_VALUE);
            }
            serializer.startTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_BRIGHTNESS_CONFIGURATIONS);
            this.mDisplayBrightnessConfigurations.saveToXml(serializer);
            serializer.endTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_BRIGHTNESS_CONFIGURATIONS);
            serializer.startTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_RESOLUTION_WIDTH);
            serializer.text(java.lang.Integer.toString(this.mWidth));
            serializer.endTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_RESOLUTION_WIDTH);
            serializer.startTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_RESOLUTION_HEIGHT);
            serializer.text(java.lang.Integer.toString(this.mHeight));
            serializer.endTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_RESOLUTION_HEIGHT);
            serializer.startTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_REFRESH_RATE);
            serializer.text(java.lang.Float.toString(this.mRefreshRate));
            serializer.endTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_REFRESH_RATE);
        }

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "ColorMode=" + this.mColorMode);
            pw.println(prefix + "BrightnessValues: ");
            for (int i = 0; i < this.mPerUserBrightness.size(); i++) {
                pw.println("User: " + this.mPerUserBrightness.keyAt(i) + " Value: " + this.mPerUserBrightness.valueAt(i));
            }
            pw.println(prefix + "DisplayBrightnessConfigurations: ");
            this.mDisplayBrightnessConfigurations.dump(pw, prefix);
            pw.println(prefix + "Resolution=" + this.mWidth + " " + this.mHeight);
            pw.println(prefix + "RefreshRate=" + this.mRefreshRate);
        }

        private void loadBrightnessFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            int userSerial;
            try {
                userSerial = parser.getAttributeInt((java.lang.String) null, com.android.server.display.PersistentDataStore.ATTR_USER_SERIAL);
            } catch (java.lang.NumberFormatException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.e(com.android.server.display.PersistentDataStore.TAG, "Failed to read user serial", e);
                userSerial = -1;
            }
            java.lang.String brightness = parser.nextText();
            try {
                this.mPerUserBrightness.set(userSerial, java.lang.Float.valueOf(java.lang.Float.parseFloat(brightness)));
            } catch (java.lang.NumberFormatException nfe) {
                android.util.Slog.e(com.android.server.display.PersistentDataStore.TAG, "Failed to read brightness", nfe);
            }
        }
    }

    private static final class StableDeviceValues {
        private int mHeight;
        private int mWidth;

        private StableDeviceValues() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.graphics.Point getDisplaySize() {
            return new android.graphics.Point(this.mWidth, this.mHeight);
        }

        public boolean setDisplaySize(android.graphics.Point r) {
            if (this.mWidth != r.x || this.mHeight != r.y) {
                this.mWidth = r.x;
                this.mHeight = r.y;
                return true;
            }
            return false;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x002c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void loadFromXml(com.android.modules.utils.TypedXmlPullParser r4) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            /*
                r3 = this;
                int r0 = r4.getDepth()
            L4:
                boolean r1 = com.android.internal.util.XmlUtils.nextElementWithin(r4, r0)
                if (r1 == 0) goto L40
                java.lang.String r1 = r4.getName()
                int r2 = r1.hashCode()
                switch(r2) {
                    case -1635792540: goto L21;
                    case 1069578729: goto L16;
                    default: goto L15;
                }
            L15:
                goto L2c
            L16:
                java.lang.String r2 = "stable-display-width"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L15
                r1 = 0
                goto L2d
            L21:
                java.lang.String r2 = "stable-display-height"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L15
                r1 = 1
                goto L2d
            L2c:
                r1 = -1
            L2d:
                switch(r1) {
                    case 0: goto L38;
                    case 1: goto L31;
                    default: goto L30;
                }
            L30:
                goto L3f
            L31:
                int r1 = loadIntValue(r4)
                r3.mHeight = r1
                goto L3f
            L38:
                int r1 = loadIntValue(r4)
                r3.mWidth = r1
            L3f:
                goto L4
            L40:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.PersistentDataStore.StableDeviceValues.loadFromXml(com.android.modules.utils.TypedXmlPullParser):void");
        }

        private static int loadIntValue(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            try {
                java.lang.String value = parser.nextText();
                return java.lang.Integer.parseInt(value);
            } catch (java.lang.NumberFormatException e) {
                return 0;
            }
        }

        public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            if (this.mWidth > 0 && this.mHeight > 0) {
                serializer.startTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_STABLE_DISPLAY_WIDTH);
                serializer.text(java.lang.Integer.toString(this.mWidth));
                serializer.endTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_STABLE_DISPLAY_WIDTH);
                serializer.startTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_STABLE_DISPLAY_HEIGHT);
                serializer.text(java.lang.Integer.toString(this.mHeight));
                serializer.endTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_STABLE_DISPLAY_HEIGHT);
            }
        }

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "StableDisplayWidth=" + this.mWidth);
            pw.println(prefix + "StableDisplayHeight=" + this.mHeight);
        }
    }

    private static final class BrightnessConfigurations {
        private final android.util.SparseArray<android.hardware.display.BrightnessConfiguration> mConfigurations = new android.util.SparseArray<>();
        private final android.util.SparseLongArray mTimeStamps = new android.util.SparseLongArray();
        private final android.util.SparseArray<java.lang.String> mPackageNames = new android.util.SparseArray<>();

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setBrightnessConfigurationForUser(android.hardware.display.BrightnessConfiguration c, int userSerial, java.lang.String packageName) {
            android.hardware.display.BrightnessConfiguration currentConfig = this.mConfigurations.get(userSerial);
            if (currentConfig == c) {
                return false;
            }
            if (currentConfig == null || !currentConfig.equals(c)) {
                if (c != null) {
                    if (packageName == null) {
                        this.mPackageNames.remove(userSerial);
                    } else {
                        this.mPackageNames.put(userSerial, packageName);
                    }
                    this.mTimeStamps.put(userSerial, java.lang.System.currentTimeMillis());
                    this.mConfigurations.put(userSerial, c);
                    return true;
                }
                this.mPackageNames.remove(userSerial);
                this.mTimeStamps.delete(userSerial);
                this.mConfigurations.remove(userSerial);
                return true;
            }
            return false;
        }

        public android.hardware.display.BrightnessConfiguration getBrightnessConfiguration(int userSerial) {
            return this.mConfigurations.get(userSerial);
        }

        public void loadFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            int userSerial;
            int outerDepth = parser.getDepth();
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                if (com.android.server.display.PersistentDataStore.TAG_BRIGHTNESS_CONFIGURATION.equals(parser.getName())) {
                    try {
                        userSerial = parser.getAttributeInt((java.lang.String) null, com.android.server.display.PersistentDataStore.ATTR_USER_SERIAL);
                    } catch (java.lang.NumberFormatException nfe) {
                        android.util.Slog.e(com.android.server.display.PersistentDataStore.TAG, "Failed to read in brightness configuration", nfe);
                        userSerial = -1;
                    }
                    java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, com.android.server.display.PersistentDataStore.ATTR_PACKAGE_NAME);
                    long timeStamp = parser.getAttributeLong((java.lang.String) null, "timestamp", -1L);
                    try {
                        android.hardware.display.BrightnessConfiguration config = android.hardware.display.BrightnessConfiguration.loadFromXml(parser);
                        if (userSerial >= 0 && config != null) {
                            this.mConfigurations.put(userSerial, config);
                            if (timeStamp != -1) {
                                this.mTimeStamps.put(userSerial, timeStamp);
                            }
                            if (packageName != null) {
                                this.mPackageNames.put(userSerial, packageName);
                            }
                        }
                    } catch (java.lang.IllegalArgumentException iae) {
                        android.util.Slog.e(com.android.server.display.PersistentDataStore.TAG, "Failed to load brightness configuration!", iae);
                    }
                }
            }
        }

        public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            for (int i = 0; i < this.mConfigurations.size(); i++) {
                int userSerial = this.mConfigurations.keyAt(i);
                android.hardware.display.BrightnessConfiguration config = this.mConfigurations.valueAt(i);
                serializer.startTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_BRIGHTNESS_CONFIGURATION);
                serializer.attributeInt((java.lang.String) null, com.android.server.display.PersistentDataStore.ATTR_USER_SERIAL, userSerial);
                java.lang.String packageName = this.mPackageNames.get(userSerial);
                if (packageName != null) {
                    serializer.attribute((java.lang.String) null, com.android.server.display.PersistentDataStore.ATTR_PACKAGE_NAME, packageName);
                }
                long timestamp = this.mTimeStamps.get(userSerial, -1L);
                if (timestamp != -1) {
                    serializer.attributeLong((java.lang.String) null, "timestamp", timestamp);
                }
                config.saveToXml(serializer);
                serializer.endTag((java.lang.String) null, com.android.server.display.PersistentDataStore.TAG_BRIGHTNESS_CONFIGURATION);
            }
        }

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            for (int i = 0; i < this.mConfigurations.size(); i++) {
                int userSerial = this.mConfigurations.keyAt(i);
                long time = this.mTimeStamps.get(userSerial, -1L);
                java.lang.String packageName = this.mPackageNames.get(userSerial);
                pw.println(prefix + "User " + userSerial + ":");
                if (time != -1) {
                    pw.println(prefix + "  set at: " + android.util.TimeUtils.formatForLogging(time));
                }
                if (packageName != null) {
                    pw.println(prefix + "  set by: " + packageName);
                }
                pw.println(prefix + "  " + this.mConfigurations.valueAt(i));
            }
        }
    }

    static class Injector {
        private final android.util.AtomicFile mAtomicFile = new android.util.AtomicFile(new java.io.File("/data/system/display-manager-state.xml"), "display-state");

        public java.io.InputStream openRead() throws java.io.FileNotFoundException {
            return this.mAtomicFile.openRead();
        }

        public java.io.OutputStream startWrite() throws java.io.IOException {
            return this.mAtomicFile.startWrite();
        }

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
