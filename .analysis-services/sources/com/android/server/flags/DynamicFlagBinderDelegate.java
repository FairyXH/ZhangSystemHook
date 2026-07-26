package com.android.server.flags;

/* JADX INFO: loaded from: classes2.dex */
class DynamicFlagBinderDelegate {
    private static final java.util.function.Function<java.lang.Integer, java.util.Set<android.flags.IFeatureFlagsCallback>> NEW_CALLBACK_SET = new java.util.function.Function() { // from class: com.android.server.flags.DynamicFlagBinderDelegate$$ExternalSyntheticLambda1
        @Override // java.util.function.Function
        public final java.lang.Object apply(java.lang.Object obj) {
            return com.android.server.flags.DynamicFlagBinderDelegate.lambda$static$0((java.lang.Integer) obj);
        }
    };
    private final com.android.server.flags.FlagOverrideStore mFlagStore;
    private final com.android.server.flags.FlagCache<com.android.server.flags.DynamicFlagBinderDelegate.DynamicFlagData> mDynamicFlags = new com.android.server.flags.FlagCache<>();
    private final java.util.Map<java.lang.Integer, java.util.Set<android.flags.IFeatureFlagsCallback>> mCallbacks = new java.util.HashMap();
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mDeviceConfigListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.flags.DynamicFlagBinderDelegate.1
        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            java.lang.String ns = properties.getNamespace();
            for (java.lang.String name : properties.getKeyset()) {
                if (com.android.server.flags.DynamicFlagBinderDelegate.this.mDynamicFlags.contains(ns, name) && !com.android.server.flags.DynamicFlagBinderDelegate.this.mFlagStore.contains(ns, name)) {
                    com.android.server.flags.DynamicFlagBinderDelegate.this.mFlagChangeCallback.onFlagChanged(ns, name, properties.getString(name, (java.lang.String) null));
                }
            }
        }
    };
    private final com.android.server.flags.FlagOverrideStore.FlagChangeCallback mFlagChangeCallback = new com.android.server.flags.FlagOverrideStore.FlagChangeCallback() { // from class: com.android.server.flags.DynamicFlagBinderDelegate$$ExternalSyntheticLambda2
        @Override // com.android.server.flags.FlagOverrideStore.FlagChangeCallback
        public final void onFlagChanged(java.lang.String str, java.lang.String str2, java.lang.String str3) {
            this.f$0.lambda$new$2(str, str2, str3);
        }
    };

    static /* synthetic */ java.util.Set lambda$static$0(java.lang.Integer k) {
        return new java.util.HashSet();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(java.lang.String namespace, java.lang.String name, java.lang.String value) {
        com.android.server.flags.DynamicFlagBinderDelegate.DynamicFlagData data;
        java.util.Set<android.flags.IFeatureFlagsCallback> cbCopy;
        if (!this.mDynamicFlags.contains(namespace, name) || (data = this.mDynamicFlags.getOrNull(namespace, name)) == null) {
            return;
        }
        if (value == null) {
            if (data.getValue().equals(data.getDefaultValue())) {
                return;
            } else {
                value = data.getDefaultValue();
            }
        } else if (data.getValue().equals(value)) {
            return;
        }
        data.setValue(value);
        synchronized (this.mCallbacks) {
            cbCopy = new java.util.HashSet<>();
            for (java.lang.Integer pid : this.mCallbacks.keySet()) {
                if (data.containsPid(pid.intValue())) {
                    cbCopy.addAll(this.mCallbacks.get(pid));
                }
            }
        }
        final android.flags.SyncableFlag sFlag = new android.flags.SyncableFlag(namespace, name, value, true);
        cbCopy.forEach(new java.util.function.Consumer() { // from class: com.android.server.flags.DynamicFlagBinderDelegate$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.flags.DynamicFlagBinderDelegate.lambda$new$1(sFlag, (android.flags.IFeatureFlagsCallback) obj);
            }
        });
    }

    static /* synthetic */ void lambda$new$1(android.flags.SyncableFlag sFlag, android.flags.IFeatureFlagsCallback cb) {
        try {
            cb.onFlagChange(sFlag);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w("FeatureFlagsService", "Failed to communicate flag change to client.");
        }
    }

    DynamicFlagBinderDelegate(com.android.server.flags.FlagOverrideStore flagStore) {
        this.mFlagStore = flagStore;
        this.mFlagStore.setChangeCallback(this.mFlagChangeCallback);
    }

    android.flags.SyncableFlag syncDynamicFlag(int pid, android.flags.SyncableFlag sf) {
        if (!sf.isDynamic()) {
            return sf;
        }
        java.lang.String ns = sf.getNamespace();
        java.lang.String name = sf.getName();
        com.android.server.flags.DynamicFlagBinderDelegate.DynamicFlagData data = this.mDynamicFlags.getOrNull(ns, name);
        java.lang.String value = getFlagValue(ns, name, sf.getValue());
        if (!this.mDynamicFlags.containsNamespace(ns)) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener(ns, com.android.internal.os.BackgroundThread.getExecutor(), this.mDeviceConfigListener);
        }
        data.addClientPid(pid);
        data.setValue(value);
        data.setDefaultValue(sf.getValue());
        return new android.flags.SyncableFlag(sf.getNamespace(), sf.getName(), value, true);
    }

    void registerCallback(int pid, android.flags.IFeatureFlagsCallback callback) {
        java.util.Set<android.flags.IFeatureFlagsCallback> callbacks;
        synchronized (this.mCallbacks) {
            callbacks = this.mCallbacks.computeIfAbsent(java.lang.Integer.valueOf(pid), NEW_CALLBACK_SET);
            callbacks.add(callback);
        }
        try {
            callback.asBinder().linkToDeath(new com.android.server.flags.DynamicFlagBinderDelegate.BinderGriever(pid), 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e("FeatureFlagsService", "Failed to link to binder death. Callback not registered.");
            synchronized (this.mCallbacks) {
                callbacks.remove(callback);
            }
        }
    }

    void unregisterCallback(int pid, android.flags.IFeatureFlagsCallback callback) {
        synchronized (this.mCallbacks) {
            java.util.Set<android.flags.IFeatureFlagsCallback> callbacks = this.mCallbacks.computeIfAbsent(java.lang.Integer.valueOf(pid), NEW_CALLBACK_SET);
            callbacks.remove(callback);
        }
    }

    java.lang.String getFlagValue(java.lang.String namespace, java.lang.String name, java.lang.String defaultValue) {
        java.lang.String value = null;
        com.android.server.flags.DynamicFlagBinderDelegate.DynamicFlagData data = this.mDynamicFlags.getOrNull(namespace, name);
        if (data != null) {
            value = data.getValue();
        } else {
            this.mDynamicFlags.setIfChanged(namespace, name, new com.android.server.flags.DynamicFlagBinderDelegate.DynamicFlagData(namespace, name));
        }
        if (!android.os.Build.IS_USER && value == null) {
            value = this.mFlagStore.get(namespace, name);
        }
        if (value == null) {
            java.lang.String value2 = android.provider.DeviceConfig.getString(namespace, name, defaultValue);
            return value2;
        }
        return value;
    }

    private static class DynamicFlagData {
        private java.lang.String mDefaultValue;
        private final java.lang.String mName;
        private final java.lang.String mNamespace;
        private final java.util.Set<java.lang.Integer> mPids;
        private java.lang.String mValue;

        private DynamicFlagData(java.lang.String namespace, java.lang.String name) {
            this.mPids = new java.util.HashSet();
            this.mNamespace = namespace;
            this.mName = name;
        }

        java.lang.String getValue() {
            return this.mValue;
        }

        void setValue(java.lang.String value) {
            this.mValue = value;
        }

        java.lang.String getDefaultValue() {
            return this.mDefaultValue;
        }

        void setDefaultValue(java.lang.String value) {
            this.mDefaultValue = value;
        }

        void addClientPid(int pid) {
            this.mPids.add(java.lang.Integer.valueOf(pid));
        }

        boolean containsPid(int pid) {
            return this.mPids.contains(java.lang.Integer.valueOf(pid));
        }

        public boolean equals(java.lang.Object other) {
            if (other == null || !(other instanceof com.android.server.flags.DynamicFlagBinderDelegate.DynamicFlagData)) {
                return false;
            }
            com.android.server.flags.DynamicFlagBinderDelegate.DynamicFlagData o = (com.android.server.flags.DynamicFlagBinderDelegate.DynamicFlagData) other;
            return this.mName.equals(o.mName) && this.mNamespace.equals(o.mNamespace) && this.mValue.equals(o.mValue) && this.mDefaultValue.equals(o.mDefaultValue);
        }

        public int hashCode() {
            return this.mName.hashCode() + this.mNamespace.hashCode() + this.mValue.hashCode() + this.mDefaultValue.hashCode();
        }
    }

    private class BinderGriever implements android.os.IBinder.DeathRecipient {
        private final int mPid;

        private BinderGriever(int pid) {
            this.mPid = pid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.flags.DynamicFlagBinderDelegate.this.mCallbacks) {
                com.android.server.flags.DynamicFlagBinderDelegate.this.mCallbacks.remove(java.lang.Integer.valueOf(this.mPid));
            }
        }
    }
}
