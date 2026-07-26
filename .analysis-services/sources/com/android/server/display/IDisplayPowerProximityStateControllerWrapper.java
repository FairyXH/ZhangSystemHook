package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IDisplayPowerProximityStateControllerWrapper {
    default void setProximitySensorEnabled(boolean enable) {
    }

    default void setScreenOffBecauseOfProximity(boolean val) {
    }

    default void setWaitingForNegativeProximity(boolean val) {
    }

    default void handleCoverModeProximitySensorEvent(long time, boolean positive) {
    }

    default com.android.server.display.WakelockController getWakelockController() {
        return null;
    }
}
