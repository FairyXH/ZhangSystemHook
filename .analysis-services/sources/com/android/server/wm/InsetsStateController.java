package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class InsetsStateController {
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private int mForcedConsumingTypes;
    private final android.view.InsetsState mLastState = new android.view.InsetsState();
    private final android.view.InsetsState mState = new android.view.InsetsState();
    private final android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> mProviders = new android.util.SparseArray<>();
    private final android.util.ArrayMap<com.android.server.wm.InsetsControlTarget, java.util.ArrayList<com.android.server.wm.InsetsSourceProvider>> mControlTargetProvidersMap = new android.util.ArrayMap<>();
    private final android.util.SparseArray<com.android.server.wm.InsetsControlTarget> mIdControlTargetMap = new android.util.SparseArray<>();
    private final android.util.SparseArray<com.android.server.wm.InsetsControlTarget> mIdFakeControlTargetMap = new android.util.SparseArray<>();
    private final android.util.ArraySet<com.android.server.wm.InsetsControlTarget> mPendingControlChanged = new android.util.ArraySet<>();
    private final java.util.function.Consumer<com.android.server.wm.WindowState> mDispatchInsetsChanged = new java.util.function.Consumer() { // from class: com.android.server.wm.InsetsStateController$$ExternalSyntheticLambda2
        @Override // java.util.function.Consumer
        public final void accept(java.lang.Object obj) {
            com.android.server.wm.InsetsStateController.lambda$new$0((com.android.server.wm.WindowState) obj);
        }
    };
    private final com.android.server.wm.InsetsControlTarget mEmptyImeControlTarget = new com.android.server.wm.InsetsStateController.AnonymousClass1();

    static /* synthetic */ void lambda$new$0(com.android.server.wm.WindowState w) {
        if (w.isReadyToDispatchInsetsState()) {
            w.notifyInsetsChanged();
        }
    }

    /* JADX INFO: renamed from: com.android.server.wm.InsetsStateController$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.wm.InsetsControlTarget {
        AnonymousClass1() {
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void notifyInsetsControlChanged(final int displayId) {
            android.view.InsetsSourceControl[] controls = com.android.server.wm.InsetsStateController.this.getControlsForDispatch(this);
            if (controls == null) {
                return;
            }
            for (android.view.InsetsSourceControl control : controls) {
                if (control != null && control.getType() == android.view.WindowInsets.Type.ime()) {
                    com.android.server.wm.InsetsStateController.this.mDisplayContent.mWmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.InsetsStateController$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.inputmethod.InputMethodManagerInternal.get().removeImeSurface(displayId);
                        }
                    });
                }
            }
        }
    }

    InsetsStateController(com.android.server.wm.DisplayContent displayContent) {
        this.mDisplayContent = displayContent;
    }

    android.view.InsetsState getRawInsetsState() {
        return this.mState;
    }

    android.view.InsetsSourceControl[] getControlsForDispatch(com.android.server.wm.InsetsControlTarget target) {
        java.util.ArrayList<com.android.server.wm.InsetsSourceProvider> controlled = this.mControlTargetProvidersMap.get(target);
        if (controlled == null) {
            return null;
        }
        int size = controlled.size();
        android.view.InsetsSourceControl[] result = new android.view.InsetsSourceControl[size];
        for (int i = 0; i < size; i++) {
            result[i] = controlled.get(i).getControl(target);
        }
        return result;
    }

    android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> getSourceProviders() {
        return this.mProviders;
    }

    com.android.server.wm.InsetsSourceProvider getOrCreateSourceProvider(int id, int type) {
        com.android.server.wm.InsetsSourceProvider insetsSourceProvider;
        int i;
        com.android.server.wm.InsetsSourceProvider provider = this.mProviders.get(id);
        if (provider != null) {
            return provider;
        }
        android.view.InsetsSource source = this.mState.getOrCreateSource(id, type);
        if (id == android.view.InsetsSource.ID_IME) {
            insetsSourceProvider = new com.android.server.wm.ImeInsetsSourceProvider(source, this, this.mDisplayContent);
        } else {
            insetsSourceProvider = new com.android.server.wm.InsetsSourceProvider(source, this, this.mDisplayContent);
        }
        com.android.server.wm.InsetsSourceProvider provider2 = insetsSourceProvider;
        if ((this.mForcedConsumingTypes & type) != 0) {
            i = 4;
        } else {
            i = 0;
        }
        provider2.setFlags(i, 4);
        this.mProviders.put(id, provider2);
        return provider2;
    }

    com.android.server.wm.ImeInsetsSourceProvider getImeSourceProvider() {
        return (com.android.server.wm.ImeInsetsSourceProvider) getOrCreateSourceProvider(android.view.InsetsSource.ID_IME, android.view.WindowInsets.Type.ime());
    }

    void removeSourceProvider(int id) {
        if (id != android.view.InsetsSource.ID_IME) {
            this.mState.removeSource(id);
            this.mProviders.remove(id);
        }
    }

    void setForcedConsumingTypes(int types) {
        int i;
        if (this.mForcedConsumingTypes != types) {
            this.mForcedConsumingTypes = types;
            boolean changed = false;
            for (int i2 = this.mProviders.size() - 1; i2 >= 0; i2--) {
                com.android.server.wm.InsetsSourceProvider provider = this.mProviders.valueAt(i2);
                if ((provider.getSource().getType() & types) != 0) {
                    i = 4;
                } else {
                    i = 0;
                }
                changed |= provider.setFlags(i, 4);
            }
            if (changed) {
                notifyInsetsChanged();
            }
        }
    }

    void onPostLayout() {
        android.os.Trace.traceBegin(32L, "ISC.onPostLayout");
        for (int i = this.mProviders.size() - 1; i >= 0; i--) {
            this.mProviders.valueAt(i).onPostLayout();
        }
        if (!this.mLastState.equals(this.mState)) {
            this.mLastState.set(this.mState, true);
            notifyInsetsChanged();
        }
        android.os.Trace.traceEnd(32L);
    }

    void updateAboveInsetsState(boolean notifyInsetsChange) {
        android.view.InsetsState aboveInsetsState = new android.view.InsetsState();
        aboveInsetsState.set(this.mState, android.view.WindowInsets.Type.displayCutout() | android.view.WindowInsets.Type.systemGestures() | android.view.WindowInsets.Type.mandatorySystemGestures());
        android.util.SparseArray<android.view.InsetsSource> localInsetsSourcesFromParent = new android.util.SparseArray<>();
        android.util.ArraySet<com.android.server.wm.WindowState> insetsChangedWindows = new android.util.ArraySet<>();
        this.mDisplayContent.updateAboveInsetsState(aboveInsetsState, localInsetsSourcesFromParent, insetsChangedWindows);
        if (notifyInsetsChange) {
            for (int i = insetsChangedWindows.size() - 1; i >= 0; i--) {
                this.mDispatchInsetsChanged.accept(insetsChangedWindows.valueAt(i));
            }
        }
    }

    void onDisplayFramesUpdated(boolean notifyInsetsChange) {
        final java.util.ArrayList<com.android.server.wm.WindowState> insetsChangedWindows = new java.util.ArrayList<>();
        this.mDisplayContent.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.InsetsStateController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onDisplayFramesUpdated$1(insetsChangedWindows, (com.android.server.wm.WindowState) obj);
            }
        }, true);
        if (notifyInsetsChange) {
            for (int i = insetsChangedWindows.size() - 1; i >= 0; i--) {
                this.mDispatchInsetsChanged.accept(insetsChangedWindows.get(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayFramesUpdated$1(java.util.ArrayList insetsChangedWindows, com.android.server.wm.WindowState w) {
        w.mAboveInsetsState.set(this.mState, android.view.WindowInsets.Type.displayCutout());
        insetsChangedWindows.add(w);
    }

    void onRequestedVisibleTypesChanged(com.android.server.wm.InsetsControlTarget caller) {
        boolean changed = false;
        for (int i = this.mProviders.size() - 1; i >= 0; i--) {
            changed |= this.mProviders.valueAt(i).updateClientVisibility(caller);
        }
        if (!android.view.inputmethod.Flags.refactorInsetsController() && changed) {
            notifyInsetsChanged();
            this.mDisplayContent.updateSystemGestureExclusion();
            this.mDisplayContent.getDisplayPolicy().updateSystemBarAttributes();
        }
    }

    int getFakeControllingTypes(com.android.server.wm.InsetsControlTarget target) {
        int types = 0;
        for (int i = this.mProviders.size() - 1; i >= 0; i--) {
            com.android.server.wm.InsetsSourceProvider provider = this.mProviders.valueAt(i);
            com.android.server.wm.InsetsControlTarget fakeControlTarget = provider.getFakeControlTarget();
            if (target == fakeControlTarget) {
                types |= provider.getSource().getType();
            }
        }
        return types;
    }

    void onImeControlTargetChanged(com.android.server.wm.InsetsControlTarget imeTarget) {
        com.android.server.wm.InsetsControlTarget target = imeTarget != null ? imeTarget : this.mEmptyImeControlTarget;
        onControlTargetChanged(getImeSourceProvider(), target, false);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(target != null ? target.getWindow() : "null");
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -6684172224226118673L, 0, null, protoLogParam0);
        }
        notifyPendingInsetsControlChanged();
    }

    void onBarControlTargetChanged(com.android.server.wm.InsetsControlTarget statusControlling, com.android.server.wm.InsetsControlTarget fakeStatusControlling, com.android.server.wm.InsetsControlTarget navControlling, com.android.server.wm.InsetsControlTarget fakeNavControlling) {
        for (int i = this.mProviders.size() - 1; i >= 0; i--) {
            com.android.server.wm.InsetsSourceProvider provider = this.mProviders.valueAt(i);
            int type = provider.getSource().getType();
            if (type == android.view.WindowInsets.Type.statusBars()) {
                onControlTargetChanged(provider, statusControlling, false);
                onControlTargetChanged(provider, fakeStatusControlling, true);
            } else if (type == android.view.WindowInsets.Type.navigationBars()) {
                onControlTargetChanged(provider, navControlling, false);
                onControlTargetChanged(provider, fakeNavControlling, true);
            }
        }
        notifyPendingInsetsControlChanged();
    }

    void notifyControlTargetChanged(com.android.server.wm.InsetsControlTarget target, com.android.server.wm.InsetsSourceProvider provider) {
        onControlTargetChanged(provider, target, false);
        notifyPendingInsetsControlChanged();
    }

    void notifyControlRevoked(com.android.server.wm.InsetsControlTarget previousControlTarget, com.android.server.wm.InsetsSourceProvider provider) {
        removeFromControlMaps(previousControlTarget, provider, false);
    }

    private void onControlTargetChanged(com.android.server.wm.InsetsSourceProvider provider, com.android.server.wm.InsetsControlTarget target, boolean fake) {
        com.android.server.wm.InsetsControlTarget lastTarget;
        if (fake) {
            lastTarget = this.mIdFakeControlTargetMap.get(provider.getSource().getId());
        } else {
            lastTarget = this.mIdControlTargetMap.get(provider.getSource().getId());
        }
        if ((target == lastTarget && !provider.mInsetsSourceProviderExt.shouldIgnoreTargetCheck()) || !provider.isControllable() || this.mDisplayContent.getInsetsPolicy().insetsPolicyExt.shouldIgnoreNavControlTarget(target)) {
            return;
        }
        if (fake) {
            provider.updateFakeControlTarget(target);
        } else {
            provider.updateControlForTarget(target, false);
            target = provider.getControlTarget();
            if (target == lastTarget) {
                return;
            }
        }
        if (lastTarget != null) {
            removeFromControlMaps(lastTarget, provider, fake);
            this.mPendingControlChanged.add(lastTarget);
        }
        if (target != null) {
            addToControlMaps(target, provider, fake);
            this.mPendingControlChanged.add(target);
        }
    }

    private void removeFromControlMaps(com.android.server.wm.InsetsControlTarget target, com.android.server.wm.InsetsSourceProvider provider, boolean fake) {
        java.util.ArrayList<com.android.server.wm.InsetsSourceProvider> array = this.mControlTargetProvidersMap.get(target);
        if (array == null) {
            return;
        }
        array.remove(provider);
        if (array.isEmpty()) {
            this.mControlTargetProvidersMap.remove(target);
        }
        if (fake) {
            this.mIdFakeControlTargetMap.remove(provider.getSource().getId());
        } else {
            this.mIdControlTargetMap.remove(provider.getSource().getId());
        }
    }

    private void addToControlMaps(com.android.server.wm.InsetsControlTarget target, com.android.server.wm.InsetsSourceProvider provider, boolean fake) {
        java.util.ArrayList<com.android.server.wm.InsetsSourceProvider> array = this.mControlTargetProvidersMap.computeIfAbsent(target, new java.util.function.Function() { // from class: com.android.server.wm.InsetsStateController$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.wm.InsetsStateController.lambda$addToControlMaps$2((com.android.server.wm.InsetsControlTarget) obj);
            }
        });
        array.add(provider);
        if (fake) {
            this.mIdFakeControlTargetMap.put(provider.getSource().getId(), target);
        } else {
            this.mIdControlTargetMap.put(provider.getSource().getId(), target);
        }
    }

    static /* synthetic */ java.util.ArrayList lambda$addToControlMaps$2(com.android.server.wm.InsetsControlTarget key) {
        return new java.util.ArrayList();
    }

    void notifyControlChanged(com.android.server.wm.InsetsControlTarget target) {
        this.mPendingControlChanged.add(target);
        notifyPendingInsetsControlChanged();
        if (android.view.inputmethod.Flags.refactorInsetsController()) {
            notifyInsetsChanged();
            this.mDisplayContent.updateSystemGestureExclusion();
            this.mDisplayContent.updateKeepClearAreas();
            this.mDisplayContent.getDisplayPolicy().updateSystemBarAttributes();
        }
    }

    private void notifyPendingInsetsControlChanged() {
        if (this.mPendingControlChanged.isEmpty()) {
            return;
        }
        this.mDisplayContent.mWmService.mAnimator.addAfterPrepareSurfacesRunnable(new java.lang.Runnable() { // from class: com.android.server.wm.InsetsStateController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyPendingInsetsControlChanged$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyPendingInsetsControlChanged$3() {
        for (int i = this.mProviders.size() - 1; i >= 0; i--) {
            com.android.server.wm.InsetsSourceProvider provider = this.mProviders.valueAt(i);
            provider.onSurfaceTransactionApplied();
        }
        android.util.ArraySet<com.android.server.wm.InsetsControlTarget> newControlTargets = new android.util.ArraySet<>();
        int displayId = this.mDisplayContent.getDisplayId();
        for (int i2 = this.mPendingControlChanged.size() - 1; i2 >= 0; i2--) {
            com.android.server.wm.InsetsControlTarget controlTarget = this.mPendingControlChanged.valueAt(i2);
            if (controlTarget != null) {
                controlTarget.notifyInsetsControlChanged(displayId);
                if (this.mControlTargetProvidersMap.containsKey(controlTarget)) {
                    newControlTargets.add(controlTarget);
                }
            }
        }
        this.mPendingControlChanged.clear();
        for (int i3 = newControlTargets.size() - 1; i3 >= 0; i3--) {
            onRequestedVisibleTypesChanged(newControlTargets.valueAt(i3));
        }
        newControlTargets.clear();
        getImeSourceProvider().checkAndStartShowImePostLayout();
    }

    void notifyInsetsChanged() {
        this.mDisplayContent.notifyInsetsChanged(this.mDispatchInsetsChanged);
    }

    boolean hasPendingControls(com.android.server.wm.InsetsControlTarget target) {
        return this.mPendingControlChanged.contains(target);
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + "WindowInsetsStateController");
        java.lang.String prefix2 = prefix + "  ";
        this.mState.dump(prefix2, pw);
        pw.println(prefix2 + "Control map:");
        for (int i = this.mControlTargetProvidersMap.size() - 1; i >= 0; i--) {
            com.android.server.wm.InsetsControlTarget controlTarget = this.mControlTargetProvidersMap.keyAt(i);
            pw.print(prefix2 + "  ");
            pw.print(controlTarget);
            pw.println(":");
            java.util.ArrayList<com.android.server.wm.InsetsSourceProvider> providers = this.mControlTargetProvidersMap.valueAt(i);
            for (int j = providers.size() - 1; j >= 0; j--) {
                com.android.server.wm.InsetsSourceProvider provider = providers.get(j);
                if (provider != null) {
                    pw.print(prefix2 + "    ");
                    if (controlTarget == provider.getFakeControlTarget()) {
                        pw.print("(fake) ");
                    }
                    pw.println(provider.getControl(controlTarget));
                }
            }
        }
        if (this.mControlTargetProvidersMap.isEmpty()) {
            pw.print(prefix2 + "  none");
        }
        pw.println(prefix2 + "InsetsSourceProviders:");
        for (int i2 = this.mProviders.size() - 1; i2 >= 0; i2--) {
            this.mProviders.valueAt(i2).dump(pw, prefix2 + "  ");
        }
        int i3 = this.mForcedConsumingTypes;
        if (i3 != 0) {
            pw.println(prefix2 + "mForcedConsumingTypes=" + android.view.WindowInsets.Type.toString(this.mForcedConsumingTypes));
        }
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, int logLevel) {
        long j;
        for (int i = this.mProviders.size() - 1; i >= 0; i--) {
            com.android.server.wm.InsetsSourceProvider provider = this.mProviders.valueAt(i);
            if (provider.getSource().getType() == android.view.WindowInsets.Type.ime()) {
                j = 1146756268063L;
            } else {
                j = 2246267895843L;
            }
            provider.dumpDebug(proto, j, logLevel);
        }
    }
}
