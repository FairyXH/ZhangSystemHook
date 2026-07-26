package com.android.server.appop;

import com.android.server.appop.AppOpsService.Op;
import com.android.server.appop.AppOpsService.UidState;

/* JADX INFO: loaded from: classes.dex */
final class AppOpsRecentAccessPersistence {
    private static final java.lang.String ATTR_ACCESS_DURATION = "d";
    private static final java.lang.String ATTR_ACCESS_TIME = "t";
    private static final java.lang.String ATTR_DEVICE_ID = "dv";
    private static final java.lang.String ATTR_ID = "id";
    private static final java.lang.String ATTR_NAME = "n";
    private static final java.lang.String ATTR_PROXY_ATTRIBUTION_TAG = "pc";
    private static final java.lang.String ATTR_PROXY_DEVICE_ID = "pdv";
    private static final java.lang.String ATTR_PROXY_PACKAGE = "pp";
    private static final java.lang.String ATTR_PROXY_UID = "pu";
    private static final java.lang.String ATTR_REJECT_TIME = "r";
    private static final int CURRENT_VERSION = 1;
    static final java.lang.String TAG = "AppOpsRecentAccessPersistence";
    private static final java.lang.String TAG_APP_OPS = "app-ops";
    private static final java.lang.String TAG_ATTRIBUTION_OP = "st";
    private static final java.lang.String TAG_OP = "op";
    private static final java.lang.String TAG_PACKAGE = "pkg";
    private static final java.lang.String TAG_UID = "uid";
    final com.android.server.appop.AppOpsService mAppOpsService;
    final android.util.AtomicFile mRecentAccessesFile;

