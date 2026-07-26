package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public final class SuspendParams {
    private static final java.lang.String ATTR_QUARANTINED = "quarantined";
    private static final java.lang.String LOG_TAG = "FrameworkPackageUserState";
    private static final java.lang.String TAG_APP_EXTRAS = "app-extras";
    private static final java.lang.String TAG_DIALOG_INFO = "dialog-info";
    private static final java.lang.String TAG_LAUNCHER_EXTRAS = "launcher-extras";
    private final android.os.PersistableBundle mAppExtras;
    private final android.content.pm.SuspendDialogInfo mDialogInfo;
    private final android.os.PersistableBundle mLauncherExtras;
    private final boolean mQuarantined;

    public SuspendParams(android.content.pm.SuspendDialogInfo dialogInfo, android.os.PersistableBundle appExtras, android.os.PersistableBundle launcherExtras) {
        this(dialogInfo, appExtras, launcherExtras, false);
    }

    public SuspendParams(android.content.pm.SuspendDialogInfo dialogInfo, android.os.PersistableBundle appExtras, android.os.PersistableBundle launcherExtras, boolean quarantined) {
        this.mDialogInfo = dialogInfo;
        this.mAppExtras = appExtras;
        this.mLauncherExtras = launcherExtras;
        this.mQuarantined = quarantined;
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.pm.pkg.SuspendParams)) {
            return false;
        }
        com.android.server.pm.pkg.SuspendParams other = (com.android.server.pm.pkg.SuspendParams) obj;
        return java.util.Objects.equals(this.mDialogInfo, other.mDialogInfo) && android.os.BaseBundle.kindofEquals(this.mAppExtras, other.mAppExtras) && android.os.BaseBundle.kindofEquals(this.mLauncherExtras, other.mLauncherExtras) && this.mQuarantined == other.mQuarantined;
    }

    public int hashCode() {
        int hashCode = java.util.Objects.hashCode(this.mDialogInfo);
        return (((((hashCode * 31) + (this.mAppExtras != null ? this.mAppExtras.size() : 0)) * 31) + (this.mLauncherExtras != null ? this.mLauncherExtras.size() : 0)) * 31) + java.lang.Boolean.hashCode(this.mQuarantined);
    }

    public void saveToXml(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
        out.attributeBoolean((java.lang.String) null, ATTR_QUARANTINED, this.mQuarantined);
        if (this.mDialogInfo != null) {
            out.startTag((java.lang.String) null, TAG_DIALOG_INFO);
            this.mDialogInfo.saveToXml(out);
            out.endTag((java.lang.String) null, TAG_DIALOG_INFO);
        }
        if (this.mAppExtras != null) {
            out.startTag((java.lang.String) null, TAG_APP_EXTRAS);
            try {
                this.mAppExtras.saveToXml(out);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.e(LOG_TAG, "Exception while trying to write appExtras. Will be lost on reboot", e);
            }
            out.endTag((java.lang.String) null, TAG_APP_EXTRAS);
        }
        if (this.mLauncherExtras != null) {
            out.startTag((java.lang.String) null, TAG_LAUNCHER_EXTRAS);
            try {
                this.mLauncherExtras.saveToXml(out);
            } catch (org.xmlpull.v1.XmlPullParserException e2) {
                android.util.Slog.e(LOG_TAG, "Exception while trying to write launcherExtras. Will be lost on reboot", e2);
            }
            out.endTag((java.lang.String) null, TAG_LAUNCHER_EXTRAS);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0034  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.pm.pkg.SuspendParams restoreFromXml(com.android.modules.utils.TypedXmlPullParser r11) throws java.io.IOException {
        /*
            java.lang.String r0 = "FrameworkPackageUserState"
            r1 = 0
            r2 = 0
            r3 = 0
            r4 = 0
            java.lang.String r5 = "quarantined"
            r6 = 0
            boolean r4 = r11.getAttributeBoolean(r4, r5, r6)
            int r5 = r11.getDepth()
        L12:
            int r7 = r11.next()     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            r8 = r7
            r9 = 1
            if (r7 == r9) goto L8b
            r7 = 3
            if (r8 != r7) goto L23
            int r10 = r11.getDepth()     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            if (r10 <= r5) goto L8b
        L23:
            if (r8 == r7) goto L12
            r7 = 4
            if (r8 != r7) goto L29
            goto L12
        L29:
            java.lang.String r7 = r11.getName()     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            int r10 = r7.hashCode()     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            switch(r10) {
                case -538220657: goto L4a;
                case -22768109: goto L40;
                case 1627485488: goto L35;
                default: goto L34;
            }     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
        L34:
            goto L53
        L35:
            java.lang.String r9 = "launcher-extras"
            boolean r7 = r7.equals(r9)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            if (r7 == 0) goto L34
            r9 = 2
            goto L54
        L40:
            java.lang.String r9 = "dialog-info"
            boolean r7 = r7.equals(r9)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            if (r7 == 0) goto L34
            r9 = r6
            goto L54
        L4a:
            java.lang.String r10 = "app-extras"
            boolean r7 = r7.equals(r10)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            if (r7 == 0) goto L34
            goto L54
        L53:
            r9 = -1
        L54:
            switch(r9) {
                case 0: goto L64;
                case 1: goto L5e;
                case 2: goto L58;
                default: goto L57;
            }     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
        L57:
            goto L6a
        L58:
            android.os.PersistableBundle r7 = android.os.PersistableBundle.restoreFromXml(r11)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            r3 = r7
            goto L8a
        L5e:
            android.os.PersistableBundle r7 = android.os.PersistableBundle.restoreFromXml(r11)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            r2 = r7
            goto L8a
        L64:
            android.content.pm.SuspendDialogInfo r7 = android.content.pm.SuspendDialogInfo.restoreFromXml(r11)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            r1 = r7
            goto L8a
        L6a:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            r7.<init>()     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            java.lang.String r9 = "Unknown tag "
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            java.lang.String r9 = r11.getName()     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            java.lang.String r9 = " in SuspendParams. Ignoring"
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            java.lang.String r7 = r7.toString()     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
            android.util.Slog.w(r0, r7)     // Catch: org.xmlpull.v1.XmlPullParserException -> L8c
        L8a:
            goto L12
        L8b:
            goto L92
        L8c:
            r6 = move-exception
            java.lang.String r7 = "Exception while trying to parse SuspendParams, some fields may default"
            android.util.Slog.e(r0, r7, r6)
        L92:
            com.android.server.pm.pkg.SuspendParams r0 = new com.android.server.pm.pkg.SuspendParams
            r0.<init>(r1, r2, r3, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.pkg.SuspendParams.restoreFromXml(com.android.modules.utils.TypedXmlPullParser):com.android.server.pm.pkg.SuspendParams");
    }

    public android.content.pm.SuspendDialogInfo getDialogInfo() {
        return this.mDialogInfo;
    }

    public android.os.PersistableBundle getAppExtras() {
        return this.mAppExtras;
    }

    public android.os.PersistableBundle getLauncherExtras() {
        return this.mLauncherExtras;
    }

    public boolean isQuarantined() {
        return this.mQuarantined;
    }
}
