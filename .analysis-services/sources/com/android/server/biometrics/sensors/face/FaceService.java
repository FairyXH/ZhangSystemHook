package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public class FaceService extends com.android.server.SystemService {
    protected static final java.lang.String TAG = "FaceService";
    private final java.util.function.Supplier<java.lang.String[]> mAidlInstanceNameSupplier;
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private final com.android.server.biometrics.sensors.BiometricStateCallback<com.android.server.biometrics.sensors.face.ServiceProvider, android.hardware.face.FaceSensorPropertiesInternal> mBiometricStateCallback;
    private final java.util.function.Function<java.lang.String, com.android.server.biometrics.sensors.face.aidl.FaceProvider> mFaceProvider;
    private final com.android.server.biometrics.sensors.face.FaceService.FaceProviderFunction mFaceProviderFunction;
    private com.android.server.biometrics.sensors.face.IFaceServiceExt mIFaceServiceExt;
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;
    private com.android.server.biometrics.sensors.face.IFaceServiceWrapper mOplusFaceServiceWrapper;
    private final com.android.server.biometrics.sensors.face.FaceServiceRegistry mRegistry;
    final com.android.server.biometrics.sensors.face.FaceService.FaceServiceWrapper mServiceWrapper;

    interface FaceProviderFunction {
        com.android.server.biometrics.sensors.face.aidl.FaceProvider getFaceProvider(android.util.Pair<java.lang.String, android.hardware.biometrics.face.SensorProps[]> pair, boolean z);
    }

    public static native android.os.NativeHandle acquireSurfaceHandle(android.view.Surface surface);

    public static native void releaseSurfaceHandle(android.os.NativeHandle nativeHandle);

    public final class FaceServiceWrapper extends android.hardware.face.IFaceService.Stub {
        public FaceServiceWrapper() {
        }

        public android.hardware.biometrics.ITestSession createTestSession(int sensorId, android.hardware.biometrics.ITestSessionCallback callback, java.lang.String opPackageName) {
            super.createTestSession_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for createTestSession, sensorId: " + sensorId);
                return null;
            }
            return provider.createTestSession(sensorId, callback, opPackageName);
        }

        public byte[] dumpSensorServiceStateProto(int sensorId, boolean clearSchedulerBuffer) {
            super.dumpSensorServiceStateProto_enforcePermission();
            android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider != null) {
                provider.dumpProtoState(sensorId, proto, clearSchedulerBuffer);
            }
            proto.flush();
            return proto.getBytes();
        }

        public java.util.List<android.hardware.face.FaceSensorPropertiesInternal> getSensorPropertiesInternal(java.lang.String opPackageName) {
            super.getSensorPropertiesInternal_enforcePermission();
            return com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getAllProperties();
        }

        public android.hardware.face.FaceSensorPropertiesInternal getSensorProperties(int sensorId, java.lang.String opPackageName) {
            super.getSensorProperties_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "No matching sensor for getSensorProperties, sensorId: " + sensorId + ", caller: " + opPackageName);
                return null;
            }
            return provider.getSensorProperties(sensorId);
        }

        public void generateChallenge(android.os.IBinder token, int sensorId, int userId, android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName) {
            super.generateChallenge_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "No matching sensor for generateChallenge, sensorId: " + sensorId);
            } else {
                provider.scheduleGenerateChallenge(sensorId, userId, token, receiver, opPackageName);
            }
        }

        public void revokeChallenge(android.os.IBinder token, int sensorId, int userId, java.lang.String opPackageName, long challenge) {
            super.revokeChallenge_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "No matching sensor for revokeChallenge, sensorId: " + sensorId);
            } else {
                provider.scheduleRevokeChallenge(sensorId, userId, token, opPackageName, challenge);
            }
        }

        public long enroll(int userId, android.os.IBinder token, byte[] hardwareAuthToken, android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName, int[] disabledFeatures, android.view.Surface previewSurface, boolean debugConsent, android.hardware.face.FaceEnrollOptions options) {
            super.enroll_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for enroll");
                return -1L;
            }
            com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().scheduleEnroll();
            return ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).scheduleEnroll(((java.lang.Integer) provider.first).intValue(), token, hardwareAuthToken, userId, receiver, opPackageName, disabledFeatures, previewSurface, debugConsent, options);
        }

        public void scheduleWatchdog() {
            super.scheduleWatchdog_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for scheduling watchdog");
            } else {
                ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).scheduleWatchdog(((java.lang.Integer) provider.first).intValue());
            }
        }

        public long enrollRemotely(int userId, android.os.IBinder token, byte[] hardwareAuthToken, android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName, int[] disabledFeatures) {
            super.enrollRemotely_enforcePermission();
            return -1L;
        }

        public void cancelEnrollment(android.os.IBinder token, long requestId) {
            super.cancelEnrollment_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for cancelEnrollment");
            } else {
                ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).cancelEnrollment(((java.lang.Integer) provider.first).intValue(), token, requestId);
            }
        }

        public long authenticate(android.os.IBinder token, long operationId, android.hardware.face.IFaceServiceReceiver receiver, android.hardware.face.FaceAuthenticateOptions options) {
            int statsClient;
            if (com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().isBiometricDisabled()) {
                return -1L;
            }
            super.authenticate_enforcePermission();
            java.lang.String opPackageName = options.getOpPackageName();
            if (com.android.server.biometrics.Utils.isKeyguard(com.android.server.biometrics.sensors.face.FaceService.this.getContext(), opPackageName)) {
                statsClient = 1;
            } else {
                statsClient = 0;
            }
            boolean isKeyguard = com.android.server.biometrics.Utils.isKeyguard(com.android.server.biometrics.sensors.face.FaceService.this.getContext(), opPackageName);
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for authenticate");
                return -1L;
            }
            options.setSensorId(((java.lang.Integer) provider.first).intValue());
            com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().authPreOperation(token, opPackageName);
            return ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).scheduleAuthenticate(token, operationId, 0, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), options, false, statsClient, isKeyguard);
        }

        public long detectFace(android.os.IBinder token, android.hardware.face.IFaceServiceReceiver receiver, android.hardware.face.FaceAuthenticateOptions options) {
            super.detectFace_enforcePermission();
            java.lang.String opPackageName = options.getOpPackageName();
            if (!com.android.server.biometrics.Utils.isKeyguard(com.android.server.biometrics.sensors.face.FaceService.this.getContext(), opPackageName)) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "detectFace called from non-sysui package: " + opPackageName);
                return -1L;
            }
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for detectFace");
                return -1L;
            }
            options.setSensorId(((java.lang.Integer) provider.first).intValue());
            return ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).scheduleFaceDetect(token, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), options, 1);
        }

        public void prepareForAuthentication(boolean requireConfirmation, android.os.IBinder token, long operationId, android.hardware.biometrics.IBiometricSensorReceiver sensorReceiver, android.hardware.face.FaceAuthenticateOptions options, long requestId, int cookie, boolean allowBackgroundAuthentication) {
            super.prepareForAuthentication_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(options.getSensorId());
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for prepareForAuthentication");
            } else {
                com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().authPreOperation(token, options.getOpPackageName());
                provider.scheduleAuthenticate(token, operationId, cookie, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(sensorReceiver), options, requestId, true, 2, allowBackgroundAuthentication);
            }
        }

        public void startPreparedClient(int sensorId, int cookie) {
            super.startPreparedClient_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for startPreparedClient");
            } else {
                provider.startPreparedClient(sensorId, cookie);
            }
        }

        public void cancelAuthentication(android.os.IBinder token, java.lang.String opPackageName, long requestId) {
            super.cancelAuthentication_enforcePermission();
            if (com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().isBiometricDisabled()) {
                return;
            }
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for cancelAuthentication");
            } else {
                ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).cancelAuthentication(((java.lang.Integer) provider.first).intValue(), token, requestId);
            }
        }

        public void cancelFaceDetect(android.os.IBinder token, java.lang.String opPackageName, long requestId) {
            super.cancelFaceDetect_enforcePermission();
            if (!com.android.server.biometrics.Utils.isKeyguard(com.android.server.biometrics.sensors.face.FaceService.this.getContext(), opPackageName)) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "cancelFaceDetect called from non-sysui package: " + opPackageName);
                return;
            }
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for cancelFaceDetect");
            } else {
                ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).cancelFaceDetect(((java.lang.Integer) provider.first).intValue(), token, requestId);
            }
        }

        public void cancelAuthenticationFromService(int sensorId, android.os.IBinder token, java.lang.String opPackageName, long requestId) {
            super.cancelAuthenticationFromService_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for cancelAuthenticationFromService");
            } else {
                provider.cancelAuthentication(sensorId, token, requestId);
            }
        }

        public void remove(android.os.IBinder token, int faceId, int userId, android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName) {
            super.remove_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for remove");
            } else {
                ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).scheduleRemove(((java.lang.Integer) provider.first).intValue(), token, faceId, userId, receiver, opPackageName);
            }
        }

        public void removeAll(android.os.IBinder token, int userId, final android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName) {
            super.removeAll_enforcePermission();
            android.hardware.face.IFaceServiceReceiver iFaceServiceReceiver = new android.hardware.face.FaceServiceReceiver() { // from class: com.android.server.biometrics.sensors.face.FaceService.FaceServiceWrapper.1
                final int numSensors;
                int sensorsFinishedRemoving = 0;

                {
                    this.numSensors = com.android.server.biometrics.sensors.face.FaceService.FaceServiceWrapper.this.getSensorPropertiesInternal(com.android.server.biometrics.sensors.face.FaceService.this.getContext().getOpPackageName()).size();
                }

                public void onRemoved(android.hardware.face.Face face, int remaining) throws android.os.RemoteException {
                    if (remaining == 0) {
                        this.sensorsFinishedRemoving++;
                        android.util.Slog.d(com.android.server.biometrics.sensors.face.FaceService.TAG, "sensorsFinishedRemoving: " + this.sensorsFinishedRemoving + ", numSensors: " + this.numSensors);
                        if (this.sensorsFinishedRemoving == this.numSensors) {
                            receiver.onRemoved((android.hardware.face.Face) null, 0);
                        }
                    }
                }

                public void onError(int error, int vendorCode) throws android.os.RemoteException {
                    receiver.onError(error, vendorCode);
                }
            };
            for (com.android.server.biometrics.sensors.face.ServiceProvider provider : com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviders()) {
                java.util.List<android.hardware.face.FaceSensorPropertiesInternal> props = provider.getSensorProperties();
                for (android.hardware.face.FaceSensorPropertiesInternal prop : props) {
                    provider.scheduleRemoveAll(prop.sensorId, token, userId, iFaceServiceReceiver, opPackageName);
                }
            }
        }

        public void addLockoutResetCallback(android.hardware.biometrics.IBiometricServiceLockoutResetCallback callback, java.lang.String opPackageName) {
            super.addLockoutResetCallback_enforcePermission();
            com.android.server.biometrics.sensors.face.FaceService.this.mLockoutResetDispatcher.addCallback(callback, opPackageName);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            new com.android.server.biometrics.sensors.face.FaceShellCommand(com.android.server.biometrics.sensors.face.FaceService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.biometrics.sensors.face.FaceService.this.getContext(), com.android.server.biometrics.sensors.face.FaceService.TAG, pw)) {
                return;
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                if (args.length > 1 && "--proto".equals(args[0]) && "--state".equals(args[1])) {
                    android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
                    for (com.android.server.biometrics.sensors.face.ServiceProvider provider : com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviders()) {
                        java.util.Iterator<android.hardware.face.FaceSensorPropertiesInternal> it = provider.getSensorProperties().iterator();
                        while (it.hasNext()) {
                            provider.dumpProtoState(it.next().sensorId, proto, false);
                        }
                    }
                    proto.flush();
                } else if (args.length > 0 && "--proto".equals(args[0])) {
                    for (com.android.server.biometrics.sensors.face.ServiceProvider provider2 : com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviders()) {
                        java.util.Iterator<android.hardware.face.FaceSensorPropertiesInternal> it2 = provider2.getSensorProperties().iterator();
                        while (it2.hasNext()) {
                            provider2.dumpProtoMetrics(it2.next().sensorId, fd);
                        }
                    }
                } else if (args.length > 1 && "--hal".equals(args[0])) {
                    for (com.android.server.biometrics.sensors.face.ServiceProvider provider3 : com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviders()) {
                        java.util.Iterator<android.hardware.face.FaceSensorPropertiesInternal> it3 = provider3.getSensorProperties().iterator();
                        while (it3.hasNext()) {
                            provider3.dumpHal(it3.next().sensorId, fd, (java.lang.String[]) java.util.Arrays.copyOfRange(args, 1, args.length, args.getClass()));
                        }
                    }
                } else {
                    for (com.android.server.biometrics.sensors.face.ServiceProvider provider4 : com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviders()) {
                        for (android.hardware.face.FaceSensorPropertiesInternal props : provider4.getSensorProperties()) {
                            pw.println("Dumping for sensorId: " + props.sensorId + ", provider: " + provider4.getClass().getSimpleName());
                            provider4.dumpInternal(props.sensorId, pw);
                            com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().dumpInternal(provider4, pw, args);
                            pw.println();
                        }
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isHardwareDetected(int sensorId, java.lang.String opPackageName) {
            super.isHardwareDetected_enforcePermission();
            if (com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().isBiometricDisabled()) {
                return false;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
                if (provider == null) {
                    android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for isHardwareDetected, caller: " + opPackageName);
                    return false;
                }
                return provider.isHardwareDetected(sensorId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public java.util.List<android.hardware.face.Face> getEnrolledFaces(int sensorId, int userId, java.lang.String opPackageName) {
            super.getEnrolledFaces_enforcePermission();
            if (com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().isBiometricDisabled()) {
                return java.util.Collections.emptyList();
            }
            if (userId != android.os.UserHandle.getCallingUserId()) {
                com.android.server.biometrics.Utils.checkPermission(com.android.server.biometrics.sensors.face.FaceService.this.getContext(), "android.permission.INTERACT_ACROSS_USERS");
            }
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for getEnrolledFaces, caller: " + opPackageName);
                return java.util.Collections.emptyList();
            }
            return com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().getEnrolledFacesExcludePalms(provider.getEnrolledFaces(sensorId, userId));
        }

        public boolean hasEnrolledFaces(int sensorId, int userId, java.lang.String opPackageName) {
            super.hasEnrolledFaces_enforcePermission();
            if (com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().isBiometricDisabled()) {
                return false;
            }
            if (userId != android.os.UserHandle.getCallingUserId()) {
                com.android.server.biometrics.Utils.checkPermission(com.android.server.biometrics.sensors.face.FaceService.this.getContext(), "android.permission.INTERACT_ACROSS_USERS");
            }
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider != null) {
                return com.android.server.biometrics.sensors.face.FaceService.this.getWrapper().getExtImpl().getEnrolledFacesExcludePalms(provider.getEnrolledFaces(sensorId, userId)).size() > 0;
            }
            android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for hasEnrolledFaces, caller: " + opPackageName);
            return false;
        }

        public int getLockoutModeForUser(int sensorId, int userId) {
            super.getLockoutModeForUser_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for getLockoutModeForUser");
                return 0;
            }
            return provider.getLockoutModeForUser(sensorId, userId);
        }

        public void invalidateAuthenticatorId(int sensorId, int userId, android.hardware.biometrics.IInvalidationCallback callback) {
            super.invalidateAuthenticatorId_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for invalidateAuthenticatorId");
            } else {
                provider.scheduleInvalidateAuthenticatorId(sensorId, userId, callback);
            }
        }

        public long getAuthenticatorId(int sensorId, int userId) {
            super.getAuthenticatorId_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for getAuthenticatorId");
                return 0L;
            }
            return provider.getAuthenticatorId(sensorId, userId);
        }

        public void resetLockout(android.os.IBinder token, int sensorId, int userId, byte[] hardwareAuthToken, java.lang.String opPackageName) {
            super.resetLockout_enforcePermission();
            com.android.server.biometrics.sensors.face.ServiceProvider provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for resetLockout, caller: " + opPackageName);
            } else {
                provider.scheduleResetLockout(sensorId, userId, hardwareAuthToken);
            }
        }

        public void setFeature(android.os.IBinder token, int userId, int feature, boolean enabled, byte[] hardwareAuthToken, android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName) {
            super.setFeature_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for setFeature");
            } else {
                ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).scheduleSetFeature(((java.lang.Integer) provider.first).intValue(), token, userId, feature, enabled, hardwareAuthToken, receiver, opPackageName);
            }
        }

        public void getFeature(android.os.IBinder token, int userId, int feature, android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName) {
            super.getFeature_enforcePermission();
            android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
            if (provider == null) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.FaceService.TAG, "Null provider for getFeature");
            } else {
                ((com.android.server.biometrics.sensors.face.ServiceProvider) provider.second).scheduleGetFeature(((java.lang.Integer) provider.first).intValue(), token, userId, feature, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), opPackageName);
            }
        }

        public void registerAuthenticators(final android.hardware.face.FaceSensorConfigurations faceSensorConfigurations) {
            super.registerAuthenticators_enforcePermission();
            if (!faceSensorConfigurations.hasSensorConfigurations()) {
                android.util.Slog.d(com.android.server.biometrics.sensors.face.FaceService.TAG, "No face sensors to register.");
            } else {
                com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.registerAll(new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.face.FaceService$FaceServiceWrapper$$ExternalSyntheticLambda0
                    @Override // java.util.function.Supplier
                    public final java.lang.Object get() {
                        return this.f$0.lambda$registerAuthenticators$0(faceSensorConfigurations);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: getProviders, reason: merged with bridge method [inline-methods] */
        public java.util.List<com.android.server.biometrics.sensors.face.ServiceProvider> lambda$registerAuthenticators$0(android.hardware.face.FaceSensorConfigurations faceSensorConfigurations) {
            java.util.List<com.android.server.biometrics.sensors.face.ServiceProvider> providers = new java.util.ArrayList<>();
            android.util.Pair<java.lang.String, android.hardware.biometrics.face.SensorProps[]> filteredSensorProps = filterAvailableHalInstances(faceSensorConfigurations);
            providers.add(com.android.server.biometrics.sensors.face.FaceService.this.mFaceProviderFunction.getFaceProvider(filteredSensorProps, faceSensorConfigurations.getResetLockoutRequiresChallenge()));
            return providers;
        }

        private android.util.Pair<java.lang.String, android.hardware.biometrics.face.SensorProps[]> filterAvailableHalInstances(android.hardware.face.FaceSensorConfigurations faceSensorConfigurations) {
            java.lang.String notAVirtualInstance;
            java.lang.String finalSensorInstance = faceSensorConfigurations.getSensorInstance();
            if (faceSensorConfigurations.isSingleSensorConfigurationPresent()) {
                return new android.util.Pair<>(finalSensorInstance, faceSensorConfigurations.getSensorPropForInstance(finalSensorInstance));
            }
            boolean isVirtualHalPresent = faceSensorConfigurations.doesInstanceExist("virtual");
            if (com.android.server.biometrics.Flags.faceVhalFeature() && com.android.server.biometrics.Utils.isFaceVirtualEnabled(com.android.server.biometrics.sensors.face.FaceService.this.getContext())) {
                if (isVirtualHalPresent) {
                    return new android.util.Pair<>("virtual", faceSensorConfigurations.getSensorPropForInstance("virtual"));
                }
                android.util.Slog.e(com.android.server.biometrics.sensors.face.FaceService.TAG, "Could not find virtual interface while it is enabled");
                return new android.util.Pair<>(finalSensorInstance, faceSensorConfigurations.getSensorPropForInstance(finalSensorInstance));
            }
            if (isVirtualHalPresent && (notAVirtualInstance = faceSensorConfigurations.getSensorNameNotForInstance("virtual")) != null) {
                return new android.util.Pair<>(notAVirtualInstance, faceSensorConfigurations.getSensorPropForInstance(notAVirtualInstance));
            }
            return new android.util.Pair<>(finalSensorInstance, faceSensorConfigurations.getSensorPropForInstance(finalSensorInstance));
        }

        public void addAuthenticatorsRegisteredCallback(android.hardware.face.IFaceAuthenticatorsRegisteredCallback callback) {
            com.android.server.biometrics.Utils.checkPermission(com.android.server.biometrics.sensors.face.FaceService.this.getContext(), "android.permission.USE_BIOMETRIC_INTERNAL");
            com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.addAllRegisteredCallback(callback);
        }

        public void registerBiometricStateListener(android.hardware.biometrics.IBiometricStateListener listener) {
            com.android.server.biometrics.sensors.face.FaceService.this.mBiometricStateCallback.registerBiometricStateListener(listener);
        }

        public void registerAuthenticationStateListener(android.hardware.biometrics.AuthenticationStateListener listener) {
            super.registerAuthenticationStateListener_enforcePermission();
            com.android.server.biometrics.sensors.face.FaceService.this.mAuthenticationStateListeners.registerAuthenticationStateListener(listener);
        }

        public void unregisterAuthenticationStateListener(android.hardware.biometrics.AuthenticationStateListener listener) {
            super.unregisterAuthenticationStateListener_enforcePermission();
            com.android.server.biometrics.sensors.face.FaceService.this.mAuthenticationStateListeners.unregisterAuthenticationStateListener(listener);
        }
    }

    public FaceService(android.content.Context context) {
        this(context, null, new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.face.FaceService$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return android.hardware.biometrics.IBiometricService.Stub.asInterface(android.os.ServiceManager.getService("biometric"));
            }
        }, null, new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.face.FaceService$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return android.os.ServiceManager.getDeclaredInstances(android.hardware.biometrics.face.IFace.DESCRIPTOR);
            }
        });
    }

    FaceService(android.content.Context context, com.android.server.biometrics.sensors.face.FaceService.FaceProviderFunction faceProviderFunction, java.util.function.Supplier<android.hardware.biometrics.IBiometricService> biometricServiceSupplier, java.util.function.Function<java.lang.String, com.android.server.biometrics.sensors.face.aidl.FaceProvider> faceProvider, java.util.function.Supplier<java.lang.String[]> aidlInstanceNameSupplier) {
        super(context);
        this.mOplusFaceServiceWrapper = new com.android.server.biometrics.sensors.face.FaceService.OplusFaceServiceWrapper();
        this.mIFaceServiceExt = (com.android.server.biometrics.sensors.face.IFaceServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.face.IFaceServiceExt.class).base(this).create();
        this.mServiceWrapper = new com.android.server.biometrics.sensors.face.FaceService.FaceServiceWrapper();
        this.mLockoutResetDispatcher = new com.android.server.biometrics.sensors.LockoutResetDispatcher(context);
        this.mLockPatternUtils = new com.android.internal.widget.LockPatternUtils(context);
        this.mBiometricStateCallback = new com.android.server.biometrics.sensors.BiometricStateCallback<>(android.os.UserManager.get(context));
        this.mAuthenticationStateListeners = new com.android.server.biometrics.sensors.AuthenticationStateListeners();
        this.mRegistry = new com.android.server.biometrics.sensors.face.FaceServiceRegistry(this.mServiceWrapper, biometricServiceSupplier);
        this.mRegistry.addAllRegisteredCallback(new android.hardware.face.IFaceAuthenticatorsRegisteredCallback.Stub() { // from class: com.android.server.biometrics.sensors.face.FaceService.1
            public void onAllAuthenticatorsRegistered(java.util.List<android.hardware.face.FaceSensorPropertiesInternal> sensors) {
                com.android.server.biometrics.sensors.face.FaceService.this.mBiometricStateCallback.start(com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviders());
            }
        });
        this.mAidlInstanceNameSupplier = aidlInstanceNameSupplier;
        this.mFaceProvider = faceProvider != null ? faceProvider : new java.util.function.Function() { // from class: com.android.server.biometrics.sensors.face.FaceService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$new$2((java.lang.String) obj);
            }
        };
        this.mFaceProviderFunction = faceProviderFunction != null ? faceProviderFunction : new com.android.server.biometrics.sensors.face.FaceService.FaceProviderFunction() { // from class: com.android.server.biometrics.sensors.face.FaceService$$ExternalSyntheticLambda1
            @Override // com.android.server.biometrics.sensors.face.FaceService.FaceProviderFunction
            public final com.android.server.biometrics.sensors.face.aidl.FaceProvider getFaceProvider(android.util.Pair pair, boolean z) {
                return this.f$0.lambda$new$3(pair, z);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.biometrics.sensors.face.aidl.FaceProvider lambda$new$2(java.lang.String name) {
        java.lang.String fqName = android.hardware.biometrics.face.IFace.DESCRIPTOR + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + name;
        android.hardware.biometrics.face.IFace face = android.hardware.biometrics.face.IFace.Stub.asInterface(android.os.Binder.allowBlocking(android.os.ServiceManager.waitForDeclaredService(fqName)));
        if (face == null) {
            android.util.Slog.e(TAG, "Unable to get declared service: " + fqName);
            return null;
        }
        try {
            android.hardware.biometrics.face.SensorProps[] props = face.getSensorProps();
            return new com.android.server.biometrics.sensors.face.aidl.FaceProvider(getContext(), this.mBiometricStateCallback, this.mAuthenticationStateListeners, props, name, this.mLockoutResetDispatcher, com.android.server.biometrics.log.BiometricContext.getInstance(getContext()), false);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception in getSensorProps: " + fqName);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.biometrics.sensors.face.aidl.FaceProvider lambda$new$3(android.util.Pair filteredSensorProps, boolean resetLockoutRequiresChallenge) {
        return new com.android.server.biometrics.sensors.face.aidl.FaceProvider(getContext(), this.mBiometricStateCallback, this.mAuthenticationStateListeners, (android.hardware.biometrics.face.SensorProps[]) filteredSensorProps.second, (java.lang.String) filteredSensorProps.first, this.mLockoutResetDispatcher, com.android.server.biometrics.log.BiometricContext.getInstance(getContext()), resetLockoutRequiresChallenge);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        getWrapper().getExtImpl().init();
        publishBinderService("face", this.mServiceWrapper);
    }

    void syncEnrollmentsNow() {
        com.android.server.biometrics.Utils.checkPermissionOrShell(getContext(), "android.permission.MANAGE_FACE");
        if (com.android.server.biometrics.Flags.faceVhalFeature() && com.android.server.biometrics.Utils.isFaceVirtualEnabled(getContext())) {
            android.util.Slog.i(TAG, "Sync virtual enrollments");
            int userId = android.app.ActivityManager.getCurrentUser();
            for (com.android.server.biometrics.sensors.face.ServiceProvider provider : this.mRegistry.getProviders()) {
                for (android.hardware.face.FaceSensorPropertiesInternal props : provider.getSensorProperties()) {
                    provider.scheduleInternalCleanup(props.sensorId, userId, null, true);
                }
            }
        }
    }

    public com.android.server.biometrics.sensors.face.IFaceServiceWrapper getWrapper() {
        return this.mOplusFaceServiceWrapper;
    }

    private class OplusFaceServiceWrapper implements com.android.server.biometrics.sensors.face.IFaceServiceWrapper {
        private OplusFaceServiceWrapper() {
        }

        @Override // com.android.server.biometrics.sensors.face.IFaceServiceWrapper
        public com.android.server.biometrics.sensors.face.IFaceServiceExt getExtImpl() {
            return com.android.server.biometrics.sensors.face.FaceService.this.mIFaceServiceExt;
        }

        @Override // com.android.server.biometrics.sensors.face.IFaceServiceWrapper
        public void setExtensionWrapper(android.os.IBinder extension) {
            com.android.server.biometrics.sensors.face.FaceService.this.mServiceWrapper.setExtension(extension);
        }

        @Override // com.android.server.biometrics.sensors.face.IFaceServiceWrapper
        public com.android.server.biometrics.sensors.face.ServiceProvider getProviderForSensorWrapper(int sensorId) {
            return com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getProviderForSensor(sensorId);
        }

        @Override // com.android.server.biometrics.sensors.face.IFaceServiceWrapper
        public android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> getSingleProviderWrapper() {
            return com.android.server.biometrics.sensors.face.FaceService.this.mRegistry.getSingleProvider();
        }
    }

    void sendFaceReEnrollNotification() {
        com.android.server.biometrics.Utils.checkPermissionOrShell(getContext(), "android.permission.MANAGE_FACE");
        if (android.os.Build.IS_DEBUGGABLE) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> provider = this.mRegistry.getSingleProvider();
                if (provider != null) {
                    com.android.server.biometrics.sensors.face.aidl.FaceProvider faceProvider = (com.android.server.biometrics.sensors.face.aidl.FaceProvider) provider.second;
                    faceProvider.sendFaceReEnrollNotification();
                } else {
                    android.util.Slog.w(TAG, "Null provider for notification");
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }
}
