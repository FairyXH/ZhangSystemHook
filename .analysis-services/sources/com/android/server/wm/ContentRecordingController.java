package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class ContentRecordingController {
    private android.view.ContentRecordingSession mSession = null;
    private com.android.server.wm.DisplayContent mDisplayContent = null;

    ContentRecordingController() {
    }

    android.view.ContentRecordingSession getContentRecordingSessionLocked() {
        return this.mSession;
    }

    void setContentRecordingSessionLocked(android.view.ContentRecordingSession incomingSession, com.android.server.wm.WindowManagerService wmService) {
        if (incomingSession != null && !android.view.ContentRecordingSession.isValid(incomingSession)) {
            return;
        }
        boolean hasSessionUpdatedWithConsent = (this.mSession == null || incomingSession == null || !this.mSession.isWaitingForConsent() || incomingSession.isWaitingForConsent()) ? false : true;
        if (android.view.ContentRecordingSession.isProjectionOnSameDisplay(this.mSession, incomingSession)) {
            if (hasSessionUpdatedWithConsent) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    long protoLogParam0 = incomingSession.getVirtualDisplayId();
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mSession.getVirtualDisplayId());
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -225319884529912382L, 1, "Content Recording: Accept session updating same display %d with granted consent, with an existing session %s", java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
                }
            } else {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    long protoLogParam02 = incomingSession.getVirtualDisplayId();
                    java.lang.String protoLogParam12 = java.lang.String.valueOf(this.mSession.getVirtualDisplayId());
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -5981322449150461244L, 1, "Content Recording: Ignoring session on same display %d, with an existing session %s", java.lang.Long.valueOf(protoLogParam02), protoLogParam12);
                    return;
                }
                return;
            }
        }
        com.android.server.wm.DisplayContent incomingDisplayContent = null;
        if (incomingSession != null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                long protoLogParam03 = incomingSession.getVirtualDisplayId();
                java.lang.String protoLogParam13 = java.lang.String.valueOf(this.mSession == null ? null : java.lang.Integer.valueOf(this.mSession.getVirtualDisplayId()));
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 4226710957373144819L, 1, "Content Recording: Handle incoming session on display %d, with a pre-existing session %s", java.lang.Long.valueOf(protoLogParam03), protoLogParam13);
            }
            incomingDisplayContent = wmService.mRoot.getDisplayContentOrCreate(incomingSession.getVirtualDisplayId());
            if (incomingDisplayContent == null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    long protoLogParam04 = incomingSession.getVirtualDisplayId();
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -1415855962859555663L, 1, "Content Recording: Incoming session on display %d can't be set since it is already null; the corresponding VirtualDisplay must have already been removed.", java.lang.Long.valueOf(protoLogParam04));
                    return;
                }
                return;
            }
            incomingDisplayContent.setContentRecordingSession(incomingSession);
            incomingDisplayContent.updateRecording();
        }
        if (this.mSession != null && !hasSessionUpdatedWithConsent && this.mSession == incomingSession) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                java.lang.String protoLogParam05 = java.lang.String.valueOf(this.mDisplayContent.getDisplayId());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -5750232782380780139L, 0, "Content Recording: Pause the recording session on display %s", protoLogParam05);
            }
            this.mDisplayContent.pauseRecording();
            this.mDisplayContent.setContentRecordingSession(null);
        }
        this.mDisplayContent = incomingDisplayContent;
        this.mSession = incomingSession;
    }
}
