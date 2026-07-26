package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
final class GameServiceProviderInstanceImpl implements com.android.server.app.GameServiceProviderInstance {
    private static final int CREATE_GAME_SESSION_TIMEOUT_MS = 10000;
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "GameServiceProviderInstance";
    private final android.app.IActivityManager mActivityManager;
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final android.app.IActivityTaskManager mActivityTaskManager;
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
    private final java.util.concurrent.Executor mBackgroundExecutor;
    private final android.content.Context mContext;
    private final com.android.internal.infra.ServiceConnector<android.service.games.IGameService> mGameServiceConnector;
    private final com.android.internal.infra.ServiceConnector<android.service.games.IGameSessionService> mGameSessionServiceConnector;
    private final com.android.server.app.GameTaskInfoProvider mGameTaskInfoProvider;
    private volatile boolean mIsRunning;
    private final com.android.internal.util.ScreenshotHelper mScreenshotHelper;
    private final android.os.UserHandle mUserHandle;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private final com.android.server.wm.WindowManagerService mWindowManagerService;
    private final com.android.internal.infra.ServiceConnector.ServiceLifecycleCallbacks<android.service.games.IGameService> mGameServiceLifecycleCallbacks = new com.android.internal.infra.ServiceConnector.ServiceLifecycleCallbacks<android.service.games.IGameService>() { // from class: com.android.server.app.GameServiceProviderInstanceImpl.1
        public void onConnected(android.service.games.IGameService service) {
            try {
                service.connected(com.android.server.app.GameServiceProviderInstanceImpl.this.mGameServiceController);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.app.GameServiceProviderInstanceImpl.TAG, "Failed to send connected event", ex);
            }
        }
    };
    private final com.android.internal.infra.ServiceConnector.ServiceLifecycleCallbacks<android.service.games.IGameSessionService> mGameSessionServiceLifecycleCallbacks = new com.android.server.app.GameServiceProviderInstanceImpl.AnonymousClass2();
    private final com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener mTaskSystemBarsVisibilityListener = new com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener() { // from class: com.android.server.app.GameServiceProviderInstanceImpl.3
        @Override // com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener
        public void onTransientSystemBarsVisibilityChanged(int taskId, boolean visible, boolean wereRevealedFromSwipeOnSystemBar) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.onTransientSystemBarsVisibilityChanged(taskId, visible, wereRevealedFromSwipeOnSystemBar);
        }
    };
    private final android.app.TaskStackListener mTaskStackListener = new com.android.server.app.GameServiceProviderInstanceImpl.AnonymousClass4();
    private final android.app.IProcessObserver mProcessObserver = new com.android.server.app.GameServiceProviderInstanceImpl.AnonymousClass5();
    private final android.service.games.IGameServiceController mGameServiceController = new com.android.server.app.GameServiceProviderInstanceImpl.AnonymousClass6();
    private final android.service.games.IGameSessionController mGameSessionController = new com.android.server.app.GameServiceProviderInstanceImpl.AnonymousClass7();
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.android.server.app.GameSessionRecord> mGameSessions = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.String> mPidToPackageMap = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Integer> mPackageNameToProcessCountMap = new java.util.concurrent.ConcurrentHashMap<>();

    /* JADX INFO: renamed from: com.android.server.app.GameServiceProviderInstanceImpl$2, reason: invalid class name */
    class AnonymousClass2 implements com.android.internal.infra.ServiceConnector.ServiceLifecycleCallbacks<android.service.games.IGameSessionService> {
        AnonymousClass2() {
        }

        public void onBinderDied() {
            com.android.server.app.GameServiceProviderInstanceImpl.this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onBinderDied$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBinderDied$0() {
            synchronized (com.android.server.app.GameServiceProviderInstanceImpl.this.mLock) {
                com.android.server.app.GameServiceProviderInstanceImpl.this.destroyAndClearAllGameSessionsLocked();
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.app.GameServiceProviderInstanceImpl$4, reason: invalid class name */
    class AnonymousClass4 extends android.app.TaskStackListener {
        AnonymousClass4() {
        }

        public void onTaskCreated(final int taskId, final android.content.ComponentName componentName) throws android.os.RemoteException {
            if (componentName == null) {
                return;
            }
            com.android.server.app.GameServiceProviderInstanceImpl.this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTaskCreated$0(taskId, componentName);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskCreated$0(int taskId, android.content.ComponentName componentName) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.onTaskCreated(taskId, componentName);
        }

        public void onTaskRemoved(final int taskId) throws android.os.RemoteException {
            com.android.server.app.GameServiceProviderInstanceImpl.this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$4$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTaskRemoved$1(taskId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskRemoved$1(int taskId) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.onTaskRemoved(taskId);
        }

        public void onTaskFocusChanged(final int taskId, final boolean focused) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$4$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTaskFocusChanged$2(taskId, focused);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskFocusChanged$2(int taskId, boolean focused) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.onTaskFocusChanged(taskId, focused);
        }
    }

    /* JADX INFO: renamed from: com.android.server.app.GameServiceProviderInstanceImpl$5, reason: invalid class name */
    class AnonymousClass5 extends android.app.IProcessObserver.Stub {
        AnonymousClass5() {
        }

        public void onForegroundActivitiesChanged(final int pid, int uid, boolean fg) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onForegroundActivitiesChanged$0(pid);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onForegroundActivitiesChanged$0(int pid) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.onForegroundActivitiesChanged(pid);
        }

        public void onProcessDied(final int pid, int uid) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$5$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onProcessDied$1(pid);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onProcessDied$1(int pid) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.onProcessDied(pid);
        }

        public void onProcessStarted(int pid, int processUid, int packageUid, java.lang.String packageName, java.lang.String processName) {
        }

        public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) {
        }
    }

    /* JADX INFO: renamed from: com.android.server.app.GameServiceProviderInstanceImpl$6, reason: invalid class name */
    class AnonymousClass6 extends android.service.games.IGameServiceController.Stub {
        AnonymousClass6() {
        }

        public void createGameSession(final int taskId) {
            super.createGameSession_enforcePermission();
            com.android.server.app.GameServiceProviderInstanceImpl.this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$createGameSession$0(taskId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createGameSession$0(int taskId) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.createGameSession(taskId);
        }
    }

    /* JADX INFO: renamed from: com.android.server.app.GameServiceProviderInstanceImpl$7, reason: invalid class name */
    class AnonymousClass7 extends android.service.games.IGameSessionController.Stub {
        AnonymousClass7() {
        }

        public void takeScreenshot(final int taskId, final com.android.internal.infra.AndroidFuture gameScreenshotResultFuture) {
            super.takeScreenshot_enforcePermission();
            com.android.server.app.GameServiceProviderInstanceImpl.this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$7$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$takeScreenshot$0(taskId, gameScreenshotResultFuture);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$takeScreenshot$0(int taskId, com.android.internal.infra.AndroidFuture gameScreenshotResultFuture) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.takeScreenshot(taskId, gameScreenshotResultFuture);
        }

        public void restartGame(final int taskId) {
            super.restartGame_enforcePermission();
            com.android.server.app.GameServiceProviderInstanceImpl.this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$restartGame$1(taskId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$restartGame$1(int taskId) {
            com.android.server.app.GameServiceProviderInstanceImpl.this.restartGame(taskId);
        }
    }

    GameServiceProviderInstanceImpl(android.os.UserHandle userHandle, java.util.concurrent.Executor backgroundExecutor, android.content.Context context, com.android.server.app.GameTaskInfoProvider gameTaskInfoProvider, android.app.IActivityManager activityManager, android.app.ActivityManagerInternal activityManagerInternal, android.app.IActivityTaskManager activityTaskManager, com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.WindowManagerInternal windowManagerInternal, com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerInternal, com.android.internal.infra.ServiceConnector<android.service.games.IGameService> gameServiceConnector, com.android.internal.infra.ServiceConnector<android.service.games.IGameSessionService> gameSessionServiceConnector, com.android.internal.util.ScreenshotHelper screenshotHelper) {
        this.mUserHandle = userHandle;
        this.mBackgroundExecutor = backgroundExecutor;
        this.mContext = context;
        this.mGameTaskInfoProvider = gameTaskInfoProvider;
        this.mActivityManager = activityManager;
        this.mActivityManagerInternal = activityManagerInternal;
        this.mActivityTaskManager = activityTaskManager;
        this.mWindowManagerService = windowManagerService;
        this.mWindowManagerInternal = windowManagerInternal;
        this.mActivityTaskManagerInternal = activityTaskManagerInternal;
        this.mGameServiceConnector = gameServiceConnector;
        this.mGameSessionServiceConnector = gameSessionServiceConnector;
        this.mScreenshotHelper = screenshotHelper;
    }

    @Override // com.android.server.app.GameServiceProviderInstance
    public void start() {
        synchronized (this.mLock) {
            startLocked();
        }
    }

    @Override // com.android.server.app.GameServiceProviderInstance
    public void stop() {
        synchronized (this.mLock) {
            stopLocked();
        }
    }

    private void startLocked() {
        if (this.mIsRunning) {
            return;
        }
        this.mIsRunning = true;
        this.mGameServiceConnector.setServiceLifecycleCallbacks(this.mGameServiceLifecycleCallbacks);
        this.mGameSessionServiceConnector.setServiceLifecycleCallbacks(this.mGameSessionServiceLifecycleCallbacks);
        this.mGameServiceConnector.connect();
        try {
            this.mActivityTaskManager.registerTaskStackListener(this.mTaskStackListener);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to register task stack listener", e);
        }
        try {
            this.mActivityManager.registerProcessObserver(this.mProcessObserver);
        } catch (android.os.RemoteException e2) {
            android.util.Slog.w(TAG, "Failed to register process observer", e2);
        }
        this.mWindowManagerInternal.registerTaskSystemBarsListener(this.mTaskSystemBarsVisibilityListener);
    }

    private void stopLocked() {
        if (!this.mIsRunning) {
            return;
        }
        this.mIsRunning = false;
        try {
            this.mActivityManager.unregisterProcessObserver(this.mProcessObserver);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to unregister process observer", e);
        }
        try {
            this.mActivityTaskManager.unregisterTaskStackListener(this.mTaskStackListener);
        } catch (android.os.RemoteException e2) {
            android.util.Slog.w(TAG, "Failed to unregister task stack listener", e2);
        }
        this.mWindowManagerInternal.unregisterTaskSystemBarsListener(this.mTaskSystemBarsVisibilityListener);
        destroyAndClearAllGameSessionsLocked();
        this.mGameServiceConnector.post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.games.IGameService) obj).disconnected();
            }
        }).whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$stopLocked$0((java.lang.Void) obj, (java.lang.Throwable) obj2);
            }
        });
        this.mGameSessionServiceConnector.unbind();
        this.mGameServiceConnector.setServiceLifecycleCallbacks((com.android.internal.infra.ServiceConnector.ServiceLifecycleCallbacks) null);
        this.mGameSessionServiceConnector.setServiceLifecycleCallbacks((com.android.internal.infra.ServiceConnector.ServiceLifecycleCallbacks) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopLocked$0(java.lang.Void result, java.lang.Throwable t) {
        this.mGameServiceConnector.unbind();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTaskCreated(int taskId, android.content.ComponentName componentName) {
        com.android.server.app.GameTaskInfo taskInfo = this.mGameTaskInfoProvider.get(taskId, componentName);
        if (!taskInfo.mIsGameTask) {
            return;
        }
        synchronized (this.mLock) {
            gameTaskStartedLocked(taskInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTaskFocusChanged(int taskId, boolean focused) {
        synchronized (this.mLock) {
            onTaskFocusChangedLocked(taskId, focused);
        }
    }

    private void onTaskFocusChangedLocked(int taskId, boolean focused) {
        com.android.server.app.GameSessionRecord gameSessionRecord = this.mGameSessions.get(java.lang.Integer.valueOf(taskId));
        if (gameSessionRecord == null) {
            if (focused) {
                maybeCreateGameSessionForFocusedTaskLocked(taskId);
            }
        } else {
            if (gameSessionRecord.getGameSession() == null) {
                return;
            }
            try {
                gameSessionRecord.getGameSession().onTaskFocusChanged(focused);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to notify session of task focus change: " + gameSessionRecord);
            }
        }
    }

    private void maybeCreateGameSessionForFocusedTaskLocked(int taskId) {
        com.android.server.app.GameTaskInfo taskInfo = this.mGameTaskInfoProvider.get(taskId);
        if (taskInfo == null) {
            android.util.Slog.w(TAG, "No task info for focused task: " + taskId);
        } else {
            if (!taskInfo.mIsGameTask) {
                return;
            }
            gameTaskStartedLocked(taskInfo);
        }
    }

    private void gameTaskStartedLocked(final com.android.server.app.GameTaskInfo gameTaskInfo) {
        if (!this.mIsRunning) {
            return;
        }
        com.android.server.app.GameSessionRecord existingGameSessionRecord = this.mGameSessions.get(java.lang.Integer.valueOf(gameTaskInfo.mTaskId));
        if (existingGameSessionRecord != null) {
            android.util.Slog.w(TAG, "Existing game session found for task (id: " + gameTaskInfo.mTaskId + ") creation. Ignoring.");
            return;
        }
        com.android.server.app.GameSessionRecord gameSessionRecord = com.android.server.app.GameSessionRecord.awaitingGameSessionRequest(gameTaskInfo.mTaskId, gameTaskInfo.mComponentName);
        this.mGameSessions.put(java.lang.Integer.valueOf(gameTaskInfo.mTaskId), gameSessionRecord);
        this.mGameServiceConnector.post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$$ExternalSyntheticLambda3
            public final void runNoResult(java.lang.Object obj) {
                com.android.server.app.GameTaskInfo gameTaskInfo2 = gameTaskInfo;
                ((android.service.games.IGameService) obj).gameStarted(new android.service.games.GameStartedEvent(gameTaskInfo2.mTaskId, gameTaskInfo2.mComponentName.getPackageName()));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTaskRemoved(int taskId) {
        synchronized (this.mLock) {
            boolean isTaskAssociatedWithGameSession = this.mGameSessions.containsKey(java.lang.Integer.valueOf(taskId));
            if (isTaskAssociatedWithGameSession) {
                removeAndDestroyGameSessionIfNecessaryLocked(taskId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTransientSystemBarsVisibilityChanged(int taskId, boolean visible, boolean wereRevealedFromSwipeOnSystemBar) {
        com.android.server.app.GameSessionRecord gameSessionRecord;
        android.service.games.IGameSession gameSession;
        if (visible && !wereRevealedFromSwipeOnSystemBar) {
            return;
        }
        synchronized (this.mLock) {
            gameSessionRecord = this.mGameSessions.get(java.lang.Integer.valueOf(taskId));
        }
        if (gameSessionRecord == null || (gameSession = gameSessionRecord.getGameSession()) == null) {
            return;
        }
        try {
            gameSession.onTransientSystemBarVisibilityFromRevealGestureChanged(visible);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to send transient system bars visibility from reveal gesture for task: " + taskId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createGameSession(int taskId) {
        synchronized (this.mLock) {
            createGameSessionLocked(taskId);
        }
    }

    private void createGameSessionLocked(final int taskId) {
        if (!this.mIsRunning) {
            return;
        }
        final com.android.server.app.GameSessionRecord existingGameSessionRecord = this.mGameSessions.get(java.lang.Integer.valueOf(taskId));
        if (existingGameSessionRecord == null) {
            android.util.Slog.w(TAG, "No existing game session record found for task (id: " + taskId + ") creation. Ignoring.");
            return;
        }
        if (!existingGameSessionRecord.isAwaitingGameSessionRequest()) {
            android.util.Slog.w(TAG, "Existing game session for task (id: " + taskId + ") is not awaiting game session request. Ignoring.");
            return;
        }
        final android.service.games.GameSessionViewHostConfiguration gameSessionViewHostConfiguration = createViewHostConfigurationForTask(taskId);
        if (gameSessionViewHostConfiguration == null) {
            android.util.Slog.w(TAG, "Failed to create view host configuration for task (id" + taskId + ") creation. Ignoring.");
            return;
        }
        this.mGameSessions.put(java.lang.Integer.valueOf(taskId), existingGameSessionRecord.withGameSessionRequested());
        final com.android.internal.infra.AndroidFuture<android.service.games.CreateGameSessionResult> createGameSessionResultFuture = new com.android.internal.infra.AndroidFuture().orTimeout(10000L, java.util.concurrent.TimeUnit.MILLISECONDS).whenCompleteAsync(new java.util.function.BiConsumer() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$createGameSessionLocked$2(existingGameSessionRecord, taskId, (android.service.games.CreateGameSessionResult) obj, (java.lang.Throwable) obj2);
            }
        }, this.mBackgroundExecutor);
        this.mGameSessionServiceConnector.post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$$ExternalSyntheticLambda5
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                this.f$0.lambda$createGameSessionLocked$3(taskId, existingGameSessionRecord, gameSessionViewHostConfiguration, createGameSessionResultFuture, (android.service.games.IGameSessionService) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGameSessionLocked$2(com.android.server.app.GameSessionRecord existingGameSessionRecord, int taskId, android.service.games.CreateGameSessionResult createGameSessionResult, java.lang.Throwable exception) {
        if (exception != null || createGameSessionResult == null) {
            android.util.Slog.w(TAG, "Failed to create GameSession: " + existingGameSessionRecord, exception);
            synchronized (this.mLock) {
                removeAndDestroyGameSessionIfNecessaryLocked(taskId);
            }
            return;
        }
        synchronized (this.mLock) {
            attachGameSessionLocked(taskId, createGameSessionResult);
        }
        setGameSessionFocusedIfNecessary(taskId, createGameSessionResult.getGameSession());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGameSessionLocked$3(int taskId, com.android.server.app.GameSessionRecord existingGameSessionRecord, android.service.games.GameSessionViewHostConfiguration gameSessionViewHostConfiguration, com.android.internal.infra.AndroidFuture createGameSessionResultFuture, android.service.games.IGameSessionService gameSessionService) throws java.lang.Exception {
        android.service.games.CreateGameSessionRequest createGameSessionRequest = new android.service.games.CreateGameSessionRequest(taskId, existingGameSessionRecord.getComponentName().getPackageName());
        gameSessionService.create(this.mGameSessionController, createGameSessionRequest, gameSessionViewHostConfiguration, createGameSessionResultFuture);
    }

    private void setGameSessionFocusedIfNecessary(int taskId, android.service.games.IGameSession gameSession) {
        try {
            android.app.ActivityTaskManager.RootTaskInfo rootTaskInfo = this.mActivityTaskManager.getFocusedRootTaskInfo();
            if (rootTaskInfo != null && rootTaskInfo.taskId == taskId) {
                gameSession.onTaskFocusChanged(true);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to set task focused for ID: " + taskId);
        }
    }

    private void attachGameSessionLocked(int taskId, android.service.games.CreateGameSessionResult createGameSessionResult) {
        com.android.server.app.GameSessionRecord gameSessionRecord = this.mGameSessions.get(java.lang.Integer.valueOf(taskId));
        if (gameSessionRecord == null) {
            android.util.Slog.w(TAG, "No associated game session record. Destroying id: " + taskId);
            destroyGameSessionDuringAttach(taskId, createGameSessionResult);
        } else {
            if (!gameSessionRecord.isGameSessionRequested()) {
                destroyGameSessionDuringAttach(taskId, createGameSessionResult);
                return;
            }
            try {
                this.mWindowManagerInternal.addTrustedTaskOverlay(taskId, createGameSessionResult.getSurfacePackage());
                this.mGameSessions.put(java.lang.Integer.valueOf(taskId), gameSessionRecord.withGameSession(createGameSessionResult.getGameSession(), createGameSessionResult.getSurfacePackage()));
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.w(TAG, "Failed to add task overlay. Destroying id: " + taskId);
                destroyGameSessionDuringAttach(taskId, createGameSessionResult);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyAndClearAllGameSessionsLocked() {
        for (com.android.server.app.GameSessionRecord gameSessionRecord : this.mGameSessions.values()) {
            destroyGameSessionFromRecordLocked(gameSessionRecord);
        }
        this.mGameSessions.clear();
    }

    private void destroyGameSessionDuringAttach(int taskId, android.service.games.CreateGameSessionResult createGameSessionResult) {
        try {
            createGameSessionResult.getGameSession().onDestroyed();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to destroy session: " + taskId);
        }
    }

    private void removeAndDestroyGameSessionIfNecessaryLocked(int taskId) {
        com.android.server.app.GameSessionRecord gameSessionRecord = this.mGameSessions.remove(java.lang.Integer.valueOf(taskId));
        if (gameSessionRecord == null) {
            return;
        }
        destroyGameSessionFromRecordLocked(gameSessionRecord);
    }

    private void destroyGameSessionFromRecordLocked(com.android.server.app.GameSessionRecord gameSessionRecord) {
        android.view.SurfaceControlViewHost.SurfacePackage surfacePackage = gameSessionRecord.getSurfacePackage();
        if (surfacePackage != null) {
            try {
                this.mWindowManagerInternal.removeTrustedTaskOverlay(gameSessionRecord.getTaskId(), surfacePackage);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.i(TAG, "Failed to remove task overlay. This is expected if the task is already destroyed: " + gameSessionRecord);
            }
        }
        android.service.games.IGameSession gameSession = gameSessionRecord.getGameSession();
        if (gameSession != null) {
            try {
                gameSession.onDestroyed();
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(TAG, "Failed to destroy session: " + gameSessionRecord, ex);
            }
        }
        if (this.mGameSessions.isEmpty()) {
            this.mGameSessionServiceConnector.unbind();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onForegroundActivitiesChanged(int pid) {
        synchronized (this.mLock) {
            onForegroundActivitiesChangedLocked(pid);
        }
    }

    private void onForegroundActivitiesChangedLocked(int pid) {
        if (this.mPidToPackageMap.containsKey(java.lang.Integer.valueOf(pid))) {
            return;
        }
        java.lang.String packageName = this.mActivityManagerInternal.getPackageNameByPid(pid);
        if (android.text.TextUtils.isEmpty(packageName) || !gameSessionExistsForPackageNameLocked(packageName)) {
            return;
        }
        this.mPidToPackageMap.put(java.lang.Integer.valueOf(pid), packageName);
        int processCountForPackage = this.mPackageNameToProcessCountMap.getOrDefault(packageName, 0).intValue() + 1;
        this.mPackageNameToProcessCountMap.put(packageName, java.lang.Integer.valueOf(processCountForPackage));
        if (processCountForPackage > 0) {
            recreateEndedGameSessionsLocked(packageName);
        }
    }

    private void recreateEndedGameSessionsLocked(java.lang.String packageName) {
        for (com.android.server.app.GameSessionRecord gameSessionRecord : this.mGameSessions.values()) {
            if (gameSessionRecord.isGameSessionEndedForProcessDeath() && packageName.equals(gameSessionRecord.getComponentName().getPackageName())) {
                int taskId = gameSessionRecord.getTaskId();
                this.mGameSessions.put(java.lang.Integer.valueOf(taskId), com.android.server.app.GameSessionRecord.awaitingGameSessionRequest(taskId, gameSessionRecord.getComponentName()));
                createGameSessionLocked(gameSessionRecord.getTaskId());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProcessDied(int pid) {
        synchronized (this.mLock) {
            onProcessDiedLocked(pid);
        }
    }

    private void onProcessDiedLocked(int pid) {
        java.lang.String packageName = this.mPidToPackageMap.remove(java.lang.Integer.valueOf(pid));
        if (packageName == null) {
            return;
        }
        java.lang.Integer oldProcessCountForPackage = this.mPackageNameToProcessCountMap.get(packageName);
        if (oldProcessCountForPackage == null) {
            android.util.Slog.w(TAG, "onProcessDiedLocked(): Missing process count for package");
            return;
        }
        int processCountForPackage = oldProcessCountForPackage.intValue() - 1;
        this.mPackageNameToProcessCountMap.put(packageName, java.lang.Integer.valueOf(processCountForPackage));
        if (processCountForPackage <= 0) {
            endGameSessionsForPackageLocked(packageName);
        }
    }

    private void endGameSessionsForPackageLocked(java.lang.String packageName) {
        android.app.ActivityManager.RunningTaskInfo runningTaskInfo;
        for (com.android.server.app.GameSessionRecord gameSessionRecord : this.mGameSessions.values()) {
            if (gameSessionRecord.getGameSession() != null && packageName.equals(gameSessionRecord.getComponentName().getPackageName()) && ((runningTaskInfo = this.mGameTaskInfoProvider.getRunningTaskInfo(gameSessionRecord.getTaskId())) == null || !runningTaskInfo.isVisible)) {
                this.mGameSessions.put(java.lang.Integer.valueOf(gameSessionRecord.getTaskId()), gameSessionRecord.withGameSessionEndedOnProcessDeath());
                destroyGameSessionFromRecordLocked(gameSessionRecord);
            }
        }
    }

    private boolean gameSessionExistsForPackageNameLocked(java.lang.String packageName) {
        for (com.android.server.app.GameSessionRecord gameSessionRecord : this.mGameSessions.values()) {
            if (packageName.equals(gameSessionRecord.getComponentName().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    private android.service.games.GameSessionViewHostConfiguration createViewHostConfigurationForTask(int taskId) {
        android.app.ActivityManager.RunningTaskInfo runningTaskInfo = this.mGameTaskInfoProvider.getRunningTaskInfo(taskId);
        if (runningTaskInfo == null) {
            return null;
        }
        android.graphics.Rect bounds = runningTaskInfo.configuration.windowConfiguration.getBounds();
        return new android.service.games.GameSessionViewHostConfiguration(runningTaskInfo.displayId, bounds.width(), bounds.height());
    }

    void takeScreenshot(final int taskId, final com.android.internal.infra.AndroidFuture callback) {
        synchronized (this.mLock) {
            final com.android.server.app.GameSessionRecord gameSessionRecord = this.mGameSessions.get(java.lang.Integer.valueOf(taskId));
            if (gameSessionRecord == null) {
                android.util.Slog.w(TAG, "No game session found for id: " + taskId);
                callback.complete(android.service.games.GameScreenshotResult.createInternalErrorResult());
            } else {
                android.view.SurfaceControlViewHost.SurfacePackage overlaySurfacePackage = gameSessionRecord.getSurfacePackage();
                final android.view.SurfaceControl overlaySurfaceControl = overlaySurfacePackage != null ? overlaySurfacePackage.getSurfaceControl() : null;
                this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$takeScreenshot$5(overlaySurfaceControl, taskId, callback, gameSessionRecord);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$takeScreenshot$5(android.view.SurfaceControl overlaySurfaceControl, int taskId, final com.android.internal.infra.AndroidFuture callback, com.android.server.app.GameSessionRecord gameSessionRecord) {
        android.window.ScreenCapture.LayerCaptureArgs.Builder layerCaptureArgsBuilder = new android.window.ScreenCapture.LayerCaptureArgs.Builder((android.view.SurfaceControl) null);
        if (overlaySurfaceControl != null) {
            android.view.SurfaceControl[] excludeLayers = {overlaySurfaceControl};
            layerCaptureArgsBuilder.setExcludeLayers(excludeLayers);
        }
        android.graphics.Bitmap bitmap = this.mWindowManagerService.captureTaskBitmap(taskId, layerCaptureArgsBuilder);
        if (bitmap == null) {
            android.util.Slog.w(TAG, "Could not get bitmap for id: " + taskId);
            callback.complete(android.service.games.GameScreenshotResult.createInternalErrorResult());
            return;
        }
        android.app.ActivityManager.RunningTaskInfo runningTaskInfo = this.mGameTaskInfoProvider.getRunningTaskInfo(taskId);
        if (runningTaskInfo == null) {
            android.util.Slog.w(TAG, "Could not get running task info for id: " + taskId);
            callback.complete(android.service.games.GameScreenshotResult.createInternalErrorResult());
        }
        android.graphics.Rect crop = runningTaskInfo.configuration.windowConfiguration.getBounds();
        java.util.function.Consumer<android.net.Uri> completionConsumer = new java.util.function.Consumer() { // from class: com.android.server.app.GameServiceProviderInstanceImpl$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.app.GameServiceProviderInstanceImpl.lambda$takeScreenshot$4(callback, (android.net.Uri) obj);
            }
        };
        com.android.internal.util.ScreenshotRequest request = new com.android.internal.util.ScreenshotRequest.Builder(3, 5).setTopComponent(gameSessionRecord.getComponentName()).setTaskId(taskId).setUserId(this.mUserHandle.getIdentifier()).setBitmap(bitmap).setBoundsOnScreen(crop).setInsets(android.graphics.Insets.NONE).build();
        this.mScreenshotHelper.takeScreenshot(request, com.android.internal.os.BackgroundThread.getHandler(), completionConsumer);
    }

    static /* synthetic */ void lambda$takeScreenshot$4(com.android.internal.infra.AndroidFuture callback, android.net.Uri uri) {
        if (uri == null) {
            callback.complete(android.service.games.GameScreenshotResult.createInternalErrorResult());
        } else {
            callback.complete(android.service.games.GameScreenshotResult.createSuccessResult());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restartGame(int taskId) {
        synchronized (this.mLock) {
            com.android.server.app.GameSessionRecord gameSessionRecord = this.mGameSessions.get(java.lang.Integer.valueOf(taskId));
            if (gameSessionRecord == null) {
                return;
            }
            java.lang.String packageName = gameSessionRecord.getComponentName().getPackageName();
            if (packageName == null) {
                return;
            }
            this.mActivityTaskManagerInternal.restartTaskActivityProcessIfVisible(taskId, packageName);
        }
    }
}
