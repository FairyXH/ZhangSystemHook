package com.android.server;

/* JADX INFO: loaded from: classes.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public abstract class SystemService {
    protected static final boolean DEBUG_USER = false;
    public static final int PHASE_ACTIVITY_MANAGER_READY = 550;
    public static final int PHASE_BOOT_COMPLETED = 1000;
    public static final int PHASE_DEVICE_SPECIFIC_SERVICES_READY = 520;
    public static final int PHASE_LOCK_SETTINGS_READY = 480;
    public static final int PHASE_SYSTEM_SERVICES_READY = 500;
    public static final int PHASE_THIRD_PARTY_APPS_CAN_START = 600;
    public static final int PHASE_WAIT_FOR_DEFAULT_DISPLAY = 100;
    public static final int PHASE_WAIT_FOR_SENSOR_SERVICE = 200;
    private final android.content.Context mContext;
    private final java.util.List<java.lang.Class<?>> mDependencies;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface BootPhase {
    }

    public abstract void onStart();

    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
    public static final class TargetUser {
        private final boolean mFull;
        private final boolean mPreCreated;
        private final boolean mProfile;
        private final int mUserId;
        private final java.lang.String mUserType;

        public TargetUser(android.content.pm.UserInfo userInfo) {
            this.mUserId = userInfo.id;
            this.mFull = userInfo.isFull();
            this.mProfile = userInfo.isProfile();
            this.mUserType = userInfo.userType;
            this.mPreCreated = userInfo.preCreated;
        }

        public boolean isFull() {
            return this.mFull;
        }

        public boolean isProfile() {
            return this.mProfile;
        }

        public boolean isManagedProfile() {
            return android.os.UserManager.isUserTypeManagedProfile(this.mUserType);
        }

        public boolean isPreCreated() {
            return this.mPreCreated;
        }

        public android.os.UserHandle getUserHandle() {
            return android.os.UserHandle.of(this.mUserId);
        }

        public int getUserIdentifier() {
            return this.mUserId;
        }

        public java.lang.String toString() {
            return java.lang.Integer.toString(this.mUserId);
        }

        public void dump(java.io.PrintWriter pw) {
            pw.print(getUserIdentifier());
            if (isFull() || isProfile()) {
                pw.print('(');
                if (isFull()) {
                    pw.print("full");
                }
                if (isProfile()) {
                    if (0 != 0) {
                        pw.print(',');
                    }
                    pw.print("profile");
                }
                pw.print(')');
            }
        }
    }

    public static final class UserCompletedEventType {
        public static final int EVENT_TYPE_USER_STARTING = 1;
        public static final int EVENT_TYPE_USER_SWITCHING = 4;
        public static final int EVENT_TYPE_USER_UNLOCKED = 2;
        private final int mEventType;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface EventTypesFlag {
        }

        UserCompletedEventType(int eventType) {
            this.mEventType = eventType;
        }

        public static com.android.server.SystemService.UserCompletedEventType newUserCompletedEventTypeForTest(int eventType) {
            return new com.android.server.SystemService.UserCompletedEventType(eventType);
        }

        public boolean includesOnUserStarting() {
            return (this.mEventType & 1) != 0;
        }

        public boolean includesOnUserUnlocked() {
            return (this.mEventType & 2) != 0;
        }

        public boolean includesOnUserSwitching() {
            return (this.mEventType & 4) != 0;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("{");
            if (includesOnUserSwitching()) {
                sb.append("|Switching");
            }
            if (includesOnUserUnlocked()) {
                sb.append("|Unlocked");
            }
            if (includesOnUserStarting()) {
                sb.append("|Starting");
            }
            if (sb.length() > 1) {
                sb.append("|");
            }
            sb.append("}");
            return sb.toString();
        }
    }

    public SystemService(android.content.Context context) {
        this(context, java.util.Collections.emptyList());
    }

    public SystemService(android.content.Context context, java.util.List<java.lang.Class<?>> dependencies) {
        this.mContext = context;
        this.mDependencies = (java.util.List) java.util.Objects.requireNonNull(dependencies);
    }

    public final android.content.Context getContext() {
        return this.mContext;
    }

    public final android.content.Context getUiContext() {
        return android.app.ActivityThread.currentActivityThread().getSystemUiContext();
    }

    public final java.util.List<java.lang.Class<?>> getDependencies() {
        return this.mDependencies;
    }

    public final boolean isSafeMode() {
        return getManager().isSafeMode();
    }

    public void onBootPhase(int phase) {
    }

    public boolean isUserSupported(com.android.server.SystemService.TargetUser user) {
        return true;
    }

    protected void dumpSupportedUsers(java.io.PrintWriter pw, java.lang.String prefix) {
        java.util.List<android.content.pm.UserInfo> allUsers = android.os.UserManager.get(this.mContext).getUsers();
        java.util.List<java.lang.Integer> supportedUsers = new java.util.ArrayList<>(allUsers.size());
        for (int i = 0; i < allUsers.size(); i++) {
            android.content.pm.UserInfo user = allUsers.get(i);
            if (isUserSupported(new com.android.server.SystemService.TargetUser(user))) {
                supportedUsers.add(java.lang.Integer.valueOf(user.id));
            }
        }
        if (supportedUsers.isEmpty()) {
            pw.print(prefix);
            pw.println("No supported users");
            return;
        }
        int size = supportedUsers.size();
        pw.print(prefix);
        pw.print(size);
        pw.print(" supported user");
        if (size > 1) {
            pw.print("s");
        }
        pw.print(": ");
        pw.println(supportedUsers);
    }

    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
    }

    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
    }

    public void onUserUnlocked(com.android.server.SystemService.TargetUser user) {
    }

    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
    }

    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
    }

    public void onUserStopped(com.android.server.SystemService.TargetUser user) {
    }

    public void onUserCompletedEvent(com.android.server.SystemService.TargetUser user, com.android.server.SystemService.UserCompletedEventType eventType) {
    }

    protected final void publishBinderService(java.lang.String name, android.os.IBinder service) {
        publishBinderService(name, service, false);
    }

    protected final void publishBinderService(java.lang.String name, android.os.IBinder service, boolean allowIsolated) {
        publishBinderService(name, service, allowIsolated, 8);
    }

    protected final void publishBinderService(java.lang.String name, android.os.IBinder service, boolean allowIsolated, int dumpPriority) {
        android.os.ServiceManager.addService(name, service, allowIsolated, dumpPriority);
    }

    protected final android.os.IBinder getBinderService(java.lang.String name) {
        return android.os.ServiceManager.getService(name);
    }

    protected final <T> void publishLocalService(java.lang.Class<T> type, T service) {
        com.android.server.LocalServices.addService(type, service);
    }

    protected final <T> T getLocalService(java.lang.Class<T> cls) {
        return (T) com.android.server.LocalServices.getService(cls);
    }

    private com.android.server.SystemServiceManager getManager() {
        return (com.android.server.SystemServiceManager) com.android.server.LocalServices.getService(com.android.server.SystemServiceManager.class);
    }
}
