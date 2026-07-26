package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public final class GlobalKeyIntent {
    private static final java.lang.String EXTRA_BEGAN_FROM_NON_INTERACTIVE = "EXTRA_BEGAN_FROM_NON_INTERACTIVE";
    private final boolean mBeganFromNonInteractive;
    private final android.content.ComponentName mComponentName;
    private final android.view.KeyEvent mKeyEvent;

    GlobalKeyIntent(android.content.ComponentName componentName, android.view.KeyEvent event, boolean beganFromNonInteractive) {
        this.mComponentName = componentName;
        this.mKeyEvent = new android.view.KeyEvent(event);
        this.mBeganFromNonInteractive = beganFromNonInteractive;
    }

    android.content.Intent getIntent() {
        android.content.Intent intent = new android.content.Intent("android.intent.action.GLOBAL_BUTTON").setComponent(this.mComponentName).setFlags(268435456).putExtra("android.intent.extra.KEY_EVENT", this.mKeyEvent).putExtra(EXTRA_BEGAN_FROM_NON_INTERACTIVE, this.mBeganFromNonInteractive);
        return intent;
    }

    public android.view.KeyEvent getKeyEvent() {
        return this.mKeyEvent;
    }

    public boolean beganFromNonInteractive() {
        return this.mBeganFromNonInteractive;
    }

    public static com.android.server.policy.GlobalKeyIntent from(android.content.Intent intent) {
        if (intent.getAction() != "android.intent.action.GLOBAL_BUTTON") {
            android.util.Log.wtf("GlobalKeyIntent", "Intent should be ACTION_GLOBAL_BUTTON");
            return null;
        }
        android.view.KeyEvent event = (android.view.KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT", android.view.KeyEvent.class);
        boolean fromNonInteractive = intent.getBooleanExtra(EXTRA_BEGAN_FROM_NON_INTERACTIVE, false);
        return new com.android.server.policy.GlobalKeyIntent(intent.getComponent(), event, fromNonInteractive);
    }
}
