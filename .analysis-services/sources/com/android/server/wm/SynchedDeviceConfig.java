package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class SynchedDeviceConfig implements android.provider.DeviceConfig.OnPropertiesChangedListener {
    private final java.util.Map<java.lang.String, com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigEntry> mDeviceConfigEntries;
    private final java.util.concurrent.Executor mExecutor;
    private final java.lang.String mNamespace;

    static com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigBuilder builder(java.lang.String namespace, java.util.concurrent.Executor executor) {
        return new com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigBuilder(namespace, executor);
    }

    private SynchedDeviceConfig(java.lang.String namespace, java.util.concurrent.Executor executor, java.util.Map<java.lang.String, com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigEntry> deviceConfigEntries) {
        this.mNamespace = namespace;
        this.mExecutor = executor;
        this.mDeviceConfigEntries = deviceConfigEntries;
    }

    public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
        for (com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigEntry entry : this.mDeviceConfigEntries.values()) {
            if (properties.getKeyset().contains(entry.mFlagKey)) {
                entry.updateValue(properties.getBoolean(entry.mFlagKey, entry.mDefaultValue));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.wm.SynchedDeviceConfig start() {
        android.provider.DeviceConfig.addOnPropertiesChangedListener(this.mNamespace, this.mExecutor, this);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFlags$0(java.lang.String key, com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigEntry entry) {
        entry.updateValue(isDeviceConfigFlagEnabled(key, entry.mDefaultValue));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.wm.SynchedDeviceConfig updateFlags() {
        this.mDeviceConfigEntries.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.wm.SynchedDeviceConfig$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$updateFlags$0((java.lang.String) obj, (com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigEntry) obj2);
            }
        });
        return this;
    }

    boolean getFlagValue(java.lang.String key) {
        com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigEntry entry = this.mDeviceConfigEntries.get(key);
        if (entry == null) {
            throw new java.lang.IllegalArgumentException("Unexpected flag name: " + key);
        }
        return entry.getValue();
    }

    boolean isBuildTimeFlagEnabled(java.lang.String key) {
        com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigEntry entry = this.mDeviceConfigEntries.get(key);
        if (entry == null) {
            throw new java.lang.IllegalArgumentException("Unexpected flag name: " + key);
        }
        return entry.isBuildTimeFlagEnabled();
    }

    private boolean isDeviceConfigFlagEnabled(java.lang.String key, boolean defaultValue) {
        return android.provider.DeviceConfig.getBoolean(this.mNamespace, key, defaultValue);
    }

    static class SynchedDeviceConfigBuilder {
        private final java.util.Map<java.lang.String, com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigEntry> mDeviceConfigEntries;
        private final java.util.concurrent.Executor mExecutor;
        private final java.lang.String mNamespace;

        private SynchedDeviceConfigBuilder(java.lang.String namespace, java.util.concurrent.Executor executor) {
            this.mDeviceConfigEntries = new java.util.concurrent.ConcurrentHashMap();
            this.mNamespace = namespace;
            this.mExecutor = executor;
        }

        com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigBuilder addDeviceConfigEntry(java.lang.String key, boolean defaultValue, boolean enabled) {
            if (this.mDeviceConfigEntries.containsKey(key)) {
                throw new java.lang.AssertionError("Key already present: " + key);
            }
            this.mDeviceConfigEntries.put(key, new com.android.server.wm.SynchedDeviceConfig.SynchedDeviceConfigEntry(key, defaultValue, enabled));
            return this;
        }

        com.android.server.wm.SynchedDeviceConfig build() {
            return new com.android.server.wm.SynchedDeviceConfig(this.mNamespace, this.mExecutor, this.mDeviceConfigEntries).updateFlags().start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class SynchedDeviceConfigEntry {
        private final boolean mBuildTimeFlagEnabled;
        private final boolean mDefaultValue;
        private final java.lang.String mFlagKey;
        private volatile boolean mOverrideValue;

        private SynchedDeviceConfigEntry(java.lang.String flagKey, boolean defaultValue, boolean enabled) {
            this.mFlagKey = flagKey;
            this.mDefaultValue = defaultValue;
            this.mOverrideValue = defaultValue;
            this.mBuildTimeFlagEnabled = enabled;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateValue(boolean newValue) {
            this.mOverrideValue = newValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean getValue() {
            return this.mBuildTimeFlagEnabled && this.mOverrideValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isBuildTimeFlagEnabled() {
            return this.mBuildTimeFlagEnabled;
        }
    }
}
