package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintUserState extends com.android.server.biometrics.sensors.BiometricUserState<android.hardware.fingerprint.Fingerprint> {
    private static final java.lang.String ATTR_DEVICE_ID = "deviceId";
    private static final java.lang.String ATTR_FINGER_ID = "fingerId";
    private static final java.lang.String ATTR_GROUP_ID = "groupId";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String TAG = "FingerprintState";
    private static final java.lang.String TAG_FINGERPRINT = "fingerprint";
    private static final java.lang.String TAG_FINGERPRINTS = "fingerprints";
    public com.android.server.biometrics.sensors.fingerprint.IFingerprintUserStateExt mFingerprintUserStateExt;

    private void initUserStateExt() {
        if (this.mFingerprintUserStateExt == null) {
            this.mFingerprintUserStateExt = (com.android.server.biometrics.sensors.fingerprint.IFingerprintUserStateExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.IFingerprintUserStateExt.class).base(this).create();
        }
    }

    public FingerprintUserState(android.content.Context context, int userId, java.lang.String fileName) {
        super(context, userId, fileName);
        this.mFingerprintUserStateExt = null;
        initUserStateExt();
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected java.lang.String getBiometricsTag() {
        return TAG_FINGERPRINTS;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected int getNameTemplateResource() {
        return android.R.string.fingerprint_frr_notification_title;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected java.util.ArrayList<android.hardware.fingerprint.Fingerprint> getCopy(java.util.ArrayList<android.hardware.fingerprint.Fingerprint> array) {
        java.util.ArrayList<android.hardware.fingerprint.Fingerprint> result = new java.util.ArrayList<>();
        for (android.hardware.fingerprint.Fingerprint fp : array) {
            result.add(this.mFingerprintUserStateExt.getCopyFingerprint(fp));
        }
        return result;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected void doWriteState(com.android.modules.utils.TypedXmlSerializer serializer) throws java.lang.Exception {
        java.util.ArrayList<android.hardware.fingerprint.Fingerprint> fingerprints;
        synchronized (this) {
            fingerprints = getCopy(this.mBiometrics);
        }
        serializer.startTag((java.lang.String) null, TAG_FINGERPRINTS);
        int count = fingerprints.size();
        for (int i = 0; i < count; i++) {
            android.hardware.fingerprint.Fingerprint fp = fingerprints.get(i);
            serializer.startTag((java.lang.String) null, TAG_FINGERPRINT);
            serializer.attributeInt((java.lang.String) null, ATTR_FINGER_ID, fp.getBiometricId());
            serializer.attribute((java.lang.String) null, "name", fp.getName().toString());
            serializer.attributeInt((java.lang.String) null, ATTR_GROUP_ID, fp.getGroupId());
            serializer.attributeLong((java.lang.String) null, ATTR_DEVICE_ID, fp.getDeviceId());
            this.mFingerprintUserStateExt.attributeFingerprint(serializer, fp);
            serializer.endTag((java.lang.String) null, TAG_FINGERPRINT);
        }
        serializer.endTag((java.lang.String) null, TAG_FINGERPRINTS);
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected void parseBiometricsLocked(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next != 1) {
                if (next != 3 || typedXmlPullParser.getDepth() > depth) {
                    if (next != 3 && next != 4 && typedXmlPullParser.getName().equals(TAG_FINGERPRINT)) {
                        java.lang.String attributeValue = typedXmlPullParser.getAttributeValue((java.lang.String) null, "name");
                        int attributeInt = typedXmlPullParser.getAttributeInt((java.lang.String) null, ATTR_GROUP_ID);
                        int attributeInt2 = typedXmlPullParser.getAttributeInt((java.lang.String) null, ATTR_FINGER_ID);
                        long attributeLong = typedXmlPullParser.getAttributeLong((java.lang.String) null, ATTR_DEVICE_ID);
                        initUserStateExt();
                        this.mBiometrics.add((T) this.mFingerprintUserStateExt.parseBiometricsLocked(typedXmlPullParser, new android.hardware.fingerprint.Fingerprint(attributeValue, attributeInt, attributeInt2, attributeLong)));
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }
}
