package com.android.server.textclassifier;

/* JADX INFO: loaded from: classes3.dex */
public final class TextClassificationManagerService extends android.service.textclassifier.ITextClassifierService.Stub {
    private static final boolean DEBUG = false;
    private static final java.lang.String LOG_TAG = "TextClassificationManagerService";
    private static final android.service.textclassifier.ITextClassifierCallback NO_OP_CALLBACK = new android.service.textclassifier.ITextClassifierCallback() { // from class: com.android.server.textclassifier.TextClassificationManagerService.1
        public void onSuccess(android.os.Bundle result) {
        }

        public void onFailure() {
        }

        public android.os.IBinder asBinder() {
            return null;
        }
    };
    private final android.content.Context mContext;
    private final java.lang.String mDefaultTextClassifierPackage;
    private final java.lang.Object mLock;
    private final com.android.server.textclassifier.TextClassificationManagerService.SessionCache mSessionCache;
    private final android.view.textclassifier.TextClassificationConstants mSettings;
    private final com.android.server.textclassifier.TextClassificationManagerService.TextClassifierSettingsListener mSettingsListener;
    private final java.lang.String mSystemTextClassifierPackage;
    final android.util.SparseArray<com.android.server.textclassifier.TextClassificationManagerService.UserState> mUserStates;

    public static final class Lifecycle extends com.android.server.SystemService {
        private final com.android.server.textclassifier.TextClassificationManagerService mManagerService;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mManagerService = new com.android.server.textclassifier.TextClassificationManagerService(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            try {
                publishBinderService("textclassification", this.mManagerService);
                this.mManagerService.startListenSettings();
                this.mManagerService.startTrackingPackageChanges();
            } catch (java.lang.Throwable t) {
                android.util.Slog.e(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "Could not start the TextClassificationManagerService.", t);
            }
        }

        @Override // com.android.server.SystemService
        public void onUserStarting(com.android.server.SystemService.TargetUser user) {
            updatePackageStateForUser(user.getUserIdentifier());
            processAnyPendingWork(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            updatePackageStateForUser(user.getUserIdentifier());
            processAnyPendingWork(user.getUserIdentifier());
        }

        private void processAnyPendingWork(int userId) {
            synchronized (this.mManagerService.mLock) {
                this.mManagerService.getUserStateLocked(userId).bindIfHasPendingRequestsLocked();
            }
        }

        private void updatePackageStateForUser(int userId) {
            synchronized (this.mManagerService.mLock) {
                this.mManagerService.getUserStateLocked(userId).updatePackageStateLocked();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser user) {
            int userId = user.getUserIdentifier();
            synchronized (this.mManagerService.mLock) {
                com.android.server.textclassifier.TextClassificationManagerService.UserState userState = this.mManagerService.peekUserStateLocked(userId);
                if (userState != null) {
                    userState.cleanupServiceLocked();
                    this.mManagerService.mUserStates.remove(userId);
                }
            }
        }
    }

    private TextClassificationManagerService(android.content.Context context) {
        this.mUserStates = new android.util.SparseArray<>();
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mLock = new java.lang.Object();
        this.mSettings = new android.view.textclassifier.TextClassificationConstants();
        this.mSettingsListener = new com.android.server.textclassifier.TextClassificationManagerService.TextClassifierSettingsListener(this.mContext);
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        this.mDefaultTextClassifierPackage = packageManager.getDefaultTextClassifierPackageName();
        this.mSystemTextClassifierPackage = packageManager.getSystemTextClassifierPackageName();
        this.mSessionCache = new com.android.server.textclassifier.TextClassificationManagerService.SessionCache(this.mLock);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startListenSettings() {
        this.mSettingsListener.registerObserver();
    }

    void startTrackingPackageChanges() {
        com.android.internal.content.PackageMonitor monitor = new com.android.internal.content.PackageMonitor() { // from class: com.android.server.textclassifier.TextClassificationManagerService.2
            public void onPackageAdded(java.lang.String packageName, int uid) {
                notifyPackageInstallStatusChange(packageName, true);
            }

            public void onPackageRemoved(java.lang.String packageName, int uid) {
                notifyPackageInstallStatusChange(packageName, false);
            }

            public void onPackageModified(java.lang.String packageName) {
                int userId = getChangingUserId();
                synchronized (com.android.server.textclassifier.TextClassificationManagerService.this.mLock) {
                    com.android.server.textclassifier.TextClassificationManagerService.UserState userState = com.android.server.textclassifier.TextClassificationManagerService.this.getUserStateLocked(userId);
                    com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState = userState.getServiceStateLocked(packageName);
                    if (serviceState != null) {
                        serviceState.onPackageModifiedLocked();
                    }
                }
            }

            private void notifyPackageInstallStatusChange(java.lang.String packageName, boolean installed) {
                int userId = getChangingUserId();
                synchronized (com.android.server.textclassifier.TextClassificationManagerService.this.mLock) {
                    com.android.server.textclassifier.TextClassificationManagerService.UserState userState = com.android.server.textclassifier.TextClassificationManagerService.this.getUserStateLocked(userId);
                    com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState = userState.getServiceStateLocked(packageName);
                    if (serviceState != null) {
                        serviceState.onPackageInstallStatusChangeLocked(installed);
                    }
                }
            }
        };
        monitor.register(this.mContext, (android.os.Looper) null, android.os.UserHandle.ALL, true);
    }

    public void onConnectedStateChanged(int connected) {
    }

    public void onSuggestSelection(final android.view.textclassifier.TextClassificationSessionId sessionId, final android.view.textclassifier.TextSelection.Request request, final android.service.textclassifier.ITextClassifierCallback callback) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(request);
        java.util.Objects.requireNonNull(request.getSystemTextClassifierMetadata());
        handleRequest(request.getSystemTextClassifierMetadata(), true, true, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda0
            public final void acceptOrThrow(java.lang.Object obj) {
                ((android.service.textclassifier.ITextClassifierService) obj).onSuggestSelection(sessionId, request, com.android.server.textclassifier.TextClassificationManagerService.wrap(callback));
            }
        }, "onSuggestSelection", callback);
    }

