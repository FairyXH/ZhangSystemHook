package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
public class IntentFirewall {
    private static final int LOG_PACKAGES_MAX_LENGTH = 150;
    private static final int LOG_PACKAGES_SUFFICIENT_LENGTH = 125;
    private static final java.io.File RULES_DIR = new java.io.File(android.os.Environment.getDataSystemDirectory(), "ifw");
    static final java.lang.String TAG = "IntentFirewall";
    private static final java.lang.String TAG_ACTIVITY = "activity";
    private static final java.lang.String TAG_BROADCAST = "broadcast";
    private static final java.lang.String TAG_RULES = "rules";
    private static final java.lang.String TAG_SERVICE = "service";
    private static final int TYPE_ACTIVITY = 0;
    private static final int TYPE_BROADCAST = 1;
    private static final int TYPE_SERVICE = 2;
    private static final java.util.HashMap<java.lang.String, com.android.server.firewall.FilterFactory> factoryMap;
    private com.android.server.firewall.IntentFirewall.FirewallIntentResolver mActivityResolver;
    private final com.android.server.firewall.IntentFirewall.AMSInterface mAms;
    private com.android.server.firewall.IntentFirewall.FirewallIntentResolver mBroadcastResolver;
    final com.android.server.firewall.IntentFirewall.FirewallHandler mHandler;
    private final com.android.server.firewall.IntentFirewall.RuleObserver mObserver;
    private android.content.pm.PackageManagerInternal mPackageManager;
    private com.android.server.firewall.IntentFirewall.FirewallIntentResolver mServiceResolver;

    public interface AMSInterface {
        int checkComponentPermission(java.lang.String str, int i, int i2, int i3, boolean z);

        java.lang.Object getAMSLock();
    }

    static {
        com.android.server.firewall.FilterFactory[] factories = {com.android.server.firewall.AndFilter.FACTORY, com.android.server.firewall.OrFilter.FACTORY, com.android.server.firewall.NotFilter.FACTORY, com.android.server.firewall.StringFilter.ACTION, com.android.server.firewall.StringFilter.COMPONENT, com.android.server.firewall.StringFilter.COMPONENT_NAME, com.android.server.firewall.StringFilter.COMPONENT_PACKAGE, com.android.server.firewall.StringFilter.DATA, com.android.server.firewall.StringFilter.HOST, com.android.server.firewall.StringFilter.MIME_TYPE, com.android.server.firewall.StringFilter.SCHEME, com.android.server.firewall.StringFilter.PATH, com.android.server.firewall.StringFilter.SSP, com.android.server.firewall.CategoryFilter.FACTORY, com.android.server.firewall.SenderFilter.FACTORY, com.android.server.firewall.SenderPackageFilter.FACTORY, com.android.server.firewall.SenderPermissionFilter.FACTORY, com.android.server.firewall.PortFilter.FACTORY};
        factoryMap = new java.util.HashMap<>((factories.length * 4) / 3);
        for (com.android.server.firewall.FilterFactory factory : factories) {
            factoryMap.put(factory.getTagName(), factory);
        }
    }

    public IntentFirewall(com.android.server.firewall.IntentFirewall.AMSInterface ams, android.os.Handler handler) {
        this.mActivityResolver = new com.android.server.firewall.IntentFirewall.FirewallIntentResolver();
        this.mBroadcastResolver = new com.android.server.firewall.IntentFirewall.FirewallIntentResolver();
        this.mServiceResolver = new com.android.server.firewall.IntentFirewall.FirewallIntentResolver();
        this.mAms = ams;
        this.mHandler = new com.android.server.firewall.IntentFirewall.FirewallHandler(handler.getLooper());
        java.io.File rulesDir = getRulesDir();
        rulesDir.mkdirs();
        readRulesDir(rulesDir);
        this.mObserver = new com.android.server.firewall.IntentFirewall.RuleObserver(rulesDir);
        this.mObserver.startWatching();
    }

