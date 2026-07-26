package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class HostingRecord {
    private static final int APP_ZYGOTE = 2;
    public static final java.lang.String HOSTING_TYPE_ACTIVITY = "activity";
    public static final java.lang.String HOSTING_TYPE_ADDED_APPLICATION = "added application";
    public static final java.lang.String HOSTING_TYPE_BACKUP = "backup";
    public static final java.lang.String HOSTING_TYPE_BROADCAST = "broadcast";
    public static final java.lang.String HOSTING_TYPE_CONTENT_PROVIDER = "content provider";
    public static final java.lang.String HOSTING_TYPE_EMPTY = "";
    public static final java.lang.String HOSTING_TYPE_LINK_FAIL = "link fail";
    public static final java.lang.String HOSTING_TYPE_NEXT_ACTIVITY = "next-activity";
    public static final java.lang.String HOSTING_TYPE_NEXT_TOP_ACTIVITY = "next-top-activity";
    public static final java.lang.String HOSTING_TYPE_ON_HOLD = "on-hold";
    public static final java.lang.String HOSTING_TYPE_RESTART = "restart";
    public static final java.lang.String HOSTING_TYPE_SERVICE = "service";
    public static final java.lang.String HOSTING_TYPE_SYSTEM = "system";
    public static final java.lang.String HOSTING_TYPE_TOP_ACTIVITY = "top-activity";
    private static final int REGULAR_ZYGOTE = 0;
    public static final java.lang.String TRIGGER_TYPE_ALARM = "alarm";
    public static final java.lang.String TRIGGER_TYPE_JOB = "job";
    public static final java.lang.String TRIGGER_TYPE_PUSH_MESSAGE = "push_message";
    public static final java.lang.String TRIGGER_TYPE_PUSH_MESSAGE_OVER_QUOTA = "push_message_over_quota";
    public static final java.lang.String TRIGGER_TYPE_UNKNOWN = "unknown";
    private static final int WEBVIEW_ZYGOTE = 1;
    private final java.lang.String mAction;
    private final java.lang.String mDefiningPackageName;
    private final java.lang.String mDefiningProcessName;
    private final int mDefiningUid;
    private final java.lang.String mHostingName;
    private com.android.server.am.IHostingRecordExt mHostingRecordExt;
    private final java.lang.String mHostingType;
    private final int mHostingZygote;
    private final boolean mIsTopApp;
    private final java.lang.String mTriggerType;
    private com.android.server.am.HostingRecord.HostingRecordWrapper mWrapper;

    public com.android.server.am.IHostingRecordWrapper getWrapper() {
        return this.mWrapper;
    }

    private class HostingRecordWrapper implements com.android.server.am.IHostingRecordWrapper {
        private HostingRecordWrapper() {
        }

        @Override // com.android.server.am.IHostingRecordWrapper
        public com.android.server.am.IHostingRecordExt getExtImpl() {
            return com.android.server.am.HostingRecord.this.mHostingRecordExt;
        }
    }

    public HostingRecord(java.lang.String hostingType) {
        this(hostingType, null, 0, null, -1, false, null, null, "unknown");
    }

    public HostingRecord(java.lang.String hostingType, android.content.ComponentName hostingName) {
        this(hostingType, hostingName, 0);
    }

    public HostingRecord(java.lang.String hostingType, android.content.ComponentName hostingName, java.lang.String action, java.lang.String triggerType) {
        this(hostingType, hostingName.toShortString(), 0, null, -1, false, null, action, triggerType);
    }

    public HostingRecord(java.lang.String hostingType, android.content.ComponentName hostingName, java.lang.String definingPackageName, int definingUid, java.lang.String definingProcessName, java.lang.String triggerType) {
        this(hostingType, hostingName.toShortString(), 0, definingPackageName, definingUid, false, definingProcessName, null, triggerType);
    }

    public HostingRecord(java.lang.String hostingType, android.content.ComponentName hostingName, boolean isTopApp) {
        this(hostingType, hostingName.toShortString(), 0, null, -1, isTopApp, null, null, "unknown");
    }

    public HostingRecord(java.lang.String hostingType, java.lang.String hostingName) {
        this(hostingType, hostingName, 0);
    }

    private HostingRecord(java.lang.String hostingType, android.content.ComponentName hostingName, int hostingZygote) {
        this(hostingType, hostingName.toShortString(), hostingZygote);
    }

    private HostingRecord(java.lang.String hostingType, java.lang.String hostingName, int hostingZygote) {
        this(hostingType, hostingName, hostingZygote, null, -1, false, null, null, "unknown");
    }

    private HostingRecord(java.lang.String hostingType, java.lang.String hostingName, int hostingZygote, java.lang.String definingPackageName, int definingUid, boolean isTopApp, java.lang.String definingProcessName, java.lang.String action, java.lang.String triggerType) {
        this.mWrapper = new com.android.server.am.HostingRecord.HostingRecordWrapper();
        this.mHostingRecordExt = (com.android.server.am.IHostingRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IHostingRecordExt.class).base(this).create();
        this.mHostingType = hostingType;
        this.mHostingName = hostingName;
        this.mHostingZygote = hostingZygote;
        this.mDefiningPackageName = definingPackageName;
        this.mDefiningUid = definingUid;
        this.mIsTopApp = isTopApp;
        this.mDefiningProcessName = definingProcessName;
        this.mAction = action;
        this.mTriggerType = triggerType;
    }

    public java.lang.String getType() {
        return this.mHostingType;
    }

    public java.lang.String getName() {
        return this.mHostingName;
    }

    public boolean isTopApp() {
        return this.mIsTopApp;
    }

    public int getDefiningUid() {
        return this.mDefiningUid;
    }

    public java.lang.String getDefiningPackageName() {
        return this.mDefiningPackageName;
    }

    public java.lang.String getDefiningProcessName() {
        return this.mDefiningProcessName;
    }

    public java.lang.String getAction() {
        return this.mAction;
    }

    public java.lang.String getTriggerType() {
        return this.mTriggerType;
    }

    public static com.android.server.am.HostingRecord byWebviewZygote(android.content.ComponentName hostingName, java.lang.String definingPackageName, int definingUid, java.lang.String definingProcessName) {
        return new com.android.server.am.HostingRecord("", hostingName.toShortString(), 1, definingPackageName, definingUid, false, definingProcessName, null, "unknown");
    }

    public static com.android.server.am.HostingRecord byAppZygote(android.content.ComponentName hostingName, java.lang.String definingPackageName, int definingUid, java.lang.String definingProcessName) {
        return new com.android.server.am.HostingRecord("", hostingName.toShortString(), 2, definingPackageName, definingUid, false, definingProcessName, null, "unknown");
    }

    public boolean usesAppZygote() {
        return this.mHostingZygote == 2;
    }

    public boolean usesWebviewZygote() {
        return this.mHostingZygote == 1;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getHostingTypeIdStatsd(java.lang.String r15) {
        /*
            Method dump skipped, instruction units count: 294
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.HostingRecord.getHostingTypeIdStatsd(java.lang.String):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getTriggerTypeForStatsd(java.lang.String r5) {
        /*
            int r0 = r5.hashCode()
            r1 = 0
            r2 = 3
            r3 = 2
            r4 = 1
            switch(r0) {
                case -2000959542: goto L2c;
                case 105405: goto L21;
                case 92895825: goto L17;
                case 679713762: goto Lc;
                default: goto Lb;
            }
        Lb:
            goto L37
        Lc:
            java.lang.String r0 = "push_message"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto Lb
            r0 = r4
            goto L38
        L17:
            java.lang.String r0 = "alarm"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto Lb
            r0 = r1
            goto L38
        L21:
            java.lang.String r0 = "job"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto Lb
            r0 = r2
            goto L38
        L2c:
            java.lang.String r0 = "push_message_over_quota"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto Lb
            r0 = r3
            goto L38
        L37:
            r0 = -1
        L38:
            switch(r0) {
                case 0: goto L40;
                case 1: goto L3f;
                case 2: goto L3e;
                case 3: goto L3c;
                default: goto L3b;
            }
        L3b:
            return r1
        L3c:
            r0 = 4
            return r0
        L3e:
            return r2
        L3f:
            return r3
        L40:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.HostingRecord.getTriggerTypeForStatsd(java.lang.String):int");
    }

    private static boolean isTypeActivity(java.lang.String hostingType) {
        return HOSTING_TYPE_ACTIVITY.equals(hostingType) || HOSTING_TYPE_NEXT_ACTIVITY.equals(hostingType) || HOSTING_TYPE_NEXT_TOP_ACTIVITY.equals(hostingType) || HOSTING_TYPE_TOP_ACTIVITY.equals(hostingType);
    }

    public boolean isTypeActivity() {
        return isTypeActivity(this.mHostingType);
    }
}
