package com.android.server.devicestate;

/* JADX INFO: loaded from: classes.dex */
final class OverrideRequestController {
    static final int FLAG_POWER_SAVE_ENABLED = 2;
    static final int FLAG_THERMAL_CRITICAL = 1;
    static final int STATUS_ACTIVE = 1;
    static final int STATUS_CANCELED = 2;
    static final int STATUS_UNKNOWN = 0;
    private static final java.lang.String TAG = "OverrideRequestController";
    private com.android.server.devicestate.OverrideRequest mBaseStateRequest;
    private final com.android.server.devicestate.OverrideRequestController.StatusChangeListener mListener;
    private com.android.server.devicestate.OverrideRequest mRequest;
    private boolean mStickyRequest;
    private boolean mStickyRequestsAllowed;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface RequestStatus {
    }

    public interface StatusChangeListener {
        void onStatusChanged(com.android.server.devicestate.OverrideRequest overrideRequest, int i, int i2);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface StatusChangedFlag {
    }

    static java.lang.String statusToString(int status) {
        switch (status) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "ACTIVE";
            case 2:
                return "CANCELED";
            default:
                throw new java.lang.IllegalArgumentException("Unknown status: " + status);
        }
    }

    OverrideRequestController(com.android.server.devicestate.OverrideRequestController.StatusChangeListener listener) {
        this.mListener = listener;
    }

    void setStickyRequestsAllowed(boolean stickyRequestsAllowed) {
        this.mStickyRequestsAllowed = stickyRequestsAllowed;
        if (!this.mStickyRequestsAllowed) {
            cancelStickyRequest();
        }
    }

    void addRequest(com.android.server.devicestate.OverrideRequest request) {
        com.android.server.devicestate.OverrideRequest previousRequest = this.mRequest;
        this.mRequest = request;
        this.mListener.onStatusChanged(request, 1, 0);
        if (previousRequest != null) {
            cancelRequestLocked(previousRequest);
        }
    }

    void addBaseStateRequest(com.android.server.devicestate.OverrideRequest request) {
        com.android.server.devicestate.OverrideRequest previousRequest = this.mBaseStateRequest;
        this.mBaseStateRequest = request;
        this.mListener.onStatusChanged(request, 1, 0);
        if (previousRequest != null) {
            cancelRequestLocked(previousRequest);
        }
    }

    void cancelRequest(com.android.server.devicestate.OverrideRequest request) {
        if (!hasRequest(request.getToken(), request.getRequestType())) {
            return;
        }
        cancelCurrentRequestLocked();
    }

    void cancelStickyRequest() {
        if (this.mStickyRequest) {
            cancelCurrentRequestLocked();
        }
    }

    void cancelOverrideRequest() {
        cancelCurrentRequestLocked();
    }

    void cancelBaseStateOverrideRequest() {
        cancelCurrentBaseStateRequestLocked();
    }

    boolean hasRequest(android.os.IBinder token, int requestType) {
        return requestType == 1 ? this.mBaseStateRequest != null && token == this.mBaseStateRequest.getToken() : this.mRequest != null && token == this.mRequest.getToken();
    }

    void handleProcessDied(int pid) {
        if (this.mBaseStateRequest != null && this.mBaseStateRequest.getPid() == pid) {
            cancelCurrentBaseStateRequestLocked();
        }
        if (this.mRequest != null && this.mRequest.getPid() == pid) {
            if (this.mRequest.getRequestedDeviceState().hasProperty(5)) {
                cancelCurrentRequestLocked();
            } else if (this.mStickyRequestsAllowed) {
                this.mStickyRequest = true;
            } else {
                cancelCurrentRequestLocked();
            }
        }
    }

    void handleBaseStateChanged(int state) {
        if (this.mBaseStateRequest != null && state != this.mBaseStateRequest.getRequestedStateIdentifier()) {
            cancelBaseStateOverrideRequest();
        }
        if (this.mRequest != null && (this.mRequest.getFlags() & 1) != 0) {
            cancelCurrentRequestLocked();
        }
    }

    void handleNewSupportedStates(int[] newSupportedStates, int reason) {
        boolean isThermalCritical = reason == 3;
        boolean isPowerSaveEnabled = reason == 4;
        int flags = (isThermalCritical ? 1 : 0) | 0 | (isPowerSaveEnabled ? 2 : 0);
        if (this.mBaseStateRequest != null && !contains(newSupportedStates, this.mBaseStateRequest.getRequestedStateIdentifier())) {
            cancelCurrentBaseStateRequestLocked(flags);
        }
        if (this.mRequest != null && !contains(newSupportedStates, this.mRequest.getRequestedStateIdentifier())) {
            cancelCurrentRequestLocked(flags);
        }
    }

    void dumpInternal(java.io.PrintWriter pw) {
        com.android.server.devicestate.OverrideRequest overrideRequest = this.mRequest;
        boolean requestActive = overrideRequest != null;
        pw.println();
        pw.println("Override Request active: " + requestActive);
        if (requestActive) {
            pw.println("Request: mPid=" + overrideRequest.getPid() + ", mRequestedState=" + overrideRequest.getRequestedStateIdentifier() + ", mFlags=" + overrideRequest.getFlags() + ", mStatus=" + statusToString(1));
        }
    }

    private void cancelRequestLocked(com.android.server.devicestate.OverrideRequest requestToCancel) {
        cancelRequestLocked(requestToCancel, 0);
    }

    private void cancelRequestLocked(com.android.server.devicestate.OverrideRequest requestToCancel, int flags) {
        this.mListener.onStatusChanged(requestToCancel, 2, flags);
    }

    private void cancelCurrentRequestLocked() {
        cancelCurrentRequestLocked(0);
    }

    private void cancelCurrentRequestLocked(int flags) {
        if (this.mRequest == null) {
            android.util.Slog.w(TAG, "Attempted to cancel a null OverrideRequest");
            return;
        }
        this.mStickyRequest = false;
        cancelRequestLocked(this.mRequest, flags);
        this.mRequest = null;
    }

    private void cancelCurrentBaseStateRequestLocked() {
        cancelCurrentBaseStateRequestLocked(0);
    }

    private void cancelCurrentBaseStateRequestLocked(int flags) {
        if (this.mBaseStateRequest == null) {
            android.util.Slog.w(TAG, "Attempted to cancel a null OverrideRequest");
        } else {
            cancelRequestLocked(this.mBaseStateRequest, flags);
            this.mBaseStateRequest = null;
        }
    }

    private static boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }
}
