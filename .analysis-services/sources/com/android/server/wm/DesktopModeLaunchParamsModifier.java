package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DesktopModeLaunchParamsModifier implements com.android.server.wm.LaunchParamsController.LaunchParamsModifier {
    private static final boolean DEBUG = false;
    public static final float DESKTOP_MODE_INITIAL_BOUNDS_SCALE = android.os.SystemProperties.getInt("persist.wm.debug.desktop_mode_initial_bounds_scale", 75) / 100.0f;
    private static final boolean ENFORCE_DEVICE_RESTRICTIONS = android.os.SystemProperties.getBoolean("persist.wm.debug.desktop_mode_enforce_device_restrictions", true);
    private static final java.lang.String TAG = "ActivityTaskManager";
    private final android.content.Context mContext;
    private java.lang.StringBuilder mLogBuilder;

    DesktopModeLaunchParamsModifier(android.content.Context context) {
        this.mContext = context;
    }

    @Override // com.android.server.wm.LaunchParamsController.LaunchParamsModifier
    public int onCalculate(com.android.server.wm.Task task, android.content.pm.ActivityInfo.WindowLayout layout, com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord source, android.app.ActivityOptions options, com.android.server.wm.ActivityStarter.Request request, int phase, com.android.server.wm.LaunchParamsController.LaunchParams currentParams, com.android.server.wm.LaunchParamsController.LaunchParams outParams) {
        initLogBuilder(task, activity);
        int result = calculate(task, layout, activity, source, options, request, phase, currentParams, outParams);
        outputLog();
        return result;
    }

    private int calculate(com.android.server.wm.Task task, android.content.pm.ActivityInfo.WindowLayout layout, com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord source, android.app.ActivityOptions options, com.android.server.wm.ActivityStarter.Request request, int phase, com.android.server.wm.LaunchParamsController.LaunchParams currentParams, com.android.server.wm.LaunchParamsController.LaunchParams outParams) {
        if (!canEnterDesktopMode(this.mContext)) {
            appendLog("desktop mode is not enabled, skipping", new java.lang.Object[0]);
            return 0;
        }
        if (task == null) {
            appendLog("task null, skipping", new java.lang.Object[0]);
            return 0;
        }
        if (!task.isActivityTypeStandardOrUndefined()) {
            appendLog("not standard or undefined activity type, skipping", new java.lang.Object[0]);
            return 0;
        }
        if (phase < 1) {
            appendLog("not in windowing mode or bounds phase, skipping", new java.lang.Object[0]);
            return 0;
        }
        outParams.set(currentParams);
        if (source != null && source.getTask() != null) {
            com.android.server.wm.Task sourceTask = source.getTask();
            outParams.mWindowingMode = sourceTask.getWindowingMode();
            appendLog("inherit-from-source=" + outParams.mWindowingMode, new java.lang.Object[0]);
        }
        if (phase == 1) {
            return 2;
        }
        if (!currentParams.mBounds.isEmpty()) {
            appendLog("currentParams has bounds set, not overriding", new java.lang.Object[0]);
            return 0;
        }
        android.graphics.Rect stableBounds = new android.graphics.Rect();
        task.getDisplayArea().getStableRect(stableBounds);
        int desiredWidth = (int) (stableBounds.width() * DESKTOP_MODE_INITIAL_BOUNDS_SCALE);
        int desiredHeight = (int) (stableBounds.height() * DESKTOP_MODE_INITIAL_BOUNDS_SCALE);
        if (options != null && options.getLaunchBounds() != null) {
            outParams.mBounds.set(options.getLaunchBounds());
            appendLog("inherit-from-options=" + outParams.mBounds, new java.lang.Object[0]);
        } else if (layout != null) {
            int verticalGravity = layout.gravity & 112;
            int horizontalGravity = layout.gravity & 7;
            if (layout.hasSpecifiedSize()) {
                com.android.server.wm.LaunchParamsUtil.calculateLayoutBounds(stableBounds, layout, outParams.mBounds, new android.util.Size(desiredWidth, desiredHeight));
                com.android.server.wm.LaunchParamsUtil.applyLayoutGravity(verticalGravity, horizontalGravity, outParams.mBounds, stableBounds);
                appendLog("layout specifies sizes, inheriting size and applying gravity", new java.lang.Object[0]);
            } else if (verticalGravity > 0 || horizontalGravity > 0) {
                calculateAndCentreInitialBounds(task, outParams);
                com.android.server.wm.LaunchParamsUtil.applyLayoutGravity(verticalGravity, horizontalGravity, outParams.mBounds, stableBounds);
                appendLog("layout specifies gravity, applying desired bounds and gravity", new java.lang.Object[0]);
            }
        } else {
            calculateAndCentreInitialBounds(task, outParams);
            appendLog("layout not specified, applying desired bounds", new java.lang.Object[0]);
        }
        appendLog("final desktop mode task bounds set to %s", outParams.mBounds);
        return 2;
    }

    private void calculateAndCentreInitialBounds(com.android.server.wm.Task task, com.android.server.wm.LaunchParamsController.LaunchParams outParams) {
        android.graphics.Rect stableBounds = new android.graphics.Rect();
        task.getDisplayArea().getStableRect(stableBounds);
        int desiredWidth = (int) (stableBounds.width() * DESKTOP_MODE_INITIAL_BOUNDS_SCALE);
        int desiredHeight = (int) (stableBounds.height() * DESKTOP_MODE_INITIAL_BOUNDS_SCALE);
        outParams.mBounds.right = desiredWidth;
        outParams.mBounds.bottom = desiredHeight;
        outParams.mBounds.offset(stableBounds.centerX() - outParams.mBounds.centerX(), stableBounds.centerY() - outParams.mBounds.centerY());
    }

    private void initLogBuilder(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord activity) {
    }

    private void appendLog(java.lang.String format, java.lang.Object... args) {
    }

    private void outputLog() {
    }

    static boolean isDesktopModeEnabled() {
        return com.android.window.flags.Flags.enableDesktopWindowingMode();
    }

    static boolean enforceDeviceRestrictions() {
        return ENFORCE_DEVICE_RESTRICTIONS;
    }

    static boolean isDesktopModeSupported(android.content.Context context) {
        return context.getResources().getBoolean(android.R.bool.config_guestUserAllowEphemeralStateChange);
    }

    static boolean canEnterDesktopMode(android.content.Context context) {
        return isDesktopModeEnabled() && (!enforceDeviceRestrictions() || isDesktopModeSupported(context));
    }
}
