package com.android.server.sensorprivacy;

/* JADX INFO: loaded from: classes3.dex */
class PersistedState {
    private static final int CURRENT_PERSISTENCE_VERSION = 2;
    private static final int CURRENT_VERSION = 2;
    private static final java.lang.String LOG_TAG = com.android.server.sensorprivacy.PersistedState.class.getSimpleName();
    private static final java.lang.String XML_ATTRIBUTE_LAST_CHANGE = "last-change";
    private static final java.lang.String XML_ATTRIBUTE_PERSISTENCE_VERSION = "persistence-version";
    private static final java.lang.String XML_ATTRIBUTE_SENSOR = "sensor";
    private static final java.lang.String XML_ATTRIBUTE_STATE_TYPE = "state-type";
    private static final java.lang.String XML_ATTRIBUTE_TOGGLE_TYPE = "toggle-type";
    private static final java.lang.String XML_ATTRIBUTE_USER_ID = "user-id";
    private static final java.lang.String XML_ATTRIBUTE_VERSION = "version";
    private static final java.lang.String XML_TAG_SENSOR_PRIVACY = "sensor-privacy";
    private static final java.lang.String XML_TAG_SENSOR_STATE = "sensor-state";
    private final android.util.AtomicFile mAtomicFile;
    private android.util.ArrayMap<com.android.server.sensorprivacy.PersistedState.TypeUserSensor, com.android.server.sensorprivacy.SensorState> mStates = new android.util.ArrayMap<>();

    static com.android.server.sensorprivacy.PersistedState fromFile(java.lang.String fileName) {
        return new com.android.server.sensorprivacy.PersistedState(fileName);
    }

