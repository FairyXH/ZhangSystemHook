package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public final class SELinuxUtil {
    public static final java.lang.String COMPLETE_STR = ":complete";
    private static final java.lang.String INSTANT_APP_STR = ":ephemeralapp";

    public static java.lang.String getSeinfoUser(com.android.server.pm.pkg.PackageUserState userState) {
        if (userState.isInstantApp()) {
            return ":ephemeralapp:complete";
        }
        return COMPLETE_STR;
    }
}
