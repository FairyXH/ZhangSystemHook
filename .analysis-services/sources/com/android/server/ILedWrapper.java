package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface ILedWrapper {
    default com.android.server.ILedExt getExtImpl() {
        return new com.android.server.ILedExt() { // from class: com.android.server.ILedWrapper.1
        };
    }

    default com.android.server.lights.LogicalLight getBatteryLight() {
        return new com.android.server.lights.LogicalLight() { // from class: com.android.server.ILedWrapper.2
            @Override // com.android.server.lights.LogicalLight
            public void setBrightness(float brightness) {
            }

            @Override // com.android.server.lights.LogicalLight
            public void setBrightness(float brightness, int brightnessMode) {
            }

            @Override // com.android.server.lights.LogicalLight
            public void setColor(int color) {
            }

            @Override // com.android.server.lights.LogicalLight
            public void setFlashing(int color, int mode, int onMS, int offMS) {
            }

            @Override // com.android.server.lights.LogicalLight
            public void pulse() {
            }

            @Override // com.android.server.lights.LogicalLight
            public void pulse(int color, int onMS) {
            }

            @Override // com.android.server.lights.LogicalLight
            public void turnOff() {
            }

            @Override // com.android.server.lights.LogicalLight
            public void setVrMode(boolean enabled) {
            }
        };
    }
}
