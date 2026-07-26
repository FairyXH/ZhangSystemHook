package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
class SenderPermissionFilter implements com.android.server.firewall.Filter {
    private static final java.lang.String ATTR_NAME = "name";
    public static final com.android.server.firewall.FilterFactory FACTORY = new com.android.server.firewall.FilterFactory("sender-permission") { // from class: com.android.server.firewall.SenderPermissionFilter.1
        @Override // com.android.server.firewall.FilterFactory
        public com.android.server.firewall.Filter newFilter(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.lang.String permission = parser.getAttributeValue(null, "name");
            if (permission == null) {
                throw new org.xmlpull.v1.XmlPullParserException("Permission name must be specified.", parser, null);
            }
            return new com.android.server.firewall.SenderPermissionFilter(permission);
        }
    };
    private final java.lang.String mPermission;

    private SenderPermissionFilter(java.lang.String permission) {
        this.mPermission = permission;
    }

    @Override // com.android.server.firewall.Filter
    public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
        return ifw.checkComponentPermission(this.mPermission, callerPid, callerUid, receivingUid, true);
    }
}
