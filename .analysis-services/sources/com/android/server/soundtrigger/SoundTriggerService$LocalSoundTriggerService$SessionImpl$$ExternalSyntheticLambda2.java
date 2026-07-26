package com.android.server.soundtrigger;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda2 implements java.util.function.Consumer {
    public final /* synthetic */ com.android.server.soundtrigger.SoundTriggerHelper f$0;

    @Override // java.util.function.Consumer
    public final void accept(java.lang.Object obj) {
        this.f$0.onAppOpStateChanged(((java.lang.Boolean) obj).booleanValue());
    }
}
