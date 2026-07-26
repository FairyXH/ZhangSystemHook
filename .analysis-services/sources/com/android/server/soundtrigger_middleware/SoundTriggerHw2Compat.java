package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
final class SoundTriggerHw2Compat implements com.android.server.soundtrigger_middleware.ISoundTriggerHal {
    private static final java.lang.String TAG = "SoundTriggerHw2Compat";
    private final android.os.IHwBinder mBinder;
    private final android.media.soundtrigger.Properties mProperties;
    private final java.lang.Runnable mRebootRunnable;
    private android.hardware.soundtrigger.V2_0.ISoundTriggerHw mUnderlying_2_0;
    private android.hardware.soundtrigger.V2_1.ISoundTriggerHw mUnderlying_2_1;
    private android.hardware.soundtrigger.V2_2.ISoundTriggerHw mUnderlying_2_2;
    private android.hardware.soundtrigger.V2_3.ISoundTriggerHw mUnderlying_2_3;
    private final java.util.concurrent.ConcurrentMap<java.lang.Integer, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback> mModelCallbacks = new java.util.concurrent.ConcurrentHashMap();
    private final java.util.Map<android.os.IBinder.DeathRecipient, android.os.IHwBinder.DeathRecipient> mDeathRecipientMap = new java.util.HashMap();

    static com.android.server.soundtrigger_middleware.ISoundTriggerHal create(android.hardware.soundtrigger.V2_0.ISoundTriggerHw underlying, java.lang.Runnable rebootRunnable, com.android.server.soundtrigger_middleware.ICaptureStateNotifier notifier) {
        return create(underlying.asBinder(), rebootRunnable, notifier);
    }

    static com.android.server.soundtrigger_middleware.ISoundTriggerHal create(android.os.IHwBinder binder, java.lang.Runnable rebootRunnable, com.android.server.soundtrigger_middleware.ICaptureStateNotifier notifier) {
        com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat compat = new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat(binder, rebootRunnable);
        com.android.server.soundtrigger_middleware.ISoundTriggerHal result = new com.android.server.soundtrigger_middleware.SoundTriggerHalMaxModelLimiter(compat, compat.mProperties.maxSoundModels);
        return !compat.mProperties.concurrentCapture ? new com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler(result, notifier) : result;
    }

    private SoundTriggerHw2Compat(android.os.IHwBinder binder, java.lang.Runnable rebootRunnable) {
        this.mRebootRunnable = (java.lang.Runnable) java.util.Objects.requireNonNull(rebootRunnable);
        this.mBinder = (android.os.IHwBinder) java.util.Objects.requireNonNull(binder);
        initUnderlying(binder);
        this.mProperties = (android.media.soundtrigger.Properties) java.util.Objects.requireNonNull(getPropertiesInternal());
    }

    private void initUnderlying(android.os.IHwBinder binder) {
        android.hardware.soundtrigger.V2_3.ISoundTriggerHw as2_3 = android.hardware.soundtrigger.V2_3.ISoundTriggerHw.asInterface(binder);
        if (as2_3 != null) {
            this.mUnderlying_2_3 = as2_3;
            this.mUnderlying_2_2 = as2_3;
            this.mUnderlying_2_1 = as2_3;
            this.mUnderlying_2_0 = as2_3;
            return;
        }
        android.hardware.soundtrigger.V2_2.ISoundTriggerHw as2_2 = android.hardware.soundtrigger.V2_2.ISoundTriggerHw.asInterface(binder);
        if (as2_2 != null) {
            this.mUnderlying_2_2 = as2_2;
            this.mUnderlying_2_1 = as2_2;
            this.mUnderlying_2_0 = as2_2;
            this.mUnderlying_2_3 = null;
            return;
        }
        android.hardware.soundtrigger.V2_1.ISoundTriggerHw as2_1 = android.hardware.soundtrigger.V2_1.ISoundTriggerHw.asInterface(binder);
        if (as2_1 != null) {
            this.mUnderlying_2_1 = as2_1;
            this.mUnderlying_2_0 = as2_1;
            this.mUnderlying_2_3 = null;
            this.mUnderlying_2_2 = null;
            return;
        }
        android.hardware.soundtrigger.V2_0.ISoundTriggerHw as2_0 = android.hardware.soundtrigger.V2_0.ISoundTriggerHw.asInterface(binder);
        if (as2_0 != null) {
            this.mUnderlying_2_0 = as2_0;
            this.mUnderlying_2_3 = null;
            this.mUnderlying_2_2 = null;
            this.mUnderlying_2_1 = null;
            return;
        }
        if (android.os.Build.isMtkPlatform()) {
            android.util.Log.e(TAG, "Failed to asInterface for binder:" + binder);
            android.os.RemoteException remoteException = new android.os.RemoteException("Conver HwBinder Interface failed, throw remote exception and retry again.");
            throw new java.lang.RuntimeException("Binder doesn't support ISoundTriggerHw@2.0", remoteException);
        }
        throw new java.lang.RuntimeException("Binder doesn't support ISoundTriggerHw@2.0");
    }

