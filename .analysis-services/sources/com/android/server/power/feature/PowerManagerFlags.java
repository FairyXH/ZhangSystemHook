package com.android.server.power.feature;

/* JADX INFO: loaded from: classes3.dex */
public class PowerManagerFlags {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "PowerManagerFlags";
    private final com.android.server.power.feature.PowerManagerFlags.FlagState mEarlyScreenTimeoutDetectorFlagState;
    private final com.android.server.power.feature.PowerManagerFlags.FlagState mImproveWakelockLatency;

    public PowerManagerFlags() {
        this.mEarlyScreenTimeoutDetectorFlagState = new com.android.server.power.feature.PowerManagerFlags.FlagState("com.android.server.power.feature.flags.enable_early_screen_timeout_detector", new java.util.function.Supplier() { // from class: com.android.server.power.feature.PowerManagerFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.power.feature.flags.Flags.enableEarlyScreenTimeoutDetector());
            }
        });
        this.mImproveWakelockLatency = new com.android.server.power.feature.PowerManagerFlags.FlagState("com.android.server.power.feature.flags.improve_wakelock_latency", new java.util.function.Supplier() { // from class: com.android.server.power.feature.PowerManagerFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.power.feature.flags.Flags.improveWakelockLatency());
            }
        });
    }

    public boolean isEarlyScreenTimeoutDetectorEnabled() {
        return this.mEarlyScreenTimeoutDetectorFlagState.isEnabled();
    }

    public boolean improveWakelockLatency() {
        return this.mImproveWakelockLatency.isEnabled();
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("PowerManagerFlags:");
        pw.println(" " + this.mEarlyScreenTimeoutDetectorFlagState);
        pw.println(" " + this.mImproveWakelockLatency);
    }

    private static class FlagState {
        private boolean mEnabled;
        private boolean mEnabledSet;
        private final java.util.function.Supplier<java.lang.Boolean> mFlagFunction;
        private final java.lang.String mName;

        private FlagState(java.lang.String name, java.util.function.Supplier<java.lang.Boolean> flagFunction) {
            this.mName = name;
            this.mFlagFunction = flagFunction;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEnabled() {
            if (this.mEnabledSet) {
                return this.mEnabled;
            }
            this.mEnabled = this.mFlagFunction.get().booleanValue();
            this.mEnabledSet = true;
            return this.mEnabled;
        }

        public java.lang.String toString() {
            int nameLength = this.mName.length();
            return android.text.TextUtils.substring(this.mName, 39, nameLength) + ": " + android.text.TextUtils.formatSimple("%" + (91 - nameLength) + "s%s", new java.lang.Object[]{" ", java.lang.Boolean.valueOf(isEnabled())}) + " (def:" + this.mFlagFunction.get() + ")";
        }
    }
}
