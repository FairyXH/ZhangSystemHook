package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class Utils {
    private static final java.lang.String TAG = "BiometricUtils";

    public static boolean isDebugEnabled(android.content.Context context, int targetUserId) {
        if (targetUserId == -10000) {
            return false;
        }
        return (android.os.Build.IS_ENG || android.os.Build.IS_USERDEBUG) && android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "biometric_debug_enabled", 0, targetUserId) != 0;
    }

    public static boolean isFingerprintVirtualEnabled(android.content.Context context) {
        if (android.os.Build.isDebuggable()) {
            return android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "biometric_fingerprint_virtual_enabled", 0, -2) == 1 || android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "biometric_virtual_enabled", 0, -2) == 1;
        }
        return false;
    }

    public static boolean isFaceVirtualEnabled(android.content.Context context) {
        if (android.os.Build.isDebuggable()) {
            return android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "biometric_face_virtual_enabled", 0, -2) == 1 || android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "biometric_virtual_enabled", 0, -2) == 1;
        }
        return false;
    }

    static void combineAuthenticatorBundles(android.hardware.biometrics.PromptInfo promptInfo) {
        int authenticators;
        boolean deviceCredentialAllowed = promptInfo.isDeviceCredentialAllowed();
        promptInfo.setDeviceCredentialAllowed(false);
        if (promptInfo.getAuthenticators() != 0) {
            authenticators = promptInfo.getAuthenticators();
        } else if (deviceCredentialAllowed) {
            authenticators = 33023;
        } else {
            authenticators = 255;
        }
        promptInfo.setAuthenticators(authenticators);
    }

    static boolean isCredentialRequested(int authenticators) {
        return (32768 & authenticators) != 0;
    }

    static boolean isCredentialRequested(android.hardware.biometrics.PromptInfo promptInfo) {
        return isCredentialRequested(promptInfo.getAuthenticators());
    }

    static int getPublicBiometricStrength(int authenticators) {
        return authenticators & 255;
    }

    static int getPublicBiometricStrength(android.hardware.biometrics.PromptInfo promptInfo) {
        return getPublicBiometricStrength(promptInfo.getAuthenticators());
    }

    static boolean isBiometricRequested(int authenticators) {
        return getPublicBiometricStrength(authenticators) != 0;
    }

    static boolean isBiometricRequested(android.hardware.biometrics.PromptInfo promptInfo) {
        return getPublicBiometricStrength(promptInfo) != 0;
    }

    public static boolean isAtLeastStrength(int sensorStrength, int requestedStrength) {
        int sensorStrength2 = sensorStrength & 32767;
        if (((~requestedStrength) & sensorStrength2) != 0) {
            return false;
        }
        for (int i = 1; i <= requestedStrength; i = (i << 1) | 1) {
            if (i == sensorStrength2) {
                return true;
            }
        }
        android.util.Slog.e("BiometricService", "Unknown sensorStrength: " + sensorStrength2 + ", requestedStrength: " + requestedStrength);
        return false;
    }

    static boolean isValidAuthenticatorConfig(android.hardware.biometrics.PromptInfo promptInfo) {
        int authenticators = promptInfo.getAuthenticators();
        return isValidAuthenticatorConfig(authenticators);
    }

    static boolean isValidAuthenticatorConfig(int authenticators) {
        if (authenticators == 0) {
            return true;
        }
        if (((-65536) & authenticators) != 0) {
            android.util.Slog.e("BiometricService", "Non-biometric, non-credential bits found. Authenticators: " + authenticators);
            return false;
        }
        int biometricBits = authenticators & 32767;
        if ((biometricBits == 0 && isCredentialRequested(authenticators)) || biometricBits == 15 || biometricBits == 255) {
            return true;
        }
        android.util.Slog.e("BiometricService", "Unsupported biometric flags. Authenticators: " + authenticators);
        return false;
    }

    static int biometricConstantsToBiometricManager(int biometricConstantsCode) {
        switch (biometricConstantsCode) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 7:
            case 9:
                return 0;
            case 11:
            case 14:
                return 11;
            case 12:
                return 12;
            case 15:
                return 15;
            case 18:
                return 1;
            default:
                android.util.Slog.e("BiometricService", "Unhandled result code: " + biometricConstantsCode);
                return 1;
        }
    }

    static int getAuthenticationTypeForResult(int reason) {
        switch (reason) {
            case 1:
            case 4:
                return 2;
            case 7:
                return 1;
            default:
                throw new java.lang.IllegalArgumentException("Unsupported dismissal reason: " + reason);
        }
    }

    static int authenticatorStatusToBiometricConstant(int status) {
        switch (status) {
            case 1:
                return 0;
            case 2:
            case 4:
                return 12;
            case 3:
            case 6:
            case 8:
            default:
                return 1;
            case 5:
                return 15;
            case 7:
                return 11;
            case 9:
                return 14;
            case 10:
                return 7;
            case 11:
                return 9;
            case 12:
                return 18;
        }
    }

    static boolean isConfirmationSupported(int modality) {
        switch (modality) {
            case 4:
            case 8:
                return true;
            default:
                return false;
        }
    }

    static int removeBiometricBits(int authenticators) {
        return authenticators & (-32768);
    }

    public static boolean listContains(int[] haystack, int needle) {
        for (int i : haystack) {
            if (i == needle) {
                return true;
            }
        }
        return false;
    }

    public static void checkPermissionOrShell(android.content.Context context, java.lang.String permission) {
        if (android.os.Binder.getCallingUid() == 2000) {
            return;
        }
        checkPermission(context, permission);
    }

    public static void checkPermission(android.content.Context context, java.lang.String permission) {
        context.enforceCallingOrSelfPermission(permission, "Must have " + permission + " permission.");
    }

    public static boolean isCurrentUserOrProfile(android.content.Context context, int userId) {
        android.os.UserManager um = android.os.UserManager.get(context);
        if (um == null) {
            android.util.Slog.e(TAG, "Unable to get UserManager");
            return false;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            for (int profileId : um.getEnabledProfileIds(android.app.ActivityManager.getCurrentUser())) {
                if (profileId == userId) {
                    android.os.Binder.restoreCallingIdentity(token);
                    return true;
                }
            }
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public static boolean isStrongBiometric(int sensorId) {
        android.hardware.biometrics.IBiometricService service = android.hardware.biometrics.IBiometricService.Stub.asInterface(android.os.ServiceManager.getService("biometric"));
        try {
            return isAtLeastStrength(service.getCurrentStrength(sensorId), 15);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "RemoteException", e);
            return false;
        }
    }

    public static int getCurrentStrength(int sensorId) {
        android.hardware.biometrics.IBiometricService service = android.hardware.biometrics.IBiometricService.Stub.asInterface(android.os.ServiceManager.getService("biometric"));
        try {
            return service.getCurrentStrength(sensorId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "RemoteException", e);
            return 0;
        }
    }

    public static boolean isKeyguard(android.content.Context context, java.lang.String clientPackage) {
        boolean hasPermission = hasInternalPermission(context);
        android.content.ComponentName keyguardComponent = android.content.ComponentName.unflattenFromString(context.getResources().getString(android.R.string.config_mms_user_agent));
        java.lang.String keyguardPackage = keyguardComponent != null ? keyguardComponent.getPackageName() : null;
        return hasPermission && keyguardPackage != null && keyguardPackage.equals(clientPackage);
    }

    public static boolean isSystem(android.content.Context context, java.lang.String clientPackage) {
        return hasInternalPermission(context) && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(clientPackage);
    }

    public static boolean isSettings(android.content.Context context, java.lang.String clientPackage) {
        return hasInternalPermission(context) && "com.android.settings".equals(clientPackage);
    }

    private static boolean hasInternalPermission(android.content.Context context) {
        return context.checkCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL") == 0;
    }

    public static java.lang.String getClientName(com.android.server.biometrics.sensors.BaseClientMonitor client) {
        return client != null ? client.getClass().getSimpleName() : "null";
    }

    private static boolean containsFlag(int haystack, int needle) {
        return (haystack & needle) != 0;
    }

    public static boolean isUserEncryptedOrLockdown(com.android.internal.widget.LockPatternUtils lpu, int user) {
        int strongAuth = lpu.getStrongAuthForUser(user);
        boolean isEncrypted = containsFlag(strongAuth, 1);
        boolean isLockDown = containsFlag(strongAuth, 2) || containsFlag(strongAuth, 32);
        android.util.Slog.d(TAG, "isEncrypted: " + isEncrypted + " isLockdown: " + isLockDown);
        return isEncrypted || isLockDown;
    }

    public static boolean isForeground(int callingUid, int callingPid) {
        try {
            java.util.List<android.app.ActivityManager.RunningAppProcessInfo> procs = android.app.ActivityManager.getService().getRunningAppProcesses();
            if (procs == null) {
                android.util.Slog.e(TAG, "No running app processes found, defaulting to true");
                return true;
            }
            for (int i = 0; i < procs.size(); i++) {
                android.app.ActivityManager.RunningAppProcessInfo proc = procs.get(i);
                if (proc.pid == callingPid && proc.uid == callingUid && isImportanceForeground(callingUid)) {
                    return true;
                }
            }
            return false;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "am.getRunningAppProcesses() failed");
            return false;
        }
    }

    public static int authenticatorStrengthToPropertyStrength(int strength) {
        switch (strength) {
            case 15:
                return 2;
            case 255:
                return 1;
            case 4095:
                return 0;
            default:
                throw new java.lang.IllegalArgumentException("Unknown strength: " + strength);
        }
    }

    public static int propertyStrengthToAuthenticatorStrength(int strength) {
        switch (strength) {
            case 0:
                return 4095;
            case 1:
                return 255;
            case 2:
                return 15;
            default:
                throw new java.lang.IllegalArgumentException("Unknown strength: " + strength);
        }
    }

    public static boolean isBackground(java.lang.String clientPackage) {
        android.util.Slog.v(TAG, "Checking if the authenticating is in background, clientPackage:" + clientPackage);
        java.util.List<android.app.ActivityManager.RunningTaskInfo> tasks = android.app.ActivityTaskManager.getInstance().getTasks(Integer.MAX_VALUE);
        if (tasks == null || tasks.isEmpty()) {
            android.util.Slog.d(TAG, "No running tasks reported");
            return true;
        }
        for (android.app.ActivityManager.RunningTaskInfo taskInfo : tasks) {
            android.content.ComponentName topActivity = taskInfo.topActivity;
            if (topActivity != null) {
                java.lang.String topPackage = topActivity.getPackageName();
                if (topPackage.contentEquals(clientPackage) && taskInfo.isVisible()) {
                    return false;
                }
                android.util.Slog.i(TAG, "Running task, top: " + topPackage + ", isVisible: " + taskInfo.isVisible());
            }
        }
        return true;
    }

    private static boolean isImportanceForeground(int callingUid) {
        android.app.ActivityManagerInternal activityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        if (activityManagerInternal != null) {
            int importance = android.app.ActivityManager.RunningAppProcessInfo.procStateToImportance(activityManagerInternal.getUidProcessState(callingUid));
            android.util.Slog.d(TAG, "callingUid = " + callingUid + ", importance = " + importance);
            if (importance <= 125) {
                return true;
            }
            return false;
        }
        return false;
    }
}