    private static void handleHalStatus(int status, java.lang.String methodName) {
        if (status != 0) {
            throw new com.android.server.soundtrigger_middleware.HalException(status, methodName);
        }
    }

    private static void handleHalStatusAllowBusy(int status, java.lang.String methodName) {
        if (status == (-android.system.OsConstants.EBUSY)) {
            throw new com.android.server.soundtrigger_middleware.RecoverableException(1);
        }
        handleHalStatus(status, methodName);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void reboot() {
        this.mRebootRunnable.run();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void detach() {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.Properties getProperties() {
        return this.mProperties;
    }

    private android.media.soundtrigger.Properties getPropertiesInternal() {
        try {
            final java.util.concurrent.atomic.AtomicInteger retval = new java.util.concurrent.atomic.AtomicInteger(-1);
            final java.util.concurrent.atomic.AtomicReference<android.hardware.soundtrigger.V2_3.Properties> properties = new java.util.concurrent.atomic.AtomicReference<>();
            try {
                as2_3().getProperties_2_3(new android.hardware.soundtrigger.V2_3.ISoundTriggerHw.getProperties_2_3Callback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat$$ExternalSyntheticLambda6
                    @Override // android.hardware.soundtrigger.V2_3.ISoundTriggerHw.getProperties_2_3Callback
                    public final void onValues(int i, android.hardware.soundtrigger.V2_3.Properties properties2) {
                        com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.lambda$getPropertiesInternal$0(retval, properties, i, properties2);
                    }
                });
                handleHalStatus(retval.get(), "getProperties_2_3");
                return com.android.server.soundtrigger_middleware.ConversionUtil.hidl2aidlProperties(properties.get());
            } catch (com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported e) {
                return getProperties_2_0();
            }
        } catch (android.os.RemoteException e2) {
            throw e2.rethrowAsRuntimeException();
        }
    }

    static /* synthetic */ void lambda$getPropertiesInternal$0(java.util.concurrent.atomic.AtomicInteger retval, java.util.concurrent.atomic.AtomicReference properties, int r, android.hardware.soundtrigger.V2_3.Properties p) {
        retval.set(r);
        properties.set(p);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void registerCallback(com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback callback) {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadSoundModel(android.media.soundtrigger.SoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel hidlModel = com.android.server.soundtrigger_middleware.ConversionUtil.aidl2hidlSoundModel(soundModel);
        try {
            try {
                final java.util.concurrent.atomic.AtomicInteger retval = new java.util.concurrent.atomic.AtomicInteger(-1);
                final java.util.concurrent.atomic.AtomicInteger handle = new java.util.concurrent.atomic.AtomicInteger(0);
                try {
                    as2_1().loadSoundModel_2_1(hidlModel, new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.ModelCallbackWrapper(callback), 0, new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadSoundModel_2_1Callback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat$$ExternalSyntheticLambda7
                        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadSoundModel_2_1Callback
                        public final void onValues(int i, int i2) {
                            com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.lambda$loadSoundModel$1(retval, handle, i, i2);
                        }
                    });
                    handleHalStatus(retval.get(), "loadSoundModel_2_1");
                    this.mModelCallbacks.put(java.lang.Integer.valueOf(handle.get()), callback);
                    return handle.get();
                } catch (com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported e) {
                    int iLoadSoundModel_2_0 = loadSoundModel_2_0(hidlModel, callback);
                    if (hidlModel.data != null) {
                        try {
                            hidlModel.data.close();
                        } catch (java.io.IOException e2) {
                            android.util.Slog.e(TAG, "Failed to close file", e2);
                        }
                    }
                    return iLoadSoundModel_2_0;
                }
            } finally {
                if (hidlModel.data != null) {
                    try {
                        hidlModel.data.close();
                    } catch (java.io.IOException e3) {
                        android.util.Slog.e(TAG, "Failed to close file", e3);
                    }
                }
            }
        } catch (android.os.RemoteException e4) {
            throw e4.rethrowAsRuntimeException();
        }
    }

    static /* synthetic */ void lambda$loadSoundModel$1(java.util.concurrent.atomic.AtomicInteger retval, java.util.concurrent.atomic.AtomicInteger handle, int r, int h) {
        retval.set(r);
        handle.set(h);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadPhraseSoundModel(android.media.soundtrigger.PhraseSoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel hidlModel = com.android.server.soundtrigger_middleware.ConversionUtil.aidl2hidlPhraseSoundModel(soundModel);
        try {
            try {
                final java.util.concurrent.atomic.AtomicInteger retval = new java.util.concurrent.atomic.AtomicInteger(-1);
                final java.util.concurrent.atomic.AtomicInteger handle = new java.util.concurrent.atomic.AtomicInteger(0);
                try {
                    as2_1().loadPhraseSoundModel_2_1(hidlModel, new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.ModelCallbackWrapper(callback), 0, new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadPhraseSoundModel_2_1Callback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat$$ExternalSyntheticLambda1
                        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadPhraseSoundModel_2_1Callback
                        public final void onValues(int i, int i2) {
                            com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.lambda$loadPhraseSoundModel$2(retval, handle, i, i2);
                        }
                    });
                    handleHalStatus(retval.get(), "loadPhraseSoundModel_2_1");
                    this.mModelCallbacks.put(java.lang.Integer.valueOf(handle.get()), callback);
                    return handle.get();
                } catch (com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported e) {
                    int iLoadPhraseSoundModel_2_0 = loadPhraseSoundModel_2_0(hidlModel, callback);
                    if (hidlModel.common.data != null) {
                        try {
                            hidlModel.common.data.close();
                        } catch (java.io.IOException e2) {
                            android.util.Slog.e(TAG, "Failed to close file", e2);
                        }
                    }
                    return iLoadPhraseSoundModel_2_0;
                }
            } catch (android.os.RemoteException e3) {
                throw e3.rethrowAsRuntimeException();
            }
        } finally {
            if (hidlModel.common.data != null) {
                try {
                    hidlModel.common.data.close();
                } catch (java.io.IOException e4) {
                    android.util.Slog.e(TAG, "Failed to close file", e4);
                }
            }
        }
    }

    static /* synthetic */ void lambda$loadPhraseSoundModel$2(java.util.concurrent.atomic.AtomicInteger retval, java.util.concurrent.atomic.AtomicInteger handle, int r, int h) {
        retval.set(r);
        handle.set(h);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unloadSoundModel(int modelHandle) {
        try {
            this.mModelCallbacks.remove(java.lang.Integer.valueOf(modelHandle));
            int retval = as2_0().unloadSoundModel(modelHandle);
            handleHalStatus(retval, "unloadSoundModel");
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void stopRecognition(int modelHandle) {
        try {
            int retval = as2_0().stopRecognition(modelHandle);
            handleHalStatus(retval, "stopRecognition");
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void startRecognition(int modelHandle, int deviceHandle, int ioHandle, android.media.soundtrigger.RecognitionConfig config) {
        android.hardware.soundtrigger.V2_3.RecognitionConfig hidlConfig = com.android.server.soundtrigger_middleware.ConversionUtil.aidl2hidlRecognitionConfig(config, deviceHandle, ioHandle);
        try {
            try {
                int retval = as2_3().startRecognition_2_3(modelHandle, hidlConfig);
                handleHalStatus(retval, "startRecognition_2_3");
            } catch (com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported e) {
                startRecognition_2_1(modelHandle, hidlConfig);
            }
        } catch (android.os.RemoteException e2) {
            throw e2.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void forceRecognitionEvent(int modelHandle) {
        try {
            int retval = as2_2().getModelState(modelHandle);
            handleHalStatus(retval, "getModelState");
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported e2) {
            throw e2.throwAsRecoverableException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int getModelParameter(int modelHandle, int param) {
        final java.util.concurrent.atomic.AtomicInteger status = new java.util.concurrent.atomic.AtomicInteger(-1);
        final java.util.concurrent.atomic.AtomicInteger value = new java.util.concurrent.atomic.AtomicInteger(0);
        try {
            as2_3().getParameter(modelHandle, param, new android.hardware.soundtrigger.V2_3.ISoundTriggerHw.getParameterCallback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat$$ExternalSyntheticLambda8
                @Override // android.hardware.soundtrigger.V2_3.ISoundTriggerHw.getParameterCallback
                public final void onValues(int i, int i2) {
                    com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.lambda$getModelParameter$3(status, value, i, i2);
                }
            });
            handleHalStatus(status.get(), "getParameter");
            return value.get();
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported e2) {
            throw e2.throwAsRecoverableException();
        }
    }

    static /* synthetic */ void lambda$getModelParameter$3(java.util.concurrent.atomic.AtomicInteger status, java.util.concurrent.atomic.AtomicInteger value, int s, int v) {
        status.set(s);
        value.set(v);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void setModelParameter(int modelHandle, int param, int value) {
        try {
            int retval = as2_3().setParameter(modelHandle, param, value);
            handleHalStatus(retval, "setParameter");
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported e2) {
            throw e2.throwAsRecoverableException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.ModelParameterRange queryParameter(int modelHandle, int param) {
        final java.util.concurrent.atomic.AtomicInteger status = new java.util.concurrent.atomic.AtomicInteger(-1);
        final java.util.concurrent.atomic.AtomicReference<android.hardware.soundtrigger.V2_3.OptionalModelParameterRange> optionalRange = new java.util.concurrent.atomic.AtomicReference<>();
        try {
            as2_3().queryParameter(modelHandle, param, new android.hardware.soundtrigger.V2_3.ISoundTriggerHw.queryParameterCallback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat$$ExternalSyntheticLambda2
                @Override // android.hardware.soundtrigger.V2_3.ISoundTriggerHw.queryParameterCallback
                public final void onValues(int i, android.hardware.soundtrigger.V2_3.OptionalModelParameterRange optionalModelParameterRange) {
                    com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.lambda$queryParameter$4(status, optionalRange, i, optionalModelParameterRange);
                }
            });
            handleHalStatus(status.get(), "queryParameter");
            if (optionalRange.get().getDiscriminator() != 1) {
                return null;
            }
            return com.android.server.soundtrigger_middleware.ConversionUtil.hidl2aidlModelParameterRange(optionalRange.get().range());
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported e2) {
            return null;
        }
    }

    static /* synthetic */ void lambda$queryParameter$4(java.util.concurrent.atomic.AtomicInteger status, java.util.concurrent.atomic.AtomicReference optionalRange, int s, android.hardware.soundtrigger.V2_3.OptionalModelParameterRange r) {
        status.set(s);
        optionalRange.set(r);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void linkToDeath(final android.os.IBinder.DeathRecipient recipient) {
        android.os.IHwBinder.DeathRecipient wrapper = new android.os.IHwBinder.DeathRecipient() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat$$ExternalSyntheticLambda3
            public final void serviceDied(long j) {
                recipient.binderDied();
            }
        };
        this.mDeathRecipientMap.put(recipient, wrapper);
        this.mBinder.linkToDeath(wrapper, 0L);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unlinkToDeath(android.os.IBinder.DeathRecipient recipient) {
        this.mBinder.unlinkToDeath(this.mDeathRecipientMap.remove(recipient));
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public java.lang.String interfaceDescriptor() {
        try {
            return as2_0().interfaceDescriptor();
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void flushCallbacks() {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientAttached(android.os.IBinder binder) {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientDetached(android.os.IBinder binder) {
    }

    private android.media.soundtrigger.Properties getProperties_2_0() throws android.os.RemoteException {
        final java.util.concurrent.atomic.AtomicInteger retval = new java.util.concurrent.atomic.AtomicInteger(-1);
        final java.util.concurrent.atomic.AtomicReference<android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties> properties = new java.util.concurrent.atomic.AtomicReference<>();
        as2_0().getProperties(new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.getPropertiesCallback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat$$ExternalSyntheticLambda5
            @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw.getPropertiesCallback
            public final void onValues(int i, android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties properties2) {
                com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.lambda$getProperties_2_0$6(retval, properties, i, properties2);
            }
        });
        handleHalStatus(retval.get(), "getProperties");
        return com.android.server.soundtrigger_middleware.ConversionUtil.hidl2aidlProperties(com.android.server.soundtrigger_middleware.Hw2CompatUtil.convertProperties_2_0_to_2_3(properties.get()));
    }

    static /* synthetic */ void lambda$getProperties_2_0$6(java.util.concurrent.atomic.AtomicInteger retval, java.util.concurrent.atomic.AtomicReference properties, int r, android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties p) {
        retval.set(r);
        properties.set(p);
    }

    private int loadSoundModel_2_0(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) throws android.os.RemoteException {
        android.hardware.soundtrigger.V2_0.ISoundTriggerHw.SoundModel model_2_0 = com.android.server.soundtrigger_middleware.Hw2CompatUtil.convertSoundModel_2_1_to_2_0(soundModel);
        final java.util.concurrent.atomic.AtomicInteger retval = new java.util.concurrent.atomic.AtomicInteger(-1);
        final java.util.concurrent.atomic.AtomicInteger handle = new java.util.concurrent.atomic.AtomicInteger(0);
        as2_0().loadSoundModel(model_2_0, new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.ModelCallbackWrapper(callback), 0, new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadSoundModelCallback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat$$ExternalSyntheticLambda0
            @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadSoundModelCallback
            public final void onValues(int i, int i2) {
                com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.lambda$loadSoundModel_2_0$7(retval, handle, i, i2);
            }
        });
        handleHalStatus(retval.get(), "loadSoundModel");
        this.mModelCallbacks.put(java.lang.Integer.valueOf(handle.get()), callback);
        return handle.get();
    }

    static /* synthetic */ void lambda$loadSoundModel_2_0$7(java.util.concurrent.atomic.AtomicInteger retval, java.util.concurrent.atomic.AtomicInteger handle, int r, int h) {
        retval.set(r);
        handle.set(h);
    }

    private int loadPhraseSoundModel_2_0(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) throws android.os.RemoteException {
        android.hardware.soundtrigger.V2_0.ISoundTriggerHw.PhraseSoundModel model_2_0 = com.android.server.soundtrigger_middleware.Hw2CompatUtil.convertPhraseSoundModel_2_1_to_2_0(soundModel);
        final java.util.concurrent.atomic.AtomicInteger retval = new java.util.concurrent.atomic.AtomicInteger(-1);
        final java.util.concurrent.atomic.AtomicInteger handle = new java.util.concurrent.atomic.AtomicInteger(0);
        as2_0().loadPhraseSoundModel(model_2_0, new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.ModelCallbackWrapper(callback), 0, new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadPhraseSoundModelCallback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat$$ExternalSyntheticLambda4
            @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadPhraseSoundModelCallback
            public final void onValues(int i, int i2) {
                com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.lambda$loadPhraseSoundModel_2_0$8(retval, handle, i, i2);
            }
        });
        handleHalStatus(retval.get(), "loadSoundModel");
        this.mModelCallbacks.put(java.lang.Integer.valueOf(handle.get()), callback);
        return handle.get();
    }

    static /* synthetic */ void lambda$loadPhraseSoundModel_2_0$8(java.util.concurrent.atomic.AtomicInteger retval, java.util.concurrent.atomic.AtomicInteger handle, int r, int h) {
        retval.set(r);
        handle.set(h);
    }

    private void startRecognition_2_1(int modelHandle, android.hardware.soundtrigger.V2_3.RecognitionConfig config) {
        try {
            try {
                android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig config_2_1 = com.android.server.soundtrigger_middleware.Hw2CompatUtil.convertRecognitionConfig_2_3_to_2_1(config);
                int retval = as2_1().startRecognition_2_1(modelHandle, config_2_1, new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.ModelCallbackWrapper(this.mModelCallbacks.get(java.lang.Integer.valueOf(modelHandle))), 0);
                handleHalStatus(retval, "startRecognition_2_1");
            } catch (com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported e) {
                startRecognition_2_0(modelHandle, config);
            }
        } catch (android.os.RemoteException e2) {
            throw e2.rethrowAsRuntimeException();
        }
    }

    private void startRecognition_2_0(int modelHandle, android.hardware.soundtrigger.V2_3.RecognitionConfig config) throws android.os.RemoteException {
        android.hardware.soundtrigger.V2_0.ISoundTriggerHw.RecognitionConfig config_2_0 = com.android.server.soundtrigger_middleware.Hw2CompatUtil.convertRecognitionConfig_2_3_to_2_0(config);
        int retval = as2_0().startRecognition(modelHandle, config_2_0, new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.ModelCallbackWrapper(this.mModelCallbacks.get(java.lang.Integer.valueOf(modelHandle))), 0);
        handleHalStatus(retval, "startRecognition");
    }

    private android.hardware.soundtrigger.V2_0.ISoundTriggerHw as2_0() {
        return this.mUnderlying_2_0;
    }

    private android.hardware.soundtrigger.V2_1.ISoundTriggerHw as2_1() throws com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported {
        if (this.mUnderlying_2_1 == null) {
            throw new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported("Underlying driver version < 2.1");
        }
        return this.mUnderlying_2_1;
    }

    private android.hardware.soundtrigger.V2_2.ISoundTriggerHw as2_2() throws com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported {
        if (this.mUnderlying_2_2 == null) {
            throw new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported("Underlying driver version < 2.2");
        }
        return this.mUnderlying_2_2;
    }

    private android.hardware.soundtrigger.V2_3.ISoundTriggerHw as2_3() throws com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported {
        if (this.mUnderlying_2_3 == null) {
            throw new com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.NotSupported("Underlying driver version < 2.3");
        }
        return this.mUnderlying_2_3;
    }

    private static class NotSupported extends java.lang.Exception {
        NotSupported(java.lang.String message) {
            super(message);
        }

        com.android.server.soundtrigger_middleware.RecoverableException throwAsRecoverableException() {
            throw new com.android.server.soundtrigger_middleware.RecoverableException(2, getMessage());
        }
    }

    private static class ModelCallbackWrapper extends android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.Stub {
        private final com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback mDelegate;

        private ModelCallbackWrapper(com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback delegate) {
            this.mDelegate = (com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback) java.util.Objects.requireNonNull(delegate);
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback
        public void recognitionCallback_2_1(android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.RecognitionEvent event, int cookie) {
            android.media.soundtrigger_middleware.RecognitionEventSys eventSys = new android.media.soundtrigger_middleware.RecognitionEventSys();
            eventSys.recognitionEvent = com.android.server.soundtrigger_middleware.ConversionUtil.hidl2aidlRecognitionEvent(event);
            eventSys.halEventReceivedMillis = android.os.SystemClock.elapsedRealtime();
            this.mDelegate.recognitionCallback(event.header.model, eventSys);
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback
        public void phraseRecognitionCallback_2_1(android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.PhraseRecognitionEvent event, int cookie) {
            android.media.soundtrigger_middleware.PhraseRecognitionEventSys eventSys = new android.media.soundtrigger_middleware.PhraseRecognitionEventSys();
            eventSys.phraseRecognitionEvent = com.android.server.soundtrigger_middleware.ConversionUtil.hidl2aidlPhraseRecognitionEvent(event);
            eventSys.halEventReceivedMillis = android.os.SystemClock.elapsedRealtime();
            this.mDelegate.phraseRecognitionCallback(event.common.header.model, eventSys);
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback
        public void soundModelCallback_2_1(android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.ModelEvent event, int cookie) {
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback
        public void recognitionCallback(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent event, int cookie) {
            android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.RecognitionEvent event_2_1 = com.android.server.soundtrigger_middleware.Hw2CompatUtil.convertRecognitionEvent_2_0_to_2_1(event);
            recognitionCallback_2_1(event_2_1, cookie);
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback
        public void phraseRecognitionCallback(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent event, int cookie) {
            android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.PhraseRecognitionEvent event_2_1 = com.android.server.soundtrigger_middleware.Hw2CompatUtil.convertPhraseRecognitionEvent_2_0_to_2_1(event);
            phraseRecognitionCallback_2_1(event_2_1, cookie);
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback
        public void soundModelCallback(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent event, int cookie) {
        }
    }
}
