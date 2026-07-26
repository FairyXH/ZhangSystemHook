package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
class LegacyAppOpStateParser {
    private static final int NO_FILE_VERSION = -2;
    private static final int NO_VERSION = -1;
    static final java.lang.String TAG = com.android.server.appop.LegacyAppOpStateParser.class.getSimpleName();

    LegacyAppOpStateParser() {
    }

    public int readState(android.util.AtomicFile file, android.util.SparseArray<android.util.SparseIntArray> uidModes, android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> userPackageModes) {
        int type;
        try {
            java.io.FileInputStream stream = file.openRead();
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(stream);
                do {
                    type = parser.next();
                    if (type == 2) {
                        break;
                    }
                } while (type != 1);
                if (type != 2) {
                    throw new java.lang.IllegalStateException("no start tag found");
                }
                int versionAtBoot = parser.getAttributeInt((java.lang.String) null, "v", -1);
                int outerDepth = parser.getDepth();
                while (true) {
                    int type2 = parser.next();
                    if (type2 == 1 || (type2 == 3 && parser.getDepth() <= outerDepth)) {
                        break;
                    }
                    if (type2 != 3 && type2 != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("pkg")) {
                            readPackage(parser, userPackageModes);
                        } else if (tagName.equals("uid")) {
                            readUidOps(parser, uidModes);
                        } else if (tagName.equals("user")) {
                            readUser(parser, userPackageModes);
                        } else {
                            android.util.Slog.w(TAG, "Unknown element under <app-ops>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                }
                if (stream != null) {
                    stream.close();
                }
                return versionAtBoot;
            } catch (java.lang.Throwable th) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.io.FileNotFoundException e) {
            android.util.Slog.i(TAG, "No existing app ops " + file.getBaseFile() + "; starting empty");
            return -2;
        } catch (java.io.IOException e2) {
            throw new java.lang.RuntimeException(e2);
        } catch (org.xmlpull.v1.XmlPullParserException e3) {
            throw new java.lang.RuntimeException(e3);
        }
    }

    private void readPackage(com.android.modules.utils.TypedXmlPullParser parser, android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> userPackageModes) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        java.lang.String pkgName = parser.getAttributeValue((java.lang.String) null, "n");
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("uid")) {
                            readPackageUid(parser, pkgName, userPackageModes);
                        } else {
                            android.util.Slog.w(TAG, "Unknown element under <pkg>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readPackageUid(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String pkgName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> userPackageModes) throws org.xmlpull.v1.XmlPullParserException, java.lang.NumberFormatException, java.io.IOException {
        int userId = android.os.UserHandle.getUserId(parser.getAttributeInt((java.lang.String) null, "n"));
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("op")) {
                            readOp(parser, userId, pkgName, userPackageModes);
                        } else {
                            android.util.Slog.w(TAG, "Unknown element under <pkg>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readUidOps(com.android.modules.utils.TypedXmlPullParser parser, android.util.SparseArray<android.util.SparseIntArray> uidModes) throws org.xmlpull.v1.XmlPullParserException, java.lang.NumberFormatException, java.io.IOException {
        int uid = parser.getAttributeInt((java.lang.String) null, "n");
        android.util.SparseIntArray modes = uidModes.get(uid);
        if (modes == null) {
            modes = new android.util.SparseIntArray();
            uidModes.put(uid, modes);
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("op")) {
                            int code = parser.getAttributeInt((java.lang.String) null, "n");
                            int mode = parser.getAttributeInt((java.lang.String) null, "m");
                            if (mode != android.app.AppOpsManager.opToDefaultMode(code)) {
                                modes.put(code, mode);
                            }
                        } else {
                            android.util.Slog.w(TAG, "Unknown element under <uid>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readUser(com.android.modules.utils.TypedXmlPullParser parser, android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> userPackageModes) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        int userId = parser.getAttributeInt((java.lang.String) null, "n");
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("pkg")) {
                            readPackageOp(parser, userId, userPackageModes);
                        } else {
                            android.util.Slog.w(TAG, "Unknown element under <user>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readPackageOp(com.android.modules.utils.TypedXmlPullParser parser, int userId, android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> userPackageModes) throws org.xmlpull.v1.XmlPullParserException, java.lang.NumberFormatException, java.io.IOException {
        java.lang.String pkgName = parser.getAttributeValue((java.lang.String) null, "n");
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("op")) {
                            readOp(parser, userId, pkgName, userPackageModes);
                        } else {
                            android.util.Slog.w(TAG, "Unknown element under <pkg>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readOp(com.android.modules.utils.TypedXmlPullParser parser, int userId, java.lang.String pkgName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> userPackageModes) throws org.xmlpull.v1.XmlPullParserException, java.lang.NumberFormatException {
        int opCode = parser.getAttributeInt((java.lang.String) null, "n");
        int defaultMode = android.app.AppOpsManager.opToDefaultMode(opCode);
        int mode = parser.getAttributeInt((java.lang.String) null, "m", defaultMode);
        if (mode != defaultMode) {
            android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes = userPackageModes.get(userId);
            if (packageModes == null) {
                packageModes = new android.util.ArrayMap<>();
                userPackageModes.put(userId, packageModes);
            }
            android.util.SparseIntArray modes = packageModes.get(pkgName);
            if (modes == null) {
                modes = new android.util.SparseIntArray();
                packageModes.put(pkgName, modes);
            }
            modes.put(opCode, mode);
        }
    }
}
