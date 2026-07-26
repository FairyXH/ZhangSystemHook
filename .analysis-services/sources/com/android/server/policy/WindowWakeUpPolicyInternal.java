package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface WindowWakeUpPolicyInternal {

    public interface InputWakeUpDelegate {
        boolean wakeUpFromKey(long j, int i, boolean z);

        boolean wakeUpFromMotion(long j, int i, boolean z);
    }

    void setInputWakeUpDelegate(com.android.server.policy.WindowWakeUpPolicyInternal.InputWakeUpDelegate inputWakeUpDelegate);
}
