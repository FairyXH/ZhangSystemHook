package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
class LegacyGlobalActions implements android.content.DialogInterface.OnDismissListener, android.content.DialogInterface.OnClickListener {
    private static final int DIALOG_DISMISS_DELAY = 300;
    private static final java.lang.String GLOBAL_ACTION_KEY_AIRPLANE = "airplane";
    private static final java.lang.String GLOBAL_ACTION_KEY_ASSIST = "assist";
    private static final java.lang.String GLOBAL_ACTION_KEY_BUGREPORT = "bugreport";
    private static final java.lang.String GLOBAL_ACTION_KEY_LOCKDOWN = "lockdown";
    private static final java.lang.String GLOBAL_ACTION_KEY_POWER = "power";
    private static final java.lang.String GLOBAL_ACTION_KEY_RESTART = "restart";
    private static final java.lang.String GLOBAL_ACTION_KEY_SETTINGS = "settings";
    private static final java.lang.String GLOBAL_ACTION_KEY_SILENT = "silent";
    private static final java.lang.String GLOBAL_ACTION_KEY_USERS = "users";
    private static final java.lang.String GLOBAL_ACTION_KEY_VOICEASSIST = "voiceassist";
    private static final int MESSAGE_DISMISS = 0;
    private static final int MESSAGE_REFRESH = 1;
    private static final int MESSAGE_SHOW = 2;
    private static final boolean SHOW_SILENT_TOGGLE = true;
    private static final java.lang.String TAG = "LegacyGlobalActions";
    private com.android.internal.globalactions.ActionsAdapter mAdapter;
    private com.android.internal.globalactions.ToggleAction mAirplaneModeOn;
    private final android.media.AudioManager mAudioManager;
    private final android.content.Context mContext;
    private com.android.internal.globalactions.ActionsDialog mDialog;
    private final com.android.internal.util.EmergencyAffordanceManager mEmergencyAffordanceManager;
    private final boolean mHasTelephony;
    private boolean mHasVibrator;
    private java.util.ArrayList<com.android.internal.globalactions.Action> mItems;
    private final java.lang.Runnable mOnDismiss;
    private final boolean mShowSilentToggle;
    private com.android.internal.globalactions.Action mSilentModeAction;
    private final com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs mWindowManagerFuncs;
    private boolean mKeyguardShowing = false;
    private boolean mDeviceProvisioned = false;
    private com.android.internal.globalactions.ToggleAction.State mAirplaneState = com.android.internal.globalactions.ToggleAction.State.Off;
    private boolean mIsWaitingForEcmExit = false;
    private android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.policy.LegacyGlobalActions.9
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action) || "android.intent.action.SCREEN_OFF".equals(action)) {
                java.lang.String reason = intent.getStringExtra(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY);
                if (!com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS.equals(reason)) {
                    com.android.server.policy.LegacyGlobalActions.this.mHandler.sendEmptyMessage(0);
                    return;
                }
                return;
            }
            if ("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED".equals(action) && !intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false) && com.android.server.policy.LegacyGlobalActions.this.mIsWaitingForEcmExit) {
                com.android.server.policy.LegacyGlobalActions.this.mIsWaitingForEcmExit = false;
                com.android.server.policy.LegacyGlobalActions.this.changeAirplaneModeSystemSetting(true);
            }
        }
    };
    android.telephony.PhoneStateListener mPhoneStateListener = new android.telephony.PhoneStateListener() { // from class: com.android.server.policy.LegacyGlobalActions.10
        @Override // android.telephony.PhoneStateListener
        public void onServiceStateChanged(android.telephony.ServiceState serviceState) {
            if (com.android.server.policy.LegacyGlobalActions.this.mHasTelephony) {
                boolean inAirplaneMode = serviceState.getState() == 3;
                com.android.server.policy.LegacyGlobalActions.this.mAirplaneState = inAirplaneMode ? com.android.internal.globalactions.ToggleAction.State.On : com.android.internal.globalactions.ToggleAction.State.Off;
                com.android.server.policy.LegacyGlobalActions.this.mAirplaneModeOn.updateState(com.android.server.policy.LegacyGlobalActions.this.mAirplaneState);
                com.android.server.policy.LegacyGlobalActions.this.mAdapter.notifyDataSetChanged();
            }
        }
    };
    private android.content.BroadcastReceiver mRingerModeReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.policy.LegacyGlobalActions.11
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
                com.android.server.policy.LegacyGlobalActions.this.mHandler.sendEmptyMessage(1);
            }
        }
    };
    private android.database.ContentObserver mAirplaneModeObserver = new android.database.ContentObserver(new android.os.Handler()) { // from class: com.android.server.policy.LegacyGlobalActions.12
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            com.android.server.policy.LegacyGlobalActions.this.onAirplaneModeChanged();
        }
    };
    private android.os.Handler mHandler = new android.os.Handler() { // from class: com.android.server.policy.LegacyGlobalActions.13
        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (com.android.server.policy.LegacyGlobalActions.this.mDialog != null) {
                        com.android.server.policy.LegacyGlobalActions.this.mDialog.dismiss();
                        com.android.server.policy.LegacyGlobalActions.this.mDialog = null;
                    }
                    break;
                case 1:
                    com.android.server.policy.LegacyGlobalActions.this.refreshSilentMode();
                    com.android.server.policy.LegacyGlobalActions.this.mAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    com.android.server.policy.LegacyGlobalActions.this.handleShow();
                    break;
            }
        }
    };
    private final android.service.dreams.IDreamManager mDreamManager = android.service.dreams.IDreamManager.Stub.asInterface(android.os.ServiceManager.getService("dreams"));

    public LegacyGlobalActions(android.content.Context context, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs, java.lang.Runnable onDismiss) {
        boolean z = false;
        this.mContext = context;
        this.mWindowManagerFuncs = windowManagerFuncs;
        this.mOnDismiss = onDismiss;
        this.mAudioManager = (android.media.AudioManager) this.mContext.getSystemService("audio");
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        context.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, filter, null, null, 2);
        this.mHasTelephony = context.getPackageManager().hasSystemFeature("android.hardware.telephony");
        android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) context.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
        telephonyManager.listen(this.mPhoneStateListener, 1);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("airplane_mode_on"), true, this.mAirplaneModeObserver);
        android.os.Vibrator vibrator = (android.os.Vibrator) this.mContext.getSystemService("vibrator");
        if (vibrator != null && vibrator.hasVibrator()) {
            z = true;
        }
        this.mHasVibrator = z;
        this.mShowSilentToggle = !this.mContext.getResources().getBoolean(android.R.bool.config_supportsMultiDisplay);
        this.mEmergencyAffordanceManager = new com.android.internal.util.EmergencyAffordanceManager(context);
    }

    public void showDialog(boolean keyguardShowing, boolean isDeviceProvisioned) {
        this.mKeyguardShowing = keyguardShowing;
        this.mDeviceProvisioned = isDeviceProvisioned;
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
            this.mHandler.sendEmptyMessage(2);
            return;
        }
        handleShow();
    }

    private void awakenIfNecessary() {
        if (this.mDreamManager != null) {
            try {
                if (this.mDreamManager.isDreaming()) {
                    this.mDreamManager.awaken();
                }
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleShow() {
        awakenIfNecessary();
        this.mDialog = createDialog();
        prepareDialog();
        if (this.mAdapter.getCount() == 1 && (this.mAdapter.getItem(0) instanceof com.android.internal.globalactions.SinglePressAction) && !(this.mAdapter.getItem(0) instanceof com.android.internal.globalactions.LongPressAction)) {
            this.mAdapter.getItem(0).onPress();
            return;
        }
        if (this.mDialog != null) {
            android.view.WindowManager.LayoutParams attrs = this.mDialog.getWindow().getAttributes();
            attrs.setTitle(TAG);
            this.mDialog.getWindow().setAttributes(attrs);
            this.mDialog.show();
            this.mDialog.getWindow().getDecorView().setSystemUiVisibility(65536);
        }
    }

    private com.android.internal.globalactions.ActionsDialog createDialog() {
        if (!this.mHasVibrator) {
            this.mSilentModeAction = new com.android.server.policy.LegacyGlobalActions.SilentModeToggleAction();
        } else {
            this.mSilentModeAction = new com.android.server.policy.LegacyGlobalActions.SilentModeTriStateAction(this.mContext, this.mAudioManager, this.mHandler);
        }
        this.mAirplaneModeOn = new com.android.internal.globalactions.ToggleAction(android.R.drawable.ic_doc_presentation, android.R.drawable.ic_doc_text, android.R.string.global_actions_airplane_mode_off_status, android.R.string.global_actions, android.R.string.global_action_voice_assist) { // from class: com.android.server.policy.LegacyGlobalActions.1
            public void onToggle(boolean on) {
                if (com.android.server.policy.LegacyGlobalActions.this.mHasTelephony && ((java.lang.Boolean) android.sysprop.TelephonyProperties.in_ecm_mode().orElse(false)).booleanValue()) {
                    com.android.server.policy.LegacyGlobalActions.this.mIsWaitingForEcmExit = true;
                    android.content.Intent ecmDialogIntent = new android.content.Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS", (android.net.Uri) null);
                    ecmDialogIntent.addFlags(268435456);
                    com.android.server.policy.LegacyGlobalActions.this.mContext.startActivity(ecmDialogIntent);
                    return;
                }
                com.android.server.policy.LegacyGlobalActions.this.changeAirplaneModeSystemSetting(on);
            }

            protected void changeStateFromPress(boolean buttonOn) {
                if (com.android.server.policy.LegacyGlobalActions.this.mHasTelephony && !((java.lang.Boolean) android.sysprop.TelephonyProperties.in_ecm_mode().orElse(false)).booleanValue()) {
                    this.mState = buttonOn ? com.android.internal.globalactions.ToggleAction.State.TurningOn : com.android.internal.globalactions.ToggleAction.State.TurningOff;
                    com.android.server.policy.LegacyGlobalActions.this.mAirplaneState = this.mState;
                }
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public boolean showBeforeProvisioning() {
                return false;
            }
        };
        onAirplaneModeChanged();
        this.mItems = new java.util.ArrayList<>();
        java.lang.String[] defaultActions = this.mContext.getResources().getStringArray(android.R.array.config_forceSlowJpegModeList);
        android.util.ArraySet<java.lang.String> addedKeys = new android.util.ArraySet<>();
        for (java.lang.String actionKey : defaultActions) {
            if (!addedKeys.contains(actionKey)) {
                if (GLOBAL_ACTION_KEY_POWER.equals(actionKey)) {
                    this.mItems.add(new com.android.server.policy.PowerAction(this.mContext, this.mWindowManagerFuncs));
                } else if (GLOBAL_ACTION_KEY_AIRPLANE.equals(actionKey)) {
                    this.mItems.add(this.mAirplaneModeOn);
                } else if (GLOBAL_ACTION_KEY_BUGREPORT.equals(actionKey)) {
                    this.mItems.add(new com.android.server.policy.LegacyGlobalActions.BugReportAction());
                } else if (GLOBAL_ACTION_KEY_SILENT.equals(actionKey)) {
                    if (this.mShowSilentToggle) {
                        this.mItems.add(this.mSilentModeAction);
                    }
                } else if ("users".equals(actionKey)) {
                    if (android.os.SystemProperties.getBoolean("fw.power_user_switcher", false)) {
                        addUsersToMenu(this.mItems);
                    }
                } else if (GLOBAL_ACTION_KEY_SETTINGS.equals(actionKey)) {
                    this.mItems.add(getSettingsAction());
                } else if (GLOBAL_ACTION_KEY_LOCKDOWN.equals(actionKey)) {
                    this.mItems.add(getLockdownAction());
                } else if (GLOBAL_ACTION_KEY_VOICEASSIST.equals(actionKey)) {
                    this.mItems.add(getVoiceAssistAction());
                } else if ("assist".equals(actionKey)) {
                    this.mItems.add(getAssistAction());
                } else if ("restart".equals(actionKey)) {
                    this.mItems.add(new com.android.server.policy.RestartAction(this.mContext, this.mWindowManagerFuncs));
                } else {
                    android.util.Log.e(TAG, "Invalid global action key " + actionKey);
                }
                addedKeys.add(actionKey);
            }
        }
        if (this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            this.mItems.add(getEmergencyAction());
        }
        this.mAdapter = new com.android.internal.globalactions.ActionsAdapter(this.mContext, this.mItems, new java.util.function.BooleanSupplier() { // from class: com.android.server.policy.LegacyGlobalActions$$ExternalSyntheticLambda0
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return this.f$0.lambda$createDialog$0();
            }
        }, new java.util.function.BooleanSupplier() { // from class: com.android.server.policy.LegacyGlobalActions$$ExternalSyntheticLambda1
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return this.f$0.lambda$createDialog$1();
            }
        });
        com.android.internal.app.AlertController.AlertParams params = new com.android.internal.app.AlertController.AlertParams(this.mContext);
        params.mAdapter = this.mAdapter;
        params.mOnClickListener = this;
        params.mForceInverseBackground = true;
        com.android.internal.globalactions.ActionsDialog dialog = new com.android.internal.globalactions.ActionsDialog(this.mContext, params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getListView().setItemsCanFocus(true);
        dialog.getListView().setLongClickable(true);
        dialog.getListView().setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() { // from class: com.android.server.policy.LegacyGlobalActions.2
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                com.android.internal.globalactions.LongPressAction item = com.android.server.policy.LegacyGlobalActions.this.mAdapter.getItem(position);
                if (item instanceof com.android.internal.globalactions.LongPressAction) {
                    return item.onLongPress();
                }
                return false;
            }
        });
        dialog.getWindow().setType(2009);
        dialog.getWindow().setFlags(131072, 131072);
        dialog.setOnDismissListener(this);
        return dialog;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createDialog$0() {
        return this.mDeviceProvisioned;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createDialog$1() {
        return this.mKeyguardShowing;
    }

    private class BugReportAction extends com.android.internal.globalactions.SinglePressAction implements com.android.internal.globalactions.LongPressAction {
        public BugReportAction() {
            super(android.R.drawable.ic_doc_word, android.R.string.call_notification_ongoing_text);
        }

        public void onPress() {
            if (android.app.ActivityManager.isUserAMonkey()) {
                return;
            }
            com.android.server.policy.LegacyGlobalActions.this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.policy.LegacyGlobalActions.BugReportAction.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        com.android.internal.logging.MetricsLogger.action(com.android.server.policy.LegacyGlobalActions.this.mContext, 292);
                        android.app.ActivityManager.getService().requestInteractiveBugReport();
                    } catch (android.os.RemoteException e) {
                    }
                }
            }, 500L);
        }

        public boolean onLongPress() {
            if (android.app.ActivityManager.isUserAMonkey()) {
                return false;
            }
            try {
                com.android.internal.logging.MetricsLogger.action(com.android.server.policy.LegacyGlobalActions.this.mContext, 293);
                android.app.ActivityManager.getService().requestFullBugReport();
            } catch (android.os.RemoteException e) {
            }
            return false;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public boolean showBeforeProvisioning() {
            return false;
        }

        public java.lang.String getStatus() {
            return com.android.server.policy.LegacyGlobalActions.this.mContext.getString(android.R.string.call_notification_incoming_text, android.os.Build.VERSION.RELEASE_OR_CODENAME, android.os.Build.ID);
        }
    }

    private com.android.internal.globalactions.Action getSettingsAction() {
        return new com.android.internal.globalactions.SinglePressAction(android.R.drawable.ic_notification_summary_auto, android.R.string.global_action_screenshot) { // from class: com.android.server.policy.LegacyGlobalActions.3
            public void onPress() {
                android.content.Intent intent = new android.content.Intent("android.settings.SETTINGS");
                intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF);
                com.android.server.policy.LegacyGlobalActions.this.mContext.startActivity(intent);
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public boolean showBeforeProvisioning() {
                return true;
            }
        };
    }

    private com.android.internal.globalactions.Action getEmergencyAction() {
        return new com.android.internal.globalactions.SinglePressAction(android.R.drawable.dialog_ic_close_focused_holo_dark, android.R.string.global_action_bug_report) { // from class: com.android.server.policy.LegacyGlobalActions.4
            public void onPress() {
                com.android.server.policy.LegacyGlobalActions.this.mEmergencyAffordanceManager.performEmergencyCall();
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public boolean showBeforeProvisioning() {
                return true;
            }
        };
    }

    private com.android.internal.globalactions.Action getAssistAction() {
        return new com.android.internal.globalactions.SinglePressAction(android.R.drawable.fastscroll_track_pressed_holo_light, android.R.string.geofencing_service) { // from class: com.android.server.policy.LegacyGlobalActions.5
            public void onPress() {
                android.content.Intent intent = new android.content.Intent("android.intent.action.ASSIST");
                intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF);
                com.android.server.policy.LegacyGlobalActions.this.mContext.startActivity(intent);
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public boolean showBeforeProvisioning() {
                return true;
            }
        };
    }

    private com.android.internal.globalactions.Action getVoiceAssistAction() {
        return new com.android.internal.globalactions.SinglePressAction(android.R.drawable.ic_qs_night_display_on, android.R.string.global_action_standby) { // from class: com.android.server.policy.LegacyGlobalActions.6
            public void onPress() {
                android.content.Intent intent = new android.content.Intent("android.intent.action.VOICE_ASSIST");
                intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF);
                com.android.server.policy.LegacyGlobalActions.this.mContext.startActivity(intent);
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public boolean showBeforeProvisioning() {
                return true;
            }
        };
    }

    private com.android.internal.globalactions.Action getLockdownAction() {
        return new com.android.internal.globalactions.SinglePressAction(android.R.drawable.ic_lock_lock, android.R.string.global_action_lock) { // from class: com.android.server.policy.LegacyGlobalActions.7
            public void onPress() {
                new com.android.internal.widget.LockPatternUtils(com.android.server.policy.LegacyGlobalActions.this.mContext).requireCredentialEntry(-1);
                try {
                    android.view.WindowManagerGlobal.getWindowManagerService().lockNow((android.os.Bundle) null);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.policy.LegacyGlobalActions.TAG, "Error while trying to lock device.", e);
                }
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public boolean showBeforeProvisioning() {
                return false;
            }
        };
    }

    private android.content.pm.UserInfo getCurrentUser() {
        try {
            return android.app.ActivityManager.getService().getCurrentUser();
        } catch (android.os.RemoteException e) {
            return null;
        }
    }

    private boolean isCurrentUserAdmin() {
        android.content.pm.UserInfo currentUser = getCurrentUser();
        return currentUser != null && currentUser.isAdmin();
    }

    private void addUsersToMenu(java.util.ArrayList<com.android.internal.globalactions.Action> items) {
        android.os.UserManager um = (android.os.UserManager) this.mContext.getSystemService("user");
        if (um.isUserSwitcherEnabled()) {
            java.util.List<android.content.pm.UserInfo> users = um.getUsers();
            android.content.pm.UserInfo currentUser = getCurrentUser();
            for (final android.content.pm.UserInfo user : users) {
                if (user.supportsSwitchToByUser()) {
                    boolean z = true;
                    if (currentUser != null ? currentUser.id != user.id : user.id != 0) {
                        z = false;
                    }
                    boolean isCurrentUser = z;
                    android.graphics.drawable.Drawable icon = user.iconPath != null ? android.graphics.drawable.Drawable.createFromPath(user.iconPath) : null;
                    com.android.internal.globalactions.SinglePressAction switchToUser = new com.android.internal.globalactions.SinglePressAction(android.R.drawable.ic_media_route_connecting_light_03_mtrl, icon, (user.name != null ? user.name : "Primary") + (isCurrentUser ? " ✔" : "")) { // from class: com.android.server.policy.LegacyGlobalActions.8
                        public void onPress() {
                            try {
                                android.app.ActivityManager.getService().switchUser(user.id);
                            } catch (android.os.RemoteException re) {
                                android.util.Log.e(com.android.server.policy.LegacyGlobalActions.TAG, "Couldn't switch user " + re);
                            }
                        }

                        public boolean showDuringKeyguard() {
                            return true;
                        }

                        public boolean showBeforeProvisioning() {
                            return false;
                        }
                    };
                    items.add(switchToUser);
                }
            }
        }
    }

    private void prepareDialog() {
        refreshSilentMode();
        this.mAirplaneModeOn.updateState(this.mAirplaneState);
        this.mAdapter.notifyDataSetChanged();
        this.mDialog.getWindow().setType(2009);
        if (this.mShowSilentToggle) {
            android.content.IntentFilter filter = new android.content.IntentFilter("android.media.RINGER_MODE_CHANGED");
            this.mContext.registerReceiver(this.mRingerModeReceiver, filter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSilentMode() {
        if (!this.mHasVibrator) {
            boolean silentModeOn = this.mAudioManager.getRingerMode() != 2;
            this.mSilentModeAction.updateState(silentModeOn ? com.android.internal.globalactions.ToggleAction.State.On : com.android.internal.globalactions.ToggleAction.State.Off);
        }
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(android.content.DialogInterface dialog) {
        if (this.mOnDismiss != null) {
            this.mOnDismiss.run();
        }
        if (this.mShowSilentToggle) {
            try {
                this.mContext.unregisterReceiver(this.mRingerModeReceiver);
            } catch (java.lang.IllegalArgumentException ie) {
                android.util.Log.w(TAG, ie);
            }
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(android.content.DialogInterface dialog, int which) {
        if (!(this.mAdapter.getItem(which) instanceof com.android.server.policy.LegacyGlobalActions.SilentModeTriStateAction)) {
            dialog.dismiss();
        }
        this.mAdapter.getItem(which).onPress();
    }

    private class SilentModeToggleAction extends com.android.internal.globalactions.ToggleAction {
        public SilentModeToggleAction() {
            super(android.R.drawable.global_actions_item_grey_background_shape, android.R.drawable.global_actions_item_grey_background, android.R.string.global_action_silent_mode_on_status, android.R.string.global_action_silent_mode_off_status, android.R.string.global_action_settings);
        }

        public void onToggle(boolean on) {
            if (on) {
                com.android.server.policy.LegacyGlobalActions.this.mAudioManager.setRingerMode(0);
            } else {
                com.android.server.policy.LegacyGlobalActions.this.mAudioManager.setRingerMode(2);
            }
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public boolean showBeforeProvisioning() {
            return false;
        }
    }

    private static class SilentModeTriStateAction implements com.android.internal.globalactions.Action, android.view.View.OnClickListener {
        private final int[] ITEM_IDS = {android.R.id.nonav, android.R.id.none, android.R.id.normal};
        private final android.media.AudioManager mAudioManager;
        private final android.content.Context mContext;
        private final android.os.Handler mHandler;

        SilentModeTriStateAction(android.content.Context context, android.media.AudioManager audioManager, android.os.Handler handler) {
            this.mAudioManager = audioManager;
            this.mHandler = handler;
            this.mContext = context;
        }

        private int ringerModeToIndex(int ringerMode) {
            return ringerMode;
        }

        private int indexToRingerMode(int index) {
            return index;
        }

        public java.lang.CharSequence getLabelForAccessibility(android.content.Context context) {
            return null;
        }

        public android.view.View create(android.content.Context context, android.view.View convertView, android.view.ViewGroup parent, android.view.LayoutInflater inflater) {
            android.view.View v = inflater.inflate(android.R.layout.fragment_bread_crumbs, parent, false);
            int selectedIndex = ringerModeToIndex(this.mAudioManager.getRingerMode());
            int i = 0;
            while (i < 3) {
                android.view.View itemView = v.findViewById(this.ITEM_IDS[i]);
                itemView.setSelected(selectedIndex == i);
                itemView.setTag(java.lang.Integer.valueOf(i));
                itemView.setOnClickListener(this);
                i++;
            }
            return v;
        }

        public void onPress() {
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public boolean showBeforeProvisioning() {
            return false;
        }

        public boolean isEnabled() {
            return true;
        }

        void willCreate() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(android.view.View v) {
            if (v.getTag() instanceof java.lang.Integer) {
                int index = ((java.lang.Integer) v.getTag()).intValue();
                this.mAudioManager.setRingerMode(indexToRingerMode(index));
                this.mHandler.sendEmptyMessageDelayed(0, 300L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAirplaneModeChanged() {
        if (this.mHasTelephony) {
            return;
        }
        boolean airplaneModeOn = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        this.mAirplaneState = airplaneModeOn ? com.android.internal.globalactions.ToggleAction.State.On : com.android.internal.globalactions.ToggleAction.State.Off;
        this.mAirplaneModeOn.updateState(this.mAirplaneState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeAirplaneModeSystemSetting(boolean z) {
        android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "airplane_mode_on", z ? 1 : 0);
        android.content.Intent intent = new android.content.Intent("android.intent.action.AIRPLANE_MODE");
        intent.addFlags(536870912);
        intent.putExtra("state", z);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
        if (!this.mHasTelephony) {
            this.mAirplaneState = z ? com.android.internal.globalactions.ToggleAction.State.On : com.android.internal.globalactions.ToggleAction.State.Off;
        }
    }
}
