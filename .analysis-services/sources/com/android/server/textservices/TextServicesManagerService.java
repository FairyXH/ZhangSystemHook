package com.android.server.textservices;

/* JADX INFO: loaded from: classes3.dex */
public class TextServicesManagerService extends com.android.internal.textservice.ITextServicesManager.Stub {
    private static final boolean DBG = false;
    private static final java.lang.String TAG = com.android.server.textservices.TextServicesManagerService.class.getSimpleName();
    private final android.content.Context mContext;
    private final android.os.UserManager mUserManager;
    private final android.util.SparseArray<com.android.server.textservices.TextServicesManagerService.TextServicesData> mUserData = new android.util.SparseArray<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.textservices.TextServicesManagerService.TextServicesMonitor mMonitor = new com.android.server.textservices.TextServicesManagerService.TextServicesMonitor();

    private static class TextServicesData {
        private final android.content.Context mContext;
        private final android.content.ContentResolver mResolver;
        private final int mUserId;
        public int mUpdateCount = 0;
        private final java.util.HashMap<java.lang.String, android.view.textservice.SpellCheckerInfo> mSpellCheckerMap = new java.util.HashMap<>();
        private final java.util.ArrayList<android.view.textservice.SpellCheckerInfo> mSpellCheckerList = new java.util.ArrayList<>();
        private final java.util.HashMap<java.lang.String, com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> mSpellCheckerBindGroups = new java.util.HashMap<>();

        public TextServicesData(int userId, android.content.Context context) {
            this.mUserId = userId;
            this.mContext = context;
            this.mResolver = context.getContentResolver();
        }

        private void putString(java.lang.String key, java.lang.String str) {
            android.provider.Settings.Secure.putStringForUser(this.mResolver, key, str, this.mUserId);
        }

        private java.lang.String getString(java.lang.String key, java.lang.String defaultValue) {
            java.lang.String result = android.provider.Settings.Secure.getStringForUser(this.mResolver, key, this.mUserId);
            return result != null ? result : defaultValue;
        }

        private void putInt(java.lang.String key, int value) {
            android.provider.Settings.Secure.putIntForUser(this.mResolver, key, value, this.mUserId);
        }

        private int getInt(java.lang.String key, int defaultValue) {
            return android.provider.Settings.Secure.getIntForUser(this.mResolver, key, defaultValue, this.mUserId);
        }

        private boolean getBoolean(java.lang.String str, boolean z) {
            return getInt(str, z ? 1 : 0) == 1;
        }

        private void putSelectedSpellChecker(java.lang.String sciId) {
            putString("selected_spell_checker", sciId);
        }

        private void putSelectedSpellCheckerSubtype(int hashCode) {
            putInt("selected_spell_checker_subtype", hashCode);
        }

        private java.lang.String getSelectedSpellChecker() {
            return getString("selected_spell_checker", "");
        }

        public int getSelectedSpellCheckerSubtype(int defaultValue) {
            return getInt("selected_spell_checker_subtype", defaultValue);
        }

        public boolean isSpellCheckerEnabled() {
            return getBoolean("spell_checker_enabled", true);
        }

        public android.view.textservice.SpellCheckerInfo getCurrentSpellChecker() {
            java.lang.String curSpellCheckerId = getSelectedSpellChecker();
            if (android.text.TextUtils.isEmpty(curSpellCheckerId)) {
                return null;
            }
            return this.mSpellCheckerMap.get(curSpellCheckerId);
        }

