package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MediaKeyDispatcher {
    static final int KEY_EVENT_DOUBLE_TAP = 2;
    static final int KEY_EVENT_LONG_PRESS = 8;
    static final int KEY_EVENT_SINGLE_TAP = 1;
    static final int KEY_EVENT_TRIPLE_TAP = 4;
    private java.util.Map<java.lang.Integer, java.lang.Integer> mOverriddenKeyEvents = new java.util.HashMap();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface KeyEventType {
    }

    public MediaKeyDispatcher(android.content.Context context) {
        this.mOverriddenKeyEvents.put(126, 0);
        this.mOverriddenKeyEvents.put(127, 0);
        this.mOverriddenKeyEvents.put(85, 0);
        this.mOverriddenKeyEvents.put(91, 0);
        this.mOverriddenKeyEvents.put(79, 0);
        this.mOverriddenKeyEvents.put(86, 0);
        this.mOverriddenKeyEvents.put(87, 0);
        this.mOverriddenKeyEvents.put(88, 0);
        this.mOverriddenKeyEvents.put(25, 0);
        this.mOverriddenKeyEvents.put(24, 0);
        this.mOverriddenKeyEvents.put(164, 0);
    }

    android.media.session.MediaSession.Token getMediaSession(android.view.KeyEvent keyEvent, int uid, boolean asSystemService) {
        return null;
    }

    android.app.PendingIntent getMediaButtonReceiver(android.view.KeyEvent keyEvent, int uid, boolean asSystemService) {
        return null;
    }

    java.util.Map<java.lang.Integer, java.lang.Integer> getOverriddenKeyEvents() {
        return this.mOverriddenKeyEvents;
    }

    static boolean isSingleTapOverridden(int overriddenKeyEvents) {
        return (overriddenKeyEvents & 1) != 0;
    }

    static boolean isDoubleTapOverridden(int overriddenKeyEvents) {
        return (overriddenKeyEvents & 2) != 0;
    }

    static boolean isTripleTapOverridden(int overriddenKeyEvents) {
        return (overriddenKeyEvents & 4) != 0;
    }

    static boolean isLongPressOverridden(int overriddenKeyEvents) {
        return (overriddenKeyEvents & 8) != 0;
    }

    void setOverriddenKeyEvents(int keyCode, int keyEventType) {
        this.mOverriddenKeyEvents.put(java.lang.Integer.valueOf(keyCode), java.lang.Integer.valueOf(keyEventType));
    }

    void onSingleTap(android.view.KeyEvent keyEvent) {
    }

    void onDoubleTap(android.view.KeyEvent keyEvent) {
    }

    void onTripleTap(android.view.KeyEvent keyEvent) {
    }

    void onLongPress(android.view.KeyEvent keyEvent) {
    }
}
