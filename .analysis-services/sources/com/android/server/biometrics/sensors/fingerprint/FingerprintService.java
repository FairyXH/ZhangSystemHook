package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintService extends com.android.server.SystemService {
    protected static final java.lang.String TAG = "FingerprintService";
    private final java.util.function.Supplier<java.lang.String[]> mAidlInstanceNameSupplier;
    private final android.app.AppOpsManager mAppOps;
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private final com.android.server.biometrics.log.BiometricContext mBiometricContext;
    private final com.android.server.biometrics.sensors.BiometricStateCallback<com.android.server.biometrics.sensors.fingerprint.ServiceProvider, android.hardware.fingerprint.FingerprintSensorPropertiesInternal> mBiometricStateCallback;
    private final java.util.function.Function<java.lang.String, com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider> mFingerprintProvider;
    private final com.android.server.biometrics.sensors.fingerprint.FingerprintService.FingerprintProviderFunction mFingerprintProviderFunction;
    private final com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher mGestureAvailabilityDispatcher;
    private final android.os.Handler mHandler;
    private com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceExt mIOplusFingerprintServiceExt;
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;
    private com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper mOplusFingerprintServiceWrapper;
    private final com.android.server.biometrics.sensors.fingerprint.FingerprintServiceRegistry mRegistry;
    final android.hardware.fingerprint.IFingerprintService.Stub mServiceWrapper;

    interface FingerprintProviderFunction {
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider getFingerprintProvider(android.util.Pair<java.lang.String, android.hardware.biometrics.fingerprint.SensorProps[]> pair, boolean z);
    }

    /* JADX INFO: renamed from: com.android.server.biometrics.sensors.fingerprint.FingerprintService$1, reason: invalid class name */
    class AnonymousClass1 extends android.hardware.fingerprint.IFingerprintService.Stub {
        AnonymousClass1() {
        }

        public android.hardware.biometrics.ITestSession createTestSession(int sensorId, android.hardware.biometrics.ITestSessionCallback callback, java.lang.String opPackageName) {
            super.createTestSession_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for createTestSession, sensorId: " + sensorId);
                return null;
            }
            return provider.createTestSession(sensorId, callback, opPackageName);
        }

        public byte[] dumpSensorServiceStateProto(int sensorId, boolean clearSchedulerBuffer) {
            super.dumpSensorServiceStateProto_enforcePermission();
            android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider != null) {
                provider.dumpProtoState(sensorId, proto, clearSchedulerBuffer);
            }
            proto.flush();
            return proto.getBytes();
        }

        public java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> getSensorPropertiesInternal(java.lang.String opPackageName) {
            if (com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext().checkCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL") != 0) {
                com.android.server.biometrics.Utils.checkPermission(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), "android.permission.TEST_BIOMETRIC");
            }
            return com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getAllProperties();
        }

        public android.hardware.fingerprint.FingerprintSensorPropertiesInternal getSensorProperties(int sensorId, java.lang.String opPackageName) {
            super.getSensorProperties_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "No matching sensor for getSensorProperties, sensorId: " + sensorId + ", caller: " + opPackageName);
                return null;
            }
            return provider.getSensorProperties(sensorId);
        }

        public void generateChallenge(android.os.IBinder token, int sensorId, int userId, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, java.lang.String opPackageName) {
            super.generateChallenge_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "No matching sensor for generateChallenge, sensorId: " + sensorId);
            } else {
                provider.scheduleGenerateChallenge(sensorId, userId, token, receiver, opPackageName);
            }
        }

        public void revokeChallenge(android.os.IBinder token, int sensorId, int userId, java.lang.String opPackageName, long challenge) {
            super.revokeChallenge_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "No matching sensor for revokeChallenge, sensorId: " + sensorId);
            } else {
                provider.scheduleRevokeChallenge(sensorId, userId, token, opPackageName, challenge);
            }
        }

        public long enroll(android.os.IBinder token, byte[] hardwareAuthToken, int userId, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, java.lang.String opPackageName, int enrollReason, android.hardware.fingerprint.FingerprintEnrollOptions options) {
            super.enroll_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for enroll");
                return -1L;
            }
            if (!com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().enrollPreOperation(token, opPackageName, userId)) {
                return ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).scheduleEnroll(((java.lang.Integer) provider.first).intValue(), token, hardwareAuthToken, userId, receiver, opPackageName, enrollReason, options);
            }
            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().notifyOperationCanceled(receiver);
            return -1L;
        }

        public void cancelEnrollment(android.os.IBinder token, long requestId) {
            super.cancelEnrollment_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for cancelEnrollment");
            } else {
                ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).cancelEnrollment(((java.lang.Integer) provider.first).intValue(), token, requestId);
            }
        }

        public long authenticate(android.os.IBinder token, long operationId, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, android.hardware.fingerprint.FingerprintAuthenticateOptions options) throws java.lang.Throwable {
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider;
            int userId;
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider2;
            java.lang.String str;
            boolean zIsIgnoreEnrollmentState;
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int callingUserId = android.os.UserHandle.getCallingUserId();
            java.lang.String opPackageName = options.getOpPackageName();
            java.lang.String attributionTag = options.getAttributionTag();
            int oriUserId = options.getUserId();
            int userId2 = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().changeUserIdIfNeeded(oriUserId);
            if (!com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.canUseFingerprint(opPackageName, attributionTag, true, callingUid, callingPid, callingUserId)) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Authenticate rejecting package: " + opPackageName + ", " + callingUid + ", " + callingPid);
                return -1L;
            }
            boolean isKeyguard = com.android.server.biometrics.Utils.isKeyguard(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), opPackageName);
            long identity2 = android.os.Binder.clearCallingIdentity();
            if (isKeyguard) {
                try {
                    if (com.android.server.biometrics.Utils.isUserEncryptedOrLockdown(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mLockPatternUtils, userId2)) {
                        android.util.EventLog.writeEvent(1397638484, "79776455");
                        android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Authenticate invoked when user is encrypted or lockdown");
                        return -1L;
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity2);
                }
            }
            android.os.Binder.restoreCallingIdentity(identity2);
            boolean restricted = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext().checkCallingPermission("android.permission.MANAGE_FINGERPRINT") != 0;
            int statsClient = isKeyguard ? 1 : 3;
            if (options.getSensorId() == -1) {
                provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
            } else {
                com.android.server.biometrics.Utils.checkPermission(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), "android.permission.USE_BIOMETRIC_INTERNAL");
                provider = new android.util.Pair<>(java.lang.Integer.valueOf(options.getSensorId()), com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(options.getSensorId()));
            }
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for authenticate");
                return -1L;
            }
            options.setSensorId(((java.lang.Integer) provider.first).intValue());
            if (com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().authPreOperation(token, opPackageName, options.getSensorId())) {
                com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().notifyOperationCanceled(receiver);
                return -1L;
            }
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal sensorProps = ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).getSensorProperties(options.getSensorId());
            if (isKeyguard || com.android.server.biometrics.Utils.isSettings(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), opPackageName) || com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().skipAuthWithPrompt(opPackageName) || sensorProps == null) {
                userId = userId2;
                provider2 = provider;
            } else {
                if (sensorProps.isAnyUdfpsType() || sensorProps.isAnySidefpsType()) {
                    try {
                        zIsIgnoreEnrollmentState = options.isIgnoreEnrollmentState();
                        str = com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG;
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        e = e;
                        str = com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG;
                    }
                    try {
                        return authenticateWithPrompt(operationId, sensorProps, callingUid, callingUserId, receiver, opPackageName, zIsIgnoreEnrollmentState);
                    } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                        e = e2;
                        android.util.Slog.e(str, "Invalid package", e);
                        return -1L;
                    }
                }
                userId = userId2;
                provider2 = provider;
            }
            identity2 = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.companion.virtual.VirtualDeviceManagerInternal vdm = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getLocalService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
                if (vdm != null) {
                    try {
                        vdm.onAuthenticationPrompt(callingUid);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                android.os.Binder.restoreCallingIdentity(identity2);
                android.hardware.fingerprint.FingerprintAuthenticateOptions changedOptions = new android.hardware.fingerprint.FingerprintAuthenticateOptions.Builder().setUserId(userId).setSensorId(options.getSensorId()).setIgnoreEnrollmentState(options.isIgnoreEnrollmentState()).setOpPackageName(options.getOpPackageName()).setAttributionTag(options.getAttributionTag()).setDisplayState(options.getDisplayState()).build();
                return ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider2.second).scheduleAuthenticate(token, operationId, 0, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), changedOptions, restricted, statsClient, isKeyguard);
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }

        private long authenticateWithPrompt(long operationId, final android.hardware.fingerprint.FingerprintSensorPropertiesInternal props, int uId, final int userId, final android.hardware.fingerprint.IFingerprintServiceReceiver receiver, java.lang.String opPackageName, boolean ignoreEnrollmentState) throws android.content.pm.PackageManager.NameNotFoundException {
            android.content.Context context = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getUiContext();
            android.content.Context promptContext = context.createPackageContextAsUser(opPackageName, 0, android.os.UserHandle.getUserHandleForUid(uId));
            java.util.concurrent.Executor executor = context.getMainExecutor();
            android.hardware.biometrics.BiometricPrompt biometricPrompt = new android.hardware.biometrics.BiometricPrompt.Builder(promptContext).setTitle(context.getString(android.R.string.biometric_error_user_canceled)).setSubtitle(context.getString(android.R.string.fingerprint_dialog_use_fingerprint_instead)).setNegativeButton(context.getString(android.R.string.cancel), executor, new android.content.DialogInterface.OnClickListener() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$1$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(android.content.DialogInterface dialogInterface, int i) {
                    com.android.server.biometrics.sensors.fingerprint.FingerprintService.AnonymousClass1.lambda$authenticateWithPrompt$0(receiver, dialogInterface, i);
                }
            }).setIsForLegacyFingerprintManager(props.sensorId).setIgnoreEnrollmentState(ignoreEnrollmentState).build();
            android.hardware.biometrics.BiometricPrompt.AuthenticationCallback promptCallback = new android.hardware.biometrics.BiometricPrompt.AuthenticationCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService.1.1
                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationError(int errorCode, java.lang.CharSequence errString) {
                    try {
                        if (com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.isKnownErrorCode(errorCode)) {
                            receiver.onError(errorCode, 0);
                        } else {
                            receiver.onError(8, errorCode);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Remote exception in onAuthenticationError()", e);
                    }
                }

                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationSucceeded(android.hardware.biometrics.BiometricPrompt.AuthenticationResult result) {
                    android.hardware.fingerprint.Fingerprint fingerprint = new android.hardware.fingerprint.Fingerprint("", 0, 0L);
                    boolean isStrong = props.sensorStrength == 2;
                    try {
                        receiver.onAuthenticationSucceeded(fingerprint, userId, isStrong);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Remote exception in onAuthenticationSucceeded()", e);
                    }
                }

                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationFailed() {
                    try {
                        receiver.onAuthenticationFailed();
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Remote exception in onAuthenticationFailed()", e);
                    }
                }

                public void onAuthenticationAcquired(int acquireInfo) {
                    try {
                        if (com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.isKnownAcquiredCode(acquireInfo)) {
                            receiver.onAcquired(acquireInfo, 0);
                        } else {
                            receiver.onAcquired(6, acquireInfo);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Remote exception in onAuthenticationAcquired()", e);
                    }
                }

                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationHelp(int acquireInfo, java.lang.CharSequence helpString) {
                    onAuthenticationAcquired(acquireInfo);
                }
            };
            return biometricPrompt.authenticateForOperation(new android.os.CancellationSignal(), executor, promptCallback, operationId);
        }

        static /* synthetic */ void lambda$authenticateWithPrompt$0(android.hardware.fingerprint.IFingerprintServiceReceiver receiver, android.content.DialogInterface dialog, int which) {
            try {
                receiver.onError(10, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Remote exception in negative button onClick()", e);
            }
        }

        public long detectFingerprint(android.os.IBinder token, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, android.hardware.fingerprint.FingerprintAuthenticateOptions options) {
            super.detectFingerprint_enforcePermission();
            java.lang.String opPackageName = options.getOpPackageName();
            if (!com.android.server.biometrics.Utils.isKeyguard(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), opPackageName)) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "detectFingerprint called from non-sysui package: " + opPackageName);
                return -1L;
            }
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for detectFingerprint");
                return -1L;
            }
            options.setSensorId(((java.lang.Integer) provider.first).intValue());
            return ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).scheduleFingerDetect(token, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), options, 1);
        }

        public void prepareForAuthentication(android.os.IBinder token, long operationId, android.hardware.biometrics.IBiometricSensorReceiver sensorReceiver, android.hardware.fingerprint.FingerprintAuthenticateOptions options, long requestId, int cookie, boolean allowBackgroundAuthentication, boolean isForLegacyFingerprintManager) {
            super.prepareForAuthentication_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(options.getSensorId());
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for prepareForAuthentication");
                return;
            }
            int statsClient = isForLegacyFingerprintManager ? 3 : 2;
            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().prepareForAuthPreOperation(token, options.getOpPackageName());
            provider.scheduleAuthenticate(token, operationId, cookie, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(sensorReceiver), options, requestId, true, statsClient, allowBackgroundAuthentication);
        }

        public void startPreparedClient(int sensorId, int cookie) {
            super.startPreparedClient_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for startPreparedClient");
            } else {
                provider.startPreparedClient(sensorId, cookie);
            }
        }

        public void cancelAuthentication(android.os.IBinder token, java.lang.String opPackageName, java.lang.String attributionTag, long requestId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (!com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.canUseFingerprint(opPackageName, attributionTag, true, callingUid, callingPid, callingUserId) && !com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().skipAuthWithPrompt(opPackageName)) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "cancelAuthentication rejecting package: " + opPackageName);
                com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().setSkipAuthPrompt(false, opPackageName);
                return;
            }
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for cancelAuthentication");
            } else {
                com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().setSkipAuthPrompt(false, opPackageName);
                ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).cancelAuthentication(((java.lang.Integer) provider.first).intValue(), token, requestId);
            }
        }

        public void cancelFingerprintDetect(android.os.IBinder token, java.lang.String opPackageName, long requestId) {
            super.cancelFingerprintDetect_enforcePermission();
            if (!com.android.server.biometrics.Utils.isKeyguard(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), opPackageName)) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "cancelFingerprintDetect called from non-sysui package: " + opPackageName);
                return;
            }
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for cancelFingerprintDetect");
            } else {
                ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).cancelAuthentication(((java.lang.Integer) provider.first).intValue(), token, requestId);
            }
        }

        public void cancelAuthenticationFromService(int sensorId, android.os.IBinder token, java.lang.String opPackageName, long requestId) {
            super.cancelAuthenticationFromService_enforcePermission();
            android.util.Slog.d(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "cancelAuthenticationFromService, sensorId: " + sensorId);
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for cancelAuthenticationFromService");
            } else {
                com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().setSkipAuthPrompt(false, opPackageName);
                provider.cancelAuthentication(sensorId, token, requestId);
            }
        }

        public void remove(android.os.IBinder token, int fingerId, int userId, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, java.lang.String opPackageName) {
            super.remove_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for remove");
            } else {
                ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).scheduleRemove(((java.lang.Integer) provider.first).intValue(), token, receiver, fingerId, userId, opPackageName);
            }
        }

        public void removeAll(android.os.IBinder token, int userId, final android.hardware.fingerprint.IFingerprintServiceReceiver receiver, java.lang.String opPackageName) {
            super.removeAll_enforcePermission();
            android.hardware.fingerprint.IFingerprintServiceReceiver iFingerprintServiceReceiver = new android.hardware.fingerprint.FingerprintServiceReceiver() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService.1.2
                final int numSensors;
                int sensorsFinishedRemoving = 0;

                {
                    this.numSensors = com.android.server.biometrics.sensors.fingerprint.FingerprintService.AnonymousClass1.this.getSensorPropertiesInternal(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext().getOpPackageName()).size();
                }

                public void onRemoved(android.hardware.fingerprint.Fingerprint fp, int remaining) throws android.os.RemoteException {
                    if (remaining == 0) {
                        this.sensorsFinishedRemoving++;
                        android.util.Slog.d(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "sensorsFinishedRemoving: " + this.sensorsFinishedRemoving + ", numSensors: " + this.numSensors);
                        if (this.sensorsFinishedRemoving == this.numSensors) {
                            receiver.onRemoved((android.hardware.fingerprint.Fingerprint) null, 0);
                        }
                    }
                }
            };
            for (com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider : com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviders()) {
                java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> props = provider.getSensorProperties();
                for (android.hardware.fingerprint.FingerprintSensorPropertiesInternal prop : props) {
                    provider.scheduleRemoveAll(prop.sensorId, token, iFingerprintServiceReceiver, userId, opPackageName);
                }
            }
        }

        public void addLockoutResetCallback(android.hardware.biometrics.IBiometricServiceLockoutResetCallback callback, java.lang.String opPackageName) {
            super.addLockoutResetCallback_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mLockoutResetDispatcher.addCallback(callback, opPackageName);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            new com.android.server.biometrics.sensors.fingerprint.FingerprintShellCommand(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), com.android.server.biometrics.sensors.fingerprint.FingerprintService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, pw)) {
                return;
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                if (args.length > 1 && "--proto".equals(args[0]) && "--state".equals(args[1])) {
                    android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
                    for (com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider : com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviders()) {
                        java.util.Iterator<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> it = provider.getSensorProperties().iterator();
                        while (it.hasNext()) {
                            provider.dumpProtoState(it.next().sensorId, proto, false);
                        }
                    }
                    proto.flush();
                } else if (args.length > 0 && "--proto".equals(args[0])) {
                    for (com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider2 : com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviders()) {
                        java.util.Iterator<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> it2 = provider2.getSensorProperties().iterator();
                        while (it2.hasNext()) {
                            provider2.dumpProtoMetrics(it2.next().sensorId, fd);
                        }
                    }
                } else {
                    for (com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider3 : com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviders()) {
                        for (android.hardware.fingerprint.FingerprintSensorPropertiesInternal props : provider3.getSensorProperties()) {
                            pw.println("Dumping for sensorId: " + props.sensorId + ", provider: " + provider3.getClass().getSimpleName());
                            pw.println("Fps state: " + com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mBiometricStateCallback.getBiometricState());
                            provider3.dumpInternal(props.sensorId, pw);
                            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().dumpInternal(provider3, fd, pw, args);
                            pw.println();
                        }
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isHardwareDetectedDeprecated(java.lang.String opPackageName, java.lang.String attributionTag) {
            if (!com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.canUseFingerprint(opPackageName, attributionTag, false, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), android.os.UserHandle.getCallingUserId())) {
                return false;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
                if (provider == null) {
                    android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for isHardwareDetectedDeprecated, caller: " + opPackageName);
                    return false;
                }
                return ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).isHardwareDetected(((java.lang.Integer) provider.first).intValue());
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean isHardwareDetected(int sensorId, java.lang.String opPackageName) {
            super.isHardwareDetected_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for isHardwareDetected, caller: " + opPackageName);
                return false;
            }
            return provider.isHardwareDetected(sensorId);
        }

        public void rename(int fingerId, int userId, java.lang.String name) {
            super.rename_enforcePermission();
            if (!com.android.server.biometrics.Utils.isCurrentUserOrProfile(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), userId)) {
                return;
            }
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for rename");
            } else {
                ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).rename(((java.lang.Integer) provider.first).intValue(), fingerId, userId, name);
            }
        }

        public java.util.List<android.hardware.fingerprint.Fingerprint> getEnrolledFingerprints(int userId, java.lang.String opPackageName, java.lang.String attributionTag) {
            if (!com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.canUseFingerprint(opPackageName, attributionTag, false, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), android.os.UserHandle.getCallingUserId())) {
                return java.util.Collections.emptyList();
            }
            if (userId != android.os.UserHandle.getCallingUserId()) {
                com.android.server.biometrics.Utils.checkPermission(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), "android.permission.INTERACT_ACROSS_USERS");
            }
            return com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getEnrolledFingerprintsDeprecated(userId, opPackageName);
        }

        public boolean hasEnrolledFingerprintsDeprecated(int userId, java.lang.String opPackageName, java.lang.String attributionTag) {
            if (!com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.canUseFingerprint(opPackageName, attributionTag, false, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), android.os.UserHandle.getCallingUserId())) {
                return false;
            }
            if (userId != android.os.UserHandle.getCallingUserId()) {
                com.android.server.biometrics.Utils.checkPermission(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getContext(), "android.permission.INTERACT_ACROSS_USERS");
            }
            return !com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getEnrolledFingerprintsDeprecated(userId, opPackageName).isEmpty();
        }

        public boolean hasEnrolledFingerprints(int sensorId, int userId, java.lang.String opPackageName) {
            super.hasEnrolledFingerprints_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider != null) {
                return provider.getEnrolledFingerprints(sensorId, com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getWrapper().getExtImpl().changeUserIdIfNeeded(userId)).size() > 0;
            }
            android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for hasEnrolledFingerprints, caller: " + opPackageName);
            return false;
        }

        public int getLockoutModeForUser(int sensorId, int userId) {
            super.getLockoutModeForUser_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for getLockoutModeForUser");
                return 0;
            }
            return provider.getLockoutModeForUser(sensorId, userId);
        }

        public void invalidateAuthenticatorId(int sensorId, int userId, android.hardware.biometrics.IInvalidationCallback callback) {
            super.invalidateAuthenticatorId_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for invalidateAuthenticatorId");
            } else {
                provider.scheduleInvalidateAuthenticatorId(sensorId, userId, callback);
            }
        }

        public long getAuthenticatorId(int sensorId, int userId) {
            super.getAuthenticatorId_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for getAuthenticatorId");
                return 0L;
            }
            return provider.getAuthenticatorId(sensorId, userId);
        }

        public void resetLockout(android.os.IBinder token, int sensorId, int userId, byte[] hardwareAuthToken, java.lang.String opPackageName) {
            super.resetLockout_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for resetLockout, caller: " + opPackageName);
            } else {
                provider.scheduleResetLockout(sensorId, userId, hardwareAuthToken);
            }
        }

        public boolean isClientActive() {
            super.isClientActive_enforcePermission();
            return com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mGestureAvailabilityDispatcher.isAnySensorActive();
        }

        public void addClientActiveCallback(android.hardware.fingerprint.IFingerprintClientActiveCallback callback) {
            super.addClientActiveCallback_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mGestureAvailabilityDispatcher.registerCallback(callback);
        }

        public void removeClientActiveCallback(android.hardware.fingerprint.IFingerprintClientActiveCallback callback) {
            super.removeClientActiveCallback_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mGestureAvailabilityDispatcher.removeCallback(callback);
        }

        public void registerAuthenticators(final android.hardware.fingerprint.FingerprintSensorConfigurations fingerprintSensorConfigurations) {
            super.registerAuthenticators_enforcePermission();
            if (!fingerprintSensorConfigurations.hasSensorConfigurations()) {
                android.util.Slog.d(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "No fingerprint sensors available.");
            } else {
                com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.registerAll(new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$1$$ExternalSyntheticLambda1
                    @Override // java.util.function.Supplier
                    public final java.lang.Object get() {
                        return this.f$0.lambda$registerAuthenticators$1(fingerprintSensorConfigurations);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.util.List lambda$registerAuthenticators$1(android.hardware.fingerprint.FingerprintSensorConfigurations fingerprintSensorConfigurations) {
            return com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.getProviders(fingerprintSensorConfigurations);
        }

        public void addAuthenticatorsRegisteredCallback(android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback callback) {
            super.addAuthenticatorsRegisteredCallback_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.addAllRegisteredCallback(callback);
        }

        public void registerAuthenticationStateListener(android.hardware.biometrics.AuthenticationStateListener listener) {
            super.registerAuthenticationStateListener_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mAuthenticationStateListeners.registerAuthenticationStateListener(listener);
        }

        public void unregisterAuthenticationStateListener(android.hardware.biometrics.AuthenticationStateListener listener) {
            super.unregisterAuthenticationStateListener_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mAuthenticationStateListeners.unregisterAuthenticationStateListener(listener);
        }

        public void registerBiometricStateListener(android.hardware.biometrics.IBiometricStateListener listener) {
            super.registerBiometricStateListener_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mBiometricStateCallback.registerBiometricStateListener(listener);
        }

        public void onPointerDown(long requestId, int sensorId, android.hardware.biometrics.fingerprint.PointerContext pc) {
            super.onPointerDown_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "No matching provider for onFingerDown, sensorId: " + sensorId);
            } else {
                provider.onPointerDown(requestId, sensorId, pc);
            }
        }

        public void onPointerUp(long requestId, int sensorId, android.hardware.biometrics.fingerprint.PointerContext pc) {
            super.onPointerUp_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "No matching provider for onFingerUp, sensorId: " + sensorId);
            } else {
                provider.onPointerUp(requestId, sensorId, pc);
            }
        }

        public void onUdfpsUiEvent(int event, long requestId, int sensorId) {
            super.onUdfpsUiEvent_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "No matching provider for onUdfpsUiEvent, sensorId: " + sensorId);
            } else {
                provider.onUdfpsUiEvent(event, requestId, sensorId);
            }
        }

        public void setIgnoreDisplayTouches(long requestId, int sensorId, boolean ignoreTouches) {
            super.setIgnoreDisplayTouches_enforcePermission();
            com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "No matching provider for setIgnoreDisplayTouches, sensorId: " + sensorId);
            } else {
                provider.setIgnoreDisplayTouches(requestId, sensorId, ignoreTouches);
            }
        }

        public void setUdfpsOverlayController(android.hardware.fingerprint.IUdfpsOverlayController controller) {
            super.setUdfpsOverlayController_enforcePermission();
            for (com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider : com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviders()) {
                provider.setUdfpsOverlayController(controller);
            }
        }

        public void onPowerPressed() {
            super.onPowerPressed_enforcePermission();
            for (com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider : com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviders()) {
                provider.onPowerPressed();
            }
        }

        public void scheduleWatchdog() {
            super.scheduleWatchdog_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Null provider for scheduling watchdog");
            } else {
                ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).scheduleWatchdog(((java.lang.Integer) provider.first).intValue());
            }
        }
    }

    public FingerprintService(android.content.Context context) {
        this(context, com.android.server.biometrics.log.BiometricContext.getInstance(context), new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return android.hardware.biometrics.IBiometricService.Stub.asInterface(android.os.ServiceManager.getService("biometric"));
            }
        }, new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return android.os.ServiceManager.getDeclaredInstances(android.hardware.biometrics.fingerprint.IFingerprint.DESCRIPTOR);
            }
        }, null, null);
    }

    FingerprintService(android.content.Context context, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.function.Supplier<android.hardware.biometrics.IBiometricService> biometricServiceSupplier, java.util.function.Supplier<java.lang.String[]> aidlInstanceNameSupplier, java.util.function.Function<java.lang.String, com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider> fingerprintProvider, com.android.server.biometrics.sensors.fingerprint.FingerprintService.FingerprintProviderFunction fingerprintProviderFunction) {
        com.android.server.biometrics.sensors.fingerprint.FingerprintService.FingerprintProviderFunction fingerprintProviderFunction2;
        super(context);
        this.mServiceWrapper = new com.android.server.biometrics.sensors.fingerprint.FingerprintService.AnonymousClass1();
        this.mOplusFingerprintServiceWrapper = new com.android.server.biometrics.sensors.fingerprint.FingerprintService.OplusFingerprintServiceWrapper();
        this.mIOplusFingerprintServiceExt = (com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceExt.class).base(this).create();
        this.mBiometricContext = biometricContext;
        this.mAidlInstanceNameSupplier = aidlInstanceNameSupplier;
        this.mAppOps = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        this.mGestureAvailabilityDispatcher = new com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher();
        this.mLockoutResetDispatcher = new com.android.server.biometrics.sensors.LockoutResetDispatcher(context);
        this.mLockPatternUtils = new com.android.internal.widget.LockPatternUtils(context);
        this.mBiometricStateCallback = new com.android.server.biometrics.sensors.BiometricStateCallback<>(android.os.UserManager.get(context));
        this.mAuthenticationStateListeners = new com.android.server.biometrics.sensors.AuthenticationStateListeners();
        this.mFingerprintProvider = fingerprintProvider != null ? fingerprintProvider : new java.util.function.Function() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$new$2((java.lang.String) obj);
            }
        };
        if (fingerprintProviderFunction != null) {
            fingerprintProviderFunction2 = fingerprintProviderFunction;
        } else {
            fingerprintProviderFunction2 = new com.android.server.biometrics.sensors.fingerprint.FingerprintService.FingerprintProviderFunction() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService$$ExternalSyntheticLambda1
                @Override // com.android.server.biometrics.sensors.fingerprint.FingerprintService.FingerprintProviderFunction
                public final com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider getFingerprintProvider(android.util.Pair pair, boolean z) {
                    return this.f$0.lambda$new$3(pair, z);
                }
            };
        }
        this.mFingerprintProviderFunction = fingerprintProviderFunction2;
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        getWrapper().getExtImpl().setBinderExtension(this.mServiceWrapper);
        this.mRegistry = new com.android.server.biometrics.sensors.fingerprint.FingerprintServiceRegistry(this.mServiceWrapper, biometricServiceSupplier);
        this.mRegistry.addAllRegisteredCallback(new android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService.2
            public void onAllAuthenticatorsRegistered(java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> sensors) {
                com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mBiometricStateCallback.start(com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviders());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider lambda$new$2(java.lang.String name) {
        java.lang.String fqName = android.hardware.biometrics.fingerprint.IFingerprint.DESCRIPTOR + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + name;
        android.hardware.biometrics.fingerprint.IFingerprint fp = android.hardware.biometrics.fingerprint.IFingerprint.Stub.asInterface(android.os.Binder.allowBlocking(android.os.ServiceManager.waitForDeclaredService(fqName)));
        if (fp == null) {
            android.util.Slog.e(TAG, "Unable to get declared service: " + fqName);
            return null;
        }
        try {
            return new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider(getContext(), this.mBiometricStateCallback, this.mAuthenticationStateListeners, fp.getSensorProps(), name, this.mLockoutResetDispatcher, this.mGestureAvailabilityDispatcher, this.mBiometricContext, true);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception in getSensorProps: " + fqName);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider lambda$new$3(android.util.Pair filteredSensorProps, boolean resetLockoutRequiresHardwareAuthToken) {
        return new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider(getContext(), this.mBiometricStateCallback, this.mAuthenticationStateListeners, (android.hardware.biometrics.fingerprint.SensorProps[]) filteredSensorProps.second, (java.lang.String) filteredSensorProps.first, this.mLockoutResetDispatcher, this.mGestureAvailabilityDispatcher, this.mBiometricContext, resetLockoutRequiresHardwareAuthToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<com.android.server.biometrics.sensors.fingerprint.ServiceProvider> getProviders(android.hardware.fingerprint.FingerprintSensorConfigurations fingerprintSensorConfigurations) {
        java.util.List<com.android.server.biometrics.sensors.fingerprint.ServiceProvider> providers = new java.util.ArrayList<>();
        android.util.Pair<java.lang.String, android.hardware.biometrics.fingerprint.SensorProps[]> filteredSensorProps = filterAvailableHalInstances(fingerprintSensorConfigurations);
        providers.add(this.mFingerprintProviderFunction.getFingerprintProvider(filteredSensorProps, fingerprintSensorConfigurations.getResetLockoutRequiresHardwareAuthToken()));
        getWrapper().getExtImpl().onSystemReady();
        return providers;
    }

    private android.util.Pair<java.lang.String, android.hardware.biometrics.fingerprint.SensorProps[]> filterAvailableHalInstances(android.hardware.fingerprint.FingerprintSensorConfigurations fingerprintSensorConfigurations) {
        java.lang.String notAVirtualInstance;
        java.lang.String finalSensorInstance = fingerprintSensorConfigurations.getSensorInstance();
        if (fingerprintSensorConfigurations.isSingleSensorConfigurationPresent()) {
            return new android.util.Pair<>(finalSensorInstance, fingerprintSensorConfigurations.getSensorPropForInstance(finalSensorInstance));
        }
        boolean isVirtualHalPresent = fingerprintSensorConfigurations.doesInstanceExist("virtual");
        if (com.android.server.biometrics.Utils.isFingerprintVirtualEnabled(getContext())) {
            if (isVirtualHalPresent) {
                return new android.util.Pair<>("virtual", fingerprintSensorConfigurations.getSensorPropForInstance("virtual"));
            }
            android.util.Slog.e(TAG, "Could not find virtual interface while it is enabled");
            return new android.util.Pair<>(finalSensorInstance, fingerprintSensorConfigurations.getSensorPropForInstance(finalSensorInstance));
        }
        if (isVirtualHalPresent && (notAVirtualInstance = fingerprintSensorConfigurations.getSensorNameNotForInstance("virtual")) != null) {
            return new android.util.Pair<>(notAVirtualInstance, fingerprintSensorConfigurations.getSensorPropForInstance(notAVirtualInstance));
        }
        return new android.util.Pair<>(finalSensorInstance, fingerprintSensorConfigurations.getSensorPropForInstance(finalSensorInstance));
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("fingerprint", this.mServiceWrapper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.hardware.fingerprint.Fingerprint> getEnrolledFingerprintsDeprecated(int userId, java.lang.String opPackageName) {
        android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = this.mRegistry.getSingleProvider();
        if (provider == null) {
            android.util.Slog.w(TAG, "Null provider for getEnrolledFingerprintsDeprecated, caller: " + opPackageName);
            return java.util.Collections.emptyList();
        }
        return ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).getEnrolledFingerprints(((java.lang.Integer) provider.first).intValue(), getWrapper().getExtImpl().changeUserIdIfNeeded(userId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canUseFingerprint(java.lang.String opPackageName, java.lang.String attributionTag, boolean requireForeground, int uid, int pid, int userId) {
        if (getWrapper().getExtImpl().isBiometricDisabled()) {
            android.util.Slog.w(TAG, "Rejecting " + opPackageName + "; enterprise customized version is disabled");
            return false;
        }
        if (getContext().checkCallingPermission("android.permission.USE_FINGERPRINT") != 0) {
            com.android.server.biometrics.Utils.checkPermission(getContext(), "android.permission.USE_BIOMETRIC");
        }
        if (android.os.Binder.getCallingUid() == 1000 || com.android.server.biometrics.Utils.isKeyguard(getContext(), opPackageName)) {
            return true;
        }
        if (!com.android.server.biometrics.Utils.isCurrentUserOrProfile(getContext(), userId)) {
            android.util.Slog.w(TAG, "Rejecting " + opPackageName + "; not a current user or profile");
            return false;
        }
        if (!checkAppOps(uid, opPackageName, attributionTag)) {
            android.util.Slog.w(TAG, "Rejecting " + opPackageName + "; permission denied");
            return false;
        }
        if (!requireForeground || com.android.server.biometrics.Utils.isForeground(uid, pid)) {
            return true;
        }
        android.util.Slog.w(TAG, "Rejecting " + opPackageName + "; not in foreground");
        return false;
    }

    private boolean checkAppOps(int uid, java.lang.String opPackageName, java.lang.String attributionTag) {
        if (this.mAppOps.noteOp(78, uid, opPackageName, attributionTag, (java.lang.String) null) != 0 && this.mAppOps.noteOp(55, uid, opPackageName, attributionTag, (java.lang.String) null) != 0) {
            return false;
        }
        return true;
    }

    void syncEnrollmentsNow() {
        com.android.server.biometrics.Utils.checkPermissionOrShell(getContext(), "android.permission.MANAGE_FINGERPRINT");
        if (com.android.server.biometrics.Utils.isFingerprintVirtualEnabled(getContext())) {
            android.util.Slog.i(TAG, "Sync virtual enrollments");
            int userId = android.app.ActivityManager.getCurrentUser();
            final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(this.mRegistry.getProviders().size());
            for (com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider : this.mRegistry.getProviders()) {
                for (android.hardware.fingerprint.FingerprintSensorPropertiesInternal props : provider.getSensorProperties()) {
                    provider.scheduleInternalCleanup(props.sensorId, userId, new com.android.server.biometrics.sensors.ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.FingerprintService.3
                        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
                        public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
                            latch.countDown();
                            if (!success) {
                                android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.FingerprintService.TAG, "Sync virtual enrollments failed");
                            }
                        }
                    }, true);
                }
            }
            try {
                latch.await(3L, java.util.concurrent.TimeUnit.SECONDS);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to wait for sync finishing", e);
            }
        }
    }

    void simulateVhalFingerDown() {
        if (com.android.server.biometrics.Utils.isFingerprintVirtualEnabled(getContext())) {
            android.util.Slog.i(TAG, "Simulate virtual HAL finger down event");
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = this.mRegistry.getSingleProvider();
            if (provider != null) {
                ((com.android.server.biometrics.sensors.fingerprint.ServiceProvider) provider.second).simulateVhalFingerDown(android.os.UserHandle.getCallingUserId(), ((java.lang.Integer) provider.first).intValue());
            }
        }
    }

    public com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper getWrapper() {
        return this.mOplusFingerprintServiceWrapper;
    }

    private class OplusFingerprintServiceWrapper implements com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper {
        private OplusFingerprintServiceWrapper() {
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper
        public com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceExt getExtImpl() {
            return com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mIOplusFingerprintServiceExt;
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper
        public com.android.server.biometrics.sensors.fingerprint.ServiceProvider getProviderForSensorWrapper(int sensorId) {
            return com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getProviderForSensor(sensorId);
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper
        public android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> getSingleProviderWrapper() {
            return com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.mRegistry.getSingleProvider();
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper
        public boolean canUseFingerprintWrapper(java.lang.String opPackageName, java.lang.String attributionTag, boolean requireForeground, int uid, int pid, int userId) {
            return com.android.server.biometrics.sensors.fingerprint.FingerprintService.this.canUseFingerprint(opPackageName, attributionTag, requireForeground, uid, pid, userId);
        }
    }

    void sendFingerprintReEnrollNotification() {
        com.android.server.biometrics.Utils.checkPermissionOrShell(getContext(), "android.permission.MANAGE_FINGERPRINT");
        if (android.os.Build.IS_DEBUGGABLE) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> provider = this.mRegistry.getSingleProvider();
                if (provider != null) {
                    com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider fingerprintProvider = (com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider) provider.second;
                    fingerprintProvider.sendFingerprintReEnrollNotification();
                } else {
                    android.util.Slog.w(TAG, "Null provider for notification");
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }
}
