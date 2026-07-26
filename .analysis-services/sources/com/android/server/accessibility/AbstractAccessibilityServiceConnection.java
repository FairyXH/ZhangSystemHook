package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
abstract class AbstractAccessibilityServiceConnection extends android.accessibilityservice.IAccessibilityServiceConnection.Stub implements android.content.ServiceConnection, android.os.IBinder.DeathRecipient, com.android.server.accessibility.KeyEventDispatcher.KeyEventFilter, com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient {
    private static final boolean DEBUG = false;
    public static final int DISPLAY_TYPE_DEFAULT = 1;
    public static final int DISPLAY_TYPE_PROXY = 2;
    private static final java.lang.String LOG_TAG = "AbstractAccessibilityServiceConnection";
    protected static final java.lang.String TAKE_SCREENSHOT = "takeScreenshot";
    private static final java.lang.String TRACE_SVC_CLIENT = "AbstractAccessibilityServiceConnection.IAccessibilityServiceClient";
    private static final java.lang.String TRACE_SVC_CONN = "AbstractAccessibilityServiceConnection.IAccessibilityServiceConnection";
    private static final java.lang.String TRACE_WM = "WindowManagerInternal";
    private static final int WAIT_WINDOWS_TIMEOUT_MILLIS = 5000;
    final com.android.server.accessibility.AccessibilityWindowManager mA11yWindowManager;
    protected final android.accessibilityservice.AccessibilityServiceInfo mAccessibilityServiceInfo;
    protected java.lang.String mAttributionTag;
    boolean mCaptureFingerprintGestures;
    final android.content.ComponentName mComponentName;
    protected final android.content.Context mContext;
    private final android.hardware.display.DisplayManager mDisplayManager;
    protected int mDisplayTypes;
    public android.os.Handler mEventDispatchHandler;
    int mEventTypes;
    int mFeedbackType;
    int mFetchFlags;
    int mGenericMotionEventSources;
    private final com.android.internal.compat.IPlatformCompat mIPlatformCompat;
    final int mId;
    public final com.android.server.accessibility.AbstractAccessibilityServiceConnection.InvocationHandler mInvocationHandler;
    boolean mIsDefault;
    boolean mLastAccessibilityButtonCallbackState;
    protected final java.lang.Object mLock;
    private final android.os.Handler mMainHandler;
    long mNotificationTimeout;
    int mObservedMotionEventSources;
    final android.util.SparseArray<android.os.IBinder> mOverlayWindowTokens;
    private java.util.List<android.view.SurfaceControl> mOverlays;
    java.util.Set<java.lang.String> mPackageNames;
    final android.util.SparseArray<android.view.accessibility.AccessibilityEvent> mPendingEvents;
    private final android.os.PowerManager mPowerManager;
    boolean mReceivedAccessibilityButtonCallbackSinceBind;
    boolean mRequestAccessibilityButton;
    boolean mRequestFilterKeyEvents;
    boolean mRequestImeApis;
    private boolean mRequestMultiFingerGestures;
    private android.util.SparseArray<java.lang.Long> mRequestTakeScreenshotOfWindowTimestampMs;
    private long mRequestTakeScreenshotTimestampMs;
    boolean mRequestTouchExplorationMode;
    private boolean mRequestTwoFingerPassthrough;
    boolean mRetrieveInteractiveWindows;
    protected final com.android.server.accessibility.AccessibilitySecurityPolicy mSecurityPolicy;
    private boolean mSendMotionEvents;
    android.os.IBinder mService;
    private android.util.SparseArray<java.lang.Boolean> mServiceDetectsGestures;
    private boolean mServiceHandlesDoubleTap;
    android.accessibilityservice.IAccessibilityServiceClient mServiceInterface;
    private final com.android.server.accessibility.SystemActionPerformer mSystemActionPerformer;
    protected final com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport mSystemSupport;
    protected final android.accessibilityservice.AccessibilityTrace mTrace;
    boolean mUsesAccessibilityCache;
    protected final com.android.server.wm.WindowManagerInternal mWindowManagerService;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DisplayTypes {
    }

    public interface SystemSupport {
        void attachAccessibilityOverlayToDisplay(int i, int i2, android.view.SurfaceControl surfaceControl, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback);

        int getCurrentUserIdLocked();

        com.android.server.accessibility.FingerprintGestureDispatcher getFingerprintGestureDispatcher();

        com.android.server.accessibility.KeyEventDispatcher getKeyEventDispatcher();

        com.android.server.accessibility.magnification.MagnificationProcessor getMagnificationProcessor();

        com.android.server.accessibility.MotionEventInjector getMotionEventInjectorForDisplayLocked(int i);

        android.app.PendingIntent getPendingIntentActivity(android.content.Context context, int i, android.content.Intent intent, int i2);

        android.util.Pair<float[], android.view.MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(int i);

        boolean isAccessibilityButtonShown();

        void onClientChangeLocked(boolean z);

        void onDoubleTap(int i);

        void onDoubleTapAndHold(int i);

        void onProxyChanged(int i);

        void persistComponentNamesToSettingLocked(java.lang.String str, java.util.Set<android.content.ComponentName> set, int i);

        void requestDelegating(int i);

        void requestDragging(int i, int i2);

        void requestImeLocked(com.android.server.accessibility.AbstractAccessibilityServiceConnection abstractAccessibilityServiceConnection);

        void requestTouchExploration(int i);

        void setGestureDetectionPassthroughRegion(int i, android.graphics.Region region);

        void setServiceDetectsGesturesEnabled(int i, boolean z);

        void setTouchExplorationPassthroughRegion(int i, android.graphics.Region region);

        void unbindImeLocked(com.android.server.accessibility.AbstractAccessibilityServiceConnection abstractAccessibilityServiceConnection);
    }

    protected abstract boolean hasRightsToCurrentUserLocked();

