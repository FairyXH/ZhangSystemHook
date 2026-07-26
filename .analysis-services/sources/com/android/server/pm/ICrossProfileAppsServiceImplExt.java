package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface ICrossProfileAppsServiceImplExt {
    default boolean skipProfileInGetTargetUserProfilesUnchecked(int profileId, java.lang.String packageName) {
        return false;
    }

    default boolean interceptInSetInteractAcrossProfilesAppOpForProfileOrThrow(int profileId, java.lang.String packageName) {
        return false;
    }
}
