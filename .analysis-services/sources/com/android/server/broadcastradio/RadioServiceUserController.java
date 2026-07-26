package com.android.server.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public final class RadioServiceUserController {
    private RadioServiceUserController() {
        throw new java.lang.UnsupportedOperationException("RadioServiceUserController class is noninstantiable");
    }

    public static boolean isCurrentOrSystemUser() {
        int callingUser = android.os.Binder.getCallingUserHandle().getIdentifier();
        return callingUser == getCurrentUser() || callingUser == 0;
    }

    public static int getCurrentUser() {
        int userId = -10000;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            userId = android.app.ActivityManager.getCurrentUser();
        } catch (java.lang.RuntimeException e) {
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
        android.os.Binder.restoreCallingIdentity(identity);
        return userId;
    }
}
