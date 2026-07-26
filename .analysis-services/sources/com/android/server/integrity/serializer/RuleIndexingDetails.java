package com.android.server.integrity.serializer;

/* JADX INFO: loaded from: classes2.dex */
class RuleIndexingDetails {
    static final int APP_CERTIFICATE_INDEXED = 2;
    static final java.lang.String DEFAULT_RULE_KEY = "N/A";
    static final int NOT_INDEXED = 0;
    static final int PACKAGE_NAME_INDEXED = 1;
    private int mIndexType;
    private java.lang.String mRuleKey;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface IndexType {
    }

    RuleIndexingDetails(int indexType) {
        this.mIndexType = indexType;
        this.mRuleKey = DEFAULT_RULE_KEY;
    }

    RuleIndexingDetails(int indexType, java.lang.String ruleKey) {
        this.mIndexType = indexType;
        this.mRuleKey = ruleKey;
    }

    public int getIndexType() {
        return this.mIndexType;
    }

    public java.lang.String getRuleKey() {
        return this.mRuleKey;
    }
}
