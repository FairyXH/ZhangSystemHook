package com.android.server.grammaticalinflection;

/* JADX INFO: loaded from: classes2.dex */
public class GrammaticalInflectionUtils {
    private static final java.lang.String TAG = "GrammaticalInflectionUtils";

    public static boolean checkSystemGrammaticalGenderPermission(android.permission.PermissionManager permissionManager, android.content.AttributionSource attributionSource) {
        int permissionCheckResult = permissionManager.checkPermissionForDataDelivery("android.permission.READ_SYSTEM_GRAMMATICAL_GENDER", attributionSource, (java.lang.String) null);
        if (permissionCheckResult != 0) {
            android.util.Log.v(TAG, "AttributionSource: " + attributionSource + " does not have READ_SYSTEM_GRAMMATICAL_GENDER permission.");
            return false;
        }
        return true;
    }
}
