package com.android.server.compat;

/* JADX INFO: loaded from: classes.dex */
public final class CompatChange extends com.android.internal.compat.CompatibilityChangeInfo {
    static final long CTS_SYSTEM_API_CHANGEID = 149391281;
    static final long CTS_SYSTEM_API_OVERRIDABLE_CHANGEID = 174043039;
    private java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Boolean> mEvaluatedOverrides;
    com.android.server.compat.CompatChange.ChangeListener mListener;
    private java.util.concurrent.ConcurrentHashMap<java.lang.String, android.app.compat.PackageOverride> mRawOverrides;

    public interface ChangeListener {
        void onCompatChange(java.lang.String str);
    }

    public CompatChange(long changeId) {
        this(changeId, null, -1, -1, false, false, null, false);
    }

    public CompatChange(com.android.server.compat.config.Change change) {
        this(change.getId(), change.getName(), change.getEnableAfterTargetSdk(), change.getEnableSinceTargetSdk(), change.getDisabled(), change.getLoggingOnly(), change.getDescription(), change.getOverridable());
    }

    public CompatChange(long changeId, java.lang.String name, int enableAfterTargetSdk, int enableSinceTargetSdk, boolean disabled, boolean loggingOnly, java.lang.String description, boolean overridable) {
        super(java.lang.Long.valueOf(changeId), name, enableAfterTargetSdk, enableSinceTargetSdk, disabled, loggingOnly, description, overridable);
        this.mListener = null;
        this.mEvaluatedOverrides = new java.util.concurrent.ConcurrentHashMap<>();
        this.mRawOverrides = new java.util.concurrent.ConcurrentHashMap<>();
    }

    synchronized void registerListener(com.android.server.compat.CompatChange.ChangeListener listener) {
        if (this.mListener != null) {
            throw new java.lang.IllegalStateException("Listener for change " + toString() + " already registered.");
        }
        this.mListener = listener;
    }

    private void addPackageOverrideInternal(java.lang.String pname, boolean enabled) {
        if (getLoggingOnly()) {
            throw new java.lang.IllegalArgumentException("Can't add overrides for a logging only change " + toString());
        }
        this.mEvaluatedOverrides.put(pname, java.lang.Boolean.valueOf(enabled));
        notifyListener(pname);
    }

    private void removePackageOverrideInternal(java.lang.String pname) {
        if (this.mEvaluatedOverrides.remove(pname) != null) {
            notifyListener(pname);
        }
    }

    synchronized void addPackageOverride(java.lang.String packageName, android.app.compat.PackageOverride override, com.android.internal.compat.OverrideAllowedState allowedState, java.lang.Long versionCode) {
        if (getLoggingOnly()) {
            throw new java.lang.IllegalArgumentException("Can't add overrides for a logging only change " + toString());
        }
        this.mRawOverrides.put(packageName, override);
        recheckOverride(packageName, allowedState, versionCode);
    }

    synchronized boolean recheckOverride(java.lang.String packageName, com.android.internal.compat.OverrideAllowedState allowedState, java.lang.Long versionCode) {
        if (packageName == null) {
            return false;
        }
        boolean allowed = allowedState.state == 0;
        if (versionCode != null && this.mRawOverrides.containsKey(packageName) && allowed) {
            int overrideValue = this.mRawOverrides.get(packageName).evaluate(versionCode.longValue());
            switch (overrideValue) {
                case 0:
                    removePackageOverrideInternal(packageName);
                    break;
                case 1:
                    addPackageOverrideInternal(packageName, true);
                    break;
                case 2:
                    addPackageOverrideInternal(packageName, false);
                    break;
            }
            return true;
        }
        removePackageOverrideInternal(packageName);
        return false;
    }

    synchronized boolean removePackageOverride(java.lang.String pname, com.android.internal.compat.OverrideAllowedState allowedState, java.lang.Long versionCode) {
        if (!this.mRawOverrides.containsKey(pname)) {
            return false;
        }
        allowedState.enforce(getId(), pname);
        this.mRawOverrides.remove(pname);
        recheckOverride(pname, allowedState, versionCode);
        return true;
    }

    boolean isEnabled(android.content.pm.ApplicationInfo app, com.android.internal.compat.AndroidBuildClassifier buildClassifier) {
        java.lang.Boolean enabled;
        if (app == null) {
            return defaultValue();
        }
        if (app.packageName != null && (enabled = this.mEvaluatedOverrides.get(app.packageName)) != null) {
            return enabled.booleanValue();
        }
        if (getDisabled()) {
            return false;
        }
        if (getEnableSinceTargetSdk() == -1) {
            return true;
        }
        int compareSdk = java.lang.Math.min(app.targetSdkVersion, buildClassifier.platformTargetSdk());
        return compareSdk >= getEnableSinceTargetSdk();
    }

