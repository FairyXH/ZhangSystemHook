package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class CredentialDescriptionRegistry {
    private static final int MAX_ALLOWED_CREDENTIAL_DESCRIPTIONS = 128;
    private static final int MAX_ALLOWED_ENTRIES_PER_PROVIDER = 16;
    private static final android.util.SparseArray<com.android.server.credentials.CredentialDescriptionRegistry> sCredentialDescriptionSessionPerUser = new android.util.SparseArray<>();
    private static final java.util.concurrent.locks.ReentrantLock sLock = new java.util.concurrent.locks.ReentrantLock();
    private java.util.Map<java.lang.String, java.util.Set<android.credentials.CredentialDescription>> mCredentialDescriptions = new java.util.HashMap();
    private int mTotalDescriptionCount = 0;

    public static final class FilterResult {
        final java.util.List<android.service.credentials.CredentialEntry> mCredentialEntries;
        final java.util.Set<java.lang.String> mElementKeys;
        final java.lang.String mPackageName;

        FilterResult(java.lang.String packageName, java.util.Set<java.lang.String> elementKeys, java.util.List<android.service.credentials.CredentialEntry> credentialEntries) {
            this.mPackageName = packageName;
            this.mElementKeys = elementKeys;
            this.mCredentialEntries = credentialEntries;
        }
    }

    public static com.android.server.credentials.CredentialDescriptionRegistry forUser(int userId) {
        sLock.lock();
        try {
            com.android.server.credentials.CredentialDescriptionRegistry session = sCredentialDescriptionSessionPerUser.get(userId, null);
            if (session == null) {
                session = new com.android.server.credentials.CredentialDescriptionRegistry();
                sCredentialDescriptionSessionPerUser.put(userId, session);
            }
            return session;
        } finally {
            sLock.unlock();
        }
    }

    public static void clearUserSession(int userId) {
        sLock.lock();
        try {
            sCredentialDescriptionSessionPerUser.remove(userId);
        } finally {
            sLock.unlock();
        }
    }

    static void clearAllSessions() {
        sLock.lock();
        try {
            sCredentialDescriptionSessionPerUser.clear();
        } finally {
            sLock.unlock();
        }
    }

    static void setSession(int userId, com.android.server.credentials.CredentialDescriptionRegistry credentialDescriptionRegistry) {
        sLock.lock();
        try {
            sCredentialDescriptionSessionPerUser.put(userId, credentialDescriptionRegistry);
        } finally {
            sLock.unlock();
        }
    }

    private CredentialDescriptionRegistry() {
    }

    public void executeRegisterRequest(android.credentials.RegisterCredentialDescriptionRequest request, java.lang.String callingPackageName) {
        if (!this.mCredentialDescriptions.containsKey(callingPackageName)) {
            this.mCredentialDescriptions.put(callingPackageName, new java.util.HashSet());
        }
        if (this.mTotalDescriptionCount <= 128 && this.mCredentialDescriptions.get(callingPackageName).size() <= 16) {
            java.util.Set<android.credentials.CredentialDescription> descriptions = request.getCredentialDescriptions();
            int size = this.mCredentialDescriptions.get(callingPackageName).size();
            this.mCredentialDescriptions.get(callingPackageName).addAll(descriptions);
            this.mTotalDescriptionCount += this.mCredentialDescriptions.get(callingPackageName).size() - size;
        }
    }

    public void executeUnregisterRequest(android.credentials.UnregisterCredentialDescriptionRequest request, java.lang.String callingPackageName) {
        if (this.mCredentialDescriptions.containsKey(callingPackageName)) {
            int size = this.mCredentialDescriptions.get(callingPackageName).size();
            this.mCredentialDescriptions.get(callingPackageName).removeAll(request.getCredentialDescriptions());
            this.mTotalDescriptionCount -= size - this.mCredentialDescriptions.get(callingPackageName).size();
        }
    }

    public java.util.Set<com.android.server.credentials.CredentialDescriptionRegistry.FilterResult> getFilteredResultForProvider(java.lang.String packageName, java.util.Set<java.lang.String> requestedKeyElements) {
        java.util.Set<com.android.server.credentials.CredentialDescriptionRegistry.FilterResult> result = new java.util.HashSet<>();
        if (!this.mCredentialDescriptions.containsKey(packageName)) {
            return result;
        }
        java.util.Set<android.credentials.CredentialDescription> currentSet = this.mCredentialDescriptions.get(packageName);
        for (android.credentials.CredentialDescription containedDescription : currentSet) {
            if (checkForMatch(containedDescription.getSupportedElementKeys(), requestedKeyElements)) {
                result.add(new com.android.server.credentials.CredentialDescriptionRegistry.FilterResult(packageName, containedDescription.getSupportedElementKeys(), containedDescription.getCredentialEntries()));
            }
        }
        return result;
    }

    public java.util.Set<com.android.server.credentials.CredentialDescriptionRegistry.FilterResult> getMatchingProviders(java.util.Set<java.util.Set<java.lang.String>> supportedElementKeys) {
        java.util.Set<com.android.server.credentials.CredentialDescriptionRegistry.FilterResult> result = new java.util.HashSet<>();
        for (java.lang.String packageName : this.mCredentialDescriptions.keySet()) {
            java.util.Set<android.credentials.CredentialDescription> currentSet = this.mCredentialDescriptions.get(packageName);
            for (android.credentials.CredentialDescription containedDescription : currentSet) {
                if (canProviderSatisfyAny(containedDescription.getSupportedElementKeys(), supportedElementKeys)) {
                    result.add(new com.android.server.credentials.CredentialDescriptionRegistry.FilterResult(packageName, containedDescription.getSupportedElementKeys(), containedDescription.getCredentialEntries()));
                }
            }
        }
        return result;
    }

    void evictProviderWithPackageName(java.lang.String packageName) {
        if (this.mCredentialDescriptions.containsKey(packageName)) {
            this.mCredentialDescriptions.remove(packageName);
        }
    }

    private static boolean canProviderSatisfyAny(java.util.Set<java.lang.String> registeredElementKeys, java.util.Set<java.util.Set<java.lang.String>> requestedElementKeys) {
        for (java.util.Set<java.lang.String> requestedUnflattenedString : requestedElementKeys) {
            if (registeredElementKeys.containsAll(requestedUnflattenedString)) {
                return true;
            }
        }
        return false;
    }

    static boolean checkForMatch(java.util.Set<java.lang.String> registeredElementKeys, java.util.Set<java.lang.String> requestedElementKeys) {
        return registeredElementKeys.containsAll(requestedElementKeys);
    }
}
