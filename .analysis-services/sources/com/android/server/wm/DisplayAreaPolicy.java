package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public abstract class DisplayAreaPolicy {
    protected final com.android.server.wm.RootDisplayArea mRoot;
    protected final com.android.server.wm.WindowManagerService mWmService;

    public abstract void addWindow(com.android.server.wm.WindowToken windowToken);

    public abstract com.android.server.wm.DisplayArea.Tokens findAreaForWindowType(int i, android.os.Bundle bundle, boolean z, boolean z2);

    public abstract com.android.server.wm.TaskDisplayArea getDefaultTaskDisplayArea();

    public abstract java.util.List<com.android.server.wm.DisplayArea<? extends com.android.server.wm.WindowContainer>> getDisplayAreas(int i);

    public abstract com.android.server.wm.TaskDisplayArea getTaskDisplayArea(android.os.Bundle bundle);

    public abstract com.android.server.wm.DisplayArea<? extends com.android.server.wm.WindowContainer> getWindowingArea();

    protected DisplayAreaPolicy(com.android.server.wm.WindowManagerService wmService, com.android.server.wm.RootDisplayArea root) {
        this.mWmService = wmService;
        this.mRoot = root;
    }

    static final class DefaultProvider implements com.android.server.wm.DisplayAreaPolicy.Provider {
        DefaultProvider() {
        }

        @Override // com.android.server.wm.DisplayAreaPolicy.Provider
        public com.android.server.wm.DisplayAreaPolicy instantiate(com.android.server.wm.WindowManagerService wmService, com.android.server.wm.DisplayContent content, com.android.server.wm.RootDisplayArea root, com.android.server.wm.DisplayArea.Tokens imeContainer) {
            com.android.server.wm.TaskDisplayArea defaultTaskDisplayArea = new com.android.server.wm.TaskDisplayArea(content, wmService, "DefaultTaskDisplayArea", 1);
            java.util.List<com.android.server.wm.TaskDisplayArea> tdaList = new java.util.ArrayList<>();
            tdaList.add(defaultTaskDisplayArea);
            com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder rootHierarchy = new com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder(root);
            rootHierarchy.setImeContainer(imeContainer).setTaskDisplayAreas(tdaList);
            if (content.isTrusted()) {
                configureTrustedHierarchyBuilder(rootHierarchy, wmService, content);
            }
            return new com.android.server.wm.DisplayAreaPolicyBuilder().setRootHierarchy(rootHierarchy).build(wmService);
        }

        private void configureTrustedHierarchyBuilder(com.android.server.wm.DisplayAreaPolicyBuilder.HierarchyBuilder rootHierarchy, com.android.server.wm.WindowManagerService wmService, com.android.server.wm.DisplayContent content) {
            rootHierarchy.addFeature(new com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder(wmService.mPolicy, "WindowedMagnification", 4).upTo(2039).except(2039).setNewDisplayAreaSupplier(new com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier() { // from class: com.android.server.wm.DisplayAreaPolicy$DefaultProvider$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.DisplayAreaPolicyBuilder.NewDisplayAreaSupplier
                public final com.android.server.wm.DisplayArea create(com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.DisplayArea.Type type, java.lang.String str, int i) {
                    return new com.android.server.wm.DisplayArea.Dimmable(windowManagerService, type, str, i);
                }
            }).build());
            if (content.isDefaultDisplay) {
                rootHierarchy.addFeature(new com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder(wmService.mPolicy, "HideDisplayCutout", 6).all().except(2019, 2024, 2000, 2040).build()).addFeature(new com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder(wmService.mPolicy, "OneHanded", 3).all().except(2019, 2024, 2015).build());
            }
            rootHierarchy.addFeature(new com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder(wmService.mPolicy, "FullscreenMagnification", 5).all().except(2039, 2011, 2012, 2027, 2019, 2024).build()).addFeature(new com.android.server.wm.DisplayAreaPolicyBuilder.Feature.Builder(wmService.mPolicy, "ImePlaceholder", 7).and(2011, 2012).build());
        }
    }

    public interface Provider {
        com.android.server.wm.DisplayAreaPolicy instantiate(com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.DisplayContent displayContent, com.android.server.wm.RootDisplayArea rootDisplayArea, com.android.server.wm.DisplayArea.Tokens tokens);

        static com.android.server.wm.DisplayAreaPolicy.Provider fromResources(android.content.res.Resources res) {
            java.lang.String name = res.getString(android.R.string.config_dreamsDefaultComponent);
            if (android.text.TextUtils.isEmpty(name)) {
                return new com.android.server.wm.DisplayAreaPolicy.DefaultProvider();
            }
            try {
                return (com.android.server.wm.DisplayAreaPolicy.Provider) java.lang.Class.forName(name).newInstance();
            } catch (java.lang.ClassCastException | java.lang.ReflectiveOperationException e) {
                throw new java.lang.IllegalStateException("Couldn't instantiate class " + name + " for config_deviceSpecificDisplayAreaPolicyProvider: make sure it has a public zero-argument constructor and implements DisplayAreaPolicy.Provider", e);
            }
        }
    }
}
