package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class AuthService extends com.android.server.SystemService {
    private static final boolean AIDL_SUPPORT;
    private static final int DEFAULT_HIDL_DISABLED = 0;
    private static final java.lang.String SETTING_HIDL_DISABLED = "com.android.server.biometrics.AuthService.hidlDisabled";
    private static final java.lang.String SYSPROP_API_LEVEL = "ro.board.api_level";
    private static final java.lang.String SYSPROP_FIRST_API_LEVEL = "ro.board.first_api_level";
    private static final java.lang.String TAG = "AuthService";
    private android.hardware.biometrics.IBiometricService mBiometricService;
    final android.hardware.biometrics.IAuthService.Stub mImpl;
    private final com.android.server.biometrics.AuthService.Injector mInjector;

    static {
        AIDL_SUPPORT = "1".equals(android.os.SystemProperties.get("vendor.fingerprint.aidl.support", "0")) || "vsoc_arm64".equals(android.os.SystemProperties.get("ro.soc.model"));
    }

    public static class Injector {
        public android.hardware.biometrics.IBiometricService getBiometricService() {
            return android.hardware.biometrics.IBiometricService.Stub.asInterface(android.os.ServiceManager.getService("biometric"));
        }

        public void publishBinderService(com.android.server.biometrics.AuthService service, android.hardware.biometrics.IAuthService.Stub impl) {
            service.publishBinderService("auth", impl);
        }

        public java.lang.String[] getConfiguration(android.content.Context context) {
            return context.getResources().getStringArray(android.R.array.config_bg_current_drain_high_threshold_to_restricted_bucket);
        }

        public java.lang.String[] getFingerprintConfiguration(android.content.Context context) {
            if (com.android.server.biometrics.AuthService.AIDL_SUPPORT) {
                return getConfiguration(context);
            }
            return new java.lang.String[]{"0:2:15"};
        }

        public java.lang.String[] getFaceConfiguration(android.content.Context context) {
            return getConfiguration(context);
        }

        public java.lang.String[] getIrisConfiguration(android.content.Context context) {
            return getConfiguration(context);
        }

        public android.hardware.fingerprint.IFingerprintService getFingerprintService() {
            return android.hardware.fingerprint.IFingerprintService.Stub.asInterface(android.os.ServiceManager.getService("fingerprint"));
        }

        public android.hardware.face.IFaceService getFaceService() {
            return android.hardware.face.IFaceService.Stub.asInterface(android.os.ServiceManager.getService("face"));
        }

        public android.hardware.iris.IIrisService getIrisService() {
            return android.hardware.iris.IIrisService.Stub.asInterface(android.os.ServiceManager.getService("iris"));
        }

        public android.app.AppOpsManager getAppOps(android.content.Context context) {
            return (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        }

        public boolean isHidlDisabled(android.content.Context context) {
            return (android.os.Build.IS_ENG || android.os.Build.IS_USERDEBUG) && android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), com.android.server.biometrics.AuthService.SETTING_HIDL_DISABLED, 0, -2) == 1;
        }

        public java.lang.String[] getFingerprintAidlInstances() {
            return android.os.ServiceManager.getDeclaredInstances(android.hardware.biometrics.fingerprint.IFingerprint.DESCRIPTOR);
        }

        public java.lang.String[] getFaceAidlInstances() {
            return android.os.ServiceManager.getDeclaredInstances(android.hardware.biometrics.face.IFace.DESCRIPTOR);
        }

        public com.android.server.biometrics.BiometricHandlerProvider getBiometricHandlerProvider() {
            return com.android.server.biometrics.BiometricHandlerProvider.getInstance();
        }
    }

    private final class AuthServiceImpl extends android.hardware.biometrics.IAuthService.Stub {
        private AuthServiceImpl() {
        }

        public android.hardware.biometrics.ITestSession createTestSession(int sensorId, android.hardware.biometrics.ITestSessionCallback callback, java.lang.String opPackageName) throws android.os.RemoteException {
            super.createTestSession_enforcePermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.biometrics.AuthService.this.mInjector.getBiometricService().createTestSession(sensorId, callback, opPackageName);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<android.hardware.biometrics.SensorPropertiesInternal> getSensorProperties(java.lang.String opPackageName) throws android.os.RemoteException {
            super.getSensorProperties_enforcePermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.biometrics.AuthService.this.mInjector.getBiometricService().getSensorProperties(opPackageName);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.lang.String getUiPackage() {
            super.getUiPackage_enforcePermission();
            return com.android.server.biometrics.AuthService.this.getContext().getResources().getString(android.R.string.config_chooserActivity);
        }

        public long authenticate(android.os.IBinder token, long sessionId, int userId, android.hardware.biometrics.IBiometricServiceReceiver receiver, java.lang.String opPackageName, android.hardware.biometrics.PromptInfo promptInfo) throws android.os.RemoteException {
            int callingUserId = android.os.UserHandle.getCallingUserId();
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            if (userId == callingUserId) {
                com.android.server.biometrics.AuthService.this.checkPermission();
            } else {
                android.util.Slog.w(com.android.server.biometrics.AuthService.TAG, "User " + callingUserId + " is requesting authentication of userid: " + userId);
                com.android.server.biometrics.AuthService.this.checkInternalPermission();
            }
            if (!com.android.server.biometrics.AuthService.this.checkAppOps(callingUid, opPackageName, "authenticate()")) {
                authenticateFastFail("Denied by app ops: " + opPackageName, receiver);
                return -1L;
            }
            if (token == null || receiver == null || opPackageName == null || promptInfo == null) {
                authenticateFastFail("Unable to authenticate, one or more null arguments", receiver);
                return -1L;
            }
            if (!com.android.server.biometrics.Utils.isForeground(callingUid, callingPid)) {
                authenticateFastFail("Caller is not foreground: " + opPackageName, receiver);
                return -1L;
            }
            if (promptInfo.requiresTestOrInternalPermission() && com.android.server.biometrics.AuthService.this.getContext().checkCallingOrSelfPermission("android.permission.TEST_BIOMETRIC") != 0) {
                com.android.server.biometrics.AuthService.this.checkInternalPermission();
            }
            if (promptInfo.requiresInternalPermission()) {
                com.android.server.biometrics.AuthService.this.checkInternalPermission();
            }
            if (promptInfo.requiresAdvancedPermission()) {
                com.android.server.biometrics.AuthService.this.checkBiometricAdvancedPermission();
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.companion.virtual.VirtualDeviceManagerInternal vdm = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.biometrics.AuthService.this.getLocalService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
                if (vdm != null) {
                    vdm.onAuthenticationPrompt(callingUid);
                }
                return com.android.server.biometrics.AuthService.this.mBiometricService.authenticate(token, sessionId, userId, receiver, opPackageName, promptInfo);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private void authenticateFastFail(java.lang.String message, android.hardware.biometrics.IBiometricServiceReceiver receiver) {
            android.util.Slog.e(com.android.server.biometrics.AuthService.TAG, "authenticateFastFail: " + message);
            try {
                receiver.onError(0, 5, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.biometrics.AuthService.TAG, "authenticateFastFail failed to notify caller", e);
            }
        }

        public void cancelAuthentication(android.os.IBinder token, java.lang.String opPackageName, long requestId) throws android.os.RemoteException {
            com.android.server.biometrics.AuthService.this.checkPermission();
            if (token == null || opPackageName == null) {
                android.util.Slog.e(com.android.server.biometrics.AuthService.TAG, "Unable to cancel authentication, one or more null arguments");
                return;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.biometrics.AuthService.this.mBiometricService.cancelAuthentication(token, opPackageName, requestId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public int canAuthenticate(java.lang.String opPackageName, int userId, int authenticators) throws android.os.RemoteException {
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (userId != callingUserId) {
                com.android.server.biometrics.AuthService.this.checkInternalPermission();
            } else {
                com.android.server.biometrics.AuthService.this.checkPermission();
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                int result = com.android.server.biometrics.AuthService.this.mBiometricService.canAuthenticate(opPackageName, userId, callingUserId, authenticators);
                android.util.Slog.d(com.android.server.biometrics.AuthService.TAG, "canAuthenticate, userId: " + userId + ", callingUserId: " + callingUserId + ", authenticators: " + authenticators + ", result: " + result);
                return result;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public long getLastAuthenticationTime(int userId, int authenticators) throws android.os.RemoteException {
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (userId != callingUserId) {
                com.android.server.biometrics.AuthService.this.checkInternalPermission();
            } else {
                com.android.server.biometrics.AuthService.this.checkPermission();
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (!com.android.internal.hidden_from_bootclasspath.android.hardware.biometrics.Flags.lastAuthenticationTime()) {
                    throw new java.lang.UnsupportedOperationException();
                }
                return com.android.server.biometrics.AuthService.this.mBiometricService.getLastAuthenticationTime(userId, authenticators);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public boolean hasEnrolledBiometrics(int userId, java.lang.String opPackageName) throws android.os.RemoteException {
            com.android.server.biometrics.AuthService.this.checkInternalPermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.biometrics.AuthService.this.mBiometricService.hasEnrolledBiometrics(userId, opPackageName);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void registerEnabledOnKeyguardCallback(android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback callback) throws android.os.RemoteException {
            com.android.server.biometrics.AuthService.this.checkInternalPermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.biometrics.AuthService.this.mBiometricService.registerEnabledOnKeyguardCallback(callback);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void registerAuthenticationStateListener(android.hardware.biometrics.AuthenticationStateListener listener) throws android.os.RemoteException {
            com.android.server.biometrics.AuthService.this.checkInternalPermission();
            android.hardware.fingerprint.IFingerprintService fingerprintService = com.android.server.biometrics.AuthService.this.mInjector.getFingerprintService();
            if (fingerprintService != null) {
                fingerprintService.registerAuthenticationStateListener(listener);
            }
            android.hardware.face.IFaceService faceService = com.android.server.biometrics.AuthService.this.mInjector.getFaceService();
            if (faceService != null) {
                faceService.registerAuthenticationStateListener(listener);
            }
        }

        public void unregisterAuthenticationStateListener(android.hardware.biometrics.AuthenticationStateListener listener) throws android.os.RemoteException {
            com.android.server.biometrics.AuthService.this.checkInternalPermission();
            android.hardware.fingerprint.IFingerprintService fingerprintService = com.android.server.biometrics.AuthService.this.mInjector.getFingerprintService();
            if (fingerprintService != null) {
                fingerprintService.unregisterAuthenticationStateListener(listener);
            }
            android.hardware.face.IFaceService faceService = com.android.server.biometrics.AuthService.this.mInjector.getFaceService();
            if (faceService != null) {
                faceService.unregisterAuthenticationStateListener(listener);
            }
        }

        public void invalidateAuthenticatorIds(int userId, int fromSensorId, android.hardware.biometrics.IInvalidationCallback callback) throws android.os.RemoteException {
            com.android.server.biometrics.AuthService.this.checkInternalPermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.biometrics.AuthService.this.mBiometricService.invalidateAuthenticatorIds(userId, fromSensorId, callback);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public long[] getAuthenticatorIds(int userId) throws android.os.RemoteException {
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (userId != callingUserId) {
                com.android.server.biometrics.AuthService.this.getContext().enforceCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL", "Must have android.permission.USE_BIOMETRIC_INTERNAL permission.");
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.biometrics.AuthService.this.mBiometricService.getAuthenticatorIds(userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void resetLockoutTimeBound(android.os.IBinder token, java.lang.String opPackageName, int fromSensorId, int userId, byte[] hardwareAuthToken) throws android.os.RemoteException {
            com.android.server.biometrics.AuthService.this.checkInternalPermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.biometrics.AuthService.this.mBiometricService.resetLockoutTimeBound(token, opPackageName, fromSensorId, userId, hardwareAuthToken);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void resetLockout(int userId, byte[] hardwareAuthToken) throws android.os.RemoteException {
            com.android.server.biometrics.AuthService.this.checkInternalPermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.biometrics.AuthService.this.mBiometricService.resetLockout(userId, hardwareAuthToken);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.lang.CharSequence getButtonLabel(int userId, java.lang.String opPackageName, int authenticators) throws android.os.RemoteException {
            java.lang.String result;
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (userId != callingUserId) {
                com.android.server.biometrics.AuthService.this.checkInternalPermission();
            } else {
                com.android.server.biometrics.AuthService.this.checkPermission();
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                int modality = com.android.server.biometrics.AuthService.this.mBiometricService.getCurrentModality(opPackageName, userId, callingUserId, authenticators);
                switch (com.android.server.biometrics.AuthService.getCredentialBackupModality(modality)) {
                    case 0:
                        result = null;
                        break;
                    case 1:
                        result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.roamingText5);
                        break;
                    case 2:
                        result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.fingerprint_authenticated);
                        break;
                    case 8:
                        result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.face_authenticated_no_confirmation_required);
                        break;
                    default:
                        result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.biometric_error_canceled);
                        break;
                }
                return result;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.lang.CharSequence getPromptMessage(int userId, java.lang.String opPackageName, int authenticators) throws android.os.RemoteException {
            java.lang.String result;
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (userId != callingUserId) {
                com.android.server.biometrics.AuthService.this.checkInternalPermission();
            } else {
                com.android.server.biometrics.AuthService.this.checkPermission();
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                int modality = com.android.server.biometrics.AuthService.this.mBiometricService.getCurrentModality(opPackageName, userId, callingUserId, authenticators);
                boolean isCredentialAllowed = com.android.server.biometrics.Utils.isCredentialRequested(authenticators);
                switch (com.android.server.biometrics.AuthService.getCredentialBackupModality(modality)) {
                    case 0:
                        result = null;
                        break;
                    case 1:
                        result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.roamingText6);
                        break;
                    case 2:
                        if (isCredentialAllowed) {
                            result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.fingerprint_name_template);
                        } else {
                            result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.fingerprint_dialog_use_fingerprint_instead);
                        }
                        break;
                    case 8:
                        if (isCredentialAllowed) {
                            result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.face_recalibrate_notification_name);
                        } else {
                            result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.face_error_hw_not_available);
                        }
                        break;
                    default:
                        if (isCredentialAllowed) {
                            result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.bugreport_message);
                        } else {
                            result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.biometric_error_hw_unavailable);
                        }
                        break;
                }
                return result;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.lang.CharSequence getSettingName(int userId, java.lang.String opPackageName, int authenticators) throws android.os.RemoteException {
            java.lang.String result;
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (userId != callingUserId) {
                com.android.server.biometrics.AuthService.this.checkInternalPermission();
            } else {
                com.android.server.biometrics.AuthService.this.checkPermission();
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                int modality = com.android.server.biometrics.AuthService.this.mBiometricService.getSupportedModalities(authenticators);
                switch (modality) {
                    case 0:
                        result = null;
                        break;
                    case 1:
                        result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.roamingText5);
                        break;
                    case 2:
                        result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.fingerprint_authenticated);
                        break;
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        if ((modality & 1) == 0) {
                            result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.biometric_error_canceled);
                        } else {
                            int biometricModality = modality & (-2);
                            if (biometricModality == 2) {
                                result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.fingerprint_loe_notification_msg);
                            } else if (biometricModality == 8) {
                                result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.face_recalibrate_notification_content);
                            } else {
                                result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.bugreport_countdown);
                            }
                        }
                        break;
                    case 4:
                        result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.biometric_error_canceled);
                        break;
                    case 8:
                        result = com.android.server.biometrics.AuthService.this.getContext().getString(android.R.string.face_authenticated_no_confirmation_required);
                        break;
                }
                return result;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public AuthService(android.content.Context context) {
        this(context, new com.android.server.biometrics.AuthService.Injector());
    }

    public AuthService(android.content.Context context, com.android.server.biometrics.AuthService.Injector injector) {
        super(context);
        this.mInjector = injector;
        this.mImpl = new com.android.server.biometrics.AuthService.AuthServiceImpl();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mBiometricService = this.mInjector.getBiometricService();
        android.util.Slog.d(TAG, "AIDL_SUPPORT: " + AIDL_SUPPORT);
        if (!AIDL_SUPPORT && !this.mInjector.isHidlDisabled(getContext())) {
            int firstApiLevel = android.os.SystemProperties.getInt(SYSPROP_FIRST_API_LEVEL, 0);
            int apiLevel = android.os.SystemProperties.getInt(SYSPROP_API_LEVEL, firstApiLevel);
            java.lang.String[] configStrings = this.mInjector.getConfiguration(getContext());
            if (configStrings.length == 0 && apiLevel == 30) {
                android.util.Slog.w(TAG, "Found R vendor partition without config_biometric_sensors");
                configStrings = generateRSdkCompatibleConfiguration();
            }
            com.android.server.biometrics.SensorConfig[] hidlConfigs = new com.android.server.biometrics.SensorConfig[configStrings.length];
            for (int i = 0; i < configStrings.length; i++) {
                hidlConfigs[i] = new com.android.server.biometrics.SensorConfig(configStrings[i]);
            }
        }
        registerAuthenticators();
        this.mInjector.publishBinderService(this, this.mImpl);
    }

    private void registerAuthenticators() {
        com.android.server.biometrics.BiometricHandlerProvider handlerProvider = this.mInjector.getBiometricHandlerProvider();
        registerFingerprintSensors(this.mInjector.getFingerprintAidlInstances(), this.mInjector.getFingerprintConfiguration(getContext()), getContext(), this.mInjector.getFingerprintService(), handlerProvider);
        registerFaceSensors(this.mInjector.getFaceAidlInstances(), this.mInjector.getFaceConfiguration(getContext()), getContext(), this.mInjector.getFaceService(), handlerProvider);
        registerIrisSensors(this.mInjector.getIrisConfiguration(getContext()));
    }

    private void registerIrisSensors(java.lang.String[] hidlConfigStrings) {
        com.android.server.biometrics.SensorConfig[] hidlConfigs;
        if (!this.mInjector.isHidlDisabled(getContext())) {
            int firstApiLevel = android.os.SystemProperties.getInt(SYSPROP_FIRST_API_LEVEL, 0);
            int apiLevel = android.os.SystemProperties.getInt(SYSPROP_API_LEVEL, firstApiLevel);
            if (hidlConfigStrings.length == 0 && apiLevel == 30) {
                android.util.Slog.w(TAG, "Found R vendor partition without config_biometric_sensors");
                hidlConfigStrings = generateRSdkCompatibleConfiguration();
            }
            hidlConfigs = new com.android.server.biometrics.SensorConfig[hidlConfigStrings.length];
            for (int i = 0; i < hidlConfigStrings.length; i++) {
                hidlConfigs[i] = new com.android.server.biometrics.SensorConfig(hidlConfigStrings[i]);
            }
        } else {
            hidlConfigs = null;
        }
        java.util.List<android.hardware.biometrics.SensorPropertiesInternal> hidlIrisSensors = new java.util.ArrayList<>();
        if (hidlConfigs != null) {
            for (com.android.server.biometrics.SensorConfig sensor : hidlConfigs) {
                switch (sensor.modality) {
                    case 4:
                        hidlIrisSensors.add(getHidlIrisSensorProps(sensor.id, sensor.strength));
                        break;
                    default:
                        android.util.Slog.e(TAG, "Unknown modality: " + sensor.modality);
                        break;
                }
            }
        }
        android.hardware.iris.IIrisService irisService = this.mInjector.getIrisService();
        if (irisService != null) {
            try {
                irisService.registerAuthenticators(hidlIrisSensors);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "RemoteException when registering iris authenticators", e);
                return;
            }
        }
        if (hidlIrisSensors.size() > 0) {
            android.util.Slog.e(TAG, "HIDL iris configuration exists, but IrisService is null.");
        }
    }

    private static void registerFaceSensors(final java.lang.String[] faceAidlInstances, final java.lang.String[] hidlConfigStrings, final android.content.Context context, final android.hardware.face.IFaceService faceService, com.android.server.biometrics.BiometricHandlerProvider handlerProvider) {
        if ((hidlConfigStrings == null || hidlConfigStrings.length == 0) && (faceAidlInstances == null || faceAidlInstances.length == 0)) {
            android.util.Slog.d(TAG, "No face sensors.");
        } else {
            handlerProvider.getFaceHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.AuthService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.biometrics.AuthService.lambda$registerFaceSensors$0(hidlConfigStrings, context, faceAidlInstances, faceService);
                }
            });
        }
    }

    static /* synthetic */ void lambda$registerFaceSensors$0(java.lang.String[] hidlConfigStrings, android.content.Context context, java.lang.String[] faceAidlInstances, android.hardware.face.IFaceService faceService) {
        android.hardware.face.FaceSensorConfigurations mFaceSensorConfigurations = new android.hardware.face.FaceSensorConfigurations(hidlConfigStrings != null && hidlConfigStrings.length > 0);
        if (hidlConfigStrings != null && hidlConfigStrings.length > 0) {
            mFaceSensorConfigurations.addHidlConfigs(hidlConfigStrings, context);
        }
        if (faceAidlInstances != null && faceAidlInstances.length > 0) {
            mFaceSensorConfigurations.addAidlConfigs(faceAidlInstances);
        }
        if (faceService != null) {
            try {
                faceService.registerAuthenticators(mFaceSensorConfigurations);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "RemoteException when registering face authenticators", e);
                return;
            }
        }
        if (mFaceSensorConfigurations.hasSensorConfigurations()) {
            android.util.Slog.e(TAG, "Face configuration exists, but FaceService is null.");
        }
    }

    private static void registerFingerprintSensors(final java.lang.String[] fingerprintAidlInstances, final java.lang.String[] hidlConfigStrings, final android.content.Context context, final android.hardware.fingerprint.IFingerprintService fingerprintService, com.android.server.biometrics.BiometricHandlerProvider handlerProvider) {
        if ((hidlConfigStrings == null || hidlConfigStrings.length == 0) && (fingerprintAidlInstances == null || fingerprintAidlInstances.length == 0)) {
            android.util.Slog.d(TAG, "No fingerprint sensors.");
        } else {
            handlerProvider.getFingerprintHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.AuthService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.biometrics.AuthService.lambda$registerFingerprintSensors$1(hidlConfigStrings, context, fingerprintAidlInstances, fingerprintService);
                }
            });
        }
    }

    static /* synthetic */ void lambda$registerFingerprintSensors$1(java.lang.String[] hidlConfigStrings, android.content.Context context, java.lang.String[] fingerprintAidlInstances, android.hardware.fingerprint.IFingerprintService fingerprintService) {
        android.hardware.fingerprint.FingerprintSensorConfigurations mFingerprintSensorConfigurations = new android.hardware.fingerprint.FingerprintSensorConfigurations(hidlConfigStrings == null || hidlConfigStrings.length <= 0);
        if (hidlConfigStrings != null && hidlConfigStrings.length > 0) {
            mFingerprintSensorConfigurations.addHidlSensors(hidlConfigStrings, context);
        }
        if (fingerprintAidlInstances != null && fingerprintAidlInstances.length > 0) {
            mFingerprintSensorConfigurations.addAidlSensors(fingerprintAidlInstances);
        }
        if (fingerprintService != null) {
            try {
                fingerprintService.registerAuthenticators(mFingerprintSensorConfigurations);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "RemoteException when registering fingerprint authenticators", e);
                return;
            }
        }
        if (mFingerprintSensorConfigurations.hasSensorConfigurations()) {
            android.util.Slog.e(TAG, "Fingerprint configuration exists, but FingerprintService is null.");
        }
    }

    private java.lang.String[] generateRSdkCompatibleConfiguration() {
        android.content.pm.PackageManager pm = getContext().getPackageManager();
        java.util.ArrayList<java.lang.String> modalities = new java.util.ArrayList<>();
        if (pm.hasSystemFeature("android.hardware.fingerprint")) {
            modalities.add(java.lang.String.valueOf(2));
        }
        if (pm.hasSystemFeature("android.hardware.biometrics.face")) {
            modalities.add(java.lang.String.valueOf(8));
        }
        java.lang.String strength = java.lang.String.valueOf(4095);
        java.lang.String[] configStrings = new java.lang.String[modalities.size()];
        for (int i = 0; i < modalities.size(); i++) {
            java.lang.String id = java.lang.String.valueOf(i);
            java.lang.String modality = modalities.get(i);
            configStrings[i] = java.lang.String.join(":", id, modality, strength);
        }
        android.util.Slog.d(TAG, "Generated config_biometric_sensors: " + java.util.Arrays.toString(configStrings));
        return configStrings;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkInternalPermission() {
        getContext().enforceCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL", "Must have USE_BIOMETRIC_INTERNAL permission");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkBiometricAdvancedPermission() {
        getContext().enforceCallingOrSelfPermission("android.permission.SET_BIOMETRIC_DIALOG_ADVANCED", "Must have SET_BIOMETRIC_DIALOG_ADVANCED permission");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPermission() {
        if (getContext().checkCallingOrSelfPermission("android.permission.USE_FINGERPRINT") != 0) {
            getContext().enforceCallingOrSelfPermission("android.permission.USE_BIOMETRIC", "Must have USE_BIOMETRIC permission");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkAppOps(int uid, java.lang.String opPackageName, java.lang.String reason) {
        return this.mInjector.getAppOps(getContext()).noteOp(78, uid, opPackageName, (java.lang.String) null, reason) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getCredentialBackupModality(int modality) {
        return modality == 1 ? modality : modality & (-2);
    }

    private android.hardware.fingerprint.FingerprintSensorPropertiesInternal getHidlFingerprintSensorProps(int sensorId, int strength) {
        int sensorType;
        int[] udfpsProps = getContext().getResources().getIntArray(android.R.array.config_tether_usb_regexs);
        boolean isUdfps = !com.android.internal.util.ArrayUtils.isEmpty(udfpsProps);
        boolean isPowerbuttonFps = getContext().getResources().getBoolean(android.R.bool.config_hasRecents);
        if (isUdfps) {
            sensorType = 3;
        } else if (isPowerbuttonFps) {
            sensorType = 4;
        } else {
            sensorType = 1;
        }
        int maxEnrollmentsPerUser = getContext().getResources().getInteger(android.R.integer.config_dreamsBatteryLevelMinimumWhenPowered);
        java.util.List<android.hardware.biometrics.ComponentInfoInternal> componentInfo = new java.util.ArrayList<>();
        if (isUdfps && udfpsProps.length == 3) {
            return new android.hardware.fingerprint.FingerprintSensorPropertiesInternal(sensorId, com.android.server.biometrics.Utils.authenticatorStrengthToPropertyStrength(strength), maxEnrollmentsPerUser, componentInfo, sensorType, true, false, java.util.List.of(new android.hardware.biometrics.SensorLocationInternal("", udfpsProps[0], udfpsProps[1], udfpsProps[2])));
        }
        return new android.hardware.fingerprint.FingerprintSensorPropertiesInternal(sensorId, com.android.server.biometrics.Utils.authenticatorStrengthToPropertyStrength(strength), maxEnrollmentsPerUser, componentInfo, sensorType, false);
    }

    private android.hardware.face.FaceSensorPropertiesInternal getHidlFaceSensorProps(int sensorId, int strength) {
        boolean supportsSelfIllumination = getContext().getResources().getBoolean(android.R.bool.config_enableVirtualDeviceManager);
        int maxTemplatesAllowed = getContext().getResources().getInteger(android.R.integer.config_dreamsBatteryLevelMinimumWhenNotPowered);
        java.util.List<android.hardware.biometrics.ComponentInfoInternal> componentInfo = new java.util.ArrayList<>();
        return new android.hardware.face.FaceSensorPropertiesInternal(sensorId, com.android.server.biometrics.Utils.authenticatorStrengthToPropertyStrength(strength), maxTemplatesAllowed, componentInfo, 0, false, supportsSelfIllumination, true);
    }

    private android.hardware.biometrics.SensorPropertiesInternal getHidlIrisSensorProps(int sensorId, int strength) {
        java.util.List<android.hardware.biometrics.ComponentInfoInternal> componentInfo = new java.util.ArrayList<>();
        return new android.hardware.biometrics.SensorPropertiesInternal(sensorId, com.android.server.biometrics.Utils.authenticatorStrengthToPropertyStrength(strength), 1, componentInfo, false, false);
    }
}
