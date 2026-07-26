package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class IntegerPolicySerializer extends com.android.server.devicepolicy.PolicySerializer<java.lang.Integer> {
    private static final java.lang.String ATTR_VALUE = "value";
    private static final java.lang.String TAG = "IntegerPolicySerializer";

    IntegerPolicySerializer() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer, java.lang.Integer value) throws java.io.IOException {
        java.util.Objects.requireNonNull(value);
        serializer.attributeInt((java.lang.String) null, ATTR_VALUE, value.intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public android.app.admin.IntegerPolicyValue readFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
        try {
            return new android.app.admin.IntegerPolicyValue(parser.getAttributeInt((java.lang.String) null, ATTR_VALUE));
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(TAG, "Error parsing Integer policy value", e);
            return null;
        }
    }
}
