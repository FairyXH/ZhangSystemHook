package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowContainer<E extends com.android.server.wm.WindowContainer> extends com.android.server.wm.ConfigurationContainer<E> implements java.lang.Comparable<com.android.server.wm.WindowContainer>, com.android.server.wm.SurfaceAnimator.Animatable, com.android.server.wm.SurfaceFreezer.Freezable, com.android.server.wm.InsetsControlTarget {
    static final int POSITION_BOTTOM = Integer.MIN_VALUE;
    static final int POSITION_TOP = Integer.MAX_VALUE;
    public static final int SYNC_STATE_NONE = 0;
    public static final int SYNC_STATE_READY = 2;
    public static final int SYNC_STATE_WAITING_FOR_DRAW = 1;
    private static final java.lang.String TAG = "WindowManager";
    android.view.SurfaceControl mAnimationBoundsLayer;
    private android.view.SurfaceControl mAnimationLeash;
    private boolean mCommittedReparentToAnimationLeash;
    protected com.android.server.wm.InsetsSourceProvider mControllableInsetProvider;
    protected com.android.server.wm.DisplayContent mDisplayContent;
    private android.util.ArrayMap<android.os.IBinder, com.android.server.wm.WindowContainer<E>.DeathRecipient> mInsetsOwnerDeathRecipientMap;
    private android.view.MagnificationSpec mLastMagnificationSpec;
    protected com.android.server.wm.WindowContainer mLastOrientationSource;
    boolean mLaunchTaskBehind;
    boolean mNeedsAnimationBoundsLayer;
    boolean mNeedsZBoost;
    protected com.android.server.wm.TrustedOverlayHost mOverlayHost;
    private final android.view.SurfaceControl.Transaction mPendingTransaction;
    boolean mReparenting;
    protected final com.android.server.wm.SurfaceAnimator mSurfaceAnimator;
    protected android.view.SurfaceControl mSurfaceControl;
    final com.android.server.wm.SurfaceFreezer mSurfaceFreezer;
    final android.view.SurfaceControl.Transaction mSyncTransaction;
    com.android.server.wm.WindowContainerThumbnail mThumbnail;
    int mTransit;
    int mTransitFlags;
    final com.android.server.wm.TransitionController mTransitionController;
    protected boolean mVisibleRequested;
    protected final com.android.server.wm.WindowManagerService mWmService;
    private com.android.server.wm.WindowContainer<com.android.server.wm.WindowContainer> mParent = null;
    android.util.SparseArray<android.view.InsetsSource> mLocalInsetsSources = null;
    protected android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> mInsetsSourceProviders = null;
    protected final com.android.server.wm.WindowList<E> mChildren = new com.android.server.wm.WindowList<>();
    private int mOverrideOrientation = -2;
    private final android.util.Pools.SynchronizedPool<com.android.server.wm.WindowContainer<E>.ForAllWindowsConsumerWrapper> mConsumerWrapperPool = new android.util.Pools.SynchronizedPool<>(3);
    private int mLastLayer = 0;
    private android.view.SurfaceControl mLastRelativeToLayer = null;
    com.android.server.wm.IWindowContainerExt mWindowContainerExt = (com.android.server.wm.IWindowContainerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowContainerExt.class).base(this).create();
    final java.util.ArrayList<com.android.server.wm.WindowState> mWaitingForDrawn = new java.util.ArrayList<>();
    private final android.util.ArraySet<com.android.server.wm.WindowContainer> mSurfaceAnimationSources = new android.util.ArraySet<>();
    private final android.graphics.Point mTmpPos = new android.graphics.Point();
    protected final android.graphics.Point mLastSurfacePosition = new android.graphics.Point();
    protected int mLastDeltaRotation = 0;
    private int mTreeWeight = 1;
    private int mSyncTransactionCommitCallbackDepth = 0;
    final android.graphics.Point mTmpPoint = new android.graphics.Point();
    protected final android.graphics.Rect mTmpRect = new android.graphics.Rect();
    final android.graphics.Rect mTmpPrevBounds = new android.graphics.Rect();
    private boolean mIsFocusable = true;
    com.android.server.wm.WindowContainer.RemoteToken mRemoteToken = null;
    com.android.server.wm.BLASTSyncEngine.SyncGroup mSyncGroup = null;
    int mSyncState = 0;
    int mSyncMethodOverride = -1;
    private final java.util.List<com.android.server.wm.WindowContainerListener> mListeners = new java.util.ArrayList();
    private final java.util.LinkedList<com.android.server.wm.WindowContainer> mTmpChain1 = new java.util.LinkedList<>();
    private final java.util.LinkedList<com.android.server.wm.WindowContainer> mTmpChain2 = new java.util.LinkedList<>();
    private com.android.server.wm.WindowContainer<E>.WindowContainerWrapper mWindowContainerWrapper = new com.android.server.wm.WindowContainer.WindowContainerWrapper();

    public interface AnimationFlags {
        public static final int CHILDREN = 4;
        public static final int PARENTS = 2;
        public static final int TRANSITION = 1;
    }

    @java.lang.FunctionalInterface
    interface ConfigurationMerger {
        android.content.res.Configuration merge(android.content.res.Configuration configuration, android.content.res.Configuration configuration2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface IAnimationStarter {
        void startAnimation(android.view.SurfaceControl.Transaction transaction, com.android.server.wm.AnimationAdapter animationAdapter, boolean z, int i, com.android.server.wm.AnimationAdapter animationAdapter2);
    }

    @interface SyncState {
    }

    WindowContainer(com.android.server.wm.WindowManagerService wms) {
        this.mWmService = wms;
        this.mTransitionController = this.mWmService.mAtmService.getTransitionController();
        java.util.function.Supplier<android.view.SurfaceControl.Transaction> lockedTransactionFactory = wms.getWrapper().getExtImpl().getLockedTransactionFactory();
        if (lockedTransactionFactory != null) {
            this.mPendingTransaction = lockedTransactionFactory.get();
            this.mSyncTransaction = lockedTransactionFactory.get();
        } else {
            this.mPendingTransaction = wms.mTransactionFactory.get();
            this.mSyncTransaction = wms.mTransactionFactory.get();
        }
        this.mSurfaceAnimator = new com.android.server.wm.SurfaceAnimator(this, new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.WindowContainer$$ExternalSyntheticLambda5
            @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
            public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
                this.f$0.onAnimationFinished(i, animationAdapter);
            }
        }, wms);
        this.mSurfaceFreezer = new com.android.server.wm.SurfaceFreezer(this, wms);
    }

    void updateAboveInsetsState(android.view.InsetsState aboveInsetsState, android.util.SparseArray<android.view.InsetsSource> localInsetsSourcesFromParent, android.util.ArraySet<com.android.server.wm.WindowState> insetsChangedWindows) {
        android.util.SparseArray<android.view.InsetsSource> mergedLocalInsetsSources = createMergedSparseArray(localInsetsSourcesFromParent, this.mLocalInsetsSources);
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            this.mChildren.get(i).updateAboveInsetsState(aboveInsetsState, mergedLocalInsetsSources, insetsChangedWindows);
        }
    }

    static <T> android.util.SparseArray<T> createMergedSparseArray(android.util.SparseArray<T> sa1, android.util.SparseArray<T> sa2) {
        int size1 = sa1 != null ? sa1.size() : 0;
        int size2 = sa2 != null ? sa2.size() : 0;
        android.util.SparseArray<T> mergedArray = new android.util.SparseArray<>(size1 + size2);
        if (size1 > 0) {
            for (int i = 0; i < size1; i++) {
                mergedArray.append(sa1.keyAt(i), sa1.valueAt(i));
            }
        }
        if (size2 > 0) {
            for (int i2 = 0; i2 < size2; i2++) {
                mergedArray.put(sa2.keyAt(i2), sa2.valueAt(i2));
            }
        }
        return mergedArray;
    }

    void addLocalInsetsFrameProvider(android.view.InsetsFrameProvider provider, android.os.IBinder owner) {
        if (provider == null || owner == null) {
            throw new java.lang.IllegalArgumentException("Insets provider or owner not specified.");
        }
        if (this.mDisplayContent == null) {
            android.util.Slog.w(TAG, "Can't add insets frame provider when detached. " + this);
            return;
        }
        if (this.mInsetsOwnerDeathRecipientMap == null) {
            this.mInsetsOwnerDeathRecipientMap = new android.util.ArrayMap<>();
        }
        com.android.server.wm.WindowContainer<E>.DeathRecipient deathRecipient = this.mInsetsOwnerDeathRecipientMap.get(owner);
        if (deathRecipient == null) {
            deathRecipient = new com.android.server.wm.WindowContainer.DeathRecipient(owner);
            try {
                owner.linkToDeath(deathRecipient, 0);
                this.mInsetsOwnerDeathRecipientMap.put(owner, deathRecipient);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to add source for " + provider + " since the owner has died.");
                return;
            }
        }
        int id = provider.getId();
        deathRecipient.addSourceId(id);
        if (this.mLocalInsetsSources == null) {
            this.mLocalInsetsSources = new android.util.SparseArray<>();
        }
        if (this.mLocalInsetsSources.get(id) != null && com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.d(TAG, "The local insets source for this " + provider + " already exists. Overwriting.");
        }
        android.view.InsetsSource source = new android.view.InsetsSource(id, provider.getType());
        source.setFrame(provider.getArbitraryRectangle()).updateSideHint(getBounds()).setBoundingRects(provider.getBoundingRects());
        this.mLocalInsetsSources.put(id, source);
        this.mDisplayContent.getInsetsStateController().updateAboveInsetsState(true);
    }

    private class DeathRecipient implements android.os.IBinder.DeathRecipient {
        private final android.os.IBinder mOwner;
        private final android.util.ArraySet<java.lang.Integer> mSourceIds = new android.util.ArraySet<>();

        DeathRecipient(android.os.IBinder owner) {
            this.mOwner = owner;
        }

        void addSourceId(int id) {
            this.mSourceIds.add(java.lang.Integer.valueOf(id));
        }

        void removeSourceId(int id) {
            this.mSourceIds.remove(java.lang.Integer.valueOf(id));
        }

        boolean hasSource() {
            return !this.mSourceIds.isEmpty();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowContainer.this.mWmService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                boolean changed = false;
                try {
                    for (int i = this.mSourceIds.size() - 1; i >= 0; i--) {
                        changed |= com.android.server.wm.WindowContainer.this.removeLocalInsetsSource(this.mSourceIds.valueAt(i).intValue());
                    }
                    this.mSourceIds.clear();
                    this.mOwner.unlinkToDeath(this, 0);
                    com.android.server.wm.WindowContainer.this.mInsetsOwnerDeathRecipientMap.remove(this.mOwner);
                    if (changed && com.android.server.wm.WindowContainer.this.mDisplayContent != null) {
                        com.android.server.wm.WindowContainer.this.mDisplayContent.getInsetsStateController().updateAboveInsetsState(true);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    void removeLocalInsetsFrameProvider(android.view.InsetsFrameProvider provider, android.os.IBinder owner) {
        com.android.server.wm.WindowContainer<E>.DeathRecipient deathRecipient;
        if (provider == null || owner == null) {
            throw new java.lang.IllegalArgumentException("Insets provider or owner not specified.");
        }
        int id = provider.getId();
        if (removeLocalInsetsSource(id) && this.mDisplayContent != null) {
            this.mDisplayContent.getInsetsStateController().updateAboveInsetsState(true);
        }
        if (this.mInsetsOwnerDeathRecipientMap == null || (deathRecipient = this.mInsetsOwnerDeathRecipientMap.get(owner)) == null) {
            return;
        }
        deathRecipient.removeSourceId(id);
        if (!deathRecipient.hasSource()) {
            owner.unlinkToDeath(deathRecipient, 0);
            this.mInsetsOwnerDeathRecipientMap.remove(owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeLocalInsetsSource(int id) {
        if (this.mLocalInsetsSources == null) {
            return false;
        }
        if (this.mLocalInsetsSources.removeReturnOld(id) == null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
                android.util.Slog.d(TAG, "Given id " + java.lang.Integer.toHexString(id) + " doesn't exist.");
            }
            return false;
        }
        return true;
    }

    void setControllableInsetProvider(com.android.server.wm.InsetsSourceProvider insetProvider) {
        this.mControllableInsetProvider = insetProvider;
    }

    com.android.server.wm.InsetsSourceProvider getControllableInsetProvider() {
        return this.mControllableInsetProvider;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.ConfigurationContainer
    public final com.android.server.wm.WindowContainer getParent() {
        return this.mParent;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    protected int getChildCount() {
        return this.mChildren.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.ConfigurationContainer
    public E getChildAt(int index) {
        return this.mChildren.get(index);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
        super.onConfigurationChanged(newParentConfig);
        updateSurfacePositionNonOrganized();
        scheduleAnimation();
        if (this.mOverlayHost != null) {
            this.mOverlayHost.dispatchConfigurationChanged(getConfiguration());
        }
    }

    void reparent(com.android.server.wm.WindowContainer newParent, int position) {
        if (newParent == null) {
            throw new java.lang.IllegalArgumentException("reparent: can't reparent to null " + this);
        }
        if (newParent == this) {
            throw new java.lang.IllegalArgumentException("Can not reparent to itself " + this);
        }
        com.android.server.wm.WindowContainer<com.android.server.wm.WindowContainer> windowContainer = this.mParent;
        if (this.mParent == newParent) {
            throw new java.lang.IllegalArgumentException("WC=" + this + " already child of " + this.mParent);
        }
        this.mTransitionController.collectReparentChange(this, newParent);
        com.android.server.wm.DisplayContent prevDc = null;
        if (windowContainer != null) {
            prevDc = windowContainer.getDisplayContent();
        }
        com.android.server.wm.DisplayContent dc = newParent.getDisplayContent();
        this.mWindowContainerExt.handleComapctReparent(this, true, newParent);
        this.mReparenting = true;
        if (windowContainer != null) {
            this.mWindowContainerExt.preReparent(this, dc);
            windowContainer.removeChild(this);
        }
        newParent.addChild(this, position);
        this.mReparenting = false;
        this.mWindowContainerExt.handleComapctReparent(this, false, newParent);
        dc.setLayoutNeeded();
        if (prevDc != dc) {
            onDisplayChanged(dc);
            if (prevDc != null) {
                prevDc.setLayoutNeeded();
            }
        }
        onParentChanged(newParent, windowContainer);
        onSyncReparent(windowContainer, newParent);
    }

    protected final void setParent(com.android.server.wm.WindowContainer<com.android.server.wm.WindowContainer> parent) {
        com.android.server.wm.WindowContainer<com.android.server.wm.WindowContainer> windowContainer = this.mParent;
        this.mParent = parent;
        if (this.mParent != null) {
            this.mParent.onChildAdded(this);
        } else if (this.mSurfaceAnimator.hasLeash()) {
            this.mSurfaceAnimator.cancelAnimation();
        }
        if (!this.mReparenting) {
            onSyncReparent(windowContainer, this.mParent);
            if (this.mParent != null && this.mParent.mDisplayContent != null && this.mDisplayContent != this.mParent.mDisplayContent) {
                onDisplayChanged(this.mParent.mDisplayContent);
            }
            onParentChanged(this.mParent, windowContainer);
        }
        this.mWindowContainerExt.onChildAdded(this, parent);
        this.mWindowContainerExt.onParentConfirmed(this);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    void onParentChanged(com.android.server.wm.ConfigurationContainer newParent, com.android.server.wm.ConfigurationContainer oldParent) {
        super.onParentChanged(newParent, oldParent);
        if (this.mParent == null) {
            return;
        }
        if (this.mSurfaceControl == null) {
            createSurfaceControl(false);
        } else if (this.mWindowContainerExt.blockUpdateSurfacePosition(this)) {
            return;
        } else {
            reparentSurfaceControl(getSyncTransaction(), this.mParent.mSurfaceControl);
        }
        this.mParent.assignChildLayers();
    }

    void createSurfaceControl(boolean force) {
        setInitialSurfaceControlProperties(makeSurface());
    }

    void setInitialSurfaceControlProperties(android.view.SurfaceControl.Builder b) {
        setSurfaceControl(b.setCallsite("WindowContainer.setInitialSurfaceControlProperties").build());
        if (showSurfaceOnCreation()) {
            getSyncTransaction().show(this.mSurfaceControl);
            this.mWindowContainerExt.showSurfaceControl(this);
        }
        updateSurfacePositionNonOrganized();
        if (this.mLastMagnificationSpec != null) {
            applyMagnificationSpec(getSyncTransaction(), this.mLastMagnificationSpec);
        }
    }

    void migrateToNewSurfaceControl(android.view.SurfaceControl.Transaction t) {
        t.remove(this.mSurfaceControl);
        this.mLastSurfacePosition.set(0, 0);
        this.mLastDeltaRotation = 0;
        android.view.SurfaceControl.Builder b = this.mWmService.makeSurfaceBuilder(null).setContainerLayer().setName(getName());
        setInitialSurfaceControlProperties(b);
        t.reparent(this.mSurfaceControl, this.mParent != null ? this.mParent.mSurfaceControl : null);
        if (this.mLastRelativeToLayer != null) {
            t.setRelativeLayer(this.mSurfaceControl, this.mLastRelativeToLayer, this.mLastLayer);
        } else {
            t.setLayer(this.mSurfaceControl, this.mLastLayer);
        }
        for (int i = 0; i < this.mChildren.size(); i++) {
            android.view.SurfaceControl sc = this.mChildren.get(i).getSurfaceControl();
            if (sc != null) {
                t.reparent(sc, this.mSurfaceControl);
                if (this.mChildren.get(i).asActivityRecord() != null && this.mTransitionController.inPlayingTransition(this.mChildren.get(i))) {
                    this.mChildren.get(i).asActivityRecord().getWrapper().getExtImpl().setParentChanged(true);
                }
            }
        }
        if (this.mOverlayHost != null) {
            this.mOverlayHost.setParent(t, this.mSurfaceControl);
        }
        scheduleAnimation();
    }

    private void checkPreconditions() {
        if (android.os.Build.IS_AGING_VERSION && !java.lang.Thread.holdsLock(this.mWmService.mGlobalLock)) {
            android.util.Slog.d(TAG, this + " Unlocked access to synchronized WindowContainer. ", new java.lang.Throwable());
        }
    }

    protected void addChild(E child, java.util.Comparator<E> comparator) {
        if (!child.mReparenting && child.getParent() != null) {
            throw new java.lang.IllegalArgumentException("addChild: container=" + child.getName() + " is already a child of container=" + child.getParent().getName() + " can't add to container=" + getName());
        }
        checkPreconditions();
        this.mWindowContainerExt.addChild(this, child);
        int positionToAdd = -1;
        if (comparator != null) {
            int count = this.mChildren.size();
            int i = 0;
            while (true) {
                if (i >= count) {
                    break;
                }
                if (comparator.compare(child, this.mChildren.get(i)) >= 0) {
                    i++;
                } else {
                    positionToAdd = i;
                    break;
                }
            }
        }
        if (positionToAdd == -1) {
            this.mChildren.add(child);
        } else {
            this.mChildren.add(positionToAdd, child);
        }
        child.setParent(this);
    }

    void addChild(E child, int index) {
        if (!child.mReparenting && child.getParent() != null) {
            throw new java.lang.IllegalArgumentException("addChild: container=" + child.getName() + " is already a child of container=" + child.getParent().getName() + " can't add to container=" + getName() + "\n callers=" + android.os.Debug.getCallers(15, "\n"));
        }
        if ((index < 0 && index != Integer.MIN_VALUE) || (index > this.mChildren.size() && index != Integer.MAX_VALUE)) {
            throw new java.lang.IllegalArgumentException("addChild: invalid position=" + index + ", children number=" + this.mChildren.size());
        }
        checkPreconditions();
        if (index == Integer.MAX_VALUE) {
            index = this.mChildren.size();
        } else if (index == Integer.MIN_VALUE) {
            index = 0;
        }
        this.mChildren.add(index, child);
        child.setParent(this);
    }

    private void onChildAdded(com.android.server.wm.WindowContainer child) {
        this.mTreeWeight += child.mTreeWeight;
        for (com.android.server.wm.WindowContainer parent = getParent(); parent != null; parent = parent.getParent()) {
            parent.mTreeWeight += child.mTreeWeight;
        }
        onChildVisibleRequestedChanged(child);
        onChildPositionChanged(child);
    }

    void removeChild(E child) {
        checkPreconditions();
        if (this.mChildren.remove(child)) {
            this.mWindowContainerExt.removeChild(this, child);
            onChildRemoved(child);
            if (!child.mReparenting) {
                child.setParent(null);
                return;
            }
            return;
        }
        throw new java.lang.IllegalArgumentException("removeChild: container=" + child.getName() + " is not a child of container=" + getName());
    }

    private void onChildRemoved(com.android.server.wm.WindowContainer child) {
        this.mWindowContainerExt.onChildRemoved(child, this);
        this.mTreeWeight -= child.mTreeWeight;
        for (com.android.server.wm.WindowContainer parent = getParent(); parent != null; parent = parent.getParent()) {
            parent.mTreeWeight -= child.mTreeWeight;
        }
        onChildVisibleRequestedChanged(null);
        onChildPositionChanged(child);
    }

    void removeImmediately() {
        checkPreconditions();
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        if (dc != null) {
            dc.mClosingChangingContainers.remove(this);
            this.mSurfaceFreezer.unfreeze(getSyncTransaction());
        }
        while (!this.mChildren.isEmpty()) {
            E child = this.mChildren.peekLast();
            child.removeImmediately();
            if (this.mChildren.remove(child)) {
                onChildRemoved(child);
            }
        }
        if (this.mSurfaceControl != null) {
            getSyncTransaction().remove(this.mSurfaceControl);
            setSurfaceControl(null);
            this.mLastSurfacePosition.set(0, 0);
            this.mLastDeltaRotation = 0;
            scheduleAnimation();
        }
        if (this.mOverlayHost != null) {
            this.mOverlayHost.release();
            this.mOverlayHost = null;
        }
        if (this.mParent != null) {
            this.mParent.removeChild(this);
        }
        for (int i = this.mListeners.size() - 1; i >= 0; i--) {
            this.mListeners.get(i).onRemoved();
        }
    }

    int getTreeWeight() {
        return this.mTreeWeight;
    }

    int getPrefixOrderIndex() {
        if (this.mParent == null) {
            return 0;
        }
        return this.mParent.getPrefixOrderIndex(this);
    }

    private int getPrefixOrderIndex(com.android.server.wm.WindowContainer child) {
        com.android.server.wm.WindowContainer childI;
        int order = 0;
        for (int i = 0; i < this.mChildren.size() && child != (childI = this.mChildren.get(i)); i++) {
            order += childI.mTreeWeight;
        }
        if (this.mParent != null) {
            order += this.mParent.getPrefixOrderIndex(this);
        }
        return order + 1;
    }

    void removeIfPossible() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            wc.removeIfPossible();
        }
    }

    boolean hasChild(E child) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            E current = this.mChildren.get(i);
            if (current == child || current.hasChild(child)) {
                return true;
            }
        }
        return false;
    }

    boolean isDescendantOf(com.android.server.wm.WindowContainer ancestor) {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent == ancestor) {
            return true;
        }
        return parent != null && parent.isDescendantOf(ancestor);
    }

    void positionChildAt(int position, E child, boolean includingParents) {
        if (child.getParent() != this) {
            throw new java.lang.IllegalArgumentException("positionChildAt: container=" + child.getName() + " is not a child of container=" + getName() + " current parent=" + child.getParent());
        }
        checkPreconditions();
        if (position >= this.mChildren.size() - 1) {
            position = Integer.MAX_VALUE;
        } else if (position <= 0) {
            position = Integer.MIN_VALUE;
        }
        switch (position) {
            case Integer.MIN_VALUE:
                if (this.mChildren.peekFirst() != child) {
                    this.mChildren.remove(child);
                    this.mChildren.addFirst(child);
                    onChildPositionChanged(child);
                }
                if (includingParents && getParent() != null) {
                    getParent().positionChildAt(Integer.MIN_VALUE, this, true);
                    return;
                }
                return;
            case Integer.MAX_VALUE:
                if (this.mChildren.peekLast() != child) {
                    if (this.mWindowContainerExt.shouldIgnorePositionChildAtTop(this, child)) {
                        return;
                    }
                    this.mChildren.remove(child);
                    this.mChildren.add(child);
                    onChildPositionChanged(child);
                }
                if (includingParents && getParent() != null) {
                    getParent().positionChildAt(Integer.MAX_VALUE, this, true);
                    return;
                }
                return;
            default:
                if (this.mChildren.indexOf(child) != position) {
                    this.mChildren.remove(child);
                    this.mChildren.add(position, child);
                    onChildPositionChanged(child);
                    return;
                }
                return;
        }
    }

    void onChildPositionChanged(com.android.server.wm.WindowContainer child) {
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void onRequestedOverrideConfigurationChanged(android.content.res.Configuration overrideConfiguration) {
        int diff = diffRequestedOverrideBounds(overrideConfiguration.windowConfiguration.getBounds());
        super.onRequestedOverrideConfigurationChanged(overrideConfiguration);
        if (this.mParent != null) {
            this.mParent.onDescendantOverrideConfigurationChanged();
        }
        if (diff == 0) {
            return;
        }
        if ((diff & 2) == 2) {
            onResize();
        } else {
            onMovedByResize();
        }
    }

    void onDescendantOverrideConfigurationChanged() {
        if (this.mParent != null) {
            this.mParent.onDescendantOverrideConfigurationChanged();
        }
    }

    void onDisplayChanged(com.android.server.wm.DisplayContent dc) {
        if (this.mDisplayContent != null && this.mDisplayContent != dc) {
            this.mDisplayContent.mClosingChangingContainers.remove(this);
            if (this.mDisplayContent.mChangingContainers.remove(this)) {
                this.mSurfaceFreezer.unfreeze(getSyncTransaction());
            }
        }
        this.mDisplayContent = dc;
        if (dc != null && dc != this) {
            dc.getPendingTransaction().merge(this.mPendingTransaction);
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = this.mChildren.get(i);
            child.onDisplayChanged(dc);
        }
        for (int i2 = this.mListeners.size() - 1; i2 >= 0; i2--) {
            this.mListeners.get(i2).onDisplayChanged(dc);
        }
    }

    public boolean hasInsetsSourceProvider() {
        return this.mInsetsSourceProviders != null;
    }

    public android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> getInsetsSourceProviders() {
        if (this.mInsetsSourceProviders == null) {
            this.mInsetsSourceProviders = new android.util.SparseArray<>();
        }
        return this.mInsetsSourceProviders;
    }

    public final com.android.server.wm.DisplayContent getDisplayContent() {
        return this.mDisplayContent;
    }

    com.android.server.wm.DisplayArea getDisplayArea() {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null) {
            return parent.getDisplayArea();
        }
        return null;
    }

    com.android.server.wm.RootDisplayArea getRootDisplayArea() {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null) {
            return parent.getRootDisplayArea();
        }
        return null;
    }

    com.android.server.wm.TaskDisplayArea getTaskDisplayArea() {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null) {
            return parent.getTaskDisplayArea();
        }
        return null;
    }

    boolean isAttached() {
        com.android.server.wm.WindowContainer parent = getParent();
        return parent != null && parent.isAttached();
    }

    void onResize() {
        if (this.mControllableInsetProvider != null) {
            this.mControllableInsetProvider.onWindowContainerBoundsChanged();
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            wc.onParentResize();
        }
    }

    void onParentResize() {
        if (hasOverrideBounds()) {
            return;
        }
        onResize();
    }

    void onMovedByResize() {
        if (this.mControllableInsetProvider != null) {
            this.mControllableInsetProvider.onWindowContainerBoundsChanged();
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            wc.onMovedByResize();
        }
    }

    void resetDragResizingChangeReported() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            wc.resetDragResizingChangeReported();
        }
    }

    boolean canCustomizeAppTransition() {
        return false;
    }

    final boolean isAnimating(int flags, int typesToCheck) {
        return getAnimatingContainer(flags, typesToCheck) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @java.lang.Deprecated
    public final boolean isAnimating(int flags) {
        return isAnimating(flags, -1);
    }

    boolean isWaitingForTransitionStart() {
        return false;
    }

    boolean isAppTransitioning() {
        return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowContainer$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRecord) obj).isAnimating(3);
            }
        }) != null;
    }

    boolean inTransitionSelfOrParent() {
        if (!this.mTransitionController.isShellTransitionsEnabled()) {
            return isAnimating(3, 9);
        }
        return inTransition();
    }

    final boolean isAnimating() {
        return isAnimating(0);
    }

    boolean isChangingAppTransition() {
        return this.mDisplayContent != null && this.mDisplayContent.mChangingContainers.contains(this);
    }

    boolean inTransition() {
        return this.mTransitionController.inTransition(this);
    }

    boolean isExitAnimationRunningSelfOrChild() {
        if (!this.mTransitionController.isShellTransitionsEnabled()) {
            return isAnimating(5, 25);
        }
        if (this.mChildren.isEmpty() && inTransition()) {
            return true;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = this.mChildren.get(i);
            if (child.isExitAnimationRunningSelfOrChild()) {
                return true;
            }
        }
        return false;
    }

    void sendAppVisibilityToClients() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            wc.sendAppVisibilityToClients();
        }
    }

    boolean hasContentToDisplay() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            if (wc.hasContentToDisplay()) {
                return true;
            }
        }
        return false;
    }

    boolean isVisible() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            if (wc.isVisible()) {
                return true;
            }
        }
        return false;
    }

    void checkCachedSurfaceBufferRelease() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            wc.checkCachedSurfaceBufferRelease();
        }
    }

    boolean isVisibleRequested() {
        return this.mVisibleRequested;
    }

    boolean setVisibleRequested(boolean visible) {
        if (this.mVisibleRequested == visible) {
            return false;
        }
        this.mVisibleRequested = visible;
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null) {
            parent.onChildVisibleRequestedChanged(this);
        }
        for (int i = this.mListeners.size() - 1; i >= 0; i--) {
            this.mListeners.get(i).onVisibleRequestedChanged(this.mVisibleRequested);
        }
        return true;
    }

    protected boolean onChildVisibleRequestedChanged(com.android.server.wm.WindowContainer child) {
        boolean childVisReq = child != null && child.isVisibleRequested();
        boolean newVisReq = this.mVisibleRequested;
        if (childVisReq && !this.mVisibleRequested) {
            newVisReq = true;
        } else if (!childVisReq && this.mVisibleRequested) {
            newVisReq = false;
            int i = this.mChildren.size() - 1;
            while (true) {
                if (i >= 0) {
                    com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
                    if (wc == child || !wc.isVisibleRequested()) {
                        i--;
                    } else {
                        newVisReq = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return setVisibleRequested(newVisReq);
    }

    void onChildVisibilityRequested(boolean visible) {
        if (!visible) {
            boolean skipUnfreeze = false;
            if (asTaskFragment() != null) {
                skipUnfreeze = asTaskFragment().setClosingChangingStartBoundsIfNeeded();
            }
            if (!skipUnfreeze) {
                this.mSurfaceFreezer.unfreeze(getSyncTransaction());
            }
        }
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null) {
            parent.onChildVisibilityRequested(visible);
        }
    }

    boolean isClosingWhenResizing() {
        return this.mDisplayContent != null && this.mDisplayContent.mClosingChangingContainers.containsKey(this);
    }

    void writeIdentifierToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, java.lang.System.identityHashCode(this));
        proto.write(1120986464258L, -10000);
        proto.write(1138166333443L, "WindowContainer");
        proto.end(token);
    }

    boolean isFocusable() {
        com.android.server.wm.WindowContainer parent = getParent();
        return (parent == null || parent.isFocusable()) && this.mIsFocusable;
    }

    boolean setFocusable(boolean focusable) {
        if (this.mIsFocusable == focusable) {
            return false;
        }
        this.mIsFocusable = focusable;
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    boolean isOnTop() {
        com.android.server.wm.WindowContainer parent = getParent();
        return parent != 0 && parent.getTopChild() == this && parent.isOnTop();
    }

    E getTopChild() {
        return this.mChildren.peekLast();
    }

    boolean handleCompleteDeferredRemoval() {
        boolean stillDeferringRemoval = false;
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (i >= this.mChildren.size()) {
                android.util.Slog.d(TAG, "checkCompleteDeferredRemoval IndexOutOfBoundsE this = " + this);
            } else {
                com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
                stillDeferringRemoval |= wc.handleCompleteDeferredRemoval();
                if (!hasChild()) {
                    return false;
                }
            }
        }
        return stillDeferringRemoval;
    }

    void checkAppWindowsReadyToShow() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            wc.checkAppWindowsReadyToShow();
        }
    }

    void onAppTransitionDone() {
        if (this.mSurfaceFreezer.hasLeash()) {
            this.mSurfaceFreezer.unfreeze(getSyncTransaction());
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            wc.onAppTransitionDone();
        }
    }

    boolean onDescendantOrientationChanged(com.android.server.wm.WindowContainer requestingContainer) {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent == null) {
            return false;
        }
        return parent.onDescendantOrientationChanged(requestingContainer);
    }

    boolean handlesOrientationChangeFromDescendant(int orientation) {
        com.android.server.wm.WindowContainer parent = getParent();
        return parent != null && parent.handlesOrientationChangeFromDescendant(orientation);
    }

    int getRequestedConfigurationOrientation() {
        return getRequestedConfigurationOrientation(false);
    }

    int getRequestedConfigurationOrientation(boolean forDisplay) {
        return getRequestedConfigurationOrientation(forDisplay, getOverrideOrientation());
    }

    int getRequestedConfigurationOrientation(boolean forDisplay, int requestedOrientation) {
        int requestedOrientation2 = this.mWindowContainerExt.getFixedScreenOrientation(this, requestedOrientation);
        com.android.server.wm.RootDisplayArea root = getRootDisplayArea();
        if (forDisplay && root != null && root.isOrientationDifferentFromDisplay()) {
            requestedOrientation2 = android.content.pm.ActivityInfo.reverseOrientation(requestedOrientation2);
        }
        if (requestedOrientation2 == 5) {
            if (this.mDisplayContent != null) {
                return this.mDisplayContent.getNaturalConfigurationOrientation();
            }
            return 0;
        }
        if (requestedOrientation2 == 14) {
            return getConfiguration().orientation;
        }
        if (android.content.pm.ActivityInfo.isFixedOrientationLandscape(requestedOrientation2)) {
            return 2;
        }
        if (android.content.pm.ActivityInfo.isFixedOrientationPortrait(requestedOrientation2)) {
            return 1;
        }
        return 0;
    }

    void setOrientation(int orientation) {
        setOrientation(orientation, null);
    }

    void setOrientation(int orientation, com.android.server.wm.WindowContainer requestingContainer) {
        int orientation2 = this.mWindowContainerExt.adjustOrientationForBracketMode(orientation);
        if (getOverrideOrientation() == orientation2 && !this.mWindowContainerExt.forceUpdateConfig(requestingContainer, orientation2)) {
            return;
        }
        setOverrideOrientation(orientation2);
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null) {
            if ((getConfiguration().orientation != getRequestedConfigurationOrientation() && (inMultiWindowMode() || !handlesOrientationChangeFromDescendant(orientation2))) || this.mWindowContainerExt.shouldUpdateConfig(requestingContainer, orientation2)) {
                onConfigurationChanged(parent.getConfiguration());
            }
            onDescendantOrientationChanged(requestingContainer);
        }
    }

    int getOrientation() {
        return getOrientation(getOverrideOrientation());
    }

    int getOrientation(int candidate) {
        this.mLastOrientationSource = null;
        if (!providesOrientation()) {
            return -2;
        }
        if (getOverrideOrientation() != -2 && getOverrideOrientation() != -1) {
            this.mLastOrientationSource = this;
            return getOverrideOrientation();
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
            if (!this.mWindowContainerExt.hookGetOrientation(wc)) {
                int orientation = wc.getOrientation(candidate == 3 ? 3 : -2);
                if (orientation == 3) {
                    this.mWindowContainerExt.logBehindOrientation(wc);
                    candidate = orientation;
                    this.mLastOrientationSource = wc;
                } else if (orientation != -2 && (orientation != -1 || wc.providesOrientation())) {
                    if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                        android.util.Slog.v(TAG, "getOrientation: wc = " + wc + " is requesting orientation " + android.content.pm.ActivityInfo.screenOrientationToString(orientation) + " wc.fillsParent() = " + wc.fillsParent());
                    } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(wc.toString());
                        long protoLogParam1 = orientation;
                        java.lang.String protoLogParam2 = java.lang.String.valueOf(android.content.pm.ActivityInfo.screenOrientationToString(orientation));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -5231580410559054259L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), protoLogParam2);
                    }
                    this.mLastOrientationSource = wc;
                    return orientation;
                }
            }
        }
        return candidate;
    }

    protected int getOverrideOrientation() {
        return this.mOverrideOrientation;
    }

    protected void setOverrideOrientation(int orientation) {
        this.mOverrideOrientation = orientation;
    }

    com.android.server.wm.WindowContainer getLastOrientationSource() {
        com.android.server.wm.WindowContainer nextSource;
        com.android.server.wm.WindowContainer<E> windowContainer = this.mLastOrientationSource;
        if (windowContainer != null && windowContainer != this && (nextSource = windowContainer.getLastOrientationSource()) != null) {
            return nextSource;
        }
        return windowContainer;
    }

    boolean providesOrientation() {
        return fillsParent();
    }

    boolean fillsParent() {
        return false;
    }

    static int computeScreenLayout(int sourceScreenLayout, int screenWidthDp, int screenHeightDp) {
        int longSize = java.lang.Math.max(screenWidthDp, screenHeightDp);
        int shortSize = java.lang.Math.min(screenWidthDp, screenHeightDp);
        return android.content.res.Configuration.reduceScreenLayout(sourceScreenLayout & 63, longSize, shortSize);
    }

    void switchUser(int userId) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            this.mChildren.get(i).switchUser(userId);
        }
    }

    boolean showToCurrentUser() {
        return true;
    }

    void forAllWindowContainers(java.util.function.Consumer<com.android.server.wm.WindowContainer> callback) {
        checkPreconditions();
        callback.accept(this);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            this.mChildren.get(i).forAllWindowContainers(callback);
        }
    }

    boolean forAllWindows(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        if (traverseTopToBottom) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                if (i >= this.mChildren.size()) {
                    android.util.Slog.d(TAG, "forAllWindows IndexOutOfBoundsE this = " + this);
                } else if (this.mChildren.get(i).forAllWindows(callback, traverseTopToBottom)) {
                    return true;
                }
            }
            return false;
        }
        int count = this.mChildren.size();
        for (int i2 = 0; i2 < count; i2++) {
            if (i2 >= this.mChildren.size()) {
                android.util.Slog.d(TAG, "forAllWindows IndexOutOfBoundsE this = " + this);
            } else if (this.mChildren.get(i2).forAllWindows(callback, traverseTopToBottom)) {
                return true;
            }
        }
        return false;
    }

    void forAllWindows(java.util.function.Consumer<com.android.server.wm.WindowState> callback, boolean traverseTopToBottom) {
        com.android.server.wm.WindowContainer<E>.ForAllWindowsConsumerWrapper wrapper = obtainConsumerWrapper(callback);
        forAllWindows(wrapper, traverseTopToBottom);
        wrapper.release();
    }

    boolean forAllActivities(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback) {
        return forAllActivities(callback, true);
    }

    boolean forAllActivities(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        if (traverseTopToBottom) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                if (i >= this.mChildren.size()) {
                    android.util.Slog.d(TAG, "forAllActivities IndexOutOfBoundsE this = " + this);
                } else if (this.mChildren.get(i).forAllActivities(callback, traverseTopToBottom)) {
                    return true;
                }
            }
            return false;
        }
        int count = this.mChildren.size();
        for (int i2 = 0; i2 < count; i2++) {
            if (i2 >= this.mChildren.size()) {
                android.util.Slog.d(TAG, "forAllActivities IndexOutOfBoundsE this = " + this);
                return false;
            }
            if (this.mChildren.get(i2).forAllActivities(callback, traverseTopToBottom)) {
                return true;
            }
        }
        return false;
    }

    void forAllActivities(java.util.function.Consumer<com.android.server.wm.ActivityRecord> callback) {
        forAllActivities(callback, true);
    }

    void forAllActivities(java.util.function.Consumer<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        if (traverseTopToBottom) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                if (i >= this.mChildren.size()) {
                    android.util.Slog.d(TAG, "forAllActivities IndexOutOfBoundsE this = " + this);
                } else {
                    this.mChildren.get(i).forAllActivities(callback, traverseTopToBottom);
                }
            }
            return;
        }
        int count = this.mChildren.size();
        for (int i2 = 0; i2 < count; i2++) {
            if (i2 >= this.mChildren.size()) {
                android.util.Slog.d(TAG, "forAllActivities IndexOutOfBoundsE this = " + this);
                return;
            }
            this.mChildren.get(i2).forAllActivities(callback, traverseTopToBottom);
        }
    }

    final boolean forAllActivities(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, com.android.server.wm.WindowContainer boundary, boolean includeBoundary, boolean traverseTopToBottom) {
        return forAllActivities(callback, boundary, includeBoundary, traverseTopToBottom, new boolean[1]);
    }

    private boolean forAllActivities(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, com.android.server.wm.WindowContainer boundary, boolean includeBoundary, boolean traverseTopToBottom, boolean[] boundaryFound) {
        checkPreconditions();
        if (traverseTopToBottom) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                if (processForAllActivitiesWithBoundary(callback, boundary, includeBoundary, traverseTopToBottom, boundaryFound, this.mChildren.get(i))) {
                    return true;
                }
            }
            return false;
        }
        int count = this.mChildren.size();
        for (int i2 = 0; i2 < count; i2++) {
            if (processForAllActivitiesWithBoundary(callback, boundary, includeBoundary, traverseTopToBottom, boundaryFound, this.mChildren.get(i2))) {
                return true;
            }
        }
        return false;
    }

    private boolean processForAllActivitiesWithBoundary(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, com.android.server.wm.WindowContainer boundary, boolean includeBoundary, boolean traverseTopToBottom, boolean[] boundaryFound, com.android.server.wm.WindowContainer wc) {
        if (wc == boundary) {
            boundaryFound[0] = true;
            if (!includeBoundary) {
                return false;
            }
        }
        if (boundaryFound[0]) {
            return wc.forAllActivities(callback, traverseTopToBottom);
        }
        return wc.forAllActivities(callback, boundary, includeBoundary, traverseTopToBottom, boundaryFound);
    }

    boolean hasActivity() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (this.mChildren.get(i).hasActivity()) {
                return true;
            }
        }
        return false;
    }

    com.android.server.wm.ActivityRecord getActivity(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback) {
        return getActivity(callback, true);
    }

    com.android.server.wm.ActivityRecord getActivity(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom) {
        return getActivity(callback, traverseTopToBottom, null);
    }

    com.android.server.wm.ActivityRecord getActivity(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom, com.android.server.wm.ActivityRecord boundary) {
        if (traverseTopToBottom) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowContainer wc = this.mChildren.get(i);
                if (wc == boundary) {
                    return boundary;
                }
                com.android.server.wm.ActivityRecord r = wc.getActivity(callback, traverseTopToBottom, boundary);
                if (r != null) {
                    return r;
                }
            }
            return null;
        }
        int count = this.mChildren.size();
        for (int i2 = 0; i2 < count; i2++) {
            com.android.server.wm.WindowContainer wc2 = this.mChildren.get(i2);
            if (wc2 == boundary) {
                return boundary;
            }
            com.android.server.wm.ActivityRecord r2 = wc2.getActivity(callback, traverseTopToBottom, boundary);
            if (r2 != null) {
                return r2;
            }
        }
        return null;
    }

    final com.android.server.wm.ActivityRecord getActivity(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, com.android.server.wm.WindowContainer boundary, boolean includeBoundary, boolean traverseTopToBottom) {
        return getActivity(callback, boundary, includeBoundary, traverseTopToBottom, new boolean[1]);
    }

    private com.android.server.wm.ActivityRecord getActivity(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, com.android.server.wm.WindowContainer boundary, boolean includeBoundary, boolean traverseTopToBottom, boolean[] boundaryFound) {
        if (traverseTopToBottom) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.ActivityRecord r = processGetActivityWithBoundary(callback, boundary, includeBoundary, traverseTopToBottom, boundaryFound, this.mChildren.get(i));
                if (r != null) {
                    return r;
                }
            }
            return null;
        }
        int count = this.mChildren.size();
        for (int i2 = 0; i2 < count; i2++) {
            com.android.server.wm.ActivityRecord r2 = processGetActivityWithBoundary(callback, boundary, includeBoundary, traverseTopToBottom, boundaryFound, this.mChildren.get(i2));
            if (r2 != null) {
                return r2;
            }
        }
        return null;
    }

    int getDistanceFromTop(com.android.server.wm.WindowContainer child) {
        int idx = this.mChildren.indexOf(child);
        if (idx < 0) {
            return -1;
        }
        return (this.mChildren.size() - 1) - idx;
    }

    private com.android.server.wm.ActivityRecord processGetActivityWithBoundary(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, com.android.server.wm.WindowContainer boundary, boolean includeBoundary, boolean traverseTopToBottom, boolean[] boundaryFound, com.android.server.wm.WindowContainer wc) {
        if (wc == null) {
            android.util.Slog.d(TAG, "WindowContainer is null");
            return null;
        }
        if (wc == boundary || boundary == null) {
            boundaryFound[0] = true;
            if (!includeBoundary) {
                return null;
            }
        }
        if (boundaryFound[0]) {
            return wc.getActivity(callback, traverseTopToBottom);
        }
        return wc.getActivity(callback, boundary, includeBoundary, traverseTopToBottom, boundaryFound);
    }

    static <T> java.util.function.Predicate<T> alwaysTruePredicate() {
        return com.android.server.wm.utils.AlwaysTruePredicate.INSTANCE;
    }

    com.android.server.wm.ActivityRecord getActivityAbove(com.android.server.wm.ActivityRecord r) {
        return getActivity(alwaysTruePredicate(), r, false, false);
    }

    com.android.server.wm.ActivityRecord getActivityBelow(com.android.server.wm.ActivityRecord r) {
        return getActivity(alwaysTruePredicate(), r, false, true);
    }

    com.android.server.wm.ActivityRecord getBottomMostActivity() {
        return getActivity(alwaysTruePredicate(), false);
    }

    com.android.server.wm.ActivityRecord getTopMostActivity() {
        return getActivity(alwaysTruePredicate(), true);
    }

    com.android.server.wm.ActivityRecord getTopActivity(boolean includeFinishing, boolean includeOverlays) {
        if (includeFinishing) {
            if (includeOverlays) {
                return getActivity(alwaysTruePredicate());
            }
            return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowContainer$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.WindowContainer.lambda$getTopActivity$1((com.android.server.wm.ActivityRecord) obj);
                }
            });
        }
        if (includeOverlays) {
            return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowContainer$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.WindowContainer.lambda$getTopActivity$2((com.android.server.wm.ActivityRecord) obj);
                }
            });
        }
        return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowContainer$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.WindowContainer.lambda$getTopActivity$3((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$getTopActivity$1(com.android.server.wm.ActivityRecord r) {
        return !r.isTaskOverlay();
    }

    static /* synthetic */ boolean lambda$getTopActivity$2(com.android.server.wm.ActivityRecord r) {
        return !r.finishing;
    }

    static /* synthetic */ boolean lambda$getTopActivity$3(com.android.server.wm.ActivityRecord r) {
        return (r.finishing || r.isTaskOverlay()) ? false : true;
    }

    void forAllWallpaperWindows(java.util.function.Consumer<com.android.server.wm.WallpaperWindowToken> callback) {
        checkPreconditions();
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            this.mChildren.get(i).forAllWallpaperWindows(callback);
        }
    }

    boolean forAllTasks(java.util.function.Predicate<com.android.server.wm.Task> callback) {
        checkPreconditions();
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (this.mChildren.get(i).forAllTasks(callback)) {
                return true;
            }
        }
        return false;
    }

    boolean forAllLeafTasks(java.util.function.Predicate<com.android.server.wm.Task> callback) {
        checkPreconditions();
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (this.mChildren.get(i).forAllLeafTasks(callback)) {
                return true;
            }
        }
        return false;
    }

    boolean forAllLeafTaskFragments(java.util.function.Predicate<com.android.server.wm.TaskFragment> callback) {
        checkPreconditions();
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (this.mChildren.get(i).forAllLeafTaskFragments(callback)) {
                return true;
            }
        }
        return false;
    }

    boolean forAllRootTasks(java.util.function.Predicate<com.android.server.wm.Task> callback) {
        return forAllRootTasks(callback, true);
    }

    boolean forAllRootTasks(java.util.function.Predicate<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        int count = this.mChildren.size();
        if (traverseTopToBottom) {
            for (int i = count - 1; i >= 0; i--) {
                if (this.mChildren.get(i).forAllRootTasks(callback, traverseTopToBottom)) {
                    return true;
                }
            }
            return false;
        }
        int i2 = 0;
        while (i2 < count) {
            if (this.mChildren.get(i2).forAllRootTasks(callback, traverseTopToBottom)) {
                return true;
            }
            int newCount = this.mChildren.size();
            int i3 = i2 - (count - newCount);
            count = newCount;
            i2 = i3 + 1;
        }
        return false;
    }

    void forAllTasks(java.util.function.Consumer<com.android.server.wm.Task> callback) {
        forAllTasks(callback, true);
    }

    void forAllTasks(java.util.function.Consumer<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        int count = this.mChildren.size();
        if (traverseTopToBottom) {
            for (int i = count - 1; i >= 0; i--) {
                this.mChildren.get(i).forAllTasks(callback, traverseTopToBottom);
            }
            return;
        }
        for (int i2 = 0; i2 < count; i2++) {
            this.mChildren.get(i2).forAllTasks(callback, traverseTopToBottom);
        }
    }

    void forAllTaskFragments(java.util.function.Consumer<com.android.server.wm.TaskFragment> callback) {
        forAllTaskFragments(callback, true);
    }

    void forAllTaskFragments(java.util.function.Consumer<com.android.server.wm.TaskFragment> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        int count = this.mChildren.size();
        if (traverseTopToBottom) {
            for (int i = count - 1; i >= 0; i--) {
                this.mChildren.get(i).forAllTaskFragments(callback, traverseTopToBottom);
            }
            return;
        }
        for (int i2 = 0; i2 < count; i2++) {
            this.mChildren.get(i2).forAllTaskFragments(callback, traverseTopToBottom);
        }
    }

    void forAllLeafTasks(java.util.function.Consumer<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        int count = this.mChildren.size();
        if (traverseTopToBottom) {
            for (int i = count - 1; i >= 0; i--) {
                this.mChildren.get(i).forAllLeafTasks(callback, traverseTopToBottom);
            }
            return;
        }
        for (int i2 = 0; i2 < count; i2++) {
            this.mChildren.get(i2).forAllLeafTasks(callback, traverseTopToBottom);
        }
    }

    void forAllLeafTaskFragments(java.util.function.Consumer<com.android.server.wm.TaskFragment> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        int count = this.mChildren.size();
        if (traverseTopToBottom) {
            for (int i = count - 1; i >= 0; i--) {
                this.mChildren.get(i).forAllLeafTaskFragments(callback, traverseTopToBottom);
            }
            return;
        }
        for (int i2 = 0; i2 < count; i2++) {
            this.mChildren.get(i2).forAllLeafTaskFragments(callback, traverseTopToBottom);
        }
    }

    void forAllRootTasks(java.util.function.Consumer<com.android.server.wm.Task> callback) {
        forAllRootTasks(callback, true);
    }

    void forAllRootTasks(java.util.function.Consumer<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        int count = this.mChildren.size();
        if (traverseTopToBottom) {
            for (int i = count - 1; i >= 0; i--) {
                if (i < this.mChildren.size()) {
                    this.mChildren.get(i).forAllRootTasks(callback, traverseTopToBottom);
                }
            }
            return;
        }
        int i2 = 0;
        while (i2 < count) {
            if (i2 < this.mChildren.size()) {
                this.mChildren.get(i2).forAllRootTasks(callback, traverseTopToBottom);
                int newCount = this.mChildren.size();
                i2 -= count - newCount;
                count = newCount;
            }
            i2++;
        }
    }

    com.android.server.wm.Task getTaskBelow(com.android.server.wm.Task t) {
        return getTask(alwaysTruePredicate(), t, false, true);
    }

    com.android.server.wm.Task getBottomMostTask() {
        return getTask(alwaysTruePredicate(), false);
    }

    com.android.server.wm.Task getTopMostTask() {
        return getTask(alwaysTruePredicate(), true);
    }

    com.android.server.wm.Task getTask(java.util.function.Predicate<com.android.server.wm.Task> callback) {
        return getTask(callback, true);
    }

    com.android.server.wm.Task getTask(java.util.function.Predicate<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        if (traverseTopToBottom) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.Task t = this.mChildren.get(i).getTask(callback, traverseTopToBottom);
                if (t != null) {
                    return t;
                }
            }
            return null;
        }
        int count = this.mChildren.size();
        for (int i2 = 0; i2 < count; i2++) {
            com.android.server.wm.Task t2 = this.mChildren.get(i2).getTask(callback, traverseTopToBottom);
            if (t2 != null) {
                return t2;
            }
        }
        return null;
    }

    final com.android.server.wm.Task getTask(java.util.function.Predicate<com.android.server.wm.Task> callback, com.android.server.wm.WindowContainer boundary, boolean includeBoundary, boolean traverseTopToBottom) {
        return getTask(callback, boundary, includeBoundary, traverseTopToBottom, new boolean[1]);
    }

    private com.android.server.wm.Task getTask(java.util.function.Predicate<com.android.server.wm.Task> callback, com.android.server.wm.WindowContainer boundary, boolean includeBoundary, boolean traverseTopToBottom, boolean[] boundaryFound) {
        if (traverseTopToBottom) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.Task t = processGetTaskWithBoundary(callback, boundary, includeBoundary, traverseTopToBottom, boundaryFound, this.mChildren.get(i));
                if (t != null) {
                    return t;
                }
            }
            return null;
        }
        int count = this.mChildren.size();
        for (int i2 = 0; i2 < count; i2++) {
            com.android.server.wm.Task t2 = processGetTaskWithBoundary(callback, boundary, includeBoundary, traverseTopToBottom, boundaryFound, this.mChildren.get(i2));
            if (t2 != null) {
                return t2;
            }
        }
        return null;
    }

    com.android.server.wm.Task getRootTask(java.util.function.Predicate<com.android.server.wm.Task> callback) {
        return getRootTask(callback, true);
    }

    com.android.server.wm.Task getRootTask(java.util.function.Predicate<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        int count = this.mChildren.size();
        if (traverseTopToBottom) {
            for (int i = count - 1; i >= 0; i--) {
                com.android.server.wm.Task t = this.mChildren.get(i).getRootTask(callback, traverseTopToBottom);
                if (t != null) {
                    return t;
                }
            }
            return null;
        }
        int i2 = 0;
        while (i2 < count) {
            com.android.server.wm.Task t2 = this.mChildren.get(i2).getRootTask(callback, traverseTopToBottom);
            if (t2 != null) {
                return t2;
            }
            int newCount = this.mChildren.size();
            int i3 = i2 - (count - newCount);
            count = newCount;
            i2 = i3 + 1;
        }
        return null;
    }

    private com.android.server.wm.Task processGetTaskWithBoundary(java.util.function.Predicate<com.android.server.wm.Task> callback, com.android.server.wm.WindowContainer boundary, boolean includeBoundary, boolean traverseTopToBottom, boolean[] boundaryFound, com.android.server.wm.WindowContainer wc) {
        if (wc == boundary || boundary == null) {
            boundaryFound[0] = true;
            if (!includeBoundary) {
                return null;
            }
        }
        if (boundaryFound[0]) {
            return wc.getTask(callback, traverseTopToBottom);
        }
        return wc.getTask(callback, boundary, includeBoundary, traverseTopToBottom, boundaryFound);
    }

    com.android.server.wm.TaskFragment getTaskFragment(java.util.function.Predicate<com.android.server.wm.TaskFragment> callback) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.TaskFragment tf = this.mChildren.get(i).getTaskFragment(callback);
            if (tf != null) {
                return tf;
            }
        }
        return null;
    }

    com.android.server.wm.WindowState getWindow(java.util.function.Predicate<com.android.server.wm.WindowState> callback) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState w = this.mChildren.get(i).getWindow(callback);
            if (w != null) {
                return w;
            }
        }
        return null;
    }

    void forAllDisplayAreas(java.util.function.Consumer<com.android.server.wm.DisplayArea> callback) {
        checkPreconditions();
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            this.mChildren.get(i).forAllDisplayAreas(callback);
        }
    }

    boolean forAllTaskDisplayAreas(java.util.function.Predicate<com.android.server.wm.TaskDisplayArea> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        int childCount = this.mChildren.size();
        int i = traverseTopToBottom ? childCount - 1 : 0;
        while (i >= 0 && i < childCount) {
            int i2 = 1;
            if (this.mChildren.get(i).forAllTaskDisplayAreas(callback, traverseTopToBottom)) {
                return true;
            }
            if (traverseTopToBottom) {
                i2 = -1;
            }
            i += i2;
        }
        return false;
    }

    boolean forAllTaskDisplayAreas(java.util.function.Predicate<com.android.server.wm.TaskDisplayArea> callback) {
        return forAllTaskDisplayAreas(callback, true);
    }

    void forAllTaskDisplayAreas(java.util.function.Consumer<com.android.server.wm.TaskDisplayArea> callback, boolean traverseTopToBottom) {
        checkPreconditions();
        int childCount = this.mChildren.size();
        int i = traverseTopToBottom ? childCount - 1 : 0;
        while (i >= 0 && i < childCount) {
            this.mChildren.get(i).forAllTaskDisplayAreas(callback, traverseTopToBottom);
            i += traverseTopToBottom ? -1 : 1;
        }
    }

    void forAllTaskDisplayAreas(java.util.function.Consumer<com.android.server.wm.TaskDisplayArea> callback) {
        forAllTaskDisplayAreas(callback, true);
    }

    <R> R reduceOnAllTaskDisplayAreas(java.util.function.BiFunction<com.android.server.wm.TaskDisplayArea, R, R> biFunction, R r, boolean z) {
        int size = this.mChildren.size();
        int i = z ? size - 1 : 0;
        R r2 = r;
        while (i >= 0 && i < size) {
            r2 = (R) this.mChildren.get(i).reduceOnAllTaskDisplayAreas(biFunction, r2, z);
            i += z ? -1 : 1;
        }
        return r2;
    }

    <R> R reduceOnAllTaskDisplayAreas(java.util.function.BiFunction<com.android.server.wm.TaskDisplayArea, R, R> biFunction, R r) {
        return (R) reduceOnAllTaskDisplayAreas(biFunction, r, true);
    }

    <R> R getItemFromDisplayAreas(java.util.function.Function<com.android.server.wm.DisplayArea, R> function) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            R r = (R) this.mChildren.get(size).getItemFromDisplayAreas(function);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    <R> R getItemFromTaskDisplayAreas(java.util.function.Function<com.android.server.wm.TaskDisplayArea, R> function, boolean z) {
        int size = this.mChildren.size();
        int i = z ? size - 1 : 0;
        while (i >= 0 && i < size) {
            R r = (R) this.mChildren.get(i).getItemFromTaskDisplayAreas(function, z);
            if (r != null) {
                return r;
            }
            i += z ? -1 : 1;
        }
        return null;
    }

    <R> R getItemFromTaskDisplayAreas(java.util.function.Function<com.android.server.wm.TaskDisplayArea, R> function) {
        return (R) getItemFromTaskDisplayAreas(function, true);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(com.android.server.wm.WindowContainer other) {
        if (this == other) {
            return 0;
        }
        if (other == null) {
            return -1;
        }
        if (this.mParent != null && this.mParent == other.mParent) {
            com.android.server.wm.WindowList<com.android.server.wm.WindowContainer> list = this.mParent.mChildren;
            return list.indexOf(this) > list.indexOf(other) ? 1 : -1;
        }
        java.util.LinkedList<com.android.server.wm.WindowContainer> thisParentChain = this.mTmpChain1;
        java.util.LinkedList<com.android.server.wm.WindowContainer> otherParentChain = this.mTmpChain2;
        try {
            getParents(thisParentChain);
            other.getParents(otherParentChain);
            com.android.server.wm.WindowContainer commonAncestor = null;
            com.android.server.wm.WindowContainer thisTop = thisParentChain.peekLast();
            for (com.android.server.wm.WindowContainer otherTop = otherParentChain.peekLast(); thisTop != null && otherTop != null && thisTop == otherTop; otherTop = otherParentChain.peekLast()) {
                commonAncestor = thisParentChain.removeLast();
                otherParentChain.removeLast();
                thisTop = thisParentChain.peekLast();
            }
            if (commonAncestor == null) {
                int thisZ = getPrefixOrderIndex();
                int otherZ = other.getPrefixOrderIndex();
                android.util.Slog.w(TAG, "Compare not in the same hierarchy this=" + thisParentChain + " thisZ=" + thisZ + " other=" + otherParentChain + " otherZ=" + otherZ);
                return java.lang.Integer.compare(thisZ, otherZ);
            }
            if (commonAncestor == this) {
                return -1;
            }
            if (commonAncestor == other) {
                return 1;
            }
            com.android.server.wm.WindowList<com.android.server.wm.WindowContainer> list2 = commonAncestor.mChildren;
            return list2.indexOf(thisParentChain.peekLast()) > list2.indexOf(otherParentChain.peekLast()) ? 1 : -1;
        } finally {
            this.mTmpChain1.clear();
            this.mTmpChain2.clear();
        }
    }

    private void getParents(java.util.LinkedList<com.android.server.wm.WindowContainer> parents) {
        parents.clear();
        com.android.server.wm.WindowContainer<com.android.server.wm.WindowContainer> windowContainer = this;
        do {
            parents.addLast(windowContainer);
            windowContainer = windowContainer.mParent;
        } while (windowContainer != null);
    }

    android.view.SurfaceControl.Builder makeSurface() {
        com.android.server.wm.WindowContainer p = getParent();
        return p.makeChildSurface(this);
    }

    android.view.SurfaceControl.Builder makeChildSurface(com.android.server.wm.WindowContainer child) {
        com.android.server.wm.WindowContainer p = getParent();
        if (this.mWindowContainerExt.isFingerPrintToken(child)) {
            return p.makeChildSurface(child).setParent(null);
        }
        return p.makeChildSurface(child).setParent(this.mSurfaceControl);
    }

    public android.view.SurfaceControl getParentSurfaceControl() {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent == null) {
            return null;
        }
        return parent.getSurfaceControl();
    }

    boolean shouldMagnify() {
        if (this.mSurfaceControl == null) {
            return false;
        }
        for (int i = 0; i < this.mChildren.size(); i++) {
            if (!this.mChildren.get(i).shouldMagnify()) {
                return false;
            }
        }
        return true;
    }

    android.view.SurfaceSession getSession() {
        if (getParent() != null) {
            return getParent().getSession();
        }
        return null;
    }

    void assignLayer(android.view.SurfaceControl.Transaction t, int layer) {
        if (this.mTransitionController.canAssignLayers(this)) {
            boolean changed = (layer == this.mLastLayer && this.mLastRelativeToLayer == null) ? false : true;
            if (this.mSurfaceControl != null) {
                if (changed || this.mWindowContainerExt.assignLayerForTransition()) {
                    setLayer(t, layer);
                    this.mLastLayer = layer;
                    this.mLastRelativeToLayer = null;
                }
            }
        }
    }

    void assignRelativeLayer(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl relativeTo, int layer, boolean forceUpdate) {
        boolean changed = (layer == this.mLastLayer && this.mLastRelativeToLayer == relativeTo) ? false : true;
        if (this.mSurfaceControl != null) {
            if (changed || forceUpdate) {
                setRelativeLayer(t, relativeTo, layer);
                this.mLastLayer = layer;
                this.mLastRelativeToLayer = relativeTo;
            }
        }
    }

    void assignRelativeLayer(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl relativeTo, int layer) {
        assignRelativeLayer(t, relativeTo, layer, false);
    }

    protected void setLayer(android.view.SurfaceControl.Transaction t, int layer) {
        if (this.mSurfaceFreezer.hasLeash()) {
            this.mSurfaceFreezer.setLayer(t, layer);
        } else {
            this.mSurfaceAnimator.setLayer(t, layer);
        }
    }

    int getLastLayer() {
        return this.mLastLayer;
    }

    android.view.SurfaceControl getLastRelativeLayer() {
        return this.mLastRelativeToLayer;
    }

    protected void setRelativeLayer(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl relativeTo, int layer) {
        if (this.mSurfaceFreezer.hasLeash()) {
            this.mSurfaceFreezer.setRelativeLayer(t, relativeTo, layer);
        } else {
            this.mSurfaceAnimator.setRelativeLayer(t, relativeTo, layer);
        }
    }

    protected void reparentSurfaceControl(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl newParent) {
        if (this.mSurfaceFreezer.hasLeash() || this.mSurfaceAnimator.hasLeash()) {
            return;
        }
        t.reparent(getSurfaceControl(), newParent);
    }

    void assignChildLayers(android.view.SurfaceControl.Transaction t) {
        int layer = 0;
        for (int j = 0; j < this.mChildren.size(); j++) {
            com.android.server.wm.WindowContainer wc = this.mChildren.get(j);
            wc.assignChildLayers(t);
            if (!wc.needsZBoost()) {
                wc.assignLayer(t, layer);
                layer++;
            }
        }
        for (int j2 = 0; j2 < this.mChildren.size(); j2++) {
            com.android.server.wm.WindowContainer wc2 = this.mChildren.get(j2);
            if (wc2.needsZBoost()) {
                wc2.assignLayer(t, layer);
                layer++;
            }
        }
        if (this.mOverlayHost != null) {
            int i = layer + 1;
            this.mOverlayHost.setLayer(t, layer);
        }
    }

    void assignChildLayers() {
        assignChildLayers(getSyncTransaction());
        scheduleAnimation();
    }

    boolean needsZBoost() {
        if (this.mNeedsZBoost) {
            return true;
        }
        for (int i = 0; i < this.mChildren.size(); i++) {
            if (this.mChildren.get(i).needsZBoost()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        boolean isVisible = isVisible();
        if (logLevel == 2 && !isVisible) {
            return;
        }
        long token = proto.start(fieldId);
        super.dumpDebug(proto, 1146756268033L, logLevel);
        proto.write(1120986464258L, this.mOverrideOrientation);
        proto.write(1133871366147L, isVisible);
        writeIdentifierToProto(proto, 1146756268038L);
        if (this.mSurfaceAnimator.isAnimating()) {
            this.mSurfaceAnimator.dumpDebug(proto, 1146756268036L);
        }
        if (this.mSurfaceControl != null) {
            this.mSurfaceControl.dumpDebug(proto, 1146756268039L);
        }
        for (int i = 0; i < getChildCount(); i++) {
            long childToken = proto.start(2246267895813L);
            com.android.server.wm.WindowContainer childAt = getChildAt(i);
            childAt.dumpDebug(proto, childAt.getProtoFieldId(), logLevel);
            proto.end(childToken);
        }
        proto.end(token);
    }

    long getProtoFieldId() {
        return 1146756268034L;
    }

    private com.android.server.wm.WindowContainer<E>.ForAllWindowsConsumerWrapper obtainConsumerWrapper(java.util.function.Consumer<com.android.server.wm.WindowState> consumer) {
        com.android.server.wm.WindowContainer<E>.ForAllWindowsConsumerWrapper wrapper = (com.android.server.wm.WindowContainer.ForAllWindowsConsumerWrapper) this.mConsumerWrapperPool.acquire();
        if (wrapper == null) {
            wrapper = new com.android.server.wm.WindowContainer.ForAllWindowsConsumerWrapper();
        }
        wrapper.setConsumer(consumer);
        return wrapper;
    }

    private final class ForAllWindowsConsumerWrapper implements com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> {
        private java.util.function.Consumer<com.android.server.wm.WindowState> mConsumer;

        private ForAllWindowsConsumerWrapper() {
        }

        void setConsumer(java.util.function.Consumer<com.android.server.wm.WindowState> consumer) {
            this.mConsumer = consumer;
        }

        public boolean apply(com.android.server.wm.WindowState w) {
            this.mConsumer.accept(w);
            return false;
        }

        void release() {
            this.mConsumer = null;
            com.android.server.wm.WindowContainer.this.mConsumerWrapperPool.release(this);
        }
    }

    void applyMagnificationSpec(android.view.SurfaceControl.Transaction t, android.view.MagnificationSpec spec) {
        if (shouldMagnify()) {
            t.setMatrix(this.mSurfaceControl, spec.scale, 0.0f, 0.0f, spec.scale).setPosition(this.mSurfaceControl, spec.offsetX + this.mLastSurfacePosition.x, spec.offsetY + this.mLastSurfacePosition.y);
            this.mLastMagnificationSpec = spec;
            return;
        }
        clearMagnificationSpec(t);
        for (int i = 0; i < this.mChildren.size(); i++) {
            this.mChildren.get(i).applyMagnificationSpec(t, spec);
        }
    }

    void clearMagnificationSpec(android.view.SurfaceControl.Transaction t) {
        if (this.mLastMagnificationSpec != null) {
            t.setMatrix(this.mSurfaceControl, 1.0f, 0.0f, 0.0f, 1.0f).setPosition(this.mSurfaceControl, this.mLastSurfacePosition.x, this.mLastSurfacePosition.y);
        }
        this.mLastMagnificationSpec = null;
        for (int i = 0; i < this.mChildren.size(); i++) {
            this.mChildren.get(i).clearMagnificationSpec(t);
        }
    }

    void prepareSurfaces() {
        this.mCommittedReparentToAnimationLeash = this.mSurfaceAnimator.hasLeash();
        for (int i = 0; i < this.mChildren.size(); i++) {
            this.mChildren.get(i).prepareSurfaces();
        }
        this.mWindowContainerExt.hookPrepareSurfacesEnd();
    }

    boolean hasCommittedReparentToAnimationLeash() {
        return this.mCommittedReparentToAnimationLeash;
    }

    void scheduleAnimation() {
        this.mWmService.scheduleAnimationLocked();
    }

    public android.view.SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }

    public android.view.SurfaceControl.Transaction getSyncTransaction() {
        if (this.mSyncTransactionCommitCallbackDepth > 0) {
            return this.mSyncTransaction;
        }
        if (this.mSyncState != 0) {
            return this.mSyncTransaction;
        }
        return getPendingTransaction();
    }

    public android.view.SurfaceControl.Transaction getPendingTransaction() {
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        if (displayContent != null && displayContent != this) {
            return displayContent.getPendingTransaction();
        }
        return this.mPendingTransaction;
    }

    void startAnimation(android.view.SurfaceControl.Transaction t, com.android.server.wm.AnimationAdapter anim, boolean hidden, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback animationFinishedCallback, java.lang.Runnable animationCancelledCallback, com.android.server.wm.AnimationAdapter snapshotAnim) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            long protoLogParam1 = type;
            java.lang.String protoLogParam2 = java.lang.String.valueOf(anim);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, 6949303417875346627L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), protoLogParam2);
        }
        this.mSurfaceAnimator.startAnimation(t, anim, hidden, type, animationFinishedCallback, animationCancelledCallback, snapshotAnim, this.mSurfaceFreezer);
    }

    void startAnimation(android.view.SurfaceControl.Transaction t, com.android.server.wm.AnimationAdapter anim, boolean hidden, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback animationFinishedCallback) {
        startAnimation(t, anim, hidden, type, animationFinishedCallback, null, null);
    }

    void startAnimation(android.view.SurfaceControl.Transaction t, com.android.server.wm.AnimationAdapter anim, boolean hidden, int type) {
        startAnimation(t, anim, hidden, type, null);
    }

    void transferAnimation(com.android.server.wm.WindowContainer from) {
        this.mSurfaceAnimator.transferAnimation(from.mSurfaceAnimator);
    }

    void cancelAnimation() {
        doAnimationFinished(this.mSurfaceAnimator.getAnimationType(), this.mSurfaceAnimator.getAnimation());
        this.mSurfaceAnimator.cancelAnimation();
        this.mSurfaceFreezer.unfreeze(getSyncTransaction());
    }

    boolean canStartChangeTransition() {
        if (this.mWmService.mDisableTransitionAnimation || !okToAnimate() || this.mDisplayContent == null || getSurfaceControl() == null || !isVisible() || !isVisibleRequested() || this.mDisplayContent.inTransition() || this.mWindowContainerExt.isZoomMode(getWindowingMode())) {
            return false;
        }
        if (com.android.server.wm.ActivityTaskManagerService.isPip2ExperimentEnabled()) {
            return true;
        }
        return (inPinnedWindowingMode() || getParent() == null || getParent().inPinnedWindowingMode()) ? false : true;
    }

    void initializeChangeTransition(android.graphics.Rect startBounds, android.view.SurfaceControl freezeTarget) {
        if (this.mDisplayContent.mTransitionController.isShellTransitionsEnabled()) {
            this.mDisplayContent.mTransitionController.collectVisibleChange(this);
            return;
        }
        this.mDisplayContent.prepareAppTransition(6);
        this.mDisplayContent.mChangingContainers.add(this);
        android.graphics.Rect parentBounds = getParent().getBounds();
        this.mTmpPoint.set(startBounds.left - parentBounds.left, startBounds.top - parentBounds.top);
        this.mSurfaceFreezer.freeze(getSyncTransaction(), startBounds, this.mTmpPoint, freezeTarget);
    }

    void initializeChangeTransition(android.graphics.Rect startBounds) {
        initializeChangeTransition(startBounds, null);
    }

    android.util.ArraySet<com.android.server.wm.WindowContainer> getAnimationSources() {
        return this.mSurfaceAnimationSources;
    }

    public android.view.SurfaceControl getFreezeSnapshotTarget() {
        if (!this.mDisplayContent.mAppTransition.containsTransitRequest(6) || !this.mDisplayContent.mChangingContainers.contains(this)) {
            return null;
        }
        return getSurfaceControl();
    }

    public void onUnfrozen() {
        if (this.mDisplayContent != null) {
            this.mDisplayContent.mChangingContainers.remove(this);
        }
    }

    public android.view.SurfaceControl.Builder makeAnimationLeash() {
        return makeSurface().setContainerLayer();
    }

    public android.view.SurfaceControl getAnimationLeashParent() {
        return getParentSurfaceControl();
    }

    android.graphics.Rect getAnimationBounds(int appRootTaskClipMode) {
        return getBounds();
    }

    void getAnimationPosition(android.graphics.Point outPosition) {
        getRelativePosition(outPosition);
    }

    boolean applyAnimation(android.view.WindowManager.LayoutParams lp, int transit, boolean enter, boolean isVoiceInteraction, java.util.ArrayList<com.android.server.wm.WindowContainer> sources) {
        if (this.mWmService.mDisableTransitionAnimation) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -8730310387200541562L, 0, null, protoLogParam0);
            }
            cancelAnimation();
            return false;
        }
        try {
            android.os.Trace.traceBegin(32L, "WC#applyAnimation");
            if (okToAnimate()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(com.android.server.wm.AppTransition.appTransitionOldToString(transit));
                    java.lang.String protoLogParam2 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 2363818604357955690L, 12, null, protoLogParam02, java.lang.Boolean.valueOf(enter), protoLogParam2);
                }
                applyAnimationUnchecked(lp, enter, transit, isVoiceInteraction, sources);
            } else {
                cancelAnimation();
            }
            android.os.Trace.traceEnd(32L);
            return isAnimating();
        } catch (java.lang.Throwable th) {
            android.os.Trace.traceEnd(32L);
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00ad  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    android.util.Pair<com.android.server.wm.AnimationAdapter, com.android.server.wm.AnimationAdapter> getAnimationAdapter(android.view.WindowManager.LayoutParams r26, int r27, boolean r28, boolean r29) {
        /*
            Method dump skipped, instruction units count: 662
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowContainer.getAnimationAdapter(android.view.WindowManager$LayoutParams, int, boolean, boolean):android.util.Pair");
    }

    protected void applyAnimationUnchecked(android.view.WindowManager.LayoutParams lp, boolean enter, int transit, boolean isVoiceInteraction, java.util.ArrayList<com.android.server.wm.WindowContainer> sources) {
        com.android.server.wm.TaskFragment organizedTf;
        com.android.server.wm.Task parentTask;
        int backgroundColorForTransition;
        com.android.server.wm.Task task = asTask();
        if (task != null && !enter && !task.isActivityTypeHomeOrRecents()) {
            boolean isImeLayeringTarget = false;
            com.android.server.wm.InsetsControlTarget imeTarget = this.mDisplayContent.getImeTarget(0);
            if (imeTarget != null && imeTarget.getWindow() != null && imeTarget.getWindow().getTask() == task) {
                isImeLayeringTarget = true;
            }
            if (isImeLayeringTarget && com.android.server.wm.AppTransition.isTaskCloseTransitOld(transit)) {
                this.mDisplayContent.showImeScreenshot();
            }
        }
        if (task != null && task.getDisplayContent() != null && task.getDisplayContent().getWrapper().getNonStaticExtImpl().isPuttDisplay()) {
            android.util.Slog.d(TAG, "applyAnimationUnchecked ignore as putt task " + task);
            return;
        }
        if (asActivityRecord() == null || asActivityRecord().getDisplayContent() == null || !asActivityRecord().getDisplayContent().getWrapper().getNonStaticExtImpl().isPuttDisplay()) {
            android.util.Pair<com.android.server.wm.AnimationAdapter, com.android.server.wm.AnimationAdapter> adapters = getAnimationAdapter(lp, transit, enter, isVoiceInteraction);
            com.android.server.wm.AnimationAdapter adapter = (com.android.server.wm.AnimationAdapter) adapters.first;
            com.android.server.wm.AnimationAdapter thumbnailAdapter = (com.android.server.wm.AnimationAdapter) adapters.second;
            if (adapter != null) {
                if (sources != null) {
                    this.mSurfaceAnimationSources.addAll(sources);
                }
                com.android.server.wm.WindowContainer<E>.AnimationRunnerBuilder animationRunnerBuilder = new com.android.server.wm.WindowContainer.AnimationRunnerBuilder();
                com.android.server.wm.ActivityRecord activityRecord = asActivityRecord();
                com.android.server.wm.TaskFragment taskFragment = asTaskFragment();
                if (adapter.getShowBackground() && ((activityRecord != null && com.android.server.wm.AppTransition.isActivityTransitOld(transit)) || (taskFragment != null && taskFragment.isEmbedded() && com.android.server.wm.AppTransition.isTaskFragmentTransitOld(transit)))) {
                    if (adapter.getBackgroundColor() != 0) {
                        backgroundColorForTransition = adapter.getBackgroundColor();
                    } else {
                        if (activityRecord != null) {
                            organizedTf = activityRecord.getOrganizedTaskFragment();
                        } else {
                            organizedTf = taskFragment.getOrganizedTaskFragment();
                        }
                        if (organizedTf != null && organizedTf.getAnimationParams().getAnimationBackgroundColor() != 0) {
                            backgroundColorForTransition = organizedTf.getAnimationParams().getAnimationBackgroundColor();
                        } else {
                            if (activityRecord != null) {
                                parentTask = activityRecord.getTask();
                            } else {
                                parentTask = taskFragment.getTask();
                            }
                            backgroundColorForTransition = parentTask.getTaskDescription().getBackgroundColor();
                        }
                    }
                    animationRunnerBuilder.setTaskBackgroundColor(com.android.internal.graphics.ColorUtils.setAlphaComponent(backgroundColorForTransition, 255));
                }
                animationRunnerBuilder.build().startAnimation(getPendingTransaction(), adapter, !isVisible(), 1, thumbnailAdapter);
                if (adapter.getShowWallpaper()) {
                    getDisplayContent().pendingLayoutChanges |= 4;
                    return;
                }
                return;
            }
            return;
        }
        android.util.Slog.d(TAG, "applyAnimationUnchecked ignore as putt ar " + asActivityRecord());
    }

    final com.android.server.wm.SurfaceAnimationRunner getSurfaceAnimationRunner() {
        return this.mWmService.mSurfaceAnimationRunner;
    }

    private android.view.animation.Animation loadAnimation(android.view.WindowManager.LayoutParams lp, int transit, boolean enter, boolean isVoiceInteraction) {
        boolean enter2;
        if ((com.android.server.wm.AppTransitionController.isTaskViewTask(this) || (isOrganized() && getWindowingMode() != 1 && getWindowingMode() != 5 && getWindowingMode() != 6)) && this.mWindowContainerExt.skipLoadAnimation()) {
            return null;
        }
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        android.view.DisplayInfo displayInfo = displayContent.getDisplayInfo();
        int width = this.mWindowContainerExt.getAdjustDisplayInfo(displayInfo)[0];
        int height = this.mWindowContainerExt.getAdjustDisplayInfo(displayInfo)[1];
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 2262119454684034794L, 0, null, protoLogParam0);
        }
        android.graphics.Rect frame = new android.graphics.Rect(0, 0, width, height);
        android.graphics.Rect displayFrame = new android.graphics.Rect(0, 0, displayInfo.logicalWidth, displayInfo.logicalHeight);
        android.graphics.Rect insets = new android.graphics.Rect();
        android.graphics.Rect stableInsets = new android.graphics.Rect();
        android.graphics.Rect surfaceInsets = new android.graphics.Rect();
        getAnimationFrames(frame, insets, stableInsets, surfaceInsets);
        this.mWindowContainerExt.adjustAnimationFrameForExpandedWindow(this, frame, transit, enter);
        if (!this.mLaunchTaskBehind) {
            enter2 = enter;
        } else {
            enter2 = false;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[0]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(com.android.server.wm.AppTransition.appTransitionOldToString(transit));
            boolean protoLogParam1 = enter2;
            java.lang.String protoLogParam2 = java.lang.String.valueOf(frame);
            java.lang.String protoLogParam3 = java.lang.String.valueOf(insets);
            java.lang.String protoLogParam4 = java.lang.String.valueOf(surfaceInsets);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 5857165752965610762L, 12, null, protoLogParam02, java.lang.Boolean.valueOf(protoLogParam1), protoLogParam2, protoLogParam3, protoLogParam4);
        }
        android.content.res.Configuration displayConfig = displayContent.getConfiguration();
        android.view.animation.Animation a = getDisplayContent().mAppTransition.loadAnimation(lp, transit, enter2, displayConfig.uiMode, displayConfig.orientation, frame, displayFrame, insets, surfaceInsets, stableInsets, isVoiceInteraction, inFreeformWindowingMode(), this);
        if (a == null && isActivityTypeHome() && !enter2 && this.mDisplayContent.mSkipAppTransitionAnimation) {
            a = this.mWindowContainerExt.createAnimationForLauncherExit();
        }
        if (a != null) {
            if (a != null) {
                a.restrictDuration(3000L);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.DEBUG) && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[2]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(a);
                java.lang.String protoLogParam12 = java.lang.String.valueOf(this);
                long protoLogParam22 = a != null ? a.getDuration() : 0L;
                java.lang.String protoLogParam32 = java.lang.String.valueOf(android.os.Debug.getCallers(20));
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, 9017113545720281233L, 16, null, protoLogParam03, protoLogParam12, java.lang.Long.valueOf(protoLogParam22), protoLogParam32);
            }
            int containingWidth = frame.width();
            int containingHeight = frame.height();
            a.initialize(containingWidth, containingHeight, width, height);
            a.scaleCurrentDuration(this.mWmService.getTransitionAnimationScaleLocked());
            this.mWindowContainerExt.adjustAnimationForMultiTask(this, a, frame);
            boolean z = enter2;
            this.mWindowContainerExt.addAnimationUpdateRecorder(a, transit, z, containingWidth, containingHeight, width, height);
            this.mWindowContainerExt.addRoundedCornersToAnimationIfNeed(lp, transit, z, isVoiceInteraction, a);
        }
        return a;
    }

    android.view.RemoteAnimationTarget createRemoteAnimationTarget(com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord record) {
        return null;
    }

    boolean canCreateRemoteAnimationTarget() {
        return false;
    }

    boolean okToDisplay() {
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        return dc != null && dc.okToDisplay();
    }

    boolean okToAnimate() {
        return okToAnimate(false, false);
    }

    boolean okToAnimate(boolean ignoreFrozen, boolean ignoreScreenOn) {
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        return dc != null && dc.okToAnimate(ignoreFrozen, ignoreScreenOn);
    }

    public void commitPendingTransaction() {
        scheduleAnimation();
    }

    void transformFrameToSurfacePosition(int left, int top, android.graphics.Point outPoint) {
        outPoint.set(left, top);
        com.android.server.wm.WindowContainer parentWindowContainer = getParent();
        if (parentWindowContainer == null) {
            return;
        }
        android.graphics.Rect parentBounds = parentWindowContainer.getBounds();
        outPoint.offset(-parentBounds.left, -parentBounds.top);
    }

    void reassignLayer(android.view.SurfaceControl.Transaction t) {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null) {
            parent.assignChildLayers(t);
        }
    }

    void resetSurfacePositionForAnimationLeash(android.view.SurfaceControl.Transaction t) {
        t.setPosition(this.mSurfaceControl, 0.0f, 0.0f);
        android.view.SurfaceControl.Transaction syncTransaction = getSyncTransaction();
        if (t != syncTransaction) {
            syncTransaction.setPosition(this.mSurfaceControl, 0.0f, 0.0f);
        }
        this.mLastSurfacePosition.set(0, 0);
    }

    public void onAnimationLeashCreated(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
        this.mLastLayer = -1;
        this.mAnimationLeash = leash;
        reassignLayer(t);
        this.mWindowContainerExt.onAnimationLeashCreated(this, t);
        resetSurfacePositionForAnimationLeash(t);
    }

    public void onAnimationLeashLost(android.view.SurfaceControl.Transaction t) {
        this.mLastLayer = -1;
        this.mWmService.mSurfaceAnimationRunner.onAnimationLeashLost(this.mAnimationLeash, t);
        this.mAnimationLeash = null;
        this.mNeedsZBoost = false;
        reassignLayer(t);
        updateSurfacePosition(t);
        this.mWindowContainerExt.onAnimationLeashLost(this, t);
    }

    public android.view.SurfaceControl getAnimationLeash() {
        return this.mAnimationLeash;
    }

    private void doAnimationFinished(int type, com.android.server.wm.AnimationAdapter anim) {
        for (int i = 0; i < this.mSurfaceAnimationSources.size(); i++) {
            this.mSurfaceAnimationSources.valueAt(i).onAnimationFinished(type, anim);
        }
        this.mSurfaceAnimationSources.clear();
        if (this.mDisplayContent != null) {
            this.mDisplayContent.onWindowAnimationFinished(this, type);
        }
    }

    protected void onAnimationFinished(int type, com.android.server.wm.AnimationAdapter anim) {
        doAnimationFinished(type, anim);
        this.mWmService.onAnimationFinished();
        this.mNeedsZBoost = false;
        this.mWindowContainerExt.onAnimationFinished(this, this.mWmService.mAnimationHandler);
    }

    com.android.server.wm.AnimationAdapter getAnimation() {
        return this.mSurfaceAnimator.getAnimation();
    }

    com.android.server.wm.WindowContainer getAnimatingContainer(int flags, int typesToCheck) {
        if (isSelfAnimating(flags, typesToCheck)) {
            return this;
        }
        if ((flags & 2) != 0) {
            for (com.android.server.wm.WindowContainer parent = getParent(); parent != null; parent = parent.getParent()) {
                if (parent.isSelfAnimating(flags, typesToCheck)) {
                    return parent;
                }
            }
        }
        if ((flags & 4) != 0) {
            for (int i = 0; i < this.mChildren.size(); i++) {
                com.android.server.wm.WindowContainer wc = this.mChildren.get(i).getAnimatingContainer(flags & (-3), typesToCheck);
                if (wc != null) {
                    return wc;
                }
            }
            return null;
        }
        return null;
    }

    protected boolean isSelfAnimating(int flags, int typesToCheck) {
        if (!this.mSurfaceAnimator.isAnimating() || (this.mSurfaceAnimator.getAnimationType() & typesToCheck) <= 0) {
            return (flags & 1) != 0 && isWaitingForTransitionStart();
        }
        return true;
    }

    @java.lang.Deprecated
    final com.android.server.wm.WindowContainer getAnimatingContainer() {
        return getAnimatingContainer(2, -1);
    }

    void startDelayingAnimationStart() {
        this.mSurfaceAnimator.startDelayingAnimationStart();
    }

    void endDelayingAnimationStart() {
        this.mSurfaceAnimator.endDelayingAnimationStart();
    }

    public int getSurfaceWidth() {
        return this.mSurfaceControl.getWidth();
    }

    public int getSurfaceHeight() {
        return this.mSurfaceControl.getHeight();
    }

    static void enforceSurfaceVisible(com.android.server.wm.WindowContainer<?> wc) {
        if (wc.mSurfaceControl == null) {
            return;
        }
        wc.getSyncTransaction().show(wc.mSurfaceControl);
        com.android.server.wm.ActivityRecord ar = wc.asActivityRecord();
        if (ar != null) {
            ar.mLastSurfaceShowing = true;
        }
        for (com.android.server.wm.WindowContainer<?> p = wc.getParent(); p != null && p != wc.mDisplayContent; p = p.getParent()) {
            if (p.mSurfaceControl != null) {
                p.getSyncTransaction().show(p.mSurfaceControl);
                com.android.server.wm.Task task = p.asTask();
                if (task != null) {
                    task.mLastSurfaceShowing = true;
                }
            }
        }
        wc.scheduleAnimation();
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        if (this.mSurfaceAnimator.isAnimating()) {
            pw.print(prefix);
            pw.println("ContainerAnimator:");
            this.mSurfaceAnimator.dump(pw, prefix + "  ");
        }
        if (this.mLastOrientationSource != null && this == this.mDisplayContent) {
            pw.println(prefix + "mLastOrientationSource=" + this.mLastOrientationSource);
            pw.println(prefix + "deepestLastOrientationSource=" + getLastOrientationSource());
        }
        if (this.mLocalInsetsSources != null && this.mLocalInsetsSources.size() != 0) {
            pw.println(prefix + this.mLocalInsetsSources.size() + " LocalInsetsSources");
            java.lang.String childPrefix = prefix + "  ";
            for (int i = 0; i < this.mLocalInsetsSources.size(); i++) {
                this.mLocalInsetsSources.valueAt(i).dump(childPrefix, pw);
            }
        }
        this.mWindowContainerExt.dump(pw, prefix, this);
    }

    final void updateSurfacePositionNonOrganized() {
        if (isOrganized()) {
            return;
        }
        updateSurfacePosition(getSyncTransaction());
    }

    void updateSurfacePosition(android.view.SurfaceControl.Transaction t) {
        if (this.mSurfaceControl == null || this.mSurfaceAnimator.hasLeash() || this.mSurfaceFreezer.hasLeash()) {
            return;
        }
        if (isClosingWhenResizing()) {
            getRelativePosition(this.mDisplayContent.mClosingChangingContainers.get(this), this.mTmpPos);
        } else {
            getRelativePosition(this.mTmpPos);
        }
        if (this.mWindowContainerExt.hookupdateSurfacePosition(getWindowingMode(), asTask(), this)) {
            return;
        }
        int deltaRotation = getRelativeDisplayRotation();
        if (this.mWindowContainerExt.blockUpdateSurfacePosition(this)) {
            return;
        }
        if (this.mTmpPos.equals(this.mLastSurfacePosition) && deltaRotation == this.mLastDeltaRotation) {
            return;
        }
        t.setPosition(this.mSurfaceControl, this.mTmpPos.x, this.mTmpPos.y);
        this.mLastSurfacePosition.set(this.mTmpPos.x, this.mTmpPos.y);
        if (this.mTransitionController.isShellTransitionsEnabled() && !this.mTransitionController.useShellTransitionsRotation()) {
            if (deltaRotation != 0) {
                updateSurfaceRotation(t, deltaRotation, null);
                getPendingTransaction().setFixedTransformHint(this.mSurfaceControl, getWindowConfiguration().getDisplayRotation());
            } else if (deltaRotation != this.mLastDeltaRotation) {
                t.setMatrix(this.mSurfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
                getPendingTransaction().unsetFixedTransformHint(this.mSurfaceControl);
                this.mWindowContainerExt.enablePendingApplyTransition(this, t);
            }
        }
        this.mLastDeltaRotation = deltaRotation;
    }

    protected void updateSurfaceRotation(android.view.SurfaceControl.Transaction t, int deltaRotation, android.view.SurfaceControl positionLeash) {
        android.util.RotationUtils.rotateSurface(t, this.mSurfaceControl, deltaRotation);
        this.mTmpPos.set(this.mLastSurfacePosition.x, this.mLastSurfacePosition.y);
        android.graphics.Rect parentBounds = getParent().getBounds();
        boolean flipped = deltaRotation % 2 != 0;
        android.util.RotationUtils.rotatePoint(this.mTmpPos, deltaRotation, flipped ? parentBounds.height() : parentBounds.width(), flipped ? parentBounds.width() : parentBounds.height());
        t.setPosition(positionLeash != null ? positionLeash : this.mSurfaceControl, this.mTmpPos.x, this.mTmpPos.y);
    }

    android.graphics.Point getLastSurfacePosition() {
        return this.mLastSurfacePosition;
    }

    void getAnimationFrames(android.graphics.Rect outFrame, android.graphics.Rect outInsets, android.graphics.Rect outStableInsets, android.graphics.Rect outSurfaceInsets) {
        android.view.DisplayInfo displayInfo = getDisplayContent().getDisplayInfo();
        outFrame.set(0, 0, displayInfo.appWidth, displayInfo.appHeight);
        outInsets.setEmpty();
        outStableInsets.setEmpty();
        outSurfaceInsets.setEmpty();
    }

    void getRelativePosition(android.graphics.Point outPos) {
        getRelativePosition(getBounds(), outPos);
    }

    void getRelativePosition(android.graphics.Rect curBounds, android.graphics.Point outPos) {
        outPos.set(curBounds.left, curBounds.top);
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null) {
            android.graphics.Rect parentBounds = parent.getBounds();
            outPos.offset(-parentBounds.left, -parentBounds.top);
        }
    }

    int getRelativeDisplayRotation() {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent == null) {
            return 0;
        }
        int rotation = getWindowConfiguration().getDisplayRotation();
        int parentRotation = parent.getWindowConfiguration().getDisplayRotation();
        return android.util.RotationUtils.deltaRotation(rotation, parentRotation);
    }

    void waitForAllWindowsDrawn() {
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowContainer$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$waitForAllWindowsDrawn$4((com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$waitForAllWindowsDrawn$4(com.android.server.wm.WindowState w) {
        w.requestDrawIfNeeded(this.mWaitingForDrawn);
    }

    com.android.server.wm.Dimmer getDimmer() {
        if (this.mParent == null) {
            return null;
        }
        return this.mParent.getDimmer();
    }

    void setSurfaceControl(android.view.SurfaceControl sc) {
        this.mSurfaceControl = sc;
    }

    android.view.RemoteAnimationDefinition getRemoteAnimationDefinition() {
        return null;
    }

    com.android.server.wm.Task asTask() {
        return null;
    }

    com.android.server.wm.TaskFragment asTaskFragment() {
        return null;
    }

    com.android.server.wm.WindowToken asWindowToken() {
        return null;
    }

    com.android.server.wm.WindowState asWindowState() {
        return null;
    }

    com.android.server.wm.ActivityRecord asActivityRecord() {
        return null;
    }

    com.android.server.wm.WallpaperWindowToken asWallpaperToken() {
        return null;
    }

    com.android.server.wm.DisplayArea asDisplayArea() {
        return null;
    }

    com.android.server.wm.RootDisplayArea asRootDisplayArea() {
        return null;
    }

    com.android.server.wm.TaskDisplayArea asTaskDisplayArea() {
        return null;
    }

    com.android.server.wm.DisplayContent asDisplayContent() {
        return null;
    }

    boolean isOrganized() {
        return false;
    }

    boolean isEmbedded() {
        return false;
    }

    boolean showSurfaceOnCreation() {
        return true;
    }

    boolean showWallpaper() {
        if (!isVisibleRequested() || inMultiWindowMode()) {
            return false;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = this.mChildren.get(i);
            if (child.showWallpaper()) {
                return true;
            }
        }
        return false;
    }

    boolean hasWallpaper() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = this.mChildren.get(i);
            if (child.hasWallpaper()) {
                return true;
            }
        }
        return false;
    }

    static com.android.server.wm.WindowContainer fromBinder(android.os.IBinder binder) {
        return com.android.server.wm.WindowContainer.RemoteToken.fromBinder(binder).getContainer();
    }

    static class RemoteToken extends android.window.IWindowContainerToken.Stub {
        final java.lang.ref.WeakReference<com.android.server.wm.WindowContainer> mWeakRef;
        private android.window.WindowContainerToken mWindowContainerToken;

        RemoteToken(com.android.server.wm.WindowContainer container) {
            this.mWeakRef = new java.lang.ref.WeakReference<>(container);
        }

        com.android.server.wm.WindowContainer getContainer() {
            return this.mWeakRef.get();
        }

        /* JADX WARN: Multi-variable type inference failed */
        static com.android.server.wm.WindowContainer.RemoteToken fromBinder(android.os.IBinder iBinder) {
            return (com.android.server.wm.WindowContainer.RemoteToken) iBinder;
        }

        android.window.WindowContainerToken toWindowContainerToken() {
            if (this.mWindowContainerToken == null) {
                this.mWindowContainerToken = new android.window.WindowContainerToken(this);
            }
            return this.mWindowContainerToken;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("RemoteToken{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(' ');
            sb.append(this.mWeakRef.get());
            sb.append('}');
            return sb.toString();
        }
    }

    boolean onSyncFinishedDrawing() {
        if (this.mSyncState == 0) {
            return false;
        }
        this.mSyncState = 2;
        this.mSyncMethodOverride = -1;
        this.mWindowContainerExt.onSyncFinishedDrawing(this);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 5272307326252759722L, 0, null, protoLogParam0);
        }
        return true;
    }

    void setSyncGroup(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
            long protoLogParam0 = group.mSyncId;
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -8311909671193661340L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
        }
        if (this.mSyncGroup != null && this.mSyncGroup != group) {
            throw new java.lang.IllegalStateException("Can't sync on 2 groups simultaneously currentSyncId=" + this.mSyncGroup.mSyncId + " newSyncId=" + group.mSyncId + " wc=" + this);
        }
        this.mSyncGroup = group;
    }

    com.android.server.wm.BLASTSyncEngine.SyncGroup getSyncGroup() {
        if (this.mSyncGroup != null) {
            return this.mSyncGroup;
        }
        for (com.android.server.wm.WindowContainer<?> parent = this.mParent; parent != null; parent = parent.mParent) {
            if (parent.mSyncGroup != null) {
                return parent.mSyncGroup;
            }
        }
        return null;
    }

    boolean prepareSync() {
        if (this.mSyncState != 0) {
            return false;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = getChildAt(i);
            child.prepareSync();
        }
        this.mSyncState = 2;
        return true;
    }

    boolean syncNextBuffer() {
        return this.mSyncState != 0;
    }

    void finishSync(android.view.SurfaceControl.Transaction outMergedTransaction, com.android.server.wm.BLASTSyncEngine.SyncGroup group, boolean cancel) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
            android.util.Slog.d(TAG, "finishSync wc=" + this + ", mSyncState=" + this.mSyncState + ", group=" + group + ", wc.group=" + getSyncGroup() + ", call by=" + android.os.Debug.getCallers(5));
        }
        if (this.mSyncState == 0 && !this.mWindowContainerExt.forceFinishSync(this, group)) {
            return;
        }
        com.android.server.wm.BLASTSyncEngine.SyncGroup syncGroup = getSyncGroup();
        if (syncGroup == null || group == syncGroup) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
                java.lang.String protoLogParam1 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -3871009616397322067L, 3, null, java.lang.Boolean.valueOf(cancel), protoLogParam1);
            }
            outMergedTransaction.merge(this.mSyncTransaction);
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                this.mChildren.get(i).finishSync(outMergedTransaction, group, cancel);
            }
            if (cancel && this.mSyncGroup != null) {
                this.mSyncGroup.onCancelSync(this);
            }
            this.mSyncState = 0;
            this.mSyncMethodOverride = -1;
            this.mSyncGroup = null;
        }
    }

    boolean isSyncFinished(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        if (!isVisibleRequested()) {
            return true;
        }
        if (this.mSyncState == 0 && getSyncGroup() != null) {
            android.util.Slog.i(TAG, "prepareSync in isSyncFinished: " + this);
            prepareSync();
        }
        if (this.mWindowContainerExt.skipCheckSyncFinishedForFlexible(this)) {
            return true;
        }
        if (this.mSyncState == 1) {
            return false;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = this.mChildren.get(i);
            boolean childFinished = group.isIgnoring(child) || child.isSyncFinished(group);
            if (childFinished && child.isVisibleRequested() && child.fillsParent()) {
                return true;
            }
            if (!childFinished && (this.mWindowContainerExt.notSkipSyncFinishedWhenCanvas(group) || !this.mWindowContainerExt.skipCheckSyncFinished(this, child, this.mChildren.size()))) {
                return false;
            }
        }
        return true;
    }

    boolean allSyncFinished() {
        if (!isVisibleRequested()) {
            return true;
        }
        if (this.mSyncState != 2) {
            return false;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = this.mChildren.get(i);
            if (!child.allSyncFinished()) {
                return false;
            }
        }
        return true;
    }

    private void onSyncReparent(com.android.server.wm.WindowContainer oldParent, com.android.server.wm.WindowContainer newParent) {
        if (this.mSyncState != 0 && oldParent != null && newParent != null && oldParent.getDisplayContent() != null && newParent.getDisplayContent() != null && oldParent.getDisplayContent() != newParent.getDisplayContent()) {
            this.mTransitionController.setReady(oldParent.getDisplayContent());
        }
        if (newParent == null || newParent.mSyncState == 0) {
            if (this.mSyncState == 0) {
                return;
            }
            if (newParent == null) {
                com.android.server.wm.BLASTSyncEngine.SyncGroup syncGroup = getSyncGroup();
                if (oldParent.mSyncState != 0) {
                    finishSync(oldParent.mSyncTransaction, syncGroup, true);
                    return;
                } else if (syncGroup != null) {
                    finishSync(syncGroup.getOrphanTransaction(), syncGroup, true);
                    return;
                } else {
                    android.util.Slog.wtf(TAG, this + " is in sync mode without a sync group");
                    finishSync(getPendingTransaction(), null, true);
                    return;
                }
            }
            if (this.mSyncGroup == null) {
                finishSync(getPendingTransaction(), getSyncGroup(), true);
                return;
            }
        }
        if (oldParent != null && newParent != null && !shouldUpdateSyncOnReparent()) {
            return;
        }
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            this.mSyncState = 0;
            this.mSyncMethodOverride = -1;
        }
        prepareSync();
    }

    protected boolean shouldUpdateSyncOnReparent() {
        return true;
    }

    void registerWindowContainerListener(com.android.server.wm.WindowContainerListener listener) {
        registerWindowContainerListener(listener, true);
    }

    void registerWindowContainerListener(com.android.server.wm.WindowContainerListener listener, boolean shouldDispatchConfig) {
        if (this.mListeners.contains(listener)) {
            return;
        }
        this.mListeners.add(listener);
        registerConfigurationChangeListener(listener, shouldDispatchConfig);
        if (shouldDispatchConfig) {
            listener.onDisplayChanged(getDisplayContent());
        }
    }

    void unregisterWindowContainerListener(com.android.server.wm.WindowContainerListener listener) {
        this.mListeners.remove(listener);
        unregisterConfigurationChangeListener(listener);
    }

    static void overrideConfigurationPropagation(com.android.server.wm.WindowContainer<?> receiver, com.android.server.wm.WindowContainer<?> supplier) {
        overrideConfigurationPropagation(receiver, supplier, null);
    }

    static com.android.server.wm.WindowContainerListener overrideConfigurationPropagation(final com.android.server.wm.WindowContainer<?> receiver, final com.android.server.wm.WindowContainer<?> supplier, final com.android.server.wm.WindowContainer.ConfigurationMerger configurationMerger) {
        final com.android.server.wm.ConfigurationContainerListener listener = new com.android.server.wm.ConfigurationContainerListener() { // from class: com.android.server.wm.WindowContainer.1
            @Override // com.android.server.wm.ConfigurationContainerListener
            public void onMergedOverrideConfigurationChanged(android.content.res.Configuration mergedOverrideConfig) {
                android.content.res.Configuration mergedConfiguration;
                if (configurationMerger != null) {
                    mergedConfiguration = configurationMerger.merge(mergedOverrideConfig, receiver.getRequestedOverrideConfiguration());
                } else {
                    mergedConfiguration = supplier.getConfiguration();
                }
                receiver.onRequestedOverrideConfigurationChanged(mergedConfiguration);
            }
        };
        supplier.registerConfigurationChangeListener(listener);
        com.android.server.wm.WindowContainerListener wcListener = new com.android.server.wm.WindowContainerListener() { // from class: com.android.server.wm.WindowContainer.2
            @Override // com.android.server.wm.WindowContainerListener
            public void onRemoved() {
                com.android.server.wm.WindowContainer.this.unregisterWindowContainerListener(this);
                supplier.unregisterConfigurationChangeListener(listener);
            }
        };
        receiver.registerWindowContainerListener(wcListener);
        return wcListener;
    }

    int getWindowType() {
        return -1;
    }

    boolean setCanScreenshot(android.view.SurfaceControl.Transaction t, boolean canScreenshot) {
        if (this.mSurfaceControl == null) {
            return false;
        }
        t.setSecure(this.mSurfaceControl, !canScreenshot);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class AnimationRunnerBuilder {
        private final java.util.List<java.lang.Runnable> mOnAnimationCancelled;
        private final java.util.List<java.lang.Runnable> mOnAnimationFinished;

        private AnimationRunnerBuilder() {
            this.mOnAnimationFinished = new java.util.LinkedList();
            this.mOnAnimationCancelled = new java.util.LinkedList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setTaskBackgroundColor(int backgroundColor) {
            final com.android.server.wm.TaskDisplayArea taskDisplayArea = com.android.server.wm.WindowContainer.this.getTaskDisplayArea();
            if (taskDisplayArea != null && backgroundColor != 0) {
                taskDisplayArea.setBackgroundColor(backgroundColor);
                final java.util.concurrent.atomic.AtomicInteger callbackCounter = new java.util.concurrent.atomic.AtomicInteger(0);
                java.lang.Runnable clearBackgroundColorHandler = new java.lang.Runnable() { // from class: com.android.server.wm.WindowContainer$AnimationRunnerBuilder$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.wm.WindowContainer.AnimationRunnerBuilder.lambda$setTaskBackgroundColor$0(callbackCounter, taskDisplayArea);
                    }
                };
                this.mOnAnimationFinished.add(clearBackgroundColorHandler);
                this.mOnAnimationCancelled.add(clearBackgroundColorHandler);
            }
        }

        static /* synthetic */ void lambda$setTaskBackgroundColor$0(java.util.concurrent.atomic.AtomicInteger callbackCounter, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
            if (callbackCounter.getAndIncrement() == 0) {
                taskDisplayArea.clearBackgroundColor();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.WindowContainer.IAnimationStarter build() {
            return new com.android.server.wm.WindowContainer.IAnimationStarter() { // from class: com.android.server.wm.WindowContainer$AnimationRunnerBuilder$$ExternalSyntheticLambda1
                @Override // com.android.server.wm.WindowContainer.IAnimationStarter
                public final void startAnimation(android.view.SurfaceControl.Transaction transaction, com.android.server.wm.AnimationAdapter animationAdapter, boolean z, int i, com.android.server.wm.AnimationAdapter animationAdapter2) {
                    this.f$0.lambda$build$3(transaction, animationAdapter, z, i, animationAdapter2);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$build$3(android.view.SurfaceControl.Transaction t, com.android.server.wm.AnimationAdapter adapter, boolean hidden, int type, com.android.server.wm.AnimationAdapter snapshotAnim) {
            com.android.server.wm.WindowContainer.this.startAnimation(com.android.server.wm.WindowContainer.this.getPendingTransaction(), adapter, !com.android.server.wm.WindowContainer.this.isVisible(), type, new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.WindowContainer$AnimationRunnerBuilder$$ExternalSyntheticLambda3
                @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
                    this.f$0.lambda$build$1(i, animationAdapter);
                }
            }, new java.lang.Runnable() { // from class: com.android.server.wm.WindowContainer$AnimationRunnerBuilder$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$build$2();
                }
            }, snapshotAnim);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$build$1(int animType, com.android.server.wm.AnimationAdapter anim) {
            this.mOnAnimationFinished.forEach(new com.android.server.wm.WindowContainer$AnimationRunnerBuilder$$ExternalSyntheticLambda0());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$build$2() {
            this.mOnAnimationCancelled.forEach(new com.android.server.wm.WindowContainer$AnimationRunnerBuilder$$ExternalSyntheticLambda0());
        }
    }

    void addTrustedOverlay(android.view.SurfaceControlViewHost.SurfacePackage overlay, com.android.server.wm.WindowState initialWindowState) {
        if (this.mOverlayHost == null) {
            this.mOverlayHost = new com.android.server.wm.TrustedOverlayHost(this.mWmService);
        }
        this.mOverlayHost.addOverlay(overlay, this.mSurfaceControl);
        try {
            overlay.getRemoteInterface().onConfigurationChanged(getConfiguration());
        } catch (java.lang.Exception e) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[4]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -4267530270533009730L, 0, null, null);
            }
            removeTrustedOverlay(overlay);
        }
        if (initialWindowState != null) {
            android.view.InsetsState insetsState = initialWindowState.getInsetsState();
            android.graphics.Rect dispBounds = getBounds();
            try {
                overlay.getRemoteInterface().onInsetsChanged(insetsState, dispBounds);
            } catch (java.lang.Exception e2) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[4]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, 5179630990780610966L, 0, null, null);
                }
                removeTrustedOverlay(overlay);
            }
        }
    }

    void removeTrustedOverlay(android.view.SurfaceControlViewHost.SurfacePackage overlay) {
        if (this.mOverlayHost != null && !this.mOverlayHost.removeOverlay(overlay)) {
            this.mOverlayHost.release();
            this.mOverlayHost = null;
        }
    }

    void updateOverlayInsetsState(com.android.server.wm.WindowState originalChange) {
        com.android.server.wm.WindowContainer p = getParent();
        if (p != null) {
            p.updateOverlayInsetsState(originalChange);
        }
    }

    void waitForSyncTransactionCommit(android.util.ArraySet<com.android.server.wm.WindowContainer> wcAwaitingCommit) {
        if (wcAwaitingCommit.contains(this)) {
            return;
        }
        this.mSyncTransactionCommitCallbackDepth++;
        wcAwaitingCommit.add(this);
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            this.mChildren.get(i).waitForSyncTransactionCommit(wcAwaitingCommit);
        }
        this.mWindowContainerExt.hookWaitForSyncTransactionCommit(this);
    }

    void onSyncTransactionCommitted(android.view.SurfaceControl.Transaction t) {
        this.mSyncTransactionCommitCallbackDepth--;
        if (this.mSyncTransactionCommitCallbackDepth > 0 || this.mSyncState != 0) {
            return;
        }
        t.merge(this.mSyncTransaction);
    }

    public com.android.server.wm.IWindowContainerWrapper getWCWrapper() {
        return this.mWindowContainerWrapper;
    }

    private class WindowContainerWrapper implements com.android.server.wm.IWindowContainerWrapper {
        private WindowContainerWrapper() {
        }

        @Override // com.android.server.wm.IWindowContainerWrapper
        public com.android.server.wm.IWindowContainerExt getExtImpl() {
            return com.android.server.wm.WindowContainer.this.mWindowContainerExt;
        }

        @Override // com.android.server.wm.IWindowContainerWrapper
        public int syncTransactionCommitCallbackDepth() {
            return com.android.server.wm.WindowContainer.this.mSyncTransactionCommitCallbackDepth;
        }

        @Override // com.android.server.wm.IWindowContainerWrapper
        public java.util.List<com.android.server.wm.WindowContainerListener> getWindowContainerListener() {
            return com.android.server.wm.WindowContainer.this.mListeners;
        }
    }
}
