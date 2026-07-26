package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class ShortcutRequestPinProcessor {
    private static final boolean DEBUG = com.android.server.pm.ShortcutService.DEBUG;
    private static final java.lang.String TAG = "ShortcutService";
    private final java.lang.Object mLock;
    private final com.android.server.pm.ShortcutService mService;

    private static abstract class PinItemRequestInner extends android.content.pm.IPinItemRequest.Stub {
        private boolean mAccepted;
        private final int mLauncherUid;
        protected final com.android.server.pm.ShortcutRequestPinProcessor mProcessor;
        private final android.content.IntentSender mResultIntent;

        private PinItemRequestInner(com.android.server.pm.ShortcutRequestPinProcessor processor, android.content.IntentSender resultIntent, int launcherUid) {
            this.mProcessor = processor;
            this.mResultIntent = resultIntent;
            this.mLauncherUid = launcherUid;
        }

        public android.content.pm.ShortcutInfo getShortcutInfo() {
            return null;
        }

        public android.appwidget.AppWidgetProviderInfo getAppWidgetProviderInfo() {
            return null;
        }

        public android.os.Bundle getExtras() {
            return null;
        }

        private boolean isCallerValid() {
            return this.mProcessor.isCallerUid(this.mLauncherUid);
        }

        public boolean isValid() {
            boolean z;
            if (!isCallerValid()) {
                return false;
            }
            synchronized (this) {
                z = this.mAccepted ? false : true;
            }
            return z;
        }

        public boolean accept(android.os.Bundle options) {
            if (!isCallerValid()) {
                throw new java.lang.SecurityException("Calling uid mismatch");
            }
            android.content.Intent extras = null;
            if (options != null) {
                try {
                    options.size();
                    extras = new android.content.Intent().putExtras(options);
                } catch (java.lang.RuntimeException e) {
                    throw new java.lang.IllegalArgumentException("options cannot be unparceled", e);
                }
            }
            synchronized (this) {
                if (this.mAccepted) {
                    throw new java.lang.IllegalStateException("accept() called already");
                }
                this.mAccepted = true;
            }
            if (tryAccept()) {
                this.mProcessor.sendResultIntent(this.mResultIntent, extras);
                return true;
            }
            return false;
        }

        protected boolean tryAccept() {
            return true;
        }
    }

    private static class PinAppWidgetRequestInner extends com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner {
        final android.appwidget.AppWidgetProviderInfo mAppWidgetProviderInfo;
        final android.os.Bundle mExtras;

        private PinAppWidgetRequestInner(com.android.server.pm.ShortcutRequestPinProcessor processor, android.content.IntentSender resultIntent, int launcherUid, android.appwidget.AppWidgetProviderInfo appWidgetProviderInfo, android.os.Bundle extras) {
            super(resultIntent, launcherUid);
            this.mAppWidgetProviderInfo = appWidgetProviderInfo;
            this.mExtras = extras;
        }

        @Override // com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner
        public android.appwidget.AppWidgetProviderInfo getAppWidgetProviderInfo() {
            return this.mAppWidgetProviderInfo;
        }

        @Override // com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner
        public android.os.Bundle getExtras() {
            return this.mExtras;
        }
    }

    private static class PinShortcutRequestInner extends com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner {
        public final java.lang.String launcherPackage;
        public final int launcherUserId;
        public final boolean preExisting;
        public final android.content.pm.ShortcutInfo shortcutForLauncher;
        public final android.content.pm.ShortcutInfo shortcutOriginal;

        private PinShortcutRequestInner(com.android.server.pm.ShortcutRequestPinProcessor processor, android.content.pm.ShortcutInfo shortcutOriginal, android.content.pm.ShortcutInfo shortcutForLauncher, android.content.IntentSender resultIntent, java.lang.String launcherPackage, int launcherUserId, int launcherUid, boolean preExisting) {
            super(resultIntent, launcherUid);
            this.shortcutOriginal = shortcutOriginal;
            this.shortcutForLauncher = shortcutForLauncher;
            this.launcherPackage = launcherPackage;
            this.launcherUserId = launcherUserId;
            this.preExisting = preExisting;
        }

        @Override // com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner
        public android.content.pm.ShortcutInfo getShortcutInfo() {
            return this.shortcutForLauncher;
        }

        @Override // com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner
        protected boolean tryAccept() {
            if (com.android.server.pm.ShortcutRequestPinProcessor.DEBUG) {
                android.util.Slog.d(com.android.server.pm.ShortcutRequestPinProcessor.TAG, "Launcher accepted shortcut. ID=" + this.shortcutOriginal.getId() + " package=" + this.shortcutOriginal.getPackage());
            }
            return this.mProcessor.directPinShortcut(this);
        }
    }

    public ShortcutRequestPinProcessor(com.android.server.pm.ShortcutService service, java.lang.Object lock) {
        this.mService = service;
        this.mLock = lock;
    }

    public boolean isRequestPinItemSupported(int callingUserId, int requestType) {
        return getRequestPinConfirmationActivity(callingUserId, requestType) != null;
    }

    public boolean requestPinItemLocked(android.content.pm.ShortcutInfo inShortcut, android.appwidget.AppWidgetProviderInfo inAppWidget, android.os.Bundle extras, int userId, android.content.IntentSender resultIntent) {
        android.content.pm.LauncherApps.PinItemRequest request;
        int requestType = inShortcut != null ? 1 : 2;
        android.util.Pair<android.content.ComponentName, java.lang.Integer> confirmActivity = getRequestPinConfirmationActivity(userId, requestType);
        if (confirmActivity == null) {
            android.util.Log.w(TAG, "Launcher doesn't support requestPinnedShortcut(). Shortcut not created.");
            return false;
        }
        int launcherUserId = ((java.lang.Integer) confirmActivity.second).intValue();
        this.mService.throwIfUserLockedL(launcherUserId);
        if (inShortcut != null) {
            request = requestPinShortcutLocked(inShortcut, resultIntent, ((android.content.ComponentName) confirmActivity.first).getPackageName(), ((java.lang.Integer) confirmActivity.second).intValue());
        } else {
            int launcherUid = this.mService.injectGetPackageUid(((android.content.ComponentName) confirmActivity.first).getPackageName(), launcherUserId);
            request = new android.content.pm.LauncherApps.PinItemRequest(new com.android.server.pm.ShortcutRequestPinProcessor.PinAppWidgetRequestInner(resultIntent, launcherUid, inAppWidget, extras), 2);
        }
        if (!this.mService.getWrapper().getExtImpl().adjustRequestPinItemReturn(((android.content.ComponentName) confirmActivity.first).getPackageName(), inShortcut)) {
            return startRequestConfirmActivity((android.content.ComponentName) confirmActivity.first, launcherUserId, request, requestType);
        }
        request.accept();
        return true;
    }

    public android.content.Intent createShortcutResultIntent(android.content.pm.ShortcutInfo inShortcut, int userId) {
        int launcherUserId = this.mService.getParentOrSelfUserId(userId);
        java.lang.String defaultLauncher = this.mService.getDefaultLauncher(launcherUserId);
        if (defaultLauncher == null) {
            android.util.Log.e(TAG, "Default launcher not found.");
            return null;
        }
        this.mService.throwIfUserLockedL(launcherUserId);
        android.content.pm.LauncherApps.PinItemRequest request = requestPinShortcutLocked(inShortcut, null, defaultLauncher, launcherUserId);
        return new android.content.Intent().putExtra("android.content.pm.extra.PIN_ITEM_REQUEST", request);
    }

    private android.content.pm.LauncherApps.PinItemRequest requestPinShortcutLocked(android.content.pm.ShortcutInfo inShortcut, android.content.IntentSender resultIntentOriginal, java.lang.String launcherPackage, int launcherUserId) {
        android.content.IntentSender resultIntentToSend;
        android.content.pm.ShortcutInfo shortcutForLauncher;
        com.android.server.pm.ShortcutPackage ps = this.mService.getPackageShortcutsForPublisherLocked(inShortcut.getPackage(), inShortcut.getUserId());
        android.content.pm.ShortcutInfo existing = ps.findShortcutById(inShortcut.getId());
        boolean z = false;
        boolean existsAlready = existing != null;
        if (existsAlready && existing.isVisibleToPublisher()) {
            z = true;
        }
        boolean existingIsVisible = z;
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestPinnedShortcut: package=" + inShortcut.getPackage() + " existsAlready=" + existsAlready + " existingIsVisible=" + existingIsVisible + " shortcut=" + inShortcut.toInsecureString());
        }
        android.content.IntentSender resultIntentToSend2 = resultIntentOriginal;
        if (existsAlready) {
            validateExistingShortcut(existing);
            boolean isAlreadyPinned = this.mService.getLauncherShortcutsLocked(launcherPackage, existing.getUserId(), launcherUserId).hasPinned(existing);
            if (isAlreadyPinned) {
                sendResultIntent(resultIntentOriginal, null);
                resultIntentToSend2 = null;
            }
            android.content.pm.ShortcutInfo shortcutForLauncher2 = existing.clone(27);
            if (!isAlreadyPinned) {
                shortcutForLauncher2.clearFlags(2);
            }
            resultIntentToSend = resultIntentToSend2;
            shortcutForLauncher = shortcutForLauncher2;
        } else {
            if (inShortcut.getActivity() == null) {
                inShortcut.setActivity(this.mService.injectGetDefaultMainActivity(inShortcut.getPackage(), inShortcut.getUserId()));
            }
            this.mService.validateShortcutForPinRequest(inShortcut);
            inShortcut.resolveResourceStrings(this.mService.injectGetResourcesForApplicationAsUser(inShortcut.getPackage(), inShortcut.getUserId()));
            if (DEBUG) {
                android.util.Slog.d(TAG, "Resolved shortcut=" + inShortcut.toInsecureString());
            }
            resultIntentToSend = resultIntentToSend2;
            shortcutForLauncher = inShortcut.clone(26);
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Sending to launcher=" + shortcutForLauncher.toInsecureString());
        }
        com.android.server.pm.ShortcutRequestPinProcessor.PinShortcutRequestInner inner = new com.android.server.pm.ShortcutRequestPinProcessor.PinShortcutRequestInner(inShortcut, shortcutForLauncher, resultIntentToSend, launcherPackage, launcherUserId, this.mService.injectGetPackageUid(launcherPackage, launcherUserId), existsAlready);
        return new android.content.pm.LauncherApps.PinItemRequest(inner, 1);
    }

    private void validateExistingShortcut(android.content.pm.ShortcutInfo shortcutInfo) {
        com.android.internal.util.Preconditions.checkArgument(shortcutInfo.isEnabled(), "Shortcut ID=" + shortcutInfo + " already exists but disabled.");
    }

    private boolean startRequestConfirmActivity(android.content.ComponentName activity, int launcherUserId, android.content.pm.LauncherApps.PinItemRequest request, int requestType) {
        java.lang.String action = requestType == 1 ? "android.content.pm.action.CONFIRM_PIN_SHORTCUT" : "android.content.pm.action.CONFIRM_PIN_APPWIDGET";
        android.content.Intent confirmIntent = new android.content.Intent(action);
        confirmIntent.setComponent(activity);
        confirmIntent.putExtra("android.content.pm.extra.PIN_ITEM_REQUEST", request);
        confirmIntent.addFlags(268468224);
        this.mService.getWrapper().getExtImpl().startRequestConfirmActivity(request, confirmIntent);
        long token = this.mService.injectClearCallingIdentity();
        try {
            try {
                this.mService.mContext.startActivityAsUser(confirmIntent, android.os.UserHandle.of(launcherUserId));
                return true;
            } catch (java.lang.RuntimeException e) {
                android.util.Log.e(TAG, "Unable to start activity " + activity, e);
                this.mService.injectRestoreCallingIdentity(token);
                return false;
            }
        } finally {
            this.mService.injectRestoreCallingIdentity(token);
        }
    }

    android.util.Pair<android.content.ComponentName, java.lang.Integer> getRequestPinConfirmationActivity(int callingUserId, int requestType) {
        if (!this.mService.areShortcutsSupportedOnHomeScreen(callingUserId)) {
            return null;
        }
        int launcherUserId = this.mService.getParentOrSelfUserId(callingUserId);
        java.lang.String defaultLauncher = this.mService.getDefaultLauncher(launcherUserId);
        if (defaultLauncher == null) {
            android.util.Log.e(TAG, "Default launcher not found.");
            return null;
        }
        android.content.ComponentName activity = this.mService.injectGetPinConfirmationActivity(defaultLauncher, launcherUserId, requestType);
        if (activity == null) {
            return null;
        }
        return android.util.Pair.create(activity, java.lang.Integer.valueOf(launcherUserId));
    }

    public void sendResultIntent(android.content.IntentSender intent, android.content.Intent extras) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Sending result intent.");
        }
        this.mService.injectSendIntentSender(intent, extras);
    }

    public boolean isCallerUid(int uid) {
        return uid == this.mService.injectBinderCallingUid();
    }

    public boolean directPinShortcut(com.android.server.pm.ShortcutRequestPinProcessor.PinShortcutRequestInner request) {
        android.content.pm.ShortcutInfo original = request.shortcutOriginal;
        int appUserId = original.getUserId();
        java.lang.String appPackageName = original.getPackage();
        int launcherUserId = request.launcherUserId;
        java.lang.String launcherPackage = request.launcherPackage;
        java.lang.String shortcutId = original.getId();
        synchronized (this.mLock) {
            if (this.mService.isUserUnlockedL(appUserId) && this.mService.isUserUnlockedL(request.launcherUserId)) {
                com.android.server.pm.ShortcutLauncher launcher = this.mService.getLauncherShortcutsLocked(launcherPackage, appUserId, launcherUserId);
                launcher.attemptToRestoreIfNeededAndSave();
                if (launcher.hasPinned(original)) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Shortcut " + original + " already pinned.");
                    }
                    return true;
                }
                com.android.server.pm.ShortcutPackage ps = this.mService.getPackageShortcutsForPublisherLocked(appPackageName, appUserId);
                android.content.pm.ShortcutInfo current = ps.findShortcutById(shortcutId);
                try {
                    if (current == null) {
                        this.mService.validateShortcutForPinRequest(original);
                    } else {
                        validateExistingShortcut(current);
                    }
                    if (current == null) {
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "Temporarily adding " + shortcutId + " as dynamic");
                        }
                        if (original.getActivity() == null) {
                            original.setActivity(this.mService.getDummyMainActivity(appPackageName));
                        }
                        ps.addOrReplaceDynamicShortcut(original);
                    }
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Pinning " + shortcutId);
                    }
                    launcher.addPinnedShortcut(appPackageName, appUserId, shortcutId, true);
                    if (current == null) {
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "Removing " + shortcutId + " as dynamic");
                        }
                        ps.deleteDynamicWithId(shortcutId, false, false);
                    }
                    ps.adjustRanks();
                    java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = java.util.Collections.singletonList(ps.findShortcutById(shortcutId));
                    this.mService.verifyStates();
                    this.mService.packageShortcutsChanged(ps, changedShortcuts, null);
                    return true;
                } catch (java.lang.RuntimeException e) {
                    android.util.Log.w(TAG, "Unable to pin shortcut: " + e.getMessage());
                    return false;
                }
            }
            android.util.Log.w(TAG, "User is locked now.");
            return false;
        }
    }
}
