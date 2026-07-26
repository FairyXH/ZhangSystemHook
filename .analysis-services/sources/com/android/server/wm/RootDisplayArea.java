package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class RootDisplayArea extends com.android.server.wm.DisplayArea.Dimmable {
    private com.android.server.wm.DisplayArea.Tokens[] mAreaForLayer;
    java.util.Map<com.android.server.wm.DisplayAreaPolicyBuilder.Feature, java.util.List<com.android.server.wm.DisplayArea<com.android.server.wm.WindowContainer>>> mFeatureToDisplayAreas;
    java.util.List<com.android.server.wm.DisplayAreaPolicyBuilder.Feature> mFeatures;
    private boolean mHasBuiltHierarchy;

    RootDisplayArea(com.android.server.wm.WindowManagerService wms, java.lang.String name, int featureId) {
        super(wms, com.android.server.wm.DisplayArea.Type.ANY, name, featureId);
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.RootDisplayArea getRootDisplayArea() {
        return this;
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.RootDisplayArea asRootDisplayArea() {
        return this;
    }

    boolean isOrientationDifferentFromDisplay() {
        return false;
    }

    boolean placeImeContainer(com.android.server.wm.DisplayArea.Tokens imeContainer) {
        com.android.server.wm.RootDisplayArea previousRoot = imeContainer.getRootDisplayArea();
        java.util.List<com.android.server.wm.DisplayAreaPolicyBuilder.Feature> features = this.mFeatures;
        for (int i = 0; i < features.size(); i++) {
            com.android.server.wm.DisplayAreaPolicyBuilder.Feature feature = features.get(i);
            if (feature.getId() == 7) {
                java.util.List<com.android.server.wm.DisplayArea<com.android.server.wm.WindowContainer>> imeDisplayAreas = this.mFeatureToDisplayAreas.get(feature);
                if (imeDisplayAreas.size() != 1) {
                    throw new java.lang.IllegalStateException("There must be exactly one DisplayArea for the FEATURE_IME_PLACEHOLDER");
                }
                previousRoot.updateImeContainerForLayers(null);
                imeContainer.reparent(imeDisplayAreas.get(0), Integer.MAX_VALUE);
                updateImeContainerForLayers(imeContainer);
                return true;
            }
        }
        if (!isDescendantOf(previousRoot)) {
            android.util.Slog.w("WindowManager", "The IME target is not in the same root as the IME container, but there is no DisplayArea of FEATURE_IME_PLACEHOLDER in the target RootDisplayArea");
        }
        return false;
    }

    com.android.server.wm.DisplayArea.Tokens findAreaForTokenInLayer(com.android.server.wm.WindowToken token) {
        return findAreaForWindowTypeInLayer(token.windowType, token.mOwnerCanManageAppTokens, token.mRoundedCornerOverlay);
    }

    com.android.server.wm.DisplayArea.Tokens findAreaForWindowTypeInLayer(int windowType, boolean ownerCanManageAppTokens, boolean roundedCornerOverlay) {
        int windowLayerFromType = this.mWmService.mPolicy.getWindowLayerFromTypeLw(windowType, ownerCanManageAppTokens, roundedCornerOverlay);
        if (windowLayerFromType == 2) {
            throw new java.lang.IllegalArgumentException("There shouldn't be WindowToken on APPLICATION_LAYER");
        }
        return this.mAreaForLayer[windowLayerFromType];
    }

    void onHierarchyBuilt(java.util.ArrayList<com.android.server.wm.DisplayAreaPolicyBuilder.Feature> features, com.android.server.wm.DisplayArea.Tokens[] areaForLayer, java.util.Map<com.android.server.wm.DisplayAreaPolicyBuilder.Feature, java.util.List<com.android.server.wm.DisplayArea<com.android.server.wm.WindowContainer>>> featureToDisplayAreas) {
        if (this.mHasBuiltHierarchy) {
            throw new java.lang.IllegalStateException("Root should only build the hierarchy once");
        }
        this.mHasBuiltHierarchy = true;
        this.mFeatures = java.util.Collections.unmodifiableList(features);
        this.mAreaForLayer = areaForLayer;
        this.mFeatureToDisplayAreas = featureToDisplayAreas;
    }

    private void updateImeContainerForLayers(com.android.server.wm.DisplayArea.Tokens imeContainer) {
        com.android.server.policy.WindowManagerPolicy policy = this.mWmService.mPolicy;
        this.mAreaForLayer[policy.getWindowLayerFromTypeLw(2011)] = imeContainer;
        this.mAreaForLayer[policy.getWindowLayerFromTypeLw(2012)] = imeContainer;
    }
}