    boolean willBeEnabled(java.lang.String packageName) {
        if (packageName == null) {
            return defaultValue();
        }
        android.app.compat.PackageOverride override = this.mRawOverrides.get(packageName);
        if (override != null) {
            switch (override.evaluateForAllVersions()) {
            }
            return defaultValue();
        }
        return defaultValue();
    }

    boolean defaultValue() {
        return !getDisabled();
    }

    synchronized void clearOverrides() {
        this.mRawOverrides.clear();
        this.mEvaluatedOverrides.clear();
    }

    synchronized void loadOverrides(com.android.server.compat.overrides.ChangeOverrides changeOverrides) {
        if (changeOverrides.getDeferred() != null) {
            for (com.android.server.compat.overrides.OverrideValue override : changeOverrides.getDeferred().getOverrideValue()) {
                this.mRawOverrides.put(override.getPackageName(), new android.app.compat.PackageOverride.Builder().setEnabled(override.getEnabled()).build());
            }
        }
        if (changeOverrides.getValidated() != null) {
            for (com.android.server.compat.overrides.OverrideValue override2 : changeOverrides.getValidated().getOverrideValue()) {
                this.mEvaluatedOverrides.put(override2.getPackageName(), java.lang.Boolean.valueOf(override2.getEnabled()));
                this.mRawOverrides.put(override2.getPackageName(), new android.app.compat.PackageOverride.Builder().setEnabled(override2.getEnabled()).build());
            }
        }
        if (changeOverrides.getRaw() != null) {
            for (com.android.server.compat.overrides.RawOverrideValue override3 : changeOverrides.getRaw().getRawOverrideValue()) {
                android.app.compat.PackageOverride packageOverride = new android.app.compat.PackageOverride.Builder().setMinVersionCode(override3.getMinVersionCode()).setMaxVersionCode(override3.getMaxVersionCode()).setEnabled(override3.getEnabled()).build();
                this.mRawOverrides.put(override3.getPackageName(), packageOverride);
            }
        }
    }

    synchronized com.android.server.compat.overrides.ChangeOverrides saveOverrides() {
        if (this.mRawOverrides.isEmpty()) {
            return null;
        }
        com.android.server.compat.overrides.ChangeOverrides changeOverrides = new com.android.server.compat.overrides.ChangeOverrides();
        changeOverrides.setChangeId(getId());
        com.android.server.compat.overrides.ChangeOverrides.Raw rawOverrides = new com.android.server.compat.overrides.ChangeOverrides.Raw();
        java.util.List<com.android.server.compat.overrides.RawOverrideValue> rawList = rawOverrides.getRawOverrideValue();
        for (java.util.Map.Entry<java.lang.String, android.app.compat.PackageOverride> entry : this.mRawOverrides.entrySet()) {
            com.android.server.compat.overrides.RawOverrideValue override = new com.android.server.compat.overrides.RawOverrideValue();
            override.setPackageName(entry.getKey());
            override.setMinVersionCode(entry.getValue().getMinVersionCode());
            override.setMaxVersionCode(entry.getValue().getMaxVersionCode());
            override.setEnabled(entry.getValue().isEnabled());
            rawList.add(override);
        }
        changeOverrides.setRaw(rawOverrides);
        com.android.server.compat.overrides.ChangeOverrides.Validated validatedOverrides = new com.android.server.compat.overrides.ChangeOverrides.Validated();
        java.util.List<com.android.server.compat.overrides.OverrideValue> validatedList = validatedOverrides.getOverrideValue();
        for (java.util.Map.Entry<java.lang.String, java.lang.Boolean> entry2 : this.mEvaluatedOverrides.entrySet()) {
            com.android.server.compat.overrides.OverrideValue override2 = new com.android.server.compat.overrides.OverrideValue();
            override2.setPackageName(entry2.getKey());
            override2.setEnabled(entry2.getValue().booleanValue());
            validatedList.add(override2);
        }
        changeOverrides.setValidated(validatedOverrides);
        return changeOverrides;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("ChangeId(").append(getId());
        if (getName() != null) {
            sb.append("; name=").append(getName());
        }
        if (getEnableSinceTargetSdk() != -1) {
            sb.append("; enableSinceTargetSdk=").append(getEnableSinceTargetSdk());
        }
        if (getDisabled()) {
            sb.append("; disabled");
        }
        if (getLoggingOnly()) {
            sb.append("; loggingOnly");
        }
        if (!this.mEvaluatedOverrides.isEmpty()) {
            sb.append("; packageOverrides=").append(this.mEvaluatedOverrides);
        }
        if (!this.mRawOverrides.isEmpty()) {
            sb.append("; rawOverrides=").append(this.mRawOverrides);
        }
        if (getOverridable()) {
            sb.append("; overridable");
        }
        return sb.append(")").toString();
    }

    private synchronized void notifyListener(java.lang.String packageName) {
        if (this.mListener != null) {
            this.mListener.onCompatChange(packageName);
        }
    }
}
