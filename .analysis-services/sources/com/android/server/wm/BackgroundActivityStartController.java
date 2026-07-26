package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class BackgroundActivityStartController {
    public static final android.app.ActivityOptions ACTIVITY_OPTIONS_SYSTEM_DEFINED = android.app.ActivityOptions.makeBasic().setPendingIntentBackgroundActivityStartMode(0).setPendingIntentCreatorBackgroundActivityStartMode(0);
    private static final int ASM_GRACEPERIOD_MAX_REPEATS = 5;
    private static final long ASM_GRACEPERIOD_TIMEOUT_MS = 3000;
    static final java.lang.String AUTO_OPT_IN_CALL_FOR_RESULT = "callForResult";
    static final java.lang.String AUTO_OPT_IN_COMPAT = "compatibility";
    static final java.lang.String AUTO_OPT_IN_NOT_PENDING_INTENT = "notPendingIntent";
    static final java.lang.String AUTO_OPT_IN_SAME_UID = "sameUid";
    static final int BAL_ALLOW_ALLOWLISTED_COMPONENT = 3;
    static final int BAL_ALLOW_ALLOWLISTED_UID = 2;
    static final int BAL_ALLOW_DEFAULT = 1;
    static final int BAL_ALLOW_FOREGROUND = 9;
    static final int BAL_ALLOW_GRACE_PERIOD = 8;
    static final int BAL_ALLOW_NON_APP_VISIBLE_WINDOW = 11;
    static final int BAL_ALLOW_PENDING_INTENT = 5;
    static final int BAL_ALLOW_PERMISSION = 6;
    static final int BAL_ALLOW_SAW_PERMISSION = 7;
    static final int BAL_ALLOW_SDK_SANDBOX = 10;
    static final int BAL_ALLOW_VISIBLE_WINDOW = 4;
    static final int BAL_BLOCK = 0;
    private static final long DEFAULT_RESCIND_BAL_PRIVILEGES_FROM_PENDING_INTENT_CREATOR = 296478951;
    private static final int NO_PROCESS_UID = -1;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    private com.android.server.wm.IBackgroundActivityStartControllerExt mBackgroundActivityStartControllerExt = (com.android.server.wm.IBackgroundActivityStartControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IBackgroundActivityStartControllerExt.class).base(this).create();
    private final java.util.HashMap<java.lang.Integer, com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry> mTaskIdToFinishedActivity = new java.util.HashMap<>();
    private com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry mTopFinishedActivity = null;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface BalCode {
    }

    static java.lang.String balCodeToString(int balCode) {
        switch (balCode) {
            case 0:
                return "BAL_BLOCK";
            case 1:
                return "BAL_ALLOW_DEFAULT";
            case 2:
                return "BAL_ALLOW_ALLOWLISTED_UID";
            case 3:
                return "BAL_ALLOW_ALLOWLISTED_COMPONENT";
            case 4:
                return "BAL_ALLOW_VISIBLE_WINDOW";
            case 5:
                return "BAL_ALLOW_PENDING_INTENT";
            case 6:
                return "BAL_ALLOW_PERMISSION";
            case 7:
                return "BAL_ALLOW_SAW_PERMISSION";
            case 8:
                return "BAL_ALLOW_GRACE_PERIOD";
            case 9:
                return "BAL_ALLOW_FOREGROUND";
            case 10:
                return "BAL_ALLOW_SDK_SANDBOX";
            case 11:
                return "BAL_ALLOW_NON_APP_VISIBLE_WINDOW";
            default:
                throw new java.lang.IllegalArgumentException("Unexpected value: " + balCode);
        }
    }

    BackgroundActivityStartController(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.ActivityTaskSupervisor supervisor) {
        this.mService = service;
        this.mSupervisor = supervisor;
    }

    private boolean isHomeApp(int uid, java.lang.String packageName) {
        if (this.mService.mHomeProcess != null) {
            return uid == this.mService.mHomeProcess.mUid;
        }
        if (packageName == null) {
            return false;
        }
        android.content.ComponentName activity = this.mService.getPackageManagerInternalLocked().getDefaultHomeActivity(android.os.UserHandle.getUserId(uid));
        return activity != null && packageName.equals(activity.getPackageName());
    }

    class BalState {
        private final int mAppSwitchState;
        private final boolean mAutoOptInCaller;
        final java.lang.String mAutoOptInReason;
        final android.app.BackgroundStartPrivileges mBalAllowedByPiCreator;
        final android.app.BackgroundStartPrivileges mBalAllowedByPiCreatorWithHardening;
        final android.app.BackgroundStartPrivileges mBalAllowedByPiSender;
        private final com.android.server.wm.WindowProcessController mCallerApp;
        private final java.lang.String mCallingPackage;
        private final int mCallingPid;
        private final int mCallingUid;
        private final boolean mCallingUidHasAnyVisibleWindow;
        private final int mCallingUidProcState;
        private final android.app.ActivityOptions mCheckedOptions;
        private final android.app.BackgroundStartPrivileges mForcedBalByPiSender;
        private final android.content.Intent mIntent;
        private final boolean mIsCallForResult;
        private final boolean mIsCallingUidPersistentSystemProcess;
        private final boolean mIsRealCallingUidPersistentSystemProcess;
        private final com.android.server.am.PendingIntentRecord mOriginatingPendingIntent;
        private final com.android.server.wm.WindowProcessController mRealCallerApp;
        private final java.lang.String mRealCallingPackage;
        private final int mRealCallingPid;
        private final int mRealCallingUid;
        private final boolean mRealCallingUidHasAnyVisibleWindow;
        private final int mRealCallingUidProcState;
        private com.android.server.wm.BackgroundActivityStartController.BalVerdict mResultForCaller;
        private com.android.server.wm.BackgroundActivityStartController.BalVerdict mResultForRealCaller;

        BalState(int callingUid, int callingPid, java.lang.String callingPackage, int realCallingUid, int realCallingPid, com.android.server.wm.WindowProcessController callerApp, com.android.server.am.PendingIntentRecord originatingPendingIntent, android.app.BackgroundStartPrivileges forcedBalByPiSender, com.android.server.wm.ActivityRecord resultRecord, android.content.Intent intent, android.app.ActivityOptions checkedOptions) {
            android.app.BackgroundStartPrivileges mBalAllowedByPiCreatorWithoutHardening;
            android.app.BackgroundStartPrivileges backgroundStartPrivileges;
            com.android.server.wm.WindowProcessController processController;
            android.app.BackgroundStartPrivileges backgroundStartPrivileges2;
            android.app.BackgroundStartPrivileges backgroundStartPrivileges3;
            this.mCallingPackage = callingPackage;
            this.mCallingUid = callingUid;
            this.mCallingPid = callingPid;
            this.mRealCallingUid = realCallingUid;
            this.mRealCallingPid = realCallingPid;
            this.mCallerApp = callerApp;
            this.mForcedBalByPiSender = forcedBalByPiSender;
            this.mOriginatingPendingIntent = originatingPendingIntent;
            this.mIntent = intent;
            this.mRealCallingPackage = com.android.server.wm.BackgroundActivityStartController.this.mService.getPackageNameIfUnique(realCallingUid, realCallingPid);
            this.mIsCallForResult = resultRecord != null;
            this.mCheckedOptions = checkedOptions;
            int callerBackgroundActivityStartMode = checkedOptions.getPendingIntentCreatorBackgroundActivityStartMode();
            int realCallerBackgroundActivityStartMode = checkedOptions.getPendingIntentBackgroundActivityStartMode();
            if (!com.android.window.flags.Flags.balImproveRealCallerVisibilityCheck()) {
                this.mAutoOptInReason = null;
                this.mAutoOptInCaller = false;
            } else if (originatingPendingIntent == null) {
                this.mAutoOptInReason = com.android.server.wm.BackgroundActivityStartController.AUTO_OPT_IN_NOT_PENDING_INTENT;
                this.mAutoOptInCaller = true;
            } else if (this.mIsCallForResult) {
                this.mAutoOptInReason = com.android.server.wm.BackgroundActivityStartController.AUTO_OPT_IN_CALL_FOR_RESULT;
                this.mAutoOptInCaller = false;
            } else if (callingUid == realCallingUid && !com.android.window.flags.Flags.balRequireOptInSameUid()) {
                this.mAutoOptInReason = com.android.server.wm.BackgroundActivityStartController.AUTO_OPT_IN_SAME_UID;
                this.mAutoOptInCaller = false;
            } else if (realCallerBackgroundActivityStartMode == -1) {
                this.mAutoOptInReason = com.android.server.wm.BackgroundActivityStartController.AUTO_OPT_IN_COMPAT;
                this.mAutoOptInCaller = false;
            } else {
                this.mAutoOptInReason = null;
                this.mAutoOptInCaller = false;
            }
            if (!this.mAutoOptInCaller) {
                this.mBalAllowedByPiCreatorWithHardening = getBackgroundStartPrivilegesAllowedByCreator(callingUid, callingPackage, checkedOptions);
                if (callerBackgroundActivityStartMode == 2) {
                    mBalAllowedByPiCreatorWithoutHardening = android.app.BackgroundStartPrivileges.NONE;
                } else {
                    mBalAllowedByPiCreatorWithoutHardening = android.app.BackgroundStartPrivileges.ALLOW_BAL;
                }
                if (com.android.window.flags.Flags.balRequireOptInByPendingIntentCreator()) {
                    backgroundStartPrivileges = this.mBalAllowedByPiCreatorWithHardening;
                } else {
                    backgroundStartPrivileges = mBalAllowedByPiCreatorWithoutHardening;
                }
                this.mBalAllowedByPiCreator = backgroundStartPrivileges;
            } else {
                if (callerBackgroundActivityStartMode == 2) {
                    backgroundStartPrivileges3 = android.app.BackgroundStartPrivileges.NONE;
                } else {
                    backgroundStartPrivileges3 = android.app.BackgroundStartPrivileges.ALLOW_BAL;
                }
                this.mBalAllowedByPiCreator = backgroundStartPrivileges3;
                this.mBalAllowedByPiCreatorWithHardening = backgroundStartPrivileges3;
            }
            if (this.mAutoOptInReason == null) {
                this.mBalAllowedByPiSender = com.android.server.am.PendingIntentRecord.getBackgroundStartPrivilegesAllowedByCaller(checkedOptions, realCallingUid, this.mRealCallingPackage);
            } else {
                if (realCallerBackgroundActivityStartMode == 2) {
                    backgroundStartPrivileges2 = android.app.BackgroundStartPrivileges.NONE;
                } else {
                    backgroundStartPrivileges2 = android.app.BackgroundStartPrivileges.ALLOW_BAL;
                }
                this.mBalAllowedByPiSender = backgroundStartPrivileges2;
            }
            this.mAppSwitchState = com.android.server.wm.BackgroundActivityStartController.this.mService.getBalAppSwitchesState();
            this.mCallingUidProcState = com.android.server.wm.BackgroundActivityStartController.this.mService.mActiveUids.getUidState(callingUid);
            this.mIsCallingUidPersistentSystemProcess = this.mCallingUidProcState <= 1;
            this.mCallingUidHasAnyVisibleWindow = com.android.server.wm.BackgroundActivityStartController.this.mService.hasActiveVisibleWindow(callingUid);
            if (realCallingUid == -1) {
                this.mRealCallingUidProcState = 20;
                this.mRealCallingUidHasAnyVisibleWindow = false;
                this.mRealCallerApp = null;
                this.mIsRealCallingUidPersistentSystemProcess = false;
                return;
            }
            if (callingUid == realCallingUid) {
                this.mRealCallingUidProcState = this.mCallingUidProcState;
                this.mRealCallingUidHasAnyVisibleWindow = this.mCallingUidHasAnyVisibleWindow;
                if (callerApp == null) {
                    processController = com.android.server.wm.BackgroundActivityStartController.this.mService.getProcessController(realCallingPid, realCallingUid);
                } else {
                    processController = callerApp;
                }
                this.mRealCallerApp = processController;
                this.mIsRealCallingUidPersistentSystemProcess = this.mIsCallingUidPersistentSystemProcess;
                return;
            }
            this.mRealCallingUidProcState = com.android.server.wm.BackgroundActivityStartController.this.mService.mActiveUids.getUidState(realCallingUid);
            this.mRealCallingUidHasAnyVisibleWindow = com.android.server.wm.BackgroundActivityStartController.this.mService.hasActiveVisibleWindow(realCallingUid);
            this.mRealCallerApp = com.android.server.wm.BackgroundActivityStartController.this.mService.getProcessController(realCallingPid, realCallingUid);
            this.mIsRealCallingUidPersistentSystemProcess = this.mRealCallingUidProcState <= 1;
        }

        private android.app.BackgroundStartPrivileges getBackgroundStartPrivilegesAllowedByCreator(int callingUid, java.lang.String callingPackage, android.app.ActivityOptions checkedOptions) {
            switch (checkedOptions.getPendingIntentCreatorBackgroundActivityStartMode()) {
                case 0:
                    if (callingUid == 0 || callingUid == 1000) {
                        return android.app.BackgroundStartPrivileges.NONE;
                    }
                    if (callingPackage != null) {
                        boolean changeEnabled = android.app.compat.CompatChanges.isChangeEnabled(com.android.server.wm.BackgroundActivityStartController.DEFAULT_RESCIND_BAL_PRIVILEGES_FROM_PENDING_INTENT_CREATOR, callingPackage, android.os.UserHandle.getUserHandleForUid(callingUid));
                        return changeEnabled ? android.app.BackgroundStartPrivileges.NONE : android.app.BackgroundStartPrivileges.ALLOW_BAL;
                    }
                    boolean changeEnabled2 = android.app.compat.CompatChanges.isChangeEnabled(com.android.server.wm.BackgroundActivityStartController.DEFAULT_RESCIND_BAL_PRIVILEGES_FROM_PENDING_INTENT_CREATOR, callingUid);
                    return changeEnabled2 ? android.app.BackgroundStartPrivileges.NONE : android.app.BackgroundStartPrivileges.ALLOW_BAL;
                case 1:
                    return android.app.BackgroundStartPrivileges.ALLOW_BAL;
                case 2:
                    return android.app.BackgroundStartPrivileges.NONE;
                default:
                    throw new java.lang.IllegalStateException("unsupported BackgroundActivityStartMode: " + checkedOptions.getPendingIntentCreatorBackgroundActivityStartMode());
            }
        }

        private java.lang.String getDebugPackageName(java.lang.String packageName, int uid) {
            if (packageName != null) {
                return packageName;
            }
            if (uid == 0) {
                return "root[debugOnly]";
            }
            java.lang.String name = com.android.server.wm.BackgroundActivityStartController.this.mService.getPackageManagerInternalLocked().getNameForUid(uid);
            if (name == null) {
                name = "uid=" + uid;
            }
            return name + "[debugOnly]";
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasRealCaller() {
            return this.mRealCallingUid != -1;
        }

        boolean isPendingIntent() {
            return this.mOriginatingPendingIntent != null && hasRealCaller();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean callerIsRealCaller() {
            return this.mCallingUid == this.mRealCallingUid;
        }

        public void setResultForCaller(com.android.server.wm.BackgroundActivityStartController.BalVerdict resultForCaller) {
            com.android.internal.util.Preconditions.checkState(this.mResultForCaller == null, "mResultForCaller can only be set once");
            this.mResultForCaller = resultForCaller;
        }

        public void setResultForRealCaller(com.android.server.wm.BackgroundActivityStartController.BalVerdict resultForRealCaller) {
            com.android.internal.util.Preconditions.checkState(this.mResultForRealCaller == null, "mResultForRealCaller can only be set once");
            this.mResultForRealCaller = resultForRealCaller;
        }

        public boolean isPendingIntentBalAllowedByPermission() {
            return com.android.server.am.PendingIntentRecord.isPendingIntentBalAllowedByPermission(this.mCheckedOptions);
        }

        public boolean callerExplicitOptInOrAutoOptIn() {
            if (this.mAutoOptInCaller) {
                return !callerExplicitOptOut();
            }
            return this.mCheckedOptions.getPendingIntentCreatorBackgroundActivityStartMode() == 1;
        }

        public boolean realCallerExplicitOptInOrAutoOptIn() {
            if (this.mAutoOptInReason != null) {
                return !realCallerExplicitOptOut();
            }
            return this.mCheckedOptions.getPendingIntentBackgroundActivityStartMode() == 1;
        }

        public boolean callerExplicitOptOut() {
            return this.mCheckedOptions.getPendingIntentCreatorBackgroundActivityStartMode() == 2;
        }

        public boolean realCallerExplicitOptOut() {
            return this.mCheckedOptions.getPendingIntentBackgroundActivityStartMode() == 2;
        }

        public boolean callerExplicitOptInOrOut() {
            return this.mCheckedOptions.getPendingIntentCreatorBackgroundActivityStartMode() != 0;
        }

        public boolean realCallerExplicitOptInOrOut() {
            return this.mCheckedOptions.getPendingIntentBackgroundActivityStartMode() != 0;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(2048);
            sb.append("[callingPackage: ").append(getDebugPackageName(this.mCallingPackage, this.mCallingUid));
            sb.append("; callingPackageTargetSdk: ").append(com.android.server.wm.BackgroundActivityStartController.this.getTargetSdk(this.mCallingPackage));
            sb.append("; callingUid: ").append(this.mCallingUid);
            sb.append("; callingPid: ").append(this.mCallingPid);
            sb.append("; appSwitchState: ").append(this.mAppSwitchState);
            sb.append("; callingUidHasAnyVisibleWindow: ").append(this.mCallingUidHasAnyVisibleWindow);
            sb.append("; callingUidProcState: ").append(android.util.DebugUtils.valueToString(android.app.ActivityManager.class, "PROCESS_STATE_", this.mCallingUidProcState));
            sb.append("; isCallingUidPersistentSystemProcess: ").append(this.mIsCallingUidPersistentSystemProcess);
            sb.append("; forcedBalByPiSender: ").append(this.mForcedBalByPiSender);
            sb.append("; intent: ").append(this.mIntent);
            sb.append("; callerApp: ").append(this.mCallerApp);
            if (this.mCallerApp != null) {
                sb.append("; inVisibleTask: ").append(this.mCallerApp.hasActivityInVisibleTask());
            }
            sb.append("; balAllowedByPiCreator: ").append(this.mBalAllowedByPiCreator);
            sb.append("; balAllowedByPiCreatorWithHardening: ").append(this.mBalAllowedByPiCreatorWithHardening);
            sb.append("; resultIfPiCreatorAllowsBal: ").append(this.mResultForCaller);
            sb.append("; hasRealCaller: ").append(hasRealCaller());
            sb.append("; isCallForResult: ").append(this.mIsCallForResult);
            sb.append("; isPendingIntent: ").append(isPendingIntent());
            sb.append("; autoOptInReason: ").append(this.mAutoOptInReason);
            if (hasRealCaller()) {
                sb.append("; realCallingPackage: ").append(getDebugPackageName(this.mRealCallingPackage, this.mRealCallingUid));
                sb.append("; realCallingPackageTargetSdk: ").append(com.android.server.wm.BackgroundActivityStartController.this.getTargetSdk(this.mRealCallingPackage));
                sb.append("; realCallingUid: ").append(this.mRealCallingUid);
                sb.append("; realCallingPid: ").append(this.mRealCallingPid);
                sb.append("; realCallingUidHasAnyVisibleWindow: ").append(this.mRealCallingUidHasAnyVisibleWindow);
                sb.append("; realCallingUidProcState: ").append(android.util.DebugUtils.valueToString(android.app.ActivityManager.class, "PROCESS_STATE_", this.mRealCallingUidProcState));
                sb.append("; isRealCallingUidPersistentSystemProcess: ").append(this.mIsRealCallingUidPersistentSystemProcess);
                sb.append("; originatingPendingIntent: ").append(this.mOriginatingPendingIntent);
                sb.append("; realCallerApp: ").append(this.mRealCallerApp);
                if (this.mRealCallerApp != null) {
                    sb.append("; realInVisibleTask: ").append(this.mRealCallerApp.hasActivityInVisibleTask());
                }
                sb.append("; balAllowedByPiSender: ").append(this.mBalAllowedByPiSender);
                sb.append("; resultIfPiSenderAllowsBal: ").append(this.mResultForRealCaller);
            }
            sb.append("; balImproveRealCallerVisibilityCheck: ").append(com.android.window.flags.Flags.balImproveRealCallerVisibilityCheck());
            sb.append("; balRequireOptInByPendingIntentCreator: ").append(com.android.window.flags.Flags.balRequireOptInByPendingIntentCreator());
            sb.append("; balRequireOptInSameUid: ").append(com.android.window.flags.Flags.balRequireOptInSameUid());
            sb.append("; balRespectAppSwitchStateWhenCheckBoundByForegroundUid: ").append(com.android.window.flags.Flags.balRespectAppSwitchStateWhenCheckBoundByForegroundUid());
            sb.append("; balDontBringExistingBackgroundTaskStackToFg: ").append(com.android.window.flags.Flags.balDontBringExistingBackgroundTaskStackToFg());
            sb.append("]");
            return sb.toString();
        }
    }

    static class BalVerdict {
        public static final int BLOCK_TYPE_ALLOW_GRACE_PERIOD = 1;
        public static final int BLOCK_TYPE_SAW_PERMISSION = 0;
        private int blockType = -1;
        private final boolean mBackground;
        private boolean mBasedOnRealCaller;
        private final int mCode;
        private final java.lang.String mMessage;
        private boolean mOnlyCreatorAllows;
        private java.lang.String mProcessInfo;
        static final com.android.server.wm.BackgroundActivityStartController.BalVerdict BLOCK = new com.android.server.wm.BackgroundActivityStartController.BalVerdict(0, false, "Blocked");
        static final com.android.server.wm.BackgroundActivityStartController.BalVerdict ALLOW_BY_DEFAULT = new com.android.server.wm.BackgroundActivityStartController.BalVerdict(1, false, "Default");
        static final com.android.server.wm.BackgroundActivityStartController.BalVerdict ALLOW_PRIVILEGED = new com.android.server.wm.BackgroundActivityStartController.BalVerdict(2, false, "PRIVILEGED");

        BalVerdict(int balCode, boolean background, java.lang.String message) {
            this.mBackground = background;
            this.mCode = balCode;
            this.mMessage = message;
        }

        public com.android.server.wm.BackgroundActivityStartController.BalVerdict withProcessInfo(java.lang.String msg, com.android.server.wm.WindowProcessController process) {
            this.mProcessInfo = msg + " (uid=" + process.mUid + ",pid=" + process.getPid() + ")";
            return this;
        }

        public void setBlockType(int blockType) {
            this.blockType = blockType;
        }

        public int getBlockType() {
            return this.blockType;
        }

        boolean blocks() {
            return this.mCode == 0;
        }

        boolean allows() {
            return !blocks();
        }

        void setOnlyCreatorAllows(boolean onlyCreatorAllows) {
            this.mOnlyCreatorAllows = onlyCreatorAllows;
        }

        boolean onlyCreatorAllows() {
            return this.mOnlyCreatorAllows;
        }

        com.android.server.wm.BackgroundActivityStartController.BalVerdict setBasedOnRealCaller() {
            this.mBasedOnRealCaller = true;
            return this;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append(com.android.server.wm.BackgroundActivityStartController.balCodeToString(this.mCode));
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ACTIVITY_STARTS) {
                builder.append(" (");
                if (this.mBackground) {
                    builder.append("Background ");
                }
                builder.append("Activity start ");
                if (this.mCode == 0) {
                    builder.append("denied");
                } else {
                    builder.append("allowed: ").append(this.mMessage);
                }
                if (this.mProcessInfo != null) {
                    builder.append(" ");
                    builder.append(this.mProcessInfo);
                }
                builder.append(")");
            }
            return builder.toString();
        }

        public int getRawCode() {
            return this.mCode;
        }

        public int getCode() {
            if (this.mBasedOnRealCaller && this.mCode != 0) {
                return 5;
            }
            return this.mCode;
        }
    }

    com.android.server.wm.BackgroundActivityStartController.BalVerdict checkBackgroundActivityStart(int callingUid, int callingPid, java.lang.String callingPackage, int realCallingUid, int realCallingPid, com.android.server.wm.WindowProcessController callerApp, com.android.server.am.PendingIntentRecord originatingPendingIntent, android.app.BackgroundStartPrivileges forcedBalByPiSender, com.android.server.wm.ActivityRecord resultRecord, android.content.Intent intent, android.app.ActivityOptions checkedOptions) {
        android.app.ActivityOptions checkedOptions2;
        com.android.server.wm.BackgroundActivityStartController.BalVerdict resultForRealCaller;
        if (checkedOptions != null) {
            checkedOptions2 = checkedOptions;
        } else {
            checkedOptions2 = ACTIVITY_OPTIONS_SYSTEM_DEFINED;
        }
        com.android.server.wm.BackgroundActivityStartController.BalState state = new com.android.server.wm.BackgroundActivityStartController.BalState(callingUid, callingPid, callingPackage, realCallingUid, realCallingPid, callerApp, originatingPendingIntent, forcedBalByPiSender, resultRecord, intent, checkedOptions2);
        boolean realCallerCanAllow = false;
        if (android.os.Process.isSdkSandboxUid(state.mRealCallingUid)) {
            int realCallingSdkSandboxUidToAppUid = android.os.Process.getAppUidForSdkSandboxUid(state.mRealCallingUid);
            if (this.mService.hasActiveVisibleWindow(realCallingSdkSandboxUidToAppUid)) {
                state.setResultForRealCaller(new com.android.server.wm.BackgroundActivityStartController.BalVerdict(10, false, "uid in SDK sandbox has visible (non-toast) window"));
                return allowBasedOnRealCaller(state);
            }
        }
        com.android.server.wm.BackgroundActivityStartController.BalVerdict resultForCaller = checkBackgroundActivityStartAllowedByCaller(state);
        state.setResultForCaller(resultForCaller);
        if (!state.hasRealCaller()) {
            if (resultForCaller.allows()) {
                return allowBasedOnCaller(state);
            }
            return abortLaunch(state);
        }
        if (state.callerIsRealCaller() && resultForCaller.allows()) {
            resultForRealCaller = resultForCaller;
        } else {
            resultForRealCaller = checkBackgroundActivityStartAllowedBySender(state).setBasedOnRealCaller();
        }
        state.setResultForRealCaller(resultForRealCaller);
        if (state.isPendingIntent()) {
            resultForCaller.setOnlyCreatorAllows(resultForCaller.allows() && resultForRealCaller.blocks());
        }
        if (resultForCaller.allows() && state.callerExplicitOptInOrAutoOptIn()) {
            return allowBasedOnCaller(state);
        }
        if (resultForRealCaller.allows() && state.realCallerExplicitOptInOrAutoOptIn()) {
            return allowBasedOnRealCaller(state);
        }
        boolean callerCanAllow = resultForCaller.allows() && !state.callerExplicitOptOut();
        if (resultForRealCaller.allows() && !state.realCallerExplicitOptOut()) {
            realCallerCanAllow = true;
        }
        if (callerCanAllow && state.mBalAllowedByPiCreator.allowsBackgroundActivityStarts()) {
            android.util.Slog.wtf(TAG, "With Android 15 BAL hardening this activity start may be blocked if the PI creator upgrades target_sdk to 35+!  (missing opt in by PI creator)!" + state);
            return allowBasedOnCaller(state);
        }
        if (realCallerCanAllow && state.mBalAllowedByPiSender.allowsBackgroundActivityStarts()) {
            android.util.Slog.wtf(TAG, "With Android 14 BAL hardening this activity start will be blocked if the PI sender upgrades target_sdk to 34+!  (missing opt in by PI sender)!" + state);
            return allowBasedOnRealCaller(state);
        }
        if (callerCanAllow || realCallerCanAllow) {
            android.util.Slog.w(TAG, "Without BAL hardening this activity start would be allowed");
        }
        return abortLaunch(state);
    }

    private com.android.server.wm.BackgroundActivityStartController.BalVerdict allowBasedOnCaller(com.android.server.wm.BackgroundActivityStartController.BalState state) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ACTIVITY_STARTS) {
            android.util.Slog.d(TAG, "Background activity launch allowed based on caller. " + state);
        }
        return statsLog(state.mResultForCaller, state);
    }

    private com.android.server.wm.BackgroundActivityStartController.BalVerdict allowBasedOnRealCaller(com.android.server.wm.BackgroundActivityStartController.BalState state) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ACTIVITY_STARTS) {
            android.util.Slog.d(TAG, "Background activity launch allowed based on real caller. " + state);
        }
        return statsLog(state.mResultForRealCaller, state);
    }

    private com.android.server.wm.BackgroundActivityStartController.BalVerdict abortLaunch(com.android.server.wm.BackgroundActivityStartController.BalState state) {
        android.util.Slog.wtf(TAG, "Background activity launch blocked! " + state);
        if (com.android.window.flags.Flags.balShowToastsBlocked() && (state.mResultForCaller.allows() || state.mResultForRealCaller.allows())) {
            showToast("BAL blocked. goo.gle/android-bal");
        }
        return statsLog(com.android.server.wm.BackgroundActivityStartController.BalVerdict.BLOCK, state);
    }

    com.android.server.wm.BackgroundActivityStartController.BalVerdict checkBackgroundActivityStartAllowedByCaller(com.android.server.wm.BackgroundActivityStartController.BalState state) {
        if (this.mBackgroundActivityStartControllerExt.interceptBackgroundActivityStartBegin(state.mIntent, state.mCallingUid, state.mCallingPid, state.mCallingPackage, state.mRealCallingUid, state.mRealCallingPid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(2, false, "Custom list");
        }
        boolean appSwitchAllowedOrFg = state.mAppSwitchState == 2 || state.mAppSwitchState == 1;
        if (appSwitchAllowedOrFg && state.mCallingUidHasAnyVisibleWindow) {
            this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_callingUidHasAnyVisbleWindowOrPersistentProcess", true, false);
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(4, false, "callingUid has visible window");
        }
        if (this.mService.mActiveUids.hasNonAppVisibleWindow(state.mCallingUid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(11, false, "callingUid has non-app visible window");
        }
        int callingAppId = android.os.UserHandle.getAppId(state.mCallingUid);
        if (state.mCallingUid == 0 || callingAppId == 1000 || callingAppId == 1027) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(2, false, "Important callingUid");
        }
        if (isHomeApp(state.mCallingUid, state.mCallingPackage)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, false, "Home app");
        }
        com.android.server.wm.WindowState imeWindow = this.mService.mRootWindowContainer.getCurrentInputMethodWindow();
        if (imeWindow != null && callingAppId == imeWindow.mOwnerUid) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, false, "Active ime");
        }
        if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE && this.mBackgroundActivityStartControllerExt.isFromBackgroundWhiteList(this.mService, state.mRealCallingUid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, true, "OPLUS_FEATURE_MS_LTW");
        }
        if (state.mIsCallingUidPersistentSystemProcess) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, false, "callingUid is persistent system process");
        }
        if (hasBalPermission(state.mCallingUid, state.mCallingPid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(6, true, "START_ACTIVITIES_FROM_BACKGROUND permission granted");
        }
        if (this.mSupervisor.mRecentTasks.isCallerRecents(state.mCallingUid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, true, "Recents Component");
        }
        if (this.mService.isDeviceOwner(state.mCallingUid)) {
            this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_callingUidDeviceOwner", false, false);
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, true, "Device Owner");
        }
        if (this.mService.isAffiliatedProfileOwner(state.mCallingUid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, true, "Affiliated Profile Owner");
        }
        int callingUserId = android.os.UserHandle.getUserId(state.mCallingUid);
        if (this.mService.isAssociatedCompanionApp(callingUserId, state.mCallingUid)) {
            this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_callingUidHasCompanionDevice", false, false);
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, true, "Companion App");
        }
        if (this.mService.hasSystemAlertWindowPermission(state.mCallingUid, state.mCallingPid, state.mCallingPackage)) {
            android.util.Slog.w(TAG, "Background activity start for " + state.mCallingPackage + " allowed because SYSTEM_ALERT_WINDOW permission is granted.");
            this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_callingUidHasSYSTEM_ALERT_WINDOW", false, false);
            if (this.mBackgroundActivityStartControllerExt.checkBackgroundActivityPermission(this.mService, state.mCallingUid, state.mCallingPackage, state.mIntent)) {
                return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(7, true, "SYSTEM_ALERT_WINDOW permission is granted");
            }
        }
        if (isSystemExemptFlagEnabled() && this.mService.getAppOpsManager().checkOpNoThrow(130, state.mCallingUid, state.mCallingPackage) == 0) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(6, true, "OP_SYSTEM_EXEMPT_FROM_ACTIVITY_BG_START_RESTRICTION appop is granted");
        }
        com.android.server.wm.BackgroundActivityStartController.BalVerdict callerAppAllowsBal = checkProcessAllowsBal(state.mCallerApp, state);
        if (callerAppAllowsBal.allows()) {
            return callerAppAllowsBal;
        }
        this.mBackgroundActivityStartControllerExt.monitorActivityStartInfoIfNeed("S_forbid_AOSP", false, false);
        return com.android.server.wm.BackgroundActivityStartController.BalVerdict.BLOCK;
    }

    com.android.server.wm.BackgroundActivityStartController.BalVerdict checkBackgroundActivityStartAllowedBySender(com.android.server.wm.BackgroundActivityStartController.BalState state) {
        boolean appSwitchAllowedOrFg = state.mAppSwitchState == 2 || state.mAppSwitchState == 1 || isHomeApp(state.mRealCallingUid, state.mRealCallingPackage);
        if (com.android.window.flags.Flags.balImproveRealCallerVisibilityCheck()) {
            if (appSwitchAllowedOrFg && (state.mRealCallingUidHasAnyVisibleWindow || this.mBackgroundActivityStartControllerExt.startAllowedIfRealCallingUidIsHome(this.mService, state.mRealCallingUid))) {
                return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(4, false, "realCallingUid has visible window");
            }
            if (this.mService.mActiveUids.hasNonAppVisibleWindow(state.mRealCallingUid)) {
                return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(11, false, "realCallingUid has non-app visible window");
            }
        } else if (state.mRealCallingUidHasAnyVisibleWindow || this.mBackgroundActivityStartControllerExt.startAllowedIfRealCallingUidIsHome(this.mService, state.mRealCallingUid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(4, false, "realCallingUid has visible (non-toast) window.");
        }
        if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE && this.mBackgroundActivityStartControllerExt.isFromBackgroundWhiteList(this.mService, state.mRealCallingUid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, true, "OPLUS_FEATURE_MS_LTW");
        }
        if (state.isPendingIntentBalAllowedByPermission() && hasBalPermission(state.mRealCallingUid, state.mRealCallingPid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(6, false, "realCallingUid has BAL permission.");
        }
        if (state.mForcedBalByPiSender.allowsBackgroundActivityStarts() && state.mIsRealCallingUidPersistentSystemProcess) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(2, false, "realCallingUid is persistent system process AND intent sender forced to allow.");
        }
        if (this.mService.isAssociatedCompanionApp(android.os.UserHandle.getUserId(state.mRealCallingUid), state.mRealCallingUid)) {
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(3, false, "realCallingUid is a companion app.");
        }
        com.android.server.wm.BackgroundActivityStartController.BalVerdict realCallerAppAllowsBal = checkProcessAllowsBal(state.mRealCallerApp, state);
        if (realCallerAppAllowsBal.allows()) {
            return realCallerAppAllowsBal;
        }
        return com.android.server.wm.BackgroundActivityStartController.BalVerdict.BLOCK;
    }

    boolean hasBalPermission(int uid, int pid) {
        return com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.START_ACTIVITIES_FROM_BACKGROUND", pid, uid) == 0;
    }

    com.android.server.wm.BackgroundActivityStartController.BalVerdict checkProcessAllowsBal(com.android.server.wm.WindowProcessController app, com.android.server.wm.BackgroundActivityStartController.BalState state) {
        if (app == null) {
            return com.android.server.wm.BackgroundActivityStartController.BalVerdict.BLOCK;
        }
        com.android.server.wm.BackgroundActivityStartController.BalVerdict balAllowedForCaller = app.areBackgroundActivityStartsAllowed(state.mAppSwitchState);
        if (balAllowedForCaller.allows()) {
            return balAllowedForCaller.withProcessInfo("callerApp process", app);
        }
        android.util.ArraySet<com.android.server.wm.WindowProcessController> uidProcesses = this.mService.mProcessMap.getProcesses(app.mUid);
        if (uidProcesses != null) {
            for (int i = uidProcesses.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowProcessController proc = uidProcesses.valueAt(i);
                if (proc != app) {
                    com.android.server.wm.BackgroundActivityStartController.BalVerdict balAllowedForUid = proc.areBackgroundActivityStartsAllowed(state.mAppSwitchState);
                    if (balAllowedForUid.allows()) {
                        return balAllowedForUid.withProcessInfo("process", proc);
                    }
                }
            }
        }
        return com.android.server.wm.BackgroundActivityStartController.BalVerdict.BLOCK;
    }

    boolean checkActivityAllowedToStart(com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord targetRecord, boolean newTask, boolean avoidMoveTaskToFront, com.android.server.wm.Task targetTask, int launchFlags, int balCode, int callingUid, int realCallingUid, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea) {
        com.android.server.wm.TaskDisplayArea displayArea;
        com.android.server.wm.BackgroundActivityStartController.BlockActivityStart bas;
        if (balCode == 2) {
            return true;
        }
        boolean z = false;
        boolean taskToFront = newTask || this.mBackgroundActivityStartControllerExt.startAllowedIfRealCallingUidIsHome(this.mService, realCallingUid) || (launchFlags & 268435456) == 268435456;
        if (taskToFront && (balCode == 3 || balCode == 6 || balCode == 5 || balCode == 7 || balCode == 4 || balCode == 11)) {
            return true;
        }
        com.android.server.wm.BackgroundActivityStartController.BlockActivityStart bas2 = new com.android.server.wm.BackgroundActivityStartController.BlockActivityStart();
        if (sourceRecord != null) {
            com.android.server.wm.Task sourceTask = sourceRecord.getTask();
            com.android.server.wm.Task taskToCheck = taskToFront ? sourceTask : targetTask;
            com.android.server.wm.BackgroundActivityStartController.BlockActivityStart bas3 = checkTopActivityForAsm(taskToCheck, sourceRecord.getUid(), sourceRecord, bas2);
            if (taskToFront && bas3.mTopActivityMatchesSource) {
                if (sourceTask != null && (sourceTask.isVisible() || sourceTask == targetTask)) {
                    z = true;
                }
                bas3.mTopActivityMatchesSource = z;
            }
            bas = bas3;
        } else if (targetTask != null && (!taskToFront || avoidMoveTaskToFront)) {
            bas = checkTopActivityForAsm(targetTask, callingUid, null, bas2);
        } else {
            if (targetTask != null && targetTask.getDisplayArea() != null) {
                displayArea = targetTask.getDisplayArea();
            } else {
                displayArea = preferredTaskDisplayArea;
            }
            if (displayArea == null) {
                bas = bas2;
            } else {
                java.util.ArrayList<com.android.server.wm.Task> visibleTasks = displayArea.getVisibleTasks();
                for (int i = 0; i < visibleTasks.size(); i++) {
                    com.android.server.wm.Task task = visibleTasks.get(i);
                    if (visibleTasks.size() == 1 && task.isActivityTypeHomeOrRecents()) {
                        bas2.optedIn(task.getTopMostActivity());
                    } else {
                        bas2 = checkTopActivityForAsm(task, callingUid, null, bas2);
                    }
                }
                bas = bas2;
            }
        }
        if (bas.mTopActivityMatchesSource) {
            return true;
        }
        return logAsmFailureAndCheckFeatureEnabled(sourceRecord, callingUid, realCallingUid, newTask, avoidMoveTaskToFront, targetTask, targetRecord, balCode, launchFlags, bas, taskToFront);
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x013d  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0185 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean logAsmFailureAndCheckFeatureEnabled(com.android.server.wm.ActivityRecord r37, int r38, int r39, boolean r40, boolean r41, com.android.server.wm.Task r42, com.android.server.wm.ActivityRecord r43, int r44, int r45, com.android.server.wm.BackgroundActivityStartController.BlockActivityStart r46, boolean r47) {
        /*
            Method dump skipped, instruction units count: 390
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.BackgroundActivityStartController.logAsmFailureAndCheckFeatureEnabled(com.android.server.wm.ActivityRecord, int, int, boolean, boolean, com.android.server.wm.Task, com.android.server.wm.ActivityRecord, int, int, com.android.server.wm.BackgroundActivityStartController$BlockActivityStart, boolean):boolean");
    }

    static /* synthetic */ boolean lambda$logAsmFailureAndCheckFeatureEnabled$0(com.android.server.wm.ActivityRecord ar) {
        return (ar.finishing || ar.isAlwaysOnTop()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showToast$1(java.lang.String toastText) {
        android.widget.Toast.makeText(this.mService.mContext, toastText, 1).show();
    }

    void showToast(final java.lang.String toastText) {
        com.android.server.UiThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showToast$1(toastText);
            }
        });
    }

    void clearTopIfNeeded(com.android.server.wm.Task targetTask, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord targetRecord, final int callingUid, final int realCallingUid, int launchFlags, int balCode) {
        java.lang.String str;
        if ((launchFlags & 268435456) != 268435456 || balCode == 2) {
            return;
        }
        final int startingUid = targetRecord.getUid();
        java.util.function.Predicate<com.android.server.wm.ActivityRecord> isLaunchingOrLaunched = new java.util.function.Predicate() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.BackgroundActivityStartController.lambda$clearTopIfNeeded$2(startingUid, callingUid, realCallingUid, (com.android.server.wm.ActivityRecord) obj);
            }
        };
        com.android.server.wm.ActivityRecord targetTaskTop = targetTask.getTopMostActivity();
        if (targetTaskTop != null && !isLaunchingOrLaunched.test(targetTaskTop)) {
            int[] finishCount = new int[1];
            boolean shouldBlockActivityStart = com.android.server.wm.ActivitySecurityModelFeatureFlags.shouldRestrictActivitySwitch(callingUid);
            com.android.server.wm.BackgroundActivityStartController.BlockActivityStart bas = checkCrossUidActivitySwitchFromBelow(targetTaskTop, callingUid, new com.android.server.wm.BackgroundActivityStartController.BlockActivityStart());
            if (shouldBlockActivityStart && bas.mTopActivityOptedIn) {
                com.android.server.wm.ActivityRecord activity = targetTask.getActivity(isLaunchingOrLaunched);
                if (activity == null) {
                    activity = targetRecord;
                }
                targetTask.performClearTop(activity, launchFlags, finishCount);
                if (finishCount[0] > 0) {
                    android.util.Slog.w(TAG, "Cleared top n: " + finishCount[0] + " activities from task t: " + targetTask + " not matching top uid: " + callingUid);
                }
            }
            if (com.android.server.wm.ActivitySecurityModelFeatureFlags.shouldShowToast(callingUid)) {
                if (!shouldBlockActivityStart || finishCount[0] > 0) {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    if (shouldBlockActivityStart) {
                        str = "Top activities cleared by ";
                    } else {
                        str = "Top activities would be cleared by ";
                    }
                    showToast(sb.append(str).append("go/android-asm").toString());
                    android.util.Slog.i(TAG, getDebugInfoForActivitySecurity("Clear Top", sourceRecord, targetRecord, targetTask, targetTaskTop, realCallingUid, balCode, shouldBlockActivityStart, true, false, false, bas.mActivityOptedIn));
                }
            }
        }
    }

    static /* synthetic */ boolean lambda$clearTopIfNeeded$2(int startingUid, int callingUid, int realCallingUid, com.android.server.wm.ActivityRecord ar) {
        return ar.isUid(startingUid) || ar.isUid(callingUid) || ar.isUid(realCallingUid);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void checkActivityAllowedToClearTask(com.android.server.wm.Task task, int i, int i2, java.lang.String str) {
        com.android.server.wm.TaskDisplayArea taskDisplayArea;
        java.lang.CharSequence applicationLabel;
        if (i == 1000 || !task.isVisible()) {
            return;
        }
        if (task.inMultiWindowMode()) {
            return;
        }
        int i3 = checkBackgroundActivityStartAllowedByCaller(new com.android.server.wm.BackgroundActivityStartController.BalState(i, i2, this.mService.mContext.getPackageManager().getNameForUid(i), -1, -1, null, null, null, null, null, android.app.ActivityOptions.makeBasic())).mCode;
        if (i3 == 2 || i3 == 3 || i3 == 6 || i3 == 7 || i3 == 4) {
            return;
        }
        if (i3 != 11 && (taskDisplayArea = task.getTaskDisplayArea()) != null) {
            com.android.server.wm.BackgroundActivityStartController.BlockActivityStart blockActivityStartCheckTopActivityForAsm = checkTopActivityForAsm(task, i, null, new com.android.server.wm.BackgroundActivityStartController.BlockActivityStart());
            if (blockActivityStartCheckTopActivityForAsm.mTopActivityMatchesSource) {
                return;
            }
            com.android.server.wm.ActivityRecord activity = task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.BackgroundActivityStartController.lambda$checkActivityAllowedToClearTask$3((com.android.server.wm.ActivityRecord) obj);
                }
            });
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.ACTIVITY_ACTION_BLOCKED, i, str, activity == null ? -1 : activity.getUid(), activity != null ? activity.info.name : null, false, -1, (java.lang.String) null, (java.lang.String) null, 0, 4, 11, false, -1, (java.lang.String) null);
            boolean z = com.android.server.wm.ActivitySecurityModelFeatureFlags.shouldRestrictActivitySwitch(i) && blockActivityStartCheckTopActivityForAsm.mTopActivityOptedIn;
            android.content.pm.PackageManager packageManager = this.mService.mContext.getPackageManager();
            java.lang.String nameForUid = packageManager.getNameForUid(i);
            if (nameForUid == null) {
                nameForUid = java.lang.String.valueOf(i);
                applicationLabel = nameForUid;
            } else {
                applicationLabel = com.android.server.wm.ActivityTaskSupervisor.getApplicationLabel(packageManager, nameForUid);
            }
            if (com.android.server.wm.ActivitySecurityModelFeatureFlags.shouldShowToast(i)) {
                showToast("go/android-asm" + (z ? " returned home due to " : " would return home due to ") + ((java.lang.Object) applicationLabel));
            }
            if (z) {
                android.util.Slog.w(TAG, "[ASM] Return to home as source: " + nameForUid + " is not on top of task t: " + task);
                taskDisplayArea.moveHomeActivityToTop("taskRemoved");
            } else {
                android.util.Slog.i(TAG, "[ASM] Would return to home as source: " + nameForUid + " is not on top of task t: " + task);
            }
        }
    }

    static /* synthetic */ boolean lambda$checkActivityAllowedToClearTask$3(com.android.server.wm.ActivityRecord ar) {
        return (ar.finishing || ar.isAlwaysOnTop()) ? false : true;
    }

    private com.android.server.wm.BackgroundActivityStartController.BlockActivityStart checkTopActivityForAsm(com.android.server.wm.Task task, final int uid, final com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.BackgroundActivityStartController.BlockActivityStart bas) {
        com.android.server.wm.TaskFragment taskFragment;
        com.android.server.wm.TaskFragment adjacentTaskFragment;
        com.android.server.wm.ActivityRecord topActivity;
        if (sourceRecord != null && sourceRecord.isVisibleRequested()) {
            return bas.matchesSource();
        }
        com.android.server.wm.ActivityRecord topActivity2 = task.getTopMostActivity();
        if (topActivity2 == null) {
            android.util.Slog.wtf(TAG, "Activities for task: " + task + " not found.");
            return bas.optedIn(topActivity2);
        }
        com.android.server.wm.BackgroundActivityStartController.BlockActivityStart bas2 = checkCrossUidActivitySwitchFromBelow(topActivity2, uid, bas);
        if (bas2.mTopActivityMatchesSource) {
            return bas2;
        }
        if (task.forAllActivities(new java.util.function.Predicate() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.BackgroundActivityStartController.lambda$checkTopActivityForAsm$4(uid, (com.android.server.wm.ActivityRecord) obj);
            }
        })) {
            return bas2.matchesSource();
        }
        java.util.function.Predicate<com.android.server.wm.ActivityRecord> topOfStackPredicate = new java.util.function.Predicate() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.BackgroundActivityStartController.lambda$checkTopActivityForAsm$5(sourceRecord, (com.android.server.wm.ActivityRecord) obj);
            }
        };
        com.android.server.wm.ActivityRecord topActivity3 = task.getActivity(topOfStackPredicate);
        if (topActivity3 == null) {
            return bas2;
        }
        com.android.server.wm.BackgroundActivityStartController.BlockActivityStart bas3 = checkCrossUidActivitySwitchFromBelow(topActivity3, uid, bas2);
        if (bas3.mTopActivityMatchesSource || (taskFragment = topActivity3.getTaskFragment()) == null || (adjacentTaskFragment = taskFragment.getAdjacentTaskFragment()) == null || (topActivity = adjacentTaskFragment.getActivity(topOfStackPredicate)) == null) {
            return bas3;
        }
        return checkCrossUidActivitySwitchFromBelow(topActivity, uid, bas3);
    }

    static /* synthetic */ boolean lambda$checkTopActivityForAsm$4(int uid, com.android.server.wm.ActivityRecord ar) {
        return ar.isUid(uid) && ar.isVisibleRequested();
    }

    static /* synthetic */ boolean lambda$checkTopActivityForAsm$5(com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord ar) {
        return ar.equals(sourceRecord) || !(ar.finishing || ar.isAlwaysOnTop());
    }

    private com.android.server.wm.BackgroundActivityStartController.BlockActivityStart checkCrossUidActivitySwitchFromBelow(com.android.server.wm.ActivityRecord ar, int sourceUid, com.android.server.wm.BackgroundActivityStartController.BlockActivityStart bas) {
        if (ar.isUid(sourceUid)) {
            return bas.matchesSource();
        }
        if (ar.mAllowCrossUidActivitySwitchFromBelow) {
            bas.mTopActivityOptedIn = false;
            return bas.matchesSource();
        }
        if (ar.isUid(1000)) {
            if (android.security.Flags.asmOptSystemIntoEnforcement()) {
                return bas.optedIn(ar);
            }
            return bas;
        }
        if (!android.app.compat.CompatChanges.isChangeEnabled(230590090L, ar.getUid())) {
            return bas;
        }
        java.lang.String packageName = ar.packageName;
        if (packageName == null) {
            android.util.Slog.wtf(TAG, "Package name: " + ar + " not found.");
            return bas.optedIn(ar);
        }
        android.content.pm.PackageManager pm = this.mService.mContext.getPackageManager();
        try {
            android.content.pm.ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            return applicationInfo.allowCrossUidActivitySwitchFromBelow ? bas : bas.optedIn(ar);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.wtf(TAG, "Package name: " + packageName + " not found.");
            return bas.optedIn(ar);
        }
    }

    private java.lang.String getDebugInfoForActivitySecurity(java.lang.String action, final com.android.server.wm.ActivityRecord sourceRecord, final com.android.server.wm.ActivityRecord targetRecord, com.android.server.wm.Task targetTask, final com.android.server.wm.ActivityRecord targetTopActivity, int realCallingUid, int balCode, boolean enforceBlock, boolean taskToFront, boolean avoidMoveTaskToFront, boolean allowedByGracePeriod, com.android.server.wm.ActivityRecord activityOptedIn) {
        final java.util.function.Function<com.android.server.wm.ActivityRecord, java.lang.String> recordToString = new java.util.function.Function() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.wm.BackgroundActivityStartController.lambda$getDebugInfoForActivitySecurity$6(sourceRecord, targetTopActivity, targetRecord, (com.android.server.wm.ActivityRecord) obj);
            }
        };
        final java.util.StringJoiner joiner = new java.util.StringJoiner("\n");
        joiner.add("[ASM] ------ Activity Security " + action + " Debug Logging Start ------");
        joiner.add("[ASM] Block Enabled: " + enforceBlock);
        if (!enforceBlock) {
            joiner.add("[ASM] Feature Flag Enabled: " + android.security.Flags.asmRestrictionsEnabled());
            joiner.add("[ASM] Mendel Override: " + com.android.server.wm.ActivitySecurityModelFeatureFlags.asmRestrictionsEnabledForAll());
        }
        joiner.add("[ASM] ASM Version: 11");
        joiner.add("[ASM] System Time: " + android.os.SystemClock.uptimeMillis());
        joiner.add("[ASM] Activity Opted In: " + recordToString.apply(activityOptedIn));
        boolean targetTaskMatchesSourceTask = (targetTask == null || sourceRecord == null || sourceRecord.getTask() != targetTask) ? false : true;
        if (sourceRecord == null) {
            joiner.add("[ASM] Source Package: " + targetRecord.launchedFromPackage);
            java.lang.String realCallingPackage = this.mService.mContext.getPackageManager().getNameForUid(realCallingUid);
            joiner.add("[ASM] Real Calling Uid Package: " + realCallingPackage);
        } else {
            joiner.add("[ASM] Source Record: " + recordToString.apply(sourceRecord));
            joiner.add("[ASM] Source Launch Package: " + sourceRecord.launchedFromPackage);
            joiner.add("[ASM] Source Launch Intent: " + sourceRecord.intent);
            if (targetTaskMatchesSourceTask) {
                joiner.add("[ASM] Source/Target Task: " + sourceRecord.getTask());
                joiner.add("[ASM] Source/Target Task Stack: ");
            } else {
                joiner.add("[ASM] Source Task: " + sourceRecord.getTask());
                joiner.add("[ASM] Source Task Stack: ");
            }
            sourceRecord.getTask().forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    joiner.add("[ASM] " + ((java.lang.String) recordToString.apply((com.android.server.wm.ActivityRecord) obj)));
                }
            });
        }
        joiner.add("[ASM] Target Task Top: " + recordToString.apply(targetTopActivity));
        if (!targetTaskMatchesSourceTask) {
            joiner.add("[ASM] Target Task: " + targetTask);
            if (targetTask != null) {
                joiner.add("[ASM] Target Task Stack: ");
                targetTask.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        joiner.add("[ASM] " + ((java.lang.String) recordToString.apply((com.android.server.wm.ActivityRecord) obj)));
                    }
                });
            }
        }
        joiner.add("[ASM] Target Record: " + recordToString.apply(targetRecord));
        joiner.add("[ASM] Intent: " + targetRecord.intent);
        joiner.add("[ASM] TaskToFront: " + taskToFront);
        joiner.add("[ASM] AvoidMoveToFront: " + avoidMoveTaskToFront);
        joiner.add("[ASM] BalCode: " + balCodeToString(balCode));
        joiner.add("[ASM] Allowed By Grace Period: " + allowedByGracePeriod);
        joiner.add("[ASM] LastResumedActivity: " + recordToString.apply(this.mService.mLastResumedActivity));
        joiner.add("[ASM] System opted into enforcement: " + android.security.Flags.asmOptSystemIntoEnforcement());
        if (this.mTopFinishedActivity != null) {
            joiner.add("[ASM] TopFinishedActivity: " + this.mTopFinishedActivity.mDebugInfo);
        }
        if (!this.mTaskIdToFinishedActivity.isEmpty()) {
            joiner.add("[ASM] TaskIdToFinishedActivity: ");
            this.mTaskIdToFinishedActivity.values().forEach(new java.util.function.Consumer() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    joiner.add("[ASM]   " + ((com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry) obj).mDebugInfo);
                }
            });
        }
        if (balCode == 4 || balCode == 11 || balCode == 9) {
            com.android.server.wm.Task task = sourceRecord != null ? sourceRecord.getTask() : targetTask;
            if (task != null && task.getDisplayArea() != null) {
                joiner.add("[ASM] Tasks: ");
                task.getDisplayArea().forAllTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.BackgroundActivityStartController$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        joiner.add("[ASM]    T: " + ((com.android.server.wm.Task) obj).toFullString());
                    }
                });
            }
        }
        joiner.add("[ASM] ------ Activity Security " + action + " Debug Logging End ------");
        return joiner.toString();
    }

    static /* synthetic */ java.lang.String lambda$getDebugInfoForActivitySecurity$6(com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord targetTopActivity, com.android.server.wm.ActivityRecord targetRecord, com.android.server.wm.ActivityRecord ar) {
        java.lang.String str;
        if (ar == null) {
            return null;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (ar == sourceRecord) {
            str = " [source]=> ";
        } else if (ar == targetTopActivity) {
            str = " [ top  ]=> ";
        } else {
            str = ar == targetRecord ? " [target]=> " : "         => ";
        }
        return sb.append(str).append(getDebugStringForActivityRecord(ar)).toString();
    }

    private boolean allowedByAsmGracePeriod(int callingUid, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.Task targetTask, int balCode, boolean taskToFront, boolean avoidMoveTaskToFront) {
        com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry finishedEntry;
        com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry finishedEntry2;
        if (balCode == 8) {
            if (taskToFront && this.mTopFinishedActivity != null && this.mTopFinishedActivity.mUid == callingUid) {
                return true;
            }
            if (!taskToFront || avoidMoveTaskToFront) {
                if (targetTask == null || (finishedEntry2 = this.mTaskIdToFinishedActivity.get(java.lang.Integer.valueOf(targetTask.mTaskId))) == null || finishedEntry2.mUid != callingUid) {
                    return (sourceRecord == null || (finishedEntry = this.mTaskIdToFinishedActivity.get(java.lang.Integer.valueOf(sourceRecord.getTask().mTaskId))) == null || finishedEntry.mUid != callingUid) ? false : true;
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isSystemExemptFlagEnabled() {
        return android.provider.DeviceConfig.getBoolean("window_manager", "system_exempt_from_activity_bg_start_restriction_enabled", true);
    }

    private com.android.server.wm.BackgroundActivityStartController.BalVerdict statsLog(com.android.server.wm.BackgroundActivityStartController.BalVerdict finalVerdict, com.android.server.wm.BackgroundActivityStartController.BalState state) {
        java.lang.String activityName;
        java.lang.String activityName2;
        if (finalVerdict.blocks() && this.mService.isActivityStartsLoggingEnabled()) {
            this.mSupervisor.getActivityMetricsLogger().logAbortedBgActivityStart(state.mIntent, state.mCallerApp, state.mCallingUid, state.mCallingPackage, state.mCallingUidProcState, state.mCallingUidHasAnyVisibleWindow, state.mRealCallingUid, state.mRealCallingUidProcState, state.mRealCallingUidHasAnyVisibleWindow, state.mOriginatingPendingIntent != null);
        }
        if (com.android.window.flags.Flags.balImprovedMetrics()) {
            if (shouldLogStats(finalVerdict, state)) {
                if (shouldLogIntentActivity(finalVerdict, state)) {
                    android.content.Intent intent = state.mIntent;
                    activityName2 = intent == null ? "noIntent" : ((android.content.ComponentName) java.util.Objects.requireNonNull(intent.getComponent())).flattenToShortString();
                } else {
                    activityName2 = "";
                }
                writeBalAllowedLog(activityName2, finalVerdict.getCode(), state);
            }
        } else {
            int code = finalVerdict.getCode();
            int callingUid = state.mCallingUid;
            int realCallingUid = state.mRealCallingUid;
            android.content.Intent intent2 = state.mIntent;
            if (code == 5 && (callingUid < 10000 || realCallingUid < 10000)) {
                if (intent2 == null) {
                    activityName = "";
                } else {
                    activityName = ((android.content.ComponentName) java.util.Objects.requireNonNull(intent2.getComponent())).flattenToShortString();
                }
                writeBalAllowedLog(activityName, 5, state);
            }
            if (code == 6 || code == 9 || code == 7) {
                writeBalAllowedLog("", code, state);
            }
        }
        return finalVerdict;
    }

    boolean shouldLogStats(com.android.server.wm.BackgroundActivityStartController.BalVerdict finalVerdict, com.android.server.wm.BackgroundActivityStartController.BalState state) {
        if (finalVerdict.getRawCode() == 4) {
            return state.isPendingIntent() && !finalVerdict.mBasedOnRealCaller;
        }
        return true;
    }

    boolean shouldLogIntentActivity(com.android.server.wm.BackgroundActivityStartController.BalVerdict finalVerdict, com.android.server.wm.BackgroundActivityStartController.BalState state) {
        return finalVerdict.mBasedOnRealCaller ? state.mRealCallingUid < 10000 : state.mCallingUid < 10000;
    }

    void writeBalAllowedLog(java.lang.String activityName, int code, com.android.server.wm.BackgroundActivityStartController.BalState state) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BAL_ALLOWED, activityName, code, state.mCallingUid, state.mRealCallingUid, state.mResultForCaller == null ? 0 : state.mResultForCaller.getRawCode(), state.mBalAllowedByPiCreator.allowsBackgroundActivityStarts(), state.callerExplicitOptInOrOut(), state.mResultForRealCaller == null ? 0 : state.mResultForRealCaller.getRawCode(), state.mBalAllowedByPiSender.allowsBackgroundActivityStarts(), state.realCallerExplicitOptInOrOut(), getTargetSdk(state.mCallingPackage), getTargetSdk(state.mRealCallingPackage));
    }

    void onActivityRequestedFinishing(com.android.server.wm.ActivityRecord finishActivity) {
        com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry entry = this.mTaskIdToFinishedActivity.get(java.lang.Integer.valueOf(finishActivity.getTask().mTaskId));
        if (entry != null && finishActivity.isUid(entry.mUid) && entry.mLaunchCount > 5) {
            return;
        }
        if (!finishActivity.isVisibleRequested() && finishActivity != finishActivity.getTask().getTopMostActivity()) {
            return;
        }
        com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry newEntry = new com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry(finishActivity);
        this.mTaskIdToFinishedActivity.put(java.lang.Integer.valueOf(finishActivity.getTask().mTaskId), newEntry);
        if (finishActivity.getTask().mVisibleRequested) {
            this.mTopFinishedActivity = newEntry;
        }
    }

    void onNewActivityLaunched(com.android.server.wm.ActivityRecord activityStarted) {
        if (activityStarted.getTask() == null) {
            return;
        }
        if (activityStarted.getTask().mVisibleRequested) {
            this.mTopFinishedActivity = null;
        }
        com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry entry = this.mTaskIdToFinishedActivity.get(java.lang.Integer.valueOf(activityStarted.getTask().mTaskId));
        if (entry != null && activityStarted.getTask().isTaskId(entry.mTaskId)) {
            this.mTaskIdToFinishedActivity.remove(java.lang.Integer.valueOf(entry.mTaskId));
        }
    }

    private static class BlockActivityStart {
        private com.android.server.wm.ActivityRecord mActivityOptedIn;
        private boolean mTopActivityMatchesSource;
        private boolean mTopActivityOptedIn;

        private BlockActivityStart() {
        }

        com.android.server.wm.BackgroundActivityStartController.BlockActivityStart optedIn(com.android.server.wm.ActivityRecord activity) {
            this.mTopActivityOptedIn = true;
            if (this.mActivityOptedIn == null) {
                this.mActivityOptedIn = activity;
            }
            return this;
        }

        com.android.server.wm.BackgroundActivityStartController.BlockActivityStart matchesSource() {
            this.mTopActivityMatchesSource = true;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getDebugStringForActivityRecord(com.android.server.wm.ActivityRecord ar) {
        return ar + " :: visible=" + ar.isVisible() + ", visibleRequested=" + ar.isVisibleRequested() + ", finishing=" + ar.finishing + ", alwaysOnTop=" + ar.isAlwaysOnTop() + ", lastLaunchTime=" + ar.lastLaunchTime + ", lastVisibleTime=" + ar.lastVisibleTime + ", taskFragment=" + ar.getTaskFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTargetSdk(java.lang.String packageName) {
        if (packageName == null) {
            return -1;
        }
        try {
            android.content.pm.PackageManager pm = this.mService.mContext.getPackageManager();
            return pm.getTargetSdkVersion(packageName);
        } catch (java.lang.Exception e) {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class FinishedActivityEntry {
        java.lang.String mDebugInfo;
        int mLaunchCount;
        int mTaskId;
        int mUid;

        FinishedActivityEntry(com.android.server.wm.ActivityRecord ar) {
            com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry entry = (com.android.server.wm.BackgroundActivityStartController.FinishedActivityEntry) com.android.server.wm.BackgroundActivityStartController.this.mTaskIdToFinishedActivity.get(java.lang.Integer.valueOf(ar.getTask().mTaskId));
            final int taskId = ar.getTask().mTaskId;
            this.mUid = ar.getUid();
            this.mTaskId = taskId;
            int i = 1;
            if (entry != null && ar.isUid(entry.mUid)) {
                i = 1 + entry.mLaunchCount;
            }
            this.mLaunchCount = i;
            this.mDebugInfo = com.android.server.wm.BackgroundActivityStartController.getDebugStringForActivityRecord(ar);
            com.android.server.wm.BackgroundActivityStartController.this.mService.mH.postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.BackgroundActivityStartController$FinishedActivityEntry$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0(taskId);
                }
            }, 3000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int taskId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.BackgroundActivityStartController.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.BackgroundActivityStartController.this.mTaskIdToFinishedActivity.get(java.lang.Integer.valueOf(taskId)) == this) {
                        com.android.server.wm.BackgroundActivityStartController.this.mTaskIdToFinishedActivity.remove(java.lang.Integer.valueOf(taskId));
                    }
                    if (com.android.server.wm.BackgroundActivityStartController.this.mTopFinishedActivity == this) {
                        com.android.server.wm.BackgroundActivityStartController.this.mTopFinishedActivity = null;
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }
}
