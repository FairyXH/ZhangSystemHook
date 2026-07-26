package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public class AppOpsRestrictionsImpl implements com.android.server.appop.AppOpsRestrictions {
    private static final int UID_ANY = -2;
    private com.android.server.appop.AppOpsRestrictions.AppOpsRestrictionRemovedListener mAppOpsRestrictionRemovedListener;
    private android.content.Context mContext;
    private android.os.Handler mHandler;
    private final android.util.ArrayMap<java.lang.Object, android.util.SparseBooleanArray> mGlobalRestrictions = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.Object, android.util.SparseArray<android.util.SparseBooleanArray>> mUserRestrictions = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.Object, android.util.SparseArray<android.os.PackageTagsList>> mUserRestrictionExcludedPackageTags = new android.util.ArrayMap<>();

    public AppOpsRestrictionsImpl(android.content.Context context, android.os.Handler handler, com.android.server.appop.AppOpsRestrictions.AppOpsRestrictionRemovedListener appOpsRestrictionRemovedListener) {
        this.mContext = context;
        this.mHandler = handler;
        this.mAppOpsRestrictionRemovedListener = appOpsRestrictionRemovedListener;
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public boolean setGlobalRestriction(java.lang.Object clientToken, int code, boolean restricted) {
        if (restricted) {
            if (!this.mGlobalRestrictions.containsKey(clientToken)) {
                this.mGlobalRestrictions.put(clientToken, new android.util.SparseBooleanArray());
            }
            android.util.SparseBooleanArray restrictedCodes = this.mGlobalRestrictions.get(clientToken);
            java.util.Objects.requireNonNull(restrictedCodes);
            boolean changed = !restrictedCodes.get(code);
            restrictedCodes.put(code, true);
            return changed;
        }
        android.util.SparseBooleanArray restrictedCodes2 = this.mGlobalRestrictions.get(clientToken);
        if (restrictedCodes2 == null) {
            return false;
        }
        boolean changed2 = restrictedCodes2.get(code);
        restrictedCodes2.delete(code);
        if (restrictedCodes2.size() == 0) {
            this.mGlobalRestrictions.remove(clientToken);
        }
        return changed2;
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public boolean getGlobalRestriction(java.lang.Object clientToken, int code) {
        android.util.SparseBooleanArray restrictedCodes = this.mGlobalRestrictions.get(clientToken);
        if (restrictedCodes == null) {
            return false;
        }
        return restrictedCodes.get(code);
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public boolean hasGlobalRestrictions(java.lang.Object clientToken) {
        return this.mGlobalRestrictions.containsKey(clientToken);
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public boolean clearGlobalRestrictions(java.lang.Object clientToken) {
        return this.mGlobalRestrictions.remove(clientToken) != null;
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public boolean setUserRestriction(java.lang.Object clientToken, int userId, int code, boolean restricted, android.os.PackageTagsList excludedPackageTags) {
        int[] userIds = resolveUserId(userId);
        boolean changed = false;
        for (int i = 0; i < userIds.length; i++) {
            changed = changed | putUserRestriction(clientToken, userIds[i], code, restricted) | putUserRestrictionExclusions(clientToken, userIds[i], excludedPackageTags);
        }
        return changed;
    }

    private int[] resolveUserId(int userId) {
        if (userId == -1) {
            java.util.List<android.content.pm.UserInfo> liveUsers = android.os.UserManager.get(this.mContext).getUsers();
            int[] userIds = new int[liveUsers.size()];
            for (int i = 0; i < liveUsers.size(); i++) {
                userIds[i] = liveUsers.get(i).id;
            }
            return userIds;
        }
        return new int[]{userId};
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public boolean hasUserRestrictions(java.lang.Object clientToken) {
        return this.mUserRestrictions.containsKey(clientToken);
    }

    private boolean getUserRestriction(java.lang.Object clientToken, int userId, int code) {
        android.util.SparseBooleanArray restrictedCodes;
        android.util.SparseArray<android.util.SparseBooleanArray> userIdRestrictedCodes = this.mUserRestrictions.get(clientToken);
        if (userIdRestrictedCodes == null || (restrictedCodes = userIdRestrictedCodes.get(userId)) == null) {
            return false;
        }
        return restrictedCodes.get(code);
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public boolean getUserRestriction(java.lang.Object clientToken, int userId, int code, java.lang.String packageName, java.lang.String attributionTag, boolean isCheckOp) {
        boolean restricted = getUserRestriction(clientToken, userId, code);
        if (!restricted) {
            return false;
        }
        android.os.PackageTagsList perUserExclusions = getUserRestrictionExclusions(clientToken, userId);
        if (perUserExclusions == null) {
            return true;
        }
        return isCheckOp ? true ^ perUserExclusions.includes(packageName) : true ^ perUserExclusions.contains(packageName, attributionTag);
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public boolean clearUserRestrictions(java.lang.Object clientToken) {
        android.util.SparseBooleanArray allUserRestrictedCodes = collectAllUserRestrictedCodes(clientToken);
        boolean changed = false | (this.mUserRestrictions.remove(clientToken) != null);
        boolean changed2 = changed | (this.mUserRestrictionExcludedPackageTags.remove(clientToken) != null);
        notifyAllUserRestrictions(allUserRestrictedCodes);
        return changed2;
    }

    private android.util.SparseBooleanArray collectAllUserRestrictedCodes(java.lang.Object clientToken) {
        android.util.SparseBooleanArray allRestrictedCodes = new android.util.SparseBooleanArray();
        android.util.SparseArray<android.util.SparseBooleanArray> userIdRestrictedCodes = this.mUserRestrictions.get(clientToken);
        if (userIdRestrictedCodes == null) {
            return allRestrictedCodes;
        }
        int userIdRestrictedCodesSize = userIdRestrictedCodes.size();
        for (int i = 0; i < userIdRestrictedCodesSize; i++) {
            android.util.SparseBooleanArray restrictedCodes = userIdRestrictedCodes.valueAt(i);
            int restrictedCodesSize = restrictedCodes.size();
            for (int j = 0; j < restrictedCodesSize; j++) {
                int code = restrictedCodes.keyAt(j);
                allRestrictedCodes.put(code, true);
            }
        }
        return allRestrictedCodes;
    }

    private void notifyAllUserRestrictions(android.util.SparseBooleanArray allUserRestrictedCodes) {
        int restrictedCodesSize = allUserRestrictedCodes.size();
        for (int j = 0; j < restrictedCodesSize; j++) {
            final int code = allUserRestrictedCodes.keyAt(j);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.appop.AppOpsRestrictionsImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyAllUserRestrictions$0(code);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyAllUserRestrictions$0(int code) {
        this.mAppOpsRestrictionRemovedListener.onAppOpsRestrictionRemoved(code);
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public boolean clearUserRestrictions(java.lang.Object clientToken, java.lang.Integer userId) {
        boolean changed = false;
        android.util.SparseArray<android.util.SparseBooleanArray> userIdRestrictedCodes = this.mUserRestrictions.get(clientToken);
        if (userIdRestrictedCodes != null) {
            changed = false | userIdRestrictedCodes.contains(userId.intValue());
            userIdRestrictedCodes.remove(userId.intValue());
            if (userIdRestrictedCodes.size() == 0) {
                this.mUserRestrictions.remove(clientToken);
            }
        }
        android.util.SparseArray<android.os.PackageTagsList> userIdPackageTags = this.mUserRestrictionExcludedPackageTags.get(clientToken);
        if (userIdPackageTags != null) {
            changed |= userIdPackageTags.contains(userId.intValue());
            userIdPackageTags.remove(userId.intValue());
            if (userIdPackageTags.size() == 0) {
                this.mUserRestrictionExcludedPackageTags.remove(clientToken);
            }
        }
        return changed;
    }

    private boolean putUserRestriction(java.lang.Object token, int userId, int code, boolean restricted) {
        android.util.SparseBooleanArray restrictedCodes;
        if (restricted) {
            if (!this.mUserRestrictions.containsKey(token)) {
                this.mUserRestrictions.put(token, new android.util.SparseArray<>());
            }
            android.util.SparseArray<android.util.SparseBooleanArray> userIdRestrictedCodes = this.mUserRestrictions.get(token);
            java.util.Objects.requireNonNull(userIdRestrictedCodes);
            if (!userIdRestrictedCodes.contains(userId)) {
                userIdRestrictedCodes.put(userId, new android.util.SparseBooleanArray());
            }
            android.util.SparseBooleanArray restrictedCodes2 = userIdRestrictedCodes.get(userId);
            boolean changed = !restrictedCodes2.get(code);
            restrictedCodes2.put(code, restricted);
            return changed;
        }
        android.util.SparseArray<android.util.SparseBooleanArray> userIdRestrictedCodes2 = this.mUserRestrictions.get(token);
        if (userIdRestrictedCodes2 == null || (restrictedCodes = userIdRestrictedCodes2.get(userId)) == null) {
            return false;
        }
        boolean changed2 = restrictedCodes.get(code);
        restrictedCodes.delete(code);
        if (restrictedCodes.size() == 0) {
            userIdRestrictedCodes2.remove(userId);
        }
        if (userIdRestrictedCodes2.size() == 0) {
            this.mUserRestrictions.remove(token);
            return changed2;
        }
        return changed2;
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public android.os.PackageTagsList getUserRestrictionExclusions(java.lang.Object clientToken, int userId) {
        android.util.SparseArray<android.os.PackageTagsList> userIdPackageTags = this.mUserRestrictionExcludedPackageTags.get(clientToken);
        if (userIdPackageTags == null) {
            return null;
        }
        return userIdPackageTags.get(userId);
    }

    private boolean putUserRestrictionExclusions(java.lang.Object token, int userId, android.os.PackageTagsList excludedPackageTags) {
        boolean addingExclusions = (excludedPackageTags == null || excludedPackageTags.isEmpty()) ? false : true;
        if (addingExclusions) {
            if (!this.mUserRestrictionExcludedPackageTags.containsKey(token)) {
                this.mUserRestrictionExcludedPackageTags.put(token, new android.util.SparseArray<>());
            }
            android.util.SparseArray<android.os.PackageTagsList> userIdExcludedPackageTags = this.mUserRestrictionExcludedPackageTags.get(token);
            java.util.Objects.requireNonNull(userIdExcludedPackageTags);
            userIdExcludedPackageTags.put(userId, excludedPackageTags);
            return true;
        }
        android.util.SparseArray<android.os.PackageTagsList> userIdExclusions = this.mUserRestrictionExcludedPackageTags.get(token);
        if (userIdExclusions == null) {
            return false;
        }
        boolean changed = userIdExclusions.get(userId) != null;
        userIdExclusions.remove(userId);
        if (userIdExclusions.size() == 0) {
            this.mUserRestrictionExcludedPackageTags.remove(token);
        }
        return changed;
    }

    @Override // com.android.server.appop.AppOpsRestrictions
    public void dumpRestrictions(java.io.PrintWriter pw, int code, java.lang.String dumpPackage, boolean showUserRestrictions) {
        java.lang.String str;
        int restrictionCount;
        int globalRestrictionCount;
        int userRestrictionCount;
        java.lang.String str2;
        boolean hasPackage;
        java.lang.String str3;
        android.util.SparseArray<android.util.SparseBooleanArray> perUserRestrictions;
        com.android.server.appop.AppOpsRestrictionsImpl appOpsRestrictionsImpl = this;
        java.io.PrintWriter printWriter = pw;
        int i = code;
        int globalRestrictionCount2 = appOpsRestrictionsImpl.mGlobalRestrictions.size();
        int i2 = 0;
        while (true) {
            str = "[";
            if (i2 >= globalRestrictionCount2) {
                break;
            }
            java.lang.Object token = appOpsRestrictionsImpl.mGlobalRestrictions.keyAt(i2);
            android.util.SparseBooleanArray restrictedOps = appOpsRestrictionsImpl.mGlobalRestrictions.valueAt(i2);
            printWriter.println("  Global restrictions for token " + token + ":");
            java.lang.StringBuilder restrictedOpsValue = new java.lang.StringBuilder();
            restrictedOpsValue.append("[");
            int restrictedOpCount = restrictedOps.size();
            for (int j = 0; j < restrictedOpCount; j++) {
                if (restrictedOpsValue.length() > 1) {
                    restrictedOpsValue.append(", ");
                }
                restrictedOpsValue.append(android.app.AppOpsManager.opToName(restrictedOps.keyAt(j)));
            }
            restrictedOpsValue.append("]");
            printWriter.println("      Restricted ops: " + ((java.lang.Object) restrictedOpsValue));
            i2++;
        }
        if (!showUserRestrictions) {
            return;
        }
        int userRestrictionCount2 = appOpsRestrictionsImpl.mUserRestrictions.size();
        int i3 = 0;
        while (i3 < userRestrictionCount2) {
            java.lang.Object token2 = appOpsRestrictionsImpl.mUserRestrictions.keyAt(i3);
            android.util.SparseArray<android.util.SparseBooleanArray> perUserRestrictions2 = appOpsRestrictionsImpl.mUserRestrictions.get(token2);
            android.util.SparseArray<android.os.PackageTagsList> perUserExcludedPackageTags = appOpsRestrictionsImpl.mUserRestrictionExcludedPackageTags.get(token2);
            boolean printedTokenHeader = false;
            int size = 0;
            if (perUserRestrictions2 == null) {
                restrictionCount = 0;
            } else {
                restrictionCount = perUserRestrictions2.size();
            }
            int restrictionCount2 = restrictionCount;
            if (restrictionCount2 <= 0 || dumpPackage != null) {
                globalRestrictionCount = globalRestrictionCount2;
                userRestrictionCount = userRestrictionCount2;
                str2 = str;
            } else {
                boolean printedOpsHeader = false;
                globalRestrictionCount = globalRestrictionCount2;
                int globalRestrictionCount3 = 0;
                while (globalRestrictionCount3 < restrictionCount2) {
                    int restrictionCount3 = restrictionCount2;
                    int userId = perUserRestrictions2.keyAt(globalRestrictionCount3);
                    int userRestrictionCount3 = userRestrictionCount2;
                    android.util.SparseBooleanArray restrictedOps2 = perUserRestrictions2.valueAt(globalRestrictionCount3);
                    if (restrictedOps2 == null || (i >= 0 && !restrictedOps2.get(i))) {
                        str3 = str;
                        perUserRestrictions = perUserRestrictions2;
                    } else {
                        if (printedTokenHeader) {
                            perUserRestrictions = perUserRestrictions2;
                        } else {
                            perUserRestrictions = perUserRestrictions2;
                            printWriter.println("  User restrictions for token " + token2 + ":");
                            printedTokenHeader = true;
                        }
                        if (!printedOpsHeader) {
                            printWriter.println("      Restricted ops:");
                            printedOpsHeader = true;
                        }
                        java.lang.StringBuilder restrictedOpsValue2 = new java.lang.StringBuilder();
                        restrictedOpsValue2.append(str);
                        str3 = str;
                        int restrictedOpCount2 = restrictedOps2.size();
                        boolean printedTokenHeader2 = printedTokenHeader;
                        int k = 0;
                        while (k < restrictedOpCount2) {
                            int restrictedOp = restrictedOps2.keyAt(k);
                            android.util.SparseBooleanArray restrictedOps3 = restrictedOps2;
                            int restrictedOpCount3 = restrictedOpCount2;
                            if (restrictedOpsValue2.length() > 1) {
                                restrictedOpsValue2.append(", ");
                            }
                            restrictedOpsValue2.append(android.app.AppOpsManager.opToName(restrictedOp));
                            k++;
                            restrictedOps2 = restrictedOps3;
                            restrictedOpCount2 = restrictedOpCount3;
                        }
                        restrictedOpsValue2.append("]");
                        printWriter.print("        ");
                        printWriter.print("user: ");
                        printWriter.print(userId);
                        printWriter.print(" restricted ops: ");
                        printWriter.println(restrictedOpsValue2);
                        printedTokenHeader = printedTokenHeader2;
                    }
                    globalRestrictionCount3++;
                    restrictionCount2 = restrictionCount3;
                    perUserRestrictions2 = perUserRestrictions;
                    userRestrictionCount2 = userRestrictionCount3;
                    str = str3;
                }
                userRestrictionCount = userRestrictionCount2;
                str2 = str;
            }
            if (perUserExcludedPackageTags != null) {
                size = perUserExcludedPackageTags.size();
            }
            int excludedPackageCount = size;
            if (excludedPackageCount > 0 && i < 0) {
                android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(printWriter);
                ipw.increaseIndent();
                boolean printedPackagesHeader = false;
                int j2 = 0;
                while (j2 < excludedPackageCount) {
                    int userId2 = perUserExcludedPackageTags.keyAt(j2);
                    int excludedPackageCount2 = excludedPackageCount;
                    android.os.PackageTagsList packageNames = perUserExcludedPackageTags.valueAt(j2);
                    if (packageNames != null) {
                        if (dumpPackage != null) {
                            hasPackage = packageNames.includes(dumpPackage);
                        } else {
                            hasPackage = true;
                        }
                        if (hasPackage) {
                            if (!printedTokenHeader) {
                                ipw.println("User restrictions for token " + token2 + ":");
                                printedTokenHeader = true;
                            }
                            ipw.increaseIndent();
                            if (!printedPackagesHeader) {
                                ipw.println("Excluded packages:");
                                printedPackagesHeader = true;
                            }
                            ipw.increaseIndent();
                            ipw.print("user: ");
                            ipw.print(userId2);
                            ipw.println(" packages: ");
                            ipw.increaseIndent();
                            packageNames.dump(ipw);
                            ipw.decreaseIndent();
                            ipw.decreaseIndent();
                            ipw.decreaseIndent();
                        }
                    }
                    j2++;
                    excludedPackageCount = excludedPackageCount2;
                }
                ipw.decreaseIndent();
            }
            i3++;
            appOpsRestrictionsImpl = this;
            printWriter = pw;
            i = code;
            globalRestrictionCount2 = globalRestrictionCount;
            userRestrictionCount2 = userRestrictionCount;
            str = str2;
        }
    }
}
