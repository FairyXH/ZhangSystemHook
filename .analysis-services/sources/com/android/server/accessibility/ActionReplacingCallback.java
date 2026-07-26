package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class ActionReplacingCallback extends android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub {
    private static final boolean DEBUG = false;
    private static final java.lang.String LOG_TAG = "ActionReplacingCallback";
    private final android.view.accessibility.IAccessibilityInteractionConnection mConnectionWithReplacementActions;
    private final int mInteractionId;
    android.view.accessibility.AccessibilityNodeInfo mNodeFromOriginalWindow;
    android.view.accessibility.AccessibilityNodeInfo mNodeWithReplacementActions;
    private final int mNodeWithReplacementActionsInteractionId;
    java.util.List<android.view.accessibility.AccessibilityNodeInfo> mNodesFromOriginalWindow;
    java.util.List<android.view.accessibility.AccessibilityNodeInfo> mPrefetchedNodesFromOriginalWindow;
    private boolean mReplacementNodeIsReadyOrFailed;
    private final android.view.accessibility.IAccessibilityInteractionConnectionCallback mServiceCallback;
    private final java.lang.Object mLock = new java.lang.Object();
    boolean mSetFindNodeFromOriginalWindowCalled = false;
    boolean mSetFindNodesFromOriginalWindowCalled = false;
    boolean mSetPrefetchFromOriginalWindowCalled = false;

    public ActionReplacingCallback(android.view.accessibility.IAccessibilityInteractionConnectionCallback serviceCallback, android.view.accessibility.IAccessibilityInteractionConnection connectionWithReplacementActions, int interactionId, int interrogatingPid, long interrogatingTid) {
        this.mServiceCallback = serviceCallback;
        this.mConnectionWithReplacementActions = connectionWithReplacementActions;
        this.mInteractionId = interactionId;
        this.mNodeWithReplacementActionsInteractionId = interactionId + 1;
        long identityToken = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mConnectionWithReplacementActions.findAccessibilityNodeInfoByAccessibilityId(android.view.accessibility.AccessibilityNodeInfo.ROOT_NODE_ID, (android.graphics.Region) null, this.mNodeWithReplacementActionsInteractionId, this, 0, interrogatingPid, interrogatingTid, (android.view.MagnificationSpec) null, (float[]) null, (android.os.Bundle) null);
            } catch (android.os.RemoteException e) {
                this.mReplacementNodeIsReadyOrFailed = true;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identityToken);
        }
    }

    public void setFindAccessibilityNodeInfoResult(android.view.accessibility.AccessibilityNodeInfo info, int interactionId) {
        synchronized (this.mLock) {
            if (interactionId == this.mInteractionId) {
                this.mNodeFromOriginalWindow = info;
                this.mSetFindNodeFromOriginalWindowCalled = true;
            } else if (interactionId == this.mNodeWithReplacementActionsInteractionId) {
                this.mNodeWithReplacementActions = info;
                this.mReplacementNodeIsReadyOrFailed = true;
            } else {
                android.util.Slog.e(LOG_TAG, "Callback with unexpected interactionId");
                return;
            }
            replaceInfoActionsAndCallServiceIfReady();
        }
    }

    public void setFindAccessibilityNodeInfosResult(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos, int interactionId) {
        synchronized (this.mLock) {
            if (interactionId == this.mInteractionId) {
                this.mNodesFromOriginalWindow = infos;
                this.mSetFindNodesFromOriginalWindowCalled = true;
            } else if (interactionId == this.mNodeWithReplacementActionsInteractionId) {
                setNodeWithReplacementActionsFromList(infos);
                this.mReplacementNodeIsReadyOrFailed = true;
            } else {
                android.util.Slog.e(LOG_TAG, "Callback with unexpected interactionId");
                return;
            }
            replaceInfoActionsAndCallServiceIfReady();
        }
    }

    public void setPrefetchAccessibilityNodeInfoResult(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos, int interactionId) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (interactionId == this.mInteractionId) {
                this.mPrefetchedNodesFromOriginalWindow = infos;
                this.mSetPrefetchFromOriginalWindowCalled = true;
                replaceInfoActionsAndCallServiceIfReady();
                return;
            }
            android.util.Slog.e(LOG_TAG, "Callback with unexpected interactionId");
        }
    }

    private void replaceInfoActionsAndCallServiceIfReady() {
        replaceInfoActionsAndCallService();
        replaceInfosActionsAndCallService();
        replacePrefetchInfosActionsAndCallService();
    }

    private void setNodeWithReplacementActionsFromList(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos) {
        for (int i = 0; i < infos.size(); i++) {
            android.view.accessibility.AccessibilityNodeInfo info = infos.get(i);
            if (info.getSourceNodeId() == android.view.accessibility.AccessibilityNodeInfo.ROOT_NODE_ID) {
                this.mNodeWithReplacementActions = info;
            }
        }
    }

    public void setPerformAccessibilityActionResult(boolean succeeded, int interactionId) throws android.os.RemoteException {
        this.mServiceCallback.setPerformAccessibilityActionResult(succeeded, interactionId);
    }

    public void sendTakeScreenshotOfWindowError(int errorCode, int interactionId) throws android.os.RemoteException {
        this.mServiceCallback.sendTakeScreenshotOfWindowError(errorCode, interactionId);
    }

    private void replaceInfoActionsAndCallService() {
        boolean doCallback;
        android.view.accessibility.AccessibilityNodeInfo nodeToReturn;
        synchronized (this.mLock) {
            doCallback = this.mReplacementNodeIsReadyOrFailed && this.mSetFindNodeFromOriginalWindowCalled;
            if (doCallback && this.mNodeFromOriginalWindow != null) {
                replaceActionsOnInfoLocked(this.mNodeFromOriginalWindow);
                this.mSetFindNodeFromOriginalWindowCalled = false;
            }
            nodeToReturn = this.mNodeFromOriginalWindow;
        }
        if (doCallback) {
            try {
                this.mServiceCallback.setFindAccessibilityNodeInfoResult(nodeToReturn, this.mInteractionId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void replaceInfosActionsAndCallService() {
        boolean doCallback;
        java.util.List<android.view.accessibility.AccessibilityNodeInfo> nodesToReturn = null;
        synchronized (this.mLock) {
            doCallback = this.mReplacementNodeIsReadyOrFailed && this.mSetFindNodesFromOriginalWindowCalled;
            if (doCallback) {
                nodesToReturn = replaceActionsLocked(this.mNodesFromOriginalWindow);
                this.mSetFindNodesFromOriginalWindowCalled = false;
            }
        }
        if (doCallback) {
            try {
                this.mServiceCallback.setFindAccessibilityNodeInfosResult(nodesToReturn, this.mInteractionId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void replacePrefetchInfosActionsAndCallService() {
        boolean doCallback;
        java.util.List<android.view.accessibility.AccessibilityNodeInfo> nodesToReturn = null;
        synchronized (this.mLock) {
            doCallback = this.mReplacementNodeIsReadyOrFailed && this.mSetPrefetchFromOriginalWindowCalled;
            if (doCallback) {
                nodesToReturn = replaceActionsLocked(this.mPrefetchedNodesFromOriginalWindow);
                this.mSetPrefetchFromOriginalWindowCalled = false;
            }
        }
        if (doCallback) {
            try {
                this.mServiceCallback.setPrefetchAccessibilityNodeInfoResult(nodesToReturn, this.mInteractionId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private java.util.List<android.view.accessibility.AccessibilityNodeInfo> replaceActionsLocked(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos) {
        if (infos != null) {
            for (int i = 0; i < infos.size(); i++) {
                replaceActionsOnInfoLocked(infos.get(i));
            }
        }
        if (infos == null) {
            return null;
        }
        return new java.util.ArrayList(infos);
    }

    private void replaceActionsOnInfoLocked(android.view.accessibility.AccessibilityNodeInfo info) {
        info.removeAllActions();
        info.setClickable(false);
        info.setFocusable(false);
        info.setContextClickable(false);
        info.setScrollable(false);
        info.setLongClickable(false);
        info.setDismissable(false);
        if (info.getSourceNodeId() == android.view.accessibility.AccessibilityNodeInfo.ROOT_NODE_ID && this.mNodeWithReplacementActions != null) {
            java.util.List<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> actions = this.mNodeWithReplacementActions.getActionList();
            if (actions != null) {
                for (int j = 0; j < actions.size(); j++) {
                    info.addAction(actions.get(j));
                }
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS);
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            }
            info.setClickable(this.mNodeWithReplacementActions.isClickable());
            info.setFocusable(this.mNodeWithReplacementActions.isFocusable());
            info.setContextClickable(this.mNodeWithReplacementActions.isContextClickable());
            info.setScrollable(this.mNodeWithReplacementActions.isScrollable());
            info.setLongClickable(this.mNodeWithReplacementActions.isLongClickable());
            info.setDismissable(this.mNodeWithReplacementActions.isDismissable());
        }
    }

    public void sendAttachOverlayResult(int result, int interactionId) throws android.os.RemoteException {
        this.mServiceCallback.sendAttachOverlayResult(result, interactionId);
    }
}
