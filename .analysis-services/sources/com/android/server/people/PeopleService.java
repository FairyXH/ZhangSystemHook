package com.android.server.people;

/* JADX INFO: loaded from: classes2.dex */
public class PeopleService extends com.android.server.SystemService {
    private static final java.lang.String TAG = "PeopleService";
    private com.android.server.people.PeopleService.ConversationListenerHelper mLazyConversationListenerHelper;
    private com.android.server.people.data.DataManager mLazyDataManager;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    final android.os.IBinder mService;

    public PeopleService(android.content.Context context) {
        super(context);
        this.mService = new android.app.people.IPeopleManager.Stub() { // from class: com.android.server.people.PeopleService.1
            public android.app.people.ConversationChannel getConversation(java.lang.String packageName, int userId, java.lang.String shortcutId) {
                com.android.server.people.PeopleService.this.enforceSystemRootOrSystemUI(com.android.server.people.PeopleService.this.getContext(), "get conversation");
                return com.android.server.people.PeopleService.this.getDataManager().getConversation(packageName, userId, shortcutId);
            }

            public android.content.pm.ParceledListSlice<android.app.people.ConversationChannel> getRecentConversations() {
                com.android.server.people.PeopleService.this.enforceSystemRootOrSystemUI(com.android.server.people.PeopleService.this.getContext(), "get recent conversations");
                return new android.content.pm.ParceledListSlice<>(com.android.server.people.PeopleService.this.getDataManager().getRecentConversations(android.os.Binder.getCallingUserHandle().getIdentifier()));
            }

            public void removeRecentConversation(java.lang.String packageName, int userId, java.lang.String shortcutId) {
                com.android.server.people.PeopleService.enforceSystemOrRoot("remove a recent conversation");
                com.android.server.people.PeopleService.this.getDataManager().removeRecentConversation(packageName, userId, shortcutId, android.os.Binder.getCallingUserHandle().getIdentifier());
            }

            public void removeAllRecentConversations() {
                com.android.server.people.PeopleService.enforceSystemOrRoot("remove all recent conversations");
                com.android.server.people.PeopleService.this.getDataManager().removeAllRecentConversations(android.os.Binder.getCallingUserHandle().getIdentifier());
            }

            public boolean isConversation(java.lang.String packageName, int userId, java.lang.String shortcutId) {
                enforceHasReadPeopleDataPermission();
                com.android.server.people.PeopleService.this.handleIncomingUser(userId);
                return com.android.server.people.PeopleService.this.getDataManager().isConversation(packageName, userId, shortcutId);
            }

            private void enforceHasReadPeopleDataPermission() throws java.lang.SecurityException {
                if (com.android.server.people.PeopleService.this.getContext().checkCallingPermission("android.permission.READ_PEOPLE_DATA") != 0) {
                    throw new java.lang.SecurityException("Caller doesn't have READ_PEOPLE_DATA permission.");
                }
            }

            public long getLastInteraction(java.lang.String packageName, int userId, java.lang.String shortcutId) {
                com.android.server.people.PeopleService.this.enforceSystemRootOrSystemUI(com.android.server.people.PeopleService.this.getContext(), "get last interaction");
                return com.android.server.people.PeopleService.this.getDataManager().getLastInteraction(packageName, userId, shortcutId);
            }

            public void addOrUpdateStatus(java.lang.String packageName, int userId, java.lang.String conversationId, android.app.people.ConversationStatus status) {
                com.android.server.people.PeopleService.this.handleIncomingUser(userId);
                com.android.server.people.PeopleService.this.checkCallerIsSameApp(packageName);
                if (status.getStartTimeMillis() > java.lang.System.currentTimeMillis()) {
                    throw new java.lang.IllegalArgumentException("Start time must be in the past");
                }
                com.android.server.people.PeopleService.this.getDataManager().addOrUpdateStatus(packageName, userId, conversationId, status);
            }

            public void clearStatus(java.lang.String packageName, int userId, java.lang.String conversationId, java.lang.String statusId) {
                com.android.server.people.PeopleService.this.handleIncomingUser(userId);
                com.android.server.people.PeopleService.this.checkCallerIsSameApp(packageName);
                com.android.server.people.PeopleService.this.getDataManager().clearStatus(packageName, userId, conversationId, statusId);
            }

            public void clearStatuses(java.lang.String packageName, int userId, java.lang.String conversationId) {
                com.android.server.people.PeopleService.this.handleIncomingUser(userId);
                com.android.server.people.PeopleService.this.checkCallerIsSameApp(packageName);
                com.android.server.people.PeopleService.this.getDataManager().clearStatuses(packageName, userId, conversationId);
            }

            public android.content.pm.ParceledListSlice<android.app.people.ConversationStatus> getStatuses(java.lang.String packageName, int userId, java.lang.String conversationId) {
                com.android.server.people.PeopleService.this.handleIncomingUser(userId);
                if (!com.android.server.people.PeopleService.isSystemOrRoot()) {
                    com.android.server.people.PeopleService.this.checkCallerIsSameApp(packageName);
                }
                return new android.content.pm.ParceledListSlice<>(com.android.server.people.PeopleService.this.getDataManager().getStatuses(packageName, userId, conversationId));
            }

            public void registerConversationListener(java.lang.String packageName, int userId, java.lang.String shortcutId, android.app.people.IConversationListener listener) {
                com.android.server.people.PeopleService.this.enforceSystemRootOrSystemUI(com.android.server.people.PeopleService.this.getContext(), "register conversation listener");
                com.android.server.people.PeopleService.this.getConversationListenerHelper().addConversationListener(new com.android.server.people.PeopleService.ListenerKey(packageName, java.lang.Integer.valueOf(userId), shortcutId), listener);
            }

            public void unregisterConversationListener(android.app.people.IConversationListener listener) {
                com.android.server.people.PeopleService.this.enforceSystemRootOrSystemUI(com.android.server.people.PeopleService.this.getContext(), "unregister conversation listener");
                com.android.server.people.PeopleService.this.getConversationListenerHelper().removeConversationListener(listener);
            }
        };
    }