    public AbstractAccessibilityServiceConnection(android.content.Context context, android.content.ComponentName componentName, android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo, int id, android.os.Handler mainHandler, java.lang.Object lock, com.android.server.accessibility.AccessibilitySecurityPolicy securityPolicy, com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport systemSupport, android.accessibilityservice.AccessibilityTrace trace, com.android.server.wm.WindowManagerInternal windowManagerInternal, com.android.server.accessibility.SystemActionPerformer systemActionPerfomer, com.android.server.accessibility.AccessibilityWindowManager a11yWindowManager) {
        super(android.os.PermissionEnforcer.fromContext(context));
        this.mDisplayTypes = 1;
        this.mPackageNames = new java.util.HashSet();
        this.mServiceDetectsGestures = new android.util.SparseArray<>(0);
        this.mPendingEvents = new android.util.SparseArray<>();
        this.mUsesAccessibilityCache = false;
        this.mOverlayWindowTokens = new android.util.SparseArray<>();
        this.mOverlays = new java.util.ArrayList();
        this.mRequestTakeScreenshotOfWindowTimestampMs = new android.util.SparseArray<>();
        this.mContext = context;
        this.mWindowManagerService = windowManagerInternal;
        this.mId = id;
        this.mComponentName = componentName;
        this.mAccessibilityServiceInfo = accessibilityServiceInfo;
        this.mLock = lock;
        this.mSecurityPolicy = securityPolicy;
        this.mSystemActionPerformer = systemActionPerfomer;
        this.mSystemSupport = systemSupport;
        this.mTrace = trace;
        this.mMainHandler = mainHandler;
        this.mInvocationHandler = new com.android.server.accessibility.AbstractAccessibilityServiceConnection.InvocationHandler(mainHandler.getLooper());
        this.mA11yWindowManager = a11yWindowManager;
        this.mDisplayManager = (android.hardware.display.DisplayManager) context.getSystemService("display");
        this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService("power");
        this.mIPlatformCompat = com.android.internal.compat.IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService("platform_compat"));
        this.mEventDispatchHandler = new android.os.Handler(mainHandler.getLooper()) { // from class: com.android.server.accessibility.AbstractAccessibilityServiceConnection.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message message) {
                int eventType = message.what;
                android.view.accessibility.AccessibilityEvent event = (android.view.accessibility.AccessibilityEvent) message.obj;
                boolean serviceWantsEvent = message.arg1 != 0;
                com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.notifyAccessibilityEventInternal(eventType, event, serviceWantsEvent);
            }
        };
        setDynamicallyConfigurableProperties(accessibilityServiceInfo);
    }

    @Override // com.android.server.accessibility.KeyEventDispatcher.KeyEventFilter
    public boolean onKeyEvent(android.view.KeyEvent keyEvent, int sequenceNumber) {
        if (!this.mRequestFilterKeyEvents || this.mServiceInterface == null || (this.mAccessibilityServiceInfo.getCapabilities() & 8) == 0 || !this.mSecurityPolicy.checkAccessibilityAccess(this)) {
            return false;
        }
        try {
            if (svcClientTracingEnabled()) {
                logTraceSvcClient("onKeyEvent", keyEvent + ", " + sequenceNumber);
            }
            this.mServiceInterface.onKeyEvent(keyEvent, sequenceNumber);
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    public void setDynamicallyConfigurableProperties(android.accessibilityservice.AccessibilityServiceInfo info) {
        this.mEventTypes = info.eventTypes;
        this.mFeedbackType = info.feedbackType;
        java.lang.String[] packageNames = info.packageNames;
        this.mPackageNames.clear();
        if (packageNames != null) {
            this.mPackageNames.addAll(java.util.Arrays.asList(packageNames));
        }
        this.mNotificationTimeout = info.notificationTimeout;
        this.mIsDefault = (info.flags & 1) != 0;
        this.mGenericMotionEventSources = info.getMotionEventSources();
        if (android.view.accessibility.Flags.motionEventObserving()) {
            if (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESSIBILITY_MOTION_EVENT_OBSERVING") == 0) {
                this.mObservedMotionEventSources = info.getObservedMotionEventSources();
            } else {
                android.util.Slog.e(LOG_TAG, "Observing motion events requires android.Manifest.permission.ACCESSIBILITY_MOTION_EVENT_OBSERVING.");
                this.mObservedMotionEventSources = 0;
            }
        }
        if (supportsFlagForNotImportantViews(info)) {
            if ((info.flags & 2) != 0) {
                this.mFetchFlags |= 128;
            } else {
                this.mFetchFlags &= -129;
            }
        }
        if ((info.flags & 16) != 0) {
            this.mFetchFlags |= 256;
        } else {
            this.mFetchFlags &= -257;
        }
        if (this.mAccessibilityServiceInfo.isAccessibilityTool()) {
            this.mFetchFlags |= 512;
        } else {
            this.mFetchFlags &= -513;
        }
        this.mRequestTouchExplorationMode = (info.flags & 4) != 0;
        this.mServiceHandlesDoubleTap = (info.flags & 2048) != 0;
        this.mRequestMultiFingerGestures = (info.flags & 4096) != 0;
        this.mRequestTwoFingerPassthrough = (info.flags & 8192) != 0;
        this.mSendMotionEvents = (info.flags & 16384) != 0;
        this.mRequestFilterKeyEvents = (info.flags & 32) != 0;
        this.mRetrieveInteractiveWindows = (info.flags & 64) != 0;
        this.mCaptureFingerprintGestures = (info.flags & 512) != 0;
        this.mRequestAccessibilityButton = (info.flags & 256) != 0;
        this.mRequestImeApis = (info.flags & 32768) != 0;
    }

    protected boolean supportsFlagForNotImportantViews(android.accessibilityservice.AccessibilityServiceInfo info) {
        return info.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion >= 16;
    }

    public boolean canReceiveEventsLocked() {
        return (this.mEventTypes == 0 || this.mService == null) ? false : true;
    }

    public void setOnKeyEventResult(boolean handled, int sequence) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setOnKeyEventResult", "handled=" + handled + ";sequence=" + sequence);
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemSupport.getKeyEventDispatcher().setOnKeyEventResult(this, handled, sequence);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public android.accessibilityservice.AccessibilityServiceInfo getServiceInfo() {
        android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getServiceInfo", "");
        }
        synchronized (this.mLock) {
            accessibilityServiceInfo = this.mAccessibilityServiceInfo;
        }
        return accessibilityServiceInfo;
    }

    public int getCapabilities() {
        return this.mAccessibilityServiceInfo.getCapabilities();
    }

    int getRelevantEventTypes() {
        return (this.mUsesAccessibilityCache ? 4307005 : 32) | this.mEventTypes;
    }

    public void setServiceInfo(android.accessibilityservice.AccessibilityServiceInfo info) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setServiceInfo", "info=" + info);
        }
        if (!info.isWithinParcelableSize()) {
            throw new java.lang.IllegalStateException("Cannot update service info: size is larger than safe parcelable limits.");
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                boolean oldRequestIme = this.mRequestImeApis;
                android.accessibilityservice.AccessibilityServiceInfo oldInfo = this.mAccessibilityServiceInfo;
                if (oldInfo != null) {
                    oldInfo.updateDynamicallyConfigurableProperties(this.mIPlatformCompat, info);
                    setDynamicallyConfigurableProperties(oldInfo);
                } else {
                    setDynamicallyConfigurableProperties(info);
                }
                this.mSystemSupport.onClientChangeLocked(true);
                if (!oldRequestIme && this.mRequestImeApis) {
                    this.mSystemSupport.requestImeLocked(this);
                } else if (oldRequestIme && !this.mRequestImeApis) {
                    this.mSystemSupport.unbindImeLocked(this);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void setInstalledAndEnabledServices(java.util.List<android.accessibilityservice.AccessibilityServiceInfo> infos) {
    }

    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAndEnabledServices() {
        return null;
    }

    public void setAttributionTag(java.lang.String attributionTag) {
        this.mAttributionTag = attributionTag;
    }

    java.lang.String getAttributionTag() {
        return this.mAttributionTag;
    }

    public android.view.accessibility.AccessibilityWindowInfo.WindowListSparseArray getWindows() {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getWindows", "");
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            boolean permissionGranted = this.mSecurityPolicy.canRetrieveWindowsLocked(this);
            if (!permissionGranted) {
                return null;
            }
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return null;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.view.accessibility.AccessibilityWindowInfo.WindowListSparseArray allWindows = new android.view.accessibility.AccessibilityWindowInfo.WindowListSparseArray();
                java.util.ArrayList<java.lang.Integer> displayList = this.mA11yWindowManager.getDisplayListLocked(this.mDisplayTypes);
                int displayListCounts = displayList.size();
                if (displayListCounts > 0) {
                    for (int i = 0; i < displayListCounts; i++) {
                        int displayId = displayList.get(i).intValue();
                        ensureWindowsAvailableTimedLocked(displayId);
                        java.util.List<android.view.accessibility.AccessibilityWindowInfo> windowList = getWindowsByDisplayLocked(displayId);
                        if (windowList != null) {
                            allWindows.put(displayId, windowList);
                        }
                    }
                }
                return allWindows;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    protected void setDisplayTypes(int displayTypes) {
        this.mDisplayTypes = displayTypes;
    }

    public android.view.accessibility.AccessibilityWindowInfo getWindow(int windowId) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getWindow", "windowId=" + windowId);
        }
        int displayId = -1;
        if (windowId != -1) {
            displayId = this.mA11yWindowManager.getDisplayIdByUserIdAndWindowId(this.mSystemSupport.getCurrentUserIdLocked(), windowId);
        }
        synchronized (this.mLock) {
            ensureWindowsAvailableTimedLocked(displayId);
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            boolean permissionGranted = this.mSecurityPolicy.canRetrieveWindowsLocked(this);
            if (!permissionGranted) {
                return null;
            }
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return null;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.view.accessibility.AccessibilityWindowInfo window = this.mA11yWindowManager.findA11yWindowInfoByIdLocked(windowId);
                if (window == null) {
                    return null;
                }
                android.view.accessibility.AccessibilityWindowInfo windowClone = android.view.accessibility.AccessibilityWindowInfo.obtain(window);
                windowClone.setConnectionId(this.mId);
                return windowClone;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public java.lang.String[] findAccessibilityNodeInfosByViewId(int accessibilityWindowId, long accessibilityNodeId, java.lang.String viewIdResName, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long interrogatingTid) throws java.lang.Throwable {
        android.graphics.Region partialInteractiveRegion;
        android.view.MagnificationSpec spec;
        int interrogatingPid;
        android.graphics.Region partialInteractiveRegion2;
        android.view.MagnificationSpec spec2;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("findAccessibilityNodeInfosByViewId", "accessibilityWindowId=" + accessibilityWindowId + ";accessibilityNodeId=" + accessibilityNodeId + ";viewIdResName=" + viewIdResName + ";interactionId=" + interactionId + ";callback=" + callback + ";interrogatingTid=" + interrogatingTid);
        }
        android.graphics.Region partialInteractiveRegion3 = android.graphics.Region.obtain();
        synchronized (this.mLock) {
            try {
                this.mUsesAccessibilityCache = true;
                if (!hasRightsToCurrentUserLocked()) {
                    return null;
                }
                int resolvedWindowId = resolveAccessibilityWindowIdLocked(accessibilityWindowId);
                boolean permissionGranted = this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolvedWindowId);
                if (!permissionGranted) {
                    return null;
                }
                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolvedWindowId);
                if (connection == null) {
                    return null;
                }
                if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolvedWindowId, partialInteractiveRegion3)) {
                    partialInteractiveRegion = partialInteractiveRegion3;
                } else {
                    partialInteractiveRegion3.recycle();
                    partialInteractiveRegion = null;
                }
                try {
                    android.util.Pair<float[], android.view.MagnificationSpec> transformMatrixAndSpec = getWindowTransformationMatrixAndMagnificationSpec(resolvedWindowId);
                    float[] transformMatrix = (float[]) transformMatrixAndSpec.first;
                    android.view.MagnificationSpec spec3 = (android.view.MagnificationSpec) transformMatrixAndSpec.second;
                    if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                        return null;
                    }
                    int interrogatingPid2 = android.os.Binder.getCallingPid();
                    android.graphics.Region partialInteractiveRegion4 = partialInteractiveRegion;
                    android.view.accessibility.IAccessibilityInteractionConnectionCallback callback2 = replaceCallbackIfNeeded(callback, resolvedWindowId, interactionId, interrogatingPid2, interrogatingTid);
                    long identityToken = android.os.Binder.clearCallingIdentity();
                    if (intConnTracingEnabled()) {
                        interrogatingPid = interrogatingPid2;
                        spec = spec3;
                        logTraceIntConn("findAccessibilityNodeInfosByViewId", accessibilityNodeId + ";" + viewIdResName + ";" + partialInteractiveRegion4 + ";" + interactionId + ";" + callback2 + ";" + this.mFetchFlags + ";" + interrogatingPid + ";" + interrogatingTid + ";" + spec + ";" + java.util.Arrays.toString(transformMatrix));
                    } else {
                        spec = spec3;
                        interrogatingPid = interrogatingPid2;
                    }
                    try {
                        spec2 = spec;
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (android.os.RemoteException e) {
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    }
                    try {
                        connection.getRemote().findAccessibilityNodeInfosByViewId(accessibilityNodeId, viewIdResName, partialInteractiveRegion2, interactionId, callback2, this.mFetchFlags, interrogatingPid, interrogatingTid, spec2, transformMatrix);
                        java.lang.String[] strArrComputeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connection.getPackageName(), connection.getUid());
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return strArrComputeValidReportedPackages;
                    } catch (android.os.RemoteException e2) {
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return null;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    public java.lang.String[] findAccessibilityNodeInfosByText(int accessibilityWindowId, long accessibilityNodeId, java.lang.String text, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long interrogatingTid) throws java.lang.Throwable {
        android.graphics.Region partialInteractiveRegion;
        android.view.MagnificationSpec spec;
        int interrogatingPid;
        android.graphics.Region partialInteractiveRegion2;
        android.view.MagnificationSpec spec2;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("findAccessibilityNodeInfosByText", "accessibilityWindowId=" + accessibilityWindowId + ";accessibilityNodeId=" + accessibilityNodeId + ";text=" + text + ";interactionId=" + interactionId + ";callback=" + callback + ";interrogatingTid=" + interrogatingTid);
        }
        android.graphics.Region partialInteractiveRegion3 = android.graphics.Region.obtain();
        synchronized (this.mLock) {
            try {
                this.mUsesAccessibilityCache = true;
                if (!hasRightsToCurrentUserLocked()) {
                    return null;
                }
                int resolvedWindowId = resolveAccessibilityWindowIdLocked(accessibilityWindowId);
                boolean permissionGranted = this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolvedWindowId);
                if (!permissionGranted) {
                    return null;
                }
                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolvedWindowId);
                if (connection == null) {
                    return null;
                }
                if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolvedWindowId, partialInteractiveRegion3)) {
                    partialInteractiveRegion = partialInteractiveRegion3;
                } else {
                    partialInteractiveRegion3.recycle();
                    partialInteractiveRegion = null;
                }
                try {
                    android.util.Pair<float[], android.view.MagnificationSpec> transformMatrixAndSpec = getWindowTransformationMatrixAndMagnificationSpec(resolvedWindowId);
                    float[] transformMatrix = (float[]) transformMatrixAndSpec.first;
                    android.view.MagnificationSpec spec3 = (android.view.MagnificationSpec) transformMatrixAndSpec.second;
                    if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                        return null;
                    }
                    int interrogatingPid2 = android.os.Binder.getCallingPid();
                    android.graphics.Region partialInteractiveRegion4 = partialInteractiveRegion;
                    android.view.accessibility.IAccessibilityInteractionConnectionCallback callback2 = replaceCallbackIfNeeded(callback, resolvedWindowId, interactionId, interrogatingPid2, interrogatingTid);
                    long identityToken = android.os.Binder.clearCallingIdentity();
                    if (intConnTracingEnabled()) {
                        interrogatingPid = interrogatingPid2;
                        spec = spec3;
                        logTraceIntConn("findAccessibilityNodeInfosByText", accessibilityNodeId + ";" + text + ";" + partialInteractiveRegion4 + ";" + interactionId + ";" + callback2 + ";" + this.mFetchFlags + ";" + interrogatingPid + ";" + interrogatingTid + ";" + spec + ";" + java.util.Arrays.toString(transformMatrix));
                    } else {
                        spec = spec3;
                        interrogatingPid = interrogatingPid2;
                    }
                    try {
                        spec2 = spec;
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (android.os.RemoteException e) {
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    }
                    try {
                        connection.getRemote().findAccessibilityNodeInfosByText(accessibilityNodeId, text, partialInteractiveRegion2, interactionId, callback2, this.mFetchFlags, interrogatingPid, interrogatingTid, spec2, transformMatrix);
                        java.lang.String[] strArrComputeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connection.getPackageName(), connection.getUid());
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return strArrComputeValidReportedPackages;
                    } catch (android.os.RemoteException e2) {
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return null;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    public java.lang.String[] findAccessibilityNodeInfoByAccessibilityId(int accessibilityWindowId, long accessibilityNodeId, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, long interrogatingTid, android.os.Bundle arguments) throws java.lang.Throwable {
        android.graphics.Region partialInteractiveRegion;
        android.view.MagnificationSpec spec;
        int interrogatingPid;
        android.graphics.Region partialInteractiveRegion2;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("findAccessibilityNodeInfoByAccessibilityId", "accessibilityWindowId=" + accessibilityWindowId + ";accessibilityNodeId=" + accessibilityNodeId + ";interactionId=" + interactionId + ";callback=" + callback + ";flags=" + flags + ";interrogatingTid=" + interrogatingTid + ";arguments=" + arguments);
        }
        android.graphics.Region partialInteractiveRegion3 = android.graphics.Region.obtain();
        synchronized (this.mLock) {
            try {
                this.mUsesAccessibilityCache = true;
                if (!hasRightsToCurrentUserLocked()) {
                    return null;
                }
                int resolvedWindowId = resolveAccessibilityWindowIdLocked(accessibilityWindowId);
                boolean permissionGranted = this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolvedWindowId);
                if (!permissionGranted) {
                    return null;
                }
                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolvedWindowId);
                if (connection == null) {
                    return null;
                }
                if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolvedWindowId, partialInteractiveRegion3)) {
                    partialInteractiveRegion = partialInteractiveRegion3;
                } else {
                    partialInteractiveRegion3.recycle();
                    partialInteractiveRegion = null;
                }
                try {
                    android.util.Pair<float[], android.view.MagnificationSpec> transformMatrixAndSpec = getWindowTransformationMatrixAndMagnificationSpec(resolvedWindowId);
                    float[] transformMatrix = (float[]) transformMatrixAndSpec.first;
                    android.view.MagnificationSpec spec2 = (android.view.MagnificationSpec) transformMatrixAndSpec.second;
                    if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                        return null;
                    }
                    int interrogatingPid2 = android.os.Binder.getCallingPid();
                    android.graphics.Region partialInteractiveRegion4 = partialInteractiveRegion;
                    android.view.accessibility.IAccessibilityInteractionConnectionCallback callback2 = replaceCallbackIfNeeded(callback, resolvedWindowId, interactionId, interrogatingPid2, interrogatingTid);
                    long identityToken = android.os.Binder.clearCallingIdentity();
                    if (intConnTracingEnabled()) {
                        interrogatingPid = interrogatingPid2;
                        spec = spec2;
                        logTraceIntConn("findAccessibilityNodeInfoByAccessibilityId", accessibilityNodeId + ";" + partialInteractiveRegion4 + ";" + interactionId + ";" + callback2 + ";" + (this.mFetchFlags | flags) + ";" + interrogatingPid + ";" + interrogatingTid + ";" + spec2 + ";" + java.util.Arrays.toString(transformMatrix) + ";" + arguments);
                    } else {
                        spec = spec2;
                        interrogatingPid = interrogatingPid2;
                    }
                    try {
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (android.os.RemoteException e) {
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    }
                    try {
                        connection.getRemote().findAccessibilityNodeInfoByAccessibilityId(accessibilityNodeId, partialInteractiveRegion2, interactionId, callback2, this.mFetchFlags | flags, interrogatingPid, interrogatingTid, spec, transformMatrix, arguments);
                        java.lang.String[] strArrComputeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connection.getPackageName(), connection.getUid());
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return strArrComputeValidReportedPackages;
                    } catch (android.os.RemoteException e2) {
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return null;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    public java.lang.String[] findFocus(int accessibilityWindowId, long accessibilityNodeId, int focusType, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long interrogatingTid) throws java.lang.Throwable {
        android.graphics.Region partialInteractiveRegion;
        int interrogatingPid;
        android.view.MagnificationSpec spec;
        android.graphics.Region partialInteractiveRegion2;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("findFocus", "accessibilityWindowId=" + accessibilityWindowId + ";accessibilityNodeId=" + accessibilityNodeId + ";focusType=" + focusType + ";interactionId=" + interactionId + ";callback=" + callback + ";interrogatingTid=" + interrogatingTid);
        }
        android.graphics.Region partialInteractiveRegion3 = android.graphics.Region.obtain();
        synchronized (this.mLock) {
            try {
                if (!hasRightsToCurrentUserLocked()) {
                    return null;
                }
                int resolvedWindowId = resolveAccessibilityWindowIdForFindFocusLocked(accessibilityWindowId, focusType);
                boolean permissionGranted = this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolvedWindowId);
                if (!permissionGranted) {
                    return null;
                }
                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolvedWindowId);
                if (connection == null) {
                    return null;
                }
                if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolvedWindowId, partialInteractiveRegion3)) {
                    partialInteractiveRegion = partialInteractiveRegion3;
                } else {
                    partialInteractiveRegion3.recycle();
                    partialInteractiveRegion = null;
                }
                try {
                    android.util.Pair<float[], android.view.MagnificationSpec> transformMatrixAndSpec = getWindowTransformationMatrixAndMagnificationSpec(resolvedWindowId);
                    float[] transformMatrix = (float[]) transformMatrixAndSpec.first;
                    android.view.MagnificationSpec spec2 = (android.view.MagnificationSpec) transformMatrixAndSpec.second;
                    if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                        return null;
                    }
                    int interrogatingPid2 = android.os.Binder.getCallingPid();
                    android.graphics.Region partialInteractiveRegion4 = partialInteractiveRegion;
                    android.view.accessibility.IAccessibilityInteractionConnectionCallback callback2 = replaceCallbackIfNeeded(callback, resolvedWindowId, interactionId, interrogatingPid2, interrogatingTid);
                    long identityToken = android.os.Binder.clearCallingIdentity();
                    if (intConnTracingEnabled()) {
                        interrogatingPid = interrogatingPid2;
                        spec = spec2;
                        logTraceIntConn("findFocus", accessibilityNodeId + ";" + focusType + ";" + partialInteractiveRegion4 + ";" + interactionId + ";" + callback2 + ";" + this.mFetchFlags + ";" + interrogatingPid + ";" + interrogatingTid + ";" + spec + ";" + java.util.Arrays.toString(transformMatrix));
                    } else {
                        interrogatingPid = interrogatingPid2;
                        spec = spec2;
                    }
                    try {
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (android.os.RemoteException e) {
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    }
                    try {
                        connection.getRemote().findFocus(accessibilityNodeId, focusType, partialInteractiveRegion2, interactionId, callback2, this.mFetchFlags, interrogatingPid, interrogatingTid, spec, transformMatrix);
                        java.lang.String[] strArrComputeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connection.getPackageName(), connection.getUid());
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return strArrComputeValidReportedPackages;
                    } catch (android.os.RemoteException e2) {
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return null;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    public java.lang.String[] focusSearch(int accessibilityWindowId, long accessibilityNodeId, int direction, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long interrogatingTid) throws java.lang.Throwable {
        android.graphics.Region partialInteractiveRegion;
        android.view.MagnificationSpec spec;
        int interrogatingPid;
        android.graphics.Region partialInteractiveRegion2;
        android.view.MagnificationSpec spec2;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("focusSearch", "accessibilityWindowId=" + accessibilityWindowId + ";accessibilityNodeId=" + accessibilityNodeId + ";direction=" + direction + ";interactionId=" + interactionId + ";callback=" + callback + ";interrogatingTid=" + interrogatingTid);
        }
        android.graphics.Region partialInteractiveRegion3 = android.graphics.Region.obtain();
        synchronized (this.mLock) {
            try {
                if (!hasRightsToCurrentUserLocked()) {
                    return null;
                }
                int resolvedWindowId = resolveAccessibilityWindowIdLocked(accessibilityWindowId);
                boolean permissionGranted = this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolvedWindowId);
                if (!permissionGranted) {
                    return null;
                }
                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolvedWindowId);
                if (connection == null) {
                    return null;
                }
                if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolvedWindowId, partialInteractiveRegion3)) {
                    partialInteractiveRegion = partialInteractiveRegion3;
                } else {
                    partialInteractiveRegion3.recycle();
                    partialInteractiveRegion = null;
                }
                try {
                    android.util.Pair<float[], android.view.MagnificationSpec> transformMatrixAndSpec = getWindowTransformationMatrixAndMagnificationSpec(resolvedWindowId);
                    float[] transformMatrix = (float[]) transformMatrixAndSpec.first;
                    android.view.MagnificationSpec spec3 = (android.view.MagnificationSpec) transformMatrixAndSpec.second;
                    if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                        return null;
                    }
                    int interrogatingPid2 = android.os.Binder.getCallingPid();
                    android.graphics.Region partialInteractiveRegion4 = partialInteractiveRegion;
                    android.view.accessibility.IAccessibilityInteractionConnectionCallback callback2 = replaceCallbackIfNeeded(callback, resolvedWindowId, interactionId, interrogatingPid2, interrogatingTid);
                    long identityToken = android.os.Binder.clearCallingIdentity();
                    if (intConnTracingEnabled()) {
                        interrogatingPid = interrogatingPid2;
                        spec = spec3;
                        logTraceIntConn("focusSearch", accessibilityNodeId + ";" + direction + ";" + partialInteractiveRegion4 + ";" + interactionId + ";" + callback2 + ";" + this.mFetchFlags + ";" + interrogatingPid + ";" + interrogatingTid + ";" + spec + ";" + java.util.Arrays.toString(transformMatrix));
                    } else {
                        spec = spec3;
                        interrogatingPid = interrogatingPid2;
                    }
                    try {
                        spec2 = spec;
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (android.os.RemoteException e) {
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        partialInteractiveRegion2 = partialInteractiveRegion4;
                    }
                    try {
                        connection.getRemote().focusSearch(accessibilityNodeId, direction, partialInteractiveRegion2, interactionId, callback2, this.mFetchFlags, interrogatingPid, interrogatingTid, spec2, transformMatrix);
                        java.lang.String[] strArrComputeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connection.getPackageName(), connection.getUid());
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return strArrComputeValidReportedPackages;
                    } catch (android.os.RemoteException e2) {
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        return null;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        android.os.Binder.restoreCallingIdentity(identityToken);
                        if (partialInteractiveRegion2 != null && android.os.Binder.isProxy(connection.getRemote())) {
                            partialInteractiveRegion2.recycle();
                        }
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    public void sendGesture(int sequence, android.content.pm.ParceledListSlice gestureSteps) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("sendGesture", "sequence=" + sequence + ";gestureSteps=" + gestureSteps);
        }
    }

    public void dispatchGesture(int sequence, android.content.pm.ParceledListSlice gestureSteps, int displayId) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("dispatchGesture", "sequence=" + sequence + ";gestureSteps=" + gestureSteps + ";displayId=" + displayId);
        }
    }

    public boolean performAccessibilityAction(int accessibilityWindowId, long accessibilityNodeId, int action, android.os.Bundle arguments, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long interrogatingTid) throws android.os.RemoteException {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("performAccessibilityAction", "accessibilityWindowId=" + accessibilityWindowId + ";accessibilityNodeId=" + accessibilityNodeId + ";action=" + action + ";arguments=" + arguments + ";interactionId=" + interactionId + ";callback=" + callback + ";interrogatingTid=" + interrogatingTid);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            int resolvedWindowId = resolveAccessibilityWindowIdLocked(accessibilityWindowId);
            if (!this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolvedWindowId)) {
                return false;
            }
            if (this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return performAccessibilityActionInternal(this.mSystemSupport.getCurrentUserIdLocked(), resolvedWindowId, accessibilityNodeId, action, arguments, interactionId, callback, this.mFetchFlags, interrogatingTid);
            }
            return false;
        }
    }

    public boolean performGlobalAction(int action) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("performGlobalAction", "action=" + action);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return this.mSystemActionPerformer.performSystemAction(action);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public java.util.List<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> getSystemActions() {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getSystemActions", "");
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return java.util.Collections.emptyList();
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return this.mSystemActionPerformer.getSystemActions();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public boolean isFingerprintGestureDetectionAvailable() {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("isFingerprintGestureDetectionAvailable", "");
        }
        boolean z = false;
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            return false;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (!isCapturingFingerprintGestures()) {
                return false;
            }
            com.android.server.accessibility.FingerprintGestureDispatcher dispatcher = this.mSystemSupport.getFingerprintGestureDispatcher();
            if (dispatcher != null) {
                if (dispatcher.isFingerprintGestureDetectionAvailable()) {
                    z = true;
                }
            }
            return z;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public android.accessibilityservice.MagnificationConfig getMagnificationConfig(int displayId) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationConfig", "displayId=" + displayId);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return this.mSystemSupport.getMagnificationProcessor().getMagnificationConfig(displayId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public float getMagnificationScale(int displayId) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationScale", "displayId=" + displayId);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return 1.0f;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return this.mSystemSupport.getMagnificationProcessor().getScale(displayId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public android.graphics.Region getMagnificationRegion(int displayId) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationRegion", "displayId=" + displayId);
        }
        synchronized (this.mLock) {
            android.graphics.Region region = android.graphics.Region.obtain();
            if (!hasRightsToCurrentUserLocked()) {
                return region;
            }
            com.android.server.accessibility.magnification.MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                magnificationProcessor.getFullscreenMagnificationRegion(displayId, region, this.mSecurityPolicy.canControlMagnification(this));
                return region;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public android.graphics.Region getCurrentMagnificationRegion(int displayId) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getCurrentMagnificationRegion", "displayId=" + displayId);
        }
        synchronized (this.mLock) {
            android.graphics.Region region = android.graphics.Region.obtain();
            if (!hasRightsToCurrentUserLocked()) {
                return region;
            }
            com.android.server.accessibility.magnification.MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                magnificationProcessor.getCurrentMagnificationRegion(displayId, region, this.mSecurityPolicy.canControlMagnification(this));
                return region;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public float getMagnificationCenterX(int displayId) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationCenterX", "displayId=" + displayId);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return 0.0f;
            }
            com.android.server.accessibility.magnification.MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return magnificationProcessor.getCenterX(displayId, this.mSecurityPolicy.canControlMagnification(this));
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public float getMagnificationCenterY(int displayId) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationCenterY", "displayId=" + displayId);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return 0.0f;
            }
            com.android.server.accessibility.magnification.MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return magnificationProcessor.getCenterY(displayId, this.mSecurityPolicy.canControlMagnification(this));
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0056  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean resetMagnification(int r6, boolean r7) {
        /*
            r5 = this;
            boolean r0 = r5.svcConnTracingEnabled()
            if (r0 == 0) goto L29
            java.lang.String r0 = "resetMagnification"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "displayId="
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r6)
            java.lang.String r2 = ";animate="
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r7)
            java.lang.String r1 = r1.toString()
            r5.logTraceSvcConn(r0, r1)
        L29:
            java.lang.Object r0 = r5.mLock
            monitor-enter(r0)
            boolean r1 = r5.hasRightsToCurrentUserLocked()     // Catch: java.lang.Throwable -> L60
            r2 = 0
            if (r1 != 0) goto L35
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            return r2
        L35:
            com.android.server.accessibility.AccessibilitySecurityPolicy r1 = r5.mSecurityPolicy     // Catch: java.lang.Throwable -> L60
            boolean r1 = r1.canControlMagnification(r5)     // Catch: java.lang.Throwable -> L60
            if (r1 != 0) goto L3f
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            return r2
        L3f:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            long r0 = android.os.Binder.clearCallingIdentity()
            com.android.server.accessibility.AbstractAccessibilityServiceConnection$SystemSupport r3 = r5.mSystemSupport     // Catch: java.lang.Throwable -> L5b
            com.android.server.accessibility.magnification.MagnificationProcessor r3 = r3.getMagnificationProcessor()     // Catch: java.lang.Throwable -> L5b
            boolean r4 = r3.resetFullscreenMagnification(r6, r7)     // Catch: java.lang.Throwable -> L5b
            if (r4 != 0) goto L56
            boolean r4 = r3.isMagnifying(r6)     // Catch: java.lang.Throwable -> L5b
            if (r4 != 0) goto L57
        L56:
            r2 = 1
        L57:
            android.os.Binder.restoreCallingIdentity(r0)
            return r2
        L5b:
            r2 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            throw r2
        L60:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.AbstractAccessibilityServiceConnection.resetMagnification(int, boolean):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0056  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean resetCurrentMagnification(int r6, boolean r7) {
        /*
            r5 = this;
            boolean r0 = r5.svcConnTracingEnabled()
            if (r0 == 0) goto L29
            java.lang.String r0 = "resetCurrentMagnification"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "displayId="
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r6)
            java.lang.String r2 = ";animate="
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r7)
            java.lang.String r1 = r1.toString()
            r5.logTraceSvcConn(r0, r1)
        L29:
            java.lang.Object r0 = r5.mLock
            monitor-enter(r0)
            boolean r1 = r5.hasRightsToCurrentUserLocked()     // Catch: java.lang.Throwable -> L60
            r2 = 0
            if (r1 != 0) goto L35
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            return r2
        L35:
            com.android.server.accessibility.AccessibilitySecurityPolicy r1 = r5.mSecurityPolicy     // Catch: java.lang.Throwable -> L60
            boolean r1 = r1.canControlMagnification(r5)     // Catch: java.lang.Throwable -> L60
            if (r1 != 0) goto L3f
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            return r2
        L3f:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            long r0 = android.os.Binder.clearCallingIdentity()
            com.android.server.accessibility.AbstractAccessibilityServiceConnection$SystemSupport r3 = r5.mSystemSupport     // Catch: java.lang.Throwable -> L5b
            com.android.server.accessibility.magnification.MagnificationProcessor r3 = r3.getMagnificationProcessor()     // Catch: java.lang.Throwable -> L5b
            boolean r4 = r3.resetCurrentMagnification(r6, r7)     // Catch: java.lang.Throwable -> L5b
            if (r4 != 0) goto L56
            boolean r4 = r3.isMagnifying(r6)     // Catch: java.lang.Throwable -> L5b
            if (r4 != 0) goto L57
        L56:
            r2 = 1
        L57:
            android.os.Binder.restoreCallingIdentity(r0)
            return r2
        L5b:
            r2 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            throw r2
        L60:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.AbstractAccessibilityServiceConnection.resetCurrentMagnification(int, boolean):boolean");
    }

    public boolean setMagnificationConfig(int displayId, android.accessibilityservice.MagnificationConfig config, boolean animate) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setMagnificationSpec", "displayId=" + displayId + ", config=" + config.toString());
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            if (!this.mSecurityPolicy.canControlMagnification(this)) {
                return false;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.accessibility.magnification.MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
                return magnificationProcessor.setMagnificationConfig(displayId, config, animate, this.mId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public void setMagnificationCallbackEnabled(int displayId, boolean enabled) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setMagnificationCallbackEnabled", "displayId=" + displayId + ";enabled=" + enabled);
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mInvocationHandler.setMagnificationCallbackEnabled(displayId, enabled);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean isMagnificationCallbackEnabled(int displayId) {
        return this.mInvocationHandler.isMagnificationCallbackEnabled(displayId);
    }

    public void setSoftKeyboardCallbackEnabled(boolean enabled) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setSoftKeyboardCallbackEnabled", "enabled=" + enabled);
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mInvocationHandler.setSoftKeyboardCallbackEnabled(enabled);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void takeScreenshotOfWindow(int accessibilityWindowId, int interactionId, android.window.ScreenCapture.ScreenCaptureListener listener, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback) throws android.os.RemoteException {
        long currentTimestamp = android.os.SystemClock.uptimeMillis();
        if (currentTimestamp - this.mRequestTakeScreenshotOfWindowTimestampMs.get(accessibilityWindowId, 0L).longValue() <= 333) {
            callback.sendTakeScreenshotOfWindowError(3, interactionId);
            return;
        }
        this.mRequestTakeScreenshotOfWindowTimestampMs.put(accessibilityWindowId, java.lang.Long.valueOf(currentTimestamp));
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                callback.sendTakeScreenshotOfWindowError(1, interactionId);
                return;
            }
            if (!this.mSecurityPolicy.canTakeScreenshotLocked(this)) {
                callback.sendTakeScreenshotOfWindowError(2, interactionId);
                return;
            }
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                callback.sendTakeScreenshotOfWindowError(2, interactionId);
                return;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdLocked(accessibilityWindowId));
                if (connection == null) {
                    callback.sendTakeScreenshotOfWindowError(5, interactionId);
                } else {
                    connection.getRemote().takeScreenshotOfWindow(interactionId, listener, callback);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public void takeScreenshot(final int displayId, final android.os.RemoteCallback callback) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn(TAKE_SCREENSHOT, "displayId=" + displayId + ";callback=" + callback);
        }
        long currentTimestamp = android.os.SystemClock.uptimeMillis();
        if (this.mRequestTakeScreenshotTimestampMs != 0 && currentTimestamp - this.mRequestTakeScreenshotTimestampMs <= 333) {
            sendScreenshotFailure(3, callback);
            return;
        }
        this.mRequestTakeScreenshotTimestampMs = currentTimestamp;
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                sendScreenshotFailure(1, callback);
                return;
            }
            if (!this.mSecurityPolicy.canTakeScreenshotLocked(this)) {
                throw new java.lang.SecurityException("Services don't have the capability of taking the screenshot.");
            }
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                sendScreenshotFailure(2, callback);
                return;
            }
            android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService("display");
            android.view.Display display = displayManager.getDisplay(displayId);
            if (display == null || (display.getType() == 5 && (display.getFlags() & 4) != 0)) {
                sendScreenshotFailure(4, callback);
                return;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.window.flags.Flags.deleteCaptureDisplay()) {
                    try {
                        android.window.ScreenCapture.ScreenCaptureListener screenCaptureListener = new android.window.ScreenCapture.ScreenCaptureListener(new java.util.function.ObjIntConsumer() { // from class: com.android.server.accessibility.AbstractAccessibilityServiceConnection$$ExternalSyntheticLambda1
                            @Override // java.util.function.ObjIntConsumer
                            public final void accept(java.lang.Object obj, int i) {
                                this.f$0.lambda$takeScreenshot$0(callback, (android.window.ScreenCapture.ScreenshotHardwareBuffer) obj, i);
                            }
                        });
                        this.mWindowManagerService.captureDisplay(displayId, null, screenCaptureListener);
                    } catch (java.lang.Exception e) {
                        sendScreenshotFailure(4, callback);
                    }
                } else {
                    try {
                        this.mMainHandler.post(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AbstractAccessibilityServiceConnection$$ExternalSyntheticLambda2
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                this.f$0.lambda$takeScreenshot$1(displayId, callback, obj);
                            }
                        }, (java.lang.Object) null).recycleOnUse());
                    } finally {
                    }
                }
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$takeScreenshot$0(android.os.RemoteCallback callback, android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer, int result) {
        if (screenshotBuffer != null && result == 0) {
            sendScreenshotSuccess(screenshotBuffer, callback);
        } else {
            sendScreenshotFailure(4, callback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$takeScreenshot$1(int displayId, android.os.RemoteCallback callback, java.lang.Object nonArg) {
        android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer = ((android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class)).userScreenshot(displayId);
        if (screenshotBuffer != null) {
            sendScreenshotSuccess(screenshotBuffer, callback);
        } else {
            sendScreenshotFailure(4, callback);
        }
    }

    private void sendScreenshotSuccess(final android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer, final android.os.RemoteCallback callback) {
        if (com.android.window.flags.Flags.deleteCaptureDisplay()) {
            this.mMainHandler.post(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AbstractAccessibilityServiceConnection$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.lambda$sendScreenshotSuccess$2(screenshotBuffer, callback, obj);
                }
            }, (java.lang.Object) null).recycleOnUse());
            return;
        }
        android.hardware.HardwareBuffer hardwareBuffer = screenshotBuffer.getHardwareBuffer();
        android.graphics.ParcelableColorSpace colorSpace = new android.graphics.ParcelableColorSpace(screenshotBuffer.getColorSpace());
        android.os.Bundle payload = new android.os.Bundle();
        payload.putInt("screenshot_status", 0);
        payload.putParcelable("screenshot_hardwareBuffer", hardwareBuffer);
        payload.putParcelable("screenshot_colorSpace", colorSpace);
        payload.putLong("screenshot_timestamp", android.os.SystemClock.uptimeMillis());
        callback.sendResult(payload);
        hardwareBuffer.close();
    }

    static /* synthetic */ void lambda$sendScreenshotSuccess$2(android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer, android.os.RemoteCallback callback, java.lang.Object nonArg) {
        android.hardware.HardwareBuffer hardwareBuffer = screenshotBuffer.getHardwareBuffer();
        android.graphics.ParcelableColorSpace colorSpace = new android.graphics.ParcelableColorSpace(screenshotBuffer.getColorSpace());
        android.os.Bundle payload = new android.os.Bundle();
        payload.putInt("screenshot_status", 0);
        payload.putParcelable("screenshot_hardwareBuffer", hardwareBuffer);
        payload.putParcelable("screenshot_colorSpace", colorSpace);
        payload.putLong("screenshot_timestamp", android.os.SystemClock.uptimeMillis());
        callback.sendResult(payload);
        hardwareBuffer.close();
    }

    private void sendScreenshotFailure(final int errorCode, final android.os.RemoteCallback callback) {
        this.mMainHandler.post(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AbstractAccessibilityServiceConnection$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.accessibility.AbstractAccessibilityServiceConnection.lambda$sendScreenshotFailure$3(errorCode, callback, obj);
            }
        }, (java.lang.Object) null).recycleOnUse());
    }

    static /* synthetic */ void lambda$sendScreenshotFailure$3(int errorCode, android.os.RemoteCallback callback, java.lang.Object nonArg) {
        android.os.Bundle payload = new android.os.Bundle();
        payload.putInt("screenshot_status", errorCode);
        callback.sendResult(payload);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, LOG_TAG, pw)) {
            synchronized (this.mLock) {
                pw.append((java.lang.CharSequence) ("Service[label=" + ((java.lang.Object) this.mAccessibilityServiceInfo.getResolveInfo().loadLabel(this.mContext.getPackageManager()))));
                pw.append((java.lang.CharSequence) (", feedbackType" + android.accessibilityservice.AccessibilityServiceInfo.feedbackTypeToString(this.mFeedbackType)));
                pw.append((java.lang.CharSequence) (", capabilities=" + this.mAccessibilityServiceInfo.getCapabilities()));
                pw.append((java.lang.CharSequence) (", eventTypes=" + android.view.accessibility.AccessibilityEvent.eventTypeToString(this.mEventTypes)));
                pw.append((java.lang.CharSequence) (", notificationTimeout=" + this.mNotificationTimeout));
                pw.append((java.lang.CharSequence) (", requestA11yBtn=" + this.mRequestAccessibilityButton));
                pw.append("]");
            }
        }
    }

    void addWindowTokensForAllDisplays() {
        android.view.Display[] displays = this.mDisplayManager.getDisplays();
        for (android.view.Display display : displays) {
            int displayId = display.getDisplayId();
            addWindowTokenForDisplay(displayId);
        }
    }

    void addWindowTokenForDisplay(int displayId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.os.IBinder overlayWindowToken = new android.os.Binder();
            if (wmTracingEnabled()) {
                logTraceWM("addWindowToken", overlayWindowToken + ";TYPE_ACCESSIBILITY_OVERLAY;" + displayId + ";null");
            }
            this.mWindowManagerService.addWindowToken(overlayWindowToken, 2032, displayId, null);
            synchronized (this.mLock) {
                this.mOverlayWindowTokens.put(displayId, overlayWindowToken);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onRemoved() {
        android.view.Display[] displays = this.mDisplayManager.getDisplays();
        for (android.view.Display display : displays) {
            int displayId = display.getDisplayId();
            onDisplayRemoved(displayId);
        }
        detachAllOverlays();
    }

    public void onDisplayRemoved(int displayId) {
        long identity = android.os.Binder.clearCallingIdentity();
        if (wmTracingEnabled()) {
            logTraceWM("addWindowToken", this.mOverlayWindowTokens.get(displayId) + ";true;" + displayId);
        }
        try {
            this.mWindowManagerService.removeWindowToken(this.mOverlayWindowTokens.get(displayId), true, displayId);
            synchronized (this.mLock) {
                this.mOverlayWindowTokens.remove(displayId);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public android.os.IBinder getOverlayWindowToken(int displayId) {
        android.os.IBinder iBinder;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getOverlayWindowToken", "displayId=" + displayId);
        }
        synchronized (this.mLock) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                iBinder = this.mOverlayWindowTokens.get(displayId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        return iBinder;
    }

    public int getWindowIdForLeashToken(android.os.IBinder token) {
        int windowIdLocked;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getWindowIdForLeashToken", "token=" + token);
        }
        synchronized (this.mLock) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                windowIdLocked = this.mA11yWindowManager.getWindowIdLocked(token);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        return windowIdLocked;
    }

    public void resetLocked() {
        this.mAccessibilityServiceInfo.resetDynamicallyConfigurableProperties();
        this.mSystemSupport.getKeyEventDispatcher().flush(this);
        try {
            if (this.mServiceInterface != null) {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("init", "null, " + this.mId + ", null");
                }
                this.mServiceInterface.init((android.accessibilityservice.IAccessibilityServiceConnection) null, this.mId, (android.os.IBinder) null);
            }
        } catch (android.os.RemoteException e) {
        }
        if (this.mService != null) {
            try {
                this.mService.unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e2) {
                android.util.Slog.e(LOG_TAG, "Failed unregistering death link");
            }
            this.mService = null;
        }
        this.mServiceInterface = null;
        this.mReceivedAccessibilityButtonCallbackSinceBind = false;
    }

    public boolean isConnectedLocked() {
        return this.mService != null;
    }

    public void notifyAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        android.os.Message message;
        synchronized (this.mLock) {
            int eventType = event.getEventType();
            boolean serviceWantsEvent = wantsEventLocked(event);
            int i = 1;
            boolean requiredForCacheConsistency = this.mUsesAccessibilityCache && (4307005 & eventType) != 0;
            if (serviceWantsEvent || requiredForCacheConsistency) {
                if (this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                    android.view.accessibility.AccessibilityEvent newEvent = android.view.accessibility.AccessibilityEvent.obtain(event);
                    if (this.mNotificationTimeout > 0 && eventType != 2048) {
                        android.view.accessibility.AccessibilityEvent oldEvent = this.mPendingEvents.get(eventType);
                        this.mPendingEvents.put(eventType, newEvent);
                        if (oldEvent != null) {
                            this.mEventDispatchHandler.removeMessages(eventType);
                            oldEvent.recycle();
                        }
                        message = this.mEventDispatchHandler.obtainMessage(eventType);
                    } else {
                        message = this.mEventDispatchHandler.obtainMessage(eventType, newEvent);
                    }
                    if (!serviceWantsEvent) {
                        i = 0;
                    }
                    message.arg1 = i;
                    this.mEventDispatchHandler.sendMessageDelayed(message, this.mNotificationTimeout);
                }
            }
        }
    }

    private boolean wantsEventLocked(android.view.accessibility.AccessibilityEvent event) {
        if (!canReceiveEventsLocked()) {
            return false;
        }
        boolean includeNotImportantViews = (this.mFetchFlags & 128) != 0;
        if (event.getWindowId() != -1 && !event.isImportantForAccessibility() && !includeNotImportantViews) {
            return false;
        }
        if (event.isAccessibilityDataSensitive() && (this.mFetchFlags & 512) == 0) {
            return false;
        }
        int eventType = event.getEventType();
        if ((this.mEventTypes & eventType) != eventType) {
            return false;
        }
        java.util.Set<java.lang.String> packageNames = this.mPackageNames;
        java.lang.String packageName = event.getPackageName() != null ? event.getPackageName().toString() : null;
        return packageNames.isEmpty() || packageNames.contains(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAccessibilityEventInternal(int eventType, android.view.accessibility.AccessibilityEvent event, boolean serviceWantsEvent) {
        synchronized (this.mLock) {
            android.accessibilityservice.IAccessibilityServiceClient listener = this.mServiceInterface;
            if (listener == null) {
                return;
            }
            if (event == null) {
                event = this.mPendingEvents.get(eventType);
                if (event == null) {
                    return;
                } else {
                    this.mPendingEvents.remove(eventType);
                }
            }
            if (this.mSecurityPolicy.canRetrieveWindowContentLocked(this)) {
                event.setConnectionId(this.mId);
            } else {
                event.setSource(null);
            }
            event.setSealed(true);
            try {
                try {
                    if (svcClientTracingEnabled()) {
                        logTraceSvcClient("onAccessibilityEvent", event + ";" + serviceWantsEvent);
                    }
                    listener.onAccessibilityEvent(event, serviceWantsEvent);
                } catch (android.os.RemoteException re) {
                    android.util.Slog.e(LOG_TAG, "Error during sending " + event + " to " + listener, re);
                }
            } finally {
                event.recycle();
            }
        }
    }

    public void notifyGesture(android.accessibilityservice.AccessibilityGestureEvent gestureEvent) {
        if (android.view.accessibility.Flags.copyEventsForGestureDetection()) {
            this.mInvocationHandler.obtainMessage(1, gestureEvent.copyForAsync()).sendToTarget();
        } else {
            this.mInvocationHandler.obtainMessage(1, gestureEvent).sendToTarget();
        }
    }

    public void notifySystemActionsChangedLocked() {
        this.mInvocationHandler.sendEmptyMessage(9);
    }

    public void notifyClearAccessibilityNodeInfoCache() {
        this.mInvocationHandler.sendEmptyMessage(2);
    }

    public void notifyMagnificationChangedLocked(int displayId, android.graphics.Region region, android.accessibilityservice.MagnificationConfig config) {
        this.mInvocationHandler.notifyMagnificationChangedLocked(displayId, region, config);
    }

    public void notifySoftKeyboardShowModeChangedLocked(int showState) {
        this.mInvocationHandler.notifySoftKeyboardShowModeChangedLocked(showState);
    }

    public void notifyAccessibilityButtonClickedLocked(int displayId) {
        this.mInvocationHandler.notifyAccessibilityButtonClickedLocked(displayId);
    }

    public void notifyAccessibilityButtonAvailabilityChangedLocked(boolean available) {
        this.mInvocationHandler.notifyAccessibilityButtonAvailabilityChangedLocked(available);
    }

    public void createImeSessionLocked() {
        this.mInvocationHandler.createImeSessionLocked();
    }

    public void setImeSessionEnabledLocked(com.android.internal.inputmethod.IAccessibilityInputMethodSession session, boolean enabled) {
        this.mInvocationHandler.setImeSessionEnabledLocked(session, enabled);
    }

    public void bindInputLocked() {
        this.mInvocationHandler.bindInputLocked();
    }

    public void unbindInputLocked() {
        this.mInvocationHandler.unbindInputLocked();
    }

    public void startInputLocked(com.android.internal.inputmethod.IRemoteAccessibilityInputConnection connection, android.view.inputmethod.EditorInfo editorInfo, boolean restarting) {
        this.mInvocationHandler.startInputLocked(connection, editorInfo, restarting);
    }

    private android.util.Pair<float[], android.view.MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(int resolvedWindowId) {
        return this.mSystemSupport.getWindowTransformationMatrixAndMagnificationSpec(resolvedWindowId);
    }

    public boolean wantsGenericMotionEvent(android.view.MotionEvent event) {
        int eventSourceWithoutClass = event.getSource() & (-256);
        return (this.mGenericMotionEventSources & eventSourceWithoutClass) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMagnificationChangedInternal(int displayId, android.graphics.Region region, android.accessibilityservice.MagnificationConfig config) {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onMagnificationChanged", displayId + ", " + region + ", " + config.toString());
                }
                listener.onMagnificationChanged(displayId, region, config);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error sending magnification changes to " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySoftKeyboardShowModeChangedInternal(int showState) {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onSoftKeyboardShowModeChanged", java.lang.String.valueOf(showState));
                }
                listener.onSoftKeyboardShowModeChanged(showState);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error sending soft keyboard show mode changes to " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAccessibilityButtonClickedInternal(int displayId) {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onAccessibilityButtonClicked", java.lang.String.valueOf(displayId));
                }
                listener.onAccessibilityButtonClicked(displayId);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error sending accessibility button click to " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAccessibilityButtonAvailabilityChangedInternal(boolean available) {
        if (this.mReceivedAccessibilityButtonCallbackSinceBind && this.mLastAccessibilityButtonCallbackState == available) {
            return;
        }
        this.mReceivedAccessibilityButtonCallbackSinceBind = true;
        this.mLastAccessibilityButtonCallbackState = available;
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onAccessibilityButtonAvailabilityChanged", java.lang.String.valueOf(available));
                }
                listener.onAccessibilityButtonAvailabilityChanged(available);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error sending accessibility button availability change to " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyGestureInternal(android.accessibilityservice.AccessibilityGestureEvent gestureInfo) {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onGesture", gestureInfo.toString());
                }
                listener.onGesture(gestureInfo);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error during sending gesture " + gestureInfo + " to " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySystemActionsChangedInternal() {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onSystemActionsChanged", "");
                }
                listener.onSystemActionsChanged();
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error sending system actions change to " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyClearAccessibilityCacheInternal() {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("clearAccessibilityCache", "");
                }
                listener.clearAccessibilityCache();
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error during requesting accessibility info cache to be cleared.", re);
            }
        }
    }

    protected void createImeSessionInternal() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImeSessionEnabledInternal(com.android.internal.inputmethod.IAccessibilityInputMethodSession session, boolean enabled) {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null && session != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("createImeSession", "");
                }
                listener.setImeSessionEnabled(session, enabled);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error requesting IME session from " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindInputInternal() {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("bindInput", "");
                }
                listener.bindInput();
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error binding input to " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindInputInternal() {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("unbindInput", "");
                }
                listener.unbindInput();
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error unbinding input to " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startInputInternal(com.android.internal.inputmethod.IRemoteAccessibilityInputConnection connection, android.view.inputmethod.EditorInfo editorInfo, boolean restarting) {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("startInput", "editorInfo=" + editorInfo + " restarting=" + restarting);
                }
                listener.startInput(connection, editorInfo, restarting);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error starting input to " + this.mService, re);
            }
        }
    }

    protected android.accessibilityservice.IAccessibilityServiceClient getServiceInterfaceSafely() {
        android.accessibilityservice.IAccessibilityServiceClient iAccessibilityServiceClient;
        synchronized (this.mLock) {
            iAccessibilityServiceClient = this.mServiceInterface;
        }
        return iAccessibilityServiceClient;
    }

    private int resolveAccessibilityWindowIdLocked(int accessibilityWindowId) {
        if (accessibilityWindowId == Integer.MAX_VALUE) {
            int focusedWindowId = this.mA11yWindowManager.getActiveWindowId(this.mSystemSupport.getCurrentUserIdLocked());
            if (!this.mA11yWindowManager.windowIdBelongsToDisplayType(focusedWindowId, this.mDisplayTypes)) {
                return -1;
            }
            return focusedWindowId;
        }
        return accessibilityWindowId;
    }

    int resolveAccessibilityWindowIdForFindFocusLocked(int windowId, int focusType) {
        if (windowId == -2) {
            int focusedWindowId = this.mA11yWindowManager.getFocusedWindowId(focusType);
            if (!this.mA11yWindowManager.windowIdBelongsToDisplayType(focusedWindowId, this.mDisplayTypes)) {
                return -1;
            }
            return focusedWindowId;
        }
        return windowId;
    }

    private void ensureWindowsAvailableTimedLocked(int displayId) {
        if (displayId == -1 || this.mA11yWindowManager.getWindowListLocked(displayId) != null) {
            return;
        }
        if (!this.mA11yWindowManager.isTrackingWindowsLocked(displayId)) {
            this.mSystemSupport.onClientChangeLocked(false);
        }
        if (!this.mA11yWindowManager.isTrackingWindowsLocked(displayId)) {
            return;
        }
        long startMillis = android.os.SystemClock.uptimeMillis();
        while (this.mA11yWindowManager.getWindowListLocked(displayId) == null) {
            long elapsedMillis = android.os.SystemClock.uptimeMillis() - startMillis;
            long remainMillis = 5000 - elapsedMillis;
            if (remainMillis <= 0) {
                return;
            } else {
                try {
                    this.mLock.wait(remainMillis);
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0058  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean performAccessibilityActionInternal(int r24, int r25, long r26, int r28, android.os.Bundle r29, int r30, android.view.accessibility.IAccessibilityInteractionConnectionCallback r31, int r32, long r33) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 372
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.AbstractAccessibilityServiceConnection.performAccessibilityActionInternal(int, int, long, int, android.os.Bundle, int, android.view.accessibility.IAccessibilityInteractionConnectionCallback, int, long):boolean");
    }

    private android.view.accessibility.IAccessibilityInteractionConnectionCallback replaceCallbackIfNeeded(android.view.accessibility.IAccessibilityInteractionConnectionCallback originalCallback, int resolvedWindowId, int interactionId, int interrogatingPid, long interrogatingTid) throws java.lang.Throwable {
        com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection pipActionReplacingConnection = this.mA11yWindowManager.getPictureInPictureActionReplacingConnection();
        synchronized (this.mLock) {
            try {
                try {
                    android.view.accessibility.AccessibilityWindowInfo windowInfo = this.mA11yWindowManager.findA11yWindowInfoByIdLocked(resolvedWindowId);
                    if (windowInfo != null && windowInfo.isInPictureInPictureMode() && pipActionReplacingConnection != null) {
                        return new com.android.server.accessibility.ActionReplacingCallback(originalCallback, pipActionReplacingConnection.getRemote(), interactionId, interrogatingPid, interrogatingTid);
                    }
                    return originalCallback;
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private java.util.List<android.view.accessibility.AccessibilityWindowInfo> getWindowsByDisplayLocked(int displayId) {
        java.util.List<android.view.accessibility.AccessibilityWindowInfo> internalWindowList = this.mA11yWindowManager.getWindowListLocked(displayId);
        if (internalWindowList == null) {
            return null;
        }
        java.util.List<android.view.accessibility.AccessibilityWindowInfo> returnedWindowList = new java.util.ArrayList<>();
        int windowCount = internalWindowList.size();
        for (int i = 0; i < windowCount; i++) {
            android.view.accessibility.AccessibilityWindowInfo window = internalWindowList.get(i);
            android.view.accessibility.AccessibilityWindowInfo windowClone = android.view.accessibility.AccessibilityWindowInfo.obtain(window);
            windowClone.setConnectionId(this.mId);
            returnedWindowList.add(windowClone);
        }
        return returnedWindowList;
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    private final class InvocationHandler extends android.os.Handler {
        private static final int MSG_BIND_INPUT = 12;
        public static final int MSG_CLEAR_ACCESSIBILITY_CACHE = 2;
        private static final int MSG_CREATE_IME_SESSION = 10;
        private static final int MSG_ON_ACCESSIBILITY_BUTTON_AVAILABILITY_CHANGED = 8;
        private static final int MSG_ON_ACCESSIBILITY_BUTTON_CLICKED = 7;
        public static final int MSG_ON_GESTURE = 1;
        private static final int MSG_ON_MAGNIFICATION_CHANGED = 5;
        private static final int MSG_ON_SOFT_KEYBOARD_STATE_CHANGED = 6;
        private static final int MSG_ON_SYSTEM_ACTIONS_CHANGED = 9;
        private static final int MSG_SET_IME_SESSION_ENABLED = 11;
        private static final int MSG_START_INPUT = 14;
        private static final int MSG_UNBIND_INPUT = 13;
        private boolean mIsSoftKeyboardCallbackEnabled;
        private final android.util.SparseArray<java.lang.Boolean> mMagnificationCallbackState;

        public InvocationHandler(android.os.Looper looper) {
            super(looper, null, true);
            this.mMagnificationCallbackState = new android.util.SparseArray<>(0);
            this.mIsSoftKeyboardCallbackEnabled = false;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            boolean restarting;
            int type = message.what;
            switch (type) {
                case 1:
                    java.lang.Object obj = message.obj;
                    if (obj instanceof android.accessibilityservice.AccessibilityGestureEvent) {
                        android.accessibilityservice.AccessibilityGestureEvent gesture = (android.accessibilityservice.AccessibilityGestureEvent) obj;
                        com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.notifyGestureInternal(gesture);
                        if (android.view.accessibility.Flags.copyEventsForGestureDetection()) {
                            gesture.recycle();
                            return;
                        }
                        return;
                    }
                    return;
                case 2:
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.notifyClearAccessibilityCacheInternal();
                    return;
                case 3:
                case 4:
                default:
                    throw new java.lang.IllegalArgumentException("Unknown message: " + type);
                case 5:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) message.obj;
                    android.graphics.Region region = (android.graphics.Region) args.arg1;
                    android.accessibilityservice.MagnificationConfig config = (android.accessibilityservice.MagnificationConfig) args.arg2;
                    int displayId = args.argi1;
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.notifyMagnificationChangedInternal(displayId, region, config);
                    args.recycle();
                    return;
                case 6:
                    int showState = message.arg1;
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.notifySoftKeyboardShowModeChangedInternal(showState);
                    return;
                case 7:
                    int displayId2 = message.arg1;
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.notifyAccessibilityButtonClickedInternal(displayId2);
                    return;
                case 8:
                    restarting = message.arg1 != 0;
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.notifyAccessibilityButtonAvailabilityChangedInternal(restarting);
                    return;
                case 9:
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.notifySystemActionsChangedInternal();
                    return;
                case 10:
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.createImeSessionInternal();
                    return;
                case 11:
                    restarting = message.arg1 != 0;
                    com.android.internal.inputmethod.IAccessibilityInputMethodSession session = (com.android.internal.inputmethod.IAccessibilityInputMethodSession) message.obj;
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.setImeSessionEnabledInternal(session, restarting);
                    return;
                case 12:
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.bindInputInternal();
                    return;
                case 13:
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.unbindInputInternal();
                    return;
                case 14:
                    restarting = message.arg1 != 0;
                    com.android.internal.os.SomeArgs args2 = (com.android.internal.os.SomeArgs) message.obj;
                    com.android.internal.inputmethod.IRemoteAccessibilityInputConnection connection = (com.android.internal.inputmethod.IRemoteAccessibilityInputConnection) args2.arg1;
                    android.view.inputmethod.EditorInfo editorInfo = (android.view.inputmethod.EditorInfo) args2.arg2;
                    com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.startInputInternal(connection, editorInfo, restarting);
                    args2.recycle();
                    return;
            }
        }

        public void notifyMagnificationChangedLocked(int displayId, android.graphics.Region region, android.accessibilityservice.MagnificationConfig config) {
            synchronized (com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.mLock) {
                if (this.mMagnificationCallbackState.get(displayId) == null) {
                    return;
                }
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.arg1 = region;
                args.arg2 = config;
                args.argi1 = displayId;
                android.os.Message msg = obtainMessage(5, args);
                msg.sendToTarget();
            }
        }

        public void setMagnificationCallbackEnabled(int displayId, boolean enabled) {
            synchronized (com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.mLock) {
                if (enabled) {
                    this.mMagnificationCallbackState.put(displayId, true);
                } else {
                    this.mMagnificationCallbackState.remove(displayId);
                }
            }
        }

        public boolean isMagnificationCallbackEnabled(int displayId) {
            boolean z;
            synchronized (com.android.server.accessibility.AbstractAccessibilityServiceConnection.this.mLock) {
                z = this.mMagnificationCallbackState.get(displayId) != null;
            }
            return z;
        }

        public void notifySoftKeyboardShowModeChangedLocked(int showState) {
            if (!this.mIsSoftKeyboardCallbackEnabled) {
                return;
            }
            android.os.Message msg = obtainMessage(6, showState, 0);
            msg.sendToTarget();
        }

        public void setSoftKeyboardCallbackEnabled(boolean enabled) {
            this.mIsSoftKeyboardCallbackEnabled = enabled;
        }

        public void notifyAccessibilityButtonClickedLocked(int displayId) {
            android.os.Message msg = obtainMessage(7, displayId, 0);
            msg.sendToTarget();
        }

        public void notifyAccessibilityButtonAvailabilityChangedLocked(boolean z) {
            obtainMessage(8, z ? 1 : 0, 0).sendToTarget();
        }

        public void createImeSessionLocked() {
            android.os.Message msg = obtainMessage(10);
            msg.sendToTarget();
        }

        public void setImeSessionEnabledLocked(com.android.internal.inputmethod.IAccessibilityInputMethodSession iAccessibilityInputMethodSession, boolean z) {
            obtainMessage(11, z ? 1 : 0, 0, iAccessibilityInputMethodSession).sendToTarget();
        }

        public void bindInputLocked() {
            android.os.Message msg = obtainMessage(12);
            msg.sendToTarget();
        }

        public void unbindInputLocked() {
            android.os.Message msg = obtainMessage(13);
            msg.sendToTarget();
        }

        public void startInputLocked(com.android.internal.inputmethod.IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, android.view.inputmethod.EditorInfo editorInfo, boolean z) {
            com.android.internal.os.SomeArgs someArgsObtain = com.android.internal.os.SomeArgs.obtain();
            someArgsObtain.arg1 = iRemoteAccessibilityInputConnection;
            someArgsObtain.arg2 = editorInfo;
            obtainMessage(14, z ? 1 : 0, 0, someArgsObtain).sendToTarget();
        }
    }

    public boolean isServiceHandlesDoubleTapEnabled() {
        return this.mServiceHandlesDoubleTap;
    }

    public boolean isMultiFingerGesturesEnabled() {
        return this.mRequestMultiFingerGestures;
    }

    public boolean isTwoFingerPassthroughEnabled() {
        return this.mRequestTwoFingerPassthrough;
    }

    public boolean isSendMotionEventsEnabled() {
        return this.mSendMotionEvents;
    }

    public void setGestureDetectionPassthroughRegion(int displayId, android.graphics.Region region) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setGestureDetectionPassthroughRegion", "displayId=" + displayId + ";region=" + region);
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemSupport.setGestureDetectionPassthroughRegion(displayId, region);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void setTouchExplorationPassthroughRegion(int displayId, android.graphics.Region region) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setTouchExplorationPassthroughRegion", "displayId=" + displayId + ";region=" + region);
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemSupport.setTouchExplorationPassthroughRegion(displayId, region);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void setFocusAppearance(int strokeWidth, int color) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setFocusAppearance", "strokeWidth=" + strokeWidth + ";color=" + color);
        }
    }

    public void setCacheEnabled(boolean enabled) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setCacheEnabled", "enabled=" + enabled);
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                this.mUsesAccessibilityCache = enabled;
                this.mSystemSupport.onClientChangeLocked(true);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void logTrace(long timestamp, java.lang.String where, long loggingTypes, java.lang.String callingParams, int processId, long threadId, int callingUid, android.os.Bundle callingStack) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mTrace.isA11yTracingEnabledForTypes(loggingTypes)) {
                java.util.ArrayList<java.lang.StackTraceElement> list = (java.util.ArrayList) callingStack.getSerializable("call_stack", java.util.ArrayList.class);
                java.util.HashSet<java.lang.String> ignoreList = (java.util.HashSet) callingStack.getSerializable("ignore_call_stack", java.util.HashSet.class);
                this.mTrace.logTrace(timestamp, where, loggingTypes, callingParams, processId, threadId, callingUid, (java.lang.StackTraceElement[]) list.toArray(new java.lang.StackTraceElement[list.size()]), ignoreList);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    protected boolean svcClientTracingEnabled() {
        return this.mTrace.isA11yTracingEnabledForTypes(2L);
    }

    protected void logTraceSvcClient(java.lang.String methodName, java.lang.String params) {
        this.mTrace.logTrace("AbstractAccessibilityServiceConnection.IAccessibilityServiceClient." + methodName, 2L, params);
    }

    protected boolean svcConnTracingEnabled() {
        return this.mTrace.isA11yTracingEnabledForTypes(1L);
    }

    protected void logTraceSvcConn(java.lang.String methodName, java.lang.String params) {
        this.mTrace.logTrace("AbstractAccessibilityServiceConnection.IAccessibilityServiceConnection." + methodName, 1L, params);
    }

    protected boolean intConnTracingEnabled() {
        return this.mTrace.isA11yTracingEnabledForTypes(16L);
    }

    protected void logTraceIntConn(java.lang.String methodName, java.lang.String params) {
        this.mTrace.logTrace("AbstractAccessibilityServiceConnection." + methodName, 16L, params);
    }

    protected boolean wmTracingEnabled() {
        return this.mTrace.isA11yTracingEnabledForTypes(512L);
    }

    protected void logTraceWM(java.lang.String methodName, java.lang.String params) {
        this.mTrace.logTrace("WindowManagerInternal." + methodName, 512L, params);
    }

    public void setServiceDetectsGesturesEnabled(int displayId, boolean mode) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mServiceDetectsGestures.put(displayId, java.lang.Boolean.valueOf(mode));
            this.mSystemSupport.setServiceDetectsGesturesEnabled(displayId, mode);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean isServiceDetectsGesturesEnabled(int displayId) {
        if (this.mServiceDetectsGestures.contains(displayId)) {
            return this.mServiceDetectsGestures.get(displayId).booleanValue();
        }
        return false;
    }

    public void requestTouchExploration(int displayId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemSupport.requestTouchExploration(displayId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void requestDragging(int displayId, int pointerId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemSupport.requestDragging(displayId, pointerId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void requestDelegating(int displayId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemSupport.requestDelegating(displayId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onDoubleTap(int displayId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemSupport.onDoubleTap(displayId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onDoubleTapAndHold(int displayId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemSupport.onDoubleTapAndHold(displayId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void setAnimationScale(float scale) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Global.putFloat(this.mContext.getContentResolver(), "window_animation_scale", scale);
            android.provider.Settings.Global.putFloat(this.mContext.getContentResolver(), "transition_animation_scale", scale);
            android.provider.Settings.Global.putFloat(this.mContext.getContentResolver(), "animator_duration_scale", scale);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void attachAccessibilityOverlayToDisplay(int interactionId, int displayId, android.view.SurfaceControl sc, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemSupport.attachAccessibilityOverlayToDisplay(interactionId, displayId, sc, callback);
            this.mOverlays.add(sc);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void attachAccessibilityOverlayToWindow(int interactionId, int accessibilityWindowId, android.view.SurfaceControl sc, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback) throws android.os.RemoteException {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
            t.setTrustedOverlay(sc, true).apply();
            t.close();
            synchronized (this.mLock) {
                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdLocked(accessibilityWindowId));
                if (connection == null) {
                    callback.sendAttachOverlayResult(2, interactionId);
                } else {
                    connection.getRemote().attachAccessibilityOverlayToWindow(sc, interactionId, callback);
                    this.mOverlays.add(sc);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    protected void detachAllOverlays() {
        android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
        for (android.view.SurfaceControl sc : this.mOverlays) {
            if (sc.isValid()) {
                t.reparent(sc, null);
            }
        }
        t.apply();
        t.close();
        this.mOverlays.clear();
    }

    public void connectBluetoothBrailleDisplay(java.lang.String bluetoothAddress, android.accessibilityservice.IBrailleDisplayController controller) {
        connectBluetoothBrailleDisplay_enforcePermission();
        throw new java.lang.UnsupportedOperationException();
    }

    public void connectUsbBrailleDisplay(android.hardware.usb.UsbDevice usbDevice, android.accessibilityservice.IBrailleDisplayController controller) {
        throw new java.lang.UnsupportedOperationException();
    }

    public void setTestBrailleDisplayData(java.util.List<android.os.Bundle> brailleDisplays) {
        setTestBrailleDisplayData_enforcePermission();
        throw new java.lang.UnsupportedOperationException();
    }
}
