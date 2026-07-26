package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
final class PersistentDataStore {
    private static final int INVALID_VALUE = -1;
    static final java.lang.String TAG = "InputManager";
    private boolean mDirty;
    private final com.android.server.input.PersistentDataStore.Injector mInjector;
    private final java.util.HashMap<java.lang.String, com.android.server.input.PersistentDataStore.InputDeviceState> mInputDevices;
    private final java.util.Map<java.lang.Integer, java.lang.Integer> mKeyRemapping;
    private boolean mLoaded;

    public PersistentDataStore() {
        this(new com.android.server.input.PersistentDataStore.Injector());
    }

    PersistentDataStore(com.android.server.input.PersistentDataStore.Injector injector) {
        this.mInputDevices = new java.util.HashMap<>();
        this.mKeyRemapping = new java.util.HashMap();
        this.mInjector = injector;
    }

    public void saveIfNeeded() {
        if (this.mDirty) {
            save();
            this.mDirty = false;
        }
    }

    public boolean hasInputDeviceEntry(java.lang.String inputDeviceDescriptor) {
        return getInputDeviceState(inputDeviceDescriptor) != null;
    }

    public android.hardware.input.TouchCalibration getTouchCalibration(java.lang.String inputDeviceDescriptor, int surfaceRotation) {
        com.android.server.input.PersistentDataStore.InputDeviceState state = getInputDeviceState(inputDeviceDescriptor);
        if (state == null) {
            return android.hardware.input.TouchCalibration.IDENTITY;
        }
        android.hardware.input.TouchCalibration cal = state.getTouchCalibration(surfaceRotation);
        if (cal == null) {
            return android.hardware.input.TouchCalibration.IDENTITY;
        }
        return cal;
    }

    public boolean setTouchCalibration(java.lang.String inputDeviceDescriptor, int surfaceRotation, android.hardware.input.TouchCalibration calibration) {
        com.android.server.input.PersistentDataStore.InputDeviceState state = getOrCreateInputDeviceState(inputDeviceDescriptor);
        if (state.setTouchCalibration(surfaceRotation, calibration)) {
            setDirty();
            return true;
        }
        return false;
    }

    public java.lang.String getKeyboardLayout(java.lang.String inputDeviceDescriptor, java.lang.String key) {
        com.android.server.input.PersistentDataStore.InputDeviceState state = getInputDeviceState(inputDeviceDescriptor);
        if (state != null) {
            return state.getKeyboardLayout(key);
        }
        return null;
    }

    public boolean setKeyboardLayout(java.lang.String inputDeviceDescriptor, java.lang.String key, java.lang.String keyboardLayoutDescriptor) {
        com.android.server.input.PersistentDataStore.InputDeviceState state = getOrCreateInputDeviceState(inputDeviceDescriptor);
        if (state.setKeyboardLayout(key, keyboardLayoutDescriptor)) {
            setDirty();
            return true;
        }
        return false;
    }

    public boolean setSelectedKeyboardLayouts(java.lang.String inputDeviceDescriptor, java.util.Set<java.lang.String> selectedLayouts) {
        com.android.server.input.PersistentDataStore.InputDeviceState state = getOrCreateInputDeviceState(inputDeviceDescriptor);
        if (state.setSelectedKeyboardLayouts(selectedLayouts)) {
            setDirty();
            return true;
        }
        return false;
    }

    public boolean setKeyboardBacklightBrightness(java.lang.String inputDeviceDescriptor, int lightId, int brightness) {
        com.android.server.input.PersistentDataStore.InputDeviceState state = getOrCreateInputDeviceState(inputDeviceDescriptor);
        if (state.setKeyboardBacklightBrightness(lightId, brightness)) {
            setDirty();
            return true;
        }
        return false;
    }

    public java.util.OptionalInt getKeyboardBacklightBrightness(java.lang.String inputDeviceDescriptor, int lightId) {
        com.android.server.input.PersistentDataStore.InputDeviceState state = getInputDeviceState(inputDeviceDescriptor);
        if (state == null) {
            return java.util.OptionalInt.empty();
        }
        return state.getKeyboardBacklightBrightness(lightId);
    }

