package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class OverlayDisplayWindow implements com.android.internal.util.DumpUtils.Dump {
    private final android.content.Context mContext;
    private final android.view.Display mDefaultDisplay;
    private int mDensityDpi;
    private final android.hardware.display.DisplayManager mDisplayManager;
    private android.view.GestureDetector mGestureDetector;
    private final int mGravity;
    private int mHeight;
    private final com.android.server.display.OverlayDisplayWindow.Listener mListener;
    private float mLiveTranslationX;
    private float mLiveTranslationY;
    private final java.lang.String mName;
    private android.view.ScaleGestureDetector mScaleGestureDetector;
    private final boolean mSecure;
    private android.view.TextureView mTextureView;
    private java.lang.String mTitle;
    private android.widget.TextView mTitleTextView;
    private int mWidth;
    private android.view.View mWindowContent;
    private final android.view.WindowManager mWindowManager;
    private android.view.WindowManager.LayoutParams mWindowParams;
    private float mWindowScale;
    private boolean mWindowVisible;
    private int mWindowX;
    private int mWindowY;
    private static final java.lang.String TAG = "OverlayDisplayWindow";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);
    private final float INITIAL_SCALE = 0.5f;
    private final float MIN_SCALE = 0.3f;
    private final float MAX_SCALE = 1.0f;
    private final float WINDOW_ALPHA = 0.8f;
    private final boolean DISABLE_MOVE_AND_RESIZE = false;
    private final android.view.DisplayInfo mDefaultDisplayInfo = new android.view.DisplayInfo();
    private float mLiveScale = 1.0f;
    private final android.hardware.display.DisplayManager.DisplayListener mDisplayListener = new android.hardware.display.DisplayManager.DisplayListener() { // from class: com.android.server.display.OverlayDisplayWindow.1
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            if (displayId == com.android.server.display.OverlayDisplayWindow.this.mDefaultDisplay.getDisplayId()) {
                if (com.android.server.display.OverlayDisplayWindow.this.updateDefaultDisplayInfo()) {
                    com.android.server.display.OverlayDisplayWindow.this.relayout();
                    com.android.server.display.OverlayDisplayWindow.this.mListener.onStateChanged(com.android.server.display.OverlayDisplayWindow.this.mDefaultDisplayInfo.state);
                } else {
                    com.android.server.display.OverlayDisplayWindow.this.dismiss();
                }
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
            if (displayId == com.android.server.display.OverlayDisplayWindow.this.mDefaultDisplay.getDisplayId()) {
                com.android.server.display.OverlayDisplayWindow.this.dismiss();
            }
        }
    };
    private final android.view.TextureView.SurfaceTextureListener mSurfaceTextureListener = new android.view.TextureView.SurfaceTextureListener() { // from class: com.android.server.display.OverlayDisplayWindow.2
        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(android.graphics.SurfaceTexture surfaceTexture, int width, int height) {
            com.android.server.display.OverlayDisplayWindow.this.mListener.onWindowCreated(surfaceTexture, com.android.server.display.OverlayDisplayWindow.this.mDefaultDisplayInfo.getRefreshRate(), com.android.server.display.OverlayDisplayWindow.this.mDefaultDisplayInfo.presentationDeadlineNanos, com.android.server.display.OverlayDisplayWindow.this.mDefaultDisplayInfo.state);
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture surfaceTexture) {
            com.android.server.display.OverlayDisplayWindow.this.mListener.onWindowDestroyed();
            return true;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture surfaceTexture, int width, int height) {
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(android.graphics.SurfaceTexture surfaceTexture) {
        }
    };
    private final android.view.View.OnTouchListener mOnTouchListener = new android.view.View.OnTouchListener() { // from class: com.android.server.display.OverlayDisplayWindow.3
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(android.view.View view, android.view.MotionEvent event) {
            float oldX = event.getX();
            float oldY = event.getY();
            event.setLocation(event.getRawX(), event.getRawY());
            com.android.server.display.OverlayDisplayWindow.this.mGestureDetector.onTouchEvent(event);
            com.android.server.display.OverlayDisplayWindow.this.mScaleGestureDetector.onTouchEvent(event);
            switch (event.getActionMasked()) {
                case 1:
                case 3:
                    com.android.server.display.OverlayDisplayWindow.this.saveWindowParams();
                    break;
            }
            event.setLocation(oldX, oldY);
            return true;
        }
    };
    private final android.view.GestureDetector.OnGestureListener mOnGestureListener = new android.view.GestureDetector.SimpleOnGestureListener() { // from class: com.android.server.display.OverlayDisplayWindow.4
        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(android.view.MotionEvent e1, android.view.MotionEvent e2, float distanceX, float distanceY) {
            com.android.server.display.OverlayDisplayWindow.this.mLiveTranslationX -= distanceX;
            com.android.server.display.OverlayDisplayWindow.this.mLiveTranslationY -= distanceY;
            com.android.server.display.OverlayDisplayWindow.this.relayout();
            return true;
        }
    };
    private final android.view.ScaleGestureDetector.OnScaleGestureListener mOnScaleGestureListener = new android.view.ScaleGestureDetector.SimpleOnScaleGestureListener() { // from class: com.android.server.display.OverlayDisplayWindow.5
        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(android.view.ScaleGestureDetector detector) {
            com.android.server.display.OverlayDisplayWindow.this.mLiveScale *= detector.getScaleFactor();
            com.android.server.display.OverlayDisplayWindow.this.relayout();
            return true;
        }
    };

    public interface Listener {
        void onStateChanged(int i);

        void onWindowCreated(android.graphics.SurfaceTexture surfaceTexture, float f, long j, int i);

        void onWindowDestroyed();
    }

    public OverlayDisplayWindow(android.content.Context context, java.lang.String name, int width, int height, int densityDpi, int gravity, boolean secure, com.android.server.display.OverlayDisplayWindow.Listener listener) {
        android.view.ThreadedRenderer.disableVsync();
        this.mContext = context;
        this.mName = name;
        this.mGravity = gravity;
        this.mSecure = secure;
        this.mListener = listener;
        this.mDisplayManager = (android.hardware.display.DisplayManager) context.getSystemService("display");
        this.mWindowManager = (android.view.WindowManager) context.getSystemService("window");
        this.mDefaultDisplay = this.mContext.getDisplay();
        updateDefaultDisplayInfo();
        resize(width, height, densityDpi, false);
        createWindow();
    }

    public void show() {
        if (!this.mWindowVisible) {
            this.mDisplayManager.registerDisplayListener(this.mDisplayListener, null);
            if (!updateDefaultDisplayInfo()) {
                this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
                return;
            }
            clearLiveState();
            updateWindowParams();
            this.mWindowManager.addView(this.mWindowContent, this.mWindowParams);
            this.mWindowVisible = true;
        }
    }

    public void dismiss() {
        if (this.mWindowVisible) {
            this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
            this.mWindowManager.removeView(this.mWindowContent);
            this.mWindowVisible = false;
        }
    }

    public void resize(int width, int height, int densityDpi) {
        resize(width, height, densityDpi, true);
    }

    private void resize(int width, int height, int densityDpi, boolean doLayout) {
        this.mWidth = width;
        this.mHeight = height;
        this.mDensityDpi = densityDpi;
        this.mTitle = this.mContext.getResources().getString(android.R.string.dump_heap_title, this.mName, java.lang.Integer.valueOf(this.mWidth), java.lang.Integer.valueOf(this.mHeight), java.lang.Integer.valueOf(this.mDensityDpi));
        if (this.mSecure) {
            this.mTitle += this.mContext.getResources().getString(android.R.string.dump_heap_text);
        }
        if (doLayout) {
            relayout();
        }
    }

    public void relayout() {
        if (this.mWindowVisible) {
            updateWindowParams();
            this.mWindowManager.updateViewLayout(this.mWindowContent, this.mWindowParams);
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println("mWindowVisible=" + this.mWindowVisible);
        pw.println("mWindowX=" + this.mWindowX);
        pw.println("mWindowY=" + this.mWindowY);
        pw.println("mWindowScale=" + this.mWindowScale);
        pw.println("mWindowParams=" + this.mWindowParams);
        if (this.mTextureView != null) {
            pw.println("mTextureView.getScaleX()=" + this.mTextureView.getScaleX());
            pw.println("mTextureView.getScaleY()=" + this.mTextureView.getScaleY());
        }
        pw.println("mLiveTranslationX=" + this.mLiveTranslationX);
        pw.println("mLiveTranslationY=" + this.mLiveTranslationY);
        pw.println("mLiveScale=" + this.mLiveScale);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateDefaultDisplayInfo() {
        if (!this.mDefaultDisplay.getDisplayInfo(this.mDefaultDisplayInfo)) {
            android.util.Slog.w(TAG, "Cannot show overlay display because there is no default display upon which to show it.");
            return false;
        }
        return true;
    }

    private void createWindow() {
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this.mContext);
        this.mWindowContent = inflater.inflate(android.R.layout.notification_material_action_tombstone, (android.view.ViewGroup) null);
        this.mWindowContent.setOnTouchListener(this.mOnTouchListener);
        this.mTextureView = (android.view.TextureView) this.mWindowContent.findViewById(android.R.id.notification_headerless_view_column);
        this.mTextureView.setPivotX(0.0f);
        this.mTextureView.setPivotY(0.0f);
        this.mTextureView.getLayoutParams().width = this.mWidth;
        this.mTextureView.getLayoutParams().height = this.mHeight;
        this.mTextureView.setOpaque(false);
        this.mTextureView.setSurfaceTextureListener(this.mSurfaceTextureListener);
        this.mTitleTextView = (android.widget.TextView) this.mWindowContent.findViewById(android.R.id.notification_headerless_view_row);
        this.mTitleTextView.setText(this.mTitle);
        this.mWindowParams = new android.view.WindowManager.LayoutParams(2026);
        this.mWindowParams.flags |= 16778024;
        if (this.mSecure) {
            this.mWindowParams.flags |= 8192;
        }
        this.mWindowParams.privateFlags |= 2;
        this.mWindowParams.alpha = 0.8f;
        this.mWindowParams.gravity = 51;
        this.mWindowParams.setTitle(this.mTitle);
        this.mGestureDetector = new android.view.GestureDetector(this.mContext, this.mOnGestureListener);
        this.mScaleGestureDetector = new android.view.ScaleGestureDetector(this.mContext, this.mOnScaleGestureListener);
        this.mWindowX = (this.mGravity & 3) == 3 ? 0 : this.mDefaultDisplayInfo.logicalWidth;
        this.mWindowY = (this.mGravity & 48) != 48 ? this.mDefaultDisplayInfo.logicalHeight : 0;
        this.mWindowScale = 0.5f;
    }

    private void updateWindowParams() {
        float scale = java.lang.Math.max(0.3f, java.lang.Math.min(1.0f, java.lang.Math.min(java.lang.Math.min(this.mWindowScale * this.mLiveScale, this.mDefaultDisplayInfo.logicalWidth / this.mWidth), this.mDefaultDisplayInfo.logicalHeight / this.mHeight)));
        float offsetScale = ((scale / this.mWindowScale) - 1.0f) * 0.5f;
        int width = (int) (this.mWidth * scale);
        int height = (int) (this.mHeight * scale);
        int x = (int) ((this.mWindowX + this.mLiveTranslationX) - (width * offsetScale));
        int y = (int) ((this.mWindowY + this.mLiveTranslationY) - (height * offsetScale));
        int x2 = java.lang.Math.max(0, java.lang.Math.min(x, this.mDefaultDisplayInfo.logicalWidth - width));
        int y2 = java.lang.Math.max(0, java.lang.Math.min(y, this.mDefaultDisplayInfo.logicalHeight - height));
        if (DEBUG) {
            android.util.Slog.d(TAG, "updateWindowParams: scale=" + scale + ", offsetScale=" + offsetScale + ", x=" + x2 + ", y=" + y2 + ", width=" + width + ", height=" + height);
        }
        this.mTextureView.setScaleX(scale);
        this.mTextureView.setScaleY(scale);
        this.mWindowParams.x = x2;
        this.mWindowParams.y = y2;
        this.mWindowParams.width = width;
        this.mWindowParams.height = height;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveWindowParams() {
        this.mWindowX = this.mWindowParams.x;
        this.mWindowY = this.mWindowParams.y;
        this.mWindowScale = this.mTextureView.getScaleX();
        clearLiveState();
    }

    private void clearLiveState() {
        this.mLiveTranslationX = 0.0f;
        this.mLiveTranslationY = 0.0f;
        this.mLiveScale = 1.0f;
    }
}
