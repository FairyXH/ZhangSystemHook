package com.android.server.translation;

/* JADX INFO: loaded from: classes3.dex */
final class TranslationManagerServiceImpl extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.translation.TranslationManagerServiceImpl, com.android.server.translation.TranslationManagerService> implements android.os.IBinder.DeathRecipient {
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.translation.TranslationManagerServiceImpl.ActiveTranslation> mActiveTranslations;
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
    private final android.os.RemoteCallbackList<android.os.IRemoteCallback> mCallbacks;
    private java.lang.ref.WeakReference<com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens> mLastActivityTokens;
    private final com.android.server.translation.TranslationManagerServiceImpl.TranslationServiceRemoteCallback mRemoteServiceCallback;
    private com.android.server.translation.RemoteTranslationService mRemoteTranslationService;
    private android.content.pm.ServiceInfo mRemoteTranslationServiceInfo;
    private final android.os.RemoteCallbackList<android.os.IRemoteCallback> mTranslationCapabilityCallbacks;
    private android.service.translation.TranslationServiceInfo mTranslationServiceInfo;
    private final android.util.ArraySet<android.os.IBinder> mWaitingFinishedCallbackActivities;
    private static final java.lang.String TAG = "TranslationManagerServiceImpl";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    protected TranslationManagerServiceImpl(com.android.server.translation.TranslationManagerService master, java.lang.Object lock, int userId, boolean disabled) {
        super(master, lock, userId);
        this.mRemoteServiceCallback = new com.android.server.translation.TranslationManagerServiceImpl.TranslationServiceRemoteCallback();
        this.mTranslationCapabilityCallbacks = new android.os.RemoteCallbackList<>();
        this.mWaitingFinishedCallbackActivities = new android.util.ArraySet<>();
        this.mActiveTranslations = new android.util.ArrayMap<>();
        this.mCallbacks = new android.os.RemoteCallbackList<>();
        updateRemoteServiceLocked();
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        this.mTranslationServiceInfo = new android.service.translation.TranslationServiceInfo(getContext(), serviceComponent, isTemporaryServiceSetLocked(), this.mUserId);
        this.mRemoteTranslationServiceInfo = this.mTranslationServiceInfo.getServiceInfo();
        return this.mTranslationServiceInfo.getServiceInfo();
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected boolean updateLocked(boolean disabled) {
        boolean enabledChanged = super.updateLocked(disabled);
        updateRemoteServiceLocked();
        return enabledChanged;
    }

    private void updateRemoteServiceLocked() {
        if (this.mRemoteTranslationService != null) {
            if (((com.android.server.translation.TranslationManagerService) this.mMaster).debug) {
                android.util.Slog.d(TAG, "updateRemoteService(): destroying old remote service");
            }
            this.mRemoteTranslationService.unbind();
            this.mRemoteTranslationService = null;
        }
    }

    private com.android.server.translation.RemoteTranslationService ensureRemoteServiceLocked() {
        if (this.mRemoteTranslationService == null) {
            java.lang.String serviceName = getComponentNameLocked();
            if (serviceName == null) {
                if (((com.android.server.translation.TranslationManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "ensureRemoteServiceLocked(): no service component name.");
                }
                return null;
            }
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                boolean isServiceAvailableForUser = isServiceAvailableForUser(serviceComponent);
                if (((com.android.server.translation.TranslationManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "ensureRemoteServiceLocked(): isServiceAvailableForUser=" + isServiceAvailableForUser);
                }
                if (!isServiceAvailableForUser) {
                    android.util.Slog.w(TAG, "ensureRemoteServiceLocked(): " + serviceComponent + " is not available,");
                    return null;
                }
                this.mRemoteTranslationService = new com.android.server.translation.RemoteTranslationService(getContext(), serviceComponent, this.mUserId, false, this.mRemoteServiceCallback);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        return this.mRemoteTranslationService;
    }

    private boolean isServiceAvailableForUser(android.content.ComponentName serviceComponent) {
        android.content.Intent intent = new android.content.Intent("android.service.translation.TranslationService").setComponent(serviceComponent);
        android.content.pm.ResolveInfo resolveInfo = getContext().getPackageManager().resolveServiceAsUser(intent, 132, this.mUserId);
        return (resolveInfo == null || resolveInfo.serviceInfo == null) ? false : true;
    }

    void onTranslationCapabilitiesRequestLocked(int sourceFormat, int destFormat, android.os.ResultReceiver resultReceiver) {
        com.android.server.translation.RemoteTranslationService remoteService = ensureRemoteServiceLocked();
        if (remoteService != null) {
            remoteService.onTranslationCapabilitiesRequest(sourceFormat, destFormat, resultReceiver);
        } else {
            android.util.Slog.v(TAG, "onTranslationCapabilitiesRequestLocked(): no remote service.");
            resultReceiver.send(2, null);
        }
    }

    public void registerTranslationCapabilityCallback(android.os.IRemoteCallback callback, int sourceUid) {
        this.mTranslationCapabilityCallbacks.register(callback, java.lang.Integer.valueOf(sourceUid));
        ensureRemoteServiceLocked();
    }

    public void unregisterTranslationCapabilityCallback(android.os.IRemoteCallback callback) {
        this.mTranslationCapabilityCallbacks.unregister(callback);
    }

    void onSessionCreatedLocked(android.view.translation.TranslationContext translationContext, int sessionId, com.android.internal.os.IResultReceiver resultReceiver) throws android.os.RemoteException {
        com.android.server.translation.RemoteTranslationService remoteService = ensureRemoteServiceLocked();
        if (remoteService != null) {
            remoteService.onSessionCreated(translationContext, sessionId, resultReceiver);
        } else {
            android.util.Slog.v(TAG, "onSessionCreatedLocked(): no remote service.");
            resultReceiver.send(2, (android.os.Bundle) null);
        }
    }

    private int getAppUidByComponentName(android.content.Context context, android.content.ComponentName componentName, int userId) {
        if (componentName == null) {
            return -1;
        }
        try {
            int translatedAppUid = context.getPackageManager().getApplicationInfoAsUser(componentName.getPackageName(), 0, userId).uid;
            return translatedAppUid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.d(TAG, "Cannot find packageManager for" + componentName);
            return -1;
        }
    }

    public void onTranslationFinishedLocked(boolean activityDestroyed, android.os.IBinder token, android.content.ComponentName componentName) {
        int translatedAppUid = getAppUidByComponentName(getContext(), componentName, getUserId());
        java.lang.String packageName = componentName.getPackageName();
        if (activityDestroyed || this.mWaitingFinishedCallbackActivities.contains(token)) {
            invokeCallbacks(3, null, null, packageName, translatedAppUid);
            this.mWaitingFinishedCallbackActivities.remove(token);
            this.mActiveTranslations.remove(token);
        }
    }

    public void updateUiTranslationStateLocked(int state, android.view.translation.TranslationSpec sourceSpec, android.view.translation.TranslationSpec targetSpec, java.util.List<android.view.autofill.AutofillId> viewIds, android.os.IBinder token, int taskId, android.view.translation.UiTranslationSpec uiTranslationSpec) {
        com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens candidateActivityTokens = this.mActivityTaskManagerInternal.getAttachedNonFinishingActivityForTask(taskId, token);
        if (candidateActivityTokens != null) {
            this.mLastActivityTokens = new java.lang.ref.WeakReference<>(candidateActivityTokens);
            if (state == 3) {
                this.mWaitingFinishedCallbackActivities.add(token);
            }
            android.os.IBinder activityToken = candidateActivityTokens.getActivityToken();
            try {
                candidateActivityTokens.getApplicationThread().updateUiTranslationState(activityToken, state, sourceSpec, targetSpec, viewIds, uiTranslationSpec);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Update UiTranslationState fail: " + e);
            }
            android.content.ComponentName componentName = this.mActivityTaskManagerInternal.getActivityName(activityToken);
            int translatedAppUid = getAppUidByComponentName(getContext(), componentName, getUserId());
            java.lang.String packageName = componentName.getPackageName();
            invokeCallbacksIfNecessaryLocked(state, sourceSpec, targetSpec, packageName, token, translatedAppUid);
            updateActiveTranslationsLocked(state, sourceSpec, targetSpec, packageName, token, translatedAppUid);
            return;
        }
        android.util.Slog.w(TAG, "Unknown activity or it was finished to query for update translation state for token=" + token + " taskId=" + taskId + " for state= " + state);
    }

    private void updateActiveTranslationsLocked(int state, android.view.translation.TranslationSpec sourceSpec, android.view.translation.TranslationSpec targetSpec, java.lang.String packageName, android.os.IBinder shareableActivityToken, int translatedAppUid) {
        com.android.server.translation.TranslationManagerServiceImpl.ActiveTranslation activeTranslation = this.mActiveTranslations.get(shareableActivityToken);
        switch (state) {
            case 0:
                if (activeTranslation == null) {
                    try {
                        shareableActivityToken.linkToDeath(this, 0);
                        this.mActiveTranslations.put(shareableActivityToken, new com.android.server.translation.TranslationManagerServiceImpl.ActiveTranslation(sourceSpec, targetSpec, translatedAppUid, packageName));
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG, "Failed to call linkToDeath for translated app with uid=" + translatedAppUid + "; activity is already dead", e);
                        invokeCallbacks(3, sourceSpec, targetSpec, packageName, translatedAppUid);
                        return;
                    }
                }
                break;
            case 1:
                if (activeTranslation != null) {
                    activeTranslation.isPaused = true;
                }
                break;
            case 2:
                if (activeTranslation != null) {
                    activeTranslation.isPaused = false;
                }
                break;
            case 3:
                if (activeTranslation != null) {
                    this.mActiveTranslations.remove(shareableActivityToken);
                }
                break;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Updating to translation state=" + state + " for app with uid=" + translatedAppUid + " packageName=" + packageName);
        }
    }

    private void invokeCallbacksIfNecessaryLocked(int state, android.view.translation.TranslationSpec sourceSpec, android.view.translation.TranslationSpec targetSpec, java.lang.String packageName, android.os.IBinder shareableActivityToken, int translatedAppUid) {
        boolean shouldInvokeCallbacks = true;
        int stateForCallbackInvocation = state;
        com.android.server.translation.TranslationManagerServiceImpl.ActiveTranslation activeTranslation = this.mActiveTranslations.get(shareableActivityToken);
        if (activeTranslation == null) {
            if (state != 0) {
                shouldInvokeCallbacks = false;
                android.util.Slog.w(TAG, "Updating to translation state=" + state + " for app with uid=" + translatedAppUid + " packageName=" + packageName + " but no active translation was found for it");
            }
        } else {
            switch (state) {
                case 0:
                    boolean specsAreIdentical = activeTranslation.sourceSpec.getLocale().equals(sourceSpec.getLocale()) && activeTranslation.targetSpec.getLocale().equals(targetSpec.getLocale());
                    if (specsAreIdentical) {
                        if (activeTranslation.isPaused) {
                            stateForCallbackInvocation = 2;
                        } else {
                            shouldInvokeCallbacks = false;
                        }
                    }
                    break;
                case 1:
                    if (activeTranslation.isPaused) {
                        shouldInvokeCallbacks = false;
                    }
                    break;
                case 2:
                    if (!activeTranslation.isPaused) {
                        shouldInvokeCallbacks = false;
                    }
                    break;
                case 3:
                    shouldInvokeCallbacks = false;
                    break;
            }
        }
        if (shouldInvokeCallbacks) {
            invokeCallbacks(stateForCallbackInvocation, sourceSpec, targetSpec, packageName, translatedAppUid);
        }
    }

    public void dumpLocked(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter pw) {
        if (this.mLastActivityTokens != null) {
            com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens activityTokens = this.mLastActivityTokens.get();
            if (activityTokens == null) {
                return;
            }
            try {
                com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
                try {
                    activityTokens.getApplicationThread().dumpActivity(tp.getWriteFd(), activityTokens.getActivityToken(), prefix, new java.lang.String[]{"--dump-dumpable", "UiTranslationController"});
                    tp.go(fd);
                    tp.close();
                } finally {
                }
            } catch (android.os.RemoteException e) {
                pw.println(prefix + "Got a RemoteException while dumping the activity");
            } catch (java.io.IOException e2) {
                pw.println(prefix + "Failure while dumping the activity: " + e2);
            }
        } else {
            pw.print(prefix);
            pw.println("No requested UiTranslation Activity.");
        }
        int waitingFinishCallbackSize = this.mWaitingFinishedCallbackActivities.size();
        if (waitingFinishCallbackSize > 0) {
            pw.print(prefix);
            pw.print("number waiting finish callback activities: ");
            pw.println(waitingFinishCallbackSize);
            for (android.os.IBinder activityToken : this.mWaitingFinishedCallbackActivities) {
                pw.print(prefix);
                pw.print("shareableActivityToken: ");
                pw.println(activityToken);
            }
        }
    }

    private void invokeCallbacks(int state, android.view.translation.TranslationSpec sourceSpec, android.view.translation.TranslationSpec targetSpec, java.lang.String packageName, final int translatedAppUid) {
        final android.os.Bundle result = createResultForCallback(state, sourceSpec, targetSpec, packageName);
        int registeredCallbackCount = this.mCallbacks.getRegisteredCallbackCount();
        if (DEBUG) {
            android.util.Slog.d(TAG, "Invoking " + registeredCallbackCount + " callbacks for translation state=" + state + " for app with uid=" + translatedAppUid + " packageName=" + packageName);
        }
        if (registeredCallbackCount == 0) {
            return;
        }
        final java.util.List<android.view.inputmethod.InputMethodInfo> enabledInputMethods = getEnabledInputMethods();
        this.mCallbacks.broadcast(new java.util.function.BiConsumer() { // from class: com.android.server.translation.TranslationManagerServiceImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$invokeCallbacks$0(translatedAppUid, result, enabledInputMethods, (android.os.IRemoteCallback) obj, obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$invokeCallbacks$0(int translatedAppUid, android.os.Bundle result, java.util.List enabledInputMethods, android.os.IRemoteCallback callback, java.lang.Object uid) {
        invokeCallback(((java.lang.Integer) uid).intValue(), translatedAppUid, callback, result, enabledInputMethods);
    }

    private java.util.List<android.view.inputmethod.InputMethodInfo> getEnabledInputMethods() {
        return ((com.android.server.inputmethod.InputMethodManagerInternal) com.android.server.LocalServices.getService(com.android.server.inputmethod.InputMethodManagerInternal.class)).getEnabledInputMethodListAsUser(this.mUserId);
    }

    private android.os.Bundle createResultForCallback(int state, android.view.translation.TranslationSpec sourceSpec, android.view.translation.TranslationSpec targetSpec, java.lang.String packageName) {
        android.os.Bundle result = new android.os.Bundle();
        result.putInt("state", state);
        if (sourceSpec != null) {
            result.putSerializable("source_locale", sourceSpec.getLocale());
            result.putSerializable("target_locale", targetSpec.getLocale());
        }
        result.putString("package_name", packageName);
        return result;
    }

    private void invokeCallback(int callbackSourceUid, int translatedAppUid, android.os.IRemoteCallback callback, android.os.Bundle result, java.util.List<android.view.inputmethod.InputMethodInfo> enabledInputMethods) {
        if (callbackSourceUid == translatedAppUid) {
            try {
                callback.sendResult(result);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to invoke UiTranslationStateCallback: " + e);
                return;
            }
        }
        boolean isIme = false;
        java.util.Iterator<android.view.inputmethod.InputMethodInfo> it = enabledInputMethods.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.view.inputmethod.InputMethodInfo inputMethod = it.next();
            if (callbackSourceUid == inputMethod.getServiceInfo().applicationInfo.uid) {
                isIme = true;
                break;
            }
        }
        if (!isIme) {
            return;
        }
        try {
            callback.sendResult(result);
        } catch (android.os.RemoteException e2) {
            android.util.Slog.w(TAG, "Failed to invoke UiTranslationStateCallback: " + e2);
        }
    }

    public void registerUiTranslationStateCallbackLocked(android.os.IRemoteCallback callback, int sourceUid) {
        this.mCallbacks.register(callback, java.lang.Integer.valueOf(sourceUid));
        int numActiveTranslations = this.mActiveTranslations.size();
        android.util.Slog.i(TAG, "New registered callback for sourceUid=" + sourceUid + " with currently " + numActiveTranslations + " active translations");
        if (numActiveTranslations == 0) {
            return;
        }
        java.util.List<android.view.inputmethod.InputMethodInfo> enabledInputMethods = getEnabledInputMethods();
        for (int i = 0; i < this.mActiveTranslations.size(); i++) {
            com.android.server.translation.TranslationManagerServiceImpl.ActiveTranslation activeTranslation = this.mActiveTranslations.valueAt(i);
            int translatedAppUid = activeTranslation.translatedAppUid;
            java.lang.String packageName = activeTranslation.packageName;
            if (DEBUG) {
                android.util.Slog.d(TAG, "Triggering callback for sourceUid=" + sourceUid + " for translated app with uid=" + translatedAppUid + "packageName=" + packageName + " isPaused=" + activeTranslation.isPaused);
            }
            android.os.Bundle startedResult = createResultForCallback(0, activeTranslation.sourceSpec, activeTranslation.targetSpec, packageName);
            invokeCallback(sourceUid, translatedAppUid, callback, startedResult, enabledInputMethods);
            if (activeTranslation.isPaused) {
                android.os.Bundle pausedResult = createResultForCallback(1, activeTranslation.sourceSpec, activeTranslation.targetSpec, packageName);
                invokeCallback(sourceUid, translatedAppUid, callback, pausedResult, enabledInputMethods);
            }
        }
    }

    public void unregisterUiTranslationStateCallback(android.os.IRemoteCallback callback) {
        this.mCallbacks.unregister(callback);
    }

    public android.content.ComponentName getServiceSettingsActivityLocked() {
        java.lang.String activityName;
        if (this.mTranslationServiceInfo == null || (activityName = this.mTranslationServiceInfo.getSettingsActivity()) == null) {
            return null;
        }
        java.lang.String packageName = this.mTranslationServiceInfo.getServiceInfo().packageName;
        return new android.content.ComponentName(packageName, activityName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyClientsTranslationCapability(android.view.translation.TranslationCapability capability) {
        final android.os.Bundle res = new android.os.Bundle();
        res.putParcelable("translation_capabilities", capability);
        this.mTranslationCapabilityCallbacks.broadcast(new java.util.function.BiConsumer() { // from class: com.android.server.translation.TranslationManagerServiceImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.translation.TranslationManagerServiceImpl.lambda$notifyClientsTranslationCapability$1(res, (android.os.IRemoteCallback) obj, obj2);
            }
        });
    }

    static /* synthetic */ void lambda$notifyClientsTranslationCapability$1(android.os.Bundle res, android.os.IRemoteCallback callback, java.lang.Object uid) {
        try {
            callback.sendResult(res);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to invoke UiTranslationStateCallback: " + e);
        }
    }

    private final class TranslationServiceRemoteCallback extends android.view.translation.ITranslationServiceCallback.Stub {
        private TranslationServiceRemoteCallback() {
        }

        public void updateTranslationCapability(android.view.translation.TranslationCapability capability) {
            if (capability == null) {
                android.util.Slog.wtf(com.android.server.translation.TranslationManagerServiceImpl.TAG, "received a null TranslationCapability from TranslationService.");
            } else {
                com.android.server.translation.TranslationManagerServiceImpl.this.notifyClientsTranslationCapability(capability);
            }
        }
    }

    private static final class ActiveTranslation {
        public boolean isPaused;
        public final java.lang.String packageName;
        public final android.view.translation.TranslationSpec sourceSpec;
        public final android.view.translation.TranslationSpec targetSpec;
        public final int translatedAppUid;

        private ActiveTranslation(android.view.translation.TranslationSpec sourceSpec, android.view.translation.TranslationSpec targetSpec, int translatedAppUid, java.lang.String packageName) {
            this.isPaused = false;
            this.sourceSpec = sourceSpec;
            this.targetSpec = targetSpec;
            this.translatedAppUid = translatedAppUid;
            this.packageName = packageName;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(android.os.IBinder who) {
        synchronized (this.mLock) {
            this.mWaitingFinishedCallbackActivities.remove(who);
            com.android.server.translation.TranslationManagerServiceImpl.ActiveTranslation activeTranslation = this.mActiveTranslations.remove(who);
            if (activeTranslation != null) {
                invokeCallbacks(3, activeTranslation.sourceSpec, activeTranslation.targetSpec, activeTranslation.packageName, activeTranslation.translatedAppUid);
            }
        }
    }
}