    public void onClassifyText(final android.view.textclassifier.TextClassificationSessionId sessionId, final android.view.textclassifier.TextClassification.Request request, final android.service.textclassifier.ITextClassifierCallback callback) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(request);
        java.util.Objects.requireNonNull(request.getSystemTextClassifierMetadata());
        handleRequest(request.getSystemTextClassifierMetadata(), true, true, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda7
            public final void acceptOrThrow(java.lang.Object obj) {
                ((android.service.textclassifier.ITextClassifierService) obj).onClassifyText(sessionId, request, com.android.server.textclassifier.TextClassificationManagerService.wrap(callback));
            }
        }, "onClassifyText", callback);
    }

    public void onGenerateLinks(final android.view.textclassifier.TextClassificationSessionId sessionId, final android.view.textclassifier.TextLinks.Request request, final android.service.textclassifier.ITextClassifierCallback callback) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(request);
        java.util.Objects.requireNonNull(request.getSystemTextClassifierMetadata());
        handleRequest(request.getSystemTextClassifierMetadata(), true, true, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda3
            public final void acceptOrThrow(java.lang.Object obj) {
                ((android.service.textclassifier.ITextClassifierService) obj).onGenerateLinks(sessionId, request, callback);
            }
        }, "onGenerateLinks", callback);
    }

    public void onSelectionEvent(final android.view.textclassifier.TextClassificationSessionId sessionId, final android.view.textclassifier.SelectionEvent event) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(event);
        java.util.Objects.requireNonNull(event.getSystemTextClassifierMetadata());
        handleRequest(event.getSystemTextClassifierMetadata(), true, true, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda5
            public final void acceptOrThrow(java.lang.Object obj) {
                ((android.service.textclassifier.ITextClassifierService) obj).onSelectionEvent(sessionId, event);
            }
        }, "onSelectionEvent", NO_OP_CALLBACK);
    }

    public void onTextClassifierEvent(final android.view.textclassifier.TextClassificationSessionId sessionId, final android.view.textclassifier.TextClassifierEvent event) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(event);
        android.view.textclassifier.TextClassificationContext eventContext = event.getEventContext();
        android.view.textclassifier.SystemTextClassifierMetadata systemTcMetadata = eventContext != null ? eventContext.getSystemTextClassifierMetadata() : null;
        handleRequest(systemTcMetadata, true, true, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda2
            public final void acceptOrThrow(java.lang.Object obj) {
                ((android.service.textclassifier.ITextClassifierService) obj).onTextClassifierEvent(sessionId, event);
            }
        }, "onTextClassifierEvent", NO_OP_CALLBACK);
    }

    public void onDetectLanguage(final android.view.textclassifier.TextClassificationSessionId sessionId, final android.view.textclassifier.TextLanguage.Request request, final android.service.textclassifier.ITextClassifierCallback callback) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(request);
        java.util.Objects.requireNonNull(request.getSystemTextClassifierMetadata());
        handleRequest(request.getSystemTextClassifierMetadata(), true, true, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda10
            public final void acceptOrThrow(java.lang.Object obj) {
                ((android.service.textclassifier.ITextClassifierService) obj).onDetectLanguage(sessionId, request, callback);
            }
        }, "onDetectLanguage", callback);
    }

    public void onSuggestConversationActions(final android.view.textclassifier.TextClassificationSessionId sessionId, final android.view.textclassifier.ConversationActions.Request request, final android.service.textclassifier.ITextClassifierCallback callback) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(request);
        java.util.Objects.requireNonNull(request.getSystemTextClassifierMetadata());
        handleRequest(request.getSystemTextClassifierMetadata(), true, true, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda11
            public final void acceptOrThrow(java.lang.Object obj) {
                ((android.service.textclassifier.ITextClassifierService) obj).onSuggestConversationActions(sessionId, request, com.android.server.textclassifier.TextClassificationManagerService.wrap(callback));
            }
        }, "onSuggestConversationActions", callback);
    }

    public void onCreateTextClassificationSession(final android.view.textclassifier.TextClassificationContext classificationContext, final android.view.textclassifier.TextClassificationSessionId sessionId) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(sessionId);
        java.util.Objects.requireNonNull(classificationContext);
        java.util.Objects.requireNonNull(classificationContext.getSystemTextClassifierMetadata());
        synchronized (this.mLock) {
            this.mSessionCache.put(sessionId, classificationContext);
        }
        handleRequest(classificationContext.getSystemTextClassifierMetadata(), true, false, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda4
            public final void acceptOrThrow(java.lang.Object obj) {
                ((android.service.textclassifier.ITextClassifierService) obj).onCreateTextClassificationSession(classificationContext, sessionId);
            }
        }, "onCreateTextClassificationSession", NO_OP_CALLBACK);
    }

    public void onDestroyTextClassificationSession(final android.view.textclassifier.TextClassificationSessionId sessionId) throws android.os.RemoteException {
        int userId;
        java.util.Objects.requireNonNull(sessionId);
        synchronized (this.mLock) {
            com.android.server.textclassifier.TextClassificationManagerService.StrippedTextClassificationContext textClassificationContext = this.mSessionCache.get(sessionId.getToken());
            if (textClassificationContext != null) {
                userId = textClassificationContext.userId;
            } else {
                userId = android.os.UserHandle.getCallingUserId();
            }
            boolean useDefaultTextClassifier = textClassificationContext == null || textClassificationContext.useDefaultTextClassifier;
            android.view.textclassifier.SystemTextClassifierMetadata sysTcMetadata = new android.view.textclassifier.SystemTextClassifierMetadata("", userId, useDefaultTextClassifier);
            handleRequest(sysTcMetadata, false, false, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda6
                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$onDestroyTextClassificationSession$8(sessionId, (android.service.textclassifier.ITextClassifierService) obj);
                }
            }, "onDestroyTextClassificationSession", NO_OP_CALLBACK);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDestroyTextClassificationSession$8(android.view.textclassifier.TextClassificationSessionId sessionId, android.service.textclassifier.ITextClassifierService service) throws java.lang.Exception {
        try {
            service.onDestroyTextClassificationSession(sessionId);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(LOG_TAG, "text classfication service died......");
        }
        this.mSessionCache.remove(sessionId.getToken());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.textclassifier.TextClassificationManagerService.UserState getUserStateLocked(int userId) {
        com.android.server.textclassifier.TextClassificationManagerService.UserState result = this.mUserStates.get(userId);
        if (result == null) {
            com.android.server.textclassifier.TextClassificationManagerService.UserState result2 = new com.android.server.textclassifier.TextClassificationManagerService.UserState(userId);
            this.mUserStates.put(userId, result2);
            return result2;
        }
        return result;
    }

    com.android.server.textclassifier.TextClassificationManagerService.UserState peekUserStateLocked(int userId) {
        return this.mUserStates.get(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int resolvePackageToUid(java.lang.String packageName, int userId) {
        if (packageName == null) {
            return -1;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            return pm.getPackageUidAsUser(packageName, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(LOG_TAG, "Could not get the UID for " + packageName);
            return -1;
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, LOG_TAG, fout)) {
            final com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(fout, "  ");
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda1
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$dump$9(pw);
                }
            });
            pw.printPair("context", this.mContext);
            pw.println();
            pw.printPair("defaultTextClassifierPackage", this.mDefaultTextClassifierPackage);
            pw.println();
            pw.printPair("systemTextClassifierPackage", this.mSystemTextClassifierPackage);
            pw.println();
            synchronized (this.mLock) {
                int size = this.mUserStates.size();
                pw.print("Number user states: ");
                pw.println(size);
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        pw.increaseIndent();
                        com.android.server.textclassifier.TextClassificationManagerService.UserState userState = this.mUserStates.valueAt(i);
                        pw.printPair("User", java.lang.Integer.valueOf(this.mUserStates.keyAt(i)));
                        pw.println();
                        userState.dump(pw);
                        pw.decreaseIndent();
                    }
                }
                pw.println("Number of active sessions: " + this.mSessionCache.size());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$9(com.android.internal.util.IndentingPrintWriter pw) throws java.lang.Exception {
        ((android.view.textclassifier.TextClassificationManager) this.mContext.getSystemService(android.view.textclassifier.TextClassificationManager.class)).dump(pw);
    }

    private void handleRequest(android.view.textclassifier.SystemTextClassifierMetadata sysTcMetadata, boolean verifyCallingPackage, boolean attemptToBind, final com.android.internal.util.FunctionalUtils.ThrowingConsumer<android.service.textclassifier.ITextClassifierService> textClassifierServiceConsumer, java.lang.String methodName, final android.service.textclassifier.ITextClassifierCallback callback) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(textClassifierServiceConsumer);
        java.util.Objects.requireNonNull(methodName);
        java.util.Objects.requireNonNull(callback);
        int userId = sysTcMetadata == null ? android.os.UserHandle.getCallingUserId() : sysTcMetadata.getUserId();
        java.lang.String callingPackageName = sysTcMetadata == null ? null : sysTcMetadata.getCallingPackageName();
        boolean useDefaultTextClassifier = sysTcMetadata == null ? true : sysTcMetadata.useDefaultTextClassifier();
        if (verifyCallingPackage) {
            try {
                validateCallingPackage(callingPackageName);
            } catch (java.lang.Exception e) {
                throw new android.os.RemoteException("Invalid request: " + e.getMessage(), e, true, true);
            }
        }
        validateUser(userId);
        synchronized (this.mLock) {
            com.android.server.textclassifier.TextClassificationManagerService.UserState userState = getUserStateLocked(userId);
            final com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState = userState.getServiceStateLocked(useDefaultTextClassifier);
            if (serviceState == null) {
                android.util.Slog.d(LOG_TAG, "No configured system TextClassifierService");
                callback.onFailure();
            } else if (!serviceState.isInstalledLocked() || !serviceState.isEnabledLocked()) {
                callback.onFailure();
            } else if (attemptToBind && !serviceState.bindLocked()) {
                android.util.Slog.d(LOG_TAG, "Unable to bind TextClassifierService at " + methodName);
                callback.onFailure();
            } else if (serviceState.isBoundLocked()) {
                if (!serviceState.checkRequestAcceptedLocked(android.os.Binder.getCallingUid(), methodName)) {
                    android.util.Slog.w(LOG_TAG, java.lang.String.format("UID %d is not allowed to see the %s request", java.lang.Integer.valueOf(android.os.Binder.getCallingUid()), methodName));
                    callback.onFailure();
                    return;
                }
                consumeServiceNoExceptLocked(textClassifierServiceConsumer, serviceState.mService);
            } else {
                com.android.server.textclassifier.FixedSizeQueue<com.android.server.textclassifier.TextClassificationManagerService.PendingRequest> fixedSizeQueue = serviceState.mPendingRequests;
                com.android.internal.util.FunctionalUtils.ThrowingRunnable throwingRunnable = new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda8
                    public final void runOrThrow() {
                        com.android.server.textclassifier.TextClassificationManagerService.consumeServiceNoExceptLocked(textClassifierServiceConsumer, serviceState.mService);
                    }
                };
                java.util.Objects.requireNonNull(callback);
                fixedSizeQueue.add(new com.android.server.textclassifier.TextClassificationManagerService.PendingRequest(methodName, throwingRunnable, new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda9
                    public final void runOrThrow() {
                        callback.onFailure();
                    }
                }, callback.asBinder(), this, serviceState, android.os.Binder.getCallingUid()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void consumeServiceNoExceptLocked(com.android.internal.util.FunctionalUtils.ThrowingConsumer<android.service.textclassifier.ITextClassifierService> textClassifierServiceConsumer, android.service.textclassifier.ITextClassifierService service) {
        try {
            textClassifierServiceConsumer.accept(service);
        } catch (java.lang.Error | java.lang.RuntimeException e) {
            android.util.Slog.e(LOG_TAG, "Exception when consume textClassifierService: " + e);
        }
    }

    private static android.service.textclassifier.ITextClassifierCallback wrap(android.service.textclassifier.ITextClassifierCallback orig) {
        return new com.android.server.textclassifier.TextClassificationManagerService.CallbackWrapper(orig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTextClassifierServicePackageOverrideChanged(java.lang.String overriddenPackage) {
        synchronized (this.mLock) {
            int size = this.mUserStates.size();
            for (int i = 0; i < size; i++) {
                com.android.server.textclassifier.TextClassificationManagerService.UserState userState = this.mUserStates.valueAt(i);
                userState.onTextClassifierServicePackageOverrideChangedLocked(overriddenPackage);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class PendingRequest implements android.os.IBinder.DeathRecipient {
        private final android.os.IBinder mBinder;
        private final java.lang.String mName;
        private final java.lang.Runnable mOnServiceFailure;
        private final java.lang.Runnable mRequest;
        private final com.android.server.textclassifier.TextClassificationManagerService mService;
        private final com.android.server.textclassifier.TextClassificationManagerService.ServiceState mServiceState;
        private final int mUid;

        PendingRequest(java.lang.String name, com.android.internal.util.FunctionalUtils.ThrowingRunnable request, com.android.internal.util.FunctionalUtils.ThrowingRunnable onServiceFailure, android.os.IBinder binder, com.android.server.textclassifier.TextClassificationManagerService service, com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState, int uid) {
            this.mName = name;
            this.mRequest = com.android.server.textclassifier.TextClassificationManagerService.logOnFailure((com.android.internal.util.FunctionalUtils.ThrowingRunnable) java.util.Objects.requireNonNull(request), "handling pending request");
            this.mOnServiceFailure = com.android.server.textclassifier.TextClassificationManagerService.logOnFailure((com.android.internal.util.FunctionalUtils.ThrowingRunnable) java.util.Objects.requireNonNull(onServiceFailure), "notifying callback of service failure");
            this.mBinder = binder;
            this.mService = service;
            this.mServiceState = (com.android.server.textclassifier.TextClassificationManagerService.ServiceState) java.util.Objects.requireNonNull(serviceState);
            if (this.mBinder != null) {
                try {
                    this.mBinder.linkToDeath(this, 0);
                } catch (android.os.RemoteException e) {
                    e.printStackTrace();
                }
            }
            this.mUid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (this.mService.mLock) {
                removeLocked();
            }
        }

        private void removeLocked() {
            this.mServiceState.mPendingRequests.remove(this);
            if (this.mBinder != null) {
                this.mBinder.unlinkToDeath(this, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Runnable logOnFailure(com.android.internal.util.FunctionalUtils.ThrowingRunnable r, final java.lang.String opDesc) {
        if (r == null) {
            return null;
        }
        return com.android.internal.util.FunctionalUtils.handleExceptions(r, new java.util.function.Consumer() { // from class: com.android.server.textclassifier.TextClassificationManagerService$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                android.util.Slog.d(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "Error " + opDesc + ": " + ((java.lang.Throwable) obj).getMessage());
            }
        });
    }

    private void validateCallingPackage(java.lang.String callingPackage) throws android.content.pm.PackageManager.NameNotFoundException {
        if (callingPackage != null) {
            int packageUid = this.mContext.getPackageManager().getPackageUidAsUser(callingPackage, android.os.UserHandle.getCallingUserId());
            int callingUid = android.os.Binder.getCallingUid();
            com.android.internal.util.Preconditions.checkArgument(callingUid == packageUid || callingUid == 1000, "Invalid package name. callingPackage=" + callingPackage + ", callingUid=" + callingUid);
        }
    }

    private void validateUser(int userId) {
        com.android.internal.util.Preconditions.checkArgument(userId != -10000, "Null userId");
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (callingUserId != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "Invalid userId. UserId=" + userId + ", CallingUserId=" + callingUserId);
        }
    }

    static final class SessionCache {
        private static final int MAX_CACHE_SIZE = 100;
        private final java.lang.Object mLock;
        private final android.os.IBinder.DeathRecipient mDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.textclassifier.TextClassificationManagerService.SessionCache.1
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied(android.os.IBinder who) {
                com.android.server.textclassifier.TextClassificationManagerService.SessionCache.this.remove(who);
            }
        };
        private final android.util.LruCache<android.os.IBinder, com.android.server.textclassifier.TextClassificationManagerService.StrippedTextClassificationContext> mCache = new android.util.LruCache<android.os.IBinder, com.android.server.textclassifier.TextClassificationManagerService.StrippedTextClassificationContext>(100) { // from class: com.android.server.textclassifier.TextClassificationManagerService.SessionCache.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.util.LruCache
            public void entryRemoved(boolean evicted, android.os.IBinder token, com.android.server.textclassifier.TextClassificationManagerService.StrippedTextClassificationContext oldValue, com.android.server.textclassifier.TextClassificationManagerService.StrippedTextClassificationContext newValue) {
                if (evicted) {
                    token.unlinkToDeath(com.android.server.textclassifier.TextClassificationManagerService.SessionCache.this.mDeathRecipient, 0);
                }
            }
        };

        SessionCache(java.lang.Object lock) {
            this.mLock = java.util.Objects.requireNonNull(lock);
        }

        void put(android.view.textclassifier.TextClassificationSessionId sessionId, android.view.textclassifier.TextClassificationContext textClassificationContext) {
            synchronized (this.mLock) {
                this.mCache.put(sessionId.getToken(), new com.android.server.textclassifier.TextClassificationManagerService.StrippedTextClassificationContext(textClassificationContext));
                try {
                    sessionId.getToken().linkToDeath(this.mDeathRecipient, 0);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "SessionCache: Failed to link to death", e);
                }
            }
        }

        com.android.server.textclassifier.TextClassificationManagerService.StrippedTextClassificationContext get(android.os.IBinder token) {
            com.android.server.textclassifier.TextClassificationManagerService.StrippedTextClassificationContext strippedTextClassificationContext;
            java.util.Objects.requireNonNull(token);
            synchronized (this.mLock) {
                strippedTextClassificationContext = this.mCache.get(token);
            }
            return strippedTextClassificationContext;
        }

        void remove(android.os.IBinder token) {
            java.util.Objects.requireNonNull(token);
            synchronized (this.mLock) {
                if (token != null) {
                    try {
                        token.unlinkToDeath(this.mDeathRecipient, 0);
                    } catch (java.util.NoSuchElementException e) {
                    }
                    this.mCache.remove(token);
                } else {
                    this.mCache.remove(token);
                }
            }
        }

        int size() {
            int size;
            synchronized (this.mLock) {
                size = this.mCache.size();
            }
            return size;
        }
    }

    static class StrippedTextClassificationContext {
        public final boolean useDefaultTextClassifier;
        public final int userId;

        StrippedTextClassificationContext(android.view.textclassifier.TextClassificationContext textClassificationContext) {
            android.view.textclassifier.SystemTextClassifierMetadata sysTcMetadata = textClassificationContext.getSystemTextClassifierMetadata();
            this.userId = sysTcMetadata.getUserId();
            this.useDefaultTextClassifier = sysTcMetadata.useDefaultTextClassifier();
        }
    }

    private final class UserState {
        private final com.android.server.textclassifier.TextClassificationManagerService.ServiceState mDefaultServiceState;
        private final com.android.server.textclassifier.TextClassificationManagerService.ServiceState mSystemServiceState;
        private com.android.server.textclassifier.TextClassificationManagerService.ServiceState mUntrustedServiceState;
        final int mUserId;

        private UserState(int userId) {
            com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState;
            this.mUserId = userId;
            if (android.text.TextUtils.isEmpty(com.android.server.textclassifier.TextClassificationManagerService.this.mDefaultTextClassifierPackage)) {
                serviceState = null;
            } else {
                serviceState = new com.android.server.textclassifier.TextClassificationManagerService.ServiceState(userId, com.android.server.textclassifier.TextClassificationManagerService.this.mDefaultTextClassifierPackage, true);
            }
            this.mDefaultServiceState = serviceState;
            this.mSystemServiceState = android.text.TextUtils.isEmpty(com.android.server.textclassifier.TextClassificationManagerService.this.mSystemTextClassifierPackage) ? null : new com.android.server.textclassifier.TextClassificationManagerService.ServiceState(userId, com.android.server.textclassifier.TextClassificationManagerService.this.mSystemTextClassifierPackage, true);
        }

        com.android.server.textclassifier.TextClassificationManagerService.ServiceState getServiceStateLocked(boolean useDefaultTextClassifier) {
            if (useDefaultTextClassifier) {
                return this.mDefaultServiceState;
            }
            final android.view.textclassifier.TextClassificationConstants textClassificationConstants = com.android.server.textclassifier.TextClassificationManagerService.this.mSettings;
            java.util.Objects.requireNonNull(textClassificationConstants);
            java.lang.String textClassifierServicePackageOverride = (java.lang.String) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.textclassifier.TextClassificationManagerService$UserState$$ExternalSyntheticLambda0
                public final java.lang.Object getOrThrow() {
                    return textClassificationConstants.getTextClassifierServicePackageOverride();
                }
            });
            if (android.text.TextUtils.isEmpty(textClassifierServicePackageOverride)) {
                return this.mSystemServiceState != null ? this.mSystemServiceState : this.mDefaultServiceState;
            }
            if (textClassifierServicePackageOverride.equals(com.android.server.textclassifier.TextClassificationManagerService.this.mDefaultTextClassifierPackage)) {
                return this.mDefaultServiceState;
            }
            if (textClassifierServicePackageOverride.equals(com.android.server.textclassifier.TextClassificationManagerService.this.mSystemTextClassifierPackage) && this.mSystemServiceState != null) {
                return this.mSystemServiceState;
            }
            if (this.mUntrustedServiceState == null) {
                this.mUntrustedServiceState = new com.android.server.textclassifier.TextClassificationManagerService.ServiceState(this.mUserId, textClassifierServicePackageOverride, false);
            }
            return this.mUntrustedServiceState;
        }

        void onTextClassifierServicePackageOverrideChangedLocked(java.lang.String overriddenPackageName) {
            for (com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState : getAllServiceStatesLocked()) {
                serviceState.unbindIfBoundLocked();
            }
            this.mUntrustedServiceState = null;
        }

        void bindIfHasPendingRequestsLocked() {
            for (com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState : getAllServiceStatesLocked()) {
                serviceState.bindIfHasPendingRequestsLocked();
            }
        }

        void cleanupServiceLocked() {
            for (com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState : getAllServiceStatesLocked()) {
                if (serviceState.mConnection != null) {
                    serviceState.mConnection.cleanupService();
                }
            }
        }

        private java.util.List<com.android.server.textclassifier.TextClassificationManagerService.ServiceState> getAllServiceStatesLocked() {
            java.util.List<com.android.server.textclassifier.TextClassificationManagerService.ServiceState> serviceStates = new java.util.ArrayList<>();
            if (this.mDefaultServiceState != null) {
                serviceStates.add(this.mDefaultServiceState);
            }
            if (this.mSystemServiceState != null) {
                serviceStates.add(this.mSystemServiceState);
            }
            if (this.mUntrustedServiceState != null) {
                serviceStates.add(this.mUntrustedServiceState);
            }
            return serviceStates;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.textclassifier.TextClassificationManagerService.ServiceState getServiceStateLocked(java.lang.String packageName) {
            for (com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState : getAllServiceStatesLocked()) {
                if (serviceState.mPackageName.equals(packageName)) {
                    return serviceState;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePackageStateLocked() {
            for (com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState : getAllServiceStatesLocked()) {
                serviceState.updatePackageStateLocked();
            }
        }

        void dump(com.android.internal.util.IndentingPrintWriter pw) {
            synchronized (com.android.server.textclassifier.TextClassificationManagerService.this.mLock) {
                pw.increaseIndent();
                dump(pw, this.mDefaultServiceState, "Default");
                dump(pw, this.mSystemServiceState, "System");
                dump(pw, this.mUntrustedServiceState, "Untrusted");
                pw.decreaseIndent();
            }
        }

        private void dump(com.android.internal.util.IndentingPrintWriter pw, com.android.server.textclassifier.TextClassificationManagerService.ServiceState serviceState, java.lang.String name) {
            synchronized (com.android.server.textclassifier.TextClassificationManagerService.this.mLock) {
                if (serviceState != null) {
                    pw.print(name + ": ");
                    serviceState.dump(pw);
                    pw.println();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class ServiceState {
        private static final int MAX_PENDING_REQUESTS = 20;
        final int mBindServiceFlags;
        boolean mBinding;
        android.content.ComponentName mBoundComponentName;
        int mBoundServiceUid;
        final com.android.server.textclassifier.TextClassificationManagerService.ServiceState.TextClassifierServiceConnection mConnection;
        boolean mEnabled;
        boolean mInstalled;
        final boolean mIsTrusted;
        final java.lang.String mPackageName;
        final com.android.server.textclassifier.FixedSizeQueue<com.android.server.textclassifier.TextClassificationManagerService.PendingRequest> mPendingRequests;
        android.service.textclassifier.ITextClassifierService mService;
        final int mUserId;

        static /* synthetic */ void lambda$new$0(com.android.server.textclassifier.TextClassificationManagerService.PendingRequest request) {
            android.util.Slog.w(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, java.lang.String.format("Pending request[%s] is dropped", request.mName));
            request.mOnServiceFailure.run();
        }

        private ServiceState(int userId, java.lang.String packageName, boolean isTrusted) {
            this.mPendingRequests = new com.android.server.textclassifier.FixedSizeQueue<>(20, new com.android.server.textclassifier.FixedSizeQueue.OnEntryEvictedListener() { // from class: com.android.server.textclassifier.TextClassificationManagerService$ServiceState$$ExternalSyntheticLambda0
                @Override // com.android.server.textclassifier.FixedSizeQueue.OnEntryEvictedListener
                public final void onEntryEvicted(java.lang.Object obj) {
                    com.android.server.textclassifier.TextClassificationManagerService.ServiceState.lambda$new$0((com.android.server.textclassifier.TextClassificationManagerService.PendingRequest) obj);
                }
            });
            this.mBoundComponentName = null;
            this.mBoundServiceUid = -1;
            this.mUserId = userId;
            this.mPackageName = packageName;
            this.mConnection = new com.android.server.textclassifier.TextClassificationManagerService.ServiceState.TextClassifierServiceConnection(this.mUserId);
            this.mIsTrusted = isTrusted;
            this.mBindServiceFlags = createBindServiceFlags(packageName);
            this.mInstalled = isPackageInstalledForUser();
            this.mEnabled = isServiceEnabledForUser();
        }

        private int createBindServiceFlags(java.lang.String packageName) {
            if (!packageName.equals(com.android.server.textclassifier.TextClassificationManagerService.this.mDefaultTextClassifierPackage)) {
                int flags = 67108865 | 2097152;
                return flags;
            }
            return android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN;
        }

        private boolean isPackageInstalledForUser() {
            try {
                android.content.pm.PackageManager packageManager = com.android.server.textclassifier.TextClassificationManagerService.this.mContext.getPackageManager();
                return packageManager.getPackageInfoAsUser(this.mPackageName, 0, this.mUserId) != null;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        private boolean isServiceEnabledForUser() {
            android.content.pm.PackageManager packageManager = com.android.server.textclassifier.TextClassificationManagerService.this.mContext.getPackageManager();
            android.content.Intent intent = new android.content.Intent("android.service.textclassifier.TextClassifierService");
            intent.setPackage(this.mPackageName);
            android.content.pm.ResolveInfo resolveInfo = packageManager.resolveServiceAsUser(intent, 4, this.mUserId);
            android.content.pm.ServiceInfo serviceInfo = resolveInfo == null ? null : resolveInfo.serviceInfo;
            return serviceInfo != null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onPackageInstallStatusChangeLocked(boolean installed) {
            this.mInstalled = installed;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onPackageModifiedLocked() {
            this.mEnabled = isServiceEnabledForUser();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePackageStateLocked() {
            this.mInstalled = isPackageInstalledForUser();
            this.mEnabled = isServiceEnabledForUser();
        }

        boolean isInstalledLocked() {
            return this.mInstalled;
        }

        boolean isEnabledLocked() {
            return this.mEnabled;
        }

        boolean isBoundLocked() {
            return this.mService != null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handlePendingRequestsLocked() {
            while (true) {
                com.android.server.textclassifier.TextClassificationManagerService.PendingRequest request = this.mPendingRequests.poll();
                if (request != null) {
                    if (isBoundLocked()) {
                        if (!checkRequestAcceptedLocked(request.mUid, request.mName)) {
                            android.util.Slog.w(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, java.lang.String.format("UID %d is not allowed to see the %s request", java.lang.Integer.valueOf(request.mUid), request.mName));
                            request.mOnServiceFailure.run();
                        } else {
                            request.mRequest.run();
                        }
                    } else {
                        android.util.Slog.d(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "Unable to bind TextClassifierService for PendingRequest " + request.mName);
                        request.mOnServiceFailure.run();
                    }
                    if (request.mBinder != null) {
                        request.mBinder.unlinkToDeath(request, 0);
                    }
                } else {
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean bindIfHasPendingRequestsLocked() {
            return !this.mPendingRequests.isEmpty() && bindLocked();
        }

        void unbindIfBoundLocked() {
            if (isBoundLocked()) {
                android.util.Slog.v(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "Unbinding " + this.mBoundComponentName + " for " + this.mUserId);
                com.android.server.textclassifier.TextClassificationManagerService.this.mContext.unbindService(this.mConnection);
                this.mConnection.cleanupService();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean bindLocked() {
            if (isBoundLocked() || this.mBinding) {
                return true;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.content.ComponentName componentName = getTextClassifierServiceComponent();
                if (componentName != null) {
                    android.content.Intent serviceIntent = new android.content.Intent("android.service.textclassifier.TextClassifierService").setComponent(componentName);
                    android.util.Slog.d(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "Binding to " + serviceIntent.getComponent());
                    boolean willBind = com.android.server.textclassifier.TextClassificationManagerService.this.mContext.bindServiceAsUser(serviceIntent, this.mConnection, this.mBindServiceFlags, android.os.UserHandle.of(this.mUserId));
                    if (!willBind) {
                        android.util.Slog.e(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "Could not bind to " + componentName);
                    }
                    this.mBinding = willBind;
                    return willBind;
                }
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private android.content.ComponentName getTextClassifierServiceComponent() {
            return android.service.textclassifier.TextClassifierService.getServiceComponentName(com.android.server.textclassifier.TextClassificationManagerService.this.mContext, this.mPackageName, this.mIsTrusted ? 1048576 : 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(com.android.internal.util.IndentingPrintWriter pw) {
            pw.printPair("context", com.android.server.textclassifier.TextClassificationManagerService.this.mContext);
            pw.printPair("userId", java.lang.Integer.valueOf(this.mUserId));
            synchronized (com.android.server.textclassifier.TextClassificationManagerService.this.mLock) {
                pw.printPair(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, this.mPackageName);
                pw.printPair("installed", java.lang.Boolean.valueOf(this.mInstalled));
                pw.printPair(com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED, java.lang.Boolean.valueOf(this.mEnabled));
                pw.printPair("boundComponentName", this.mBoundComponentName);
                pw.printPair("isTrusted", java.lang.Boolean.valueOf(this.mIsTrusted));
                pw.printPair("bindServiceFlags", java.lang.Integer.valueOf(this.mBindServiceFlags));
                pw.printPair("boundServiceUid", java.lang.Integer.valueOf(this.mBoundServiceUid));
                pw.printPair("binding", java.lang.Boolean.valueOf(this.mBinding));
                pw.printPair("numOfPendingRequests", java.lang.Integer.valueOf(this.mPendingRequests.size()));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean checkRequestAcceptedLocked(int requestUid, java.lang.String methodName) {
            if (this.mIsTrusted || requestUid == this.mBoundServiceUid) {
                return true;
            }
            android.util.Slog.w(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, java.lang.String.format("[%s] Non-default TextClassifierServices may only see text from the same uid.", methodName));
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateServiceInfoLocked(int userId, android.content.ComponentName componentName) {
            int iResolvePackageToUid;
            this.mBoundComponentName = componentName;
            if (this.mBoundComponentName == null) {
                iResolvePackageToUid = -1;
            } else {
                iResolvePackageToUid = com.android.server.textclassifier.TextClassificationManagerService.this.resolvePackageToUid(this.mBoundComponentName.getPackageName(), userId);
            }
            this.mBoundServiceUid = iResolvePackageToUid;
        }

        private final class TextClassifierServiceConnection implements android.content.ServiceConnection {
            private final int mUserId;

            TextClassifierServiceConnection(int userId) {
                this.mUserId = userId;
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                android.service.textclassifier.ITextClassifierService tcService = android.service.textclassifier.ITextClassifierService.Stub.asInterface(service);
                try {
                    tcService.onConnectedStateChanged(0);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "error in onConnectedStateChanged");
                }
                init(tcService, name);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(android.content.ComponentName name) {
                android.util.Slog.i(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "onServiceDisconnected called with " + name);
                cleanupService();
            }

            @Override // android.content.ServiceConnection
            public void onBindingDied(android.content.ComponentName name) {
                android.util.Slog.i(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "onBindingDied called with " + name);
                cleanupService();
            }

            @Override // android.content.ServiceConnection
            public void onNullBinding(android.content.ComponentName name) {
                android.util.Slog.i(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "onNullBinding called with " + name);
                cleanupService();
            }

            void cleanupService() {
                init(null, null);
            }

            private void init(android.service.textclassifier.ITextClassifierService service, android.content.ComponentName name) {
                synchronized (com.android.server.textclassifier.TextClassificationManagerService.this.mLock) {
                    com.android.server.textclassifier.TextClassificationManagerService.ServiceState.this.mService = service;
                    com.android.server.textclassifier.TextClassificationManagerService.ServiceState.this.mBinding = false;
                    com.android.server.textclassifier.TextClassificationManagerService.ServiceState.this.updateServiceInfoLocked(this.mUserId, name);
                    com.android.server.textclassifier.TextClassificationManagerService.ServiceState.this.handlePendingRequestsLocked();
                }
            }
        }
    }

    private final class TextClassifierSettingsListener implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private final android.content.Context mContext;
        private java.lang.String mServicePackageOverride;

        TextClassifierSettingsListener(android.content.Context context) {
            this.mContext = context;
            this.mServicePackageOverride = com.android.server.textclassifier.TextClassificationManagerService.this.mSettings.getTextClassifierServicePackageOverride();
        }

        void registerObserver() {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("textclassifier", this.mContext.getMainExecutor(), this);
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            java.lang.String currentServicePackageOverride = com.android.server.textclassifier.TextClassificationManagerService.this.mSettings.getTextClassifierServicePackageOverride();
            if (android.text.TextUtils.equals(currentServicePackageOverride, this.mServicePackageOverride)) {
                return;
            }
            this.mServicePackageOverride = currentServicePackageOverride;
            com.android.server.textclassifier.TextClassificationManagerService.this.onTextClassifierServicePackageOverrideChanged(currentServicePackageOverride);
        }
    }

    private static final class CallbackWrapper extends android.service.textclassifier.ITextClassifierCallback.Stub {
        private final android.service.textclassifier.ITextClassifierCallback mWrapped;

        CallbackWrapper(android.service.textclassifier.ITextClassifierCallback wrapped) {
            this.mWrapped = (android.service.textclassifier.ITextClassifierCallback) java.util.Objects.requireNonNull(wrapped);
        }

        public void onSuccess(android.os.Bundle result) {
            android.os.Parcelable parcelled = android.service.textclassifier.TextClassifierService.getResponse(result);
            if (parcelled instanceof android.view.textclassifier.TextClassification) {
                rewriteTextClassificationIcons(result);
            } else if (parcelled instanceof android.view.textclassifier.ConversationActions) {
                rewriteConversationActionsIcons(result);
            } else if (parcelled instanceof android.view.textclassifier.TextSelection) {
                rewriteTextSelectionIcons(result);
            }
            try {
                this.mWrapped.onSuccess(result);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "Callback error", e);
            }
        }

        private static void rewriteTextSelectionIcons(android.os.Bundle result) {
            android.view.textclassifier.TextClassification newTextClassification;
            android.view.textclassifier.TextSelection textSelection = (android.view.textclassifier.TextSelection) android.service.textclassifier.TextClassifierService.getResponse(result);
            if (textSelection.getTextClassification() == null || (newTextClassification = rewriteTextClassificationIcons(textSelection.getTextClassification())) == null) {
                return;
            }
            android.service.textclassifier.TextClassifierService.putResponse(result, textSelection.toBuilder().setTextClassification(newTextClassification).build());
        }

        private static android.view.textclassifier.TextClassification rewriteTextClassificationIcons(android.view.textclassifier.TextClassification textClassification) {
            android.app.RemoteAction validAction;
            boolean rewrite = false;
            java.util.List<android.app.RemoteAction> actions = textClassification.getActions();
            int size = actions.size();
            java.util.List<android.app.RemoteAction> validActions = new java.util.ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                android.app.RemoteAction action = actions.get(i);
                if (shouldRewriteIcon(action)) {
                    rewrite = true;
                    validAction = validAction(action);
                } else {
                    validAction = action;
                }
                validActions.add(validAction);
            }
            if (rewrite) {
                return textClassification.toBuilder().clearActions().addActions(validActions).build();
            }
            return null;
        }

        private static void rewriteTextClassificationIcons(android.os.Bundle result) {
            android.view.textclassifier.TextClassification classification = (android.view.textclassifier.TextClassification) android.service.textclassifier.TextClassifierService.getResponse(result);
            android.view.textclassifier.TextClassification newTextClassification = rewriteTextClassificationIcons(classification);
            if (newTextClassification != null) {
                android.service.textclassifier.TextClassifierService.putResponse(result, newTextClassification);
            }
        }

        private static void rewriteConversationActionsIcons(android.os.Bundle result) {
            android.view.textclassifier.ConversationAction validConvAction;
            android.view.textclassifier.ConversationActions convActions = (android.view.textclassifier.ConversationActions) android.service.textclassifier.TextClassifierService.getResponse(result);
            boolean rewrite = false;
            java.util.List<android.view.textclassifier.ConversationAction> origConvActions = convActions.getConversationActions();
            int size = origConvActions.size();
            java.util.List<android.view.textclassifier.ConversationAction> validConvActions = new java.util.ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                android.view.textclassifier.ConversationAction convAction = origConvActions.get(i);
                if (shouldRewriteIcon(convAction.getAction())) {
                    rewrite = true;
                    validConvAction = convAction.toBuilder().setAction(validAction(convAction.getAction())).build();
                } else {
                    validConvAction = convAction;
                }
                validConvActions.add(validConvAction);
            }
            if (rewrite) {
                android.service.textclassifier.TextClassifierService.putResponse(result, new android.view.textclassifier.ConversationActions(validConvActions, convActions.getId()));
            }
        }

        private static android.app.RemoteAction validAction(android.app.RemoteAction action) {
            android.app.RemoteAction newAction = new android.app.RemoteAction(changeIcon(action.getIcon()), action.getTitle(), action.getContentDescription(), action.getActionIntent());
            newAction.setEnabled(action.isEnabled());
            newAction.setShouldShowIcon(action.shouldShowIcon());
            return newAction;
        }

        private static boolean shouldRewriteIcon(android.app.RemoteAction action) {
            return action != null && action.getIcon().getType() == 2;
        }

        private static android.graphics.drawable.Icon changeIcon(android.graphics.drawable.Icon icon) {
            android.net.Uri uri = com.android.server.textclassifier.IconsUriHelper.getInstance().getContentUri(icon.getResPackage(), icon.getResId());
            return android.graphics.drawable.Icon.createWithContentUri(uri);
        }

        public void onFailure() {
            try {
                this.mWrapped.onFailure();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.textclassifier.TextClassificationManagerService.LOG_TAG, "Callback error", e);
            }
        }
    }
}
