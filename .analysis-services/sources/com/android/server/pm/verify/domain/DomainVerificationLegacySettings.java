package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationLegacySettings {
    public static final java.lang.String ATTR_PACKAGE_NAME = "packageName";
    public static final java.lang.String ATTR_STATE = "state";
    public static final java.lang.String ATTR_USER_ID = "userId";
    public static final java.lang.String TAG_DOMAIN_VERIFICATIONS_LEGACY = "domain-verifications-legacy";
    public static final java.lang.String TAG_USER_STATE = "user-state";
    public static final java.lang.String TAG_USER_STATES = "user-states";
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState> mStates = new android.util.ArrayMap<>();

    public void add(java.lang.String packageName, android.content.pm.IntentFilterVerificationInfo info) {
        synchronized (this.mLock) {
            getOrCreateStateLocked(packageName).setInfo(info);
        }
    }

    public void add(java.lang.String packageName, int userId, int state) {
        synchronized (this.mLock) {
            getOrCreateStateLocked(packageName).addUserState(userId, state);
        }
    }

    public int getUserState(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState state = this.mStates.get(packageName);
            if (state != null) {
                return state.getUserState(userId);
            }
            return 0;
        }
    }

    public android.util.SparseIntArray getUserStates(java.lang.String packageName) {
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState state = this.mStates.get(packageName);
            if (state != null) {
                return state.getUserStates();
            }
            return null;
        }
    }

    public android.content.pm.IntentFilterVerificationInfo remove(java.lang.String packageName) {
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState state = this.mStates.get(packageName);
            if (state != null && !state.isAttached()) {
                state.markAttached();
                return state.getInfo();
            }
            return null;
        }
    }

    private com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState getOrCreateStateLocked(java.lang.String packageName) {
        com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState state = this.mStates.get(packageName);
        if (state == null) {
            com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState state2 = new com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState();
            this.mStates.put(packageName, state2);
            return state2;
        }
        return state;
    }

    public void writeSettings(com.android.modules.utils.TypedXmlSerializer xmlSerializer) throws java.io.IOException {
        com.android.server.pm.SettingsXml.Serializer serializer = com.android.server.pm.SettingsXml.serializer(xmlSerializer);
        try {
            com.android.server.pm.SettingsXml.WriteSection ignored = serializer.startSection(TAG_DOMAIN_VERIFICATIONS_LEGACY);
            try {
                synchronized (this.mLock) {
                    int statesSize = this.mStates.size();
                    for (int stateIndex = 0; stateIndex < statesSize; stateIndex++) {
                        com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState state = this.mStates.valueAt(stateIndex);
                        android.util.SparseIntArray userStates = state.getUserStates();
                        if (userStates != null) {
                            java.lang.String packageName = this.mStates.keyAt(stateIndex);
                            com.android.server.pm.SettingsXml.WriteSection userStatesSection = serializer.startSection(TAG_USER_STATES).attribute(ATTR_PACKAGE_NAME, packageName);
                            try {
                                int userStatesSize = userStates.size();
                                for (int userStateIndex = 0; userStateIndex < userStatesSize; userStateIndex++) {
                                    int userId = userStates.keyAt(userStateIndex);
                                    int userState = userStates.valueAt(userStateIndex);
                                    userStatesSection.startSection("user-state").attribute("userId", userId).attribute("state", userState).finish();
                                }
                                if (userStatesSection != null) {
                                    userStatesSection.close();
                                }
                            } finally {
                            }
                        }
                    }
                }
                if (ignored != null) {
                    ignored.close();
                }
                if (serializer != null) {
                    serializer.close();
                }
            } finally {
            }
        } finally {
        }
    }

    public void readSettings(com.android.modules.utils.TypedXmlPullParser xmlParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.pm.SettingsXml.ChildSection child = com.android.server.pm.SettingsXml.parser(xmlParser).children();
        while (child.moveToNext()) {
            if (TAG_USER_STATES.equals(child.getName())) {
                readUserStates(child);
            }
        }
    }

    private void readUserStates(com.android.server.pm.SettingsXml.ReadSection section) {
        java.lang.String packageName = section.getString(ATTR_PACKAGE_NAME);
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState legacyState = getOrCreateStateLocked(packageName);
            com.android.server.pm.SettingsXml.ChildSection child = section.children();
            while (child.moveToNext()) {
                if ("user-state".equals(child.getName())) {
                    readUserState(child, legacyState);
                }
            }
        }
    }

    private void readUserState(com.android.server.pm.SettingsXml.ReadSection section, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.LegacyState legacyState) {
        int userId = section.getInt("userId");
        int state = section.getInt("state");
        legacyState.addUserState(userId, state);
    }

    static class LegacyState {
        private boolean attached;
        private android.content.pm.IntentFilterVerificationInfo mInfo;
        private android.util.SparseIntArray mUserStates;

        LegacyState() {
        }

        public android.content.pm.IntentFilterVerificationInfo getInfo() {
            return this.mInfo;
        }

        public int getUserState(int userId) {
            return this.mUserStates.get(userId, 0);
        }

        public android.util.SparseIntArray getUserStates() {
            return this.mUserStates;
        }

        public void setInfo(android.content.pm.IntentFilterVerificationInfo info) {
            this.mInfo = info;
        }

        public void addUserState(int userId, int state) {
            if (this.mUserStates == null) {
                this.mUserStates = new android.util.SparseIntArray(1);
            }
            this.mUserStates.put(userId, state);
        }

        public boolean isAttached() {
            return this.attached;
        }

        public void markAttached() {
            this.attached = true;
        }
    }
}
