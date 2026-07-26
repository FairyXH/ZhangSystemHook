package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessWearBedtimeModeClamper extends com.android.server.display.brightness.clamper.BrightnessClamper<com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.WearBedtimeModeData> {
    public static final int BEDTIME_MODE_OFF = 0;
    public static final int BEDTIME_MODE_ON = 1;
    private final android.content.Context mContext;
    private final android.database.ContentObserver mSettingsObserver;

    interface WearBedtimeModeData {
        float getBrightnessWearBedtimeModeCap();
    }

    BrightnessWearBedtimeModeClamper(android.os.Handler handler, android.content.Context context, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener listener, com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.WearBedtimeModeData data) {
        this(new com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.Injector(), handler, context, listener, data);
    }

    BrightnessWearBedtimeModeClamper(com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.Injector injector, android.os.Handler handler, android.content.Context context, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener listener, com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.WearBedtimeModeData data) {
        super(handler, listener);
        this.mContext = context;
        this.mBrightnessCap = data.getBrightnessWearBedtimeModeCap();
        this.mSettingsObserver = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                int bedtimeModeSetting = android.provider.Settings.Global.getInt(com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.this.mContext.getContentResolver(), "bedtime_mode", 0);
                com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.this.mIsActive = bedtimeModeSetting == 1;
                com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.this.mChangeListener.onChanged();
            }
        };
        injector.registerBedtimeModeObserver(context.getContentResolver(), this.mSettingsObserver);
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    com.android.server.display.brightness.clamper.BrightnessClamper.Type getType() {
        return com.android.server.display.brightness.clamper.BrightnessClamper.Type.WEAR_BEDTIME_MODE;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    void onDeviceConfigChanged() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    public void onDisplayChanged(final com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.WearBedtimeModeData displayData) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onDisplayChanged$0(displayData);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayChanged$0(com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.WearBedtimeModeData displayData) {
        this.mBrightnessCap = displayData.getBrightnessWearBedtimeModeCap();
        this.mChangeListener.onChanged();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    void stop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
    }

    static class Injector {
        Injector() {
        }

        void registerBedtimeModeObserver(android.content.ContentResolver cr, android.database.ContentObserver observer) {
            cr.registerContentObserver(android.provider.Settings.Global.getUriFor("bedtime_mode"), false, observer, -1);
        }
    }
}
