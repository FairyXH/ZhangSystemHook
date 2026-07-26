package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class BugReportHandlerUtil {
    private static final java.lang.String INTENT_BUGREPORT_REQUESTED = "com.android.internal.intent.action.BUGREPORT_REQUESTED";
    private static final java.lang.String INTENT_GET_BUGREPORT_HANDLER_RESPONSE = "com.android.internal.intent.action.GET_BUGREPORT_HANDLER_RESPONSE";
    private static final java.lang.String SHELL_APP_PACKAGE = "com.android.shell";
    private static final java.lang.String TAG = "ActivityManager";

    static boolean isBugReportHandlerEnabled(android.content.Context context) {
        return context.getResources().getBoolean(android.R.bool.config_buttonTextAllCaps);
    }

    static boolean launchBugReportHandlerApp(android.content.Context userContext) {
        java.lang.String handlerApp;
        int handlerUser;
        java.lang.String str;
        if (!isBugReportHandlerEnabled(userContext)) {
            return false;
        }
        java.lang.String handlerApp2 = getCustomBugReportHandlerApp(userContext);
        if (isShellApp(handlerApp2)) {
            return false;
        }
        int handlerUser2 = getCustomBugReportHandlerUser(userContext);
        if (!isValidBugReportHandlerApp(handlerApp2)) {
            handlerApp = getDefaultBugReportHandlerApp(userContext);
            handlerUser = userContext.getUserId();
        } else if (getBugReportHandlerAppReceivers(userContext, handlerApp2, handlerUser2).isEmpty()) {
            java.lang.String handlerApp3 = getDefaultBugReportHandlerApp(userContext);
            int handlerUser3 = userContext.getUserId();
            resetCustomBugreportHandlerAppAndUser(userContext);
            handlerApp = handlerApp3;
            handlerUser = handlerUser3;
        } else {
            handlerApp = handlerApp2;
            handlerUser = handlerUser2;
        }
        if (isShellApp(handlerApp) || !isValidBugReportHandlerApp(handlerApp) || getBugReportHandlerAppReceivers(userContext, handlerApp, handlerUser).isEmpty()) {
            return false;
        }
        if (getBugReportHandlerAppResponseReceivers(userContext, handlerApp, handlerUser).isEmpty()) {
            launchBugReportHandlerApp(userContext, handlerApp, handlerUser);
            return true;
        }
        android.util.Slog.i("ActivityManager", "Getting response from bug report handler app: " + handlerApp);
        android.content.Intent intent = new android.content.Intent(INTENT_GET_BUGREPORT_HANDLER_RESPONSE);
        intent.setPackage(handlerApp);
        intent.addFlags(268435456);
        intent.addFlags(16777216);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            str = "ActivityManager";
            try {
                try {
                    userContext.sendOrderedBroadcastAsUser(intent, android.os.UserHandle.of(handlerUser), "android.permission.DUMP", -1, null, new com.android.server.am.BugReportHandlerUtil.BugreportHandlerResponseBroadcastReceiver(handlerApp, handlerUser), null, 0, null, null);
                    android.os.Binder.restoreCallingIdentity(identity);
                    return true;
                } catch (java.lang.RuntimeException e) {
                    e = e;
                    android.util.Slog.e(str, "Error while trying to get response from bug report handler app.", e);
                    android.os.Binder.restoreCallingIdentity(identity);
                    return false;
                }
            } catch (java.lang.Throwable th) {
                e = th;
                android.os.Binder.restoreCallingIdentity(identity);
                throw e;
            }
        } catch (java.lang.RuntimeException e2) {
            e = e2;
            str = "ActivityManager";
        } catch (java.lang.Throwable th2) {
            e = th2;
            android.os.Binder.restoreCallingIdentity(identity);
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void launchBugReportHandlerApp(android.content.Context context, java.lang.String handlerApp, int handlerUser) {
        android.util.Slog.i("ActivityManager", "Launching bug report handler app: " + handlerApp);
        android.content.Intent intent = new android.content.Intent(INTENT_BUGREPORT_REQUESTED);
        intent.setPackage(handlerApp);
        intent.addFlags(268435456);
        intent.addFlags(16777216);
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setBackgroundActivityStartsAllowed(true);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                context.sendBroadcastAsUser(intent, android.os.UserHandle.of(handlerUser), "android.permission.DUMP", options.toBundle());
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.e("ActivityManager", "Error while trying to launch bugreport handler app.", e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private static java.lang.String getCustomBugReportHandlerApp(android.content.Context context) {
        return android.provider.Settings.Secure.getStringForUser(context.getContentResolver(), "custom_bugreport_handler_app", context.getUserId());
    }

    private static int getCustomBugReportHandlerUser(android.content.Context context) {
        return android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "custom_bugreport_handler_user", -10000, context.getUserId());
    }

    private static boolean isShellApp(java.lang.String app) {
        return "com.android.shell".equals(app);
    }

    private static boolean isValidBugReportHandlerApp(java.lang.String app) {
        return !android.text.TextUtils.isEmpty(app) && isBugreportWhitelistedApp(app);
    }

    private static boolean isBugreportWhitelistedApp(java.lang.String app) {
        android.util.ArraySet<java.lang.String> whitelistedApps = com.android.server.SystemConfig.getInstance().getBugreportWhitelistedPackages();
        return whitelistedApps.contains(app);
    }

    private static java.util.List<android.content.pm.ResolveInfo> getBugReportHandlerAppReceivers(android.content.Context context, java.lang.String handlerApp, int handlerUser) {
        android.content.Intent intent = new android.content.Intent(INTENT_BUGREPORT_REQUESTED);
        intent.setPackage(handlerApp);
        return context.getPackageManager().queryBroadcastReceiversAsUser(intent, 1048576, handlerUser);
    }

    private static java.util.List<android.content.pm.ResolveInfo> getBugReportHandlerAppResponseReceivers(android.content.Context context, java.lang.String handlerApp, int handlerUser) {
        android.content.Intent intent = new android.content.Intent(INTENT_GET_BUGREPORT_HANDLER_RESPONSE);
        intent.setPackage(handlerApp);
        return context.getPackageManager().queryBroadcastReceiversAsUser(intent, 1048576, handlerUser);
    }

    private static java.lang.String getDefaultBugReportHandlerApp(android.content.Context context) {
        return context.getResources().getString(android.R.string.config_defaultCredentialManagerHybridService);
    }

    private static void resetCustomBugreportHandlerAppAndUser(android.content.Context context) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Secure.putString(context.getContentResolver(), "custom_bugreport_handler_app", getDefaultBugReportHandlerApp(context));
            android.provider.Settings.Secure.putInt(context.getContentResolver(), "custom_bugreport_handler_user", context.getUserId());
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private static class BugreportHandlerResponseBroadcastReceiver extends android.content.BroadcastReceiver {
        private final java.lang.String handlerApp;
        private final int handlerUser;

        BugreportHandlerResponseBroadcastReceiver(java.lang.String handlerApp, int handlerUser) {
            this.handlerApp = handlerApp;
            this.handlerUser = handlerUser;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (getResultCode() == -1) {
                com.android.server.am.BugReportHandlerUtil.launchBugReportHandlerApp(context, this.handlerApp, this.handlerUser);
                return;
            }
            android.util.Slog.w("ActivityManager", "Request bug report because no response from handler app.");
            android.os.BugreportManager bugreportManager = (android.os.BugreportManager) context.getSystemService(android.os.BugreportManager.class);
            bugreportManager.requestBugreport(new android.os.BugreportParams(1), null, null);
        }
    }
}