    public boolean remapKey(int fromKey, int toKey) {
        loadIfNeeded();
        if (this.mKeyRemapping.getOrDefault(java.lang.Integer.valueOf(fromKey), -1).intValue() == toKey) {
            return false;
        }
        this.mKeyRemapping.put(java.lang.Integer.valueOf(fromKey), java.lang.Integer.valueOf(toKey));
        setDirty();
        return true;
    }

    public boolean clearMappedKey(int key) {
        loadIfNeeded();
        if (this.mKeyRemapping.containsKey(java.lang.Integer.valueOf(key))) {
            this.mKeyRemapping.remove(java.lang.Integer.valueOf(key));
            setDirty();
            return true;
        }
        return true;
    }

    public java.util.Map<java.lang.Integer, java.lang.Integer> getKeyRemapping() {
        loadIfNeeded();
        return new java.util.HashMap(this.mKeyRemapping);
    }

    public boolean removeUninstalledKeyboardLayouts(java.util.Set<java.lang.String> availableKeyboardLayouts) {
        boolean changed = false;
        for (com.android.server.input.PersistentDataStore.InputDeviceState state : this.mInputDevices.values()) {
            if (state.removeUninstalledKeyboardLayouts(availableKeyboardLayouts)) {
                changed = true;
            }
        }
        if (changed) {
            setDirty();
            return true;
        }
        return false;
    }

    private com.android.server.input.PersistentDataStore.InputDeviceState getInputDeviceState(java.lang.String inputDeviceDescriptor) {
        loadIfNeeded();
        return this.mInputDevices.get(inputDeviceDescriptor);
    }

    private com.android.server.input.PersistentDataStore.InputDeviceState getOrCreateInputDeviceState(java.lang.String inputDeviceDescriptor) {
        loadIfNeeded();
        com.android.server.input.PersistentDataStore.InputDeviceState state = this.mInputDevices.get(inputDeviceDescriptor);
        if (state == null) {
            com.android.server.input.PersistentDataStore.InputDeviceState state2 = new com.android.server.input.PersistentDataStore.InputDeviceState();
            this.mInputDevices.put(inputDeviceDescriptor, state2);
            setDirty();
            return state2;
        }
        return state;
    }

    private void loadIfNeeded() {
        if (!this.mLoaded) {
            load();
            this.mLoaded = true;
        }
    }

    private void setDirty() {
        this.mDirty = true;
    }

    private void clearState() {
        this.mKeyRemapping.clear();
        this.mInputDevices.clear();
    }

