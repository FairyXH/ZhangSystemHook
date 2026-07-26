package com.android.server.emergency;

/* JADX INFO: loaded from: classes2.dex */
public class EmergencyAffordanceService extends com.android.server.SystemService {
    private static final boolean DBG = false;
    private static final java.lang.String EMERGENCY_AFFORDANCE_OVERRIDE_ISO = "emergency_affordance_override_iso";
    private static final int INITIALIZE_STATE = 1;
    private static final int NETWORK_COUNTRY_CHANGED = 2;
    private static final java.lang.String SERVICE_NAME = "emergency_affordance";
    private static final int SUBSCRIPTION_CHANGED = 3;
    private static final java.lang.String TAG = "EmergencyAffordanceService";
    private static final int UPDATE_AIRPLANE_MODE_STATUS = 4;
    private boolean mAirplaneModeEnabled;
    private boolean mAnyNetworkNeedsEmergencyAffordance;
    private boolean mAnySimNeedsEmergencyAffordance;
    private android.content.BroadcastReceiver mBroadcastReceiver;
    private final android.content.Context mContext;
    private boolean mEmergencyAffordanceNeeded;
    private final java.util.ArrayList<java.lang.String> mEmergencyCallCountryIsos;
    private com.android.server.emergency.EmergencyAffordanceService.MyHandler mHandler;
    private android.telephony.SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionChangedListener;
    private android.telephony.SubscriptionManager mSubscriptionManager;
    private android.telephony.TelephonyManager mTelephonyManager;
    private boolean mVoiceCapable;

