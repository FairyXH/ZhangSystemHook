package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class ComponentNamePolicySerializer extends com.android.server.devicepolicy.PolicySerializer<android.content.ComponentName> {
    private static final java.lang.String ATTR_CLASS_NAME = "class-name";
    private static final java.lang.String ATTR_PACKAGE_NAME = "package-name";
    private static final java.lang.String TAG = "ComponentNamePolicySerializer";

    ComponentNamePolicySerializer() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer, android.content.ComponentName value) throws java.io.IOException {
        java.util.Objects.requireNonNull(value);
        serializer.attribute((java.lang.String) null, ATTR_PACKAGE_NAME, value.getPackageName());
        serializer.attribute((java.lang.String) null, ATTR_CLASS_NAME, value.getClassName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public android.app.admin.ComponentNamePolicyValue readFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
        java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, ATTR_PACKAGE_NAME);
        java.lang.String className = parser.getAttributeValue((java.lang.String) null, ATTR_CLASS_NAME);
        if (packageName == null || className == null) {
            android.util.Log.e(TAG, "Error parsing ComponentName policy.");
            return null;
        }
        return new android.app.admin.ComponentNamePolicyValue(new android.content.ComponentName(packageName, className));
    }
}
