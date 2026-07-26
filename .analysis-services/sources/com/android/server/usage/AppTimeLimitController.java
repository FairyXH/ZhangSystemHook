package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class AppTimeLimitController {
    private static final boolean DEBUG = false;
    private static final long MAX_OBSERVER_PER_UID = 1000;
    private static final java.lang.Integer ONE = new java.lang.Integer(1);
    private static final long ONE_MINUTE = 60000;
    private static final java.lang.String TAG = "AppTimeLimitController";
    private android.app.AlarmManager mAlarmManager;
    private final android.content.Context mContext;
    private final com.android.server.usage.AppTimeLimitController.MyHandler mHandler;
    private com.android.server.usage.AppTimeLimitController.TimeLimitCallbackListener mListener;
    private final com.android.server.usage.AppTimeLimitController.Lock mLock = new com.android.server.usage.AppTimeLimitController.Lock();
    private final android.util.SparseArray<com.android.server.usage.AppTimeLimitController.UserData> mUsers = new android.util.SparseArray<>();
    private final android.util.SparseArray<com.android.server.usage.AppTimeLimitController.ObserverAppData> mObserverApps = new android.util.SparseArray<>();

    public interface TimeLimitCallbackListener {
        void onLimitReached(int i, int i2, long j, long j2, android.app.PendingIntent pendingIntent);

        void onSessionEnd(int i, int i2, long j, android.app.PendingIntent pendingIntent);
    }

    private static class Lock {
        private Lock() {
        }
    }

    private class UserData {
        public final android.util.ArrayMap<java.lang.String, java.lang.Integer> currentlyActive;
        public final android.util.ArrayMap<java.lang.String, java.util.ArrayList<com.android.server.usage.AppTimeLimitController.UsageGroup>> observedMap;
        private int userId;

        private UserData(int userId) {
            this.currentlyActive = new android.util.ArrayMap<>();
            this.observedMap = new android.util.ArrayMap<>();
            this.userId = userId;
        }

        boolean isActive(java.lang.String[] entities) {
            for (java.lang.String str : entities) {
                if (this.currentlyActive.containsKey(str)) {
                    return true;
                }
            }
            return false;
        }

        void addUsageGroup(com.android.server.usage.AppTimeLimitController.UsageGroup group) {
            int size = group.mObserved.length;
            for (int i = 0; i < size; i++) {
                java.util.ArrayList<com.android.server.usage.AppTimeLimitController.UsageGroup> list = this.observedMap.get(group.mObserved[i]);
                if (list == null) {
                    list = new java.util.ArrayList<>();
                    this.observedMap.put(group.mObserved[i], list);
                }
                list.add(group);
            }
        }

        void removeUsageGroup(com.android.server.usage.AppTimeLimitController.UsageGroup group) {
            int size = group.mObserved.length;
            for (int i = 0; i < size; i++) {
                java.lang.String observed = group.mObserved[i];
                java.util.ArrayList<com.android.server.usage.AppTimeLimitController.UsageGroup> list = this.observedMap.get(observed);
                if (list != null) {
                    list.remove(group);
                    if (list.isEmpty()) {
                        this.observedMap.remove(observed);
                    }
                }
            }
        }

        void dump(java.io.PrintWriter pw) {
            pw.print(" userId=");
            pw.println(this.userId);
            pw.print(" Currently Active:");
            int nActive = this.currentlyActive.size();
            for (int i = 0; i < nActive; i++) {
                pw.print(this.currentlyActive.keyAt(i));
                pw.print(", ");
            }
            pw.println();
            pw.print(" Observed Entities:");
            int nEntities = this.observedMap.size();
            for (int i2 = 0; i2 < nEntities; i2++) {
                pw.print(this.observedMap.keyAt(i2));
                pw.print(", ");
            }
            pw.println();
        }
    }

    private class ObserverAppData {
        android.util.SparseArray<com.android.server.usage.AppTimeLimitController.AppUsageGroup> appUsageGroups;
        android.util.SparseArray<com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup> appUsageLimitGroups;
        android.util.SparseArray<com.android.server.usage.AppTimeLimitController.SessionUsageGroup> sessionUsageGroups;
        private int uid;

        private ObserverAppData(int uid) {
            this.appUsageGroups = new android.util.SparseArray<>();
            this.sessionUsageGroups = new android.util.SparseArray<>();
            this.appUsageLimitGroups = new android.util.SparseArray<>();
            this.uid = uid;
        }

        void removeAppUsageGroup(int observerId) {
            this.appUsageGroups.remove(observerId);
        }

        void removeSessionUsageGroup(int observerId) {
            this.sessionUsageGroups.remove(observerId);
        }

        void removeAppUsageLimitGroup(int observerId) {
            this.appUsageLimitGroups.remove(observerId);
        }

        void dump(java.io.PrintWriter pw) {
            pw.print(" uid=");
            pw.println(this.uid);
            pw.println("    App Usage Groups:");
            int nAppUsageGroups = this.appUsageGroups.size();
            for (int i = 0; i < nAppUsageGroups; i++) {
                this.appUsageGroups.valueAt(i).dump(pw);
                pw.println();
            }
            pw.println("    Session Usage Groups:");
            int nSessionUsageGroups = this.sessionUsageGroups.size();
            for (int i2 = 0; i2 < nSessionUsageGroups; i2++) {
                this.sessionUsageGroups.valueAt(i2).dump(pw);
                pw.println();
            }
            pw.println("    App Usage Limit Groups:");
            int nAppUsageLimitGroups = this.appUsageLimitGroups.size();
            for (int i3 = 0; i3 < nAppUsageLimitGroups; i3++) {
                this.appUsageLimitGroups.valueAt(i3).dump(pw);
                pw.println();
            }
        }
    }

    abstract class UsageGroup {
        protected int mActives;
        protected long mLastKnownUsageTimeMs;
        protected long mLastUsageEndTimeMs;
        protected android.app.PendingIntent mLimitReachedCallback;
        protected java.lang.String[] mObserved;
        protected java.lang.ref.WeakReference<com.android.server.usage.AppTimeLimitController.ObserverAppData> mObserverAppRef;
        protected int mObserverId;
        protected long mTimeLimitMs;
        protected long mUsageTimeMs;
        protected java.lang.ref.WeakReference<com.android.server.usage.AppTimeLimitController.UserData> mUserRef;

        UsageGroup(com.android.server.usage.AppTimeLimitController.UserData user, com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp, int observerId, java.lang.String[] observed, long timeLimitMs, android.app.PendingIntent limitReachedCallback) {
            this.mUserRef = new java.lang.ref.WeakReference<>(user);
            this.mObserverAppRef = new java.lang.ref.WeakReference<>(observerApp);
            this.mObserverId = observerId;
            this.mObserved = observed;
            this.mTimeLimitMs = timeLimitMs;
            this.mLimitReachedCallback = limitReachedCallback;
        }

        public long getTimeLimitMs() {
            return this.mTimeLimitMs;
        }

        public long getUsageTimeMs() {
            return this.mUsageTimeMs;
        }

        public void remove() {
            com.android.server.usage.AppTimeLimitController.UserData user = this.mUserRef.get();
            if (user != null) {
                user.removeUsageGroup(this);
            }
            this.mLimitReachedCallback = null;
        }

        void noteUsageStart(long startTimeMs) {
            noteUsageStart(startTimeMs, startTimeMs);
        }

        void noteUsageStart(long startTimeMs, long currentTimeMs) {
            int i = this.mActives;
            this.mActives = i + 1;
            if (i == 0) {
                long startTimeMs2 = this.mLastUsageEndTimeMs > startTimeMs ? this.mLastUsageEndTimeMs : startTimeMs;
                this.mLastKnownUsageTimeMs = startTimeMs2;
                long timeRemaining = ((this.mTimeLimitMs - this.mUsageTimeMs) - currentTimeMs) + startTimeMs2;
                if (timeRemaining > 0) {
                    com.android.server.usage.AppTimeLimitController.this.postCheckTimeoutLocked(this, timeRemaining);
                    return;
                }
                return;
            }
            if (this.mActives > this.mObserved.length) {
                this.mActives = this.mObserved.length;
                com.android.server.usage.AppTimeLimitController.UserData user = this.mUserRef.get();
                if (user == null) {
                    return;
                }
                java.lang.Object[] array = user.currentlyActive.keySet().toArray();
                android.util.Slog.e(com.android.server.usage.AppTimeLimitController.TAG, "Too many noted usage starts! Observed entities: " + java.util.Arrays.toString(this.mObserved) + "   Active Entities: " + java.util.Arrays.toString(array));
            }
        }

        void noteUsageStop(long stopTimeMs) {
            int i = this.mActives - 1;
            this.mActives = i;
            if (i == 0) {
                boolean limitNotCrossed = this.mUsageTimeMs < this.mTimeLimitMs;
                this.mUsageTimeMs += stopTimeMs - this.mLastKnownUsageTimeMs;
                this.mLastUsageEndTimeMs = stopTimeMs;
                if (limitNotCrossed && this.mUsageTimeMs >= this.mTimeLimitMs) {
                    com.android.server.usage.AppTimeLimitController.this.postInformLimitReachedListenerLocked(this);
                }
                com.android.server.usage.AppTimeLimitController.this.cancelCheckTimeoutLocked(this);
                return;
            }
            if (this.mActives < 0) {
                this.mActives = 0;
                com.android.server.usage.AppTimeLimitController.UserData user = this.mUserRef.get();
                if (user == null) {
                    return;
                }
                java.lang.Object[] array = user.currentlyActive.keySet().toArray();
                android.util.Slog.e(com.android.server.usage.AppTimeLimitController.TAG, "Too many noted usage stops! Observed entities: " + java.util.Arrays.toString(this.mObserved) + "   Active Entities: " + java.util.Arrays.toString(array));
            }
        }

        void checkTimeout(long currentTimeMs) {
            com.android.server.usage.AppTimeLimitController.UserData user = this.mUserRef.get();
            if (user == null) {
                return;
            }
            long timeRemainingMs = this.mTimeLimitMs - this.mUsageTimeMs;
            if (timeRemainingMs > 0 && user.isActive(this.mObserved)) {
                long timeUsedMs = currentTimeMs - this.mLastKnownUsageTimeMs;
                if (timeRemainingMs <= timeUsedMs) {
                    this.mUsageTimeMs += timeUsedMs;
                    this.mLastKnownUsageTimeMs = currentTimeMs;
                    com.android.server.usage.AppTimeLimitController.this.postInformLimitReachedListenerLocked(this);
                    return;
                }
                com.android.server.usage.AppTimeLimitController.this.postCheckTimeoutLocked(this, timeRemainingMs - timeUsedMs);
            }
        }

        public void onLimitReached() {
            com.android.server.usage.AppTimeLimitController.UserData user = this.mUserRef.get();
            if (user != null && com.android.server.usage.AppTimeLimitController.this.mListener != null) {
                com.android.server.usage.AppTimeLimitController.this.mListener.onLimitReached(this.mObserverId, user.userId, this.mTimeLimitMs, this.mUsageTimeMs, this.mLimitReachedCallback);
            }
        }

        void dump(java.io.PrintWriter pw) {
            pw.print("        Group id=");
            pw.print(this.mObserverId);
            pw.print(" timeLimit=");
            pw.print(this.mTimeLimitMs);
            pw.print(" used=");
            pw.print(this.mUsageTimeMs);
            pw.print(" lastKnownUsage=");
            pw.print(this.mLastKnownUsageTimeMs);
            pw.print(" mActives=");
            pw.print(this.mActives);
            pw.print(" observed=");
            pw.print(java.util.Arrays.toString(this.mObserved));
        }
    }

    class AppUsageGroup extends com.android.server.usage.AppTimeLimitController.UsageGroup {
        public AppUsageGroup(com.android.server.usage.AppTimeLimitController.UserData user, com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp, int observerId, java.lang.String[] observed, long timeLimitMs, android.app.PendingIntent limitReachedCallback) {
            super(user, observerApp, observerId, observed, timeLimitMs, limitReachedCallback);
        }

        @Override // com.android.server.usage.AppTimeLimitController.UsageGroup
        public void remove() {
            super.remove();
            com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp = this.mObserverAppRef.get();
            if (observerApp != null) {
                observerApp.removeAppUsageGroup(this.mObserverId);
            }
        }

        @Override // com.android.server.usage.AppTimeLimitController.UsageGroup
        public void onLimitReached() {
            super.onLimitReached();
            remove();
        }
    }

    class SessionUsageGroup extends com.android.server.usage.AppTimeLimitController.UsageGroup implements android.app.AlarmManager.OnAlarmListener {
        private long mNewSessionThresholdMs;
        private android.app.PendingIntent mSessionEndCallback;

        public SessionUsageGroup(com.android.server.usage.AppTimeLimitController.UserData user, com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp, int observerId, java.lang.String[] observed, long timeLimitMs, android.app.PendingIntent limitReachedCallback, long newSessionThresholdMs, android.app.PendingIntent sessionEndCallback) {
            super(user, observerApp, observerId, observed, timeLimitMs, limitReachedCallback);
            this.mNewSessionThresholdMs = newSessionThresholdMs;
            this.mSessionEndCallback = sessionEndCallback;
        }

        @Override // com.android.server.usage.AppTimeLimitController.UsageGroup
        public void remove() {
            super.remove();
            com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp = this.mObserverAppRef.get();
            if (observerApp != null) {
                observerApp.removeSessionUsageGroup(this.mObserverId);
            }
            this.mSessionEndCallback = null;
        }

        @Override // com.android.server.usage.AppTimeLimitController.UsageGroup
        public void noteUsageStart(long startTimeMs, long currentTimeMs) {
            if (this.mActives == 0) {
                if (startTimeMs - this.mLastUsageEndTimeMs > this.mNewSessionThresholdMs) {
                    this.mUsageTimeMs = 0L;
                }
                com.android.server.usage.AppTimeLimitController.this.getAlarmManager().cancel(this);
            }
            super.noteUsageStart(startTimeMs, currentTimeMs);
        }

        @Override // com.android.server.usage.AppTimeLimitController.UsageGroup
        public void noteUsageStop(long stopTimeMs) {
            super.noteUsageStop(stopTimeMs);
            if (this.mActives == 0 && this.mUsageTimeMs >= this.mTimeLimitMs) {
                com.android.server.usage.AppTimeLimitController.this.getAlarmManager().setExact(3, com.android.server.usage.AppTimeLimitController.this.getElapsedRealtime() + this.mNewSessionThresholdMs, com.android.server.usage.AppTimeLimitController.TAG, this, com.android.server.usage.AppTimeLimitController.this.mHandler);
            }
        }

        public void onSessionEnd() {
            com.android.server.usage.AppTimeLimitController.UserData user = this.mUserRef.get();
            if (user != null && com.android.server.usage.AppTimeLimitController.this.mListener != null) {
                com.android.server.usage.AppTimeLimitController.this.mListener.onSessionEnd(this.mObserverId, user.userId, this.mUsageTimeMs, this.mSessionEndCallback);
            }
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            synchronized (com.android.server.usage.AppTimeLimitController.this.mLock) {
                onSessionEnd();
            }
        }

        @Override // com.android.server.usage.AppTimeLimitController.UsageGroup
        void dump(java.io.PrintWriter pw) {
            super.dump(pw);
            pw.print(" lastUsageEndTime=");
            pw.print(this.mLastUsageEndTimeMs);
            pw.print(" newSessionThreshold=");
            pw.print(this.mNewSessionThresholdMs);
        }
    }

    class AppUsageLimitGroup extends com.android.server.usage.AppTimeLimitController.UsageGroup {
        public AppUsageLimitGroup(com.android.server.usage.AppTimeLimitController.UserData user, com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp, int observerId, java.lang.String[] observed, long timeLimitMs, long timeUsedMs, android.app.PendingIntent limitReachedCallback) {
            super(user, observerApp, observerId, observed, timeLimitMs, limitReachedCallback);
            this.mUsageTimeMs = timeUsedMs;
        }

        @Override // com.android.server.usage.AppTimeLimitController.UsageGroup
        public void remove() {
            super.remove();
            com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp = this.mObserverAppRef.get();
            if (observerApp != null) {
                observerApp.removeAppUsageLimitGroup(this.mObserverId);
            }
        }

        long getTotaUsageLimit() {
            return this.mTimeLimitMs;
        }

        long getUsageRemaining() {
            if (this.mActives > 0) {
                return (this.mTimeLimitMs - this.mUsageTimeMs) - (com.android.server.usage.AppTimeLimitController.this.getElapsedRealtime() - this.mLastKnownUsageTimeMs);
            }
            return this.mTimeLimitMs - this.mUsageTimeMs;
        }
    }

    private class MyHandler extends android.os.Handler {
        static final int MSG_CHECK_TIMEOUT = 1;
        static final int MSG_INFORM_LIMIT_REACHED_LISTENER = 2;

        MyHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    synchronized (com.android.server.usage.AppTimeLimitController.this.mLock) {
                        ((com.android.server.usage.AppTimeLimitController.UsageGroup) msg.obj).checkTimeout(com.android.server.usage.AppTimeLimitController.this.getElapsedRealtime());
                        break;
                    }
                    return;
                case 2:
                    synchronized (com.android.server.usage.AppTimeLimitController.this.mLock) {
                        ((com.android.server.usage.AppTimeLimitController.UsageGroup) msg.obj).onLimitReached();
                        break;
                    }
                    return;
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    }

    public AppTimeLimitController(android.content.Context context, com.android.server.usage.AppTimeLimitController.TimeLimitCallbackListener listener, android.os.Looper looper) {
        this.mContext = context;
        this.mHandler = new com.android.server.usage.AppTimeLimitController.MyHandler(looper);
        this.mListener = listener;
    }

    protected android.app.AlarmManager getAlarmManager() {
        if (this.mAlarmManager == null) {
            this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        }
        return this.mAlarmManager;
    }

    protected long getElapsedRealtime() {
        return android.os.SystemClock.elapsedRealtime();
    }

    protected long getAppUsageObserverPerUidLimit() {
        return 1000L;
    }

    protected long getUsageSessionObserverPerUidLimit() {
        return 1000L;
    }

    protected long getAppUsageLimitObserverPerUidLimit() {
        return 1000L;
    }

    protected long getMinTimeLimit() {
        return 60000L;
    }

    com.android.server.usage.AppTimeLimitController.AppUsageGroup getAppUsageGroup(int observerAppUid, int observerId) {
        com.android.server.usage.AppTimeLimitController.AppUsageGroup appUsageGroup;
        synchronized (this.mLock) {
            appUsageGroup = getOrCreateObserverAppDataLocked(observerAppUid).appUsageGroups.get(observerId);
        }
        return appUsageGroup;
    }

    com.android.server.usage.AppTimeLimitController.SessionUsageGroup getSessionUsageGroup(int observerAppUid, int observerId) {
        com.android.server.usage.AppTimeLimitController.SessionUsageGroup sessionUsageGroup;
        synchronized (this.mLock) {
            sessionUsageGroup = getOrCreateObserverAppDataLocked(observerAppUid).sessionUsageGroups.get(observerId);
        }
        return sessionUsageGroup;
    }

    com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup getAppUsageLimitGroup(int observerAppUid, int observerId) {
        com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup appUsageLimitGroup;
        synchronized (this.mLock) {
            appUsageLimitGroup = getOrCreateObserverAppDataLocked(observerAppUid).appUsageLimitGroups.get(observerId);
        }
        return appUsageLimitGroup;
    }

    public android.app.usage.UsageStatsManagerInternal.AppUsageLimitData getAppUsageLimit(java.lang.String packageName, android.os.UserHandle user) {
        synchronized (this.mLock) {
            com.android.server.usage.AppTimeLimitController.UserData userData = getOrCreateUserDataLocked(user.getIdentifier());
            if (userData == null) {
                return null;
            }
            java.util.ArrayList<com.android.server.usage.AppTimeLimitController.UsageGroup> usageGroups = userData.observedMap.get(packageName);
            if (usageGroups != null && !usageGroups.isEmpty()) {
                android.util.ArraySet<com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup> usageLimitGroups = new android.util.ArraySet<>();
                for (int i = 0; i < usageGroups.size(); i++) {
                    if (usageGroups.get(i) instanceof com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup) {
                        com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup group = (com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup) usageGroups.get(i);
                        int j = 0;
                        while (true) {
                            if (j >= group.mObserved.length) {
                                break;
                            }
                            if (!group.mObserved[j].equals(packageName)) {
                                j++;
                            } else {
                                usageLimitGroups.add(group);
                                break;
                            }
                        }
                    }
                }
                if (usageLimitGroups.isEmpty()) {
                    return null;
                }
                com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup smallestGroup = usageLimitGroups.valueAt(0);
                for (int i2 = 1; i2 < usageLimitGroups.size(); i2++) {
                    com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup otherGroup = usageLimitGroups.valueAt(i2);
                    if (otherGroup.getUsageRemaining() < smallestGroup.getUsageRemaining()) {
                        smallestGroup = otherGroup;
                    }
                }
                return new android.app.usage.UsageStatsManagerInternal.AppUsageLimitData(smallestGroup.getTotaUsageLimit(), smallestGroup.getUsageRemaining());
            }
            return null;
        }
    }

    private com.android.server.usage.AppTimeLimitController.UserData getOrCreateUserDataLocked(int userId) {
        com.android.server.usage.AppTimeLimitController.UserData userData = this.mUsers.get(userId);
        if (userData == null) {
            com.android.server.usage.AppTimeLimitController.UserData userData2 = new com.android.server.usage.AppTimeLimitController.UserData(userId);
            this.mUsers.put(userId, userData2);
            return userData2;
        }
        return userData;
    }

    private com.android.server.usage.AppTimeLimitController.ObserverAppData getOrCreateObserverAppDataLocked(int uid) {
        com.android.server.usage.AppTimeLimitController.ObserverAppData appData = this.mObserverApps.get(uid);
        if (appData == null) {
            com.android.server.usage.AppTimeLimitController.ObserverAppData appData2 = new com.android.server.usage.AppTimeLimitController.ObserverAppData(uid);
            this.mObserverApps.put(uid, appData2);
            return appData2;
        }
        return appData;
    }

    public void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            this.mUsers.remove(userId);
        }
    }

    private void noteActiveLocked(com.android.server.usage.AppTimeLimitController.UserData user, com.android.server.usage.AppTimeLimitController.UsageGroup group, long currentTimeMs) {
        int size = group.mObserved.length;
        for (int i = 0; i < size; i++) {
            if (user.currentlyActive.containsKey(group.mObserved[i])) {
                group.noteUsageStart(currentTimeMs);
            }
        }
    }

    public void addAppUsageObserver(int requestingUid, int observerId, java.lang.String[] observed, long timeLimit, android.app.PendingIntent callbackIntent, int userId) throws java.lang.Throwable {
        if (timeLimit < getMinTimeLimit()) {
            throw new java.lang.IllegalArgumentException("Time limit must be >= " + getMinTimeLimit());
        }
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.usage.AppTimeLimitController.UserData user = getOrCreateUserDataLocked(userId);
                    com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp = getOrCreateObserverAppDataLocked(requestingUid);
                    com.android.server.usage.AppTimeLimitController.AppUsageGroup group = observerApp.appUsageGroups.get(observerId);
                    if (group != null) {
                        group.remove();
                    }
                    int observerIdCount = observerApp.appUsageGroups.size();
                    if (observerIdCount >= getAppUsageObserverPerUidLimit()) {
                        throw new java.lang.IllegalStateException("Too many app usage observers added by uid " + requestingUid);
                    }
                    com.android.server.usage.AppTimeLimitController.AppUsageGroup group2 = new com.android.server.usage.AppTimeLimitController.AppUsageGroup(user, observerApp, observerId, observed, timeLimit, callbackIntent);
                    observerApp.appUsageGroups.append(observerId, group2);
                    user.addUsageGroup(group2);
                    noteActiveLocked(user, group2, getElapsedRealtime());
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public void removeAppUsageObserver(int requestingUid, int observerId, int userId) {
        synchronized (this.mLock) {
            com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp = getOrCreateObserverAppDataLocked(requestingUid);
            com.android.server.usage.AppTimeLimitController.AppUsageGroup group = observerApp.appUsageGroups.get(observerId);
            if (group != null) {
                group.remove();
            }
        }
    }

    public void addUsageSessionObserver(int requestingUid, int observerId, java.lang.String[] observed, long timeLimit, long sessionThresholdTime, android.app.PendingIntent limitReachedCallbackIntent, android.app.PendingIntent sessionEndCallbackIntent, int userId) throws java.lang.Throwable {
        if (timeLimit < getMinTimeLimit()) {
            throw new java.lang.IllegalArgumentException("Time limit must be >= " + getMinTimeLimit());
        }
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.usage.AppTimeLimitController.UserData user = getOrCreateUserDataLocked(userId);
                    com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp = getOrCreateObserverAppDataLocked(requestingUid);
                    com.android.server.usage.AppTimeLimitController.SessionUsageGroup group = observerApp.sessionUsageGroups.get(observerId);
                    if (group != null) {
                        group.remove();
                    }
                    int observerIdCount = observerApp.sessionUsageGroups.size();
                    try {
                        if (observerIdCount >= getUsageSessionObserverPerUidLimit()) {
                            throw new java.lang.IllegalStateException("Too many app usage observers added by uid " + requestingUid);
                        }
                        try {
                            try {
                                com.android.server.usage.AppTimeLimitController.SessionUsageGroup group2 = new com.android.server.usage.AppTimeLimitController.SessionUsageGroup(user, observerApp, observerId, observed, timeLimit, limitReachedCallbackIntent, sessionThresholdTime, sessionEndCallbackIntent);
                                observerApp.sessionUsageGroups.append(observerId, group2);
                                user.addUsageGroup(group2);
                                noteActiveLocked(user, group2, getElapsedRealtime());
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
            }
        }
    }

    public void removeUsageSessionObserver(int requestingUid, int observerId, int userId) {
        synchronized (this.mLock) {
            com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp = getOrCreateObserverAppDataLocked(requestingUid);
            com.android.server.usage.AppTimeLimitController.SessionUsageGroup group = observerApp.sessionUsageGroups.get(observerId);
            if (group != null) {
                group.remove();
            }
        }
    }

    public void addAppUsageLimitObserver(int requestingUid, int observerId, java.lang.String[] observed, long timeLimit, long timeUsed, android.app.PendingIntent callbackIntent, int userId) throws java.lang.Throwable {
        if (timeLimit < getMinTimeLimit()) {
            throw new java.lang.IllegalArgumentException("Time limit must be >= " + getMinTimeLimit());
        }
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.usage.AppTimeLimitController.UserData user = getOrCreateUserDataLocked(userId);
                    com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp = getOrCreateObserverAppDataLocked(requestingUid);
                    com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup group = observerApp.appUsageLimitGroups.get(observerId);
                    if (group != null) {
                        group.remove();
                    }
                    int observerIdCount = observerApp.appUsageLimitGroups.size();
                    if (observerIdCount >= getAppUsageLimitObserverPerUidLimit()) {
                        throw new java.lang.IllegalStateException("Too many app usage observers added by uid " + requestingUid);
                    }
                    com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup group2 = new com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup(user, observerApp, observerId, observed, timeLimit, timeUsed, timeUsed >= timeLimit ? null : callbackIntent);
                    observerApp.appUsageLimitGroups.append(observerId, group2);
                    user.addUsageGroup(group2);
                    noteActiveLocked(user, group2, getElapsedRealtime());
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public void removeAppUsageLimitObserver(int requestingUid, int observerId, int userId) {
        synchronized (this.mLock) {
            com.android.server.usage.AppTimeLimitController.ObserverAppData observerApp = getOrCreateObserverAppDataLocked(requestingUid);
            com.android.server.usage.AppTimeLimitController.AppUsageLimitGroup group = observerApp.appUsageLimitGroups.get(observerId);
            if (group != null) {
                group.remove();
            }
        }
    }

    public void noteUsageStart(java.lang.String name, int userId, long timeAgoMs) throws java.lang.IllegalArgumentException {
        java.lang.Integer count;
        synchronized (this.mLock) {
            com.android.server.usage.AppTimeLimitController.UserData user = getOrCreateUserDataLocked(userId);
            int index = user.currentlyActive.indexOfKey(name);
            if (index >= 0 && (count = user.currentlyActive.valueAt(index)) != null) {
                user.currentlyActive.setValueAt(index, java.lang.Integer.valueOf(count.intValue() + 1));
                return;
            }
            long currentTime = getElapsedRealtime();
            user.currentlyActive.put(name, ONE);
            java.util.ArrayList<com.android.server.usage.AppTimeLimitController.UsageGroup> groups = user.observedMap.get(name);
            if (groups == null) {
                return;
            }
            int size = groups.size();
            for (int i = 0; i < size; i++) {
                com.android.server.usage.AppTimeLimitController.UsageGroup group = groups.get(i);
                group.noteUsageStart(currentTime - timeAgoMs, currentTime);
            }
        }
    }

    public void noteUsageStart(java.lang.String name, int userId) throws java.lang.IllegalArgumentException {
        noteUsageStart(name, userId, 0L);
    }

    public void noteUsageStop(java.lang.String name, int userId) throws java.lang.IllegalArgumentException {
        synchronized (this.mLock) {
            com.android.server.usage.AppTimeLimitController.UserData user = getOrCreateUserDataLocked(userId);
            int index = user.currentlyActive.indexOfKey(name);
            if (index < 0) {
                throw new java.lang.IllegalArgumentException("Unable to stop usage for " + name + ", not in use");
            }
            java.lang.Integer count = user.currentlyActive.valueAt(index);
            if (!count.equals(ONE)) {
                user.currentlyActive.setValueAt(index, java.lang.Integer.valueOf(count.intValue() - 1));
                return;
            }
            user.currentlyActive.removeAt(index);
            long currentTime = getElapsedRealtime();
            java.util.ArrayList<com.android.server.usage.AppTimeLimitController.UsageGroup> groups = user.observedMap.get(name);
            if (groups == null) {
                return;
            }
            int size = groups.size();
            for (int i = 0; i < size; i++) {
                com.android.server.usage.AppTimeLimitController.UsageGroup group = groups.get(i);
                group.noteUsageStop(currentTime);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postInformLimitReachedListenerLocked(com.android.server.usage.AppTimeLimitController.UsageGroup group) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2, group));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postCheckTimeoutLocked(com.android.server.usage.AppTimeLimitController.UsageGroup group, long timeout) {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, group), timeout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelCheckTimeoutLocked(com.android.server.usage.AppTimeLimitController.UsageGroup group) {
        this.mHandler.removeMessages(1, group);
    }

    void dump(java.lang.String[] args, java.io.PrintWriter pw) {
        if (args != null) {
            for (java.lang.String arg : args) {
                if ("actives".equals(arg)) {
                    synchronized (this.mLock) {
                        int nUsers = this.mUsers.size();
                        for (int user = 0; user < nUsers; user++) {
                            android.util.ArrayMap<java.lang.String, java.lang.Integer> actives = this.mUsers.valueAt(user).currentlyActive;
                            int nActive = actives.size();
                            for (int active = 0; active < nActive; active++) {
                                pw.println(actives.keyAt(active));
                            }
                        }
                    }
                    return;
                }
            }
        }
        synchronized (this.mLock) {
            pw.println("\n  App Time Limits");
            int nUsers2 = this.mUsers.size();
            for (int i = 0; i < nUsers2; i++) {
                pw.print("   User ");
                this.mUsers.valueAt(i).dump(pw);
            }
            pw.println();
            int nObserverApps = this.mObserverApps.size();
            for (int i2 = 0; i2 < nObserverApps; i2++) {
                pw.print("   Observer App ");
                this.mObserverApps.valueAt(i2).dump(pw);
            }
        }
    }
}
