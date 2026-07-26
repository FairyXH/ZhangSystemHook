package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayInfoOverrides {
    public static final com.android.server.wm.utils.DisplayInfoOverrides.DisplayInfoFieldsUpdater WM_OVERRIDE_FIELDS = new com.android.server.wm.utils.DisplayInfoOverrides.DisplayInfoFieldsUpdater() { // from class: com.android.server.wm.utils.DisplayInfoOverrides$$ExternalSyntheticLambda0
        @Override // com.android.server.wm.utils.DisplayInfoOverrides.DisplayInfoFieldsUpdater
        public final void setFields(android.view.DisplayInfo displayInfo, android.view.DisplayInfo displayInfo2) {
            com.android.server.wm.utils.DisplayInfoOverrides.lambda$static$0(displayInfo, displayInfo2);
        }
    };

    public interface DisplayInfoFieldsUpdater {
        void setFields(android.view.DisplayInfo displayInfo, android.view.DisplayInfo displayInfo2);
    }

    static /* synthetic */ void lambda$static$0(android.view.DisplayInfo out, android.view.DisplayInfo source) {
        out.appWidth = source.appWidth;
        out.appHeight = source.appHeight;
        out.smallestNominalAppWidth = source.smallestNominalAppWidth;
        out.smallestNominalAppHeight = source.smallestNominalAppHeight;
        out.largestNominalAppWidth = source.largestNominalAppWidth;
        out.largestNominalAppHeight = source.largestNominalAppHeight;
        out.logicalWidth = source.logicalWidth;
        out.logicalHeight = source.logicalHeight;
        out.physicalXDpi = source.physicalXDpi;
        out.physicalYDpi = source.physicalYDpi;
        out.rotation = source.rotation;
        out.displayCutout = source.displayCutout;
        out.logicalDensityDpi = source.logicalDensityDpi;
        out.roundedCorners = source.roundedCorners;
        out.displayShape = source.displayShape;
    }

    public static void copyDisplayInfoFields(android.view.DisplayInfo out, android.view.DisplayInfo base, android.view.DisplayInfo override, com.android.server.wm.utils.DisplayInfoOverrides.DisplayInfoFieldsUpdater fields) {
        out.copyFrom(base);
        if (override != null) {
            fields.setFields(out, override);
        }
    }
}
