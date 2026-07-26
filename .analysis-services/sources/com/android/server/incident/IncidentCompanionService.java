package com.android.server.incident;

/* JADX INFO: loaded from: classes2.dex */
public class IncidentCompanionService extends com.android.server.SystemService {
    static final java.lang.String TAG = "IncidentCompanionService";
    private com.android.server.incident.PendingReports mPendingReports;
    private static java.lang.String[] RESTRICTED_IMAGE_DUMP_ARGS = {"--hal", "--restricted_image"};
    private static final java.lang.String[] DUMP_AND_USAGE_STATS_PERMISSIONS = {"android.permission.DUMP", "android.permission.PACKAGE_USAGE_STATS"};

    private final class BinderService extends android.os.IIncidentCompanion.Stub {
        private BinderService() {
        }

        public void authorizeReport(int callingUid, java.lang.String callingPackage, java.lang.String receiverClass, java.lang.String reportId, int flags, android.os.IIncidentAuthListener listener) {
            enforceRequestAuthorizationPermission();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.incident.IncidentCompanionService.this.mPendingReports.authorizeReport(callingUid, callingPackage, receiverClass, reportId, flags, listener);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void cancelAuthorization(android.os.IIncidentAuthListener listener) {
            enforceRequestAuthorizationPermission();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.incident.IncidentCompanionService.this.mPendingReports.cancelAuthorization(listener);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void sendReportReadyBroadcast(java.lang.String pkg, java.lang.String cls) {
            enforceRequestAuthorizationPermission();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.content.Context context = com.android.server.incident.IncidentCompanionService.this.getContext();
                int currentAdminUser = com.android.server.incident.IncidentCompanionService.getCurrentUserIfAdmin();
                if (currentAdminUser == -10000) {
                    return;
                }
                android.content.Intent intent = new android.content.Intent("android.intent.action.INCIDENT_REPORT_READY");
                intent.setComponent(new android.content.ComponentName(pkg, cls));
                android.util.Log.d(com.android.server.incident.IncidentCompanionService.TAG, "sendReportReadyBroadcast sending currentUser=" + currentAdminUser + " userHandle=" + android.os.UserHandle.of(currentAdminUser) + " intent=" + intent);
                context.sendBroadcastAsUserMultiplePermissions(intent, android.os.UserHandle.of(currentAdminUser), com.android.server.incident.IncidentCompanionService.DUMP_AND_USAGE_STATS_PERMISSIONS);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public java.util.List<java.lang.String> getPendingReports() {
            enforceAuthorizePermission();
            return com.android.server.incident.IncidentCompanionService.this.mPendingReports.getPendingReports();
        }

        public void approveReport(java.lang.String uri) {
            enforceAuthorizePermission();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.incident.IncidentCompanionService.this.mPendingReports.approveReport(uri);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void denyReport(java.lang.String uri) {
            enforceAuthorizePermission();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.incident.IncidentCompanionService.this.mPendingReports.denyReport(uri);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public java.util.List<java.lang.String> getIncidentReportList(java.lang.String pkg, java.lang.String cls) throws android.os.RemoteException {
            enforceAccessReportsPermissions(null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.incident.IncidentCompanionService.this.getIIncidentManager().getIncidentReportList(pkg, cls);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void deleteIncidentReports(java.lang.String pkg, java.lang.String cls, java.lang.String id) throws android.os.RemoteException {
            if (pkg == null || cls == null || id == null || pkg.length() == 0 || cls.length() == 0 || id.length() == 0) {
                throw new java.lang.RuntimeException("Invalid pkg, cls or id");
            }
            enforceAccessReportsPermissions(pkg);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.incident.IncidentCompanionService.this.getIIncidentManager().deleteIncidentReports(pkg, cls, id);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void deleteAllIncidentReports(java.lang.String pkg) throws android.os.RemoteException {
            if (pkg == null || pkg.length() == 0) {
                throw new java.lang.RuntimeException("Invalid pkg");
            }
            enforceAccessReportsPermissions(pkg);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.incident.IncidentCompanionService.this.getIIncidentManager().deleteAllIncidentReports(pkg);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public android.os.IncidentManager.IncidentReport getIncidentReport(java.lang.String pkg, java.lang.String cls, java.lang.String id) throws android.os.RemoteException {
            if (pkg == null || cls == null || id == null || pkg.length() == 0 || cls.length() == 0 || id.length() == 0) {
                throw new java.lang.RuntimeException("Invalid pkg, cls or id");
            }
            enforceAccessReportsPermissions(pkg);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.incident.IncidentCompanionService.this.getIIncidentManager().getIncidentReport(pkg, cls, id);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.incident.IncidentCompanionService.this.getContext(), com.android.server.incident.IncidentCompanionService.TAG, writer)) {
                return;
            }
            if (args.length == 1 && "--restricted_image".equals(args[0])) {
                dumpRestrictedImages(fd);
            } else {
                com.android.server.incident.IncidentCompanionService.this.mPendingReports.dump(fd, writer, args);
            }
        }

        private void dumpRestrictedImages(java.io.FileDescriptor fd) {
            if (!android.os.Build.IS_ENG && !android.os.Build.IS_USERDEBUG) {
                return;
            }
            android.content.res.Resources res = com.android.server.incident.IncidentCompanionService.this.getContext().getResources();
            java.lang.String[] services = res.getStringArray(android.R.array.config_protectedNetworks);
            for (java.lang.String name : services) {
                android.util.Log.d(com.android.server.incident.IncidentCompanionService.TAG, "Looking up service " + name);
                android.os.IBinder service = android.os.ServiceManager.getService(name);
                if (service != null) {
                    android.util.Log.d(com.android.server.incident.IncidentCompanionService.TAG, "Calling dump on service: " + name);
                    try {
                        service.dump(fd, com.android.server.incident.IncidentCompanionService.RESTRICTED_IMAGE_DUMP_ARGS);
                    } catch (android.os.RemoteException ex) {
                        android.util.Log.w(com.android.server.incident.IncidentCompanionService.TAG, "dump --restricted_image of " + name + " threw", ex);
                    }
                }
            }
        }

        private void enforceRequestAuthorizationPermission() {
            com.android.server.incident.IncidentCompanionService.this.getContext().enforceCallingOrSelfPermission("android.permission.REQUEST_INCIDENT_REPORT_APPROVAL", null);
        }

        private void enforceAuthorizePermission() {
            com.android.server.incident.IncidentCompanionService.this.getContext().enforceCallingOrSelfPermission("android.permission.APPROVE_INCIDENT_REPORTS", null);
        }

        private void enforceAccessReportsPermissions(java.lang.String pkg) {
            if (com.android.server.incident.IncidentCompanionService.this.getContext().checkCallingPermission("android.permission.APPROVE_INCIDENT_REPORTS") != 0) {
                com.android.server.incident.IncidentCompanionService.this.getContext().enforceCallingOrSelfPermission("android.permission.DUMP", null);
                com.android.server.incident.IncidentCompanionService.this.getContext().enforceCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS", null);
                if (pkg != null) {
                    enforceCallerIsSameApp(pkg);
                }
            }
        }

        private void enforceCallerIsSameApp(java.lang.String pkg) throws java.lang.SecurityException {
            try {
                int uid = android.os.Binder.getCallingUid();
                int userId = android.os.UserHandle.getCallingUserId();
                android.content.pm.ApplicationInfo ai = com.android.server.incident.IncidentCompanionService.this.getContext().getPackageManager().getApplicationInfoAsUser(pkg, 0, userId);
                if (ai == null) {
                    throw new java.lang.SecurityException("Unknown package " + pkg);
                }
                if (!android.os.UserHandle.isSameApp(ai.uid, uid)) {
                    throw new java.lang.SecurityException("Calling uid " + uid + " gave package " + pkg + " which is owned by uid " + ai.uid);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException re) {
                throw new java.lang.SecurityException("Unknown package " + pkg + "\n" + re);
            }
        }
    }

    public IncidentCompanionService(android.content.Context context) {
        super(context);
        this.mPendingReports = new com.android.server.incident.PendingReports(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("incidentcompanion", new com.android.server.incident.IncidentCompanionService.BinderService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        super.onBootPhase(phase);
        switch (phase) {
            case 1000:
                this.mPendingReports.onBootCompleted();
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.IIncidentManager getIIncidentManager() throws android.os.RemoteException {
        return android.os.IIncidentManager.Stub.asInterface(android.os.ServiceManager.getService("incident"));
    }

    public static int getCurrentUserIfAdmin() {
        try {
            android.content.pm.UserInfo currentUser = android.app.ActivityManager.getService().getCurrentUser();
            if (currentUser == null) {
                android.util.Log.w(TAG, "No current user.  Nobody to approve the report. The report will be denied.");
                return -10000;
            }
            if (!currentUser.isAdmin()) {
                android.util.Log.w(TAG, "Only an admin user running in foreground can approve bugreports, but the current foreground user is not an admin user. The report will be denied.");
                return -10000;
            }
            return currentUser.id;
        } catch (android.os.RemoteException ex) {
            throw new java.lang.RuntimeException(ex);
        }
    }
}
