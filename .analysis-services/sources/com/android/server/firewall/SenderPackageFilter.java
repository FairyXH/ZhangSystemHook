package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
public class SenderPackageFilter implements com.android.server.firewall.Filter {
    private static final java.lang.String ATTR_NAME = "name";
    public static final com.android.server.firewall.FilterFactory FACTORY = new com.android.server.firewall.FilterFactory("sender-package") { // from class: com.android.server.firewall.SenderPackageFilter.1
        @Override // com.android.server.firewall.FilterFactory
        public com.android.server.firewall.Filter newFilter(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.lang.String packageName = parser.getAttributeValue(null, "name");
            if (packageName == null) {
                throw new org.xmlpull.v1.XmlPullParserException("A package name must be specified.", parser, null);
            }
            return new com.android.server.firewall.SenderPackageFilter(packageName);
        }
    };
    public final java.lang.String mPackageName;

    public SenderPackageFilter(java.lang.String packageName) {
        this.mPackageName = packageName;
    }

    @Override // com.android.server.firewall.Filter
    public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
        android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
        int packageUid = -1;
        try {
            packageUid = pm.getPackageUid(this.mPackageName, 4194304L, 0);
        } catch (android.os.RemoteException e) {
        }
        if (packageUid == -1) {
            return false;
        }
        return android.os.UserHandle.isSameApp(packageUid, callerUid);
    }
}