    android.content.pm.PackageManagerInternal getPackageManager() {
        if (this.mPackageManager == null) {
            this.mPackageManager = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        }
        return this.mPackageManager;
    }

    public boolean checkStartActivity(android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, android.content.pm.ApplicationInfo resolvedApp) {
        return checkIntent(this.mActivityResolver, intent.getComponent(), 0, intent, callerUid, callerPid, resolvedType, resolvedApp.uid);
    }

    public boolean checkService(android.content.ComponentName resolvedService, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, android.content.pm.ApplicationInfo resolvedApp) {
        return checkIntent(this.mServiceResolver, resolvedService, 2, intent, callerUid, callerPid, resolvedType, resolvedApp.uid);
    }

    public boolean checkBroadcast(android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
        return checkIntent(this.mBroadcastResolver, intent.getComponent(), 1, intent, callerUid, callerPid, resolvedType, receivingUid);
    }

    public boolean checkIntent(com.android.server.firewall.IntentFirewall.FirewallIntentResolver resolver, android.content.ComponentName resolvedComponent, int intentType, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
        boolean log = false;
        boolean block = false;
        java.util.List<com.android.server.firewall.IntentFirewall.Rule> candidateRules = resolver.queryIntent(getPackageManager().snapshot(), intent, resolvedType, false, 0);
        if (candidateRules == null) {
            candidateRules = new java.util.ArrayList();
        }
        resolver.queryByComponent(resolvedComponent, candidateRules);
        for (int i = 0; i < candidateRules.size(); i++) {
            com.android.server.firewall.IntentFirewall.Rule rule = candidateRules.get(i);
            if (rule.matches(this, resolvedComponent, intent, callerUid, callerPid, resolvedType, receivingUid)) {
                block |= rule.getBlock();
                log |= rule.getLog();
                if (block && log) {
                    break;
                }
            }
        }
        if (log) {
            logIntent(intentType, intent, callerUid, resolvedType);
        }
        return !block;
    }

    private static void logIntent(int intentType, android.content.Intent intent, int callerUid, java.lang.String resolvedType) {
        java.lang.String shortComponent;
        java.lang.String callerPackages;
        int callerPackageCount;
        android.content.ComponentName cn = intent.getComponent();
        if (cn == null) {
            shortComponent = null;
        } else {
            java.lang.String shortComponent2 = cn.flattenToShortString();
            shortComponent = shortComponent2;
        }
        java.lang.String callerPackages2 = null;
        int callerPackageCount2 = 0;
        android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
        if (pm == null) {
            callerPackages = null;
            callerPackageCount = 0;
        } else {
            try {
                java.lang.String[] callerPackagesArray = pm.getPackagesForUid(callerUid);
                if (callerPackagesArray != null) {
                    callerPackageCount2 = callerPackagesArray.length;
                    callerPackages2 = joinPackages(callerPackagesArray);
                }
                callerPackages = callerPackages2;
                callerPackageCount = callerPackageCount2;
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(TAG, "Remote exception while retrieving packages", ex);
                callerPackages = null;
                callerPackageCount = callerPackageCount2;
            }
        }
        com.android.server.EventLogTags.writeIfwIntentMatched(intentType, shortComponent, callerUid, callerPackageCount, callerPackages, intent.getAction(), resolvedType, intent.getDataString(), intent.getFlags());
    }

