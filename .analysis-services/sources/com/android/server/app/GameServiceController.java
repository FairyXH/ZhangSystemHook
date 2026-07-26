package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
final class GameServiceController {
    private static final java.lang.String TAG = "GameServiceController";
    private volatile com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration mActiveGameServiceComponentConfiguration;
    private volatile java.lang.String mActiveGameServiceProviderPackage;
    private final java.util.concurrent.Executor mBackgroundExecutor;
    private final android.content.Context mContext;
    private volatile com.android.server.SystemService.TargetUser mCurrentForegroundUser;
    private android.content.BroadcastReceiver mGameServicePackageChangedReceiver;
    private volatile com.android.server.app.GameServiceProviderInstance mGameServiceProviderInstance;
    private final com.android.server.app.GameServiceProviderInstanceFactory mGameServiceProviderInstanceFactory;
    private volatile java.lang.String mGameServiceProviderOverride;
    private final com.android.server.app.GameServiceProviderSelector mGameServiceProviderSelector;
    private volatile boolean mHasBootCompleted;
    private final java.lang.Object mLock = new java.lang.Object();

    GameServiceController(android.content.Context context, java.util.concurrent.Executor backgroundExecutor, com.android.server.app.GameServiceProviderSelector gameServiceProviderSelector, com.android.server.app.GameServiceProviderInstanceFactory gameServiceProviderInstanceFactory) {
        this.mContext = context;
        this.mGameServiceProviderInstanceFactory = gameServiceProviderInstanceFactory;
        this.mBackgroundExecutor = backgroundExecutor;
        this.mGameServiceProviderSelector = gameServiceProviderSelector;
    }

    void onBootComplete() {
        if (this.mHasBootCompleted) {
            return;
        }
        this.mHasBootCompleted = true;
        this.mBackgroundExecutor.execute(new com.android.server.app.GameServiceController$$ExternalSyntheticLambda0(this));
    }

    void notifyUserStarted(com.android.server.SystemService.TargetUser user) {
        if (this.mCurrentForegroundUser != null) {
            return;
        }
        setCurrentForegroundUserAndEvaluateProvider(user);
    }

    void notifyNewForegroundUser(com.android.server.SystemService.TargetUser user) {
        setCurrentForegroundUserAndEvaluateProvider(user);
    }

    void notifyUserUnlocking(com.android.server.SystemService.TargetUser user) {
        boolean isSameAsForegroundUser = this.mCurrentForegroundUser != null && this.mCurrentForegroundUser.getUserIdentifier() == user.getUserIdentifier();
        if (!isSameAsForegroundUser) {
            return;
        }
        this.mBackgroundExecutor.execute(new com.android.server.app.GameServiceController$$ExternalSyntheticLambda0(this));
    }

    void notifyUserStopped(com.android.server.SystemService.TargetUser user) {
        boolean isSameAsForegroundUser = this.mCurrentForegroundUser != null && this.mCurrentForegroundUser.getUserIdentifier() == user.getUserIdentifier();
        if (!isSameAsForegroundUser) {
            return;
        }
        setCurrentForegroundUserAndEvaluateProvider(null);
    }

    void setGameServiceProvider(java.lang.String packageName) {
        boolean hasPackageChanged = !java.util.Objects.equals(this.mGameServiceProviderOverride, packageName);
        if (!hasPackageChanged) {
            return;
        }
        this.mGameServiceProviderOverride = packageName;
        this.mBackgroundExecutor.execute(new com.android.server.app.GameServiceController$$ExternalSyntheticLambda0(this));
    }

    private void setCurrentForegroundUserAndEvaluateProvider(com.android.server.SystemService.TargetUser user) {
        boolean hasUserChanged = !java.util.Objects.equals(this.mCurrentForegroundUser, user);
        if (!hasUserChanged) {
            return;
        }
        this.mCurrentForegroundUser = user;
        this.mBackgroundExecutor.execute(new com.android.server.app.GameServiceController$$ExternalSyntheticLambda0(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateActiveGameServiceProvider() {
        java.lang.String gameServicePackage;
        com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration gameServiceComponentConfiguration;
        if (!this.mHasBootCompleted) {
            return;
        }
        synchronized (this.mLock) {
            com.android.server.app.GameServiceConfiguration selectedGameServiceConfiguration = this.mGameServiceProviderSelector.get(this.mCurrentForegroundUser, this.mGameServiceProviderOverride);
            if (selectedGameServiceConfiguration == null) {
                gameServicePackage = null;
            } else {
                gameServicePackage = selectedGameServiceConfiguration.getPackageName();
            }
            if (selectedGameServiceConfiguration == null) {
                gameServiceComponentConfiguration = null;
            } else {
                gameServiceComponentConfiguration = selectedGameServiceConfiguration.getGameServiceComponentConfiguration();
            }
            evaluateGameServiceProviderPackageChangedListenerLocked(gameServicePackage);
            boolean didActiveGameServiceProviderChange = !java.util.Objects.equals(gameServiceComponentConfiguration, this.mActiveGameServiceComponentConfiguration);
            if (didActiveGameServiceProviderChange) {
                if (this.mGameServiceProviderInstance != null) {
                    android.util.Slog.i(TAG, "Stopping Game Service provider: " + this.mActiveGameServiceComponentConfiguration);
                    this.mGameServiceProviderInstance.stop();
                    this.mGameServiceProviderInstance = null;
                }
                this.mActiveGameServiceComponentConfiguration = gameServiceComponentConfiguration;
                if (this.mActiveGameServiceComponentConfiguration == null) {
                    return;
                }
                android.util.Slog.i(TAG, "Starting Game Service provider: " + this.mActiveGameServiceComponentConfiguration);
                this.mGameServiceProviderInstance = this.mGameServiceProviderInstanceFactory.create(this.mActiveGameServiceComponentConfiguration);
                this.mGameServiceProviderInstance.start();
            }
        }
    }

    private void evaluateGameServiceProviderPackageChangedListenerLocked(java.lang.String gameServicePackage) {
        if (android.text.TextUtils.equals(this.mActiveGameServiceProviderPackage, gameServicePackage)) {
            return;
        }
        if (this.mGameServicePackageChangedReceiver != null) {
            this.mContext.unregisterReceiver(this.mGameServicePackageChangedReceiver);
            this.mGameServicePackageChangedReceiver = null;
        }
        this.mActiveGameServiceProviderPackage = gameServicePackage;
        if (android.text.TextUtils.isEmpty(this.mActiveGameServiceProviderPackage)) {
            return;
        }
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart(gameServicePackage, 0);
        this.mGameServicePackageChangedReceiver = new com.android.server.app.GameServiceController.PackageChangedBroadcastReceiver(gameServicePackage);
        this.mContext.registerReceiver(this.mGameServicePackageChangedReceiver, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class PackageChangedBroadcastReceiver extends android.content.BroadcastReceiver {
        private final java.lang.String mPackageName;

        PackageChangedBroadcastReceiver(java.lang.String packageName) {
            this.mPackageName = packageName;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (!android.text.TextUtils.equals(intent.getData().getSchemeSpecificPart(), this.mPackageName)) {
                return;
            }
            java.util.concurrent.Executor executor = com.android.server.app.GameServiceController.this.mBackgroundExecutor;
            final com.android.server.app.GameServiceController gameServiceController = com.android.server.app.GameServiceController.this;
            executor.execute(new java.lang.Runnable() { // from class: com.android.server.app.GameServiceController$PackageChangedBroadcastReceiver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    gameServiceController.evaluateActiveGameServiceProvider();
                }
            });
        }
    }
}
