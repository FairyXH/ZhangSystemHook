package com.android.wm.shell;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.wm.shell.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.wm.shell.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.wm.shell.Flags.FLAG_ANIMATE_BUBBLE_SIZE_CHANGE, com.android.wm.shell.Flags.FLAG_ENABLE_APP_PAIRS, com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLE_ANYTHING, com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLE_BAR, com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLE_STASHING, com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLES_LONG_PRESS_NAV_HANDLE, com.android.wm.shell.Flags.FLAG_ENABLE_LEFT_RIGHT_SPLIT_IN_PORTRAIT, com.android.wm.shell.Flags.FLAG_ENABLE_NEW_BUBBLE_ANIMATIONS, com.android.wm.shell.Flags.FLAG_ENABLE_OPTIONAL_BUBBLE_OVERFLOW, com.android.wm.shell.Flags.FLAG_ENABLE_PIP2_IMPLEMENTATION, com.android.wm.shell.Flags.FLAG_ENABLE_PIP_UMO_EXPERIENCE, com.android.wm.shell.Flags.FLAG_ENABLE_RETRIEVABLE_BUBBLES, com.android.wm.shell.Flags.FLAG_ENABLE_SPLIT_CONTEXTUAL, com.android.wm.shell.Flags.FLAG_ENABLE_TASKBAR_NAVBAR_UNIFICATION, com.android.wm.shell.Flags.FLAG_ENABLE_TINY_TASKBAR, com.android.wm.shell.Flags.FLAG_ONLY_REUSE_BUBBLED_TASK_WHEN_LAUNCHED_FROM_BUBBLE, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.wm.shell.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean animateBubbleSizeChange() {
        return getValue(com.android.wm.shell.Flags.FLAG_ANIMATE_BUBBLE_SIZE_CHANGE, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).animateBubbleSizeChange();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableAppPairs() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_APP_PAIRS, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableAppPairs();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableBubbleAnything() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLE_ANYTHING, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableBubbleAnything();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableBubbleBar() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLE_BAR, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableBubbleBar();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableBubbleStashing() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLE_STASHING, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableBubbleStashing();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableBubblesLongPressNavHandle() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLES_LONG_PRESS_NAV_HANDLE, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableBubblesLongPressNavHandle();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableLeftRightSplitInPortrait() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_LEFT_RIGHT_SPLIT_IN_PORTRAIT, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableLeftRightSplitInPortrait();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableNewBubbleAnimations() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_NEW_BUBBLE_ANIMATIONS, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableNewBubbleAnimations();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableOptionalBubbleOverflow() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_OPTIONAL_BUBBLE_OVERFLOW, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableOptionalBubbleOverflow();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enablePip2Implementation() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_PIP2_IMPLEMENTATION, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enablePip2Implementation();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enablePipUmoExperience() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_PIP_UMO_EXPERIENCE, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enablePipUmoExperience();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableRetrievableBubbles() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_RETRIEVABLE_BUBBLES, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableRetrievableBubbles();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableSplitContextual() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_SPLIT_CONTEXTUAL, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableSplitContextual();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableTaskbarNavbarUnification() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_TASKBAR_NAVBAR_UNIFICATION, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableTaskbarNavbarUnification();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean enableTinyTaskbar() {
        return getValue(com.android.wm.shell.Flags.FLAG_ENABLE_TINY_TASKBAR, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).enableTinyTaskbar();
            }
        });
    }

    @Override // com.android.wm.shell.FeatureFlags
    public boolean onlyReuseBubbledTaskWhenLaunchedFromBubble() {
        return getValue(com.android.wm.shell.Flags.FLAG_ONLY_REUSE_BUBBLED_TASK_WHEN_LAUNCHED_FROM_BUBBLE, new java.util.function.Predicate() { // from class: com.android.wm.shell.CustomFeatureFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.wm.shell.FeatureFlags) obj).onlyReuseBubbledTaskWhenLaunchedFromBubble();
            }
        });
    }

    public boolean isFlagReadOnlyOptimized(java.lang.String flagName) {
        if (this.mReadOnlyFlagsSet.contains(flagName) && isOptimizationEnabled()) {
            return true;
        }
        return false;
    }

    private boolean isOptimizationEnabled() {
        return false;
    }

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.wm.shell.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.wm.shell.Flags.FLAG_ANIMATE_BUBBLE_SIZE_CHANGE, com.android.wm.shell.Flags.FLAG_ENABLE_APP_PAIRS, com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLE_ANYTHING, com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLE_BAR, com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLE_STASHING, com.android.wm.shell.Flags.FLAG_ENABLE_BUBBLES_LONG_PRESS_NAV_HANDLE, com.android.wm.shell.Flags.FLAG_ENABLE_LEFT_RIGHT_SPLIT_IN_PORTRAIT, com.android.wm.shell.Flags.FLAG_ENABLE_NEW_BUBBLE_ANIMATIONS, com.android.wm.shell.Flags.FLAG_ENABLE_OPTIONAL_BUBBLE_OVERFLOW, com.android.wm.shell.Flags.FLAG_ENABLE_PIP2_IMPLEMENTATION, com.android.wm.shell.Flags.FLAG_ENABLE_PIP_UMO_EXPERIENCE, com.android.wm.shell.Flags.FLAG_ENABLE_RETRIEVABLE_BUBBLES, com.android.wm.shell.Flags.FLAG_ENABLE_SPLIT_CONTEXTUAL, com.android.wm.shell.Flags.FLAG_ENABLE_TASKBAR_NAVBAR_UNIFICATION, com.android.wm.shell.Flags.FLAG_ENABLE_TINY_TASKBAR, com.android.wm.shell.Flags.FLAG_ONLY_REUSE_BUBBLED_TASK_WHEN_LAUNCHED_FROM_BUBBLE);
    }
}
