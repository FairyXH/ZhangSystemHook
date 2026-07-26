package com.android.server.tv;

/* JADX INFO: loaded from: classes3.dex */
final class TvRemoteServiceInput extends android.media.tv.ITvRemoteServiceInput.Stub {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_KEYS = false;
    private static final java.lang.String TAG = "TvRemoteServiceInput";
    private final java.util.Map<android.os.IBinder, com.android.server.tv.UinputBridge> mBridgeMap = new android.util.ArrayMap();
    private final java.lang.Object mLock;
    private final android.media.tv.ITvRemoteProvider mProvider;

    TvRemoteServiceInput(java.lang.Object lock, android.media.tv.ITvRemoteProvider provider) {
        this.mLock = lock;
        this.mProvider = provider;
    }

    public void openInputBridge(final android.os.IBinder token, java.lang.String name, int width, int height, int maxPointers) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    if (!this.mBridgeMap.containsKey(token)) {
                        long idToken = android.os.Binder.clearCallingIdentity();
                        try {
                            try {
                                try {
                                    this.mBridgeMap.put(token, new com.android.server.tv.UinputBridge(token, name, width, height, maxPointers));
                                    token.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.tv.TvRemoteServiceInput.1
                                        @Override // android.os.IBinder.DeathRecipient
                                        public void binderDied() {
                                            com.android.server.tv.TvRemoteServiceInput.this.closeInputBridge(token);
                                        }
                                    }, 0);
                                    android.os.Binder.restoreCallingIdentity(idToken);
                                } catch (java.lang.Throwable th) {
                                    e = th;
                                    android.os.Binder.restoreCallingIdentity(idToken);
                                    throw e;
                                }
                            } catch (java.io.IOException e) {
                                try {
                                    android.util.Slog.e(TAG, "Cannot create device for " + name);
                                    android.os.Binder.restoreCallingIdentity(idToken);
                                    return;
                                } catch (java.lang.Throwable th2) {
                                    e = th2;
                                    android.os.Binder.restoreCallingIdentity(idToken);
                                    throw e;
                                }
                            }
                        } catch (android.os.RemoteException e2) {
                            android.util.Slog.e(TAG, "Token is already dead");
                            closeInputBridge(token);
                            android.os.Binder.restoreCallingIdentity(idToken);
                            return;
                        }
                    }
                    try {
                        this.mProvider.onInputBridgeConnected(token);
                    } catch (android.os.RemoteException e3) {
                        android.util.Slog.e(TAG, "Failed remote call to onInputBridgeConnected");
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    public void openGamepadBridge(final android.os.IBinder token, java.lang.String name) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (!this.mBridgeMap.containsKey(token)) {
                long idToken = android.os.Binder.clearCallingIdentity();
                try {
                    this.mBridgeMap.put(token, com.android.server.tv.UinputBridge.openGamepad(token, name));
                    token.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.tv.TvRemoteServiceInput.2
                        @Override // android.os.IBinder.DeathRecipient
                        public void binderDied() {
                            com.android.server.tv.TvRemoteServiceInput.this.closeInputBridge(token);
                        }
                    }, 0);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Token is already dead");
                    closeInputBridge(token);
                    return;
                } catch (java.io.IOException e2) {
                    android.util.Slog.e(TAG, "Cannot create device for " + name);
                    return;
                } finally {
                    android.os.Binder.restoreCallingIdentity(idToken);
                }
            }
        }
        try {
            this.mProvider.onInputBridgeConnected(token);
        } catch (android.os.RemoteException e3) {
            android.util.Slog.e(TAG, "Failed remote call to onInputBridgeConnected");
        }
    }

    public void closeInputBridge(android.os.IBinder token) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.remove(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.close(token);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }

    public void clearInputBridge(android.os.IBinder token) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.get(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.clear(token);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }

    public void sendTimestamp(android.os.IBinder token, long timestamp) {
    }

    public void sendKeyDown(android.os.IBinder token, int keyCode) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.get(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.sendKeyDown(token, keyCode);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }

    public void sendKeyUp(android.os.IBinder token, int keyCode) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.get(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.sendKeyUp(token, keyCode);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }

    public void sendPointerDown(android.os.IBinder token, int pointerId, int x, int y) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.get(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.sendPointerDown(token, pointerId, x, y);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }

    public void sendPointerUp(android.os.IBinder token, int pointerId) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.get(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.sendPointerUp(token, pointerId);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }

    public void sendPointerSync(android.os.IBinder token) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.get(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.sendPointerSync(token);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }

    public void sendGamepadKeyUp(android.os.IBinder token, int keyIndex) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.get(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.sendGamepadKey(token, keyIndex, false);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }

    public void sendGamepadKeyDown(android.os.IBinder token, int keyCode) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.get(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.sendGamepadKey(token, keyCode, true);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }

    public void sendGamepadAxisValue(android.os.IBinder token, int axis, float value) {
        synchronized (this.mLock) {
            com.android.server.tv.UinputBridge inputBridge = this.mBridgeMap.get(token);
            if (inputBridge == null) {
                android.util.Slog.w(TAG, java.lang.String.format("Input bridge not found for token: %s", token));
                return;
            }
            long idToken = android.os.Binder.clearCallingIdentity();
            try {
                inputBridge.sendGamepadAxisValue(token, axis, value);
            } finally {
                android.os.Binder.restoreCallingIdentity(idToken);
            }
        }
    }
}
