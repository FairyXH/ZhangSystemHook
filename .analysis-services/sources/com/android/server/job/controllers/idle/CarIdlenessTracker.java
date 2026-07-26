package com.android.server.job.controllers.idle;

/* JADX INFO: loaded from: classes2.dex */
public final class CarIdlenessTracker extends android.content.BroadcastReceiver implements com.android.server.job.controllers.idle.IdlenessTracker {
    public static final java.lang.String ACTION_FORCE_IDLE = "com.android.server.jobscheduler.FORCE_IDLE";
    public static final java.lang.String ACTION_GARAGE_MODE_OFF = "com.android.server.jobscheduler.GARAGE_MODE_OFF";
    public static final java.lang.String ACTION_GARAGE_MODE_ON = "com.android.server.jobscheduler.GARAGE_MODE_ON";
    public static final java.lang.String ACTION_UNFORCE_IDLE = "com.android.server.jobscheduler.UNFORCE_IDLE";
    private static final boolean DEBUG;
    private static final java.lang.String TAG = "JobScheduler.CarIdlenessTracker";
    private com.android.server.job.controllers.idle.IdlenessListener mIdleListener;
    private boolean mIdle = false;
    private boolean mGarageModeOn = false;
    private boolean mForced = false;
    private boolean mScreenOn = true;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public boolean isIdle() {
        return this.mIdle;
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void startTracking(android.content.Context context, com.android.server.job.JobSchedulerService service, com.android.server.job.controllers.idle.IdlenessListener listener) {
        this.mIdleListener = listener;
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction(ACTION_GARAGE_MODE_ON);
        filter.addAction(ACTION_GARAGE_MODE_OFF);
        filter.addAction(ACTION_FORCE_IDLE);
        filter.addAction(ACTION_UNFORCE_IDLE);
        filter.addAction(com.android.server.am.ActivityManagerService.ACTION_TRIGGER_IDLE);
        context.registerReceiver(this, filter, null, com.android.server.AppSchedulingModuleThread.getHandler());
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void processConstant(android.provider.DeviceConfig.Properties properties, java.lang.String key) {
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void onBatteryStateChanged(boolean isCharging, boolean isBatteryNotLow) {
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void dump(java.io.PrintWriter pw) {
        pw.print("  mIdle: ");
        pw.println(this.mIdle);
        pw.print("  mGarageModeOn: ");
        pw.println(this.mGarageModeOn);
        pw.print("  mForced: ");
        pw.println(this.mForced);
        pw.print("  mScreenOn: ");
        pw.println(this.mScreenOn);
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        long ciToken = proto.start(1146756268034L);
        proto.write(1133871366145L, this.mIdle);
        proto.write(1133871366146L, this.mGarageModeOn);
        proto.end(ciToken);
        proto.end(token);
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void dumpConstants(android.util.IndentingPrintWriter pw) {
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        java.lang.String action = intent.getAction();
        logIfDebug("Received action: " + action);
        if (action.equals(ACTION_FORCE_IDLE)) {
            logIfDebug("Forcing idle...");
            setForceIdleState(true);
            return;
        }
        if (action.equals(ACTION_UNFORCE_IDLE)) {
            logIfDebug("Unforcing idle...");
            setForceIdleState(false);
            return;
        }
        if (action.equals("android.intent.action.SCREEN_ON")) {
            logIfDebug("Screen is on...");
            handleScreenOn();
            return;
        }
        if (action.equals("android.intent.action.SCREEN_OFF")) {
            logIfDebug("Screen is off...");
            this.mScreenOn = false;
            return;
        }
        if (action.equals(ACTION_GARAGE_MODE_ON)) {
            logIfDebug("GarageMode is on...");
            this.mGarageModeOn = true;
            updateIdlenessState();
        } else if (action.equals(ACTION_GARAGE_MODE_OFF)) {
            logIfDebug("GarageMode is off...");
            this.mGarageModeOn = false;
            updateIdlenessState();
        } else if (action.equals(com.android.server.am.ActivityManagerService.ACTION_TRIGGER_IDLE)) {
            if (!this.mGarageModeOn) {
                logIfDebug("Idle trigger fired...");
                triggerIdleness();
            } else {
                logIfDebug("TRIGGER_IDLE received but not changing state; mIdle=" + this.mIdle + " mGarageModeOn=" + this.mGarageModeOn);
            }
        }
    }

    private void setForceIdleState(boolean forced) {
        this.mForced = forced;
        updateIdlenessState();
    }

    private void updateIdlenessState() {
        boolean newState = this.mForced || this.mGarageModeOn;
        if (this.mIdle != newState) {
            logIfDebug("Device idleness changed. New idle=" + newState);
            this.mIdle = newState;
            this.mIdleListener.reportNewIdleState(this.mIdle);
            return;
        }
        logIfDebug("Device idleness is the same. Current idle=" + newState);
    }

    private void triggerIdleness() {
        if (this.mIdle) {
            logIfDebug("Device is already idle");
        } else {
            if (!this.mScreenOn) {
                logIfDebug("Device is going idle");
                this.mIdle = true;
                this.mIdleListener.reportNewIdleState(this.mIdle);
                return;
            }
            logIfDebug("TRIGGER_IDLE received but not changing state: mIdle = " + this.mIdle + ", mScreenOn = " + this.mScreenOn);
        }
    }

    private void handleScreenOn() {
        this.mScreenOn = true;
        if (this.mForced || this.mGarageModeOn) {
            logIfDebug("Screen is on, but device cannot exit idle");
        } else {
            if (this.mIdle) {
                logIfDebug("Device is exiting idle");
                this.mIdle = false;
                this.mIdleListener.reportNewIdleState(this.mIdle);
                return;
            }
            logIfDebug("Device is already non-idle");
        }
    }

    private static void logIfDebug(java.lang.String msg) {
        if (DEBUG) {
            android.util.Slog.v(TAG, msg);
        }
    }
}
