package com.android.server.job.controllers.idle;

/* JADX INFO: loaded from: classes2.dex */
public final class DeviceIdlenessTracker extends android.content.BroadcastReceiver implements com.android.server.job.controllers.idle.IdlenessTracker {
    private static final boolean DEBUG;
    private static final java.lang.String IC_DIT_CONSTANT_PREFIX = "ic_dit_";
    private static final java.lang.String KEY_IDLE_WINDOW_SLOP_MS = "ic_dit_idle_window_slop_ms";
    static final java.lang.String KEY_INACTIVITY_IDLE_THRESHOLD_MS = "ic_dit_inactivity_idle_threshold_ms";
    static final java.lang.String KEY_INACTIVITY_STABLE_POWER_IDLE_THRESHOLD_MS = "ic_dit_inactivity_idle_stable_power_threshold_ms";
    private static final java.lang.String TAG = "JobScheduler.DeviceIdlenessTracker";
    private android.app.AlarmManager mAlarm;
    private boolean mDockIdle;
    private boolean mIdle;
    com.android.server.job.controllers.IIdleControllerExt mIdleControllerExt;
    private com.android.server.job.controllers.idle.IdlenessListener mIdleListener;
    private long mIdleWindowSlop;
    private long mInactivityIdleThreshold;
    private long mInactivityStablePowerIdleThreshold;
    private boolean mIsStablePower;
    private android.os.PowerManager mPowerManager;
    private boolean mProjectionActive;
    private long mIdlenessCheckScheduledElapsed = -1;
    private long mIdleStartElapsed = Long.MAX_VALUE;
    private final android.app.UiModeManager.OnProjectionStateChangedListener mOnProjectionStateChangedListener = new android.app.UiModeManager.OnProjectionStateChangedListener() { // from class: com.android.server.job.controllers.idle.DeviceIdlenessTracker$$ExternalSyntheticLambda0
        public final void onProjectionStateChanged(int i, java.util.Set set) {
            this.f$0.onProjectionStateChanged(i, set);
        }
    };
    private android.app.AlarmManager.OnAlarmListener mIdleAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.job.controllers.idle.DeviceIdlenessTracker$$ExternalSyntheticLambda1
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            this.f$0.lambda$new$0();
        }
    };
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
        this.mInactivityIdleThreshold = context.getResources().getInteger(android.R.integer.config_externalDisplayPeakWidth);
        this.mInactivityStablePowerIdleThreshold = context.getResources().getInteger(android.R.integer.config_extraFreeKbytesAbsolute);
        this.mIdleWindowSlop = context.getResources().getInteger(android.R.integer.config_externalDisplayPeakRefreshRate);
        this.mAlarm = (android.app.AlarmManager) context.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        this.mPowerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.DREAMING_STARTED");
        filter.addAction("android.intent.action.DREAMING_STOPPED");
        filter.addAction(com.android.server.am.ActivityManagerService.ACTION_TRIGGER_IDLE);
        this.mIdleControllerExt = ((com.android.server.job.controllers.IdleController) listener).mIdleControllerExt;
        filter.addAction(com.android.server.job.controllers.IIdleControllerExt.ACTION_FAST_IDLE_TRIGGER_INTENT);
        filter.addAction("android.intent.action.DOCK_IDLE");
        filter.addAction("android.intent.action.DOCK_ACTIVE");
        context.registerReceiver(this, filter, null, com.android.server.AppSchedulingModuleThread.getHandler());
        ((android.app.UiModeManager) context.getSystemService(android.app.UiModeManager.class)).addOnProjectionStateChangedListener(-1, com.android.server.AppSchedulingModuleThread.getExecutor(), this.mOnProjectionStateChangedListener);
        this.mIsStablePower = service.isBatteryCharging() && service.isBatteryNotLow();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0029  */
    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void processConstant(android.provider.DeviceConfig.Properties r8, java.lang.String r9) {
        /*
            r7 = this;
            int r0 = r9.hashCode()
            switch(r0) {
                case -365017934: goto L1e;
                case -165204881: goto L13;
                case 500388387: goto L8;
                default: goto L7;
            }
        L7:
            goto L29
        L8:
            java.lang.String r0 = "ic_dit_inactivity_idle_stable_power_threshold_ms"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto L7
            r0 = 1
            goto L2a
        L13:
            java.lang.String r0 = "ic_dit_inactivity_idle_threshold_ms"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto L7
            r0 = 0
            goto L2a
        L1e:
            java.lang.String r0 = "ic_dit_idle_window_slop_ms"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto L7
            r0 = 2
            goto L2a
        L29:
            r0 = -1
        L2a:
            r1 = 14400000(0xdbba00, double:7.1145453E-317)
            r3 = 60000(0xea60, double:2.9644E-319)
            switch(r0) {
                case 0: goto L59;
                case 1: goto L48;
                case 2: goto L34;
                default: goto L33;
            }
        L33:
            goto L6a
        L34:
            long r0 = r7.mIdleWindowSlop
            long r0 = r8.getLong(r9, r0)
            r5 = 900000(0xdbba0, double:4.44659E-318)
            long r0 = java.lang.Math.min(r5, r0)
            long r0 = java.lang.Math.max(r3, r0)
            r7.mIdleWindowSlop = r0
            goto L6a
        L48:
            long r5 = r7.mInactivityStablePowerIdleThreshold
            long r5 = r8.getLong(r9, r5)
            long r0 = java.lang.Math.min(r1, r5)
            long r0 = java.lang.Math.max(r3, r0)
            r7.mInactivityStablePowerIdleThreshold = r0
            goto L6a
        L59:
            long r5 = r7.mInactivityIdleThreshold
            long r5 = r8.getLong(r9, r5)
            long r0 = java.lang.Math.min(r1, r5)
            long r0 = java.lang.Math.max(r3, r0)
            r7.mInactivityIdleThreshold = r0
        L6a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.idle.DeviceIdlenessTracker.processConstant(android.provider.DeviceConfig$Properties, java.lang.String):void");
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void onBatteryStateChanged(boolean isCharging, boolean isBatteryNotLow) {
        boolean isStablePower = isCharging && isBatteryNotLow;
        if (this.mIsStablePower != isStablePower) {
            this.mIsStablePower = isStablePower;
            maybeScheduleIdlenessCheck("stable power changed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProjectionStateChanged(int activeProjectionTypes, java.util.Set<java.lang.String> projectingPackages) {
        boolean projectionActive = activeProjectionTypes != 0;
        if (this.mProjectionActive == projectionActive) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.v(TAG, "Projection state changed: " + projectionActive);
        }
        this.mProjectionActive = projectionActive;
        if (this.mProjectionActive) {
            exitIdle();
            this.mIdleControllerExt.updateFastIdleflag();
        } else {
            maybeScheduleIdlenessCheck("Projection ended");
        }
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void dump(java.io.PrintWriter pw) {
        pw.print("  mIdle: ");
        pw.println(this.mIdle);
        pw.print("  mScreenOn: ");
        pw.println(this.mScreenOn);
        pw.print("  mIsStablePower: ");
        pw.println(this.mIsStablePower);
        pw.print("  mDockIdle: ");
        pw.println(this.mDockIdle);
        pw.print("  mProjectionActive: ");
        pw.println(this.mProjectionActive);
        pw.print("  mIdlenessCheckScheduledElapsed: ");
        pw.println(this.mIdlenessCheckScheduledElapsed);
        pw.print("  mIdleStartElapsed: ");
        pw.println(this.mIdleStartElapsed);
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        long diToken = proto.start(1146756268033L);
        proto.write(1133871366145L, this.mIdle);
        proto.write(1133871366146L, this.mScreenOn);
        proto.write(1133871366147L, this.mDockIdle);
        proto.write(1133871366149L, this.mProjectionActive);
        proto.end(diToken);
        proto.end(token);
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void dumpConstants(android.util.IndentingPrintWriter pw) {
        pw.println("DeviceIdlenessTracker:");
        pw.increaseIndent();
        pw.print(KEY_INACTIVITY_IDLE_THRESHOLD_MS, java.lang.Long.valueOf(this.mInactivityIdleThreshold)).println();
        pw.print(KEY_INACTIVITY_STABLE_POWER_IDLE_THRESHOLD_MS, java.lang.Long.valueOf(this.mInactivityStablePowerIdleThreshold)).println();
        pw.print(KEY_IDLE_WINDOW_SLOP_MS, java.lang.Long.valueOf(this.mIdleWindowSlop)).println();
        pw.decreaseIndent();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007a  */
    @Override // android.content.BroadcastReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onReceive(android.content.Context r7, android.content.Intent r8) {
        /*
            Method dump skipped, instruction units count: 256
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.idle.DeviceIdlenessTracker.onReceive(android.content.Context, android.content.Intent):void");
    }

    private void maybeScheduleIdlenessCheck(java.lang.String reason) {
        long inactivityThresholdMs;
        if (this.mIdle) {
            if (DEBUG) {
                android.util.Slog.w(TAG, "Already idle. Redundant reason=" + reason);
                return;
            }
            return;
        }
        if ((!this.mScreenOn || this.mDockIdle) && !this.mProjectionActive) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (!this.mIsStablePower) {
                inactivityThresholdMs = this.mInactivityIdleThreshold;
            } else {
                inactivityThresholdMs = this.mInactivityStablePowerIdleThreshold;
            }
            if (this.mIdlenessCheckScheduledElapsed < 0) {
                this.mIdlenessCheckScheduledElapsed = nowElapsed;
            } else if (this.mIdlenessCheckScheduledElapsed + inactivityThresholdMs <= nowElapsed) {
                if (DEBUG) {
                    android.util.Slog.v(TAG, "Previous idle check @ " + this.mIdlenessCheckScheduledElapsed + " allows device to be idle now");
                }
                lambda$new$0();
                return;
            }
            long when = this.mIdlenessCheckScheduledElapsed + inactivityThresholdMs;
            if (when == this.mIdleStartElapsed) {
                if (DEBUG) {
                    android.util.Slog.i(TAG, "No change to idle start time");
                }
            } else {
                this.mIdleStartElapsed = when;
                if (DEBUG) {
                    android.util.Slog.v(TAG, "Scheduling idle : " + reason + " now:" + nowElapsed + " checkElapsed=" + this.mIdlenessCheckScheduledElapsed + " when=" + this.mIdleStartElapsed);
                }
                this.mAlarm.setWindow(2, this.mIdleStartElapsed, this.mIdleWindowSlop, "JS idleness", com.android.server.AppSchedulingModuleThread.getExecutor(), this.mIdleAlarmListener);
            }
        }
    }

    private void exitIdle() {
        this.mAlarm.cancel(this.mIdleAlarmListener);
        this.mIdlenessCheckScheduledElapsed = -1L;
        this.mIdleStartElapsed = Long.MAX_VALUE;
        if (this.mIdle) {
            this.mIdle = false;
            this.mIdleListener.reportNewIdleState(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleIdleTrigger, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        if (!this.mIdle && ((!this.mScreenOn || this.mDockIdle) && !this.mProjectionActive)) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Idle trigger fired @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
            }
            this.mIdle = true;
            this.mIdleListener.reportNewIdleState(this.mIdle);
            return;
        }
        if (DEBUG) {
            android.util.Slog.v(TAG, "TRIGGER_IDLE received but not changing state; idle=" + this.mIdle + " screen=" + this.mScreenOn + " projection=" + this.mProjectionActive);
        }
    }
}
