package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public interface ISessionExt {
    public static final int FILL_REQUEST_BY_NATIVE = 0;
    public static final int FILL_REQUEST_BY_OPLUS_AUTOFILL_REENTER_VIEW = 1;
    public static final int SAVE_REQUEST_BY_NATIVE = 0;
    public static final int SAVE_REQUEST_BY_OPLUS_AUTOFILL_ACTIVITY_FINISH = 1;

    default void hookSanitizeForParceling(android.app.assist.AssistStructure structure) {
    }

    default boolean isOplusAutofillService() {
        return false;
    }

    default void setRemoteServiceComponentName(android.content.ComponentName componentName) {
    }

    default java.lang.String getOplusAutofillDatasetFlag() {
        return null;
    }

    default boolean skipSaveUiAndNativeProcess() {
        return false;
    }

    default boolean skipSaveUi() {
        return false;
    }

    default boolean useOplusAutofillService(android.os.Bundle client, android.service.autofill.FillResponse newResponse) {
        return false;
    }

    default void hookHandleEmptyCurrentView(android.os.Bundle client) {
    }

    default void hookSetOnFillRequestReason(int reason) {
    }

    default void hookSetOnSaveRequestReason(int reason) {
    }

    default android.os.Bundle hookOnFillRequestClientState(android.os.Bundle clientState) {
        return clientState;
    }

    default android.os.Bundle hookOnSaveRequestClientState(android.os.Bundle clientState) {
        return clientState;
    }

    default boolean hookShouldRequestNewFillResponse() {
        return false;
    }
}
