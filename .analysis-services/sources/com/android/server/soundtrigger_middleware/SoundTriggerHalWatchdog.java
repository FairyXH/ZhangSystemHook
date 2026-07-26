package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerHalWatchdog implements com.android.server.soundtrigger_middleware.ISoundTriggerHal {
    private static final java.lang.String TAG = "SoundTriggerHalWatchdog";
    private static final long TIMEOUT_MS = 13000;
    private final com.android.server.soundtrigger_middleware.UptimeTimer mTimer = new com.android.server.soundtrigger_middleware.UptimeTimer(TAG);
    private final com.android.server.soundtrigger_middleware.ISoundTriggerHal mUnderlying;

    public SoundTriggerHalWatchdog(com.android.server.soundtrigger_middleware.ISoundTriggerHal underlying) {
        this.mUnderlying = (com.android.server.soundtrigger_middleware.ISoundTriggerHal) java.util.Objects.requireNonNull(underlying);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.Properties getProperties() {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            android.media.soundtrigger.Properties properties = this.mUnderlying.getProperties();
            ignore.close();
            return properties;
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void registerCallback(com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback callback) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            this.mUnderlying.registerCallback(callback);
            ignore.close();
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadSoundModel(android.media.soundtrigger.SoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            int iLoadSoundModel = this.mUnderlying.loadSoundModel(soundModel, callback);
            ignore.close();
            return iLoadSoundModel;
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadPhraseSoundModel(android.media.soundtrigger.PhraseSoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            int iLoadPhraseSoundModel = this.mUnderlying.loadPhraseSoundModel(soundModel, callback);
            ignore.close();
            return iLoadPhraseSoundModel;
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unloadSoundModel(int modelHandle) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            this.mUnderlying.unloadSoundModel(modelHandle);
            ignore.close();
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void stopRecognition(int modelHandle) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            this.mUnderlying.stopRecognition(modelHandle);
            ignore.close();
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void startRecognition(int modelHandle, int deviceHandle, int ioHandle, android.media.soundtrigger.RecognitionConfig config) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            this.mUnderlying.startRecognition(modelHandle, deviceHandle, ioHandle, config);
            ignore.close();
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void forceRecognitionEvent(int modelHandle) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            this.mUnderlying.forceRecognitionEvent(modelHandle);
            ignore.close();
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int getModelParameter(int modelHandle, int param) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            int modelParameter = this.mUnderlying.getModelParameter(modelHandle, param);
            ignore.close();
            return modelParameter;
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void setModelParameter(int modelHandle, int param, int value) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            this.mUnderlying.setModelParameter(modelHandle, param, value);
            ignore.close();
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.ModelParameterRange queryParameter(int modelHandle, int param) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog ignore = new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.Watchdog();
        try {
            android.media.soundtrigger.ModelParameterRange modelParameterRangeQueryParameter = this.mUnderlying.queryParameter(modelHandle, param);
            ignore.close();
            return modelParameterRangeQueryParameter;
        } catch (java.lang.Throwable th) {
            try {
                ignore.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
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

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void reboot() {
        this.mUnderlying.reboot();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void detach() {
        this.mUnderlying.detach();
        this.mTimer.quit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Watchdog implements java.lang.AutoCloseable {
        private final java.lang.Exception mException = new java.lang.Exception();
        private final com.android.server.soundtrigger_middleware.UptimeTimer.Task mTask;

        Watchdog() {
            this.mTask = com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.this.mTimer.createTask(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog$Watchdog$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0();
                }
            }, com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.TIMEOUT_MS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            android.util.Slog.e(com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.TAG, "HAL deadline expired. Rebooting.", this.mException);
            com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog.this.reboot();
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            this.mTask.cancel();
        }
    }
}
