package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
class GLHelper {
    private static final java.lang.String TAG = com.android.server.wallpaper.GLHelper.class.getSimpleName();
    private static final int sMaxTextureSize;

    GLHelper() {
    }

    static {
        int maxTextureSize = android.os.SystemProperties.getInt("sys.max_texture_size", 0);
        sMaxTextureSize = maxTextureSize > 0 ? maxTextureSize : retrieveTextureSizeFromGL();
    }

    private static int retrieveTextureSizeFromGL() {
        try {
            android.opengl.EGLDisplay eglDisplay = android.opengl.EGL14.eglGetDisplay(0);
            if (eglDisplay != null && eglDisplay != android.opengl.EGL14.EGL_NO_DISPLAY) {
                if (!android.opengl.EGL14.eglInitialize(eglDisplay, null, 0, null, 1)) {
                    java.lang.String err = "eglInitialize failed: " + android.opengl.GLUtils.getEGLErrorString(android.opengl.EGL14.eglGetError());
                    throw new java.lang.RuntimeException(err);
                }
                android.opengl.EGLConfig eglConfig = null;
                int[] configsCount = new int[1];
                android.opengl.EGLConfig[] configs = new android.opengl.EGLConfig[1];
                int[] configSpec = {12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12325, 0, 12326, 0, 12327, 12344, 12344};
                if (!android.opengl.EGL14.eglChooseConfig(eglDisplay, configSpec, 0, configs, 0, 1, configsCount, 0)) {
                    java.lang.String err2 = "eglChooseConfig failed: " + android.opengl.GLUtils.getEGLErrorString(android.opengl.EGL14.eglGetError());
                    throw new java.lang.RuntimeException(err2);
                }
                if (configsCount[0] > 0) {
                    eglConfig = configs[0];
                }
                if (eglConfig == null) {
                    throw new java.lang.RuntimeException("eglConfig not initialized!");
                }
                int[] attr_list = {12440, 2, 12344};
                android.opengl.EGLContext eglContext = android.opengl.EGL14.eglCreateContext(eglDisplay, eglConfig, android.opengl.EGL14.EGL_NO_CONTEXT, attr_list, 0);
                if (eglContext != null && eglContext != android.opengl.EGL14.EGL_NO_CONTEXT) {
                    int[] attrs = {12375, 1, 12374, 1, 12344};
                    android.opengl.EGLSurface eglSurface = android.opengl.EGL14.eglCreatePbufferSurface(eglDisplay, eglConfig, attrs, 0);
                    android.opengl.EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
                    int[] maxSize = new int[1];
                    android.opengl.GLES20.glGetIntegerv(3379, maxSize, 0);
                    android.opengl.EGL14.eglMakeCurrent(eglDisplay, android.opengl.EGL14.EGL_NO_SURFACE, android.opengl.EGL14.EGL_NO_SURFACE, android.opengl.EGL14.EGL_NO_CONTEXT);
                    android.opengl.EGL14.eglDestroySurface(eglDisplay, eglSurface);
                    android.opengl.EGL14.eglDestroyContext(eglDisplay, eglContext);
                    android.opengl.EGL14.eglTerminate(eglDisplay);
                    return maxSize[0];
                }
                java.lang.String err3 = "eglCreateContext failed: " + android.opengl.GLUtils.getEGLErrorString(android.opengl.EGL14.eglGetError());
                throw new java.lang.RuntimeException(err3);
            }
            java.lang.String err4 = "eglGetDisplay failed: " + android.opengl.GLUtils.getEGLErrorString(android.opengl.EGL14.eglGetError());
            throw new java.lang.RuntimeException(err4);
        } catch (java.lang.RuntimeException e) {
            android.util.Log.w(TAG, "Retrieve from GL failed", e);
            return Integer.MAX_VALUE;
        }
    }

    static int getMaxTextureSize() {
        return sMaxTextureSize;
    }
}
