package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ImeTargetVisibilityPolicy {
    public abstract boolean removeImeScreenshot(int i);

    public abstract boolean showImeScreenshot(android.os.IBinder iBinder, int i);

    public static boolean canComputeImeParent(com.android.server.wm.WindowState imeLayeringTarget, com.android.server.wm.InputTarget imeInputTarget) {
        if (imeLayeringTarget == null) {
            return false;
        }
        if (shouldComputeImeParentForEmbeddedActivity(imeLayeringTarget, imeInputTarget)) {
            return true;
        }
        boolean imeLayeringTargetMayUseIme = android.view.WindowManager.LayoutParams.mayUseInputMethod(imeLayeringTarget.mAttrs.flags) || imeLayeringTarget.mAttrs.type == 3;
        boolean inputAndLayeringTargetsDisagree = (imeInputTarget == null || imeLayeringTarget.mActivityRecord == imeInputTarget.getActivityRecord()) ? false : true;
        boolean inputTargetStale = imeLayeringTargetMayUseIme && inputAndLayeringTargetsDisagree;
        return !inputTargetStale;
    }

    private static boolean shouldComputeImeParentForEmbeddedActivity(com.android.server.wm.WindowState imeLayeringTarget, com.android.server.wm.InputTarget imeInputTarget) {
        com.android.server.wm.WindowState inputTargetWindow;
        if (imeInputTarget == null || imeLayeringTarget == null || (inputTargetWindow = imeInputTarget.getWindowState()) == null || !imeLayeringTarget.isAttached() || !inputTargetWindow.isAttached()) {
            return false;
        }
        com.android.server.wm.ActivityRecord inputTargetRecord = imeInputTarget.getActivityRecord();
        com.android.server.wm.ActivityRecord layeringTargetRecord = imeLayeringTarget.getActivityRecord();
        return inputTargetRecord != null && layeringTargetRecord != null && inputTargetRecord != layeringTargetRecord && inputTargetRecord.getTask() == layeringTargetRecord.getTask() && inputTargetRecord.isEmbedded() && layeringTargetRecord.isEmbedded() && imeLayeringTarget.compareTo((com.android.server.wm.WindowContainer) inputTargetWindow) > 0;
    }
}