    com.android.server.people.PeopleService.ConversationListenerHelper getConversationListenerHelper() {
        if (this.mLazyConversationListenerHelper == null) {
            initLazyStuff();
        }
        return this.mLazyConversationListenerHelper;
    }

    private synchronized void initLazyStuff() {
        if (this.mLazyDataManager == null) {
            this.mLazyDataManager = new com.android.server.people.data.DataManager(getContext());
            this.mLazyDataManager.initialize();
            this.mLazyConversationListenerHelper = new com.android.server.people.PeopleService.ConversationListenerHelper();
            this.mLazyDataManager.addConversationsListener(this.mLazyConversationListenerHelper);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.people.data.DataManager getDataManager() {
        if (this.mLazyDataManager == null) {
            initLazyStuff();
        }
        return this.mLazyDataManager;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        onStart(false);
    }

    protected void onStart(boolean isForTesting) {
        if (!isForTesting) {
            publishBinderService("people", this.mService);
        }
        publishLocalService(com.android.server.people.PeopleServiceInternal.class, new com.android.server.people.PeopleService.LocalService());
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocked(com.android.server.SystemService.TargetUser user) {
        getDataManager().onUserUnlocked(user.getUserIdentifier());
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        getDataManager().onUserStopping(user.getUserIdentifier());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void enforceSystemOrRoot(java.lang.String message) {
        if (!isSystemOrRoot()) {
            throw new java.lang.SecurityException("Only system may " + message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSystemOrRoot() {
        int uid = android.os.Binder.getCallingUid();
        return android.os.UserHandle.isSameApp(uid, 1000) || uid == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int handleIncomingUser(int userId) {
        try {
            return android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, true, true, "", (java.lang.String) null);
        } catch (android.os.RemoteException e) {
            return userId;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSameApp(java.lang.String pkg) {
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (this.mPackageManagerInternal.getPackageUid(pkg, 0L, callingUserId) != callingUid) {
            throw new java.lang.SecurityException("Calling uid " + callingUid + " cannot query eventsfor package " + pkg);
        }
    }

    protected void enforceSystemRootOrSystemUI(android.content.Context context, java.lang.String message) {
        if (isSystemOrRoot()) {
            return;
        }
        context.enforceCallingPermission("android.permission.STATUS_BAR_SERVICE", message);
    }

    public interface ConversationsListener {
        default void onConversationsUpdate(java.util.List<android.app.people.ConversationChannel> conversations) {
        }
    }

    public static class ConversationListenerHelper implements com.android.server.people.PeopleService.ConversationsListener {
        final android.os.RemoteCallbackList<android.app.people.IConversationListener> mListeners = new android.os.RemoteCallbackList<>();

        ConversationListenerHelper() {
        }

        public synchronized void addConversationListener(com.android.server.people.PeopleService.ListenerKey key, android.app.people.IConversationListener listener) {
            this.mListeners.unregister(listener);
            this.mListeners.register(listener, key);
        }

        public synchronized void removeConversationListener(android.app.people.IConversationListener listener) {
            this.mListeners.unregister(listener);
        }

        @Override // com.android.server.people.PeopleService.ConversationsListener
        public void onConversationsUpdate(java.util.List<android.app.people.ConversationChannel> conversations) {
            int count = this.mListeners.beginBroadcast();
            if (count == 0) {
                return;
            }
            java.util.Map<com.android.server.people.PeopleService.ListenerKey, android.app.people.ConversationChannel> keyedConversations = new java.util.HashMap<>();
            for (android.app.people.ConversationChannel conversation : conversations) {
                keyedConversations.put(getListenerKey(conversation), conversation);
            }
            for (int i = 0; i < count; i++) {
                com.android.server.people.PeopleService.ListenerKey listenerKey = (com.android.server.people.PeopleService.ListenerKey) this.mListeners.getBroadcastCookie(i);
                if (keyedConversations.containsKey(listenerKey)) {
                    android.app.people.IConversationListener listener = this.mListeners.getBroadcastItem(i);
                    try {
                        android.app.people.ConversationChannel channel = keyedConversations.get(listenerKey);
                        listener.onConversationUpdate(channel);
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
            this.mListeners.finishBroadcast();
        }

        private com.android.server.people.PeopleService.ListenerKey getListenerKey(android.app.people.ConversationChannel conversation) {
            android.content.pm.ShortcutInfo info = conversation.getShortcutInfo();
            return new com.android.server.people.PeopleService.ListenerKey(info.getPackage(), java.lang.Integer.valueOf(info.getUserId()), info.getId());
        }
    }

    private static class ListenerKey {
        private final java.lang.String mPackageName;
        private final java.lang.String mShortcutId;
        private final java.lang.Integer mUserId;

        ListenerKey(java.lang.String packageName, java.lang.Integer userId, java.lang.String shortcutId) {
            this.mPackageName = packageName;
            this.mUserId = userId;
            this.mShortcutId = shortcutId;
        }

        public java.lang.String getPackageName() {
            return this.mPackageName;
        }

        public java.lang.Integer getUserId() {
            return this.mUserId;
        }

        public java.lang.String getShortcutId() {
            return this.mShortcutId;
        }

        public boolean equals(java.lang.Object o) {
            com.android.server.people.PeopleService.ListenerKey key = (com.android.server.people.PeopleService.ListenerKey) o;
            return key.getPackageName().equals(this.mPackageName) && java.util.Objects.equals(key.getUserId(), this.mUserId) && key.getShortcutId().equals(this.mShortcutId);
        }

        public int hashCode() {
            return this.mPackageName.hashCode() + this.mUserId.hashCode() + this.mShortcutId.hashCode();
        }
    }

    final class LocalService extends com.android.server.people.PeopleServiceInternal {
        private java.util.Map<android.app.prediction.AppPredictionSessionId, com.android.server.people.SessionInfo> mSessions = new android.util.ArrayMap();

        LocalService() {
        }

        public void onCreatePredictionSession(android.app.prediction.AppPredictionContext appPredictionContext, android.app.prediction.AppPredictionSessionId sessionId) {
            this.mSessions.put(sessionId, new com.android.server.people.SessionInfo(appPredictionContext, com.android.server.people.PeopleService.this.getDataManager(), sessionId.getUserId(), com.android.server.people.PeopleService.this.getContext()));
        }

        public void notifyAppTargetEvent(android.app.prediction.AppPredictionSessionId sessionId, final android.app.prediction.AppTargetEvent event) {
            runForSession(sessionId, new java.util.function.Consumer() { // from class: com.android.server.people.PeopleService$LocalService$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.people.SessionInfo) obj).getPredictor().onAppTargetEvent(event);
                }
            });
        }

        public void notifyLaunchLocationShown(android.app.prediction.AppPredictionSessionId sessionId, final java.lang.String launchLocation, final android.content.pm.ParceledListSlice targetIds) {
            runForSession(sessionId, new java.util.function.Consumer() { // from class: com.android.server.people.PeopleService$LocalService$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.people.SessionInfo) obj).getPredictor().onLaunchLocationShown(launchLocation, targetIds.getList());
                }
            });
        }

        public void sortAppTargets(android.app.prediction.AppPredictionSessionId sessionId, final android.content.pm.ParceledListSlice targets, final android.app.prediction.IPredictionCallback callback) {
            runForSession(sessionId, new java.util.function.Consumer() { // from class: com.android.server.people.PeopleService$LocalService$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$sortAppTargets$3(targets, callback, (com.android.server.people.SessionInfo) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$sortAppTargets$3(android.content.pm.ParceledListSlice targets, final android.app.prediction.IPredictionCallback callback, com.android.server.people.SessionInfo sessionInfo) {
            sessionInfo.getPredictor().onSortAppTargets(targets.getList(), new java.util.function.Consumer() { // from class: com.android.server.people.PeopleService$LocalService$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$sortAppTargets$2(callback, (java.util.List) obj);
                }
            });
        }

        public void registerPredictionUpdates(android.app.prediction.AppPredictionSessionId sessionId, final android.app.prediction.IPredictionCallback callback) {
            runForSession(sessionId, new java.util.function.Consumer() { // from class: com.android.server.people.PeopleService$LocalService$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.people.SessionInfo) obj).addCallback(callback);
                }
            });
        }

        public void unregisterPredictionUpdates(android.app.prediction.AppPredictionSessionId sessionId, final android.app.prediction.IPredictionCallback callback) {
            runForSession(sessionId, new java.util.function.Consumer() { // from class: com.android.server.people.PeopleService$LocalService$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.people.SessionInfo) obj).removeCallback(callback);
                }
            });
        }

        public void requestPredictionUpdate(android.app.prediction.AppPredictionSessionId sessionId) {
            runForSession(sessionId, new java.util.function.Consumer() { // from class: com.android.server.people.PeopleService$LocalService$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.people.SessionInfo) obj).getPredictor().onRequestPredictionUpdate();
                }
            });
        }

        public void onDestroyPredictionSession(final android.app.prediction.AppPredictionSessionId sessionId) {
            runForSession(sessionId, new java.util.function.Consumer() { // from class: com.android.server.people.PeopleService$LocalService$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onDestroyPredictionSession$7(sessionId, (com.android.server.people.SessionInfo) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDestroyPredictionSession$7(android.app.prediction.AppPredictionSessionId sessionId, com.android.server.people.SessionInfo sessionInfo) {
            sessionInfo.onDestroy();
            this.mSessions.remove(sessionId);
        }

        @Override // com.android.server.people.PeopleServiceInternal
        public void pruneDataForUser(int userId, android.os.CancellationSignal signal) {
            com.android.server.people.PeopleService.this.getDataManager().pruneDataForUser(userId, signal);
        }

        @Override // com.android.server.people.PeopleServiceInternal
        public byte[] getBackupPayload(int userId) {
            return com.android.server.people.PeopleService.this.getDataManager().getBackupPayload(userId);
        }

        @Override // com.android.server.people.PeopleServiceInternal
        public void restore(int userId, byte[] payload) throws java.io.IOException {
            com.android.server.people.PeopleService.this.getDataManager().restore(userId, payload);
        }

        public void requestServiceFeatures(android.app.prediction.AppPredictionSessionId sessionId, android.os.IRemoteCallback callback) {
        }

        com.android.server.people.SessionInfo getSessionInfo(android.app.prediction.AppPredictionSessionId sessionId) {
            return this.mSessions.get(sessionId);
        }

        private void runForSession(android.app.prediction.AppPredictionSessionId sessionId, java.util.function.Consumer<com.android.server.people.SessionInfo> method) {
            com.android.server.people.SessionInfo sessionInfo = this.mSessions.get(sessionId);
            if (sessionInfo == null) {
                android.util.Slog.e(com.android.server.people.PeopleService.TAG, "Failed to find the session: " + sessionId);
            } else {
                method.accept(sessionInfo);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: invokePredictionCallback, reason: merged with bridge method [inline-methods] */
        public void lambda$sortAppTargets$2(android.app.prediction.IPredictionCallback callback, java.util.List<android.app.prediction.AppTarget> targets) {
            try {
                callback.onResult(new android.content.pm.ParceledListSlice(targets));
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.people.PeopleService.TAG, "Failed to calling callback" + e);
            }
        }
    }
}
