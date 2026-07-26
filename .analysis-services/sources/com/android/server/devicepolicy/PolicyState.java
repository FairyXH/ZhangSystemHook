package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class PolicyState<V> {
    private static final java.lang.String TAG = "PolicyState";
    private static final java.lang.String TAG_ADMIN_POLICY_ENTRY = "admin-policy-entry";
    private static final java.lang.String TAG_ENFORCING_ADMIN_ENTRY = "enforcing-admin-entry";
    private static final java.lang.String TAG_POLICY_DEFINITION_ENTRY = "policy-definition-entry";
    private static final java.lang.String TAG_POLICY_VALUE_ENTRY = "policy-value-entry";
    private static final java.lang.String TAG_RESOLVED_VALUE_ENTRY = "resolved-value-entry";
    private android.app.admin.PolicyValue<V> mCurrentResolvedPolicy;
    private final java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> mPoliciesSetByAdmins = new java.util.LinkedHashMap<>();
    private final com.android.server.devicepolicy.PolicyDefinition<V> mPolicyDefinition;

    PolicyState(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition) {
        this.mPolicyDefinition = (com.android.server.devicepolicy.PolicyDefinition) java.util.Objects.requireNonNull(policyDefinition);
    }

    private PolicyState(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> policiesSetByAdmins, android.app.admin.PolicyValue<V> currentEnforcedPolicy) {
        java.util.Objects.requireNonNull(policyDefinition);
        java.util.Objects.requireNonNull(policiesSetByAdmins);
        this.mPolicyDefinition = policyDefinition;
        this.mPoliciesSetByAdmins.putAll(policiesSetByAdmins);
        this.mCurrentResolvedPolicy = currentEnforcedPolicy;
    }

    boolean addPolicy(com.android.server.devicepolicy.EnforcingAdmin admin, android.app.admin.PolicyValue<V> policy) {
        java.util.Objects.requireNonNull(admin);
        this.mPoliciesSetByAdmins.remove(admin);
        this.mPoliciesSetByAdmins.put(admin, policy);
        return resolvePolicy();
    }

    boolean addPolicy(com.android.server.devicepolicy.EnforcingAdmin admin, android.app.admin.PolicyValue<V> policy, java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> globalPoliciesSetByAdmins) {
        this.mPoliciesSetByAdmins.put((com.android.server.devicepolicy.EnforcingAdmin) java.util.Objects.requireNonNull(admin), policy);
        return resolvePolicy(globalPoliciesSetByAdmins);
    }

    boolean removePolicy(com.android.server.devicepolicy.EnforcingAdmin admin) {
        java.util.Objects.requireNonNull(admin);
        if (this.mPoliciesSetByAdmins.remove(admin) == null) {
            return false;
        }
        return resolvePolicy();
    }

    boolean removePolicy(com.android.server.devicepolicy.EnforcingAdmin admin, java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> globalPoliciesSetByAdmins) {
        java.util.Objects.requireNonNull(admin);
        if (this.mPoliciesSetByAdmins.remove(admin) == null) {
            return false;
        }
        return resolvePolicy(globalPoliciesSetByAdmins);
    }

    boolean resolvePolicy(java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> globalPoliciesSetByAdmins) {
        if (this.mPolicyDefinition.isNonCoexistablePolicy()) {
            return false;
        }
        java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> mergedPolicies = new java.util.LinkedHashMap<>(globalPoliciesSetByAdmins);
        mergedPolicies.putAll(this.mPoliciesSetByAdmins);
        android.app.admin.PolicyValue<V> resolvedPolicy = this.mPolicyDefinition.resolvePolicy(mergedPolicies);
        boolean policyChanged = !java.util.Objects.equals(resolvedPolicy, this.mCurrentResolvedPolicy);
        this.mCurrentResolvedPolicy = resolvedPolicy;
        return policyChanged;
    }

    java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> getPoliciesSetByAdmins() {
        return new java.util.LinkedHashMap<>(this.mPoliciesSetByAdmins);
    }

    private boolean resolvePolicy() {
        if (this.mPolicyDefinition.isNonCoexistablePolicy()) {
            return false;
        }
        android.app.admin.PolicyValue<V> resolvedPolicy = this.mPolicyDefinition.resolvePolicy(this.mPoliciesSetByAdmins);
        boolean policyChanged = !java.util.Objects.equals(resolvedPolicy, this.mCurrentResolvedPolicy);
        this.mCurrentResolvedPolicy = resolvedPolicy;
        return policyChanged;
    }

    android.app.admin.PolicyValue<V> getCurrentResolvedPolicy() {
        return this.mCurrentResolvedPolicy;
    }

    android.app.admin.PolicyState<V> getParcelablePolicyState() {
        java.util.LinkedHashMap<android.app.admin.EnforcingAdmin, android.app.admin.PolicyValue<V>> adminPolicies = new java.util.LinkedHashMap<>();
        for (com.android.server.devicepolicy.EnforcingAdmin admin : this.mPoliciesSetByAdmins.keySet()) {
            adminPolicies.put(admin.getParcelableAdmin(), this.mPoliciesSetByAdmins.get(admin));
        }
        return new android.app.admin.PolicyState<>(adminPolicies, this.mCurrentResolvedPolicy, this.mPolicyDefinition.getResolutionMechanism().mo3274getParcelableResolutionMechanism());
    }

    public java.lang.String toString() {
        return "\nPolicyKey - " + this.mPolicyDefinition.getPolicyKey() + "\nmPolicyDefinition= \n\t" + this.mPolicyDefinition + "\nmPoliciesSetByAdmins= \n\t" + this.mPoliciesSetByAdmins + ",\nmCurrentResolvedPolicy= \n\t" + this.mCurrentResolvedPolicy + " }";
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        pw.println(this.mPolicyDefinition.getPolicyKey());
        pw.increaseIndent();
        pw.println("Per-admin Policy:");
        pw.increaseIndent();
        if (this.mPoliciesSetByAdmins.size() == 0) {
            pw.println("null");
        } else {
            for (com.android.server.devicepolicy.EnforcingAdmin admin : this.mPoliciesSetByAdmins.keySet()) {
                pw.println(admin);
                pw.increaseIndent();
                pw.println(this.mPoliciesSetByAdmins.get(admin));
                pw.decreaseIndent();
            }
        }
        pw.decreaseIndent();
        pw.printf("Resolved Policy (%s):\n", new java.lang.Object[]{this.mPolicyDefinition.getResolutionMechanism().getClass().getSimpleName()});
        pw.increaseIndent();
        pw.println(this.mCurrentResolvedPolicy);
        pw.decreaseIndent();
        pw.decreaseIndent();
    }

    void saveToXml(com.android.modules.utils.TypedXmlSerializer typedXmlSerializer) throws java.io.IOException {
        typedXmlSerializer.startTag((java.lang.String) null, TAG_POLICY_DEFINITION_ENTRY);
        this.mPolicyDefinition.saveToXml(typedXmlSerializer);
        typedXmlSerializer.endTag((java.lang.String) null, TAG_POLICY_DEFINITION_ENTRY);
        if (this.mCurrentResolvedPolicy != null) {
            typedXmlSerializer.startTag((java.lang.String) null, TAG_RESOLVED_VALUE_ENTRY);
            this.mPolicyDefinition.savePolicyValueToXml(typedXmlSerializer, (V) this.mCurrentResolvedPolicy.getValue());
            typedXmlSerializer.endTag((java.lang.String) null, TAG_RESOLVED_VALUE_ENTRY);
        }
        for (com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin : this.mPoliciesSetByAdmins.keySet()) {
            typedXmlSerializer.startTag((java.lang.String) null, TAG_ADMIN_POLICY_ENTRY);
            if (this.mPoliciesSetByAdmins.get(enforcingAdmin) != null) {
                typedXmlSerializer.startTag((java.lang.String) null, TAG_POLICY_VALUE_ENTRY);
                this.mPolicyDefinition.savePolicyValueToXml(typedXmlSerializer, (V) this.mPoliciesSetByAdmins.get(enforcingAdmin).getValue());
                typedXmlSerializer.endTag((java.lang.String) null, TAG_POLICY_VALUE_ENTRY);
            }
            typedXmlSerializer.startTag((java.lang.String) null, TAG_ENFORCING_ADMIN_ENTRY);
            enforcingAdmin.saveToXml(typedXmlSerializer);
            typedXmlSerializer.endTag((java.lang.String) null, TAG_ENFORCING_ADMIN_ENTRY);
            typedXmlSerializer.endTag((java.lang.String) null, TAG_ADMIN_POLICY_ENTRY);
        }
    }

    static <V> com.android.server.devicepolicy.PolicyState<V> readFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        byte b;
        byte b2;
        com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition = null;
        android.app.admin.PolicyValue<V> currentResolvedPolicy = null;
        java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> policiesSetByAdmins = new java.util.LinkedHashMap<>();
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            java.lang.String tag = parser.getName();
            switch (tag.hashCode()) {
                case 394982067:
                    b = !tag.equals(TAG_POLICY_DEFINITION_ENTRY) ? (byte) -1 : (byte) 1;
                    break;
                case 695389653:
                    b = !tag.equals(TAG_ADMIN_POLICY_ENTRY) ? (byte) -1 : (byte) 0;
                    break;
                case 829992641:
                    b = !tag.equals(TAG_RESOLVED_VALUE_ENTRY) ? (byte) -1 : (byte) 2;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    android.app.admin.PolicyValue<V> value = null;
                    com.android.server.devicepolicy.EnforcingAdmin admin = null;
                    int adminPolicyDepth = parser.getDepth();
                    while (com.android.internal.util.XmlUtils.nextElementWithin(parser, adminPolicyDepth)) {
                        java.lang.String adminPolicyTag = parser.getName();
                        switch (adminPolicyTag.hashCode()) {
                            case -1365069882:
                                b2 = !adminPolicyTag.equals(TAG_ENFORCING_ADMIN_ENTRY) ? (byte) -1 : (byte) 0;
                                break;
                            case -698838789:
                                b2 = !adminPolicyTag.equals(TAG_POLICY_VALUE_ENTRY) ? (byte) -1 : (byte) 1;
                                break;
                            default:
                                b2 = -1;
                                break;
                        }
                        switch (b2) {
                            case 0:
                                com.android.server.devicepolicy.EnforcingAdmin admin2 = com.android.server.devicepolicy.EnforcingAdmin.readFromXml(parser);
                                if (admin2 == null) {
                                    com.android.server.utils.Slogf.wtf(TAG, "Error Parsing TAG_ENFORCING_ADMIN_ENTRY, EnforcingAdmin is null");
                                }
                                admin = admin2;
                                break;
                            case 1:
                                value = policyDefinition.readPolicyValueFromXml(parser);
                                if (value == null) {
                                    com.android.server.utils.Slogf.wtf(TAG, "Error Parsing TAG_POLICY_VALUE_ENTRY, PolicyValue is null");
                                }
                                break;
                        }
                    }
                    if (admin != null && value != null) {
                        policiesSetByAdmins.put(admin, value);
                    } else {
                        com.android.server.utils.Slogf.wtf(TAG, "Error Parsing TAG_ADMIN_POLICY_ENTRY for " + (policyDefinition != null ? "policy with definition " + policyDefinition : "unknown policy") + ", EnforcingAdmin is: " + (admin == null ? "null" : admin) + ", value is : " + (value != null ? value : "null"));
                    }
                    break;
                case 1:
                    policyDefinition = com.android.server.devicepolicy.PolicyDefinition.readFromXml(parser);
                    if (policyDefinition == null) {
                        com.android.server.utils.Slogf.wtf(TAG, "Error Parsing TAG_POLICY_DEFINITION_ENTRY, PolicyDefinition is null");
                    }
                    break;
                case 2:
                    if (policyDefinition == null) {
                        com.android.server.utils.Slogf.wtf(TAG, "Error Parsing TAG_RESOLVED_VALUE_ENTRY, policyDefinition is null");
                    } else {
                        currentResolvedPolicy = policyDefinition.readPolicyValueFromXml(parser);
                        if (currentResolvedPolicy == null) {
                            com.android.server.utils.Slogf.wtf(TAG, "Error Parsing TAG_RESOLVED_VALUE_ENTRY for " + (policyDefinition != null ? "policy with definition " + policyDefinition : "unknown policy") + ", currentResolvedPolicy is null");
                        }
                    }
                    break;
                default:
                    com.android.server.utils.Slogf.wtf(TAG, "Unknown tag: " + tag);
                    break;
            }
        }
        if (policyDefinition != null) {
            return new com.android.server.devicepolicy.PolicyState<>(policyDefinition, policiesSetByAdmins, currentResolvedPolicy);
        }
        com.android.server.utils.Slogf.wtf(TAG, "Error parsing policyState, policyDefinition is null");
        return null;
    }

    com.android.server.devicepolicy.PolicyDefinition<V> getPolicyDefinition() {
        return this.mPolicyDefinition;
    }
}