        public void setCurrentSpellChecker(android.view.textservice.SpellCheckerInfo sci) {
            if (sci != null) {
                putSelectedSpellChecker(sci.getId());
            } else {
                putSelectedSpellChecker("");
            }
            putSelectedSpellCheckerSubtype(0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initializeTextServicesData() {
            this.mSpellCheckerList.clear();
            this.mSpellCheckerMap.clear();
            this.mUpdateCount++;
            android.content.pm.PackageManager pm = this.mContext.getPackageManager();
            java.util.List<android.content.pm.ResolveInfo> services = pm.queryIntentServicesAsUser(new android.content.Intent("android.service.textservice.SpellCheckerService"), 128, this.mUserId);
            int N = services.size();
            for (int i = 0; i < N; i++) {
                android.content.pm.ResolveInfo ri = services.get(i);
                android.content.pm.ServiceInfo si = ri.serviceInfo;
                android.content.ComponentName compName = new android.content.ComponentName(si.packageName, si.name);
                if (!"android.permission.BIND_TEXT_SERVICE".equals(si.permission)) {
                    android.util.Slog.w(com.android.server.textservices.TextServicesManagerService.TAG, "Skipping text service " + compName + ": it does not require the permission android.permission.BIND_TEXT_SERVICE");
                } else {
                    try {
                        android.view.textservice.SpellCheckerInfo sci = new android.view.textservice.SpellCheckerInfo(this.mContext, ri);
                        if (sci.getSubtypeCount() <= 0) {
                            android.util.Slog.w(com.android.server.textservices.TextServicesManagerService.TAG, "Skipping text service " + compName + ": it does not contain subtypes.");
                        } else {
                            this.mSpellCheckerList.add(sci);
                            this.mSpellCheckerMap.put(sci.getId(), sci);
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.w(com.android.server.textservices.TextServicesManagerService.TAG, "Unable to load the spell checker " + compName, e);
                    } catch (org.xmlpull.v1.XmlPullParserException e2) {
                        android.util.Slog.w(com.android.server.textservices.TextServicesManagerService.TAG, "Unable to load the spell checker " + compName, e2);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw) {
            int spellCheckerIndex = 0;
            pw.println("  User #" + this.mUserId);
            pw.println("  Spell Checkers:");
            pw.println("  Spell Checkers: mUpdateCount=" + this.mUpdateCount);
            for (android.view.textservice.SpellCheckerInfo info : this.mSpellCheckerMap.values()) {
                pw.println("  Spell Checker #" + spellCheckerIndex);
                info.dump(pw, "    ");
                spellCheckerIndex++;
            }
            pw.println("");
            pw.println("  Spell Checker Bind Groups:");
            java.util.HashMap<java.lang.String, com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> spellCheckerBindGroups = this.mSpellCheckerBindGroups;
            for (java.util.Map.Entry<java.lang.String, com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> ent : spellCheckerBindGroups.entrySet()) {
                com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup grp = ent.getValue();
                pw.println("    " + ent.getKey() + " " + grp + ":");
                pw.println("      mInternalConnection=" + grp.mInternalConnection);
                pw.println("      mSpellChecker=" + grp.mSpellChecker);
                pw.println("      mUnbindCalled=" + grp.mUnbindCalled);
                pw.println("      mConnected=" + grp.mConnected);
                int numPendingSessionRequests = grp.mPendingSessionRequests.size();
                int j = 0;
                while (j < numPendingSessionRequests) {
                    com.android.server.textservices.TextServicesManagerService.SessionRequest req = (com.android.server.textservices.TextServicesManagerService.SessionRequest) grp.mPendingSessionRequests.get(j);
                    pw.println("      Pending Request #" + j + ":");
                    pw.println("        mTsListener=" + req.mTsListener);
                    pw.println("        mScListener=" + req.mScListener);
                    pw.println("        mScLocale=" + req.mLocale + " mUid=" + req.mUid);
                    j++;
                    spellCheckerIndex = spellCheckerIndex;
                }
                int spellCheckerIndex2 = spellCheckerIndex;
                int j2 = 0;
                for (int numOnGoingSessionRequests = grp.mOnGoingSessionRequests.size(); j2 < numOnGoingSessionRequests; numOnGoingSessionRequests = numOnGoingSessionRequests) {
                    com.android.server.textservices.TextServicesManagerService.SessionRequest req2 = (com.android.server.textservices.TextServicesManagerService.SessionRequest) grp.mOnGoingSessionRequests.get(j2);
                    pw.println("      On going Request #" + j2 + ":");
                    pw.println("        mTsListener=" + req2.mTsListener);
                    pw.println("        mScListener=" + req2.mScListener);
                    pw.println("        mScLocale=" + req2.mLocale + " mUid=" + req2.mUid);
                    j2++;
                }
                int N = grp.mListeners.getRegisteredCallbackCount();
                for (int j3 = 0; j3 < N; j3++) {
                    com.android.internal.textservice.ISpellCheckerSessionListener mScListener = grp.mListeners.getRegisteredCallbackItem(j3);
                    pw.println("      Listener #" + j3 + ":");
                    pw.println("        mScListener=" + mScListener);
                    pw.println("        mGroup=" + grp);
                }
                spellCheckerIndex = spellCheckerIndex2;
            }
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private com.android.server.textservices.TextServicesManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mService = new com.android.server.textservices.TextServicesManagerService(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            com.android.server.LocalServices.addService(com.android.server.textservices.TextServicesManagerInternal.class, new com.android.server.textservices.TextServicesManagerInternal() { // from class: com.android.server.textservices.TextServicesManagerService.Lifecycle.1
                @Override // com.android.server.textservices.TextServicesManagerInternal
                public android.view.textservice.SpellCheckerInfo getCurrentSpellCheckerForUser(int userId) {
                    return com.android.server.textservices.TextServicesManagerService.Lifecycle.this.mService.getCurrentSpellCheckerForUser(userId);
                }
            });
            publishBinderService("textservices", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser user) {
            this.mService.onStopUser(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            this.mService.onUnlockUser(user.getUserIdentifier());
        }
    }

    void onStopUser(int userId) {
        synchronized (this.mLock) {
            com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = this.mUserData.get(userId);
            if (tsd == null) {
                return;
            }
            unbindServiceLocked(tsd);
            this.mUserData.remove(userId);
        }
    }

    void onUnlockUser(int userId) {
        synchronized (this.mLock) {
            initializeInternalStateLocked(userId);
        }
    }

    public TextServicesManagerService(android.content.Context context) {
        this.mContext = context;
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        this.mMonitor.register(context, null, android.os.UserHandle.ALL, true);
    }

    private void initializeInternalStateLocked(int userId) {
        com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = this.mUserData.get(userId);
        if (tsd == null) {
            tsd = new com.android.server.textservices.TextServicesManagerService.TextServicesData(userId, this.mContext);
            this.mUserData.put(userId, tsd);
        }
        tsd.initializeTextServicesData();
        android.view.textservice.SpellCheckerInfo sci = tsd.getCurrentSpellChecker();
        if (sci == null) {
            android.view.textservice.SpellCheckerInfo sci2 = findAvailSystemSpellCheckerLocked(null, tsd);
            setCurrentSpellCheckerLocked(sci2, tsd);
        }
    }

    private final class TextServicesMonitor extends com.android.internal.content.PackageMonitor {
        private TextServicesMonitor() {
        }

        public void onSomePackagesChanged() {
            android.view.textservice.SpellCheckerInfo availSci;
            int userId = getChangingUserId();
            synchronized (com.android.server.textservices.TextServicesManagerService.this.mLock) {
                com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = (com.android.server.textservices.TextServicesManagerService.TextServicesData) com.android.server.textservices.TextServicesManagerService.this.mUserData.get(userId);
                if (tsd == null) {
                    return;
                }
                android.view.textservice.SpellCheckerInfo sci = tsd.getCurrentSpellChecker();
                tsd.initializeTextServicesData();
                if (tsd.isSpellCheckerEnabled()) {
                    if (sci == null) {
                        com.android.server.textservices.TextServicesManagerService.this.setCurrentSpellCheckerLocked(com.android.server.textservices.TextServicesManagerService.this.findAvailSystemSpellCheckerLocked(null, tsd), tsd);
                    } else {
                        java.lang.String packageName = sci.getPackageName();
                        int change = isPackageDisappearing(packageName);
                        if ((change == 3 || change == 2) && ((availSci = com.android.server.textservices.TextServicesManagerService.this.findAvailSystemSpellCheckerLocked(packageName, tsd)) == null || (availSci != null && !availSci.getId().equals(sci.getId())))) {
                            com.android.server.textservices.TextServicesManagerService.this.setCurrentSpellCheckerLocked(availSci, tsd);
                        }
                    }
                }
            }
        }
    }

    private boolean bindCurrentSpellCheckerService(android.content.Intent service, android.content.ServiceConnection conn, int flags, int userId) {
        if (service == null || conn == null) {
            android.util.Slog.e(TAG, "--- bind failed: service = " + service + ", conn = " + conn + ", userId =" + userId);
            return false;
        }
        return this.mContext.bindServiceAsUser(service, conn, flags, android.os.UserHandle.of(userId));
    }

    private void unbindServiceLocked(com.android.server.textservices.TextServicesManagerService.TextServicesData tsd) {
        java.util.HashMap<java.lang.String, com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> spellCheckerBindGroups = tsd.mSpellCheckerBindGroups;
        for (com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup scbg : spellCheckerBindGroups.values()) {
            scbg.removeAllLocked();
        }
        spellCheckerBindGroups.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.view.textservice.SpellCheckerInfo findAvailSystemSpellCheckerLocked(java.lang.String prefPackage, com.android.server.textservices.TextServicesManagerService.TextServicesData tsd) {
        java.util.ArrayList<android.view.textservice.SpellCheckerInfo> spellCheckerList = new java.util.ArrayList<>();
        for (android.view.textservice.SpellCheckerInfo sci : tsd.mSpellCheckerList) {
            if ((1 & sci.getServiceInfo().applicationInfo.flags) != 0) {
                spellCheckerList.add(sci);
            }
        }
        int spellCheckersCount = spellCheckerList.size();
        if (spellCheckersCount == 0) {
            android.util.Slog.w(TAG, "no available spell checker services found");
            return null;
        }
        if (prefPackage != null) {
            for (int i = 0; i < spellCheckersCount; i++) {
                android.view.textservice.SpellCheckerInfo sci2 = spellCheckerList.get(i);
                if (prefPackage.equals(sci2.getPackageName())) {
                    return sci2;
                }
            }
        }
        java.util.Locale systemLocal = this.mContext.getResources().getConfiguration().locale;
        java.util.ArrayList<java.util.Locale> suitableLocales = com.android.server.textservices.LocaleUtils.getSuitableLocalesForSpellChecker(systemLocal);
        int localeCount = suitableLocales.size();
        for (int localeIndex = 0; localeIndex < localeCount; localeIndex++) {
            java.util.Locale locale = suitableLocales.get(localeIndex);
            for (int spellCheckersIndex = 0; spellCheckersIndex < spellCheckersCount; spellCheckersIndex++) {
                android.view.textservice.SpellCheckerInfo info = spellCheckerList.get(spellCheckersIndex);
                int subtypeCount = info.getSubtypeCount();
                for (int subtypeIndex = 0; subtypeIndex < subtypeCount; subtypeIndex++) {
                    android.view.textservice.SpellCheckerSubtype subtype = info.getSubtypeAt(subtypeIndex);
                    java.util.Locale subtypeLocale = com.android.internal.inputmethod.SubtypeLocaleUtils.constructLocaleFromString(subtype.getLocale());
                    if (locale.equals(subtypeLocale)) {
                        return info;
                    }
                }
            }
        }
        if (spellCheckersCount > 1) {
            android.util.Slog.w(TAG, "more than one spell checker service found, picking first");
        }
        return spellCheckerList.get(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.view.textservice.SpellCheckerInfo getCurrentSpellCheckerForUser(int userId) {
        android.view.textservice.SpellCheckerInfo currentSpellChecker;
        synchronized (this.mLock) {
            com.android.server.textservices.TextServicesManagerService.TextServicesData data = this.mUserData.get(userId);
            currentSpellChecker = data != null ? data.getCurrentSpellChecker() : null;
        }
        return currentSpellChecker;
    }

    public android.view.textservice.SpellCheckerInfo getCurrentSpellChecker(int userId, java.lang.String locale) {
        verifyUser(userId);
        synchronized (this.mLock) {
            com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = getDataFromCallingUserIdLocked(userId);
            if (tsd == null) {
                return null;
            }
            return tsd.getCurrentSpellChecker();
        }
    }

    public android.view.textservice.SpellCheckerSubtype getCurrentSpellCheckerSubtype(int userId, boolean allowImplicitlySelectedSubtype) {
        verifyUser(userId);
        synchronized (this.mLock) {
            com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = getDataFromCallingUserIdLocked(userId);
            if (tsd == null) {
                return null;
            }
            int subtypeHashCode = tsd.getSelectedSpellCheckerSubtype(0);
            android.view.textservice.SpellCheckerInfo sci = tsd.getCurrentSpellChecker();
            java.util.Locale systemLocale = this.mContext.getResources().getConfiguration().locale;
            if (sci == null || sci.getSubtypeCount() == 0) {
                return null;
            }
            if (subtypeHashCode == 0 && !allowImplicitlySelectedSubtype) {
                return null;
            }
            int numSubtypes = sci.getSubtypeCount();
            if (subtypeHashCode != 0) {
                for (int i = 0; i < numSubtypes; i++) {
                    android.view.textservice.SpellCheckerSubtype scs = sci.getSubtypeAt(i);
                    if (scs.hashCode() == subtypeHashCode) {
                        return scs;
                    }
                }
                return null;
            }
            if (systemLocale == null) {
                return null;
            }
            android.view.textservice.SpellCheckerSubtype firstLanguageMatchingSubtype = null;
            for (int i2 = 0; i2 < sci.getSubtypeCount(); i2++) {
                android.view.textservice.SpellCheckerSubtype scs2 = sci.getSubtypeAt(i2);
                java.util.Locale scsLocale = scs2.getLocaleObject();
                if (java.util.Objects.equals(scsLocale, systemLocale)) {
                    return scs2;
                }
                if (firstLanguageMatchingSubtype == null && scsLocale != null && android.text.TextUtils.equals(systemLocale.getLanguage(), scsLocale.getLanguage())) {
                    firstLanguageMatchingSubtype = scs2;
                }
            }
            return firstLanguageMatchingSubtype;
        }
    }

    public void getSpellCheckerService(int userId, java.lang.String sciId, java.lang.String locale, com.android.internal.textservice.ITextServicesSessionListener tsListener, com.android.internal.textservice.ISpellCheckerSessionListener scListener, android.os.Bundle bundle, int supportedAttributes) throws java.lang.Throwable {
        com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup bindGroup;
        verifyUser(userId);
        if (android.text.TextUtils.isEmpty(sciId) || tsListener == null || scListener == null) {
            android.util.Slog.e(TAG, "getSpellCheckerService: Invalid input.");
            return;
        }
        synchronized (this.mLock) {
            try {
                com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = getDataFromCallingUserIdLocked(userId);
                if (tsd == null) {
                    return;
                }
                java.util.HashMap<java.lang.String, android.view.textservice.SpellCheckerInfo> spellCheckerMap = tsd.mSpellCheckerMap;
                if (spellCheckerMap.containsKey(sciId)) {
                    android.view.textservice.SpellCheckerInfo sci = spellCheckerMap.get(sciId);
                    int uid = android.os.Binder.getCallingUid();
                    try {
                        if (canCallerAccessSpellChecker(sci, uid, userId)) {
                            java.util.HashMap<java.lang.String, com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> spellCheckerBindGroups = tsd.mSpellCheckerBindGroups;
                            com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup bindGroup2 = spellCheckerBindGroups.get(sciId);
                            if (bindGroup2 != null) {
                                bindGroup = bindGroup2;
                            } else {
                                long ident = android.os.Binder.clearCallingIdentity();
                                try {
                                    com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup bindGroup3 = startSpellCheckerServiceInnerLocked(sci, tsd);
                                    if (bindGroup3 == null) {
                                        return;
                                    } else {
                                        bindGroup = bindGroup3;
                                    }
                                } finally {
                                    android.os.Binder.restoreCallingIdentity(ident);
                                }
                            }
                            bindGroup.getISpellCheckerSessionOrQueueLocked(new com.android.server.textservices.TextServicesManagerService.SessionRequest(uid, locale, tsListener, scListener, bundle, supportedAttributes));
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    public boolean isSpellCheckerEnabled(int userId) {
        verifyUser(userId);
        synchronized (this.mLock) {
            com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = getDataFromCallingUserIdLocked(userId);
            if (tsd == null) {
                return false;
            }
            return tsd.isSpellCheckerEnabled();
        }
    }

    private com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup startSpellCheckerServiceInnerLocked(android.view.textservice.SpellCheckerInfo info, com.android.server.textservices.TextServicesManagerService.TextServicesData tsd) {
        java.lang.String sciId = info.getId();
        com.android.server.textservices.TextServicesManagerService.InternalServiceConnection connection = new com.android.server.textservices.TextServicesManagerService.InternalServiceConnection(sciId, tsd.mSpellCheckerBindGroups);
        android.content.Intent serviceIntent = new android.content.Intent("android.service.textservice.SpellCheckerService");
        serviceIntent.setComponent(info.getComponent());
        if (!bindCurrentSpellCheckerService(serviceIntent, connection, 8388609, tsd.mUserId)) {
            android.util.Slog.e(TAG, "Failed to get a spell checker service.");
            return null;
        }
        com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup group = new com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup(connection);
        tsd.mSpellCheckerBindGroups.put(sciId, group);
        return group;
    }

    public android.view.textservice.SpellCheckerInfo[] getEnabledSpellCheckers(int userId) {
        verifyUser(userId);
        synchronized (this.mLock) {
            com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = getDataFromCallingUserIdLocked(userId);
            if (tsd == null) {
                return null;
            }
            java.util.ArrayList<android.view.textservice.SpellCheckerInfo> spellCheckerList = new java.util.ArrayList<>(tsd.mSpellCheckerList);
            int size = spellCheckerList.size();
            int callingUid = android.os.Binder.getCallingUid();
            for (int i = size - 1; i >= 0; i--) {
                if (!canCallerAccessSpellChecker(spellCheckerList.get(i), callingUid, userId)) {
                    spellCheckerList.remove(i);
                }
            }
            if (spellCheckerList.isEmpty()) {
                return null;
            }
            return (android.view.textservice.SpellCheckerInfo[]) spellCheckerList.toArray(new android.view.textservice.SpellCheckerInfo[spellCheckerList.size()]);
        }
    }

    public void finishSpellCheckerService(int userId, com.android.internal.textservice.ISpellCheckerSessionListener listener) {
        verifyUser(userId);
        synchronized (this.mLock) {
            com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = getDataFromCallingUserIdLocked(userId);
            if (tsd == null) {
                return;
            }
            java.util.ArrayList<com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> removeList = new java.util.ArrayList<>();
            java.util.HashMap<java.lang.String, com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> spellCheckerBindGroups = tsd.mSpellCheckerBindGroups;
            for (com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup group : spellCheckerBindGroups.values()) {
                if (group != null) {
                    removeList.add(group);
                }
            }
            int removeSize = removeList.size();
            for (int i = 0; i < removeSize; i++) {
                removeList.get(i).removeListener(listener);
            }
        }
    }

    private void verifyUser(int userId) {
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (userId != callingUserId) {
            this.mContext.enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "Cross-user interaction requires INTERACT_ACROSS_USERS_FULL. userId=" + userId + " callingUserId=" + callingUserId);
        }
    }

    private boolean canCallerAccessSpellChecker(android.view.textservice.SpellCheckerInfo sci, int callingUid, int userId) {
        android.view.textservice.SpellCheckerInfo currentSci = getCurrentSpellCheckerForUser(userId);
        if (currentSci != null && currentSci.getId().equals(sci.getId())) {
            return true;
        }
        android.content.pm.PackageManagerInternal pmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        return true ^ pmInternal.filterAppAccess(sci.getPackageName(), callingUid, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentSpellCheckerLocked(android.view.textservice.SpellCheckerInfo sci, com.android.server.textservices.TextServicesManagerService.TextServicesData tsd) {
        if (sci != null) {
            sci.getId();
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            tsd.setCurrentSpellChecker(sci);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            if (args.length == 0 || (args.length == 1 && args[0].equals("-a"))) {
                synchronized (this.mLock) {
                    pw.println("Current Text Services Manager state:");
                    pw.println("  Users:");
                    int numOfUsers = this.mUserData.size();
                    for (int i = 0; i < numOfUsers; i++) {
                        this.mUserData.valueAt(i).dump(pw);
                    }
                }
                return;
            }
            if (args.length != 2 || !args[0].equals("--user")) {
                pw.println("Invalid arguments to text services.");
                return;
            }
            int userId = java.lang.Integer.parseInt(args[1]);
            android.content.pm.UserInfo userInfo = this.mUserManager.getUserInfo(userId);
            if (userInfo == null) {
                pw.println("Non-existent user.");
                return;
            }
            com.android.server.textservices.TextServicesManagerService.TextServicesData tsd = this.mUserData.get(userId);
            if (tsd == null) {
                pw.println("User needs to unlock first.");
                return;
            }
            synchronized (this.mLock) {
                pw.println("Current Text Services Manager state:");
                pw.println("  User " + userId + ":");
                tsd.dump(pw);
            }
        }
    }

    private com.android.server.textservices.TextServicesManagerService.TextServicesData getDataFromCallingUserIdLocked(int callingUserId) {
        return this.mUserData.get(callingUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SessionRequest {
        public final android.os.Bundle mBundle;
        public final java.lang.String mLocale;
        public final com.android.internal.textservice.ISpellCheckerSessionListener mScListener;
        public final int mSupportedAttributes;
        public final com.android.internal.textservice.ITextServicesSessionListener mTsListener;
        public final int mUid;

        SessionRequest(int uid, java.lang.String locale, com.android.internal.textservice.ITextServicesSessionListener tsListener, com.android.internal.textservice.ISpellCheckerSessionListener scListener, android.os.Bundle bundle, int supportedAttributes) {
            this.mUid = uid;
            this.mLocale = locale;
            this.mTsListener = tsListener;
            this.mScListener = scListener;
            this.mBundle = bundle;
            this.mSupportedAttributes = supportedAttributes;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class SpellCheckerBindGroup {
        private boolean mConnected;
        private final com.android.server.textservices.TextServicesManagerService.InternalServiceConnection mInternalConnection;
        private com.android.internal.textservice.ISpellCheckerService mSpellChecker;
        java.util.HashMap<java.lang.String, com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> mSpellCheckerBindGroups;
        private boolean mUnbindCalled;
        private final java.lang.String TAG = com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup.class.getSimpleName();
        private final java.util.ArrayList<com.android.server.textservices.TextServicesManagerService.SessionRequest> mPendingSessionRequests = new java.util.ArrayList<>();
        private final java.util.ArrayList<com.android.server.textservices.TextServicesManagerService.SessionRequest> mOnGoingSessionRequests = new java.util.ArrayList<>();
        private final com.android.server.textservices.TextServicesManagerService.InternalDeathRecipients mListeners = new com.android.server.textservices.TextServicesManagerService.InternalDeathRecipients(this);

        public SpellCheckerBindGroup(com.android.server.textservices.TextServicesManagerService.InternalServiceConnection connection) {
            this.mInternalConnection = connection;
            this.mSpellCheckerBindGroups = connection.mSpellCheckerBindGroups;
        }

        public void onServiceConnectedLocked(com.android.internal.textservice.ISpellCheckerService spellChecker) {
            if (this.mUnbindCalled) {
                return;
            }
            this.mSpellChecker = spellChecker;
            this.mConnected = true;
            try {
                int size = this.mPendingSessionRequests.size();
                for (int i = 0; i < size; i++) {
                    com.android.server.textservices.TextServicesManagerService.SessionRequest request = this.mPendingSessionRequests.get(i);
                    this.mSpellChecker.getISpellCheckerSession(request.mLocale, request.mScListener, request.mBundle, request.mSupportedAttributes, new com.android.server.textservices.TextServicesManagerService.ISpellCheckerServiceCallbackBinder(this, request));
                    this.mOnGoingSessionRequests.add(request);
                }
                this.mPendingSessionRequests.clear();
            } catch (android.os.RemoteException e) {
                removeAllLocked();
            }
            cleanLocked();
        }

        public void onServiceDisconnectedLocked() {
            this.mSpellChecker = null;
            this.mConnected = false;
        }

        public void removeListener(com.android.internal.textservice.ISpellCheckerSessionListener listener) {
            synchronized (com.android.server.textservices.TextServicesManagerService.this.mLock) {
                this.mListeners.unregister(listener);
                final android.os.IBinder scListenerBinder = listener.asBinder();
                java.util.function.Predicate<com.android.server.textservices.TextServicesManagerService.SessionRequest> removeCondition = new java.util.function.Predicate() { // from class: com.android.server.textservices.TextServicesManagerService$SpellCheckerBindGroup$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup.lambda$removeListener$0(scListenerBinder, (com.android.server.textservices.TextServicesManagerService.SessionRequest) obj);
                    }
                };
                this.mPendingSessionRequests.removeIf(removeCondition);
                this.mOnGoingSessionRequests.removeIf(removeCondition);
                cleanLocked();
            }
        }

        static /* synthetic */ boolean lambda$removeListener$0(android.os.IBinder scListenerBinder, com.android.server.textservices.TextServicesManagerService.SessionRequest request) {
            return request.mScListener.asBinder() == scListenerBinder;
        }

        private void cleanLocked() {
            if (this.mUnbindCalled || this.mListeners.getRegisteredCallbackCount() > 0 || !this.mPendingSessionRequests.isEmpty() || !this.mOnGoingSessionRequests.isEmpty()) {
                return;
            }
            java.lang.String sciId = this.mInternalConnection.mSciId;
            com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup cur = this.mSpellCheckerBindGroups.get(sciId);
            if (cur == this) {
                this.mSpellCheckerBindGroups.remove(sciId);
            }
            com.android.server.textservices.TextServicesManagerService.this.mContext.unbindService(this.mInternalConnection);
            this.mUnbindCalled = true;
        }

        public void removeAllLocked() {
            android.util.Slog.e(this.TAG, "Remove the spell checker bind unexpectedly.");
            int size = this.mListeners.getRegisteredCallbackCount();
            for (int i = size - 1; i >= 0; i--) {
                this.mListeners.unregister(this.mListeners.getRegisteredCallbackItem(i));
            }
            this.mPendingSessionRequests.clear();
            this.mOnGoingSessionRequests.clear();
            cleanLocked();
        }

        public void getISpellCheckerSessionOrQueueLocked(com.android.server.textservices.TextServicesManagerService.SessionRequest request) {
            if (this.mUnbindCalled) {
                return;
            }
            this.mListeners.register(request.mScListener);
            if (!this.mConnected) {
                this.mPendingSessionRequests.add(request);
                return;
            }
            try {
                this.mSpellChecker.getISpellCheckerSession(request.mLocale, request.mScListener, request.mBundle, request.mSupportedAttributes, new com.android.server.textservices.TextServicesManagerService.ISpellCheckerServiceCallbackBinder(this, request));
                this.mOnGoingSessionRequests.add(request);
            } catch (android.os.RemoteException e) {
                removeAllLocked();
            }
            cleanLocked();
        }

        void onSessionCreated(com.android.internal.textservice.ISpellCheckerSession newSession, com.android.server.textservices.TextServicesManagerService.SessionRequest request) {
            synchronized (com.android.server.textservices.TextServicesManagerService.this.mLock) {
                if (this.mUnbindCalled) {
                    return;
                }
                if (this.mOnGoingSessionRequests.remove(request)) {
                    try {
                        request.mTsListener.onServiceConnected(newSession);
                    } catch (android.os.RemoteException e) {
                    }
                }
                cleanLocked();
            }
        }
    }

    private final class InternalServiceConnection implements android.content.ServiceConnection {
        private final java.lang.String mSciId;
        private final java.util.HashMap<java.lang.String, com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> mSpellCheckerBindGroups;

        public InternalServiceConnection(java.lang.String id, java.util.HashMap<java.lang.String, com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> spellCheckerBindGroups) {
            this.mSciId = id;
            this.mSpellCheckerBindGroups = spellCheckerBindGroups;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized (com.android.server.textservices.TextServicesManagerService.this.mLock) {
                onServiceConnectedInnerLocked(name, service);
            }
        }

        private void onServiceConnectedInnerLocked(android.content.ComponentName name, android.os.IBinder service) {
            com.android.internal.textservice.ISpellCheckerService spellChecker = com.android.internal.textservice.ISpellCheckerService.Stub.asInterface(service);
            com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup group = this.mSpellCheckerBindGroups.get(this.mSciId);
            if (group != null && this == group.mInternalConnection) {
                group.onServiceConnectedLocked(spellChecker);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            synchronized (com.android.server.textservices.TextServicesManagerService.this.mLock) {
                onServiceDisconnectedInnerLocked(name);
            }
        }

        private void onServiceDisconnectedInnerLocked(android.content.ComponentName name) {
            com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup group = this.mSpellCheckerBindGroups.get(this.mSciId);
            if (group != null && this == group.mInternalConnection) {
                group.onServiceDisconnectedLocked();
            }
        }
    }

    private static final class InternalDeathRecipients extends android.os.RemoteCallbackList<com.android.internal.textservice.ISpellCheckerSessionListener> {
        private final com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup mGroup;

        public InternalDeathRecipients(com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup group) {
            this.mGroup = group;
        }

        @Override // android.os.RemoteCallbackList
        public void onCallbackDied(com.android.internal.textservice.ISpellCheckerSessionListener listener) {
            this.mGroup.removeListener(listener);
        }
    }

    private static final class ISpellCheckerServiceCallbackBinder extends com.android.internal.textservice.ISpellCheckerServiceCallback.Stub {
        private java.lang.ref.WeakReference<com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup> mBindGroup;
        private final java.lang.Object mCallbackLock = new java.lang.Object();
        private java.lang.ref.WeakReference<com.android.server.textservices.TextServicesManagerService.SessionRequest> mRequest;

        ISpellCheckerServiceCallbackBinder(com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup bindGroup, com.android.server.textservices.TextServicesManagerService.SessionRequest request) {
            synchronized (this.mCallbackLock) {
                this.mBindGroup = new java.lang.ref.WeakReference<>(bindGroup);
                this.mRequest = new java.lang.ref.WeakReference<>(request);
            }
        }

        public void onSessionCreated(com.android.internal.textservice.ISpellCheckerSession newSession) {
            synchronized (this.mCallbackLock) {
                if (this.mBindGroup != null && this.mRequest != null) {
                    com.android.server.textservices.TextServicesManagerService.SpellCheckerBindGroup group = this.mBindGroup.get();
                    com.android.server.textservices.TextServicesManagerService.SessionRequest request = this.mRequest.get();
                    this.mBindGroup = null;
                    this.mRequest = null;
                    if (group != null && request != null) {
                        group.onSessionCreated(newSession, request);
                    }
                }
            }
        }
    }
}
