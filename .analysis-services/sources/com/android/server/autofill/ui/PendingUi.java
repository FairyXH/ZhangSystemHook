package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
public final class PendingUi {
    public static final int STATE_CREATED = 1;
    public static final int STATE_FINISHED = 4;
    public static final int STATE_PENDING = 2;
    public final android.view.autofill.IAutoFillManagerClient client;
    private int mState = 1;
    private final android.os.IBinder mToken;
    public final int sessionId;

    public PendingUi(android.os.IBinder token, int sessionId, android.view.autofill.IAutoFillManagerClient client) {
        this.mToken = token;
        this.sessionId = sessionId;
        this.client = client;
    }

    public android.os.IBinder getToken() {
        return this.mToken;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public int getState() {
        return this.mState;
    }

    public boolean matches(android.os.IBinder token) {
        return this.mToken.equals(token);
    }

    public java.lang.String toString() {
        return "PendingUi: [token=" + this.mToken + ", sessionId=" + this.sessionId + ", state=" + android.util.DebugUtils.flagsToString(com.android.server.autofill.ui.PendingUi.class, "STATE_", this.mState) + "]";
    }
}
