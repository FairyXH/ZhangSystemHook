package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TransparentPolicy {
    private static final java.util.function.Predicate<com.android.server.wm.ActivityRecord> FIRST_OPAQUE_NOT_FINISHING_ACTIVITY_PREDICATE = new com.android.server.wm.RecentsAnimation$$ExternalSyntheticLambda3();
    private static final java.lang.String TAG = "ActivityTaskManager";
    private final com.android.server.wm.ActivityRecord mActivityRecord;
    private final java.util.List<com.android.server.wm.TransparentPolicy> mDestroyListeners = new java.util.ArrayList();
    private final java.util.function.BooleanSupplier mIsTranslucentLetterboxingEnabledSupplier;
    private final com.android.server.wm.TransparentPolicy.TransparentPolicyState mTransparentPolicyState;

    TransparentPolicy(com.android.server.wm.ActivityRecord activityRecord, final com.android.server.wm.LetterboxConfiguration letterboxConfiguration) {
        this.mActivityRecord = activityRecord;
        java.util.Objects.requireNonNull(letterboxConfiguration);
        this.mIsTranslucentLetterboxingEnabledSupplier = new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.TransparentPolicy$$ExternalSyntheticLambda0
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return letterboxConfiguration.isTranslucentLetterboxingEnabled();
            }
        };
        this.mTransparentPolicyState = new com.android.server.wm.TransparentPolicy.TransparentPolicyState(activityRecord);
    }

    void start() {
        if (!this.mIsTranslucentLetterboxingEnabledSupplier.getAsBoolean()) {
            return;
        }
        com.android.server.wm.WindowContainer<?> parent = this.mActivityRecord.getParent();
        if (parent == null) {
            return;
        }
        this.mTransparentPolicyState.reset();
        com.android.server.wm.ActivityRecord firstOpaqueActivity = this.mActivityRecord.getTask().getActivity(FIRST_OPAQUE_NOT_FINISHING_ACTIVITY_PREDICATE, this.mActivityRecord, false, true);
        if (shouldSkipTransparentPolicy(firstOpaqueActivity)) {
            return;
        }
        this.mTransparentPolicyState.start(firstOpaqueActivity);
    }

    void stop() {
        for (int i = this.mDestroyListeners.size() - 1; i >= 0; i--) {
            this.mDestroyListeners.get(i).start();
        }
        this.mDestroyListeners.clear();
        this.mTransparentPolicyState.reset();
    }

    boolean isRunning() {
        return this.mTransparentPolicyState.isRunning();
    }

    boolean hasInheritedOrientation() {
        return isRunning() && this.mActivityRecord.getOverrideOrientation() != -1;
    }

    float getInheritedMinAspectRatio() {
        return this.mTransparentPolicyState.mInheritedMinAspectRatio;
    }

    float getInheritedMaxAspectRatio() {
        return this.mTransparentPolicyState.mInheritedMaxAspectRatio;
    }

    int getInheritedAppCompatState() {
        return this.mTransparentPolicyState.mInheritedAppCompatState;
    }

    int getInheritedOrientation() {
        return this.mTransparentPolicyState.mInheritedOrientation;
    }

    com.android.server.wm.ActivityRecord.CompatDisplayInsets getInheritedCompatDisplayInsets() {
        return this.mTransparentPolicyState.mInheritedCompatDisplayInsets;
    }

    void clearInheritedCompatDisplayInsets() {
        this.mTransparentPolicyState.clearInheritedCompatDisplayInsets();
    }

    com.android.server.wm.TransparentPolicy.TransparentPolicyState getTransparentPolicyState() {
        return this.mTransparentPolicyState;
    }

    boolean applyOnOpaqueActivityBelow(java.util.function.Consumer<com.android.server.wm.ActivityRecord> consumer) {
        return this.mTransparentPolicyState.applyOnOpaqueActivityBelow(consumer);
    }

    java.util.Optional<com.android.server.wm.ActivityRecord> getFirstOpaqueActivity() {
        return isRunning() ? java.util.Optional.of(this.mTransparentPolicyState.mFirstOpaqueActivity) : java.util.Optional.empty();
    }

    java.util.Optional<com.android.server.wm.ActivityRecord> findOpaqueNotFinishingActivityBelow() {
        return this.mTransparentPolicyState.findOpaqueNotFinishingActivityBelow();
    }

    private boolean shouldSkipTransparentPolicy(com.android.server.wm.ActivityRecord opaqueActivity) {
        if (opaqueActivity == null || opaqueActivity.isEmbedded()) {
            this.mActivityRecord.recomputeConfiguration();
            return true;
        }
        if (this.mActivityRecord.getTask() == null || this.mActivityRecord.fillsParent() || this.mActivityRecord.hasCompatDisplayInsetsWithoutInheritance()) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetTranslucentOverrideConfig(android.content.res.Configuration config) {
        config.orientation = 0;
        config.compatScreenWidthDp = 0;
        config.screenWidthDp = 0;
        config.compatScreenHeightDp = 0;
        config.screenHeightDp = 0;
        config.compatSmallestScreenWidthDp = 0;
        config.smallestScreenWidthDp = 0;
    }

    private void inheritConfiguration(com.android.server.wm.ActivityRecord firstOpaque) {
        this.mTransparentPolicyState.inheritFromOpaque(firstOpaque);
    }

    static class TransparentPolicyState {
        private static final float UNDEFINED_ASPECT_RATIO = 0.0f;
        private final com.android.server.wm.ActivityRecord mActivityRecord;
        private com.android.server.wm.ActivityRecord mFirstOpaqueActivity;
        private com.android.server.wm.ActivityRecord.CompatDisplayInsets mInheritedCompatDisplayInsets;
        private com.android.server.wm.WindowContainerListener mLetterboxConfigListener;
        private int mInheritedOrientation = 0;
        private float mInheritedMinAspectRatio = 0.0f;
        private float mInheritedMaxAspectRatio = 0.0f;
        private int mInheritedAppCompatState = 0;

        TransparentPolicyState(com.android.server.wm.ActivityRecord activityRecord) {
            this.mActivityRecord = activityRecord;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void start(com.android.server.wm.ActivityRecord firstOpaqueActivity) {
            this.mFirstOpaqueActivity = firstOpaqueActivity;
            this.mFirstOpaqueActivity.mTransparentPolicy.mDestroyListeners.add(this.mActivityRecord.mTransparentPolicy);
            inheritFromOpaque(firstOpaqueActivity);
            final com.android.server.wm.WindowContainer<?> parent = this.mActivityRecord.getParent();
            this.mLetterboxConfigListener = com.android.server.wm.WindowContainer.overrideConfigurationPropagation(this.mActivityRecord, this.mFirstOpaqueActivity, new com.android.server.wm.WindowContainer.ConfigurationMerger() { // from class: com.android.server.wm.TransparentPolicy$TransparentPolicyState$$ExternalSyntheticLambda1
                @Override // com.android.server.wm.WindowContainer.ConfigurationMerger
                public final android.content.res.Configuration merge(android.content.res.Configuration configuration, android.content.res.Configuration configuration2) {
                    return this.f$0.lambda$start$0(parent, configuration, configuration2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ android.content.res.Configuration lambda$start$0(com.android.server.wm.WindowContainer parent, android.content.res.Configuration opaqueConfig, android.content.res.Configuration transparentOverrideConfig) {
            com.android.server.wm.TransparentPolicy.resetTranslucentOverrideConfig(transparentOverrideConfig);
            android.graphics.Rect parentBounds = parent.getWindowConfiguration().getBounds();
            android.graphics.Rect bounds = transparentOverrideConfig.windowConfiguration.getBounds();
            android.graphics.Rect letterboxBounds = opaqueConfig.windowConfiguration.getBounds();
            bounds.set(parentBounds.left, parentBounds.top, parentBounds.left + letterboxBounds.width(), parentBounds.top + letterboxBounds.height());
            transparentOverrideConfig.windowConfiguration.setAppBounds(new android.graphics.Rect());
            inheritFromOpaque(this.mFirstOpaqueActivity);
            return transparentOverrideConfig;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void inheritFromOpaque(com.android.server.wm.ActivityRecord opaqueActivity) {
            if (this.mActivityRecord.getMinAspectRatio() != 0.0f) {
                this.mInheritedMinAspectRatio = opaqueActivity.getMinAspectRatio();
            }
            if (this.mActivityRecord.getMaxAspectRatio() != 0.0f) {
                this.mInheritedMaxAspectRatio = opaqueActivity.getMaxAspectRatio();
            }
            this.mInheritedOrientation = opaqueActivity.getRequestedConfigurationOrientation();
            this.mInheritedAppCompatState = opaqueActivity.getAppCompatState();
            this.mInheritedCompatDisplayInsets = opaqueActivity.getCompatDisplayInsets();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reset() {
            if (this.mLetterboxConfigListener != null) {
                this.mLetterboxConfigListener.onRemoved();
            }
            this.mLetterboxConfigListener = null;
            this.mInheritedOrientation = 0;
            this.mInheritedMinAspectRatio = 0.0f;
            this.mInheritedMaxAspectRatio = 0.0f;
            this.mInheritedAppCompatState = 0;
            this.mInheritedCompatDisplayInsets = null;
            if (this.mFirstOpaqueActivity != null) {
                this.mFirstOpaqueActivity.mTransparentPolicy.mDestroyListeners.remove(this.mActivityRecord.mTransparentPolicy);
            }
            this.mFirstOpaqueActivity = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isRunning() {
            return this.mLetterboxConfigListener != null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearInheritedCompatDisplayInsets() {
            this.mInheritedCompatDisplayInsets = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.util.Optional<com.android.server.wm.ActivityRecord> findOpaqueNotFinishingActivityBelow() {
            if (!isRunning() || this.mActivityRecord.getTask() == null) {
                return java.util.Optional.empty();
            }
            return java.util.Optional.ofNullable(this.mFirstOpaqueActivity);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean applyOnOpaqueActivityBelow(final java.util.function.Consumer<com.android.server.wm.ActivityRecord> consumer) {
            return ((java.lang.Boolean) findOpaqueNotFinishingActivityBelow().map(new java.util.function.Function() { // from class: com.android.server.wm.TransparentPolicy$TransparentPolicyState$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.wm.TransparentPolicy.TransparentPolicyState.lambda$applyOnOpaqueActivityBelow$1(consumer, (com.android.server.wm.ActivityRecord) obj);
                }
            }).orElse(false)).booleanValue();
        }

        static /* synthetic */ java.lang.Boolean lambda$applyOnOpaqueActivityBelow$1(java.util.function.Consumer consumer, com.android.server.wm.ActivityRecord activityRecord) {
            consumer.accept(activityRecord);
            return true;
        }
    }
}
