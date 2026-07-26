package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class AdditionalSubtypeMapRepository {
    private static final android.util.SparseArray<com.android.server.inputmethod.AdditionalSubtypeMap> sPerUserMap = new android.util.SparseArray<>();
    private static final com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter sWriter = new com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter();

    static final class WriteTask extends java.lang.Record {
        private final com.android.server.inputmethod.InputMethodMap inputMethodMap;
        private final com.android.server.inputmethod.AdditionalSubtypeMap subtypeMap;
        private final int userId;

        WriteTask(int userId, com.android.server.inputmethod.AdditionalSubtypeMap subtypeMap, com.android.server.inputmethod.InputMethodMap inputMethodMap) {
            this.userId = userId;
            this.subtypeMap = subtypeMap;
            this.inputMethodMap = inputMethodMap;
        }

        @Override // java.lang.Record
        public final boolean equals(java.lang.Object o) {
            return (boolean) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "equals", java.lang.invoke.MethodType.methodType(java.lang.Boolean.TYPE, com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask.class, java.lang.Object.class), com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask.class, "userId;subtypeMap;inputMethodMap", "FIELD:Lcom/android/server/inputmethod/AdditionalSubtypeMapRepository$WriteTask;->userId:I", "FIELD:Lcom/android/server/inputmethod/AdditionalSubtypeMapRepository$WriteTask;->subtypeMap:Lcom/android/server/inputmethod/AdditionalSubtypeMap;", "FIELD:Lcom/android/server/inputmethod/AdditionalSubtypeMapRepository$WriteTask;->inputMethodMap:Lcom/android/server/inputmethod/InputMethodMap;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "hashCode", java.lang.invoke.MethodType.methodType(java.lang.Integer.TYPE, com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask.class), com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask.class, "userId;subtypeMap;inputMethodMap", "FIELD:Lcom/android/server/inputmethod/AdditionalSubtypeMapRepository$WriteTask;->userId:I", "FIELD:Lcom/android/server/inputmethod/AdditionalSubtypeMapRepository$WriteTask;->subtypeMap:Lcom/android/server/inputmethod/AdditionalSubtypeMap;", "FIELD:Lcom/android/server/inputmethod/AdditionalSubtypeMapRepository$WriteTask;->inputMethodMap:Lcom/android/server/inputmethod/InputMethodMap;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        public com.android.server.inputmethod.InputMethodMap inputMethodMap() {
            return this.inputMethodMap;
        }

        public com.android.server.inputmethod.AdditionalSubtypeMap subtypeMap() {
            return this.subtypeMap;
        }

        @Override // java.lang.Record
        public final java.lang.String toString() {
            return (java.lang.String) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "toString", java.lang.invoke.MethodType.methodType(java.lang.String.class, com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask.class), com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask.class, "userId;subtypeMap;inputMethodMap", "FIELD:Lcom/android/server/inputmethod/AdditionalSubtypeMapRepository$WriteTask;->userId:I", "FIELD:Lcom/android/server/inputmethod/AdditionalSubtypeMapRepository$WriteTask;->subtypeMap:Lcom/android/server/inputmethod/AdditionalSubtypeMap;", "FIELD:Lcom/android/server/inputmethod/AdditionalSubtypeMapRepository$WriteTask;->inputMethodMap:Lcom/android/server/inputmethod/InputMethodMap;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        public int userId() {
            return this.userId;
        }
    }

    static final class SingleThreadedBackgroundWriter {
        private final java.util.concurrent.locks.ReentrantLock mLock = new java.util.concurrent.locks.ReentrantLock();
        private final java.util.concurrent.locks.Condition mLockNotifier = this.mLock.newCondition();
        private final android.util.SparseArray<com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask> mPendingTasks = new android.util.SparseArray<>();
        private final android.util.IntArray mRemovedUsers = new android.util.IntArray();
        private final java.lang.Thread mWriterThread = new com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.AnonymousClass1("android.ime.as");

        SingleThreadedBackgroundWriter() {
        }

        /* JADX INFO: renamed from: com.android.server.inputmethod.AdditionalSubtypeMapRepository$SingleThreadedBackgroundWriter$1, reason: invalid class name */
        class AnonymousClass1 extends java.lang.Thread {
            AnonymousClass1(java.lang.String arg1) {
                super(arg1);
            }

            private java.util.ArrayList<com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask> fetchNextTasks() {
                android.util.IntArray removedUsers;
                com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.this.mLock.lock();
                while (com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.this.mPendingTasks.size() == 0) {
                    try {
                        com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.this.mLockNotifier.awaitUninterruptibly();
                    } catch (java.lang.Throwable th) {
                        com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.this.mLock.unlock();
                        throw th;
                    }
                }
                android.util.SparseArray<com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask> tasks = com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.this.mPendingTasks.clone();
                com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.this.mPendingTasks.clear();
                if (com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.this.mRemovedUsers.size() == 0) {
                    removedUsers = null;
                } else {
                    removedUsers = com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.this.mRemovedUsers.clone();
                }
                com.android.server.inputmethod.AdditionalSubtypeMapRepository.SingleThreadedBackgroundWriter.this.mLock.unlock();
                int size = tasks.size();
                java.util.ArrayList<com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask> result = new java.util.ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    int userId = tasks.keyAt(i);
                    if (removedUsers == null || !removedUsers.contains(userId)) {
                        result.add(tasks.valueAt(i));
                    }
                }
                return result;
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                android.os.Process.setThreadPriority(10);
                while (true) {
                    java.util.ArrayList<com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask> tasks = fetchNextTasks();
                    tasks.forEach(new java.util.function.Consumer() { // from class: com.android.server.inputmethod.AdditionalSubtypeMapRepository$SingleThreadedBackgroundWriter$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) throws java.lang.Throwable {
                            com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask writeTask = (com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask) obj;
                            com.android.server.inputmethod.AdditionalSubtypeUtils.save(writeTask.subtypeMap, writeTask.inputMethodMap, writeTask.userId);
                        }
                    });
                }
            }
        }

        void scheduleWriteTask(int userId, com.android.server.inputmethod.AdditionalSubtypeMap subtypeMap, com.android.server.inputmethod.InputMethodMap inputMethodMap) {
            com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask task = new com.android.server.inputmethod.AdditionalSubtypeMapRepository.WriteTask(userId, subtypeMap, inputMethodMap);
            this.mLock.lock();
            try {
                if (this.mRemovedUsers.contains(userId)) {
                    return;
                }
                this.mPendingTasks.put(userId, task);
                this.mLockNotifier.signalAll();
            } finally {
                this.mLock.unlock();
            }
        }

        void onUserCreated(int userId) {
            this.mLock.lock();
            try {
                for (int i = this.mRemovedUsers.size() - 1; i >= 0; i--) {
                    if (this.mRemovedUsers.get(i) == userId) {
                        this.mRemovedUsers.remove(i);
                    }
                }
            } finally {
                this.mLock.unlock();
            }
        }

        void onUserRemoved(int userId) {
            this.mLock.lock();
            try {
                this.mRemovedUsers.add(userId);
                this.mPendingTasks.remove(userId);
            } finally {
                this.mLock.unlock();
            }
        }

        void startThread() {
            this.mWriterThread.start();
        }
    }

    private AdditionalSubtypeMapRepository() {
    }

    static com.android.server.inputmethod.AdditionalSubtypeMap get(int userId) {
        com.android.server.inputmethod.AdditionalSubtypeMap map = sPerUserMap.get(userId);
        if (map != null) {
            return map;
        }
        com.android.server.inputmethod.AdditionalSubtypeMap newMap = com.android.server.inputmethod.AdditionalSubtypeUtils.load(userId);
        sPerUserMap.put(userId, newMap);
        return newMap;
    }

    static void putAndSave(int userId, com.android.server.inputmethod.AdditionalSubtypeMap map, com.android.server.inputmethod.InputMethodMap inputMethodMap) {
        com.android.server.inputmethod.AdditionalSubtypeMap previous = sPerUserMap.get(userId);
        if (previous == map) {
            return;
        }
        sPerUserMap.put(userId, map);
        sWriter.scheduleWriteTask(userId, map, inputMethodMap);
    }

    static void startWriterThread() {
        sWriter.startThread();
    }

    static void initialize(final android.os.Handler handler, final android.content.Context context) {
        final com.android.server.pm.UserManagerInternal userManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.AdditionalSubtypeMapRepository$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.inputmethod.AdditionalSubtypeMapRepository.lambda$initialize$0(userManagerInternal, handler, context);
            }
        });
    }

    /* JADX INFO: renamed from: com.android.server.inputmethod.AdditionalSubtypeMapRepository$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.pm.UserManagerInternal.UserLifecycleListener {
        final /* synthetic */ android.content.Context val$context;
        final /* synthetic */ android.os.Handler val$handler;

        AnonymousClass1(android.os.Handler handler, android.content.Context context) {
            this.val$handler = handler;
            this.val$context = context;
        }

        @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
        public void onUserCreated(android.content.pm.UserInfo user, java.lang.Object token) {
            final int userId = user.id;
            com.android.server.inputmethod.AdditionalSubtypeMapRepository.sWriter.onUserCreated(userId);
            android.os.Handler handler = this.val$handler;
            final android.content.Context context = this.val$context;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.AdditionalSubtypeMapRepository$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.inputmethod.AdditionalSubtypeMapRepository.AnonymousClass1.lambda$onUserCreated$0(userId, context);
                }
            });
        }

        static /* synthetic */ void lambda$onUserCreated$0(int userId, android.content.Context context) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (!com.android.server.inputmethod.AdditionalSubtypeMapRepository.sPerUserMap.contains(userId)) {
                    com.android.server.inputmethod.AdditionalSubtypeMap additionalSubtypeMap = com.android.server.inputmethod.AdditionalSubtypeUtils.load(userId);
                    com.android.server.inputmethod.AdditionalSubtypeMapRepository.sPerUserMap.put(userId, additionalSubtypeMap);
                    com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodManagerService.queryInputMethodServicesInternal(context, userId, additionalSubtypeMap, 0);
                    com.android.server.inputmethod.InputMethodSettingsRepository.put(userId, settings);
                }
            }
        }

        @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
        public void onUserRemoved(android.content.pm.UserInfo user) {
            final int userId = user.id;
            com.android.server.inputmethod.AdditionalSubtypeMapRepository.sWriter.onUserRemoved(userId);
            this.val$handler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.AdditionalSubtypeMapRepository$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.inputmethod.AdditionalSubtypeMapRepository.AnonymousClass1.lambda$onUserRemoved$1(userId);
                }
            });
        }

        static /* synthetic */ void lambda$onUserRemoved$1(int userId) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.AdditionalSubtypeMapRepository.sPerUserMap.remove(userId);
            }
        }
    }

    static /* synthetic */ void lambda$initialize$0(com.android.server.pm.UserManagerInternal userManagerInternal, android.os.Handler handler, android.content.Context context) {
        userManagerInternal.addUserLifecycleListener(new com.android.server.inputmethod.AdditionalSubtypeMapRepository.AnonymousClass1(handler, context));
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            for (int userId : userManagerInternal.getUserIds()) {
                sPerUserMap.put(userId, com.android.server.inputmethod.AdditionalSubtypeUtils.load(userId));
            }
        }
    }
}
