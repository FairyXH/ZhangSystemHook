package com.android.server.contentcapture;

/* JADX INFO: loaded from: classes.dex */
final class ContentCapturePerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.contentcapture.ContentCapturePerUserService, com.android.server.contentcapture.ContentCaptureManagerService> implements com.android.server.contentcapture.RemoteContentCaptureService.ContentCaptureServiceCallbacks {
    static final int EVENT_LOG_CONNECT_STATE_CONNECTED = 1;
    private static final int EVENT_LOG_CONNECT_STATE_DIED = 0;
    static final int EVENT_LOG_CONNECT_STATE_DISCONNECTED = 2;
    private static final java.lang.String TAG = com.android.server.contentcapture.ContentCapturePerUserService.class.getSimpleName();
    private final android.util.ArrayMap<java.lang.String, android.util.ArraySet<android.view.contentcapture.ContentCaptureCondition>> mConditionsByPkg;
    private android.service.contentcapture.ContentCaptureServiceInfo mInfo;
    com.android.server.contentcapture.RemoteContentCaptureService mRemoteService;
    private final com.android.server.contentcapture.ContentCapturePerUserService.ContentCaptureServiceRemoteCallback mRemoteServiceCallback;
    private final android.util.SparseArray<com.android.server.contentcapture.ContentCaptureServerSession> mSessions;
    private boolean mZombie;

    ContentCapturePerUserService(com.android.server.contentcapture.ContentCaptureManagerService master, java.lang.Object lock, boolean disabled, int userId) {
        super(master, lock, userId);
        this.mSessions = new android.util.SparseArray<>();
        this.mRemoteServiceCallback = new com.android.server.contentcapture.ContentCapturePerUserService.ContentCaptureServiceRemoteCallback();
        this.mConditionsByPkg = new android.util.ArrayMap<>();
        updateRemoteServiceLocked(disabled);
    }

