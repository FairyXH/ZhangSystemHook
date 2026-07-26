package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SystemGesturesPointerEventListener implements android.view.WindowManagerPolicyConstants.PointerEventListener {
    private static final boolean DEBUG = false;
    static boolean DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final int MAX_FLING_TIME_MILLIS = 5000;
    private static final int MAX_TRACKED_POINTERS = 32;
    private static final int SWIPE_FROM_BOTTOM = 2;
    private static final int SWIPE_FROM_LEFT = 4;
    private static final int SWIPE_FROM_RIGHT = 3;
    private static final int SWIPE_FROM_TOP = 1;
    private static final int SWIPE_NONE = 0;
    private static final long SWIPE_TIMEOUT_MS = 500;
    private static final java.lang.String TAG = "SystemGestures";
    private static final int TRACKPAD_SWIPE_FROM_BOTTOM = 2;
    private static final int TRACKPAD_SWIPE_FROM_LEFT = 4;
    private static final int TRACKPAD_SWIPE_FROM_RIGHT = 3;
    private static final int TRACKPAD_SWIPE_FROM_TOP = 1;
    private static final int TRACKPAD_SWIPE_NONE = 0;
    private static final int UNTRACKED_POINTER = -1;
    private boolean mAllowSetTouchData;
    private final com.android.server.wm.SystemGesturesPointerEventListener.Callbacks mCallbacks;
    private final android.content.Context mContext;
    private boolean mDebugFireable;
    private int mDisplayCutoutTouchableRegionSize;
    private int mDownPointers;
    private android.view.GestureDetector mGestureDetector;
    private final android.os.Handler mHandler;
    private long mLastFlingTime;
    private boolean mMouseHoveringAtBottom;
    private boolean mMouseHoveringAtLeft;
    private boolean mMouseHoveringAtRight;
    private boolean mMouseHoveringAtTop;
    private int mSwipeDistanceThreshold;
    private boolean mSwipeFireable;
    int screenHeight;
    int screenWidth;
    private final android.graphics.Rect mSwipeStartThreshold = new android.graphics.Rect();
    private final int[] mDownPointerId = new int[32];
    private final float[] mDownX = new float[32];
    private final float[] mDownY = new float[32];
    private final long[] mDownTime = new long[32];
    com.android.server.wm.ISystemGesturesPointerEventListenerSocExt mSocExt = (com.android.server.wm.ISystemGesturesPointerEventListenerSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISystemGesturesPointerEventListenerSocExt.class).base(this).create();
    private android.os.ITheiaManagerExt mTheiaManagerExt = (android.os.ITheiaManagerExt) system.ext.loader.core.ExtLoader.type(android.os.ITheiaManagerExt.class).create();
    private com.android.server.wm.IWindowManagerServiceExt mWindowManagerServiceExt = (com.android.server.wm.IWindowManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowManagerServiceExt.class).create();
    com.android.server.wm.ISystemGesturesPointerEventListenerExt mSystemGesturesPointerEventListenerExt = (com.android.server.wm.ISystemGesturesPointerEventListenerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISystemGesturesPointerEventListenerExt.class).base(this).create();

    interface Callbacks {
        void onDebug();

        void onDown();

        void onFling(int i);

        void onHorizontalFling(int i);

        void onMouseHoverAtBottom();

        void onMouseHoverAtLeft();

        void onMouseHoverAtRight();

        void onMouseHoverAtTop();

        void onMouseLeaveFromBottom();

        void onMouseLeaveFromLeft();

        void onMouseLeaveFromRight();

        void onMouseLeaveFromTop();

        void onScroll(boolean z);

        void onSwipeFromBottom();

        void onSwipeFromLeft();

        void onSwipeFromRight();

        void onSwipeFromTop();

        void onUpOrCancel();

        void onVerticalFling(int i);
    }

    SystemGesturesPointerEventListener(android.content.Context context, android.os.Handler handler, com.android.server.wm.SystemGesturesPointerEventListener.Callbacks callbacks) {
        this.mAllowSetTouchData = false;
        this.mContext = (android.content.Context) checkNull("context", context);
        this.mHandler = handler;
        this.mCallbacks = (com.android.server.wm.SystemGesturesPointerEventListener.Callbacks) checkNull("callbacks", callbacks);
        this.mSystemGesturesPointerEventListenerExt.init(context);
        this.mAllowSetTouchData = allowShowBfs();
        onConfigurationChanged();
    }

    void onDisplayInfoChanged(android.view.DisplayInfo info) {
        this.screenWidth = info.logicalWidth;
        this.screenHeight = info.logicalHeight;
        onConfigurationChanged();
    }

    void onConfigurationChanged() {
        android.content.res.Resources r = this.mContext.getResources();
        int startThreshold = r.getDimensionPixelSize(android.R.dimen.primary_content_alpha_material_dark);
        this.mSwipeStartThreshold.set(startThreshold, startThreshold, startThreshold, startThreshold);
        if (this.mSystemGesturesPointerEventListenerExt != null) {
            this.mSystemGesturesPointerEventListenerExt.setSwipeStartThreshold(new android.graphics.Rect(this.mSwipeStartThreshold));
        }
        this.mSwipeDistanceThreshold = r.getDimensionPixelSize(android.R.dimen.primary_content_alpha_device_default);
        android.view.Display display = android.hardware.display.DisplayManagerGlobal.getInstance().getRealDisplay(0);
        android.view.DisplayCutout displayCutout = display.getCutout();
        if (displayCutout != null) {
            this.mDisplayCutoutTouchableRegionSize = r.getDimensionPixelSize(android.R.dimen.default_background_blur_radius);
            android.graphics.Rect[] bounds = displayCutout.getBoundingRectsAll();
            if (bounds[0] != null) {
                this.mSwipeStartThreshold.left = java.lang.Math.max(this.mSwipeStartThreshold.left, bounds[0].width() + this.mDisplayCutoutTouchableRegionSize);
            }
            if (bounds[1] != null) {
                this.mSwipeStartThreshold.top = java.lang.Math.max(this.mSwipeStartThreshold.top, bounds[1].height() + this.mDisplayCutoutTouchableRegionSize);
            }
            if (bounds[2] != null) {
                this.mSwipeStartThreshold.right = java.lang.Math.max(this.mSwipeStartThreshold.right, bounds[2].width() + this.mDisplayCutoutTouchableRegionSize);
            }
            if (bounds[3] != null) {
                this.mSwipeStartThreshold.bottom = java.lang.Math.max(this.mSwipeStartThreshold.bottom, bounds[3].height() + this.mDisplayCutoutTouchableRegionSize);
            }
        }
        this.mSystemGesturesPointerEventListenerExt.updateDefaultSwipeDistance();
    }

    private static <T> T checkNull(java.lang.String name, T arg) {
        if (arg == null) {
            throw new java.lang.IllegalArgumentException(name + " must not be null");
        }
        return arg;
    }

    public void systemReady() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.SystemGesturesPointerEventListener$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$systemReady$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$0() {
        int displayId = this.mContext.getDisplayId();
        android.view.DisplayInfo info = android.hardware.display.DisplayManagerGlobal.getInstance().getDisplayInfo(displayId);
        if (info == null) {
            android.util.Slog.w(TAG, "Cannot create GestureDetector, display removed:" + displayId);
        } else {
            this.mGestureDetector = new android.view.GestureDetector(this.mContext, new com.android.server.wm.SystemGesturesPointerEventListener.FlingGestureDetector(), this.mSystemGesturesPointerEventListenerExt.getOplusUiHandler(this.mHandler)) { // from class: com.android.server.wm.SystemGesturesPointerEventListener.1
            };
        }
    }

    private boolean allowShowBfs() {
        boolean getXmlAllow = this.mWindowManagerServiceExt.getBfsKeyAllowEvents(this.mContext);
        boolean getPropVersion = android.os.SystemProperties.get("ro.build.version.ota", "ota_version").contains("PRE");
        android.util.Slog.v(TAG, "getBfsKeyAllowEventsd = " + this.mWindowManagerServiceExt.getBfsKeyAllowEvents(this.mContext));
        return getPropVersion && getXmlAllow;
    }

    public void onPointerEvent(android.view.MotionEvent event) {
        if (this.mAllowSetTouchData) {
            this.mTheiaManagerExt.analysisInputEvent(this.mContext, event.toString(), "theia_bfs_data_catch");
        }
        if (this.mGestureDetector != null && event.isTouchEvent()) {
            this.mGestureDetector.onTouchEvent(event);
        }
        switch (event.getActionMasked()) {
            case 0:
                this.mSwipeFireable = true;
                if (this.mSystemGesturesPointerEventListenerExt.inSplitHandleRegion(event)) {
                    android.util.Slog.d(TAG, "swipe gesture start in split-screen handle region");
                    this.mSwipeFireable = false;
                }
                if (this.mSystemGesturesPointerEventListenerExt.isOnePuttHandleRegion(event)) {
                    android.util.Slog.d(TAG, "swipe gesture start in one putt handle region");
                    this.mSwipeFireable = false;
                }
                this.mSystemGesturesPointerEventListenerExt.notifyMotionDown();
                this.mDebugFireable = true;
                this.mSocExt.hookSetScrollFired(false);
                this.mDownPointers = 0;
                captureDown(event, 0);
                if (this.mMouseHoveringAtLeft) {
                    this.mMouseHoveringAtLeft = false;
                    this.mCallbacks.onMouseLeaveFromLeft();
                }
                if (this.mMouseHoveringAtTop) {
                    this.mMouseHoveringAtTop = false;
                    this.mCallbacks.onMouseLeaveFromTop();
                }
                if (this.mMouseHoveringAtRight) {
                    this.mMouseHoveringAtRight = false;
                    this.mCallbacks.onMouseLeaveFromRight();
                }
                if (this.mMouseHoveringAtBottom) {
                    this.mMouseHoveringAtBottom = false;
                    this.mCallbacks.onMouseLeaveFromBottom();
                }
                this.mCallbacks.onDown();
                break;
            case 1:
            case 3:
                this.mSwipeFireable = false;
                this.mDebugFireable = false;
                this.mSystemGesturesPointerEventListenerExt.resetInterceptSwipeEvent();
                if (this.mSocExt.hookGetScrollFired()) {
                    this.mCallbacks.onScroll(false);
                }
                this.mSocExt.hookSetScrollFired(false);
                this.mSystemGesturesPointerEventListenerExt.notifyMotionUpOrCancel();
                this.mCallbacks.onUpOrCancel();
                break;
            case 2:
                if (this.mSwipeFireable) {
                    int trackpadSwipe = detectTrackpadThreeFingerSwipe(event);
                    this.mSwipeFireable = trackpadSwipe == 0;
                    boolean interceptSwipeEvent = this.mSystemGesturesPointerEventListenerExt.getInterceptSwipeEvent();
                    if (!interceptSwipeEvent || trackpadSwipe == 1 || this.mSwipeFireable) {
                        if (!this.mSwipeFireable) {
                            if (trackpadSwipe == 1) {
                                this.mCallbacks.onSwipeFromTop();
                            } else if (trackpadSwipe == 2) {
                                this.mCallbacks.onSwipeFromBottom();
                            } else if (trackpadSwipe == 3) {
                                this.mCallbacks.onSwipeFromRight();
                            } else if (trackpadSwipe == 4) {
                                this.mCallbacks.onSwipeFromLeft();
                            }
                        } else {
                            int swipe = detectSwipe(event);
                            if (!interceptSwipeEvent || swipe == 1) {
                                this.mSwipeFireable = swipe == 0;
                                if (!this.mSwipeFireable && DEBUG_PANIC) {
                                    android.util.Slog.d(TAG, "detectSwipe: " + swipe);
                                }
                                if (swipe == 1) {
                                    this.mCallbacks.onSwipeFromTop();
                                } else if (swipe == 2) {
                                    this.mCallbacks.onSwipeFromBottom();
                                } else if (swipe == 3) {
                                    this.mCallbacks.onSwipeFromRight();
                                } else if (swipe == 4) {
                                    this.mCallbacks.onSwipeFromLeft();
                                }
                            }
                        }
                    }
                }
                break;
            case 5:
                captureDown(event, event.getActionIndex());
                this.mSystemGesturesPointerEventListenerExt.setInterceptSwipeEvent();
                if (this.mDebugFireable) {
                    this.mDebugFireable = event.getPointerCount() < 5;
                    if (!this.mDebugFireable) {
                        this.mCallbacks.onDebug();
                    }
                }
                break;
            case 7:
                if (event.isFromSource(8194)) {
                    float eventX = event.getX();
                    float eventY = event.getY();
                    if (!this.mMouseHoveringAtLeft && eventX == 0.0f) {
                        this.mCallbacks.onMouseHoverAtLeft();
                        this.mMouseHoveringAtLeft = true;
                    } else if (this.mMouseHoveringAtLeft && eventX > 0.0f) {
                        this.mCallbacks.onMouseLeaveFromLeft();
                        this.mMouseHoveringAtLeft = false;
                    }
                    if (!this.mMouseHoveringAtTop && eventY == 0.0f) {
                        this.mCallbacks.onMouseHoverAtTop();
                        this.mMouseHoveringAtTop = true;
                    } else if (this.mMouseHoveringAtTop && eventY > 0.0f) {
                        this.mCallbacks.onMouseLeaveFromTop();
                        this.mMouseHoveringAtTop = false;
                    }
                    if (!this.mMouseHoveringAtRight && eventX >= this.screenWidth - 1) {
                        this.mCallbacks.onMouseHoverAtRight();
                        this.mMouseHoveringAtRight = true;
                    } else if (this.mMouseHoveringAtRight && eventX < this.screenWidth - 1) {
                        this.mCallbacks.onMouseLeaveFromRight();
                        this.mMouseHoveringAtRight = false;
                    }
                    if (!this.mMouseHoveringAtBottom && eventY >= this.screenHeight - 1) {
                        this.mCallbacks.onMouseHoverAtBottom();
                        this.mMouseHoveringAtBottom = true;
                        break;
                    } else if (this.mMouseHoveringAtBottom && eventY < this.screenHeight - 1) {
                        this.mCallbacks.onMouseLeaveFromBottom();
                        this.mMouseHoveringAtBottom = false;
                        break;
                    }
                }
                break;
        }
    }

    private void captureDown(android.view.MotionEvent event, int pointerIndex) {
        int pointerId = event.getPointerId(pointerIndex);
        int i = findIndex(pointerId);
        if (i != -1) {
            this.mDownX[i] = event.getX(pointerIndex);
            this.mDownY[i] = event.getY(pointerIndex);
            this.mDownTime[i] = event.getEventTime();
        }
    }

    protected boolean currentGestureStartedInRegion(android.graphics.Region r) {
        return r.contains((int) this.mDownX[0], (int) this.mDownY[0]);
    }

    private int findIndex(int pointerId) {
        for (int i = 0; i < this.mDownPointers; i++) {
            if (this.mDownPointerId[i] == pointerId) {
                return i;
            }
        }
        int i2 = this.mDownPointers;
        if (i2 == 32 || pointerId == -1) {
            return -1;
        }
        int[] iArr = this.mDownPointerId;
        int i3 = this.mDownPointers;
        this.mDownPointers = i3 + 1;
        iArr[i3] = pointerId;
        return this.mDownPointers - 1;
    }

    private int detectTrackpadThreeFingerSwipe(android.view.MotionEvent move) {
        if (!isTrackpadThreeFingerSwipe(move)) {
            return 0;
        }
        float dx = move.getX() - this.mDownX[0];
        float dy = move.getY() - this.mDownY[0];
        if (java.lang.Math.abs(dx) < java.lang.Math.abs(dy)) {
            if (java.lang.Math.abs(dy) > this.mSwipeDistanceThreshold) {
                return dy > 0.0f ? 1 : 2;
            }
        } else if (java.lang.Math.abs(dx) > this.mSwipeDistanceThreshold) {
            return dx > 0.0f ? 4 : 3;
        }
        return 0;
    }

    private static boolean isTrackpadThreeFingerSwipe(android.view.MotionEvent event) {
        return event.getClassification() == 4 && event.getAxisValue(53) == 3.0f;
    }

    private int detectSwipe(android.view.MotionEvent move) {
        int historySize = move.getHistorySize();
        int pointerCount = move.getPointerCount();
        for (int p = 0; p < pointerCount; p++) {
            int pointerId = move.getPointerId(p);
            int i = findIndex(pointerId);
            if (i != -1) {
                for (int h = 0; h < historySize; h++) {
                    long time = move.getHistoricalEventTime(h);
                    float x = move.getHistoricalX(p, h);
                    float y = move.getHistoricalY(p, h);
                    int swipe = detectSwipe(i, time, x, y);
                    if (swipe != 0) {
                        return swipe;
                    }
                }
                int swipe2 = detectSwipe(i, move.getEventTime(), move.getX(p), move.getY(p));
                if (swipe2 != 0) {
                    return swipe2;
                }
            }
        }
        return 0;
    }

    private int detectSwipe(int i, long time, float x, float y) {
        float fromX = this.mDownX[i];
        float fromY = this.mDownY[i];
        long elapsed = time - this.mDownTime[i];
        if (fromY <= this.mSwipeStartThreshold.top && y > this.mSwipeDistanceThreshold + fromY && elapsed < 500) {
            return this.mSystemGesturesPointerEventListenerExt.hookSwipeFromTop(fromX, fromY) ? -1 : 1;
        }
        if (fromY >= this.screenHeight - this.mSwipeStartThreshold.bottom && y < fromY - this.mSwipeDistanceThreshold && elapsed < 500) {
            return !this.mSystemGesturesPointerEventListenerExt.checkSwipeFromBottom(fromX, fromY, this.screenHeight) ? 0 : 2;
        }
        if (fromX >= this.screenWidth - this.mSwipeStartThreshold.right && x < fromX - this.mSwipeDistanceThreshold && elapsed < 500) {
            return (this.mSystemGesturesPointerEventListenerExt == null || !this.mSystemGesturesPointerEventListenerExt.checkSwipeForGameMode(2, this.screenWidth, fromX)) ? 3 : 0;
        }
        if (fromX > this.mSwipeStartThreshold.left || x <= this.mSwipeDistanceThreshold + fromX || elapsed >= 500) {
            return 0;
        }
        return (this.mSystemGesturesPointerEventListenerExt == null || !this.mSystemGesturesPointerEventListenerExt.checkSwipeForGameMode(1, this.screenWidth, fromX)) ? 4 : 0;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        java.lang.String inner = prefix + "  ";
        pw.println(prefix + TAG + ":");
        pw.print(inner);
        pw.print("mDisplayCutoutTouchableRegionSize=");
        pw.println(this.mDisplayCutoutTouchableRegionSize);
        pw.print(inner);
        pw.print("mSwipeStartThreshold=");
        pw.println(this.mSwipeStartThreshold);
        pw.print(inner);
        pw.print("mSwipeDistanceThreshold=");
        pw.println(this.mSwipeDistanceThreshold);
    }

    private final class FlingGestureDetector extends android.view.GestureDetector.SimpleOnGestureListener {
        private android.widget.OverScroller mOverscroller;

        FlingGestureDetector() {
            this.mOverscroller = new android.widget.OverScroller(com.android.server.wm.SystemGesturesPointerEventListener.this.mContext);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(android.view.MotionEvent e) {
            if (!this.mOverscroller.isFinished()) {
                this.mOverscroller.forceFinished(true);
            }
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(android.view.MotionEvent down, android.view.MotionEvent up, float velocityX, float velocityY) {
            this.mOverscroller.computeScrollOffset();
            long now = android.os.SystemClock.uptimeMillis();
            if (com.android.server.wm.SystemGesturesPointerEventListener.this.mLastFlingTime != 0 && now > com.android.server.wm.SystemGesturesPointerEventListener.this.mLastFlingTime + 5000) {
                this.mOverscroller.forceFinished(true);
            }
            this.mOverscroller.fling(0, 0, (int) velocityX, (int) velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            int duration = this.mOverscroller.getDuration();
            if (duration > 5000) {
                duration = 5000;
            }
            com.android.server.wm.SystemGesturesPointerEventListener.this.mSocExt.hookOnFling(com.android.server.wm.SystemGesturesPointerEventListener.this.mCallbacks, velocityX, velocityY, duration);
            com.android.server.wm.SystemGesturesPointerEventListener.this.mSystemGesturesPointerEventListenerExt.hookOnGlobalFlingGesture(duration);
            com.android.server.wm.SystemGesturesPointerEventListener.this.mLastFlingTime = now;
            com.android.server.wm.SystemGesturesPointerEventListener.this.mSystemGesturesPointerEventListenerExt.notifyFlingGestureStatus(duration);
            com.android.server.wm.SystemGesturesPointerEventListener.this.mCallbacks.onFling(duration);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(android.view.MotionEvent e1, android.view.MotionEvent e2, float distanceX, float distanceY) {
            if (!com.android.server.wm.SystemGesturesPointerEventListener.this.mSocExt.hookGetScrollFired()) {
                com.android.server.wm.SystemGesturesPointerEventListener.this.mCallbacks.onScroll(true);
                com.android.server.wm.SystemGesturesPointerEventListener.this.mSocExt.hookSetScrollFired(true);
            }
            com.android.server.wm.SystemGesturesPointerEventListener.this.mSystemGesturesPointerEventListenerExt.notifyScrollGestureStatus();
            return true;
        }
    }
}
