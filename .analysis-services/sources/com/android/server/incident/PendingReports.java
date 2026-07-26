package com.android.server.incident;

/* JADX INFO: loaded from: classes2.dex */
class PendingReports {
    static final java.lang.String TAG = "IncidentCompanionService";
    private final android.app.AppOpsManager mAppOpsManager;
    private final android.content.Context mContext;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.permission.PermissionManager mPermissionManager;
    private final android.os.Handler mHandler = new android.os.Handler();
    private final com.android.server.incident.RequestQueue mRequestQueue = new com.android.server.incident.RequestQueue(this.mHandler);
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.ArrayList<com.android.server.incident.PendingReports.PendingReportRec> mPending = new java.util.ArrayList<>();
    private int mNextPendingId = 1;

    private final class PendingReportRec {
        public long addedRealtime;
        public long addedWalltime;
        public java.lang.String callingPackage;
        public int flags;
        public int id;
        public android.os.IIncidentAuthListener listener;
        public java.lang.String receiverClass;
        public java.lang.String reportId;

        PendingReportRec(java.lang.String callingPackage, java.lang.String receiverClass, java.lang.String reportId, int flags, android.os.IIncidentAuthListener listener) {
            int i = com.android.server.incident.PendingReports.this.mNextPendingId;
            com.android.server.incident.PendingReports.this.mNextPendingId = i + 1;
            this.id = i;
            this.callingPackage = callingPackage;
            this.flags = flags;
            this.listener = listener;
            this.addedRealtime = android.os.SystemClock.elapsedRealtime();
            this.addedWalltime = java.lang.System.currentTimeMillis();
            this.receiverClass = receiverClass;
            this.reportId = reportId;
        }

        android.net.Uri getUri() {
            android.net.Uri.Builder builder = new android.net.Uri.Builder().scheme(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT).authority("android.os.IncidentManager").path("/pending").appendQueryParameter("id", java.lang.Integer.toString(this.id)).appendQueryParameter("pkg", this.callingPackage).appendQueryParameter("flags", java.lang.Integer.toString(this.flags)).appendQueryParameter("t", java.lang.Long.toString(this.addedWalltime));
            if (this.receiverClass != null && this.receiverClass.length() > 0) {
                builder.appendQueryParameter("receiver", this.receiverClass);
            }
            if (this.reportId != null && this.reportId.length() > 0) {
                builder.appendQueryParameter(com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD, this.reportId);
            }
            return builder.build();
        }
    }

