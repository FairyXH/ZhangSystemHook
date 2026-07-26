package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
class KeyboardLayoutManager implements android.hardware.input.InputManager.InputDeviceListener {
    private static final int MSG_RELOAD_KEYBOARD_LAYOUTS = 2;
    private static final int MSG_UPDATE_EXISTING_DEVICES = 1;
    private static final int MSG_UPDATE_KEYBOARD_LAYOUTS = 3;
    private static final java.lang.String VENDOR_ID = "22d9";
    private final android.content.Context mContext;
    private com.android.server.input.KeyboardLayoutManager.ImeInfo mCurrentImeInfo;
    private final com.android.server.input.PersistentDataStore mDataStore;
    private final android.os.Handler mHandler;
    private final com.android.server.input.NativeInputManagerService mNative;
    private static final java.lang.String TAG = "KeyboardLayoutManager";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private final android.util.SparseArray<com.android.server.input.KeyboardLayoutManager.KeyboardConfiguration> mConfiguredKeyboards = new android.util.SparseArray<>();
    private final android.util.SparseArray<java.lang.Boolean> mOfficialKeyboard = new android.util.SparseArray<>();
    private final java.util.Map<java.lang.String, android.hardware.input.KeyboardLayoutSelectionResult> mKeyboardLayoutCache = new android.util.ArrayMap();
    private java.util.HashSet<java.lang.String> mAvailableLayouts = new java.util.HashSet<>();
    private final java.lang.Object mImeInfoLock = new java.lang.Object();

    /* JADX INFO: Access modifiers changed from: private */
    interface KeyboardLayoutVisitor {
        void visitKeyboardLayout(android.content.res.Resources resources, int i, android.hardware.input.KeyboardLayout keyboardLayout);
    }

