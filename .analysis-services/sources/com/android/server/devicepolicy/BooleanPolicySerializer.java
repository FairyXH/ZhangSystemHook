package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class BooleanPolicySerializer extends com.android.server.devicepolicy.PolicySerializer<java.lang.Boolean> {
    private static final java.lang.String ATTR_VALUE = "value";
    private static final java.lang.String TAG = "BooleanPolicySerializer";

    BooleanPolicySerializer() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer, java.lang.Boolean value) throws java.io.IOException {
        java.util.Objects.requireNonNull(value);
        serializer.attributeBoolean((java.lang.String) null, ATTR_VALUE, value.booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public android.app.admin.BooleanPolicyValue readFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
        try {
            return new android.app.admin.BooleanPolicyValue(parser.getAttributeBoolean((java.lang.String) null, ATTR_VALUE));
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(TAG, "Error parsing Boolean policy value", e);
            return null;
        }
    }
}
