package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class MagnificationScaleProvider {
    protected static final float DEFAULT_MAGNIFICATION_SCALE = 2.0f;
    public static final float MAX_SCALE = com.android.internal.accessibility.common.MagnificationConstants.SCALE_MAX_VALUE;
    public static final float MIN_SCALE = 1.0f;
    private final android.content.Context mContext;
    private final android.util.SparseArray<android.util.SparseArray<java.lang.Float>> mUsersScales = new android.util.SparseArray<>();
    private int mCurrentUserId = 0;
    private final java.lang.Object mLock = new java.lang.Object();

    public MagnificationScaleProvider(android.content.Context context) {
        this.mContext = context;
    }

    void putScale(final float scale, int displayId) {
        if (displayId == 0) {
            com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationScaleProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$putScale$0(scale);
                }
            });
            return;
        }
        synchronized (this.mLock) {
            getScalesWithCurrentUser().put(displayId, java.lang.Float.valueOf(scale));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putScale$0(float scale) {
        android.provider.Settings.Secure.putFloatForUser(this.mContext.getContentResolver(), "accessibility_display_magnification_scale", scale, this.mCurrentUserId);
    }

    float getScale(int displayId) {
        float fFloatValue;
        if (displayId == 0) {
            return android.provider.Settings.Secure.getFloatForUser(this.mContext.getContentResolver(), "accessibility_display_magnification_scale", DEFAULT_MAGNIFICATION_SCALE, this.mCurrentUserId);
        }
        synchronized (this.mLock) {
            fFloatValue = getScalesWithCurrentUser().get(displayId, java.lang.Float.valueOf(DEFAULT_MAGNIFICATION_SCALE)).floatValue();
        }
        return fFloatValue;
    }

    private android.util.SparseArray<java.lang.Float> getScalesWithCurrentUser() {
        android.util.SparseArray<java.lang.Float> scales = this.mUsersScales.get(this.mCurrentUserId);
        if (scales == null) {
            android.util.SparseArray<java.lang.Float> scales2 = new android.util.SparseArray<>();
            this.mUsersScales.put(this.mCurrentUserId, scales2);
            return scales2;
        }
        return scales;
    }

    void onUserChanged(int userId) {
        synchronized (this.mLock) {
            this.mCurrentUserId = userId;
        }
    }

    void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            this.mUsersScales.remove(userId);
        }
    }

    void onDisplayRemoved(int displayId) {
        synchronized (this.mLock) {
            int userCounts = this.mUsersScales.size();
            for (int i = userCounts - 1; i >= 0; i--) {
                this.mUsersScales.get(i).remove(displayId);
            }
        }
    }

    public java.lang.String toString() {
        java.lang.String str;
        synchronized (this.mLock) {
            str = "MagnificationScaleProvider{mCurrentUserId=" + this.mCurrentUserId + "Scale on the default display=" + getScale(0) + "Scales on non-default displays=" + getScalesWithCurrentUser() + '}';
        }
        return str;
    }

    static float constrainScale(float scale) {
        return android.util.MathUtils.constrain(scale, 1.0f, com.android.internal.accessibility.common.MagnificationConstants.SCALE_MAX_VALUE);
    }
}
