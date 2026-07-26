package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public class FaceUserState extends com.android.server.biometrics.sensors.BiometricUserState<android.hardware.face.Face> {
    private static final java.lang.String ATTR_DEVICE_ID = "deviceId";
    private static final java.lang.String ATTR_FACE_ID = "faceId";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String TAG = "FaceState";
    private static final java.lang.String TAG_FACE = "face";
    private static final java.lang.String TAG_FACES = "faces";

    public FaceUserState(android.content.Context ctx, int userId, java.lang.String fileName) {
        super(ctx, userId, fileName);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected java.lang.String getBiometricsTag() {
        return TAG_FACES;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected int getNameTemplateResource() {
        return android.R.string.face_or_screen_lock_dialog_default_subtitle;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected java.util.ArrayList<android.hardware.face.Face> getCopy(java.util.ArrayList<android.hardware.face.Face> array) {
        java.util.ArrayList<android.hardware.face.Face> result = new java.util.ArrayList<>();
        for (android.hardware.face.Face f : array) {
            result.add(new android.hardware.face.Face(f.getName(), f.getBiometricId(), f.getDeviceId()));
        }
        return result;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected void doWriteState(com.android.modules.utils.TypedXmlSerializer serializer) throws java.lang.Exception {
        java.util.ArrayList<android.hardware.face.Face> faces;
        synchronized (this) {
            faces = getCopy(this.mBiometrics);
        }
        serializer.startTag((java.lang.String) null, TAG_FACES);
        int count = faces.size();
        for (int i = 0; i < count; i++) {
            android.hardware.face.Face f = faces.get(i);
            serializer.startTag((java.lang.String) null, TAG_FACE);
            serializer.attributeInt((java.lang.String) null, ATTR_FACE_ID, f.getBiometricId());
            serializer.attribute((java.lang.String) null, "name", f.getName().toString());
            serializer.attributeLong((java.lang.String) null, ATTR_DEVICE_ID, f.getDeviceId());
            serializer.endTag((java.lang.String) null, TAG_FACE);
        }
        serializer.endTag((java.lang.String) null, TAG_FACES);
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected void parseBiometricsLocked(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next != 1) {
                if (next != 3 || typedXmlPullParser.getDepth() > depth) {
                    if (next != 3 && next != 4 && typedXmlPullParser.getName().equals(TAG_FACE)) {
                        this.mBiometrics.add((T) new android.hardware.face.Face(typedXmlPullParser.getAttributeValue((java.lang.String) null, "name"), typedXmlPullParser.getAttributeInt((java.lang.String) null, ATTR_FACE_ID), typedXmlPullParser.getAttributeLong((java.lang.String) null, ATTR_DEVICE_ID)));
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
