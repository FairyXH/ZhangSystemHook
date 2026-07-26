package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public class UserWakeupStore {
    private static final java.lang.String ATTR_USER_ID = "user_id";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG_USER = "user";
    private static final java.lang.String TAG_USERS = "users";
    static final java.lang.String USERS_FILE_NAME = "usersWithAlarmClocks.xml";
    public static final int XML_VERSION_CURRENT = 1;
    private java.util.concurrent.Executor mBackgroundExecutor;
    static final java.lang.String USER_WAKEUP_TAG = com.android.server.alarm.UserWakeupStore.class.getSimpleName();
    static final long BUFFER_TIME_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(30);
    static final long USER_START_TIME_DEVIATION_LIMIT_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(5);
    static final long INITIAL_USER_START_SCHEDULING_DELAY_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(5);
    static final java.lang.String ROOT_DIR_NAME = "alarms";
    private static final java.io.File USER_WAKEUP_DIR = new java.io.File(android.os.Environment.getDataSystemDirectory(), ROOT_DIR_NAME);
    private static final java.util.Random sRandom = new java.util.Random(500);
    private final java.lang.Object mUserWakeupLock = new java.lang.Object();
    private final android.util.SparseLongArray mUserStarts = new android.util.SparseLongArray();
    private final android.util.SparseLongArray mStartingUsers = new android.util.SparseLongArray();

    public void init() {
        this.mBackgroundExecutor = com.android.internal.os.BackgroundThread.getExecutor();
        this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.alarm.UserWakeupStore$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.readUserIdList();
            }
        });
    }

    public void addUserWakeup(int userId, long alarmTime) {
        synchronized (this.mUserWakeupLock) {
            this.mStartingUsers.delete(userId);
            this.mUserStarts.put(userId, (alarmTime - BUFFER_TIME_MS) + getUserWakeupOffset());
        }
        updateUserListFile();
    }

    public void removeUserWakeup(int userId) {
        if (deleteWakeupFromUserStarts(userId)) {
            updateUserListFile();
        }
    }

    public int[] getUserIdsToWakeup(long nowElapsed) {
        int[] iArrCopyOfRange;
        synchronized (this.mUserWakeupLock) {
            int[] userIds = new int[this.mUserStarts.size()];
            int index = 0;
            for (int i = this.mUserStarts.size() - 1; i >= 0; i--) {
                if (this.mUserStarts.valueAt(i) <= nowElapsed) {
                    userIds[index] = this.mUserStarts.keyAt(i);
                    index++;
                }
            }
            iArrCopyOfRange = java.util.Arrays.copyOfRange(userIds, 0, index);
        }
        return iArrCopyOfRange;
    }

    private void updateUserListFile() {
        this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.alarm.UserWakeupStore$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateUserListFile$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUserListFile$0() {
        try {
            writeUserIdList();
        } catch (java.lang.Exception e) {
            android.util.Slog.e(USER_WAKEUP_TAG, "Failed to write " + e.getLocalizedMessage());
        }
    }

    long getWakeupTimeForUser(int userId) {
        long j;
        synchronized (this.mUserWakeupLock) {
            j = this.mUserStarts.get(userId, -1L);
        }
        return j;
    }

    public void onUserStarting(int userId) {
        synchronized (this.mUserWakeupLock) {
            long wakeup = getWakeupTimeForUser(userId);
            if (wakeup >= 0) {
                this.mStartingUsers.put(userId, wakeup);
                this.mUserStarts.delete(userId);
            }
        }
    }

    public void onUserStarted(int userId) {
        if (deleteWakeupFromStartingUsers(userId)) {
            updateUserListFile();
        }
    }

    public void onUserRemoved(int userId) {
        if (deleteWakeupFromUserStarts(userId) || deleteWakeupFromStartingUsers(userId)) {
            updateUserListFile();
        }
    }

    private boolean deleteWakeupFromUserStarts(int userId) {
        int index;
        synchronized (this.mUserWakeupLock) {
            index = this.mUserStarts.indexOfKey(userId);
            if (index >= 0) {
                this.mUserStarts.removeAt(index);
            }
        }
        return index >= 0;
    }

    private boolean deleteWakeupFromStartingUsers(int userId) {
        int index;
        synchronized (this.mUserWakeupLock) {
            index = this.mStartingUsers.indexOfKey(userId);
            if (index >= 0) {
                this.mStartingUsers.removeAt(index);
            }
        }
        return index >= 0;
    }

    public long getNextWakeupTime() {
        long nextWakeupTime = -1;
        synchronized (this.mUserWakeupLock) {
            for (int i = 0; i < this.mUserStarts.size(); i++) {
                if (this.mUserStarts.valueAt(i) < nextWakeupTime || nextWakeupTime == -1) {
                    nextWakeupTime = this.mUserStarts.valueAt(i);
                }
            }
        }
        return nextWakeupTime;
    }

    private static long getUserWakeupOffset() {
        return sRandom.nextLong(USER_START_TIME_DEVIATION_LIMIT_MS * 2) - USER_START_TIME_DEVIATION_LIMIT_MS;
    }

    private void writeUserIdList() {
        android.util.AtomicFile file = getUserWakeupFile();
        if (file == null) {
            return;
        }
        try {
            java.io.FileOutputStream fos = file.startWrite(android.os.SystemClock.uptimeMillis());
            try {
                com.android.internal.util.FastXmlSerializer fastXmlSerializer = new com.android.internal.util.FastXmlSerializer();
                fastXmlSerializer.setOutput(fos, java.nio.charset.StandardCharsets.UTF_8.name());
                fastXmlSerializer.startDocument(null, true);
                fastXmlSerializer.startTag(null, "users");
                com.android.internal.util.XmlUtils.writeIntAttribute(fastXmlSerializer, ATTR_VERSION, 1);
                java.util.List<android.util.Pair<java.lang.Integer, java.lang.Long>> listOfUsers = new java.util.ArrayList<>();
                synchronized (this.mUserWakeupLock) {
                    for (int i = 0; i < this.mUserStarts.size(); i++) {
                        listOfUsers.add(new android.util.Pair<>(java.lang.Integer.valueOf(this.mUserStarts.keyAt(i)), java.lang.Long.valueOf(this.mUserStarts.valueAt(i))));
                    }
                    for (int i2 = 0; i2 < this.mStartingUsers.size(); i2++) {
                        listOfUsers.add(new android.util.Pair<>(java.lang.Integer.valueOf(this.mStartingUsers.keyAt(i2)), java.lang.Long.valueOf(this.mStartingUsers.valueAt(i2))));
                    }
                }
                java.util.Collections.sort(listOfUsers, java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.alarm.UserWakeupStore$$ExternalSyntheticLambda0
                    @Override // java.util.function.ToLongFunction
                    public final long applyAsLong(java.lang.Object obj) {
                        return ((java.lang.Long) ((android.util.Pair) obj).second).longValue();
                    }
                }));
                for (int i3 = 0; i3 < listOfUsers.size(); i3++) {
                    fastXmlSerializer.startTag(null, TAG_USER);
                    com.android.internal.util.XmlUtils.writeIntAttribute(fastXmlSerializer, ATTR_USER_ID, ((java.lang.Integer) listOfUsers.get(i3).first).intValue());
                    fastXmlSerializer.endTag(null, TAG_USER);
                }
                fastXmlSerializer.endTag(null, "users");
                fastXmlSerializer.endDocument();
                file.finishWrite(fos);
                if (fos != null) {
                    fos.close();
                }
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.wtf(USER_WAKEUP_TAG, "Error writing user wakeup data", e);
            file.delete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:45:0x00d5
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public void readUserIdList() {
        /*
            Method dump skipped, instruction units count: 251
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.alarm.UserWakeupStore.readUserIdList():void");
    }

    private android.util.AtomicFile getUserWakeupFile() {
        if (!USER_WAKEUP_DIR.exists() && !USER_WAKEUP_DIR.mkdir()) {
            android.util.Slog.wtf(USER_WAKEUP_TAG, "Failed to mkdir() user list file: " + USER_WAKEUP_DIR);
            return null;
        }
        java.io.File userFile = new java.io.File(USER_WAKEUP_DIR, USERS_FILE_NAME);
        return new android.util.AtomicFile(userFile);
    }

    void dump(android.util.IndentingPrintWriter pw, long nowELAPSED) {
        synchronized (this.mUserWakeupLock) {
            pw.increaseIndent();
            pw.print("User wakeup store file path: ");
            android.util.AtomicFile file = getUserWakeupFile();
            if (file == null) {
                pw.println("null");
            } else {
                pw.println(file.getBaseFile().getAbsolutePath());
            }
            pw.println(this.mUserStarts.size() + " user wakeups scheduled: ");
            for (int i = 0; i < this.mUserStarts.size(); i++) {
                pw.print("UserId: ");
                pw.print(this.mUserStarts.keyAt(i));
                pw.print(", userStartTime: ");
                android.util.TimeUtils.formatDuration(this.mUserStarts.valueAt(i), nowELAPSED, pw);
                pw.println();
            }
            pw.println(this.mStartingUsers.size() + " starting users: ");
            for (int i2 = 0; i2 < this.mStartingUsers.size(); i2++) {
                pw.print("UserId: ");
                pw.print(this.mStartingUsers.keyAt(i2));
                pw.print(", userStartTime: ");
                android.util.TimeUtils.formatDuration(this.mStartingUsers.valueAt(i2), nowELAPSED, pw);
                pw.println();
            }
            pw.decreaseIndent();
        }
    }
}
