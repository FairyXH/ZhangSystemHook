package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class CachedDeviceStateService extends com.android.server.SystemService {
    private static final java.lang.String TAG = "CachedDeviceStateService";
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final com.android.internal.os.CachedDeviceState mDeviceState;

    public CachedDeviceStateService(android.content.Context context) {
        super(context);
        this.mDeviceState = new com.android.internal.os.CachedDeviceState();
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.CachedDeviceStateService.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:14:0x002c  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r5, android.content.Intent r6) {
                /*
                    r4 = this;
                    java.lang.String r0 = r6.getAction()
                    int r1 = r0.hashCode()
                    r2 = 1
                    r3 = 0
                    switch(r1) {
                        case -2128145023: goto L22;
                        case -1538406691: goto L18;
                        case -1454123155: goto Le;
                        default: goto Ld;
                    }
                Ld:
                    goto L2c
                Le:
                    java.lang.String r1 = "android.intent.action.SCREEN_ON"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = r2
                    goto L2d
                L18:
                    java.lang.String r1 = "android.intent.action.BATTERY_CHANGED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = r3
                    goto L2d
                L22:
                    java.lang.String r1 = "android.intent.action.SCREEN_OFF"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = 2
                    goto L2d
                L2c:
                    r0 = -1
                L2d:
                    switch(r0) {
                        case 0: goto L45;
                        case 1: goto L3b;
                        case 2: goto L31;
                        default: goto L30;
                    }
                L30:
                    goto L5a
                L31:
                    com.android.server.CachedDeviceStateService r0 = com.android.server.CachedDeviceStateService.this
                    com.android.internal.os.CachedDeviceState r0 = com.android.server.CachedDeviceStateService.m113$$Nest$fgetmDeviceState(r0)
                    r0.setScreenInteractive(r3)
                    goto L5a
                L3b:
                    com.android.server.CachedDeviceStateService r0 = com.android.server.CachedDeviceStateService.this
                    com.android.internal.os.CachedDeviceState r0 = com.android.server.CachedDeviceStateService.m113$$Nest$fgetmDeviceState(r0)
                    r0.setScreenInteractive(r2)
                    goto L5a
                L45:
                    com.android.server.CachedDeviceStateService r0 = com.android.server.CachedDeviceStateService.this
                    com.android.internal.os.CachedDeviceState r0 = com.android.server.CachedDeviceStateService.m113$$Nest$fgetmDeviceState(r0)
                    java.lang.String r1 = "plugged"
                    int r1 = r6.getIntExtra(r1, r3)
                    if (r1 == 0) goto L55
                    goto L56
                L55:
                    r2 = r3
                L56:
                    r0.setCharging(r2)
                L5a:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.CachedDeviceStateService.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishLocalService(com.android.internal.os.CachedDeviceState.Readonly.class, this.mDeviceState.getReadonlyClient());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (500 == phase) {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.BATTERY_CHANGED");
            filter.addAction("android.intent.action.SCREEN_ON");
            filter.addAction("android.intent.action.SCREEN_OFF");
            filter.setPriority(1000);
            filter.addCategory("oplusBrEx@android.intent.action.BATTERY_CHANGED@BATTERYSTATE=CHARGING_CHANGED");
            getContext().registerReceiver(this.mBroadcastReceiver, filter);
            this.mDeviceState.setCharging(queryIsCharging());
            this.mDeviceState.setScreenInteractive(queryScreenInteractive(getContext()));
        }
    }

    private boolean queryIsCharging() {
        android.os.BatteryManagerInternal batteryManager = (android.os.BatteryManagerInternal) com.android.server.LocalServices.getService(android.os.BatteryManagerInternal.class);
        if (batteryManager != null) {
            return batteryManager.getPlugType() != 0;
        }
        android.util.Slog.wtf(TAG, "BatteryManager null while starting CachedDeviceStateService");
        return true;
    }

    private boolean queryScreenInteractive(android.content.Context context) {
        android.os.PowerManager powerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        if (powerManager == null) {
            android.util.Slog.wtf(TAG, "PowerManager null while starting CachedDeviceStateService");
            return false;
        }
        return powerManager.isInteractive();
    }
}
