package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class LongPolicySerializer extends com.android.server.devicepolicy.PolicySerializer<java.lang.Long> {
    private static final java.lang.String ATTR_VALUE = "value";
    private static final java.lang.String TAG = "LongPolicySerializer";

    LongPolicySerializer() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer, java.lang.Long value) throws java.io.IOException {
        java.util.Objects.requireNonNull(value);
        serializer.attributeLong((java.lang.String) null, ATTR_VALUE, value.longValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public android.app.admin.LongPolicyValue readFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
        try {
            return new android.app.admin.LongPolicyValue(parser.getAttributeLong((java.lang.String) null, ATTR_VALUE));
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(TAG, "Error parsing Long policy value", e);
            return null;
        }
    }
}
