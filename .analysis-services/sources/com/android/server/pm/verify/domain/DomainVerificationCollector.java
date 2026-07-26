package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationCollector {
    private static final int MAX_DOMAINS_BYTE_SIZE = 1048576;
    public static final long RESTRICT_DOMAINS = 175408749;
    private final java.util.regex.Matcher mDomainMatcher = DOMAIN_NAME_WITH_WILDCARD.matcher("");
    private final com.android.server.compat.PlatformCompat mPlatformCompat;
    private final com.android.server.SystemConfig mSystemConfig;
    private static final java.util.regex.Pattern DOMAIN_NAME_WITH_WILDCARD = java.util.regex.Pattern.compile("(\\*\\.)?" + android.util.Patterns.DOMAIN_NAME.pattern());
    private static final java.util.function.BiFunction<android.util.ArraySet<java.lang.String>, java.lang.String, java.lang.Boolean> ARRAY_SET_COLLECTOR = new java.util.function.BiFunction() { // from class: com.android.server.pm.verify.domain.DomainVerificationCollector$$ExternalSyntheticLambda0
        @Override // java.util.function.BiFunction
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
            return com.android.server.pm.verify.domain.DomainVerificationCollector.lambda$static$0((android.util.ArraySet) obj, (java.lang.String) obj2);
        }
    };

    static /* synthetic */ java.lang.Boolean lambda$static$0(android.util.ArraySet set, java.lang.String domain) {
        set.add(domain);
        return null;
    }

    public DomainVerificationCollector(com.android.server.compat.PlatformCompat platformCompat, com.android.server.SystemConfig systemConfig) {
        this.mPlatformCompat = platformCompat;
        this.mSystemConfig = systemConfig;
    }

    public android.util.ArraySet<java.lang.String> collectAllWebDomains(com.android.server.pm.pkg.AndroidPackage pkg) {
        return collectDomains(pkg, false, true);
    }

    public android.util.ArraySet<java.lang.String> collectValidAutoVerifyDomains(com.android.server.pm.pkg.AndroidPackage pkg) {
        return collectDomains(pkg, true, true);
    }

    public android.util.ArraySet<java.lang.String> collectInvalidAutoVerifyDomains(com.android.server.pm.pkg.AndroidPackage pkg) {
        return collectDomains(pkg, true, false);
    }

    public boolean containsWebDomain(com.android.server.pm.pkg.AndroidPackage pkg, final java.lang.String targetDomain) {
        return collectDomains(pkg, false, true, null, new java.util.function.BiFunction() { // from class: com.android.server.pm.verify.domain.DomainVerificationCollector$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.pm.verify.domain.DomainVerificationCollector.lambda$containsWebDomain$1(targetDomain, (java.lang.Void) obj, (java.lang.String) obj2);
            }
        }) != null;
    }

    static /* synthetic */ java.lang.Boolean lambda$containsWebDomain$1(java.lang.String targetDomain, java.lang.Void unused, java.lang.String domain) {
        if (java.util.Objects.equals(targetDomain, domain)) {
            return true;
        }
        return null;
    }

    public boolean containsAutoVerifyDomain(com.android.server.pm.pkg.AndroidPackage pkg, final java.lang.String targetDomain) {
        return collectDomains(pkg, true, true, null, new java.util.function.BiFunction() { // from class: com.android.server.pm.verify.domain.DomainVerificationCollector$$ExternalSyntheticLambda2
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.pm.verify.domain.DomainVerificationCollector.lambda$containsAutoVerifyDomain$2(targetDomain, (java.lang.Void) obj, (java.lang.String) obj2);
            }
        }) != null;
    }

    static /* synthetic */ java.lang.Boolean lambda$containsAutoVerifyDomain$2(java.lang.String targetDomain, java.lang.Void unused, java.lang.String domain) {
        if (java.util.Objects.equals(targetDomain, domain)) {
            return true;
        }
        return null;
    }

    private android.util.ArraySet<java.lang.String> collectDomains(com.android.server.pm.pkg.AndroidPackage pkg, boolean checkAutoVerify, boolean valid) {
        android.util.ArraySet<java.lang.String> domains = new android.util.ArraySet<>();
        collectDomains(pkg, checkAutoVerify, valid, domains, ARRAY_SET_COLLECTOR);
        return domains;
    }

    private <InitialValue, ReturnValue> ReturnValue collectDomains(com.android.server.pm.pkg.AndroidPackage androidPackage, boolean z, boolean z2, InitialValue initialvalue, java.util.function.BiFunction<InitialValue, java.lang.String, ReturnValue> biFunction) {
        if (com.android.server.pm.verify.domain.DomainVerificationUtils.isChangeEnabled(this.mPlatformCompat, androidPackage, RESTRICT_DOMAINS)) {
            return (ReturnValue) collectDomainsInternal(androidPackage, z, z2, initialvalue, biFunction);
        }
        return (ReturnValue) collectDomainsLegacy(androidPackage, z, z2, initialvalue, biFunction);
    }

    private <InitialValue, ReturnValue> ReturnValue collectDomainsLegacy(com.android.server.pm.pkg.AndroidPackage androidPackage, boolean z, boolean z2, InitialValue initialvalue, java.util.function.BiFunction<InitialValue, java.lang.String, ReturnValue> biFunction) {
        if (!z) {
            return (ReturnValue) collectDomainsInternal(androidPackage, false, true, initialvalue, biFunction);
        }
        java.util.List activities = androidPackage.getActivities();
        int size = activities.size();
        boolean zContains = this.mSystemConfig.getLinkedApps().contains(androidPackage.getPackageName());
        if (!zContains) {
            for (int i = 0; i < size && !zContains; i++) {
                java.util.List intents = ((com.android.internal.pm.pkg.component.ParsedActivity) activities.get(i)).getIntents();
                int size2 = intents.size();
                for (int i2 = 0; i2 < size2 && !zContains; i2++) {
                    zContains = ((com.android.internal.pm.pkg.component.ParsedIntentInfo) intents.get(i2)).getIntentFilter().needsVerification();
                }
            }
            if (!zContains) {
                return null;
            }
        }
        int iByteSizeOf = 0;
        boolean z3 = true;
        int i3 = 0;
        while (i3 < size && z3) {
            java.util.List intents2 = ((com.android.internal.pm.pkg.component.ParsedActivity) activities.get(i3)).getIntents();
            int size3 = intents2.size();
            int i4 = 0;
            while (i4 < size3 && z3) {
                android.content.IntentFilter intentFilter = ((com.android.internal.pm.pkg.component.ParsedIntentInfo) intents2.get(i4)).getIntentFilter();
                if (intentFilter.handlesWebUris(false)) {
                    int iCountDataAuthorities = intentFilter.countDataAuthorities();
                    int i5 = 0;
                    while (i5 < iCountDataAuthorities) {
                        java.lang.String host = intentFilter.getDataAuthority(i5).getHost();
                        java.util.List list = activities;
                        int i6 = size;
                        if (isValidHost(host) == z2) {
                            iByteSizeOf += byteSizeOf(host);
                            z3 = iByteSizeOf < 1048576;
                            ReturnValue returnvalueApply = biFunction.apply(initialvalue, host);
                            if (returnvalueApply != null) {
                                return returnvalueApply;
                            }
                        }
                        i5++;
                        activities = list;
                        size = i6;
                    }
                }
                i4++;
                activities = activities;
                size = size;
            }
            i3++;
            activities = activities;
            size = size;
        }
        return null;
    }

    private <InitialValue, ReturnValue> ReturnValue collectDomainsInternal(com.android.server.pm.pkg.AndroidPackage pkg, boolean checkAutoVerify, boolean valid, InitialValue initialValue, java.util.function.BiFunction<InitialValue, java.lang.String, ReturnValue> domainCollector) {
        boolean underMaxSize;
        com.android.server.pm.verify.domain.DomainVerificationCollector domainVerificationCollector = this;
        boolean z = checkAutoVerify;
        int totalSize = 0;
        boolean underMaxSize2 = true;
        java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        int activityIndex = 0;
        while (activityIndex < activitiesSize && underMaxSize2) {
            com.android.internal.pm.pkg.component.ParsedActivity activity = activities.get(activityIndex);
            java.util.List<com.android.internal.pm.pkg.component.ParsedIntentInfo> intents = activity.getIntents();
            int intentsSize = intents.size();
            int intentIndex = 0;
            while (intentIndex < intentsSize && underMaxSize2) {
                com.android.internal.pm.pkg.component.ParsedIntentInfo intent = intents.get(intentIndex);
                android.content.IntentFilter intentFilter = intent.getIntentFilter();
                if (z && !intentFilter.getAutoVerify()) {
                    underMaxSize = underMaxSize2;
                } else if (!intentFilter.hasCategory("android.intent.category.DEFAULT")) {
                    underMaxSize = underMaxSize2;
                } else if (intentFilter.handlesWebUris(z)) {
                    int authorityCount = intentFilter.countDataAuthorities();
                    int index = 0;
                    while (index < authorityCount && underMaxSize2) {
                        java.lang.String host = intentFilter.getDataAuthority(index).getHost();
                        boolean underMaxSize3 = underMaxSize2;
                        if (domainVerificationCollector.isValidHost(host) == valid) {
                            totalSize += domainVerificationCollector.byteSizeOf(host);
                            boolean underMaxSize4 = totalSize < 1048576;
                            underMaxSize3 = underMaxSize4;
                            ReturnValue returnValue = domainCollector.apply(initialValue, host);
                            if (returnValue != null) {
                                return returnValue;
                            }
                        }
                        index++;
                        domainVerificationCollector = this;
                        underMaxSize2 = underMaxSize3;
                    }
                    underMaxSize = underMaxSize2;
                } else {
                    underMaxSize = underMaxSize2;
                }
                intentIndex++;
                domainVerificationCollector = this;
                z = checkAutoVerify;
                underMaxSize2 = underMaxSize;
            }
            activityIndex++;
            domainVerificationCollector = this;
            z = checkAutoVerify;
            underMaxSize2 = underMaxSize2;
        }
        return null;
    }

    private int byteSizeOf(java.lang.String string) {
        return android.content.pm.verify.domain.DomainVerificationUtils.estimatedByteSizeOf(string);
    }

    private boolean isValidHost(java.lang.String host) {
        if (android.text.TextUtils.isEmpty(host)) {
            return false;
        }
        this.mDomainMatcher.reset(host);
        return this.mDomainMatcher.matches();
    }
}
