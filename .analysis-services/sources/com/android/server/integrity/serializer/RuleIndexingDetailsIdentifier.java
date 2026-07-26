package com.android.server.integrity.serializer;

/* JADX INFO: loaded from: classes2.dex */
class RuleIndexingDetailsIdentifier {
    RuleIndexingDetailsIdentifier() {
    }

    public static java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.util.List<android.content.integrity.Rule>>> splitRulesIntoIndexBuckets(java.util.List<android.content.integrity.Rule> rules) {
        if (rules == null) {
            throw new java.lang.IllegalArgumentException("Index buckets cannot be created for null rule list.");
        }
        java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.util.List<android.content.integrity.Rule>>> typeOrganizedRuleMap = new java.util.HashMap<>();
        typeOrganizedRuleMap.put(0, new java.util.HashMap<>());
        typeOrganizedRuleMap.put(1, new java.util.HashMap<>());
        typeOrganizedRuleMap.put(2, new java.util.HashMap<>());
        for (android.content.integrity.Rule rule : rules) {
            try {
                com.android.server.integrity.serializer.RuleIndexingDetails indexingDetails = getIndexingDetails(rule.getFormula());
                int ruleIndexType = indexingDetails.getIndexType();
                java.lang.String ruleKey = indexingDetails.getRuleKey();
                if (!typeOrganizedRuleMap.get(java.lang.Integer.valueOf(ruleIndexType)).containsKey(ruleKey)) {
                    typeOrganizedRuleMap.get(java.lang.Integer.valueOf(ruleIndexType)).put(ruleKey, new java.util.ArrayList<>());
                }
                typeOrganizedRuleMap.get(java.lang.Integer.valueOf(ruleIndexType)).get(ruleKey).add(rule);
            } catch (java.lang.Exception e) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Malformed rule identified. [%s]", rule.toString()));
            }
        }
        return typeOrganizedRuleMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.android.server.integrity.serializer.RuleIndexingDetails getIndexingDetails(android.content.integrity.IntegrityFormula formula) {
        switch (formula.getTag()) {
            case 0:
                return getIndexingDetailsForCompoundFormula((android.content.integrity.CompoundFormula) formula);
            case 1:
                return getIndexingDetailsForStringAtomicFormula((android.content.integrity.AtomicFormula.StringAtomicFormula) formula);
            case 2:
            case 3:
            case 4:
                return new com.android.server.integrity.serializer.RuleIndexingDetails(0);
            default:
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid formula tag type: %s", java.lang.Integer.valueOf(formula.getTag())));
        }
    }

    private static com.android.server.integrity.serializer.RuleIndexingDetails getIndexingDetailsForCompoundFormula(android.content.integrity.CompoundFormula compoundFormula) {
        int connector = compoundFormula.getConnector();
        java.util.List<android.content.integrity.IntegrityFormula> formulas = compoundFormula.getFormulas();
        switch (connector) {
            case 0:
            case 1:
                java.util.Optional<com.android.server.integrity.serializer.RuleIndexingDetails> packageNameRule = formulas.stream().map(new java.util.function.Function() { // from class: com.android.server.integrity.serializer.RuleIndexingDetailsIdentifier$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.integrity.serializer.RuleIndexingDetailsIdentifier.getIndexingDetails((android.content.integrity.IntegrityFormula) obj);
                    }
                }).filter(new java.util.function.Predicate() { // from class: com.android.server.integrity.serializer.RuleIndexingDetailsIdentifier$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.integrity.serializer.RuleIndexingDetailsIdentifier.lambda$getIndexingDetailsForCompoundFormula$1((com.android.server.integrity.serializer.RuleIndexingDetails) obj);
                    }
                }).findAny();
                if (packageNameRule.isPresent()) {
                    return packageNameRule.get();
                }
                java.util.Optional<com.android.server.integrity.serializer.RuleIndexingDetails> appCertificateRule = formulas.stream().map(new java.util.function.Function() { // from class: com.android.server.integrity.serializer.RuleIndexingDetailsIdentifier$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.integrity.serializer.RuleIndexingDetailsIdentifier.getIndexingDetails((android.content.integrity.IntegrityFormula) obj);
                    }
                }).filter(new java.util.function.Predicate() { // from class: com.android.server.integrity.serializer.RuleIndexingDetailsIdentifier$$ExternalSyntheticLambda3
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.integrity.serializer.RuleIndexingDetailsIdentifier.lambda$getIndexingDetailsForCompoundFormula$3((com.android.server.integrity.serializer.RuleIndexingDetails) obj);
                    }
                }).findAny();
                if (appCertificateRule.isPresent()) {
                    return appCertificateRule.get();
                }
                return new com.android.server.integrity.serializer.RuleIndexingDetails(0);
            default:
                return new com.android.server.integrity.serializer.RuleIndexingDetails(0);
        }
    }

    static /* synthetic */ boolean lambda$getIndexingDetailsForCompoundFormula$1(com.android.server.integrity.serializer.RuleIndexingDetails ruleIndexingDetails) {
        return ruleIndexingDetails.getIndexType() == 1;
    }

    static /* synthetic */ boolean lambda$getIndexingDetailsForCompoundFormula$3(com.android.server.integrity.serializer.RuleIndexingDetails ruleIndexingDetails) {
        return ruleIndexingDetails.getIndexType() == 2;
    }

    private static com.android.server.integrity.serializer.RuleIndexingDetails getIndexingDetailsForStringAtomicFormula(android.content.integrity.AtomicFormula.StringAtomicFormula atomicFormula) {
        switch (atomicFormula.getKey()) {
            case 0:
                return new com.android.server.integrity.serializer.RuleIndexingDetails(1, atomicFormula.getValue());
            case 1:
                return new com.android.server.integrity.serializer.RuleIndexingDetails(2, atomicFormula.getValue());
            default:
                return new com.android.server.integrity.serializer.RuleIndexingDetails(0);
        }
    }
}
