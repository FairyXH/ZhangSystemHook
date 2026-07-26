package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public enum OemUiUsageStatus {
    UNKNOWN(0),
    SUCCESS(1),
    FAILURE_NOT_SPECIFIED(2),
    FAILURE_SPECIFIED_BUT_NOT_FOUND(3),
    FAILURE_SPECIFIED_BUT_NOT_ENABLED(4);

    private final int mLoggingInt;

    OemUiUsageStatus(int loggingInt) {
        this.mLoggingInt = loggingInt;
    }

    public int getLoggingInt() {
        return this.mLoggingInt;
    }

    /* JADX INFO: renamed from: com.android.server.credentials.metrics.OemUiUsageStatus$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$credentials$selection$IntentCreationResult$OemUiUsageStatus = new int[android.credentials.selection.IntentCreationResult.OemUiUsageStatus.values().length];

        static {
            try {
                $SwitchMap$android$credentials$selection$IntentCreationResult$OemUiUsageStatus[android.credentials.selection.IntentCreationResult.OemUiUsageStatus.UNKNOWN.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$credentials$selection$IntentCreationResult$OemUiUsageStatus[android.credentials.selection.IntentCreationResult.OemUiUsageStatus.SUCCESS.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$credentials$selection$IntentCreationResult$OemUiUsageStatus[android.credentials.selection.IntentCreationResult.OemUiUsageStatus.OEM_UI_CONFIG_NOT_SPECIFIED.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$credentials$selection$IntentCreationResult$OemUiUsageStatus[android.credentials.selection.IntentCreationResult.OemUiUsageStatus.OEM_UI_CONFIG_SPECIFIED_BUT_NOT_FOUND.ordinal()] = 4;
            } catch (java.lang.NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$credentials$selection$IntentCreationResult$OemUiUsageStatus[android.credentials.selection.IntentCreationResult.OemUiUsageStatus.OEM_UI_CONFIG_SPECIFIED_FOUND_BUT_NOT_ENABLED.ordinal()] = 5;
            } catch (java.lang.NoSuchFieldError e5) {
            }
        }
    }

    public static com.android.server.credentials.metrics.OemUiUsageStatus createFrom(android.credentials.selection.IntentCreationResult.OemUiUsageStatus from) {
        switch (com.android.server.credentials.metrics.OemUiUsageStatus.AnonymousClass1.$SwitchMap$android$credentials$selection$IntentCreationResult$OemUiUsageStatus[from.ordinal()]) {
            case 1:
                return UNKNOWN;
            case 2:
                return SUCCESS;
            case 3:
                return FAILURE_NOT_SPECIFIED;
            case 4:
                return FAILURE_SPECIFIED_BUT_NOT_FOUND;
            case 5:
                return FAILURE_SPECIFIED_BUT_NOT_ENABLED;
            default:
                return UNKNOWN;
        }
    }
}
