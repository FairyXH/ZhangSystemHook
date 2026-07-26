package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class AuthenticationStateListeners implements android.os.IBinder.DeathRecipient {
    private static final java.lang.String TAG = "AuthenticationStateListeners";
    private final java.util.concurrent.CopyOnWriteArrayList<android.hardware.biometrics.AuthenticationStateListener> mAuthenticationStateListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public void registerAuthenticationStateListener(android.hardware.biometrics.AuthenticationStateListener listener) {
        this.mAuthenticationStateListeners.add(listener);
        try {
            listener.asBinder().linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to link to death", e);
        }
    }

    public void unregisterAuthenticationStateListener(android.hardware.biometrics.AuthenticationStateListener listener) {
        this.mAuthenticationStateListeners.remove(listener);
    }

    public void onAuthenticationAcquired(android.hardware.biometrics.events.AuthenticationAcquiredInfo authInfo) {
        for (android.hardware.biometrics.AuthenticationStateListener listener : this.mAuthenticationStateListeners) {
            try {
                listener.onAuthenticationAcquired(authInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in notifying listener that authentication acquired", e);
            }
        }
    }

    public void onAuthenticationError(android.hardware.biometrics.events.AuthenticationErrorInfo authInfo) {
        for (android.hardware.biometrics.AuthenticationStateListener listener : this.mAuthenticationStateListeners) {
            try {
                listener.onAuthenticationError(authInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in notifying listener of unrecoverable authentication error", e);
            }
        }
    }

    public void onAuthenticationFailed(android.hardware.biometrics.events.AuthenticationFailedInfo authInfo) {
        for (android.hardware.biometrics.AuthenticationStateListener listener : this.mAuthenticationStateListeners) {
            try {
                listener.onAuthenticationFailed(authInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in notifying listener that authentication failed", e);
            }
        }
    }

    public void onAuthenticationHelp(android.hardware.biometrics.events.AuthenticationHelpInfo authInfo) {
        for (android.hardware.biometrics.AuthenticationStateListener listener : this.mAuthenticationStateListeners) {
            try {
                listener.onAuthenticationHelp(authInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in notifying listener of recoverable authentication error", e);
            }
        }
    }

    public void onAuthenticationStarted(android.hardware.biometrics.events.AuthenticationStartedInfo authInfo) {
        for (android.hardware.biometrics.AuthenticationStateListener listener : this.mAuthenticationStateListeners) {
            try {
                listener.onAuthenticationStarted(authInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in notifying listener that authentication started", e);
            }
        }
    }

    public void onAuthenticationStopped(android.hardware.biometrics.events.AuthenticationStoppedInfo authInfo) {
        for (android.hardware.biometrics.AuthenticationStateListener listener : this.mAuthenticationStateListeners) {
            try {
                listener.onAuthenticationStopped(authInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in notifying listener that authentication stopped", e);
            }
        }
    }

    public void onAuthenticationSucceeded(android.hardware.biometrics.events.AuthenticationSucceededInfo authInfo) {
        for (android.hardware.biometrics.AuthenticationStateListener listener : this.mAuthenticationStateListeners) {
            try {
                listener.onAuthenticationSucceeded(authInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in notifying listener that authentication succeeded", e);
            }
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(final android.os.IBinder who) {
        android.util.Slog.w(TAG, "Callback binder died: " + who);
        if (this.mAuthenticationStateListeners.removeIf(new java.util.function.Predicate() { // from class: com.android.server.biometrics.sensors.AuthenticationStateListeners$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.hardware.biometrics.AuthenticationStateListener) obj).asBinder().equals(who);
            }
        })) {
            android.util.Slog.w(TAG, "Removed dead listener for " + who);
        } else {
            android.util.Slog.w(TAG, "No dead listeners found");
        }
    }
}
