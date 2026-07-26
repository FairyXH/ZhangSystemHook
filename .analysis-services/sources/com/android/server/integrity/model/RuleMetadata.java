package com.android.server.integrity.model;

/* JADX INFO: loaded from: classes2.dex */
public class RuleMetadata {
    private final java.lang.String mRuleProvider;
    private final java.lang.String mVersion;

    public RuleMetadata(java.lang.String ruleProvider, java.lang.String version) {
        this.mRuleProvider = ruleProvider;
        this.mVersion = version;
    }

    public java.lang.String getRuleProvider() {
        return this.mRuleProvider;
    }

    public java.lang.String getVersion() {
        return this.mVersion;
    }
}
