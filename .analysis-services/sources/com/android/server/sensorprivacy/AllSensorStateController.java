package com.android.server.sensorprivacy;

/* JADX INFO: loaded from: classes3.dex */
class AllSensorStateController {
    private static final java.lang.String LOG_TAG = com.android.server.sensorprivacy.AllSensorStateController.class.getSimpleName();
    private static final java.lang.String SENSOR_PRIVACY_XML_FILE = "sensor_privacy.xml";
    private static final java.lang.String XML_ATTRIBUTE_ENABLED = "enabled";
    private static final java.lang.String XML_TAG_SENSOR_PRIVACY = "all-sensor-privacy";
    private static final java.lang.String XML_TAG_SENSOR_PRIVACY_LEGACY = "sensor-privacy";
    private static com.android.server.sensorprivacy.AllSensorStateController sInstance;
    private final android.util.AtomicFile mAtomicFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), SENSOR_PRIVACY_XML_FILE));
    private boolean mEnabled;
    private com.android.server.sensorprivacy.SensorPrivacyStateController.AllSensorPrivacyListener mListener;
    private android.os.Handler mListenerHandler;

    static com.android.server.sensorprivacy.AllSensorStateController getInstance() {
        if (sInstance == null) {
            sInstance = new com.android.server.sensorprivacy.AllSensorStateController();
        }
        return sInstance;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x003f, code lost:
    
        r7.mEnabled |= com.android.internal.util.XmlUtils.readBooleanAttribute(r2, "enabled", false);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private AllSensorStateController() {
        /*
            r7 = this;
            r7.<init>()
            android.util.AtomicFile r0 = new android.util.AtomicFile
            java.io.File r1 = new java.io.File
            java.io.File r2 = android.os.Environment.getDataSystemDirectory()
            java.lang.String r3 = "sensor_privacy.xml"
            r1.<init>(r2, r3)
            r0.<init>(r1)
            r7.mAtomicFile = r0
            android.util.AtomicFile r0 = r7.mAtomicFile
            boolean r0 = r0.exists()
            if (r0 != 0) goto L1f
            return
        L1f:
            r0 = 0
            android.util.AtomicFile r1 = r7.mAtomicFile     // Catch: java.lang.Throwable -> L8c
            java.io.FileInputStream r1 = r1.openRead()     // Catch: java.lang.Throwable -> L8c
            com.android.modules.utils.TypedXmlPullParser r2 = android.util.Xml.resolvePullParser(r1)     // Catch: java.lang.Throwable -> L80
        L2a:
            int r3 = r2.getEventType()     // Catch: java.lang.Throwable -> L80
            r4 = 1
            if (r3 == r4) goto L7a
            java.lang.String r3 = r2.getName()     // Catch: java.lang.Throwable -> L80
            java.lang.String r4 = "all-sensor-privacy"
            boolean r4 = r4.equals(r3)     // Catch: java.lang.Throwable -> L80
            java.lang.String r5 = "enabled"
            if (r4 == 0) goto L49
            boolean r4 = r7.mEnabled     // Catch: java.lang.Throwable -> L80
            boolean r5 = com.android.internal.util.XmlUtils.readBooleanAttribute(r2, r5, r0)     // Catch: java.lang.Throwable -> L80
            r4 = r4 | r5
            r7.mEnabled = r4     // Catch: java.lang.Throwable -> L80
            goto L7a
        L49:
            java.lang.String r4 = "sensor-privacy"
            boolean r4 = r4.equals(r3)     // Catch: java.lang.Throwable -> L80
            if (r4 == 0) goto L5b
            boolean r4 = r7.mEnabled     // Catch: java.lang.Throwable -> L80
            boolean r6 = com.android.internal.util.XmlUtils.readBooleanAttribute(r2, r5, r0)     // Catch: java.lang.Throwable -> L80
            r4 = r4 | r6
            r7.mEnabled = r4     // Catch: java.lang.Throwable -> L80
        L5b:
            java.lang.String r4 = "user"
            boolean r4 = r4.equals(r3)     // Catch: java.lang.Throwable -> L80
            if (r4 == 0) goto L76
            java.lang.String r4 = "id"
            r6 = -1
            int r4 = com.android.internal.util.XmlUtils.readIntAttribute(r2, r4, r6)     // Catch: java.lang.Throwable -> L80
            if (r4 != 0) goto L76
            boolean r6 = r7.mEnabled     // Catch: java.lang.Throwable -> L80
            boolean r5 = com.android.internal.util.XmlUtils.readBooleanAttribute(r2, r5)     // Catch: java.lang.Throwable -> L80
            r5 = r5 | r6
            r7.mEnabled = r5     // Catch: java.lang.Throwable -> L80
        L76:
            com.android.internal.util.XmlUtils.nextElement(r2)     // Catch: java.lang.Throwable -> L80
            goto L2a
        L7a:
            if (r1 == 0) goto L7f
            r1.close()     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> L8c
        L7f:
            goto L96
        L80:
            r2 = move-exception
            if (r1 == 0) goto L8b
            r1.close()     // Catch: java.lang.Throwable -> L87
            goto L8b
        L87:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> L8c
        L8b:
            throw r2     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> L8c
        L8c:
            r1 = move-exception
            java.lang.String r2 = com.android.server.sensorprivacy.AllSensorStateController.LOG_TAG
            java.lang.String r3 = "Caught an exception reading the state from storage: "
            android.util.Log.e(r2, r3, r1)
            r7.mEnabled = r0
        L96:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.sensorprivacy.AllSensorStateController.<init>():void");
    }

    public boolean getAllSensorStateLocked() {
        return this.mEnabled;
    }

    public void setAllSensorStateLocked(boolean enabled) {
        if (this.mEnabled != enabled) {
            this.mEnabled = enabled;
            if (this.mListener != null && this.mListenerHandler != null) {
                android.os.Handler handler = this.mListenerHandler;
                final com.android.server.sensorprivacy.SensorPrivacyStateController.AllSensorPrivacyListener allSensorPrivacyListener = this.mListener;
                java.util.Objects.requireNonNull(allSensorPrivacyListener);
                handler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.sensorprivacy.AllSensorStateController$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        allSensorPrivacyListener.onAllSensorPrivacyChanged(((java.lang.Boolean) obj).booleanValue());
                    }
                }, java.lang.Boolean.valueOf(enabled)));
            }
        }
    }

    void setAllSensorPrivacyListenerLocked(android.os.Handler handler, com.android.server.sensorprivacy.SensorPrivacyStateController.AllSensorPrivacyListener listener) {
        java.util.Objects.requireNonNull(handler);
        java.util.Objects.requireNonNull(listener);
        if (this.mListener != null) {
            throw new java.lang.IllegalStateException("Listener is already set");
        }
        this.mListener = listener;
        this.mListenerHandler = handler;
    }

    public void schedulePersistLocked() {
        com.android.server.IoThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.sensorprivacy.AllSensorStateController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.persist(((java.lang.Boolean) obj).booleanValue());
            }
        }, java.lang.Boolean.valueOf(this.mEnabled)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persist(boolean enabled) {
        java.io.FileOutputStream outputStream = null;
        try {
            outputStream = this.mAtomicFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(outputStream);
            serializer.startDocument((java.lang.String) null, true);
            serializer.startTag((java.lang.String) null, XML_TAG_SENSOR_PRIVACY);
            serializer.attributeBoolean((java.lang.String) null, "enabled", enabled);
            serializer.endTag((java.lang.String) null, XML_TAG_SENSOR_PRIVACY);
            serializer.endDocument();
            this.mAtomicFile.finishWrite(outputStream);
        } catch (java.io.IOException e) {
            android.util.Log.e(LOG_TAG, "Caught an exception persisting the sensor privacy state: ", e);
            this.mAtomicFile.failWrite(outputStream);
        }
    }

    void resetForTesting() {
        this.mListener = null;
        this.mListenerHandler = null;
        this.mEnabled = false;
    }

    void dumpLocked(com.android.internal.util.dump.DualDumpOutputStream dumpStream) {
    }
}
