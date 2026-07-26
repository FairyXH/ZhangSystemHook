package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public class ModifierShortcutManager {
    private static final java.lang.String ATTRIBUTE_CATEGORY = "category";
    private static final java.lang.String ATTRIBUTE_CLASS = "class";
    private static final java.lang.String ATTRIBUTE_PACKAGE = "package";
    private static final java.lang.String ATTRIBUTE_ROLE = "role";
    private static final java.lang.String ATTRIBUTE_SHIFT = "shift";
    private static final java.lang.String ATTRIBUTE_SHORTCUT = "shortcut";
    public static final java.lang.String EXTRA_ROLE = "com.android.server.policy.ModifierShortcutManager.EXTRA_ROLE";
    private static final java.lang.String TAG = "ModifierShortcutManager";
    private static final java.lang.String TAG_BOOKMARK = "bookmark";
    private static final java.lang.String TAG_BOOKMARKS = "bookmarks";
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.app.role.RoleManager mRoleManager;
    static android.util.SparseArray<java.lang.String> sApplicationLaunchKeyRoles = new android.util.SparseArray<>();
    static android.util.SparseArray<java.lang.String> sApplicationLaunchKeyCategories = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.content.Intent> mIntentShortcuts = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.content.Intent> mShiftShortcuts = new android.util.SparseArray<>();
    private final android.util.SparseArray<java.lang.String> mRoleShortcuts = new android.util.SparseArray<>();
    private final android.util.SparseArray<java.lang.String> mShiftRoleShortcuts = new android.util.SparseArray<>();
    private final java.util.Map<java.lang.String, android.content.Intent> mRoleIntents = new java.util.HashMap();
    private android.util.LongSparseArray<com.android.internal.policy.IShortcutService> mShortcutKeyServices = new android.util.LongSparseArray<>();
    private boolean mSearchKeyShortcutPending = false;
    private boolean mConsumeSearchKeyUp = true;

    static {
        sApplicationLaunchKeyRoles.append(64, "android.app.role.BROWSER");
        sApplicationLaunchKeyCategories.append(65, "android.intent.category.APP_EMAIL");
        sApplicationLaunchKeyCategories.append(207, "android.intent.category.APP_CONTACTS");
        sApplicationLaunchKeyCategories.append(208, "android.intent.category.APP_CALENDAR");
        sApplicationLaunchKeyCategories.append(209, "android.intent.category.APP_MUSIC");
        sApplicationLaunchKeyCategories.append(210, "android.intent.category.APP_CALCULATOR");
    }

    ModifierShortcutManager(android.content.Context context, android.os.Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        this.mPackageManager = this.mContext.getPackageManager();
        this.mRoleManager = (android.app.role.RoleManager) this.mContext.getSystemService(android.app.role.RoleManager.class);
        this.mRoleManager.addOnRoleHoldersChangedListenerAsUser(this.mContext.getMainExecutor(), new android.app.role.OnRoleHoldersChangedListener() { // from class: com.android.server.policy.ModifierShortcutManager$$ExternalSyntheticLambda1
            public final void onRoleHoldersChanged(java.lang.String str, android.os.UserHandle userHandle) {
                this.f$0.lambda$new$0(str, userHandle);
            }
        }, android.os.UserHandle.ALL);
        loadShortcuts();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.lang.String roleName, android.os.UserHandle user) {
        this.mRoleIntents.remove(roleName);
    }

    private android.content.Intent getIntent(android.view.KeyCharacterMap kcm, int keyCode, int metaState) {
        int shortcutChar;
        boolean isShiftOn = android.view.KeyEvent.metaStateHasModifiers(metaState, 1);
        if (!isShiftOn && !android.view.KeyEvent.metaStateHasNoModifiers(metaState)) {
            return null;
        }
        android.content.Intent shortcutIntent = null;
        android.util.SparseArray<android.content.Intent> shortcutMap = isShiftOn ? this.mShiftShortcuts : this.mIntentShortcuts;
        int shortcutChar2 = kcm.get(keyCode, metaState);
        if (shortcutChar2 != 0) {
            android.content.Intent shortcutIntent2 = shortcutMap.get(shortcutChar2);
            shortcutIntent = shortcutIntent2;
        }
        if (shortcutIntent == null && (shortcutChar = java.lang.Character.toLowerCase(kcm.getDisplayLabel(keyCode))) != 0) {
            android.content.Intent shortcutIntent3 = shortcutMap.get(shortcutChar);
            android.content.Intent shortcutIntent4 = shortcutIntent3;
            if (shortcutIntent4 == null) {
                java.lang.String role = isShiftOn ? this.mShiftRoleShortcuts.get(shortcutChar) : this.mRoleShortcuts.get(shortcutChar);
                if (role != null) {
                    android.content.Intent shortcutIntent5 = getRoleLaunchIntent(role);
                    return shortcutIntent5;
                }
                return shortcutIntent4;
            }
            return shortcutIntent4;
        }
        return shortcutIntent;
    }

    private android.content.Intent getRoleLaunchIntent(java.lang.String role) {
        android.content.Intent intent = this.mRoleIntents.get(role);
        if (intent == null) {
            if (this.mRoleManager.isRoleAvailable(role)) {
                java.lang.String rolePackage = this.mRoleManager.getDefaultApplication(role);
                if (rolePackage != null) {
                    android.content.Intent intent2 = this.mPackageManager.getLaunchIntentForPackage(rolePackage);
                    intent2.putExtra(EXTRA_ROLE, role);
                    this.mRoleIntents.put(role, intent2);
                    return intent2;
                }
                android.util.Log.w(TAG, "No default application for role " + role);
                return intent;
            }
            android.util.Log.w(TAG, "Role " + role + " is not available.");
            return intent;
        }
        return intent;
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0182 A[Catch: IOException | XmlPullParserException -> 0x01bf, IOException | XmlPullParserException -> 0x01bf, TryCatch #2 {IOException | XmlPullParserException -> 0x01bf, blocks: (B:3:0x0004, B:4:0x0017, B:7:0x0022, B:10:0x0031, B:13:0x0068, B:13:0x0068, B:14:0x00a7, B:14:0x00a7, B:16:0x00af, B:16:0x00af, B:26:0x00c5, B:26:0x00c5, B:28:0x00ce, B:28:0x00ce, B:35:0x00f3, B:35:0x00f3, B:44:0x0182, B:44:0x0182, B:45:0x0188, B:45:0x0188, B:32:0x00d7, B:32:0x00d7, B:34:0x00ea, B:34:0x00ea, B:37:0x0104, B:37:0x0104, B:38:0x012c, B:38:0x012c, B:41:0x015a, B:41:0x015a, B:42:0x017c, B:42:0x017c, B:49:0x0193, B:49:0x0193, B:50:0x019a, B:50:0x019a, B:51:0x01a1, B:51:0x01a1), top: B:60:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0188 A[Catch: IOException | XmlPullParserException -> 0x01bf, IOException | XmlPullParserException -> 0x01bf, TryCatch #2 {IOException | XmlPullParserException -> 0x01bf, blocks: (B:3:0x0004, B:4:0x0017, B:7:0x0022, B:10:0x0031, B:13:0x0068, B:13:0x0068, B:14:0x00a7, B:14:0x00a7, B:16:0x00af, B:16:0x00af, B:26:0x00c5, B:26:0x00c5, B:28:0x00ce, B:28:0x00ce, B:35:0x00f3, B:35:0x00f3, B:44:0x0182, B:44:0x0182, B:45:0x0188, B:45:0x0188, B:32:0x00d7, B:32:0x00d7, B:34:0x00ea, B:34:0x00ea, B:37:0x0104, B:37:0x0104, B:38:0x012c, B:38:0x012c, B:41:0x015a, B:41:0x015a, B:42:0x017c, B:42:0x017c, B:49:0x0193, B:49:0x0193, B:50:0x019a, B:50:0x019a, B:51:0x01a1, B:51:0x01a1), top: B:60:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void loadShortcuts() {
        /*
            Method dump skipped, instruction units count: 454
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.policy.ModifierShortcutManager.loadShortcuts():void");
    }

    void registerShortcutKey(long shortcutCode, com.android.internal.policy.IShortcutService shortcutService) throws android.os.RemoteException {
        com.android.internal.policy.IShortcutService service = this.mShortcutKeyServices.get(shortcutCode);
        if (service != null && service.asBinder().pingBinder()) {
            throw new android.os.RemoteException("Key already exists.");
        }
        this.mShortcutKeyServices.put(shortcutCode, shortcutService);
    }

    private boolean handleShortcutService(int keyCode, int metaState) {
        long shortcutCode = keyCode;
        if ((metaState & 4096) != 0) {
            shortcutCode |= 17592186044416L;
        }
        if ((metaState & 2) != 0) {
            shortcutCode |= 8589934592L;
        }
        if ((metaState & 1) != 0) {
            shortcutCode |= 4294967296L;
        }
        if ((65536 & metaState) != 0) {
            shortcutCode |= 281474976710656L;
        }
        com.android.internal.policy.IShortcutService shortcutService = this.mShortcutKeyServices.get(shortcutCode);
        if (shortcutService != null) {
            try {
                shortcutService.notifyShortcutKeyPressed(shortcutCode);
                return true;
            } catch (android.os.RemoteException e) {
                this.mShortcutKeyServices.delete(shortcutCode);
                return true;
            }
        }
        return false;
    }

    private boolean handleIntentShortcut(android.view.KeyCharacterMap kcm, android.view.KeyEvent keyEvent, int metaState) {
        int keyCode = keyEvent.getKeyCode();
        if (this.mSearchKeyShortcutPending) {
            if (!kcm.isPrintingKey(keyCode)) {
                return false;
            }
            this.mConsumeSearchKeyUp = true;
            this.mSearchKeyShortcutPending = false;
        } else if ((458752 & metaState) != 0) {
            metaState &= -458753;
        } else {
            android.content.Intent intent = null;
            java.lang.String role = sApplicationLaunchKeyRoles.get(keyCode);
            java.lang.String category = sApplicationLaunchKeyCategories.get(keyCode);
            if (role != null) {
                intent = getRoleLaunchIntent(role);
            } else if (category != null) {
                intent = android.content.Intent.makeMainSelectorActivity("android.intent.action.MAIN", category);
            }
            if (intent == null) {
                return false;
            }
            intent.setFlags(268435456);
            try {
                this.mContext.startActivityAsUser(intent, android.os.UserHandle.CURRENT);
            } catch (android.content.ActivityNotFoundException e) {
                android.util.Slog.w(TAG, "Dropping application launch key because the activity to which it is registered was not found: keyCode=" + android.view.KeyEvent.keyCodeToString(keyCode) + ", category=" + category + " role=" + role);
            }
            logKeyboardShortcut(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.getLogEventFromIntent(intent));
            return true;
        }
        android.content.Intent shortcutIntent = getIntent(kcm, keyCode, metaState);
        if (shortcutIntent == null) {
            return false;
        }
        shortcutIntent.addFlags(268435456);
        try {
            this.mContext.startActivityAsUser(shortcutIntent, android.os.UserHandle.CURRENT);
        } catch (android.content.ActivityNotFoundException e2) {
            android.util.Slog.w(TAG, "Dropping shortcut key combination because the activity to which it is registered was not found: META+ or SEARCH" + android.view.KeyEvent.keyCodeToString(keyCode));
        }
        logKeyboardShortcut(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.getLogEventFromIntent(shortcutIntent));
        return true;
    }

    private void logKeyboardShortcut(final android.view.KeyEvent event, final com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent logEvent) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.policy.ModifierShortcutManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$logKeyboardShortcut$1(event, logEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleKeyboardLogging, reason: merged with bridge method [inline-methods] */
    public void lambda$logKeyboardShortcut$1(android.view.KeyEvent event, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent logEvent) {
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class);
        android.view.InputDevice inputDevice = inputManager != null ? inputManager.getInputDevice(event.getDeviceId()) : null;
        com.android.server.input.KeyboardMetricsCollector.logKeyboardSystemsEventReportedAtom(inputDevice, logEvent, event.getMetaState(), event.getKeyCode());
    }

    boolean interceptKey(android.view.KeyEvent event) {
        if (event.getRepeatCount() != 0) {
            return false;
        }
        int metaState = event.getModifiers();
        int keyCode = event.getKeyCode();
        if (keyCode == 84) {
            if (event.getAction() == 0) {
                this.mSearchKeyShortcutPending = true;
                this.mConsumeSearchKeyUp = false;
            } else {
                this.mSearchKeyShortcutPending = false;
                if (this.mConsumeSearchKeyUp) {
                    this.mConsumeSearchKeyUp = false;
                    return true;
                }
            }
            return false;
        }
        if (event.getAction() != 0) {
            return false;
        }
        android.view.KeyCharacterMap kcm = event.getKeyCharacterMap();
        return handleIntentShortcut(kcm, event, metaState) || handleShortcutService(keyCode, metaState);
    }
}
