package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class RestrictionsSet {
    private static final java.lang.String TAG_RESTRICTIONS = "restrictions";
    private static final java.lang.String TAG_RESTRICTIONS_USER = "restrictions_user";
    private static final java.lang.String USER_ID = "user_id";
    private final android.util.SparseArray<android.os.Bundle> mUserRestrictions = new android.util.SparseArray<>(0);

    public RestrictionsSet() {
    }

    public RestrictionsSet(int userId, android.os.Bundle restrictions) {
        if (restrictions.isEmpty()) {
            throw new java.lang.IllegalArgumentException("empty restriction bundle cannot be added.");
        }
        this.mUserRestrictions.put(userId, restrictions);
    }

    public boolean updateRestrictions(int userId, android.os.Bundle restrictions) {
        boolean changed = !com.android.server.pm.UserRestrictionsUtils.areEqual(this.mUserRestrictions.get(userId), restrictions);
        if (!changed) {
            return false;
        }
        if (!com.android.server.BundleUtils.isEmpty(restrictions)) {
            this.mUserRestrictions.put(userId, restrictions);
        } else {
            this.mUserRestrictions.delete(userId);
        }
        return true;
    }

    public boolean removeRestrictionsForAllUsers(java.lang.String restriction) {
        boolean removed = false;
        for (int i = 0; i < this.mUserRestrictions.size(); i++) {
            android.os.Bundle restrictions = this.mUserRestrictions.valueAt(i);
            if (com.android.server.pm.UserRestrictionsUtils.contains(restrictions, restriction)) {
                restrictions.remove(restriction);
                removed = true;
            }
        }
        return removed;
    }

    public void moveRestriction(com.android.server.pm.RestrictionsSet destRestrictions, java.lang.String restriction) {
        int i = 0;
        while (i < this.mUserRestrictions.size()) {
            int userId = this.mUserRestrictions.keyAt(i);
            android.os.Bundle from = this.mUserRestrictions.valueAt(i);
            if (com.android.server.pm.UserRestrictionsUtils.contains(from, restriction)) {
                from.remove(restriction);
                android.os.Bundle to = destRestrictions.getRestrictions(userId);
                if (to == null) {
                    android.os.Bundle to2 = new android.os.Bundle();
                    to2.putBoolean(restriction, true);
                    destRestrictions.updateRestrictions(userId, to2);
                } else {
                    to.putBoolean(restriction, true);
                }
                if (from.isEmpty()) {
                    this.mUserRestrictions.removeAt(i);
                    i--;
                }
            }
            i++;
        }
    }

    public boolean isEmpty() {
        return this.mUserRestrictions.size() == 0;
    }

    public android.os.Bundle mergeAll() {
        android.os.Bundle result = new android.os.Bundle();
        for (int i = 0; i < this.mUserRestrictions.size(); i++) {
            com.android.server.pm.UserRestrictionsUtils.merge(result, this.mUserRestrictions.valueAt(i));
        }
        return result;
    }

    public java.util.List<android.os.UserManager.EnforcingUser> getEnforcingUsers(java.lang.String restriction, int userId) {
        java.util.List<android.os.UserManager.EnforcingUser> result = new java.util.ArrayList<>();
        if (getRestrictionsNonNull(userId).containsKey(restriction)) {
            result.add(new android.os.UserManager.EnforcingUser(userId, 4));
        }
        if (getRestrictionsNonNull(-1).containsKey(restriction)) {
            result.add(new android.os.UserManager.EnforcingUser(-1, 2));
        }
        return result;
    }

    public android.os.Bundle getRestrictions(int userId) {
        return this.mUserRestrictions.get(userId);
    }

    public android.os.Bundle getRestrictionsNonNull(int userId) {
        return com.android.server.pm.UserRestrictionsUtils.nonNull(this.mUserRestrictions.get(userId));
    }

    public boolean remove(int userId) {
        boolean hasUserRestriction = this.mUserRestrictions.contains(userId);
        this.mUserRestrictions.remove(userId);
        return hasUserRestriction;
    }

    public void removeAllRestrictions() {
        this.mUserRestrictions.clear();
    }

    public void writeRestrictions(com.android.modules.utils.TypedXmlSerializer serializer, java.lang.String outerTag) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, outerTag);
        for (int i = 0; i < this.mUserRestrictions.size(); i++) {
            serializer.startTag((java.lang.String) null, TAG_RESTRICTIONS_USER);
            serializer.attributeInt((java.lang.String) null, USER_ID, this.mUserRestrictions.keyAt(i));
            com.android.server.pm.UserRestrictionsUtils.writeRestrictions(serializer, this.mUserRestrictions.valueAt(i), TAG_RESTRICTIONS);
            serializer.endTag((java.lang.String) null, TAG_RESTRICTIONS_USER);
        }
        serializer.endTag((java.lang.String) null, outerTag);
    }

    public static com.android.server.pm.RestrictionsSet readRestrictions(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String outerTag) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.pm.RestrictionsSet restrictionsSet = new com.android.server.pm.RestrictionsSet();
        int userId = 0;
        while (true) {
            int type = parser.next();
            if (type != 1) {
                java.lang.String tag = parser.getName();
                if (type == 3 && outerTag.equals(tag)) {
                    return restrictionsSet;
                }
                if (type == 2 && TAG_RESTRICTIONS_USER.equals(tag)) {
                    userId = parser.getAttributeInt((java.lang.String) null, USER_ID);
                } else if (type == 2 && TAG_RESTRICTIONS.equals(tag)) {
                    android.os.Bundle restrictions = com.android.server.pm.UserRestrictionsUtils.readRestrictions(parser);
                    restrictionsSet.updateRestrictions(userId, restrictions);
                }
            } else {
                throw new org.xmlpull.v1.XmlPullParserException("restrictions cannot be read as xml is malformed.");
            }
        }
    }

    public void dumpRestrictions(java.io.PrintWriter pw, java.lang.String prefix) {
        boolean noneSet = true;
        for (int i = 0; i < this.mUserRestrictions.size(); i++) {
            pw.println(prefix + "User Id: " + this.mUserRestrictions.keyAt(i));
            com.android.server.pm.UserRestrictionsUtils.dumpRestrictions(pw, prefix + "  ", this.mUserRestrictions.valueAt(i));
            noneSet = false;
        }
        if (noneSet) {
            pw.println(prefix + "none");
        }
    }

    public android.util.IntArray getUserIds() {
        android.util.IntArray userIds = new android.util.IntArray(this.mUserRestrictions.size());
        for (int i = 0; i < this.mUserRestrictions.size(); i++) {
            userIds.add(this.mUserRestrictions.keyAt(i));
        }
        return userIds;
    }

    public boolean containsKey(int userId) {
        return this.mUserRestrictions.contains(userId);
    }

    public int size() {
        return this.mUserRestrictions.size();
    }

    public int keyAt(int index) {
        return this.mUserRestrictions.keyAt(index);
    }

    public android.os.Bundle valueAt(int index) {
        return this.mUserRestrictions.valueAt(index);
    }
}
