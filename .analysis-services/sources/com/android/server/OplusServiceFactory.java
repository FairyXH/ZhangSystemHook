package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class OplusServiceFactory extends com.android.server.OplusCommonServiceFactory {
    private static final java.lang.String CLASSNAME2 = "com.android.server.OplusServiceFactoryImpl";
    private static final java.lang.String TAG = "OplusServiceFactory";
    private static com.android.server.OplusServiceFactory sInstance;

    public static com.android.server.OplusServiceFactory getInstance() {
        if (sInstance == null) {
            synchronized (com.android.server.OplusServiceFactory.class) {
                try {
                    if (sInstance == null) {
                        sInstance = (com.android.server.OplusServiceFactory) newInstance(CLASSNAME2);
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, "WindowManagerService Reflect exception getInstance: " + e.toString());
                    if (sInstance == null) {
                        sInstance = new com.android.server.OplusServiceFactory();
                    }
                }
            }
        }
        return sInstance;
    }

    public boolean isValid(int index) {
        boolean validOplus = index < android.common.OplusFeatureList.OplusIndex.EndOplusServiceFactory.ordinal() && index > android.common.OplusFeatureList.OplusIndex.StartOplusServiceFactory.ordinal();
        boolean vaildOplusOs = index < android.common.OplusFeatureList.OplusIndex.EndOplusOsServiceFactory.ordinal() && index > android.common.OplusFeatureList.OplusIndex.StartOplusOsServiceFactory.ordinal();
        return vaildOplusOs || validOplus;
    }

    public int getColorSystemThemeEx(int theme) {
        warn("getColorSystemThemeEx dummy");
        return theme;
    }

    public com.android.server.inputmethod.InputMethodManagerService getOplusInputMethodManagerService(android.content.Context context) {
        warn("getInputMethodManagerService");
        return new com.android.server.inputmethod.InputMethodManagerService(context, false);
    }

    public com.android.server.display.IOplusEyeProtectManager getOplusEyeProtectManager() {
        return com.android.server.display.IOplusEyeProtectManager.DEFAULT;
    }

    public com.android.server.display.IOplusDisplayManagerServiceEx getColorDisplayManagerServiceEx(android.content.Context context, com.android.server.display.DisplayManagerService dms) {
        warn("getColorDisplayManagerServiceEx dummy");
        return com.android.server.display.IOplusDisplayManagerServiceEx.DEFAULT;
    }

    public com.android.server.am.IOplusActivityManagerServiceEx getOplusActivityManagerServiceEx(android.content.Context context, com.android.server.am.ActivityManagerService ams) {
        warn("getOplusActivityManagerServiceEx dummy");
        return com.android.server.am.IOplusActivityManagerServiceEx.DEFAULT;
    }
}
