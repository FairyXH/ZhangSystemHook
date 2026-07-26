package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
class SenderFilter {
    private static final java.lang.String ATTR_TYPE = "type";
    public static final com.android.server.firewall.FilterFactory FACTORY = new com.android.server.firewall.FilterFactory("sender") { // from class: com.android.server.firewall.SenderFilter.1
        @Override // com.android.server.firewall.FilterFactory
        public com.android.server.firewall.Filter newFilter(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.lang.String typeString = parser.getAttributeValue(null, "type");
            if (typeString == null) {
                throw new org.xmlpull.v1.XmlPullParserException("type attribute must be specified for <sender>", parser, null);
            }
            if (typeString.equals("system")) {
                return com.android.server.firewall.SenderFilter.SYSTEM;
            }
            if (typeString.equals(com.android.server.firewall.SenderFilter.VAL_SIGNATURE)) {
                return com.android.server.firewall.SenderFilter.SIGNATURE;
            }
            if (typeString.equals(com.android.server.firewall.SenderFilter.VAL_SYSTEM_OR_SIGNATURE)) {
                return com.android.server.firewall.SenderFilter.SYSTEM_OR_SIGNATURE;
            }
            if (typeString.equals("userId")) {
                return com.android.server.firewall.SenderFilter.USER_ID;
            }
            throw new org.xmlpull.v1.XmlPullParserException("Invalid type attribute for <sender>: " + typeString, parser, null);
        }
    };
    private static final com.android.server.firewall.Filter SIGNATURE = new com.android.server.firewall.Filter() { // from class: com.android.server.firewall.SenderFilter.2
        @Override // com.android.server.firewall.Filter
        public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
            return ifw.signaturesMatch(callerUid, receivingUid);
        }
    };
    private static final com.android.server.firewall.Filter SYSTEM = new com.android.server.firewall.Filter() { // from class: com.android.server.firewall.SenderFilter.3
        @Override // com.android.server.firewall.Filter
        public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
            return com.android.server.firewall.SenderFilter.isPrivilegedApp(ifw.getPackageManager(), callerUid, callerPid);
        }
    };
    private static final com.android.server.firewall.Filter SYSTEM_OR_SIGNATURE = new com.android.server.firewall.Filter() { // from class: com.android.server.firewall.SenderFilter.4
        @Override // com.android.server.firewall.Filter
        public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
            return com.android.server.firewall.SenderFilter.isPrivilegedApp(ifw.getPackageManager(), callerUid, callerPid) || ifw.signaturesMatch(callerUid, receivingUid);
        }
    };
    private static final com.android.server.firewall.Filter USER_ID = new com.android.server.firewall.Filter() { // from class: com.android.server.firewall.SenderFilter.5
        @Override // com.android.server.firewall.Filter
        public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
            return ifw.checkComponentPermission(null, callerPid, callerUid, receivingUid, false);
        }
    };
    private static final java.lang.String VAL_SIGNATURE = "signature";
    private static final java.lang.String VAL_SYSTEM = "system";
    private static final java.lang.String VAL_SYSTEM_OR_SIGNATURE = "system|signature";
    private static final java.lang.String VAL_USER_ID = "userId";

    SenderFilter() {
    }

    static boolean isPrivilegedApp(android.content.pm.PackageManagerInternal pmi, int callerUid, int callerPid) {
        if (callerUid == 1000 || callerUid == 0 || callerPid == android.os.Process.myPid() || callerPid == 0) {
            return true;
        }
        return pmi.isUidPrivileged(callerUid);
    }
}
