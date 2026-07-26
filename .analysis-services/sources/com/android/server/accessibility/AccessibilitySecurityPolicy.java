package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilitySecurityPolicy {
    private static final int KEEP_SOURCE_EVENT_TYPES = 71547327;
    private static final java.lang.String LOG_TAG = "AccessibilitySecurityPolicy";
    private static final int OWN_PROCESS_ID = android.os.Process.myPid();
    private final com.android.server.accessibility.AccessibilitySecurityPolicy.AccessibilityUserManager mAccessibilityUserManager;
    private com.android.server.accessibility.AccessibilityWindowManager mAccessibilityWindowManager;
    private final android.app.AppOpsManager mAppOpsManager;
    private android.appwidget.AppWidgetManagerInternal mAppWidgetService;
    private final android.content.Context mContext;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final com.android.server.accessibility.PolicyWarningUIController mPolicyWarningUIController;
    private final android.os.UserManager mUserManager;
    private final android.util.ArraySet<android.content.ComponentName> mNonA11yCategoryServices = new android.util.ArraySet<>();
    private int mCurrentUserId = -10000;
    private boolean mSendNonA11yToolNotificationEnabled = false;

    public interface AccessibilityUserManager {
        int getCurrentUserIdLocked();

        android.util.SparseBooleanArray getVisibleUserIdsLocked();
    }

    public AccessibilitySecurityPolicy(com.android.server.accessibility.PolicyWarningUIController policyWarningUIController, android.content.Context context, com.android.server.accessibility.AccessibilitySecurityPolicy.AccessibilityUserManager a11yUserManager, android.content.pm.PackageManagerInternal packageManagerInternal) {
        this.mContext = context;
        this.mAccessibilityUserManager = a11yUserManager;
        this.mPackageManager = this.mContext.getPackageManager();
        this.mPackageManagerInternal = packageManagerInternal;
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService("user");
        this.mAppOpsManager = (android.app.AppOpsManager) context.getSystemService("appops");
        this.mPolicyWarningUIController = policyWarningUIController;
    }

    public void setSendingNonA11yToolNotificationLocked(boolean enable) {
        if (enable == this.mSendNonA11yToolNotificationEnabled) {
            return;
        }
        this.mSendNonA11yToolNotificationEnabled = enable;
        this.mPolicyWarningUIController.enableSendingNonA11yToolNotification(enable);
        if (enable) {
            for (int i = 0; i < this.mNonA11yCategoryServices.size(); i++) {
                android.content.ComponentName service = this.mNonA11yCategoryServices.valueAt(i);
                this.mPolicyWarningUIController.onNonA11yCategoryServiceBound(this.mCurrentUserId, service);
            }
        }
    }

    public void setAccessibilityWindowManager(com.android.server.accessibility.AccessibilityWindowManager awm) {
        this.mAccessibilityWindowManager = awm;
    }

    public void setAppWidgetManager(android.appwidget.AppWidgetManagerInternal appWidgetManager) {
        this.mAppWidgetService = appWidgetManager;
    }

    public boolean canDispatchAccessibilityEventLocked(int userId, android.view.accessibility.AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case 32:
            case 64:
            case 128:
            case 256:
            case 512:
            case 1024:
            case 16384:
            case 262144:
            case 524288:
            case 1048576:
            case 2097152:
            case 4194304:
            case 16777216:
                return true;
            default:
                return isRetrievalAllowingWindowLocked(userId, event.getWindowId());
        }
    }

    public java.lang.String resolveValidReportedPackageLocked(java.lang.CharSequence packageName, int appId, int userId, int pid) {
        if (packageName == null) {
            return null;
        }
        if (appId == 1000) {
            return packageName.toString();
        }
        java.lang.String packageNameStr = packageName.toString();
        int resolvedUid = android.os.UserHandle.getUid(userId, appId);
        if (isValidPackageForUid(packageNameStr, resolvedUid)) {
            return packageName.toString();
        }
        if (this.mAppWidgetService != null && com.android.internal.util.ArrayUtils.contains(this.mAppWidgetService.getHostedWidgetPackages(resolvedUid), packageNameStr)) {
            return packageName.toString();
        }
        if (this.mContext.checkPermission("android.permission.ACT_AS_PACKAGE_FOR_ACCESSIBILITY", pid, resolvedUid) == 0) {
            return packageName.toString();
        }
        java.lang.String[] packageNames = this.mPackageManager.getPackagesForUid(resolvedUid);
        if (com.android.internal.util.ArrayUtils.isEmpty(packageNames)) {
            return null;
        }
        return packageNames[0];
    }

    public java.lang.String[] computeValidReportedPackages(java.lang.String targetPackage, int targetUid) {
        android.util.ArraySet<java.lang.String> widgetPackages;
        if (android.os.UserHandle.getAppId(targetUid) == 1000) {
            return libcore.util.EmptyArray.STRING;
        }
        java.lang.String[] uidPackages = {targetPackage};
        if (this.mAppWidgetService != null && (widgetPackages = this.mAppWidgetService.getHostedWidgetPackages(targetUid)) != null && !widgetPackages.isEmpty()) {
            java.lang.String[] validPackages = new java.lang.String[uidPackages.length + widgetPackages.size()];
            java.lang.System.arraycopy(uidPackages, 0, validPackages, 0, uidPackages.length);
            int widgetPackageCount = widgetPackages.size();
            for (int i = 0; i < widgetPackageCount; i++) {
                validPackages[uidPackages.length + i] = widgetPackages.valueAt(i);
            }
            return validPackages;
        }
        return uidPackages;
    }

    public void updateEventSourceLocked(android.view.accessibility.AccessibilityEvent event) {
        if ((event.getEventType() & KEEP_SOURCE_EVENT_TYPES) == 0) {
            event.setSource(null);
        }
    }

    public boolean canGetAccessibilityNodeInfoLocked(int userId, com.android.server.accessibility.AbstractAccessibilityServiceConnection service, int windowId) {
        return canRetrieveWindowContentLocked(service) && isRetrievalAllowingWindowLocked(userId, windowId);
    }

    public boolean canRetrieveWindowsLocked(com.android.server.accessibility.AbstractAccessibilityServiceConnection service) {
        return canRetrieveWindowContentLocked(service) && service.mRetrieveInteractiveWindows;
    }

    public boolean canRetrieveWindowContentLocked(com.android.server.accessibility.AbstractAccessibilityServiceConnection service) {
        return (service.getCapabilities() & 1) != 0;
    }

    public boolean canControlMagnification(com.android.server.accessibility.AbstractAccessibilityServiceConnection service) {
        return (service.getCapabilities() & 16) != 0;
    }

    public boolean canPerformGestures(com.android.server.accessibility.AccessibilityServiceConnection service) {
        return (service.getCapabilities() & 32) != 0;
    }

    public boolean canCaptureFingerprintGestures(com.android.server.accessibility.AccessibilityServiceConnection service) {
        return (service.getCapabilities() & 64) != 0;
    }

    public boolean canTakeScreenshotLocked(com.android.server.accessibility.AbstractAccessibilityServiceConnection service) {
        return (service.getCapabilities() & 128) != 0;
    }

    int canEnableDisableInputMethod(java.lang.String imeId, com.android.server.accessibility.AbstractAccessibilityServiceConnection service) throws java.lang.SecurityException {
        java.lang.String servicePackageName = service.getComponentName().getPackageName();
        int callingUserId = android.os.UserHandle.getCallingUserId();
        android.view.inputmethod.InputMethodInfo inputMethodInfo = null;
        java.util.List<android.view.inputmethod.InputMethodInfo> inputMethodInfoList = com.android.server.inputmethod.InputMethodManagerInternal.get().getInputMethodListAsUser(callingUserId);
        if (inputMethodInfoList != null) {
            java.util.Iterator<android.view.inputmethod.InputMethodInfo> it = inputMethodInfoList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.view.inputmethod.InputMethodInfo info = it.next();
                if (info.getId().equals(imeId)) {
                    inputMethodInfo = info;
                    break;
                }
            }
        }
        if (inputMethodInfo == null || !inputMethodInfo.getPackageName().equals(servicePackageName)) {
            throw new java.lang.SecurityException("The input method is in a different package with the accessibility service");
        }
        if (com.android.server.accessibility.RestrictedLockUtilsInternal.checkIfInputMethodDisallowed(this.mContext, inputMethodInfo.getPackageName(), callingUserId) != null) {
            return 1;
        }
        return 0;
    }

    public int resolveProfileParentLocked(int userId) {
        if (userId != this.mAccessibilityUserManager.getCurrentUserIdLocked()) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.content.pm.UserInfo parent = this.mUserManager.getProfileParent(userId);
                if (parent != null) {
                    return parent.getUserHandle().getIdentifier();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        return userId;
    }

    public int resolveCallingUserIdEnforcingPermissionsLocked(int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        int currentUserId = this.mAccessibilityUserManager.getCurrentUserIdLocked();
        if (callingUid == 0 || callingUid == 1000 || callingUid == 2000) {
            if (userId == -2 || userId == -3) {
                return currentUserId;
            }
            return resolveProfileParentLocked(userId);
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (callingUserId == userId) {
            return resolveProfileParentLocked(userId);
        }
        int callingUserParentId = resolveProfileParentLocked(callingUserId);
        if (callingUserParentId == currentUserId && (userId == -2 || userId == -3)) {
            return currentUserId;
        }
        if (!hasPermission("android.permission.INTERACT_ACROSS_USERS") && !hasPermission("android.permission.INTERACT_ACROSS_USERS_FULL")) {
            throw new java.lang.SecurityException("Call from user " + callingUserId + " as user " + userId + " without permission INTERACT_ACROSS_USERS or INTERACT_ACROSS_USERS_FULL not allowed.");
        }
        if (userId == -2 || userId == -3) {
            return currentUserId;
        }
        return resolveProfileParentLocked(userId);
    }

    public boolean isCallerInteractingAcrossUsers(int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        return android.os.Binder.getCallingPid() == android.os.Process.myPid() || callingUid == 2000 || userId == -2 || userId == -3;
    }

    private boolean isValidPackageForUid(java.lang.String packageName, int uid) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return this.mPackageManagerInternal.isSameApp(packageName, 4194304L, uid, android.os.UserHandle.getUserId(uid));
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean isRetrievalAllowingWindowLocked(int userId, int windowId) {
        if (android.os.Binder.getCallingUid() == 1000) {
            return true;
        }
        if (android.os.Binder.getCallingUid() != 2000 || isShellAllowedToRetrieveWindowLocked(userId, windowId)) {
            return this.mAccessibilityWindowManager.resolveParentWindowIdLocked(windowId) == this.mAccessibilityWindowManager.getActiveWindowId(userId) || this.mAccessibilityWindowManager.findA11yWindowInfoByIdLocked(windowId) != null;
        }
        return false;
    }

    private boolean isShellAllowedToRetrieveWindowLocked(int userId, int windowId) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.os.IBinder windowToken = this.mAccessibilityWindowManager.getWindowTokenForUserAndWindowIdLocked(userId, windowId);
            if (windowToken == null) {
                return false;
            }
            int windowOwnerUserId = this.mAccessibilityWindowManager.getWindowOwnerUserId(windowToken);
            if (windowOwnerUserId == -10000) {
                return false;
            }
            return !this.mUserManager.hasUserRestriction("no_debugging_features", android.os.UserHandle.of(windowOwnerUserId));
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void enforceCallingPermission(java.lang.String permission, java.lang.String function) {
        if (OWN_PROCESS_ID != android.os.Binder.getCallingPid() && !hasPermission(permission)) {
            throw new java.lang.SecurityException("You do not have " + permission + " required to call " + function + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid());
        }
    }

    public boolean hasPermission(java.lang.String permission) {
        return this.mContext.checkCallingPermission(permission) == 0;
    }

    public boolean canRegisterService(android.content.pm.ServiceInfo serviceInfo) {
        if (!"android.permission.BIND_ACCESSIBILITY_SERVICE".equals(serviceInfo.permission)) {
            android.util.Slog.w(LOG_TAG, "Skipping accessibility service " + new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name).flattenToShortString() + ": it does not require the permission android.permission.BIND_ACCESSIBILITY_SERVICE");
            return false;
        }
        if ((serviceInfo.flags & 4) != 0) {
            android.util.Slog.w(LOG_TAG, "Skipping accessibility service " + new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name).flattenToShortString() + ": the service is the external one and doesn't allow to register as an accessibility service ");
            return false;
        }
        int servicePackageUid = serviceInfo.applicationInfo.uid;
        if (this.mAppOpsManager.noteOpNoThrow("android:bind_accessibility_service", servicePackageUid, serviceInfo.packageName, null, null) != 0) {
            android.util.Slog.w(LOG_TAG, "Skipping accessibility service " + new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name).flattenToShortString() + ": disallowed by AppOps");
            return false;
        }
        return true;
    }

    public boolean checkAccessibilityAccess(com.android.server.accessibility.AbstractAccessibilityServiceConnection service) {
        java.lang.String packageName = service.getComponentName().getPackageName();
        android.content.pm.ResolveInfo resolveInfo = service.getServiceInfo().getResolveInfo();
        if (resolveInfo == null) {
            return true;
        }
        int servicePackageUid = resolveInfo.serviceInfo.applicationInfo.uid;
        int callingPid = android.os.Binder.getCallingPid();
        long identityToken = android.os.Binder.clearCallingIdentity();
        java.lang.String attributionTag = service.getAttributionTag();
        try {
            if (OWN_PROCESS_ID == callingPid) {
                return this.mAppOpsManager.noteOpNoThrow("android:access_accessibility", servicePackageUid, packageName, attributionTag, null) == 0;
            }
            return this.mAppOpsManager.noteOp("android:access_accessibility", servicePackageUid, packageName, attributionTag, null) == 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(identityToken);
        }
    }

    public void enforceCallingOrSelfPermission(java.lang.String permission) {
        if (this.mContext.checkCallingOrSelfPermission(permission) != 0) {
            throw new java.lang.SecurityException("Caller does not hold permission " + permission);
        }
    }

    public void checkForAccessibilityPermissionOrRole() {
        boolean canManageAccessibility = this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_ACCESSIBILITY") == 0;
        if (canManageAccessibility) {
            return;
        }
        int callingUid = android.os.Binder.getCallingUid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.app.role.RoleManager roleManager = (android.app.role.RoleManager) this.mContext.getSystemService(android.app.role.RoleManager.class);
            if (roleManager != null) {
                java.util.List<java.lang.String> holders = roleManager.getRoleHoldersAsUser("android.app.role.COMPANION_DEVICE_APP_STREAMING", android.os.UserHandle.getUserHandleForUid(callingUid));
                java.lang.String[] packageNames = this.mPackageManager.getPackagesForUid(callingUid);
                if (packageNames != null) {
                    for (java.lang.String packageName : packageNames) {
                        if (holders.contains(packageName)) {
                            return;
                        }
                    }
                }
            }
            throw new java.lang.SecurityException("Cannot register a proxy for a device without the android.app.role.COMPANION_DEVICE_APP_STREAMING role or the MANAGE_ACCESSIBILITY permission.");
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onBoundServicesChangedLocked(int userId, java.util.ArrayList<com.android.server.accessibility.AccessibilityServiceConnection> boundServices) {
        if (this.mAccessibilityUserManager.getCurrentUserIdLocked() != userId) {
            return;
        }
        android.util.ArraySet<android.content.ComponentName> tempNonA11yCategoryServices = new android.util.ArraySet<>();
        for (int i = 0; i < boundServices.size(); i++) {
            android.accessibilityservice.AccessibilityServiceInfo a11yServiceInfo = boundServices.get(i).getServiceInfo();
            android.content.ComponentName service = a11yServiceInfo.getComponentName().clone();
            if (!a11yServiceInfo.isAccessibilityTool()) {
                tempNonA11yCategoryServices.add(service);
                if (this.mNonA11yCategoryServices.contains(service)) {
                    this.mNonA11yCategoryServices.remove(service);
                } else if (this.mSendNonA11yToolNotificationEnabled) {
                    this.mPolicyWarningUIController.onNonA11yCategoryServiceBound(userId, service);
                }
            }
        }
        for (int i2 = 0; i2 < this.mNonA11yCategoryServices.size(); i2++) {
            this.mPolicyWarningUIController.onNonA11yCategoryServiceUnbound(userId, this.mNonA11yCategoryServices.valueAt(i2));
        }
        this.mNonA11yCategoryServices.clear();
        this.mNonA11yCategoryServices.addAll((android.util.ArraySet<? extends android.content.ComponentName>) tempNonA11yCategoryServices);
    }

    public void onSwitchUserLocked(int userId, java.util.Set<android.content.ComponentName> enabledServices) {
        if (this.mCurrentUserId == userId) {
            return;
        }
        this.mPolicyWarningUIController.onSwitchUser(userId, new android.util.ArraySet(enabledServices));
        for (int i = 0; i < this.mNonA11yCategoryServices.size(); i++) {
            this.mPolicyWarningUIController.onNonA11yCategoryServiceUnbound(this.mCurrentUserId, this.mNonA11yCategoryServices.valueAt(i));
        }
        this.mNonA11yCategoryServices.clear();
        this.mCurrentUserId = userId;
    }

    public void onEnabledServicesChangedLocked(int userId, java.util.Set<android.content.ComponentName> enabledServices) {
        if (this.mAccessibilityUserManager.getCurrentUserIdLocked() != userId) {
            return;
        }
        this.mPolicyWarningUIController.onEnabledServicesChanged(userId, new android.util.ArraySet(enabledServices));
    }
}
