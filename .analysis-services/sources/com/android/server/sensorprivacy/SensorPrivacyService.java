package com.android.server.sensorprivacy;

/* JADX INFO: loaded from: classes3.dex */
public final class SensorPrivacyService extends com.android.server.SystemService {
    private static final int ACTION__ACTION_UNKNOWN = 0;
    private static final int ACTION__TOGGLE_OFF = 2;
    private static final int ACTION__TOGGLE_ON = 1;
    private static final int ACTION__TOGGLE_ON_EXCEPT_ALLOWLISTED_APPS = 3;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_LOGGING = false;
    public static final int REMINDER_DIALOG_DELAY_MILLIS = 500;
    private static final java.lang.String SENSOR_PRIVACY_CHANNEL_ID = "sensor_privacy";
    private final android.app.ActivityManager mActivityManager;
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final android.app.ActivityTaskManager mActivityTaskManager;
    private final android.app.AppOpsManager mAppOpsManager;
    private final android.app.AppOpsManagerInternal mAppOpsManagerInternal;
    private final android.os.IBinder mAppOpsRestrictionToken;
    private com.android.server.sensorprivacy.SensorPrivacyService.CallStateHelper mCallStateHelper;
    java.util.List<java.lang.String> mCameraPrivacyAllowlist;
    private com.android.server.sensorprivacy.CameraPrivacyLightController mCameraPrivacyLightController;
    private final android.content.Context mContext;
    private int mCurrentUser;
    private android.app.KeyguardManager mKeyguardManager;
    private final android.app.NotificationManager mNotificationManager;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyManagerInternalImpl mSensorPrivacyManagerInternal;
    public com.android.server.sensorprivacy.ISensorPrivacyServiceExt mSensorPrivacyServiceExt;
    private final com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl mSensorPrivacyServiceImpl;
    private final android.telephony.TelephonyManager mTelephonyManager;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private static final java.lang.String TAG = com.android.server.sensorprivacy.SensorPrivacyService.class.getSimpleName();
    private static final java.lang.String ACTION_DISABLE_TOGGLE_SENSOR_PRIVACY = com.android.server.sensorprivacy.SensorPrivacyService.class.getName() + ".action.disable_sensor_privacy";

