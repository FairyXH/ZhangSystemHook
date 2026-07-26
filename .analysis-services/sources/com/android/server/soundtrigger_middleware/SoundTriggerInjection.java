package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerInjection implements android.media.soundtrigger_middleware.ISoundTriggerInjection, android.os.IBinder.DeathRecipient {
    private static final java.lang.String TAG = "SoundTriggerInjection";
    private final java.lang.Object mClientLock = new java.lang.Object();
    private android.media.soundtrigger_middleware.ISoundTriggerInjection mClient = null;
    private android.media.soundtrigger_middleware.IInjectGlobalEvent mGlobalEventInjection = null;

    /* JADX WARN: Removed duplicated region for block: B:15:0x0031 A[Catch: RemoteException -> 0x0039, all -> 0x003f, TRY_LEAVE, TryCatch #1 {RemoteException -> 0x0039, blocks: (B:13:0x0024, B:15:0x0031), top: B:27:0x0024, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003d A[Catch: all -> 0x003f, DONT_GENERATE, TryCatch #2 {, blocks: (B:4:0x0003, B:7:0x000b, B:11:0x0019, B:12:0x0022, B:13:0x0024, B:15:0x0031, B:20:0x003d, B:19:0x003b, B:10:0x0012), top: B:29:0x0003, inners: #0, #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void registerClient(android.media.soundtrigger_middleware.ISoundTriggerInjection r6) {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mClientLock
            monitor-enter(r0)
            java.util.Objects.requireNonNull(r6)     // Catch: java.lang.Throwable -> L3f
            android.media.soundtrigger_middleware.ISoundTriggerInjection r1 = r5.mClient     // Catch: java.lang.Throwable -> L3f
            r2 = 0
            if (r1 == 0) goto L22
            android.media.soundtrigger_middleware.ISoundTriggerInjection r1 = r5.mClient     // Catch: android.os.RemoteException -> L11 java.lang.Throwable -> L3f
            r1.onPreempted()     // Catch: android.os.RemoteException -> L11 java.lang.Throwable -> L3f
            goto L19
        L11:
            r1 = move-exception
            java.lang.String r3 = "SoundTriggerInjection"
            java.lang.String r4 = "RemoteException when handling preemption"
            android.util.Slog.e(r3, r4, r1)     // Catch: java.lang.Throwable -> L3f
        L19:
            android.media.soundtrigger_middleware.ISoundTriggerInjection r1 = r5.mClient     // Catch: java.lang.Throwable -> L3f
            android.os.IBinder r1 = r1.asBinder()     // Catch: java.lang.Throwable -> L3f
            r1.unlinkToDeath(r5, r2)     // Catch: java.lang.Throwable -> L3f
        L22:
            r5.mClient = r6     // Catch: java.lang.Throwable -> L3f
            android.media.soundtrigger_middleware.ISoundTriggerInjection r1 = r5.mClient     // Catch: android.os.RemoteException -> L39 java.lang.Throwable -> L3f
            android.os.IBinder r1 = r1.asBinder()     // Catch: android.os.RemoteException -> L39 java.lang.Throwable -> L3f
            r1.linkToDeath(r5, r2)     // Catch: android.os.RemoteException -> L39 java.lang.Throwable -> L3f
            android.media.soundtrigger_middleware.IInjectGlobalEvent r1 = r5.mGlobalEventInjection     // Catch: android.os.RemoteException -> L39 java.lang.Throwable -> L3f
            if (r1 == 0) goto L38
            android.media.soundtrigger_middleware.ISoundTriggerInjection r1 = r5.mClient     // Catch: android.os.RemoteException -> L39 java.lang.Throwable -> L3f
            android.media.soundtrigger_middleware.IInjectGlobalEvent r2 = r5.mGlobalEventInjection     // Catch: android.os.RemoteException -> L39 java.lang.Throwable -> L3f
            r1.registerGlobalEventInjection(r2)     // Catch: android.os.RemoteException -> L39 java.lang.Throwable -> L3f
        L38:
            goto L3d
        L39:
            r1 = move-exception
            r2 = 0
            r5.mClient = r2     // Catch: java.lang.Throwable -> L3f
        L3d:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L3f
            return
        L3f:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L3f
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.soundtrigger_middleware.SoundTriggerInjection.registerClient(android.media.soundtrigger_middleware.ISoundTriggerInjection):void");
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        android.util.Slog.wtf(TAG, "Binder died without params");
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(android.os.IBinder who) {
        synchronized (this.mClientLock) {
            if (this.mClient != null && who == this.mClient.asBinder()) {
                this.mClient = null;
            }
        }
    }

    public void registerGlobalEventInjection(android.media.soundtrigger_middleware.IInjectGlobalEvent globalInjection) {
        synchronized (this.mClientLock) {
            this.mGlobalEventInjection = globalInjection;
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.registerGlobalEventInjection(this.mGlobalEventInjection);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onRestarted(android.media.soundtrigger_middleware.IInjectGlobalEvent globalSession) {
        synchronized (this.mClientLock) {
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.onRestarted(globalSession);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onFrameworkDetached(android.media.soundtrigger_middleware.IInjectGlobalEvent globalSession) {
        synchronized (this.mClientLock) {
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.onFrameworkDetached(globalSession);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onClientAttached(android.os.IBinder token, android.media.soundtrigger_middleware.IInjectGlobalEvent globalSession) {
        synchronized (this.mClientLock) {
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.onClientAttached(token, globalSession);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onClientDetached(android.os.IBinder token) {
        synchronized (this.mClientLock) {
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.onClientDetached(token);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onSoundModelLoaded(android.media.soundtrigger.SoundModel model, android.media.soundtrigger.Phrase[] phrases, android.media.soundtrigger_middleware.IInjectModelEvent modelInjection, android.media.soundtrigger_middleware.IInjectGlobalEvent globalSession) {
        synchronized (this.mClientLock) {
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.onSoundModelLoaded(model, phrases, modelInjection, globalSession);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onParamSet(int modelParam, int value, android.media.soundtrigger_middleware.IInjectModelEvent modelSession) {
        synchronized (this.mClientLock) {
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.onParamSet(modelParam, value, modelSession);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onRecognitionStarted(int audioSessionToken, android.media.soundtrigger.RecognitionConfig config, android.media.soundtrigger_middleware.IInjectRecognitionEvent recognitionInjection, android.media.soundtrigger_middleware.IInjectModelEvent modelSession) {
        synchronized (this.mClientLock) {
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.onRecognitionStarted(audioSessionToken, config, recognitionInjection, modelSession);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onRecognitionStopped(android.media.soundtrigger_middleware.IInjectRecognitionEvent recognitionSession) {
        synchronized (this.mClientLock) {
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.onRecognitionStopped(recognitionSession);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onSoundModelUnloaded(android.media.soundtrigger_middleware.IInjectModelEvent modelSession) {
        synchronized (this.mClientLock) {
            if (this.mClient == null) {
                return;
            }
            try {
                this.mClient.onSoundModelUnloaded(modelSession);
            } catch (android.os.RemoteException e) {
                this.mClient = null;
            }
        }
    }

    public void onPreempted() {
        android.util.Slog.wtf(TAG, "Unexpected preempted!");
    }

    public android.os.IBinder asBinder() {
        android.util.Slog.wtf(TAG, "Unexpected asBinder!");
        throw new java.lang.UnsupportedOperationException("Calling asBinder on a fake binder object");
    }
}
