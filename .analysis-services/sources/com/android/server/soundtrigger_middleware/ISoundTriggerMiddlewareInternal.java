package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public interface ISoundTriggerMiddlewareInternal {
    android.media.soundtrigger_middleware.ISoundTriggerModule attach(int i, android.media.soundtrigger_middleware.ISoundTriggerCallback iSoundTriggerCallback, boolean z);

    android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] listModules();
}
