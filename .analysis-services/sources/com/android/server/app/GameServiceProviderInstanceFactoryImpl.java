package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
final class GameServiceProviderInstanceFactoryImpl implements com.android.server.app.GameServiceProviderInstanceFactory {
    private final android.content.Context mContext;

    GameServiceProviderInstanceFactoryImpl(android.content.Context context) {
        this.mContext = context;
    }

    @Override // com.android.server.app.GameServiceProviderInstanceFactory
    public com.android.server.app.GameServiceProviderInstance create(com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration configuration) {
        android.os.UserHandle userHandle = configuration.getUserHandle();
        android.app.IActivityTaskManager activityTaskManager = android.app.ActivityTaskManager.getService();
        return new com.android.server.app.GameServiceProviderInstanceImpl(userHandle, com.android.internal.os.BackgroundThread.getExecutor(), this.mContext, new com.android.server.app.GameTaskInfoProvider(userHandle, activityTaskManager, new com.android.server.app.GameClassifierImpl(this.mContext.getPackageManager())), android.app.ActivityManager.getService(), (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class), activityTaskManager, (com.android.server.wm.WindowManagerService) android.os.ServiceManager.getService("window"), (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class), (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class), new com.android.server.app.GameServiceProviderInstanceFactoryImpl.GameServiceConnector(this.mContext, configuration), new com.android.server.app.GameServiceProviderInstanceFactoryImpl.GameSessionServiceConnector(this.mContext, configuration), new com.android.internal.util.ScreenshotHelper(this.mContext));
    }

    private static final class GameServiceConnector extends com.android.internal.infra.ServiceConnector.Impl<android.service.games.IGameService> {
        private static final int BINDING_FLAGS = 1048576;
        private static final int DISABLE_AUTOMATIC_DISCONNECT_TIMEOUT = 0;

        GameServiceConnector(android.content.Context context, com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration configuration) {
            super(context, new android.content.Intent("android.service.games.action.GAME_SERVICE").setComponent(configuration.getGameServiceComponentName()), 1048576, configuration.getUserHandle().getIdentifier(), new java.util.function.Function() { // from class: com.android.server.app.GameServiceProviderInstanceFactoryImpl$GameServiceConnector$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.service.games.IGameService.Stub.asInterface((android.os.IBinder) obj);
                }
            });
        }

        protected long getAutoDisconnectTimeoutMs() {
            return 0L;
        }
    }

    private static final class GameSessionServiceConnector extends com.android.internal.infra.ServiceConnector.Impl<android.service.games.IGameSessionService> {
        private static final int BINDING_FLAGS = 135790592;
        private static final int DISABLE_AUTOMATIC_DISCONNECT_TIMEOUT = 0;

        GameSessionServiceConnector(android.content.Context context, com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration configuration) {
            super(context, new android.content.Intent("android.service.games.action.GAME_SESSION_SERVICE").setComponent(configuration.getGameSessionServiceComponentName()), BINDING_FLAGS, configuration.getUserHandle().getIdentifier(), new java.util.function.Function() { // from class: com.android.server.app.GameServiceProviderInstanceFactoryImpl$GameSessionServiceConnector$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.service.games.IGameSessionService.Stub.asInterface((android.os.IBinder) obj);
                }
            });
        }

        protected long getAutoDisconnectTimeoutMs() {
            return 0L;
        }
    }
}
