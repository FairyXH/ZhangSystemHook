package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class UsageStatsDatabase {
    public static final int BACKUP_VERSION = 4;
    private static final java.lang.String BAK_SUFFIX = ".bak";
    private static final java.lang.String CHECKED_IN_SUFFIX = "-c";
    private static final int DEFAULT_CURRENT_VERSION = 5;
    static final boolean KEEP_BACKUP_DIR = false;
    static final java.lang.String KEY_USAGE_STATS = "usage_stats";
    private static final java.lang.String TAG = "UsageStatsDatabase";
    private final java.io.File mBackupsDir;
    private final com.android.server.usage.UnixCalendar mCal;
    private int mCurrentVersion;
    private boolean mFirstUpdate;
    private final java.io.File[] mIntervalDirs;
    private final java.lang.Object mLock;
    private boolean mNewUpdate;
    private final java.io.File mPackageMappingsFile;
    final com.android.server.usage.PackagesTokenData mPackagesTokenData;
    final android.util.LongSparseArray<android.util.AtomicFile>[] mSortedStatFiles;
    private final java.io.File mUpdateBreadcrumb;
    private boolean mUpgradePerformed;
    private final java.io.File mVersionFile;
    static final int[] MAX_FILES_PER_INTERVAL_TYPE = {100, 50, 12, 10};
    private static final boolean DEBUG = com.android.server.usage.UsageStatsService.DEBUG;
    private static final java.lang.String RETENTION_LEN_KEY = "ro.usagestats.chooser.retention";
    private static final int SELECTION_LOG_RETENTION_LEN = android.os.SystemProperties.getInt(RETENTION_LEN_KEY, 14);

    public interface CheckinAction {
        boolean checkin(com.android.server.usage.IntervalStats intervalStats);
    }

    public interface StatCombiner<T> {
        boolean combine(com.android.server.usage.IntervalStats intervalStats, boolean z, java.util.List<T> list);
    }

    public UsageStatsDatabase(java.io.File dir, int version) {
        this.mLock = new java.lang.Object();
        this.mPackagesTokenData = new com.android.server.usage.PackagesTokenData();
        this.mIntervalDirs = new java.io.File[]{new java.io.File(dir, com.android.server.net.IOplusNetworkPolicyManagerServiceEx.TYPE_DAILY), new java.io.File(dir, "weekly"), new java.io.File(dir, "monthly"), new java.io.File(dir, "yearly")};
        this.mCurrentVersion = version;
        this.mVersionFile = new java.io.File(dir, "version");
        this.mBackupsDir = new java.io.File(dir, "backups");
        this.mUpdateBreadcrumb = new java.io.File(dir, "breadcrumb");
        this.mSortedStatFiles = new android.util.LongSparseArray[this.mIntervalDirs.length];
        this.mPackageMappingsFile = new java.io.File(dir, "mappings");
        this.mCal = new com.android.server.usage.UnixCalendar(0L);
    }

    public UsageStatsDatabase(java.io.File dir) {
        this(dir, 5);
    }

    public void init(long currentTimeMillis) {
        synchronized (this.mLock) {
            for (java.io.File f : this.mIntervalDirs) {
                f.mkdirs();
                if (!f.exists()) {
                    throw new java.lang.IllegalStateException("Failed to create directory " + f.getAbsolutePath());
                }
            }
            checkVersionAndBuildLocked();
            if (this.mUpgradePerformed) {
                prune(currentTimeMillis);
            } else {
                indexFilesLocked();
            }
            for (android.util.LongSparseArray<android.util.AtomicFile> files : this.mSortedStatFiles) {
                int startIndex = files.firstIndexOnOrAfter(currentTimeMillis);
                if (startIndex >= 0) {
                    int fileCount = files.size();
                    for (int i = startIndex; i < fileCount; i++) {
                        files.valueAt(i).delete();
                    }
                    for (int i2 = startIndex; i2 < fileCount; i2++) {
                        files.removeAt(i2);
                    }
                }
            }
        }
    }

    public boolean checkinDailyFiles(com.android.server.usage.UsageStatsDatabase.CheckinAction checkinAction) {
        synchronized (this.mLock) {
            android.util.LongSparseArray<android.util.AtomicFile> files = this.mSortedStatFiles[0];
            int fileCount = files.size();
            int lastCheckin = -1;
            for (int i = 0; i < fileCount - 1; i++) {
                if (files.valueAt(i).getBaseFile().getPath().endsWith(CHECKED_IN_SUFFIX)) {
                    lastCheckin = i;
                }
            }
            int i2 = lastCheckin + 1;
            if (i2 == fileCount - 1) {
                return true;
            }
            for (int i3 = i2; i3 < fileCount - 1; i3++) {
                try {
                    com.android.server.usage.IntervalStats stats = new com.android.server.usage.IntervalStats();
                    readLocked(files.valueAt(i3), stats, false);
                    if (!checkinAction.checkin(stats)) {
                        return false;
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, "Failed to check-in", e);
                    return false;
                }
            }
            for (int i4 = i2; i4 < fileCount - 1; i4++) {
                android.util.AtomicFile file = files.valueAt(i4);
                java.io.File checkedInFile = new java.io.File(file.getBaseFile().getPath() + CHECKED_IN_SUFFIX);
                if (!file.getBaseFile().renameTo(checkedInFile)) {
                    android.util.Slog.e(TAG, "Failed to mark file " + file.getBaseFile().getPath() + " as checked-in");
                    return true;
                }
                files.setValueAt(i4, new android.util.AtomicFile(checkedInFile));
            }
            return true;
        }
    }

    void forceIndexFiles() {
        synchronized (this.mLock) {
            indexFilesLocked();
        }
    }

    private void indexFilesLocked() {
        java.io.FilenameFilter backupFileFilter = new java.io.FilenameFilter() { // from class: com.android.server.usage.UsageStatsDatabase.1
            @Override // java.io.FilenameFilter
            public boolean accept(java.io.File dir, java.lang.String name) {
                return !name.endsWith(com.android.server.usage.UsageStatsDatabase.BAK_SUFFIX);
            }
        };
        for (int i = 0; i < this.mSortedStatFiles.length; i++) {
            if (this.mSortedStatFiles[i] == null) {
                this.mSortedStatFiles[i] = new android.util.LongSparseArray<>();
            } else {
                this.mSortedStatFiles[i].clear();
            }
            java.io.File[] files = this.mIntervalDirs[i].listFiles(backupFileFilter);
            if (files != null) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Found " + files.length + " stat files for interval " + i);
                }
                for (java.io.File f : files) {
                    android.util.AtomicFile af = new android.util.AtomicFile(f);
                    try {
                        this.mSortedStatFiles[i].put(parseBeginTime(af), af);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "failed to index file: " + f, e);
                    }
                }
                int toDelete = this.mSortedStatFiles[i].size() - MAX_FILES_PER_INTERVAL_TYPE[i];
                if (toDelete > 0) {
                    for (int j = 0; j < toDelete; j++) {
                        this.mSortedStatFiles[i].valueAt(0).delete();
                        this.mSortedStatFiles[i].removeAt(0);
                    }
                    android.util.Slog.d(TAG, "Deleted " + toDelete + " stat files for interval " + i);
                }
            }
        }
    }

    boolean isFirstUpdate() {
        return this.mFirstUpdate;
    }

    boolean isNewUpdate() {
        return this.mNewUpdate;
    }

    boolean wasUpgradePerformed() {
        return this.mUpgradePerformed;
    }

    private void checkVersionAndBuildLocked() {
        int version;
        java.io.BufferedReader reader;
        java.lang.String currentFingerprint = getBuildFingerprint();
        this.mFirstUpdate = true;
        this.mNewUpdate = true;
        try {
            reader = new java.io.BufferedReader(new java.io.FileReader(this.mVersionFile));
        } catch (java.io.IOException | java.lang.NumberFormatException e) {
            version = 0;
        }
        try {
            version = java.lang.Integer.parseInt(reader.readLine());
            java.lang.String buildFingerprint = reader.readLine();
            if (buildFingerprint != null) {
                this.mFirstUpdate = false;
            }
            if (currentFingerprint.equals(buildFingerprint)) {
                this.mNewUpdate = false;
            }
            reader.close();
            if (version != this.mCurrentVersion) {
                android.util.Slog.i(TAG, "Upgrading from version " + version + " to " + this.mCurrentVersion);
                if (!this.mUpdateBreadcrumb.exists()) {
                    try {
                        doUpgradeLocked(version);
                    } catch (java.lang.Exception e2) {
                        android.util.Slog.e(TAG, "Failed to upgrade from version " + version + " to " + this.mCurrentVersion, e2);
                        this.mCurrentVersion = version;
                        return;
                    }
                } else {
                    android.util.Slog.i(TAG, "Version upgrade breadcrumb found on disk! Continuing version upgrade");
                }
            }
            if (this.mUpdateBreadcrumb.exists()) {
                try {
                    java.io.BufferedReader reader2 = new java.io.BufferedReader(new java.io.FileReader(this.mUpdateBreadcrumb));
                    try {
                        long token = java.lang.Long.parseLong(reader2.readLine());
                        int previousVersion = java.lang.Integer.parseInt(reader2.readLine());
                        reader2.close();
                        if (this.mCurrentVersion >= 4) {
                            continueUpgradeLocked(previousVersion, token);
                        } else {
                            android.util.Slog.wtf(TAG, "Attempting to upgrade to an unsupported version: " + this.mCurrentVersion);
                        }
                    } catch (java.lang.Throwable th) {
                        try {
                            reader2.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (java.io.IOException | java.lang.NumberFormatException e3) {
                    android.util.Slog.e(TAG, "Failed read version upgrade breadcrumb");
                    throw new java.lang.RuntimeException(e3);
                }
            }
            if (version != this.mCurrentVersion || this.mNewUpdate) {
                try {
                    java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(this.mVersionFile));
                    try {
                        writer.write(java.lang.Integer.toString(this.mCurrentVersion));
                        writer.write("\n");
                        writer.write(currentFingerprint);
                        writer.write("\n");
                        writer.flush();
                        writer.close();
                    } finally {
                    }
                } catch (java.io.IOException e4) {
                    android.util.Slog.e(TAG, "Failed to write new version");
                    throw new java.lang.RuntimeException(e4);
                }
            }
            if (this.mUpdateBreadcrumb.exists()) {
                this.mUpdateBreadcrumb.delete();
                this.mUpgradePerformed = true;
            }
            if (this.mBackupsDir.exists()) {
                this.mUpgradePerformed = true;
                deleteDirectory(this.mBackupsDir);
            }
        } catch (java.lang.Throwable th3) {
            try {
                reader.close();
            } catch (java.lang.Throwable th4) {
                th3.addSuppressed(th4);
            }
            throw th3;
        }
    }

    private java.lang.String getBuildFingerprint() {
        return android.os.Build.VERSION.RELEASE + ";" + android.os.Build.VERSION.CODENAME + ";" + android.os.Build.VERSION.INCREMENTAL;
    }

    private void doUpgradeLocked(int thisVersion) {
        boolean z = false;
        if (thisVersion < 2) {
            android.util.Slog.i(TAG, "Deleting all usage stats files");
            for (int i = 0; i < this.mIntervalDirs.length; i++) {
                java.io.File[] files = this.mIntervalDirs[i].listFiles();
                if (files != null) {
                    for (java.io.File f : files) {
                        f.delete();
                    }
                }
            }
            return;
        }
        long token = java.lang.System.currentTimeMillis();
        java.io.File backupDir = new java.io.File(this.mBackupsDir, java.lang.Long.toString(token));
        backupDir.mkdirs();
        if (!backupDir.exists()) {
            throw new java.lang.IllegalStateException("Failed to create backup directory " + backupDir.getAbsolutePath());
        }
        try {
            java.nio.file.Files.copy(this.mVersionFile.toPath(), new java.io.File(backupDir, this.mVersionFile.getName()).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            int i2 = 0;
            while (i2 < this.mIntervalDirs.length) {
                java.io.File backupIntervalDir = new java.io.File(backupDir, this.mIntervalDirs[i2].getName());
                backupIntervalDir.mkdir();
                if (!backupIntervalDir.exists()) {
                    throw new java.lang.IllegalStateException("Failed to create interval backup directory " + backupIntervalDir.getAbsolutePath());
                }
                java.io.File[] files2 = this.mIntervalDirs[i2].listFiles();
                if (files2 != null) {
                    int j = 0;
                    while (j < files2.length) {
                        java.io.File backupFile = new java.io.File(backupIntervalDir, files2[j].getName());
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "Creating versioned (" + java.lang.Integer.toString(thisVersion) + ") backup of " + files2[j].toString() + " stat files for interval " + i2 + " to " + backupFile.toString());
                        }
                        try {
                            java.nio.file.Files.move(files2[j].toPath(), backupFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                            j++;
                            z = false;
                        } catch (java.io.IOException e) {
                            android.util.Slog.e(TAG, "Failed to back up file : " + files2[j].toString());
                            throw new java.lang.RuntimeException(e);
                        }
                    }
                }
                i2++;
                z = z;
            }
            java.io.BufferedWriter writer = null;
            try {
                try {
                    writer = new java.io.BufferedWriter(new java.io.FileWriter(this.mUpdateBreadcrumb));
                    writer.write(java.lang.Long.toString(token));
                    writer.write("\n");
                    writer.write(java.lang.Integer.toString(thisVersion));
                    writer.write("\n");
                    writer.flush();
                } catch (java.io.IOException e2) {
                    android.util.Slog.e(TAG, "Failed to write new version upgrade breadcrumb");
                    throw new java.lang.RuntimeException(e2);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(writer);
            }
        } catch (java.io.IOException e3) {
            android.util.Slog.e(TAG, "Failed to back up version file : " + this.mVersionFile.toString());
            throw new java.lang.RuntimeException(e3);
        }
    }

    private void continueUpgradeLocked(int version, long token) {
        if (version <= 3) {
            android.util.Slog.w(TAG, "Reading UsageStats as XML; current database version: " + this.mCurrentVersion);
        }
        java.io.File backupDir = new java.io.File(this.mBackupsDir, java.lang.Long.toString(token));
        if (version >= 5) {
            readMappingsLocked();
        }
        for (int i = 0; i < this.mIntervalDirs.length; i++) {
            java.io.File backedUpInterval = new java.io.File(backupDir, this.mIntervalDirs[i].getName());
            java.io.File[] files = backedUpInterval.listFiles();
            if (files != null) {
                for (int j = 0; j < files.length && j <= MAX_FILES_PER_INTERVAL_TYPE[i]; j++) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Upgrading " + files[j].toString() + " to version (" + java.lang.Integer.toString(this.mCurrentVersion) + ") for interval " + i);
                    }
                    try {
                        com.android.server.usage.IntervalStats stats = new com.android.server.usage.IntervalStats();
                        readLocked(new android.util.AtomicFile(files[j]), stats, version, this.mPackagesTokenData, false);
                        if (this.mCurrentVersion >= 5) {
                            stats.obfuscateData(this.mPackagesTokenData);
                        }
                        writeLocked(new android.util.AtomicFile(new java.io.File(this.mIntervalDirs[i], java.lang.Long.toString(stats.beginTime))), stats, this.mCurrentVersion, this.mPackagesTokenData);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(TAG, "Failed to upgrade backup file : " + files[j].toString());
                    }
                }
            }
        }
        if (this.mCurrentVersion >= 5) {
            try {
                writeMappingsLocked();
            } catch (java.io.IOException e2) {
                android.util.Slog.e(TAG, "Failed to write the tokens mappings file.");
            }
        }
    }

    int onPackageRemoved(java.lang.String packageName, long timeRemoved) {
        int tokenRemoved;
        synchronized (this.mLock) {
            tokenRemoved = this.mPackagesTokenData.removePackage(packageName, timeRemoved);
            try {
                writeMappingsLocked();
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Unable to update package mappings on disk after removing token " + tokenRemoved);
            }
        }
        return tokenRemoved;
    }

    boolean pruneUninstalledPackagesData() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mIntervalDirs.length; i++) {
                java.io.File[] files = this.mIntervalDirs[i].listFiles();
                if (files != null) {
                    for (int j = 0; j < files.length; j++) {
                        try {
                            com.android.server.usage.IntervalStats stats = new com.android.server.usage.IntervalStats();
                            android.util.AtomicFile atomicFile = new android.util.AtomicFile(files[j]);
                            if (readLocked(atomicFile, stats, this.mCurrentVersion, this.mPackagesTokenData, false)) {
                                writeLocked(atomicFile, stats, this.mCurrentVersion, this.mPackagesTokenData);
                            }
                        } catch (java.lang.Exception e) {
                            android.util.Slog.e(TAG, "Failed to prune data from: " + files[j].toString());
                            return false;
                        }
                    }
                }
            }
            try {
                writeMappingsLocked();
            } catch (java.io.IOException e2) {
                android.util.Slog.e(TAG, "Failed to write package mappings after pruning data.");
                return false;
            }
        }
        return true;
    }

    void prunePackagesDataOnUpgrade(java.util.HashMap<java.lang.String, java.lang.Long> installedPackages) {
        if (com.android.internal.util.ArrayUtils.isEmpty(installedPackages)) {
            return;
        }
        synchronized (this.mLock) {
            for (int i = 0; i < this.mIntervalDirs.length; i++) {
                java.io.File[] files = this.mIntervalDirs[i].listFiles();
                if (files != null) {
                    for (int j = 0; j < files.length; j++) {
                        try {
                            com.android.server.usage.IntervalStats stats = new com.android.server.usage.IntervalStats();
                            android.util.AtomicFile atomicFile = new android.util.AtomicFile(files[j]);
                            readLocked(atomicFile, stats, this.mCurrentVersion, this.mPackagesTokenData, false);
                            if (pruneStats(installedPackages, stats)) {
                                writeLocked(atomicFile, stats, this.mCurrentVersion, this.mPackagesTokenData);
                            }
                        } catch (java.lang.Exception e) {
                            android.util.Slog.e(TAG, "Failed to prune data from: " + files[j].toString());
                        }
                    }
                }
            }
        }
    }

    private boolean pruneStats(java.util.HashMap<java.lang.String, java.lang.Long> installedPackages, com.android.server.usage.IntervalStats stats) {
        boolean dataPruned = false;
        for (int i = stats.packageStats.size() - 1; i >= 0; i--) {
            android.app.usage.UsageStats usageStats = stats.packageStats.valueAt(i);
            java.lang.Long timeInstalled = installedPackages.get(usageStats.mPackageName);
            if (timeInstalled == null || timeInstalled.longValue() > usageStats.mEndTimeStamp) {
                stats.packageStats.removeAt(i);
                dataPruned = true;
            }
        }
        if (dataPruned) {
            stats.packageStatsObfuscated.clear();
        }
        for (int i2 = stats.events.size() - 1; i2 >= 0; i2--) {
            android.app.usage.UsageEvents.Event event = stats.events.get(i2);
            java.lang.Long timeInstalled2 = installedPackages.get(event.mPackage);
            if (timeInstalled2 == null || timeInstalled2.longValue() > event.mTimeStamp) {
                stats.events.remove(i2);
                dataPruned = true;
            }
        }
        return dataPruned;
    }

    public void onTimeChanged(long timeDiffMillis, long currentTime) {
        long j = timeDiffMillis;
        synchronized (this.mLock) {
            java.lang.StringBuilder logBuilder = new java.lang.StringBuilder();
            logBuilder.append("Time changed by ");
            android.util.TimeUtils.formatDuration(j, logBuilder);
            logBuilder.append(".");
            int filesDeleted = 0;
            int filesDeleted2 = 0;
            android.util.LongSparseArray<android.util.AtomicFile>[] longSparseArrayArr = this.mSortedStatFiles;
            int length = longSparseArrayArr.length;
            int i = 0;
            while (i < length) {
                android.util.LongSparseArray<android.util.AtomicFile> files = longSparseArrayArr[i];
                int fileCount = files.size();
                int i2 = 0;
                int filesMoved = filesDeleted2;
                int filesMoved2 = filesDeleted;
                while (i2 < fileCount) {
                    android.util.AtomicFile file = files.valueAt(i2);
                    if (files.keyAt(i2) / 10000 == currentTime / 10000) {
                        if (DEBUG) {
                            android.util.Slog.w(TAG, "maybe get wrong mSortedStatFiles so don't modid diff");
                        }
                    } else {
                        long newTime = files.keyAt(i2) + j;
                        if (newTime < 0) {
                            filesMoved2++;
                            file.delete();
                        } else {
                            try {
                                file.openRead().close();
                            } catch (java.io.IOException e) {
                            }
                            java.lang.String newName = java.lang.Long.toString(newTime);
                            if (file.getBaseFile().getName().endsWith(CHECKED_IN_SUFFIX)) {
                                newName = newName + CHECKED_IN_SUFFIX;
                            }
                            java.io.File newFile = new java.io.File(file.getBaseFile().getParentFile(), newName);
                            filesMoved++;
                            file.getBaseFile().renameTo(newFile);
                        }
                    }
                    i2++;
                    j = timeDiffMillis;
                }
                files.clear();
                i++;
                j = timeDiffMillis;
                filesDeleted = filesMoved2;
                filesDeleted2 = filesMoved;
            }
            logBuilder.append(" files deleted: ").append(filesDeleted);
            logBuilder.append(" files moved: ").append(filesDeleted2);
            android.util.Slog.i(TAG, logBuilder.toString());
            indexFilesLocked();
        }
    }

    public com.android.server.usage.IntervalStats getLatestUsageStats(int intervalType) {
        synchronized (this.mLock) {
            if (intervalType >= 0) {
                if (intervalType < this.mIntervalDirs.length) {
                    int fileCount = this.mSortedStatFiles[intervalType].size();
                    if (fileCount == 0) {
                        return null;
                    }
                    try {
                        android.util.AtomicFile f = this.mSortedStatFiles[intervalType].valueAt(fileCount - 1);
                        com.android.server.usage.IntervalStats stats = new com.android.server.usage.IntervalStats();
                        readLocked(f, stats, false);
                        long actualSystemTime = java.lang.System.currentTimeMillis();
                        if (stats.beginTime <= com.android.server.usage.UserUsageStatsService.INTERVAL_LENGTH[intervalType] + actualSystemTime && stats.endTime >= actualSystemTime - com.android.server.usage.UserUsageStatsService.INTERVAL_LENGTH[intervalType]) {
                            return stats;
                        }
                        android.util.Slog.e(TAG, "getLatestUsageStats time is invalid, type=" + intervalType + " b=" + stats.beginTime + " e=" + stats.endTime);
                        return null;
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(TAG, "Failed to read usage stats file", e);
                        return null;
                    }
                }
            }
            throw new java.lang.IllegalArgumentException("Bad interval type " + intervalType);
        }
    }

    void filterStats(com.android.server.usage.IntervalStats stats) {
        if (this.mPackagesTokenData.removedPackagesMap.isEmpty() || stats == null) {
            return;
        }
        android.util.ArrayMap<java.lang.String, java.lang.Long> removedPackagesMap = this.mPackagesTokenData.removedPackagesMap;
        int removedPackagesSize = removedPackagesMap.size();
        for (int i = 0; i < removedPackagesSize; i++) {
            java.lang.String removedPackage = removedPackagesMap.keyAt(i);
            android.app.usage.UsageStats usageStats = stats.packageStats.get(removedPackage);
            if (usageStats != null && usageStats.mEndTimeStamp < removedPackagesMap.valueAt(i).longValue()) {
                stats.packageStats.remove(removedPackage);
            }
        }
        for (int i2 = stats.events.size() - 1; i2 >= 0; i2--) {
            android.app.usage.UsageEvents.Event event = stats.events.get(i2);
            java.lang.Long timeRemoved = removedPackagesMap.get(event.mPackage);
            if (timeRemoved != null && timeRemoved.longValue() > event.mTimeStamp) {
                stats.events.remove(i2);
            }
        }
    }

    public <T> java.util.List<T> queryUsageStats(int intervalType, long beginTime, long endTime, com.android.server.usage.UsageStatsDatabase.StatCombiner<T> combiner, boolean skipEvents) {
        int endIndex;
        int startIndex;
        com.android.server.usage.UsageStatsDatabase usageStatsDatabase = this;
        if (intervalType < 0 || intervalType >= usageStatsDatabase.mIntervalDirs.length) {
            throw new java.lang.IllegalArgumentException("Bad interval type " + intervalType);
        }
        if (endTime <= beginTime) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "endTime(" + endTime + ") <= beginTime(" + beginTime + ")");
            }
            return null;
        }
        synchronized (usageStatsDatabase.mLock) {
            try {
                try {
                    android.util.LongSparseArray<android.util.AtomicFile> intervalStats = usageStatsDatabase.mSortedStatFiles[intervalType];
                    int endIndex2 = intervalStats.lastIndexOnOrBefore(endTime);
                    if (endIndex2 < 0) {
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "No results for this range. All stats start after.");
                        }
                        return null;
                    }
                    if (intervalStats.keyAt(endIndex2) != endTime) {
                        endIndex = endIndex2;
                    } else {
                        int endIndex3 = endIndex2 - 1;
                        if (endIndex3 >= 0) {
                            endIndex = endIndex3;
                        } else {
                            if (DEBUG) {
                                android.util.Slog.d(TAG, "No results for this range. All stats start after.");
                            }
                            return null;
                        }
                    }
                    int startIndex2 = intervalStats.lastIndexOnOrBefore(beginTime);
                    if (startIndex2 >= 0) {
                        startIndex = startIndex2;
                    } else {
                        startIndex = 0;
                    }
                    java.util.ArrayList<T> results = new java.util.ArrayList<>();
                    int i = startIndex;
                    while (i <= endIndex) {
                        android.util.AtomicFile f = intervalStats.valueAt(i);
                        com.android.server.usage.IntervalStats stats = new com.android.server.usage.IntervalStats();
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "Reading stat file " + f.getBaseFile().getAbsolutePath());
                        }
                        try {
                            usageStatsDatabase.readLocked(f, stats, skipEvents);
                            if (beginTime < stats.endTime) {
                                try {
                                    if (!combiner.combine(stats, false, results)) {
                                        break;
                                    }
                                } catch (java.lang.Exception e) {
                                    e = e;
                                    android.util.Slog.e(TAG, "Failed to read usage stats file", e);
                                }
                            }
                        } catch (java.lang.Exception e2) {
                            e = e2;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                        i++;
                        usageStatsDatabase = this;
                    }
                    return results;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    public int findBestFitBucket(long beginTimeStamp, long endTimeStamp) {
        int bestBucket;
        synchronized (this.mLock) {
            bestBucket = -1;
            long smallestDiff = Long.MAX_VALUE;
            for (int i = this.mSortedStatFiles.length - 1; i >= 0; i--) {
                int index = this.mSortedStatFiles[i].lastIndexOnOrBefore(beginTimeStamp);
                int size = this.mSortedStatFiles[i].size();
                if (index >= 0 && index < size) {
                    long diff = java.lang.Math.abs(this.mSortedStatFiles[i].keyAt(index) - beginTimeStamp);
                    if (diff < smallestDiff) {
                        smallestDiff = diff;
                        bestBucket = i;
                    }
                }
            }
        }
        return bestBucket;
    }

    public void prune(long currentTimeMillis) {
        synchronized (this.mLock) {
            this.mCal.setTimeInMillis(currentTimeMillis);
            this.mCal.addYears(-2);
            pruneFilesOlderThan(this.mIntervalDirs[3], this.mCal.getTimeInMillis());
            this.mCal.setTimeInMillis(currentTimeMillis);
            this.mCal.addMonths(-6);
            pruneFilesOlderThan(this.mIntervalDirs[2], this.mCal.getTimeInMillis());
            this.mCal.setTimeInMillis(currentTimeMillis);
            this.mCal.addWeeks(-4);
            pruneFilesOlderThan(this.mIntervalDirs[1], this.mCal.getTimeInMillis());
            this.mCal.setTimeInMillis(currentTimeMillis);
            this.mCal.addDays(-10);
            pruneFilesOlderThan(this.mIntervalDirs[0], this.mCal.getTimeInMillis());
            this.mCal.setTimeInMillis(currentTimeMillis);
            this.mCal.addDays(-SELECTION_LOG_RETENTION_LEN);
            for (int i = 0; i < this.mIntervalDirs.length; i++) {
                pruneChooserCountsOlderThan(this.mIntervalDirs[i], this.mCal.getTimeInMillis());
            }
            indexFilesLocked();
        }
    }

    private static void pruneFilesOlderThan(java.io.File dir, long expiryTime) {
        long beginTime;
        java.io.File[] files = dir.listFiles();
        if (files != null) {
            for (java.io.File f : files) {
                try {
                    beginTime = parseBeginTime(f);
                } catch (java.io.IOException e) {
                    beginTime = 0;
                }
                if (beginTime < expiryTime) {
                    new android.util.AtomicFile(f).delete();
                }
            }
        }
    }

    private void pruneChooserCountsOlderThan(java.io.File dir, long expiryTime) {
        long beginTime;
        java.io.File[] files = dir.listFiles();
        if (files != null) {
            for (java.io.File f : files) {
                try {
                    beginTime = parseBeginTime(f);
                } catch (java.io.IOException e) {
                    beginTime = 0;
                }
                if (beginTime < expiryTime) {
                    try {
                        android.util.AtomicFile af = new android.util.AtomicFile(f);
                        com.android.server.usage.IntervalStats stats = new com.android.server.usage.IntervalStats();
                        readLocked(af, stats, false);
                        int pkgCount = stats.packageStats.size();
                        for (int i = 0; i < pkgCount; i++) {
                            android.app.usage.UsageStats pkgStats = stats.packageStats.valueAt(i);
                            if (pkgStats.mChooserCounts != null) {
                                pkgStats.mChooserCounts.clear();
                            }
                        }
                        writeLocked(af, stats);
                    } catch (java.lang.Exception e2) {
                        android.util.Slog.e(TAG, "Failed to delete chooser counts from usage stats file", e2);
                    }
                }
            }
        }
    }

    private static long parseBeginTime(android.util.AtomicFile file) throws java.io.IOException {
        return parseBeginTime(file.getBaseFile());
    }

    private static long parseBeginTime(java.io.File file) throws java.io.IOException {
        java.lang.String name = file.getName();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c < '0' || c > '9') {
                name = name.substring(0, i);
                break;
            }
        }
        try {
            return java.lang.Long.parseLong(name);
        } catch (java.lang.NumberFormatException e) {
            throw new java.io.IOException(e);
        }
    }

    private void writeLocked(android.util.AtomicFile file, com.android.server.usage.IntervalStats stats) throws java.io.IOException, java.lang.RuntimeException {
        if (this.mCurrentVersion <= 3) {
            android.util.Slog.wtf(TAG, "Attempting to write UsageStats as XML with version " + this.mCurrentVersion);
        } else {
            writeLocked(file, stats, this.mCurrentVersion, this.mPackagesTokenData);
        }
    }

    private static void writeLocked(android.util.AtomicFile file, com.android.server.usage.IntervalStats stats, int version, com.android.server.usage.PackagesTokenData packagesTokenData) throws java.io.IOException, java.lang.RuntimeException {
        java.io.FileOutputStream fos = file.startWrite();
        try {
            writeLocked(fos, stats, version, packagesTokenData);
            file.finishWrite(fos);
            fos = null;
        } catch (java.lang.Exception e) {
        } catch (java.lang.Throwable th) {
            file.failWrite(fos);
            throw th;
        }
        file.failWrite(fos);
    }

    private static void writeLocked(java.io.OutputStream out, com.android.server.usage.IntervalStats stats, int version, com.android.server.usage.PackagesTokenData packagesTokenData) throws java.lang.Exception {
        switch (version) {
            case 1:
            case 2:
            case 3:
                android.util.Slog.wtf(TAG, "Attempting to write UsageStats as XML with version " + version);
                return;
            case 4:
                try {
                    com.android.server.usage.UsageStatsProto.write(out, stats);
                    return;
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, "Unable to write interval stats to proto.", e);
                    throw e;
                }
            case 5:
                stats.obfuscateData(packagesTokenData);
                try {
                    com.android.server.usage.UsageStatsProtoV2.write(out, stats);
                    return;
                } catch (java.lang.Exception e2) {
                    android.util.Slog.e(TAG, "Unable to write interval stats to proto.", e2);
                    throw e2;
                }
            default:
                throw new java.lang.RuntimeException("Unhandled UsageStatsDatabase version: " + java.lang.Integer.toString(version) + " on write.");
        }
    }

    private void readLocked(android.util.AtomicFile file, com.android.server.usage.IntervalStats statsOut, boolean skipEvents) throws java.io.IOException, java.lang.RuntimeException {
        if (this.mCurrentVersion <= 3) {
            android.util.Slog.wtf(TAG, "Reading UsageStats as XML; current database version: " + this.mCurrentVersion);
        }
        readLocked(file, statsOut, this.mCurrentVersion, this.mPackagesTokenData, skipEvents);
    }

    private static boolean readLocked(android.util.AtomicFile file, com.android.server.usage.IntervalStats statsOut, int version, com.android.server.usage.PackagesTokenData packagesTokenData, boolean skipEvents) throws java.io.IOException, java.lang.RuntimeException {
        try {
            java.io.FileInputStream in = file.openRead();
            try {
                statsOut.beginTime = parseBeginTime(file);
                boolean dataOmitted = readLocked(in, statsOut, version, packagesTokenData, skipEvents);
                statsOut.lastTimeSaved = file.getLastModifiedTime();
                return dataOmitted;
            } finally {
                try {
                    in.close();
                } catch (java.io.IOException e) {
                }
            }
        } catch (java.io.FileNotFoundException e2) {
            android.util.Slog.e(TAG, TAG, e2);
            throw e2;
        }
    }

    private static boolean readLocked(java.io.InputStream in, com.android.server.usage.IntervalStats statsOut, int version, com.android.server.usage.PackagesTokenData packagesTokenData, boolean skipEvents) throws java.lang.RuntimeException {
        switch (version) {
            case 1:
            case 2:
            case 3:
                android.util.Slog.w(TAG, "Reading UsageStats as XML; database version: " + version);
                try {
                    com.android.server.usage.UsageStatsXml.read(in, statsOut);
                    return false;
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, "Unable to read interval stats from XML", e);
                    return false;
                }
            case 4:
                try {
                    com.android.server.usage.UsageStatsProto.read(in, statsOut);
                    return false;
                } catch (java.lang.Exception e2) {
                    android.util.Slog.e(TAG, "Unable to read interval stats from proto.", e2);
                    return false;
                }
            case 5:
                try {
                    com.android.server.usage.UsageStatsProtoV2.read(in, statsOut, skipEvents);
                    break;
                } catch (java.lang.Exception e3) {
                    android.util.Slog.e(TAG, "Unable to read interval stats from proto.", e3);
                }
                boolean dataOmitted = statsOut.deobfuscateData(packagesTokenData);
                return dataOmitted;
            default:
                throw new java.lang.RuntimeException("Unhandled UsageStatsDatabase version: " + java.lang.Integer.toString(version) + " on read.");
        }
    }

    public void readMappingsLocked() {
        if (!this.mPackageMappingsFile.exists()) {
            return;
        }
        try {
            java.io.FileInputStream in = new android.util.AtomicFile(this.mPackageMappingsFile).openRead();
            try {
                com.android.server.usage.UsageStatsProtoV2.readObfuscatedData(in, this.mPackagesTokenData);
                if (in != null) {
                    in.close();
                }
                android.util.SparseArray<java.util.ArrayList<java.lang.String>> tokensToPackagesMap = this.mPackagesTokenData.tokensToPackagesMap;
                int tokensToPackagesMapSize = tokensToPackagesMap.size();
                for (int i = 0; i < tokensToPackagesMapSize; i++) {
                    int packageToken = tokensToPackagesMap.keyAt(i);
                    java.util.ArrayList<java.lang.String> tokensMap = tokensToPackagesMap.valueAt(i);
                    android.util.ArrayMap<java.lang.String, java.lang.Integer> packageStringsMap = new android.util.ArrayMap<>();
                    int tokensMapSize = tokensMap.size();
                    packageStringsMap.put(tokensMap.get(0), java.lang.Integer.valueOf(packageToken));
                    for (int j = 1; j < tokensMapSize; j++) {
                        packageStringsMap.put(tokensMap.get(j), java.lang.Integer.valueOf(j));
                    }
                    this.mPackagesTokenData.packagesToTokensMap.put(tokensMap.get(0), packageStringsMap);
                }
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to read the obfuscated packages mapping file.", e);
        }
    }

    void writeMappingsLocked() throws java.io.IOException {
        android.util.AtomicFile file = new android.util.AtomicFile(this.mPackageMappingsFile);
        java.io.FileOutputStream fos = file.startWrite();
        try {
            try {
                com.android.server.usage.UsageStatsProtoV2.writeObfuscatedData(fos, this.mPackagesTokenData);
                file.finishWrite(fos);
                fos = null;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Unable to write obfuscated data to proto.", e);
            }
        } finally {
            file.failWrite(fos);
        }
    }

    void obfuscateCurrentStats(com.android.server.usage.IntervalStats[] currentStats) {
        if (this.mCurrentVersion < 5) {
            return;
        }
        for (com.android.server.usage.IntervalStats stats : currentStats) {
            stats.obfuscateData(this.mPackagesTokenData);
        }
    }

    public void putUsageStats(int intervalType, com.android.server.usage.IntervalStats stats) throws java.io.IOException {
        if (stats == null) {
            return;
        }
        synchronized (this.mLock) {
            if (intervalType >= 0) {
                if (intervalType < this.mIntervalDirs.length) {
                    android.util.AtomicFile f = this.mSortedStatFiles[intervalType].get(stats.beginTime);
                    if (f == null) {
                        f = new android.util.AtomicFile(new java.io.File(this.mIntervalDirs[intervalType], java.lang.Long.toString(stats.beginTime)));
                        this.mSortedStatFiles[intervalType].put(stats.beginTime, f);
                    }
                    writeLocked(f, stats);
                    stats.lastTimeSaved = f.getLastModifiedTime();
                }
            }
            throw new java.lang.IllegalArgumentException("Bad interval type " + intervalType);
        }
    }

    byte[] getBackupPayload(java.lang.String key) {
        return getBackupPayload(key, 4);
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0113 A[Catch: all -> 0x0119, TryCatch #1 {, blocks: (B:13:0x002a, B:15:0x0038, B:16:0x0044, B:17:0x0054, B:19:0x005e, B:20:0x006e, B:21:0x007a, B:23:0x0084, B:24:0x0094, B:25:0x00a1, B:27:0x00ab, B:28:0x00bb, B:29:0x00c7, B:31:0x00d1, B:32:0x00e1, B:34:0x00e5, B:37:0x0109, B:38:0x0113, B:39:0x0117), top: B:48:0x002a, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public byte[] getBackupPayload(java.lang.String r9, int r10) {
        /*
            Method dump skipped, instruction units count: 309
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.UsageStatsDatabase.getBackupPayload(java.lang.String, int):byte[]");
    }

    private void calculatePackagesUsedWithinTimeframe(com.android.server.usage.IntervalStats stats, java.util.Set<java.lang.String> packagesList, long timeframeMs) {
        for (android.app.usage.UsageStats stat : stats.packageStats.values()) {
            if (stat.getLastTimePackageUsed() > timeframeMs) {
                packagesList.add(stat.mPackageName);
            }
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public java.util.Set<java.lang.String> applyRestoredPayload(java.lang.String key, byte[] payload) throws java.lang.Throwable {
        com.android.server.usage.IntervalStats dailyConfigSource;
        com.android.server.usage.IntervalStats weeklyConfigSource;
        com.android.server.usage.IntervalStats monthlyConfigSource;
        com.android.server.usage.IntervalStats yearlyConfigSource;
        java.util.Set<java.lang.String> packagesRestored;
        java.io.DataInputStream in;
        int backupDataVersion;
        synchronized (this.mLock) {
            try {
                try {
                    if (!KEY_USAGE_STATS.equals(key)) {
                        return java.util.Collections.EMPTY_SET;
                    }
                    try {
                        dailyConfigSource = getLatestUsageStats(0);
                        weeklyConfigSource = getLatestUsageStats(1);
                        monthlyConfigSource = getLatestUsageStats(2);
                        yearlyConfigSource = getLatestUsageStats(3);
                        packagesRestored = new android.util.ArraySet<>();
                        try {
                        } catch (java.io.IOException e) {
                            ioe = e;
                        } catch (java.lang.Throwable th) {
                            th = th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                    try {
                        in = new java.io.DataInputStream(new java.io.ByteArrayInputStream(payload));
                        backupDataVersion = in.readInt();
                    } catch (java.io.IOException e2) {
                        ioe = e2;
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        indexFilesLocked();
                        throw th;
                    }
                    if (backupDataVersion < 1 || backupDataVersion > 4) {
                        indexFilesLocked();
                        return packagesRestored;
                    }
                    for (int i = 0; i < this.mIntervalDirs.length; i++) {
                        try {
                            deleteDirectoryContents(this.mIntervalDirs[i]);
                        } catch (java.io.IOException e3) {
                            ioe = e3;
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                            indexFilesLocked();
                            throw th;
                        }
                    }
                    com.android.server.usage.IntervalStats monthlyConfigSource2 = monthlyConfigSource;
                    try {
                        long timeframe = java.lang.System.currentTimeMillis() - java.util.concurrent.TimeUnit.DAYS.toMillis(90L);
                        int fileCount = in.readInt();
                        for (int i2 = 0; i2 < fileCount; i2++) {
                            try {
                                com.android.server.usage.IntervalStats stats = deserializeIntervalStats(getIntervalStatsBytes(in), backupDataVersion);
                                calculatePackagesUsedWithinTimeframe(stats, packagesRestored, timeframe);
                                packagesRestored.addAll(stats.packageStats.keySet());
                                putUsageStats(0, mergeStats(stats, dailyConfigSource));
                            } catch (java.io.IOException e4) {
                                ioe = e4;
                            } catch (java.lang.Throwable th5) {
                                th = th5;
                                indexFilesLocked();
                                throw th;
                            }
                        }
                        int fileCount2 = in.readInt();
                        for (int i3 = 0; i3 < fileCount2; i3++) {
                            com.android.server.usage.IntervalStats stats2 = deserializeIntervalStats(getIntervalStatsBytes(in), backupDataVersion);
                            calculatePackagesUsedWithinTimeframe(stats2, packagesRestored, timeframe);
                            putUsageStats(1, mergeStats(stats2, weeklyConfigSource));
                        }
                        int i4 = in.readInt();
                        int i5 = 0;
                        for (int fileCount3 = i4; i5 < fileCount3; fileCount3 = fileCount3) {
                            com.android.server.usage.IntervalStats stats3 = deserializeIntervalStats(getIntervalStatsBytes(in), backupDataVersion);
                            calculatePackagesUsedWithinTimeframe(stats3, packagesRestored, timeframe);
                            com.android.server.usage.IntervalStats monthlyConfigSource3 = monthlyConfigSource2;
                            try {
                                putUsageStats(2, mergeStats(stats3, monthlyConfigSource3));
                                i5++;
                                monthlyConfigSource2 = monthlyConfigSource3;
                            } catch (java.io.IOException e5) {
                                ioe = e5;
                            }
                        }
                        int i6 = 0;
                        for (int fileCount4 = in.readInt(); i6 < fileCount4; fileCount4 = fileCount4) {
                            com.android.server.usage.IntervalStats stats4 = deserializeIntervalStats(getIntervalStatsBytes(in), backupDataVersion);
                            calculatePackagesUsedWithinTimeframe(stats4, packagesRestored, timeframe);
                            putUsageStats(3, mergeStats(stats4, yearlyConfigSource));
                            i6++;
                        }
                        if (DEBUG) {
                            android.util.Slog.i(TAG, "Completed Restoring UsageStats");
                        }
                    } catch (java.io.IOException e6) {
                        ioe = e6;
                    } catch (java.lang.Throwable th6) {
                        th = th6;
                    }
                    indexFilesLocked();
                    return packagesRestored;
                    android.util.Slog.d(TAG, "Failed to read data from input stream", ioe);
                    indexFilesLocked();
                    return packagesRestored;
                } catch (java.lang.Throwable th7) {
                    th = th7;
                    throw th;
                }
            } catch (java.lang.Throwable th8) {
                th = th8;
            }
        }
    }

    private com.android.server.usage.IntervalStats mergeStats(com.android.server.usage.IntervalStats beingRestored, com.android.server.usage.IntervalStats onDevice) {
        if (onDevice == null) {
            return beingRestored;
        }
        if (beingRestored == null) {
            return null;
        }
        beingRestored.activeConfiguration = onDevice.activeConfiguration;
        beingRestored.configurations.putAll((android.util.ArrayMap<? extends android.content.res.Configuration, ? extends android.app.usage.ConfigurationStats>) onDevice.configurations);
        beingRestored.events.clear();
        beingRestored.events.merge(onDevice.events);
        return beingRestored;
    }

    private void writeIntervalStatsToStream(java.io.DataOutputStream out, android.util.AtomicFile statsFile, int version) throws java.io.IOException {
        com.android.server.usage.IntervalStats stats = new com.android.server.usage.IntervalStats();
        try {
            readLocked(statsFile, stats, false);
            sanitizeIntervalStatsForBackup(stats);
            byte[] data = serializeIntervalStats(stats, version);
            out.writeInt(data.length);
            out.write(data);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to read usage stats file", e);
            out.writeInt(0);
        }
    }

    private static byte[] getIntervalStatsBytes(java.io.DataInputStream in) throws java.io.IOException {
        int length = in.readInt();
        byte[] buffer = new byte[length];
        in.read(buffer, 0, length);
        return buffer;
    }

    private static void sanitizeIntervalStatsForBackup(com.android.server.usage.IntervalStats stats) {
        if (stats == null) {
            return;
        }
        stats.activeConfiguration = null;
        stats.configurations.clear();
        stats.events.clear();
    }

    private byte[] serializeIntervalStats(com.android.server.usage.IntervalStats stats, int version) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream out = new java.io.DataOutputStream(baos);
        try {
            out.writeLong(stats.beginTime);
            writeLocked(out, stats, version, this.mPackagesTokenData);
        } catch (java.lang.Exception ioe) {
            android.util.Slog.d(TAG, "Serializing IntervalStats Failed", ioe);
            baos.reset();
        }
        return baos.toByteArray();
    }

    private com.android.server.usage.IntervalStats deserializeIntervalStats(byte[] data, int version) {
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(data);
        java.io.DataInputStream in = new java.io.DataInputStream(bais);
        com.android.server.usage.IntervalStats stats = new com.android.server.usage.IntervalStats();
        try {
            stats.beginTime = in.readLong();
            readLocked((java.io.InputStream) in, stats, version, this.mPackagesTokenData, false);
            return stats;
        } catch (java.lang.Exception e) {
            android.util.Slog.d(TAG, "DeSerializing IntervalStats Failed", e);
            return null;
        }
    }

    private static void deleteDirectoryContents(java.io.File directory) {
        java.io.File[] files = directory.listFiles();
        for (java.io.File file : files) {
            deleteDirectory(file);
        }
    }

    private static void deleteDirectory(java.io.File directory) {
        java.io.File[] files = directory.listFiles();
        if (files != null) {
            for (java.io.File file : files) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw, boolean compact) {
        synchronized (this.mLock) {
            pw.println();
            pw.println("UsageStatsDatabase:");
            pw.increaseIndent();
            dumpMappings(pw);
            pw.decreaseIndent();
            pw.println("Database Summary:");
            pw.increaseIndent();
            for (int i = 0; i < this.mSortedStatFiles.length; i++) {
                android.util.LongSparseArray<android.util.AtomicFile> files = this.mSortedStatFiles[i];
                int size = files.size();
                pw.print(com.android.server.usage.UserUsageStatsService.intervalToString(i));
                pw.print(" stats files: ");
                pw.print(size);
                pw.println(", sorted list of files:");
                pw.increaseIndent();
                for (int f = 0; f < size; f++) {
                    long fileName = files.keyAt(f);
                    if (compact) {
                        pw.print(com.android.server.usage.UserUsageStatsService.formatDateTime(fileName, false));
                    } else {
                        pw.printPair(java.lang.Long.toString(fileName), com.android.server.usage.UserUsageStatsService.formatDateTime(fileName, true));
                    }
                    pw.println();
                }
                pw.decreaseIndent();
            }
            pw.decreaseIndent();
        }
    }

    void dumpMappings(com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("Obfuscated Packages Mappings:");
            pw.increaseIndent();
            pw.println("Counter: " + this.mPackagesTokenData.counter);
            pw.println("Tokens Map Size: " + this.mPackagesTokenData.tokensToPackagesMap.size());
            if (!this.mPackagesTokenData.removedPackageTokens.isEmpty()) {
                pw.println("Removed Package Tokens: " + java.util.Arrays.toString(this.mPackagesTokenData.removedPackageTokens.toArray()));
            }
            for (int i = 0; i < this.mPackagesTokenData.tokensToPackagesMap.size(); i++) {
                int packageToken = this.mPackagesTokenData.tokensToPackagesMap.keyAt(i);
                java.lang.String packageStrings = java.lang.String.join(", ", this.mPackagesTokenData.tokensToPackagesMap.valueAt(i));
                pw.println("Token " + packageToken + ": [" + packageStrings + "]");
            }
            pw.println();
            pw.decreaseIndent();
        }
    }

    void deleteDataFor(java.lang.String pkg) {
        prunePackagesDataOnUpgrade(new java.util.HashMap<>(java.util.Collections.singletonMap(pkg, java.lang.Long.valueOf(android.os.SystemClock.elapsedRealtime()))));
    }

    com.android.server.usage.IntervalStats readIntervalStatsForFile(int interval, long fileName) {
        com.android.server.usage.IntervalStats stats;
        synchronized (this.mLock) {
            stats = new com.android.server.usage.IntervalStats();
            try {
                readLocked(this.mSortedStatFiles[interval].get(fileName, null), stats, false);
            } catch (java.lang.Exception e) {
                return null;
            }
        }
        return stats;
    }
}
