package com.android.server;

/* JADX INFO: loaded from: classes.dex */
final class DockObserver extends com.android.server.SystemService {
    private static final int MSG_DOCK_STATE_CHANGED = 0;
    private static final java.lang.String TAG = "DockObserver";
    private int mActualDockState;
    private final boolean mAllowTheaterModeWakeFromDock;
    private com.android.server.DockObserver.DeviceProvisionedObserver mDeviceProvisionedObserver;
    private final java.util.List<com.android.server.DockObserver.ExtconStateConfig> mExtconStateConfigs;
    private final com.android.server.ExtconUEventObserver mExtconUEventObserver;
    private final android.os.Handler mHandler;
    private final boolean mKeepDreamingWhenUnplugging;
    private final java.lang.Object mLock;
    private final android.os.PowerManager mPowerManager;
    private int mPreviousDockState;
    private int mReportedDockState;
    private boolean mSystemReady;
    private boolean mUpdatesStopped;
    private final android.os.PowerManager.WakeLock mWakeLock;

    static final class ExtconStateProvider {
        private final java.util.Map<java.lang.String, java.lang.String> mState;

        ExtconStateProvider(java.util.Map<java.lang.String, java.lang.String> state) {
            this.mState = state;
        }

        java.lang.String getValue(java.lang.String key) {
            return this.mState.get(key);
        }

        static com.android.server.DockObserver.ExtconStateProvider fromString(java.lang.String stateString) {
            java.util.Map<java.lang.String, java.lang.String> states = new java.util.HashMap<>();
            java.lang.String[] lines = stateString.split("\n");
            for (java.lang.String line : lines) {
                java.lang.String[] fields = line.split("=");
                if (fields.length == 2) {
                    states.put(fields[0], fields[1]);
                } else {
                    android.util.Slog.e(com.android.server.DockObserver.TAG, "Invalid line: " + line);
                }
            }
            return new com.android.server.DockObserver.ExtconStateProvider(states);
        }

        static com.android.server.DockObserver.ExtconStateProvider fromFile(java.lang.String stateFilePath) {
            char[] buffer = new char[1024];
            try {
                java.io.FileReader file = new java.io.FileReader(stateFilePath);
                try {
                    int len = file.read(buffer, 0, 1024);
                    java.lang.String stateString = new java.lang.String(buffer, 0, len).trim();
                    com.android.server.DockObserver.ExtconStateProvider extconStateProviderFromString = fromString(stateString);
                    file.close();
                    return extconStateProviderFromString;
                } finally {
                }
            } catch (java.io.FileNotFoundException e) {
                android.util.Slog.w(com.android.server.DockObserver.TAG, "No state file found at: " + stateFilePath);
                return new com.android.server.DockObserver.ExtconStateProvider(new java.util.HashMap());
            } catch (java.lang.Exception e2) {
                android.util.Slog.e(com.android.server.DockObserver.TAG, "", e2);
                return new com.android.server.DockObserver.ExtconStateProvider(new java.util.HashMap());
            }
        }
    }

    private static final class ExtconStateConfig {
        public final int extraStateValue;
        public final java.util.List<android.util.Pair<java.lang.String, java.lang.String>> keyValuePairs = new java.util.ArrayList();

        ExtconStateConfig(int extraStateValue) {
            this.extraStateValue = extraStateValue;
        }
    }

    private static java.util.List<com.android.server.DockObserver.ExtconStateConfig> loadExtconStateConfigs(android.content.Context context) {
        java.lang.String[] rows = context.getResources().getStringArray(android.R.array.config_displayWhiteBalanceStrongDisplayColorTemperatures);
        try {
            java.util.ArrayList<com.android.server.DockObserver.ExtconStateConfig> configs = new java.util.ArrayList<>();
            for (java.lang.String row : rows) {
                java.lang.String[] rowFields = row.split(",");
                com.android.server.DockObserver.ExtconStateConfig config = new com.android.server.DockObserver.ExtconStateConfig(java.lang.Integer.parseInt(rowFields[0]));
                for (int i = 1; i < rowFields.length; i++) {
                    java.lang.String[] keyValueFields = rowFields[i].split("=");
                    if (keyValueFields.length != 2) {
                        throw new java.lang.IllegalArgumentException("Invalid key-value: " + rowFields[i]);
                    }
                    config.keyValuePairs.add(android.util.Pair.create(keyValueFields[0], keyValueFields[1]));
                }
                configs.add(config);
            }
            return configs;
        } catch (java.lang.ArrayIndexOutOfBoundsException | java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Could not parse extcon state config", e);
            return new java.util.ArrayList();
        }
    }

