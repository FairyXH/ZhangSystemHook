package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class TaskPersister implements com.android.server.wm.PersisterQueue.Listener {
    static final boolean DEBUG = false;
    private static final java.lang.String IMAGES_DIRNAME = "recent_images";
    static final java.lang.String IMAGE_EXTENSION = ".png";
    private static final java.lang.String PERSISTED_TASK_IDS_FILENAME = "persisted_taskIds.txt";
    static final java.lang.String TAG = "TaskPersister";
    private static final java.lang.String TAG_TASK = "task";
    private static final java.lang.String TASKS_DIRNAME = "recent_tasks";
    private static final java.lang.String TASK_FILENAME_SUFFIX = "_task.xml";
    private final java.lang.Object mIoLock;
    private final com.android.server.wm.PersisterQueue mPersisterQueue;
    private final com.android.server.wm.RecentTasks mRecentTasks;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final java.io.File mTaskIdsDir;
    private final android.util.SparseArray<android.util.SparseBooleanArray> mTaskIdsInFile;
    private final com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;
    private final android.util.ArraySet<java.lang.Integer> mTmpTaskIds;

    TaskPersister(java.io.File systemDir, com.android.server.wm.ActivityTaskSupervisor taskSupervisor, com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.RecentTasks recentTasks, com.android.server.wm.PersisterQueue persisterQueue) {
        this.mTaskIdsInFile = new android.util.SparseArray<>();
        this.mIoLock = new java.lang.Object();
        this.mTmpTaskIds = new android.util.ArraySet<>();
        java.io.File legacyImagesDir = new java.io.File(systemDir, IMAGES_DIRNAME);
        if (legacyImagesDir.exists() && (!android.os.FileUtils.deleteContents(legacyImagesDir) || !legacyImagesDir.delete())) {
            android.util.Slog.i(TAG, "Failure deleting legacy images directory: " + legacyImagesDir);
        }
        java.io.File legacyTasksDir = new java.io.File(systemDir, TASKS_DIRNAME);
        if (legacyTasksDir.exists() && (!android.os.FileUtils.deleteContents(legacyTasksDir) || !legacyTasksDir.delete())) {
            android.util.Slog.i(TAG, "Failure deleting legacy tasks directory: " + legacyTasksDir);
        }
        this.mTaskIdsDir = new java.io.File(android.os.Environment.getDataDirectory(), "system_de");
        this.mTaskSupervisor = taskSupervisor;
        this.mService = service;
        this.mRecentTasks = recentTasks;
        this.mPersisterQueue = persisterQueue;
        this.mPersisterQueue.addListener(this);
    }

    TaskPersister(java.io.File workingDir) {
        this.mTaskIdsInFile = new android.util.SparseArray<>();
        this.mIoLock = new java.lang.Object();
        this.mTmpTaskIds = new android.util.ArraySet<>();
        this.mTaskIdsDir = workingDir;
        this.mTaskSupervisor = null;
        this.mService = null;
        this.mRecentTasks = null;
        this.mPersisterQueue = new com.android.server.wm.PersisterQueue();
        this.mPersisterQueue.addListener(this);
    }

    private void removeThumbnails(final com.android.server.wm.Task task) {
        this.mPersisterQueue.removeItems(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskPersister$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.TaskPersister.lambda$removeThumbnails$0(task, (com.android.server.wm.TaskPersister.ImageWriteQueueItem) obj);
            }
        }, com.android.server.wm.TaskPersister.ImageWriteQueueItem.class);
    }

    static /* synthetic */ boolean lambda$removeThumbnails$0(com.android.server.wm.Task task, com.android.server.wm.TaskPersister.ImageWriteQueueItem item) {
        java.io.File file = new java.io.File(item.mFilePath);
        return file.getName().startsWith(java.lang.Integer.toString(task.mTaskId));
    }

    android.util.SparseBooleanArray readPersistedTaskIdsFromFileForUser(int userId) {
        android.util.SparseBooleanArray persistedTaskIds = new android.util.SparseBooleanArray();
        synchronized (this.mIoLock) {
            java.io.BufferedReader reader = null;
            try {
                try {
                    reader = new java.io.BufferedReader(new java.io.FileReader(getUserPersistedTaskIdsFile(userId)));
                    while (true) {
                        java.lang.String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        for (java.lang.String taskIdString : line.split("\\s+")) {
                            int id = java.lang.Integer.parseInt(taskIdString);
                            persistedTaskIds.put(id, true);
                        }
                    }
                    libcore.io.IoUtils.closeQuietly(reader);
                } catch (java.io.FileNotFoundException e) {
                } catch (java.lang.Exception e2) {
                    android.util.Slog.e(TAG, "Error while reading taskIds file for user " + userId, e2);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(reader);
            }
        }
        android.util.Slog.i(TAG, "Loaded persisted task ids for user " + userId);
        return persistedTaskIds;
    }

    void writePersistedTaskIdsForUser(android.util.SparseBooleanArray taskIds, int userId) {
        if (userId < 0) {
            return;
        }
        java.io.File persistedTaskIdsFile = getUserPersistedTaskIdsFile(userId);
        synchronized (this.mIoLock) {
            java.io.BufferedWriter writer = null;
            try {
                try {
                    writer = new java.io.BufferedWriter(new java.io.FileWriter(persistedTaskIdsFile));
                    for (int i = 0; i < taskIds.size(); i++) {
                        if (taskIds.valueAt(i)) {
                            writer.write(java.lang.String.valueOf(taskIds.keyAt(i)));
                            writer.newLine();
                        }
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, "Error while writing taskIds file for user " + userId, e);
                    libcore.io.IoUtils.closeQuietly(writer);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(writer);
            }
        }
    }

    void setPersistedTaskIds(int userId, android.util.SparseBooleanArray taskIds) {
        this.mTaskIdsInFile.put(userId, taskIds);
    }

    void unloadUserDataFromMemory(int userId) {
        this.mTaskIdsInFile.delete(userId);
    }

    void wakeup(final com.android.server.wm.Task task, boolean flush) {
        synchronized (this.mPersisterQueue) {
            if (task != null) {
                com.android.server.wm.TaskPersister.TaskWriteQueueItem item = (com.android.server.wm.TaskPersister.TaskWriteQueueItem) this.mPersisterQueue.findLastItem(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskPersister$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.wm.TaskPersister.lambda$wakeup$1(task, (com.android.server.wm.TaskPersister.TaskWriteQueueItem) obj);
                    }
                }, com.android.server.wm.TaskPersister.TaskWriteQueueItem.class);
                if (item != null && !task.inRecents) {
                    removeThumbnails(task);
                }
                if (item == null && task.isPersistable) {
                    this.mPersisterQueue.addItem(new com.android.server.wm.TaskPersister.TaskWriteQueueItem(task, this.mService), flush);
                }
            } else {
                this.mPersisterQueue.addItem(com.android.server.wm.PersisterQueue.EMPTY_ITEM, flush);
            }
        }
        this.mPersisterQueue.yieldIfQueueTooDeep();
    }

    static /* synthetic */ boolean lambda$wakeup$1(com.android.server.wm.Task task, com.android.server.wm.TaskPersister.TaskWriteQueueItem queueItem) {
        return task == queueItem.mTask;
    }

    void flush() {
        this.mPersisterQueue.flush();
    }

    void saveImage(android.graphics.Bitmap image, java.lang.String filePath) {
        this.mPersisterQueue.updateLastOrAddItem(new com.android.server.wm.TaskPersister.ImageWriteQueueItem(filePath, image), false);
    }

    android.graphics.Bitmap getTaskDescriptionIcon(java.lang.String filePath) {
        android.graphics.Bitmap icon = getImageFromWriteQueue(filePath);
        if (icon != null) {
            return icon;
        }
        return restoreImage(filePath);
    }

    private android.graphics.Bitmap getImageFromWriteQueue(final java.lang.String filePath) {
        com.android.server.wm.TaskPersister.ImageWriteQueueItem item = (com.android.server.wm.TaskPersister.ImageWriteQueueItem) this.mPersisterQueue.findLastItem(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskPersister$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.TaskPersister.ImageWriteQueueItem) obj).mFilePath.equals(filePath);
            }
        }, com.android.server.wm.TaskPersister.ImageWriteQueueItem.class);
        if (item != null) {
            return item.mImage;
        }
        return null;
    }

    private static java.lang.String fileToString(java.io.File file) {
        java.lang.String newline = java.lang.System.lineSeparator();
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            java.lang.StringBuffer sb = new java.lang.StringBuffer(((int) file.length()) * 2);
            while (true) {
                java.lang.String line = reader.readLine();
                if (line != null) {
                    sb.append(line + newline);
                } else {
                    reader.close();
                    return sb.toString();
                }
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Couldn't read file " + file.getName());
            return null;
        }
    }

    private com.android.server.wm.Task taskIdToTask(int taskId, java.util.ArrayList<com.android.server.wm.Task> tasks) {
        if (taskId < 0) {
            return null;
        }
        for (int taskNdx = tasks.size() - 1; taskNdx >= 0; taskNdx--) {
            com.android.server.wm.Task task = tasks.get(taskNdx);
            if (task.mTaskId == taskId) {
                return task;
            }
        }
        android.util.Slog.e(TAG, "Restore affiliation error looking for taskId=" + taskId);
        return null;
    }

    static com.android.server.wm.TaskPersister.RecentTaskFiles loadTasksForUser(int userId) {
        java.util.ArrayList<com.android.server.wm.TaskPersister.RecentTaskFile> taskFiles = new java.util.ArrayList<>();
        java.io.File userTasksDir = getUserTasksDir(userId);
        java.io.File[] recentFiles = userTasksDir.listFiles();
        if (recentFiles == null) {
            android.util.Slog.i(TAG, "loadTasksForUser: Unable to list files from " + userTasksDir + " exists=" + userTasksDir.exists());
            return new com.android.server.wm.TaskPersister.RecentTaskFiles(new java.io.File[0], taskFiles);
        }
        for (java.io.File taskFile : recentFiles) {
            if (taskFile.getName().endsWith(TASK_FILENAME_SUFFIX)) {
                try {
                    int taskId = java.lang.Integer.parseInt(taskFile.getName().substring(0, taskFile.getName().length() - TASK_FILENAME_SUFFIX.length()));
                    try {
                        taskFiles.add(new com.android.server.wm.TaskPersister.RecentTaskFile(taskId, taskFile));
                    } catch (java.io.IOException e) {
                        android.util.Slog.w(TAG, "Failed to read file: " + fileToString(taskFile), e);
                        taskFile.delete();
                    }
                } catch (java.lang.NumberFormatException e2) {
                    android.util.Slog.w(TAG, "Unexpected task file name", e2);
                }
            }
        }
        return new com.android.server.wm.TaskPersister.RecentTaskFiles(recentFiles, taskFiles);
    }

    java.util.ArrayList<com.android.server.wm.Task> restoreTasksForUserLocked(int userId, com.android.server.wm.TaskPersister.RecentTaskFiles recentTaskFiles, android.util.IntArray existedTaskIds) {
        java.util.ArrayList<com.android.server.wm.TaskPersister.RecentTaskFile> taskFiles;
        int taskNdx;
        java.lang.Throwable th;
        com.android.server.wm.TaskPersister.RecentTaskFile recentTask;
        java.util.ArrayList<com.android.server.wm.Task> tasks = new java.util.ArrayList<>();
        java.util.ArrayList<com.android.server.wm.TaskPersister.RecentTaskFile> taskFiles2 = recentTaskFiles.mLoadedFiles;
        if (taskFiles2.isEmpty()) {
            return tasks;
        }
        android.util.ArraySet<java.lang.Integer> recoveredTaskIds = new android.util.ArraySet<>();
        int taskNdx2 = 0;
        while (true) {
            int event = 1;
            if (taskNdx2 >= taskFiles2.size()) {
                removeObsoleteFiles(recoveredTaskIds, recentTaskFiles.mUserTaskFiles);
                for (int taskNdx3 = tasks.size() - 1; taskNdx3 >= 0; taskNdx3--) {
                    com.android.server.wm.Task task = tasks.get(taskNdx3);
                    task.setPrevAffiliate(taskIdToTask(task.mPrevAffiliateTaskId, tasks));
                    task.setNextAffiliate(taskIdToTask(task.mNextAffiliateTaskId, tasks));
                }
                java.util.Collections.sort(tasks, new java.util.Comparator<com.android.server.wm.Task>() { // from class: com.android.server.wm.TaskPersister.1
                    @Override // java.util.Comparator
                    public int compare(com.android.server.wm.Task lhs, com.android.server.wm.Task rhs) {
                        long diff = rhs.mLastTimeMoved - lhs.mLastTimeMoved;
                        if (diff < 0) {
                            return -1;
                        }
                        if (diff > 0) {
                            return 1;
                        }
                        return 0;
                    }
                });
                return tasks;
            }
            com.android.server.wm.TaskPersister.RecentTaskFile recentTask2 = taskFiles2.get(taskNdx2);
            if (existedTaskIds.contains(recentTask2.mTaskId)) {
                android.util.Slog.w(TAG, "Task #" + recentTask2.mTaskId + " has already been created, so skip restoring");
                taskFiles = taskFiles2;
                taskNdx = taskNdx2;
            } else {
                java.io.File taskFile = recentTask2.mFile;
                try {
                    java.io.InputStream is = recentTask2.mXmlContent;
                    try {
                        com.android.modules.utils.TypedXmlPullParser in = android.util.Xml.resolvePullParser(is);
                        while (true) {
                            int event2 = in.next();
                            if (event2 == event) {
                                taskNdx = taskNdx2;
                                taskFiles = taskFiles2;
                                break;
                            }
                            if (event2 == 3) {
                                taskFiles = taskFiles2;
                                taskNdx = taskNdx2;
                                break;
                            }
                            java.lang.String name = in.getName();
                            taskFiles = taskFiles2;
                            if (event2 == 2) {
                                try {
                                    if (TAG_TASK.equals(name)) {
                                        com.android.server.wm.Task task2 = com.android.server.wm.Task.restoreFromXml(in, this.mTaskSupervisor);
                                        if (task2 != null) {
                                            recentTask = recentTask2;
                                            try {
                                                int taskId = task2.mTaskId;
                                                boolean persistedTask = task2.hasActivity();
                                                if (!persistedTask || this.mRecentTasks.getTask(taskId) == null) {
                                                    taskNdx = taskNdx2;
                                                    if (!persistedTask && this.mService.mRootWindowContainer.anyTaskForId(taskId, 1) != null) {
                                                        android.util.Slog.wtf(TAG, "Existing task with taskId " + taskId + " found");
                                                    } else if (userId != task2.mUserId) {
                                                        android.util.Slog.wtf(TAG, "Task with userId " + task2.mUserId + " found in " + taskFile.getAbsolutePath());
                                                    } else {
                                                        this.mTaskSupervisor.setNextTaskIdForUser(taskId, userId);
                                                        task2.isPersistable = true;
                                                        tasks.add(task2);
                                                        recoveredTaskIds.add(java.lang.Integer.valueOf(taskId));
                                                    }
                                                } else {
                                                    taskNdx = taskNdx2;
                                                    try {
                                                        android.util.Slog.wtf(TAG, "Existing persisted task with taskId " + taskId + " found");
                                                    } catch (java.lang.Throwable th2) {
                                                        th = th2;
                                                        if (is != null) {
                                                            try {
                                                                is.close();
                                                            } catch (java.lang.Throwable th3) {
                                                                try {
                                                                    th.addSuppressed(th3);
                                                                } catch (java.lang.Throwable th4) {
                                                                    th = th4;
                                                                    if (0 != 0) {
                                                                        taskFile.delete();
                                                                    }
                                                                    throw th;
                                                                }
                                                            }
                                                        }
                                                        throw th;
                                                    }
                                                }
                                            } catch (java.lang.Throwable th5) {
                                                taskNdx = taskNdx2;
                                                th = th5;
                                            }
                                        } else {
                                            taskNdx = taskNdx2;
                                            recentTask = recentTask2;
                                            android.util.Slog.e(TAG, "restoreTasksForUserLocked: Unable to restore taskFile=" + taskFile + ": " + fileToString(taskFile));
                                        }
                                    } else {
                                        taskNdx = taskNdx2;
                                        recentTask = recentTask2;
                                        android.util.Slog.wtf(TAG, "restoreTasksForUserLocked: Unknown xml event=" + event2 + " name=" + name);
                                    }
                                } catch (java.lang.Throwable th6) {
                                    taskNdx = taskNdx2;
                                    th = th6;
                                }
                            } else {
                                taskNdx = taskNdx2;
                                recentTask = recentTask2;
                            }
                            com.android.internal.util.XmlUtils.skipCurrentTag(in);
                            taskFiles2 = taskFiles;
                            recentTask2 = recentTask;
                            taskNdx2 = taskNdx;
                            event = 1;
                        }
                        if (is != null) {
                            try {
                                is.close();
                            } catch (java.lang.Exception e) {
                                e = e;
                                android.util.Slog.wtf(TAG, "Unable to parse " + taskFile + ". Error ", e);
                                android.util.Slog.e(TAG, "Failing file: " + fileToString(taskFile));
                                if (1 != 0) {
                                    taskFile.delete();
                                }
                            }
                        }
                        if (0 != 0) {
                            taskFile.delete();
                        }
                    } catch (java.lang.Throwable th7) {
                        taskFiles = taskFiles2;
                        taskNdx = taskNdx2;
                        th = th7;
                    }
                } catch (java.lang.Exception e2) {
                    e = e2;
                    taskFiles = taskFiles2;
                    taskNdx = taskNdx2;
                } catch (java.lang.Throwable th8) {
                    th = th8;
                }
            }
            taskNdx2 = taskNdx + 1;
            taskFiles2 = taskFiles;
        }
    }

    @Override // com.android.server.wm.PersisterQueue.Listener
    public void onPreProcessItem(boolean queueEmpty) {
        if (queueEmpty) {
            this.mTmpTaskIds.clear();
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mRecentTasks.getPersistableTaskIds(this.mTmpTaskIds);
                    this.mService.mWindowManager.removeObsoleteTaskFiles(this.mTmpTaskIds, this.mRecentTasks.usersWithRecentsLoadedLocked());
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            removeObsoleteFiles(this.mTmpTaskIds);
        }
        writeTaskIdsFiles();
    }

    private static void removeObsoleteFiles(android.util.ArraySet<java.lang.Integer> persistentTaskIds, java.io.File[] files) {
        if (files == null) {
            android.util.Slog.e(TAG, "File error accessing recents directory (directory doesn't exist?).");
            return;
        }
        for (java.io.File file : files) {
            java.lang.String filename = file.getName();
            int taskIdEnd = filename.indexOf(95);
            if (taskIdEnd > 0) {
                try {
                    int taskId = java.lang.Integer.parseInt(filename.substring(0, taskIdEnd));
                    if (!persistentTaskIds.contains(java.lang.Integer.valueOf(taskId))) {
                        file.delete();
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.wtf(TAG, "removeObsoleteFiles: Can't parse file=" + file.getName());
                    file.delete();
                }
            }
        }
    }

    private void writeTaskIdsFiles() {
        android.util.SparseArray<android.util.SparseBooleanArray> changedTaskIdsPerUser = new android.util.SparseArray<>();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                for (int userId : this.mRecentTasks.usersWithRecentsLoadedLocked()) {
                    android.util.SparseBooleanArray taskIdsToSave = this.mRecentTasks.getTaskIdsForLoadedUser(userId);
                    android.util.SparseBooleanArray persistedIdsInFile = this.mTaskIdsInFile.get(userId);
                    if (persistedIdsInFile == null || !persistedIdsInFile.equals(taskIdsToSave)) {
                        android.util.SparseBooleanArray taskIdsToSaveCopy = taskIdsToSave.clone();
                        this.mTaskIdsInFile.put(userId, taskIdsToSaveCopy);
                        changedTaskIdsPerUser.put(userId, taskIdsToSaveCopy);
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        for (int i = 0; i < changedTaskIdsPerUser.size(); i++) {
            writePersistedTaskIdsForUser(changedTaskIdsPerUser.valueAt(i), changedTaskIdsPerUser.keyAt(i));
        }
    }

    private void removeObsoleteFiles(android.util.ArraySet<java.lang.Integer> persistentTaskIds) {
        int[] candidateUserIds;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                candidateUserIds = this.mRecentTasks.usersWithRecentsLoadedLocked();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        for (int userId : candidateUserIds) {
            removeObsoleteFiles(persistentTaskIds, getUserImagesDir(userId).listFiles());
            removeObsoleteFiles(persistentTaskIds, getUserTasksDir(userId).listFiles());
        }
    }

    static android.graphics.Bitmap restoreImage(java.lang.String filename) {
        return android.graphics.BitmapFactory.decodeFile(filename);
    }

    private java.io.File getUserPersistedTaskIdsFile(int userId) {
        java.io.File userTaskIdsDir = new java.io.File(this.mTaskIdsDir, java.lang.String.valueOf(userId));
        if (!userTaskIdsDir.exists() && !userTaskIdsDir.mkdirs()) {
            android.util.Slog.e(TAG, "Error while creating user directory: " + userTaskIdsDir);
        }
        return new java.io.File(userTaskIdsDir, PERSISTED_TASK_IDS_FILENAME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.io.File getUserTasksDir(int userId) {
        return new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), TASKS_DIRNAME);
    }

    static java.io.File getUserImagesDir(int userId) {
        return new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), IMAGES_DIRNAME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean createParentDirectory(java.lang.String filePath) {
        java.io.File parentDir = new java.io.File(filePath).getParentFile();
        return parentDir.isDirectory() || parentDir.mkdir();
    }

    private static class RecentTaskFile {
        final java.io.File mFile;
        final int mTaskId;
        final java.io.ByteArrayInputStream mXmlContent;

        RecentTaskFile(int taskId, java.io.File file) throws java.io.IOException {
            this.mTaskId = taskId;
            this.mFile = file;
            this.mXmlContent = new java.io.ByteArrayInputStream(java.nio.file.Files.readAllBytes(file.toPath()));
        }
    }

    static class RecentTaskFiles {
        final java.util.ArrayList<com.android.server.wm.TaskPersister.RecentTaskFile> mLoadedFiles;
        final java.io.File[] mUserTaskFiles;

        RecentTaskFiles(java.io.File[] userFiles, java.util.ArrayList<com.android.server.wm.TaskPersister.RecentTaskFile> loadedFiles) {
            this.mUserTaskFiles = userFiles;
            this.mLoadedFiles = loadedFiles;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class TaskWriteQueueItem implements com.android.server.wm.PersisterQueue.WriteQueueItem {
        private final com.android.server.wm.ActivityTaskManagerService mService;
        private final com.android.server.wm.Task mTask;

        TaskWriteQueueItem(com.android.server.wm.Task task, com.android.server.wm.ActivityTaskManagerService service) {
            this.mTask = task;
            this.mService = service;
        }

        private byte[] saveToXml(com.android.server.wm.Task task) throws java.lang.Exception {
            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            com.android.modules.utils.TypedXmlSerializer xmlSerializer = android.util.Xml.resolveSerializer(os);
            xmlSerializer.startDocument((java.lang.String) null, true);
            xmlSerializer.startTag((java.lang.String) null, com.android.server.wm.TaskPersister.TAG_TASK);
            task.saveToXml(xmlSerializer);
            xmlSerializer.endTag((java.lang.String) null, com.android.server.wm.TaskPersister.TAG_TASK);
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            return os.toByteArray();
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() throws java.lang.Exception {
            byte[] data = null;
            com.android.server.wm.Task task = this.mTask;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (task.inRecents) {
                        try {
                            data = saveToXml(task);
                        } catch (java.lang.Exception e) {
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            if (data != null) {
                android.util.AtomicFile atomicFile = null;
                try {
                    java.io.File userTasksDir = com.android.server.wm.TaskPersister.getUserTasksDir(task.mUserId);
                    if (!userTasksDir.isDirectory() && !userTasksDir.mkdirs()) {
                        android.util.Slog.e(com.android.server.wm.TaskPersister.TAG, "Failure creating tasks directory for user " + task.mUserId + ": " + userTasksDir + " Dropping persistence for task " + task);
                        return;
                    }
                    android.util.AtomicFile atomicFile2 = new android.util.AtomicFile(new java.io.File(userTasksDir, java.lang.String.valueOf(task.mTaskId) + com.android.server.wm.TaskPersister.TASK_FILENAME_SUFFIX));
                    java.io.FileOutputStream file = atomicFile2.startWrite();
                    file.write(data);
                    atomicFile2.finishWrite(file);
                } catch (java.io.IOException e2) {
                    if (0 != 0) {
                        atomicFile.failWrite(null);
                    }
                    android.util.Slog.e(com.android.server.wm.TaskPersister.TAG, "Unable to open " + ((java.lang.Object) null) + " for persisting. " + e2);
                }
            }
        }

        public java.lang.String toString() {
            return "TaskWriteQueueItem{task=" + this.mTask + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ImageWriteQueueItem implements com.android.server.wm.PersisterQueue.WriteQueueItem<com.android.server.wm.TaskPersister.ImageWriteQueueItem> {
        final java.lang.String mFilePath;
        android.graphics.Bitmap mImage;

        ImageWriteQueueItem(java.lang.String filePath, android.graphics.Bitmap image) {
            this.mFilePath = filePath;
            this.mImage = image;
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            java.lang.String filePath = this.mFilePath;
            if (!com.android.server.wm.TaskPersister.createParentDirectory(filePath)) {
                android.util.Slog.e(com.android.server.wm.TaskPersister.TAG, "Error while creating images directory for file: " + filePath);
                return;
            }
            android.graphics.Bitmap bitmap = this.mImage;
            java.io.FileOutputStream imageFile = null;
            try {
                try {
                    imageFile = new java.io.FileOutputStream(new java.io.File(filePath));
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, imageFile);
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(com.android.server.wm.TaskPersister.TAG, "saveImage: unable to save " + filePath, e);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(imageFile);
            }
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public boolean matches(com.android.server.wm.TaskPersister.ImageWriteQueueItem item) {
            return this.mFilePath.equals(item.mFilePath);
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void updateFrom(com.android.server.wm.TaskPersister.ImageWriteQueueItem item) {
            this.mImage = item.mImage;
        }

        public java.lang.String toString() {
            return "ImageWriteQueueItem{path=" + this.mFilePath + ", image=(" + this.mImage.getWidth() + "x" + this.mImage.getHeight() + ")}";
        }
    }
}
