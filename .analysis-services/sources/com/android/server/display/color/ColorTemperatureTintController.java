package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
abstract class ColorTemperatureTintController extends com.android.server.display.color.TintController {
    abstract float[] computeMatrixForCct(int i);

    abstract int getAppliedCct();

    abstract int getDisabledCct();

    abstract com.android.server.display.color.CctEvaluator getEvaluator();

    abstract int getTargetCct();

    abstract void setAppliedCct(int i);

    abstract void setTargetCct(int i);

    ColorTemperatureTintController() {
    }
}
