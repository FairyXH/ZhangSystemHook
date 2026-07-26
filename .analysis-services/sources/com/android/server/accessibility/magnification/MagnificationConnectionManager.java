package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class MagnificationConnectionManager implements com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate, com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks {
    private static final int CONNECTED = 1;
    private static final int CONNECTING = 0;
    private static final boolean DBG = false;
    private static final int DISCONNECTED = 3;
    private static final int DISCONNECTING = 2;
    private static final java.lang.String TAG = "MagnificationConnectionManager";
    private static final int WAIT_CONNECTION_TIMEOUT_MILLIS = android.os.Build.HW_TIMEOUT_MULTIPLIER * 200;
    public static final int WINDOW_POSITION_AT_CENTER = 0;
    public static final int WINDOW_POSITION_AT_TOP_LEFT = 1;
    private final com.android.server.accessibility.magnification.MagnificationConnectionManager.Callback mCallback;
    private com.android.server.accessibility.magnification.MagnificationConnectionManager.ConnectionCallback mConnectionCallback;
    com.android.server.accessibility.magnification.MagnificationConnectionWrapper mConnectionWrapper;
    private final android.content.Context mContext;
    private final java.lang.Object mLock;
    private final com.android.server.accessibility.magnification.MagnificationScaleProvider mScaleProvider;
    private final com.android.server.accessibility.AccessibilityTraceManager mTrace;
    private int mConnectionState = 3;
    private android.util.SparseArray<com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier> mWindowMagnifiers = new android.util.SparseArray<>();
    private boolean mMagnificationFollowTypingEnabled = true;
    private final android.util.SparseBooleanArray mIsImeVisibleArray = new android.util.SparseBooleanArray();
    private final android.util.SparseArray<java.lang.Float> mLastActivatedScale = new android.util.SparseArray<>();
    private boolean mReceiverRegistered = false;
    protected final android.content.BroadcastReceiver mScreenStateReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.accessibility.magnification.MagnificationConnectionManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            int displayId = context.getDisplayId();
            com.android.server.accessibility.magnification.MagnificationConnectionManager.this.removeMagnificationButton(displayId);
            com.android.server.accessibility.magnification.MagnificationConnectionManager.this.disableWindowMagnification(displayId, false, null);
        }
    };

    public interface Callback {
        void onAccessibilityActionPerformed(int i);

        void onChangeMagnificationMode(int i, int i2);

        void onPerformScaleAction(int i, float f, boolean z);

        void onSourceBoundsChanged(int i, android.graphics.Rect rect);

        void onWindowMagnificationActivationState(int i, boolean z);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface ConnectionState {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface WindowPosition {
    }

    private static java.lang.String connectionStateToString(int state) {
        switch (state) {
            case 0:
                return "CONNECTING";
            case 1:
                return "CONNECTED";
            case 2:
                return "DISCONNECTING";
            case 3:
                return "DISCONNECTED";
            default:
                return "UNKNOWN:" + state;
        }
    }

    public MagnificationConnectionManager(android.content.Context context, java.lang.Object lock, com.android.server.accessibility.magnification.MagnificationConnectionManager.Callback callback, com.android.server.accessibility.AccessibilityTraceManager trace, com.android.server.accessibility.magnification.MagnificationScaleProvider scaleProvider) {
        this.mContext = context;
        this.mLock = lock;
        this.mCallback = callback;
        this.mTrace = trace;
        this.mScaleProvider = scaleProvider;
    }

    public void setConnection(android.view.accessibility.IMagnificationConnection connection) {
        java.lang.Object obj;
        synchronized (this.mLock) {
            if (this.mConnectionWrapper != null) {
                this.mConnectionWrapper.setConnectionCallback(null);
                if (this.mConnectionCallback != null) {
                    this.mConnectionCallback.mExpiredDeathRecipient = true;
                }
                this.mConnectionWrapper.unlinkToDeath(this.mConnectionCallback);
                this.mConnectionWrapper = null;
                if (this.mConnectionState != 0) {
                    setConnectionState(3);
                }
            }
            if (connection != null) {
                this.mConnectionWrapper = new com.android.server.accessibility.magnification.MagnificationConnectionWrapper(connection, this.mTrace);
            }
            try {
                if (this.mConnectionWrapper != null) {
                    try {
                        this.mConnectionCallback = new com.android.server.accessibility.magnification.MagnificationConnectionManager.ConnectionCallback();
                        this.mConnectionWrapper.linkToDeath(this.mConnectionCallback);
                        this.mConnectionWrapper.setConnectionCallback(this.mConnectionCallback);
                        setConnectionState(1);
                        obj = this.mLock;
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(TAG, "setConnection failed", e);
                        this.mConnectionWrapper = null;
                        setConnectionState(3);
                        obj = this.mLock;
                    }
                    obj.notify();
                }
            } catch (java.lang.Throwable th) {
                this.mLock.notify();
                throw th;
            }
        }
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnectionWrapper != null;
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0070 A[Catch: all -> 0x0034, TryCatch #0 {all -> 0x0034, blocks: (B:9:0x002b, B:11:0x002f, B:22:0x0043, B:23:0x006c, B:18:0x003b, B:20:0x003f, B:26:0x0070, B:28:0x007b, B:33:0x0095, B:30:0x0085, B:32:0x008c), top: B:45:0x002b }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0085 A[Catch: all -> 0x0034, TryCatch #0 {all -> 0x0034, blocks: (B:9:0x002b, B:11:0x002f, B:22:0x0043, B:23:0x006c, B:18:0x003b, B:20:0x003f, B:26:0x0070, B:28:0x007b, B:33:0x0095, B:30:0x0085, B:32:0x008c), top: B:45:0x002b }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean requestConnection(boolean r9) {
        /*
            r8 = this;
            com.android.server.accessibility.AccessibilityTraceManager r0 = r8.mTrace
            r1 = 128(0x80, double:6.3E-322)
            boolean r0 = r0.isA11yTracingEnabledForTypes(r1)
            if (r0 == 0) goto L24
            com.android.server.accessibility.AccessibilityTraceManager r0 = r8.mTrace
            java.lang.String r3 = "MagnificationConnectionManager.requestMagnificationConnection"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "connect="
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r9)
            java.lang.String r4 = r4.toString()
            r0.logTrace(r3, r1, r4)
        L24:
            java.lang.Object r0 = r8.mLock
            monitor-enter(r0)
            r1 = 1
            r2 = 0
            if (r9 == 0) goto L37
            int r3 = r8.mConnectionState     // Catch: java.lang.Throwable -> L34
            if (r3 == r1) goto L43
            int r3 = r8.mConnectionState     // Catch: java.lang.Throwable -> L34
            if (r3 == 0) goto L43
            goto L37
        L34:
            r1 = move-exception
            goto La8
        L37:
            r3 = 2
            r4 = 3
            if (r9 != 0) goto L6e
            int r5 = r8.mConnectionState     // Catch: java.lang.Throwable -> L34
            if (r5 == r4) goto L43
            int r5 = r8.mConnectionState     // Catch: java.lang.Throwable -> L34
            if (r5 != r3) goto L6e
        L43:
            java.lang.String r1 = "MagnificationConnectionManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L34
            r3.<init>()     // Catch: java.lang.Throwable -> L34
            java.lang.String r4 = "requestConnection duplicated request: connect="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L34
            java.lang.StringBuilder r3 = r3.append(r9)     // Catch: java.lang.Throwable -> L34
            java.lang.String r4 = ", mConnectionState="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L34
            int r4 = r8.mConnectionState     // Catch: java.lang.Throwable -> L34
            java.lang.String r4 = connectionStateToString(r4)     // Catch: java.lang.Throwable -> L34
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L34
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L34
            android.util.Slog.w(r1, r3)     // Catch: java.lang.Throwable -> L34
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L34
            return r2
        L6e:
            if (r9 == 0) goto L85
            android.content.IntentFilter r5 = new android.content.IntentFilter     // Catch: java.lang.Throwable -> L34
            java.lang.String r6 = "android.intent.action.SCREEN_OFF"
            r5.<init>(r6)     // Catch: java.lang.Throwable -> L34
            boolean r6 = r8.mReceiverRegistered     // Catch: java.lang.Throwable -> L34
            if (r6 != 0) goto L84
            android.content.Context r6 = r8.mContext     // Catch: java.lang.Throwable -> L34
            android.content.BroadcastReceiver r7 = r8.mScreenStateReceiver     // Catch: java.lang.Throwable -> L34
            r6.registerReceiver(r7, r5)     // Catch: java.lang.Throwable -> L34
            r8.mReceiverRegistered = r1     // Catch: java.lang.Throwable -> L34
        L84:
            goto L95
        L85:
            r8.disableAllWindowMagnifiers()     // Catch: java.lang.Throwable -> L34
            boolean r5 = r8.mReceiverRegistered     // Catch: java.lang.Throwable -> L34
            if (r5 == 0) goto L95
            android.content.Context r5 = r8.mContext     // Catch: java.lang.Throwable -> L34
            android.content.BroadcastReceiver r6 = r8.mScreenStateReceiver     // Catch: java.lang.Throwable -> L34
            r5.unregisterReceiver(r6)     // Catch: java.lang.Throwable -> L34
            r8.mReceiverRegistered = r2     // Catch: java.lang.Throwable -> L34
        L95:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L34
            boolean r0 = r8.requestConnectionInternal(r9)
            if (r0 == 0) goto La4
            if (r9 == 0) goto L9f
            goto La0
        L9f:
            r2 = r3
        La0:
            r8.setConnectionState(r2)
            return r1
        La4:
            r8.setConnectionState(r4)
            return r2
        La8:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L34
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.magnification.MagnificationConnectionManager.requestConnection(boolean):boolean");
    }

    private boolean requestConnectionInternal(boolean connect) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.statusbar.StatusBarManagerInternal service = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
            if (service != null) {
                return service.requestMagnificationConnection(connect);
            }
            android.os.Binder.restoreCallingIdentity(identity);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public java.lang.String getConnectionState() {
        return connectionStateToString(this.mConnectionState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setConnectionState(int state) {
        this.mConnectionState = state;
    }

    void disableAllWindowMagnifiers() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mWindowMagnifiers.size(); i++) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.valueAt(i);
                magnifier.disableWindowMagnificationInternal(null);
            }
            this.mWindowMagnifiers.clear();
        }
    }

    public void resetAllIfNeeded(int connectionId) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mWindowMagnifiers.size(); i++) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.valueAt(i);
                if (magnifier != null && magnifier.mEnabled && connectionId == magnifier.getIdOfLastServiceToControl()) {
                    magnifier.disableWindowMagnificationInternal(null);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetWindowMagnifiers() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mWindowMagnifiers.size(); i++) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.valueAt(i);
                magnifier.reset();
            }
        }
    }

    @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks
    public void onRectangleOnScreenRequested(int displayId, int left, int top, int right, int bottom) {
        if (!this.mMagnificationFollowTypingEnabled) {
            return;
        }
        float toCenterX = (left + right) / 2.0f;
        float toCenterY = (top + bottom) / 2.0f;
        synchronized (this.mLock) {
            if (this.mIsImeVisibleArray.get(displayId, false) && !isPositionInSourceBounds(displayId, toCenterX, toCenterY) && isTrackingTypingFocusEnabled(displayId)) {
                moveWindowMagnifierToPositionInternal(displayId, toCenterX, toCenterY, android.view.accessibility.MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK);
            }
        }
    }

    void setMagnificationFollowTypingEnabled(boolean enabled) {
        this.mMagnificationFollowTypingEnabled = enabled;
    }

    boolean isMagnificationFollowTypingEnabled() {
        return this.mMagnificationFollowTypingEnabled;
    }

    public int getIdOfLastServiceToMagnify(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier != null) {
                return magnifier.mIdOfLastServiceToControl;
            }
            return -1;
        }
    }

    void setTrackingTypingFocusEnabled(int displayId, boolean trackingTypingFocusEnabled) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null) {
                return;
            }
            magnifier.setTrackingTypingFocusEnabled(trackingTypingFocusEnabled);
        }
    }

    private void enableAllTrackingTypingFocus() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mWindowMagnifiers.size(); i++) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.valueAt(i);
                magnifier.setTrackingTypingFocusEnabled(true);
            }
        }
    }

    private void pauseTrackingTypingFocusRecord(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null) {
                return;
            }
            magnifier.pauseTrackingTypingFocusRecord();
        }
    }

    void onImeWindowVisibilityChanged(int displayId, boolean shown) {
        synchronized (this.mLock) {
            this.mIsImeVisibleArray.put(displayId, shown);
        }
        if (shown) {
            enableAllTrackingTypingFocus();
        } else {
            pauseTrackingTypingFocusRecord(displayId);
        }
    }

    boolean isImeVisible(int displayId) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsImeVisibleArray.get(displayId);
        }
        return z;
    }

    void logTrackingTypingFocus(long duration) {
        com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logMagnificationFollowTypingFocusSession(duration);
    }

    @Override // com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate
    public boolean processScroll(int displayId, float distanceX, float distanceY) {
        moveWindowMagnification(displayId, -distanceX, -distanceY);
        setTrackingTypingFocusEnabled(displayId, false);
        return true;
    }

    @Override // com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate
    public void setScale(int displayId, float scale) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null) {
                return;
            }
            magnifier.setScale(scale);
            this.mLastActivatedScale.put(displayId, java.lang.Float.valueOf(scale));
        }
    }

    public boolean enableWindowMagnification(int displayId, float scale, float centerX, float centerY) {
        return enableWindowMagnification(displayId, scale, centerX, centerY, android.view.accessibility.MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK, 0);
    }

    public boolean enableWindowMagnification(int displayId, float scale, float centerX, float centerY, android.view.accessibility.MagnificationAnimationCallback animationCallback, int id) {
        return enableWindowMagnification(displayId, scale, centerX, centerY, animationCallback, 0, id);
    }

    public boolean enableWindowMagnification(int displayId, float scale, float centerX, float centerY, int windowPosition) {
        return enableWindowMagnification(displayId, scale, centerX, centerY, android.view.accessibility.MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK, windowPosition, 0);
    }

    public boolean enableWindowMagnification(int displayId, float scale, float centerX, float centerY, android.view.accessibility.MagnificationAnimationCallback animationCallback, int windowPosition, int id) {
        boolean previousEnabled;
        boolean enabled;
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null) {
                magnifier = createWindowMagnifier(displayId);
            }
            previousEnabled = magnifier.mEnabled;
            enabled = magnifier.enableWindowMagnificationInternal(scale, centerX, centerY, animationCallback, windowPosition, id);
            if (enabled) {
                this.mLastActivatedScale.put(displayId, java.lang.Float.valueOf(getScale(displayId)));
            }
        }
        if (enabled) {
            setTrackingTypingFocusEnabled(displayId, true);
            if (!previousEnabled) {
                this.mCallback.onWindowMagnificationActivationState(displayId, true);
            }
        }
        return enabled;
    }

    public boolean disableWindowMagnification(int displayId, boolean clear) {
        return disableWindowMagnification(displayId, clear, android.view.accessibility.MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK);
    }

    public boolean disableWindowMagnification(int displayId, boolean clear, android.view.accessibility.MagnificationAnimationCallback animationCallback) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null) {
                return false;
            }
            boolean disabled = magnifier.disableWindowMagnificationInternal(animationCallback);
            if (clear) {
                this.mWindowMagnifiers.delete(displayId);
            }
            if (disabled) {
                this.mCallback.onWindowMagnificationActivationState(displayId, false);
            }
            return disabled;
        }
    }

    public boolean onFullscreenMagnificationActivationChanged(int displayId, boolean activated) {
        synchronized (this.mLock) {
            if (!waitConnectionWithTimeoutIfNeeded()) {
                android.util.Slog.w(TAG, "onFullscreenMagnificationActivationChanged mConnectionWrapper is null. mConnectionState=" + connectionStateToString(this.mConnectionState));
                return false;
            }
            return this.mConnectionWrapper.onFullscreenMagnificationActivationChanged(displayId, activated);
        }
    }

    int pointersInWindow(int displayId, android.view.MotionEvent motionEvent) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null) {
                return 0;
            }
            return magnifier.pointersInWindow(motionEvent);
        }
    }

    boolean isPositionInSourceBounds(int displayId, float x, float y) {
        com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
        if (magnifier == null) {
            return false;
        }
        return magnifier.isPositionInSourceBounds(x, y);
    }

    public boolean isWindowMagnifierEnabled(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null) {
                return false;
            }
            return magnifier.isEnabled();
        }
    }

    float getPersistedScale(int displayId) {
        return android.util.MathUtils.constrain(this.mScaleProvider.getScale(displayId), 1.3f, com.android.server.accessibility.magnification.MagnificationScaleProvider.MAX_SCALE);
    }

    void persistScale(int displayId) {
        float scale = getScale(displayId);
        if (scale < 1.3f) {
            return;
        }
        this.mScaleProvider.putScale(scale, displayId);
    }

    @Override // com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate
    public float getScale(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier != null && magnifier.mEnabled) {
                return magnifier.getScale();
            }
            return 1.0f;
        }
    }

    protected float getLastActivatedScale(int displayId) {
        synchronized (this.mLock) {
            if (!this.mLastActivatedScale.contains(displayId)) {
                return -1.0f;
            }
            return this.mLastActivatedScale.get(displayId).floatValue();
        }
    }

    void moveWindowMagnification(int displayId, float offsetX, float offsetY) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null) {
                return;
            }
            magnifier.move(offsetX, offsetY);
        }
    }

    public boolean showMagnificationButton(int displayId, int magnificationMode) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnectionWrapper != null && this.mConnectionWrapper.showMagnificationButton(displayId, magnificationMode);
        }
        return z;
    }

    public boolean removeMagnificationButton(int displayId) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnectionWrapper != null && this.mConnectionWrapper.removeMagnificationButton(displayId);
        }
        return z;
    }

    public boolean removeMagnificationSettingsPanel(int displayId) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnectionWrapper != null && this.mConnectionWrapper.removeMagnificationSettingsPanel(displayId);
        }
        return z;
    }

    public boolean onUserMagnificationScaleChanged(int userId, int displayId, float scale) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnectionWrapper != null && this.mConnectionWrapper.onUserMagnificationScaleChanged(userId, displayId, scale);
        }
        return z;
    }

    public float getCenterX(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier != null && magnifier.mEnabled) {
                return magnifier.getCenterX();
            }
            return Float.NaN;
        }
    }

    public float getCenterY(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier != null && magnifier.mEnabled) {
                return magnifier.getCenterY();
            }
            return Float.NaN;
        }
    }

    boolean isTrackingTypingFocusEnabled(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null) {
                return false;
            }
            return magnifier.isTrackingTypingFocusEnabled();
        }
    }

    public void getMagnificationSourceBounds(int displayId, android.graphics.Region outRegion) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = this.mWindowMagnifiers.get(displayId);
            if (magnifier == null || !magnifier.mEnabled) {
                outRegion.setEmpty();
            } else {
                outRegion.set(magnifier.mSourceBounds);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier createWindowMagnifier(int displayId) {
        com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = new com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier(displayId, this);
        this.mWindowMagnifiers.put(displayId, magnifier);
        return magnifier;
    }

    public void onDisplayRemoved(int displayId) {
        disableWindowMagnification(displayId, true);
    }

    private class ConnectionCallback extends android.view.accessibility.IMagnificationConnectionCallback.Stub implements android.os.IBinder.DeathRecipient {
        private boolean mExpiredDeathRecipient;

        private ConnectionCallback() {
            this.mExpiredDeathRecipient = false;
        }

        public void onWindowMagnifierBoundsChanged(int displayId, android.graphics.Rect bounds) {
            if (com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.logTrace("MagnificationConnectionManagerConnectionCallback.onWindowMagnifierBoundsChanged", 256L, "displayId=" + displayId + ";bounds=" + bounds);
            }
            synchronized (com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mLock) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = (com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier) com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mWindowMagnifiers.get(displayId);
                if (magnifier == null) {
                    magnifier = com.android.server.accessibility.magnification.MagnificationConnectionManager.this.createWindowMagnifier(displayId);
                }
                magnifier.setMagnifierLocation(bounds);
            }
        }

        public void onChangeMagnificationMode(int displayId, int magnificationMode) throws android.os.RemoteException {
            if (com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.logTrace("MagnificationConnectionManagerConnectionCallback.onChangeMagnificationMode", 256L, "displayId=" + displayId + ";mode=" + magnificationMode);
            }
            com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mCallback.onChangeMagnificationMode(displayId, magnificationMode);
        }

        public void onSourceBoundsChanged(int displayId, android.graphics.Rect sourceBounds) {
            if (com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.logTrace("MagnificationConnectionManagerConnectionCallback.onSourceBoundsChanged", 256L, "displayId=" + displayId + ";source=" + sourceBounds);
            }
            synchronized (com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mLock) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier magnifier = (com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier) com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mWindowMagnifiers.get(displayId);
                if (magnifier == null) {
                    magnifier = com.android.server.accessibility.magnification.MagnificationConnectionManager.this.createWindowMagnifier(displayId);
                }
                magnifier.onSourceBoundsChanged(sourceBounds);
            }
            com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mCallback.onSourceBoundsChanged(displayId, sourceBounds);
        }

        public void onPerformScaleAction(int displayId, float scale, boolean updatePersistence) {
            if (com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.logTrace("MagnificationConnectionManagerConnectionCallback.onPerformScaleAction", 256L, "displayId=" + displayId + ";scale=" + scale + ";updatePersistence=" + updatePersistence);
            }
            com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mCallback.onPerformScaleAction(displayId, scale, updatePersistence);
        }

        public void onAccessibilityActionPerformed(int displayId) {
            if (com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.logTrace("MagnificationConnectionManagerConnectionCallback.onAccessibilityActionPerformed", 256L, "displayId=" + displayId);
            }
            com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mCallback.onAccessibilityActionPerformed(displayId);
        }

        public void onMove(int displayId) {
            if (com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mTrace.logTrace("MagnificationConnectionManagerConnectionCallback.onMove", 256L, "displayId=" + displayId);
            }
            com.android.server.accessibility.magnification.MagnificationConnectionManager.this.setTrackingTypingFocusEnabled(displayId, false);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mLock) {
                android.util.Slog.w(com.android.server.accessibility.magnification.MagnificationConnectionManager.TAG, "binderDied DeathRecipient :" + this.mExpiredDeathRecipient);
                if (this.mExpiredDeathRecipient) {
                    return;
                }
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mConnectionWrapper.unlinkToDeath(this);
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mConnectionWrapper = null;
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.mConnectionCallback = null;
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.setConnectionState(3);
                com.android.server.accessibility.magnification.MagnificationConnectionManager.this.resetWindowMagnifiers();
            }
        }
    }

    private static class WindowMagnifier {
        private static final java.util.concurrent.atomic.AtomicLongFieldUpdater<com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier> SUM_TIME_UPDATER = java.util.concurrent.atomic.AtomicLongFieldUpdater.newUpdater(com.android.server.accessibility.magnification.MagnificationConnectionManager.WindowMagnifier.class, "mTrackingTypingFocusSumTime");
        private final int mDisplayId;
        private boolean mEnabled;
        private final com.android.server.accessibility.magnification.MagnificationConnectionManager mMagnificationConnectionManager;
        private float mScale = 1.0f;
        private final android.graphics.Rect mBounds = new android.graphics.Rect();
        private final android.graphics.Rect mSourceBounds = new android.graphics.Rect();
        private int mIdOfLastServiceToControl = -1;
        private final android.graphics.PointF mMagnificationFrameOffsetRatio = new android.graphics.PointF(0.0f, 0.0f);
        private boolean mTrackingTypingFocusEnabled = true;
        private volatile long mTrackingTypingFocusStartTime = 0;
        private volatile long mTrackingTypingFocusSumTime = 0;

        WindowMagnifier(int displayId, com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager) {
            this.mDisplayId = displayId;
            this.mMagnificationConnectionManager = magnificationConnectionManager;
        }

        boolean enableWindowMagnificationInternal(float scale, float centerX, float centerY, android.view.accessibility.MagnificationAnimationCallback animationCallback, int windowPosition, int id) {
            if (java.lang.Float.isNaN(scale)) {
                scale = getScale();
            }
            float normScale = com.android.server.accessibility.magnification.MagnificationScaleProvider.constrainScale(scale);
            setMagnificationFrameOffsetRatioByWindowPosition(windowPosition);
            if (this.mMagnificationConnectionManager.enableWindowMagnificationInternal(this.mDisplayId, normScale, centerX, centerY, this.mMagnificationFrameOffsetRatio.x, this.mMagnificationFrameOffsetRatio.y, animationCallback)) {
                this.mScale = normScale;
                this.mEnabled = true;
                this.mIdOfLastServiceToControl = id;
                return true;
            }
            return false;
        }

        void setMagnificationFrameOffsetRatioByWindowPosition(int windowPosition) {
            switch (windowPosition) {
                case 0:
                    this.mMagnificationFrameOffsetRatio.set(0.0f, 0.0f);
                    break;
                case 1:
                    this.mMagnificationFrameOffsetRatio.set(-1.0f, -1.0f);
                    break;
            }
        }

        boolean disableWindowMagnificationInternal(android.view.accessibility.MagnificationAnimationCallback animationResultCallback) {
            if (!this.mEnabled || !this.mMagnificationConnectionManager.disableWindowMagnificationInternal(this.mDisplayId, animationResultCallback)) {
                return false;
            }
            this.mEnabled = false;
            this.mIdOfLastServiceToControl = -1;
            this.mTrackingTypingFocusEnabled = false;
            pauseTrackingTypingFocusRecord();
            return true;
        }

        void setScale(float scale) {
            if (!this.mEnabled) {
                return;
            }
            float normScale = com.android.server.accessibility.magnification.MagnificationScaleProvider.constrainScale(scale);
            if (java.lang.Float.compare(this.mScale, normScale) != 0 && this.mMagnificationConnectionManager.setScaleForWindowMagnificationInternal(this.mDisplayId, scale)) {
                this.mScale = normScale;
            }
        }

        float getScale() {
            return this.mScale;
        }

        void setMagnifierLocation(android.graphics.Rect rect) {
            this.mBounds.set(rect);
        }

        int getIdOfLastServiceToControl() {
            return this.mIdOfLastServiceToControl;
        }

        int pointersInWindow(android.view.MotionEvent motionEvent) {
            int count = 0;
            int pointerCount = motionEvent.getPointerCount();
            for (int i = 0; i < pointerCount; i++) {
                float x = motionEvent.getX(i);
                float y = motionEvent.getY(i);
                if (this.mBounds.contains((int) x, (int) y)) {
                    count++;
                }
            }
            return count;
        }

        boolean isPositionInSourceBounds(float x, float y) {
            return this.mSourceBounds.contains((int) x, (int) y);
        }

        void setTrackingTypingFocusEnabled(boolean trackingTypingFocusEnabled) {
            if (this.mMagnificationConnectionManager.isWindowMagnifierEnabled(this.mDisplayId) && this.mMagnificationConnectionManager.isImeVisible(this.mDisplayId) && trackingTypingFocusEnabled) {
                startTrackingTypingFocusRecord();
            }
            if (this.mTrackingTypingFocusEnabled && !trackingTypingFocusEnabled) {
                stopAndLogTrackingTypingFocusRecordIfNeeded();
            }
            this.mTrackingTypingFocusEnabled = trackingTypingFocusEnabled;
        }

        boolean isTrackingTypingFocusEnabled() {
            return this.mTrackingTypingFocusEnabled;
        }

        void startTrackingTypingFocusRecord() {
            if (this.mTrackingTypingFocusStartTime == 0) {
                this.mTrackingTypingFocusStartTime = android.os.SystemClock.uptimeMillis();
            }
        }

        void pauseTrackingTypingFocusRecord() {
            if (this.mTrackingTypingFocusStartTime != 0) {
                long elapsed = android.os.SystemClock.uptimeMillis() - this.mTrackingTypingFocusStartTime;
                SUM_TIME_UPDATER.addAndGet(this, elapsed);
                this.mTrackingTypingFocusStartTime = 0L;
            }
        }

        void stopAndLogTrackingTypingFocusRecordIfNeeded() {
            if (this.mTrackingTypingFocusStartTime != 0 || this.mTrackingTypingFocusSumTime != 0) {
                long elapsed = this.mTrackingTypingFocusStartTime != 0 ? android.os.SystemClock.uptimeMillis() - this.mTrackingTypingFocusStartTime : 0L;
                long duration = this.mTrackingTypingFocusSumTime + elapsed;
                this.mMagnificationConnectionManager.logTrackingTypingFocus(duration);
                this.mTrackingTypingFocusStartTime = 0L;
                this.mTrackingTypingFocusSumTime = 0L;
            }
        }

        boolean isEnabled() {
            return this.mEnabled;
        }

        void move(float offsetX, float offsetY) {
            this.mMagnificationConnectionManager.moveWindowMagnifierInternal(this.mDisplayId, offsetX, offsetY);
        }

        void reset() {
            this.mEnabled = false;
            this.mIdOfLastServiceToControl = -1;
            this.mSourceBounds.setEmpty();
        }

        public void onSourceBoundsChanged(android.graphics.Rect sourceBounds) {
            this.mSourceBounds.set(sourceBounds);
        }

        float getCenterX() {
            return this.mSourceBounds.exactCenterX();
        }

        float getCenterY() {
            return this.mSourceBounds.exactCenterY();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean enableWindowMagnificationInternal(int displayId, float scale, float centerX, float centerY, float magnificationFrameOffsetRatioX, float magnificationFrameOffsetRatioY, android.view.accessibility.MagnificationAnimationCallback animationCallback) {
        if (!waitConnectionWithTimeoutIfNeeded()) {
            android.util.Slog.w(TAG, "enableWindowMagnificationInternal mConnectionWrapper is null. mConnectionState=" + connectionStateToString(this.mConnectionState));
            return false;
        }
        return this.mConnectionWrapper.enableWindowMagnification(displayId, scale, centerX, centerY, magnificationFrameOffsetRatioX, magnificationFrameOffsetRatioY, animationCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setScaleForWindowMagnificationInternal(int displayId, float scale) {
        return this.mConnectionWrapper != null && this.mConnectionWrapper.setScaleForWindowMagnification(displayId, scale);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean disableWindowMagnificationInternal(int displayId, android.view.accessibility.MagnificationAnimationCallback animationCallback) {
        if (this.mConnectionWrapper == null) {
            android.util.Slog.w(TAG, "mConnectionWrapper is null");
            return false;
        }
        return this.mConnectionWrapper.disableWindowMagnification(displayId, animationCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean moveWindowMagnifierInternal(int displayId, float offsetX, float offsetY) {
        return this.mConnectionWrapper != null && this.mConnectionWrapper.moveWindowMagnifier(displayId, offsetX, offsetY);
    }

    private boolean moveWindowMagnifierToPositionInternal(int displayId, float positionX, float positionY, android.view.accessibility.MagnificationAnimationCallback animationCallback) {
        return this.mConnectionWrapper != null && this.mConnectionWrapper.moveWindowMagnifierToPosition(displayId, positionX, positionY, animationCallback);
    }

    boolean waitConnectionWithTimeoutIfNeeded() {
        long endMillis = android.os.SystemClock.uptimeMillis() + ((long) WAIT_CONNECTION_TIMEOUT_MILLIS);
        while (this.mConnectionState == 0 && android.os.SystemClock.uptimeMillis() < endMillis) {
            try {
                this.mLock.wait(endMillis - android.os.SystemClock.uptimeMillis());
            } catch (java.lang.InterruptedException e) {
            }
        }
        return isConnected();
    }
}
