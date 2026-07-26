package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerMiddlewareLogging implements com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal, com.android.server.soundtrigger_middleware.Dumpable {
    private static final int SESSION_MAX_EVENT_SIZE = 128;
    private static final java.lang.String TAG = "SoundTriggerMiddlewareLogging";
    private final java.util.function.Supplier<android.os.BatteryStatsInternal> mBatteryStatsInternalSupplier;
    private final com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal mDelegate;
    private final java.util.Deque<com.android.server.utils.EventLogger> mDetachedSessionEventLoggers;
    private final com.android.internal.util.LatencyTracker mLatencyTracker;
    private final com.android.server.utils.EventLogger mServiceEventLogger;
    private final java.util.concurrent.atomic.AtomicInteger mSessionCount;
    private final java.util.Set<com.android.server.utils.EventLogger> mSessionEventLoggers;

    public SoundTriggerMiddlewareLogging(android.content.Context context, com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal delegate) {
        this(com.android.internal.util.LatencyTracker.getInstance(context), new java.util.function.Supplier() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.BatteryStatsHolder.INSTANCE;
            }
        }, delegate);
    }

    public SoundTriggerMiddlewareLogging(com.android.internal.util.LatencyTracker latencyTracker, java.util.function.Supplier<android.os.BatteryStatsInternal> batteryStatsInternalSupplier, com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal delegate) {
        this.mServiceEventLogger = new com.android.server.utils.EventLogger(256, "Service Events");
        this.mSessionEventLoggers = java.util.concurrent.ConcurrentHashMap.newKeySet(4);
        this.mDetachedSessionEventLoggers = new java.util.concurrent.LinkedBlockingDeque(4);
        this.mSessionCount = new java.util.concurrent.atomic.AtomicInteger(0);
        this.mDelegate = delegate;
        this.mLatencyTracker = latencyTracker;
        this.mBatteryStatsInternalSupplier = batteryStatsInternalSupplier;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] listModules() throws java.lang.Exception {
        try {
            android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] result = this.mDelegate.listModules();
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ModulePropertySummary[] moduleSummary = (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ModulePropertySummary[]) java.util.Arrays.stream(result).map(new java.util.function.Function() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.lambda$listModules$1((android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor) obj);
                }
            }).toArray(new java.util.function.IntFunction() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging$$ExternalSyntheticLambda2
                @Override // java.util.function.IntFunction
                public final java.lang.Object apply(int i) {
                    return com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.lambda$listModules$2(i);
                }
            });
            this.mServiceEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.Type.LIST_MODULE, android.media.permission.IdentityContext.get().packageName, moduleSummary, new java.lang.Object[0]).printLog(0, TAG));
            return result;
        } catch (java.lang.Exception e) {
            this.mServiceEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.Type.LIST_MODULE, android.media.permission.IdentityContext.get().packageName, e, new java.lang.Object[0]).printLog(2, TAG));
            throw e;
        }
    }

    static /* synthetic */ com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ModulePropertySummary lambda$listModules$1(android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor descriptor) {
        return new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ModulePropertySummary(descriptor.handle, descriptor.properties.implementor, descriptor.properties.version);
    }

    static /* synthetic */ com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ModulePropertySummary[] lambda$listModules$2(int x$0) {
        return new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ModulePropertySummary[x$0];
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public android.media.soundtrigger_middleware.ISoundTriggerModule attach(int handle, android.media.soundtrigger_middleware.ISoundTriggerCallback callback, boolean isTrusted) throws java.lang.Exception {
        try {
            android.media.permission.Identity originatorIdentity = android.media.permission.IdentityContext.getNonNull();
            java.lang.String packageIdentification = originatorIdentity.packageName + this.mSessionCount.getAndIncrement() + (isTrusted ? "trusted" : "");
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ModuleLogging result = new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ModuleLogging();
            com.android.server.utils.EventLogger eventLogger = new com.android.server.utils.EventLogger(128, "Session logger for: " + packageIdentification);
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.CallbackLogging callbackWrapper = new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.CallbackLogging(callback, eventLogger, originatorIdentity);
            result.attach(this.mDelegate.attach(handle, callbackWrapper, isTrusted), eventLogger);
            this.mServiceEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.Type.ATTACH, packageIdentification, result, java.lang.Integer.valueOf(handle), callback, java.lang.Boolean.valueOf(isTrusted)).printLog(0, TAG));
            this.mSessionEventLoggers.add(eventLogger);
            return result;
        } catch (java.lang.Exception e) {
            this.mServiceEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.Type.ATTACH, android.media.permission.IdentityContext.get().packageName, e, java.lang.Integer.valueOf(handle), callback).printLog(2, TAG));
            throw e;
        }
    }

    public java.lang.String toString() {
        return this.mDelegate.toString();
    }

    private class ModuleLogging implements android.media.soundtrigger_middleware.ISoundTriggerModule {
        private android.media.soundtrigger_middleware.ISoundTriggerModule mDelegate;
        private com.android.server.utils.EventLogger mEventLogger;

        private ModuleLogging() {
        }

        void attach(android.media.soundtrigger_middleware.ISoundTriggerModule delegate, com.android.server.utils.EventLogger eventLogger) {
            this.mDelegate = delegate;
            this.mEventLogger = eventLogger;
        }

        public int loadModel(android.media.soundtrigger.SoundModel model) throws java.lang.Exception {
            try {
                int result = this.mDelegate.loadModel(model);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.LOAD_MODEL, java.lang.Integer.valueOf(result), model.uuid).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                return result;
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.LOAD_MODEL, e, model.uuid).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public int loadPhraseModel(android.media.soundtrigger.PhraseSoundModel model) throws java.lang.Exception {
            try {
                int result = this.mDelegate.loadPhraseModel(model);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.LOAD_PHRASE_MODEL, java.lang.Integer.valueOf(result), model.common.uuid).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                return result;
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.LOAD_PHRASE_MODEL, e, model.common.uuid).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public void unloadModel(int modelHandle) throws java.lang.Exception {
            try {
                this.mDelegate.unloadModel(modelHandle);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.UNLOAD_MODEL, java.lang.Integer.valueOf(modelHandle)).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.UNLOAD_MODEL, e, java.lang.Integer.valueOf(modelHandle)).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public android.os.IBinder startRecognition(int modelHandle, android.media.soundtrigger.RecognitionConfig config) throws java.lang.Exception {
            try {
                android.os.IBinder result = this.mDelegate.startRecognition(modelHandle, config);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.START_RECOGNITION, result, java.lang.Integer.valueOf(modelHandle), config).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                return result;
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.START_RECOGNITION, e, java.lang.Integer.valueOf(modelHandle), config).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public void stopRecognition(int modelHandle) throws java.lang.Exception {
            try {
                this.mDelegate.stopRecognition(modelHandle);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.STOP_RECOGNITION, java.lang.Integer.valueOf(modelHandle)).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.STOP_RECOGNITION, e, java.lang.Integer.valueOf(modelHandle)).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public void forceRecognitionEvent(int modelHandle) throws java.lang.Exception {
            try {
                this.mDelegate.forceRecognitionEvent(modelHandle);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.FORCE_RECOGNITION, java.lang.Integer.valueOf(modelHandle)).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.FORCE_RECOGNITION, e, java.lang.Integer.valueOf(modelHandle)).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public void setModelParameter(int modelHandle, int modelParam, int value) throws java.lang.Exception {
            try {
                this.mDelegate.setModelParameter(modelHandle, modelParam, value);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.SET_MODEL_PARAMETER, java.lang.Integer.valueOf(modelHandle), java.lang.Integer.valueOf(modelParam), java.lang.Integer.valueOf(value)).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.SET_MODEL_PARAMETER, e, java.lang.Integer.valueOf(modelHandle), java.lang.Integer.valueOf(modelParam), java.lang.Integer.valueOf(value)).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public int getModelParameter(int modelHandle, int modelParam) throws java.lang.Exception {
            try {
                int result = this.mDelegate.getModelParameter(modelHandle, modelParam);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.GET_MODEL_PARAMETER, java.lang.Integer.valueOf(result), java.lang.Integer.valueOf(modelHandle), java.lang.Integer.valueOf(modelParam)).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                return result;
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.GET_MODEL_PARAMETER, e, java.lang.Integer.valueOf(modelHandle), java.lang.Integer.valueOf(modelParam)).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public android.media.soundtrigger.ModelParameterRange queryModelParameterSupport(int modelHandle, int modelParam) throws java.lang.Exception {
            try {
                android.media.soundtrigger.ModelParameterRange result = this.mDelegate.queryModelParameterSupport(modelHandle, modelParam);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.QUERY_MODEL_PARAMETER, result, java.lang.Integer.valueOf(modelHandle), java.lang.Integer.valueOf(modelParam)).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                return result;
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.QUERY_MODEL_PARAMETER, e, java.lang.Integer.valueOf(modelHandle), java.lang.Integer.valueOf(modelParam)).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public void detach() throws java.lang.Exception {
            try {
                if (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.this.mSessionEventLoggers.remove(this.mEventLogger)) {
                    while (!com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.this.mDetachedSessionEventLoggers.offerFirst(this.mEventLogger)) {
                        com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.this.mDetachedSessionEventLoggers.pollLast();
                    }
                }
                this.mDelegate.detach();
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.DETACH, new java.lang.Object[0]).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.DETACH, e, new java.lang.Object[0]).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public android.os.IBinder asBinder() {
            return this.mDelegate.asBinder();
        }

        public java.lang.String toString() {
            return java.util.Objects.toString(this.mDelegate);
        }
    }

    private class CallbackLogging implements android.media.soundtrigger_middleware.ISoundTriggerCallback {
        private final android.media.soundtrigger_middleware.ISoundTriggerCallback mCallbackDelegate;
        private final com.android.server.utils.EventLogger mEventLogger;
        private final android.media.permission.Identity mOriginatorIdentity;

        private CallbackLogging(android.media.soundtrigger_middleware.ISoundTriggerCallback delegate, com.android.server.utils.EventLogger eventLogger, android.media.permission.Identity originatorIdentity) {
            this.mCallbackDelegate = (android.media.soundtrigger_middleware.ISoundTriggerCallback) java.util.Objects.requireNonNull(delegate);
            this.mEventLogger = (com.android.server.utils.EventLogger) java.util.Objects.requireNonNull(eventLogger);
            this.mOriginatorIdentity = originatorIdentity;
        }

        public void onRecognition(int modelHandle, android.media.soundtrigger_middleware.RecognitionEventSys event, int captureSession) throws java.lang.Exception {
            try {
                ((android.os.BatteryStatsInternal) com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.this.mBatteryStatsInternalSupplier.get()).noteWakingSoundTrigger(android.os.SystemClock.elapsedRealtime(), this.mOriginatorIdentity.uid);
                this.mCallbackDelegate.onRecognition(modelHandle, event, captureSession);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.RECOGNITION, java.lang.Integer.valueOf(modelHandle), event, java.lang.Integer.valueOf(captureSession)).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.RECOGNITION, e, java.lang.Integer.valueOf(modelHandle), event, java.lang.Integer.valueOf(captureSession)).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public void onPhraseRecognition(int modelHandle, android.media.soundtrigger_middleware.PhraseRecognitionEventSys event, int captureSession) throws java.lang.Exception {
            try {
                ((android.os.BatteryStatsInternal) com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.this.mBatteryStatsInternalSupplier.get()).noteWakingSoundTrigger(android.os.SystemClock.elapsedRealtime(), this.mOriginatorIdentity.uid);
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.this.startKeyphraseEventLatencyTracking(event.phraseRecognitionEvent);
                this.mCallbackDelegate.onPhraseRecognition(modelHandle, event, captureSession);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.RECOGNITION, java.lang.Integer.valueOf(modelHandle), event, java.lang.Integer.valueOf(captureSession)).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.RECOGNITION, e, java.lang.Integer.valueOf(modelHandle), event, java.lang.Integer.valueOf(captureSession)).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public void onModelUnloaded(int modelHandle) throws java.lang.Exception {
            try {
                this.mCallbackDelegate.onModelUnloaded(modelHandle);
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.MODEL_UNLOADED, java.lang.Integer.valueOf(modelHandle)).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.MODEL_UNLOADED, e, java.lang.Integer.valueOf(modelHandle)).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public void onResourcesAvailable() throws java.lang.Exception {
            try {
                this.mCallbackDelegate.onResourcesAvailable();
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.RESOURCES_AVAILABLE, new java.lang.Object[0]).printLog(0, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.RESOURCES_AVAILABLE, e, new java.lang.Object[0]).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public void onModuleDied() throws java.lang.Exception {
            try {
                this.mCallbackDelegate.onModuleDied();
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.MODULE_DIED, new java.lang.Object[0]).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
            } catch (java.lang.Exception e) {
                this.mEventLogger.enqueue(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type.MODULE_DIED, e, new java.lang.Object[0]).printLog(2, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.TAG));
                throw e;
            }
        }

        public android.os.IBinder asBinder() {
            return this.mCallbackDelegate.asBinder();
        }

        public java.lang.String toString() {
            return java.util.Objects.toString(this.mCallbackDelegate);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class BatteryStatsHolder {
        private static final android.os.BatteryStatsInternal INSTANCE = (android.os.BatteryStatsInternal) com.android.server.LocalServices.getService(android.os.BatteryStatsInternal.class);

        private BatteryStatsHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startKeyphraseEventLatencyTracking(android.media.soundtrigger.PhraseRecognitionEvent event) {
        if (event.common.status != 0 || com.android.internal.util.ArrayUtils.isEmpty(event.phraseExtras)) {
            return;
        }
        java.lang.String latencyTrackerTag = "KeyphraseId=" + event.phraseExtras[0].id;
        this.mLatencyTracker.onActionCancel(19);
        this.mLatencyTracker.onActionStart(19, latencyTrackerTag);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.StringBuilder printArgs(java.lang.StringBuilder builder, java.lang.Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            com.android.server.soundtrigger_middleware.ObjectPrinter.print(builder, args[i]);
        }
        return builder;
    }

    @Override // com.android.server.soundtrigger_middleware.Dumpable
    public void dump(java.io.PrintWriter pw) {
        pw.println("##Service-Wide logs:");
        this.mServiceEventLogger.dump(pw, "  ");
        pw.println("\n##Active Session dumps:\n");
        for (com.android.server.utils.EventLogger sessionLogger : this.mSessionEventLoggers) {
            sessionLogger.dump(pw, "  ");
            pw.println("");
        }
        pw.println("##Detached Session dumps:\n");
        for (com.android.server.utils.EventLogger sessionLogger2 : this.mDetachedSessionEventLoggers) {
            sessionLogger2.dump(pw, "  ");
            pw.println("");
        }
        if (this.mDelegate instanceof com.android.server.soundtrigger_middleware.Dumpable) {
            ((com.android.server.soundtrigger_middleware.Dumpable) this.mDelegate).dump(pw);
        }
    }

    public static void printSystemLog(int type, java.lang.String tag, java.lang.String message, java.lang.Exception e) {
        switch (type) {
            case 0:
                android.util.Slog.i(tag, message, e);
                break;
            case 1:
                android.util.Slog.e(tag, message, e);
                break;
            case 2:
                android.util.Slog.w(tag, message, e);
                break;
            default:
                android.util.Slog.v(tag, message, e);
                break;
        }
    }

    public static class ServiceEvent extends com.android.server.utils.EventLogger.Event {
        private final java.lang.Exception mException;
        private final java.lang.String mPackageName;
        private final java.lang.Object[] mParams;
        private final java.lang.Object mReturnValue;
        private final com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.Type mType;

        public enum Type {
            ATTACH,
            LIST_MODULE
        }

        public static com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.Type type, java.lang.String packageName, java.lang.Exception exception, java.lang.Object... params) {
            return new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent(exception, type, packageName, null, params);
        }

        public static com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.Type type, java.lang.String packageName, java.lang.Object returnValue, java.lang.Object... params) {
            return new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent(null, type, packageName, returnValue, params);
        }

        private ServiceEvent(java.lang.Exception exception, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.ServiceEvent.Type type, java.lang.String packageName, java.lang.Object returnValue, java.lang.Object... params) {
            this.mException = exception;
            this.mType = type;
            this.mPackageName = packageName;
            this.mReturnValue = returnValue;
            this.mParams = params;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public com.android.server.utils.EventLogger.Event printLog(int type, java.lang.String tag) {
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.printSystemLog(type, tag, eventToString(), this.mException);
            return this;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(this.mType.name()).append(" [client= ");
            com.android.server.soundtrigger_middleware.ObjectPrinter.print(sb, this.mPackageName);
            sb.append("] (");
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.printArgs(sb, this.mParams);
            sb.append(") -> ");
            if (this.mException != null) {
                sb.append("ERROR: ");
                com.android.server.soundtrigger_middleware.ObjectPrinter.print(sb, this.mException);
            } else {
                com.android.server.soundtrigger_middleware.ObjectPrinter.print(sb, this.mReturnValue);
            }
            return sb.toString();
        }
    }

    public static class SessionEvent extends com.android.server.utils.EventLogger.Event {
        private final java.lang.Exception mException;
        private final java.lang.Object[] mParams;
        private final java.lang.Object mReturnValue;
        private final com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type mType;

        public enum Type {
            LOAD_MODEL,
            LOAD_PHRASE_MODEL,
            START_RECOGNITION,
            STOP_RECOGNITION,
            FORCE_RECOGNITION,
            UNLOAD_MODEL,
            GET_MODEL_PARAMETER,
            SET_MODEL_PARAMETER,
            QUERY_MODEL_PARAMETER,
            DETACH,
            RECOGNITION,
            MODEL_UNLOADED,
            MODULE_DIED,
            RESOURCES_AVAILABLE
        }

        public static com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent createForException(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type type, java.lang.Exception exception, java.lang.Object... params) {
            return new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent(exception, type, null, params);
        }

        public static com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent createForReturn(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type type, java.lang.Object returnValue, java.lang.Object... params) {
            return new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent(null, type, returnValue, params);
        }

        public static com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent createForVoid(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type type, java.lang.Object... params) {
            return new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent(null, type, null, params);
        }

        private SessionEvent(java.lang.Exception exception, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.SessionEvent.Type type, java.lang.Object returnValue, java.lang.Object... params) {
            this.mException = exception;
            this.mType = type;
            this.mReturnValue = returnValue;
            this.mParams = params;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public com.android.server.utils.EventLogger.Event printLog(int type, java.lang.String tag) {
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.printSystemLog(type, tag, eventToString(), this.mException);
            return this;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(this.mType.name());
            sb.append(" (");
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging.printArgs(sb, this.mParams);
            sb.append(")");
            if (this.mException != null) {
                sb.append(" -> ERROR: ");
                com.android.server.soundtrigger_middleware.ObjectPrinter.print(sb, this.mException);
            } else if (this.mReturnValue != null) {
                sb.append(" -> ");
                com.android.server.soundtrigger_middleware.ObjectPrinter.print(sb, this.mReturnValue);
            }
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class ModulePropertySummary {
        private int mId;
        private java.lang.String mImplementor;
        private int mVersion;

        ModulePropertySummary(int id, java.lang.String implementor, int version) {
            this.mId = id;
            this.mImplementor = implementor;
            this.mVersion = version;
        }

        public java.lang.String toString() {
            return "{Id: " + this.mId + ", Implementor: " + this.mImplementor + ", Version: " + this.mVersion + "}";
        }
    }
}