    private PersistedState(java.lang.String fileName) {
        this.mAtomicFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), fileName));
        readState();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readState() {
        android.util.AtomicFile atomicFile = this.mAtomicFile;
        if (!atomicFile.exists()) {
            android.util.AtomicFile atomicFile2 = new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), "sensor_privacy.xml"));
            if (atomicFile2.exists()) {
                try {
                    java.io.FileInputStream fileInputStreamOpenRead = atomicFile2.openRead();
                    try {
                        com.android.internal.util.XmlUtils.beginDocument(android.util.Xml.resolvePullParser(fileInputStreamOpenRead), XML_TAG_SENSOR_PRIVACY);
                        atomicFile = atomicFile2;
                        if (fileInputStreamOpenRead != null) {
                            fileInputStreamOpenRead.close();
                        }
                    } finally {
                    }
                } catch (java.io.IOException e) {
                    android.util.Log.e(LOG_TAG, "Caught an exception reading the state from storage: ", e);
                    atomicFile2.delete();
                } catch (org.xmlpull.v1.XmlPullParserException e2) {
                }
            }
        }
        java.lang.Object objFromPVersion1 = null;
        int i = 2;
        java.lang.Object[] objArr = 0;
        java.lang.Object[] objArr2 = 0;
        java.lang.Object[] objArr3 = 0;
        if (atomicFile.exists()) {
            try {
                java.io.FileInputStream fileInputStreamOpenRead2 = atomicFile.openRead();
                try {
                    com.android.modules.utils.TypedXmlPullParser typedXmlPullParserResolvePullParser = android.util.Xml.resolvePullParser(fileInputStreamOpenRead2);
                    com.android.internal.util.XmlUtils.beginDocument(typedXmlPullParserResolvePullParser, XML_TAG_SENSOR_PRIVACY);
                    int attributeInt = typedXmlPullParserResolvePullParser.getAttributeInt((java.lang.String) null, XML_ATTRIBUTE_PERSISTENCE_VERSION, 0);
                    if (attributeInt == 0) {
                        com.android.server.sensorprivacy.PersistedState.PVersion0 pVersion0 = new com.android.server.sensorprivacy.PersistedState.PVersion0(0);
                        objFromPVersion1 = pVersion0;
                        readPVersion0(typedXmlPullParserResolvePullParser, pVersion0);
                    } else if (attributeInt == 1) {
                        com.android.server.sensorprivacy.PersistedState.PVersion1 pVersion1 = new com.android.server.sensorprivacy.PersistedState.PVersion1(typedXmlPullParserResolvePullParser.getAttributeInt((java.lang.String) null, XML_ATTRIBUTE_VERSION, 1));
                        objFromPVersion1 = pVersion1;
                        readPVersion1(typedXmlPullParserResolvePullParser, pVersion1);
                    } else if (attributeInt == 2) {
                        com.android.server.sensorprivacy.PersistedState.PVersion2 pVersion2 = new com.android.server.sensorprivacy.PersistedState.PVersion2(typedXmlPullParserResolvePullParser.getAttributeInt((java.lang.String) null, XML_ATTRIBUTE_VERSION, 2));
                        objFromPVersion1 = pVersion2;
                        readPVersion2(typedXmlPullParserResolvePullParser, pVersion2);
                    } else {
                        android.util.Log.e(LOG_TAG, "Unknown persistence version: " + attributeInt + ". Deleting.", new java.lang.RuntimeException());
                        atomicFile.delete();
                        objFromPVersion1 = null;
                    }
                    if (fileInputStreamOpenRead2 != null) {
                        fileInputStreamOpenRead2.close();
                    }
                } finally {
                }
            } catch (java.io.IOException | java.lang.RuntimeException | org.xmlpull.v1.XmlPullParserException e3) {
                android.util.Log.e(LOG_TAG, "Caught an exception reading the state from storage: ", e3);
                atomicFile.delete();
                objFromPVersion1 = null;
            }
        }
        if (objFromPVersion1 == null) {
            objFromPVersion1 = new com.android.server.sensorprivacy.PersistedState.PVersion2(i);
        }
        if (objFromPVersion1 instanceof com.android.server.sensorprivacy.PersistedState.PVersion0) {
            objFromPVersion1 = com.android.server.sensorprivacy.PersistedState.PVersion1.fromPVersion0((com.android.server.sensorprivacy.PersistedState.PVersion0) objFromPVersion1);
        }
        if (objFromPVersion1 instanceof com.android.server.sensorprivacy.PersistedState.PVersion1) {
            objFromPVersion1 = com.android.server.sensorprivacy.PersistedState.PVersion2.fromPVersion1((com.android.server.sensorprivacy.PersistedState.PVersion1) objFromPVersion1);
        }
        if (objFromPVersion1 instanceof com.android.server.sensorprivacy.PersistedState.PVersion2) {
            this.mStates = ((com.android.server.sensorprivacy.PersistedState.PVersion2) objFromPVersion1).mStates;
        } else {
            android.util.Log.e(LOG_TAG, "State not successfully upgraded.");
            this.mStates = new android.util.ArrayMap<>();
        }
    }

    private static void readPVersion0(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.sensorprivacy.PersistedState.PVersion0 version0) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.internal.util.XmlUtils.nextElement(parser);
        while (parser.getEventType() != 1) {
            if ("individual-sensor-privacy".equals(parser.getName())) {
                int sensor = com.android.internal.util.XmlUtils.readIntAttribute(parser, "sensor");
                boolean indEnabled = com.android.internal.util.XmlUtils.readBooleanAttribute(parser, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
                version0.addState(sensor, indEnabled);
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            } else {
                com.android.internal.util.XmlUtils.nextElement(parser);
            }
        }
    }

    private static void readPVersion1(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.sensorprivacy.PersistedState.PVersion1 version1) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        while (parser.getEventType() != 1) {
            com.android.internal.util.XmlUtils.nextElement(parser);
            if ("user".equals(parser.getName())) {
                int currentUserId = parser.getAttributeInt((java.lang.String) null, "id");
                int depth = parser.getDepth();
                while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                    if ("individual-sensor-privacy".equals(parser.getName())) {
                        int sensor = parser.getAttributeInt((java.lang.String) null, "sensor");
                        boolean isEnabled = parser.getAttributeBoolean((java.lang.String) null, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
                        version1.addState(currentUserId, sensor, isEnabled);
                    }
                }
            }
        }
    }

    private static void readPVersion2(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.sensorprivacy.PersistedState.PVersion2 version2) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        while (parser.getEventType() != 1) {
            com.android.internal.util.XmlUtils.nextElement(parser);
            if (XML_TAG_SENSOR_STATE.equals(parser.getName())) {
                int toggleType = parser.getAttributeInt((java.lang.String) null, XML_ATTRIBUTE_TOGGLE_TYPE);
                int userId = parser.getAttributeInt((java.lang.String) null, XML_ATTRIBUTE_USER_ID);
                int sensor = parser.getAttributeInt((java.lang.String) null, "sensor");
                int state = parser.getAttributeInt((java.lang.String) null, XML_ATTRIBUTE_STATE_TYPE);
                long lastChange = parser.getAttributeLong((java.lang.String) null, XML_ATTRIBUTE_LAST_CHANGE);
                version2.addState(toggleType, userId, sensor, state, lastChange);
            } else {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        }
    }

    public com.android.server.sensorprivacy.SensorState getState(int toggleType, int userId, int sensor) {
        return this.mStates.get(new com.android.server.sensorprivacy.PersistedState.TypeUserSensor(toggleType, userId, sensor));
    }

    public com.android.server.sensorprivacy.SensorState setState(int toggleType, int userId, int sensor, com.android.server.sensorprivacy.SensorState sensorState) {
        return this.mStates.put(new com.android.server.sensorprivacy.PersistedState.TypeUserSensor(toggleType, userId, sensor), sensorState);
    }

    private static class TypeUserSensor {
        int mSensor;
        int mType;
        int mUserId;

        TypeUserSensor(int type, int userId, int sensor) {
            this.mType = type;
            this.mUserId = userId;
            this.mSensor = sensor;
        }

        TypeUserSensor(com.android.server.sensorprivacy.PersistedState.TypeUserSensor typeUserSensor) {
            this(typeUserSensor.mType, typeUserSensor.mUserId, typeUserSensor.mSensor);
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.sensorprivacy.PersistedState.TypeUserSensor)) {
                return false;
            }
            com.android.server.sensorprivacy.PersistedState.TypeUserSensor that = (com.android.server.sensorprivacy.PersistedState.TypeUserSensor) o;
            return this.mType == that.mType && this.mUserId == that.mUserId && this.mSensor == that.mSensor;
        }

        public int hashCode() {
            return (((this.mType * 31) + this.mUserId) * 31) + this.mSensor;
        }
    }

    void schedulePersist() {
        int numStates = this.mStates.size();
        android.util.ArrayMap<com.android.server.sensorprivacy.PersistedState.TypeUserSensor, com.android.server.sensorprivacy.SensorState> statesCopy = new android.util.ArrayMap<>();
        for (int i = 0; i < numStates; i++) {
            statesCopy.put(new com.android.server.sensorprivacy.PersistedState.TypeUserSensor(this.mStates.keyAt(i)), new com.android.server.sensorprivacy.SensorState(this.mStates.valueAt(i)));
        }
        com.android.server.IoThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.sensorprivacy.PersistedState$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.sensorprivacy.PersistedState) obj).persist((android.util.ArrayMap) obj2);
            }
        }, this, statesCopy));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persist(android.util.ArrayMap<com.android.server.sensorprivacy.PersistedState.TypeUserSensor, com.android.server.sensorprivacy.SensorState> states) {
        java.io.FileOutputStream outputStream = null;
        try {
            outputStream = this.mAtomicFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(outputStream);
            serializer.startDocument((java.lang.String) null, true);
            serializer.startTag((java.lang.String) null, XML_TAG_SENSOR_PRIVACY);
            serializer.attributeInt((java.lang.String) null, XML_ATTRIBUTE_PERSISTENCE_VERSION, 2);
            serializer.attributeInt((java.lang.String) null, XML_ATTRIBUTE_VERSION, 2);
            for (int i = 0; i < states.size(); i++) {
                com.android.server.sensorprivacy.PersistedState.TypeUserSensor userSensor = states.keyAt(i);
                com.android.server.sensorprivacy.SensorState sensorState = states.valueAt(i);
                if (userSensor.mType == 1) {
                    serializer.startTag((java.lang.String) null, XML_TAG_SENSOR_STATE);
                    serializer.attributeInt((java.lang.String) null, XML_ATTRIBUTE_TOGGLE_TYPE, userSensor.mType);
                    serializer.attributeInt((java.lang.String) null, XML_ATTRIBUTE_USER_ID, userSensor.mUserId);
                    serializer.attributeInt((java.lang.String) null, "sensor", userSensor.mSensor);
                    serializer.attributeInt((java.lang.String) null, XML_ATTRIBUTE_STATE_TYPE, sensorState.getState());
                    serializer.attributeLong((java.lang.String) null, XML_ATTRIBUTE_LAST_CHANGE, sensorState.getLastChange());
                    serializer.endTag((java.lang.String) null, XML_TAG_SENSOR_STATE);
                }
            }
            serializer.endTag((java.lang.String) null, XML_TAG_SENSOR_PRIVACY);
            serializer.endDocument();
            this.mAtomicFile.finishWrite(outputStream);
        } catch (java.io.IOException e) {
            android.util.Log.e(LOG_TAG, "Caught an exception persisting the sensor privacy state: ", e);
            this.mAtomicFile.failWrite(outputStream);
        }
    }

    void dump(com.android.internal.util.dump.DualDumpOutputStream dumpStream) {
        android.util.SparseArray<android.util.SparseArray<android.util.Pair<java.lang.Integer, com.android.server.sensorprivacy.SensorState>>> statesMatrix = new android.util.SparseArray<>();
        int numStates = this.mStates.size();
        for (int i = 0; i < numStates; i++) {
            int toggleType = this.mStates.keyAt(i).mType;
            int userId = this.mStates.keyAt(i).mUserId;
            int sensor = this.mStates.keyAt(i).mSensor;
            android.util.SparseArray<android.util.Pair<java.lang.Integer, com.android.server.sensorprivacy.SensorState>> userStates = statesMatrix.get(userId);
            if (userStates == null) {
                userStates = new android.util.SparseArray<>();
                statesMatrix.put(userId, userStates);
            }
            userStates.put(sensor, new android.util.Pair<>(java.lang.Integer.valueOf(toggleType), this.mStates.valueAt(i)));
        }
        dumpStream.write("storage_implementation", 1138166333444L, com.android.server.sensorprivacy.SensorPrivacyStateControllerImpl.class.getName());
        int numUsers = statesMatrix.size();
        int i2 = 0;
        while (i2 < numUsers) {
            int userId2 = statesMatrix.keyAt(i2);
            long userToken = dumpStream.start(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_USERS, 2246267895811L);
            dumpStream.write("user_id", 1120986464257L, userId2);
            android.util.SparseArray<android.util.Pair<java.lang.Integer, com.android.server.sensorprivacy.SensorState>> userStates2 = statesMatrix.valueAt(i2);
            int numSensors = userStates2.size();
            int j = 0;
            while (j < numSensors) {
                int sensor2 = userStates2.keyAt(j);
                int toggleType2 = ((java.lang.Integer) userStates2.valueAt(j).first).intValue();
                com.android.server.sensorprivacy.SensorState sensorState = (com.android.server.sensorprivacy.SensorState) userStates2.valueAt(j).second;
                int numSensors2 = numSensors;
                long sensorToken = dumpStream.start("sensors", 2246267895812L);
                dumpStream.write("sensor", 1120986464257L, sensor2);
                long toggleToken = dumpStream.start("toggles", 2246267895810L);
                dumpStream.write("toggle_type", 1159641169924L, toggleType2);
                dumpStream.write("state_type", 1159641169925L, sensorState.getState());
                dumpStream.write("last_change", 1112396529667L, sensorState.getLastChange());
                dumpStream.end(toggleToken);
                dumpStream.end(sensorToken);
                j++;
                numSensors = numSensors2;
                statesMatrix = statesMatrix;
                numStates = numStates;
                numUsers = numUsers;
                i2 = i2;
            }
            dumpStream.end(userToken);
            i2++;
        }
    }

    void forEachKnownState(com.android.internal.util.function.QuadConsumer<java.lang.Integer, java.lang.Integer, java.lang.Integer, com.android.server.sensorprivacy.SensorState> consumer) {
        int numStates = this.mStates.size();
        for (int i = 0; i < numStates; i++) {
            com.android.server.sensorprivacy.PersistedState.TypeUserSensor tus = this.mStates.keyAt(i);
            com.android.server.sensorprivacy.SensorState sensorState = this.mStates.valueAt(i);
            consumer.accept(java.lang.Integer.valueOf(tus.mType), java.lang.Integer.valueOf(tus.mUserId), java.lang.Integer.valueOf(tus.mSensor), sensorState);
        }
    }

    private static class PVersion0 {
        private android.util.SparseArray<com.android.server.sensorprivacy.SensorState> mIndividualEnabled;

        private PVersion0(int version) {
            this.mIndividualEnabled = new android.util.SparseArray<>();
            if (version != 0) {
                throw new java.lang.RuntimeException("Only version 0 supported");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addState(int sensor, boolean enabled) {
            this.mIndividualEnabled.put(sensor, new com.android.server.sensorprivacy.SensorState(enabled));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void upgrade() {
        }
    }

    private static class PVersion1 {
        private android.util.SparseArray<android.util.SparseArray<com.android.server.sensorprivacy.SensorState>> mIndividualEnabled;

        private PVersion1(int version) {
            this.mIndividualEnabled = new android.util.SparseArray<>();
            if (version != 1) {
                throw new java.lang.RuntimeException("Only version 1 supported");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static com.android.server.sensorprivacy.PersistedState.PVersion1 fromPVersion0(com.android.server.sensorprivacy.PersistedState.PVersion0 version0) {
            version0.upgrade();
            com.android.server.sensorprivacy.PersistedState.PVersion1 result = new com.android.server.sensorprivacy.PersistedState.PVersion1(1);
            int[] users = {0};
            try {
                users = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserIds();
            } catch (java.lang.Exception e) {
                android.util.Log.e(com.android.server.sensorprivacy.PersistedState.LOG_TAG, "Unable to get users.", e);
            }
            for (int userId : users) {
                for (int j = 0; j < version0.mIndividualEnabled.size(); j++) {
                    int sensor = version0.mIndividualEnabled.keyAt(j);
                    com.android.server.sensorprivacy.SensorState sensorState = (com.android.server.sensorprivacy.SensorState) version0.mIndividualEnabled.valueAt(j);
                    result.addState(userId, sensor, sensorState.isEnabled());
                }
            }
            return result;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addState(int userId, int sensor, boolean enabled) {
            android.util.SparseArray<com.android.server.sensorprivacy.SensorState> userIndividualSensorEnabled = this.mIndividualEnabled.get(userId, new android.util.SparseArray<>());
            this.mIndividualEnabled.put(userId, userIndividualSensorEnabled);
            userIndividualSensorEnabled.put(sensor, new com.android.server.sensorprivacy.SensorState(enabled));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void upgrade() {
        }
    }

    private static class PVersion2 {
        private android.util.ArrayMap<com.android.server.sensorprivacy.PersistedState.TypeUserSensor, com.android.server.sensorprivacy.SensorState> mStates;

        private PVersion2(int version) {
            this.mStates = new android.util.ArrayMap<>();
            if (version != 2) {
                throw new java.lang.RuntimeException("Only version 2 supported");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static com.android.server.sensorprivacy.PersistedState.PVersion2 fromPVersion1(com.android.server.sensorprivacy.PersistedState.PVersion1 version1) {
            version1.upgrade();
            com.android.server.sensorprivacy.PersistedState.PVersion2 result = new com.android.server.sensorprivacy.PersistedState.PVersion2(2);
            android.util.SparseArray<android.util.SparseArray<com.android.server.sensorprivacy.SensorState>> individualEnabled = version1.mIndividualEnabled;
            int numUsers = individualEnabled.size();
            for (int i = 0; i < numUsers; i++) {
                int userId = individualEnabled.keyAt(i);
                android.util.SparseArray<com.android.server.sensorprivacy.SensorState> userIndividualEnabled = individualEnabled.valueAt(i);
                int numSensors = userIndividualEnabled.size();
                for (int j = 0; j < numSensors; j++) {
                    int sensor = userIndividualEnabled.keyAt(j);
                    com.android.server.sensorprivacy.SensorState sensorState = userIndividualEnabled.valueAt(j);
                    result.addState(1, userId, sensor, sensorState.getState(), sensorState.getLastChange());
                }
            }
            return result;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addState(int toggleType, int userId, int sensor, int state, long lastChange) {
            this.mStates.put(new com.android.server.sensorprivacy.PersistedState.TypeUserSensor(toggleType, userId, sensor), new com.android.server.sensorprivacy.SensorState(state, lastChange));
        }
    }

    public void resetForTesting() {
        this.mStates = new android.util.ArrayMap<>();
    }
}
