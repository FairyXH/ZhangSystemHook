package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class PackageSetPolicySerializer extends com.android.server.devicepolicy.PolicySerializer<java.util.Set<java.lang.String>> {
    private static final java.lang.String ATTR_VALUES = "strings";
    private static final java.lang.String ATTR_VALUES_SEPARATOR = ";";

    PackageSetPolicySerializer() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer, java.util.Set<java.lang.String> value) throws java.io.IOException {
        java.util.Objects.requireNonNull(value);
        serializer.attribute((java.lang.String) null, ATTR_VALUES, java.lang.String.join(ATTR_VALUES_SEPARATOR, value));
    }

    @Override // com.android.server.devicepolicy.PolicySerializer
    android.app.admin.PolicyValue<java.util.Set<java.lang.String>> readFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
        java.lang.String valuesStr = parser.getAttributeValue((java.lang.String) null, ATTR_VALUES);
        if (valuesStr == null) {
            android.util.Log.e("DevicePolicyEngine", "Error parsing PackageSet policy value.");
            return null;
        }
        java.util.Set<java.lang.String> values = java.util.Set.of((java.lang.Object[]) valuesStr.split(ATTR_VALUES_SEPARATOR));
        return new android.app.admin.PackageSetPolicyValue(values);
    }
}
