package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class UnknownAppVisibilityController {
    private static final java.lang.String TAG = "WindowManager";
    private static final int UNKNOWN_STATE_WAITING_RELAYOUT = 2;
    private static final int UNKNOWN_STATE_WAITING_RESUME = 1;
    private static final int UNKNOWN_STATE_WAITING_VISIBILITY_UPDATE = 3;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private final com.android.server.wm.WindowManagerService mService;
    private final android.util.ArrayMap<com.android.server.wm.ActivityRecord, java.lang.Integer> mUnknownApps = new android.util.ArrayMap<>();

    UnknownAppVisibilityController(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent) {
        this.mService = service;
        this.mDisplayContent = displayContent;
    }

    boolean allResolved() {
        return this.mUnknownApps.isEmpty();
    }

    boolean isVisibilityUnknown(com.android.server.wm.ActivityRecord r) {
        if (this.mUnknownApps.isEmpty()) {
            return false;
        }
        return this.mUnknownApps.containsKey(r);
    }

    void clear() {
        this.mUnknownApps.clear();
    }

    java.lang.String getDebugMessage() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        for (int i = this.mUnknownApps.size() - 1; i >= 0; i--) {
            builder.append("app=").append(this.mUnknownApps.keyAt(i)).append(" state=").append(this.mUnknownApps.valueAt(i));
            if (i != 0) {
                builder.append(' ');
            }
        }
        return builder.toString();
    }

    void appRemovedOrHidden(com.android.server.wm.ActivityRecord activity) {
        if (this.mUnknownApps.isEmpty()) {
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
            android.util.Slog.d(TAG, "App removed or hidden activity=" + activity);
        }
        this.mUnknownApps.remove(activity);
    }

    void notifyLaunched(com.android.server.wm.ActivityRecord activity) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
            android.util.Slog.d(TAG, "App launched activity=" + activity);
        }
        if (!activity.mLaunchTaskBehind) {
            this.mUnknownApps.put(activity, 1);
        } else {
            this.mUnknownApps.put(activity, 2);
        }
    }

    void notifyAppResumedFinished(com.android.server.wm.ActivityRecord activity) {
        java.lang.Integer state;
        if (!this.mUnknownApps.isEmpty() && (state = this.mUnknownApps.get(activity)) != null && state.intValue() == 1) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
                android.util.Slog.d(TAG, "App resume finished activity=" + activity);
            }
            this.mUnknownApps.put(activity, 2);
        }
    }

    void notifyRelayouted(com.android.server.wm.ActivityRecord activity) {
        java.lang.Integer state;
        if (this.mUnknownApps.isEmpty() || (state = this.mUnknownApps.get(activity)) == null) {
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
            android.util.Slog.d(TAG, "App relayouted appWindow=" + activity);
        }
        if (state.intValue() == 2 || activity.mStartingWindow != null) {
            this.mUnknownApps.put(activity, 3);
            this.mDisplayContent.notifyKeyguardFlagsChanged();
            notifyVisibilitiesUpdated();
        }
    }

    private void notifyVisibilitiesUpdated() {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
            android.util.Slog.d(TAG, "Visibility updated DONE");
        }
        boolean changed = false;
        for (int i = this.mUnknownApps.size() - 1; i >= 0; i--) {
            if (this.mUnknownApps.valueAt(i).intValue() == 3) {
                this.mUnknownApps.removeAt(i);
                changed = true;
            }
        }
        if (changed) {
            this.mService.mWindowPlacerLocked.performSurfacePlacement();
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        if (this.mUnknownApps.isEmpty()) {
            return;
        }
        pw.println(prefix + "Unknown visibilities:");
        for (int i = this.mUnknownApps.size() - 1; i >= 0; i--) {
            pw.println(prefix + "  app=" + this.mUnknownApps.keyAt(i) + " state=" + this.mUnknownApps.valueAt(i));
        }
    }
}
