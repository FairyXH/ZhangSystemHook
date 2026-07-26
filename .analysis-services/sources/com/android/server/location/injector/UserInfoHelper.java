package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class UserInfoHelper {
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.UserInfoHelper.UserListener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface UserListener {
        public static final int CURRENT_USER_CHANGED = 1;
        public static final int USER_STARTED = 2;
        public static final int USER_STOPPED = 3;
        public static final int USER_VISIBILITY_CHANGED = 4;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface UserChange {
        }

        void onUserChanged(int i, int i2);
    }

    public abstract void dump(java.io.FileDescriptor fileDescriptor, android.util.IndentingPrintWriter indentingPrintWriter, java.lang.String[] strArr);

    public abstract int getCurrentUserId();

    protected abstract int[] getProfileIds(int i);

    public abstract int[] getRunningUserIds();

    public abstract boolean isCurrentUserId(int i);

    public abstract boolean isVisibleUserId(int i);

    public final void addListener(com.android.server.location.injector.UserInfoHelper.UserListener listener) {
        this.mListeners.add(listener);
    }

    public final void removeListener(com.android.server.location.injector.UserInfoHelper.UserListener listener) {
        this.mListeners.remove(listener);
    }

    protected final void dispatchOnUserStarted(int userId) {
        if (com.android.server.location.LocationManagerService.D) {
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "u" + userId + " started");
        }
        for (com.android.server.location.injector.UserInfoHelper.UserListener listener : this.mListeners) {
            listener.onUserChanged(userId, 2);
        }
    }

    protected final void dispatchOnUserStopped(int userId) {
        if (com.android.server.location.LocationManagerService.D) {
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "u" + userId + " stopped");
        }
        for (com.android.server.location.injector.UserInfoHelper.UserListener listener : this.mListeners) {
            listener.onUserChanged(userId, 3);
        }
    }

    protected final void dispatchOnCurrentUserChanged(int fromUserId, int toUserId) {
        int[] fromUserIds = getProfileIds(fromUserId);
        int[] toUserIds = getProfileIds(toUserId);
        if (com.android.server.location.LocationManagerService.D) {
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "current user changed from u" + java.util.Arrays.toString(fromUserIds) + " to u" + java.util.Arrays.toString(toUserIds));
        }
        com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logUserSwitched(fromUserId, toUserId);
        java.util.Iterator<com.android.server.location.injector.UserInfoHelper.UserListener> it = this.mListeners.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.android.server.location.injector.UserInfoHelper.UserListener listener = it.next();
            for (int userId : fromUserIds) {
                listener.onUserChanged(userId, 1);
            }
        }
        for (com.android.server.location.injector.UserInfoHelper.UserListener listener2 : this.mListeners) {
            for (int userId2 : toUserIds) {
                listener2.onUserChanged(userId2, 1);
            }
        }
    }

    protected final void dispatchOnVisibleUserChanged(int userId, boolean visible) {
        if (com.android.server.location.LocationManagerService.D) {
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "visibility of u" + userId + " changed to " + (visible ? com.android.server.wm.ActivityTaskManagerService.DUMP_VISIBLE_ACTIVITIES : "invisible"));
        }
        com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logUserVisibilityChanged(userId, visible);
        for (com.android.server.location.injector.UserInfoHelper.UserListener listener : this.mListeners) {
            listener.onUserChanged(userId, 4);
        }
    }
}
