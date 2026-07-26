package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class SecureSettingsWrapper {
    private static final com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter NOOP;
    private static final android.util.SparseArray<com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter> sUserMap;
    private static volatile android.content.ContentResolver sContentResolver = null;
    private static final android.util.ArraySet<java.lang.String> CLONE_TO_MANAGED_PROFILE = new android.util.ArraySet<>();

    private interface ReaderWriter {
        int getInt(java.lang.String str, int i);

        java.lang.String getString(java.lang.String str, java.lang.String str2);

        void putInt(java.lang.String str, int i);

        void putString(java.lang.String str, java.lang.String str2);
    }

    static {
        android.provider.Settings.Secure.getCloneToManagedProfileSettings(CLONE_TO_MANAGED_PROFILE);
        sUserMap = new android.util.SparseArray<>();
        NOOP = new com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter() { // from class: com.android.server.inputmethod.SecureSettingsWrapper.1
            @Override // com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
            public void putString(java.lang.String key, java.lang.String str) {
            }

            @Override // com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
            public java.lang.String getString(java.lang.String key, java.lang.String defaultValue) {
                return defaultValue;
            }

            @Override // com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
            public void putInt(java.lang.String key, int value) {
            }

            @Override // com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
            public int getInt(java.lang.String key, int defaultValue) {
                return defaultValue;
            }
        };
    }

    private SecureSettingsWrapper() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getUserIdForClonedSettings(java.lang.String key, int userId) {
        if (CLONE_TO_MANAGED_PROFILE.contains(key)) {
            return ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getProfileParentId(userId);
        }
        return userId;
    }

    private static class UnlockedUserImpl implements com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter {
        private final android.content.ContentResolver mContentResolver;
        private final int mUserId;

        UnlockedUserImpl(int userId, android.content.ContentResolver contentResolver) {
            this.mUserId = userId;
            this.mContentResolver = contentResolver;
        }

        @Override // com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
        public void putString(java.lang.String key, java.lang.String value) {
            int userId = com.android.server.inputmethod.SecureSettingsWrapper.getUserIdForClonedSettings(key, this.mUserId);
            android.provider.Settings.Secure.putStringForUser(this.mContentResolver, key, value, userId);
        }

        @Override // com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
        public java.lang.String getString(java.lang.String key, java.lang.String defaultValue) {
            java.lang.String result = android.provider.Settings.Secure.getStringForUser(this.mContentResolver, key, this.mUserId);
            return result != null ? result : defaultValue;
        }

        @Override // com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
        public void putInt(java.lang.String key, int value) {
            int userId = com.android.server.inputmethod.SecureSettingsWrapper.getUserIdForClonedSettings(key, this.mUserId);
            android.provider.Settings.Secure.putIntForUser(this.mContentResolver, key, value, userId);
        }

        @Override // com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
        public int getInt(java.lang.String key, int defaultValue) {
            return android.provider.Settings.Secure.getIntForUser(this.mContentResolver, key, defaultValue, this.mUserId);
        }
    }

    private static final class LockedUserImpl extends com.android.server.inputmethod.SecureSettingsWrapper.UnlockedUserImpl {
        private final android.util.ArrayMap<java.lang.String, java.lang.String> mNonPersistentKeyValues;

        LockedUserImpl(int userId, android.content.ContentResolver contentResolver) {
            super(userId, contentResolver);
            this.mNonPersistentKeyValues = new android.util.ArrayMap<>();
        }

        @Override // com.android.server.inputmethod.SecureSettingsWrapper.UnlockedUserImpl, com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
        public void putString(java.lang.String key, java.lang.String value) {
            synchronized (this.mNonPersistentKeyValues) {
                this.mNonPersistentKeyValues.put(key, value);
            }
        }

        @Override // com.android.server.inputmethod.SecureSettingsWrapper.UnlockedUserImpl, com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
        public java.lang.String getString(java.lang.String key, java.lang.String defaultValue) {
            synchronized (this.mNonPersistentKeyValues) {
                if (this.mNonPersistentKeyValues.containsKey(key)) {
                    java.lang.String result = this.mNonPersistentKeyValues.get(key);
                    return result != null ? result : defaultValue;
                }
                return super.getString(key, defaultValue);
            }
        }

        @Override // com.android.server.inputmethod.SecureSettingsWrapper.UnlockedUserImpl, com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
        public void putInt(java.lang.String key, int value) {
            synchronized (this.mNonPersistentKeyValues) {
                this.mNonPersistentKeyValues.put(key, java.lang.String.valueOf(value));
            }
        }

        @Override // com.android.server.inputmethod.SecureSettingsWrapper.UnlockedUserImpl, com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter
        public int getInt(java.lang.String key, int defaultValue) {
            synchronized (this.mNonPersistentKeyValues) {
                if (this.mNonPersistentKeyValues.containsKey(key)) {
                    java.lang.String result = this.mNonPersistentKeyValues.get(key);
                    return result != null ? java.lang.Integer.parseInt(result) : defaultValue;
                }
                return super.getInt(key, defaultValue);
            }
        }
    }

    private static com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter createImpl(com.android.server.pm.UserManagerInternal userManagerInternal, int userId) {
        if (userManagerInternal.isUserUnlockingOrUnlocked(userId)) {
            return new com.android.server.inputmethod.SecureSettingsWrapper.UnlockedUserImpl(userId, sContentResolver);
        }
        return new com.android.server.inputmethod.SecureSettingsWrapper.LockedUserImpl(userId, sContentResolver);
    }

    private static com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter putOrGet(int userId, com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter readerWriter) {
        boolean isUnlockedUserImpl = readerWriter instanceof com.android.server.inputmethod.SecureSettingsWrapper.UnlockedUserImpl;
        synchronized (sUserMap) {
            com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter current = sUserMap.get(userId);
            if (current == null) {
                sUserMap.put(userId, readerWriter);
                return readerWriter;
            }
            if (!(current instanceof com.android.server.inputmethod.SecureSettingsWrapper.LockedUserImpl) || !isUnlockedUserImpl) {
                return current;
            }
            sUserMap.put(userId, readerWriter);
            return readerWriter;
        }
    }

    private static com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter get(int userId) {
        synchronized (sUserMap) {
            com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter readerWriter = sUserMap.get(userId);
            if (readerWriter != null) {
                return readerWriter;
            }
            com.android.server.pm.UserManagerInternal userManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            if (!userManagerInternal.exists(userId)) {
                return NOOP;
            }
            return putOrGet(userId, createImpl(userManagerInternal, userId));
        }
    }

    static void onStart(android.content.Context context) {
        sContentResolver = context.getContentResolver();
        final int userId = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getCurrentUserId();
        com.android.server.pm.UserManagerInternal userManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        putOrGet(userId, createImpl(userManagerInternal, userId));
        userManagerInternal.addUserLifecycleListener(new com.android.server.pm.UserManagerInternal.UserLifecycleListener() { // from class: com.android.server.inputmethod.SecureSettingsWrapper.2
            @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
            public void onUserRemoved(android.content.pm.UserInfo user) {
                synchronized (com.android.server.inputmethod.SecureSettingsWrapper.sUserMap) {
                    com.android.server.inputmethod.SecureSettingsWrapper.sUserMap.remove(userId);
                }
            }
        });
    }

    static void onUserStarting(int userId) {
        putOrGet(userId, createImpl((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class), userId));
    }

    static void onUserUnlocking(int userId) {
        com.android.server.inputmethod.SecureSettingsWrapper.ReaderWriter readerWriter = new com.android.server.inputmethod.SecureSettingsWrapper.UnlockedUserImpl(userId, sContentResolver);
        putOrGet(userId, readerWriter);
    }

    static void putString(java.lang.String key, java.lang.String value, int userId) {
        get(userId).putString(key, value);
    }

    static java.lang.String getString(java.lang.String key, java.lang.String defaultValue, int userId) {
        return get(userId).getString(key, defaultValue);
    }

    static void putInt(java.lang.String key, int value, int userId) {
        get(userId).putInt(key, value);
    }

    static int getInt(java.lang.String key, int defaultValue, int userId) {
        return get(userId).getInt(key, defaultValue);
    }

    static void putBoolean(java.lang.String str, boolean z, int i) {
        get(i).putInt(str, z ? 1 : 0);
    }

    static boolean getBoolean(java.lang.String str, boolean z, int i) {
        return get(i).getInt(str, z ? 1 : 0) == 1;
    }
}
