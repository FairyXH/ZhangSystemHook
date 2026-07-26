package com.android.server.slice;

/* JADX INFO: loaded from: classes3.dex */
public class SliceFullAccessList {
    static final int DB_VERSION = 1;
    private static final java.lang.String TAG = "SliceFullAccessList";
    private static final java.lang.String TAG_LIST = "slice-access-list";
    private static final java.lang.String TAG_PKG = "pkg";
    private static final java.lang.String TAG_USER = "user";
    private final android.content.Context mContext;
    private final java.lang.String ATT_USER_ID = TAG_USER;
    private final java.lang.String ATT_VERSION = "version";
    private final android.util.SparseArray<android.util.ArraySet<java.lang.String>> mFullAccessPkgs = new android.util.SparseArray<>();

    public SliceFullAccessList(android.content.Context context) {
        this.mContext = context;
    }

    public boolean hasFullAccess(java.lang.String pkg, int userId) {
        android.util.ArraySet<java.lang.String> pkgs = this.mFullAccessPkgs.get(userId, null);
        return pkgs != null && pkgs.contains(pkg);
    }

    public void grantFullAccess(java.lang.String pkg, int userId) {
        android.util.ArraySet<java.lang.String> pkgs = this.mFullAccessPkgs.get(userId, null);
        if (pkgs == null) {
            pkgs = new android.util.ArraySet<>();
            this.mFullAccessPkgs.put(userId, pkgs);
        }
        pkgs.add(pkg);
    }

    public void removeGrant(java.lang.String pkg, int userId) {
        android.util.ArraySet<java.lang.String> pkgs = this.mFullAccessPkgs.get(userId, null);
        if (pkgs == null) {
            pkgs = new android.util.ArraySet<>();
            this.mFullAccessPkgs.put(userId, pkgs);
        }
        pkgs.remove(pkg);
    }

    public void writeXml(org.xmlpull.v1.XmlSerializer out, int user) throws java.io.IOException {
        out.startTag(null, TAG_LIST);
        out.attribute(null, "version", java.lang.String.valueOf(1));
        int N = this.mFullAccessPkgs.size();
        for (int i = 0; i < N; i++) {
            int userId = this.mFullAccessPkgs.keyAt(i);
            android.util.ArraySet<java.lang.String> pkgs = this.mFullAccessPkgs.valueAt(i);
            if (user == -1 || user == userId) {
                out.startTag(null, TAG_USER);
                out.attribute(null, TAG_USER, java.lang.Integer.toString(userId));
                if (pkgs != null) {
                    int M = pkgs.size();
                    for (int j = 0; j < M; j++) {
                        out.startTag(null, TAG_PKG);
                        out.text(pkgs.valueAt(j));
                        out.endTag(null, TAG_PKG);
                    }
                }
                out.endTag(null, TAG_USER);
            }
        }
        out.endTag(null, TAG_LIST);
    }

    public void readXml(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int xmlVersion = com.android.internal.util.XmlUtils.readIntAttribute(parser, "version", 0);
        java.util.List<android.content.pm.UserInfo> activeUsers = android.os.UserManager.get(this.mContext).getAliveUsers();
        for (android.content.pm.UserInfo userInfo : activeUsers) {
            upgradeXml(xmlVersion, userInfo.getUserHandle().getIdentifier());
        }
        this.mFullAccessPkgs.clear();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                java.lang.String tag = parser.getName();
                if (type != 3 || !TAG_LIST.equals(tag)) {
                    if (type == 2 && TAG_USER.equals(tag)) {
                        int userId = com.android.internal.util.XmlUtils.readIntAttribute(parser, TAG_USER, 0);
                        android.util.ArraySet<java.lang.String> pkgs = new android.util.ArraySet<>();
                        while (true) {
                            int type2 = parser.next();
                            if (type2 == 1) {
                                break;
                            }
                            java.lang.String userTag = parser.getName();
                            if (type2 == 3 && TAG_USER.equals(userTag)) {
                                break;
                            }
                            if (type2 == 2 && TAG_PKG.equals(userTag)) {
                                java.lang.String pkg = parser.nextText();
                                pkgs.add(pkg);
                            }
                        }
                        this.mFullAccessPkgs.put(userId, pkgs);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    protected void upgradeXml(int xmlVersion, int userId) {
    }
}
