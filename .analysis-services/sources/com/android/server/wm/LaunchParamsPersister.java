package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class LaunchParamsPersister {
    private static final char ESCAPED_COMPONENT_SEPARATOR = '-';
    private static final java.lang.String LAUNCH_PARAMS_DIRNAME = "launch_params";
    private static final java.lang.String LAUNCH_PARAMS_FILE_SUFFIX = ".xml";
    private static final char OLD_ESCAPED_COMPONENT_SEPARATOR = '_';
    private static final char ORIGINAL_COMPONENT_SEPARATOR = '/';
    private static final java.lang.String TAG = "LaunchParamsPersister";
    private static final java.lang.String TAG_LAUNCH_PARAMS = "launch_params";
    private final android.util.SparseArray<android.util.ArrayMap<android.content.ComponentName, com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams>> mLaunchParamsMap;
    private com.android.server.pm.PackageList mPackageList;
    private final com.android.server.wm.PersisterQueue mPersisterQueue;
    private final com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    private final java.util.function.IntFunction<java.io.File> mUserFolderGetter;
    private final android.util.ArrayMap<java.lang.String, android.util.ArraySet<android.content.ComponentName>> mWindowLayoutAffinityMap;

    LaunchParamsPersister(com.android.server.wm.PersisterQueue persisterQueue, com.android.server.wm.ActivityTaskSupervisor supervisor) {
        this(persisterQueue, supervisor, new java.util.function.IntFunction() { // from class: com.android.server.wm.LaunchParamsPersister$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return android.os.Environment.getDataSystemCeDirectory(i);
            }
        });
    }

    LaunchParamsPersister(com.android.server.wm.PersisterQueue persisterQueue, com.android.server.wm.ActivityTaskSupervisor supervisor, java.util.function.IntFunction<java.io.File> userFolderGetter) {
        this.mLaunchParamsMap = new android.util.SparseArray<>();
        this.mWindowLayoutAffinityMap = new android.util.ArrayMap<>();
        this.mPersisterQueue = persisterQueue;
        this.mSupervisor = supervisor;
        this.mUserFolderGetter = userFolderGetter;
    }

    void onSystemReady() {
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mPackageList = pmi.getPackageList(new com.android.server.wm.LaunchParamsPersister.PackageListObserver());
    }

    void onUnlockUser(int userId) {
        loadLaunchParams(userId);
    }

    void onCleanupUser(int userId) {
        this.mLaunchParamsMap.remove(userId);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x012f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void loadLaunchParams(int r22) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 565
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.LaunchParamsPersister.loadLaunchParams(int):void");
    }

    void saveTask(com.android.server.wm.Task task) {
        saveTask(task, task.getDisplayContent());
    }

    void saveTask(com.android.server.wm.Task task, com.android.server.wm.DisplayContent display) {
        android.util.ArrayMap<android.content.ComponentName, com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams> map;
        android.content.ComponentName name = task.realActivity;
        if (name == null) {
            return;
        }
        int userId = task.mUserId;
        android.util.ArrayMap<android.content.ComponentName, com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams> map2 = this.mLaunchParamsMap.get(userId);
        if (map2 != null) {
            map = map2;
        } else {
            android.util.ArrayMap<android.content.ComponentName, com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams> map3 = new android.util.ArrayMap<>();
            this.mLaunchParamsMap.put(userId, map3);
            map = map3;
        }
        com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams params = map.computeIfAbsent(name, new java.util.function.Function() { // from class: com.android.server.wm.LaunchParamsPersister$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$saveTask$0((android.content.ComponentName) obj);
            }
        });
        boolean changed = saveTaskToLaunchParam(task, display, params);
        addComponentNameToLaunchParamAffinityMapIfNotNull(name, params.mWindowLayoutAffinity);
        if (changed) {
            this.mPersisterQueue.updateLastOrAddItem(new com.android.server.wm.LaunchParamsPersister.LaunchParamsWriteQueueItem(userId, name, params), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams lambda$saveTask$0(android.content.ComponentName componentName) {
        return new com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams();
    }

    private boolean saveTaskToLaunchParam(com.android.server.wm.Task task, com.android.server.wm.DisplayContent display, com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams params) {
        boolean changed;
        android.view.DisplayInfo info = new android.view.DisplayInfo();
        display.mDisplay.getDisplayInfo(info);
        boolean changed2 = !java.util.Objects.equals(params.mDisplayUniqueId, info.uniqueId);
        params.mDisplayUniqueId = info.uniqueId;
        boolean changed3 = changed2 | (params.mWindowingMode != task.getWindowingMode());
        params.mWindowingMode = task.getWindowingMode();
        if (task.mLastNonFullscreenBounds != null) {
            changed = changed3 | (true ^ java.util.Objects.equals(params.mBounds, task.mLastNonFullscreenBounds));
            params.mBounds.set(task.mLastNonFullscreenBounds);
        } else {
            changed = changed3 | (true ^ params.mBounds.isEmpty());
            params.mBounds.setEmpty();
        }
        java.lang.String launchParamAffinity = task.mWindowLayoutAffinity;
        boolean changed4 = changed | java.util.Objects.equals(launchParamAffinity, params.mWindowLayoutAffinity);
        params.mWindowLayoutAffinity = launchParamAffinity;
        if (changed4) {
            params.mTimestamp = java.lang.System.currentTimeMillis();
        }
        return changed4;
    }

    private void addComponentNameToLaunchParamAffinityMapIfNotNull(android.content.ComponentName name, java.lang.String launchParamAffinity) {
        if (launchParamAffinity == null) {
            return;
        }
        this.mWindowLayoutAffinityMap.computeIfAbsent(launchParamAffinity, new java.util.function.Function() { // from class: com.android.server.wm.LaunchParamsPersister$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.wm.LaunchParamsPersister.lambda$addComponentNameToLaunchParamAffinityMapIfNotNull$1((java.lang.String) obj);
            }
        }).add(name);
    }

    static /* synthetic */ android.util.ArraySet lambda$addComponentNameToLaunchParamAffinityMapIfNotNull$1(java.lang.String affinity) {
        return new android.util.ArraySet();
    }

    void getLaunchParams(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord activity, com.android.server.wm.LaunchParamsController.LaunchParams outParams) {
        java.lang.String windowLayoutAffinity;
        android.content.ComponentName name = task != null ? task.realActivity : activity.mActivityComponent;
        int userId = task != null ? task.mUserId : activity.mUserId;
        if (task != null) {
            windowLayoutAffinity = task.mWindowLayoutAffinity;
        } else {
            android.content.pm.ActivityInfo.WindowLayout layout = activity.info.windowLayout;
            windowLayoutAffinity = layout == null ? null : layout.windowLayoutAffinity;
        }
        outParams.reset();
        java.util.Map<android.content.ComponentName, com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams> map = this.mLaunchParamsMap.get(userId);
        if (map == null) {
            return;
        }
        com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams persistableParams = map.get(name);
        if (windowLayoutAffinity != null && this.mWindowLayoutAffinityMap.get(windowLayoutAffinity) != null) {
            android.util.ArraySet<android.content.ComponentName> candidates = this.mWindowLayoutAffinityMap.get(windowLayoutAffinity);
            for (int i = 0; i < candidates.size(); i++) {
                android.content.ComponentName candidate = candidates.valueAt(i);
                com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams candidateParams = map.get(candidate);
                if (candidateParams != null && (persistableParams == null || candidateParams.mTimestamp > persistableParams.mTimestamp)) {
                    persistableParams = candidateParams;
                }
            }
        }
        if (persistableParams == null) {
            return;
        }
        com.android.server.wm.DisplayContent display = this.mSupervisor.mRootWindowContainer.getDisplayContent(persistableParams.mDisplayUniqueId);
        if (display != null) {
            outParams.mPreferredTaskDisplayArea = display.getDefaultTaskDisplayArea();
        }
        outParams.mWindowingMode = persistableParams.mWindowingMode;
        outParams.mBounds.set(persistableParams.mBounds);
    }

    void removeRecordForPackage(final java.lang.String packageName) {
        java.util.List<java.io.File> fileToDelete = new java.util.ArrayList<>();
        for (int i = 0; i < this.mLaunchParamsMap.size(); i++) {
            int userId = this.mLaunchParamsMap.keyAt(i);
            java.io.File launchParamsFolder = getLaunchParamFolder(userId);
            android.util.ArrayMap<android.content.ComponentName, com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams> map = this.mLaunchParamsMap.valueAt(i);
            for (int j = map.size() - 1; j >= 0; j--) {
                android.content.ComponentName name = map.keyAt(j);
                if (name.getPackageName().equals(packageName)) {
                    map.removeAt(j);
                    fileToDelete.add(getParamFile(launchParamsFolder, name));
                }
            }
        }
        synchronized (this.mPersisterQueue) {
            this.mPersisterQueue.removeItems(new java.util.function.Predicate() { // from class: com.android.server.wm.LaunchParamsPersister$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.wm.LaunchParamsPersister.LaunchParamsWriteQueueItem) obj).mComponentName.getPackageName().equals(packageName);
                }
            }, com.android.server.wm.LaunchParamsPersister.LaunchParamsWriteQueueItem.class);
            this.mPersisterQueue.addItem(new com.android.server.wm.LaunchParamsPersister.CleanUpComponentQueueItem(fileToDelete), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.io.File getParamFile(java.io.File launchParamFolder, android.content.ComponentName name) {
        java.lang.String componentNameString = name.flattenToShortString().replace(ORIGINAL_COMPONENT_SEPARATOR, ESCAPED_COMPONENT_SEPARATOR);
        return new java.io.File(launchParamFolder, componentNameString + LAUNCH_PARAMS_FILE_SUFFIX);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.io.File getLaunchParamFolder(int userId) {
        java.io.File userFolder = this.mUserFolderGetter.apply(userId);
        return new java.io.File(userFolder, "launch_params");
    }

    private class PackageListObserver implements android.content.pm.PackageManagerInternal.PackageListObserver {
        private PackageListObserver() {
        }

        @Override // android.content.pm.PackageManagerInternal.PackageListObserver
        public void onPackageAdded(java.lang.String packageName, int uid) {
        }

        @Override // android.content.pm.PackageManagerInternal.PackageListObserver
        public void onPackageRemoved(java.lang.String packageName, int uid) {
            com.android.server.wm.WindowManagerGlobalLock globalLock = com.android.server.wm.LaunchParamsPersister.this.mSupervisor.mService.getGlobalLock();
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (globalLock) {
                try {
                    com.android.server.wm.LaunchParamsPersister.this.removeRecordForPackage(packageName);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class LaunchParamsWriteQueueItem implements com.android.server.wm.PersisterQueue.WriteQueueItem<com.android.server.wm.LaunchParamsPersister.LaunchParamsWriteQueueItem> {
        private final android.content.ComponentName mComponentName;
        private com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams mLaunchParams;
        private final int mUserId;

        private LaunchParamsWriteQueueItem(int userId, android.content.ComponentName componentName, com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams launchParams) {
            this.mUserId = userId;
            this.mComponentName = componentName;
            this.mLaunchParams = launchParams;
        }

        private byte[] saveParamsToXml() {
            try {
                java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
                com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(os);
                serializer.startDocument((java.lang.String) null, true);
                serializer.startTag((java.lang.String) null, "launch_params");
                this.mLaunchParams.saveToXml(serializer);
                serializer.endTag((java.lang.String) null, "launch_params");
                serializer.endDocument();
                serializer.flush();
                return os.toByteArray();
            } catch (java.io.IOException e) {
                return null;
            }
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            byte[] data = saveParamsToXml();
            java.io.File launchParamFolder = com.android.server.wm.LaunchParamsPersister.this.getLaunchParamFolder(this.mUserId);
            if (!launchParamFolder.isDirectory() && !launchParamFolder.mkdir()) {
                android.util.Slog.w(com.android.server.wm.LaunchParamsPersister.TAG, "Failed to create folder for " + this.mUserId);
                return;
            }
            java.io.File launchParamFile = com.android.server.wm.LaunchParamsPersister.this.getParamFile(launchParamFolder, this.mComponentName);
            android.util.AtomicFile atomicFile = new android.util.AtomicFile(launchParamFile);
            java.io.FileOutputStream stream = null;
            try {
                stream = atomicFile.startWrite();
                stream.write(data);
                atomicFile.finishWrite(stream);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.wm.LaunchParamsPersister.TAG, "Failed to write param file for " + this.mComponentName, e);
                if (stream != null) {
                    atomicFile.failWrite(stream);
                }
            }
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public boolean matches(com.android.server.wm.LaunchParamsPersister.LaunchParamsWriteQueueItem item) {
            return this.mUserId == item.mUserId && this.mComponentName.equals(item.mComponentName);
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void updateFrom(com.android.server.wm.LaunchParamsPersister.LaunchParamsWriteQueueItem item) {
            this.mLaunchParams = item.mLaunchParams;
        }
    }

    private class CleanUpComponentQueueItem implements com.android.server.wm.PersisterQueue.WriteQueueItem {
        private final java.util.List<java.io.File> mComponentFiles;

        private CleanUpComponentQueueItem(java.util.List<java.io.File> componentFiles) {
            this.mComponentFiles = componentFiles;
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            for (java.io.File file : this.mComponentFiles) {
                if (!file.delete()) {
                    android.util.Slog.w(com.android.server.wm.LaunchParamsPersister.TAG, "Failed to delete " + file.getAbsolutePath());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class PersistableLaunchParams {
        private static final java.lang.String ATTR_BOUNDS = "bounds";
        private static final java.lang.String ATTR_DISPLAY_UNIQUE_ID = "display_unique_id";
        private static final java.lang.String ATTR_WINDOWING_MODE = "windowing_mode";
        private static final java.lang.String ATTR_WINDOW_LAYOUT_AFFINITY = "window_layout_affinity";
        final android.graphics.Rect mBounds;
        java.lang.String mDisplayUniqueId;
        long mTimestamp;
        java.lang.String mWindowLayoutAffinity;
        int mWindowingMode;

        private PersistableLaunchParams() {
            this.mBounds = new android.graphics.Rect();
        }

        void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            serializer.attribute((java.lang.String) null, ATTR_DISPLAY_UNIQUE_ID, this.mDisplayUniqueId);
            serializer.attributeInt((java.lang.String) null, ATTR_WINDOWING_MODE, this.mWindowingMode);
            serializer.attribute((java.lang.String) null, ATTR_BOUNDS, this.mBounds.flattenToString());
            if (this.mWindowLayoutAffinity != null) {
                serializer.attribute((java.lang.String) null, ATTR_WINDOW_LAYOUT_AFFINITY, this.mWindowLayoutAffinity);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0041  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void restore(java.io.File r5, com.android.modules.utils.TypedXmlPullParser r6) {
            /*
                r4 = this;
                r0 = 0
            L1:
                int r1 = r6.getAttributeCount()
                if (r0 >= r1) goto L62
                java.lang.String r1 = r6.getAttributeValue(r0)
                java.lang.String r2 = r6.getAttributeName(r0)
                int r3 = r2.hashCode()
                switch(r3) {
                    case -1499361012: goto L37;
                    case -1383205195: goto L2d;
                    case 748872656: goto L22;
                    case 1999609934: goto L17;
                    default: goto L16;
                }
            L16:
                goto L41
            L17:
                java.lang.String r3 = "window_layout_affinity"
                boolean r2 = r2.equals(r3)
                if (r2 == 0) goto L16
                r2 = 3
                goto L42
            L22:
                java.lang.String r3 = "windowing_mode"
                boolean r2 = r2.equals(r3)
                if (r2 == 0) goto L16
                r2 = 1
                goto L42
            L2d:
                java.lang.String r3 = "bounds"
                boolean r2 = r2.equals(r3)
                if (r2 == 0) goto L16
                r2 = 2
                goto L42
            L37:
                java.lang.String r3 = "display_unique_id"
                boolean r2 = r2.equals(r3)
                if (r2 == 0) goto L16
                r2 = 0
                goto L42
            L41:
                r2 = -1
            L42:
                switch(r2) {
                    case 0: goto L5c;
                    case 1: goto L55;
                    case 2: goto L49;
                    case 3: goto L46;
                    default: goto L45;
                }
            L45:
                goto L5f
            L46:
                r4.mWindowLayoutAffinity = r1
                goto L5f
            L49:
                android.graphics.Rect r2 = android.graphics.Rect.unflattenFromString(r1)
                if (r2 == 0) goto L5f
                android.graphics.Rect r3 = r4.mBounds
                r3.set(r2)
                goto L5f
            L55:
                int r2 = java.lang.Integer.parseInt(r1)
                r4.mWindowingMode = r2
                goto L5f
            L5c:
                r4.mDisplayUniqueId = r1
            L5f:
                int r0 = r0 + 1
                goto L1
            L62:
                long r0 = r5.lastModified()
                r4.mTimestamp = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.LaunchParamsPersister.PersistableLaunchParams.restore(java.io.File, com.android.modules.utils.TypedXmlPullParser):void");
        }

        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder("PersistableLaunchParams{");
            builder.append(" windowingMode=" + this.mWindowingMode);
            builder.append(" displayUniqueId=" + this.mDisplayUniqueId);
            builder.append(" bounds=" + this.mBounds);
            if (this.mWindowLayoutAffinity != null) {
                builder.append(" launchParamsAffinity=" + this.mWindowLayoutAffinity);
            }
            builder.append(" timestamp=" + this.mTimestamp);
            builder.append(" }");
            return builder.toString();
        }
    }
}
