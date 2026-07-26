package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class MagnificationProcessor {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "MagnificationProcessor";
    private final com.android.server.accessibility.magnification.MagnificationController mController;

    public MagnificationProcessor(com.android.server.accessibility.magnification.MagnificationController controller) {
        this.mController = controller;
    }

    public android.accessibilityservice.MagnificationConfig getMagnificationConfig(int displayId) {
        int mode = getControllingMode(displayId);
        android.accessibilityservice.MagnificationConfig.Builder builder = new android.accessibilityservice.MagnificationConfig.Builder();
        if (mode == 1) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController fullScreenMagnificationController = this.mController.getFullScreenMagnificationController();
            builder.setMode(mode).setActivated(this.mController.isActivated(displayId, 1)).setScale(fullScreenMagnificationController.getScale(displayId)).setCenterX(fullScreenMagnificationController.getCenterX(displayId)).setCenterY(fullScreenMagnificationController.getCenterY(displayId));
        } else if (mode == 2) {
            com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager = this.mController.getMagnificationConnectionManager();
            builder.setMode(mode).setActivated(this.mController.isActivated(displayId, 2)).setScale(magnificationConnectionManager.getScale(displayId)).setCenterX(magnificationConnectionManager.getCenterX(displayId)).setCenterY(magnificationConnectionManager.getCenterY(displayId));
        } else {
            builder.setActivated(false);
        }
        return builder.build();
    }

    public boolean setMagnificationConfig(int displayId, android.accessibilityservice.MagnificationConfig config, boolean animate, int id) {
        if (transitionModeIfNeeded(displayId, config, animate, id)) {
            return true;
        }
        int configMode = config.getMode();
        if (configMode == 0) {
            configMode = getControllingMode(displayId);
        }
        boolean configActivated = config.isActivated();
        if (configMode == 1) {
            if (configActivated) {
                return setScaleAndCenterForFullScreenMagnification(displayId, config.getScale(), config.getCenterX(), config.getCenterY(), animate, id);
            }
            return resetFullscreenMagnification(displayId, animate);
        }
        if (configMode != 2) {
            return false;
        }
        if (configActivated) {
            return this.mController.getMagnificationConnectionManager().enableWindowMagnification(displayId, config.getScale(), config.getCenterX(), config.getCenterY(), animate ? android.view.accessibility.MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK : null, id);
        }
        return this.mController.getMagnificationConnectionManager().disableWindowMagnification(displayId, false);
    }

    private boolean setScaleAndCenterForFullScreenMagnification(int displayId, float scale, float centerX, float centerY, boolean animate, int id) {
        if (!isRegistered(displayId)) {
            register(displayId);
        }
        return this.mController.getFullScreenMagnificationController().setScaleAndCenter(displayId, scale, centerX, centerY, animate, id);
    }

    private boolean transitionModeIfNeeded(int displayId, android.accessibilityservice.MagnificationConfig config, boolean animate, int id) {
        int currentMode = getControllingMode(displayId);
        if (config.getMode() == 0) {
            return false;
        }
        if (currentMode == config.getMode() && !this.mController.hasDisableMagnificationCallback(displayId)) {
            return false;
        }
        this.mController.transitionMagnificationConfigMode(displayId, config, animate, id);
        return true;
    }

    public float getScale(int displayId) {
        return this.mController.getFullScreenMagnificationController().getScale(displayId);
    }

    public float getCenterX(int displayId, boolean canControlMagnification) {
        boolean registeredJustForThisCall = registerDisplayMagnificationIfNeeded(displayId, canControlMagnification);
        try {
            return this.mController.getFullScreenMagnificationController().getCenterX(displayId);
        } finally {
            if (registeredJustForThisCall) {
                unregister(displayId);
            }
        }
    }

    public float getCenterY(int displayId, boolean canControlMagnification) {
        boolean registeredJustForThisCall = registerDisplayMagnificationIfNeeded(displayId, canControlMagnification);
        try {
            return this.mController.getFullScreenMagnificationController().getCenterY(displayId);
        } finally {
            if (registeredJustForThisCall) {
                unregister(displayId);
            }
        }
    }

    public void getCurrentMagnificationRegion(int displayId, android.graphics.Region outRegion, boolean canControlMagnification) {
        int currentMode = getControllingMode(displayId);
        if (currentMode == 1) {
            getFullscreenMagnificationRegion(displayId, outRegion, canControlMagnification);
        } else if (currentMode == 2) {
            this.mController.getMagnificationConnectionManager().getMagnificationSourceBounds(displayId, outRegion);
        }
    }

    public void getFullscreenMagnificationRegion(int displayId, android.graphics.Region outRegion, boolean canControlMagnification) {
        boolean registeredJustForThisCall = registerDisplayMagnificationIfNeeded(displayId, canControlMagnification);
        try {
            this.mController.getFullScreenMagnificationController().getMagnificationRegion(displayId, outRegion);
        } finally {
            if (registeredJustForThisCall) {
                unregister(displayId);
            }
        }
    }

    public boolean resetCurrentMagnification(int displayId, boolean animate) {
        int mode = getControllingMode(displayId);
        if (mode == 1) {
            return this.mController.getFullScreenMagnificationController().reset(displayId, animate);
        }
        if (mode == 2) {
            return this.mController.getMagnificationConnectionManager().disableWindowMagnification(displayId, false, animate ? android.view.accessibility.MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK : null);
        }
        return false;
    }

    public boolean resetFullscreenMagnification(int displayId, boolean animate) {
        return this.mController.getFullScreenMagnificationController().reset(displayId, animate);
    }

    public void resetAllIfNeeded(int connectionId) {
        this.mController.getFullScreenMagnificationController().resetAllIfNeeded(connectionId);
        this.mController.getMagnificationConnectionManager().resetAllIfNeeded(connectionId);
    }

    public boolean isMagnifying(int displayId) {
        int mode = getControllingMode(displayId);
        if (mode == 1) {
            return this.mController.getFullScreenMagnificationController().isActivated(displayId);
        }
        if (mode == 2) {
            return this.mController.getMagnificationConnectionManager().isWindowMagnifierEnabled(displayId);
        }
        return false;
    }

    public int getControllingMode(int displayId) {
        if (this.mController.isActivated(displayId, 2)) {
            return 2;
        }
        return (!this.mController.isActivated(displayId, 1) && this.mController.getLastMagnificationActivatedMode(displayId) == 2) ? 2 : 1;
    }

    private boolean registerDisplayMagnificationIfNeeded(int displayId, boolean canControlMagnification) {
        if (!isRegistered(displayId) && canControlMagnification) {
            register(displayId);
            return true;
        }
        return false;
    }

    private boolean isRegistered(int displayId) {
        return this.mController.getFullScreenMagnificationController().isRegistered(displayId);
    }

    private void register(int displayId) {
        this.mController.getFullScreenMagnificationController().register(displayId);
    }

    private void unregister(int displayId) {
        this.mController.getFullScreenMagnificationController().unregister(displayId);
    }

    public void dump(java.io.PrintWriter pw, java.util.ArrayList<android.view.Display> displaysList) {
        for (int i = 0; i < displaysList.size(); i++) {
            int displayId = displaysList.get(i).getDisplayId();
            android.accessibilityservice.MagnificationConfig config = getMagnificationConfig(displayId);
            pw.println("Magnifier on display#" + displayId);
            pw.append((java.lang.CharSequence) ("    " + config)).println();
            android.graphics.Region region = new android.graphics.Region();
            getCurrentMagnificationRegion(displayId, region, true);
            if (!region.isEmpty()) {
                pw.append("    Magnification region=").append((java.lang.CharSequence) region.toString()).println();
            }
            pw.append((java.lang.CharSequence) ("    IdOfLastServiceToMagnify=" + getIdOfLastServiceToMagnify(config.getMode(), displayId))).println();
            dumpTrackingTypingFocusEnabledState(pw, displayId, config.getMode());
        }
        pw.append((java.lang.CharSequence) ("    SupportWindowMagnification=" + this.mController.supportWindowMagnification())).println();
        pw.append((java.lang.CharSequence) ("    WindowMagnificationConnectionState=" + this.mController.getMagnificationConnectionManager().getConnectionState())).println();
    }

    private int getIdOfLastServiceToMagnify(int mode, int displayId) {
        if (mode == 1) {
            return this.mController.getFullScreenMagnificationController().getIdOfLastServiceToMagnify(displayId);
        }
        return this.mController.getMagnificationConnectionManager().getIdOfLastServiceToMagnify(displayId);
    }

    private void dumpTrackingTypingFocusEnabledState(java.io.PrintWriter pw, int displayId, int mode) {
        if (mode == 2) {
            pw.append((java.lang.CharSequence) ("    TrackingTypingFocusEnabled=" + this.mController.getMagnificationConnectionManager().isTrackingTypingFocusEnabled(displayId))).println();
        }
    }
}
