package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class BroadcastSkipPolicy {
    private com.android.server.am.IBroadcastSkipPolicyExt mBroadcastSkipPolicyExt = (com.android.server.am.IBroadcastSkipPolicyExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IBroadcastSkipPolicyExt.class).create();
    private android.permission.PermissionManager mPermissionManager;
    private final com.android.server.am.ActivityManagerService mService;

    public BroadcastSkipPolicy(com.android.server.am.ActivityManagerService service) {
        this.mService = (com.android.server.am.ActivityManagerService) java.util.Objects.requireNonNull(service);
    }

    public java.lang.String shouldSkipMessage(com.android.server.am.BroadcastRecord r, java.lang.Object target) {
        if (target instanceof com.android.server.am.BroadcastFilter) {
            return shouldSkipMessage(r, (com.android.server.am.BroadcastFilter) target);
        }
        return shouldSkipMessage(r, (android.content.pm.ResolveInfo) target);
    }

    /* JADX WARN: Removed duplicated region for block: B:178:0x0660  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x061c A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String shouldSkipMessage(com.android.server.am.BroadcastRecord r28, android.content.pm.ResolveInfo r29) {
        /*
            Method dump skipped, instruction units count: 1786
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.BroadcastSkipPolicy.shouldSkipMessage(com.android.server.am.BroadcastRecord, android.content.pm.ResolveInfo):java.lang.String");
    }

    public boolean disallowBackgroundStart(com.android.server.am.BroadcastRecord r) {
        return (r.intent.getFlags() & 8388608) != 0 || (r.intent.getComponent() == null && r.intent.getPackage() == null && (r.intent.getFlags() & 16777216) == 0 && !isSignaturePerm(r.requiredPermissions));
    }

    private java.lang.String shouldSkipMessage(com.android.server.am.BroadcastRecord r, com.android.server.am.BroadcastFilter filter) {
        java.lang.String str;
        java.lang.String str2;
        java.lang.String str3;
        java.lang.String str4;
        java.lang.String str5;
        java.lang.String str6;
        com.android.server.am.BroadcastSkipPolicy broadcastSkipPolicy;
        android.content.AttributionSource attributionSource;
        java.lang.String str7;
        int i;
        java.lang.String str8;
        android.content.AttributionSource attributionSource2;
        int perm;
        java.lang.String str9;
        java.lang.String str10;
        com.android.server.am.BroadcastSkipPolicy broadcastSkipPolicy2 = this;
        if (r.options != null && !r.options.testRequireCompatChange(filter.owningUid)) {
            return "Compat change filtered: broadcasting " + r.intent.toString() + " to uid " + filter.owningUid + " due to compat change " + r.options.getRequireCompatChangeId();
        }
        if (!broadcastSkipPolicy2.mService.validateAssociationAllowedLocked(r.callerPackage, r.callingUid, filter.packageName, filter.owningUid)) {
            return "Association not allowed: broadcasting " + r.intent.toString() + " from " + r.callerPackage + " (pid=" + r.callingPid + ", uid=" + r.callingUid + ") to " + filter.packageName + " through " + filter;
        }
        if (!broadcastSkipPolicy2.mService.mIntentFirewall.checkBroadcast(r.intent, r.callingUid, r.callingPid, r.resolvedType, filter.receiverList.uid)) {
            return "Firewall blocked: broadcasting " + r.intent.toString() + " from " + r.callerPackage + " (pid=" + r.callingPid + ", uid=" + r.callingUid + ") to " + filter.packageName + " through " + filter;
        }
        java.lang.String str11 = ") requires ";
        java.lang.String str12 = ") requires appop ";
        if (filter.requiredPermission != null) {
            if (com.android.server.am.ActivityManagerService.checkComponentPermission(filter.requiredPermission, r.callingPid, r.callingUid, -1, true) != 0) {
                return "Permission Denial: broadcasting " + r.intent.toString() + " from " + r.callerPackage + " (pid=" + r.callingPid + ", uid=" + r.callingUid + ") requires " + filter.requiredPermission + " due to registered receiver " + filter;
            }
            int opCode = android.app.AppOpsManager.permissionToOpCode(filter.requiredPermission);
            if (opCode != -1 && broadcastSkipPolicy2.mService.getAppOpsManager().noteOpNoThrow(opCode, r.callingUid, r.callerPackage, r.callerFeatureId, "Broadcast sent to protected receiver") != 0) {
                return "Appop Denial: broadcasting " + r.intent.toString() + " from " + r.callerPackage + " (pid=" + r.callingPid + ", uid=" + r.callingUid + ") requires appop " + android.app.AppOpsManager.permissionToOp(filter.requiredPermission) + " due to registered receiver " + filter;
            }
        }
        if (filter.receiverList.app == null || filter.receiverList.app.isKilled() || filter.receiverList.app.mErrorState.isCrashing()) {
            if (filter.receiverList.app != null) {
                android.util.Slog.w(com.android.server.am.BroadcastQueue.TAG, "Process state: app.isKilled = " + filter.receiverList.app.isKilled() + " app.mErrorState.isCrashing = " + filter.receiverList.app.mErrorState.isCrashing());
            }
            return "Skipping deliver [" + r.queue.toString() + "] " + r + " to " + filter.receiverList + ": process gone or crashing";
        }
        boolean visibleToInstantApps = (r.intent.getFlags() & 2097152) != 0;
        if (!visibleToInstantApps && filter.instantApp && filter.receiverList.uid != r.callingUid) {
            return "Instant App Denial: receiving " + r.intent.toString() + " to " + filter.receiverList.app + " (pid=" + filter.receiverList.pid + ", uid=" + filter.receiverList.uid + ") due to sender " + r.callerPackage + " (uid " + r.callingUid + ") not specifying FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS";
        }
        if (!filter.visibleToInstantApp && r.callerInstantApp && filter.receiverList.uid != r.callingUid) {
            return "Instant App Denial: receiving " + r.intent.toString() + " to " + filter.receiverList.app + " (pid=" + filter.receiverList.pid + ", uid=" + filter.receiverList.uid + ") requires receiver be visible to instant apps due to sender " + r.callerPackage + " (uid " + r.callingUid + ")";
        }
        java.lang.String str13 = "Permission Denial: receiving ";
        java.lang.String str14 = "Appop Denial: receiving ";
        java.lang.String str15 = "Broadcast delivered to registered receiver ";
        if (r.requiredPermissions == null || r.requiredPermissions.length <= 0) {
            str = "Broadcast delivered to registered receiver ";
            str2 = "Permission Denial: receiving ";
            str3 = ") due to sender ";
            str4 = "Appop Denial: receiving ";
            str5 = ") requires appop ";
        } else {
            if (com.android.server.am.Flags.usePermissionManagerForBroadcastDeliveryCheck()) {
                str3 = ") due to sender ";
                attributionSource = new android.content.AttributionSource.Builder(filter.receiverList.uid).setPid(filter.receiverList.pid).setPackageName(filter.packageName).setAttributionTag(filter.featureId).build();
            } else {
                str3 = ") due to sender ";
                attributionSource = null;
            }
            int i2 = 0;
            while (true) {
                java.lang.String str16 = str12;
                if (i2 >= r.requiredPermissions.length) {
                    str = str15;
                    str2 = str13;
                    str4 = str14;
                    str5 = str16;
                    break;
                }
                java.lang.String requiredPermission = r.requiredPermissions[i2];
                if (com.android.server.am.Flags.usePermissionManagerForBroadcastDeliveryCheck()) {
                    i = i2;
                    str8 = str14;
                    str7 = str15;
                    if (broadcastSkipPolicy2.hasPermissionForDataDelivery(requiredPermission, str15 + filter.receiverId, attributionSource)) {
                        perm = 0;
                    } else {
                        perm = -1;
                    }
                    attributionSource2 = attributionSource;
                } else {
                    str7 = str15;
                    i = i2;
                    str8 = str14;
                    attributionSource2 = attributionSource;
                    perm = com.android.server.am.ActivityManagerService.checkComponentPermission(requiredPermission, filter.receiverList.pid, filter.receiverList.uid, -1, true);
                }
                if (perm != 0) {
                    return str13 + r.intent.toString() + " to " + filter.receiverList.app + " (pid=" + filter.receiverList.pid + ", uid=" + filter.receiverList.uid + str11 + requiredPermission + " due to sender " + r.callerPackage + " (uid " + r.callingUid + ")";
                }
                if (com.android.server.am.Flags.usePermissionManagerForBroadcastDeliveryCheck()) {
                    str9 = str11;
                    str10 = str13;
                } else {
                    int appOp = android.app.AppOpsManager.permissionToOpCode(requiredPermission);
                    if (appOp == -1 || appOp == r.appOp) {
                        str9 = str11;
                        str10 = str13;
                    } else {
                        str9 = str11;
                        str10 = str13;
                        if (broadcastSkipPolicy2.mService.getAppOpsManager().noteOpNoThrow(appOp, filter.receiverList.uid, filter.packageName, filter.featureId, str7 + filter.receiverId) != 0) {
                            return str8 + r.intent.toString() + " to " + filter.receiverList.app + " (pid=" + filter.receiverList.pid + ", uid=" + filter.receiverList.uid + str16 + android.app.AppOpsManager.permissionToOp(requiredPermission) + " due to sender " + r.callerPackage + " (uid " + r.callingUid + ")";
                        }
                    }
                }
                i2 = i + 1;
                str14 = str8;
                str12 = str16;
                str15 = str7;
                attributionSource = attributionSource2;
                str11 = str9;
                str13 = str10;
            }
        }
        if ((r.requiredPermissions == null || r.requiredPermissions.length == 0) && com.android.server.am.ActivityManagerService.checkComponentPermission(null, filter.receiverList.pid, filter.receiverList.uid, -1, true) != 0) {
            return "Permission Denial: security check failed when receiving " + r.intent.toString() + " to " + filter.receiverList.app + " (pid=" + filter.receiverList.pid + ", uid=" + filter.receiverList.uid + str3 + r.callerPackage + " (uid " + r.callingUid + ")";
        }
        if (r.excludedPermissions == null || r.excludedPermissions.length <= 0) {
            str6 = str5;
        } else {
            int i3 = 0;
            while (i3 < r.excludedPermissions.length) {
                java.lang.String excludedPermission = r.excludedPermissions[i3];
                java.lang.String str17 = str5;
                int perm2 = com.android.server.am.ActivityManagerService.checkComponentPermission(excludedPermission, filter.receiverList.pid, filter.receiverList.uid, -1, true);
                int appOp2 = android.app.AppOpsManager.permissionToOpCode(excludedPermission);
                if (appOp2 != -1) {
                    if (perm2 == 0 && broadcastSkipPolicy2.mService.getAppOpsManager().checkOpNoThrow(appOp2, filter.receiverList.uid, filter.packageName) == 0) {
                        return str4 + r.intent.toString() + " to " + filter.receiverList.app + " (pid=" + filter.receiverList.pid + ", uid=" + filter.receiverList.uid + ") excludes appop " + android.app.AppOpsManager.permissionToOp(excludedPermission) + " due to sender " + r.callerPackage + " (uid " + r.callingUid + ")";
                    }
                } else if (perm2 == 0) {
                    return str2 + r.intent.toString() + " to " + filter.receiverList.app + " (pid=" + filter.receiverList.pid + ", uid=" + filter.receiverList.uid + ") excludes " + excludedPermission + " due to sender " + r.callerPackage + " (uid " + r.callingUid + ")";
                }
                i3++;
                broadcastSkipPolicy2 = this;
                str2 = str2;
                str5 = str17;
            }
            str6 = str5;
        }
        if (r.excludedPackages != null && r.excludedPackages.length > 0 && com.android.internal.util.ArrayUtils.contains(r.excludedPackages, filter.packageName)) {
            return "Skipping delivery of excluded package " + r.intent.toString() + " to " + filter.receiverList.app + " (pid=" + filter.receiverList.pid + ", uid=" + filter.receiverList.uid + ") excludes package " + filter.packageName + " due to sender " + r.callerPackage + " (uid " + r.callingUid + ")";
        }
        if (r.appOp == -1) {
            broadcastSkipPolicy = this;
        } else {
            broadcastSkipPolicy = this;
            if (broadcastSkipPolicy.mService.getAppOpsManager().noteOpNoThrow(r.appOp, filter.receiverList.uid, filter.packageName, filter.featureId, str + filter.receiverId) != 0) {
                return str4 + r.intent.toString() + " to " + filter.receiverList.app + " (pid=" + filter.receiverList.pid + ", uid=" + filter.receiverList.uid + str6 + android.app.AppOpsManager.opToName(r.appOp) + " due to sender " + r.callerPackage + " (uid " + r.callingUid + ")";
            }
        }
        int originalCallingUid = r.sticky ? r.originalStickyCallingUid : r.callingUid;
        if (!filter.exported && com.android.server.am.ActivityManagerService.checkComponentPermission(null, r.callingPid, originalCallingUid, filter.receiverList.uid, filter.exported) != 0) {
            return "Exported Denial: sending " + r.intent.toString() + ", action: " + r.intent.getAction() + " from " + r.callerPackage + " (uid=" + originalCallingUid + ") due to receiver " + filter.receiverList.app + " (uid " + filter.receiverList.uid + ") not specifying RECEIVER_EXPORTED";
        }
        if (!broadcastSkipPolicy.requestStartTargetPermissionsReviewIfNeededLocked(r, filter.packageName, filter.owningUserId)) {
            return "Skipping delivery to " + filter.packageName + " due to permissions review";
        }
        return broadcastSkipPolicy.mBroadcastSkipPolicyExt.shouldSkipMessage(r, filter);
    }

    private static java.lang.String broadcastDescription(com.android.server.am.BroadcastRecord r, android.content.ComponentName component) {
        return r.intent.toString() + " from " + r.callerPackage + " (pid=" + r.callingPid + ", uid=" + r.callingUid + ") to " + component.flattenToShortString();
    }

    private boolean noteOpForManifestReceiver(int appOp, com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info, android.content.ComponentName component) {
        if (com.android.internal.util.ArrayUtils.isEmpty(info.activityInfo.attributionTags)) {
            return noteOpForManifestReceiverInner(appOp, r, info, component, null);
        }
        for (java.lang.String tag : info.activityInfo.attributionTags) {
            if (!noteOpForManifestReceiverInner(appOp, r, info, component, tag)) {
                return false;
            }
        }
        return true;
    }

    private boolean noteOpForManifestReceiverInner(int appOp, com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info, android.content.ComponentName component, java.lang.String tag) {
        if (this.mService.getAppOpsManager().noteOpNoThrow(appOp, info.activityInfo.applicationInfo.uid, info.activityInfo.packageName, tag, "Broadcast delivered to " + info.activityInfo.name) != 0) {
            android.util.Slog.w(com.android.server.am.BroadcastQueue.TAG, "Appop Denial: receiving " + r.intent + " to " + component.flattenToShortString() + " requires appop " + android.app.AppOpsManager.opToName(appOp) + " due to sender " + r.callerPackage + " (uid " + r.callingUid + ")");
            return false;
        }
        return true;
    }

    private static boolean isSignaturePerm(java.lang.String[] perms) {
        if (perms == null) {
            return false;
        }
        android.permission.IPermissionManager pm = android.app.AppGlobals.getPermissionManager();
        for (int i = perms.length - 1; i >= 0; i--) {
            try {
                android.content.pm.PermissionInfo pi = pm.getPermissionInfo(perms[i], com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 0);
                if (pi == null || (pi.protectionLevel & 31) != 2) {
                    return false;
                }
            } catch (android.os.RemoteException e) {
                return false;
            }
        }
        return true;
    }

    private boolean requestStartTargetPermissionsReviewIfNeededLocked(com.android.server.am.BroadcastRecord receiverRecord, java.lang.String receivingPackageName, final int receivingUserId) throws java.lang.Throwable {
        if (!this.mService.getPackageManagerInternal().isPermissionsReviewRequired(receivingPackageName, receivingUserId)) {
            return true;
        }
        boolean callerForeground = receiverRecord.callerApp == null || receiverRecord.callerApp.mState.getSetSchedGroup() != 0;
        if (!callerForeground || receiverRecord.intent.getComponent() == null) {
            android.util.Slog.w(com.android.server.am.BroadcastQueue.TAG, "u" + receivingUserId + " Receiving a broadcast in package" + receivingPackageName + " requires a permissions review");
        } else {
            com.android.server.am.PendingIntentRecord intentSender = this.mService.mPendingIntentController.getIntentSender(1, receiverRecord.callerPackage, receiverRecord.callerFeatureId, receiverRecord.callingUid, receiverRecord.userId, null, null, 0, new android.content.Intent[]{receiverRecord.intent}, new java.lang.String[]{receiverRecord.intent.resolveType(this.mService.mContext.getContentResolver())}, 1409286144, null);
            final android.content.Intent intent = new android.content.Intent("android.intent.action.REVIEW_PERMISSIONS");
            intent.addFlags(411041792);
            intent.putExtra("android.intent.extra.PACKAGE_NAME", receivingPackageName);
            intent.putExtra("android.intent.extra.INTENT", new android.content.IntentSender(intentSender));
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PERMISSIONS_REVIEW) {
                android.util.Slog.i(com.android.server.am.BroadcastQueue.TAG, "u" + receivingUserId + " Launching permission review for package " + receivingPackageName);
            }
            this.mService.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BroadcastSkipPolicy.1
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.am.BroadcastSkipPolicy.this.mService.mContext.startActivityAsUser(intent, new android.os.UserHandle(receivingUserId));
                }
            });
        }
        return false;
    }

    private android.permission.PermissionManager getPermissionManager() {
        if (this.mPermissionManager == null) {
            this.mPermissionManager = (android.permission.PermissionManager) this.mService.mContext.getSystemService(android.permission.PermissionManager.class);
        }
        return this.mPermissionManager;
    }

    private boolean hasPermissionForDataDelivery(java.lang.String permission, java.lang.String message, android.content.AttributionSource... attributionSources) {
        android.permission.PermissionManager permissionManager = getPermissionManager();
        if (permissionManager == null) {
            return false;
        }
        for (android.content.AttributionSource attributionSource : attributionSources) {
            int permissionCheckResult = permissionManager.checkPermissionForDataDelivery(permission, attributionSource, message);
            if (permissionCheckResult != 0) {
                return false;
            }
        }
        return true;
    }

    private android.content.AttributionSource[] createAttributionSourcesForResolveInfo(android.content.pm.ResolveInfo info) {
        java.lang.String[] attributionTags = info.activityInfo.attributionTags;
        if (com.android.internal.util.ArrayUtils.isEmpty(attributionTags)) {
            return new android.content.AttributionSource[]{new android.content.AttributionSource.Builder(info.activityInfo.applicationInfo.uid).setPackageName(info.activityInfo.packageName).build()};
        }
        android.content.AttributionSource[] attributionSources = new android.content.AttributionSource[attributionTags.length];
        for (int i = 0; i < attributionTags.length; i++) {
            attributionSources[i] = new android.content.AttributionSource.Builder(info.activityInfo.applicationInfo.uid).setPackageName(info.activityInfo.packageName).setAttributionTag(attributionTags[i]).build();
        }
        return attributionSources;
    }
}
