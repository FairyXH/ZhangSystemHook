package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public interface DomainVerificationManagerInternal {
    public static final int APPROVAL_LEVEL_DISABLED = -3;
    public static final int APPROVAL_LEVEL_INSTANT_APP = 5;
    public static final int APPROVAL_LEVEL_LEGACY_ALWAYS = 2;
    public static final int APPROVAL_LEVEL_LEGACY_ASK = 1;
    public static final int APPROVAL_LEVEL_NONE = 0;
    public static final int APPROVAL_LEVEL_NOT_INSTALLED = -4;
    public static final int APPROVAL_LEVEL_SELECTION = 3;
    public static final int APPROVAL_LEVEL_UNDECLARED = -2;
    public static final int APPROVAL_LEVEL_UNVERIFIED = -1;
    public static final int APPROVAL_LEVEL_VERIFIED = 4;
    public static final java.util.UUID DISABLED_ID = new java.util.UUID(0, 0);

    public @interface ApprovalLevel {
    }

    public interface Connection extends com.android.server.pm.verify.domain.DomainVerificationEnforcer.Callback {
        int[] getAllUserIds();

        int getCallingUid();

        int getCallingUserId();

        void schedule(int i, java.lang.Object obj);

        void scheduleWriteSettings();

        com.android.server.pm.Computer snapshot();
    }

    void addLegacySetting(java.lang.String str, android.content.pm.IntentFilterVerificationInfo intentFilterVerificationInfo);

    void addPackage(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, android.content.pm.verify.domain.DomainSet domainSet);

    int approvalLevelForDomain(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, android.content.Intent intent, long j, int i);

    void clearPackage(java.lang.String str);

    void clearPackageForUser(java.lang.String str, int i);

    void clearUser(int i);

    android.util.Pair<java.util.List<android.content.pm.ResolveInfo>, java.lang.Integer> filterToApprovedApp(android.content.Intent intent, java.util.List<android.content.pm.ResolveInfo> list, int i, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> function);

    java.util.UUID generateNewId();

    com.android.server.pm.verify.domain.DomainVerificationCollector getCollector();

    android.content.pm.verify.domain.DomainVerificationInfo getDomainVerificationInfo(java.lang.String str) throws android.content.pm.PackageManager.NameNotFoundException;

    java.util.UUID getDomainVerificationInfoId(java.lang.String str);

    int getLegacyState(java.lang.String str, int i);

    com.android.server.pm.verify.domain.proxy.DomainVerificationProxy getProxy();

    com.android.server.pm.verify.domain.DomainVerificationShell getShell();

    void migrateState(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, com.android.server.pm.pkg.PackageStateInternal packageStateInternal2, android.content.pm.verify.domain.DomainSet domainSet);

    void printState(com.android.server.pm.Computer computer, android.util.IndentingPrintWriter indentingPrintWriter, java.lang.String str, java.lang.Integer num) throws android.content.pm.PackageManager.NameNotFoundException;

    void readLegacySettings(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;

    void readSettings(com.android.server.pm.Computer computer, com.android.modules.utils.TypedXmlPullParser typedXmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;

    void restoreSettings(com.android.server.pm.Computer computer, com.android.modules.utils.TypedXmlPullParser typedXmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;

    boolean runMessage(int i, java.lang.Object obj);

    void setConnection(com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection connection);

    int setDomainVerificationStatusInternal(int i, java.util.UUID uuid, java.util.Set<java.lang.String> set, int i2) throws android.content.pm.PackageManager.NameNotFoundException;

    boolean setLegacyUserState(java.lang.String str, int i, int i2);

    void setProxy(com.android.server.pm.verify.domain.proxy.DomainVerificationProxy domainVerificationProxy);

    void writeSettings(com.android.server.pm.Computer computer, com.android.modules.utils.TypedXmlSerializer typedXmlSerializer, boolean z, int i) throws java.io.IOException;

    static java.lang.String approvalLevelToDebugString(int level) {
        switch (level) {
            case -4:
                return "NOT_INSTALLED";
            case -3:
                return "DISABLED";
            case -2:
                return "UNDECLARED";
            case -1:
                return "UNVERIFIED";
            case 0:
                return "NONE";
            case 1:
                return "LEGACY_ASK";
            case 2:
                return "LEGACY_ALWAYS";
            case 3:
                return "USER_SELECTION";
            case 4:
                return "VERIFIED";
            case 5:
                return "INSTANT_APP";
            default:
                return "UNKNOWN";
        }
    }
}
