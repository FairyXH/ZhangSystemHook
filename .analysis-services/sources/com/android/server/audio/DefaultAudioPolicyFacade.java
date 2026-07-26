package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class DefaultAudioPolicyFacade implements com.android.server.audio.AudioPolicyFacade {
    private static final java.lang.String AUDIO_POLICY_SERVICE_NAME = "media.audio_policy";
    private final com.android.server.audio.ServiceHolder<android.media.IAudioPolicyService> mServiceHolder;

    public DefaultAudioPolicyFacade(java.util.concurrent.Executor e) {
        this.mServiceHolder = new com.android.server.audio.ServiceHolder<>(AUDIO_POLICY_SERVICE_NAME, new java.util.function.Function() { // from class: com.android.server.audio.DefaultAudioPolicyFacade$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.media.IAudioPolicyService.Stub.asInterface((android.os.IBinder) obj);
            }
        }, e);
        this.mServiceHolder.registerOnStartTask(new java.util.function.Consumer() { // from class: com.android.server.audio.DefaultAudioPolicyFacade$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                android.os.Binder.allowBlocking(((android.media.IAudioPolicyService) obj).asBinder());
            }
        });
    }

    @Override // com.android.server.audio.AudioPolicyFacade
    public boolean isHotwordStreamSupported(boolean lookbackAudio) {
        android.media.IAudioPolicyService ap = this.mServiceHolder.waitForService();
        try {
            return ap.isHotwordStreamSupported(lookbackAudio);
        } catch (android.os.RemoteException e) {
            this.mServiceHolder.attemptClear(ap.asBinder());
            throw new java.lang.IllegalStateException();
        }
    }

    @Override // com.android.server.audio.AudioPolicyFacade
    public com.android.media.permission.INativePermissionController getPermissionController() {
        android.media.IAudioPolicyService ap = this.mServiceHolder.checkService();
        if (ap == null) {
            return null;
        }
        try {
            com.android.media.permission.INativePermissionController res = (com.android.media.permission.INativePermissionController) java.util.Objects.requireNonNull(ap.getPermissionController());
            android.os.Binder.allowBlocking(res.asBinder());
            return res;
        } catch (android.os.RemoteException e) {
            this.mServiceHolder.attemptClear(ap.asBinder());
            return null;
        }
    }

    @Override // com.android.server.audio.AudioPolicyFacade
    public void registerOnStartTask(final java.lang.Runnable task) {
        this.mServiceHolder.registerOnStartTask(new java.util.function.Consumer() { // from class: com.android.server.audio.DefaultAudioPolicyFacade$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                task.run();
            }
        });
    }
}
