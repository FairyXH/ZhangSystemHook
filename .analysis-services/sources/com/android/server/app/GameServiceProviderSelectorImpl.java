package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
final class GameServiceProviderSelectorImpl implements com.android.server.app.GameServiceProviderSelector {
    private static final boolean DEBUG = false;
    private static final java.lang.String GAME_SERVICE_NODE_NAME = "game-service";
    private static final java.lang.String TAG = "GameServiceProviderSelector";
    private final android.content.pm.PackageManager mPackageManager;
    private final android.content.res.Resources mResources;

    GameServiceProviderSelectorImpl(android.content.res.Resources resources, android.content.pm.PackageManager packageManager) {
        this.mResources = resources;
        this.mPackageManager = packageManager;
    }

    @Override // com.android.server.app.GameServiceProviderSelector
    public com.android.server.app.GameServiceConfiguration get(com.android.server.SystemService.TargetUser user, java.lang.String packageNameOverride) {
        int resolveInfoQueryFlags;
        java.lang.String gameServicePackage;
        android.content.pm.ServiceInfo gameServiceServiceInfo;
        android.content.ComponentName gameSessionServiceComponentName;
        if (user == null) {
            return null;
        }
        boolean isUserSupported = user.isFull() && !user.isManagedProfile();
        if (!isUserSupported) {
            android.util.Slog.i(TAG, "Game Service not supported for user: " + user.getUserIdentifier());
            return null;
        }
        if (!android.text.TextUtils.isEmpty(packageNameOverride)) {
            resolveInfoQueryFlags = 0;
            gameServicePackage = packageNameOverride;
        } else {
            resolveInfoQueryFlags = 1048576;
            gameServicePackage = this.mResources.getString(android.R.string.config_tcp_buffers);
        }
        if (android.text.TextUtils.isEmpty(gameServicePackage)) {
            android.util.Slog.w(TAG, "No game service package defined");
            return null;
        }
        int userId = user.getUserIdentifier();
        java.util.List<android.content.pm.ResolveInfo> gameServiceResolveInfos = this.mPackageManager.queryIntentServicesAsUser(new android.content.Intent("android.service.games.action.GAME_SERVICE").setPackage(gameServicePackage), resolveInfoQueryFlags | 128, userId);
        if (gameServiceResolveInfos == null || gameServiceResolveInfos.isEmpty()) {
            android.util.Slog.w(TAG, "No available game service found for user id: " + userId);
            return new com.android.server.app.GameServiceConfiguration(gameServicePackage, null);
        }
        com.android.server.app.GameServiceConfiguration selectedProvider = null;
        java.util.Iterator<android.content.pm.ResolveInfo> it = gameServiceResolveInfos.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.content.pm.ResolveInfo resolveInfo = it.next();
            if (resolveInfo.serviceInfo != null && (gameSessionServiceComponentName = determineGameSessionServiceFromGameService((gameServiceServiceInfo = resolveInfo.serviceInfo))) != null) {
                selectedProvider = new com.android.server.app.GameServiceConfiguration(gameServicePackage, new com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration(new android.os.UserHandle(userId), gameServiceServiceInfo.getComponentName(), gameSessionServiceComponentName));
                break;
            }
        }
        if (selectedProvider == null) {
            android.util.Slog.w(TAG, "No valid game service found for user id: " + userId);
            return new com.android.server.app.GameServiceConfiguration(gameServicePackage, null);
        }
        return selectedProvider;
    }

    private android.content.ComponentName determineGameSessionServiceFromGameService(android.content.pm.ServiceInfo gameServiceServiceInfo) {
        int type;
        try {
            android.content.res.XmlResourceParser parser = gameServiceServiceInfo.loadXmlMetaData(this.mPackageManager, "android.game_service");
            try {
                if (parser == null) {
                    android.util.Slog.w(TAG, "No android.game_service meta-data found for " + gameServiceServiceInfo.getComponentName());
                    if (parser != null) {
                        parser.close();
                    }
                    return null;
                }
                android.content.res.Resources resources = this.mPackageManager.getResourcesForApplication(gameServiceServiceInfo.packageName);
                android.util.AttributeSet attributeSet = android.util.Xml.asAttributeSet(parser);
                do {
                    type = parser.next();
                    if (type == 1) {
                        break;
                    }
                } while (type != 2);
                boolean isStartingTagGameService = GAME_SERVICE_NODE_NAME.equals(parser.getName());
                if (!isStartingTagGameService) {
                    android.util.Slog.w(TAG, "Meta-data does not start with game-service tag");
                    if (parser != null) {
                        parser.close();
                    }
                    return null;
                }
                android.content.res.TypedArray array = resources.obtainAttributes(attributeSet, com.android.internal.R.styleable.GameService);
                java.lang.String gameSessionService = array.getString(0);
                array.recycle();
                if (parser != null) {
                    parser.close();
                }
                if (android.text.TextUtils.isEmpty(gameSessionService)) {
                    android.util.Slog.w(TAG, "No gameSessionService specified");
                    return null;
                }
                android.content.ComponentName componentName = new android.content.ComponentName(gameServiceServiceInfo.packageName, gameSessionService);
                try {
                    this.mPackageManager.getServiceInfo(componentName, 0);
                    return componentName;
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Slog.w(TAG, "GameSessionService does not exist: " + componentName);
                    return null;
                }
            } catch (java.lang.Throwable th) {
                if (parser != null) {
                    try {
                        parser.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException | java.io.IOException | org.xmlpull.v1.XmlPullParserException ex) {
            android.util.Slog.w("Error while parsing meta-data for " + gameServiceServiceInfo.getComponentName(), ex);
            return null;
        }
    }
}