    PendingReports(android.content.Context context) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mAppOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        this.mPermissionManager = (android.permission.PermissionManager) context.getSystemService(android.permission.PermissionManager.class);
    }

    public void authorizeReport(final int callingUid, final java.lang.String callingPackage, final java.lang.String receiverClass, final java.lang.String reportId, final int flags, final android.os.IIncidentAuthListener listener) {
        this.mRequestQueue.enqueue(listener.asBinder(), true, new java.lang.Runnable() { // from class: com.android.server.incident.PendingReports$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$authorizeReport$0(callingUid, callingPackage, receiverClass, reportId, flags, listener);
            }
        });
    }

    public void cancelAuthorization(final android.os.IIncidentAuthListener listener) {
        this.mRequestQueue.enqueue(listener.asBinder(), false, new java.lang.Runnable() { // from class: com.android.server.incident.PendingReports$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelAuthorization$1(listener);
            }
        });
    }

    public java.util.List<java.lang.String> getPendingReports() {
        java.util.ArrayList<java.lang.String> result;
        synchronized (this.mLock) {
            int size = this.mPending.size();
            result = new java.util.ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                result.add(this.mPending.get(i).getUri().toString());
            }
        }
        return result;
    }

    public void approveReport(java.lang.String uri) {
        synchronized (this.mLock) {
            com.android.server.incident.PendingReports.PendingReportRec rec = findAndRemovePendingReportRecLocked(uri);
            if (rec == null) {
                android.util.Log.e(TAG, "confirmApproved: Couldn't find record for uri: " + uri);
                return;
            }
            sendBroadcast();
            android.util.Log.i(TAG, "Approved report: " + uri);
            try {
                rec.listener.onReportApproved();
            } catch (android.os.RemoteException ex) {
                android.util.Log.w(TAG, "Failed calling back for approval for: " + uri, ex);
            }
        }
    }

    public void denyReport(java.lang.String uri) {
        synchronized (this.mLock) {
            com.android.server.incident.PendingReports.PendingReportRec rec = findAndRemovePendingReportRecLocked(uri);
            if (rec == null) {
                android.util.Log.e(TAG, "confirmDenied: Couldn't find record for uri: " + uri);
                return;
            }
            sendBroadcast();
            android.util.Log.i(TAG, "Denied report: " + uri);
            try {
                rec.listener.onReportDenied();
            } catch (android.os.RemoteException ex) {
                android.util.Log.w(TAG, "Failed calling back for denial for: " + uri, ex);
            }
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (args.length == 0) {
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            synchronized (this.mLock) {
                int size = this.mPending.size();
                writer.println("mPending: (" + size + ")");
                for (int i = 0; i < size; i++) {
                    com.android.server.incident.PendingReports.PendingReportRec entry = this.mPending.get(i);
                    writer.println(java.lang.String.format("  %11d %s: %s", java.lang.Long.valueOf(entry.addedRealtime), df.format(new java.util.Date(entry.addedWalltime)), entry.getUri().toString()));
                }
            }
        }
    }

    public void onBootCompleted() {
        this.mRequestQueue.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:50:0x0114
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    /* JADX INFO: renamed from: authorizeReportImpl, reason: merged with bridge method [inline-methods] */
    public void lambda$authorizeReport$0(int r20, java.lang.String r21, java.lang.String r22, java.lang.String r23, int r24, final android.os.IIncidentAuthListener r25) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 321
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.incident.PendingReports.lambda$authorizeReport$0(int, java.lang.String, java.lang.String, java.lang.String, int, android.os.IIncidentAuthListener):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$authorizeReportImpl$2(android.os.IIncidentAuthListener listener, android.content.ComponentName receiver, int currentAdminUser) {
        android.util.Log.i(TAG, "Got death notification listener=" + listener);
        cancelReportImpl(listener, receiver, currentAdminUser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: cancelReportImpl, reason: merged with bridge method [inline-methods] */
    public void lambda$cancelAuthorization$1(android.os.IIncidentAuthListener listener) {
        int currentAdminUser = getCurrentUserIfAdmin();
        android.content.ComponentName receiver = getApproverComponent(currentAdminUser);
        if (currentAdminUser != -10000 && receiver != null) {
            cancelReportImpl(listener, receiver, currentAdminUser);
        }
    }

    private void cancelReportImpl(android.os.IIncidentAuthListener listener, android.content.ComponentName receiver, int user) {
        synchronized (this.mLock) {
            removePendingReportRecLocked(listener);
        }
        sendBroadcast(receiver, user);
    }

    private void sendBroadcast() {
        android.content.ComponentName receiver;
        int currentAdminUser = getCurrentUserIfAdmin();
        if (currentAdminUser == -10000 || (receiver = getApproverComponent(currentAdminUser)) == null) {
            return;
        }
        sendBroadcast(receiver, currentAdminUser);
    }

    private void sendBroadcast(android.content.ComponentName receiver, int currentUser) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.PENDING_INCIDENT_REPORTS_CHANGED");
        intent.setComponent(receiver);
        intent.addFlags(268435456);
        intent.addFlags(16777216);
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setBackgroundActivityStartsAllowed(true);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.of(currentUser), "android.permission.APPROVE_INCIDENT_REPORTS", options.toBundle());
    }

    private com.android.server.incident.PendingReports.PendingReportRec findAndRemovePendingReportRecLocked(java.lang.String uriString) {
        android.net.Uri uri = android.net.Uri.parse(uriString);
        try {
            java.lang.String idStr = uri.getQueryParameter("id");
            int id = java.lang.Integer.parseInt(idStr);
            java.util.Iterator<com.android.server.incident.PendingReports.PendingReportRec> i = this.mPending.iterator();
            while (i.hasNext()) {
                com.android.server.incident.PendingReports.PendingReportRec rec = i.next();
                if (rec.id == id) {
                    i.remove();
                    return rec;
                }
            }
            return null;
        } catch (java.lang.NumberFormatException e) {
            android.util.Log.w(TAG, "Can't parse id from: " + uriString);
            return null;
        }
    }

    private void removePendingReportRecLocked(android.os.IIncidentAuthListener listener) {
        java.util.Iterator<com.android.server.incident.PendingReports.PendingReportRec> i = this.mPending.iterator();
        while (i.hasNext()) {
            com.android.server.incident.PendingReports.PendingReportRec rec = i.next();
            if (rec.listener.asBinder() == listener.asBinder()) {
                android.util.Log.i(TAG, "  ...Removed PendingReportRec index=" + i + ": " + rec.getUri());
                i.remove();
            }
        }
    }

    private void denyReportBeforeAddingRec(android.os.IIncidentAuthListener listener, java.lang.String pkg) {
        try {
            listener.onReportDenied();
        } catch (android.os.RemoteException ex) {
            android.util.Log.w(TAG, "Failed calling back for denial for " + pkg, ex);
        }
    }

    private int getCurrentUserIfAdmin() {
        return com.android.server.incident.IncidentCompanionService.getCurrentUserIfAdmin();
    }

    private android.content.ComponentName getApproverComponent(int userId) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.PENDING_INCIDENT_REPORTS_CHANGED");
        java.util.List<android.content.pm.ResolveInfo> matches = this.mPackageManager.queryBroadcastReceiversAsUser(intent, 1835008, userId);
        if (matches.size() == 1) {
            return matches.get(0).getComponentInfo().getComponentName();
        }
        android.util.Log.w(TAG, "Didn't find exactly one BroadcastReceiver to handle android.intent.action.PENDING_INCIDENT_REPORTS_CHANGED. The report will be denied. size=" + matches.size() + ": matches=" + matches);
        return null;
    }

    private boolean isPackageInUid(int uid, java.lang.String packageName) {
        try {
            this.mAppOpsManager.checkPackage(uid, packageName);
            return true;
        } catch (java.lang.SecurityException e) {
            return false;
        }
    }

    private boolean isSameProfileGroupUser(int currentAdminUser, int callingUser) {
        return android.os.UserManager.get(this.mContext).isSameProfileGroup(currentAdminUser, callingUser);
    }
}