    public EmergencyAffordanceService(android.content.Context context) {
        super(context);
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.emergency.EmergencyAffordanceService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.telephony.action.NETWORK_COUNTRY_CHANGED".equals(intent.getAction())) {
                    java.lang.String countryCode = intent.getStringExtra("android.telephony.extra.NETWORK_COUNTRY");
                    int slotId = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
                    com.android.server.emergency.EmergencyAffordanceService.this.mHandler.obtainMessage(2, slotId, 0, countryCode).sendToTarget();
                } else if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                    com.android.server.emergency.EmergencyAffordanceService.this.mHandler.obtainMessage(4).sendToTarget();
                }
            }
        };
        this.mSubscriptionChangedListener = new android.telephony.SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.server.emergency.EmergencyAffordanceService.2
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                com.android.server.emergency.EmergencyAffordanceService.this.mHandler.obtainMessage(3).sendToTarget();
            }
        };
        this.mContext = context;
        java.lang.String[] isos = context.getResources().getStringArray(android.R.array.config_dozeTapSensorPostureMapping);
        this.mEmergencyCallCountryIsos = new java.util.ArrayList<>(isos.length);
        for (java.lang.String iso : isos) {
            this.mEmergencyCallCountryIsos.add(iso);
        }
        if (android.os.Build.IS_DEBUGGABLE) {
            java.lang.String overrideIso = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), EMERGENCY_AFFORDANCE_OVERRIDE_ISO);
            if (!android.text.TextUtils.isEmpty(overrideIso)) {
                this.mEmergencyCallCountryIsos.clear();
                this.mEmergencyCallCountryIsos.add(overrideIso);
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService(SERVICE_NAME, new com.android.server.emergency.EmergencyAffordanceService.BinderService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 600) {
            handleThirdPartyBootPhase();
        }
    }

    private class MyHandler extends android.os.Handler {
        public MyHandler(android.os.Looper l) {
            super(l);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.emergency.EmergencyAffordanceService.this.handleInitializeState();
                    break;
                case 2:
                    java.lang.String countryIso = (java.lang.String) msg.obj;
                    int slotId = msg.arg1;
                    com.android.server.emergency.EmergencyAffordanceService.this.handleNetworkCountryChanged(countryIso, slotId);
                    break;
                case 3:
                    com.android.server.emergency.EmergencyAffordanceService.this.handleUpdateSimSubscriptionInfo();
                    break;
                case 4:
                    com.android.server.emergency.EmergencyAffordanceService.this.handleUpdateAirplaneModeStatus();
                    break;
                default:
                    android.util.Slog.e(com.android.server.emergency.EmergencyAffordanceService.TAG, "Unexpected message received: " + msg.what);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitializeState() {
        handleUpdateAirplaneModeStatus();
        handleUpdateSimSubscriptionInfo();
        updateNetworkCountry();
        updateEmergencyAffordanceNeeded();
    }

    private void handleThirdPartyBootPhase() {
        this.mTelephonyManager = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
        this.mVoiceCapable = this.mTelephonyManager.isVoiceCapable();
        if (!this.mVoiceCapable) {
            updateEmergencyAffordanceNeeded();
            return;
        }
        android.os.HandlerThread thread = new android.os.HandlerThread(TAG);
        thread.start();
        this.mHandler = new com.android.server.emergency.EmergencyAffordanceService.MyHandler(thread.getLooper());
        this.mSubscriptionManager = android.telephony.SubscriptionManager.from(this.mContext);
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mSubscriptionChangedListener);
        android.content.IntentFilter filter = new android.content.IntentFilter("android.intent.action.AIRPLANE_MODE");
        filter.addAction("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateAirplaneModeStatus() {
        this.mAirplaneModeEnabled = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateSimSubscriptionInfo() {
        java.util.List<android.telephony.SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null) {
            return;
        }
        boolean needsAffordance = false;
        java.util.Iterator<android.telephony.SubscriptionInfo> it = activeSubscriptionInfoList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.telephony.SubscriptionInfo info = it.next();
            if (isoRequiresEmergencyAffordance(info.getCountryIso())) {
                needsAffordance = true;
                break;
            }
        }
        this.mAnySimNeedsEmergencyAffordance = needsAffordance;
        updateEmergencyAffordanceNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetworkCountryChanged(java.lang.String countryIso, int slotId) {
        if (android.text.TextUtils.isEmpty(countryIso) && this.mAirplaneModeEnabled) {
            android.util.Slog.w(TAG, "Ignore empty countryIso report when APM is on.");
        } else {
            updateNetworkCountry();
            updateEmergencyAffordanceNeeded();
        }
    }

    private void updateNetworkCountry() {
        boolean needsAffordance = false;
        int activeModems = this.mTelephonyManager.getActiveModemCount();
        int i = 0;
        while (true) {
            if (i >= activeModems) {
                break;
            }
            java.lang.String countryIso = this.mTelephonyManager.getNetworkCountryIso(i);
            if (!isoRequiresEmergencyAffordance(countryIso)) {
                i++;
            } else {
                needsAffordance = true;
                break;
            }
        }
        this.mAnyNetworkNeedsEmergencyAffordance = needsAffordance;
        updateEmergencyAffordanceNeeded();
    }

    private boolean isoRequiresEmergencyAffordance(java.lang.String iso) {
        return this.mEmergencyCallCountryIsos.contains(iso);
    }

    private void updateEmergencyAffordanceNeeded() {
        boolean z = this.mEmergencyAffordanceNeeded;
        this.mEmergencyAffordanceNeeded = this.mVoiceCapable && (this.mAnySimNeedsEmergencyAffordance || this.mAnyNetworkNeedsEmergencyAffordance);
        if (z != this.mEmergencyAffordanceNeeded) {
            android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "emergency_affordance_needed", this.mEmergencyAffordanceNeeded ? 1 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(com.android.internal.util.IndentingPrintWriter ipw) {
        ipw.println("EmergencyAffordanceService (dumpsys emergency_affordance) state:\n");
        ipw.println("mEmergencyAffordanceNeeded=" + this.mEmergencyAffordanceNeeded);
        ipw.println("mVoiceCapable=" + this.mVoiceCapable);
        ipw.println("mAnySimNeedsEmergencyAffordance=" + this.mAnySimNeedsEmergencyAffordance);
        ipw.println("mAnyNetworkNeedsEmergencyAffordance=" + this.mAnyNetworkNeedsEmergencyAffordance);
        ipw.println("mEmergencyCallCountryIsos=" + java.lang.String.join(",", this.mEmergencyCallCountryIsos));
    }

    private final class BinderService extends android.os.Binder {
        private BinderService() {
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.emergency.EmergencyAffordanceService.this.mContext, com.android.server.emergency.EmergencyAffordanceService.TAG, pw)) {
                return;
            }
            com.android.server.emergency.EmergencyAffordanceService.this.dumpInternal(new com.android.internal.util.IndentingPrintWriter(pw, "  "));
        }
    }
}
