package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class AutofillManagerServiceImpl extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.autofill.AutofillManagerServiceImpl, com.android.server.autofill.AutofillManagerService> {
    private static final int MAX_ABANDONED_SESSION_MILLIS = 30000;
    private static final int MAX_SESSION_ID_CREATE_TRIES = 2048;
    private static final java.lang.String TAG = "AutofillManagerServiceImpl";
    private static final java.util.Random sRandom = new java.util.Random();
    private android.service.autofill.FillEventHistory mAugmentedAutofillEventHistory;
    private final com.android.server.autofill.AutofillManagerService.AutofillCompatState mAutofillCompatState;
    private com.android.server.autofill.IAutofillManagerServiceImplExt mAutofillManagerServiceImplExt;
    private android.os.RemoteCallbackList<android.view.autofill.IAutoFillManagerClient> mClients;
    private final com.android.server.contentcapture.ContentCaptureManagerInternal mContentCaptureManagerInternal;
    private final com.android.server.autofill.AutofillManagerService.DisabledInfoCache mDisabledInfoCache;
    private android.service.autofill.FillEventHistory mEventHistory;
    private final com.android.server.autofill.FieldClassificationStrategy mFieldClassificationStrategy;
    private final android.os.Handler mHandler;
    private android.service.autofill.AutofillServiceInfo mInfo;
    private final com.android.server.inputmethod.InputMethodManagerInternal mInputMethodManagerInternal;
    private long mLastPrune;
    private final com.android.internal.logging.MetricsLogger mMetricsLogger;
    private com.android.server.autofill.RemoteAugmentedAutofillService mRemoteAugmentedAutofillService;
    private android.content.pm.ServiceInfo mRemoteAugmentedAutofillServiceInfo;
    private com.android.server.autofill.RemoteFieldClassificationService mRemoteFieldClassificationService;
    private android.content.pm.ServiceInfo mRemoteFieldClassificationServiceInfo;
    private com.android.server.autofill.RemoteInlineSuggestionRenderService mRemoteInlineSuggestionRenderService;
    private final android.util.SparseArray<com.android.server.autofill.Session> mSessions;
    private final com.android.server.autofill.ui.AutoFillUI mUi;
    private final android.util.LocalLog mUiLatencyHistory;
    private android.service.autofill.UserData mUserData;
    private final android.util.LocalLog mWtfHistory;

    AutofillManagerServiceImpl(com.android.server.autofill.AutofillManagerService master, java.lang.Object lock, android.util.LocalLog uiLatencyHistory, android.util.LocalLog wtfHistory, int userId, com.android.server.autofill.ui.AutoFillUI ui, com.android.server.autofill.AutofillManagerService.AutofillCompatState autofillCompatState, boolean disabled, com.android.server.autofill.AutofillManagerService.DisabledInfoCache disableCache) {
        super(master, lock, userId);
        this.mMetricsLogger = new com.android.internal.logging.MetricsLogger();
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper(), null, true);
        this.mSessions = new android.util.SparseArray<>();
        this.mLastPrune = 0L;
        this.mAutofillManagerServiceImplExt = (com.android.server.autofill.IAutofillManagerServiceImplExt) system.ext.loader.core.ExtLoader.type(com.android.server.autofill.IAutofillManagerServiceImplExt.class).base(this).create();
        this.mUiLatencyHistory = uiLatencyHistory;
        this.mWtfHistory = wtfHistory;
        this.mUi = ui;
        this.mFieldClassificationStrategy = new com.android.server.autofill.FieldClassificationStrategy(getContext(), userId);
        this.mAutofillCompatState = autofillCompatState;
        this.mInputMethodManagerInternal = (com.android.server.inputmethod.InputMethodManagerInternal) com.android.server.LocalServices.getService(com.android.server.inputmethod.InputMethodManagerInternal.class);
        this.mContentCaptureManagerInternal = (com.android.server.contentcapture.ContentCaptureManagerInternal) com.android.server.LocalServices.getService(com.android.server.contentcapture.ContentCaptureManagerInternal.class);
        this.mDisabledInfoCache = disableCache;
        updateLocked(disabled);
    }

    boolean sendActivityAssistDataToContentCapture(android.os.IBinder activityToken, android.os.Bundle data) {
        if (this.mContentCaptureManagerInternal != null) {
            this.mContentCaptureManagerInternal.sendActivityAssistData(getUserId(), activityToken, data);
            return true;
        }
        return false;
    }

    void onBackKeyPressed() {
        com.android.server.autofill.RemoteAugmentedAutofillService remoteService = getRemoteAugmentedAutofillServiceLocked();
        if (remoteService != null) {
            remoteService.onDestroyAutofillWindowsRequest();
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected boolean updateLocked(boolean disabled) {
        forceRemoveAllSessionsLocked();
        boolean enabledChanged = super.updateLocked(disabled);
        if (enabledChanged) {
            if (!isEnabledLocked()) {
                int sessionCount = this.mSessions.size();
                for (int i = sessionCount - 1; i >= 0; i--) {
                    com.android.server.autofill.Session session = this.mSessions.valueAt(i);
                    session.removeFromServiceLocked();
                }
            }
            sendStateToClients(false);
        }
        updateRemoteAugmentedAutofillService();
        getRemoteInlineSuggestionRenderServiceLocked();
        return enabledChanged;
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = getContext().getPackageManager().queryIntentServicesAsUser(new android.content.Intent("android.service.autofill.AutofillService"), 8388736, this.mUserId);
        boolean serviceHasAutofillIntentFilter = false;
        java.util.Iterator<android.content.pm.ResolveInfo> it = resolveInfos.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.content.pm.ResolveInfo resolveInfo = it.next();
            android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo.getComponentName().equals(serviceComponent)) {
                serviceHasAutofillIntentFilter = true;
                break;
            }
        }
        if (!serviceHasAutofillIntentFilter) {
            android.util.Slog.w(TAG, "Autofill service from '" + serviceComponent.getPackageName() + "' doesnot have intent filter android.service.autofill.AutofillService");
            throw new java.lang.SecurityException("Service does not declare intent filter android.service.autofill.AutofillService");
        }
        this.mInfo = new android.service.autofill.AutofillServiceInfo(getContext(), serviceComponent, this.mUserId);
        return this.mInfo.getServiceInfo();
    }

    java.lang.String[] getUrlBarResourceIdsForCompatMode(java.lang.String packageName) {
        return this.mAutofillCompatState.getUrlBarResourceIds(packageName, this.mUserId);
    }

    int addClientLocked(android.view.autofill.IAutoFillManagerClient client, android.content.ComponentName componentName, boolean credmanRequested) {
        synchronized (this.mLock) {
            android.content.ComponentName credComponentName = getCredentialAutofillService(getContext());
            if (!credmanRequested) {
                if (java.util.Objects.equals(credComponentName, this.mInfo == null ? null : this.mInfo.getServiceInfo().getComponentName())) {
                    return 0;
                }
            }
            if (this.mClients == null) {
                this.mClients = new android.os.RemoteCallbackList<>();
            }
            this.mClients.register(client);
            if (isEnabledLocked()) {
                return 1;
            }
            return (componentName != null && isAugmentedAutofillServiceAvailableLocked() && isWhitelistedForAugmentedAutofillLocked(componentName)) ? 8 : 0;
        }
    }

    void removeClientLocked(android.view.autofill.IAutoFillManagerClient client) {
        if (this.mClients != null) {
            this.mClients.unregister(client);
        }
    }

    void setAuthenticationResultLocked(android.os.Bundle data, int sessionId, int authenticationId, int uid) {
        com.android.server.autofill.Session session;
        if (isEnabledLocked() && (session = this.mSessions.get(sessionId)) != null && uid == session.uid) {
            synchronized (session.mLock) {
                session.setAuthenticationResultLocked(data, authenticationId);
            }
        }
    }

    void setHasCallback(int sessionId, int uid, boolean hasIt) {
        com.android.server.autofill.Session session;
        if (isEnabledLocked() && (session = this.mSessions.get(sessionId)) != null && uid == session.uid) {
            synchronized (this.mLock) {
                session.setHasCallbackLocked(hasIt);
            }
        }
    }

    long startSessionLocked(android.os.IBinder activityToken, int taskId, int clientUid, android.os.IBinder clientCallback, android.view.autofill.AutofillId autofillId, android.graphics.Rect virtualBounds, android.view.autofill.AutofillValue value, boolean hasCallback, android.content.ComponentName clientActivity, boolean compatMode, boolean bindInstantServiceAllowed, int flags) {
        boolean forAugmentedAutofillOnly;
        boolean forAugmentedAutofillOnly2 = (flags & 8) != 0;
        if (!isEnabledLocked() && !forAugmentedAutofillOnly2) {
            return 0L;
        }
        java.lang.String str = null;
        if (!forAugmentedAutofillOnly2 && isAutofillDisabledLocked(clientActivity)) {
            if (isWhitelistedForAugmentedAutofillLocked(clientActivity)) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "startSession(" + clientActivity + "): disabled by service but whitelisted for augmented autofill");
                }
                forAugmentedAutofillOnly = true;
            } else {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "startSession(" + clientActivity + "): ignored because disabled by service and not whitelisted for augmented autofill");
                }
                android.view.autofill.IAutoFillManagerClient client = android.view.autofill.IAutoFillManagerClient.Stub.asInterface(clientCallback);
                try {
                    client.setSessionFinished(4, (java.util.List) null);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Could not notify " + clientActivity + " that it's disabled: " + e);
                }
                return 2147483647L;
            }
        } else {
            forAugmentedAutofillOnly = forAugmentedAutofillOnly2;
        }
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "startSession(): token=" + activityToken + ", flags=" + flags + ", forAugmentedAutofillOnly=" + forAugmentedAutofillOnly);
        }
        pruneAbandonedSessionsLocked();
        boolean forAugmentedAutofillOnly3 = forAugmentedAutofillOnly;
        com.android.server.autofill.Session newSession = createSessionByTokenLocked(activityToken, taskId, clientUid, clientCallback, hasCallback, clientActivity, compatMode, bindInstantServiceAllowed, forAugmentedAutofillOnly, flags);
        if (newSession == null) {
            return 2147483647L;
        }
        if (this.mInfo != null) {
            str = this.mInfo.getServiceInfo().packageName;
        }
        java.lang.String servicePackageName = str;
        java.lang.String historyItem = "id=" + newSession.id + " uid=" + clientUid + " a=" + clientActivity.toShortString() + " s=" + servicePackageName + " u=" + this.mUserId + " i=" + autofillId + " b=" + virtualBounds + " hc=" + hasCallback + " f=" + flags + " aa=" + forAugmentedAutofillOnly3;
        ((com.android.server.autofill.AutofillManagerService) this.mMaster).logRequestLocked(historyItem);
        synchronized (newSession.mLock) {
            newSession.updateLocked(autofillId, virtualBounds, value, 1, flags);
        }
        if (forAugmentedAutofillOnly3) {
            long result = ((long) newSession.id) | 4294967296L;
            return result;
        }
        return newSession.id;
    }

    private void pruneAbandonedSessionsLocked() {
        long now = java.lang.System.currentTimeMillis();
        if (this.mLastPrune < now - 30000) {
            this.mLastPrune = now;
            if (this.mSessions.size() > 0) {
                new com.android.server.autofill.AutofillManagerServiceImpl.PruneTask().execute(new java.lang.Void[0]);
            }
        }
    }

    void setAutofillFailureLocked(int sessionId, int uid, java.util.List<android.view.autofill.AutofillId> ids) {
        if (!isEnabledLocked()) {
            android.util.Slog.wtf(TAG, "Service not enabled");
            return;
        }
        com.android.server.autofill.Session session = this.mSessions.get(sessionId);
        if (session == null || uid != session.uid) {
            android.util.Slog.v(TAG, "setAutofillFailure(): no session for " + sessionId + "(" + uid + ")");
        } else {
            session.setAutofillFailureLocked(ids);
        }
    }

    void setViewAutofilledLocked(int sessionId, int uid, android.view.autofill.AutofillId id) {
        if (!isEnabledLocked()) {
            android.util.Slog.wtf(TAG, "Service not enabled");
            return;
        }
        com.android.server.autofill.Session session = this.mSessions.get(sessionId);
        if (session == null || uid != session.uid) {
            android.util.Slog.v(TAG, "setViewAutofilled(): no session for " + sessionId + "(" + uid + ")");
        } else {
            session.setViewAutofilledLocked(id);
        }
    }

    void finishSessionLocked(int sessionId, int uid, int commitReason) {
        if (!isEnabledLocked()) {
            android.util.Slog.wtf(TAG, "Service not enabled");
            return;
        }
        com.android.server.autofill.Session session = this.mSessions.get(sessionId);
        if (session == null || uid != session.uid) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "finishSessionLocked(): no session for " + sessionId + "(" + uid + ")");
                return;
            }
            return;
        }
        int commitReason2 = this.mAutofillManagerServiceImplExt.hookHandleCommitReason(commitReason);
        if (commitReason != commitReason2) {
            com.android.server.autofill.ISessionExt sessionExt = session.getWrapper().getSessionExt();
            session.getWrapper().getSessionExt();
            sessionExt.hookSetOnSaveRequestReason(1);
        } else {
            com.android.server.autofill.ISessionExt sessionExt2 = session.getWrapper().getSessionExt();
            session.getWrapper().getSessionExt();
            sessionExt2.hookSetOnSaveRequestReason(0);
        }
        com.android.server.autofill.Session.SaveResult saveResult = session.showSaveLocked();
        session.logContextCommittedLocked(saveResult.getNoSaveUiReason(), commitReason2);
        if (saveResult.isLogSaveShown()) {
            session.logSaveUiShown();
        }
        boolean finished = saveResult.isRemoveSession();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "finishSessionLocked(): session finished? " + finished + ", showing save UI? " + saveResult.isLogSaveShown());
        }
        if (finished) {
            session.removeFromServiceLocked();
        }
    }

    void cancelSessionLocked(int sessionId, int uid) {
        if (!isEnabledLocked()) {
            return;
        }
        com.android.server.autofill.Session session = this.mSessions.get(sessionId);
        if (session == null || uid != session.uid) {
            android.util.Slog.w(TAG, "cancelSessionLocked(): no session for " + sessionId + "(" + uid + ")");
        } else {
            session.removeFromServiceLocked();
        }
    }

    void disableOwnedAutofillServicesLocked(int uid) {
        android.util.Slog.i(TAG, "disableOwnedServices(" + uid + "): " + this.mInfo);
        if (this.mInfo == null) {
            return;
        }
        android.content.pm.ServiceInfo serviceInfo = this.mInfo.getServiceInfo();
        if (serviceInfo.applicationInfo.uid != uid) {
            android.util.Slog.w(TAG, "disableOwnedServices(): ignored when called by UID " + uid + " instead of " + serviceInfo.applicationInfo.uid + " for service " + this.mInfo);
            return;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String autoFillService = getComponentNameLocked();
            android.content.ComponentName componentName = serviceInfo.getComponentName();
            if (componentName.equals(android.content.ComponentName.unflattenFromString(autoFillService))) {
                this.mMetricsLogger.action(1135, componentName.getPackageName());
                android.provider.Settings.Secure.putStringForUser(getContext().getContentResolver(), "autofill_service", null, this.mUserId);
                forceRemoveAllSessionsLocked();
            } else {
                android.util.Slog.w(TAG, "disableOwnedServices(): ignored because current service (" + serviceInfo + ") does not match Settings (" + autoFillService + ")");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private com.android.server.autofill.Session createSessionByTokenLocked(android.os.IBinder clientActivityToken, int taskId, int clientUid, android.os.IBinder clientCallback, boolean hasCallback, android.content.ComponentName clientActivity, boolean compatMode, boolean bindInstantServiceAllowed, boolean forAugmentedAutofillOnly, int flags) {
        android.content.ComponentName serviceComponentName;
        com.android.server.autofill.AutofillManagerServiceImpl autofillManagerServiceImpl = this;
        int tries = 0;
        while (true) {
            int tries2 = tries + 1;
            if (tries2 > 2048) {
                android.util.Slog.w(TAG, "Cannot create session in 2048 tries");
                return null;
            }
            int sessionId = java.lang.Math.abs(sRandom.nextInt());
            if (sessionId == 0 || sessionId == Integer.MAX_VALUE || autofillManagerServiceImpl.mSessions.indexOfKey(sessionId) >= 0) {
                autofillManagerServiceImpl = autofillManagerServiceImpl;
                tries = tries2;
            } else {
                autofillManagerServiceImpl.assertCallerLocked(clientActivity, compatMode);
                if (autofillManagerServiceImpl.mInfo == null) {
                    serviceComponentName = null;
                } else {
                    serviceComponentName = autofillManagerServiceImpl.mInfo.getServiceInfo().getComponentName();
                }
                boolean isPrimaryCredential = (flags & 2048) != 0;
                com.android.server.autofill.Session newSession = new com.android.server.autofill.Session(this, autofillManagerServiceImpl.mUi, getContext(), autofillManagerServiceImpl.mHandler, autofillManagerServiceImpl.mUserId, autofillManagerServiceImpl.mLock, sessionId, taskId, clientUid, clientActivityToken, clientCallback, hasCallback, autofillManagerServiceImpl.mUiLatencyHistory, autofillManagerServiceImpl.mWtfHistory, serviceComponentName, clientActivity, compatMode, bindInstantServiceAllowed, forAugmentedAutofillOnly, flags, autofillManagerServiceImpl.mInputMethodManagerInternal, isPrimaryCredential);
                this.mSessions.put(newSession.id, newSession);
                return newSession;
            }
        }
    }

    private void assertCallerLocked(android.content.ComponentName componentName, boolean compatMode) {
        java.lang.String packageName = componentName.getPackageName();
        android.content.pm.PackageManager pm = getContext().getPackageManager();
        int callingUid = android.os.Binder.getCallingUid();
        try {
            int packageUid = pm.getPackageUidAsUser(packageName, android.os.UserHandle.getCallingUserId());
            if (callingUid != packageUid && !((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).hasRunningActivity(callingUid, packageName)) {
                java.lang.String[] packages = pm.getPackagesForUid(callingUid);
                java.lang.String callingPackage = packages != null ? packages[0] : "uid-" + callingUid;
                android.util.Slog.w(TAG, "App (package=" + callingPackage + ", UID=" + callingUid + ") passed component (" + componentName + ") owned by UID " + packageUid);
                android.metrics.LogMaker log = new android.metrics.LogMaker(948).setPackageName(callingPackage).addTaggedData(908, getServicePackageName()).addTaggedData(949, componentName == null ? "null" : componentName.flattenToShortString());
                if (compatMode) {
                    log.addTaggedData(1414, 1);
                }
                this.mMetricsLogger.write(log);
                throw new java.lang.SecurityException("Invalid component: " + componentName);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.SecurityException("Could not verify UID for " + componentName);
        }
    }

    boolean restoreSession(int sessionId, int uid, android.os.IBinder activityToken, android.os.IBinder appCallback) {
        com.android.server.autofill.Session session = this.mSessions.get(sessionId);
        if (session == null || uid != session.uid) {
            return false;
        }
        session.switchActivity(activityToken, appCallback);
        return true;
    }

    boolean updateSessionLocked(int sessionId, int uid, android.view.autofill.AutofillId autofillId, android.graphics.Rect virtualBounds, android.view.autofill.AutofillValue value, int action, int flags) {
        com.android.server.autofill.Session session = this.mSessions.get(sessionId);
        if (session == null || session.uid != uid) {
            if ((flags & 1) != 0) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "restarting session " + sessionId + " due to manual request on " + autofillId);
                    return true;
                }
                return true;
            }
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "updateSessionLocked(): session gone for " + sessionId + "(" + uid + ")");
            }
            return false;
        }
        session.updateLocked(autofillId, virtualBounds, value, action, flags);
        return false;
    }

    void removeSessionLocked(int sessionId) {
        this.mSessions.remove(sessionId);
    }

    java.util.ArrayList<com.android.server.autofill.Session> getPreviousSessionsLocked(com.android.server.autofill.Session session) {
        int size = this.mSessions.size();
        java.util.ArrayList<com.android.server.autofill.Session> previousSessions = null;
        for (int i = 0; i < size; i++) {
            com.android.server.autofill.Session previousSession = this.mSessions.valueAt(i);
            if (previousSession.taskId == session.taskId && previousSession.id != session.id && (previousSession.getSaveInfoFlagsLocked() & 4) != 0) {
                if (previousSessions == null) {
                    previousSessions = new java.util.ArrayList<>(size);
                }
                previousSessions.add(previousSession);
            }
        }
        return previousSessions;
    }

    void handleSessionSave(com.android.server.autofill.Session session) {
        synchronized (this.mLock) {
            if (this.mSessions.get(session.id) == null) {
                android.util.Slog.w(TAG, "handleSessionSave(): already gone: " + session.id);
            } else {
                session.callSaveLocked();
            }
        }
    }

    void onPendingSaveUi(int operation, android.os.IBinder token) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "onPendingSaveUi(" + operation + "): " + token);
        }
        synchronized (this.mLock) {
            int sessionCount = this.mSessions.size();
            for (int i = sessionCount - 1; i >= 0; i--) {
                com.android.server.autofill.Session session = this.mSessions.valueAt(i);
                if (session.isSaveUiPendingForTokenLocked(token)) {
                    session.onPendingSaveUi(operation, token);
                    return;
                }
            }
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "No pending Save UI for token " + token + " and operation " + android.util.DebugUtils.flagsToString(android.view.autofill.AutofillManager.class, "PENDING_UI_OPERATION_", operation));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractPerUserSystemService
    public void handlePackageUpdateLocked(java.lang.String packageName) {
        android.content.pm.ServiceInfo serviceInfo = this.mFieldClassificationStrategy.getServiceInfo();
        if (serviceInfo != null && serviceInfo.packageName.equals(packageName)) {
            resetExtServiceLocked();
        }
    }

    void resetExtServiceLocked() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "reset autofill service in ExtServices.");
        }
        this.mFieldClassificationStrategy.reset();
        if (this.mRemoteInlineSuggestionRenderService != null) {
            this.mRemoteInlineSuggestionRenderService.destroy();
            this.mRemoteInlineSuggestionRenderService = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroyLocked() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "destroyLocked()");
        }
        resetExtServiceLocked();
        int numSessions = this.mSessions.size();
        android.util.ArraySet<com.android.server.autofill.RemoteFillService> remoteFillServices = new android.util.ArraySet<>(numSessions);
        for (int i = 0; i < numSessions; i++) {
            com.android.server.autofill.RemoteFillService remoteFillService = this.mSessions.valueAt(i).destroyLocked();
            if (remoteFillService != null) {
                remoteFillServices.add(remoteFillService);
            }
        }
        this.mSessions.clear();
        for (int i2 = 0; i2 < remoteFillServices.size(); i2++) {
            remoteFillServices.valueAt(i2).destroy();
        }
        sendStateToClients(true);
        if (this.mClients != null) {
            this.mClients.kill();
            this.mClients = null;
        }
    }

    void setLastResponseLocked(int sessionId, android.service.autofill.FillResponse response) {
        this.mEventHistory = new android.service.autofill.FillEventHistory(sessionId, response.getClientState());
    }

    void setLastAugmentedAutofillResponse(int sessionId) {
        synchronized (this.mLock) {
            this.mAugmentedAutofillEventHistory = new android.service.autofill.FillEventHistory(sessionId, null);
        }
    }

    void resetLastResponse() {
        synchronized (this.mLock) {
            this.mEventHistory = null;
        }
    }

    void resetLastAugmentedAutofillResponse() {
        synchronized (this.mLock) {
            this.mAugmentedAutofillEventHistory = null;
        }
    }

    private boolean isValidEventLocked(java.lang.String method, int sessionId) {
        if (this.mEventHistory == null) {
            android.util.Slog.w(TAG, method + ": not logging event because history is null");
            return false;
        }
        if (sessionId != this.mEventHistory.getSessionId()) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, method + ": not logging event for session " + sessionId + " because tracked session is " + this.mEventHistory.getSessionId());
            }
            return false;
        }
        return true;
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    void setAuthenticationSelected(int sessionId, android.os.Bundle clientState, int uiType) {
        synchronized (this.mLock) {
            try {
                if (isValidEventLocked("setAuthenticationSelected()", sessionId)) {
                    this.mEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(2, null, clientState, null, null, null, null, null, null, null, null, 0, uiType));
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    void logDatasetAuthenticationSelected(java.lang.String selectedDataset, int sessionId, android.os.Bundle clientState, int uiType) {
        synchronized (this.mLock) {
            try {
                if (isValidEventLocked("logDatasetAuthenticationSelected()", sessionId)) {
                    this.mEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(1, selectedDataset, clientState, null, null, null, null, null, null, null, null, 0, uiType));
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    void logSaveShown(int sessionId, android.os.Bundle clientState) {
        synchronized (this.mLock) {
            try {
                if (isValidEventLocked("logSaveShown()", sessionId)) {
                    this.mEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(3, null, clientState, null, null, null, null, null, null, null, null));
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    void logDatasetSelected(java.lang.String selectedDataset, int sessionId, android.os.Bundle clientState, int uiType) {
        synchronized (this.mLock) {
            try {
                if (isValidEventLocked("logDatasetSelected()", sessionId)) {
                    this.mEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(0, selectedDataset, clientState, null, null, null, null, null, null, null, null, 0, uiType));
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    void logDatasetShown(int sessionId, android.os.Bundle clientState, int uiType) {
        synchronized (this.mLock) {
            try {
                if (isValidEventLocked("logDatasetShown", sessionId)) {
                    this.mEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(5, null, clientState, null, null, null, null, null, null, null, null, 0, uiType));
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    void logViewEntered(int sessionId, android.os.Bundle clientState) {
        synchronized (this.mLock) {
            try {
                if (isValidEventLocked("logViewEntered", sessionId)) {
                    if (this.mEventHistory.getEvents() != null) {
                        for (android.service.autofill.FillEventHistory.Event event : this.mEventHistory.getEvents()) {
                            if (event.getType() == 6) {
                                android.util.Slog.v(TAG, "logViewEntered: already logged TYPE_VIEW_REQUESTED_AUTOFILL");
                                return;
                            }
                        }
                    }
                    this.mEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(6, null, clientState, null, null, null, null, null, null, null, null));
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    void logAugmentedAutofillAuthenticationSelected(int sessionId, java.lang.String selectedDataset, android.os.Bundle clientState) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mAugmentedAutofillEventHistory != null) {
                        if (this.mAugmentedAutofillEventHistory.getSessionId() == sessionId) {
                            this.mAugmentedAutofillEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(1, selectedDataset, clientState, null, null, null, null, null, null, null, null));
                        }
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

    void logAugmentedAutofillSelected(int sessionId, java.lang.String suggestionId, android.os.Bundle clientState) {
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mAugmentedAutofillEventHistory != null) {
                        if (this.mAugmentedAutofillEventHistory.getSessionId() == sessionId) {
                            this.mAugmentedAutofillEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(0, suggestionId, clientState, null, null, null, null, null, null, null, null));
                        }
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

    void logAugmentedAutofillShown(int sessionId, android.os.Bundle clientState) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mAugmentedAutofillEventHistory != null) {
                        if (this.mAugmentedAutofillEventHistory.getSessionId() == sessionId) {
                            this.mAugmentedAutofillEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(5, null, clientState, null, null, null, null, null, null, null, null, 0, 2));
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    void logContextCommittedLocked(int sessionId, android.os.Bundle clientState, java.util.ArrayList<java.lang.String> selectedDatasets, android.util.ArraySet<java.lang.String> ignoredDatasets, java.util.ArrayList<android.view.autofill.AutofillId> changedFieldIds, java.util.ArrayList<java.lang.String> changedDatasetIds, java.util.ArrayList<android.view.autofill.AutofillId> manuallyFilledFieldIds, java.util.ArrayList<java.util.ArrayList<java.lang.String>> manuallyFilledDatasetIds, android.content.ComponentName appComponentName, boolean compatMode) {
        logContextCommittedLocked(sessionId, clientState, selectedDatasets, ignoredDatasets, changedFieldIds, changedDatasetIds, manuallyFilledFieldIds, manuallyFilledDatasetIds, null, null, appComponentName, compatMode, 0);
    }

    void logContextCommittedLocked(int sessionId, android.os.Bundle clientState, java.util.ArrayList<java.lang.String> selectedDatasets, android.util.ArraySet<java.lang.String> ignoredDatasets, java.util.ArrayList<android.view.autofill.AutofillId> changedFieldIds, java.util.ArrayList<java.lang.String> changedDatasetIds, java.util.ArrayList<android.view.autofill.AutofillId> manuallyFilledFieldIds, java.util.ArrayList<java.util.ArrayList<java.lang.String>> manuallyFilledDatasetIds, java.util.ArrayList<android.view.autofill.AutofillId> detectedFieldIdsList, java.util.ArrayList<android.service.autofill.FieldClassification> detectedFieldClassificationsList, android.content.ComponentName appComponentName, boolean compatMode, int saveDialogNotShowReason) {
        android.service.autofill.FieldClassification[] detectedFieldClassifications;
        if (isValidEventLocked("logDatasetNotSelected()", sessionId)) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "logContextCommitted() with FieldClassification: id=" + sessionId + ", selectedDatasets=" + selectedDatasets + ", ignoredDatasetIds=" + ignoredDatasets + ", changedAutofillIds=" + changedFieldIds + ", changedDatasetIds=" + changedDatasetIds + ", manuallyFilledFieldIds=" + manuallyFilledFieldIds + ", detectedFieldIds=" + detectedFieldIdsList + ", detectedFieldClassifications=" + detectedFieldClassificationsList + ", appComponentName=" + appComponentName.toShortString() + ", compatMode=" + compatMode + ", saveDialogNotShowReason=" + saveDialogNotShowReason);
            }
            android.view.autofill.AutofillId[] detectedFieldsIds = null;
            if (detectedFieldIdsList == null) {
                detectedFieldClassifications = null;
            } else {
                android.view.autofill.AutofillId[] detectedFieldsIds2 = new android.view.autofill.AutofillId[detectedFieldIdsList.size()];
                detectedFieldIdsList.toArray(detectedFieldsIds2);
                android.service.autofill.FieldClassification[] detectedFieldClassifications2 = new android.service.autofill.FieldClassification[detectedFieldClassificationsList.size()];
                detectedFieldClassificationsList.toArray(detectedFieldClassifications2);
                int numberFields = detectedFieldsIds2.length;
                int totalSize = 0;
                float totalScore = 0.0f;
                int i = 0;
                while (i < numberFields) {
                    android.service.autofill.FieldClassification fc = detectedFieldClassifications2[i];
                    java.util.List<android.service.autofill.FieldClassification.Match> matches = fc.getMatches();
                    android.view.autofill.AutofillId[] detectedFieldsIds3 = detectedFieldsIds2;
                    int size = matches.size();
                    totalSize += size;
                    android.service.autofill.FieldClassification[] detectedFieldClassifications3 = detectedFieldClassifications2;
                    for (int j = 0; j < size; j++) {
                        totalScore += matches.get(j).getScore();
                    }
                    i++;
                    detectedFieldsIds2 = detectedFieldsIds3;
                    detectedFieldClassifications2 = detectedFieldClassifications3;
                }
                int averageScore = (int) ((100.0f * totalScore) / totalSize);
                this.mMetricsLogger.write(com.android.server.autofill.Helper.newLogMaker(1273, appComponentName, getServicePackageName(), sessionId, compatMode).setCounterValue(numberFields).addTaggedData(1274, java.lang.Integer.valueOf(averageScore)));
                detectedFieldsIds = detectedFieldsIds2;
                detectedFieldClassifications = detectedFieldClassifications2;
            }
            this.mEventHistory.addEvent(new android.service.autofill.FillEventHistory.Event(4, null, clientState, selectedDatasets, ignoredDatasets, changedFieldIds, changedDatasetIds, manuallyFilledFieldIds, manuallyFilledDatasetIds, detectedFieldsIds, detectedFieldClassifications, saveDialogNotShowReason));
        }
    }

    android.service.autofill.FillEventHistory getFillEventHistory(int callingUid) {
        synchronized (this.mLock) {
            if (this.mEventHistory != null && isCalledByServiceLocked("getFillEventHistory", callingUid)) {
                return this.mEventHistory;
            }
            if (this.mAugmentedAutofillEventHistory != null && isCalledByAugmentedAutofillServiceLocked("getFillEventHistory", callingUid)) {
                return this.mAugmentedAutofillEventHistory;
            }
            return null;
        }
    }

    android.service.autofill.UserData getUserData() {
        android.service.autofill.UserData userData;
        synchronized (this.mLock) {
            userData = this.mUserData;
        }
        return userData;
    }

    android.service.autofill.UserData getUserData(int callingUid) {
        synchronized (this.mLock) {
            if (isCalledByServiceLocked("getUserData", callingUid)) {
                return this.mUserData;
            }
            return null;
        }
    }

    void setUserData(int callingUid, android.service.autofill.UserData userData) {
        synchronized (this.mLock) {
            if (isCalledByServiceLocked("setUserData", callingUid)) {
                this.mUserData = userData;
                int numberFields = this.mUserData == null ? 0 : this.mUserData.getCategoryIds().length;
                this.mMetricsLogger.write(new android.metrics.LogMaker(1272).setPackageName(getServicePackageName()).addTaggedData(914, java.lang.Integer.valueOf(numberFields)));
            }
        }
    }

    private boolean isCalledByServiceLocked(java.lang.String methodName, int callingUid) {
        int serviceUid = getServiceUidLocked();
        if (serviceUid != callingUid) {
            android.util.Slog.w(TAG, methodName + "() called by UID " + callingUid + ", but service UID is " + serviceUid);
            return false;
        }
        return true;
    }

    int getSupportedSmartSuggestionModesLocked() {
        return ((com.android.server.autofill.AutofillManagerService) this.mMaster).getSupportedSmartSuggestionModesLocked();
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        super.dumpLocked(prefix, pw);
        java.lang.String prefix2 = prefix + "  ";
        pw.print(prefix);
        pw.print("UID: ");
        pw.println(getServiceUidLocked());
        pw.print(prefix);
        pw.print("Autofill Service Info: ");
        if (this.mInfo == null) {
            pw.println("N/A");
        } else {
            pw.println();
            this.mInfo.dump(prefix2, pw);
        }
        pw.print(prefix);
        pw.print("Default component: ");
        pw.println(getContext().getString(android.R.string.config_defaultCredentialManagerAutofillService));
        pw.println();
        pw.print(prefix);
        pw.println("mAugmentedAutofillName: ");
        pw.print(prefix2);
        ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedAutofillResolver.dumpShort(pw, this.mUserId);
        pw.println();
        if (this.mRemoteAugmentedAutofillService != null) {
            pw.print(prefix);
            pw.println("RemoteAugmentedAutofillService: ");
            this.mRemoteAugmentedAutofillService.dump(prefix2, pw);
        }
        if (this.mRemoteAugmentedAutofillServiceInfo != null) {
            pw.print(prefix);
            pw.print("RemoteAugmentedAutofillServiceInfo: ");
            pw.println(this.mRemoteAugmentedAutofillServiceInfo);
        }
        pw.println();
        pw.print(prefix);
        pw.println("mFieldClassificationService for system detection");
        pw.print(prefix2);
        pw.print("Default component: ");
        pw.println(getContext().getString(android.R.string.config_defaultNetworkScorerPackageName));
        pw.print(prefix2);
        ((com.android.server.autofill.AutofillManagerService) this.mMaster).mFieldClassificationResolver.dumpShort(pw, this.mUserId);
        pw.println();
        if (this.mRemoteFieldClassificationService != null) {
            pw.print(prefix);
            pw.println("RemoteFieldClassificationService: ");
            this.mRemoteFieldClassificationService.dump(prefix2, pw);
        } else {
            pw.print(prefix);
            pw.println("mRemoteFieldClassificationService: null");
        }
        if (this.mRemoteFieldClassificationServiceInfo != null) {
            pw.print(prefix);
            pw.print("RemoteFieldClassificationServiceInfo: ");
            pw.println(this.mRemoteFieldClassificationServiceInfo);
        } else {
            pw.print(prefix);
            pw.println("mRemoteFieldClassificationServiceInfo: null");
        }
        pw.println();
        pw.print(prefix);
        pw.print("Field classification enabled: ");
        pw.println(isFieldClassificationEnabledLocked());
        pw.print(prefix);
        pw.print("Compat pkgs: ");
        android.util.ArrayMap<java.lang.String, java.lang.Long> compatPkgs = getCompatibilityPackagesLocked();
        if (compatPkgs == null) {
            pw.println("N/A");
        } else {
            pw.println(compatPkgs);
        }
        pw.print(prefix);
        pw.print("Inline Suggestions Enabled: ");
        pw.println(isInlineSuggestionsEnabledLocked());
        pw.print(prefix);
        pw.print("Last prune: ");
        pw.println(this.mLastPrune);
        this.mDisabledInfoCache.dump(this.mUserId, prefix, pw);
        int size = this.mSessions.size();
        if (size == 0) {
            pw.print(prefix);
            pw.println("No sessions");
        } else {
            pw.print(prefix);
            pw.print(size);
            pw.println(" sessions:");
            for (int i = 0; i < size; i++) {
                pw.print(prefix);
                pw.print("#");
                pw.println(i + 1);
                this.mSessions.valueAt(i).dumpLocked(prefix2, pw);
            }
        }
        pw.print(prefix);
        pw.print("Clients: ");
        if (this.mClients == null) {
            pw.println("N/A");
        } else {
            pw.println();
            this.mClients.dump(pw, prefix2);
        }
        if (this.mEventHistory == null || this.mEventHistory.getEvents() == null || this.mEventHistory.getEvents().size() == 0) {
            pw.print(prefix);
            pw.println("No event on last fill response");
        } else {
            pw.print(prefix);
            pw.println("Events of last fill response:");
            pw.print(prefix);
            int numEvents = this.mEventHistory.getEvents().size();
            for (int i2 = 0; i2 < numEvents; i2++) {
                android.service.autofill.FillEventHistory.Event event = this.mEventHistory.getEvents().get(i2);
                pw.println("  " + i2 + ": eventType=" + event.getType() + " datasetId=" + event.getDatasetId());
            }
        }
        pw.print(prefix);
        pw.print("User data: ");
        if (this.mUserData == null) {
            pw.println("N/A");
        } else {
            pw.println();
            this.mUserData.dump(prefix2, pw);
        }
        pw.print(prefix);
        pw.println("Field Classification strategy: ");
        this.mFieldClassificationStrategy.dump(prefix2, pw);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceRemoveAllSessionsLocked() {
        int sessionCount = this.mSessions.size();
        if (sessionCount == 0) {
            this.mUi.destroyAll(null, null, false);
            return;
        }
        for (int i = sessionCount - 1; i >= 0; i--) {
            this.mSessions.valueAt(i).forceRemoveFromServiceLocked();
        }
    }

    void forceRemoveForAugmentedOnlySessionsLocked() {
        int sessionCount = this.mSessions.size();
        for (int i = sessionCount - 1; i >= 0; i--) {
            this.mSessions.valueAt(i).forceRemoveFromServiceIfForAugmentedOnlyLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceRemoveFinishedSessionsLocked() {
        int sessionCount = this.mSessions.size();
        for (int i = sessionCount - 1; i >= 0; i--) {
            com.android.server.autofill.Session session = this.mSessions.valueAt(i);
            if (session.isSaveUiShowingLocked()) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "destroyFinishedSessionsLocked(): " + session.id);
                }
                session.forceRemoveFromServiceLocked();
            } else {
                session.destroyAugmentedAutofillWindowsLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void listSessionsLocked(java.util.ArrayList<java.lang.String> output) {
        java.lang.String service;
        java.lang.String augmentedService;
        int numSessions = this.mSessions.size();
        if (numSessions <= 0) {
            return;
        }
        for (int i = 0; i < numSessions; i++) {
            int id = this.mSessions.keyAt(i);
            if (this.mInfo == null) {
                service = "no_svc";
            } else {
                service = this.mInfo.getServiceInfo().getComponentName().flattenToShortString();
            }
            if (this.mRemoteAugmentedAutofillServiceInfo == null) {
                augmentedService = "no_aug";
            } else {
                augmentedService = this.mRemoteAugmentedAutofillServiceInfo.getComponentName().flattenToShortString();
            }
            output.add(java.lang.String.format("%d:%s:%s", java.lang.Integer.valueOf(id), service, augmentedService));
        }
    }

    android.util.ArrayMap<java.lang.String, java.lang.Long> getCompatibilityPackagesLocked() {
        if (this.mInfo != null) {
            return this.mInfo.getCompatibilityPackages();
        }
        return null;
    }

    boolean isInlineSuggestionsEnabledLocked() {
        if (this.mInfo != null) {
            return this.mInfo.isInlineSuggestionsEnabled();
        }
        return false;
    }

    void requestSavedPasswordCount(com.android.internal.os.IResultReceiver receiver) {
        com.android.server.autofill.RemoteFillService remoteService = new com.android.server.autofill.RemoteFillService(getContext(), this.mInfo.getServiceInfo().getComponentName(), this.mUserId, null, ((com.android.server.autofill.AutofillManagerService) this.mMaster).isInstantServiceAllowed(), ((com.android.server.autofill.AutofillManagerService) this.mMaster).mCredentialAutofillService);
        remoteService.onSavedPasswordCountRequest(receiver);
    }

    com.android.server.autofill.RemoteAugmentedAutofillService getRemoteAugmentedAutofillServiceLocked() {
        if (this.mRemoteAugmentedAutofillService == null) {
            java.lang.String serviceName = ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedAutofillResolver.getServiceName(this.mUserId);
            if (serviceName == null) {
                if (((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "getRemoteAugmentedAutofillServiceLocked(): not set");
                }
                return null;
            }
            android.util.Pair<android.content.pm.ServiceInfo, android.content.ComponentName> pair = com.android.server.autofill.RemoteAugmentedAutofillService.getComponentName(serviceName, this.mUserId, ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedAutofillResolver.isTemporary(this.mUserId));
            if (pair == null) {
                return null;
            }
            this.mRemoteAugmentedAutofillServiceInfo = (android.content.pm.ServiceInfo) pair.first;
            android.content.ComponentName componentName = (android.content.ComponentName) pair.second;
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "getRemoteAugmentedAutofillServiceLocked(): " + componentName);
            }
            com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks callbacks = new com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks() { // from class: com.android.server.autofill.AutofillManagerServiceImpl.1
                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void resetLastResponse() {
                    com.android.server.autofill.AutofillManagerServiceImpl.this.resetLastAugmentedAutofillResponse();
                }

                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void setLastResponse(int sessionId) {
                    com.android.server.autofill.AutofillManagerServiceImpl.this.setLastAugmentedAutofillResponse(sessionId);
                }

                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void logAugmentedAutofillShown(int sessionId, android.os.Bundle clientState) throws java.lang.Throwable {
                    com.android.server.autofill.AutofillManagerServiceImpl.this.logAugmentedAutofillShown(sessionId, clientState);
                }

                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void logAugmentedAutofillSelected(int sessionId, java.lang.String suggestionId, android.os.Bundle clientState) {
                    com.android.server.autofill.AutofillManagerServiceImpl.this.logAugmentedAutofillSelected(sessionId, suggestionId, clientState);
                }

                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void logAugmentedAutofillAuthenticationSelected(int sessionId, java.lang.String suggestionId, android.os.Bundle clientState) throws java.lang.Throwable {
                    com.android.server.autofill.AutofillManagerServiceImpl.this.logAugmentedAutofillAuthenticationSelected(sessionId, suggestionId, clientState);
                }

                public void onServiceDied(com.android.server.autofill.RemoteAugmentedAutofillService service) {
                    android.util.Slog.w(com.android.server.autofill.AutofillManagerServiceImpl.TAG, "remote augmented autofill service died");
                    com.android.server.autofill.RemoteAugmentedAutofillService remoteService = com.android.server.autofill.AutofillManagerServiceImpl.this.mRemoteAugmentedAutofillService;
                    if (remoteService != null) {
                        remoteService.unbind();
                    }
                    com.android.server.autofill.AutofillManagerServiceImpl.this.mRemoteAugmentedAutofillService = null;
                }
            };
            int serviceUid = this.mRemoteAugmentedAutofillServiceInfo.applicationInfo.uid;
            this.mRemoteAugmentedAutofillService = new com.android.server.autofill.RemoteAugmentedAutofillService(getContext(), serviceUid, componentName, this.mUserId, callbacks, ((com.android.server.autofill.AutofillManagerService) this.mMaster).isInstantServiceAllowed(), ((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose, ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedServiceIdleUnbindTimeoutMs, ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedServiceRequestTimeoutMs);
        }
        return this.mRemoteAugmentedAutofillService;
    }

    com.android.server.autofill.RemoteAugmentedAutofillService getRemoteAugmentedAutofillServiceIfCreatedLocked() {
        return this.mRemoteAugmentedAutofillService;
    }

    void updateRemoteAugmentedAutofillService() {
        synchronized (this.mLock) {
            if (this.mRemoteAugmentedAutofillService != null) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "updateRemoteAugmentedAutofillService(): destroying old remote service");
                }
                forceRemoveForAugmentedOnlySessionsLocked();
                this.mRemoteAugmentedAutofillService.unbind();
                this.mRemoteAugmentedAutofillService = null;
                this.mRemoteAugmentedAutofillServiceInfo = null;
                resetAugmentedAutofillWhitelistLocked();
            }
            boolean available = isAugmentedAutofillServiceAvailableLocked();
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "updateRemoteAugmentedAutofillService(): " + available);
            }
            if (available) {
                this.mRemoteAugmentedAutofillService = getRemoteAugmentedAutofillServiceLocked();
            }
        }
    }

    private boolean isAugmentedAutofillServiceAvailableLocked() {
        if (((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose) {
            android.util.Slog.v(TAG, "isAugmentedAutofillService(): setupCompleted=" + isSetupCompletedLocked() + ", disabled=" + isDisabledByUserRestrictionsLocked() + ", augmentedService=" + ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedAutofillResolver.getServiceName(this.mUserId));
        }
        if (!isSetupCompletedLocked() || isDisabledByUserRestrictionsLocked() || ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedAutofillResolver.getServiceName(this.mUserId) == null) {
            return false;
        }
        return true;
    }

    boolean isAugmentedAutofillServiceForUserLocked(int callingUid) {
        return this.mRemoteAugmentedAutofillServiceInfo != null && this.mRemoteAugmentedAutofillServiceInfo.applicationInfo.uid == callingUid;
    }

    boolean setAugmentedAutofillWhitelistLocked(java.util.List<java.lang.String> packages, java.util.List<android.content.ComponentName> activities, int callingUid) {
        java.lang.String serviceName;
        if (!isCalledByAugmentedAutofillServiceLocked("setAugmentedAutofillWhitelistLocked", callingUid)) {
            return false;
        }
        if (((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose) {
            android.util.Slog.v(TAG, "setAugmentedAutofillWhitelistLocked(packages=" + packages + ", activities=" + activities + ")");
        }
        allowlistForAugmentedAutofillPackages(packages, activities);
        if (this.mRemoteAugmentedAutofillServiceInfo != null) {
            serviceName = this.mRemoteAugmentedAutofillServiceInfo.getComponentName().flattenToShortString();
        } else {
            android.util.Slog.e(TAG, "setAugmentedAutofillWhitelistLocked(): no service");
            serviceName = "N/A";
        }
        android.metrics.LogMaker log = new android.metrics.LogMaker(1721).addTaggedData(908, serviceName);
        if (packages != null) {
            log.addTaggedData(1722, java.lang.Integer.valueOf(packages.size()));
        }
        if (activities != null) {
            log.addTaggedData(1723, java.lang.Integer.valueOf(activities.size()));
        }
        this.mMetricsLogger.write(log);
        return true;
    }

    private boolean isCalledByAugmentedAutofillServiceLocked(java.lang.String methodName, int callingUid) {
        com.android.server.autofill.RemoteAugmentedAutofillService service = getRemoteAugmentedAutofillServiceLocked();
        if (service == null) {
            android.util.Slog.w(TAG, methodName + "() called by UID " + callingUid + ", but there is no augmented autofill service defined for user " + getUserId());
            return false;
        }
        if (getAugmentedAutofillServiceUidLocked() != callingUid) {
            android.util.Slog.w(TAG, methodName + "() called by UID " + callingUid + ", but service UID is " + getAugmentedAutofillServiceUidLocked() + " for user " + getUserId());
            return false;
        }
        return true;
    }

    private android.content.ComponentName getCredentialAutofillService(android.content.Context context) {
        android.content.ComponentName componentName = null;
        java.lang.String credentialManagerAutofillCompName = context.getResources().getString(android.R.string.config_defaultModuleMetadataProvider);
        if (credentialManagerAutofillCompName != null && !credentialManagerAutofillCompName.isEmpty()) {
            componentName = android.content.ComponentName.unflattenFromString(credentialManagerAutofillCompName);
        }
        if (componentName == null) {
            android.util.Slog.w(TAG, "Invalid CredentialAutofillService");
        }
        return componentName;
    }

    private int getAugmentedAutofillServiceUidLocked() {
        if (this.mRemoteAugmentedAutofillServiceInfo == null) {
            if (((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose) {
                android.util.Slog.v(TAG, "getAugmentedAutofillServiceUid(): no mRemoteAugmentedAutofillServiceInfo");
                return -1;
            }
            return -1;
        }
        return this.mRemoteAugmentedAutofillServiceInfo.applicationInfo.uid;
    }

    boolean isWhitelistedForAugmentedAutofillLocked(android.content.ComponentName componentName) {
        return ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedAutofillState.isWhitelisted(this.mUserId, componentName);
    }

    private void allowlistForAugmentedAutofillPackages(java.util.List<java.lang.String> packages, java.util.List<android.content.ComponentName> components) {
        synchronized (this.mLock) {
            if (((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose) {
                android.util.Slog.v(TAG, "whitelisting packages: " + packages + "and activities: " + components);
            }
            ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedAutofillState.setWhitelist(this.mUserId, packages, components);
        }
    }

    void resetAugmentedAutofillWhitelistLocked() {
        if (((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose) {
            android.util.Slog.v(TAG, "resetting augmented autofill whitelist");
        }
        ((com.android.server.autofill.AutofillManagerService) this.mMaster).mAugmentedAutofillState.resetWhitelist(this.mUserId);
    }

    private void sendStateToClients(boolean resetClient) {
        boolean resetSession;
        boolean isEnabled;
        synchronized (this.mLock) {
            if (this.mClients == null) {
                return;
            }
            android.os.RemoteCallbackList<android.view.autofill.IAutoFillManagerClient> clients = this.mClients;
            int userClientCount = clients.beginBroadcast();
            for (int i = 0; i < userClientCount; i++) {
                try {
                    android.view.autofill.IAutoFillManagerClient client = (android.view.autofill.IAutoFillManagerClient) clients.getBroadcastItem(i);
                    try {
                    } catch (android.os.RemoteException e) {
                    }
                    synchronized (this.mLock) {
                        if (resetClient) {
                            resetSession = true;
                            isEnabled = isEnabledLocked();
                        } else {
                            try {
                                if (isClientSessionDestroyedLocked(client)) {
                                    resetSession = true;
                                    isEnabled = isEnabledLocked();
                                } else {
                                    resetSession = false;
                                    isEnabled = isEnabledLocked();
                                }
                            } catch (java.lang.Throwable th) {
                                throw th;
                            }
                        }
                    }
                    int flags = 0;
                    if (isEnabled) {
                        flags = 0 | 1;
                    }
                    if (resetSession) {
                        flags |= 2;
                    }
                    if (resetClient) {
                        flags |= 4;
                    }
                    if (com.android.server.autofill.Helper.sDebug) {
                        flags |= 8;
                    }
                    if (com.android.server.autofill.Helper.sVerbose) {
                        flags |= 16;
                    }
                    client.setState(flags);
                } finally {
                    clients.finishBroadcast();
                }
            }
        }
    }

    private boolean isClientSessionDestroyedLocked(android.view.autofill.IAutoFillManagerClient client) {
        int sessionCount = this.mSessions.size();
        for (int i = 0; i < sessionCount; i++) {
            com.android.server.autofill.Session session = this.mSessions.valueAt(i);
            if (session.getClient().equals(client)) {
                return session.isDestroyed();
            }
        }
        return true;
    }

    void disableAutofillForApp(java.lang.String packageName, long duration, int sessionId, boolean compatMode) {
        synchronized (this.mLock) {
            long expiration = android.os.SystemClock.elapsedRealtime() + duration;
            if (expiration < 0) {
                expiration = Long.MAX_VALUE;
            }
            this.mDisabledInfoCache.addDisabledAppLocked(this.mUserId, packageName, expiration);
            int intDuration = duration > 2147483647L ? Integer.MAX_VALUE : (int) duration;
            this.mMetricsLogger.write(com.android.server.autofill.Helper.newLogMaker(1231, packageName, getServicePackageName(), sessionId, compatMode).addTaggedData(1145, java.lang.Integer.valueOf(intDuration)));
        }
    }

    void disableAutofillForActivity(android.content.ComponentName componentName, long duration, int sessionId, boolean compatMode) {
        int intDuration;
        synchronized (this.mLock) {
            long expiration = android.os.SystemClock.elapsedRealtime() + duration;
            if (expiration < 0) {
                expiration = Long.MAX_VALUE;
            }
            this.mDisabledInfoCache.addDisabledActivityLocked(this.mUserId, componentName, expiration);
            if (duration > 2147483647L) {
                intDuration = Integer.MAX_VALUE;
            } else {
                intDuration = (int) duration;
            }
            android.metrics.LogMaker log = com.android.server.autofill.Helper.newLogMaker(1232, componentName, getServicePackageName(), sessionId, compatMode).addTaggedData(1145, java.lang.Integer.valueOf(intDuration));
            this.mMetricsLogger.write(log);
        }
    }

    private boolean isAutofillDisabledLocked(android.content.ComponentName componentName) {
        return this.mDisabledInfoCache.isAutofillDisabledLocked(this.mUserId, componentName);
    }

    boolean isFieldClassificationEnabled(int callingUid) {
        synchronized (this.mLock) {
            if (!isCalledByServiceLocked("isFieldClassificationEnabled", callingUid)) {
                return false;
            }
            return isFieldClassificationEnabledLocked();
        }
    }

    boolean isFieldClassificationEnabledLocked() {
        return android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "autofill_field_classification", 1, this.mUserId) == 1;
    }

    com.android.server.autofill.FieldClassificationStrategy getFieldClassificationStrategy() {
        return this.mFieldClassificationStrategy;
    }

    java.lang.String[] getAvailableFieldClassificationAlgorithms(int callingUid) {
        synchronized (this.mLock) {
            if (isCalledByServiceLocked("getFCAlgorithms()", callingUid)) {
                return this.mFieldClassificationStrategy.getAvailableAlgorithms();
            }
            return null;
        }
    }

    java.lang.String getDefaultFieldClassificationAlgorithm(int callingUid) {
        synchronized (this.mLock) {
            if (isCalledByServiceLocked("getDefaultFCAlgorithm()", callingUid)) {
                return this.mFieldClassificationStrategy.getDefaultAlgorithm();
            }
            return null;
        }
    }

    com.android.server.autofill.RemoteInlineSuggestionRenderService getRemoteInlineSuggestionRenderServiceLocked() {
        if (this.mRemoteInlineSuggestionRenderService == null) {
            android.content.ComponentName componentName = com.android.server.autofill.RemoteInlineSuggestionRenderService.getServiceComponentName(getContext(), this.mUserId);
            if (componentName == null) {
                android.util.Slog.w(TAG, "No valid component found for InlineSuggestionRenderService");
                return null;
            }
            this.mRemoteInlineSuggestionRenderService = new com.android.server.autofill.RemoteInlineSuggestionRenderService(getContext(), componentName, "android.service.autofill.InlineSuggestionRenderService", this.mUserId, new com.android.server.autofill.AutofillManagerServiceImpl.InlineSuggestionRenderCallbacksImpl(), ((com.android.server.autofill.AutofillManagerService) this.mMaster).isBindInstantServiceAllowed(), ((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose);
        }
        return this.mRemoteInlineSuggestionRenderService;
    }

    private class InlineSuggestionRenderCallbacksImpl implements com.android.server.autofill.RemoteInlineSuggestionRenderService.InlineSuggestionRenderCallbacks {
        private InlineSuggestionRenderCallbacksImpl() {
        }

        public void onServiceDied(com.android.server.autofill.RemoteInlineSuggestionRenderService service) {
            android.util.Slog.w(com.android.server.autofill.AutofillManagerServiceImpl.TAG, "remote service died: " + service);
            synchronized (com.android.server.autofill.AutofillManagerServiceImpl.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl.this.resetExtServiceLocked();
            }
        }
    }

    void onSwitchInputMethod() {
        synchronized (this.mLock) {
            int sessionCount = this.mSessions.size();
            for (int i = 0; i < sessionCount; i++) {
                com.android.server.autofill.Session session = this.mSessions.valueAt(i);
                session.onSwitchInputMethodLocked();
            }
        }
    }

    com.android.server.autofill.RemoteFieldClassificationService getRemoteFieldClassificationServiceLocked() {
        if (this.mRemoteFieldClassificationService == null) {
            java.lang.String serviceName = ((com.android.server.autofill.AutofillManagerService) this.mMaster).mFieldClassificationResolver.getServiceName(this.mUserId);
            if (serviceName == null) {
                if (((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "getRemoteFieldClassificationServiceLocked(): not set");
                }
                return null;
            }
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "getRemoteFieldClassificationServiceLocked serviceName: " + serviceName);
            }
            boolean sTemporaryFieldDetectionService = ((com.android.server.autofill.AutofillManagerService) this.mMaster).mFieldClassificationResolver.isTemporary(this.mUserId);
            android.util.Pair<android.content.pm.ServiceInfo, android.content.ComponentName> pair = com.android.server.autofill.RemoteFieldClassificationService.getComponentName(serviceName, this.mUserId, sTemporaryFieldDetectionService);
            if (pair == null) {
                android.util.Slog.w(TAG, "RemoteFieldClassificationService.getComponentName returned null with serviceName: " + serviceName);
                return null;
            }
            this.mRemoteFieldClassificationServiceInfo = (android.content.pm.ServiceInfo) pair.first;
            android.content.ComponentName componentName = (android.content.ComponentName) pair.second;
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "getRemoteFieldClassificationServiceLocked(): " + componentName);
            }
            int serviceUid = this.mRemoteFieldClassificationServiceInfo.applicationInfo.uid;
            this.mRemoteFieldClassificationService = new com.android.server.autofill.RemoteFieldClassificationService(getContext(), componentName, serviceUid, this.mUserId);
        }
        return this.mRemoteFieldClassificationService;
    }

    com.android.server.autofill.RemoteFieldClassificationService getRemoteFieldClassificationServiceIfCreatedLocked() {
        return this.mRemoteFieldClassificationService;
    }

    public boolean isPccClassificationEnabled() {
        boolean result = isPccClassificationEnabledInternal();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "pccEnabled: " + result);
        }
        return result;
    }

    public boolean isPccClassificationEnabledInternal() {
        boolean z;
        boolean flagEnabled = ((com.android.server.autofill.AutofillManagerService) this.mMaster).isPccClassificationFlagEnabled();
        if (!flagEnabled) {
            return false;
        }
        synchronized (this.mLock) {
            z = getRemoteFieldClassificationServiceLocked() != null;
        }
        return z;
    }

    public boolean isAutofillCredmanIntegrationEnabled() {
        return ((com.android.server.autofill.AutofillManagerService) this.mMaster).isAutofillCredmanIntegrationEnabled();
    }

    void updateRemoteFieldClassificationService() {
        synchronized (this.mLock) {
            if (this.mRemoteFieldClassificationService != null) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "updateRemoteFieldClassificationService(): destroying old remote service");
                }
                this.mRemoteFieldClassificationService.unbind();
                this.mRemoteFieldClassificationService = null;
                this.mRemoteFieldClassificationServiceInfo = null;
            }
            boolean available = isFieldClassificationServiceAvailableLocked();
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "updateRemoteFieldClassificationService(): " + available);
            }
            if (available) {
                this.mRemoteFieldClassificationService = getRemoteFieldClassificationServiceLocked();
            }
        }
    }

    private boolean isFieldClassificationServiceAvailableLocked() {
        if (((com.android.server.autofill.AutofillManagerService) this.mMaster).verbose) {
            android.util.Slog.v(TAG, "isFieldClassificationService(): setupCompleted=" + isSetupCompletedLocked() + ", disabled=" + isDisabledByUserRestrictionsLocked() + ", augmentedService=" + ((com.android.server.autofill.AutofillManagerService) this.mMaster).mFieldClassificationResolver.getServiceName(this.mUserId));
        }
        if (!isSetupCompletedLocked() || isDisabledByUserRestrictionsLocked() || ((com.android.server.autofill.AutofillManagerService) this.mMaster).mFieldClassificationResolver.getServiceName(this.mUserId) == null) {
            return false;
        }
        return true;
    }

    boolean isRemoteClassificationServiceForUserLocked(int callingUid) {
        return this.mRemoteFieldClassificationServiceInfo != null && this.mRemoteFieldClassificationServiceInfo.applicationInfo.uid == callingUid;
    }

    public java.lang.String toString() {
        return "AutofillManagerServiceImpl: [userId=" + this.mUserId + ", component=" + (this.mInfo != null ? this.mInfo.getServiceInfo().getComponentName() : null) + "]";
    }

    private class PruneTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Void> {
        private PruneTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public java.lang.Void doInBackground(java.lang.Void... ignored) {
            int numSessionsToRemove;
            android.util.SparseArray<android.os.IBinder> sessionsToRemove;
            synchronized (com.android.server.autofill.AutofillManagerServiceImpl.this.mLock) {
                numSessionsToRemove = com.android.server.autofill.AutofillManagerServiceImpl.this.mSessions.size();
                sessionsToRemove = new android.util.SparseArray<>(numSessionsToRemove);
                for (int i = 0; i < numSessionsToRemove; i++) {
                    com.android.server.autofill.Session session = (com.android.server.autofill.Session) com.android.server.autofill.AutofillManagerServiceImpl.this.mSessions.valueAt(i);
                    sessionsToRemove.put(session.id, session.getActivityTokenLocked());
                }
            }
            com.android.server.wm.ActivityTaskManagerInternal atmInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
            int i2 = 0;
            while (i2 < numSessionsToRemove) {
                if (atmInternal.getActivityName(sessionsToRemove.valueAt(i2)) != null) {
                    sessionsToRemove.removeAt(i2);
                    i2--;
                    numSessionsToRemove--;
                }
                i2++;
            }
            synchronized (com.android.server.autofill.AutofillManagerServiceImpl.this.mLock) {
                for (int i3 = 0; i3 < numSessionsToRemove; i3++) {
                    com.android.server.autofill.Session sessionToRemove = (com.android.server.autofill.Session) com.android.server.autofill.AutofillManagerServiceImpl.this.mSessions.get(sessionsToRemove.keyAt(i3));
                    if (sessionToRemove != null && sessionsToRemove.valueAt(i3) == sessionToRemove.getActivityTokenLocked()) {
                        if (sessionToRemove.isSaveUiShowingLocked()) {
                            if (com.android.server.autofill.Helper.sVerbose) {
                                android.util.Slog.v(com.android.server.autofill.AutofillManagerServiceImpl.TAG, "Session " + sessionToRemove.id + " is saving");
                            }
                        } else {
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.i(com.android.server.autofill.AutofillManagerServiceImpl.TAG, "Prune session " + sessionToRemove.id + " (" + sessionToRemove.getActivityTokenLocked() + ")");
                            }
                            sessionToRemove.removeFromServiceLocked();
                        }
                    }
                }
            }
            return null;
        }
    }
}