    KeyboardLayoutManager(android.content.Context context, com.android.server.input.NativeInputManagerService nativeService, com.android.server.input.PersistentDataStore dataStore, android.os.Looper looper) {
        this.mContext = context;
        this.mNative = nativeService;
        this.mDataStore = dataStore;
        this.mHandler = new android.os.Handler(looper, new android.os.Handler.Callback() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda5
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(android.os.Message message) {
                return this.f$0.handleMessage(message);
            }
        }, true);
    }

    public void systemRunning() {
        android.content.IntentFilter filter = new android.content.IntentFilter("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_CHANGED");
        filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addDataScheme("package");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.input.KeyboardLayoutManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) throws java.lang.Throwable {
                com.android.server.input.KeyboardLayoutManager.this.updateKeyboardLayouts();
            }
        }, filter, null, this.mHandler);
        this.mHandler.sendEmptyMessage(3);
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) java.util.Objects.requireNonNull((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class));
        inputManager.registerInputDeviceListener(this, this.mHandler);
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 1, inputManager.getInputDeviceIds());
        this.mHandler.sendMessage(msg);
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceAdded(int deviceId) {
        android.view.InputDevice inputDevice = getInputDevice(deviceId);
        if (inputDevice != null && java.lang.Integer.toHexString(inputDevice.getVendorId()).equals(VENDOR_ID) && !inputDevice.isVirtual() && inputDevice.isFullKeyboard()) {
            this.mOfficialKeyboard.put(deviceId, true);
        } else {
            this.mOfficialKeyboard.put(deviceId, false);
        }
        onInputDeviceChangedInternal(deviceId, true);
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceRemoved(int deviceId) {
        this.mConfiguredKeyboards.remove(deviceId);
        boolean notToShowNotification = this.mOfficialKeyboard.get(deviceId) != null && this.mOfficialKeyboard.get(deviceId).booleanValue();
        this.mOfficialKeyboard.remove(deviceId);
        if (notToShowNotification) {
            return;
        }
        maybeUpdateNotification();
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceChanged(int deviceId) {
        onInputDeviceChangedInternal(deviceId, false);
    }

    private void onInputDeviceChangedInternal(int i, boolean z) {
        com.android.server.input.KeyboardLayoutManager.KeyboardConfiguration keyboardConfiguration;
        android.view.InputDevice inputDevice = getInputDevice(i);
        if (inputDevice == null || inputDevice.isVirtual() || !inputDevice.isFullKeyboard()) {
            return;
        }
        boolean z2 = false;
        com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier keyboardIdentifier = new com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier(inputDevice);
        com.android.server.input.KeyboardLayoutManager.KeyboardConfiguration keyboardConfiguration2 = this.mConfiguredKeyboards.get(i);
        if (keyboardConfiguration2 != null) {
            keyboardConfiguration = keyboardConfiguration2;
        } else {
            com.android.server.input.KeyboardLayoutManager.KeyboardConfiguration keyboardConfiguration3 = new com.android.server.input.KeyboardLayoutManager.KeyboardConfiguration(i);
            this.mConfiguredKeyboards.put(i, keyboardConfiguration3);
            keyboardConfiguration = keyboardConfiguration3;
        }
        boolean z3 = false;
        java.util.HashSet hashSet = new java.util.HashSet();
        java.util.List<com.android.server.input.KeyboardLayoutManager.ImeInfo> imeInfoListForLayoutMapping = getImeInfoListForLayoutMapping();
        java.util.ArrayList arrayList = new java.util.ArrayList();
        boolean z4 = this.mOfficialKeyboard.get(i) != null && this.mOfficialKeyboard.get(i).booleanValue();
        java.util.Iterator<com.android.server.input.KeyboardLayoutManager.ImeInfo> it = imeInfoListForLayoutMapping.iterator();
        boolean z5 = false;
        while (it.hasNext()) {
            android.hardware.input.KeyboardLayoutSelectionResult keyboardLayoutForInputDeviceInternal = getKeyboardLayoutForInputDeviceInternal(keyboardIdentifier, it.next());
            if (keyboardLayoutForInputDeviceInternal.getLayoutDescriptor() != null) {
                hashSet.add(keyboardLayoutForInputDeviceInternal.getLayoutDescriptor());
            } else {
                z5 = true;
            }
            arrayList.add(keyboardLayoutForInputDeviceInternal);
            z5 = z5;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Layouts selected for input device: " + keyboardIdentifier + " -> selectedLayouts: " + hashSet);
        }
        if (z5) {
            hashSet.clear();
        }
        keyboardConfiguration.setConfiguredLayouts(hashSet);
        synchronized (this.mDataStore) {
            try {
                java.lang.String string = keyboardIdentifier.toString();
                if (this.mDataStore.setSelectedKeyboardLayouts(string, hashSet)) {
                    z3 = true;
                }
                if (z) {
                    logKeyboardConfigurationEvent(inputDevice, imeInfoListForLayoutMapping, arrayList, this.mDataStore.hasInputDeviceEntry(string) ? false : true);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
        if (z3 && !z4) {
            maybeUpdateNotification();
        }
    }

    private static boolean isCompatibleLocale(java.util.Locale systemLocale, java.util.Locale keyboardLocale) {
        if (systemLocale.getLanguage().equals(keyboardLocale.getLanguage())) {
            return android.text.TextUtils.isEmpty(systemLocale.getCountry()) || android.text.TextUtils.isEmpty(keyboardLocale.getCountry()) || systemLocale.getCountry().equals(keyboardLocale.getCountry());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKeyboardLayouts() throws java.lang.Throwable {
        final java.util.HashSet<java.lang.String> availableKeyboardLayouts = new java.util.HashSet<>();
        visitAllKeyboardLayouts(new com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda6
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(android.content.res.Resources resources, int i, android.hardware.input.KeyboardLayout keyboardLayout) {
                availableKeyboardLayouts.add(keyboardLayout.getDescriptor());
            }
        });
        if (this.mAvailableLayouts.equals(availableKeyboardLayouts)) {
            return;
        }
        this.mAvailableLayouts = availableKeyboardLayouts;
        synchronized (this.mDataStore) {
            try {
                this.mDataStore.removeUninstalledKeyboardLayouts(availableKeyboardLayouts);
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
        synchronized (this.mKeyboardLayoutCache) {
            this.mKeyboardLayoutCache.clear();
        }
        reloadKeyboardLayouts();
    }

    public android.hardware.input.KeyboardLayout[] getKeyboardLayouts() throws java.lang.Throwable {
        final java.util.ArrayList<android.hardware.input.KeyboardLayout> list = new java.util.ArrayList<>();
        visitAllKeyboardLayouts(new com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda3
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(android.content.res.Resources resources, int i, android.hardware.input.KeyboardLayout keyboardLayout) {
                list.add(keyboardLayout);
            }
        });
        return (android.hardware.input.KeyboardLayout[]) list.toArray(new android.hardware.input.KeyboardLayout[0]);
    }

    public android.hardware.input.KeyboardLayout getKeyboardLayout(java.lang.String keyboardLayoutDescriptor) throws java.lang.Throwable {
        java.util.Objects.requireNonNull(keyboardLayoutDescriptor, "keyboardLayoutDescriptor must not be null");
        final android.hardware.input.KeyboardLayout[] result = new android.hardware.input.KeyboardLayout[1];
        visitKeyboardLayout(keyboardLayoutDescriptor, new com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda0
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(android.content.res.Resources resources, int i, android.hardware.input.KeyboardLayout keyboardLayout) {
                com.android.server.input.KeyboardLayoutManager.lambda$getKeyboardLayout$2(result, resources, i, keyboardLayout);
            }
        });
        if (result[0] == null) {
            android.util.Slog.w(TAG, "Could not get keyboard layout with descriptor '" + keyboardLayoutDescriptor + "'.");
        }
        return result[0];
    }

    static /* synthetic */ void lambda$getKeyboardLayout$2(android.hardware.input.KeyboardLayout[] result, android.content.res.Resources resources, int keyboardLayoutResId, android.hardware.input.KeyboardLayout layout) {
        result[0] = layout;
    }

    public android.view.KeyCharacterMap getKeyCharacterMap(java.lang.String layoutDescriptor) throws java.lang.Throwable {
        final java.lang.String[] overlay = new java.lang.String[1];
        visitKeyboardLayout(layoutDescriptor, new com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda4
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(android.content.res.Resources resources, int i, android.hardware.input.KeyboardLayout keyboardLayout) {
                com.android.server.input.KeyboardLayoutManager.lambda$getKeyCharacterMap$3(overlay, resources, i, keyboardLayout);
            }
        });
        if (android.text.TextUtils.isEmpty(overlay[0])) {
            return android.view.KeyCharacterMap.load(-1);
        }
        return android.view.KeyCharacterMap.load(layoutDescriptor, overlay[0]);
    }

    static /* synthetic */ void lambda$getKeyCharacterMap$3(java.lang.String[] overlay, android.content.res.Resources resources, int keyboardLayoutResId, android.hardware.input.KeyboardLayout layout) {
        try {
            java.io.InputStreamReader stream = new java.io.InputStreamReader(resources.openRawResource(keyboardLayoutResId));
            try {
                overlay[0] = libcore.io.Streams.readFully(stream);
                stream.close();
            } catch (java.lang.Throwable th) {
                try {
                    stream.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (android.content.res.Resources.NotFoundException | java.io.IOException e) {
        }
    }

    private void visitAllKeyboardLayouts(com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor visitor) throws java.lang.Throwable {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.content.Intent intent = new android.content.Intent("android.hardware.input.action.QUERY_KEYBOARD_LAYOUTS");
        for (android.content.pm.ResolveInfo resolveInfo : pm.queryBroadcastReceiversAsUser(intent, 786560, 0)) {
            if (resolveInfo != null && resolveInfo.activityInfo != null) {
                android.content.pm.ActivityInfo activityInfo = resolveInfo.activityInfo;
                int priority = resolveInfo.priority;
                visitKeyboardLayoutsInPackage(pm, activityInfo, null, priority, visitor);
            }
        }
    }

    private void visitKeyboardLayout(java.lang.String keyboardLayoutDescriptor, com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor visitor) throws java.lang.Throwable {
        com.android.server.input.KeyboardLayoutManager.KeyboardLayoutDescriptor d = com.android.server.input.KeyboardLayoutManager.KeyboardLayoutDescriptor.parse(keyboardLayoutDescriptor);
        if (d != null) {
            android.content.pm.PackageManager pm = this.mContext.getPackageManager();
            try {
                android.content.pm.ActivityInfo receiver = pm.getReceiverInfo(new android.content.ComponentName(d.packageName, d.receiverName), 786560);
                visitKeyboardLayoutsInPackage(pm, receiver, d.keyboardLayoutName, 0, visitor);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:92:0x0104 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void visitKeyboardLayoutsInPackage(android.content.pm.PackageManager r27, android.content.pm.ActivityInfo r28, java.lang.String r29, int r30, com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor r31) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 527
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.input.KeyboardLayoutManager.visitKeyboardLayoutsInPackage(android.content.pm.PackageManager, android.content.pm.ActivityInfo, java.lang.String, int, com.android.server.input.KeyboardLayoutManager$KeyboardLayoutVisitor):void");
    }

    private static android.os.LocaleList getLocalesFromLanguageTags(java.lang.String languageTags) {
        if (android.text.TextUtils.isEmpty(languageTags)) {
            return android.os.LocaleList.getEmptyLocaleList();
        }
        return android.os.LocaleList.forLanguageTags(languageTags.replace('|', ','));
    }

    public java.lang.String[] getKeyboardLayoutOverlay(android.hardware.input.InputDeviceIdentifier identifier, java.lang.String languageTag, java.lang.String layoutType) throws java.lang.Throwable {
        java.lang.String keyboardLayoutDescriptor;
        synchronized (this.mImeInfoLock) {
            keyboardLayoutDescriptor = getKeyboardLayoutForInputDeviceInternal(new com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier(identifier, languageTag, layoutType), this.mCurrentImeInfo).getLayoutDescriptor();
        }
        if (keyboardLayoutDescriptor == null) {
            return null;
        }
        final java.lang.String[] result = new java.lang.String[2];
        visitKeyboardLayout(keyboardLayoutDescriptor, new com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda1
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(android.content.res.Resources resources, int i, android.hardware.input.KeyboardLayout keyboardLayout) {
                com.android.server.input.KeyboardLayoutManager.lambda$getKeyboardLayoutOverlay$4(result, resources, i, keyboardLayout);
            }
        });
        if (result[0] == null) {
            android.util.Slog.w(TAG, "Could not get keyboard layout with descriptor '" + keyboardLayoutDescriptor + "'.");
            return null;
        }
        return result;
    }

    static /* synthetic */ void lambda$getKeyboardLayoutOverlay$4(java.lang.String[] result, android.content.res.Resources resources, int keyboardLayoutResId, android.hardware.input.KeyboardLayout layout) {
        try {
            java.io.InputStreamReader stream = new java.io.InputStreamReader(resources.openRawResource(keyboardLayoutResId));
            try {
                result[0] = layout.getDescriptor();
                result[1] = libcore.io.Streams.readFully(stream);
                stream.close();
            } catch (java.lang.Throwable th) {
                try {
                    stream.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (android.content.res.Resources.NotFoundException | java.io.IOException e) {
        }
    }

    public android.hardware.input.KeyboardLayoutSelectionResult getKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, int userId, android.view.inputmethod.InputMethodInfo imeInfo, android.view.inputmethod.InputMethodSubtype imeSubtype) {
        android.view.InputDevice inputDevice = getInputDevice(identifier);
        if (inputDevice == null || inputDevice.isVirtual() || !inputDevice.isFullKeyboard()) {
            return android.hardware.input.KeyboardLayoutSelectionResult.FAILED;
        }
        com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier keyboardIdentifier = new com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier(inputDevice);
        android.hardware.input.KeyboardLayoutSelectionResult result = getKeyboardLayoutForInputDeviceInternal(keyboardIdentifier, new com.android.server.input.KeyboardLayoutManager.ImeInfo(userId, imeInfo, imeSubtype));
        if (DEBUG) {
            android.util.Slog.d(TAG, "getKeyboardLayoutForInputDevice() " + identifier.toString() + ", userId : " + userId + ", subtype = " + imeSubtype + " -> " + result);
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier inputDeviceIdentifier, int i, android.view.inputmethod.InputMethodInfo inputMethodInfo, android.view.inputmethod.InputMethodSubtype inputMethodSubtype, java.lang.String str) {
        java.util.Objects.requireNonNull(str, "keyboardLayoutDescriptor must not be null");
        android.view.InputDevice inputDevice = getInputDevice(inputDeviceIdentifier);
        if (inputDevice == null || inputDevice.isVirtual() || !inputDevice.isFullKeyboard()) {
            return;
        }
        com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier keyboardIdentifier = new com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier(inputDevice);
        java.lang.String string = new com.android.server.input.KeyboardLayoutManager.LayoutKey(keyboardIdentifier, new com.android.server.input.KeyboardLayoutManager.ImeInfo(i, inputMethodInfo, inputMethodSubtype)).toString();
        synchronized (this.mDataStore) {
            try {
                if (this.mDataStore.setKeyboardLayout(keyboardIdentifier.toString(), string, str)) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "setKeyboardLayoutForInputDevice() " + inputDeviceIdentifier + " key: " + string + " keyboardLayoutDescriptor: " + str);
                    }
                    this.mHandler.sendEmptyMessage(2);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    public android.hardware.input.KeyboardLayout[] getKeyboardLayoutListForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, int userId, android.view.inputmethod.InputMethodInfo imeInfo, android.view.inputmethod.InputMethodSubtype imeSubtype) {
        android.view.InputDevice inputDevice = getInputDevice(identifier);
        if (inputDevice == null || inputDevice.isVirtual() || !inputDevice.isFullKeyboard()) {
            return new android.hardware.input.KeyboardLayout[0];
        }
        return getKeyboardLayoutListForInputDeviceInternal(new com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier(inputDevice), new com.android.server.input.KeyboardLayoutManager.ImeInfo(userId, imeInfo, imeSubtype));
    }

    private android.hardware.input.KeyboardLayout[] getKeyboardLayoutListForInputDeviceInternal(final com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier keyboardIdentifier, com.android.server.input.KeyboardLayoutManager.ImeInfo imeInfo) throws java.lang.Throwable {
        final java.lang.String userSelectedLayout;
        java.lang.String imeLanguageTag;
        java.lang.String layoutKey = new com.android.server.input.KeyboardLayoutManager.LayoutKey(keyboardIdentifier, imeInfo).toString();
        synchronized (this.mDataStore) {
            userSelectedLayout = this.mDataStore.getKeyboardLayout(keyboardIdentifier.toString(), layoutKey);
        }
        final java.util.ArrayList<android.hardware.input.KeyboardLayout> potentialLayouts = new java.util.ArrayList<>();
        if (imeInfo == null || imeInfo.mImeSubtype == null) {
            imeLanguageTag = "";
        } else {
            android.icu.util.ULocale imeLocale = imeInfo.mImeSubtype.getPhysicalKeyboardHintLanguageTag();
            imeLanguageTag = imeLocale != null ? imeLocale.toLanguageTag() : imeInfo.mImeSubtype.getCanonicalizedLanguageTag();
        }
        final java.lang.String str = imeLanguageTag;
        visitAllKeyboardLayouts(new com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager.2
            boolean mDeviceSpecificLayoutAvailable;

            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public void visitKeyboardLayout(android.content.res.Resources resources, int keyboardLayoutResId, android.hardware.input.KeyboardLayout layout) {
                if (layout.getVendorId() == keyboardIdentifier.mIdentifier.getVendorId() && layout.getProductId() == keyboardIdentifier.mIdentifier.getProductId()) {
                    if (!this.mDeviceSpecificLayoutAvailable) {
                        this.mDeviceSpecificLayoutAvailable = true;
                        potentialLayouts.clear();
                    }
                    potentialLayouts.add(layout);
                    return;
                }
                if (layout.getVendorId() == -1 && layout.getProductId() == -1 && !this.mDeviceSpecificLayoutAvailable && com.android.server.input.KeyboardLayoutManager.isLayoutCompatibleWithLanguageTag(layout, str)) {
                    potentialLayouts.add(layout);
                } else if (layout.getDescriptor().equals(userSelectedLayout)) {
                    potentialLayouts.add(layout);
                }
            }
        });
        java.util.Collections.sort(potentialLayouts);
        return (android.hardware.input.KeyboardLayout[]) potentialLayouts.toArray(new android.hardware.input.KeyboardLayout[0]);
    }

    public void onInputMethodSubtypeChanged(int userId, com.android.internal.inputmethod.InputMethodSubtypeHandle subtypeHandle, android.view.inputmethod.InputMethodSubtype subtype) {
        if (subtypeHandle == null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "No InputMethod is running, ignoring change");
                return;
            }
            return;
        }
        synchronized (this.mImeInfoLock) {
            if (this.mCurrentImeInfo == null || !subtypeHandle.equals(this.mCurrentImeInfo.mImeSubtypeHandle) || this.mCurrentImeInfo.mUserId != userId) {
                this.mCurrentImeInfo = new com.android.server.input.KeyboardLayoutManager.ImeInfo(userId, subtypeHandle, subtype);
                this.mHandler.sendEmptyMessage(2);
                if (DEBUG) {
                    android.util.Slog.d(TAG, "InputMethodSubtype changed: userId=" + userId + " subtypeHandle=" + subtypeHandle);
                }
            }
        }
    }

    private android.hardware.input.KeyboardLayoutSelectionResult getKeyboardLayoutForInputDeviceInternal(com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier keyboardIdentifier, com.android.server.input.KeyboardLayoutManager.ImeInfo imeInfo) {
        java.lang.String layoutKey = new com.android.server.input.KeyboardLayoutManager.LayoutKey(keyboardIdentifier, imeInfo).toString();
        synchronized (this.mDataStore) {
            java.lang.String layout = this.mDataStore.getKeyboardLayout(keyboardIdentifier.toString(), layoutKey);
            if (layout != null) {
                return new android.hardware.input.KeyboardLayoutSelectionResult(layout, 1);
            }
            synchronized (this.mKeyboardLayoutCache) {
                if (this.mKeyboardLayoutCache.containsKey(layoutKey)) {
                    return this.mKeyboardLayoutCache.get(layoutKey);
                }
                android.hardware.input.KeyboardLayout[] layoutList = getKeyboardLayoutListForInputDeviceInternal(keyboardIdentifier, imeInfo);
                android.hardware.input.KeyboardLayoutSelectionResult result = getDefaultKeyboardLayoutBasedOnImeInfo(keyboardIdentifier, imeInfo, layoutList);
                this.mKeyboardLayoutCache.put(layoutKey, result);
                return result;
            }
        }
    }

    private static android.hardware.input.KeyboardLayoutSelectionResult getDefaultKeyboardLayoutBasedOnImeInfo(com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier keyboardIdentifier, com.android.server.input.KeyboardLayoutManager.ImeInfo imeInfo, android.hardware.input.KeyboardLayout[] layoutList) {
        java.lang.String layoutDesc;
        java.util.Arrays.sort(layoutList);
        for (android.hardware.input.KeyboardLayout layout : layoutList) {
            if (layout.getVendorId() == keyboardIdentifier.mIdentifier.getVendorId() && layout.getProductId() == keyboardIdentifier.mIdentifier.getProductId()) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "getDefaultKeyboardLayoutBasedOnImeInfo() : Layout found based on vendor and product Ids. " + keyboardIdentifier + " : " + layout.getDescriptor());
                }
                return new android.hardware.input.KeyboardLayoutSelectionResult(layout.getDescriptor(), 2);
            }
        }
        java.lang.String inputLanguageTag = keyboardIdentifier.mLanguageTag;
        if (inputLanguageTag != null && (layoutDesc = getMatchingLayoutForProvidedLanguageTagAndLayoutType(layoutList, inputLanguageTag, keyboardIdentifier.mLayoutType)) != null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "getDefaultKeyboardLayoutBasedOnImeInfo() : Layout found based on HW information (Language tag and Layout type). " + keyboardIdentifier + " : " + layoutDesc);
            }
            return new android.hardware.input.KeyboardLayoutSelectionResult(layoutDesc, 2);
        }
        if (imeInfo == null || imeInfo.mImeSubtypeHandle == null || imeInfo.mImeSubtype == null) {
            return android.hardware.input.KeyboardLayoutSelectionResult.FAILED;
        }
        android.view.inputmethod.InputMethodSubtype subtype = imeInfo.mImeSubtype;
        android.icu.util.ULocale pkLocale = subtype.getPhysicalKeyboardHintLanguageTag();
        java.lang.String pkLanguageTag = pkLocale != null ? pkLocale.toLanguageTag() : subtype.getCanonicalizedLanguageTag();
        java.lang.String layoutDesc2 = getMatchingLayoutForProvidedLanguageTagAndLayoutType(layoutList, pkLanguageTag, subtype.getPhysicalKeyboardHintLayoutType());
        if (DEBUG) {
            android.util.Slog.d(TAG, "getDefaultKeyboardLayoutBasedOnImeInfo() : Layout found based on IME locale matching. " + keyboardIdentifier + " : " + layoutDesc2);
        }
        if (layoutDesc2 != null) {
            return new android.hardware.input.KeyboardLayoutSelectionResult(layoutDesc2, 3);
        }
        return android.hardware.input.KeyboardLayoutSelectionResult.FAILED;
    }

    private static java.lang.String getMatchingLayoutForProvidedLanguageTagAndLayoutType(android.hardware.input.KeyboardLayout[] layoutList, java.lang.String languageTag, java.lang.String layoutType) {
        if (layoutType == null || !android.hardware.input.KeyboardLayout.isLayoutTypeValid(layoutType)) {
            layoutType = "undefined";
        }
        java.util.List<android.hardware.input.KeyboardLayout> layoutsFilteredByLayoutType = new java.util.ArrayList<>();
        for (android.hardware.input.KeyboardLayout layout : layoutList) {
            if (layout.getLayoutType().equals(layoutType)) {
                layoutsFilteredByLayoutType.add(layout);
            }
        }
        java.lang.String layoutDesc = getMatchingLayoutForProvidedLanguageTag(layoutsFilteredByLayoutType, languageTag);
        if (layoutDesc != null) {
            return layoutDesc;
        }
        return getMatchingLayoutForProvidedLanguageTag(java.util.Arrays.asList(layoutList), languageTag);
    }

    private static java.lang.String getMatchingLayoutForProvidedLanguageTag(java.util.List<android.hardware.input.KeyboardLayout> layoutList, java.lang.String languageTag) {
        java.util.Locale locale = java.util.Locale.forLanguageTag(languageTag);
        java.lang.String bestMatchingLayout = null;
        float bestMatchingLayoutScore = 0.0f;
        for (android.hardware.input.KeyboardLayout layout : layoutList) {
            android.os.LocaleList locales = layout.getLocales();
            for (int i = 0; i < locales.size(); i++) {
                java.util.Locale l = locales.get(i);
                if (l != null && l.getLanguage().equals(locale.getLanguage())) {
                    float layoutScore = 1.0f;
                    if (l.getCountry().equals(locale.getCountry())) {
                        layoutScore = 1.0f + 1.0f;
                    } else if (android.text.TextUtils.isEmpty(l.getCountry())) {
                        layoutScore = (float) (((double) 1.0f) + 0.5d);
                    }
                    if (l.getVariant().equals(locale.getVariant())) {
                        layoutScore += 1.0f;
                    } else if (android.text.TextUtils.isEmpty(l.getVariant())) {
                        layoutScore = (float) (((double) layoutScore) + 0.5d);
                    }
                    if (layoutScore > bestMatchingLayoutScore) {
                        bestMatchingLayoutScore = layoutScore;
                        bestMatchingLayout = layout.getDescriptor();
                    }
                }
            }
        }
        return bestMatchingLayout;
    }

    private void reloadKeyboardLayouts() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Reloading keyboard layouts.");
        }
        this.mNative.reloadKeyboardLayouts();
    }

    private void maybeUpdateNotification() {
        java.util.List<com.android.server.input.KeyboardLayoutManager.KeyboardConfiguration> configurations = new java.util.ArrayList<>();
        for (int i = 0; i < this.mConfiguredKeyboards.size(); i++) {
            int deviceId = this.mConfiguredKeyboards.keyAt(i);
            com.android.server.input.KeyboardLayoutManager.KeyboardConfiguration config = this.mConfiguredKeyboards.valueAt(i);
            if (!isVirtualDevice(deviceId)) {
                if (!config.hasConfiguredLayouts()) {
                    showMissingKeyboardLayoutNotification();
                    return;
                }
                configurations.add(config);
            }
        }
        int i2 = configurations.size();
        if (i2 == 0) {
            hideKeyboardLayoutNotification();
        } else {
            showConfiguredKeyboardLayoutNotification(configurations);
        }
    }

    private void showMissingKeyboardLayoutNotification() {
        android.content.res.Resources r = this.mContext.getResources();
        java.lang.String missingKeyboardLayoutNotificationContent = r.getString(android.R.string.satellite_messaging_location_disabled_notification_title);
        if (this.mConfiguredKeyboards.size() == 1) {
            android.view.InputDevice device = getInputDevice(this.mConfiguredKeyboards.keyAt(0));
            if (device == null) {
                return;
            }
            showKeyboardLayoutNotification(r.getString(android.R.string.satellite_messaging_not_in_allowed_region_notification_summary, device.getName()), missingKeyboardLayoutNotificationContent, device);
            return;
        }
        showKeyboardLayoutNotification(r.getString(android.R.string.satellite_messaging_not_provisioned_notification_summary), missingKeyboardLayoutNotificationContent, null);
    }

    private void showKeyboardLayoutNotification(java.lang.String intentTitle, java.lang.String intentContent, android.view.InputDevice targetDevice) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        if (notificationManager == null) {
            return;
        }
        android.content.Intent intent = new android.content.Intent("android.settings.HARD_KEYBOARD_SETTINGS");
        if (targetDevice != null) {
            intent.putExtra("input_device_identifier", (android.os.Parcelable) targetDevice.getIdentifier());
            intent.putExtra("com.android.settings.inputmethod.EXTRA_ENTRYPOINT", 0);
        }
        intent.setFlags(337641472);
        android.app.PendingIntent keyboardLayoutIntent = android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, 67108864, null, android.os.UserHandle.CURRENT);
        android.app.Notification notification = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.PHYSICAL_KEYBOARD).setContentTitle(intentTitle).setContentText(intentContent).setContentIntent(keyboardLayoutIntent).setSmallIcon(android.R.drawable.ic_pan_tool).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).setAutoCancel(true).build();
        notificationManager.notifyAsUser(null, 19, notification, android.os.UserHandle.ALL);
    }

    private void hideKeyboardLayoutNotification() {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        if (notificationManager == null) {
            return;
        }
        notificationManager.cancelAsUser(null, 19, android.os.UserHandle.ALL);
    }

    private void showConfiguredKeyboardLayoutNotification(java.util.List<com.android.server.input.KeyboardLayoutManager.KeyboardConfiguration> configurations) {
        android.content.res.Resources r = this.mContext.getResources();
        if (configurations.size() != 1) {
            showKeyboardLayoutNotification(r.getString(android.R.string.install_carrier_app_notification_button), r.getString(android.R.string.input_method_switcher_settings_button), null);
            return;
        }
        com.android.server.input.KeyboardLayoutManager.KeyboardConfiguration config = configurations.get(0);
        android.view.InputDevice inputDevice = getInputDevice(config.getDeviceId());
        if (inputDevice == null || !config.hasConfiguredLayouts()) {
            return;
        }
        showKeyboardLayoutNotification(r.getString(android.R.string.install_carrier_app_notification_text_app_name, inputDevice.getName()), createConfiguredNotificationText(this.mContext, config.getConfiguredLayouts()), inputDevice);
    }

    private java.lang.String createConfiguredNotificationText(android.content.Context context, java.util.Set<java.lang.String> selectedLayouts) {
        android.content.res.Resources r = context.getResources();
        final java.util.List<java.lang.String> layoutNames = new java.util.ArrayList<>();
        selectedLayouts.forEach(new java.util.function.Consumer() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$createConfiguredNotificationText$5(layoutNames, (java.lang.String) obj);
            }
        });
        java.util.Collections.sort(layoutNames);
        switch (layoutNames.size()) {
            case 1:
                return r.getString(android.R.string.install_carrier_app_notification_text, layoutNames.get(0));
            case 2:
                return r.getString(android.R.string.invalidPin, layoutNames.get(0), layoutNames.get(1));
            case 3:
                return r.getString(android.R.string.install_carrier_app_notification_title, layoutNames.get(0), layoutNames.get(1), layoutNames.get(2));
            default:
                return r.getString(android.R.string.input_method_nav_back_button_desc, layoutNames.get(0), layoutNames.get(1), layoutNames.get(2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createConfiguredNotificationText$5(java.util.List layoutNames, java.lang.String layoutDesc) {
        layoutNames.add(getKeyboardLayout(layoutDesc).getLabel());
    }

    private void logKeyboardConfigurationEvent(android.view.InputDevice inputDevice, java.util.List<com.android.server.input.KeyboardLayoutManager.ImeInfo> imeInfoList, java.util.List<android.hardware.input.KeyboardLayoutSelectionResult> resultList, boolean isFirstConfiguration) {
        if (imeInfoList.isEmpty() || resultList.isEmpty()) {
            return;
        }
        com.android.server.input.KeyboardMetricsCollector.KeyboardConfigurationEvent.Builder configurationEventBuilder = new com.android.server.input.KeyboardMetricsCollector.KeyboardConfigurationEvent.Builder(inputDevice).setIsFirstTimeConfiguration(isFirstConfiguration);
        for (int i = 0; i < imeInfoList.size(); i++) {
            android.hardware.input.KeyboardLayoutSelectionResult result = resultList.get(i);
            java.lang.String layoutName = null;
            int layoutSelectionCriteria = 4;
            if (result != null && result.getLayoutDescriptor() != null) {
                layoutSelectionCriteria = result.getSelectionCriteria();
                com.android.server.input.KeyboardLayoutManager.KeyboardLayoutDescriptor d = com.android.server.input.KeyboardLayoutManager.KeyboardLayoutDescriptor.parse(result.getLayoutDescriptor());
                if (d != null) {
                    layoutName = d.keyboardLayoutName;
                }
            }
            configurationEventBuilder.addLayoutSelection(imeInfoList.get(i).mImeSubtype, layoutName, layoutSelectionCriteria);
        }
        com.android.server.input.KeyboardMetricsCollector.logKeyboardConfiguredAtom(configurationEventBuilder.build());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleMessage(android.os.Message msg) throws java.lang.Throwable {
        switch (msg.what) {
            case 1:
                for (int deviceId : (int[]) msg.obj) {
                    onInputDeviceAdded(deviceId);
                }
                return true;
            case 2:
                reloadKeyboardLayouts();
                return true;
            case 3:
                updateKeyboardLayouts();
                return true;
            default:
                return false;
        }
    }

    private android.view.InputDevice getInputDevice(int deviceId) {
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class);
        if (inputManager != null) {
            return inputManager.getInputDevice(deviceId);
        }
        return null;
    }

    private android.view.InputDevice getInputDevice(android.hardware.input.InputDeviceIdentifier identifier) {
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class);
        if (inputManager != null) {
            return inputManager.getInputDeviceByDescriptor(identifier.getDescriptor());
        }
        return null;
    }

    public java.util.List<com.android.server.input.KeyboardLayoutManager.ImeInfo> getImeInfoListForLayoutMapping() {
        java.util.List<com.android.server.input.KeyboardLayoutManager.ImeInfo> imeInfoList = new java.util.ArrayList<>();
        android.os.UserManager userManager = (android.os.UserManager) java.util.Objects.requireNonNull((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class));
        android.view.inputmethod.InputMethodManager inputMethodManager = (android.view.inputmethod.InputMethodManager) java.util.Objects.requireNonNull((android.view.inputmethod.InputMethodManager) this.mContext.getSystemService(android.view.inputmethod.InputMethodManager.class));
        com.android.server.inputmethod.InputMethodManagerInternal inputMethodManagerInternal = com.android.server.inputmethod.InputMethodManagerInternal.get();
        for (android.os.UserHandle userHandle : userManager.getUserHandles(true)) {
            int userId = userHandle.getIdentifier();
            for (android.view.inputmethod.InputMethodInfo imeInfo : inputMethodManagerInternal.getEnabledInputMethodListAsUser(userId)) {
                for (android.view.inputmethod.InputMethodSubtype imeSubtype : inputMethodManager.getEnabledInputMethodSubtypeList(imeInfo, true)) {
                    if (imeSubtype.isSuitableForPhysicalKeyboardLayoutMapping()) {
                        imeInfoList.add(new com.android.server.input.KeyboardLayoutManager.ImeInfo(userId, imeInfo, imeSubtype));
                    }
                }
            }
        }
        return imeInfoList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isLayoutCompatibleWithLanguageTag(android.hardware.input.KeyboardLayout layout, java.lang.String languageTag) {
        android.os.LocaleList layoutLocales = layout.getLocales();
        if (layoutLocales.isEmpty() || android.text.TextUtils.isEmpty(languageTag)) {
            return true;
        }
        int[] scriptsFromLanguageTag = getScriptCodes(java.util.Locale.forLanguageTag(languageTag));
        if (scriptsFromLanguageTag.length == 0) {
            return true;
        }
        for (int i = 0; i < layoutLocales.size(); i++) {
            java.util.Locale locale = layoutLocales.get(i);
            int[] scripts = getScriptCodes(locale);
            if (haveCommonValue(scripts, scriptsFromLanguageTag)) {
                return true;
            }
        }
        return false;
    }

    public boolean isVirtualDevice(int deviceId) {
        com.android.server.companion.virtual.VirtualDeviceManagerInternal vdm = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        return vdm != null && vdm.isInputDeviceOwnedByVirtualDevice(deviceId);
    }

    private static int[] getScriptCodes(java.util.Locale locale) {
        int scriptCode;
        if (locale == null) {
            return new int[0];
        }
        if (!android.text.TextUtils.isEmpty(locale.getScript()) && (scriptCode = android.icu.lang.UScript.getCodeFromName(locale.getScript())) != -1) {
            return new int[]{scriptCode};
        }
        int[] scripts = android.icu.lang.UScript.getCode(locale);
        if (scripts != null) {
            return scripts;
        }
        return new int[0];
    }

    private static boolean haveCommonValue(int[] arr1, int[] arr2) {
        for (int a1 : arr1) {
            for (int a2 : arr2) {
                if (a1 == a2) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final class KeyboardLayoutDescriptor {
        public java.lang.String keyboardLayoutName;
        public java.lang.String packageName;
        public java.lang.String receiverName;

        private KeyboardLayoutDescriptor() {
        }

        public static java.lang.String format(java.lang.String packageName, java.lang.String receiverName, java.lang.String keyboardName) {
            return packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + receiverName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + keyboardName;
        }

        public static com.android.server.input.KeyboardLayoutManager.KeyboardLayoutDescriptor parse(java.lang.String descriptor) {
            int pos2;
            int pos = descriptor.indexOf(47);
            if (pos < 0 || pos + 1 == descriptor.length() || (pos2 = descriptor.indexOf(47, pos + 1)) < pos + 2 || pos2 + 1 == descriptor.length()) {
                return null;
            }
            com.android.server.input.KeyboardLayoutManager.KeyboardLayoutDescriptor result = new com.android.server.input.KeyboardLayoutManager.KeyboardLayoutDescriptor();
            result.packageName = descriptor.substring(0, pos);
            result.receiverName = descriptor.substring(pos + 1, pos2);
            result.keyboardLayoutName = descriptor.substring(pos2 + 1);
            return result;
        }
    }

    public static class ImeInfo {
        android.view.inputmethod.InputMethodSubtype mImeSubtype;
        com.android.internal.inputmethod.InputMethodSubtypeHandle mImeSubtypeHandle;
        int mUserId;

        ImeInfo(int userId, com.android.internal.inputmethod.InputMethodSubtypeHandle imeSubtypeHandle, android.view.inputmethod.InputMethodSubtype imeSubtype) {
            this.mUserId = userId;
            this.mImeSubtypeHandle = imeSubtypeHandle;
            this.mImeSubtype = imeSubtype;
        }

        ImeInfo(int userId, android.view.inputmethod.InputMethodInfo imeInfo, android.view.inputmethod.InputMethodSubtype imeSubtype) {
            this(userId, com.android.internal.inputmethod.InputMethodSubtypeHandle.of(imeInfo, imeSubtype), imeSubtype);
        }
    }

    private static class KeyboardConfiguration {
        private java.util.Set<java.lang.String> mConfiguredLayouts;
        private final int mDeviceId;

        private KeyboardConfiguration(int deviceId) {
            this.mDeviceId = deviceId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getDeviceId() {
            return this.mDeviceId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasConfiguredLayouts() {
            return (this.mConfiguredLayouts == null || this.mConfiguredLayouts.isEmpty()) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.util.Set<java.lang.String> getConfiguredLayouts() {
            return this.mConfiguredLayouts;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setConfiguredLayouts(java.util.Set<java.lang.String> configuredLayouts) {
            this.mConfiguredLayouts = configuredLayouts;
        }
    }

    private static class KeyboardIdentifier {
        private final android.hardware.input.InputDeviceIdentifier mIdentifier;
        private final java.lang.String mLanguageTag;
        private final java.lang.String mLayoutType;

        private KeyboardIdentifier(android.hardware.input.InputDeviceIdentifier inputDeviceIdentifier) {
            this(inputDeviceIdentifier, null, null);
        }

        private KeyboardIdentifier(android.view.InputDevice inputDevice) {
            this(inputDevice.getIdentifier(), inputDevice.getKeyboardLanguageTag(), inputDevice.getKeyboardLayoutType());
        }

        private KeyboardIdentifier(android.hardware.input.InputDeviceIdentifier identifier, java.lang.String languageTag, java.lang.String layoutType) {
            java.util.Objects.requireNonNull(identifier, "identifier must not be null");
            java.util.Objects.requireNonNull(identifier.getDescriptor(), "descriptor must not be null");
            this.mIdentifier = identifier;
            this.mLanguageTag = languageTag;
            this.mLayoutType = layoutType;
        }

        public int hashCode() {
            return java.util.Objects.hashCode(toString());
        }

        public java.lang.String toString() {
            if (this.mIdentifier.getVendorId() == 0 && this.mIdentifier.getProductId() == 0) {
                return this.mIdentifier.getDescriptor();
            }
            java.lang.StringBuilder key = new java.lang.StringBuilder();
            key.append("vendor:").append(this.mIdentifier.getVendorId()).append(",product:").append(this.mIdentifier.getProductId());
            if (!android.text.TextUtils.isEmpty(this.mLanguageTag)) {
                key.append(",languageTag:").append(this.mLanguageTag);
            }
            if (!android.text.TextUtils.isEmpty(this.mLayoutType)) {
                key.append(",layoutType:").append(this.mLayoutType);
            }
            return key.toString();
        }
    }

    private static class LayoutKey {
        private final com.android.server.input.KeyboardLayoutManager.ImeInfo mImeInfo;
        private final com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier mKeyboardIdentifier;

        private LayoutKey(com.android.server.input.KeyboardLayoutManager.KeyboardIdentifier keyboardIdentifier, com.android.server.input.KeyboardLayoutManager.ImeInfo imeInfo) {
            this.mKeyboardIdentifier = keyboardIdentifier;
            this.mImeInfo = imeInfo;
        }

        public int hashCode() {
            return java.util.Objects.hashCode(toString());
        }

        public java.lang.String toString() {
            if (this.mImeInfo == null) {
                return this.mKeyboardIdentifier.toString();
            }
            java.util.Objects.requireNonNull(this.mImeInfo.mImeSubtypeHandle, "subtypeHandle must not be null");
            return "layoutDescriptor:" + this.mKeyboardIdentifier + ",userId:" + this.mImeInfo.mUserId + ",subtypeHandle:" + this.mImeInfo.mImeSubtypeHandle.toStringHandle();
        }
    }
}
