package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public abstract class ExtconStateObserver<S> extends com.android.server.ExtconUEventObserver {
    private static final boolean LOG = false;
    private static final java.lang.String TAG = "ExtconStateObserver";

    public abstract S parseState(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo, java.lang.String str);

    public abstract void updateState(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo, java.lang.String str, S s);

    public S parseStateFromFile(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo) throws java.io.IOException {
        java.lang.String statePath = extconInfo.getStatePath();
        return parseState(extconInfo, android.os.FileUtils.readTextFile(new java.io.File(statePath), 0, null).trim());
    }

    @Override // com.android.server.ExtconUEventObserver
    public void onUEvent(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo, android.os.UEventObserver.UEvent event) {
        java.lang.String name = event.get("NAME");
        S state = parseState(extconInfo, event.get("STATE"));
        if (state != null) {
            updateState(extconInfo, name, state);
        }
    }
}
