package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class ImePlatformCompatUtils {
    private final com.android.internal.compat.IPlatformCompat mPlatformCompat = com.android.internal.compat.IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService("platform_compat"));

    ImePlatformCompatUtils() {
    }

    public boolean shouldUseSetInteractiveProtocol(int imeUid) {
        return isChangeEnabledByUid(156215187L, imeUid);
    }

    public boolean shouldClearShowForcedFlag(int clientUid) {
        return isChangeEnabledByUid(214016041L, clientUid);
    }

    private boolean isChangeEnabledByUid(long changeFlag, int uid) {
        java.lang.String flagString;
        boolean result = false;
        try {
            result = this.mPlatformCompat.isChangeEnabledByUid(changeFlag, uid);
            if (com.android.server.inputmethod.InputMethodManagerService.DEBUG) {
                if (changeFlag == 214016041) {
                    flagString = "CLEAR_SHOW_FORCED_FLAG_WHEN_LEAVING";
                } else if (changeFlag == 156215187) {
                    flagString = "FINISH_INPUT_NO_FALLBACK_CONNECTION";
                } else {
                    flagString = java.lang.String.valueOf(changeFlag);
                }
                android.util.Slog.d("ImePlatformCompatUtils", "isChangeEnabledByUid: changeFlag = " + flagString + ", uid = " + uid + ", result = " + result);
            }
        } catch (android.os.RemoteException e) {
        }
        return result;
    }
}
