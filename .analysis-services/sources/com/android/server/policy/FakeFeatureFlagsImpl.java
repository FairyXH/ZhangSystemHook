package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public class FakeFeatureFlagsImpl extends com.android.server.policy.CustomFeatureFlags {
    private final com.android.server.policy.FeatureFlags mDefaults;
    private final java.util.Map<java.lang.String, java.lang.Boolean> mFlagMap;

    public FakeFeatureFlagsImpl() {
        this(null);
    }

    public FakeFeatureFlagsImpl(com.android.server.policy.FeatureFlags defaults) {
        super(null);
        this.mFlagMap = new java.util.HashMap();
        this.mDefaults = defaults;
        for (java.lang.String flagName : getFlagNames()) {
            this.mFlagMap.put(flagName, null);
        }
    }

    @Override // com.android.server.policy.CustomFeatureFlags
    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.policy.FeatureFlags> getter) {
        java.lang.Boolean value = this.mFlagMap.get(flagName);
        if (value != null) {
            return value.booleanValue();
        }
        if (this.mDefaults != null) {
            return getter.test(this.mDefaults);
        }
        throw new java.lang.IllegalArgumentException(flagName + " is not set");
    }

    public void setFlag(java.lang.String flagName, boolean value) {
        if (!this.mFlagMap.containsKey(flagName)) {
            throw new java.lang.IllegalArgumentException("no such flag " + flagName);
        }
        this.mFlagMap.put(flagName, java.lang.Boolean.valueOf(value));
    }

    public void resetAll() {
        java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.Boolean>> it = this.mFlagMap.entrySet().iterator();
        while (it.hasNext()) {
            it.next().setValue(null);
        }
    }
}
