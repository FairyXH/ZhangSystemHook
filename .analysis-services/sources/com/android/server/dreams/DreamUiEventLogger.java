package com.android.server.dreams;

/* JADX INFO: loaded from: classes2.dex */
public interface DreamUiEventLogger {
    void log(com.android.internal.logging.UiEventLogger.UiEventEnum uiEventEnum, java.lang.String str);

    public enum DreamUiEventEnum implements com.android.internal.logging.UiEventLogger.UiEventEnum {
        DREAM_START(577),
        DREAM_STOP(com.android.internal.util.FrameworkStatsLog.HOTWORD_AUDIO_EGRESS_EVENT_REPORTED);

        private final int mId;

        DreamUiEventEnum(int id) {
            this.mId = id;
        }

        public int getId() {
            return this.mId;
        }
    }
}
