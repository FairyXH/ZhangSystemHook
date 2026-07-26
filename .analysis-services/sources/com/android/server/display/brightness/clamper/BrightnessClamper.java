package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
abstract class BrightnessClamper<T> {
    protected final com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener mChangeListener;
    protected final android.os.Handler mHandler;
    protected float mBrightnessCap = 1.0f;
    protected boolean mIsActive = false;

    protected enum Type {
        THERMAL,
        POWER,
        WEAR_BEDTIME_MODE
    }

    abstract com.android.server.display.brightness.clamper.BrightnessClamper.Type getType();

    abstract void onDeviceConfigChanged();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void onDisplayChanged(T t);

    abstract void stop();

    BrightnessClamper(android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener changeListener) {
        this.mHandler = handler;
        this.mChangeListener = changeListener;
    }

    float getBrightnessCap() {
        return this.mBrightnessCap;
    }

    float getCustomAnimationRate() {
        return -1.0f;
    }

    boolean isActive() {
        return this.mIsActive;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(java.io.PrintWriter writer) {
        writer.println("BrightnessClamper:" + getType());
        writer.println(" mBrightnessCap: " + this.mBrightnessCap);
        writer.println(" mIsActive: " + this.mIsActive);
    }
}
