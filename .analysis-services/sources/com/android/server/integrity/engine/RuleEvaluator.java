package com.android.server.integrity.engine;

/* JADX INFO: loaded from: classes2.dex */
final class RuleEvaluator {
    RuleEvaluator() {
    }

    static com.android.server.integrity.model.IntegrityCheckResult evaluateRules(java.util.List<android.content.integrity.Rule> rules, final android.content.integrity.AppInstallMetadata appInstallMetadata) {
        java.util.List<android.content.integrity.Rule> matchedRules = (java.util.List) rules.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.integrity.engine.RuleEvaluator$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.content.integrity.Rule) obj).getFormula().matches(appInstallMetadata);
            }
        }).collect(java.util.stream.Collectors.toList());
        java.util.List<android.content.integrity.Rule> matchedPowerAllowRules = (java.util.List) matchedRules.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.integrity.engine.RuleEvaluator$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.integrity.engine.RuleEvaluator.lambda$evaluateRules$1((android.content.integrity.Rule) obj);
            }
        }).collect(java.util.stream.Collectors.toList());
        if (!matchedPowerAllowRules.isEmpty()) {
            return com.android.server.integrity.model.IntegrityCheckResult.allow(matchedPowerAllowRules);
        }
        java.util.List<android.content.integrity.Rule> matchedDenyRules = (java.util.List) matchedRules.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.integrity.engine.RuleEvaluator$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.integrity.engine.RuleEvaluator.lambda$evaluateRules$2((android.content.integrity.Rule) obj);
            }
        }).collect(java.util.stream.Collectors.toList());
        if (!matchedDenyRules.isEmpty()) {
            return com.android.server.integrity.model.IntegrityCheckResult.deny(matchedDenyRules);
        }
        return com.android.server.integrity.model.IntegrityCheckResult.allow();
    }

    static /* synthetic */ boolean lambda$evaluateRules$1(android.content.integrity.Rule rule) {
        return rule.getEffect() == 1;
    }

    static /* synthetic */ boolean lambda$evaluateRules$2(android.content.integrity.Rule rule) {
        return rule.getEffect() == 0;
    }
}
