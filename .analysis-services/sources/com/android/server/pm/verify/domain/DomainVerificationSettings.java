package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
class DomainVerificationSettings {
    private final com.android.server.pm.verify.domain.DomainVerificationCollector mCollector;
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> mPendingPkgStates = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> mRestoredPkgStates = new android.util.ArrayMap<>();
    private final java.lang.Object mLock = new java.lang.Object();

    public DomainVerificationSettings(com.android.server.pm.verify.domain.DomainVerificationCollector collector) {
        this.mCollector = collector;
    }

    public void writeSettings(com.android.modules.utils.TypedXmlSerializer xmlSerializer, com.android.server.pm.verify.domain.models.DomainVerificationStateMap<com.android.server.pm.verify.domain.models.DomainVerificationPkgState> liveState, java.util.function.Function<java.lang.String, java.lang.String> pkgSignatureFunction) {
    }

    public void writeSettings(com.android.modules.utils.TypedXmlSerializer xmlSerializer, com.android.server.pm.verify.domain.models.DomainVerificationStateMap<com.android.server.pm.verify.domain.models.DomainVerificationPkgState> liveState, int userId, java.util.function.Function<java.lang.String, java.lang.String> pkgSignatureFunction) throws java.io.IOException {
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.DomainVerificationPersistence.writeToXml(xmlSerializer, liveState, this.mPendingPkgStates, this.mRestoredPkgStates, userId, pkgSignatureFunction);
        }
    }

    public void readSettings(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.pm.verify.domain.models.DomainVerificationStateMap<com.android.server.pm.verify.domain.models.DomainVerificationPkgState> liveState, com.android.server.pm.Computer snapshot) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.pm.verify.domain.DomainVerificationPersistence.ReadResult result = com.android.server.pm.verify.domain.DomainVerificationPersistence.readFromXml(parser);
        android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> active = result.active;
        android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> restored = result.restored;
        synchronized (this.mLock) {
            int activeSize = active.size();
            for (int activeIndex = 0; activeIndex < activeSize; activeIndex++) {
                com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = active.valueAt(activeIndex);
                java.lang.String pkgName = pkgState.getPackageName();
                com.android.server.pm.verify.domain.models.DomainVerificationPkgState existingState = liveState.get(pkgName);
                if (existingState != null) {
                    if (!existingState.getId().equals(pkgState.getId())) {
                        mergePkgState(existingState, pkgState, snapshot);
                    }
                } else {
                    this.mPendingPkgStates.put(pkgName, pkgState);
                }
            }
            int restoredSize = restored.size();
            for (int restoredIndex = 0; restoredIndex < restoredSize; restoredIndex++) {
                com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState2 = restored.valueAt(restoredIndex);
                this.mRestoredPkgStates.put(pkgState2.getPackageName(), pkgState2);
            }
        }
    }

    public void restoreSettings(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.pm.verify.domain.models.DomainVerificationStateMap<com.android.server.pm.verify.domain.models.DomainVerificationPkgState> liveState, com.android.server.pm.Computer snapshot) throws java.lang.Throwable {
        com.android.server.pm.verify.domain.models.DomainVerificationPkgState newState;
        java.lang.String pkgName;
        com.android.server.pm.verify.domain.DomainVerificationPersistence.ReadResult result = com.android.server.pm.verify.domain.DomainVerificationPersistence.readFromXml(parser);
        android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> stateList = result.restored;
        stateList.putAll((android.util.ArrayMap<? extends java.lang.String, ? extends com.android.server.pm.verify.domain.models.DomainVerificationPkgState>) result.active);
        synchronized (this.mLock) {
            for (int stateIndex = 0; stateIndex < stateList.size(); stateIndex++) {
                try {
                    newState = stateList.valueAt(stateIndex);
                    pkgName = newState.getPackageName();
                } catch (java.lang.Throwable th) {
                    th = th;
                }
                try {
                    com.android.server.pm.verify.domain.models.DomainVerificationPkgState existingState = liveState.get(pkgName);
                    if (existingState == null) {
                        existingState = this.mPendingPkgStates.get(pkgName);
                    }
                    if (existingState == null) {
                        existingState = this.mRestoredPkgStates.get(pkgName);
                    }
                    if (existingState != null) {
                        try {
                            mergePkgState(existingState, newState, snapshot);
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            throw th;
                        }
                    } else {
                        android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap = newState.getStateMap();
                        int size = stateMap.size();
                        for (int index = size - 1; index >= 0; index--) {
                            java.lang.Integer stateInteger = stateMap.valueAt(index);
                            if (stateInteger != null) {
                                int state = stateInteger.intValue();
                                if (state == 1 || state == 5) {
                                    stateMap.setValueAt(index, 5);
                                } else {
                                    stateMap.removeAt(index);
                                }
                            }
                        }
                        this.mRestoredPkgStates.put(pkgName, newState);
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
    }

    public void mergePkgState(com.android.server.pm.verify.domain.models.DomainVerificationPkgState oldState, com.android.server.pm.verify.domain.models.DomainVerificationPkgState newState, com.android.server.pm.Computer snapshot) {
        com.android.server.pm.pkg.PackageStateInternal pkgSetting;
        java.lang.Integer oldStateCode;
        com.android.server.pm.pkg.PackageStateInternal pkgSetting2 = snapshot.getPackageStateInternal(oldState.getPackageName());
        com.android.server.pm.pkg.AndroidPackage pkg = pkgSetting2 == null ? null : pkgSetting2.getPkg();
        java.util.Set<java.lang.String> validDomains = pkg == null ? java.util.Collections.emptySet() : this.mCollector.collectValidAutoVerifyDomains(pkg);
        android.util.ArrayMap<java.lang.String, java.lang.Integer> oldStateMap = oldState.getStateMap();
        android.util.ArrayMap<java.lang.String, java.lang.Integer> newStateMap = newState.getStateMap();
        int size = newStateMap.size();
        for (int index = 0; index < size; index++) {
            java.lang.String domain = newStateMap.keyAt(index);
            java.lang.Integer newStateCode = newStateMap.valueAt(index);
            if (validDomains.contains(domain) && (((oldStateCode = oldStateMap.get(domain)) == null || oldStateCode.intValue() == 0) && (newStateCode.intValue() == 1 || newStateCode.intValue() == 5))) {
                oldStateMap.put(domain, 5);
            }
        }
        android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> oldSelectionStates = oldState.getUserStates();
        android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> newSelectionStates = newState.getUserStates();
        int userStateSize = newSelectionStates.size();
        int index2 = 0;
        while (index2 < userStateSize) {
            int userId = newSelectionStates.keyAt(index2);
            com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState newUserState = newSelectionStates.valueAt(index2);
            if (newUserState == null) {
                pkgSetting = pkgSetting2;
            } else {
                android.util.ArraySet<java.lang.String> newEnabledHosts = newUserState.getEnabledHosts();
                com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState oldUserState = oldSelectionStates.get(userId);
                pkgSetting = pkgSetting2;
                boolean linkHandlingAllowed = newUserState.isLinkHandlingAllowed();
                if (oldUserState == null) {
                    oldSelectionStates.put(userId, new com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState(userId, newEnabledHosts, linkHandlingAllowed));
                } else {
                    oldUserState.addHosts(newEnabledHosts).setLinkHandlingAllowed(linkHandlingAllowed);
                }
            }
            index2++;
            pkgSetting2 = pkgSetting;
        }
    }

    public void removePackage(java.lang.String packageName) {
        synchronized (this.mLock) {
            this.mPendingPkgStates.remove(packageName);
            this.mRestoredPkgStates.remove(packageName);
        }
    }

    public void removePackageForUser(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pendingPkgState = this.mPendingPkgStates.get(packageName);
            if (pendingPkgState != null) {
                pendingPkgState.removeUser(userId);
            }
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState restoredPkgState = this.mRestoredPkgStates.get(packageName);
            if (restoredPkgState != null) {
                restoredPkgState.removeUser(userId);
            }
        }
    }

    public void removeUser(int userId) {
        synchronized (this.mLock) {
            int pendingSize = this.mPendingPkgStates.size();
            for (int index = 0; index < pendingSize; index++) {
                this.mPendingPkgStates.valueAt(index).removeUser(userId);
            }
            int restoredSize = this.mRestoredPkgStates.size();
            for (int index2 = 0; index2 < restoredSize; index2++) {
                this.mRestoredPkgStates.valueAt(index2).removeUser(userId);
            }
        }
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationPkgState removePendingState(java.lang.String pkgName) {
        com.android.server.pm.verify.domain.models.DomainVerificationPkgState domainVerificationPkgStateRemove;
        synchronized (this.mLock) {
            domainVerificationPkgStateRemove = this.mPendingPkgStates.remove(pkgName);
        }
        return domainVerificationPkgStateRemove;
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationPkgState removeRestoredState(java.lang.String pkgName) {
        com.android.server.pm.verify.domain.models.DomainVerificationPkgState domainVerificationPkgStateRemove;
        synchronized (this.mLock) {
            domainVerificationPkgStateRemove = this.mRestoredPkgStates.remove(pkgName);
        }
        return domainVerificationPkgStateRemove;
    }
}
