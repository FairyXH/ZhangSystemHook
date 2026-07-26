package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface GlobalActionsProvider {

    public interface GlobalActionsListener {
        void onGlobalActionsAvailableChanged(boolean z);

        void onGlobalActionsDismissed();

        void onGlobalActionsShown();
    }

    boolean isGlobalActionsDisabled();

    void setGlobalActionsListener(com.android.server.policy.GlobalActionsProvider.GlobalActionsListener globalActionsListener);

    void showGlobalActions();
}
