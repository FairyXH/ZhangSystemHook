package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayArea<T extends com.android.server.wm.WindowContainer> extends com.android.server.wm.WindowContainer<T> {
    boolean mDisplayAreaAppearedSent;
    com.android.server.wm.IDisplayAreaExt mDisplayAreaExt;
    final int mFeatureId;
    private final java.lang.String mName;
    android.window.IDisplayAreaOrganizer mOrganizer;
    private final com.android.server.wm.DisplayAreaOrganizerController mOrganizerController;
    protected boolean mSetIgnoreOrientationRequest;
    private final android.content.res.Configuration mTmpConfiguration;
    protected final com.android.server.wm.DisplayArea.Type mType;

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void commitPendingTransaction() {
        super.commitPendingTransaction();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ int compareTo(com.android.server.wm.WindowContainer windowContainer) {
        return super.compareTo(windowContainer);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getAnimationLeash() {
        return super.getAnimationLeash();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getAnimationLeashParent() {
        return super.getAnimationLeashParent();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getFreezeSnapshotTarget() {
        return super.getFreezeSnapshotTarget();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ android.util.SparseArray getInsetsSourceProviders() {
        return super.getInsetsSourceProviders();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getParentSurfaceControl() {
        return super.getParentSurfaceControl();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl.Transaction getPendingTransaction() {
        return super.getPendingTransaction();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getSurfaceControl() {
        return super.getSurfaceControl();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ int getSurfaceHeight() {
        return super.getSurfaceHeight();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ int getSurfaceWidth() {
        return super.getSurfaceWidth();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl.Transaction getSyncTransaction() {
        return super.getSyncTransaction();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ com.android.server.wm.IWindowContainerWrapper getWCWrapper() {
        return super.getWCWrapper();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ boolean hasInsetsSourceProvider() {
        return super.hasInsetsSourceProvider();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl.Builder makeAnimationLeash() {
        return super.makeAnimationLeash();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void onAnimationLeashCreated(android.view.SurfaceControl.Transaction transaction, android.view.SurfaceControl surfaceControl) {
        super.onAnimationLeashCreated(transaction, surfaceControl);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void onAnimationLeashLost(android.view.SurfaceControl.Transaction transaction) {
        super.onAnimationLeashLost(transaction);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public /* bridge */ /* synthetic */ void onRequestedOverrideConfigurationChanged(android.content.res.Configuration configuration) {
        super.onRequestedOverrideConfigurationChanged(configuration);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ void onUnfrozen() {
        super.onUnfrozen();
    }

    DisplayArea(com.android.server.wm.WindowManagerService wms, com.android.server.wm.DisplayArea.Type type, java.lang.String name) {
        this(wms, type, name, -1);
    }

    DisplayArea(com.android.server.wm.WindowManagerService wms, com.android.server.wm.DisplayArea.Type type, java.lang.String name, int featureId) {
        super(wms);
        this.mTmpConfiguration = new android.content.res.Configuration();
        this.mDisplayAreaExt = (com.android.server.wm.IDisplayAreaExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayAreaExt.class).base(this).create();
        setOverrideOrientation(-2);
        this.mType = type;
        this.mName = name;
        this.mFeatureId = featureId;
        this.mRemoteToken = new com.android.server.wm.WindowContainer.RemoteToken(this);
        this.mOrganizerController = wms.mAtmService.mWindowOrganizerController.mDisplayAreaOrganizerController;
    }

    @Override // com.android.server.wm.WindowContainer
    void onChildPositionChanged(com.android.server.wm.WindowContainer child) {
        super.onChildPositionChanged(child);
        com.android.server.wm.DisplayArea.Type.checkChild(this.mType, com.android.server.wm.DisplayArea.Type.typeOf(child));
        if (child instanceof com.android.server.wm.Task) {
            return;
        }
        for (int i = 1; i < getChildCount(); i++) {
            com.android.server.wm.WindowContainer top = getChildAt(i - 1);
            com.android.server.wm.WindowContainer bottom = getChildAt(i);
            if (child == top || child == bottom) {
                com.android.server.wm.DisplayArea.Type.checkSiblings(com.android.server.wm.DisplayArea.Type.typeOf(top), com.android.server.wm.DisplayArea.Type.typeOf(bottom));
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void positionChildAt(int position, T child, boolean includingParents) {
        if (child.asDisplayArea() == null) {
            super.positionChildAt(position, child, includingParents);
            return;
        }
        int targetPosition = findPositionForChildDisplayArea(position, child.asDisplayArea());
        super.positionChildAt(targetPosition, child, false);
        com.android.server.wm.WindowContainer parent = getParent();
        if (!includingParents || parent == null) {
            return;
        }
        if (position == Integer.MAX_VALUE || position == Integer.MIN_VALUE) {
            parent.positionChildAt(position, this, true);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    int getOrientation(int candidate) {
        int orientation = super.getOrientation(candidate);
        if (shouldIgnoreOrientationRequest(orientation)) {
            this.mLastOrientationSource = null;
            return -2;
        }
        return orientation;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean handlesOrientationChangeFromDescendant(int orientation) {
        return !shouldIgnoreOrientationRequest(orientation) && super.handlesOrientationChangeFromDescendant(orientation);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean onDescendantOrientationChanged(com.android.server.wm.WindowContainer requestingContainer) {
        int orientation;
        if (requestingContainer != null) {
            orientation = requestingContainer.getOverrideOrientation();
        } else {
            orientation = -2;
        }
        return !shouldIgnoreOrientationRequest(orientation) && super.onDescendantOrientationChanged(requestingContainer);
    }

    boolean setIgnoreOrientationRequest(boolean ignoreOrientationRequest) {
        if (this.mSetIgnoreOrientationRequest == ignoreOrientationRequest) {
            return false;
        }
        this.mSetIgnoreOrientationRequest = ignoreOrientationRequest;
        if (this.mDisplayContent == null) {
            return false;
        }
        if (this.mDisplayContent.mFocusedApp != null) {
            this.mDisplayContent.onLastFocusedTaskDisplayAreaChanged(this.mDisplayContent.mFocusedApp.getDisplayArea());
        }
        if (!ignoreOrientationRequest) {
            return this.mDisplayContent.updateOrientation();
        }
        int lastOrientation = this.mDisplayContent.getLastOrientation();
        com.android.server.wm.WindowContainer lastOrientationSource = this.mDisplayContent.getLastOrientationSource();
        if (lastOrientation == -2 || lastOrientation == -1) {
            return false;
        }
        if (lastOrientationSource == null || lastOrientationSource.isDescendantOf(this)) {
            return this.mDisplayContent.updateOrientation();
        }
        return false;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        if (isAlwaysOnTop() == alwaysOnTop) {
            return;
        }
        super.setAlwaysOnTop(alwaysOnTop);
        if (getParent().asDisplayArea() != null) {
            getParent().asDisplayArea().positionChildAt(Integer.MAX_VALUE, this, false);
        }
    }

    boolean shouldIgnoreOrientationRequest(int orientation) {
        return (orientation == 14 || orientation == 14 || !getIgnoreOrientationRequest() || shouldRespectOrientationRequestDueToPerAppOverride()) ? false : true;
    }

    private boolean shouldRespectOrientationRequestDueToPerAppOverride() {
        com.android.server.wm.ActivityRecord activity;
        return (this.mDisplayContent == null || (activity = this.mDisplayContent.topRunningActivity(true)) == null || activity.getTaskFragment() == null || activity.getTaskFragment().getWindowingMode() != 1 || !activity.mLetterboxUiController.isOverrideRespectRequestedOrientationEnabled()) ? false : true;
    }

    boolean getIgnoreOrientationRequest() {
        return this.mSetIgnoreOrientationRequest && !this.mWmService.isIgnoreOrientationRequestDisabled();
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private int findPositionForChildDisplayArea(int requestPosition, com.android.server.wm.DisplayArea child) {
        if (child.getParent() != this) {
            throw new java.lang.IllegalArgumentException("positionChildAt: container=" + child.getName() + " is not a child of container=" + getName() + " current parent=" + child.getParent());
        }
        int maxPosition = findMaxPositionForChildDisplayArea(child);
        int minPosition = findMinPositionForChildDisplayArea(child);
        int alwaysOnTopCount = 0;
        for (int i = minPosition; i <= maxPosition; i++) {
            if (((com.android.server.wm.WindowContainer) this.mChildren.get(i)).isAlwaysOnTop()) {
                alwaysOnTopCount++;
            }
        }
        if (child.isAlwaysOnTop()) {
            minPosition = (maxPosition - alwaysOnTopCount) + 1;
        } else {
            maxPosition -= alwaysOnTopCount;
        }
        return java.lang.Math.max(java.lang.Math.min(requestPosition, maxPosition), minPosition);
    }

    private int findMaxPositionForChildDisplayArea(com.android.server.wm.DisplayArea child) {
        com.android.server.wm.DisplayArea.Type childType = com.android.server.wm.DisplayArea.Type.typeOf(child);
        for (int i = this.mChildren.size() - 1; i > 0; i--) {
            if (com.android.server.wm.DisplayArea.Type.typeOf(getChildAt(i)) == childType) {
                return i;
            }
        }
        return 0;
    }

    private int findMinPositionForChildDisplayArea(com.android.server.wm.DisplayArea child) {
        com.android.server.wm.DisplayArea.Type childType = com.android.server.wm.DisplayArea.Type.typeOf(child);
        for (int i = 0; i < this.mChildren.size(); i++) {
            if (com.android.server.wm.DisplayArea.Type.typeOf(getChildAt(i)) == childType) {
                return i;
            }
        }
        return this.mChildren.size() - 1;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean needsZBoost() {
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean fillsParent() {
        return true;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    java.lang.String getName() {
        return this.mName;
    }

    public java.lang.String toString() {
        return this.mName + "@" + java.lang.System.identityHashCode(this);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        if (logLevel == 2 && !isVisible()) {
            return;
        }
        long token = proto.start(fieldId);
        super.dumpDebug(proto, 1146756268033L, logLevel);
        proto.write(1138166333442L, this.mName);
        proto.write(1133871366148L, isTaskDisplayArea());
        proto.write(1133871366149L, asRootDisplayArea() != null);
        proto.write(1120986464262L, this.mFeatureId);
        proto.write(1133871366151L, isOrganized());
        proto.write(1133871366152L, getIgnoreOrientationRequest());
        proto.end(token);
    }

    @Override // com.android.server.wm.WindowContainer
    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        super.dump(pw, prefix, dumpAll);
        if (this.mSetIgnoreOrientationRequest) {
            pw.println(prefix + "mSetIgnoreOrientationRequest=true");
        }
        if (hasRequestedOverrideConfiguration()) {
            pw.println(prefix + "overrideConfig=" + getRequestedOverrideConfiguration());
        }
    }

    void dumpChildDisplayArea(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        java.lang.String doublePrefix = prefix + "  ";
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayArea<?> childArea = getChildAt(i).asDisplayArea();
            if (childArea != null) {
                pw.print(prefix + "* " + childArea.getName());
                if (childArea.isOrganized()) {
                    pw.print(" (organized)");
                }
                pw.println();
                if (!childArea.isTaskDisplayArea()) {
                    childArea.dump(pw, doublePrefix, dumpAll);
                    childArea.dumpChildDisplayArea(pw, doublePrefix, dumpAll);
                }
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268036L;
    }

    @Override // com.android.server.wm.WindowContainer
    final com.android.server.wm.DisplayArea asDisplayArea() {
        return this;
    }

    com.android.server.wm.DisplayArea.Tokens asTokens() {
        return null;
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.ActivityRecord getActivity(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom, com.android.server.wm.ActivityRecord boundary) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return null;
        }
        return super.getActivity(callback, traverseTopToBottom, boundary);
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.Task getTask(java.util.function.Predicate<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return null;
        }
        return super.getTask(callback, traverseTopToBottom);
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.Task getRootTask(java.util.function.Predicate<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return null;
        }
        return super.getRootTask(callback, traverseTopToBottom);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean forAllActivities(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return false;
        }
        return super.forAllActivities(callback, traverseTopToBottom);
    }

    @Override // com.android.server.wm.WindowContainer
    void forAllActivities(java.util.function.Consumer<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return;
        }
        super.forAllActivities(callback, traverseTopToBottom);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean forAllRootTasks(java.util.function.Predicate<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return false;
        }
        return super.forAllRootTasks(callback, traverseTopToBottom);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean forAllTasks(java.util.function.Predicate<com.android.server.wm.Task> callback) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return false;
        }
        return super.forAllTasks(callback);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean forAllLeafTasks(java.util.function.Predicate<com.android.server.wm.Task> callback) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return false;
        }
        return super.forAllLeafTasks(callback);
    }

    @Override // com.android.server.wm.WindowContainer
    void forAllLeafTasks(java.util.function.Consumer<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return;
        }
        super.forAllLeafTasks(callback, traverseTopToBottom);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean forAllLeafTaskFragments(java.util.function.Predicate<com.android.server.wm.TaskFragment> callback) {
        if (this.mType == com.android.server.wm.DisplayArea.Type.ABOVE_TASKS) {
            return false;
        }
        return super.forAllLeafTaskFragments(callback);
    }

    @Override // com.android.server.wm.WindowContainer
    void forAllDisplayAreas(java.util.function.Consumer<com.android.server.wm.DisplayArea> callback) {
        super.forAllDisplayAreas(callback);
        callback.accept(this);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.WindowContainer
    boolean forAllTaskDisplayAreas(java.util.function.Predicate<com.android.server.wm.TaskDisplayArea> callback, boolean traverseTopToBottom) {
        if (this.mType != com.android.server.wm.DisplayArea.Type.ANY) {
            return false;
        }
        int childCount = this.mChildren.size();
        int i = traverseTopToBottom ? childCount - 1 : 0;
        while (i >= 0 && i < childCount) {
            com.android.server.wm.WindowContainer windowContainer = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            int i2 = 1;
            if (windowContainer.asDisplayArea() != null && windowContainer.asDisplayArea().forAllTaskDisplayAreas(callback, traverseTopToBottom)) {
                return true;
            }
            if (traverseTopToBottom) {
                i2 = -1;
            }
            i += i2;
        }
        return false;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.WindowContainer
    void forAllTaskDisplayAreas(java.util.function.Consumer<com.android.server.wm.TaskDisplayArea> callback, boolean traverseTopToBottom) {
        if (this.mType != com.android.server.wm.DisplayArea.Type.ANY) {
            return;
        }
        int childCount = this.mChildren.size();
        int i = traverseTopToBottom ? childCount - 1 : 0;
        while (i >= 0 && i < childCount) {
            com.android.server.wm.WindowContainer windowContainer = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            if (windowContainer.asDisplayArea() != null) {
                windowContainer.asDisplayArea().forAllTaskDisplayAreas(callback, traverseTopToBottom);
            }
            i += traverseTopToBottom ? -1 : 1;
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.WindowContainer
    <R> R reduceOnAllTaskDisplayAreas(java.util.function.BiFunction<com.android.server.wm.TaskDisplayArea, R, R> biFunction, R r, boolean z) {
        if (this.mType != com.android.server.wm.DisplayArea.Type.ANY) {
            return r;
        }
        int size = this.mChildren.size();
        int i = z ? size - 1 : 0;
        R r2 = r;
        while (i >= 0 && i < size) {
            com.android.server.wm.WindowContainer windowContainer = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            if (windowContainer.asDisplayArea() != null) {
                r2 = (R) windowContainer.asDisplayArea().reduceOnAllTaskDisplayAreas(biFunction, r2, z);
            }
            i += z ? -1 : 1;
        }
        return r2;
    }

    @Override // com.android.server.wm.WindowContainer
    <R> R getItemFromDisplayAreas(java.util.function.Function<com.android.server.wm.DisplayArea, R> function) {
        R r = (R) super.getItemFromDisplayAreas(function);
        return r != null ? r : function.apply(this);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.WindowContainer
    <R> R getItemFromTaskDisplayAreas(java.util.function.Function<com.android.server.wm.TaskDisplayArea, R> function, boolean z) {
        R r;
        if (this.mType != com.android.server.wm.DisplayArea.Type.ANY) {
            return null;
        }
        int size = this.mChildren.size();
        int i = z ? size - 1 : 0;
        while (i >= 0 && i < size) {
            com.android.server.wm.WindowContainer windowContainer = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            if (windowContainer.asDisplayArea() != null && (r = (R) windowContainer.asDisplayArea().getItemFromTaskDisplayAreas(function, z)) != null) {
                return r;
            }
            i += z ? -1 : 1;
        }
        return null;
    }

    void setOrganizer(android.window.IDisplayAreaOrganizer organizer) {
        setOrganizer(organizer, false);
    }

    void setOrganizer(android.window.IDisplayAreaOrganizer organizer, boolean skipDisplayAreaAppeared) {
        if (this.mOrganizer == organizer) {
            return;
        }
        if (this.mDisplayContent == null || !this.mDisplayContent.isTrusted()) {
            throw new java.lang.IllegalStateException("Don't organize or trigger events for unavailable or untrusted display.");
        }
        android.window.IDisplayAreaOrganizer lastOrganizer = this.mOrganizer;
        this.mOrganizer = organizer;
        sendDisplayAreaVanished(lastOrganizer);
        if (!skipDisplayAreaAppeared) {
            sendDisplayAreaAppeared();
        } else if (organizer != null) {
            this.mDisplayAreaAppearedSent = true;
        }
    }

    void sendDisplayAreaAppeared() {
        if (this.mOrganizer == null || this.mDisplayAreaAppearedSent) {
            return;
        }
        this.mOrganizerController.onDisplayAreaAppeared(this.mOrganizer, this);
        this.mDisplayAreaAppearedSent = true;
    }

    void sendDisplayAreaInfoChanged() {
        if (this.mOrganizer == null || !this.mDisplayAreaAppearedSent) {
            return;
        }
        this.mOrganizerController.onDisplayAreaInfoChanged(this.mOrganizer, this);
    }

    void sendDisplayAreaVanished(android.window.IDisplayAreaOrganizer organizer) {
        if (organizer == null || !this.mDisplayAreaAppearedSent) {
            return;
        }
        migrateToNewSurfaceControl(getSyncTransaction());
        this.mOrganizerController.onDisplayAreaVanished(organizer, this);
        this.mDisplayAreaAppearedSent = false;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
        this.mTransitionController.collectForDisplayAreaChange(this);
        this.mTmpConfiguration.setTo(getConfiguration());
        super.onConfigurationChanged(newParentConfig);
        if (this.mOrganizer != null && getConfiguration().diff(this.mTmpConfiguration) != 0) {
            sendDisplayAreaInfoChanged();
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    void resolveOverrideConfiguration(android.content.res.Configuration newParentConfiguration) {
        super.resolveOverrideConfiguration(newParentConfiguration);
        android.content.res.Configuration resolvedConfig = getResolvedOverrideConfiguration();
        android.graphics.Rect overrideBounds = resolvedConfig.windowConfiguration.getBounds();
        android.graphics.Rect overrideAppBounds = resolvedConfig.windowConfiguration.getAppBounds();
        android.graphics.Rect parentAppBounds = newParentConfiguration.windowConfiguration.getAppBounds();
        if (!overrideBounds.isEmpty()) {
            if ((overrideAppBounds == null || overrideAppBounds.isEmpty()) && parentAppBounds != null && !parentAppBounds.isEmpty()) {
                android.graphics.Rect appBounds = new android.graphics.Rect(overrideBounds);
                appBounds.intersect(parentAppBounds);
                resolvedConfig.windowConfiguration.setAppBounds(appBounds);
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isOrganized() {
        return this.mOrganizer != null;
    }

    android.window.DisplayAreaInfo getDisplayAreaInfo() {
        android.window.DisplayAreaInfo info = new android.window.DisplayAreaInfo(this.mRemoteToken.toWindowContainerToken(), getDisplayContent().getDisplayId(), this.mFeatureId);
        com.android.server.wm.RootDisplayArea root = getRootDisplayArea();
        info.rootDisplayAreaId = root == null ? getDisplayContent().mFeatureId : root.mFeatureId;
        info.configuration.setTo(getConfiguration());
        return info;
    }

    void getStableRect(android.graphics.Rect out) {
        if (this.mDisplayContent == null) {
            getBounds(out);
        } else {
            this.mDisplayContent.getStableRect(out);
            out.intersect(getBounds());
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean providesMaxBounds() {
        return true;
    }

    boolean isTaskDisplayArea() {
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    void removeImmediately() {
        setOrganizer(null);
        super.removeImmediately();
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.DisplayArea getDisplayArea() {
        return this;
    }

    public static class Tokens extends com.android.server.wm.DisplayArea<com.android.server.wm.WindowToken> {
        private final java.util.function.Predicate<com.android.server.wm.WindowState> mGetOrientingWindow;
        int mLastKeyguardForcedOrientation;
        private final java.util.Comparator<com.android.server.wm.WindowToken> mWindowComparator;

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ void commitPendingTransaction() {
            super.commitPendingTransaction();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
        public /* bridge */ /* synthetic */ int compareTo(com.android.server.wm.WindowContainer windowContainer) {
            return super.compareTo(windowContainer);
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ android.view.SurfaceControl getAnimationLeash() {
            return super.getAnimationLeash();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ android.view.SurfaceControl getAnimationLeashParent() {
            return super.getAnimationLeashParent();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
        public /* bridge */ /* synthetic */ android.view.SurfaceControl getFreezeSnapshotTarget() {
            return super.getFreezeSnapshotTarget();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
        public /* bridge */ /* synthetic */ android.util.SparseArray getInsetsSourceProviders() {
            return super.getInsetsSourceProviders();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ android.view.SurfaceControl getParentSurfaceControl() {
            return super.getParentSurfaceControl();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ android.view.SurfaceControl.Transaction getPendingTransaction() {
            return super.getPendingTransaction();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ android.view.SurfaceControl getSurfaceControl() {
            return super.getSurfaceControl();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ int getSurfaceHeight() {
            return super.getSurfaceHeight();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ int getSurfaceWidth() {
            return super.getSurfaceWidth();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ android.view.SurfaceControl.Transaction getSyncTransaction() {
            return super.getSyncTransaction();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
        public /* bridge */ /* synthetic */ com.android.server.wm.IWindowContainerWrapper getWCWrapper() {
            return super.getWCWrapper();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
        public /* bridge */ /* synthetic */ boolean hasInsetsSourceProvider() {
            return super.hasInsetsSourceProvider();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ android.view.SurfaceControl.Builder makeAnimationLeash() {
            return super.makeAnimationLeash();
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ void onAnimationLeashCreated(android.view.SurfaceControl.Transaction transaction, android.view.SurfaceControl surfaceControl) {
            super.onAnimationLeashCreated(transaction, surfaceControl);
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
        public /* bridge */ /* synthetic */ void onAnimationLeashLost(android.view.SurfaceControl.Transaction transaction) {
            super.onAnimationLeashLost(transaction);
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
        public /* bridge */ /* synthetic */ void onRequestedOverrideConfigurationChanged(android.content.res.Configuration configuration) {
            super.onRequestedOverrideConfigurationChanged(configuration);
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
        public /* bridge */ /* synthetic */ void onUnfrozen() {
            super.onUnfrozen();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$0(com.android.server.wm.WindowState w) {
            if (!w.isVisible() || !w.mLegacyPolicyVisibilityAfterAnim) {
                return false;
            }
            com.android.server.policy.WindowManagerPolicy policy = this.mWmService.mPolicy;
            if (policy.isKeyguardHostWindow(w.mAttrs)) {
                if (!this.mDisplayContent.isKeyguardLocked() && this.mDisplayContent.getDisplayPolicy().isAwake() && policy.okToAnimate(true)) {
                    return false;
                }
                boolean isUnoccluding = this.mDisplayContent.mAppTransition.isUnoccluding() && this.mDisplayContent.mUnknownAppVisibilityController.allResolved();
                if (policy.isKeyguardShowingAndNotOccluded() || isUnoccluding) {
                    return true;
                }
            }
            int req = w.mAttrs.screenOrientation;
            if (req == -1 || req == 3 || req == -2) {
                return false;
            }
            return (req == 1 && this.mDisplayAreaExt.shouldBlockOrientingWindowDuringFixedRotation(this.mWmService, this.mDisplayContent, w, req)) ? false : true;
        }

        Tokens(com.android.server.wm.WindowManagerService wms, com.android.server.wm.DisplayArea.Type type, java.lang.String name) {
            this(wms, type, name, 2);
        }

        Tokens(com.android.server.wm.WindowManagerService wms, com.android.server.wm.DisplayArea.Type type, java.lang.String name, int featureId) {
            super(wms, type, name, featureId);
            this.mLastKeyguardForcedOrientation = -1;
            this.mWindowComparator = java.util.Comparator.comparingInt(new java.util.function.ToIntFunction() { // from class: com.android.server.wm.DisplayArea$Tokens$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(java.lang.Object obj) {
                    return ((com.android.server.wm.WindowToken) obj).getWindowLayerFromType();
                }
            });
            this.mGetOrientingWindow = new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayArea$Tokens$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$new$0((com.android.server.wm.WindowState) obj);
                }
            };
        }

        void addChild(com.android.server.wm.WindowToken token) {
            addChild(token, this.mWindowComparator);
        }

        @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
        int getOrientation(int candidate) {
            this.mLastOrientationSource = null;
            com.android.server.wm.WindowState win = getWindow(this.mGetOrientingWindow);
            if (win == null) {
                return candidate;
            }
            int req = win.mAttrs.screenOrientation;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
                long protoLogParam1 = req;
                long protoLogParam2 = this.mDisplayContent.getDisplayId();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 2230151187668089583L, 20, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
            }
            if (this.mWmService.mPolicy.isKeyguardHostWindow(win.mAttrs)) {
                if (req != -2 && req != -1) {
                    this.mLastKeyguardForcedOrientation = req;
                } else {
                    req = this.mLastKeyguardForcedOrientation;
                }
            }
            this.mLastOrientationSource = win;
            return req;
        }

        @Override // com.android.server.wm.DisplayArea
        final com.android.server.wm.DisplayArea.Tokens asTokens() {
            return this;
        }
    }

    static class Dimmable extends com.android.server.wm.DisplayArea<com.android.server.wm.DisplayArea> {
        private final com.android.server.wm.Dimmer mDimmer;

        Dimmable(com.android.server.wm.WindowManagerService wms, com.android.server.wm.DisplayArea.Type type, java.lang.String name, int featureId) {
            super(wms, type, name, featureId);
            this.mDimmer = com.android.server.wm.Dimmer.create(this);
        }

        @Override // com.android.server.wm.WindowContainer
        com.android.server.wm.Dimmer getDimmer() {
            return this.mDimmer;
        }

        @Override // com.android.server.wm.WindowContainer
        void prepareSurfaces() {
            this.mDimmer.resetDimStates();
            super.prepareSurfaces();
            android.graphics.Rect dimBounds = this.mDimmer.getDimBounds();
            if (dimBounds != null) {
                getBounds(dimBounds);
                dimBounds.offsetTo(0, 0);
            }
            if (!this.mTransitionController.isShellTransitionsEnabled() && forAllTasks(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayArea$Dimmable$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.DisplayArea.Dimmable.lambda$prepareSurfaces$0((com.android.server.wm.Task) obj);
                }
            })) {
                this.mDimmer.resetDimStates();
            }
            if (dimBounds != null && this.mDimmer.updateDims(getSyncTransaction())) {
                scheduleAnimation();
            }
        }

        static /* synthetic */ boolean lambda$prepareSurfaces$0(com.android.server.wm.Task task) {
            return !task.canAffectSystemUiFlags();
        }
    }

    enum Type {
        ABOVE_TASKS,
        BELOW_TASKS,
        ANY;

        static void checkSiblings(com.android.server.wm.DisplayArea.Type bottom, com.android.server.wm.DisplayArea.Type top) {
            com.android.internal.util.Preconditions.checkState(bottom == BELOW_TASKS || top != BELOW_TASKS, bottom + " must be above BELOW_TASKS");
            com.android.internal.util.Preconditions.checkState(bottom != ABOVE_TASKS || top == ABOVE_TASKS, top + " must be below ABOVE_TASKS");
        }

        static void checkChild(com.android.server.wm.DisplayArea.Type parent, com.android.server.wm.DisplayArea.Type child) {
            switch (parent) {
                case ABOVE_TASKS:
                    com.android.internal.util.Preconditions.checkState(child == ABOVE_TASKS, "ABOVE_TASKS can only contain ABOVE_TASKS");
                    break;
                case BELOW_TASKS:
                    com.android.internal.util.Preconditions.checkState(child == BELOW_TASKS, "BELOW_TASKS can only contain BELOW_TASKS");
                    break;
            }
        }

        static com.android.server.wm.DisplayArea.Type typeOf(com.android.server.wm.WindowContainer c) {
            if (c.asDisplayArea() != null) {
                return ((com.android.server.wm.DisplayArea) c).mType;
            }
            if ((c instanceof com.android.server.wm.WindowToken) && !(c instanceof com.android.server.wm.ActivityRecord)) {
                return typeOf((com.android.server.wm.WindowToken) c);
            }
            if (c instanceof com.android.server.wm.Task) {
                return ANY;
            }
            throw new java.lang.IllegalArgumentException("Unknown container: " + c);
        }

        private static com.android.server.wm.DisplayArea.Type typeOf(com.android.server.wm.WindowToken c) {
            return c.getWindowLayerFromType() < 2 ? BELOW_TASKS : ABOVE_TASKS;
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.WindowContainer
    void assignChildLayers(android.view.SurfaceControl.Transaction t) {
        int layer = 0;
        int j = 0;
        while (j < this.mChildren.size()) {
            com.android.server.wm.WindowContainer wc = (com.android.server.wm.WindowContainer) this.mChildren.get(j);
            wc.assignChildLayers(t);
            wc.assignLayer(t, layer);
            j++;
            layer++;
        }
    }
}
