package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class AmbientDisplaySuppressionController {
    private static final java.lang.String TAG = "AmbientDisplaySuppressionController";
    private final com.android.server.power.AmbientDisplaySuppressionController.AmbientDisplaySuppressionChangedCallback mCallback;
    private com.android.internal.statusbar.IStatusBarService mStatusBarService;
    private final java.util.Set<android.util.Pair<java.lang.String, java.lang.Integer>> mSuppressionTokens = java.util.Collections.synchronizedSet(new android.util.ArraySet());

    interface AmbientDisplaySuppressionChangedCallback {
        void onSuppressionChanged(boolean z);
    }

    AmbientDisplaySuppressionController(com.android.server.power.AmbientDisplaySuppressionController.AmbientDisplaySuppressionChangedCallback callback) {
        this.mCallback = (com.android.server.power.AmbientDisplaySuppressionController.AmbientDisplaySuppressionChangedCallback) java.util.Objects.requireNonNull(callback);
    }

    public void suppress(java.lang.String token, int callingUid, boolean suppress) {
        android.util.Pair<java.lang.String, java.lang.Integer> suppressionToken = android.util.Pair.create((java.lang.String) java.util.Objects.requireNonNull(token), java.lang.Integer.valueOf(callingUid));
        boolean wasSuppressed = isSuppressed();
        if (suppress) {
            this.mSuppressionTokens.add(suppressionToken);
        } else {
            this.mSuppressionTokens.remove(suppressionToken);
        }
        boolean isSuppressed = isSuppressed();
        if (isSuppressed != wasSuppressed) {
            this.mCallback.onSuppressionChanged(isSuppressed);
        }
        try {
            synchronized (this.mSuppressionTokens) {
                getStatusBar().suppressAmbientDisplay(isSuppressed);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to suppress ambient display", e);
        }
    }

    java.util.List<java.lang.String> getSuppressionTokens(int callingUid) {
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        synchronized (this.mSuppressionTokens) {
            for (android.util.Pair<java.lang.String, java.lang.Integer> token : this.mSuppressionTokens) {
                if (((java.lang.Integer) token.second).intValue() == callingUid) {
                    result.add((java.lang.String) token.first);
                }
            }
        }
        return result;
    }

    public boolean isSuppressed(java.lang.String token, int callingUid) {
        return this.mSuppressionTokens.contains(android.util.Pair.create((java.lang.String) java.util.Objects.requireNonNull(token), java.lang.Integer.valueOf(callingUid)));
    }

    public boolean isSuppressed() {
        return !this.mSuppressionTokens.isEmpty();
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("AmbientDisplaySuppressionController:");
        pw.println(" ambientDisplaySuppressed=" + isSuppressed());
        pw.println(" mSuppressionTokens=" + this.mSuppressionTokens);
    }

    private synchronized com.android.internal.statusbar.IStatusBarService getStatusBar() {
        if (this.mStatusBarService == null) {
            this.mStatusBarService = com.android.internal.statusbar.IStatusBarService.Stub.asInterface(android.os.ServiceManager.getService("statusbar"));
        }
        return this.mStatusBarService;
    }
}