    private void load() {
        clearState();
        try {
            java.io.InputStream is = this.mInjector.openRead();
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(is);
                loadFromXml(parser);
            } catch (java.io.IOException ex) {
                android.util.Slog.w(TAG, "Failed to load input manager persistent store data.", ex);
                clearState();
            } catch (org.xmlpull.v1.XmlPullParserException ex2) {
                android.util.Slog.w(TAG, "Failed to load input manager persistent store data.", ex2);
                clearState();
            } finally {
                libcore.io.IoUtils.closeQuietly(is);
            }
        } catch (java.io.FileNotFoundException e) {
        }
    }

    private void save() {
        try {
            java.io.FileOutputStream os = this.mInjector.startWrite();
            try {
                com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(os);
                saveToXml(serializer);
                serializer.flush();
                this.mInjector.finishWrite(os, true);
            } catch (java.lang.Throwable th) {
                this.mInjector.finishWrite(os, false);
                throw th;
            }
        } catch (java.io.IOException ex) {
            android.util.Slog.w(TAG, "Failed to save input manager persistent store data.", ex);
        }
    }

    private void loadFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.internal.util.XmlUtils.beginDocument(parser, "input-manager-state");
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals("key-remapping")) {
                loadKeyRemappingFromXml(parser);
            } else if (parser.getName().equals("input-devices")) {
                loadInputDevicesFromXml(parser);
            }
        }
    }

    private void loadInputDevicesFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals("input-device")) {
                java.lang.String descriptor = parser.getAttributeValue((java.lang.String) null, "descriptor");
                if (descriptor == null) {
                    throw new org.xmlpull.v1.XmlPullParserException("Missing descriptor attribute on input-device.");
                }
                if (this.mInputDevices.containsKey(descriptor)) {
                    throw new org.xmlpull.v1.XmlPullParserException("Found duplicate input device.");
                }
                com.android.server.input.PersistentDataStore.InputDeviceState state = new com.android.server.input.PersistentDataStore.InputDeviceState();
                state.loadFromXml(parser);
                this.mInputDevices.put(descriptor, state);
            }
        }
    }

    private void loadKeyRemappingFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals("remap")) {
                int fromKey = parser.getAttributeInt((java.lang.String) null, "from-key");
                int toKey = parser.getAttributeInt((java.lang.String) null, "to-key");
                this.mKeyRemapping.put(java.lang.Integer.valueOf(fromKey), java.lang.Integer.valueOf(toKey));
            }
        }
    }

    private void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.startDocument((java.lang.String) null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startTag((java.lang.String) null, "input-manager-state");
        serializer.startTag((java.lang.String) null, "key-remapping");
        java.util.Iterator<java.lang.Integer> it = this.mKeyRemapping.keySet().iterator();
        while (it.hasNext()) {
            int fromKey = it.next().intValue();
            int toKey = this.mKeyRemapping.get(java.lang.Integer.valueOf(fromKey)).intValue();
            serializer.startTag((java.lang.String) null, "remap");
            serializer.attributeInt((java.lang.String) null, "from-key", fromKey);
            serializer.attributeInt((java.lang.String) null, "to-key", toKey);
            serializer.endTag((java.lang.String) null, "remap");
        }
        serializer.endTag((java.lang.String) null, "key-remapping");
        serializer.startTag((java.lang.String) null, "input-devices");
        for (java.util.Map.Entry<java.lang.String, com.android.server.input.PersistentDataStore.InputDeviceState> entry : this.mInputDevices.entrySet()) {
            java.lang.String descriptor = entry.getKey();
            com.android.server.input.PersistentDataStore.InputDeviceState state = entry.getValue();
            serializer.startTag((java.lang.String) null, "input-device");
            serializer.attribute((java.lang.String) null, "descriptor", descriptor);
            state.saveToXml(serializer);
            serializer.endTag((java.lang.String) null, "input-device");
        }
        serializer.endTag((java.lang.String) null, "input-devices");
        serializer.endTag((java.lang.String) null, "input-manager-state");
        serializer.endDocument();
    }

    private static final class InputDeviceState {
        private static final java.lang.String[] CALIBRATION_NAME = {"x_scale", "x_ymix", "x_offset", "y_xmix", "y_scale", "y_offset"};
        private final android.util.SparseIntArray mKeyboardBacklightBrightnessMap;
        private final java.util.Map<java.lang.String, java.lang.String> mKeyboardLayoutMap;
        private java.util.Set<java.lang.String> mSelectedKeyboardLayouts;
        private final android.hardware.input.TouchCalibration[] mTouchCalibration;

        private InputDeviceState() {
            this.mTouchCalibration = new android.hardware.input.TouchCalibration[4];
            this.mKeyboardBacklightBrightnessMap = new android.util.SparseIntArray();
            this.mKeyboardLayoutMap = new android.util.ArrayMap();
        }

        public android.hardware.input.TouchCalibration getTouchCalibration(int surfaceRotation) {
            try {
                return this.mTouchCalibration[surfaceRotation];
            } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
                android.util.Slog.w(com.android.server.input.PersistentDataStore.TAG, "Cannot get touch calibration.", ex);
                return null;
            }
        }

        public boolean setTouchCalibration(int surfaceRotation, android.hardware.input.TouchCalibration calibration) {
            try {
                if (calibration.equals(this.mTouchCalibration[surfaceRotation])) {
                    return false;
                }
                this.mTouchCalibration[surfaceRotation] = calibration;
                return true;
            } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
                android.util.Slog.w(com.android.server.input.PersistentDataStore.TAG, "Cannot set touch calibration.", ex);
                return false;
            }
        }

        public java.lang.String getKeyboardLayout(java.lang.String key) {
            return this.mKeyboardLayoutMap.get(key);
        }

        public boolean setKeyboardLayout(java.lang.String key, java.lang.String keyboardLayout) {
            return !java.util.Objects.equals(this.mKeyboardLayoutMap.put(key, keyboardLayout), keyboardLayout);
        }

        public boolean setSelectedKeyboardLayouts(java.util.Set<java.lang.String> selectedLayouts) {
            if (java.util.Objects.equals(this.mSelectedKeyboardLayouts, selectedLayouts)) {
                return false;
            }
            this.mSelectedKeyboardLayouts = new java.util.HashSet(selectedLayouts);
            return true;
        }

        public boolean setKeyboardBacklightBrightness(int lightId, int brightness) {
            if (this.mKeyboardBacklightBrightnessMap.get(lightId, -1) == brightness) {
                return false;
            }
            this.mKeyboardBacklightBrightnessMap.put(lightId, brightness);
            return true;
        }

        public java.util.OptionalInt getKeyboardBacklightBrightness(int lightId) {
            int brightness = this.mKeyboardBacklightBrightnessMap.get(lightId, -1);
            return brightness == -1 ? java.util.OptionalInt.empty() : java.util.OptionalInt.of(brightness);
        }

        public boolean removeUninstalledKeyboardLayouts(java.util.Set<java.lang.String> availableKeyboardLayouts) {
            java.util.List<java.lang.String> removedEntries = new java.util.ArrayList<>();
            for (java.lang.String key : this.mKeyboardLayoutMap.keySet()) {
                if (!availableKeyboardLayouts.contains(this.mKeyboardLayoutMap.get(key))) {
                    removedEntries.add(key);
                }
            }
            if (removedEntries.isEmpty()) {
                return false;
            }
            java.util.Iterator<java.lang.String> it = removedEntries.iterator();
            while (it.hasNext()) {
                this.mKeyboardLayoutMap.remove(it.next());
            }
            return true;
        }

        public void loadFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            int outerDepth = parser.getDepth();
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                if (parser.getName().equals("keyed-keyboard-layout")) {
                    java.lang.String key = parser.getAttributeValue((java.lang.String) null, "key");
                    if (key == null) {
                        throw new org.xmlpull.v1.XmlPullParserException("Missing key attribute on keyed-keyboard-layout.");
                    }
                    java.lang.String layout = parser.getAttributeValue((java.lang.String) null, "layout");
                    if (layout == null) {
                        throw new org.xmlpull.v1.XmlPullParserException("Missing layout attribute on keyed-keyboard-layout.");
                    }
                    this.mKeyboardLayoutMap.put(key, layout);
                } else if (parser.getName().equals("selected-keyboard-layout")) {
                    java.lang.String layout2 = parser.getAttributeValue((java.lang.String) null, "layout");
                    if (layout2 == null) {
                        throw new org.xmlpull.v1.XmlPullParserException("Missing layout attribute on selected-keyboard-layout.");
                    }
                    if (this.mSelectedKeyboardLayouts == null) {
                        this.mSelectedKeyboardLayouts = new java.util.HashSet();
                    }
                    this.mSelectedKeyboardLayouts.add(layout2);
                } else if (parser.getName().equals("light-info")) {
                    int lightId = parser.getAttributeInt((java.lang.String) null, "light-id");
                    int lightBrightness = parser.getAttributeInt((java.lang.String) null, "light-brightness");
                    this.mKeyboardBacklightBrightnessMap.put(lightId, lightBrightness);
                } else if (parser.getName().equals("calibration")) {
                    java.lang.String format = parser.getAttributeValue((java.lang.String) null, "format");
                    java.lang.String rotation = parser.getAttributeValue((java.lang.String) null, "rotation");
                    int r = -1;
                    if (format == null) {
                        throw new org.xmlpull.v1.XmlPullParserException("Missing format attribute on calibration.");
                    }
                    if (!format.equals("affine")) {
                        throw new org.xmlpull.v1.XmlPullParserException("Unsupported format for calibration.");
                    }
                    if (rotation != null) {
                        try {
                            r = stringToSurfaceRotation(rotation);
                        } catch (java.lang.IllegalArgumentException e) {
                            throw new org.xmlpull.v1.XmlPullParserException("Unsupported rotation for calibration.");
                        }
                    }
                    float[] matrix = android.hardware.input.TouchCalibration.IDENTITY.getAffineTransform();
                    int depth = parser.getDepth();
                    while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                        java.lang.String tag = parser.getName().toLowerCase();
                        java.lang.String value = parser.nextText();
                        int i = 0;
                        while (true) {
                            if (i >= matrix.length || i >= CALIBRATION_NAME.length) {
                                break;
                            }
                            if (!tag.equals(CALIBRATION_NAME[i])) {
                                i++;
                            } else {
                                matrix[i] = java.lang.Float.parseFloat(value);
                                break;
                            }
                        }
                    }
                    if (r == -1) {
                        for (int r2 = 0; r2 < this.mTouchCalibration.length; r2++) {
                            this.mTouchCalibration[r2] = new android.hardware.input.TouchCalibration(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
                        }
                    } else {
                        this.mTouchCalibration[r] = new android.hardware.input.TouchCalibration(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
                    }
                } else {
                    continue;
                }
            }
        }

        public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            for (java.lang.String key : this.mKeyboardLayoutMap.keySet()) {
                serializer.startTag((java.lang.String) null, "keyed-keyboard-layout");
                serializer.attribute((java.lang.String) null, "key", key);
                serializer.attribute((java.lang.String) null, "layout", this.mKeyboardLayoutMap.get(key));
                serializer.endTag((java.lang.String) null, "keyed-keyboard-layout");
            }
            if (this.mSelectedKeyboardLayouts != null) {
                for (java.lang.String layout : this.mSelectedKeyboardLayouts) {
                    serializer.startTag((java.lang.String) null, "selected-keyboard-layout");
                    serializer.attribute((java.lang.String) null, "layout", layout);
                    serializer.endTag((java.lang.String) null, "selected-keyboard-layout");
                }
            }
            for (int i = 0; i < this.mKeyboardBacklightBrightnessMap.size(); i++) {
                serializer.startTag((java.lang.String) null, "light-info");
                serializer.attributeInt((java.lang.String) null, "light-id", this.mKeyboardBacklightBrightnessMap.keyAt(i));
                serializer.attributeInt((java.lang.String) null, "light-brightness", this.mKeyboardBacklightBrightnessMap.valueAt(i));
                serializer.endTag((java.lang.String) null, "light-info");
            }
            for (int i2 = 0; i2 < this.mTouchCalibration.length; i2++) {
                if (this.mTouchCalibration[i2] != null) {
                    java.lang.String rotation = surfaceRotationToString(i2);
                    float[] transform = this.mTouchCalibration[i2].getAffineTransform();
                    serializer.startTag((java.lang.String) null, "calibration");
                    serializer.attribute((java.lang.String) null, "format", "affine");
                    serializer.attribute((java.lang.String) null, "rotation", rotation);
                    for (int j = 0; j < transform.length && j < CALIBRATION_NAME.length; j++) {
                        serializer.startTag((java.lang.String) null, CALIBRATION_NAME[j]);
                        serializer.text(java.lang.Float.toString(transform[j]));
                        serializer.endTag((java.lang.String) null, CALIBRATION_NAME[j]);
                    }
                    serializer.endTag((java.lang.String) null, "calibration");
                }
            }
        }

        private static java.lang.String surfaceRotationToString(int surfaceRotation) {
            switch (surfaceRotation) {
                case 0:
                    return "0";
                case 1:
                    return "90";
                case 2:
                    return "180";
                case 3:
                    return "270";
                default:
                    throw new java.lang.IllegalArgumentException("Unsupported surface rotation value" + surfaceRotation);
            }
        }

        private static int stringToSurfaceRotation(java.lang.String s) {
            if ("0".equals(s)) {
                return 0;
            }
            if ("90".equals(s)) {
                return 1;
            }
            if ("180".equals(s)) {
                return 2;
            }
            if ("270".equals(s)) {
                return 3;
            }
            throw new java.lang.IllegalArgumentException("Unsupported surface rotation string '" + s + "'");
        }
    }

    static class Injector {
        private final android.util.AtomicFile mAtomicFile = new android.util.AtomicFile(new java.io.File("/data/system/input-manager-state.xml"), "input-state");

        Injector() {
        }

        java.io.InputStream openRead() throws java.io.FileNotFoundException {
            return this.mAtomicFile.openRead();
        }

        java.io.FileOutputStream startWrite() throws java.io.IOException {
            return this.mAtomicFile.startWrite();
        }

        void finishWrite(java.io.FileOutputStream fos, boolean success) {
            if (success) {
                this.mAtomicFile.finishWrite(fos);
            } else {
                this.mAtomicFile.failWrite(fos);
            }
        }
    }
}
