package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class AnimatingActivityRegistry {
    private boolean mEndingDeferredFinish;
    private android.util.ArraySet<com.android.server.wm.ActivityRecord> mAnimatingActivities = new android.util.ArraySet<>();
    private java.util.LinkedHashMap<com.android.server.wm.ActivityRecord, java.lang.Runnable> mFinishedTokens = new java.util.LinkedHashMap<>();
    private java.util.ArrayList<java.lang.Runnable> mTmpRunnableList = new java.util.ArrayList<>();
    private com.android.server.wm.AnimatingActivityRegistry.AnimatingActivityRegistryWrapper mWrapper = new com.android.server.wm.AnimatingActivityRegistry.AnimatingActivityRegistryWrapper();

    AnimatingActivityRegistry() {
    }

    void notifyStarting(com.android.server.wm.ActivityRecord token) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
            android.util.Slog.i("AnimatingActivityRegistry", "Animation of " + token + " is starting.");
        }
        this.mAnimatingActivities.add(token);
    }

    void notifyFinished(com.android.server.wm.ActivityRecord activity) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
            android.util.Slog.i("AnimatingActivityRegistry", "Animation of " + activity + " is finished.");
        }
        this.mAnimatingActivities.remove(activity);
        this.mFinishedTokens.remove(activity);
        if (this.mAnimatingActivities.isEmpty()) {
            endDeferringFinished();
        }
    }

    boolean notifyAboutToFinish(com.android.server.wm.ActivityRecord activity, java.lang.Runnable endDeferFinishCallback) {
        boolean removed = this.mAnimatingActivities.remove(activity);
        if (!removed) {
            return false;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
            android.util.Slog.i("AnimatingActivityRegistry", "Animation of " + activity + " is about to finish.");
        }
        if (this.mAnimatingActivities.isEmpty() && !this.mWrapper.getExtImpl().shouldDeferAnimatingActivityFinished(activity)) {
            this.mFinishedTokens.put(activity, endDeferFinishCallback);
            endDeferringFinished();
            return false;
        }
        this.mFinishedTokens.put(activity, endDeferFinishCallback);
        return true;
    }

    private void endDeferringFinished() {
        if (this.mEndingDeferredFinish) {
            return;
        }
        try {
            this.mEndingDeferredFinish = true;
            this.mWrapper.getExtImpl().makeRunnableList(this.mFinishedTokens, this.mTmpRunnableList);
            this.mFinishedTokens.clear();
            for (int i = 0; i < this.mTmpRunnableList.size(); i++) {
                this.mTmpRunnableList.get(i).run();
            }
            this.mTmpRunnableList.clear();
        } finally {
            this.mEndingDeferredFinish = false;
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String header, java.lang.String prefix) {
        if (!this.mAnimatingActivities.isEmpty() || !this.mFinishedTokens.isEmpty()) {
            pw.print(prefix);
            pw.println(header);
            java.lang.String prefix2 = prefix + "  ";
            pw.print(prefix2);
            pw.print("mAnimatingActivities=");
            pw.println(this.mAnimatingActivities);
            pw.print(prefix2);
            pw.print("mFinishedTokens=");
            pw.println(this.mFinishedTokens);
        }
    }

    public com.android.server.wm.IAnimatingActivityRegistryWrapper getWrapper() {
        return this.mWrapper;
    }

    private class AnimatingActivityRegistryWrapper implements com.android.server.wm.IAnimatingActivityRegistryWrapper {
        private com.android.server.wm.IAnimatingActivityRegistryExt mAnimatingActivityRegistryExt;

        private AnimatingActivityRegistryWrapper() {
            this.mAnimatingActivityRegistryExt = (com.android.server.wm.IAnimatingActivityRegistryExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IAnimatingActivityRegistryExt.class).base(com.android.server.wm.AnimatingActivityRegistry.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.IAnimatingActivityRegistryExt getExtImpl() {
            return this.mAnimatingActivityRegistryExt;
        }

        @Override // com.android.server.wm.IAnimatingActivityRegistryWrapper
        public java.util.LinkedHashMap<com.android.server.wm.ActivityRecord, java.lang.Runnable> getFinishedTokens() {
            return com.android.server.wm.AnimatingActivityRegistry.this.mFinishedTokens;
        }
    }
}