    private static java.lang.String joinPackages(java.lang.String[] packages) {
        boolean first = true;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.String pkg : packages) {
            if (sb.length() + pkg.length() + 1 < 150) {
                if (!first) {
                    sb.append(',');
                } else {
                    first = false;
                }
                sb.append(pkg);
            } else if (sb.length() >= 125) {
                return sb.toString();
            }
        }
        int i = sb.length();
        if (i == 0 && packages.length > 0) {
            java.lang.String pkg2 = packages[0];
            return pkg2.substring((pkg2.length() - 150) + 1) + '-';
        }
        return null;
    }

    public static java.io.File getRulesDir() {
        return RULES_DIR;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readRulesDir(java.io.File rulesDir) {
        com.android.server.firewall.IntentFirewall.FirewallIntentResolver[] resolvers = new com.android.server.firewall.IntentFirewall.FirewallIntentResolver[3];
        for (int i = 0; i < resolvers.length; i++) {
            resolvers[i] = new com.android.server.firewall.IntentFirewall.FirewallIntentResolver();
        }
        java.io.File[] files = rulesDir.listFiles();
        if (files != null) {
            for (java.io.File file : files) {
                if (file.getName().endsWith(".xml")) {
                    readRules(file, resolvers);
                }
            }
        }
        android.util.Slog.i(TAG, "Read new rules (A:" + resolvers[0].filterSet().size() + " B:" + resolvers[1].filterSet().size() + " S:" + resolvers[2].filterSet().size() + ")");
        synchronized (this.mAms.getAMSLock()) {
            this.mActivityResolver = resolvers[0];
            this.mBroadcastResolver = resolvers[1];
            this.mServiceResolver = resolvers[2];
        }
    }

    private void readRules(java.io.File rulesFile, com.android.server.firewall.IntentFirewall.FirewallIntentResolver[] resolvers) {
        java.util.List<java.util.List<com.android.server.firewall.IntentFirewall.Rule>> rulesByType = new java.util.ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            rulesByType.add(new java.util.ArrayList<>());
        }
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(rulesFile);
            try {
                try {
                    try {
                        org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
                        parser.setInput(fis, null);
                        com.android.internal.util.XmlUtils.beginDocument(parser, TAG_RULES);
                        int outerDepth = parser.getDepth();
                        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                            java.lang.String tagName = parser.getName();
                            int ruleType = tagName.equals("activity") ? 0 : tagName.equals("broadcast") ? 1 : tagName.equals("service") ? 2 : -1;
                            if (ruleType != -1) {
                                com.android.server.firewall.IntentFirewall.Rule rule = new com.android.server.firewall.IntentFirewall.Rule();
                                java.util.List<com.android.server.firewall.IntentFirewall.Rule> rules = rulesByType.get(ruleType);
                                try {
                                    rule.readFromXml(parser);
                                    rules.add(rule);
                                } catch (org.xmlpull.v1.XmlPullParserException ex) {
                                    android.util.Slog.e(TAG, "Error reading an intent firewall rule from " + rulesFile, ex);
                                }
                            }
                        }
                        try {
                            fis.close();
                        } catch (java.io.IOException ex2) {
                            android.util.Slog.e(TAG, "Error while closing " + rulesFile, ex2);
                        }
                        for (int ruleType2 = 0; ruleType2 < rulesByType.size(); ruleType2++) {
                            java.util.List<com.android.server.firewall.IntentFirewall.Rule> rules2 = rulesByType.get(ruleType2);
                            com.android.server.firewall.IntentFirewall.FirewallIntentResolver resolver = resolvers[ruleType2];
                            for (int ruleIndex = 0; ruleIndex < rules2.size(); ruleIndex++) {
                                com.android.server.firewall.IntentFirewall.Rule rule2 = rules2.get(ruleIndex);
                                for (int i2 = 0; i2 < rule2.getIntentFilterCount(); i2++) {
                                    resolver.addFilter(null, rule2.getIntentFilter(i2));
                                }
                                for (int i3 = 0; i3 < rule2.getComponentFilterCount(); i3++) {
                                    resolver.addComponentFilter(rule2.getComponentFilter(i3), rule2);
                                }
                            }
                        }
                    } catch (org.xmlpull.v1.XmlPullParserException ex3) {
                        android.util.Slog.e(TAG, "Error reading intent firewall rules from " + rulesFile, ex3);
                        try {
                            fis.close();
                        } catch (java.io.IOException ex4) {
                            android.util.Slog.e(TAG, "Error while closing " + rulesFile, ex4);
                        }
                    }
                } catch (java.io.IOException ex5) {
                    android.util.Slog.e(TAG, "Error reading intent firewall rules from " + rulesFile, ex5);
                    try {
                        fis.close();
                    } catch (java.io.IOException ex6) {
                        android.util.Slog.e(TAG, "Error while closing " + rulesFile, ex6);
                    }
                }
            } finally {
            }
        } catch (java.io.FileNotFoundException e) {
        }
    }

    static com.android.server.firewall.Filter parseFilter(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String elementName = parser.getName();
        com.android.server.firewall.FilterFactory factory = factoryMap.get(elementName);
        if (factory == null) {
            throw new org.xmlpull.v1.XmlPullParserException("Unknown element in filter list: " + elementName);
        }
        return factory.newFilter(parser);
    }

    private static class Rule extends com.android.server.firewall.AndFilter {
        private static final java.lang.String ATTR_BLOCK = "block";
        private static final java.lang.String ATTR_LOG = "log";
        private static final java.lang.String ATTR_NAME = "name";
        private static final java.lang.String TAG_COMPONENT_FILTER = "component-filter";
        private static final java.lang.String TAG_INTENT_FILTER = "intent-filter";
        private boolean block;
        private boolean log;
        private final java.util.ArrayList<android.content.ComponentName> mComponentFilters;
        private final java.util.ArrayList<com.android.server.firewall.IntentFirewall.FirewallIntentFilter> mIntentFilters;

        private Rule() {
            this.mIntentFilters = new java.util.ArrayList<>(1);
            this.mComponentFilters = new java.util.ArrayList<>(0);
        }

        @Override // com.android.server.firewall.FilterList
        public com.android.server.firewall.IntentFirewall.Rule readFromXml(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            this.block = java.lang.Boolean.parseBoolean(parser.getAttributeValue(null, ATTR_BLOCK));
            this.log = java.lang.Boolean.parseBoolean(parser.getAttributeValue(null, ATTR_LOG));
            super.readFromXml(parser);
            return this;
        }

        @Override // com.android.server.firewall.FilterList
        protected void readChild(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.lang.String currentTag = parser.getName();
            if (currentTag.equals(TAG_INTENT_FILTER)) {
                com.android.server.firewall.IntentFirewall.FirewallIntentFilter intentFilter = new com.android.server.firewall.IntentFirewall.FirewallIntentFilter(this);
                intentFilter.readFromXml(parser);
                this.mIntentFilters.add(intentFilter);
            } else {
                if (currentTag.equals(TAG_COMPONENT_FILTER)) {
                    java.lang.String componentStr = parser.getAttributeValue(null, "name");
                    if (componentStr == null) {
                        throw new org.xmlpull.v1.XmlPullParserException("Component name must be specified.", parser, null);
                    }
                    android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(componentStr);
                    if (componentName == null) {
                        throw new org.xmlpull.v1.XmlPullParserException("Invalid component name: " + componentStr);
                    }
                    this.mComponentFilters.add(componentName);
                    return;
                }
                super.readChild(parser);
            }
        }

        public int getIntentFilterCount() {
            return this.mIntentFilters.size();
        }

        public com.android.server.firewall.IntentFirewall.FirewallIntentFilter getIntentFilter(int index) {
            return this.mIntentFilters.get(index);
        }

        public int getComponentFilterCount() {
            return this.mComponentFilters.size();
        }

        public android.content.ComponentName getComponentFilter(int index) {
            return this.mComponentFilters.get(index);
        }

        public boolean getBlock() {
            return this.block;
        }

        public boolean getLog() {
            return this.log;
        }
    }

    private static class FirewallIntentFilter extends android.content.IntentFilter {
        private final com.android.server.firewall.IntentFirewall.Rule rule;

        public FirewallIntentFilter(com.android.server.firewall.IntentFirewall.Rule rule) {
            this.rule = rule;
        }
    }

    private static class FirewallIntentResolver extends com.android.server.IntentResolver<com.android.server.firewall.IntentFirewall.FirewallIntentFilter, com.android.server.firewall.IntentFirewall.Rule> {
        private final android.util.ArrayMap<android.content.ComponentName, com.android.server.firewall.IntentFirewall.Rule[]> mRulesByComponent;

        private FirewallIntentResolver() {
            this.mRulesByComponent = new android.util.ArrayMap<>(0);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public boolean allowFilterResult(com.android.server.firewall.IntentFirewall.FirewallIntentFilter filter, java.util.List<com.android.server.firewall.IntentFirewall.Rule> dest) {
            return !dest.contains(filter.rule);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public boolean isPackageForFilter(java.lang.String packageName, com.android.server.firewall.IntentFirewall.FirewallIntentFilter filter) {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.IntentResolver
        public com.android.server.firewall.IntentFirewall.FirewallIntentFilter[] newArray(int size) {
            return new com.android.server.firewall.IntentFirewall.FirewallIntentFilter[size];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public com.android.server.firewall.IntentFirewall.Rule newResult(com.android.server.pm.Computer computer, com.android.server.firewall.IntentFirewall.FirewallIntentFilter filter, int match, int userId, long customFlags) {
            return filter.rule;
        }

        @Override // com.android.server.IntentResolver
        protected void sortResults(java.util.List<com.android.server.firewall.IntentFirewall.Rule> results) {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.content.IntentFilter getIntentFilter(com.android.server.firewall.IntentFirewall.FirewallIntentFilter input) {
            return input;
        }

        public void queryByComponent(android.content.ComponentName componentName, java.util.List<com.android.server.firewall.IntentFirewall.Rule> candidateRules) {
            com.android.server.firewall.IntentFirewall.Rule[] rules = this.mRulesByComponent.get(componentName);
            if (rules != null) {
                candidateRules.addAll(java.util.Arrays.asList(rules));
            }
        }

        public void addComponentFilter(android.content.ComponentName componentName, com.android.server.firewall.IntentFirewall.Rule rule) {
            com.android.server.firewall.IntentFirewall.Rule[] rules = this.mRulesByComponent.get(componentName);
            this.mRulesByComponent.put(componentName, (com.android.server.firewall.IntentFirewall.Rule[]) com.android.internal.util.ArrayUtils.appendElement(com.android.server.firewall.IntentFirewall.Rule.class, rules, rule));
        }
    }

    private final class FirewallHandler extends android.os.Handler {
        public FirewallHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            com.android.server.firewall.IntentFirewall.this.readRulesDir(com.android.server.firewall.IntentFirewall.getRulesDir());
        }
    }

    private class RuleObserver extends android.os.FileObserver {
        private static final int MONITORED_EVENTS = 968;

        public RuleObserver(java.io.File monitoredDir) {
            super(monitoredDir.getAbsolutePath(), MONITORED_EVENTS);
        }

        @Override // android.os.FileObserver
        public void onEvent(int event, java.lang.String path) {
            if (path != null && path.endsWith(".xml")) {
                com.android.server.firewall.IntentFirewall.this.mHandler.removeMessages(0);
                com.android.server.firewall.IntentFirewall.this.mHandler.sendEmptyMessageDelayed(0, 250L);
            }
        }
    }

    boolean checkComponentPermission(java.lang.String permission, int pid, int uid, int owningUid, boolean exported) {
        return this.mAms.checkComponentPermission(permission, pid, uid, owningUid, exported) == 0;
    }

    boolean signaturesMatch(int uid1, int uid2) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return getPackageManager().checkUidSignaturesForAllUsers(uid1, uid2) == 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }
}
