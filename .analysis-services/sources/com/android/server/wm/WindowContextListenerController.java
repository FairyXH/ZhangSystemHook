package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowContextListenerController {
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.inputmethod.update.appbounds.switch", true);
    private static final java.lang.String TAG = "WindowContextListenerImpl";
    final android.util.ArrayMap<android.os.IBinder, com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl> mListeners = new android.util.ArrayMap<>();

    WindowContextListenerController() {
    }

    void registerWindowContainerListener(com.android.server.wm.WindowProcessController wpc, android.os.IBinder clientToken, com.android.server.wm.WindowContainer<?> container, int type, android.os.Bundle options) {
        registerWindowContainerListener(wpc, clientToken, container, type, options, true);
    }

    void registerWindowContainerListener(com.android.server.wm.WindowProcessController wpc, android.os.IBinder clientToken, com.android.server.wm.WindowContainer<?> container, int type, android.os.Bundle options, boolean shouldDispatchConfigWhenRegistering) {
        com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl listener = this.mListeners.get(clientToken);
        if (listener == null) {
            com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl listener2 = new com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl(wpc, clientToken, container, type, options);
            listener2.register(shouldDispatchConfigWhenRegistering);
        } else {
            updateContainerForWindowContextListener(clientToken, container);
        }
    }

    void updateContainerForWindowContextListener(android.os.IBinder clientToken, com.android.server.wm.WindowContainer<?> container) {
        com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl listener = this.mListeners.get(clientToken);
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("Can't find listener for " + clientToken);
        }
        listener.updateContainer(container);
    }

    void unregisterWindowContainerListener(android.os.IBinder clientToken) {
        com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl listener = this.mListeners.get(clientToken);
        if (listener == null) {
            return;
        }
        listener.unregister();
        if (listener.mDeathRecipient != null) {
            listener.mDeathRecipient.unlinkToDeath();
        }
    }

    void dispatchPendingConfigurationIfNeeded(int displayId) {
        for (int i = this.mListeners.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl listener = this.mListeners.valueAt(i);
            if (listener.getWindowContainer().getDisplayContent().getDisplayId() == displayId && listener.mHasPendingConfiguration) {
                listener.dispatchWindowContextInfoChange();
            }
        }
    }

    boolean assertCallerCanModifyListener(android.os.IBinder clientToken, boolean callerCanManageAppTokens, int callingUid) {
        com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl listener = this.mListeners.get(clientToken);
        if (listener == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[2]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 2163930285157267092L, 0, null, null);
                return false;
            }
            return false;
        }
        if (callerCanManageAppTokens || callingUid == listener.getUid()) {
            return true;
        }
        throw new java.lang.UnsupportedOperationException("Uid mismatch. Caller uid is " + callingUid + ", while the listener's owner is from " + listener.getUid());
    }

    boolean hasListener(android.os.IBinder clientToken) {
        return this.mListeners.containsKey(clientToken);
    }

    int getWindowType(android.os.IBinder clientToken) {
        com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl listener = this.mListeners.get(clientToken);
        if (listener != null) {
            return listener.mType;
        }
        return -1;
    }

    android.os.Bundle getOptions(android.os.IBinder clientToken) {
        com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl listener = this.mListeners.get(clientToken);
        if (listener != null) {
            return listener.mOptions;
        }
        return null;
    }

    com.android.server.wm.WindowContainer<?> getContainer(android.os.IBinder clientToken) {
        com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl listener = this.mListeners.get(clientToken);
        if (listener != null) {
            return listener.mContainer;
        }
        return null;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder("WindowContextListenerController{");
        builder.append("mListeners=[");
        int size = this.mListeners.values().size();
        for (int i = 0; i < size; i++) {
            builder.append(this.mListeners.valueAt(i));
            if (i != size - 1) {
                builder.append(", ");
            }
        }
        builder.append("]}");
        return builder.toString();
    }

    class WindowContextListenerImpl implements com.android.server.wm.WindowContainerListener {
        private final android.os.IBinder mClientToken;
        private com.android.server.wm.WindowContainer<?> mContainer;
        private com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl.DeathRecipient mDeathRecipient;
        private boolean mHasPendingConfiguration;
        private android.content.res.Configuration mLastReportedConfig;
        private int mLastReportedDisplay;
        private final android.os.Bundle mOptions;
        private final int mType;
        private final com.android.server.wm.WindowProcessController mWpc;

        private WindowContextListenerImpl(com.android.server.wm.WindowProcessController wpc, android.os.IBinder clientToken, com.android.server.wm.WindowContainer<?> container, int type, android.os.Bundle options) {
            this.mLastReportedDisplay = -1;
            this.mWpc = (com.android.server.wm.WindowProcessController) java.util.Objects.requireNonNull(wpc);
            this.mClientToken = clientToken;
            this.mContainer = (com.android.server.wm.WindowContainer) java.util.Objects.requireNonNull(container);
            this.mType = type;
            this.mOptions = options;
            com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl.DeathRecipient deathRecipient = new com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl.DeathRecipient();
            try {
                deathRecipient.linkToDeath();
                this.mDeathRecipient = deathRecipient;
            } catch (android.os.RemoteException e) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[4]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(clientToken);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mContainer);
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 6139364662459841509L, 0, "Could not register window container listener token=%s, container=%s", protoLogParam0, protoLogParam1);
                }
            }
        }

        com.android.server.wm.WindowContainer<?> getWindowContainer() {
            return this.mContainer;
        }

        int getUid() {
            return this.mWpc.mUid;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateContainer(com.android.server.wm.WindowContainer<?> newContainer) {
            java.util.Objects.requireNonNull(newContainer);
            if (this.mContainer.equals(newContainer)) {
                return;
            }
            this.mContainer.unregisterWindowContainerListener(this);
            this.mContainer = newContainer;
            clear();
            register();
        }

        private void register() {
            register(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register(boolean shouldDispatchConfig) {
            android.os.IBinder token = this.mClientToken;
            if (this.mDeathRecipient == null) {
                throw new java.lang.IllegalStateException("Invalid client token: " + token);
            }
            com.android.server.wm.WindowContextListenerController.this.mListeners.putIfAbsent(token, this);
            this.mContainer.registerWindowContainerListener(this, shouldDispatchConfig);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregister() {
            this.mContainer.unregisterWindowContainerListener(this);
            com.android.server.wm.WindowContextListenerController.this.mListeners.remove(this.mClientToken);
        }

        private void clear() {
            this.mLastReportedConfig = null;
            this.mLastReportedDisplay = -1;
        }

        @Override // com.android.server.wm.ConfigurationContainerListener
        public void onMergedOverrideConfigurationChanged(android.content.res.Configuration mergedOverrideConfig) {
            dispatchWindowContextInfoChange();
        }

        @Override // com.android.server.wm.WindowContainerListener
        public void onDisplayChanged(com.android.server.wm.DisplayContent dc) {
            dispatchWindowContextInfoChange();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dispatchWindowContextInfoChange() {
            if (this.mDeathRecipient == null) {
                throw new java.lang.IllegalStateException("Invalid client token: " + this.mClientToken);
            }
            com.android.server.wm.DisplayContent dc = this.mContainer.getDisplayContent();
            if (!dc.isReady()) {
                return;
            }
            boolean rotated = true;
            if (!android.window.WindowProviderService.isWindowProviderService(this.mOptions) && android.view.Display.isSuspendedState(dc.getDisplayInfo().state)) {
                this.mHasPendingConfiguration = true;
                return;
            }
            android.content.res.Configuration config = this.mContainer.getConfiguration();
            int displayId = dc.getDisplayId();
            if (this.mLastReportedConfig == null) {
                this.mLastReportedConfig = new android.content.res.Configuration();
            }
            if (config.equals(this.mLastReportedConfig) && displayId == this.mLastReportedDisplay) {
                return;
            }
            this.mLastReportedConfig.setTo(config);
            this.mLastReportedDisplay = displayId;
            if (com.android.server.wm.WindowContextListenerController.DEBUG && (this.mContainer instanceof com.android.server.wm.WindowToken) && ((com.android.server.wm.WindowToken) this.mContainer).windowType == 2011 && dc.isDefaultDisplay && config.windowConfiguration.getWindowingMode() == 1 && !com.oplus.flexiblewindow.FlexibleWindowManager.isFlexibleActivitySuitable(config)) {
                int rotation = config.windowConfiguration.getRotation();
                if (rotation == -1) {
                    rotation = dc.getRotation();
                }
                if (rotation != 1 && rotation != 3) {
                    rotated = false;
                }
                int dw = rotated ? dc.mBaseDisplayHeight : dc.mBaseDisplayWidth;
                int dh = rotated ? dc.mBaseDisplayWidth : dc.mBaseDisplayHeight;
                com.android.server.wm.DisplayPolicy.DecorInsets.Info info = dc.getDisplayPolicy().getDecorInsetsInfo(rotation, dw, dh);
                config.windowConfiguration.setAppBounds(info.mOverrideNonDecorFrame);
                android.util.Log.i(com.android.server.wm.WindowContextListenerController.TAG, "dispatchWindowContextInfoChange mContainer = " + this.mContainer + " ,rotated = " + rotated + " ,dw = " + dw + " ,dh = " + dh + " ,appBounds = " + info.mOverrideNonDecorFrame + " ,tempConfig = " + config);
            }
            this.mWpc.scheduleClientTransactionItem(android.app.servertransaction.WindowContextInfoChangeItem.obtain(this.mClientToken, config, displayId));
            this.mHasPendingConfiguration = false;
        }

        @Override // com.android.server.wm.WindowContainerListener
        public void onRemoved() {
            com.android.server.wm.DisplayContent dc;
            if (this.mDeathRecipient == null) {
                throw new java.lang.IllegalStateException("Invalid client token: " + this.mClientToken);
            }
            com.android.server.wm.WindowToken windowToken = this.mContainer.asWindowToken();
            if (windowToken != null && windowToken.isFromClient() && (dc = windowToken.mWmService.mRoot.getDisplayContent(this.mLastReportedDisplay)) != null) {
                com.android.server.wm.DisplayArea<?> da = dc.findAreaForToken(windowToken);
                updateContainer(da);
            } else {
                this.mDeathRecipient.unlinkToDeath();
                this.mWpc.scheduleClientTransactionItem(android.app.servertransaction.WindowContextWindowRemovalItem.obtain(this.mClientToken));
                unregister();
            }
        }

        public java.lang.String toString() {
            return "WindowContextListenerImpl{clientToken=" + this.mClientToken + ", container=" + this.mContainer + "}";
        }

        private class DeathRecipient implements android.os.IBinder.DeathRecipient {
            private DeathRecipient() {
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl.this.mContainer.mWmService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl.this.mDeathRecipient = null;
                        com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl.this.unregister();
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }

            void linkToDeath() throws android.os.RemoteException {
                com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl.this.mClientToken.linkToDeath(this, 0);
            }

            void unlinkToDeath() {
                com.android.server.wm.WindowContextListenerController.WindowContextListenerImpl.this.mClientToken.unlinkToDeath(this, 0);
            }
        }
    }
}