    AppOpsRecentAccessPersistence(android.util.AtomicFile recentAccessesFile, com.android.server.appop.AppOpsService appOpsService) {
        this.mRecentAccessesFile = recentAccessesFile;
        this.mAppOpsService = appOpsService;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:37:0x0090
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    void readRecentAccesses(android.util.SparseArray<com.android.server.appop.AppOpsService.UidState> r12) {
        /*
            Method dump skipped, instruction units count: 262
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appop.AppOpsRecentAccessPersistence.readRecentAccesses(android.util.SparseArray):void");
    }

    private void readPackage(com.android.modules.utils.TypedXmlPullParser parser, android.util.SparseArray<com.android.server.appop.AppOpsService.UidState> uidStates) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        java.lang.String pkgName = parser.getAttributeValue((java.lang.String) null, ATTR_NAME);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("uid")) {
                            readUid(parser, pkgName, uidStates);
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

    private void readUid(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String pkgName, android.util.SparseArray<com.android.server.appop.AppOpsService.UidState> uidStates) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        int uid = parser.getAttributeInt((java.lang.String) null, ATTR_NAME);
        com.android.server.appop.AppOpsService appOpsService = this.mAppOpsService;
        java.util.Objects.requireNonNull(appOpsService);
        com.android.server.appop.AppOpsService.UidState uidState = appOpsService.new UidState(uid);
        uidStates.put(uid, uidState);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals(TAG_OP)) {
                            readOp(parser, uidState, pkgName);
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

    private void readOp(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.appop.AppOpsService.UidState uidState, java.lang.String pkgName) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        int opCode = parser.getAttributeInt((java.lang.String) null, ATTR_NAME);
        com.android.server.appop.AppOpsService appOpsService = this.mAppOpsService;
        java.util.Objects.requireNonNull(appOpsService);
        com.android.server.appop.AppOpsService.Op op = appOpsService.new Op(uidState, pkgName, opCode, uidState.uid);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tagName = parser.getName();
                if (tagName.equals(TAG_ATTRIBUTION_OP)) {
                    readAttributionOp(parser, op, com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_ID));
                } else {
                    android.util.Slog.w(TAG, "Unknown element under <op>: " + parser.getName());
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        com.android.server.appop.AppOpsService.Ops ops = uidState.pkgOps.get(pkgName);
        if (ops == null) {
            ops = new com.android.server.appop.AppOpsService.Ops(pkgName, uidState);
            uidState.pkgOps.put(pkgName, ops);
        }
        ops.put(op.op, op);
    }

    private void readAttributionOp(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.appop.AppOpsService.Op parent, java.lang.String attribution) throws org.xmlpull.v1.XmlPullParserException, java.lang.NumberFormatException, java.io.IOException {
        com.android.server.appop.AttributedOp attributedOp;
        long key;
        long j;
        int opFlags;
        long key2 = parser.getAttributeLong((java.lang.String) null, ATTR_NAME);
        int uidState = android.app.AppOpsManager.extractUidStateFromKey(key2);
        int opFlags2 = android.app.AppOpsManager.extractFlagsFromKey(key2);
        java.lang.String deviceId = parser.getAttributeValue((java.lang.String) null, ATTR_DEVICE_ID);
        long accessTime = parser.getAttributeLong((java.lang.String) null, ATTR_ACCESS_TIME, 0L);
        long rejectTime = parser.getAttributeLong((java.lang.String) null, "r", 0L);
        long accessDuration = parser.getAttributeLong((java.lang.String) null, ATTR_ACCESS_DURATION, -1L);
        java.lang.String proxyPkg = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_PROXY_PACKAGE);
        int proxyUid = parser.getAttributeInt((java.lang.String) null, ATTR_PROXY_UID, -1);
        java.lang.String proxyAttributionTag = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_PROXY_ATTRIBUTION_TAG);
        java.lang.String proxyDeviceId = parser.getAttributeValue((java.lang.String) null, ATTR_PROXY_DEVICE_ID);
        com.android.server.appop.AttributedOp attributedOp2 = parent.getOrCreateAttribution(parent, attribution, (deviceId == null || java.util.Objects.equals(deviceId, "")) ? "default:0" : deviceId);
        if (accessTime > 0) {
            attributedOp = attributedOp2;
            key = rejectTime;
            j = 0;
            opFlags = opFlags2;
            attributedOp2.accessed(accessTime, accessDuration, proxyUid, proxyPkg, proxyAttributionTag, proxyDeviceId, uidState, opFlags2);
        } else {
            attributedOp = attributedOp2;
            key = rejectTime;
            j = 0;
            opFlags = opFlags2;
        }
        if (key > j) {
            attributedOp.rejected(key, uidState, opFlags);
        }
    }

    void writeRecentAccesses(android.util.SparseArray<com.android.server.appop.AppOpsService.UidState> uidStates) {
        synchronized (this.mRecentAccessesFile) {
            try {
                try {
                    java.io.FileOutputStream stream = this.mRecentAccessesFile.startWrite();
                    try {
                        com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
                        out.startDocument((java.lang.String) null, true);
                        out.startTag((java.lang.String) null, TAG_APP_OPS);
                        out.attributeInt((java.lang.String) null, "v", 1);
                        for (int uidIndex = 0; uidIndex < uidStates.size(); uidIndex++) {
                            com.android.server.appop.AppOpsService.UidState uidState = uidStates.valueAt(uidIndex);
                            int uid = uidState.uid;
                            for (int pkgIndex = 0; pkgIndex < uidState.pkgOps.size(); pkgIndex++) {
                                java.lang.String packageName = uidState.pkgOps.keyAt(pkgIndex);
                                com.android.server.appop.AppOpsService.Ops ops = uidState.pkgOps.valueAt(pkgIndex);
                                out.startTag((java.lang.String) null, TAG_PACKAGE);
                                out.attribute((java.lang.String) null, ATTR_NAME, packageName);
                                out.startTag((java.lang.String) null, "uid");
                                out.attributeInt((java.lang.String) null, ATTR_NAME, uid);
                                for (int opIndex = 0; opIndex < ops.size(); opIndex++) {
                                    com.android.server.appop.AppOpsService.Op op = ops.valueAt(opIndex);
                                    out.startTag((java.lang.String) null, TAG_OP);
                                    out.attributeInt((java.lang.String) null, ATTR_NAME, op.op);
                                    writeDeviceAttributedOps(out, op);
                                    out.endTag((java.lang.String) null, TAG_OP);
                                }
                                out.endTag((java.lang.String) null, "uid");
                                out.endTag((java.lang.String) null, TAG_PACKAGE);
                            }
                        }
                        out.endTag((java.lang.String) null, TAG_APP_OPS);
                        out.endDocument();
                        this.mRecentAccessesFile.finishWrite(stream);
                    } catch (java.io.IOException e) {
                        android.util.Slog.w(TAG, "Failed to write state, restoring backup.", e);
                        this.mRecentAccessesFile.failWrite(stream);
                    }
                } catch (java.io.IOException e2) {
                    android.util.Slog.w(TAG, "Failed to write state: " + e2);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    private void writeDeviceAttributedOps(com.android.modules.utils.TypedXmlSerializer out, com.android.server.appop.AppOpsService.Op op) throws java.io.IOException {
        android.app.AppOpsManager.AttributedOpEntry attributedOpEntry;
        android.util.ArraySet<java.lang.Long> keys;
        java.lang.String attributionTag;
        com.android.server.appop.AppOpsService.Op op2 = op;
        java.util.Iterator<java.lang.String> it = op2.mDeviceAttributedOps.keySet().iterator();
        while (it.hasNext()) {
            java.lang.String deviceId = it.next();
            android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps = op2.mDeviceAttributedOps.get(deviceId);
            int attrIndex = 0;
            while (attrIndex < attributedOps.size()) {
                java.lang.String attributionTag2 = attributedOps.keyAt(attrIndex);
                android.app.AppOpsManager.AttributedOpEntry attributedOpEntry2 = attributedOps.valueAt(attrIndex).createAttributedOpEntryLocked();
                android.util.ArraySet<java.lang.Long> keys2 = attributedOpEntry2.collectKeys();
                int k = 0;
                while (k < keys2.size()) {
                    long key = keys2.valueAt(k).longValue();
                    int uidState = android.app.AppOpsManager.extractUidStateFromKey(key);
                    int flags = android.app.AppOpsManager.extractFlagsFromKey(key);
                    long accessTime = attributedOpEntry2.getLastAccessTime(uidState, uidState, flags);
                    java.util.Iterator<java.lang.String> it2 = it;
                    long rejectTime = attributedOpEntry2.getLastRejectTime(uidState, uidState, flags);
                    android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps2 = attributedOps;
                    int attrIndex2 = attrIndex;
                    long accessDuration = attributedOpEntry2.getLastDuration(uidState, uidState, flags);
                    android.app.AppOpsManager.OpEventProxyInfo proxy = attributedOpEntry2.getLastProxyInfo(uidState, uidState, flags);
                    if (accessTime <= 0 && rejectTime <= 0 && accessDuration <= 0 && proxy == null) {
                        attributionTag = attributionTag2;
                        attributedOpEntry = attributedOpEntry2;
                        keys = keys2;
                    } else {
                        attributedOpEntry = attributedOpEntry2;
                        keys = keys2;
                        out.startTag((java.lang.String) null, TAG_ATTRIBUTION_OP);
                        if (attributionTag2 != null) {
                            out.attribute((java.lang.String) null, ATTR_ID, attributionTag2);
                        }
                        out.attributeLong((java.lang.String) null, ATTR_NAME, key);
                        if (java.util.Objects.equals(deviceId, "default:0")) {
                            attributionTag = attributionTag2;
                        } else {
                            attributionTag = attributionTag2;
                            out.attribute((java.lang.String) null, ATTR_DEVICE_ID, deviceId);
                        }
                        if (accessTime > 0) {
                            out.attributeLong((java.lang.String) null, ATTR_ACCESS_TIME, accessTime);
                        }
                        if (rejectTime > 0) {
                            out.attributeLong((java.lang.String) null, "r", rejectTime);
                        }
                        if (accessDuration > 0) {
                            out.attributeLong((java.lang.String) null, ATTR_ACCESS_DURATION, accessDuration);
                        }
                        if (proxy != null) {
                            out.attributeInt((java.lang.String) null, ATTR_PROXY_UID, proxy.getUid());
                            if (proxy.getPackageName() != null) {
                                out.attribute((java.lang.String) null, ATTR_PROXY_PACKAGE, proxy.getPackageName());
                            }
                            if (proxy.getAttributionTag() != null) {
                                out.attribute((java.lang.String) null, ATTR_PROXY_ATTRIBUTION_TAG, proxy.getAttributionTag());
                            }
                            if (proxy.getDeviceId() != null && !java.util.Objects.equals(proxy.getDeviceId(), "default:0")) {
                                out.attribute((java.lang.String) null, ATTR_PROXY_DEVICE_ID, proxy.getDeviceId());
                            }
                        }
                        out.endTag((java.lang.String) null, TAG_ATTRIBUTION_OP);
                    }
                    k++;
                    it = it2;
                    attributedOps = attributedOps2;
                    attrIndex = attrIndex2;
                    attributedOpEntry2 = attributedOpEntry;
                    keys2 = keys;
                    attributionTag2 = attributionTag;
                }
                attrIndex++;
            }
            op2 = op;
        }
    }
}
