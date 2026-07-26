package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class LockTaskPolicySerializer extends com.android.server.devicepolicy.PolicySerializer<android.app.admin.LockTaskPolicy> {
    private static final java.lang.String ATTR_FLAGS = "flags";
    private static final java.lang.String ATTR_PACKAGES = "packages";
    private static final java.lang.String ATTR_PACKAGES_SEPARATOR = ";";
    private static final java.lang.String TAG = "LockTaskPolicySerializer";

    LockTaskPolicySerializer() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer, android.app.admin.LockTaskPolicy value) throws java.io.IOException {
        java.util.Objects.requireNonNull(value);
        serializer.attribute((java.lang.String) null, ATTR_PACKAGES, java.lang.String.join(ATTR_PACKAGES_SEPARATOR, value.getPackages()));
        serializer.attributeInt((java.lang.String) null, ATTR_FLAGS, value.getFlags());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public android.app.admin.LockTaskPolicy readFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
        java.lang.String packagesStr = parser.getAttributeValue((java.lang.String) null, ATTR_PACKAGES);
        if (packagesStr == null) {
            android.util.Log.e(TAG, "Error parsing LockTask policy value.");
            return null;
        }
        java.util.Set<java.lang.String> packages = java.util.Set.of((java.lang.Object[]) packagesStr.split(ATTR_PACKAGES_SEPARATOR));
        try {
            int flags = parser.getAttributeInt((java.lang.String) null, ATTR_FLAGS);
            return new android.app.admin.LockTaskPolicy(packages, flags);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(TAG, "Error parsing LockTask policy value", e);
            return null;
        }
    }
}
