package com.android.server.soundtrigger;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerHelper implements android.hardware.soundtrigger.SoundTrigger.StatusListener {
    public static final int INVALID_MODULE_ID = -1;
    private static final int INVALID_VALUE = Integer.MIN_VALUE;
    public static final int STATUS_ERROR = Integer.MIN_VALUE;
    public static final int STATUS_OK = 0;
    static final java.lang.String TAG = "SoundTriggerHelper";
    private final android.content.Context mContext;
    private final com.android.server.utils.EventLogger mEventLogger;
    private android.hardware.soundtrigger.SoundTriggerModule mModule;
    private final int mModuleId;
    private final java.util.function.Supplier<java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties>> mModulePropertiesProvider;
    private final java.util.function.Function<android.hardware.soundtrigger.SoundTrigger.StatusListener, android.hardware.soundtrigger.SoundTriggerModule> mModuleProvider;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.HashMap<java.util.UUID, com.android.server.soundtrigger.SoundTriggerHelper.ModelData> mModelDataMap = new java.util.HashMap<>();
    private final java.util.HashMap<java.lang.Integer, java.util.UUID> mKeyphraseUuidMap = new java.util.HashMap<>();
    private boolean mRecognitionRequested = false;
    private boolean mIsDetached = false;
    private com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState mDeviceState = com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState.DISABLE;
    private boolean mIsAppOpPermitted = true;

    SoundTriggerHelper(android.content.Context context, com.android.server.utils.EventLogger eventLogger, java.util.function.Function<android.hardware.soundtrigger.SoundTrigger.StatusListener, android.hardware.soundtrigger.SoundTriggerModule> moduleProvider, int moduleId, java.util.function.Supplier<java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties>> modulePropertiesProvider) {
        this.mModuleId = moduleId;
        this.mContext = context;
        this.mModuleProvider = moduleProvider;
        this.mEventLogger = eventLogger;
        this.mModulePropertiesProvider = modulePropertiesProvider;
        if (moduleId == -1) {
            this.mModule = null;
        } else {
            this.mModule = this.mModuleProvider.apply(this);
        }
    }

    public int startGenericRecognition(java.util.UUID modelId, android.hardware.soundtrigger.SoundTrigger.GenericSoundModel soundModel, android.hardware.soundtrigger.IRecognitionStatusCallback callback, android.hardware.soundtrigger.SoundTrigger.RecognitionConfig recognitionConfig, boolean runInBatterySaverMode) {
        com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_start_recognition", 1);
        if (modelId == null || soundModel == null || callback == null || recognitionConfig == null) {
            android.util.Slog.w(TAG, "Passed in bad data to startGenericRecognition().");
            return Integer.MIN_VALUE;
        }
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
            com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = getOrCreateGenericModelDataLocked(modelId);
            if (modelData == null) {
                android.util.Slog.w(TAG, "Irrecoverable error occurred, check UUID / sound model data.");
                return Integer.MIN_VALUE;
            }
            return startRecognition(soundModel, modelData, callback, recognitionConfig, Integer.MIN_VALUE, runInBatterySaverMode);
        }
    }

    public int startKeyphraseRecognition(int keyphraseId, android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel soundModel, android.hardware.soundtrigger.IRecognitionStatusCallback callback, android.hardware.soundtrigger.SoundTrigger.RecognitionConfig recognitionConfig, boolean runInBatterySaverMode) {
        com.android.server.soundtrigger.SoundTriggerHelper.ModelData model;
        synchronized (this.mLock) {
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_start_recognition", 1);
            if (soundModel != null && callback != null && recognitionConfig != null) {
                if (this.mIsDetached) {
                    throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
                }
                com.android.server.soundtrigger.SoundTriggerHelper.ModelData model2 = getKeyphraseModelDataLocked(keyphraseId);
                if (model2 != null && !model2.isKeyphraseModel()) {
                    android.util.Slog.e(TAG, "Generic model with same UUID exists.");
                    return Integer.MIN_VALUE;
                }
                if (model2 != null && !model2.getModelId().equals(soundModel.getUuid())) {
                    int status = cleanUpExistingKeyphraseModelLocked(model2);
                    if (status != 0) {
                        return status;
                    }
                    removeKeyphraseModelLocked(keyphraseId);
                    model2 = null;
                }
                if (model2 != null) {
                    model = model2;
                } else {
                    model = createKeyphraseModelDataLocked(soundModel.getUuid(), keyphraseId);
                }
                return startRecognition(soundModel, model, callback, recognitionConfig, keyphraseId, runInBatterySaverMode);
            }
            return Integer.MIN_VALUE;
        }
    }

    private int cleanUpExistingKeyphraseModelLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData) {
        int status = tryStopAndUnloadLocked(modelData, true, true);
        if (status != 0) {
            android.util.Slog.w(TAG, "Unable to stop or unload previous model: " + modelData.toString());
        }
        return status;
    }

    private int prepareForRecognition(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData) {
        if (this.mModule == null) {
            android.util.Slog.w(TAG, "prepareForRecognition: cannot attach to sound trigger module");
            return Integer.MIN_VALUE;
        }
        if (!modelData.isModelLoaded()) {
            stopAndUnloadDeadModelsLocked();
            int[] handle = {0};
            int status = this.mModule.loadSoundModel(modelData.getSoundModel(), handle);
            if (status != 0) {
                android.util.Slog.w(TAG, "prepareForRecognition: loadSoundModel failed with status: " + status);
                return status;
            }
            modelData.setHandle(handle[0]);
            modelData.setLoaded();
        }
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0044 A[Catch: all -> 0x00f4, TryCatch #2 {, blocks: (B:4:0x0003, B:6:0x0009, B:8:0x0013, B:9:0x002f, B:13:0x003b, B:12:0x0034, B:14:0x003e, B:16:0x0044, B:18:0x0050, B:26:0x0071, B:28:0x0077, B:29:0x0093, B:21:0x0059, B:23:0x0063, B:31:0x0095, B:33:0x00ac, B:35:0x00b2, B:37:0x00b4, B:39:0x00b8, B:40:0x00bb, B:43:0x00bf, B:48:0x00f2, B:46:0x00d4), top: B:57:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ac A[Catch: all -> 0x00f4, TryCatch #2 {, blocks: (B:4:0x0003, B:6:0x0009, B:8:0x0013, B:9:0x002f, B:13:0x003b, B:12:0x0034, B:14:0x003e, B:16:0x0044, B:18:0x0050, B:26:0x0071, B:28:0x0077, B:29:0x0093, B:21:0x0059, B:23:0x0063, B:31:0x0095, B:33:0x00ac, B:35:0x00b2, B:37:0x00b4, B:39:0x00b8, B:40:0x00bb, B:43:0x00bf, B:48:0x00f2, B:46:0x00d4), top: B:57:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00bf A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int startRecognition(android.hardware.soundtrigger.SoundTrigger.SoundModel r10, com.android.server.soundtrigger.SoundTriggerHelper.ModelData r11, android.hardware.soundtrigger.IRecognitionStatusCallback r12, android.hardware.soundtrigger.SoundTrigger.RecognitionConfig r13, int r14, boolean r15) {
        /*
            Method dump skipped, instruction units count: 247
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.soundtrigger.SoundTriggerHelper.startRecognition(android.hardware.soundtrigger.SoundTrigger$SoundModel, com.android.server.soundtrigger.SoundTriggerHelper$ModelData, android.hardware.soundtrigger.IRecognitionStatusCallback, android.hardware.soundtrigger.SoundTrigger$RecognitionConfig, int, boolean):int");
    }

    public int stopGenericRecognition(java.util.UUID modelId, android.hardware.soundtrigger.IRecognitionStatusCallback callback) {
        synchronized (this.mLock) {
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_stop_recognition", 1);
            if (callback != null && modelId != null) {
                if (this.mIsDetached) {
                    throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
                }
                com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = this.mModelDataMap.get(modelId);
                if (modelData != null && modelData.isGenericModel()) {
                    int status = stopRecognition(modelData, callback);
                    if (status != 0) {
                        android.util.Slog.w(TAG, "stopGenericRecognition failed: " + status);
                    }
                    return status;
                }
                android.util.Slog.w(TAG, "Attempting stopRecognition on invalid model with id:" + modelId);
                return Integer.MIN_VALUE;
            }
            android.util.Slog.e(TAG, "Null callbackreceived for stopGenericRecognition() for modelid:" + modelId);
            return Integer.MIN_VALUE;
        }
    }

    public int stopKeyphraseRecognition(int keyphraseId, android.hardware.soundtrigger.IRecognitionStatusCallback callback) {
        synchronized (this.mLock) {
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_stop_recognition", 1);
            if (callback == null) {
                android.util.Slog.e(TAG, "Null callback received for stopKeyphraseRecognition() for keyphraseId:" + keyphraseId);
                return Integer.MIN_VALUE;
            }
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
            com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = getKeyphraseModelDataLocked(keyphraseId);
            if (modelData != null && modelData.isKeyphraseModel()) {
                int status = stopRecognition(modelData, callback);
                return status != 0 ? status : status;
            }
            android.util.Slog.w(TAG, "No model exists for given keyphrase Id " + keyphraseId);
            return Integer.MIN_VALUE;
        }
    }

    private int stopRecognition(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData, android.hardware.soundtrigger.IRecognitionStatusCallback callback) {
        synchronized (this.mLock) {
            if (callback == null) {
                return Integer.MIN_VALUE;
            }
            if (this.mModule == null) {
                android.util.Slog.w(TAG, "Attempting stopRecognition after detach");
                return Integer.MIN_VALUE;
            }
            android.hardware.soundtrigger.IRecognitionStatusCallback currentCallback = modelData.getCallback();
            if (modelData != null && currentCallback != null && (modelData.isRequested() || modelData.isModelStarted())) {
                if (currentCallback.asBinder() != callback.asBinder()) {
                    android.util.Slog.w(TAG, "Attempting stopRecognition for another recognition");
                    return Integer.MIN_VALUE;
                }
                modelData.setRequested(false);
                int status = updateRecognitionLocked(modelData, false);
                if (status != 0) {
                    return status;
                }
                if (android.os.Build.isQcomPlatform()) {
                    modelData.setLoaded();
                }
                modelData.clearCallback();
                modelData.setRecognitionConfig(null);
                return status;
            }
            android.util.Slog.w(TAG, "Attempting stopRecognition without a successful startRecognition");
            return Integer.MIN_VALUE;
        }
    }

    private int tryStopAndUnloadLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData, boolean stopModel, boolean unloadModel) {
        int status = 0;
        if (modelData.isModelNotLoaded()) {
            return 0;
        }
        if (stopModel && modelData.isModelStarted() && (status = stopRecognitionLocked(modelData, false)) != 0) {
            android.util.Slog.w(TAG, "stopRecognition failed: " + status);
            return status;
        }
        if (unloadModel && modelData.isModelLoaded()) {
            android.util.Slog.d(TAG, "Unloading previously loaded stale model.");
            if (this.mModule == null) {
                return Integer.MIN_VALUE;
            }
            status = this.mModule.unloadSoundModel(modelData.getHandle());
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_unloading_stale_model", 1);
            if (status != 0) {
                android.util.Slog.w(TAG, "unloadSoundModel call failed with " + status);
            } else {
                modelData.clearState();
            }
        }
        return status;
    }

    public android.hardware.soundtrigger.SoundTrigger.ModuleProperties getModuleProperties() {
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
        }
        for (android.hardware.soundtrigger.SoundTrigger.ModuleProperties moduleProperties : this.mModulePropertiesProvider.get()) {
            if (moduleProperties.getId() == this.mModuleId) {
                return moduleProperties;
            }
        }
        android.util.Slog.e(TAG, "Module properties not found for existing moduleId " + this.mModuleId);
        return null;
    }

    public int unloadKeyphraseSoundModel(int keyphraseId) {
        synchronized (this.mLock) {
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_unload_keyphrase_sound_model", 1);
            com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = getKeyphraseModelDataLocked(keyphraseId);
            if (this.mModule != null && modelData != null && modelData.isModelLoaded() && modelData.isKeyphraseModel()) {
                if (this.mIsDetached) {
                    throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
                }
                modelData.setRequested(false);
                int status = updateRecognitionLocked(modelData, false);
                if (status != 0) {
                    android.util.Slog.w(TAG, "Stop recognition failed for keyphrase ID:" + status);
                }
                int status2 = this.mModule.unloadSoundModel(modelData.getHandle());
                if (status2 != 0) {
                    android.util.Slog.w(TAG, "unloadKeyphraseSoundModel call failed with " + status2);
                }
                removeKeyphraseModelLocked(keyphraseId);
                return status2;
            }
            return Integer.MIN_VALUE;
        }
    }

    public int unloadGenericSoundModel(java.util.UUID modelId) {
        int status;
        synchronized (this.mLock) {
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_unload_generic_sound_model", 1);
            if (modelId != null && this.mModule != null) {
                if (this.mIsDetached) {
                    throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
                }
                com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = this.mModelDataMap.get(modelId);
                if (modelData != null && modelData.isGenericModel()) {
                    if (!modelData.isModelLoaded()) {
                        android.util.Slog.i(TAG, "Unload: Given generic model is not loaded:" + modelId);
                        return 0;
                    }
                    if (modelData.isModelStarted() && (status = stopRecognitionLocked(modelData, false)) != 0) {
                        android.util.Slog.w(TAG, "stopGenericRecognition failed: " + status);
                    }
                    if (this.mModule == null) {
                        return Integer.MIN_VALUE;
                    }
                    int status2 = this.mModule.unloadSoundModel(modelData.getHandle());
                    if (status2 != 0) {
                        android.util.Slog.w(TAG, "unloadGenericSoundModel() call failed with " + status2);
                        android.util.Slog.w(TAG, "unloadGenericSoundModel() force-marking model as unloaded.");
                    }
                    this.mModelDataMap.remove(modelId);
                    return status2;
                }
                android.util.Slog.w(TAG, "Unload error: Attempting unload invalid generic model with id:" + modelId);
                return Integer.MIN_VALUE;
            }
            return Integer.MIN_VALUE;
        }
    }

    public boolean isRecognitionRequested(java.util.UUID modelId) {
        boolean z;
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
            com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = this.mModelDataMap.get(modelId);
            z = modelData != null && modelData.isRequested();
        }
        return z;
    }

    public void onDeviceStateChanged(com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState state) {
        synchronized (this.mLock) {
            android.util.Slog.d(TAG, "mIsDetached = " + this.mIsDetached + ",state = " + state + ",mDeviceState = " + this.mDeviceState);
            if (!this.mIsDetached && this.mDeviceState != state) {
                this.mDeviceState = state;
                updateAllRecognitionsLocked();
            }
        }
    }

    public void onAppOpStateChanged(boolean isPermitted) {
        synchronized (this.mLock) {
            if (this.mIsAppOpPermitted == isPermitted) {
                return;
            }
            android.util.Slog.d(TAG, "mIsAppOpPermitted = " + this.mIsAppOpPermitted + " isPermitted = " + isPermitted);
            this.mIsAppOpPermitted = isPermitted;
            updateAllRecognitionsLocked();
        }
    }

    public int getGenericModelState(java.util.UUID modelId) {
        synchronized (this.mLock) {
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_get_generic_model_state", 1);
            if (modelId != null && this.mModule != null) {
                if (this.mIsDetached) {
                    throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
                }
                com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = this.mModelDataMap.get(modelId);
                if (modelData != null && modelData.isGenericModel()) {
                    if (!modelData.isModelLoaded()) {
                        android.util.Slog.i(TAG, "GetGenericModelState: Given generic model is not loaded:" + modelId);
                        return Integer.MIN_VALUE;
                    }
                    if (!modelData.isModelStarted()) {
                        android.util.Slog.i(TAG, "GetGenericModelState: Given generic model is not started:" + modelId);
                        return Integer.MIN_VALUE;
                    }
                    return this.mModule.getModelState(modelData.getHandle());
                }
                android.util.Slog.w(TAG, "GetGenericModelState error: Invalid generic model id:" + modelId);
                return Integer.MIN_VALUE;
            }
            return Integer.MIN_VALUE;
        }
    }

    public int setParameter(java.util.UUID modelId, int modelParam, int value) {
        int parameterLocked;
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
            parameterLocked = setParameterLocked(this.mModelDataMap.get(modelId), modelParam, value);
        }
        return parameterLocked;
    }

    public int setKeyphraseParameter(int keyphraseId, int modelParam, int value) {
        int parameterLocked;
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
            parameterLocked = setParameterLocked(getKeyphraseModelDataLocked(keyphraseId), modelParam, value);
        }
        return parameterLocked;
    }

    private int setParameterLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData, int modelParam, int value) {
        com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_set_parameter", 1);
        if (this.mModule == null) {
            return android.hardware.soundtrigger.SoundTrigger.STATUS_NO_INIT;
        }
        if (modelData == null || !modelData.isModelLoaded()) {
            android.util.Slog.i(TAG, "SetParameter: Given model is not loaded:" + modelData);
            return android.hardware.soundtrigger.SoundTrigger.STATUS_BAD_VALUE;
        }
        return this.mModule.setParameter(modelData.getHandle(), modelParam, value);
    }

    public int getParameter(java.util.UUID modelId, int modelParam) {
        int parameterLocked;
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
            parameterLocked = getParameterLocked(this.mModelDataMap.get(modelId), modelParam);
        }
        return parameterLocked;
    }

    public int getKeyphraseParameter(int keyphraseId, int modelParam) {
        int parameterLocked;
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
            parameterLocked = getParameterLocked(getKeyphraseModelDataLocked(keyphraseId), modelParam);
        }
        return parameterLocked;
    }

    private int getParameterLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData, int modelParam) {
        com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_get_parameter", 1);
        if (this.mModule == null) {
            throw new java.lang.UnsupportedOperationException("SoundTriggerModule not initialized");
        }
        if (modelData == null) {
            throw new java.lang.IllegalArgumentException("Invalid model id");
        }
        if (!modelData.isModelLoaded()) {
            throw new java.lang.UnsupportedOperationException("Given model is not loaded:" + modelData);
        }
        return this.mModule.getParameter(modelData.getHandle(), modelParam);
    }

    public android.hardware.soundtrigger.SoundTrigger.ModelParamRange queryParameter(java.util.UUID modelId, int modelParam) {
        android.hardware.soundtrigger.SoundTrigger.ModelParamRange modelParamRangeQueryParameterLocked;
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
            modelParamRangeQueryParameterLocked = queryParameterLocked(this.mModelDataMap.get(modelId), modelParam);
        }
        return modelParamRangeQueryParameterLocked;
    }

    public android.hardware.soundtrigger.SoundTrigger.ModelParamRange queryKeyphraseParameter(int keyphraseId, int modelParam) {
        android.hardware.soundtrigger.SoundTrigger.ModelParamRange modelParamRangeQueryParameterLocked;
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                throw new java.lang.IllegalStateException("SoundTriggerHelper has been detached");
            }
            modelParamRangeQueryParameterLocked = queryParameterLocked(getKeyphraseModelDataLocked(keyphraseId), modelParam);
        }
        return modelParamRangeQueryParameterLocked;
    }

    private android.hardware.soundtrigger.SoundTrigger.ModelParamRange queryParameterLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData, int modelParam) {
        com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_query_parameter", 1);
        if (this.mModule == null) {
            return null;
        }
        if (modelData == null) {
            android.util.Slog.w(TAG, "queryParameter: Invalid model id");
            return null;
        }
        if (!modelData.isModelLoaded()) {
            android.util.Slog.i(TAG, "queryParameter: Given model is not loaded:" + modelData);
            return null;
        }
        return this.mModule.queryParameter(modelData.getHandle(), modelParam);
    }

    public void onRecognition(android.hardware.soundtrigger.SoundTrigger.RecognitionEvent event) {
        if (event == null) {
            android.util.Slog.w(TAG, "Null recognition event!");
            return;
        }
        if (!(event instanceof android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent) && !(event instanceof android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent)) {
            android.util.Slog.w(TAG, "Invalid recognition event type (not one of generic or keyphrase)!");
            return;
        }
        synchronized (this.mLock) {
            switch (event.status) {
                case 0:
                case 2:
                case 3:
                    if (isKeyphraseRecognitionEvent(event)) {
                        onKeyphraseRecognitionLocked((android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent) event);
                    } else {
                        onGenericRecognitionLocked((android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent) event);
                    }
                    break;
                case 1:
                    onRecognitionAbortLocked(event);
                    break;
            }
        }
    }

    private boolean isKeyphraseRecognitionEvent(android.hardware.soundtrigger.SoundTrigger.RecognitionEvent event) {
        return event instanceof android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent;
    }

    private void onGenericRecognitionLocked(android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent event) {
        com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_generic_recognition_event", 1);
        if (event.status != 0 && event.status != 3) {
            return;
        }
        com.android.server.soundtrigger.SoundTriggerHelper.ModelData model = getModelDataForLocked(event.soundModelHandle);
        if (model == null || !model.isGenericModel()) {
            android.util.Slog.w(TAG, "Generic recognition event: Model does not exist for handle: " + event.soundModelHandle);
            return;
        }
        if (!java.util.Objects.equals(event.getToken(), model.getToken())) {
            return;
        }
        android.hardware.soundtrigger.IRecognitionStatusCallback callback = model.getCallback();
        if (callback == null) {
            android.util.Slog.w(TAG, "Generic recognition event: Null callback for model handle: " + event.soundModelHandle);
            return;
        }
        if (!event.recognitionStillActive) {
            model.setStopped();
        }
        try {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.RECOGNITION, model.getModelId()));
            callback.onGenericSoundTriggerDetected(event);
            android.hardware.soundtrigger.SoundTrigger.RecognitionConfig config = model.getRecognitionConfig();
            if (config == null) {
                android.util.Slog.w(TAG, "Generic recognition event: Null RecognitionConfig for model handle: " + event.soundModelHandle);
                return;
            }
            model.setRequested(config.allowMultipleTriggers);
            if (model.isRequested()) {
                updateRecognitionLocked(model, true);
            }
        } catch (android.os.RemoteException e) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.RECOGNITION, model.getModelId(), "RemoteException").printLog(2, TAG));
            forceStopAndUnloadModelLocked(model, e);
        }
    }

    public void onModelUnloaded(int modelHandle) {
        synchronized (this.mLock) {
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_sound_model_updated", 1);
            onModelUnloadedLocked(modelHandle);
        }
    }

    public void onResourcesAvailable() {
        synchronized (this.mLock) {
            onResourcesAvailableLocked();
        }
    }

    public void onServiceDied() {
        android.util.Slog.e(TAG, "onServiceDied!!");
        com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_service_died", 1);
        synchronized (this.mLock) {
            onServiceDiedLocked();
        }
    }

    private void onModelUnloadedLocked(int modelHandle) {
        com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = getModelDataForLocked(modelHandle);
        if (modelData != null) {
            modelData.setNotLoaded();
        }
    }

    private void onResourcesAvailableLocked() {
        this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.RESOURCES_AVAILABLE, null));
        updateAllRecognitionsLocked();
    }

    private void onRecognitionAbortLocked(android.hardware.soundtrigger.SoundTrigger.RecognitionEvent event) {
        android.util.Slog.w(TAG, "Recognition aborted");
        com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_recognition_aborted", 1);
        com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = getModelDataForLocked(event.soundModelHandle);
        if (modelData != null && modelData.isModelStarted()) {
            if (!java.util.Objects.equals(event.getToken(), modelData.getToken())) {
                return;
            }
            modelData.setStopped();
            try {
                android.hardware.soundtrigger.IRecognitionStatusCallback callback = modelData.getCallback();
                if (callback != null) {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.PAUSE, modelData.getModelId()));
                    callback.onRecognitionPaused();
                }
            } catch (android.os.RemoteException e) {
                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.PAUSE, modelData.getModelId(), "RemoteException").printLog(2, TAG));
                forceStopAndUnloadModelLocked(modelData, e);
            }
        }
        if (android.os.Build.isMtkPlatform() && modelData != null && modelData.isModelLoaded()) {
            modelData.setRequested(true);
        }
    }

    private int getKeyphraseIdFromEvent(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent event) {
        if (event == null) {
            android.util.Slog.w(TAG, "Null RecognitionEvent received.");
            return Integer.MIN_VALUE;
        }
        android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[] keyphraseExtras = event.keyphraseExtras;
        if (keyphraseExtras == null || keyphraseExtras.length == 0) {
            android.util.Slog.w(TAG, "Invalid keyphrase recognition event!");
            return Integer.MIN_VALUE;
        }
        return keyphraseExtras[0].id;
    }

    private void onKeyphraseRecognitionLocked(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent event) {
        android.util.Slog.i(TAG, "Recognition success");
        com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_keyphrase_recognition_event", 1);
        int keyphraseId = getKeyphraseIdFromEvent(event);
        com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = getKeyphraseModelDataLocked(keyphraseId);
        if (modelData == null || !modelData.isKeyphraseModel()) {
            android.util.Slog.e(TAG, "Keyphase model data does not exist for ID:" + keyphraseId);
            return;
        }
        if (!java.util.Objects.equals(event.getToken(), modelData.getToken())) {
            return;
        }
        if (modelData.getCallback() == null) {
            android.util.Slog.w(TAG, "Received onRecognition event without callback for keyphrase model.");
            return;
        }
        if (!event.recognitionStillActive) {
            modelData.setStopped();
        }
        try {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.RECOGNITION, modelData.getModelId()));
            modelData.getCallback().onKeyphraseDetected(event);
            android.hardware.soundtrigger.SoundTrigger.RecognitionConfig config = modelData.getRecognitionConfig();
            if (config != null) {
                modelData.setRequested(config.allowMultipleTriggers);
            }
            if (modelData.isRequested()) {
                updateRecognitionLocked(modelData, true);
            }
        } catch (android.os.RemoteException e) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.RECOGNITION, modelData.getModelId(), "RemoteException").printLog(2, TAG));
            forceStopAndUnloadModelLocked(modelData, e);
        }
    }

    private void updateAllRecognitionsLocked() {
        java.util.ArrayList<com.android.server.soundtrigger.SoundTriggerHelper.ModelData> modelDatas = new java.util.ArrayList<>(this.mModelDataMap.values());
        for (com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData : modelDatas) {
            updateRecognitionLocked(modelData, true);
        }
    }

    private int updateRecognitionLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData model, boolean notifyClientOnError) {
        boolean shouldStartModel = model.isRequested() && isRecognitionAllowed(model);
        if (shouldStartModel == model.isModelStarted()) {
            return 0;
        }
        if (shouldStartModel) {
            int status = prepareForRecognition(model);
            if (status != 0) {
                android.util.Slog.w(TAG, "startRecognition failed to prepare model for recognition");
                return status;
            }
            return startRecognitionLocked(model, notifyClientOnError);
        }
        return stopRecognitionLocked(model, notifyClientOnError);
    }

    private void onServiceDiedLocked() {
        try {
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_service_died", 1);
            for (com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData : this.mModelDataMap.values()) {
                android.hardware.soundtrigger.IRecognitionStatusCallback callback = modelData.getCallback();
                if (callback != null) {
                    try {
                        this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.MODULE_DIED, modelData.getModelId()).printLog(2, TAG));
                        callback.onModuleDied();
                    } catch (android.os.RemoteException e) {
                        this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.MODULE_DIED, modelData.getModelId(), "RemoteException").printLog(2, TAG));
                    }
                }
            }
            internalClearModelStateLocked();
            if (this.mModule != null) {
                this.mModule.detach();
                try {
                    this.mModule = this.mModuleProvider.apply(this);
                } catch (java.lang.Exception e2) {
                    this.mModule = null;
                }
            }
        } catch (java.lang.Throwable th) {
            internalClearModelStateLocked();
            if (this.mModule != null) {
                this.mModule.detach();
                try {
                    this.mModule = this.mModuleProvider.apply(this);
                } catch (java.lang.Exception e3) {
                    this.mModule = null;
                }
            }
            throw th;
        }
    }

    private void internalClearModelStateLocked() {
        for (com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData : this.mModelDataMap.values()) {
            modelData.clearState();
        }
    }

    public void detach() {
        synchronized (this.mLock) {
            if (this.mIsDetached) {
                return;
            }
            this.mIsDetached = true;
            for (com.android.server.soundtrigger.SoundTriggerHelper.ModelData model : this.mModelDataMap.values()) {
                forceStopAndUnloadModelLocked(model, null);
            }
            this.mModelDataMap.clear();
            if (this.mModule != null) {
                this.mModule.detach();
                this.mModule = null;
            }
        }
    }

    private void forceStopAndUnloadModelLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData, java.lang.Exception exception) {
        forceStopAndUnloadModelLocked(modelData, exception, null);
    }

    private void forceStopAndUnloadModelLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData, java.lang.Exception exception, java.util.Iterator modelDataIterator) {
        if (exception != null) {
            android.util.Slog.e(TAG, "forceStopAndUnloadModel", exception);
        }
        if (this.mModule == null) {
            return;
        }
        if (modelData.isModelStarted()) {
            android.util.Slog.d(TAG, "Stopping previously started dangling model " + modelData.getHandle());
            if (this.mModule.stopRecognition(modelData.getHandle()) != 0) {
                android.util.Slog.e(TAG, "Failed to stop model " + modelData.getHandle());
            } else {
                modelData.setStopped();
                modelData.setRequested(false);
            }
        }
        if (modelData.isModelLoaded()) {
            android.util.Slog.d(TAG, "Unloading previously loaded dangling model " + modelData.getHandle());
            if (this.mModule.unloadSoundModel(modelData.getHandle()) != 0) {
                android.util.Slog.e(TAG, "Failed to unload model " + modelData.getHandle());
                return;
            }
            if (modelDataIterator != null) {
                modelDataIterator.remove();
            } else {
                this.mModelDataMap.remove(modelData.getModelId());
            }
            java.util.Iterator<java.util.Map.Entry<java.lang.Integer, java.util.UUID>> it = this.mKeyphraseUuidMap.entrySet().iterator();
            while (it.hasNext()) {
                if (it.next().getValue().equals(modelData.getModelId())) {
                    it.remove();
                }
            }
            modelData.clearState();
        }
    }

    private void stopAndUnloadDeadModelsLocked() {
        java.util.Iterator<java.util.Map.Entry<java.util.UUID, com.android.server.soundtrigger.SoundTriggerHelper.ModelData>> it = this.mModelDataMap.entrySet().iterator();
        while (it.hasNext()) {
            com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = it.next().getValue();
            if (modelData.isModelLoaded() && (modelData.getCallback() == null || (modelData.getCallback().asBinder() != null && !modelData.getCallback().asBinder().pingBinder()))) {
                android.util.Slog.w(TAG, "Removing model " + modelData.getHandle() + " that has no clients");
                forceStopAndUnloadModelLocked(modelData, null, it);
            }
        }
    }

    private com.android.server.soundtrigger.SoundTriggerHelper.ModelData getOrCreateGenericModelDataLocked(java.util.UUID modelId) {
        com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = this.mModelDataMap.get(modelId);
        if (modelData == null) {
            com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData2 = com.android.server.soundtrigger.SoundTriggerHelper.ModelData.createGenericModelData(modelId);
            this.mModelDataMap.put(modelId, modelData2);
            return modelData2;
        }
        if (!modelData.isGenericModel()) {
            android.util.Slog.e(TAG, "UUID already used for non-generic model.");
            return null;
        }
        return modelData;
    }

    private void removeKeyphraseModelLocked(int keyphraseId) {
        java.util.UUID uuid = this.mKeyphraseUuidMap.get(java.lang.Integer.valueOf(keyphraseId));
        if (uuid == null) {
            return;
        }
        this.mModelDataMap.remove(uuid);
        this.mKeyphraseUuidMap.remove(java.lang.Integer.valueOf(keyphraseId));
    }

    private com.android.server.soundtrigger.SoundTriggerHelper.ModelData getKeyphraseModelDataLocked(int keyphraseId) {
        java.util.UUID uuid = this.mKeyphraseUuidMap.get(java.lang.Integer.valueOf(keyphraseId));
        if (uuid == null) {
            return null;
        }
        return this.mModelDataMap.get(uuid);
    }

    private com.android.server.soundtrigger.SoundTriggerHelper.ModelData createKeyphraseModelDataLocked(java.util.UUID modelId, int keyphraseId) {
        this.mKeyphraseUuidMap.remove(java.lang.Integer.valueOf(keyphraseId));
        this.mModelDataMap.remove(modelId);
        this.mKeyphraseUuidMap.put(java.lang.Integer.valueOf(keyphraseId), modelId);
        com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData = com.android.server.soundtrigger.SoundTriggerHelper.ModelData.createKeyphraseModelData(modelId);
        this.mModelDataMap.put(modelId, modelData);
        return modelData;
    }

    private com.android.server.soundtrigger.SoundTriggerHelper.ModelData getModelDataForLocked(int modelHandle) {
        for (com.android.server.soundtrigger.SoundTriggerHelper.ModelData model : this.mModelDataMap.values()) {
            if (model.getHandle() == modelHandle) {
                return model;
            }
        }
        return null;
    }

    private boolean isRecognitionAllowed(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData) {
        android.util.Slog.d(TAG, "mIsAppOpPermitted = " + this.mIsAppOpPermitted + ",mDeviceState = " + this.mDeviceState);
        if (!this.mIsAppOpPermitted) {
            return false;
        }
        switch (this.mDeviceState) {
            case DISABLE:
                return false;
            case CRITICAL:
                return modelData.shouldRunInBatterySaverMode();
            case ENABLE:
                return true;
            default:
                throw new java.lang.AssertionError("Enum changed between compile and runtime");
        }
    }

    private int startRecognitionLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData, boolean notifyClientOnError) {
        android.hardware.soundtrigger.IRecognitionStatusCallback callback = modelData.getCallback();
        android.hardware.soundtrigger.SoundTrigger.RecognitionConfig config = modelData.getRecognitionConfig();
        if (callback == null || !modelData.isModelLoaded() || config == null) {
            android.util.Slog.w(TAG, "startRecognition: Bad data passed in.");
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_start_recognition_error", 1);
            return Integer.MIN_VALUE;
        }
        if (!isRecognitionAllowed(modelData)) {
            android.util.Slog.w(TAG, "startRecognition requested but not allowed.");
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_start_recognition_not_allowed", 1);
            return 0;
        }
        if (this.mModule == null) {
            return Integer.MIN_VALUE;
        }
        int status = 0;
        try {
            modelData.setToken(this.mModule.startRecognitionWithToken(modelData.getHandle(), config));
        } catch (java.lang.Exception e) {
            status = android.hardware.soundtrigger.SoundTrigger.handleException(e);
        }
        if (status != 0) {
            android.util.Slog.w(TAG, "startRecognition failed with " + status);
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_start_recognition_error", 1);
            if (notifyClientOnError) {
                try {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.RESUME_FAILED, modelData.getModelId(), java.lang.String.valueOf(status)).printLog(2, TAG));
                    modelData.setRequested(false);
                    callback.onResumeFailed(status);
                } catch (android.os.RemoteException e2) {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.RESUME_FAILED, modelData.getModelId(), java.lang.String.valueOf(status) + " - RemoteException").printLog(2, TAG));
                    forceStopAndUnloadModelLocked(modelData, e2);
                }
            }
        } else {
            android.util.Slog.i(TAG, "startRecognition successful.");
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_start_recognition_success", 1);
            modelData.setStarted();
            if (notifyClientOnError) {
                try {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.RESUME, modelData.getModelId()));
                    callback.onRecognitionResumed();
                } catch (android.os.RemoteException e3) {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.RESUME, modelData.getModelId(), "RemoteException").printLog(2, TAG));
                    forceStopAndUnloadModelLocked(modelData, e3);
                }
            }
        }
        return status;
    }

    private int stopRecognitionLocked(com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData, boolean notify) {
        if (this.mModule == null) {
            return Integer.MIN_VALUE;
        }
        android.hardware.soundtrigger.IRecognitionStatusCallback callback = modelData.getCallback();
        int status = this.mModule.stopRecognition(modelData.getHandle());
        if (status != 0) {
            android.util.Slog.e(TAG, "stopRecognition call failed with " + status);
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_stop_recognition_error", 1);
            if (notify) {
                try {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.PAUSE_FAILED, modelData.getModelId(), java.lang.String.valueOf(status)).printLog(2, TAG));
                    modelData.setRequested(false);
                    callback.onPauseFailed(status);
                } catch (android.os.RemoteException e) {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.PAUSE_FAILED, modelData.getModelId(), java.lang.String.valueOf(status) + " - RemoteException").printLog(2, TAG));
                    forceStopAndUnloadModelLocked(modelData, e);
                }
            }
        } else {
            modelData.setStopped();
            com.android.internal.logging.MetricsLogger.count(this.mContext, "sth_stop_recognition_success", 1);
            if (notify) {
                try {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.PAUSE, modelData.getModelId()));
                    callback.onRecognitionPaused();
                } catch (android.os.RemoteException e2) {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.PAUSE, modelData.getModelId(), "RemoteException").printLog(2, TAG));
                    forceStopAndUnloadModelLocked(modelData, e2);
                }
            }
        }
        return status;
    }

    private boolean computeRecognitionRequestedLocked() {
        if (this.mModule == null) {
            this.mRecognitionRequested = false;
            return this.mRecognitionRequested;
        }
        for (com.android.server.soundtrigger.SoundTriggerHelper.ModelData modelData : this.mModelDataMap.values()) {
            if (modelData.isRequested()) {
                this.mRecognitionRequested = true;
                return this.mRecognitionRequested;
            }
        }
        this.mRecognitionRequested = false;
        return this.mRecognitionRequested;
    }

    private static class ModelData {
        static final int MODEL_LOADED = 1;
        static final int MODEL_NOTLOADED = 0;
        static final int MODEL_STARTED = 2;
        private int mModelHandle;
        private java.util.UUID mModelId;
        private int mModelState;
        private int mModelType;
        private boolean mRequested = false;
        private android.hardware.soundtrigger.IRecognitionStatusCallback mCallback = null;
        private android.hardware.soundtrigger.SoundTrigger.RecognitionConfig mRecognitionConfig = null;
        public boolean mRunInBatterySaverMode = false;
        private android.hardware.soundtrigger.SoundTrigger.SoundModel mSoundModel = null;
        private android.os.IBinder mRecognitionToken = null;

        private ModelData(java.util.UUID modelId, int modelType) {
            this.mModelType = -1;
            this.mModelId = modelId;
            this.mModelType = modelType;
        }

        static com.android.server.soundtrigger.SoundTriggerHelper.ModelData createKeyphraseModelData(java.util.UUID modelId) {
            return new com.android.server.soundtrigger.SoundTriggerHelper.ModelData(modelId, 0);
        }

        static com.android.server.soundtrigger.SoundTriggerHelper.ModelData createGenericModelData(java.util.UUID modelId) {
            return new com.android.server.soundtrigger.SoundTriggerHelper.ModelData(modelId, 1);
        }

        static com.android.server.soundtrigger.SoundTriggerHelper.ModelData createModelDataOfUnknownType(java.util.UUID modelId) {
            return new com.android.server.soundtrigger.SoundTriggerHelper.ModelData(modelId, -1);
        }

        synchronized void setCallback(android.hardware.soundtrigger.IRecognitionStatusCallback callback) {
            this.mCallback = callback;
        }

        synchronized android.hardware.soundtrigger.IRecognitionStatusCallback getCallback() {
            return this.mCallback;
        }

        synchronized boolean isModelLoaded() {
            boolean z;
            z = true;
            if (this.mModelState != 1) {
                if (this.mModelState != 2) {
                    z = false;
                }
            }
            return z;
        }

        synchronized boolean isModelNotLoaded() {
            return this.mModelState == 0;
        }

        synchronized void setStarted() {
            this.mModelState = 2;
        }

        synchronized void setStopped() {
            this.mRecognitionToken = null;
            this.mModelState = 1;
        }

        synchronized void setLoaded() {
            this.mModelState = 1;
        }

        synchronized void setNotLoaded() {
            this.mRecognitionToken = null;
            this.mModelState = 0;
        }

        synchronized boolean isModelStarted() {
            return this.mModelState == 2;
        }

        synchronized void clearState() {
            this.mModelState = 0;
            this.mRecognitionToken = null;
            this.mRecognitionConfig = null;
            this.mRequested = false;
            this.mCallback = null;
        }

        synchronized void clearCallback() {
            this.mCallback = null;
        }

        synchronized void setHandle(int handle) {
            this.mModelHandle = handle;
        }

        synchronized void setRecognitionConfig(android.hardware.soundtrigger.SoundTrigger.RecognitionConfig config) {
            this.mRecognitionConfig = config;
        }

        synchronized void setRunInBatterySaverMode(boolean runInBatterySaverMode) {
            this.mRunInBatterySaverMode = runInBatterySaverMode;
        }

        synchronized boolean shouldRunInBatterySaverMode() {
            return this.mRunInBatterySaverMode;
        }

        synchronized int getHandle() {
            return this.mModelHandle;
        }

        synchronized java.util.UUID getModelId() {
            return this.mModelId;
        }

        synchronized android.hardware.soundtrigger.SoundTrigger.RecognitionConfig getRecognitionConfig() {
            return this.mRecognitionConfig;
        }

        synchronized boolean isRequested() {
            return this.mRequested;
        }

        synchronized void setRequested(boolean requested) {
            this.mRequested = requested;
        }

        synchronized void setSoundModel(android.hardware.soundtrigger.SoundTrigger.SoundModel soundModel) {
            this.mSoundModel = soundModel;
        }

        synchronized android.hardware.soundtrigger.SoundTrigger.SoundModel getSoundModel() {
            return this.mSoundModel;
        }

        synchronized android.os.IBinder getToken() {
            return this.mRecognitionToken;
        }

        synchronized void setToken(android.os.IBinder token) {
            this.mRecognitionToken = token;
        }

        synchronized int getModelType() {
            return this.mModelType;
        }

        synchronized boolean isKeyphraseModel() {
            return this.mModelType == 0;
        }

        synchronized boolean isGenericModel() {
            return this.mModelType == 1;
        }

        synchronized java.lang.String stateToString() {
            switch (this.mModelState) {
                case 0:
                    return "NOT_LOADED";
                case 1:
                    return "LOADED";
                case 2:
                    return "STARTED";
                default:
                    return "Unknown state";
            }
        }

        synchronized java.lang.String requestedToString() {
            return "Requested: " + (this.mRequested ? "Yes" : "No");
        }

        synchronized java.lang.String callbackToString() {
            return "Callback: " + (this.mCallback != null ? this.mCallback.asBinder() : "null");
        }

        synchronized java.lang.String uuidToString() {
            return "UUID: " + this.mModelId;
        }

        public synchronized java.lang.String toString() {
            return "Handle: " + this.mModelHandle + "\nModelState: " + stateToString() + "\n" + requestedToString() + "\n" + callbackToString() + "\n" + uuidToString() + "\n" + modelTypeToString() + "RunInBatterySaverMode=" + this.mRunInBatterySaverMode;
        }

        synchronized java.lang.String modelTypeToString() {
            java.lang.String type;
            type = null;
            switch (this.mModelType) {
                case -1:
                    type = "Unknown";
                    break;
                case 0:
                    type = "Keyphrase";
                    break;
                case 1:
                    type = "Generic";
                    break;
            }
            return "Model type: " + type + "\n";
        }
    }
}
