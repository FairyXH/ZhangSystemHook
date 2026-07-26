package com.android.server.dreams;

/* JADX INFO: loaded from: classes2.dex */
public class DreamUiEventLoggerImpl implements com.android.server.dreams.DreamUiEventLogger {
    private final java.lang.String[] mLoggableDreamPrefixes;

    DreamUiEventLoggerImpl(java.lang.String[] loggableDreamPrefixes) {
        this.mLoggableDreamPrefixes = loggableDreamPrefixes;
    }

    @Override // com.android.server.dreams.DreamUiEventLogger
    public void log(com.android.internal.logging.UiEventLogger.UiEventEnum event, java.lang.String dreamComponentName) {
        int eventID = event.getId();
        if (eventID <= 0) {
            return;
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DREAM_UI_EVENT_REPORTED, 0, eventID, 0, isFirstPartyDream(dreamComponentName) ? dreamComponentName : "other");
    }

    private boolean isFirstPartyDream(java.lang.String dreamComponentName) {
        for (int i = 0; i < this.mLoggableDreamPrefixes.length; i++) {
            if (dreamComponentName.startsWith(this.mLoggableDreamPrefixes[i])) {
                return true;
            }
        }
        return false;
    }
}
