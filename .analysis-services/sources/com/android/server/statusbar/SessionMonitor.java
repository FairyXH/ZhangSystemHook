package com.android.server.statusbar;

/* JADX INFO: loaded from: classes3.dex */
public class SessionMonitor {
    private static final java.lang.String TAG = "SessionMonitor";
    private final android.content.Context mContext;
    private final java.util.Map<java.lang.Integer, java.util.Set<com.android.internal.statusbar.ISessionListener>> mSessionToListeners = new java.util.HashMap();

    public SessionMonitor(android.content.Context context) {
        this.mContext = context;
        java.util.Iterator it = android.app.StatusBarManager.ALL_SESSIONS.iterator();
        while (it.hasNext()) {
            int session = ((java.lang.Integer) it.next()).intValue();
            this.mSessionToListeners.put(java.lang.Integer.valueOf(session), new java.util.HashSet());
        }
    }

    public void registerSessionListener(int sessionFlags, com.android.internal.statusbar.ISessionListener listener) {
        requireListenerPermissions(sessionFlags);
        synchronized (this.mSessionToListeners) {
            java.util.Iterator it = android.app.StatusBarManager.ALL_SESSIONS.iterator();
            while (it.hasNext()) {
                int sessionType = ((java.lang.Integer) it.next()).intValue();
                if ((sessionFlags & sessionType) != 0) {
                    this.mSessionToListeners.get(java.lang.Integer.valueOf(sessionType)).add(listener);
                }
            }
        }
    }

    public void unregisterSessionListener(int sessionFlags, com.android.internal.statusbar.ISessionListener listener) {
        synchronized (this.mSessionToListeners) {
            java.util.Iterator it = android.app.StatusBarManager.ALL_SESSIONS.iterator();
            while (it.hasNext()) {
                int sessionType = ((java.lang.Integer) it.next()).intValue();
                if ((sessionFlags & sessionType) != 0) {
                    this.mSessionToListeners.get(java.lang.Integer.valueOf(sessionType)).remove(listener);
                }
            }
        }
    }

    public void onSessionStarted(int sessionType, com.android.internal.logging.InstanceId instanceId) {
        requireSetterPermissions(sessionType);
        if (!isValidSessionType(sessionType)) {
            android.util.Log.e(TAG, "invalid onSessionStarted sessionType=" + sessionType);
            return;
        }
        synchronized (this.mSessionToListeners) {
            for (com.android.internal.statusbar.ISessionListener listener : this.mSessionToListeners.get(java.lang.Integer.valueOf(sessionType))) {
                try {
                    listener.onSessionStarted(sessionType, instanceId);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(TAG, "unable to send session start to listener=" + listener, e);
                }
            }
        }
    }

    public void onSessionEnded(int sessionType, com.android.internal.logging.InstanceId instanceId) {
        requireSetterPermissions(sessionType);
        if (!isValidSessionType(sessionType)) {
            android.util.Log.e(TAG, "invalid onSessionEnded sessionType=" + sessionType);
            return;
        }
        synchronized (this.mSessionToListeners) {
            for (com.android.internal.statusbar.ISessionListener listener : this.mSessionToListeners.get(java.lang.Integer.valueOf(sessionType))) {
                try {
                    listener.onSessionEnded(sessionType, instanceId);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(TAG, "unable to send session end to listener=" + listener, e);
                }
            }
        }
    }

    private boolean isValidSessionType(int sessionType) {
        return android.app.StatusBarManager.ALL_SESSIONS.contains(java.lang.Integer.valueOf(sessionType));
    }

    private void requireListenerPermissions(int sessionFlags) {
        if ((sessionFlags & 1) != 0) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_BIOMETRIC", "StatusBarManagerService.SessionMonitor");
        }
        if ((sessionFlags & 2) != 0) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_BIOMETRIC", "StatusBarManagerService.SessionMonitor");
        }
    }

    private void requireSetterPermissions(int sessionFlags) {
        if ((sessionFlags & 1) != 0) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_KEYGUARD", "StatusBarManagerService.SessionMonitor");
        }
        if ((sessionFlags & 2) != 0) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.STATUS_BAR_SERVICE", "StatusBarManagerService.SessionMonitor");
        }
    }
}
