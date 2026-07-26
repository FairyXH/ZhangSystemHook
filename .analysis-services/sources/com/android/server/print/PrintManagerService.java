package com.android.server.print;

/* JADX INFO: loaded from: classes3.dex */
public final class PrintManagerService extends com.android.server.SystemService {
    private static final java.lang.String LOG_TAG = "PrintManagerService";
    private final com.android.server.print.PrintManagerService.PrintManagerImpl mPrintManagerImpl;

    public PrintManagerService(android.content.Context context) {
        super(context);
        this.mPrintManagerImpl = new com.android.server.print.PrintManagerService.PrintManagerImpl(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("print", this.mPrintManagerImpl);
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        this.mPrintManagerImpl.handleUserUnlocked(user.getUserIdentifier());
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        this.mPrintManagerImpl.handleUserStopped(user.getUserIdentifier());
    }

    class PrintManagerImpl extends android.print.IPrintManager.Stub {
        private static final int BACKGROUND_USER_ID = -10;
        private final android.content.Context mContext;
        private final android.os.UserManager mUserManager;
        private final java.lang.Object mLock = new java.lang.Object();
        private final android.util.SparseArray<com.android.server.print.UserState> mUserStates = new android.util.SparseArray<>();

        PrintManagerImpl(android.content.Context context) {
            this.mContext = context;
            this.mUserManager = (android.os.UserManager) context.getSystemService("user");
            registerContentObservers();
            registerBroadcastReceivers();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.print.PrintShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        public android.os.Bundle print(java.lang.String printJobName, android.print.IPrintDocumentAdapter adapter, android.print.PrintAttributes attributes, java.lang.String packageName, int appId, int userId) throws java.lang.Throwable {
            long identity;
            java.util.Objects.requireNonNull(adapter);
            if (!isPrintingEnabled()) {
                android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
                int callingUserId = android.os.UserHandle.getCallingUserId();
                identity = android.os.Binder.clearCallingIdentity();
                try {
                    java.lang.CharSequence disabledMessage = dpmi.getPrintingDisabledReasonForUser(callingUserId);
                    if (disabledMessage != null) {
                        android.widget.Toast.makeText(this.mContext, android.os.Looper.getMainLooper(), disabledMessage, 1).show();
                    }
                    try {
                        adapter.start();
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(com.android.server.print.PrintManagerService.LOG_TAG, "Error calling IPrintDocumentAdapter.start()");
                    }
                    try {
                        adapter.finish();
                    } catch (android.os.RemoteException e2) {
                        android.util.Log.e(com.android.server.print.PrintManagerService.LOG_TAG, "Error calling IPrintDocumentAdapter.finish()");
                    }
                    return null;
                } finally {
                }
            }
            java.lang.String printJobName2 = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty(printJobName);
            java.lang.String packageName2 = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty(packageName);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                try {
                    try {
                        if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                            return null;
                        }
                        int resolvedAppId = resolveCallingAppEnforcingPermissions(appId);
                        java.lang.String resolvedPackageName = resolveCallingPackageNameEnforcingSecurity(packageName2);
                        com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                        identity = android.os.Binder.clearCallingIdentity();
                        try {
                            return userState.print(printJobName2, adapter, attributes, resolvedPackageName, resolvedAppId);
                        } finally {
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        public java.util.List<android.print.PrintJobInfo> getPrintJobInfos(int appId, int userId) {
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return null;
                }
                int resolvedAppId = resolveCallingAppEnforcingPermissions(appId);
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return userState.getPrintJobInfos(resolvedAppId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public android.print.PrintJobInfo getPrintJobInfo(android.print.PrintJobId printJobId, int appId, int userId) {
            if (printJobId == null) {
                return null;
            }
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return null;
                }
                int resolvedAppId = resolveCallingAppEnforcingPermissions(appId);
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return userState.getPrintJobInfo(printJobId, resolvedAppId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public android.graphics.drawable.Icon getCustomPrinterIcon(android.print.PrinterId printerId, int userId) {
            java.util.Objects.requireNonNull(printerId);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return null;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    android.graphics.drawable.Icon icon = userState.getCustomPrinterIcon(printerId);
                    return validateIconUserBoundary(icon);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        private android.graphics.drawable.Icon validateIconUserBoundary(android.graphics.drawable.Icon icon) {
            java.lang.String encodedUser;
            if (icon != null && ((icon.getType() == 4 || icon.getType() == 6) && (encodedUser = icon.getUri().getEncodedUserInfo()) != null)) {
                int userId = java.lang.Integer.parseInt(encodedUser);
                int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
                synchronized (this.mLock) {
                    if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                        return null;
                    }
                }
            }
            return icon;
        }

        public void cancelPrintJob(android.print.PrintJobId printJobId, int appId, int userId) {
            if (printJobId == null) {
                return;
            }
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                int resolvedAppId = resolveCallingAppEnforcingPermissions(appId);
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.cancelPrintJob(printJobId, resolvedAppId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void restartPrintJob(android.print.PrintJobId printJobId, int appId, int userId) {
            if (printJobId == null || !isPrintingEnabled()) {
                return;
            }
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                int resolvedAppId = resolveCallingAppEnforcingPermissions(appId);
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.restartPrintJob(printJobId, resolvedAppId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public java.util.List<android.printservice.PrintServiceInfo> getPrintServices(int selectionFlags, int userId) {
            com.android.internal.util.Preconditions.checkFlagsArgument(selectionFlags, 3);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICES", null);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return null;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return userState.getPrintServices(selectionFlags);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void setPrintServiceEnabled(android.content.ComponentName service, boolean isEnabled, int userId) {
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            int appId = android.os.UserHandle.getAppId(android.os.Binder.getCallingUid());
            if (appId != 1000) {
                try {
                    if (appId != android.os.UserHandle.getAppId(this.mContext.getPackageManager().getPackageUidAsUser("com.android.printspooler", resolvedUserId))) {
                        throw new java.lang.SecurityException("Only system and print spooler can call this");
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Log.e(com.android.server.print.PrintManagerService.LOG_TAG, "Could not verify caller", e);
                    return;
                }
            }
            java.util.Objects.requireNonNull(service);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.setPrintServiceEnabled(service, isEnabled);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public boolean isPrintServiceEnabled(android.content.ComponentName service, int userId) {
            java.lang.String[] packages = this.mContext.getPackageManager().getPackagesForUid(android.os.Binder.getCallingUid());
            boolean matchCalling = false;
            int i = 0;
            while (true) {
                if (i >= packages.length) {
                    break;
                }
                if (!packages[i].equals(service.getPackageName())) {
                    i++;
                } else {
                    matchCalling = true;
                    break;
                }
            }
            if (!matchCalling) {
                throw new java.lang.SecurityException("PrintService does not share UID with caller.");
            }
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return false;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                return userState.isPrintServiceEnabled(service);
            }
        }

        public java.util.List<android.printservice.recommendation.RecommendationInfo> getPrintServiceRecommendations(int userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICE_RECOMMENDATIONS", null);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return null;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return userState.getPrintServiceRecommendations();
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void createPrinterDiscoverySession(android.print.IPrinterDiscoveryObserver observer, int userId) {
            java.util.Objects.requireNonNull(observer);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.createPrinterDiscoverySession(observer);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void destroyPrinterDiscoverySession(android.print.IPrinterDiscoveryObserver observer, int userId) {
            java.util.Objects.requireNonNull(observer);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.destroyPrinterDiscoverySession(observer);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void startPrinterDiscovery(android.print.IPrinterDiscoveryObserver observer, java.util.List<android.print.PrinterId> priorityList, int userId) {
            java.util.Objects.requireNonNull(observer);
            if (priorityList != null) {
                priorityList = (java.util.List) com.android.internal.util.Preconditions.checkCollectionElementsNotNull(priorityList, "PrinterId");
            }
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.startPrinterDiscovery(observer, priorityList);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void stopPrinterDiscovery(android.print.IPrinterDiscoveryObserver observer, int userId) {
            java.util.Objects.requireNonNull(observer);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.stopPrinterDiscovery(observer);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void validatePrinters(java.util.List<android.print.PrinterId> printerIds, int userId) {
            java.util.List<android.print.PrinterId> printerIds2 = (java.util.List) com.android.internal.util.Preconditions.checkCollectionElementsNotNull(printerIds, "PrinterId");
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.validatePrinters(printerIds2);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void startPrinterStateTracking(android.print.PrinterId printerId, int userId) {
            java.util.Objects.requireNonNull(printerId);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.startPrinterStateTracking(printerId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void stopPrinterStateTracking(android.print.PrinterId printerId, int userId) {
            java.util.Objects.requireNonNull(printerId);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.stopPrinterStateTracking(printerId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void addPrintJobStateChangeListener(android.print.IPrintJobStateChangeListener listener, int appId, int userId) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(listener);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                int resolvedAppId = resolveCallingAppEnforcingPermissions(appId);
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.addPrintJobStateChangeListener(listener, resolvedAppId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void removePrintJobStateChangeListener(android.print.IPrintJobStateChangeListener listener, int userId) {
            java.util.Objects.requireNonNull(listener);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.removePrintJobStateChangeListener(listener);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void addPrintServicesChangeListener(android.print.IPrintServicesChangeListener listener, int userId) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(listener);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICES", null);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.addPrintServicesChangeListener(listener);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void removePrintServicesChangeListener(android.print.IPrintServicesChangeListener listener, int userId) {
            java.util.Objects.requireNonNull(listener);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICES", null);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.removePrintServicesChangeListener(listener);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void addPrintServiceRecommendationsChangeListener(android.printservice.recommendation.IRecommendationsChangeListener listener, int userId) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(listener);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICE_RECOMMENDATIONS", null);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.addPrintServiceRecommendationsChangeListener(listener);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void removePrintServiceRecommendationsChangeListener(android.printservice.recommendation.IRecommendationsChangeListener listener, int userId) {
            java.util.Objects.requireNonNull(listener);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICE_RECOMMENDATIONS", null);
            int resolvedUserId = resolveCallingUserEnforcingPermissions(userId);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolvedUserId) != getCurrentUserId()) {
                    return;
                }
                com.android.server.print.UserState userState = getOrCreateUserStateLocked(resolvedUserId, false);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    userState.removePrintServiceRecommendationsChangeListener(listener);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            java.lang.String opt;
            java.util.Objects.requireNonNull(fd);
            if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, com.android.server.print.PrintManagerService.LOG_TAG, pw)) {
                int opti = 0;
                boolean dumpAsProto = false;
                while (opti < args.length && (opt = args[opti]) != null && opt.length() > 0 && opt.charAt(0) == '-') {
                    opti++;
                    if ("--proto".equals(opt)) {
                        dumpAsProto = true;
                    } else {
                        pw.println("Unknown argument: " + opt + "; use -h for help");
                    }
                }
                java.util.ArrayList<com.android.server.print.UserState> userStatesToDump = new java.util.ArrayList<>();
                synchronized (this.mLock) {
                    int numUserStates = this.mUserStates.size();
                    for (int i = 0; i < numUserStates; i++) {
                        userStatesToDump.add(this.mUserStates.valueAt(i));
                    }
                }
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    if (dumpAsProto) {
                        dump(new com.android.internal.util.dump.DualDumpOutputStream(new android.util.proto.ProtoOutputStream(fd)), userStatesToDump);
                    } else {
                        pw.println("PRINT MANAGER STATE (dumpsys print)");
                        dump(new com.android.internal.util.dump.DualDumpOutputStream(new com.android.internal.util.IndentingPrintWriter(pw, "  ")), userStatesToDump);
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public boolean getBindInstantServiceAllowed(int userId) {
            com.android.server.print.UserState userState;
            int callingUid = android.os.Binder.getCallingUid();
            if (callingUid != 2000 && callingUid != 0) {
                throw new java.lang.SecurityException("Can only be called by uid 2000 or 0");
            }
            synchronized (this.mLock) {
                userState = getOrCreateUserStateLocked(userId, false);
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return userState.getBindInstantServiceAllowed();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setBindInstantServiceAllowed(int userId, boolean allowed) {
            com.android.server.print.UserState userState;
            int callingUid = android.os.Binder.getCallingUid();
            if (callingUid != 2000 && callingUid != 0) {
                throw new java.lang.SecurityException("Can only be called by uid 2000 or 0");
            }
            synchronized (this.mLock) {
                userState = getOrCreateUserStateLocked(userId, false);
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                userState.setBindInstantServiceAllowed(allowed);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private boolean isPrintingEnabled() {
            return !this.mUserManager.hasUserRestriction("no_printing", android.os.Binder.getCallingUserHandle());
        }

        private void dump(com.android.internal.util.dump.DualDumpOutputStream dumpStream, java.util.ArrayList<com.android.server.print.UserState> userStatesToDump) {
            int userStateCount = userStatesToDump.size();
            for (int i = 0; i < userStateCount; i++) {
                long token = dumpStream.start("user_states", 2246267895809L);
                userStatesToDump.get(i).dump(dumpStream);
                dumpStream.end(token);
            }
            dumpStream.flush();
        }

        private void registerContentObservers() {
            final android.net.Uri enabledPrintServicesUri = android.provider.Settings.Secure.getUriFor("disabled_print_services");
            android.database.ContentObserver observer = new android.database.ContentObserver(com.android.internal.os.BackgroundThread.getHandler()) { // from class: com.android.server.print.PrintManagerService.PrintManagerImpl.1
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
                    if (enabledPrintServicesUri.equals(uri)) {
                        synchronized (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mLock) {
                            int userCount = com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserStates.size();
                            for (int i = 0; i < userCount; i++) {
                                if (userId == -1 || userId == com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserStates.keyAt(i)) {
                                    ((com.android.server.print.UserState) com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserStates.valueAt(i)).updateIfNeededLocked();
                                }
                            }
                        }
                    }
                }
            };
            this.mContext.getContentResolver().registerContentObserver(enabledPrintServicesUri, false, observer, -1);
        }

        private void registerBroadcastReceivers() {
            com.android.internal.content.PackageMonitor monitor = new com.android.internal.content.PackageMonitor(true) { // from class: com.android.server.print.PrintManagerService.PrintManagerImpl.2
                private boolean hasPrintService(java.lang.String packageName) {
                    android.content.Intent intent = new android.content.Intent("android.printservice.PrintService");
                    intent.setPackage(packageName);
                    java.util.List<android.content.pm.ResolveInfo> installedServices = com.android.server.print.PrintManagerService.PrintManagerImpl.this.mContext.getPackageManager().queryIntentServicesAsUser(intent, 276824068, getChangingUserId());
                    return (installedServices == null || installedServices.isEmpty()) ? false : true;
                }

                private boolean hadPrintService(com.android.server.print.UserState userState, java.lang.String packageName) {
                    java.util.List<android.printservice.PrintServiceInfo> installedServices = userState.getPrintServices(3);
                    if (installedServices == null) {
                        return false;
                    }
                    int numInstalledServices = installedServices.size();
                    for (int i = 0; i < numInstalledServices; i++) {
                        if (installedServices.get(i).getResolveInfo().serviceInfo.packageName.equals(packageName)) {
                            return true;
                        }
                    }
                    return false;
                }

                public void onPackageModified(java.lang.String packageName) {
                    if (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(getChangingUserId())) {
                        com.android.server.print.UserState userState = com.android.server.print.PrintManagerService.PrintManagerImpl.this.getOrCreateUserStateLocked(getChangingUserId(), false, false);
                        boolean prunePrintServices = false;
                        synchronized (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mLock) {
                            if (hadPrintService(userState, packageName) || hasPrintService(packageName)) {
                                userState.updateIfNeededLocked();
                                prunePrintServices = true;
                            }
                        }
                        if (prunePrintServices) {
                            userState.prunePrintServices();
                        }
                    }
                }

                public void onPackageRemoved(java.lang.String packageName, int uid) {
                    if (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(getChangingUserId())) {
                        com.android.server.print.UserState userState = com.android.server.print.PrintManagerService.PrintManagerImpl.this.getOrCreateUserStateLocked(getChangingUserId(), false, false);
                        boolean prunePrintServices = false;
                        synchronized (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mLock) {
                            if (hadPrintService(userState, packageName)) {
                                userState.updateIfNeededLocked();
                                prunePrintServices = true;
                            }
                        }
                        if (prunePrintServices) {
                            userState.prunePrintServices();
                        }
                    }
                }

                public boolean onHandleForceStop(android.content.Intent intent, java.lang.String[] stoppedPackages, int uid, boolean doit) {
                    if (!com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(getChangingUserId())) {
                        return false;
                    }
                    synchronized (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mLock) {
                        com.android.server.print.UserState userState = com.android.server.print.PrintManagerService.PrintManagerImpl.this.getOrCreateUserStateLocked(getChangingUserId(), false, false);
                        boolean stoppedSomePackages = false;
                        java.util.List<android.printservice.PrintServiceInfo> enabledServices = userState.getPrintServices(1);
                        if (enabledServices == null) {
                            return false;
                        }
                        java.util.Iterator<android.printservice.PrintServiceInfo> iterator = enabledServices.iterator();
                        while (iterator.hasNext()) {
                            android.content.ComponentName componentName = iterator.next().getComponentName();
                            java.lang.String componentPackage = componentName.getPackageName();
                            int length = stoppedPackages.length;
                            int i = 0;
                            while (true) {
                                if (i < length) {
                                    java.lang.String stoppedPackage = stoppedPackages[i];
                                    if (!componentPackage.equals(stoppedPackage)) {
                                        i++;
                                    } else {
                                        if (!doit) {
                                            return true;
                                        }
                                        stoppedSomePackages = true;
                                    }
                                }
                            }
                        }
                        if (stoppedSomePackages) {
                            userState.updateIfNeededLocked();
                        }
                        return false;
                    }
                }

                public void onPackageAdded(java.lang.String packageName, int uid) {
                    if (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(getChangingUserId())) {
                        synchronized (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mLock) {
                            if (hasPrintService(packageName)) {
                                com.android.server.print.UserState userState = com.android.server.print.PrintManagerService.PrintManagerImpl.this.getOrCreateUserStateLocked(getChangingUserId(), false, false);
                                userState.updateIfNeededLocked();
                            }
                        }
                    }
                }
            };
            monitor.register(this.mContext, com.android.internal.os.BackgroundThread.getHandler().getLooper(), android.os.UserHandle.ALL, true);
        }

        private com.android.server.print.UserState getOrCreateUserStateLocked(int userId, boolean lowPriority) {
            return getOrCreateUserStateLocked(userId, lowPriority, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.print.UserState getOrCreateUserStateLocked(int userId, boolean lowPriority, boolean enforceUserUnlockingOrUnlocked) {
            return getOrCreateUserStateLocked(userId, lowPriority, enforceUserUnlockingOrUnlocked, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.print.UserState getOrCreateUserStateLocked(int userId, boolean lowPriority, boolean enforceUserUnlockingOrUnlocked, boolean shouldUpdateState) {
            if (enforceUserUnlockingOrUnlocked && !this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
                throw new java.lang.IllegalStateException("User " + userId + " must be unlocked for printing to be available");
            }
            com.android.server.print.UserState userState = this.mUserStates.get(userId);
            if (userState == null) {
                userState = new com.android.server.print.UserState(this.mContext, userId, this.mLock, lowPriority);
                this.mUserStates.put(userId, userState);
            } else if (shouldUpdateState) {
                userState.updateIfNeededLocked();
            }
            if (!lowPriority) {
                userState.increasePriority();
            }
            return userState;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleUserUnlocked(final int userId) {
            com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.print.PrintManagerService.PrintManagerImpl.3
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.print.UserState userState;
                    if (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
                        synchronized (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mLock) {
                            userState = com.android.server.print.PrintManagerService.PrintManagerImpl.this.getOrCreateUserStateLocked(userId, true, false, true);
                        }
                        userState.removeObsoletePrintJobs();
                    }
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleUserStopped(final int userId) {
            com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.print.PrintManagerService.PrintManagerImpl.4
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (com.android.server.print.PrintManagerService.PrintManagerImpl.this.mLock) {
                        com.android.server.print.UserState userState = (com.android.server.print.UserState) com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserStates.get(userId);
                        if (userState != null) {
                            userState.destroyLocked();
                            com.android.server.print.PrintManagerService.PrintManagerImpl.this.mUserStates.remove(userId);
                        }
                    }
                }
            });
        }

        private int resolveCallingProfileParentLocked(int userId) {
            if (userId != getCurrentUserId()) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    android.content.pm.UserInfo parent = this.mUserManager.getProfileParent(userId);
                    if (parent != null) {
                        return parent.getUserHandle().getIdentifier();
                    }
                    android.os.Binder.restoreCallingIdentity(identity);
                    return -10;
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return userId;
        }

        private int resolveCallingAppEnforcingPermissions(int appId) {
            int callingAppId;
            int callingUid = android.os.Binder.getCallingUid();
            if (callingUid != 0 && appId != (callingAppId = android.os.UserHandle.getAppId(callingUid)) && callingAppId != 2000 && callingAppId != 1000 && this.mContext.checkCallingPermission("com.android.printspooler.permission.ACCESS_ALL_PRINT_JOBS") != 0) {
                throw new java.lang.SecurityException("Call from app " + callingAppId + " as app " + appId + " without com.android.printspooler.permission.ACCESS_ALL_PRINT_JOBS");
            }
            return appId;
        }

        private int resolveCallingUserEnforcingPermissions(int userId) {
            try {
                return android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, true, true, "", (java.lang.String) null);
            } catch (android.os.RemoteException e) {
                return userId;
            }
        }

        private java.lang.String resolveCallingPackageNameEnforcingSecurity(java.lang.String packageName) {
            java.lang.String[] packages = this.mContext.getPackageManager().getPackagesForUid(android.os.Binder.getCallingUid());
            for (java.lang.String str : packages) {
                if (packageName.equals(str)) {
                    return packageName;
                }
            }
            throw new java.lang.IllegalArgumentException("packageName has to belong to the caller");
        }

        private int getCurrentUserId() {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return android.app.ActivityManager.getCurrentUser();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }
}
