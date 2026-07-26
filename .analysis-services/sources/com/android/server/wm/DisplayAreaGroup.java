package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DisplayAreaGroup extends com.android.server.wm.RootDisplayArea {
    DisplayAreaGroup(com.android.server.wm.WindowManagerService wms, java.lang.String name, int featureId) {
        super(wms, name, featureId);
    }

    @Override // com.android.server.wm.RootDisplayArea
    boolean isOrientationDifferentFromDisplay() {
        return isOrientationDifferentFromDisplay(getBounds());
    }

    private boolean isOrientationDifferentFromDisplay(android.graphics.Rect bounds) {
        if (this.mDisplayContent == null) {
            return false;
        }
        android.graphics.Rect displayBounds = this.mDisplayContent.getBounds();
        return (bounds.width() < bounds.height()) != (displayBounds.width() < displayBounds.height());
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    int getOrientation(int candidate) {
        int orientation = super.getOrientation(candidate);
        return isOrientationDifferentFromDisplay() ? android.content.pm.ActivityInfo.reverseOrientation(orientation) : orientation;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.ConfigurationContainer
    void resolveOverrideConfiguration(android.content.res.Configuration newParentConfiguration) {
        android.graphics.Rect overrideBounds;
        super.resolveOverrideConfiguration(newParentConfiguration);
        android.content.res.Configuration resolvedConfig = getResolvedOverrideConfiguration();
        if (resolvedConfig.orientation != 0) {
            return;
        }
        android.graphics.Rect overrideBounds2 = resolvedConfig.windowConfiguration.getBounds();
        if (overrideBounds2.isEmpty()) {
            overrideBounds = newParentConfiguration.windowConfiguration.getBounds();
        } else {
            overrideBounds = overrideBounds2;
        }
        if (isOrientationDifferentFromDisplay(overrideBounds)) {
            if (newParentConfiguration.orientation == 1) {
                resolvedConfig.orientation = 2;
            } else if (newParentConfiguration.orientation == 2) {
                resolvedConfig.orientation = 1;
            }
        }
    }
}
