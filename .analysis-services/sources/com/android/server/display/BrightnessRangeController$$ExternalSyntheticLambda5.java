package com.android.server.display;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class BrightnessRangeController$$ExternalSyntheticLambda5 implements com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener {
    public final /* synthetic */ java.lang.Runnable f$0;

    public /* synthetic */ BrightnessRangeController$$ExternalSyntheticLambda5(java.lang.Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener
    public final void onChanged() {
        this.f$0.run();
    }
}
