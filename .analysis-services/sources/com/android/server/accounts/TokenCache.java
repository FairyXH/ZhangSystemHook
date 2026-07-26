package com.android.server.accounts;

/* JADX INFO: loaded from: classes.dex */
class TokenCache {
    private static final int MAX_CACHE_CHARS = 64000;
    private com.android.server.accounts.TokenCache.TokenLruCache mCachedTokens = new com.android.server.accounts.TokenCache.TokenLruCache();

    TokenCache() {
    }

    static class Value {
        public final long expiryEpochMillis;
        public final java.lang.String token;

        public Value(java.lang.String token, long expiryEpochMillis) {
            this.token = token;
            this.expiryEpochMillis = expiryEpochMillis;
        }
    }

    private static class Key {
        public final android.accounts.Account account;
        public final java.lang.String packageName;
        public final byte[] sigDigest;
        public final java.lang.String tokenType;

        public Key(android.accounts.Account account, java.lang.String tokenType, java.lang.String packageName, byte[] sigDigest) {
            this.account = account;
            this.tokenType = tokenType;
            this.packageName = packageName;
            this.sigDigest = sigDigest;
        }

        public boolean equals(java.lang.Object o) {
            if (o == null || !(o instanceof com.android.server.accounts.TokenCache.Key)) {
                return false;
            }
            com.android.server.accounts.TokenCache.Key cacheKey = (com.android.server.accounts.TokenCache.Key) o;
            return java.util.Objects.equals(this.account, cacheKey.account) && java.util.Objects.equals(this.packageName, cacheKey.packageName) && java.util.Objects.equals(this.tokenType, cacheKey.tokenType) && java.util.Arrays.equals(this.sigDigest, cacheKey.sigDigest);
        }

        public int hashCode() {
            return ((this.account.hashCode() ^ this.packageName.hashCode()) ^ this.tokenType.hashCode()) ^ java.util.Arrays.hashCode(this.sigDigest);
        }
    }

    private static class TokenLruCache extends android.util.LruCache<com.android.server.accounts.TokenCache.Key, com.android.server.accounts.TokenCache.Value> {
        private java.util.HashMap<android.accounts.Account, com.android.server.accounts.TokenCache.TokenLruCache.Evictor> mAccountEvictors;
        private java.util.HashMap<android.util.Pair<java.lang.String, java.lang.String>, com.android.server.accounts.TokenCache.TokenLruCache.Evictor> mTokenEvictors;

        private class Evictor {
            private final java.util.List<com.android.server.accounts.TokenCache.Key> mKeys = new java.util.ArrayList();

            public Evictor() {
            }

            public void add(com.android.server.accounts.TokenCache.Key k) {
                this.mKeys.add(k);
            }

            public void evict() {
                for (com.android.server.accounts.TokenCache.Key k : this.mKeys) {
                    com.android.server.accounts.TokenCache.TokenLruCache.this.remove(k);
                }
            }
        }

        public TokenLruCache() {
            super(com.android.server.accounts.TokenCache.MAX_CACHE_CHARS);
            this.mTokenEvictors = new java.util.HashMap<>();
            this.mAccountEvictors = new java.util.HashMap<>();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.util.LruCache
        public int sizeOf(com.android.server.accounts.TokenCache.Key k, com.android.server.accounts.TokenCache.Value v) {
            return v.token.length();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.util.LruCache
        public void entryRemoved(boolean evicted, com.android.server.accounts.TokenCache.Key k, com.android.server.accounts.TokenCache.Value oldVal, com.android.server.accounts.TokenCache.Value newVal) {
            com.android.server.accounts.TokenCache.TokenLruCache.Evictor evictor;
            if (oldVal != null && newVal == null && (evictor = this.mTokenEvictors.remove(new android.util.Pair(k.account.type, oldVal.token))) != null) {
                evictor.evict();
            }
        }

        public void putToken(com.android.server.accounts.TokenCache.Key k, com.android.server.accounts.TokenCache.Value v) {
            android.util.Pair<java.lang.String, java.lang.String> mapKey = new android.util.Pair<>(k.account.type, v.token);
            com.android.server.accounts.TokenCache.TokenLruCache.Evictor tokenEvictor = this.mTokenEvictors.get(mapKey);
            if (tokenEvictor == null) {
                tokenEvictor = new com.android.server.accounts.TokenCache.TokenLruCache.Evictor();
            }
            tokenEvictor.add(k);
            this.mTokenEvictors.put(mapKey, tokenEvictor);
            com.android.server.accounts.TokenCache.TokenLruCache.Evictor accountEvictor = this.mAccountEvictors.get(k.account);
            if (accountEvictor == null) {
                accountEvictor = new com.android.server.accounts.TokenCache.TokenLruCache.Evictor();
            }
            accountEvictor.add(k);
            this.mAccountEvictors.put(k.account, accountEvictor);
            put(k, v);
        }

        public void evict(java.lang.String accountType, java.lang.String token) {
            com.android.server.accounts.TokenCache.TokenLruCache.Evictor evictor = this.mTokenEvictors.get(new android.util.Pair(accountType, token));
            if (evictor != null) {
                evictor.evict();
            }
        }

        public void evict(android.accounts.Account account) {
            com.android.server.accounts.TokenCache.TokenLruCache.Evictor evictor = this.mAccountEvictors.get(account);
            if (evictor != null) {
                evictor.evict();
            }
        }
    }

    public void put(android.accounts.Account account, java.lang.String token, java.lang.String tokenType, java.lang.String packageName, byte[] sigDigest, long expiryMillis) {
        java.util.Objects.requireNonNull(account);
        if (token == null || java.lang.System.currentTimeMillis() > expiryMillis) {
            return;
        }
        com.android.server.accounts.TokenCache.Key k = new com.android.server.accounts.TokenCache.Key(account, tokenType, packageName, sigDigest);
        com.android.server.accounts.TokenCache.Value v = new com.android.server.accounts.TokenCache.Value(token, expiryMillis);
        this.mCachedTokens.putToken(k, v);
    }

    public void remove(java.lang.String accountType, java.lang.String token) {
        this.mCachedTokens.evict(accountType, token);
    }

    public void remove(android.accounts.Account account) {
        this.mCachedTokens.evict(account);
    }

    public com.android.server.accounts.TokenCache.Value get(android.accounts.Account account, java.lang.String tokenType, java.lang.String packageName, byte[] sigDigest) {
        com.android.server.accounts.TokenCache.Key k = new com.android.server.accounts.TokenCache.Key(account, tokenType, packageName, sigDigest);
        com.android.server.accounts.TokenCache.Value v = this.mCachedTokens.get(k);
        long currentTime = java.lang.System.currentTimeMillis();
        if (v != null && currentTime < v.expiryEpochMillis) {
            return v;
        }
        if (v != null) {
            remove(account.type, v.token);
            return null;
        }
        return null;
    }
}
