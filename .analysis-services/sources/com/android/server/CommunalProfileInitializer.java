package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class CommunalProfileInitializer {
    private static final java.lang.String TAG = com.android.server.CommunalProfileInitializer.class.getSimpleName();
    private final com.android.server.am.ActivityManagerService mAms;
    private com.android.server.pm.UserManagerInternal mUmi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);

    public CommunalProfileInitializer(com.android.server.am.ActivityManagerService ams) {
        this.mAms = ams;
    }

    public void init(com.android.server.utils.TimingsTraceAndSlog t) {
        com.android.server.utils.Slogf.i(TAG, "init())");
        t.traceBegin("createCommunalProfileIfNeeded");
        createCommunalProfileIfNeeded();
        t.traceEnd();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    private void createCommunalProfileIfNeeded() {
        int communalProfile = this.mUmi.getCommunalProfileId();
        if (communalProfile != -10000) {
            com.android.server.utils.Slogf.d(TAG, "Found existing Communal Profile, userId=%d", java.lang.Integer.valueOf(communalProfile));
            return;
        }
        com.android.server.utils.Slogf.d(TAG, "Creating a new Communal Profile");
        try {
            android.content.pm.UserInfo newProfile = this.mUmi.createUserEvenWhenDisallowed(null, "android.os.usertype.profile.COMMUNAL", 0, null, null);
            com.android.server.utils.Slogf.i(TAG, "Successfully created Communal Profile, userId=%d", java.lang.Integer.valueOf(newProfile.id));
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            com.android.server.utils.Slogf.wtf(TAG, "Communal Profile creation failed", (java.lang.Throwable) e);
        }
    }

    static void removeCommunalProfileIfPresent() {
        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        int communalProfile = umi.getCommunalProfileId();
        if (communalProfile == -10000) {
            return;
        }
        com.android.server.utils.Slogf.d(TAG, "Removing existing Communal Profile, userId=%d", java.lang.Integer.valueOf(communalProfile));
        boolean removeSucceeded = umi.removeUserEvenWhenDisallowed(communalProfile);
        if (!removeSucceeded) {
            com.android.server.utils.Slogf.e(TAG, "Failed to remove Communal Profile, userId=%d", java.lang.Integer.valueOf(communalProfile));
        }
    }
}
