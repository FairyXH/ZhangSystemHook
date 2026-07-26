package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class ProcessServiceRecord {
    boolean mAllowlistManager;
    final com.android.server.am.ProcessRecord mApp;
    private int mConnectionGroup;
    private int mConnectionImportance;
    private com.android.server.am.ServiceRecord mConnectionService;
    private boolean mExecServicesFg;
    private int mFgServiceTypes;
    private boolean mHasAboveClient;
    private boolean mHasClientActivities;
    private boolean mHasForegroundServices;
    private boolean mHasTopStartedAlmostPerceptibleServices;
    private boolean mHasTypeNoneFgs;
    private long mLastTopStartedAlmostPerceptibleBindRequestUptimeMs;
    private int mRepFgServiceTypes;
    private boolean mRepHasForegroundServices;
    private boolean mScheduleServiceTimeoutPending;
    private android.util.ArraySet<com.android.server.am.ConnectionRecord> mSdkSandboxConnections;
    private final com.android.server.am.ActivityManagerService mService;
    private boolean mTreatLikeActivity;
    final android.util.ArraySet<com.android.server.am.ServiceRecord> mServices = new android.util.ArraySet<>();
    private final android.util.ArraySet<com.android.server.am.ServiceRecord> mExecutingServices = new android.util.ArraySet<>();
    private final android.util.ArraySet<com.android.server.am.ConnectionRecord> mConnections = new android.util.ArraySet<>();
    private android.util.ArraySet<java.lang.Integer> mBoundClientUids = new android.util.ArraySet<>();

    ProcessServiceRecord(com.android.server.am.ProcessRecord app) {
        this.mApp = app;
        this.mService = app.mService;
    }

    void setHasClientActivities(boolean hasClientActivities) {
        this.mHasClientActivities = hasClientActivities;
        this.mApp.getWindowProcessController().setHasClientActivities(hasClientActivities);
    }

    boolean hasClientActivities() {
        return this.mHasClientActivities;
    }

    void setHasForegroundServices(boolean hasForegroundServices, int fgServiceTypes, boolean hasTypeNoneFgs) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            if (hasForegroundServices != (fgServiceTypes != 0 || hasTypeNoneFgs)) {
                throw new java.lang.IllegalStateException("hasForegroundServices mismatch");
            }
        }
        this.mHasForegroundServices = hasForegroundServices;
        this.mFgServiceTypes = fgServiceTypes;
        this.mHasTypeNoneFgs = hasTypeNoneFgs;
        this.mApp.getWindowProcessController().setHasForegroundServices(hasForegroundServices);
        if (hasForegroundServices) {
            this.mApp.mProfile.addHostingComponentType(256);
        } else {
            this.mApp.mProfile.clearHostingComponentType(256);
        }
    }

    boolean hasForegroundServices() {
        return this.mHasForegroundServices;
    }

    void setHasReportedForegroundServices(boolean hasForegroundServices) {
        this.mRepHasForegroundServices = hasForegroundServices;
    }

    boolean hasReportedForegroundServices() {
        return this.mRepHasForegroundServices;
    }

    int getForegroundServiceTypes() {
        if (this.mHasForegroundServices) {
            return this.mFgServiceTypes;
        }
        return 0;
    }

    boolean areForegroundServiceTypesSame(int types, boolean hasTypeNoneFgs) {
        return (getForegroundServiceTypes() & types) == types && this.mHasTypeNoneFgs == hasTypeNoneFgs;
    }

    boolean containsAnyForegroundServiceTypes(int types) {
        return (getForegroundServiceTypes() & types) != 0;
    }

    boolean hasNonShortForegroundServices() {
        if (this.mHasForegroundServices) {
            return this.mHasTypeNoneFgs || this.mFgServiceTypes != 2048;
        }
        return false;
    }

    boolean areAllShortForegroundServicesProcstateTimedOut(long nowUptime) {
        if (!this.mHasForegroundServices || hasNonShortForegroundServices()) {
            return false;
        }
        for (int i = this.mServices.size() - 1; i >= 0; i--) {
            com.android.server.am.ServiceRecord sr = this.mServices.valueAt(i);
            if (sr.isShortFgs() && sr.hasShortFgsInfo() && sr.getShortFgsInfo().getProcStateDemoteTime() >= nowUptime) {
                return false;
            }
        }
        return true;
    }

    int getReportedForegroundServiceTypes() {
        return this.mRepFgServiceTypes;
    }

    void setReportedForegroundServiceTypes(int foregroundServiceTypes) {
        this.mRepFgServiceTypes = foregroundServiceTypes;
    }

    int getNumForegroundServices() {
        int count = 0;
        int serviceCount = this.mServices.size();
        for (int i = 0; i < serviceCount; i++) {
            if (this.mServices.valueAt(i).isForeground) {
                count++;
            }
        }
        return count;
    }

    void updateHasTopStartedAlmostPerceptibleServices() {
        this.mHasTopStartedAlmostPerceptibleServices = false;
        this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs = 0L;
        for (int s = this.mServices.size() - 1; s >= 0; s--) {
            com.android.server.am.ServiceRecord sr = this.mServices.valueAt(s);
            this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs = java.lang.Math.max(this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs, sr.lastTopAlmostPerceptibleBindRequestUptimeMs);
            if (!this.mHasTopStartedAlmostPerceptibleServices && isAlmostPerceptible(sr)) {
                this.mHasTopStartedAlmostPerceptibleServices = true;
            }
        }
    }

    private boolean isAlmostPerceptible(com.android.server.am.ServiceRecord record) {
        if (record.lastTopAlmostPerceptibleBindRequestUptimeMs <= 0) {
            return false;
        }
        android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> serviceConnections = record.getConnections();
        for (int m = serviceConnections.size() - 1; m >= 0; m--) {
            java.util.ArrayList<com.android.server.am.ConnectionRecord> clist = serviceConnections.valueAt(m);
            for (int c = clist.size() - 1; c >= 0; c--) {
                com.android.server.am.ConnectionRecord cr = clist.get(c);
                if (cr.hasFlag(65536)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean hasTopStartedAlmostPerceptibleServices() {
        return this.mHasTopStartedAlmostPerceptibleServices || (this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs > 0 && android.os.SystemClock.uptimeMillis() - this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs < this.mService.mConstants.mServiceBindAlmostPerceptibleTimeoutMs);
    }

    com.android.server.am.ServiceRecord getConnectionService() {
        return this.mConnectionService;
    }

    void setConnectionService(com.android.server.am.ServiceRecord connectionService) {
        this.mConnectionService = connectionService;
    }

    int getConnectionGroup() {
        return this.mConnectionGroup;
    }

    void setConnectionGroup(int connectionGroup) {
        this.mConnectionGroup = connectionGroup;
    }

    int getConnectionImportance() {
        return this.mConnectionImportance;
    }

    void setConnectionImportance(int connectionImportance) {
        this.mConnectionImportance = connectionImportance;
    }

    void updateHasAboveClientLocked() {
        this.mHasAboveClient = false;
        for (int i = this.mConnections.size() - 1; i >= 0; i--) {
            com.android.server.am.ConnectionRecord cr = this.mConnections.valueAt(i);
            boolean isSameProcess = cr.binding.service.app != null && cr.binding.service.app.mServices == this;
            if (!isSameProcess && cr.hasFlag(8)) {
                this.mHasAboveClient = true;
                return;
            }
        }
    }

    void setHasAboveClient(boolean hasAboveClient) {
        this.mHasAboveClient = hasAboveClient;
    }

    boolean hasAboveClient() {
        return this.mHasAboveClient;
    }

    int modifyRawOomAdj(int adj) {
        if (this.mHasAboveClient && adj >= 0) {
            if (adj < 100) {
                return 100;
            }
            if (adj < 200) {
                return 200;
            }
            if (adj < 250) {
                return 250;
            }
            if (adj < 900) {
                return 900;
            }
            if (adj < 999) {
                return adj + 1;
            }
            return adj;
        }
        return adj;
    }

    public boolean isTreatedLikeActivity() {
        return this.mTreatLikeActivity;
    }

    void setTreatLikeActivity(boolean treatLikeActivity) {
        this.mTreatLikeActivity = treatLikeActivity;
    }

    boolean shouldExecServicesFg() {
        return this.mExecServicesFg;
    }

    void setExecServicesFg(boolean execServicesFg) {
        this.mExecServicesFg = execServicesFg;
    }

    boolean startService(com.android.server.am.ServiceRecord record) {
        if (record == null) {
            return false;
        }
        boolean added = this.mServices.add(record);
        if (added && record.serviceInfo != null) {
            this.mApp.getWindowProcessController().onServiceStarted(record.serviceInfo);
            updateHostingComonentTypeForBindingsLocked();
        }
        if (record.lastTopAlmostPerceptibleBindRequestUptimeMs > 0) {
            this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs = java.lang.Math.max(this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs, record.lastTopAlmostPerceptibleBindRequestUptimeMs);
            if (!this.mHasTopStartedAlmostPerceptibleServices) {
                this.mHasTopStartedAlmostPerceptibleServices = isAlmostPerceptible(record);
            }
        }
        return added;
    }

    boolean stopService(com.android.server.am.ServiceRecord record) {
        boolean removed = this.mServices.remove(record);
        if (record.lastTopAlmostPerceptibleBindRequestUptimeMs > 0) {
            updateHasTopStartedAlmostPerceptibleServices();
        }
        if (removed) {
            updateHostingComonentTypeForBindingsLocked();
        }
        return removed;
    }

    void stopAllServices() {
        this.mServices.clear();
        updateHasTopStartedAlmostPerceptibleServices();
    }

    int numberOfRunningServices() {
        return this.mServices.size();
    }

    com.android.server.am.ServiceRecord getRunningServiceAt(int index) {
        return this.mServices.valueAt(index);
    }

    void startExecutingService(com.android.server.am.ServiceRecord service) {
        this.mExecutingServices.add(service);
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, 1);
    }

    void stopExecutingService(com.android.server.am.ServiceRecord service) {
        this.mExecutingServices.remove(service);
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, 2);
    }

    void stopAllExecutingServices() {
        this.mExecutingServices.clear();
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, -1);
    }

    com.android.server.am.ServiceRecord getExecutingServiceAt(int index) {
        return this.mExecutingServices.valueAt(index);
    }

    int numberOfExecutingServices() {
        return this.mExecutingServices.size();
    }

    void addConnection(com.android.server.am.ConnectionRecord connection) {
        this.mConnections.add(connection);
        addSdkSandboxConnectionIfNecessary(connection);
    }

    void removeConnection(com.android.server.am.ConnectionRecord connection) {
        this.mConnections.remove(connection);
        removeSdkSandboxConnectionIfNecessary(connection);
    }

    void removeAllConnections() {
        int size = this.mConnections.size();
        for (int i = 0; i < size; i++) {
            removeSdkSandboxConnectionIfNecessary(this.mConnections.valueAt(i));
        }
        this.mConnections.clear();
    }

    com.android.server.am.ConnectionRecord getConnectionAt(int index) {
        return this.mConnections.valueAt(index);
    }

    int numberOfConnections() {
        return this.mConnections.size();
    }

    private void addSdkSandboxConnectionIfNecessary(com.android.server.am.ConnectionRecord connection) {
        com.android.server.am.ProcessRecord attributedClient = connection.binding.attributedClient;
        if (attributedClient != null && connection.binding.service.isSdkSandbox) {
            if (attributedClient.mServices.mSdkSandboxConnections == null) {
                attributedClient.mServices.mSdkSandboxConnections = new android.util.ArraySet<>();
            }
            attributedClient.mServices.mSdkSandboxConnections.add(connection);
        }
    }

    private void removeSdkSandboxConnectionIfNecessary(com.android.server.am.ConnectionRecord connection) {
        com.android.server.am.ProcessRecord attributedClient = connection.binding.attributedClient;
        if (attributedClient != null && connection.binding.service.isSdkSandbox && attributedClient.mServices.mSdkSandboxConnections != null) {
            attributedClient.mServices.mSdkSandboxConnections.remove(connection);
        }
    }

    void removeAllSdkSandboxConnections() {
        if (this.mSdkSandboxConnections != null) {
            this.mSdkSandboxConnections.clear();
        }
    }

    com.android.server.am.ConnectionRecord getSdkSandboxConnectionAt(int index) {
        if (this.mSdkSandboxConnections != null) {
            return this.mSdkSandboxConnections.valueAt(index);
        }
        return null;
    }

    int numberOfSdkSandboxConnections() {
        if (this.mSdkSandboxConnections != null) {
            return this.mSdkSandboxConnections.size();
        }
        return 0;
    }

    void addBoundClientUid(int clientUid, java.lang.String clientPackageName, long bindFlags) {
        this.mBoundClientUids.add(java.lang.Integer.valueOf(clientUid));
        this.mApp.getWindowProcessController().addBoundClientUid(clientUid, clientPackageName, bindFlags);
    }

    void updateBoundClientUids() {
        clearBoundClientUids();
        if (this.mServices.isEmpty()) {
            return;
        }
        android.util.ArraySet<java.lang.Integer> boundClientUids = new android.util.ArraySet<>();
        int serviceCount = this.mServices.size();
        com.android.server.wm.WindowProcessController controller = this.mApp.getWindowProcessController();
        for (int j = 0; j < serviceCount; j++) {
            android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> conns = this.mServices.valueAt(j).getConnections();
            int size = conns.size();
            for (int conni = 0; conni < size; conni++) {
                java.util.ArrayList<com.android.server.am.ConnectionRecord> c = conns.valueAt(conni);
                for (int i = 0; i < c.size(); i++) {
                    com.android.server.am.ConnectionRecord cr = c.get(i);
                    boundClientUids.add(java.lang.Integer.valueOf(cr.clientUid));
                    controller.addBoundClientUid(cr.clientUid, cr.clientPackageName, cr.getFlags());
                }
            }
        }
        this.mBoundClientUids = boundClientUids;
    }

    void addBoundClientUidsOfNewService(com.android.server.am.ServiceRecord sr) {
        if (sr == null) {
            return;
        }
        android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> conns = sr.getConnections();
        for (int conni = conns.size() - 1; conni >= 0; conni--) {
            java.util.ArrayList<com.android.server.am.ConnectionRecord> c = conns.valueAt(conni);
            for (int i = 0; i < c.size(); i++) {
                com.android.server.am.ConnectionRecord cr = c.get(i);
                this.mBoundClientUids.add(java.lang.Integer.valueOf(cr.clientUid));
                this.mApp.getWindowProcessController().addBoundClientUid(cr.clientUid, cr.clientPackageName, cr.getFlags());
            }
        }
    }

    void clearBoundClientUids() {
        this.mBoundClientUids.clear();
        this.mApp.getWindowProcessController().clearBoundClientUids();
    }

    void updateHostingComonentTypeForBindingsLocked() {
        boolean hasBoundClient = false;
        int i = numberOfRunningServices() - 1;
        while (true) {
            if (i >= 0) {
                com.android.server.am.ServiceRecord sr = getRunningServiceAt(i);
                if (sr == null || sr.getConnections().isEmpty()) {
                    i--;
                } else {
                    hasBoundClient = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (hasBoundClient) {
            this.mApp.mProfile.addHostingComponentType(512);
        } else {
            this.mApp.mProfile.clearHostingComponentType(512);
        }
    }

    boolean incServiceCrashCountLocked(long now) {
        boolean procIsBoundForeground = this.mApp.mState.getCurProcState() == 5;
        boolean tryAgain = false;
        for (int i = numberOfRunningServices() - 1; i >= 0; i--) {
            com.android.server.am.ServiceRecord sr = getRunningServiceAt(i);
            if (now > sr.restartTime + ((long) com.android.server.am.ActivityManagerConstants.MIN_CRASH_INTERVAL)) {
                sr.crashCount = 1;
            } else {
                sr.crashCount++;
            }
            if (sr.crashCount < this.mService.mConstants.BOUND_SERVICE_MAX_CRASH_RETRY && (sr.isForeground || procIsBoundForeground)) {
                tryAgain = true;
            }
        }
        return tryAgain;
    }

    void onCleanupApplicationRecordLocked() {
        this.mTreatLikeActivity = false;
        this.mHasAboveClient = false;
        setHasClientActivities(false);
    }

    void noteScheduleServiceTimeoutPending(boolean pending) {
        this.mScheduleServiceTimeoutPending = pending;
    }

    boolean isScheduleServiceTimeoutPending() {
        return this.mScheduleServiceTimeoutPending;
    }

    void onProcessUnfrozen() {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                scheduleServiceTimeoutIfNeededLocked();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    void onProcessFrozenCancelled() {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                scheduleServiceTimeoutIfNeededLocked();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void scheduleServiceTimeoutIfNeededLocked() {
        if (com.android.server.am.Flags.serviceBindingOomAdjPolicy() && this.mScheduleServiceTimeoutPending && this.mExecutingServices.size() > 0) {
            this.mService.mServices.scheduleServiceTimeoutLocked(this.mApp);
            long now = android.os.SystemClock.uptimeMillis();
            int size = this.mExecutingServices.size();
            for (int i = 0; i < size; i++) {
                this.mExecutingServices.valueAt(i).executingStart = now;
            }
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, long nowUptime) {
        if (this.mHasForegroundServices || this.mApp.mState.getForcingToImportant() != null) {
            pw.print(prefix);
            pw.print("mHasForegroundServices=");
            pw.print(this.mHasForegroundServices);
            pw.print(" forcingToImportant=");
            pw.println(this.mApp.mState.getForcingToImportant());
        }
        if (this.mHasTopStartedAlmostPerceptibleServices || this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs > 0) {
            pw.print(prefix);
            pw.print("mHasTopStartedAlmostPerceptibleServices=");
            pw.print(this.mHasTopStartedAlmostPerceptibleServices);
            pw.print(" mLastTopStartedAlmostPerceptibleBindRequestUptimeMs=");
            pw.println(this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs);
        }
        if (this.mHasClientActivities || this.mHasAboveClient || this.mTreatLikeActivity) {
            pw.print(prefix);
            pw.print("hasClientActivities=");
            pw.print(this.mHasClientActivities);
            pw.print(" hasAboveClient=");
            pw.print(this.mHasAboveClient);
            pw.print(" treatLikeActivity=");
            pw.println(this.mTreatLikeActivity);
        }
        if (this.mConnectionService != null || this.mConnectionGroup != 0) {
            pw.print(prefix);
            pw.print("connectionGroup=");
            pw.print(this.mConnectionGroup);
            pw.print(" Importance=");
            pw.print(this.mConnectionImportance);
            pw.print(" Service=");
            pw.println(this.mConnectionService);
        }
        if (this.mAllowlistManager) {
            pw.print(prefix);
            pw.print("allowlistManager=");
            pw.println(this.mAllowlistManager);
        }
        if (this.mServices.size() > 0) {
            pw.print(prefix);
            pw.println("Services:");
            int size = this.mServices.size();
            for (int i = 0; i < size; i++) {
                pw.print(prefix);
                pw.print("  - ");
                pw.println(this.mServices.valueAt(i));
            }
        }
        if (this.mExecutingServices.size() > 0) {
            pw.print(prefix);
            pw.print("Executing Services (fg=");
            pw.print(this.mExecServicesFg);
            pw.println(")");
            int size2 = this.mExecutingServices.size();
            for (int i2 = 0; i2 < size2; i2++) {
                pw.print(prefix);
                pw.print("  - ");
                pw.println(this.mExecutingServices.valueAt(i2));
            }
        }
        if (this.mConnections.size() > 0) {
            pw.print(prefix);
            pw.println("mConnections:");
            int size3 = this.mConnections.size();
            for (int i3 = 0; i3 < size3; i3++) {
                pw.print(prefix);
                pw.print("  - ");
                pw.println(this.mConnections.valueAt(i3));
            }
        }
        if (com.android.server.am.Flags.serviceBindingOomAdjPolicy()) {
            pw.print(prefix);
            pw.print("scheduleServiceTimeoutPending=");
            pw.println(this.mScheduleServiceTimeoutPending);
        }
    }
}
