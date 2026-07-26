package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class EmbeddedWindowController {
    private static final java.lang.String TAG = "WindowManager";
    private final com.android.server.wm.ActivityTaskManagerService mAtmService;
    private final java.lang.Object mGlobalLock;
    private final com.android.server.input.InputManagerService mInputManagerService;
    private android.util.ArrayMap<android.os.IBinder, com.android.server.wm.EmbeddedWindowController.EmbeddedWindow> mWindows = new android.util.ArrayMap<>();
    private android.util.ArrayMap<android.window.InputTransferToken, com.android.server.wm.EmbeddedWindowController.EmbeddedWindow> mWindowsByInputTransferToken = new android.util.ArrayMap<>();
    private android.util.ArrayMap<android.os.IBinder, com.android.server.wm.EmbeddedWindowController.EmbeddedWindow> mWindowsByWindowToken = new android.util.ArrayMap<>();

    EmbeddedWindowController(com.android.server.wm.ActivityTaskManagerService atmService, com.android.server.input.InputManagerService inputManagerService) {
        this.mAtmService = atmService;
        this.mGlobalLock = atmService.getGlobalLock();
        this.mInputManagerService = inputManagerService;
    }

    void add(final android.os.IBinder inputToken, com.android.server.wm.EmbeddedWindowController.EmbeddedWindow window) {
        try {
            this.mWindows.put(inputToken, window);
            final android.window.InputTransferToken inputTransferToken = window.getInputTransferToken();
            this.mWindowsByInputTransferToken.put(inputTransferToken, window);
            final android.os.IBinder windowToken = window.getWindowToken();
            this.mWindowsByWindowToken.put(windowToken, window);
            updateProcessController(window);
            window.mClient.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wm.EmbeddedWindowController$$ExternalSyntheticLambda0
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$add$0(inputToken, inputTransferToken, windowToken);
                }
            }, 0);
        } catch (android.os.RemoteException e) {
            this.mWindows.remove(inputToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$add$0(android.os.IBinder inputToken, android.window.InputTransferToken inputTransferToken, android.os.IBinder windowToken) {
        synchronized (this.mGlobalLock) {
            this.mWindows.remove(inputToken);
            this.mWindowsByInputTransferToken.remove(inputTransferToken);
            this.mWindowsByWindowToken.remove(windowToken);
        }
    }

    private void updateProcessController(com.android.server.wm.EmbeddedWindowController.EmbeddedWindow window) {
        if (window.mHostActivityRecord == null) {
            return;
        }
        com.android.server.wm.WindowProcessController processController = this.mAtmService.getProcessController(window.mOwnerPid, window.mOwnerUid);
        if (processController == null) {
            android.util.Slog.w(TAG, "Could not find the embedding process.");
        } else {
            processController.addHostActivity(window.mHostActivityRecord);
        }
    }

    void remove(android.os.IBinder client) {
        for (int i = this.mWindows.size() - 1; i >= 0; i--) {
            com.android.server.wm.EmbeddedWindowController.EmbeddedWindow ew = this.mWindows.valueAt(i);
            if (ew.mClient == client) {
                this.mWindows.removeAt(i).onRemoved();
                this.mWindowsByInputTransferToken.remove(ew.getInputTransferToken());
                this.mWindowsByWindowToken.remove(ew.getWindowToken());
                return;
            }
        }
    }

    void onWindowRemoved(com.android.server.wm.WindowState host) {
        for (int i = this.mWindows.size() - 1; i >= 0; i--) {
            com.android.server.wm.EmbeddedWindowController.EmbeddedWindow ew = this.mWindows.valueAt(i);
            if (ew.mHostWindowState == host) {
                this.mWindows.removeAt(i).onRemoved();
                this.mWindowsByInputTransferToken.remove(ew.getInputTransferToken());
                this.mWindowsByWindowToken.remove(ew.getWindowToken());
            }
        }
    }

    com.android.server.wm.EmbeddedWindowController.EmbeddedWindow get(android.os.IBinder inputToken) {
        return this.mWindows.get(inputToken);
    }

    com.android.server.wm.EmbeddedWindowController.EmbeddedWindow getByInputTransferToken(android.window.InputTransferToken inputTransferToken) {
        return this.mWindowsByInputTransferToken.get(inputTransferToken);
    }

    com.android.server.wm.EmbeddedWindowController.EmbeddedWindow getByWindowToken(android.os.IBinder windowToken) {
        return this.mWindowsByWindowToken.get(windowToken);
    }

    private boolean isValidTouchGestureParams(com.android.server.wm.WindowState hostWindowState, com.android.server.wm.EmbeddedWindowController.EmbeddedWindow embeddedWindow) {
        if (embeddedWindow == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_EMBEDDED_WINDOWS_enabled[3]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, -1797662102094201628L, 0, null, null);
            }
            return false;
        }
        com.android.server.wm.WindowState wsAssociatedWithEmbedded = embeddedWindow.getWindowState();
        if (wsAssociatedWithEmbedded == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_EMBEDDED_WINDOWS_enabled[3]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, 929964979835124721L, 0, null, null);
            }
            return false;
        }
        if (wsAssociatedWithEmbedded.mClient.asBinder() != hostWindowState.mClient.asBinder()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_EMBEDDED_WINDOWS_enabled[3]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, 676191989331669410L, 0, null, null);
            }
            return false;
        }
        if (embeddedWindow.getInputChannelToken() == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_EMBEDDED_WINDOWS_enabled[3]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, 553249487221306249L, 0, null, null);
            }
            return false;
        }
        if (hostWindowState.mInputChannelToken == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_EMBEDDED_WINDOWS_enabled[3]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_EMBEDDED_WINDOWS, -8678904073078032058L, 0, null, null);
            }
            return false;
        }
        return true;
    }

    boolean transferToHost(int callingUid, android.window.InputTransferToken embeddedWindowToken, com.android.server.wm.WindowState transferToHostWindowState) {
        com.android.server.wm.EmbeddedWindowController.EmbeddedWindow ew = getByInputTransferToken(embeddedWindowToken);
        if (!isValidTouchGestureParams(transferToHostWindowState, ew)) {
            return false;
        }
        if (callingUid != ew.mOwnerUid) {
            throw new java.lang.SecurityException("Transfer request must originate from owner of transferFromToken");
        }
        return this.mInputManagerService.transferTouchGesture(ew.getInputChannelToken(), transferToHostWindowState.mInputChannelToken);
    }

    boolean transferToEmbedded(int callingUid, com.android.server.wm.WindowState hostWindowState, android.window.InputTransferToken transferToToken) {
        com.android.server.wm.EmbeddedWindowController.EmbeddedWindow ew = getByInputTransferToken(transferToToken);
        if (!isValidTouchGestureParams(hostWindowState, ew)) {
            return false;
        }
        if (callingUid != hostWindowState.mOwnerUid) {
            throw new java.lang.SecurityException("Transfer request must originate from owner of transferFromToken");
        }
        return this.mInputManagerService.transferTouchGesture(hostWindowState.mInputChannelToken, ew.getInputChannelToken());
    }

    static class EmbeddedWindow implements com.android.server.wm.InputTarget {
        final android.os.IBinder mClient;
        final int mDisplayId;
        final com.android.server.wm.ActivityRecord mHostActivityRecord;
        final com.android.server.wm.WindowState mHostWindowState;
        android.view.InputChannel mInputChannel;
        private final android.window.InputTransferToken mInputTransferToken;
        private boolean mIsFocusable;
        final java.lang.String mName;
        final int mOwnerPid;
        final int mOwnerUid;
        public com.android.server.wm.Session mSession;
        final int mWindowType;
        final com.android.server.wm.WindowManagerService mWmService;

        EmbeddedWindow(com.android.server.wm.Session session, com.android.server.wm.WindowManagerService service, android.os.IBinder clientToken, com.android.server.wm.WindowState hostWindowState, int ownerUid, int ownerPid, int windowType, int displayId, android.window.InputTransferToken inputTransferToken, java.lang.String inputHandleName, boolean isFocusable) {
            this.mSession = session;
            this.mWmService = service;
            this.mClient = clientToken;
            this.mHostWindowState = hostWindowState;
            this.mHostActivityRecord = this.mHostWindowState != null ? this.mHostWindowState.mActivityRecord : null;
            this.mOwnerUid = ownerUid;
            this.mOwnerPid = ownerPid;
            this.mWindowType = windowType;
            this.mDisplayId = displayId;
            this.mInputTransferToken = inputTransferToken;
            java.lang.String hostWindowName = this.mHostWindowState != null ? "-" + this.mHostWindowState.getWindowTag().toString() : "";
            this.mIsFocusable = isFocusable;
            this.mName = "Embedded{" + inputHandleName + hostWindowName + "}";
        }

        public java.lang.String toString() {
            return this.mName;
        }

        android.view.InputApplicationHandle getApplicationHandle() {
            if (this.mHostWindowState == null || this.mHostWindowState.mInputWindowHandle.getInputApplicationHandle() == null) {
                return null;
            }
            return new android.view.InputApplicationHandle(this.mHostWindowState.mInputWindowHandle.getInputApplicationHandle());
        }

        void openInputChannel(android.view.InputChannel outInputChannel) {
            java.lang.String name = toString();
            this.mInputChannel = this.mWmService.mInputManager.createInputChannel(name);
            this.mInputChannel.copyTo(outInputChannel);
        }

        void onRemoved() {
            com.android.server.wm.WindowProcessController wpc;
            if (this.mInputChannel != null) {
                this.mWmService.mInputManager.removeInputChannel(this.mInputChannel.getToken());
                this.mInputChannel.dispose();
                this.mInputChannel = null;
            }
            if (this.mHostActivityRecord != null && (wpc = this.mWmService.mAtmService.getProcessController(this.mOwnerPid, this.mOwnerUid)) != null) {
                wpc.removeHostActivity(this.mHostActivityRecord);
            }
        }

        @Override // com.android.server.wm.InputTarget
        public com.android.server.wm.WindowState getWindowState() {
            return this.mHostWindowState;
        }

        @Override // com.android.server.wm.InputTarget
        public int getDisplayId() {
            return this.mDisplayId;
        }

        @Override // com.android.server.wm.InputTarget
        public com.android.server.wm.DisplayContent getDisplayContent() {
            return this.mWmService.mRoot.getDisplayContent(getDisplayId());
        }

        @Override // com.android.server.wm.InputTarget
        public android.os.IBinder getWindowToken() {
            return this.mClient;
        }

        @Override // com.android.server.wm.InputTarget
        public int getPid() {
            return this.mOwnerPid;
        }

        @Override // com.android.server.wm.InputTarget
        public int getUid() {
            return this.mOwnerUid;
        }

        android.window.InputTransferToken getInputTransferToken() {
            return this.mInputTransferToken;
        }

        android.os.IBinder getInputChannelToken() {
            if (this.mInputChannel != null) {
                return this.mInputChannel.getToken();
            }
            return null;
        }

        void setIsFocusable(boolean isFocusable) {
            this.mIsFocusable = isFocusable;
        }

        @Override // com.android.server.wm.InputTarget
        public boolean receiveFocusFromTapOutside() {
            return this.mIsFocusable;
        }

        private void handleTap(boolean grantFocus) throws java.lang.Throwable {
            if (this.mInputChannel != null) {
                if (this.mHostWindowState != null) {
                    this.mWmService.grantEmbeddedWindowFocus(null, this.mHostWindowState.mClient, this.mInputTransferToken, grantFocus);
                    if (grantFocus) {
                        this.mHostWindowState.handleTapOutsideFocusInsideSelf();
                        return;
                    }
                    return;
                }
                this.mWmService.grantEmbeddedWindowFocus(this.mSession, this.mInputTransferToken, grantFocus);
            }
        }

        @Override // com.android.server.wm.InputTarget
        public void handleTapOutsideFocusOutsideSelf() throws java.lang.Throwable {
            handleTap(false);
        }

        @Override // com.android.server.wm.InputTarget
        public void handleTapOutsideFocusInsideSelf() throws java.lang.Throwable {
            handleTap(true);
        }

        @Override // com.android.server.wm.InputTarget
        public boolean shouldControlIme() {
            return this.mHostWindowState != null;
        }

        @Override // com.android.server.wm.InputTarget
        public boolean canScreenshotIme() {
            return true;
        }

        @Override // com.android.server.wm.InputTarget
        public com.android.server.wm.InsetsControlTarget getImeControlTarget() {
            if (this.mHostWindowState != null) {
                return this.mHostWindowState.getImeControlTarget();
            }
            return this.mWmService.getDefaultDisplayContentLocked().mRemoteInsetsControlTarget;
        }

        @Override // com.android.server.wm.InputTarget
        public boolean isInputMethodClientFocus(int uid, int pid) {
            return uid == this.mOwnerUid && pid == this.mOwnerPid;
        }

        @Override // com.android.server.wm.InputTarget
        public com.android.server.wm.ActivityRecord getActivityRecord() {
            return this.mHostActivityRecord;
        }

        @Override // com.android.server.wm.InputTarget
        public void dumpProto(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
            long token = proto.start(fieldId);
            long token2 = proto.start(1146756268034L);
            proto.write(1120986464257L, java.lang.System.identityHashCode(this));
            proto.write(1138166333443L, "EmbeddedWindow");
            proto.end(token2);
            proto.end(token);
        }
    }
}
