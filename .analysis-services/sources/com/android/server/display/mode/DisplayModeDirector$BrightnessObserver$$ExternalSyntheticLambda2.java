package com.android.server.display.mode;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda2 implements java.util.function.Function {
    @Override // java.util.function.Function
    public final java.lang.Object apply(java.lang.Object obj) {
        return com.android.server.display.utils.DeviceConfigParsingUtils.displayBrightnessThresholdsIntToFloat((int[]) obj);
    }
}
