package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerMiddlewareValidation implements com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal, com.android.server.soundtrigger_middleware.Dumpable {
    private static final java.lang.String TAG = "SoundTriggerMiddlewareValidation";
    private final com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal mDelegate;
    private java.util.Map<java.lang.Integer, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleState> mModules;

    private enum ModuleStatus {
        ALIVE,
        DETACHED,
        DEAD
    }

    private class ModuleState {
        public android.media.soundtrigger.Properties properties;
        public java.util.Set<com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session> sessions;

        private ModuleState(android.media.soundtrigger.Properties properties) {
            this.sessions = new java.util.HashSet();
            this.properties = properties;
        }
    }

    public SoundTriggerMiddlewareValidation(com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal delegate) {
        this.mDelegate = delegate;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    static java.lang.RuntimeException handleException(java.lang.Exception e) throws android.os.ServiceSpecificException {
        if (e instanceof com.android.server.soundtrigger_middleware.RecoverableException) {
            throw new android.os.ServiceSpecificException(((com.android.server.soundtrigger_middleware.RecoverableException) e).errorCode, e.getMessage());
        }
        android.util.Slog.wtf(TAG, "Unexpected exception", e);
        throw new android.os.ServiceSpecificException(5, e.getMessage());
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] listModules() {
        android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] result;
        synchronized (this) {
            try {
                result = this.mDelegate.listModules();
                int i = 0;
                if (this.mModules == null) {
                    this.mModules = new java.util.HashMap(result.length);
                    int length = result.length;
                    while (i < length) {
                        android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor desc = result[i];
                        this.mModules.put(java.lang.Integer.valueOf(desc.handle), new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleState(desc.properties));
                        i++;
                    }
                } else {
                    if (result.length != this.mModules.size()) {
                        throw new java.lang.RuntimeException("listModules must always return the same result.");
                    }
                    int length2 = result.length;
                    while (i < length2) {
                        android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor desc2 = result[i];
                        if (!this.mModules.containsKey(java.lang.Integer.valueOf(desc2.handle))) {
                            throw new java.lang.RuntimeException("listModules must always return the same result.");
                        }
                        this.mModules.get(java.lang.Integer.valueOf(desc2.handle)).properties = desc2.properties;
                        i++;
                    }
                }
            } catch (java.lang.Exception e) {
                throw handleException(e);
            }
        }
        return result;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public android.media.soundtrigger_middleware.ISoundTriggerModule attach(int handle, android.media.soundtrigger_middleware.ISoundTriggerCallback callback, boolean isTrusted) {
        com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session session;
        java.util.Objects.requireNonNull(callback);
        java.util.Objects.requireNonNull(callback.asBinder());
        synchronized (this) {
            if (this.mModules == null) {
                throw new java.lang.IllegalStateException("Client must call listModules() prior to attaching.");
            }
            if (!this.mModules.containsKey(java.lang.Integer.valueOf(handle))) {
                throw new java.lang.IllegalArgumentException("Invalid handle: " + handle);
            }
            try {
                session = new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session(handle, callback);
                session.attach(this.mDelegate.attach(handle, session.getCallbackWrapper(), isTrusted));
            } catch (java.lang.Exception e) {
                throw handleException(e);
            }
        }
        return session;
    }

    public java.lang.String toString() {
        return this.mDelegate.toString();
    }

    @Override // com.android.server.soundtrigger_middleware.Dumpable
    public void dump(java.io.PrintWriter pw) {
        synchronized (this) {
            if (this.mModules != null) {
                java.util.Iterator<java.lang.Integer> it = this.mModules.keySet().iterator();
                while (it.hasNext()) {
                    int handle = it.next().intValue();
                    com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleState module = this.mModules.get(java.lang.Integer.valueOf(handle));
                    pw.println("=========================================");
                    pw.printf("Module %d\n%s\n", java.lang.Integer.valueOf(handle), com.android.server.soundtrigger_middleware.ObjectPrinter.print(module.properties, 16));
                    pw.println("=========================================");
                    for (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session session : module.sessions) {
                        session.dump(pw);
                    }
                }
            } else {
                pw.println("Modules have not yet been enumerated.");
            }
        }
        pw.println();
        if (this.mDelegate instanceof com.android.server.soundtrigger_middleware.Dumpable) {
            ((com.android.server.soundtrigger_middleware.Dumpable) this.mDelegate).dump(pw);
        }
    }

    static class ModelState {
        android.media.soundtrigger.RecognitionConfig config;
        final java.lang.String description;
        com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity activityState = com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.LOADED;
        private final java.util.Map<java.lang.Integer, android.media.soundtrigger.ModelParameterRange> parameterSupport = new java.util.HashMap();

        enum Activity {
            LOADED,
            ACTIVE,
            PREEMPTED
        }

        ModelState(android.media.soundtrigger.SoundModel model) {
            this.description = com.android.server.soundtrigger_middleware.ObjectPrinter.print(model, 16);
        }

        ModelState(android.media.soundtrigger.PhraseSoundModel model) {
            this.description = com.android.server.soundtrigger_middleware.ObjectPrinter.print(model, 16);
        }

        void checkSupported(int modelParam) {
            if (!this.parameterSupport.containsKey(java.lang.Integer.valueOf(modelParam))) {
                throw new java.lang.IllegalStateException("Parameter has not been checked for support.");
            }
            android.media.soundtrigger.ModelParameterRange range = this.parameterSupport.get(java.lang.Integer.valueOf(modelParam));
            if (range == null) {
                throw new java.lang.IllegalArgumentException("Paramater is not supported.");
            }
        }

        void checkSupported(int modelParam, int value) {
            if (!this.parameterSupport.containsKey(java.lang.Integer.valueOf(modelParam))) {
                throw new java.lang.IllegalStateException("Parameter has not been checked for support.");
            }
            android.media.soundtrigger.ModelParameterRange range = this.parameterSupport.get(java.lang.Integer.valueOf(modelParam));
            if (range == null) {
                throw new java.lang.IllegalArgumentException("Paramater is not supported.");
            }
            com.android.internal.util.Preconditions.checkArgumentInRange(value, range.minInclusive, range.maxInclusive, "value");
        }
    }

    private class Session extends android.media.soundtrigger_middleware.ISoundTriggerModule.Stub {
        private final com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.CallbackWrapper mCallbackWrapper;
        private android.media.soundtrigger_middleware.ISoundTriggerModule mDelegate;
        private final int mHandle;
        private final java.util.Map<java.lang.Integer, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState> mLoadedModels = new java.util.HashMap();
        private com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus mState = com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.ALIVE;
        private final android.media.permission.Identity mOriginatorIdentity = android.media.permission.IdentityContext.get();

        Session(int handle, android.media.soundtrigger_middleware.ISoundTriggerCallback callback) {
            this.mCallbackWrapper = new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.CallbackWrapper(callback);
            this.mHandle = handle;
        }

        android.media.soundtrigger_middleware.ISoundTriggerCallback getCallbackWrapper() {
            return this.mCallbackWrapper;
        }

        void attach(android.media.soundtrigger_middleware.ISoundTriggerModule delegate) {
            this.mDelegate = delegate;
            ((com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleState) com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this.mModules.get(java.lang.Integer.valueOf(this.mHandle))).sessions.add(this);
        }

        public int loadModel(android.media.soundtrigger.SoundModel model) {
            int handle;
            com.android.server.soundtrigger_middleware.ValidationUtil.validateGenericModel(model);
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has been detached.");
                }
                try {
                    handle = this.mDelegate.loadModel(model);
                    this.mLoadedModels.put(java.lang.Integer.valueOf(handle), new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState(model));
                } catch (java.lang.Exception e) {
                    throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
                }
            }
            return handle;
        }

        public int loadPhraseModel(android.media.soundtrigger.PhraseSoundModel model) {
            int handle;
            com.android.server.soundtrigger_middleware.ValidationUtil.validatePhraseModel(model);
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has been detached.");
                }
                try {
                    handle = this.mDelegate.loadPhraseModel(model);
                    this.mLoadedModels.put(java.lang.Integer.valueOf(handle), new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState(model));
                } catch (java.lang.Exception e) {
                    throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
                }
            }
            return handle;
        }

        public void unloadModel(int modelHandle) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has been detached.");
                }
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                if (modelState == null) {
                    throw new java.lang.IllegalStateException("Invalid handle: " + modelHandle);
                }
                if (modelState.activityState != com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.LOADED && modelState.activityState != com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.PREEMPTED) {
                    throw new java.lang.IllegalStateException("Model with handle: " + modelHandle + " has invalid state for unloading");
                }
            }
            try {
                this.mDelegate.unloadModel(modelHandle);
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                    this.mLoadedModels.remove(java.lang.Integer.valueOf(modelHandle));
                }
            } catch (java.lang.Exception e) {
                throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
            }
        }

        public android.os.IBinder startRecognition(int modelHandle, android.media.soundtrigger.RecognitionConfig config) {
            android.os.IBinder result;
            com.android.server.soundtrigger_middleware.ValidationUtil.validateRecognitionConfig(config);
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has been detached.");
                }
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                if (modelState == null) {
                    throw new java.lang.IllegalStateException("Invalid handle: " + modelHandle);
                }
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity activityState = modelState.activityState;
                if (activityState != com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.LOADED && activityState != com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.PREEMPTED) {
                    throw new java.lang.IllegalStateException("Model with handle: " + modelHandle + " has invalid state for starting recognition");
                }
                try {
                    result = this.mDelegate.startRecognition(modelHandle, config);
                    modelState.config = config;
                    modelState.activityState = com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.ACTIVE;
                } catch (java.lang.Exception e) {
                    throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
                }
            }
            return result;
        }

        public void stopRecognition(int modelHandle) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has been detached.");
                }
                if (this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle)) == null) {
                    throw new java.lang.IllegalStateException("Invalid handle: " + modelHandle);
                }
            }
            try {
                this.mDelegate.stopRecognition(modelHandle);
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                    com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                    if (modelState == null) {
                        return;
                    }
                    if (modelState.activityState != com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.PREEMPTED) {
                        modelState.activityState = com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.LOADED;
                    }
                }
            } catch (java.lang.Exception e) {
                throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
            }
        }

        public void forceRecognitionEvent(int modelHandle) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has been detached.");
                }
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                if (modelState == null) {
                    throw new java.lang.IllegalStateException("Invalid handle: " + modelHandle);
                }
                try {
                    if (modelState.activityState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.ACTIVE) {
                        this.mDelegate.forceRecognitionEvent(modelHandle);
                    }
                } catch (java.lang.Exception e) {
                    throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
                }
            }
        }

        public void setModelParameter(int modelHandle, int modelParam, int value) {
            com.android.server.soundtrigger_middleware.ValidationUtil.validateModelParameter(modelParam);
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has been detached.");
                }
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                if (modelState == null) {
                    throw new java.lang.IllegalStateException("Invalid handle: " + modelHandle);
                }
                modelState.checkSupported(modelParam, value);
                try {
                    this.mDelegate.setModelParameter(modelHandle, modelParam, value);
                } catch (java.lang.Exception e) {
                    throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
                }
            }
        }

        public int getModelParameter(int modelHandle, int modelParam) {
            int modelParameter;
            com.android.server.soundtrigger_middleware.ValidationUtil.validateModelParameter(modelParam);
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has been detached.");
                }
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                if (modelState == null) {
                    throw new java.lang.IllegalStateException("Invalid handle: " + modelHandle);
                }
                modelState.checkSupported(modelParam);
                try {
                    modelParameter = this.mDelegate.getModelParameter(modelHandle, modelParam);
                } catch (java.lang.Exception e) {
                    throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
                }
            }
            return modelParameter;
        }

        public android.media.soundtrigger.ModelParameterRange queryModelParameterSupport(int modelHandle, int modelParam) {
            android.media.soundtrigger.ModelParameterRange result;
            com.android.server.soundtrigger_middleware.ValidationUtil.validateModelParameter(modelParam);
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has been detached.");
                }
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                if (modelState == null) {
                    throw new java.lang.IllegalStateException("Invalid handle: " + modelHandle);
                }
                try {
                    result = this.mDelegate.queryModelParameterSupport(modelHandle, modelParam);
                    modelState.parameterSupport.put(java.lang.Integer.valueOf(modelParam), result);
                } catch (java.lang.Exception e) {
                    throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
                }
            }
            return result;
        }

        public void detach() {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED) {
                    throw new java.lang.IllegalStateException("Module has already been detached.");
                }
                if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.ALIVE && !this.mLoadedModels.isEmpty()) {
                    throw new java.lang.IllegalStateException("Cannot detach while models are loaded.");
                }
                try {
                    detachInternal();
                } catch (java.lang.Exception e) {
                    throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
                }
            }
        }

        public java.lang.String toString() {
            return java.util.Objects.toString(this.mDelegate);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void detachInternal() {
            try {
                this.mDelegate.detach();
                this.mState = com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DETACHED;
                this.mCallbackWrapper.detached();
                ((com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleState) com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this.mModules.get(java.lang.Integer.valueOf(this.mHandle))).sessions.remove(this);
            } catch (android.os.RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }

        void dump(java.io.PrintWriter pw) {
            if (this.mState == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.ALIVE) {
                pw.println("-------------------------------");
                pw.printf("Session %s, client: %s\n", toString(), com.android.server.soundtrigger_middleware.ObjectPrinter.print(this.mOriginatorIdentity, 16));
                pw.println("Loaded models (handle, active, description):");
                pw.println();
                pw.println("-------------------------------");
                for (java.util.Map.Entry<java.lang.Integer, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState> entry : this.mLoadedModels.entrySet()) {
                    pw.print(entry.getKey());
                    pw.print('\t');
                    pw.print(entry.getValue().activityState.name());
                    pw.print('\t');
                    pw.print(entry.getValue().description);
                    pw.println();
                }
                pw.println();
                return;
            }
            pw.printf("Session %s is dead", toString());
            pw.println();
        }

        class CallbackWrapper implements android.media.soundtrigger_middleware.ISoundTriggerCallback, android.os.IBinder.DeathRecipient {
            private final android.media.soundtrigger_middleware.ISoundTriggerCallback mCallback;

            CallbackWrapper(android.media.soundtrigger_middleware.ISoundTriggerCallback callback) {
                this.mCallback = callback;
                try {
                    this.mCallback.asBinder().linkToDeath(this, 0);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            }

            void detached() {
                this.mCallback.asBinder().unlinkToDeath(this, 0);
            }

            public void onRecognition(int modelHandle, android.media.soundtrigger_middleware.RecognitionEventSys event, int captureSession) {
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                    com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState) com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                    if (!event.recognitionEvent.recognitionStillActive) {
                        modelState.activityState = com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.LOADED;
                    }
                }
                try {
                    this.mCallback.onRecognition(modelHandle, event, captureSession);
                } catch (java.lang.Exception e) {
                    android.util.Slog.w(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.TAG, "Client callback exception.", e);
                }
            }

            public void onPhraseRecognition(int modelHandle, android.media.soundtrigger_middleware.PhraseRecognitionEventSys event, int captureSession) {
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                    com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState) com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                    if (modelState == null) {
                        return;
                    }
                    if (!event.phraseRecognitionEvent.common.recognitionStillActive) {
                        modelState.activityState = com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.LOADED;
                    }
                    try {
                        this.mCallback.onPhraseRecognition(modelHandle, event, captureSession);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.w(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.TAG, "Client callback exception.", e);
                    }
                }
            }

            public void onModelUnloaded(int modelHandle) {
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                    com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState modelState = (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState) com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
                    modelState.activityState = com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.PREEMPTED;
                }
                try {
                    this.mCallback.onModelUnloaded(modelHandle);
                } catch (java.lang.Exception e) {
                    android.util.Slog.w(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.TAG, "Client callback exception.", e);
                }
            }

            public void onResourcesAvailable() {
                try {
                    this.mCallback.onResourcesAvailable();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.TAG, "Client callback exception.", e);
                }
            }

            public void onModuleDied() {
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                    com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mState = com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModuleStatus.DEAD;
                }
                try {
                    this.mCallback.onModuleDied();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.TAG, "Client callback exception.", e);
                }
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                android.util.SparseArray<com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity> cachedMap = new android.util.SparseArray<>();
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                    for (java.util.Map.Entry<java.lang.Integer, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState> entry : com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mLoadedModels.entrySet()) {
                        cachedMap.put(entry.getKey().intValue(), entry.getValue().activityState);
                    }
                }
                for (int i = 0; i < cachedMap.size(); i++) {
                    try {
                        if (cachedMap.valueAt(i) == com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState.Activity.ACTIVE) {
                            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mDelegate.stopRecognition(cachedMap.keyAt(i));
                        }
                        com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mDelegate.unloadModel(cachedMap.keyAt(i));
                    } catch (java.lang.Exception e) {
                        throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e);
                    }
                }
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.this) {
                    for (java.util.Map.Entry<java.lang.Integer, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.ModelState> entry2 : com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mLoadedModels.entrySet()) {
                        if (cachedMap.get(entry2.getKey().intValue()) != entry2.getValue().activityState) {
                            android.util.Slog.e(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.TAG, "Unexpected state update in binderDied. Race occurred!");
                        }
                    }
                    if (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mLoadedModels.size() != cachedMap.size()) {
                        android.util.Slog.e(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.TAG, "Unexpected state update in binderDied. Race occurred!");
                    }
                    try {
                        com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.detachInternal();
                    } catch (java.lang.Exception e2) {
                        throw com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.handleException(e2);
                    }
                }
            }

            public android.os.IBinder asBinder() {
                return this.mCallback.asBinder();
            }

            public java.lang.String toString() {
                return java.util.Objects.toString(com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation.Session.this.mDelegate);
            }
        }
    }
}
