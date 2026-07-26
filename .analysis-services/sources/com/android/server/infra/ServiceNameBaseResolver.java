package com.android.server.infra;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ServiceNameBaseResolver implements com.android.server.infra.ServiceNameResolver {
    private static final int MSG_RESET_TEMPORARY_SERVICE = 0;
    private static final java.lang.String TAG = com.android.server.infra.ServiceNameBaseResolver.class.getSimpleName();
    protected final android.content.Context mContext;
    protected final boolean mIsMultiple;
    private com.android.server.infra.ServiceNameResolver.NameResolverListener mOnSetCallback;
    private android.os.Handler mTemporaryHandler;
    private long mTemporaryServiceExpiration;
    protected final java.lang.Object mLock = new java.lang.Object();
    protected final android.util.SparseArray<java.lang.String[]> mTemporaryServiceNamesList = new android.util.SparseArray<>();
    protected final android.util.SparseBooleanArray mDefaultServicesDisabled = new android.util.SparseBooleanArray();

    public abstract java.lang.String readServiceName(int i);

    public abstract java.lang.String[] readServiceNameList(int i);

    protected ServiceNameBaseResolver(android.content.Context context, boolean isMultiple) {
        this.mContext = context;
        this.mIsMultiple = isMultiple;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void setOnTemporaryServiceNameChangedCallback(com.android.server.infra.ServiceNameResolver.NameResolverListener callback) {
        synchronized (this.mLock) {
            this.mOnSetCallback = callback;
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public java.lang.String getServiceName(int userId) {
        java.lang.String[] serviceNames = getServiceNameList(userId);
        if (serviceNames == null || serviceNames.length == 0) {
            return null;
        }
        return serviceNames[0];
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public java.lang.String getDefaultServiceName(int userId) {
        java.lang.String[] serviceNames = getDefaultServiceNameList(userId);
        if (serviceNames == null || serviceNames.length == 0) {
            return null;
        }
        return serviceNames[0];
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public java.lang.String[] getServiceNameList(int userId) {
        synchronized (this.mLock) {
            java.lang.String[] temporaryNames = this.mTemporaryServiceNamesList.get(userId);
            if (temporaryNames != null) {
                android.util.Slog.w(TAG, "getServiceName(): using temporary name " + java.util.Arrays.toString(temporaryNames) + " for user " + userId);
                return temporaryNames;
            }
            boolean disabled = this.mDefaultServicesDisabled.get(userId);
            if (disabled) {
                android.util.Slog.w(TAG, "getServiceName(): temporary name not set and default disabled for user " + userId);
                return null;
            }
            return getDefaultServiceNameList(userId);
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public java.lang.String[] getDefaultServiceNameList(int userId) {
        synchronized (this.mLock) {
            if (this.mIsMultiple) {
                java.lang.String[] serviceNameList = readServiceNameList(userId);
                java.util.List<java.lang.String> validatedServiceNameList = new java.util.ArrayList<>();
                for (int i = 0; i < serviceNameList.length; i++) {
                    try {
                        if (!android.text.TextUtils.isEmpty(serviceNameList[i])) {
                            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceNameList[i]);
                            android.content.pm.ServiceInfo serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 786432L, userId);
                            if (serviceInfo != null) {
                                validatedServiceNameList.add(serviceNameList[i]);
                            }
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(TAG, "Could not validate provided services.", e);
                    }
                }
                java.lang.String[] validatedServiceNameArray = new java.lang.String[validatedServiceNameList.size()];
                return (java.lang.String[]) validatedServiceNameList.toArray(validatedServiceNameArray);
            }
            java.lang.String name = readServiceName(userId);
            return android.text.TextUtils.isEmpty(name) ? new java.lang.String[0] : new java.lang.String[]{name};
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public boolean isConfiguredInMultipleMode() {
        return this.mIsMultiple;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public boolean isTemporary(int userId) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mTemporaryServiceNamesList.get(userId) != null;
        }
        return z;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void setTemporaryService(int userId, java.lang.String componentName, int durationMs) {
        setTemporaryServices(userId, new java.lang.String[]{componentName}, durationMs);
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void setTemporaryServices(final int userId, java.lang.String[] componentNames, int durationMs) {
        synchronized (this.mLock) {
            this.mTemporaryServiceNamesList.put(userId, componentNames);
            if (this.mTemporaryHandler == null) {
                this.mTemporaryHandler = new android.os.Handler(android.os.Looper.getMainLooper(), null, true) { // from class: com.android.server.infra.ServiceNameBaseResolver.1
                    @Override // android.os.Handler
                    public void handleMessage(android.os.Message msg) {
                        if (msg.what == 0) {
                            synchronized (com.android.server.infra.ServiceNameBaseResolver.this.mLock) {
                                com.android.server.infra.ServiceNameBaseResolver.this.resetTemporaryService(userId);
                            }
                            return;
                        }
                        android.util.Slog.wtf(com.android.server.infra.ServiceNameBaseResolver.TAG, "invalid handler msg: " + msg);
                    }
                };
            } else {
                this.mTemporaryHandler.removeMessages(0);
            }
            this.mTemporaryServiceExpiration = android.os.SystemClock.elapsedRealtime() + ((long) durationMs);
            this.mTemporaryHandler.sendEmptyMessageDelayed(0, durationMs);
            for (java.lang.String str : componentNames) {
                notifyTemporaryServiceNameChangedLocked(userId, str, true);
            }
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void resetTemporaryService(int userId) {
        synchronized (this.mLock) {
            android.util.Slog.i(TAG, "resetting temporary service for user " + userId + " from " + java.util.Arrays.toString(this.mTemporaryServiceNamesList.get(userId)));
            this.mTemporaryServiceNamesList.remove(userId);
            if (this.mTemporaryHandler != null) {
                this.mTemporaryHandler.removeMessages(0);
                this.mTemporaryHandler = null;
            }
            notifyTemporaryServiceNameChangedLocked(userId, null, false);
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public boolean setDefaultServiceEnabled(int userId, boolean enabled) {
        synchronized (this.mLock) {
            boolean currentlyEnabled = isDefaultServiceEnabledLocked(userId);
            if (currentlyEnabled == enabled) {
                android.util.Slog.i(TAG, "setDefaultServiceEnabled(" + userId + "): already " + enabled);
                return false;
            }
            if (enabled) {
                android.util.Slog.i(TAG, "disabling default service for user " + userId);
                this.mDefaultServicesDisabled.delete(userId);
            } else {
                android.util.Slog.i(TAG, "enabling default service for user " + userId);
                this.mDefaultServicesDisabled.put(userId, true);
            }
            return true;
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public boolean isDefaultServiceEnabled(int userId) {
        boolean zIsDefaultServiceEnabledLocked;
        synchronized (this.mLock) {
            zIsDefaultServiceEnabledLocked = isDefaultServiceEnabledLocked(userId);
        }
        return zIsDefaultServiceEnabledLocked;
    }

    private boolean isDefaultServiceEnabledLocked(int userId) {
        return !this.mDefaultServicesDisabled.get(userId);
    }

    public java.lang.String toString() {
        java.lang.String str;
        synchronized (this.mLock) {
            str = "FrameworkResourcesServiceNamer[temps=" + this.mTemporaryServiceNamesList + "]";
        }
        return str;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void dumpShort(java.io.PrintWriter pw, int userId) {
        synchronized (this.mLock) {
            java.lang.String[] temporaryNames = this.mTemporaryServiceNamesList.get(userId);
            if (temporaryNames != null) {
                pw.print("tmpName=");
                pw.print(java.util.Arrays.toString(temporaryNames));
                long ttl = this.mTemporaryServiceExpiration - android.os.SystemClock.elapsedRealtime();
                pw.print(" (expires in ");
                android.util.TimeUtils.formatDuration(ttl, pw);
                pw.print("), ");
            }
            pw.print("defaultName=");
            pw.print(getDefaultServiceName(userId));
            boolean disabled = this.mDefaultServicesDisabled.get(userId);
            pw.println(disabled ? " (disabled)" : " (enabled)");
        }
    }

    private void notifyTemporaryServiceNameChangedLocked(int userId, java.lang.String newTemporaryName, boolean isTemporary) {
        if (this.mOnSetCallback != null) {
            this.mOnSetCallback.onNameResolved(userId, newTemporaryName, isTemporary);
        }
    }
}
