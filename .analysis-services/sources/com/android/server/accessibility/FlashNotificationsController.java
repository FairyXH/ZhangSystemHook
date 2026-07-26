package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
class FlashNotificationsController {
    static final java.lang.String ACTION_FLASH_NOTIFICATION_START_PREVIEW = "com.android.internal.intent.action.FLASH_NOTIFICATION_START_PREVIEW";
    static final java.lang.String ACTION_FLASH_NOTIFICATION_STOP_PREVIEW = "com.android.internal.intent.action.FLASH_NOTIFICATION_STOP_PREVIEW";
    private static final boolean DEBUG = true;
    static final java.lang.String EXTRA_FLASH_NOTIFICATION_PREVIEW_COLOR = "com.android.internal.intent.extra.FLASH_NOTIFICATION_PREVIEW_COLOR";
    static final java.lang.String EXTRA_FLASH_NOTIFICATION_PREVIEW_TYPE = "com.android.internal.intent.extra.FLASH_NOTIFICATION_PREVIEW_TYPE";
    private static final java.lang.String LOG_TAG = "FlashNotifController";
    static final int PREVIEW_TYPE_LONG = 1;
    static final int PREVIEW_TYPE_SHORT = 0;
    private static final int SCREEN_DEFAULT_ALPHA = 1711276032;
    private static final int SCREEN_DEFAULT_COLOR = 16776960;
    private static final int SCREEN_DEFAULT_COLOR_WITH_ALPHA = 1728052992;
    private static final int SCREEN_FADE_DURATION_MS = 200;
    private static final int SCREEN_FADE_OUT_TIMEOUT_MS = 10;
    static final java.lang.String SETTING_KEY_CAMERA_FLASH_NOTIFICATION = "camera_flash_notification";
    static final java.lang.String SETTING_KEY_SCREEN_FLASH_NOTIFICATION = "screen_flash_notification";
    static final java.lang.String SETTING_KEY_SCREEN_FLASH_NOTIFICATION_COLOR = "screen_flash_notification_color_global";
    private static final java.lang.String TAG_ALARM = "alarm";
    private static final java.lang.String TAG_PREVIEW = "preview";
    private static final int TYPE_DEFAULT = 1;
    private static final int TYPE_DEFAULT_OFF_MS = 250;
    private static final int TYPE_DEFAULT_ON_MS = 350;
    private static final int TYPE_DEFAULT_SCREEN_DELAY_MS = 300;
    private static final int TYPE_LONG_PREVIEW = 3;
    private static final int TYPE_LONG_PREVIEW_OFF_MS = 1000;
    private static final int TYPE_LONG_PREVIEW_ON_MS = 5000;
    private static final int TYPE_SEQUENCE = 2;
    private static final int TYPE_SEQUENCE_OFF_MS = 700;
    private static final int TYPE_SEQUENCE_ON_MS = 700;
    private static final java.lang.String WAKE_LOCK_TAG = "a11y:FlashNotificationsController";
    private static final long WAKE_LOCK_TIMEOUT_MS = 300000;
    private final android.media.AudioManager.AudioPlaybackCallback mAudioPlaybackCallback;
    private final android.os.Handler mCallbackHandler;
    private java.lang.String mCameraId;
    private android.hardware.camera2.CameraManager mCameraManager;
    private final android.content.Context mContext;
    private com.android.server.accessibility.FlashNotificationsController.FlashNotification mCurrentFlashNotification;
    private final android.hardware.display.DisplayManager mDisplayManager;
    private int mDisplayState;
    final com.android.server.accessibility.FlashNotificationsController.FlashBroadcastReceiver mFlashBroadcastReceiver;
    private final android.os.Handler mFlashNotificationHandler;
    private final java.util.LinkedList<com.android.server.accessibility.FlashNotificationsController.FlashNotification> mFlashNotifications;
    private boolean mIsAlarming;
    private boolean mIsCameraFlashNotificationEnabled;
    private boolean mIsCameraOpened;
    private boolean mIsScreenFlashNotificationEnabled;
    private boolean mIsTorchOn;
    private boolean mIsTorchTouched;
    private final android.os.Handler mMainHandler;
    private android.view.View mScreenFlashNotificationOverlayView;
    private volatile com.android.server.accessibility.FlashNotificationsController.FlashNotificationThread mThread;
    final android.hardware.camera2.CameraManager.AvailabilityCallback mTorchAvailabilityCallback;
    private final android.hardware.camera2.CameraManager.TorchCallback mTorchCallback;
    private final android.os.PowerManager.WakeLock mWakeLock;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface FlashNotificationType {
    }

    /* JADX INFO: renamed from: com.android.server.accessibility.FlashNotificationsController$3, reason: invalid class name */
    class AnonymousClass3 extends android.media.AudioManager.AudioPlaybackCallback {
        AnonymousClass3() {
        }