    public SensorPrivacyService(android.content.Context context) {
        super(context);
        this.mAppOpsRestrictionToken = new android.os.Binder();
        this.mCameraPrivacyAllowlist = new java.util.ArrayList();
        this.mCurrentUser = -10000;
        this.mSensorPrivacyServiceExt = (com.android.server.sensorprivacy.ISensorPrivacyServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.sensorprivacy.ISensorPrivacyServiceExt.class).base(this).create();
        this.mContext = context;
        this.mAppOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        this.mAppOpsManagerInternal = (android.app.AppOpsManagerInternal) getLocalService(android.app.AppOpsManagerInternal.class);
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) getLocalService(com.android.server.pm.UserManagerInternal.class);
        this.mActivityManager = (android.app.ActivityManager) context.getSystemService(android.app.ActivityManager.class);
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) getLocalService(android.app.ActivityManagerInternal.class);
        this.mActivityTaskManager = (android.app.ActivityTaskManager) context.getSystemService(android.app.ActivityTaskManager.class);
        this.mTelephonyManager = (android.telephony.TelephonyManager) context.getSystemService(android.telephony.TelephonyManager.class);
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) getLocalService(android.content.pm.PackageManagerInternal.class);
        this.mNotificationManager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        this.mSensorPrivacyServiceImpl = new com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl();
        for (java.lang.String entry : com.android.server.SystemConfig.getInstance().getCameraPrivacyAllowlist()) {
            this.mCameraPrivacyAllowlist.add(entry);
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService(SENSOR_PRIVACY_CHANNEL_ID, this.mSensorPrivacyServiceImpl);
        this.mSensorPrivacyManagerInternal = new com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyManagerInternalImpl();
        publishLocalService(android.hardware.SensorPrivacyManagerInternal.class, this.mSensorPrivacyManagerInternal);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            this.mKeyguardManager = (android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class);
            this.mCallStateHelper = new com.android.server.sensorprivacy.SensorPrivacyService.CallStateHelper();
            this.mSensorPrivacyServiceImpl.registerSettingsObserver();
        } else if (phase == 550) {
            this.mCameraPrivacyLightController = new com.android.server.sensorprivacy.CameraPrivacyLightController(this.mContext);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        if (this.mCurrentUser == -10000) {
            this.mCurrentUser = user.getUserIdentifier();
            this.mSensorPrivacyServiceImpl.userSwitching(-10000, user.getUserIdentifier());
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        this.mCurrentUser = to.getUserIdentifier();
        this.mSensorPrivacyServiceImpl.userSwitching(from.getUserIdentifier(), to.getUserIdentifier());
    }

    class SensorPrivacyServiceImpl extends android.hardware.ISensorPrivacyManager.Stub implements android.app.AppOpsManager.OnOpNotedInternalListener, android.app.AppOpsManager.OnOpStartedListener, android.os.IBinder.DeathRecipient, com.android.server.pm.UserManagerInternal.UserRestrictionsListener {
        private final com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyHandler mHandler;
        private final java.lang.Object mLock = new java.lang.Object();
        private android.util.ArrayMap<android.util.Pair<java.lang.Integer, android.os.UserHandle>, java.util.ArrayList<android.os.IBinder>> mSuppressReminders = new android.util.ArrayMap<>();
        private final android.util.ArrayMap<com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.SensorUseReminderDialogInfo, android.util.ArraySet<java.lang.Integer>> mQueuedSensorUseReminderDialogs = new android.util.ArrayMap<>();
        private com.android.server.sensorprivacy.SensorPrivacyStateController mSensorPrivacyStateController = com.android.server.sensorprivacy.SensorPrivacyStateController.getInstance();

        /* JADX INFO: Access modifiers changed from: private */
        class SensorUseReminderDialogInfo {
            private java.lang.String mPackageName;
            private int mTaskId;
            private android.os.UserHandle mUser;

            SensorUseReminderDialogInfo(int taskId, android.os.UserHandle user, java.lang.String packageName) {
                this.mTaskId = taskId;
                this.mUser = user;
                this.mPackageName = packageName;
            }

            public boolean equals(java.lang.Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || !(o instanceof com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.SensorUseReminderDialogInfo)) {
                    return false;
                }
                com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.SensorUseReminderDialogInfo that = (com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.SensorUseReminderDialogInfo) o;
                if (this.mTaskId == that.mTaskId && java.util.Objects.equals(this.mUser, that.mUser) && java.util.Objects.equals(this.mPackageName, that.mPackageName)) {
                    return true;
                }
                return false;
            }

            public int hashCode() {
                return java.util.Objects.hash(java.lang.Integer.valueOf(this.mTaskId), this.mUser, this.mPackageName);
            }
        }

        SensorPrivacyServiceImpl() {
            this.mHandler = com.android.server.sensorprivacy.SensorPrivacyService.this.new SensorPrivacyHandler(com.android.server.FgThread.get().getLooper(), com.android.server.sensorprivacy.SensorPrivacyService.this.mContext);
            correctStateIfNeeded();
            int[] micAndCameraOps = {27, 100, 26, 101, 121};
            com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsManager.startWatchingNoted(micAndCameraOps, this);
            com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsManager.startWatchingStarted(micAndCameraOps, this);
            com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.setToggleSensorPrivacy(((android.os.UserHandle) intent.getParcelableExtra("android.intent.extra.USER", android.os.UserHandle.class)).getIdentifier(), 5, intent.getIntExtra(android.hardware.SensorPrivacyManager.EXTRA_SENSOR, 0), false);
                    int notificationId = intent.getIntExtra(android.hardware.SensorPrivacyManager.EXTRA_NOTIFICATION_ID, 0);
                    if (notificationId != 0) {
                        com.android.server.sensorprivacy.SensorPrivacyService.this.mNotificationManager.cancel(notificationId);
                    }
                }
            }, new android.content.IntentFilter(com.android.server.sensorprivacy.SensorPrivacyService.ACTION_DISABLE_TOGGLE_SENSOR_PRIVACY), "android.permission.MANAGE_SENSOR_PRIVACY", null, 2);
            com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.registerReceiver(new com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.AnonymousClass2(com.android.server.sensorprivacy.SensorPrivacyService.this), new android.content.IntentFilter("android.intent.action.ACTION_SHUTDOWN"));
            com.android.server.sensorprivacy.SensorPrivacyService.this.mUserManagerInternal.addUserRestrictionsListener(this);
            com.android.server.sensorprivacy.SensorPrivacyStateController sensorPrivacyStateController = this.mSensorPrivacyStateController;
            com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyHandler sensorPrivacyHandler = this.mHandler;
            final com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyHandler sensorPrivacyHandler2 = this.mHandler;
            java.util.Objects.requireNonNull(sensorPrivacyHandler2);
            sensorPrivacyStateController.setAllSensorPrivacyListener(sensorPrivacyHandler, new com.android.server.sensorprivacy.SensorPrivacyStateController.AllSensorPrivacyListener() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda6
                @Override // com.android.server.sensorprivacy.SensorPrivacyStateController.AllSensorPrivacyListener
                public final void onAllSensorPrivacyChanged(boolean z) {
                    sensorPrivacyHandler2.handleSensorPrivacyChanged(z);
                }
            });
            this.mSensorPrivacyStateController.setSensorPrivacyListener(this.mHandler, new com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyListener() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda7
                @Override // com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyListener
                public final void onSensorPrivacyChanged(int i, int i2, int i3, com.android.server.sensorprivacy.SensorState sensorState) {
                    this.f$0.lambda$new$0(i, i2, i3, sensorState);
                }
            });
        }

        /* JADX INFO: renamed from: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$2, reason: invalid class name */
        class AnonymousClass2 extends android.content.BroadcastReceiver {
            final /* synthetic */ com.android.server.sensorprivacy.SensorPrivacyService val$this$0;

            AnonymousClass2(com.android.server.sensorprivacy.SensorPrivacyService sensorPrivacyService) {
                this.val$this$0 = sensorPrivacyService;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.mSensorPrivacyStateController.forEachState(new com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyStateConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$2$$ExternalSyntheticLambda0
                    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyStateConsumer
                    public final void accept(int i, int i2, int i3, com.android.server.sensorprivacy.SensorState sensorState) {
                        this.f$0.lambda$onReceive$0(i, i2, i3, sensorState);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onReceive$0(int toggleType, int userId, int sensor, com.android.server.sensorprivacy.SensorState state) {
                com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.logSensorPrivacyToggle(5, sensor, state.isEnabled(), state.getLastChange(), true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int toggleType, int userId, int sensor, com.android.server.sensorprivacy.SensorState state) {
            this.mHandler.handleSensorPrivacyChanged(userId, toggleType, sensor, state.isEnabled());
            if (com.android.internal.camera.flags.Flags.cameraPrivacyAllowlist()) {
                this.mHandler.handleSensorPrivacyChanged(userId, toggleType, sensor, state.getState());
            }
        }

        private void correctStateIfNeeded() {
            this.mSensorPrivacyStateController.forEachState(new com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyStateConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda14
                @Override // com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyStateConsumer
                public final void accept(int i, int i2, int i3, com.android.server.sensorprivacy.SensorState sensorState) {
                    this.f$0.lambda$correctStateIfNeeded$1(i, i2, i3, sensorState);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$correctStateIfNeeded$1(int type, int user, int sensor, com.android.server.sensorprivacy.SensorState state) {
            if (type == 1 && !supportsSensorToggle(1, sensor) && state.isEnabled()) {
                setToggleSensorPrivacyUnchecked(1, user, 5, sensor, false);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal.UserRestrictionsListener
        public void onUserRestrictionsChanged(int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
            if (!prevRestrictions.getBoolean("disallow_camera_toggle") && newRestrictions.getBoolean("disallow_camera_toggle")) {
                setToggleSensorPrivacyUnchecked(1, userId, 5, 2, false);
            }
            if (!prevRestrictions.getBoolean("disallow_microphone_toggle") && newRestrictions.getBoolean("disallow_microphone_toggle")) {
                setToggleSensorPrivacyUnchecked(1, userId, 5, 1, false);
            }
        }

        public void onOpStarted(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int flags, int result) {
            onOpNoted(code, uid, packageName, attributionTag, flags, result);
        }

        public void onOpNoted(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int flags, int result) {
            int sensor;
            if ((flags & 13) != 0 && result == 1) {
                if (code == 27 || code == 100 || code == 121) {
                    sensor = 1;
                } else if (code == 26 || code == 101) {
                    sensor = 2;
                } else {
                    return;
                }
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    onSensorUseStarted(uid, packageName, sensor);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        }

        private void onSensorUseStarted(int uid, java.lang.String packageName, int sensor) {
            java.lang.String inputMethodPackageName;
            android.os.UserHandle user = android.os.UserHandle.of(com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser);
            if (com.android.internal.camera.flags.Flags.cameraPrivacyAllowlist() && sensor == 2 && isAutomotive(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext)) {
                if (!isCameraPrivacyEnabled(packageName)) {
                    return;
                }
            } else if (!isCombinedToggleSensorPrivacyEnabled(sensor)) {
                return;
            }
            if (uid == 1000) {
                return;
            }
            synchronized (this.mLock) {
                if (this.mSuppressReminders.containsKey(new android.util.Pair(java.lang.Integer.valueOf(sensor), user))) {
                    android.util.Log.d(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Suppressed sensor privacy reminder for " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + user);
                    return;
                }
                java.util.List<android.app.ActivityManager.RunningTaskInfo> tasksOfPackageUsingSensor = new java.util.ArrayList<>();
                java.util.List<android.app.ActivityManager.RunningTaskInfo> tasks = com.android.server.sensorprivacy.SensorPrivacyService.this.mActivityTaskManager.getTasks(Integer.MAX_VALUE);
                int numTasks = tasks.size();
                for (int taskNum = 0; taskNum < numTasks; taskNum++) {
                    android.app.ActivityManager.RunningTaskInfo task = tasks.get(taskNum);
                    if (task.isVisible) {
                        if (task.topActivity.getPackageName().equals(packageName)) {
                            if (task.isFocused) {
                                enqueueSensorUseReminderDialogAsync(task.taskId, user, packageName, sensor);
                                return;
                            }
                            tasksOfPackageUsingSensor.add(task);
                        } else if (task.topActivity.flattenToString().equals(getSensorUseActivityName(new android.util.ArraySet<>(java.util.Arrays.asList(java.lang.Integer.valueOf(sensor))))) && task.isFocused) {
                            enqueueSensorUseReminderDialogAsync(task.taskId, user, packageName, sensor);
                        }
                    }
                }
                int taskNum2 = tasksOfPackageUsingSensor.size();
                if (taskNum2 == 1) {
                    enqueueSensorUseReminderDialogAsync(tasksOfPackageUsingSensor.get(0).taskId, user, packageName, sensor);
                    return;
                }
                if (tasksOfPackageUsingSensor.size() > 1) {
                    showSensorUseReminderNotification(user, packageName, sensor);
                    return;
                }
                java.util.List<android.app.ActivityManager.RunningServiceInfo> services = com.android.server.sensorprivacy.SensorPrivacyService.this.mActivityManager.getRunningServices(Integer.MAX_VALUE);
                int numServices = services.size();
                for (int serviceNum = 0; serviceNum < numServices; serviceNum++) {
                    android.app.ActivityManager.RunningServiceInfo service = services.get(serviceNum);
                    if (service.foreground && service.service.getPackageName().equals(packageName)) {
                        showSensorUseReminderNotification(user, packageName, sensor);
                        return;
                    }
                }
                java.lang.String inputMethodComponent = android.provider.Settings.Secure.getStringForUser(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getContentResolver(), "default_input_method", user.getIdentifier());
                if (inputMethodComponent != null && !inputMethodComponent.isEmpty()) {
                    java.lang.String inputMethodPackageName2 = android.content.ComponentName.unflattenFromString(inputMethodComponent).getPackageName();
                    inputMethodPackageName = inputMethodPackageName2;
                } else {
                    inputMethodPackageName = null;
                }
                try {
                    int capability = com.android.server.sensorprivacy.SensorPrivacyService.this.mActivityManagerInternal.getUidCapability(uid);
                    if (sensor == 1) {
                        android.service.voice.VoiceInteractionManagerInternal voiceInteractionManagerInternal = (android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class);
                        if (voiceInteractionManagerInternal != null && voiceInteractionManagerInternal.hasActiveSession(packageName)) {
                            enqueueSensorUseReminderDialogAsync(-1, user, packageName, sensor);
                            return;
                        } else if (android.text.TextUtils.equals(packageName, inputMethodPackageName) && (capability & 4) != 0) {
                            enqueueSensorUseReminderDialogAsync(-1, user, packageName, sensor);
                            return;
                        }
                    }
                    if (sensor == 2 && android.text.TextUtils.equals(packageName, inputMethodPackageName) && (capability & 2) != 0) {
                        enqueueSensorUseReminderDialogAsync(-1, user, packageName, sensor);
                    } else {
                        android.util.Log.i(com.android.server.sensorprivacy.SensorPrivacyService.TAG, packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid + " started using sensor " + sensor + " but no activity or foreground service was running. The user will not be informed. System components should check if sensor privacy is enabled for the sensor before accessing it.");
                    }
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Log.w(com.android.server.sensorprivacy.SensorPrivacyService.TAG, e);
                }
            }
        }

        private void enqueueSensorUseReminderDialogAsync(int taskId, android.os.UserHandle user, java.lang.String packageName, int sensor) {
            this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda11
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                    ((com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl) obj).enqueueSensorUseReminderDialog(((java.lang.Integer) obj2).intValue(), (android.os.UserHandle) obj3, (java.lang.String) obj4, ((java.lang.Integer) obj5).intValue());
                }
            }, this, java.lang.Integer.valueOf(taskId), user, packageName, java.lang.Integer.valueOf(sensor)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enqueueSensorUseReminderDialog(int taskId, android.os.UserHandle user, java.lang.String packageName, int sensor) {
            com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.SensorUseReminderDialogInfo info = new com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.SensorUseReminderDialogInfo(taskId, user, packageName);
            if (!this.mQueuedSensorUseReminderDialogs.containsKey(info)) {
                android.util.ArraySet<java.lang.Integer> sensors = new android.util.ArraySet<>();
                if ((sensor == 1 && this.mSuppressReminders.containsKey(new android.util.Pair(2, user))) || (sensor == 2 && this.mSuppressReminders.containsKey(new android.util.Pair(1, user)))) {
                    sensors.add(1);
                    sensors.add(2);
                } else {
                    sensors.add(java.lang.Integer.valueOf(sensor));
                }
                this.mQueuedSensorUseReminderDialogs.put(info, sensors);
                this.mHandler.sendMessageDelayed(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda1
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl) obj).showSensorUserReminderDialog((com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.SensorUseReminderDialogInfo) obj2);
                    }
                }, this, info), 500L);
                return;
            }
            this.mQueuedSensorUseReminderDialogs.get(info).add(java.lang.Integer.valueOf(sensor));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showSensorUserReminderDialog(com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.SensorUseReminderDialogInfo info) {
            android.util.ArraySet<java.lang.Integer> sensors = this.mQueuedSensorUseReminderDialogs.get(info);
            this.mQueuedSensorUseReminderDialogs.remove(info);
            if (sensors == null) {
                android.util.Log.e(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Unable to show sensor use dialog because sensor set is null. Was the dialog queue modified from outside the handler thread?");
                return;
            }
            android.content.Intent dialogIntent = new android.content.Intent();
            dialogIntent.setComponent(android.content.ComponentName.unflattenFromString(getSensorUseActivityName(sensors)));
            android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic();
            options.setLaunchTaskId(info.mTaskId);
            options.setTaskOverlay(true, true);
            dialogIntent.addFlags(8650752);
            dialogIntent.putExtra("android.intent.extra.PACKAGE_NAME", info.mPackageName);
            if (sensors.size() == 1) {
                dialogIntent.putExtra(android.hardware.SensorPrivacyManager.EXTRA_SENSOR, sensors.valueAt(0));
            } else if (sensors.size() == 2) {
                dialogIntent.putExtra(android.hardware.SensorPrivacyManager.EXTRA_ALL_SENSORS, true);
            } else {
                android.util.Log.e(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Attempted to show sensor use dialog for " + sensors.size() + " sensors");
                return;
            }
            boolean skip = false;
            for (java.lang.Integer sensor : sensors) {
                if (com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceExt.notifySystemUI(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext, sensor.intValue())) {
                    android.util.Log.i(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "StealthSecurityMode systemui show float tips, sensor:" + sensor);
                    skip = true;
                }
            }
            if (skip) {
                return;
            }
            com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.startActivityAsUser(dialogIntent, options.toBundle(), android.os.UserHandle.SYSTEM);
        }

        private java.lang.String getSensorUseActivityName(android.util.ArraySet<java.lang.Integer> sensors) {
            for (java.lang.Integer sensor : sensors) {
                if (isToggleSensorPrivacyEnabled(2, sensor.intValue())) {
                    return com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getResources().getString(android.R.string.config_signalAttributionPath);
                }
            }
            return com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getResources().getString(android.R.string.config_sharedConnectivityServicePackage);
        }

        private void showSensorUseReminderNotification(android.os.UserHandle user, java.lang.String packageName, int sensor) {
            int iconRes;
            int messageRes;
            int notificationId;
            int notificationId2;
            long j;
            if (com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceExt.notifySystemUI(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext, sensor)) {
                android.util.Log.i(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "StealthSecurityMode don't show Sensor Notification");
                return;
            }
            try {
                java.lang.CharSequence packageLabel = com.android.server.sensorprivacy.SensorPrivacyService.this.getUiContext().getPackageManager().getApplicationInfoAsUser(packageName, 0, user).loadLabel(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getPackageManager());
                if (sensor == 1) {
                    iconRes = 17302853;
                    messageRes = 17041763;
                    notificationId = 65;
                } else {
                    iconRes = 17302434;
                    messageRes = 17041761;
                    notificationId = 66;
                }
                android.app.NotificationChannel channel = new android.app.NotificationChannel(com.android.server.sensorprivacy.SensorPrivacyService.SENSOR_PRIVACY_CHANNEL_ID, com.android.server.sensorprivacy.SensorPrivacyService.this.getUiContext().getString(android.R.string.satellite_notification_how_it_works), 4);
                channel.setSound(null, null);
                channel.setBypassDnd(true);
                channel.enableVibration(false);
                channel.setBlockable(false);
                com.android.server.sensorprivacy.SensorPrivacyService.this.mNotificationManager.createNotificationChannel(channel);
                android.graphics.drawable.Icon icon = android.graphics.drawable.Icon.createWithResource(com.android.server.sensorprivacy.SensorPrivacyService.this.getUiContext().getResources(), iconRes);
                java.lang.String contentTitle = com.android.server.sensorprivacy.SensorPrivacyService.this.getUiContext().getString(messageRes);
                android.text.Spanned contentText = android.text.Html.fromHtml(com.android.server.sensorprivacy.SensorPrivacyService.this.getUiContext().getString(android.R.string.satellite_notification_summary, packageLabel), 0);
                android.safetycenter.SafetyCenterManager safetyCenterManager = (android.safetycenter.SafetyCenterManager) com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getSystemService(android.safetycenter.SafetyCenterManager.class);
                java.lang.String action = safetyCenterManager.isSafetyCenterEnabled() ? "android.settings.PRIVACY_CONTROLS" : "android.settings.PRIVACY_SETTINGS";
                android.os.UserHandle currentUserHandle = new android.os.UserHandle(com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser);
                android.app.PendingIntent contentIntent = android.app.PendingIntent.getActivityAsUser(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext, sensor, new android.content.Intent(action), android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, null, currentUserHandle);
                java.lang.String actionTitle = com.android.server.sensorprivacy.SensorPrivacyService.this.getUiContext().getString(android.R.string.satellite_notification_manual_title);
                android.app.PendingIntent actionIntent = android.app.PendingIntent.getBroadcast(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext, (com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser * 3) + sensor, new android.content.Intent(com.android.server.sensorprivacy.SensorPrivacyService.ACTION_DISABLE_TOGGLE_SENSOR_PRIVACY).setPackage(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getPackageName()).putExtra(android.hardware.SensorPrivacyManager.EXTRA_SENSOR, sensor).putExtra(android.hardware.SensorPrivacyManager.EXTRA_NOTIFICATION_ID, notificationId).putExtra("android.intent.extra.USER", user), android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
                android.app.NotificationManager notificationManager = com.android.server.sensorprivacy.SensorPrivacyService.this.mNotificationManager;
                android.app.Notification.Builder builderExtend = new android.app.Notification.Builder(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext, com.android.server.sensorprivacy.SensorPrivacyService.SENSOR_PRIVACY_CHANNEL_ID).setContentTitle(contentTitle).setContentText(contentText).setSmallIcon(icon).addAction(new android.app.Notification.Action.Builder(icon, actionTitle, actionIntent).build()).setContentIntent(contentIntent).extend(new android.app.Notification.TvExtender());
                if (isTelevision(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext)) {
                    notificationId2 = notificationId;
                    j = 1;
                } else {
                    notificationId2 = notificationId;
                    j = 0;
                }
                notificationManager.notifyAsUser(null, notificationId2, builderExtend.setTimeoutAfter(j).build(), currentUserHandle);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.e(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Cannot show sensor use notification for " + packageName);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showSensorStateChangedActivity(int sensor, int toggleType) {
            java.lang.String activityName = com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getResources().getString(android.R.string.config_sharedConnectivityServiceIntentAction);
            if (android.text.TextUtils.isEmpty(activityName)) {
                return;
            }
            android.content.Intent dialogIntent = new android.content.Intent();
            dialogIntent.setComponent(android.content.ComponentName.unflattenFromString(activityName));
            android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic();
            options.setTaskOverlay(true, true);
            dialogIntent.addFlags(8650752);
            dialogIntent.putExtra(android.hardware.SensorPrivacyManager.EXTRA_SENSOR, sensor);
            dialogIntent.putExtra(android.hardware.SensorPrivacyManager.EXTRA_TOGGLE_TYPE, toggleType);
            com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.startActivityAsUser(dialogIntent, options.toBundle(), android.os.UserHandle.SYSTEM);
        }

        private boolean isTelevision(android.content.Context context) {
            int uiMode = context.getResources().getConfiguration().uiMode;
            return (uiMode & 15) == 4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isAutomotive(android.content.Context context) {
            int uiMode = context.getResources().getConfiguration().uiMode;
            return (uiMode & 15) == 3;
        }

        public void setSensorPrivacy(boolean enable) {
            enforceManageSensorPrivacyPermission();
            this.mSensorPrivacyStateController.setAllSensorState(enable);
        }

        public void setToggleSensorPrivacy(int userId, int source, int sensor, boolean enable) {
            enforceManageSensorPrivacyPermission();
            if (userId == -2) {
                userId = com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser;
            }
            if (!canChangeToggleSensorPrivacy(userId, sensor) && !com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceExt.canSkipSetCheckForStealthMode(android.os.Binder.getCallingPid())) {
                return;
            }
            if (enable && !supportsSensorToggle(1, sensor)) {
                return;
            }
            setToggleSensorPrivacyUnchecked(1, userId, source, sensor, enable);
        }

        public void setToggleSensorPrivacyState(int userId, int source, int sensor, int state) {
            enforceManageSensorPrivacyPermission();
            if (userId == -2) {
                userId = com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser;
            }
            if (!canChangeToggleSensorPrivacy(userId, sensor) || !supportsSensorToggle(1, sensor)) {
                return;
            }
            setToggleSensorPrivacyStateUnchecked(1, userId, source, sensor, state);
        }

        private void setToggleSensorPrivacyStateUnchecked(final int toggleType, final int userId, final int source, final int sensor, final int state) {
            final long[] lastChange = new long[1];
            this.mSensorPrivacyStateController.atomic(new java.lang.Runnable() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setToggleSensorPrivacyStateUnchecked$3(toggleType, userId, sensor, lastChange, state, source);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setToggleSensorPrivacyStateUnchecked$3(int toggleType, final int userId, final int sensor, final long[] lastChange, final int state, final int source) {
            com.android.server.sensorprivacy.SensorState sensorState = this.mSensorPrivacyStateController.getState(toggleType, userId, sensor);
            lastChange[0] = sensorState.getLastChange();
            this.mSensorPrivacyStateController.setState(toggleType, userId, sensor, state, this.mHandler, new com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda9
                @Override // com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback
                public final void callback(boolean z) {
                    this.f$0.lambda$setToggleSensorPrivacyStateUnchecked$2(userId, source, sensor, state, lastChange, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setToggleSensorPrivacyStateUnchecked$2(int userId, int source, int sensor, int state, long[] lastChange, boolean changeSuccessful) {
            if (changeSuccessful && userId == com.android.server.sensorprivacy.SensorPrivacyService.this.mUserManagerInternal.getProfileParentId(userId)) {
                this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.HexConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda8
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) {
                        ((com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl) obj).logSensorPrivacyStateToggle(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), ((java.lang.Long) obj5).longValue(), ((java.lang.Boolean) obj6).booleanValue());
                    }
                }, this, java.lang.Integer.valueOf(source), java.lang.Integer.valueOf(sensor), java.lang.Integer.valueOf(state), java.lang.Long.valueOf(lastChange[0]), false));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void logSensorPrivacyStateToggle(int source, int sensor, int state, long lastChange, boolean onShutDown) {
            int logSensor;
            int logSource;
            long logMins = java.lang.Math.max(0L, (com.android.server.sensorprivacy.SensorPrivacyService.getCurrentTimeMillis() - lastChange) / 60000);
            int logAction = 0;
            if (!onShutDown) {
                switch (state) {
                    case 1:
                        logAction = 2;
                        break;
                    case 2:
                        logAction = 1;
                        break;
                    case 3:
                        logAction = 3;
                        break;
                    default:
                        logAction = 0;
                        break;
                }
            }
            switch (sensor) {
                case 1:
                    logSensor = 1;
                    break;
                case 2:
                    logSensor = 2;
                    break;
                default:
                    logSensor = 0;
                    break;
            }
            switch (source) {
                case 1:
                    logSource = 3;
                    break;
                case 2:
                    logSource = 2;
                    break;
                case 3:
                    logSource = 1;
                    break;
                default:
                    logSource = 0;
                    break;
            }
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PRIVACY_SENSOR_TOGGLE_INTERACTION, logSensor, logAction, logSource, logMins);
        }

        public void setToggleSensorPrivacyStateForProfileGroup(int userId, final int source, final int sensor, final int state) {
            enforceManageSensorPrivacyPermission();
            if (userId == -2) {
                userId = com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser;
            }
            final int parentId = com.android.server.sensorprivacy.SensorPrivacyService.this.mUserManagerInternal.getProfileParentId(userId);
            com.android.server.sensorprivacy.SensorPrivacyService.this.forAllUsers(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda13
                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$setToggleSensorPrivacyStateForProfileGroup$4(parentId, source, sensor, state, (java.lang.Integer) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setToggleSensorPrivacyStateForProfileGroup$4(int parentId, int source, int sensor, int state, java.lang.Integer userId2) throws java.lang.Exception {
            if (parentId == com.android.server.sensorprivacy.SensorPrivacyService.this.mUserManagerInternal.getProfileParentId(userId2.intValue())) {
                setToggleSensorPrivacyState(userId2.intValue(), source, sensor, state);
            }
        }

        public java.util.List<java.lang.String> getCameraPrivacyAllowlist() {
            enforceObserveSensorPrivacyPermission();
            return com.android.server.sensorprivacy.SensorPrivacyService.this.mCameraPrivacyAllowlist;
        }

        public void setCameraPrivacyAllowlist(java.util.List<java.lang.String> allowlist) {
            enforceManageSensorPrivacyPermission();
            com.android.server.sensorprivacy.SensorPrivacyService.this.mCameraPrivacyAllowlist = new java.util.ArrayList(allowlist);
        }

        public boolean isCameraPrivacyEnabled(java.lang.String packageName) {
            enforceObserveSensorPrivacyPermission();
            int state = this.mSensorPrivacyStateController.getState(1, com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser, 2).getState();
            if (state == 1) {
                return true;
            }
            if (state == 2 || state != 3) {
                return false;
            }
            for (java.lang.String entry : com.android.server.sensorprivacy.SensorPrivacyService.this.mCameraPrivacyAllowlist) {
                if (packageName.equals(entry)) {
                    return false;
                }
            }
            return true;
        }

        public int getToggleSensorPrivacyState(int toggleType, int sensor) {
            enforceObserveSensorPrivacyPermission();
            return this.mSensorPrivacyStateController.getState(toggleType, com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser, sensor).getState();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setToggleSensorPrivacyUnchecked(final int toggleType, final int userId, final int source, final int sensor, final boolean enable) {
            final long[] lastChange = new long[1];
            this.mSensorPrivacyStateController.atomic(new java.lang.Runnable() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setToggleSensorPrivacyUnchecked$6(toggleType, userId, sensor, lastChange, enable, source);
                }
            });
            com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceExt.disappearNotification(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.createContextAsUser(new android.os.UserHandle(userId), 0), enable, sensor == 1 ? 65 : 66);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setToggleSensorPrivacyUnchecked$6(int toggleType, final int userId, final int sensor, final long[] lastChange, final boolean enable, final int source) {
            com.android.server.sensorprivacy.SensorState sensorState = this.mSensorPrivacyStateController.getState(toggleType, userId, sensor);
            lastChange[0] = sensorState.getLastChange();
            this.mSensorPrivacyStateController.setState(toggleType, userId, sensor, enable, this.mHandler, new com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda0
                @Override // com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback
                public final void callback(boolean z) {
                    this.f$0.lambda$setToggleSensorPrivacyUnchecked$5(userId, source, sensor, enable, lastChange, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setToggleSensorPrivacyUnchecked$5(int userId, int source, int sensor, boolean enable, long[] lastChange, boolean changeSuccessful) {
            if (changeSuccessful && userId == com.android.server.sensorprivacy.SensorPrivacyService.this.mUserManagerInternal.getProfileParentId(userId)) {
                this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.HexConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda12
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) {
                        ((com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl) obj).logSensorPrivacyToggle(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Boolean) obj4).booleanValue(), ((java.lang.Long) obj5).longValue(), ((java.lang.Boolean) obj6).booleanValue());
                    }
                }, this, java.lang.Integer.valueOf(source), java.lang.Integer.valueOf(sensor), java.lang.Boolean.valueOf(enable), java.lang.Long.valueOf(lastChange[0]), false));
            }
        }

        private boolean canChangeToggleSensorPrivacy(int userId, int sensor) {
            if (sensor == 1 && com.android.server.sensorprivacy.SensorPrivacyService.this.mCallStateHelper.isInEmergencyCall()) {
                android.util.Log.i(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Can't change mic toggle during an emergency call");
                return false;
            }
            if (requiresAuthentication() && com.android.server.sensorprivacy.SensorPrivacyService.this.mKeyguardManager != null && com.android.server.sensorprivacy.SensorPrivacyService.this.mKeyguardManager.isDeviceLocked(userId)) {
                android.util.Log.i(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Can't change mic/cam toggle while device is locked");
                return false;
            }
            if (sensor == 1 && com.android.server.sensorprivacy.SensorPrivacyService.this.mUserManagerInternal.getUserRestriction(userId, "disallow_microphone_toggle")) {
                android.util.Log.i(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Can't change mic toggle due to admin restriction");
                return false;
            }
            if (sensor != 2 || !com.android.server.sensorprivacy.SensorPrivacyService.this.mUserManagerInternal.getUserRestriction(userId, "disallow_camera_toggle")) {
                return true;
            }
            android.util.Log.i(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Can't change camera toggle due to admin restriction");
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void logSensorPrivacyToggle(int source, int sensor, boolean enabled, long lastChange, boolean onShutDown) {
            int logAction;
            int logSensor;
            int logSource;
            long logMins = java.lang.Math.max(0L, (com.android.server.sensorprivacy.SensorPrivacyService.getCurrentTimeMillis() - lastChange) / 60000);
            if (onShutDown) {
                if (enabled) {
                    logAction = 0;
                } else {
                    logAction = 0;
                }
            } else if (enabled) {
                logAction = 2;
            } else {
                logAction = 1;
            }
            switch (sensor) {
                case 1:
                    logSensor = 1;
                    break;
                case 2:
                    logSensor = 2;
                    break;
                default:
                    logSensor = 0;
                    break;
            }
            switch (source) {
                case 1:
                    logSource = 3;
                    break;
                case 2:
                    logSource = 2;
                    break;
                case 3:
                    logSource = 1;
                    break;
                default:
                    logSource = 0;
                    break;
            }
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PRIVACY_SENSOR_TOGGLE_INTERACTION, logSensor, logAction, logSource, logMins);
        }

        public void setToggleSensorPrivacyForProfileGroup(int userId, final int source, final int sensor, final boolean enable) {
            enforceManageSensorPrivacyPermission();
            if (userId == -2) {
                userId = com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser;
            }
            final int parentId = com.android.server.sensorprivacy.SensorPrivacyService.this.mUserManagerInternal.getProfileParentId(userId);
            com.android.server.sensorprivacy.SensorPrivacyService.this.forAllUsers(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda10
                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$setToggleSensorPrivacyForProfileGroup$7(parentId, source, sensor, enable, (java.lang.Integer) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setToggleSensorPrivacyForProfileGroup$7(int parentId, int source, int sensor, boolean enable, java.lang.Integer userId2) throws java.lang.Exception {
            if (parentId == com.android.server.sensorprivacy.SensorPrivacyService.this.mUserManagerInternal.getProfileParentId(userId2.intValue())) {
                setToggleSensorPrivacy(userId2.intValue(), source, sensor, enable);
            }
        }

        private void enforceManageSensorPrivacyPermission() {
            if (com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_SENSOR_PRIVACY") == 0) {
            } else {
                throw new java.lang.SecurityException("Changing sensor privacy requires the following permission: android.permission.MANAGE_SENSOR_PRIVACY");
            }
        }

        private void enforceObserveSensorPrivacyPermission() {
            java.lang.String systemUIPackage = com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getString(android.R.string.config_systemUi);
            int systemUIAppId = android.os.UserHandle.getAppId(com.android.server.sensorprivacy.SensorPrivacyService.this.mPackageManagerInternal.getPackageUid(systemUIPackage, 1048576L, 0));
            if (android.os.UserHandle.getCallingAppId() == systemUIAppId || com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.checkCallingOrSelfPermission("android.permission.OBSERVE_SENSOR_PRIVACY") == 0) {
            } else {
                throw new java.lang.SecurityException("Observing sensor privacy changes requires the following permission: android.permission.OBSERVE_SENSOR_PRIVACY");
            }
        }

        public boolean isSensorPrivacyEnabled() {
            enforceObserveSensorPrivacyPermission();
            return this.mSensorPrivacyStateController.getAllSensorState();
        }

        public boolean isToggleSensorPrivacyEnabled(int toggleType, int sensor) {
            enforceObserveSensorPrivacyPermission();
            return this.mSensorPrivacyStateController.getState(toggleType, com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser, sensor).isEnabled();
        }

        public boolean isCombinedToggleSensorPrivacyEnabled(int sensor) {
            return isToggleSensorPrivacyEnabled(1, sensor) || isToggleSensorPrivacyEnabled(2, sensor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isToggleSensorPrivacyEnabledInternal(int userId, int toggleType, int sensor) {
            return this.mSensorPrivacyStateController.getState(toggleType, userId, sensor).isEnabled();
        }

        public boolean supportsSensorToggle(int toggleType, int sensor) {
            if (toggleType == 1) {
                if (sensor == 1) {
                    return com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getResources().getBoolean(android.R.bool.config_sms_ask_every_time_support);
                }
                if (sensor == 2) {
                    return com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getResources().getBoolean(android.R.bool.config_skipScreenOnBrightnessRamp);
                }
            } else if (toggleType == 2) {
                if (sensor == 1) {
                    return com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getResources().getBoolean(android.R.bool.config_smma_notification_supported_over_ims);
                }
                if (sensor == 2) {
                    return com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getResources().getBoolean(android.R.bool.config_smart_battery_available);
                }
            }
            throw new java.lang.IllegalArgumentException("Invalid arguments. toggleType=" + toggleType + " sensor=" + sensor);
        }

        public void addSensorPrivacyListener(android.hardware.ISensorPrivacyListener listener) {
            enforceObserveSensorPrivacyPermission();
            if (listener == null) {
                throw new java.lang.NullPointerException("listener cannot be null");
            }
            this.mHandler.addListener(listener);
        }

        public void addToggleSensorPrivacyListener(android.hardware.ISensorPrivacyListener listener) {
            enforceObserveSensorPrivacyPermission();
            if (listener == null) {
                throw new java.lang.IllegalArgumentException("listener cannot be null");
            }
            this.mHandler.addToggleListener(listener);
        }

        public void removeSensorPrivacyListener(android.hardware.ISensorPrivacyListener listener) {
            enforceObserveSensorPrivacyPermission();
            if (listener == null) {
                throw new java.lang.NullPointerException("listener cannot be null");
            }
            this.mHandler.removeListener(listener);
        }

        public void removeToggleSensorPrivacyListener(android.hardware.ISensorPrivacyListener listener) {
            enforceObserveSensorPrivacyPermission();
            if (listener == null) {
                throw new java.lang.IllegalArgumentException("listener cannot be null");
            }
            this.mHandler.removeToggleListener(listener);
        }

        public void suppressToggleSensorPrivacyReminders(int userId, int sensor, android.os.IBinder token, boolean suppress) {
            enforceManageSensorPrivacyPermission();
            if (userId == -2) {
                userId = com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser;
            }
            java.util.Objects.requireNonNull(token);
            android.util.Pair<java.lang.Integer, android.os.UserHandle> key = new android.util.Pair<>(java.lang.Integer.valueOf(sensor), android.os.UserHandle.of(userId));
            synchronized (this.mLock) {
                if (suppress) {
                    try {
                        token.linkToDeath(this, 0);
                        java.util.ArrayList<android.os.IBinder> suppressPackageReminderTokens = this.mSuppressReminders.get(key);
                        if (suppressPackageReminderTokens == null) {
                            suppressPackageReminderTokens = new java.util.ArrayList<>(1);
                            this.mSuppressReminders.put(key, suppressPackageReminderTokens);
                        }
                        suppressPackageReminderTokens.add(token);
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Could not suppress sensor use reminder", e);
                    }
                } else {
                    this.mHandler.removeSuppressPackageReminderToken(key, token);
                }
            }
        }

        public boolean requiresAuthentication() {
            enforceObserveSensorPrivacyPermission();
            return com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getResources().getBoolean(android.R.bool.config_preferenceFragmentClipToPadding);
        }

        public void showSensorUseDialog(int sensor) {
            if (android.os.Binder.getCallingUid() != 1000) {
                throw new java.lang.SecurityException("Can only be called by the system uid");
            }
            if (!isCombinedToggleSensorPrivacyEnabled(sensor)) {
                return;
            }
            enqueueSensorUseReminderDialogAsync(-1, android.os.UserHandle.of(com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser), com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, sensor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void userSwitching(final int from, final int to) {
            int i;
            final boolean[] micState = new boolean[2];
            final boolean[] camState = new boolean[2];
            final boolean[] prevMicState = new boolean[2];
            final boolean[] prevCamState = new boolean[2];
            this.mSensorPrivacyStateController.atomic(new java.lang.Runnable() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$userSwitching$8(prevMicState, from, prevCamState, micState, to, camState);
                }
            });
            this.mSensorPrivacyStateController.atomic(new java.lang.Runnable() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$userSwitching$9(prevMicState, from, prevCamState, micState, to, camState);
                }
            });
            if (from != -10000 && prevMicState[0] == micState[0] && prevMicState[1] == micState[1]) {
                i = to;
            } else {
                i = to;
                this.mHandler.handleSensorPrivacyChanged(i, 1, 1, micState[0]);
                this.mHandler.handleSensorPrivacyChanged(i, 2, 1, micState[1]);
                setGlobalRestriction(1, micState[0] || micState[1]);
            }
            if (from == -10000 || prevCamState[0] != camState[0] || prevCamState[1] != camState[1]) {
                this.mHandler.handleSensorPrivacyChanged(i, 1, 2, camState[0]);
                this.mHandler.handleSensorPrivacyChanged(i, 2, 2, camState[1]);
                setGlobalRestriction(2, camState[0] || camState[1]);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$userSwitching$8(boolean[] prevMicState, int from, boolean[] prevCamState, boolean[] micState, int to, boolean[] camState) {
            prevMicState[0] = isToggleSensorPrivacyEnabledInternal(from, 1, 1);
            prevCamState[0] = isToggleSensorPrivacyEnabledInternal(from, 1, 2);
            micState[0] = isToggleSensorPrivacyEnabledInternal(to, 1, 1);
            camState[0] = isToggleSensorPrivacyEnabledInternal(to, 1, 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$userSwitching$9(boolean[] prevMicState, int from, boolean[] prevCamState, boolean[] micState, int to, boolean[] camState) {
            prevMicState[1] = isToggleSensorPrivacyEnabledInternal(from, 2, 1);
            prevCamState[1] = isToggleSensorPrivacyEnabledInternal(from, 2, 2);
            micState[1] = isToggleSensorPrivacyEnabledInternal(to, 2, 1);
            camState[1] = isToggleSensorPrivacyEnabledInternal(to, 2, 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setGlobalRestriction(int sensor, boolean enabled) {
            switch (sensor) {
                case 1:
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsManagerInternal.setGlobalRestriction(27, enabled, com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsRestrictionToken);
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsManagerInternal.setGlobalRestriction(136, enabled, com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsRestrictionToken);
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsManagerInternal.setGlobalRestriction(100, enabled, com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsRestrictionToken);
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsManagerInternal.setGlobalRestriction(120, enabled, com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsRestrictionToken);
                    boolean allowed = android.provider.Settings.Global.getInt(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getContentResolver(), "receive_explicit_user_interaction_audio_enabled", 1) == 1;
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsManagerInternal.setGlobalRestriction(121, enabled && !allowed, com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsRestrictionToken);
                    break;
                case 2:
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsManagerInternal.setGlobalRestriction(26, enabled, com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsRestrictionToken);
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsManagerInternal.setGlobalRestriction(101, enabled, com.android.server.sensorprivacy.SensorPrivacyService.this.mAppOpsRestrictionToken);
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeSuppressPackageReminderToken(android.util.Pair<java.lang.Integer, android.os.UserHandle> key, android.os.IBinder token) {
            synchronized (this.mLock) {
                java.util.ArrayList<android.os.IBinder> suppressPackageReminderTokens = this.mSuppressReminders.get(key);
                if (suppressPackageReminderTokens == null) {
                    android.util.Log.e(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "No tokens for " + key);
                    return;
                }
                boolean wasRemoved = suppressPackageReminderTokens.remove(token);
                if (wasRemoved) {
                    token.unlinkToDeath(this, 0);
                    if (suppressPackageReminderTokens.isEmpty()) {
                        this.mSuppressReminders.remove(key);
                    }
                } else {
                    android.util.Log.w(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Could not remove sensor use reminder suppression token " + token + " from " + key);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void registerSettingsObserver() {
            com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("receive_explicit_user_interaction_audio_enabled"), false, new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.3
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange) {
                    com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.setGlobalRestriction(1, com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.isCombinedToggleSensorPrivacyEnabled(1));
                }
            });
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied(android.os.IBinder token) {
            synchronized (this.mLock) {
                for (android.util.Pair<java.lang.Integer, android.os.UserHandle> key : this.mSuppressReminders.keySet()) {
                    removeSuppressPackageReminderToken(key, token);
                }
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            java.lang.String opt;
            java.util.Objects.requireNonNull(fd);
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext, com.android.server.sensorprivacy.SensorPrivacyService.TAG, pw)) {
                int opti = 0;
                boolean dumpAsProto = false;
                while (opti < args.length && (opt = args[opti]) != null && opt.length() > 0 && opt.charAt(0) == '-') {
                    opti++;
                    if ("--proto".equals(opt)) {
                        dumpAsProto = true;
                    } else {
                        pw.println("Unknown argument: " + opt + "; use -h for help");
                    }
                }
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    if (dumpAsProto) {
                        this.mSensorPrivacyStateController.dump(new com.android.internal.util.dump.DualDumpOutputStream(new android.util.proto.ProtoOutputStream(fd)));
                    } else {
                        pw.println("SENSOR PRIVACY MANAGER STATE (dumpsys sensor_privacy)");
                        this.mSensorPrivacyStateController.dump(new com.android.internal.util.dump.DualDumpOutputStream(new android.util.IndentingPrintWriter(pw, "  ")));
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0022  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int sensorStrToId(java.lang.String r4) {
            /*
                r3 = this;
                r0 = 0
                if (r4 != 0) goto L4
                return r0
            L4:
                int r1 = r4.hashCode()
                r2 = 1
                switch(r1) {
                    case -1367751899: goto L18;
                    case 1370921258: goto Ld;
                    default: goto Lc;
                }
            Lc:
                goto L22
            Ld:
                java.lang.String r1 = "microphone"
                boolean r1 = r4.equals(r1)
                if (r1 == 0) goto Lc
                r1 = r0
                goto L23
            L18:
                java.lang.String r1 = "camera"
                boolean r1 = r4.equals(r1)
                if (r1 == 0) goto Lc
                r1 = r2
                goto L23
            L22:
                r1 = -1
            L23:
                switch(r1) {
                    case 0: goto L29;
                    case 1: goto L27;
                    default: goto L26;
                }
            L26:
                return r0
            L27:
                r0 = 2
                return r0
            L29:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.sensorStrToId(java.lang.String):int");
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyServiceImpl$4] */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new android.os.ShellCommand() { // from class: com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.4
                public int onCommand(java.lang.String cmd) {
                    byte b;
                    if (cmd == null) {
                        return handleDefaultCommands(cmd);
                    }
                    int userId = java.lang.Integer.parseInt(getNextArgRequired());
                    java.io.PrintWriter pw = getOutPrintWriter();
                    switch (cmd.hashCode()) {
                        case -1298848381:
                            b = !cmd.equals(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE) ? (byte) -1 : (byte) 0;
                            break;
                        case 553634933:
                            b = !cmd.equals("enable_except_allowlisted_apps") ? (byte) -1 : (byte) 2;
                            break;
                        case 1671308008:
                            b = !cmd.equals("disable") ? (byte) -1 : (byte) 1;
                            break;
                        default:
                            b = -1;
                            break;
                    }
                    switch (b) {
                        case 0:
                            int sensor = com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.sensorStrToId(getNextArgRequired());
                            if (sensor == 0) {
                                pw.println("Invalid sensor");
                                return -1;
                            }
                            com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.setToggleSensorPrivacy(userId, 4, sensor, true);
                            return 0;
                        case 1:
                            int sensor2 = com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.sensorStrToId(getNextArgRequired());
                            if (sensor2 == 0) {
                                pw.println("Invalid sensor");
                                return -1;
                            }
                            com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.setToggleSensorPrivacy(userId, 4, sensor2, false);
                            return 0;
                        case 2:
                            if (com.android.internal.camera.flags.Flags.cameraPrivacyAllowlist()) {
                                int sensor3 = com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.sensorStrToId(getNextArgRequired());
                                if (!com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.isAutomotive(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext) || sensor3 != 2) {
                                    pw.println("Command not valid for this sensor");
                                    return -1;
                                }
                                com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.setToggleSensorPrivacyState(userId, 4, sensor3, 3);
                            }
                            return 0;
                        default:
                            return handleDefaultCommands(cmd);
                    }
                }

                public void onHelp() {
                    java.io.PrintWriter pw = getOutPrintWriter();
                    pw.println("Sensor privacy manager (sensor_privacy) commands:");
                    pw.println("  help");
                    pw.println("    Print this help text.");
                    pw.println("");
                    pw.println("  enable USER_ID SENSOR");
                    pw.println("    Enable privacy for a certain sensor.");
                    pw.println("");
                    pw.println("  disable USER_ID SENSOR");
                    pw.println("    Disable privacy for a certain sensor.");
                    pw.println("");
                    if (com.android.internal.camera.flags.Flags.cameraPrivacyAllowlist() && com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl.this.isAutomotive(com.android.server.sensorprivacy.SensorPrivacyService.this.mContext)) {
                        pw.println("  enable_except_allowlisted_apps USER_ID SENSOR");
                        pw.println("    Enable privacy except for automotive apps which are required by OEM.");
                        pw.println("");
                    }
                }
            }.exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class SensorPrivacyHandler extends android.os.Handler {
        private static final int MESSAGE_SENSOR_PRIVACY_CHANGED = 1;
        private final android.content.Context mContext;
        private final android.util.ArrayMap<android.hardware.ISensorPrivacyListener, android.util.Pair<com.android.server.sensorprivacy.SensorPrivacyService.DeathRecipient, java.lang.Integer>> mDeathRecipients;
        private final java.lang.Object mListenerLock;
        private final android.os.RemoteCallbackList<android.hardware.ISensorPrivacyListener> mListeners;
        private final android.os.RemoteCallbackList<android.hardware.ISensorPrivacyListener> mToggleSensorListeners;

        SensorPrivacyHandler(android.os.Looper looper, android.content.Context context) {
            super(looper);
            this.mListenerLock = new java.lang.Object();
            this.mListeners = new android.os.RemoteCallbackList<android.hardware.ISensorPrivacyListener>() { // from class: com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyHandler.1
                @Override // android.os.RemoteCallbackList
                public void onCallbackDied(android.hardware.ISensorPrivacyListener callback) {
                    synchronized (com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyHandler.this.mListenerLock) {
                        com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyHandler.this.mDeathRecipients.remove(callback);
                    }
                }
            };
            this.mToggleSensorListeners = new android.os.RemoteCallbackList<android.hardware.ISensorPrivacyListener>() { // from class: com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyHandler.2
                @Override // android.os.RemoteCallbackList
                public void onCallbackDied(android.hardware.ISensorPrivacyListener callback) {
                    synchronized (com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyHandler.this.mListenerLock) {
                        com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyHandler.this.mDeathRecipients.remove(callback);
                    }
                }
            };
            this.mDeathRecipients = new android.util.ArrayMap<>();
            this.mContext = context;
        }

        public void addListener(android.hardware.ISensorPrivacyListener listener) {
            synchronized (this.mListenerLock) {
                if (this.mListeners.register(listener)) {
                    addDeathRecipient(listener);
                }
            }
        }

        public void addToggleListener(android.hardware.ISensorPrivacyListener listener) {
            synchronized (this.mListenerLock) {
                if (this.mToggleSensorListeners.register(listener)) {
                    addDeathRecipient(listener);
                }
            }
        }

        public void removeListener(android.hardware.ISensorPrivacyListener listener) {
            synchronized (this.mListenerLock) {
                if (this.mListeners.unregister(listener)) {
                    removeDeathRecipient(listener);
                }
            }
        }

        public void removeToggleListener(android.hardware.ISensorPrivacyListener listener) {
            synchronized (this.mListenerLock) {
                if (this.mToggleSensorListeners.unregister(listener)) {
                    removeDeathRecipient(listener);
                }
            }
        }

        public void handleSensorPrivacyChanged(boolean enabled) {
            int count = this.mListeners.beginBroadcast();
            for (int i = 0; i < count; i++) {
                android.hardware.ISensorPrivacyListener listener = this.mListeners.getBroadcastItem(i);
                try {
                    listener.onSensorPrivacyChanged(-1, -1, enabled);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Caught an exception notifying listener " + listener + ": ", e);
                }
            }
            this.mListeners.finishBroadcast();
        }

        public void handleSensorPrivacyChanged(int userId, int toggleType, int sensor, boolean enabled) {
            com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyManagerInternal.dispatch(userId, sensor, enabled);
            if (userId == com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser) {
                com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.setGlobalRestriction(sensor, com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.isCombinedToggleSensorPrivacyEnabled(sensor));
            }
            if (userId != com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser) {
                return;
            }
            synchronized (this.mListenerLock) {
                try {
                    int count = this.mToggleSensorListeners.beginBroadcast();
                    for (int i = 0; i < count; i++) {
                        android.hardware.ISensorPrivacyListener listener = this.mToggleSensorListeners.getBroadcastItem(i);
                        try {
                            listener.onSensorPrivacyChanged(toggleType, sensor, enabled);
                        } catch (android.os.RemoteException e) {
                            android.util.Log.e(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Caught an exception notifying listener " + listener + ": ", e);
                        }
                    }
                } finally {
                    this.mToggleSensorListeners.finishBroadcast();
                }
            }
            com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.showSensorStateChangedActivity(sensor, toggleType);
        }

        public void handleSensorPrivacyChanged(int userId, int toggleType, int sensor, int state) {
            if (userId == com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser) {
                com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.setGlobalRestriction(sensor, com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.isCombinedToggleSensorPrivacyEnabled(sensor));
            }
            if (userId != com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser) {
                return;
            }
            synchronized (this.mListenerLock) {
                try {
                    int count = this.mToggleSensorListeners.beginBroadcast();
                    for (int i = 0; i < count; i++) {
                        android.hardware.ISensorPrivacyListener listener = this.mToggleSensorListeners.getBroadcastItem(i);
                        try {
                            listener.onSensorPrivacyStateChanged(toggleType, sensor, state);
                        } catch (android.os.RemoteException e) {
                            android.util.Log.e(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "Caught an exception notifying listener " + listener + ": ", e);
                        }
                    }
                } finally {
                    this.mToggleSensorListeners.finishBroadcast();
                }
            }
            com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.showSensorStateChangedActivity(sensor, toggleType);
        }

        public void removeSuppressPackageReminderToken(android.util.Pair<java.lang.Integer, android.os.UserHandle> key, android.os.IBinder token) {
            sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyHandler$$ExternalSyntheticLambda0
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    ((com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl) obj).removeSuppressPackageReminderToken((android.util.Pair) obj2, (android.os.IBinder) obj3);
                }
            }, com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl, key, token));
        }

        private void addDeathRecipient(android.hardware.ISensorPrivacyListener listener) {
            android.util.Pair<com.android.server.sensorprivacy.SensorPrivacyService.DeathRecipient, java.lang.Integer> deathRecipient;
            android.util.Pair<com.android.server.sensorprivacy.SensorPrivacyService.DeathRecipient, java.lang.Integer> deathRecipient2 = this.mDeathRecipients.get(listener);
            if (deathRecipient2 != null) {
                int newRefCount = ((java.lang.Integer) deathRecipient2.second).intValue() + 1;
                deathRecipient = new android.util.Pair<>((com.android.server.sensorprivacy.SensorPrivacyService.DeathRecipient) deathRecipient2.first, java.lang.Integer.valueOf(newRefCount));
            } else {
                deathRecipient = new android.util.Pair<>(com.android.server.sensorprivacy.SensorPrivacyService.this.new DeathRecipient(listener), 1);
            }
            this.mDeathRecipients.put(listener, deathRecipient);
        }

        private void removeDeathRecipient(android.hardware.ISensorPrivacyListener listener) {
            android.util.Pair<com.android.server.sensorprivacy.SensorPrivacyService.DeathRecipient, java.lang.Integer> deathRecipient = this.mDeathRecipients.get(listener);
            if (deathRecipient == null) {
                return;
            }
            int newRefCount = ((java.lang.Integer) deathRecipient.second).intValue() - 1;
            if (newRefCount == 0) {
                this.mDeathRecipients.remove(listener);
                ((com.android.server.sensorprivacy.SensorPrivacyService.DeathRecipient) deathRecipient.first).destroy();
            } else {
                this.mDeathRecipients.put(listener, new android.util.Pair<>((com.android.server.sensorprivacy.SensorPrivacyService.DeathRecipient) deathRecipient.first, java.lang.Integer.valueOf(newRefCount)));
            }
        }
    }

    private final class DeathRecipient implements android.os.IBinder.DeathRecipient {
        private android.hardware.ISensorPrivacyListener mListener;

        DeathRecipient(android.hardware.ISensorPrivacyListener listener) {
            this.mListener = listener;
            try {
                this.mListener.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.removeSensorPrivacyListener(this.mListener);
            com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.removeToggleSensorPrivacyListener(this.mListener);
        }

        public void destroy() {
            try {
                this.mListener.asBinder().unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forAllUsers(com.android.internal.util.FunctionalUtils.ThrowingConsumer<java.lang.Integer> c) {
        int[] userIds = this.mUserManagerInternal.getUserIds();
        for (int i : userIds) {
            c.accept(java.lang.Integer.valueOf(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SensorPrivacyManagerInternalImpl extends android.hardware.SensorPrivacyManagerInternal {
        private android.util.ArrayMap<java.lang.Integer, android.util.ArraySet<android.hardware.SensorPrivacyManagerInternal.OnUserSensorPrivacyChangedListener>> mAllUserListeners;
        private android.util.ArrayMap<java.lang.Integer, android.util.ArrayMap<java.lang.Integer, android.util.ArraySet<android.hardware.SensorPrivacyManagerInternal.OnSensorPrivacyChangedListener>>> mListeners;
        private final java.lang.Object mLock;

        private SensorPrivacyManagerInternalImpl() {
            this.mListeners = new android.util.ArrayMap<>();
            this.mAllUserListeners = new android.util.ArrayMap<>();
            this.mLock = new java.lang.Object();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dispatch(final int userId, int sensor, final boolean enabled) {
            android.util.ArraySet<android.hardware.SensorPrivacyManagerInternal.OnSensorPrivacyChangedListener> sensorListeners;
            synchronized (this.mLock) {
                android.util.ArraySet<android.hardware.SensorPrivacyManagerInternal.OnUserSensorPrivacyChangedListener> allUserSensorListeners = this.mAllUserListeners.get(java.lang.Integer.valueOf(sensor));
                if (allUserSensorListeners != null) {
                    for (int i = 0; i < allUserSensorListeners.size(); i++) {
                        final android.hardware.SensorPrivacyManagerInternal.OnUserSensorPrivacyChangedListener listener = allUserSensorListeners.valueAt(i);
                        com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyManagerInternalImpl$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                listener.onSensorPrivacyChanged(userId, enabled);
                            }
                        });
                    }
                }
                android.util.ArrayMap<java.lang.Integer, android.util.ArraySet<android.hardware.SensorPrivacyManagerInternal.OnSensorPrivacyChangedListener>> userSensorListeners = this.mListeners.get(java.lang.Integer.valueOf(userId));
                if (userSensorListeners != null && (sensorListeners = userSensorListeners.get(java.lang.Integer.valueOf(sensor))) != null) {
                    for (int i2 = 0; i2 < sensorListeners.size(); i2++) {
                        final android.hardware.SensorPrivacyManagerInternal.OnSensorPrivacyChangedListener listener2 = sensorListeners.valueAt(i2);
                        com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.sensorprivacy.SensorPrivacyService$SensorPrivacyManagerInternalImpl$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                listener2.onSensorPrivacyChanged(enabled);
                            }
                        });
                    }
                }
            }
        }

        public boolean isSensorPrivacyEnabled(int userId, int sensor) {
            return com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.isToggleSensorPrivacyEnabledInternal(userId, 1, sensor);
        }

        public void addSensorPrivacyListener(int userId, int sensor, android.hardware.SensorPrivacyManagerInternal.OnSensorPrivacyChangedListener listener) {
            synchronized (this.mLock) {
                android.util.ArrayMap<java.lang.Integer, android.util.ArraySet<android.hardware.SensorPrivacyManagerInternal.OnSensorPrivacyChangedListener>> userSensorListeners = this.mListeners.get(java.lang.Integer.valueOf(userId));
                if (userSensorListeners == null) {
                    userSensorListeners = new android.util.ArrayMap<>();
                    this.mListeners.put(java.lang.Integer.valueOf(userId), userSensorListeners);
                }
                android.util.ArraySet<android.hardware.SensorPrivacyManagerInternal.OnSensorPrivacyChangedListener> sensorListeners = userSensorListeners.get(java.lang.Integer.valueOf(sensor));
                if (sensorListeners == null) {
                    sensorListeners = new android.util.ArraySet<>();
                    userSensorListeners.put(java.lang.Integer.valueOf(sensor), sensorListeners);
                }
                sensorListeners.add(listener);
            }
        }

        public void addSensorPrivacyListenerForAllUsers(int sensor, android.hardware.SensorPrivacyManagerInternal.OnUserSensorPrivacyChangedListener listener) {
            synchronized (this.mLock) {
                android.util.ArraySet<android.hardware.SensorPrivacyManagerInternal.OnUserSensorPrivacyChangedListener> sensorListeners = this.mAllUserListeners.get(java.lang.Integer.valueOf(sensor));
                if (sensorListeners == null) {
                    sensorListeners = new android.util.ArraySet<>();
                    this.mAllUserListeners.put(java.lang.Integer.valueOf(sensor), sensorListeners);
                }
                sensorListeners.add(listener);
            }
        }

        public void setPhysicalToggleSensorPrivacy(int userId, int sensor, boolean enable) {
            com.android.server.sensorprivacy.SensorPrivacyService.SensorPrivacyServiceImpl sps = com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl;
            int userId2 = userId == -2 ? com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser : userId;
            int realUserId = userId2 == -10000 ? com.android.server.sensorprivacy.SensorPrivacyService.this.mContext.getUserId() : userId2;
            sps.setToggleSensorPrivacyUnchecked(2, realUserId, 5, sensor, enable);
            if (!enable) {
                sps.setToggleSensorPrivacyUnchecked(1, realUserId, 5, sensor, enable);
            }
        }
    }

    private class CallStateHelper {
        private boolean mIsInEmergencyCall;
        private boolean mMicUnmutedForEmergencyCall;
        private java.lang.Object mCallStateLock = new java.lang.Object();
        private com.android.server.sensorprivacy.SensorPrivacyService.CallStateHelper.OutgoingEmergencyStateCallback mEmergencyStateCallback = new com.android.server.sensorprivacy.SensorPrivacyService.CallStateHelper.OutgoingEmergencyStateCallback();
        private com.android.server.sensorprivacy.SensorPrivacyService.CallStateHelper.CallStateCallback mCallStateCallback = new com.android.server.sensorprivacy.SensorPrivacyService.CallStateHelper.CallStateCallback();

        /* JADX WARN: Multi-variable type inference failed */
        CallStateHelper() {
            com.android.server.sensorprivacy.SensorPrivacyService.this.mTelephonyManager.registerTelephonyCallback(com.android.server.FgThread.getExecutor(), this.mEmergencyStateCallback);
            com.android.server.sensorprivacy.SensorPrivacyService.this.mTelephonyManager.registerTelephonyCallback(com.android.server.FgThread.getExecutor(), this.mCallStateCallback);
        }

        boolean isInEmergencyCall() {
            boolean z;
            synchronized (this.mCallStateLock) {
                z = this.mIsInEmergencyCall;
            }
            return z;
        }

        private class OutgoingEmergencyStateCallback extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.OutgoingEmergencyCallListener {
            private OutgoingEmergencyStateCallback() {
            }

            public void onOutgoingEmergencyCall(android.telephony.emergency.EmergencyNumber placedEmergencyNumber, int subscriptionId) {
                com.android.server.sensorprivacy.SensorPrivacyService.CallStateHelper.this.onEmergencyCall();
            }
        }

        private class CallStateCallback extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.CallStateListener {
            private CallStateCallback() {
            }

            @Override // android.telephony.TelephonyCallback.CallStateListener
            public void onCallStateChanged(int state) {
                if (state == 0) {
                    com.android.server.sensorprivacy.SensorPrivacyService.CallStateHelper.this.onCallOver();
                } else {
                    com.android.server.sensorprivacy.SensorPrivacyService.CallStateHelper.this.onCall();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onEmergencyCall() {
            synchronized (this.mCallStateLock) {
                if (com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceExt.isStealthSecurityMode()) {
                    onCall();
                    android.util.Log.i(com.android.server.sensorprivacy.SensorPrivacyService.TAG, "onEmergencyCall isStealthSecurityMode true");
                    return;
                }
                if (!this.mIsInEmergencyCall) {
                    this.mIsInEmergencyCall = true;
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceExt.enterEnmergencyCall();
                    if (com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.isToggleSensorPrivacyEnabled(1, 1)) {
                        com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.setToggleSensorPrivacyUnchecked(1, com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser, 5, 1, false);
                        this.mMicUnmutedForEmergencyCall = true;
                    } else {
                        this.mMicUnmutedForEmergencyCall = false;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onCall() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (this.mCallStateLock) {
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.showSensorUseDialog(1);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onCallOver() {
            synchronized (this.mCallStateLock) {
                if (this.mIsInEmergencyCall) {
                    this.mIsInEmergencyCall = false;
                    if (this.mMicUnmutedForEmergencyCall) {
                        com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceImpl.setToggleSensorPrivacyUnchecked(1, com.android.server.sensorprivacy.SensorPrivacyService.this.mCurrentUser, 5, 1, true);
                        this.mMicUnmutedForEmergencyCall = false;
                    }
                    com.android.server.sensorprivacy.SensorPrivacyService.this.mSensorPrivacyServiceExt.exitEnmergencyCall();
                }
            }
        }
    }

    static long getCurrentTimeMillis() {
        return android.os.SystemClock.elapsedRealtime();
    }
}