    public DockObserver(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mActualDockState = 0;
        this.mReportedDockState = 0;
        this.mPreviousDockState = 0;
        this.mHandler = new android.os.Handler(true) { // from class: com.android.server.DockObserver.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0:
                        com.android.server.DockObserver.this.handleDockStateChange();
                        com.android.server.DockObserver.this.mWakeLock.release();
                        break;
                }
            }
        };
        this.mExtconUEventObserver = new com.android.server.ExtconUEventObserver() { // from class: com.android.server.DockObserver.2
            @Override // com.android.server.ExtconUEventObserver
            public void onUEvent(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo, android.os.UEventObserver.UEvent event) {
                synchronized (com.android.server.DockObserver.this.mLock) {
                    java.lang.String stateString = event.get("STATE");
                    if (stateString != null) {
                        com.android.server.DockObserver.this.setDockStateFromProviderLocked(com.android.server.DockObserver.ExtconStateProvider.fromString(stateString));
                    } else {
                        android.util.Slog.e(com.android.server.DockObserver.TAG, "Extcon event missing STATE: " + event);
                    }
                }
            }
        };
        this.mPowerManager = (android.os.PowerManager) context.getSystemService("power");
        this.mWakeLock = this.mPowerManager.newWakeLock(1, TAG);
        this.mAllowTheaterModeWakeFromDock = context.getResources().getBoolean(android.R.bool.config_allowSeamlessRotationDespiteNavBarMoving);
        this.mKeepDreamingWhenUnplugging = context.getResources().getBoolean(android.R.bool.config_hideDisplayCutoutWithDisplayArea);
        this.mDeviceProvisionedObserver = new com.android.server.DockObserver.DeviceProvisionedObserver(this.mHandler);
        this.mExtconStateConfigs = loadExtconStateConfigs(context);
        java.util.List<com.android.server.ExtconUEventObserver.ExtconInfo> infos = com.android.server.ExtconUEventObserver.ExtconInfo.getExtconInfoForTypes(new java.lang.String[]{com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_DOCK});
        if (!infos.isEmpty()) {
            com.android.server.ExtconUEventObserver.ExtconInfo info = infos.get(0);
            android.util.Slog.i(TAG, "Found extcon info devPath: " + info.getDevicePath() + ", statePath: " + info.getStatePath());
            setDockStateFromProviderLocked(com.android.server.DockObserver.ExtconStateProvider.fromFile(info.getStatePath()));
            this.mPreviousDockState = this.mActualDockState;
            this.mExtconUEventObserver.startObserving(info);
            return;
        }
        android.util.Slog.i(TAG, "No extcon dock device found in this kernel.");
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService(TAG, new com.android.server.DockObserver.BinderService());
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DOCK_STATE_CHANGED, this.mReportedDockState);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 550) {
            synchronized (this.mLock) {
                this.mSystemReady = true;
                this.mDeviceProvisionedObserver.onSystemReady();
                updateIfDockedLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateIfDockedLocked() {
        if (this.mReportedDockState != 0) {
            updateLocked();
        }
    }

    private void setActualDockStateLocked(int newState) {
        this.mActualDockState = newState;
        if (!this.mUpdatesStopped) {
            setDockStateLocked(newState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDockStateLocked(int newState) {
        if (newState != this.mReportedDockState) {
            this.mReportedDockState = newState;
            if (this.mSystemReady) {
                if (allowWakeFromDock()) {
                    this.mPowerManager.wakeUp(android.os.SystemClock.uptimeMillis(), "android.server:DOCK");
                }
                updateLocked();
            }
        }
    }

    private boolean allowWakeFromDock() {
        if (this.mKeepDreamingWhenUnplugging) {
            return false;
        }
        return this.mAllowTheaterModeWakeFromDock || android.provider.Settings.Global.getInt(getContext().getContentResolver(), "theater_mode_on", 0) == 0;
    }

    private void updateLocked() {
        this.mWakeLock.acquire();
        this.mHandler.sendEmptyMessage(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDockStateChange() {
        java.lang.String soundPath;
        android.net.Uri soundUri;
        android.media.Ringtone sfx;
        synchronized (this.mLock) {
            android.util.Slog.i(TAG, "Dock state changed from " + this.mPreviousDockState + " to " + this.mReportedDockState);
            int previousDockState = this.mPreviousDockState;
            this.mPreviousDockState = this.mReportedDockState;
            android.content.ContentResolver cr = getContext().getContentResolver();
            if (!this.mDeviceProvisionedObserver.isDeviceProvisioned()) {
                android.util.Slog.i(TAG, "Device not provisioned, skipping dock broadcast");
                return;
            }
            android.content.Intent intent = new android.content.Intent("android.intent.action.DOCK_EVENT");
            intent.addFlags(536870912);
            intent.putExtra("android.intent.extra.DOCK_STATE", this.mReportedDockState);
            boolean dockSoundsEnabled = android.provider.Settings.Global.getInt(cr, "dock_sounds_enabled", 1) == 1;
            boolean dockSoundsEnabledWhenAccessibility = android.provider.Settings.Global.getInt(cr, "dock_sounds_enabled_when_accessbility", 1) == 1;
            boolean accessibilityEnabled = android.provider.Settings.Secure.getInt(cr, "accessibility_enabled", 0) == 1;
            if (dockSoundsEnabled || (accessibilityEnabled && dockSoundsEnabledWhenAccessibility)) {
                java.lang.String whichSound = null;
                if (this.mReportedDockState == 0) {
                    if (previousDockState == 1 || previousDockState == 3 || previousDockState == 4) {
                        whichSound = "desk_undock_sound";
                    } else if (previousDockState == 2) {
                        whichSound = "car_undock_sound";
                    }
                } else if (this.mReportedDockState == 1 || this.mReportedDockState == 3 || this.mReportedDockState == 4) {
                    whichSound = "desk_dock_sound";
                } else if (this.mReportedDockState == 2) {
                    whichSound = "car_dock_sound";
                }
                if (whichSound != null && (soundPath = android.provider.Settings.Global.getString(cr, whichSound)) != null && (soundUri = android.net.Uri.parse("file://" + soundPath)) != null && (sfx = android.media.RingtoneManager.getRingtone(getContext(), soundUri)) != null) {
                    sfx.setStreamType(1);
                    sfx.preferBuiltinDevice(true);
                    sfx.play();
                }
            }
            getContext().sendStickyBroadcastAsUser(intent, android.os.UserHandle.ALL);
        }
    }

    private int getDockedStateExtraValue(com.android.server.DockObserver.ExtconStateProvider state) {
        for (com.android.server.DockObserver.ExtconStateConfig config : this.mExtconStateConfigs) {
            boolean match = true;
            for (android.util.Pair<java.lang.String, java.lang.String> keyValue : config.keyValuePairs) {
                java.lang.String stateValue = state.getValue((java.lang.String) keyValue.first);
                match = match && ((java.lang.String) keyValue.second).equals(stateValue);
                if (!match) {
                    break;
                }
            }
            if (match) {
                return config.extraStateValue;
            }
        }
        return 1;
    }

    void setDockStateFromProviderForTesting(com.android.server.DockObserver.ExtconStateProvider provider) {
        synchronized (this.mLock) {
            setDockStateFromProviderLocked(provider);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDockStateFromProviderLocked(com.android.server.DockObserver.ExtconStateProvider provider) {
        int state = 0;
        if ("1".equals(provider.getValue(com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_DOCK))) {
            state = getDockedStateExtraValue(provider);
        }
        setActualDockStateLocked(state);
    }

    private final class BinderService extends android.os.Binder {
        private BinderService() {
        }

        /* JADX WARN: Removed duplicated region for block: B:33:0x00b8 A[Catch: all -> 0x0124, Merged into TryCatch #1 {all -> 0x0127, blocks: (B:6:0x0013, B:7:0x0019, B:42:0x0126, B:9:0x001c, B:11:0x001f, B:14:0x002c, B:16:0x0031, B:18:0x003c, B:19:0x0041, B:21:0x004a, B:37:0x011e, B:22:0x0059, B:25:0x0071, B:27:0x0089, B:29:0x008c, B:31:0x0097, B:32:0x00a8, B:33:0x00b8, B:35:0x00c5, B:36:0x00ca), top: B:48:0x0013 }] */
        @Override // android.os.Binder
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        protected void dump(java.io.FileDescriptor r9, java.io.PrintWriter r10, java.lang.String[] r11) {
            /*
                Method dump skipped, instruction units count: 300
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.DockObserver.BinderService.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
        }
    }

    private final class DeviceProvisionedObserver extends android.database.ContentObserver {
        private boolean mRegistered;

        public DeviceProvisionedObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            synchronized (com.android.server.DockObserver.this.mLock) {
                updateRegistration();
                if (isDeviceProvisioned()) {
                    com.android.server.DockObserver.this.updateIfDockedLocked();
                }
            }
        }

        void onSystemReady() {
            updateRegistration();
        }

        private void updateRegistration() {
            boolean register = !isDeviceProvisioned();
            if (register == this.mRegistered) {
                return;
            }
            android.content.ContentResolver resolver = com.android.server.DockObserver.this.getContext().getContentResolver();
            if (register) {
                resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("device_provisioned"), false, this);
            } else {
                resolver.unregisterContentObserver(this);
            }
            this.mRegistered = register;
        }

        boolean isDeviceProvisioned() {
            return android.provider.Settings.Global.getInt(com.android.server.DockObserver.this.getContext().getContentResolver(), "device_provisioned", 0) != 0;
        }
    }
}
