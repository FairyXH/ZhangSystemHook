package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DisplayAreaPolicyBuilder {
    private final java.util.ArrayList<com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder> mDisplayAreaGroupHierarchyBuilders = new java.util.ArrayList<>();
    private com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder mRootHierarchyBuilder;
    private java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, com.android.server.wm.RootDisplayArea> mSelectRootForWindowFunc;
    private java.util.function.Function<android.os.Bundle, com.android.server.wm.TaskDisplayArea> mSelectTaskDisplayAreaFunc;

    interface NewDisplayAreaSupplier {
        com.android.server.wm.DisplayArea create(com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.DisplayArea.Type type, java.lang.String str, int i);
    }

    DisplayAreaPolicyBuilder() {
    }

    com.android.server.wm.DisplayAreaPolicyBuilder setRootHierarchy(com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder rootHierarchyBuilder) {
        this.mRootHierarchyBuilder = rootHierarchyBuilder;
        return this;
    }

    com.android.server.wm.DisplayAreaPolicyBuilder addDisplayAreaGroupHierarchy(com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder displayAreaGroupHierarchy) {
        this.mDisplayAreaGroupHierarchyBuilders.add(displayAreaGroupHierarchy);
        return this;
    }

    com.android.server.wm.DisplayAreaPolicyBuilder setSelectRootForWindowFunc(java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, com.android.server.wm.RootDisplayArea> selectRootForWindowFunc) {
        this.mSelectRootForWindowFunc = selectRootForWindowFunc;
        return this;
    }

    com.android.server.wm.DisplayAreaPolicyBuilder setSelectTaskDisplayAreaFunc(java.util.function.Function<android.os.Bundle, com.android.server.wm.TaskDisplayArea> selectTaskDisplayAreaFunc) {
        this.mSelectTaskDisplayAreaFunc = selectTaskDisplayAreaFunc;
        return this;
    }

    private void validate() {
        if (this.mRootHierarchyBuilder == null) {
            throw new java.lang.IllegalStateException("Root must be set for the display area policy.");
        }
        java.util.Set<java.lang.Integer> uniqueIdSet = new android.util.ArraySet<>();
        java.util.Set<java.lang.Integer> allIdSet = new android.util.ArraySet<>();
        validateIds(this.mRootHierarchyBuilder, uniqueIdSet, allIdSet);
        boolean containsImeContainer = this.mRootHierarchyBuilder.mImeContainer != null;
        boolean containsDefaultTda = containsDefaultTaskDisplayArea(this.mRootHierarchyBuilder);
        for (int i = 0; i < this.mDisplayAreaGroupHierarchyBuilders.size(); i++) {
            com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder hierarchyBuilder = this.mDisplayAreaGroupHierarchyBuilders.get(i);
            validateIds(hierarchyBuilder, uniqueIdSet, allIdSet);
            if (hierarchyBuilder.mTaskDisplayAreas.isEmpty()) {
                throw new java.lang.IllegalStateException("DisplayAreaGroup must contain at least one TaskDisplayArea.");
            }
            if (containsImeContainer) {
                if (hierarchyBuilder.mImeContainer != null) {
                    throw new java.lang.IllegalStateException("Only one DisplayArea hierarchy can contain the IME container");
                }
            } else {
                containsImeContainer = hierarchyBuilder.mImeContainer != null;
            }
            if (containsDefaultTda) {
                if (containsDefaultTaskDisplayArea(hierarchyBuilder)) {
                    throw new java.lang.IllegalStateException("Only one TaskDisplayArea can have the feature id of FEATURE_DEFAULT_TASK_CONTAINER");
                }
            } else {
                containsDefaultTda = containsDefaultTaskDisplayArea(hierarchyBuilder);
            }
        }
        if (!containsImeContainer) {
            throw new java.lang.IllegalStateException("IME container must be set.");
        }
        if (!containsDefaultTda) {
            throw new java.lang.IllegalStateException("There must be a default TaskDisplayArea with id of FEATURE_DEFAULT_TASK_CONTAINER.");
        }
        if (!this.mRootHierarchyBuilder.hasValidWindowingLayer()) {
            throw new java.lang.IllegalStateException("WindowingLayer must exist at the top level index");
        }
    }

    private static boolean containsDefaultTaskDisplayArea(com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder displayAreaHierarchy) {
        for (int i = 0; i < displayAreaHierarchy.mTaskDisplayAreas.size(); i++) {
            if (((com.android.server.wm.TaskDisplayArea) displayAreaHierarchy.mTaskDisplayAreas.get(i)).mFeatureId == 1) {
                return true;
            }
        }
        return false;
    }

    static boolean canBeWindowingLayer(int featureId) {
        return featureId == 4 || featureId == 9;
    }

    private static void validateIds(com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder displayAreaHierarchy, java.util.Set<java.lang.Integer> uniqueIdSet, java.util.Set<java.lang.Integer> allIdSet) {
        int rootId = displayAreaHierarchy.mRoot.mFeatureId;
        if (!allIdSet.add(java.lang.Integer.valueOf(rootId)) || !uniqueIdSet.add(java.lang.Integer.valueOf(rootId))) {
            throw new java.lang.IllegalStateException("RootDisplayArea must have unique id, but id=" + rootId + " is not unique.");
        }
        if (rootId > 20001) {
            throw new java.lang.IllegalStateException("RootDisplayArea should not have an id greater than FEATURE_VENDOR_LAST.");
        }
        for (int i = 0; i < displayAreaHierarchy.mTaskDisplayAreas.size(); i++) {
            int taskDisplayAreaId = ((com.android.server.wm.TaskDisplayArea) displayAreaHierarchy.mTaskDisplayAreas.get(i)).mFeatureId;
            if (!allIdSet.add(java.lang.Integer.valueOf(taskDisplayAreaId)) || !uniqueIdSet.add(java.lang.Integer.valueOf(taskDisplayAreaId))) {
                throw new java.lang.IllegalStateException("TaskDisplayArea must have unique id, but id=" + taskDisplayAreaId + " is not unique.");
            }
            if (taskDisplayAreaId > 20001) {
                throw new java.lang.IllegalStateException("TaskDisplayArea declared in the policy should nothave an id greater than FEATURE_VENDOR_LAST.");
            }
        }
        java.util.Set<java.lang.Integer> featureIdSet = new android.util.ArraySet<>();
        for (int i2 = 0; i2 < displayAreaHierarchy.mFeatures.size(); i2++) {
            int featureId = ((com.android.server.wm.DisplayAreaPolicyBuilder.Feature) displayAreaHierarchy.mFeatures.get(i2)).getId();
            if (uniqueIdSet.contains(java.lang.Integer.valueOf(featureId))) {
                throw new java.lang.IllegalStateException("Feature must not have same id with any RootDisplayArea or TaskDisplayArea, but id=" + featureId + " is used");
            }
            if (!featureIdSet.add(java.lang.Integer.valueOf(featureId))) {
                throw new java.lang.IllegalStateException("Feature below the same root must have unique id, but id=" + featureId + " is not unique.");
            }
            if (featureId > 20001) {
                throw new java.lang.IllegalStateException("Feature should not have an id greater than FEATURE_VENDOR_LAST.");
            }
        }
        allIdSet.addAll(featureIdSet);
    }

    com.android.server.wm.DisplayAreaPolicyBuilder.Result build(com.android.server.wm.WindowManagerService wmService) {
        if (this.mRootHierarchyBuilder != null && !this.mRootHierarchyBuilder.hasValidWindowingLayer()) {
            this.mRootHierarchyBuilder.mFeatures.add(0, new com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder(wmService.mPolicy, "WindowingLayer", 9).setExcludeRoundedCornerOverlay(false).all().build());
        }
        validate();
        this.mRootHierarchyBuilder.build(this.mDisplayAreaGroupHierarchyBuilders);
        java.util.List<com.android.server.wm.RootDisplayArea> displayAreaGroupRoots = new java.util.ArrayList<>(this.mDisplayAreaGroupHierarchyBuilders.size());
        for (int i = 0; i < this.mDisplayAreaGroupHierarchyBuilders.size(); i++) {
            com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder hierarchyBuilder = this.mDisplayAreaGroupHierarchyBuilders.get(i);
            hierarchyBuilder.build();
            displayAreaGroupRoots.add(hierarchyBuilder.mRoot);
        }
        if (this.mSelectRootForWindowFunc == null) {
            this.mSelectRootForWindowFunc = new com.android.server.wm.DisplayAreaPolicyBuilder.DefaultSelectRootForWindowFunction(this.mRootHierarchyBuilder.mRoot, displayAreaGroupRoots);
        }
        return new com.android.server.wm.DisplayAreaPolicyBuilder.Result(wmService, this.mRootHierarchyBuilder.mRoot, displayAreaGroupRoots, this.mSelectRootForWindowFunc, this.mSelectTaskDisplayAreaFunc);
    }

    private static class DefaultSelectRootForWindowFunction implements java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, com.android.server.wm.RootDisplayArea> {
        final java.util.List<com.android.server.wm.RootDisplayArea> mDisplayAreaGroupRoots;
        final com.android.server.wm.RootDisplayArea mDisplayRoot;

        DefaultSelectRootForWindowFunction(com.android.server.wm.RootDisplayArea displayRoot, java.util.List<com.android.server.wm.RootDisplayArea> displayAreaGroupRoots) {
            this.mDisplayRoot = displayRoot;
            this.mDisplayAreaGroupRoots = java.util.Collections.unmodifiableList(displayAreaGroupRoots);
        }

        @Override // java.util.function.BiFunction
        public com.android.server.wm.RootDisplayArea apply(java.lang.Integer windowType, android.os.Bundle options) {
            if (this.mDisplayAreaGroupRoots.isEmpty()) {
                return this.mDisplayRoot;
            }
            if (options != null && options.containsKey("root_display_area_id")) {
                int rootId = options.getInt("root_display_area_id");
                if (this.mDisplayRoot.mFeatureId == rootId) {
                    return this.mDisplayRoot;
                }
                for (int i = this.mDisplayAreaGroupRoots.size() - 1; i >= 0; i--) {
                    if (this.mDisplayAreaGroupRoots.get(i).mFeatureId == rootId) {
                        return this.mDisplayAreaGroupRoots.get(i);
                    }
                }
            }
            return this.mDisplayRoot;
        }
    }

    private static class DefaultSelectTaskDisplayAreaFunction implements java.util.function.Function<android.os.Bundle, com.android.server.wm.TaskDisplayArea> {
        private final com.android.server.wm.TaskDisplayArea mDefaultTaskDisplayArea;
        private final int mDisplayId;

        DefaultSelectTaskDisplayAreaFunction(com.android.server.wm.TaskDisplayArea defaultTaskDisplayArea) {
            this.mDefaultTaskDisplayArea = defaultTaskDisplayArea;
            this.mDisplayId = defaultTaskDisplayArea.getDisplayId();
        }

        @Override // java.util.function.Function
        public com.android.server.wm.TaskDisplayArea apply(android.os.Bundle options) {
            if (options == null) {
                return this.mDefaultTaskDisplayArea;
            }
            android.app.ActivityOptions activityOptions = new android.app.ActivityOptions(options);
            android.window.WindowContainerToken tdaToken = activityOptions.getLaunchTaskDisplayArea();
            if (tdaToken == null) {
                return this.mDefaultTaskDisplayArea;
            }
            com.android.server.wm.TaskDisplayArea tda = com.android.server.wm.WindowContainer.fromBinder(tdaToken.asBinder()).asTaskDisplayArea();
            if (tda == null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[3]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(tdaToken);
                    com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 4917824058925068521L, 0, null, protoLogParam0);
                }
                return this.mDefaultTaskDisplayArea;
            }
            if (tda.getDisplayId() != this.mDisplayId) {
                throw new java.lang.IllegalArgumentException("The specified TaskDisplayArea must attach to Display#" + this.mDisplayId + ", but it is in Display#" + tda.getDisplayId());
            }
            return tda;
        }
    }

    static class HierarchyBuilder {
        private static final int LEAF_TYPE_IME_CONTAINERS = 2;
        private static final int LEAF_TYPE_TASK_CONTAINERS = 1;
        private static final int LEAF_TYPE_TOKENS = 0;
        private com.android.server.wm.DisplayArea.Tokens mImeContainer;
        private final com.android.server.wm.RootDisplayArea mRoot;
        private final java.util.ArrayList<com.android.server.wm.DisplayAreaPolicyBuilder.Feature> mFeatures = new java.util.ArrayList<>();
        private final java.util.ArrayList<com.android.server.wm.TaskDisplayArea> mTaskDisplayAreas = new java.util.ArrayList<>();

        HierarchyBuilder(com.android.server.wm.RootDisplayArea root) {
            this.mRoot = root;
        }

        com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder addFeature(com.android.server.wm.DisplayAreaPolicyBuilder.Feature feature) {
            this.mFeatures.add(feature);
            return this;
        }

        com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder setTaskDisplayAreas(java.util.List<com.android.server.wm.TaskDisplayArea> taskDisplayAreas) {
            this.mTaskDisplayAreas.clear();
            this.mTaskDisplayAreas.addAll(taskDisplayAreas);
            return this;
        }

        com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder setImeContainer(com.android.server.wm.DisplayArea.Tokens imeContainer) {
            this.mImeContainer = imeContainer;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void build() {
            build(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void build(java.util.List<com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder> displayAreaGroupHierarchyBuilders) {
            com.android.server.policy.WindowManagerPolicy policy = this.mRoot.mWmService.mPolicy;
            int maxWindowLayerCount = policy.getMaxWindowLayer() + 1;
            com.android.server.wm.DisplayArea.Tokens[] displayAreaForLayer = new com.android.server.wm.DisplayArea.Tokens[maxWindowLayerCount];
            java.util.Map<com.android.server.wm.DisplayAreaPolicyBuilder.Feature, java.util.List<com.android.server.wm.DisplayArea<com.android.server.wm.WindowContainer>>> featureAreas = new android.util.ArrayMap<>(this.mFeatures.size());
            for (int i = 0; i < this.mFeatures.size(); i++) {
                featureAreas.put(this.mFeatures.get(i), new java.util.ArrayList<>());
            }
            com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea[] areaForLayer = new com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea[maxWindowLayerCount];
            com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea root = new com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea(null, 0, null);
            java.util.Arrays.fill(areaForLayer, root);
            int size = this.mFeatures.size();
            for (int i2 = 0; i2 < size; i2++) {
                com.android.server.wm.DisplayAreaPolicyBuilder.Feature feature = this.mFeatures.get(i2);
                com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea featureArea = null;
                for (int layer = 0; layer < maxWindowLayerCount; layer++) {
                    if (feature.mWindowLayers[layer]) {
                        if (featureArea == null || featureArea.mParent != areaForLayer[layer]) {
                            featureArea = new com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea(feature, layer, areaForLayer[layer]);
                            areaForLayer[layer].mChildren.add(featureArea);
                        }
                        areaForLayer[layer] = featureArea;
                    } else {
                        featureArea = null;
                    }
                }
            }
            com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea leafArea = null;
            int leafType = 0;
            for (int layer2 = 0; layer2 < maxWindowLayerCount; layer2++) {
                int type = typeOfLayer(policy, layer2);
                if (leafArea == null || leafArea.mParent != areaForLayer[layer2] || type != leafType) {
                    leafArea = new com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea(null, layer2, areaForLayer[layer2]);
                    areaForLayer[layer2].mChildren.add(leafArea);
                    leafType = type;
                    if (leafType == 1) {
                        addTaskDisplayAreasToApplicationLayer(areaForLayer[layer2]);
                        addDisplayAreaGroupsToApplicationLayer(areaForLayer[layer2], displayAreaGroupHierarchyBuilders);
                        leafArea.mSkipTokens = true;
                    } else if (leafType == 2) {
                        leafArea.mExisting = this.mImeContainer;
                        leafArea.mSkipTokens = true;
                    }
                }
                leafArea.mMaxLayer = layer2;
            }
            root.computeMaxLayer();
            root.instantiateChildren(this.mRoot, displayAreaForLayer, 0, featureAreas);
            this.mRoot.onHierarchyBuilt(this.mFeatures, displayAreaForLayer, featureAreas);
        }

        private void addTaskDisplayAreasToApplicationLayer(com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea parentPendingArea) {
            int count = this.mTaskDisplayAreas.size();
            for (int i = 0; i < count; i++) {
                com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea leafArea = new com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea(null, 2, parentPendingArea);
                leafArea.mExisting = this.mTaskDisplayAreas.get(i);
                leafArea.mMaxLayer = 2;
                parentPendingArea.mChildren.add(leafArea);
            }
        }

        private void addDisplayAreaGroupsToApplicationLayer(com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea parentPendingArea, java.util.List<com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder> displayAreaGroupHierarchyBuilders) {
            if (displayAreaGroupHierarchyBuilders == null) {
                return;
            }
            int count = displayAreaGroupHierarchyBuilders.size();
            for (int i = 0; i < count; i++) {
                com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea leafArea = new com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea(null, 2, parentPendingArea);
                leafArea.mExisting = displayAreaGroupHierarchyBuilders.get(i).mRoot;
                leafArea.mMaxLayer = 2;
                parentPendingArea.mChildren.add(leafArea);
            }
        }

        boolean hasValidWindowingLayer() {
            return !this.mFeatures.isEmpty() && com.android.server.wm.DisplayAreaPolicyBuilder.canBeWindowingLayer(this.mFeatures.get(0).mId);
        }

        private static int typeOfLayer(com.android.server.policy.WindowManagerPolicy policy, int layer) {
            if (layer == 2) {
                return 1;
            }
            if (layer == policy.getWindowLayerFromTypeLw(2011) || layer == policy.getWindowLayerFromTypeLw(2012)) {
                return 2;
            }
            return 0;
        }
    }

    static class Feature {
        private final int mId;
        private final java.lang.String mName;
        private final com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier mNewDisplayAreaSupplier;
        private final boolean[] mWindowLayers;

        private Feature(java.lang.String name, int id, boolean[] windowLayers, com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier newDisplayAreaSupplier) {
            this.mName = name;
            this.mId = id;
            this.mWindowLayers = windowLayers;
            this.mNewDisplayAreaSupplier = newDisplayAreaSupplier;
        }

        public int getId() {
            return this.mId;
        }

        public java.lang.String toString() {
            return "Feature(\"" + this.mName + "\", " + this.mId + '}';
        }

        static class Builder {
            private final int mId;
            private final boolean[] mLayers;
            private final java.lang.String mName;
            private final com.android.server.policy.WindowManagerPolicy mPolicy;
            private com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier mNewDisplayAreaSupplier = new com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier() { // from class: com.android.server.wm.DisplayAreaPolicyBuilder$Feature$Builder$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier
                public final com.android.server.wm.DisplayArea create(com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.DisplayArea.Type type, java.lang.String str, int i) {
                    return new com.android.server.wm.DisplayArea(windowManagerService, type, str, i);
                }
            };
            private boolean mExcludeRoundedCorner = true;

            Builder(com.android.server.policy.WindowManagerPolicy policy, java.lang.String name, int id) {
                this.mPolicy = policy;
                this.mName = name;
                this.mId = id;
                this.mLayers = new boolean[this.mPolicy.getMaxWindowLayer() + 1];
            }

            com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder all() {
                java.util.Arrays.fill(this.mLayers, true);
                return this;
            }

            com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder and(int... types) {
                for (int type : types) {
                    set(type, true);
                }
                return this;
            }

            com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder except(int... types) {
                for (int type : types) {
                    set(type, false);
                }
                return this;
            }

            com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder upTo(int typeInclusive) {
                int max = layerFromType(typeInclusive, false);
                for (int i = 0; i < max; i++) {
                    this.mLayers[i] = true;
                }
                set(typeInclusive, true);
                return this;
            }

            com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder setNewDisplayAreaSupplier(com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier newDisplayAreaSupplier) {
                this.mNewDisplayAreaSupplier = newDisplayAreaSupplier;
                return this;
            }

            com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder setExcludeRoundedCornerOverlay(boolean excludeRoundedCorner) {
                this.mExcludeRoundedCorner = excludeRoundedCorner;
                return this;
            }

            com.android.server.wm.DisplayAreaPolicyBuilder.Feature build() {
                if (this.mExcludeRoundedCorner) {
                    this.mLayers[this.mPolicy.getMaxWindowLayer()] = false;
                }
                return new com.android.server.wm.DisplayAreaPolicyBuilder.Feature(this.mName, this.mId, (boolean[]) this.mLayers.clone(), this.mNewDisplayAreaSupplier);
            }

            private void set(int type, boolean value) {
                this.mLayers[layerFromType(type, true)] = value;
                if (type == 2038) {
                    this.mLayers[layerFromType(type, true)] = value;
                    this.mLayers[layerFromType(2003, false)] = value;
                    this.mLayers[layerFromType(2006, false)] = value;
                    this.mLayers[layerFromType(2010, false)] = value;
                }
            }

            private int layerFromType(int type, boolean internalWindows) {
                return this.mPolicy.getWindowLayerFromTypeLw(type, internalWindows);
            }
        }
    }

    static class Result extends com.android.server.wm.DisplayAreaPolicy {
        private final com.android.server.wm.TaskDisplayArea mDefaultTaskDisplayArea;
        final java.util.List<com.android.server.wm.RootDisplayArea> mDisplayAreaGroupRoots;
        final java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, com.android.server.wm.RootDisplayArea> mSelectRootForWindowFunc;
        private final java.util.function.Function<android.os.Bundle, com.android.server.wm.TaskDisplayArea> mSelectTaskDisplayAreaFunc;

        Result(com.android.server.wm.WindowManagerService wmService, com.android.server.wm.RootDisplayArea root, java.util.List<com.android.server.wm.RootDisplayArea> displayAreaGroupRoots, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, com.android.server.wm.RootDisplayArea> selectRootForWindowFunc, java.util.function.Function<android.os.Bundle, com.android.server.wm.TaskDisplayArea> selectTaskDisplayAreaFunc) {
            java.util.function.Function<android.os.Bundle, com.android.server.wm.TaskDisplayArea> defaultSelectTaskDisplayAreaFunction;
            super(wmService, root);
            this.mDisplayAreaGroupRoots = java.util.Collections.unmodifiableList(displayAreaGroupRoots);
            this.mSelectRootForWindowFunc = selectRootForWindowFunc;
            this.mDefaultTaskDisplayArea = (com.android.server.wm.TaskDisplayArea) this.mRoot.getItemFromTaskDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.DisplayAreaPolicyBuilder$Result$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.wm.DisplayAreaPolicyBuilder.Result.lambda$new$0((com.android.server.wm.TaskDisplayArea) obj);
                }
            });
            if (this.mDefaultTaskDisplayArea == null) {
                throw new java.lang.IllegalStateException("No display area with FEATURE_DEFAULT_TASK_CONTAINER");
            }
            if (selectTaskDisplayAreaFunc != null) {
                defaultSelectTaskDisplayAreaFunction = selectTaskDisplayAreaFunc;
            } else {
                defaultSelectTaskDisplayAreaFunction = new com.android.server.wm.DisplayAreaPolicyBuilder.DefaultSelectTaskDisplayAreaFunction(this.mDefaultTaskDisplayArea);
            }
            this.mSelectTaskDisplayAreaFunc = defaultSelectTaskDisplayAreaFunction;
        }

        static /* synthetic */ com.android.server.wm.TaskDisplayArea lambda$new$0(com.android.server.wm.TaskDisplayArea taskDisplayArea) {
            if (taskDisplayArea.mFeatureId == 1) {
                return taskDisplayArea;
            }
            return null;
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public void addWindow(com.android.server.wm.WindowToken token) {
            com.android.server.wm.DisplayArea.Tokens area = findAreaForToken(token);
            area.addChild(token);
        }

        com.android.server.wm.DisplayArea.Tokens findAreaForToken(com.android.server.wm.WindowToken token) {
            return this.mSelectRootForWindowFunc.apply(java.lang.Integer.valueOf(token.windowType), token.mOptions).findAreaForTokenInLayer(token);
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public com.android.server.wm.DisplayArea.Tokens findAreaForWindowType(int type, android.os.Bundle options, boolean ownerCanManageAppTokens, boolean roundedCornerOverlay) {
            return this.mSelectRootForWindowFunc.apply(java.lang.Integer.valueOf(type), options).findAreaForWindowTypeInLayer(type, ownerCanManageAppTokens, roundedCornerOverlay);
        }

        java.util.List<com.android.server.wm.DisplayAreaPolicyBuilder.Feature> getFeatures() {
            java.util.Set<com.android.server.wm.DisplayAreaPolicyBuilder.Feature> features = new android.util.ArraySet<>();
            features.addAll(this.mRoot.mFeatures);
            for (int i = 0; i < this.mDisplayAreaGroupRoots.size(); i++) {
                features.addAll(this.mDisplayAreaGroupRoots.get(i).mFeatures);
            }
            return new java.util.ArrayList(features);
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public java.util.List<com.android.server.wm.DisplayArea<? extends com.android.server.wm.WindowContainer>> getDisplayAreas(int featureId) {
            java.util.List<com.android.server.wm.DisplayArea<? extends com.android.server.wm.WindowContainer>> displayAreas = new java.util.ArrayList<>();
            getDisplayAreas(this.mRoot, featureId, displayAreas);
            for (int i = 0; i < this.mDisplayAreaGroupRoots.size(); i++) {
                getDisplayAreas(this.mDisplayAreaGroupRoots.get(i), featureId, displayAreas);
            }
            return displayAreas;
        }

        private static void getDisplayAreas(com.android.server.wm.RootDisplayArea root, int featureId, java.util.List<com.android.server.wm.DisplayArea<? extends com.android.server.wm.WindowContainer>> displayAreas) {
            java.util.List<com.android.server.wm.DisplayAreaPolicyBuilder.Feature> features = root.mFeatures;
            for (int i = 0; i < features.size(); i++) {
                com.android.server.wm.DisplayAreaPolicyBuilder.Feature feature = features.get(i);
                if (feature.mId == featureId) {
                    displayAreas.addAll(root.mFeatureToDisplayAreas.get(feature));
                }
            }
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public com.android.server.wm.DisplayArea<? extends com.android.server.wm.WindowContainer> getWindowingArea() {
            if (this.mRoot.mFeatures.isEmpty()) {
                throw new java.lang.IllegalStateException("There must be at least one feature.");
            }
            com.android.server.wm.DisplayAreaPolicyBuilder.Feature feature = this.mRoot.mFeatures.get(0);
            if (com.android.server.wm.DisplayAreaPolicyBuilder.canBeWindowingLayer(feature.mId)) {
                java.util.List<com.android.server.wm.DisplayArea<com.android.server.wm.WindowContainer>> areas = this.mRoot.mFeatureToDisplayAreas.get(feature);
                if (areas.size() == 1) {
                    return areas.get(0);
                }
            }
            throw new java.lang.IllegalStateException("There must be exactly one DisplayArea at top for the FEATURE_WINDOWED_MAGNIFICATION or FEATURE_WINDOWING_LAYER");
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public com.android.server.wm.TaskDisplayArea getDefaultTaskDisplayArea() {
            return this.mDefaultTaskDisplayArea;
        }

        @Override // com.android.server.wm.DisplayAreaPolicy
        public com.android.server.wm.TaskDisplayArea getTaskDisplayArea(android.os.Bundle options) {
            return this.mSelectTaskDisplayAreaFunc.apply(options);
        }
    }

    static class PendingArea {
        com.android.server.wm.DisplayArea mExisting;
        final com.android.server.wm.DisplayAreaPolicyBuilder.Feature mFeature;
        int mMaxLayer;
        final int mMinLayer;
        final com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea mParent;
        final java.util.ArrayList<com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea> mChildren = new java.util.ArrayList<>();
        boolean mSkipTokens = false;

        PendingArea(com.android.server.wm.DisplayAreaPolicyBuilder.Feature feature, int minLayer, com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea parent) {
            this.mMinLayer = minLayer;
            this.mFeature = feature;
            this.mParent = parent;
        }

        int computeMaxLayer() {
            for (int i = 0; i < this.mChildren.size(); i++) {
                this.mMaxLayer = java.lang.Math.max(this.mMaxLayer, this.mChildren.get(i).computeMaxLayer());
            }
            int i2 = this.mMaxLayer;
            return i2;
        }

        void instantiateChildren(com.android.server.wm.DisplayArea<com.android.server.wm.DisplayArea> parent, com.android.server.wm.DisplayArea.Tokens[] areaForLayer, int level, java.util.Map<com.android.server.wm.DisplayAreaPolicyBuilder.Feature, java.util.List<com.android.server.wm.DisplayArea<com.android.server.wm.WindowContainer>>> areas) {
            this.mChildren.sort(java.util.Comparator.comparingInt(new java.util.function.ToIntFunction() { // from class: com.android.server.wm.DisplayAreaPolicyBuilder$PendingArea$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(java.lang.Object obj) {
                    return ((com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea) obj).mMinLayer;
                }
            }));
            for (int i = 0; i < this.mChildren.size(); i++) {
                com.android.server.wm.DisplayAreaPolicyBuilder.PendingArea pendingArea = this.mChildren.get(i);
                com.android.server.wm.DisplayArea displayAreaCreateArea = pendingArea.createArea(parent, areaForLayer);
                if (displayAreaCreateArea != null) {
                    parent.addChild(displayAreaCreateArea, Integer.MAX_VALUE);
                    if (pendingArea.mFeature != null) {
                        areas.get(pendingArea.mFeature).add(displayAreaCreateArea);
                    }
                    pendingArea.instantiateChildren(displayAreaCreateArea, areaForLayer, level + 1, areas);
                }
            }
        }

        private com.android.server.wm.DisplayArea createArea(com.android.server.wm.DisplayArea<com.android.server.wm.DisplayArea> parent, com.android.server.wm.DisplayArea.Tokens[] areaForLayer) {
            com.android.server.wm.DisplayArea.Type type;
            if (this.mExisting != null) {
                if (this.mExisting.asTokens() != null) {
                    fillAreaForLayers(this.mExisting.asTokens(), areaForLayer);
                }
                return this.mExisting;
            }
            if (this.mSkipTokens) {
                return null;
            }
            if (this.mMinLayer > 2) {
                type = com.android.server.wm.DisplayArea.Type.ABOVE_TASKS;
            } else if (this.mMaxLayer < 2) {
                type = com.android.server.wm.DisplayArea.Type.BELOW_TASKS;
            } else {
                type = com.android.server.wm.DisplayArea.Type.ANY;
            }
            if (this.mFeature == null) {
                com.android.server.wm.DisplayArea.Tokens leaf = new com.android.server.wm.DisplayArea.Tokens(parent.mWmService, type, "Leaf:" + this.mMinLayer + ":" + this.mMaxLayer);
                fillAreaForLayers(leaf, areaForLayer);
                return leaf;
            }
            return this.mFeature.mNewDisplayAreaSupplier.create(parent.mWmService, type, this.mFeature.mName + ":" + this.mMinLayer + ":" + this.mMaxLayer, this.mFeature.mId);
        }

        private void fillAreaForLayers(com.android.server.wm.DisplayArea.Tokens leaf, com.android.server.wm.DisplayArea.Tokens[] areaForLayer) {
            for (int i = this.mMinLayer; i <= this.mMaxLayer; i++) {
                areaForLayer[i] = leaf;
            }
        }
    }
}
