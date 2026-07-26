package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerHalEnforcer implements com.android.server.soundtrigger_middleware.ISoundTriggerHal {
    private static final java.lang.String TAG = "SoundTriggerHalEnforcer";
    private final java.util.Map<java.lang.Integer, com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState> mModelStates = new java.util.HashMap();
    private final com.android.server.soundtrigger_middleware.ISoundTriggerHal mUnderlying;

    private enum ModelState {
        INACTIVE,
        ACTIVE,
        PENDING_STOP
    }

    public SoundTriggerHalEnforcer(com.android.server.soundtrigger_middleware.ISoundTriggerHal underlying) {
        this.mUnderlying = underlying;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.Properties getProperties() {
        try {
            return this.mUnderlying.getProperties();
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void registerCallback(com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback callback) {
        try {
            this.mUnderlying.registerCallback(callback);
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadSoundModel(android.media.soundtrigger.SoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        int handle;
        try {
            synchronized (this.mModelStates) {
                handle = this.mUnderlying.loadSoundModel(soundModel, new com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelCallbackEnforcer(callback));
                this.mModelStates.put(java.lang.Integer.valueOf(handle), com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState.INACTIVE);
            }
            return handle;
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadPhraseSoundModel(android.media.soundtrigger.PhraseSoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        int handle;
        try {
            synchronized (this.mModelStates) {
                handle = this.mUnderlying.loadPhraseSoundModel(soundModel, new com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelCallbackEnforcer(callback));
                this.mModelStates.put(java.lang.Integer.valueOf(handle), com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState.INACTIVE);
            }
            return handle;
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unloadSoundModel(int modelHandle) {
        try {
            this.mUnderlying.unloadSoundModel(modelHandle);
            synchronized (this.mModelStates) {
                this.mModelStates.remove(java.lang.Integer.valueOf(modelHandle));
            }
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void stopRecognition(int modelHandle) {
        try {
            synchronized (this.mModelStates) {
                this.mModelStates.replace(java.lang.Integer.valueOf(modelHandle), com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState.PENDING_STOP);
            }
            this.mUnderlying.stopRecognition(modelHandle);
            synchronized (this.mModelStates) {
                this.mModelStates.replace(java.lang.Integer.valueOf(modelHandle), com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState.INACTIVE);
            }
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void startRecognition(int modelHandle, int deviceHandle, int ioHandle, android.media.soundtrigger.RecognitionConfig config) {
        try {
            synchronized (this.mModelStates) {
                this.mUnderlying.startRecognition(modelHandle, deviceHandle, ioHandle, config);
                this.mModelStates.replace(java.lang.Integer.valueOf(modelHandle), com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState.ACTIVE);
            }
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void forceRecognitionEvent(int modelHandle) {
        try {
            this.mUnderlying.forceRecognitionEvent(modelHandle);
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int getModelParameter(int modelHandle, int param) {
        try {
            return this.mUnderlying.getModelParameter(modelHandle, param);
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void setModelParameter(int modelHandle, int param, int value) {
        try {
            this.mUnderlying.setModelParameter(modelHandle, param, value);
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.ModelParameterRange queryParameter(int modelHandle, int param) {
        try {
            return this.mUnderlying.queryParameter(modelHandle, param);
        } catch (java.lang.RuntimeException e) {
            throw handleException(e);
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void linkToDeath(android.os.IBinder.DeathRecipient recipient) {
        this.mUnderlying.linkToDeath(recipient);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unlinkToDeath(android.os.IBinder.DeathRecipient recipient) {
        this.mUnderlying.unlinkToDeath(recipient);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public java.lang.String interfaceDescriptor() {
        return this.mUnderlying.interfaceDescriptor();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void flushCallbacks() {
        this.mUnderlying.flushCallbacks();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientAttached(android.os.IBinder binder) {
        this.mUnderlying.clientAttached(binder);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientDetached(android.os.IBinder binder) {
        this.mUnderlying.clientDetached(binder);
    }

    private java.lang.RuntimeException handleException(java.lang.RuntimeException e) {
        if (e instanceof com.android.server.soundtrigger_middleware.RecoverableException) {
            throw e;
        }
        if (e.getCause() instanceof android.os.DeadObjectException) {
            android.util.Slog.e(TAG, "HAL died");
            throw new com.android.server.soundtrigger_middleware.RecoverableException(4);
        }
        android.util.Slog.e(TAG, "Exception caught from HAL, rebooting HAL");
        reboot();
        throw e;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void reboot() {
        if (!android.os.Build.isMtkPlatform()) {
            this.mUnderlying.reboot();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void detach() {
        this.mUnderlying.detach();
    }

    private class ModelCallbackEnforcer implements com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback {
        private final com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback mUnderlying;

        private ModelCallbackEnforcer(com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback underlying) {
            this.mUnderlying = underlying;
        }

        @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
        public void recognitionCallback(int model, android.media.soundtrigger_middleware.RecognitionEventSys event) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates) {
                com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState state = (com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState) com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates.get(java.lang.Integer.valueOf(model));
                if (state == null) {
                    android.util.Slog.wtfStack(com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.TAG, "Unexpected recognition event for model: " + model);
                    com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.reboot();
                } else if (event.recognitionEvent.recognitionStillActive && event.recognitionEvent.status != 0 && event.recognitionEvent.status != 3) {
                    android.util.Slog.wtfStack(com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.TAG, "recognitionStillActive is only allowed when the recognition status is SUCCESS");
                    com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.reboot();
                } else {
                    if (!event.recognitionEvent.recognitionStillActive) {
                        com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates.replace(java.lang.Integer.valueOf(model), com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState.INACTIVE);
                    }
                    this.mUnderlying.recognitionCallback(model, event);
                }
            }
        }

        @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
        public void phraseRecognitionCallback(int model, android.media.soundtrigger_middleware.PhraseRecognitionEventSys event) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates) {
                com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState state = (com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState) com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates.get(java.lang.Integer.valueOf(model));
                if (state == null) {
                    android.util.Slog.wtfStack(com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.TAG, "Unexpected recognition event for model: " + model);
                    com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.reboot();
                    return;
                }
                if (event.phraseRecognitionEvent.common.recognitionStillActive && event.phraseRecognitionEvent.common.status != 0 && event.phraseRecognitionEvent.common.status != 3) {
                    android.util.Slog.wtfStack(com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.TAG, "recognitionStillActive is only allowed when the recognition status is SUCCESS");
                    com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.reboot();
                    return;
                }
                if (android.os.Build.isMtkPlatform()) {
                    int eventCommonDataLength = 0;
                    if (event.phraseRecognitionEvent.common.data != null) {
                        eventCommonDataLength = event.phraseRecognitionEvent.common.data.length;
                    }
                    if (!event.phraseRecognitionEvent.common.recognitionStillActive && (eventCommonDataLength == 0 || event.phraseRecognitionEvent.common.data[0] != 1)) {
                        android.util.Log.i(com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.TAG, "[phraseRecognitionCallback] ModelStates.replace ");
                        com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates.replace(java.lang.Integer.valueOf(model), com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState.INACTIVE);
                    }
                } else if (!event.phraseRecognitionEvent.common.recognitionStillActive) {
                    com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates.replace(java.lang.Integer.valueOf(model), com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState.INACTIVE);
                }
                this.mUnderlying.phraseRecognitionCallback(model, event);
            }
        }

        @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
        public void modelUnloaded(int modelHandle) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates) {
                com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState state = (com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState) com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates.get(java.lang.Integer.valueOf(modelHandle));
                if (state == null) {
                    android.util.Slog.wtfStack(com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.TAG, "Unexpected unload event for model: " + modelHandle);
                    com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.reboot();
                } else if (state == com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.ModelState.ACTIVE) {
                    android.util.Slog.wtfStack(com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.TAG, "Trying to unload an active model: " + modelHandle);
                    com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.reboot();
                } else {
                    com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer.this.mModelStates.remove(java.lang.Integer.valueOf(modelHandle));
                    this.mUnderlying.modelUnloaded(modelHandle);
                }
            }
        }
    }
}
