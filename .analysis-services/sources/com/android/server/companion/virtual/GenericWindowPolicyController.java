package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
public class GenericWindowPolicyController extends android.window.DisplayWindowPolicyController {
    public static final long ALLOW_SECURE_ACTIVITY_DISPLAY_ON_REMOTE_DEVICE = 201712607;
    private static final android.content.ComponentName BLOCKED_APP_STREAMING_COMPONENT = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.internal.app.BlockedAppStreamingActivity.class.getName());
    private static final java.lang.String TAG = "GenericWindowPolicyController";
    private final com.android.server.companion.virtual.GenericWindowPolicyController.ActivityBlockedCallback mActivityBlockedCallback;
    private boolean mActivityLaunchAllowedByDefault;
    private final android.companion.virtual.VirtualDeviceManager.ActivityListener mActivityListener;
    private final java.util.Set<android.content.ComponentName> mActivityPolicyExemptions;
    private final android.util.ArraySet<android.os.UserHandle> mAllowedUsers;
    private final android.content.AttributionSource mAttributionSource;
    private final boolean mCrossTaskNavigationAllowedByDefault;
    private final android.util.ArraySet<android.content.ComponentName> mCrossTaskNavigationExemptions;
    private final android.content.ComponentName mCustomHomeComponent;
    private final java.util.Set<java.lang.String> mDisplayCategories;
    private final com.android.server.companion.virtual.GenericWindowPolicyController.IntentListenerCallback mIntentListenerCallback;
    private final android.content.ComponentName mPermissionDialogComponent;
    private final com.android.server.companion.virtual.GenericWindowPolicyController.PipBlockedCallback mPipBlockedCallback;
    private final com.android.server.companion.virtual.GenericWindowPolicyController.SecureWindowCallback mSecureWindowCallback;
    private boolean mShowTasksInHostDeviceRecents;
    private final java.lang.Object mGenericWindowPolicyControllerLock = new java.lang.Object();
    private int mDisplayId = -1;
    private boolean mIsMirrorDisplay = false;
    private final java.util.concurrent.CountDownLatch mDisplayIdSetLatch = new java.util.concurrent.CountDownLatch(1);
    private final android.util.ArraySet<java.lang.Integer> mRunningUids = new android.util.ArraySet<>();
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final android.util.ArraySet<com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener> mRunningAppsChangedListeners = new android.util.ArraySet<>();

    public interface ActivityBlockedCallback {
        void onActivityBlocked(int i, android.content.pm.ActivityInfo activityInfo);
    }

    public interface IntentListenerCallback {
        boolean shouldInterceptIntent(android.content.Intent intent);
    }

    public interface PipBlockedCallback {
        void onEnteringPipBlocked(int i);
    }

    public interface RunningAppsChangedListener {
        void onRunningAppsChanged(android.util.ArraySet<java.lang.Integer> arraySet);
    }

    public interface SecureWindowCallback {
        void onSecureWindowShown(int i, int i2);
    }

    public GenericWindowPolicyController(int windowFlags, int systemWindowFlags, android.content.AttributionSource attributionSource, android.util.ArraySet<android.os.UserHandle> allowedUsers, boolean activityLaunchAllowedByDefault, java.util.Set<android.content.ComponentName> activityPolicyExemptions, boolean crossTaskNavigationAllowedByDefault, java.util.Set<android.content.ComponentName> crossTaskNavigationExemptions, android.content.ComponentName permissionDialogComponent, android.companion.virtual.VirtualDeviceManager.ActivityListener activityListener, com.android.server.companion.virtual.GenericWindowPolicyController.PipBlockedCallback pipBlockedCallback, com.android.server.companion.virtual.GenericWindowPolicyController.ActivityBlockedCallback activityBlockedCallback, com.android.server.companion.virtual.GenericWindowPolicyController.SecureWindowCallback secureWindowCallback, com.android.server.companion.virtual.GenericWindowPolicyController.IntentListenerCallback intentListenerCallback, java.util.Set<java.lang.String> displayCategories, boolean showTasksInHostDeviceRecents, android.content.ComponentName customHomeComponent) {
        this.mAttributionSource = attributionSource;
        this.mAllowedUsers = allowedUsers;
        this.mActivityLaunchAllowedByDefault = activityLaunchAllowedByDefault;
        this.mActivityPolicyExemptions = activityPolicyExemptions;
        this.mCrossTaskNavigationAllowedByDefault = crossTaskNavigationAllowedByDefault;
        this.mCrossTaskNavigationExemptions = new android.util.ArraySet<>(crossTaskNavigationExemptions);
        this.mPermissionDialogComponent = permissionDialogComponent;
        this.mActivityBlockedCallback = activityBlockedCallback;
        setInterestedWindowFlags(windowFlags, systemWindowFlags);
        this.mActivityListener = activityListener;
        this.mPipBlockedCallback = pipBlockedCallback;
        this.mSecureWindowCallback = secureWindowCallback;
        this.mIntentListenerCallback = intentListenerCallback;
        this.mDisplayCategories = displayCategories;
        this.mShowTasksInHostDeviceRecents = showTasksInHostDeviceRecents;
        this.mCustomHomeComponent = customHomeComponent;
    }

    void setDisplayId(int displayId, boolean isMirrorDisplay) {
        this.mDisplayId = displayId;
        this.mIsMirrorDisplay = isMirrorDisplay;
        this.mDisplayIdSetLatch.countDown();
    }

    private int waitAndGetDisplayId() {
        try {
            if (!this.mDisplayIdSetLatch.await(10L, java.util.concurrent.TimeUnit.SECONDS)) {
                android.util.Slog.e(TAG, "Timed out while waiting for GWPC displayId to be set.");
                return -1;
            }
            return this.mDisplayId;
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.e(TAG, "Interrupted while waiting for GWPC displayId to be set.");
            return -1;
        }
    }

    private boolean waitAndGetIsMirrorDisplay() {
        try {
            if (!this.mDisplayIdSetLatch.await(10L, java.util.concurrent.TimeUnit.SECONDS)) {
                android.util.Slog.e(TAG, "Timed out while waiting for GWPC isMirrorDisplay to be set.");
                return false;
            }
            return this.mIsMirrorDisplay;
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.e(TAG, "Interrupted while waiting for GWPC isMirrorDisplay to be set.");
            return false;
        }
    }

    public void setShowInHostDeviceRecents(boolean showInHostDeviceRecents) {
        synchronized (this.mGenericWindowPolicyControllerLock) {
            this.mShowTasksInHostDeviceRecents = showInHostDeviceRecents;
        }
    }

    void setActivityLaunchDefaultAllowed(boolean activityLaunchDefaultAllowed) {
        synchronized (this.mGenericWindowPolicyControllerLock) {
            if (this.mActivityLaunchAllowedByDefault != activityLaunchDefaultAllowed) {
                this.mActivityPolicyExemptions.clear();
            }
            this.mActivityLaunchAllowedByDefault = activityLaunchDefaultAllowed;
        }
    }

    void addActivityPolicyExemption(android.content.ComponentName componentName) {
        synchronized (this.mGenericWindowPolicyControllerLock) {
            this.mActivityPolicyExemptions.add(componentName);
        }
    }

    void removeActivityPolicyExemption(android.content.ComponentName componentName) {
        synchronized (this.mGenericWindowPolicyControllerLock) {
            this.mActivityPolicyExemptions.remove(componentName);
        }
    }

    public void registerRunningAppsChangedListener(com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener listener) {
        synchronized (this.mGenericWindowPolicyControllerLock) {
            this.mRunningAppsChangedListeners.add(listener);
        }
    }

    public void unregisterRunningAppsChangedListener(com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener listener) {
        synchronized (this.mGenericWindowPolicyControllerLock) {
            this.mRunningAppsChangedListeners.remove(listener);
        }
    }

    public boolean canActivityBeLaunched(android.content.pm.ActivityInfo activityInfo, android.content.Intent intent, int windowingMode, int launchingFromDisplayId, boolean isNewTask) {
        if (android.companion.virtual.flags.Flags.interceptIntentsBeforeApplyingPolicy()) {
            if (this.mIntentListenerCallback != null && intent != null && this.mIntentListenerCallback.shouldInterceptIntent(intent)) {
                logActivityLaunchBlocked("Virtual device intercepting intent");
                return false;
            }
            if (!canContainActivity(activityInfo, windowingMode, launchingFromDisplayId, isNewTask)) {
                notifyActivityBlocked(activityInfo);
                return false;
            }
            return true;
        }
        if (!canContainActivity(activityInfo, windowingMode, launchingFromDisplayId, isNewTask)) {
            notifyActivityBlocked(activityInfo);
            return false;
        }
        if (this.mIntentListenerCallback != null && intent != null && this.mIntentListenerCallback.shouldInterceptIntent(intent)) {
            logActivityLaunchBlocked("Virtual device intercepting intent");
            return false;
        }
        return true;
    }

    public boolean canContainActivity(android.content.pm.ActivityInfo activityInfo, int windowingMode, int launchingFromDisplayId, boolean isNewTask) {
        if (waitAndGetIsMirrorDisplay()) {
            logActivityLaunchBlocked("Mirror virtual displays cannot contain activities.");
            return false;
        }
        if (!isWindowingModeSupported(windowingMode)) {
            logActivityLaunchBlocked("Virtual device doesn't support windowing mode " + windowingMode);
            return false;
        }
        if ((activityInfo.flags & 65536) == 0) {
            logActivityLaunchBlocked("Activity requires android:canDisplayOnRemoteDevices=true");
            return false;
        }
        android.os.UserHandle activityUser = android.os.UserHandle.getUserHandleForUid(activityInfo.applicationInfo.uid);
        android.content.ComponentName activityComponent = activityInfo.getComponentName();
        if (BLOCKED_APP_STREAMING_COMPONENT.equals(activityComponent) && activityUser.isSystem()) {
            return true;
        }
        if (!activityUser.isSystem() && !this.mAllowedUsers.contains(activityUser)) {
            logActivityLaunchBlocked("Activity launch disallowed from user " + activityUser);
            return false;
        }
        if (!activityMatchesDisplayCategory(activityInfo)) {
            logActivityLaunchBlocked("The activity's required display category '" + activityInfo.requiredDisplayCategory + "' not found on virtual display with the following categories: " + this.mDisplayCategories);
            return false;
        }
        synchronized (this.mGenericWindowPolicyControllerLock) {
            if (!isAllowedByPolicy(this.mActivityLaunchAllowedByDefault, this.mActivityPolicyExemptions, activityComponent)) {
                logActivityLaunchBlocked("Activity launch disallowed by policy: " + activityComponent);
                return false;
            }
            if (isNewTask && launchingFromDisplayId != 0 && !isAllowedByPolicy(this.mCrossTaskNavigationAllowedByDefault, this.mCrossTaskNavigationExemptions, activityComponent)) {
                logActivityLaunchBlocked("Cross task navigation disallowed by policy: " + activityComponent);
                return false;
            }
            if (this.mPermissionDialogComponent == null || !this.mPermissionDialogComponent.equals(activityComponent)) {
                return true;
            }
            logActivityLaunchBlocked("Permission dialog not allowed on virtual device");
            return false;
        }
    }

    private void logActivityLaunchBlocked(java.lang.String reason) {
        android.util.Slog.d(TAG, "Virtual device activity launch disallowed on display " + waitAndGetDisplayId() + ", reason: " + reason);
    }

    public boolean keepActivityOnWindowFlagsChanged(final android.content.pm.ActivityInfo activityInfo, int windowFlags, int systemWindowFlags) {
        final int displayId = waitAndGetDisplayId();
        if ((windowFlags & 8192) != 0 && this.mSecureWindowCallback != null && displayId != -1) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.companion.virtual.GenericWindowPolicyController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$keepActivityOnWindowFlagsChanged$0(displayId, activityInfo);
                }
            });
        }
        if (!android.app.compat.CompatChanges.isChangeEnabled(ALLOW_SECURE_ACTIVITY_DISPLAY_ON_REMOTE_DEVICE, activityInfo.packageName, android.os.UserHandle.getUserHandleForUid(activityInfo.applicationInfo.uid))) {
            if ((windowFlags & 8192) != 0 || (524288 & systemWindowFlags) != 0) {
                notifyActivityBlocked(activityInfo);
                return false;
            }
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$keepActivityOnWindowFlagsChanged$0(int displayId, android.content.pm.ActivityInfo activityInfo) {
        this.mSecureWindowCallback.onSecureWindowShown(displayId, activityInfo.applicationInfo.uid);
    }

    public void onTopActivityChanged(final android.content.ComponentName topActivity, int uid, final int userId) {
        final int displayId = waitAndGetDisplayId();
        if (this.mActivityListener != null && topActivity != null && displayId != -1) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.companion.virtual.GenericWindowPolicyController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTopActivityChanged$1(displayId, topActivity, userId);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTopActivityChanged$1(int displayId, android.content.ComponentName topActivity, int userId) {
        this.mActivityListener.onTopActivityChanged(displayId, topActivity, userId);
    }

    public void onRunningAppsChanged(final android.util.ArraySet<java.lang.Integer> runningUids) {
        synchronized (this.mGenericWindowPolicyControllerLock) {
            this.mRunningUids.clear();
            this.mRunningUids.addAll((android.util.ArraySet<? extends java.lang.Integer>) runningUids);
            final int displayId = waitAndGetDisplayId();
            if (this.mActivityListener != null && this.mRunningUids.isEmpty() && displayId != -1) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.companion.virtual.GenericWindowPolicyController$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onRunningAppsChanged$2(displayId);
                    }
                });
            }
            if (!this.mRunningAppsChangedListeners.isEmpty()) {
                final android.util.ArraySet<com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener> listeners = new android.util.ArraySet<>(this.mRunningAppsChangedListeners);
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.companion.virtual.GenericWindowPolicyController$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.companion.virtual.GenericWindowPolicyController.lambda$onRunningAppsChanged$3(listeners, runningUids);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRunningAppsChanged$2(int displayId) {
        this.mActivityListener.onDisplayEmpty(displayId);
    }

    static /* synthetic */ void lambda$onRunningAppsChanged$3(android.util.ArraySet listeners, android.util.ArraySet runningUids) {
        java.util.Iterator it = listeners.iterator();
        while (it.hasNext()) {
            com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener listener = (com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener) it.next();
            listener.onRunningAppsChanged(runningUids);
        }
    }

    public boolean canShowTasksInHostDeviceRecents() {
        boolean z;
        synchronized (this.mGenericWindowPolicyControllerLock) {
            z = this.mShowTasksInHostDeviceRecents;
        }
        return z;
    }

    public boolean isEnteringPipAllowed(final int uid) {
        if (super.isEnteringPipAllowed(uid)) {
            return true;
        }
        if (this.mPipBlockedCallback != null) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.companion.virtual.GenericWindowPolicyController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$isEnteringPipAllowed$4(uid);
                }
            });
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$isEnteringPipAllowed$4(int uid) {
        this.mPipBlockedCallback.onEnteringPipBlocked(uid);
    }

    public android.content.ComponentName getCustomHomeComponent() {
        return this.mCustomHomeComponent;
    }

    boolean containsUid(int uid) {
        boolean zContains;
        synchronized (this.mGenericWindowPolicyControllerLock) {
            zContains = this.mRunningUids.contains(java.lang.Integer.valueOf(uid));
        }
        return zContains;
    }

    private boolean activityMatchesDisplayCategory(android.content.pm.ActivityInfo activityInfo) {
        return this.mDisplayCategories.isEmpty() ? activityInfo.requiredDisplayCategory == null : activityInfo.requiredDisplayCategory != null && this.mDisplayCategories.contains(activityInfo.requiredDisplayCategory);
    }

    private void notifyActivityBlocked(android.content.pm.ActivityInfo activityInfo) {
        int displayId = waitAndGetDisplayId();
        if (!waitAndGetIsMirrorDisplay() && this.mActivityBlockedCallback != null && displayId != -1) {
            this.mActivityBlockedCallback.onActivityBlocked(displayId, activityInfo);
        }
        if (android.companion.virtualdevice.flags.Flags.metricsCollection()) {
            com.android.modules.expresslog.Counter.logIncrementWithUid("virtual_devices.value_activity_blocked_count", this.mAttributionSource.getUid());
        }
    }

    private static boolean isAllowedByPolicy(boolean allowedByDefault, java.util.Set<android.content.ComponentName> exemptions, android.content.ComponentName component) {
        return allowedByDefault != exemptions.contains(component);
    }

    int getRunningAppsChangedListenersSizeForTesting() {
        int size;
        synchronized (this.mGenericWindowPolicyControllerLock) {
            size = this.mRunningAppsChangedListeners.size();
        }
        return size;
    }
}
