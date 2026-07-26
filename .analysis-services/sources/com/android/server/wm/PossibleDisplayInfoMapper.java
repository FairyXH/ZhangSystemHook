package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class PossibleDisplayInfoMapper {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "PossibleDisplayInfoMapper";
    private final android.util.SparseArray<java.util.Set<android.view.DisplayInfo>> mDisplayInfos = new android.util.SparseArray<>();
    private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;

    PossibleDisplayInfoMapper(android.hardware.display.DisplayManagerInternal displayManagerInternal) {
        this.mDisplayManagerInternal = displayManagerInternal;
    }

    public java.util.List<android.view.DisplayInfo> getPossibleDisplayInfos(int displayId) {
        updatePossibleDisplayInfos(displayId);
        if (!this.mDisplayInfos.contains(displayId)) {
            return new java.util.ArrayList();
        }
        return java.util.List.copyOf(this.mDisplayInfos.get(displayId));
    }

    public void updatePossibleDisplayInfos(int displayId) {
        java.util.Set<android.view.DisplayInfo> displayInfos = this.mDisplayManagerInternal.getPossibleDisplayInfo(displayId);
        updateDisplayInfos(displayInfos);
    }

    public void removePossibleDisplayInfos(int displayId) {
        this.mDisplayInfos.remove(displayId);
    }

    private void updateDisplayInfos(java.util.Set<android.view.DisplayInfo> displayInfos) {
        this.mDisplayInfos.clear();
        for (android.view.DisplayInfo di : displayInfos) {
            java.util.Set<android.view.DisplayInfo> priorDisplayInfos = this.mDisplayInfos.get(di.displayId, new android.util.ArraySet());
            priorDisplayInfos.add(di);
            this.mDisplayInfos.put(di.displayId, priorDisplayInfos);
        }
    }
}
