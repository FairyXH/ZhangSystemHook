package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class ColorFade {
    private static final int COLOR_FADE_LAYER = 1073741825;
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("dbg.dms.colorfade", false);
    private static final int DEJANK_FRAMES = 3;
    private static final int EGL_GL_COLORSPACE_DISPLAY_P3_PASSTHROUGH_EXT = 13456;
    private static final int EGL_GL_COLORSPACE_KHR = 12445;
    private static final int EGL_PROTECTED_CONTENT_EXT = 12992;
    public static final int MODE_COOL_DOWN = 1;
    public static final int MODE_FADE = 2;
    public static final int MODE_WARM_UP = 0;
    private static final java.lang.String TAG = "ColorFade";
    private android.graphics.BLASTBufferQueue mBLASTBufferQueue;
    private android.view.SurfaceControl mBLASTSurfaceControl;
    private boolean mCreatedResources;
    private int mDisplayHeight;
    private final int mDisplayId;
    private int mDisplayLayerStack;
    private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    private int mDisplayWidth;
    private android.opengl.EGLConfig mEglConfig;
    private android.opengl.EGLContext mEglContext;
    private android.opengl.EGLDisplay mEglDisplay;
    private android.opengl.EGLSurface mEglSurface;
    private final int[] mGLBuffers;
    private int mGammaLoc;
    private boolean mLastWasProtectedContent;
    private boolean mLastWasWideColor;
    private int mMode;
    private int mOpacityLoc;
    private boolean mPrepared;
    private int mProgram;
    private final float[] mProjMatrix;
    private int mProjMatrixLoc;
    private android.view.Surface mSurface;
    private float mSurfaceAlpha;
    private android.view.SurfaceControl mSurfaceControl;
    private com.android.server.display.ColorFade.NaturalSurfaceLayout mSurfaceLayout;
    private boolean mSurfaceVisible;
    private final java.nio.FloatBuffer mTexCoordBuffer;
    private int mTexCoordLoc;
    private final float[] mTexMatrix;
    private int mTexMatrixLoc;
    private final int[] mTexNames;
    private boolean mTexNamesGenerated;
    private int mTexUnitLoc;
    private final android.view.SurfaceControl.Transaction mTransaction;
    private final java.nio.FloatBuffer mVertexBuffer;
    private int mVertexLoc;

    public ColorFade(int displayId) {
        this(displayId, (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class));
    }

    ColorFade(int displayId, android.hardware.display.DisplayManagerInternal displayManagerInternal) {
        this.mTexNames = new int[1];
        this.mTexMatrix = new float[16];
        this.mProjMatrix = new float[16];
        this.mGLBuffers = new int[2];
        this.mVertexBuffer = createNativeFloatBuffer(8);
        this.mTexCoordBuffer = createNativeFloatBuffer(8);
        this.mTransaction = new android.view.SurfaceControl.Transaction();
        this.mDisplayId = displayId;
        this.mDisplayManagerInternal = displayManagerInternal;
        android.util.Slog.d(TAG, "create ColorFade for displayId =" + displayId);
    }

    public boolean prepare(android.content.Context context, int mode) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "prepare: mode=" + mode);
        }
        this.mMode = mode;
        android.view.DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(this.mDisplayId);
        if (displayInfo == null) {
            return false;
        }
        this.mDisplayLayerStack = displayInfo.layerStack;
        this.mDisplayWidth = displayInfo.getNaturalWidth();
        this.mDisplayHeight = displayInfo.getNaturalHeight();
        boolean isWideColor = displayInfo.colorMode == 9;
        this.mPrepared = true;
        android.window.ScreenCapture.ScreenshotHardwareBuffer hardwareBuffer = captureScreen();
        if (hardwareBuffer == null) {
            dismiss();
            return false;
        }
        boolean isProtected = com.android.internal.policy.TransitionAnimation.hasProtectedContent(hardwareBuffer.getHardwareBuffer());
        if (!createSurfaceControl(hardwareBuffer.containsSecureLayers())) {
            dismiss();
            return false;
        }
        if (this.mMode == 2) {
            return true;
        }
        if (!createEglContext(isProtected) || !createEglSurface(isProtected, isWideColor) || !setScreenshotTextureAndSetViewport(hardwareBuffer, displayInfo.rotation)) {
            dismiss();
            return false;
        }
        if (!attachEglContext()) {
            return false;
        }
        try {
            if (!initGLShaders(context) || !initGLBuffers() || checkGlErrors("prepare")) {
                detachEglContext();
                dismiss();
                return false;
            }
            detachEglContext();
            this.mCreatedResources = true;
            this.mLastWasProtectedContent = isProtected;
            this.mLastWasWideColor = isWideColor;
            if (mode == 1) {
                for (int i = 0; i < 3; i++) {
                    draw(1.0f);
                }
            }
            return true;
        } finally {
            detachEglContext();
        }
    }

    private java.lang.String readFile(android.content.Context context, int resourceId) {
        try {
            java.io.InputStream stream = context.getResources().openRawResource(resourceId);
            return new java.lang.String(libcore.io.Streams.readFully(new java.io.InputStreamReader(stream)));
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Unrecognized shader " + java.lang.Integer.toString(resourceId));
            throw new java.lang.RuntimeException(e);
        }
    }

    private int loadShader(android.content.Context context, int resourceId, int type) {
        java.lang.String source = readFile(context, resourceId);
        int shader = android.opengl.GLES20.glCreateShader(type);
        android.opengl.GLES20.glShaderSource(shader, source);
        android.opengl.GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        android.opengl.GLES20.glGetShaderiv(shader, 35713, compiled, 0);
        if (compiled[0] == 0) {
            android.util.Slog.e(TAG, "Could not compile shader " + shader + ", " + type + ":");
            android.util.Slog.e(TAG, android.opengl.GLES20.glGetShaderSource(shader));
            android.util.Slog.e(TAG, android.opengl.GLES20.glGetShaderInfoLog(shader));
            android.opengl.GLES20.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    private boolean initGLShaders(android.content.Context context) {
        int vshader = loadShader(context, android.R.raw.color_fade_vert, 35633);
        int fshader = loadShader(context, android.R.raw.color_fade_frag, 35632);
        android.opengl.GLES20.glReleaseShaderCompiler();
        if (vshader == 0 || fshader == 0) {
            return false;
        }
        this.mProgram = android.opengl.GLES20.glCreateProgram();
        android.opengl.GLES20.glAttachShader(this.mProgram, vshader);
        android.opengl.GLES20.glAttachShader(this.mProgram, fshader);
        android.opengl.GLES20.glDeleteShader(vshader);
        android.opengl.GLES20.glDeleteShader(fshader);
        android.opengl.GLES20.glLinkProgram(this.mProgram);
        this.mVertexLoc = android.opengl.GLES20.glGetAttribLocation(this.mProgram, "position");
        this.mTexCoordLoc = android.opengl.GLES20.glGetAttribLocation(this.mProgram, "uv");
        this.mProjMatrixLoc = android.opengl.GLES20.glGetUniformLocation(this.mProgram, "proj_matrix");
        this.mTexMatrixLoc = android.opengl.GLES20.glGetUniformLocation(this.mProgram, "tex_matrix");
        this.mOpacityLoc = android.opengl.GLES20.glGetUniformLocation(this.mProgram, "opacity");
        this.mGammaLoc = android.opengl.GLES20.glGetUniformLocation(this.mProgram, "gamma");
        this.mTexUnitLoc = android.opengl.GLES20.glGetUniformLocation(this.mProgram, "texUnit");
        android.opengl.GLES20.glUseProgram(this.mProgram);
        android.opengl.GLES20.glUniform1i(this.mTexUnitLoc, 0);
        android.opengl.GLES20.glUseProgram(0);
        return true;
    }

    private void destroyGLShaders() {
        android.opengl.GLES20.glDeleteProgram(this.mProgram);
        checkGlErrors("glDeleteProgram");
    }

    private boolean initGLBuffers() {
        setQuad(this.mVertexBuffer, 0.0f, 0.0f, this.mDisplayWidth, this.mDisplayHeight);
        android.opengl.GLES20.glBindTexture(36197, this.mTexNames[0]);
        android.opengl.GLES20.glTexParameteri(36197, 10240, 9728);
        android.opengl.GLES20.glTexParameteri(36197, 10241, 9728);
        android.opengl.GLES20.glTexParameteri(36197, 10242, 33071);
        android.opengl.GLES20.glTexParameteri(36197, 10243, 33071);
        android.opengl.GLES20.glBindTexture(36197, 0);
        android.opengl.GLES20.glGenBuffers(2, this.mGLBuffers, 0);
        android.opengl.GLES20.glBindBuffer(34962, this.mGLBuffers[0]);
        android.opengl.GLES20.glBufferData(34962, this.mVertexBuffer.capacity() * 4, this.mVertexBuffer, 35044);
        android.opengl.GLES20.glBindBuffer(34962, this.mGLBuffers[1]);
        android.opengl.GLES20.glBufferData(34962, this.mTexCoordBuffer.capacity() * 4, this.mTexCoordBuffer, 35044);
        android.opengl.GLES20.glBindBuffer(34962, 0);
        return true;
    }

    private void destroyGLBuffers() {
        android.opengl.GLES20.glDeleteBuffers(2, this.mGLBuffers, 0);
        checkGlErrors("glDeleteBuffers");
    }

    private static void setQuad(java.nio.FloatBuffer vtx, float x, float y, float w, float h) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "setQuad: x=" + x + ", y=" + y + ", w=" + w + ", h=" + h);
        }
        vtx.put(0, x);
        vtx.put(1, y);
        vtx.put(2, x);
        vtx.put(3, y + h);
        vtx.put(4, x + w);
        vtx.put(5, y + h);
        vtx.put(6, x + w);
        vtx.put(7, y);
    }

    public void dismissResources() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "dismissResources");
        }
        if (this.mCreatedResources) {
            attachEglContext();
            try {
                destroyScreenshotTexture();
                destroyGLShaders();
                destroyGLBuffers();
                destroyEglSurface();
                detachEglContext();
                android.opengl.GLES20.glFlush();
                this.mCreatedResources = false;
            } catch (java.lang.Throwable th) {
                detachEglContext();
                throw th;
            }
        }
    }

    public void dismiss() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "dismiss");
        }
        if (!"PowerManagerService".equals(java.lang.Thread.currentThread().getName())) {
            android.util.Slog.e(TAG, "dismiss in other thread", new java.lang.Throwable());
        }
        if (this.mPrepared) {
            dismissResources();
            destroySurface();
            this.mPrepared = false;
        }
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            if (this.mEglContext != null) {
                android.opengl.EGL14.eglDestroyContext(this.mEglDisplay, this.mEglContext);
                this.mEglContext = null;
            }
        } finally {
            super.finalize();
        }
    }

    public void destroy() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "destroy");
        }
        if (this.mPrepared) {
            if (this.mCreatedResources) {
                attachEglContext();
                try {
                    destroyScreenshotTexture();
                    destroyGLShaders();
                    destroyGLBuffers();
                    destroyEglSurface();
                } finally {
                    detachEglContext();
                }
            }
            destroyEglContext();
            destroySurface();
        }
    }

    public boolean draw(float level) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "drawFrame: level=" + level);
        }
        if (!this.mPrepared) {
            return false;
        }
        if (this.mMode == 2) {
            return showSurface(1.0f - level);
        }
        if (!attachEglContext()) {
            return false;
        }
        if (level != 0.0f) {
            return showSurface(0.0f);
        }
        try {
            android.opengl.GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            android.opengl.GLES20.glClear(16384);
            double one_minus_level = 1.0f - level;
            double cos = java.lang.Math.cos(3.141592653589793d * one_minus_level);
            double sign = cos < 0.0d ? -1.0d : 1.0d;
            float opacity = ((float) (-java.lang.Math.pow(one_minus_level, 2.0d))) + 1.0f;
            float gamma = (float) ((((sign * 0.5d * java.lang.Math.pow(cos, 2.0d)) + 0.5d) * 0.9d) + 0.1d);
            drawFaded(opacity, 1.0f / gamma);
            if (checkGlErrors("drawFrame")) {
                return false;
            }
            android.opengl.EGL14.eglSwapBuffers(this.mEglDisplay, this.mEglSurface);
            detachEglContext();
            return showSurface(1.0f);
        } finally {
            detachEglContext();
        }
    }

    private void drawFaded(float opacity, float gamma) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "drawFaded: opacity=" + opacity + ", gamma=" + gamma);
        }
        android.opengl.GLES20.glUseProgram(this.mProgram);
        android.opengl.GLES20.glUniformMatrix4fv(this.mProjMatrixLoc, 1, false, this.mProjMatrix, 0);
        android.opengl.GLES20.glUniformMatrix4fv(this.mTexMatrixLoc, 1, false, this.mTexMatrix, 0);
        android.opengl.GLES20.glUniform1f(this.mOpacityLoc, opacity);
        android.opengl.GLES20.glUniform1f(this.mGammaLoc, gamma);
        android.opengl.GLES20.glActiveTexture(33984);
        android.opengl.GLES20.glBindTexture(36197, this.mTexNames[0]);
        android.opengl.GLES20.glBindBuffer(34962, this.mGLBuffers[0]);
        android.opengl.GLES20.glEnableVertexAttribArray(this.mVertexLoc);
        android.opengl.GLES20.glVertexAttribPointer(this.mVertexLoc, 2, 5126, false, 0, 0);
        android.opengl.GLES20.glBindBuffer(34962, this.mGLBuffers[1]);
        android.opengl.GLES20.glEnableVertexAttribArray(this.mTexCoordLoc);
        android.opengl.GLES20.glVertexAttribPointer(this.mTexCoordLoc, 2, 5126, false, 0, 0);
        android.opengl.GLES20.glDrawArrays(6, 0, 4);
        android.opengl.GLES20.glBindTexture(36197, 0);
        android.opengl.GLES20.glBindBuffer(34962, 0);
    }

    private void ortho(float left, float right, float bottom, float top, float znear, float zfar) {
        this.mProjMatrix[0] = 2.0f / (right - left);
        this.mProjMatrix[1] = 0.0f;
        this.mProjMatrix[2] = 0.0f;
        this.mProjMatrix[3] = 0.0f;
        this.mProjMatrix[4] = 0.0f;
        this.mProjMatrix[5] = 2.0f / (top - bottom);
        this.mProjMatrix[6] = 0.0f;
        this.mProjMatrix[7] = 0.0f;
        this.mProjMatrix[8] = 0.0f;
        this.mProjMatrix[9] = 0.0f;
        this.mProjMatrix[10] = (-2.0f) / (zfar - znear);
        this.mProjMatrix[11] = 0.0f;
        this.mProjMatrix[12] = (-(right + left)) / (right - left);
        this.mProjMatrix[13] = (-(top + bottom)) / (top - bottom);
        this.mProjMatrix[14] = (-(zfar + znear)) / (zfar - znear);
        this.mProjMatrix[15] = 1.0f;
    }

    private boolean setScreenshotTextureAndSetViewport(android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer, int rotation) {
        if (!attachEglContext()) {
            return false;
        }
        try {
            if (!this.mTexNamesGenerated) {
                android.opengl.GLES20.glGenTextures(1, this.mTexNames, 0);
                if (checkGlErrors("glGenTextures")) {
                    return false;
                }
                this.mTexNamesGenerated = true;
            }
            android.graphics.SurfaceTexture st = new android.graphics.SurfaceTexture(this.mTexNames[0]);
            android.view.Surface s = new android.view.Surface(st);
            try {
                s.attachAndQueueBufferWithColorSpace(screenshotBuffer.getHardwareBuffer(), screenshotBuffer.getColorSpace());
                st.updateTexImage();
                st.getTransformMatrix(this.mTexMatrix);
                s.release();
                st.release();
                int indexDelta = 2;
                if (rotation != 1) {
                    if (rotation == 2) {
                        indexDelta = 4;
                    } else {
                        indexDelta = rotation == 3 ? 6 : 0;
                    }
                }
                this.mTexCoordBuffer.put(indexDelta, 0.0f);
                this.mTexCoordBuffer.put(indexDelta + 1, 0.0f);
                this.mTexCoordBuffer.put((indexDelta + 2) % 8, 0.0f);
                this.mTexCoordBuffer.put((indexDelta + 3) % 8, 1.0f);
                this.mTexCoordBuffer.put((indexDelta + 4) % 8, 1.0f);
                this.mTexCoordBuffer.put((indexDelta + 5) % 8, 1.0f);
                this.mTexCoordBuffer.put((indexDelta + 6) % 8, 1.0f);
                this.mTexCoordBuffer.put((indexDelta + 7) % 8, 0.0f);
                android.opengl.GLES20.glViewport(0, 0, this.mDisplayWidth, this.mDisplayHeight);
                ortho(0.0f, this.mDisplayWidth, 0.0f, this.mDisplayHeight, -1.0f, 1.0f);
                return true;
            } catch (java.lang.Throwable th) {
                s.release();
                st.release();
                throw th;
            }
        } finally {
            detachEglContext();
        }
    }

    private void destroyScreenshotTexture() {
        if (this.mTexNamesGenerated) {
            this.mTexNamesGenerated = false;
            android.opengl.GLES20.glDeleteTextures(1, this.mTexNames, 0);
            checkGlErrors("glDeleteTextures");
        }
    }

    private android.window.ScreenCapture.ScreenshotHardwareBuffer captureScreen() {
        android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer = this.mDisplayManagerInternal.systemScreenshot(this.mDisplayId);
        if (screenshotBuffer == null) {
            android.util.Slog.e(TAG, "Failed to take screenshot. Buffer is null");
            return null;
        }
        return screenshotBuffer;
    }

    private boolean createSurfaceControl(boolean isSecure) {
        if (this.mSurfaceControl != null) {
            this.mTransaction.setSecure(this.mSurfaceControl, isSecure).apply();
            return true;
        }
        try {
            android.view.SurfaceControl.Builder builder = new android.view.SurfaceControl.Builder().setName(TAG).setSecure(isSecure).setCallsite("ColorFade.createSurface");
            if (this.mMode == 2) {
                builder.setColorLayer();
            } else {
                builder.setContainerLayer();
            }
            this.mSurfaceControl = builder.build();
            this.mTransaction.setLayerStack(this.mSurfaceControl, this.mDisplayLayerStack);
            this.mTransaction.setWindowCrop(this.mSurfaceControl, this.mDisplayWidth, this.mDisplayHeight);
            this.mSurfaceLayout = new com.android.server.display.ColorFade.NaturalSurfaceLayout(this.mDisplayManagerInternal, this.mDisplayId, this.mSurfaceControl);
            this.mSurfaceLayout.onDisplayTransaction(this.mTransaction);
            this.mTransaction.apply();
            if (this.mMode != 2) {
                android.view.SurfaceControl.Builder b = new android.view.SurfaceControl.Builder().setName("ColorFade BLAST").setParent(this.mSurfaceControl).setHidden(false).setSecure(isSecure).setBLASTLayer();
                this.mBLASTSurfaceControl = b.build();
                this.mBLASTBufferQueue = new android.graphics.BLASTBufferQueue(TAG, this.mBLASTSurfaceControl, this.mDisplayWidth, this.mDisplayHeight, -3);
                this.mSurface = this.mBLASTBufferQueue.createSurface();
            }
            return true;
        } catch (android.view.Surface.OutOfResourcesException ex) {
            android.util.Slog.e(TAG, "Unable to create surface.", ex);
            return false;
        }
    }

    private boolean createEglContext(boolean isProtected) {
        if (this.mEglDisplay == null) {
            this.mEglDisplay = android.opengl.EGL14.eglGetDisplay(0);
            if (this.mEglDisplay == android.opengl.EGL14.EGL_NO_DISPLAY) {
                logEglError("eglGetDisplay");
                return false;
            }
            int[] version = new int[2];
            if (!android.opengl.EGL14.eglInitialize(this.mEglDisplay, version, 0, version, 1)) {
                this.mEglDisplay = null;
                logEglError("eglInitialize");
                return false;
            }
        }
        if (this.mEglConfig == null) {
            int[] eglConfigAttribList = {12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344};
            int[] numEglConfigs = new int[1];
            android.opengl.EGLConfig[] eglConfigs = new android.opengl.EGLConfig[1];
            if (!android.opengl.EGL14.eglChooseConfig(this.mEglDisplay, eglConfigAttribList, 0, eglConfigs, 0, eglConfigs.length, numEglConfigs, 0)) {
                logEglError("eglChooseConfig");
                return false;
            }
            if (numEglConfigs[0] > 0) {
                this.mEglConfig = eglConfigs[0];
            } else {
                android.util.Slog.e(TAG, "no valid config found");
                return false;
            }
        }
        if (this.mEglContext != null && isProtected != this.mLastWasProtectedContent) {
            android.util.Slog.i(TAG, "eglDestroyContext for mDisplayId = " + this.mDisplayId + " mEglContext " + this.mEglContext);
            android.opengl.EGL14.eglDestroyContext(this.mEglDisplay, this.mEglContext);
            this.mEglContext = null;
        }
        if (this.mEglContext == null) {
            int[] eglContextAttribList = {12440, 2, 12344, 12344, 12344};
            if (isProtected) {
                eglContextAttribList[2] = EGL_PROTECTED_CONTENT_EXT;
                eglContextAttribList[3] = 1;
            }
            this.mEglContext = android.opengl.EGL14.eglCreateContext(this.mEglDisplay, this.mEglConfig, android.opengl.EGL14.EGL_NO_CONTEXT, eglContextAttribList, 0);
            android.util.Slog.i(TAG, "eglCreateContext for mDisplayId = " + this.mDisplayId + " mEglContext " + this.mEglContext);
            if (this.mEglContext == null) {
                logEglError("eglCreateContext");
                return false;
            }
        }
        return true;
    }

    public void dismissEglContext() {
        if (this.mEglDisplay == null || this.mEglContext == null) {
            android.util.Slog.i(TAG, "force dismissEglContext failed of mDisplayId =" + this.mDisplayId + " for mEglDisplay = " + this.mEglDisplay + " mEglContext = " + this.mEglContext);
            return;
        }
        android.util.Slog.i(TAG, "force dismissEglContext(eglDestroyContext) for mDisplayId = " + this.mDisplayId + " mEglContext " + this.mEglContext);
        android.opengl.EGL14.eglDestroyContext(this.mEglDisplay, this.mEglContext);
        this.mEglContext = null;
    }

    private boolean createEglSurface(boolean isProtected, boolean isWideColor) {
        boolean didContentAttributesChange = (isProtected == this.mLastWasProtectedContent && isWideColor == this.mLastWasWideColor) ? false : true;
        if (this.mEglSurface != null && didContentAttributesChange) {
            android.opengl.EGL14.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
            this.mEglSurface = null;
        }
        if (this.mSurface == null) {
            android.util.Slog.e(TAG, "createEglSurface but surface is null, please check dismiss()");
            return false;
        }
        if (this.mEglSurface == null) {
            int[] eglSurfaceAttribList = {12344, 12344, 12344, 12344, 12344};
            int index = 0;
            if (isWideColor) {
                int index2 = 0 + 1;
                eglSurfaceAttribList[0] = EGL_GL_COLORSPACE_KHR;
                index = index2 + 1;
                eglSurfaceAttribList[index2] = EGL_GL_COLORSPACE_DISPLAY_P3_PASSTHROUGH_EXT;
            }
            if (isProtected) {
                eglSurfaceAttribList[index] = EGL_PROTECTED_CONTENT_EXT;
                eglSurfaceAttribList[index + 1] = 1;
            }
            this.mEglSurface = android.opengl.EGL14.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, this.mSurface, eglSurfaceAttribList, 0);
            if (this.mEglSurface == null) {
                logEglError("eglCreateWindowSurface");
                return false;
            }
        }
        return true;
    }

    private void destroyEglSurface() {
        if (this.mEglSurface != null) {
            if (!android.opengl.EGL14.eglDestroySurface(this.mEglDisplay, this.mEglSurface)) {
                logEglError("eglDestroySurface");
            }
            this.mEglSurface = null;
        }
    }

    private void destroySurface() {
        if (this.mSurfaceControl != null) {
            this.mSurfaceLayout.dispose();
            this.mSurfaceLayout = null;
            this.mTransaction.hide(this.mSurfaceControl).apply();
            this.mTransaction.remove(this.mSurfaceControl).apply();
            if (this.mSurface != null) {
                this.mSurface.release();
                this.mSurface = null;
            }
            if (this.mBLASTSurfaceControl != null) {
                this.mBLASTSurfaceControl.release();
                this.mBLASTSurfaceControl = null;
                this.mBLASTBufferQueue.destroy();
                this.mBLASTBufferQueue = null;
            }
            this.mSurfaceControl = null;
            this.mSurfaceVisible = false;
            this.mSurfaceAlpha = 0.0f;
        }
    }

    private boolean showSurface(float alpha) {
        if (!this.mSurfaceVisible || this.mSurfaceAlpha != alpha) {
            this.mTransaction.setLayer(this.mSurfaceControl, 1073741825).setAlpha(this.mSurfaceControl, alpha).show(this.mSurfaceControl).apply();
            this.mSurfaceVisible = true;
            this.mSurfaceAlpha = alpha;
        }
        return true;
    }

    private boolean attachEglContext() {
        if (this.mEglSurface == null || this.mEglDisplay == null || this.mEglContext == null) {
            return false;
        }
        if (!android.opengl.EGL14.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext)) {
            logEglError("eglMakeCurrent");
            return false;
        }
        return true;
    }

    private void detachEglContext() {
        if (this.mEglDisplay != null) {
            android.opengl.EGL14.eglMakeCurrent(this.mEglDisplay, android.opengl.EGL14.EGL_NO_SURFACE, android.opengl.EGL14.EGL_NO_SURFACE, android.opengl.EGL14.EGL_NO_CONTEXT);
        }
    }

    private void destroyEglContext() {
        if (this.mEglDisplay != null && this.mEglContext != null) {
            android.opengl.EGL14.eglDestroyContext(this.mEglDisplay, this.mEglContext);
        }
    }

    private static java.nio.FloatBuffer createNativeFloatBuffer(int size) {
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocateDirect(size * 4);
        bb.order(java.nio.ByteOrder.nativeOrder());
        return bb.asFloatBuffer();
    }

    private static void logEglError(java.lang.String func) {
        android.util.Slog.e(TAG, func + " failed: error " + android.opengl.EGL14.eglGetError(), new java.lang.Throwable());
    }

    private static boolean checkGlErrors(java.lang.String func) {
        return checkGlErrors(func, true);
    }

    private static boolean checkGlErrors(java.lang.String func, boolean log) {
        boolean hadError = false;
        while (true) {
            int error = android.opengl.GLES20.glGetError();
            if (error != 0) {
                if (log) {
                    android.util.Slog.e(TAG, func + " failed: error " + error, new java.lang.Throwable());
                }
                hadError = true;
            } else {
                return hadError;
            }
        }
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println();
        pw.println("Color Fade State:");
        pw.println("  mPrepared=" + this.mPrepared);
        pw.println("  mMode=" + this.mMode);
        pw.println("  mDisplayLayerStack=" + this.mDisplayLayerStack);
        pw.println("  mDisplayWidth=" + this.mDisplayWidth);
        pw.println("  mDisplayHeight=" + this.mDisplayHeight);
        pw.println("  mSurfaceVisible=" + this.mSurfaceVisible);
        pw.println("  mSurfaceAlpha=" + this.mSurfaceAlpha);
    }

    private static final class NaturalSurfaceLayout implements android.hardware.display.DisplayManagerInternal.DisplayTransactionListener {
        private final int mDisplayId;
        private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
        private android.view.SurfaceControl mSurfaceControl;

        public NaturalSurfaceLayout(android.hardware.display.DisplayManagerInternal displayManagerInternal, int displayId, android.view.SurfaceControl surfaceControl) {
            this.mDisplayManagerInternal = displayManagerInternal;
            this.mDisplayId = displayId;
            this.mSurfaceControl = surfaceControl;
            this.mDisplayManagerInternal.registerDisplayTransactionListener(this);
        }

        public void dispose() {
            synchronized (this) {
                this.mSurfaceControl = null;
            }
            this.mDisplayManagerInternal.unregisterDisplayTransactionListener(this);
        }

        public void onDisplayTransaction(android.view.SurfaceControl.Transaction t) {
            synchronized (this) {
                if (this.mSurfaceControl == null) {
                    return;
                }
                android.view.DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(this.mDisplayId);
                if (displayInfo == null) {
                    return;
                }
                switch (displayInfo.rotation) {
                    case 0:
                        t.setPosition(this.mSurfaceControl, 0.0f, 0.0f);
                        t.setMatrix(this.mSurfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
                        break;
                    case 1:
                        t.setPosition(this.mSurfaceControl, 0.0f, displayInfo.logicalHeight);
                        t.setMatrix(this.mSurfaceControl, 0.0f, -1.0f, 1.0f, 0.0f);
                        break;
                    case 2:
                        t.setPosition(this.mSurfaceControl, displayInfo.logicalWidth, displayInfo.logicalHeight);
                        t.setMatrix(this.mSurfaceControl, -1.0f, 0.0f, 0.0f, -1.0f);
                        break;
                    case 3:
                        t.setPosition(this.mSurfaceControl, displayInfo.logicalWidth, 0.0f);
                        t.setMatrix(this.mSurfaceControl, 0.0f, 1.0f, -1.0f, 0.0f);
                        break;
                }
            }
        }
    }
}
