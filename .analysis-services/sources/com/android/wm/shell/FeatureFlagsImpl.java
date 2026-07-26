package com.android.wm.shell;

/* JADX INFO: loaded from: classes3.dex */
public final class FeatureFlagsImpl implements com.android.wm.shell.FeatureFlags {
    @Override // com.android.wm.shell.FeatureFlags
    public boolean animateBubbleSizeChange() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableAppPairs() {
        return true;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableBubbleAnything() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableBubbleBar() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableBubbleStashing() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableBubblesLongPressNavHandle() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableLeftRightSplitInPortrait() {
        return true;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableNewBubbleAnimations() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableOptionalBubbleOverflow() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enablePip2Implementation() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enablePipUmoExperience() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableRetrievableBubbles() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableSplitContextual() {
        return true;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableTaskbarNavbarUnification() {
        return true;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableTinyTaskbar() {
        return false;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean onlyReuseBubbledTaskWhenLaunchedFromBubble() {
        return false;
    }
}