        @Override // android.media.AudioManager.AudioPlaybackCallback
        public void onPlaybackConfigChanged(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
            boolean isAlarmActive = false;
            if (configs != null) {
                isAlarmActive = configs.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.accessibility.FlashNotificationsController$3$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.accessibility.FlashNotificationsController.AnonymousClass3.lambda$onPlaybackConfigChanged$0((android.media.AudioPlaybackConfiguration) obj);
                    }
                });
            }
            if (com.android.server.accessibility.FlashNotificationsController.this.mIsAlarming != isAlarmActive) {
                android.util.Log.d(com.android.server.accessibility.FlashNotificationsController.LOG_TAG, "alarm state changed: " + isAlarmActive);
                if (isAlarmActive) {
                    com.android.server.accessibility.FlashNotificationsController.this.startFlashNotificationSequenceForAlarm();
                } else {
                    com.android.server.accessibility.FlashNotificationsController.this.stopFlashNotificationSequenceForAlarm();
                }
                com.android.server.accessibility.FlashNotificationsController.this.mIsAlarming = isAlarmActive;
            }
        }

        static /* synthetic */ boolean lambda$onPlaybackConfigChanged$0(android.media.AudioPlaybackConfiguration config) {
            return config.isActive() && config.getAudioAttributes().getUsage() == 4;
        }
    }

    FlashNotificationsController(android.content.Context context) {
        this(context, getStartedHandler("FlashNotificationThread"), getStartedHandler(LOG_TAG));
    }

    FlashNotificationsController(android.content.Context context, android.os.Handler flashNotificationHandler, android.os.Handler callbackHandler) {
        this.mFlashNotifications = new java.util.LinkedList<>();
        this.mIsTorchTouched = false;
        this.mIsTorchOn = false;
        this.mIsCameraFlashNotificationEnabled = false;
        this.mIsScreenFlashNotificationEnabled = false;
        this.mIsAlarming = false;
        this.mDisplayState = 1;
        this.mIsCameraOpened = false;
        this.mCameraId = null;
        this.mTorchCallback = new android.hardware.camera2.CameraManager.TorchCallback() { // from class: com.android.server.accessibility.FlashNotificationsController.1
            @Override // android.hardware.camera2.CameraManager.TorchCallback
            public void onTorchModeChanged(java.lang.String cameraId, boolean enabled) {
                if (com.android.server.accessibility.FlashNotificationsController.this.mCameraId != null && com.android.server.accessibility.FlashNotificationsController.this.mCameraId.equals(cameraId)) {
                    com.android.server.accessibility.FlashNotificationsController.this.mIsTorchOn = enabled;
                    android.util.Log.d(com.android.server.accessibility.FlashNotificationsController.LOG_TAG, "onTorchModeChanged, set mIsTorchOn=" + enabled);
                }
            }
        };
        this.mTorchAvailabilityCallback = new android.hardware.camera2.CameraManager.AvailabilityCallback() { // from class: com.android.server.accessibility.FlashNotificationsController.2
            public void onCameraOpened(java.lang.String cameraId, java.lang.String packageId) {
                if (com.android.server.accessibility.FlashNotificationsController.this.mCameraId != null && com.android.server.accessibility.FlashNotificationsController.this.mCameraId.equals(cameraId)) {
                    com.android.server.accessibility.FlashNotificationsController.this.mIsCameraOpened = true;
                }
            }

            public void onCameraClosed(java.lang.String cameraId) {
                if (com.android.server.accessibility.FlashNotificationsController.this.mCameraId != null && com.android.server.accessibility.FlashNotificationsController.this.mCameraId.equals(cameraId)) {
                    com.android.server.accessibility.FlashNotificationsController.this.mIsCameraOpened = false;
                }
            }
        };
        this.mAudioPlaybackCallback = new com.android.server.accessibility.FlashNotificationsController.AnonymousClass3();
        this.mContext = context;
        this.mMainHandler = new android.os.Handler(this.mContext.getMainLooper());
        this.mFlashNotificationHandler = flashNotificationHandler;
        this.mCallbackHandler = callbackHandler;
        new com.android.server.accessibility.FlashNotificationsController.FlashContentObserver(this.mMainHandler).register(this.mContext.getContentResolver());
        android.content.IntentFilter broadcastFilter = new android.content.IntentFilter();
        broadcastFilter.addAction("android.intent.action.BOOT_COMPLETED");
        broadcastFilter.addAction(ACTION_FLASH_NOTIFICATION_START_PREVIEW);
        broadcastFilter.addAction(ACTION_FLASH_NOTIFICATION_STOP_PREVIEW);
        this.mFlashBroadcastReceiver = new com.android.server.accessibility.FlashNotificationsController.FlashBroadcastReceiver();
        this.mContext.registerReceiver(this.mFlashBroadcastReceiver, broadcastFilter, 4);
        android.os.PowerManager powerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
        this.mWakeLock = powerManager.newWakeLock(1, WAKE_LOCK_TAG);
        this.mDisplayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
        android.hardware.display.DisplayManager.DisplayListener displayListener = new android.hardware.display.DisplayManager.DisplayListener() { // from class: com.android.server.accessibility.FlashNotificationsController.4
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int displayId) {
                android.view.Display display;
                if (com.android.server.accessibility.FlashNotificationsController.this.mDisplayManager != null && (display = com.android.server.accessibility.FlashNotificationsController.this.mDisplayManager.getDisplay(displayId)) != null) {
                    com.android.server.accessibility.FlashNotificationsController.this.mDisplayState = display.getState();
                }
            }
        };
        if (this.mDisplayManager != null) {
            this.mDisplayManager.registerDisplayListener(displayListener, null);
        }
    }

    private static android.os.Handler getStartedHandler(java.lang.String tag) {
        android.os.HandlerThread handlerThread = new android.os.HandlerThread(tag);
        handlerThread.start();
        return handlerThread.getThreadHandler();
    }

    boolean startFlashNotificationSequence(final java.lang.String opPkg, int reason, android.os.IBinder token) {
        com.android.server.accessibility.FlashNotificationsController.FlashNotification flashNotification = new com.android.server.accessibility.FlashNotificationsController.FlashNotification(opPkg, 2, getScreenFlashColorPreference(reason), token, new android.os.IBinder.DeathRecipient() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda2
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                this.f$0.lambda$startFlashNotificationSequence$0(opPkg);
            }
        });
        if (!flashNotification.tryLinkToDeath()) {
            return false;
        }
        requestStartFlashNotification(flashNotification);
        return true;
    }

    boolean stopFlashNotificationSequence(java.lang.String opPkg) {
        lambda$startFlashNotificationSequence$0(opPkg);
        return true;
    }

    boolean startFlashNotificationEvent(java.lang.String opPkg, int reason, java.lang.String reasonPkg) {
        requestStartFlashNotification(new com.android.server.accessibility.FlashNotificationsController.FlashNotification(opPkg, 1, getScreenFlashColorPreference(reason, reasonPkg)));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFlashNotificationShortPreview() {
        requestStartFlashNotification(new com.android.server.accessibility.FlashNotificationsController.FlashNotification(TAG_PREVIEW, 1, getScreenFlashColorPreference(4)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFlashNotificationLongPreview(int color) {
        requestStartFlashNotification(new com.android.server.accessibility.FlashNotificationsController.FlashNotification(TAG_PREVIEW, 3, color));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopFlashNotificationLongPreview() {
        lambda$startFlashNotificationSequence$0(TAG_PREVIEW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFlashNotificationSequenceForAlarm() {
        requestStartFlashNotification(new com.android.server.accessibility.FlashNotificationsController.FlashNotification("alarm", 2, getScreenFlashColorPreference(2)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopFlashNotificationSequenceForAlarm() {
        lambda$startFlashNotificationSequence$0("alarm");
    }

    private void requestStartFlashNotification(com.android.server.accessibility.FlashNotificationsController.FlashNotification flashNotification) {
        android.util.Log.d(LOG_TAG, "requestStartFlashNotification");
        boolean isFeatureOn = android.util.FeatureFlagUtils.isEnabled(this.mContext, "settings_flash_notifications");
        boolean z = false;
        this.mIsCameraFlashNotificationEnabled = isFeatureOn && android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), SETTING_KEY_CAMERA_FLASH_NOTIFICATION, 0, -2) != 0;
        if (isFeatureOn && android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), SETTING_KEY_SCREEN_FLASH_NOTIFICATION, 0, -2) != 0) {
            z = true;
        }
        this.mIsScreenFlashNotificationEnabled = z;
        if (flashNotification.mType == 1 && this.mIsScreenFlashNotificationEnabled) {
            this.mMainHandler.sendMessageDelayed(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda4
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.accessibility.FlashNotificationsController) obj).startFlashNotification((com.android.server.accessibility.FlashNotificationsController.FlashNotification) obj2);
                }
            }, this, flashNotification), 300L);
            android.util.Log.i(LOG_TAG, "give some delay for flash notification");
        } else {
            startFlashNotification(flashNotification);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: stopFlashNotification, reason: merged with bridge method [inline-methods] */
    public void lambda$startFlashNotificationSequence$0(java.lang.String tag) {
        android.util.Log.i(LOG_TAG, "stopFlashNotification: tag=" + tag);
        synchronized (this.mFlashNotifications) {
            com.android.server.accessibility.FlashNotificationsController.FlashNotification notification = removeFlashNotificationLocked(tag);
            if (this.mCurrentFlashNotification != null && notification == this.mCurrentFlashNotification) {
                stopFlashNotificationLocked();
                startNextFlashNotificationLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareForCameraFlashNotification() {
        this.mCameraManager = (android.hardware.camera2.CameraManager) this.mContext.getSystemService(android.hardware.camera2.CameraManager.class);
        if (this.mCameraManager != null) {
            try {
                this.mCameraId = getCameraId();
            } catch (android.hardware.camera2.CameraAccessException e) {
                android.util.Log.e(LOG_TAG, "CameraAccessException", e);
            }
            this.mCameraManager.registerTorchCallback(this.mTorchCallback, (android.os.Handler) null);
        }
    }

    private java.lang.String getCameraId() throws android.hardware.camera2.CameraAccessException {
        java.lang.String[] ids = this.mCameraManager.getCameraIdList();
        for (java.lang.String id : ids) {
            android.hardware.camera2.CameraCharacteristics c = this.mCameraManager.getCameraCharacteristics(id);
            java.lang.Boolean flashAvailable = (java.lang.Boolean) c.get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE);
            java.lang.Integer lensFacing = (java.lang.Integer) c.get(android.hardware.camera2.CameraCharacteristics.LENS_FACING);
            if (flashAvailable != null && lensFacing != null && flashAvailable.booleanValue() && lensFacing.intValue() == 1) {
                android.util.Log.d(LOG_TAG, "Found valid camera, cameraId=" + id);
                return id;
            }
        }
        return null;
    }

    private void showScreenNotificationOverlayView(int color) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.FlashNotificationsController) obj).showScreenNotificationOverlayViewMainThread(((java.lang.Integer) obj2).intValue());
            }
        }, this, java.lang.Integer.valueOf(color)));
    }

    private void hideScreenNotificationOverlayView() {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.accessibility.FlashNotificationsController) obj).fadeOutScreenNotificationOverlayViewMainThread();
            }
        }, this));
        this.mMainHandler.sendMessageDelayed(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.accessibility.FlashNotificationsController) obj).hideScreenNotificationOverlayViewMainThread();
            }
        }, this), 210L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showScreenNotificationOverlayViewMainThread(int color) {
        android.util.Log.d(LOG_TAG, "showScreenNotificationOverlayViewMainThread");
        android.view.WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams(-1, -1, 2015, com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_LOCATION_PROVIDER, -3);
        params.privateFlags |= 16;
        params.layoutInDisplayCutoutMode = 1;
        params.inputFeatures |= 1;
        if (this.mScreenFlashNotificationOverlayView == null) {
            this.mScreenFlashNotificationOverlayView = getScreenNotificationOverlayView(color);
            ((android.view.WindowManager) this.mContext.getSystemService(android.view.WindowManager.class)).addView(this.mScreenFlashNotificationOverlayView, params);
            fadeScreenNotificationOverlayViewMainThread(this.mScreenFlashNotificationOverlayView, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeOutScreenNotificationOverlayViewMainThread() {
        android.util.Log.d(LOG_TAG, "fadeOutScreenNotificationOverlayViewMainThread");
        if (this.mScreenFlashNotificationOverlayView != null) {
            fadeScreenNotificationOverlayViewMainThread(this.mScreenFlashNotificationOverlayView, false);
        }
    }

    private void fadeScreenNotificationOverlayViewMainThread(android.view.View view, boolean in) {
        android.animation.ObjectAnimator fade = android.animation.ObjectAnimator.ofFloat(view, "alpha", in ? 0.0f : 1.0f, in ? 1.0f : 0.0f);
        fade.setInterpolator(new android.view.animation.AccelerateInterpolator());
        fade.setAutoCancel(true);
        fade.setDuration(200L);
        fade.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideScreenNotificationOverlayViewMainThread() {
        android.util.Log.d(LOG_TAG, "hideScreenNotificationOverlayViewMainThread");
        if (this.mScreenFlashNotificationOverlayView != null) {
            this.mScreenFlashNotificationOverlayView.setVisibility(8);
            ((android.view.WindowManager) this.mContext.getSystemService(android.view.WindowManager.class)).removeView(this.mScreenFlashNotificationOverlayView);
            this.mScreenFlashNotificationOverlayView = null;
        }
    }

    private android.view.View getScreenNotificationOverlayView(int color) {
        android.view.View screenNotificationOverlayView = new android.widget.FrameLayout(this.mContext);
        screenNotificationOverlayView.setBackgroundColor(color);
        screenNotificationOverlayView.setAlpha(0.0f);
        return screenNotificationOverlayView;
    }

    private int getScreenFlashColorPreference(int reason, java.lang.String reasonPkg) {
        return getScreenFlashColorPreference();
    }

    private int getScreenFlashColorPreference(int reason) {
        return getScreenFlashColorPreference();
    }

    private int getScreenFlashColorPreference() {
        return android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), SETTING_KEY_SCREEN_FLASH_NOTIFICATION_COLOR, SCREEN_DEFAULT_COLOR_WITH_ALPHA, -2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFlashNotification(com.android.server.accessibility.FlashNotificationsController.FlashNotification flashNotification) {
        int type = flashNotification.mType;
        java.lang.String tag = flashNotification.mTag;
        android.util.Log.i(LOG_TAG, "startFlashNotification: type=" + type + ", tag=" + tag);
        if (!this.mIsCameraFlashNotificationEnabled && !this.mIsScreenFlashNotificationEnabled && !flashNotification.mForceStartScreenFlash) {
            android.util.Log.d(LOG_TAG, "Flash notification is disabled");
            return;
        }
        if (this.mIsCameraOpened) {
            android.util.Log.d(LOG_TAG, "Since camera for torch is opened, block notification.");
            return;
        }
        if (this.mIsCameraFlashNotificationEnabled && this.mCameraId == null) {
            prepareForCameraFlashNotification();
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mFlashNotifications) {
                if (type == 1 || type == 3) {
                    if (this.mCurrentFlashNotification != null) {
                        android.util.Log.i(LOG_TAG, "Default type of flash notification can not work because previous flash notification is working");
                    } else {
                        startFlashNotificationLocked(flashNotification);
                    }
                } else if (type == 2) {
                    if (this.mCurrentFlashNotification != null) {
                        removeFlashNotificationLocked(tag);
                        stopFlashNotificationLocked();
                    }
                    this.mFlashNotifications.addFirst(flashNotification);
                    startNextFlashNotificationLocked();
                } else {
                    android.util.Log.e(LOG_TAG, "Unavailable flash notification type");
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private com.android.server.accessibility.FlashNotificationsController.FlashNotification removeFlashNotificationLocked(java.lang.String tag) {
        java.util.ListIterator<com.android.server.accessibility.FlashNotificationsController.FlashNotification> iterator = this.mFlashNotifications.listIterator(0);
        while (iterator.hasNext()) {
            com.android.server.accessibility.FlashNotificationsController.FlashNotification notification = iterator.next();
            if (notification != null && notification.mTag.equals(tag)) {
                iterator.remove();
                notification.tryUnlinkToDeath();
                android.util.Log.i(LOG_TAG, "removeFlashNotificationLocked: tag=" + notification.mTag);
                return notification;
            }
        }
        if (this.mCurrentFlashNotification != null && this.mCurrentFlashNotification.mTag.equals(tag)) {
            this.mCurrentFlashNotification.tryUnlinkToDeath();
            return this.mCurrentFlashNotification;
        }
        return null;
    }

    private void stopFlashNotificationLocked() {
        if (this.mThread != null) {
            android.util.Log.i(LOG_TAG, "stopFlashNotificationLocked: tag=" + this.mThread.mFlashNotification.mTag);
            this.mThread.cancel();
            this.mThread = null;
        }
        doCameraFlashNotificationOff();
        doScreenFlashNotificationOff();
    }

    private void startNextFlashNotificationLocked() {
        android.util.Log.i(LOG_TAG, "startNextFlashNotificationLocked");
        if (this.mFlashNotifications.size() <= 0) {
            this.mCurrentFlashNotification = null;
        } else {
            startFlashNotificationLocked(this.mFlashNotifications.getFirst());
        }
    }

    private void startFlashNotificationLocked(com.android.server.accessibility.FlashNotificationsController.FlashNotification notification) {
        android.util.Log.i(LOG_TAG, "startFlashNotificationLocked: type=" + notification.mType + ", tag=" + notification.mTag);
        this.mCurrentFlashNotification = notification;
        this.mThread = new com.android.server.accessibility.FlashNotificationsController.FlashNotificationThread(notification);
        this.mFlashNotificationHandler.post(this.mThread);
    }

    private boolean isDozeMode() {
        return this.mDisplayState == 3 || this.mDisplayState == 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCameraFlashNotificationOn() {
        if (this.mIsCameraFlashNotificationEnabled && !this.mIsTorchOn) {
            doCameraFlashNotification(true);
        }
        android.util.Log.i(LOG_TAG, "doCameraFlashNotificationOn: isCameraFlashNotificationEnabled=" + this.mIsCameraFlashNotificationEnabled + ", isTorchOn=" + this.mIsTorchOn + ", isTorchTouched=" + this.mIsTorchTouched);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCameraFlashNotificationOff() {
        if (this.mIsTorchTouched) {
            doCameraFlashNotification(false);
        }
        android.util.Log.i(LOG_TAG, "doCameraFlashNotificationOff: isCameraFlashNotificationEnabled=" + this.mIsCameraFlashNotificationEnabled + ", isTorchOn=" + this.mIsTorchOn + ", isTorchTouched=" + this.mIsTorchTouched);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doScreenFlashNotificationOn(int color, boolean forceStartScreenFlash) {
        boolean isDoze = isDozeMode();
        if ((this.mIsScreenFlashNotificationEnabled || forceStartScreenFlash) && !isDoze) {
            showScreenNotificationOverlayView(color);
        }
        android.util.Log.i(LOG_TAG, "doScreenFlashNotificationOn: isScreenFlashNotificationEnabled=" + this.mIsScreenFlashNotificationEnabled + ", isDozeMode=" + isDoze + ", color=" + java.lang.Integer.toHexString(color));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doScreenFlashNotificationOff() {
        hideScreenNotificationOverlayView();
        android.util.Log.i(LOG_TAG, "doScreenFlashNotificationOff: isScreenFlashNotificationEnabled=" + this.mIsScreenFlashNotificationEnabled);
    }

    private void doCameraFlashNotification(boolean on) {
        android.util.Log.d(LOG_TAG, "doCameraFlashNotification: " + on + " mCameraId : " + this.mCameraId);
        if (this.mCameraManager != null && this.mCameraId != null) {
            try {
                this.mCameraManager.setTorchMode(this.mCameraId, on);
                this.mIsTorchTouched = on;
                return;
            } catch (android.hardware.camera2.CameraAccessException e) {
                android.util.Log.e(LOG_TAG, "Failed to setTorchMode: " + e);
                return;
            }
        }
        android.util.Log.e(LOG_TAG, "Can not use camera flash notification, please check CameraManager!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class FlashNotification {
        private final int mColor;
        private final android.os.IBinder.DeathRecipient mDeathRecipient;
        private final boolean mForceStartScreenFlash;
        private final int mOffDuration;
        private final int mOnDuration;
        private int mRepeat;
        private final java.lang.String mTag;
        private final android.os.IBinder mToken;
        private final int mType;

        private FlashNotification(java.lang.String tag, int type, int color) {
            this(tag, type, color, null, null);
        }

        private FlashNotification(java.lang.String tag, int type, int color, android.os.IBinder token, android.os.IBinder.DeathRecipient deathRecipient) {
            this.mType = type;
            this.mTag = tag;
            this.mColor = color;
            this.mToken = token;
            this.mDeathRecipient = deathRecipient;
            switch (type) {
                case 2:
                    this.mOnDuration = com.android.server.am.ProcessList.PREVIOUS_APP_ADJ;
                    this.mOffDuration = com.android.server.am.ProcessList.PREVIOUS_APP_ADJ;
                    this.mRepeat = 0;
                    this.mForceStartScreenFlash = false;
                    break;
                case 3:
                    this.mOnDuration = 5000;
                    this.mOffDuration = 1000;
                    this.mRepeat = 1;
                    this.mForceStartScreenFlash = true;
                    break;
                default:
                    this.mOnDuration = 350;
                    this.mOffDuration = 250;
                    this.mRepeat = 2;
                    this.mForceStartScreenFlash = false;
                    break;
            }
        }

        boolean tryLinkToDeath() {
            if (this.mToken == null || this.mDeathRecipient == null) {
                return false;
            }
            try {
                this.mToken.linkToDeath(this.mDeathRecipient, 0);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.accessibility.FlashNotificationsController.LOG_TAG, "RemoteException", e);
                return false;
            }
        }

        boolean tryUnlinkToDeath() {
            if (this.mToken == null || this.mDeathRecipient == null) {
                return false;
            }
            try {
                this.mToken.unlinkToDeath(this.mDeathRecipient, 0);
                return true;
            } catch (java.lang.Exception e) {
                return false;
            }
        }
    }

    private class FlashNotificationThread extends java.lang.Thread {
        private int mColor;
        private final com.android.server.accessibility.FlashNotificationsController.FlashNotification mFlashNotification;
        private boolean mForceStop;
        private boolean mShouldDoCameraFlash;
        private boolean mShouldDoScreenFlash;

        private FlashNotificationThread(com.android.server.accessibility.FlashNotificationsController.FlashNotification flashNotification) {
            this.mColor = 0;
            this.mShouldDoScreenFlash = false;
            this.mShouldDoCameraFlash = false;
            this.mFlashNotification = flashNotification;
            this.mForceStop = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            android.util.Log.d(com.android.server.accessibility.FlashNotificationsController.LOG_TAG, "run started: " + this.mFlashNotification.mTag);
            android.os.Process.setThreadPriority(-8);
            this.mColor = this.mFlashNotification.mColor;
            this.mShouldDoScreenFlash = android.graphics.Color.alpha(this.mColor) != 0 || this.mFlashNotification.mForceStartScreenFlash;
            this.mShouldDoCameraFlash = this.mFlashNotification.mType != 3;
            synchronized (this) {
                com.android.server.accessibility.FlashNotificationsController.this.mWakeLock.acquire(300000L);
                try {
                    startFlashNotification();
                } finally {
                    com.android.server.accessibility.FlashNotificationsController.this.doScreenFlashNotificationOff();
                    com.android.server.accessibility.FlashNotificationsController.this.doCameraFlashNotificationOff();
                    try {
                        com.android.server.accessibility.FlashNotificationsController.this.mWakeLock.release();
                    } catch (java.lang.RuntimeException e) {
                        android.util.Log.e(com.android.server.accessibility.FlashNotificationsController.LOG_TAG, "Error while releasing FlashNotificationsController wakelock (already released by the system?)");
                    }
                }
            }
            synchronized (com.android.server.accessibility.FlashNotificationsController.this.mFlashNotifications) {
                if (com.android.server.accessibility.FlashNotificationsController.this.mThread == this) {
                    com.android.server.accessibility.FlashNotificationsController.this.mThread = null;
                }
                if (!this.mForceStop) {
                    this.mFlashNotification.tryUnlinkToDeath();
                    com.android.server.accessibility.FlashNotificationsController.this.mCurrentFlashNotification = null;
                }
            }
            android.util.Log.d(com.android.server.accessibility.FlashNotificationsController.LOG_TAG, "run finished: " + this.mFlashNotification.mTag);
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0024 A[Catch: all -> 0x0064, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0005, B:7:0x000e, B:9:0x0016, B:12:0x0024, B:14:0x0028, B:15:0x0035, B:17:0x0039, B:18:0x003e, B:21:0x0057, B:22:0x0062), top: B:27:0x0001 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void startFlashNotification() {
            /*
                r3 = this;
                monitor-enter(r3)
            L1:
                boolean r0 = r3.mForceStop     // Catch: java.lang.Throwable -> L64
                if (r0 != 0) goto L62
                com.android.server.accessibility.FlashNotificationsController$FlashNotification r0 = r3.mFlashNotification     // Catch: java.lang.Throwable -> L64
                int r0 = com.android.server.accessibility.FlashNotificationsController.FlashNotification.m748$$Nest$fgetmType(r0)     // Catch: java.lang.Throwable -> L64
                r1 = 2
                if (r0 == r1) goto L24
                com.android.server.accessibility.FlashNotificationsController$FlashNotification r0 = r3.mFlashNotification     // Catch: java.lang.Throwable -> L64
                int r0 = com.android.server.accessibility.FlashNotificationsController.FlashNotification.m746$$Nest$fgetmRepeat(r0)     // Catch: java.lang.Throwable -> L64
                if (r0 < 0) goto L24
                com.android.server.accessibility.FlashNotificationsController$FlashNotification r0 = r3.mFlashNotification     // Catch: java.lang.Throwable -> L64
                int r1 = com.android.server.accessibility.FlashNotificationsController.FlashNotification.m746$$Nest$fgetmRepeat(r0)     // Catch: java.lang.Throwable -> L64
                int r2 = r1 + (-1)
                com.android.server.accessibility.FlashNotificationsController.FlashNotification.m749$$Nest$fputmRepeat(r0, r2)     // Catch: java.lang.Throwable -> L64
                if (r1 != 0) goto L24
                goto L62
            L24:
                boolean r0 = r3.mShouldDoScreenFlash     // Catch: java.lang.Throwable -> L64
                if (r0 == 0) goto L35
                com.android.server.accessibility.FlashNotificationsController r0 = com.android.server.accessibility.FlashNotificationsController.this     // Catch: java.lang.Throwable -> L64
                int r1 = r3.mColor     // Catch: java.lang.Throwable -> L64
                com.android.server.accessibility.FlashNotificationsController$FlashNotification r2 = r3.mFlashNotification     // Catch: java.lang.Throwable -> L64
                boolean r2 = com.android.server.accessibility.FlashNotificationsController.FlashNotification.m743$$Nest$fgetmForceStartScreenFlash(r2)     // Catch: java.lang.Throwable -> L64
                com.android.server.accessibility.FlashNotificationsController.m735$$Nest$mdoScreenFlashNotificationOn(r0, r1, r2)     // Catch: java.lang.Throwable -> L64
            L35:
                boolean r0 = r3.mShouldDoCameraFlash     // Catch: java.lang.Throwable -> L64
                if (r0 == 0) goto L3e
                com.android.server.accessibility.FlashNotificationsController r0 = com.android.server.accessibility.FlashNotificationsController.this     // Catch: java.lang.Throwable -> L64
                com.android.server.accessibility.FlashNotificationsController.m733$$Nest$mdoCameraFlashNotificationOn(r0)     // Catch: java.lang.Throwable -> L64
            L3e:
                com.android.server.accessibility.FlashNotificationsController$FlashNotification r0 = r3.mFlashNotification     // Catch: java.lang.Throwable -> L64
                int r0 = com.android.server.accessibility.FlashNotificationsController.FlashNotification.m745$$Nest$fgetmOnDuration(r0)     // Catch: java.lang.Throwable -> L64
                long r0 = (long) r0     // Catch: java.lang.Throwable -> L64
                r3.delay(r0)     // Catch: java.lang.Throwable -> L64
                com.android.server.accessibility.FlashNotificationsController r0 = com.android.server.accessibility.FlashNotificationsController.this     // Catch: java.lang.Throwable -> L64
                com.android.server.accessibility.FlashNotificationsController.m734$$Nest$mdoScreenFlashNotificationOff(r0)     // Catch: java.lang.Throwable -> L64
                com.android.server.accessibility.FlashNotificationsController r0 = com.android.server.accessibility.FlashNotificationsController.this     // Catch: java.lang.Throwable -> L64
                com.android.server.accessibility.FlashNotificationsController.m732$$Nest$mdoCameraFlashNotificationOff(r0)     // Catch: java.lang.Throwable -> L64
                boolean r0 = r3.mForceStop     // Catch: java.lang.Throwable -> L64
                if (r0 == 0) goto L57
                goto L62
            L57:
                com.android.server.accessibility.FlashNotificationsController$FlashNotification r0 = r3.mFlashNotification     // Catch: java.lang.Throwable -> L64
                int r0 = com.android.server.accessibility.FlashNotificationsController.FlashNotification.m744$$Nest$fgetmOffDuration(r0)     // Catch: java.lang.Throwable -> L64
                long r0 = (long) r0     // Catch: java.lang.Throwable -> L64
                r3.delay(r0)     // Catch: java.lang.Throwable -> L64
                goto L1
            L62:
                monitor-exit(r3)     // Catch: java.lang.Throwable -> L64
                return
            L64:
                r0 = move-exception
                monitor-exit(r3)     // Catch: java.lang.Throwable -> L64
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.FlashNotificationsController.FlashNotificationThread.startFlashNotification():void");
        }

        void cancel() {
            android.util.Log.d(com.android.server.accessibility.FlashNotificationsController.LOG_TAG, "run canceled: " + this.mFlashNotification.mTag);
            synchronized (this) {
                com.android.server.accessibility.FlashNotificationsController.this.mThread.mForceStop = true;
                com.android.server.accessibility.FlashNotificationsController.this.mThread.notify();
            }
        }

        private void delay(long duration) {
            if (duration > 0) {
                long bedtime = android.os.SystemClock.uptimeMillis() + duration;
                do {
                    try {
                        wait(duration);
                    } catch (java.lang.InterruptedException e) {
                    }
                    if (!this.mForceStop) {
                        duration = bedtime - android.os.SystemClock.uptimeMillis();
                    } else {
                        return;
                    }
                } while (duration > 0);
            }
        }
    }

    class FlashBroadcastReceiver extends android.content.BroadcastReceiver {
        FlashBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                if (android.os.UserHandle.myUserId() != android.app.ActivityManager.getCurrentUser()) {
                    return;
                }
                com.android.server.accessibility.FlashNotificationsController.this.mIsCameraFlashNotificationEnabled = android.provider.Settings.System.getIntForUser(com.android.server.accessibility.FlashNotificationsController.this.mContext.getContentResolver(), com.android.server.accessibility.FlashNotificationsController.SETTING_KEY_CAMERA_FLASH_NOTIFICATION, 0, -2) != 0;
                if (com.android.server.accessibility.FlashNotificationsController.this.mIsCameraFlashNotificationEnabled) {
                    com.android.server.accessibility.FlashNotificationsController.this.prepareForCameraFlashNotification();
                } else if (com.android.server.accessibility.FlashNotificationsController.this.mCameraManager != null) {
                    com.android.server.accessibility.FlashNotificationsController.this.mCameraManager.unregisterTorchCallback(com.android.server.accessibility.FlashNotificationsController.this.mTorchCallback);
                }
                android.media.AudioManager audioManager = (android.media.AudioManager) com.android.server.accessibility.FlashNotificationsController.this.mContext.getSystemService(android.media.AudioManager.class);
                if (audioManager != null) {
                    audioManager.registerAudioPlaybackCallback(com.android.server.accessibility.FlashNotificationsController.this.mAudioPlaybackCallback, com.android.server.accessibility.FlashNotificationsController.this.mCallbackHandler);
                }
                com.android.server.accessibility.FlashNotificationsController.this.mCameraManager = (android.hardware.camera2.CameraManager) com.android.server.accessibility.FlashNotificationsController.this.mContext.getSystemService(android.hardware.camera2.CameraManager.class);
                com.android.server.accessibility.FlashNotificationsController.this.mCameraManager.registerAvailabilityCallback(com.android.server.accessibility.FlashNotificationsController.this.mTorchAvailabilityCallback, com.android.server.accessibility.FlashNotificationsController.this.mCallbackHandler);
                return;
            }
            if (com.android.server.accessibility.FlashNotificationsController.ACTION_FLASH_NOTIFICATION_START_PREVIEW.equals(intent.getAction())) {
                android.util.Log.i(com.android.server.accessibility.FlashNotificationsController.LOG_TAG, "ACTION_FLASH_NOTIFICATION_START_PREVIEW");
                int color = intent.getIntExtra(com.android.server.accessibility.FlashNotificationsController.EXTRA_FLASH_NOTIFICATION_PREVIEW_COLOR, 0);
                int type = intent.getIntExtra(com.android.server.accessibility.FlashNotificationsController.EXTRA_FLASH_NOTIFICATION_PREVIEW_TYPE, 0);
                if (type == 1) {
                    com.android.server.accessibility.FlashNotificationsController.this.startFlashNotificationLongPreview(color);
                    return;
                } else {
                    if (type == 0) {
                        com.android.server.accessibility.FlashNotificationsController.this.startFlashNotificationShortPreview();
                        return;
                    }
                    return;
                }
            }
            if (com.android.server.accessibility.FlashNotificationsController.ACTION_FLASH_NOTIFICATION_STOP_PREVIEW.equals(intent.getAction())) {
                android.util.Log.i(com.android.server.accessibility.FlashNotificationsController.LOG_TAG, "ACTION_FLASH_NOTIFICATION_STOP_PREVIEW");
                com.android.server.accessibility.FlashNotificationsController.this.stopFlashNotificationLongPreview();
            }
        }
    }

    private final class FlashContentObserver extends android.database.ContentObserver {
        private final android.net.Uri mCameraFlashNotificationUri;
        private final android.net.Uri mScreenFlashNotificationUri;

        FlashContentObserver(android.os.Handler handler) {
            super(handler);
            this.mCameraFlashNotificationUri = android.provider.Settings.System.getUriFor(com.android.server.accessibility.FlashNotificationsController.SETTING_KEY_CAMERA_FLASH_NOTIFICATION);
            this.mScreenFlashNotificationUri = android.provider.Settings.System.getUriFor(com.android.server.accessibility.FlashNotificationsController.SETTING_KEY_SCREEN_FLASH_NOTIFICATION);
        }

        void register(android.content.ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.mCameraFlashNotificationUri, false, this, -1);
            contentResolver.registerContentObserver(this.mScreenFlashNotificationUri, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (this.mCameraFlashNotificationUri.equals(uri)) {
                com.android.server.accessibility.FlashNotificationsController.this.mIsCameraFlashNotificationEnabled = android.provider.Settings.System.getIntForUser(com.android.server.accessibility.FlashNotificationsController.this.mContext.getContentResolver(), com.android.server.accessibility.FlashNotificationsController.SETTING_KEY_CAMERA_FLASH_NOTIFICATION, 0, -2) != 0;
                if (com.android.server.accessibility.FlashNotificationsController.this.mIsCameraFlashNotificationEnabled) {
                    com.android.server.accessibility.FlashNotificationsController.this.prepareForCameraFlashNotification();
                    return;
                }
                com.android.server.accessibility.FlashNotificationsController.this.mIsTorchOn = false;
                if (com.android.server.accessibility.FlashNotificationsController.this.mCameraManager != null) {
                    com.android.server.accessibility.FlashNotificationsController.this.mCameraManager.unregisterTorchCallback(com.android.server.accessibility.FlashNotificationsController.this.mTorchCallback);
                    return;
                }
                return;
            }
            if (this.mScreenFlashNotificationUri.equals(uri)) {
                com.android.server.accessibility.FlashNotificationsController.this.mIsScreenFlashNotificationEnabled = android.provider.Settings.System.getIntForUser(com.android.server.accessibility.FlashNotificationsController.this.mContext.getContentResolver(), com.android.server.accessibility.FlashNotificationsController.SETTING_KEY_SCREEN_FLASH_NOTIFICATION, 0, -2) != 0;
            }
        }
    }
}
