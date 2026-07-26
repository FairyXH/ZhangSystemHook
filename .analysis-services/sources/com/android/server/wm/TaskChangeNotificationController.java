package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TaskChangeNotificationController {
    private static final int NOTIFY_ACTIVITY_DISMISSING_DOCKED_ROOT_TASK_MSG = 7;
    private static final int NOTIFY_ACTIVITY_LAUNCH_ON_SECONDARY_DISPLAY_FAILED_MSG = 18;
    private static final int NOTIFY_ACTIVITY_LAUNCH_ON_SECONDARY_DISPLAY_REROUTED_MSG = 19;
    private static final int NOTIFY_ACTIVITY_PINNED_LISTENERS_MSG = 3;
    private static final int NOTIFY_ACTIVITY_REQUESTED_ORIENTATION_CHANGED_LISTENERS = 12;
    private static final int NOTIFY_ACTIVITY_RESTART_ATTEMPT_LISTENERS_MSG = 4;
    private static final int NOTIFY_ACTIVITY_ROTATED_MSG = 26;
    private static final int NOTIFY_ACTIVITY_UNPINNED_LISTENERS_MSG = 17;
    private static final int NOTIFY_BACK_PRESSED_ON_TASK_ROOT = 20;
    private static final int NOTIFY_FORCED_RESIZABLE_MSG = 6;
    private static final int NOTIFY_LOCK_TASK_MODE_CHANGED_MSG = 28;
    private static final int NOTIFY_TASK_ADDED_LISTENERS_MSG = 8;
    private static final int NOTIFY_TASK_DESCRIPTION_CHANGED_LISTENERS_MSG = 11;
    private static final int NOTIFY_TASK_DISPLAY_CHANGED_LISTENERS_MSG = 21;
    private static final int NOTIFY_TASK_FOCUS_CHANGED_MSG = 24;
    private static final int NOTIFY_TASK_LIST_FROZEN_UNFROZEN_MSG = 23;
    private static final int NOTIFY_TASK_LIST_UPDATED_LISTENERS_MSG = 22;
    private static final int NOTIFY_TASK_MOVED_TO_BACK_LISTENERS_MSG = 27;
    private static final int NOTIFY_TASK_MOVED_TO_FRONT_LISTENERS_MSG = 10;
    private static final int NOTIFY_TASK_PROFILE_LOCKED_LISTENERS_MSG = 14;
    private static final int NOTIFY_TASK_REMOVAL_STARTED_LISTENERS = 13;
    private static final int NOTIFY_TASK_REMOVED_LISTENERS_MSG = 9;
    private static final int NOTIFY_TASK_REQUESTED_ORIENTATION_CHANGED_MSG = 25;
    private static final int NOTIFY_TASK_SNAPSHOT_CHANGED_LISTENERS_MSG = 15;
    private static final int NOTIFY_TASK_SNAPSHOT_INVALIDATED_LISTENERS_MSG = 29;
    private static final int NOTIFY_TASK_STACK_CHANGE_LISTENERS_DELAY = 100;
    private static final int NOTIFY_TASK_STACK_CHANGE_LISTENERS_MSG = 2;
    private final android.os.Handler mHandler;
    private com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;
    private final android.os.RemoteCallbackList<android.app.ITaskStackListener> mRemoteTaskStackListeners = new android.os.RemoteCallbackList<>();
    private com.android.server.wm.ITaskChangeNotificationControllerExt mTaskChangeNotificationControllerExt = (com.android.server.wm.ITaskChangeNotificationControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskChangeNotificationControllerExt.class).base(this).create();
    private final java.util.ArrayList<android.app.ITaskStackListener> mLocalTaskStackListeners = new java.util.ArrayList<>();
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskStackChanged = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda0
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskStackChanged();
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskCreated = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda11
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskCreated(message.arg1, (android.content.ComponentName) message.obj);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskRemoved = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda18
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskRemoved(message.arg1);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskMovedToFront = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda19
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskMovedToFront((android.app.ActivityManager.RunningTaskInfo) message.obj);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskDescriptionChanged = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda20
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskDescriptionChanged((android.app.ActivityManager.RunningTaskInfo) message.obj);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyBackPressedOnTaskRoot = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda21
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onBackPressedOnTaskRoot((android.app.ActivityManager.RunningTaskInfo) message.obj);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyActivityRequestedOrientationChanged = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda22
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onActivityRequestedOrientationChanged(message.arg1, message.arg2);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskRemovalStarted = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda23
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskRemovalStarted((android.app.ActivityManager.RunningTaskInfo) message.obj);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyActivityPinned = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda24
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onActivityPinned((java.lang.String) message.obj, message.sendingUid, message.arg1, message.arg2);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyActivityUnpinned = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda25
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onActivityUnpinned();
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyActivityRestartAttempt = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda1
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) throws android.os.RemoteException {
            com.android.server.wm.TaskChangeNotificationController.lambda$new$10(iTaskStackListener, message);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyActivityForcedResizable = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda2
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onActivityForcedResizable((java.lang.String) message.obj, message.arg1, message.arg2);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyActivityDismissingDockedTask = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda3
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onActivityDismissingDockedTask();
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyActivityLaunchOnSecondaryDisplayFailed = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda4
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onActivityLaunchOnSecondaryDisplayFailed((android.app.ActivityManager.RunningTaskInfo) message.obj, message.arg1);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyActivityLaunchOnSecondaryDisplayRerouted = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda5
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onActivityLaunchOnSecondaryDisplayRerouted((android.app.ActivityManager.RunningTaskInfo) message.obj, message.arg1);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskProfileLocked = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda6
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskProfileLocked((android.app.ActivityManager.RunningTaskInfo) message.obj, message.arg1);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskSnapshotChanged = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda7
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) throws android.os.RemoteException {
            this.f$0.lambda$new$16(iTaskStackListener, message);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskSnapshotInvalidated = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda8
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskSnapshotInvalidated(message.arg1);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskDisplayChanged = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda9
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskDisplayChanged(message.arg1, message.arg2);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskListUpdated = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda10
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onRecentTaskListUpdated();
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskListFrozen = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda12
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onRecentTaskListFrozenChanged(message.arg1 != 0);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskFocusChanged = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda13
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskFocusChanged(message.arg1, message.arg2 != 0);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskRequestedOrientationChanged = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda14
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskRequestedOrientationChanged(message.arg1, message.arg2);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyOnActivityRotation = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda15
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onActivityRotation(message.arg1);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyTaskMovedToBack = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda16
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onTaskMovedToBack((android.app.ActivityManager.RunningTaskInfo) message.obj);
        }
    };
    private final com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer mNotifyLockTaskModeChanged = new com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer() { // from class: com.android.server.wm.TaskChangeNotificationController$$ExternalSyntheticLambda17
        @Override // com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer
        public final void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) {
            iTaskStackListener.onLockTaskModeChanged(message.arg1);
        }
    };

    @java.lang.FunctionalInterface
    public interface TaskStackConsumer {
        void accept(android.app.ITaskStackListener iTaskStackListener, android.os.Message message) throws android.os.RemoteException;
    }

    static /* synthetic */ void lambda$new$10(android.app.ITaskStackListener l, android.os.Message m) throws android.os.RemoteException {
        com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) m.obj;
        l.onActivityRestartAttempt((android.app.ActivityManager.RunningTaskInfo) args.arg1, args.argi1 != 0, args.argi2 != 0, args.argi3 != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$16(android.app.ITaskStackListener l, android.os.Message m) throws android.os.RemoteException {
        android.window.TaskSnapshot tSnapshot = (android.window.TaskSnapshot) m.obj;
        if (tSnapshot != null && tSnapshot.getSnapshot() != null && tSnapshot.getSnapshot().isDestroyed()) {
            android.util.Slog.i("TaskChangeNotificationController", "NotifyTaskSnapshotChanged " + tSnapshot + ", is destroy");
        } else {
            if (this.mTaskChangeNotificationControllerExt.shouldSkipSendTaskSnapshot(this.mTaskSupervisor, l, m)) {
                return;
            }
            l.onTaskSnapshotChanged(m.arg1, (android.window.TaskSnapshot) m.obj);
        }
    }

    private class MainHandler extends android.os.Handler {
        public MainHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskStackChanged, msg);
                    break;
                case 3:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyActivityPinned, msg);
                    break;
                case 4:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyActivityRestartAttempt, msg);
                    break;
                case 6:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyActivityForcedResizable, msg);
                    break;
                case 7:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyActivityDismissingDockedTask, msg);
                    break;
                case 8:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskCreated, msg);
                    break;
                case 9:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskRemoved, msg);
                    break;
                case 10:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskMovedToFront, msg);
                    break;
                case 11:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskDescriptionChanged, msg);
                    break;
                case 12:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyActivityRequestedOrientationChanged, msg);
                    break;
                case 13:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskRemovalStarted, msg);
                    break;
                case 14:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskProfileLocked, msg);
                    break;
                case 15:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskSnapshotChanged, msg);
                    ((android.window.TaskSnapshot) msg.obj).removeReference(1);
                    break;
                case 17:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyActivityUnpinned, msg);
                    break;
                case 18:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyActivityLaunchOnSecondaryDisplayFailed, msg);
                    break;
                case 19:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyActivityLaunchOnSecondaryDisplayRerouted, msg);
                    break;
                case 20:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyBackPressedOnTaskRoot, msg);
                    break;
                case 21:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskDisplayChanged, msg);
                    break;
                case 22:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskListUpdated, msg);
                    break;
                case 23:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskListFrozen, msg);
                    break;
                case 24:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskFocusChanged, msg);
                    break;
                case 25:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskRequestedOrientationChanged, msg);
                    break;
                case 26:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyOnActivityRotation, msg);
                    break;
                case 27:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskMovedToBack, msg);
                    break;
                case 28:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyLockTaskModeChanged, msg);
                    break;
                case 29:
                    com.android.server.wm.TaskChangeNotificationController.this.forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.this.mNotifyTaskSnapshotInvalidated, msg);
                    break;
            }
            if (msg.obj instanceof com.android.internal.os.SomeArgs) {
                ((com.android.internal.os.SomeArgs) msg.obj).recycle();
            }
        }
    }

    TaskChangeNotificationController(com.android.server.wm.ActivityTaskSupervisor taskSupervisor, android.os.Handler handler) {
        this.mTaskSupervisor = taskSupervisor;
        this.mHandler = new com.android.server.wm.TaskChangeNotificationController.MainHandler(handler.getLooper());
    }

    public void registerTaskStackListener(android.app.ITaskStackListener listener) {
        if (listener instanceof android.os.Binder) {
            synchronized (this.mLocalTaskStackListeners) {
                if (!this.mLocalTaskStackListeners.contains(listener)) {
                    if (listener instanceof android.app.TaskStackListener) {
                        ((android.app.TaskStackListener) listener).setIsLocal();
                    }
                    this.mLocalTaskStackListeners.add(listener);
                }
            }
            return;
        }
        if (listener != null) {
            synchronized (this.mRemoteTaskStackListeners) {
                this.mRemoteTaskStackListeners.register(listener);
            }
        }
    }

    public void unregisterTaskStackListener(android.app.ITaskStackListener listener) {
        if (listener instanceof android.os.Binder) {
            synchronized (this.mLocalTaskStackListeners) {
                this.mLocalTaskStackListeners.remove(listener);
            }
        } else if (listener != null) {
            synchronized (this.mRemoteTaskStackListeners) {
                this.mRemoteTaskStackListeners.unregister(listener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forAllRemoteListeners(com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer callback, android.os.Message message) {
        synchronized (this.mRemoteTaskStackListeners) {
            for (int i = this.mRemoteTaskStackListeners.beginBroadcast() - 1; i >= 0; i--) {
                try {
                    callback.accept((android.app.ITaskStackListener) this.mRemoteTaskStackListeners.getBroadcastItem(i), message);
                } catch (android.os.RemoteException e) {
                }
            }
            this.mRemoteTaskStackListeners.finishBroadcast();
        }
    }

    private void forAllLocalListeners(com.android.server.wm.TaskChangeNotificationController.TaskStackConsumer callback, android.os.Message message) {
        synchronized (this.mLocalTaskStackListeners) {
            for (int i = this.mLocalTaskStackListeners.size() - 1; i >= 0; i--) {
                try {
                    callback.accept(this.mLocalTaskStackListeners.get(i), message);
                } catch (android.os.RemoteException e) {
                }
            }
        }
    }

    void notifyTaskStackChanged() {
        this.mTaskSupervisor.getActivityMetricsLogger().logWindowState();
        this.mHandler.removeMessages(2);
        android.os.Message msg = this.mHandler.obtainMessage(2);
        forAllLocalListeners(this.mNotifyTaskStackChanged, msg);
        this.mHandler.sendMessageDelayed(msg, 100L);
    }

    void notifyActivityPinned(com.android.server.wm.ActivityRecord r) {
        this.mHandler.removeMessages(3);
        android.os.Message msg = this.mHandler.obtainMessage(3, r.getTask().mTaskId, r.getRootTaskId(), r.packageName);
        msg.sendingUid = r.mUserId;
        forAllLocalListeners(this.mNotifyActivityPinned, msg);
        msg.sendToTarget();
    }

    void notifyActivityUnpinned() {
        this.mHandler.removeMessages(17);
        android.os.Message msg = this.mHandler.obtainMessage(17);
        forAllLocalListeners(this.mNotifyActivityUnpinned, msg);
        msg.sendToTarget();
    }

    void notifyActivityRestartAttempt(android.app.ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
        this.mHandler.removeMessages(4);
        com.android.internal.os.SomeArgs someArgsObtain = com.android.internal.os.SomeArgs.obtain();
        someArgsObtain.arg1 = runningTaskInfo;
        someArgsObtain.argi1 = z ? 1 : 0;
        someArgsObtain.argi2 = z2 ? 1 : 0;
        someArgsObtain.argi3 = z3 ? 1 : 0;
        android.os.Message messageObtainMessage = this.mHandler.obtainMessage(4, someArgsObtain);
        forAllLocalListeners(this.mNotifyActivityRestartAttempt, messageObtainMessage);
        messageObtainMessage.sendToTarget();
    }

    void notifyActivityDismissingDockedRootTask() {
        this.mHandler.removeMessages(7);
        android.os.Message msg = this.mHandler.obtainMessage(7);
        forAllLocalListeners(this.mNotifyActivityDismissingDockedTask, msg);
        msg.sendToTarget();
    }

    void notifyActivityForcedResizable(int taskId, int reason, java.lang.String packageName) {
        this.mHandler.removeMessages(6);
        android.os.Message msg = this.mHandler.obtainMessage(6, taskId, reason, packageName);
        forAllLocalListeners(this.mNotifyActivityForcedResizable, msg);
        msg.sendToTarget();
    }

    void notifyActivityLaunchOnSecondaryDisplayFailed(android.app.TaskInfo ti, int requestedDisplayId) {
        this.mHandler.removeMessages(18);
        android.os.Message msg = this.mHandler.obtainMessage(18, requestedDisplayId, 0, ti);
        forAllLocalListeners(this.mNotifyActivityLaunchOnSecondaryDisplayFailed, msg);
        msg.sendToTarget();
    }

    void notifyActivityLaunchOnSecondaryDisplayRerouted(android.app.TaskInfo ti, int requestedDisplayId) {
        this.mHandler.removeMessages(19);
        android.os.Message msg = this.mHandler.obtainMessage(19, requestedDisplayId, 0, ti);
        forAllLocalListeners(this.mNotifyActivityLaunchOnSecondaryDisplayRerouted, msg);
        msg.sendToTarget();
    }

    void notifyTaskCreated(int taskId, android.content.ComponentName componentName) {
        android.os.Message msg = this.mHandler.obtainMessage(8, taskId, 0, componentName);
        forAllLocalListeners(this.mNotifyTaskCreated, msg);
        msg.sendToTarget();
    }

    void notifyTaskRemoved(int taskId) {
        android.os.Message msg = this.mHandler.obtainMessage(9, taskId, 0);
        forAllLocalListeners(this.mNotifyTaskRemoved, msg);
        msg.sendToTarget();
    }

    void notifyTaskMovedToFront(android.app.TaskInfo ti) {
        android.os.Message msg = this.mHandler.obtainMessage(10, ti);
        forAllLocalListeners(this.mNotifyTaskMovedToFront, msg);
        msg.sendToTarget();
    }

    void notifyTaskDescriptionChanged(android.app.TaskInfo taskInfo) {
        android.os.Message msg = this.mHandler.obtainMessage(11, taskInfo);
        forAllLocalListeners(this.mNotifyTaskDescriptionChanged, msg);
        msg.sendToTarget();
    }

    void notifyActivityRequestedOrientationChanged(int taskId, int orientation) {
        android.os.Message msg = this.mHandler.obtainMessage(12, taskId, orientation);
        forAllLocalListeners(this.mNotifyActivityRequestedOrientationChanged, msg);
        msg.sendToTarget();
    }

    void notifyTaskRemovalStarted(android.app.ActivityManager.RunningTaskInfo taskInfo) {
        android.os.Message msg = this.mHandler.obtainMessage(13, taskInfo);
        forAllLocalListeners(this.mNotifyTaskRemovalStarted, msg);
        msg.sendToTarget();
    }

    void notifyTaskProfileLocked(android.app.ActivityManager.RunningTaskInfo taskInfo, int userId) {
        android.os.Message msg = this.mHandler.obtainMessage(14, userId, 0, taskInfo);
        forAllLocalListeners(this.mNotifyTaskProfileLocked, msg);
        msg.sendToTarget();
    }

    void notifyTaskSnapshotChanged(int taskId, android.window.TaskSnapshot snapshot) {
        snapshot.addReference(1);
        android.os.Message msg = this.mHandler.obtainMessage(15, taskId, 0, snapshot);
        forAllLocalListeners(this.mNotifyTaskSnapshotChanged, msg);
        msg.sendToTarget();
    }

    void notifyTaskSnapshotInvalidated(int taskId) {
        android.os.Message msg = this.mHandler.obtainMessage(29, taskId, 0);
        forAllLocalListeners(this.mNotifyTaskSnapshotInvalidated, msg);
        msg.sendToTarget();
    }

    void notifyBackPressedOnTaskRoot(android.app.TaskInfo taskInfo) {
        android.os.Message msg = this.mHandler.obtainMessage(20, taskInfo);
        forAllLocalListeners(this.mNotifyBackPressedOnTaskRoot, msg);
        msg.sendToTarget();
    }

    void notifyTaskDisplayChanged(int taskId, int newDisplayId) {
        android.os.Message msg = this.mHandler.obtainMessage(21, taskId, newDisplayId);
        forAllLocalListeners(this.mNotifyTaskDisplayChanged, msg);
        msg.sendToTarget();
    }

    void notifyTaskListUpdated() {
        android.os.Message msg = this.mHandler.obtainMessage(22);
        forAllLocalListeners(this.mNotifyTaskListUpdated, msg);
        msg.sendToTarget();
    }

    void notifyTaskListFrozen(boolean z) {
        android.os.Message messageObtainMessage = this.mHandler.obtainMessage(23, z ? 1 : 0, 0);
        forAllLocalListeners(this.mNotifyTaskListFrozen, messageObtainMessage);
        messageObtainMessage.sendToTarget();
    }

    void notifyTaskFocusChanged(int i, boolean z) {
        android.os.Message messageObtainMessage = this.mHandler.obtainMessage(24, i, z ? 1 : 0);
        forAllLocalListeners(this.mNotifyTaskFocusChanged, messageObtainMessage);
        messageObtainMessage.sendToTarget();
    }

    void notifyTaskRequestedOrientationChanged(int taskId, int requestedOrientation) {
        android.os.Message msg = this.mHandler.obtainMessage(25, taskId, requestedOrientation);
        forAllLocalListeners(this.mNotifyTaskRequestedOrientationChanged, msg);
        msg.sendToTarget();
    }

    void notifyOnActivityRotation(int displayId) {
        android.os.Message msg = this.mHandler.obtainMessage(26, displayId, 0);
        forAllLocalListeners(this.mNotifyOnActivityRotation, msg);
        msg.sendToTarget();
    }

    void notifyTaskMovedToBack(android.app.TaskInfo ti) {
        android.os.Message msg = this.mHandler.obtainMessage(27, ti);
        forAllLocalListeners(this.mNotifyTaskMovedToBack, msg);
        msg.sendToTarget();
    }

    void notifyLockTaskModeChanged(int lockTaskModeState) {
        android.os.Message msg = this.mHandler.obtainMessage(28, lockTaskModeState, 0);
        forAllLocalListeners(this.mNotifyLockTaskModeChanged, msg);
        msg.sendToTarget();
    }
}
