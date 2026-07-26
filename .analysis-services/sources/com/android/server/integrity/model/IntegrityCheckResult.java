package com.android.server.integrity.model;

/* JADX INFO: loaded from: classes2.dex */
public final class IntegrityCheckResult {
    private final com.android.server.integrity.model.IntegrityCheckResult.Effect mEffect;
    private final java.util.List<android.content.integrity.Rule> mRuleList;

    public enum Effect {
        ALLOW,
        DENY
    }

    private IntegrityCheckResult(com.android.server.integrity.model.IntegrityCheckResult.Effect effect, java.util.List<android.content.integrity.Rule> ruleList) {
        this.mEffect = effect;
        this.mRuleList = ruleList;
    }

    public com.android.server.integrity.model.IntegrityCheckResult.Effect getEffect() {
        return this.mEffect;
    }

    public java.util.List<android.content.integrity.Rule> getMatchedRules() {
        return this.mRuleList;
    }

    public static com.android.server.integrity.model.IntegrityCheckResult allow() {
        return new com.android.server.integrity.model.IntegrityCheckResult(com.android.server.integrity.model.IntegrityCheckResult.Effect.ALLOW, java.util.Collections.emptyList());
    }

    public static com.android.server.integrity.model.IntegrityCheckResult allow(java.util.List<android.content.integrity.Rule> ruleList) {
        return new com.android.server.integrity.model.IntegrityCheckResult(com.android.server.integrity.model.IntegrityCheckResult.Effect.ALLOW, ruleList);
    }

    public static com.android.server.integrity.model.IntegrityCheckResult deny(java.util.List<android.content.integrity.Rule> ruleList) {
        return new com.android.server.integrity.model.IntegrityCheckResult(com.android.server.integrity.model.IntegrityCheckResult.Effect.DENY, ruleList);
    }

    public int getLoggingResponse() {
        if (getEffect() == com.android.server.integrity.model.IntegrityCheckResult.Effect.DENY) {
            return 2;
        }
        if (getEffect() == com.android.server.integrity.model.IntegrityCheckResult.Effect.ALLOW && getMatchedRules().isEmpty()) {
            return 1;
        }
        if (getEffect() == com.android.server.integrity.model.IntegrityCheckResult.Effect.ALLOW && !getMatchedRules().isEmpty()) {
            return 3;
        }
        throw new java.lang.IllegalStateException("IntegrityCheckResult is not valid.");
    }

    public boolean isCausedByAppCertRule() {
        return this.mRuleList.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.integrity.model.IntegrityCheckResult$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.content.integrity.Rule) obj).getFormula().isAppCertificateFormula();
            }
        });
    }

    public boolean isCausedByInstallerRule() {
        return this.mRuleList.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.integrity.model.IntegrityCheckResult$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.content.integrity.Rule) obj).getFormula().isInstallerFormula();
            }
        });
    }
}
