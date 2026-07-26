package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public final class PackagesTokenData {
    private static final int PACKAGE_NAME_INDEX = 0;
    public static final int UNASSIGNED_TOKEN = -1;
    public int counter = 1;
    public final android.util.SparseArray<java.util.ArrayList<java.lang.String>> tokensToPackagesMap = new android.util.SparseArray<>();
    public final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Integer>> packagesToTokensMap = new android.util.ArrayMap<>();
    public final android.util.ArrayMap<java.lang.String, java.lang.Long> removedPackagesMap = new android.util.ArrayMap<>();
    public final android.util.ArraySet<java.lang.Integer> removedPackageTokens = new android.util.ArraySet<>();

    public int getPackageTokenOrAdd(java.lang.String packageName, long timeStamp) {
        java.lang.Long timeRemoved = this.removedPackagesMap.get(packageName);
        if (timeRemoved != null && timeRemoved.longValue() > timeStamp) {
            return -1;
        }
        android.util.ArrayMap<java.lang.String, java.lang.Integer> packageTokensMap = this.packagesToTokensMap.get(packageName);
        if (packageTokensMap == null) {
            packageTokensMap = new android.util.ArrayMap<>();
            this.packagesToTokensMap.put(packageName, packageTokensMap);
        }
        int token = packageTokensMap.getOrDefault(packageName, -1).intValue();
        if (token == -1) {
            int token2 = this.counter;
            this.counter = token2 + 1;
            java.util.ArrayList<java.lang.String> tokenPackages = new java.util.ArrayList<>();
            tokenPackages.add(packageName);
            packageTokensMap.put(packageName, java.lang.Integer.valueOf(token2));
            this.tokensToPackagesMap.put(token2, tokenPackages);
            return token2;
        }
        return token;
    }

    public int getTokenOrAdd(int packageToken, java.lang.String packageName, java.lang.String key) {
        if (packageName.equals(key)) {
            return 0;
        }
        int token = this.packagesToTokensMap.get(packageName).getOrDefault(key, -1).intValue();
        if (token == -1) {
            int token2 = this.tokensToPackagesMap.get(packageToken).size();
            this.packagesToTokensMap.get(packageName).put(key, java.lang.Integer.valueOf(token2));
            this.tokensToPackagesMap.get(packageToken).add(key);
            return token2;
        }
        return token;
    }

    public java.lang.String getPackageString(int packageToken) {
        java.util.ArrayList<java.lang.String> packageStrings = this.tokensToPackagesMap.get(packageToken);
        if (packageStrings == null) {
            return null;
        }
        return packageStrings.get(0);
    }

    public java.lang.String getString(int packageToken, int token) {
        try {
            return this.tokensToPackagesMap.get(packageToken).get(token);
        } catch (java.lang.IndexOutOfBoundsException e) {
            return null;
        } catch (java.lang.NullPointerException npe) {
            android.util.Slog.e("PackagesTokenData", "Unable to find tokenized strings for package " + packageToken, npe);
            return null;
        }
    }

    public int removePackage(java.lang.String packageName, long timeRemoved) {
        this.removedPackagesMap.put(packageName, java.lang.Long.valueOf(timeRemoved));
        if (!this.packagesToTokensMap.containsKey(packageName)) {
            return -1;
        }
        int packageToken = this.packagesToTokensMap.get(packageName).get(packageName).intValue();
        this.packagesToTokensMap.remove(packageName);
        this.tokensToPackagesMap.delete(packageToken);
        this.removedPackageTokens.add(java.lang.Integer.valueOf(packageToken));
        return packageToken;
    }
}
