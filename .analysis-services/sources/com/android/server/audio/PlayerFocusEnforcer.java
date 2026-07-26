package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface PlayerFocusEnforcer {
    boolean duckPlayers(com.android.server.audio.FocusRequester focusRequester, com.android.server.audio.FocusRequester focusRequester2, boolean z);

    boolean fadeOutPlayers(com.android.server.audio.FocusRequester focusRequester, com.android.server.audio.FocusRequester focusRequester2);

    void forgetUid(int i);

    long getFadeInDelayForOffendersMillis(android.media.AudioAttributes audioAttributes);

    long getFadeOutDurationMillis(android.media.AudioAttributes audioAttributes);

    void mutePlayersForCall(int[] iArr);

    void restoreVShapedPlayers(com.android.server.audio.FocusRequester focusRequester);

    boolean shouldEnforceFade();

    void unmutePlayersForCall();
}