    private void updateRemoteServiceLocked(boolean disabled) {
        if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).verbose) {
            android.util.Slog.v(TAG, "updateRemoteService(disabled=" + disabled + ")");
        }
        if (this.mRemoteService != null) {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
                android.util.Slog.d(TAG, "updateRemoteService(): destroying old remote service");
            }
            this.mRemoteService.destroy();
            this.mRemoteService = null;
            resetContentCaptureWhitelistLocked();
        }
        android.content.ComponentName serviceComponentName = updateServiceInfoLocked();
        if (serviceComponentName == null) {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
                android.util.Slog.d(TAG, "updateRemoteService(): no service component name");
            }
        } else if (!disabled) {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
                android.util.Slog.d(TAG, "updateRemoteService(): creating new remote service for " + serviceComponentName);
            }
            this.mRemoteService = new com.android.server.contentcapture.RemoteContentCaptureService(((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).getContext(), "android.service.contentcapture.ContentCaptureService", serviceComponentName, this.mRemoteServiceCallback, this.mUserId, this, ((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).isBindInstantServiceAllowed(), ((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).verbose, ((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).mDevCfgIdleUnbindTimeoutMs);
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        this.mInfo = new android.service.contentcapture.ContentCaptureServiceInfo(getContext(), serviceComponent, isTemporaryServiceSetLocked(), this.mUserId);
        return this.mInfo.getServiceInfo();
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected boolean updateLocked(boolean disabled) {
        boolean disabledStateChanged = super.updateLocked(disabled);
        if (disabledStateChanged) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                this.mSessions.valueAt(i).setContentCaptureEnabledLocked(!disabled);
            }
        }
        destroyLocked();
        updateRemoteServiceLocked(disabled);
        return disabledStateChanged;
    }

    public void onServiceDied(com.android.server.contentcapture.RemoteContentCaptureService service) {
        android.util.Slog.w(TAG, "remote service died: " + service);
        synchronized (this.mLock) {
            this.mZombie = true;
            android.content.ComponentName serviceComponent = getServiceComponentName();
            com.android.server.contentcapture.ContentCaptureMetricsLogger.writeServiceEvent(16, serviceComponent);
            android.util.EventLog.writeEvent(com.android.server.contentcapture.EventLogTags.CC_CONNECT_STATE_CHANGED, java.lang.Integer.valueOf(this.mUserId), 0, 0);
        }
    }

    void onConnected() {
        synchronized (this.mLock) {
            if (this.mZombie) {
                if (this.mRemoteService == null) {
                    android.util.Slog.w(TAG, "Cannot ressurect sessions because remote service is null");
                } else {
                    this.mZombie = false;
                    resurrectSessionsLocked();
                }
            }
        }
    }

    private void resurrectSessionsLocked() {
        int numSessions = this.mSessions.size();
        if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
            android.util.Slog.d(TAG, "Ressurrecting remote service (" + this.mRemoteService + ") on " + numSessions + " sessions");
        }
        for (int i = 0; i < numSessions; i++) {
            com.android.server.contentcapture.ContentCaptureServerSession session = this.mSessions.valueAt(i);
            session.resurrectLocked();
        }
    }

    void onPackageUpdatingLocked() {
        int numSessions = this.mSessions.size();
        if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
            android.util.Slog.d(TAG, "Pausing " + numSessions + " sessions while package is updating");
        }
        for (int i = 0; i < numSessions; i++) {
            com.android.server.contentcapture.ContentCaptureServerSession session = this.mSessions.valueAt(i);
            session.pauseLocked();
        }
    }

    void onPackageUpdatedLocked() {
        updateRemoteServiceLocked(!isEnabledLocked());
        resurrectSessionsLocked();
    }

    public void startSessionLocked(android.os.IBinder activityToken, android.os.IBinder shareableActivityToken, android.content.pm.ActivityPresentationInfo activityPresentationInfo, int sessionId, int uid, int flags, com.android.internal.os.IResultReceiver clientReceiver) {
        if (activityPresentationInfo == null) {
            android.util.Slog.w(TAG, "basic activity info is null");
            android.service.contentcapture.ContentCaptureService.setClientState(clientReceiver, 260, (android.os.IBinder) null);
            return;
        }
        int taskId = activityPresentationInfo.taskId;
        int displayId = activityPresentationInfo.displayId;
        android.content.ComponentName componentName = activityPresentationInfo.componentName;
        boolean whiteListed = ((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).mGlobalContentCaptureOptions.isWhitelisted(this.mUserId, componentName) || ((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).mGlobalContentCaptureOptions.isWhitelisted(this.mUserId, componentName.getPackageName());
        android.content.ComponentName serviceComponentName = getServiceComponentName();
        boolean enabled = isEnabledLocked();
        if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).mRequestsHistory != null) {
            java.lang.String historyItem = "id=" + sessionId + " uid=" + uid + " a=" + android.content.ComponentName.flattenToShortString(componentName) + " t=" + taskId + " d=" + displayId + " s=" + android.content.ComponentName.flattenToShortString(serviceComponentName) + " u=" + this.mUserId + " f=" + flags + (enabled ? "" : " (disabled)") + " w=" + whiteListed;
            ((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).mRequestsHistory.log(historyItem);
        }
        if (!enabled) {
            android.service.contentcapture.ContentCaptureService.setClientState(clientReceiver, 20, (android.os.IBinder) null);
            com.android.server.contentcapture.ContentCaptureMetricsLogger.writeSessionEvent(sessionId, 3, 20, serviceComponentName, false);
            return;
        }
        if (serviceComponentName == null) {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
                android.util.Slog.d(TAG, "startSession(" + activityToken + "): hold your horses");
                return;
            }
            return;
        }
        if (!whiteListed) {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
                android.util.Slog.d(TAG, "startSession(" + componentName + "): package or component not whitelisted");
            }
            android.service.contentcapture.ContentCaptureService.setClientState(clientReceiver, 516, (android.os.IBinder) null);
            com.android.server.contentcapture.ContentCaptureMetricsLogger.writeSessionEvent(sessionId, 3, 516, serviceComponentName, false);
            return;
        }
        com.android.server.contentcapture.ContentCaptureServerSession existingSession = this.mSessions.get(sessionId);
        if (existingSession == null) {
            if (this.mRemoteService == null) {
                updateRemoteServiceLocked(false);
            }
            if (this.mRemoteService == null) {
                android.util.Slog.w(TAG, "startSession(id=" + existingSession + ", token=" + activityToken + ": ignoring because service is not set");
                android.service.contentcapture.ContentCaptureService.setClientState(clientReceiver, 20, (android.os.IBinder) null);
                com.android.server.contentcapture.ContentCaptureMetricsLogger.writeSessionEvent(sessionId, 3, 20, serviceComponentName, false);
                return;
            } else {
                this.mRemoteService.ensureBoundLocked();
                com.android.server.contentcapture.ContentCaptureServerSession newSession = new com.android.server.contentcapture.ContentCaptureServerSession(this.mLock, activityToken, new android.app.assist.ActivityId(taskId, shareableActivityToken), this, componentName, clientReceiver, taskId, displayId, sessionId, uid, flags);
                if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "startSession(): new session for " + android.content.ComponentName.flattenToShortString(componentName) + " and id " + sessionId);
                }
                this.mSessions.put(sessionId, newSession);
                newSession.notifySessionStartedLocked(clientReceiver);
                return;
            }
        }
        android.util.Slog.w(TAG, "startSession(id=" + existingSession + ", token=" + activityToken + ": ignoring because it already exists for " + existingSession.mActivityToken);
        android.service.contentcapture.ContentCaptureService.setClientState(clientReceiver, 12, (android.os.IBinder) null);
        com.android.server.contentcapture.ContentCaptureMetricsLogger.writeSessionEvent(sessionId, 3, 12, serviceComponentName, false);
    }

    public void finishSessionLocked(int sessionId) {
        if (!isEnabledLocked()) {
            return;
        }
        com.android.server.contentcapture.ContentCaptureServerSession session = this.mSessions.get(sessionId);
        if (session == null) {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
                android.util.Slog.d(TAG, "finishSession(): no session with id" + sessionId);
            }
        } else {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).verbose) {
                android.util.Slog.v(TAG, "finishSession(): id=" + sessionId);
            }
            session.removeSelfLocked(true);
        }
    }

    public void removeDataLocked(android.view.contentcapture.DataRemovalRequest request) {
        if (!isEnabledLocked()) {
            return;
        }
        assertCallerLocked(request.getPackageName());
        this.mRemoteService.onDataRemovalRequest(request);
    }

    public void onDataSharedLocked(android.view.contentcapture.DataShareRequest request, android.service.contentcapture.IDataShareCallback.Stub dataShareCallback) {
        if (!isEnabledLocked()) {
            return;
        }
        assertCallerLocked(request.getPackageName());
        this.mRemoteService.onDataShareRequest(request, dataShareCallback);
    }

    public android.content.ComponentName getServiceSettingsActivityLocked() {
        java.lang.String activityName;
        if (this.mInfo == null || (activityName = this.mInfo.getSettingsActivity()) == null) {
            return null;
        }
        java.lang.String packageName = this.mInfo.getServiceInfo().packageName;
        return new android.content.ComponentName(packageName, activityName);
    }

    private void assertCallerLocked(java.lang.String packageName) {
        android.content.pm.PackageManager pm = getContext().getPackageManager();
        int callingUid = android.os.Binder.getCallingUid();
        try {
            int packageUid = pm.getPackageUidAsUser(packageName, android.os.UserHandle.getCallingUserId());
            if (callingUid != packageUid && !((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).hasRunningActivity(callingUid, packageName)) {
                android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity hotwordDetectionServiceIdentity = ((android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class)).getHotwordDetectionServiceIdentity();
                boolean isHotwordDetectionServiceCall = hotwordDetectionServiceIdentity != null && callingUid == hotwordDetectionServiceIdentity.getIsolatedUid() && packageUid == hotwordDetectionServiceIdentity.getOwnerUid();
                if (!isHotwordDetectionServiceCall) {
                    java.lang.String[] packages = pm.getPackagesForUid(callingUid);
                    java.lang.String callingPackage = packages != null ? packages[0] : "uid-" + callingUid;
                    android.util.Slog.w(TAG, "App (package=" + callingPackage + ", UID=" + callingUid + ") passed package (" + packageName + ") owned by UID " + packageUid);
                    throw new java.lang.SecurityException("Invalid package: " + packageName);
                }
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.SecurityException("Could not verify UID for " + packageName);
        }
    }

    public boolean sendActivityAssistDataLocked(android.os.IBinder activityToken, android.os.Bundle data) {
        int id = getSessionId(activityToken);
        android.os.Bundle assistData = data.getBundle("data");
        android.app.assist.AssistStructure assistStructure = (android.app.assist.AssistStructure) data.getParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_STRUCTURE, android.app.assist.AssistStructure.class);
        android.app.assist.AssistContent assistContent = (android.app.assist.AssistContent) data.getParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT, android.app.assist.AssistContent.class);
        android.service.contentcapture.SnapshotData snapshotData = new android.service.contentcapture.SnapshotData(assistData, assistStructure, assistContent);
        if (id != 0) {
            com.android.server.contentcapture.ContentCaptureServerSession session = this.mSessions.get(id);
            session.sendActivitySnapshotLocked(snapshotData);
            return true;
        }
        if (this.mRemoteService == null) {
            return false;
        }
        this.mRemoteService.onActivitySnapshotRequest(0, snapshotData);
        android.util.Slog.d(TAG, "Notified activity assist data for activity: " + activityToken + " without a session Id");
        return true;
    }

    public void removeSessionLocked(int sessionId) {
        this.mSessions.remove(sessionId);
    }

    public boolean isContentCaptureServiceForUserLocked(int uid) {
        return uid == getServiceUidLocked();
    }

    private com.android.server.contentcapture.ContentCaptureServerSession getSession(android.os.IBinder activityToken) {
        for (int i = 0; i < this.mSessions.size(); i++) {
            com.android.server.contentcapture.ContentCaptureServerSession session = this.mSessions.valueAt(i);
            if (session.mActivityToken.equals(activityToken)) {
                return session;
            }
        }
        return null;
    }

    public void destroyLocked() {
        if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
            android.util.Slog.d(TAG, "destroyLocked()");
        }
        if (this.mRemoteService != null) {
            this.mRemoteService.destroy();
        }
        destroySessionsLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroySessionsLocked() {
        int numSessions = this.mSessions.size();
        for (int i = 0; i < numSessions; i++) {
            com.android.server.contentcapture.ContentCaptureServerSession session = this.mSessions.valueAt(i);
            session.destroyLocked(true);
        }
        this.mSessions.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void listSessionsLocked(java.util.ArrayList<java.lang.String> output) {
        int numSessions = this.mSessions.size();
        for (int i = 0; i < numSessions; i++) {
            com.android.server.contentcapture.ContentCaptureServerSession session = this.mSessions.valueAt(i);
            output.add(session.toShortString());
        }
    }

    android.util.ArraySet<android.view.contentcapture.ContentCaptureCondition> getContentCaptureConditionsLocked(java.lang.String packageName) {
        return this.mConditionsByPkg.get(packageName);
    }

    android.util.ArraySet<java.lang.String> getContentCaptureAllowlist() {
        android.util.ArraySet<java.lang.String> allowPackages;
        synchronized (this.mLock) {
            allowPackages = ((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).mGlobalContentCaptureOptions.getWhitelistedPackages(this.mUserId);
        }
        return allowPackages;
    }

    void onActivityEventLocked(android.app.assist.ActivityId activityId, android.content.ComponentName componentName, int type) {
        if (this.mRemoteService == null) {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
                android.util.Slog.d(this.mTag, "onActivityEvent(): no remote service");
            }
        } else if (this.mRemoteService.getServiceInterface() == null) {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).debug) {
                android.util.Slog.d(this.mTag, "onActivityEvent(): remote service is dead or unbound");
            }
        } else {
            android.service.contentcapture.ActivityEvent event = new android.service.contentcapture.ActivityEvent(activityId, componentName, type);
            if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).verbose) {
                android.util.Slog.v(this.mTag, "onActivityEvent(): " + event);
            }
            this.mRemoteService.onActivityLifecycleEvent(event);
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        super.dumpLocked(prefix, pw);
        java.lang.String prefix2 = prefix + "  ";
        pw.print(prefix);
        pw.print("Service Info: ");
        if (this.mInfo == null) {
            pw.println("N/A");
        } else {
            pw.println();
            this.mInfo.dump(prefix2, pw);
        }
        pw.print(prefix);
        pw.print("Zombie: ");
        pw.println(this.mZombie);
        if (this.mRemoteService != null) {
            pw.print(prefix);
            pw.println("remote service:");
            this.mRemoteService.dump(prefix2, pw);
        }
        if (this.mSessions.size() == 0) {
            pw.print(prefix);
            pw.println("no sessions");
            return;
        }
        int sessionsSize = this.mSessions.size();
        pw.print(prefix);
        pw.print("number sessions: ");
        pw.println(sessionsSize);
        for (int i = 0; i < sessionsSize; i++) {
            pw.print(prefix);
            pw.print("#");
            pw.println(i);
            com.android.server.contentcapture.ContentCaptureServerSession session = this.mSessions.valueAt(i);
            session.dumpLocked(prefix2, pw);
            pw.println();
        }
    }

    private int getSessionId(android.os.IBinder activityToken) {
        for (int i = 0; i < this.mSessions.size(); i++) {
            com.android.server.contentcapture.ContentCaptureServerSession session = this.mSessions.valueAt(i);
            if (session.isActivitySession(activityToken)) {
                return this.mSessions.keyAt(i);
            }
        }
        return 0;
    }

    private void resetContentCaptureWhitelistLocked() {
        if (((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).verbose) {
            android.util.Slog.v(TAG, "resetting content capture whitelist");
        }
        ((com.android.server.contentcapture.ContentCaptureManagerService) this.mMaster).mGlobalContentCaptureOptions.resetWhitelist(this.mUserId);
    }

    private final class ContentCaptureServiceRemoteCallback extends android.service.contentcapture.IContentCaptureServiceCallback.Stub {
        private ContentCaptureServiceRemoteCallback() {
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        public void setContentCaptureWhitelist(java.util.List<java.lang.String> packages, java.util.List<android.content.ComponentName> activities) throws java.lang.Throwable {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).verbose) {
                android.util.Slog.v(com.android.server.contentcapture.ContentCapturePerUserService.TAG, "setContentCaptureWhitelist(" + (packages == null ? "null_packages" : packages.size() + " packages") + ", " + (activities == null ? "null_activities" : activities.size() + " activities") + ") for user " + com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId);
            }
            android.util.ArraySet oldList = ((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).mGlobalContentCaptureOptions.getWhitelistedPackages(com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId);
            android.util.EventLog.writeEvent(com.android.server.contentcapture.EventLogTags.CC_CURRENT_ALLOWLIST, java.lang.Integer.valueOf(com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId), java.lang.Integer.valueOf(com.android.internal.util.CollectionUtils.size(oldList)));
            ((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).mGlobalContentCaptureOptions.setWhitelist(com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId, packages, activities);
            android.util.EventLog.writeEvent(com.android.server.contentcapture.EventLogTags.CC_SET_ALLOWLIST, java.lang.Integer.valueOf(com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId), java.lang.Integer.valueOf(com.android.internal.util.CollectionUtils.size(packages)), java.lang.Integer.valueOf(com.android.internal.util.CollectionUtils.size(activities)));
            com.android.server.contentcapture.ContentCaptureMetricsLogger.writeSetWhitelistEvent(com.android.server.contentcapture.ContentCapturePerUserService.this.getServiceComponentName(), packages, activities);
            updateContentCaptureOptions(oldList);
            int numSessions = com.android.server.contentcapture.ContentCapturePerUserService.this.mSessions.size();
            if (numSessions <= 0) {
                return;
            }
            android.util.SparseBooleanArray blacklistedSessions = new android.util.SparseBooleanArray(numSessions);
            for (int i = 0; i < numSessions; i++) {
                com.android.server.contentcapture.ContentCaptureServerSession session = (com.android.server.contentcapture.ContentCaptureServerSession) com.android.server.contentcapture.ContentCapturePerUserService.this.mSessions.valueAt(i);
                boolean whitelisted = ((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).mGlobalContentCaptureOptions.isWhitelisted(com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId, session.appComponentName);
                if (!whitelisted) {
                    int sessionId = com.android.server.contentcapture.ContentCapturePerUserService.this.mSessions.keyAt(i);
                    if (((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).debug) {
                        android.util.Slog.d(com.android.server.contentcapture.ContentCapturePerUserService.TAG, "marking session " + sessionId + " (" + session.appComponentName + ") for un-whitelisting");
                    }
                    blacklistedSessions.append(sessionId, true);
                }
            }
            int numBlacklisted = blacklistedSessions.size();
            if (numBlacklisted <= 0) {
                return;
            }
            synchronized (com.android.server.contentcapture.ContentCapturePerUserService.this.mLock) {
                for (int i2 = 0; i2 < numBlacklisted; i2++) {
                    int sessionId2 = blacklistedSessions.keyAt(i2);
                    if (((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).debug) {
                        android.util.Slog.d(com.android.server.contentcapture.ContentCapturePerUserService.TAG, "un-whitelisting " + sessionId2);
                    }
                    ((com.android.server.contentcapture.ContentCaptureServerSession) com.android.server.contentcapture.ContentCapturePerUserService.this.mSessions.get(sessionId2)).setContentCaptureEnabledLocked(false);
                }
            }
        }

        public void setContentCaptureConditions(java.lang.String packageName, java.util.List<android.view.contentcapture.ContentCaptureCondition> conditions) {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).verbose) {
                android.util.Slog.v(com.android.server.contentcapture.ContentCapturePerUserService.TAG, "setContentCaptureConditions(" + packageName + "): " + (conditions == null ? "null" : conditions.size() + " conditions"));
            }
            synchronized (com.android.server.contentcapture.ContentCapturePerUserService.this.mLock) {
                if (conditions == null) {
                    com.android.server.contentcapture.ContentCapturePerUserService.this.mConditionsByPkg.remove(packageName);
                } else {
                    com.android.server.contentcapture.ContentCapturePerUserService.this.mConditionsByPkg.put(packageName, new android.util.ArraySet(conditions));
                }
            }
        }

        public void disableSelf() {
            if (((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).verbose) {
                android.util.Slog.v(com.android.server.contentcapture.ContentCapturePerUserService.TAG, "disableSelf()");
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                android.provider.Settings.Secure.putStringForUser(com.android.server.contentcapture.ContentCapturePerUserService.this.getContext().getContentResolver(), "content_capture_enabled", "0", com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId);
                android.os.Binder.restoreCallingIdentity(token);
                com.android.server.contentcapture.ContentCaptureMetricsLogger.writeServiceEvent(4, com.android.server.contentcapture.ContentCapturePerUserService.this.getServiceComponentName());
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        }

        public void writeSessionFlush(int sessionId, android.content.ComponentName app, android.service.contentcapture.FlushMetrics flushMetrics, android.content.ContentCaptureOptions options, int flushReason) {
            com.android.server.contentcapture.ContentCaptureMetricsLogger.writeSessionFlush(sessionId, com.android.server.contentcapture.ContentCapturePerUserService.this.getServiceComponentName(), flushMetrics, options, flushReason);
        }

        private void updateContentCaptureOptions(android.util.ArraySet<java.lang.String> oldList) throws java.lang.Throwable {
            android.util.ArraySet<java.lang.String> adding = ((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).mGlobalContentCaptureOptions.getWhitelistedPackages(com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId);
            android.util.EventLog.writeEvent(com.android.server.contentcapture.EventLogTags.CC_CURRENT_ALLOWLIST, java.lang.Integer.valueOf(com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId), java.lang.Integer.valueOf(com.android.internal.util.CollectionUtils.size(adding)));
            if (oldList != null && adding != null) {
                adding.removeAll((android.util.ArraySet<? extends java.lang.String>) oldList);
            }
            int addingCount = com.android.internal.util.CollectionUtils.size(adding);
            android.util.EventLog.writeEvent(com.android.server.contentcapture.EventLogTags.CC_UPDATE_OPTIONS, java.lang.Integer.valueOf(com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId), java.lang.Integer.valueOf(addingCount));
            for (int i = 0; i < addingCount; i++) {
                java.lang.String packageName = adding.valueAt(i);
                android.content.ContentCaptureOptions options = ((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).mGlobalContentCaptureOptions.getOptions(com.android.server.contentcapture.ContentCapturePerUserService.this.mUserId, packageName);
                ((com.android.server.contentcapture.ContentCaptureManagerService) com.android.server.contentcapture.ContentCapturePerUserService.this.mMaster).updateOptions(packageName, options);
            }
        }
    }
}
